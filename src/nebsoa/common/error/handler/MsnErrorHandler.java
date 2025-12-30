/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.error.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import nebsoa.common.collection.DataSet;
import nebsoa.common.exception.SysException;
import nebsoa.common.jdbc.DBHandler;
import nebsoa.common.log.LogManager;
import nebsoa.common.mail.EmailManager;
import nebsoa.common.mail.MailInfo;
import nebsoa.common.msn.MsnManager;
import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 실질적으로 오류처리를 담당하는 클래스입니다.
 * 오류 발생시 Msn으로 오류 내용을 전송합니다.
 * 
 * 2.사용법
 * userParamValue값은 "Key=Value;Key=Value;Key=Value;" 형식의 문자열로
 * 이루어져 있다.
 * 
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: MsnErrorHandler.java,v $
 * Revision 1.1  2018/01/15 03:39:51  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:05  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:25  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:28  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:55  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/19 06:50:19  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2007/07/17 20:03:27  최수종
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/17 18:25:50  최수종
 * 최초등록
 *
 *
 *
 * </pre>
 ******************************************************************/
public class MsnErrorHandler extends ErrorHandler {

	/**
	 * 오류 핸들러 추가시 비즈니스 로직을 구현할 메소드
	 * 
	 * 송신자명과 수신자명 파라미터 설정은 옵션이며, 
	 * 그 외의 모든 파라미터는 필수로 설정되어야 한다.
	 * 
	 * 
	 * @param userParamVaule 사용자용 파라미터 설정값
	 * @param sysParamValue 시스템용 파라미터 설정값
	 */
	public void executeHandler(String userParamVaule, String sysParamValue) throws Exception
	{
		
		try {
			

			LogManager.debug("##### MsnErrorHandler.executeHandler() 실행 !!");
			
			if(userParamVaule == null)
			{
				throw new SysException("사용자용 파라미터 설정값이 없습니다.");
			}
			
	//		if(sysParamValue == null)
	//		{
	//			throw new SysException("시스템용 파라미터 설정값이 없습니다.");
	//		}
			
	
				
				
			String sysKey = null;
			String sysValue = null;
			
			String id = null;
			String password = null;
			
			int sysCount = 0;
			int userCount = 0;
			String sysTemp = null;
				
			// 시스템용 파라미터 처리 부분
			if(sysParamValue != null) 
			{
				
				StringTokenizer sysSt = new StringTokenizer(sysParamValue, ";");
				
				while(sysSt.hasMoreTokens())
				{
					sysTemp = sysSt.nextToken();
		
		//			System.out.println("{all}sysTemp=>"+sysTemp);
					
					StringTokenizer sysTempSt = new StringTokenizer(sysTemp, "=");
					while(sysTempSt.hasMoreTokens())
					{		
						sysKey = sysTempSt.nextToken().trim();
						
						if(sysTempSt.hasMoreTokens())
						{
							if(sysKey.equals("id")) {  // 보낸사람의 msn ID
								id = sysTempSt.nextToken().trim();
								sysCount++;					
							}
							else if(sysKey.equals("password")) {  // 보낸사람의 msn 비밀번호
								password = sysTempSt.nextToken().trim();
								sysCount++;					
							}
							else
							{
								throw new SysException("올바르지 않은 Key 형식입니다. Key = ["+sysKey+"]");
							}				
						}
					}
				}		
			}
			
			// 사용자용 파라미터 처리 부분
			StringTokenizer userSt = new StringTokenizer(userParamVaule, ";");
			
			String userKey = null;
			String userValue = null;
	
			ArrayList arrayList = new ArrayList();
			String message = null;
			
			String userTemp = null;
			while(userSt.hasMoreTokens())
			{
				userTemp = userSt.nextToken();
				
	//			System.out.println("{all}temp=>"+userTemp);
				
				StringTokenizer userTempSt = new StringTokenizer(userTemp, "=");
				while(userTempSt.hasMoreTokens())
				{		
					userKey = userTempSt.nextToken().trim();
					
					if(userTempSt.hasMoreTokens())
					{
						userValue = userTempSt.nextToken().trim();
						
	//					System.out.println("{all}userKey=>"+userKey);
	//					System.out.println("{all}userValue=>"+userValue);
						
						
						if(userKey.equals("id")) {  // 받는사람 msn ID
	//						System.out.println("receiverEmail Key=>"+userKey); 
	//						System.out.println("receiverEmail Value=>"+userValue);					
							
							StringTokenizer userReceiverSt = new StringTokenizer(userValue, ",");
	
							while(userReceiverSt.hasMoreTokens())
							{
								String str = userReceiverSt.nextToken().trim();
								
	//							System.out.println("userValue=>"+str);
								arrayList.add(str);
							}
	
							userCount++;
						}
						else if(userKey.equals("message")) {  // msn으로 전송할 메시지
							
							message = userValue;
							userCount++;
	//						System.out.println("[receiverName]Key=>"+userKey);
	//						System.out.println("[receiverName]Value=>"+userValue);					
	
	//						StringTokenizer userReceiverSt = new StringTokenizer(userValue, ",");
	//						ArrayList arrayList = new ArrayList();
	//						while(userReceiverSt.hasMoreTokens())
	//						{
	//							String str = userReceiverSt.nextToken().trim();
	//							
	//							System.out.println("[receiverName]userValue=>"+str);
	//							arrayList.add(str);
	//						}
						}		
						else
						{
							throw new SysException("올바르지 않은 Key 형식입니다. Key = ["+userKey+"]");
						}					
						
					}
				}
			}
			
			
	//		System.out.println("ip=>"+ip);
	//		System.out.println("port=>"+port);
	//		System.out.println("id=>"+id);
	//		System.out.println("password=>"+password);
	//		System.out.println("mailInfo=>"+mailInfo);
	//		
	//		
	//		System.out.println("sysCount=>"+sysCount);
	//		System.out.println("userCount=>"+userCount);
	//		
	//		
	//		System.out.println("arrayList=>["+arrayList+"]");
	//		System.out.println("message=>["+message+"]");
	//		System.out.println("id=>["+id+"]");
	//		System.out.println("password=>["+password+"]");
			
			// 필수 입력항목 갯수 체크
			if(userCount == 2 && sysCount == 0) {  // 사용자용 파라미터만 등록했을 경우
				
				MsnManager.getInstance().sendMessage(arrayList, message);
			}
			else if(userCount == 2 && sysCount == 2) {  // 사용자용 파라미터, 시스템 파라미터 모두 등록했을 경우
				
				MsnManager.getInstance().sendMessage(id, password, arrayList, message);
			}
			else
			{
				// 메일 보내지 않음
				throw new SysException("필수입력할 데이타가 있습니다.");
			}
		
		}
		catch(Exception e)
		{
//			if(e instanceof java.net.UnknownHostException)
//			{
//				throw new Exception("[MSN] 네크워크 연결 장애");
//			}
//			else 
//			{
				throw new Exception("MSN 전송 실패");
//			}
		}
	}
	
	
	public static void main(String[] args)
	{
		
//		String userParamVaule = 
////			"receiverEmail=afoc77@nate.com,ohmyallah@nate.com;receiverName=홍길동,김길동;subject=멜테스트;content=테스트입니다.";
//			"receiverEmail=afoc77@nate.com,ohmyallah@nate.com;receiverName=;subject=멜테스트1881112345;content=테스트입니다.112322245";			
//		String sysParamValue = 
////			"ip=211.115.70.148;port=25;id=nonorain;password=nonorain99;senderEmail=ohmyallah@nate.com;senderName=최수종";
//			"ip=211.115.70.148;port=25;id=nonorain;password=nonorain99;senderEmail=ohmyallah@nate.com;senderName=";
		
		String userParamVaule = 
			"id=ohmyallah@hotmail.com,flea815@hotmail.com;message=기관 시스템이 중지되었습니다. 조속한 확인 및 조치 바랍니다.";

		String sysParamValue = " ";
//			"id=maria4901@msn.com;password=123456";
		
		String executeResultText = null;
		String executeResultCode = null;
		try
		{
		
			MsnErrorHandler handler = new MsnErrorHandler();
			
			handler.executeHandler(userParamVaule, sysParamValue);
		}
		catch(Exception e)
		{
			LogManager.error("오류별 처리 핸들러 ID:[] 수행중 에러가 발생했습니다. "+e.getMessage());
			
//			StringBuffer errorMsgBuf = new StringBuffer(e.toString());  // 에러메시지
//			
//			
//			Object[] obj = e.getCause().getStackTrace();
//			for(int i=0 ; i<obj.length ; i++)
//			{
//				errorMsgBuf.append("\n");
//				errorMsgBuf.append(obj[i]);
//			}
//			executeResultText = errorMsgBuf.toString();
//			
//			byte[] executeResultByte = executeResultText.getBytes();
//			byte[] executeResultTempByte = new byte[2000];
//			
//			if(executeResultByte.length > 2000)
//			{
//				System.arraycopy(executeResultByte, 0, executeResultTempByte, 0, 2000);
//			}				
//			
//			executeResultCode = "99999999";
//			executeResultText = new String(executeResultTempByte);
//			
//			LogManager.debug("@@@@ executeResult : ["+ executeResultText +"]");
			
//			e.printStackTrace();
		}
	}
}






