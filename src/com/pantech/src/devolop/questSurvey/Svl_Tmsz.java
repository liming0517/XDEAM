package com.pantech.src.devolop.questSurvey;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import net.sf.json.JSONArray;

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_Tmsz extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page")); // 获得页面page参数
																		// 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows")); // 获得页面rows参数
																		// 分页

		Vector jsonV = null;// 返回结果集
		JSONArray jal = null;// 返回json对象
		TmszBean bean = new TmszBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）

		System.out.println("active的值:" + active);
		// 读取题目
		if (active.equalsIgnoreCase("query")) {
			try {
				// bean.AuthCode();
				bean.setTT_TMNR(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("TT_TMNR")),"UTF-8"));
				jsonV = bean.queryRec(pageNum, pageSize);// 按条件查询
				// 下面这段代码固定，不需要改
				// [0,rows,2] 0代表的是有多少条数据，rows代表的是有多少行，2代表的是json对象
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					if (!"".equalsIgnoreCase(bean.getAUTHCODE())
							&& jal.size() > 0) {// 避免出现空行
						jal = JsonUtil.addJsonParams(jal, "icAUTHCODE",
								bean.getAUTHCODE());
					}
					if (!"".equalsIgnoreCase(bean.getListsql())
							&& jal.size() > 0) {// 此句避免出现空行
						jal = JsonUtil.addJsonParams(jal, "listsql",
								bean.getListsql());
					}
					response.getWriter().write(
							"{\"total\":" + MyTools.StrFiltr(jsonV.get(0))
									+ ",\"rows\":" + jal.toString() + "}");

					// 固定代码结束
				}
			} catch (Exception e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}

		if (active.equalsIgnoreCase("view")) {
			System.out.println("查询");
			// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
			try {
				bean.loadData();// 读取一条记录
				// 将BEAN传回
				jal = JsonUtil.beanToJsonarray(bean);
				// TraceLog.Trace("resp:   "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}

		if (active.equalsIgnoreCase("One")) {
			// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
			String page = MyTools.StrFiltr(request.getParameter("page"));
			String rows = MyTools.StrFiltr(request.getParameter("rows"));
			try {
				// bean.AuthCode();
				jsonV = bean.queryOne(MyTools.parseInt(page),
						MyTools.parseInt(rows));// 按条件查询
				// 下面这段代码固定，不需要改
				// [0,rows,2] 0代表的是有多少条数据，rows代表的是有多少行，2代表的是json对象
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					if (!"".equalsIgnoreCase(bean.getAUTHCODE())
							&& jal.size() > 0) {// 避免出现空行
						jal = JsonUtil.addJsonParams(jal, "icAUTHCODE",
								bean.getAUTHCODE());
					}
					if (!"".equalsIgnoreCase(bean.getListsql())
							&& jal.size() > 0) {// 此句避免出现空行
						jal = JsonUtil.addJsonParams(jal, "listsql",
								bean.getListsql());
					}
					response.getWriter().write(
							"{\"total\":" + MyTools.StrFiltr(jsonV.get(0))
									+ ",\"rows\":" + jal.toString() + "}");
				}
				// 固定代码结束
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		// 操作类别：存盘(添加或修改) 前台获取主关键字 判断行为
		// 保存表单题信息
		if (active.equalsIgnoreCase("save")) {
			try {
	
				bean.saveRec();
				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "TT_TMBH", bean.getTT_TMBH());
				jal=JsonUtil.addJsonParams(jal, "TT_TMNR", bean.getTT_TMNR());
				// jal = JsonUtil.addJsonParams(jal, "icAA_CODE1",
				// bean.getAA_CODE());

				response.getWriter().write(jal.toString());
				System.out.println(jal.toString());
			} catch (WrongSQLException e) {

				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {

				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		// 删除
		if (active.equalsIgnoreCase("del")) {
			try {
				bean.DelRec();

				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "TT_TMBH", bean.getTT_TMBH());
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());

			} catch (WrongSQLException e) {
				// 异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// 异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		//查询选择题选项
		if (active.equalsIgnoreCase("queryXZT")) {
			System.out.println("查询选择题选项");

			// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
			try {
				// bean.AuthCode();
				jsonV = bean.queryXZTXX(pageNum, pageSize);// 按条件查询
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(
						"{\"total\":" + MyTools.StrFiltr(jsonV.get(0))
								+ ",\"rows\":" + jal.toString() + "}");
				// 下面这段代码固定，不需要改

			} catch (Exception e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		
		//删除选择题选项
		if (active.equalsIgnoreCase("delXZTXX")) {
			try {
				bean.setTT_XXBH(MyTools.StrFiltr(request.getParameter("TT_XXBH")));// 选择题选项编号
				System.out.println("========================================="+bean.getTT_XXBH());
				bean.DelXztxx();

				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "TT_XXBH", bean.getTT_XXBH());
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());

			} catch (WrongSQLException e) {
				// 异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// 异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		// 操作类别：存盘(添加或修改) 前台获取主关键字 判断行为
		// 保存选择题选项信息
		if (active.equalsIgnoreCase("saveXztxx")) {
			bean.setTT_TMBH(MyTools.StrFiltr(request.getParameter("TT_TMBH1")));// 题目编号
			try {
				bean.saveXztxx();
				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "TT_XX", bean.getTT_XX());
				jal = JsonUtil.addJsonParams(jal, "TT_TMBH", bean.getTT_TMBH());
				jal = JsonUtil.addJsonParams(jal, "TT_XXNR", bean.getTT_XXNR());
				
				response.getWriter().write(jal.toString());
				System.out.println(jal.toString());
			} catch (WrongSQLException e) {

				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {

				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		// 操作类别：存盘(添加或修改) 前台获取主关键字 判断行为
		// 保存选择题信息
		if(active.equalsIgnoreCase("saveXzt")){
			try {
				bean.saveXzt();
				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "TT_TMBH", bean.getTT_TMBH());
				jal = JsonUtil.addJsonParams(jal, "XZT_TMLX", bean.getTT_TMLX());
				jal = JsonUtil.addJsonParams(jal, "XZT_TMNR", bean.getTT_TMNR());
				response.getWriter().write(jal.toString());
				System.out.println(jal.toString());
			} catch (WrongSQLException e) {

				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {

				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
			
		}
		//删除选择题===========================================================================================
				if (active.equalsIgnoreCase("delXZT")) {
					try {
						bean.DelXzt();

						// 2b-传回主关键字及提示消息
						jal = JsonUtil.addJsonParams(jal, "TT_TMBH", bean.getTT_TMBH());
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());

					} catch (WrongSQLException e) {
						// 异常处理
						e.printStackTrace();
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					} catch (SQLException e) {
						// 异常处理
						e.printStackTrace();
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					}
				}

	}

	/**
	 * 从界面获取参数
	 * 
	 * @date
	 * @author:
	 * @param request
	 * @param KbcxBean
	 */
	private void getFormData(HttpServletRequest request, TmszBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		bean.setTT_TMBH(MyTools.StrFiltr(request.getParameter("TT_TMBH")));// 题目编号
		
		bean.setTT_TMNR(MyTools.StrFiltr(request.getParameter("TT_TMNR")));// 题目内容
		bean.setTT_TMLX(MyTools.StrFiltr(request.getParameter("TT_TMLX")));// 状态
		
		bean.setTT_TMYD(MyTools.StrFiltr(request.getParameter("TT_TMYD_BDSZ")));// 题目要点
		
		bean.setTT_FZ(MyTools.StrFiltr(request.getParameter("TT_FZ_BDSZ")));// 分值
		
		bean.setTT_CJR(MyTools.StrFiltr(request.getParameter("TT_CJR")));// 创建人

		bean.setTT_XXBH(MyTools.StrFiltr(request.getParameter("TT_XXBH")));// 选择题选项编号
		bean.setTT_XX(MyTools.StrFiltr(request.getParameter("TT_XX")));// 选择题选项
		bean.setTT_XXNR(MyTools.StrFiltr(request.getParameter("TT_XXNR")));// 选择题选项
		
		}

}
