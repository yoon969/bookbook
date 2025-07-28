package dev.mvc.recordgood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.recordgood.RecordgoodProc")
public class RecordgoodProc implements RecordgoodProcInter {
  @Autowired
  RecordgoodDAOInter recordgoodDAO;
  
  @Override
  public int create(RecordgoodVO recordgoodVO) {
    int cnt = this.recordgoodDAO.create(recordgoodVO);
    return cnt;
  }

  @Override
  public ArrayList<RecordgoodVO> list_all() {
    ArrayList<RecordgoodVO> list = this.recordgoodDAO.list_all();
    return list;
  }


  @Override
  public RecordgoodVO read(int recordgoodno) {
    RecordgoodVO recordgoodVO = this.recordgoodDAO.read(recordgoodno);
    return recordgoodVO;
  }
  
  @Override
  public int delete(int recordgoodno) {
    int cnt = this.recordgoodDAO.delete(recordgoodno);
    return cnt;
  }

  @Override
  public int hartCnt(HashMap<String, Object> map) {
    int cnt = this.recordgoodDAO.hartCnt(map);
    return cnt;
  }


  @Override
  public RecordgoodVO readByRecordnoReviewerno(HashMap<String, Object> map) {
    RecordgoodVO recordgoodVO = this.recordgoodDAO.readByRecordnoReviewerno(map);
    return recordgoodVO;
  }

  @Override
  public ArrayList<RecordRecordgoodReviewerVO> list_all_join() {
    ArrayList<RecordRecordgoodReviewerVO> list = this.recordgoodDAO.list_all_join();
    return list;
  }

}



