package customer.affeliateconsumer.task;

import customer.affeliateconsumer.service.CJService;

/**
 * Created by roman rasskazov on 31.05.2015.
 */
public class CJUpdateAdvertiserCommissionsTask extends RestTask{

    @Override
    public String getTaskName() {
        return "CJ Update Advertisers Commissions";
    }

    @Override
    public String getParameterName() {
        return "CJ_UPDATE_COMMISSIONS";
    }

    @Override
    public int runTask() {
        return CJService.getInstance().updateAdvertisersCommissions();
    }

}
