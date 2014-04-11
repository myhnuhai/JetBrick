package org.myhnuhai.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TORGANIZATION", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Torganization implements java.io.Serializable {

	private String pid;// 虚拟属性，用于获得当前机构的父机构ID

	private String id;
	private Date createdatetime;
	private Date updatedatetime;
	private String name;
	private String address;
	private String code;
	private String iconCls;
	private Integer seq;
	private Torganization organization;
	private Set<Torganization> organizations = new HashSet<Torganization>(0);
	private Set<Tuser> users = new HashSet<Tuser>(0);
	private Set<Tresource> resources = new HashSet<Tresource>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID")
    public Torganization getOrganization() {
        return organization;
    }

    public void setOrganization(Torganization organization) {
        this.organization = organization;
    }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
    public Set<Torganization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Torganization> organizations) {
        this.organizations = organizations;
    }
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TUSER_TORGANIZATION", schema = "", joinColumns = { @JoinColumn(name = "ORGANIZATION_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) })
    public Set<Tuser> getUsers() {
        return users;
    }

    public void setUsers(Set<Tuser> users) {
        this.users = users;
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TORGANIZATION_TRESOURCE", schema = "", joinColumns = { @JoinColumn(name = "ORGANIZATION_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false) })
    public Set<Tresource> getResources() {
        return resources;
    }

    public void setResources(Set<Tresource> resources) {
        this.resources = resources;
    }

    @Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		if (!StringUtils.isBlank(this.id)) {
			return this.id;
		}
		return UUID.randomUUID().toString();
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATEDATETIME", length = 19)
	public Date getUpdatedatetime() {
		if (this.updatedatetime != null)
			return this.updatedatetime;
		return new Date();
	}

	public void setUpdatedatetime(Date updatedatetime) {
		this.updatedatetime = updatedatetime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATETIME", length = 19)
	public Date getCreatedatetime() {
		if (this.createdatetime != null)
			return this.createdatetime;
		return new Date();
	}

	public void setCreatedatetime(Date createdatetime) {
		this.createdatetime = createdatetime;
	}

	@Column(name = "NAME", length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ADDRESS", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "CODE", length = 200)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "ICONCLS", length = 100)
	public String getIconCls() {
		return this.iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	@Column(name = "SEQ", precision = 8, scale = 0)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * 用于业务逻辑的字段，注解@Transient代表不需要持久化到数据库中
	 * 
	 * @return
	 */
	@Transient
	public String getPid() {
		if (organization != null && !StringUtils.isBlank(organization.getId())) {
			return organization.getId();
		}
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
