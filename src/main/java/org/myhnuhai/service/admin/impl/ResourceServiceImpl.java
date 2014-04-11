package org.myhnuhai.service.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.myhnuhai.dao.admin.IResourceDao;
import org.myhnuhai.dao.admin.IResourceTypeDao;
import org.myhnuhai.dao.admin.IUserDao;
import org.myhnuhai.model.Tresource;
import org.myhnuhai.model.Trole;
import org.myhnuhai.model.Tuser;
import org.myhnuhai.pageModel.Resource;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;
import org.myhnuhai.service.admin.IResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ResourceServiceImpl implements IResourceService {

    private final static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
    @Autowired
    private IResourceDao resourceDao;

    @Autowired
    private IResourceTypeDao resourceTypeDao;

    @Autowired
    private IUserDao userDao;

    @Override
    public List<Tree> tree(SessionInfo sessionInfo) {
        List<Tresource> l = null;
        List<Tree> lt = new ArrayList<Tree>();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("resourceTypeId", "0");// 菜单类型的资源

        if (sessionInfo != null) {
            params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
            l = resourceDao
                    .find("select distinct t from Tresource t join fetch t.resourcetype type join fetch t.roles role join role.users user where type.id = :resourceTypeId and user.id = :userId order by t.seq",
                            params);
        } else {
            l = resourceDao
                    .find("select distinct t from Tresource t join fetch t.resourcetype type where type.id = :resourceTypeId order by t.seq",
                            params);
        }

        if (l != null && l.size() > 0) {
            for (Tresource r : l) {
                Tree tree = new Tree();
                BeanUtils.copyProperties(r, tree);
                if (r.getResource() != null) {
                    tree.setPid(r.getResource().getId());
                }
                tree.setText(r.getName());
                tree.setIconCls(r.getIconCls());
                Map<String, Object> attr = new HashMap<String, Object>();
                attr.put("url", r.getUrl());
                tree.setAttributes(attr);
                lt.add(tree);
            }
        }
        return lt;
    }

    @Override
    public List<Tree> allTree() {
        return tree(null);
    }

    @Override
    public List<Resource> treeGrid(SessionInfo sessionInfo) {
        List<Tresource> l = null;
        List<Resource> lr = new ArrayList<Resource>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (sessionInfo != null) {
            params.put("userId", sessionInfo.getId());// 自查自己有权限的资源
            l = resourceDao
                    .find("select distinct t from Tresource t join fetch t.resourcetype type join fetch t.roles role join role.users user where user.id = :userId order by t.seq",
                            params);
        } else {
            l = resourceDao
                    .find("select distinct t from Tresource t join fetch t.resourcetype type order by t.seq",
                            params);
        }

        if (l != null && l.size() > 0) {
            for (Tresource t : l) {
                Resource r = new Resource();
                BeanUtils.copyProperties(t, r);
                if (t.getResource() != null) {
                    r.setPid(t.getResource().getId());
                    r.setPname(t.getResource().getName());
                }
                r.setTypeId(t.getResourcetype().getId());
                r.setTypeName(t.getResourcetype().getName());
                if (t.getIconCls() != null && !t.getIconCls().equalsIgnoreCase("")) {
                    r.setIconCls(t.getIconCls());
                }
                lr.add(r);
            }
        }

        return lr;
    }

    @Override
    public void add(Resource resource, SessionInfo sessionInfo) {
        logger.info(resource.toString());
        Tresource t = new Tresource();
        BeanUtils.copyProperties(resource, t);
        if (resource.getPid() != null
                && !resource.getPid().equalsIgnoreCase("")) {
            t.setResource(resourceDao.get(Tresource.class, resource.getPid()));
        }
        if (resource.getTypeId() != null
                && !resource.getTypeId().equalsIgnoreCase("")) {
            t.setResourcetype(resourceTypeDao.getById(resource.getTypeId()));
        }
        if (resource.getIconCls() != null
                && !resource.getIconCls().equalsIgnoreCase("")) {
            t.setIconCls(resource.getIconCls());
        }
        resourceDao.save(t);

        // 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
        Tuser user = userDao.get(Tuser.class, sessionInfo.getId());
        Set<Trole> roles = user.getRoles();
        for (Trole r : roles) {
            r.getResources().add(t);
        }
    }

    @Override
    public void delete(String id) {
        Tresource t = resourceDao.get(Tresource.class, id);
        del(t);
    }

    private void del(Tresource t) {
        if (t.getResources() != null && t.getResources().size() > 0) {
            for (Tresource r : t.getResources()) {
                del(r);
            }
        }
        resourceDao.delete(t);
    }

    @Override
    public void edit(Resource resource) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", resource.getId());
        Tresource t = resourceDao.get(
                "select distinct t from Tresource t where t.id = :id", params);
        if (t != null) {
            BeanUtils.copyProperties(resource, t);
            if (resource.getTypeId() != null
                    && !resource.getTypeId().equalsIgnoreCase("")) {
                t.setResourcetype(resourceTypeDao.getById(resource.getTypeId()));// 赋值资源类型
            }
            if (resource.getIconCls() != null
                    && !resource.getIconCls().equalsIgnoreCase("")) {
                t.setIconCls(resource.getIconCls());
            }
            if (resource.getPid() != null
                    && !resource.getPid().equalsIgnoreCase("")) {// 说明前台选中了上级资源
                Tresource pt = resourceDao.get(Tresource.class,
                        resource.getPid());
                isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
                t.setResource(pt);
            } else {
                t.setResource(null);// 前台没有选中上级资源，所以就置空
            }
        }
    }

    /**
     * 判断是否是将当前节点修改到当前节点的子节点
     *
     * @param t  当前节点
     * @param pt 要修改到的节点
     * @return
     */
    private boolean isChildren(Tresource t, Tresource pt) {
        if (pt != null && pt.getResource() != null) {
            if (pt.getResource().getId().equalsIgnoreCase(t.getId())) {
                pt.setResource(null);
                return true;
            } else {
                return isChildren(t, pt.getResource());
            }
        }
        return false;
    }

    @Override
    public Resource get(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        Tresource t = resourceDao
                .get("from Tresource t left join fetch t.resource resource left join fetch t.resourcetype resourceType where t.id = :id",
                        params);
        Resource r = new Resource();
        BeanUtils.copyProperties(t, r);
        if (t.getResource() != null) {
            r.setPid(t.getResource().getId());
            r.setPname(t.getResource().getName());
        }
        r.setTypeId(t.getResourcetype().getId());
        r.setTypeName(t.getResourcetype().getName());
        if (t.getIconCls() != null && !t.getIconCls().equalsIgnoreCase("")) {
            r.setIconCls(t.getIconCls());
        }
        return r;
    }

}
