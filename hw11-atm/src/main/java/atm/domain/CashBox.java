package atm.domain;

import atm.Banknote;
import atm.exception.InvalidBanknoteNominalException;

public interface CashBox {
    boolean canAcceptBanknote(Banknote banknote);
    void acceptBanknote(Banknote banknote) throws InvalidBanknoteNominalException;
    int calculateSum();
}
