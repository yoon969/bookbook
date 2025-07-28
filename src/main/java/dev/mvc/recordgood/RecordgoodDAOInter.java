package dev.mvc.recordgood;

import java.util.ArrayList;
import java.util.HashMap;

public interface RecordgoodDAOInter {
  /**
   * 등록, 추상 메소드
   * @param recordgoodVO
   * @return
   */
  public int create(RecordgoodVO recordgoodVO);
  
  /**
   * 모든 목록
   * @return
   */
  public ArrayList<RecordgoodVO> list_all();
  
  /**
   * 삭제
   * @param recordgoodno
   * @return
   */
  public int delete(int recordgoodno);
  
  /**
   * 특정 컨텐츠의 특정 회원 추천 갯수 산출
   * @param map
   * @return
   */
  public int hartCnt(HashMap<String, Object> map);  

  /**
   * 조회
   * @param recordgoodno
   * @return
   */
  public RecordgoodVO read(int recordgoodno);

  /**
   * recordno, memberno로 조회
   * @param map
   * @return
   */
  public RecordgoodVO readByRecordnoReviewerno(HashMap<String, Object> map);
  
  /**
   * 모든 목록, 테이블 3개 join
   * @return
   */
  public ArrayList<RecordRecordgoodReviewerVO> list_all_join();
  
}




