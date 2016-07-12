package customer.affeliateconsumer.dto.linkshare;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
@XmlRootElement(namespace = "http://endpoint.linkservice.linkshare.com/", name = "getMerchByIDResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetMerchByIDResponseDTO {

    @XmlElement(namespace = "http://endpoint.linkservice.linkshare.com/", name="return")
    private ReturnDTO returnDTO;

    public ReturnDTO getReturnDTO() {
        return returnDTO;
    }

    public void setReturnDTO(ReturnDTO returnDTO) {
        this.returnDTO = returnDTO;
    }
}
