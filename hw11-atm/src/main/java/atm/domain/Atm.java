package atm.domain;

import atm.Banknote;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.WithdrawException;

import java.util.Collection;

public interface Atm {
    void acceptBanknotes(Collection<Banknote> banknotes) throws InvalidBanknoteNominalException;
    Collection<Banknote> withdrawAmount(int amount) throws WithdrawException;
    int getBalance();
}
