package org.myhnuhai.service.admin;

import org.myhnuhai.pageModel.DataGrid;
import org.myhnuhai.pageModel.Orgnization;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;

import java.util.List;

/**
 * 部门Dept
 * Created by Administrator on 14-3-21.
 */
public interface IOrgnizationService {
    /**
     * 获得资源树(资源类型为菜单类型)
     *
     * 通过用户ID判断，他能看到的资源
     *
     * @param sessionInfo
     * @return
     */
    public List<Tree> tree(SessionInfo sessionInfo);

    /**
     * 获得资源树(包括所有资源类型)
     *
     * 通过用户ID判断，他能看到的资源
     *
     * @return
     */
    public List<Tree> allTree();
    /**
     * 获得资源列表
     *
     * @param sessionInfo
     *
     * @return
     */
    public List<Orgnization> treeGrid(SessionInfo sessionInfo);

    /**
     * 添加资源
     *
     * @param dept
     * @param sessionInfo
     */
    public void add(Orgnization dept, SessionInfo sessionInfo);

    /**
     * 删除资源
     *
     * @param id
     */
    public void delete(String id);

    /**
     * 修改资源
     *
     * @param dept
     */
    public void edit(Orgnization dept);

    /**
     * 获得一个资源
     *
     * @param id
     * @return
     */
    public Orgnization get(String id);

    /**
     * 部门授权
     * @param orgnization
     */
    public void grant(Orgnization orgnization);
}
