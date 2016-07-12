package customer.affeliateconsumer.storage.dao;

import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by roman rasskazov on 30.05.2015.
 */
public class ParametersDTO {
    @MongoObjectId
    private String _id;
    String parameter;
    String value;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
