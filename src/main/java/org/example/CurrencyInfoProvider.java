package org.example;

import org.example.Mono.Mono;
import org.example.nbu.Nbu;
import org.example.privat.PrivatCurrencyService;

public interface CurrencyInfoProvider {
    default String getCurrencyInfo(User user) {
        PrivatCurrencyService privat = new PrivatCurrencyService();
        Mono mono = new Mono();
        Nbu nbu = new Nbu();
        String bankName = user.getBank();

        switch (bankName) {
            case "Privat":
                return "Privat" + privat.getRate().toString();
            case "Mono":
                return "Mono" + mono.getCurrencyRates().toString();
            case "NBU":
                return "NBU" + nbu.getCource().toString();
            default:
                return "Another bank";
        }
    }
}

