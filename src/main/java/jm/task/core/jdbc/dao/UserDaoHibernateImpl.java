package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl extends Util implements UserDao {
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        String sqlCreatingUserTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id SERIAL Primary Key AUTO_INCREMENT,
                    user_name VARCHAR(45) NOT NULL,
                    lastName VARCHAR(45),
                    age INT NOT NULL
                    );
                """;
        ddlNativeQuery(sqlCreatingUserTable);
    }

    @Override
    public void dropUsersTable() {
        String sqlDropUsersTable = """
                 DROP TABLE IF EXISTS users;
                """;
        ddlNativeQuery(sqlDropUsersTable);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (SessionFactory sessionFactory = getSessionFactory();
             Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = new User(name, lastName, age);
                session.save(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (SessionFactory sessionFactory = getSessionFactory();
             Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                session.delete(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (SessionFactory sessionFactory = getSessionFactory();
             Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (SessionFactory sessionFactory = getSessionFactory();
             Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery("delete from User").executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    private void ddlNativeQuery(String sqlQuery) {
        try (SessionFactory sessionFactory = getSessionFactory();
             Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();
            try {
                session.createNativeQuery(sqlQuery).executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        }
    }
}
