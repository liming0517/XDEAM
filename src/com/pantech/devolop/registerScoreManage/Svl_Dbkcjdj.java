package com.pantech.devolop.registerScoreManage;
/**
编制日期：2016.08.04
创建人：yeq
模块名称：M7.2 补考成绩登记
说明:
功能索引:
	1-查询
	2-设置
**/
import java.io.IOException;
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

public class Svl_Dbkcjdj extends HttpServlet {
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
		DbkcjdjBean bean = new DbkcjdjBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//查询科目列表
		if("queSubjectList".equalsIgnoreCase(active)){
			String XN_CX = MyTools.StrFiltr(request.getParameter("XN_CX"));
			String KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			String KCLX_CX = MyTools.StrFiltr(request.getParameter("KCLX_CX"));
			
			try {
				jsonV = bean.queSubjectList(pageNum, pageSize, XN_CX, KCMC_CX, KCLX_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取文字成绩下拉框数据
		if("loadComboData".equalsIgnoreCase(active)){
			try {
				//查询当前学年
				String curXn = bean.loadCurXn();
				jal = JsonUtil.addJsonParams(jal, "curXn", curXn);
				
				jsonV = bean.loadXnCombo();
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
				
				jsonV = bean.loadWzcjCombo();
				jal = JsonUtil.addJsonParams(jal, "wzcjData", ((JSONArray)jsonV.get(2)).toString());
				
				jsonV = bean.loadWzcjShowCombo();
				jal = JsonUtil.addJsonParams(jal, "wzcjShowData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取当前选择班级课程的学生列表
		if("loadStuList".equalsIgnoreCase(active)){
			String dbkxn = MyTools.StrFiltr(request.getParameter("DBKXN"));
			String kcmc = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC")), "UTF-8");
			
			try {
				String result = bean.loadStuList(dbkxn, kcmc);
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存成绩
		if("saveStuScore".equalsIgnoreCase(active) || "saveStuScoreTimer".equalsIgnoreCase(active)){
			String updateInfo = MyTools.StrFiltr(request.getParameter("updateInfo"));
			
			try {
				bean.saveStuScore(updateInfo);
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
    * @param XqcjdjBean
    */
	private void getFormData(HttpServletRequest request,DbkcjdjBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCX_ID(MyTools.StrFiltr(request.getParameter("CX_ID"))); //编号
		bean.setCX_XH(MyTools.StrFiltr(request.getParameter("CX_XH"))); //学号
		bean.setCX_XM(MyTools.StrFiltr(request.getParameter("CX_XM"))); //姓名
		bean.setCX_XGBH(MyTools.StrFiltr(request.getParameter("CX_XGBH"))); //相关编号
		bean.setCX_ZP(MyTools.StrFiltr(request.getParameter("CX_ZP"))); //总评
		bean.setCX_CX1(MyTools.StrFiltr(request.getParameter("CX_CX1"))); //重修1
		bean.setCX_CX2(MyTools.StrFiltr(request.getParameter("CX_CX2"))); //重修2
		bean.setCX_BK(MyTools.StrFiltr(request.getParameter("CX_BK"))); //补考
		bean.setCX_DBK(MyTools.StrFiltr(request.getParameter("CX_DBK"))); //大补考
		bean.setCX_DYCJ(MyTools.StrFiltr(request.getParameter("CX_DYCJ"))); //打印成绩
		bean.setCX_CJZT(MyTools.StrFiltr(request.getParameter("CX_CJZT"))); //成绩状态
		bean.setCX_CJR(MyTools.StrFiltr(request.getParameter("CX_CJR"))); //创建人
		bean.setCX_CJSJ(MyTools.StrFiltr(request.getParameter("CX_CJSJ"))); //创建时间
		bean.setCX_ZT(MyTools.StrFiltr(request.getParameter("CX_ZT"))); //状态
	}
}