package bankUtilsBuilder;

public enum CurrenciesAndCodeEnum {
     USD("840")
    ,EUR("978")
    ,PLN("985")
    ,GBP("826")
    ,UAH("980");

    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    CurrenciesAndCodeEnum(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
