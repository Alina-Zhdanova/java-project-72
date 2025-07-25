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
import java.util.LinkedHashMap;

import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
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

    public static void index(Context ctx) {
        var urls = UrlRepository.getEntities();
        var urlsAndLastCheck = new LinkedHashMap<Url, UrlCheck>();
        for (var url : urls) {
            if (!UrlCheckRepository.find(url.getId()).isEmpty()) {
                var lastCheck = UrlCheckRepository.find(url.getId()).getFirst();
                urlsAndLastCheck.put(url, lastCheck);
            }
        }
        var page = new UrlsPage(urls, urlsAndLastCheck);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
            .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var checks = UrlCheckRepository.find(id);
        var page = new UrlPage(url, checks);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) {

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

    public static void checks(Context ctx) {

        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
            .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        HttpResponse<String> response = Unirest.get(url.getName()).asString();

        var statusCode = response.getStatus();

        var body = response.getBody();
        Document documentBody = Jsoup.parse(body);

        var title = documentBody.title();
        var h1Element = documentBody.selectFirst("h1");
        var h1 = h1Element != null ? h1Element.text() : "";
        var descriptionElement = documentBody.selectFirst("meta[name=description]");
        var description = descriptionElement != null ? descriptionElement.attr("content") : "";

        var urlCheck = new UrlCheck(id, statusCode, title, h1, description);
        UrlCheckRepository.save(urlCheck);

        ctx.redirect(NamedRoutes.urlPath(id));

    }
}
