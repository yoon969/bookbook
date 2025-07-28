DROP TABLE memo;

CREATE TABLE memo (
  memono       NUMBER(10) NOT NULL PRIMARY KEY,
  memotitle    VARCHAR2(255) NOT NULL,
  memocontent  CLOB NOT NULL,
  created_date DATE DEFAULT SYSDATE
);

COMMENT ON TABLE memo is '메모';
COMMENT ON COLUMN memo.memono is '메모번호';
COMMENT ON COLUMN memo.memotitle is '메모제목';
COMMENT ON COLUMN memo.memocontent is '메모내용';
COMMENT ON COLUMN memo.created_date is '등록일시';

DROP SEQUENCE memo_seq;

CREATE SEQUENCE memo_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;                      -- 다시 1부터 생성되는 것을 방지
  
COMMIT;