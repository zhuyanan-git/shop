package com.qfant.wx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class Reply {

    private Integer id;
    private String keyword;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    private Integer start;//分页起始页
    private Integer pageSize;//分页总条数



}
