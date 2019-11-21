package com.kmm.goods.service;

import com.kmm.goods.entity.Goods;
import com.kmm.goods.repository.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    public List<Goods> selectGoodsList(Goods goods){return goodsMapper.selectGoodsList(goods);}

    public Integer getTotal(Goods goods){return goodsMapper.getTotal(goods);}
}
