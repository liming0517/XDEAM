package com.pantech.devolop.registerScoreSet;
/**
编制日期：2016.01.29
创建人：yeq
模块名称：M6.1 科目设置
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
import com.pantech.devolop.courseManage.PkszBean;

public class Svl_Kmsz extends HttpServlet {
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
		KmszBean bean = new KmszBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//删除重复的班级课程
		if("delRepeatCourse".equalsIgnoreCase(active)){
			try {
				KmszBean.delRepeatCourse(request);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//初始化页面数据
		if("initComboData".equalsIgnoreCase(active)){
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
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询科目列表
		if("queSubjectList".equalsIgnoreCase(active)){
			String CK_XNXQMC_CX = MyTools.StrFiltr(request.getParameter("CK_XNXQMC_CX"));
			String CK_JXXZ_CX = MyTools.StrFiltr(request.getParameter("CK_JXXZ_CX"));
			String CK_ZYMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CK_ZYMC_CX")), "UTF-8");
			String CK_KCMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("CK_KCMC_CX")), "UTF-8");
			String CK_KMLX_CX = MyTools.StrFiltr(request.getParameter("CK_KMLX_CX"));
			String CK_CYJD_CX = MyTools.StrFiltr(request.getParameter("CK_CYJD_CX"));
			String CK_NJDM_CX = MyTools.StrFiltr(request.getParameter("CK_NJDM_CX"));
			String CK_KCLX_CX = MyTools.StrFiltr(request.getParameter("CK_KCLX_CX"));
			
			try {
				//查询学年学期列表
				jsonV = bean.queSubjectList(pageNum, pageSize, CK_XNXQMC_CX, CK_JXXZ_CX, CK_ZYMC_CX, CK_KCMC_CX, CK_KMLX_CX, CK_CYJD_CX, CK_NJDM_CX, CK_KCLX_CX);
				jal = (JSONArray)jsonV.get(2);
//				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
//				response.getWriter().write(jal.toString());
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//设置科目相关信息
		if("setSubject".equalsIgnoreCase(active)){
			String selectId = MyTools.StrFiltr(request.getParameter("selectId"));
			String setType = MyTools.StrFiltr(request.getParameter("setType"));
			
			try {
				bean.updateSubjectInfo(selectId, setType);
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
    * @param KmszBean
    */
	private void getFormData(HttpServletRequest request,KmszBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTH(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
		bean.setCK_ID(MyTools.StrFiltr(request.getParameter("CK_ID"))); //科目编号
		bean.setCK_XNXQBM(MyTools.StrFiltr(request.getParameter("CK_XNXQBM"))); //学年学期编码
		bean.setCK_XNXQMC(MyTools.StrFiltr(request.getParameter("CK_XNXQMC"))); //学年学期名称
		bean.setCK_NJDM(MyTools.StrFiltr(request.getParameter("CK_NJDM"))); //年级代码
		bean.setCK_NJMC(MyTools.StrFiltr(request.getParameter("CK_NJMC"))); //年级名称
		bean.setCK_ZYDM(MyTools.StrFiltr(request.getParameter("CK_ZYDM"))); //专业代码
		bean.setCK_ZYMC(MyTools.StrFiltr(request.getParameter("CK_ZYMC"))); //专业名称
		bean.setCK_KCDM(MyTools.StrFiltr(request.getParameter("CK_KCDM"))); //课程代码
		bean.setCK_KCBM(MyTools.StrFiltr(request.getParameter("CK_KCBM"))); //课程名称
		bean.setCK_KCLX(MyTools.StrFiltr(request.getParameter("CK_KCLX"))); //课程类型
		bean.setCK_KMLX(MyTools.StrFiltr(request.getParameter("CK_KMLX"))); //科目类型
		bean.setCK_SFCYJD(MyTools.StrFiltr(request.getParameter("CK_SFCYJD"))); //是否参与绩点
		bean.setCK_CJR(MyTools.StrFiltr(request.getParameter("CK_CJR"))); //创建人
		bean.setCK_CJSJ(MyTools.StrFiltr(request.getParameter("CK_CJSJ"))); //创建时间
		bean.setCK_ZT(MyTools.StrFiltr(request.getParameter("CK_ZT"))); //状态
	}
}