package com.pantech.devolop.customExamManage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import net.sf.json.JSONArray;

import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.zhuozhengsoft.pageoffice.PageOfficeLink;

public class Svl_Yktj extends HttpServlet {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
		int pageNum = MyTools.parseInt(request.getParameter("page")); // 获得页面page参数
		int pageSize = MyTools.parseInt(request.getParameter("rows")); // 获得页面rows参数
		Vector jsonV = null;// 返回结果集
		JSONArray jal = null;// 返回json对象
		YktjBean bean = new YktjBean(request);
		this.getFormData(request, bean); // 获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			try {
				//查询月考班级课程列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				
				//查询班级下拉框
				jsonV = bean.loadBJCombo();
				jal = JsonUtil.addJsonParams(jal, "bjData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询课程下拉框
				jsonV = bean.loadKCCombo();
				jal = JsonUtil.addJsonParams(jal, "kcData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询学年学期下拉框
				jsonV = bean.loadXnxqCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//获取当前学年学期
				String curXnxq = bean.loadCurXnxq();
				jal = JsonUtil.addJsonParams(jal, "curXnxq", curXnxq);
				
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//查询
		if("query".equalsIgnoreCase(active)){
			try {
				//查询学年学期列表
				jsonV = bean.queryRec(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//读取年级标准分统计信息（图表）
		if("loadGradeyktjChart".equalsIgnoreCase(active)){
			String bj = MyTools.StrFiltr(request.getParameter("BJ"));
			String kc = MyTools.StrFiltr(request.getParameter("KC"));
			try {
				jsonV = bean.loadGradeyktjChart(bj,kc);
				jal = JsonUtil.addJsonParams(jal, "examInfo", MyTools.StrFiltr(jsonV.get(0)));
				jal = JsonUtil.addJsonParams(jal, "chartData", MyTools.StrFiltr(jsonV.get(1)));
				response.getWriter().write(jal.toString());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		//读取年级标准分统计datagrid数据
		if("loadGradeyktjList".equalsIgnoreCase(active)){
			try {
				jsonV = bean.loadGradeyktjList();
				jal = JsonUtil.addJsonParams(jal, "listData", MyTools.StrFiltr(jsonV.get(0)));
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//查询班级学生月考考试成绩列表信息
		if("queClassStuScoreList".equalsIgnoreCase(active)){
			bean.setSTUNAME(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUNAME")), "UTF-8")); //姓名
			
			try {
				jsonV = bean.queClassStuScoreList(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//查询班级学生月考考试成绩列表信息
		if("queClassStuScoreList2".equalsIgnoreCase(active)){
			bean.setSTUNAME(URLDecoder.decode(MyTools.StrFiltr(request.getParameter("STUNAME")), "UTF-8")); //姓名
			
			try {
				jsonV = bean.queClassStuScoreList2(pageNum, pageSize);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + MyTools.StrFiltr(jal.toString()) + "}");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//导出月考信息
		if("exportYK".equalsIgnoreCase(active)){
			String bj = MyTools.StrFiltr(request.getParameter("BJ"));
			String kc = MyTools.StrFiltr(request.getParameter("KC"));
			try {
				String filePath = bean.exportYK(bj,kc);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		//pageOffice
		if("loadClassPageOfficeLink".equalsIgnoreCase(active)){
			String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String exportType = MyTools.StrFiltr(request.getParameter("exportType"));
			
			String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
			String USERCODE = MyTools.StrFiltr(request.getParameter("USERCODE"));
			try {
				String linkStr = "";
				if("hztj".equalsIgnoreCase(exportType)){
					linkStr = PageOfficeLink.openWindow(request, "form/customExamManage/exportHztj.jsp?exportType=" + exportType
							+ "&xnxqbm=" + xnxqbm + "&sAuth=" + sAuth+"&USERCODE=" + USERCODE+ "","width=1200px;height=700px;");
				}
				
				jal = JsonUtil.addJsonParams(jal, "linkStr", linkStr);
				
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//导出月考信息
		if("exportYKXQ".equalsIgnoreCase(active)){
			String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
			String usercode = MyTools.StrFiltr(request.getParameter("usercode"));
			try {
				String filePath = bean.exportYKXQ(xnxqbm,sAuth,usercode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "filePath", filePath);
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		//删除打印预览文件
		if("delViewFile".equalsIgnoreCase(active)){
			String filePath = MyTools.StrFiltr(request.getParameter("filePath"));
			
			try {
				boolean flag = bean.deleteFile(filePath);
				if(flag)
					bean.setMSG("删除成功");
				else
					bean.setMSG("删除失败");
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		//判断当前学期是否有月考
		if("ykcount".equalsIgnoreCase(active)){
			String xnxqbm = MyTools.StrFiltr(request.getParameter("xnxqbm"));
			try {
				bean.pdykxx(xnxqbm);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
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
	private void getFormData(HttpServletRequest request, YktjBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); // 用户编号
		bean.setBJDM(MyTools.StrFiltr(request.getParameter("BJDM")));// 班级代码
		bean.setKCDM(MyTools.StrFiltr(request.getParameter("KCDM")));// 课程代码
		bean.setXNXQBM(MyTools.StrFiltr(request.getParameter("XNXQBM")));// 学年学期编码
		bean.setQSXNXQ(MyTools.StrFiltr(request.getParameter("QSXNXQ"))); //起始学年学期
		bean.setJSXNXQ(MyTools.StrFiltr(request.getParameter("JSXNXQ"))); //结束学年学期
		bean.setKSXKBH(MyTools.StrFiltr(request.getParameter("KSXKBH"))); //考试学科编号
		bean.setKSBH(MyTools.StrFiltr(request.getParameter("KSBH"))); //考试编号
		
		bean.setAUTHCODE(MyTools.StrFiltr(request.getParameter("sAuth"))); //用户权限
	}

}
