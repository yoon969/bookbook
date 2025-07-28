package dev.mvc.reviewer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewerVO {
//  reviewerno NUMBER(10) NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
//  reviewerid         VARCHAR(30)   NOT NULL UNIQUE, -- 이메일(아이디), 중복 안됨, 레코드를 구분 
//  reviewerpasswd     VARCHAR(200)   NOT NULL, -- 패스워드, 영숫자 조합, 암호화
//  reviewername      VARCHAR(30)   NOT NULL, -- 성명, 한글 10자 저장 가능
//  phonenumber            VARCHAR(14)   NOT NULL, -- 전화번호
//  housecode     VARCHAR(5)        NULL, -- 우편번호, 12345
//  address1    VARCHAR(80)       NULL, -- 주소 1
//  address2    VARCHAR(50)       NULL, -- 주소 2
//  rdate       DATE             NOT NULL, -- 가입일    
//  grade        NUMBER(2)     NOT NULL, -- 등급(1~10: 관리자, 11~20: 회원, 40~49: 정지 회원, 99: 탈퇴 회원)
//  PRIMARY KEY (reviewerno)               -- 한번 등록된 값은 중복 안됨

  /** 회원 번호 */
  private int reviewerno;

  /** 이메일(아이디), 중복 안됨 */
  private String reviewerid ="";

  /** 패스워드, 암호화 예정 */
  private String reviewerpasswd="";

  /** 성명 */
  private String reviewername="";

  /** 전화번호 */
  private String phonenumber="";

  /** 우편번호 */
  private String housecode="";

  /** 주소 1 */
  private String address1="";

  /** 주소 2 */
  private String address2="";

  /** 가입일 */
  private String rdate=""; // java.sql.Date 또는 java.time.LocalDate로 바꿔도 됨

  /** 회원 등급 */
  private int grade = 0;

  /** 등록된 패스워드 */
  private String old_passwd = "";
  /** id 저장 여부 */
  private String id_save = "";
  /** passwd 저장 여부 */
  private String passwd_save = "";
  /** 이동할 주소 저장 */
  private String url_address = "";
}
