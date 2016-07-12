package customer.affeliateconsumer.storage.dao;

import customer.affeliateconsumer.AffiliateNetwork;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Date;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
public class TransactionDTO {

    @MongoObjectId
    private String _id;
    private String retailerID;
    private String orderID;
    private String retailerName;
    private String purchaseAmount;
    private String cashbackAmount;
    private TransactionStatus status;
    private Date transactionTimestamp;
    private Date updateTimestamp;

    private AffiliateNetwork affiliateNetwork;

    public String getRetailerID() {
        return retailerID;
    }

    public void setRetailerID(String retailerID) {
        this.retailerID = retailerID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(String cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Date getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(Date transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public AffiliateNetwork getAffiliateNetwork() {
        return affiliateNetwork;
    }

    public void setAffiliateNetwork(AffiliateNetwork affiliateNetwork) {
        this.affiliateNetwork = affiliateNetwork;
    }
}
