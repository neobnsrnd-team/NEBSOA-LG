package nebsoa.spiderlink.exception;

import nebsoa.common.util.DataMap;

public class MessageTimeoutException extends MessageException {

	public MessageTimeoutException(String stdErrorCode, String errorMessage) {
		super(stdErrorCode, errorMessage);
	}

	public MessageTimeoutException(String stdErrorCode, String orgErrorCode, String errorMessage) {
		super(stdErrorCode, orgErrorCode, errorMessage);
	}
	
	public MessageTimeoutException(String stdErrorCode, String errorMessage, DataMap dataMap) {
		super(stdErrorCode, errorMessage, dataMap);
	}	

}
