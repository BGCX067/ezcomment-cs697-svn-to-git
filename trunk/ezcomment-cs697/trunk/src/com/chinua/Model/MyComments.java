package com.chinua.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.chinua.Entity.*;
import com.chinua.util.*;
import com.chinua.Model.MyCBox;

import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@ManagedBean()
@RequestScoped
public class MyComments  implements java.io.Serializable{
	List<Comment> comments=new ArrayList<Comment>();
	Long cbxid=0l;
	Long commentid=0l;
	String owner;
	String text;
	String style;
	String state;
	Alert alert =new Alert();
	MyCBox mcb=new MyCBox();
	Template t=new Template();
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public MyComments(){
		
		System.out.println("==myComments=="+this.toString());
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		
		if(id==null){
			
			System.out.println("=====NO ID===== but ");
		}
		else{
			System.out.println("::===ID=="+id);
			if(id.equalsIgnoreCase("NewComments")){
				//this.getNewComments();
			}else{
				MyValues myValues=new MyValues(id);
				this.setOwner(id);
				this.getMyComments();
			}
		}
		
		
	}
	private void fetchOwner(){
		
		try {
			MyValues myValues=(MyValues)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("myValues");
			this.owner=myValues.getOwner();
			System.out.println("=====NO ID===== frm Session "+this.owner);
		} catch (Exception e) {
			System.out.println("=====CAN NOT PICK myValue in Session===== ");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//String own="";
		//Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		 //own= params.get("id");
		 //this.owner=own;
		//return own;
	}
	public String prepare(){
		if(owner==null) owner="";
		if(owner.length()==0){
			this.fetchOwner();
		}
		System.out.println("==owner=="+owner);
		Long din=mcb.long2String(owner);
		try{
			t=mcb.getTemplate(mcb.getMaxCBXId(din));
			
			}catch(Exception e){
				/*
				try{
				FacesContext.getCurrentInstance().getExternalContext().redirect("/pages/404.faces");
				System.out.println("Error redirecting::"+e.getMessage());
				}catch(Exception e1){System.out.println("Could NOT redirecting::"+e.getMessage());}
				*/
				}
			
		if(t==null){
			try{
			FacesContext.getCurrentInstance().getExternalContext().redirect("/pages/404.faces");
			}catch(Exception e){System.out.println("Error redirecting::"+e.getMessage());}
			return "404.xhtml";
		}else{
			this.style=" background:#"+t.getBgcolor()+"; color:#"+t.getFontcolor()+"; ";
		}
		return "";
	}
	public String fetch() throws Exception{
		FacesContext fc = FacesContext.getCurrentInstance()	  ;  
		ELResolver elResolver = fc.getApplication().getELResolver();
		Comment cmt = (Comment)(elResolver.getValue(
	            fc.getELContext(), null, "comment"));
		String resp=this.addComment(cmt);
		if(resp.equalsIgnoreCase("Done!")){
			cmt.setAuthor("");
			cmt.setEmail("");
			cmt.setContent("");
		}
		System.out.println("==fetch=="+cmt.getAuthor()+" resp:"+resp);
		Map requestParams = fc.getExternalContext().getRequestParameterMap();
		this.owner=(String) requestParams.get("owner");
		System.out.println("1111 Owner::"+owner);
		this.getMyComments();
		return null;
	}
	public String getCBXComments(){
		if(owner.length()==0){
			this.fetchOwner();
		}
		this.owner=this.cbxid.toString();
		System.out.print("Acting Owner::"+owner);
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + Comment.class.getName() + " where parent=='"+owner+"' order by date desc ";
	    this.comments = (List<Comment>) pm.newQuery(query).execute();
	    int n=comments.size();
	    if(n==0) this.text="No Comments";
	    else this.text="";
	    System.out.print("Got getCBXComments::"+n);
		return "mycomments.xhtml";
	}
	public String getCBXCommentsfrmid(){
		//this.owner=this.cbxid.toString();
		this.owner=this.cbxid.toString();
		return getCBXComments();
	}
	public String getCBXComments(String own){
		//this.owner=this.cbxid.toString();
		this.owner=own;
		return getCBXComments();
	}
	public void getMyComments(){//add owner
		String str=this.prepare();
		PersistenceManager pm = PMF.get().getPersistenceManager();//
	    String query = "select from " + Comment.class.getName() + " where parent=='"+owner+"' order by date desc ";
	    this.comments = (List<Comment>) pm.newQuery(query).execute();
	    int n=comments.size();
	    if(n==0) this.text="No Comments";
	    else this.text="";
	    System.out.print("Got Comments::"+n);
	}
	public String addComment(Comment cmt){
		String str="Done";
		Date date = new Date();
		//Comment greeting = new Comment(user, content, date);
		Comment comment=new Comment(cmt.getAuthor(),cmt.getEmail(),cmt.getContent(),date,this.owner);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            pm.makePersistent(comment);
            //this.text="Comment Added";
            alert.Sucess("Comment Added", context);
            this.setState("saved");
        }
        catch(Exception e){
        	str="Error adding comment";
        	//this.text="Comment could NOT be Added";
        	alert.Error("Comment could NOT be Added", context);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
        
        return str;
	}
	public String dels()throws Exception{//chk for comments that will be ophraned
		Comment c2= new Comment();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		FacesContext context = FacesContext.getCurrentInstance();
        try {
        	c2=pm.getObjectById(Comment.class, this.commentid);
            pm.deletePersistent(c2);
            alert.Sucess("Comment deleted", context);
        }
        catch(Exception e){
        	alert.Error("Comment NOT be deleted", context);
        	e.printStackTrace();
        } finally {
            pm.close();
            
        }
        return getCBXComments();
	}
	public String getUser(String str){
		User u=(User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		System.out.println(str+"==myComment=== owner:"+u.getUsername());
		return u.getUsername();
	}
	@SuppressWarnings("deprecation")
	public void getNewComments(User me){
		//User me=this.getUserRole("getNewComments");
		System.out.println("==me=== :"+me.getUsername()+" role:"+me.getRole());
		String query="";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date td=me.getDateReg();
		//Date startDate1 = new GregorianCalendar(td.getYear(), td.getMonth(), 31, 14, 00).getTime();
		int yr=td.getYear();
		int mon=td.getMonth();
		int day=td.getDay();
		int hr=td.getHours();
		int min=td.getMinutes();
		int sec=td.getSeconds();
		Calendar calendar = Calendar.getInstance();
		  calendar.clear();
		  calendar.set(yr, mon, day,hr,min,sec);
		  Date date = calendar.getTime(); 
		
		PersistenceManager pm = PMF.get().getPersistenceManager();//
		if(me.getRole().equalsIgnoreCase("admin")){
			query = "select from " + Comment.class.getName() + " where this.date > DATETIME("+yr+","+mon+","+day+","+hr+","+min+","+sec+") order by date desc ";
			System.out.println("Query::"+query);
			Query q =pm.newQuery(query);
			//q.declareParameters("java.util.Date date"); 
			this.comments = (List<Comment>) q.execute();
			System.out.println("ADMIN::"+comments.size());
		}else{
			Query q = pm.newQuery("select id from " + CommentBox.class.getName());
		    List ids = (List) q.execute();
		    System.out.println("IDS::"+ids.toString());
		    Query q1= pm.newQuery("select from " + Comment.class.getName() + " where date > "+me.getDateReg()+" && :p.contains(parent) order by date desc ");
		    this.comments = (List<Comment>)q1.execute(ids);
		    System.out.println("USER::"+comments.size());
		}
		/*
	    if(me.getRole().equalsIgnoreCase("admin")) query = "select from " + Comment.class.getName() + " where date > "+me.getDateReg()+" order by date desc ";
	    else query = "select from " + Comment.class.getName() + " where date > "+me.getDateReg()+" &&  order by date desc ";////
	    this.comments = (List<Comment>) pm.newQuery(query).execute();
	    int n=comments.size();
	    */
	}
	public void updateLoginDate(String id){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User u2;
		Date date=new Date();
		try{
			u2=pm.getObjectById(User.class, id);
			u2.setDateReg(date);
			System.out.println("Updated USER loginDate::"+u2.getUsername()+" date:"+u2.getDateReg());
		}catch(Exception e){
			System.out.println("Update UserLogin Date Failed");
			e.printStackTrace();
		}finally {
            pm.close();
            
        }
	}
	public User getUserRole(String str){
		User u=(User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		if(u==null) return null;
		else return u;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public Long getCbxid() {
		return cbxid;
	}
	public void setCbxid(Long cbxid) {
		this.cbxid = cbxid;
	}
	public Long getCommentid() {
		return commentid;
	}
	public void setCommentid(Long commentid) {
		this.commentid = commentid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
