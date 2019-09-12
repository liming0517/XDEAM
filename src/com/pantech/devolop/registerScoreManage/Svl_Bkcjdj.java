package com.pantech.devolop.registerScoreManage;
/**
编制日期：2016.08.04
创建人：yeq
模块名称：M7.2 补考成绩登记
说明:
功能索引:
	1-查询
	2-设置
**/
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

public class Svl_Bkcjdj extends HttpServlet {
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
		BkcjdjBean bean = new BkcjdjBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//查询科目列表
		if("queSubjectList".equalsIgnoreCase(active)){
			String XN_CX = MyTools.StrFiltr(request.getParameter("XN_CX"));
			String XBDM_CX = MyTools.StrFiltr(request.getParameter("XBDM_CX"));
			String ZYDM_CX = MyTools.StrFiltr(request.getParameter("ZYDM_CX"));
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			String KCLX_CX = MyTools.StrFiltr(request.getParameter("KCLX_CX"));
			
			try {
				jsonV = bean.queSubjectList(pageNum, pageSize, XN_CX, XBDM_CX, ZYDM_CX, NJDM_CX, KCMC_CX, KCLX_CX);
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
				//查询学年下拉框
				jsonV = bean.loadXnCombo();
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
				
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
				e.printStackTrace();
			}
		}
		
		//查询科目相关信息
		if("loadSubjectInfo".equalsIgnoreCase(active)){
			String xn = MyTools.StrFiltr(request.getParameter("XN"));
			String xbdm = MyTools.StrFiltr(request.getParameter("XBDM"));
			String zydm = MyTools.StrFiltr(request.getParameter("ZYDM"));
			String njdm = MyTools.StrFiltr(request.getParameter("NJDM"));
			String kcdm = MyTools.StrFiltr(request.getParameter("KCDM"));
			
			try {
				jsonV = bean.loadSubjectInfo(xn);
//				jal = JsonUtil.addJsonParams(jal, "xnxqmc", MyTools.StrFiltr(jsonV.get(0)));
//				jal = JsonUtil.addJsonParams(jal, "zymc", MyTools.StrFiltr(jsonV.get(1)));
//				jal = JsonUtil.addJsonParams(jal, "kcmc", MyTools.StrFiltr(jsonV.get(2)));
//				jal = JsonUtil.addJsonParams(jal, "zpblxx", MyTools.StrFiltr(jsonV.get(3)));
//				jal = JsonUtil.addJsonParams(jal, "psbl", MyTools.StrFiltr(jsonV.get(4)));
//				jal = JsonUtil.addJsonParams(jal, "qzbl", MyTools.StrFiltr(jsonV.get(5)));
//				jal = JsonUtil.addJsonParams(jal, "sxbl", MyTools.StrFiltr(jsonV.get(6)));
//				jal = JsonUtil.addJsonParams(jal, "qmbl", MyTools.StrFiltr(jsonV.get(7)));
//				jal = JsonUtil.addJsonParams(jal, "cjlx", MyTools.StrFiltr(jsonV.get(8)));
//				jal = JsonUtil.addJsonParams(jal, "cjlxmc", MyTools.StrFiltr(jsonV.get(9)));
//				jal = JsonUtil.addJsonParams(jal, "sx", MyTools.StrFiltr(jsonV.get(10)));
//				jal = JsonUtil.addJsonParams(jal, "sxmc", MyTools.StrFiltr(jsonV.get(11)));
				jal = JsonUtil.addJsonParams(jal, "dfsj", MyTools.StrFiltr(jsonV.get(0)));
				
				jsonV = bean.queClassTree(xn, xbdm, zydm, njdm, kcdm);
				jal = JsonUtil.addJsonParams(jal, "classData", ((JSONArray)jsonV.get(2)).toString());
				
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
				jal = JsonUtil.addJsonParams(jal, "zpblxx", MyTools.StrFiltr(jsonV.get(1)));
				jal = JsonUtil.addJsonParams(jal, "psbl", MyTools.StrFiltr(jsonV.get(2)));
				jal = JsonUtil.addJsonParams(jal, "qzbl", MyTools.StrFiltr(jsonV.get(3)));
				jal = JsonUtil.addJsonParams(jal, "sxbl", MyTools.StrFiltr(jsonV.get(4)));
				jal = JsonUtil.addJsonParams(jal, "qmbl", MyTools.StrFiltr(jsonV.get(5)));
				jal = JsonUtil.addJsonParams(jal, "cjlx", MyTools.StrFiltr(jsonV.get(6)));
				jal = JsonUtil.addJsonParams(jal, "cjlxmc", MyTools.StrFiltr(jsonV.get(7)));
				jal = JsonUtil.addJsonParams(jal, "sx", MyTools.StrFiltr(jsonV.get(8)));
				jal = JsonUtil.addJsonParams(jal, "sxmc", MyTools.StrFiltr(jsonV.get(9)));
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//读取当前选择班级课程的学生列表
		if("loadStuList".equalsIgnoreCase(active)){
			try {
				//初始化学生补考成绩信息
				bean.initStuBkInfo();
				
				String result = bean.loadStuList();
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
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param XqcjdjBean
    */
	private void getFormData(HttpServletRequest request,BkcjdjBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCX_ID(MyTools.StrFiltr(request.getParameter("CX_ID"))); //编号
		bean.setCX_XH(MyTools.StrFiltr(request.getParameter("CX_XH"))); //学号
		bean.setCX_XM(MyTools.StrFiltr(request.getParameter("CX_XM"))); //姓名
		bean.setCX_XGBH(MyTools.StrFiltr(request.getParameter("CX_XGBH"))); //相关编号
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
	}
}