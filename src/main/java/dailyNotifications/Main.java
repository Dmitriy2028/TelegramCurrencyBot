package dailyNotifications;

import org.quartz.*;



public class Main
{
    public static void main( String[] args ) throws SchedulerException {
//        //створюємо робочий план
//        JobDetail job  = JobBuilder.newJob(NotificationJob.class)
//                .withIdentity("notificationJob", "group1")
//                .build();
//
//        //Встановлюємо час виконання
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity("notificationTrigger", "group1")
//                .startNow()
//                .withSchedule(
//                        CronScheduleBuilder.dailyAtHourAndMinute(13,48)
//                )
//                .build();
//
//        //Планувальник
//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job, trigger);
NotificationSchedule notificationSchedule = new NotificationSchedule(15, 7);
    }
}
