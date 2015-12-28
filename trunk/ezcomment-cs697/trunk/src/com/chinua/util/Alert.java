package com.chinua.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Alert implements java.io.Serializable {

	public void Sucess(String msg,FacesContext context){
		 //= FacesContext.getCurrentInstance();
	    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
	            "Sucessful", msg+" !"));
	    System.out.println("Alert::::"+msg);
	}
	public void Error(String msg,FacesContext context){
	    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
	            "Error", msg+" !"));
	    System.out.println("Alert::::"+msg);
	}
	public void warn(String msg,FacesContext context){
	    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
	            "Warning", msg+" !"));
	    System.out.println("Alert::::"+msg);
	}
}
