package com.pantech.devolop.customExamManage;

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

public class Svl_Kssz extends HttpServlet {

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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page")); // 获得页面page参数
		int pageSize = MyTools.parseInt(request.getParameter("rows")); // 获得页面rows参数
		Vector jsonV = null;// 返回结果集
		JSONArray jal = null;// 返回json对象
		KsszBean bean = new KsszBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//获取当前学年学期
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
						
				String curXn = "";
				String curXq = "";
				if(!"".equalsIgnoreCase(curXnxq)){
					curXn = curXnxq.substring(0, 4);
					curXq = curXnxq.substring(4,5);
				}
						
				//查询考试列表
				jsonV = bean.queryRec(pageNum, pageSize, curXn, curXq , "" , "");
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
				//查询学年下拉框
				jsonV = bean.loadXnCombo();
				jal = JsonUtil.addJsonParams(jal, "xnData", ((JSONArray)jsonV.get(2)).toString());
						
				//查询学期下拉框
				jsonV = bean.loadXqCombo();
				jal = JsonUtil.addJsonParams(jal, "xqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询类别下拉框
				jsonV = bean.loadLbCombo();
				jal = JsonUtil.addJsonParams(jal, "lbData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学年学期下拉框
				jsonV = bean.loadXnXqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询招生类别下拉框
				jsonV = bean.loadZSLBCombo();
				jal = JsonUtil.addJsonParams(jal, "zslbData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询招生类别数量下拉框
				bean.loadLbXJBJCombo();
				jal = JsonUtil.addJsonParams(jal, "zslbsum", bean.getMSG());
				
				//查询新建编辑里的考试类别下拉框
				/*jsonV = bean.loadLbXJBJCombo();
				jal = JsonUtil.addJsonParams(jal, "kslbxjbjData", ((JSONArray)jsonV.get(2)).toString());*/
						
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		// 查询考试
		if (active.equalsIgnoreCase("queryRec")) {
			//String authType = MyTools.StrFiltr(request.getParameter("authType"));
			/*bean.setZK_XN(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_XN")),"UTF-8")); //学年
			bean.setZK_XQ(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_XQ")),"UTF-8")); //学期
			bean.setZK_KSMC(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_KSMC")),"UTF-8")); //考试名称
			bean.setZK_LBBH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_LBBH")),"UTF-8")); //类别
			 */			
			String XN_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_XN")),"UTF-8");//学年
			String XQ_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_XQ")),"UTF-8");//学期
			String KSMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_KSMC")), "UTF-8");//考试名称
			String KSLB_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("ZK_KSLB")),"UTF-8");//类别
			
			try {
				//查询教师列表
				jsonV = bean.queryRec(pageNum, pageSize, XN_CX, XQ_CX, KSMC_CX, KSLB_CX);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + ((JSONArray)jsonV.get(2)).toString() + "}");
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
			} catch (WrongSQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		
		//删除
		if("del".equalsIgnoreCase(active)){
			try {
				bean.delRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		
		// 查询月考考试
		if (active.equalsIgnoreCase("queryykksRec")) {
			String YK_KSMC = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("YK_KSMC")),"UTF-8");//考试名称
					
			try {
				//查询教师列表
				jsonV = bean.queryykksRec(pageNum, pageSize, YK_KSMC);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//编辑月考状态
		if("ModYKRec".equalsIgnoreCase(active)){
					
			String ZZZBH = MyTools.StrFiltr(request.getParameter("ZZZBH"));//编号
			String ZT = MyTools.StrFiltr(request.getParameter("ZT"));//状态
			
			try {
				bean.ModYKRec(ZZZBH,ZT);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (WrongSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//查询月考里班级下拉框
		if("loadBJCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadBJCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(MyTools.StrFiltr(jal.toString()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询月考里课程下拉框
		if("loadKCMCCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadKCMCCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(MyTools.StrFiltr(jal.toString()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存月考信息
		if("saveYKXX".equalsIgnoreCase(active)){
			String YKSZ_BH = MyTools.StrFiltr(request.getParameter("YKSZ_BH"));//编号
			String YKSZ_KSMC = MyTools.StrFiltr(request.getParameter("YKSZ_KSMC"));//考试名称
			
			String YKSZ_BJ = MyTools.StrFiltr(request.getParameter("YKSZ_BJ"));//班级
			String YKSZ_KCMC = MyTools.StrFiltr(request.getParameter("YKSZ_KCMC"));//课程名称
			String YKSZ_CJSJ = MyTools.StrFiltr(request.getParameter("YKSZ_CJSJ"));//创建时间
			
			try {
				bean.saveYKXX(YKSZ_BH,YKSZ_KSMC,YKSZ_BJ,YKSZ_KCMC,YKSZ_CJSJ);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (WrongSQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		
		//删除月考信息
		if("delYKRec".equalsIgnoreCase(active)){
			String YKSZ_BH = MyTools.StrFiltr(request.getParameter("YKSZ_BH"));//编号
			try {
				bean.delYKRec(YKSZ_BH);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				response.getWriter().write(jal.toString());
			}
		}
		
		//查询班级信息
		if("loadClassTree".equalsIgnoreCase(active)){
			try {
				String result = bean.loadClassTree();
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO: handle exception
			}
		}
		
		//查询考试列表
		if("querySubjectList".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.querySubjectList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//查询班级已选学科信息
		if("loadClassSelSubject".equalsIgnoreCase(active)){
			try {
				String result = bean.loadClassSelSubject();
				response.getWriter().write(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存班级已选学科信息
		if("saveXK".equalsIgnoreCase(active)){
			String selXkInfo = MyTools.StrFiltr(request.getParameter("xkInfo"));
					
			try {
				bean.saveSelXk(selXkInfo);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//考试学科批量设置学年combobox
		if("loadXKPLCombo".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadXKPLCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(MyTools.StrFiltr(jal.toString()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//考试学科批量设置专业combobox
		if("loadXKPLZYCombo".equalsIgnoreCase(active)){
			String xn = MyTools.StrFiltr(request.getParameter("PL_XN"));//学年
			try {
				jsonV = bean.loadXKPLZYCombo(xn);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(MyTools.StrFiltr(jal.toString()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//考试学科批量设置课程combobox
		if("loadXKPLKCCombo".equalsIgnoreCase(active)){
			String xn = MyTools.StrFiltr(request.getParameter("PL_XN"));//学年
			String zy = MyTools.StrFiltr(request.getParameter("PL_ZY"));//专业
			try {
				jsonV = bean.loadXKPLKCCombo(xn,zy);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(MyTools.StrFiltr(jal.toString()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//保存班级已选学科信息
		if("saveXKPL".equalsIgnoreCase(active)){
			String plnjInfo = MyTools.StrFiltr(request.getParameter("plnjInfo"));
			String plzyInfo = MyTools.StrFiltr(request.getParameter("plzyInfo"));
			String plkcInfo = MyTools.StrFiltr(request.getParameter("plkcInfo"));
							
			try {
				bean.saveSelXkPL(plnjInfo,plzyInfo,plkcInfo);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 从界面获取参数
	 * 
	 * @date
	 * @author:
	 * @param request
	 * @param KsszBean
	 */
	private void getFormData(HttpServletRequest request, KsszBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		bean.setZK_KSBH(MyTools.StrFiltr(request.getParameter("ZK_KSBH")));// 考试编号
		bean.setZK_KSMC(MyTools.StrFiltr(request.getParameter("ZK_KSMC")));// 考试名称
		bean.setZK_XNXQBM(MyTools.StrFiltr(request.getParameter("ZK_XNXQBM")));// 学年学期编码
		bean.setZK_LBBH(MyTools.StrFiltr(request.getParameter("ZK_LBBH")));// 类别编号
		bean.setZK_CJR(MyTools.StrFiltr(request.getParameter("ZK_CJR")));// 创建人
		bean.setZK_CJSJ(MyTools.StrFiltr(request.getParameter("ZK_CJSJ")));// 创建时间
		bean.setZK_ZT(MyTools.StrFiltr(request.getParameter("ZK_ZT")));// 状态
		bean.setZK_BJBH(MyTools.StrFiltr(request.getParameter("ZK_BJBH")));//班级编号
		bean.setZK_KSLB(MyTools.StrFiltr(request.getParameter("ZK_KSLBXJ")));// 考试类别
		bean.setZK_ZSLB(MyTools.StrFiltr(request.getParameter("ZK_ZSLBXJ")));// 招生类别
		bean.setZK_DJKXSLB(MyTools.StrFiltr(request.getParameter("ZK_DJKXSLB")));// 等级考学生类别
		bean.setZK_DFKSSJ(MyTools.StrFiltr(request.getParameter("ZK_DFKSSJ")));//登分开始时间
		bean.setZK_DFJSSJ(MyTools.StrFiltr(request.getParameter("ZK_DFJSSJ")));//登分结束时间
	}
}