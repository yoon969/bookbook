package dev.mvc.reviewer;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

public interface ReviewerProcInter {

  /**
   * 중복 아이디 검사
   * @param reviewerid
   * @return 중복된 아이디 개수
   */
  public int checkID(String reviewerid);

  /**
   * 회원 가입
   * @param reviewerVO
   * @return 생성된 레코드 수
   */
  public int create(ReviewerVO reviewerVO);

  /**
   * 전체 리뷰어 목록
   * @return 리뷰어 목록
   */
  public ArrayList<ReviewerVO> list();

  /**
   * 리뷰어 번호로 조회
   * @param reviewerno
   * @return ReviewerVO
   */
  public ReviewerVO read(int reviewerno);

  /**
   * 아이디로 리뷰어 조회
   * @param reviewerid
   * @return ReviewerVO
   */
  public ReviewerVO readById(String reviewerid);

  /**
   * 로그인된 일반 리뷰어인지 확인
   * @param session
   * @return true: 일반 사용자
   */
  public boolean isReviewer(HttpSession session);

  /**
   * 로그인된 관리자 리뷰어인지 확인
   * @param session
   * @return true: 관리자
   */
  public boolean isAdmin(HttpSession session);

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
   * 현재 비밀번호 일치 여부 확인
   * @param map (reviewerno, reviewerpasswd)
   * @return 0: 불일치, 1: 일치
   */
  public int passwd_check(HashMap<String, Object> map);

  /**
   * 비밀번호 변경
   * @param map (reviewerno, reviewerpasswd)
   * @return 변경된 레코드 수
   */
  public int passwd_update(HashMap<String, Object> map);

  /**
   * 로그인 처리
   * @param map (reviewerid, reviewerpasswd)
   * @return 0: 실패, 1: 성공
   */
  public int login(HashMap<String, Object> map);
}
