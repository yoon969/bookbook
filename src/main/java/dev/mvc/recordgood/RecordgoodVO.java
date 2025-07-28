package dev.mvc.recordgood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE recordgood (
//    recordgoodno    NUMBER(10)        NOT NULL,
//    rdate            DATE        NOT NULL,
//    recordno        NUMBER(10)        NOT NULL,
//    reviewerno        NUMBER(10)        NOT NULL,
//    PRIMARY KEY (recordgoodno),
//    FOREIGN KEY (recordno) REFERENCES record(recordno),
//    FOREIGN KEY (reviewerno) REFERENCES reviewer(reviewerno)
//);

@Getter @Setter @ToString
public class RecordgoodVO {
  /** 컨텐츠 추천 번호 */
  private int recordgoodno;
  
  /** 등록일 */
  private String rdate;
  
  /** 컨텐츠 번호 */
  private int recordno;
  
  /** 회원 번호 */
  private int reviewerno;
  
}



