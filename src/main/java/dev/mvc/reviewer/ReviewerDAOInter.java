package dev.mvc.reviewer;

import java.util.ArrayList;
import java.util.HashMap;

public interface ReviewerDAOInter {
  
  /**
   * 중복 아이디 검사
   * @param reviewerid
   * @return 중복 아이디 갯수
   */
  public int checkID(String reviewerid);
  
  /**
   * 회원 가입
   * @param reviewerVO
   * @return 추가된 레코드 수
   */
  public int create(ReviewerVO reviewerVO);

  /**
   * 전체 회원 목록
   * @return
   */
  public ArrayList<ReviewerVO> list();

  /**
   * reviewerno로 회원 정보 조회
   * @param reviewerno
   * @return
   */
  public ReviewerVO read(int reviewerno);
  
  /**
   * reviewerid로 회원 정보 조회
   * @param reviewerid
   * @return
   */
  public ReviewerVO readById(String reviewerid);

  /**
   * 회원 정보 수정
   * @param reviewerVO
   * @return 수정된 레코드 수
   */
  public int update(ReviewerVO reviewerVO);
 
  /**
   * 회원 삭제
   * @param reviewerno
   * @return 삭제된 레코드 수
   */
  public int delete(int reviewerno);
  
  /**
   * 현재 패스워드 검사
   * @param map
   * @return 0: 불일치, 1: 일치
   */
  public int passwd_check(HashMap<String, Object> map);
  
  /**
   * 패스워드 변경
   * @param map
   * @return 변경된 레코드 수
   */
  public int passwd_update(HashMap<String, Object> map);
  
  /**
   * 로그인 처리
   * @param map (reviewerid, reviewerpasswd)
   * @return 로그인 성공 여부 (1: 성공, 0: 실패)
   */
  public int login(HashMap<String, Object> map);
}
