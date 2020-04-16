package orm.service;

import orm.entity.Account;

import java.util.Optional;

public interface DbServiceAccount {
    Account saveAccount(Account account);
    Account updateAccount(Account account);
    Optional<Account> getAccount(long no);
}
