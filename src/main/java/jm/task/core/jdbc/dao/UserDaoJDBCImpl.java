package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String TABLE_NAME = "users";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
            + "name VARCHAR(255) NOT NULL, "
            + "lastName VARCHAR(255) NOT NULL, "
            + "age TINYINT NOT NULL"
            + ")";
    private static final String INSERT_USER_SQL = "INSERT INTO " + TABLE_NAME + " (name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_USER_BY_ID_SQL = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String CLEAN_TABLE_SQL = "TRUNCATE TABLE " + TABLE_NAME;
    private static final String GET_ALL_USERS_SQL = "SELECT * FROM " + TABLE_NAME;

    public UserDaoJDBCImpl() {
        // Конструктор по умолчанию (требование задания)
    }

    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(CREATE_TABLE_SQL);
            System.out.println("Таблица " + TABLE_NAME + " создана или уже существует");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы", e);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(DROP_TABLE_SQL);
            System.out.println("Таблица " + TABLE_NAME + " удалена");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            System.out.println("User с именем - " + name + " добавлен в базу данных");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении пользователя с ID = " + id, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка пользователей", e);
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(CLEAN_TABLE_SQL);
            System.out.println("Таблица " + TABLE_NAME + " очищена");

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }
}