<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.File" %>
<%@ page import="java.net.URLEncoder" %>
<html>
<body>

<h2>Мой файловый браузер</h2>

<p>Текущая папка: <%= request.getAttribute("currentFolder") %></p>
<p>Страница создана: <%= request.getAttribute("timeNow") %></p>

<%
    // Кнопка "Наверх"
    String parent = (String) request.getAttribute("parentFolder");
    if (parent != null) {
%>
    <a href="browser?path=<%= parent %>">⬆️ НАВЕРХ</a>
    <br><br>
<%
    }
%>

<table border="1" cellpadding="5">
    <tr>
        <th>Имя</th>
        <th>Размер</th>
        <th>Дата</th>
    </tr>

<%
    File[] files = (File[]) request.getAttribute("files");
    if (files != null) {
        for (File f : files) {
            String name = f.getName();
            String fullPath = f.getAbsolutePath();
            String size = "";
            String link = "browser?path=" + URLEncoder.encode(fullPath, "UTF-8");

            if (f.isDirectory()) {
                // Это папка
                size = "&lt;ПАПКА&gt;";
            } else {
                // Это файл
                size = f.length() + " байт";
                link = "download?path=" + URLEncoder.encode(fullPath, "UTF-8");
            }

            String date = new java.util.Date(f.lastModified()).toString();
%>
    <tr>
        <td><a href="<%= link %>"><%= name %></a></td>
        <td><%= size %></td>
        <td><%= date %></td>
    </tr>
<%
        }
    }
%>
</table>

</body>
</html>