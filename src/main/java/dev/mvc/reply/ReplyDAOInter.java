package dev.mvc.reply;

import java.util.List;

public interface ReplyDAOInter {
  /**
   * 댓글 등록
   * @param replyVO
   * @return 등록된 레코드 수
   */
  public int create(ReplyVO replyVO);

  /**
   * 전체 댓글 목록
   * @return 댓글 목록
   */
  public List<ReplyVO> list();

  /**
   * 특정 콘텐츠에 대한 댓글 목록
   * @param recordno 콘텐츠 번호
   * @return 댓글 목록
   */
  public List<ReplyVO> list_by_recordno(int recordno);

  /**
   * 특정 댓글 조회
   * @param replyno 댓글 번호
   * @return 댓글 정보
   */
  public ReplyVO read(int replyno);

  /**
   * 댓글 수정
   * @param replyVO
   * @return 수정된 레코드 수
   */
  public int update(ReplyVO replyVO);

  /**
   * 댓글 삭제
   * @param replyno 댓글 번호
   * @return 삭제된 레코드 수
   */
  public int delete(int replyno);
  
  public int delete_by_recordno(int recordno);
}
