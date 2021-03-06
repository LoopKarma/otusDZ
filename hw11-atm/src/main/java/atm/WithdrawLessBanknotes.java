package atm;

import atm.domain.CashBox;
import atm.domain.WithdrawPolicy;
import atm.exception.WithdrawException;

import java.io.Serializable;
import java.util.*;

public class WithdrawLessBanknotes implements WithdrawPolicy, Serializable {
    private static final long serialVersionUID = 0L;
    @Override
    public Collection<Banknote> doWithdraw(int amount, Map<Integer, CashBox> cashBoxes) {
        Integer[] availableNotes = cashBoxes.keySet().toArray(new Integer[cashBoxes.keySet().size()]);
        Arrays.sort(availableNotes, Comparator.reverseOrder());

        int leftToWithdraw = amount;
        ArrayList<Banknote> banknotes = new ArrayList<>();
        for (Integer banknoteNominal: availableNotes) {
            while (banknoteNominal <= leftToWithdraw && cashBoxes.get(banknoteNominal).calculateSum() > 0) {
                leftToWithdraw -= banknoteNominal;
                banknotes.add(cashBoxes.get(banknoteNominal).getBanknote());
            }
        }
        if (leftToWithdraw != 0) {
            throw new WithdrawException(String.format("Cannot withdraw this amount: %d", amount));
        }

        return banknotes;
    }
}
