package customer.affeliateconsumer.storage.dao;

import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman rasskazov on 30.05.2015.
 */
public class CommissionsDTO {
    @MongoObjectId
    private String _id;
    private String cuid;
    List<TransactionDTO> commissions = new ArrayList<>();

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public List<TransactionDTO> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<TransactionDTO> commissions) {
        this.commissions = commissions;
    }
}
