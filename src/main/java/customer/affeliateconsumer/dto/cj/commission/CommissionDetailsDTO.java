package customer.affeliateconsumer.dto.cj.commission;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
@XmlRootElement(name = "commission")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionDetailsDTO {

    @XmlElement(name = "action-status")
    private CDStatus actionStatus;

    @XmlElement(name = "event-date")
    private String eventDate;

    @XmlElement(name = "order-id")
    private String orderId;

    @XmlElement(name = "cid")
    private String cid;

    @XmlElement(name = "advertiser-name")
    private String advertiserName;

    @XmlElement(name = "commission-amount")
    private String commissionAmount;

    @XmlElement(name = "sid")
    private String sid;

    @XmlElement(name = "sale-amount")
    private String saleAmount;

    public CDStatus getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(CDStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }
}
