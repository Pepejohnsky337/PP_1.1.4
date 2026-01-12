package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import javax.persistence.PersistenceException;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        this.sessionFactory = Util.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "lastName VARCHAR(255) NOT NULL, " +
                    "age TINYINT NOT NULL" +
                    ")";

            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            System.out.println("Таблица users создана");

        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String sql = "DROP TABLE IF EXISTS users";
            session.createNativeQuery(sql).executeUpdate();

            transaction.commit();
            System.out.println("Таблица users удалена");

        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.save(user);

            transaction.commit();
            System.out.println("User с именем - " + name + " добавлен в базу данных");

        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                System.out.println("Пользователь с ID " + id + " удален");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
            }

            transaction.commit();

        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (PersistenceException e) {
            System.out.println("Ошибка при получении пользователей: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            String sql = "TRUNCATE TABLE users";
            session.createNativeQuery(sql).executeUpdate();

            transaction.commit();
            System.out.println("Таблица users очищена");

        } catch (PersistenceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при очистке таблицы: " + e.getMessage());
        }
    }
}