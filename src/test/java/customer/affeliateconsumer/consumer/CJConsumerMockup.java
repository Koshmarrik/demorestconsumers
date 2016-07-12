package customer.affeliateconsumer.consumer;

import customer.affeliateconsumer.dto.cj.advertiser.AdvertisersListDTO;
import customer.affeliateconsumer.dto.cj.commission.CommissionsListDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
public class CJConsumerMockup implements CJConsumer {

    private final ClassLoader classLoader = getClass().getClassLoader();

    @Override
    public AdvertisersListDTO requestJoinedAdvertisers(int page) {
        try {
            JAXBContext context = JAXBContext.newInstance(AdvertisersListDTO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            File xml = new File(classLoader.getResource("CJ Lookup Advertisers Response XML.xml").toURI());
            return (AdvertisersListDTO) unmarshaller.unmarshal(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CommissionsListDTO requestCommissionDetail(String startDate, String endDate) {
        try {
            JAXBContext context = JAXBContext.newInstance(CommissionsListDTO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            File xml = new File(classLoader.getResource("CJ Commission Detail Response.xml").toURI());
            return (CommissionsListDTO) unmarshaller.unmarshal(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
