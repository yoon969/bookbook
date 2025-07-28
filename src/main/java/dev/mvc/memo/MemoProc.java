package dev.mvc.memo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("dev.mvc.memo.MemoProc")
public class MemoProc implements MemoProcInter {

  @Autowired
  private MemoDAOInter memoDAO;
  
  @Override
  public int create(MemoVO memoVO) {
    int cnt = this.memoDAO.create(memoVO);
    return cnt;
  }

  @Override
  public ArrayList<MemoVO> list_all() {
    ArrayList<MemoVO> list = this.memoDAO.list_all();
    return list;
  }

  @Override
  public MemoVO read(int memono) {
    MemoVO memoVO = this.memoDAO.read(memono);
    return memoVO;
  }

  @Override
  public int update(MemoVO memoVO) {
    int cnt = this.memoDAO.update(memoVO);
    return cnt;
  }

  @Override
  public int delete(int memono) {
    int cnt = this.memoDAO.delete(memono);
    return cnt;
  }
  
  @Override
  public ArrayList<MemoVO> list_by_word_paging(String word, int offset, int limit) {
      return memoDAO.list_by_word_paging(word, offset, limit);
  }

  @Override
  public int count_by_word(String word) {
      return memoDAO.count_by_word(word);
  }
}