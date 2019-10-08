package com.qfant.admin;

import com.qfant.wx.entity.MemberAndOrder;
import com.qfant.wx.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;
    @GetMapping
    public String order(){return "order/order";}

    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value = "page") Integer page, @RequestParam(value = "limit") Integer limit, MemberAndOrder memberAndOrder){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        memberAndOrder.setStart((page-1)*limit);
        memberAndOrder.setPageSize(limit);
        List<MemberAndOrder> orderList = orderService.selectAllOrder(memberAndOrder);
        Integer count = orderService.getTotalOrder(memberAndOrder);
        resultMap.put("code",0);
        resultMap.put("count",count);
        resultMap.put("data", orderList);
        return resultMap;
    }
}
