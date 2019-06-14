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
    private String status;
    private String cardcode;
    private String cardno;
    private double bonus;
    private double balance;
    private Date createtime;
}
