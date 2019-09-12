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

import net.sf.json.JSONArray;

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_Wjjgcx extends HttpServlet {

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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page")); // 获得页面page参数
		int pageSize = MyTools.parseInt(request.getParameter("rows")); // 获得页面rows参数
		Vector jsonV = null;// 返回结果集
		JSONArray jal = null;// 返回json对象
		WjjgcxBean bean = new WjjgcxBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）

		// 读取题目
		if (active.equalsIgnoreCase("query")) {
			try {
				bean.setWJ_BT(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_BT")),"UTF-8"));
				bean.setWJ_XNXQBM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_XNXQBM_CX")),"UTF-8"));
				bean.setWJ_KSSJ(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_KSSJ_CX")),"UTF-8"));
				bean.setWJ_JSSJ(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_JSSJ_CX")),"UTF-8"));
				jsonV = bean.queryRec(pageNum, pageSize);// 按条件查询
				if (jsonV != null && jsonV.size() > 0) {
					// 最终处理：传回AJAX 结果集
					jal = (JSONArray) jsonV.get(2);
					if (!"".equalsIgnoreCase(bean.getAUTHCODE()) && jal.size() > 0) {// 避免出现空行
						jal = JsonUtil.addJsonParams(jal, "icAUTHCODE", bean.getAUTHCODE());
					}
					if (!"".equalsIgnoreCase(bean.getListsql()) && jal.size() > 0) {// 此句避免出现空行
						jal = JsonUtil.addJsonParams(jal, "listsql", bean.getListsql());
					}
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
			} catch (Exception e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		
		// 读取结果信息
		if (active.equalsIgnoreCase("queryCKJG")) {
			try {
				bean.setWJ_NJ(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_NJ_CX")),"UTF-8"));
				bean.setWJ_WCQK(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_WCQK_CX")),"UTF-8"));
				bean.setWJ_XZBMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_BJMC_CX")),"UTF-8"));
				bean.setWJ_XNXQBM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_XNXQBM_CX")),"UTF-8"));
				jsonV = bean.queryRecCKJG(pageNum, pageSize);// 按条件查询
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
		
		//查询年级下拉框数据
		if("loadNjCombo".equalsIgnoreCase(active)){
			try {
				bean.setWJ_XNXQBM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_XNXQBM")),"UTF-8"));
				jsonV = bean.loadNjCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (active.equalsIgnoreCase("queryOne")) {
			// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
			String page = MyTools.StrFiltr(request.getParameter("page"));
			String rows = MyTools.StrFiltr(request.getParameter("rows"));
			bean.setWJ_XZBDM(MyTools.StrFiltr(request.getParameter("WJ_XZBDM")));//行政班代码
			try {
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
		
		
		//查询重置按钮里的问卷下拉框数据
		if("loadCZWJMCCombo".equalsIgnoreCase(active)){
			try {
				bean.setWJ_XNXQBM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_XNXQBM")),"UTF-8"));
				jsonV = bean.loadCZWJMCCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询重置按钮里的班级下拉框数据
		if("loadCZBJCombo".equalsIgnoreCase(active)){
			try {
				bean.setWJ_XNXQBM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_XNXQBM")),"UTF-8"));
				jsonV = bean.loadCZBJCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询重置按钮里班级里的所有学生
		if("loadBJXSCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadBJXSCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		// 删除问卷
		if("delCZWJXX".equalsIgnoreCase(active)){
			try {
				bean.setWJ_XNXQBM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WJ_XNXQBM")),"UTF-8"));
				String CZ_XSXM = MyTools.StrFiltr(request.getParameter("CZ_XSXM"));
				bean.delCZWJXX(CZ_XSXM);

				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());

			} catch (SQLException e) {
				// 异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		
		// 获取当前学年学期
		if("loadCurXnxq".equalsIgnoreCase(active)){
			try {
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);

				// 2b-传回主关键字及提示消息
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
	 * @param WjjgcxBean
	 */
	private void getFormData(HttpServletRequest request, WjjgcxBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		bean.setWJ_WJBH(MyTools.StrFiltr(request.getParameter("WJ_WJBH")));// 问卷编号
		bean.setWJ_WJLX(MyTools.StrFiltr(request.getParameter("WJ_WJLX")));// 问卷类型
		bean.setWJ_BT(MyTools.StrFiltr(request.getParameter("WJ_BT")));// 标题
		bean.setWJ_XNXQBM(MyTools.StrFiltr(request.getParameter("WJ_XNXQBM_CK")));// 学年学期编码
		bean.setWJ_KSSJ(MyTools.StrFiltr(request.getParameter("WJ_KSSJ")));// 开始时间
		bean.setWJ_JSSJ(MyTools.StrFiltr(request.getParameter("WJ_JSSJ")));// 结束时间
		bean.setWJ_CJR(MyTools.StrFiltr(request.getParameter("WJ_CJR")));// 创建人
		bean.setWJ_CJSJ(MyTools.StrFiltr(request.getParameter("WJ_CJSJ")));// 创建时间
		bean.setWJ_ZT(MyTools.StrFiltr(request.getParameter("WJ_ZT")));// 状态
		bean.setWJ_NJ(MyTools.StrFiltr(request.getParameter("WJ_NJ_CX")));// 年级
		bean.setWJ_XZBMC(MyTools.StrFiltr(request.getParameter("WJ_BJMC_CX")));// 行政班名称
		bean.setWJ_WCQK(MyTools.StrFiltr(request.getParameter("WJ_WCQK_CX")));// 完成情况
		bean.setWJ_XZBDM(MyTools.StrFiltr(request.getParameter("WJ_XZBDM")));//行政班代码
	}
}