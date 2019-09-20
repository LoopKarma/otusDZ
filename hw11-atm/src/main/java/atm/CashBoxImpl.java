package atm;

import atm.domain.CashBox;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.NoBanknotesFound;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CashBoxImpl implements CashBox, Serializable {
    private static final long serialVersionUID = 0L;
    private int acceptedRating;
    private int count;
    private ArrayList<Banknote> banknotes;

    CashBoxImpl(int rating) {
        for (Banknote banknote: Banknote.values()) {
            if (banknote.getAmount() == rating) {
                acceptedRating = rating;
                banknotes = new ArrayList<>();
                return;
            }
        }
        throw new InvalidBanknoteNominalException();
    }

    //handy constructor to create cashbox with some banknotes inside
    public CashBoxImpl(int rating, List<Banknote> banknotes) {
        for (Banknote banknote: Banknote.values()) {
            if (banknote.getAmount() == rating) {
                acceptedRating = rating;
                this.banknotes = new ArrayList<>();

                for (Banknote b : banknotes) {
                    if (b.getAmount() != rating) {
                        throw new InvalidBanknoteNominalException();
                    }
                    this.banknotes.add(b);
                    count++;
                }
                return;
            }
        }
        throw new InvalidBanknoteNominalException();
    }

    @Override
    public int getAcceptedBanknoteNominal() {
        return acceptedRating;
    }

    @Override
    public void acceptBanknote(Banknote banknote) throws InvalidBanknoteNominalException {
        if (getAcceptedBanknoteNominal() != banknote.getAmount()) {
            throw new InvalidBanknoteNominalException();
        }
        count++;
        banknotes.add(banknote);
    }

    @Override
    public Banknote getBanknote() throws NoBanknotesFound {
        if (banknotes.isEmpty()) {
            throw new NoBanknotesFound();
        }
        count--;
        return banknotes.remove(banknotes.size() - 1);
    }

    @Override
    public int calculateSum() {
        return count * acceptedRating;
    }
}
