/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 메일 전송을 처리하는 클래스입니다.
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
 * $Log: EmailManager.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:28  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:26  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:48  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/06/20 10:41:46  최수종
 * 오류 핸들러 관련 수정 및 추가
 *
 * Revision 1.2  2007/06/07 09:47:16  홍윤석
 * 수정
 *
 * Revision 1.1  2007/06/07 05:05:08  홍윤석
 * 수정
 *
 *
 * </pre>
 ******************************************************************/

public class EmailManager {

	/**
     * 여러건 메일 전송
	 * @param smtpIP   : SMTP 서버
	 * @param port     : 포트
	 * @param id       : 서버 아이디
	 * @param pw       : 서버 패스워드
	 * @param mailList : 메일 내용
	 * @throws EmailException
	 * @throws IOException
	 */
	public static void send(String smtpIP, String port, String id, String pw, List mailList)
			throws EmailException, IOException {
		MailInfo mailInfo = null;
		String senderName = null;
		List receiverNameList = null;
		String receiverName = null;
		
		for (int m = 0; m < mailList.size(); m++) {
			mailInfo = (MailInfo) mailList.get(m);
			HtmlEmail email = new HtmlEmail();
			email.setCharset("euc-kr"); 
			//Port 지정
			if(!StringUtil.isNull(port)){				
				email.setSmtpPort(Integer.parseInt(port));
			}
			//서버 계정
			if(!StringUtil.isNull(id) && !StringUtil.isNull(pw)){
				email.setAuthentication(id, pw);				
			}
			email.setHostName(smtpIP); // SMTP 서버를 지정
			//송신자
			
			senderName = mailInfo.getSenderName();
			if(senderName == null){
				senderName = "";			
			}
			
			email.setFrom(mailInfo.getSenderEmail(), senderName);
			//수신자
			for(int i = 0; i < mailInfo.getReceiverEmail().size(); i++){	
				
				receiverNameList = mailInfo.getReceiverName();
				if(receiverNameList == null){
					receiverName = "";			
				}
				else
				{
					receiverName = (String)mailInfo.getReceiverName().get(i);
				}
				email.addTo((String)mailInfo.getReceiverEmail().get(i), receiverName);			
			}		
			//메일 제목
			email.setSubject(mailInfo.getSubject());
			//메일내용
			email.setHtmlMsg(mailInfo.getContent());
			email.getMailSession().setDebug(true);
			email.send();
		}

	}
	
	/**
     * 단건 메일 전송
	 * @param smtpIP   : SMTP 서버
	 * @param port     : 포트
	 * @param id       : 서버 아이디
	 * @param pw       : 서버 패스워드
	 * @param mailInfo : 메일 내용
	 * @throws EmailException
	 * @throws IOException
	 */
	public static void send(String smtpIP, String port, String id, String pw, MailInfo mailInfo)
			throws EmailException, IOException {
		
		String senderName = null;
		List receiverNameList = null;
		String receiverName = null;
		
		HtmlEmail email = new HtmlEmail();
		email.setCharset("euc-kr"); 
		//Port 지정
		if(!StringUtil.isNull(port)){				
			email.setSmtpPort(Integer.parseInt(port));
		}
		//서버 계정
		if(!StringUtil.isNull(id) && !StringUtil.isNull(pw)){
			email.setAuthentication(id, pw);				
		}
		email.setHostName(smtpIP); // SMTP 서버를 지정
		
		
		senderName = mailInfo.getSenderName();
		if(senderName == null){
			senderName = "";			
		}
		
		//송신자
		email.setFrom(mailInfo.getSenderEmail(), senderName);
		//수신자
		for(int i = 0; i < mailInfo.getReceiverEmail().size(); i++){	
			
			receiverNameList = mailInfo.getReceiverName();
			if(receiverNameList == null){
				receiverName = "";			
			}
			else
			{
				receiverName = (String)mailInfo.getReceiverName().get(i);
			}
			
			email.addTo((String)mailInfo.getReceiverEmail().get(i), receiverName);			
		}
		
		//메일 제목
		//email.setSubject(ml.getSubject());
		email.setSubject(mailInfo.getSubject());
		//메일내용
		email.setHtmlMsg(mailInfo.getContent());
		
		email.getMailSession().setDebug(true);
		
		email.send();
	}
	
	
	
	
	public static void main(String args[]) throws EmailException, IOException{
		
		ArrayList mailList = new ArrayList();
		
		MailInfo mailInfo = new MailInfo();
		mailInfo.setContent("test 제목");
		ArrayList rEmail = new ArrayList();
		rEmail.add("csj78@dreamwiz.com");
		ArrayList rName = new ArrayList();
		rName.add("홍길동");
		mailInfo.setReceiverEmail(rEmail);
		mailInfo.setReceiverName(rName);
		mailInfo.setSenderEmail("kyun@serverside.co.kr");
		mailInfo.setSenderName("kyun");
		mailInfo.setSubject("test 내용");
		mailList.add(mailInfo);
		
		MailInfo m = null;
		System.out.println("메일 사이즈================> " + mailList.size());
		m = (MailInfo)mailList.get(0);
		System.out.println("첫번째 수신자 사이즈================> " + m.getReceiverEmail().size());
		for(int i = 0; i<m.getReceiverName().size(); i++){
			System.out.println("첫번째 수신자 이메일================> " +m.getReceiverEmail().get(i).toString() );
			System.out.println("첫번째 수신자 이름   ================> "+m.getReceiverName().get(i).toString());
		}
/*		
		ArrayList rEmail1 = new ArrayList();
		ArrayList rName1 = new ArrayList();
		MailInfo mailInfo1 = new MailInfo();
		mailInfo1.setContent("test 단체메일1");
		rEmail1.add("s080914@nate.com");
		rName1.add("신상수");
		rEmail1.add("ohmyallah@nate.com");
		rName1.add("최수종");
		mailInfo1.setReceiverEmail(rEmail1);
		mailInfo1.setReceiverName(rName1);
		mailInfo1.setSenderEmail("kyun@serverside.co.kr");
		mailInfo1.setSenderName("kyun");
		mailInfo1.setSubject("test 단체메일1");
		mailList.add(mailInfo1);
		
		System.out.println("메일 사이즈================> " + mailList.size());
		m = (MailInfo)mailList.get(1);
		System.out.println("두번째 수신자 사이즈================> " + m.getReceiverEmail().size());
		
		for(int i = 0; i<m.getReceiverName().size(); i++){
			System.out.println("두번째 수신자 이메일================> " +m.getReceiverEmail().get(i).toString() );
			System.out.println("두번째 수신자 이름   ================> "+m.getReceiverName().get(i).toString());
		}
*/		
		send("211.115.70.148","25","nonorain","nonorain99", mailList);
		
	}
	
}
