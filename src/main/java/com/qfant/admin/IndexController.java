package com.qfant.admin;

import com.qfant.wx.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController{

    @Autowired
    private UserMapper userRepository;

    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        return "index";
    }

}