package nebsoa.management.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nebsoa.common.batch.BatchManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.schedule.ScheduleManager;
import nebsoa.common.util.DataMap;

public class ExecutingBatchAgent {

    /**
     * 실행 중인 배치 목록을 가져옵니다.
     * @return
     */
	public static ArrayList getExecutingBatchList() {
		HashMap hash = (HashMap)BatchManager.getInstance().getExecuteBatchMap();
		ArrayList al = new ArrayList();

		Set set = hash.keySet();

		if (set.size() > 0) {

			Iterator it = set.iterator();
			String key="";
			
			while(it.hasNext()){
				al.add(it.next());
			}
		}
		
		return al;
	}

    /**
     * 실행중인 배치 프로그램의 정지 플래그를 셋팅합니다.
     * @param map
     * @return
     */
	public static DataMap doStopBatch(DataMap map) {

		String key = map.getString("batchAppId");
		BatchManager.getInstance().forceStop(key);
		map.put("batchAppIdStatie","STOP");
		return map;
	}
	
	
	/**
	 * 배치 Reload
	 *
	 */
	public static void doReload(){
		LogManager.debug("######## ScheduleManager를 Reload 합니다. #########");
		//1. 스케쥴 매니저 
		ScheduleManager.getInstance().refresh();
		LogManager.debug("######## BatchManager를 Reload 합니다. #########");
		//2. 배치
		BatchManager.getInstance().reload();
		LogManager.debug("######## Reload를 완료하였습니다. #########");
	}
	

	/**
	 * DataMap에 있는 BATCH_ID,STD_DATE,EXECUTE_USER_ID,PROPERTY 를 가져와서
	 * 수동 실행
	 * @param map
	 */
	public static void doForceStart( DataMap map ) {
		
		BatchManager.getInstance().forceStart(map);
	}
	
	
}
