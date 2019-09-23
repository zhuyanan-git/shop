package com.qfant.admin;

import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.qfant.utils.page.TableDataInfo;
import com.qfant.wx.entity.Member;
import com.qfant.wx.entity.Menu;
import com.qfant.wx.service.MenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{


    @Autowired
    private MenuService menuService;

    private WxMpService wxMpService = new WxMpServiceImpl();
    private WxPayService wxPayService = new WxPayServiceImpl();
    @GetMapping()
    public String menu(){
        return "menu/menu";
    }

    //查询会员信息
    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value="page",required=false)Integer page, @RequestParam(value="limit",required=false)Integer limit, Menu menu){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Integer count = menuService.getTotal();
        List<Menu> menuList = menuService.selecrAllMenu(page,limit);
        resultMap.put("code",0);
        resultMap.put("count",count);
        resultMap.put("data",menuList);
        return resultMap;
    }

}
