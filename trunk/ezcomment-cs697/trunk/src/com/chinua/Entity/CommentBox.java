package com.chinua.Entity;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@ManagedBean()
@RequestScoped
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CommentBox  implements java.io.Serializable{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	@Persistent
	private String site;
	@Persistent
	private String page;
	@Persistent
	private Long template;
	@Persistent
    private Date date;
	@Persistent
	private String owner;
	@Persistent
	private long read;
	@Persistent
	private long write;
	public CommentBox() {}
	public CommentBox(String site, String page,Long template, Date date,String owner) {
		this.site = site;
		this.page = page;
		this.template=template;
		this.date = date;
		this.owner=owner;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getRead() {
		return read;
	}
	public void setRead(long read) {
		this.read = read;
	}
	public long getWrite() {
		return write;
	}
	public void setWrite(long write) {
		this.write = write;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Long getTemplate() {
		return template;
	}
	public void setTemplate(Long template) {
		this.template = template;
	}
	
}
