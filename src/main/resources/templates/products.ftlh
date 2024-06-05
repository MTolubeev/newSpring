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
      <a href="/basket/hi/${user.id}">View Basket</a>
      <#if user.isAdmin()>
             <a href="/admin">Панель администратора</a>
         </#if>
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

</body>
</html>