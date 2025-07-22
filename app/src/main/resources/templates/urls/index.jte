@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage

@param UrlsPage page

@template.layout.page(
content = @`
    <div class="container py-4">

        @if(page.getFlash() != null)
            <div class="alert alert-${page.getFlashType()} alert-dismissible fade show mb-4 w-75 mx-auto" role="alert">
                ${page.getFlash()}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        @endif

        <h1 class="mt-4 mb-4 fw-bold text-dark text-center">Сайты</h1>

        <div class="table-responsive shadow-sm rounded-3 overflow-hidden">
            <table class="table table-hover table-striped border mb-0">
                <thead>
                <tr>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">ID</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">Имя</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">Последняя проверка</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">Код ответа</th>
                </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                    <tr>
                        <td class="py-3 px-4">${url.getId()}</td>
                        <td class="py-3 px-4">
                            <a href="${NamedRoutes.urlPath(url.getId())}" class="text-decoration-none text-primary fw-medium">${url.getName()}</a>
                        </td>
                        @if(page.getUrlsAndLastCheck().containsKey(url))
                            <td class="py-3 px-4">${page.getUrlsAndLastCheck().get(url).getFormattedCreatedAt()}</td>
                            <td class="py-3 px-4">
                                <span class="badge bg-success text-white rounded-pill">${page.getUrlsAndLastCheck().get(url).getStatusCode()}</span>
                            </td>
                        @else
                            <td class="py-3 px-4"></td>
                            <td class="py-3 px-4">
                                <span class="badge bg-success text-white rounded-pill"></span>
                            </td>
                        @endif
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
`
)
