package dev.mvc.book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.book.BookVO;
import dev.mvc.reviewer.ReviewerProc;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookCont {
  @Autowired
  @Qualifier("dev.mvc.book.BookProc")
  private BookProcInter bookProc;
  
  @Autowired
  @Qualifier("dev.mvc.reviewer.ReviewerProc")
  private ReviewerProc reviewerProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 6;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  /** 페이징 목록 주소, @GetMapping(value="/list_search") */
  private String list_url = "/book/list_search";
  
  public BookCont() {
    System.out.println("-> Book created");
  }
  
//  @GetMapping(value="/create") //http://localhost:9091/book/create
//  @ResponseBody
//  public String create() {
//    System.out.println("-> http://localhost:9091/book/create");
//    return "<h2>Create test</h2>";
//  }
  
  /**
   * 등록 폼
   * http://localhost:9092/book/create
   * @return
   */
    @GetMapping(value="/create") //http://localhost:9092/book/create
    public String create(@ModelAttribute("bookVO") BookVO bookVO) {
//      bookVO.setGrp("영화/여행/개발...");
//      bookVO.setName("카테고리 이름");
      return "/book/create"; // /templates/book/create.html
    }
    
    @PostMapping(value = "/create")
    public String create(Model model,
                              @Valid BookVO bookVO,
                              BindingResult bindingResult,
                              @RequestParam(name="word", defaultValue = "") String word,
                              RedirectAttributes ra){
      
      if(bindingResult.hasErrors()==true) {
        return "book/create"; // /templates.book.create.html
      }
      
      int cnt = this.bookProc.create(bookVO);
      System.out.println("-> cnt: "+cnt);
      
      if(cnt == 1) {
//        model.addAttribute("code", Tool.CREATE_SUCCESS);
//        model.addAttribute("name",bookVO.getTitle());
        ra.addAttribute("word", word);
        // return "redirect:/book/list_all";
        return "redirect:/book/list_search";
      } else {
        model.addAttribute("code", Tool.CREATE_FAIL);
      }
      
      model.addAttribute("cnt",cnt);
      return "book/msg";  // templates/book/msg.html
    }
    
    @GetMapping(value="/list_all")
      public String list_all(Model model, @ModelAttribute("bookVO") BookVO bookVO) {
        bookVO.setTitle("");
        bookVO.setAuthor("");
      
        ArrayList<BookVO> list = this.bookProc.list_all();
        model.addAttribute("list", list);
        
     // 2단 메뉴
        ArrayList<BookVOMenu> menu = this.bookProc.menu();
        model.addAttribute("menu", menu);
        
     // 카테고리 그룹 목록
        ArrayList<String> authorset = this.bookProc.authorset();
        bookVO.setAuthor(String.join("/",  authorset));
        System.out.println("-> bookVO.getAuthor(): " + bookVO.getAuthor());
        
        return "book/list_all"; // /templates/book/list_all.html
    }
    
    @GetMapping(value="/list_search") 
    public String list_search_paging(HttpSession session, Model model, 
                                     @RequestParam(name="word", defaultValue = "") String word,
                                     @RequestParam(name="now_page", defaultValue="1") int now_page) {
      if (this.reviewerProc.isAdmin(session)) {
        BookVO bookVO = new BookVO();
        // bookVO.setGenre("분류");
        // bookVO.setName("카테고리 이름을 입력하세요."); // Form으로 초기값을 전달
        
        // 카테고리 그룹 목록
        ArrayList<String> list_author = this.bookProc.authorset();
        bookVO.setAuthor(String.join("/", list_author));
        
        model.addAttribute("bookVO", bookVO); // 등록폼 카테고리 그룹 초기값
        
        word = Tool.checkNull(word);             // null -> ""
        
        ArrayList<BookVO> list = this.bookProc.list_search_paging(word, now_page, this.record_per_page);
        model.addAttribute("list", list);
        
//        ArrayList<BookVO> menu = this.bookProc.list_all_bookgrp_y();
//        model.addAttribute("menu", menu);

        ArrayList<BookVOMenu> menu = this.bookProc.menu();
        model.addAttribute("menu", menu);
        
        int search_cnt = list.size();
        model.addAttribute("search_cnt", search_cnt);    
        
        model.addAttribute("word", word); // 검색어
        
        // --------------------------------------------------------------------------------------
        // 페이지 번호 목록 생성
        // --------------------------------------------------------------------------------------
        int search_count = this.bookProc.list_search_count(word);
        String paging = this.bookProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);
        
        // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
        int no = search_count - ((now_page - 1) * this.record_per_page);
        model.addAttribute("no", no);
        System.out.println("-> no: " + no);
        // --------------------------------------------------------------------------------------    
        
        return "book/list_search";  // /templates/book/list_search.html
      } else {
        return "redirect:/reviewer/login_cookie_need?url=/book/list_search"; // redirect
      }
      
    }  
    
    
    @GetMapping(value="/read/{bookno}")
    public String read(Model model, @PathVariable("bookno") Integer bookno,
        @RequestParam(name="word", defaultValue = "") String word,
        @RequestParam(name="now_page", defaultValue="1") int now_page) {
      
      BookVO bookVO = this.bookProc.read(bookno);
      model.addAttribute("bookVO", bookVO);
      
      // 2단 메뉴
      ArrayList<BookVOMenu> menu = this.bookProc.menu();
      model.addAttribute("menu", menu);
      
//    카테고리 그룹 목록
//      ArrayList<String> authorset = this.bookProc.authorset();
//      bookVO.setAuthor(String.join("/",  authorset));
//      System.out.println("-> bookVO.getAuthor(): " + bookVO.getAuthor());
      
      model.addAttribute("word", word);
      System.out.println("-> word null 체크: " + word);

      // ArrayList<BookVO> list = this.bookProc.list_search(word);  // 검색 목록
      ArrayList<BookVO> list = this.bookProc.list_search_paging(word, now_page, this.record_per_page); // 검색 목록 + 페이징
      model.addAttribute("list", list);
      
      int list_search_count = this.bookProc.list_search_count(word); // 검색된 레코드 갯수
      model.addAttribute("list_search_count", list_search_count);
      
      
   // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.bookProc.list_search_count(word);
      String paging = this.bookProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      System.out.println("-> no: " + no);
      // --------------------------------------------------------------------------------------    
      
      return "book/read"; // /templates/book/read.html
  }
    
    @GetMapping(value="/update/{bookno}")
    public String update(Model model, @PathVariable("bookno") Integer bookno,
                              @RequestParam(name="word", defaultValue = "") String word,
                              @RequestParam(name="now_page", defaultValue="1") int now_page) {
//      System.out.println("-> read bookno:" + bookno);
      BookVO bookVO = this.bookProc.read(bookno);
      model.addAttribute("bookVO", bookVO);
      
      String formattedDate = bookVO.getPublished_date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      model.addAttribute("formattedDate", formattedDate);
      
      ArrayList<BookVO> list = this.bookProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
   // 2단 메뉴
      ArrayList<BookVOMenu> menu = this.bookProc.menu();
      model.addAttribute("menu", menu);
      
      model.addAttribute("word", word);
      
      
   // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.bookProc.list_search_count(word);
      String paging = this.bookProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      System.out.println("-> no: " + no);
      // --------------------------------------------------------------------------------------    
      
      return "book/update"; // /templates/book/update.html
  }
  
    
    /**
     * 수정 처리
     * @param model
     * @param bookVO
     * @param bindingResult
     * @return
     */
    @PostMapping(value="/update")
    public String update_process(Model model, 
                                        @Valid BookVO bookVO, 
                                        BindingResult bindingResult,
                                        @RequestParam(name="word", defaultValue = "") String word,
                                        @RequestParam(name="now_page", defaultValue = "1") int now_page,
                                        RedirectAttributes ra) {
      
      ArrayList<BookVO> list = this.bookProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      if(bindingResult.hasErrors()==true) {
        return "book/update"; // /templates.book.update.html
      }
      
      int cnt = this.bookProc.update(bookVO);
      System.out.println("-> read cnt:" + cnt);
      
      if(cnt == 1) {
//        model.addAttribute("code", Tool.UPDATE_SUCCESS);
//        model.addAttribute("name",bookVO.getTitle());
//        return "redirect:/book/update?bookno=" + bookVO.getBookno();
        ra.addAttribute("now_page", now_page);
        ra.addAttribute("word", word); // redirect로 데이터 전송, 한글 깨짐 방지
        return "redirect:/book/update/" + bookVO.getBookno();
      } else {
        model.addAttribute("code", Tool.UPDATE_FAIL);
      }
      
   // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.bookProc.list_search_count(word);
      String paging = this.bookProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      System.out.println("-> no: " + no);
      // --------------------------------------------------------------------------------------    
      
      
      model.addAttribute("cnt", cnt);
      return "book/msg"; // /templates/book/update.html
  }
    
    /**
     * 삭제폼
     * @param model
     * @param bookno
     * @return
     */
     @GetMapping(value="/delete/{bookno}")
     public String delete(Model model, 
                               @PathVariable("bookno") Integer bookno,
                               @RequestParam(name="word", defaultValue = "") String word,
                               @RequestParam(name="now_page", defaultValue="1") int now_page) {
       System.out.println("-> read bookno:" + bookno);
       
       BookVO bookVO = this.bookProc.read(bookno);
       model.addAttribute("bookVO", bookVO);
       
       int childCount = this.bookProc.countByBookno(bookno);
       model.addAttribute("childCount", childCount);
       
//       ArrayList<BookVO> list = this.bookProc.list_all();
//       model.addAttribute("list", list);
       ArrayList<BookVO> list = this.bookProc.list_search_paging(word, now_page, this.record_per_page);
       System.out.println("-> delete form list.size(): " + list.size());
       model.addAttribute("list", list);
       
       // 2단 메뉴
       ArrayList<BookVOMenu> menu = this.bookProc.menu();
       model.addAttribute("menu", menu);
       
       model.addAttribute("word", word);
       
    // --------------------------------------------------------------------------------------
       // 페이지 번호 목록 생성
       // --------------------------------------------------------------------------------------
       int search_count = this.bookProc.list_search_count(word);
       String paging = this.bookProc.pagingBox(now_page, word, this.list_url, search_count, this.record_per_page, this.page_per_block);
       model.addAttribute("paging", paging);
       model.addAttribute("now_page", now_page);
       
       // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
       int no = search_count - ((now_page - 1) * this.record_per_page);
       model.addAttribute("no", no);
       System.out.println("-> no: " + no);
       // --------------------------------------------------------------------------------------    
       
       
       return "book/delete"; // /templates/book/update.html
   }
     
     @PostMapping(value="/delete/{bookno}")
     public String delete_process(Model model, 
                                         @PathVariable("bookno") Integer bookno,
                                         @RequestParam(name="word", defaultValue = "") String word,
                                         RedirectAttributes ra,
                                         @RequestParam(name="now_page", defaultValue="1") int now_page) {
       
       BookVO bookVO = this.bookProc.read(bookno);
       model.addAttribute("bookVO",bookVO);
       
       // 1. 자식 레코드가 있는지 확인
       int childCount = this.bookProc.countByBookno(bookno);
       model.addAttribute("childCount", childCount);
       
       if (childCount > 0) {
         // 2. 자식 레코드가 있으면 먼저 자식 레코드 삭제
         int deleteCount = this.bookProc.deleteByBookno(bookno); // 자식 레코드 삭제
         System.out.println("-> 자식 레코드 삭제됨: " + deleteCount);
         
      // 자식 레코드 수만큼 카운트 감소
         Map<String, Object> params = new HashMap<>();
         params.put("deleteCount", deleteCount);  // 자식 레코드 수
         params.put("author", bookVO.getAuthor());  // 카테고리 그룹

         // 카테고리 cnt 값 감소 처리
         int updateCount = this.bookProc.update_author_cnt_down(params);  // 자식 레코드 수만큼 카운트 감소
         System.out.println("-> 카테고리 cnt 값 감소됨: " + updateCount);
     }
       
       int cnt = this.bookProc.delete(bookno);
       System.out.println("->cnt: " + cnt);
       
       if (cnt == 1) {
         // ----------------------------------------------------------------------------------------------------------
         // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
         int search_cnt = this.bookProc.list_search_count(word);
         if (search_cnt % this.record_per_page == 0) {
           now_page = now_page - 1;
           if (now_page < 1) {
             now_page = 1; // 최소 시작 페이지
           }
         }
      // model.addAttribute("code", Tool.DELETE_SUCCESS);
         ra.addAttribute("word", word);
         ra.addAttribute("now_page", now_page);      

         return "redirect:/book/list_search"; // @GetMapping(value="/list_search")
       } else {
         model.addAttribute("code", Tool.DELETE_FAIL); 
       }
         
       model.addAttribute("author", bookVO.getAuthor());
       model.addAttribute("title", bookVO.getTitle());
       model.addAttribute("cnt", cnt); 

       return "book/msg"; // /templates/book/update.html
      }
     
     @GetMapping(value="/update_seqno_forward/{bookno}")
     public String update_seqno_forward(Model model, 
                                                   @PathVariable("bookno") Integer bookno,
                                                   @RequestParam(name="word", defaultValue = "") String word,
                                                   @RequestParam(name="now_page", defaultValue = "1") int now_page,
                                                   RedirectAttributes ra) {
       
       this.bookProc.update_seqno_forward(bookno);
       ra.addAttribute("word", word);
       ra.addAttribute("now_page", now_page);
       
       
       return "redirect:/book/list_search";  // @GetMapping(value="/list_all")
     }
     
     @GetMapping(value = "/update_seqno_backward/{bookno}")
     public String update_seqno_backward(Model model, 
                                                     @PathVariable("bookno") Integer bookno,
                                                     @RequestParam(name = "word", defaultValue = "") String word,
                                                     @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                                                     RedirectAttributes ra) {

       this.bookProc.update_seqno_backward(bookno);
       ra.addAttribute("word", word);
       ra.addAttribute("now_page", now_page);

       return "redirect:/book/list_search"; // @GetMapping(value="/list_all")
     }
     
     @GetMapping(value="/update_visible_y/{bookno}")
     public String update_visible_y(Model model, 
                                           @PathVariable("bookno") Integer bookno,
                                           @RequestParam(name = "word", defaultValue = "") String word,
                                           @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                                           RedirectAttributes ra) {
       System.out.println("-> update_visible_y()");
       
       this.bookProc.update_visible_y(bookno);
       ra.addAttribute("word", word);
       ra.addAttribute("now_page", now_page);
       
       return "redirect:/book/list_search";  // @GetMapping(value="/list_all")
     }
     
     /**
      * 카테고리 비공개 설정
      * http://localhost:9091/book/update_visible_n/1
      */
     @GetMapping(value="/update_visible_n/{bookno}")
     public String update_visible_n(Model model, 
                                           @PathVariable("bookno") Integer bookno,
                                           @RequestParam(name = "word", defaultValue = "") String word,
                                           @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                                           RedirectAttributes ra) {
       System.out.println("-> update_visible_n()");
       
       this.bookProc.update_visible_n(bookno);
       ra.addAttribute("word", word);
       ra.addAttribute("now_page", now_page);

       return "redirect:/book/list_search";  // @GetMapping(value="/list_all")
     }
     
     
}