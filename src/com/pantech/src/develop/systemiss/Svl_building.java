package com.pantech.src.develop.systemiss;

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

public class Svl_building  extends HttpServlet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4948994841499826222L;
	public Svl_building() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");//设置字符编码为UTF-8
		response.setContentType("text/html;charset=UTF-8");//设置字符编码为UTF-8
		String active = MyTools.StrFiltr(request.getParameter("active"));//接受active
		String active1=MyTools.StrFiltr(request.getParameter("active1"));
		building bul=new building(request);
		this.getFormDate(bul, request);
		Vector vector=null;
		Vector jsonV = null;
		JSONArray jal=null;
		System.out.println("active+"+active+" xqdm"+request.getParameter("XQDM"));
		System.out.println(request.getParameter("JZWH"));
		System.out.println(request.getParameter("LXBH"));
		
		if("list1".equalsIgnoreCase(active)){
			try {
				jsonV=bul.loadlist1();
				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("list2".equalsIgnoreCase(active)){
			try {
				jsonV=bul.loadlist2();
				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if("list3".equalsIgnoreCase(active)){
			try {
				jsonV=bul.loadlist3();
				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("savexq".equalsIgnoreCase(active)){
			System.out.println("into");
			try {
				bul.savexq();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		if("combox".equalsIgnoreCase(active)){
//			try {
//				jal=bul.xqcombox();
//				System.out.println(jal);
//				response.getWriter().write(jal.toString());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		if("savejz".equalsIgnoreCase(active)){
			try {
				bul.savejz();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//加载下拉菜单
		if("comboxjzwmc".equalsIgnoreCase(active)){
			try {
				jal=bul.jzcombox();
				System.out.println(jal);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//加载下拉菜单教师类型
		if("comboxtype".equalsIgnoreCase(active)){
			try {
				jal=bul.lxcombox();
				System.out.println(jal);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("savejs".equalsIgnoreCase(active)){
			try {
				bul.savejs();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("updatejs".equalsIgnoreCase(active)){
//			System.out.println(request.getParameter("jsbh")+","+request.getParameter("jsmc")+","+request.getParameter("xqdm")+","+request.getParameter("lxbh")+","+request.getParameter("jzh")+","+request.getParameter("numberjs")+","+request.getParameter("namejs"));
			try {
				bul.queryupdatejs();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("updatejz".equalsIgnoreCase(active)){
//			System.out.println(request.getParameter("jzh")+","+request.getParameter("jzwmc")+","+request.getParameter("numberjz"));
			try {
				bul.queryupdatejz();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if("updatexq".equalsIgnoreCase(active)){
//			System.out.println(request.getParameter("xqdm")+","+request.getParameter("xqmc")+","+request.getParameter("xqdz")+","+request.getParameter("numberxq")+","+request.getParameter("namexq"));
			try {
				bul.queryupdatexq();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("comboxxqmc".equalsIgnoreCase(active)){
			try {
				jal=bul.querycomboxxqmc();
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("comboxjsmc".equalsIgnoreCase(active)){
				try {
					jal=bul.querycomboxjsmc();
					response.getWriter().write(jal.toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if("deljs".equalsIgnoreCase(active1)){
			try {
				bul.deljs();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("deljz".equalsIgnoreCase(active1)){
			try {
				System.out.println("into deljz");
				bul.deljz();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("delxq".equalsIgnoreCase(active1)){
			try {
				bul.delxq();
				jal=JsonUtil.addJsonParams(jal, "MSG", bul.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	private void getFormDate(building bul, HttpServletRequest request) {
		bul.setJSBH(MyTools.StrFiltr(request.getParameter("JSBH")));//教室编号
		bul.setJSMC(MyTools.StrFiltr(request.getParameter("JSMC")));//教室名称
		bul.setJZWH(MyTools.StrFiltr(request.getParameter("JZWH")));//建筑号
		bul.setJZWMC(MyTools.StrFiltr(request.getParameter("JZWMC")));//建筑物名称
		bul.setXQDM(MyTools.StrFiltr(request.getParameter("XQDM")));//校区代码
		bul.setXQDZ(MyTools.StrFiltr(request.getParameter("XQDZ")));//校区地址
		bul.setXQH(MyTools.StrFiltr(request.getParameter("XQH")));//校区号
		bul.setXQMC(MyTools.StrFiltr(request.getParameter("XQMC")));//校区名称	
		bul.setJSLX(MyTools.StrFiltr(request.getParameter("JSLX")));//教室类型
		bul.setLXBH(MyTools.StrFiltr(request.getParameter("LXBH")));//类型编号
		bul.setSFKY(MyTools.StrFiltr(request.getParameter("SFKY")));//是否可用
	}

}
