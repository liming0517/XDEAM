package com.pantech.devolop.registerScoreManage;
/**
编制日期：2016.02.04
创建人：yeq
模块名称：M7.1 学期成绩登记
说明:
	 
功能索引:
	1-查询
	2-设置
**/
import java.io.IOException;
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
import com.pantech.devolop.registerScoreSet.DfryszBean;

public class Svl_Xqcjdj extends HttpServlet {

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
		XqcjdjBean bean = new XqcjdjBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//查询科目列表
		if("queSubjectList".equalsIgnoreCase(active)){
			String XNXQMC_CX = MyTools.StrFiltr(request.getParameter("XNXQMC_CX"));
			String JXXZ_CX = MyTools.StrFiltr(request.getParameter("JXXZ_CX"));
			String ZYMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZYMC_CX")), "UTF-8");
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			String KCLX_CX = MyTools.StrFiltr(request.getParameter("KCLX_CX"));
			
			try {
				jsonV = bean.queSubjectList(pageNum, pageSize, XNXQMC_CX, JXXZ_CX, ZYMC_CX, NJDM_CX, KCMC_CX, KCLX_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询免考科目列表
		if("queMkSubjectList".equalsIgnoreCase(active)){
			String XNXQMC_CX = MyTools.StrFiltr(request.getParameter("XNXQMC_CX"));
			String JXXZ_CX = MyTools.StrFiltr(request.getParameter("JXXZ_CX"));
			String ZYMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZYMC_CX")), "UTF-8");
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			String KCLX_CX = MyTools.StrFiltr(request.getParameter("KCLX_CX"));
			
			try {
				jsonV = bean.queMkSubjectList(pageNum, pageSize, XNXQMC_CX, JXXZ_CX, ZYMC_CX, NJDM_CX, KCMC_CX, KCLX_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//初始化下拉框数据
		if("initComboData".equalsIgnoreCase(active)){
			try {
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询年级下拉框
				jsonV = bean.loadNJDMCombo();
				jal = JsonUtil.addJsonParams(jal, "njdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadXbdmCombo();
				jal = JsonUtil.addJsonParams(jal, "xbdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询专业下拉框
				jsonV = bean.loadMajorCombo();
				jal = JsonUtil.addJsonParams(jal, "zydmData", ((JSONArray)jsonV.get(2)).toString());
				
				jsonV = bean.loadWzcjCombo();
				jal = JsonUtil.addJsonParams(jal, "wzcjData", ((JSONArray)jsonV.get(2)).toString());
				
				jsonV = bean.loadWzcjShowCombo();
				jal = JsonUtil.addJsonParams(jal, "wzcjShowData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取班级下拉框数据
		if("loadClassCombo".equalsIgnoreCase(active)){
			String xbdm = MyTools.StrFiltr(request.getParameter("XBDM"));
			String zydm = MyTools.StrFiltr(request.getParameter("ZYDM"));
			String njdm = MyTools.StrFiltr(request.getParameter("NJDM"));
			String bjlx = MyTools.StrFiltr(request.getParameter("BJLX"));
			
			try {
				jsonV = bean.loadClassCombo(xbdm, zydm, njdm, bjlx);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询班级列表
		if("queClassList".equalsIgnoreCase(active)){
			String XNXQBM_CX = MyTools.StrFiltr(request.getParameter("XNXQBM_CX"));
			String XBDM_CX = MyTools.StrFiltr(request.getParameter("XBDM_CX"));
			String ZYDM_CX = MyTools.StrFiltr(request.getParameter("ZYDM_CX"));
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String BJDM_CX = MyTools.StrFiltr(request.getParameter("BJDM_CX"));
			String BJLX_CX = MyTools.StrFiltr(request.getParameter("BJLX_CX"));
			
			try {
				jsonV = bean.queClassList(pageNum, pageSize, XNXQBM_CX, XBDM_CX, ZYDM_CX, NJDM_CX, BJDM_CX, BJLX_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询免考班级列表
		if("queMkClassList".equalsIgnoreCase(active)){
			String XNXQBM_CX = MyTools.StrFiltr(request.getParameter("XNXQBM_CX"));
			String XBDM_CX = MyTools.StrFiltr(request.getParameter("XBDM_CX"));
			String ZYDM_CX = MyTools.StrFiltr(request.getParameter("ZYDM_CX"));
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String BJDM_CX = MyTools.StrFiltr(request.getParameter("BJDM_CX"));
			String BJLX_CX = MyTools.StrFiltr(request.getParameter("BJLX_CX"));
			
			try {
				jsonV = bean.queMkClassList(pageNum, pageSize, XNXQBM_CX, XBDM_CX, ZYDM_CX, NJDM_CX, BJDM_CX, BJLX_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询登分教师科目列表
		if("queTeaSubjectList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queTeaSubjectList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取文字成绩下拉框数据
		if("loadComboData".equalsIgnoreCase(active)){
			try {
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询年级下拉框
				jsonV = bean.loadNJDMCombo();
				jal = JsonUtil.addJsonParams(jal, "njdmData", ((JSONArray)jsonV.get(2)).toString());
				
				jsonV = bean.loadWzcjCombo();
				jal = JsonUtil.addJsonParams(jal, "wzcjData", ((JSONArray)jsonV.get(2)).toString());
				
				jsonV = bean.loadWzcjShowCombo();
				jal = JsonUtil.addJsonParams(jal, "wzcjShowData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询科目相关信息
		if("loadSubjectInfo".equalsIgnoreCase(active)){
			//String type = MyTools.StrFiltr(request.getParameter("type"));
			
			try {
				//添加学科学生成绩基础数据
				/*
				if("set".equalsIgnoreCase(type)){
					bean.addSubScoreInfo();
				}
				*/
				//20170110yeq修改查询是否可登分相关代码
				bean.addSubScoreInfo();
				
				jsonV = bean.loadSubjectInfo();
				jal = JsonUtil.addJsonParams(jal, "xnxqmc", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "zymc", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "kcmc", MyTools.StrFiltr(jsonV.get(2)));
//				jal = JsonUtil.addJsonParams(jal, "dfsj", MyTools.StrFiltr(jsonV.get(3)));
//				jal = JsonUtil.addJsonParams(jal, "dfqx", MyTools.StrFiltr(jsonV.get(4)));
//				jal = JsonUtil.addJsonParams(jal, "dysd", MyTools.StrFiltr(jsonV.get(5)));
				
				jsonV = bean.queClassTree();
				jal = JsonUtil.addJsonParams(jal, "classData", ((JSONArray)jsonV.get(2)).toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询免考科目相关信息
		if("loadMkSubjectInfo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadSubjectInfo();
				jal = JsonUtil.addJsonParams(jal, "xnxqmc", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "zymc", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "kcmc", MyTools.StrFiltr(jsonV.get(2)));
				
				jsonV = bean.queMkClassTree();
				jal = JsonUtil.addJsonParams(jal, "classData", ((JSONArray)jsonV.get(2)).toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询班级相关课程信息
		if("loadClassCourseInfo".equalsIgnoreCase(active)){
			String XNXQBM = MyTools.StrFiltr(request.getParameter("XNXQBM"));
			String BJDM = MyTools.StrFiltr(request.getParameter("BJDM"));
			String BJLX = MyTools.StrFiltr(request.getParameter("BJLX"));
			
			try {
				//添加学生成绩基础信息
				bean.addClassSubScoreInfo(XNXQBM, BJDM, BJLX);
				
				jsonV = bean.loadClassCourseTree(XNXQBM, BJDM, BJLX);
				jal = JsonUtil.addJsonParams(jal, "classData", ((JSONArray)jsonV.get(2)).toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询班级相关免考课程信息
		if("loadClassMkCourseInfo".equalsIgnoreCase(active)){
			String XNXQBM = MyTools.StrFiltr(request.getParameter("XNXQBM"));
			String BJDM = MyTools.StrFiltr(request.getParameter("BJDM"));
			String BJLX = MyTools.StrFiltr(request.getParameter("BJLX"));
			
			try {
				jsonV = bean.loadClassMkCourseTree(XNXQBM, BJDM, BJLX);
				jal = JsonUtil.addJsonParams(jal, "classData", ((JSONArray)jsonV.get(2)).toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询当前课程是否可登分状态
		if("loadLockState".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadLockState();
				jal = JsonUtil.addJsonParams(jal, "dfsj", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "dfqx", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "dysd", MyTools.StrFiltr(jsonV.get(2)));
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询登分配置信息
		if("loadDfConfig".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadDfConfig();
				jal = JsonUtil.addJsonParams(jal, "kslx", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "kslxmc", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "zpblxx", MyTools.StrFiltr(jsonV.get(2)));
				jal = JsonUtil.addJsonParams(jal, "psbl", MyTools.StrFiltr(jsonV.get(3)));
				jal = JsonUtil.addJsonParams(jal, "qzbl", MyTools.StrFiltr(jsonV.get(4)));
				jal = JsonUtil.addJsonParams(jal, "sxbl", MyTools.StrFiltr(jsonV.get(5)));
				jal = JsonUtil.addJsonParams(jal, "qmbl", MyTools.StrFiltr(jsonV.get(6)));
				jal = JsonUtil.addJsonParams(jal, "cjlx", MyTools.StrFiltr(jsonV.get(7)));
				jal = JsonUtil.addJsonParams(jal, "cjlxmc", MyTools.StrFiltr(jsonV.get(8)));
				jal = JsonUtil.addJsonParams(jal, "sx", MyTools.StrFiltr(jsonV.get(9)));
				jal = JsonUtil.addJsonParams(jal, "sxmc", MyTools.StrFiltr(jsonV.get(10)));
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//保存登分设置
		if("saveDfsz".equalsIgnoreCase(active)){
			String updateInfo = MyTools.StrFiltr(request.getParameter("updateInfo"));
			
			try {
				if(updateInfo.length() > 0){
					bean.saveStuScore(updateInfo);
				}
				bean.saveDfsz();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取当前选择班级课程的学生列表
		if("loadStuList".equalsIgnoreCase(active)){
			try {
				String result = bean.loadStuList();
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取当前选择班级课程的免考学生列表
		if("loadMkStuList".equalsIgnoreCase(active)){
			try {
				String result = bean.loadMkStuList();
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存成绩
		if("saveStuScore".equalsIgnoreCase(active) || "saveStuScoreTimer".equalsIgnoreCase(active)){
			String updateInfo = MyTools.StrFiltr(request.getParameter("updateInfo"));
			
			try {
				bean.saveStuScore(updateInfo);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取考分统计信息
		if("loadKftj".equalsIgnoreCase(active)){
			String countType = MyTools.StrFiltr(request.getParameter("countType"));
			try {
				String result = bean.loadKftj(countType);
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		//导出
		if("export".equalsIgnoreCase(active)){
			try {
				String filePath = bean.scoreExport();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				
			}
		}
		//导入
		if("".equalsIgnoreCase(active)){
			ServletConfig sc = this.getServletConfig();
			SmartUpload mySmartUpload = new SmartUpload("UTF-8");
			mySmartUpload.initialize(sc, request, response);
			try {
				mySmartUpload.upload();
			} catch (SmartUploadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String active2=MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("active2"));// 拿取前台的active值
			if ("Savestudentimportxls".equalsIgnoreCase(active2)) {
				try {
					String xkmc=MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("CK_XKMC"));// 学科名称
					bean.Savestudentimportxls(mySmartUpload,xkmc);
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					jal = JsonUtil.addJsonParams(jal, "PromptMsg", bean.getMSG2());
					response.getWriter().write(jal.toString());
				} catch (Exception e) {
							
					e.printStackTrace();
				}
			}
		}
		
		//查询免考学生成绩列表
		if("queMkStuScoreList".equalsIgnoreCase(active)){
			String PLDF_XNXQ = MyTools.StrFiltr(request.getParameter("PLDF_XNXQ"));
			String PLDF_CJLX = MyTools.StrFiltr(request.getParameter("PLDF_CJLX"));
			
			try {
				jsonV = bean.queMkClassList(pageNum, pageSize, PLDF_XNXQ, PLDF_CJLX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//自动计算免考学生成绩
		if("autoCount".equalsIgnoreCase(active)){
			String PLDF_XNXQ = MyTools.StrFiltr(request.getParameter("PLDF_XNXQ"));
			String PLDF_CJLX = MyTools.StrFiltr(request.getParameter("PLDF_CJLX"));
			String PLDF_PSZB = MyTools.StrFiltr(request.getParameter("PLDF_PSZB"));
			String PLDF_QZZB = MyTools.StrFiltr(request.getParameter("PLDF_QZZB"));
			String PLDF_QMZB = MyTools.StrFiltr(request.getParameter("PLDF_QMZB"));
			
			try {
				bean.autoCount(PLDF_XNXQ, PLDF_CJLX, PLDF_PSZB, PLDF_QZZB, PLDF_QMZB);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
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
    * @param XqcjdjBean
    */
	private void getFormData(HttpServletRequest request,XqcjdjBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCX_ID(MyTools.StrFiltr(request.getParameter("CX_ID"))); //编号
		bean.setCX_XH(MyTools.StrFiltr(request.getParameter("CX_XH"))); //学号
		bean.setCX_XM(MyTools.StrFiltr(request.getParameter("CX_XM"))); //姓名
		bean.setCX_XGBH(MyTools.StrFiltr(request.getParameter("CX_XGBH"))); //相关编号
		bean.setCX_PS(MyTools.StrFiltr(request.getParameter("CX_PS"))); //平时
		bean.setCX_QZ(MyTools.StrFiltr(request.getParameter("CX_QZ"))); //期中
		bean.setCX_SX(MyTools.StrFiltr(request.getParameter("CX_SX"))); //实训
		bean.setCX_QM(MyTools.StrFiltr(request.getParameter("CX_QM"))); //期末
		bean.setCX_ZP(MyTools.StrFiltr(request.getParameter("CX_ZP"))); //总评
		bean.setCX_CX1(MyTools.StrFiltr(request.getParameter("CX_CX1"))); //重修1
		bean.setCX_CX2(MyTools.StrFiltr(request.getParameter("CX_CX2"))); //重修2
		bean.setCX_BK(MyTools.StrFiltr(request.getParameter("CX_BK"))); //补考
		bean.setCX_DBK(MyTools.StrFiltr(request.getParameter("CX_DBK"))); //大补考
		bean.setCX_DYCJ(MyTools.StrFiltr(request.getParameter("CX_DYCJ"))); //打印成绩
		bean.setCX_CJZT(MyTools.StrFiltr(request.getParameter("CX_CJZT"))); //成绩状态
		bean.setCX_CJR(MyTools.StrFiltr(request.getParameter("CX_CJR"))); //创建人
		bean.setCX_CJSJ(MyTools.StrFiltr(request.getParameter("CX_CJSJ"))); //创建时间
		bean.setCX_ZT(MyTools.StrFiltr(request.getParameter("CX_ZT"))); //状态
		
		bean.setCK_KSLX(MyTools.StrFiltr(request.getParameter("CK_KSLX"))); //考试类型
		bean.setCK_ZPBLXX(MyTools.StrFiltr(request.getParameter("CK_ZPBLXX"))); //总评比例选项
		bean.setCK_PSBL(MyTools.StrFiltr(request.getParameter("CK_PSBL"))); //平时比例
		bean.setCK_QZBL(MyTools.StrFiltr(request.getParameter("CK_QZBL"))); //期中比例
		bean.setCK_SXBL(MyTools.StrFiltr(request.getParameter("CK_SXBL"))); //实训比例
		bean.setCK_QMBL(MyTools.StrFiltr(request.getParameter("CK_QMBL"))); //期末比例
		bean.setCK_CJLX(MyTools.StrFiltr(request.getParameter("CK_CJLX"))); //成绩类型
		bean.setCK_SX(MyTools.StrFiltr(request.getParameter("CK_SX"))); //实训
	}
}