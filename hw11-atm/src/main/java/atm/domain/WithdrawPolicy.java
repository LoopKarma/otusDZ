package atm.domain;

import atm.Banknote;

import java.util.Collection;

public interface WithdrawPolicy {
    Collection<Banknote> doWithdraw(int amount, Collection<CashBox> cashBoxes);
}
