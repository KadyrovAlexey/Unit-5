package com.example.servlet;

import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {

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

        String filePath = req.getParameter("path");
        if (filePath == null) {
            resp.sendError(400);
            return;
        }

        filePath = URLDecoder.decode(filePath, "UTF-8");
        File file = new File(filePath);

        // Проверка безопасности
        String canonicalFile = file.getCanonicalPath();
        String canonicalUserHome = new File(userHome).getCanonicalPath();

        if (!canonicalFile.startsWith(canonicalUserHome)) {
            resp.sendError(403);
            return;
        }

        if (!file.exists() || file.isDirectory()) {
            resp.sendError(404);
            return;
        }

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}