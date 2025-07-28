package dev.mvc.book;

import java.util.ArrayList;
import java.util.Map;

public interface BookDAOInter {

  public int create(BookVO bookVO);
  
  public ArrayList<BookVO> list_all();
  
  /**
   * 검색, 전체 목록
   * @return
   */
  public ArrayList<BookVO> list_search(String word);

  /**
   * 검색, 전체 레코드 갯수, 페이징 버튼 생성시 필요 ★★★★★
   * @return
   */
  public int list_search_count(String word);
  
  /**
   * 조회
   * @param bookno
   * @return
   */
  public BookVO read(int bookno);
  
  public int update(BookVO bookVO);
  
  public int delete(int bookno);
  
  /**
   * 우선 순위 높임, 10 등 -> 1 등
   * @param int
   * @return
   */
  public int update_seqno_forward(int bookno);

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등
   * @param int
   * @return
   */
  public int update_seqno_backward(int bookno);
  
  /**
   * 카테고리 공개 설정
   * @param int
   * @return
   */
  public int update_visible_y(int bookno);

  /**
   * 카테고리 비공개 설정
   * @param int
   * @return
   */
  public int update_visible_n(int bookno);

  /**
   * 공개된 대분류만 출력
   * @return
   */
  public ArrayList<BookVO> list_all_author_y();
  
  /**
   * 특정 그룹의 중분류 출력
   * @return
   */
  public ArrayList<BookVO> list_all_title_y(String author);

  /**
   * 카테고리 그룹 목록
   * @return
   */
  public ArrayList<String> authorset();

  /**
   * 검색, 전체 목록
   * @return
   */
  public ArrayList<BookVO> list_search_paging(Map map);
  
//특정 그룹(bookno)에 속한 레코드 수
  public int countByBookno(int bookno);

  // 특정 관리자(reviewerno)에 속한 레코드 수
  public int countByReviewerno(int reviewerno);

  // 특정 그룹(bookno)에 속한 레코드 삭제
  public int deleteByBookno(int bookno);

  // 특정 관리자(reviewerno)에 속한 레코드 삭제
  public int deleteByReviewerno(int reviewerno);
  
  public int update_author_cnt_down(Map<String, Object> params);
}
