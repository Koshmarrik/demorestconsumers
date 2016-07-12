package customer.affeliateconsumer;

import customer.affeliateconsumer.consumer.CJConsumerMockup;
import customer.affeliateconsumer.service.CJService;
import customer.affeliateconsumer.storage.dao.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by roman rasskazov on 01.06.2015.
 */
public class CJServiceTest {

    private static CJService cjService;

    private static RetailerCommissionDataDAO retailerCommissionDataDAO;

    private static CommissionsDAO commissionsDAO;

    @BeforeClass
    public static void eraseCollection(){
        ExceptionLogger.SEND_MAIL = false;
        retailerCommissionDataDAO = RetailerCommissionDataDAO.getInstance();
        commissionsDAO = CommissionsDAO.getInstance();
        retailerCommissionDataDAO.clear();
        commissionsDAO.clear();
        cjService = CJService.getInstance(new CJConsumerMockup());
    }

    private TransactionDTO getCommission(String cuid, String retailerId, String orderId) {
        CommissionsDTO commissions = commissionsDAO.find(cuid);
        assertNotNull(commissions);
        for (TransactionDTO transaction : commissions.getCommissions()){
            if (transaction.getRetailerID().equals(retailerId) && transaction.getOrderID().equals(orderId)){
                return transaction;
            }
        }
        return null;
    }

    @Test
    public void testUpdateAdvertisersCommissions() throws Exception {
        cjService.updateAdvertisersCommissions();
        assertEquals(retailerCommissionDataDAO.find("2962777", AffiliateNetwork.CJ).getBaseCommission(), new Double(1.5));
    }

    @Test
    public void testTransactionLifeCycle() throws Exception {
        cjService.requestNewTransactions();
        TransactionDTO transaction = getCommission("0", "1234567", "123456");
        assertNotNull(transaction);
        assertNotNull(transaction.getPurchaseAmount());
        assertNotNull(transaction.getCashbackAmount());
        assertNotNull(transaction.getTransactionTimestamp());
        assertNotNull(transaction.getUpdateTimestamp());
        assertNotNull(transaction.getAffiliateNetwork());
        assertNotNull(transaction.getStatus());
        assertEquals(transaction.getAffiliateNetwork(), AffiliateNetwork.CJ);
        Assert.assertEquals(transaction.getStatus(), TransactionStatus.CONFIRMED);
    }
}
