/**
	编制日期：2015.06.03
	创建人：wangzh
	模块名称：M1.3特殊规则设置
	说明:
		 
	功能索引:
		1-保存
		2-删除
		3-还原
		4-查询
**/

package com.pantech.src.devolop.ruleManage;

import java.io.IOException;
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

public class Svl_Tsgz extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Svl_Tsgz() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}
	
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
		TsgzBean bean = new TsgzBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询有效的特殊规则列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教师编号下拉框
				//jsonV = bean.loadJSBHCombo();
				//jal = JsonUtil.addJsonParams(jal, "jsbhData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询有效的特殊规则列表
		if("query".equalsIgnoreCase(active)){
			try {
				//查询特殊规则列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询无效的特殊规则列表
		if("quehis".equalsIgnoreCase(active)){
			try {
				jsonV = bean.quehisRec(pageNum,pageSize);
				if (jsonV != null && jsonV.size() > 0) {
					jal = (JSONArray) jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				}          
			} catch (SQLException e) {
				e.printStackTrace();
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		}
		
		//保存
		if("save".equalsIgnoreCase(active)){
			try {
				bean.saveRec();
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (WrongSQLException e) {
				e.printStackTrace();
			}
		}
		
		//删除
		if("del".equalsIgnoreCase(active)){
			try {
				bean.delRec();
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//还原
		if("redu".equalsIgnoreCase(active)){
			try {
				bean.reduRec();
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void getFormData(HttpServletRequest request, TsgzBean bean) {
		bean.setGT_XNXQBM(MyTools.StrFiltr(request.getParameter("GT_XNXQBM"))); //学年学期编码
		bean.setGT_JSBH(MyTools.StrFiltr(request.getParameter("GT_JSBH"))); //教师编号
		bean.setGT_XNXQBM1(MyTools.StrFiltr(request.getParameter("GT_XNXQBM1"))); //学年学期编码
		bean.setGT_JSBH1(MyTools.StrFiltr(request.getParameter("GT_JSBH1"))); //教师编号
		bean.setGT_JSXM(MyTools.StrFiltr(request.getParameter("GT_JSXM"))); //教师姓名
		bean.setGT_JXXZ(MyTools.StrFiltr(request.getParameter("GT_JXXZ"))); //教学性质
		bean.setGT_MTCS(MyTools.StrFiltr(request.getParameter("GT_MTCS"))); //每天次数
		bean.setGT_MZCS(MyTools.StrFiltr(request.getParameter("GT_MZCS"))); //每周次数
		bean.setGT_MTJC(MyTools.StrFiltr(request.getParameter("GT_MTJC"))); //每天节次
		bean.setGT_MZJC(MyTools.StrFiltr(request.getParameter("GT_MZJC"))); //每周节次
		bean.setGT_ZDZJKCS(MyTools.StrFiltr(request.getParameter("GT_ZDZJKCS"))); //最大执教课程数
		bean.setGT_ZT(MyTools.StrFiltr(request.getParameter("GT_ZT"))); //状态
		bean.setGT_JS(MyTools.StrFiltr(request.getParameter("GT_JS")));//角色
	}
}
