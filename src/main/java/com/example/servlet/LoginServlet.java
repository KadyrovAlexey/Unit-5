package com.example.servlet;

import com.example.model.User;
import com.example.util.DatabaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect("browser");
            return;
        }
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User user = DatabaseUtil.getUser(login);

        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect("browser");
        } else {
            req.setAttribute("error", "Неверный логин или пароль!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}