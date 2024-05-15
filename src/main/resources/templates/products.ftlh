<!DOCTYPE html>
<html>
<head>
<title>POMOGITE</title>
</head>
<body>
<h1>hiiiii </h1><hr>
 <#if user.email??>
     <h3>Имя пользователя: <i>${user.name}</i></h3>
     <form action="/logout" method="post">
         <input type="hidden" name="_csrf" value="${_csrf.token}">
         <input type="submit" value="Выйти"/>
     </form>
 <#else>
     <a href="/login">Войти</a></h1>
 </#if>
 <hr>
<h4>products we have</h4>
<form action="/" method="get">
poisk: <input type ="text" name ="title"><br>
<input type="submit" value="poisk"/>
</form>
<#list products as product >
<div>
<p>${product.title} ${product.price} | <a href= "/product/${product.id}">More</a></p>
</div>
<#else>
<h3>Net tovara</h3>
</#list>
<#if user.email??>
<hr>
<h3> Make new</h3>
<form action="/product/create" method="post" enctype="multipart/form-data">
Nazvanie <input type="text" name="title"/><br><br>
Opis <input type="text" name="description"/><br><br>
Price <input type="number" name="price"/><br><br>
kolvo <input type="number" name="count"/><br><br>

1 Photo <input type="file" name="file1"/><br><br>
2 Photo <input type="file" name="file2"/><br><br>
3 Photo <input type="file" name="file3"/><br><br>
<input type ="hidden" name = "_csrf" value="${_csrf.token}">

<input type="submit" value="add prod"/>
</form>
</#if>
</body>
</html>