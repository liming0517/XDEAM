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
import com.pantech.base.common.tools.TraceLog;

public class Svl_JyzxxSet extends HttpServlet {
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
		
		TraceLog.Trace("active...:"+active);
		Vector jsonV = null;//返回结果集
		JSONArray jal = null;//返回json对象     
		JyzxxSetBean bean = new JyzxxSetBean(request); //对象
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		if("initData".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queJYZList(pageNum, pageSize);
				jal = JsonUtil.addJsonParams(jal, "jyzData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				
				//查询教研组组长下拉框
				jsonV = bean.loadJYZzzCombo();
				jal = JsonUtil.addJsonParams(jal, "jyzzzData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学科下拉框
				jsonV = bean.loadXKzzCombo();
				jal = JsonUtil.addJsonParams(jal, "jyzxkData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//查询教研组列表
		if("query".equalsIgnoreCase(active)){
			bean.setJN_JYZMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JN_JYZMC")), "UTF-8"));
			bean.setJN_JYZXK(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JN_JYZXK")), "UTF-8"));
			bean.setJN_JYXZZBH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JN_JYZZZ")), "UTF-8"));
			try {
				jsonV = bean.queJYZList(pageNum, pageSize);
				if (jsonV != null && jsonV.size() > 0) {
					//最终处理：传回AJAX 结果集
					jal = (JSONArray)jsonV.get(2);
					jal = JsonUtil.addJsonParams(jal, "jyzData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
					response.getWriter().write(jal.toString());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "查询错误!"+ bean.getMSG() );
				response.getWriter().write(jal.toString());
			}
		}
		
		//保存教研组信息
		if ("save".equalsIgnoreCase(active)) {
			String saveType = MyTools.StrFiltr(request.getParameter("saveType"));
			
			try {
				if("new".equalsIgnoreCase(saveType)){
					bean.AddRec();
				}else{
					bean.ModRec();
				}
				// 传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (WrongSQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG","操作失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		if("del".equalsIgnoreCase(active)){
			try {							
				bean.DelRec();			
				//传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (WrongSQLException e) {
				//异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG() );
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				//异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG() );
				response.getWriter().write(jal.toString());
			}	
		}
	}
	
	/**
	* 从界面没获取参数
	* @date 
    * @param request
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, JyzxxSetBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setJN_JYZDM(MyTools.StrFiltr(request.getParameter("JN_JYZDM1"))); //教研组名称
		bean.setJN_JYZMC(MyTools.StrFiltr(request.getParameter("JN_JYZXK1"))); //教研组名称
		bean.setJN_JYZXK(MyTools.StrFiltr(request.getParameter("JN_XKMC1"))); //教研组学科
		bean.setJN_JYXZZBH(MyTools.StrFiltr(request.getParameter("JN_JYZZZBH"))); //教研组组长
		bean.setJN_CJR(MyTools.StrFiltr(request.getParameter("JN_CJR"))); //创建人
		bean.setJN_CJSJ(MyTools.StrFiltr(request.getParameter("JN_CJSJ"))); //创建时间
		bean.setJN_ZT(MyTools.StrFiltr(request.getParameter("JN_ZT"))); //状态
	}
}