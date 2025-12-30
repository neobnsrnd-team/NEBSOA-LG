package test.biz;

import nebsoa.biz.base.SpiderBaseTestCase;
import nebsoa.biz.client.RPCClient;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

public class RPCTest extends SpiderBaseTestCase {
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
        setUserInfo();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Test method for {@link test.account.AccountListBiz#execute(nebsoa.common.util.DataMap)}.
	 */
	public void testExecute() throws Exception {
        DataMap map = new DataMap();
        map.put("이거","그거");
		map = RPCClient.doRPC("test", "test.biz.RPCTestTarget","go",map);
        LogManager.debug("받은 맵 데이터 : "+map);
        
        map.put("이거","아까 그거");
        map = RPCClient.doRPC("default", "nebsoa.biz.ejb.RPCTestTarget","go",map);
        LogManager.debug("받은 맵 데이터 : \n"+map);
	}

}
