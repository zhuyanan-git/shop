package com.qfant.wx.entity;

import lombok.Data;

import java.util.Date;
@Data
public class Member {
    private Integer id;
    private String name;
    private String phone;
    private String gender;
    private String birthday;
    private String idcard;
    private String openid;
    private String nickname;
    private String status;
    private String cardcode;//微信慧眼卡号
    private String cardno;//管家婆会员卡号
    private double bonus;
    private double balance;
    private Integer type;//类型1新会员 2 老会员
    private Date createtime;
    private Date bindtime;
}
