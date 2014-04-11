package org.myhnuhai.pageModel;

import java.util.Date;

public class Role implements java.io.Serializable {

	private String id;
	private String name;
	private String remark;
	private Integer seq;
	private String iconCls;

    private Date createdatetime;
    private Date updatedatetime;

	private String resourceIds;
	private String resourceNames;


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

    public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}


    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +

                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", seq=" + seq +
                ", iconCls='" + iconCls + '\'' +
                ", createdatetime=" + createdatetime +
                ", updatedatetime=" + updatedatetime +
                ", resourceIds='" + resourceIds + '\'' +
                ", resourceNames='" + resourceNames + '\'' +
                '}';
    }
}
