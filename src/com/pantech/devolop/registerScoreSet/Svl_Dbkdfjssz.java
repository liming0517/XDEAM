package com.pantech.devolop.registerScoreSet;

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

public class Svl_Dbkdfjssz extends HttpServlet {
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
		DbkdfjsszBean bean = new DbkdfjsszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化下拉框页面数据
		if("initComboData".equalsIgnoreCase(active)){
			try {
				//查询当前学年
				String curXn = bean.loadCurXn();
				jal = JsonUtil.addJsonParams(jal, "curXn", curXn);
				
				//查询学年下拉框
				jsonV = bean.loadXnCombo();
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询科目列表
		if("queCourseList".equalsIgnoreCase(active)){
			String DBKXN_CX = MyTools.StrFiltr(request.getParameter("DBKXN_CX"));
			String ZYMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZYMC_CX")), "UTF-8");
			String KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			String KCLX_CX = MyTools.StrFiltr(request.getParameter("KCLX_CX"));
			
			try {
				jsonV = bean.queCourseList(pageNum, pageSize, DBKXN_CX, ZYMC_CX, KCMC_CX, KCLX_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询登分教师列表
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
		
		//保存登分教师信息
		if("saveTea".equalsIgnoreCase(active)){
			try {
				bean.saveTea();
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
    * @param DfryszBean
    */
	private void getFormData(HttpServletRequest request, DbkdfjsszBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCD_ID(MyTools.StrFiltr(request.getParameter("CD_ID"))); //编号
		bean.setCD_LYLX(MyTools.StrFiltr(request.getParameter("CD_LYLX"))); //来源类型
		bean.setCD_KCDM(MyTools.StrFiltr(request.getParameter("CD_KCDM"))); //课程代码
		bean.setCD_KCMC(MyTools.StrFiltr(request.getParameter("CD_KCMC"))); //课程名称
		bean.setCD_DFJSBH(MyTools.StrFiltr(request.getParameter("CD_DFJSBH"))); //登分教师编号
		bean.setCD_DFJSXM(MyTools.StrFiltr(request.getParameter("CD_DFJSXM"))); //登分教师姓名
		bean.setCD_CJR(MyTools.StrFiltr(request.getParameter("CD_CJR"))); //创建人
		bean.setCD_CJSJ(MyTools.StrFiltr(request.getParameter("CD_CJSJ"))); //创建时间
		bean.setCD_ZT(MyTools.StrFiltr(request.getParameter("CD_ZT"))); //状态
	}
}