package dev.mvc.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface RecordDAOInter {
  /**
   * 등록, 추상 메소드
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