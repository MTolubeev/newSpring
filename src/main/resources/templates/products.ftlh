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
 <#if user.isAdmin()>
<h3>Создать новый товар</h3>
<form action="/product/create" method="post" enctype="multipart/form-data">
               <input type="hidden" name="_csrf" value="${_csrf.token}">
    Название объявления: <input type="text" name="title"/><br><br>
    Описание объявления: <input type="text" name="description"/><br><br>
    Цена: <input type="text" name="price"/><br><br>
  Колличество: <input type="number" name="count"/><br><br>
  Первая фотография: <input type="file" name="file1"/><br><br>
      Вторая фотография: <input type="file" name="file2"/><br><br>
      Третья фотография: <input type="file" name="file3"/><br><br>

    <input type="submit" value="Добавить товар"/>
</form>
         </#if>

</body>
</html>