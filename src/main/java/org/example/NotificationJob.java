package org.example;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NotificationJob implements Job {
    private TelegramFront bot;
    private User user;
    public void NotificationJob(TelegramFront bot){
        this.bot = bot;
    }
    private Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //System.out.println("Send message at 9.30");
        TelegramFront telegramFront = new TelegramFront();
        try {
            telegramFront.execute()
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public static void onStart() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }

    public void  setNotification (User user) throws SchedulerException {
        JobDetail job  = JobBuilder.newJob(NotificationJob.class)
                .withIdentity(user.getChatId().toString())
                .build();
        //System.out.println(job.getKey());
        //Встановлюємо час виконання
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(user.getChatId()+"Trigger")
                .startNow()
                .withSchedule(
                        CronScheduleBuilder.dailyAtHourAndMinute(Integer.valueOf(user.getTimeOfNotifications()),0)
                )
                .build();

        //Планувальник
        scheduler.scheduleJob(job, trigger);

    }
    public void removeNotification(User user) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(user.getChatId().toString());
        scheduler.deleteJob(jobKey);
    }

    public void readNotificationsFromFile(){

    }

}
