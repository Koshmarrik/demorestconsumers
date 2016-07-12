package customer.affeliateconsumer.service;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import customer.affeliateconsumer.DateParser;
import customer.affeliateconsumer.ParameterName;
import customer.affeliateconsumer.consumer.LinkShareConsumer;
import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.ExceptionLogger;
import customer.affeliateconsumer.converter.EventConverter;
import customer.affeliateconsumer.converter.SignatureConverter;
import customer.affeliateconsumer.dto.linkshare.ReturnDTO;
import customer.affeliateconsumer.consumer.LinkShareConsumerImpl;
import customer.affeliateconsumer.dto.linkshare.EventDTO;
import customer.affeliateconsumer.dto.linkshare.EventListDTO;
import customer.affeliateconsumer.dto.linkshare.GetMerchByAppStatusResponseDTO;
import customer.affeliateconsumer.storage.dao.TransactionStatus;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman rasskazov on 28.05.2015.
 */
public class LinkShareService {

    private static final String NO_RESULTS_FOUND = "No Results Found";
    private static final String FIELD_NAME_ORDER_ID = "Order ID";
    private static final String FIELD_NAME_TOTAL_COMMISSION_AMOUNT_PAID = "Total Commission Amount Paid";
    private static final String FIELD_NAME_PAYMENT_ID = "Payment ID";
    private static final String FIELD_NAME_PAYMENT_STATUS = "Payment Status";
    private static final String FIELD_NAME_INVOICE_NUMBER = "Invoice Number";
    private static final String FIELD_NAME_ADVERTISER_ID = "Advertiser ID";
    private static final String TRANSACTION_STATUS_ISSUED = "Issued";
    private static final String PARAM_PROCESS_DATE_START = "process_date_start";
    private static final String PARAM_PROCESS_DATE_END = "process_date_end";
    private final Logger log = Logger.getLogger(getClass());

    private static final String SPACE = " ";
    private static final String ZERO = "0";

    private MongoService mongoService = MongoService.getInstance();
    private LinkShareConsumer linkShareConsumer;
    private static LinkShareService instance;

    public LinkShareService(LinkShareConsumer linkShareConsumer){
        this.linkShareConsumer = linkShareConsumer;
    }

    public static LinkShareService getInstance(LinkShareConsumer consumer){
        if (instance == null){
            instance = new LinkShareService(consumer);
        }
        return instance;
    }
    public static LinkShareService getInstance() {
        if (instance == null){
            instance = new LinkShareService(new LinkShareConsumerImpl());
        }
        return instance;
    }

    private Double getMinimalCommission(ReturnDTO advertiser) {
        String commissionTerms = advertiser.getOffer().getCommissionTerms();
        Double minimalCommission = null;
        for (String token : commissionTerms.split(SPACE)) {
            if (token.endsWith("%")) {
                try {
                    Double commission = Double.parseDouble(ZERO + token.substring(0, token.length() - 1));
                    if (commission != 0 && (minimalCommission == null || commission < minimalCommission)) {
                        minimalCommission = commission;
                    }
                } catch (NumberFormatException e) {
                    ExceptionLogger.logAndSendError(log, e, "LinkShare update commission: error parsing commission. " +
                            "Advertiser id = " + advertiser.getMid() + ", commission = " + commissionTerms);
                }
            }
        }
        return minimalCommission;
    }

    private List<CSVRecord> parseCSV(String input) throws IOException {
        return parseCSV(input, false, null);
    }

    private List<CSVRecord> parseCSV(String input, boolean unique, String reportName) throws IOException {
        List<CSVRecord> list;
        if (input == null || input.startsWith(NO_RESULTS_FOUND)) {
            list = new ArrayList<CSVRecord>();
        } else {
            Reader in = new StringReader(input);
            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withHeader());
            list = parser.getRecords();
        }
        if (unique && list.size() != 1) {
            ExceptionLogger.logAndSendError(log, null, "LinkShare " + reportName + " report failture. Expected 1 " +
                    "record, " +
                    "found:" + list.size() +
                    ". Full response:\n" + input);
        }
        return list;
    }

    public int updateAdvertisersCommissions() {
        GetMerchByAppStatusResponseDTO advertisersList = linkShareConsumer.getLinkLocatorReport();
        for (ReturnDTO advertiser : advertisersList.getReturns()) {
            Double commission = getMinimalCommission(advertiser);
            mongoService.updateAdvertiserCommission(advertiser.getMid(), commission, AffiliateNetwork.Linkshare);
        }
        return advertisersList.getReturns().size();
    }

    public int requestNewTransactions() {
        MultivaluedMapImpl map = new MultivaluedMapImpl();
        String startDate = MongoService.getParameter(ParameterName.LAST_UPDATED_LINKSHARE_EVENT);
        String endDate = DateParser.getLinkShareTimeGMT();
        map.putSingle(PARAM_PROCESS_DATE_START, startDate);
        map.putSingle(PARAM_PROCESS_DATE_END, endDate);
        int page = 0;
        EventListDTO eventList = linkShareConsumer.getEvents(map, page);
        do
        {
            if (page != 0 ){
                eventList = linkShareConsumer.getEvents(map, page);
            }
            for (EventDTO event : eventList) {
                mongoService.updateTransaction(new EventConverter(event));
            }
            page++;
        } while (eventList.size() == LinkShareConsumerImpl.PARAM_LIMIT_VALUE);
        MongoService.setParameter(ParameterName.LAST_UPDATED_LINKSHARE_EVENT, endDate);
        return eventList.size();
    }

    public int requestConfirmedTransactions() {
        String startDate = MongoService.getParameter(ParameterName.LAST_UPDATED_LINKSHARE_SIGNATURE);
        String endDate = DateParser.getLinkShareAdvancedTimeGMT();
        String report = linkShareConsumer.getSignatureOrderReport(startDate, endDate);
        int updated = 0;
        List<CSVRecord> records;
        try {
            records = parseCSV(report);
            for (CSVRecord record : records) {
                boolean result = mongoService.updateTransaction(new SignatureConverter(record));
                if (result) {
                    updated++;
                }
            }
            MongoService.setParameter(ParameterName.LAST_UPDATED_LINKSHARE_SIGNATURE, endDate);
        } catch (Exception e) {
            ExceptionLogger.logAndSendError(log, e, null);
        }
        return updated;
    }

    public int requestAvailableTransactions() {
        try {
            String startDate = MongoService.getParameter(ParameterName.LAST_UPDATED_LINKSHARE_PAYMENT_HISTORY);
            String endDate = DateParser.getLinkShareAdvancedTimeGMT();
            String paymentHistory = linkShareConsumer.getPaymentHistorySummary(startDate, endDate);
            List<CSVRecord> paymentHistoryList = parseCSV(paymentHistory);
            for (CSVRecord recordSummary : paymentHistoryList) {
                if (!recordSummary.get(FIELD_NAME_PAYMENT_STATUS).equalsIgnoreCase(TRANSACTION_STATUS_ISSUED)){
                    continue;
                };
                String commission = recordSummary.get(FIELD_NAME_TOTAL_COMMISSION_AMOUNT_PAID);
                String paymentId = recordSummary.get(FIELD_NAME_PAYMENT_ID);
                String advertiserPaymentHistory = linkShareConsumer.getAdvertiserPaymentHistory(paymentId);

                List<CSVRecord> advertiserPaymentHistoryList = parseCSV(advertiserPaymentHistory, true,
                        "AdvertiserPaymentHistory");
                for (CSVRecord advertiserHistoryRecord : advertiserPaymentHistoryList) {

                    String invoiceId = advertiserHistoryRecord.get(FIELD_NAME_INVOICE_NUMBER);
                    String paymentDetails = linkShareConsumer.getPaymentDetails(invoiceId);

                    List<CSVRecord> paymentDetailsList = parseCSV(paymentDetails, true, "PaymentDetails");
                    for (CSVRecord paymentDetail : paymentDetailsList) {

                        String advertiserId = paymentDetail.get(FIELD_NAME_ADVERTISER_ID);
                        String orderId = paymentDetail.get(FIELD_NAME_ORDER_ID);
                        mongoService.updateTransactionStatus(advertiserId, null, orderId, AffiliateNetwork.Linkshare,
                                TransactionStatus.AVAILABLE, commission);
                    }
                }
            }
            MongoService.setParameter(ParameterName.LAST_UPDATED_LINKSHARE_PAYMENT_HISTORY, endDate);
            return paymentHistoryList.size();
        } catch (IOException e) {
            ExceptionLogger.logAndSendError(log, e, null);
        }
        return 0;
    }

}
