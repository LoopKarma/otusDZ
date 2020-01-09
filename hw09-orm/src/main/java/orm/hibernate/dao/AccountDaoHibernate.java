package orm.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orm.dao.AccountDao;
import orm.dao.DaoException;
import orm.entity.Account;
import orm.hibernate.sessionmanager.DatabaseSessionHibernate;
import orm.hibernate.sessionmanager.SessionManagerHibernate;
import orm.sessionmanager.SessionManager;

import java.security.InvalidParameterException;
import java.util.Optional;

public class AccountDaoHibernate implements AccountDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public AccountDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Account> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(Account.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Account create(Account account) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (account.getNo() > 0) {
                hibernateSession.merge(account);
            } else {
                hibernateSession.persist(account);
            }
            return account;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }

    }

    @Override
    public Account update(Account account) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (hibernateSession.contains(account)) {
                hibernateSession.update(account);
            } else {
                throw new InvalidParameterException("");
            }
            return account;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
