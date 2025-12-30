package nebsoa.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nebsoa.common.jdbc.DBManager;
import nebsoa.service.util.BankStatus;

public class BankStatusChecker implements BankStatusCheckable {

    /**
     * 은행상태 정보를 가지고 있는 객체들의 풀 
     */
    private Map bankStatusPool;
    
    /**
     * bank status loading sql
     */
    private static final String BANK_STATUS_LOAD_SQL =
        "SELECT BANK_CODE "
        +"\n, BANK_NAME "
        +"\n, STABLE_YN "
        +"\n, TIMEOUT "
        +"\n FROM FWK_BANK_STATUS ";
    
	/**
     * Load bank status information from FWK_BANK_STATUS table
     */
    public Map loadBankStatusPool() {

        ArrayList list = DBManager.executePreparedQueryToBeanList(
                BANK_STATUS_LOAD_SQL, new Object[]{}, BankStatus.class);
        
        bankStatusPool = new HashMap();
        BankStatus bankStatus = null;

        for (int index = 0, size = list.size(); index < size; index++) {
            bankStatus = (BankStatus) list.get(index);
            bankStatusPool.put(bankStatus.getBankCode(), bankStatus);
        }
    	return bankStatusPool;
    }
}
