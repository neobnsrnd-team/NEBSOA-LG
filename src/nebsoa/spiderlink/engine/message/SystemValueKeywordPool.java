package nebsoa.spiderlink.engine.message;


import java.util.HashMap;
import java.util.Map;

import nebsoa.common.util.DataMap;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 시스템 생성 값을 관리하고 생성해주는 클래스
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
 * $Log: SystemValueKeywordPool.java,v $
 * Revision 1.1  2018/01/15 03:39:48  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:16  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:50  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:22  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:20  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.3  2008/01/10 03:25:42  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/09 09:59:28  안경아
 * 루프count를 얻어오기 위하여 변경
 *
 * Revision 1.1  2007/11/26 08:38:05  안경아
 * *** empty log message ***
 *
 * Revision 1.28  2006/11/27 08:24:40  김승희
 * SystemKeywordPool 클래스 Factory방식으로 생성
 *
 * Revision 1.27  2006/11/08 02:11:26  김승희
 * web access ip를 우선적으로 넣는다.
 *
 * Revision 1.26  2006/10/28 05:57:40  김승희
 * *** empty log message ***
 *
 * Revision 1.25  2006/10/28 03:31:26  김승희
 * *** empty log message ***
 *
 * Revision 1.24  2006/10/21 05:35:23  안경아
 * *** empty log message ***
 *
 * Revision 1.23  2006/09/11 08:11:19  김승희
 * UID 관련 변경
 *
 * Revision 1.22  2006/08/24 04:21:23  안경아
 * *** empty log message ***
 *
 * Revision 1.21  2006/08/24 04:17:02  안경아
 * *** empty log message ***
 *
 * Revision 1.20  2006/08/23 11:58:32  안경아
 * *** empty log message ***
 *
 * Revision 1.19  2006/08/23 04:43:38  안경아
 * *** empty log message ***
 *
 * Revision 1.18  2006/08/22 00:20:01  안경아
 * *** empty log message ***
 *
 * Revision 1.17  2006/08/21 07:46:34  안경아
 * *** empty log message ***
 *
 * Revision 1.16  2006/08/04 08:03:54  김승희
 * _$OTP_전문일련번호  추가
 *
 * Revision 1.15  2006/07/10 07:13:36  김승희
 * 인터페이스환경 추가
 *
 * Revision 1.14  2006/07/07 07:07:42  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2006/07/04 00:35:26  안경아
 * *** empty log message ***
 *
 * Revision 1.12  2006/06/26 10:48:14  김승희
 * _$IVOSTRO_UID 추가
 *
 * Revision 1.11  2006/06/19 11:36:15  김승희
 * _$송신서버일련번호 키워드 추가
 *
 * Revision 1.10  2006/06/19 08:04:23  김승희
 * 현재 시각 DataMap에서 구해오는 것으로 변경
 *
 * Revision 1.9  2006/06/17 10:12:09  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class SystemValueKeywordPool{
	
	 protected Map keywordMap;
	 
	 //FIXED 키워드의 상수값
	 public static final String FIXED_KEYWORD = "_$FIXED";
	 
//	MAP 키워드의 상수값
	 public static final String MAP_KEYWORD = "_$MAP";
	 
	 protected SystemValueKeywordPool(){
		 keywordMap = new HashMap();
	 }
	 
	 //멤버변수 읽어서keywordMap에 담는다.
	 protected void load(){ }
	 
	 public class SystemValue{
	     
		/**
	      * 시스템 지정 값을 리턴한다.
	      * @param dataMap
	      * @return 시스템 지정 값
	      */
		 public String getValue(DataMap dataMap){
			 return null;
		 }
		 
		 protected SystemValue(String keyword, int keywordValue){
			 this.keyword = keyword;
			 this.keywordValue = keywordValue;
		 }
		 protected int keywordValue;
		 protected String keyword;
	 }
	 
	 public SystemValue get(Object keyword){
		 if(keyword.toString().startsWith(FIXED_KEYWORD)){
			 
			 return (SystemValue)keywordMap.get(FIXED_KEYWORD);
		 }else
			 return (SystemValue)keywordMap.get(keyword);
	 }
	 
	 public boolean contains(Object keyword){
		 if(keyword==null) return false;
		 //키워드가 _$FIXED로 시작할 경우..
		 else if(keyword.toString().startsWith(FIXED_KEYWORD)
				 || keyword.toString().startsWith(MAP_KEYWORD)){
			 return true;			 
		 }else
			 return keywordMap.containsKey(keyword);
		 
	 }

}
