package customer.affeliateconsumer.dto.cj.advertiser;

import javax.xml.bind.annotation.*;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionDTO {
    @XmlElement
    private CommissionDTO commission;

    @XmlElement
    private String type;

    public CommissionDTO getCommission() {
        return commission;
    }

    public void setCommission(CommissionDTO commission) {
        this.commission = commission;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
