package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) throws SQLException {
        var dataSource = getDataSource();
        var app = getApp(dataSource);
        app.start(7070);
    }

    public static DataSource getDataSource() throws SQLException {
        var jdbcUrl = System.getenv().getOrDefault("DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);

        var dataSource = new HikariDataSource(hikariConfig);

        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
            .lines().collect(Collectors.joining("\n"));

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }

        return dataSource;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    public static Javalin getApp(DataSource dataSource) {

        BaseRepository.dataSource = (HikariDataSource) dataSource;

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), UrlsController::root);
        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.post(NamedRoutes.urlChecksPath("{id}"), UrlsController::checks);

        return app;
    }
}
