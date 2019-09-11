package department;

import atm.BanknoteCreator;

public class DepartmentUtils {
    private BanknoteCreator banknoteCreator;

    BanknoteCreator getBanknoteCreator() {
        if (banknoteCreator == null) {
            banknoteCreator = new BanknoteCreator();
        }

        return banknoteCreator;
    }
}
