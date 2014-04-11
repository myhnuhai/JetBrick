package org.myhnuhai.core.security;

import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 马英虎 on 14-3-14.
 */
public class SecurityInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private List<String> excludeRequst;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());
          logger.info("Request:" + url);       //如果请求HomerController中的方法或者是包含无需验证的请求则放过

        boolean needLogin = true;
//        if(needLogin){
//            return true;
//        }
        if(excludeRequst.contains(url)){
            logger.info("验证通过：" + url);
            needLogin = false;
        }
        SessionInfo sessionInfo = (SessionInfo)request.getSession().getAttribute(ConfigUtil.getSessionInfoName());
        if(needLogin){
            if(sessionInfo == null || sessionInfo.getId() == ""){
                logger.info("你好未登录，请登录！");
                request.getRequestDispatcher("/login.html").forward(request, response);
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }

//        if (!sessionInfo.getResourceList().contains(url)) {// 如果当前用户没有访问此资源的权限
//            logger.info("你好，你没有权限访问该界面！");
////            request.setAttribute("msg", "您没有访问此资源的权限！<br/>请联系超管赋予您<br/>[" + url + "]<br/>的资源访问权限！");
////            request.getRequestDispatcher("/error/noSecurity.jsp").forward(request, response);
//            return false;
//        }
       // return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public List<String> getExcludeRequst() {
        return excludeRequst;
    }

    public void setExcludeRequst(List<String> excludeRequst) {
        this.excludeRequst = excludeRequst;
    }
}
