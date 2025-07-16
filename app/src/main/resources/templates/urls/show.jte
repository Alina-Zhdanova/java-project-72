@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
content = @`
    <div>

        <h1>Сайты</h1>

        <table>

            <tr>
                <th>
                    ID
                </th>
                <td>
                    ${page.getUrl().getId()}
                </td>
            </tr>

            <tr>
                <th>
                    Имя
                </th>
                <td>
                    ${page.getUrl().getName()}
                </td>
            </tr>

            <tr>
                <th>
                    Дата создания
                </th>
                <td>
                    ${page.getUrl().getCreatedAt().toString()}
                </td>
            </tr>

        </table>

    </div>
    `
)
