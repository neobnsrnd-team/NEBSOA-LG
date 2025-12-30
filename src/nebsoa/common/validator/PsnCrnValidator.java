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
 * 주민 , 사업자등록번호를 검증하기 위한 클래스
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
 * $Log: PsnCrnValidator.java,v $
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
 * Revision 1.1  2008/08/04 08:54:56  youngseokkim
 * *** empty log message ***
 *
 * Revision 1.1  2008/01/22 05:58:35  오재훈
 * 패키지 리펙토링
 *
 * Revision 1.1  2007/11/26 08:38:56  안경아
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/17 04:13:05  안경아
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/12 09:29:49  안경아
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/26 21:08:40  오재훈
 * 주민 사업자번호 validator
 *
 *
 * </pre>
 ******************************************************************/
public class PsnCrnValidator {
	/* (non-Javadoc)
     * @see nebsoa.web.base.Command#execute()
     */
	public static void validate(String data) throws InvalidException {
		
		if (!StringUtil.isNull(data)) {

			if(data.length()==10)
			{
				CrnValidator.validate(data);
			}else if(data.length()==13)
			{
				PsnValidator.validate(data);
			}else 
				throw new InvalidException(data);


		}
	}
	/* (non-Javadoc)
     * @see nebsoa.web.base.Command#execute()
     */
	public static void validate(String key, String data) throws InvalidException {
		
		if (!StringUtil.isNull(data)) {

			if(data.length()==10)
			{
				CrnValidator.validate(key, data);
			}else if(data.length()==13)
			{
				PsnValidator.validate(key, data);
			}else 
				throw new InvalidException(key, data);


		}
	}

}
