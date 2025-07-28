package dev.mvc.reviewer;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.book.BookProcInter;
import dev.mvc.book.BookVOMenu;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/reviewer")
@Controller
public class ReviewerCont {
  @Autowired
  @Qualifier("dev.mvc.reviewer.ReviewerProc")  // @Service("dev.mvc.member.MemberProc")
  private ReviewerProcInter reviewerProc;
  
  @Autowired
  @Qualifier("dev.mvc.book.BookProc")
  private BookProcInter bookProc;
  
  public ReviewerCont() {
    System.out.println("-> ReviewerCont created.");  
  }
  
  @GetMapping(value="/checkID") // http://localhost:9091/member/checkID?id=admin
  @ResponseBody
  public String checkID(@RequestParam(name="reviewerid", defaultValue = "") String reviewerid) {    
//    System.out.println("-> id: " + id);
    int cnt = this.reviewerProc.checkID(reviewerid);
    
    // return "cnt: " + cnt;
    // return "{\"cnt\": " + cnt + "}";    // {"cnt": 1} JSON
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  
  /**
   * 회원 가입 폼
   * @param model
   * @param memberVO
   * @return
   */
  @GetMapping(value="/create") // http://localhost:9091/member/create
  public String create_form(Model model, 
                                      @ModelAttribute("reviewerVO") ReviewerVO reviewerVO) {
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);
    
    return "reviewer/create";    // /template/member/create.html
  }
  
  @PostMapping(value="/create")
  public String create_proc(Model model,
                                     @ModelAttribute("reviewerVO") ReviewerVO reviewerVO) {
    int checkID_cnt = this.reviewerProc.checkID(reviewerVO.getReviewerid());
    
    if (checkID_cnt == 0) {
      reviewerVO.setGrade(15); // 기본 회원 15
      int cnt = this.reviewerProc.create(reviewerVO);
      
      if (cnt == 1) {
        model.addAttribute("code", "create_success");
        model.addAttribute("reviewername", reviewerVO.getReviewername());
        model.addAttribute("reviewerid", reviewerVO.getReviewerid());
      } else {
        model.addAttribute("code", "create_fail");
      }
      
      model.addAttribute("cnt", cnt);
    } else { // id 중복
      model.addAttribute("code", "duplicte_fail");
      model.addAttribute("cnt", 0);
    }
    
    return "reviewer/msg"; // /templates/member/msg.html
  }
  
  @GetMapping(value="/list")
  public String list(HttpSession session, Model model) {
    if(this.reviewerProc.isAdmin(session)) {
      ArrayList<ReviewerVO> list = this.reviewerProc.list();
      
      model.addAttribute("list", list);
      ArrayList<BookVOMenu> menu = this.bookProc.menu();
      model.addAttribute("menu", menu);
      
      return "reviewer/list";  // /templates/member/list.html
    } else {
      return "redirect:/reviewer/login_cookie_need?url=/reviewer/list";
    }
  }
  
  @GetMapping(value="/read")
  public String read(HttpSession session, Model model,
                            @RequestParam(name="reviewerno", defaultValue = "") int reviewerno) {
    String grade = (String)session.getAttribute("grade"); // 등급: admin, member, guest
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);
    if (grade.equals("reviewer") &&  reviewerno == (int)session.getAttribute("reviewerno")) {
      System.out.println("-> read reviewerno: " + reviewerno);
      
      ReviewerVO reviewerVO = this.reviewerProc.read(reviewerno);
      model.addAttribute("reviewerVO", reviewerVO);
      
      return "reviewer/read";  // templates/member/read.html
      
    } else if (grade.equals("admin")) {
      System.out.println("-> read reviewerno: " + reviewerno);
      
      ReviewerVO reviewerVO = this.reviewerProc.read(reviewerno);
      model.addAttribute("reviewerVO", reviewerVO);
      
      return "reviewer/read";  // templates/member/read.html
    } else {
      return "redirect:/reviewer/login_cookie_need";  // redirect
    }
  }
  
  @PostMapping(value="/update")
  public String update_proc(Model model, 
                                       @ModelAttribute("reviewerVO") ReviewerVO reviewerVO) {
    int cnt = this.reviewerProc.update(reviewerVO); // 수정
    
    if (cnt == 1) {
      model.addAttribute("code", "update_success");
      model.addAttribute("reviewername", reviewerVO.getReviewername());
      model.addAttribute("reviewerid", reviewerVO.getReviewerid());
    } else {
      model.addAttribute("code", "update_fail");
    }
    
    model.addAttribute("cnt", cnt);
    
    return "reviewer/msg"; // /templates/member/msg.html
  }
  
  @GetMapping(value="/delete")
  public String delete(Model model, @RequestParam(name = "reviewerno", defaultValue ="") Integer reviewerno) {
    System.out.println("-> delete reviewerno: " + reviewerno);
    
    ReviewerVO reviewerVO = this.reviewerProc.read(reviewerno);
    model.addAttribute("reviewerVO", reviewerVO);
    
    return "reviewer/delete";  // templates/member/delete.html
  }
  
  /**
   * 회원 Delete process
   * @param model
   * @param memberno 삭제할 레코드 번호
   * @return
   */
  @PostMapping(value="/delete")
  public String delete_process(Model model, @RequestParam(name = "reviewerno", defaultValue ="") Integer reviewerno) {
    int cnt = this.reviewerProc.delete(reviewerno);
    
    if (cnt == 1) {
      return "redirect:/reviewer/list";
    } else {
      model.addAttribute("code", "delete_fail");
      return "reviewer/msg"; // /templates/member/msg.html
    }
  }
  
  @GetMapping(value="/login")
  public String login_form(Model model, HttpServletRequest request, HttpSession session) {
//    System.out.println("sessionid -> " + session.getId());
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    
    //    <input type='text' class="form-control" name='id' id='id' 
    //            th:value='${ck_id }' required="required" 
    //            style='width: 30%;' placeholder="아이디" autofocus="autofocus">
    model.addAttribute("ck_id", ck_id);
  
    //    <input type='checkbox' name='id_save' value='Y' 
    //            th:checked="${ck_id_save == 'Y'}"> 저장
    model.addAttribute("ck_id_save", ck_id_save);
  
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

//    model.addAttribute("ck_id_save", "Y");
//    model.addAttribute("ck_passwd_save", "Y");
    
    return "reviewer/login_cookie";  // templates/member/login_cookie.html
  }

  /**
   * Cookie 기반 로그인 처리
   * @param session
   * @param request
   * @param response
   * @param model
   * @param id 아이디
   * @param passwd 패스워드
   * @param id_save 아이디 저장 여부
   * @param passwd_save 패스워드 저장 여부
   * @return
   */
  @PostMapping(value="/login")
  public String login_proc(HttpSession session,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Model model, 
                                     @RequestParam(value="reviewerid", defaultValue = "") String reviewerid, 
                                     @RequestParam(value="reviewerpasswd", defaultValue = "") String reviewerpasswd,
                                     @RequestParam(value="id_save", defaultValue = "") String id_save,
                                     @RequestParam(value="passwd_save", defaultValue = "") String passwd_save,
                                     @RequestParam(value="url", defaultValue = "")String url
      ) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("reviewerid", reviewerid);
    map.put("reviewerpasswd", reviewerpasswd);
    
    int cnt = this.reviewerProc.login(map);
    System.out.println("-> login_proc cnt: " + cnt);
    
    model.addAttribute("cnt", cnt);
    
    if (cnt == 1) {
      // id를 이용하여 회원 정보 조회
      ReviewerVO reviewerVO = this.reviewerProc.readById(reviewerid);
      session.setAttribute("reviewerno", reviewerVO.getReviewerno());
      session.setAttribute("reviewerid", reviewerVO.getReviewerid());
      session.setAttribute("reviewername", reviewerVO.getReviewername());
//      session.setAttribute("grade", reviewerVO.getGrade());
      
   // 회원 등급 처리
      if (reviewerVO.getGrade() >= 1 && reviewerVO.getGrade() <= 10) {
        session.setAttribute("grade", "admin");
      } else if (reviewerVO.getGrade() >= 11 && reviewerVO.getGrade() <= 20) {
        session.setAttribute("grade", "member");
      } else if (reviewerVO.getGrade() >= 21) {
        session.setAttribute("grade", "guest");
      }
      // Cookie 관련 코드---------------------------------------------------------
      // -------------------------------------------------------------------
      // id 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (id_save.equals("Y")) { // id를 저장할 경우, Checkbox를 체크한 경우
        Cookie ck_id = new Cookie("ck_id", reviewerid);
        ck_id.setPath("/");  // root 폴더에 쿠키를 기록함으로 모든 경로에서 쿠기 접근 가능
        ck_id.setMaxAge(60 * 60 * 24 * 30); // 30 day, 초단위
        response.addCookie(ck_id); // id 저장
      } else { // N, id를 저장하지 않는 경우, Checkbox를 체크 해제한 경우
        Cookie ck_id = new Cookie("ck_id", "");
        ck_id.setPath("/");
        ck_id.setMaxAge(0);
        response.addCookie(ck_id); // id 저장
      }
      
      // id를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_id_save = new Cookie("ck_id_save", id_save);
      ck_id_save.setPath("/");
      ck_id_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_id_save);
      // -------------------------------------------------------------------
  
      // -------------------------------------------------------------------
      // Password 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
        Cookie ck_passwd = new Cookie("ck_passwd", reviewerpasswd);
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(60 * 60 * 24 * 30); // 30 day
        response.addCookie(ck_passwd);
      } else { // N, 패스워드를 저장하지 않을 경우
        Cookie ck_passwd = new Cookie("ck_passwd", "");
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(0);
        response.addCookie(ck_passwd);
      }
      // passwd를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
      ck_passwd_save.setPath("/");
      ck_passwd_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_passwd_save);
      // -------------------------------------------------------------------
      // ----------------------------------------------------------------------------     
      
      if(url.length()>0) {
        return "redirect:"+url;
      } else {
        return "redirect:/";
      }
    } else {
      model.addAttribute("code", "login_fail");
      return "reviewer/msg";
    }
  }
  
  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 종료
  // ----------------------------------------------------------------------------------
  /**
   * 로그아웃
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/logout")
  public String logout(HttpSession session, Model model) {
    session.invalidate();  // 모든 세션 변수 삭제
    return "redirect:/";
  }
  
  /**
   * 패스워드 수정 폼
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value="/passwd_update")
  public String passwd_update_form(HttpSession session, Model model) {
    if(this.reviewerProc.isReviewer(session)) {
      int reviewerno = (int)session.getAttribute("reviewerno"); // session에서 가져오기
      
      ReviewerVO reviewerVO = this.reviewerProc.read(reviewerno);
      
      model.addAttribute("reviewerVO", reviewerVO);
      
      return "reviewer/passwd_update";    
    } else {
      return "redirect:/reviewer/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 현재 패스워드 확인
   * @param session
   * @param current_passwd
   * @return 1: 일치, 0: 불일치
   */
  @PostMapping(value="/passwd_check")
  @ResponseBody
  public String passwd_check(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}

    JSONObject src = new JSONObject(json_src); // String -> JSON

    String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    System.out.println("-> current_passwd: " + current_passwd);
    
    try {
      Thread.sleep(3000);
    } catch(Exception e) {
      
    }
    
    int reviewerno = (int)session.getAttribute("reviewerno"); // session에서 가져오기
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("reviewerno", reviewerno);
    map.put("reviewerpasswd", current_passwd);
    
    int cnt = this.reviewerProc.passwd_check(map);
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);
    System.out.println(json.toString());
    
    return json.toString();   
  }
  
  /**
   * 패스워드 변경
   * @param session
   * @param model
   * @param current_passwd 현재 패스워드
   * @param passwd 새로운 패스워드
   * @return
   */
  @PostMapping(value="/passwd_update_proc")
  public String update_passwd_proc(HttpSession session, 
                                                    Model model, 
                                                    @RequestParam(value="current_passwd", defaultValue = "") String current_passwd, 
                                                    @RequestParam(value="reviewerpasswd", defaultValue = "") String reviewerpasswd) {
    if(this.reviewerProc.isReviewer(session)) {
      int reviewerno = (int)session.getAttribute("reviewerno"); // session에서 가져오기
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("reviewerno", reviewerno);
      map.put("reviewerpasswd", current_passwd);
      
      int cnt = this.reviewerProc.passwd_check(map);
      
      if (cnt == 0) { // 패스워드 불일치
        model.addAttribute("code", "passwd_not_equal");
        model.addAttribute("cnt", 0);
        
      } else { // 패스워드 일치
        map = new HashMap<String, Object>();
        map.put("reviewerno", reviewerno);
        map.put("reviewerpasswd", reviewerpasswd); // 새로운 패스워드
        
        int passwd_change_cnt = this.reviewerProc.passwd_update(map);
        
        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }

      return "reviewer/msg";   // /templates/member/msg.html
    } else {
      return "redirect:/reviewer/login_cookie_need"; // redirect
    }
    
  }
  
  /**
   * 로그인 요구에 따른 로그인 폼 출력 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/login_cookie_need")
  public String login_cookie_need(Model model, HttpServletRequest request,
                                          @RequestParam(name="url", defaultValue = "") String url) {
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    
    //    <input type='text' class="form-control" name='id' id='id' 
    //            th:value='${ck_id }' required="required" 
    //            style='width: 30%;' placeholder="아이디" autofocus="autofocus">
    model.addAttribute("ck_id", ck_id);
  
    //    <input type='checkbox' name='id_save' value='Y' 
    //            th:checked="${ck_id_save == 'Y'}"> 저장
    model.addAttribute("ck_id_save", ck_id_save);
  
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

//    model.addAttribute("ck_id_save", "Y");
//    model.addAttribute("ck_passwd_save", "Y");
    
    model.addAttribute("url", url);
    return "reviewer/login_cookie_need";  // templates/member/login_cookie_need.html
  }
}