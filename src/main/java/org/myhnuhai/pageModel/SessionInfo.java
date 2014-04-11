package org.myhnuhai.pageModel;

import org.myhnuhai.model.Torganization;
import org.myhnuhai.model.Trole;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 马英虎 on 14-3-15.
 */
public class SessionInfo {
    private String ip;// 此属性不存数据库，虚拟属性

    private String id;
    private Date createdatetime;
    private Date updatedatetime;
    private String loginname;
    private String pwd;
    private String name;
    private String sex;
    private Integer age;
    private String photo;

    private String roleIds;
    private String roleNames;

    private Date createdatetimeStart;
    private Date createdatetimeEnd;

    private Date modifydatetimeStart;
    private Date modifydatetimeEnd;

    private Set<Torganization> organizations = new HashSet<Torganization>(0);
    private Set<Trole> roles = new HashSet<Trole>(0);

    @Override
    public String toString() {
        return "SessionInfo{" +
                "ip='" + ip + '\'' +
                ", id='" + id + '\'' +
                ", createdatetime=" + createdatetime +
                ", updatedatetime=" + updatedatetime +
                ", loginname='" + loginname + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", photo='" + photo + '\'' +
                ", roleIds='" + roleIds + '\'' +
                ", roleNames='" + roleNames + '\'' +
                ", createdatetimeStart=" + createdatetimeStart +
                ", createdatetimeEnd=" + createdatetimeEnd +
                ", modifydatetimeStart=" + modifydatetimeStart +
                ", modifydatetimeEnd=" + modifydatetimeEnd +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(Date createdatetime) {
        this.createdatetime = createdatetime;
    }

    public Date getUpdatedatetime() {
        return updatedatetime;
    }

    public void setUpdatedatetime(Date updatedatetime) {
        this.updatedatetime = updatedatetime;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public Date getCreatedatetimeStart() {
        return createdatetimeStart;
    }

    public void setCreatedatetimeStart(Date createdatetimeStart) {
        this.createdatetimeStart = createdatetimeStart;
    }

    public Date getCreatedatetimeEnd() {
        return createdatetimeEnd;
    }

    public void setCreatedatetimeEnd(Date createdatetimeEnd) {
        this.createdatetimeEnd = createdatetimeEnd;
    }

    public Date getModifydatetimeStart() {
        return modifydatetimeStart;
    }

    public void setModifydatetimeStart(Date modifydatetimeStart) {
        this.modifydatetimeStart = modifydatetimeStart;
    }

    public Date getModifydatetimeEnd() {
        return modifydatetimeEnd;
    }

    public void setModifydatetimeEnd(Date modifydatetimeEnd) {
        this.modifydatetimeEnd = modifydatetimeEnd;
    }

    public Set<Torganization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Torganization> organizations) {
        this.organizations = organizations;
    }

    public Set<Trole> getRoles() {
        return roles;
    }

    public void setRoles(Set<Trole> roles) {
        this.roles = roles;
    }
}
