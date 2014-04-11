package org.myhnuhai.service.admin.impl;

import org.myhnuhai.dao.admin.IResourceTypeDao;
import org.myhnuhai.model.Tresourcetype;
import org.myhnuhai.pageModel.ResourceType;
import org.myhnuhai.service.admin.IResourceTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ResourceTypeServiceImpl implements IResourceTypeService {

	@Autowired
	private IResourceTypeDao resourceType;

	@Override
	@Cacheable(value = "resourceTypeServiceCache", key = "'resourceTypeList'")
	public List<ResourceType> getResourceTypeList() {
		List<Tresourcetype> l = resourceType.find("from Tresourcetype t");
		List<ResourceType> rl = new ArrayList<ResourceType>();
		if (l != null && l.size() > 0) {
			for (Tresourcetype t : l) {
				ResourceType rt = new ResourceType();
				BeanUtils.copyProperties(t, rt);
				rl.add(rt);
			}
		}
		return rl;
	}

}
