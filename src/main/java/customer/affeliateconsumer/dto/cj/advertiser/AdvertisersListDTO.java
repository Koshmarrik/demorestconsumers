package customer.affeliateconsumer.dto.cj.advertiser;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
@XmlRootElement(name = "cj-api")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdvertisersListDTO implements Serializable {

    @XmlElement
    private AdvertisersListWrapper advertisers;

    public AdvertisersListWrapper getAdvertisers() {
        return advertisers;
    }

    public void setAdvertisers(AdvertisersListWrapper advertisers) {
        this.advertisers = advertisers;
    }
}
