package org.example;

import org.quartz.SchedulerException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import java.util.*;

public class TelegramFront extends TelegramLongPollingBot {

    private static HashMap<Long, User> users = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "Currency_group4_Bot";
    }

    @Override
    public String getBotToken() {
        return "6939606814:AAHurPGRFOC36BlmekpJw31vujhlseh3pEI";
    }
    NotificationJob notificationJob = new NotificationJob();
    String data;
    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);


        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            users.put(chatId, new User(chatId));
            SendMessage message = createMessage("Ласкаво просимо. Цей бот допоможе відслідковувати актуальні курси валют");
            message.setChatId(chatId);

            attachButtons(message, startButtons());

            sendApiMethodAsync(message);
        }
        if (update.hasCallbackQuery()) {
            data = update.getCallbackQuery().getData();

            if (data.matches("chars_after_coma_\\d")) {
                users.get(chatId).setCharsAfterComa(Integer.valueOf(data.substring(data.lastIndexOf('_') + 1)));

                EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
                editedMessage.setChatId(chatId);
                editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
                editedMessage.setReplyMarkup(changeButtons(charsAfterComaButtons(users.get(chatId))));

                try {
                    execute(editedMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (data.matches("values_\\w+")) {
                if (users.get(chatId).isCurrencyDefault()) {
                    users.get(chatId).currencyWasChanged();
                }
                users.get(chatId).checkCurrency(data.substring(data.lastIndexOf('_') + 1));

                EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
                editedMessage.setChatId(chatId);
                editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
                editedMessage.setReplyMarkup(changeButtons(valuesButtons(users.get(chatId))));

                try {
                    execute(editedMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (data.matches("time_of_notifications_\\w+")) {
                users.get(chatId).setTimeOfNotifications(data.substring(data.lastIndexOf('_') + 1));
                if (data.substring(data.lastIndexOf('_')+1)=="off"){
                    try {
                        notificationJob.removeNotification(users.get(chatId));
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    try {
                        notificationJob.setNotification(users.get(chatId));
                    } catch (SchedulerException e) {
                        throw new RuntimeException(e);
                    }
                }
                EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
                editedMessage.setChatId(chatId);
                editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
                editedMessage.setReplyMarkup(changeButtons(timeOfNotificationsButtons(users.get(chatId))));

                try {
                    execute(editedMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (data.matches("bank_\\w+")) {
                users.get(chatId).setBank(data.substring(data.lastIndexOf('_') + 1));

                EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
                editedMessage.setChatId(chatId);
                editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
                editedMessage.setReplyMarkup(changeButtons(bankButtons(users.get(chatId))));

                try {
                    execute(editedMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            if (data.equals("get_info")) {

                SendMessage message = createMessage("При натисканні на кнопку \"Отримати інфо\" користувач отримує актуальний курс відповідно до його налаштувань (округлення, банк і т.д.)");
                message.setChatId(chatId);

                attachButtons(message, getInfoButtons());

                sendApiMethodAsync(message);
            }

            if (data.equals("settings")) {

                SendMessage message = createMessage("Налаштування");
                message.setChatId(chatId);

                attachButtons(message, settingsButtons());
                sendApiMethodAsync(message);
            }

            if (data.equals("chars_after_coma")) {

                SendMessage message = createMessage("Виберіть кількість знаків після коми");
                message.setChatId(chatId);

                attachButtons(message, charsAfterComaButtons(users.get(chatId)));
                sendApiMethodAsync(message);
            }

            if (data.equals("bank")) {

                SendMessage message = createMessage("Виберіть банк");
                message.setChatId(chatId);

                attachButtons(message, bankButtons(users.get(chatId)));
                sendApiMethodAsync(message);
            }

            if (data.equals("values")) {

                SendMessage message = createMessage("Виберіть валюту");
                message.setChatId(chatId);

                attachButtons(message, valuesButtons(users.get(chatId)));
                sendApiMethodAsync(message);
            }

            if (data.equals("time_of_notifications")) {

                SendMessage message = createMessage("Виберіть час сповіщення");
                message.setChatId(chatId);

                attachButtons(message, timeOfNotificationsButtons(users.get(chatId)));
                sendApiMethodAsync(message);
            }

        }
    }

    protected void sendNotificationMessage(){

    }

    private LinkedHashMap<String, String> startButtons() {
        LinkedHashMap<String, String> startButtons = new LinkedHashMap<String, String>();

        startButtons.put("get_info", "Отримати інфо");
        startButtons.put("settings", "Налаштування");

        return startButtons;
    }

    private LinkedHashMap<String, String> getInfoButtons() {
        LinkedHashMap<String, String> getInfoButtons = new LinkedHashMap<String, String>();

        getInfoButtons.put("get_info", "Отримати інфо");
        getInfoButtons.put("settings", "Налаштування");

        return getInfoButtons;
    }

    private LinkedHashMap<String, String> settingsButtons() {
        LinkedHashMap<String, String> settingsButtons = new LinkedHashMap<String, String>();

        settingsButtons.put("chars_after_coma", "Кількість знаків після коми");
        settingsButtons.put("bank", "Банк");
        settingsButtons.put("values", "Валюти");
        settingsButtons.put("time_of_notifications", "Час сповіщень");

        return settingsButtons;
    }

    private LinkedHashMap<String, String> charsAfterComaButtons(User user) {
        LinkedHashMap<String, String> charsAfterComaButtons = new LinkedHashMap<String, String>();

        charsAfterComaButtons.put("chars_after_coma_2", "2");
        charsAfterComaButtons.put("chars_after_coma_3", "3");
        charsAfterComaButtons.put("chars_after_coma_4", "4");

        charsAfterComaButtons.put("chars_after_coma_" + user.getCharsAfterComa(), user.getCharsAfterComa() + "\u2705");

        return charsAfterComaButtons;
    }

    private LinkedHashMap<String, String> valuesButtons(User user) {
        LinkedHashMap<String, String> valuesButtons = new LinkedHashMap<String, String>();

        valuesButtons.put("values_USD", "USD");
        valuesButtons.put("values_EUR", "EUR");
        if (!user.isCurrencyDefault()) {
            for (String currency : user.getCurrency()) {
                valuesButtons.put("values_" + currency, currency + "\u2705");
            }
        }
        return valuesButtons;
    }

    private LinkedHashMap<String, String> bankButtons(User user) {
        LinkedHashMap<String, String> bankButtons = new LinkedHashMap<String, String>();

        bankButtons.put("bank_Nbu", "НБУ");
        bankButtons.put("bank_Privat", "ПриватБанк");
        bankButtons.put("bank_Mono", "Монобанк");

        bankButtons.put("bank_" + user.getBank(), bankButtons.get("bank_" + user.getBank()) + "\u2705");

        return bankButtons;
    }

    private LinkedHashMap<String, String> timeOfNotificationsButtons(User user) {
        LinkedHashMap<String, String> timeOfNotificationsButtons = new LinkedHashMap<String, String>();
        timeOfNotificationsButtons.put("time_of_notifications_9", "9");
        timeOfNotificationsButtons.put("time_of_notifications_10", "10");
        timeOfNotificationsButtons.put("time_of_notifications_11", "11");
        timeOfNotificationsButtons.put("time_of_notifications_12", "12");
        timeOfNotificationsButtons.put("time_of_notifications_13", "13");
        timeOfNotificationsButtons.put("time_of_notifications_14", "14");
        timeOfNotificationsButtons.put("time_of_notifications_15", "15");
        timeOfNotificationsButtons.put("time_of_notifications_16", "16");
        timeOfNotificationsButtons.put("time_of_notifications_17", "17");
        timeOfNotificationsButtons.put("time_of_notifications_18", "18");
        timeOfNotificationsButtons.put("time_of_notifications_off", "Вимкнути сповіщення");

        timeOfNotificationsButtons.put("time_of_notifications_" + user.getTimeOfNotifications(), user.getTimeOfNotifications() + "\u2705");

        return timeOfNotificationsButtons;
    }

    private Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }

        return null;
    }

    private SendMessage createMessage(String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setParseMode("markdown");
        return message;
    }

    private void attachButtons(SendMessage message, LinkedHashMap<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String buttonValue : buttons.keySet()) {
            String buttonName = buttons.get(buttonValue);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonName);
            button.setCallbackData(buttonValue);

            keyboard.add(Arrays.asList(button));
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }

    private InlineKeyboardMarkup changeButtons(LinkedHashMap<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String buttonValue : buttons.keySet()) {
            String buttonName = buttons.get(buttonValue);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonName);
            button.setCallbackData(buttonValue);

            keyboard.add(Arrays.asList(button));
        }
        markup.setKeyboard(keyboard);
        return markup;
    }
}