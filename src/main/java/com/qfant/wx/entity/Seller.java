package com.qfant.wx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Seller{
    private Integer id;
    private String name;
    private String phone;
    private String openid;
    private String storeid;
    private String storename;//门店名称
    private String status;
    private Integer isdelete;//是否删除0否1是
    private Integer online;//是否在线1是 0否
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date bindtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date audittime;
}
