package atm;

import atm.exception.InvalidBanknoteNominalException;
import atm.exception.NoBanknotesFound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CashBoxImplTest {
    @Test
    void createCashboxWithIllegalNominal() {
        int acceptedNominal = 187;
        assertThrows(
                InvalidBanknoteNominalException.class,
                () -> new CashBoxImpl(acceptedNominal)
        );
    }

    @Test
    void getAcceptedBanknoteNominal() {
        int acceptedNominal = 10;
        CashBoxImpl cashBox = new CashBoxImpl(acceptedNominal);
        assertEquals(acceptedNominal, cashBox.getAcceptedBanknoteNominal());
    }

    @Test
    void acceptBanknote() {
        int acceptedNominal = 10;
        CashBoxImpl cashBox = new CashBoxImpl(acceptedNominal);
        cashBox.acceptBanknote(Banknote.TEN);
        assertEquals(1 * acceptedNominal, cashBox.calculateSum());

        cashBox.acceptBanknote(Banknote.TEN);
        cashBox.acceptBanknote(Banknote.TEN);
        assertEquals(3 * acceptedNominal, cashBox.calculateSum());


        cashBox.acceptBanknote(Banknote.TEN);
        cashBox.acceptBanknote(Banknote.TEN);
        cashBox.acceptBanknote(Banknote.TEN);
        assertEquals(6 * acceptedNominal, cashBox.calculateSum());
    }

    @Test
    void getBanknotesFromCashbox() {
        int acceptedNominal = 10;
        CashBoxImpl cashBox = new CashBoxImpl(acceptedNominal);
        for (int i = 0; i < 9; i++) {
            cashBox.acceptBanknote(Banknote.TEN);
        }
        for (int i = 0; i < 9; i++) {
            assertEquals(Banknote.TEN, cashBox.getBanknote());
        }
        assertThrows(
                NoBanknotesFound.class,
                () -> cashBox.getBanknote()
        );
    }

    @Test
    void acceptWrongBanknote() {
        int acceptedNominal = 10;
        CashBoxImpl cashBox = new CashBoxImpl(acceptedNominal);

        assertThrows(
                InvalidBanknoteNominalException.class,
                () -> cashBox.acceptBanknote(Banknote.FIFTY)
        );
    }
}