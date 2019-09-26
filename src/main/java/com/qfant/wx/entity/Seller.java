package com.qfant.wx.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class Seller{
    private Integer id;
    private String name;
    private String storename;
    private String phone;
    private String openid;
    private Integer storeid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date bindtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date audittime;//审核时间
    private Integer status;//审核状态0未审核 1审核通过 2审核不通过
    private Integer isdelete;//是否删除0否1是
    private Integer online;//是否在线1是 0否
}
