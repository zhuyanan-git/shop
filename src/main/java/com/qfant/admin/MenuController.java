package com.qfant.admin;

import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.qfant.utils.page.TableDataInfo;
import com.qfant.wx.entity.Member;
import com.qfant.wx.entity.Menu;
import com.qfant.wx.service.MenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{


    @Autowired
    private MenuService menuService;

    private WxMpService wxMpService = new WxMpServiceImpl();
    private WxPayService wxPayService = new WxPayServiceImpl();
    @GetMapping()
    public String menu(){
        return "menu/menu";
    }

    //查询会员信息
    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value="page",required=false)Integer page, @RequestParam(value="limit",required=false)Integer limit, Menu menu){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Integer count = menuService.getTotal();
        List<Menu> menuList = menuService.selecrAllMenu(page-1,limit);
        resultMap.put("code",0);
        resultMap.put("count",count);
        resultMap.put("data",menuList);
        return resultMap;
    }

    /**
     * 跳转到添加页面
     * @return
     */
    @RequestMapping("/add")
    public String add(){
        return "menu/add";
    }

    /**
     * 跳转到修改页面
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Integer id, ModelMap mmap){
        Menu menu = menuService.getMenuById(id);
        if (menu.getType()==1){
            menu.setTypeName("网页类型");
        }else if (menu.getType()==2){
            menu.setTypeName("点击类型");
        }else {
            menu.setTypeName("小程序类型");
        }
        if (menu.getPid()==0){
            menu.setPidName("一级菜单");
        }else {
            menu.setPidName("二级菜单");
        }
        mmap.put("menu",menu);
        return "menu/edit";
    }


    /**
     *添加菜单方法
     * @param menu
     * @return
     */
    @PostMapping("/addSave")
    @ResponseBody
    public Map<String,Object> addSave(Menu menu){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        menuService.insertMenu(menu);
        resultMap.put("success",true);
        return resultMap;
    }

    /**
     *添加菜单方法
     * @param menu
     * @return
     */
    @PostMapping("/editSave")
    @ResponseBody
    public Map<String,Object> editSave(Menu menu){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        menuService.updateMenu(menu);
        resultMap.put("success",true);
        return resultMap;
    }

    /**
     *删除菜单方法
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Map<String,Object> addDelete(Integer id){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        menuService.deleteById(id);
        resultMap.put("success",true);
        return resultMap;
    }





}
