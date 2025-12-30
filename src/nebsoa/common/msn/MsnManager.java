/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.msn;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.commons.mail.EmailException;

import rath.msnm.MSNMessenger;
import rath.msnm.UserStatus;
import rath.msnm.entity.MsnFriend;
import rath.msnm.event.MsnAdapter;
import rath.msnm.msg.MimeMessage;
import nebsoa.common.log.LogManager;
import nebsoa.common.mail.MailInfo;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * msn 전송을 처리하는 클래스입니다.
 * 
 * 2.사용법
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
 * $Log: MsnManager.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:28  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:36  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2007/08/17 12:24:02  최수종
 * *** empty log message ***
 *
 * Revision 1.12  2007/07/19 06:50:19  최수종
 * *** empty log message ***
 *
 * Revision 1.11  2007/07/17 20:03:53  최수종
 * *** empty log message ***
 *
 * Revision 1.10  2007/07/17 20:03:27  최수종
 * *** empty log message ***
 *
 * Revision 1.9  2007/07/17 18:25:38  최수종
 * msn 메시지 전송 로직 수정
 *
 * Revision 1.8  2007/07/17 08:58:31  김재범
 * 여러명에게 메세지 보내는 메서드 추가
 *
 * Revision 1.7  2007/07/17 07:37:42  최수종
 * *** empty log message ***
 *
 * Revision 1.6  2007/07/17 07:33:40  최수종
 * *** empty log message ***
 *
 * Revision 1.5  2007/07/17 07:29:24  최수종
 * *** empty log message ***
 *
 * Revision 1.4  2007/07/17 07:28:17  최수종
 * *** empty log message ***
 *
 * Revision 1.3  2007/07/17 07:27:15  최수종
 * *** empty log message ***
 *
 * Revision 1.2  2007/07/17 06:53:50  김재범
 * 수정
 *
 * Revision 1.1  2007/07/15 14:36:59  김재범
 * 최초등록
 *
 *
 * Revision 1.1  2007/07/15 23:10:08  김재범
 * 수정
 *
 *
 * </pre>
 ******************************************************************/

public class MsnManager {
	
	private static final String MSN_DEFAULT_LOGIN_ID = "maria4901@msn.com";  // MSN 로그인 아이디
	private static final String MSN_DEFAULT_LOGIN_PW = "123456";  // MSN 로그인 패스워드
	
	private static MsnManager instance;
	
 
	private MSNMessenger msn;

	
	private MsnManager() {}

    public static synchronized MsnManager getInstance() {
        
    	if (instance == null) 
        {
            instance = new MsnManager();
        }
        return instance;
    }
	   
    /**
     * msn으로 메시지를 보내기 위한 
     * msn 로그인 처리 메소드
     * 
     * @param msnUserId 보낸사람의 msn ID
     * @param password 보낸사람의 msn 비밀번호
     */
	private void msnLogin(String msnUserId , String password) throws Exception {

		//로그인이 되어 있지 않으면 로그인 한다.
//		if(msn == null || !msn.isLoggedIn()) {
		
			msn = new MSNMessenger(msnUserId, password);
			
			//user 상태를 온라인으로 바꾼다.
        	msn.setInitialStatus(UserStatus.ONLINE);   
        	
        	// 이벤트 설정(로그인 이벤트 발생시 로그 남김)
			msn.addMsnListener(new MsnAdapter() {
				public void loginComplete(MsnFriend friend) {
					LogManager.debug("@@@@@ MSN 로그인 완료 !!!");
				}
			});	        	

			// msn 로그인
        	msn.login();
//		}

		// 최대 30초까지 로그인이 될때까지 기다린다. 
		// 로그인이 되어야 메시지 전송이 가능하며, 로그인이 되기까지 5~10여초의 시간이 필요하기 때문임.
		int i=1;
		while(true) {
			LogManager.debug("trying logging in...");
			
			if(!msn.isLoggedIn()) {
				Thread.sleep(1000);
				
				LogManager.debug("trying logging in isLoggedIn...");
				
			} else {
				
				LogManager.debug("trying logging in break...");
				
				break;
			}
			
		    // 최대 10초까지 대기
			if(i > 10) {
				LogManager.debug("logging fail...");
				
				break;
			}
			i++;
		}	
		
	}
	
	/**
	 * msn으로 한명에게 메시지를 전송하는 메소드
	 * 
	 * @param receiverId 받는사람의 msn ID
	 * @param message 보낼 메시지
	 * @throws Exception
	 */
	public void sendMessage(String receiverId, String message) throws Exception {
		
		ArrayList receiverIds = new ArrayList();
		receiverIds.add(receiverId);
		
		sendMessage(MSN_DEFAULT_LOGIN_ID, MSN_DEFAULT_LOGIN_PW, receiverIds, message);
	}	
	
	/**
	 * msn으로 여러명에게 메시지를 전송하는 메소드
	 * 
	 * @param receiverId 받는사람의 msn ID
	 * @param message 보낼 메시지
	 * @throws Exception
	 */
	public void sendMessage(ArrayList receiverIds, String message) throws Exception {
		
		sendMessage(MSN_DEFAULT_LOGIN_ID, MSN_DEFAULT_LOGIN_PW, receiverIds, message);
	}	

	
	/**
	 * msn으로 한명에게 메시지를 전송하는 메소드
	 * 
     * @param msnUserId 보낸사람의 msn ID
     * @param password 보낸사람의 msn 비밀번호  
	 * @param receiverId 받는사람의 msn ID
	 * @param message 보낼 메시지
	 * @throws Exception
	 */
	public void sendMessage(String msnUserId , String password,
			String receiverId, String message) throws Exception {
		
		ArrayList receiverIds = new ArrayList();
		receiverIds.add(receiverId);
		
		sendMessage(msnUserId, password, receiverIds, message);
	}
	
	
	/**
	 * msn으로 여러명에게 메시지를 전송하는 메소드
	 * 
     * @param msnUserId 보낸사람의 msn ID
     * @param password 보낸사람의 msn 비밀번호 
	 * @param receiverIds 받는사람의 msn ID 의 ArrayList
	 * @param message 보낼 메시지
	 * @throws Exception
	 */
	public void sendMessage(String msnUserId , String password, 
			ArrayList receiverIds, String message) throws Exception {
		
		
		if(msn == null || !msn.isLoggedIn()) {  // 로그인 전
			
//			throw new Exception("로그인 후 사용하시기 바랍니다.");
			msnLogin(msnUserId, password);
		}
						
		String receiverId = null;  // 받는 사람의 msn ID
		
		// 유저 순서대로 메세지를 보낸다.
		for(int i=0; i<receiverIds.size(); i++) {

			receiverId = receiverIds.get(i).toString();
			
			if(receiverId == null)
			{
				throw new Exception("MSN 전송을 위한 필수 파라미터[보낸사람의 ID]가 없습니다.");
			}
		
			String friendLoginName = receiverId;			
            msn.doCallWait(friendLoginName);
			MimeMessage sendMessage = new MimeMessage(message);  // 메세지 저장
			sendMessage.setKind(MimeMessage.KIND_MESSAGE);
         	
			if (msn.sendMessage(friendLoginName, sendMessage) == true )  // 메세지 보내는부분(성공적으로 전송시)
			{ 
				LogManager.debug("@@@@@ msn send success!!! :"+ receiverId);
			} 
			else  // 메세지 전송 실패시
			{ 
				LogManager.debug("@@@@@ message :"+sendMessage.getMessage());
				LogManager.debug("@@@@@ msn send failed!!! :"+ receiverId);
				throw new Exception("MSN 전송이 실패하였습니다.");
			}
		}
	}
	
	public static void main(String args[]) throws Exception{
		
		//MsnManager.SendMessage("flea815@hotmail.com","OUT OF MEMORY 에러 발생");			
		//MsnManager.SendMessage("ohmyallah@hotmail.com","OUT OF MEMORY 에러 발생");
		ArrayList user = new ArrayList();
//		user.add("flea815@hotmail.com");
//		user.add("serverside@msn.com");
		user.add("ohmyallah@hotmail.com");
//		user.add("ssagazyless@hotmail.com");
		
		MsnManager.getInstance().sendMessage(user, "시스템 장애가 발생했습니다.");
		

	}
}



