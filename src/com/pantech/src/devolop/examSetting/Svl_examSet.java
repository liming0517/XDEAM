package com.pantech.src.devolop.examSetting;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

public class Svl_examSet extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Svl_examSet() {
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
		System.out.println("active:------------------------"+active);
		int pageNum = MyTools.parseInt(request.getParameter("page"));	//获得页面page参数 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows"));	//获得页面rows参数 分页
		
		Vector jsonV = null;//返回结果集
		JSONArray jal = null;//返回json对象
		ExamSetBean bean = new ExamSetBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("checkksgl".equalsIgnoreCase(active)){
			try {
				bean.checkKSGL();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("checkdabukao".equalsIgnoreCase(active)){
			try {
				bean.checkDABUKAO();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//初始化页面数据
		if("showNotCourse".equalsIgnoreCase(active)){
			try {
				jsonV = bean.showNotCourse();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//初始化页面数据
		if("showNotKSXS".equalsIgnoreCase(active)){
			try {
				jsonV = bean.showNotKSXS();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//考试形式查询
		if("showAllKSXS".equalsIgnoreCase(active)){
			String GS_KCMC=MyTools.StrFiltr(request.getParameter("GS_KCMC"));
			String GS_ZYMC=MyTools.StrFiltr(request.getParameter("GS_ZYMC"));
			String GS_KSXS=MyTools.StrFiltr(request.getParameter("GS_KSXS"));
			String GS_XZBMC=MyTools.StrFiltr(request.getParameter("GS_XZBMC"));
			try {
				jsonV = bean.showAllKSXS(pageNum,pageSize,GS_KCMC,GS_ZYMC,GS_KSXS,GS_XZBMC);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//补考查询
		if("showBUKAO".equalsIgnoreCase(active)){
			String GS_KCMC=MyTools.StrFiltr(request.getParameter("GS_KCMC"));
			String GS_KSXS=MyTools.StrFiltr(request.getParameter("GS_KSXS"));
			String GS_XZBMC=MyTools.StrFiltr(request.getParameter("GS_XZBMC"));
			try {
				//查询列表
				String revec = bean.showBUKAO(pageNum, pageSize,GS_KCMC,GS_KSXS,GS_XZBMC);		
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
				//TraceLog.Trace("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//大补考查询
		if("showDABUKAO".equalsIgnoreCase(active)){
			String GS_KCMC=MyTools.StrFiltr(request.getParameter("GS_KCMC"));
			String GS_KSXS=MyTools.StrFiltr(request.getParameter("GS_KSXS"));
			String GS_XZBMC=MyTools.StrFiltr(request.getParameter("GS_XZBMC"));
			try {
				//查询列表
				String revec = bean.showDABUKAO(pageNum, pageSize,GS_KCMC,GS_KSXS,GS_XZBMC);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
				//TraceLog.Trace("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询列表
				//jsonV = bean.queryTree(pageNum, pageSize,"");
				//jal = (JSONArray)jsonV.get(2);
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询考试周期下拉框
				jsonV = bean.loadQZQMCombo();
				jal = JsonUtil.addJsonParams(jal, "qzqmData", ((JSONArray)jsonV.get(2)).toString());
						
				//查询学年下拉框
				jsonV = bean.loadYearCombo("");
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询专业班级树
		if("queryTree".equalsIgnoreCase(active)){
			String parentId=MyTools.StrFiltr(request.getParameter("parentId"));
			String level = MyTools.StrFiltr(request.getParameter("level"));
			try {
				//查询列表
				jsonV = bean.queryTree(pageNum, pageSize, parentId, level);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询列表
		if("query".equalsIgnoreCase(active)){
			String termid=MyTools.StrFiltr(request.getParameter("termid"));
			try {
				//查询列表
				//jsonV = bean.queryTree(pageNum, pageSize,"");
				//jal = (JSONArray)jsonV.get(2);
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				//查询周次数量
				bean.getWeeknum(termid);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//初始化页面数据
		if("initGridData".equalsIgnoreCase(active)){
			try {
				//查询列表
				jsonV = bean.queryGrid(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				//查询节次时间
				jsonV = bean.loadJCSJ();
				jal = JsonUtil.addJsonParams(jal, "timeData", ((JSONArray)jsonV.get(2)).toString());

				//查询
				//jsonV = bean.loadGPJP();
				//jal = JsonUtil.addJsonParams(jal, "gpjpData", ((JSONArray)jsonV.get(2)).toString());
						
				//String times = bean.loadGPJP2();
				//jal = JsonUtil.addJsonParams(jal, "gpjpData2", times);
						
				//查询学科名称下拉框
				//jsonV = bean.loadXKMCCombo();
				//jal = JsonUtil.addJsonParams(jal, "xkmcData", ((JSONArray)jsonV.get(2)).toString());
						
				//查询任课教师下拉框
				//jsonV = bean.loadRKJSCombo();
				//jal = JsonUtil.addJsonParams(jal, "rkjsData", ((JSONArray)jsonV.get(2)).toString());
						
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		
		//保存考试修改信息
		if("savesfks".equalsIgnoreCase(active)){
			String sfks = MyTools.StrFiltr(request.getParameter("GG_SFKS"));
			String cdlx = MyTools.StrFiltr(request.getParameter("GG_CDLX"));
			String kszq = MyTools.StrFiltr(request.getParameter("GG_KSZQ"));
			String sjlx = MyTools.StrFiltr(request.getParameter("GG_SJLX"));
			String ksxs = MyTools.StrFiltr(request.getParameter("GG_KSXS"));
			try {
				bean.savesfks(sfks,cdlx,kszq,sjlx,ksxs);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

		//大补考修改信息
		if("savedbkksxs".equalsIgnoreCase(active)){
			String dbkarray = MyTools.StrFiltr(request.getParameter("dbkarray"));
			String dbk_xnxq = MyTools.StrFiltr(request.getParameter("dbk_xnxq"));
			//String dbk_kcmc = MyTools.StrFiltr(request.getParameter("dbk_kcmc"));
			String dbk_ksxs = MyTools.StrFiltr(request.getParameter("dbk_ksxs"));

			try {
				bean.savedbkksxs(dbkarray,dbk_xnxq,dbk_ksxs);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//合并大补考
		if("addDBKinfo".equalsIgnoreCase(active)){
			try {
				//查询列表
				String dbkarray = MyTools.StrFiltr(request.getParameter("dbkarray"));
				String bktype = MyTools.StrFiltr(request.getParameter("bktype"));
				bean.addDBKinfo(dbkarray,bktype);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//拆分大补考
		if("splitDBKinfo".equalsIgnoreCase(active)){
			try {
				//查询列表
				String dbkinfo = MyTools.StrFiltr(request.getParameter("dbkinfo"));
				String bktype = MyTools.StrFiltr(request.getParameter("bktype"));
				bean.splitDBKinfo(dbkinfo,bktype);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if("listkskc".equalsIgnoreCase(active)){
			try {
				//查询列表
				String BJMC_CX = MyTools.StrFiltr(request.getParameter("BJMC_CX"));
				String KCMC_CX = MyTools.StrFiltr(request.getParameter("KCMC_CX"));
				String ZYDM_CX = MyTools.StrFiltr(request.getParameter("ZYDM_CX"));
				String examid = MyTools.StrFiltr(request.getParameter("examid"));
				jsonV = bean.listkskc(pageNum, pageSize,BJMC_CX,KCMC_CX,ZYDM_CX,examid);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("listtskc".equalsIgnoreCase(active)){
			try {
				//查询列表
				String examid = MyTools.StrFiltr(request.getParameter("examid"));
				jsonV = bean.listtskc(pageNum, pageSize,examid);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("listckxs".equalsIgnoreCase(active)){
			try {
				//查询列表
				String examid = MyTools.StrFiltr(request.getParameter("examid"));
				jsonV = bean.listckxs(pageNum, pageSize,examid);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("saveselect".equalsIgnoreCase(active)){
			try {
				//查询列表
				String skid = MyTools.StrFiltr(request.getParameter("skid"));
				String examid = MyTools.StrFiltr(request.getParameter("examid"));
				String delexamid = MyTools.StrFiltr(request.getParameter("delexamid"));
				bean.saveselect(skid,examid,delexamid);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		if("delselect".equalsIgnoreCase(active)){
			try {
				//查询列表
				String skjhid = MyTools.StrFiltr(request.getParameter("skjhid"));
				bean.delselect(skjhid);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("listkcap".equalsIgnoreCase(active)){
			try {
				//查询列表
				String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
				String jxxz = MyTools.StrFiltr(request.getParameter("jxxz"));
				jsonV = bean.listkcap(pageNum, pageSize,xnxq,jxxz);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("loadGridExamCourse".equalsIgnoreCase(active)){
			try {
				//查询列表
				jsonV = bean.loadGridExamCourse(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("loadGridExamDate".equalsIgnoreCase(active)){
			try {
				//查询列表
				jsonV = bean.loadGridExamDate(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//显示部门
		if("loadGridDept".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadGridDept();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("importDeptTea".equalsIgnoreCase(active)){
			try {
				//查询列表
				String deptarray = MyTools.StrFiltr(request.getParameter("deptarray"));
				bean.importDeptTea(deptarray);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("loadGridExamTeacher".equalsIgnoreCase(active)){
			String teabh = MyTools.StrFiltr(request.getParameter("teabh"));
			String teaname = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("teaname"),"utf-8"));
			String tealx = MyTools.StrFiltr(request.getParameter("tealx"));
			try {
				//查询列表				
				jsonV = bean.loadGridExamTeacher(pageNum, pageSize,teabh,teaname,tealx);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//保存最大监考次数
		if("saveTeaNum".equalsIgnoreCase(active)){
			String ksjsarray = MyTools.StrFiltr(request.getParameter("ksjsarray"));
			String jkcs = MyTools.StrFiltr(request.getParameter("jkcs"));
			try {
				bean.saveTeaNum(ksjsarray,jkcs);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		if("searchKSLX".equalsIgnoreCase(active)){
			String ex_kszbbh = MyTools.StrFiltr(request.getParameter("ex_kszbbh"));
			String ex_xbdm = MyTools.StrFiltr(request.getParameter("ex_xbdm"));
			try {
				//查询列表				
				bean.searchKSLX(ex_kszbbh,ex_xbdm);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("loadKSLXCombo".equalsIgnoreCase(active)){  //考试类型
			try {
				jsonV = bean.loadKSLXCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("CX_XNXQMCCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.CX_XNXQMCCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("KCQZQMCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.loadQZQMCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("CXQZQMCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.CXQZQMCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("PKQZQMCombobox".equalsIgnoreCase(active)){  //考试名称
			String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
			try {
				jsonV = bean.PKQZQMCombobox(xnxq);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("PKXBDMCombobox".equalsIgnoreCase(active)){  //系部名称
			String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
			try {
				jsonV = bean.PKXBDMCombobox(xnxq);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("PKKSTSCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.PKKSTSCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("KSZQDRCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.loadQZQMCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("KSJXLHCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.KSJXLHCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("KS_KSLXCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.KS_KSLXCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//教师类型
		if("GT_JSLXCombobox".equalsIgnoreCase(active)){  
			try {
				jsonV = bean.GT_JSLXCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//监考日期combobox
		if("JKRQCombobox".equalsIgnoreCase(active)){  
			String jzzs = MyTools.StrFiltr(request.getParameter("JZZS"));//考试周期
			try {
				jsonV = bean.JKRQCombobox(jzzs);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//自动排课
		if("autoAreaArrange".equalsIgnoreCase(active)){
			String xnxqbm = MyTools.StrFiltr(request.getParameter("KC_XNXQBM"));//学年学期编码
			String qzqm = MyTools.StrFiltr(request.getParameter("KC_QZQM"));//考试名称
			String xbdm = MyTools.StrFiltr(request.getParameter("KC_XBDM"));//系部代码

			try {
				bean.autoAreaArrange(xnxqbm, qzqm, xbdm);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				jal = JsonUtil.addJsonParams(jal, "MSG3", bean.getMSG3());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				String result = "{\"MSG\":\"自动排考场发生错误，请联系管理员！\"}";
				response.getWriter().write(result);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
				//查询当前学年设置的每周天数和每天节数
				if("initBlankKCAP".equalsIgnoreCase(active)){
					String PK_KSZBBH = MyTools.StrFiltr(request.getParameter("PK_KSZBBH"));//考试周期
					try {
						jsonV = bean.initBlankKCAP(PK_KSZBBH);
						if(jsonV!=null && jsonV.size()>0){
							Vector vec = (Vector)jsonV.get(0);
							if(vec!=null && vec.size()>0){
								
								jal = JsonUtil.addJsonParams(jal, "sw", MyTools.StrFiltr(vec.get(1)));
								jal = JsonUtil.addJsonParams(jal, "zw", MyTools.StrFiltr(vec.get(2)));
								jal = JsonUtil.addJsonParams(jal, "xw", MyTools.StrFiltr(vec.get(3)));
								jal = JsonUtil.addJsonParams(jal, "ws", MyTools.StrFiltr(vec.get(4)));
								jal = JsonUtil.addJsonParams(jal, "jcsj", MyTools.StrFiltr(vec.get(5)));
								
							}else{
								
								jal = JsonUtil.addJsonParams(jal, "sw", "0");
								jal = JsonUtil.addJsonParams(jal, "zw", "0");
								jal = JsonUtil.addJsonParams(jal, "xw", "0");
								jal = JsonUtil.addJsonParams(jal, "ws", "0");
								jal = JsonUtil.addJsonParams(jal, "jcsj", "");
								
							}
							//获取当前专业班级最大课程数
							vec = (Vector)jsonV.get(1);
							if(vec!=null && vec.size()>0){
								jal = JsonUtil.addJsonParams(jal, "maxNum", MyTools.StrFiltr(vec.get(0)));
							}else{
								jal = JsonUtil.addJsonParams(jal, "maxNum", "1");
							}
							//获取学期周次
							jal = JsonUtil.addJsonParams(jal, "xqzc", MyTools.StrFiltr(jsonV.get(2).toString()));
							jal = JsonUtil.addJsonParams(jal, "ksrq", MyTools.StrFiltr(jsonV.get(3).toString()));
						}
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//查询班级课程表
				if("loadClassKCAP".equalsIgnoreCase(active)){
					try {
						jsonV = bean.loadClassKCAP();
						jal = JsonUtil.addJsonParams(jal, "kcb", MyTools.StrFiltr(((Vector)jsonV.get(0)).get(2)));
						jal = JsonUtil.addJsonParams(jal, "wpkc", MyTools.StrFiltr(((Vector)jsonV.get(1)).get(2)));
						
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//更新考试安排表
				if("updateKCB".equalsIgnoreCase(active)){
					//String[] kcbInfo = MyTools.StrFiltr(request.getParameter("kcbInfo")).split(",", -1);
					
					String kcaparray = MyTools.StrFiltr(request.getParameter("kcaparray"));				
					try {
						bean.updateKCB(kcaparray);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//查询监考教师
				if("loadClassKCJS".equalsIgnoreCase(active)){
					String teacherCode = MyTools.StrFiltr(request.getParameter("teacherCode"));
					try {
						jsonV = bean.loadClassKCJS(teacherCode);
						jal = JsonUtil.addJsonParams(jal, "kcb", MyTools.StrFiltr(((Vector)jsonV.get(0)).get(2)));
//						jal = JsonUtil.addJsonParams(jal, "wpkc", MyTools.StrFiltr(((Vector)jsonV.get(1)).get(2)));
//						jal = JsonUtil.addJsonParams(jal, "bz", MyTools.StrFiltr(jsonV.get(2)));
//						jal = JsonUtil.addJsonParams(jal, "stuNum", MyTools.StrFiltr(jsonV.get(3)));
//						jal = JsonUtil.addJsonParams(jal, "tkOrder", MyTools.StrFiltr(jsonV.get(4)));
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				if("cxfsCombobx".equalsIgnoreCase(active)){  //查询方式下拉框
					try {
						jsonV = bean.CXFSCombobx();
						jal = JsonUtil.addJsonParams(jal, "cxfs", ((JSONArray)jsonV.get(2)).toString());
						response.getWriter().write(jal.toString());
						
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				
				//查询考场
				if("loadClassKCRM".equalsIgnoreCase(active)){
					String roomCode = MyTools.StrFiltr(request.getParameter("roomCode"));
					try {
						jsonV = bean.loadClassKCRM(roomCode);
						jal = JsonUtil.addJsonParams(jal, "kcb", MyTools.StrFiltr(((Vector)jsonV.get(0)).get(2)));
						jal = JsonUtil.addJsonParams(jal, "wpkc", MyTools.StrFiltr(((Vector)jsonV.get(1)).get(2)));
						jal = JsonUtil.addJsonParams(jal, "bz", MyTools.StrFiltr(jsonV.get(2)));
						jal = JsonUtil.addJsonParams(jal, "stuNum", MyTools.StrFiltr(jsonV.get(3)));
						jal = JsonUtil.addJsonParams(jal, "tkOrder", MyTools.StrFiltr(jsonV.get(4)));
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//查询班级树
				if("queClassTree".equalsIgnoreCase(active)){
					String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
					String level = MyTools.StrFiltr(request.getParameter("level"));
					String PK_KSZBBH = MyTools.StrFiltr(request.getParameter("PK_KSZBBH"));
					try {
						jsonV = bean.queClassTree(level, parentCode, PK_KSZBBH);
						jal = (JSONArray) jsonV.get(2);
						response.getWriter().write(jal.toString());
					}catch(SQLException e){
						e.printStackTrace();
					}catch (WrongSQLException e) {
						e.printStackTrace();
					}
				}
				
				//查询班级课程表
				if("saveExamInfo".equalsIgnoreCase(active)){
					String teachid = MyTools.StrFiltr(request.getParameter("teachid"));
					String teachname = MyTools.StrFiltr(request.getParameter("teachname"));
					String roomid = MyTools.StrFiltr(request.getParameter("roomid"));
					String roomname = MyTools.StrFiltr(request.getParameter("roomname"));
					try {
						bean.saveExamInfo(teachid,teachname,roomid,roomname);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}	
				
				//查询班级课程表
				if("loadKSRQ".equalsIgnoreCase(active)){
					try {
						jsonV=bean.loadKSRQ();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				//保存考试日期
				if("saveKSRQ".equalsIgnoreCase(active)){
					String saveType = MyTools.StrFiltr(request.getParameter("saveType"));
					String kssj = MyTools.StrFiltr(request.getParameter("kssj"));
					String jssj = MyTools.StrFiltr(request.getParameter("jssj"));
					try {
						if(saveType.equals("newExamDate")) {
							bean.saveKSRQ(kssj,jssj);
						}else if(saveType.equals("editExamDate")) {
							bean.editKSRQ(kssj,jssj);
						}	
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				//删除考试日期
				if("delKSRQ".equalsIgnoreCase(active)){
					try {
						bean.delKSRQ();
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存考试课程
				if("saveKSKC".equalsIgnoreCase(active)){
					String ei_zydm = MyTools.StrFiltr(request.getParameter("ei_zydm"));
					String kskcarray = MyTools.StrFiltr(request.getParameter("kskcarray"));
					try {
						bean.saveKSKC(ei_zydm,kskcarray);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				//导入补考信息
				if("resitInfo".equalsIgnoreCase(active)){
					
					try {
						bean.resitInfo();
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				//补考信息里学期combobox
				if("resit_xnxq".equalsIgnoreCase(active)){
					try {
						jsonV=bean.resit_xnxq();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());				
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//查询授课教师列表
				if("queTeaList".equalsIgnoreCase(active)){
					String JKJSBH = MyTools.StrFiltr(request.getParameter("JKJSBH"));
					String JKJSXM = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("JKJSXM"),"utf-8"));
					String SJXL = MyTools.StrFiltr(request.getParameter("SJXL"));
					String KCMC = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("KCMC"),"utf-8")); 
					String num = MyTools.StrFiltr(request.getParameter("num")); 
					String jkjsarray = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("jkjsarray"),"utf-8")); 
					String ic_teaCode = MyTools.StrFiltr(request.getParameter("ic_teaCode")); 
					String ic_teaName = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("ic_teaName"),"utf-8")); 
					String chgteaarray = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("chgteaarray"),"utf-8")); 
					
					try {
						jsonV = bean.queTeaList(pageNum,pageSize,JKJSBH,JKJSXM,SJXL,KCMC,num,jkjsarray,ic_teaCode,ic_teaName,chgteaarray);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//保存替换的监考教师
				if("saveJKJSInfo".equalsIgnoreCase(active)){
					String KCMC = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("KCMC"),"utf-8")); 
					String chgteaarray = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("chgteaarray"),"utf-8")); 
					try {
						bean.saveJKJSInfo(KCMC,chgteaarray);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//获取临时授课教师列表
				if("getVecTea".equalsIgnoreCase(active)){
					try {
						jsonV = bean.getVecTea();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
			
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				
				//查询班级树
				if("queMajorTree".equalsIgnoreCase(active)){
					String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
					String level = MyTools.StrFiltr(request.getParameter("level"));
					String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
					String zydm = MyTools.StrFiltr(request.getParameter("zydm"));
					try {
						jsonV = bean.queMajorTree(level, parentCode,sAuth,zydm);
						jal = (JSONArray) jsonV.get(2);
						response.getWriter().write(jal.toString());
					}catch(SQLException e){
						e.printStackTrace();
					}catch (WrongSQLException e) {
						e.printStackTrace();
					}
				}
				
				//查询专业班级树
				if("queryExamSetTree".equalsIgnoreCase(active)){
					String parentId=MyTools.StrFiltr(request.getParameter("parentId"));
					try {
						//查询列表
						jsonV = bean.queryExamSetTree(pageNum, pageSize, parentId);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if("BJMCCombobox".equalsIgnoreCase(active)){  //编辑文件
					try {
						jsonV = bean.BJMCCombobox();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				if("KCMCCombobox".equalsIgnoreCase(active)){  //编辑文件
					try {
						jsonV = bean.KCMCCombobox();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				if("KSZQCombobox".equalsIgnoreCase(active)){  //编辑文件
					String DRxnxq = MyTools.StrFiltr(request.getParameter("DRxnxq"));
					try {
						jsonV = bean.KSZQCombobox(DRxnxq);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				//保存新建考试
				if("saveNewExam".equalsIgnoreCase(active)){
					String KCDM=MyTools.StrFiltr(request.getParameter("KCDM"));
					String KCMC=MyTools.StrFiltr(request.getParameter("KCMC"));
					try {
						//查询列表
						bean.saveNewExam(KCDM, KCMC);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				//删除未排课程
				if ("deleteWeipai".equalsIgnoreCase(active)) {
					String weipaiarray = MyTools.StrFiltr(request.getParameter("weipaiarray"));
					try {
						bean.deleteWeipai(weipaiarray);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//显示考试形式
				if("selectKSXS".equalsIgnoreCase(active)){
					try {
						jsonV = bean.selectKSXS();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				//考试安排表导出
				if("ExportExcelKSAPB".equalsIgnoreCase(active)){
					try {
						String filePath = bean.ExportExcelKSAPB();
						jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
						jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
						response.getWriter().write(jal.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				//试卷标签导出
				if("ExportExcelSJBQ".equalsIgnoreCase(active)){
					try {
						String filePath = bean.ExportExcelSJBQ();
						jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
						jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
						response.getWriter().write(jal.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				//大补考答疑表导出
				if("ExportExcelDBKDY".equalsIgnoreCase(active)){
					try {
						String filePath = bean.ExportExcelDBKDY();
						jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
						jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
						response.getWriter().write(jal.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				//补考安排表导出
				if("ExportExcelBKAP".equalsIgnoreCase(active)){
					String tiyutime = MyTools.StrFiltr(request.getParameter("tiyutime"));
					String qtlxtime = MyTools.StrFiltr(request.getParameter("qtlxtime"));
					try {
						String filePath = bean.ExportExcelBKAP(tiyutime,qtlxtime);
						jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
						jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
						response.getWriter().write(jal.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				//大补考安排表导出
				if("ExportExcelDBKAP".equalsIgnoreCase(active)){
					String tiyutime = MyTools.StrFiltr(request.getParameter("tiyutime"));
					String qtlxtime = MyTools.StrFiltr(request.getParameter("qtlxtime"));
					try {
						String filePath = bean.ExportExcelDBKAP(tiyutime,qtlxtime);
						jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
						jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
						response.getWriter().write(jal.toString());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				
				//新建考试
				if("createExam".equalsIgnoreCase(active)){
					try {
						//查询列表
						bean.createExam();
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//编辑考试
				if("editCreExam".equalsIgnoreCase(active)){
					String ksapbh = MyTools.StrFiltr(request.getParameter("ksapbh"));
					try {
						//查询列表
						bean.editCreExam(ksapbh);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//删除考试
				if("delCreExam".equalsIgnoreCase(active)){
					String ksapbh = MyTools.StrFiltr(request.getParameter("ksapbh"));
					try {
						//查询列表
						bean.delCreExam(ksapbh);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				//学年学期编码
				if("DRXNXQCombobox".equalsIgnoreCase(active)){  
					try {
						jsonV = bean.DRXNXQCombobox();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				//考试名称
				if("DRKSMCCombobox".equalsIgnoreCase(active)){  
					String DRxnxq = MyTools.StrFiltr(request.getParameter("DRxnxq"));
					try {
						jsonV = bean.DRKSMCCombobox(DRxnxq);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				//查询监考教师
				if("openTeacher".equalsIgnoreCase(active)){
					String jzzs = MyTools.StrFiltr(request.getParameter("JZZS"));
					String teaId = MyTools.StrFiltr(request.getParameter("teaId"));
					String teaName = MyTools.StrFiltr(request.getParameter("teaName"));
					try {
						jsonV = bean.openTeacher(pageNum,pageSize,jzzs,teaId,teaName);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//查询监考教师日期
				if("openTeacherDate".equalsIgnoreCase(active)){
					String jzzs = MyTools.StrFiltr(request.getParameter("JZZS"));
					String teaId = MyTools.StrFiltr(request.getParameter("teaId"));
					String teaName = MyTools.StrFiltr(request.getParameter("teaName"));
					try {
						jsonV = bean.openTeacherDate(pageNum,pageSize,jzzs,teaId,teaName);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存监考教师
				if("saveTeacher".equalsIgnoreCase(active)){
					String jzzs = MyTools.StrFiltr(request.getParameter("JZZS"));
					String teainfoidarray = MyTools.StrFiltr(request.getParameter("teainfoidarray"));
					String teainfoarray = MyTools.StrFiltr(request.getParameter("teainfoarray"));
					try {
						bean.saveTeacher(jzzs,teainfoidarray,teainfoarray);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存监考啊教师日期
				if("saveTeaDate".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					String teadateidarray = MyTools.StrFiltr(request.getParameter("teadateidarray"));
					String jkrq = MyTools.StrFiltr(request.getParameter("jkrq"));
					try {
						bean.saveTeaDate(kszbbh,teadateidarray,jkrq);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//查询教室
				if("openRoom".equalsIgnoreCase(active)){
					String jzzs = MyTools.StrFiltr(request.getParameter("JZZS"));
					String roomId = MyTools.StrFiltr(request.getParameter("roomId"));
					try {
						jsonV = bean.openRoom(pageNum,pageSize,jzzs,roomId);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存考试教室
				if("saveClassroom".equalsIgnoreCase(active)){
					String jzzs = MyTools.StrFiltr(request.getParameter("JZZS"));
					String jxlh = MyTools.StrFiltr(request.getParameter("jxlh"));
					String clsinfoidarray = MyTools.StrFiltr(request.getParameter("clsinfoidarray"));
					String clsinfoarray = MyTools.StrFiltr(request.getParameter("clsinfoarray"));
					try {
						bean.saveClassroom(jzzs,jxlh,clsinfoidarray,clsinfoarray);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//读取考试规则设置
				if("loadGridSchedule".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					String kslx = MyTools.StrFiltr(request.getParameter("kslx"));
					try {
						jsonV = bean.loadGridSchedule(kszbbh,kslx);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存考试规则设置
				if("saveSchedule".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					String ksrqsjdarray = MyTools.StrFiltr(request.getParameter("ksrqsjdarray"));
					String kslx = MyTools.StrFiltr(request.getParameter("kslx"));
					String ksnj = MyTools.StrFiltr(request.getParameter("ksnj"));
					String kclx = MyTools.StrFiltr(request.getParameter("kclx"));
					String jsrs = MyTools.StrFiltr(request.getParameter("jsrs"));
					String kcsl = MyTools.StrFiltr(request.getParameter("kcsl"));
					String kydjs = MyTools.StrFiltr(request.getParameter("kydjs"));
					try {
						bean.saveSchedule(kszbbh,ksrqsjdarray,kslx,ksnj,kclx,jsrs,kcsl,kydjs);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存考试规则设置
				if("clearSchedule".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					String ksrqsjdarray = MyTools.StrFiltr(request.getParameter("ksrqsjdarray"));
					String jsrs = MyTools.StrFiltr(request.getParameter("jsrs"));
					String kcsl = MyTools.StrFiltr(request.getParameter("kcsl"));
					String kydjs = MyTools.StrFiltr(request.getParameter("kydjs"));
					String kslx = MyTools.StrFiltr(request.getParameter("kslx"));
					try {
						bean.clearSchedule(kszbbh,ksrqsjdarray,jsrs,kcsl,kydjs,kslx);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存考试规则设置
				if("loadGridSpecialCourse".equalsIgnoreCase(active)){
					String QZQM = MyTools.StrFiltr(request.getParameter("QZQM"));
					try {
						jsonV = bean.loadGridSpecialCourse(pageNum, pageSize, QZQM);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//读取左边datagrid
				if("loadGridselRQ".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					try {
						jsonV = bean.loadGridselRQ(kszbbh);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//读取右边datagrid
				if("loadGridselKSKC".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					String kcmc = MyTools.StrFiltr(request.getParameter("kcmc"));
					String bjmc = MyTools.StrFiltr(request.getParameter("bjmc"));
					String ksbh = MyTools.StrFiltr(request.getParameter("ksbh"));
					String ksccbh = MyTools.StrFiltr(request.getParameter("ksccbh"));
					String kstype=MyTools.StrFiltr(request.getParameter("kstype"));
					try {
						if(kstype.equals("")){//普通考试
							jsonV = bean.loadGridselKSKC(pageNum, pageSize, kszbbh ,kcmc ,bjmc,ksccbh);
							jal = (JSONArray)jsonV.get(2);
							response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
						}else if(kstype.equals("bk")){//补考
							String revec = bean.showBUKAO(pageNum, pageSize,kcmc,"请选择",bjmc);		
							response.getWriter().write("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
						}else if(kstype.equals("dbk")){//大补考
							String revec = bean.showDABUKAO(pageNum, pageSize,kcmc,"请选择",bjmc);
							response.getWriter().write("{\"total\":" + MyTools.StrFiltr(bean.getTATOL()) + ",\"rows\":" + revec + "}");
						}
						//TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存特殊课程设置
				if("savespecial".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					String tskcrqsjdarray = MyTools.StrFiltr(request.getParameter("tskcrqsjdarray"));
					String ksccbh = MyTools.StrFiltr(request.getParameter("ksccbh"));
					String kcmc = MyTools.StrFiltr(request.getParameter("kcmc"));
					String bjmc = MyTools.StrFiltr(request.getParameter("bjmc"));
					try {
						bean.savespecial(kszbbh ,tskcrqsjdarray,ksccbh,kcmc,bjmc);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//保存特殊课程设置
				if("delspecial".equalsIgnoreCase(active)){
					String kszbbh = MyTools.StrFiltr(request.getParameter("kszbbh"));
					String ksccbh = MyTools.StrFiltr(request.getParameter("ksccbh"));

					try {
						bean.delspecial(kszbbh ,ksccbh);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					} catch (SQLException e) {
						// TODO: handle exception
					}
				}
				
				//查询教师树
				if("queTeaTree".equalsIgnoreCase(active)){
					String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
					String level = MyTools.StrFiltr(request.getParameter("level"));
					String PK_KSZBBH = MyTools.StrFiltr(request.getParameter("PK_KSZBBH"));
					String ic_jsxm = MyTools.StrFiltr(URLDecoder.decode(request.getParameter("ic_jsxm"),"utf-8")); //教师姓名
					
					try {
						jsonV = bean.queTeaTree(level, parentCode,PK_KSZBBH,ic_jsxm);
						jal = (JSONArray) jsonV.get(2);
						response.getWriter().write(jal.toString());
					}catch(SQLException e){
						e.printStackTrace();
					}catch (WrongSQLException e) {
						e.printStackTrace();
					}
				}
							
				if("queryBUKAOinfo".equalsIgnoreCase(active)){  //编辑文件
					String BK_KCMC = MyTools.StrFiltr(request.getParameter("BK_KCMC"));
					String BK_KSXS = MyTools.StrFiltr(request.getParameter("BK_KSXS"));
					try {
						jsonV = bean.queryBUKAOinfo(pageNum,pageSize,BK_KCMC,BK_KSXS);
						jal = (JSONArray)jsonV.get(2);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");			
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				if("BKKSXSCombobox".equalsIgnoreCase(active)){  //编辑文件
					String PK_KSLX=MyTools.StrFiltr(request.getParameter("PK_KSLX"));
					try {
						jsonV = bean.BKKSXSCombobox(PK_KSLX);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());						
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				if("kc_zydmCombobox".equalsIgnoreCase(active)){  //编辑文件
					try {
						jsonV = bean.kc_zydmCombobox();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());						
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				if("KC_XZBDMCombobox".equalsIgnoreCase(active)){  //编辑文件
					try {
						jsonV = bean.KC_XZBDMCombobox();
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write(jal.toString());						
					} catch (SQLException e) {
						// TODO 自动生成 catch 块
						e.printStackTrace();
					}
				}
				
				if("sel_KCCombobox".equalsIgnoreCase(active)){  //编辑文件
					String selcouArray=MyTools.StrFiltr(request.getParameter("selcouArray"));
					String choice=MyTools.StrFiltr(request.getParameter("choice"));
					try {	
						//查询课程下拉框
						jsonV = bean.sel_KCCombobox(selcouArray,choice);
						jal = JsonUtil.addJsonParams(jal, "kcinfo", ((JSONArray)jsonV.get(2)).toString());
					
						jsonV = bean.sel_KCallCombobox(selcouArray,choice);
						jal = JsonUtil.addJsonParams(jal, "allkcinfo", ((JSONArray)jsonV.get(2)).toString());
						
						response.getWriter().write(jal.toString());
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
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
	private void getFormData(HttpServletRequest request,ExamSetBean bean){
		bean.setGG_BH(MyTools.StrFiltr(request.getParameter("GG_BH"))); //编号
		bean.setGG_LX(MyTools.StrFiltr(request.getParameter("GG_LX"))); //类型
		bean.setAuth(MyTools.StrFiltr(request.getParameter("AUTH"))); //用户权限
		bean.setGG_XNXQBM(MyTools.StrFiltr(request.getParameter("GG_XNXQBM"))); //学年学期编码
		bean.setGG_XZBDM(MyTools.StrFiltr(request.getParameter("GG_XZBDM"))); //行政班代码
		bean.setGG_SJXL(MyTools.StrFiltr(request.getParameter("GG_SJXL"))); //时间序列
		bean.setGG_SKJHMXBH(MyTools.StrFiltr(request.getParameter("GG_SKJHMXBH"))); //授课计划明细编号
		bean.setGG_LJXGBH(MyTools.StrFiltr(request.getParameter("GG_LJXGBH"))); //连节相关编号
		bean.setGG_ZT(MyTools.StrFiltr(request.getParameter("GG_ZT"))); //状态
		bean.setSKJHMXBH(MyTools.StrFiltr(request.getParameter("SKJHMXBH"))); //授课计划明细编号
		bean.setXZBDM(MyTools.StrFiltr(request.getParameter("XZBDM"))); //行政班代码
		bean.setXNXQ(MyTools.StrFiltr(request.getParameter("XNXQ"))); //学年学期
		bean.setKCJS(MyTools.StrFiltr(request.getParameter("KCJS"))); //课程教师
		bean.setJXXZ(MyTools.StrFiltr(request.getParameter("JXXZ"))); //教学性质
		bean.setQZQM(MyTools.StrFiltr(request.getParameter("QZQM"))); //期中期末
		bean.setQCXX(MyTools.StrFiltr(request.getParameter("QCXX"))); //清除的信息
		bean.setiUSERCODE(MyTools.StrFiltr(request.getParameter("iUSERCODE")));//用户编号
		bean.setKCAPZBBH(MyTools.StrFiltr(request.getParameter("KCAPZBBH")));//考场安排主表编号
		bean.setKCAPMXBH(MyTools.StrFiltr(request.getParameter("KCAPMXBH")));//考场安排明细表编号
		bean.setKSRQBH(MyTools.StrFiltr(request.getParameter("KSRQBH")));//考试日期编号
		bean.setEx_xnxq(MyTools.StrFiltr(request.getParameter("ex_xnxq"))); //学年学期
		bean.setEx_jxxz(MyTools.StrFiltr(request.getParameter("ex_jxxz"))); //教学性质
		bean.setEx_ksmc(MyTools.StrFiltr(request.getParameter("ex_ksmc"))); //考试名称
		bean.setEx_kslx(MyTools.StrFiltr(request.getParameter("ex_kslx"))); //考试类型
		bean.setEx_jzzs(MyTools.StrFiltr(request.getParameter("ex_jzzs"))); //上课截止周数
		bean.setEx_ksrq(MyTools.StrFiltr(request.getParameter("ex_ksrq"))); //考试日期
	}
}

