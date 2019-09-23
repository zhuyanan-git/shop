package com.qfant.wx.entity;

import lombok.Data;


/**
 * @author 乐乐
 * @site www.javanood.com
 * @create 2019-09-23 15:02
 */
@Data
public class Menu {

    private Integer id;//编号id

    private String name;//菜单名称

    private Integer type;// 类型 1click点击 2view 打开url 3miniprogram小程序

    private String keyword;//关键字

    private String url;//路径

    private String appid;//小程序appid

    private String pagepath;//小程序页面路径

    private Integer pid;//父菜单id

    private String typeName;

    private String pidName;

}
