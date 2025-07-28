package dev.mvc.memo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.book.BookProcInter;
import dev.mvc.book.BookVOMenu;

@RequestMapping(value = "/memo")
@Controller
public class MemoCont {
  
  @Autowired
  @Qualifier("dev.mvc.book.BookProc") // @Component("dev.mvc.book.BookProc")
  private BookProcInter bookProc;

  @Autowired
  @Qualifier("dev.mvc.memo.MemoProc") // @Service("dev.mvc.memo.MemoProc")
  private MemoProcInter memoProc;

  public MemoCont() {
    System.out.println("-> MemoCont created.");
  }

  /**
   * 메모 등록 폼
   */
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("memoVO") MemoVO memoVO) {
    return "memo/create"; // /templates/memo/create.html
  }

  /**
   * 메모 등록 처리
   */
  @PostMapping(value = "/create")
  public String create(MemoVO memoVO, Model model) {
    int cnt = memoProc.create(memoVO); // 메모 등록
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);
    if (cnt == 1) {
      model.addAttribute("msg", "메모 등록 성공!");
      return "redirect:/memo/list_all"; // 메모 목록 페이지로 리다이렉트
    } else {
      model.addAttribute("msg", "메모 등록 실패!");
      return "memo/create"; // 등록 실패 시 등록 폼으로 돌아감
    }
  }

  @GetMapping(value = "/list_all")
  public String list(Model model,
                     @RequestParam(name="word", defaultValue="") String word,
                     @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
      int records_per_page = 8;
      int pages_per_block = 5;
    
      int offset = (now_page - 1) * records_per_page;
    
      ArrayList<MemoVO> list = memoProc.list_by_word_paging(word, offset, records_per_page);
      model.addAttribute("list", list);
    
      int totalCount = memoProc.count_by_word(word);
      int totalPage = (int)(Math.ceil((double)totalCount / records_per_page));
    
      int currentBlock = (int)Math.ceil((double)now_page / pages_per_block);
      int startPage = (currentBlock - 1) * pages_per_block + 1;
      int endPage = startPage + pages_per_block - 1;
      if (endPage > totalPage) {
          endPage = totalPage;
      }
    
      model.addAttribute("totalPage", totalPage);
      model.addAttribute("now_page", now_page);
      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      model.addAttribute("word", word);
    
      ArrayList<BookVOMenu> menu = this.bookProc.menu();
      model.addAttribute("menu", menu);
    
      return "memo/list_all";
  }
  
  
  /**
   * 메모 상세 조회
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
      @RequestParam(name="memono", defaultValue="0") int memono) {
    MemoVO memoVO = memoProc.read(memono); // 메모 상세 조회
    model.addAttribute("memoVO", memoVO);
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);
    return "memo/read"; // /templates/memo/read.html
  }

  /**
   * 메모 수정 폼
   */
  @GetMapping(value = "/update")
  public String update(Model model, 
      @RequestParam(name="memono", defaultValue="0") int memono) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);
    
    MemoVO memoVO = memoProc.read(memono); // 수정할 메모 조회
    model.addAttribute("memoVO", memoVO);
    return "memo/update"; // /templates/memo/update.html
  }

  /**
   * 메모 수정 처리
   */
  @PostMapping(value = "/update")
  public String update(MemoVO memoVO, Model model) {
    int cnt = memoProc.update(memoVO); // 메모 수정
    if (cnt == 1) {
      model.addAttribute("msg", "메모 수정 성공!");
      return "redirect:/memo/list_all"; // 수정 후 목록 페이지로 리다이렉트
    } else {
      model.addAttribute("msg", "메모 수정 실패!");
      return "memo/update"; // 수정 실패 시 수정 폼으로 돌아감
    }
  }

  @GetMapping("/delete")
  public String delete(Model model,
      @RequestParam(name = "memono") int memono) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);
    
    MemoVO memoVO = memoProc.read(memono);
    model.addAttribute("memoVO", memoVO);

    return "memo/delete"; // 삭제 확인 페이지로 이동
  }
  
  
  @PostMapping("/delete")
  public String delete_proc(Model model, 
      @RequestParam(name = "memono") int memono) {
    
    int cnt = memoProc.delete(memono);
    if (cnt == 1) {
      model.addAttribute("msg", "메모 삭제 성공!");
    } else {
      model.addAttribute("msg", "메모 삭제 실패!");
    }

    return "redirect:/memo/list_all";
  }
}