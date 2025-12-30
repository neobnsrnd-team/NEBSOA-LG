package test.biz;

import nebsoa.biz.base.SpiderBaseTestCase;
import nebsoa.biz.client.BizClient;

public class AsyncTest extends SpiderBaseTestCase {
	
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
		BizClient.doBizAsyncProcess("account_list", map);
	}

}
