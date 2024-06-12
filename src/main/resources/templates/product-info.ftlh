<!DOCTYPE html>
<html>
<head>
<title>Shop</title>
</head>
<body>
<h1>Подробнее о товаре</h1><hr>




<#list images as img>
    <img src="/images/${img.id}" height="60px"/><br><br>
</#list>


<b>Навзание </b>${product.title}<br>
<b>Описание </b>${product.description}<br>
<b>Цена </b>${product.price}<br>

<form action="/basket/${user.id}" method="post">
    <input type="hidden" name="productId" value="${product.id}">
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <#if product.count gte 6>
    <h3>В наличии</h3>
    <input type="submit" value="Add v korzinochky"/>
    <#elseif product.count lte 5 && product.count != 0>
    <h3>В наличии</h3>
        <input type="submit" value="Add v korzinochky"/>
    <#else>
    <h3>Нет в наличии</h3>
    </#if>
</form>
<#if user.isAdmin()>
<form action="/product/delete/${product.id}" method="post">
<input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Удалить товар"/>
</#if>
</form>
</body>
</html>