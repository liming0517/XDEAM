package com.pantech.devolop.teachManage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

import net.sf.json.JSONArray;

/**
 * createTime:2017-02-09
 * createUser:马晓良
 * description:教学标准信息; 获取数据库中数据供前台使用
 */
public class Svl_TeachManage extends HttpServlet{
	public Svl_TeachManage(){
		super();
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response)
			throws IOException,ServletException{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			String active = MyTools.StrFiltr(request.getParameter("active"));// 拿取前台的active值
			Vector vector = null;
			JSONArray jal = null;
			TeachManageBean bean = new TeachManageBean(request);
			getFormdata(request, bean);
			
			//dategrid加载和查询方法
			if("queryList".equalsIgnoreCase(active)){// 判断传过来的参数是否为queryList确定执行哪步操作
				int pageNum = MyTools.parseInt(request.getParameter("page"));//获取分页页数
				int pageSize = MyTools.parseInt(request.getParameter("rows"));//获取分页行数pageSize
				try{
						vector=bean.queryListRec(pageNum,pageSize);
					if(vector != null && vector.size()>0){
						jal = (JSONArray)vector.get(2);
						//System.out.println("$$$$$$$$$"+vector.get(0)+","+jal.toString());
						response.getWriter().write("{\"total\":" + MyTools.StrFiltr(vector.get(0)) + ",\"rows\":" + jal.toString() + "}");
					}
				}catch(SQLException e){
					e.printStackTrace();
				} catch (WrongSQLException e) {
					e.printStackTrace();
				}
			}
			
			//保存方法,包括新建和修改
			if("save".equalsIgnoreCase(active)){// 判断传过来的参数是否为save确定执行哪步操作
				System.out.println("SVL的save1操作");
				try {
					String path = MyTools.StrFiltr(request.getParameter("path"));
					String filename = MyTools.StrFiltr(request.getParameter("fileUpLoadName"));
					bean.save(path,filename);//调用添加或更新方法
					jal = JsonUtil.addJsonParams(jal, "MSG", bean.getMsg()); // 判断传过来的参数是否为save确定执行哪步操作
					response.getWriter().write(jal.toString());
					}catch(SQLException e){
						e.printStackTrace();
					} catch (WrongSQLException e) {
						e.printStackTrace();
					}
			}
			//删除方法
			if("deleteRow".equalsIgnoreCase(active)){// 判断传过来的参数是否为delete确定执行哪步操作
				//System.out.println("================="+request.getParameter("iKeyCode"));
						 try {
								//调用删除操作
								bean.deleteRec();
								//返回操作信息
								jal=JsonUtil.addJsonParams(jal, "msg", bean.getMsg());
								response.getWriter().write(jal.toString());
							} catch (SQLException e) {
								e.printStackTrace();
								jal = JsonUtil.addJsonParams(jal, "msg", "无法获取数据<br>"+bean.getMsg());
								response.getWriter().write(jal.toString());
							} catch (WrongSQLException e) {
								e.printStackTrace();
							}		 
					}
			
			//参数赋值
			if("view".equalsIgnoreCase(active)){
				try{
					bean.loadData();
					jal = JsonUtil.beanToJsonarray(bean);
					System.out.println(jal.toString());
					response.getWriter().write(jal.toString());
				} catch(Exception e){
					e.printStackTrace();
				}		
			}
			
			//前台发送请求到servlet,servlet从后台拿到下拉框所有数据返回到前台
			if("JXBZCombobox".equalsIgnoreCase(active)){
				try{
					//System.out.println(".............................Ajax已经进入后台 ");
					jal=bean.InitJXBZCombobox();
					//System.out.println(jal.toString());
					response.getWriter().write(jal.toString());
				} catch (WrongSQLException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}
			
			//将word等文件转换成swf格式
			if("fileToSwf".equalsIgnoreCase(active)){ 
				String path = MyTools.StrFiltr(request.getParameter("filePath"));  //文件路径
				String filePath =path;
				Office2SWF o2s = new Office2SWF(request, filePath);
				boolean flag = o2s.conver();
				if(flag){
					jal = JsonUtil.addJsonParams(jal,"MSG", "转换成功");
				}else{
					jal = JsonUtil.addJsonParams(jal,"MSG", "转换失败");
					if("openoffice服务未启动".equalsIgnoreCase(o2s.getMSG()) || "openofficeBat文件不存在".equalsIgnoreCase(o2s.getMSG())){
						jal = JsonUtil.addJsonParams(jal, "serverMsg", o2s.getMSG());
					}
				}
				response.getWriter().write(jal.toString());
			}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
	private void getFormdata(HttpServletRequest request, TeachManageBean bean) {
		bean.setUSERCODE(MyTools.getSessionUserCode(request)); //用户编号
		bean.setCXJXBZ_JXBZMC(MyTools.StrFiltr(request.getParameter("icCXJXBZ_JXBZMC")));//教学标准名称(查询)
		bean.setCXJXBZ_ZYBM(MyTools.StrFiltr(request.getParameter("icCXJXBZ_ZYMC")));//专业名称(查询)
		bean.setJXBZ_JXBZBM(MyTools.StrFiltr(request.getParameter("icJXBZ_JXBZBM")));//教学标准编码
		bean.setJXBZ_JXBZMC(MyTools.StrFiltr(request.getParameter("icJXBZ_JXBZMC")));//教学标准名称
		bean.setJXBZ_ZYBM(MyTools.StrFiltr(request.getParameter("icJXBZ_ZYBM")));//专业编码
		bean.setJXBZ_ZYMC(MyTools.StrFiltr(request.getParameter("icJXBZ_ZYMC")));//专业名称
	}
}
