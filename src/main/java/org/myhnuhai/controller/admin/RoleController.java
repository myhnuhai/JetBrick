package org.myhnuhai.controller.admin;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.myhnuhai.controller.BaseController;
import org.myhnuhai.pageModel.*;
import org.myhnuhai.service.admin.IRoleService;
import org.myhnuhai.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 角色控制器
 * 
 * @author myhnuhai
 * 
 */
@Controller
@RequestMapping("/roleController")
public class RoleController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);
	@Autowired
	private IRoleService roleService;

	/**
	 * 跳转到角色管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/manager.html")
	public String manager() {
        request.setAttribute("canEdit", true);
        request.setAttribute("canDelete", true);
        request.setAttribute("canGrant",true);
        request.setAttribute("canAdd", true);
		return "/admin/role";
	}

	/**
	 * 跳转到角色添加页面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage.html")
	public String addPage(HttpServletRequest request) {
		Role r = new Role();
		r.setId(UUID.randomUUID().toString());
		request.setAttribute("role", r);
		return "/admin/roleAdd";
	}

	/**
	 * 添加角色
	 * 
	 * @return
	 */
	@RequestMapping("/add.jetx")
	@ResponseBody
	public Json add(Role role, HttpSession session) {
		SessionInfo sessionInfo = getSessionInfo();
		Json j = new Json();
		roleService.add(role, sessionInfo);
		j.setSuccess(true);
		j.setMsg("添加成功！");
		return j;
	}

	/**
	 * 跳转到角色修改页面
	 * 
	 * @return
	 */
	@RequestMapping("/editPage.html")
	public String editPage(HttpServletRequest request, String id) {
		Role r = roleService.get(id);
        logger.info(r.toString());
		request.setAttribute("role", r);
		return "/admin/roleEdit";
	}

	/**
	 * 修改角色
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping("/edit.jetx")
	@ResponseBody
	public Json edit(Role role) {
        logger.info(role.toString());
		Json j = new Json();
		roleService.edit(role);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}

	/**
	 * 获得角色列表
	 * 
	 * @return
	 */
	@RequestMapping("/treeGrid.jetx")
	@ResponseBody
	public List<Role> treeGrid(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return roleService.treeGrid(sessionInfo);
	}

    /**
     * 获得角色列表
     *
     * @return
     */
    @RequestMapping("/dataGrid.jetx")
    @ResponseBody
    public DataGrid dataGrid(HttpSession session) {
        SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
        return roleService.dataGrid(sessionInfo);
    }

	/**
	 * 角色树(只能看到自己拥有的角色)
	 * 
	 * @return
	 */
	@RequestMapping("/tree.jetx")
	@ResponseBody
	public List<Tree> tree(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return roleService.tree(sessionInfo);
	}

	/**
	 * 角色树
	 * 
	 * @return
	 */
	@RequestMapping("/allTree.jetx")
	@ResponseBody
	public List<Tree> allTree() {
		return roleService.allTree();
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete.jetx")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		roleService.delete(id);
		j.setMsg("删除成功！");
		j.setSuccess(true);
		return j;
	}

	/**
	 * 跳转到角色授权页面
	 * 
	 * @return
	 */
	@RequestMapping("/grantPage.html")
	public String grantPage(String id) {
		Role r = roleService.get(id);
		request.setAttribute("role", r);
		return "/admin/roleGrant";
	}

	/**
	 * 授权
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping("/grant.jetx")
	@ResponseBody
	public Json grant(Role role) {
		Json j = new Json();
		roleService.grant(role);
		j.setMsg("授权成功！");
		j.setSuccess(true);
		return j;
	}

}
