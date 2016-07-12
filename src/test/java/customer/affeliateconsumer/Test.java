package customer.affeliateconsumer;

import customer.affeliateconsumer.consumer.LinkShareConsumerImpl;
import customer.affeliateconsumer.service.CJService;
import customer.affeliateconsumer.service.LinkShareService;
import customer.affeliateconsumer.consumer.CJConsumerImpl;
import customer.affeliateconsumer.consumer.CJConsumerMockup;
import customer.affeliateconsumer.consumer.LinkShareConsumerMockup;
import org.apache.log4j.Logger;

/**
 * Created by roman rasskazov on 24.05.2015.
 */
public class Test {

    private static final Logger log = Logger.getLogger(Test.class);

    private static boolean MOCKUP = true;

    public static void main(String[] args) {
        try {
            ExceptionLogger.SEND_MAIL = false;
            CJService cjService = CJService.getInstance(MOCKUP ? new CJConsumerMockup() : new CJConsumerImpl());
            LinkShareService linkShareService = LinkShareService.getInstance(MOCKUP ? new LinkShareConsumerMockup() : new LinkShareConsumerImpl());

//            RestScheduler.runScheduler();
//                MailService.sendMail("Hello from SMTP", "Hi Meraj, \nHappy to inform you that smtp is configured. \n\nDynaObject");

//            cjService.updateAdvertisersCommissions();
//            cjService.requestNewTransactions();

            linkShareService.updateAdvertisersCommissions();
//            linkShareService.requestNewTransactions();
//            linkShareService.requestConfirmedTransactions();
//            linkShareService.requestAvailableTransactions();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
