package com.qfant.wx.service;


import com.qfant.wx.entity.Order;
import com.qfant.wx.repository.OrderMapper;
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
}
