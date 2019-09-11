package department.memento;

import atm.domain.Atm;

public interface AtmWithStateHistory extends Atm {
    String backupState();
    void restoreLastState(String state);
}
