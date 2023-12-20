package dailyNotifications;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class NotificationSchedule {
    private int hours;
    private int minutes;

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public NotificationSchedule(int hours, int minutes) throws SchedulerException {
        this.hours = hours;
        this.minutes = minutes;

        //створюємо робочий план
        JobDetail job = JobBuilder.newJob(NotificationJob.class)
                .withIdentity("notificationJob", "group1")
                .build();

        //Встановлюємо час виконання
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("notificationTrigger", "group1")
                .startNow()
                .withSchedule(
                        CronScheduleBuilder.dailyAtHourAndMinute(hours, minutes)
                )
                .build();

        //Планувальник
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
