/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package test.monitor.dashboard.data.gc.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import nebsoa.monitor.dashboard.data.gc.impl.WebsphereGCDataCollecter;

/*******************************************************************
 * <pre>
 * 1.설명 
 * Websphere  메모리 GC정보를 수집하는 클래스 
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
 * $Log: WebsphereGCDataCollecterTest.java,v $
 * Revision 1.1  2018/01/15 03:39:54  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:36  cvs
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.2  2008/03/04 02:05:19  이종원
 * 기존파일백업로직 추가
 *
 * Revision 1.1  2008/03/04 01:57:31  이종원
 * 최초작성
 *
 * </pre>
 ******************************************************************/
public class WebsphereGCDataCollecterTest implements Runnable{
	static String filePath = "c:\\temp\\websphere_test.log";
	public static void main(String[] args){
		
		new Thread(new WebsphereGCDataCollecterTest(5)).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new WebsphereGCDataCollecter("CA11", filePath)).start();
		
	}
	
	public WebsphereGCDataCollecterTest(int count){
		this.count = count;
	}
	int count=0;
	public void run() {
		String s =  "<af type=\"tenured\" id=\"9\" timestamp=\"월  3월 03 16:35:29 2008\" intervalms=\"4736670.915\">"
			+"\r\n  <minimum requested_bytes=\"24\" />"
			+"\r\n  <time exclusiveaccessms=\"0.147\" />"
			+"\r\n  <tenured freebytes=\"10737152\" totalbytes=\"1073741824\" percent=\"0\" >"
			+"\r\n    <soa freebytes=\"0\" totalbytes=\"1063004672\" percent=\"0\" />"
			+"\r\n    <loa freebytes=\"10737152\" totalbytes=\"10737152\" percent=\"100\" />"
			+"\r\n  </tenured>"
			+"\r\n  <gc type=\"global\" id=\"9\" totalid=\"9\" intervalms=\"4736671.460\">"
			+"\r\n    <refs_cleared soft=\"275\" threshold=\"32\" weak=\"9876\" phantom=\"8\" />"
			+"\r\n    <finalization objectsqueued=\"2459\" />"
			+"\r\n    <timesms mark=\"320.480\" sweep=\"26.946\" compact=\"0.000\" total=\"347.813\" />"
			+"\r\n    <tenured freebytes=\"954740104\" totalbytes=\"1073741824\" percent=\"88\" >"
			+"\r\n      <soa freebytes=\"945076616\" totalbytes=\"1064078336\" percent=\"88\" />"
			+"\r\n      <loa freebytes=\"9663488\" totalbytes=\"9663488\" percent=\"100\" />"
			+"\r\n    </tenured>"
			+"\r\n  </gc>"
			+"\r\n  <tenured freebytes=\"954739064\" totalbytes=\"1073741824\" percent=\"88\" >"
			+"\r\n    <soa freebytes=\"945075576\" totalbytes=\"1064078336\" percent=\"88\" />"
			+"\r\n    <loa freebytes=\"9663488\" totalbytes=\"9663488\" percent=\"100\" />"
			+"\r\n  </tenured>"
			+"\r\n  <time totalms=\"348.502\" />" 
			+"\r\n	</af>";
		int i=0;
		FileWriter out = null;
		File f = new File(filePath);
		if(f.exists()) f.renameTo(new File( f.getParent(), f.getName()+"_bak"));
		try {
			out = new FileWriter(filePath,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(i++ < count){
			try {
				out.write(s);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Write ok..........");
			try {
				Thread.sleep(5*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}