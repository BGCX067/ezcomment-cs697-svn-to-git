package com.chinua.Model;


import java.util.Date;
import java.util.List;

import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.jdo.PersistenceManager;

import com.chinua.Entity.*;
import com.chinua.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.RequestScoped;

@ManagedBean()
@RequestScoped
public class MyTemplates  implements java.io.Serializable{
	Template template;
	String text;
	List<Template> templates;//=new List<Template>();
	String owner;
	//User user;
	Alert alert =new Alert();
	public MyTemplates(){
		//templates=new ArrayList<Template>();
		//System.out.println("11=="+this.toString()+"== FN:"+user.getFullName());
		//this.owner=this.getUser("constructor");
	}
	public String create()throws Exception{
		this.owner=this.getUser("create");
		FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		ELResolver elResolver = fc.getApplication().getELResolver();
		Template temp = (Template)(elResolver.getValue(
	            fc.getELContext(), null, "template"));
		//User u = (User)(elResolver.getValue(
	     //       fc.getELContext(), null, "user"));
		String resp=this.make(temp);
		System.out.println("==createTemplate=="+temp.getName()+" resp:"+resp);
		return null;
	}
	public String make(Template t){
		String str="main.xhtml";
		this.owner=this.getUser("make");
		t.setHeight(t.getHeight()+t.getHunit());
		t.setWidth(t.getWidth()+t.getWunit());
		System.out.println("--w:"+t.getWidth()+" h:"+t.getHeight());
		Template tp=new Template(t.getName(),t.getDescription(),t.getHeight(),t.getWidth(),t.getFontcolor(),t.getBgcolor(),owner);
		System.out.println(t.getName()+" === o:"+owner+" f:"+t.getFontcolor()+" b:"+t.getBgcolor());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FacesContext context = FacesContext.getCurrentInstance();
		
        try {
            pm.makePersistent(tp);
            alert.Sucess("Template created", context);
        }
        catch(Exception e){
        	str="";
        	//this.text="Sorry, template could Not be created. Please try agin later";
        	alert.Error("Sorry, template could Not be created. Please try again later", context);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
        return str;
	}
	public String getaTemplate(){
		String str="modetemp.xhtml";
		/*FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		ELResolver elResolver = fc.getApplication().getELResolver();
		MyValues myvals = (MyValues)(elResolver.getValue(
	            fc.getELContext(), null, "myMyValues"));
		long patchid=myvals.getTempid();
		System.out.println("==PatchID=="+patchid);
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
        	this.template=pm.getObjectById(Template.class, patchid);
        	
        }
        catch(Exception e){
        	e.printStackTrace();
        } finally {
            pm.close();
        }*/
		this.owner=this.getUser("getTemplate");
		System.out.println(template.getName()+" === o:"+owner+" f:"+template.getFontcolor()+" b:"+template.getBgcolor());
		return str;
	}
	public String getMyTemplates(){//add owner
		this.owner=this.getUser("getMyTemplates");
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + Template.class.getName() + " where owner=='"+owner+"' ";//order by name 
	    this.templates = (List<Template>) pm.newQuery(query).execute();
	    int n=templates.size();
	    if(n==0) this.text="No Templates";
	    else this.text="";
	    System.out.print("Got Tempaltes::"+n);
	    return "viewtemp.xhtml";
	}
	public String getMyTemplatesAll(){
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + Template.class.getName() + " ";//order by name 
	    this.templates = (List<Template>) pm.newQuery(query).execute();
	    int n=templates.size();
	    if(n==0) this.text="No Templates in All TEMPLATES";
	    else this.text="All TEMPLATES";
	    System.out.print("Got Tempaltes::"+n);
	    return "viewtemp.xhtml";
	}
	public String mods()throws Exception{//wrk on dis guy, not getting template
		String str="viewtemp.xhtml";
		this.owner=this.getUser("mods");
		System.out.println("==MOD=="+template.getId());
		Template t2=new Template();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FacesContext context = FacesContext.getCurrentInstance();
        try {
        	t2=pm.getObjectById(Template.class, template.getId());
        	//t2=template;
        	t2.setBgcolor(template.getBgcolor());
        	t2.setDescription(template.getDescription());
        	t2.setFontcolor(template.getFontcolor());
        	t2.setHeight(template.getHeight());
        	t2.setName(template.getName());
        	t2.setWidth(template.getWidth());
        	alert.Sucess("Template modified", context);
        }
        catch(Exception e){
        	str="";
        	//this.text="Sorry, template could Not be modified. Please try agin later";
        	alert.Error("Sorry, template could Not be modified. Please try agin later", context);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
		return str;
	}
	public String dels()throws Exception{ //err javax.jdo.JDOUserException: Transient instances cant be deleted.
		String str="viewtemp.xhtml";
		this.owner=this.getUser("create");
		//FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		//ELResolver elResolver = fc.getApplication().getELResolver();
		//Template temp = (Template)(elResolver.getValue(
	    //        fc.getELContext(), null, "template"));
		//temp.setId(this.getTemplate().getId());
		//System.out.println("==del=="+temp.getId());
		//FacesContext context = FacesContext.getCurrentInstance();
		FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		ELResolver elResolver = fc.getApplication().getELResolver();
		MyValues myvals = (MyValues)(elResolver.getValue(
	            fc.getELContext(), null, "myMyValues"));
		long patchid=myvals.getTempid();
		System.out.println("==PatchID=="+patchid);
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
        	this.template=pm.getObjectById(Template.class,patchid);
            pm.deletePersistent(template);
            alert.Sucess("Template deleted", fc);
        }
        catch(Exception e){
        	str="";
        	//this.text="Sorry, template could Not be deleted. Please try agin later";
        	alert.Error("Sorry, template could Not be deleted. Please try agin later", fc);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
		return str;
	}
	public String getUser(String str){
		User u=(User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		System.out.println(str+"==myTemplate=== owner:"+u.getUsername());
		return u.getUsername();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public List<Template> getTemplates() {
		return templates;
	}
	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}
	/*
	 public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	*/
}
