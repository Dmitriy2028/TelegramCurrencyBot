package org.example;

import org.example.privat.PrivatCurrencyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

public class CurrencyRounder {

    public LinkedHashMap<String, BigDecimal> roundBuySellValues(LinkedHashMap<String, Double> currencyMap, int charsAfterComa) {
        LinkedHashMap<String, BigDecimal> roundedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : currencyMap.entrySet()) {
            String currencyCode = entry.getKey();
            Double originalValue = entry.getValue();

            BigDecimal originalBigDecimal = BigDecimal.valueOf(originalValue);

            // Визначення, чи це значення для купівлі чи продажу
            RoundingMode roundingMode = currencyCode.toLowerCase().contains("buy") ? RoundingMode.CEILING : RoundingMode.DOWN;

            BigDecimal roundedBigDecimal = originalBigDecimal.setScale(charsAfterComa, roundingMode);

            roundedMap.put(currencyCode, roundedBigDecimal);
        }

        return roundedMap;
    }
//test
    public static void main(String[] args) {
        PrivatCurrencyService privatCurrencyService = new PrivatCurrencyService();
        System.out.println(privatCurrencyService.getRate());

        LinkedHashMap<String, Double> ratesMap = privatCurrencyService.getRate();
        CurrencyRounder currencyRounder = new CurrencyRounder();

        LinkedHashMap<String, BigDecimal> roundedRatesMap = currencyRounder.roundBuySellValues(ratesMap, 3);

        System.out.println(roundedRatesMap);
    }
}
