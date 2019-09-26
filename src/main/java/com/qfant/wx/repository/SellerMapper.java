package com.qfant.wx.repository;

import com.qfant.wx.entity.Seller;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SellerMapper {
    @Results({
            @Result(property = "cardno",column = "cardno"),
            @Result(property = "name",column = "name"),
            @Result(property = "createtime",column = "createtime")
            })


    @Select("SELECT * FROM Seller WHERE openid = #{openId}")
    Seller getSellerByopenId(String openId);

    @Update("update seller set storeid=#{storeid} where id=#{id}")
    void update(Seller seller);






}
