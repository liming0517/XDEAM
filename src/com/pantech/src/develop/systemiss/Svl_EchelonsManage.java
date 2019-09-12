package com.pantech.src.develop.systemiss;
/**
编制日期：2015.07.21
创建人：shenlei
模块名称：S1.13 层级管理
说明:
	 
功能索引:
	1-新建
	2-编辑
	3-查询
**/
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 



import net.sf.json.JSONArray;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;



public class Svl_EchelonsManage extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1311399487924845743L;
	/**
	 * 
	 */

	public Svl_EchelonsManage() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");//设置字符编码为UTF-8
		response.setContentType("text/html;charset=UTF-8");//设置字符编码为UTF-8
		String active = MyTools.StrFiltr(request.getParameter("active"));//接受active
		
		EchlonsManage ech = new EchlonsManage(request);//初始化bean
		
		this.getFormDate(ech, request); // 获取SUBMIT提交时的参数（AJAX适用）

		Vector vector=null;
		Vector jsonV = null;
		JSONArray jal=null;
		
		System.out.println(123+".........");
		System.out.println(request.getParameter("active")+"..");
		//新建层级关系
		if("add".equalsIgnoreCase(active)){
			try {
				ech.saveRec();
				jal=JsonUtil.addJsonParams(jal, "MSG", ech.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//加载combox
		if("combox".equalsIgnoreCase(active)){
			try {
				jal=ech.querycombox();
				System.out.println(jal);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//编辑层级表
		if("queryupdate".equalsIgnoreCase(active)){
			try {
				ech.queryupdate();
				jal=JsonUtil.addJsonParams(jal, "MSG", ech.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//加载权限列表
		if("load".equalsIgnoreCase(active)){
			try {
				jsonV=ech.queryload();
				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//加载权限列表1
		if("load1".equalsIgnoreCase(active)){
			try {
				jsonV=ech.queryload1();
				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//保存层级权限关系
		if("saveAuth".equalsIgnoreCase(active)){
			System.out.println("encode"+ech.getEcheCode());
			try {
				ech.saveAuth();
				jal=JsonUtil.addJsonParams(jal, "MSG", ech.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//删除层级关系
		if("delAuth".equalsIgnoreCase(active)){
			System.out.println("encode"+ech.getEcheCode());
			try {
				ech.del();
				jal=JsonUtil.addJsonParams(jal, "MSG", ech.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	//加载层级权限关系表	
		if("loadlist".equalsIgnoreCase(active)){
			try {
				jsonV=ech.loadlist();
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	//删除层级权限关系	
		if("querydel".equalsIgnoreCase(active)){
			try {
				ech.del();
				jal=JsonUtil.addJsonParams(jal, "MSG", ech.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//查询层级权限关系
		if("que".equalsIgnoreCase(active)){
			try {
				jsonV=ech.check();
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
				System.out.println(jal);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
		
	
	}
	private void getFormDate(EchlonsManage ech, HttpServletRequest request) {
		ech.setEcheName(MyTools.StrFiltr(request.getParameter("EcheName")));//层级编号
		ech.setEcheCode(MyTools.StrFiltr(request.getParameter("EcheCode")));//层级名称
		ech.setState(MyTools.StrFiltr(request.getParameter("state")));//状态
		ech.setAuthCode(MyTools.StrFiltr(request.getParameter("AuthCode")));
		
	}

}
