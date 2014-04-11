package org.myhnuhai.controller.admin;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.myhnuhai.controller.BaseController;
import org.myhnuhai.model.Tuser;
import org.myhnuhai.pageModel.*;
import org.myhnuhai.service.admin.IResourceService;
import org.myhnuhai.service.admin.IRoleService;
import org.myhnuhai.service.admin.IUserService;
import org.myhnuhai.util.ConfigUtil;
import org.myhnuhai.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSON;

/**
 * 用户控制器
 * 
 * @author myhnuhai
 * 
 */
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IResourceService resourceService;

	/**
	 * 用户登录
	 * 
	 * @param user
	 *            用户对象
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/login.jetx")
	public Json login(User user) {
		Json j = new Json();
        SessionInfo sessionInfo = userService.login(user);
		if (sessionInfo != null) {
			j.setSuccess(true);
			j.setMsg("登陆成功！");
            User user1 = new User();
            BeanUtils.copyProperties(sessionInfo,user1);
			request.getSession().setAttribute(ConfigUtil.getSessionInfoName(), sessionInfo);
			j.setData(user1);
		} else {
			j.setMsg("用户名或密码错误！");
		}
		return j;
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 *            用户对象
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/reg.jetx")
	public Json reg(User user) {
		Json j = new Json();
		try {
			userService.reg(user);
			j.setSuccess(true);
			j.setMsg("注册成功！新注册的用户没有任何权限，请让管理员赋予权限后再使用本系统！");
			j.setData(user);
		} catch (Exception e) {
			// e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/logout.jetx")
	public Json logout() {
		Json j = new Json();
		if (request.getSession() != null) {
			super.clearSession();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		return j;
	}

	/**
	 * 跳转到用户管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager.html")
	public String manager() {
        request.setAttribute("canEdit", true);
        request.setAttribute("canDelete",true);
        request.setAttribute("canGrant", true);
        request.setAttribute("canAdd", true);
        request.setAttribute("canBatchDelete", true);
        request.setAttribute("canEditPwd", true);
		return "/admin/user";
	}

	/**
	 * 获取用户数据表格
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/dataGrid.jetx")
	@ResponseBody
	public DataGrid dataGrid(User user, PageHelper ph) {
		return userService.dataGrid(user, ph);
	}

	/**
	 * 跳转到添加用户页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage.html")
	public String addPage() {
		User u = new User();
		u.setId(UUID.randomUUID().toString());
		request.setAttribute("user", u);
		return "/admin/userAdd";
	}

	/**
	 * 添加用户
	 * 
	 * @return
	 */
	@RequestMapping("/add.jetx")
	@ResponseBody
	public Json add(User user) {
		Json j = new Json();
		try {
			userService.add(user);
			j.setSuccess(true);
			j.setMsg("添加成功！");
			j.setData(user);
		} catch (Exception e) {
			// e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 跳转到用户修改页面
	 * 
	 * @return
	 */
	@RequestMapping("/editPage.html")
	public String editPage(String id) {
		User u = userService.get(id);
		request.setAttribute("user", u);
		return "/admin/userEdit";
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/edit.jetx")
	@ResponseBody
	public Json edit(User user) {
		Json j = new Json();
		try {
			userService.edit(user);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
			j.setData(user);
		} catch (Exception e) {
			// e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete.jetx")
	@ResponseBody
	public Json delete(String id, HttpSession session) {
		SessionInfo sessionInfo = getSessionInfo();
		Json j = new Json();
		if (id != null && !id.equalsIgnoreCase(sessionInfo.getId())) {// 不能删除自己
			userService.delete(id);
		}
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量删除用户
	 * 
	 * @param ids
	 *            ('0','1','2')
	 * @return
	 */
	@RequestMapping("/batchDelete.jetx")
	@ResponseBody
	public Json batchDelete(String ids) {
		Json j = new Json();
		if (ids != null && ids.length() > 0) {
			for (String id : ids.split(",")) {
				if (id != null) {
					this.delete(id, request.getSession());
				}
			}
		}
		j.setMsg("批量删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 跳转到用户角色授权页面
	 * 
	 * @return
	 */
	@RequestMapping("/grantRolePage.html")
	public String grantRolePage(String ids) {
        logger.info("IDS获取到的：" +ids);
		request.setAttribute("ids", ids);
        if (ids != null && !ids.equalsIgnoreCase("") && ids.indexOf(",") == -1) {
            User u = userService.get(ids);
            request.setAttribute("user", u);
        }
		return "/admin/userRoleGrant";
	}
    /**
     * 用户授权
     *
     * @param ids
     * @return
     */
    @RequestMapping("/grantRole.jetx")
    @ResponseBody
    public Json grant(String ids, User user) {
        Json j = new Json();
        userService.grant(ids, user);
        j.setSuccess(true);
        j.setMsg("授权成功！");
        return j;
    }

    /**
     * 跳转到用户授权页面
     *
     * @return
     */
    @RequestMapping("/grantOrganizationPage.html")
    public String grantOrganizationPage(String id) {
        logger.info("IDS获取到的：" +id);
        request.setAttribute("ids", id);
        if (id != null && !id.equalsIgnoreCase("") && id.indexOf(",") == -1) {
            User u = userService.get(id);
            request.setAttribute("user", u);
            logger.info(u.toString());
        }
        return "/admin/userOrganizationGrant";
    }
    /**
     * 用户授权
     *
     * @param id
     * @return
     */
    @RequestMapping("/grantOrganization.jetx")
    @ResponseBody
    public Json grantOrganization(String id, String orginizationIds) {
        Json j = new Json();
        userService.grantOrgnization(id, orginizationIds);
        j.setSuccess(true);
        j.setMsg("授权成功！");
        return j;
    }


	/**
	 * 跳转到编辑用户密码页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/editPwdPage.html")
	public String editPwdPage(String id) {
		User u = userService.get(id);
		request.setAttribute("user", u);
		return "/admin/userEditPwd";
	}

	/**
	 * 编辑用户密码
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/editPwd.jetx")
	@ResponseBody
	public Json editPwd(User user) {
		Json j = new Json();
		userService.editPwd(user);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}

	/**
	 * 跳转到编辑自己的密码页面
	 * 
	 * @return
	 */
	@RequestMapping("/editCurrentUserPwdPage.html")
	public String editCurrentUserPwdPage() {
		return "/user/userEditPwd";
	}

	/**
	 * 修改自己的密码
	 * 
	 * @param session
	 * @param pwd
	 * @return
	 */
	@RequestMapping("/editCurrentUserPwd.jetx")
	@ResponseBody
	public Json editCurrentUserPwd(HttpSession session, String oldPwd, String pwd) {
		Json j = new Json();
		if (session != null) {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
			if (sessionInfo != null) {
				if (userService.editCurrentUserPwd(sessionInfo, oldPwd, pwd)) {
					j.setSuccess(true);
					j.setMsg("编辑密码成功，下次登录生效！");
				} else {
					j.setMsg("原密码错误！");
				}
			} else {
				j.setMsg("登录超时，请重新登录！");
			}
		} else {
			j.setMsg("登录超时，请重新登录！");
		}
		return j;
	}

	/**
	 * 跳转到显示用户角色页面
	 * 
	 * @return
	 */
	@RequestMapping("/currentUserRolePage.html")
	public String currentUserRolePage(HttpServletRequest request, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		request.setAttribute("userRoles", JSON.toJSONString(roleService.tree(sessionInfo)));
		return "/user/userRole";
	}

	/**
	 * 跳转到显示用户权限页面
	 * 
	 * @return
	 */
	@RequestMapping("/currentUserResourcePage.html")
	public String currentUserResourcePage(HttpServletRequest request, HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		request.setAttribute("userResources", JSON.toJSONString(resourceService.allTree()));
		return "/user/userResource";
	}

	/**
	 * 用户登录时的autocomplete
	 * 
	 * @param q
	 *            参数
	 * @return
	 */
	@RequestMapping("/loginCombobox.jetx")
	@ResponseBody
	public List<User> loginCombobox(String q) {
		return userService.loginCombobox(q);
	}

	/**
	 * 用户登录时的combogrid
	 * 
	 * @param q
	 * @param ph
	 * @return
	 */
	@RequestMapping("/loginCombogrid.jetx")
	@ResponseBody
	public DataGrid loginCombogrid(String q, PageHelper ph) {
		return userService.loginCombogrid(q, ph);
	}

}
