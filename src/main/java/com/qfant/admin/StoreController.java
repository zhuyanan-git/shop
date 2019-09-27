package com.qfant.admin;

import com.qfant.utils.DateUtils;
import com.qfant.utils.QRCodeUtil;
import com.qfant.utils.StringUtils;
import com.qfant.wx.entity.Store;
import com.qfant.wx.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class StoreController extends BaseController{
    @Autowired
    private StoreService storeService;

    @Value("${profile}")
    private String profile;
    @Value("${payment.url}")
    private String paymentUrl;
    @GetMapping()
    public String store(){return "store/store";}

    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> list(@RequestParam(value = "page")Integer page,
                                   @RequestParam(value = "limit")Integer limit, Store store){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Integer count = storeService.getStoreTotal();
        List<Store> storeList = storeService.selectStoreAll((page-1)*limit,limit);
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
    public String edit(Integer id , ModelMap modelMap){
        Store store = storeService.selectStoreById(id);
        modelMap.put("store",store);
        return "store/edit";
    }
    @RequestMapping("/editSave")
    @ResponseBody
    public Map<String,Object> editSave(Store store){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        storeService.updateStore(store);
        resultMap.put("success",true);
        return resultMap;
    }

    @RequestMapping("/qrcode")
    @ResponseBody
    public Map<String,Object> qrcode(Integer id) throws Exception{
        Map<String,Object> resultMap = new HashMap<String, Object>();
        //生成二维码

        // 嵌入二维码的图片路径
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String imgPath = profile+"weixin.png";
        Store store = storeService.selectStoreById(id);
        String codePath ;
        if (StringUtils.isNotEmpty(store.getQrcode())) {
            codePath = store.getQrcode();
        } else {
            codePath = DateUtils.dateTimeNow() + ".jpg";
        }
        // 生成的二维码的路径及名称
        String destPath = profile+codePath;
        QRCodeUtil.createImage(paymentUrl+"?storeid="+id, imgPath,destPath);
        //overlapImage(destPath,imgPath);
        store.setQrcode(codePath);
        storeService.update(store);
        resultMap.put("success",true);
        return resultMap;
    }


}
