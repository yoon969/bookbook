package dev.mvc.bookstory;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.book.BookDAOInter;
import dev.mvc.book.BookVO;
import dev.mvc.book.BookProcInter;

@SpringBootTest
class BookstoryApplicationTests {
  
  @Autowired
  private BookDAOInter bookDAO;
  
  @Autowired
  @Qualifier("dev.mvc.book.BookProc")
  private BookProcInter bookProc;

	@Test
	void contextLoads() {
	}

	@Test
  public void testCreate() {
      BookVO bookVO = new BookVO();
      bookVO.setTitle("주술회전");
      bookVO.setAuthor("데일 카네기");
      bookVO.setPublisher("현대지성");
      bookVO.setPublished_date(LocalDate.of(2019, 10, 7));
      
      int cnt = this.bookProc.create(bookVO);
      System.out.println("-> cnt: " + cnt);
  }
}
