package customer.affeliateconsumer.converter;

import customer.affeliateconsumer.DateParser;
import customer.affeliateconsumer.storage.dao.TransactionDTO;
import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.dto.linkshare.EventDTO;
import customer.affeliateconsumer.storage.dao.TransactionStatus;

import java.util.Date;

/**
 * Created by roman rasskazov on 30.05.2015.
 */
public class EventConverter extends TransactionConverter {

    private EventDTO event;

    public EventConverter(EventDTO event) {
        this.event = event;
    }

    @Override
    public String getRetailerID() {
        return event.getAdvertiser_id();
    }

    @Override
    public String getOrderID() {
        return event.getOrderId();
    }

    @Override
    public String getNullableCuid() {
        return event.getU1();
    }

    @Override
    public AffiliateNetwork getAffiliateNetwork() {
        return AffiliateNetwork.Linkshare;
    }

    @Override
    public TransactionStatus getStatus() {
        return TransactionStatus.PENDING;
    }

    @Override
    public String getCommission() {
        return event.getCommissions();
    }

    @Override
    public TransactionDTO getTransactionDTOObject(){
        TransactionDTO transaction = new TransactionDTO();
        transaction.setRetailerID(getRetailerID());
        transaction.setOrderID(getOrderID());
        transaction.setRetailerName(getRetailerName());
        transaction.setPurchaseAmount(event.getSaleAmount());
        transaction.setCashbackAmount(getCommission());
        transaction.setStatus(getStatus());
        transaction.setTransactionTimestamp(DateParser.parseLinkShareEventDate(event.getTransactionDate()));
        transaction.setUpdateTimestamp(new Date());
        transaction.setAffiliateNetwork(getAffiliateNetwork());
        return transaction;
    }

    @Override
    public String getRetailerName() {
        return null;
    }
}
