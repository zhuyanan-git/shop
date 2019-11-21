package com.kmm.goods.repository;

import com.kmm.goods.entity.Goods;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface GoodsMapper {
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "goods_name",property = "goodsName"),
            @Result(column = "store_id",property = "storeId"),
            @Result(column = "store_name",property = "storeName"),
            @Result(column = "category_id",property = "categoryId"),
            @Result(column = "category_name",property = "categoryName"),
            @Result(column = "brand_id",property = "brandId"),
            @Result(column = "brand_name",property = "brandName"),
            @Result(column = "old_price",property = "oldPrice"),
            @Result(column = "new_price",property = "newPrice"),
            @Result(column = "stock",property = "stock"),
            @Result(column = "status",property = "status"),
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "goods_img",property = "goodsImg"),
            @Result(column = "buy_number",property = "buyNumber"),
            @Result(column = "goods_description",property = "goodsDescription")

    })
    @Select("select g.id,g.goods_name,g.store_id,s.store_name,g.category_id,c.category_name,g.brand_id,b.brand_name,g.old_price," +
            "g.new_price,g.stock,g.status,g.create_time,g.update_time,g.goods_img,g.buy_number,g.goods_description " +
            "from goods as g,store as s,category as c,brand as b where g.store_id=s.id and g.category_id=c.id and g.brand_id=b.id " +
            "and store_id=#{storeId} limit #{start},#{pageSize}")
    List<Goods> selectGoodsList(Goods goods);
    @Select("select count(*) from goods where store_id=#{storeId}")
    Integer getTotal(Goods goods);
}
