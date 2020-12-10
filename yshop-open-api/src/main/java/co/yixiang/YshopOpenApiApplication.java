package co.yixiang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"co.yixiang.modules.*.mapper"})
public class YshopOpenApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YshopOpenApiApplication.class, args);
    }

}
