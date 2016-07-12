package customer.affeliateconsumer.converter;

import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.DateParser;
import customer.affeliateconsumer.storage.dao.TransactionDTO;
import customer.affeliateconsumer.storage.dao.TransactionStatus;
import org.apache.commons.csv.CSVRecord;

import java.util.Date;

/**
 * Created by roman rasskazov on 01.06.2015.
 */
public class SignatureConverter extends TransactionConverter {

    private static final String FIELD_NAME_RETAILER_ID = "Merchant ID";
    private static final String FIELD_NAME_MERCHANT_NAME = "Merchant Name";
    private static final String FIELD_NAME_COMMISSION = "Commissions";
    private static final String FIELD_NAME_ORDER_ID = "Order ID";
    private static final String FIELD_NAME_MEMBER_ID = "Member ID";
    private static final String FIELD_NAME_SALES = "Sales";
    private static final String FIELD_NAME_TRANSACTION_TIME = "Transaction Time";
    private static final String FIELD_NAME_TRANSACTION_DATE = "Transaction Date";

    private final CSVRecord csvRecord;

    public SignatureConverter(CSVRecord csvRecord) {
        this.csvRecord = csvRecord;
    }

    @Override
    public String getRetailerID() {
        return csvRecord.get(FIELD_NAME_RETAILER_ID);
    }

    @Override
    public String getOrderID() {
        return csvRecord.get(FIELD_NAME_ORDER_ID);
    }

    @Override
    public AffiliateNetwork getAffiliateNetwork() {
        return AffiliateNetwork.Linkshare;
    }

    @Override
    public TransactionStatus getStatus() {
        double commissionDouble = Double.parseDouble(getCommission());
        return commissionDouble >= 0 ? TransactionStatus.CONFIRMED : TransactionStatus.CANCELLED;
    }

    @Override
    public String getNullableCuid() {
        return csvRecord.get(FIELD_NAME_MEMBER_ID);
    }

    @Override
    public String getCommission() {
        return csvRecord.get(FIELD_NAME_COMMISSION);
    }

    @Override
    public TransactionDTO getTransactionDTOObject() {
        TransactionDTO transaction = new TransactionDTO();
        transaction.setRetailerID(getRetailerID());
        transaction.setOrderID(getOrderID());
        transaction.setRetailerName(getRetailerName());
        transaction.setPurchaseAmount(csvRecord.get(FIELD_NAME_SALES));
        transaction.setCashbackAmount(getCommission());
        transaction.setStatus(getStatus());
        transaction.setTransactionTimestamp(DateParser.parseLinkShareSignatureDate(csvRecord.get
                (FIELD_NAME_TRANSACTION_DATE), csvRecord.get(FIELD_NAME_TRANSACTION_TIME)));
        transaction.setUpdateTimestamp(new Date());
        transaction.setAffiliateNetwork(getAffiliateNetwork());
        return transaction;
    }

    @Override
    public String getRetailerName() {
        return csvRecord.get(FIELD_NAME_MERCHANT_NAME);
    }
}
