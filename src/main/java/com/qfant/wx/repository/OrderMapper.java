package com.qfant.wx.repository;

import com.qfant.wx.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM corder WHERE id = #{id}")
    Order getOrderById(int id);
    @Select("SELECT * FROM corder WHERE orderno = #{orderno}")
    Order getOrderByOrderno(String orderno);

    @Select("SELECT * FROM corder WHERE openid = #{openId} order by submittime desc")
    List<Order> getOrderByOpenId(String openId);

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO corder(orderno,openid,submittime,ip,price,resultcode,errcode,errcodedes,transactionid,timeend,notifytime,status)" +
            "VALUES (#{orderno},#{openid},#{submittime},#{ip},#{price},#{resultcode},#{errcode},#{errcodedes},#{transactionid}, #{timeend},#{notifytime},#{status})")
    void insert(Order order);

    @Update("update corder set orderno=#{orderno}, openid=#{openid},submittime=#{submittime},ip=#{ip},price=#{price},resultcode=#{resultcode},errcode=#{errcode}" +
            ",errcodedes=#{errcodedes},transactionid=#{transactionid},timeend=#{timeend},notifytime=#{notifytime},status=#{status} where id=#{id}")
    void update(Order order);
}
