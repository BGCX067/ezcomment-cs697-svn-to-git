package com.chinua.Entity;

import com.google.appengine.api.datastore.Key;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Extension;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="user", eager=true)
@SessionScoped

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User implements java.io.Serializable{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	//@PrimaryKey
	@Persistent
	//@Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
    private String username;
	@Persistent
	private String password;
	@Persistent
	private String fullName;
	@Persistent
	private String email;
	@Persistent
	private Date dateReg;
	@Persistent
	private String status;
	@Persistent
	private String role;
	String value1;
	
	@Persistent
	private long read;
	@Persistent
	private long write;
	public User(){}
	public User(String username,String password,String fullName,
			String email,Date date,String st,String role){
		this.username=username;
		this.password=password;
		this.fullName=fullName;
		this.email=email;
		this.dateReg=date;
		this.status=st;
		this.role=role;
	}
	@Override
	public int hashCode() {
	        int result = 1;
	        result =((id == null) ? 0 : id.hashCode());
	        return result;
	}

	@Override
	public boolean equals(Object obj) {
	        if (this == obj)
	                return true;
	        if (obj == null)
	                return false;
	        if (getClass() != obj.getClass())
	                return false;
	        User other = (User) obj;
	        if (id == null) {
	                if (other.id != null)
	                        return false;
	        } else if (!id.equals(other.id))
	                return false;
	        return true;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDateReg() {
		return dateReg;
	}
	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}
		
	 public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
}
