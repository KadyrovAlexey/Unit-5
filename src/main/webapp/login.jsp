<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Вход</title>
</head>
<body>
    <h2>Авторизация</h2>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red"><%= request.getAttribute("error") %></p>
    <% } %>

    <form method="post" action="login">
        Логин: <input type="text" name="login"><br><br>
        Пароль: <input type="password" name="password"><br><br>
        <input type="submit" value="Войти">
    </form>
</body>
</html>