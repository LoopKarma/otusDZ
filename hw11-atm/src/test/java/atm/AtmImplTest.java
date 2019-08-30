package atm;

import atm.domain.CashBox;
import atm.domain.WithdrawPolicy;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.WithdrawException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AtmImplTest {

    public static final int amount = 35;

    @Test
    void acceptBanknotes() {
        AtmImpl atm = createAtm();
        assertEquals(0, atm.getBalance());

        ArrayList<Banknote> correctBanknotesCollection = getBanknotes();

        atm.acceptBanknotes(correctBanknotesCollection);
        assertEquals(amount, atm.getBalance());

        ArrayList<Banknote> illegalBanknotesCollection = new ArrayList<>();
        illegalBanknotesCollection.add(Banknote.HUNDRED);

        assertThrows(
                InvalidBanknoteNominalException.class,
                () -> atm.acceptBanknotes(illegalBanknotesCollection)
        );
    }

    @Test
    void withdrawAmountThrowsTooBigAmountException() {
        AtmImpl atm = createAtm();
        ArrayList<Banknote> correctBanknotesCollection = getBanknotes();
        atm.acceptBanknotes(correctBanknotesCollection);

        WithdrawException tooBigAmountException = assertThrows(
                WithdrawException.class,
                () -> atm.withdrawAmount(40)
        );
        assertEquals("Cannot withdraw this amount. Try withdraw smaller amount", tooBigAmountException.getMessage());
    }

    @Test
    void withdrawAmountThrowsMultiplicityAmountException() {
        AtmImpl atm = createAtm();
        ArrayList<Banknote> correctBanknotesCollection = getBanknotes();
        atm.acceptBanknotes(correctBanknotesCollection);

        WithdrawException multiplicityAmountException = assertThrows(
                WithdrawException.class,
                () -> atm.withdrawAmount(21)
        );
        assertEquals("Cannot withdraw this amount. Amount should be multiple of 5", multiplicityAmountException.getMessage());
    }

    @Test
    void doWithdraw() {
        AtmImpl atm = createAtm();
        ArrayList<Banknote> correctBanknotesCollection = getBanknotes();
        atm.acceptBanknotes(correctBanknotesCollection);
        Collection<Banknote> banknotes = atm.withdrawAmount(amount);
        assertEquals(3, banknotes.size());
    }

    private ArrayList<Banknote> getBanknotes() {
        ArrayList<Banknote> correctBanknotesCollection = new ArrayList<>();
        correctBanknotesCollection.add(Banknote.FIVE);
        correctBanknotesCollection.add(Banknote.TEN);
        correctBanknotesCollection.add(Banknote.TEN);
        correctBanknotesCollection.add(Banknote.TEN);
        return correctBanknotesCollection;
    }

    private AtmImpl createAtm() {
        WithdrawPolicy withdrawPolicy = mock(WithdrawPolicy.class);

        CashBoxImpl cashBox10 = new CashBoxImpl(Banknote.TEN.getAmount());
        CashBoxImpl cashBox5 = new CashBoxImpl(Banknote.FIVE.getAmount());

        ArrayList<CashBox> cashBoxes = new ArrayList<>();
        cashBoxes.add(cashBox5);
        cashBoxes.add(cashBox10);

        ArrayList<Banknote> banknotes = new ArrayList<>();
        banknotes.add(Banknote.TWO_HUNDREDS);
        banknotes.add(Banknote.FIFTY);
        banknotes.add(Banknote.TEN);
        when(withdrawPolicy.doWithdraw(anyInt(), any())).thenReturn(banknotes);

        return new AtmImpl(cashBoxes, withdrawPolicy);
    }
}