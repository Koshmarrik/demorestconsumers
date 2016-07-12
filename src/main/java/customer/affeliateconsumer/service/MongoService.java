package customer.affeliateconsumer.service;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import customer.affeliateconsumer.ParameterName;
import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.ExceptionLogger;
import customer.affeliateconsumer.converter.TransactionConverter;
import customer.affeliateconsumer.storage.dao.*;
import org.apache.log4j.Logger;
import org.jongo.Jongo;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
public class MongoService {

    private final Logger log = Logger.getLogger(getClass());

    private RetailerCommissionDataDAO retailerCommissionDataDAO = RetailerCommissionDataDAO.getInstance();
    private CommissionsDAO commissionsDAO = CommissionsDAO.getInstance();
    private ParametersDAO parametersDAO = ParametersDAO.getInstance();

    private static final String DB_NAME = "affiliate_networks";
    private static final String TRANSACTION_NOT_FOUND = "MongoService updateTransactionStatus: cannot find " +
            "transaction to update status, retailerId = {0}, orderId = {1}, network = {2}, status = {3}";

    private static MongoService instance;

    public static MongoService getInstance() {
        if (instance == null){
            instance = new MongoService();
        }
        return instance;
    }

    private static Jongo jongo;

    public static Jongo getJongo(){
        if (jongo == null){
            DB db = new MongoClient().getDB(DB_NAME);
            jongo = new Jongo(db);
        }
        return jongo;
    }

    synchronized public void updateAdvertiserCommission(String advertiserId, Double commissionValue, AffiliateNetwork network) {
        RetailerCommissionDataDTO commission = retailerCommissionDataDAO.find(advertiserId, network);
        if (commission == null){
            commission = new RetailerCommissionDataDTO(advertiserId, commissionValue, network);
        } else {
            commission.setCommission(commissionValue);
        }
        retailerCommissionDataDAO.save(commission);
    }

    synchronized public boolean updateTransaction(TransactionConverter newTransaction) {
        CommissionsDTO commissions = commissionsDAO.find(newTransaction.getRetailerID(), newTransaction.getOrderID(),
                newTransaction.getAffiliateNetwork());
        Boolean result = true;
        if (commissions == null) {
            commissions = commissionsDAO.find(newTransaction.getCuid());
            if (commissions == null) {
                commissions = new CommissionsDTO();
                commissions.setCuid(newTransaction.getCuid());
            }
            commissions.getCommissions().add(newTransaction.getTransactionDTOObject());
        } else {
            result = updateTransaction(commissions, newTransaction.getRetailerID(), newTransaction.getRetailerName(), newTransaction.getOrderID(),
                    newTransaction.getAffiliateNetwork(), newTransaction.getStatus(), newTransaction.getCommission());
        }
        commissionsDAO.save(commissions);
        return result;
    }

    /**
     * Looks for transaction in commissons list using retailerId, orderId, network, then updates status, commission
     * @return if commission was updated, null if not found
     */
    private boolean updateTransaction(CommissionsDTO commissions, String retailerId, String retailerName, String orderId, AffiliateNetwork network, TransactionStatus status, String commission) {
        for (TransactionDTO transaction : commissions.getCommissions()) {
            if (transaction.getOrderID().equals(orderId) && transaction.getRetailerID()
                    .equals(retailerId) && transaction.getAffiliateNetwork() == network) {
                if (transaction.getStatus() == status) {
                    return false;
                }
                transaction.setStatus(status);
                transaction.setUpdateTimestamp(new Date());
                if (commission != null){
                    transaction.setCashbackAmount(commission);
                }
                if (retailerName != null) {
                    transaction.setRetailerName(retailerName);
                }
                return true;
            }
        }
        ExceptionLogger.logAndSendError(log, null, MessageFormat.format(TRANSACTION_NOT_FOUND, retailerId, orderId,
                network, status));
        return false;
    }

    /**
     * Looks for transaction in commissions using retailerId, orderId, network, updates status, commission
     * @param retailerId
     * @param retailerName used only for Signature to fill retailerName which was not presented in event API response
     * @param orderId
     * @param network
     * @param status
     * @param commission
     * @return if commission was updated
     */
    synchronized public boolean updateTransactionStatus(String retailerId, String retailerName, String orderId, AffiliateNetwork network, TransactionStatus status, String commission) {
        CommissionsDTO commissions = commissionsDAO.find(retailerId, orderId, network);
        if (commissions != null){
            boolean result = updateTransaction(commissions, retailerId, retailerName, orderId, network, status, commission);
            if (result) {
                commissionsDAO.save(commissions);
            }
            return result;
        }
        ExceptionLogger.logAndSendError(log, null, MessageFormat.format(TRANSACTION_NOT_FOUND, retailerId, orderId,
                network, status));
        return false;
    }

    synchronized private static ParametersDTO getParameter(String parameterName) {
        return getInstance().parametersDAO.getParameter(parameterName);
    }

    public static String getParameter(ParameterName parameterName) {
        return getParameter(parameterName.toString()).getValue();
    }

    public static double getDoubleParameter(String parameterName) {
        return Double.parseDouble(getParameter(parameterName).getValue());
    }

    public static int getIntParameter(ParameterName parameterName) {
        return Integer.parseInt(getParameter(parameterName.toString()).getValue());
    }

    public static void setParameter(ParameterName name, String value) {
        ParametersDTO parametersDTO = getParameter(name.toString());
        if (parametersDTO == null){
            parametersDTO = new ParametersDTO();
            parametersDTO.setParameter(name.toString());
        }
        parametersDTO.setValue(value);
        getInstance().parametersDAO.save(parametersDTO);
    }

}
