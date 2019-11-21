package com.kmm.goods.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Goods {

    private Integer id;
    private String goodsName;
    private Integer storeId;
    private String storeName;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;
    private Double oldPrice;
    private Double newPrice;
    private Integer stock;
    private Integer status;//0未审核，1待审核，2审核未通过，3审核通过
    private Date createTime;
    private Date updateTime;
    private String goodsImg;
    private Integer buyNumber;
    private String goodsDescription;

    private Integer start;//分页开始数

    private Integer pageSize;//分页大小
}
