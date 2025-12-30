/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.file;

import nebsoa.common.schedule.JobContext;
import nebsoa.common.schedule.SimpleJob;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 파일 리스너를 동작시키기 위한 클래스
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
 * $Log: FileListenerJob.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:30  cvs
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
public class FileListenerJob extends SimpleJob {
	
	private FileListener listener = FileListener.getInstance();

	/**
	 * 파일 리스너를 동작시킵니다.
	 * 
	 * @param context 스케줄 실행에 대한 설정이 포함된 JobContext 객체
	 * @see nebsoa.common.schedule.SimpleJob#execute(nebsoa.common.schedule.JobContext)
	 */
	public void execute(JobContext context) {
		listener.listen(context.getJobName());
	}//end of execute()

}// end of FileListenerJob.java