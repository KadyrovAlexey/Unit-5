package com.example.util;

import com.example.model.User;
import java.sql.*;

public class DatabaseUtil {

    // НАСТРОЙКИ - ИЗМЕНИТЕ ПАРОЛЬ ЕСЛИ НУЖНО
    private static final String URL = "jdbc:mysql://localhost:3306/filemanager?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";  // Если пароль другой, поменяйте

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Создание таблицы
    public static void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "login VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "email VARCHAR(100) NOT NULL" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица users готова");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Добавление пользователя
    public static boolean addUser(String login, String password, String email) {
        String sql = "INSERT INTO users (login, password, email) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Ошибка: возможно пользователь уже существует");
            return false;
        }
    }

    // Получение пользователя по логину
    public static User getUser(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Проверка существования пользователя
    public static boolean userExists(String login) {
        return getUser(login) != null;
    }
}