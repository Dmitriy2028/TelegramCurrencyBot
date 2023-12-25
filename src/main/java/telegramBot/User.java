package telegramBot;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final Long CHAT_ID;
    private int charsAfterComa;
    private String bank;
    private String timeOfNotifications;
    private List<String> currency = new ArrayList<>();
    private boolean currencyIsDefault;

    public User(Long chatId) {
        this.CHAT_ID = chatId;
        this.charsAfterComa = 2;
        this.bank = "PRIVAT";
        this.timeOfNotifications = "off";
        this.currency.add("USD");
        this.currencyIsDefault = true;
    }

    public Long getChatId() {
        return CHAT_ID;
    }

    public int getCharsAfterComa() {
        return charsAfterComa;
    }

    public void setCharsAfterComa(int charsAfterComa) {
        this.charsAfterComa = charsAfterComa;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getTimeOfNotifications() {
        return timeOfNotifications;
    }

    public void setTimeOfNotifications(String timeOfNotifications) {
        this.timeOfNotifications = timeOfNotifications;
    }

    public List<String> getCurrency() {
        return currency;
    }

    public void addCurrency(String currency) {
        this.currency.add(currency);
    }

    public void removeCurrency(String currency) {
        this.currency.remove(currency);
    }

    public void checkCurrency(String currency){
        if (this.currency.contains(currency)){
            removeCurrency(currency);
        }else {
            addCurrency(currency);
        }
    }

    public void currencyWasChanged(){
        this.currencyIsDefault = false;
        removeCurrency("USD");
    }
    public boolean isCurrencyDefault() {
        return currencyIsDefault;
    }




}
