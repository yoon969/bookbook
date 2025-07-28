package dev.mvc.reviewer;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.reviewer.ReviewerProc")
public class ReviewerProc implements ReviewerProcInter {

  @Autowired
  private ReviewerDAOInter reviewerDAO;
  
  @Autowired
  Security security;

  public ReviewerProc() {
    // System.out.println("-> ReviewerProc created.");
  }

  @Override
  public int checkID(String reviewerid) {
    return this.reviewerDAO.checkID(reviewerid);
  }

  @Override
  public int create(ReviewerVO reviewerVO) {
    String reviewerpasswd = reviewerVO.getReviewerpasswd();
//    Security security = new Security();
    String passwd_encoded = security.aesEncode(reviewerpasswd);
    reviewerVO.setReviewerpasswd(passwd_encoded);
    
    // memberVO.setPasswd(new Security().aesEncode(memberVO.getPasswd())); // 단축형
    
    int cnt = this.reviewerDAO.create(reviewerVO);
    return cnt;
  }

  @Override
  public ArrayList<ReviewerVO> list() {
    return this.reviewerDAO.list();
  }

  @Override
  public ReviewerVO read(int reviewerno) {
    return this.reviewerDAO.read(reviewerno);
  }

  @Override
  public ReviewerVO readById(String reviewerid) {
    return this.reviewerDAO.readById(reviewerid);
  }

  /**
   * 회원인지 검사
   */
  @Override
  public boolean isReviewer(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin") || grade.equals("member")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    
    return sw;
  }
  
  /**
   * 관리자, 회원인지 검사
   */  
  @Override
  public boolean isAdmin(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    
    return sw;
  }

  @Override
  public int update(ReviewerVO reviewerVO) {
    return this.reviewerDAO.update(reviewerVO);
  }

  @Override
  public int delete(int reviewerno) {
    return this.reviewerDAO.delete(reviewerno);
  }

  @Override
  public int passwd_check(HashMap<String, Object> map) {
    String reviewerpasswd = (String)map.get("reviewerpasswd");
    reviewerpasswd = this.security.aesEncode(reviewerpasswd);
    map.put("reviewerpasswd", reviewerpasswd);    
    
    int cnt = this.reviewerDAO.passwd_check(map);
    return cnt;
  }

  @Override
  public int passwd_update(HashMap<String, Object> map) {
    String reviewerpasswd = (String)map.get("reviewerpasswd");
    reviewerpasswd = this.security.aesEncode(reviewerpasswd);
    map.put("reviewerpasswd", reviewerpasswd);
    
    int cnt = this.reviewerDAO.passwd_update(map);
    return cnt;
  }

  @Override
  public int login(HashMap<String, Object> map) {
    String reviewerpasswd = (String)map.get("reviewerpasswd");
    reviewerpasswd = this.security.aesEncode(reviewerpasswd);
    map.put("reviewerpasswd", reviewerpasswd);
    
    int cnt = this.reviewerDAO.login(map);
    
    return cnt;
  }
}
