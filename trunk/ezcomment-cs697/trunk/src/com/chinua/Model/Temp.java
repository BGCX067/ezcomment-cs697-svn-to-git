package com.chinua.Model;

import java.util.List;
import java.util.Map;

import javax.el.ELResolver;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.jdo.PersistenceManager;

import com.chinua.Entity.*;
import com.chinua.util.*;

@ManagedBean()
@RequestScoped
public class Temp implements java.io.Serializable {
	Template template;
	MyValues myValues;
	CommentBox cbx;
	List<Comment> comments;
	String text;
	Alert alert =new Alert();
	Long myID;
	public Temp(){
		System.out.println("==Temp=="+this.toString());
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		String mode=params.get("mode");
		if(mode==null){System.out.println("=====NO MODE=====  ");}
		else{
			if(id==null){System.out.println("=====NO ID=====  ");}
			else{
				String str="0000";
				mode=mode.trim();
				System.out.println("==MOD =="+mode+"== id::"+id+"::");
				if(mode.equals("mod")){
					Long tempid=Long.parseLong(id);
					str=getaTemplate(tempid);
				}else if(mode.equals("deltemp")){
					Long tempid=Long.parseLong(id);
					str=dels(tempid);
				}else if(mode.equals("delcbx")){
					Long tempid=Long.parseLong(id);
					str=this.delsCBX(tempid);
				}else if(mode.equals("delcmt")){
					Long tempid=Long.parseLong(id);
					str=this.delsCMT(tempid);
				}
				else if(mode.equals("vcmt")){
					str=getMyComments(id);
				}else{
					str="NO option";
				}
				System.out.println("Resp:::"+str);
			}
		}
	}
	public String getaTemplate(Long id){
		System.out.println("Get A template id::"+id);
		String str="modetemp.xhtml";
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
        	this.template=pm.getObjectById(Template.class, id);
        }
        catch(Exception e){
        	e.printStackTrace();
        } finally {
            pm.close();
        }
		System.out.println(template.getName()+" === o:"+template.getOwner()+" f:"+template.getFontcolor()+" b:"+template.getBgcolor());
		return str;
	}
	public String getMyComments(String owner){//add owner
		System.out.println("GetComments owner::"+owner);
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + Comment.class.getName() + " where parent=='"+owner+"' order by date desc ";
	    this.comments = (List<Comment>) pm.newQuery(query).execute();
	    int n=comments.size();
	    if(n==0) this.text="No Comments";
	    else this.text="";
	    System.out.print("Got Comments::"+n);
	    return this.text;
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
        }
		*/
		System.out.println(template.getName()+" === o:"+template.getOwner()+" f:"+template.getFontcolor()+" b:"+template.getBgcolor());
		return str;
	}
	public String mods()throws Exception{//wrk on dis guy, not getting template
		String str="";//"viewtemp.xhtml";
		//this.owner=this.getUser("mods");
		FacesContext fc = FacesContext.getCurrentInstance();  
		ELResolver elResolver = fc.getApplication().getELResolver();
		Temp temp1 = (Temp)(elResolver.getValue(
	            fc.getELContext(), null, "temp"));
		Map requestParams = fc.getExternalContext().getRequestParameterMap();
		String id=(String)requestParams.get("userreg:id");
		System.out.println("ID--"+id+"--");
		Long num=0L;
		num=Long.parseLong(id.trim());
		//template.setId(num);
		Template template=new Template();
		template.setBgcolor((String) requestParams.get("userreg:bgcolor"));
		template.setDescription((String) requestParams.get("userreg:desc"));
		template.setFontcolor((String) requestParams.get("userreg:fcolor"));
		template.setHeight((String) requestParams.get("userreg:height"));
		template.setName((String) requestParams.get("userreg:name"));
		template.setWidth((String) requestParams.get("userreg:width"));
		System.out.println(num+"==MOD== -name:"+template.getName());
		//this.template=temp1.getTemplate();
		Template t2=new Template();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FacesContext context = FacesContext.getCurrentInstance();
        try {
        	t2=pm.getObjectById(Template.class, num);
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
        //MyTemplates mt=new MyTemplates();
        //mt.getMyTemplates();
		return str;
	}
	public String dels(long id){ 
		System.out.println("Del template id::"+id);
		String str="viewtemp.xhtml";
		FacesContext fc = FacesContext.getCurrentInstance();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//chk if any commentBox is using it!!
		String query= "select from " + CommentBox.class.getName() + " where template=="+id+" ";
		List<CommentBox> cbs = (List<CommentBox>) pm.newQuery(query).execute();
		if(cbs.size()==0){
			try {
	        	this.template=pm.getObjectById(Template.class,id);
	            pm.deletePersistent(template);
	            this.text="Template deleted";
	        }
	        catch(Exception e){
	        	str="";
	        	this.text="Sorry, template could Not be deleted. Please try agin later";
	        	e.printStackTrace();
	        } finally {
	            pm.close();
	            
	        }
		}
		else{
			System.out.println("=====SizeFetched:: "+cbs.size());
			this.text="Template can not be deleted because it is being used by "+cbs.size()+" CommentBoxes";
		}
        
		return str;
	}
	public String delsCBX(long id){ 
		System.out.println("Del CBX id::"+id);
		String str="viewtemp.xhtml";
		FacesContext fc = FacesContext.getCurrentInstance();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//chk if any commentBox is using it!!
		String query= "select from " + Comment.class.getName() + " where parent=='"+id+"' ";
		comments = (List<Comment>) pm.newQuery(query).execute();
		if(comments.size()==0){
			try {
	        	this.cbx=pm.getObjectById(CommentBox.class,id);
	            pm.deletePersistent(cbx);
	            this.text="CommentBox deleted";
	        }
	        catch(Exception e){
	        	str="";
	        	this.text="Sorry, CommentBox could Not be deleted. Please try agin later";
	        	e.printStackTrace();
	        } finally {
	            pm.close();
	            
	        }
		}
		else{
			System.out.println("=====SizeFetched:: "+comments.size());
			this.text="CommentBox can not be deleted because it has "+comments.size()+" Comments";
		}
        
		return str;
	}
	public String delsCMT(long id){ 
		System.out.println("Del CBX id::"+id);
		String str="viewtemp.xhtml";
		FacesContext fc = FacesContext.getCurrentInstance();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
			try {
	        	Comment c=pm.getObjectById(Comment.class,id);
	            pm.deletePersistent(c);
	            this.text="Comment deleted";
	        }
	        catch(Exception e){
	        	str="";
	        	this.text="Sorry, Comment could Not be deleted. Please try agin later";
	        	e.printStackTrace();
	        } finally {
	            pm.close();
	            
	        }
	  return str;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public MyValues getMyValues() {
		return myValues;
	}
	public void setMyValues(MyValues myValues) {
		this.myValues = myValues;
	}
	public CommentBox getCbx() {
		return cbx;
	}
	public void setCbx(CommentBox cbx) {
		this.cbx = cbx;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getMyID() {
		return myID;
	}
	public void setMyID(Long myID) {
		this.myID = myID;
	}
	
	

}
