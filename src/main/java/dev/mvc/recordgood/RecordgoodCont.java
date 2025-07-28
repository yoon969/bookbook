package dev.mvc.recordgood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.book.BookProcInter;
import dev.mvc.book.BookVOMenu;
import dev.mvc.calendar.CalendarVO;
import dev.mvc.record.RecordProcInter;
import dev.mvc.reviewer.ReviewerProcInter;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/recordgood")
public class RecordgoodCont {
  @Autowired
  @Qualifier("dev.mvc.reviewer.ReviewerProc") // @Service("dev.mvc.reviewer.ReviewerProc")
  private ReviewerProcInter reviewerProc;
  
  @Autowired
  @Qualifier("dev.mvc.book.BookProc") // @Component("dev.mvc.book.BookProc")
  private BookProcInter bookProc;
  
  @Autowired
  @Qualifier("dev.mvc.record.RecordProc") // @Component("dev.mvc.record.RecordProc")
  private RecordProcInter recordProc;
  
  @Autowired
  @Qualifier("dev.mvc.recordgood.RecordgoodProc") 
  private RecordgoodProcInter recordgoodProc;
  
  public RecordgoodCont() {
    System.out.println("-> RecordgoodCont created.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  
  @PostMapping(value="/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody RecordgoodVO recordgoodVO) {
    System.out.println("-> 수신 데이터:" + recordgoodVO.toString());
    
    int reviewerno = 1; // test 
    // int reviewerno = (int)session.getAttribute("reviewerno"); // 보안성 향상
    recordgoodVO.setReviewerno(reviewerno);
    
    int cnt = this.recordgoodProc.create(recordgoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();
  }
  
//  /**
//   * 목록
//   * 
//   * @param model
//   * @return
//   */
//  // http://localhost:9091/recordgood/list_all
//  @GetMapping(value = "/list_all")
//  public String list_all(Model model) {
//    ArrayList<RecordgoodVO> list = this.recordgoodProc.list_all();
//    model.addAttribute("list", list);
//
//    ArrayList<BookVOMenu> menu = this.bookProc.menu();
//    model.addAttribute("menu", menu);
//
//    return "recordgood/list_all"; // /templates/recordgood/list_all.html
//  }
  
  /**
   * 목록
   * 
   * @param model
   * @return
   */
  // http://localhost:9091/recordgood/list_all
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<RecordRecordgoodReviewerVO> list = this.recordgoodProc.list_all_join();
    model.addAttribute("list", list);

    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    return "recordgood/list_all_join"; // /templates/recordgood/list_all.html
  }
  
  /**
   * 삭제 처리 http://localhost:9091/recordgood/delete?calendarno=1
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="recordgoodno", defaultValue = "0") int recordgoodno, 
      RedirectAttributes ra) {    
    
    if (this.reviewerProc.isAdmin(session)) { // 관리자 로그인 확인
      // recordno 얻기
      RecordgoodVO vo = this.recordgoodProc.read(recordgoodno);
      int recordno = vo.getRecordno();
      
      this.recordgoodProc.delete(recordgoodno);       // 삭제
      this.recordProc.decreaseRecom(recordno); // 카운트 감소
      return "redirect:/recordgood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/reviewer/login_cookie_need"); // /templates/reviewer/login_cookie_need.html
      return "redirect:/recordgood/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
}







