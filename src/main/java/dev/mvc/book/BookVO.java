package dev.mvc.book;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE books (
//    bookno          NUMBER(8)     NOT NULL  PRIMARY KEY,
//    title         VARCHAR(255)  NOT NULL,
//      author          VARCHAR(100)  NOT NULL,
//    cnt            NUMBER(7)     DEFAULT 0     NOT NULL,
//    publisher     VARCHAR(100)  NOT NULL,
//    published_date  DATE          NOT NULL,
//    created_at      DATE          NOT NULL,
//      seqno           NUMBER(5)       DEFAULT 1       NOT NULL,
//      VISIBLE         CHAR(1)         DEFAULT 'N'    NOT NULL
//  );

@Setter @Getter @ToString
public class BookVO {
  private Integer bookno;

  /** 책 제목 */
  @NotEmpty(message = "책 제목은 필수 항목입니다.")
  @Size(min = 1, max = 255, message = "책 제목은 최소 1자에서 최대 255자입니다.")
  private String title;

  /** 저자 */
  @NotEmpty(message = "저자는 필수 항목입니다.")
  @Size(min = 1, max = 100, message = "저자명은 최소 1자에서 최대 100자입니다.")
  private String author;
  
  /** 관련 자료수 */
  @NotNull(message="관련 자료수는 필수 입력 항목입니다.")
  @Min(value=0)
  @Max(value=1000000)
  private Integer cnt=0;

  /** 출판사 */
  @NotEmpty(message = "출판사는 필수 항목입니다.")
  @Size(min = 1, max = 100, message = "출판사명은 최소 1자에서 최대 100자입니다.")
  private String publisher;

  /** 출판일 */
  @NotNull(message = "출판일은 필수 항목입니다.")
  private LocalDate published_date;

  /** 등록일 (시스템에서 자동 생성) */
  private LocalDate created_at;
    
  /** 출력 순서 */
  @NotNull(message="출력 순서는 필수 입력 항목입니다.")
  @Min(value=1)
  @Max(value=1000000)  
  private Integer seqno;
  
  /** 출력 모드 */
  @NotEmpty(message="출력 모드는 필수 항목입니다.")
  @Pattern(regexp="^[YN]$", message="Y 또는 N만 입력 가능합니다.")
  private String visible = "N";
}