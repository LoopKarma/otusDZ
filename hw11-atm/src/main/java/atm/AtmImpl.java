package atm;

import atm.domain.Atm;
import atm.domain.CashBox;
import atm.domain.WithdrawPolicy;
import atm.exception.WithdrawException;

import java.util.Collection;

public class AtmImpl implements Atm {
    private Collection<CashBox> cashBoxes;
    private WithdrawPolicy withdrawPolicy;

    public AtmImpl(Collection<CashBox> cashBoxes, WithdrawPolicy withdrawPolicy) {
        this.cashBoxes = cashBoxes;
        this.withdrawPolicy = withdrawPolicy;
    }

    @Override
    public void acceptBanknotes(Collection<Banknote> banknotes) {
        for (Banknote banknote: banknotes) {
            for (CashBox cashBox: this.cashBoxes) {
                if (cashBox.canAcceptBanknote(banknote)) {
                    cashBox.acceptBanknote(banknote);
                }
            }
        }
    }

    @Override
    public Collection<Banknote> withdrawAmount(int amount) throws WithdrawException {
        if (amount > this.getBalance()) {
            throw new WithdrawException("Cannot withdraw this amount. Try withdraw smaller amount");
        }

        return this.withdrawPolicy.doWithdraw(amount, this.cashBoxes);
    }

    @Override
    public int getBalance() {
        return cashBoxes
                .stream()
                .map(CashBox::calculateSum)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
