package com.pantech.src.develop.systemiss;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class Svl_teamleader extends HttpServlet {

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");//设置字符编码为UTF-8
		response.setContentType("text/html;charset=UTF-8");//设置字符编码为UTF-8
		String active = MyTools.StrFiltr(request.getParameter("active"));//接受active
		Teamleaderbean team= new Teamleaderbean(request);
		this.getFormDate(team, request);
		Vector vector=null;
		Vector jsonV = null;
		JSONArray jal=null;
		
		if("comboxxqmc".equalsIgnoreCase(active)){
			try {
				jsonV = team.querycomboxxqmc();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("comboxjsmc".equalsIgnoreCase(active)){
			try {
				jsonV = team.querycomboxjsmc();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("comboxzy".equalsIgnoreCase(active)){
			try {
				jsonV = team.querycomboxzy();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("savezuzhang".equalsIgnoreCase(active)){
			String JSMC=MyTools.StrFiltr(request.getParameter("JSMC"));
				try {
					team.savezuzhang(JSMC);
					jal=JsonUtil.addJsonParams(jal, "MSG",team.getMSG());
					response.getWriter().write(jal.toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if("comboxxqmcd".equalsIgnoreCase(active)){
			try {
				jsonV = team.querycomboxjsmc();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("comboxjsmcd".equalsIgnoreCase(active)){
			try {
				jsonV = team.querycomboxxqmc();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("querylist".equalsIgnoreCase(active)){
			
			try {
				jal = team.querylist();
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("updatezz".equalsIgnoreCase(active)){
			String JSMC=MyTools.StrFiltr(request.getParameter("JSMC"));
			try {
				team.queryupdate(JSMC);
				jal=JsonUtil.addJsonParams(jal, "MSG",team.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("query".equalsIgnoreCase(active)){
			String queryjs=URLDecoder.decode(MyTools.StrFiltr(request.getParameter("queryjs")),"UTF-8");
			try {
				jsonV=team.query(queryjs);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("del".equalsIgnoreCase(active)){
			try {
				team.del();
				jal=JsonUtil.addJsonParams(jal, "MSG",team.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				jal=JsonUtil.addJsonParams(jal, "MSG","删除失败");
				response.getWriter().write(jal.toString());
				e.printStackTrace();
			}
		}
		
		if("importTeacher".equalsIgnoreCase(active)){
			String teaName = MyTools.StrFiltr(request.getParameter("teaName"));
			try {
				team.importTeacher(teaName);
				jal=JsonUtil.addJsonParams(jal, "MSG",team.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				jal=JsonUtil.addJsonParams(jal, "MSG","导入失败");
				response.getWriter().write(jal.toString());
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	private void getFormDate(Teamleaderbean team, HttpServletRequest request) {
		team.setXQDM(MyTools.StrFiltr(request.getParameter("XQDM")));//校区代码
		team.setZYDM(MyTools.StrFiltr(request.getParameter("ZYDM")));//专业代码
		team.setJSDM(MyTools.StrFiltr(request.getParameter("JSDM")));//教师代码
		team.setXq1(MyTools.StrFiltr(request.getParameter("xq1")));//原校区代码
		team.setTyzy(MyTools.StrFiltr(request.getParameter("tyzy")));//原专业代码
		team.setTyzz1(MyTools.StrFiltr(request.getParameter("tyzz1")));//原教师代码
	}

}
