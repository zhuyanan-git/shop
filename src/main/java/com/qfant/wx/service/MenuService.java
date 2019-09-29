package com.qfant.wx.service;

import com.qfant.wx.entity.Menu;
import com.qfant.wx.repository.MenuMapper;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 乐乐
 * @site www.javanood.com
 * @create 2019-09-23 15:24
 */
@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;



    public void insertMenu(Menu menu){
        menuMapper.insert(menu);
    }

    public void updateMenu(Menu menu){
        menuMapper.update(menu);
    }


    public Menu getMenuById(Integer id){
        return  menuMapper.getMenuById(id);
    }

    public List<Menu> getMenuByPid(Integer pid){
        return  menuMapper.getMenuByPid(pid);
    }

    public List<Menu> selectAllMenu(Integer start,Integer pageSize){
        return menuMapper.selecrAllMenu(start,pageSize);
    }

    public Integer  getTotal(){
        return menuMapper.getTotal();
    }

    public  void deleteById(Integer id){
        menuMapper.deleteById(id);
    }

    public  List<Menu> selecrMenuList(){
        return menuMapper.selecrMenuList();
    }

    public  void deleteByPidId(Integer pid){
         menuMapper.deleteByPidId(pid);
    }

}
