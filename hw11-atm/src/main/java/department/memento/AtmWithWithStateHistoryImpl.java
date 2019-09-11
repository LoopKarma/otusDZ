package department.memento;

import atm.Banknote;
import atm.domain.CashBox;
import atm.domain.WithdrawPolicy;
import atm.exception.InvalidBanknoteNominalException;
import atm.exception.WithdrawException;

import java.io.*;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AtmWithWithStateHistoryImpl implements AtmWithStateHistory {
    private Map<Integer, CashBox> cashBoxes = new HashMap<Integer, CashBox>();
    private WithdrawPolicy withdrawPolicy;

    public AtmWithWithStateHistoryImpl(Collection<CashBox> cashBoxes, WithdrawPolicy withdrawPolicy) {
        for (CashBox cashBox: cashBoxes) {
            Integer nominal = cashBox.getAcceptedBanknoteNominal();
            this.cashBoxes.put(nominal, cashBox);
        }
        this.withdrawPolicy = withdrawPolicy;
    }

    @Override
    public void acceptBanknotes(Collection<Banknote> banknotes) throws InvalidBanknoteNominalException {
        for (Banknote banknote: banknotes) {
            if (cashBoxes.get(banknote.getAmount()) == null) {
                throw new InvalidBanknoteNominalException();
            }
            cashBoxes.get(banknote.getAmount()).acceptBanknote(banknote);
        }
    }

    @Override
    public Collection<Banknote> withdrawAmount(int amount) throws WithdrawException {
        if (amount > this.getBalance() ) {
            throw new WithdrawException("Cannot withdraw this amount. Try withdraw smaller amount");
        }
        if (amount % Banknote.FIVE.getAmount() != 0) {
            throw new WithdrawException("Cannot withdraw this amount. Amount should be multiple of 5");
        }

        return this.withdrawPolicy.doWithdraw(amount, this.cashBoxes);
    }

    @Override
    public int getBalance() {
        return cashBoxes
                .values()
                .stream()
                .map(CashBox::calculateSum)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    public String backupState() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this.cashBoxes);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public void restoreLastState(String state) {
        try {
            byte[] data = Base64.getDecoder().decode(state);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            this.cashBoxes = (Map) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            System.out.print("ClassNotFoundException occurred.");
        } catch (IOException e) {
            System.out.print("IOException occurred.");
        }
    }
}
