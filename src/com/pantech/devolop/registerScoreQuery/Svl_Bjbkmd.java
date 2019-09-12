package com.pantech.devolop.registerScoreQuery;

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

public class Svl_Bjbkmd extends HttpServlet {

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
		BjbkmdBean bean = new BjbkmdBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
//				jsonV = bean.queryRec(pageNum, pageSize);
//				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询年级下拉框
				jsonV = bean.loadNjdmCombo();
				jal = JsonUtil.addJsonParams(jal, "njdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询导出年级下拉框
				jsonV = bean.loadExportNjCombo();
				jal = JsonUtil.addJsonParams(jal, "exportNjData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadXbdmCombo();
				jal = JsonUtil.addJsonParams(jal, "xbdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadExportXbCombo();
				jal = JsonUtil.addJsonParams(jal, "exportXbData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询专业下拉框
				jsonV = bean.loadMajorCombo();
				jal = JsonUtil.addJsonParams(jal, "sszyData", ((JSONArray)jsonV.get(2)).toString());
				//查询专业专业下拉框
				jsonV = bean.loadExportZyCombo();
				jal = JsonUtil.addJsonParams(jal, "exportZyData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学年下拉框
				jsonV = bean.loadXnCombo();
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//导出
		if("loadExportBjCombo".equalsIgnoreCase(active)){
			String nj = MyTools.StrFiltr(request.getParameter("nj"));
			String zy = MyTools.StrFiltr(request.getParameter("zy"));
			String xb = MyTools.StrFiltr(request.getParameter("xb"));
			String xn = MyTools.StrFiltr(request.getParameter("xn"));
			try {
			jsonV = bean.loadExportBjCombo(nj, zy,xb,xn);
			jal = JsonUtil.addJsonParams(jal, "bjData", ((JSONArray)jsonV.get(2)).toString());
			response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			//查询学科
		if("loadKcCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadKcCombo();
				jal = JsonUtil.addJsonParams(jal, "kcData", ((JSONArray)jsonV.get(2)).toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询班级列表
		if("query".equalsIgnoreCase(active)){
			bean.setBJBH(URLDecoder.decode(bean.getBJBH(), "UTF-8"));
			bean.setBJMC(URLDecoder.decode(bean.getBJMC(), "UTF-8"));
			
			try {
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//获取初始化学生成绩信息
		if("initStuScoreInfo".equalsIgnoreCase(active)){
			String XH = MyTools.StrFiltr(request.getParameter("XH"));
			String KCMC = MyTools.StrFiltr(request.getParameter("KCMC"));
			try {
				//查询学生成绩信息
				jsonV = bean.loadStuScoreInfo(XH,KCMC);
				jal = JsonUtil.addJsonParams(jal, "colData", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "llData", MyTools.StrFiltr(jsonV.get(1)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		//导出多个班级学生成绩信息
		if("exportScoreInfo".equalsIgnoreCase(active)){
			
			try {
				String filePath = bean.exportScoreInfo();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//导出单个班级学生成绩信息
		if("exportSingle".equalsIgnoreCase(active)){
			try {
					String filePath = bean.exportSingle();
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
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, BjbkmdBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM"))); //学年学期编码
		bean.setXNXQMC(MyTools.StrFiltr(request.getParameter("XNXQMC"))); //学年学期名称
		bean.setBJBH(MyTools.StrFiltr(request.getParameter("BJBH"))); //行政班代码
		bean.setBJMC(MyTools.StrFiltr(request.getParameter("BJMC"))); //行政班名称
		bean.setXBDM(MyTools.StrFiltr(request.getParameter("XBDM"))); //系部代码
		bean.setNJDM(MyTools.StrFiltr(request.getParameter("NJDM"))); //年级代码
		bean.setSSZY(MyTools.StrFiltr(request.getParameter("SSZY"))); //专业代码
		bean.setEXAMTYPE(MyTools.StrFiltr(request.getParameter("EXAMTYPE"))); //考试类型
		bean.setXN(MyTools.StrFiltr(request.getParameter("XN"))); //学年
	}
}