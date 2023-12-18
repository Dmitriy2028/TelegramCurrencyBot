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

public class Mono {

    public static String getCurrencyRates(User user) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            HttpGet request = new HttpGet("https://api.monobank.ua/bank/currency");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    return processCurrencyData(result, user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Не вдалося отримати дані";
    }

    private static String processCurrencyData(String jsonData, User user) {

        JSONArray jsonArray = new JSONArray(jsonData);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currencyObject = jsonArray.getJSONObject(i);
            int currencyCodeA = currencyObject.getInt("currencyCodeA");
            int currencyCodeB = currencyObject.getInt("currencyCodeB");
            if (currencyCodeB == 980 && (currencyCodeA == 840 || currencyCodeA == 978)) { // 840 - USD, 978 - EUR, 980 - UAH
                String currencyName = currencyCodeA == 840 ? "Долар" : "Євро";
                double rateBuy = currencyObject.getDouble("rateBuy");
                double rateSell = currencyObject.getDouble("rateSell");

                sb.append(currencyName)
                        .append(" - курс купівлі: ")
                        .append(String.format("%." + user.getCharsAfterComa() + "f", rateBuy))
                        .append(", курс продажу: ")
                        .append(String.format("%." + user.getCharsAfterComa() + "f", rateSell))
                        .append("; ");
            }
        }
        return sb.toString();
    }
}
