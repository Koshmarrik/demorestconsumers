package customer.affeliateconsumer;

import customer.affeliateconsumer.service.CJService;
import customer.affeliateconsumer.service.LinkShareService;
import customer.affeliateconsumer.service.MongoService;
import customer.affeliateconsumer.task.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by roman rasskazov on 31.05.2015.
 */
public class RestScheduler {

    private static final Logger log = Logger.getLogger(RestScheduler.class);

    private static final int EXECUTOR_POOL_SIZE = 10;
    private static final String RUN_PERIOD_SUFFIX = "_RUN_PERIOD_IN_HOURS";
    private static final String START_TIME_SUFFIX = "_START_TIME_IN_HOURS";

    private static final List<RestTask> tasks;

    static {
        tasks = new ArrayList<>();
        tasks.add(new CJUpdateAdvertiserCommissionsTask());
        tasks.add(new CJUpdateTransactionsTask());
        tasks.add(new LinkShareUpdateAdvertiserCommissionsTask());
        tasks.add(new LinkShareRequestNewTransactionsTask());
        tasks.add(new LinkShareRequestConfirmedTransactionsTask());
        tasks.add(new LinkShareRequestAvailableTransactionsTask());
    }

    private static List<ScheduledFuture> scheduledList = new ArrayList<ScheduledFuture>();

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXECUTOR_POOL_SIZE);

    public static void runScheduler(){
        //create first to avoid services creation synchronisation
        CJService.getInstance();
        LinkShareService.getInstance();
        for (RestTask task : tasks){
            double startTimeHours = MongoService.getDoubleParameter(task.getParameterName() + START_TIME_SUFFIX);
            double periodHours = MongoService.getDoubleParameter(task.getParameterName() + RUN_PERIOD_SUFFIX);
            Calendar midnight = Calendar.getInstance();
            midnight.add(Calendar.DATE, -1);
            midnight.set(Calendar.HOUR, 0);
            midnight.set(Calendar.MINUTE, 0);
            midnight.set(Calendar.SECOND, 0);
            midnight.set(Calendar.MILLISECOND, 0);
            long startTime = midnight.getTimeInMillis() + (long) (startTimeHours * 60 * 60 * 1000);
            long period = (long) (periodHours * 60 * 60 * 1000);
            long delay = (((System.currentTimeMillis() - startTime) / period) + 1) * period + startTime - System.currentTimeMillis();
            scheduledList.add(executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS));
        }
    }

    public static void releaseThreads (){
        for (ScheduledFuture task : scheduledList){
            task.cancel(false);
            log.debug("release");
        }
    }
}
