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

public class Svl_Wjsz extends HttpServlet {

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
																		// 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows")); // 获得页面rows参数
																		// 分页

		Vector jsonV = null;// 返回结果集
		JSONArray jal = null;// 返回json对象
		WjszBean bean = new WjszBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）

		System.out.println("active的值:" + active);
		// 读取题目
		if (active.equalsIgnoreCase("query")) {
			try {
				// bean.AuthCode();
				bean.setWW_BT(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("WW_BT")),"UTF-8"));
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
		// 保存问卷信息
		if (active.equalsIgnoreCase("save")) {
			try {
				System.out.println("=============================================================进入servlet");
				bean.setWW_WJBH(MyTools.StrFiltr(request.getParameter("WW_WJBH1")));// 问卷编号
				bean.saveRec();
				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "WW_WJBH1", bean.getWW_WJBH());

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
		// 删除问卷
		if (active.equalsIgnoreCase("del")) {
			try {
				bean.DelRec();

				// 2b-传回主关键字及提示消息
				jal = JsonUtil.addJsonParams(jal, "WW_WJBH", bean.getWW_WJBH());
				jal = JsonUtil.addJsonParams(jal, "WW_WJLX", bean.getWW_WJLX());
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
		//查询问卷里的题目(选择题)
			if (active.equalsIgnoreCase("queryWJXZT")) {
				System.out.println("查询问卷里的题目(选择题)");

					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					// bean.AuthCode();
					jsonV = bean.queryWJXZT(pageNum, pageSize);// 按条件查询
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
			//查询问卷里的题目(表单题)
			if (active.equalsIgnoreCase("queryWJBDT")) {
				System.out.println("查询问卷里的题目(表单题)");

					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					// bean.AuthCode();
					jsonV = bean.queryWJBDT(pageNum, pageSize);// 按条件查询
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
			
			// 删除问卷里的选择题
			if (active.equalsIgnoreCase("delWjtm")) {
				try {
					bean.DelWjtm();

					// 2b-传回主关键字及提示消息
					jal = JsonUtil.addJsonParams(jal, "WW_TMBH", bean.getWW_TMBH());
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
			//查询所有(选择题)
			if (active.equalsIgnoreCase("querySyxzt")) {
				System.out.println("查询问卷里的题目(选择题)");

					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					// bean.AuthCode();
					jsonV = bean.querySyxzt(pageNum, pageSize);// 按条件查询
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
			
			// 操作类别：存盘(添加或修改) 前台获取主关键字 判断行为
			// 保存选择题题目到问卷
			if (active.equalsIgnoreCase("AddWjtm")) {
				try {
					System.out.println("===================================================");
						String xztARRAY=MyTools.StrFiltr(request.getParameter("xztARRAY"));
						
						bean.AddWjtm(xztARRAY);
						jal=JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					
					
					// 2b-传回主关键字及提示消息
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
						// 保存表单题题目到问卷
			if (active.equalsIgnoreCase("AddWjtmbdt")) {
				try {
						String bdtARRAY=MyTools.StrFiltr(request.getParameter("bdtARRAY"));
									
						bean.AddWjtmbdt(bdtARRAY);
						jal=JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
								
								
					// 2b-传回主关键字及提示消息
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
			
			//查询所有(表单题)
			if (active.equalsIgnoreCase("querySybdt")) {
				System.out.println("查询问卷里的题目(表单题)");

					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					// bean.AuthCode();
					jsonV = bean.querySybdt(pageNum, pageSize);// 按条件查询
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
			
			//查询所有(辅导员)
			if (active.equalsIgnoreCase("querySyfdy")) {
				System.out.println("查询问卷里(辅导员)");

					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					// bean.AuthCode();
					jsonV = bean.querySyfdy(pageNum, pageSize);// 按条件查询
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
			
			//用来绑定行政班名称combobox
			if (active.equalsIgnoreCase("querySybjfdy")) {
				System.out.println("用来绑定行政班名称combobox");

					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					// bean.AuthCode();
					jsonV = bean.querySybjfdy(pageNum, pageSize);// 按条件查询
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
			
			//查询所有行政班
			if (active.equalsIgnoreCase("querySyxzb")) {
				System.out.println("/查询所有行政班");

					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					// bean.AuthCode();
					jsonV = bean.querySyxzb(pageNum, pageSize);// 按条件查询
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
			
			
			
			// 操作类别：存盘(添加或修改) 前台获取主关键字 判断行为
			// 保存辅导员和行政班代码到问卷
			if (active.equalsIgnoreCase("AddBJFDY")) {
				try {
					bean.AddBJFDY();
					jal=JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					
					
					// 2b-传回主关键字及提示消息
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
			
			// 删除问卷里的选择题
			if (active.equalsIgnoreCase("Delfdy")) {
				try {
					bean.Delfdy();

					// 2b-传回主关键字及提示消息
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
			
			
			//查询无限制专业列表
			if("queMajorList".equalsIgnoreCase(active)){
				try {
					jsonV = bean.queMajorList(pageNum, pageSize);
					jal = (JSONArray)jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//查询专业下拉框数据
			if("loadMajorCombo".equalsIgnoreCase(active)){
				try {
					jsonV = bean.loadMajorCombo();
					jal = (JSONArray) jsonV.get(2);
					response.getWriter().write(jal.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			//保存
			if("savezy".equalsIgnoreCase(active)){
				try {
					bean.addRec();
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			//删除
			if("delzy".equalsIgnoreCase(active)){
				try {
					bean.delRec();
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			// 复制单选多选表单题 
			if (active.equalsIgnoreCase("Adddxdxbdt")) {
				System.out.println("进入复制单选多选表单题servlet ");
				try {
					bean.Adddxdxbdt();
					jal=JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					// 2b-传回主关键字及提示消息
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
			
			// 复制专业教师无限制表
			if (active.equalsIgnoreCase("Addzyjswxzb")) {
					System.out.println("进入复制专业教师无限制表servlet ");
					try {
						bean.Addzyjswxzb();
						jal=JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						// 2b-传回主关键字及提示消息
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
			//用来绑定学年学期名称combobox
			if (active.equalsIgnoreCase("XNXQMCcombobox")) {
				try {
					jsonV = bean.XNXQMCcombobox();// 按条件查询
					jal = (JSONArray) jsonV.get(2);
					response.getWriter().write(jal.toString());
					// 下面这段代码固定，不需要改

				} catch (Exception e) {
					e.printStackTrace();
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				}
			}
			
			//用来绑定行政班名称combobox
			if (active.equalsIgnoreCase("XZBMCcombobox")) {
				System.out.println("用来绑定行政班名称combobox");
				bean.setWW_WJBH(MyTools.StrFiltr(request.getParameter("WW_WJBH_TJ")));// 问卷编号
				bean.setWW_XNXQBM(MyTools.StrFiltr(request.getParameter("WW_XNXQBM_TJ")));// 学年学期编码
				bean.setWW_XZBDM(MyTools.StrFiltr(request.getParameter("WW_XZBDM")));//行政班代码
					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					jsonV = bean.XZBMCcombobox();// 按条件查询
					jal = (JSONArray) jsonV.get(2);
					response.getWriter().write(jal.toString());
					// 下面这段代码固定，不需要改

				} catch (Exception e) {
					e.printStackTrace();
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				}
			}
			
			//用来绑定辅导员姓名combobox
			if (active.equalsIgnoreCase("FDYXMcombobox")) {
				System.out.println("用来绑定辅导员姓名combobox");
				//bean.setWW_WJBH(MyTools.StrFiltr(request.getParameter("WW_WJBH_TJ")));// 问卷编号
					// 操作类别：编辑或查看详情 --根据KEY获取单条详细记录
				try {
					jsonV = bean.FDYXMcombobox();// 按条件查询
					jal = (JSONArray) jsonV.get(2);
					response.getWriter().write(jal.toString());
					// 下面这段代码固定，不需要改

				} catch (Exception e) {
					e.printStackTrace();
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				}
			}
			
			//删除单选多选题
			if("delDXDXT".equalsIgnoreCase(active)){
				try {
					bean.delDXDXT();
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			//编辑辅导员信息
			if("ModRecFDY".equalsIgnoreCase(active)){
				try {
					bean.ModRecFDY();
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				} catch (WrongSQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}catch (SQLException e) {
					e.printStackTrace();
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
	private void getFormData(HttpServletRequest request, WjszBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		
		bean.setWW_WJBH(MyTools.StrFiltr(request.getParameter("WW_WJBH")));// 问卷编号
		bean.setWW_WJBH1(MyTools.StrFiltr(request.getParameter("WW_WJBH_CYM")));// 问卷编号
		
		bean.setWW_WJLX(MyTools.StrFiltr(request.getParameter("WW_WJLX")));// 问卷类型
		bean.setWW_BT(MyTools.StrFiltr(request.getParameter("WW_BT")));// 标题
		bean.setWW_XNXQBM(MyTools.StrFiltr(request.getParameter("WW_XNXQBM")));// 学年学期编码
		bean.setWW_KSSJ(MyTools.StrFiltr(request.getParameter("WW_KSSJ")));// 开始时间
		
		bean.setWW_JSSJ(MyTools.StrFiltr(request.getParameter("WW_JSSJ")));// 结束时间

		bean.setWW_CJR(MyTools.StrFiltr(request.getParameter("WW_CJR")));// 创建人
		bean.setWW_CJSJ(MyTools.StrFiltr(request.getParameter("WW_CJSJ")));// 创建时间
		bean.setWW_ZT(MyTools.StrFiltr(request.getParameter("WW_ZT")));// 状态
		bean.setWW_TMBH(MyTools.StrFiltr(request.getParameter("WW_TMBH")));//选择题表单题题目编号
		
		bean.setWW_GH(MyTools.StrFiltr(request.getParameter("WW_GH")));//辅导员工号
		bean.setWW_XM(MyTools.StrFiltr(request.getParameter("WW_XM")));//辅导员姓名
		bean.setWW_XZBDM(MyTools.StrFiltr(request.getParameter("WW_XZBDM")));//行政班代码
		bean.setWW_XZBMC(MyTools.StrFiltr(request.getParameter("WW_XZBMC")));//行政班名称
		
		bean.setWW_ZYDM(MyTools.StrFiltr(request.getParameter("WW_ZYDM")));//专业代码
		
		bean.setWW_XZBDM1(MyTools.StrFiltr(request.getParameter("WW_XZBDM1")));//行政班代码之前的
		
		
		}

}
