package com.pantech.src.devolop.ruleManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;


import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;
import com.pantech.src.devolop.ruleManage.ImportBean;


public class Svl_Import extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	public Svl_Import() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
				// TODO Auto-generated method stub
				request.setCharacterEncoding("UTF-8");//request编码类型
				response.setContentType("text/html;charset=UTF-8");//reponse类型统一
				
				Vector vector=new Vector();
				JSONArray jsonArray = new JSONArray(); 
				Vector jsonV = null;//返回结果集
				JSONArray jal = null;//返回json对象
				
				ImportBean  bean = new ImportBean(request);
				ServletConfig sc = this.getServletConfig();
				SmartUpload mySmartUpload = new SmartUpload("UTF-8");
				mySmartUpload.initialize(sc, request, response);
				try {
					mySmartUpload.upload();
				} catch (SmartUploadException exception1) {
					// TODO 自动生成 catch 块
					exception1.printStackTrace();
				}
				getParameters(request, bean,mySmartUpload);
				String active = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("active"));//获取前台提交的操作类型
				String term = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("ic_term"));//获取前台提交的学期
				System.out.println("active="+active);
				
				//导入授课计划
				if(active.equalsIgnoreCase("uploadImport")){
						System.out.println("导入授课计划");	
						String weeks = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("weeks"));
						String ic_XNXQ = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("ic_XNXQ"));
						String ic_ZYMC = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("ic_ZYMC"));
						try {
							bean.saveimportxls(mySmartUpload,term,weeks,ic_XNXQ,ic_ZYMC);//试题xls解析保存
							jsonArray = JsonUtil.addJsonParams(jsonArray, "MSG", bean.getMSG());
							jsonArray = JsonUtil.addJsonParams(jsonArray, "MSG2", bean.getMSG2());
							jsonArray = JsonUtil.addJsonParams(jsonArray, "SKTERM", bean.getSKTERM());
							response.getWriter().write(jsonArray.toString());
							TraceLog.Trace(jsonArray.toString());
						} catch (SQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (SmartUploadException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (WrongSQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						}
				}
				
				//导入交费信息
				if(active.equalsIgnoreCase("uploadImportCost")){
					System.out.println("导入交费信息");	
						try {
							bean.uploadImportCost(mySmartUpload);//试题xls解析保存
							jsonArray = JsonUtil.addJsonParams(jsonArray, "MSG", bean.getMSG());
							response.getWriter().write(jsonArray.toString());
							
						} catch (SQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (SmartUploadException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (WrongSQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						}
				}
				
				//导入考试信息
				if(active.equalsIgnoreCase("importExamInfo")){
					System.out.println("导入考试信息");	
					String importType = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("ic_importType"));//获取前台提交的操作类型
					String ic_xnxq = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("ic_xnxq"));//获取前台提交的操作类型
					String ic_qzqm = MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("ic_qzqm"));//获取前台提交的操作类型
					try {
							bean.importExamInfo(mySmartUpload,importType,ic_xnxq,ic_qzqm);//试题xls解析保存
							jsonArray = JsonUtil.addJsonParams(jsonArray, "MSG", bean.getMSG());
							response.getWriter().write(jsonArray.toString());
							
						} catch (SQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (SmartUploadException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (WrongSQLException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						}
				}
				
				
				
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	//获得页面参数
	public void getParameters(HttpServletRequest request,ImportBean bean,SmartUpload mySmartUpload){
			//bean.setKechen(MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("kechen"))); //		
			//bean.setBookid(MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("bookid"))); //
			
	}
	
}
