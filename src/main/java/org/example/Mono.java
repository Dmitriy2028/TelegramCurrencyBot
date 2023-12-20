package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedHashMap;

public class Mono {

    public static LinkedHashMap<String, Double> getCurrencyRates() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://api.monobank.ua/bank/currency");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    return processCurrencyData(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LinkedHashMap<>();
    }

    private static LinkedHashMap<String, Double> processCurrencyData(String jsonData) {
        JSONArray jsonArray = new JSONArray(jsonData);
        LinkedHashMap<String, Double> currencyRates = new LinkedHashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currencyObject = jsonArray.getJSONObject(i);
            int currencyCodeA = currencyObject.getInt("currencyCodeA");
            int currencyCodeB = currencyObject.getInt("currencyCodeB");

            if (currencyCodeB == 980 && (currencyCodeA == 840 || currencyCodeA == 978)) {
                String currencyName = currencyCodeA == 840 ? "USD" : "EUR";
                Double rateBuy = currencyObject.getDouble("rateBuy");
                Double rateSell = currencyObject.getDouble("rateSell");

                currencyRates.put(currencyName + "_buy", rateBuy);
                currencyRates.put(currencyName + "_sell", rateSell);
            }
        }

        return currencyRates;
    }
}





