package org.myhnuhai.service.admin;

import org.myhnuhai.pageModel.DataGrid;
import org.myhnuhai.pageModel.Role;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;

import java.util.List;

import javax.servlet.http.HttpSession;


/**
 * 角色业务逻辑
 * 
 * @author myhnuhai
 * 
 */
public interface IRoleService {

	/**
	 * 保存角色
	 * 
	 * @param role
	 */
	public void add(Role role, SessionInfo sessionInfo);

	/**
	 * 获得角色
	 * 
	 * @param id
	 * @return
	 */
	public Role get(String id);

	/**
	 * 编辑角色
	 * 
	 * @param role
	 */
	public void edit(Role role);

	/**
	 * 获得角色treeGrid
	 * 
	 * @return
	 */
	public List<Role> treeGrid(SessionInfo sessionInfo);

    /**
     * 获得角色dataGrid
     *
     * @return
     */
    public DataGrid dataGrid(SessionInfo sessionInfo);

	/**
	 * 删除角色
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 获得角色树(只能看到自己拥有的角色)
	 * 
	 * @return
	 */
	public List<Tree> tree(SessionInfo sessionInfo);

	/**
	 * 获得角色树
	 * 
	 * @return
	 */
	public List<Tree> allTree();

	/**
	 * 为角色授权
	 * 
	 * @param role
	 */
	public void grant(Role role);

}
