package com.pantech.src.devolop.questSurvey;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;

import net.sf.json.JSONArray;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_wjdc extends HttpServlet {

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
		WjdcBean bean = new WjdcBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//添加授课计划
//		if("addSkjh".equalsIgnoreCase(active)){
//			try {
//				bean.addSkjh();//添加普通课程
//			}catch(SQLException e){
//				e.printStackTrace();
//			}
//		}
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询查分时间
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
				
				//查询学年学期下拉框
				jsonV = bean.loadSemCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取问卷调查列表
		if("loadWjdcList".equalsIgnoreCase(active)){
			try {
				String result = bean.loadWjdcList();
				response.getWriter().write(result);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//读取问卷调查
		if("loadWjdc".equalsIgnoreCase(active)){
			try {
				String result = bean.loadWjdc();
				response.getWriter().write(result);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//保存问卷结果
		if("saveResult".equalsIgnoreCase(active)){
			String result = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("result")), "UTF-8");
			try {
				bean.saveResult(result);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//读取问卷调查统计结果
		if("loadStatistics".equalsIgnoreCase(active)){
			//String result = MyTools.StrFiltr(request.getParameter("result"));
			try {
				String result = bean.loadStatistics();
				response.getWriter().write(result);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//导出统计结果
		if("exportToExcel".equalsIgnoreCase(active)){
			String imageData = MyTools.StrFiltr(request.getParameter("imageData"));
			String savePath = MyTools.getProp(request, "Base.exportExcelPath") + "wjdcExport";
			
			try {
				String filePath = bean.queExportData(savePath, imageData);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "PATH", filePath);
			response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param KbcxBean
    */
	private void getFormData(HttpServletRequest request,WjdcBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAuth(MyTools.StrFiltr(request.getParameter("sAuth")));//用户权限
		bean.setWW_XNXQBM(MyTools.StrFiltr(request.getParameter("WW_XNXQBM"))); //学年学期编码
		bean.setWW_WJBH(MyTools.StrFiltr(request.getParameter("WW_WJBH"))); //问卷编号
		bean.setWW_WJLX(MyTools.StrFiltr(request.getParameter("WW_WJLX"))); //问卷类型
	}
}