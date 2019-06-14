package com.qfant.gjp.repository;

import com.qfant.gjp.entity.VipCard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VipCardMapper {
    @Select("SELECT * FROM VipCard WHERE ID = #{id}")
    VipCard getVipCardById(int id);

    @Insert("INSERT INTO VipCard(CardNo,Name,Tel,CardType,Discount,BulidDate,ValidityDate,Lose,StopUse,BuyTotal,BuyTotalPerDay,AutoDiscount,membertotal,sex,birthday,IsLife,VIPBarCode) " +
            "VALUES (#{CardNo}, #{Name},#{Tel}, #{CardType},#{Discount}, #{BulidDate},#{ValidityDate}, #{Lose},#{StopUse}, #{BuyTotal},#{BuyTotalPerDay}, #{AutoDiscount},#{membertotal},#{sex}, #{birthday},#{IsLife},#{VIPBarCode})")
    int insert(VipCard vipCard);
}
