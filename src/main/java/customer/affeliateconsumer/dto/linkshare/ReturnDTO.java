package customer.affeliateconsumer.dto.linkshare;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
@XmlRootElement(name = "return")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnDTO {

    @XmlElement(namespace = "http://endpoint.linkservice.linkshare.com/")
    private String mid;

    @XmlElement(namespace = "http://endpoint.linkservice.linkshare.com/")
    private OfferDTO offer;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public OfferDTO getOffer() {
        return offer;
    }

    public void setOffer(OfferDTO offer) {
        this.offer = offer;
    }
}
