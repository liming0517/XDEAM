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

public class Svl_Xkkszlfx extends HttpServlet {
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
		XkkszlfxBean bean = new XkkszlfxBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//读取课程数据列表
		if("queTeaCourseList".equalsIgnoreCase(active)){
			bean.setCOURSENAME(URLDecoder.decode(bean.getCOURSENAME(), "UTF-8"));
			bean.setTEACODE(URLDecoder.decode(bean.getTEACODE(), "UTF-8"));
			bean.setTEANAME(URLDecoder.decode(bean.getTEANAME(), "UTF-8"));
			String enterType = MyTools.StrFiltr(request.getParameter("enterType"));
			
			try {
				jsonV = bean.queTeaCourseList(pageNum, pageSize, enterType);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取学年学期下拉框数据
		if("loadXnxqCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadXnxqCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//删除打印预览文件
		if("delViewFile".equalsIgnoreCase(active)){
			String filePath = MyTools.StrFiltr(request.getParameter("filePath"));
			
			try {
				boolean flag = bean.deleteFile(filePath);
				if(flag)
					bean.setMSG("删除成功");
				else
					bean.setMSG("删除失败");
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//导出文件
		if("exportReport".equalsIgnoreCase(active)){
			bean.setCOURSENAME(URLDecoder.decode(bean.getCOURSENAME(), "UTF-8"));
			bean.setTEANAME(URLDecoder.decode(bean.getTEANAME(), "UTF-8"));
			
			try {
				String filePath = bean.exportReport();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//保存质量分析表信息
		if("saveInfo".equalsIgnoreCase(active)){
			bean.setCOURSENAME(URLDecoder.decode(bean.getCOURSENAME(), "UTF-8"));
			String problem = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PROBLEM")), "UTF-8");
			String measure = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("MEASURE")), "UTF-8");
			
			try {
				bean.saveInfo(problem, measure);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
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
	private void getFormData(HttpServletRequest request, XkkszlfxBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM"))); //学年学期编码
		bean.setCOURSENAME(MyTools.StrFiltr(request.getParameter("COURSENAME"))); //课程名称
		bean.setEXAMTYPE(MyTools.StrFiltr(request.getParameter("EXAMTYPE"))); //考试类型
		bean.setTEACODE(MyTools.StrFiltr(request.getParameter("TEACODE"))); //教师工号
		bean.setTEANAME(MyTools.StrFiltr(request.getParameter("TEANAME"))); //教师姓名
	}
}