package webserver.dao;

import webserver.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {
    User saveUser(User user);
    void flush();
    Optional<User> findById(long id);
    Optional<User> findRandomUser();
    Optional<User> findByLogin(String login);
    Collection<User> getAll();
}
