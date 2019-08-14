package atm;

import atm.domain.CashBox;
import atm.exception.InvalidBanknoteNominalException;

public class CashBoxImpl implements CashBox {
    private int acceptedRating;
    private int count;

    public CashBoxImpl(int acceptedRating) {
        for (Banknote banknote: Banknote.values()) {
            if (banknote.getAmount() == acceptedRating) {
                this.acceptedRating = acceptedRating;
            }
        }
        throw new InvalidBanknoteNominalException();
    }

    @Override
    public boolean canAcceptBanknote(Banknote banknote) {
        return banknote.getAmount() == acceptedRating;
    }

    @Override
    public void acceptBanknote(Banknote banknote) throws InvalidBanknoteNominalException {
        if (!canAcceptBanknote(banknote)) {
            throw new InvalidBanknoteNominalException();
        }
        count++;
    }

    @Override
    public int calculateSum() {
        return count * acceptedRating;
    }
}
