package com.tjpcms.common.Crud;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.ndktools.javamd5.Mademd5;
import com.tjpcms.common.CL;
import com.tjpcms.common.Hanshu;
import com.tjpcms.spring.mapper.EntMapper;

public class Crud {
	
	Aev aev = new Aev();//add,edit,view

	Retrieve r = new Retrieve();
	Delete d = new Delete();

	String tphtml;//提示文字html
	List<String> lstOp;//操作有哪些

	EntMapper mp;
	HttpServletRequest rq;
	String tb;
	String bread;
	
	List<Map<String,Object>>lsttbZd;//把表字段查出来存起来，再用的话就不用再查了
	String u;//uuid
	String[] cid;//channel id



	public String[] getCid() {
		return cid;
	}
	public void setCid(String[] cid) {
		this.cid = cid;
	}
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public List<Map<String, Object>> getLsttbZd() {
		return lsttbZd;
	}
	public void setLsttbZd(List<Map<String, Object>> lsttbZd) {
		this.lsttbZd = lsttbZd;
	}
	public Aev getAev() {
		return aev;
	}
	public void setAev(Aev aev) {
		this.aev = aev;
	}
	
	//获得调用函数的名称
	private String getDyfnName() {  
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();  
        StackTraceElement e = stacktrace[3];  
        String methodName = e.getMethodName();  
        return methodName;  
    }
	public Crud(EntMapper mp,HttpServletRequest rq, String tb, String bread) throws UnsupportedEncodingException {
		this.mp = mp;
		this.rq = rq;
		this.tb = tb;

		String ___lmmc = rq.getParameter("___lmmc");
		if (StringUtils.isNotEmpty(___lmmc)){
			___lmmc = URLDecoder.decode(___lmmc,"UTF-8");
		}
		boolean isfulmzi = "fulanmu_zi".equals(rq.getParameter("___lxpy"));
		if (isfulmzi){
			this.bread = "当前位置："+___lmmc;
		}else if (StringUtils.isNotEmpty(bread)){
			if (bread.contains("<a")){
				this.bread = bread;
			}else{
				this.bread = "当前位置："+bread;
				if (StringUtils.isNotEmpty(___lmmc) &&!bread.contains(___lmmc))this.bread += " - "+___lmmc;
			}
		}
		if (StringUtils.isEmpty(this.bread)){
			this.bread = "当前位置："+___lmmc;
		}

		Mademd5 mad = new Mademd5();
		u=mad.toMd5(getDyfnName()+mad.toMd5(tb)+mp);
		HttpSession ses = rq.getSession();
		ses.setMaxInactiveInterval(CL.SESTO_CRUD);
/*		Crud x = (Crud)ses.getAttribute(this.u);
		if (x==null){//防止重复点击某个栏目，不断生成新的session的情况，此处是以固定的名称来记录栏目session的
			ses.setAttribute(u, this);
		}*/
		cid = rq.getParameterValues("___cid");
		String exw = "";
		if (cid!=null && cid.length>0 && StringUtils.isNotEmpty(cid[0])) {//说明是点击了栏目内容
			if (isfulmzi){
				d.setNeed(false);
				aev.setNeede(false);
				aev.setNeeda(false);
				exw +="tt.id in"+Hanshu.arr2instr(cid);
			}else{//是栏目内容中的普通栏目或者是父栏目nr
				exw +="tt.cid in"+Hanshu.arr2instr(cid);
				if (Hanshu.zdInDbcol("delf", Hanshu.getListZiduan(this))){
					exw +=" and delf=0";
					d.setBatdel(true).setZdysql("  update "+tb+" set delf=1");
				}
			}
		}
		if (StringUtils.isNotEmpty(exw)) r.setExwhere(exw);
		ses.setAttribute(u, this);
		ses.setAttribute("o", this);
	}
	public Crud(EntMapper mp,HttpServletRequest rq, String tb) throws UnsupportedEncodingException {
		this(mp,rq,tb,"");
	}

	public Retrieve getR() {
		return r;
	}
	public void setR(Retrieve r) {
		this.r = r;
	}
	public Delete getD() {
		return d;
	}
	public void setD(Delete d) {
		this.d = d;
	}
	public String getTphtml() {
		return tphtml;
	}
	public void setTphtml(String tphtml) {
		this.tphtml = tphtml;
	}
	public List<String> getLstOp() {
		return lstOp;
	}
	public void setLstOp(List<String> lstOp) {
		this.lstOp = lstOp;
	}
	public EntMapper getMp() {
		return mp;
	}
	public void setMp(EntMapper mp) {
		this.mp = mp;
	}
	public HttpServletRequest getRq() {
		return rq;
	}
	public void setRq(HttpServletRequest rq) {
		this.rq = rq;
	}
	public String getTb() {
		return tb;
	}
	public void setTb(String tb) {
		this.tb = tb;
	}
	public String getBread() {
		return bread;
	}
	public void setBread(String bread) {
		this.bread = bread;
	}
}
