package com.qfant.admin;

import com.qfant.wx.entity.Seller;
import com.qfant.wx.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller")
public class SellerController extends BaseController{
    @Autowired
    private SellerService sellerService;
    //跳转查看店员信息页面
    @GetMapping()
    public String seller(){ return "seller/seller"; }

    //显示所有店员信息
    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value = "page") Integer page,
                                   @RequestParam(value="limit") Integer limit, Seller seller){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Integer count = sellerService.getSellerTotal(0);
        List<Seller> sellerList = sellerService.selectSellerAll((page-1)*limit,limit);
        resultMap.put("code",0);
        resultMap.put("count",count);
        resultMap.put("data",sellerList);
        return resultMap;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(Integer id){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        sellerService.deleteSellerById(id);
        resultMap.put("success",true);
        return resultMap;
    }
    @PostMapping("editIsdelete")
    @ResponseBody
    public Map<String,Object> editIsdelete(Integer id,Integer isdelete){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        sellerService.editIsdeleteById(id,isdelete);
        resultMap.put("success",true);
        return resultMap;
    }
    /**
     * 跳转到审核页面
     * @return
     */
    @RequestMapping("/edit")
    public String edit(Integer id, ModelMap mmap){
        Seller seller = sellerService.getSellerById(id);
        mmap.put("seller",seller);
        return "seller/edit";
    }

    /**
     * 审核通过
     * @param seller
     * @return
     */
    @RequestMapping("/pass")
    @ResponseBody
    public Map<String,Object> pass(Seller seller){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        sellerService.updatePass(seller);
        resultMap.put("success",true);
        return resultMap;
    }

    /**
     * 审核未通过
     * @param seller
     * @return
     */
    @RequestMapping("/nopass")
    @ResponseBody
    public Map<String,Object> nopass(Seller seller){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        sellerService.updateNopass(seller);
        resultMap.put("success",true);
        return resultMap;
    }
}
