package customer.affeliateconsumer.task;

import customer.affeliateconsumer.service.CJService;

/**
 * Created by roman rasskazov on 31.05.2015.
 */
public class CJUpdateTransactionsTask extends RestTask{

        @Override
        public String getTaskName() {
            return "CJ Update Transactions";
        }

        @Override
        public String getParameterName() {
            return "CJ_UPDATE_TRANSACTIONS";
        }

        @Override
        public int runTask() {
            return CJService.getInstance().requestNewTransactions();
        }

    }
