<!DOCTYPE html>
<html>
<head>
<title>POMOGITE</title>
</head>
<body>
<h1>hiiiii</h1><hr>
<h4>PODROBNO</h4>



<#list images as img>
    <img src="/images/${img.id}" height="60px"/><br><br>
</#list>


<b>Nazvanie </b>${product.title}<br>
<b>Opis </b>${product.description}<br>
<b>Cena </b>${product.price}<br>
<b>Kolvo </b>${product.count}<br>
<form action="/basket/adding" method="post">
    <input type="hidden" name="id" value="${product.id}">
    <input type="hidden" name="title" value="${product.title}">
    <input type="hidden" name="description" value="${product.description}">
    <input type="hidden" name="price" value="${product.price}">
    <input type="hidden" name="count" value="${product.count}">
<#if user??>
<p>user is present: ${user}</p>
 <input type="hidden" name="uid" value="${user.id}">
    <input type="hidden" name="email" value="${user.email}">
    <input type="hidden" name="phoneNumber" value="${user.phoneNumber}">
    <input type="hidden" name="name" value="${user.name}">
<#else>
<p>User unknown</p>
</#if>

    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Add v korzinochky"/>
</form>
</body>
</html>