package com.qfant.admin;

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

@Controller
public class LoginController extends BaseController{

    @Autowired
    private UserMapper userRepository;
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
    public AjaxResult ajaxLogin(String username, String password) {
        User user=userRepository.getUserByUsername(username);
        if(user!=null){
            String encryptPassword=encryptPassword(username,password,user.getSalt());
            if(encryptPassword.equals(user.getPassword())){
                getRequest().getSession().setAttribute("user",user);
                return success();
            }else {
                String msg = "用户或密码错误";
                return error(msg);
            }
        }else {
            String msg = "用户不存在";
            return error(msg);
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
    public String encryptPassword(String username, String password, String salt) {
        return new Md5Hash(username + password + salt).toHex().toString();
    }
}