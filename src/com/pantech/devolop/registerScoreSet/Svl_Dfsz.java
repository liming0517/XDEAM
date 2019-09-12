package com.pantech.devolop.registerScoreSet;
/**
编制日期：2016.03.11
创建人：yeq
模块名称：M6.2 登分设置
说明:
	 
功能索引:
	1-查询
	2-设置
**/
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
import com.pantech.devolop.timetableQuery.KbcxBean;

public class Svl_Dfsz extends HttpServlet {

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
		DfszBean bean = new DfszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.queSemList(pageNum, pageSize, "", "");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学年学期列表
		if("queSemList".equalsIgnoreCase(active)){
			String XNXQMC_CX = MyTools.StrFiltr(request.getParameter("XNXQMC_CX"));
			String JXXZ_CX = MyTools.StrFiltr(request.getParameter("JXXZ_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queSemList(pageNum, pageSize, XNXQMC_CX, JXXZ_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存登分时间
		if("saveTime".equalsIgnoreCase(active)){
			try {
				bean.saveTime();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//保存补考登分时间
		if("saveBkTime".equalsIgnoreCase(active)){
			try {
				bean.saveBkTime();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//保存大补考登分时间
		if("saveDbkTime".equalsIgnoreCase(active)){
			try {
				bean.saveDbkTime();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询教师列表
		if("queTeaList".equalsIgnoreCase(active)){
			String DFJSBH_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("DFJSBH_CX")), "UTF-8");
			String DFJSMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("DFJSMC_CX")), "UTF-8");
			
			try {
				//查询学年学期列表
				jsonV = bean.loadTeaList(pageNum, pageSize, DFJSBH_CX, DFJSMC_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存教师登分权限
		if("saveTea".equalsIgnoreCase(active)){
			try {
				bean.saveTea();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//读取重名教师下拉框数据
		if("loadBeforeTeaCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadBeforeTeaCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取同名教师下拉框数据
		if("loadSameNameTeaCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadSameNameTeaCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//重置教师账号
		if("resetTea".equalsIgnoreCase(active)){
			String beforeTea = MyTools.StrFiltr(request.getParameter("beforeTea"));
			String afterTea = MyTools.StrFiltr(request.getParameter("afterTea"));
			
			try {
				bean.resetTea(beforeTea, afterTea);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
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
    * @param DfszBean
    */
	private void getFormData(HttpServletRequest request,DfszBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAuth(MyTools.StrFiltr(request.getParameter("sAuth")));//用户权限
		bean.setCD_XNXQBM(MyTools.StrFiltr(request.getParameter("CD_XNXQBM"))); //学年学期编码
		bean.setCD_KSSJ(MyTools.StrFiltr(request.getParameter("CD_KSSJ"))); //开始时间
		bean.setCD_JSSJ(MyTools.StrFiltr(request.getParameter("CD_JSSJ"))); //结束时间
		bean.setCJ_JSBH(MyTools.StrFiltr(request.getParameter("CJ_JSBH"))); //教师编号
		bean.setCD_CJR(MyTools.StrFiltr(request.getParameter("CD_CJR"))); //创建人
		bean.setCD_CJSJ(MyTools.StrFiltr(request.getParameter("CD_CJSJ"))); //创建时间
		bean.setCD_ZT(MyTools.StrFiltr(request.getParameter("CD_ZT"))); //状态
		bean.setCD_DBKXNFW(MyTools.StrFiltr(request.getParameter("CD_DBKXNFW"))); //大补考学年范围
	}
}