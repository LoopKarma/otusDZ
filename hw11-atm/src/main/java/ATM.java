import java.util.Collection;

public class ATM implements ATMInterface {
    private Collection<CashBoxInterface> cashBoxes;
    private WithdrawPolicyInterface withdrawPolicy;

    public ATM(Collection<CashBoxInterface> cashBoxes, WithdrawPolicyInterface withdrawPolicy) {
        this.cashBoxes = cashBoxes;
        this.withdrawPolicy = withdrawPolicy;
    }

    @Override
    public void acceptBanknotes(Collection<Banknote> banknotes) {
        for (Banknote banknote: banknotes) {
            for (CashBoxInterface cashBox: this.cashBoxes) {
                if (cashBox.canAcceptBanknote(banknote)) {
                    cashBox.acceptBanknote(banknote);
                }
            }
        }
    }

    @Override
    public Collection<Banknote> withdrawAmount(int amount) {
        if (amount > this.getBalance()) {
            throw new RuntimeException("Cannot withdraw this amount. Try withdraw smaller amount");
        }

        return this.withdrawPolicy.doWithdraw(amount, this.cashBoxes);
    }

    @Override
    public int getBalance() {
        int sum = 0;
        for (CashBoxInterface cashBox: this.cashBoxes) {
            sum += cashBox.getBanknotes()
                    .stream()
                    .map(Banknote::getAmount)
                    .mapToInt(Integer::intValue)
                    .sum();
        }
        return sum;
    }
}
