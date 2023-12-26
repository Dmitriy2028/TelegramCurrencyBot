package bankUtilsBuilder;

import enums.BankNames;

import java.util.List;

public class Bank {

    private BankNames bankName;
    private String url;

//    private Map<String, Integer> currencyMap ;
    private CurrenciesAndCodeEnum currencyEnum;
    private List<CurrenciesAndCodeEnum> listCurrencyEnum;

    private String currency_USD;
    private String currency_EUR;
    private String jsonCurrencyCode;
    private String currencyCode_A;
    private String currencyCode_B;
    private String buy;
    private String sell;
    private Integer currencyCouter;
    private boolean isMono = false;

    Bank(){
    }

    Bank(BankNames bankName, String url, CurrenciesAndCodeEnum currencyEnum, List<CurrenciesAndCodeEnum> listCurrencyEnum, String currency_USD, String currency_EUR, String jsonCurrencyCode, String currencyCode_A, String currencyCode_B, String buy, String sell, Integer currencyCouter, boolean isMono) {
        this.bankName = bankName;
        this.url = url;

        this.currencyEnum = currencyEnum;
        this.listCurrencyEnum = listCurrencyEnum;

        this.currency_USD = currency_USD;
        this.currency_EUR = currency_EUR;
        this.jsonCurrencyCode = jsonCurrencyCode;
        this.currencyCode_A = currencyCode_A;
        this.currencyCode_B = currencyCode_B;
        this.buy = buy;
        this.sell = sell;
        this.currencyCouter = currencyCouter;
        this.isMono = isMono;
    }

    public BankNames getBankName() {
        return bankName;
    }

    public String getUrl() {
        return url;
    }

    public CurrenciesAndCodeEnum getCurrencyEnum() {
        return currencyEnum;
    }

    public List<CurrenciesAndCodeEnum> getListCurrencyEnum() {
        return listCurrencyEnum;
    }

    public String getCurrency_USD() {
        return currency_USD;
    }

    public String getCurrency_EUR() {
        return currency_EUR;
    }

    public String jsonCurrencyCode() {
        return jsonCurrencyCode;
    }

    public String getCurrencyCode_A() {
        return currencyCode_A;
    }

    public String getCurrencyCode_B() {
        return currencyCode_B;
    }

    public String getBuy() {
        return buy;
    }

    public String getSell() {
        return sell;
    }

    public Integer getCurrencyCouter() {
        return currencyCouter;
    }

    public boolean isMono() {
        return isMono;
    }

    @Override
    public String toString() {
        return "{" +
                "\nbankName=" + bankName +
                ", \nurl='" + url + '\'' +
                ", \ncurrencyEnum=" + currencyEnum +
                ", \ncurrencyEnumArray=" + listCurrencyEnum +
                ", \ncurrency_USD='" + currency_USD + '\'' +
                ", \ncurrency_EUR='" + currency_EUR + '\'' +
                ", \njsonCurrencyCode='" + jsonCurrencyCode + '\'' +
                ", \ncurrencyCode='" + currencyCode_A + '\'' +
                ", \ncurrencyCodeB_mono='" + currencyCode_B + '\'' +
                ", \nbuy='" + buy + '\'' +
                ", \nsell='" + sell + '\'' +
                ", \ncurrencyCouterl='" + currencyCouter + '\'' +
                ", \nisMono=" + isMono +
                "\n}";
    }
}
