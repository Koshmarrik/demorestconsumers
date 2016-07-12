package customer.affeliateconsumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import customer.affeliateconsumer.dto.linkshare.EventListDTO;
import customer.affeliateconsumer.dto.linkshare.GetMerchByAppStatusResponseDTO;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by roman rasskazov on 29.05.2015.
 */
public class LinkShareConsumerMockup implements LinkShareConsumer {

    private final ClassLoader classLoader = getClass().getClassLoader();

    @Override
    public GetMerchByAppStatusResponseDTO getLinkLocatorReport() {
        try {
            JAXBContext context = JAXBContext.newInstance(GetMerchByAppStatusResponseDTO.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            File xml = new File(classLoader.getResource("LinkShare LinkLocator API.xml").toURI());
            return (GetMerchByAppStatusResponseDTO) unmarshaller.unmarshal(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EventListDTO getEvents(MultivaluedMap<String, String> queryParams, int page) {
        try {
            File xml = new File(classLoader.getResource("LinkShare Event Json.json").toURI());
            return new ObjectMapper().readValue(xml, EventListDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getSignatureOrderReport(String startDate, String endDate) {
        return "Member ID,Merchant ID,Merchant Name,Order ID,Transaction Date,Transaction Time," +
                "SKU Number,Sales,Quantity,Commissions,Process Date,Process Time\n\n" +
                "adc99999999cda,1111,AdvertiserName,333333333,8/24/2011,8:58,PROD32,7.99,1,0.3995,8/26/2012,21:05";
//        return "Member ID,Merchant ID,Merchant Name,Order ID,Transaction Date,Transaction Time,SKU Number,Sales,Quantity,Commissions,Process Date,Process Time,\n" +
//        "<none>,38501,Shoes.com,X3332887,05/29/2015,01:58,654 00063 02 120 LM,14.99,1,0.8994,05/30/2015,18:51,";
    }

    @Override
    public String getPaymentHistorySummary(String startDate, String endDate) {
        return "Payment ID,Date,Payment Type,Check Number,Currency Code,Total Commission Amount Paid,Payment Status" +
                "\n\n" + "883463,4/19/2012,Direct Deposit,NA,USD,568.24,Issued";
    }

    @Override
    public String getAdvertiserPaymentHistory(String paymentId) {
        return "Advertiser ID,Advertiser,Invoice Number,Transaction Commissions,Bonus Amount, CPM & CPC Commissions," +
                "Cancelled Commissions,Previously Held Commissions,Payment Amount,Advertiser Payment Date\n\n" +
                "34290,Auto Parts Warehouse,1142851,90.39,0,0,0,0,90.39,4/25/2012";
    }

    @Override
    public String getPaymentDetails(String invoiceId) {
        return "Date,Time,Advertiser ID,Advertiser,Order ID,SKU #,Product Name,Items,Sales,Baseline Commission," +
                "Adjusted Commission,Actual Commission,Reason,Advertiser Payment Memo,Advertiser Payment Date\n\n" +
                "2/2/2012,18:36:00,1111,Auto Parts Warehouse,333333333,WRE13317323,Antenna,1,14.42,5.5179,0,4.5179," +
                "UNKNOWN,N/A, 4/25/2012";
    }

}
