package telegramBot;

import bankUtils.BankUtil;
import enums.BankNames;
import prettyOutput.OutputTextCreator;
import dailyNotifications.NotificationSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.*;

public class TelegramFront extends TelegramLongPollingBot {


    private static final Map<Long, User> USERS = new HashMap<>();

//    List commandsList = new ArrayList();
//    commandsList.add(new BotCommand("commandName", "description"));
//    this.execute(new SetMyCommands(commands));

    @Override
    public String getBotUsername() {
        return TelegramBotValues.NAME.toString();
    }

    @Override
    public String getBotToken() {
        return TelegramBotValues.TOKEN.toString();
    }

    private final NotificationSender notificationSender = new NotificationSender(this);
    private final BankUtil bankUtil = new BankUtil();
    private final OutputTextCreator outputTextCreator = new OutputTextCreator();

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            onStart(chatId);
        }
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            if (data.matches("chars_after_coma_\\d")) {
                charsAfterComaData(update, chatId, data);
            } else if (data.matches("values_\\w+")) {
                valuesData(update, chatId, data);
            } else if (data.matches("time_of_notifications_\\w+")) {
                timeOfNotificationsData(update, chatId, data);
            } else if (data.matches("bank_\\w+")) {
                bankData(update, chatId, data);
            } else if (data.equals("get_info")) {
                getInfoSelect(chatId);
            } else if (data.equals("settings")) {
                settingsSelect(chatId);
            } else if (data.equals("chars_after_coma")) {
                charsAfterComeSelect(chatId);
            } else if (data.equals("bank")) {
                BankSelect(chatId);
            } else if (data.equals("values")) {
                valuesSelect(chatId);
            } else if (data.equals("time_of_notifications")) {
                timeOfNotificationsSelect(chatId);
            }

        }
    }

    private void onStart(Long chatId) {
        USERS.put(chatId, new User(chatId));
        SendMessage message = createMessage("Ласкаво просимо. Цей бот допоможе відслідковувати актуальні курси валют");
        message.setChatId(chatId);

        message.setReplyMarkup(attachButtons(startButtons()));

        sendApiMethodAsync(message);
    }

    private void timeOfNotificationsSelect(Long chatId) {
        SendMessage message = createMessage("Виберіть час сповіщення");
        message.setChatId(chatId);

        message.setReplyMarkup(attachButtons(timeOfNotificationsButtons(USERS.get(chatId))));
        sendApiMethodAsync(message);
    }

    private void valuesSelect(Long chatId) {
        SendMessage message = createMessage("Виберіть валюту");
        message.setChatId(chatId);

        message.setReplyMarkup(attachButtons(valuesButtons(USERS.get(chatId))));
        sendApiMethodAsync(message);
    }

    private void BankSelect(Long chatId) {
        SendMessage message = createMessage("Виберіть банк");
        message.setChatId(chatId);

        message.setReplyMarkup(attachButtons(bankButtons(USERS.get(chatId))));
        sendApiMethodAsync(message);
    }

    private void charsAfterComeSelect(Long chatId) {
        SendMessage message = createMessage("Виберіть кількість знаків після коми");
        message.setChatId(chatId);

        message.setReplyMarkup(attachButtons(charsAfterComaButtons(USERS.get(chatId))));
        sendApiMethodAsync(message);
    }

    private void settingsSelect(Long chatId) {
        SendMessage message = createMessage("Налаштування");
        message.setChatId(chatId);

        message.setReplyMarkup(attachButtons(settingsButtons()));
        sendApiMethodAsync(message);
    }

    private void getInfoSelect(Long chatId) {
        String infoMessage = outputTextCreator.prettyOutput(USERS.get(chatId), bankUtil.getCource(BankNames.valueOf(USERS.get(chatId).getBank())));

        SendMessage message = createMessage(infoMessage);
        message.setChatId(chatId);

        message.setReplyMarkup(attachButtons(getInfoButtons()));

        sendApiMethodAsync(message);
    }

    private void bankData(Update update, Long chatId, String data) {
        USERS.get(chatId).setBank(data.substring(data.lastIndexOf('_') + 1));

        EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
        editedMessage.setChatId(chatId);
        editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
        editedMessage.setReplyMarkup(attachButtons(bankButtons(USERS.get(chatId))));

        try {
            execute(editedMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void timeOfNotificationsData(Update update, Long chatId, String data) {
        USERS.get(chatId).setTimeOfNotifications(data.substring(data.lastIndexOf('_') + 1));
        if (data.substring(data.lastIndexOf('_') + 1).equals("off")) {
            notificationSender.removeUser(USERS.get(chatId));
        } else {
            notificationSender.removeUser(USERS.get(chatId));
            notificationSender.addUser(USERS.get(chatId));
        }
        EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
        editedMessage.setChatId(chatId);
        editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
        editedMessage.setReplyMarkup(attachButtons(timeOfNotificationsButtons(USERS.get(chatId))));

        try {
            execute(editedMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void valuesData(Update update, Long chatId, String data) {
        if (USERS.get(chatId).isCurrencyDefault()) {
            USERS.get(chatId).currencyWasChanged();
        }
        USERS.get(chatId).checkCurrency(data.substring(data.lastIndexOf('_') + 1));

        EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
        editedMessage.setChatId(chatId);
        editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
        editedMessage.setReplyMarkup(attachButtons(valuesButtons(USERS.get(chatId))));

        try {
            execute(editedMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void charsAfterComaData(Update update, Long chatId, String data) {
        USERS.get(chatId).setCharsAfterComa(Integer.parseInt(data.substring(data.lastIndexOf('_') + 1)));

        EditMessageReplyMarkup editedMessage = new EditMessageReplyMarkup();
        editedMessage.setChatId(chatId);
        editedMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editedMessage.setInlineMessageId(update.getCallbackQuery().getInlineMessageId());
        editedMessage.setReplyMarkup(attachButtons(charsAfterComaButtons(USERS.get(chatId))));

        try {
            execute(editedMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotificationMessage(Long chatId) {
        //OutputTextCreator outputTextCreator = new OutputTextCreator();
        String infoMessage = outputTextCreator.prettyOutput(USERS.get(chatId), bankUtil.getCource(BankNames.valueOf(USERS.get(chatId).getBank())));

        SendMessage message = createMessage(infoMessage);

        message.setChatId(chatId);

        //attachButtons(message, getInfoButtons());

        sendApiMethodAsync(message);
    }

    private Map<String, String> startButtons() {
        Map<String, String> startButtons = new LinkedHashMap<>();

        startButtons.put("get_info", "Отримати інфо ℹ️");
        startButtons.put("settings", "Налаштування ⚙️");

        return startButtons;
    }

    private Map<String, String> getInfoButtons() {
        Map<String, String> getInfoButtons = new LinkedHashMap<>();

        getInfoButtons.put("get_info", "Отримати інфо ℹ️");
        getInfoButtons.put("settings", "Налаштування ⚙️");

        return getInfoButtons;
    }

    private Map<String, String> settingsButtons() {
        Map<String, String> settingsButtons = new LinkedHashMap<>();

        settingsButtons.put("chars_after_coma", "Кількість знаків після коми \uD83D\uDD22");
        settingsButtons.put("bank", "Банк \uD83C\uDFE6");
        settingsButtons.put("values", "Валюти \uD83D\uDCB0");
        settingsButtons.put("time_of_notifications", "Час сповіщень \uD83D\uDD53");

        return settingsButtons;
    }

    private Map<String, String> charsAfterComaButtons(User user) {
        Map<String, String> charsAfterComaButtons = new LinkedHashMap<>();

        charsAfterComaButtons.put("chars_after_coma_2", "2");
        charsAfterComaButtons.put("chars_after_coma_3", "3");
        charsAfterComaButtons.put("chars_after_coma_4", "4");

        charsAfterComaButtons.put("chars_after_coma_" + user.getCharsAfterComa(), user.getCharsAfterComa() + "✅");

        return charsAfterComaButtons;
    }

    private Map<String, String> valuesButtons(User user) {
        Map<String, String> valuesButtons = new LinkedHashMap<>();

        valuesButtons.put("values_USD", "USD");
        valuesButtons.put("values_EUR", "EUR");
        if (!user.isCurrencyDefault()) {
            for (String currency : user.getCurrency()) {
                valuesButtons.put("values_" + currency, currency + "✅");
            }
        }
        return valuesButtons;
    }

    private Map<String, String> bankButtons(User user) {
        Map<String, String> bankButtons = new LinkedHashMap<>();

        bankButtons.put("bank_NBU", "НБУ");
        bankButtons.put("bank_PRIVAT", "ПриватБанк");
        bankButtons.put("bank_MONO", "Монобанк");

        bankButtons.put("bank_" + user.getBank(), bankButtons.get("bank_" + user.getBank()) + "✅");

        return bankButtons;
    }

    private Map<String, String> timeOfNotificationsButtons(User user) {
        Map<String, String> timeOfNotificationsButtons = new LinkedHashMap<>();

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

        timeOfNotificationsButtons.put("time_of_notifications_" + user.getTimeOfNotifications(), user.getTimeOfNotifications() + "✅");

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

    private InlineKeyboardMarkup attachButtons(Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (String buttonValue : buttons.keySet()) {
            String buttonName = buttons.get(buttonValue);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonName);
            button.setCallbackData(buttonValue);

            keyboard.add(List.of(button));
        }
        markup.setKeyboard(keyboard);
        return markup;
        //message.setReplyMarkup(markup);
    }
}