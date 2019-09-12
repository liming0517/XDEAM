package com.pantech.devolop.customExamManage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class CjtjBean {

	private String AUTHCODE;// 用户权限

	private String CJ_KSBH; //考试编号
	private String CJ_KSXKBH; //考试学科编号
	private String CJ_KCDM; //课程代码
	private String CJ_BJDM; //班级代码
	private String CJ_CJR; //创建人
	private String CJ_CJSJ; //创建时间
	private String CJ_ZT; //状态
	
	private String CJ_XNXQBM;// 学年学期编码
	
	private String CJ_KSMC; //考试名称
	
	private String USERCODE;
	private String listsql;// 查询sql语句

	// 以下变量为此类的固定变量
	protected HttpServletRequest request;
	private DBSource db;
	private String MSG;

	/**
	 * 初始化变量
	 * 
	 * @date:2016-7-13
	 * @author:翟旭超
	 */
	private void initialData() {
		// TODO Auto-generated method stub
		AUTHCODE = "";// 用户权限
		
		CJ_KSBH = ""; //考试编号
		CJ_KSXKBH = ""; //考试学科编号
		CJ_KCDM = ""; //课程代码
		CJ_BJDM = ""; //班级代码
		CJ_CJR = ""; //创建人
		CJ_CJSJ = ""; //创建时间
		CJ_ZT = ""; //状态
		CJ_XNXQBM = "";// 学年学期编码
		CJ_KSMC = ""; //考试名称
		
		USERCODE = "";// 用户编号
	}


	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public CjtjBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}


	
	/**
	 * 分页查询 考试信息列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String XNXQ_CX, String KSMC_CX, String KSLB_CX, String XK_CX ) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		Vector bjVec=null;//班级
		Vector xkVec=null;//学科
		Vector ksxkbhVec=null;
		String bjString="";
		String xkString="";
		String ksxkbhString ="";
		String sql2="";
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		/*sql="select a.考试编号,a.考试名称,a.学年学期编码,c.学年学期名称,a.类别编号,b.类别名称,a.创建人,convert(nvarchar(10),a.创建时间,21) as 创建时间,a.状态 from V_自设考试管理_考试信息表 as a " +
				" left join V_信息类别_类别操作 as b on a.类别编号=b.描述" +
				" left join V_规则管理_学年学期表 as c on a.学年学期编码=c.学年学期编码" +
				" where b.父类别代码='KSLBDM' and a.状态='1' ";*/
		
		/*sql="select distinct b.课程代码,f.课程名称,a.学年学期编码,d.学年学期名称,a.考试编号,a.考试名称,a.类别编号,e.类别名称,a.创建时间,a.状态  " +
				"from V_自设考试管理_考试信息表 as a " +
				"left join V_自设考试管理_考试学科信息表 as b on a.考试编号=b.考试编号 " +
				"left join V_课程数据子类 as c on b.课程代码=c.课程号 " +
				"left join V_规则管理_学年学期表 as d on a.学年学期编码=d.学年学期编码 " +
				"left join V_信息类别_类别操作 as e on a.类别编号=e.描述 " +
				"left join V_课程数据子类 as f on b.课程代码=f.课程号 " +
				"where a.状态='1' and b.状态='1' and d.状态='1' and e.父类别代码='KSLBDM' ";*/
			
		

		/*sql="select distinct b.课程代码,f.课程名称,a.学年学期编码,d.学年学期名称,a.考试编号,a.考试名称,a.类别编号,e.类别名称,a.创建时间,a.状态  " +
				"from V_自设考试管理_考试信息表 as a " +
				"left join V_自设考试管理_考试学科信息表 as b on a.考试编号=b.考试编号 " +
				"left join V_课程数据子类 as c on b.课程代码=c.课程号  " +
				"left join V_规则管理_学年学期表 as d on a.学年学期编码=d.学年学期编码  " +
				"left join V_信息类别_类别操作 as e on a.类别编号=e.描述 " +
				"left join V_课程数据子类 as f on b.课程代码=f.课程号 " +
				"left join V_基础信息_教研组信息表 as g on b.课程代码=g.学科代码 "+
				"where a.状态='1' and b.状态='1' and d.状态='1' and e.父类别代码='KSLBDM' and a.类别编号 !='01' ";//and a.类别编号 !='01'
*/		
		sql="select distinct b.课程代码,h.课程名称,a.学年学期编码,i.学年学期名称,a.考试编号,a.考试名称,a.类别编号,j.类别名称,a.创建时间,a.状态  " +
				"from V_自设考试管理_考试信息表 as a " +
				"left join V_自设考试管理_考试学科信息表 as b on a.考试编号=b.考试编号 " +
				"left join V_学校班级数据子类 c on c.行政班代码=b.班级代码  " +
				"left join V_基础信息_系部信息表 d on d.系部代码=c.系部代码  " +
				"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 ) e on e.行政班代码=b.班级代码 and e.课程代码 like '%'+b.课程代码+'%' " +
				"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
				"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 ) f on f.行政班代码=b.班级代码 and f.课程代码=b.课程代码 " +
				"left join V_基础信息_教研组信息表 g on g.学科代码=b.课程代码  " +
				"left join V_课程数据子类 h on h.课程号=b.课程代码 " +
				"left join V_规则管理_学年学期表 as i on a.学年学期编码=i.学年学期编码  " +
				"left join V_信息类别_类别操作 as j on a.类别编号=j.描述 " +
				"where 1=1 and j.父类别代码='KSLBDM' and b.课程代码 is not null ";//and a.类别编号 !='01'
		
		// 判断查询条件
		if (!"".equalsIgnoreCase(XNXQ_CX)) {
			sql += " and a.学年学期编码= '" + MyTools.fixSql(XNXQ_CX) + "'";
		}
		if (!"".equalsIgnoreCase(KSMC_CX)) {
			sql += " and a.考试名称  like '%" + MyTools.fixSql(KSMC_CX) + "%'";
		}
		if (!"".equalsIgnoreCase(KSLB_CX)) {
			sql += " and a.类别编号 = '" + MyTools.fixSql(KSLB_CX) + "'";
		}
		if (!"".equalsIgnoreCase(XK_CX)) {
			sql += " and b.课程代码 = '" + MyTools.fixSql(XK_CX) + "'";
		}
		
		//权限判断
		/*if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
			sql+=" and b.课程代码 in (select distinct a.课程代码 from V_规则管理_授课计划明细表 as a where a.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			//系部教务人员
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				//if(this.getAUTHCODE().indexOf(admin) > -1){
					sql += " or ";
				//}
				sql += " e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}*/
		
		//2017919翟旭超添加权限判断代码
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0&&this.getAUTHCODE().indexOf(xbjwgl)<0){
			sql+=" and (e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or f.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
		
			if(this.getAUTHCODE().indexOf(jyzzz)>-1){
				sql+=" or g.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
			}
			//系部
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += " or c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			sql+=" ) ";
		}
		
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
			
			/*sql2 = "select distinct a.班级代码  " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码  " +
					"left join V_规则管理_授课计划明细表 as c on c.授课计划主表编号=b.授课计划主表编号  " +
					"left join V_学校班级数据子类 as d on d.行政班代码=a.班级代码  " +
					"left join V_基础信息_系部教师信息表 as e on e.系部代码=d.系部代码  " +
					"where a.状态='1' and d.状态='1' ";*/
			
			sql2 = "select distinct a.班级代码  " +
					"from V_自设考试管理_考试学科信息表 as a " +
					
					"left join V_课程数据子类 b  on a.课程代码 = b.课程号 " +
					"left join V_学校班级数据子类 c  on a.班级代码 = c.行政班代码 " +
					"left join V_排课管理_课程表明细详情表 d  on c.行政班代码 = d.行政班代码 and b.课程号 = d.课程代码 " +
					"left join V_基础信息_教研组信息表 as e on d.课程代码=e.学科代码 " +
					"left join V_基础信息_系部教师信息表 as f on f.系部代码=c.系部代码 " +
					"left join (" +
					"select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 " +
					"from V_规则管理_授课计划主表 as t1 " +
					"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as g on g.行政班代码=a.班级代码 and g.课程代码=a.课程代码  " +
					
					"where 1=1 ";
			
			//权限判断
			if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
				sql2+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or g.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				
				if(this.getAUTHCODE().indexOf(jyzzz)>-1){
					sql2+=" or e.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
				}
				
				//班主任
				if(this.getAUTHCODE().indexOf(bzr) > -1){
					sql2 += " or c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
				}
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					sql2 += " or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql2+=" ) ";
			}
			
			/*if(this.getAUTHCODE().indexOf(jyzzz)<0){
				sql2 += " and b.状态='1' and c.状态='1' and c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				//系部教务人员
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					sql2 += " or ";
					sql2 += " e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
			}*/
			
			
			/*//系部教务人员
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql2+="or e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') ";
			}*/
			bjVec = db.GetContextVector(sql2);
			if(bjVec.size()>0){
				for(int i=0;i<bjVec.size();i++){
					bjString += bjVec.get(i) + ",";
				}
				bjString.substring(0, bjString.length()-1);
				String bjStr[] = bjString.split(",");
				
				bjString="";
				for(int i=0;i<bjStr.length;i++){
					bjString += "'" + bjStr[i] + "',";
				}
				if(!bjString.equals("")){
					bjString=bjString.substring(0, bjString.length()-1);
				}
			}else{
				bjString="''";
			}
			
			/*sql2 = " select distinct a.课程代码  " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码 " +
					"left join V_规则管理_授课计划明细表 as c on c.授课计划主表编号=b.授课计划主表编号 and c.课程代码=a.课程代码 " +
					"left join V_学校班级数据子类 as d on b.行政班代码=d.行政班代码 " +
					"left join V_基础信息_系部教师信息表 as e on e.系部代码=d.系部代码 " +
					"left join V_基础信息_教研组信息表 as f on a.课程代码=f.学科代码 "+
					"where a.状态='1' and b.状态='1' and c.状态='1' and d.状态='1' and  c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";*/
			
			sql2 = " select distinct a.课程代码  " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
					"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
					"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表) d on d.行政班代码=a.班级代码 and d.课程代码 like '%'+a.课程代码+'%' " +
					"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
					"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  ) e on e.行政班代码=a.班级代码 and e.课程代码=a.课程代码 " +
					"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  " +
					"left join V_课程数据子类 g on g.课程号=a.课程代码 " +
					"where 1=1 ";
			
			//2017920翟旭超添加权限判断代码
			if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0&&this.getAUTHCODE().indexOf(xbjwgl)<0){
				sql2+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			
				if(this.getAUTHCODE().indexOf(jyzzz)>-1){
					sql2+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
				}
				//系部
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					sql2 += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				//班主任
				if(this.getAUTHCODE().indexOf(bzr) > -1){
					sql2 += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
				}
				sql2+=" ) ";
			}
			
			
			/*//系部教务人员
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql2+=" or e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') ";
			}
			
			if(this.getAUTHCODE().indexOf(jyzzz)>0){
				sql2+=" or f.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
			}*/
				
			
			
			xkVec=db.GetContextVector(sql2);
			if(xkVec.size()>0){
				for(int i=0;i<xkVec.size();i++){
					xkString += xkVec.get(i) + ",";
				}
				
				xkString.substring(0, xkString.length()-1);
				String xkStr[] = xkString.split(",");
				
				xkString="";
				for(int i=0;i<xkStr.length;i++){
					xkString += "'" + xkStr[i] + "',";
				}
				if(!xkString.equals("")){
					xkString=xkString.substring(0, xkString.length()-1);
				}
			}else{
				xkString="''";
			}
			
			/*sql2="select distinct b.考试学科编号 " +
					"from V_自设考试管理_考试信息表 as a " +
					"left join V_自设考试管理_考试学科信息表 as b on a.考试编号=b.考试编号 " +
					"left join V_学校班级数据子类 as c on b.班级代码=c.行政班代码 " +
					"left join V_规则管理_授课计划主表 as d on d.行政班代码=b.班级代码 " +
					"left join V_基础信息_系部教师信息表 as f on f.系部代码=c.系部代码 " +
					"left join V_规则管理_授课计划明细表 as e on e.授课计划主表编号=d.授课计划主表编号 and e.授课教师编号=f.教师编号 " +*/
				
			sql2="select distinct b.考试学科编号 " +
					"from V_自设考试管理_考试信息表 as a " +
					"left join V_自设考试管理_考试学科信息表 as b on a.考试编号=b.考试编号 " +
					"left join V_学校班级数据子类 as c on b.班级代码=c.行政班代码 " +
					"left join V_基础信息_系部信息表 d on d.系部代码=c.系部代码  " +
					"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表) e on e.行政班代码=b.班级代码 and e.课程代码 like '%'+b.课程代码+'%' " +
					"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
					"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  ) f on f.行政班代码=b.班级代码 and f.课程代码=b.课程代码 " +
					"left join V_基础信息_教研组信息表 g on g.学科代码=b.课程代码  " +
					"left join V_课程数据子类 h on h.课程号=b.课程代码 " +
					
					"where 1=1";
			
			sql2 += "and b.班级代码 in ("+bjString+") and b.课程代码 in ("+xkString+")";
			ksxkbhVec=db.GetContextVector(sql2);
			if(ksxkbhVec.size()>0){
				for(int i=0;i<ksxkbhVec.size();i++){
					ksxkbhString += ksxkbhVec.get(i) + ",";
				}
				
				ksxkbhString.substring(0, ksxkbhString.length()-1);
				String ksxkbhStr[] = ksxkbhString.split(",");
				
				ksxkbhString="";
				for(int i=0;i<ksxkbhStr.length;i++){
					ksxkbhString += "'" + ksxkbhStr[i] + "',";
				}
				if(!ksxkbhString.equals("")){
					ksxkbhString=ksxkbhString.substring(0, ksxkbhString.length()-1);
				}
			}else{
				ksxkbhString="''";
			}
			
			sql+= " and b.考试学科编号 in ("+ksxkbhString+")";
			
			/*sql+=" and b.考试学科编号 in ( " +
					"select distinct b.考试学科编号 " +
					"from V_自设考试管理_考试信息表 as a " +
					"left join V_自设考试管理_考试学科信息表 as b on a.考试编号=b.考试编号 " +
					"left join V_学校班级数据子类 as c on b.班级代码=c.行政班代码 " +
					"left join V_规则管理_授课计划主表 as d on d.行政班代码=b.班级代码 " +
					"left join V_基础信息_系部教师信息表 as f on f.系部代码=c.系部代码 " +
					"left join V_规则管理_授课计划明细表 as e on e.授课计划主表编号=d.授课计划主表编号 and e.授课教师编号=f.教师编号 " +
					"where a.状态='1' and b.状态='1' and c.状态='1' and d.状态='1' and e.状态='1' and e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			
			//系部教务人员
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql+=" or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') ";
			}
			sql += "and b.班级代码 in ";
			
			sql += "(select distinct a.班级代码  " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码  " +
					"left join V_规则管理_授课计划明细表 as c on c.授课计划主表编号=b.授课计划主表编号  " +
					"left join V_学校班级数据子类 as d on d.行政班代码=a.班级代码  " +
					"left join V_基础信息_系部教师信息表 as e on e.系部代码=d.系部代码  " +
					"where a.状态='1' and b.状态='1' and c.状态='1' and d.状态='1'  and c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			//系部教务人员
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql+="or e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') ";
			}
			sql+=") ";
			sql += "and b.课程代码 in ";
			
			sql += " (select distinct a.课程代码  " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码 " +
					"left join V_规则管理_授课计划明细表 as c on c.授课计划主表编号=b.授课计划主表编号 and c.课程代码=a.课程代码 " +
					"left join V_学校班级数据子类 as d on b.行政班代码=d.行政班代码 " +
					"left join V_基础信息_系部教师信息表 as e on e.系部代码=d.系部代码 " +
					"where a.状态='1' and b.状态='1' and c.状态='1' and d.状态='1' and  c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			//系部教务人员
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql+=" or e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') ";
			}
			sql += ")) ";*/
			//注释还原 end
		}
		
		
		sql += " group by b.课程代码,h.课程名称,a.考试编号,a.学年学期编码,i.学年学期名称,a.考试名称,a.类别编号,j.类别名称,a.创建时间,a.状态  ";
		
		sql = sql + " ORDER BY a.学年学期编码 desc,a.创建时间 desc ";

		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017-07-27
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnXqCombo() throws SQLException{
		Vector vec = null;
		/*String sql="select '请选择' as comboName,'' as comboValue,0  as orderNum " +
				" union all" +
				" select distinct 学年 as comboName,学年 as comboValue,1" +
				" from V_规则管理_学年学期表" +
				" where 状态='1' order by orderNum,comboValue desc";*/
		
		String sql="select '请选择' as comboName,'' as comboValue,0  as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,1 " +
				"from V_规则管理_学年学期表 " +
				"where 状态='1' order by orderNum,comboValue desc ";
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	
	/**
	 * 读取当前学年学期
	 * @date:2017-07-27
	 * @author:zhaixuchao
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		String curXnxq = "";
		Vector vec = null;
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		
		return curXnxq;
	}
	
	
	/**
	 * 读取考试类别下拉框
	 * @date:2017-07-27
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadLbCombo() throws SQLException{
		Vector vec = null;
		
		String sql="select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 类别名称 , 描述  from  V_信息类别_类别操作 where 父类别代码='KSLBDM' and 描述 !='01'" +
				"order by comboValue";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	
	/**
	 * 课程下拉框赋值
	 * @date:2017-07-27
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKCMCCombo() throws SQLException {
		Vector vec = null;
		String sql="";
			
		sql="select '' as comboValue,'请选择' as comboName,0  as orderNum " +
				"union all " +
				"select 课程号 as comboValue , 课程名称 as comboName , 1 from V_课程数据子类 where LEFT(课程号,1)=0 " +
				"order by orderNum,comboValue ";
			
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 分页查询 成绩统计详情 列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Vector sourceStatisticsRec(int pageNum, int page, String CJ_KSBH, String CJ_KCDM) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		
		sql="select t.班级代码,t.行政班名称,t.人数,t.优秀,t.合格,t.不合格,convert(decimal(18,2),t.平均分) as 平均分,case when t.人数<>'0' then cast(cast(round(1.0*t.优秀/t.人数,2)*100 as float) as varchar)+'%' else '0%' end as 优秀率,case when t.人数<>'0' then cast(cast(round(1.0*(t.优秀+t.合格)/t.人数,2)*100 as float) as varchar)+'%' else '0%' end as 合格率 from (select b.班级代码,d.行政班名称, COUNT(a.成绩) as 人数,count(case when a.成绩 between 80 and 100 then 1 end) as 优秀,count(case when a.成绩 between 60 and 79 then 1 end) as 合格,count(case when a.成绩 between 0 and 59 then 1 end) as 不合格,isnull(AVG(a.成绩),0) as 平均分 " +
				"from  V_自设考试管理_考试学科信息表 as b " +
				"left join V_自设考试管理_学生成绩信息表 as a on a.考试学科编号=b.考试学科编号 " +
				"left join V_自设考试管理_考试信息表 as c on b.考试编号=c.考试编号 " +
				"left join V_学校班级数据子类 as d on b.班级代码=d.行政班代码 "+
				//"where c.考试编号='" + MyTools.fixSql(CJ_KSBH) + "' and b.课程代码='" + MyTools.fixSql(CJ_KCDM) + "' ";
				"where 1=1 ";
		
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
			/*sql += " and b.班级代码 in (" +
					"select distinct a.班级代码 " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码 " +
					"left join V_规则管理_授课计划明细表 as c on c.授课计划主表编号=b.授课计划主表编号 " +
					"left join V_学校班级数据子类 as d on d.行政班代码=a.班级代码 " +
					"left join V_基础信息_系部教师信息表 as e on e.系部代码=d.系部代码 " +
					"left join V_基础信息_教研组信息表 as f on a.课程代码=f.学科代码 "+
					"where a.状态='1' and d.状态='1' and a.考试编号='" + MyTools.fixSql(CJ_KSBH) + "' ";*/
			
			
			sql += " and b.班级代码 in (" +
					"select distinct a.班级代码 " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_学校班级数据子类 as b on b.行政班代码=a.班级代码 " +
					"left join V_课程数据子类 c  on a.课程代码 = c.课程号 "+
					"left join V_排课管理_课程表明细详情表 d  on b.行政班代码 = d.行政班代码 and c.课程号 = d.课程代码  " +
					"left join V_基础信息_教研组信息表 as e on a.课程代码=e.学科代码  " +
					"left join V_基础信息_系部教师信息表 as f on f.系部代码=b.系部代码  " +
					"left join (select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 from V_规则管理_授课计划主表 as t1 "+
					"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as h on h.行政班代码=a.班级代码 and h.课程代码=a.课程代码 "+
					"where 1=1 and a.课程代码='" + MyTools.fixSql(CJ_KCDM) + "' and a.考试编号='" + MyTools.fixSql(CJ_KSBH) + "' ";
			/*if(this.getAUTHCODE().indexOf(jyzzz)<0){
				sql += " and b.状态='1' and c.状态='1' and c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				//系部教务人员
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					sql += " or ";
					sql += " e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
			}*/
			
			sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or h.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";

			if(this.getAUTHCODE().indexOf(jyzzz)>-1){
				sql+=" or e.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
			}
			
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql += " or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
			
			sql += ")";
		}
			sql+=" and c.考试编号='" + MyTools.fixSql(CJ_KSBH) + "' and b.课程代码='" + MyTools.fixSql(CJ_KCDM) + "' ";
		
		sql+=" group by b.班级代码,d.行政班名称) as t";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	
	
	/**
	 * ExportExcelStuScore 学生考试成绩信息统计导出
	 * @author zhaixuchao
	 * @date:2017-07-28
	 * @param examName 考试名称
	 * @param className 班级名称
	 * @return savePath 下载路径
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */
	public String ExportExcelStuScore(String kcdm,String kcmc,String xnxqbm,String ksxkbh,String ksbh,String ksmc) throws SQLException, UnsupportedEncodingException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		String savePath = "";
		//String cellContent = ""; //当前单元格的内容
		//String[] title = {"卡号","姓名 ","班级"};	
		ArrayList title = new ArrayList();
		title.add("班级");
		title.add("考试人数");
		title.add("80-100(人数)");
		title.add("60-79(人数)");
		title.add("0-59(人数)");
		title.add("平均分");
		title.add("优秀率");
		title.add("合格率");
		//读取学科信息
		sql="select t.行政班名称,t.人数,t.优秀,t.合格,t.不合格,convert(decimal(18,2),t.平均分) as 平均分,case when t.人数<>'0' then cast(cast(round(1.0*t.优秀/t.人数,2)*100 as float) as varchar)+'%' else '0%' end as 优秀率,case when t.人数<>'0' then cast(cast(round(1.0*(t.优秀+t.合格)/t.人数,2)*100 as float) as varchar)+'%' else '0%' end as 合格率 from (select b.班级代码,d.行政班名称, COUNT(a.成绩) as 人数,count(case when a.成绩 between 80 and 100 then 1 end) as 优秀,count(case when a.成绩 between 60 and 79 then 1 end) as 合格,count(case when a.成绩 between 0 and 59 then 1 end) as 不合格,isnull(AVG(a.成绩),0) as 平均分 " +
				"from  V_自设考试管理_考试学科信息表 as b " +
				"left join V_自设考试管理_学生成绩信息表 as a on a.考试学科编号=b.考试学科编号 " +
				"left join V_自设考试管理_考试信息表 as c on b.考试编号=c.考试编号 " +
				"left join V_学校班级数据子类 as d on b.班级代码=d.行政班代码 "+
				//"where c.考试编号='" + MyTools.fixSql(ksbh) + "' and b.课程代码='" + MyTools.fixSql(kcdm) + "' and b.状态='1' and a.状态='1' and c.状态='1' and d.状态='1' ";
				"where 1=1 ";
		
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
			/*sql += " and b.班级代码 in (" +
					"select distinct a.班级代码 " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码 " +
					"left join V_规则管理_授课计划明细表 as c on c.授课计划主表编号=b.授课计划主表编号 " +
					"left join V_学校班级数据子类 as d on d.行政班代码=a.班级代码 " +
					"left join V_基础信息_系部教师信息表 as e on e.系部代码=d.系部代码 " +
					"where a.状态='1'  and d.状态='1' and a.考试编号='" + MyTools.fixSql(ksbh) + "' ";*/
			
			sql += " and b.班级代码 in (" +
					"select distinct a.班级代码 " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_学校班级数据子类 as b on b.行政班代码=a.班级代码 " +
					"left join V_课程数据子类 c  on a.课程代码 = c.课程号 "+
					"left join V_排课管理_课程表明细详情表 d  on b.行政班代码 = d.行政班代码 and c.课程号 = d.课程代码  " +
					"left join V_基础信息_教研组信息表 as e on a.课程代码=e.学科代码  " +
					"left join V_基础信息_系部教师信息表 as f on f.系部代码=b.系部代码  " +
					"left join (select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 from V_规则管理_授课计划主表 as t1 "+
					"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as h on h.行政班代码=a.班级代码 and h.课程代码=a.课程代码 "+
					"where 1=1 and a.课程代码='" + MyTools.fixSql(kcdm) + "' and a.考试编号='" + MyTools.fixSql(ksbh) + "' ";
			
			sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or h.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";

			if(this.getAUTHCODE().indexOf(jyzzz)>-1){
				sql+=" or e.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
			}
			
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql += " or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
			
			/*if(this.getAUTHCODE().indexOf(jyzzz)<0){
				sql += " and b.状态='1' and c.状态='1' and c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				//系部教务人员
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					sql += " or ";
					sql += " e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
			}*/
			sql += ")";
		}
		sql+=" and c.考试编号='" + MyTools.fixSql(ksbh) + "' and b.课程代码='" + MyTools.fixSql(kcdm) + "' ";
		
		sql+=" group by b.班级代码, d.行政班名称) as t";
		vec = db.GetContextVector(sql);
		//整理数据
		if(vec!=null && vec.size()>0){
			String stuCode = "";
		
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			
			savePath = MyTools.getProp(request, "Base.exportExcelPath");
			
			//创建
			try {
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				// 输出的excel的路径   
				//filePath += "d:\\年级学生成绩汇总信息_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls"; 
				//输出流
				savePath += "/"+ksmc+" "+kcmc+" "+"成绩统计_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);//工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				
				//设置列宽
				for(int i=0; i<title.size(); i++){
					wsheet.setColumnView(i, 15);
				}
				
				//=====
				//设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 15, WritableFont.BOLD);
				contentStyle=new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				//加的标题
				wsheet.mergeCells(0, 0, 7, 0);
				content = new Label(0, 0, ksmc+" "+kcmc+"  "+"成绩统计信息", contentStyle);
				wsheet.addCell(content);
				//===
				
				
				//设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				
				for(int i=0;i<title.size();i++){   
				     // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z   
				     // 在Label对象的子对象中指明单元格的位置和内容   
					content = new Label(i,1,title.get(i).toString(),contentStyle); 
				     // 将定义好的单元格添加到工作表中   
					wsheet.addCell(content);   
				 }  
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				//填充数据
				//k:用于循环时Excel的行号
				//外层for用于循环行,内曾for用于循环列'
				for(int i=0,k=2; i<vec.size();i+=title.size(), k++){
					for(int j=0;j<title.size();j++){ 
						content = new Label(j, k, vec.get(i+j) + "",contentStyle);
						wsheet.addCell(content);
					}
				 } 	 
				//写入数据   
				wbook.write();   
				//关闭文件   
				wbook.close();
				os.close();
				this.setMSG("文件生成成功");
			} catch (FileNotFoundException e) {
				this.setMSG("导出前请先关闭相关EXCEL");
			} catch (WriteException e) {
				this.setMSG("文件生成失败");
			} catch (IOException e) {
				this.setMSG("文件生成失败");
			} 
			
			this.setMSG("文件生成成功");
		}else{
			this.setMSG("没有符合条件的成绩信息");
		}
			return savePath;
	}
	
	
	public String getAUTHCODE() {
		return AUTHCODE;
	}


	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
	}


	public String getCJ_KSBH() {
		return CJ_KSBH;
	}


	public void setCJ_KSBH(String cJ_KSBH) {
		CJ_KSBH = cJ_KSBH;
	}


	public String getCJ_KSXKBH() {
		return CJ_KSXKBH;
	}


	public void setCJ_KSXKBH(String cJ_KSXKBH) {
		CJ_KSXKBH = cJ_KSXKBH;
	}


	public String getCJ_KCDM() {
		return CJ_KCDM;
	}


	public void setCJ_KCDM(String cJ_KCDM) {
		CJ_KCDM = cJ_KCDM;
	}


	public String getCJ_BJDM() {
		return CJ_BJDM;
	}


	public void setCJ_BJDM(String cJ_BJDM) {
		CJ_BJDM = cJ_BJDM;
	}


	public String getCJ_CJR() {
		return CJ_CJR;
	}


	public void setCJ_CJR(String cJ_CJR) {
		CJ_CJR = cJ_CJR;
	}


	public String getCJ_CJSJ() {
		return CJ_CJSJ;
	}


	public void setCJ_CJSJ(String cJ_CJSJ) {
		CJ_CJSJ = cJ_CJSJ;
	}


	public String getCJ_ZT() {
		return CJ_ZT;
	}


	public void setCJ_ZT(String cJ_ZT) {
		CJ_ZT = cJ_ZT;
	}


	public String getCJ_XNXQBM() {
		return CJ_XNXQBM;
	}


	public void setCJ_XNXQBM(String cJ_XNXQBM) {
		CJ_XNXQBM = cJ_XNXQBM;
	}


	public String getUSERCODE() {
		return USERCODE;
	}


	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}


	public String getListsql() {
		return listsql;
	}


	public void setListsql(String listsql) {
		this.listsql = listsql;
	}


	public String getMSG() {
		return MSG;
	}


	public void setMSG(String mSG) {
		MSG = mSG;
	}


	public String getCJ_KSMC() {
		return CJ_KSMC;
	}


	public void setCJ_KSMC(String cJ_KSMC) {
		CJ_KSMC = cJ_KSMC;
	}

	
	

}