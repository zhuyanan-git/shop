package com.qfant.wx.entity;

import lombok.Data;
/**
 * 微信会员卡基本信息.
 * @author yuanqixun
 * date:2018-08-25 00:36
 */
@Data
public class BaseInfo{

  private Integer id;
  /**
   * 卡券的商户logo,建议像素为300*300.
   */
  private String logoUrl;

  /**
   * Code展示类型.
   * "CODE_TYPE_TEXT" 文本 "CODE_TYPE_BARCODE" 一维码 "CODE_TYPE_QRCODE" 二维码 "CODE_TYPE_ONLY_QRCODE" 仅显示二维码 "CODE_TYPE_ONLY_BARCODE" 仅显示一维码 "CODE_TYPE_NONE" 不显示任何码型
   */
  private String codeType = "CODE_TYPE_QRCODE";


  /**
   * 是否设置该会员卡中部的按钮同时支持微信支付刷卡和会员卡二维码.
   */
  private boolean isPayAndQrcode;

  /**
   * 商户名字,字数上限为12个汉字.
   */
  private String brandName;

  /**
   * 卡券名,字数上限为9个汉字 (建议涵盖卡券属性、服务及金额).
   */
  private String title;

  /**
   * 券颜色,按色彩规范标注填写Color010-Color100.
   */
  private String color;

  /**
   * 卡券使用提醒,字数上限为16个汉字.
   */
  private String notice;

  /**
   * 卡券使用说明,字数上限为1024个汉字.
   */
  private String description;

  /**
   * 是否自定义Code码,填写true或false.
   * 默认为false 通常自有优惠码系统的开发者选择自定义Code码，详情见 是否自定义code
   */
  private boolean useCustomCode;

  /**
   * 是否指定用户领取,填写true或false。默认为false.
   */
  private boolean bindOpenid;

  /**
   * 客服电话.
   */
  private String servicePhone;

  /**
   * 门店位置ID,调用 POI门店管理接口 获取门店位置ID.
   */
  private String locationIdList;

  /**
   * 会员卡是否支持全部门店,填写后商户门店更新时会自动同步至卡券.
   */
  private boolean useAllLocations = true;

  /**
   * 卡券中部居中的按钮,仅在卡券激活后且可用状态 时显示.
   */
  private String centerTitle;

  /**
   * 显示在入口下方的提示语,仅在卡券激活后且可用状态时显示.
   */
  private String centerSubTitle;

  /**
   * 顶部居中的url,仅在卡券激活后且可用状态时显示.
   */
  private String centerUrl;

  /**
   * 自定义跳转外链的入口名字.
   */
  private String customUrlName;

  /**
   * 自定义跳转的URL.
   */
  private String customUrl;

  /**
   * 显示在入口右侧的提示语.
   */
  private String customUrlSubTitle;

  /**
   * 营销场景的自定义入口名称.
   */
  private String promotionUrlName;

  /**
   * 入口跳转外链的地址链接.
   */
  private String promotionUrl;

  /**
   * 显示在营销入口右侧的提示语.
   */
  private String promotionUrlSubTitle;

  /**
   * 每人可领券的数量限制,建议会员卡每人限领一张.
   */
  private Integer getLimit = 1;

  /**
   * 卡券领取页面是否可分享,默认为true.
   */
  private boolean canShare;

  /**
   * 卡券是否可转赠,默认为true.
   */
  private boolean canGiveFriend;

  /**
   * 用户点击进入会员卡时推送事件.
   * 填写true为用户点击进入会员卡时推送事件，默认为false。详情见 进入会员卡事件推送
   */
  private boolean needPushOnView;

  private Integer cardId;
}
