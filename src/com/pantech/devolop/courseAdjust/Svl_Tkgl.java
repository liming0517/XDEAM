package com.pantech.devolop.courseAdjust;

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
import com.pantech.devolop.courseManage.PkszBean;

public class Svl_Tkgl extends HttpServlet {

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
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		//设置字符编码为UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page"));	//获得页面page参数 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows"));	//获得页面rows参数 分页
		
		Vector jsonV = null;//返回结果集
		JSONArray jal = null;//返回json对象
		TkglBean bean = new TkglBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询课程表列表
				jsonV = bean.queRequestNoteList(pageNum, pageSize, "", "", "", "", "", "");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
//				//查询专业下拉框
//				jsonV = bean.loadMajorCombo();
//				jal = JsonUtil.addJsonParams(jal, "zydmData", ((JSONArray)jsonV.get(2)).toString());
//				
//				//查询课程下拉框
//				jsonV = bean.loadCourseCombo();
//				jal = JsonUtil.addJsonParams(jal, "kcbhData", ((JSONArray)jsonV.get(2)).toString());
//				
//				//查询班级下拉框
//				jsonV = bean.loadClassCombo();
//				jal = JsonUtil.addJsonParams(jal, "bjbhData", ((JSONArray)jsonV.get(2)).toString());
				
				//获取当前学年学期
				String xnxq = bean.loadCurSemseter();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", xnxq);
				
				//查询系部下拉框
				jsonV = bean.loadDeptCombo();
				jal = JsonUtil.addJsonParams(jal, "xbdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//20170425yeq修改,原combotree页面效率太慢,修改为combobox
//				jsonV = bean.loadTeaAndSiteCombo();
//				jal = JsonUtil.addJsonParams(jal, "teaComboData", MyTools.StrFiltr(jsonV.get(0)));
//				jal = JsonUtil.addJsonParams(jal, "siteComboData", MyTools.StrFiltr(jsonV.get(1)));
				jsonV = bean.loadTeaCombo();
				jal = JsonUtil.addJsonParams(jal, "teaComboData", ((JSONArray)jsonV.get(2)).toString());
//				jsonV = bean.loadSiteCombo();
//				jal = JsonUtil.addJsonParams(jal, "siteComboData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学年学期课程表列表
		if("queRequestNote".equalsIgnoreCase(active)){
			String TT_XNXQMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("TT_XNXQMC_CX")), "UTF-8");
			String TT_JXXZ_CX = MyTools.StrFiltr(request.getParameter("TT_JXXZ_CX"));
			String TT_XBMC_CX = MyTools.StrFiltr(request.getParameter("TT_ZYMC_CX"));
			String TT_KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("TT_KCMC_CX")), "UTF-8");
			String TT_BJMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("TT_BJMC_CX")), "UTF-8");
			String TT_SQLX_CX = MyTools.StrFiltr(request.getParameter("TT_SQLX_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queRequestNoteList(pageNum, pageSize, TT_XNXQMC_CX, TT_JXXZ_CX, TT_XBMC_CX, TT_KCMC_CX, TT_BJMC_CX, TT_SQLX_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取当前学期专业下拉框
//		if("loadCurMajorCombo".equalsIgnoreCase(active)){
//			try {
//				String result = bean.loadCurMajorCombo();
//				//jal = (JSONArray) jsonV.get(2);
//				//JSONArray.fromObject(jsonV.get(2));
//				response.getWriter().write(result);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		
		//读取当前学期系部下拉框
		if("loadCurDeptCombo".equalsIgnoreCase(active)){
			try {
				String result = bean.loadCurDeptCombo();
				//jal = (JSONArray) jsonV.get(2);
				//JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(result);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询课程下拉框数据
		if("loadCourseCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadCourseCombo();
				jal = (JSONArray) jsonV.get(2);
				JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询班级下拉框数据
		if("loadClassCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadClassCombo();
				jal = (JSONArray) jsonV.get(2);
				JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取当前课程的授课周次
		if("loadSkzcInfo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadSkzcInfo();
				jal = JsonUtil.addJsonParams(jal, "weekNum", MyTools.StrFiltr(jsonV.get(0)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取相应的星期几
		if("loadWeekday".equalsIgnoreCase(active)){
			String weekNum = MyTools.StrFiltr(request.getParameter("weekNum"));
			
			try {
				String result = bean.loadWeekday(weekNum);
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取相应的时间序列
		if("loadOrder".equalsIgnoreCase(active)){
			String weekNum = MyTools.StrFiltr(request.getParameter("weekNum"));
			String weekDay = MyTools.StrFiltr(request.getParameter("weekDay"));
			
			try {
				String result = bean.loadOrder(weekNum, weekDay);
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取相应的教师和场地信息
		if("loadTeaAndSiteInfo".equalsIgnoreCase(active)){
			String weekNum = MyTools.StrFiltr(request.getParameter("weekNum"));
			String weekDay = MyTools.StrFiltr(request.getParameter("weekDay"));
			String order = MyTools.StrFiltr(request.getParameter("order"));
			
			try {
				jsonV = bean.loadTeaAndSiteInfo(weekNum, weekDay, order);
				jal = JsonUtil.addJsonParams(jal, "sameFlag", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "teaCode", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "teaName", MyTools.StrFiltr(jsonV.get(2)));
				jal = JsonUtil.addJsonParams(jal, "siteCode", MyTools.StrFiltr(jsonV.get(3)));
				jal = JsonUtil.addJsonParams(jal, "siteName", MyTools.StrFiltr(jsonV.get(4)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 查询当前学期周次，天数等信息
		if("loadWeekInfo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadWeekInfo();
				jal = JsonUtil.addJsonParams(jal, "weekNum", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "dayNum", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "lessonNum", MyTools.StrFiltr(jsonV.get(2)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存调整课程信息
		if("save".equalsIgnoreCase(active)){
			try {
				bean.saveRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "ID", bean.getTT_ID());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//提交调整课程信息
		if("submit".equalsIgnoreCase(active)){
			try {
				bean.submitRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//审核调整课程信息
		if("pass".equalsIgnoreCase(active) || "reject".equalsIgnoreCase(active)){
			try {
				bean.auditRec(active);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取班级所属系部相关教室信息
		if("loadDeptSiteInfo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadDeptSiteInfo();
				jal = JsonUtil.addJsonParams(jal, "siteComboData", ((JSONArray)jsonV.get(2)).toString());
				
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
    * @param PkszBean
    */
	private void getFormData(HttpServletRequest request,TkglBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setTT_ID(MyTools.StrFiltr(request.getParameter("TT_ID"))); //编号
		bean.setTT_SXLX(MyTools.StrFiltr(request.getParameter("TT_SXLX"))); //时限类型
		bean.setTT_SQLX(MyTools.StrFiltr(request.getParameter("TT_LX"))); //申请类型
		bean.setTT_XNXQBM(MyTools.StrFiltr(request.getParameter("TT_XNXQBM"))); //学年学期编码
		bean.setTT_ZYDM(MyTools.StrFiltr(request.getParameter("TT_ZYDM"))); //专业代码
		bean.setTT_KCBH(MyTools.StrFiltr(request.getParameter("TT_KC"))); //课程编号
		bean.setTT_BJBH(MyTools.StrFiltr(request.getParameter("TT_BJ"))); //班级编号
		bean.setTT_SKJHMXBH(MyTools.StrFiltr(request.getParameter("TT_SKJHMXBH"))); //授课计划明细编号
		bean.setTT_BGLY(MyTools.StrFiltr(request.getParameter("TT_BGLY"))); //变更理由
		bean.setTT_YJHSKZC(MyTools.StrFiltr(request.getParameter("TT_YJHSKZC"))); //原计划授课周次
		bean.setTT_YJHXQ(MyTools.StrFiltr(request.getParameter("TT_YJHXQ"))); //原计划星期
		bean.setTT_YJHSJXL(MyTools.StrFiltr(request.getParameter("TT_YJHSJXL"))); //原计划时间序列
		bean.setTT_YJHSKJSBH(MyTools.StrFiltr(request.getParameter("TT_YJHSKJSBH"))); //原计划授课教师编号
		bean.setTT_YJHSKJSMC(MyTools.StrFiltr(request.getParameter("TT_YJHSKJSMC"))); //原计划授课教师名称
		bean.setTT_YJHCDBH(MyTools.StrFiltr(request.getParameter("TT_YJHCDBH"))); //原计划场地编号
		bean.setTT_YJHCDMC(MyTools.StrFiltr(request.getParameter("TT_YJHCDMC"))); //原计划场地名称
		bean.setTT_TZHSKZC(MyTools.StrFiltr(request.getParameter("TT_TZHSKZC"))); //调整后授课周次
		bean.setTT_TZHXQ(MyTools.StrFiltr(request.getParameter("TT_TZHXQ"))); //调整后星期
		bean.setTT_TZHSJXL(MyTools.StrFiltr(request.getParameter("TT_TZHSJXL"))); //调整后时间序列
		bean.setTT_TZHSKJSBH(MyTools.StrFiltr(request.getParameter("TT_TZHSKJSBH"))); //调整后授课教师编号
		bean.setTT_TZHSKJSMC(MyTools.StrFiltr(request.getParameter("TT_TZHSKJSMC"))); //调整后授课教师名称
		bean.setTT_TZHCDBH(MyTools.StrFiltr(request.getParameter("TT_TZHCDBH"))); //调整后场地编号
		bean.setTT_TZHCDMC(MyTools.StrFiltr(request.getParameter("TT_TZHCDMC"))); //调整后场地名称
		bean.setTT_QTSM(MyTools.StrFiltr(request.getParameter("TT_QTSM"))); //其他说明
		bean.setTT_SHZT(MyTools.StrFiltr(request.getParameter("TT_SHZT"))); //审核状态
		bean.setTT_CJR(MyTools.StrFiltr(request.getParameter("TT_CJR"))); //创建人
		bean.setTT_CJSJ(MyTools.StrFiltr(request.getParameter("TT_CJSJ"))); //创建时间
		bean.setTT_ZT(MyTools.StrFiltr(request.getParameter("TT_ZT"))); //状态
	}
}
