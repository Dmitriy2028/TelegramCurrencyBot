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
        // Створюємо HTTP клієнт
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Створюємо запит
            HttpGet request = new HttpGet("https://api.monobank.ua/bank/currency");

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Перевіряємо статус
                if (response.getStatusLine().getStatusCode() == 200) {
                    // Отримуємо тіло відповіді та конвертуємо в рядок
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    // Обробляємо JSON відповідь
                    return processCurrencyData(result, user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Не вдалося отримати дані";
    }

    private static String processCurrencyData(String jsonData, User user) {
        // Перетворювання рядка JSON на JSONArray
        JSONArray jsonArray = new JSONArray(jsonData);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currencyObject = jsonArray.getJSONObject(i);
            // Отримати код валют
            int currencyCodeA = currencyObject.getInt("currencyCodeA");
            int currencyCodeB = currencyObject.getInt("currencyCodeB");

            // Перевіряємо, чи підходить валюта під налаштування користувача
            if (currencyCodeB == 980 && (currencyCodeA == 840 || currencyCodeA == 978)) { // 840 - USD, 978 - EUR, 980 - UAH
                String currencyName = currencyCodeA == 840 ? "Долар" : "Євро";
                double rateBuy = currencyObject.getDouble("rateBuy");
                double rateSell = currencyObject.getDouble("rateSell");

                // Форматування
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
