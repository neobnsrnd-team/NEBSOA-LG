/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.spiderlink.connector.listener.worker;

import nebsoa.common.util.ConfigMap;
import nebsoa.util.workerpool.Worker;
import nebsoa.util.workerpool.WorkerFactory;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Worker를 생성하는 factory이며 org.apache.commons.pool.PoolableObjectFactory를
 * wrapping하여 사용한다.
 * 
 * 2.사용법
 * WorkerFactory factory = new WorkerFactory(ip,port,timeout,"DEBUG");
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
 * $Log: InputMessageWorkerFactory.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:38  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:34  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/03 09:12:44  이종원
 * refactoring
 *
 * Revision 1.1  2006/10/02 19:48:56  이종원
 * 기본기능 구현
 *
 * Revision 1.1  2006/10/02 15:41:40  이종원
 * 최초작성
 *
 * Revision 1.1  2006/10/02 15:40:48  이종원
 * 기본 기능 구현
 *
 * </pre>
 ******************************************************************/
public class InputMessageWorkerFactory extends WorkerFactory {
    ConfigMap config;
    public InputMessageWorkerFactory(ConfigMap config){
        this.config = config;
    }
    
    public Worker createWorker() {
        
        InputMessageWorker worker = new InputMessageWorker();
        return worker;
    }

}