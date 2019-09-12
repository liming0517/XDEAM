package com.pantech.devolop.courseManage;
/**
编制日期：2015.05.27
创建人：yeq
模块名称：M2.1排课设置
说明:
	 
功能索引:
	1-查询
	2-自动排课
**/
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class Svl_Pksz extends HttpServlet {
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurredqueSiteList
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
		PkszBean bean = new PkszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询课程表列表
				jsonV = bean.queSemesterList(pageNum, pageSize, "", "", "", "");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				jal = JsonUtil.addJsonParams(jal, "existInfo", MyTools.StrFiltr(jsonV.get(3)));
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学年下拉框
				jsonV = bean.loadYearCombo("");
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询专业下拉框
				jsonV = bean.loadMajorCombo();
				jal = JsonUtil.addJsonParams(jal, "zydmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadXBCombo();
				jal = JsonUtil.addJsonParams(jal, "xbData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教师层级下拉框
				jsonV = bean.loadTeaLevelCombo();
				jal = JsonUtil.addJsonParams(jal, "teaLevel", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教室类型下拉框
				jsonV = bean.loadSiteTypeCombo();
				jal = JsonUtil.addJsonParams(jal, "siteType", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学期下拉框数据
		if("queXqCombo".equalsIgnoreCase(active)){
			String xn = MyTools.StrFiltr(request.getParameter("PK_XN"));
			try {
				jsonV = bean.queXqCombo(xn, "");
				jal = (JSONArray) jsonV.get(2);
				JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询教学性质下拉框数据
		if("queJxxzCombo".equalsIgnoreCase(active)){
			String xn = MyTools.StrFiltr(request.getParameter("PK_XN"));
			String xq = MyTools.StrFiltr(request.getParameter("PK_XQ"));
			try {
				jsonV = bean.queJxxzCombo(xn, xq, "");
				jal = (JSONArray) jsonV.get(2);
				JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询学年学期课程表列表
		if("queSemesterList".equalsIgnoreCase(active)){
			String PK_XNXQMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("PK_XNXQMC_CX")), "UTF-8");
			String PK_JXXZ_CX = MyTools.StrFiltr(request.getParameter("PK_JXXZ_CX"));
			String PK_ZY_CX = MyTools.StrFiltr(request.getParameter("PK_ZY_CX"));
			String PK_TJZT_CX = MyTools.StrFiltr(request.getParameter("PK_TJZT_CX"));
			try {
				//查询学年学期列表
				jsonV = bean.queSemesterList(pageNum, pageSize, PK_XNXQMC_CX, PK_JXXZ_CX, PK_ZY_CX, PK_TJZT_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				jal = JsonUtil.addJsonParams(jal, "existInfo", MyTools.StrFiltr(jsonV.get(3)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//检查教室冲突
		if("checkCourseArrange".equalsIgnoreCase(active)){
			//String type = MyTools.StrFiltr(request.getParameter("type"));//排课类型
			//String tsgzFlag = MyTools.StrFiltr(request.getParameter("tsgzFlag"));//特殊规则验证标识
			//String checkTeaFlag = MyTools.StrFiltr(request.getParameter("checkTeaFlag"));//禁排验证标识
			try {
				bean.checkCourseArrange();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (Exception e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//自动排课
		if("autoCourseArrange".equalsIgnoreCase(active)){
			String type = MyTools.StrFiltr(request.getParameter("type"));//排课类型
			String tsgzFlag = MyTools.StrFiltr(request.getParameter("tsgzFlag"));//特殊规则验证标识
			String checkTeaFlag = MyTools.StrFiltr(request.getParameter("checkTeaFlag"));//禁排验证标识
			try {
				String result = bean.autoCourseArrange(type, tsgzFlag, checkTeaFlag);
				response.getWriter().write(result);
			} catch (Exception e) {
				String result = "{\"MSG\":\"自动排课发生错误，请检查规则管理中课程设置后重试！\"}";
				response.getWriter().write(result);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取进度条
		if("loadProgressBar".equalsIgnoreCase(active)){
			HttpSession session = request.getSession();
			String progressStr = MyTools.StrFiltr(session.getAttribute("pkjd"));
			String tipsStr = MyTools.StrFiltr(session.getAttribute("pkTips"));
			String classStr = MyTools.StrFiltr(session.getAttribute("pkClass"));
			String courseStr = MyTools.StrFiltr(session.getAttribute("pkCourse"));
			String firstEnter = MyTools.StrFiltr(request.getParameter("firstEnter"));
			if("yes".equalsIgnoreCase(firstEnter) && progressStr==null){
				session.setAttribute("pkjd", "0");
				tipsStr = "正在初始化课程表信息...";
				classStr = "&nbsp;";
				courseStr = "&nbsp;";
				session.setAttribute("pkTips", tipsStr);
				session.setAttribute("pkClass", classStr);
				session.setAttribute("pkCourse", courseStr);
				progressStr = "0";
			}
			
			jal = JsonUtil.addJsonParams(jal, "progress", progressStr);
			jal = JsonUtil.addJsonParams(jal, "tips", tipsStr);
			jal = JsonUtil.addJsonParams(jal, "className", classStr);
			jal = JsonUtil.addJsonParams(jal, "courseName", courseStr);
			response.getWriter().write(jal.toString());
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
		
		//查询当前学年设置的每周天数和每天节数
		if("initBlankKCB".equalsIgnoreCase(active)){
			try {
				jsonV = bean.initBlankKcb();
				if(jsonV!=null && jsonV.size()>0){
					Vector vec = (Vector)jsonV.get(0);
					if(vec!=null && vec.size()>0){
						jal = JsonUtil.addJsonParams(jal, "mzts", MyTools.StrFiltr(vec.get(0)));
						jal = JsonUtil.addJsonParams(jal, "sw", MyTools.StrFiltr(vec.get(1)));
						jal = JsonUtil.addJsonParams(jal, "zw", MyTools.StrFiltr(vec.get(2)));
						jal = JsonUtil.addJsonParams(jal, "xw", MyTools.StrFiltr(vec.get(3)));
						jal = JsonUtil.addJsonParams(jal, "ws", MyTools.StrFiltr(vec.get(4)));
						jal = JsonUtil.addJsonParams(jal, "jcsj", MyTools.StrFiltr(vec.get(5)));
						jal = JsonUtil.addJsonParams(jal, "sfgq", MyTools.StrFiltr(vec.get(6)));
					}else{
						jal = JsonUtil.addJsonParams(jal, "mzts", "5");
						jal = JsonUtil.addJsonParams(jal, "sw", "4");
						jal = JsonUtil.addJsonParams(jal, "zw", "0");
						jal = JsonUtil.addJsonParams(jal, "xw", "3");
						jal = JsonUtil.addJsonParams(jal, "ws", "0");
						jal = JsonUtil.addJsonParams(jal, "jcsj", "");
						jal = JsonUtil.addJsonParams(jal, "sfgq", "1");
					}
					//获取当前专业班级最大课程数
					vec = (Vector)jsonV.get(1);
					if(vec!=null && vec.size()>0){
						jal = JsonUtil.addJsonParams(jal, "maxNum", MyTools.StrFiltr(vec.get(0)));
					}else{
						jal = JsonUtil.addJsonParams(jal, "maxNum", "1");
					}
					//获取学期周次
					jal = JsonUtil.addJsonParams(jal, "xqzc", MyTools.StrFiltr(jsonV.get(2)));
				}
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询班级课程表
		if("loadClassKcb".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadClassKcb();
				jal = JsonUtil.addJsonParams(jal, "kcb", MyTools.StrFiltr(((Vector)jsonV.get(0)).get(2)));
				jal = JsonUtil.addJsonParams(jal, "wpkc", MyTools.StrFiltr(((Vector)jsonV.get(1)).get(2)));
				jal = JsonUtil.addJsonParams(jal, "bz", MyTools.StrFiltr(jsonV.get(2)));
				jal = JsonUtil.addJsonParams(jal, "stuNum", MyTools.StrFiltr(jsonV.get(3)));
				jal = JsonUtil.addJsonParams(jal, "tkOrder", MyTools.StrFiltr(jsonV.get(4)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询教师和教室已用时间信息
		if("queUsedOrderInfo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queUsedOrderInfo("all", "");
				jal = JsonUtil.addJsonParams(jal, "teaUsedOrder", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "siteUsedOrder", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "teaKXB", MyTools.StrFiltr(jsonV.get(2)));
				
				jsonV = bean.queHbInfo();
				jal = JsonUtil.addJsonParams(jal, "hbSet", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "hbInfo", MyTools.StrFiltr(jsonV.get(1)));
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询添加课程信息
		if("queAddCourseInfo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queAddCourseInfo();
				jal = JsonUtil.addJsonParams(jal, "addCourseInfo", MyTools.StrFiltr(jsonV.get(0)));
				
				jsonV = bean.queUsedOrderInfo("all", "");
				jal = JsonUtil.addJsonParams(jal, "teaUsedOrder", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "siteUsedOrder", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "teaKXB", MyTools.StrFiltr(jsonV.get(2)));
				
				jsonV = bean.queHbInfo();
				jal = JsonUtil.addJsonParams(jal, "hbSet", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "hbInfo", MyTools.StrFiltr(jsonV.get(1)));
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//保存课程表备注
		if("saveRemark".equalsIgnoreCase(active)){
			try {
				bean.saveRemark();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//更新班级课程表
		if("updateKCB".equalsIgnoreCase(active)){
			String[] kcbInfo = MyTools.StrFiltr(request.getParameter("kcbInfo")).split(",", -1);
			String[] wpkcInfo = MyTools.StrFiltr(request.getParameter("wpkcInfo")).split(",", -1);
			
			try {
				bean.updateKCB(kcbInfo, wpkcInfo);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询排课过期的学期
		if("queOverXqCombo".equalsIgnoreCase(active)){
			String xn = MyTools.StrFiltr(request.getParameter("PK_XN"));
			try {
				jsonV = bean.queXqCombo(xn, "over");
				jal = (JSONArray) jsonV.get(2);
				JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询排课过期的教学性质
		if("queOverJxxzCombo".equalsIgnoreCase(active)){
			String xn = MyTools.StrFiltr(request.getParameter("PK_XN"));
			String xq = MyTools.StrFiltr(request.getParameter("PK_XQ"));
			try {
				jsonV = bean.queJxxzCombo(xn, xq, "over");
				jal = (JSONArray) jsonV.get(2);
				JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//初始化添加学期课程下拉框数据
		if("initSemComboData".equalsIgnoreCase(active)){
			try {
				//查询学年学期下拉框
				jsonV = bean.loadSemCombo();
				jal = JsonUtil.addJsonParams(jal, "semData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询课程下拉框
				jsonV = bean.loadCourseCombo();
				jal = JsonUtil.addJsonParams(jal, "courseData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询考试形式下拉框
				jsonV = bean.loadKsxsCombo();
				jal = JsonUtil.addJsonParams(jal, "ksxsData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教师下拉框
				jsonV = bean.queTeaCombo();
				jal = JsonUtil.addJsonParams(jal, "teaData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询添加学期课程列表信息
		if("queSemCourseList".equalsIgnoreCase(active)){
			String xnxqbm = MyTools.StrFiltr(request.getParameter("XNXQBM_CX"));
			String zymc = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZYMC_CX")), "UTF-8");
			String xzbmc = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XZBMC_CX")), "UTF-8");
			String kcmc = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			
			try {
				jsonV = bean.queSemCourseList(pageNum, pageSize, xnxqbm, zymc, xzbmc, kcmc);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//添加学期课程
		if("addSemCourse".equalsIgnoreCase(active)){
			String teaType = MyTools.StrFiltr(request.getParameter("PT_SKJSLX"));
			String classCode = MyTools.StrFiltr(request.getParameter("classCode"));
			
			try {
				bean.addSemCourse(teaType, classCode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询添加学期课程班级树
		if("queSemClassTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			String level = MyTools.StrFiltr(request.getParameter("level"));
			try {
				jsonV = bean.queSemClassTree(level, parentCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询添加学期课程班级列表
		if("queSemClassList".equalsIgnoreCase(active)){
			String zymc = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("SEM_ZYMC_CX")), "UTF-8");
			String xzbmc = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("SEM_XZBMC_CX")), "UTF-8");
			
			try {
				jsonV = bean.queSemClassList(pageNum, pageSize, zymc, xzbmc);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//删除学期课程
		if("delSemCourse".equalsIgnoreCase(active)){
			String delCode = MyTools.StrFiltr(request.getParameter("delCode"));
			
			try {
				bean.delSemCourse(delCode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//删除添加的个人课程
		if("delAddCourse".equalsIgnoreCase(active)){
			try {
				bean.delAddCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//初始化添加班级课程页面数据
		if("initAddClassCourse".equalsIgnoreCase(active)){
			try {
				//查询课程表信息
				jsonV = bean.loadClassKcbInfo();
				jal = JsonUtil.addJsonParams(jal, "kcbData", MyTools.StrFiltr(((Vector)jsonV.get(0)).get(2)));
				jal = JsonUtil.addJsonParams(jal, "addCourseData", MyTools.StrFiltr(((Vector)jsonV.get(1)).get(2)));
				
				//查询课程下拉框
				jsonV = bean.loadCourseCombo();
				jal = JsonUtil.addJsonParams(jal, "courseData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询考试形式下拉框
				jsonV = bean.loadKsxsCombo();
				jal = JsonUtil.addJsonParams(jal, "ksxsData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询可用教室（添加班级课程&添加个人课程）
		if("queSiteCombotree".equalsIgnoreCase(active)){
			try {
				String result = bean.queSiteCombotree();
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询教师下拉树数据（添加班级课程）
		if("queTeaCombotree".equalsIgnoreCase(active)){
			try {
				String result = bean.queTeaCombotree();
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//添加班级课程
		if("addClassCourse".equalsIgnoreCase(active)){
			try {
				bean.addClassCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "ID", bean.getPT_ID());
				jal = JsonUtil.addJsonParams(jal, "courseCode", bean.getPT_KCBH());
				jal = JsonUtil.addJsonParams(jal, "courseName", bean.getPT_KCMC());
				jal = JsonUtil.addJsonParams(jal, "teaCode", bean.getPT_SKJSBH());
				jal = JsonUtil.addJsonParams(jal, "teaName", bean.getPT_SKJSMC());
				jal = JsonUtil.addJsonParams(jal, "score", bean.getPT_XF());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//编辑班级课程
		if("editClassCourse".equalsIgnoreCase(active)){
			try {
				bean.editClassCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "ID", bean.getPT_ID());
				jal = JsonUtil.addJsonParams(jal, "courseCode", bean.getPT_KCBH());
				jal = JsonUtil.addJsonParams(jal, "courseName", bean.getPT_KCMC());
				jal = JsonUtil.addJsonParams(jal, "teaCode", bean.getPT_SKJSBH());
				jal = JsonUtil.addJsonParams(jal, "teaName", bean.getPT_SKJSMC());
				jal = JsonUtil.addJsonParams(jal, "ksxs", bean.getPT_KSXS());
				jal = JsonUtil.addJsonParams(jal, "score", bean.getPT_XF());
				jal = JsonUtil.addJsonParams(jal, "zks", bean.getPT_ZKS());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//删除添加的整班课程
		if("delClassCourse".equalsIgnoreCase(active)){
			try {
				bean.delClassCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//初始化添加个人课程页面数据
		if("initAddStuCourse".equalsIgnoreCase(active)){
			try {
				//获取学期周次
				String xqzc = bean.loadSemesterWeek();
				jal = JsonUtil.addJsonParams(jal, "xqzc", xqzc);
				
				//查询课程表信息
				jsonV = bean.loadClassKcbInfo();
				jal = JsonUtil.addJsonParams(jal, "kcbData", MyTools.StrFiltr(((Vector)jsonV.get(0)).get(2)));
				jal = JsonUtil.addJsonParams(jal, "addCourseData", MyTools.StrFiltr(((Vector)jsonV.get(1)).get(2)));
				
				//查询班级下拉框
				jsonV = bean.loadClassCombo();
				jal = JsonUtil.addJsonParams(jal, "classData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询教学性质下拉框数据
		if("loadKcbCourseCombo".equalsIgnoreCase(active)){
			try {
				//查询课程下拉框
				String result = bean.loadKcbCourseCombo();
				response.getWriter().write(result);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询学生树
		if("queStuTree".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queStuTree();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询学生添加课程信息
		if("loadStuAddCourse".equalsIgnoreCase(active)){
			try {
				String result = bean.loadStuAddCourse();
				response.getWriter().write(result);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//添加个人课程
		if("addStuCourse".equalsIgnoreCase(active)){
			try {
				jsonV = bean.addStuCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "ID", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "kcbCode", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "classCode", MyTools.StrFiltr(jsonV.get(2)));
				jal = JsonUtil.addJsonParams(jal, "className", MyTools.StrFiltr(jsonV.get(3)));
				jal = JsonUtil.addJsonParams(jal, "courseName", MyTools.StrFiltr(jsonV.get(4)));
				jal = JsonUtil.addJsonParams(jal, "teaName", MyTools.StrFiltr(jsonV.get(5)));
				jal = JsonUtil.addJsonParams(jal, "weekNum", MyTools.StrFiltr(jsonV.get(6)));
				jal = JsonUtil.addJsonParams(jal, "siteName", MyTools.StrFiltr(jsonV.get(7)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//编辑个人课程
		if("editStuCourse".equalsIgnoreCase(active)){
			try {
				jsonV = bean.editStuCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "ID", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "kcbCode", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "classCode", MyTools.StrFiltr(jsonV.get(2)));
				jal = JsonUtil.addJsonParams(jal, "className", MyTools.StrFiltr(jsonV.get(3)));
				jal = JsonUtil.addJsonParams(jal, "courseName", MyTools.StrFiltr(jsonV.get(4)));
				jal = JsonUtil.addJsonParams(jal, "teaName", MyTools.StrFiltr(jsonV.get(5)));
				jal = JsonUtil.addJsonParams(jal, "weekNum", MyTools.StrFiltr(jsonV.get(6)));
				jal = JsonUtil.addJsonParams(jal, "siteName", MyTools.StrFiltr(jsonV.get(7)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//保存更改的课程信息
		if("saveCourseInfo".equalsIgnoreCase(active)){
			int xqzc = MyTools.StringToInt(MyTools.StrFiltr(request.getParameter("xqzc")));
			
			try {
				String skzcDetail = bean.saveCourseInfo(xqzc);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				if("保存成功".equalsIgnoreCase(bean.getMSG())){
					jal = JsonUtil.addJsonParams(jal, "teaCode", bean.getPT_SKJSBH());
					jal = JsonUtil.addJsonParams(jal, "teaName", bean.getPT_SKJSMC());
					jal = JsonUtil.addJsonParams(jal, "siteCode", bean.getPT_CDBH());
					jal = JsonUtil.addJsonParams(jal, "siteName", bean.getPT_CDMC());
					jal = JsonUtil.addJsonParams(jal, "weekAll", bean.getPT_SKZC());
					jal = JsonUtil.addJsonParams(jal, "weekDetail", skzcDetail);
					
					//获取教师和场地已用信息
					String teaCondition = "'"+bean.getPT_SKJSBH().replaceAll("[+]", "','")+"'";
					//String siteCondition = "'"+bean.getPT_CDBH().replaceAll("[+]", "','")+"'";
					jsonV = bean.queUsedOrderInfo("tea", teaCondition);
					jal = JsonUtil.addJsonParams(jal, "teaUsedOrder", MyTools.StrFiltr(jsonV.get(0)));
					jal = JsonUtil.addJsonParams(jal, "teaList", MyTools.StrFiltr(jsonV.get(1)));
					
				}
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//设置排课状态
		if("setPkState".equalsIgnoreCase(active)){
			ServletContext sc = this.getServletContext();//取得Application对象
			String state = MyTools.StrFiltr(request.getParameter("pkState"));
			try {
				if("using".equalsIgnoreCase(state)){
					Vector teaInfo = bean.getTeaTel();//获取当前教师姓名及联系方式
					sc.setAttribute("pkState", state);
					sc.setAttribute("pkTeaCode", MyTools.getSessionUserCode(request));
					sc.setAttribute("pkTeaName", MyTools.StrFiltr(teaInfo.get(0)));
					//sc.setAttribute("pkTeaTel", MyTools.StrFiltr(teaInfo.get(1)));
					//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
					//sc.setAttribute("pkTime", df.format(new Date()));// new Date()为获取当前系统时间System.currentTimeMillis()
					sc.setAttribute("pkTime", System.currentTimeMillis());//为获取当前系统时间
				}else{
					//判断当前登录人与正在排课的教师是否相同
					if(bean.getUSERCODE().equalsIgnoreCase(MyTools.StrFiltr(sc.getAttribute("pkTeaCode")))){
						sc.setAttribute("pkState", state);
						sc.setAttribute("pkTeaCode", "");
						sc.setAttribute("pkTeaName", "");
						sc.setAttribute("pkTeaTel", "");
						sc.setAttribute("pkTime", "");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//解锁排课状态
		if("unlockPkState".equalsIgnoreCase(active)){
			ServletContext sc = this.getServletContext();//取得Application对象
			
			try {
				sc.setAttribute("pkState", "noUse");
				sc.setAttribute("pkTeaCode", "");
				sc.setAttribute("pkTeaName", "");
				sc.setAttribute("pkTeaTel", "");
				sc.setAttribute("pkTime", "");
				jal = JsonUtil.addJsonParams(jal, "MSG", "解锁成功");
			} catch (Exception e) {
				jal = JsonUtil.addJsonParams(jal, "MSG", "解锁成功");
			}
			
			response.getWriter().write(jal.toString());
		}
		
		//检查目前是否有教师在排课
		if("checkPkState".equalsIgnoreCase(active)){
			ServletContext sc = this.getServletContext();//取得Application对象
	        String pkState = MyTools.StrFiltr(sc.getAttribute("pkState"));
	        
	        //判断如果是正在排课状态
	        if("using".equalsIgnoreCase(pkState)){
	        	if(bean.getUSERCODE().equalsIgnoreCase(MyTools.StrFiltr(sc.getAttribute("pkTeaCode")))){
	        		jal = JsonUtil.addJsonParams(jal, "pkState", "noUse");
	        	}else{
	        		jal = JsonUtil.addJsonParams(jal, "pkState", pkState);
	        		jal = JsonUtil.addJsonParams(jal, "pkTeaName", MyTools.StrFiltr(sc.getAttribute("pkTeaName")));
	        		jal = JsonUtil.addJsonParams(jal, "pkTeaTel", MyTools.StrFiltr(sc.getAttribute("pkTeaTel")));
	        		
	        		String resultTime = "";
	        		long pkTime = MyTools.StringToLong(MyTools.StrFiltr(sc.getAttribute("pkTime")));
	        		long different = System.currentTimeMillis() - pkTime;
	        		long days = different / (1000 * 60 * 60 * 24);  
	        	    long hours = (different % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
	        	    long minutes = (different % (1000 * 60 * 60)) / (1000 * 60);  
	        	    long seconds = (different % (1000 * 60)) / 1000;
	        	    if(days > 0)
	        	    	resultTime += days + "天";
	        	    if(hours > 0)
	        	    	resultTime += hours + "小时";
	        	    if(minutes > 0)
	        	    	resultTime += minutes + "分钟";
	        	    if(seconds > 0)
	        	    	resultTime += seconds + "秒";
	        	    
	        		jal = JsonUtil.addJsonParams(jal, "pkTime", resultTime);
	        	}
	        }else{
	        	jal = JsonUtil.addJsonParams(jal, "pkState", pkState);
	        }
			response.getWriter().write(jal.toString());
		}
		
		//公共课确认
		if("pubCourseConfirm".equalsIgnoreCase(active)){
			try {
				bean.pubCourseConfirm();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//删除学期课程表相关信息
		if("delTimetable".equalsIgnoreCase(active)){
			try {
				bean.delTimetable();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询授课教师列表
		if("queTeaList".equalsIgnoreCase(active)){
			String SKJSBH_CX = MyTools.StrFiltr(request.getParameter("SKJSBH_CX"));
			String SKJSMC_CX = MyTools.StrFiltr(request.getParameter("SKJSMC_CX"));
//			String SKJSLB_CX = MyTools.StrFiltr(request.getParameter("SKJSLB_CX"));
			String xqzc = MyTools.StrFiltr(request.getParameter("curXqzc"));
			String bjbh = MyTools.StrFiltr(request.getParameter("BJBH")); //班级编号
			
			try {
//				String result = bean.loadTeaList(pageNum, pageSize, SKJSBH_CX, SKJSMC_CX, SKJSLB_CX, xqzc);
				String result = bean.loadTeaList(pageNum, pageSize, SKJSBH_CX, SKJSMC_CX, xqzc, bjbh);
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询授课教室列表
		if("queSiteList".equalsIgnoreCase(active)){
			String CDBH_CX = MyTools.StrFiltr(request.getParameter("CDBH_CX"));
			String CDMC_CX = MyTools.StrFiltr(request.getParameter("CDMC_CX"));
			String CDLX_CX = MyTools.StrFiltr(request.getParameter("CDLX_CX"));
			String xqzc = MyTools.StrFiltr(request.getParameter("curXqzc"));
			String bjbh = MyTools.StrFiltr(request.getParameter("BJBH")); //班级编号
			
			try {
//				String result = bean.loadSiteList(pageNum, pageSize, CDBH_CX, CDMC_CX, CDLX_CX, xqzc);
				String result = bean.loadSiteList2(pageNum, pageSize, CDBH_CX, CDMC_CX, CDLX_CX, xqzc, bjbh);
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//检查周次可用情况信息
		if("checkWeekUsable".equalsIgnoreCase(active)){
			String xqzc = MyTools.StrFiltr(request.getParameter("xqzc"));
			
			try {
				//查询学年学期列表
				String result = bean.checkWeekUsable(xqzc);
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//添加周课表信息
//		if("addZkbxx".equalsIgnoreCase(active)){
//			try {
//				DBSource db = new DBSource(request);
//				//获取所有需要排课的班级
//				String sql = "select distinct b.行政班代码 from V_规则管理_授课计划明细表 a " +
//					"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
//					"where a.状态='1' and b.状态='1' and b.学年学期编码='2015201'";
//				Vector classVec = db.GetContextVector(sql);
//				
//				for(int i=0; i<classVec.size(); i++){
//					bean.addWeekDetail("2015201", "", MyTools.StrFiltr(classVec.get(i)));
//				}
//				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
//				response.getWriter().write(jal.toString());
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param PkszBean
    */
	private void getFormData(HttpServletRequest request,PkszBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAuth(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setPK_KCBZBBH(MyTools.StrFiltr(request.getParameter("PK_KCBZBBH"))); //课程表主表编号
		bean.setPK_XNXQBM(MyTools.StrFiltr(request.getParameter("PK_XNXQBM"))); //学年学期编码
		bean.setPK_ZYDM(MyTools.StrFiltr(request.getParameter("PK_ZYDM"))); //专业代码
		bean.setPK_XZBDM(MyTools.StrFiltr(request.getParameter("PK_XZBDM"))); //行政班代码
		bean.setPK_KCBMXBH(MyTools.StrFiltr(request.getParameter("PK_KCBMXBH"))); //课程表明细编号
		bean.setPK_KCBZBBH(MyTools.StrFiltr(request.getParameter("PK_KCBZBBH"))); //课程表主表编号
		bean.setPK_BJPKZT(MyTools.StrFiltr(request.getParameter("PK_BJPKZT"))); //班级排课状态
		bean.setPK_SJXL(MyTools.StrFiltr(request.getParameter("PK_SJXL"))); //时间序列
		bean.setPK_LJXGBH(MyTools.StrFiltr(request.getParameter("PK_LJXGBH"))); //连节相关编号
		bean.setPK_SKJHMXBH(MyTools.StrFiltr(request.getParameter("PK_SKJHMXBH"))); //授课计划明细编号
		bean.setPK_SJCDBH(MyTools.StrFiltr(request.getParameter("PK_SJCDBH"))); //实际场地编号
		bean.setPK_SJCDMC(MyTools.StrFiltr(request.getParameter("PK_SJCDMC"))); //实际场地名称
		bean.setPK_BZ(MyTools.StrFiltr(request.getParameter("PK_BZ"))); //备注
		bean.setPK_ZT(MyTools.StrFiltr(request.getParameter("PK_ZT"))); //状态
		
		bean.setPK_XB(MyTools.StrFiltr(request.getParameter("PK_XB"))); //系部
		
		bean.setPT_ID(MyTools.StrFiltr(request.getParameter("PT_ID"))); //编号
		bean.setPT_LX(MyTools.StrFiltr(request.getParameter("PT_LX"))); //类型
		bean.setPT_XNXQBM(MyTools.StrFiltr(request.getParameter("PT_XNXQBM"))); //学年学期编码
		bean.setPT_XZBDM(MyTools.StrFiltr(request.getParameter("PT_XZBDM"))); //行政班代码
		bean.setPT_XH(MyTools.StrFiltr(request.getParameter("PT_XH"))); //学号
		bean.setPT_KCBMXBH(MyTools.StrFiltr(request.getParameter("PT_KCBMXBH"))); //相关课程表编号
		bean.setPT_SJXL(MyTools.StrFiltr(request.getParameter("PT_SJXL"))); //时间序列
		bean.setPT_KCBH(MyTools.StrFiltr(request.getParameter("PT_KCBH"))); //课程编号
		bean.setPT_KCMC(MyTools.StrFiltr(request.getParameter("PT_KCMC"))); //课程名称
		bean.setPT_SKJSBH(MyTools.StrFiltr(request.getParameter("PT_SKJSBH"))); //授课教师编号
		bean.setPT_SKJSMC(MyTools.StrFiltr(request.getParameter("PT_SKJSMC"))); //授课教师名称
		bean.setPT_CDBH(MyTools.StrFiltr(request.getParameter("PT_CDBH"))); //场地编号
		bean.setPT_CDMC(MyTools.StrFiltr(request.getParameter("PT_CDMC"))); //场地名称
		bean.setPT_SKZC(MyTools.StrFiltr(request.getParameter("PT_SKZC"))); //授课周次
		bean.setPT_KSXS(MyTools.StrFiltr(request.getParameter("PT_KSXS"))); //考试形式
		bean.setPT_XF(MyTools.StrFiltr(request.getParameter("PT_XF"))); //学分
		bean.setPT_ZKS(MyTools.StrFiltr(request.getParameter("PT_ZKS"))); //总课时
		bean.setPT_CJR(MyTools.StrFiltr(request.getParameter("PT_CJR"))); //创建人
		bean.setPT_CJSJ(MyTools.StrFiltr(request.getParameter("PT_CJSJ"))); //创建时间
		bean.setPT_ZT(MyTools.StrFiltr(request.getParameter("PT_ZT"))); //状态
	}
}