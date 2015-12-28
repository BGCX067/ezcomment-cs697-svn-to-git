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

@ManagedBean()
@RequestScoped

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Comment  implements java.io.Serializable{
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String author;
    
    @Persistent
    private String email;
    
    @Persistent
    private String parent;
    
    @Persistent
    private String content;

    @Persistent
    private Date date;
    
    private String temp;
    
    public Comment(){ }
    public Comment(String author,String email, String content, Date date,String parent) {
        this.author = author;
        this.content = content;
        this.date = date;
        this.email=email;
        this.parent=parent;
    }

    public Key getKey() {
        return key;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
}
