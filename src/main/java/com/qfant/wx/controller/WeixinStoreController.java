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
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    private Environment env;

    @GetMapping("/bindstore")
    public String Store(String code, ModelMap mmap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url = init(code, "bindstore", request, response);
            if (StringUtils.isNotEmpty(url)) {
                return url;
            }
        }
        Seller seller = sellerService.getSellerByOPenId(session.getAttribute("openId").toString());
        if (seller != null) {
            return "forward:/wx/store/bindDetail?sellerId="+seller.getId();
        } else {
            List<Store> list = storeService.selectStoreList();
            mmap.put("openId", session.getAttribute("openId").toString());
            mmap.put("list", list);
            mmap.put("seller", new Seller());
            return "wx/bindStore";
        }

    }
    @GetMapping("/bindedit")
    public String bindedit(String code, ModelMap mmap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String url;
        if (session.getAttribute("openId") == null || StringUtils.isEmpty(session.getAttribute("openId").toString())) {
            url = init(code, "bindstore", request, response);
            if (StringUtils.isNotEmpty(url)) {
                return url;
            }
        }
        Seller seller = sellerService.getSellerByOPenId(session.getAttribute("openId").toString());
        List<Store> list = storeService.selectStoreList();
        mmap.put("openId", session.getAttribute("openId").toString());
        mmap.put("list", list);
        mmap.put("seller", seller);
        return "wx/bindStore";
    }
    @GetMapping("/bindDetail")
    public String bindDetail(Integer sellerId,ModelMap mmap) {
        Seller seller=sellerService.getSellerById(sellerId);
        Store store=storeService.selectStoreById(seller.getStoreid());
        mmap.put("seller",seller);
        mmap.put("store",store);
        return "wx/bindDetail";
    }
    @GetMapping("/bindsuccess")
    public String Success(){

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
                String url = wxService.oauth2buildAuthorizationUrl(env.getProperty("payment.domain") + "/wx/store/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
                return "redirect:" + url;
            }
        } else {
            String url = wxService.oauth2buildAuthorizationUrl(env.getProperty("payment.domain") + "/wx/store/" + method, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
            return "redirect:" + url;
        }
    }

    @PostMapping("/bind")
    @ResponseBody
    public Map bind(Integer storeId, String openId, String name, String phone) {
        Map result = new HashMap();
        if (storeId==null || Strings.isNullOrEmpty(openId) || Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(phone)) {
            result.put("code", -1);
        } else {
            Seller seller = sellerService.getSellerByOPenId(openId);
            if(seller==null){
                seller=new Seller();
                seller.setBindtime(new Date());
                seller.setOpenid(openId);
                seller.setName(name);
                seller.setPhone(phone);
                seller.setStoreid(storeId);
                sellerService.insertSeller(seller);
            }else {
                seller.setBindtime(new Date());
                seller.setOpenid(openId);
                seller.setName(name);
                seller.setPhone(phone);
                seller.setStoreid(storeId);
                sellerService.updateSeller(seller);
            }
            result.put("sellerId", seller.getId());
            result.put("code", 0);
        }
        return result;
    }

}
