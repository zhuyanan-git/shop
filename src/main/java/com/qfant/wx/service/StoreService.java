package com.qfant.wx.service;

import com.qfant.wx.entity.Store;
import com.qfant.wx.repository.StoreMapper;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreMapper storeMapper;

    public List<Store> selectStoreAll(Integer page, Integer pageSize){ return storeMapper.selectStoreAll(page,pageSize);}

    public List<Store> selectStoreList(){ return storeMapper.selectStoreList();}

    public Integer getStoreTotal(){return storeMapper.getStoreTotal();}

    public void insertStore(Store store){storeMapper.insertStore(store);}

    public void deleteStore(Integer id){storeMapper.deleteStore(id);}

    public Store selectStoreById(Integer id){
        return storeMapper.selectStoreById(id);
    }

    public void update(Store store){
        storeMapper.update(store);
    }
}
