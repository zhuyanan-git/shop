package com.qfant.wx.repository;

import com.qfant.wx.entity.Reply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReplyMapper {
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "keyword",column = "keyword"),
            @Result(property = "content",column = "content"),
            @Result(property = "createtime",column = "createtime")
            })
    @Select("SELECT * FROM Reply WHERE id = #{id}")
    Reply getReplyById(int id);

    @Insert("INSERT INTO Reply(keyword, content,createtime) VALUES (#{keyword}, #{content},#{createtime})")
    void insert(Reply reply);


    @Update("update Reply set keyword=#{keyword}, content=#{content},createtime=#{createtime} where id=#{id}")
    void update(Reply reply);


    @Select("<script> select * from reply" +
            " <if test='keyword!=null'>  where keyword like concat('%',#{keyword},'%') order by createtime desc </if>" +
            "<if test = 'start != null and pageSize != null'>limit #{start},#{pageSize}</if>"+
            "</script>")
    List<Reply> selectReplyList(Reply reply);
    @Select("<script> select count(*) from reply" +
            " <if test='keyword!=null'>  where keyword like concat('%',#{keyword},'%') order by createtime desc </if>" +
            "</script>")
    Integer getTotal(Reply reply);
    @Delete("delete from reply where id=#{id}")
    void deleteReplyById(Integer id);

    @Select("SELECT * FROM Reply WHERE keyword = #{keyword}")
    Reply getReplyByKeyword(String  keyword);
}
