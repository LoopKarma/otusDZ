package department;

import atm.Banknote;
import atm.BanknoteCreator;
import atm.CashBoxImpl;
import atm.WithdrawLessBanknotes;
import atm.domain.CashBox;
import atm.domain.WithdrawPolicy;
import department.events.RestorePreviousStateEvent;
import department.events.SaveStateEvent;
import department.memento.AtmWithHistory;

import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        WithdrawPolicy withdrawPolicy = new WithdrawLessBanknotes();

        List<AtmWithHistory> atmList = new ArrayList<>();
        atmList.add(new AtmWithHistory(initializeCashBoxes(), withdrawPolicy));
        atmList.add(new AtmWithHistory(initializeCashBoxes(), withdrawPolicy));
        atmList.add(new AtmWithHistory(initializeCashBoxes(), withdrawPolicy));

        ConcreteDepartment department = new ConcreteDepartment(atmList);
        department.getBalance();

        System.out.println("---- Withdraw 205 euro ----");
        department.throwAnEvent(new SaveStateEvent());
        department.doSomeWork();
        department.getBalance();

        System.out.println("---- Withdraw another 205 euro ----");
        department.throwAnEvent(new SaveStateEvent());
        department.doSomeWork();
        department.getBalance();


        System.out.println("---- Restore original state ----");
        department.throwAnEvent(new RestorePreviousStateEvent());
        department.throwAnEvent(new RestorePreviousStateEvent());
        department.getBalance();
    }

    private static ArrayList<CashBox> initializeCashBoxes() {
        DepartmentUtils utils = new DepartmentUtils();
        BanknoteCreator banknoteCreator = utils.getBanknoteCreator();

        CashBoxImpl cashBox5 = new CashBoxImpl(
                Banknote.FIVE.getAmount(),
                banknoteCreator.createBanknotes(Banknote.FIVE.ordinal(), 50)
        );
        CashBoxImpl cashBox10 = new CashBoxImpl(
                Banknote.TEN.getAmount(),
                banknoteCreator.createBanknotes(Banknote.TEN.ordinal(), 20)
        );
        CashBoxImpl cashBox20 = new CashBoxImpl(
                Banknote.TWENTY.getAmount(),
                banknoteCreator.createBanknotes(Banknote.TWENTY.ordinal(), 10)
        );
        CashBoxImpl cashBox50 = new CashBoxImpl(
                Banknote.FIFTY.getAmount(),
                banknoteCreator.createBanknotes(Banknote.FIFTY.ordinal(), 5)
        );

        ArrayList<CashBox> cashBoxes = new ArrayList<>();
        cashBoxes.add(cashBox5);
        cashBoxes.add(cashBox10);
        cashBoxes.add(cashBox20);
        cashBoxes.add(cashBox50);
        return cashBoxes;
    }
}
