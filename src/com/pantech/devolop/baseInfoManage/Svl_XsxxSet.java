package com.pantech.devolop.baseInfoManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;

import org.apache.commons.lang.StringEscapeUtils;

import net.sf.json.JSONArray;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.zhuozhengsoft.pageoffice.PageOfficeLink;

public class Svl_XsxxSet extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Svl_XsxxSet() {
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//设置字符编码为UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String active=MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum=MyTools.parseInt(request.getParameter("page"));// 拿取前台的page值
		int pagSize=MyTools.parseInt(request.getParameter("rows"));// 拿取前台的rows值
		Vector jsonV=null;//返回结果集
		JSONArray jal=null;//返回json数组
		XsxxSetBean bean=new XsxxSetBean(request);//对象
		this.getFormData(request, bean);
		System.out.println(active);
		//查询学生信息列表
		if("queStudent".equalsIgnoreCase(active)){
			bean.setJX_XJH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_XJH_CX")),"UTF-8"));//学籍号
			bean.setJX_XH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_XH_CX")),"UTF-8"));//学号
			bean.setJX_XM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_XM_CX")),"UTF-8"));
			bean.setJX_XZBDM(MyTools.StrFiltr(request.getParameter("JX_XZBDM_CX")));
			bean.setJX_YYXSH(MyTools.StrFiltr(request.getParameter("JX_XBDM_CX")));
			bean.setJX_YZYM(MyTools.StrFiltr(request.getParameter("JX_ZYDM_CX")));
			bean.setJX_YNJ(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_NJDM_CX")),"UTF-8"));
			
			try {
				jsonV=bean.queStudent(pageNum, pagSize);
				if(jsonV!=null&&jsonV.size()>0){
					jal=(JSONArray) jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG() );
				response.getWriter().write(jal.toString());
			}
		}
		
		//保存学生信息
		if("save".equalsIgnoreCase(active)){
			String invoke=MyTools.StrFiltr(request.getParameter("invoke"));
			bean.setJX_ZPLJ(MyTools.StrFiltr(request.getParameter("JX_ZP")));//照片路径
			
			try {
				if(invoke.equalsIgnoreCase("new")){
					bean.AddStudent();
				}else if(invoke.equalsIgnoreCase("edit")){
					String code = MyTools.StrFiltr(request.getParameter("JX_YXJH"));
					String stuCode = MyTools.StrFiltr(request.getParameter("JX_YXH"));
					String stuName = MyTools.StrFiltr(request.getParameter("JX_YXM"));
					
					bean.ModStudent(code, stuCode, stuName);
				}
				
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "保存失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//删除一个学生
		if("del".equalsIgnoreCase(active)){
			try {
				String xhInfo=MyTools.StrFiltr(request.getParameter("JX_XH"));//学号
				bean.DelRec(xhInfo);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG","删除失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//查询下拉框
		if("initData".equalsIgnoreCase(active)){
			try {
				//班级
				jsonV = bean.loadBJCombo();
				jal = JsonUtil.addJsonParams(jal, "bjData", ((JSONArray)jsonV.get(2)).toString());
				//异动下拉框
				jsonV = bean.loadYDCombo();
				jal = JsonUtil.addJsonParams(jal, "ydData", ((JSONArray)jsonV.get(2)).toString());
				//性别码
				jsonV = bean.loadXBCombo();
				jal = JsonUtil.addJsonParams(jal, "xbData", ((JSONArray)jsonV.get(2)).toString());
				//学生类别码
				jsonV = bean.loadXSLBCombo();
				jal = JsonUtil.addJsonParams(jal, "xslbData", ((JSONArray)jsonV.get(2)).toString());
				//民族代码
				jsonV = bean.loadMZCombo();
				jal = JsonUtil.addJsonParams(jal, "mzData", ((JSONArray)jsonV.get(2)).toString());
				//血型代码
				jsonV = bean.loadXXCombo();
				jal = JsonUtil.addJsonParams(jal, "xxData", ((JSONArray)jsonV.get(2)).toString());
				//港澳台侨代码
				jsonV = bean.loadGATQCombo();
				jal = JsonUtil.addJsonParams(jal, "gatqData", ((JSONArray)jsonV.get(2)).toString());
				//政治面貌
				jsonV = bean.loadZZMMCombo();
				jal = JsonUtil.addJsonParams(jal, "zzmmData", ((JSONArray)jsonV.get(2)).toString());
				//国别代码
				jsonV = bean.loadGBCombo();
				jal = JsonUtil.addJsonParams(jal, "gbData", ((JSONArray)jsonV.get(2)).toString());
				//健康状况
				jsonV = bean.loadJKZKCombo();
				jal = JsonUtil.addJsonParams(jal, "jkztData", ((JSONArray)jsonV.get(2)).toString());
				//城市代码
				jsonV = bean.loadCSDMCombo();
				jal = JsonUtil.addJsonParams(jal, "csData", ((JSONArray)jsonV.get(2)).toString());
				//户口性质
				jsonV = bean.loadHKXZCombo();
				jal = JsonUtil.addJsonParams(jal, "hkxzData", ((JSONArray)jsonV.get(2)).toString());
				//流动人口状况
				jsonV = bean.loadLDRKCombo();
				jal = JsonUtil.addJsonParams(jal, "ldrkzkData", ((JSONArray)jsonV.get(2)).toString());
				//招生类别码
				jsonV = bean.loadZSLBCombo();
				jal = JsonUtil.addJsonParams(jal, "zslbData", ((JSONArray)jsonV.get(2)).toString());
				//身份证件类型码hyzkData
				jsonV = bean.loadSFZJLXCombo();
				jal = JsonUtil.addJsonParams(jal, "sfzjlxData", ((JSONArray)jsonV.get(2)).toString());		
				//婚姻状况
				jsonV = bean.loadHYZKCombo();
				jal = JsonUtil.addJsonParams(jal, "hyzkData", ((JSONArray)jsonV.get(2)).toString());	
				//系名称
				jsonV = bean.loadXMCCombo();
				jal = JsonUtil.addJsonParams(jal, "xmcData", ((JSONArray)jsonV.get(2)).toString());	
				//专业名称
				jsonV = bean.loadZYMCCombo();
				jal = JsonUtil.addJsonParams(jal, "zymcData", ((JSONArray)jsonV.get(2)).toString());
				//年级名称
				jsonV = bean.loadNJMCCombo();
				jal = JsonUtil.addJsonParams(jal, "njmcData", ((JSONArray)jsonV.get(2)).toString());
				//查询班级下拉框异动下拉框
				jsonV = bean.loadStuClassCombo();
				jal = JsonUtil.addJsonParams(jal, "allbjData", ((JSONArray)jsonV.get(2)).toString());
					
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "bjData", "");
				jal = JsonUtil.addJsonParams(jal, "ydData", "");
				jal = JsonUtil.addJsonParams(jal, "allbjData", "");
				response.getWriter().write(jal.toString());
			}
		}
		if("initExportData".equalsIgnoreCase(active)){
			try {
				//查询导出年级下拉框
				jsonV = bean.loadExportNjCombo();
				jal = JsonUtil.addJsonParams(jal, "exportNjData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询系部下拉框
				jsonV = bean.loadExportXbCombo();
				jal = JsonUtil.addJsonParams(jal, "exportXbData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询专业专业下拉框
				jsonV = bean.loadExportZyCombo();
				jal = JsonUtil.addJsonParams(jal, "exportZyData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询批量导出班级下拉框
		if("loadExportBjCombo".equalsIgnoreCase(active)){
			String NJDM = MyTools.StrFiltr(request.getParameter("NJDM"));
			String XBDM = MyTools.StrFiltr(request.getParameter("XBDM"));
			String SSZY = MyTools.StrFiltr(request.getParameter("SSZY"));
			
			try {
				jsonV = bean.loadExportBjCombo(NJDM,XBDM,SSZY);
				jal = (JSONArray) jsonV.get(2);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("exportScoreInfo".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));//导出类型
			String NJDM = MyTools.StrFiltr(request.getParameter("NJDM"));
			String XBDM = MyTools.StrFiltr(request.getParameter("XBDM"));//导出班级
			String SSZY = MyTools.StrFiltr(request.getParameter("SSZY"));
			String BJBH = MyTools.StrFiltr(request.getParameter("BJBH"));
			
			try {
				String linkStr = "";
				if("studentInfo".equalsIgnoreCase(exportType)){
						linkStr = PageOfficeLink.openWindow(request, "form/registerScoreManage/stuInfoView.jsp?exportType="+ exportType+
								"&NJDM="+NJDM+ "&XBDM=" + XBDM +"&SSZY=" + SSZY + "&BJBH=" + BJBH+"","width=1000px;height=800px;");
				}
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG() );
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("".equalsIgnoreCase(active)){
			ServletConfig sc = this.getServletConfig();
			SmartUpload mySmartUpload = new SmartUpload("UTF-8");
			mySmartUpload.initialize(sc, request, response);
			try {
				mySmartUpload.upload();
			} catch (SmartUploadException exception1) {
				// TODO 自动生成 catch 块
				exception1.printStackTrace();
			}
			String active2=MyTools.StrFiltr(mySmartUpload.getRequest().getParameter("active2"));// 拿取前台的active值
			
			//学生信息设置导入
			if ("Savestudentimportxls".equalsIgnoreCase(active2)) {
				try {
					bean.Savestudentimportxls(mySmartUpload);
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
					jal = JsonUtil.addJsonParams(jal, "PromptMsg", bean.getMSG2());
					response.getWriter().write(jal.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				//保存照片路径
				try {
					bean.uploadImg(mySmartUpload);
					jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
					response.getWriter().write(jal.toString());
					//System.out.println("++++++++++++++头像名称"+bean.getMsg());
				}catch (Exception e) {
					e.printStackTrace();
					jal = JsonUtil.addJsonParams(jal, "MSG","");
					response.getWriter().write(jal.toString());
				}
			}
		}
		
		if("savePhoto".equalsIgnoreCase(active)){
			String SFZJH=MyTools.StrFiltr(request.getParameter("SFZJH"));
			String savePath=MyTools.StrFiltr(request.getParameter("savePath"));
			bean.saveImp(SFZJH,savePath);
			jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
			response.getWriter().write(jal.toString());
		}
		
		//学生信息设置导出
		if("ExportExcelStudent".equalsIgnoreCase(active)){
			try {
				String filePath = bean.ExportExcelStudent();
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//学生信息设置导出
		if("ExportExcelStuScoreall".equalsIgnoreCase(active)){
			try {
				String lieInfo=MyTools.StrFiltr(request.getParameter("lieInfo"));
				String bjbh=MyTools.StrFiltr(request.getParameter("bjbh"));
				String bjdm=MyTools.StrFiltr(request.getParameter("bjdm"));
				String filePath = bean.ExportExcelStuScoreall(lieInfo,bjbh,bjdm);
				jal = JsonUtil.addJsonParams(jal, "MSG",bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//查询异动信息
		if("queStudent_Abn".equalsIgnoreCase(active)){
			bean.setJX_XH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_XH_Abn")),"UTF-8"));
			bean.setJX_XM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_XM_Abn")),"UTF-8"));
			try {
				jsonV=bean.queStudentAbn(pageNum, pagSize);
				if(jsonV!=null&&jsonV.size()>0){
					jal=(JSONArray) jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG() );
				response.getWriter().write(jal.toString());
			}
		}
		
		//查询个人异动Info
		if("queStudent_AbnInfo".equalsIgnoreCase(active)){
			bean.setJX_XH(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_XH")),"UTF-8"));
			bean.setJX_XM(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("JX_XM_Abn")),"UTF-8"));
			try {
				jsonV=bean.queStudent_AbnInfo(pageNum, pagSize);
				if(jsonV!=null&&jsonV.size()>0){
					jal=(JSONArray) jsonV.get(2);
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG() );
				response.getWriter().write(jal.toString());
			}
		}
		
		//修改打印预览文件
		if("renameViewFile".equalsIgnoreCase(active)){
			String filePath = MyTools.StrFiltr(request.getParameter("filePath"));
			try {
				String path = bean.renameFile(filePath);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG() );
				jal = JsonUtil.addJsonParams(jal, "filePath", path);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		//保存异动
		if("save_Abn".equalsIgnoreCase(active)){
			bean.setJX_YDLXM(MyTools.StrFiltr(request.getParameter("JX_YDLXM")));//异动类型码
			bean.setJX_YDRQ(MyTools.StrFiltr(request.getParameter("JX_YDRQ")));//异动日期
			bean.setJX_YBJDM(MyTools.StrFiltr(request.getParameter("JX_YBJDM")));//原班级代码
			bean.setJX_XBJDM(MyTools.StrFiltr(request.getParameter("JX_XBJDM")));//新班级代码
			bean.setJX_YDYY(MyTools.StrFiltr(request.getParameter("JX_YDYY")));//异动原因
			bean.setJX_YDLYXX(MyTools.StrFiltr(request.getParameter("JX_YDLYXX")));//异来源学校
			bean.setJX_YDQXXX(MyTools.StrFiltr(request.getParameter("JX_YDQXXX")));//异动去向学校
			bean.setJX_YYXSH(MyTools.StrFiltr(request.getParameter("JX_YYXSH")));//原院系所好
			bean.setJX_YZYM(MyTools.StrFiltr(request.getParameter("JX_YZYM")));//原专业吗
			bean.setJX_YBH(MyTools.StrFiltr(request.getParameter("JX_YBH")));//原班号
			bean.setJX_YNJ(MyTools.StrFiltr(request.getParameter("JX_YNJ")));//原年级
			bean.setJX_XYXSH(MyTools.StrFiltr(request.getParameter("JX_XYXSH")));//现院系所号
			bean.setJX_XZYM(MyTools.StrFiltr(request.getParameter("JX_XZYM")));//现专业码
			bean.setJX_XBH(MyTools.StrFiltr(request.getParameter("JX_XBH")));//现班号
			bean.setJX_XNJ(MyTools.StrFiltr(request.getParameter("JX_XNJ")));//现年级
			bean.setJX_XH(MyTools.StrFiltr(request.getParameter("JX_XH")));//学号
			bean.setJX_XM(MyTools.StrFiltr(request.getParameter("JX_XM")));//姓名
			bean.setJX_SFZJH(MyTools.StrFiltr(request.getParameter("JX_SFZJH")));//身份证件号
			try {
				bean.AddAbn();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "保存失败");
				response.getWriter().write(jal.toString());
			}
		}
		
		//切换学籍状态
		if("changeState".equalsIgnoreCase(active)){
			try {
				bean.changeState();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG","保存失败");
				response.getWriter().write(jal.toString());
			}
		}
		if("loadTeaPageOfficeLink".equalsIgnoreCase(active)){
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String JX_XJH = MyTools.StrFiltr(request.getParameter("JX_XJH"));
			String BJBH = MyTools.StrFiltr(request.getParameter("BJBH"));
			String XSLX = MyTools.StrFiltr(request.getParameter("XSLX"));
			String XM = MyTools.StrFiltr(request.getParameter("XM"));
			try {
				String linkStr = "";
				if("classKcb".equalsIgnoreCase(exportType)){
					linkStr = PageOfficeLink.openWindow(request, "form/registerScoreManage/stuInfoView.jsp?exportType="+ exportType+
						"&JX_XJH="+JX_XJH+ "&BJBH=" + BJBH +"&XSLX=" + XSLX +"&XM=" + URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(XM, "UTF-8"), "UTF-8"), "UTF-8")   + "","width=1000px;height=800px;");
				}
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//批量上传学生照片
		if("uploadAllstuPhoto".equals(active)){
			try {
				ServletConfig sc = this.getServletConfig();
				SmartUpload su = new SmartUpload("UTF-8");
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
		
		//导出多个班级学生成绩信息
		if("exportScoreInfo2".equalsIgnoreCase(active)){
			String NJDM = MyTools.StrFiltr(request.getParameter("NJDM"));
			String XBDM = MyTools.StrFiltr(request.getParameter("XBDM"));//导出班级
			String SSZY = MyTools.StrFiltr(request.getParameter("SSZY"));
			String BJBH = MyTools.StrFiltr(request.getParameter("BJBH"));
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			String JX_XJH = MyTools.StrFiltr(request.getParameter("JX_XJH"));
			String XM = MyTools.StrFiltr(request.getParameter("XM"));
			
			try {
				String filePath = bean.exportScoreInfo2(XBDM,NJDM,SSZY,BJBH,exportType,JX_XJH,XM);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		
		//删除学生照片
		if("delZP".equalsIgnoreCase(active)){
			String JX_XHZP= MyTools.StrFiltr(request.getParameter("JX_XH"));
			try {
				bean.delZPLJ(JX_XHZP);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//删除预览学生照片
		if("emptyfile".equalsIgnoreCase(active)){
			try {
				bean.emptyfile();
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
	* @date 
	* @author:Maowei
    * @param request
    * @param XsxxSetBean
    */
	private void getFormData(HttpServletRequest request, XsxxSetBean bean) {
		// TODO Auto-generated method stub
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setJX_XJH(MyTools.StrFiltr(request.getParameter("JX_XJH")));//学籍号
		bean.setJX_XH(MyTools.StrFiltr(request.getParameter("JX_XH")));//学号
		bean.setJX_XM(MyTools.StrFiltr(request.getParameter("JX_XM")));//姓名
		bean.setJX_YWXM(MyTools.StrFiltr(request.getParameter("JX_YWXM")));//英文姓名
		bean.setJX_XMPY(MyTools.StrFiltr(request.getParameter("JX_XMPY")));//姓名拼音
		bean.setJX_XZBDM(MyTools.StrFiltr(request.getParameter("JX_XZBJ")));//行政班代码
		bean.setJX_BNXH(MyTools.StrFiltr(request.getParameter("JX_BNXH")));//班内学号
		bean.setJX_CYM(MyTools.StrFiltr(request.getParameter("JX_CYM")));//曾用名
		bean.setJX_XBM(MyTools.StrFiltr(request.getParameter("JX_XBM")));//性别码
		bean.setJX_RXRQ(MyTools.StrFiltr(request.getParameter("JX_RXRQ")));//入学日期
		bean.setJX_BRSJ(MyTools.StrFiltr(request.getParameter("JX_BRSJ")));//本人手机
		bean.setJX_JTYB(MyTools.StrFiltr(request.getParameter("JX_JTYB")));//家庭邮编
		bean.setJX_SFBSHJ(MyTools.StrFiltr(request.getParameter("JX_SFBSHJ")));//是否本市户籍
		bean.setJX_BYLXRQ(MyTools.StrFiltr(request.getParameter("JX_BYLXRQ")));//毕业离校日期
		bean.setJX_BYNF(MyTools.StrFiltr(request.getParameter("JX_BYNF")));//毕业年份
		bean.setJX_XSLBM(MyTools.StrFiltr(request.getParameter("JX_XSLBM")));//学生类别码
		bean.setJX_SFZJH(MyTools.StrFiltr(request.getParameter("JX_SFZJH")));//身份证件号
		bean.setJX_CSNY(MyTools.StrFiltr(request.getParameter("JX_CSNY")));//出身年月
		bean.setJX_MZM(MyTools.StrFiltr(request.getParameter("JX_MZM")));//民族码
		bean.setJX_XXM(MyTools.StrFiltr(request.getParameter("JX_XXM")));//血型码
		bean.setJX_ZJXY(MyTools.StrFiltr(request.getParameter("JX_ZJXY")));//宗教信仰
		bean.setJX_GATQM(MyTools.StrFiltr(request.getParameter("JX_GATQM")));//港澳台侨
		bean.setJX_ZZMMM(MyTools.StrFiltr(request.getParameter("JX_ZZMMM")));//政治面貌
		bean.setJX_FQZZMMM(MyTools.StrFiltr(request.getParameter("JX_FQZZMMM")));//父亲政治面貌
		bean.setJX_MQZZMMM(MyTools.StrFiltr(request.getParameter("JX_MQZZMMM")));//母亲政治面貌
		bean.setJX_QTCYZZMMM(MyTools.StrFiltr(request.getParameter("JX_QTCYZZMMM")));//其他成员政治面貌
		bean.setJX_GBM(MyTools.StrFiltr(request.getParameter("JX_GBM")));//国籍
		bean.setJX_BZ(MyTools.StrFiltr(request.getParameter("JX_BZ")));//备注
		bean.setJX_JKZKM(MyTools.StrFiltr(request.getParameter("JX_JKZKM")));//健康状况码
		bean.setJX_JGM(MyTools.StrFiltr(request.getParameter("JX_JGM")));//籍贯码
		bean.setJX_HYZK(MyTools.StrFiltr(request.getParameter("JX_HYZK")));//婚姻状况
		bean.setJX_CSDZM(MyTools.StrFiltr(request.getParameter("JX_CSDZM")));//出生地址
		bean.setJX_BYXX(MyTools.StrFiltr(request.getParameter("JX_BYXX")));//毕业学校
		bean.setJX_XZZZ(MyTools.StrFiltr(request.getParameter("JX_XZZZ")));//现在住址
		bean.setJX_HKSZD(MyTools.StrFiltr(request.getParameter("JX_HKSZD")));//户口所在地
		bean.setJX_HKLBM(MyTools.StrFiltr(request.getParameter("JX_HKLBM")));//户口类别码
		bean.setJX_SSPCS(MyTools.StrFiltr(request.getParameter("JX_SSPCS")));//所属派出所
		bean.setJX_TXDZ(MyTools.StrFiltr(request.getParameter("JX_TXDZ")));//通讯地址
		bean.setJX_YZBM(MyTools.StrFiltr(request.getParameter("JX_YZBM")));//邮政编码
		bean.setJX_ZSLBM(MyTools.StrFiltr(request.getParameter("JX_ZSLBM")));//招生类别码
		bean.setJX_PZH(MyTools.StrFiltr(request.getParameter("JX_PZH")));//拍照号
		bean.setJX_SFZJLXM(MyTools.StrFiltr(request.getParameter("JX_SFZJLXM")));//身份证件类型码
		bean.setJX_QTCYDH(MyTools.StrFiltr(request.getParameter("JX_QTCYDH")));//其他成员电话/
		bean.setJX_QTCYNL(MyTools.StrFiltr(request.getParameter("JX_QTCYNL")));//其他成员年龄
		bean.setJX_QTCYGZDW(MyTools.StrFiltr(request.getParameter("JX_QTCYGZDW")));//其他成员工作单位
		bean.setJX_QTCYCW(MyTools.StrFiltr(request.getParameter("JX_QTCYCW")));//其他成员称谓
		bean.setJX_QTCYXM(MyTools.StrFiltr(request.getParameter("JX_QTCYXM")));//其他成员姓名
		bean.setJX_MQDH(MyTools.StrFiltr(request.getParameter("JX_MQDH")));//母亲电话
		bean.setJX_MQNL(MyTools.StrFiltr(request.getParameter("JX_MQNL")));//母亲年龄
		bean.setJX_MQGZDW(MyTools.StrFiltr(request.getParameter("JX_MQGZDW")));//母亲工作单位
		bean.setJX_MQXM(MyTools.StrFiltr(request.getParameter("JX_MQXM")));//母亲姓名
		bean.setJX_FQXM(MyTools.StrFiltr(request.getParameter("JX_FQXM")));//父亲姓名
		bean.setJX_FQNL(MyTools.StrFiltr(request.getParameter("JX_FQNL")));//父亲年龄
		bean.setJX_FQDH(MyTools.StrFiltr(request.getParameter("JX_FQDH")));//父亲电话父亲电话
		bean.setJX_FQGZDW(MyTools.StrFiltr(request.getParameter("JX_FQGZDW")));//父亲工作单位
		bean.setJX_ZPLJ(MyTools.StrFiltr(request.getParameter("JX_ZPLJ")));//照片路径
		bean.setJX_LDRKZK(MyTools.StrFiltr(request.getParameter("JX_LDRKZK")));//流动人口
		
		bean.setJX_YDLXM(MyTools.StrFiltr(request.getParameter("JX_YDLXM")));//异动类型码
		bean.setJX_YDRQ(MyTools.StrFiltr(request.getParameter("JX_YDRQ")));//异动日期
		bean.setJX_YBJDM(MyTools.StrFiltr(request.getParameter("JX_YBJDM")));//原班级代码
		bean.setJX_XBJDM(MyTools.StrFiltr(request.getParameter("JX_XBJDM")));//新班级代码
		bean.setJX_YDYY(MyTools.StrFiltr(request.getParameter("JX_YDYY")));//异动原因
		bean.setJX_YDLYXX(MyTools.StrFiltr(request.getParameter("JX_YDLYXX")));//异来源学校
		bean.setJX_YDQXXX(MyTools.StrFiltr(request.getParameter("JX_YDQXXX")));//异动去向学校
		bean.setJX_YYXSH(MyTools.StrFiltr(request.getParameter("JX_YYXSH")));//原院系所好
		bean.setJX_YZYM(MyTools.StrFiltr(request.getParameter("JX_YZYM")));//原专业吗
		bean.setJX_YBH(MyTools.StrFiltr(request.getParameter("JX_YBH")));//原班号
		bean.setJX_YNJ(MyTools.StrFiltr(request.getParameter("JX_YNJ")));//原年级
		bean.setJX_XYXSH(MyTools.StrFiltr(request.getParameter("JX_XYXSH")));//现院系所号
		bean.setJX_XZYM(MyTools.StrFiltr(request.getParameter("JX_XZYM")));//现专业码
		bean.setJX_XBH(MyTools.StrFiltr(request.getParameter("JX_XBH")));//现班号
		bean.setJX_XNJ(MyTools.StrFiltr(request.getParameter("JX_XNJ")));//现年级
	}
}