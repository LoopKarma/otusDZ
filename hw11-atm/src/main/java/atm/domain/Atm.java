package atm.domain;

import atm.Banknote;
import atm.exception.WithdrawException;

import java.util.Collection;

public interface Atm {
    void acceptBanknotes(Collection<Banknote> banknotes);
    Collection<Banknote> withdrawAmount(int amount) throws WithdrawException;
    int getBalance();
}
