package com.qfant.admin;

import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{


    private WxMpService wxMpService = new WxMpServiceImpl();
    private WxPayService wxPayService = new WxPayServiceImpl();
    @GetMapping()
    public String menu(){
        return "member/member";
    }


}
