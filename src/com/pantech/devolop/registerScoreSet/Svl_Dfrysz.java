package com.pantech.devolop.registerScoreSet;
/**
编制日期：2016.02.01
创建人：yeq
模块名称：M6.3 登分人员设置
说明:
	 
功能索引:
	1-查询
	2-设置
**/
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

public class Svl_Dfrysz extends HttpServlet {

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
		DfryszBean bean = new DfryszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化下拉框页面数据
		if("initComboData".equalsIgnoreCase(active)){
			try {
				//查询课程表列表
//				jsonV = bean.queCourseList(pageNum, pageSize, "", "", "", "", "", "");
//				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询科目列表
		if("queCourseList".equalsIgnoreCase(active)){
			String XNXQMC_CX = MyTools.StrFiltr(request.getParameter("XNXQMC_CX"));
			String JXXZ_CX = MyTools.StrFiltr(request.getParameter("JXXZ_CX"));
			String ZYMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZYMC_CX")), "UTF-8");
			String XZBMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XZBMC_CX")), "UTF-8");
			String KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			String KCLX_CX = MyTools.StrFiltr(request.getParameter("KCLX_CX"));
			
			try {
				//删除重复的班级课程
				//20170914修改为进入成绩系统执行此操作
				//KmszBean.delRepeatCourse(request);
				
				jsonV = bean.queCourseList(pageNum, pageSize, XNXQMC_CX, JXXZ_CX, ZYMC_CX, XZBMC_CX, KCMC_CX, KCLX_CX);
				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
//				response.getWriter().write(jal.toString());
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
		
		//查询学生列表
		if("queStuList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadStuList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学生列表
		if("queDelStuList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadDelStuList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询课程列表
		if("queClassCourseList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queClassCourseList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存学生信息
		if("saveStu".equalsIgnoreCase(active)){
			try {
				bean.saveStu();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//查询班级学生树数据
		if("queClassStuTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			String classCode = MyTools.StrFiltr(request.getParameter("classCode"));
			
			try {
				jsonV = bean.queClassStuTree(parentCode, classCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//保存新增学生
		if("saveAddStu".equalsIgnoreCase(active)){
			String stuCode = MyTools.StrFiltr(request.getParameter("addStuInfo"));
			
			try {
				bean.saveAddStu(stuCode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//删除学生
		if("delStu".equalsIgnoreCase(active)){
			String delStuCode = MyTools.StrFiltr(request.getParameter("delStuCode"));
			
			try {
				String stuCode = bean.delStu(delStuCode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "stuCode", stuCode);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//添加学生成绩基础数据
		if("addStuScoreData".equalsIgnoreCase(active)){
			try {
				String stuCode = bean.addStuScoreData();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "stuCode", stuCode);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//同步学生名单
		if("syncStuList".equalsIgnoreCase(active)){
			String courseCode = MyTools.StrFiltr(request.getParameter("courseCode"));
			
			try {
				bean.saveStu();
				bean.syncStuList(courseCode);
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
	private void getFormData(HttpServletRequest request,DfryszBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCD_ID(MyTools.StrFiltr(request.getParameter("CD_ID"))); //编号
		bean.setCD_KMBH(MyTools.StrFiltr(request.getParameter("CD_KMBH"))); //科目编号
		bean.setCD_LYLX(MyTools.StrFiltr(request.getParameter("CD_LYLX"))); //来源类型
		bean.setCD_XGBH(MyTools.StrFiltr(request.getParameter("CD_XGBH"))); //相关编号
		bean.setCD_XZBDM(MyTools.StrFiltr(request.getParameter("CD_XZBDM"))); //行政班代码
		bean.setCD_XZBMC(MyTools.StrFiltr(request.getParameter("CD_XZBMC"))); //行政班名称
		bean.setCD_KCDM(MyTools.StrFiltr(request.getParameter("CD_KCDM"))); //课程代码
		bean.setCD_KCMC(MyTools.StrFiltr(request.getParameter("CD_KCMC"))); //课程名称
		bean.setCD_DFJSBH(MyTools.StrFiltr(request.getParameter("CD_DFJSBH"))); //登分教师编号
		bean.setCD_DFJSXM(MyTools.StrFiltr(request.getParameter("CD_DFJSXM"))); //登分教师姓名
		bean.setCD_CJR(MyTools.StrFiltr(request.getParameter("CD_CJR"))); //创建人
		bean.setCD_CJSJ(MyTools.StrFiltr(request.getParameter("CD_CJSJ"))); //创建时间
		bean.setCD_ZT(MyTools.StrFiltr(request.getParameter("CD_ZT"))); //状态
		bean.setCX_XH(MyTools.StrFiltr(request.getParameter("CX_XH"))); //学号
		bean.setCX_XM(MyTools.StrFiltr(request.getParameter("CX_XM"))); //姓名
	}
}