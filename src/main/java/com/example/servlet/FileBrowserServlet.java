package com.example.servlet;

import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

@WebServlet("/browser")
public class FileBrowserServlet extends HttpServlet {

    private static final String BASE_DIR = "C:\\filemanager\\";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String userHome = BASE_DIR + user.getLogin();

        String path = req.getParameter("path");
        File currentFolder;

        if (path == null || path.isEmpty()) {
            currentFolder = new File(userHome);
        } else {
            path = URLDecoder.decode(path, "UTF-8");
            currentFolder = new File(path);
        }

        // Проверка безопасности - нельзя выйти за пределы домашней папки
        String canonicalUserHome = new File(userHome).getCanonicalPath();
        String canonicalCurrent = currentFolder.getCanonicalPath();

        if (!canonicalCurrent.startsWith(canonicalUserHome)) {
            //currentFolder = new File(userHome);

            resp.sendError(403, "Доступ запрещён: нельзя просматривать папки другого пользователя");
            return;

        }

        File[] content = currentFolder.listFiles();

        req.setAttribute("files", content);
        req.setAttribute("currentFolder", currentFolder.getAbsolutePath());
        req.setAttribute("timeNow", new Date().toString());

        // Родительская папка (только если она в пределах домашней)
        String parentFolder = null;
        File parentFile = currentFolder.getParentFile();
        if (parentFile != null) {
            String canonicalParent = parentFile.getCanonicalPath();
            if (canonicalParent.startsWith(canonicalUserHome)) {
                parentFolder = canonicalParent;
            }
        }
        req.setAttribute("parentFolder", parentFolder);

        req.getRequestDispatcher("filebrowser.jsp").forward(req, resp);
    }
}