package com.qfant.wx.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private Integer id;
    private String orderno;
    private String openid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submittime;
    private String ip;
    private Double price;
    private String resultcode;
    private String errcode;
    private String errcodedes;
    private String transactionid;
    private String timeend;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notifytime;
    private int status ;//0 未支付 1 支付成功 2 支付失败
    private int type ;//订单类型 1充值 2收款码支付
    private int storeid ;//门店id
    private int isnotice ;//是否通知门店0否1是
    private String storename ;//门店
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date noticetime;
}
