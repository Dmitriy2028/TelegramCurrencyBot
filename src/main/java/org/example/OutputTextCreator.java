package org.example;

import java.util.LinkedHashMap;

public class OutputTextCreator {
    public String prettyOutput(User user, LinkedHashMap<String, Double> returnFromBank) {
        StringBuilder result = new StringBuilder();
        for (String currency : user.getCurrency()) {
            result.append("Курс в ")
                    .append(BankNames.valueOf(user.getBank()))
                    .append(": ")
                    .append(currency)
                    .append("/UAH\n");
            for (String i : returnFromBank.keySet()) {
                if (currency.equals(i.substring(0, i.indexOf('_')))) {
                    result.append(BuySell.valueOf(i.substring(i.lastIndexOf('_') + 1)))
                            .append(": ")
                            .append(returnFromBank.get(i))
                            .append("\n");
                }
            }
        }
        return result.toString();
    }

//    public static void main(String[] args) {
//        User user = new User(1L);
//        user.addCurrency("EUR");
//
//        LinkedHashMap<String, Double> returnFromBank = new LinkedHashMap<>();
//        returnFromBank.put("USD_buy", 24.5);
//        returnFromBank.put("USD_sell", 25.5);
//        returnFromBank.put("EUR_buy", 26.5);
//        returnFromBank.put("EUR_sell", 27.5);
//
//        System.out.println(example(user,returnFromBank));
//    }
}
