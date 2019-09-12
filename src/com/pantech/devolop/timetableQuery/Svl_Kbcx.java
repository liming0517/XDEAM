package com.pantech.devolop.timetableQuery;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import com.pantech.base.common.tools.TraceLog;
import com.zhuozhengsoft.pageoffice.PageOfficeLink;

public class Svl_Kbcx extends HttpServlet {

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
		KbcxBean bean = new KbcxBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			String type = MyTools.StrFiltr(request.getParameter("type"));
			try {
				//查询课程表列表
				if("teaKCB".equalsIgnoreCase(type) || "courseKCB".equalsIgnoreCase(type) || "classKCB".equalsIgnoreCase(type)){
					jsonV = bean.queKcbList(pageNum, pageSize, "", "");
				}else{
					if("allClassKCB".equalsIgnoreCase(type)){
						jsonV = bean.queAllClassKcbList(pageNum, pageSize, "", "", "");
					}else{
						jsonV = bean.queAllTeaKcbList(pageNum, pageSize, "", "", "");
					}
				}
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				if("allClassKCB".equalsIgnoreCase(type)){
//					//查询专业下拉框
//					jsonV = bean.loadMajorCombo();
//					jal = JsonUtil.addJsonParams(jal, "zydmData", ((JSONArray)jsonV.get(2)).toString());
					//查询系部下拉框
					jsonV = bean.loadDepatmentCombo();
					jal = JsonUtil.addJsonParams(jal, "xbdmData", ((JSONArray)jsonV.get(2)).toString());
				}
				if("allTeaKCB".equalsIgnoreCase(type)){
					//查询角色层级下拉框
//					jsonV = bean.loadLevelCombo();
//					jal = JsonUtil.addJsonParams(jal, "jsdmData", ((JSONArray)jsonV.get(2)).toString());
				}
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学年学期课程表列表
		if("queSemesterList".equalsIgnoreCase(active)){
			String PK_XNXQMC_CX = MyTools.StrFiltr(request.getParameter("PK_XNXQMC_CX"));
			String PK_JXXZ_CX = MyTools.StrFiltr(request.getParameter("PK_JXXZ_CX"));
			String PK_ZY_CX = MyTools.StrFiltr(request.getParameter("PK_ZY_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queSemesterList(pageNum, pageSize, PK_XNXQMC_CX, PK_JXXZ_CX, PK_ZY_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询班级树
		if("queClassTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			String level = MyTools.StrFiltr(request.getParameter("level"));
			try {
				jsonV = bean.queClassTree(level, parentCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}

		//查询班级课程表
		if("loadClassKcb".equalsIgnoreCase(active)){
			String week = MyTools.StrFiltr(request.getParameter("skzc"));
			String nodeType = MyTools.StrFiltr(request.getParameter("nodeType"));
			try {
				jsonV = bean.loadClassKcb(week,nodeType);
				jal = JsonUtil.addJsonParams(jal, "kcb", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "bz", MyTools.StrFiltr(jsonV.get(1)));
				jsonV = bean.loadHbInfo();
				jal = JsonUtil.addJsonParams(jal, "hbSet", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "hbInfo", MyTools.StrFiltr(jsonV.get(1)));
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询班级树
		if("queStudentTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			String level = MyTools.StrFiltr(request.getParameter("level"));
			try {
				jsonV = bean.queStudentTree(level, parentCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询当前学年设置的每周天数和每天节数
		if("initBlankKCB".equalsIgnoreCase(active)){
			try {
				String result = bean.loadTeaRemark();
				jal = JsonUtil.addJsonParams(jal, "remark", result);
				
				jsonV = bean.loadBlankKcbInfo();
				if(jsonV!=null && jsonV.size()>0){
					jal = JsonUtil.addJsonParams(jal, "xqzc", MyTools.StrFiltr(jsonV.get(0)));
					jal = JsonUtil.addJsonParams(jal, "mzts", MyTools.StrFiltr(jsonV.get(1)));
					jal = JsonUtil.addJsonParams(jal, "sw", MyTools.StrFiltr(jsonV.get(2)));
					jal = JsonUtil.addJsonParams(jal, "zw", MyTools.StrFiltr(jsonV.get(3)));
					jal = JsonUtil.addJsonParams(jal, "xw", MyTools.StrFiltr(jsonV.get(4)));
					jal = JsonUtil.addJsonParams(jal, "ws", MyTools.StrFiltr(jsonV.get(5)));
					jal = JsonUtil.addJsonParams(jal, "jcsj", MyTools.StrFiltr(jsonV.get(6)));
				}else{
					jal = JsonUtil.addJsonParams(jal, "xqzc", "20");
					jal = JsonUtil.addJsonParams(jal, "mzts", "5");
					jal = JsonUtil.addJsonParams(jal, "sw", "4");
					jal = JsonUtil.addJsonParams(jal, "zw", "0");
					jal = JsonUtil.addJsonParams(jal, "xw", "3");
					jal = JsonUtil.addJsonParams(jal, "ws", "0");
					jal = JsonUtil.addJsonParams(jal, "jcsj", "");
				}
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询学年学期下拉框
		if("loadXNXQCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadXNXQCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//查询学生允许查看的学年学期下拉框
		if("loadStuXnxqCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadStuXnxqCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//查询周次下拉框
		if("loadWeekCombo".equalsIgnoreCase(active)){
			try {
				String result = bean.loadWeekCombo();
				response.getWriter().write(result);
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		//查询课程表列表
		if("queKcbList".equalsIgnoreCase(active)){
			String PK_XNXQMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PK_XNXQMC_CX")), "UTF-8");
			String PK_JXXZ_CX = MyTools.StrFiltr(request.getParameter("PK_JXXZ_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queKcbList(pageNum, pageSize, PK_XNXQMC_CX, PK_JXXZ_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询全校班级课程表列表
		if("queAllClassKcbList".equalsIgnoreCase(active)){
			String PK_XNXQMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PK_XNXQMC_CX")), "UTF-8");
			String PK_JXXZ_CX = MyTools.StrFiltr(request.getParameter("PK_JXXZ_CX"));
			String PK_ZY_CX = MyTools.StrFiltr(request.getParameter("PK_ZY_CX"));
			try {
				jsonV = bean.queAllClassKcbList(pageNum, pageSize, PK_XNXQMC_CX, PK_JXXZ_CX, PK_ZY_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询全校教师课程表列表
		if("queAllTeaKcbList".equalsIgnoreCase(active)){
			String PK_XNXQMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PK_XNXQMC_CX")), "UTF-8");
			String PK_JXXZ_CX = MyTools.StrFiltr(request.getParameter("PK_JXXZ_CX"));
			String PK_JS_CX = MyTools.StrFiltr(request.getParameter("PK_JS_CX"));
			try {
				jsonV = bean.queAllTeaKcbList(pageNum, pageSize, PK_XNXQMC_CX, PK_JXXZ_CX, PK_JS_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询教师树
		if("queTeaTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			String level = MyTools.StrFiltr(request.getParameter("level"));
			bean.setPK_SKJSBH(URLDecoder.decode(bean.getPK_SKJSBH(), "UTF-8"));
			
			try {
				jsonV = bean.queTeaTree(level, parentCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询教师课程表
		if("loadTeaKcb".equalsIgnoreCase(active)){
			String week = MyTools.StrFiltr(request.getParameter("skzc"));
			try {
				String result = bean.loadTeaKcb(week);
				jal = JsonUtil.addJsonParams(jal, "kcb", result);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询课程树
		if("queCourseTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			String level = MyTools.StrFiltr(request.getParameter("level"));
			bean.setPK_SKJSBH(URLDecoder.decode(bean.getPK_SKJSBH(), "UTF-8"));
			
			try {
				jsonV = bean.queCourseTree(level, parentCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询课程课程表
		if("loadCourseKcb".equalsIgnoreCase(active)){
			String courseCode = MyTools.StrFiltr(request.getParameter("courseCode"));
			String week = MyTools.StrFiltr(request.getParameter("skzc"));
			
			try {
				String result = bean.loadCourseKcb(courseCode, week);
				jal = JsonUtil.addJsonParams(jal, "kcb", result);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询班级课程表总表
		if("loadAllClassKcb".equalsIgnoreCase(active)){
			try {
				String jsonStr = bean.loadAllClassKcb();
				response.getWriter().write(jsonStr);
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询教师课程表总表
		if("loadAllTeaKcb".equalsIgnoreCase(active)){
			try {
				String jsonStr = bean.loadAllTeaKcb();
				response.getWriter().write(jsonStr);
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//保存教师课表备注
		if("saveRemark".equalsIgnoreCase(active)){
			String startDate = MyTools.StrFiltr(request.getParameter("startDate"));
			String endDate = MyTools.StrFiltr(request.getParameter("endDate"));
			String weekNum_1 = MyTools.StrFiltr(request.getParameter("weekNum_1"));
			String contactWay = MyTools.StrFiltr(request.getParameter("contactWay"));
			String weekNum_2 = MyTools.StrFiltr(request.getParameter("weekNum_2"));
			String exam_1 = MyTools.StrFiltr(request.getParameter("exam_1"));
			String weekNum_3 = MyTools.StrFiltr(request.getParameter("weekNum_3"));
			String exam_2 = MyTools.StrFiltr(request.getParameter("exam_2"));
			String weekNum_4 = MyTools.StrFiltr(request.getParameter("weekNum_4"));
			String exam_3 = MyTools.StrFiltr(request.getParameter("exam_3"));
			String year = MyTools.StrFiltr(request.getParameter("yearMonth"));
			
			try {
				bean.saveRemark(startDate, endDate, weekNum_1, contactWay, weekNum_2, exam_1, weekNum_3, exam_2, weekNum_4, exam_3, year);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		if("loadClassPageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String curXnxq = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String code = MyTools.StrFiltr(request.getParameter("code"));
			String timetableName = URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8");
			
			try {
				String linkStr = "";
				if("classKcb".equalsIgnoreCase(exportType)){
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType + 
							"&xnxqbm=" + curXnxq + "&code=" + code + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				}else{
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType + "&userCode=" + bean.getUSERCODE() + "&sAuth=" + bean.getAuth() +
							"&xnxqbm=" + curXnxq + "&code=" + code + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				}
				
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("loadTeaPageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String curXnxq = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String code = MyTools.StrFiltr(request.getParameter("code"));
			String timetableName = URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8");
			
			try {
				String linkStr = "";
				if("teaKcb".equalsIgnoreCase(exportType)){
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType + 
							"&xnxqbm=" + curXnxq + "&code=" + code + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				}else{
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType + "&userCode=" + bean.getUSERCODE() + "&sAuth=" + bean.getAuth() +
							"&xnxqbm=" + curXnxq + "&code=" + code + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				}
				
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("loadCoursePageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String curXnxq = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String code = MyTools.StrFiltr(request.getParameter("code"));
			String timetableName = URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8");
			
			try {
				String linkStr = "";
				if("courseKcb".equalsIgnoreCase(exportType)){
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType + "&userCode=" + bean.getUSERCODE() + "&sAuth=" + bean.getAuth() + 
							"&xnxqbm=" + curXnxq + "&code=" + code + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				}else{
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType + "&userCode=" + bean.getUSERCODE() + "&sAuth=" + bean.getAuth() +
							"&xnxqbm=" + curXnxq + "&code=" + code + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				}
				
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("loadAllClassPageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String curXnxq = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String code = MyTools.StrFiltr(request.getParameter("code"));
			String timetableName = URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8");
			
			try {
				String linkStr = "";
				linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType +
						"&xnxqbm=" + curXnxq + "&code=" + code + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("loadAllTeaPageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String curXnxq = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String timetableName = URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8");
			
			try {
				String linkStr = "";
				linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportExcel.jsp?exportType=" + exportType + "&userCode=" + bean.getUSERCODE() + "&sAuth=" + bean.getAuth() +
						"&xnxqbm=" + curXnxq + "&timetableName=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(timetableName, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1000px;height=800px;");
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("loadTablePageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String school = MyTools.StrFiltr(request.getParameter("school"));
			//String house = MyTools.StrFiltr(request.getParameter("house"));
			//String floor = MyTools.StrFiltr(request.getParameter("floor"));
			String week = MyTools.StrFiltr(request.getParameter("week"));
			String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
			String jxxz = MyTools.StrFiltr(request.getParameter("jxxz"));
			String titleinfo = URLDecoder.decode(request.getParameter("titleinfo"),"utf-8");

			try {
				String linkStr = "";
				if("classTable".equalsIgnoreCase(exportType)){
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportClassTable.jsp?exportType=" + exportType + 
							"&school=" + school + "&week=" + week + "&xnxq=" + xnxq+ "&jxxz=" + jxxz + "&titleinfo=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(titleinfo, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1200px;height=700px;");
				}else{
					linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportClassTable.jsp?exportType=" + exportType + "&userCode=" + bean.getUSERCODE() + "&sAuth=" + bean.getAuth() +
							"&school=" + school + "&week=" + week + "&xnxq=" + xnxq+ "&jxxz=" + jxxz + "&titleinfo=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(titleinfo, "UTF-8"), "UTF-8"), "UTF-8") + "","width=1200px;height=700px;");
				}
				
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("loadWorkPageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String XNXQ = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("XNXQ"),"utf-8"));
			//String KCJS = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("KCJS"),"utf-8"));
			//String TEAID = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("TEAID"),"utf-8"));
			//String TEANAME = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("TEANAME"),"utf-8"));
			
			String TEAID = URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("TEAID")), "utf-8");
			String TEANAME = URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("TEANAME")), "utf-8");
			//String browerType = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("browerType"),"utf-8"));
			
			try {
				String linkStr = "";
				//if(browerType.equals("ie")){
					if("workLoad".equalsIgnoreCase(exportType)){
						linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportWorkLoad.jsp?exportType=" + exportType + 
								"&XNXQ=" + XNXQ + "&TEAID=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(TEAID, "UTF-8"), "UTF-8"), "UTF-8") + "&TEANAME=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(TEANAME, "UTF-8"), "UTF-8"), "UTF-8") +"","width=1200px;height=700px;");
					}else if("teachSituation".equalsIgnoreCase(exportType)){
						linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportWorkLoad.jsp?exportType=" + exportType +
								"&XNXQ=" + XNXQ + "&TEAID=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(TEAID, "UTF-8"), "UTF-8"), "UTF-8") + "&TEANAME=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(TEANAME, "UTF-8"), "UTF-8"), "UTF-8") +"","width=1200px;height=700px;");
					}else if("teachSubsidy".equalsIgnoreCase(exportType)){
						linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportWorkLoad.jsp?exportType=" + exportType +
								"&XNXQ=" + XNXQ + "&TEAID=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(TEAID, "UTF-8"), "UTF-8"), "UTF-8") + "&TEANAME=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(TEANAME, "UTF-8"), "UTF-8"), "UTF-8") +"","width=1200px;height=700px;");
					}
				//}
//				else{
//					if("workLoad".equalsIgnoreCase(exportType)){
//						linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportWorkLoad.jsp?exportType=" + exportType + 
//								"&XNXQ=" + XNXQ + "&TEAID=" + URLEncoder.encode(URLEncoder.encode(TEAID, "UTF-8"), "UTF-8") + "&TEANAME=" + URLEncoder.encode(URLEncoder.encode(TEANAME, "UTF-8"), "UTF-8") +"","width=1200px;height=700px;");
//					}else if("teachSituation".equalsIgnoreCase(exportType)){
//						linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportWorkLoad.jsp?exportType=" + exportType +
//								"&XNXQ=" + XNXQ + "&TEAID=" + URLEncoder.encode(URLEncoder.encode(TEAID, "UTF-8"), "UTF-8") + "&TEANAME=" + URLEncoder.encode(URLEncoder.encode(TEANAME, "UTF-8"), "UTF-8") +"","width=1200px;height=700px;");
//					}else if("teachSubsidy".equalsIgnoreCase(exportType)){
//						linkStr = PageOfficeLink.openWindow(request, "form/timetableQuery/exportWorkLoad.jsp?exportType=" + exportType +
//								"&XNXQ=" + XNXQ + "&TEAID=" + URLEncoder.encode(URLEncoder.encode(TEAID, "UTF-8"), "UTF-8") + "&TEANAME=" + URLEncoder.encode(URLEncoder.encode(TEANAME, "UTF-8"), "UTF-8") +"","width=1200px;height=700px;");
//					}
//				}
				
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		 if ("ExportExceldaochu".equalsIgnoreCase(active))
		    {
		      String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
		      String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
		      String parentId = MyTools.StrFiltr(request.getParameter("parentId"));
		      String code = MyTools.StrFiltr(request.getParameter("code"));
		      String timetableName = URLDecoder.decode(URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8"), "utf-8");
		      String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
		      String userCode = MyTools.StrFiltr(request.getParameter("userCode"));
		      try
		      {
		        if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)) || 
		          ("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType)) || 
		          ("courseKcb".equalsIgnoreCase(exportType)) || ("courseKcbAll".equalsIgnoreCase(exportType)) || 
		          ("studentKcb".equalsIgnoreCase(exportType)))
		        {
		          String filePath = bean.ExportExceldaochu(sAuth, userCode, xnxqbm, exportType, parentId, code, timetableName);
		          jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
		          jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
		          response.getWriter().write(jal.toString());
		        }else{
					String filePath = bean.ExportExceldaochuquanxiao(sAuth, userCode, xnxqbm, exportType, code, timetableName);
					jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
					jal = JsonUtil.addJsonParams(jal, "filePath",filePath);
					response.getWriter().write(jal.toString());
				}
		      }
		      catch (Exception e)
		      {
		        e.printStackTrace();
		      }
		    }
		 
		//2017/11/24翟旭超加
		if("ExportExceldaochualltea".equalsIgnoreCase(active)){
				String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			    String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			    String parentId = MyTools.StrFiltr(request.getParameter("parentId"));
			    String code = MyTools.StrFiltr(request.getParameter("code"));
			    String timetableName = URLDecoder.decode(URLDecoder.decode(StringEscapeUtils.escapeJava(request.getParameter("timetableName")), "utf-8"), "utf-8");
			    String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
			    String userCode = MyTools.StrFiltr(request.getParameter("userCode"));
				
			    try {
					String filePath = bean.ExportExceldaochualltea(sAuth, userCode, xnxqbm, exportType, code, timetableName);
					jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
					jal = JsonUtil.addJsonParams(jal, "filePath",filePath);
					response.getWriter().write(jal.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param KbcxBean
    */
	private void getFormData(HttpServletRequest request,KbcxBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAuth(MyTools.StrFiltr(request.getParameter("sAuth")));//用户权限
		bean.setPK_KCBZBBH(MyTools.StrFiltr(request.getParameter("PK_KCBZBBH"))); //课程表主表编号
		bean.setPK_XNXQBM(MyTools.StrFiltr(request.getParameter("PK_XNXQBM"))); //学年学期编码
		bean.setPK_XZBDM(MyTools.StrFiltr(request.getParameter("PK_XZBDM"))); //行政班代码
		bean.setPK_ZYDM(MyTools.StrFiltr(request.getParameter("PK_ZYDM")));//专业代码
		bean.setPK_KCBMXBH(MyTools.StrFiltr(request.getParameter("PK_KCBMXBH"))); //课程表明细编号
		bean.setPK_KCBZBBH(MyTools.StrFiltr(request.getParameter("PK_KCBZBBH"))); //课程表主表编号
		bean.setPK_SKJSBH(MyTools.StrFiltr(request.getParameter("PK_SKJSBH"))); //授课教师编号
		bean.setPK_SJXL(MyTools.StrFiltr(request.getParameter("PK_SJXL"))); //时间序列
		bean.setPK_LJXGBH(MyTools.StrFiltr(request.getParameter("PK_LJXGBH"))); //连节相关编号
		bean.setPK_SKJHMXBH(MyTools.StrFiltr(request.getParameter("PK_SKJHMXBH"))); //授课计划明细编号
		bean.setPK_BZ(MyTools.StrFiltr(request.getParameter("PK_BZ"))); //备注
		bean.setPK_ZT(MyTools.StrFiltr(request.getParameter("PK_ZT"))); //状态
		bean.setPK_CJBH(MyTools.StrFiltr(request.getParameter("PK_CJBH")));//层级编号
	}
}
