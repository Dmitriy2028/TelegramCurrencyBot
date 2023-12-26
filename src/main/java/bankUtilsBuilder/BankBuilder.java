package bankUtilsBuilder;


import enums.BankNames;

import java.util.List;

public  class BankBuilder {
    private BankNames bankName;
    private String url;

    private CurrenciesAndCodeEnum currencyEnum;
    private List<CurrenciesAndCodeEnum>  listCurrencyEnum;

    private String currency_USD;
    private String currency_EUR;
    private String jsonCurrencyCode;
    private String currencyCode_A;
    private String currencyCode_B;
    private String buy;
    private String sell;
    private Integer currencyCouter;
    private boolean isMono;

    public BankBuilder bankName(BankNames bankName) {
        this.bankName = bankName;
        return this;
    }
    public BankBuilder url(String url) {
        this.url = url;
        return this;
    }

    public BankBuilder currencyEnum(CurrenciesAndCodeEnum currencyEnum) {
        this.currencyEnum = currencyEnum;
        return this;
    }

    public BankBuilder listCurrencyEnum(List<CurrenciesAndCodeEnum> listCurrencyEnum) {
        this.listCurrencyEnum = listCurrencyEnum;
        return this;
    }

    public BankBuilder currency_USD(String currency_USD) {
        this.currency_USD = currency_USD;
        return this;
    }

    public BankBuilder currency_EUR(String currency_EUR) {
        this.currency_EUR = currency_EUR;
        return this;
    }
    public BankBuilder jsonCurrencyCode(String jsonCurrencyCode) {
        this.jsonCurrencyCode = jsonCurrencyCode;
        return this;
    }

    public BankBuilder currencyCode_A(String currencyCode_A) {
        this.currencyCode_A = currencyCode_A;
        return this;
    }

    public BankBuilder currencyCode_B(String currencyCode_B) {
        this.currencyCode_B = currencyCode_B;
        return this;
    }

    public BankBuilder buy(String buy) {
        this.buy = buy;
        return this;
    }

    public BankBuilder sell(String sell) {
        this.sell = sell;
        return this;
    }

    public BankBuilder currencyCouter(Integer currencyCouter) {
        this.currencyCouter = currencyCouter;
        return this;
    }

    public BankBuilder isMono(boolean isMono) {
        this.isMono = isMono;
        return this;
    }


    public Bank build(){
        return new Bank(bankName, url, currencyEnum, listCurrencyEnum, currency_USD, currency_EUR, jsonCurrencyCode, currencyCode_A, currencyCode_B, buy, sell, currencyCouter, isMono);
    }

}
