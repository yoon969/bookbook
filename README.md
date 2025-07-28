# 📚 BookStory8 - 개인 도서관리 웹 프로젝트

> 책을 등록하고, 조회하고, 관리할 수 있는 간단한 도서 웹 애플리케이션입니다.  
> Spring Boot, Oracle DB, MyBatis 기반으로 제작되었습니다.

---

## ✅ 주요 기능

- 📖 책 등록 / 수정 / 삭제
- 🔍 도서 검색 (제목, 저자 등)
- 📂 도서 카테고리별 분류
- 📅 등록일 순 정렬
- 🧑‍💻 관리자 페이지 기능

---

## 💻 사용 기술 스택

| 분야 | 기술 |
|------|------|
| Backend | Java 17, Spring Boot, Spring MVC, MyBatis |
| Database | Oracle 11g |
| Frontend | HTML5, CSS3, Thymeleaf, JavaScript |
| 개발환경 | STS(Spring Tool Suite), Git, GitHub |
| 기타 | Lombok, Gradle, Log4j |

---

## 🛠 프로젝트 구조
'''
bookstory8/
├── src/
│ ├── main/
│ │ ├── java/dev/mvc/bookstory/ # 컨트롤러, 서비스, DAO
│ │ ├── resources/ # 설정파일 및 SQL 매퍼
│ │ └── webapp/WEB-INF/view/ # 뷰(Thymeleaf JSP 또는 HTML)
├── build.gradle / pom.xml
'''
