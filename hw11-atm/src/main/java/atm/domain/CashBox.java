package atm.domain;

import atm.Banknote;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.NoBanknotesFound;

public interface CashBox {
    int getAcceptedBanknoteNominal();
    void acceptBanknote(Banknote banknote) throws InvalidBanknoteNominalException;
    Banknote getBanknote() throws NoBanknotesFound;
    int calculateSum();
}
