package com.pantech.devolop.scoreStatistics;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.pantech.src.devolop.queryStatistics.QueryStateBean;

public class Svl_ScoreQuery extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

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

		//设置字符编码为UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		
		int pageNum = MyTools.parseInt(request.getParameter("page"));	//获得页面page参数 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows"));	//获得页面rows参数 分页
		
		Vector jsonV = null;//返回结果集
		JSONArray jal = null;//返回json对象
		ScoreQuery bean = new ScoreQuery(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//查询班级学生
		if("loadStudent".equalsIgnoreCase(active)){
			try {
				//查询列表
				jsonV = bean.loadStudent(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询学生成绩
		if("loadScore".equalsIgnoreCase(active)){
			try {
				//查询列表
				jsonV = bean.loadScore(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		if("XZBDMCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.XZBDMCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("XNXQCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.XNXQCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("ZYDMCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.ZYDMCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
	}

	private void getFormData(HttpServletRequest request,ScoreQuery bean){
		bean.setiUSERCODE(MyTools.StrFiltr(request.getParameter("iUSERCODE"))); //行政班名称
		bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM"))); //学年学期编码
		bean.setXZBDM(MyTools.StrFiltr(request.getParameter("XZBDM"))); //行政班代码
		bean.setXZBMC(MyTools.StrFiltr(request.getParameter("XZBMC"))); //行政班名称
		bean.setsAuth(MyTools.StrFiltr(request.getParameter("sAuth"))); //权限
		bean.setZYDM(MyTools.StrFiltr(request.getParameter("ZYDM"))); //专业代码
		bean.setXH(MyTools.StrFiltr(request.getParameter("XH"))); //学号
		bean.setXM(MyTools.StrFiltr(request.getParameter("XM"))); //姓名
	}
	
}
