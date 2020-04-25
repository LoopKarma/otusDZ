package webserver.dao;

import org.hibernate.Session;
import webserver.model.User;
import webserver.sessionmanager.DatabaseSessionHibernate;
import webserver.sessionmanager.SessionManagerHibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class UserDaoHibernate implements UserDao {

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findRandomUser() {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        sessionManager.beginSession();
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            CriteriaBuilder criteriaBuilder = hibernateSession.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query
                    .select(root)
                    .where(criteriaBuilder.equal(root.get("login"), login))
            ;

            Optional<User> user = hibernateSession.createQuery(query).uniqueResultOptional();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Collection<User> getAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            CriteriaBuilder criteriaBuilder = hibernateSession.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);

            return hibernateSession.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public User saveUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flush() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        currentSession.getHibernateSession().flush();
    }
}
