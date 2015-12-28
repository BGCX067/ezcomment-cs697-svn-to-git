package com.chinua.Entity;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean()
@RequestScoped
public class Obj  implements java.io.Serializable{

	String value;
	String text;
	Long id;
	public Obj(){}
	public Obj(String text,String value){
		this.text=text;
		this.value=value;
	}
	public Obj(String text,String value,Long id){
		this.text=text;
		this.value=value;
		this.id=id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
