package dev.mvc.book;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookVOMenu {
  /** 카테고리 그룹(대분류) */
  private String author;
  
  /** 카테고리(중분류) */
  private ArrayList<BookVO> list_name;
  
}