package customer.affeliateconsumer.task;

import com.sun.jersey.api.client.UniformInterfaceException;
import customer.affeliateconsumer.ExceptionLogger;
import org.apache.log4j.Logger;

/**
 * Created by roman rasskazov on 31.05.2015.
 */
public abstract class RestTask implements Runnable{

    private final Logger log = Logger.getLogger(getClass());

    abstract public String getTaskName();
    abstract public String getParameterName();
    abstract public int runTask();

    @Override
    final public void run() {
        try {
            log.debug(getTaskName() + ": start task");
            int amount = runTask();
            log.info(getTaskName() + ": processed " + amount + " records");
            log.debug(getTaskName() + ": finished task");
        } catch (UniformInterfaceException e){
            ExceptionLogger.logAndSendError(log, e, "\nError occured, response:\n" + e.getResponse().getEntity(String
                    .class) + "\n");
        } catch (Exception e){
            ExceptionLogger.logAndSendError(log, e, getTaskName() + " error occured");
        }
    }

}
