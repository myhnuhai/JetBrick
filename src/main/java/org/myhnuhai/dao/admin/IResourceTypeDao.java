package org.myhnuhai.dao.admin;


import org.myhnuhai.model.Tresourcetype;

/**
 * 资源类型数据库操作类
 * 
 * @author myhnuhai
 * 
 */
public interface IResourceTypeDao extends IBaseDao<Tresourcetype> {

	/**
	 * 通过ID获得资源类型
	 * 
	 * @param id
	 * @return
	 */
	public Tresourcetype getById(String id);

}
