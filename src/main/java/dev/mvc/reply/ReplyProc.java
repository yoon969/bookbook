package dev.mvc.reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.reply.ReplyProc")
public class ReplyProc implements ReplyProcInter {
  @Autowired
  private ReplyDAOInter replyDAO;

  /**
   * 댓글 등록
   */
  @Override
  public int create(ReplyVO replyVO) {
    return replyDAO.create(replyVO);
  }

  /**
   * 전체 댓글 목록
   */
  @Override
  public List<ReplyVO> list() {
    return replyDAO.list();
  }

  /**
   * 특정 콘텐츠의 댓글 목록
   */
  @Override
  public List<ReplyVO> list_by_recordno(int recordno) {
    return replyDAO.list_by_recordno(recordno);
  }

  /**
   * 댓글 단건 조회
   */
  @Override
  public ReplyVO read(int replyno) {
    return replyDAO.read(replyno);
  }

  /**
   * 댓글 수정
   */
  @Override
  public int update(ReplyVO replyVO) {
    return replyDAO.update(replyVO);
  }

  /**
   * 댓글 삭제
   */
  @Override
  public int delete(int replyno) {
    return replyDAO.delete(replyno);
  }
  
  @Override
  public int delete_by_recordno(int recordno) {
      return replyDAO.delete_by_recordno(recordno);
  }
}
