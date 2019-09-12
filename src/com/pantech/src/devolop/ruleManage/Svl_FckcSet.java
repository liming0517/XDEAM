package com.pantech.src.devolop.ruleManage;

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

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.devolop.registerScoreSet.DfryszBean;
import com.pantech.devolop.registerScoreSet.KmszBean;

public class Svl_FckcSet extends HttpServlet {

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
		FckcSetBean bean = new FckcSetBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化下拉框页面数据
		if("initComboData".equalsIgnoreCase(active)){
			try {
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJxxzCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系名称下拉框
				jsonV = bean.loadXdmCombo();
				jal = JsonUtil.addJsonParams(jal, "xdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询年级代码下拉框
				jsonV = bean.loadNjdmCombo();
				jal = JsonUtil.addJsonParams(jal, "njdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询考试形式下拉框
				jsonV = bean.loadKsxsCombo();
				jal = JsonUtil.addJsonParams(jal, "ksxsData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询授课教师下拉框
				jsonV = bean.loadSkjsCombo();
				jal = JsonUtil.addJsonParams(jal, "skjsData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询课程下拉框
		if("loadCourseCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadCourseCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询分层班下拉框
		if("loadClassCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadClassCombo();
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询分层课程列表
		if("queCourseList".equalsIgnoreCase(active)){
			String XNXQMC_CX = MyTools.StrFiltr(request.getParameter("XNXQMC_CX"));
			String JXXZ_CX = MyTools.StrFiltr(request.getParameter("JXXZ_CX"));
			String XDM_CX = MyTools.StrFiltr(request.getParameter("XDM_CX"));
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("KCMC_CX")), "UTF-8");
			
			try {
				jsonV = bean.queCourseList(pageNum, pageSize, XNXQMC_CX, JXXZ_CX, XDM_CX, NJDM_CX, KCMC_CX);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//新增分层课程
		if("addCourse".equalsIgnoreCase(active)){
			try {
				bean.addCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "保存失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//编辑分层课程
		if("editCourse".equalsIgnoreCase(active)){
			try {
				bean.editCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "保存失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//删除分层课程
		if("delCourse".equalsIgnoreCase(active)){
			try {
				bean.delCourse();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "删除失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//查询分层班列表
		if("queClassList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queClassList();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//新增分层班
		if("addClass".equalsIgnoreCase(active)){
			try {
				bean.addClass();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "保存失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//编辑分层班
		if("editClass".equalsIgnoreCase(active)){
			try {
				bean.editClass();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "保存失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//删除分层班
		if("delClass".equalsIgnoreCase(active)){
			try {
				bean.delClass();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "删除失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//查询分层班列表
		if("queStuList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.queStuList();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询班级学生树数据
		if("queClassStuTree".equalsIgnoreCase(active)){
			String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
			
			try {
				jsonV = bean.queClassStuTree(parentCode);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			}catch(SQLException e){
				e.printStackTrace();
			}catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//保存新增学生
		if("saveAddStu".equalsIgnoreCase(active)){
			try {
				bean.saveAddStu();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//删除分层班学生
		if("delStu".equalsIgnoreCase(active)){
			try {
				bean.delStu();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "删除失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//读取当前学期的总周次数据
		if("loadXqzc".equalsIgnoreCase(active)){
			try {
				//查询学年学期下拉框
				String xqzc = bean.loadXqzc();
				jal = JsonUtil.addJsonParams(jal, "xqzc", xqzc);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//导入学生名单
		if("importStuList".equalsIgnoreCase(active)){
			try {
				bean.importStuList();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "导入失败");
				response.getWriter().write(jal.toString());
			}
		}
	}
	
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param DfryszBean
    */
	private void getFormData(HttpServletRequest request, FckcSetBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setGF_FCKCBH(MyTools.StrFiltr(request.getParameter("GF_FCKCBH"))); //分层课程编号
		bean.setGF_XNXQBM(MyTools.StrFiltr(request.getParameter("GF_XNXQBM"))); //学年学期编码
		bean.setGF_JXXZ(MyTools.StrFiltr(request.getParameter("GF_JXXZ"))); //教学性质
		bean.setGF_XDM(MyTools.StrFiltr(request.getParameter("GF_XDM"))); //系代码
		bean.setGF_NJDM(MyTools.StrFiltr(request.getParameter("GF_NJDM"))); //年级代码
		bean.setGF_KCDM(MyTools.StrFiltr(request.getParameter("GF_KCDM"))); //课程代码
		bean.setGF_KCMC(MyTools.StrFiltr(request.getParameter("GF_KCMC"))); //课程名称
		bean.setGF_SKZC(MyTools.StrFiltr(request.getParameter("GF_SKZC"))); //授课周次
		bean.setGF_SKZCMC(MyTools.StrFiltr(request.getParameter("GF_SKZCMC"))); //授课周次名称
		bean.setGF_XF(MyTools.StrFiltr(request.getParameter("GF_XF"))); //学分
		bean.setGF_ZKS(MyTools.StrFiltr(request.getParameter("GF_ZKS"))); //总课时
		bean.setGF_KSXS(MyTools.StrFiltr(request.getParameter("GF_KSXS"))); //考试形式
		
		bean.setGF_FCBBH(MyTools.StrFiltr(request.getParameter("GF_FCBBH"))); //分层班编号
		bean.setGF_FCBMC(MyTools.StrFiltr(request.getParameter("GF_FCBMC"))); //分层班名称
		bean.setGF_SKJSBH(MyTools.StrFiltr(request.getParameter("GF_SKJSBH"))); //授课教师编号
		bean.setGF_SKJSXM(MyTools.StrFiltr(request.getParameter("GF_SKJSXM"))); //授课教师姓名
		
		bean.setGF_ID(MyTools.StrFiltr(request.getParameter("GF_ID"))); //编号
		bean.setGF_XH(MyTools.StrFiltr(request.getParameter("GF_XH"))); //学号
		
		bean.setGF_KMBH(MyTools.StrFiltr(request.getParameter("GF_KMBH"))); //科目编号
	}
}