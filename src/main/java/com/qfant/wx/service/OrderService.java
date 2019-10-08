package com.qfant.wx.service;


import com.qfant.wx.entity.MemberAndOrder;
import com.qfant.wx.entity.Order;
import com.qfant.wx.repository.OrderMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    public void saveOrder(Order order) {
        orderMapper.insert(order);
    }
    public void updateOrder(Order order) {
        orderMapper.update(order);
    }
    public Order getOrderByOrderno(String orderno){
        return orderMapper.getOrderByOrderno(orderno);
    }

    public List<Order> getOrderByOpenId(String orderno){
        return orderMapper.getOrderByOpenId(orderno);
    }

    public List<MemberAndOrder> selectAllOrder( MemberAndOrder memberAndOrder){return orderMapper.selectAllOrder(memberAndOrder);}

    public Integer getTotalOrder(MemberAndOrder memberAndOrder){ return orderMapper.getTotalOrder(memberAndOrder);}

    public Integer getTotal(Order order){
       return orderMapper.getTotal(order);
    }

    public Order  selectById(Integer id){
        return orderMapper.selectById(id);
    }

    public List<Order> selectList(Order order){
        return  orderMapper.selectList(order);
    }

}
