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

import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class Svl_Skjh extends HttpServlet {
	/**
	 * Constructor of the object.
	 */
	public Svl_Skjh() {
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
		System.out.println("active--------------"+active);
		int pageNum = MyTools.parseInt(request.getParameter("page"));	//获得页面page参数 分页
		int pageSize = MyTools.parseInt(request.getParameter("rows"));	//获得页面rows参数 分页
		
		Vector jsonV = null;//返回结果集
		JSONArray jal = null;//返回json对象
		SkjhBean bean = new SkjhBean(request);
		this.getFormData(request, bean); //获取SUBMIT提交时的参数（AJAX适用）
		
		//初始化页面数据
		if("initData".equalsIgnoreCase(active)){
			
			try {
				//查询列表
				//jsonV = bean.queryTree(pageNum, pageSize,"");
				//jal = (JSONArray)jsonV.get(2);
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
							
				//查询学年学期下拉框
				jsonV = bean.loadXNXQCombo();
				jal = JsonUtil.addJsonParams(jal, "xnxqData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询教学性质下拉框
				jsonV = bean.loadJXXZCombo();
				jal = JsonUtil.addJsonParams(jal, "jxxzData", ((JSONArray)jsonV.get(2)).toString());	
				
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
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
		
		//查询列表
		if("query".equalsIgnoreCase(active)){
			String termid=MyTools.StrFiltr(request.getParameter("termid"));
			
			try {
				//查询列表
				//jsonV = bean.queryTree(pageNum, pageSize, "");
				//jal = (JSONArray)jsonV.get(2);
				//jal = JsonUtil.addJsonParams(jal, "listData", "{\"total\":" + MyTools.StrFiltr(jsonV.get(0))+ ",\"rows\":" + jal.toString() + "}");
				//查询周次数量
				bean.getWeeknum(termid);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
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
				
				//查询学科名称下拉框
				jsonV = bean.loadXKMCCombo();
				jal = JsonUtil.addJsonParams(jal, "xkmcData", ((JSONArray)jsonV.get(2)).toString());
				
				//查询任课教师下拉框
				jsonV = bean.loadKSXSCombo();
				jal = JsonUtil.addJsonParams(jal, "ksxsData", ((JSONArray)jsonV.get(2)).toString());
				
				response.getWriter().write(jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("loadSFKSCombo".equalsIgnoreCase(active)){  //是否考试
			try {
				jsonV = bean.loadSFKSCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("loadKCLXCombo".equalsIgnoreCase(active)){  //课程类型
			try {
				jsonV = bean.loadKCLXCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("loadKCMCCombo".equalsIgnoreCase(active)){  //课程名称
			try {
				jsonV = bean.loadKCMCCombo();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("checkpaike".equalsIgnoreCase(active)){
			try {
				bean.checkpaike();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("deleteskjh".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			try {
				bean.deleteskjh(iKeyCode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("deleteAllskjh".equalsIgnoreCase(active)){
			try {
				bean.deleteAllskjh();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("save".equalsIgnoreCase(active)){
			try {
				bean.SaveRec();
				jal = JsonUtil.addJsonParams(jal, "msg", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("editCD".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			try {
				bean.editCD(iKeyCode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if("check".equalsIgnoreCase(active)){
			String teacherbh = MyTools.StrFiltr(request.getParameter("teacherbh"));
			String teacherxm = MyTools.StrFiltr(request.getParameter("teacherxm"));
			String saveType = MyTools.StrFiltr(request.getParameter("saveType"));
			try {
				bean.check(teacherbh,teacherxm,saveType);
				jal = JsonUtil.beanToJsonarray(bean);
				response.getWriter().write(jal.toString());
				TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("checkChangeSKJH".equalsIgnoreCase(active)){
			try {
				bean.checkChangeSKJH();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("checkDelSKJH".equalsIgnoreCase(active)){
			String iKeyCode = MyTools.StrFiltr(request.getParameter("iKeyCode"));
			try {
				bean.checkDelSKJH(iKeyCode);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("checkDelallSKJH".equalsIgnoreCase(active)){			
			try {
				bean.checkDelallSKJH();
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("openTeacher".equalsIgnoreCase(active)){
			String teaId = MyTools.StrFiltr(request.getParameter("teaId"));
			String teaName = MyTools.StrFiltr(request.getParameter("teaName"));
			String teaLevel = MyTools.StrFiltr(request.getParameter("teaLevel"));
			String teacharr = MyTools.StrFiltr(request.getParameter("teacharr"));
			try {
				try {
					jsonV=bean.openTeacher(pageNum,pageSize,teaId,teaName,teaLevel,teacharr);
				} catch (WrongSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				
				TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("openRoom".equalsIgnoreCase(active)){
			String selschool=MyTools.StrFiltr(request.getParameter("selschool"));
			String selhouse=MyTools.StrFiltr(request.getParameter("selhouse"));
			String seltype=MyTools.StrFiltr(request.getParameter("seltype"));
			String roomarr = MyTools.StrFiltr(request.getParameter("roomarr"));
			String classId = MyTools.StrFiltr(request.getParameter("classId"));
			try {
				try {
					jsonV=bean.openRoom(selschool,selhouse,seltype,roomarr,classId);
				} catch (WrongSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//选修课查询教室
		if("openRoomXX".equalsIgnoreCase(active)){
			//String selschool=MyTools.StrFiltr(request.getParameter("selschool"));
			//String selhouse=MyTools.StrFiltr(request.getParameter("selhouse"));
			String seltype=MyTools.StrFiltr(request.getParameter("seltype"));
			String xbdm=MyTools.StrFiltr(request.getParameter("XBDM"));
			String roomarr = MyTools.StrFiltr(request.getParameter("roomarr"));
			try {
				try {
					jsonV=bean.openRoomXX(seltype,xbdm,roomarr);
				} catch (WrongSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
						e.printStackTrace();
			}
		}
		
		if("openComRoom".equalsIgnoreCase(active)){
			String seltype=MyTools.StrFiltr(request.getParameter("seltype"));
			String roomarr = MyTools.StrFiltr(request.getParameter("roomarr"));
			try {
				try {
					jsonV=bean.openComRoom(seltype,roomarr);
				} catch (WrongSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				//TraceLog.Trace("response:   "+jal.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if("schoolCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.schoolCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("houseCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.houseCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("classtypeCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.classtypeCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		
		if("papertypeCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.papertypeCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("schoolCombobox2".equalsIgnoreCase(active)){  //编辑文件
			String iUSERCODE = MyTools.StrFiltr(request.getParameter("iUSERCODE"));
			String sAuth = MyTools.StrFiltr(request.getParameter("sAuth"));
			try {
				jsonV = bean.schoolCombobox2(iUSERCODE,sAuth);
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("houseCombobox2".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.houseCombobox2();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("floorCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.floorCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("weekCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.weekCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("loadTable".equalsIgnoreCase(active)){  //编辑文件
			String school=MyTools.StrFiltr(request.getParameter("school"));
			//String house=MyTools.StrFiltr(request.getParameter("house"));
			//String floor=MyTools.StrFiltr(request.getParameter("floor"));
			String week=MyTools.StrFiltr(request.getParameter("week"));
			String xnxq=MyTools.StrFiltr(request.getParameter("xnxq"));
			String jxxz=MyTools.StrFiltr(request.getParameter("jxxz"));
			try {
				String revec = bean.loadTable(school,week,xnxq,jxxz);
				
				jal = JsonUtil.addJsonParams(jal, "VEC", revec);
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				jal = JsonUtil.addJsonParams(jal, "MSG2", bean.getMSG2());
				jal = JsonUtil.addJsonParams(jal, "BJMC", bean.getXZBDM());
				response.getWriter().write(jal.toString());
				//TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		//导出计算机房课表
		if("loadComputerRoom".equalsIgnoreCase(active)){  
			try {				
				//查询列表
				String revec = bean.loadComputerRoom();
						
				response.getWriter().write(revec);
				TraceLog.Trace(revec);

			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("createSJDJ".equalsIgnoreCase(active)){  //编辑文件
			try {
				bean.createSJDJ();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("checkGGK".equalsIgnoreCase(active)){  //编辑文件
			try {
				bean.checkGGK();	
				jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMSG());
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		
		if("xnxqIMCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.xnxqIMCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("zymcIMCombobox".equalsIgnoreCase(active)){  //编辑文件
			try {
				jsonV = bean.zymcIMCombobox();
				jal = (JSONArray)jsonV.get(2);
				response.getWriter().write(jal.toString());
				TraceLog.Trace(jal.toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
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
	private void getFormData(HttpServletRequest request,SkjhBean bean){
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setAUTHCODE(MyTools.StrFiltr(request.getParameter("AUTH")));//权限代码
		
		bean.setGS_SKJHMXBH(MyTools.StrFiltr(request.getParameter("GS_SKJHMXBH"))); //授课计划明细编号
		bean.setGS_SKJHZBBH(MyTools.StrFiltr(request.getParameter("GS_SKJHZBBH"))); //授课计划主表编号
		bean.setGS_KCDM(MyTools.StrFiltr(request.getParameter("GS_KCDM"))); //课程代码
		bean.setGS_KCMC(MyTools.StrFiltr(request.getParameter("GS_KCMC"))); //课程名称
		bean.setGS_KCMCDM(MyTools.StrFiltr(request.getParameter("GS_KCMCDM"))); //课程代码
		bean.setGS_KCLX(MyTools.StrFiltr(request.getParameter("GS_KCLX"))); //课程类型
		bean.setGS_KSXS(MyTools.StrFiltr(request.getParameter("GS_KSXS"))); //考试形式
		bean.setGS_SKJSBH(MyTools.StrFiltr(request.getParameter("GS_SKJSBH"))); //授课教师编号
		bean.setGS_SKJSXM(MyTools.StrFiltr(request.getParameter("GS_SKJSXM"))); //授课教师姓名
		bean.setGS_JS(MyTools.StrFiltr(request.getParameter("GS_JS"))); //节数
		bean.setGS_GPYPJS(MyTools.StrFiltr(request.getParameter("GS_GPYPJS"))); //固排已排节数
		bean.setGS_SJYPJS(MyTools.StrFiltr(request.getParameter("GS_SJYPJS"))); //实际已排节数
		bean.setGS_LJ(MyTools.StrFiltr(request.getParameter("GS_LJ"))); //连节
		bean.setGS_LC(MyTools.StrFiltr(request.getParameter("GS_LC"))); //连次
		bean.setGS_GPLCCS(MyTools.StrFiltr(request.getParameter("GS_GPLCCS"))); //固排连次次数
		bean.setGS_SJLCCS(MyTools.StrFiltr(request.getParameter("GS_SJLCCS"))); //实际连次次数
		bean.setGS_CDYQ(MyTools.StrFiltr(request.getParameter("GS_CDYQ"))); //场地要求
		bean.setGS_CDMC(MyTools.StrFiltr(request.getParameter("GS_CDMC"))); //场地名称
		bean.setGS_SKZC(MyTools.StrFiltr(request.getParameter("GS_SKZC"))); //授课周次
		bean.setGS_SKZCXQ(MyTools.StrFiltr(request.getParameter("GS_SKZCXQ"))); //授课周次详情
		bean.setGS_ZT(MyTools.StrFiltr(request.getParameter("GS_ZT"))); //状态
		bean.setGS_XF(MyTools.StrFiltr(request.getParameter("GS_XF"))); //学分
		bean.setGS_ZKS(MyTools.StrFiltr(request.getParameter("GS_ZKS"))); //总课时
		
		bean.setGS_XNXQBM(MyTools.StrFiltr(request.getParameter("GS_XNXQBM"))); //学年学期编码
		bean.setGS_XZBDM(MyTools.StrFiltr(request.getParameter("GS_XZBDM"))); //行政班代码
		bean.setGS_ZYDM(MyTools.StrFiltr(request.getParameter("GS_ZYDM"))); //专业代码
		bean.setGS_ZYMC(MyTools.StrFiltr(request.getParameter("GS_ZYMC"))); //专业名称
		bean.setSKJSBH(MyTools.StrFiltr(request.getParameter("SKJSBH"))); //授课教师编号
		bean.setSKJSBHXM(MyTools.StrFiltr(request.getParameter("SKJSBHXM"))); //授课教师编号
		bean.setSKSL(MyTools.StrFiltr(request.getParameter("SKSL"))); //授课数量
		bean.setXKDM(MyTools.StrFiltr(request.getParameter("XKDM"))); //学科代码
		bean.setXZBDM(MyTools.StrFiltr(request.getParameter("XZBDM"))); //行政班代码
		bean.setXNXQ(MyTools.StrFiltr(request.getParameter("XNXQ"))); //学年学期
		bean.setJXXZ(MyTools.StrFiltr(request.getParameter("JXXZ"))); //教学性质
		bean.setGS_XNXQ2(MyTools.StrFiltr(request.getParameter("GS_XNXQ2"))); //编辑授课计划中的学年学期
		bean.setGS_SFKS(MyTools.StrFiltr(request.getParameter("GS_SFKS"))); //是否考试
		bean.setiUSERCODE(MyTools.StrFiltr(request.getParameter("iUSERCODE")));//用户编号
		bean.setUseType(MyTools.StrFiltr(request.getParameter("useType")));//保存类型
	}
}