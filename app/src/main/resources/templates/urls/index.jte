@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage

@param UrlsPage page

@template.layout.page(
    content = @`
        <div>

            @if(page.getFlash() != null)
                <p>${page.getFlash()}</p>
            @endif

            <h1>Сайты</h1>

            <table>

                <thead>
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Последняя проверка</th>
                    <th>Код ответа</th>
                </tr>
                </thead>

                <tbody>

                @for(var url : page.getUrls())

                    <tr>
                        <td>
                            ${url.getId()}
                        </td>
                        <td>
                            <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                        </td>
                        <td>
                            ${url.getCreatedAt().toString()}
                        </td>
                        <td>
                            200
                        </td>
                    </tr>

                @endfor

                </tbody>

            </table>

        </div>
    `
)
