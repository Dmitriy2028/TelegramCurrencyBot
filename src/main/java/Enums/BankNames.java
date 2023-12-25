package Enums;

public enum BankNames {
    PRIVAT,
    NBU,
    MONO;

    @Override
    public String toString() {
        return switch (this) {
            case PRIVAT -> "Приватбанк";
            case NBU -> "НБУ";
            case MONO -> "Monobank";
        };
    }
}
