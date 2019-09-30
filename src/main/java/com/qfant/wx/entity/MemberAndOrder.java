package com.qfant.wx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;



import java.util.Date;
@Data
public class MemberAndOrder {
    private Integer id;
    private String name;
    private String phone;
    private String openid;
    private String storename;
    private double bonus;
    private double balance;
    private String cardno;//管家婆会员卡号
    private String orderno;
    private Integer ordertype;//支付类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date submittime;
    private Double price;
    private int status;//0 未支付 1 支付成功 2 支付失败 3已退款

    private Integer start;//分页开始数

    private Integer pageSize;//分页大小
}
