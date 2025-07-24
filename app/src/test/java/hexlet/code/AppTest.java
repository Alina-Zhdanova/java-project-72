package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;

import hexlet.code.util.NamedRoutes;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import java.io.IOException;
import java.sql.SQLException;

public class AppTest {

    private Javalin app;
    private MockWebServer mockWebServer;

    @BeforeEach
    public final void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        app = App.getApp();
        UrlRepository.removeAll();
        UrlCheckRepository.removeAll();
    }

    @AfterEach
    public final void shutDown() {
        mockWebServer.close();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() {
        var url = new Url("https://www.test.com");
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.test.com";
            var url = "https://www.test.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);

            var addedUrl = UrlRepository.find(url);
            assertThat(addedUrl).isPresent();
            assertThat(addedUrl.get().getName()).isEqualTo(url);
        });
    }

    @Test
    public void testChecksUrl() {

        var mockServerUrl = mockWebServer.url("/test-url").toString();
        var url = new Url(mockServerUrl);
        UrlRepository.save(url);
        var urlId = url.getId();

        mockWebServer.enqueue(new MockResponse.Builder()
            .body("<html><head><title>" + "Заголовок" + "</title>"
                    + "<meta name=\"description\" content=\"" + "Описание" + "\">"
                    + "</head><body><h1>" + "H1" + "</h1></body></html>")
            .build());

        JavalinTest.test(app, (server, client) -> {
            var response = client.post(NamedRoutes.urlChecksPath(urlId));
            assertThat(response.code()).isEqualTo(200);

            var recordedRequest = mockWebServer.takeRequest();
            assertThat(recordedRequest.getUrl().url().getPath()).isEqualTo("/test-url");
            assertThat(recordedRequest.getMethod()).isEqualTo("GET");

            var lastCheck = UrlCheckRepository.find(url.getId()).getFirst();
            assertThat(lastCheck).isNotNull();
            assertThat(lastCheck.getStatusCode()).isEqualTo(200);
            assertThat(lastCheck.getTitle()).isEqualTo("Заголовок");
            assertThat(lastCheck.getDescription()).isEqualTo("Описание");
            assertThat(lastCheck.getH1()).isEqualTo("H1");
        });
    }
}
