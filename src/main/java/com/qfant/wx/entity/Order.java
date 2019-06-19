package com.qfant.wx.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private Integer id;
    private String orderno;
    private String openid;
    private Date submittime;
    private String ip;
    private Double price;
    private String resultcode;
    private String errcode;
    private String errcodedes;
    private String transactionid;
    private String timeend;
    private Date notifytime;
    private int status;//0 未支付 1 支付成功 2 支付失败
}
