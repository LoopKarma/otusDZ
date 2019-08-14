package atm;

import atm.domain.CashBox;
import atm.domain.WithdrawPolicy;

import java.util.ArrayList;
import java.util.Collection;

public class WithdrawLessBanknotes implements WithdrawPolicy {
    @Override
    public Collection<Banknote> doWithdraw(int amount, Collection<CashBox> cashBoxes) {
        /*
           return amount with less amount of banknotes
        */
        return new ArrayList<>();
    }
}
