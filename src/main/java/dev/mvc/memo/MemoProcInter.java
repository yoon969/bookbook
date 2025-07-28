package dev.mvc.memo;

import java.util.ArrayList;

public interface MemoProcInter {

  /**
   * <pre>
   * MyBATIS: <insert id="create" parameterType="dev.mvc.memo.MemoVO">
   * insert: INSERT SQL 처리 후 등록된 레코드 갯수를 리턴
   * id: 자바 메소드명
   * parameterType: MyBATIS가 전달받는 VO 객체 타입
   * </pre>
   * @param memoVO
   * @return 등록된 레코드 갯수
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
  
  ArrayList<MemoVO> list_by_word_paging(String word, int offset, int limit);
  int count_by_word(String word);

}