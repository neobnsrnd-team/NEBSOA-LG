package nebsoa.spiderlink.exception;

import nebsoa.common.util.DataMap;

public class ErrorResponseException extends MessageException {

	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -5039953114084218760L;

    public ErrorResponseException(String stdErrorCode, String errorMessage) {
		super(stdErrorCode, errorMessage);
	}

	public ErrorResponseException(String stdErrorCode, String orgErrorCode, String errorMessage) {
		super(stdErrorCode, orgErrorCode, errorMessage);
	}
    
	public ErrorResponseException(String stdErrorCode, String orgErrorCode, String errorMessage, String masterErrorMessage, String subErrorMessage) {
		super(stdErrorCode, orgErrorCode, null, errorMessage, masterErrorMessage, subErrorMessage);
	}
	
	public ErrorResponseException(String stdErrorCode, String orgErrorCode, String errorMessage, DataMap dataMap) {
		super(stdErrorCode, orgErrorCode, errorMessage, dataMap);
	}
	
	public ErrorResponseException(String stdErrorCode, String orgErrorCode, String orgType, String errorMessage, DataMap dataMap) {
		super(stdErrorCode, orgErrorCode, orgType, errorMessage, dataMap);
	}
    	
}
