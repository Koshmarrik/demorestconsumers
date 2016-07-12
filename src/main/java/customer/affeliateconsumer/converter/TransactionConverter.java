package customer.affeliateconsumer.converter;

import customer.affeliateconsumer.storage.dao.TransactionDTO;
import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.storage.dao.TransactionStatus;

/**
 * Created by roman rasskazov on 30.05.2015.
 */
public abstract class TransactionConverter {

    private static final String DEFAULT_CUID = "DEFAULT";

    abstract public String getRetailerID();

    abstract public String getOrderID();

    abstract public AffiliateNetwork getAffiliateNetwork();

    abstract public TransactionDTO getTransactionDTOObject();

    abstract public TransactionStatus getStatus();

    abstract protected String getNullableCuid();

    abstract public String getCommission();

    abstract public String getRetailerName();

    public String getCuid(){
        String cuid = getNullableCuid();
        return cuid == null ? DEFAULT_CUID : cuid;
    }
}
