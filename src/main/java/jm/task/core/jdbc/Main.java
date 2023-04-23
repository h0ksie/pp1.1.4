package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.dropUsersTable();
        userService.createUsersTable();
        userService.saveUser("1", "test", (byte) 21);
        userService.saveUser("2", "test2", (byte) 21);
        userService.saveUser("3", "test1", (byte) 21);
        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        System.out.println(userService.getAllUsers());


    }
}
