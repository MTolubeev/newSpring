<!DOCTYPE html>
<html>
<head>
<title>POMOGITE</title>
</head>
<body>
<h1>hiiiii</h1><hr>
<h4>products we have</h4>
<#list products as product >
<div>
<p>${product.title}${product.price} | <a href= "/product/${product.id}">More</a></p>
</div>
<#else>
<h3>Net tovara</h3>
</#list>
<hr>
<h3> Make new</h3>
<form action="/product/create" method="post">
Nazvanie <input type="text" name="title"/><br><br>
Opis <input type="text" name="description"/><br><br>
Price <input type="number" name="price"/><br><br>
kolvo <input type="number" name="count"/><br><br>
User <input type="text" name="user"/><br><br>
<input type="submit" value="add prod"/>
</form>
</body>
</html>