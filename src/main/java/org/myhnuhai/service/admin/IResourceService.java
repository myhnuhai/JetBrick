package org.myhnuhai.service.admin;

import org.myhnuhai.pageModel.DataGrid;
import org.myhnuhai.pageModel.Resource;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;

import javax.xml.crypto.Data;
import java.util.List;


/**
 * 资源Service
 * 
 * @author myhnuhai
 * 
 */
public interface IResourceService {

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
	public List<Resource> treeGrid(SessionInfo sessionInfo);


	/**
	 * 添加资源
	 * 
	 * @param resource
	 * @param sessionInfo
	 */
	public void add(Resource resource, SessionInfo sessionInfo);

	/**
	 * 删除资源
	 * 
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 修改资源
	 * 
	 * @param resource
	 */
	public void edit(Resource resource);

	/**
	 * 获得一个资源
	 * 
	 * @param id
	 * @return
	 */
	public Resource get(String id);

}
