@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
content = @`
    <div class="container py-4">

        <h1 class="mt-4 mb-4 fw-bold text-dark text-center">Сайт: ${page.getUrl().getName()}</h1>

        <div class="card shadow-lg rounded-3 bg-white mx-auto w-75 mb-4">
            <div class="card-body">
                <table class="table table-borderless text-start mb-0">
                    <tbody>
                    <tr>
                        <th scope="row" class="fw-bold col-md-4 text-muted py-2">ID</th>
                        <td class="py-2">${page.getUrl().getId()}</td>
                    </tr>

                    <tr>
                        <th scope="row" class="fw-bold col-md-4 text-muted py-2">Имя</th>
                        <td class="py-2">${page.getUrl().getName()}</td>
                    </tr>

                    <tr>
                        <th scope="row" class="fw-bold col-md-4 text-muted py-2">Дата создания</th>
                        <td class="py-2">${page.getUrl().getCreatedAt().toString()}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="text-center">
            <a href="#" class="btn btn-info shadow-sm mt-3 px-4">Запустить проверку</a>
        </div>


        <h2 class="mt-5 mb-4 fw-bold text-dark text-center">Проверки</h2>

        <div class="table-responsive shadow-sm rounded-3 overflow-hidden">
            <table class="table table-hover table-striped border mb-0">
                <thead>
                <tr>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">ID</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">Код ответа</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">Title</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">H1</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">Description</th>
                    <th class="bg-light text-dark py-3 px-4 fw-bold">Дата проверки</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="py-3 px-4">1</td>
                    <td class="py-3 px-4"><span class="badge bg-success text-white rounded-pill">200</span></td>
                    <td class="py-3 px-4">Пример Title</td>
                    <td class="py-3 px-4">Пример H1</td>
                    <td class="py-3 px-4">Пример Description</td>
                    <td class="py-3 px-4">2024-07-17 10:00:00</td>
                </tr>
                <tr>
                    <td class="py-3 px-4">2</td>
                    <td class="py-3 px-4"><span class="badge bg-warning text-white rounded-pill">404</span></td>
                    <td class="py-3 px-4"></td>
                    <td class="py-3 px-4"></td>
                    <td class="py-3 px-4"></td>
                    <td class="py-3 px-4">2024-07-16 12:30:00</td>
                </tr>
                </tbody>
            </table>
        </div>

    </div>
`
)
