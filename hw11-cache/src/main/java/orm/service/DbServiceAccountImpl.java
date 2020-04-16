package orm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orm.dao.AccountDao;
import orm.entity.Account;
import orm.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceAccountImpl implements DbServiceAccount {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserCacheable.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDaoDao) {
        this.accountDao = accountDaoDao;
    }

    @Override
    public Account saveAccount(Account account) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var savedAccount = accountDao.create(account);
                sessionManager.commitSession();

                logger.info("created account: {}", savedAccount.getNo());
                return savedAccount;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Account updateAccount(Account account) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var updatedAccount = accountDao.update(account);
                sessionManager.commitSession();

                logger.info("updated account: {}", updatedAccount.getNo());
                return updatedAccount;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long no) {
        try (SessionManager sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> accountOptional = accountDao.findById(no);

                logger.info("account: {}", accountOptional.orElse(null));
                return accountOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
