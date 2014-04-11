package org.myhnuhai.service.admin;

import org.myhnuhai.pageModel.ResourceType;

import java.util.List;


/**
 * 资源类型服务
 * 
 * @author myhnuhai
 * 
 */
public interface IResourceTypeService {

	/**
	 * 获取资源类型
	 * 
	 * @return
	 */
	public List<ResourceType> getResourceTypeList();

}
