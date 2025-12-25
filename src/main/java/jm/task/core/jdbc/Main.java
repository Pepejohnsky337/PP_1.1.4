package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Ivan", "Ivanov", (byte) 20);
        userService.saveUser( "Aleksei", "Alekseiv", (byte) 30);
        userService.saveUser("Vladimir", "Vladimirov", (byte) 40);
        userService.saveUser("Sveta", "Ustavshaya", (byte) 50);

        userService.removeUserById(0);
        List<User> users = userService.getAllUsers();
        int index = 0;
        for (User user : users) {
            System.out.println(index + ". ID: " + user.getId() +
                    ", Name: " + user.getName() +
                    ", LastName: " + user.getLastName() +
                    ", Age: " + user.getAge());
            index++;
        }

        System.out.println(index);
        userService.getAllUsers();

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
