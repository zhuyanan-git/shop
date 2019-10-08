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
            @Result(property = "status",column = "status"),
            @Result(property = "type",column = "type"),
            @Result(property = "storeid",column = "storeid"),
            @Result(property = "isnotice",column = "isnotice"),
            @Result(property = "storename",column = "storename"),
            @Result(property = "noticetime",column = "noticetime")
    })


    @Select("SELECT * FROM corder WHERE id = #{id}")
    Order getOrderById(int id);
    @Select("SELECT * FROM corder WHERE orderno = #{orderno}")
    Order getOrderByOrderno(String orderno);

    @Select("SELECT * FROM corder WHERE openid = #{openId} order by submittime desc")
    List<Order> getOrderByOpenId(String openId);

    @Options(useGeneratedKeys = true)
    @Insert("INSERT INTO corder(orderno,openid,submittime,ip,price,resultcode,errcode,errcodedes,transactionid,timeend,notifytime,status,type,storeid,isnotice,storename,noticetime)" +
            "VALUES (#{orderno},#{openid},#{submittime},#{ip},#{price},#{resultcode},#{errcode},#{errcodedes},#{transactionid}, #{timeend},#{notifytime},#{status},#{type},#{storeid},#{isnotice},#{storename},#{noticetime})")
    void insert(Order order);

    @Update("update corder set orderno=#{orderno}, openid=#{openid},submittime=#{submittime},ip=#{ip},price=#{price},resultcode=#{resultcode},errcode=#{errcode}" +
            ",errcodedes=#{errcodedes},transactionid=#{transactionid},timeend=#{timeend},notifytime=#{notifytime},status=#{status},type=#{type},storeid=#{storeid},isnotice=#{isnotice},storename=#{storename},noticetime=#{noticetime},outrefundno=#{outrefundno},refundtime=#{refundtime},refundid=#{refundid},refunduser=#{refunduser} where id=#{id}")
    void update(Order order);

    @Select("<script> select " +
            "c.id, m.name, m.phone,m.openid,m.bonus,m.balance,m.cardno,c.orderno,c.submittime,c.price,c.status,c.storename from member as m,corder as c where m.openid = c.openid " +
            "<if test='cardno!=null'> and m.cardno like concat('%',#{cardno},'%') </if>"+
            "<if test='ordertype!=null'> and c.type = #{ordertype}</if>"+
            "<if test='name!=null'> and m.name like concat('%',#{name},'%') order by submittime desc</if>"+
//            "<if test='cardno==null and name==null'> and m.name is not null order by submittime desc</if>"+
            "<if test='start!=null and pageSize!=null'>limit #{start},#{pageSize}</if>"+
            " </script>")
    List<MemberAndOrder> selectAllOrder(MemberAndOrder memberAndOrder);

    @Select("<script> select id,openid,orderno,submittime,price,status,storename,outrefundno,refundtime,refundid,refunduser from corder where status=1 in(1,3)" +
            "<if test='type!=null'> and type=#{type}</if>" +
            "<if test='orderno!=null and orderno!=\"\" '> and orderno=#{orderno}</if>" +
            "<if test='startTime!=null and endTime!=null'>and submittime BETWEEN #{startTime} and #{endTime}</if>" +
            "<if test='start!=null and pageSize!=null'>limit #{start},#{pageSize}</if>"+
            " </script>")
    List<Order> selectList(Order order);

    @Select("<script> select count(*) from corder where status in(1,3)" +
            "<if test='type!=null'> and type=#{type}</if>" +
            "<if test='orderno!=null and orderno!=\"\" '> and orderno=#{orderno}</if>" +
            "<if test='startTime!=null and endTime!=null'>and submittime BETWEEN #{startTime} and #{endTime}</if>" +
            " </script>")
    Integer getTotal(Order order);


    @Select("select * from corder where id=#{id}")
    Order selectById(Integer id);
}
