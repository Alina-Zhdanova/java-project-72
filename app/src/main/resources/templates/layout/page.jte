@import gg.jte.Content
@import hexlet.code.util.NamedRoutes

@param Content content

<!doctype html>

<html lang="ru">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Анализатор страниц</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
</head>

<body>

<nav>
    <a href="${NamedRoutes.rootPath()}">Главная</a>
    <a href="${NamedRoutes.urlsPath()}">Сайты</a>
</nav>

<div>
    ${content}
</div>

</body>

</html>
