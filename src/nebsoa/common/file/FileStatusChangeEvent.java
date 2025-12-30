/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.file;

import java.io.File;
import java.util.EventObject;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 파일 상태 변경에 대한 이벤트를 나타내는 클래스
 * 
 * 
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
 * $Log: FileStatusChangeEvent.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:31  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:26  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:26  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class FileStatusChangeEvent extends EventObject {
	
	/**
	 * 파일 추가 이벤트
	 */
	public static final int FILE_ADD 			= 0x001;
	/**
	 * 파일 삭제 이벤트
	 */
	public static final int FILE_DELETE 		= 0x010;
	/**
	 * 파일 수정 이벤트
	 */
	public static final int FILE_MODIFIED 	= 0x100;
	
	/**
	 * 파일 상태가 변경된 파일들의 리스트
	 */
	private FileStatusChangeEventMember [] eventMembers;

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8685092165111320484L;
	
	/**
	 * 이벤트에 대한 내용을 초기화 하는 생성자
	 * 
	 * @param eventSource 이벤트 발생 source
	 * @param eventMembers 파일 상태가 변경된 파일들의 리스트
	 */
	public FileStatusChangeEvent(File eventSource, FileStatusChangeEventMember [] eventMembers) {
		super(eventSource);
		this.eventMembers = eventMembers;
	}//end of constructor
	
	/**
	 * 발생한 event-member 들을 리턴합니다.
	 * 
	 * @return event-member
	 */
	public FileStatusChangeEventMember [] getEventMembers() {
		return this.eventMembers;
	}//end of getEventMembers()

}// end of FileStatusChangeEvent.java