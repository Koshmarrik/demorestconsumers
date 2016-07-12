package customer.affeliateconsumer.converter;

import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.DateParser;
import customer.affeliateconsumer.dto.cj.commission.CDStatus;
import customer.affeliateconsumer.dto.cj.commission.CommissionDetailsDTO;
import customer.affeliateconsumer.storage.dao.TransactionDTO;
import customer.affeliateconsumer.storage.dao.TransactionStatus;

import java.util.*;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
public class CommissionDetailsConverter extends TransactionConverter {

    private static final Map<CDStatus, TransactionStatus>statusMapping;

    static{
        Map<CDStatus, TransactionStatus> map = new LinkedHashMap<CDStatus, TransactionStatus>(4);
        map.put(CDStatus.NEW, TransactionStatus.PENDING);
        map.put(CDStatus.EXTENDED, TransactionStatus.PENDING);
        map.put(CDStatus.LOCKED, TransactionStatus.CONFIRMED);
        map.put(CDStatus.CLOSED, TransactionStatus.AVAILABLE);
        statusMapping = Collections.unmodifiableMap(map);
    }

    private final CommissionDetailsDTO commissionDetails;

    public CommissionDetailsConverter(CommissionDetailsDTO commissionDetails){
        this.commissionDetails = commissionDetails;
    }

    public TransactionDTO getTransactionDTOObject(){
        TransactionDTO transaction = new TransactionDTO();
        transaction.setRetailerID(getRetailerID());
        transaction.setOrderID(getOrderID());
        transaction.setRetailerName(getRetailerName());
        transaction.setPurchaseAmount(commissionDetails.getSaleAmount());
        transaction.setCashbackAmount(getCommission());
        transaction.setStatus(getStatus());
        transaction.setTransactionTimestamp(DateParser.parseCJDate(commissionDetails.getEventDate()));
        transaction.setUpdateTimestamp(new Date());
        transaction.setAffiliateNetwork(getAffiliateNetwork());
        return transaction;
    }

    public TransactionStatus getStatus() {
        TransactionStatus status = statusMapping.get(commissionDetails.getActionStatus());
        return status;
    }

    @Override
    public String getRetailerID() {
        return commissionDetails.getCid();
    }

    @Override
    public String getOrderID() {
        return commissionDetails.getOrderId();
    }

    @Override
    public AffiliateNetwork getAffiliateNetwork() {
        return AffiliateNetwork.CJ;
    }

    @Override
    public String getNullableCuid() {
        return commissionDetails.getSid();
    }

    @Override
    public String getCommission() {
        return commissionDetails.getCommissionAmount();
    }

    @Override
    public String getRetailerName() {
        return commissionDetails.getAdvertiserName();
    }
}
