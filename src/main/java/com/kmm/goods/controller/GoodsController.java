package com.kmm.goods.controller;

import com.kmm.admin.BaseController;
import com.kmm.goods.entity.Goods;
import com.kmm.goods.service.GoodsService;
import com.kmm.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping
    public String goods(){return "goods/goods";}

    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value = "page") Integer page,
                                   @RequestParam(value = "limit") Integer limit, Goods goods, HttpSession session){
        User user = (User)session.getAttribute("user");
        System.out.println(user);
        goods.setStoreId(user.getId());
        Map<String,Object> resultMap = new HashMap<String, Object>();
        goods.setStart((page-1)*limit);
        goods.setPageSize(limit);
        List<Goods> goodsList =goodsService.selectGoodsList(goods);
        Integer count = goodsService.getTotal(goods);
        resultMap.put("code",0);
        resultMap.put("count",count);
        resultMap.put("data",goodsList);
        return resultMap;



    }

}
