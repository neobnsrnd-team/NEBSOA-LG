/*
 * Spider Framework
 *
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 *
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.jdbc;

public interface TxService {
	final static int TX_NONE=0;
	final static int TX_BEGIN=1;
	final static int TX_COMMIT=2;
	final static int TX_ROLLBACK=3;
	final static int TX_END=4;
	final static int TX_COMMIT_FAIL=5;
	final static int TX_ROLLBACK_FAIL=6;


	public void begin();

	public void commit();

	public void rollback();

	public void end();

	public boolean isValid();


}
