package org.myhnuhai.pageModel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 马英虎 on 14-3-25.
 */
public class Orgnization implements Serializable {

    private String pid;// 虚拟属性，用于获得当前机构的父机构ID

    private String id;
    private Date createdatetime;
    private Date updatedatetime;
    private String name;
    private String address;
    private String code;
    private String iconCls;
    private Integer seq;

    private String resourceIds;
    private String resourceNames;

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getResourceNames() {
        return resourceNames;
    }

    public void setResourceNames(String resourceNames) {
        this.resourceNames = resourceNames;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }


    @Override
    public String toString() {
        return "Orgnization{" +
                "pid='" + pid + '\'' +
                ", id='" + id + '\'' +
                ", createdatetime=" + createdatetime +
                ", updatedatetime=" + updatedatetime +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", code='" + code + '\'' +
                ", iconCls='" + iconCls + '\'' +
                ", seq=" + seq +
                ", resourceIds='" + resourceIds + '\'' +
                ", resourceNames='" + resourceNames + '\'' +
                '}';
    }
}
