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

public class Svl_xfsz extends HttpServlet {

	
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
		XfszBean bean = new XfszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学分申请列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询类型下拉框
				jsonV = bean.loadLXCombo();
				jal = JsonUtil.addJsonParams(jal, "lxData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		if("query".equalsIgnoreCase(active)){
			try {
				//查询学分申请列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//查询学生列表
		if("queStuList".equalsIgnoreCase(active)){
			try {
				String xzxsxh = MyTools.StrFiltr(request.getParameter("XZXSXH"));
				//查询学生列表
				jsonV = bean.loadStuList(pageNum, pageSize, xzxsxh);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询教师列表
		if("queTeaList".equalsIgnoreCase(active)){
			try {
				String xzskjsgh = MyTools.StrFiltr(request.getParameter("XZSKJSGH"));
				//查询教师列表
				jsonV = bean.loadTeaList(pageNum, pageSize, xzskjsgh);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询最低可用学分
		if("queryKyxf".equalsIgnoreCase(active)){
			try {
				//查询学生列表
				String Kyxf = bean.loadKyxf();
				jal = JsonUtil.addJsonParams(jal, "curKyxf", Kyxf);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存
		if("save".equalsIgnoreCase(active)){
			try {
				bean.saveRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//修改
		if("edit".equalsIgnoreCase(active)){
			try {
				bean.editRec();
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
		
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yangda
    * @param request
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, XfszBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setXX_ID(MyTools.StrFiltr(request.getParameter("XX_ID")));//编号
		bean.setXX_XNXQBM(MyTools.StrFiltr(request.getParameter("XX_XNXQBM")));//学年学期编码
		bean.setXX_XH(MyTools.StrFiltr(request.getParameter("XX_XH")));// 学号
		bean.setXX_XM(MyTools.StrFiltr(request.getParameter("XX_XM")));// 姓名 
		bean.setXX_MC(MyTools.StrFiltr(request.getParameter("XX_MC")));// 名称
		bean.setXX_SKJSGH(MyTools.StrFiltr(request.getParameter("XX_SKJSGH")));// 授课教师
		bean.setXX_SKJS(MyTools.StrFiltr(request.getParameter("XX_SKJS")));// 授课教师
		bean.setXX_LX(MyTools.StrFiltr(request.getParameter("XX_LX")));// 类型
		bean.setXX_XF(MyTools.StrFiltr(request.getParameter("XX_XF")));// 学分
		bean.setXX_CJR(MyTools.StrFiltr(request.getParameter("XX_CJR")));// 创建人
		bean.setXX_CJSJ(MyTools.StrFiltr(request.getParameter("XX_CJSJ")));// 创建时间
		bean.setXX_ZT(MyTools.StrFiltr(request.getParameter("XX_ZT")));// 状态
		
	}


}
