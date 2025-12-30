/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.engine.adapter;

import java.util.Vector;

import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.engine.Lifecycle;

/*******************************************************************
 * <pre>
 * 1.설명 
 * LifeCycle을 구현한 객체들의 자원 반환 호출을 담당한다.
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
 * $Log: AdapterManager.java,v $
 * Revision 1.1  2018/01/15 03:39:52  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:12  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:24  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:39:08  안경아
 * *** empty log message ***
 *
 * Revision 1.8  2006/09/25 11:08:04  이종원
 * add finalize
 *
 * Revision 1.7  2006/08/23 07:35:29  김승희
 * close시 에러날 경우 처리
 *
 * Revision 1.6  2006/08/04 09:09:07  김승희
 * closeAll 수정
 *
 * Revision 1.5  2006/08/01 14:06:54  이종원
 * *** empty log message ***
 *
 * Revision 1.4  2006/07/31 11:03:25  이종원
 * close추가
 *
 * Revision 1.3  2006/07/31 10:57:54  이종원
 * 기동록시 이미 등록 되어 있다는 내용 출력
 *
 * Revision 1.2  2006/07/04 12:51:58  김승희
 * 최초등록
 *
 *
 * </pre>
 ******************************************************************/
public class AdapterManager {
	
	private static AdapterManager instance = new AdapterManager();
	
	private Vector adapterTable;
	
	private AdapterManager(){
		adapterTable = new Vector();
	}
	
	public static AdapterManager getInstance(){
		return instance;
	}
    
    public boolean contains(Lifecycle lifecycleObject){
        return adapterTable.contains(lifecycleObject);
    }
	
	public void register(Lifecycle lifecycleObject){
        if(contains(lifecycleObject)){
            LogManager.debug(lifecycleObject +"가 이미 등록 되어 있습니다...");
            return;
        }
		LogManager.debug(lifecycleObject +"을 AdapterManager에 등록합니다..");
		adapterTable.add(lifecycleObject);
	}
	
	public void closeAll(){
		for(int i=0; i<adapterTable.size(); i++){
			Object obj= null;
			try{
				obj=adapterTable.get(i);
	            if(obj instanceof Lifecycle){
	    			LogManager.debug(obj +"를 destroy합니다..");
	    			((Lifecycle)obj).destroy();
	            }else{
	                LogManager.error(
	                        "AdapterManager에 Lifecycle type이 아닌 객체가 있습니다."
	                        +obj.getClass().getName());
	            }
            }catch(Throwable th){
            	 LogManager.error(obj +" destroy 실패", th);
            }
            
		}
		adapterTable.clear();
	}
    
    public void finalize(){
        closeAll();
    }
    
    public void close(Lifecycle lifecycleObject){
        if(!contains(lifecycleObject)){
            
            LogManager.error(lifecycleObject +"가 등록 되어 있지 않습니다..."
                    ,new Exception());
            return;
        }
        try{
            lifecycleObject.destroy();
            adapterTable.remove(lifecycleObject);
        }catch(Exception e){
           LogManager.error(e); 
        }            
    }
}
