package orm.service;

import orm.entity.User;

import java.util.Optional;

public interface DbServiceUser {
    User saveUser(User user);
    User updateUser(User user);
    Optional<User> getUser(long id);
}
