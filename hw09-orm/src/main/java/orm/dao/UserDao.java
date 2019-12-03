package orm.dao;

import orm.entity.User;

import java.util.Optional;

public interface UserDao extends SessionDao {
    Optional<User> findById(long id);
    User create(User user);
    User update(User user);
}
