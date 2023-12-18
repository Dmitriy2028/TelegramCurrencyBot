package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

public class CurrencyService {

    public static String processCurrencyData(String jsonData, User user) {
        JSONArray jsonArray = new JSONArray(jsonData);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currencyObject = jsonArray.getJSONObject(i);
            int currencyCodeA = currencyObject.getInt("currencyCodeA");
            int currencyCodeB = currencyObject.getInt("currencyCodeB");

            if (currencyCodeB == 980 && (currencyCodeA == 840 || currencyCodeA == 978)) {
                String currencyName = currencyCodeA == 840 ? "Доллар" : "Евро";
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
