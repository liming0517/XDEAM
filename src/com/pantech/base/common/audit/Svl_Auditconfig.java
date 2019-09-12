package com.pantech.base.common.audit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.PublicTools;
import com.pantech.base.common.tools.TraceLog;

/**
 * Servlet implementation class Svl_Auditconfig
 */
public class Svl_Auditconfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Svl_Auditconfig() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//TraceLog.Trace("in the get");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		TraceLog.Trace("in the Servlet!");
		request.setCharacterEncoding("UTF-8"); //设置字符集
		response.setContentType("text/html; charset=UTF-8");
		String msg = new String();  //返回消息
		String active = MyTools.StrFiltr(request.getParameter("active")); //操作类型
		if("".equals(active)){
			active = MyTools.StrFiltr(request.getParameter("active2")); 
		}
		//String active = MyTools.StrFiltr(request.getParameter("coactive")); //操作类型
		Vector jsonV = null; //返回结果集
		JSONArray jal = null; //返回的json对象
		TraceLog.Trace("active...:"+active);
		//初始化BEAN
		AuditconfigBean  bean = new AuditconfigBean(request);  //对象
		boolean ServletFlag=false;//默认提交到后台SVL,若为AJAX模式则设置为FALSE
		this.getFormDate(request, bean);
		//判断操作类型
		if("queaudit".equalsIgnoreCase(active)) {
			//操作类型为查询
			String page = MyTools.StrFiltr(request.getParameter("page"));
			String rows = MyTools.StrFiltr(request.getParameter("rows"));
			TraceLog.Trace("==Page==" + page + "====Rows====" + rows);
			try {
				jsonV = bean.queauditRec(MyTools.parseInt(page),MyTools.parseInt(rows));
				if (jsonV != null && jsonV.size() > 0) {
					//最终处理：传回AJAX 结果集
					jal = (JSONArray)jsonV.get(2);
					System.out.println("jsonV.get(0)=   "+MyTools.StrFiltr(jsonV.get(0)));
					System.out.println("jsonV.get(1)=   "+MyTools.StrFiltr(jsonV.get(1)));
					System.out.println("jsonV.get(2)=   "+MyTools.StrFiltr(jsonV.get(2)));
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
				
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "查询错误!<br>"+ bean.getMSG() );
				response.getWriter().write(jal.toString());
			}			
		}
		if("saveaudit".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				bean.saveAudit();
				jal = JsonUtil.addJsonParams(jal,"MODEID",bean.getMODEID());
				jal = JsonUtil.addJsonParams(jal,"WAYID",bean.getWAY());
				jal = JsonUtil.addJsonParams(jal,"REPEATID",bean.getREPEAT());
				
				jal = JsonUtil.addJsonParams(jal,"REMINDID",bean.getREMIND());
				jal = JsonUtil.addJsonParams(jal,"EDTION",bean.getEDITIONS());
				jal = JsonUtil.addJsonParams(jal,"STARTDATEID",bean.getSTARTDATE());
				jal = JsonUtil.addJsonParams(jal,"LEAP",bean.getLEAP());
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
		if("deleteaudit".equals(active)){
			TraceLog.Trace("come on in delete");
			try {
				//实例化PublicTools
				bean.setDeleteauditid(MyTools.StrFiltr(request.getParameter("deleteauditid")));//需要的删除审核id
				//如果dataList中的值不为空则进行取值操作
				bean.deleteAudit();
				jal = JsonUtil.addJsonParams(jal,"MODEID",bean.getMODEID());
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","数据删除失败");
				response.getWriter().write(jal.toString());
			}	
		}
		
		if("quemode".equals(active)){
			//操作类型为查询
			String page = MyTools.StrFiltr(request.getParameter("page"));
			String rows = MyTools.StrFiltr(request.getParameter("rows"));
			TraceLog.Trace("==Page==" + page + "====Rows====" + rows);
			try {
				jsonV = bean.quemodeRec(MyTools.parseInt(page),MyTools.parseInt(rows));
				if (jsonV != null && jsonV.size() > 0) {
					//最终处理：传回AJAX 结果集
					jal = (JSONArray)jsonV.get(2);
					System.out.println("jsonV.get(0)=   "+MyTools.StrFiltr(jsonV.get(0)));
					System.out.println("jsonV.get(1)=   "+MyTools.StrFiltr(jsonV.get(1)));
					System.out.println("jsonV.get(2)=   "+MyTools.StrFiltr(jsonV.get(2)));
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
				
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "查询错误!<br>"+ bean.getMSG() );
				response.getWriter().write(jal.toString());
			}	
		}
		
		if("deletemodeid".equals(active)){
			TraceLog.Trace("come on in delete");
			try {
				//实例化PublicTools
				//bean.setDeleteauditid(MyTools.StrFiltr(request.getParameter("deleteauditid")));//需要的删除审核id
				//如果dataList中的值不为空则进行取值操作
				bean.deleteMode();
				jal = JsonUtil.addJsonParams(jal,"MODEID",bean.getMODEID());
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","数据删除失败");
				response.getWriter().write(jal.toString());
			}	
		}
		
		if("fieldlist".equals(active)){
			DBSource db = new DBSource(request);
			String sql="";
			Vector vec = new Vector();
			
			try {
				sql="select 相关视图 from dbo.V_模块初始化定义 where 模块编号='"+bean.getMODEID()+"'";
				vec=db.GetContextVector(sql);
				if(vec != null && vec.size() > 0) {
					sql="SELECT Name FROM SysColumns WHERE id=Object_Id('"+MyTools.fixSql(vec.get(0).toString())+"')";
					jal=JsonUtil.addJsonParams(jal,"fieldlist",db.getConttexJONSArr(sql).toString());
					TraceLog.Trace("retrunJsonForLoadData: "+db.getConttexJONSArr(sql).toString());
					TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
					response.getWriter().write(db.getConttexJONSArr(sql).toString());
				}
				
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("qafieldlist".equals(active)){
			DBSource db = new DBSource(request);
			String sql="";
			Vector vec = new Vector();
			
			try {
				//sql="select 相关视图 from dbo.V_模块初始化定义 where 模块编号='"+bean.getMODEID()+"'";
				//vec=db.GetContextVector(sql);
				//if(vec != null && vec.size() > 0) {
					sql="SELECT Name FROM SysColumns WHERE id=Object_Id('V_文档编辑')";
					jal=JsonUtil.addJsonParams(jal,"fieldlist",db.getConttexJONSArr(sql).toString());
					TraceLog.Trace("retrunJsonForLoadData: "+db.getConttexJONSArr(sql).toString());
					TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
					response.getWriter().write(db.getConttexJONSArr(sql).toString());
				//}
				
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		
		if("queauditsp".equalsIgnoreCase(active)) {
			//操作类型为查询
			String page = MyTools.StrFiltr(request.getParameter("page"));
			String rows = MyTools.StrFiltr(request.getParameter("rows"));
			TraceLog.Trace("==Page==" + page + "====Rows====" + rows);
			try {
				jsonV = bean.queauditspRec(MyTools.parseInt(page),MyTools.parseInt(rows));
				if (jsonV != null && jsonV.size() > 0) {
					//最终处理：传回AJAX 结果集
					jal = (JSONArray)jsonV.get(2);
					System.out.println("jsonV.get(0)=   "+MyTools.StrFiltr(jsonV.get(0)));
					System.out.println("jsonV.get(1)=   "+MyTools.StrFiltr(jsonV.get(1)));
					System.out.println("jsonV.get(2)=   "+MyTools.StrFiltr(jsonV.get(2)));
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
				
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "查询错误!<br>"+ bean.getMSG() );
				response.getWriter().write(jal.toString());
			}			
		}
		/*
		if("savespaudit".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				PublicTools publicTools =new PublicTools();
				//如果dataList中的值不为空则进行取值操作
				bean.savespAudit();
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
		*/
		if("deletespaudit".equals(active)){
			TraceLog.Trace("come on in delete");
			try {
				//实例化PublicTools
				bean.setDeletespauditid(MyTools.StrFiltr(request.getParameter("deletespauditid")));//需要的删除审核id
				//如果dataList中的值不为空则进行取值操作
				bean.deletespAudit();
				//jal = JsonUtil.addJsonParams(jal,"MODEID",bean.getMODEID());
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","数据删除失败");
				response.getWriter().write(jal.toString());
			}	
		}
		
		if("queaudittg".equalsIgnoreCase(active)) {
			//操作类型为查询
			String page = MyTools.StrFiltr(request.getParameter("page"));
			String rows = MyTools.StrFiltr(request.getParameter("rows"));
			TraceLog.Trace("==Page==" + page + "====Rows====" + rows);
			try {
				jsonV = bean.queaudittgRec(MyTools.parseInt(page),MyTools.parseInt(rows));
				if (jsonV != null && jsonV.size() > 0) {
					//最终处理：传回AJAX 结果集
					jal = (JSONArray)jsonV.get(2);
					System.out.println("jsonV.get(0)=   "+MyTools.StrFiltr(jsonV.get(0)));
					System.out.println("jsonV.get(1)=   "+MyTools.StrFiltr(jsonV.get(1)));
					System.out.println("jsonV.get(2)=   "+MyTools.StrFiltr(jsonV.get(2)));
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
				
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "查询错误!<br>"+ bean.getMSG() );
				response.getWriter().write(jal.toString());
			}			
		}
		/*
		if("savetgaudit".equalsIgnoreCase(active)) {
			//操作类型为查询
			TraceLog.Trace("come on in save");
			try {
				//实例化PublicTools
				PublicTools publicTools =new PublicTools();
				//如果dataList中的值不为空则进行取值操作
				if(!"".equals(MyTools.StrFiltr(request.getParameter("tgauditdataList")))){
					TraceLog.Trace("list not null");
					JSONArray jsArray = publicTools.getJsonArrayFromRes(request, "tgaudit");//读取datagrid
					bean.setPassauditlist(true);//设置是否读取列表值防止空指针异常
					bean.setTGID(publicTools.getStrArrFromJsonObj(jsArray, "通过编号"));//通过编号
					bean.setTGNAMEID(publicTools.getStrArrFromJsonObj(jsArray, "通过人员编号"));//通过人员编号
					bean.setTGNAME(publicTools.getStrArrFromJsonObj(jsArray, "通过人员名称"));//通过人员名称
					publicTools.printArray(bean.getTGID());	
				}
				bean.savetgAudit();
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","保存中出错,无法保存数据");
				response.getWriter().write(jal.toString());
			}		
		}
		*/
		if("deletetgaudit".equals(active)){
			TraceLog.Trace("come on in delete");
			try {
				//实例化PublicTools
				bean.setDeletetgauditid(MyTools.StrFiltr(request.getParameter("deletetgauditid")));//需要的删除审核id
				//如果dataList中的值不为空则进行取值操作
				bean.deletetgAudit();
				//jal = JsonUtil.addJsonParams(jal,"MODEID",bean.getMODEID());
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","数据删除失败");
				response.getWriter().write(jal.toString());
			}	
		}
		
		if("quedef".equals(active)){
			String page = MyTools.StrFiltr(request.getParameter("page"));
			String rows = MyTools.StrFiltr(request.getParameter("rows"));
			TraceLog.Trace("==Page==" + page + "====Rows====" + rows);
			try {
				jsonV = bean.quedefRec(MyTools.parseInt(page),MyTools.parseInt(rows));
				if (jsonV != null && jsonV.size() > 0) {
					//最终处理：传回AJAX 结果集
					jal = (JSONArray)jsonV.get(2);
					System.out.println("jsonV.get(0)=   "+MyTools.StrFiltr(jsonV.get(0)));
					System.out.println("jsonV.get(1)=   "+MyTools.StrFiltr(jsonV.get(1)));
					System.out.println("jsonV.get(2)=   "+MyTools.StrFiltr(jsonV.get(2)));
					response.getWriter().write("{\"total\":" + MyTools.StrFiltr(jsonV.get(0)) + ",\"rows\":" + jal.toString() + "}");
				}
				
			} catch (Exception e) {
		        //异常处理
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal, "MSG", "查询错误!<br>"+ bean.getMSG() );
				response.getWriter().write(jal.toString());
			}	
		}
		
		if("viewfield".equals(active)){
			DBSource db = new DBSource(request);
			String sql="";
			Vector vec = new Vector();
			
			try {
					sql="SELECT Name FROM SysColumns " +
						"WHERE id=Object_Id('"+MyTools.fixSql(request.getParameter("viewname"))+"')";
					//jal=JsonUtil.addJsonParams(jal,"fieldlist",db.getConttexJONSArr(sql).toString());
					//TraceLog.Trace("retrunJsonForLoadData: "+db.getConttexJONSArr(sql).toString());
					//TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
					response.getWriter().write(db.getConttexJONSArr(sql).toString());
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		

		
		if("deletedefmod".equals(active)){
			TraceLog.Trace("come on in delete");
			try {
				//实例化PublicTools
				bean.setDeletetgauditid(MyTools.StrFiltr(request.getParameter("deletemoduleid")));//需要的删除审核id
				//如果dataList中的值不为空则进行取值操作
				bean.deletedefmod();
				//jal = JsonUtil.addJsonParams(jal,"MODEID",bean.getMODEID());
				jal = JsonUtil.addJsonParams(jal,"MSG",bean.getMSG());
				TraceLog.Trace("retrunJsonForLoadData: "+jal.toString());
				response.getWriter().write(jal.toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				jal = JsonUtil.addJsonParams(jal,"MSG","数据删除失败");
				response.getWriter().write(jal.toString());
			}	
		}
		
	}
	

	

	public void getFormDate(HttpServletRequest request,AuditconfigBean  bean) {
		bean.setMODEID(MyTools.StrFiltr(request.getParameter("modeid")).replaceAll("_", ".")); //模块编号
		bean.setWAY(MyTools.StrFiltr(request.getParameter("way"))); //审核方式
		if("".equals(bean.getWAY())){
			bean.setWAY(MyTools.StrFiltr(request.getParameter("wayid"))); //审核方式
		}
		bean.setREPEAT(MyTools.StrFiltr(request.getParameter("repeat"))); //重复提交
		bean.setModename(MyTools.StrFiltr(request.getParameter("modename")));//模块名称
		bean.setAUDITIDS(MyTools.StrFiltr(request.getParameter("auditids"))); //审核编号
		bean.setEDITIONS(MyTools.StrFiltr(request.getParameter("edition")));
		bean.setREMIND(MyTools.StrFiltr(request.getParameter("remind")));
		bean.setSTARTDATE(MyTools.StrFiltr(request.getParameter("startdate")));
		bean.setLEAP(MyTools.StrFiltr(request.getParameter("leap")));
		bean.setREJECT(MyTools.StrFiltr(request.getParameter("reject")));
	}
}
