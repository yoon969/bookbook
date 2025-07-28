package dev.mvc.bookstory;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.mvc.book.BookProcInter;
import dev.mvc.book.BookVOMenu;
import dev.mvc.tool.Security;

@Controller
public class HomeCont {
  @Autowired // Spring이 CateProcInter를 구현한 CateProc 클래스의 객체를 생성하여 할당
  @Qualifier("dev.mvc.book.BookProc")
  private BookProcInter bookProc;
  
  @Autowired
  private Security security;
  
  public HomeCont() {
    System.out.println("-> HomeCont created.");
  }

  @GetMapping(value = "/") // http://localhost:9091
  public String home(Model model) { // 파일명 return
    if (this.security != null) {
      System.out.println("-> 객체 고유 코드: " + security.hashCode());
      System.out.println(security.aesEncode("1234"));
    }
    ArrayList<BookVOMenu> menu = this.bookProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", ""); // 시작페이지는 검색을 하지 않음.

    return "index"; // /templates/index.html
  }
}