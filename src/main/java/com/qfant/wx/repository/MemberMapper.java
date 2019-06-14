package com.qfant.wx.repository;

import com.qfant.wx.entity.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    @Select("SELECT * FROM Member WHERE id = #{id}")
    Member getMemberById(int id);

    @Insert("INSERT INTO member(name, phone,idcard,gender,birthday,openid,bonus,balance,cardno,cardcode,createtime) " +
            "VALUES (#{name}, #{phone},#{idcard}, #{gender},#{birthday}, #{openid},#{bonus}, #{balance},#{cardno}, #{cardcode},#{createtime})")
    void insert(Member member);
}
