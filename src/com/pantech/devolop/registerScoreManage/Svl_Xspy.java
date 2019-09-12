package com.pantech.devolop.registerScoreManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_Xspy extends HttpServlet {

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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page")); // 获得页面page参数
		int pageSize = MyTools.parseInt(request.getParameter("rows")); // 获得页面rows参数
		Vector jsonV = null;// 返回结果集
		JSONArray jal = null;// 返回json对象
		XspyBean bean = new XspyBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
								
				//查询考试列表
				//jsonV = bean.queryRec(pageNum, pageSize);
			//	jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				//查询系下拉框
				jsonV = bean.loadXMCCombo();
				jal = JsonUtil.addJsonParams(jal, "xData", ((JSONArray)jsonV.get(2)).toString());
								
				//查询专业下拉框
				jsonV = bean.loadZYMCCombo();
				jal = JsonUtil.addJsonParams(jal, "zyData", ((JSONArray)jsonV.get(2)).toString());
						
				//查询行政班下拉框
				jsonV = bean.loadXZBCombo();
				jal = JsonUtil.addJsonParams(jal, "xzbData", ((JSONArray)jsonV.get(2)).toString());
						
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		// 查询学生信息
		if (active.equalsIgnoreCase("queryRec")) {
			//bean.setPY_XJH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PY_XJH")),"UTF-8"));
			//bean.setPY_XM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PY_XM")),"UTF-8"));
							
			try {
				//查询教师列表
				jsonV = bean.queryRec(pageNum, pageSize);
				if(jsonV!=null&&jsonV.size()>0){
					jal=(JSONArray) jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				//response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//判断表里是否存在该学生
		if("CheckStuRec".equalsIgnoreCase(active)){
			bean.setPY_XJH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PY_XJH")),"UTF-8"));
			
			try {
				bean.CheckStuRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			} catch (WrongSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学生评语
		if("selectStuComment".equalsIgnoreCase(active)){
			try {
										
				//查询行政班下拉框
				jsonV = bean.selectStuComment();
				jal = JsonUtil.addJsonParams(jal, "stuData", ((JSONArray)jsonV.get(2)).toString());
								
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WrongSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//更新学生评语
		if("updateStuComment".equalsIgnoreCase(active)){
			bean.setPY_XJH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PY_XJH")),"UTF-8"));
					
			try {
				bean.updateStuComment();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			} catch (WrongSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询考试班级信息
		if("loadStuTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			try {
				jsonV = bean.queClassStuTree(parentCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 从界面获取参数
	 * 
	 * @date
	 * @author:
	 * @param request
	 * @param KsszBean
	 */
	private void getFormData(HttpServletRequest request, XspyBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		bean.setAUTHCODE(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		
		
		bean.setPY_XM(MyTools.StrFiltr(request.getParameter("PY_XM")));// 姓名
		bean.setPY_NJ(MyTools.StrFiltr(request.getParameter("PY_NJ")));// 年级
		bean.setPY_PY(MyTools.StrFiltr(request.getParameter("PY_PY")));// 评语
		bean.setPY_CJR(MyTools.StrFiltr(request.getParameter("PY_CJR")));// 创建人
		bean.setPY_CJSJ(MyTools.StrFiltr(request.getParameter("PY_CJSJ")));// 创建时间
		bean.setPY_ZT(MyTools.StrFiltr(request.getParameter("PY_ZT")));// 状态
		
		
		bean.setPY_XZBDM(MyTools.StrFiltr(request.getParameter("PY_XZBDM")));// 行政班代码
		bean.setPY_XBDM(MyTools.StrFiltr(request.getParameter("PY_XBDM")));// 系部代码
		bean.setPY_ZYDM(MyTools.StrFiltr(request.getParameter("PY_ZYDM")));// 专业代码
		
		bean.setPY_XJH(MyTools.StrFiltr(request.getParameter("PY_XJH")));// 学籍号
		
		bean.setPY_DD(MyTools.StrFiltr(request.getParameter("PY_DD")));// 等第
		
	}
}