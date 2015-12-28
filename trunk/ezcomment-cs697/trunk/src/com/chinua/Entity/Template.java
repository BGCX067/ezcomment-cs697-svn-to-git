package com.chinua.Entity;
import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.users.User;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="template", eager=true)
@RequestScoped

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Template  implements java.io.Serializable{
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	//@PrimaryKey
	@Persistent
	String name;
	@Persistent
	String description;
	@Persistent
	String height;
	@Persistent
	String width;
	@Persistent
	String fontcolor;
	@Persistent
	String bgcolor;
	@Persistent
	String owner;
	
	String wunit;
	String hunit;
	
	public Template(){}
	public Template(String name,String description,String height,String width,
			String fontcolor,String bgcolor,String owner){
		this.name=name;
		this.description=description;
		this.height=height;
		this.width=width;
		this.fontcolor=fontcolor;
		this.bgcolor=bgcolor;
		this.owner=owner;
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
	        Template other = (Template) obj;
	        if (id == null) {
	                if (other.id != null)
	                        return false;
	        } else if (!id.equals(other.id))
	                return false;
	        return true;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getFontcolor() {
		return fontcolor;
	}
	public void setFontcolor(String fontcolor) {
		this.fontcolor = fontcolor;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWunit() {
		return wunit;
	}
	public void setWunit(String wunit) {
		this.wunit = wunit;
	}
	public String getHunit() {
		return hunit;
	}
	public void setHunit(String hunit) {
		this.hunit = hunit;
	}
	
}
