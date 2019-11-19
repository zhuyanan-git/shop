package com.kmm.login.controller;

import com.kmm.admin.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController{

    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        return "index";
    }
    @RequestMapping("/welcome")
    public String welcome(){return "welcome";}

    @RequestMapping("/userSetting")
    public String userSetting(){return "userInfo/userSetting";}
    @RequestMapping("/updatePassword")
    public String updatepassword(){return "userInfo/password";}


}