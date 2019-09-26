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

    public List<Seller> selectSellerAll(Integer page,Integer pageSize){ return sellerMapper.selectSellerAll(page,pageSize);}

    public Integer getSellerTotal(Integer isdelete){return sellerMapper.getSellerTotal(isdelete);}

    public void deleteSellerById(Integer id){sellerMapper.deleteSellerById(id);}

    public void editIsdeleteById(Integer id,Integer isdelete){sellerMapper.editIsdeleteById(id,isdelete);}

    public Seller getSellerById(Integer id){return sellerMapper.getSellerById(id);}

    public void updatePass(Seller seller){sellerMapper.updatePass(seller);}

    public void updateNopass(Seller seller){sellerMapper.updateNopass(seller);}

    public Seller getSellerByOPenId(String openId){ return sellerMapper.getSellerByopenId(openId); }

    public void updateSeller(Seller seller) { sellerMapper.update(seller); }


}
