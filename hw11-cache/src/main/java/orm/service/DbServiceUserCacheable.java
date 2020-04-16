package orm.service;

import cachehw.MyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import orm.dao.UserDao;
import orm.entity.User;
import orm.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceUserCacheable implements DbServiceUser {
    private static Logger logger = LoggerFactory.getLogger(DbServiceUserCacheable.class);

    private final UserDao userDao;
    private final MyCache<Long, User> cache;

    public DbServiceUserCacheable(UserDao userDao, MyCache<Long, User> cache) {
        this.userDao = userDao;
        this.cache = cache;
    }

    @Override
    public User saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var savedUser = userDao.create(user);
                sessionManager.commitSession();

                logger.info("created user: {}", savedUser.getId());
                return savedUser;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public User updateUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var updatedUser = userDao.update(user);
                sessionManager.commitSession();

                logger.info("updated user: {}", updatedUser.getId());
                return updatedUser;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        User cachedValue = cache.get(id);
        if (cachedValue != null) {
            return Optional.of(cachedValue);
        }
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                logger.info("user: {}", userOptional.orElse(null));
                cache.put(id, userOptional.get());
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
