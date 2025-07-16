package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context ctx) {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
            .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) {

        var enteredAddress = ctx.formParam("url");

        try {
            var nameToUrl = new URL(enteredAddress);
            var name = extractingDomainWithProtocolAndPort(nameToUrl);

            if (UrlRepository.find(name).isPresent()) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                return;
            }

            var url = new Url(name);
            UrlRepository.save(url);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");

        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
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
}
