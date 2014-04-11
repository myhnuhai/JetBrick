package org.myhnuhai.service.admin.impl;

import org.myhnuhai.dao.admin.IOrgnizationDao;
import org.myhnuhai.dao.admin.IResourceDao;
import org.myhnuhai.dao.admin.IUserDao;
import org.myhnuhai.model.Torganization;
import org.myhnuhai.model.Tresource;
import org.myhnuhai.model.Tuser;
import org.myhnuhai.pageModel.Orgnization;
import org.myhnuhai.pageModel.SessionInfo;
import org.myhnuhai.pageModel.Tree;
import org.myhnuhai.service.admin.IOrgnizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 14-3-21.
 */
@Service
public class OrgnizationServiceImpl implements IOrgnizationService {
    private final static Logger logger = LoggerFactory.getLogger(OrgnizationServiceImpl.class);
    @Autowired
    private IUserDao userDao;

    @Autowired
    private IOrgnizationDao orgnizationDao;

    @Autowired
    private IResourceDao resourceDao;

    @Override
    public List<Tree> tree(SessionInfo sessionInfo) {
        List<Torganization> list = getTorg(sessionInfo);
        List<Tree> listTree = new ArrayList<Tree>();
        if(list != null && list.size() > 0){
            for (Torganization torganization : list) {
                Tree tree = new Tree();
                BeanUtils.copyProperties(torganization,tree);
                tree.setText(torganization.getName());
                if(torganization.getOrganization() != null){
                    tree.setPid(torganization.getOrganization().getId());
                }
                listTree.add(tree);
            }
        }
        
        return listTree;
    }

    @Override
    public List<Tree> allTree() {
        List<Tree> returnList = new ArrayList<Tree>();
        List<Torganization> list = orgnizationDao.find("select torg from Torganization torg join fetch torg.organizations");
        if(list != null && list.size() > 0){
            for (Torganization torganization : list) {
                Tree tree = new Tree();
                BeanUtils.copyProperties(torganization,tree);
                tree.setText(torganization.getName());
                if (torganization.getOrganization() != null){
                    tree.setPid(torganization.getOrganization().getId());
                }
                returnList.add(tree);
            }
        }
        return returnList;
    }

    @Override
    public List<Orgnization> treeGrid(SessionInfo sessionInfo) {
        List<Torganization> list = getTorg(sessionInfo);
        List<Orgnization> orgnizations = new ArrayList<Orgnization>();
        if(list != null && list.size() > 0){
            for (Torganization torganization : list) {
                Orgnization org = new Orgnization();
                BeanUtils.copyProperties(torganization,org);
                if(torganization.getOrganization() != null){
                    org.setPid(torganization.getOrganization().getId());
                }
                if(torganization.getResources() != null){
                    String resIds = "";
                    String resNames = "";
                    for (Tresource tresource : torganization.getResources()) {
                        resIds += tresource.getId() + ",";
                        resNames += tresource.getName() + ",";
                    }
                    org.setResourceIds(resIds);
                    org.setResourceNames(resNames);
                }
                orgnizations.add(org);
            }
        }
        return orgnizations;
    }

    private List<Torganization> getTorg(SessionInfo sessionInfo){
        Tuser tuser = userDao.get(Tuser.class, sessionInfo.getId());
        List<Torganization> list = new ArrayList<Torganization>(tuser.getOrganizations());

        Collections.sort(list, new Comparator<Torganization>() {// 排序
            @Override
            public int compare(Torganization o1, Torganization o2) {
                if (o1.getSeq() == null) {
                    o1.setSeq(1000);
                }
                if (o2.getSeq() == null) {
                    o2.setSeq(1000);
                }
                return o1.getSeq().compareTo(o2.getSeq());
            }
        });
        return list;
    }
    @Override
    public void add(Orgnization dept, SessionInfo sessionInfo) {
        logger.info(dept.toString());

        Torganization org = new Torganization();
        BeanUtils.copyProperties(dept,org);
        if(dept.getPid() != null && !dept.getPid().equalsIgnoreCase("")){
            org.setOrganization(orgnizationDao.get(Torganization.class,dept.getPid()));
        }
        orgnizationDao.save(org);


        // 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
        Tuser tuser = userDao.get(Tuser.class,sessionInfo.getId());
        tuser.getOrganizations().add(org);
    }

    @Override
    public void delete(String id) {
        Torganization org = orgnizationDao.get(Torganization.class,id);
        del(org);
    }
    private void del(Torganization t) {
        if (t.getOrganizations() != null && t.getOrganizations().size() > 0) {
            for (Torganization r : t.getOrganizations()) {
                del(r);
            }
        }
        orgnizationDao.delete(t);
    }
    @Override
    public void edit(Orgnization dept) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", dept.getId());
        Torganization org = orgnizationDao.get("select distinct t from Torganization t where t.id=:id",params);
        if (org != null){
            BeanUtils.copyProperties(dept,org);
            if (dept.getIconCls() != null
                    && !dept.getIconCls().equalsIgnoreCase("")) {
                org.setIconCls(dept.getIconCls());
            }
            if (dept.getPid() != null && !dept.getPid().equalsIgnoreCase("")){
                Torganization torganization = orgnizationDao.get(Torganization.class,dept.getPid());
                isChildren(org,torganization);
                org.setOrganization(torganization);
            }else{
                org.setOrganization(null);
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
    private boolean isChildren(Torganization t, Torganization pt) {
        if (pt != null && pt.getOrganization() != null) {
            if (pt.getOrganization().getId().equalsIgnoreCase(t.getId())) {
                pt.setOrganization(null);
                return true;
            } else {
                return isChildren(t, pt.getOrganization());
            }
        }
        return false;
    }
    @Override
    public Orgnization get(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        Torganization org = orgnizationDao.get("from Torganization t left join fetch t.organizations left join fetch t.resources where t.id=:id",params);
        Orgnization orgnization = new Orgnization();

        BeanUtils.copyProperties(org,orgnization);
        if (org.getResources() != null && org.getResources().size() > 0){
            String resIds = "";
            String resNames = "";
            for (Tresource tresource : org.getResources()) {
                resIds += tresource.getId() + ",";
                resNames += tresource.getName() + ",";
            }
            if(resIds.length() > 0 && resNames.length() > 0){
                orgnization.setResourceIds(resIds.substring(0,resIds.length()-1));
                orgnization.setResourceNames(resNames.substring(0,resNames.length()-1));
            }
        }
        return orgnization;
    }

    @Override
    public void grant(Orgnization orgnization) {
        logger.info(orgnization.toString());
        Torganization org = orgnizationDao.get(Torganization.class,orgnization.getId());
        String orgids = orgnization.getResourceIds();
        if(orgids != null && !orgids.equalsIgnoreCase("")){
            String ids = "";
            boolean b = false;
            for (String id : orgids.split(",")) {
                if (b) {
                    ids += ",";
                } else {
                    b = true;
                }
                ids += "'" + id + "'";
            }
            logger.info("IDS:" + ids);
            List<Tresource> list = resourceDao.find("select distinct t from Tresource t where t.id in (" + ids + ")");
            logger.info("List:" + list.size());
            org.setResources(new HashSet<Tresource>(list));
        }
    }
}
