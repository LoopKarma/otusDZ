package department;

import atm.Banknote;
import atm.domain.Atm;
import department.adv.behavior.RevolutBankAdv;
import department.adv.behavior.TomsInsuranceAdv;
import department.decorator.AdvertisementAtmDecorator;
import department.events.RestorePreviousStateEvent;
import department.events.SaveStateEvent;
import department.memento.AtmWithHistory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ConcreteDepartment implements Department {
    private HashMap<Integer, AdvertisementAtmDecorator> atmItems = new HashMap<>();
    private HashMap<Integer, LinkedList<String>> atmHistory = new HashMap<>();

    ConcreteDepartment(List<AtmWithHistory> atmList) {
        for (Atm atm : atmList) {
            AdvertisementAtmDecorator decoratedAtm = createAdvertisementAtmDecorator(atm);
            this.atmItems.put(decoratedAtm.hashCode(), decoratedAtm);
            initializeAtmHistory(decoratedAtm);
        }
    }

    private AdvertisementAtmDecorator createAdvertisementAtmDecorator(Atm atm) {
        AdvertisementAtmDecorator decoratedAtm = new AdvertisementAtmDecorator(atm);
        decoratedAtm.addAdvBehaviour(new RevolutBankAdv());
        decoratedAtm.addAdvBehaviour(new TomsInsuranceAdv());
        return decoratedAtm;
    }


    private void initializeAtmHistory(AdvertisementAtmDecorator atm) {
        LinkedList<String> history = new LinkedList<>();
        history.add(atm.backupState());

        atmHistory.put(atm.hashCode(), history);
    }

    void doSomeWork() {
        for (AdvertisementAtmDecorator atm: atmItems.values()) {
            atm.withdrawAmount(Banknote.FIVE.getAmount());
            atm.withdrawAmount(Banknote.TWO_HUNDREDS.getAmount());
        }
    }

    @Override
    public int getBalance() {
        int balance = 0;
        for (Atm atm : atmItems.values()) {
            balance += atm.getBalance();
        }
        return balance;
    }


    @Override
    public void throwAnEvent(Event event) {
        if (event instanceof SaveStateEvent) {
            saveStateForAtms();
        }
        if (event instanceof RestorePreviousStateEvent) {
            restoreStateForAtms();
        }
    }

    private void restoreStateForAtms() {
        for (AdvertisementAtmDecorator atm: atmItems.values()) {
            LinkedList<String> history = atmHistory.get(atm.hashCode());
            atm.restoreLastState(history.getLast());
            history.removeLast();
        }
    }

    private void saveStateForAtms() {
        for (AdvertisementAtmDecorator atm: atmItems.values()) {
            LinkedList<String> history = atmHistory.get(atm.hashCode());
            history.add(atm.backupState());
            atmHistory.put(atm.hashCode(), history);
        }
    }
}
