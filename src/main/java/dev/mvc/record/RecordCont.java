package dev.mvc.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.book.BookProcInter;
import dev.mvc.book.BookVO;
import dev.mvc.book.BookVOMenu;
import dev.mvc.recordgood.RecordgoodProcInter;
import dev.mvc.recordgood.RecordgoodVO;
import dev.mvc.reply.ReplyProcInter;
import dev.mvc.reply.ReplyVO;
import dev.mvc.reviewer.ReviewerProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@RequestMapping(value = "/record")
@Controller
public class RecordCont {
  @Autowired
  @Qualifier("dev.mvc.reviewer.ReviewerProc") // @Service("dev.mvc.reviewer.ReviewerProc")
  private ReviewerProcInter reviewerProc;

  @Autowired
  @Qualifier("dev.mvc.book.BookProc") // @Component("dev.mvc.book.BookProc")
  private BookProcInter bookProc;
  
  @Autowired
  @Qualifier("dev.mvc.reply.ReplyProc") // @Component("dev.mvc.book.BookProc")
  private ReplyProcInter replyProc;

  @Autowired
  @Qualifier("dev.mvc.record.RecordProc") // @Component("dev.mvc.record.RecordProc")
  private RecordProcInter recordProc;

  @Autowired
  @Qualifier("dev.mvc.recordgood.RecordgoodProc") 
  private RecordgoodProcInter recordgoodProc;
  
  public RecordCont() {
    System.out.println("-> RecordCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name= "url", defaultValue = "")String url) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, record 테이블은 FK로 bookno를 사용함.
  // http://localhost:9091/record/create X
  // http://localhost:9091/record/create?bookno=1 // bookno 변수값을 보내는 목적
  // http://localhost:9091/record/create?bookno=2
  // http://localhost:9091/record/create?bookno=5
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("recordVO") RecordVO recordVO, 
      @RequestParam(name="bookno", defaultValue="0") int bookno) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    BookVO bookVO = this.bookProc.read(bookno); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("bookVO", bookVO);

    return "record/create"; // /templates/record/create.html
  }

  /**
   * 등록 처리 http://localhost:9091/record/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create_proc(HttpServletRequest request, 
      HttpSession session, 
      Model model, 
      @ModelAttribute("recordVO") RecordVO recordVO,
      RedirectAttributes ra) {
    
    BookVO bookVO = bookProc.read(recordVO.getBookno());

    if (reviewerProc.isAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Record.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = recordVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          recordVO.setFile1(file1); // 순수 원본 파일명
          recordVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          recordVO.setThumb1(thumb1); // 원본이미지 축소판
          recordVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", Tool.UPLOAD_FILE_CHECK_FAIL); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/record/msg"); // msg.html, redirect parameter 적용
          return "redirect:/record/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int reviewerno = (int) session.getAttribute("reviewerno"); // reviewerno FK
      recordVO.setReviewerno(reviewerno);
      int cnt = this.recordProc.create(recordVO);

      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> recordno: " + recordVO.getRecordno());
      // mav.addObject("recordno", recordVO.getRecordno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "record/msg";

        // type 3 권장
        // return "redirect:/record/list_all"; // /templates/record/list_all.html

        // System.out.println("-> recordVO.getBookno(): " + recordVO.getBookno());
        // ra.addFlashAttribute("bookno", recordVO.getBookno()); // controller ->
        // controller: X
        this.recordProc.update_cnt_up(recordVO.getBookno());
        this.recordProc.update_grp_cnt_up(bookVO.getAuthor());

        ra.addAttribute("bookno", recordVO.getBookno()); // controller -> controller: O
        return "redirect:/record/list_by_bookno";

        // return "redirect:/record/list_by_bookno?bookno=" + recordVO.getBookno();
        // // /templates/record/list_by_bookno.html
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/record/msg"); // msg.html, redirect parameter 적용
        return "redirect:/record/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/reviewer/login_cookie_need?url=/record/create?bookno=" + recordVO.getBookno(); // /reviewer/login_cookie_need.html
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9091/record/list_all
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    if (this.reviewerProc.isAdmin(session)) { // 관리자만 조회 가능
      ArrayList<RecordVO> list = this.recordProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (RecordVO recordVO : list) {
//        String title = recordVO.getTitle();
//        String content = recordVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        recordVO.setTitle(title);
//        recordVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "/record/list_all";

    } else {
      return "redirect:/reviewer/login_cookie_need";

    }

  }

//  /**
//   * 유형 1
//   * 카테고리별 목록
//   * http://localhost:9091/record/list_by_bookno?bookno=5
//   * http://localhost:9091/record/list_by_bookno?bookno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_bookno")
//  public String list_by_bookno(HttpSession session, Model model,
//                                     @RequestParam(name="bookno", defaultValue = "")int bookno) {
//    ArrayList<BookVOMenu> menu = this.bookProc.menu();
//    model.addAttribute("menu", menu);
//    
//     BookVO bookVO = this.bookProc.read(bookno);
//     model.addAttribute("bookVO", bookVO);
//    
//    ArrayList<RecordVO> list = this.recordProc.list_by_bookno(bookno);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//
//    return "record/list_by_bookno";
//  }

//  /**
//   * 유형 2
//   * 카테고리별 목록 + 검색
//   * http://localhost:9091/record/list_by_bookno?bookno=5
//   * http://localhost:9091/record/list_by_bookno?bookno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_bookno")
//  public String list_by_bookno_search(HttpSession session, Model model, 
//                                                    int bookno, @RequestParam(name="word", defaultValue = "") String word) {
//    ArrayList<BookVOMenu> menu = this.bookProc.menu();
//    model.addAttribute("menu", menu);
//    
//     BookVO bookVO = this.bookProc.read(bookno);
//     model.addAttribute("bookVO", bookVO);
//    
//     word = Tool.checkNull(word).trim();
//     
//     HashMap<String, Object> map = new HashMap<>();
//     map.put("bookno", bookno);
//     map.put("word", word);
//     
//    ArrayList<RecordVO> list = this.recordProc.list_by_bookno_search(map);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//    
//    int search_count = this.recordProc.list_by_bookno_search_count(map);
//    model.addAttribute("search_count", search_count);
//    
//    return "record/list_by_bookno_search"; // /templates/record/list_by_bookno_search.html
//  }

  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/record/list_by_bookno?bookno=5
   * http://localhost:9091/record/list_by_bookno?bookno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_bookno")
  public String list_by_bookno_search_paging(HttpSession session, Model model, 
      @RequestParam(name = "bookno", defaultValue = "0") int bookno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> bookno: " + bookno);

    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    BookVO bookVO = this.bookProc.read(bookno);
    model.addAttribute("bookVO", bookVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("bookno", bookno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<RecordVO> list = this.recordProc.list_by_bookno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.recordProc.list_by_bookno_search_count(map);
    String paging = this.recordProc.pagingBox(bookno, now_page, word, "/record/list_by_bookno", search_count,
        Record.RECORD_PER_PAGE, Record.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Record.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "record/list_by_bookno_search_paging"; // /templates/record/list_by_bookno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9091/record/list_by_bookno?bookno=5
   * http://localhost:9091/record/list_by_bookno?bookno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_bookno_grid")
  public String list_by_bookno_search_paging_grid(HttpSession session, Model model, 
      @RequestParam(name = "bookno", defaultValue = "0") int bookno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> bookno: " + bookno);

    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    BookVO bookVO = this.bookProc.read(bookno);
    model.addAttribute("bookVO", bookVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("bookno", bookno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<RecordVO> list = this.recordProc.list_by_bookno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.recordProc.list_by_bookno_search_count(map);
    String paging = this.recordProc.pagingBox(bookno, now_page, word, "/record/list_by_bookno_grid", search_count,
        Record.RECORD_PER_PAGE, Record.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Record.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/record/list_by_bookno_search_paging_grid.html
    return "record/list_by_bookno_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9091/record/read?recordno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model, 
      @RequestParam(name="recordno", defaultValue = "0") int recordno, 
      @RequestParam(name="word", defaultValue = "") String word, 
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    RecordVO recordVO = this.recordProc.read(recordno);

//    String title = recordVO.getTitle();
//    String content = recordVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    recordVO.setTitle(title);
//    recordVO.setContent(content);  

    long size1 = recordVO.getSize1();
    String size1_label = Tool.unit(size1);
    recordVO.setSize1_label(size1_label);

    model.addAttribute("recordVO", recordVO);

    BookVO bookVO = this.bookProc.read(recordVO.getBookno());
    model.addAttribute("bookVO", bookVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_record(recordno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    // -------------------------------------------------------------------------------------------
    // 추천 관련
    // -------------------------------------------------------------------------------------------
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("recordno", recordno);
    
    int hartCnt = 0;
    if (session.getAttribute("reviewerno") != null ) { // 회원인 경우만 카운트 처리
      int reviewerno = (int)session.getAttribute("reviewerno");
      map.put("reviewerno", reviewerno);
      
      hartCnt = this.recordgoodProc.hartCnt(map);
    } 
    
    model.addAttribute("hartCnt", hartCnt);
    List<ReplyVO> list = replyProc.list_by_recordno(recordno);
    model.addAttribute("list", list);  // <== ★ 이게 꼭 있어야 함
    // -------------------------------------------------------------------------------------------
    
    return "record/read";
  }

  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/record/map?recordno=1
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, 
      @RequestParam(name="recordno", defaultValue="0") int recordno) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    RecordVO recordVO = this.recordProc.read(recordno); // map 정보 읽어 오기
    model.addAttribute("recordVO", recordVO); // request.setAttribute("recordVO", recordVO);

    BookVO bookVO = this.bookProc.read(recordVO.getBookno()); // 그룹 정보 읽기
    model.addAttribute("bookVO", bookVO);

    return "record/map";
  }

  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9091/record/map
   * 
   * @param recordVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(Model model,
                                  @RequestParam("recordno") int recordno,
                                  @RequestParam("map") String map) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("recordno", recordno);
    hashMap.put("map", map);

    this.recordProc.map(hashMap);

    return "redirect:/record/read?recordno=" + recordno;
  }

  /**
   * Youtube 등록/수정/삭제 폼 http://localhost:9091/record/youtube?recordno=1
   * 
   * @return
   */
  @GetMapping(value = "/youtube")
  public String youtube(Model model, 
                              @RequestParam(name="recordno", defaultValue="0") int recordno,
                              @RequestParam(name="word", defaultValue="")  String word, 
                              @RequestParam(name="now_page", defaultValue="0") int now_page) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    RecordVO recordVO = this.recordProc.read(recordno); // map 정보 읽어 오기
    model.addAttribute("recordVO", recordVO); // request.setAttribute("recordVO", recordVO);

    BookVO bookVO = this.bookProc.read(recordVO.getBookno()); // 그룹 정보 읽기
    model.addAttribute("bookVO", bookVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    return "record/youtube";  // forward
  }

  /**
   * Youtube 등록/수정/삭제 처리 http://localhost:9091/record/youtube
   * 
   * @param recordVO
   * @return
   */
  @PostMapping(value = "/youtube")
  public String youtube_update(Model model, 
                                             RedirectAttributes ra,
                                             @RequestParam(name="recordno", defaultValue = "0") int recordno, 
                                             @RequestParam(name="youtube", defaultValue = "") String youtube, 
                                             @RequestParam(name="word", defaultValue = "") String word, 
                                             @RequestParam(name="now_page", defaultValue = "0") int now_page) {

    if (youtube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
      youtube = Tool.youtubeResize(youtube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
    }

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("recordno", recordno);
    hashMap.put("youtube", youtube);

    this.recordProc.youtube(hashMap);
    
    ra.addAttribute("recordno", recordno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    // return "redirect:/record/read?recordno=" + recordno;
    return "redirect:/record/read";
  }

  /**
   * 수정 폼 http:// localhost:9091/record/update_text?recordno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model,
                                  RedirectAttributes ra,
                                  @RequestParam(name="recordno", defaultValue="0") int recordno,
                                  @RequestParam(name="word", defaultValue="")  String word, 
                                  @RequestParam(name="now_page", defaultValue="0") int now_page) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.reviewerProc.isAdmin(session)) { // 관리자로 로그인한경우
      RecordVO recordVO = this.recordProc.read(recordno);
      model.addAttribute("recordVO", recordVO);

      BookVO bookVO = this.bookProc.read(recordVO.getBookno());
      model.addAttribute("bookVO", bookVO);

      return "record/update_text"; // /templates/record/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
      return "redirect:/reviewer/login_cookie_need?url=/record/update_text?recordno=" + recordno;
    }

  }

  /**
   * 수정 처리 http://localhost:9091/record/update_text?recordno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text_proc(HttpSession session, Model model, 
            RecordVO recordVO, 
           RedirectAttributes ra,
           @RequestParam(name="search_word", defaultValue="")  String search_word, 
           @RequestParam(name="now_page", defaultValue="0") int now_page) {
    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

    if (this.reviewerProc.isAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("recordno", recordVO.getRecordno());
      map.put("passwd", recordVO.getPasswd());

      if (this.recordProc.password_check(map) == 1) { // 패스워드 일치
        this.recordProc.update_text(recordVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("recordno", recordVO.getRecordno());
        ra.addAttribute("bookno", recordVO.getBookno());
        return "redirect:/record/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/record/msg"); // msg.html, redirect parameter 적용

        return "redirect:/record/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      return "redirect:/reviewer/login_cookie_need?url=/record/update_text?recordno=" + recordVO.getRecordno();  
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9091/record/update_file?recordno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
                                  @RequestParam(name="recordno", defaultValue = "0") int recordno,
                                  @RequestParam(name="word", defaultValue = "0") String word, 
                                  @RequestParam(name="now_page", defaultValue = "0") int now_page) {
    if (this.reviewerProc.isAdmin(session)) { // 관리자로 로그인한경우
      ArrayList<BookVOMenu> menu = this.bookProc.menu();
      model.addAttribute("menu", menu);
      
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      RecordVO recordVO = this.recordProc.read(recordno);
      model.addAttribute("recordVO", recordVO);

      BookVO bookVO = this.bookProc.read(recordVO.getBookno());
      model.addAttribute("bookVO", bookVO);

      return "record/update_file";
    } else {
      return "redirect:/reviewer/login_cookie_need?url=/record/update_file?recordno=" + recordno;
    }
    

  }

  /**
   * 파일 수정 처리 http://localhost:9091/record/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file_proc(HttpSession session, Model model, RedirectAttributes ra,
                                      RecordVO recordVO,
                                      @RequestParam(name="word", defaultValue = "") String word,
                                      @RequestParam(name="now_page", defaultValue = "0") int now_page) {

    if (this.reviewerProc.isAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      RecordVO recordVO_old = recordProc.read(recordVO.getRecordno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = recordVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = recordVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Record.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/record/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = recordVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      recordVO.setFile1(file1);
      recordVO.setFile1saved(file1saved);
      recordVO.setThumb1(thumb1);
      recordVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.recordProc.update_file(recordVO); // Oracle 처리
      ra.addAttribute ("recordno", recordVO.getRecordno());
      ra.addAttribute("bookno", recordVO.getBookno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/record/read";
    } else {
      ra.addAttribute("url", "/reviewer/login_cookie_need"); 
      return "redirect:/record/post2get"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9091/record/delete?recordno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                            @RequestParam(name="bookno", defaultValue = "0")int bookno, 
                            @RequestParam(name="recordno", defaultValue = "0")int recordno, 
                            @RequestParam(name="word", defaultValue = "0")String word, 
                            @RequestParam(name="now_page", defaultValue = "0")int now_page) {
    if (this.reviewerProc.isAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("bookno", bookno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      ArrayList<BookVOMenu> menu = this.bookProc.menu();
      model.addAttribute("menu", menu);
      
      RecordVO recordVO = this.recordProc.read(recordno);
      model.addAttribute("recordVO", recordVO);
      
      BookVO bookVO = this.bookProc.read(recordVO.getBookno());
      model.addAttribute("bookVO", bookVO);
      
      return "record/delete"; // forward
      
    } else {
      return "redirect:/reviewer/login_cookie_need?url=/record/delete?recordno=" + recordno;
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/record/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_proc(RedirectAttributes ra,
                                  RecordVO recordVO,
                                  @RequestParam(name="recordno", defaultValue = "0")int recordno, 
                                  @RequestParam(name="bookno", defaultValue = "0")int bookno, 
                                  @RequestParam(name="word", defaultValue = "")String word, 
                                  @RequestParam(name="now_page", defaultValue = "0")int now_page) {
    BookVO bookVO = bookProc.read(recordVO.getBookno());
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    RecordVO recordVO_read = recordProc.read(recordno);
    
    replyProc.delete_by_recordno(recordno);
        
    String file1saved = recordVO_read.getFile1saved();
    String thumb1 = recordVO_read.getThumb1();
    
    String uploadDir = Record.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.recordProc.delete(recordno); // DBMS 삭제
    this.recordProc.update_cnt_down(recordVO.getBookno());
    this.recordProc.update_grp_cnt_down(bookVO.getAuthor());
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("bookno", bookno);
    map.put("word", word);
    
    if (this.recordProc.list_by_bookno_search_count(map) % Record.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("bookno", bookno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/record/list_by_bookno";    
    
  }   
   
  /**
   * 추천 처리 http://localhost:9091/record/good
   * 
   * @return
   */
  @PostMapping(value = "/good")
  @ResponseBody
  public String good(HttpSession session, Model model, @RequestBody String json_src){ 
    System.out.println("-> json_src: " + json_src); // json_src: {"recordno":"5"}
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    int recordno = (int)src.get("recordno"); // 값 가져오기
    System.out.println("-> recordno: " + recordno);
        
    if (this.reviewerProc.isReviewer(session)) { // 회원 로그인 확인
      // 추천을 한 상태인지 확인
      int reviewerno = (int)session.getAttribute("reviewerno");
      
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("recordno", recordno);
      map.put("reviewerno", reviewerno);
      
      int good_cnt = this.recordgoodProc.hartCnt(map);
      System.out.println("-> good_cnt: " + good_cnt);
      
      if (good_cnt == 1) {
        System.out.println("-> 추천 해제: " + recordno + ' ' + reviewerno);
        
        RecordgoodVO recordgoodVO = this.recordgoodProc.readByRecordnoReviewerno(map);
        
        this.recordgoodProc.delete(recordgoodVO.getRecordgoodno()); // 추천 삭제
        this.recordProc.decreaseRecom(recordno); // 카운트 감소
      } else {
        System.out.println("-> 추천: " + recordno + ' ' + reviewerno);
        
        RecordgoodVO recordgoodVO_new = new RecordgoodVO();
        recordgoodVO_new.setRecordno(recordno);
        recordgoodVO_new.setReviewerno(reviewerno);
        
        this.recordgoodProc.create(recordgoodVO_new);
        this.recordProc.increaseRecom(recordno); // 카운트 증가
      }
      
      // 추천 여부가 변경되어 다시 새로운 값을 읽어옴.
      int hartCnt = this.recordgoodProc.hartCnt(map);
      int recom = this.recordProc.read(recordno).getRecom();
            
      JSONObject result = new JSONObject();
      result.put("isReviewer", 1); // 로그인: 1, 비회원: 0
      result.put("hartCnt", hartCnt); // 추천 여부, 추천:1, 비추천: 0
      result.put("recom", recom);   // 추천인수
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
      
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      JSONObject result = new JSONObject();
      result.put("isReviewer", 0); // 로그인: 1, 비회원: 0
      
      System.out.println("-> result.toString(): " + result.toString());
      return result.toString();
    }

  }
}
