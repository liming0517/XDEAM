package com.pantech.devolop.customExamManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class Svl_Cjdj extends HttpServlet {

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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page")); // 获得页面page参数
		int pageSize = MyTools.parseInt(request.getParameter("rows")); // 获得页面rows参数
		Vector jsonV = null;// 返回结果集
		JSONArray jal = null;// 返回json对象
		CjdjBean bean = new CjdjBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//获取当前学年学期
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
						
				String curXn = "";
				String curXq = "";
				if(!"".equalsIgnoreCase(curXnxq)){
					curXn = curXnxq.substring(0, 4);
					curXq = curXnxq.substring(4, 5);
				}
						
				//查询考试列表
				jsonV = bean.queryRec(pageNum, pageSize, curXn, curXq , "" , "");
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				//查询学年下拉框
				jsonV = bean.loadXnCombo();
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
						
				//查询学期下拉框
				jsonV = bean.loadXqCombo();
				jal = JsonUtil.addJsonParams(jal, "xqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询类别下拉框
				jsonV = bean.loadLbCombo();
				jal = JsonUtil.addJsonParams(jal, "lbData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询类别下拉框
				jsonV = bean.loadDJCombo();
				jal = JsonUtil.addJsonParams(jal, "djData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 查询考试
		if (active.equalsIgnoreCase("queryRec")) {
			String XN_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_XN")),"UTF-8");//学年
			String XQ_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_XQ")),"UTF-8");//学期
			String KSMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_KSMC")), "UTF-8");//考试名称
			String KSLB_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CJ_KSLB")),"UTF-8");//类别
					
			try {
				//查询教师列表
				jsonV = bean.queryRec(pageNum, pageSize, XN_CX, XQ_CX, KSMC_CX, KSLB_CX);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询考试班级信息
		if("loadExamClassTree".equalsIgnoreCase(active)){
			String level = MyTools.StrFiltr(request.getParameter("level"));
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			String result = "";
					
			try {
				jsonV = bean.loadExamClassTree(level, parentCode);
				if("0".equalsIgnoreCase(level))
					result = MyTools.StrFiltr(jsonV.get(0));
				else
					result = ((JSONArray)jsonV.get(2)).toString();
						
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//读取班级学生成绩相关信息
		if("loadExamInfo".equalsIgnoreCase(active)){
			String dfFlag = MyTools.StrFiltr(request.getParameter("dfFlag"));
			try {
				jsonV = bean.loadExamInfo(dfFlag);
				jal = JsonUtil.addJsonParams(jal, "colData", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "listData", MyTools.StrFiltr(jsonV.get(1)));
				System.out.println("jal::::"+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		//保存成绩信息
		if("saveScore".equalsIgnoreCase(active)){
			String updateScore = MyTools.StrFiltr(request.getParameter("updateScore"));
					
			try {
				bean.saveScore(updateScore);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//班级学生成绩导出
		if("ExportExcel".equalsIgnoreCase(active)){
			String examName = MyTools.StrFiltr(request.getParameter("examName"));
			String className = MyTools.StrFiltr(request.getParameter("className"));
			
			String dfFlag = MyTools.StrFiltr(request.getParameter("dfFlag"));
			try {
				String filePath = bean.ExportExcelClassScore(examName ,className ,dfFlag);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath",filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		// 班级学生成绩导入
		if("".equalsIgnoreCase(active)){
			ServletConfig sc = this.getServletConfig();
			SmartUpload mySmartUpload = new SmartUpload("UTF-8");
			mySmartUpload.initialize(sc, request, response);
			try {
				mySmartUpload.upload();
			} catch (SmartUploadException exception1) {
					// TODO 自动生成 catch 块
				exception1.printStackTrace();
			}
			String activedr=MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("activedr"));// 拿取前台的active值
			// 学生信息设置导入
			if ("saveimportxlsZSKSGL".equalsIgnoreCase(activedr)) {
				String examCode = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("examCode"));
				String classCode = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("classCode"));
				String usercode =MyTools.getSessionUserCode(request); // 用户编号
				String authcode=MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("authcode")); //用户权限
				
				try {
					bean.saveimportxlsZSKSGL(mySmartUpload, examCode, classCode, usercode, authcode);
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					response.getWriter().write(jal.toString());
				} catch (Exception e) {
									
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 从界面获取参数
	 * 
	 * @date
	 * @author:
	 * @param request
	 * @param KsszBean
	 */
	private void getFormData(HttpServletRequest request, CjdjBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		bean.setAUTHCODE(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		
		bean.setCJ_KSBH(MyTools.StrFiltr(request.getParameter("CJ_KSBH")));// 考试编号
		bean.setCJ_BH(MyTools.StrFiltr(request.getParameter("CJ_BH")));// 编号
		bean.setCJ_XH(MyTools.StrFiltr(request.getParameter("CJ_XH")));// 学号
		bean.setCJ_XM(MyTools.StrFiltr(request.getParameter("CJ_XM")));// 姓名
		bean.setCJ_KSXKBH(MyTools.StrFiltr(request.getParameter("CJ_KSXKBH")));// 考试学科编号
		bean.setCJ_KCDM(MyTools.StrFiltr(request.getParameter("CJ_KCDM")));// 课程代码
		bean.setCJ_BJDM(MyTools.StrFiltr(request.getParameter("CJ_BJDM")));// 班级代码
		
		bean.setCJ_CJ(MyTools.StrFiltr(request.getParameter("CJ_CJ")));// 成绩
		bean.setCJ_CJR(MyTools.StrFiltr(request.getParameter("CJ_CJR")));// 创建人
		bean.setCJ_CJSJ(MyTools.StrFiltr(request.getParameter("CJ_CJSJ")));// 创建时间
		bean.setCJ_ZT(MyTools.StrFiltr(request.getParameter("CJ_ZT")));// 状态
		
		bean.setCJ_XN(MyTools.StrFiltr(request.getParameter("CJ_XN")));// 学年
		bean.setCJ_XQ(MyTools.StrFiltr(request.getParameter("CJ_XQ")));// 学期
		bean.setCJ_XNXQBM(MyTools.StrFiltr(request.getParameter("CJ_XNXQBM")));//  学年学期编码
		
	}
}