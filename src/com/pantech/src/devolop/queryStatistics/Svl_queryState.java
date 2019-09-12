package com.pantech.src.devolop.queryStatistics;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;
import com.pantech.src.devolop.queryStatistics.QueryStateBean;

public class Svl_queryState extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Svl_queryState() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

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
				QueryStateBean bean = new QueryStateBean(request);
				this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
				
				//教师工作量统计
				if("teagzl".equalsIgnoreCase(active)){
					try {
						//查询列表
						String revec = bean.queryDate(pageNum, pageSize);
						
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
						//TraceLog.Trace("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				//教师授课情况统计
				if("teaskqk".equalsIgnoreCase(active)){
					try {
						//查询列表
						String revec = bean.queryInfo(pageNum, pageSize);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
						//TraceLog.Trace("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				//教师课时费统计
				if("teaksf".equalsIgnoreCase(active)){
					try {
						//查询列表
						String revec = bean.querySubsidy(pageNum, pageSize);
						
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
						//TraceLog.Trace("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if("JSLBHCombobox".equalsIgnoreCase(active)){  //编辑文件
					try {
						jsonV = bean.JSLBHCombobox();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				if("XNXQCombobox".equalsIgnoreCase(active)){  //编辑文件
					try {
						jsonV = bean.XNXQCombobox();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
	}

	/**
	* 从界面没获取参数
	* @date 2015-06-02
	* @author:wangzh
    * @param request
    * @param XnxqBean
    */
	private void getFormData(HttpServletRequest request,QueryStateBean bean){
		bean.setBH(MyTools.StrFiltr(request.getParameter("BH"))); //编号
		bean.setLX(MyTools.StrFiltr(request.getParameter("LX"))); //类型
		bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM"))); //学年学期编码
		bean.setXZBDM(MyTools.StrFiltr(request.getParameter("XZBDM"))); //行政班代码
		bean.setSJXL(MyTools.StrFiltr(request.getParameter("SJXL"))); //时间序列
		bean.setSKJHMXBH(MyTools.StrFiltr(request.getParameter("SKJHMXBH"))); //授课计划明细编号
		bean.setLJXGBH(MyTools.StrFiltr(request.getParameter("LJXGBH"))); //连节相关编号
		bean.setZT(MyTools.StrFiltr(request.getParameter("ZT"))); //状态
		bean.setXNXQ(MyTools.StrFiltr(request.getParameter("XNXQ"))); //学年学期
		bean.setTEAID(MyTools.StrFiltr(request.getParameter("TEAID"))); //教师编号
		bean.setTEANAME(MyTools.StrFiltr(request.getParameter("TEANAME"))); //教师姓名
		bean.setKCJS(MyTools.StrFiltr(request.getParameter("KCJS"))); //课程教师
		bean.setJXXZ(MyTools.StrFiltr(request.getParameter("JXXZ"))); //教学性质
		bean.setQCXX(MyTools.StrFiltr(request.getParameter("QCXX"))); //清除的信息
		bean.setiUSERCODE(MyTools.StrFiltr(request.getParameter("iUSERCODE")));//用户编号
		
	}

}
