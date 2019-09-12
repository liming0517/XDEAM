package com.pantech.devolop.baseInfoManage;

import java.io.IOException;
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

public class Svl_JxbSet extends HttpServlet {

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
		JxbSetBean bean = new JxbSetBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize,  "", "", "", "");
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询年级下拉框
				jsonV = bean.loadNjdmCombo();
				jal = JsonUtil.addJsonParams(jal, "njdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadXbdmCombo();
				jal = JsonUtil.addJsonParams(jal, "xbdmData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询专业下拉框
				jsonV = bean.loadMajorCombo();
				jal = JsonUtil.addJsonParams(jal, "sszyData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学班类型下拉框
				jsonV = bean.loadBJLXCombo();
				jal = JsonUtil.addJsonParams(jal, "bjlxData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if("xjxbmcombobox".equalsIgnoreCase(active)){
			//查询专业下拉框
			try {
				jsonV = bean.loadClassCombo();
				jal =(JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		//查询班级列表
		if("query".equalsIgnoreCase(active)){
			//String BJBH_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("BJBH_CX")), "UTF-8");
			String BJMC_CX = URLDecoder.decode(MyTools.StrFiltr(request.getParameter("BJMC_CX")), "UTF-8");
			String XBDM_CX = MyTools.StrFiltr(request.getParameter("XBDM_CX"));
			String NJDM_CX = MyTools.StrFiltr(request.getParameter("NJDM_CX"));
			String SSZY_CX = MyTools.StrFiltr(request.getParameter("SSZY_CX"));
			
			try {
				jsonV = bean.queryRec(pageNum, pageSize, BJMC_CX, XBDM_CX, NJDM_CX, SSZY_CX);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//新建
		if("new".equalsIgnoreCase(active)){
			String BZRGH = MyTools.StrFiltr(request.getParameter("BZRGH"));
			try {
				bean.addRec(BZRGH);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//编辑
		if("edit".equalsIgnoreCase(active)){
			try {
				bean.modRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//删除
		if("del".equalsIgnoreCase(active)){
			try {
				bean.delRec();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//删除学生
				if("delmore".equalsIgnoreCase(active)){
					String BHARRAY=MyTools.StrFiltr(request.getParameter("bHarray"));
					String jxbbh=MyTools.StrFiltr(request.getParameter("jxbbh"));
					try {
						bean.delmoreRec(BHARRAY,jxbbh);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
		//查询学生列表
		if("loadStuList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadStuList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//学生名单添加
				if("add".equalsIgnoreCase(active)){
					String XSMDXZXH=MyTools.StrFiltr(request.getParameter("XSMDXZXH"));;//学生名单新增学号
					System.out.println("XSMDXZXH "+XSMDXZXH);
					String JXBH=MyTools.StrFiltr(request.getParameter("xjbbd"));//教学编号
					System.out.println("JXBH "+JXBH);
			try {
						bean.addXSMD(JXBH,XSMDXZXH);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			//学生换班
				if("change".equalsIgnoreCase(active)){
					String HBH=MyTools.StrFiltr(request.getParameter("HBH"));;
					String bdtARRAY=MyTools.StrFiltr(request.getParameter("bdtARRAY"));
					String bjbh=MyTools.StrFiltr(request.getParameter("BJBH"));
						System.out.println("bjbh///////  "+bjbh);
					try {
						bean.change(HBH,bdtARRAY);
						jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
						response.getWriter().write(jal.toString());
						} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				//查询班级学生树数据
				if("queClassStuTree".equalsIgnoreCase(active)){
					String parentCode = MyTools.StrFiltr(request.getParameter("parentCode"));
					String classCode = MyTools.StrFiltr(request.getParameter("classCode"));
					try {
						jsonV = bean.queClassStuTree(parentCode, classCode);
						jal = (JSONArray) jsonV.get(2);
						response.getWriter().write(jal.toString());
					}catch(SQLException e){
						e.printStackTrace();
					}catch (WrongSQLException e) {
						e.printStackTrace();
					}
				}
	}
			
	/**
	* 从界面没获取参数
	* @date 
	* @author:yeq
    * @param request
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, JxbSetBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setBJBH(MyTools.StrFiltr(request.getParameter("BJBH"))); //行政班代码
		bean.setBJMC(MyTools.StrFiltr(request.getParameter("BJMC"))); //行政班名称
		bean.setXBDM(MyTools.StrFiltr(request.getParameter("XBDM"))); //系部代码
		bean.setNJDM(MyTools.StrFiltr(request.getParameter("NJDM"))); //年级代码
		bean.setSSZY(MyTools.StrFiltr(request.getParameter("SSZY"))); //专业代码
		bean.setZRS(MyTools.StrFiltr(request.getParameter("ZRS"))); //总人数
		bean.setBZR(MyTools.StrFiltr(request.getParameter("BZRGH"))); //班主任
		bean.setBJJC(MyTools.StrFiltr(request.getParameter("BJJC"))); //班级简称
		bean.setBJLX(MyTools.StrFiltr(request.getParameter("BJLX"))); //班级类型
		bean.setJSBH(MyTools.StrFiltr(request.getParameter("JSBH"))); //班级类型
	}
}