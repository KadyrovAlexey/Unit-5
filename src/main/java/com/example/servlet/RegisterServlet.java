package com.example.servlet;

import com.example.model.User;
import com.example.util.DatabaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        DatabaseUtil.createTableIfNotExists();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        if (DatabaseUtil.userExists(login)) {
            req.setAttribute("error", "Пользователь уже существует!");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        boolean added = DatabaseUtil.addUser(login, password, email);

        if (!added) {
            req.setAttribute("error", "Ошибка регистрации!");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        // Создаём домашнюю папку
        File homeDir = new File("C:\\filemanager\\" + login);
        if (!homeDir.exists()) {
            homeDir.mkdirs();
        }

        resp.sendRedirect("login");
    }
}