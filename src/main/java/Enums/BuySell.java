package Enums;

public enum BuySell {
    buy,
    sell;

    @Override
    public String toString() {
        switch (this) {
            case buy: return "Купівля";
            case sell: return "Продаж";
        }
        return super.toString();
    }
}
