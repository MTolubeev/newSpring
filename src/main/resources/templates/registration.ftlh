<!DOCTYPE html>
<html>
<head>
    <title>BUYSELL</title>
</head>
<body>
<h1>Shop <a href="/login">Войти</a></h1><hr>
<h4>Регистрация</h4>
<form action="/registration" method="post">
    Имя пользователя: <input type="text" name="username"><br><br>
    Эл. почта: <input type="email" name="email"><br><br>
    Номер телефона: <input type="text" name="phoneNumber"><br><br>
    Пароль: <input type="password" name="password"><br><br>

    <input type="submit" value="Зарегистрироваться"/>
</form>
<#if errorMessage??>
<h2 style = "color: red">${errorMessage}</h2>
</#if>
</body>
</html>