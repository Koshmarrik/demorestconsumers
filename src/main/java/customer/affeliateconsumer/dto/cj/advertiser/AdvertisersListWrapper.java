package customer.affeliateconsumer.dto.cj.advertiser;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;

/**
 * Created by roman rasskazov on 02.06.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AdvertisersListWrapper {
    @XmlElement(name = "advertiser")
    private ArrayList<AdvertiserDTO> advertisers;

    @XmlAttribute(name = "total-matched")
    private Integer total;

    @XmlAttribute(name = "records-returned")
    private Integer returned;

    public ArrayList<AdvertiserDTO> getAdvertisers() {
        return advertisers;
    }

    public void setAdvertisers(ArrayList<AdvertiserDTO> advertisers) {
        this.advertisers = advertisers;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }
}
