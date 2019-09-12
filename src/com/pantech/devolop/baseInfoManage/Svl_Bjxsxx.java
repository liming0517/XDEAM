package com.pantech.devolop.baseInfoManage;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_Bjxsxx extends HttpServlet {

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
		BjxsxxBean bean = new BjxsxxBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询年级下拉框
				jsonV = bean.loadNjdmCombo();
				jal = JsonUtil.addJsonParams(jal, "njdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadXbdmCombo();
				jal = JsonUtil.addJsonParams(jal, "xbdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询专业下拉框
				jsonV = bean.loadMajorCombo();
				jal = JsonUtil.addJsonParams(jal, "sszyData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询班级下拉框
				jsonV = bean.loadBJLXCombo();
				jal = JsonUtil.addJsonParams(jal, "bjlxData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		//查询班级列表
		if("query".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学生列表
		if("loadStuList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadStuList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		//学生详细
		if("StuXq".equalsIgnoreCase(active)){
			String XH=MyTools.StrFiltr(request.getParameter("JX_XJH"));;
			try {
				jsonV = bean.StuXq(XH);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//学生信息设置导出
		if("ExportExcelStuScoreall".equalsIgnoreCase(active)){
			try {
				String lieInfo=MyTools.StrFiltr(request.getParameter("lieInfo"));
				String bjbh=MyTools.StrFiltr(request.getParameter("bjbh"));
				String bjdm=MyTools.StrFiltr(request.getParameter("bjdm"));
				String filePath = bean.ExportExcelStuScoreall(lieInfo,bjbh,bjdm);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
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
	private void getFormData(HttpServletRequest request, BjxsxxBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setBJBH(MyTools.StrFiltr(request.getParameter("BJBH"))); //行政班代码
		bean.setXJH(MyTools.StrFiltr(request.getParameter("XJH"))); //学生学号
		bean.setBJMC(MyTools.StrFiltr(request.getParameter("BJMC"))); //行政班名称
		bean.setXBDM(MyTools.StrFiltr(request.getParameter("XBDM"))); //系部代码
		bean.setNJDM(MyTools.StrFiltr(request.getParameter("NJDM"))); //年级代码
		bean.setSSZY(MyTools.StrFiltr(request.getParameter("SSZY"))); //专业代码
		bean.setZRS(MyTools.StrFiltr(request.getParameter("ZRS"))); //总人数
		bean.setBZR(MyTools.StrFiltr(request.getParameter("BZRGH"))); //班主任
		bean.setBJJC(MyTools.StrFiltr(request.getParameter("BJJC"))); //班级简称
		bean.setBJLX(MyTools.StrFiltr(request.getParameter("BJLX"))); //班级类型
		bean.setJSBH(MyTools.StrFiltr(request.getParameter("JSBH"))); //班级类型
		bean.setAuth(MyTools.StrFiltr(request.getParameter("sAuth")));//用户权限
	}
}