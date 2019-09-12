package com.pantech.devolop.registerScoreManage;

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
import com.zhuozhengsoft.pageoffice.PageOfficeLink;

public class Svl_Cjcx extends HttpServlet {
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
		CjcxBean bean = new CjcxBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询当前学年
				String curXnxq = bean.loadCurXn();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
				
				//查询查分时间
				jsonV = bean.loadQueryConfig();
				jal = JsonUtil.addJsonParams(jal, "kfxnxq", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "kfcjlx", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "beginDate", MyTools.StrFiltr(jsonV.get(2)));
				jal = JsonUtil.addJsonParams(jal, "endDate", MyTools.StrFiltr(jsonV.get(3)));
				
				//查询年级下拉框
				jsonV = bean.loadNJDMCombo();
				jal = JsonUtil.addJsonParams(jal, "njdmData", ((JSONArray)jsonV.get(2)).toString());
				//查询学年学期下拉框
				jsonV = bean.loadSemCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学年学期下拉框
				jsonV = bean.loadExportSemCombo();
				jal = JsonUtil.addJsonParams(jal, "exportXnxqData", ((JSONArray)jsonV.get(2)).toString());
				//查询系部下拉框
				jsonV = bean.loadExportXbCombo();
				jal = JsonUtil.addJsonParams(jal, "exportXbData", ((JSONArray)jsonV.get(2)).toString());
				//查询年级下拉框
				jsonV = bean.loadExportNjCombo();
				jal = JsonUtil.addJsonParams(jal, "exportNjData", ((JSONArray)jsonV.get(2)).toString());
				//查询专业下拉框
				jsonV = bean.loadExportZyCombo();
				jal = JsonUtil.addJsonParams(jal, "exportZyData", ((JSONArray)jsonV.get(2)).toString());
				//查询招生类别下拉框
				jsonV = bean.loadExportStuTypeCombo();
				jal = JsonUtil.addJsonParams(jal, "exportStuTypeData", ((JSONArray)jsonV.get(2)).toString());
				//查询学科名称下拉框
				jsonV = bean.loadExportSubCombo();
				jal = JsonUtil.addJsonParams(jal, "exportSubData", ((JSONArray)jsonV.get(2)).toString());
				//查询班级下拉框
				//jsonV = bean.loadExportBjCombo();
				//jal = JsonUtil.addJsonParams(jal, "exportBjData", ((JSONArray)jsonV.get(2)).toString());
				//查询班级下拉框
				jsonV = bean.loadPrintBjCombo();
				jal = JsonUtil.addJsonParams(jal, "printBjData", ((JSONArray)jsonV.get(2)).toString());
				
				jsonV = bean.loadWzcjShowCombo();
				jal = JsonUtil.addJsonParams(jal, "wzcjShowData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询导出班级下拉框
		if("loadExportBjCombo".equalsIgnoreCase(active)){
			String exportXb = MyTools.StrFiltr(request.getParameter("exportXb"));
			String exportNj = MyTools.StrFiltr(request.getParameter("exportNj"));
			String exportZy = MyTools.StrFiltr(request.getParameter("exportZy"));
			try {
				jsonV = bean.loadExportBjCombo(exportXb, exportNj, exportZy);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询打印教师下拉框
		if("loadPrintJsCombo".equalsIgnoreCase(active)){
			try {
				String result = bean.loadPrintJsCombo();
				response.getWriter().write(result);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询打印学生下拉框
		if("loadPrintXsCombo".equalsIgnoreCase(active)){
			String stuState = MyTools.StrFiltr(request.getParameter("stuState"));
			
			try {
				jsonV = bean.loadPrintXsCombo(stuState);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取成绩列表数据
		if("queScoreList".equalsIgnoreCase(active)){
			String type = MyTools.StrFiltr(request.getParameter("type"));
			bean.setSTUCODE(URLDecoder.decode(bean.getSTUCODE(), "UTF-8"));
			bean.setSTUNAME(URLDecoder.decode(bean.getSTUNAME(), "UTF-8"));
			bean.setZYMC(URLDecoder.decode(bean.getZYMC(), "UTF-8"));
			bean.setBJMC(URLDecoder.decode(bean.getBJMC(), "UTF-8"));
			bean.setKCMC(URLDecoder.decode(bean.getKCMC(), "UTF-8"));
			bean.setJSXM(URLDecoder.decode(bean.getJSXM(), "UTF-8"));
			
			try {
				jsonV = bean.queScoreList(pageNum, pageSize, type);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存修改的学生成绩
		if("saveScore".equalsIgnoreCase(active)){
			try {
				bean.saveScore();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//授课成绩成绩导出
		if("skcjExport".equalsIgnoreCase(active)){
			String qsxnxq = MyTools.StrFiltr(request.getParameter("export_startSem"));
			String jsxnxq = MyTools.StrFiltr(request.getParameter("export_endSem"));
			String xb = MyTools.StrFiltr(request.getParameter("export_dept"));
			String nj = MyTools.StrFiltr(request.getParameter("export_grade"));
			String zy = MyTools.StrFiltr(request.getParameter("export_major"));
			String bj = MyTools.StrFiltr(request.getParameter("export_class"));
			String cjfw = MyTools.StrFiltr(request.getParameter("export_range"));
			String zbwdfFlag = MyTools.StrFiltr(request.getParameter("export_zbwdfFlag"));
			String cjlx = MyTools.StrFiltr(request.getParameter("export_scoreType"));
			String zslb = MyTools.StrFiltr(request.getParameter("export_stuType"));
			String xkmc = MyTools.StrFiltr(request.getParameter("export_subject"));
			
			try {
				String filePath = bean.scoreExport(qsxnxq, jsxnxq, xb, nj, zy, bj, cjfw, zbwdfFlag, cjlx, zslb, xkmc);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//汇总成绩成绩导出
		if("hzcjExport".equalsIgnoreCase(active)){
			String qsxnxq = MyTools.StrFiltr(request.getParameter("export_startSem"));
			String jsxnxq = MyTools.StrFiltr(request.getParameter("export_endSem"));
			String xb = MyTools.StrFiltr(request.getParameter("export_dept"));
			String nj = MyTools.StrFiltr(request.getParameter("export_grade"));
			String zy = MyTools.StrFiltr(request.getParameter("export_major"));
			String bj = MyTools.StrFiltr(request.getParameter("export_class"));
			String cjfw = MyTools.StrFiltr(request.getParameter("export_range"));
			String zbwdfFlag = MyTools.StrFiltr(request.getParameter("export_zbwdfFlag"));
			String cjlx = MyTools.StrFiltr(request.getParameter("export_scoreType"));
			String zslb = MyTools.StrFiltr(request.getParameter("export_stuType"));
			String xkmc = MyTools.StrFiltr(request.getParameter("export_subject"));
			
			try {
				String filePath = bean.hzcjExport(qsxnxq, jsxnxq, xb, nj, zy, bj, cjfw, zbwdfFlag, cjlx, zslb, xkmc);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//整班未登分信息成绩导出
		if("zbwdfExport".equalsIgnoreCase(active)){
			String qsxnxq = MyTools.StrFiltr(request.getParameter("export_qsxnxq"));//起始学年学期
			String jsxnxq = MyTools.StrFiltr(request.getParameter("export_jsxqxq"));//结束学年学期
			String nj = MyTools.StrFiltr(request.getParameter("export_nj"));//年级
			String xb = MyTools.StrFiltr(request.getParameter("export_xb"));//系部
			String zy = MyTools.StrFiltr(request.getParameter("export_zy"));//专业
			//String kszc = MyTools.StrFiltr(request.getParameter("export_range"));
			
			try {
				String filePath = bean.zbwdfExport(qsxnxq, jsxnxq, nj, xb,zy);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取开课班下拉框数据
		if("loadPrintKkbCombo".equalsIgnoreCase(active)){
			String xnxq = MyTools.StrFiltr(request.getParameter("xnxq"));
			String teaCode = MyTools.StrFiltr(request.getParameter("jsxm"));
			
			try {
				jsonV = bean.loadPrintKkbCombo(xnxq, teaCode);
				jal = (JSONArray) jsonV.get(2);
				JSONArray.fromObject(jsonV.get(2));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//锁定
		if("lockScore".equalsIgnoreCase(active)){
			try {
				bean.lockScore();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//解锁
		if("unlock".equalsIgnoreCase(active)){
			try {
				bean.unlock();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//删除打印预览文件
		if("delViewFile".equalsIgnoreCase(active)){
			String filePath = MyTools.StrFiltr(request.getParameter("filePath"));
			
			try {
				boolean flag = bean.deleteFile(filePath);
				if(flag)
					bean.setMSG("删除成功");
				else
					bean.setMSG("删除失败");
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//保存查分设置
		if("saveConfig".equalsIgnoreCase(active)){
			String kfxnxq = MyTools.StrFiltr(request.getParameter("kfxnxq"));
			String kfcjlx = MyTools.StrFiltr(request.getParameter("kfcjlx"));
			String beginDate = MyTools.StrFiltr(request.getParameter("beginDate"));
			String endDate = MyTools.StrFiltr(request.getParameter("endDate"));
			
			try {
				bean.saveConfig(kfxnxq, kfcjlx, beginDate, endDate);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询学年学期下拉框
		if("loadXnxqCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadSemCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取学年学期列表数据
		if("queXnxqList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queXnxqList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取学年列表数据
		if("queXnList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queXnList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取补考信息列表数据
		if("queBkInfoList".equalsIgnoreCase(active)){
			bean.setSTUCODE(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUCODE")), "UTF-8")); //学号
			bean.setSTUNAME(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUNAME")), "UTF-8")); //姓名
			bean.setBJMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("BJMC")), "UTF-8")); //班级名称
			bean.setKCMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC")), "UTF-8")); //课程名称
			String zbwdfFlag = MyTools.StrFiltr(request.getParameter("zbwdfFlag"));
			
			try {
				jsonV = bean.queBkInfoList(pageNum, pageSize, zbwdfFlag);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//补考考试名册导出
		if("bkmdExport".equalsIgnoreCase(active)){
			String zbwdfFlag = MyTools.StrFiltr(request.getParameter("zbwdfFlag"));
			
			try {
				String filePath = bean.bkmdExport(zbwdfFlag);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取学年下拉框数据
		if("loadXnCombo".equalsIgnoreCase(active)){
			try {
				//查询学年下拉框
				jsonV = bean.loadXnCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取学年下拉框数据
		if("loadYearCombo".equalsIgnoreCase(active)){
			try {
				//查询学年下拉框
				jsonV = bean.loadJdXnCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取学年学期下拉框数据
		if("loadzbXnxQCombo".equalsIgnoreCase(active)){
			try {
				//查询学年下拉框
				jsonV = bean.loadzbXnXxCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取留级名单列表数据
		if("queLjmdList".equalsIgnoreCase(active)){
			String type = MyTools.StrFiltr(request.getParameter("type"));
			try {
				jsonV = bean.queLjmdList(type, pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取不及格科目详情列表数据
		if("queBjgDetialList".equalsIgnoreCase(active)){
			String type = MyTools.StrFiltr(request.getParameter("type"));
			try {
				jsonV = bean.queBjgDetialList(type, pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//留级名单导出
		if("ljmdExport".equalsIgnoreCase(active)){
			String type = MyTools.StrFiltr(request.getParameter("type"));
			try {
				String filePath = bean.ljmdExport(type);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//未打印的课程信息导出
		if("wdyExport".equalsIgnoreCase(active)){
			try {
				String filePath = bean.wdyExport();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//修改成绩状态
		if("changeStuScoreState".equalsIgnoreCase(active)){
			try {
				bean.changeStuScoreState();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//同步查分数据
		if("syncData".equalsIgnoreCase(active)){
			try {
				bean.syncData();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取无效成绩信息列表数据
		if("queInvalidScoreList".equalsIgnoreCase(active)){
			bean.setKCMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC")), "UTF-8")); //课程名称
			bean.setSTUCODE(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUCODE")), "UTF-8")); //学号
			bean.setSTUNAME(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUNAME")), "UTF-8")); //姓名
			
			try {
				jsonV = bean.queInvalidScoreList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//恢复成绩信息状态
		if("recoveryScore".equalsIgnoreCase(active)){
			try {
				bean.recoveryScoreInfo();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//删除成绩信息
//		if("delScore".equalsIgnoreCase(active)){
//			try {
//				bean.delScoreInfo();
//				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
//				response.getWriter().write(jal.toString());
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
		
		//读取绩点统计专业信息列表
		if("queJdMajorList".equalsIgnoreCase(active)){
			bean.setZYMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZYMC")), "UTF-8")); //学号
			
			try {
				jsonV = bean.queJdMajorList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取绩点统计详情列表数据
		if("queJdInfoList".equalsIgnoreCase(active)){
			bean.setSTUCODE(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUCODE")), "UTF-8")); //学号
			bean.setSTUNAME(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUNAME")), "UTF-8")); //姓名
			bean.setBJMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("BJMC")), "UTF-8")); //班级名称
			
			try {
				jsonV = bean.queJdInfoList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//绩点统计导出
		if("jdtjExport".equalsIgnoreCase(active)){
			try {
				String filePath = bean.jdtjExport();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//学生成绩单导出
		if("exportStuReport".equalsIgnoreCase(active)){
			String qsxnxq = MyTools.StrFiltr(request.getParameter("QSXNXQ"));
			String jsxnxq = MyTools.StrFiltr(request.getParameter("JSXNXQ"));
			
			try {
				String filePath = bean.exportStuReport(qsxnxq, jsxnxq);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取大补考信息列表数据
		if("queDbkInfoList".equalsIgnoreCase(active)){
			bean.setSTUCODE(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUCODE")), "UTF-8")); //学号
			bean.setSTUNAME(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUNAME")), "UTF-8")); //姓名
			bean.setBJMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("BJMC")), "UTF-8")); //班级名称
			bean.setKCMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC")), "UTF-8")); //课程名称
			String zbwdfFlag = MyTools.StrFiltr(request.getParameter("zbwdfFlag"));
			String yearRange = MyTools.StrFiltr(request.getParameter("yearRange"));
			
			try {
				jsonV = bean.queDbkInfoList(pageNum, pageSize, zbwdfFlag, yearRange);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//大补考考试名册导出
		if("dbkmdExport".equalsIgnoreCase(active)){
			String zbwdfFlag = MyTools.StrFiltr(request.getParameter("zbwdfFlag"));
			String yearRange = MyTools.StrFiltr(request.getParameter("yearRange"));
			
			try {
				String filePath = bean.dbkmdExport(zbwdfFlag, yearRange);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//查询覆盖成绩的科目下拉框数据
		if("loadCoverSubject".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadCoverSubject();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取成绩列表数据
		if("queStuScoreList".equalsIgnoreCase(active)){
			String subName = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("score_subject")), "UTF-8");
			String scoreType = MyTools.StrFiltr(request.getParameter("score_type"));
			String scoreStart = MyTools.StrFiltr(request.getParameter("score_start"));
			String scoreEnd = MyTools.StrFiltr(request.getParameter("score_end"));
			String scoreTarget = MyTools.StrFiltr(request.getParameter("score_target"));
			String stuStart = MyTools.StrFiltr(request.getParameter("stu_start"));
			String stuEnd = MyTools.StrFiltr(request.getParameter("stu_end"));
			
			try {
				jsonV = bean.queStuScoreList(pageNum, pageSize, subName, scoreType, scoreStart, scoreEnd, scoreTarget, stuStart, stuEnd);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//覆盖成绩
		if("confirmScoreCover".equalsIgnoreCase(active)){
			String subName = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("score_subject")), "UTF-8");
			String scoreType = MyTools.StrFiltr(request.getParameter("score_type"));
			String scoreStart = MyTools.StrFiltr(request.getParameter("score_start"));
			String scoreEnd = MyTools.StrFiltr(request.getParameter("score_end"));
			String scoreTarget = MyTools.StrFiltr(request.getParameter("score_target"));
			String stuStart = MyTools.StrFiltr(request.getParameter("stu_start"));
			String stuEnd = MyTools.StrFiltr(request.getParameter("stu_end"));
			
			try {
				bean.confirmScoreCover(subName, scoreType, scoreStart, scoreEnd, scoreTarget, stuStart, stuEnd);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取分卷学科信息列表数据
		if("queSubInfoList".equalsIgnoreCase(active)){
			bean.setXNXQ(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XNXQ")), "UTF-8"));
			bean.setKCMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC")), "UTF-8"));
			String examType = MyTools.StrFiltr(request.getParameter("examType"));
			try {
				jsonV = bean.queSubInfoList(pageNum, pageSize, examType);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取可添加的学科下拉框数据
		if("loadSubCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadSubCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//保存分卷学科
		if("saveSub".equalsIgnoreCase(active)){
			String mainCode = MyTools.StrFiltr(request.getParameter("mainCode"));
			String examType = MyTools.StrFiltr(request.getParameter("examType"));
			String subName = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("subName")), "UTF-8");
			
			try {
				bean.saveSub(mainCode, examType, subName);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//删除分卷学科
		if("delSub".equalsIgnoreCase(active)){
			try {
				bean.delSub();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取分卷学科详情信息列表数据
		if("queSubDetailList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queSubDetailList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取可添加的专业下拉框数据
		if("loadMajorCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadMajorCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//保存分卷学科明细
		if("saveSubDetail".equalsIgnoreCase(active)){
			String mainCode = MyTools.StrFiltr(request.getParameter("mainCode"));
			String detailCode = MyTools.StrFiltr(request.getParameter("detailCode"));
			String majorCode = MyTools.StrFiltr(request.getParameter("majorCode"));
			String majorName = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("majorName")), "UTF-8");
			
			try {
				bean.saveSubDetail(mainCode, detailCode, majorCode, majorName);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//删除分卷学科明细信息
		if("delSubDetail".equalsIgnoreCase(active)){
			try {
				bean.delSubDetail();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//查询删除整班成绩中班级信息
		if("loadDelClassCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadDelClassCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询删除整班成绩中班级相关的课程
		if("loadDelCourseCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadDelCourseCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取课程整班学生成绩信息
		if("queDelClassScoreList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queDelClassScoreList();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//批量修改成绩状态
		if("changeMultiStuScoreState".equalsIgnoreCase(active)){
			try {
				bean.changeMultiStuScoreState();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		if("initBkCombo".equalsIgnoreCase(active)){
			try {
				//查询学年学期下拉框
				jsonV = bean.loadSemCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				//查询年级下拉框
				jsonV = bean.loadExportNjCombo();
				jal = JsonUtil.addJsonParams(jal, "exportNjData", ((JSONArray)jsonV.get(2)).toString());
				//查询专业下拉框
				jsonV = bean.loadExportZyCombo();
				jal = JsonUtil.addJsonParams(jal, "exportZyData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("loadTeaPageOfficeLink".equalsIgnoreCase(active)){
			String xnxq = MyTools.StrFiltr(request.getParameter("XNXQ"));
			String jsxm = MyTools.StrFiltr(request.getParameter("JSXM"));
			String kkb = MyTools.StrFiltr(request.getParameter("KKB"));
			
			try {
				String linkStr = PageOfficeLink.openWindow(request, "form/registerScoreManage/scorePrintView.jsp?XNXQ=" + xnxq + 
						"&JSXM=" + jsxm + "&KKB=" + kkb,"width=1000px;height=800px;");
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("loadStuPageOfficeLink".equalsIgnoreCase(active)){
			String qsxnxq = MyTools.StrFiltr(request.getParameter("QSXNXQ"));
			String jsxnxq = MyTools.StrFiltr(request.getParameter("JSXNXQ"));
			String bjbh = MyTools.StrFiltr(request.getParameter("BJBH"));
			String stuState = MyTools.StrFiltr(request.getParameter("STUSTATE"));
			String xh = MyTools.StrFiltr(request.getParameter("STUCODE"));
			
			try {
				String linkStr = PageOfficeLink.openWindow(request, "form/registerScoreManage/stuReportPrintView.jsp?QSXNXQ=" + qsxnxq + 
						"&JSXNXQ=" + jsxnxq + "&BJBH=" + bjbh + "&STUSTATE=" + stuState + "&STUCODE=" + xh,"width=1000px;height=800px;");
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//整班未登分导出页面初始化
		if("queZbwdfList".equalsIgnoreCase(active)){
			String xnxq = MyTools.StrFiltr(request.getParameter("XNXQ"));
			try {
				jsonV = bean.queZbwdf(pageNum, pageSize,xnxq);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
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
    * @param CjcxBean
    */
	private void getFormData(HttpServletRequest request, CjcxBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAuth(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCODE(MyTools.StrFiltr(request.getParameter("iKeyCode"))); //编号
		bean.setSTUCODE(MyTools.StrFiltr(request.getParameter("STUCODE"))); //学号
		bean.setSTUNAME(MyTools.StrFiltr(request.getParameter("STUNAME"))); //姓名
		bean.setNJDM(MyTools.StrFiltr(request.getParameter("NJDM"))); //年级代码
		bean.setZYMC(MyTools.StrFiltr(request.getParameter("ZYMC"))); //专业名称
		bean.setBJMC(MyTools.StrFiltr(request.getParameter("BJMC"))); //班级名称
		bean.setXNXQ(MyTools.StrFiltr(request.getParameter("XNXQ"))); //学年学期
		bean.setKCMC(MyTools.StrFiltr(request.getParameter("KCMC"))); //课程名称
		bean.setJSXM(MyTools.StrFiltr(request.getParameter("JSXM"))); //教师姓名
		bean.setCJFW(MyTools.StrFiltr(request.getParameter("CJFW"))); //成绩范围
		bean.setCJLX(MyTools.StrFiltr(request.getParameter("CJLX"))); //查询成绩范围成绩类型
		bean.setPSCJ(MyTools.StrFiltr(request.getParameter("PSCJ"))); //平时成绩
		bean.setQZCJ(MyTools.StrFiltr(request.getParameter("QZCJ"))); //期中成绩
		bean.setSXCJ(MyTools.StrFiltr(request.getParameter("SXCJ"))); //实训成绩
		bean.setQMCJ(MyTools.StrFiltr(request.getParameter("QMCJ"))); //期末成绩
		bean.setZPCJ(MyTools.StrFiltr(request.getParameter("ZPCJ"))); //总评成绩
		bean.setCXCJ1(MyTools.StrFiltr(request.getParameter("CXCJ1"))); //重修成绩1
		bean.setCXCJ2(MyTools.StrFiltr(request.getParameter("CXCJ2"))); //重修成绩2
		bean.setBKCJ(MyTools.StrFiltr(request.getParameter("BKCJ"))); //补考成绩
		bean.setDBKCJ(MyTools.StrFiltr(request.getParameter("DBKCJ"))); //大补考成绩
		bean.setDYCJ(MyTools.StrFiltr(request.getParameter("DYCJ"))); //打印成绩
	}
}