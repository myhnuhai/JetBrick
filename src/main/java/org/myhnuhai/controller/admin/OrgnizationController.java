package org.myhnuhai.controller.admin;

import org.myhnuhai.controller.BaseController;
import org.myhnuhai.pageModel.Json;
import org.myhnuhai.pageModel.Orgnization;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;
import org.myhnuhai.service.admin.IOrgnizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 14-3-21.
 */
@Controller
@RequestMapping("/organizationController")
public class OrgnizationController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private IOrgnizationService orgnizationService;
    /**
     * 获得部门树(部门类型为菜单类型)
     * <p/>
     * 通过用户ID判断，他能看到的部门

     * @return
     */
    @RequestMapping("/tree.jetx")
    @ResponseBody
    public List<Tree> tree() {
        SessionInfo sessionInfo = getSessionInfo();
        return orgnizationService.tree(sessionInfo);
    }

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
        return "/admin/organization";
    }

    /**
     * 跳转到角色添加页面
     *
     * @return
     */
    @RequestMapping("/addPage.html")
    public String addPage() {
        Orgnization orgnization = new Orgnization();
        orgnization.setId(UUID.randomUUID().toString());
        request.setAttribute("orgnization", orgnization);
        return "/admin/organizationAdd";
    }

    /**
     * 添加角色
     *
     * @return
     */
    @RequestMapping("/add.jetx")
    @ResponseBody
    public Json add(Orgnization orgnization) {
        SessionInfo sessionInfo = getSessionInfo();
        Json j = new Json();
        orgnizationService.add(orgnization,sessionInfo);
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
    public String editPage( String id) {
        logger.info("部门编号："+id);
       Orgnization organization = orgnizationService.get(id);
        logger.info("部门ID："+(organization == null));
       request.setAttribute("organization",organization);
        return "/admin/organizationEdit";
    }

    /**
     * 修改角色
     *
     * @param orgnization
     * @return
     */
    @RequestMapping("/edit.jetx")
    @ResponseBody
    public Json edit(Orgnization orgnization) {
        logger.info(orgnization.toString());
        Json j = new Json();
      orgnizationService.edit(orgnization);
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
    public List<Orgnization> treeGrid() {
       SessionInfo sessionInfo = getSessionInfo();
        return orgnizationService.treeGrid(sessionInfo);
    }

    /**
     * 角色树
     *
     * @return
     */
    @RequestMapping("/allTree.jetx")
    @ResponseBody
    public List<Tree> allTree() {
        return orgnizationService.allTree();
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
        orgnizationService.delete(id);
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
       Orgnization organization = orgnizationService.get(id);
           request.setAttribute("id",id);
        request.setAttribute("organization", organization);
        return "/admin/organizationGrant";
    }

    /**
     * 授权
     *
     * @param orgnization
     * @return
     */
    @RequestMapping("/grantOrganization.jetx")
    @ResponseBody
    public Json grant(Orgnization orgnization) {
        Json j = new Json();
        orgnizationService.grant(orgnization);
        j.setMsg("授权成功！");
        j.setSuccess(true);
        return j;
    }
}
