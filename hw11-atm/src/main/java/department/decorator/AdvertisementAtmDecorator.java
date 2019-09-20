package department.decorator;

import atm.domain.Atm;
import department.adv.behavior.AdvertisementBehavior;
import department.memento.StateHistoryBehavior;

import java.util.HashSet;

public class AdvertisementAtmDecorator extends DecoratorBehavior implements StateHistoryBehavior {
    private HashSet<AdvertisementBehavior> advBehaviourPool = new HashSet<>();

    public AdvertisementAtmDecorator(Atm atm) {
        super(atm);
    }

    public void addAdvBehaviour(AdvertisementBehavior advBehaviour) {
        advBehaviourPool.add(advBehaviour);
    }

    @Override
    public int getBalance() {
        int balance = super.getBalance();
        showAdvBlock();
        System.out.println("Your balance is : " + balance);
        return balance;
    }

    private void showAdvBlock() {
        advBehaviourPool.stream().findAny().ifPresent(AdvertisementBehavior::promote);
    }

    @Override
    public String backupState() {
        if (atm instanceof StateHistoryBehavior) {
            return ((StateHistoryBehavior) atm).backupState();
        }
        return "";
    }

    @Override
    public void restoreLastState(String state) {
        if (atm instanceof StateHistoryBehavior) {
            ((StateHistoryBehavior) atm).restoreLastState(state);
        }
    }
}
