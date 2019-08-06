import java.util.Collection;

public interface WithdrawPolicyInterface {
    public Collection<Banknote> doWithdraw(int amount, Collection<CashBoxInterface> cashBoxes);
}
