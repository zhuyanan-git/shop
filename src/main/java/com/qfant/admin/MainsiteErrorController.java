package com.qfant.admin;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
class MainsiteErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 401){
            return "error/business";
        }else if(statusCode == 404){
            return "error/404";
        }else if(statusCode == 403){
            return "error/unauth";
        }
        else{
            return "error/500";
        }

    }
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
