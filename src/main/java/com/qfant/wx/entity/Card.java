package com.qfant.wx.entity;

import lombok.Data;


@Data
public class Card {

  private Integer id;

  private String cardJson;
  private String wxcardId;
//  /**
//   * 会员卡背景图.
//   */
//
//  private String backgroundPicUrl;
//
//  /**
//   * 特权说明.
//   */
//  private String prerogative;
//
//  /**
//   * 自动激活.
//   */
//  private boolean autoActivate;
//
//  /**
//   * 显示积分.
//   */
//  private boolean supplyBonus;
//
//  /**
//   * 查看积分外链,设置跳转外链查看积分详情。仅适用于积分无法通过激活接口同步的情况下使用该字段.
//   */
//  private String bonusUrl;
//
//  /**
//   * 支持储值.
//   */
//  private boolean supplyBalance;
//
//  /**
//   * 余额外链,仅适用于余额无法通过激活接口同步的情况下使用该字段.
//   */
//  private String balanceUrl;
//
//
//  /**
//   * 积分清零规则.
//   */
//  private String bonusCleared;
//
//  /**
//   * 积分规则.
//   */
//  private String bonusRules;
//
//  /**
//   * 储值规则.
//   */
//  private String balanceRules;
//
//  /**
//   * 激活会员卡的url.
//   */
//  private String activateUrl;
//
//  /**
//   * 激活会原卡url对应的小程序user_name，仅可跳转该公众号绑定的小程序.
//   */
//  private String activateAppBrandUserName;
//
//  /**
//   * 激活会原卡url对应的小程序path.
//   */
//  private String activateAppBrandPass;
//
//
//  /**
//   * 折扣,该会员卡享受的折扣优惠,填10就是九折.
//   */
//  private Integer discount;
//
//  /**
//   * 是否支持一键激活 ，填true或false.
//   */
//  private boolean wxActivate;
//
//  /**
//   * 是否支持跳转型一键激活，填true或false.
//   */
//  private boolean wxActivateAfterSubmit;
//
//  /**
//   * 跳转型一键激活跳转的地址链接，请填写http:// 或者https://开头的链接.
//   */
//  private String wxActivateAfterSubmitUrl;

}
