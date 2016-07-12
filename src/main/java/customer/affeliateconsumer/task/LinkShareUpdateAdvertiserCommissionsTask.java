package customer.affeliateconsumer.task;

import customer.affeliateconsumer.service.LinkShareService;

/**
 * Created by roman rasskazov on 31.05.2015.
 */
public class LinkShareUpdateAdvertiserCommissionsTask extends RestTask{

    @Override
    public String getTaskName() {
        return "LinkShare Update Advertisers Commissions";
    }

    @Override
    public String getParameterName() {
        return "LINKSHARE_UPDATE_COMMISSIONS";
    }

    @Override
    public int runTask() {
        return LinkShareService.getInstance().updateAdvertisersCommissions();
    }

}
