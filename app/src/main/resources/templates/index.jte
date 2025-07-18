@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage

@param BasePage page

@template.layout.page(
content = @`
    <div class="min-vh-100 d-flex flex-column justify-content-center align-items-center text-center bg-light">

        @if(page.getFlash() != null)
            <div class="alert alert-${page.getFlashType()} alert-dismissible fade show w-75 mb-4" role="alert">
                ${page.getFlash()}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        @endif

        <div class="card shadow-lg p-5 rounded-3 bg-white w-75 mx-auto">
            <div class="card-body">
                <h1 class="card-title display-4 fw-bold mb-3 text-dark">Анализатор страниц</h1>
                <p class="card-text lead text-secondary mb-4">Бесплатно проверяйте сайты на SEO пригодность</p>

                <form action="${NamedRoutes.urlsPath()}" method="post" class="d-flex justify-content-center mb-3">
                    <div class="input-group input-group-lg w-75">
                        <input type="text" name="url" class="form-control border-0 shadow-sm" placeholder="Введите URL" aria-label="Введите URL" required>
                        <button type="submit" class="btn btn-info shadow-sm px-4">Проверить</button>
                    </div>
                </form>

                <p class="text-muted small">Пример: https://www.example.com</p>
            </div>
        </div>
    </div>
`
)
