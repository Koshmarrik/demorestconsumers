package customer.affeliateconsumer.consumer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import customer.affeliateconsumer.dto.cj.advertiser.AdvertisersListDTO;
import customer.affeliateconsumer.dto.cj.commission.CommissionsListDTO;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 * Created by roman rasskazov on 24.05.2015.
 */
public class CJConsumerImpl implements CJConsumer{

    private static final String ADVERTISER_LOOKUP_URI = "https://advertiser-lookup.api.cj.com";
    private static final String API_VERSION = "v3";
    private static final String ADVERTISER_LOOKUP_PATH = "advertiser-lookup";
    private static final String PARAM_ADVERTISER_IDS = "advertiser-ids";
    private static final String ADVERTISER_IDS_PARAM_JOINED = "joined";
    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String COMMISSION_DETAIL_URI = "https://commission-detail.api.cj.com";
    private static final String COMMISSION_DETAIL_PATH = "commissions";
    private static final String PARAM_DATE_TYPE = "date-type";
    private static final String VALUE_POSTING = "posting";
    private static final String PARAM_START_DATE = "start-date";
    private static final String PARAM_END_DATE = "end-date";
    private static final String PARAM_RECORDS_PER_PAGE = "records-per-page";
    public static final Integer PARAM_RECORDS_PER_PAGE_VALUE = 100;
    private static final String PARAM_PAGE_NUMBER = "page-number";
    private Logger log = Logger.getLogger(getClass());

    private static final String AUTHORISATION_KEY = "0088ca8fa52ca7d7b6dd16418bf84d7e60a13b6696a2f1d94cc69ae75b6a2623d418858e5c7670d1034a3f5c442c9545c42823cd69c91bd6c7a19c1f4c77d54e19/04a45b1b8fd86249a2570f0260fac03f27398cd1afd5e539e5c37bf66e33bd2378b9c61f3c6a81dfa31d479f040e54a6dccb8c5a4acb5419fc5df2da4e696f01";

    private Client client;

    private Client getClient(){
        if (client == null){
            ClientConfig config = new DefaultClientConfig();
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            client = Client.create(config);
        }
        return client;
    }

    @Override
    /**
     *
     */
    public AdvertisersListDTO requestJoinedAdvertisers(int page){
        WebResource service = getClient().resource(UriBuilder.fromUri(ADVERTISER_LOOKUP_URI).build());
        AdvertisersListDTO advertisersList = service.path(API_VERSION)
                .path(ADVERTISER_LOOKUP_PATH)
                .queryParam(PARAM_ADVERTISER_IDS, ADVERTISER_IDS_PARAM_JOINED)
                .queryParam(PARAM_RECORDS_PER_PAGE, PARAM_RECORDS_PER_PAGE_VALUE.toString())
                .queryParam(PARAM_PAGE_NUMBER, Integer.toString(page+1)).header(AUTHORIZATION_HEADER, AUTHORISATION_KEY)
                .accept(MediaType.APPLICATION_XML_TYPE).get(AdvertisersListDTO.class);
        if (advertisersList.getAdvertisers().getAdvertisers().isEmpty()){
            log.warn("CJ update commissions task: not advertisers found");
        }
        return advertisersList;
    }

    @Override
    /**
     *
     */
    public CommissionsListDTO requestCommissionDetail(String startDate, String endDate) {
        WebResource service = getClient().resource(UriBuilder.fromUri(COMMISSION_DETAIL_URI).build());
        CommissionsListDTO commissionsListDTO = service.path(API_VERSION)
                .path(COMMISSION_DETAIL_PATH).queryParam(PARAM_DATE_TYPE, VALUE_POSTING).queryParam(PARAM_START_DATE, startDate).queryParam(PARAM_END_DATE, endDate).header(AUTHORIZATION_HEADER, AUTHORISATION_KEY).accept(MediaType.APPLICATION_XML_TYPE).get(CommissionsListDTO.class);
        return commissionsListDTO;
    }
}
