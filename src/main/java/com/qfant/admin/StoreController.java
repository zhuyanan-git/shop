package com.qfant.admin;

import com.qfant.wx.entity.Store;
import com.qfant.wx.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class StoreController extends BaseController{
    @Autowired
    private StoreService storeService;

    @GetMapping()
    public String store(){return "store/store";}

    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value = "page")Integer page,
                                   @RequestParam(value = "limit")Integer limit, Store store){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Integer count = storeService.getStoreTotal();
        List<Store> storeList = storeService.selectStoreAll(page-1,limit);
        resultMap.put("code",0);
        resultMap.put("count",count);
        resultMap.put("data",storeList);
        return resultMap;

    }

    @RequestMapping("/add")
    public String add(){return "store/add";}

    @PostMapping("/addSave")
    @ResponseBody
    public Map<String,Object> addSave(Store store){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        storeService.insertStore(store);
        resultMap.put("success",true);
        return resultMap;
    }
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(Integer id){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        storeService.deleteStore(id);
        resultMap.put("success",true);
        return resultMap;
    }
    @RequestMapping("/edit")
    public String edit(Integer id, ModelMap mmap){
        Store store = storeService.getStoreById(id);
        mmap.put("store",store);
        return "store/edit";
    }

    @PostMapping("/editSave")
    @ResponseBody
    public Map<String,Object> editSave(Store store){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        storeService.updateStore(store);
        resultMap.put("success",true);
        return resultMap;
    }
}
