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
    private Integer storeid;
    private String storename;//门店名称
    private Integer status;//审核状态0未审核 1审核通过 2审核不通过
    private String statusCn;
    private Integer isdelete;//是否删除0否1是
    private Integer online;//是否在线1是 0否
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date bindtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date audittime;

    public String getStatusCn() {
        if(this.getStatus()==0){
            statusCn="未审核";
        }else if(this.getStatus()==1){
            statusCn="审核通过";
        }else {
            statusCn="审核不通过";
        }
        return statusCn;
    }



}
