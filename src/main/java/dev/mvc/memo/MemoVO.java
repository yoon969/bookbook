package dev.mvc.memo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CREATE TABLE memo (
 *   memono        NUMBER(10) NOT NULL PRIMARY KEY,
 *   memotitle     VARCHAR2(255) NOT NULL,
 *   memocontent   CLOB NOT NULL,
 *   created_date  DATE DEFAULT SYSDATE
 * );
 */

@Setter
@Getter
@ToString
public class MemoVO {

  /** 메모 번호 */
  private Integer memono;

  /** 메모 제목 */
  @NotEmpty(message = "제목은 필수 항목입니다.")
  @Size(min = 1, max = 255, message = "제목은 1~255자 이내여야 합니다.")
  private String memotitle;

  /** 메모 내용 */
  @NotEmpty(message = "내용은 필수 항목입니다.")
  private String memocontent;

  /** 작성일 */
  private LocalDate created_date;
}