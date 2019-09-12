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

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;


public class Svl_Dfqk extends HttpServlet {
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
		DfqkBean bean = new DfqkBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询当前学年
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
				
				//查询学年学期下拉框
				jsonV = bean.loadXnXqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询考试列表
				jsonV = bean.queryRec(pageNum, pageSize, curXnxq, "" , "");
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
		// 查询考试
		if (active.equalsIgnoreCase("queryRec")) {
			System.out.println("queryrec");
			String XNXQ_CX = MyTools.StrFiltr(request.getParameter("XNXQ_CX"));//学年学期
			String KSMC_CX = MyTools.StrFiltr(request.getParameter("KSMC_CX"));//考试名称
			String DFQK_CX = MyTools.StrFiltr(request.getParameter("DFQK_CX"));//登分情况
									
			try {
				//查询教师列表
				jsonV = bean.queryRec(pageNum, pageSize, XNXQ_CX, KSMC_CX, DFQK_CX);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString()  + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//登分信息导出
		if("ExportExcelRegistrationMark".equalsIgnoreCase(active)){
			String XNXQ_CX = MyTools.StrFiltr(request.getParameter("XNXQ_CX"));//学年学期代码
			String KSMC_CX = MyTools.StrFiltr(request.getParameter("KSMC_CX"));//考试名称
			
			String DFQK_CX = MyTools.StrFiltr(request.getParameter("DFQK_CX"));//登分情况
			String DFQKMC = MyTools.StrFiltr(request.getParameter("DFQKMC"));//登分情况名称
					
			try {
				String filePath = bean.ExportExcelRegistrationMark(XNXQ_CX, KSMC_CX, DFQK_CX, DFQKMC);
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
	* @author:zhaixuchao
    * @param request
    * @param DfqkBean
    */
	private void getFormData(HttpServletRequest request, DfqkBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAuth(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		
		
		bean.setDD_XNXQBH(MyTools.StrFiltr(request.getParameter("DD_XNXQBH"))); //学年学期编号
		bean.setDD_XNXQMC(MyTools.StrFiltr(request.getParameter("DD_XNXQMC"))); //学年学期名称
		bean.setDD_KSBH(MyTools.StrFiltr(request.getParameter("DD_KSBH"))); //考试编号
		bean.setDD_KSMC(MyTools.StrFiltr(request.getParameter("DD_KSMC"))); //考试名称
		bean.setDD_DFQK(MyTools.StrFiltr(request.getParameter("DD_DFQK"))); //登分情况
	}
}