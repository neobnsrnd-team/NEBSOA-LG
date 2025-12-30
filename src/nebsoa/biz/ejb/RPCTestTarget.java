package nebsoa.biz.ejb;

import nebsoa.common.util.DataMap;


public class RPCTestTarget {
	
	public DataMap go(DataMap map){
	    System.out.println("rpc called.....");
        System.out.println(map);
        map.put("result","123456789");
        return map;
    }

}
