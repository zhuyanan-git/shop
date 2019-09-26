package com.qfant.wx.controller;

import com.github.binarywang.wxpay.service.WxPayService;
import com.google.common.base.Strings;
import com.qfant.utils.StringUtils;
import com.qfant.wx.entity.Seller;
import com.qfant.wx.entity.Store;
import com.qfant.wx.service.SellerService;
import com.qfant.wx.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping("/wx/store")
public class WeixinStoreController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WxMpService wxService;
    private final WxPayService wxPayService;
    @Autowired
    SellerService sellerService;
    @Autowired
    StoreService storeService;

    @GetMapping("/bindstore")
    public String Store(String code, ModelMap mmap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url =init(code, "bindstore", request, response);
            if(StringUtils.isNotEmpty(url)){
                return url;
            }
        }
        List<Store> list = storeService.selectStoreList();

        mmap.put("openId", session.getAttribute("openId").toString());
        mmap.put("list",list);
        return "wx/bindStore";
    }

    @GetMapping("/bindsuccess")
    public String Success() throws IOException {

        return "wx/bindsuccess";
    }

    private String init(String code, String method, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (code != null && !"".equals(code)) {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
            try {
                wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
                session.setAttribute("openId", wxMpOAuth2AccessToken.getOpenId());
                return null;
            } catch (WxErrorException e) {
                String url = wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/payment/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
                return "redirect:"+url;
            }
        } else {
            String url = wxService.oauth2buildAuthorizationUrl("http://htgy.qfant.com/wx/payment/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
            return "redirect:"+url;
        }
    }

    @PostMapping("/bind")
    @ResponseBody
    public Map bind(String storeId , String openId) throws WxErrorException {
        Map result = new HashMap();
        Seller seller = sellerService.getSellerByOPenId(openId);//获取mysql数据库会员信息
        if(seller != null){
            seller.setStoreid(storeId);
            sellerService.updateSeller(seller);
            result.put("code",0);
        }
        return result;
    }

}
