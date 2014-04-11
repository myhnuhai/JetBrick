package org.myhnuhai.service.admin.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.myhnuhai.dao.admin.*;
import org.myhnuhai.model.*;
import org.myhnuhai.service.admin.IInitService;
import org.myhnuhai.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InitServiceImpl implements IInitService {
    private final static Logger logger = Logger.getLogger(InitServiceImpl.class);
    private static final String FILEPATH = "initDataBase.xml";
    @Autowired
    private IUserDao userDao;

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private IResourceDao resourceDao;

    @Autowired
    private IResourceTypeDao resourceTypeDao;

    @Autowired
    private IOrgnizationDao orgnizationDao;

    @Override
    synchronized public void init() {
        try {
           Document document = new SAXReader().read(Thread.currentThread().getContextClassLoader().getResourceAsStream(FILEPATH));
//            initResourceType(document);// 初始化资源类型
//            initResource(document);// 初始化资源
//            initRole(document);// 初始化角色
//            initUser(document);// 初始化用户
//            initRoleResource(document);// 初始化角色和资源的关系
            //initOrganization(document);// 初始化机构
//            initOrganizationResource(document);// 初始化机构和资源的关系
//            initUserRole(document);// 初始化用户和角色的关系
//            initUserOrganization(document);// 初始化用户和机构的关系
            initOrg();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void initOrg() {
        try {

            File file = new File("/data.txt");
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),"utf-8");// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] tem = lineTxt.split("\\s");
                    System.out.println(tem[0] + "|" + tem[1] + "|" + tem[2] + "|" + tem[3]);

                    Torganization organization = orgnizationDao.get(Torganization.class, tem[0]);
                    if (organization == null) {
                        organization = new Torganization();
                    }
                    organization.setId(tem[0]);
                    organization.setName(tem[1]);
                    organization.setAddress(tem[1]);
                    if(tem[5].equals("")){
                        organization.setSeq(0);
                    }
                    organization.setIconCls("brick");
                    organization.setCode(tem[3]);
                    if (!StringUtils.isBlank(tem[2])) {
                        organization.setOrganization((Torganization) orgnizationDao.get(Torganization.class, tem[2]));
                    } else {
                        organization.setOrganization(null);
                    }
                    orgnizationDao.save(organization);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
    private void initResource(Document document) {
        List childNodes = document.selectNodes("//resources/resource");
        for (Object obj : childNodes) {
            Node node = (Node) obj;
            Tresource resource = resourceDao.get(Tresource.class, node.valueOf("@id"));
            if (resource == null) {
                resource = new Tresource();
            }
            resource.setId(node.valueOf("@id"));
            resource.setName(node.valueOf("@name"));
            resource.setUrl(node.valueOf("@url"));
            resource.setDescription(node.valueOf("@description"));
            resource.setIconCls(node.valueOf("@iconCls"));
            resource.setSeq(Integer.parseInt(node.valueOf("@seq")));

            if (!StringUtils.isBlank(node.valueOf("@target"))) {
                resource.setTarget(node.valueOf("@target"));
            } else {
                resource.setTarget("");
            }

            if (!StringUtils.isBlank(node.valueOf("@pid"))) {
                resource.setResource(resourceDao.get(Tresource.class, node.valueOf("@pid")));
            } else {
                resource.setResource(null);
            }

            Node n = node.selectSingleNode("resourcetype");
            Tresourcetype type = resourceTypeDao.getById(n.valueOf("@id"));
            if (type != null) {
                resource.setResourcetype(type);
            }

            logger.info(JSON.toJSONStringWithDateFormat(resource, "yyyy-MM-dd HH:mm:ss"));
            resourceDao.saveOrUpdate(resource);
        }

    }

    private void initResourceType(Document document) {

        List childNodes = document.selectNodes("//resourcetypes/resourcetype");
        for (Object obj : childNodes) {
            Node node = (Node) obj;
            Tresourcetype type = resourceTypeDao.getById(node.valueOf("@id"));
            if (type == null) {
                type = new Tresourcetype();
            }
            type.setId(node.valueOf("@id"));
            type.setName(node.valueOf("@name"));
            type.setDescription(node.valueOf("@description"));
            logger.info(JSON.toJSONStringWithDateFormat(type, "yyyy-MM-dd HH:mm:ss"));
            resourceTypeDao.saveOrUpdate(type);
        }
    }

    private void initRole(Document document) {
        List childNodes = document.selectNodes("//roles/role");
        for (Object obj : childNodes) {
            Node node = (Node) obj;
            Trole role = roleDao.get(Trole.class, node.valueOf("@id"));
            if (role == null) {
                role = new Trole();
            }
            role.setId(node.valueOf("@id"));
            role.setName(node.valueOf("@name"));
            role.setDescription(node.valueOf("@description"));
            role.setSeq(Integer.parseInt(node.valueOf("@seq")));
            logger.info(JSON.toJSONStringWithDateFormat(role, "yyyy-MM-dd HH:mm:ss"));
            roleDao.saveOrUpdate(role);
        }
    }

    private void initUser(Document document) {

        List<Node> childNodes = document.selectNodes("//users/user");
        for (Node node : childNodes) {
            Tuser user = userDao.get(Tuser.class, node.valueOf("@id"));
            if (user == null) {
                user = new Tuser();
            }
            user.setId(node.valueOf("@id"));
            user.setName(node.valueOf("@name"));
            user.setLoginname(node.valueOf("@loginname"));
            user.setPwd(MD5Util.md5(node.valueOf("@pwd")));
            user.setSex(node.valueOf("@sex"));
            user.setAge(Integer.valueOf(node.valueOf("@age")));
            logger.info(JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss"));
            List<Tuser> ul = userDao.find("from Tuser u where u.loginname = '" + user.getLoginname() + "' and u.id != '" + user.getId() + "'");
            for (Tuser u : ul) {
                userDao.delete(u);
            }
            userDao.saveOrUpdate(user);
        }
    }

    private void initRoleResource(Document document) {
        List<Node> childNodes = document.selectNodes("//roles_resources/role");
        for (Node node : childNodes) {
            Trole role = roleDao.get(Trole.class, node.valueOf("@id"));
            if (role != null) {
                role.setResources(new HashSet());
                List<Node> cNodes = node.selectNodes("resource");
                for (Node n : cNodes) {
                    Tresource resource = resourceDao.get(Tresource.class, n.valueOf("@id"));
                    if (resource != null) {
                        role.getResources().add(resource);
                    }
                }
                logger.info(JSON.toJSONStringWithDateFormat(role, "yyyy-MM-dd HH:mm:ss"));
                roleDao.saveOrUpdate(role);
            }
        }

        Trole role =  roleDao.get(Trole.class, "0");// 将角色为0的超级管理员角色，赋予所有权限
        if (role != null) {
            role.getResources().addAll(resourceDao.find("from Tresource t"));
            logger.info(JSON.toJSONStringWithDateFormat(role, "yyyy-MM-dd HH:mm:ss"));
            roleDao.saveOrUpdate(role);
        }
    }

    private void initOrganization(Document document) {
        List childNodes = document.selectNodes("//organizations/organization");
        for (Object obj : childNodes) {
            Node node = (Node) obj;
            Torganization organization = orgnizationDao.get(Torganization.class, node.valueOf("@id"));
            if (organization == null) {
                organization = new Torganization();
            }
            organization.setId(node.valueOf("@id"));
            organization.setName(node.valueOf("@name"));
            organization.setAddress(node.valueOf("@address"));
            organization.setSeq(Integer.parseInt(node.valueOf("@seq")));
            organization.setIconCls(node.valueOf("@iconCls"));

            if (!StringUtils.isBlank(node.valueOf("@pid"))) {
                organization.setOrganization((Torganization) orgnizationDao.get(Torganization.class, node.valueOf("@pid")));
            } else {
                organization.setOrganization(null);
            }

            logger.info(JSON.toJSONStringWithDateFormat(organization, "yyyy-MM-dd HH:mm:ss"));
            orgnizationDao.saveOrUpdate(organization);
        }
    }

    private void initOrganizationResource(Document document) {
        List<Node> childNodes = document.selectNodes("//organizations_resources/organization");
        for (Node node : childNodes) {
            Torganization organization =  orgnizationDao.get(Torganization.class, node.valueOf("@id"));
            if (organization != null) {
                organization.setResources(new HashSet());
                List<Node> cNodes = node.selectNodes("resource");
                for (Node n : cNodes) {
                    Tresource resource =  resourceDao.get(Tresource.class, n.valueOf("@id"));
                    if (resource != null) {
                        organization.getResources().add(resource);
                    }
                }
                logger.info(JSON.toJSONStringWithDateFormat(organization, "yyyy-MM-dd HH:mm:ss"));
                orgnizationDao.saveOrUpdate(organization);
            }
        }
    }

    private void initUserRole(Document document) {
        List<Node> childNodes = document.selectNodes("//users_roles/user");
        for (Node node : childNodes) {
            Tuser user =userDao.get(Tuser.class, node.valueOf("@id"));
            if (user != null) {
                user.setRoles(new HashSet());
                List<Node> cNodes = node.selectNodes("role");
                for (Node n : cNodes) {
                    Trole role = roleDao.get(Trole.class, n.valueOf("@id"));
                    if (role != null) {
                        user.getRoles().add(role);
                    }
                }
                logger.info(JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss"));
                userDao.saveOrUpdate(user);
            }
        }

        Tuser user = (Tuser) userDao.get(Tuser.class, "0");// 用户为0的超级管理员，赋予所有角色
        if (user != null) {
            user.getRoles().addAll(roleDao.find("from Trole"));
            logger.info(JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss"));
            userDao.saveOrUpdate(user);
        }
    }

    private void initUserOrganization(Document document) {
        List<Node> childNodes = document.selectNodes("//users_organizations/user");
        for (Node node : childNodes) {
            Tuser user =userDao.get(Tuser.class, node.valueOf("@id"));
            if (user != null) {
                user.setOrganizations(new HashSet());
                List<Node> cNodes = node.selectNodes("organization");
                for (Node n : cNodes) {
                    Torganization organization =orgnizationDao.get(Torganization.class, n.valueOf("@id"));
                    if (organization != null) {
                        user.getOrganizations().add(organization);
                    }
                }
                logger.info(JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss"));
                userDao.saveOrUpdate(user);
            }
        }

        Tuser user = userDao.get(Tuser.class, "0");// 用户为0的超级管理员，赋予所有机构
        if (user != null) {
            user.getOrganizations().addAll(orgnizationDao.find("from Torganization"));
            logger.info(JSON.toJSONStringWithDateFormat(user, "yyyy-MM-dd HH:mm:ss"));
            userDao.saveOrUpdate(user);
        }
    }
}
