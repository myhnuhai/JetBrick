package org.myhnuhai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    /**
     * 用户登录
     * @return
     */
    @RequestMapping("/login.html")
    public String loginPage() {
        return "user/login";
    }

    /**
     *      进入主界面
     *      * @param modelMap
     * @return
     */
    @RequestMapping("/main.html")
    public String initPage() {
        logger.info("HomeController!");
        return "main";
    }
    @RequestMapping("/layout/west.html")
    public String layoutWest(){
        return "layout/west";
    }

    @RequestMapping("/layout/north.html")
    public String layoutNorth(){
        return "layout/north";
    }

    @RequestMapping("/layout/center.html")
    public String layoutCenter(){
        return "layout/center";
    }

    @RequestMapping("/layout/south.html")
    public String layoutSouth(){
        return "layout/south";
    }


    /**
    * 404跳转
     * @return
     */
    @RequestMapping("/error404.html")
    public String error404() {
        return "/error/error404";
    }

    /**
     * 500跳转
     * @return
     */
    @RequestMapping("/error500.html")
    public String error500() {
        return "/error/error500";
    }
}
