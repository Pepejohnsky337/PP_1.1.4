package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String USER = "root";
    private static final String PASSWORD = "123456789";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/pepe337db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static SessionFactory sessionFactory;

    static {
        try {
            Class.forName(JDBC_DRIVER);

            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.driver_class", JDBC_DRIVER);
            configuration.setProperty("hibernate.connection.url", URL);
            configuration.setProperty("hibernate.connection.username", USER);
            configuration.setProperty("hibernate.connection.password", PASSWORD);
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "none");

            configuration.addAnnotatedClass(User.class);

            sessionFactory = configuration.buildSessionFactory();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удалось загрузить JDBC драйвер", e);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации Hibernate", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}