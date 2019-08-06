import java.util.ArrayList;
import java.util.Collection;

public class WithdrawLessBanknotes implements WithdrawPolicyInterface {
    @Override
    public Collection<Banknote> doWithdraw(int amount, Collection<CashBoxInterface> cashBoxes) {
        //return amount with less amount of banknotes
        return new ArrayList<>();
    }
}
