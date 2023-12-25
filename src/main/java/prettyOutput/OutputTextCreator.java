package prettyOutput;

import enums.BankNames;
import enums.BuySell;
import rounding.RoundValues;
import telegramBot.User;

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
                    result.append("     ")
                            .append(BuySell.valueOf(bankCurrency.substring(bankCurrency.lastIndexOf('_') + 1)))
                            .append(": ")
                            .append(rv.roundValue(returnFromBank.get(bankCurrency)))
                            .append("\n");
                }
            }
        }
        return result.toString();
    }
}
