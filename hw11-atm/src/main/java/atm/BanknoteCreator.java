package atm;

import java.util.ArrayList;
import java.util.List;

public class BanknoteCreator {
    public List<Banknote> createBanknotes(int value, int count) {
        List<Banknote> result = new ArrayList<>();
        for (int i=0; i < count; i++) {
            result.add(Banknote.values()[value]);
        }
        return result;
    }
}
