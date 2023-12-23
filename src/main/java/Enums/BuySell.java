package Enums;

public enum BuySell {
    BUY,
    SELL;

    @Override
    public String toString() {
        switch (this) {
            case BUY: return "Купівля";
            case SELL: return "Продаж";
        }
        return super.toString();
    }
}
