package customer.affeliateconsumer.service;

import customer.affeliateconsumer.DateParser;
import customer.affeliateconsumer.ParameterName;
import customer.affeliateconsumer.converter.CommissionDetailsConverter;
import customer.affeliateconsumer.dto.cj.advertiser.ActionDTO;
import customer.affeliateconsumer.dto.cj.commission.CommissionDetailsDTO;
import customer.affeliateconsumer.AffiliateNetwork;
import customer.affeliateconsumer.dto.cj.advertiser.AdvertiserDTO;
import customer.affeliateconsumer.consumer.CJConsumer;
import customer.affeliateconsumer.consumer.CJConsumerImpl;
import customer.affeliateconsumer.dto.cj.advertiser.AdvertisersListDTO;
import customer.affeliateconsumer.dto.cj.advertiser.CommissionDTO;
import customer.affeliateconsumer.dto.cj.commission.CommissionsListDTO;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman rasskazov on 25.05.2015.
 */
public class CJService {

    private Logger log = Logger.getLogger(getClass());

    private static String PERCENT = "%";
    private static String USD = "USD";
    private static final String ACTION_TYPE_SALE = "sale";
    private static final String ACTION_TYPE_ADVANCED_SALE = "advanced sale";
    private static final String ACTION_TYPE_ADVANCED_LEAD = "advanced lead";

    private MongoService mongoService = MongoService.getInstance();

    private CJConsumer cjConsumer;

    public CJService(CJConsumer cjConsumer){
        this.cjConsumer = cjConsumer;
    }

    private static CJService instance;

    public static CJService getInstance(CJConsumer consumer){
        if (instance == null){
            instance = new CJService(consumer);
        }
        return instance;
    }

    public static CJService getInstance(){
        if (instance == null){
            instance = new CJService(new CJConsumerImpl());
        }
        return instance;
    }

    private Double parseCommission(String advertiserId, String commission) {
        try {
            if (commission.endsWith(PERCENT)) {
                return Double.parseDouble(commission.substring(0, commission.length() - 1));
            } else if (commission.startsWith(USD)) {
                //ignore fixed commissions, just check that have expected format
                Double.parseDouble(commission.substring(USD.length()));
                return null;
            }
        } catch (Exception e) {
            //generate warning below
        }
        //warning because of not a number or unexpected commission format
        log.warn("CJ update commissions task: Unexpected commission format. advertiserId = " + advertiserId + ", " +
                "commission = " + commission);
        return null;
    }

    private Double getMinimalCommission(AdvertiserDTO advertiser) {
        List<String> commissionsList = new ArrayList<String>();
        for (ActionDTO action : advertiser.getActions()) {
            switch (action.getType()) {
                case ACTION_TYPE_SALE:
                case ACTION_TYPE_ADVANCED_SALE:
                    CommissionDTO commission = action.getCommission();
                    commissionsList.add(commission.getDefaultCommission());
                    if (commission.getItemlist() != null) {
                        commissionsList.addAll(action.getCommission().getItemlist());
                    }
                    break;
                case ACTION_TYPE_ADVANCED_LEAD:
                    break;
                default:
                    log.warn("CJ update commissions task: Not appropriate action type received. Advertiser-id = " +
                            advertiser.getAdvertiserId() + ", action type = " + action.getType());
            }
        }
        Double minimalCommission = null;
        for (String commissionString : commissionsList) {
            Double commission = parseCommission(advertiser.getAdvertiserId(), commissionString);
            if (minimalCommission == null || commission != null && commission < minimalCommission) {
                minimalCommission = commission;
            }
        }
        if (minimalCommission == null) {
            log.warn("CJ update commissions task: not minimal percentage commission was found in actions of supported" +
                    " types (advanced sale, sale) for advertiser-id:" + advertiser.getAdvertiserId());
        }
        return minimalCommission;
    }

    public int updateAdvertisersCommissions() {
        AdvertisersListDTO advertisersList = cjConsumer.requestJoinedAdvertisers(0);
        for (int page = 0 ; page <= (advertisersList.getAdvertisers().getTotal()-1) / CJConsumerImpl.PARAM_RECORDS_PER_PAGE_VALUE ; page++ ) {
            if (page != 0){
                advertisersList = cjConsumer.requestJoinedAdvertisers(page);
            }
            for (AdvertiserDTO advertiser : advertisersList.getAdvertisers().getAdvertisers()) {
                Double commission = getMinimalCommission(advertiser);
                mongoService.updateAdvertiserCommission(advertiser.getAdvertiserId(), commission, AffiliateNetwork.CJ);
            }
        }
        return advertisersList.getAdvertisers().getAdvertisers().size();
    }

    public int requestNewTransactions() {
        String startDate = MongoService.getParameter(ParameterName.LAST_UPDATED_CJ_COMMISSION_DETAILS);
        String endDate = DateParser.getCJCommissionsTimeTomorrowGMT();
        CommissionsListDTO transactionList = cjConsumer.requestCommissionDetail(startDate, endDate);
        for (CommissionDetailsDTO transaction : transactionList.getCommissions()) {
            mongoService.updateTransaction(new CommissionDetailsConverter(transaction));
        }
        MongoService.setParameter(ParameterName.LAST_UPDATED_CJ_COMMISSION_DETAILS, DateParser.getCJCommissionsTimeGMT());
        return transactionList.getCommissions().size();
    }

}
