/**
	编制日期：2015.05.15
	创建人：yeq
	模块名称：M1.1学年学期设置
	说明:
		 
	功能索引:
		1-新建
		2-编辑
		3-查询
**/

package com.pantech.src.devolop.ruleManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.PublicTools;

public class Svl_Xnxq extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Svl_Xnxq() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
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
		String active1 = MyTools.StrFiltr(request.getParameter("active1"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page"));	//获得页面page参数 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows"));	//获得页面rows参数 分页
		
		Vector jsonV = null;//返回结果集
		JSONArray jal = null;//返回json对象
		XnxqBean bean = new XnxqBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize, "", "");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
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
		if("query".equalsIgnoreCase(active)){
			String GX_XNXQMC_CX = MyTools.StrFiltr(request.getParameter("GX_XNXQMC_CX"));
			String GX_JXXZ_CX = MyTools.StrFiltr(request.getParameter("GX_JXXZ_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize, GX_XNXQMC_CX, GX_JXXZ_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询节次时间
		if ("loadTime".equalsIgnoreCase(active)) {
			try {
				jsonV = bean.loadTime();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// 异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "msg", "无法获取数据！");
				response.getWriter().write(jal.toString());
			}
		}
		
		//保存
		if("save".equalsIgnoreCase(active)){
			try {
				bean.saveRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WrongSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//节次时间保存
		if("saveTime".equalsIgnoreCase(active1)){
			try {
				bean.saveTime();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WrongSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//更新学期节假日信息
		if("updateHoliday".equalsIgnoreCase(active)){
			try {
				bean.updateHoliday();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param XnxqBean
    */
	private void getFormData(HttpServletRequest request,XnxqBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setGX_XNXQBM(MyTools.StrFiltr(request.getParameter("GX_XNXQBM"))); //学年学期编号
		bean.setGX_XN(MyTools.StrFiltr(request.getParameter("GX_XN"))); //学年
		bean.setGX_XQ(MyTools.StrFiltr(request.getParameter("GX_XQ"))); //学期
		bean.setGX_XNXQMC(MyTools.StrFiltr(request.getParameter("GX_XNXQMC"))); //学年学期名称
		bean.setGX_JXXZ(MyTools.StrFiltr(request.getParameter("GX_JXXZ"))); //教学性质
		bean.setGX_XQKSSJ(MyTools.StrFiltr(request.getParameter("GX_XQKSSJ"))); //学期开始时间
		bean.setGX_XQJSSJ(MyTools.StrFiltr(request.getParameter("GX_XQJSSJ"))); //学期结束时间
		bean.setGX_JJRQ(MyTools.StrFiltr(request.getParameter("GX_JJRQ"))); //节假日期
		bean.setGX_PKJZSJ(MyTools.StrFiltr(request.getParameter("GX_PKJZSJ"))); //排课截止时间

		bean.setGX_ZCBH(MyTools.StrFiltr(request.getParameter("GX_ZCBH"))); //周次编号
		bean.setGX_ZC(MyTools.StrFiltr(request.getParameter("GX_ZC"))); //周次
		bean.setGX_KSRQ(MyTools.StrFiltr(request.getParameter("GX_KSRQ"))); //开始日期
		bean.setGX_JSRQ(MyTools.StrFiltr(request.getParameter("GX_JSRQ"))); //结束日期
		
		bean.setGX_MZTS(MyTools.StrFiltr(request.getParameter("GX_MZTS"))); //每周天数
		bean.setGX_SWJS(MyTools.StrFiltr(request.getParameter("GX_SWJS"))); //上午节数
		bean.setGX_ZWJS(MyTools.StrFiltr(request.getParameter("GX_ZWJS"))); //中午节数
		bean.setGX_XWJS(MyTools.StrFiltr(request.getParameter("GX_XWJS"))); //下午节数
		bean.setGX_WSJS(MyTools.StrFiltr(request.getParameter("GX_WSJS"))); //晚上节数
		bean.setGX_SJSKZS(MyTools.StrFiltr(request.getParameter("GX_SJSKZS"))); //实际上课周数
		bean.setGX_XQFW(MyTools.StrFiltr(request.getParameter("GX_XQFW"))); //学期范围是否改动过
		
		bean.setGJ_CODE(MyTools.StrFiltr(request.getParameter("GJ_CODE"))); //编号
		bean.setGJ_XNXQBM(MyTools.StrFiltr(request.getParameter("GJ_XNXQBM"))); //学年学期编码
		bean.setGJ_JC(MyTools.StrFiltr(request.getParameter("GJ_JC"))); //节次
		bean.setGJ_KSSJ(MyTools.StrFiltr(request.getParameter("GJ_KSSJ"))); //开始时间
		bean.setGJ_JSSJ(MyTools.StrFiltr(request.getParameter("GJ_JSSJ"))); //结束时间
	}
}