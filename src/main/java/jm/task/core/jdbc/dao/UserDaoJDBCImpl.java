package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {
    private static final String STARTTRANSACTION = """
                START TRANSACTION;
                """;
    private static final String COMMIT = """
                COMMIT;
                """;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id SERIAL Primary Key AUTO_INCREMENT,
                    user_name VARCHAR(45) NOT NULL,
                    lastName VARCHAR(45),
                    age INT NOT NULL
                    );
                """;

        try (Connection con = getConnection();
             Statement statement = con.createStatement();
             Statement startStatement = con.createStatement();
             Statement comStatement = con.createStatement()) {
                 startStatement.execute(STARTTRANSACTION);
                 statement.execute(sql);
                 comStatement.execute(COMMIT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String sql = """
                 DROP TABLE IF EXISTS users;
                """;

        try (Connection con = getConnection();
             Statement statement = con.createStatement();
             Statement startStatement = con.createStatement()) {
            startStatement.execute(STARTTRANSACTION);
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String user_name, String lastName, byte age) {

        String sql = ("""
                INSERT INTO users(user_name, lastname, age)
                VALUES (?, ?, ?);
                """);
        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql);
             Statement startStatement = con.createStatement();
             Statement comStatement = con.createStatement()) {

            preparedStatement.setString(1, user_name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            startStatement.execute(STARTTRANSACTION);
            preparedStatement.executeUpdate();
            comStatement.execute(COMMIT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String sql = """
                DELETE FROM users
                WHERE user_id = ?;
                """;

        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql);
             Statement startStatement = con.createStatement();
             Statement comStatement = con.createStatement()) {

            preparedStatement.setLong(1, id);
            startStatement.execute(STARTTRANSACTION);
            preparedStatement.executeUpdate();
            comStatement.execute(COMMIT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        String sql = """
                SELECT user_id, user_name, lastname, age
                FROM users;
                """;

        try (Connection con = getConnection();
             Statement statement = con.createStatement();
             Statement startStatement = con.createStatement();
             Statement comStatement = con.createStatement()) {
            startStatement.execute(STARTTRANSACTION);
            ResultSet resultSet = statement.executeQuery(sql);
            comStatement.execute(COMMIT);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("user_id"));
                user.setName(resultSet.getString("user_name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public void cleanUsersTable() {
        String sql = """
                TRUNCATE users;
                """;
        try (Connection con = getConnection();
             Statement statement = con.createStatement();
             Statement startStatement = con.createStatement()) {
            startStatement.execute(STARTTRANSACTION);
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
