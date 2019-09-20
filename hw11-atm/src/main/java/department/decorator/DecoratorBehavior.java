package department.decorator;

import atm.Banknote;
import atm.domain.Atm;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.WithdrawException;

import java.util.Collection;

public abstract class DecoratorBehavior implements Atm {
    protected Atm atm;

    DecoratorBehavior(Atm atm) {
        this.atm = atm;
    }

    @Override
    public void acceptBanknotes(Collection<Banknote> banknotes) throws InvalidBanknoteNominalException {
        atm.acceptBanknotes(banknotes);
    }

    @Override
    public Collection<Banknote> withdrawAmount(int amount) throws WithdrawException {
        return atm.withdrawAmount(amount);
    }

    @Override
    public int getBalance() {
        return atm.getBalance();
    }
}
