package customer.affeliateconsumer.dto.linkshare;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
@XmlRootElement(namespace = "http://endpoint.linkservice.linkshare.com/", name = "getMerchByAppStatusResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetMerchByAppStatusResponseDTO {

    @XmlElement(namespace = "http://endpoint.linkservice.linkshare.com/", name = "return")
    private List<ReturnDTO> returns;

    public List<ReturnDTO> getReturns() {
        return returns;
    }

    public void setReturns(List<ReturnDTO> returns) {
        this.returns = returns;
    }
}
