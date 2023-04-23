package jm.task.core.jdbc.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";


    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        Properties props = new Properties();
        props.put(Environment.DRIVER, DRIVER);
        props.put(Environment.URL, URL);
        props.put(Environment.USER, USERNAME);
        props.put(Environment.PASS, PASSWORD);

        Configuration conf = new Configuration()
                .addAnnotatedClass(jm.task.core.jdbc.model.User.class)
                .setProperties(props);

        SessionFactory sf = conf.buildSessionFactory();
            return sf;


    }
}
