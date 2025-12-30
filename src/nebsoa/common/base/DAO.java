/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.base;

import nebsoa.common.exception.InvalidException;
import nebsoa.common.jdbc.Null;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * DAO 클래스 작성시 상속받아야 되는 클래스
 * 기본적인 데이타 변환 메소드를 제공한다.
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
 * $Log: DAO.java,v $
 * Revision 1.1  2018/01/15 03:39:50  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:22:39  cvs
 * neo cvs init
 *
 * Revision 1.1  2011/07/01 02:13:51  yshong
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:27:23  김성균
 * *** empty log message ***
 *
 * Revision 1.1  2008/11/18 11:01:27  김성균
 * LGT Gateway를 위한 프로젝트로 분리
 *
 * Revision 1.1  2008/08/04 08:54:55  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:32  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:35  안경아
 * *** empty log message ***
 *
 * Revision 1.13  2006/11/16 07:57:20  이종원
 * 품질검증완료
 *
 * Revision 1.12  2006/09/20 01:58:12  최수종
 * getPageNum() 비교 조건 수정
 *
 * Revision 1.11  2006/06/19 04:10:16  안경아
 * 최초등록
 *
 * </pre>
 ******************************************************************/
public class DAO  {

    protected DAO(){}

	/**
	 * 설명:PreparedStatement로 사용될 파라미터 객체를 생성
	 * 
	 * @param str
	 * @return 널이 아니면 Integer, 널이면 serverside.common.jdbc.Null
	 */
	protected static Object toShort(String str) {
		if (StringUtil.isNull(str)) {
			return Null.NUMBER;
		} else {
			return Short.valueOf(str);
		}
	}

	/**
	 * 설명:PreparedStatement로 사용될 파라미터 객체를 생성
	 * 
	 * @param str
	 * @return 널이 아니면 Integer, 널이면 serverside.common.jdbc.Null
	 */
	protected static Object toInteger(String str) {
		if (StringUtil.isNull(str)) {
			return Null.NUMBER;
		} else {
			return Integer.valueOf(str);
		}
	}

	/**
	 * 설명:PreparedStatement로 사용될 파라미터 객체를 생성
	 * 
	 * @param str
	 * @return 널이 아니면 Long, 널이면 serverside.common.jdbc.Null
	 */
	protected static Object toLong(String str) {
		if (StringUtil.isNull(str)) {
			return Null.NUMBER;
		} else {
			return Long.valueOf(str);
		}
	}

	/**
	 * 설명:PreparedStatement로 사용될 파라미터 객체를 생성
	 * 
	 * @param str
	 * @return 널이 아니면 Double, 널이면 serverside.common.jdbc.Null
	 */
	protected static Object toFloat(String str) {
		if (StringUtil.isNull(str)) {
			return Null.NUMBER;
		} else {
			return Float.valueOf(str);
		}
	}

	/**
	 * 설명:PreparedStatement로 사용될 파라미터 객체를 생성
	 * 
	 * @param str
	 * @return 널이 아니면 Double, 널이면 serverside.common.jdbc.Null
	 */
	protected static Object toDouble(String str) {
		if (StringUtil.isNull(str)) {
			return Null.NUMBER;
		} else {
			return Double.valueOf(str);
		}
	}

	/**
	 * 설명:PreparedStatement로 사용될 파라미터 객체를 생성
	 * 
	 * @param str
	 * @return 널이 아니면 Integer 널이면 serverside.common.jdbc.Null
	 */
	protected static Object toDate(String str) throws InvalidException {
		if (StringUtil.isNull(str)) {
			return Null.DATE;
		} else {
			int firstDash = str.indexOf('-');
			int secondDash = str.indexOf('-', firstDash + 1);
			if ((firstDash > 0) && (secondDash > 0)
					&& (secondDash < str.length() - 1)) {
				return java.sql.Date.valueOf(str);
			} else if ((firstDash == -1) && (str.length() == 8)) {
                String tempStr = str.substring(0, 4) + "-" + str.substring(4, 6) + "-"
						+ str.substring(6);
				return java.sql.Date.valueOf(tempStr);
			} else {
				throw new InvalidException("날짜(Date)-" + str);
			}
		}
	}
	
	/**
	 * 현재 페이지가 총 페이지보다 크다면, 현재 페이지값에 
	 * 마지막 페이지값을 셋팅한다.
	 * 
	 * @param totalCount 전체 row 수
	 * @param page 현재 페이지 번호
	 * @param displayCount 한 페이지에 보여질 row 수
	 * @return
	 */
	public static int getPageNum(int totalCount, int page, int displayCount)
	{
	    final int totalPage = (int)(Math.ceil((totalCount/(double)displayCount)));  // 총 페이지 번호
	    int pageNum=page;
	    if(page > totalPage && totalPage > 0)  // 만약, 현재 페이지가 총 페이지 보다 크다면 현재 페이지값에 마지막 페이지값을 셋팅.
	    {
            pageNum = totalPage;
	    }
	    return pageNum;
	}

}
