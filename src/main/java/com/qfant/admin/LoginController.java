package com.qfant.admin;

import com.qfant.config.RandomValidateCode;
import com.qfant.utils.AjaxResult;
import com.qfant.wx.entity.User;
import com.qfant.wx.repository.UserMapper;
import org.apache.shiro.crypto.hash.Md5Hash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserMapper userRepository;

    /**
     * 登录页面生成验证码
     */
    @RequestMapping(value = "getVerify")
     public void getVerify(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片  
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容  
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        RandomValidateCode randomValidateCode = new RandomValidateCode();
        try {
             randomValidateCode.getRandcode(request, response);//输出验证码图片方法  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 登录校验
     *
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/submitlogin")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password,String inputStr, HttpSession session) {
        User user=userRepository.getUserByUsername(username);
        String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
        if(random.equals(inputStr.toUpperCase())){
            if(user!=null){
                String encryptPassword=encryptPassword(username,password,user.getSalt());
                if(encryptPassword.equals(user.getPassword())){
                    getRequest().getSession().setAttribute("user",user);
                    return  success();
                }else {
                    String msg = "用户或密码错误";
                    return error(msg);
                }
            }else {
                String msg = "用户不存在";
                return error(msg);
            }
        }else if(random==null){
            String msg = "验证码错误";
            return error(msg);
        }else{
            String msg = "验证码错误";
            return  error(msg);
        }
    }
    /**
     * 注销登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/loginout")
    public String loginOut(HttpServletRequest request) {
        request.getSession().invalidate();
        return "login";
    }
    public  static String encryptPassword(String username, String password, String salt) {
        return new Md5Hash(username + password + salt).toHex().toString();
    }
}