/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.base;

import java.io.Serializable;

import nebsoa.common.util.DataMap;

/*******************************************************************************
 * <pre>
 *  1.설명 
 *  execute(DataMap map)의 메소드를 필요로 하는 모든 클래스가 
 *  재사용성을 극대화 하기 위해 상속받아야 되는 최상위 클래스입니다.
 *  
 *  2.사용법
 *  개발자는 아래의 메소드에 비즈니스 로직을 구현합니다.
 *  abstract public DataMap execute(DataMap map) throws Exception;
 *  
 *  &lt;font color=&quot;red&quot;&gt;
 *  3.주의사항
 *  &lt;/font&gt;
 * 
 *  @author 이종원
 *  @version
 * ******************************************************************
 *  - 변경이력 (버전/변경일시/작성자)
 *  
 *  $Log: Processor.java,v $
 *  Revision 1.1  2018/01/15 03:39:50  cvs
 *  *** empty log message ***
 *
 *  Revision 1.1  2016/04/15 02:22:39  cvs
 *  neo cvs init
 *
 *  Revision 1.1  2011/07/01 02:13:51  yshong
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:27:24  김성균
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/11/18 11:01:27  김성균
 *  LGT Gateway를 위한 프로젝트로 분리
 *
 *  Revision 1.1  2008/08/04 08:54:55  youngseokkim
 *  *** empty log message ***
 *
 *  Revision 1.1  2008/01/22 05:58:32  오재훈
 *  패키지 리펙토링
 *
 *  Revision 1.1  2007/11/26 08:38:36  안경아
 *  *** empty log message ***
 *
 *  Revision 1.1  2007/02/01 12:21:49  이종원
 *  최초작성
 *
 *  Revision 1.1  2007/02/01 12:07:46  이종원
 *  최초작성
 *
 * </pre>
 ******************************************************************************/
public interface Processor extends Serializable{

    /**
     * <pre>
     *  &lt;font color=red&gt; 비즈니스 로직을 처리하는 메소드이며,
     *  상속받은 클래스에서 구현해야 할 메소드이다.
     *  &lt;/font&gt;
     * </pre>
     * 
     * @param map
     * @throws Exception
     */
    abstract public DataMap doProcess(DataMap map) throws Exception;
}
