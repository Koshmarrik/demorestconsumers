package customer.affeliateconsumer.consumer;

import customer.affeliateconsumer.dto.linkshare.EventListDTO;
import customer.affeliateconsumer.dto.linkshare.GetMerchByAppStatusResponseDTO;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by roman rasskazov on 29.05.2015.
 */
public interface LinkShareConsumer {

    EventListDTO getEvents(MultivaluedMap<String, String> queryParams, int page);

    GetMerchByAppStatusResponseDTO getLinkLocatorReport();

    String getSignatureOrderReport(String startDate, String endDate);

    String getPaymentHistorySummary(String startDate, String endDate);

    String getAdvertiserPaymentHistory(String paymentId);

    String getPaymentDetails(String invoiceId);
}
