package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
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
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(session.get(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public List<User> getAllUsers() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public void cleanUsersTable() {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void ddlNativeQuery(String sqlQuery) {

        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.createNativeQuery(sqlQuery).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            session.close();
        }
    }
}
