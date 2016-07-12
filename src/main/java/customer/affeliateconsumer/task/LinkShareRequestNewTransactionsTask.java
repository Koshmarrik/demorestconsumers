package customer.affeliateconsumer.task;

import customer.affeliateconsumer.service.LinkShareService;

/**
 * Created by roman rasskazov on 31.05.2015.
 */
public class LinkShareRequestNewTransactionsTask extends RestTask{

    @Override
    public String getTaskName() {
        return "LinkShare Request New Transactions";
    }

    @Override
    public String getParameterName() {
        return "LINKSHARE_REQUEST_NEW_TRANSACTIONS";
    }

    @Override
    public int runTask() {
        return LinkShareService.getInstance().requestNewTransactions();
    }

}
