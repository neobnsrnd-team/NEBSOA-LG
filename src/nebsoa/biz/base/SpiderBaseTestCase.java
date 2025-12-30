package nebsoa.biz.base;

import junit.framework.TestCase;
import nebsoa.common.Context;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;
import nebsoa.spiderlink.context.MessageContext;
import nebsoa.spiderlink.engine.MessageEngine;
import nebsoa.spiderlink.engine.message.MessageField;
import nebsoa.spiderlink.engine.message.MessageStructure;
import nebsoa.spiderlink.engine.message.MessageStructurePool;
import nebsoa.spiderlink.engine.message.MessageStructureUtil;

public abstract class SpiderBaseTestCase extends TestCase {
	
	protected DataMap map = new DataMap();
    
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		map.setContext(new Context());
	}
	
	protected void setTestData(String trxId, String orgId, String ioType) throws Exception {
		LogManager.debug("### 1. setTestData 시작 ###");
	
		MessageContext messageContext = MessageEngine.getMessageContext(trxId, orgId, ioType);		
		String structureId = MessageStructureUtil.getMessageStructureId(messageContext.getMessageId(), messageContext.getOrgId());
		LogManager.debug("### 2. structureId = "+structureId);
		MessageStructure srcStructure = MessageStructurePool.getInstance().getMessageStructure(structureId);
		LogManager.debug("### 3. constructTestValueDataMap 시작 ###");
		constructTestValueDataMap(srcStructure);

	}
	
	private void constructTestValueDataMap(MessageStructure messageStructure){

		if(messageStructure.getParent()!=null){
			constructTestValueDataMap(messageStructure.getParent());
		}
		int size = messageStructure.size();

		MessageField messageField = null;
		
		for(int i=0; i<size; i++){
			messageField = messageStructure.getField(i);
			if(messageField!=null){
				if(messageField.getTestValue() != null && !messageField.getTestValue().equals(""))
					map.put(messageField.getName(), messageField.getTestValue());
			}
		}
		
	}	

	/**
	 * Test 할 사용자 지정
	 * 또는 전문에 공통으로 필요한 값이 있을경우 지정.
	 */
	protected void setUserInfo() {
//		map.put("헤더고객구분", "1");
//		map.put("전자금융ID", "SCHLOAN01");
//		map.put("주민사업자번호", "84092420177281");		
	}

    /**
     * @return Returns the map.
     */
    public DataMap getDataMap() {
        return map;
    }

    /**
     * @param map The map to set.
     */
    public void setDataMap(DataMap map) {
        this.map = map;
    }

}
