package com.qfant.wx.controller;

import com.qfant.wx.service.WeixinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wx/member")
public class MemberCardController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WeixinService wxService;
    @GetMapping("/bind")
    public String bind(){
        return "bind";
    }
}
