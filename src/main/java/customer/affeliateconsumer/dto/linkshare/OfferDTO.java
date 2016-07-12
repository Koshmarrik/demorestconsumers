package customer.affeliateconsumer.dto.linkshare;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by roman rasskazov on 27.05.2015.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferDTO {

    @XmlElement(namespace = "http://endpoint.linkservice.linkshare.com/")
    private String commissionTerms;

    public String getCommissionTerms() {
        return commissionTerms;
    }

    public void setCommissionTerms(String commissionTerms) {
        this.commissionTerms = commissionTerms;
    }
}
