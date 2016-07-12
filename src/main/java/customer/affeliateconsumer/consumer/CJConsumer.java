package customer.affeliateconsumer.consumer;

import customer.affeliateconsumer.dto.cj.advertiser.AdvertisersListDTO;
import customer.affeliateconsumer.dto.cj.commission.CommissionsListDTO;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
public interface CJConsumer {
    public AdvertisersListDTO requestJoinedAdvertisers(int page);
    public CommissionsListDTO requestCommissionDetail(String startDate, String endDate);
}
