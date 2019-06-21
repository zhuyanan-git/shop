package com.qfant.gjp.repository;

import com.qfant.gjp.entity.VipCard;
import org.apache.ibatis.annotations.*;

@Mapper
public interface VipCardMapper {
    @Select("SELECT * FROM VipCard WHERE ID = #{id}")
    VipCard getVipCardById(int id);

    @Select("SELECT * FROM VipCard WHERE CardNo = #{CardNo}")
    VipCard getVipCardByCardNo(String CardNo);

    @Insert("INSERT INTO VipCard(CardNo,Name,Tel,CardType,Discount,BulidDate,ValidityDate,Lose,StopUse,BuyTotal,BuyTotalPerDay,AutoDiscount,membertotal,sex,birthday,IntegralTotal,Cz,IsLife,VIPBarCode) " +
            "VALUES (#{CardNo}, #{Name},#{Tel}, #{CardType},#{Discount}, #{BulidDate},#{ValidityDate}, #{Lose},#{StopUse}, #{BuyTotal},#{BuyTotalPerDay}, #{AutoDiscount},#{membertotal},#{sex}, #{birthday},#{IntegralTotal},#{Cz},#{IsLife},#{VIPBarCode})")
    int insert(VipCard vipCard);

    @Delete("SELECT * FROM VipCard WHERE CardNo = #{CardNo} and Tel=#{Tel}")
    void deleteVipCardByCardNo(String CardNo,String Tel);


    @Update("update VipCard set CardNo=#{CardNo}, Name=#{Name},Tel=#{Tel},CardType=#{CardType},Discount=#{Discount},BulidDate=#{BulidDate},ValidityDate=#{ValidityDate},Lose=#{Lose}" +
            ",StopUse=#{StopUse},BuyTotal=#{BuyTotal},BuyTotalPerDay=#{BuyTotalPerDay},AutoDiscount=#{AutoDiscount},membertotal=#{membertotal},sex=#{sex} ,birthday=#{birthday},IntegralTotal=#{IntegralTotal},Cz=#{Cz} ,IsLife=#{IsLife} where ID=#{ID}")
    void updateVipCard(VipCard vipCard);
}
