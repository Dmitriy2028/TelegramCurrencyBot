package BankUtils;

import Enums.BankNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class BankUtil {
    private static final String NBU_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    private static final String PRIVAT_CASH_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
    private static final String PRIVAT_CASHLESS_URL = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=11";
    private static final String MONO_URL = "https://api.monobank.ua/bank/currency";
    private static final String USD = "USD";
    private static final String EUR = "EUR";

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private String url;
    private String currency_USD;
    private String currency_EUR;
    private String currencyCode;
    private String currencyCodeB_mono;   // to check and skip the rate of EUR/USD in MONO json
    private String buy;
    private String sell;
    private boolean isMono;

    public Map<String, Double> getCource(BankNames bankName) {
        setVariablesForBank(bankName);
        return getCurrency();
    }

    private Map<String, Double> getCurrency() {
        final Map<String, Double> mapCurrency = new LinkedHashMap<>();
        final HttpResponse<String> response = sendGET(URI.create(url));
        JSONArray jsonArray = new JSONArray(response.body());
        return selectCurrency(mapCurrency, jsonArray);
    }

    private Map<String, Double> selectCurrency(Map<String, Double> map, JSONArray jsonArr) {
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObject = jsonArr.getJSONObject(i);
            if (currency_USD.equals(String.valueOf(jsonObject.get(currencyCode)))) {
                addCurrencyToMap(map, jsonObject, USD);
            } else if (currency_EUR.equals(String.valueOf(jsonObject.get(currencyCode)))) {
                // only for MONO, if it's rate for EUR/USD then scip it
                if (isMono) {
                    if (currency_USD.equals(String.valueOf(jsonObject.get(currencyCodeB_mono))))
                        continue;
                }
                addCurrencyToMap(map, jsonObject, EUR);
            }
        }
        isMono = false;
        return map;
    }

    private void addCurrencyToMap(Map<String, Double> map, JSONObject jsonObject, String currencyAcronym) {
        map.put(currencyAcronym + "_BUY", jsonObject.getDouble(buy));
        map.put(currencyAcronym + "_SELL", jsonObject.getDouble(sell));
    }

    private void setVariablesForBank(BankNames bankName) {
        switch (bankName) {
            case NBU -> initVarForBank(
                    NBU_URL
                    , USD
                    , EUR
                    , "cc"
                    , "none"
                    , "rate"
                    , "rate"
                    , false
            );
            case PRIVAT -> initVarForBank(
                    PRIVAT_CASHLESS_URL
                    , USD
                    , EUR
                    , "ccy"
                    , "none"
                    , "buy"
                    , "sale"
                    , false
            );
            case MONO -> initVarForBank(
                    MONO_URL
                    , "840"
                    , "978"
                    , "currencyCodeA"
                    , "currencyCodeB"
                    , "rateBuy"
                    , "rateSell"
                    , true
            );
        }
    }

    private void initVarForBank(String url, String currency_USD, String currency_EUR, String currencyCode, String currencyCodeBmono, String buy, String sell, boolean isMono) {
        this.url = url;
        this.currency_USD = currency_USD;
        this.currency_EUR = currency_EUR;
        this.currencyCode = currencyCode;
        this.currencyCodeB_mono = currencyCodeBmono;
        this.buy = buy;
        this.sell = sell;
        this.isMono = isMono;
    }

    private HttpResponse<String> sendGET(URI uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        HttpResponse<String> response = null;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}