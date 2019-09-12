/**
	编制日期：2015.06.02
	创建人：wangzh
	模块名称：M1.2授课计划
	说明:
		 
	功能索引:
		1-编辑
		2-查询
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

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class Svl_Cdgpjp extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Svl_Cdgpjp() {
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
		CdgpjpBean bean = new CdgpjpBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询列表
				//jsonV = bean.queryTree(pageNum, pageSize);
				//jal = (JSONArray)jsonV.get(2);
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询列表
		if("query".equalsIgnoreCase(active)){
			String termid=MyTools.StrFiltr(request.getParameter("termid"));
			try {
				//查询列表
				//jsonV = bean.queryTree(pageNum, pageSize);
				//jal = (JSONArray)jsonV.get(2);
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				//查询周次数量
				bean.getWeeknum(termid);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询专业班级树
		if("queryTree".equalsIgnoreCase(active)){
			String parentId=MyTools.StrFiltr(request.getParameter("parentId"));
			String level = MyTools.StrFiltr(request.getParameter("level"));
			try {
				//查询列表
				jsonV = bean.queryTree(pageNum, pageSize, parentId, level);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//初始化页面数据
		if("initGridData".equalsIgnoreCase(active)){
			try {
				//查询列表
				jsonV = bean.queryGrid(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				//TraceLog.Trace("listData:"+jal.toString());
				//查询节次时间
				jsonV = bean.loadJCSJ();
				jal = JsonUtil.addJsonParams(jal, "timeData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询
				jsonV = bean.loadGPJP();
				jal = JsonUtil.addJsonParams(jal, "gpjpData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学科名称下拉框
				//jsonV = bean.loadXKMCCombo();
				//jal = JsonUtil.addJsonParams(jal, "xkmcData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询任课教师下拉框
				//jsonV = bean.loadRKJSCombo();
				//jal = JsonUtil.addJsonParams(jal, "rkjsData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("listData:---------"+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("schoolCombobox".equalsIgnoreCase(active)){
			try {
				jsonV = bean.schoolCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("classtypeCombobox".equalsIgnoreCase(active)){
			try {
				jsonV = bean.classtypeCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if ("checkTeaCls".equalsIgnoreCase(active)) {
			String jsxm = MyTools.StrFiltr(request.getParameter("jsxm"));
			String skzcxq = MyTools.StrFiltr(request.getParameter("skzcxq"));
			String weeks = MyTools.StrFiltr(request.getParameter("weeks"));
			String cdyq = MyTools.StrFiltr(request.getParameter("cdyq"));
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			String termid = MyTools.StrFiltr(request.getParameter("termid"));
			String cdyqId = MyTools.StrFiltr(request.getParameter("cdyqId"));
			String classdm = MyTools.StrFiltr(request.getParameter("classdm"));
			String jsbh = MyTools.StrFiltr(request.getParameter("jsbh"));
			try {
				bean.checkTeaCls(jsxm,skzcxq,weeks,cdyq,iKeyCode,termid,cdyqId,classdm,jsbh);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "msg2", bean.getMSG2());
				jal = JsonUtil.addJsonParams(jal, "msg3", bean.getMSG3());
				jal = JsonUtil.addJsonParams(jal, "msg4", bean.getMSG4());
				jal = JsonUtil.addJsonParams(jal, "msg5", bean.getMSG5());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if ("checkRec".equalsIgnoreCase(active)) {
			String idVal = MyTools.StrFiltr(request.getParameter("idVal"));
			try {
				bean.checkRec(idVal);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "lcs", bean.getLCS());
				jal = JsonUtil.addJsonParams(jal, "aod", bean.getAOD());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if ("add".equalsIgnoreCase(active)) {
			String gpjs = MyTools.StrFiltr(request.getParameter("gpjs"));
			
			try {
				bean.addRec(gpjs);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if ("del".equalsIgnoreCase(active)) {
			String idValcon = MyTools.StrFiltr(request.getParameter("idValcon"));
			try {
				bean.delRec(idValcon);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("save".equalsIgnoreCase(active)){
			String editsjxl = MyTools.StrFiltr(request.getParameter("SJXL"));
			String cdyq = MyTools.StrFiltr(request.getParameter("CDYQ"));
			String cdmc = MyTools.StrFiltr(request.getParameter("CDMC"));
			String skzc = MyTools.StrFiltr(request.getParameter("SKZC"));
			String savetype = MyTools.StrFiltr(request.getParameter("SAVETYPE"));
			try {
				bean.saveRec(editsjxl,cdyq,cdmc,skzc,savetype);
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	* 从界面没获取参数
	* @date 2015-06-02
	* @author:wangzh
    * @param request
    * @param XnxqBean
    */
	private void getFormData(HttpServletRequest request,CdgpjpBean bean){
		bean.setGG_BH(MyTools.StrFiltr(request.getParameter("GG_BH"))); //编号
		bean.setGG_LX(MyTools.StrFiltr(request.getParameter("GG_LX"))); //类型
		bean.setGG_XNXQBM(MyTools.StrFiltr(request.getParameter("GG_XNXQBM"))); //学年学期编码
		bean.setGG_XZBDM(MyTools.StrFiltr(request.getParameter("GG_XZBDM"))); //行政班代码
		bean.setGG_SJXL(MyTools.StrFiltr(request.getParameter("GG_SJXL"))); //时间序列
		bean.setGG_SKJHMXBH(MyTools.StrFiltr(request.getParameter("GG_SKJHMXBH"))); //授课计划明细编号
		bean.setGG_LJXGBH(MyTools.StrFiltr(request.getParameter("GG_LJXGBH"))); //连节相关编号
		bean.setGG_ZT(MyTools.StrFiltr(request.getParameter("GG_ZT"))); //状态
		bean.setSKJHMXBH(MyTools.StrFiltr(request.getParameter("SKJHMXBH"))); //授课计划明细编号
		bean.setXZBDM(MyTools.StrFiltr(request.getParameter("XZBDM"))); //行政班代码
		bean.setXNXQ(MyTools.StrFiltr(request.getParameter("XNXQ"))); //学年学期
		bean.setKCJS(MyTools.StrFiltr(request.getParameter("KCJS"))); //课程教师
		bean.setJXXZ(MyTools.StrFiltr(request.getParameter("JXXZ"))); //教学性质
		bean.setQCXX(MyTools.StrFiltr(request.getParameter("QCXX"))); //清除的信息
		bean.setJSBH(MyTools.StrFiltr(request.getParameter("JSBH"))); //教师编号
		bean.setJSXM(MyTools.StrFiltr(request.getParameter("JSXM"))); //教师姓名
		bean.setiUSERCODE(MyTools.StrFiltr(request.getParameter("iUSERCODE"))); //用户编号
		bean.setsAuth(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
	}
}
