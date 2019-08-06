public enum Banknote {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    HUNDRED(100),
    TWO_HUNDREDS(200);

    private final int amount;

    Banknote(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
