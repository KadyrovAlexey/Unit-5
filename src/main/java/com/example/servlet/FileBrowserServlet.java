package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@WebServlet("/browser")
public class FileBrowserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login");
            return;
        }
        String path = req.getParameter("path");
        if (path == null || path.equals(""))
        {
            path = "C:\\"; // Если ничего не указанно, просто показать диск С
        }

        //Открываем данную папку
        File currentFolder = new File(path);
        // Получаем список того, что внутри
        File[] content = currentFolder.listFiles();
        // Кладём всё в request, чтобы JSP могла это показать
        req.setAttribute("files", content);
        req.setAttribute("currentFolder", currentFolder.getAbsolutePath());
        // Родительская папка (для кнопки "Наверх")
        String parentFolder = currentFolder.getParent();
        req.setAttribute("parentFolder", parentFolder);

        // Время генерации страницы
        Date now = new Date();
        req.setAttribute("timeNow", now.toString());

        // Открываем JSP страницу
        req.getRequestDispatcher("filebrowser.jsp").forward(req, resp);

    }
}