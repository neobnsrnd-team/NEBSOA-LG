/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */
package nebsoa.common.cache.policy;

import org.shiftone.cache.policy.lfu.LfuCache;
/**
 * LFU 알고리즘을 구현한 Cache
 * @author 이종원
 */
public class LFUCache extends LfuCache {

    public LFUCache(String name, long timeout, int size) {
        super(name, timeout, size);
    }
}
