package customer.affeliateconsumer;

import customer.affeliateconsumer.service.MailService;
import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by roman rasskazov on 01.06.2015.
 */
public class ExceptionLogger {

    public static boolean SEND_MAIL = true;

    public static void logAndSendError(Logger log, Exception e, String message){
        if (message != null) {
            log.error(message);
        }
        if (e != null) {
            log.error(e.getMessage(), e);
        }
        StringWriter sw = new StringWriter();
        PrintWriter letter = new PrintWriter(sw);
        letter.println("ERROR occured:\n\n");
        if (message != null) {
            letter.println(message);
            letter.println();
        }
        if (e != null) {
            e.printStackTrace(letter);
        }
        if (SEND_MAIL) {
            MailService.sendMail("RestConsumer Error", sw.toString());
        }
    }
}
