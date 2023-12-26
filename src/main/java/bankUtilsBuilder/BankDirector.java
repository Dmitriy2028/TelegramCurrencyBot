package bankUtilsBuilder;

import enums.BankNames;
import java.util.Arrays;

public class BankDirector {

    public void buildNBU(BankBuilder builder){
        builder .bankName(BankNames.NBU)
                .url("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json")
                // temporary for testing
                .listCurrencyEnum(Arrays.asList(CurrenciesAndCodeEnum.USD,CurrenciesAndCodeEnum.EUR))
                .currency_USD(CurrenciesAndCodeEnum.USD.name())
                .currency_EUR(CurrenciesAndCodeEnum.EUR.name())
                .jsonCurrencyCode("cc")
                .buy("rate")
                .sell("rate")
                // temporary for future testing
                .currencyCouter(Arrays.asList(CurrenciesAndCodeEnum.USD,CurrenciesAndCodeEnum.EUR).size())
        ;
    }

    public void buildPRIVAT(BankBuilder builder){
        builder .bankName(BankNames.PRIVAT)
                .url("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5")
                // temporary for testing
                .listCurrencyEnum(Arrays.asList(CurrenciesAndCodeEnum.USD,CurrenciesAndCodeEnum.EUR))
                .currency_USD(CurrenciesAndCodeEnum.USD.name())
                .currency_EUR(CurrenciesAndCodeEnum.EUR.name())
                .jsonCurrencyCode("ccy")
                .buy("buy")
                .sell("sale")
                // temporary for future testing
                .currencyCouter(Arrays.asList(CurrenciesAndCodeEnum.USD,CurrenciesAndCodeEnum.EUR).size())
        ;
    }
    public void buildMONO(BankBuilder builder){
        builder .bankName(BankNames.MONO)
                .url("https://api.monobank.ua/bank/currency")
                // temporary for testing
                .listCurrencyEnum(Arrays.asList(CurrenciesAndCodeEnum.USD,CurrenciesAndCodeEnum.EUR))
                .currency_USD(CurrenciesAndCodeEnum.USD.getCurrencyCode())
                .currency_EUR(CurrenciesAndCodeEnum.EUR.getCurrencyCode())
                .jsonCurrencyCode("currencyCodeA")
                .currencyCode_B("currencyCodeB")
                .buy("rateBuy")
                .sell("rateSell")
                .isMono(true)
                // temporary for future testing
                .currencyCouter(Arrays.asList(CurrenciesAndCodeEnum.USD,CurrenciesAndCodeEnum.EUR).size())
        ;
    }
}
