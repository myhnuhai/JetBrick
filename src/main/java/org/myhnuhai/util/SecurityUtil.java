package org.myhnuhai.util;

import org.apache.commons.lang3.StringUtils;
import org.myhnuhai.model.Torganization;
import org.myhnuhai.model.Tresource;
import org.myhnuhai.model.Trole;
import org.myhnuhai.pageModel.SessionInfo;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 14-3-24.
 */
public class SecurityUtil {
    private HttpSession session;

    public SecurityUtil(HttpSession session) {
        this.session = session;
    }

    /**
     * 判断当前用户是否可以访问某资源
     *
     * @param url
     *            资源地址
     * @return
     */
    public boolean havePermission(String url) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
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
