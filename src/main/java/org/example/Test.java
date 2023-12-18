package org.example;

import java.util.LinkedHashMap;

public class Test {

    public static String example(User user, LinkedHashMap<String, Double> returnFromBank) {
        String result = "";
        for (String currency : user.getCurrency()) {
            result += "Курс в " + BankNames.valueOf(user.getBank()) + ": " + currency + "/UAH\n";
            for (String i : returnFromBank.keySet()) {
                if (currency.equals(i.substring(0, i.indexOf('_')))) {
                    result += BuySell.valueOf(
                            i.substring(i.lastIndexOf('_') + 1))
                            + ": " + returnFromBank.get(i) + "\n";
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        User user = new User(1L);
        user.addCurrency("EUR");

        LinkedHashMap<String, Double> returnFromBank = new LinkedHashMap<>();
        returnFromBank.put("USD_buy", 24.5);
        returnFromBank.put("USD_sell", 25.5);
        returnFromBank.put("EUR_buy", 26.5);
        returnFromBank.put("EUR_sell", 27.5);

        System.out.println(example(user,returnFromBank));
    }
}
