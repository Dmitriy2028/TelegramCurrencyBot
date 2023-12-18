package org.example;

public interface CurrencyInfoProvider {
    default String getCurrencyInfo(User user) {
        String bankName = user.getBank();
        String currencyInfo = "Курс валют ";

        switch (bankName) {
            case "Приват":
                currencyInfo += "в ПриватБанке";
                break;
            case "Моно":
                currencyInfo += "в МоноБанке";
                break;
            case "НБУ":
                currencyInfo += "в Национальном банке Украины";
                break;
            default:
                currencyInfo += "в неизвестном банке";
        }

        return currencyInfo;
    }
}

