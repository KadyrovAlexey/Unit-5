<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
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
        <label>Логин:</label><br>
        <input type="text" name="login" required><br><br>

        <label>Пароль:</label><br>
        <input type="password" name="password" required><br><br>

        <input type="submit" value="Войти">
    </form>

    <p><a href="register">Зарегистрироваться</a></p>
</body>
</html>