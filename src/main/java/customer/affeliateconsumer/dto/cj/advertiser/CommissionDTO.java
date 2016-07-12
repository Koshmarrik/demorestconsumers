package customer.affeliateconsumer.dto.cj.advertiser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionDTO {
    @XmlElement(name = "default")
    private String defaultCommission;

    @XmlElement
    private List<String> itemlist;

    public String getDefaultCommission() {
        return defaultCommission;
    }

    public void setDefaultCommission(String defaultCommission) {
        this.defaultCommission = defaultCommission;
    }

    public List<String> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<String> itemlist) {
        this.itemlist = itemlist;
    }
}
