package Enums;

public enum BankNames {
    PRIVAT,
    NBU,
    MONO;

    @Override
    public String toString() {
        switch (this) {
            case PRIVAT: return "Приватбанк";
            case NBU: return "НБУ";
            case MONO: return "Monobank";
        }
        return super.toString();
    }
}
