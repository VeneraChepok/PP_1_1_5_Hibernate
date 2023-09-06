package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(" CREATE TABLE IF NOT EXISTS users " +
                    "(`id` BIGINT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(45) NOT NULL," +
                    "`lastname` VARCHAR(45) NOT NULL," +
                    "`age` TINYINT NOT NULL," +
                    "PRIMARY KEY (`id`))").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
               try {
                   transaction.rollback();
               } catch (Exception ex) {
                   throw new RuntimeException(ex);
               }
            }
            System.out.println("Проблема с созданием таблицы Hibernate " + e.getStackTrace());
        }
    }


    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println("Проблема с удалением таблицы Hibernate " + e.getStackTrace());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try {
            Session session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();

            System.out.println("User с именем - " + name + " успешно добавлен в базу данных Hibernate");
        } catch (Exception e) {
            if (transaction != null) {
               try {
                   transaction.rollback();
               } catch (Exception ex) {
                   throw new RuntimeException(ex);
               }
            }
            System.out.println("Проблема с coхранением user Hibernate " + e.getStackTrace());
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println("Проблема с удалением по id Hibernate " + e.getStackTrace());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List users;

        try (Session session = Util.getSessionFactory().openSession()) {
            users = session.createQuery("FROM User").list();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery("TRUNCATE TABLE users").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
               try {
                   transaction.rollback();
               } catch (Exception ex) {
                   throw new RuntimeException(ex);
               }
            }
            System.out.println("Проблема с очисткой таблицы" + e.getStackTrace());
        }
    }
}
