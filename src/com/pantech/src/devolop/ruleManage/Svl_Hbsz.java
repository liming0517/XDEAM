package com.pantech.src.devolop.ruleManage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_Hbsz extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Svl_Hbsz() {
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
		String iKeyCode = MyTools.StrFiltr(request.getParameter("GT_JSBH"));
		String year = MyTools.StrFiltr(request.getParameter("year"));
		String xz = MyTools.StrFiltr(request.getParameter("xz"));
		String xk = MyTools.StrFiltr(request.getParameter("xk"));
		String bj = MyTools.StrFiltr(request.getParameter("bj"));
		int pageNum = MyTools.parseInt(request.getParameter("page"));	//获得页面page参数 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows"));	//获得页面rows参数 分页
		System.out.println("active:"+active);
		Vector jsonV = null;//返回结果集
		JSONArray jal = null;//返回json对象
		HbszBean bean = new HbszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询有效的特殊规则列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学科名称下拉框
				jsonV = bean.loadZYMCCombo();
				jal = JsonUtil.addJsonParams(jal, "zymcData", ((JSONArray)jsonV.get(2)).toString());
				
								
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//获取选择学期的课程
		if("loadCourse".equalsIgnoreCase(active)){
			try {
				String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
				String jxxz = MyTools.StrFiltr(request.getParameter("jxxz"));
				String zydm = MyTools.StrFiltr(request.getParameter("zydm"));
				String classid = MyTools.StrFiltr(request.getParameter("classid"));

				//查询班级名称下拉框
				jsonV = bean.loadKCMCCombo(xnxq,jxxz,zydm,classid);
				jal = (JSONArray)jsonV.get(2);
						
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//获取选择课程的班级
		if("loadClass".equalsIgnoreCase(active)){
			try {
				String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
				String jxxz = MyTools.StrFiltr(request.getParameter("jxxz"));
				String zydm = MyTools.StrFiltr(request.getParameter("zydm"));

				//查询班级名称下拉框
				jsonV = bean.loadBJMCCombo(xnxq,jxxz,zydm);
				jal = (JSONArray)jsonV.get(2);
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询有效的特殊规则列表
		if("query".equalsIgnoreCase(active)){
			try {
				//查询特殊规则列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("shoukeData".equalsIgnoreCase(active)) {
			try {
				//查询特殊规则列表
				jsonV = bean.queryShouke(year, xz, xk, bj);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("shoukeData2".equalsIgnoreCase(active)) {
			String bianhao=MyTools.StrFiltr(request.getParameter("bianhao"));
			try {
				//查询特殊规则列表
				jsonV = bean.queryShouke2(year, xz, xk, bj,bianhao);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("save".equalsIgnoreCase(active)){
			String kcdm=MyTools.StrFiltr(request.getParameter("kcdm"));
			String bjdm=MyTools.StrFiltr(request.getParameter("bjdm"));
			String jxxz=MyTools.StrFiltr(request.getParameter("jxxz"));
			String bianhao=MyTools.StrFiltr(request.getParameter("bianhao"));
			try {
				bean.saveRec(kcdm,bjdm,jxxz,bianhao);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "msg2", bean.getMSG2());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("del".equalsIgnoreCase(active)){
			try {
				bean.delRec(iKeyCode);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("changeShouke".equalsIgnoreCase(active)) {
			try {
				String mainCode = MyTools.StrFiltr(request.getParameter("mainCode"));
				String otherCode = MyTools.StrFiltr(request.getParameter("otherCode"));
				bean.changeShoukeRec(mainCode, otherCode);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "msg2", bean.getMSG2());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void getFormData(HttpServletRequest request, HbszBean bean) {
		bean.setGH_BH(MyTools.StrFiltr(request.getParameter("GH_BH"))); //编号
		bean.setGH_XNXQBM(MyTools.StrFiltr(request.getParameter("GH_XNXQBM"))); //学年学期编码
		bean.setGH_KCDM(MyTools.StrFiltr(request.getParameter("GH_KCDM"))); //课程代码
		bean.setGH_KCMC(MyTools.StrFiltr(request.getParameter("GH_KCMC"))); //课程名称
		bean.setGH_XZBDM(request.getParameterValues("GH_XZBDM")); //行政班代码
		bean.setGH_XZBMC(MyTools.StrFiltr(request.getParameter("GH_XZBMC"))); //行政班名称
		bean.setGH_ZT(MyTools.StrFiltr(request.getParameter("GH_ZT"))); //状态
	}
}
