package webserver.services;

import webserver.dao.UserDao;
import webserver.model.User;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return userDao.findByLogin(login)
                .map(user -> user.getPassword().equals(password) && user.getRole().equals(User.ROLE_ADMIN))
                .orElse(false);
    }

}
