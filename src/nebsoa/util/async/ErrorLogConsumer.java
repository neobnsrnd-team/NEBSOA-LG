package nebsoa.util.async;

import nebsoa.biz.base.Biz;
import nebsoa.biz.base.ErrorManageBiz;
import nebsoa.biz.factory.BizFactory;
import nebsoa.biz.util.BizInfo;
import nebsoa.biz.util.BizManager;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.DataMap;

public class ErrorLogConsumer extends Consumer{

	public void handleObject(Object obj){
		LogManager.debug(" ############## [ErrorLogConsumer.handleObject start] #############");
		String bizId = "nebsoa.biz.base.ErrorManageBiz";
		try {
			BizInfo bizInfo = BizManager.getInstance().getBizInfo(bizId);
			Biz biz = BizFactory.getInstance().getBiz(bizInfo);

			biz.doProcess((DataMap)obj);
			
			//new ErrorManageBiz().execute((DataMap)obj);
		} catch(Exception e){
			LogManager.debug(" ############## [ErrorLogConsumer.Exception catch] #############");
			LogManager.debug(e);
		}
		LogManager.debug(" ############## [ErrorLogConsumer.handleObject end] ##############");		        
	}

}
