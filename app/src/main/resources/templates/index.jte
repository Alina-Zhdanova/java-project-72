@import hexlet.code.util.NamedRoutes

@template.layout.page(
    content = @`
            <div>
                <h1>Анализатор страниц</h1>
                <p>Бесплатно проверяйте сайты на SEO пригодность</p>

                <form action="${NamedRoutes.urlsPath()}" method="post">
                    <label>
                        Ссылка
                        <input type="text" name="url">
                    </label>
                    <button type="submit">Проверить</button>
                </form>

                <p>Пример: https://www.example.com</p>
            </div>
    `
)

