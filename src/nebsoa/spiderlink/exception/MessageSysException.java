package nebsoa.spiderlink.exception;

import nebsoa.common.exception.SysException;
import nebsoa.common.util.DataMap;

public class MessageSysException extends SysException {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3538768699309384248L;

	/**
	 *  거래ID
	 */
	protected String trxId;
	
	/**
	 *  기관ID
	 */
	protected String orgId;
	
	/**
	 *  UID
	 */
	protected String uid;
	
	/**
	 *  거래 시각
	 */
	protected String trxTime;
	
	/**
	 *  기관 UID
	 */
	protected String orgUid;
	
	private DataMap dataMap;
	
	public MessageSysException(){}
	
    public MessageSysException(String msg) {
        super(msg);
    }

    public MessageSysException(String errorCode, String msg) {
        super(errorCode, msg);
    }
    
    public MessageSysException(String errorCode, String msg, DataMap dataMap) {
        super(errorCode, msg);
        this.dataMap = dataMap;
    }
    
    public MessageSysException(Throwable e) {
    	super(e);
    }
    
    public MessageSysException(Throwable e, DataMap dataMap) {
    	super(e);
    	this.dataMap = dataMap;
    }    

    public MessageSysException(String errorCode, Throwable e) {
        super(errorCode, e);
    }
	
    public MessageSysException(String errorCode, Throwable e, DataMap dataMap) {
        super(errorCode, e);
        this.dataMap = dataMap;
    }
    
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgUid() {
		return orgUid;
	}

	public void setOrgUid(String orgUid) {
		this.orgUid = orgUid;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public DataMap getDataMap(){
		return dataMap;
	}
	
	public void setDataMap(DataMap dataMap){
		this.dataMap = dataMap;
	}		
}
