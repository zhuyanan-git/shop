package com.kmm;

import com.kmm.utils.Md5Util;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import static com.kmm.login.controller.LoginController.encryptPassword;

@SpringBootApplication
@Configuration
public class HtgyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HtgyApplication.class, args);
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
        String password = Md5Util.string2MD5("123456");
        String encryptPassword=encryptPassword("admin","123456",password);
        System.out.println("password-------"+encryptPassword);
        System.out.println("salt-----"+password);
    }

}
