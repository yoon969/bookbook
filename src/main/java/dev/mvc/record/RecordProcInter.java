package dev.mvc.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 개발자가 구현합니다.
 * @author soldesk
 *
 */
public interface RecordProcInter {
  /**
   * 등록
   * @param recordVO
   * @return
   */
  public int create(RecordVO recordVO);

  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<RecordVO> list_all();
 
  /**
   * 카테고리별 등록된 글 목록
   * @param bookno
   * @return
   */
  public ArrayList<RecordVO> list_by_bookno(int bookno);
  
  /**
   * 조회
   * @param recordno
   * @return
   */
  public RecordVO read(int recordno);
  
  /**
   * map 등록, 수정, 삭제
   * @param map
   * @return 수정된 레코드 갯수
   */
  public int map(HashMap<String, Object> map);

  /**
   * youtube 등록, 수정, 삭제
   * @param youtube
   * @return 수정된 레코드 갯수
   */
  public int youtube(HashMap<String, Object> map);
  
  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<RecordVO> list_by_bookno_search(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_bookno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param recordVO
   * @return
   */
  public ArrayList<RecordVO> list_by_bookno_search_paging(HashMap<String, Object> map);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param bookno 카테고리 번호
   * @param now_page 현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int bookno, int now_page, String word, String list_file, int search_count, 
                                      int record_per_page, int page_per_block);   

  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
  
  /**
   * 글 정보 수정
   * @param recordVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(RecordVO recordVO);
  
  /**
   * 파일 정보 수정
   * @param recordVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(RecordVO recordVO);
  
  /**
   * 삭제
   * @param recordno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int recordno);
  
  /**
   * FK bookno 값이 같은 레코드 갯수 산출
   * @param bookno
   * @return
   */
  public int count_by_bookno(int bookno);
  
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param bookno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_bookno(int bookno);

  /**
   * FK reviewerno 값이 같은 레코드 갯수 산출
   * @param reviewerno
   * @return
   */
  public int count_by_reviewerno(int reviewerno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param reviewerno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_reviewerno(int reviewerno);
  
  /**
   * 글 수 증가
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int recordno);
 
  /**
   * 글 수 감소
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int recordno);
  
  public int update_cnt_up(int bookno);
  
  public int update_cnt_down(int bookno);

  public int update_grp_cnt_up(String author);

  public int update_grp_cnt_down(String author);
  
  public int increaseRecom(int recordno); 
  
  public int decreaseRecom(int recordno); 
}