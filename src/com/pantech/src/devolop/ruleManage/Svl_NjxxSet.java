package com.pantech.src.devolop.ruleManage;

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

public class Svl_NjxxSet extends HttpServlet {

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
		NjxxSetBean bean = new NjxxSetBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）

		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize, "", "");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询年级列表
		if("query".equalsIgnoreCase(active)){
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String NJMC_CX = MyTools.StrFiltr(request.getParameter("NJMC_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize, NJDM_CX, NJMC_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//新建
		if("new".equalsIgnoreCase(active)){
			try {
				bean.addRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//编辑
		if("edit".equalsIgnoreCase(active)){
			try {
				bean.modRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//删除
		if("del".equalsIgnoreCase(active)){
			try {
				bean.delRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, NjxxSetBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setNJDM(MyTools.StrFiltr(request.getParameter("NJDM"))); //年级代码
		bean.setNJMC(MyTools.StrFiltr(request.getParameter("NJMC"))); //年级名称
		bean.setSSNF(MyTools.StrFiltr(request.getParameter("SSNF"))); //所属年份
	}
}