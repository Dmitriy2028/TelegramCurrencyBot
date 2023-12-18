package org.example.Scheduler;

public class SchedulerEx implements DailyNotification{
    @Override
    public void run() {
        System.out.println("Scheduler is run");
    }
}
