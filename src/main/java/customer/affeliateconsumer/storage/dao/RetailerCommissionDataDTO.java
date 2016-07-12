package customer.affeliateconsumer.storage.dao;

import customer.affeliateconsumer.AffiliateNetwork;
import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
public class RetailerCommissionDataDTO {
    private Object commission;

    private RetailerCommissionDataDTO() {
    }

    public RetailerCommissionDataDTO(String retailerID, Double baseCommission, AffiliateNetwork network) {
        this.retailerID = retailerID;
        this.baseCommission = baseCommission;
        affiliateNetwork = network;
    }

    @MongoObjectId
    private String _id;
    private String retailerID;
    private AffiliateNetwork affiliateNetwork;
    private Double baseCommission;

    public void setCommission(Double baseCommission) {
        this.baseCommission = baseCommission;
    }

    public Double getBaseCommission() {
        return baseCommission;
    }

    public AffiliateNetwork getAffiliateNetwork() {
        return affiliateNetwork;
    }

    public void setAffiliateNetwork(AffiliateNetwork affiliateNetwork) {
        this.affiliateNetwork = affiliateNetwork;
    }
}
