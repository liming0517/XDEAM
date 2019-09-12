package com.pantech.devolop.customExamManage;

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

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_Cjtj extends HttpServlet {

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
		CjtjBean bean = new CjtjBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//获取当前学年学期
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
						
				//查询考试列表
				jsonV = bean.queryRec(pageNum, pageSize, curXnxq, "" , "" , "");
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				//查询学年学期下拉框
				jsonV = bean.loadXnXqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
						
				//查询考试类别下拉框
				jsonV = bean.loadLbCombo();
				jal = JsonUtil.addJsonParams(jal, "kslbData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学科下拉框
				jsonV = bean.loadKCMCCombo();
				jal = JsonUtil.addJsonParams(jal, "kcData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 查询考试
		if (active.equalsIgnoreCase("queryRec")) {
			String XNXQ_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_XNXQBM")),"UTF-8");//学年学期
			String KSMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_KSMC")), "UTF-8");//考试名称
			String KSLB_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_KSLB")),"UTF-8");//类别
			String KCDM_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_KCDM")),"UTF-8");//课程
							
			try {
				//查询教师列表
				jsonV = bean.queryRec(pageNum, pageSize, XNXQ_CX, KSMC_CX, KSLB_CX, KCDM_CX);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 查询考试
		if (active.equalsIgnoreCase("sourceStatisticsRec")) {
			String CJ_KSBH = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_KSBH")),"UTF-8");//考试编号
			String CJ_KCDM = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_KCDM")), "UTF-8");//课程代码
									
			try {
				//查询教师列表
				jsonV = bean.sourceStatisticsRec(pageNum, pageSize, CJ_KSBH, CJ_KCDM);
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				//response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//学生考试成绩信息导出
		if("ExportExcel".equalsIgnoreCase(active)){
			String kcdm = MyTools.StrFiltr(request.getParameter("DC_KCDM"));
			String kcmc = MyTools.StrFiltr(request.getParameter("DC_KCMC"));
			String xnxqbm = MyTools.StrFiltr(request.getParameter("DC_XNXQBM"));
			String ksxkbh = MyTools.StrFiltr(request.getParameter("DC_KSXKBH"));
			String ksbh = MyTools.StrFiltr(request.getParameter("DC_KSBH"));
			String ksmc = MyTools.StrFiltr(request.getParameter("DC_KSMC"));
			
			try {
				String filePath = bean.ExportExcelStuScore(kcdm,kcmc,xnxqbm,ksxkbh,ksbh,ksmc);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
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
	private void getFormData(HttpServletRequest request, CjtjBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		bean.setAUTHCODE(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCJ_KSBH(MyTools.StrFiltr(request.getParameter("CJ_KSBH")));// 考试编号
		bean.setCJ_KSXKBH(MyTools.StrFiltr(request.getParameter("CJ_KSXKBH")));// 考试学科编号
		bean.setCJ_KCDM(MyTools.StrFiltr(request.getParameter("CJ_KCDM")));// 课程代码
		bean.setCJ_BJDM(MyTools.StrFiltr(request.getParameter("CJ_BJDM")));// 班级代码
		bean.setCJ_CJR(MyTools.StrFiltr(request.getParameter("CJ_CJR")));// 创建人
		bean.setCJ_CJSJ(MyTools.StrFiltr(request.getParameter("CJ_CJSJ")));// 创建时间
		bean.setCJ_ZT(MyTools.StrFiltr(request.getParameter("CJ_ZT")));// 状态
		bean.setCJ_XNXQBM(MyTools.StrFiltr(request.getParameter("CJ_XNXQBM")));//  学年学期编码
		
		bean.setCJ_KSMC(MyTools.StrFiltr(request.getParameter("CJ_KSMC")));// 考试名称
	}
}