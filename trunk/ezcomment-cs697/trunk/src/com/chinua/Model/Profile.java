package com.chinua.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.chinua.Entity.*;
import com.chinua.util.*;

import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.jdo.PersistenceManager;

@ManagedBean()
@SessionScoped
public class Profile  implements java.io.Serializable{
	@ManagedProperty(value="#{user}")
	User user;
	String role;
	List<Obj> allobj=new ArrayList<Obj>();
	Obj obj=new Obj();
	Alert alert=new Alert();
	public Profile(){
		
		//System.out.println("=="+this.toString()+"== FN:"+user.getFullName());
	}
	public String register()throws Exception{
		FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		ELResolver elResolver = fc.getApplication().getELResolver();
		User u = (User)(elResolver.getValue(
	            fc.getELContext(), null, "user"));
		String resp=this.Reg(u);
		System.out.println("==UserRegister=="+u.getUsername()+" resp:"+resp);
		return resp;
	}
	public String login()throws Exception{
		FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		ELResolver elResolver = fc.getApplication().getELResolver();
		User u = (User)(elResolver.getValue(
	            fc.getELContext(), null, "user"));
		String resp=this.autheticate(u);
		System.out.println("==UserLogin=="+u.getUsername()+" resp:"+resp);
		return resp;
	}
	public String Reg(User u){
		String str="main.xhtml";
		Date date = new Date();
		User duser=new User(u.getUsername(),u.getPassword(),u.getFullName(),u.getEmail(),date,"active","user");
		FacesContext context = FacesContext.getCurrentInstance();
		MailUtil mail=new MailUtil();
		/*
		boolean state=MailUtil.isValidEmailAddress(role);
		if(state==false){
			alert.Error("Please enter a vaild email addresss", context);
			return "";
		}
		*/
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(duser);
            this.user=duser;
            this.setUserRole(user.getRole());
            String s=mail.sendMail(user.getEmail(), user.getFullName(), user.getUsername(), user.getPassword(), 0);
            System.out.println("Mail: "+s);
            //user.setValue1(s);
        }
        catch(Exception e){
        	str="";
        	
			alert.Error("Sorry, registeration faied. Please try agin later", context);

        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
        return str;
	}
	
	public String getActiveUsers(){
		this.setAllobj(this.user2Obj(this.getUsers(1)));
		return "auser.xhtml";
	}
	public String getInActiveUsers(){
		this.setAllobj(this.user2Obj(this.getUsers(2)));
		return "inactive.xhtml";
	}
	public String getAdmins(){
		this.setAllobj(this.user2Obj(this.getUsers(3)));
		return "admins.xhtml";
	}
	public String getNonAdmins(){
		this.setAllobj(this.user2Obj(this.getUsers(4)));
		return "dusers.xhtml";
	}
	public String makeInactive() throws Exception{//from Active users page
		this.modUser(1);
		getActiveUsers();
		return null;
	}
	public String makeActive() throws Exception{// from Inactive users page
		this.modUser(2);
		getInActiveUsers();
		return null;
	}
	public String makeUser() throws Exception{// from Admin users page
		this.modUser(3);
		getAdmins();
		return null;
	}
	public String makeAdmin() throws Exception{// from user users page
		this.modUser(4);
		getNonAdmins();
		return null;
	}
	public void modUser(int mode)throws Exception{
		Long id=this.obj.getId();
		System.out.println("==modUser=="+id+" in mode::"+mode);
		User u2=new User();
		FacesContext context = FacesContext.getCurrentInstance();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
        	u2=pm.getObjectById(User.class, id);
        	if(mode==1) u2.setStatus("inactive");
        	if(mode==2) u2.setStatus("active");
        	if(mode==3) u2.setRole("user");
        	if(mode==4) u2.setRole("admin");
        	alert.Sucess("Done", context);
        }
        catch(Exception e){
        	e.printStackTrace();
        	alert.Error("Operation failed", context);
        } finally {
            pm.close();
            
        }
		
	}
	public List<User> getUsers(int mode){
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "";
	    if(mode==1)
	    	query ="select from " + User.class.getName() + " where status=='active' ";
	    if(mode==2)
	    	query ="select from " + User.class.getName() + " where status=='inactive' ";
	    if(mode==3)
	    	query ="select from " + User.class.getName() + " where status=='active' && role=='admin' ";
	    if(mode==4)
	    	query ="select from " + User.class.getName() + " where status=='active' && role=='user' ";
	    
	    return (List<User>) pm.newQuery(query).execute();
	}
	public List<Obj> user2Obj(List<User> dusers){
		List<Obj> objs=new ArrayList<Obj>();
		for (User u1:dusers){
			Obj obj=new Obj(u1.getFullName(),u1.getUsername()+" / "+u1.getEmail(),u1.getId());
			objs.add(obj);
			//System.out.println(u1.getFullName()+" :: "+u1.getUsername()+" st:"+u1.getStatus());
	  	}
			
		return objs;
	}
	public String forgotPass() throws Exception{
    	FacesContext context = FacesContext.getCurrentInstance();
	    Map requestParams = context.getExternalContext().getRequestParameterMap();
		this.role=(String) requestParams.get("toEmail");
		MailUtil mail=new MailUtil();
		boolean state=MailUtil.isValidEmailAddress(role);
		if(state==false){
			alert.Error("Please enter a vaild email addresss", context);
			return "";
		}
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + User.class.getName() + " where email=='"+role+"'  && status=='active'";
	    List<User> ur=(List<User>) pm.newQuery(query).execute();
	    int n=ur.size();
	    System.out.println("::==usersFound== "+n);
	    if(n>0){
	    	this.user=ur.get(0);
	    	this.setUserRole(user.getRole());
	    	System.out.println(user.getFullName()+" :: "+user.getUsername()+" st:"+user.getStatus());
	    	String s=mail.sendMail(user.getEmail(), user.getFullName(), user.getUsername(), user.getPassword(), 1);
            System.out.println("Mail: "+s);
            //if(s.equalsIgnoreCase("sucess"))
            	alert.Sucess("Your login details have been sent to your registered email address", context);
             }
	    else 
	    	alert.Error("Email address not found on our system", context);
	    return "";
	}
	public String autheticate(User u){
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + User.class.getName() + " where username=='"+u.getUsername()+"' && password=='"+u.getPassword()+"' && status=='active'";
	    List<User> ur=(List<User>) pm.newQuery(query).execute();
	    int n=ur.size();
	    System.out.println("::==usersFound== "+n);
	    if(n>0){
	    	this.user=ur.get(0);
	    	this.setUserRole(user.getRole());
	    	System.out.println(user.getFullName()+" :: "+user.getUsername()+" st:"+user.getStatus());
	    	//MyComments mc=new MyComments();
	    	//mc.getNewComments(user);
	    	return "main.xhtml";
	    }
	    else {
	    	this.user=u;
	    	//user.setValue1(" ");
	    	FacesContext context = FacesContext.getCurrentInstance();
			alert.Error("Invalid username/password", context);
	    	return "";
	    	}
	}
	public String logout() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();     
			ELResolver elResolver = fc.getApplication().getELResolver();
			     
			User user = (User)elResolver.getValue(
			           fc.getELContext(), null, "user");
			
			user.setUsername("");
			user.setPassword("");
			user.setFullName("");
			user.setEmail("");
			user.setValue1("");
			alert.Sucess("You have logged out", fc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return "login.xhtml"; 
	  }
	public void setUserRole(String userRole){
		System.out.println("==Seting role for:: "+userRole);
		if(userRole.equals("admin"))
			this.setRole("adminmenu.xhtml");
		else this.setRole("menu.xhtml");
		
	}
	public void validateEmail(FacesContext context, UIComponent toValidate, Object value) {
		String email = (String) value;

		//if (email.indexOf('@') == -1)
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
	      Matcher m = p.matcher(email);
	      if (!m.find()){
		((UIInput)toValidate).setValid(false);
		
		//FacesMessage message = new FacesMessage("Invalid Email address");
		//context.addMessage(toValidate.getClientId(context), message);
		alert.Error("Invalid Email address", context);
		}
	}
	public void validateUserName(FacesContext context, UIComponent toValidate, Object value) {
		String username = (String) value;

		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + User.class.getName() + " where username=='"+username+"' ";
	    List<User> ur=(List<User>) pm.newQuery(query).execute();
	    int n=ur.size();
	    if(n>0){		
		((UIInput)toValidate).setValid(false);
		
		//FacesMessage message = new FacesMessage("Invalid Email address");
		//context.addMessage(toValidate.getClientId(context), message);
		alert.Error("Username has been used, choose another one", context);
		}
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Obj> getAllobj() {
		return allobj;
	}
	public void setAllobj(List<Obj> allobj) {
		this.allobj = allobj;
	}
	public Obj getObj() {
		return obj;
	}
	public void setObj(Obj obj) {
		this.obj = obj;
	}
	public String getRole() {
		System.out.println("===roleMenu=="+role);
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
