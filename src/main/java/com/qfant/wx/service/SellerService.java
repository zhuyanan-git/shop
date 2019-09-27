package com.qfant.wx.service;

import com.qfant.wx.entity.Seller;
import com.qfant.wx.repository.SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SellerService {
    @Autowired
    private SellerMapper sellerMapper;

    public List<Seller> selectSellerAll(Integer start,Integer pageSize){ return sellerMapper.selectSellerAll(start,pageSize);}

    public Integer getSellerTotal(Integer isdelete){return sellerMapper.getSellerTotal(isdelete);}

    public void deleteSellerById(Integer id){sellerMapper.deleteSellerById(id);}

    public void editIsdeleteById(Integer id,Integer isdelete){sellerMapper.editIsdeleteById(id,isdelete);}

    public Seller getSellerById(Integer id){return sellerMapper.getSellerById(id);}

    public void updatePass(Integer id, Date audittime,Integer status){sellerMapper.updatePass(id,audittime,status);}

    public Seller getSellerByOPenId(String openId){ return sellerMapper.getSellerByopenId(openId); }

    public void updateSeller(Seller seller) { sellerMapper.update(seller); }

    public  void insertSeller(Seller seller){
        sellerMapper.insertSeller(seller);
    }


    public List<Seller> selectSellerByStoreId(Integer storeId){
        return sellerMapper.selectSellerByStoreId(storeId);
    }
}
