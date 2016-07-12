package customer.affeliateconsumer.consumer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import customer.affeliateconsumer.ExceptionLogger;
import customer.affeliateconsumer.ParameterName;
import customer.affeliateconsumer.service.MongoService;
import customer.affeliateconsumer.dto.linkshare.AccessTokenDTO;
import customer.affeliateconsumer.dto.linkshare.EventListDTO;
import customer.affeliateconsumer.dto.linkshare.GetMerchByAppStatusResponseDTO;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

/**
 * Created by roman rasskazov on 26.05.2015.
 */
public class LinkShareConsumerImpl implements LinkShareConsumer {

    private final Logger log = Logger.getLogger(getClass());

    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_ACCEPT_VALUE_TEXT_JSON = "text/json";

    private static final String PAYMENT_HISTORY_REPORT_ID = "1";
    private static final String ADVERTISER_PAYMENT_HISTORY_REPORT_ID = "2";
    private static final String PAYMENT_DETAILS_REPORT_ID = "3";
    private static final String SIGNATURE_ORDER_REPORT_ID = "12";

    private static final String LINK_SHARE_API_URI = "https://api.rakutenmarketing.com";
    private static final String PATH_LINK_LOCATOR = "linklocator";
    private static final String PATH_API_VERSION = "1.0";
    private static final String PATH_GET_MERCH_BY_APP_STATUS = "getMerchByAppStatus";
    private static final String PATH_ADVANCED_REPORTS = "advancedreports";
    private static final String PATH_TOKEN = "token";
    private static final String PATH_APPROVED = "approved";
    private static final String PATH_EVENTS = "events";
    private static final String PATH_TRANSACTIONS = "transactions";

    private static final String TOKEN_REQUEST_AUTHORIZATON = "Basic " +
            "VXlBUTk5dGdJU013bjdmZUNMc1ltWHZ4Y2pzYTpuZzBmaWlLekFXeUlaeldsNzBoZk1yTkRNWVFh";
    private static final String ACCESS_TOCKEN_PREFIX = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String PARAM_GRANT_TYPE = "grant_type";
    private static final String PARAM_GRANT_TYPE_VALUE = "password";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_SCOPE = "scope";
    private static final String PARAM_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private static final String PARAM_REFRESH_TOKEN = "refresh_token";
    private static final String PARAM_SCOPE_PRODUCTION = "Production";
    private static final String PARAM_LIMIT = "limit";
    public static final int PARAM_LIMIT_VALUE = 1000;
    private static final String PARAM_BDATE = "bdate";
    private static final String PARAM_EDATE = "edate";
    private static final String PARAM_SECURITY_TOKEN = "token";
    private static final String PARAM_REPORTID = "reportid";
    private static final String PARAM_PAYID = "payid";
    private static final String PARAM_INVOICE_ID = "invoiceid";
    private static final String PARAM_PAGE = "page";


    private Client client;

    private AccessTokenDTO accessToken;

    private Client getClient(){
        if (client == null){
            ClientConfig config = new DefaultClientConfig();
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            config.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, MongoService.getIntParameter
                    (ParameterName.REST_CONNECTION_TIMEOUT));
            client = Client.create(config);
        }
        return client;
    }

    private long authenticationExpire = 0;

    private void authenticationRequest(MultivaluedMap<String, String> payload){
        WebResource resource = getClient().resource(UriBuilder.fromUri(LINK_SHARE_API_URI).build());
        ClientResponse response = resource.path(PATH_TOKEN).header(HEADER_AUTHORIZATION, TOKEN_REQUEST_AUTHORIZATON).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, payload);
        accessToken = response.getEntity(AccessTokenDTO.class);
        authenticationExpire = System.currentTimeMillis() + accessToken.getExpiresIn() * 1000;
        log.debug("LinkShare authentication: accessToken received. Expires in " + accessToken.getExpiresIn());
    }

    private void initialAuthentication(){
        MultivaluedMap<String, String> payload = new MultivaluedMapImpl();
        payload.add(PARAM_GRANT_TYPE, PARAM_GRANT_TYPE_VALUE);
        payload.add(PARAM_USERNAME, MongoService.getParameter(ParameterName.LINKSHARE_USERNAME));
        payload.add(PARAM_PASSWORD, MongoService.getParameter(ParameterName.LINKSHARE_PASSWORD));
        payload.add(PARAM_SCOPE, MongoService.getParameter(ParameterName.LINKSHARE_SCOPE));
        authenticationRequest(payload);
    }

    synchronized private void authenticate() {
        log.debug("LinkShare authentication: running");
        if (authenticationExpire == 0) {
            //initial authentication
            initialAuthentication();
            return;
        }
        long remainMinutes = (authenticationExpire - System.currentTimeMillis())/1000/60;
        if (remainMinutes > MongoService.getIntParameter(ParameterName.REQUEST_REAUTHENTICATION_MINUTES)){
            log.debug("LinkShare authentication: no need to authenticate, expires in " + remainMinutes + " minutes");
        } else {
            try {
                MultivaluedMap<String, String> payload = new MultivaluedMapImpl();
                payload.add(PARAM_GRANT_TYPE, PARAM_GRANT_TYPE_REFRESH_TOKEN);
                payload.add(PARAM_REFRESH_TOKEN, accessToken.getRefreshToken());
                payload.add(PARAM_SCOPE, PARAM_SCOPE_PRODUCTION);
                authenticationRequest(payload);
            } catch (Exception e){
                //try initial authentication if reauthentication fails
                ExceptionLogger.logAndSendError(log, e, null);
                initialAuthentication();
            }
        }


    }

    @Override
    public EventListDTO getEvents(MultivaluedMap<String, String> queryParams, int page){
        authenticate();
        WebResource resource = getClient().resource(UriBuilder.fromUri(LINK_SHARE_API_URI).build());
        return resource
                .path(PATH_EVENTS)
                .path(PATH_API_VERSION)
                .path(PATH_TRANSACTIONS)
                .queryParams(queryParams)
                .queryParam(PARAM_LIMIT, Integer.toString(PARAM_LIMIT_VALUE))
                .queryParam(PARAM_PAGE, Integer.toString(page))
                .header(HEADER_AUTHORIZATION, ACCESS_TOCKEN_PREFIX + accessToken.getAccessToken())
                .header(HEADER_ACCEPT, HEADER_ACCEPT_VALUE_TEXT_JSON)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(EventListDTO.class);
    }

    private String getAdvancedReport(String reportId, MultivaluedMap<String, String> queryParams){
        authenticate();
        WebResource service = getClient().resource(UriBuilder.fromUri(LINK_SHARE_API_URI).build());
        String response = service
                .path(PATH_ADVANCED_REPORTS)
                .path(PATH_API_VERSION)
                .queryParams(queryParams)
                .queryParam(PARAM_SECURITY_TOKEN, MongoService.getParameter(ParameterName.LINKSHARE_SECURITY_TOKEN))
                .queryParam(PARAM_REPORTID, reportId)
                .header(HEADER_AUTHORIZATION, ACCESS_TOCKEN_PREFIX + accessToken.getAccessToken())
                .accept(MediaType.TEXT_PLAIN_TYPE).get(String.class);
        return response;
    }

    private String getAdvancedReport(String reportId, String... strings){
        MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
        int i = 0;
        while (i<strings.length){
            queryParams.add(strings[i], strings[i+1]);
            i+=2;
        }
        return getAdvancedReport(reportId, queryParams);
    }

    @Override
    public GetMerchByAppStatusResponseDTO getLinkLocatorReport(){
        authenticate();
        WebResource service = getClient().resource(UriBuilder.fromUri(LINK_SHARE_API_URI).build());
        return service
                .path(PATH_LINK_LOCATOR)
                .path(PATH_API_VERSION)
                .path(PATH_GET_MERCH_BY_APP_STATUS)
                .path(PATH_APPROVED).header(HEADER_AUTHORIZATION, ACCESS_TOCKEN_PREFIX + accessToken.getAccessToken())
                .accept(MediaType.APPLICATION_XML_TYPE).get(GetMerchByAppStatusResponseDTO.class);
    }

    @Override
    public String getSignatureOrderReport(String startDate, String endDate){
        return getAdvancedReport(SIGNATURE_ORDER_REPORT_ID, PARAM_BDATE, startDate, PARAM_EDATE, endDate);
    }

    @Override
    public String getPaymentHistorySummary(String startDate, String endDate) {
        log.debug("LinkShare update Available Transactions: run PaymentHistorySummary");
        String result = getAdvancedReport(PAYMENT_HISTORY_REPORT_ID, PARAM_BDATE, startDate, PARAM_EDATE,
                endDate);
        log.debug("LinkShare update Available Transactions: finished PaymentHistorySummary");
        return result;
    }

    @Override
    public String getAdvertiserPaymentHistory(String paymentId) {
        log.debug("LinkShare update Available Transactions: run AdvertiserPaymentHistory");
        String result = getAdvancedReport(ADVERTISER_PAYMENT_HISTORY_REPORT_ID, PARAM_PAYID, paymentId);
        log.debug("LinkShare update Available Transactions: finished AdvertiserPaymentHistory");
        return result;
    }

    public String getPaymentDetails(String invoiceId) {
        log.debug("LinkShare update Available Transactions: run PaymentDetails");
        String result = getAdvancedReport(PAYMENT_DETAILS_REPORT_ID, PARAM_INVOICE_ID, invoiceId);
        log.debug("LinkShare update Available Transactions: finished PaymentDetails");
        return result;
    }
}
