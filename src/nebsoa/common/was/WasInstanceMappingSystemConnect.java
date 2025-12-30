package nebsoa.common.was;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

public class WasInstanceMappingSystemConnect {

	public static DataMap execute(DataMap map) throws Exception {
		Socket sock = null;
		String ip = map.getParameter("IP");
		int port = Integer.parseInt(map.getParameter("PORT"));
    	try {
			sock = new Socket (ip,port);
			map.put("result", ip+":"+port+"에 연결 되었습니다.");
		} catch (UnknownHostException e) {
			map.put("result", ip+":"+port+"에 연결 되지 않았습니다.");
		} catch (IOException e) {
			map.put("result", ip+":"+port+"에 연결 되지 않았습니다.");
		}finally{
			if(sock != null){
				sock.close();
			}
		}
		LogManager.debug("### connect 결과 메시지 = " + map.getString("result"));
		return map;
	}

}
