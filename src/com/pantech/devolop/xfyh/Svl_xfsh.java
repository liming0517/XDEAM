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

public class Svl_xfsh extends HttpServlet {

	
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
				//查询学生申请列表
				jsonV = bean.queryRec2(pageNum, pageSize,"");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo2();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//审核状态下拉框
				jsonV = bean.loadSHZTCombo2();
				jal = JsonUtil.addJsonParams(jal, "shztData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//查询
		if("query".equalsIgnoreCase(active)){
			try {
				String kcmc = MyTools.StrFiltr(request.getParameter("KCMC"));
				//查询学生申请列表
				jsonV = bean.queryRec2(pageNum, pageSize,kcmc);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//审核通过
		if("pass".equalsIgnoreCase(active)){
			try {
				bean.passRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//批量审核通过
		if("plpass".equalsIgnoreCase(active)){
			try {
				bean.plpassRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//审核驳回
		if("back".equalsIgnoreCase(active)){
			try {
				bean.backRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//批量审核驳回
		if("plback".equalsIgnoreCase(active)){
			try {
				bean.plbackRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
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
