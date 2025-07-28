DROP TABLE reviewer

CREATE TABLE reviewer (
  reviewerno NUMBER(10) NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
  reviewerid         VARCHAR(30)   NOT NULL UNIQUE, -- 이메일(아이디), 중복 안됨, 레코드를 구분 
  reviewerpasswd     VARCHAR(200)   NOT NULL, -- 패스워드, 영숫자 조합, 암호화
  reviewername      VARCHAR(30)   NOT NULL, -- 성명, 한글 10자 저장 가능
  phonenumber            VARCHAR(14)   NOT NULL, -- 전화번호
  housecode     VARCHAR(5)        NULL, -- 우편번호, 12345
  address1    VARCHAR(80)       NULL, -- 주소 1
  address2    VARCHAR(50)       NULL, -- 주소 2
  rdate       DATE             NOT NULL, -- 가입일    
  grade        NUMBER(2)     NOT NULL, -- 등급(1~10: 관리자, 11~20: 회원, 40~49: 정지 회원, 99: 탈퇴 회원)
  PRIMARY KEY (reviewerno)               -- 한번 등록된 값은 중복 안됨
);
 
COMMENT ON TABLE reviewer IS '회원 테이블';
COMMENT ON COLUMN reviewer.reviewerno IS '회원 번호, 레코드를 구분하는 기본키';
COMMENT ON COLUMN reviewer.reviewerid IS '이메일(아이디), 중복 불가';
COMMENT ON COLUMN reviewer.reviewerpasswd IS '비밀번호, 암호화 저장';
COMMENT ON COLUMN reviewer.reviewername IS '회원 성명';
COMMENT ON COLUMN reviewer.phonenumber IS '전화번호';
COMMENT ON COLUMN reviewer.housecode IS '우편번호';
COMMENT ON COLUMN reviewer.address1 IS '주소 1 (기본 주소)';
COMMENT ON COLUMN reviewer.address2 IS '주소 2 (상세 주소)';
COMMENT ON COLUMN reviewer.rdate IS '가입일';
COMMENT ON COLUMN reviewer.grade IS '회원 등급';

DROP SEQUENCE reviewer_seq;

CREATE SEQUENCE reviewer_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
 
 COMMIT;
SELECT COUNT(reviewerid) AS cnt
FROM reviewer
WHERE reviewerid = 'user1@gmail.com';

-- 관리자 계정
INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'admin', '1234', '통합 관리자', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 1);

-- QA 관리자 계정
INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'qnaadmin', '1234', '질문답변관리자', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 1);

-- 일반 회원
INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'user1@gmail.com', '1234', '왕눈이', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 15);

INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'user2@gmail.com', '1234', '아로미', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 15);

INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'user3@gmail.com', '1234', '투투투', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 15);

-- 팀 계정
INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'team1', '1234', '개발팀', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 15);

INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'team2', '1234', '웹퍼블리셔팀', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 15);

INSERT INTO reviewer(reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade)
VALUES (reviewer_seq.NEXTVAL, 'team3', '1234', '디자인팀', '000-0000-0000', '12345', '서울시 종로구', '관철동', SYSDATE, 15);

COMMIT;

SELECT reviewerno, reviewerid, reviewerpasswd, reviewername, phonenumber, housecode, address1, address2, rdate, grade
FROM reviewer
ORDER BY grade ASC, reviewerid ASC;

-- 번호로 조회
SELECT *
FROM reviewer
WHERE reviewerno = 1;

-- ID로 조회
SELECT *
FROM reviewer
WHERE reviewerid = 'user1@gmail.com';

UPDATE reviewer 
SET reviewername = '조인성',
    phonenumber = '111-1111-1111',
    housecode = '00000',
    address1 = '강원도',
    address2 = '홍천군',
    grade = 14
WHERE reviewerno = 12;

COMMIT;

-- 전체 삭제
DELETE FROM reviewer;

-- 특정 회원 삭제
DELETE FROM reviewer
WHERE reviewerno = 12;

COMMIT;

SELECT COUNT(*) AS cnt
FROM reviewer
WHERE reviewerid = 'user1@gmail.com'
AND reviewerpasswd = '1234';

-- 현재 비밀번호 확인
SELECT COUNT(*) AS cnt
FROM reviewer
WHERE reviewerno = 1
  AND reviewerpasswd = '1234';

-- 변경
UPDATE reviewer
SET reviewerpasswd = '0000'
WHERE reviewerno = 1;

8.회원등급 변경
UPDATE reviewer
SET grade=1
WHERE reviewerno=1;

UPDATE reviewer
SET grade=40
WHERE reviewerno=5;

DELETE FROM reviewer
WHERE reviewerno=9

COMMIT;
select * from reviewer;
