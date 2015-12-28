package com.chinua.Entity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean()
@SessionScoped
public class MyValues implements java.io.Serializable{
	String owner;
	long tempid;
	public MyValues(){}
	public MyValues(String owner){
		this.owner=owner;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public long getTempid() {
		return tempid;
	}

	public void setTempid(long tempid) {
		this.tempid = tempid;
	}

	
	
}
