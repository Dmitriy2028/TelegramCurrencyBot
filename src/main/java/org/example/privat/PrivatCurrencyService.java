package org.example.privat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.privat.dto.Currency;
import org.example.privat.dto.CurrencyItem;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;

public class PrivatCurrencyService {
    public LinkedHashMap<String, Double> getRate() {
        String url = "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=5";

        //Get JSON
        String json;
        try {
            json = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            throw new IllegalStateException("Can`t connect to Privat API");
        }

        //Convert JSON => Java Object
        Type typeToken = TypeToken
                .getParameterized(List.class, CurrencyItem.class)
                .getType();
        List<CurrencyItem> currencyItems = new Gson().fromJson(json, typeToken);

        LinkedHashMap<String, Double> ratesMap = new LinkedHashMap<>();

        // Adding USD rates
        ratesMap.put("USD_buy", getRateForCurrency(currencyItems, Currency.USD, true));
        ratesMap.put("USD_sell", getRateForCurrency(currencyItems, Currency.USD, false));

        // Adding EUR rates
        ratesMap.put("EUR_buy", getRateForCurrency(currencyItems, Currency.EUR, true));
        ratesMap.put("EUR_sell", getRateForCurrency(currencyItems, Currency.EUR, false));

        return ratesMap;
    }

    private double getRateForCurrency(List<CurrencyItem> currencyItems, Currency currency, boolean isBuyRate) {
        return currencyItems.stream()
                .filter(c -> c.getCcy() == currency)
                .filter(c -> c.getBase_ccy() == Currency.UAH)
                .map(isBuyRate ? CurrencyItem::getBuy : CurrencyItem::getSale)
                .findFirst()
                .orElseThrow();
    }
}
