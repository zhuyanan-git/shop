package com.qfant.wx.service;

import com.qfant.gjp.entity.VipCard;
import com.qfant.gjp.repository.VipCardMapper;
import com.qfant.utils.UUIDUtils;
import com.qfant.wx.entity.Seller;
import com.qfant.wx.repository.SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SellerService {
    @Autowired
    private SellerMapper sellerMapper;

    public Seller getSellerByOPenId(String openId){
        return sellerMapper.getSellerByopenId(openId);
    }

    @Transactional
    public void updateSeller(Seller seller) {
        sellerMapper.update(seller);
    }


}
