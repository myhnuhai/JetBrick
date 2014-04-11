package org.myhnuhai.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.myhnuhai.dao.admin.IOrgnizationDao;
import org.myhnuhai.dao.admin.IResourceDao;
import org.myhnuhai.dao.admin.IRoleDao;
import org.myhnuhai.dao.admin.IUserDao;
import org.myhnuhai.model.Torganization;
import org.myhnuhai.model.Tresource;
import org.myhnuhai.model.Trole;
import org.myhnuhai.model.Tuser;
import org.myhnuhai.pageModel.*;
import org.myhnuhai.service.admin.IUserService;
import org.myhnuhai.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements IUserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private IUserDao userDao;

	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IOrgnizationDao orgnizationDao;
	@Override
	public SessionInfo login(User user) {
        logger.info(user.toString());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", user.getName());
		params.put("pwd", MD5Util.md5(user.getPwd()));
		Tuser t = userDao.get("from Tuser t join fetch t.organizations org join fetch org.resources join fetch t.roles role join fetch role.resources  where t.loginname = :name and t.pwd = :pwd", params);
		if (t != null) {
            SessionInfo sessionInfo = new SessionInfo();
			BeanUtils.copyProperties(t, sessionInfo);
			return sessionInfo;
		}
		return null;
	}

	@Override
	synchronized public void reg(User user) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", user.getName());
		if (userDao.count("select count(*) from Tuser t where t.name = :name", params) > 0) {
			throw new Exception("登录名已存在！");
		} else {
			Tuser u = new Tuser();
			u.setId(UUID.randomUUID().toString());
			u.setName(user.getName());
			u.setPwd(MD5Util.md5(user.getPwd()));
			u.setCreatedatetime(new Date());
			userDao.save(u);
		}
	}

	@Override
	public DataGrid dataGrid(User user, PageHelper ph) {
		DataGrid dg = new DataGrid();
		List<User> ul = new ArrayList<User>();
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Tuser t ";
        logger.info("用户查询：" + hql + whereHql(user, params) + orderHql(ph));
		List<Tuser> l = userDao.find(hql + whereHql(user, params) + orderHql(ph), params, ph.getPage(), ph.getRows());
		if (l != null && l.size() > 0) {
			for (Tuser t : l) {
				User u = new User();
				BeanUtils.copyProperties(t, u);
				Set<Trole> roles = t.getRoles();
				if (roles != null && !roles.isEmpty()) {
					String roleIds = "";
					String roleNames = "";
					boolean b = false;
					for (Trole tr : roles) {
						if (b) {
							roleIds += ",";
							roleNames += ",";
						} else {
							b = true;
						}
						roleIds += tr.getId();
						roleNames += tr.getName();
					}
                    u.setRoleIds(roleIds);
                    u.setRoleNames(roleNames);
				}
                Set<Torganization> organizations = t.getOrganizations();
                if(organizations != null && !organizations.isEmpty()){
                    String orgIds = "";
                    String orgNames = "";
                    boolean b = false;
                    for (Torganization tr : organizations) {
                        if (b) {
                            orgIds += ",";
                            orgNames += ",";
                        } else {
                            b = true;
                        }
                        orgIds += tr.getId();
                        orgNames += tr.getName();
                    }
                    u.setOrganizationIds(orgIds);
                    u.setOrganizationNames(orgNames);
                }
				ul.add(u);
			}
		}
		dg.setRows(ul);
		dg.setTotal(userDao.count("select count(*) " + hql + whereHql(user, params), params));
		return dg;
	}

	private String whereHql(User user, Map<String, Object> params) {
		String hql = "";
		if (user != null) {
			hql += " where 1=1 ";
			if (user.getName() != null) {
				hql += " and t.name like :name";
				params.put("name", "%%" + user.getName() + "%%");
			}
			if (user.getCreatedatetimeStart() != null) {
				hql += " and t.createdatetime >= :createdatetimeStart";
				params.put("createdatetimeStart", user.getCreatedatetimeStart());
			}
			if (user.getCreatedatetimeEnd() != null) {
				hql += " and t.createdatetime <= :createdatetimeEnd";
				params.put("createdatetimeEnd", user.getCreatedatetimeEnd());
			}
			if (user.getModifydatetimeStart() != null) {
				hql += " and t.modifydatetime >= :modifydatetimeStart";
				params.put("modifydatetimeStart", user.getModifydatetimeStart());
			}
			if (user.getModifydatetimeEnd() != null) {
				hql += " and t.modifydatetime <= :modifydatetimeEnd";
				params.put("modifydatetimeEnd", user.getModifydatetimeEnd());
			}
		}
		return hql;
	}

	private String orderHql(PageHelper ph) {
		String orderString = "";
		if (ph.getSort() != null && ph.getOrder() != null) {
			orderString = " order by t." + ph.getSort() + " " + ph.getOrder();
		}
		return orderString;
	}

	@Override
	synchronized public void add(User user) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", user.getName());
		if (userDao.count("select count(*) from Tuser t where t.name = :name", params) > 0) {
			throw new Exception("登录名已存在！");
		} else {
			Tuser u = new Tuser();
			BeanUtils.copyProperties(user, u);
			u.setCreatedatetime(new Date());
			u.setPwd(MD5Util.md5(user.getPwd()));
			userDao.save(u);
		}
	}

	@Override
	public User get(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser t = userDao.get("select distinct t from Tuser t left join fetch t.roles role where t.id = :id", params);
		User u = new User();
		BeanUtils.copyProperties(t, u);
		if (t.getRoles() != null && !t.getRoles().isEmpty()) {
			String roleIds = "";
			String roleNames = "";
			boolean b = false;
			for (Trole role : t.getRoles()) {
				if (b) {
					roleIds += ",";
					roleNames += ",";
				} else {
					b = true;
				}
				roleIds += role.getId();
				roleNames += role.getName();
			}
			u.setRoleIds(roleIds);
			u.setRoleNames(roleNames);
		}
		return u;
	}

	@Override
	synchronized public void edit(User user) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", user.getId());
		params.put("name", user.getName());
		if (userDao.count("select count(*) from Tuser t where t.name = :name and t.id != :id", params) > 0) {
			throw new Exception("登录名已存在！");
		} else {
			Tuser u = userDao.get(Tuser.class, user.getId());
			BeanUtils.copyProperties(user, u, new String[] { "pwd", "createdatetime" });
			u.setUpdatedatetime(new Date());
		}
	}

	@Override
	public void delete(String id) {
		userDao.delete(userDao.get(Tuser.class, id));
	}

	@Override
	public void grant(String ids, User user) {
		if (ids != null && ids.length() > 0) {
			List<Trole> roles = new ArrayList<Trole>();
			if (user.getRoleIds() != null) {
				for (String roleId : user.getRoleIds().split(",")) {
					roles.add(roleDao.get(Trole.class, roleId));
				}
			}
			for (String id : ids.split(",")) {
				if (id != null && !id.equalsIgnoreCase("")) {
					Tuser t = userDao.get(Tuser.class, id);
					t.setRoles(new HashSet<Trole>(roles));
				}
			}
		}
	}

    @Override
    public void grantOrgnization(String id, String orgIds) {
        if (id != null && id.length() > 0) {
            Tuser tuser = userDao.get(Tuser.class, id);
            if (orgIds != null) {
                tuser.setOrganizations(new HashSet<Torganization>());
                for (String organizationId : orgIds.split(",")) {
                    if (!StringUtils.isBlank(organizationId)) {
                        Torganization organization = orgnizationDao.get(Torganization.class, organizationId);
                        if (organization != null) {
                            tuser.getOrganizations().add(organization);
                        }
                    }
                }
            }
        }
    }

    @Override
	public List<String> resourceList(String id) {
		List<String> resourceList = new ArrayList<String>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Tuser t = userDao.get("from Tuser t join fetch t.troles role join fetch role.tresources resource where t.id = :id", params);
		if (t != null) {
			Set<Trole> roles = t.getRoles();
			if (roles != null && !roles.isEmpty()) {
				for (Trole role : roles) {
					Set<Tresource> resources = role.getResources();
					if (resources != null && !resources.isEmpty()) {
						for (Tresource resource : resources) {
							if (resource != null && resource.getUrl() != null) {
								resourceList.add(resource.getUrl());
							}
						}
					}
				}
			}
		}
		return resourceList;
	}

	@Override
	public void editPwd(User user) {
		if (user != null && user.getPwd() != null && !user.getPwd().trim().equalsIgnoreCase("")) {
			Tuser u = userDao.get(Tuser.class, user.getId());
			u.setPwd(MD5Util.md5(user.getPwd()));
			u.setUpdatedatetime(new Date());
		}
	}

	@Override
	public boolean editCurrentUserPwd(SessionInfo sessionInfo, String oldPwd, String pwd) {
		Tuser u = userDao.get(Tuser.class, sessionInfo.getId());
		if (u.getPwd().equalsIgnoreCase(MD5Util.md5(oldPwd))) {// 说明原密码输入正确
			u.setPwd(MD5Util.md5(pwd));
			u.setUpdatedatetime(new Date());
			return true;
		}
		return false;
	}

	@Override
	public List<User> loginCombobox(String q) {
		if (q == null) {
			q = "";
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + q.trim() + "%%");
		List<Tuser> tl = userDao.find("from Tuser t where t.name like :name order by name", params, 1, 10);
		List<User> ul = new ArrayList<User>();
		if (tl != null && tl.size() > 0) {
			for (Tuser t : tl) {
				User u = new User();
				u.setName(t.getName());
				ul.add(u);
			}
		}
		return ul;
	}

	@Override
	public DataGrid loginCombogrid(String q, PageHelper ph) {
		if (q == null) {
			q = "";
		}
		DataGrid dg = new DataGrid();
		List<User> ul = new ArrayList<User>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "%%" + q.trim() + "%%");
		List<Tuser> tl = userDao.find("from Tuser t where t.name like :name order by " + ph.getSort() + " " + ph.getOrder(), params, ph.getPage(), ph.getRows());
		if (tl != null && tl.size() > 0) {
			for (Tuser t : tl) {
				User u = new User();
				u.setName(t.getName());
				u.setCreatedatetime(t.getCreatedatetime());
				u.setUpdatedatetime(t.getUpdatedatetime());
				ul.add(u);
			}
		}
		dg.setRows(ul);
		dg.setTotal(userDao.count("select count(*) from Tuser t where t.name like :name", params));
		return dg;
	}

	@Override
	public List<Long> userCreateDatetimeChart() {
		List<Long> l = new ArrayList<Long>();
		int k = 0;
		for (int i = 0; i < 12; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("s", k);
			params.put("e", k + 2);
			k = k + 2;
			l.add(userDao.count("select count(*) from Tuser t where HOUR(t.createdatetime)>=:s and HOUR(t.createdatetime)<:e", params));
		}
		return l;
	}

}
