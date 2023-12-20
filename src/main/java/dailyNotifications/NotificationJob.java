package dailyNotifications;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NotificationJob implements Job {

    // потрібно додати логіку, щоб виводило необхідне сповіщення
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Send message at 9.30");
    }
}
