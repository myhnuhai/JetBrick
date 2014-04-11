package org.myhnuhai.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TROLE", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Trole implements java.io.Serializable {

	private String id;
	private Date createdatetime;
	private Date updatedatetime;
	private String name;
	private String description;
	private String iconCls;
	private Integer seq;
	private Set<Tuser> users = new HashSet<Tuser>(0);
	private Set<Tresource> resources = new HashSet<Tresource>(0);


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TUSER_TROLE", schema = "", joinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) })
    public Set<Tuser> getUsers() {
        return users;
    }

    public void setUsers(Set<Tuser> users) {
        this.users = users;
    }
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TROLE_TRESOURCE", schema = "", joinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false) })
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
	@Column(name = "UPDATEDATETIME", length =19)
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

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 200)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}
