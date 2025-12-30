/*
 * Spider Framework
 * 
 * Copyright (c) 2006-2007 ServerSide Corp. All Rights Reserved.
 * 
 * 본 소스 및 바이너리 파일에 대한 권한은 모두 ServerSide 에 있습니다.
 * 저작자와의 협의 없이 수정 및 무단 배포를 금합니다.
 */

package nebsoa.common.validator;

import nebsoa.common.exception.InvalidException;
import nebsoa.common.log.LogManager;
import nebsoa.common.util.StringUtil;

/*******************************************************************
 * <pre>
 * 1.설명 
 * 주민등록번호를 검증하기 위한 클래스
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
 * $Log: PsnValidator.java,v $
 * Revision 1.1  2018/01/15 03:39:53  cvs
 * *** empty log message ***
 *
 * Revision 1.1  2016/04/15 02:23:32  cvs
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
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.4  2007/01/17 04:13:05  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/12 09:29:49  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2006/07/05 13:46:24  오재훈
 * 수정
 *
 * Revision 1.1  2006/06/26 08:10:18  오재훈
 * PsnValidator 이름 변경
 *
 * Revision 1.2  2006/06/17 10:37:45  김성균
 * *** empty log message ***
 *
 * </pre>
 ******************************************************************/
public class PsnValidator{
	/* (non-Javadoc)
     * @see nebsoa.web.common.base.Command#execute()
     */
	public static void validate(String data) throws InvalidException {

		/*
		if (!StringUtil.isNull(data)) {
// 주민번호 기본 밸리데이션 로직 구현
			LogManager.debug("주민번호 Validator 입니다..");
			int dataSize = data.length();
			if(data.length()!=13) throw new InvalidException(data);
			
			int lastInt = 0;
			int resultHap = 0;
			int j=2;
			for(int i=0; i<13; i++)
			{
				String delStr = data.substring(i,(i+1));
				resultHap += Integer.parseInt(delStr)*j;
				j++;
				if(i==11) lastInt = Integer.parseInt(delStr);
				if(j==10) j=2;
			}
			resultHap = (11-(resultHap%11))%10;
			if(lastInt!=resultHap)
			{
				throw new InvalidException(data);
			}
		}
		*/
		
		if (!StringUtil.isNull(data)) {
// 주민번호 기본 밸리데이션 로직 구현
			LogManager.debug("주민번호 Validator 입니다..");
			int dataSize = data.length();
			if(data.length()!=13) throw new InvalidException(data);

			String psnSumCheckArr = new String("234567892345");
			int psnSum = 0;
			int psnSumResult = 0;
			for (int i = 0; i < 12; i++)
			{
				psnSum = psnSum + (Integer.parseInt( data.substring(i, i+1) ) * Integer.parseInt(psnSumCheckArr.substring(i, i+1)) );
			}
			psnSumResult = ( 11 - (psnSum % 11)) % 10;
	
			if( psnSumResult != Integer.parseInt(data.substring(12, 13)) )
			{
				throw new InvalidException(data);
			}
		}
	}
	
	/* (non-Javadoc)
     * @see nebsoa.web.common.base.Command#execute()
     */
	public static void validate(String key, String data) throws InvalidException {

		/*
		if (!StringUtil.isNull(data)) {
// 주민번호 기본 밸리데이션 로직 구현
			LogManager.debug("주민번호 Validator 입니다..");
			int dataSize = data.length();
			if(data.length()!=13) throw new InvalidException(data);
			
			int lastInt = 0;
			int resultHap = 0;
			int j=2;
			for(int i=0; i<13; i++)
			{
				String delStr = data.substring(i,(i+1));
				resultHap += Integer.parseInt(delStr)*j;
				j++;
				if(i==11) lastInt = Integer.parseInt(delStr);
				if(j==10) j=2;
			}
			resultHap = (11-(resultHap%11))%10;
			if(lastInt!=resultHap)
			{
				throw new InvalidException(data);
			}
		}
		*/
		
		if (!StringUtil.isNull(data)) {
// 주민번호 기본 밸리데이션 로직 구현
			LogManager.debug("주민번호 Validator 입니다..");
			int dataSize = data.length();
			if(data.length()!=13) throw new InvalidException(data);

			String psnSumCheckArr = new String("234567892345");
			int psnSum = 0;
			int psnSumResult = 0;
			for (int i = 0; i < 12; i++)
			{
				psnSum = psnSum + (Integer.parseInt( data.substring(i, i+1) ) * Integer.parseInt(psnSumCheckArr.substring(i, i+1)) );
			}
			psnSumResult = ( 11 - (psnSum % 11)) % 10;
	
			if( psnSumResult != Integer.parseInt(data.substring(12, 13)) )
			{
				throw new InvalidException(key, data);
			}
		}
	}

}
