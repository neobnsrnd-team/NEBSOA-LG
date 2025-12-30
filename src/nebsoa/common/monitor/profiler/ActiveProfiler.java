/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.monitor.profiler;

import java.io.Serializable;


/*******************************************************************
 * <pre>
 * 1.설명 
 * Profile을 수행하는 클래스
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
 * $Log: ActiveProfiler.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:22  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:27  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:21  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2006/07/10 04:59:12  김성균
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/21 05:57:50  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class ActiveProfiler extends NullProfiler implements Serializable {
	private Profiler profiler;
	public ActiveProfiler(Profile profile) {
		this.profiler=profiler;
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
