package com.qfant.admin;

import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.collect.Lists;
import com.qfant.wx.entity.Menu;
import com.qfant.wx.service.MenuService;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
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

    private final WxMpService wxService;
    private WxPayService wxPayService = new WxPayServiceImpl();

    public MenuController(WxMpService wxService) {
        this.wxService = wxService;
    }

    @GetMapping()
    public String menu(){
        return "menu/menu";
    }

    //查询会员信息
    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value="page",required=false)Integer page,
                                   @RequestParam(value="limit",required=false)Integer limit, Menu menu){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Integer count = menuService.getTotal();
        List<Menu> menuList = menuService.selectAllMenu((page-1)*limit,limit);
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
    public String add(ModelMap mmap){
        List<Menu> menus=menuService.getMenuByPid(0);
        mmap.put("menus",menus);
        return "menu/add";
    }

    /**
     * 跳转到修改页面
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Integer id, ModelMap mmap){
        Menu menu = menuService.getMenuById(id);
        mmap.put("menu",menu);
        List<Menu> menus=menuService.getMenuByPid(0);
        mmap.put("menus",menus);
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
     *生成菜单方法
     * @return
     */
    @GetMapping("/generate")
    @ResponseBody
    public Map<String,Object> generate() throws WxErrorException {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        List<Menu> menus=menuService.getMenuByPid(0);
        List<WxMenuButton> menuButtons= Lists.newArrayList();
        menus.stream().forEach(v->{
            WxMenuButton wxMenuButton=setMenu(v);
            List<Menu> children=menuService.getMenuByPid(v.getId());
            List<WxMenuButton> cmenuButtons=Lists.newArrayList();
            if (children!=null&&children.size()>0){
                cmenuButtons.clear();
                children.stream().forEach(c->{
                    WxMenuButton cwxMenuButton=setMenu(c);
                    cmenuButtons.add(cwxMenuButton);
                });
            }
            wxMenuButton.setSubButtons(cmenuButtons);
            menuButtons.add(wxMenuButton);
        });
        WxMenu wxMenu = new WxMenu();
        wxMenu.setButtons(menuButtons);
        wxService.getMenuService().menuCreate(wxMenu);
        resultMap.put("success",0);
        return resultMap;
    }

    private WxMenuButton setMenu(Menu menu){
        WxMenuButton wxMenuButton=new WxMenuButton();
        wxMenuButton.setName(menu.getName());
        if(menu.getType()==1){
            wxMenuButton.setKey(menu.getKeyword());
            wxMenuButton.setType("click");
        }else if (menu.getType()==2){
            wxMenuButton.setUrl(menu.getUrl());
            wxMenuButton.setType("view");
        }else {
            wxMenuButton.setPagePath(menu.getPagepath());
            wxMenuButton.setAppId(menu.getAppid());
            wxMenuButton.setType("miniprogram");
        }
        return wxMenuButton;
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
    public Map<String,Object> delete(Integer id){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        menuService.deleteById(id);
        resultMap.put("success",true);
        return resultMap;
    }





}
