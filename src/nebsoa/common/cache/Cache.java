/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.cache;

import nebsoa.common.exception.ReloadFailException;
import nebsoa.common.exception.SysException;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 캐쉬메니져에서 객체 생성후 추가적인 작업이 있다면
 * 그 작업을 할 수 있도록 콜백 메소드를 지원하기 위한 클래스
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
 * $Log: Cache.java,v $
 * Revision 1.1  2018/01/15 03:39:49  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:29  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:26  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:51  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:23  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:37:38  안경아
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class Cache {
    String name;

    boolean isReloaderable;

    boolean isAutoReloader;

    int reloadTerm;

    Object data;

    public Cache(String name) {
        this(name, false, false, 0);
    }

    public Cache(String name, boolean isReloaderable) {
        this(name, isReloaderable, false, 0);
    }

    public Cache(String name, boolean isReloaderable, boolean isAutoReloader,
            int reloadTerm) {
        this.name = name;
        this.isReloaderable = isReloaderable;
        this.isAutoReloader = isAutoReloader;
        this.reloadTerm = reloadTerm;
    }

    /**
	 * 리로딩 하는 로직을 여기를 상속 받아 구현 합니다.
	 * 
	 * @throws SysException
	 */
	public Object reload() throws ReloadFailException {
		throw new ReloadFailException(name + " Cache is not reloaderable.");
	}

    public void destroy() {
        CacheManager.getInstance().remove(name);
    }

    public boolean isAutoReloader() {
        return isAutoReloader;
    }

    public void setAutoReloader(boolean isAutoReloader) {
        this.isAutoReloader = isAutoReloader;
    }

    public boolean isReloaderable() {
        return isReloaderable;
    }

    public void setReloaderable(boolean isReloaderable) {
        this.isReloaderable = isReloaderable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReloadTerm() {
        return reloadTerm;
    }

    public void setReloadTerm(int reloadTerm) {
        this.reloadTerm = reloadTerm;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
