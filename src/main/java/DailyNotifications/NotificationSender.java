package DailyNotifications;

import TelegramBot.TelegramFront;
import TelegramBot.User;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;

public class NotificationSender {
    private static TelegramFront bot;
    private static final Map<String, Set<User>> users = new HashMap<>();

    public NotificationSender(TelegramFront bot) {
        NotificationSender.bot = bot;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new HourlyTask(), getNextExecutionTime(), 60 * 60 * 1000);
    }

    public void addUser(User user) {
        if (users.containsKey(user.getTimeOfNotifications())) {
            users.get(user.getTimeOfNotifications()).add(user);
        } else {
            Set<User> usersSet = new HashSet<>();
            usersSet.add(user);
            users.put(user.getTimeOfNotifications(), usersSet);
        }
    }

    public void removeUser(User user) {
        if (users.containsKey(user.getTimeOfNotifications())) {
            users.get(user.getTimeOfNotifications()).remove(user);
        }
    }

    static class HourlyTask extends TimerTask {
        @Override
        public void run() {
            ZonedDateTime kyivTime = ZonedDateTime.now(ZoneId.of("Europe/Kiev"));

            // Округление времени до ближайшего часа
            ZonedDateTime roundedKyivTime = kyivTime.truncatedTo(ChronoUnit.MINUTES);

            // Получение округленного часа
            int roundedHour = roundedKyivTime.getMinute() > 30 ? roundedKyivTime.getHour() + 1 : roundedKyivTime.getHour();

            for (User user : users.get(String.valueOf(roundedHour))) {
                bot.sendNotificationMessage(user.getChatId());
            }
        }
    }

    private static long getNextExecutionTime() {
        // Получение текущего времени в миллисекундах
        long currentTimeMillis = System.currentTimeMillis();

        // Вычисление времени до следующего часа
        long nextHourMillis = (currentTimeMillis / (60 * 60 * 1000) + 1) * (60 * 60 * 1000);

        return nextHourMillis - currentTimeMillis;
    }

}
