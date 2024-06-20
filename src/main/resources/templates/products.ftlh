<!DOCTYPE html>
<html>
<head>
    <title>Shop</title>
</head>
<body>
    <h1>Главная страница</h1>
    <hr>
    <#if user?? && user.email??>
        <h3>Имя пользователя: <i>${user.surname} ${firstLetterName}. </i></h3>


        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <input type="submit" value="Выйти"/>
        </form>
        <a href="/basket/${user.id}">Корзина(${basketSize?c})</a>
        <#if user.isAdmin()>
            <a href="/admin">Панель администратора</a>
        </#if>
    <#else>
        <a href="/login">Войти</a>
    </#if>
    <hr>
    <h4>Products we have</h4>
    <#list products?sort_by("id") as product>
        <div>


            <img src="/images/${product.previewImageId?c}"  height="60px"/><br><br>
            <b>Навзание</b> ${product.title}<br>
            <b>Описание</b> ${product.description}<br>
            <b>Цена</b> ${product.price?c}<br>
            <#if user?? && user.email??>
                <form action="/basket/${user.id}" method="post">
                    <input type="hidden" name="productId" value="${product.id?c}">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <#if product.count gte 6>
                        <b>В наличии</b><br>
                        <input type="submit" value="Добавить в корзину"/>
                    <#elseif product.count lte 5 && product.count != 0>
                        <b>Мало</b><br>
                        <input type="submit" value="Добавить в корзину"/>
                    <#else>
                        <b>Нет в наличии</b>
                    </#if>
                </form>
            </#if>
            <#if user.isAdmin()>
                <form action="/product/delete/${product.id?c}" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <input type="submit" value="Удалить товар"/>
                </form>
            </#if>
        </div>
    </#list>
    <#if user.isAdmin()>
        <h3>Создать новый товар</h3>
        <form action="/product/create" method="post" enctype="multipart/form-data">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            Название объявления: <input type="text" name="title"/><br><br>
            Описание объявления: <input type="text" name="description"/><br><br>
            Цена: <input type="number" name="price"/><br><br>
            Колличество: <input type="number" name="count"/><br><br>
            Первая фотография: <input type="file" name="file1"/><br><br>
            <input type="submit" value="Добавить товар"/>
        </form>
    </#if>
</body>
</html>
