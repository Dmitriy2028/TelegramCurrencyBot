package PrettyOutput;

import Enums.BankNames;
import Enums.BuySell;
import Rounding.RoundValues;
import TelegramBot.User;

import java.util.Map;

public class OutputTextCreator {
    public String prettyOutput(User user, Map<String, Double> returnFromBank) {
        RoundValues rv = new RoundValues(user.getCharsAfterComa());

        StringBuilder result = new StringBuilder();
        for (String userCurrency : user.getCurrency()) {
            result.append("Курс в ")
                    .append(BankNames.valueOf(user.getBank()))
                    .append(": ")
                    .append(userCurrency)
                    .append("/UAH\n");
            for (String bankCurrency : returnFromBank.keySet()) {
                if (userCurrency.equals(bankCurrency.substring(0, bankCurrency.indexOf('_')))) {
                    result.append("     " + BuySell.valueOf(bankCurrency.substring(bankCurrency.lastIndexOf('_') + 1)))
                            .append(": ")
                            .append(rv.roundValue(returnFromBank.get(bankCurrency)))
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
