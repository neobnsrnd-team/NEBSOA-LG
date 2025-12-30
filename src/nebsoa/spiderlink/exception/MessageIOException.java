package nebsoa.spiderlink.exception;

import nebsoa.common.util.DataMap;

public class MessageIOException extends MessageSysException {

	public MessageIOException(String stdErrorCode, String errorMessage) {
		super(stdErrorCode, errorMessage);
	}

	public MessageIOException(String stdErrorCode, String errorMessage, DataMap dataMap) {
		super(stdErrorCode, errorMessage, dataMap);
	}	

}
