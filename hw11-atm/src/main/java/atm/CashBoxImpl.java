package atm;

import atm.domain.CashBox;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.NoBanknotesFound;

import java.util.ArrayList;

public class CashBoxImpl implements CashBox {
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
