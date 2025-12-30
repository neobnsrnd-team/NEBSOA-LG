/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.connector.listener.util;

/*******************************************************************
 * <pre>
 * 1.설명 
 *  No Ack 리스너와 응답어댑터 간의 맵핑 클래스
 * 
 * 2.사용법
 * 생성자 참조.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: ListenerConnectorMapper.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:32  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:54  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/03/04 07:40:21  김승희
 * 최초 등록
 *
 *
 * </pre>
 ******************************************************************/
public abstract class ListenerConnectorMapper {
	
	/**
	 * sourceInformation으로 부터 맵핑 식별자를 구한다.
	 * 
	 * @param sourceInformation 맵핑 식별자를 얻어낼 정보
	 * @return 맵핑 식별자
	 */
	public abstract String getIndentifier(Object sourceInformation);
}
