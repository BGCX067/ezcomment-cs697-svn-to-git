package com.chinua.Model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinua.Entity.*;
import com.chinua.util.*;

import javax.el.ELResolver;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.jdo.PersistenceManager;

@ManagedBean()
@RequestScoped
public class MyCBox  implements java.io.Serializable{
	String link="http://ezcomment.appspot.com";// /pages/comments.faces?id=
	private List<CommentBox> cbs;
	private CommentBox combox;
	private String code;
	private String owner;
	private String text;
	private String templateId;
	private long tId;//try Long
	int val=0;
	private SelectItem si;
	private List<Template> mytemplates;
	private List<SelectItem> opts1;
	private List<String> opts;
	//MyValues myValues =new MyValues();
	Alert alert =new Alert();
	
	public  MyCBox(){
		opts1=new ArrayList<SelectItem>();
		opts=new ArrayList<String>();
		cbs=new ArrayList<CommentBox>();
		combox=new CommentBox();
		si=new SelectItem(23,"james");
		//this.getMyTemplates();//watch it, maybe a link shd do dis
	}
	public String create(){
		String str="newcbx.xhtml";
		Date date = new Date();
		FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		ELResolver elResolver = fc.getApplication().getELResolver();
		this.combox = (CommentBox)(elResolver.getValue(
	            fc.getELContext(), null, "commentBox"));
		//this.tId=(Long)si.getValue();
		//Map requestParams = fc.getExternalContext().getRequestParameterMap();
		//this.owner=(String) requestParams.get("ownerxxx");
		//combox.setOwner(this.owner);
		System.out.println("222==read owner "+combox.getOwner());
		this.tId=this.long2String(combox.getOwner());//this.getMyTemplate(combox.getOwner());
		this.owner=this.getUser("create CBX");//
		CommentBox c2=new CommentBox(combox.getSite(),combox.getPage(),tId,date,owner);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FacesContext context = FacesContext.getCurrentInstance();
        try {
            pm.makePersistent(c2);
            alert.Sucess("CommentBox created", context);
        }
        catch(Exception e){
        	str="";
        	//this.text="Sorry, commentBox could NOT be created. Please try agin later";
        	alert.Error("Sorry, commentBox could NOT be created. Please try agin later", context);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
        this.code=this.generateCode(this.getMaxCBXId(0));
        return this.getMyTemplates();
	}
	public long getMyTemplate(String name){//add owner
		this.owner=this.getUser("getMyTemplates");
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + Template.class.getName() + " where name=='"+name+"' ";//order by name 
	    this.mytemplates = (List<Template>) pm.newQuery(query).execute();
	    int n=mytemplates.size();
	    
	    if(n==0) return 0;
	    else return mytemplates.get(0).getId();
	}
	public String getMyCBS(){
		String str="viewcbx.xhtml";
		this.owner=this.getUser("getMyTemplates");
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + CommentBox.class.getName() + " where owner=='"+owner+"'  ";//order by site
	    this.cbs = (List<CommentBox>) pm.newQuery(query).execute();
	    int n=cbs.size();
	    if(n==0){
	    	FacesContext context = FacesContext.getCurrentInstance();
	    	alert.warn("No commentBox. Create one", context);
	    	
	    	//this.text="No commentBox. Create one!";
	    	str=this.getMyTemplates();
	    }
	    else this.text="";
	    System.out.print("Got commentBoxs::"+n);
	    return str;
	}
	public CommentBox getMaxCBXId(long cbxid){
		CommentBox c=new CommentBox();
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    try {
			String query="";
			if(cbxid==0)
				query= "select from " + CommentBox.class.getName() + " order by id desc ";
			else
				query= "select from " + CommentBox.class.getName() + " where id=="+cbxid+" ";
			this.cbs = (List<CommentBox>) pm.newQuery(query).execute();
			System.out.println("=====SizeFetched:: "+cbs.size());
			c=cbs.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return c;
	}
	public Template getTemplate(CommentBox c){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Template t=new Template();
        try {
        	long tempid=c.getTemplate();
        	t=pm.getObjectById(Template.class, tempid);
        	}
        catch(Exception e){
        	this.text="Sorry, an error occured! Please try again later";
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
		return t;
	}
	public String generateCode(CommentBox c){
		link=link+"/pages/comments.faces?id=";
		String str="";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Template t=new Template();
        try {
        	long tempid=c.getTemplate();
        	t=pm.getObjectById(Template.class, tempid);
        	this.text="Here is your code, copy and paste it where this commentBox is needed:";
        	//String style=" style='background:#"+t.getBgcolor()+"; color:#"+t.getFontcolor()+";  ' ";
        	str="<div id='ezLOAD'>Loading Content from <a href='http://ezcomment.appspot.com/pages/home.faces' target='blank'>ezComment</a>. Please wait ...<img src='http://ezcomment.appspot.com/resources/images/loading.gif'></div>";
        	str=str+" <div id='ezLOAD1' style='display:none'><iframe onLoad='ezChk()' src='"+link+c.getId().toString()+"' height='"+t.getHeight()+"' width='"+t.getWidth()+"' marginwidth='0' marginheight='0' hspace='0' vspace='0' frameborder='0' scrolling='auto' ></iframe></div>";
        	str=str+"<script>function ezChk(){document.getElementById('ezLOAD').style.display='none';document.getElementById('ezLOAD1').style.display='inline';}</script>";
        }
        catch(Exception e){
        	str="";
        	this.text="";
        	this.text="Sorry, error occured!. Please try agin later";
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
		return str;
	}
	public String getCBSAll(){
		String str="viewcbx.xhtml";
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + CommentBox.class.getName() + " ";//order by site 
	    this.cbs = (List<CommentBox>) pm.newQuery(query).execute();
	    int n=cbs.size();
	    if(n==0){
	    	FacesContext context = FacesContext.getCurrentInstance();
	    	alert.warn("No commentBox in ALL CommentBoxes. Create one", context);
	    	//this.text="No commentBox in ALL CommentBoxes. Create one!";
	    	str=this.getMyTemplates();
	    }
	    else this.text="";
	    System.out.print("Got commentBoxes::"+n);
	    return str;
	}
	public String mods()throws Exception{//wrk on dis guy, not getting template
		String str="viewtemp.xhtml";
		this.owner=this.getUser("mods");
		System.out.println("==MOD=="+this.combox.getId());
		CommentBox c2=new CommentBox();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FacesContext context = FacesContext.getCurrentInstance();
        try {
        	c2=pm.getObjectById(CommentBox.class, combox.getId());
        	c2.setPage(combox.getPage());
        	c2.setSite(combox.getSite());
        	c2.setTemplate(combox.getTemplate());
        	alert.Sucess("CommentBox modified", context);
        }
        catch(Exception e){
        	str="";
        	//this.text="Sorry, CommentBox could Not be modified. Please try agin later";
        	alert.Error("Sorry, CommentBox could Not be modified. Please try agin later", context);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
		return str;
	}
	
	public void valChanged(ValueChangeEvent e) {
		  String value = e.getComponent().toString();//.getValue();
		  this.tId = (new Integer(value)).intValue();
		  System.out.println("===TLD== "+tId);
		}
	
	public String dels()throws Exception{//chk for comments that will be ophraned
		String str="viewtemp.xhtml";
		this.owner=this.getUser("dels");
		System.out.println("==del=="+combox.getId());
		CommentBox c2= new CommentBox();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FacesContext context = FacesContext.getCurrentInstance();
        try {
        	c2=pm.getObjectById(CommentBox.class, combox.getId());
            pm.deletePersistent(c2);
            alert.Sucess("CommentBox deleted", context);
        }
        catch(Exception e){
        	str="";
        	//this.text="Sorry, CommentBox could Not be deleted. Please try agin later";
        	alert.Error("Sorry, CommentBox could Not be deleted. Please try agin later", context);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
		return str;
	}
	public String getMyTemplates(){
		opts.clear();
		this.owner=this.getUser("getMyTemplates--CBX");
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + Template.class.getName() + " where owner=='"+owner+"' order by name ";
	    this.mytemplates= (List<Template>) pm.newQuery(query).execute();
	    System.out.println(mytemplates.size()+" Templates for "+owner);
	    int n=mytemplates.size();
	    /*
	    if(n>0){
	    	opts.add("");
	    	for(Template t: mytemplates){
	    		opts.add(t.getName());
	    		opts1.add(new SelectItem(t.getId(),t.getName()));
	    		//opts.add(new SelectItem(t.getName()));
	    	}
	    		
	    }
	    
	     int n=templates.size();
	    if(n==0) this.text="No Templates";
	    else this.text="";
	    System.out.print("Got Tempaltes::"+n);
	    
	    */
	    return "newcbx.xhtml";
	}
	public String getUser(String str){
		User u=(User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		System.out.println(str+"==myCBox=== owner:"+u.getUsername());
		return u.getUsername();
	}
	public long long2String(String str){
		Long i;
		try {
			i = Long.parseLong(str.trim());
		} catch (NumberFormatException e) {
			i=0L;
			e.printStackTrace();
		}
		return i;
	}
	
	public List<CommentBox> getCbs() {
		return cbs;
	}
	public void setCbs(List<CommentBox> cbs) {
		this.cbs = cbs;
	}
	public CommentBox getCombox() {
		return combox;
	}
	public void setCombox(CommentBox combox) {
		this.combox = combox;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public List<Template> getMytemplates() {
		return mytemplates;
	}
	public void setMytemplates(List<Template> mytemplates) {
		this.mytemplates = mytemplates;
	}
	public List<SelectItem> getOpts1() {
		return opts1;
	}
	public void setOpts1(List<SelectItem> opts1) {
		this.opts1 = opts1;
	}
	public long gettId() {
		return tId;
	}
	public void settId(long tId) {
		this.tId = tId;
	}
	public SelectItem getSi() {
		return si;
	}
	public void setSi(SelectItem si) {
		this.si = si;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
	public List<String> getOpts() {
		return opts;
	}
	public void setOpts(List<String> opts) {
		this.opts = opts;
	}
	
}
