package dev.mvc.reply;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
public class ReplyVO {
  /** 댓글 번호 */
  private int replyno;

  /** 관련 글 번호 */
  private int recordno;
  
  /** 회원 번호 */
  private int reviewerno;
  
  /** 내용 */
  private String record;
  
  /** 등록일 */
  private String rdate;
  
  private String reviewername;
  
  
}
