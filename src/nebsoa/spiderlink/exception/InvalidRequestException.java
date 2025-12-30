package nebsoa.spiderlink.exception;

import nebsoa.common.util.DataMap;

public class InvalidRequestException extends MessageSysException {

	public InvalidRequestException(String stdErrorCode, String errorMessage) {
		super(stdErrorCode, errorMessage);
	}
	
	public InvalidRequestException(String stdErrorCode, String errorMessage, DataMap dataMap) {
		super(stdErrorCode, errorMessage, dataMap);
	}

}
