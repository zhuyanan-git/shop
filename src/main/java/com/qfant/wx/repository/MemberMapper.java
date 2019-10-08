package com.qfant.wx.repository;

import com.qfant.wx.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Results({
            @Result(property = "cardno",column = "cardno"),
            @Result(property = "name",column = "name"),
            @Result(property = "createtime",column = "createtime")
            })
    @Select("SELECT * FROM Member WHERE id = #{id}")
    Member getMemberById(int id);

    @Insert("INSERT INTO member(name, phone,idcard,gender,birthday,openid,nickname,bonus,balance,status,cardno,cardcode,type,createtime,bindtime) " +
            "VALUES (#{name}, #{phone},#{idcard}, #{gender},#{birthday}, #{openid}, #{nickname},#{bonus}, #{balance},#{status},#{cardno}, #{cardcode},#{type},#{createtime},#{bindtime})")
    void insert(Member member);

    @Select("SELECT * FROM Member WHERE openid = #{openId}")
    Member getMemberByopenId(String openId);

    @Update("update member set name=#{name}, phone=#{phone},idcard=#{idcard},gender=#{gender},birthday=#{birthday},openid=#{openid},nickname=#{nickname},bonus=#{bonus}" +
            ",balance=#{balance},status=#{status},cardno=#{cardno},cardcode=#{cardcode},type=#{type},createtime=#{createtime} ,bindtime=#{bindtime} where id=#{id}")
    void update(Member member);


    @Select("<script> select * from member where name is not null" +
            " <if test='cardno!=null and cardno!=\"\"'> and cardno like concat('%',#{cardno},'%') </if>" +
            " <if test='name!=null and name!=\"\"'> and name like concat('%',#{name},'%') </if> " +
            " <if test='start!=null and pageSize!=null'>order by createtime desc limit #{start},#{pageSize}</if>"+
            "</script>")
    List<Member> selectMemberList(Member member);

    @Select("<script> select count(*) from member where name is not null" +
            " <if test='cardno!=null and cardno!=\"\"'> and cardno like concat('%',#{cardno},'%') </if>" +
            " <if test='name!=null and name!=\"\"'> and name like concat('%',#{name},'%')  order by createtime desc</if> " +
            "</script>")
    Integer getMemberTotal(Member member);

    @Select("select * from member where name is not null order by createtime desc")
    List<Member> exportMember(Member member);

}
