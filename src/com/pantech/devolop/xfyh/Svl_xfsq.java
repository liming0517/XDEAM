package com.pantech.devolop.xfyh;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class Svl_xfsq extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	
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
		XfyhBean bean = new XfyhBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学分申请列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询课程名称下拉框
				jsonV = bean.loadKCMCCombo();
				jal = JsonUtil.addJsonParams(jal, "kcmcData", ((JSONArray)jsonV.get(2)).toString());
				
				
				//获取当前学年学期名称
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
				
				//获取当前学年学期编码
				String curXnxqbm = bean.loadCurXnxqbm();
				jal = JsonUtil.addJsonParams(jal, "curXnxqbm", curXnxqbm);
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//查询学生信息
		if("xsxx".equalsIgnoreCase(active)){
			try {
				//查询学生及学分信息
				String curXf = bean.loadcurXf();
				response.getWriter().write(curXf);
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询
		if("query".equalsIgnoreCase(active)){
			try {
				//查询学分申请列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询成绩
		if("querycj".equalsIgnoreCase(active)){
			try {
				String kcmc = MyTools.StrFiltr(request.getParameter("kcmc"));
				String xnxqmc = MyTools.StrFiltr(request.getParameter("xnxqmc"));
				//获取考试成绩
				String kscj = bean.loadKscj(kcmc,xnxqmc);
				//获取本课程最高成绩
				String zgcj = bean.loadZgcj(kcmc,xnxqmc);
				
				jal = JsonUtil.addJsonParams(jal, "kscj", kscj);
				jal = JsonUtil.addJsonParams(jal, "zgcj", zgcj);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询最高成绩
		if("queryZgcj".equalsIgnoreCase(active)){
			try {
				String kcmc = MyTools.StrFiltr(request.getParameter("kcmc"));
				String xnxqmc = MyTools.StrFiltr(request.getParameter("xnxqmc"));
				//获取本课程最高成绩
				String zgcj = bean.loadZgcj(kcmc,xnxqmc);
				
				jal = JsonUtil.addJsonParams(jal, "zgcj", zgcj);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//保存
		if("save".equalsIgnoreCase(active)){
			try {
				String xnxqmc = MyTools.StrFiltr(request.getParameter("XNXQMC"));
				String tj = MyTools.StrFiltr(request.getParameter("tj"));
				String tj2 = MyTools.StrFiltr(request.getParameter("tj2"));
				String bh = bean.saveRec(tj,tj2,xnxqmc);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "bh", bh);
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//提交
		if("submit".equalsIgnoreCase(active)){
			try {
				bean.submitRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		//删除
		if("del".equalsIgnoreCase(active)){
			try {
				bean.delRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		if("xfxq".equalsIgnoreCase(active)){
			try {
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学分详情列表
//				jsonV = bean.loadXfxq(pageNum, pageSize);
//				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "xfxqData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
//				
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		if("queryXfxq".equalsIgnoreCase(active)){
			try {
				
				//查询学分详情列表
				jsonV = bean.queryXfxq(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "xfxqData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yangda
    * @param request
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, XfyhBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setXX_ID(MyTools.StrFiltr(request.getParameter("XX_ID")));//编号
		bean.setXX_XNXQBM(MyTools.StrFiltr(request.getParameter("XX_XNXQBM")));//学年学期编码
		bean.setXX_XH(MyTools.StrFiltr(request.getParameter("XX_XH")));// 学号
		bean.setXX_XM(MyTools.StrFiltr(request.getParameter("XX_XM")));// 姓名 
		bean.setXX_BJBH(MyTools.StrFiltr(request.getParameter("XX_BJBH")));//班级编号
		bean.setXX_BJMC(MyTools.StrFiltr(request.getParameter("XX_BJMC")));// 班级名称
		bean.setXX_XGBH(MyTools.StrFiltr(request.getParameter("XX_XGBH")));// 相关编号
		bean.setXX_CJ(MyTools.StrFiltr(request.getParameter("XX_CJ")));// 成绩
		bean.setXX_JF(MyTools.StrFiltr(request.getParameter("XX_JF")));// 加分
		bean.setXX_BZ(MyTools.StrFiltr(request.getParameter("XX_BZ")));// 备注
		bean.setXX_SHSJ(MyTools.StrFiltr(request.getParameter("XX_SHSJ")));// 审核时间
		bean.setXX_SHZT(MyTools.StrFiltr(request.getParameter("XX_SHZT")));// 审核状态
		bean.setXX_SHXX(MyTools.StrFiltr(request.getParameter("XX_SHXX")));// 审核信息
		bean.setXX_SHR(MyTools.StrFiltr(request.getParameter("XX_SHR"))); // 审核人
		bean.setXX_CJR(MyTools.StrFiltr(request.getParameter("XX_CJR")));// 创建人
		bean.setXX_CJSJ(MyTools.StrFiltr(request.getParameter("XX_CJSJ")));// 创建时间
		bean.setXX_ZT(MyTools.StrFiltr(request.getParameter("XX_ZT")));// 状态
		
	}

}
