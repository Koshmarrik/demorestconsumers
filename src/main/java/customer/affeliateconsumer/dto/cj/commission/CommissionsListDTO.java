package customer.affeliateconsumer.dto.cj.commission;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
@XmlRootElement(name = "cj-api")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommissionsListDTO {
    @XmlElementWrapper(name = "commissions")
    @XmlElement(name = "commission")
    private ArrayList<CommissionDetailsDTO> commissions;

    public ArrayList<CommissionDetailsDTO> getCommissions() {
        return commissions;
    }

    public void setCommissions(ArrayList<CommissionDetailsDTO> commissions) {
        this.commissions = commissions;
    }
}
