package customer.affeliateconsumer.storage.dao;

import customer.affeliateconsumer.service.MongoService;
import customer.affeliateconsumer.AffiliateNetwork;
import org.jongo.MongoCollection;

import java.text.MessageFormat;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
public class RetailerCommissionDataDAO {

    private MongoCollection retailerCommissionData;

    private RetailerCommissionDataDAO(){
        retailerCommissionData = MongoService.getJongo().getCollection("retailerCommissionData");
    }

    private static RetailerCommissionDataDAO instance;

    public static final String ADVERTISER_SELECTOR = "'{retailerID: '''{0}''', affiliateNetwork: '''{1}'''}";

    public static RetailerCommissionDataDAO getInstance() {
        if (instance == null){
            instance = new RetailerCommissionDataDAO();
        }
        return instance;
    }

    public RetailerCommissionDataDTO find(String advertiserId, AffiliateNetwork network){
        String selector = MessageFormat.format(ADVERTISER_SELECTOR, advertiserId, network);
        return retailerCommissionData.findOne(selector).as(RetailerCommissionDataDTO.class);
    }

    public void save(RetailerCommissionDataDTO retailerCommission){
        retailerCommissionData.save(retailerCommission);
    }

    public void clear() {
        retailerCommissionData.remove();
    }
}
