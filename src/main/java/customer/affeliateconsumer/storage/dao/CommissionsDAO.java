package customer.affeliateconsumer.storage.dao;

import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.service.MongoService;
import org.jongo.MongoCollection;

import java.text.MessageFormat;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
public class CommissionsDAO {
    private static final String COLLECTION_NAME = "commissions";
    private static CommissionsDAO instance;

    public static CommissionsDAO getInstance() {
        if (instance == null) {
            instance = new CommissionsDAO();
        }
        return instance;
    }

    private MongoCollection commissionsCollection;

    private CommissionsDAO() {
        commissionsCollection = MongoService.getJongo().getCollection(COLLECTION_NAME);
    }

    private static final String TRANSACTION_SELECTOR = "'{'''commissions.retailerID''': '''{0}''', '''commissions.orderID''': '''{1}''', '''commissions.affiliateNetwork''': '''{2}'''}";

    private static final String COMMISSIONS_SELECTOR = "'{cuid: '''{0}'''}";

    public CommissionsDTO find(String retailerId, String orderId, AffiliateNetwork affiliateNetwork) {
        String selector = MessageFormat.format(TRANSACTION_SELECTOR, retailerId, orderId, affiliateNetwork);
        return commissionsCollection.findOne(selector).as(CommissionsDTO.class);
    }

    public void save(CommissionsDTO commissions){
        commissionsCollection.save(commissions);
    }

    public CommissionsDTO find(String cuid) {
        String selector = MessageFormat.format(COMMISSIONS_SELECTOR, cuid);
        return commissionsCollection.findOne(selector).as(CommissionsDTO.class);
    }

    public void clear() {
        commissionsCollection.remove();
    }
}
