package com.qfant.gjp.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class VipCard {
    private Integer ID;
    private String CardNo;
    private String Name;
    private String Tel;
    private Integer CardType=1;
    private Integer Discount=100;
    private Date BulidDate;
    private Date ValidityDate=new Date(2556115199L);
    private Integer Lose=0;
    private Integer StopUse=0;
    private BigDecimal BuyTotal=new BigDecimal(0);
    private BigDecimal IntegralTotal=new BigDecimal(0);//积分
    private BigDecimal BuyTotalPerDay=new BigDecimal(0);
    private BigDecimal Cz=new BigDecimal(0);//可用储值
    private Integer AutoDiscount=1;
    private BigDecimal membertotal=new BigDecimal(0);
    private Integer automembertotal=0;
    private String cardid;
    private Integer sex=0;
    private Timestamp birthday;
    private String IdentityCard;
    private String PYZJM;
    private Integer IsLife=1;
    private String VIPBarCode;
}
