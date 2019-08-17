package atm;

import atm.domain.Atm;
import atm.domain.CashBox;
import atm.domain.WithdrawPolicy;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.WithdrawException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AtmImpl implements Atm {
    private Map<Integer, CashBox> cashBoxes;
    private WithdrawPolicy withdrawPolicy;

    AtmImpl(Collection<CashBox> cashBoxes, WithdrawPolicy withdrawPolicy) {
        this.cashBoxes = new HashMap<Integer, CashBox>();
        for (CashBox cashBox: cashBoxes) {
            Integer nominal = cashBox.getAcceptedBanknoteNominal();
            this.cashBoxes.put(nominal, cashBox);
        }
        this.withdrawPolicy = withdrawPolicy;
    }

    @Override
    public void acceptBanknotes(Collection<Banknote> banknotes) throws InvalidBanknoteNominalException {
        for (Banknote banknote: banknotes) {
            if (cashBoxes.get(banknote.getAmount()) == null) {
                throw new InvalidBanknoteNominalException();
            }
            cashBoxes.get(banknote.getAmount()).acceptBanknote(banknote);
        }
    }

    @Override
    public Collection<Banknote> withdrawAmount(int amount) throws WithdrawException {
        if (amount > this.getBalance() ) {
            throw new WithdrawException("Cannot withdraw this amount. Try withdraw smaller amount");
        }
        if (amount % Banknote.FIVE.getAmount() != 0) {
            throw new WithdrawException("Cannot withdraw this amount. Amount should be multiple of 5");
        }

        return this.withdrawPolicy.doWithdraw(amount, this.cashBoxes);
    }

    @Override
    public int getBalance() {
        return cashBoxes
                .values()
                .stream()
                .map(CashBox::calculateSum)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
