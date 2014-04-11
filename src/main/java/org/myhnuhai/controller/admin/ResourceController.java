package org.myhnuhai.controller.admin;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.myhnuhai.controller.BaseController;
import org.myhnuhai.pageModel.Json;
import org.myhnuhai.pageModel.Resource;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;
import org.myhnuhai.service.admin.IResourceService;
import org.myhnuhai.service.admin.IResourceTypeService;
import org.myhnuhai.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 资源控制器
 *
 * @author myhnuhai
 */
@Controller
@RequestMapping("/resourceController")
public class ResourceController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IResourceTypeService resourceTypeService;

    /**
     * 获得资源树(资源类型为菜单类型)
     * <p/>
     * 通过用户ID判断，他能看到的资源
     *
     * @return
     */
    @RequestMapping("/tree.jetx")
    @ResponseBody
    public List<Tree> tree() {
        SessionInfo sessionInfo = getSessionInfo();

        return resourceService.tree(sessionInfo);
    }

    /**
     * 获得资源树(包括所有资源类型)
     * <p/>
     * 通过用户ID判断，他能看到的资源
     *
     * @return
     */
    @RequestMapping("/allTree.jetx")
    @ResponseBody
    public List<Tree> allTree() {

        return resourceService.allTree();
    }

    /**
     * 跳转到资源管理页面
     *
     * @return
     */
    @RequestMapping("/manager.html")
    public String manager() {
        request.setAttribute("canEdit", super.havePermission("/resourceController/delete.jetx"));
        //logger.info("是否可以删除：" + getSessionInfo().getResourceList().contains("/resourceController/delete.jetx"));
        request.setAttribute("canDelete", true);
        return "/admin/resource";
    }

    /**
     * 跳转到资源添加页面
     *
     * @return
     */
    @RequestMapping("/addPage.html")
    public String addPage() {
        request.setAttribute("resourceTypeList",
                resourceTypeService.getResourceTypeList());
        Resource r = new Resource();
        r.setId(UUID.randomUUID().toString());
        request.setAttribute("resource", r);

        return "admin/resourceAdd";
    }

    /**
     * 添加资源
     *
     * @return
     */
    @RequestMapping("/add.jetx")
    @ResponseBody
    public Json add(Resource resource) {
        SessionInfo sessionInfo = getSessionInfo();
        Json j = new Json();
        resourceService.add(resource, sessionInfo);
        j.setSuccess(true);
        j.setMsg("添加成功！");
        return j;
    }

    /**
     * 跳转到资源编辑页面
     *
     * @return
     */
    @RequestMapping("/editPage.html")
    public String editPage(String id) {
        request.setAttribute("resourceTypeList",
                resourceTypeService.getResourceTypeList());
        Resource r = resourceService.get(id);
        request.setAttribute("resource", r);
        return "admin/resourceEdit";
    }

    /**
     * 编辑资源
     *
     * @param resource
     * @return
     */
    @RequestMapping("/edit.jetx")
    @ResponseBody
    public Json edit(Resource resource) {
        Json j = new Json();
        resourceService.edit(resource);
        j.setSuccess(true);
        j.setMsg("编辑成功！");
        return j;
    }

    /**
     * 获得资源列表
     * <p/>
     * 通过用户ID判断，他能看到的资源
     *
     * @return
     */
    @RequestMapping("/treeGrid.jetx")
    @ResponseBody
    public List<Resource> treeGrid() {
        SessionInfo sessionInfo = getSessionInfo();
        return resourceService.treeGrid(sessionInfo);
    }
    /**
     * 获得资源列表
     * <p/>
     * 通过用户ID判断，他能看到的资源
     *
     * @return
     */
    @RequestMapping("/dataGrid.jetx")
    @ResponseBody
    public List<Resource> dataGrid() {
        SessionInfo sessionInfo = getSessionInfo();
        return resourceService.treeGrid(sessionInfo);
    }
    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete.jetx")
    @ResponseBody
    public Json delete(String id) {
        Json j = new Json();
        resourceService.delete(id);
        j.setMsg("删除成功！");
        j.setSuccess(true);
        return j;
    }

}
