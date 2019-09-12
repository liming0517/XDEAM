package com.pantech.src.devolop.ruleManage;

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
import com.pantech.base.common.tools.TraceLog;
import com.sun.corba.se.impl.util.Utility;

public class Svl_CourseSet extends HttpServlet {

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
		CourseSetBean bean = new CourseSetBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		System.out.println("active-------------------------------------------------------"+active);
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize, "", "", "", "");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询课程类型下拉框
				jsonV = bean.loadKCLXCombo();
				jal = JsonUtil.addJsonParams(jal, "kclxData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询专业下拉框
				jsonV = bean.loadMajorCombo();
				jal = JsonUtil.addJsonParams(jal, "zydmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询任课教师下拉框
				jsonV = bean.loadKSXSCombo();
				jal = JsonUtil.addJsonParams(jal, "ksxsData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询任课教师下拉框
				jsonV = bean.loadDBKKSXSCombo();
				jal = JsonUtil.addJsonParams(jal, "dbkksxsData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadXBDMCombo();
				jal = JsonUtil.addJsonParams(jal, "xbdmData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询专业列表
		if("query".equalsIgnoreCase(active)){
			String KCDM_CX = MyTools.StrFiltr(request.getParameter("KCDM_CX"));
			String KCMC_CX = MyTools.StrFiltr(request.getParameter("KCMC_CX"));
			String ZYDM_CX = MyTools.StrFiltr(request.getParameter("ZYDM_CX"));
			String KCLX_CX = MyTools.StrFiltr(request.getParameter("KCLX_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize, KCDM_CX, KCMC_CX, ZYDM_CX, KCLX_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//新建
		if("new".equalsIgnoreCase(active)){
			try {
				bean.addRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//编辑
		if("edit".equalsIgnoreCase(active)){
			String kcbh = MyTools.StrFiltr(request.getParameter("kcbh"));
			String oldKCMC = MyTools.StrFiltr(request.getParameter("oldKCMC"));
			try {
				bean.modRec(kcbh,oldKCMC);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//删除
		if("del".equalsIgnoreCase(active)){
			try {
				bean.delRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//选课信息
		if("loadSelection".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				//查询学年学期列表
				jsonV = bean.loadSelection(pageNum, pageSize,iUSERCODE);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//学生信息
				jsonV=bean.getStuInfo(iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "Student", ((JSONArray)jsonV.get(2)).toString());

				jal = JsonUtil.addJsonParams(jal, "firAPP", bean.getFirst_APP());
				jal = JsonUtil.addJsonParams(jal, "secAPP", bean.getSecond_APP());
				jal = JsonUtil.addJsonParams(jal, "thiAPP", bean.getThird_APP());
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//选课信息
		if("loadAdminSelection".equalsIgnoreCase(active)){
			String SC_XNXQ = MyTools.StrFiltr(request.getParameter("SC_XNXQ"));
			String xxkskjhmx = MyTools.StrFiltr(request.getParameter("xxkskjhmx"));
			try {
				//查询学年学期列表
				jsonV = bean.loadAdminSelection(pageNum, pageSize,SC_XNXQ,xxkskjhmx);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//选课信息
		if("loadGridClassSingle".equalsIgnoreCase(active)){
			String SC_XNXQ = MyTools.StrFiltr(request.getParameter("SC_XNXQ"));
			String xh = MyTools.StrFiltr(request.getParameter("xh"));
			String xzbdm = MyTools.StrFiltr(request.getParameter("xzbdm"));
			String xxkskjhmx = MyTools.StrFiltr(request.getParameter("xxkskjhmx"));
			try {
				//查询学年学期列表
				jsonV = bean.loadGridClassSingle(pageNum, pageSize,SC_XNXQ,xh,xzbdm,xxkskjhmx);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//选课
		if("saveSelection".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			try {
				//查询学年学期列表
				bean.saveSelection(iKeyCode,iUSERCODE,xnxqbm);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//删除选课
		if("delSelection".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));

			try {
				//查询学年学期列表
				bean.delSelection(iKeyCode,iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//检查是否可以选课
		if("checkSelection".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String skzc = MyTools.StrFiltr(request.getParameter("skzc"));
			String sksj = MyTools.StrFiltr(request.getParameter("sksj"));
			String kcdm = MyTools.StrFiltr(request.getParameter("kcdm"));
			try {
				//查询学年学期列表
				bean.checkSelection(iKeyCode,iUSERCODE,xnxqbm,skzc,sksj,kcdm);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//显示个人选课结果
		if("showSelection".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));

			try {
				jsonV = bean.showSelection(pageNum, pageSize,iUSERCODE);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		//查询学生选课结果
		if("showSelectionAll".equalsIgnoreCase(active)){
			String xxbm = URLDecoder.decode(request.getParameter("xxbm"),"utf-8");
			String xh = MyTools.StrFiltr(request.getParameter("xh"));
			String xm = URLDecoder.decode(request.getParameter("xm"),"utf-8");
			try {
				jsonV = bean.showSelectionAll(pageNum, pageSize,xxbm,xh,xm);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");

				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//显示个人选课结果
		if("electiveTime".equalsIgnoreCase(active)){
			try {
				bean.electiveTime();
				jal = JsonUtil.addJsonParams(jal, "XNXQBM", bean.getXNXQBM());
				jal = JsonUtil.addJsonParams(jal, "XNXQMC", bean.getXNXQMC());
				jal = JsonUtil.addJsonParams(jal, "KSXKSJ", bean.getKSXKSJ());
				jal = JsonUtil.addJsonParams(jal, "JSXKSJ", bean.getJSXKSJ());
				jal = JsonUtil.addJsonParams(jal, "KSXKXS", bean.getKSXKXS());
				jal = JsonUtil.addJsonParams(jal, "JSXKXS", bean.getJSXKXS());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//学年学期combobox
		if("xnxqCombobox".equalsIgnoreCase(active)){ 
			try {
				jsonV = bean.xnxqCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//保存设置
		if("saveTime".equalsIgnoreCase(active)){  
			try {
				bean.saveTime();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//读取选修课信息
		if("loadElective".equalsIgnoreCase(active)){  
			String SC_XNXQ = MyTools.StrFiltr(request.getParameter("SC_XNXQ"));
			String SC_KCMC = URLDecoder.decode(request.getParameter("SC_KCMC"),"utf-8");
			try {
				jsonV = bean.loadElective(pageNum, pageSize,SC_XNXQ,SC_KCMC);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//选择选修课志愿
		if("saveApplication".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			String term = MyTools.StrFiltr(request.getParameter("term"));
			String first_APP = MyTools.StrFiltr(request.getParameter("first_APP"));
			String second_APP = MyTools.StrFiltr(request.getParameter("second_APP"));
			String third_APP = MyTools.StrFiltr(request.getParameter("third_APP"));
			try {
				//查询学年学期列表
				bean.saveApplication(iUSERCODE,term,first_APP,second_APP,third_APP);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//学年学期
		if("XNXQElecCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XNXQElecCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//课程名称
		if("XX_KCDMCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XX_KCDMCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//课程类型
		if("XX_KCLXCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XX_KCLXCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//学年学期
		if("XX_XNXQCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XX_XNXQCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//获取选择学期的周数
		if("getweeknum".equalsIgnoreCase(active)){
			String XNXQBM=MyTools.StrFiltr(request.getParameter("XNXQBM"));
			try {
				//查询周次数量
				bean.getWeeknum(XNXQBM);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//上课日期
		if("XX_SKRQCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XX_SKRQCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//上课时间
		if("XX_SKSJCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XX_SKSJCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//上课日期
		if("CP_SKRQCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.CP_SKRQCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
				
		//上课时间
		if("CP_SKSJCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.CP_SKSJCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//读取所有专业
		if("loadGridMajor".equalsIgnoreCase(active)){
			try {
				//查询列表
				String ZYDM=MyTools.StrFiltr(request.getParameter("ZYDM"));
				String ZYMC = URLDecoder.decode(request.getParameter("ZYMC"),"utf-8");
				String majorarr = MyTools.StrFiltr(request.getParameter("majorarr"));
				jsonV = bean.loadGridMajor(pageNum, pageSize,ZYDM,ZYMC,majorarr);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//选修课开班
		if("newElective".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.newElective();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//编辑选修课班级
		if("editElective".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.editElective();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//检查课程是否被使用
		if("checkCourseUsed".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.checkCourseUsed();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//删除选修课班级
		if("deleteElective".equalsIgnoreCase(active)){
			String skjhmxbh=MyTools.StrFiltr(request.getParameter("skjhmxbh"));
			try {
				//查询列表
				bean.deleteElective(skjhmxbh);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询学生
		if("loadGridClass".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadGridClass(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询补选学生
		if("loadGridBX".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.loadGridBX(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学生
		if("loadGridStu".equalsIgnoreCase(active)){
			String stuid = MyTools.StrFiltr(request.getParameter("stuid"));
			String stuname = URLDecoder.decode(request.getParameter("stuname"),"utf-8");
			try {
				//查询学年学期列表
				jsonV = bean.loadGridStu(pageNum, pageSize,stuid,stuname);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学生,按可选专业
		if("loadGridStuClass".equalsIgnoreCase(active)){
			String stuid = MyTools.StrFiltr(request.getParameter("stuid"));
			String stuname = URLDecoder.decode(request.getParameter("stuname"),"utf-8");
			String stuxibu = MyTools.StrFiltr(request.getParameter("stuxibu"));
			String stuzhiy = MyTools.StrFiltr(request.getParameter("stuzhiy"));
			String stuinfo = MyTools.StrFiltr(request.getParameter("stuinfo"));
			try {
						//查询学年学期列表
						jsonV = bean.loadGridStuClass(pageNum, pageSize,stuid,stuname,stuxibu,stuzhiy,stuinfo);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
			}
		}
		
		//查询学生,按可选专业
		if("loadGridnotWalkThrough".equalsIgnoreCase(active)){
			String stuid = MyTools.StrFiltr(request.getParameter("stuid"));
			String stuname = URLDecoder.decode(request.getParameter("stuname"),"utf-8");
			String stuxibu = MyTools.StrFiltr(request.getParameter("stuxibu"));
			try {
				//查询学年学期列表
				jsonV = bean.loadGridnotWalkThrough(pageNum, pageSize, stuid, stuname, stuxibu);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询选修课信息
		if("loadGridstuZY".equalsIgnoreCase(active)){
			String stuid = MyTools.StrFiltr(request.getParameter("stuid"));

			try {
					//查询学年学期列表
					jsonV = bean.loadGridstuZY(pageNum, pageSize,stuid);
					jal = (JSONArray)jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		
		//查询选修课信息
		if("loadGridstuXQ".equalsIgnoreCase(active)){
			String stuid = MyTools.StrFiltr(request.getParameter("stuid"));

			try {
					//查询学年学期列表
					jsonV = bean.loadGridstuXQ(pageNum, pageSize,stuid);
					jal = (JSONArray)jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		
		//查询选修班仅一人选择
		if("loadGridOneStudent".equalsIgnoreCase(active)){
			try {
					//查询学年学期列表
					jsonV = bean.loadGridOneStudent(pageNum, pageSize);
					jal = (JSONArray)jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		
		//删除学生
		if("delStudent".equalsIgnoreCase(active)){
			String stuidarray = MyTools.StrFiltr(request.getParameter("stuidarray"));
			try {
				//查询学年学期列表
				bean.delStudent(pageNum, pageSize,stuidarray);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//学生换班
		if("changeStudent".equalsIgnoreCase(active)){
			String xxkskjhmx = MyTools.StrFiltr(request.getParameter("xxkskjhmx"));//原选修课授课计划编号
			String stuidarray = MyTools.StrFiltr(request.getParameter("stuidarray"));
			String stuxmarray = MyTools.StrFiltr(request.getParameter("stuxmarray"));
			String bmrs = MyTools.StrFiltr(request.getParameter("bmrs"));
			String zrs = MyTools.StrFiltr(request.getParameter("zrs"));
			String skzc = MyTools.StrFiltr(request.getParameter("skzc"));
			String sksj = MyTools.StrFiltr(request.getParameter("sksj"));
			try {
				//查询学年学期列表
				bean.changeStudent(pageNum, pageSize,xxkskjhmx,stuidarray,stuxmarray,bmrs,zrs,skzc,sksj);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//添加所选学生
		if("submitStu".equalsIgnoreCase(active)){
			String stunumarray = MyTools.StrFiltr(request.getParameter("stunumarray"));
			String stunamearray = MyTools.StrFiltr(request.getParameter("stunamearray"));
			String bmrs = MyTools.StrFiltr(request.getParameter("bmrs"));
			String zrs = MyTools.StrFiltr(request.getParameter("zrs"));
			try {
				//查询学年学期列表
				bean.submitStu(pageNum, pageSize,stunumarray,stunamearray,bmrs,zrs);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//添加所选学生
		if("addBXStu".equalsIgnoreCase(active)){
			String addbxstuarray = MyTools.StrFiltr(request.getParameter("addbxstuarray"));
			try {
				bean.addBXStu(pageNum, pageSize,addbxstuarray);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//删除学生
		if("delbxStudent".equalsIgnoreCase(active)){
			String delbxstuarray = MyTools.StrFiltr(request.getParameter("delbxstuarray"));
			try {
				bean.delbxStudent(pageNum, pageSize,delbxstuarray);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取机房信息
		if("loadComputer".equalsIgnoreCase(active)){  
			String SC_XNXQ = MyTools.StrFiltr(request.getParameter("SC_XNXQ"));
			String SC_KCMC = MyTools.StrFiltr(request.getParameter("SC_KCMC"));
			try {
				jsonV = bean.loadComputer(pageNum, pageSize,SC_XNXQ,SC_KCMC);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//机房新建课程考试
		if("newComputer".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.newComputer();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		//编辑机房信息
		if("editComputer".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.editComputer();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		//删除机房信息
		if("deleteComputer".equalsIgnoreCase(active)){
					String skjhmxbh=MyTools.StrFiltr(request.getParameter("skjhmxbh"));
					try {
						//查询列表
						bean.deleteComputer(skjhmxbh);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
		}
		
		
		//考试形式
		if("XX_KSXSCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XX_KSXSCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//考试形式
		if("loadXBDMCombo".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.loadXBDMCombo();
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//第一志愿
		if("first_APPCombobox".equalsIgnoreCase(active)){  
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			String term = MyTools.StrFiltr(request.getParameter("term"));
			String firsel = MyTools.StrFiltr(request.getParameter("firsel"));
			String secsel = MyTools.StrFiltr(request.getParameter("secsel"));
			String thisel = MyTools.StrFiltr(request.getParameter("thisel"));
			try {
				jsonV = bean.first_APPCombobox(iUSERCODE,term,firsel,secsel,thisel);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//第二志愿
		if("second_APPCombobox".equalsIgnoreCase(active)){  
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			String term = MyTools.StrFiltr(request.getParameter("term"));
			String firsel = MyTools.StrFiltr(request.getParameter("firsel"));
			String secsel = MyTools.StrFiltr(request.getParameter("secsel"));
			String thisel = MyTools.StrFiltr(request.getParameter("thisel"));
			try {
				jsonV = bean.second_APPCombobox(iUSERCODE,term,firsel,secsel,thisel);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
				
		//第三志愿
		if("third_APPCombobox".equalsIgnoreCase(active)){  
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			String term = MyTools.StrFiltr(request.getParameter("term"));
			String firsel = MyTools.StrFiltr(request.getParameter("firsel"));
			String secsel = MyTools.StrFiltr(request.getParameter("secsel"));
			String thisel = MyTools.StrFiltr(request.getParameter("thisel"));
			try {
				jsonV = bean.third_APPCombobox(iUSERCODE,term,firsel,secsel,thisel);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//计算学分
		if("calculateCredits".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				bean.calculateCredits(iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "PromptMsg", bean.getMSG2());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询选修课确认情况
		if("checkElectiveSubmit".equalsIgnoreCase(active)){
			try {
				bean.checkElectiveSubmit();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//选修课确认
		if("electiveSubmit".equalsIgnoreCase(active)){
			try {
				bean.electiveSubmit();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//选修课课程信息导出
		if("ExportExcelXXKSubject".equalsIgnoreCase(active)){
			String xnxq=MyTools.StrFiltr(request.getParameter("SC_XNXQ"));
			String xnxqname = MyTools.StrFiltr(request.getParameter("SC_XNXQMC"));
			try {
				String filePath = bean.ExportExcelXXKSubject(xnxq,xnxqname);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//选修课学生名单导出
		if("ExportExcelStudianming".equalsIgnoreCase(active)){
			String xnxq=MyTools.StrFiltr(request.getParameter("SC_XNXQ"));
			String xnxqname = MyTools.StrFiltr(request.getParameter("SC_XNXQMC"));
			try {
				String filePath = bean.ExportExcelStudianming(xnxq,xnxqname);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//选课信息
		if("querySourcebuman".equalsIgnoreCase(active)){
			String nj=MyTools.StrFiltr(request.getParameter("nj"));
			String xh=URLDecoder.decode(MyTools.StrFiltr(request.getParameter("xh")),"UTF-8");
			String xm=URLDecoder.decode(MyTools.StrFiltr(request.getParameter("xm")),"UTF-8");
			String banji=URLDecoder.decode(MyTools.StrFiltr(request.getParameter("banji")),"UTF-8");
			try {
				//查询学年学期列表
				jsonV = bean.querySourcebuman(pageNum, pageSize,nj,xh,xm,banji);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//年级
		if("XF_NJCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XF_NJCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//系部
		if("XIBUCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.XIBUCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//系部
		if("wtXIBUCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.wtXIBUCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//志愿
		if("ZHIYCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.ZHIYCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//显示结果
		if("INFOCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.INFOCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//显示个人选课结果
		if("showSelectiongeren".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));

			try {
				jsonV = bean.showSelectiongeren(pageNum, pageSize,iUSERCODE);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");

				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//选修课学分不足学生名单导出
		if("ExportExcelStuXFBM".equalsIgnoreCase(active)){
			String nj=MyTools.StrFiltr(request.getParameter("nj"));
			String xh=URLDecoder.decode(MyTools.StrFiltr(request.getParameter("xh")),"UTF-8");
			String xm=URLDecoder.decode(MyTools.StrFiltr(request.getParameter("xm")),"UTF-8");
			String banji=URLDecoder.decode(MyTools.StrFiltr(request.getParameter("banji")),"UTF-8");
			try {
				String filePath = bean.ExportExcelStuXFBM(nj,xh,xm,banji);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//删除留级和不在校学生选课信息
		if("delelecSubmit".equalsIgnoreCase(active)){  
			try {
				//查询列表
				bean.delelecSubmit();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//导出课程设置表
		if("exportExcel".equalsIgnoreCase(active)){  
			try {
				//接收表单学年学期
				String ic_xnxq = MyTools.StrFiltr(request.getParameter("ic_xnxq"));
				String filePath = bean.exportExcel(ic_xnxq);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("openTeacher".equalsIgnoreCase(active)){
			String teaId = MyTools.StrFiltr(request.getParameter("teaId"));
			String teaName = MyTools.StrFiltr(request.getParameter("teaName"));
			String teaLevel = MyTools.StrFiltr(request.getParameter("teaLevel"));
			String teacharr = MyTools.StrFiltr(request.getParameter("teacharr"));
			try {
				try {
					jsonV=bean.openTeacher(pageNum,pageSize,teaId,teaName,teaLevel,teacharr);
				} catch (WrongSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				
				TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("openRoom".equalsIgnoreCase(active)){
			String seltype=MyTools.StrFiltr(request.getParameter("seltype"));
			String roomarr = MyTools.StrFiltr(request.getParameter("roomarr"));
			try {
				try {
					jsonV=bean.openRoom(seltype,roomarr);
				} catch (WrongSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
			
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, CourseSetBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setKCDM(MyTools.StrFiltr(request.getParameter("KCDM"))); //课程代码
		bean.setKCMC(MyTools.StrFiltr(request.getParameter("KCMC"))); //课程名称
		bean.setKCLX(MyTools.StrFiltr(request.getParameter("KCLX"))); //课程类型
		bean.setZYDM(MyTools.StrFiltr(request.getParameter("ZYDM"))); //专业代码
		bean.setZYMC(MyTools.StrFiltr(request.getParameter("ZYMC"))); //专业名称
		bean.setXBBH(MyTools.StrFiltr(request.getParameter("XBBH"))); //系部代码
		bean.setXBMC(MyTools.StrFiltr(request.getParameter("XBMC"))); //系部名称
		bean.setKSXS(MyTools.StrFiltr(request.getParameter("KSXS"))); //考试形式
		
		bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM"))); //学年学期编码
		bean.setXNXQMC(MyTools.StrFiltr(request.getParameter("XNXQMC"))); //学年学期名称
		bean.setKSXKSJ(MyTools.StrFiltr(request.getParameter("KSXKSJ"))); //开始选课日期
		bean.setJSXKSJ(MyTools.StrFiltr(request.getParameter("JSXKSJ"))); //结束选课日期
		bean.setKSXKXS(MyTools.StrFiltr(request.getParameter("KSXKXS"))); //开始选课时间
		bean.setJSXKXS(MyTools.StrFiltr(request.getParameter("JSXKXS"))); //结束选课时间
		
		bean.setXX_KCDM(MyTools.StrFiltr(request.getParameter("XX_KCDM")));//课程代码
		bean.setXX_KCMC(MyTools.StrFiltr(request.getParameter("XX_KCMC")));//课程名称
		bean.setXX_KCLX(MyTools.StrFiltr(request.getParameter("XX_KCLX")));//课程类型
		bean.setXX_XNXQ(MyTools.StrFiltr(request.getParameter("XX_XNXQ")));//学年学期
		bean.setXX_BJRS(MyTools.StrFiltr(request.getParameter("XX_BJRS")));//班级人数
		bean.setXX_BJMC(MyTools.StrFiltr(request.getParameter("XX_BJMC")));//班级名称
		bean.setXX_SKRQ(MyTools.StrFiltr(request.getParameter("XX_SKRQ")));//上课日期
		bean.setXX_SKSJ(MyTools.StrFiltr(request.getParameter("XX_SKSJ")));//上课时间
		bean.setXX_XUEF(MyTools.StrFiltr(request.getParameter("XX_XUEF")));//学分
		bean.setXX_ZOKS(MyTools.StrFiltr(request.getParameter("XX_ZOKS")));//总课时
		bean.setXX_JSBH(MyTools.StrFiltr(request.getParameter("XX_JSBH")));//教师编号
		bean.setXX_SKJS(MyTools.StrFiltr(request.getParameter("XX_SKJS")));//教师姓名
		bean.setXX_CDYQ(MyTools.StrFiltr(request.getParameter("XX_CDYQ")));//场地要求
		bean.setXX_CDMC(MyTools.StrFiltr(request.getParameter("XX_CDMC")));//场地名称
		bean.setXX_SKZC(MyTools.StrFiltr(request.getParameter("XX_SKZC")));//授课周次
		bean.setXX_SKZCXQ(MyTools.StrFiltr(request.getParameter("XX_SKZCXQ")));//授课周次详情
		bean.setXX_ZYBH(MyTools.StrFiltr(request.getParameter("XX_ZYBH")));//可报名专业编号
		bean.setXX_ZYNA(MyTools.StrFiltr(request.getParameter("XX_ZYNA")));//可报名专业名称
		bean.setXX_SJXL(MyTools.StrFiltr(request.getParameter("XX_SJXL")));//时间序列
		bean.setXX_XXKZBBH(MyTools.StrFiltr(request.getParameter("XX_XXKZBBH")));//选修课主表编号
		bean.setXX_XXKMXBH(MyTools.StrFiltr(request.getParameter("XX_XXKMXBH")));//选修课明细编号
		bean.setXX_KSXS(MyTools.StrFiltr(request.getParameter("XX_KSXS")));//考试形式
		bean.setXX_WEEK(MyTools.StrFiltr(request.getParameter("XX_WEEK")));//学期总周数
	}
}