<!DOCTYPE html>
<html>
<head>
<title>POMOGITE</title>
</head>
<body>
<h1>hiiiii</h1><hr>
<h4>PODROBNO</h4>
<b>Nazvanie</b>${product.title}<br>
<b>Opis</b>${product.description}<br>
<b>Cena</b>${product.price}<br>
<b>Kolvo</b>${product.count}<br>
<b>User</b>${product.user}<br>
<hr>
<form action="/product/delete/${product.id}" method="post">

<input type="submit" value="ydalit"/>
</form>
</body>
</html>