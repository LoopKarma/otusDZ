package atm;

import atm.domain.CashBox;
import atm.exception.WithdrawException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WithdrawLessBanknotesTest {
    @Test
    void withdraw() {
        int amount = 795;
        Map<Integer, CashBox> cashBoxMap = new HashMap<Integer, CashBox>();

        cashBoxMap.put(200, getCashBoxMock(200));
        cashBoxMap.put(100, getCashBoxMock(100));
        cashBoxMap.put(50, getCashBoxMock(50));
        cashBoxMap.put(20, getCashBoxMock(20));
        cashBoxMap.put(10, getCashBoxMock(10));
        cashBoxMap.put(5, getCashBoxMock(5));

        WithdrawLessBanknotes lessBanknotes = new WithdrawLessBanknotes();
        Collection<Banknote> banknotes = lessBanknotes.doWithdraw(amount, cashBoxMap);

        assertEquals(amount, banknotes.stream().map(Banknote::getAmount).mapToInt(Integer::intValue).sum());
    }

    @Test
    void incorrectAmount() {
        int withdrawAmount = 17;
        int availableNominal = 5;
        Map<Integer, CashBox> cashBoxMap = new HashMap<Integer, CashBox>();
        CashBoxImpl cashBoxMock = getCashBoxMock(availableNominal);
        cashBoxMap.put(availableNominal, cashBoxMock);

        WithdrawLessBanknotes lessBanknotes = new WithdrawLessBanknotes();
        WithdrawException exception = assertThrows(
                WithdrawException.class,
                () -> lessBanknotes.doWithdraw(withdrawAmount, cashBoxMap)
        );
        assertEquals(String.format("Cannot withdraw this amount: %d", withdrawAmount), exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 35, 50, 250, 355 })
    void withdrawWithOneAvailableBanknote(int withdrawAmount) {
        int availableNominal = 5;
        Map<Integer, CashBox> cashBoxMap = new HashMap<Integer, CashBox>();

        CashBoxImpl cashBoxMock = getCashBoxMock(availableNominal);
        cashBoxMap.put(availableNominal, cashBoxMock);

        WithdrawLessBanknotes lessBanknotes = new WithdrawLessBanknotes();
        Collection<Banknote> banknotes = lessBanknotes.doWithdraw(withdrawAmount, cashBoxMap);

        assertEquals(withdrawAmount, banknotes.stream().map(Banknote::getAmount).mapToInt(Integer::intValue).sum());
    }

    private CashBoxImpl getCashBoxMock(int nominal) {
        CashBoxImpl cashBox = mock(CashBoxImpl.class);
        for (Banknote banknote: Banknote.values()) {
            if (banknote.getAmount() == nominal) {
                when(cashBox.getBanknote()).thenReturn(banknote);
            }
        }
        return cashBox;
    }
}