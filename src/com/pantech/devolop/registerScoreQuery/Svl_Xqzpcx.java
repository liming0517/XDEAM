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

public class Svl_Xqzpcx extends HttpServlet {

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
		XqzpcxBean bean = new XqzpcxBean(request);
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
				
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询文字代码
//				jsonV = bean.loadWzcjShowCombo();
//				jal = JsonUtil.addJsonParams(jal, "wzcjShowData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询批量导出班级下拉框
		if("loadExportBjCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadExportBjCombo();
				jal = (JSONArray) jsonV.get(2);
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
		
		//获取化学生成绩信息
		if("loadStuScoreInfo".equalsIgnoreCase(active)){
			try {
				//查询学生成绩信息
				jsonV = bean.loadStuScoreInfo();
				jal = JsonUtil.addJsonParams(jal, "subData", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "scoreData", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//导出单个班级学生成绩信息
		if("exportSingle".equalsIgnoreCase(active)){
			bean.setXNXQMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XNXQMC")), "UTF-8"));
			bean.setBJMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("BJMC")), "UTF-8"));
			
			try {
				String filePath = bean.exportSingle();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//导出多个班级学生成绩信息
		if("exportScoreInfo".equalsIgnoreCase(active)){
			bean.setXNXQMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XNXQMC")), "UTF-8"));
			
			try {
				String filePath = bean.exportScoreInfo();
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
	private void getFormData(HttpServletRequest request, XqzpcxBean bean){
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
	}
}