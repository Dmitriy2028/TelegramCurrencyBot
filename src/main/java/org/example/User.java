package org.example;

import java.util.List;
//test
//test2
public class User {
    private Long chatId;
    private int charsAfterComa;
    private String bank;
    private int timeOfNotifications;
    private List<String> currency;

    public User(Long chatId) {
        this.chatId = chatId;
        this.charsAfterComa = 2;
        this.bank = "Privat";
        this.timeOfNotifications = 0;
        this.currency.add("USD");
    }

    public Long getChatId() {
        return chatId;
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

    public int getTimeOfNotifications() {
        return timeOfNotifications;
    }

    public void setTimeOfNotifications(int timeOfNotifications) {
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
}
