package enums;

public enum BuySell {
    BUY,
    SELL;

    @Override
    public String toString() {
        return switch (this) {
            case BUY -> "Купівля";
            case SELL -> "Продаж";
        };
    }
}
