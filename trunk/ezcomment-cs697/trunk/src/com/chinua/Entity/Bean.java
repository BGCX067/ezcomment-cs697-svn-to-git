package com.chinua.Entity;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean()
@RequestScoped
public class Bean implements java.io.Serializable {
	String text;
	public Bean(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String page = params.get("page");
		page=page+".xhtml";
		System.out.println("==Page== "+page);
		this.text= page;
	}
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
