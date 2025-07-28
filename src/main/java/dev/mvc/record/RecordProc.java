package dev.mvc.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;

@Component("dev.mvc.record.RecordProc")
public class RecordProc implements RecordProcInter {
  @Autowired
  Security security;
  
  @Autowired // RecordDAOInter interface를 구현한 클래스의 객체를 만들어 자동으로 할당해라.
  private RecordDAOInter recordDAO;

  @Override // 추상 메소드를 구현했음.
  public int create(RecordVO recordVO) {
    // -------------------------------------------------------------------
    String passwd = recordVO.getPasswd();
    String passwd_encoded = this.security.aesEncode(passwd);
    recordVO.setPasswd(passwd_encoded);
    // -------------------------------------------------------------------
    
    int cnt = this.recordDAO.create(recordVO);
    return cnt;
  }

  @Override
  public ArrayList<RecordVO> list_all() {
    ArrayList<RecordVO> list = this.recordDAO.list_all();
    return list;
  }

  @Override
  public ArrayList<RecordVO> list_by_bookno(int bookno) {
    ArrayList<RecordVO> list = this.recordDAO.list_by_bookno(bookno);
    return list;
  }

  /**
   * 조회
   */
  @Override
  public RecordVO read(int recordno) {
    RecordVO recordVO = this.recordDAO.read(recordno);
    return recordVO;
  }

  @Override
  public int map(HashMap<String, Object> map) {
    int cnt = this.recordDAO.map(map);
    return cnt;
  }

  @Override
  public int youtube(HashMap<String, Object> map) {
    int cnt = this.recordDAO.youtube(map);
    return cnt;
  }

  @Override
  public ArrayList<RecordVO> list_by_bookno_search(HashMap<String, Object> hashMap) {
    ArrayList<RecordVO> list = this.recordDAO.list_by_bookno_search(hashMap);
    return list;
  }

  @Override
  public int list_by_bookno_search_count(HashMap<String, Object> hashMap) {
    int cnt = this.recordDAO.list_by_bookno_search_count(hashMap);
    return cnt;
  }

  @Override
  public ArrayList<RecordVO> list_by_bookno_search_paging(HashMap<String, Object> map) {
    /*
     * 예) 페이지당 10개의 레코드 출력 1 page: WHERE r >= 1 AND r <= 10 2 page: WHERE r >= 11
     * AND r <= 20 3 page: WHERE r >= 21 AND r <= 30
     * 
     * 페이지에서 출력할 시작 레코드 번호 계산 기준값, nowPage는 1부터 시작 1 페이지 시작 rownum: now_page = 1, (1
     * - 1) * 10 --> 0 2 페이지 시작 rownum: now_page = 2, (2 - 1) * 10 --> 10 3 페이지 시작
     * rownum: now_page = 3, (3 - 1) * 10 --> 20
     */
    int begin_of_page = ((int)map.get("now_page") - 1) * Record.RECORD_PER_PAGE;

    // 시작 rownum 결정
    // 1 페이지 = 0 + 1: 1
    // 2 페이지 = 10 + 1: 11
    // 3 페이지 = 20 + 1: 21
    int start_num = begin_of_page + 1;

    // 종료 rownum
    // 1 페이지 = 0 + 10: 10
    // 2 페이지 = 10 + 10: 20
    // 3 페이지 = 20 + 10: 30
    int end_num = begin_of_page + Record.RECORD_PER_PAGE;
    /*
     * 1 페이지: WHERE r >= 1 AND r <= 10 2 페이지: WHERE r >= 11 AND r <= 20 3 페이지: WHERE
     * r >= 21 AND r <= 30
     */

    // System.out.println("begin_of_page: " + begin_of_page);
    // System.out.println("WHERE r >= "+start_num+" AND r <= " + end_num);

    map.put("start_num", start_num);
    map.put("end_num", end_num);

    ArrayList<RecordVO> list = this.recordDAO.list_by_bookno_search_paging(map);

    return list;
  }

  /**
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 현재 페이지: 11 / 22 [이전] 11 12 13 14 15 16 17
   * 18 19 20 [다음]
   * 
   * @param now_page        현재 페이지
   * @param word            검색어
   * @param list_file       목록 파일명
   * @param search_count    검색 레코드수
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block  블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  @Override
  public String pagingBox(int bookno, int now_page, String word, String list_file, int search_count, int record_per_page,
      int page_per_block) {
    // 전체 페이지 수: (double)1/10 -> 0.1 -> 1 페이지, (double)12/10 -> 1.2 페이지 -> 2 페이지
    int total_page = (int) (Math.ceil((double) search_count / record_per_page));
    // 전체 그룹 수: (double)1/10 -> 0.1 -> 1 그룹, (double)12/10 -> 1.2 그룹-> 2 그룹
    int total_grp = (int) (Math.ceil((double) total_page / page_per_block));
    // 현재 그룹 번호: (double)13/10 -> 1.3 -> 2 그룹
    int now_grp = (int) (Math.ceil((double) now_page / page_per_block));

    // 1 group: 1, 2, 3 ... 9, 10
    // 2 group: 11, 12 ... 19, 20
    // 3 group: 21, 22 ... 29, 30
    int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지
    int end_page = (now_grp * page_per_block); // 특정 그룹의 마지막 페이지

    StringBuffer str = new StringBuffer(); // String class 보다 문자열 추가등의 편집시 속도가 빠름

    str.append("<DIV id='paging'>");
//    str.append("현재 페이지: " + nowPage + " / " + totalPage + "  "); 

    // 이전 10개 페이지로 이동
    // now_grp: 1 (1 ~ 10 page)
    // now_grp: 2 (11 ~ 20 page)
    // now_grp: 3 (21 ~ 30 page)
    // 현재 2그룹일 경우: (2 - 1) * 10 = 1그룹의 마지막 페이지 10
    // 현재 3그룹일 경우: (3 - 1) * 10 = 2그룹의 마지막 페이지 20
    int _now_page = (now_grp - 1) * page_per_block;
    if (now_grp >= 2) { // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성
      str.append("<span class='span_box_1'><A href='" + list_file + "?bookno="+bookno+"&word=" + word + "&now_page=" + _now_page
          + "'>이전</A></span>");
    }

    // 중앙의 페이지 목록
    for (int i = start_page; i <= end_page; i++) {
      if (i > total_page) { // 마지막 페이지를 넘어갔다면 페이 출력 종료
        break;
      }

      if (now_page == i) { // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
        str.append("<span class='span_box_2'>" + i + "</span>"); // 현재 페이지, 강조
      } else {
        // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
        str.append("<span class='span_box_1'><A href='" + list_file + "?bookno="+bookno+"&word=" + word + "&now_page=" + i + "'>" + i
            + "</A></span>");
      }
    }

    // 10개 다음 페이지로 이동
    // nowGrp: 1 (1 ~ 10 page), nowGrp: 2 (11 ~ 20 page), nowGrp: 3 (21 ~ 30 page)
    // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
    // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
    // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
    _now_page = (now_grp * page_per_block) + 1; // 최대 페이지수 + 1
    if (now_grp < total_grp) {
      str.append("<span class='span_box_1'><A href='" + list_file + "?bookno="+bookno+"&word=" + word + "&now_page=" + _now_page
          + "'>다음</A></span>");
    }
    str.append("</DIV>");

    return str.toString();
  }

  @Override
  public int password_check(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.recordDAO.password_check(map);
    return cnt;
  }

  @Override
  public int update_text(RecordVO recordVO) {
    int cnt = this.recordDAO.update_text(recordVO);
    return cnt;
  }

  @Override
  public int update_file(RecordVO recordVO) {
    int cnt = this.recordDAO.update_file(recordVO);
    return cnt;
  }

  @Override
  public int delete(int recordno) {
    int cnt = this.recordDAO.delete(recordno);
    return cnt;
  }

  @Override
  public int count_by_bookno(int bookno) {
    int cnt = this.recordDAO.count_by_bookno(bookno);
    return cnt;
  }

  @Override
  public int delete_by_bookno(int bookno) {
    int cnt = this.recordDAO.delete_by_bookno(bookno);
    return cnt;
  }

  @Override
  public int count_by_reviewerno(int reviewerno) {
    int cnt = this.recordDAO.count_by_reviewerno(reviewerno);
    return cnt;
  }

  @Override
  public int delete_by_reviewerno(int reviewerno) {
    int cnt = this.recordDAO.delete_by_reviewerno(reviewerno);
    return cnt;
  }

  @Override
  public int increaseReplycnt(int recordno) {
    int count = recordDAO.increaseReplycnt(recordno);
    return count;
  }

  @Override
  public int decreaseReplycnt(int recordno) {
    int count = recordDAO.decreaseReplycnt(recordno);
    return count;
  }
  
  @Override
  public int update_cnt_up(int bookno) {
    int count = recordDAO.update_cnt_up(bookno);
    return count;
  }

  @Override
  public int update_cnt_down(int bookno) {
    int count = recordDAO.update_cnt_down(bookno);
    return count;
  }

  @Override
  public int update_grp_cnt_up(String author) {
    int count = recordDAO.update_grp_cnt_up(author);
    return count;
  }

  @Override
  public int update_grp_cnt_down(String author) {
    int count = recordDAO.update_grp_cnt_down(author);
    return count;
  }
  
  @Override
  public int increaseRecom(int recordno) {
    int count = recordDAO.increaseRecom(recordno);
    return count;
  }

  @Override
  public int decreaseRecom(int recordno) {
    int count = recordDAO.decreaseRecom(recordno);
    return count;
  }
}