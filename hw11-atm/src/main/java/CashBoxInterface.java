import java.util.Collection;

interface CashBoxInterface {
    public boolean canAcceptBanknote(Banknote banknote);
    public void acceptBanknote(Banknote banknote);
    public Collection<Banknote> getBanknotes();
    public void setBanknotes(Collection<Banknote> banknotes);
}
