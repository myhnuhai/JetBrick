package org.myhnuhai.service.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.myhnuhai.dao.admin.IResourceDao;
import org.myhnuhai.dao.admin.IRoleDao;
import org.myhnuhai.dao.admin.IUserDao;
import org.myhnuhai.model.Tresource;
import org.myhnuhai.model.Trole;
import org.myhnuhai.model.Tuser;
import org.myhnuhai.pageModel.DataGrid;
import org.myhnuhai.pageModel.Role;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;
import org.myhnuhai.service.admin.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements IRoleService {

    private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IResourceDao resourceDao;

	@Override
	public void add(Role role, SessionInfo sessionInfo) {
		Trole t = new Trole();
		BeanUtils.copyProperties(role, t);
        t.setDescription(role.getRemark());
		roleDao.save(t);

		// 刚刚添加的角色，赋予给当前的用户
		Tuser user = userDao.get(Tuser.class, sessionInfo.getId());
		user.getRoles().add(t);
	}

	@Override
	public Role get(String id) {
		Role r = new Role();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        Trole t = roleDao.get("select distinct t from Trole t left join fetch t.resources resource where t.id = :id", params);
        if (t != null) {
            BeanUtils.copyProperties(t, r);
            r.setRemark(t.getDescription());
            Set<Tresource> s = t.getResources();
            if (s != null && !s.isEmpty()) {
                boolean b = false;
                String ids = "";
                String names = "";
                for (Tresource tr : s) {
                    if (b) {
                        ids += ",";
                        names += ",";
                    } else {
                        b = true;
                    }
                    ids += tr.getId();
                    names += tr.getName();
                }
                r.setResourceIds(ids);
                r.setResourceNames(names);
            }
        }
		return r;
	}

	@Override
	public void edit(Role role) {
		Trole t = roleDao.get(Trole.class, role.getId());
		if (t != null) {
			BeanUtils.copyProperties(role, t);
            t.setDescription(role.getRemark());
		}
	}

	@Override
	public List<Role> treeGrid(SessionInfo sessionInfo) {
		List<Role> rl = new ArrayList<Role>();
		List<Trole> tl = null;
		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 查自己有权限的角色
			tl = roleDao.find("select distinct t from Trole t left join fetch t.resources resource join fetch t.users user where user.id = :userId order by t.seq", params);
		} else {
			tl = roleDao.find("select distinct t from Trole t left join fetch t.resources resource order by t.seq");
		}
        if(tl != null && tl.size() > 0){
            for (Trole trole : tl) {
                Role role = new Role();
                BeanUtils.copyProperties(trole,role);
                role.setRemark(trole.getDescription());
                String resids = "";
                String resNames = "";
                if(trole.getResources() != null){
                    for (Tresource tresource : trole.getResources()) {
                        resids += tresource.getId() +",";
                        resNames += tresource.getName() + ",";
                    }
                }
                role.setResourceIds(resids);
                role.setResourceNames(resNames);
                logger.info(role.toString());
                rl.add(role);
            }
        }
		return rl;
	}

    @Override
    public DataGrid dataGrid(SessionInfo sessionInfo) {
        DataGrid dataGrid = new DataGrid();
        List<Trole> tl = null;
        Map<String, Object> params = new HashMap<String, Object>();
        if (sessionInfo != null) {
            params.put("userId", sessionInfo.getId());// 查自己有权限的角色
            tl = roleDao.find("select distinct t from Trole t left join fetch t.resources resource join fetch t.users user where user.id = :userId order by t.seq", params);
        } else {
            tl = roleDao.find("select distinct t from Trole t left join fetch t.resources resource order by t.seq");
        }
        if(tl != null && tl.size() > 0){
            for (Trole trole : tl) {
                Role role = new Role();
                BeanUtils.copyProperties(trole,role);
                role.setRemark(trole.getDescription());
                String resids = "";
                String resNames = "";
                if(trole.getResources() != null){
                    for (Tresource tresource : trole.getResources()) {
                        resids += tresource.getId() +",";
                        resNames += tresource.getName() + ",";
                    }
                }
                role.setResourceIds(resids);
                role.setResourceNames(resNames);
                logger.info(role.toString());
                dataGrid.getRows().add(role);
            }
            dataGrid.setTotal(Long.parseLong(String.valueOf(tl.size())));
        }
        return dataGrid;
    }

    @Override
	public void delete(String id) {
		Trole t = roleDao.get(Trole.class, id);
		del(t);
	}

	private void del(Trole t) {

		roleDao.delete(t);
	}

	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {
		List<Trole> l = null;
		List<Tree> lt = new ArrayList<Tree>();

		Map<String, Object> params = new HashMap<String, Object>();
		if (sessionInfo != null) {
			params.put("userId", sessionInfo.getId());// 查自己有权限的角色
			l = roleDao.find("select distinct t from Trole t join fetch  t.resources join fetch t.users user where user.id = :userId order by t.seq", params);
		} else {
			l = roleDao.find("from Trole t join fetch  t.resources order by t.seq");
		}
        if (l != null && l.size() > 0) {
            for (Trole t : l) {
                Tree tree = new Tree();
                BeanUtils.copyProperties(t, tree);
                tree.setText(t.getName());
                tree.setIconCls("status_online");
                lt.add(tree);
            }
        }
        return lt;
	}

	@Override
	public List<Tree> allTree() {
		return this.tree(null);
	}

	@Override
	public void grant(Role role) {
        logger.info(role.toString());
        Trole t = roleDao.get(Trole.class, role.getId());
        if (role.getResourceIds() != null && !role.getResourceIds().equalsIgnoreCase("")) {
            String ids = "";
            boolean b = false;
            for (String id : role.getResourceIds().split(",")) {
                if (b) {
                    ids += ",";
                } else {
                    b = true;
                }
                ids += "'" + id + "'";
            }
            t.setResources(new HashSet<Tresource>(resourceDao.find("select distinct t from Tresource t where t.id in (" + ids + ")")));
        } else {
            t.setResources(null);
        }
	}

}
