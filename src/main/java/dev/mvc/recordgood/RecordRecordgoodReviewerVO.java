package dev.mvc.recordgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//-- 테이블 3개 join
//SELECT r.contentsgoodno, r.rdate, r.contentsno, c.title as c_title, r.memberno, m.id, m.mname
//FROM contents c, contentsgood r, member m
//WHERE c.contentsno = r.contentsno AND r.memberno = m.memberno
//ORDER BY contentsgoodno DESC;

@Getter @Setter @ToString
public class RecordRecordgoodReviewerVO {
  /** 컨텐츠 추천 번호 */
  private int recordgoodno;
  
  /** 등록일 */
  private String rdate;
  
  /** 컨텐츠 번호 */
  private int recordno;
  
  /** 제목 */
  private String c_title = "";
  
  /** 회원 번호 */
  private int reviewerno;
  
  /** 아이디(이메일) */
  private String reviewerid = "";
  
  /** 회원 성명 */
  private String reviewername = "";
  
}



