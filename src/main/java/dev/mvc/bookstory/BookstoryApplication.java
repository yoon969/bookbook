package dev.mvc.bookstory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.mvc"}) // ★★★★★★ 패키지 주의 ★★★★★★ 
public class BookstoryApplication{

    public static void main(String[] args) {
        SpringApplication.run(BookstoryApplication.class, args);
    }

}
