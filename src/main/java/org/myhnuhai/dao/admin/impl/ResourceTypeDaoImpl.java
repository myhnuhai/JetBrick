package org.myhnuhai.dao.admin.impl;

import org.myhnuhai.dao.admin.IResourceTypeDao;
import org.myhnuhai.model.Tresourcetype;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;


@Repository
public class ResourceTypeDaoImpl extends BaseDaoImpl<Tresourcetype> implements IResourceTypeDao {

	@Override
	@Cacheable(value = "resourceTypeDaoCache", key = "#id")
	public Tresourcetype getById(String id) {
		return super.get(Tresourcetype.class, id);
	}

}
