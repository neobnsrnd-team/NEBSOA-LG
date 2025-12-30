/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.monitor.dashboard.data.gc.impl;

import nebsoa.common.log.LogManager;
import nebsoa.monitor.dashboard.data.gc.GCDataCollecter;
import nebsoa.monitor.data.MonitorDataVo;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Websphere  메모리 GC정보를 수집하는 클래스.
 * GCDataCollecter를 상속 받아  parseLine만 overriding하였다.
 * 
 * 2.사용법
 * 웹스피어의 gc log는 아래 포멧이다.
 * 
 <af type="tenured" id="9" timestamp="월  3월 03 16:35:29 2008" intervalms="4736670.915">
  <minimum requested_bytes="24" />
  <time exclusiveaccessms="0.147" />
  <tenured freebytes="10737152" totalbytes="1073741824" percent="0" >
    <soa freebytes="0" totalbytes="1063004672" percent="0" />
    <loa freebytes="10737152" totalbytes="10737152" percent="100" />
  </tenured>
  <gc type="global" id="9" totalid="9" intervalms="4736671.460">
    <refs_cleared soft="275" threshold="32" weak="9876" phantom="8" />
    <finalization objectsqueued="2459" />
    <timesms mark="320.480" sweep="26.946" compact="0.000" total="347.813" />
    <tenured freebytes="954740104" totalbytes="1073741824" percent="88" >
      <soa freebytes="945076616" totalbytes="1064078336" percent="88" />
      <loa freebytes="9663488" totalbytes="9663488" percent="100" />
    </tenured>
  </gc>
  <tenured freebytes="954739064" totalbytes="1073741824" percent="88" >
    <soa freebytes="945075576" totalbytes="1064078336" percent="88" />
    <loa freebytes="9663488" totalbytes="9663488" percent="100" />
  </tenured>
  <time totalms="348.502" />
</af>
 * 
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author 이종원
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: WebsphereGCDataCollecter.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:39  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:27  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:29  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:53  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/18 01:11:09  김성균
 * MonitorDataVo 생성자 변경
 *
 * Revision 1.1  2008/03/04 01:57:31  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class WebsphereGCDataCollecter extends GCDataCollecter{
	MonitorDataVo vo;
	boolean startLogLine;
	boolean afterGcTag=false;
	
	
	public WebsphereGCDataCollecter(String wasInstanceId,String filePath){
		super(wasInstanceId,filePath);
	}
	/**
	 * 라인 단위로 파싱하는 method
	 */
	public void parseLine(String line) {
		if(line.indexOf("intervalms")>-1){
			startLogLine=true;
			if(vo==null) vo = new MonitorDataVo(wasInstanceId, "GC");
			//interval을 구한다.
			String interval = getAttrValue(line, "intervalms");
			if(interval != null){
				if(interval.indexOf(".")>-1){
					interval = interval.substring(0,interval.indexOf("."));
				}
				vo.put("intervalms",interval);
			}
		//<soa 태그가 두번 나오므로 뒤어 나온 것을 찾기 위해 flag설정
		}else if(line.indexOf("</gc>")>-1 ){	
			afterGcTag=true;
		}else if(line.indexOf("<soa") > -1 && afterGcTag){
			//<soa freebytes="945075576" totalbytes="1064078336" percent="88" />
			String freebytes = getAttrValue(line, "freebytes");
			String totalbytes = getAttrValue(line, "totalbytes");
			String percent = getAttrValue(line, "percent");
			
			if(freebytes != null && vo!= null) vo.put("freebytes",freebytes);
			if(totalbytes != null && vo!= null) vo.put("totalbytes",totalbytes);
			if(percent != null && vo!= null) vo.put("percent",percent);			
			
			//수집된 정보를 저장한다.
			saveVoInfo(vo);
			
			//clear variables...
			vo=null;
			startLogLine=false;
			afterGcTag=false;
		}
		
	}
	
	/**
	 * 수집된 정보를 저장하는 로직
	 * @param vo2
	 */
	private void saveVoInfo(MonitorDataVo vo2) {
		//do nothing ...
		LogManager.info("VO Info::"+vo);
		
	}

	/**
	 * a="b" 형식의 문자열에서 b를 잘라내가 위한 함수.
	 * @param src
	 * @param attrName
	 * @return
	 */
	private String getAttrValue(String src, String attrName){
		if(src==null || attrName==null || src.length() <= attrName.length()+3 ) return null;
		src = src.replaceAll("= \"", "=\"");
		src = src.replaceAll("= '", "=\"");
		src = src.replaceAll("='", "=\"");
		
		//LogManager.debug("Line:"+src+"\n Find : "+attrName);
		
		int beginIndex= src.indexOf(attrName+"=\"");
		if(beginIndex == -1){
			LogManager.debug("begin index is -1 : not match the attribute:"+attrName);
			return null;
		}else{
			beginIndex = beginIndex+attrName.length()+2;
		}
		int endIndex = src.indexOf("\"",beginIndex+1);
		if(endIndex ==-1 ){
			LogManager.debug("end index is -1 : not match the attribute:"+attrName);
			return null;
		}
		LogManager.debug("DataFound: "+src.substring(beginIndex, endIndex));
		return src.substring(beginIndex, endIndex);
	}

	
	

}//end of ManagementInterface.java