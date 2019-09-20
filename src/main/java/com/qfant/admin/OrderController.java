package com.qfant.admin;

import com.qfant.utils.page.TableDataInfo;
import com.qfant.wx.entity.MemberAndOrder;
import com.qfant.wx.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;
    @GetMapping
    public String order(){return "order/order";}

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MemberAndOrder memberAndOrder){
        startPage();
        List<MemberAndOrder> list = orderService.selectAllOrder(memberAndOrder);

        return getDataTable(list);

    }
}
