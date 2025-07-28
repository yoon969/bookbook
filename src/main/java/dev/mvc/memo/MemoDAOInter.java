package dev.mvc.memo;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

public interface MemoDAOInter {
  
  /**
   * 메모 등록
   * @param memoVO
   * @return 등록된 메모의 갯수 (1이면 성공, 0이면 실패)
   */
  public int create(MemoVO memoVO);

  /**
   * 모든 메모 목록 조회
   * @return 등록된 모든 메모 목록
   */
  public ArrayList<MemoVO> list_all();
  
  /**
   * 메모 번호로 메모 조회
   * @param memono
   * @return 해당 번호의 메모
   */
  public MemoVO read(int memono);
  
  /**
   * 메모 수정
   * @param memoVO
   * @return 수정된 메모의 갯수 (1이면 성공, 0이면 실패)
   */
  public int update(MemoVO memoVO);
  
  /**
   * 메모 삭제
   * @param memono
   * @return 삭제된 메모의 갯수 (1이면 성공, 0이면 실패)
   */
  public int delete(int memono);
  
  ArrayList<MemoVO> list_by_word_paging(
      @Param("word") String word,
      @Param("offset") int offset,
      @Param("limit") int limit
    );
  int count_by_word(String word);
}