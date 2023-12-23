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
    private String currencyCodeBmono;
    private String buy;
    private String sell;
    private boolean isMono = false;

    public Map<String, Double> getCource(BankNames bankName){
        setVariablesForBank(bankName);
        return getCurrency();
    }

    private  Map<String, Double> getCurrency() {
        final Map<String, Double> mapCurrency = new LinkedHashMap<>();
        final HttpResponse<String> response = sendGET(URI.create(url));
        JSONArray jsonArray = new JSONArray(response.body());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if( currency_USD.equals( String.valueOf (jsonObject.get(currencyCode)) ) ) {
                mapCurrency.put("USD_buy", jsonObject.getDouble(buy));
                mapCurrency.put("USD_sell", jsonObject.getDouble(sell));
            }else if ( currency_EUR.equals( String.valueOf (jsonObject.get(currencyCode))) ) {
                // only for MONO, if it's rate EUR/USD then scip it
                if(isMono) {
                    if (currency_USD.equals(String.valueOf(jsonObject.get(currencyCodeBmono))))
                        continue;
                }
                mapCurrency.put("EUR_buy", jsonObject.getDouble(buy));
                mapCurrency.put("EUR_sell", jsonObject.getDouble(sell));
            }
        }
        isMono = false;
        return mapCurrency;
    }

    private  void setVariablesForBank(BankNames bankName) {
        switch (bankName){
            case NBU: {
                url = NBU_URL;
                currency_USD = USD;
                currency_EUR = EUR;
                currencyCode = "cc";
                buy = "rate";
                sell = "rate";
            } break;
            case PRIVAT: {
                url = PRIVAT_CASHLESS_URL;
                currency_USD = USD;
                currency_EUR = EUR;
                currencyCode = "ccy";
                buy = "buy";
                sell = "sale";
            }break;
            case MONO: {
                url = MONO_URL;
                currency_USD = "840";
                currency_EUR = "978";
                currencyCode = "currencyCodeA";
                // to scip  rate EUR/USD
                isMono = true;
                currencyCodeBmono = "currencyCodeB";
                buy = "rateBuy";
                sell = "rateSell";
            }
        }
    }

    private HttpResponse<String> sendGET(URI uri)  {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .build();
        HttpResponse<String> response = null;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}