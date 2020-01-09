package orm.dao;

import orm.entity.Account;

import java.util.Optional;

public interface AccountDao extends SessionDao {
    Optional<Account> findById(long id);
    Account create(Account user);
    Account update(Account user);
}
