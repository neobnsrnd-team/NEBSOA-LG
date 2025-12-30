/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.spiderlink.queue;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import nebsoa.common.log.LogManager;
import nebsoa.spiderlink.context.MessageContext;

/*******************************************************************
 * <pre>
 * 1.설명
 * 현재 처리중인 메시지를 잠시 보관 해 두는 로직을  담당하는 클래스
 *
 * 2.사용법
 * 처리시작시 put하여 두고, 응답 받을 시 get하여 제거 한다.
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * $Log: ProcessingMessagePool.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:36  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.3  2009/07/16 03:22:46  김보라
 * ConcurrentHashMap 으로 수정
 *
 * Revision 1.2  2009/05/06 13:38:50  jwlee
 * java.util.ConcurrentModificationException error fetch
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
 * Revision 1.6  2008/07/09 14:18:39  김은정
 * 부하 테스트를 하면 java.util.ConcurrentModificationException  이 아래 문장에서 발생하여 주석 처리
 *
 * Revision 1.5  2008/03/24 00:06:39  오재훈
 * Queue에 넣을때 로그 남김.
 *
 * Revision 1.4  2008/01/30 09:21:37  이종원
 * 주석처리
 *
 * Revision 1.3  2008/01/25 07:38:35  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2008/01/23 09:30:46  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:33  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.2  2008/01/15 05:52:40  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2007/11/26 08:39:16  안경아
 * *** empty log message ***
 *
 * Revision 1.6  2007/07/15 13:45:41  김승희
 * 주석 수정
 *
 * Revision 1.5  2006/11/27 04:38:26  이종원
 * log update
 *
 * Revision 1.4  2006/10/11 02:01:50  이종원
 * remove(MessageContext ctx)추가
 *
 * Revision 1.3  2006/10/10 12:17:24  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/09/23 01:26:57  이종원
 * 변경없음
 *
 * Revision 1.1  2006/09/22 22:56:26  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class ProcessingMessagePool extends ConcurrentHashMap {
    static Object dummy=new Object();
    private static ProcessingMessagePool instance;
    private ProcessingMessagePool(){

    }
    public static ProcessingMessagePool getInstance(){
        if(instance==null){
            synchronized (dummy) {
                instance = new ProcessingMessagePool();
            }
        }
        return instance;
    }
    public void put(MessageContext ctx){
        try{ 
//        	LogManager.debug("Queue에 넣는다. UID: "+ctx.getUid()+",기관: "+ctx.getOrgId()+", this=["+getClass().getName() + '@' + hashCode()+"]");
            Object obj = super.put(ctx.getUid()+"",ctx);
            /**
             * 부하 테스트를 하면 java.util.ConcurrentModificationException  이 아래 문장에서 발생한다.
             */
            /***
            if(obj!=null){
            	//LogManager.debug("MSG_ERROR", "ProcessingMessagePool put return Object="+obj+", UID:["+ctx.getUid()+"]");
            }
            //이거 때문에 에러 난다.
            //LogManager.debug("MSG_ERROR","Queue에 넣은 후. UID: "+ctx.getUid()+",기관: "+ctx.getOrgId()+", this=["+getClass().getName() + '@' + hashCode()+"]"+this);
			***/
        }finally{
            //this.notifyAll();
            //LogManager.debug("NotifyAll thread....");
        }
    }

    public MessageContext get(String uid){
        MessageContext ctx= (MessageContext) super.get(uid);
        if(ctx != null){
            this.remove(uid);
        }
        return ctx;
    }

    public void remove(MessageContext ctx){
    	LogManager.debug("UID["+ctx.getUid()+"] remove..");
        if(ctx != null){
            this.remove(ctx.getUid()+"");
        }
    }
    
    
    public ConcurrentHashMap getPool() {
       	return this;
    }

    /**
     * test code.
     * 2006. 6. 29.  이종원 작성
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
    
    
}
