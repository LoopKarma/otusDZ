package department.decorator;

import department.adv.behavior.AdvertisementBehavior;
import department.memento.AtmWithStateHistory;

import java.util.HashSet;

public class AdvertisementAtmWithDecorator extends AtmWithDecorator {
    private HashSet<AdvertisementBehavior> advBehaviourPool = new HashSet<>();

    public AdvertisementAtmWithDecorator(AtmWithStateHistory atm) {
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
        return atm.backupState();
    }

    @Override
    public void restoreLastState(String state) {
        atm.restoreLastState(state);
    }
}
