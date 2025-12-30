/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nebsoa.common.log.LogManager;
import nebsoa.common.property.PropertyItem;
import nebsoa.common.property.PropertyLoader;
import nebsoa.common.util.PropertyManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * File 에 대한 Event 관리를 하는 Listener 클래스
 * 
 * 
 * 
 * 2.사용법
 * 
 * <font color="red">
 * 3.주의사항
 * </font>
 *
 * @author $Author: cvs $
 * @version
 *******************************************************************
 * - 변경이력 (버전/변경일시/작성자)
 * 
 * $Log: FileListener.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:31  cvs
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
 * Revision 1.1  2008/08/04 08:54:52  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:26  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:26  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class FileListener {
	
	private static FileListener INSTANCE = new FileListener();
	
	private static final String PROPERTY_FILE = "fileListener";
	
	private Map targetList = new HashMap();
	
	public static FileListener getInstance() {
		return INSTANCE;
	}//end of getInstance()
	
	private FileListener() {
		LogManager.info("\t##### FileListener 를 초기화 합니다.");
		init();
		LogManager.info("\t##### FileListener 초기화를 완료 하였습니다.");
	}//end of constructor
	
	public void init() {
		loadFileList();
	}//end of init()
	
	public void loadFileList() {
		this.targetList.clear();
		
        PropertyLoader prop = PropertyManager.getPropertyLoader(PROPERTY_FILE);
        
		LogManager.info("\t##### fileListener 프로퍼티 파일을 로드합니다.");
				
		Iterator iter = prop.getPropertyCache().keySet().iterator();
		while (iter.hasNext()) {
            String propKey = (String) iter.next();
			PropertyItem propertyItem = prop.getPropertyItem(propKey);
			
			/*
			 * value 는 다음과 같은 형식을 가집니다.
			 * 
			 * value0||value1||value2
			 * 
			 * value0 : listening-dir or file
			 * value1 : FileListener 구현 클래스
			 * value2 : recursive 여부(true / false) - 재귀적으로 하위 디렉토리까지 검사할 것인지 여부
			 */
			
			String key = propertyItem.getKey();
			String [] values = StringUtil.split(propertyItem.getValue(), "\\|\\|");
			File value = new File(values[0]);
			
			if (!value.exists()) {
				LogManager.error("해당 파일이 존재하지 않습니다.[" + value.getAbsolutePath() + "]");
				continue;
			}//end if
			
			LogManager.info("\t##### 다음 파일을 listen 합니다.[" + value.getName() + "]");
			
			FileStatusChangeListener listener = null;
			try {
				Class clazz = Class.forName(values[1]);
				listener = (FileStatusChangeListener)clazz.newInstance();
			} catch (Exception e) {
				LogManager.error("해당 리스너 객체를 생성할 수 없습니다.[" + values[1] + "]");
				continue;
			}//end try catch
			
			LogManager.info("\t##### 리스너 객체를 생성하였습니다.[" + values[1] + "]");
			
			Map itemMap = new HashMap();
			
			if (value.isDirectory()) {
				File [] items = value.listFiles();
				for (int i = 0; i < items.length; i++) {
					File item = items[i];
					
					if (item.isFile()) {
						itemMap.put(item.getAbsolutePath(), new TargetItem(item.length(), item.lastModified(), item));
					} else {
						recursiveParse(itemMap, item);
					}//end if else
				}//end for
			} else {				
				itemMap.put(value.getName(), new TargetItem(value.length(), value.lastModified(), value));
			}//end if else
			
			this.targetList.put(key, new Target(value, itemMap, listener, Boolean.valueOf(values[2]).booleanValue()));
		}//end for
		
	}//end of loadFileList()
	
	private void recursiveParse(Map itemMap, File dir) {
		File [] items = dir.listFiles();
		for (int i = 0; i < items.length; i++) {
			File item = items[i];
			
			if (item.isFile()) {
				itemMap.put(item.getAbsolutePath(), new TargetItem(item.length(), item.lastModified(), item));
			} else {
				recursiveParse(itemMap, item);
			}//end if else
		}//end for
	}//end of recursiveParse()
	
	public void listen(String targetName) {
		Target target = (Target)this.targetList.get(targetName);
		
		File files = target.getTargetFile();
		Map items = target.getFileList();
		
		if (target.isFile()) {
			if (items.containsKey(files.getName()) && changed(files, (TargetItem)items.get(files.getName()))) {
				FileStatusChangeEvent event = new FileStatusChangeEvent(files, new FileStatusChangeEventMember[] {new FileStatusChangeEventMember(files, FileStatusChangeEvent.FILE_MODIFIED)});
				target.getListener().fileStatusChanged(event);
			}//end if
		} else {
			Map map = getFileMap(files);
			List list = new ArrayList();
			
			for (Iterator iter3 = items.keySet().iterator(); iter3.hasNext(); ) {
				Object key1 = iter3.next();
				if (!map.containsKey(key1)) {
					File file = ((TargetItem)items.get(key1)).getFile();
					list.add(new FileStatusChangeEventMember(file, FileStatusChangeEvent.FILE_DELETE));
					iter3.remove();
				}//end if
			}//end for
			
			for (Iterator iter2 = map.keySet().iterator(); iter2.hasNext(); ) {
				String fileName = (String)iter2.next();
				File file = (File)map.get(fileName);
				if (items.containsKey(fileName)) {
					if (changed(file, (TargetItem)items.get(fileName))) {
						list.add(new FileStatusChangeEventMember(file, FileStatusChangeEvent.FILE_MODIFIED));
						TargetItem item = (TargetItem)items.get(fileName);
						item.setLastModified(file.lastModified());
						item.setFileSize(file.length());
					}//end if
				} else {
					list.add(new FileStatusChangeEventMember(file, FileStatusChangeEvent.FILE_ADD));
					items.put(file.getAbsolutePath(), new TargetItem(file.length(), file.lastModified(), file));
				}//end if
			}//end for
			if (list.size() > 0) {
				FileStatusChangeEventMember [] member = new FileStatusChangeEventMember [list.size()];
				list.toArray(member);
				target.getListener().fileStatusChanged(new FileStatusChangeEvent(files, member));
			}//end if
		}//end if else
		
	}//end of targetName()
	
	public void listen() {		
		for (Iterator iter = this.targetList.keySet().iterator(); iter.hasNext(); ) {
			Object key = iter.next();
			Target target = (Target)this.targetList.get(key);
			
			File files = target.getTargetFile();
			Map items = target.getFileList();
			
			if (target.isFile()) {
				if (items.containsKey(files.getName()) && changed(files, (TargetItem)items.get(files.getName()))) {
					FileStatusChangeEvent event = new FileStatusChangeEvent(files, new FileStatusChangeEventMember[] {new FileStatusChangeEventMember(files, FileStatusChangeEvent.FILE_MODIFIED)});
					target.getListener().fileStatusChanged(event);
				}//end if
			} else {
				Map map = getFileMap(files);
				List list = new ArrayList();
				
				for (Iterator iter3 = items.keySet().iterator(); iter3.hasNext(); ) {
					Object key1 = iter3.next();
					if (!map.containsKey(key1)) {
						File file = ((TargetItem)items.get(key1)).getFile();
						list.add(new FileStatusChangeEventMember(file, FileStatusChangeEvent.FILE_DELETE));
						iter3.remove();
					}//end if
				}//end for
				
				for (Iterator iter2 = map.keySet().iterator(); iter2.hasNext(); ) {
					String fileName = (String)iter2.next();
					File file = (File)map.get(fileName);
					if (items.containsKey(fileName)) {
						if (changed(file, (TargetItem)items.get(fileName))) {
							list.add(new FileStatusChangeEventMember(file, FileStatusChangeEvent.FILE_MODIFIED));
						}//end if
					} else {
						list.add(new FileStatusChangeEventMember(file, FileStatusChangeEvent.FILE_ADD));
						items.put(file.getAbsolutePath(), new TargetItem(file.length(), file.lastModified(), file));
					}//end if
				}//end for
				if (list.size() > 0) {
					FileStatusChangeEventMember [] member = new FileStatusChangeEventMember [list.size()];
					list.toArray(member);
					target.getListener().fileStatusChanged(new FileStatusChangeEvent(files, member));
				}//end if
			}//end if else
			
		}//end for
		
	}//end of listen()
	
	protected boolean changed(File file, TargetItem item) {
		if (file.lastModified() == item.getLastModified() && file.length() == item.getFileSize()) {
			return false;
		} else {
			return true;
		}//end if else
	}//end of changed()
	
	protected Map getFileMap(File files) {
		Map map = new HashMap();
		File [] fileList = files.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isFile()) {
				map.put(file.getAbsolutePath(), file);
			} else {
				getFileMap(map, file);
			}//end if else
		}//end for
		return map;
	}//end of getFileMap()
	
	private void getFileMap(Map fileMap, File files) {
		File [] fileList = files.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isFile()) {
				fileMap.put(file.getAbsolutePath(), file);
			} else {
				getFileMap(fileMap, file);
			}//end if else
		}//end for		
	}//end of getFileMap()

}// end of FileListener.java