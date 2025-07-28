package dev.mvc.reply;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dev.mvc.record.RecordProcInter;
import dev.mvc.record.RecordVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reply")
public class ReplyCont {

  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc")
  private ReplyProcInter replyProc;
  
  @Autowired
  @Qualifier("dev.mvc.record.RecordProc") // ★ record 댓글 수 처리용
  private RecordProcInter recordProc;

  public ReplyCont() {
    System.out.println("-> ReplyCont created.");
  }

  @PostMapping("/create")
  public String create(HttpSession session, ReplyVO replyVO, 
                       @RequestParam("recordno") int recordno) {
    
    Integer reviewerno = (Integer) session.getAttribute("reviewerno"); // 세션에서 로그인 사용자 번호 꺼냄
    if (reviewerno == null) {
      return "redirect:/reviewer/login_cookie_need";
    }

    replyVO.setReviewerno(reviewerno); // 댓글 작성자 번호 설정
    replyProc.create(replyVO);
    recordProc.increaseReplycnt(recordno);

    return "redirect:/record/read?recordno=" + recordno;
  }

  /**
   * 댓글 목록 (특정 콘텐츠용)
   */
  @GetMapping("/list_by_recordno")
  public String list_by_recordno(@RequestParam("recordno") int recordno, Model model) {
    List<ReplyVO> list = replyProc.list_by_recordno(recordno);
    model.addAttribute("list", list);
    return "/reply/list_by_recordno"; // 예: reply/list_by_recordno.html (Thymeleaf)
  }

  /**
   * 댓글 수정 처리
   */
  @PostMapping("/update")
  public String update(ReplyVO replyVO, @RequestParam("recordno") int recordno) {
    replyProc.update(replyVO);
    return "redirect:/record/read?recordno=" + recordno;
  }

  /**
   * 댓글 삭제 처리
   */
  @GetMapping("/delete")
  public String delete(@RequestParam("replyno") int replyno, @RequestParam("recordno") int recordno) {
    replyProc.delete(replyno);
    recordProc.decreaseReplycnt(recordno); // 댓글 수 감소
    return "redirect:/record/read?recordno=" + recordno;
  }
  
  @GetMapping("/record/read")
  public String read(Model model, 
                     @RequestParam("recordno") int recordno) {

    RecordVO recordVO = recordProc.read(recordno);
    model.addAttribute("recordVO", recordVO);

    List<ReplyVO> list = replyProc.list_by_recordno(recordno);
    model.addAttribute("list", list); // 댓글 목록

    return "/record/read";
  }
}
