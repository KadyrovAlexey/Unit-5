<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.File" %>
<%@ page import="java.net.URLEncoder" %>
<html>
<body>

<div style="text-align: right; margin-bottom: 10px;">
    <a href="logout">Выйти</a>
</div>

<h2>Мой файловый браузер</h2>

<p>Текущая папка: <%= request.getAttribute("currentFolder") %></p>
<p>Страница создана: <%= request.getAttribute("timeNow") %></p>

<%
    String parent = (String) request.getAttribute("parentFolder");
    if (parent != null) {
        String encodedParent = URLEncoder.encode(parent, "UTF-8");
%>
    <a href="browser?path=<%= encodedParent %>">⬆️ НАВЕРХ</a>
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
            String encodedPath = URLEncoder.encode(fullPath, "UTF-8");

            String size = "";
            String link = "";

            if (f.isDirectory()) {
                size = "&lt;ПАПКА&gt;";
                link = "browser?path=" + encodedPath;
            } else {
                size = f.length() + " байт";
                link = "download?path=" + encodedPath;
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
</

table>

</body>
</html>