DROP TABLE books;
DROP TABLE books CASCADE CONSTRAINTS; 

CREATE TABLE books (
    bookno          NUMBER(8)     NOT NULL  PRIMARY KEY,
    title         VARCHAR(255)  NOT NULL,
    author          VARCHAR(100)  NOT NULL,
    cnt            NUMBER(7)     DEFAULT 0     NOT NULL,
    publisher     VARCHAR(100)  NOT NULL,
    published_date  DATE          NOT NULL,
    created_at      DATE          NOT NULL,
    seqno           NUMBER(5)       DEFAULT 1       NOT NULL,
    VISIBLE         CHAR(1)         DEFAULT 'N'    NOT NULL
  );


COMMENT ON TABLE books is '책정보';
COMMENT ON COLUMN books.bookno IS '책 번호';
COMMENT ON COLUMN books.title IS '책 제목';
COMMENT ON COLUMN books.author IS '저자';
COMMENT ON COLUMN books.publisher IS '출판사';
COMMENT ON COLUMN books.published_date IS '출판일';
COMMENT ON COLUMN books.created_at IS '등록일';
COMMENT ON COLUMN books.seqno IS '출력순서';
COMMENT ON COLUMN books.VISIBLE IS '출력모드';

DROP SEQUENCE BOOKS_SEQ;

CREATE SEQUENCE BOOKS_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 99999999  -- 최대값: 9999999999 --> NUMBER(8) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

 COMMIT;
 --> CREATE 
INSERT INTO books(bookno, title, author, publisher, published_date, created_at)
VALUES(BOOKS_SEQ.nextval, '데일 카네기 인간관계론', '데일 카네기', '현대지성', TO_DATE('2019-10-07', 'YYYY-MM-DD'), SYSDATE);

INSERT INTO books(bookno, title, author, publisher, published_date, created_at)
VALUES (BOOKS_SEQ.nextval, '초격차', '김승호', '미래의 창', TO_DATE('2020-09-01', 'YYYY-MM-DD'), SYSDATE);

INSERT INTO books(bookno, title, author, publisher, published_date, created_at)
VALUES (BOOKS_SEQ.nextval, '하버드 상위 1퍼센트의 비밀', '에이미 졸리', '어드벤처', TO_DATE('2021-05-13', 'YYYY-MM-DD'), SYSDATE);

INSERT INTO books(bookno, title, author, publisher, published_date, created_at)
VALUES (BOOKS_SEQ.nextval, '죽음의 수용소에서', '빅터 프랭클', '정신세계', TO_DATE('2006-01-25', 'YYYY-MM-DD'), SYSDATE);

INSERT INTO books(bookno, title, author, publisher, published_date, created_at)
VALUES (BOOKS_SEQ.nextval, '인간 실격', '다자이 오사무', '문학동네', TO_DATE('1948-06-10', 'YYYY-MM-DD'), SYSDATE);

COMMIT;

