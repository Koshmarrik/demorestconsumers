package customer.affeliateconsumer.storage.dao;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by roman rasskazov on 29.05.2015.
 */
public enum TransactionStatus {
    PENDING("pending"),
    CONFIRMED("confirmed"),
    AVAILABLE("available"),
    CANCELLED("cancelled");

    private final String name;

    private TransactionStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
