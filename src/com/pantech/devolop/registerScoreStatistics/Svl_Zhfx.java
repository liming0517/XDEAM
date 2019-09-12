package com.pantech.devolop.registerScoreStatistics;

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

public class Svl_Zhfx extends HttpServlet {

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
		ZhfxBean bean = new ZhfxBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询当前学年学期
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
				
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadExportXbdmCombo();
				jal = JsonUtil.addJsonParams(jal, "exportXbdmData", ((JSONArray)jsonV.get(2)).toString());
				//查询年级下拉框
				jsonV = bean.loadExportNjdmCombo();
				jal = JsonUtil.addJsonParams(jal, "exportNjdmData", ((JSONArray)jsonV.get(2)).toString());
				//查询专业下拉框
				jsonV = bean.loadExportZydmCombo();
				jal = JsonUtil.addJsonParams(jal, "exportZydmData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取学期班级数据列表
		if("queSemClassList".equalsIgnoreCase(active)){
			bean.setCLASSNAME(URLDecoder.decode(bean.getCLASSNAME(), "UTF-8"));
			
			try {
				jsonV = bean.queClassCourseList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取学年学期下拉框数据
//		if("loadXnxqCombo".equalsIgnoreCase(active)){
//			try {
//				jsonV = bean.loadXnxqCombo();
//				jal = (JSONArray) jsonV.get(2);
//				response.getWriter().write(jal.toString());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		//读取年级下拉框数据
//		if("loadNjdmCombo".equalsIgnoreCase(active)){
//			try {
//				jsonV = bean.loadNjdmCombo();
//				jal = (JSONArray) jsonV.get(2);
//				response.getWriter().write(jal.toString());
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		//查询统计信息
		if("loadStatisticsInfo".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.loadStatisticsInfo(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//导出统计信息
		if("exportStatisticsInfo".equalsIgnoreCase(active)){
			bean.setCLASSNAME(URLDecoder.decode(bean.getCLASSNAME(), "UTF-8"));
			String type = MyTools.StrFiltr(request.getParameter("TYPE"));
			
			try {
				String filePath = bean.exportStatisticsInfo(type);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
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
    * @param XkkszlfxBean
    */
	private void getFormData(HttpServletRequest request, ZhfxBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM"))); //学年学期编码
		bean.setCLASSCODE(MyTools.StrFiltr(request.getParameter("CLASSCODE"))); //班级代码
		bean.setCLASSNAME(MyTools.StrFiltr(request.getParameter("CLASSNAME"))); //班级名称
		bean.setCLASSTYPE(MyTools.StrFiltr(request.getParameter("CLASSTYPE"))); //班级类型
		bean.setXBDM(MyTools.StrFiltr(request.getParameter("XBDM"))); //系部代码
		bean.setNJDM(MyTools.StrFiltr(request.getParameter("NJDM"))); //年级代码
		bean.setZYDM(MyTools.StrFiltr(request.getParameter("ZYDM"))); //专业代码
		bean.setEXAMTYPE(MyTools.StrFiltr(request.getParameter("EXAMTYPE"))); //考试类型
	}
}