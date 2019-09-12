package com.pantech.src.devolop.outsideExam;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;
import com.pantech.src.devolop.ruleManage.CourseSetBean;

public class Svl_outsideExam extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Svl_outsideExam() {
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
			
		//设置字符编码为UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
						
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		
		OutsideExamBean bean = new OutsideExamBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		System.out.println("active-------------------------------------------------------"+active);
		
		//上传图片页面显示的处理方法。注：此方法必须在doget方法中调用。 
		//lupengfei 2014-3-31
		if("showImg".equalsIgnoreCase(active)){
			String filepath="";
			try{
						String name=MyTools.StrFiltr(request.getParameter("name"));
						String path=MyTools.StrFiltr(request.getParameter("path"));
						filepath=path;
						FileInputStream hFile = new FileInputStream(filepath); // 以byte流的方式打开文件d:\1.gif  
						//FileInputStream hFile = new FileInputStream(path);
						int i = hFile.available(); // 得到文件大小           
						byte data[] = new byte[i];       
						hFile.read(data); // 读数据          
						hFile.close();           
						response.setContentType("image/*"); // 设置返回的文件类型           
						OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象           
						toClient.write(data); // 输出数据     
						//System.out.println("流关闭了---------------"+toClient.toString());
						toClient.close();       
			} catch (IOException e) {// 错误处理          
						PrintWriter toClient = response.getWriter(); // 得到向客户端输出文本的对象          
						//response.setContentType("text/html;charset=gb2312");   
						response.setContentType("text/html;charset=utf-8");   
						toClient.write("无法打开图片!");           
						toClient.close();
			}
		}
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
		OutsideExamBean bean = new OutsideExamBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//新建外考考试
		if("newOutsideExam".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.newOutsideExam();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取选修课信息
		if("loadOutsideExam".equalsIgnoreCase(active)){  
			String SC_KSMC = URLDecoder.decode(request.getParameter("SC_KSMC"),"utf-8");
			try {
				jsonV = bean.loadOutsideExam(pageNum, pageSize,SC_KSMC);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		//编辑选修课班级
		if("editOutsideExam".equalsIgnoreCase(active)){
			//String bz = URLDecoder.decode(request.getParameter("SC_KSMC"),"utf-8");
			try {
				//查询列表
				bean.editOutsideExam();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		//删除选修课班级
		if("checkExamStu".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.checkExamStu();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//删除选修课班级
		if("delOutsideExam".equalsIgnoreCase(active)){
			try {
				//查询列表
				bean.delOutsideExam();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//选课信息
		if("loadSelection".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				//查询学年学期列表
				jsonV = bean.loadSelection(pageNum, pageSize,iUSERCODE);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//学生信息
				jsonV=bean.getStuInfo(iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "Student", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//选课信息
		if("loadSelectionXJ".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				//查询学年学期列表
				jsonV = bean.loadSelection(pageNum, pageSize,iUSERCODE);
				jal = (JSONArray)jsonV.get(2);				
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//选课
		if("saveSelection".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				//查询学年学期列表
				bean.saveSelection(iKeyCode,iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//学生报名信息
		if("loadGridBMInfo".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				jsonV = bean.loadGridBMInfo(pageNum, pageSize,iUSERCODE);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//删除外考报名
		if("delWKSelection".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));

			try {
				bean.delWKSelection(iKeyCode,iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//查询报名外考的学生
		if("loadGridWKStu".equalsIgnoreCase(active)){
					try {
						jsonV = bean.loadGridWKStu(pageNum, pageSize);
						jal = (JSONArray)jsonV.get(2);
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
						TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		//查询学生,按可选专业
		if("loadGridStuExam".equalsIgnoreCase(active)){
			String stuid = MyTools.StrFiltr(request.getParameter("stuid"));
			String stuname = URLDecoder.decode(request.getParameter("stuname"),"utf-8");
			try {
					jsonV = bean.loadGridStuExam(pageNum, pageSize,stuid,stuname);
					jal = (JSONArray)jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
					TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		//添加所选学生
		if("submitStuExam".equalsIgnoreCase(active)){
			String stunumarray = MyTools.StrFiltr(request.getParameter("stunumarray"));
			String stunamearray = MyTools.StrFiltr(request.getParameter("stunamearray"));
			try {
				bean.submitStuExam(pageNum, pageSize,stunumarray,stunamearray);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//删除学生报名
		if("delStudentExam".equalsIgnoreCase(active)){
			String stuidarray = MyTools.StrFiltr(request.getParameter("stuidarray"));
			try {
				bean.delStudentExam(pageNum, pageSize,stuidarray);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//学生报名信息
		if("loadGridWKInfo".equalsIgnoreCase(active)){
			String xh = MyTools.StrFiltr(request.getParameter("xh"));
			String xm = URLDecoder.decode(request.getParameter("xm"),"utf-8");
			String wkmc = URLDecoder.decode(request.getParameter("wkmc"),"utf-8");
			try {
				jsonV = bean.loadGridWKInfo(pageNum, pageSize,xh,xm,wkmc);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//上传图片
		if("uploadpic".equalsIgnoreCase(active)){
			String ic_USERCODE=MyTools.StrFiltr(request.getParameter("ic_USERCODE"));
			String savePath = MyTools.getProp(request, "Base.upLoadPathFile");//this.getServletConfig().getServletContext().getRealPath("");  
			//上传路径按录入人员/课程分类
					
			bean.uploadpic(savePath, request, response,ic_USERCODE);
					
			response.getWriter().write(bean.getMSG());
			TraceLog.Trace(bean.getMSG());					
		}
		
		//获取照片路径
		if("getPhotoPath".equalsIgnoreCase(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
					
				bean.getPhotoPath(iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//上传方法,上传头像
		if("uploadPhoto".equals(active)){
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				ServletConfig sc = this.getServletConfig();
				bean.uploadImg(sc,response,iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//System.out.println("++++++++++++++头像名称"+bean.getMsg());
			}catch (SQLException e) {
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
		//批量上传学生照片
		if("uploadAllstuPhoto".equals(active)){
			try {
				ServletConfig sc = this.getServletConfig();
				bean.uploadAllstuPhoto(sc,response);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//System.out.println("++++++++++++++头像名称"+bean.getMsg());
			}catch (SQLException e) {
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
		//学生报名信息
		if("checkBM".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			try {
				bean.checkBM(pageNum, pageSize,iKeyCode,iUSERCODE);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//获取学生详细信息
		if("getStuInfo".equalsIgnoreCase(active)){
					String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
					try {
				jsonV = bean.getStuInfo(iUSERCODE);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		//获取学生详细信息
		if("exportWKClass".equalsIgnoreCase(active)){
			String exYear = MyTools.StrFiltr(request.getParameter("exYear"));
			String exMonth = MyTools.StrFiltr(request.getParameter("exMonth"));
			String exDate = MyTools.StrFiltr(request.getParameter("exDate"));
			String exTime = MyTools.StrFiltr(request.getParameter("exTime"));
			try {
				String filePath = bean.exportWKClass(exYear,exMonth,exDate,exTime);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//获取学生详细信息
		if("checkWKBMRS".equalsIgnoreCase(active)){
			try {
				bean.checkWKBMRS();
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
    * @param MajorSetBean
    */
	private void getFormData(HttpServletRequest request, OutsideExamBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setWK_KSBH(MyTools.StrFiltr(request.getParameter("WK_KSBH"))); //外考科目编号
		bean.setWK_KSMC(MyTools.StrFiltr(request.getParameter("WK_KSMC"))); //外考科目名称
		bean.setWK_BMKSSJ(MyTools.StrFiltr(request.getParameter("WK_BMKSSJ"))); //报名开始时间
		bean.setWK_BMJSSJ(MyTools.StrFiltr(request.getParameter("WK_BMJSSJ"))); //报名节数时间
		bean.setWK_KSRQ(MyTools.StrFiltr(request.getParameter("WK_KSRQ"))); //考试日期
		bean.setWK_BMFY(MyTools.StrFiltr(request.getParameter("WK_BMFY"))); //报名费用
		bean.setWK_BZ(MyTools.StrFiltr(request.getParameter("WK_BZ")));//备注
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
