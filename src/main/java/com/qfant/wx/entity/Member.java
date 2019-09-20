package com.qfant.wx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qfant.framework.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class Member{
    @Excel(name= "id")
    private Integer id;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "电话")
    private String phone;
    @Excel(name = "性别")
    private String gender;
    @Excel(name = "生日")
    private String birthday;
    @Excel(name = "会员卡编号")
    private String idcard;
    @Excel(name = "openid")
    private String openid;
    @Excel(name = "昵称")
    private String nickname;
    @Excel(name = "状态")
    private String status;
    @Excel(name = "微信慧眼卡号")
    private String cardcode;//微信慧眼卡号
    @Excel(name = "会员卡号")
    private String cardno;//管家婆会员卡号
    @Excel(name = "积分")
    private double bonus;
    @Excel(name = "余额")
    private double balance;
    @Excel(name = "类型" ,readConverterExp = "1=新会员,2=老会员")
    private Integer type;//类型1新会员 2 老会员
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "绑定时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bindtime;



}
