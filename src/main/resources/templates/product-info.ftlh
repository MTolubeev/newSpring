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
<form action="/basket" method="post">
 <input type="submit" value="Add v korzinochky"/>
 </form>
</body>
</html>