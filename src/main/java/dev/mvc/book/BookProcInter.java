package dev.mvc.book;

import java.util.ArrayList;
import java.util.Map;

import dev.mvc.book.BookVO;

public interface BookProcInter {

  /**
   * <pre>
   * MyBATIS: <insert id="create" parameterType="dev.mvc.cate.CateVO">
   * insert: INSERT SQL 처리후 등록된 레코드 갯수를 리턴
   * id: 자바 메소드명
   * parameterType: MyBATIS가 전달받는 VO객체 타입
   * </pre>
   * @param cateVO
   * @return 등록된 레코드 갯수
   */
  public int create(BookVO bookVO);
  
  public ArrayList<BookVO> list_all();
  
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
   * @param cateVO
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
   * 화면 상단 메뉴
   * @return
   */
  public ArrayList<BookVOMenu> menu();

  /**
   * 카테고리 그룹 목록
   * @return
   */
  public ArrayList<String> authorset();

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
   * 검색 + 페이징 목록
   * select id="list_search_paging" resultType="dev.mvc.cate.CateVO" parameterType="Map" 
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호: 1 ★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<BookVO> list_search_paging(String word, int now_page, int record_per_page);

  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_url 페이지 버튼 클릭시 이동할 주소, @GetMapping(value="/list_search") 
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, String word, String list_url, int search_count, int record_per_page,
      int page_per_block);
  
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