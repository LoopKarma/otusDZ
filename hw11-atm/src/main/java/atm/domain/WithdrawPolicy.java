package atm.domain;

import atm.Banknote;

import java.util.Collection;
import java.util.Map;

public interface WithdrawPolicy {
    Collection<Banknote> doWithdraw(int amount, Map<Integer, CashBox> cashBoxes);
}
