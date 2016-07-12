package customer.affeliateconsumer.dto.cj.advertiser;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdvertiserDTO {

    @XmlElement(name = "advertiser-id")
    private String advertiserId;

    @XmlElementWrapper
    @XmlElement(name = "action")
    private List<ActionDTO> actions;

    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

    public List<ActionDTO> getActions() {
        return actions;
    }

    public void setActions(List<ActionDTO> actions) {
        this.actions = actions;
    }
}
