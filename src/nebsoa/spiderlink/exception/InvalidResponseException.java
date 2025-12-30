package nebsoa.spiderlink.exception;

import nebsoa.common.util.DataMap;

public class InvalidResponseException extends MessageException {

	public InvalidResponseException(String stdErrorCode, String errorMessage) {
		super(stdErrorCode, errorMessage);
	}

	public InvalidResponseException(String stdErrorCode, String orgErrorCode, String errorMessage) {
		super(stdErrorCode, orgErrorCode, errorMessage);
	}
	
	public InvalidResponseException(String stdErrorCode, String errorMessage, DataMap dataMap) {
		super(stdErrorCode, errorMessage, dataMap);
	}

}
