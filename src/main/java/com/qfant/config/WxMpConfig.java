package com.qfant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Configuration
public class WxMpConfig {
  @Value("${wx.mp.token}")
  private String token;

  @Value("${wx.mp.appId}")
  private String appid;

  @Value("${wx.mp.secret}")
  private String appSecret;

  @Value("${wx.mp.aesKey}")
  private String aesKey;

  public String getToken() {
    return this.token;
  }

  public String getAppid() {
    return this.appid;
  }

  public String getAppSecret() {
    return this.appSecret;
  }

  public String getAesKey() {
    return this.aesKey;
  }

}
