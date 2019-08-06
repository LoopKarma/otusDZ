import java.util.Collection;

public interface ATMInterface {
    public void acceptBanknotes(Collection<Banknote> banknotes);
    public Collection<Banknote> withdrawAmount(int amount);
    public int getBalance();
}
