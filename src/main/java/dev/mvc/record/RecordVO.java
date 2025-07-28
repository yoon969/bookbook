package dev.mvc.record;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE record(
//    recordno                            NUMBER(10)         NOT NULL         PRIMARY KEY,
//    reviewerno                              NUMBER(10)     NOT NULL , -- FK
//    bookno                                NUMBER(10)         NOT NULL , -- FK
//    title                                 VARCHAR2(200)         NOT NULL,
//    record                               CLOB                  NOT NULL,
//    recom                                 NUMBER(7)         DEFAULT 0         NOT NULL,
//    cnt                                   NUMBER(7)         DEFAULT 0         NOT NULL,
//    replycnt                              NUMBER(7)         DEFAULT 0         NOT NULL,
//    passwd                                VARCHAR2(100)         NOT NULL,
//    word                                  VARCHAR2(200)         NULL ,
//    rdate                                 DATE               NOT NULL,
//    file1                                   VARCHAR(100)          NULL,  -- 원본 파일명 image
//    file1saved                            VARCHAR(100)          NULL,  -- 저장된 파일명, image
//    thumb1                              VARCHAR(100)          NULL,   -- preview image
//    size1                                 NUMBER(10)      DEFAULT 0 NULL,  -- 파일 사이즈
//    map                                   VARCHAR2(1000)            NULL,
//    youtube                               VARCHAR2(1000)            NULL,
//    mp4                                  VARCHAR2(100)            NULL,
//    visible                                CHAR(1)         DEFAULT 'Y' NOT NULL,
//    FOREIGN KEY (reviewerno) REFERENCES reviewer (reviewerno),
//    FOREIGN KEY (bookno) REFERENCES books (bookno)
//);


@Getter
@Setter
@ToString
public class RecordVO {
    /** 독서기록 번호 */
    private int recordno;

    /** 리뷰 작성자 번호 (FK) */
    private int reviewerno;

    /** 책 번호 (FK) */
    private int bookno;

    /** 제목 */
    private String title = "";

    /** 기록 내용 */
    private String record = "";

    /** 추천 수 */
    private int recom = 0;

    /** 조회 수 */
    private int cnt = 0;

    /** 댓글 수 */
    private int replycnt = 0;

    /** 패스워드 */
    private String passwd = "";

    /** 검색어 */
    private String word = "";

    /** 등록일 */
    private String rdate = "";

    /** 지도 위치 */
    private String map = "";

    /** 유튜브 링크 */
    private String youtube = "";

    /** mp4 영상 링크 */
    private String mp4 = "";

    /** 출력 여부 */
    private String visible = "Y";

    // 파일 업로드 관련 --------------------------
    /** 업로드 파일 */
    private MultipartFile file1MF = null;

    /** 파일 크기 텍스트 (예: 1.2 MB) */
    private String size1_label = "";

    /** 업로드된 원본 파일 이름 */
    private String file1 = "";

    /** 실제 저장된 파일 이름 */
    private String file1saved = "";

    /** 썸네일 파일 이름 */
    private String thumb1 = "";

    /** 파일 크기 (bytes) */
    private long size1 = 0;
}
