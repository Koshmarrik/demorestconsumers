package customer.affeliateconsumer;

import customer.affeliateconsumer.storage.dao.*;
import customer.affeliateconsumer.consumer.LinkShareConsumerMockup;
import customer.affeliateconsumer.service.LinkShareService;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by roman rasskazov on 01.06.2015.
 */
public class LinkShareServiceTest {

    private static LinkShareService linkShareService;

    private static RetailerCommissionDataDAO retailerCommissionDataDAO;

    private static CommissionsDAO commissionsDAO;

    @BeforeClass
    public static void eraseCollection(){
        ExceptionLogger.SEND_MAIL = false;
        retailerCommissionDataDAO = RetailerCommissionDataDAO.getInstance();
        commissionsDAO = CommissionsDAO.getInstance();
        retailerCommissionDataDAO.clear();
        commissionsDAO.clear();
        linkShareService = LinkShareService.getInstance(new LinkShareConsumerMockup());
    }

    private TransactionDTO getTransaction(String cuid, String retailerId, String orderId) {
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
        linkShareService.updateAdvertisersCommissions();
        assertEquals(retailerCommissionDataDAO.find("38501", AffiliateNetwork.Linkshare).getBaseCommission(), new Double(6));
    }

    @Test
    public void testTransactionLifeCycle() throws Exception {
        linkShareService.requestNewTransactions();
        TransactionDTO transaction = getTransaction("adc99999999cda", "1111", "333333333");
        assertNotNull(transaction);
        assertNotNull(transaction.getPurchaseAmount());
        assertNotNull(transaction.getCashbackAmount());
        assertNotNull(transaction.getTransactionTimestamp());
        assertNotNull(transaction.getUpdateTimestamp());
        assertNotNull(transaction.getAffiliateNetwork());
        assertNotNull(transaction.getStatus());
        assertEquals(transaction.getAffiliateNetwork(), AffiliateNetwork.Linkshare);
        assertEquals(transaction.getStatus(), TransactionStatus.PENDING);
        linkShareService.requestConfirmedTransactions();

        transaction = getTransaction("adc99999999cda", "1111", "333333333");
        assertNotNull(transaction);
        assertNotNull(transaction.getStatus());
        assertNotNull(transaction.getRetailerName());
        assertEquals(transaction.getStatus(), TransactionStatus.CONFIRMED);
        linkShareService.requestAvailableTransactions();

        transaction = getTransaction("adc99999999cda", "1111", "333333333");
        assertNotNull(transaction);
        assertNotNull(transaction.getStatus());
        assertEquals(transaction.getStatus(), TransactionStatus.AVAILABLE);
    }

    //Ignore until Signature will gather directly without Event request
    @Ignore
    @Test
    public void testSignatureWithoutEvent() throws Exception {
        linkShareService.requestConfirmedTransactions();
        TransactionDTO transaction = getTransaction("adc99999999cda", "1234567", "123456");
        assertNotNull(transaction);
    }

}