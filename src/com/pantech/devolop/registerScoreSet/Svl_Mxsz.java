package com.pantech.devolop.registerScoreSet;
/**
编制日期：2016.02.03
创建人：yeq
模块名称：M6.4 免修设置
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

public class Svl_Mxsz extends HttpServlet {

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
		MxszBean bean = new MxszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询当前学年
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
				
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询班级列表
		if("queClassList".equalsIgnoreCase(active)){
			String ZYMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZYMC_CX")), "UTF-8");
			String XZBMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XZBMC_CX")), "UTF-8");
			String BJLX_CX = MyTools.StrFiltr(request.getParameter("BJLX_CX"));
			String XNXQMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XNXQMC_CX")), "UTF-8");
			String XH_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XH_CX")), "UTF-8");
			String XM_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("XM_CX")), "UTF-8");
			
			try {
				//删除重复的班级课程
				//20170914修改为进入成绩系统执行此操作
				//KmszBean.delRepeatCourse(request);
				
				jsonV = bean.queClassList(pageNum, pageSize, ZYMC_CX, XZBMC_CX, BJLX_CX, XNXQMC_CX, XH_CX, XM_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取课程下拉框数据
		if("loadCourseCombo".equalsIgnoreCase(active)){
			String classCode = MyTools.StrFiltr(request.getParameter("classCode"));
			String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String classType = MyTools.StrFiltr(request.getParameter("classType"));
			
			try {
				jsonV = bean.loadCourseCombo(classCode, xnxqbm, classType);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取所选课程学生免修名单数据
		if("loadStuMxmd".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadStuMxmd();
				jal = JsonUtil.addJsonParams(jal, "stuCode", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "stuName", MyTools.StrFiltr(jsonV.get(1)));
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询学生列表
		if("queStuList".equalsIgnoreCase(active)){
			try {
				//添加学生基础信息
				DfryszBean.addStuScoreInfo(request, bean.getCX_XGBH());
				//查询学生列表
				jsonV = bean.loadStuList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存免修学生
		if("saveMxStu".equalsIgnoreCase(active)){
			try {
				bean.saveMxStu();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//读取学生相关课程信息
		if("loadStuCourseListData".equalsIgnoreCase(active)){
			bean.setCX_XH(URLDecoder.decode(bean.getCX_XH(), "UTF-8"));
			bean.setCX_XM(URLDecoder.decode(bean.getCX_XM(), "UTF-8"));
			
			try {
				//查询学生课程列表
				jsonV = bean.loadStuCourseListData(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				jal = JsonUtil.addJsonParams(jal, "semCode", bean.getCX_XNXQ());
				jal = JsonUtil.addJsonParams(jal, "stuCode", bean.getCX_XH());
				jal = JsonUtil.addJsonParams(jal, "stuName", bean.getCX_XM());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存指定学生免修课程信息
		if("saveMxPlsz".equalsIgnoreCase(active)){
			try {
				bean.saveStuMxCourse();
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
    * @param MxszBean
    */
	private void getFormData(HttpServletRequest request,MxszBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCX_XNXQ(MyTools.StrFiltr(request.getParameter("CX_XNXQ"))); //学年学期
		bean.setCX_XH(MyTools.StrFiltr(request.getParameter("CX_XH"))); //学号
		bean.setCX_XM(MyTools.StrFiltr(request.getParameter("CX_XM"))); //姓名
		bean.setCX_XGBH(MyTools.StrFiltr(request.getParameter("CX_XGBH"))); //相关编号
		bean.setCX_CJZT(MyTools.StrFiltr(request.getParameter("CX_CJZT"))); //成绩状态
	}
}
