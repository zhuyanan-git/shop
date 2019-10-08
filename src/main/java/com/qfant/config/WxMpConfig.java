package com.qfant.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Configuration
@Data
public class WxMpConfig {
  @Value("${wx.mp.token}")
  private  String token;

  @Value("${wx.mp.appId}")
  private   String appid;

  @Value("${wx.mp.secret}")
  private  String appSecret;

  @Value("${wx.mp.aesKey}")
  private   String aesKey;

  @Value("${wx.pay.mchId}")
  private   String mchid;//商家id



}
