package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;

import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void root(Context ctx) {

        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));

        ctx.render("index.jte", model("page", page));

    }

    public static void index(Context ctx) throws SQLException {

        var urls = UrlRepository.getEntities();
        var checks = UrlCheckRepository.getEntitiesOrderedByCreatedAt();

        var urlsAndLastCheck = new HashMap<Url, UrlCheck>();

        for (var url : urls) {

            final var currentUrlId = url.getId();
            checks.stream()
                .filter(check -> check.getUrlId().equals(currentUrlId))
                .findFirst()
                .ifPresent(lastCheck -> urlsAndLastCheck.put(url, lastCheck));

        }

        var page = new UrlsPage(urls, urlsAndLastCheck);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", model("page", page));

    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
            .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var checks = UrlCheckRepository.find(id);
        checks.sort(Comparator.comparing(UrlCheck::getCreatedAt).reversed());
        var page = new UrlPage(url, checks);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {

        var enteredAddress = ctx.formParam("url");

        try {
            var nameToUrl = new URL(enteredAddress);
            var name = extractingDomainWithProtocolAndPort(nameToUrl);

            if (UrlRepository.find(name).isPresent()) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flashType", "warning");
                ctx.redirect(NamedRoutes.urlsPath());
                return;
            }

            var url = new Url(name);
            UrlRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");

        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        ctx.redirect(NamedRoutes.urlsPath());

    }

    private static String extractingDomainWithProtocolAndPort(URL url) {

        var protocol = url.getProtocol();
        var domain = url.getHost();
        var port = url.getPort();

        if (port != -1) {
            return protocol + "://" + domain + ":" + port;
        }

        return protocol + "://" + domain;

    }

    public static void checks(Context ctx) throws SQLException {

        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
            .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        var response = Unirest.get(url.getName()).asString();

        var statusCode = response.getStatus();

        var body = response.getBody();
        Document documentBody = Jsoup.parse(body);

        var title = documentBody.title() != null ? documentBody.title() : " ";
        var h1Element = documentBody.selectFirst("h1");
        var h1 = h1Element != null ? h1Element.text() : " ";
        var descriptionElement = documentBody.selectFirst("meta[name=description]");
        var description = descriptionElement != null ? descriptionElement.attr("content") : " ";

        var urlCheck = new UrlCheck(id, statusCode, title, h1, description);
        UrlCheckRepository.save(urlCheck);

        ctx.redirect(NamedRoutes.urlPath(id));

    }
}
