package com.pantech.base.common.audit;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.PublicTools;
import com.pantech.base.common.tools.TraceLog;

/**
 * Servlet implementation class Svl_Auditedit
 */
public class Svl_Auditedit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Svl_Auditedit() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		TraceLog.Trace("in the Servlet!");
		request.setCharacterEncoding("UTF-8"); //设置字符集
		response.setContentType("text/html; charset=UTF-8");
		String msg = new String();  //返回消息
		String active = MyTools.StrFiltr(request.getParameter("coactive")); //操作类型
		Vector jsonV = null; //返回结果集
		JSONArray jal = null; //返回的json对象
		TraceLog.Trace("coactive...:"+active);
		TraceLog.Trace("IDname...:"+request.getParameter("IDname"));
		AuditeditBean  bean = new AuditeditBean(request);  //对象
		this.getFormDate(request,bean);
		if("add".equalsIgnoreCase(active)) {
			try {
				bean.add();
				jal = JsonUtil.addJsonParams(jal,"EDTION",bean.getEDITIONS());
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}			
		}
		if("edit".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				bean.edit();
				jal = JsonUtil.addJsonParams(jal,"EDTION",bean.getEDITIONS());
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
		
		if("spadd".equalsIgnoreCase(active)) {
			try {
				bean.spadd();
				jal = JsonUtil.addJsonParams(jal,"EDTION",bean.getEDITIONS());
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}			
		}
		if("spedit".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				bean.spedit();
				jal = JsonUtil.addJsonParams(jal,"EDTION",bean.getEDITIONS());
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
		
		
		if("tgadd".equalsIgnoreCase(active)) {
			try {
				bean.tgadd();
				jal = JsonUtil.addJsonParams(jal,"EDTION",bean.getEDITIONS());
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}			
		}
		if("tgedit".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				bean.tgedit();
				jal = JsonUtil.addJsonParams(jal,"EDTION",bean.getEDITIONS());
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
		
		if("savedefmod".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				PublicTools publicTools =new PublicTools();
				//如果dataList中的值不为空则进行取值操作
				bean.savedefmod();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
		
		if("editdefmod".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				PublicTools publicTools =new PublicTools();
				//如果dataList中的值不为空则进行取值操作
				bean.editdefmod();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存成功");
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
	}
	
	public void getFormDate(HttpServletRequest request,AuditeditBean  bean) {
		bean.setMODEID(MyTools.StrFiltr(request.getParameter("comodeid")).replaceAll("_", ".")); //模块编号
		bean.setAUDITID(MyTools.StrFiltr(request.getParameter("auditid")));
		bean.setEDITIONS(MyTools.StrFiltr(request.getParameter("coedtion")));
		bean.setAUDITNAME(MyTools.StrFiltr(request.getParameter("showname")));
		bean.setID(MyTools.StrFiltr(request.getParameter("auditname")));
		bean.setIDTYPE(MyTools.StrFiltr(request.getParameter("audittype")));
		bean.setCONDITION(MyTools.StrFiltr(request.getParameter("linknum")));
		if(bean.getIDTYPE().equals("ry")){
			bean.setEMPLOYEENAME(MyTools.StrFiltr(request.getParameter("IDname")));
		}
		if(bean.getIDTYPE().equals("zd")){
			bean.setFIELDNAME(MyTools.StrFiltr(request.getParameter("IDname")));
		}
		if(bean.getIDTYPE().equals("js")){
			bean.setROLENAME(MyTools.StrFiltr(request.getParameter("IDname")));
		}
		

		bean.setAUDITIDS(MyTools.StrFiltr(request.getParameter("spauditid"))); //审核编号
		bean.setACTIVENUM(MyTools.StrFiltr(request.getParameter("actnum")));
		bean.setSPID(MyTools.StrFiltr(request.getParameter("spid"))); //特殊编号
		bean.setSPFIELD(MyTools.StrFiltr(request.getParameter("spfield"))); //特殊字段
		bean.setTERMID(MyTools.StrFiltr(request.getParameter("termid"))); //条件编号
		bean.setTERM(MyTools.StrFiltr(request.getParameter("termname"))); //条件
		bean.setVALUES(MyTools.StrFiltr(request.getParameter("number"))); //数值
		
		bean.setTGID(MyTools.StrFiltr(request.getParameter("tgid"))); //通过编号
		bean.setTGNAMEID(MyTools.StrFiltr(request.getParameter("userid"))); //通过人员名称
		bean.setTGNAME(MyTools.StrFiltr(request.getParameter("username"))); //通过人员名称
		
		bean.setModule(MyTools.StrFiltr(request.getParameter("modulename")).replaceAll("_", "."));
		bean.setVIEW(MyTools.StrFiltr(request.getParameter("moduleview")));
		bean.setVIEWIKEY(MyTools.StrFiltr(request.getParameter("moduleviewikey")));
		bean.setTYPE(MyTools.StrFiltr(request.getParameter("type")));
	}
	
}
