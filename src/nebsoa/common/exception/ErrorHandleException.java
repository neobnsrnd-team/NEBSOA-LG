package nebsoa.common.exception;

import nebsoa.spiderlink.exception.MessageException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * ErrorPage를 처리하기 위한 용도로 정의하였다.
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * 시스템 장애를 표현하는 용도가 아님.
 * </font>
 *
 * @author $Author: 김은정
 * @version
 *******************************************************************/
public class ErrorHandleException extends UserException {
	private String errorPageType;
	private String targetPage;
	private String targetURL;
    private String errorCode;
    private String errorMessage;
    private String isOpenerUrl;
    
//    /**
//     * @param errorCode
//     */
//    public ErrorHandleException(String errorCode) {
//        super(errorCode, "BizException");
//    }
//    
//    /**
//     * @param errorCode
//     * @param msg
//     */
//    public ErrorHandleException(String errorCode, String msg) {
//        super(errorCode, msg);
//    }
//    
//    /**
//     * @param errorCode
//     * @param msg
//     * @param targetURL
//     */
//    public ErrorHandleException(String errorCode, String msg, String targetURL ) {
//        super(errorCode, msg);
//        this.targetURL = targetURL; 
//        if(targetURL == null) this.targetURL = "";
//    }

    /**
     * @param ex
     */
    public ErrorHandleException(Throwable ex) {
        super(ex);
    }
    
    public ErrorHandleException(Throwable ex, String errorPageType, String targetPage) {
        super(ex);
        this.errorPageType = errorPageType;
        this.targetPage = targetPage; 
    }
    
    public ErrorHandleException(Throwable ex, String errorPageType, String targetPage, String targetURL) {
        super(ex);
        this.errorPageType = errorPageType;
        this.targetPage = targetPage;
        this.targetURL = targetURL; 
    }
    
    public ErrorHandleException(Throwable ex, String errorPageType, String targetPage, String targetURL, String errorCode) {
        super(ex);
        this.errorPageType = errorPageType;
        this.targetPage = targetPage;
        this.targetURL = targetURL; 
        this.errorCode = errorCode;
    }
    
    public ErrorHandleException(Throwable ex, String errorPageType, String targetPage, String targetURL, String errorCode, String errorMessage) {
        super(ex);
        this.errorPageType = errorPageType;
        this.targetPage = targetPage;
        this.targetURL = targetURL; 
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }    
    
    public ErrorHandleException(Throwable ex, String errorPageType, String targetPage, String targetURL, String errorCode, String errorMessage, String isOpenerUrl) {
        super(ex);
        this.errorPageType = errorPageType;
        this.targetPage = targetPage;
        this.targetURL = targetURL; 
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.isOpenerUrl = isOpenerUrl;
    }    
    
    public void setErrorPageType(String errorPageType){
    	this.errorPageType = errorPageType;
    }
    
    public void setTargetPage(String targetPage){
    	this.targetPage = targetPage;
    }
    
    public void setTargetURL(String targetURL){
    	this.targetURL = targetURL;
    }

    public void setErrorCode(String errorCode){
    	this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage){
    	this.errorMessage = errorMessage;
    }
    
    public void setIsOpenerUrl(String isOpenerUrl){
    	this.isOpenerUrl = isOpenerUrl;
    }
    
    public String getErrorPageType(){
    	return errorPageType;
    }
    
    public String getTargetPage(){
    	return targetPage;
    }
    
    public String getTargetURL(){
    	return targetURL;
    }
    
    public String getErrorCode(){
    	return errorCode;
    }      
    
    public String getErrorMessage() {
    	return errorMessage;
    }
    
    public String getIsOpenerUrl(){
    	return isOpenerUrl;
    }
}
