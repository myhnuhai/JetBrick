package org.myhnuhai.controller;

import org.apache.commons.lang3.StringUtils;
import org.myhnuhai.model.Torganization;
import org.myhnuhai.model.Tresource;
import org.myhnuhai.model.Trole;
import org.myhnuhai.pageModel.Json;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.util.ConfigUtil;
import org.myhnuhai.util.StringEscapeEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 马英虎 on 14-3-17.
 */
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    protected final Json json = new Json();

    /**
     * 获取用户Session对象
     * @return
     */
    protected SessionInfo getSessionInfo(){
        return (SessionInfo)getSessionAttr(ConfigUtil.getSessionInfoName());
    }

    /**
     * 获取Session属性
     * @param attr
     * @return
     */
    protected Object getSessionAttr(String attr) {
        HttpSession session = request.getSession();
        return session.getAttribute(attr);
    }

    /**
     * 注销当前Session
     */
    protected void clearSession(){
        request.getSession().invalidate();
    }

    /**
     * 获取Request请求参数
     * @param paramName
     * @return
     */
    public String getParam(String paramName){
        return request.getParameter(paramName);
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式
         */
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

        /**
         * 防止XSS攻击
         */
        binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
    }

    /**
     * 用户跳转JSP页面
     *
     * 此方法不考虑权限控制
     *
     * @param folder
     *            路径
     * @param jspName
     *            JSP名称(不加后缀)
     * @return 指定JSP页面
     */
    @RequestMapping("/{folder}/{jspName}")
    public String redirectJsp(@PathVariable String folder, @PathVariable String jspName) {
        return "/" + folder + "/" + jspName;
    }

    /**
     * 判断当前用户是否可以访问某资源
     *
     * @param url
     *            资源地址
     * @return
     */
    protected boolean havePermission(String url) {
        SessionInfo sessionInfo = getSessionInfo();
        List<Tresource> resources = new ArrayList<Tresource>();
        for (Trole role : sessionInfo.getRoles()) {
            resources.addAll(role.getResources());
        }
        for (Torganization organization : sessionInfo.getOrganizations()) {
            resources.addAll(organization.getResources());
        }
        resources = new ArrayList<Tresource>(new HashSet<Tresource>(resources));// 去重(这里包含了当前用户可访问的所有资源)
        for (Tresource resource : resources) {
            if (StringUtils.equals(resource.getUrl(), url)) {// 如果有相同的，则代表当前用户可以访问这个资源
                return true;
            }
        }
        return false;
    }
}
