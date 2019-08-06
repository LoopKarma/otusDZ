import java.util.ArrayList;
import java.util.Collection;

public class CashBox implements CashBoxInterface {
    private int acceptedRating;
    private Collection<Banknote> volume;

    public CashBox(int acceptedRating) {
        this.acceptedRating = acceptedRating;
        this.volume = new ArrayList<>();
    }

    @Override
    public boolean canAcceptBanknote(Banknote banknote) {
        return banknote.getAmount() == acceptedRating;
    }

    @Override
    public void acceptBanknote(Banknote banknote) {
        this.volume.add(banknote);
    }

    @Override
    public Collection<Banknote> getBanknotes() {
        return volume;
    }

    @Override
    public void setBanknotes(Collection<Banknote> banknotes) {
        this.volume = banknotes;
    }
}
