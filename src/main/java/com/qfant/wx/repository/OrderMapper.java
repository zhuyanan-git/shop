package com.qfant.wx.repository;

import com.qfant.wx.entity.MemberAndOrder;
import com.qfant.wx.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Results({
            @Result(property = "name",column = "name"),
            @Result(property = "phone",column = "phone"),
            @Result(property = "openid",column = "openid"),
            @Result(property = "bonus",column = "bonus"),
            @Result(property = "balance",column = "balance"),
            @Result(property = "cardno",column = "cardno"),
            @Result(property = "orderno",column = "orderno"),
            @Result(property = "submittime",column = "submittime"),
            @Result(property = "price",column = "price"),
            @Result(property = "status",column = "status")
    })


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

    @Select("<script> select " +
            "m.id, m.name, m.phone,m.openid,m.bonus,m.balance,m.cardno,c.orderno,c.submittime,c.price,c.status " +
            "from member m , corder c " +
            "where m.openid = c.openid " +
            "<if test='cardno!=null'> and m.cardno like concat('%',#{cardno},'%') </if>"+
            "<if test='name!=null'> and m.name like concat('%',#{name},'%') order by submittime desc</if>"+
            "<if test='cardno==null and name==null'> and m.name is not null order by submittime desc</if>"+
            " </script>")
    List<MemberAndOrder> selectAllOrder(MemberAndOrder memberAndOrder);


}
