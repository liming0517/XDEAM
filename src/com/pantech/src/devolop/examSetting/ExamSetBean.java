package com.pantech.src.devolop.examSetting;

/*
@date 2015.06.02
@author wangzh
模块：M1.2授课计划
说明: ISS
重要及特殊方法：
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.UnderlineStyle;
import jxl.SheetSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class ExamSetBean {
	private String GG_BH;//编号
	private String GG_LX;//类型
	private String GG_XNXQBM;//学年学期编码
	private String GG_XZBDM;//行政班代码
	private String GG_SJXL;//时间序列
	private String GG_SKJHMXBH;//授课计划明细编号
	private String GG_LJXGBH;//连节相关编号
	private String GG_ZT;//状态
	private String SKJHMXBH;//授课计划明细编号
	private String XZBDM;//行政班代码
	private String XNXQ;//学年学期
	private String JXXZ;//教学性质
	private String QZQM;//期中期末(考试截止周数)
	private String KCJS;//课程教师
	private String QCXX;//清除的信息
	private String iUSERCODE;//用户编号
	private String Auth;//用户权限
	
	private String ex_xnxq="";//学年学期
	private String ex_jxxz="";//教学性质
	private String ex_ksmc="";//考试名称
	private String ex_kslx="";//考试类型
	private String ex_jzzs="";//上课截止周数
	private String ex_ksrq="";//考试日期
	private String ex_kszbbh="";//考试主表编号
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	private String MSG2;  //提示信息
	private String MSG3;  //提示信息
	private String LCS;  //已排连次次数
	private String AOD;  //判断添加或删除
	private String TATOL;  //提示信息
	
	private String KCAPZBBH;//考场安排主表编号
	private String KCAPMXBH;//考场安排明细表编号
	private String KSRQBH;//考试日期编号
	
	/**
	 * 构造函数
	 * @param request
	 */
	public ExamSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-06-02
	 * @author:wangzh
	 */
	public void initialData() {
		GG_BH = "";//编号
		GG_LX = "";//类型
		GG_XNXQBM = "";//学年学期编码
		GG_XZBDM = "";//行政班代码
		GG_SJXL = "";//时间序列
		GG_SKJHMXBH = "";//授课计划明细编号
        GG_LJXGBH = "";//连节相关编号
        GG_ZT = "";//状态
        SKJHMXBH = "";//授课计划明细编号
		XZBDM = "";//行政班代码
		XNXQ = "";//学年学期
		JXXZ = "";//教学性质
		KCJS = "";//课程教师
		QCXX = "";//清除的信息
		MSG = ""; //提示信息
	}
	
	/**
	 * 新建学期考试信息
	 * @date:2017-03-31
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void createExam() throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = new Vector();
		String kszbbh="";
		String sql="";
		String sql2="";
		String sql3="";
		String sql4="";
		String maxZBId="";
		String maxMxId="";
		String kssjd= MyTools.getProp(request, "Base.examTime");//考试时间段
		String[] examTime=kssjd.split(",");
		
		sql="select count(*) from dbo.V_考试管理_考场安排主表  " +
			 "where [学年学期编码]='"+MyTools.fixSql(this.getEx_xnxq()+this.getEx_jxxz())+"' and [考试名称]='"+MyTools.fixSql(this.getEx_ksmc())+"' ";
		vec=db.GetContextVector(sql);
		if(vec.get(0).toString().equals("0")){//不存在
			sql2="select max(cast(SUBSTRING([考场安排主表编号],8,9) as bigint)) from V_考试管理_考场安排主表    ";
			vec2 = db.GetContextVector(sql2);
			//System.out.println("vec2---|"+vec2+"|"+vec2.size());
			if (!vec2.toString().equals("[]") && vec2.size() > 0) {
				maxZBId=String.format("%08d", (Long.parseLong(vec2.get(0).toString())+1));  
				this.setKCAPZBBH("KSZBBH_"+maxZBId);//设置授课计划明细主键
			}else{
				this.setKCAPZBBH("KSZBBH_00000001");//设置授课计划明细主键
			}
			sql3="insert into dbo.V_考试管理_考场安排主表([考场安排主表编号],[学年学期编码],[考试名称],[考试类型]) values (" +
					"'"+MyTools.fixSql(this.getKCAPZBBH())+"'," +//考场安排主表编号
					"'"+MyTools.fixSql(this.getEx_xnxq()+this.getEx_jxxz())+"'," +//学年学期编码
					"'"+MyTools.fixSql(this.getEx_ksmc())+"'," +//考试名称
					"'' " +//考试类型
					") " ;
			vec3.add(sql3);
			
			//添加课程信息
			sql2="select distinct c.行政班代码,c.班级名称,c.学年学期编码," +
				"stuff((select '｜'+d.授课计划明细编号 from (select b.行政班代码,c.班级名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式,c.总人数  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_基础信息_班级信息表 c on b.行政班代码=c.班级编号 where b.学年学期编码='"+MyTools.fixSql(this.getEx_xnxq()+this.getEx_jxxz())+"' ) d where c.行政班代码=d.行政班代码 and c.班级名称=d.班级名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码  and c.课程名称=d.课程名称 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称  for xml path('')),1,1,'') as 授课计划明细编号," +
				"c.是否考试,c.课程代码,c.课程名称,c.专业代码,c.专业名称,c.场地要求," +
				"stuff((select '｜'+d.上课周期 from (select b.行政班代码,c.班级名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式,c.总人数  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_基础信息_班级信息表 c on b.行政班代码=c.班级编号 where b.学年学期编码='"+MyTools.fixSql(this.getEx_xnxq()+this.getEx_jxxz())+"' ) d where c.行政班代码=d.行政班代码 and c.班级名称=d.班级名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码  and c.课程名称=d.课程名称 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称  for xml path('')),1,1,'') as 上课周期," +
				"c.期中期末," +
				"stuff((select '｜'+d.考试形式 from (select b.行政班代码,c.班级名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式,c.总人数  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_基础信息_班级信息表 c on b.行政班代码=c.班级编号 where b.学年学期编码='"+MyTools.fixSql(this.getEx_xnxq()+this.getEx_jxxz())+"' ) d where c.行政班代码=d.行政班代码 and c.班级名称=d.班级名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码  and c.课程名称=d.课程名称 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称 for xml path('')),1,1,'') as 考试形式," +
			    "c.总人数 " +
				"from (select b.行政班代码,c.班级名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式,c.总人数  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_基础信息_班级信息表 c on b.行政班代码=c.班级编号 where b.学年学期编码='"+MyTools.fixSql(this.getEx_xnxq()+this.getEx_jxxz())+"' ) c " +
				"order by c.行政班代码 ";
			vec2=db.GetContextVector(sql2);
								 
			if(vec2!=null&&vec2.size()>0){
				for(int j=0;j<vec2.size();j=j+14){
					String skjh="";
					String kscc="";
					String ksxs="";
					
					//获取考场安排明细编号
					String sqlmxid="select max(cast(SUBSTRING([考场安排明细编号],8,11) as bigint)) from dbo.V_考试管理_考场安排明细表";
					Vector vecmxid = db.GetContextVector(sqlmxid);
					if (!vecmxid.toString().equals("[]") && vecmxid.size() > 0) {
						maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vecmxid.get(0))))+1+j/14);
						this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
					}else{
						maxMxId= String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr("10000000000")))+1+j/14);
						this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
					}
					
					//取第一个授课计划编号
//					if(vec2.get(j+3).toString().indexOf("｜")>-1){
//						skjh=vec2.get(j+3).toString().substring(0,vec2.get(j+3).toString().indexOf("｜"));
//					}else{
//						skjh=vec2.get(j+3).toString();
//					}
//					if(vec2.get(j+12).toString().indexOf("｜")>-1){
//						ksxs=vec2.get(j+12).toString().substring(0,vec2.get(j+12).toString().indexOf("｜"));
//					}else{
//						ksxs=vec2.get(j+12).toString();
//					}
//					kscc="KSCCBH"+skjh.substring(skjh.indexOf("_"),skjh.length());
															 	
					sql3="insert into dbo.V_考试管理_考场安排明细表  (考场安排明细编号,考场安排主表编号,课程代码,课程名称,场地类型,专业代码,专业名称,行政班代码,行政班名称,考试形式,监考教师编号,监考教师姓名,学生人数) values ( " +
							"'"+MyTools.fixSql(this.getKCAPMXBH())+"'," +
							"'"+MyTools.fixSql(this.getKCAPZBBH())+"'," +
							"'"+MyTools.fixSql(vec2.get(j+5).toString())+"'," +
							"'"+MyTools.fixSql(vec2.get(j+6).toString())+"'," +
							"'0'," +
							"'"+MyTools.fixSql(vec2.get(j+7).toString())+"'," +
							"'"+MyTools.fixSql(vec2.get(j+8).toString())+"'," +//专业名称		
							"'"+MyTools.fixSql(vec2.get(j).toString())+"'," +
							"'"+MyTools.fixSql(vec2.get(j+1).toString())+"'," +
							"'"+MyTools.fixSql(vec2.get(j+12).toString())+"'," +//考试形式
							"''," +//监考教师编号
							"''," +//监考教师姓名
							"'"+MyTools.fixSql(vec2.get(j+13).toString())+"' ) " ;//学生人数			
					vec3.add(sql3);					 
				}				 
			}			 
		}
	
		if(db.executeInsertOrUpdateTransaction(vec3)){
			this.setMSG("新建成功");
		}else{
			this.setMSG("新建失败");
		}
		
	}
	
	/**
	 * 编辑学期考试信息
	 * @date:2017-04-12
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void editCreExam(String ksapbh) throws SQLException{
		Vector vec = new Vector();
		String kszbbh="";
		String sql="";
		String sql3="";
		String kssjd= MyTools.getProp(request, "Base.examTime");//考试时间段
		String[] examTime=kssjd.split(",");
		
		sql="update dbo.V_考试管理_考场安排主表 set [学年学期编码]='"+MyTools.fixSql(this.getEx_xnxq()+this.getEx_jxxz())+"',[考试名称]='"+MyTools.fixSql(this.getEx_ksmc())+"',[考试类型]='"+MyTools.fixSql(this.getEx_kslx())+"',[上课截止周数]='"+MyTools.fixSql(this.getEx_jzzs())+"',[考试日期]='"+MyTools.fixSql(this.getEx_ksrq())+"' " +
			"where [考场安排主表编号]='"+MyTools.fixSql(ksapbh)+"' ";
		vec.add(sql);
		
		sql="delete from dbo.V_考试管理_考试规则 where 考试主表编号='"+MyTools.fixSql(ksapbh)+"' ";
		vec.add(sql);
		
		if(this.getEx_ksrq().indexOf(",")>-1){
			String[] ksdate=this.getEx_ksrq().split(",");
			for(int i=0;i<ksdate.length;i++){
				for(int j=0;j<examTime.length;j++){
					sql3="insert into dbo.V_考试管理_考试规则 ([考试主表编号],[考试日期],[考试时间段],[考试类型]) values ('"+MyTools.fixSql(ksapbh)+"','"+ksdate[i].trim()+"','"+examTime[j]+"','笔试') ";
					vec.add(sql3);
					sql3="insert into dbo.V_考试管理_考试规则 ([考试主表编号],[考试日期],[考试时间段],[考试类型]) values ('"+MyTools.fixSql(ksapbh)+"','"+ksdate[i].trim()+"','"+examTime[j]+"','上机') ";
					vec.add(sql3);
				}
			}
		}else{
			for(int j=0;j<examTime.length;j++){
				sql3="insert into dbo.V_考试管理_考试规则 ([考试主表编号],[考试日期],[考试时间段],[考试类型]) values ('"+MyTools.fixSql(ksapbh)+"','"+this.getEx_ksrq().trim()+"','"+examTime[j]+"','笔试') ";
				vec.add(sql3);
				sql3="insert into dbo.V_考试管理_考试规则 ([考试主表编号],[考试日期],[考试时间段],[考试类型]) values ('"+MyTools.fixSql(ksapbh)+"','"+this.getEx_ksrq().trim()+"','"+examTime[j]+"','上机') ";
				vec.add(sql3);
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
		
	}
	
	/**
	 * 删除学期考试信息
	 * @date:2017-04-12
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void delCreExam(String ksapbh) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String kszbbh="";
		String sql="";
		String sql2="";
		String sql3="";
		
		sql="delete from dbo.V_考试管理_考场安排明细表  where [考场安排主表编号]='"+MyTools.fixSql(ksapbh)+"' ";
		sql+="delete from dbo.V_考试管理_考场安排主表  where [考场安排主表编号]='"+MyTools.fixSql(ksapbh)+"' ";
				
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
		
	}
	
	/**
	 * 读取查询考场考试周期下拉框
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector DRXNXQCombobox() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取查询考场考试名称下拉框
	 * @date:2017-04-14
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector DRKSMCCombobox(String DRxnxq) throws SQLException{
		Vector vec = null;
		String sql = "select comboValue,comboName from ( " +
				"select '0' as comboValue,'全部' as comboName union " +
				"SELECT [考场安排主表编号] as comboValue,[考试名称] as comboName  FROM [dbo].[V_考试管理_考场安排主表] " +
				"where [学年学期编码]='"+MyTools.fixSql(DRxnxq)+"' and [考试类型]='1' ) x order by comboValue ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询监考教师
	 * @date:2017-04-18
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public Vector openTeacher(int pageNum,int pageSize,String jzzs,String teaId,String teaName)throws SQLException{
		String sql = "";
		String sql2= "";
		String sql3= "";
		String sql4= "";
		Vector vec = null;
		Vector vec2 = null;
		
		sql="select count(*) from V_考试管理_监考教师  where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考试名称]='"+MyTools.fixSql(jzzs)+"' ";
		vec=db.GetContextVector(sql);
		
		sql3="select distinct t.工号,t.姓名  from (select a.工号 ,a.姓名 ,'0' as sel from V_教职工基本数据子类 a inner join (select distinct 专业代码,授课教师编号 from V_排课管理_课程表周详情表 where 学年学期编码='"+MyTools.fixSql(this.getXNXQ())+"' and 课程代码<>'') b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' left join sysUserAuth c on c.UserCode=a.工号 left join V_权限层级关系表 d on d.权限编号=c.AuthCode left join V_层级表 e on e.层级编号=d.层级编号 "+
				 "where 1=1 and a.状态='1' and e.层级编号='C1005' union all select a.工号 as id,a.姓名 as text,state='open' from V_教职工基本数据子类 a left join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' left join V_规则管理_选修课授课计划明细表 c on c.授课计划主表编号=b.授课计划主表编号 inner join V_规则管理_学生选修课关系表 d on d.授课计划明细编号=c.授课计划明细编号 inner join V_学生基本数据子类 e on e.学号=d.学号 inner join V_基础信息_班级信息表 f on f.班级编号=e.行政班代码 inner join sysUserAuth g on g.UserCode=a.工号 inner join V_权限层级关系表 h on h.权限编号=g.AuthCode inner join V_层级表 i on i.层级编号=h.层级编号 where a.状态='1' and b.状态='1' and b.学年学期编码='"+MyTools.fixSql(this.getXNXQ())+"' and i.层级编号='C1005') as t "+
				 "where t.工号 is not null ";
		
		if(vec!=null&&vec.size()>0){
			if(vec.get(0).toString().equals("0")){//监考教师表中没有数据，显示所有系部任课教师
				sql2="select distinct stuff((select '#'+n.工号 from ( "+sql3+" ) n where m.姓名=n.姓名 for xml path('')),1,1,'') as 工号,姓名 from ( "+sql3+" ) m order by m.姓名,m.工号 ";
				
			}else{//查询监考教师表
				sql4="select distinct stuff((select '#'+n.工号 from ( "+sql3+" ) n where m.姓名=n.姓名 for xml path('')),1,1,'') as 工号,姓名 from ( "+sql3+" ) m ";
				sql2="select  m.[工号],m.[姓名],case when m.姓名=n.姓名 then '1' else '0' end as sel from (" +sql4+ ") m left join ( " +
						"SELECT  [监考教师编号] as [工号],[监考教师姓名] as [姓名] FROM [dbo].[V_考试管理_监考教师]  where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考试名称]='"+MyTools.fixSql(jzzs)+"' ) n on m.姓名=n.姓名 " +
						"order by m.[姓名] ";
			}
		}
		
		vec2 = db.getConttexJONSArr(sql2, pageNum, pageSize);
		return vec2;
	}
	
	/**
	 * 查询监考教师日期
	 * @date:2017-06-8
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public Vector openTeacherDate(int pageNum,int pageSize,String jzzs,String teaId,String teaName)throws SQLException{
		String sql = "";
		Vector vec = null;

		
		sql="SELECT  [监考教师编号] as [工号],[监考教师姓名] as [姓名],[监考日期] FROM [dbo].[V_考试管理_监考教师]  where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考试名称]='"+MyTools.fixSql(jzzs)+"' " ;

		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 查询监考教师
	 * @date:2017-04-18
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void saveTeacher(String jzzs,String teainfoidarray,String teainfoarray)throws SQLException{
		String sql = "";
		Vector vec = new Vector();
		
		String[] teaid=teainfoidarray.split(",");
		String[] teaname=teainfoarray.split(",");
		sql="delete from V_考试管理_监考教师 where 学年学期编码='"+MyTools.fixSql(this.getXNXQ())+"' and [考试名称]='"+MyTools.fixSql(jzzs)+"' ";
		vec.add(sql);
		
		if(!teainfoidarray.equals("")){
			for(int i=0;i<teaid.length;i++){
				String teanum="";
				if(teaid[i].indexOf("#")>-1){
					teanum=teaid[i].substring(0,teaid[i].indexOf("#"));
				}else{
					teanum=teaid[i];
				}
				sql="insert into V_考试管理_监考教师  ([学年学期编码],[考试名称],[监考教师编号],[监考教师姓名],[监考类型],[监考日期]) values ('"+MyTools.fixSql(this.getXNXQ())+"','"+jzzs+"','"+teanum+"','"+teaname[i]+"','4','') ";
				vec.add(sql);
			}
		}

		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 保存监考教师日期
	 * @date:2017-05-03
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void saveTeaDate(String kszbbh,String teadateidarray,String jkrq) throws SQLException {
		String sql = "";
		Vector vec = new Vector();

		String[] teaid=teadateidarray.split(",");
		for(int i=0;i<teaid.length;i++){
			sql="update [V_考试管理_监考教师] set [监考日期]='"+MyTools.fixSql(jkrq)+"' " +
				"where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考试名称]='"+MyTools.fixSql(kszbbh)+"' and [监考教师编号]='"+MyTools.fixSql(teaid[i])+"' ";
			vec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}		
	}
	
	
	/**
	 * 查询考试教室
	 * @date:2017-04-18
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public Vector openRoom(int pageNum,int pageSize,String jzzs,String roomId)throws SQLException{
		String sql = "";
		String sql2= "";
		Vector vec = null;
		Vector vec2 = null;
		
		sql="select count(*) from dbo.V_考试管理_考试教室  where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and 考试名称='"+MyTools.fixSql(jzzs)+"' and 建筑物号='"+roomId+"' ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			if(vec.get(0).toString().equals("0")){//考试教室表中没有数据，显示所有教室
				sql2="SELECT [教室编号],[教室名称],[教室类型代码],[实际容量],'0' as sel FROM [dbo].[V_教室数据类] where 是否可用='1' and 建筑物号='"+roomId+"' order by 教室编号 ";
			}else{//查询监考教师表
				sql2="select  m.[教室编号],m.[教室名称],case when m.教室编号=n.教室编号 then '1' else '0' end as sel from ( " +
						"SELECT [教室编号],[教室名称],[教室类型代码],[实际容量] FROM [dbo].[V_教室数据类] where 是否可用='1' and 建筑物号='"+roomId+"' " +
						" ) m left join ( " +
						"SELECT  [教室编号],[教室名称] FROM [dbo].[V_考试管理_考试教室]  where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and 考试名称='"+MyTools.fixSql(jzzs)+"' and 建筑物号='"+roomId+"' ) n on m.教室编号=n.教室编号 " +
						"order by m.[教室编号] ";
			}
		}
		
		vec2 = db.getConttexJONSArr(sql2, pageNum, pageSize);
		return vec2;
	}
	
	/**
	 * 查询监考教室
	 * @date:2017-04-18
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void saveClassroom(String jzzs,String jxlh,String clsinfoidarray,String clsinfoarray)throws SQLException{
		String sql = "";
		Vector vec = new Vector();
		
		String[] clsid=clsinfoidarray.split(",");
		String[] clsname=clsinfoarray.split(",");
		sql="delete from dbo.V_考试管理_考试教室 where 学年学期编码='"+MyTools.fixSql(this.getXNXQ())+"' and 考试名称='"+MyTools.fixSql(jzzs)+"' and 建筑物号='"+MyTools.fixSql(jxlh)+"' ";
		vec.add(sql);
		
		if(!clsinfoidarray.equals("")){
			for(int i=0;i<clsid.length;i++){
				sql="insert into V_考试管理_考试教室  ([学年学期编码],[考试名称],[教室编号],[教室名称],[建筑物号]) values ('"+MyTools.fixSql(this.getXNXQ())+"','"+jzzs+"','"+clsid[i]+"','"+clsname[i]+"','"+jxlh+"') ";
				vec.add(sql);
			}
		}
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 查询当前学期的教师信息树
	 * @date:2015-05-27
	 * @author:yeq
	 * @param level
	 * @param parentCode 专业代码
	 * @param ic_jsxm 教师姓名
	 * @return 教师信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queTeaTree(String level, String parentCode,String PK_KSZBBH,String ic_jsxm)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz").replaceAll("@", "");//教学主管校长@211@
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr").replaceAll("@", "");//全校教导主任@212@
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr").replaceAll("@", "");//系部教导主任@213@
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl").replaceAll("@", "");//全校教务管理@214@
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl").replaceAll("@", "");//系部教务管理@215@
		String admin = MyTools.getProp(request, "Base.admin").replaceAll("@", "");//管理员
		
		
		sql = "select [监考教师编号] as id,[监考教师姓名] as text from V_考试管理_监考教师  where [考场安排主表编号]='"+MyTools.fixSql(PK_KSZBBH)+"' ";
		if(this.getAuth().indexOf(jxzgxz) > -1 || this.getAuth().indexOf(qxjdzr) > -1 || this.getAuth().indexOf(qxjwgl) > -1 || this.getAuth().indexOf(admin) > -1){
		
		}else if(this.getAuth().indexOf(xbjdzr) > -1 || this.getAuth().indexOf(xbjwgl) > -1) {
			sql += " and [监考教师编号] in( select 教师编号 from V_基础信息_系部教师信息表 where 系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getiUSERCODE()) + "') ) ";
		}else {
			sql += " and [监考教师编号] = '" + MyTools.fixSql(this.getiUSERCODE()) + "' ";
		}
		
		if(!"".equalsIgnoreCase(ic_jsxm)) {
			sql += " and [监考教师姓名] like '%" + MyTools.fixSql(ic_jsxm) + "%' ";
		}
		
		
		sql += " and 是否任课 != '0' order by text,id ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 导入考试信息
	 * @date:2016-03-04
	 * @author:luepngfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void checkKSGL() throws SQLException {
		String sql = "";
		String sql2= "";
		String sql3= "";
		String sql4= "";
		String sql5= "";
		String sql6= "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec5 = null;
		Vector vec4 = new Vector();
		String jzzs="";
		String zbbh="";
		String sqlsz1="";
		String sqlsz2="";
		String sqlsz3="";
		Vector vecsz1=null;
		Vector vecsz2=null;
		Vector vecsz3=null;
		int drflag=0;//是否导入
		String oldzbbh="";
		
		//获取截止周数
		sql5="select [上课截止周数],[考场安排主表编号] from [V_考试管理_考场安排主表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考试类型]='1' order by [上课截止周数] ";
		vec5=db.GetContextVector(sql5);
		if(vec5!=null&&vec5.size()>0){
			for(int i=0;i<vec5.size();i=i+2){
				jzzs+=vec5.get(i).toString()+",";
				zbbh+=vec5.get(i+1).toString()+",";
			}
			if(!jzzs.equals("")){
				jzzs=jzzs.substring(0,jzzs.length()-1);
				zbbh=zbbh.substring(0,zbbh.length()-1);
			}
		}
		System.out.println("qzqm:--"+this.getQZQM());
		
		//获取同学期其它考试主表编号
		if(this.getQZQM().equals("0")){//导入全部
			 
		}else{
			String sqlzb="select [考场安排主表编号] from [V_考试管理_考场安排主表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考场安排主表编号]!='"+MyTools.fixSql(this.getQZQM())+"' and [考试类型]='1' ";
			Vector veczb=db.GetContextVector(sqlzb);
			
			if(veczb!=null&&veczb.size()>0) {
				for(int i=0;i<veczb.size();i++) {
					oldzbbh+="'"+veczb.get(i).toString()+"',";
				}
				if(!oldzbbh.equals("")) {
					oldzbbh=oldzbbh.substring(0, oldzbbh.length()-1);
				}
				sqlsz1=" SELECT [授课计划明细编号] " + 
					" FROM [dbo].[V_考试管理_考试设置] where [期中期末] in ("+oldzbbh+") ";
				vecsz1=db.GetContextVector(sqlsz1);
				
				sqlsz2=" SELECT [授课计划明细编号] " + 
						" FROM [dbo].[V_考试管理_考试设置] where [期中期末] in ('"+this.getQZQM()+"') ";
				vecsz2=db.GetContextVector(sqlsz2);
				
				sqlsz3=" SELECT [行政班代码],[行政班名称],[学年学期编码],[授课计划明细编号],[是否考试],[课程代码],[课程名称] " + 
						" FROM [dbo].[V_考试管理_考试设置] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' ";
				vecsz3=db.GetContextVector(sqlsz3);
				 
//				sqlsz1=" SELECT [行政班代码],[行政班名称],[学年学期编码],[授课计划明细编号],[是否考试],[课程代码],[课程名称] " + 
//						" FROM [dbo].[V_考试管理_考试设置] where [期中期末] in ("+oldzbbh+") ";
//				vecsz1=db.GetContextVector(sqlsz1);
//					
//				sqlsz2=" SELECT [行政班代码],[行政班名称],[学年学期编码],[授课计划明细编号],[是否考试],[课程代码],[课程名称] " + 
//							" FROM [dbo].[V_考试管理_考试设置] where [期中期末] in ('"+this.getQZQM()+"') ";
//				vecsz2=db.GetContextVector(sqlsz2);
			}
		}
		
		//导入
		sql="select 学年学期编码,convert(varchar(10),排课截止时间,21) from V_规则管理_学年学期表 where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			for(int i=0;i<vec.size();i=i+2){
				Calendar cal =Calendar.getInstance();
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        System.out.println(df.format(cal.getTime()));//当前时间
		        System.out.println(vec.get(i+1).toString());
		        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        try {
					Date dt1 = df.parse(df.format(cal.getTime()));
					Date dt2 = df.parse(vec.get(i+1).toString());
					 if (dt1.getTime() > dt2.getTime()) {//导入授课计划
						 //删除考试信息
						 if(this.getQZQM().equals("0")){//导入全部
							 sql6 = "delete from dbo.V_考试管理_考试设置  where 学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' ";
						 }else{
							 //sql6 = "delete from dbo.V_考试管理_考试设置  where 学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' and 期中期末='" + MyTools.fixSql(this.getQZQM()) + "' ";
						 }
						 
						 if(db.executeInsertOrUpdate(sql6)){
							 //授课计划
							 sql2="select distinct c.行政班代码,c.行政班名称,c.学年学期编码," +
							 		"stuff((select '｜'+d.授课计划明细编号 from (select b.行政班代码,c.行政班名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 where b.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' ) d where c.行政班代码=d.行政班代码 and c.行政班名称=d.行政班名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码  and c.课程名称=d.课程名称 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称  for xml path('')),1,1,'') as 授课计划明细编号," +
							 		"c.是否考试,c.课程代码,c.课程名称,c.专业代码,c.专业名称,c.场地要求," +
							 		"stuff((select '｜'+d.上课周期 from (select b.行政班代码,c.行政班名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 where b.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' ) d where c.行政班代码=d.行政班代码 and c.行政班名称=d.行政班名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码  and c.课程名称=d.课程名称 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称  for xml path('')),1,1,'') as 上课周期," +
							 		"c.期中期末," +
							 		"stuff((select '｜'+d.考试形式 from (select b.行政班代码,c.行政班名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 where b.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' ) d where c.行政班代码=d.行政班代码 and c.行政班名称=d.行政班名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码  and c.课程名称=d.课程名称 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称 for xml path('')),1,1,'') as 考试形式 " +
							 		"from (select b.行政班代码,c.行政班名称,b.学年学期编码,a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 where b.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' ) c " +
							 		"order by c.行政班代码 ";
							 vec2=db.GetContextVector(sql2);
							 
							 if(vec2!=null&&vec2.size()>0){
								 for(int j=0;j<vec2.size();j=j+13){
									 String skjh="";
									 String kscc="";
									 String ksxs="";
									 //取第一个授课计划编号
									 if(vec2.get(j+3).toString().indexOf("｜")>-1){
										 skjh=vec2.get(j+3).toString().substring(0,vec2.get(j+3).toString().indexOf("｜"));
									 }else{
										 skjh=vec2.get(j+3).toString();
									 }
									 if(vec2.get(j+12).toString().indexOf("｜")>-1){
										 ksxs=vec2.get(j+12).toString().substring(0,vec2.get(j+12).toString().indexOf("｜"));
									 }else{
										 ksxs=vec2.get(j+12).toString();
									 }
									 kscc="KSCCBH"+skjh.substring(skjh.indexOf("_"),skjh.length());
									 //System.out.println("10:--"+vec2.get(j+10).toString());
									 String skzq="";
									 skzq=editskzq(vec2.get(j+10).toString(),jzzs,zbbh);
									 //例：skzq="1-18#KSZBBH_00000009";
									 
									 String[] skqzqm=skzq.split("#");
									 drflag=0;//是否导入
									 if(this.getQZQM().equals("0")){//导入全部
										 drflag=1;
									 }else{
										 if(this.getQZQM().equals(skqzqm[1])){
											 drflag=2;
										 }
									 }
									 
									 if(drflag==1){
										 sql3= " select '"+MyTools.fixSql(skjh)+"' as 授课计划明细编号," +
												"'"+MyTools.fixSql(vec2.get(j+4).toString())+"' as 是否考试," +
										 		"'"+MyTools.fixSql(vec2.get(j+5).toString())+"' as 课程代码," +
										 		"'"+MyTools.fixSql(vec2.get(j+6).toString())+"' as 课程名称," +
										 		"'"+MyTools.fixSql(vec2.get(j+7).toString())+"' as 专业代码," +
										 		"'"+MyTools.fixSql(vec2.get(j+8).toString())+"' as 专业名称," +
										 		"'1' as 场地类型," +
										 		"'"+MyTools.fixSql(vec2.get(j+9).toString())+"' as 场地要求," +
										 		"'"+MyTools.fixSql(skqzqm[0])+"' as 上课周期," +
										 		"'"+MyTools.fixSql(kscc)+"' as 考试场次编号, " +
										 		"'"+MyTools.fixSql(skqzqm[1])+"' as 期中期末, " +
										 		"'"+MyTools.fixSql(vec2.get(j+2).toString())+"' as 学年学期编码," +									 		
										 		"'"+MyTools.fixSql(vec2.get(j).toString())+"' as 行政班代码," +
											    "'"+MyTools.fixSql(vec2.get(j+1).toString())+"' as 行政班名称," +
											    "'"+MyTools.fixSql(ksxs)+"' as 考试形式," +
										 		"'' as 监考教师, " +
										 		"'"+MyTools.fixSql(skjh)+"' as 参考学生, " +
										 		"'' as 试卷类型  " ;
										 sql4="insert into dbo.V_考试管理_考试设置  (授课计划明细编号,是否考试,课程代码,课程名称,专业代码,专业名称,场地类型,场地要求,上课周期,考试场次编号,期中期末,学年学期编码,行政班代码,行政班名称,考试形式,监考教师,参考学生,试卷类型) " +sql3;
										 vec4.add(sql4);
									 }else if(drflag==2){
										 int flag1=0;
										 int flag2=0;
										 int flag3=0;
										 
										 if(vecsz1!=null&&vecsz1.size()>0) {
											 for(int r=0;r<vecsz1.size();r++) {
												 if(vecsz1.get(r).toString().equals(skjh)) {
													 flag1=1;
												 }
											 }
										 }
										 if(flag1==1) {//要导入的考试课程在其它时间段 ,不导入
											 
										 }else {
											 if(vecsz2!=null&&vecsz2.size()>0) {
												 for(int s=0;s<vecsz2.size();s++) {
													 if(vecsz2.get(s).toString().equals(skjh)) {
														 flag2=1;
													 }
												 }
											 }
											 if(flag2==1) {//在选择导入的时间段  update
												 sql4="update dbo.V_考试管理_考试设置 set 是否考试='"+MyTools.fixSql(vec2.get(j+4).toString())+"',"
												 		+ "课程代码='"+MyTools.fixSql(vec2.get(j+5).toString())+"',"
												 		+ "课程名称='"+MyTools.fixSql(vec2.get(j+6).toString())+"',"
												 		+ "专业代码='"+MyTools.fixSql(vec2.get(j+7).toString())+"',"
												 		+ "专业名称='"+MyTools.fixSql(vec2.get(j+8).toString())+"',"
												 		+ "场地要求='"+MyTools.fixSql(vec2.get(j+9).toString())+"',"
												 		+ "上课周期='"+MyTools.fixSql(skqzqm[0])+"',"
												 		+ "考试场次编号='"+MyTools.fixSql(kscc)+"',"
												 		+ "期中期末='"+MyTools.fixSql(skqzqm[1])+"',"
												 		+ "学年学期编码='"+MyTools.fixSql(vec2.get(j+2).toString())+"',"
												 		+ "行政班代码='"+MyTools.fixSql(vec2.get(j).toString())+"',"
												 		+ "行政班名称='"+MyTools.fixSql(vec2.get(j+1).toString())+"',"
												 		+ "考试形式='"+MyTools.fixSql(ksxs)+"',"
												 		+ "参考学生='"+MyTools.fixSql(skjh)+"' "
												 		+ "where 授课计划明细编号='"+MyTools.fixSql(skjh)+"' ";
												 vec4.add(sql4);
											 }else {//授课计划不存在 删除原有的 insert
//												 sqlsz3=" SELECT [行政班代码],[行政班名称],[学年学期编码],[授课计划明细编号],[是否考试],[课程代码],[课程名称] " + 
//															" FROM [dbo].[V_考试管理_考试设置] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考试类型]='1' ";
												 
												 if(vecsz3!=null&&vecsz3.size()>0) {
													 for(int k=0;k<vecsz3.size();k=k+7) {
														 //课程代码,课程名称,行政班名称 相同
														 if(vec2.get(j+5).toString().equals(vecsz3.get(k+5).toString()) && vec2.get(j+6).toString().equals(vecsz3.get(k+6).toString()) && vec2.get(j+6).toString().equals(vecsz3.get(k+6).toString()) && vec2.get(j+1).toString().equals(vecsz3.get(k+1).toString()) ) {
															 if(vecsz1!=null&&vecsz1.size()>0) {
																 for(int r=0;r<vecsz1.size();r++) {
																	 if(vecsz1.get(r).toString().equals(vecsz3.get(k+3).toString())) {
																		 flag3=1;
																	 }
																 }
															 }
															 if(flag3==1) {//要导入的考试课程在其它时间段 ,不删除
																 
															 }else {
																 sql4="delete from dbo.V_考试管理_考试设置 where 授课计划明细编号='"+MyTools.fixSql(vecsz3.get(k+3).toString())+"' ";
																 vec4.add(sql4);
																 
																 sql3= " select '"+MyTools.fixSql(skjh)+"' as 授课计划明细编号," +
																			"'"+MyTools.fixSql(vec2.get(j+4).toString())+"' as 是否考试," +
																	 		"'"+MyTools.fixSql(vec2.get(j+5).toString())+"' as 课程代码," +
																	 		"'"+MyTools.fixSql(vec2.get(j+6).toString())+"' as 课程名称," +
																	 		"'"+MyTools.fixSql(vec2.get(j+7).toString())+"' as 专业代码," +
																	 		"'"+MyTools.fixSql(vec2.get(j+8).toString())+"' as 专业名称," +
																	 		"'1' as 场地类型," +
																	 		"'"+MyTools.fixSql(vec2.get(j+9).toString())+"' as 场地要求," +
																	 		"'"+MyTools.fixSql(skqzqm[0])+"' as 上课周期," +
																	 		"'"+MyTools.fixSql(kscc)+"' as 考试场次编号, " +
																	 		"'"+MyTools.fixSql(skqzqm[1])+"' as 期中期末, " +
																	 		"'"+MyTools.fixSql(vec2.get(j+2).toString())+"' as 学年学期编码," +									 		
																	 		"'"+MyTools.fixSql(vec2.get(j).toString())+"' as 行政班代码," +
																		    "'"+MyTools.fixSql(vec2.get(j+1).toString())+"' as 行政班名称," +
																		    "'"+MyTools.fixSql(ksxs)+"' as 考试形式," +
																	 		"'' as 监考教师, " +
																	 		"'"+MyTools.fixSql(skjh)+"' as 参考学生, " +
																	 		"'' as 试卷类型  " ;
																sql4="insert into dbo.V_考试管理_考试设置  (授课计划明细编号,是否考试,课程代码,课程名称,专业代码,专业名称,场地类型,场地要求,上课周期,考试场次编号,期中期末,学年学期编码,行政班代码,行政班名称,考试形式,监考教师,参考学生,试卷类型) " +sql3;
																vec4.add(sql4);
															 }
														 }
													 }
												 }
												 
											 }
										 }										 
									 }
								 }				 
							 }
							 
							 //分层班
							 sql5="select b.[年级代码] as 行政班代码,a.[分层班名称],b.学年学期编码,a.[分层班编号],'1' as 是否考试,b.课程代码,b.课程名称,b.系代码 as 专业代码,'' as 专业名称,'' as 场地要求,b.授课周次 as 上课周期,'0' as 期中期末,b.考试形式  from V_规则管理_分层班信息表 a left join V_规则管理_分层课程信息表 b on a.[分层课程编号]=b.[分层课程编号]  where b.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' " +
							 	  " order by a.分层班名称 ";
							 vec5=db.GetContextVector(sql5);
							 if(vec5!=null&&vec5.size()>0){
								 for(int j=0;j<vec5.size();j=j+13){
									 String skjh="";
									 String kscc="";
									 String ksxs="";
									 if(vec5.get(j+3).toString().indexOf("｜")>-1){
										 skjh=vec5.get(j+3).toString().substring(0,vec5.get(j+3).toString().indexOf("｜"));
									 }else{
										 skjh=vec5.get(j+3).toString();
									 }
									 if(vec5.get(j+12).toString().indexOf("｜")>-1){
										 ksxs=vec5.get(j+12).toString().substring(0,vec5.get(j+12).toString().indexOf("｜"));
									 }else{
										 ksxs=vec5.get(j+12).toString();
									 }
									 kscc="FXCCBH"+skjh.substring(skjh.indexOf("_"),skjh.length());
									 //System.out.println("10:--"+vec2.get(j+10).toString());
									 String skzq="";
									 skzq=editskzq(vec5.get(j+10).toString(),jzzs,zbbh);
							
									 String[] skqzqm=skzq.split("#");
									 drflag=0;//是否导入
									 if(this.getQZQM().equals("0")){//导入全部
										 drflag=1;
									 }else{
										 if(this.getQZQM().equals(skqzqm[1])){
											 drflag=2;
										 }
									 }
									
									 if(drflag==1){
										 sql3= " select '"+MyTools.fixSql(skjh)+"' as 授课计划明细编号," +
												"'"+MyTools.fixSql(vec5.get(j+4).toString())+"' as 是否考试," +
										 		"'"+MyTools.fixSql(vec5.get(j+5).toString())+"' as 课程代码," +
										 		"'"+MyTools.fixSql(vec5.get(j+6).toString())+"' as 课程名称," +
										 		"'"+MyTools.fixSql(vec5.get(j+7).toString())+"' as 专业代码," +
										 		"'"+MyTools.fixSql(vec5.get(j+8).toString())+"' as 专业名称," +
										 		"'1' as 场地类型," +
										 		"'"+MyTools.fixSql(vec5.get(j+9).toString())+"' as 场地要求," +
										 		"'"+MyTools.fixSql(skqzqm[0])+"' as 上课周期," +
										 		"'"+MyTools.fixSql(kscc)+"' as 考试场次编号, " +
										 		"'"+MyTools.fixSql(skqzqm[1])+"' as 期中期末, " +
										 		"'"+MyTools.fixSql(vec5.get(j+2).toString())+"' as 学年学期编码," +									 		
										 		"'"+MyTools.fixSql(vec5.get(j).toString())+"' as 行政班代码," +
											    "'"+MyTools.fixSql(vec5.get(j+1).toString())+"' as 行政班名称," +
											    "'"+MyTools.fixSql(ksxs)+"' as 考试形式," +
										 		"'' as 监考教师, " +
										 		"'"+MyTools.fixSql(skjh)+"' as 参考学生, " +
										 		"'' as 试卷类型   " ;
										 sql4="insert into dbo.V_考试管理_考试设置  (授课计划明细编号,是否考试,课程代码,课程名称,专业代码,专业名称,场地类型,场地要求,上课周期,考试场次编号,期中期末,学年学期编码,行政班代码,行政班名称,考试形式,监考教师,参考学生,试卷类型) " +sql3;
										 vec4.add(sql4);
									 }
								 }				 					 
							 }
							 
							 
							 //选修课
							 sql2="select a.选修班名称 as 行政班代码,a.选修班名称,b.学年学期编码,a.授课计划明细编号,'2' as 是否考试,b.课程代码,b.课程名称,'' as 专业代码,'' as 专业名称,'' as 场地要求,a.授课周次 as 上课周期,'0' as 期中期末,考试形式  from V_规则管理_选修课授课计划明细表 a left join dbo.V_规则管理_选修课授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号  where b.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' " +
								  " order by a.选修班名称 ";
							 vec2=db.GetContextVector(sql2);
								 
							 if(vec2!=null&&vec2.size()>0){
								for(int j=0;j<vec2.size();j=j+13){
									String skjh="";
									String kscc="";
									String ksxs="";
									if(vec2.get(j+3).toString().indexOf("｜")>-1){
										skjh=vec2.get(j+3).toString().substring(0,vec2.get(j+3).toString().indexOf("｜"));
									}else{
										skjh=vec2.get(j+3).toString();
									}
									if(vec2.get(j+12).toString().indexOf("｜")>-1){
										ksxs=vec2.get(j+12).toString().substring(0,vec2.get(j+12).toString().indexOf("｜"));
									}else{
										ksxs=vec2.get(j+12).toString();
									}
									kscc="XXCCBH"+skjh.substring(skjh.indexOf("_"),skjh.length());
									//System.out.println("10:--"+vec2.get(j+10).toString());
									String skzq="";
									if(vec2.get(j+10).toString().equals("")){
										skzq="#"+zbbh.substring(zbbh.lastIndexOf(",")+1, zbbh.length());
									}else{
										skzq=editskzq(vec2.get(j+10).toString(),jzzs,zbbh);
									}
						
									String[] skqzqm=skzq.split("#");
									 drflag=0;//是否导入
									 if(this.getQZQM().equals("0")){//导入全部
										 drflag=1;
									 }else{
										 if(this.getQZQM().equals(skqzqm[1])){
											 drflag=2;
										 }
									 }
									
									 if(drflag==1){				
										sql3= " select '"+MyTools.fixSql(skjh)+"' as 授课计划明细编号," +
											 "'"+MyTools.fixSql(vec2.get(j+4).toString())+"' as 是否考试," +
											 "'"+MyTools.fixSql(vec2.get(j+5).toString())+"' as 课程代码," +
											 "'"+MyTools.fixSql(vec2.get(j+6).toString())+"' as 课程名称," +
											 "'"+MyTools.fixSql(vec2.get(j+7).toString())+"' as 专业代码," +
											 "'"+MyTools.fixSql(vec2.get(j+8).toString())+"' as 专业名称," +
											 "'1' as 场地类型," +
											 "'"+MyTools.fixSql(vec2.get(j+9).toString())+"' as 场地要求," +
											 "'"+MyTools.fixSql(skqzqm[0])+"' as 上课周期," +
											 "'"+MyTools.fixSql(kscc)+"' as 考试场次编号, " +
											 "'"+MyTools.fixSql(skqzqm[1])+"' as 期中期末, " +
											 "'"+MyTools.fixSql(vec2.get(j+2).toString())+"' as 学年学期编码," +									 		
											 "'"+MyTools.fixSql(vec2.get(j).toString())+"' as 行政班代码," +
											 "'"+MyTools.fixSql(vec2.get(j+1).toString())+"' as 行政班名称," +
											 "'"+MyTools.fixSql(ksxs)+"' as 考试形式," +
											 "'' as 监考教师, " +
											 "'"+MyTools.fixSql(skjh)+"' as 参考学生, " +
											 "'' as 试卷类型   " ;
										sql4="insert into dbo.V_考试管理_考试设置  (授课计划明细编号,是否考试,课程代码,课程名称,专业代码,专业名称,场地类型,场地要求,上课周期,考试场次编号,期中期末,学年学期编码,行政班代码,行政班名称,考试形式,监考教师,参考学生,试卷类型) " +sql3;
										vec4.add(sql4);
									 }
								}					 
							}
							
							//添加个人课程
							sql2="select a.行政班代码,b.行政班名称,a.学年学期编码,a.编号,'2' as 是否考试,a.课程编号,a.课程名称,'' as 专业代码,'' as 专业名称,'' as 场地要求,'' as 上课周期,'0' as 期中期末,'' as 考试形式  from V_排课管理_添加课程信息表 a,dbo.V_学校班级数据子类 b  where a.行政班代码=b.行政班代码 and a.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' " +
								 " order by b.行政班名称 ";
							vec2=db.GetContextVector(sql2);
									 
							if(vec2!=null&&vec2.size()>0){
								for(int j=0;j<vec2.size();j=j+13){
										String skjh="";
										String kscc="";
										String ksxs="";
										if(vec2.get(j+3).toString().indexOf("｜")>-1){
											skjh=vec2.get(j+3).toString().substring(0,vec2.get(j+3).toString().indexOf("｜"));
										}else{
											skjh=vec2.get(j+3).toString();
										}
										if(vec2.get(j+12).toString().indexOf("｜")>-1){
											ksxs=vec2.get(j+12).toString().substring(0,vec2.get(j+12).toString().indexOf("｜"));
										}else{
											ksxs=vec2.get(j+12).toString();
										}
										kscc="GRCCBH"+skjh.substring(skjh.indexOf("_"),skjh.length());
										//System.out.println("10:--"+vec2.get(j+10).toString());
										String skzq="";
										//skzq=editskzq(vec2.get(j+10).toString());
										skzq="#"+zbbh.substring(zbbh.lastIndexOf(",")+1, zbbh.length());
										
										String[] skqzqm=skzq.split("#");
										 drflag=0;//是否导入
										 if(this.getQZQM().equals("0")){//导入全部
											 drflag=1;
										 }else{
											 if(this.getQZQM().equals(skqzqm[1])){
												 drflag=2;
											 }
										 }
										
										 if(drflag==1){
											sql3= " select '"+MyTools.fixSql(skjh)+"' as 授课计划明细编号," +
												 "'"+MyTools.fixSql(vec2.get(j+4).toString())+"' as 是否考试," +
												 "'"+MyTools.fixSql(vec2.get(j+5).toString())+"' as 课程代码," +
												 "'"+MyTools.fixSql(vec2.get(j+6).toString())+"' as 课程名称," +
												 "'"+MyTools.fixSql(vec2.get(j+7).toString())+"' as 专业代码," +
												 "'"+MyTools.fixSql(vec2.get(j+8).toString())+"' as 专业名称," +
												 "'1' as 场地类型," +
												 "'"+MyTools.fixSql(vec2.get(j+9).toString())+"' as 场地要求," +
												 "'"+MyTools.fixSql(skqzqm[0])+"' as 上课周期," +
												 "'"+MyTools.fixSql(kscc)+"' as 考试场次编号, " +
												 "'"+MyTools.fixSql(skqzqm[1])+"' as 期中期末, " +
												 "'"+MyTools.fixSql(vec2.get(j+2).toString())+"' as 学年学期编码," +									 		
												 "'"+MyTools.fixSql(vec2.get(j).toString())+"' as 行政班代码," +
												 "'"+MyTools.fixSql(vec2.get(j+1).toString())+"' as 行政班名称," +
												 "'"+MyTools.fixSql(ksxs)+"' as 考试形式," +
												 "'' as 监考教师, " +
												 "'"+MyTools.fixSql(skjh)+"' as 参考学生, " +
												 "'' as 试卷类型   " ;
											sql4="insert into dbo.V_考试管理_考试设置  (授课计划明细编号,是否考试,课程代码,课程名称,专业代码,专业名称,场地类型,场地要求,上课周期,考试场次编号,期中期末,学年学期编码,行政班代码,行政班名称,考试形式,监考教师,参考学生,试卷类型) " +sql3;
											vec4.add(sql4);
										 }
								}					 
							}
							 
							if(db.executeInsertOrUpdateTransaction(vec4)){
								sql3="";
								this.setMSG("导入成功");
							}else{
								sql3="";
								this.setMSG("导入失败");
							}
							 
						 }else{

						 }
			         }else{//无操作
			        	
			         } 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public String editskzq(String zhouci,String jzzs,String zbbh) throws SQLException {
		String skzq="";
		String flag="";
		//System.out.println("zhouci:----"+zhouci);
		String skzq1=zhouci.replaceAll("\\&amp;", "\\#").replaceAll("｜", "\\#").replaceAll("\\&", "\\#").replaceAll("-", "\\#");
		String[] skzq2=skzq1.split("\\#");
		int min=0;
		int max=0;
		for(int i=0;i<skzq2.length;i++){
			if(min>Integer.parseInt(skzq2[i])||min==0){
				min=Integer.parseInt(skzq2[i]);
			}
			if(max<Integer.parseInt(skzq2[i])||max==0){
				max=Integer.parseInt(skzq2[i]);
			}
		}
		
		String[] zcnum=jzzs.split(",");
		String[] bhnum=zbbh.split(",");
		if(min==max){
			skzq=max+"#";
			for(int j=zcnum.length-1;j>=0;j--){
				if(max<=Integer.parseInt(zcnum[j])){
					flag=zcnum[j];
				}
			}
		}else{
			skzq=min+"-"+max+"#";
			for(int j=zcnum.length-1;j>=0;j--){
				if(max<=Integer.parseInt(zcnum[j])){
					flag=zcnum[j];
				}
			}
		}
		if(flag.equals("")){
			skzq+="KSZBBH_";
		}else{
			for(int j=0;j<zcnum.length;j++){
				if(flag.equals(zcnum[j])){
					skzq+=bhnum[j];
				}
			}
		}
		
		return skzq;
	}
	
	/**
	 * 导入大补考信息
	 * @date:2016-11-11
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void checkDABUKAO() throws SQLException {
		String sql = "";
		String sql2= "";
		String sql3= "";
		String sql4= "";
		Vector vec = null;
		Vector vec2 = null;
		
		String xn=this.getXNXQ().substring(0,4);
		
		//判断考试信息中大补考信息是否存在
		sql2 = "select count(*) from dbo.V_考试管理_大补考信息  where 学年学期='" + MyTools.fixSql(this.getXNXQ()) + "' ";
						
		if(!db.getResultFromDB(sql2)){//考试设置中不存在，生成考试信息
		//授课计划
		sql2=" select  课程名称,COUNT(*) as 人数,考试形式," +
			 " case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from (" +
			 " select left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when b.来源类型='3' then g.考试形式 else f.考试形式 end as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when e.年级代码=(cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩" +
			 " from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_规则管理_授课计划明细表 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码" +
			 " where d.学生状态 in ('01','05','08') and 成绩状态='1' and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1')) as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
			 " group by 课程名称,行政班名称,行政班简称,考试形式,系名称";
		vec2=db.GetContextVector(sql2);
				 
		if(vec2!=null&&vec2.size()>0){
			for(int j=0;j<vec2.size();j=j+6){

				sql3+= " select '"+MyTools.fixSql(this.getXNXQ())+"' as 学年学期," +
						"'"+MyTools.fixSql(vec2.get(j).toString())+"' as 课程名称," +
						"'"+MyTools.fixSql(vec2.get(j+1).toString())+"' as 人数," +
						"'"+MyTools.fixSql(vec2.get(j+2).toString())+"' as 考试形式," +
						"'"+MyTools.fixSql(vec2.get(j+3).toString())+"' as 系名称," +
						"'"+MyTools.fixSql(vec2.get(j+4).toString())+"' as 行政班简称," +
						"'"+MyTools.fixSql(vec2.get(j+5).toString())+"' as 行政班名称," +
						"'' as 辅导教师," +
						"'' as 时间," +
						"'' as 地点 union " ;
			}
			sql3=sql3.substring(0,sql3.length()-6);
			sql4="insert into dbo.V_考试管理_大补考信息  ([学年学期],[课程名称],[人数],[考试形式],[系名称],[行政班简称],[行政班名称],[辅导教师],[时间],[地点]) " +sql3;
			if(db.executeInsertOrUpdate(sql4)){
				sql3="";
				this.setMSG("导入完成");
			}else{
				sql3="";
				this.setMSG("导入失败");
			}					 
		}
							 
							 //选修课
//							 sql2="select '' as 行政班代码,a.选修班名称,b.学年学期编码,a.授课计划明细编号,'2' as 是否考试,b.课程代码,b.课程名称,'' as 专业代码,'' as 专业名称,'' as 场地要求,a.授课周次 as 上课周期,'0' as 期中期末,'' as 考试形式  from V_规则管理_选修课授课计划明细表 a left join dbo.V_规则管理_选修课授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号  where b.学年学期编码='" + MyTools.fixSql(vec.get(i).toString()) + "' " +
//								  " order by a.选修班名称 ";
//							 vec2=db.GetContextVector(sql2);
//								 
//							 if(vec2!=null&&vec2.size()>0){
//									 for(int j=0;j<vec2.size();j=j+13){
//										 String skjh="";
//										 String kscc="";
//										 if(vec2.get(j+3).toString().indexOf("｜")>-1){
//											 skjh=vec2.get(j+3).toString().substring(0,vec2.get(j+3).toString().indexOf("｜"));
//										 }else{
//											 skjh=vec2.get(j+3).toString();
//										 }
//										 kscc="XXCCBH"+skjh.substring(skjh.indexOf("_"),skjh.length());
//										 //System.out.println("10:--"+vec2.get(j+10).toString());
//										 String skzq=editskzq(vec2.get(j+10).toString());
//										 String[] skqzqm=skzq.split("#");
//										 sql3+= " select '"+MyTools.fixSql(skjh)+"' as 授课计划明细编号," +
//												"'"+MyTools.fixSql(vec2.get(j+4).toString())+"' as 是否考试," +
//										 		"'"+MyTools.fixSql(vec2.get(j+5).toString())+"' as 课程代码," +
//										 		"'"+MyTools.fixSql(vec2.get(j+6).toString())+"' as 课程名称," +
//										 		"'"+MyTools.fixSql(vec2.get(j+7).toString())+"' as 专业代码," +
//										 		"'"+MyTools.fixSql(vec2.get(j+8).toString())+"' as 专业名称," +
//										 		"'1' as 场地类型," +
//										 		"'"+MyTools.fixSql(vec2.get(j+9).toString())+"' as 场地要求," +
//										 		"'"+MyTools.fixSql(skqzqm[0])+"' as 上课周期," +
//										 		"'"+MyTools.fixSql(kscc)+"' as 考试场次编号, " +
//										 		"'"+MyTools.fixSql(skqzqm[1])+"' as 期中期末, " +
//										 		"'"+MyTools.fixSql(vec2.get(j+2).toString())+"' as 学年学期编码," +									 		
//										 		"'"+MyTools.fixSql(vec2.get(j).toString())+"' as 行政班代码," +
//											    "'"+MyTools.fixSql(vec2.get(j+1).toString())+"' as 行政班名称," +
//											    "'"+MyTools.fixSql(vec2.get(j+12).toString())+"' as 考试形式," +
//										 		"'' as 监考教师, " +
//										 		"'"+MyTools.fixSql(skjh)+"' as 参考学生, " +
//										 		"'' as 试卷类型  union " ;
//									 }
//									 sql3=sql3.substring(0,sql3.length()-6);
//									 sql4="insert into dbo.V_考试管理_考试设置  (授课计划明细编号,是否考试,课程代码,课程名称,专业代码,专业名称,场地类型,场地要求,上课周期,考试场次编号,期中期末,学年学期编码,行政班代码,行政班名称,考试形式,监考教师,参考学生,试卷类型) " +sql3;
//									 if(db.executeInsertOrUpdate(sql4)){
//										 sql3="";
//										 //this.setMSG("保存成功");
//									 }else{
//										 sql3="";
//									 }					 
//							}
							 
						 }else{//存在，从考试设置获取信息
							 this.setMSG("该学期大补考信息已存在");
						 }
			        
				

	}
	
	
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryTree(int pageNum, int page,String parentId,String level) throws SQLException {
		String sql = ""; // 查询用SQL语句
		String sql1 = ""; // 查询用SQL语句
		String sql2 = ""; // 查询用SQL语句
		String sql3 = ""; // 查询用SQL语句
		String sql4 = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		String beginTime = "";
		String endTime = "";
		String user1="";
		String user2="";
		String zydm="";
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz").replaceAll("@", "");//教学主管校长@211@
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr").replaceAll("@", "");//全校教导主任@212@
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr").replaceAll("@", "");//系部教导主任@213@
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl").replaceAll("@", "");//全校教务管理@214@
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl").replaceAll("@", "");//系部教务管理@215@
		String admin = MyTools.getProp(request, "Base.admin").replaceAll("@", "");//管理员
		
//		sql4="select 专业代码 from dbo.V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getiUSERCODE()) + "@%'";
//		vec4=db.GetContextVector(sql4);
//		if(vec4!=null&&vec4.size()>0){
//			if(this.iUSERCODE.equals("post")){
//				
//			}else{
//				for(int i=0;i<vec4.size();i++){
//					zydm+="'"+vec4.get(i).toString()+"',";
//				}
//				zydm=zydm.substring(0, zydm.length()-1);
//				user1="and c.专业代码 in ("+zydm+") ";
//				user2="and b.专业代码 in ("+zydm+") ";
//			}
//		}
//		
//		if(level.equals("0")){
//			sql = "select c.专业代码 as id,c.专业名称+c.专业代码  as text,'1' as px,case when (select count(*) from V_学校班级数据子类 where c.专业代码=专业代码 )=0 then 'open' else 'closed' end as state from V_专业基本信息数据子类 c where len(c.专业代码)>4 "+user1+
//				  " union select '9999990' as id,'分层班' as text,'2' as px,'open' as state " +
//			      " union select '9999999' as id,'选修课' as text,'3' as px,'open' as state order by px,text " ;
//		}
//		if(level.equals("1")){
//			sql = "select b.行政班代码 as id,b.行政班名称 as text,'open' as state " +
//				"from V_专业基本信息数据子类 a right join V_学校班级数据子类 b on a.专业代码=b.专业代码 right join dbo.V_规则管理_授课计划主表 c on b.行政班代码=c.行政班代码 " +
//				"where 1=1 and c.学年学期编码='"+MyTools.fixSql(this.getGG_XNXQBM())+"' and b.专业代码='"+MyTools.fixSql(parentId)+"'"+user2+" order by text " ;
//		}

		sql4 = "select 专业代码 from dbo.V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getiUSERCODE()) + "@%'";
		vec4=db.GetContextVector(sql4);
		if(vec4!=null&&vec4.size()>0){
			if(this.iUSERCODE.equals("post")){
				
			}else{
				for(int i=0;i<vec4.size();i++){
					zydm+="'"+vec4.get(i).toString()+"',";
				}
				zydm=zydm.substring(0, zydm.length()-1);
				user1="and c.专业代码 in ("+zydm+") ";
				user2="and b.专业代码 in ("+zydm+") ";
			}
		}
		
		String nj=this.getXNXQ().substring(2,4);
		
		if(level.equals("0")){
			//sql = "select c.专业代码 as id,c.专业名称+c.专业代码 as text,case when (select count(*) from V_学校班级数据子类 where c.专业代码=专业代码 )=0 then 'open' else 'closed' end as state from V_专业基本信息数据子类 c where len(c.专业代码)>4 "+user1+" order by text " ;
			sql = "select 系部代码 as id,系部名称 as text,'closed' as state from V_基础信息_系部信息表 where 系部代码<>'C00' " ;	 
			
			if(this.getAuth().indexOf(jxzgxz) > -1 || this.getAuth().indexOf(qxjdzr) > -1 || this.getAuth().indexOf(qxjwgl) > -1 || this.getAuth().indexOf(admin) > -1){
				
			}else if(this.getAuth().indexOf(xbjdzr) > -1 || this.getAuth().indexOf(xbjwgl) > -1) {
				sql += " and 系部代码 in ( select 系部代码 from V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getiUSERCODE()) + "' ) ";
			}else {
				sql += " and 系部代码 in ( select 系部代码 from V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getiUSERCODE()) + "' ) ";
			}
			
			sql+= "union select 'gfb' as id,'高复班' as text,'closed' as state ";
		}
		if(level.equals("1")){
//			sql = "select b.行政班代码 as id,b.行政班名称 as text,'open' as state " +
//				"from V_专业基本信息数据子类 a right join V_学校班级数据子类 b on a.专业代码=b.专业代码  " +
//				"where 1=1 and "+nj+"-substring(b.行政班代码,1,2)>=0 and "+nj+"-substring(b.行政班代码,1,2)<3 and b.专业代码='"+MyTools.fixSql(parentId)+"'"+user2+" order by text " ;
			if(parentId.equals("gfb")){
				sql = "select 教学班编号 as id,教学班名称 as text,'open' as state from V_基础信息_教学班信息表 where "+nj+"-left(年级代码,2) between 0 and 2 and 教学班类型='2' " ;
				sql+="order by text";
			}else{
				sql = "select id,text,state from (" +
					  "select 行政班代码 as id,行政班名称 as text,'open' as state,'1' as px from V_学校班级数据子类 where "+nj+"-left(行政班代码,2) between 0 and 2 and 系部代码='" + MyTools.fixSql(parentId) + "' " +
					  "union select 教学班编号 as id,教学班名称 as text,'open' as state,'2' as px from V_基础信息_教学班信息表 where "+nj+"-left(年级代码,2) between 0 and 2 and 系部代码='" + MyTools.fixSql(parentId) + "' and 教学班类型='1' " +
					  ") x order by px,text";
			}
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryGrid(int pageNum ,int pageSize) throws SQLException {
		String sql = "";
		Vector vec = null;
		String sqlxq="";
		String sqlty="";
		
		if(!this.getQZQM().equals("4")){
			sqlxq=" and a.期中期末='" + MyTools.fixSql(this.getQZQM()) + "'";
		}
		if(this.getXZBDM().equals("9999990")){
			sqlty=" and a.授课计划明细编号 like '%FCKCMX_%' and a.[专业代码]='' ";
		}else if(this.getXZBDM().equals("9999999")){
			sqlty=" and a.授课计划明细编号 like '%XXKMX_%' and a.[专业代码]='' ";
		}else{
			sqlty=" and a.行政班代码 ='" + MyTools.fixSql(this.getXZBDM()) + "' ";
		}
		sql="select a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,a.专业代码,a.专业名称,a.场地类型,c.名称,a.场地要求,a.上课周期,a.考试场次编号,a.期中期末,a.学年学期编码,a.行政班代码,a.行政班名称,b.编号,b.考试形式,a.监考教师,a.参考学生,a.试卷类型 " +
			"from dbo.V_考试管理_考试设置 a,dbo.V_考试形式 b,dbo.V_基础信息_教室类型 c where a.考试形式=b.编号 and a.场地类型=c.编号 and a.学年学期编码='" + MyTools.fixSql(this.getXNXQ()+this.getJXXZ()) + "' "+ sqlty + sqlxq;
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-07-30
	 * @author:lupengfei
	 * @description:获取学期对应的周数量
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void getWeeknum(String termid) throws SQLException{
		Vector vec = null;
		Vector vec3 = null;
		String sql3="";
		if(termid.equalsIgnoreCase("")){
			sql3="select distinct 学年+学期+教学性质 as termid,学年+学期,教学性质 as termid FROM V_规则管理_学年学期表 where 1=1 order by termid desc";
			vec3=db.GetContextVector(sql3);
			if(vec3!=null&&vec3.size()>0){
				termid=vec3.get(0).toString();
			}
		}
		String sql = " select COUNT(*) FROM dbo.V_规则管理_学期周次表 where 学年学期编码='"+MyTools.fixSql(termid)+"'" ;
		vec = db.GetContextVector(sql);
		if(vec!=null&vec.size()>0){
			this.setMSG(vec.get(0).toString());			
		}
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年+学期 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		//String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2015-05-27
	 * @author:yeq
	 * @param type 查询类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadYearCombo(String type) throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年 as comboName,学年 as comboValue,1 " +
				"from V_规则管理_学年学期表 where 状态='1'";
		//判断是否查询排课已过期学年
		if("over".equalsIgnoreCase(type)){
			sql += " and convert(nvarchar(10),getDate(),21)>convert(nvarchar(10),排课截止时间,21)";
			
		}
		sql += " order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJXXZCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
					 "from V_基础信息_教学性质 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询考试类型
	 * @date:2017-03-31
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKSLXCombo() throws SQLException{
		Vector vec = null;
		
		String sql = " select '' as comboValue,'请选择' as comboName " +
				" union select 编号 as comboValue,考试类型 as comboName from V_考试管理_考试类型表 " ;
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取查询考场考试周期下拉框
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector CX_XNXQMCCombobox() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年+学期 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		//String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取考试周期下拉框
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadQZQMCombo() throws SQLException{
		Vector vec = null;
		
		String sql = " select '1' as comboValue,'前十周' as comboName " +
				" union select '2' as comboValue,'前十五周' as comboName " +
				" union select '3' as comboValue,'期末' as comboName " +
				" union select '4' as comboValue,'全部' as comboName " ;
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取考试周期下拉框
	 * @date:2017-04-19
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector KSJXLHCombobox() throws SQLException{
		Vector vec = null;
		String sql = " SELECT [建筑物号] as comboValue,[建筑物名称] as comboName FROM [dbo].[V_建筑物基本数据类] order by 建筑物号 " ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取考试周期下拉框
	 * @date:2017-05-23
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector KS_KSLXCombobox() throws SQLException{
		Vector vec = null;
		String sql = " SELECT '笔试' as comboValue,'笔试' as comboName union select '上机' as comboValue,'上机' as comboName " ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取教师类型下拉框
	 * @date:2017-12-18
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector GT_JSLXCombobox() throws SQLException{
		Vector vec = null;
		String sql = " SELECT '' as comboValue,'请选择' as comboName "
				+ "union select '01' as comboValue,'班主任' as comboName " 
				+ "union select '02' as comboValue,'部门' as comboName " 
				+ "union select '03' as comboValue,'任课教师' as comboName " ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取监考日期下拉框
	 * @date:2017-06-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector JKRQCombobox(String jzzs) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		
		sql="select [考试日期] FROM [dbo].[V_考试管理_考场安排主表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [考场安排主表编号]='"+MyTools.fixSql(jzzs)+"' ";
		vec=db.GetContextVector(sql);
		sql2 = " SELECT '' as comboValue,'请选择' as comboName " ;
		if(vec!=null&&vec.size()>0){
			String[] jkdate=vec.get(0).toString().split(",");
			for(int i=0;i<jkdate.length;i++){
				sql2+=" union select '"+jkdate[i].trim()+"' as comboValue,'"+jkdate[i].trim()+"' as comboName ";
			}
		}
		
		vec2 = db.getConttexJONSArr(sql2, 0, 0);
		return vec2;
	}
	
	/**
	 * 读取考试规则设置
	 * @date:2017-05-03
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridSchedule(String kszbbh,String kslx) throws SQLException {
		String sql = "";
		Vector vec = null;


		sql="select [考试日期],[考试时间段],[考试类型],[考试年级],[课程类型],[教室人数],[考场数量],[可用大教室] " +
			" from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+MyTools.fixSql(kszbbh)+"' and 考试类型='"+MyTools.fixSql(kslx)+"' " +
			" order by [考试日期],[考试时间段] ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 保存考试规则设置
	 * @date:2017-05-03
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void saveSchedule(String kszbbh,String ksrqsjdarray,String kslx,String ksnj,String kclx,String jsrs,String kcsl,String kydjs) throws SQLException {
		String sql = "";
		Vector vec = new Vector();
		String ksrq="";
		String kssjd="";
		if(ksrqsjdarray.indexOf(",")>-1){
			String[] ksrqsjd=ksrqsjdarray.split(",");
			for(int i=0;i<ksrqsjd.length;i++){
				ksrq=ksrqsjd[i].split("#")[0];
				kssjd=ksrqsjd[i].split("#")[1];
				sql="update [V_考试管理_考试规则] set [考试年级]='"+MyTools.fixSql(ksnj)+"',[课程类型]='"+MyTools.fixSql(kclx)+"',[教室人数]='"+MyTools.fixSql(jsrs)+"',[考场数量]='"+MyTools.fixSql(kcsl)+"',[可用大教室]='"+MyTools.fixSql(kydjs)+"' " +
					"where [考试主表编号]='"+MyTools.fixSql(kszbbh)+"' and [考试日期]='"+MyTools.fixSql(ksrq)+"' and [考试时间段]='"+MyTools.fixSql(kssjd)+"' and [考试类型]='"+MyTools.fixSql(kslx)+"' ";
				vec.add(sql);
			}
		}else{
			ksrq=ksrqsjdarray.split("#")[0];
			kssjd=ksrqsjdarray.split("#")[1];
			sql="update [V_考试管理_考试规则] set [考试年级]='"+MyTools.fixSql(ksnj)+"',[课程类型]='"+MyTools.fixSql(kclx)+"',[教室人数]='"+MyTools.fixSql(jsrs)+"',[考场数量]='"+MyTools.fixSql(kcsl)+"',[可用大教室]='"+MyTools.fixSql(kydjs)+"' " +
					"where [考试主表编号]='"+MyTools.fixSql(kszbbh)+"' and [考试日期]='"+MyTools.fixSql(ksrq)+"' and [考试时间段]='"+MyTools.fixSql(kssjd)+"' and [考试类型]='"+MyTools.fixSql(kslx)+"' ";
			vec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}		
	}
	
	/**
	 * 清空考试规则设置
	 * @date:2017-05-03
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void clearSchedule(String kszbbh,String ksrqsjdarray,String jsrs,String kcsl,String kydjs,String kslx) throws SQLException {
		String sql = "";
		Vector vec = new Vector();
		String ksrq="";
		String kssjd="";
		
		if(ksrqsjdarray.indexOf(",")>-1){
			String[] ksrqsjd=ksrqsjdarray.split(",");
			for(int i=0;i<ksrqsjd.length;i++){
				ksrq=ksrqsjd[i].split("#")[0];
				kssjd=ksrqsjd[i].split("#")[1];
				sql="update [V_考试管理_考试规则] set [考试年级]='',[课程类型]='',[教室人数]='',[考场数量]='',[可用大教室]='' " +
					"where [考试主表编号]='"+MyTools.fixSql(kszbbh)+"' and [考试日期]='"+MyTools.fixSql(ksrq)+"' and [考试时间段]='"+MyTools.fixSql(kssjd)+"' and [考试类型]='"+MyTools.fixSql(kslx)+"' ";
				vec.add(sql);
			}
		}else{
			ksrq=ksrqsjdarray.split("#")[0];
			kssjd=ksrqsjdarray.split("#")[1];
			sql="update [V_考试管理_考试规则] set [考试年级]='',[课程类型]='',[教室人数]='',[考场数量]='',[可用大教室]='' " +
					"where [考试主表编号]='"+MyTools.fixSql(kszbbh)+"' and [考试日期]='"+MyTools.fixSql(ksrq)+"' and [考试时间段]='"+MyTools.fixSql(kssjd)+"' and [考试类型]='"+MyTools.fixSql(kslx)+"' ";
			vec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}		
	}
	
	/**
	 * 显示特殊课程设置
	 * @date:2017-05-04
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridSpecialCourse(int pageNum, int pageSize,String QZQM) throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="SELECT a.[考试编号],b.[考试名称],a.[考试场次编号],a.[课程名称],a.[班级名称],a.[日期时间段] FROM [dbo].[V_考试管理_考试特殊课程规则] a,[dbo].[V_考试管理_考场安排主表] b where a.[考试编号]=b.[考场安排主表编号] and b.[考场安排主表编号]='"+QZQM+"' order by a.课程名称 ";
		vec=db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 按考试场次编号显示考试合并课程
	 * @date:2017-05-04
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridAllCourse(int pageNum, int pageSize) throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="SELECT distinct [考试场次编号]," +
			"stuff((select '｜'+d.[课程名称] from [V_考试管理_考试设置] d where c.[考试场次编号]=d.[考试场次编号]  for xml path('')),1,1,'') as [课程名称]," +
			"stuff((select '｜'+d.[行政班名称] from [V_考试管理_考试设置] d where c.[考试场次编号]=d.[考试场次编号]  for xml path('')),1,1,'') as [行政班名称] " +
			"FROM [dbo].[V_考试管理_考试设置] c " +
			"where c.[学年学期编码]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' and c.期中期末='"+MyTools.fixSql(this.getQZQM())+"' ";
		vec=db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 读取考试规则设置
	 * @date:2017-05-05
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridselRQ(String kszbbh) throws SQLException {
		String sql = "";
		Vector vec = null;


		sql="select [考试日期],[考试时间段] " +
			" from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+MyTools.fixSql(kszbbh)+"' and 考试类型='笔试' " +
			" order by [考试日期],[考试时间段] ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取考试规则设置
	 * @date:2017-05-05
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridselKSKC(int pageNum,int pageSize,String kszbbh,String kcmc ,String bjmc,String ksccbh) throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		Vector vec = null;
		
		sql="SELECT distinct c.考试场次编号, " +
			"stuff((select ','+课程名称 from [V_考试管理_考试设置] d where c.考试场次编号=d.考试场次编号 for XML PATH('')),1,1,'') as 课程名称," +
			"stuff((select ','+行政班名称 from [V_考试管理_考试设置] d where c.考试场次编号=d.考试场次编号 for XML PATH('')),1,1,'') as 行政班名称" +
			" FROM [dbo].[V_考试管理_考试设置] c " +
			" where c.期中期末='"+MyTools.fixSql(kszbbh)+"' and 是否考试='1' ";
		sql2="select 考试场次编号,课程名称,行政班名称,'1' as sel from ("+sql+") m where 考试场次编号='"+ksccbh+"' " +
			"union select 考试场次编号,课程名称,行政班名称,'0' as sel from ("+sql+") n where 考试场次编号!='"+ksccbh+"' ";
		sql3="select 考试场次编号 as 授课计划明细编号,课程名称,行政班名称,sel from ("+sql2+") k where 1=1";
		if(!kcmc.equals("")){
			sql3+="and 课程名称 like '%"+MyTools.fixSql(kcmc)+"%' ";
		}
		if(!bjmc.equals("")){
			sql3+="and 行政班名称 like '%"+MyTools.fixSql(bjmc)+"%' ";
		}
		sql3+="order by sel desc";
		vec = db.getConttexJONSArr(sql3, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 读取考试规则设置
	 * @date:2017-05-05
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryBUKAOinfo(int pageNum,int pageSize,String BK_KCMC,String BK_KSXS) throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		Vector vec = null;
		
		//获取日期，时间段信息
		String sqlksrq="select [考试日期] from [dbo].[V_考试管理_考场安排主表] where [考场安排主表编号]='"+this.getQZQM()+"' ";
		Vector vecksrq=db.GetContextVector(sqlksrq);
		if(vecksrq!=null&&vecksrq.size()>0){
			this.setMSG(vecksrq.get(0).toString());//考试日期
		}		
		
		sql="SELECT '"+this.getXNXQ()+"' as 学年学期名称,[考场安排明细编号],[考场安排主表编号],[考试场次编号],[时间序列],[课程名称],[场地要求],[行政班名称],[考试形式],[学生人数],[试卷类型] " +
			"FROM [dbo].[V_考试管理_考场安排明细表] where [考场安排主表编号]='"+this.getQZQM()+"' " ;
		if(!BK_KCMC.equals("")){
			sql+=" and 课程名称 like '%"+BK_KCMC+"%' ";
		}
		if(!BK_KSXS.equals("")){
			sql+=" and 试卷类型 = '"+BK_KSXS+"' ";
		}
		sql+="order by [考试形式] desc,[时间序列],[课程名称],[考场安排明细编号] ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	public Vector BKKSXSCombobox(String PK_KSLX) throws SQLException{
		Vector vec = null;
		String sql = "";
		if(PK_KSLX.equals("2")){
			sql="SELECT [考试形式] AS comboValue,case when [考试形式]='' then '请选择' else [考试形式] end AS comboName FROM [dbo].[V_考试形式]";
		}else{
			sql="SELECT distinct [考试形式] AS comboValue,case when [考试形式]='' then '请选择' else [考试形式] end AS comboName FROM [dbo].[V_考试形式大补考]";
		}	
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	public Vector kc_zydmCombobox() throws SQLException{
		Vector vec = null;
		String sql = "";
		
		sql="SELECT distinct c.专业代码 as comboValue,case when d.专业名称 is null then '公共课' else d.专业名称 end as comboName  "
				+ "FROM [dbo].[V_规则管理_授课计划明细表] a " + 
				"  left join [dbo].V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " + 
				"  left join dbo.V_课程数据子类 c on a.课程代码=c.课程号 " + 
				"  left join dbo.V_专业基本信息数据子类 d on c.专业代码=d.专业代码 " + 
				"  where b.学年学期编码='"+MyTools.fixSql(this.getXNXQ())+"' order by c.专业代码 ";	
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	public Vector KC_XZBDMCombobox() throws SQLException{
		Vector vec = null;
		String sql = "";
		
		int nj=Integer.parseInt(this.getXNXQ().substring(2,4));
		sql="SELECT [班级编号] as comboValue,[班级名称] as comboName FROM [dbo].[V_基础信息_班级信息表] where 年级代码 in ('"+(nj+"1")+"','"+((nj-1)+"1")+"','"+((nj-2)+"1")+"') order by 班级编号 ";	
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	public Vector sel_KCCombobox(String selcouArray,String choice) throws SQLException{
		Vector vec = null;
		String sql = "";
		String selcourse="";
		
		if(!selcouArray.equals("")) {
			if(selcouArray.indexOf(",")>-1) {
				String[] couid=selcouArray.split(",");
				for(int i=0;i<couid.length;i++) {
					if(couid[i].equals(choice)) {
						
					}else {
						selcourse+="'"+couid[i]+"',";
					}
				}
				if(!selcourse.equals("")) {
					selcourse=selcourse.substring(0,selcourse.length()-1);
				}
			}else {
				if(selcouArray.equals(choice)) {
					selcourse="''";
				}else {
					selcourse="'"+selcouArray+"'";
				}	
			}
		}else {
			selcourse="''";
		}
		
		sql= "select '' as comboValue,'请选择' as comboName union "
				+ "SELECT [考场安排明细编号] as comboValue,[课程名称] as comboName FROM [dbo].[V_考试管理_考场安排明细表] "
				+ "where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and 行政班代码='"+MyTools.fixSql(this.getXZBDM())+"' "
				+ "and [考场安排明细编号] not in ("+selcourse+")";	
		System.out.println(sql);
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	public Vector sel_KCallCombobox(String selcouArray,String choice) throws SQLException{
		Vector vec = null;
		String sql = "";
		String selcourse="";
		
		sql="select '' as comboValue,'请选择' as comboName union "
				+ "SELECT  [考场安排明细编号] as comboValue,[课程名称] as comboName FROM [dbo].[V_考试管理_考场安排明细表] "
				+ "where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and 行政班代码='"+MyTools.fixSql(this.getXZBDM())+"' ";
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取考试规则设置
	 * @date:2017-05-05
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void savespecial(String kszbbh,String tskcrqsjdarray ,String ksccbh,String kcmc,String bjmc) throws SQLException {
		String sql = "";
		Vector vec = null;

		sql="insert into [dbo].[V_考试管理_考试特殊课程规则] ([考试编号],[考试场次编号],[课程名称],[班级名称],[日期时间段]) values (" +
				"'"+MyTools.fixSql(kszbbh)+"'," +
				"'"+MyTools.fixSql(ksccbh)+"'," +
				"'"+MyTools.fixSql(kcmc)+"'," +
				"'"+MyTools.fixSql(bjmc)+"'," +
				"'"+MyTools.fixSql(tskcrqsjdarray)+"')";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除特殊课程设置
	 * @date:2017-05-05
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void delspecial(String kszbbh,String ksccbh) throws SQLException {
		String sql = "";
		Vector vec = null;

		sql="delete from [dbo].[V_考试管理_考试特殊课程规则] where [考试编号]='"+MyTools.fixSql(kszbbh)+"' and [考试场次编号]='"+MyTools.fixSql(ksccbh)+"' " ;

		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	
	/**
	 * 读取查询考场考试周期下拉框
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector CXQZQMCombobox() throws SQLException{
		Vector vec = null;

		String sql = " select '1' as comboValue,'前十周' as comboName,'2' as px " +
				" union select '2' as comboValue,'前十五周' as comboName,'3' as px " +
				" union select '3' as comboValue,'期末' as comboName,'4' as px " +
				" union select '4' as comboValue,'全部' as comboName,'1' as px " +
				" union select '5' as comboValue,'补考' as comboName,'5' as px " ;

		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取排考场考试周期下拉框
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector PKQZQMCombobox(String xnxq) throws SQLException{
		Vector vec = null;
		
		String sql = " select [考场安排主表编号] as comboValue,[考试名称] as comboName from V_考试管理_考场安排主表  where [学年学期编码]='"+MyTools.fixSql(xnxq)+"' order by 考场安排主表编号  " ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取排考场考试周期下拉框
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector PKXBDMCombobox(String xnxq) throws SQLException{
		Vector vec = null;
		
		String sql = " select '' as comboValue,'请选择' as comboName union "
				+ " select [系部代码] as comboValue,"
				+ "case when [系部代码]='C01' then '国商部，旅游部' else [系部名称] end as comboName "
				+ "from [V_基础信息_系部信息表] where [系部代码]!='C00' and [系部代码]!='C02' " ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取排考场考试天数下拉框
	 * @date:2016-12-19
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector PKKSTSCombobox() throws SQLException{
		Vector vec = null;
		
		String sql = " select '2' as comboValue,'2天' as comboName " +
				" union select '3' as comboValue,'3天' as comboName " ;

		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKSRQ() throws SQLException{
		Vector vec = null;
		String day="";
		String kszq="";
				
		String sql = " SELECT  a.[考试日期明细编号],a.考场安排主表编号,b.考试名称,[考试日期],[开始时间],[结束时间] FROM [dbo].[V_考试管理_考试日期] a " + 
				" left join dbo.V_考试管理_考场安排主表 b on a.考场安排主表编号=b.考场安排主表编号  where a.考场安排主表编号='"+MyTools.fixSql(this.getKCAPZBBH())+"' order by [考试日期],convert(int,substring([开始时间],0,charindex(':',[开始时间])))  ";
		vec=db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//保存考试日期
	public void saveKSRQ(String kssj,String jssj) throws SQLException{
		Vector vec = new Vector();
		String sql="";
		String maxMxId="";
		String[] kstime=null;
		
		if(Integer.parseInt(kssj.substring(0,kssj.indexOf(":")))<10) {
			kssj="0"+kssj;
		}
		if(Integer.parseInt(jssj.substring(0,jssj.indexOf(":")))<10) {
			jssj="0"+jssj;
		}
		
		//获取考场安排明细编号
		String sqlmxid="select max(cast(SUBSTRING([考试日期明细编号],8,11) as bigint)) from dbo.V_考试管理_考试日期 ";
		Vector vecmxid = db.GetContextVector(sqlmxid);
		if (!vecmxid.toString().equals("[]") && vecmxid.size() > 0) {
			maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vecmxid.get(0))))+1);
			this.setKSRQBH("KSRQBH_"+maxMxId);//设置授课计划明细主键
		}else{
			maxMxId= String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr("10000000000")))+1);
			this.setKSRQBH("KSRQBH_"+maxMxId);//设置授课计划明细主键
		}
		
		sql="insert into [V_考试管理_考试日期] ([考试日期明细编号],[考场安排主表编号],[考试日期],[开始时间],[结束时间]) "
				+ "values ('"+MyTools.fixSql(this.getKSRQBH())+"','"+MyTools.fixSql(this.getKCAPZBBH())+"','"+MyTools.fixSql(this.getEx_ksrq())+"','"+kssj+"','"+jssj+"') ";
		vec.add(sql);
				
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}

	}
	
	//编辑考试日期
	public void editKSRQ(String kssj,String jssj) throws SQLException{
		Vector vec = new Vector();
		String sql="";
		String[] kstime=null;
		
		sql="update [V_考试管理_考试日期] set [考试日期]='"+MyTools.fixSql(this.getEx_ksrq())+"',[开始时间]='"+MyTools.fixSql(kssj)+"',[结束时间]='"+MyTools.fixSql(jssj)+"' "
			+ " where 考试日期明细编号='"+MyTools.fixSql(this.getKSRQBH())+"' ";
		vec.add(sql);
				
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}

	}
	
	//编辑考试日期
	public void delKSRQ() throws SQLException{
		Vector vec = new Vector();
		String sql="";
			
		sql="delete from [V_考试管理_考试日期] where 考试日期明细编号='"+MyTools.fixSql(this.getKSRQBH())+"' ";
		vec.add(sql);
					
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}

	}
	
	//保存考试课程
	public void saveKSKC(String ei_zydm,String kskcarray) throws SQLException{
		Vector vec = new Vector();
		Vector vec2 = null;
		String sql="";
		String sql2="";
		String[] kscourse=null;
		int flag=0;
		
		//查询专业里取消的考试课程，从考试明细表里删除
		if(!kskcarray.equals("")) {
			kscourse=kskcarray.split(",");
			System.out.println(kskcarray);
			sql2="select 课程代码 from V_考试管理_考试课程 where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [专业编号]='"+MyTools.fixSql(ei_zydm)+"' ";
			vec2=db.GetContextVector(sql2);
			if(vec!=null&&vec2.size()>0) {
				for(int i=0;i<vec2.size();i++) {
					flag=0;
					for(int j=0;j<kscourse.length;j++) {
						if(vec2.get(i).toString().equals(kscourse[j])) {
							flag=1;
						}
					}
					if(flag==0) {//不存在
						String sqlzy="";
						if(ei_zydm.equals("000000")) {
							sqlzy="";
						}else {
							sqlzy=" and [专业代码]='"+MyTools.fixSql(ei_zydm)+"' ";
						}
						sql="delete from dbo.V_考试管理_考场安排明细表"
						+ " where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [课程代码]='"+vec2.get(i).toString()+"' " + sqlzy ;
						vec.add(sql);
					}
				}
			}
		}
		
		//保存
		sql="delete from V_考试管理_考试课程 where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [专业编号]='"+MyTools.fixSql(ei_zydm)+"' ";	
		vec.add(sql);
		
		if(!kskcarray.equals("")) {
			kscourse=kskcarray.split(",");
			for(int i=0;i<kscourse.length;i++) {
				sql="insert into V_考试管理_考试课程 ([考场安排主表编号],[专业编号],[课程代码]) "
					+ "values ('"+MyTools.fixSql(this.getKCAPZBBH())+"','"+MyTools.fixSql(ei_zydm)+"','"+kscourse[i]+"') ";
				vec.add(sql);
			}
		}		
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}

	}
	
	/**
	 * 
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJCSJ() throws SQLException{
		Vector vec = null;
		String sql = "";
		sql = "select 节次,开始时间,结束时间 from V_规则管理_节次时间表 where 学年学期编码='"
			  + MyTools.fixSql(MyTools.StrFiltr(this.XNXQ)) + MyTools.fixSql(MyTools.StrFiltr(this.JXXZ)) + "' order by 开始时间";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 保存方法
	 * @date:2016-03-08
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void savesfks(String sfks,String cdlx,String kszq,String sjlx,String ksxs) throws WrongSQLException, SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec4 = null; // 结果集
		
		//if(sfks.equals("1")){//是
			sql="update dbo.V_考试管理_考试设置 set 是否考试='"+MyTools.fixSql(sfks)+"'," +
				"考试形式='"+MyTools.fixSql(ksxs)+"'," +
				"场地类型='"+MyTools.fixSql(cdlx)+"'," +
				"期中期末='"+MyTools.fixSql(kszq)+"'," +
				"试卷类型='"+MyTools.fixSql(sjlx)+"' " +
				" where 授课计划明细编号='"+MyTools.fixSql(this.getGG_SKJHMXBH())+"' ";
			//修改授课计划明细表的考试形式
			sql2=" select a.课程代码,b.行政班代码,a.授课计划明细编号 from  dbo.V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b " +
					"where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号='"+MyTools.fixSql(this.getGG_SKJHMXBH())+"' ";
			vec2=db.GetContextVector(sql2);
			if(vec2!=null&&vec2.size()>0){
				sql3="update dbo.V_规则管理_授课计划明细表 set [考试形式]='"+MyTools.fixSql(ksxs)+"' where 授课计划明细编号 in " +
						"( select a.授课计划明细编号  from dbo.V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b " +
						" where a.授课计划主表编号=b.授课计划主表编号 and 课程代码='"+vec2.get(0).toString()+"' and b.行政班代码='"+vec2.get(1).toString()+"' and b.学年学期编码='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ) ";
				db.executeInsertOrUpdate(sql3);
			}

			//参考学生表里没有记录则添加
//			sql4="select count(*) from dbo.V_考试管理_参考学生 where 授课计划明细编号='"+MyTools.fixSql(this.getGG_SKJHMXBH())+"' and 期中期末='"+MyTools.fixSql(this.getQZQM())+"' ";
//			vec4=db.GetContextVector(sql4);
//			if(vec4.get(0).toString().equals("0")){
//				sql+=" insert into dbo.V_考试管理_参考学生 "+
//						"select '"+MyTools.fixSql(this.getGG_SKJHMXBH())+"' as 授课计划明细编号,a.期中期末,a.行政班代码,b.学号  from V_考试管理_考试设置 a " +
//						"left join dbo.V_学生基本数据子类 b on a.行政班代码=b.行政班代码  " +
//						"where a.授课计划明细编号='"+MyTools.fixSql(this.getGG_SKJHMXBH())+"' and a.期中期末='"+MyTools.fixSql(this.getQZQM())+"' order by a.授课计划明细编号,a.行政班代码" ;
//			}
//		}else{
//			sql="update dbo.V_考试管理_考试设置 set 是否考试='"+MyTools.fixSql(sfks)+"'," +
//				"场地类型='1'," +
//				"场地要求=''," +
//				"考试场次编号=授课计划明细编号," +
//				"试卷类型='' " +
//				" where 授课计划明细编号='"+MyTools.fixSql(this.getGG_SKJHMXBH())+"' and 期中期末='"+MyTools.fixSql(this.getQZQM())+"' ";
//			sql+=" delete from dbo.V_考试管理_参考学生 where 授课计划明细编号='"+MyTools.fixSql(this.getGG_SKJHMXBH())+"' and 期中期末='"+MyTools.fixSql(this.getQZQM())+"' ";
//		}
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}	
	}
	
	/**
	 * 保存大补考
	 * @date:2016-11-14
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void savedbkksxs(String dbkarray,String dbk_xnxq,String dbk_ksxs) throws WrongSQLException, SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec4 = null; // 结果集
		String xnxqbm=dbk_xnxq+"01";
		String database="";
		String sqlcheck="";
		Vector veccheck=null;
		
		System.out.println("dbkarray--------"+dbkarray);
		String[] skjhmx=dbkarray.split(",");
		for(int s=0;s<skjhmx.length;s++){
			String[] items=skjhmx[s].split("#");
			String dbk_skjhmx=items[0];
			//String dbk_kcmc=items[2];
			if(dbk_skjhmx.indexOf("@")>-1){
				String[] skjhs=dbk_skjhmx.split("@");
				//String[] kcmcs=dbk_kcmc.split("@");
				for(int i=0;i<skjhs.length;i++){
					
					database="V_考试管理_考试设置";
					
					if(skjhs[i].indexOf("$")>-1){
						String[] skjhmxss=skjhs[i].split("\\$");
						for(int j=0;j<skjhmxss.length;j++){
							sqlcheck="select count(*) from  V_考试管理_考试设置 where [授课计划明细编号]='"+MyTools.fixSql(skjhmxss[j])+"' ";
							veccheck=db.GetContextVector(sqlcheck);
							if(veccheck.get(0).toString().equals("0")){
								sql+="insert into "+database+" (授课计划明细编号,是否考试,考试形式) values ('"+MyTools.fixSql(skjhmxss[j])+"','1','"+MyTools.fixSql(dbk_ksxs)+"') " ;
							}else{
								sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
									"from "+database+" a where a.[授课计划明细编号]='"+MyTools.fixSql(skjhmxss[j])+"' " ;
								sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
									"from V_规则管理_选修课授课计划明细表 a where a.[授课计划明细编号]='"+MyTools.fixSql(skjhmxss[j])+"' " ;
							}
						}
					}else{
						sqlcheck="select count(*) from  V_考试管理_考试设置 where [授课计划明细编号]='"+MyTools.fixSql(skjhs[i])+"' ";
						veccheck=db.GetContextVector(sqlcheck);
						if(veccheck.get(0).toString().equals("0")){
							sql+="insert into "+database+" (授课计划明细编号,是否考试,考试形式) values ('"+MyTools.fixSql(skjhs[i])+"','1','"+MyTools.fixSql(dbk_ksxs)+"') " ;
						}else{
							sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
								"from "+database+" a where a.[授课计划明细编号]='"+MyTools.fixSql(skjhs[i])+"' " ;
							sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
								"from V_规则管理_选修课授课计划明细表 a where a.[授课计划明细编号]='"+MyTools.fixSql(skjhs[i])+"' " ;
						}	
					}
				}
			}else{
				
				database="V_考试管理_考试设置";
				
				if(dbk_skjhmx.indexOf("$")>-1){
					String[] skjhmxss=dbk_skjhmx.split("\\$");
					for(int j=0;j<skjhmxss.length;j++){
						sqlcheck="select count(*) from  V_考试管理_考试设置 where [授课计划明细编号]='"+MyTools.fixSql(skjhmxss[j])+"' ";
						veccheck=db.GetContextVector(sqlcheck);
						if(veccheck.get(0).toString().equals("0")){
							sql+="insert into "+database+" (授课计划明细编号,是否考试,考试形式) values ('"+MyTools.fixSql(skjhmxss[j])+"','1','"+MyTools.fixSql(dbk_ksxs)+"') " ;
						}else{
							sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
								"from "+database+" a where a.[授课计划明细编号]='"+MyTools.fixSql(skjhmxss[j])+"' " ;
							sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
								"from V_规则管理_选修课授课计划明细表 a where a.[授课计划明细编号]='"+MyTools.fixSql(skjhmxss[j])+"' " ;
						}
					}
				}else{
					sqlcheck="select count(*) from  V_考试管理_考试设置 where [授课计划明细编号]='"+MyTools.fixSql(dbk_skjhmx)+"' ";
					veccheck=db.GetContextVector(sqlcheck);
					if(veccheck.get(0).toString().equals("0")){
						sql+="insert into "+database+" (授课计划明细编号,是否考试,考试形式) values ('"+MyTools.fixSql(dbk_skjhmx)+"','1','"+MyTools.fixSql(dbk_ksxs)+"') " ;
					}else{
						sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
							"from "+database+" a where a.[授课计划明细编号]='"+MyTools.fixSql(dbk_skjhmx)+"' " ;
						sql+="update a set a.考试形式='"+MyTools.fixSql(dbk_ksxs)+"' " +
							"from V_规则管理_选修课授课计划明细表 a where a.[授课计划明细编号]='"+MyTools.fixSql(dbk_skjhmx)+"' " ;
					}	
				}
			}
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}	
	}
	
	/**
	 * 设置同时开考查询其他课程
	 * @date:2016-04-06
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector listkskc(int pageNum ,int pageSize,String BJMC_CX,String KCMC_CX,String ZYDM_CX,String examid) throws SQLException {
		String sql = "";
		Vector vec = null;

		String skjhid="SKJHMX"+examid.substring(examid.indexOf("_"), examid.length());
		sql="select a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,a.专业代码,a.专业名称,a.场地要求,a.上课周期,a.考试场次编号,a.期中期末,a.学年学期编码,a.行政班代码,a.行政班名称,b.考试形式,a.监考教师,a.参考学生 " +
			"from dbo.V_考试管理_考试设置 a,dbo.V_考试形式 b where a.考试形式=b.编号 and a.学年学期编码='" + MyTools.fixSql(this.getXNXQ()+this.getJXXZ()) +"' and a.期中期末='" + MyTools.fixSql(this.getQZQM()) + "' " +
			" and a.授课计划明细编号!='"+MyTools.fixSql(skjhid)+"'";
		if(!BJMC_CX.equals("")){
			sql+=" and a.行政班名称 like '%"+MyTools.fixSql(BJMC_CX)+"%'";
		}
		if(!KCMC_CX.equals("")){
			sql+=" and a.课程名称 like '%"+MyTools.fixSql(KCMC_CX)+"%'";
		}
		if(!ZYDM_CX.equals("")){
			sql+=" and a.专业代码 = '"+MyTools.fixSql(ZYDM_CX)+"'";
		}
		sql+=" order by a.课程代码 ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 设置同时开考
	 * @date:2016-04-06
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector listtskc(int pageNum ,int pageSize,String examid) throws SQLException {
		String sql = "";
		Vector vec = null;

		String ksccid=examid.substring(4, examid.length());

		sql="select a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,a.专业代码,a.专业名称,a.场地要求,a.上课周期,a.考试场次编号,a.期中期末,a.学年学期编码,a.行政班代码,a.行政班名称,b.考试形式,a.监考教师,a.参考学生 " +
			"from dbo.V_考试管理_考试设置 a,dbo.V_考试形式 b where a.考试形式=b.编号 and a.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) +"' and a.期中期末='" + MyTools.fixSql(this.getQZQM()) + "' " +
			" and a.考试场次编号  in (select [考试场次编号] from [V_考试管理_考试设置] where [授课计划明细编号]='"+MyTools.fixSql(ksccid)+"') ";
		
		sql+=" order by a.课程代码 ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 
	 * @date:2016-04-06
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector listckxs(int pageNum ,int pageSize,String examid) throws SQLException {
		String sql = "";
		Vector vec = null;

		String ksccid=examid.substring(4, examid.length());
		sql=" select a.授课计划明细编号,a.行政班代码,c.行政班名称 ,a.学号,b.姓名  from  dbo.V_考试管理_参考学生 a,dbo.V_学生基本数据子类 b ,dbo.V_学校班级数据子类 c " +
			" where a.学号=b.学号 and a.行政班代码=c.行政班代码 and a.授课计划明细编号='"+MyTools.fixSql(ksccid)+"' ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 
	 * @date:2016-04-07
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void saveselect(String skid,String examid,String delexamid) throws SQLException {
		String sql = "";
		String sql2 = "";
		Vector vec = null;
		Vector vec2 = null;
		String sqldel="";
		
		String[] skjhid=skid.split(",");
		String sqlsk="";
		for(int i=0;i<skjhid.length;i++){
			sqlsk+="'"+skjhid[i]+"',";
		}
		sqlsk=sqlsk.substring(0,sqlsk.length()-1);
		examid=examid.substring(4, examid.length());
		
		if(!delexamid.equals("")){
			String[] delexaminfo=delexamid.split(",");
			
			for(int j=0;j<delexaminfo.length;j++){
				sqldel+="'"+delexaminfo[j]+"',";
			}
			sqldel=sqldel.substring(0,sqldel.length()-1);	
		}else{
			sqldel="''";
		}
		
		sql="select 考试场次编号,试卷类型  from V_考试管理_考试设置 where 授课计划明细编号 = '"+MyTools.fixSql(examid)+"'";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			sql2="update V_考试管理_考试设置 set 考试场次编号='"+vec.get(0).toString()+"',试卷类型='"+vec.get(1).toString()+"'  where 授课计划明细编号 in ("+sqlsk+") ";
			sql2+=" update dbo.V_考试管理_考试设置 set 考试场次编号='SKJHMX'+substring(授课计划明细编号,7,LEN(授课计划明细编号)),试卷类型='' where 授课计划明细编号 in ("+sqldel+") ";
			if(db.executeInsertOrUpdate(sql2)){
				 this.setMSG("保存成功");
			 }else{
				 this.setMSG("保存失败");
			 }	
		}
		
	}
	
	/**
	 * 
	 * @date:2016-04-07
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void delselect(String skjhid) throws SQLException {
		String sql = "";
		Vector vec = null;
		sql="update V_考试管理_考试设置 set 考试场次编号=substring(考试场次编号,0,charindex('_',考试场次编号)+1)+substring(授课计划明细编号,charindex('_',授课计划明细编号)+1,len(授课计划明细编号)) where 授课计划明细编号 in ("+skjhid+") ";

		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}	
	}
	
	
	/**
	 * 
	 * @date:2016-05-25
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector listkcap(int pageNum ,int pageSize,String xnxq,String jxxz) throws SQLException {
		String sql = "";
		Vector vec = null;

		sql=" SELECT distinct a.考场安排主表编号,substring(a.学年学期编码,1,5) as ex_xnxq,substring(a.学年学期编码,6,2) as ex_jxxz,b.学年学期名称,a.考试名称 as ex_ksmc,a.考试类型 as ex_kslx FROM dbo.V_考试管理_考场安排主表 a,dbo.V_规则管理_学年学期表 b " +
			"where a.学年学期编码=b.学年学期编码 and a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' order by a.考场安排主表编号 desc ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 
	 * @date:2017-11-30
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridExamCourse(int pageNum ,int pageSize) throws SQLException {
		String sql = "";
		Vector vec = null;
	
		sql="SELECT distinct 考场安排明细编号,课程代码,课程名称 from dbo.V_考试管理_考场安排明细表 " +
			" where 考场安排主表编号='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [行政班代码]='"+MyTools.fixSql(this.getXZBDM())+"' " +
			" order by 课程代码 ";
	
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 
	 * @date:2017-11-30
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridExamDate(int pageNum ,int pageSize) throws SQLException {
		String sql = "";
		String sql2 = "";
		Vector vec = null;
		Vector vec2 = null;
		String kskc="";
		
		if(this.getXZBDM().equals("")) {
			sql="SELECT [考试日期明细编号],[考场安排主表编号],[考试日期],[开始时间],[结束时间] FROM [dbo].[V_考试管理_考试日期] " +
				" where 1=2 ";
		}else {
			sql="SELECT [考试日期明细编号],[考场安排主表编号],[考试日期],[开始时间],[结束时间] FROM [dbo].[V_考试管理_考试日期] "
				+ "where 考场安排主表编号='"+MyTools.fixSql(this.getKCAPZBBH())+"' order by 考试日期,开始时间,结束时间 ";
			
			sql2="SELECT [考试场次编号],考场安排明细编号,课程名称,[监考教师人数] from [V_考试管理_考场安排明细表] " + 
				"where 考场安排主表编号='"+MyTools.fixSql(this.getKCAPZBBH())+"' and 行政班代码='"+MyTools.fixSql(this.getXZBDM())+"' and [考试场次编号]!='' ";
			vec2=db.GetContextVector(sql2);
			if(vec2!=null&&vec2.size()>0) {
				for(int i=0;i<vec2.size();i++) {
					kskc+=vec2.get(i).toString()+",";
				}
				if(!kskc.equals("")) {
					kskc=kskc.substring(0, kskc.length()-1);
				}
				this.setMSG(kskc);
			}
		}
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 显示考试形式
	 * @date:2018-01-02
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridDept() throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="SELECT [DeptCode],[CName] FROM [dbo].[sysDepartment] " + 
			" where [type]='D' and [state]='Y' ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 
	 * @date:2017-11-30
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void importDeptTea(String deptarray) throws SQLException {
		String sql = "";
		String sql2 = "";
		String sqlbzr="";
		String sqlchu="";
		String sqlrkjs="";
		Vector vec = null;
		Vector vec2 = new Vector();
		Vector vectea = new Vector();
		Vector vecbzr = null;
		Vector vecchu = null;
		Vector vecrkjs = null;
			
		int nj=Integer.parseInt(this.getXNXQ().substring(2,4));//年级码
		sql="delete from V_考试管理_监考教师 where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' ";
		if(db.executeInsertOrUpdate(sql)) {//插入教师信息
			//添加所有教职工
			sql="insert into dbo.V_考试管理_监考教师  " + 
				"SELECT '"+MyTools.fixSql(this.getKCAPZBBH())+"' as 考场安排主表编号,工号,姓名,'' as [教师类型编号],'' as [教师类型名称],'0' as 最大监考次数,'0' as 是否任课,'0' as [已安排监考次数],'' as [已安排监考时间]  FROM [dbo].[V_教职工基本数据子类] " + 
				" where [是否有效]='1' ";			
			db.executeInsertOrUpdate(sql);	
			
			//查询所有教师
			String sqltea="SELECT [考场安排主表编号],[监考教师编号],[监考教师姓名],[教师类型编号],[教师类型名称],[最大监考次数],[是否任课] FROM [dbo].[V_考试管理_监考教师] where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' ";
			vectea=db.GetContextVector(sqltea);
			
			//查询班主任
			sqlbzr="SELECT [班主任工号] FROM [dbo].[V_基础信息_班级信息表] " + 
					" where 年级代码 in ('"+(nj+"1")+"','"+((nj-1)+"1")+"','"+((nj-2)+"1")+"') and [班主任工号]!='' ";
			vecbzr=db.GetContextVector(sqlbzr);
			if(vecbzr!=null&&vecbzr.size()>0) {
				for(int i=0;i<vecbzr.size();i++) {
					//sql2="update dbo.V_考试管理_监考教师 set [是否任课]='2',[教师类型编号]='02',[教师类型名称]='班主任' where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [监考教师编号]='"+vecbzr.get(i).toString()+"' ";
					//vec2.add(sql2);
					for(int t=0;t<vectea.size();t=t+7) {
						if(vecbzr.get(i).toString().equals(vectea.get(t+1).toString())) {
							vectea.setElementAt("01", t+3);
							vectea.setElementAt("班主任", t+4);
							vectea.setElementAt("2", t+6);
						}
					}
				}
			}
			
			//查询教导处，教学处教师
			String sqldept="";
			String deptname="";
			if(deptarray.indexOf(",")>-1) {
				String[] deptTea=deptarray.split(",");
				for(int i=0;i<deptTea.length;i++) {
					sqldept+="'"+deptTea[i]+"',";
					deptname+=deptTea[i]+"+";
				}
				if(!sqldept.equals("")) {
					sqldept=sqldept.substring(0,sqldept.length()-1);
					deptname=deptname.substring(0,deptname.length()-1);
				}
			}else {
				sqldept="'"+deptarray+"'";
				deptname=deptarray;
			}
			

			sqlchu="SELECT distinct a.[UserCode],a.[UserName] FROM [dbo].[sysUserinfo] a " + 
					"  left join dbo.sysUserDept b on a.UserCode=b.UserCode " + 
					"  left join dbo.sysDepartment c on b.DeptCode=c.DeptCode " + 
					"  where c.type='D' and c.state='Y' and c.CName in ("+sqldept+")";
			vecchu=db.GetContextVector(sqlchu);
			if(vecchu!=null&&vecchu.size()>0) {
				for(int i=0;i<vecchu.size();i=i+2) {
					//sql2="update dbo.V_考试管理_监考教师 set [是否任课]='2',[教师类型编号]='03',[教师类型名称]='教学处' where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [监考教师编号]='"+vecchu.get(i).toString()+"' ";
					//vec2.add(sql2);
					for(int t=0;t<vectea.size();t=t+7) {
						if(vecchu.get(i).toString().equals(vectea.get(t+1).toString())) {
							if(vectea.get(t+3).toString().equals("")) {
								vectea.setElementAt("02", t+3);							
								vectea.setElementAt(deptname, t+4);
							}else {
								vectea.setElementAt(vectea.get(t+3).toString()+"，02", t+3);							
								vectea.setElementAt(vectea.get(t+4).toString()+"，"+deptname, t+4);
							}
							vectea.setElementAt("2", t+6);
						}
					}
				}
			}
			
			//查询任课教师
			sqlrkjs="SELECT distinct [授课教师编号],[授课教师姓名] FROM [dbo].[V_排课管理_课程表周详情表] " + 
					" where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [授课教师编号]!='' and charIndex('+',授课教师编号)=0 ";
			vecrkjs=db.GetContextVector(sqlrkjs);
			if(vecrkjs!=null&&vecrkjs.size()>0) {
				String sqlrkjs2="SELECT distinct [授课教师编号],[授课教师姓名] FROM [dbo].[V_排课管理_课程表周详情表] " + 
						" where [学年学期编码]='"+MyTools.fixSql(this.getXNXQ())+"' and [授课教师编号]!='' and charIndex('+',授课教师编号)>0 ";
				Vector vecrkjs2=db.GetContextVector(sqlrkjs2);
				if(vecrkjs2!=null&&vecrkjs2.size()>0) {
					for(int i=0;i<vecrkjs2.size();i=i+2) {
						String[] teaid=vecrkjs2.get(i).toString().split("\\+");
						String[] teaxm=vecrkjs2.get(i+1).toString().split("\\+");
						int flagtea=-1;
						for(int j=0;j<teaid.length;j++) {
							flagtea=0;
							for(int t=0;t<vecrkjs.size();t=t+2) {
								if(teaid[j].equals(vecrkjs.get(t).toString())) {
									flagtea=1;
								}
							}
							if(flagtea==0) {
								vecrkjs.add(teaid[j]);
								vecrkjs.add(teaxm[j]);
							}
						}				
					}
				}
			}
				
			for(int i=0;i<vecrkjs.size();i=i+2) {
				for(int t=0;t<vectea.size();t=t+7) {
					if(vecrkjs.get(i).toString().equals(vectea.get(t+1).toString())) {
						if(vectea.get(t+3).toString().equals("")) {
							vectea.setElementAt("03", t+3);
							vectea.setElementAt("任课教师", t+4);
						}else {
							vectea.setElementAt(vectea.get(t+3).toString()+"，03", t+3);							
							vectea.setElementAt(vectea.get(t+4).toString()+"，任课教师", t+4);
						}
						vectea.setElementAt("1", t+6);
					}
				}
			}
			
			for(int i=0;i<vectea.size();i=i+7) {
				if(!vectea.get(i+6).toString().equals("0")) {
					sql2="update dbo.V_考试管理_监考教师 set [是否任课]='"+vectea.get(i+6).toString()+"',[教师类型编号]='"+vectea.get(i+3).toString()+"',[教师类型名称]='"+vectea.get(i+4).toString()+"' where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [监考教师编号]='"+vectea.get(i+1).toString()+"' ";
					vec2.add(sql2);
				}
			}
			
			if(db.executeInsertOrUpdateTransaction(vec2)) {
				this.setMSG("导入成功，请设置监考教师最大监考次数");
			}else {
				this.setMSG("导入失败");
			}
		}

	}
	
	/**
	 * 
	 * @date:2017-11-30
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridExamTeacher(int pageNum ,int pageSize,String teabh,String teaname,String tealx) throws SQLException {
		String sql = "";
		Vector vec = null;
			
		sql="select [监考教师编号],[监考教师姓名],[教师类型编号],[教师类型名称],[最大监考次数] from V_考试管理_监考教师 "
			+ " where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and 是否任课!='0' ";
		if(!teabh.equals("")) {
			sql+=" and [监考教师编号] like '%"+teabh+"%' ";
		}
		if(!teaname.equals("")) {
			sql+=" and [监考教师姓名] like '%"+teaname+"%' ";
		}
		if(!tealx.equals("")) {
			sql+=" and [教师类型编号] like '%"+tealx+"%' ";
		}
		sql+=" order by [监考教师编号] ";
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 保存最大监考次数
	 * @date:2017-12-01
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveTeaNum(String ksjsarray,String jkcs) throws WrongSQLException, SQLException {
		String sql = "";
		Vector vec=new Vector();
		String[] jsbh=ksjsarray.split(",");
		
		for(int i=0;i<jsbh.length;i++) {
			sql = "update V_考试管理_监考教师 set " +
				"[最大监考次数]='"+MyTools.fixSql(jkcs)+"' " +
				"where [考场安排主表编号]='"+MyTools.StrFiltr(MyTools.fixSql(this.getKCAPZBBH()))+"' and [监考教师编号]='"+MyTools.fixSql(jsbh[i])+"' ";
			vec.add(sql);
		}
		
			 
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 
	 * @date:2017-07-07
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void searchKSLX(String ex_kszbbh,String ex_xbdm) throws SQLException {
		String sql = "";
		Vector vec = null;
		String sqlxb="";
		//查询考试类型
		sql="select [考试类型] from [dbo].[V_考试管理_考场安排主表] where [考场安排主表编号]='"+MyTools.fixSql(ex_kszbbh)+"' ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			this.setMSG(vec.get(0).toString());
		}
		
		if(ex_xbdm.equals("all")) {
			sqlxb="";
		}else if(ex_xbdm.equals("C01")) {
			sqlxb=" and [行政班代码] in (select 班级编号 from dbo.V_基础信息_班级信息表 where 系部代码  in ('C01','C02') ) ";
		}else {
			sqlxb=" and [行政班代码] in (select 班级编号 from dbo.V_基础信息_班级信息表 where 系部代码  in ('"+ex_xbdm+"') ) ";
		}
		
		//判断有没有排过考试
		sql="select count(*) from [dbo].[V_考试管理_考场安排明细表] where [考场安排主表编号]='"+MyTools.fixSql(ex_kszbbh)+"' and [监考教师编号]!='' " + sqlxb ;
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			if(vec.get(0).toString().equals("0")){
				this.setMSG2("0");
			}else{
				this.setMSG2("1");
			}		
		}	
	}
	
	/**
	 * 自动排监考教师
	 * @date:2017-12-06
	 * @author:lupengfei
	 * @param qzqm 考试主表编号
	 * @return String 课程表
	 * @throws SQLException
	 */
	public void autoAreaArrange(String xnxqbm, String qzqm, String xbdm) throws SQLException{
		HttpSession session = request.getSession();
		String sql="";
		String sqlupd="";
		String sqltime="";
		Vector vec=new Vector(); 
		Vector vecupd=new Vector();
		Vector vectime=new Vector(); 
		int rkjsflag=0;//是否任课教师标记
		String wpks="";//未排考试
		String tsxx="";//提示信息
		Vector vectea=new Vector();
		
		//根据日期排时间序列
		String sqlrq="select distinct [考试日期],'' as rqxl from [V_考试管理_考试日期] where [考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' order by [考试日期] ";
		Vector vecrq=db.GetContextVector(sqlrq);
		if(vecrq!=null&&vecrq.size()>0) {
			for(int rq=0;rq<vecrq.size();rq=rq+2) {
				String rqxl="";
				if((rq/2)<10) {
					rqxl="0"+(rq/2+1);
				}else {
					rqxl=(rq/2+1)+"";
				}
				vecrq.setElementAt(rqxl, rq+1);
			}
		}
		
		String sqlsj="select distinct [开始时间],[结束时间],'' as sjxl from [V_考试管理_考试日期] where [考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' order by [开始时间],[结束时间] ";
		Vector vecsj=db.GetContextVector(sqlsj);
		if(vecsj!=null&&vecsj.size()>0) {
			for(int sj=0;sj<vecsj.size();sj=sj+3) {
				String sjxl="";
				if((sj/3)<10) {
					sjxl="0"+(sj/3+1);
				}else {
					sjxl=(sj/3+1)+"";
				}
				vecsj.setElementAt(sjxl, sj+2);
			}
		}
		
		String sqlxl="select a.[考场安排明细编号],a.[考试场次编号],b.考试日期,b.开始时间,b.结束时间  FROM [dbo].[V_考试管理_考场安排明细表] a " + 
				"  left join dbo.V_考试管理_考试日期 b on a.考试场次编号=b.考试日期明细编号 where a.[考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' and a.考试场次编号!='' ";
		Vector vecxl=db.GetContextVector(sqlxl);
		if(vecxl!=null&vecxl.size()>0) {
			String sqlxbdm="";
			if(xbdm.equals("C01")) {
				sqlxbdm="'C01','C02'";
			}else {
				sqlxbdm="'"+xbdm+"'";
			}
			
			for(int xl=0;xl<vecxl.size();xl=xl+5) {
				String timexl="";
				//考试日期相同
				for(int rq=0;rq<vecrq.size();rq=rq+2) {
					if(vecrq.get(rq).toString().equals(vecxl.get(xl+2).toString())) {
						timexl=vecrq.get(rq+1).toString();
						break;
					}
				}
				//开始、结束时间相同
				for(int sj=0;sj<vecsj.size();sj=sj+3) {
					if(vecsj.get(sj).toString().equals(vecxl.get(xl+3).toString())&&vecsj.get(sj+1).toString().equals(vecxl.get(xl+4).toString())) {
						timexl+=vecsj.get(sj+2).toString();
						break;
					}
				}
				sqltime="update [V_考试管理_考场安排明细表] set [时间序列]='"+timexl+"' where [考场安排明细编号]='"+vecxl.get(xl).toString()+"' " +
						" and [行政班代码] in (select 班级编号 from dbo.V_基础信息_班级信息表 where 系部代码  in ("+sqlxbdm+")) " ;
				vectime.add(sqltime);

			}
			//删除原有的监考教师
			sqltime="update [V_考试管理_考场安排明细表] set [监考教师编号]='',[监考教师姓名]='' where [考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' " + 
					" and [行政班代码] in (select 班级编号 from dbo.V_基础信息_班级信息表 where 系部代码  in ("+sqlxbdm+")) " ;
			vectime.add(sqltime);
			db.executeInsertOrUpdateTransaction(vectime);
		}
		
		//国商部，旅游部一起排
		if(xbdm.equals("C01")) {
			//查询系部所属教师
			String sqlxb="SELECT [系部代码],[系部名称] FROM [dbo].[V_基础信息_系部信息表] where [系部代码]!='C00' order by [排序] ";
			Vector vecxb=db.GetContextVector(sqlxb);
				
			//查询C01，C02所有任课教师
			String teaid="";		
			String sqlc12="SELECT distinct [授课教师编号] FROM [dbo].[V_排课管理_课程表周详情表] a " + 
					" left join  dbo.V_基础信息_班级信息表 b on a.行政班代码=b.班级编号 " + 
					" where a.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and a.授课计划明细编号!='' and b.系部代码 in ('C01','C02')";
			Vector vecc12=db.GetContextVector(sqlc12);
			if(vecc12!=null&&vecc12.size()>0) {
				for(int i=0;i<vecc12.size();i++) {
					if(vecc12.get(i).toString().indexOf("+")>-1) {
						String[] teabh=vecc12.get(i).toString().split("\\+");
						for(int j=0;j<teabh.length;j++) {
							teaid+="'"+teabh[j]+"',";
						}
					}else {
						teaid+="'"+vecc12.get(i).toString()+"',";
					}
				}
				if(!teaid.equals("")) {
					teaid=teaid.substring(0, teaid.length()-1);
				}else {
					teaid="''";
				}
			}
			
			Vector vecjs=new Vector();
			int flagjs=-1;
			int jsnum=-1;
			String sqljs1="SELECT distinct a.[工号],a.[姓名],b.[系部代码],c.最大监考次数,c.[是否任课] as 任课教师,'0' as 已安排监考次数,'' as 已安排监考时间 FROM [dbo].[V_教职工基本数据子类] a " + 
					" left join [V_基础信息_系部教师信息表] b on a.工号=b.教师编号 " + 
					" left join dbo.V_考试管理_监考教师 c on a.工号=c.监考教师编号 " + 
					" where a.[是否有效]='1' and b.系部代码!='' and b.系部代码 in ('C01','C02','C00') and c.考场安排主表编号='"+MyTools.fixSql(qzqm)+"' and c.[是否任课]='1' and a.工号 in ("+teaid+") " +
					" order by a.[工号],b.系部代码 ";
			Vector vecjs1=db.GetContextVector(sqljs1);
			if(vecjs1!=null&&vecjs1.size()>0) {
				//添加到vecjs,有2条记录的用C01，C02替换C00
				for(int i=0;i<vecjs1.size();i=i+7){
					flagjs=0;
					jsnum=-1;
					for(int j=0;j<vecjs.size();j=j+7) {
						if(vecjs1.get(i).toString().equals(vecjs.get(j).toString())) {
							flagjs=1;
							jsnum=j;
						}
					}
					if(flagjs==1) {
						vecjs.setElementAt(vecjs1.get(i+2).toString(), jsnum+2);
					}else {
						vecjs.add(vecjs1.get(i).toString());
						vecjs.add(vecjs1.get(i+1).toString());
						vecjs.add(vecjs1.get(i+2).toString());
						vecjs.add(vecjs1.get(i+3).toString());
						vecjs.add(vecjs1.get(i+4).toString());
						vecjs.add(vecjs1.get(i+5).toString());
						vecjs.add(vecjs1.get(i+6).toString());
					}
				}
			}
			
			String sqljs2="SELECT distinct a.[工号],a.[姓名],b.[系部代码],c.最大监考次数,c.[是否任课] as 任课教师,'0' as 已安排监考次数,'' as 已安排监考时间 FROM [dbo].[V_教职工基本数据子类] a " + 
					" left join [V_基础信息_系部教师信息表] b on a.工号=b.教师编号 " + 
					" left join dbo.V_考试管理_监考教师 c on a.工号=c.监考教师编号 " + 
					" where a.[是否有效]='1' and b.系部代码!='' and b.系部代码 in ('C01','C02') and c.考场安排主表编号='"+MyTools.fixSql(qzqm)+"' and c.[是否任课]='2' " +
					" order by a.[工号],b.系部代码 ";
			Vector vecjs2=db.GetContextVector(sqljs2);
			if(vecjs2!=null&&vecjs2.size()>0) {
				//添加到vecjs	
				for(int i=0;i<vecjs2.size();i=i+7){
					vecjs.add(vecjs2.get(i).toString());
					vecjs.add(vecjs2.get(i+1).toString());
					vecjs.add(vecjs2.get(i+2).toString());
					vecjs.add(vecjs2.get(i+3).toString());
					vecjs.add(vecjs2.get(i+4).toString());
					vecjs.add(vecjs2.get(i+5).toString());
					vecjs.add(vecjs2.get(i+6).toString());
				}
			}
			
//			System.out.println("sizejs:--"+vecjs.size());
//			for(int i=0;i<vecjs.size();i=i+7){
//				System.out.println(vecjs.get(i)+" | "+vecjs.get(i+1)+" | "+vecjs.get(i+2)+" | "+vecjs.get(i+3)+" | "+vecjs.get(i+4)+" | "+vecjs.get(i+5)+" | "+vecjs.get(i+6));
//			}
	
			//排监考教师----------------------------------------------------------
			
			//查询所有考试信息
			sql="SELECT a.[考场安排明细编号],a.[考场安排主表编号],a.[时间序列],a.[课程代码],a.[课程名称],a.[专业代码],a.[行政班代码],a.[行政班名称],a.[监考教师编号],a.[监考教师姓名],a.[学生人数],b.系部代码,b.班主任工号,a.[监考教师人数] " + 
				" FROM [dbo].[V_考试管理_考场安排明细表] a left join dbo.V_基础信息_班级信息表 b on a.行政班代码=b.班级编号 " + 
				" where a.[考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' and  a.[场地类型]='1' " +
				" and a.[行政班代码] in (select 班级编号 from dbo.V_基础信息_班级信息表 where 系部代码  in ('C01','C02')) " +
				" order by b.系部代码,a.[行政班代码] ";
			vec=db.GetContextVector(sql);
			
			if(vec!=null&&vec.size()>0){
				int teaflag=0;//是否可以排标记
				int usetea=0;//教师已经排次数
				
				//排任课教师 1
				for(int v=0;v<vec.size();v=v+14){							
					//查询该班级任课教师
					Vector vecbjrk=new Vector();//保存当前班级的任课教师 
					int teasameflag=-1;
					String sqlbj="SELECT distinct [授课教师编号],[授课教师姓名],[课程代码],[课程名称] FROM [dbo].[V_排课管理_课程表周详情表] " + 
							"  where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and [授课计划明细编号]!='' and 行政班代码='"+vec.get(v+6).toString()+"' and 行政班名称='"+vec.get(v+7).toString()+"' ";
					Vector vecbj=db.GetContextVector(sqlbj);
					if(vecbj!=null&&vecbj.size()>0) {
						for(int k=0;k<vecbj.size();k=k+4) {
							if(vecbj.indexOf("+")>-1) {//有多个教师
								String[] teabh=vecbj.get(k).toString().split("\\+");
								String[] teaname=vecbj.get(k+1).toString().split("\\+");
								for(int l=0;l<teabh.length;l++) {
									vecbj.add(teabh[l]);
									vecbj.add(teaname[l]);
									vecbj.add(vecbj.get(k+2).toString());
									vecbj.add(vecbj.get(k+3).toString());
								}
							}
						}
	
						for(int k=0;k<vecbj.size();k=k+4) {
							teasameflag=0;
							for(int l=0;l<vecbjrk.size();l++) {
								if(vecbj.get(k).toString().equals(vecbjrk.get(l).toString())&&vecbj.get(k+1).toString().equals(vecbjrk.get(l+1).toString())) {//教师相同
									if(vecbjrk.get(l+2).toString().indexOf(vecbj.get(k+2).toString())>-1 && vecbjrk.get(l+3).toString().indexOf(vecbj.get(k+3).toString())>-1) {//任课课程相同
										teasameflag=1;
									}else {//添加课程到任课课程
										teasameflag=1;
										vecbjrk.setElementAt(vecbjrk.get(l+2).toString()+","+vecbj.get(k+2).toString(), l+2);
										vecbjrk.setElementAt(vecbjrk.get(l+3).toString()+","+vecbj.get(k+3).toString(), l+3);
									}
								}
							}
							if(teasameflag==0) {
								vecbjrk.add(vecbj.get(k).toString());
								vecbjrk.add(vecbj.get(k+1).toString());
								vecbjrk.add(vecbj.get(k+2).toString());
								vecbjrk.add(vecbj.get(k+3).toString());
							}
						}
					}
					
//					for(int i=0;i<vecbjrk.size();i=i+4){
//						System.out.println(vecbjrk.get(i)+" | "+vecbjrk.get(i+1)+" | "+vecbjrk.get(i+2)+" | "+vecbjrk.get(i+3));
//					}
					
					//vectea按已安排监考次数排序
					vectea=new Vector();
					int min=100;
					int minnum=-1;
	
					while(vecjs.size()>0){
						for(int j=0;j<vecjs.size();j=j+7){
							if(Integer.parseInt(vecjs.get(j+5).toString())<min){
								min=Integer.parseInt(vecjs.get(j+5).toString());
								minnum=j;
							}
						}
						vectea.add(vecjs.get(minnum).toString());//教师编号 1
						vectea.add(vecjs.get(minnum+1).toString());//教师姓名 2
						vectea.add(vecjs.get(minnum+2).toString());//系部 3				
						vectea.add(vecjs.get(minnum+3).toString());//最大监考次数 4
						vectea.add(vecjs.get(minnum+4).toString());//任课教师 5
						vectea.add(vecjs.get(minnum+5).toString());//已安排监考次数 6
						vectea.add(vecjs.get(minnum+6).toString());//已安排监考时间 7
						for(int k=0;k<7;k++){
							vecjs.remove(minnum);
						}
						min=100;
						minnum=-1;
					};			
			
//					if(v<364) {
//						System.out.println("sizetea:--"+vectea.size());
//						for(int i=0;i<vectea.size();i=i+7){
//							System.out.println(v+"|"+vectea.get(i)+" | "+vectea.get(i+1)+" | "+vectea.get(i+2)+" | "+vectea.get(i+3)+" | "+vectea.get(i+4)+" | "+vectea.get(i+5)+" | "+vectea.get(i+6));
//						}
//					}
					
					int bjrknum=0;
					String rkkcbh="";//任课课程编号
					String rkkcmc="";//任课课程名称
					String jsbh="";
					String jsxm="";
					String xb="";
					String rkjs="";//1 是 2否
					String apjkcs="";
					String zdjkcs="";
					String teasjxl="";
					
					do {		
						//分配监考教师
						jsbh=vectea.get(bjrknum).toString();
						jsxm=vectea.get(bjrknum+1).toString();
						xb=vectea.get(bjrknum+2).toString();
						zdjkcs=vectea.get(bjrknum+3).toString();
						rkjs=vectea.get(bjrknum+4).toString();
						apjkcs=vectea.get(bjrknum+5).toString();				
						teasjxl=vectea.get(bjrknum+6).toString();
						rkkcbh="";
						rkkcmc="";
						
						if(!vec.get(v+11).toString().equals(xb)&&!vec.get(v+11).toString().equals("C00")){//不是这个系部的,也不是C00
							teaflag=1;//不能排
						}else{
							if(rkjs.equals("2")) {//不是任课教师
								teaflag=2;
							}else {
								if(Integer.parseInt(apjkcs)>=Integer.parseInt(zdjkcs)) {//已安排监考次数=最大监考次数
									teaflag=3;
								}else {
									if(vec.get(v+12).toString().equals(jsbh)) {//是班主任
										teaflag=4;
									}else {						
										for(int i=0;i<vecbjrk.size();i=i+4) {
											if(vecbjrk.get(i).toString().equals(jsbh)) {//是该班任课教师，获取教什么课程
												rkkcbh=vecbjrk.get(i+2).toString();
												rkkcmc=vecbjrk.get(i+3).toString();
											}
										}
										if(rkkcbh.equals("")||(rkkcbh.indexOf(vec.get(v+3).toString())>-1&&rkkcmc.indexOf(vec.get(v+4).toString())>-1)) {//任课老师是教这门课的
											teaflag=5;
										}else {
											if(teasjxl.indexOf(vec.get(v+2).toString())>-1) {//时间序列冲突
												teaflag=6;
											}else {//可以分配
												teaflag=0;
												vec.setElementAt(jsbh, v+8);
												vec.setElementAt(jsxm, v+9);
														
												String editsjxl="";
												if(teasjxl.equals("")) {
													editsjxl=vec.get(v+2).toString();
												}else {
													editsjxl=teasjxl+","+vec.get(v+2).toString();
												}
												vectea.setElementAt(Integer.parseInt(apjkcs)+1, bjrknum+5);
												vectea.setElementAt(editsjxl, bjrknum+6);
														
												for(int t=0;t<vectea.size();t=t+7) {
													vecjs.add(vectea.get(t).toString());//教师编号 1
													vecjs.add(vectea.get(t+1).toString());//教师姓名 2
													vecjs.add(vectea.get(t+2).toString());//系部 3				
													vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
													vecjs.add(vectea.get(t+4).toString());//任课教师 5
													vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
													vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
												}
												break;
											}
										}	
									}	
								}	
							}	
						}					
						
						bjrknum=bjrknum+7;
					}while(teaflag!=0&&bjrknum<vectea.size());
					//System.out.println("vec:"+vec.get(v).toString()+"|"+teaflag+"|"+bjrknum);
					if(teaflag!=0&&bjrknum==vectea.size()) {//未安排教师，允许用国商部教师
						for(int t=0;t<vectea.size();t=t+7) {
							vecjs.add(vectea.get(t).toString());//教师编号 1
							vecjs.add(vectea.get(t+1).toString());//教师姓名 2
							vecjs.add(vectea.get(t+2).toString());//系部 3				
							vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
							vecjs.add(vectea.get(t+4).toString());//任课教师 5
							vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
							vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
						}
						
						//教师排序 C02任课教师-C01是C02班级的任课教师-C02非任课教师-C01非任课教师-C01剩余教师
						//[工号],[姓名],[系部代码],[最大监考次数],[是否任课] as 任课教师,'0' as 已安排监考次数,'' as 已安排监考时间

//						if(v<364) {//325-338
//							System.out.println("sizejs:--"+vecjs.size());
//							for(int i=0;i<vecjs.size();i=i+7){
//								System.out.println(v+" js| "+vecjs.get(i)+" | "+vecjs.get(i+1)+" | "+vecjs.get(i+2)+" | "+vecjs.get(i+3)+" | "+vecjs.get(i+4)+" | "+vecjs.get(i+5)+" | "+vecjs.get(i+6));
//							}
//						}
						
						vectea=new Vector();
						
						//C02任课教师
						for(int i=0;i<vecjs.size();i=i+7){
							if(vecjs.get(i+2).toString().equals("C02")&&vecjs.get(i+4).toString().equals("1")) {
								vectea.add(vecjs.get(i).toString());//教师编号 1
								vectea.add(vecjs.get(i+1).toString());//教师姓名 2
								vectea.add(vecjs.get(i+2).toString());//系部 3				
								vectea.add(vecjs.get(i+3).toString());//最大监考次数 4
								vectea.add(vecjs.get(i+4).toString());//任课教师 5
								vectea.add(vecjs.get(i+5).toString());//已安排监考次数 6
								vectea.add(vecjs.get(i+6).toString());//已安排监考时间 7
								for(int k=0;k<7;k++){
									vecjs.remove(i);
								}
								i=i-7;
							}
						}
						
						//C01是C02班级的任课教师
						for(int i=0;i<vecjs.size();i=i+7){
							if(vecjs.get(i+2).toString().equals("C01")&&vecjs.get(i+4).toString().equals("1")) {
								for(int j=0;j<vecbjrk.size();j++) {
									if(vecjs.get(i).toString().equals(vecbjrk.get(j).toString())) {
										vectea.add(vecjs.get(i).toString());//教师编号 1
										vectea.add(vecjs.get(i+1).toString());//教师姓名 2
										vectea.add(vecjs.get(i+2).toString());//系部 3				
										vectea.add(vecjs.get(i+3).toString());//最大监考次数 4
										vectea.add(vecjs.get(i+4).toString());//任课教师 5
										vectea.add(vecjs.get(i+5).toString());//已安排监考次数 6
										vectea.add(vecjs.get(i+6).toString());//已安排监考时间 7
										for(int k=0;k<7;k++){
											vecjs.remove(i);
										}	
										i=i-7;
										break;
									}
								}		
							}
						}
						
						//C02非任课教师
						for(int i=0;i<vecjs.size();i=i+7){
							if(vecjs.get(i+2).toString().equals("C02")&&vecjs.get(i+4).toString().equals("2")) {
								vectea.add(vecjs.get(i).toString());//教师编号 1
								vectea.add(vecjs.get(i+1).toString());//教师姓名 2
								vectea.add(vecjs.get(i+2).toString());//系部 3				
								vectea.add(vecjs.get(i+3).toString());//最大监考次数 4
								vectea.add(vecjs.get(i+4).toString());//任课教师 5
								vectea.add(vecjs.get(i+5).toString());//已安排监考次数 6
								vectea.add(vecjs.get(i+6).toString());//已安排监考时间 7
								for(int k=0;k<7;k++){
									vecjs.remove(i);
								}
								i=i-7;
							}
						}
						
						//C01非任课教师
						for(int i=0;i<vecjs.size();i=i+7){
							if(vecjs.get(i+2).toString().equals("C01")&&vecjs.get(i+4).toString().equals("2")) {
								vectea.add(vecjs.get(i).toString());//教师编号 1
								vectea.add(vecjs.get(i+1).toString());//教师姓名 2
								vectea.add(vecjs.get(i+2).toString());//系部 3				
								vectea.add(vecjs.get(i+3).toString());//最大监考次数 4
								vectea.add(vecjs.get(i+4).toString());//任课教师 5
								vectea.add(vecjs.get(i+5).toString());//已安排监考次数 6
								vectea.add(vecjs.get(i+6).toString());//已安排监考时间 7
								for(int k=0;k<7;k++){
									vecjs.remove(i);
								}
								i=i-7;
							}
						}
						
						//C01剩余教师
						for(int i=0;i<vecjs.size();i=i+7){
								vectea.add(vecjs.get(i).toString());//教师编号 1
								vectea.add(vecjs.get(i+1).toString());//教师姓名 2
								vectea.add(vecjs.get(i+2).toString());//系部 3				
								vectea.add(vecjs.get(i+3).toString());//最大监考次数 4
								vectea.add(vecjs.get(i+4).toString());//任课教师 5
								vectea.add(vecjs.get(i+5).toString());//已安排监考次数 6
								vectea.add(vecjs.get(i+6).toString());//已安排监考时间 7
						}
						vecjs=new Vector();
						
//						if(v<364) {
//							System.out.println("sizetea:--"+vectea.size());
//							for(int i=0;i<vectea.size();i=i+7){
//								System.out.println(v+" tea|"+vectea.get(i)+" | "+vectea.get(i+1)+" | "+vectea.get(i+2)+" | "+vectea.get(i+3)+" | "+vectea.get(i+4)+" | "+vectea.get(i+5)+" | "+vectea.get(i+6));
//							}
//						}
						
						//重新排一次
						bjrknum=0;	
						do {		
							//分配监考教师
							jsbh=vectea.get(bjrknum).toString();
							jsxm=vectea.get(bjrknum+1).toString();
							xb=vectea.get(bjrknum+2).toString();
							zdjkcs=vectea.get(bjrknum+3).toString();
							rkjs=vectea.get(bjrknum+4).toString();
							apjkcs=vectea.get(bjrknum+5).toString();				
							teasjxl=vectea.get(bjrknum+6).toString();
							rkkcbh="";
							rkkcmc="";
							
									if(Integer.parseInt(apjkcs)>=Integer.parseInt(zdjkcs)) {//已安排监考次数=最大监考次数
										teaflag=3;
									}else {
										if(vec.get(v+12).toString().equals(jsbh)) {//是班主任
											teaflag=4;
										}else {						
												if(teasjxl.indexOf(vec.get(v+2).toString())>-1) {//时间序列冲突
													teaflag=6;
												}else {
													for(int i=0;i<vecbjrk.size();i=i+4) {
														if(vecbjrk.get(i).toString().equals(jsbh)) {//是该班任课教师，获取教什么课程
															rkkcbh=vecbjrk.get(i+2).toString();
															rkkcmc=vecbjrk.get(i+3).toString();
														}
													}
													if(rkkcbh.indexOf(vec.get(v+3).toString())>-1&&rkkcmc.indexOf(vec.get(v+4).toString())>-1) {//任课老师是教这门课的
														teaflag=5;
													}else {//可以分配
														teaflag=0;
														vec.setElementAt(jsbh, v+8);
														vec.setElementAt(jsxm, v+9);
																
														String editsjxl="";
														if(teasjxl.equals("")) {
															editsjxl=vec.get(v+2).toString();
														}else {
															editsjxl=teasjxl+","+vec.get(v+2).toString();
														}
														vectea.setElementAt(Integer.parseInt(apjkcs)+1, bjrknum+5);
														vectea.setElementAt(editsjxl, bjrknum+6);
																
														for(int t=0;t<vectea.size();t=t+7) {
															vecjs.add(vectea.get(t).toString());//教师编号 1
															vecjs.add(vectea.get(t+1).toString());//教师姓名 2
															vecjs.add(vectea.get(t+2).toString());//系部 3				
															vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
															vecjs.add(vectea.get(t+4).toString());//任课教师 5
															vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
															vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
														}
														
														if(rkkcbh.equals("")){//可以分配，不是该班任课教师
															tsxx+=" <br/>"+vec.get(v+7).toString()+"  "+vec.get(v+4).toString();
														}else {//可以分配，是该班任课教师
															
														}
														break;
													}
												}		
										}	
									}	
												
							bjrknum=bjrknum+7;
						}while(teaflag!=0&&bjrknum<vectea.size());
						
						if(teaflag!=0&&bjrknum==vectea.size()) {//未安排教师
							for(int t=0;t<vectea.size();t=t+7) {
								vecjs.add(vectea.get(t).toString());//教师编号 1
								vecjs.add(vectea.get(t+1).toString());//教师姓名 2
								vecjs.add(vectea.get(t+2).toString());//系部 3				
								vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
								vecjs.add(vectea.get(t+4).toString());//任课教师 5
								vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
								vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
							}
							wpks+=" <br/>"+vec.get(v+7).toString()+"  "+vec.get(v+4).toString();
						}
					}
				}
									
				//排非任课教师 2		
				//学生人数<20的班级， 只需要一位监考老师	
				int jkjsrs=-1;
				for(int t=0;t<vectea.size();t=t+7) {
					vecjs.add(vectea.get(t).toString());//教师编号 1
					vecjs.add(vectea.get(t+1).toString());//教师姓名 2
					vecjs.add(vectea.get(t+2).toString());//系部 3				
					vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
					vecjs.add(vectea.get(t+4).toString());//任课教师 5
					vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
					vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
				}
				
				for(int v=0;v<vec.size();v=v+14){							
					jkjsrs=Integer.parseInt(vec.get(v+13).toString());
					
					for(int n=1;n<jkjsrs;n++) {
						//vectea按已安排监考次数排序
						vectea=new Vector();
						int min=100;
						int minnum=-1;
		
						while(vecjs.size()>0){
							for(int j=0;j<vecjs.size();j=j+7){
								if(Integer.parseInt(vecjs.get(j+5).toString())<min){
									min=Integer.parseInt(vecjs.get(j+5).toString());
									minnum=j;
								}
							}
							vectea.add(vecjs.get(minnum).toString());//教师编号 1
							vectea.add(vecjs.get(minnum+1).toString());//教师姓名 2
							vectea.add(vecjs.get(minnum+2).toString());//系部 3				
							vectea.add(vecjs.get(minnum+3).toString());//最大监考次数 4
							vectea.add(vecjs.get(minnum+4).toString());//任课教师 5
							vectea.add(vecjs.get(minnum+5).toString());//已安排监考次数 6
							vectea.add(vecjs.get(minnum+6).toString());//已安排监考时间 7
							for(int k=0;k<7;k++){
								vecjs.remove(minnum);
							}
							min=100;
							minnum=-1;
						};			
						
	//					for(int i=0;i<vectea.size();i=i+7){
	//						System.out.println(vectea.get(i)+" | "+vectea.get(i+1)+" | "+vectea.get(i+2)+" | "+vectea.get(i+3)+" | "+vectea.get(i+4)+" | "+vectea.get(i+5)+" | "+vectea.get(i+6));
	//					}
						
						int bjrknum=0;
						String rkkcbh="";//任课课程编号
						String rkkcmc="";//任课课程名称
						String jsbh="";
						String jsxm="";
						String xb="";
						String rkjs="";//1 是 2否
						String apjkcs="";
						String zdjkcs="";
						String teasjxl="";
						
						do {
							//分配监考教师
							jsbh=vectea.get(bjrknum).toString();
							jsxm=vectea.get(bjrknum+1).toString();
							xb=vectea.get(bjrknum+2).toString();
							zdjkcs=vectea.get(bjrknum+3).toString();
							rkjs=vectea.get(bjrknum+4).toString();
							apjkcs=vectea.get(bjrknum+5).toString();				
							teasjxl=vectea.get(bjrknum+6).toString();
											
							if(!vec.get(v+11).toString().equals(xb)){//不是这个系部的
								teaflag=1;//不能排
							}else{
								
									if(Integer.parseInt(apjkcs)>=Integer.parseInt(zdjkcs)) {//已安排监考次数=最大监考次数
										teaflag=3;
									}else {
										if(vec.get(v+12).toString().equals(jsbh)) {//是班主任
											teaflag=4;
										}else {
		//									for(int i=0;i<vecbjrk.size();i=i+4) {
		//										if(vecbjrk.get(i).toString().equals(jsbh)) {//是该班任课教师，获取教什么课程
		//											rkkcbh=vecbjrk.get(i+2).toString();
		//											rkkcmc=vecbjrk.get(i+3).toString();
		//										}
		//									}
											if(Integer.parseInt(vec.get(v+10).toString())<20) {//班级人数<20
												teaflag=0;
												
												for(int t=0;t<vectea.size();t=t+7) {
													vecjs.add(vectea.get(t).toString());//教师编号 1
													vecjs.add(vectea.get(t+1).toString());//教师姓名 2
													vecjs.add(vectea.get(t+2).toString());//系部 3				
													vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
													vecjs.add(vectea.get(t+4).toString());//任课教师 5
													vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
													vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
												}
											}else {
												if(teasjxl.indexOf(vec.get(v+2).toString())>-1) {//时间序列冲突
													teaflag=6;
												}else {//可以分配
													teaflag=0;
													vec.setElementAt(vec.get(v+8).toString()+"，"+jsbh, v+8);
													vec.setElementAt(vec.get(v+9).toString()+"，"+jsxm, v+9);
															
													String editsjxl="";
													if(teasjxl.equals("")) {
														editsjxl=vec.get(v+2).toString();
													}else {
														editsjxl=teasjxl+","+vec.get(v+2).toString();
													}
													vectea.setElementAt(Integer.parseInt(apjkcs)+1, bjrknum+5);
													vectea.setElementAt(editsjxl, bjrknum+6);
															
													for(int t=0;t<vectea.size();t=t+7) {
														vecjs.add(vectea.get(t).toString());//教师编号 1
														vecjs.add(vectea.get(t+1).toString());//教师姓名 2
														vecjs.add(vectea.get(t+2).toString());//系部 3				
														vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
														vecjs.add(vectea.get(t+4).toString());//任课教师 5
														vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
														vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
													}
													break;
												}
											}	
										}	
									}	
									
							}					
							
							bjrknum=bjrknum+7;
						}while(teaflag!=0&&bjrknum<vectea.size());
						//System.out.println("vec:"+vec.get(v).toString()+"|"+teaflag+"|"+bjrknum);
						if(teaflag!=0&&bjrknum==vectea.size()) {
							for(int t=0;t<vectea.size();t=t+7) {
								vecjs.add(vectea.get(t).toString());//教师编号 1
								vecjs.add(vectea.get(t+1).toString());//教师姓名 2
								vecjs.add(vectea.get(t+2).toString());//系部 3				
								vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
								vecjs.add(vectea.get(t+4).toString());//任课教师 5
								vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
								vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
							}
							wpks+=" <br/>"+vec.get(v+7).toString()+"  "+vec.get(v+4).toString();
						}
					}
				}	
							
				//保存到数据库
				for(int v=0;v<vec.size();v=v+14){
					//System.out.println(vec.get(v).toString()+"|"+vec.get(v+1).toString()+"|"+vec.get(v+2).toString()+"|"+vec.get(v+3).toString()+"|"+vec.get(v+4).toString()+"|"+vec.get(v+5).toString()+"|"+vec.get(v+6).toString()+"|"+vec.get(v+7).toString()+"|"+vec.get(v+8).toString()+"|"+vec.get(v+9).toString()+"|"+vec.get(v+10).toString()+"|"+vec.get(v+11).toString()+"|"+vec.get(v+12).toString()+"|"+vec.get(v+13).toString()+"|"+vec.get(v+14).toString()+"|"+vec.get(v+15).toString()+"|"+vec.get(v+16).toString()+"|"+vec.get(v+17).toString()+"|"+vec.get(v+18).toString());
					sqlupd="update dbo.V_考试管理_考场安排明细表  set [监考教师编号]='"+vec.get(v+8).toString()+"',[监考教师姓名]='"+vec.get(v+9).toString()+"' where [考场安排明细编号]='"+vec.get(v).toString()+"' and [考场安排主表编号]='"+vec.get(v+1).toString()+"' ";
					vecupd.add(sqlupd);
				}
				
				for(int t=0;t<vectea.size();t=t+7) {
					sqlupd="update dbo.V_考试管理_监考教师  set [已安排监考次数]='"+vectea.get(t+5).toString()+"',[已安排监考时间]='"+vectea.get(t+6).toString()+"' where [考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' and [监考教师编号]='"+vectea.get(t).toString()+"' ";
					vecupd.add(sqlupd);
				}
			
			}else{//查不到考试数据
				this.setMSG("查不到考试信息");
			}
		}else {//其它系部
			//查询系部所属教师
			String sqlxb="SELECT [系部代码],[系部名称] FROM [dbo].[V_基础信息_系部信息表] where [系部代码]!='C00' order by [排序] ";
			Vector vecxb=db.GetContextVector(sqlxb);
				
			String sqljs="SELECT distinct a.[工号],a.[姓名],b.[系部代码],c.最大监考次数,c.[是否任课] as 任课教师,'0' as 已安排监考次数,'' as 已安排监考时间 FROM [dbo].[V_教职工基本数据子类] a " + 
					"  left join [V_基础信息_系部教师信息表] b on a.工号=b.教师编号 " + 
					"  left join dbo.V_考试管理_监考教师 c on a.工号=c.监考教师编号 " + 
					"  where a.[是否有效]='1' and b.系部代码!='' and b.系部代码='"+xbdm+"' and c.考场安排主表编号='"+MyTools.fixSql(qzqm)+"' and c.[是否任课]!='0' ";
			Vector vecjs=db.GetContextVector(sqljs);
			if(vecjs!=null&&vecjs.size()>0) {
				//查询所有任课教师，非任课教师
	//			String sqlrkjs="SELECT distinct [授课教师编号],[授课教师姓名] FROM [dbo].[V_排课管理_课程表周详情表] " + 
	//					"  where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and [授课计划明细编号]!=''";
	//			Vector vecrkjs=db.GetContextVector(sqlrkjs);
	//			if(vecrkjs!=null&&vecrkjs.size()>0) {				
	//				for(int i=0;i<vecjs.size();i=i+7) {
	//					rkjsflag=0;
	//					for(int j=0;j<vecrkjs.size();j=j+2) {
	//						if(vecrkjs.get(j).toString().indexOf(vecjs.get(i).toString())>-1) {//是任课教师
	//							rkjsflag=1;
	//						}
	//					}
	//					if(rkjsflag==1) {
	//						vecjs.setElementAt("1", i+4);
	//					}else {
	//						vecjs.setElementAt("2", i+4);
	//					}
	//				}
	//			}			
			}
	
			//排监考教师----------------------------------------------------------
			
			//查询所有考试信息
			sql="SELECT a.[考场安排明细编号],a.[考场安排主表编号],a.[时间序列],a.[课程代码],a.[课程名称],a.[专业代码],a.[行政班代码],a.[行政班名称],a.[监考教师编号],a.[监考教师姓名],a.[学生人数],b.系部代码,b.班主任工号,a.[监考教师人数] " + 
				" FROM [dbo].[V_考试管理_考场安排明细表] a left join dbo.V_基础信息_班级信息表 b on a.行政班代码=b.班级编号 " + 
				" where a.[考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' and  a.[场地类型]='1' " +
				" and a.[行政班代码] in (select 班级编号 from dbo.V_基础信息_班级信息表 where 系部代码  in ('"+xbdm+"')) " +
				" order by b.系部代码,a.[行政班代码] ";
			vec=db.GetContextVector(sql);
			
			if(vec!=null&&vec.size()>0){
				int teaflag=0;//是否可以排标记
				int usetea=0;//教师已经排次数
				
				//排任课教师 1
				for(int v=0;v<vec.size();v=v+14){							
					//查询该班级任课教师
					Vector vecbjrk=new Vector();//保存当前班级的任课教师 
					int teasameflag=-1;
					String sqlbj="SELECT distinct [授课教师编号],[授课教师姓名],[课程代码],[课程名称] FROM [dbo].[V_排课管理_课程表周详情表] " + 
							"  where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and [授课计划明细编号]!='' and 行政班代码='"+vec.get(v+6).toString()+"' and 行政班名称='"+vec.get(v+7).toString()+"' ";
					Vector vecbj=db.GetContextVector(sqlbj);
					if(vecbj!=null&&vecbj.size()>0) {
						for(int k=0;k<vecbj.size();k=k+4) {
							if(vecbj.indexOf("+")>-1) {//有多个教师
								String[] teabh=vecbj.get(k).toString().split("\\+");
								String[] teaname=vecbj.get(k+1).toString().split("\\+");
								for(int l=0;l<teabh.length;l++) {
									vecbj.add(teabh[l]);
									vecbj.add(teaname[l]);
									vecbj.add(vecbj.get(k+2).toString());
									vecbj.add(vecbj.get(k+3).toString());
								}
							}
						}
	
						for(int k=0;k<vecbj.size();k=k+4) {
							teasameflag=0;
							for(int l=0;l<vecbjrk.size();l++) {
								if(vecbj.get(k).toString().equals(vecbjrk.get(l).toString())&&vecbj.get(k+1).toString().equals(vecbjrk.get(l+1).toString())) {//教师相同
									if(vecbjrk.get(l+2).toString().indexOf(vecbj.get(k+2).toString())>-1 && vecbjrk.get(l+3).toString().indexOf(vecbj.get(k+3).toString())>-1) {//任课课程相同
										teasameflag=1;
									}else {//添加课程到任课课程
										teasameflag=1;
										vecbjrk.setElementAt(vecbjrk.get(l+2).toString()+","+vecbj.get(k+2).toString(), l+2);
										vecbjrk.setElementAt(vecbjrk.get(l+3).toString()+","+vecbj.get(k+3).toString(), l+3);
									}
								}
							}
							if(teasameflag==0) {
								vecbjrk.add(vecbj.get(k).toString());
								vecbjrk.add(vecbj.get(k+1).toString());
								vecbjrk.add(vecbj.get(k+2).toString());
								vecbjrk.add(vecbj.get(k+3).toString());
							}
						}
					}
					
	//				for(int i=0;i<vecbjrk.size();i=i+4){
	//					System.out.println(vecbjrk.get(i)+" | "+vecbjrk.get(i+1)+" | "+vecbjrk.get(i+2)+" | "+vecbjrk.get(i+3));
	//				}
					
					//vectea按已安排监考次数排序
					vectea=new Vector();
					int min=100;
					int minnum=-1;
	
					while(vecjs.size()>0){
						for(int j=0;j<vecjs.size();j=j+7){
							if(Integer.parseInt(vecjs.get(j+5).toString())<min){
								min=Integer.parseInt(vecjs.get(j+5).toString());
								minnum=j;
							}
						}
						vectea.add(vecjs.get(minnum).toString());//教师编号 1
						vectea.add(vecjs.get(minnum+1).toString());//教师姓名 2
						vectea.add(vecjs.get(minnum+2).toString());//系部 3				
						vectea.add(vecjs.get(minnum+3).toString());//最大监考次数 4
						vectea.add(vecjs.get(minnum+4).toString());//任课教师 5
						vectea.add(vecjs.get(minnum+5).toString());//已安排监考次数 6
						vectea.add(vecjs.get(minnum+6).toString());//已安排监考时间 7
						for(int k=0;k<7;k++){
							vecjs.remove(minnum);
						}
						min=100;
						minnum=-1;
					};			
			
	//				for(int i=0;i<vectea.size();i=i+7){
	//					System.out.println(vectea.get(i)+" | "+vectea.get(i+1)+" | "+vectea.get(i+2)+" | "+vectea.get(i+3)+" | "+vectea.get(i+4)+" | "+vectea.get(i+5)+" | "+vectea.get(i+6));
	//				}
					
					int bjrknum=0;
					String rkkcbh="";//任课课程编号
					String rkkcmc="";//任课课程名称
					String jsbh="";
					String jsxm="";
					String xb="";
					String rkjs="";//1 是 2否
					String apjkcs="";
					String zdjkcs="";
					String teasjxl="";
					
					do {		
						//分配监考教师
						jsbh=vectea.get(bjrknum).toString();
						jsxm=vectea.get(bjrknum+1).toString();
						xb=vectea.get(bjrknum+2).toString();
						zdjkcs=vectea.get(bjrknum+3).toString();
						rkjs=vectea.get(bjrknum+4).toString();
						apjkcs=vectea.get(bjrknum+5).toString();				
						teasjxl=vectea.get(bjrknum+6).toString();
						rkkcbh="";
						rkkcmc="";
						
						if(!vec.get(v+11).toString().equals(xb)){//不是这个系部的
							teaflag=1;//不能排
						}else{
							if(rkjs.equals("2")) {//不是任课教师
								teaflag=2;
							}else {
								if(Integer.parseInt(apjkcs)>=Integer.parseInt(zdjkcs)) {//已安排监考次数=最大监考次数
									teaflag=3;
								}else {
									if(vec.get(v+12).toString().equals(jsbh)) {//是班主任
										teaflag=4;
									}else {						
										for(int i=0;i<vecbjrk.size();i=i+4) {
											if(vecbjrk.get(i).toString().equals(jsbh)) {//是该班任课教师，获取教什么课程
												rkkcbh=vecbjrk.get(i+2).toString();
												rkkcmc=vecbjrk.get(i+3).toString();
											}
										}
										if(rkkcbh.equals("")||(rkkcbh.indexOf(vec.get(v+3).toString())>-1&&rkkcmc.indexOf(vec.get(v+4).toString())>-1)) {//任课老师是教这门课的
											teaflag=5;
										}else {
											if(teasjxl.indexOf(vec.get(v+2).toString())>-1) {//时间序列冲突
												teaflag=6;
											}else {//可以分配
												teaflag=0;
												vec.setElementAt(jsbh, v+8);
												vec.setElementAt(jsxm, v+9);
														
												String editsjxl="";
												if(teasjxl.equals("")) {
													editsjxl=vec.get(v+2).toString();
												}else {
													editsjxl=teasjxl+","+vec.get(v+2).toString();
												}
												vectea.setElementAt(Integer.parseInt(apjkcs)+1, bjrknum+5);
												vectea.setElementAt(editsjxl, bjrknum+6);
														
												for(int t=0;t<vectea.size();t=t+7) {
													vecjs.add(vectea.get(t).toString());//教师编号 1
													vecjs.add(vectea.get(t+1).toString());//教师姓名 2
													vecjs.add(vectea.get(t+2).toString());//系部 3				
													vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
													vecjs.add(vectea.get(t+4).toString());//任课教师 5
													vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
													vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
												}
												break;
											}
										}	
									}	
								}	
							}	
						}					
						
						bjrknum=bjrknum+7;
					}while(teaflag!=0&&bjrknum<vectea.size());
					//System.out.println("vec:"+vec.get(v).toString()+"|"+bjrknum);
					if(teaflag!=0&&bjrknum==vectea.size()) {
						for(int t=0;t<vectea.size();t=t+7) {
							vecjs.add(vectea.get(t).toString());//教师编号 1
							vecjs.add(vectea.get(t+1).toString());//教师姓名 2
							vecjs.add(vectea.get(t+2).toString());//系部 3				
							vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
							vecjs.add(vectea.get(t+4).toString());//任课教师 5
							vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
							vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
						}
						//wpks+=" <br/>"+vec.get(v+7).toString()+"  "+vec.get(v+4).toString()+"  "+vec.get(v+10).toString()+"人";
					
						vectea=new Vector();
						
						//剩余教师
						for(int i=0;i<vecjs.size();i=i+7){
								vectea.add(vecjs.get(i).toString());//教师编号 1
								vectea.add(vecjs.get(i+1).toString());//教师姓名 2
								vectea.add(vecjs.get(i+2).toString());//系部 3				
								vectea.add(vecjs.get(i+3).toString());//最大监考次数 4
								vectea.add(vecjs.get(i+4).toString());//任课教师 5
								vectea.add(vecjs.get(i+5).toString());//已安排监考次数 6
								vectea.add(vecjs.get(i+6).toString());//已安排监考时间 7
						}
						
						//重新排一次
						bjrknum=0;	
						do {		
							//分配监考教师
							jsbh=vectea.get(bjrknum).toString();
							jsxm=vectea.get(bjrknum+1).toString();
							xb=vectea.get(bjrknum+2).toString();
							zdjkcs=vectea.get(bjrknum+3).toString();
							rkjs=vectea.get(bjrknum+4).toString();
							apjkcs=vectea.get(bjrknum+5).toString();				
							teasjxl=vectea.get(bjrknum+6).toString();
							rkkcbh="";
							rkkcmc="";
							
									if(Integer.parseInt(apjkcs)>=Integer.parseInt(zdjkcs)) {//已安排监考次数=最大监考次数
										teaflag=3;
									}else {
										if(vec.get(v+12).toString().equals(jsbh)) {//是班主任
											teaflag=4;
										}else {						
												if(teasjxl.indexOf(vec.get(v+2).toString())>-1) {//时间序列冲突
													teaflag=6;
												}else {
													for(int i=0;i<vecbjrk.size();i=i+4) {
														if(vecbjrk.get(i).toString().equals(jsbh)) {//是该班任课教师，获取教什么课程
															rkkcbh=vecbjrk.get(i+2).toString();
															rkkcmc=vecbjrk.get(i+3).toString();
														}
													}
													if(rkkcbh.indexOf(vec.get(v+3).toString())>-1&&rkkcmc.indexOf(vec.get(v+4).toString())>-1) {//任课老师是教这门课的
														teaflag=5;
													}else {//可以分配
														teaflag=0;
														vec.setElementAt(jsbh, v+8);
														vec.setElementAt(jsxm, v+9);
																
														String editsjxl="";
														if(teasjxl.equals("")) {
															editsjxl=vec.get(v+2).toString();
														}else {
															editsjxl=teasjxl+","+vec.get(v+2).toString();
														}
														vectea.setElementAt(Integer.parseInt(apjkcs)+1, bjrknum+5);
														vectea.setElementAt(editsjxl, bjrknum+6);
																
														for(int t=0;t<vectea.size();t=t+7) {
															vecjs.add(vectea.get(t).toString());//教师编号 1
															vecjs.add(vectea.get(t+1).toString());//教师姓名 2
															vecjs.add(vectea.get(t+2).toString());//系部 3				
															vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
															vecjs.add(vectea.get(t+4).toString());//任课教师 5
															vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
															vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
														}
														
														if(rkkcbh.equals("")){//可以分配，不是该班任课教师
															tsxx+=" <br/>"+vec.get(v+7).toString()+"  "+vec.get(v+4).toString();
														}else {//可以分配，是该班任课教师
															
														}
														break;
													}
												}		
										}	
									}	
												
							bjrknum=bjrknum+7;
						}while(teaflag!=0&&bjrknum<vectea.size());
						
						if(teaflag!=0&&bjrknum==vectea.size()) {//未安排教师
							for(int t=0;t<vectea.size();t=t+7) {
								vecjs.add(vectea.get(t).toString());//教师编号 1
								vecjs.add(vectea.get(t+1).toString());//教师姓名 2
								vecjs.add(vectea.get(t+2).toString());//系部 3				
								vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
								vecjs.add(vectea.get(t+4).toString());//任课教师 5
								vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
								vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
							}
							wpks+=" <br/>"+vec.get(v+7).toString()+"  "+vec.get(v+4).toString();
						}
						
					}
				}
									
				//排非任课教师 2		
				//学生人数<20 or 开卷考试 的班级， 只需要一位监考老师	
				
				int jkjsrs=-1;
				for(int t=0;t<vectea.size();t=t+7) {
					vecjs.add(vectea.get(t).toString());//教师编号 1
					vecjs.add(vectea.get(t+1).toString());//教师姓名 2
					vecjs.add(vectea.get(t+2).toString());//系部 3				
					vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
					vecjs.add(vectea.get(t+4).toString());//任课教师 5
					vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
					vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
				}
				
				for(int v=0;v<vec.size();v=v+14){							
					jkjsrs=Integer.parseInt(vec.get(v+13).toString());
					
					for(int n=1;n<jkjsrs;n++) {
						//vectea按已安排监考次数排序
						vectea=new Vector();
						int min=100;
						int minnum=-1;
		
						while(vecjs.size()>0){
							for(int j=0;j<vecjs.size();j=j+7){
								if(Integer.parseInt(vecjs.get(j+5).toString())<min){
									min=Integer.parseInt(vecjs.get(j+5).toString());
									minnum=j;
								}
							}
							vectea.add(vecjs.get(minnum).toString());//教师编号 1
							vectea.add(vecjs.get(minnum+1).toString());//教师姓名 2
							vectea.add(vecjs.get(minnum+2).toString());//系部 3				
							vectea.add(vecjs.get(minnum+3).toString());//最大监考次数 4
							vectea.add(vecjs.get(minnum+4).toString());//任课教师 5
							vectea.add(vecjs.get(minnum+5).toString());//已安排监考次数 6
							vectea.add(vecjs.get(minnum+6).toString());//已安排监考时间 7
							for(int k=0;k<7;k++){
								vecjs.remove(minnum);
							}
							min=100;
							minnum=-1;
						};			
						
		//				for(int i=0;i<vectea.size();i=i+7){
		//					System.out.println(vectea.get(i)+" | "+vectea.get(i+1)+" | "+vectea.get(i+2)+" | "+vectea.get(i+3)+" | "+vectea.get(i+4)+" | "+vectea.get(i+5)+" | "+vectea.get(i+6));
		//				}
						
						int bjrknum=0;
						String rkkcbh="";//任课课程编号
						String rkkcmc="";//任课课程名称
						String jsbh="";
						String jsxm="";
						String xb="";
						String rkjs="";//1 是 2否
						String apjkcs="";
						String zdjkcs="";
						String teasjxl="";
						
						do {
							//分配监考教师
							jsbh=vectea.get(bjrknum).toString();
							jsxm=vectea.get(bjrknum+1).toString();
							xb=vectea.get(bjrknum+2).toString();
							zdjkcs=vectea.get(bjrknum+3).toString();
							rkjs=vectea.get(bjrknum+4).toString();
							apjkcs=vectea.get(bjrknum+5).toString();				
							teasjxl=vectea.get(bjrknum+6).toString();
											
							if(!vec.get(v+11).toString().equals(xb)){//不是这个系部的
								teaflag=1;//不能排
							}else{
								
									if(Integer.parseInt(apjkcs)>=Integer.parseInt(zdjkcs)) {//已安排监考次数=最大监考次数
										teaflag=3;
									}else {
										if(vec.get(v+12).toString().equals(jsbh)) {//是班主任
											teaflag=4;
										}else {
		//									for(int i=0;i<vecbjrk.size();i=i+4) {
		//										if(vecbjrk.get(i).toString().equals(jsbh)) {//是该班任课教师，获取教什么课程
		//											rkkcbh=vecbjrk.get(i+2).toString();
		//											rkkcmc=vecbjrk.get(i+3).toString();
		//										}
		//									}
											if(Integer.parseInt(vec.get(v+10).toString())<20) {//班级人数<20
												teaflag=0;
											}else {
												if(teasjxl.indexOf(vec.get(v+2).toString())>-1) {//时间序列冲突
													teaflag=6;
												}else {//可以分配
													teaflag=0;
													vec.setElementAt(vec.get(v+8).toString()+"，"+jsbh, v+8);
													vec.setElementAt(vec.get(v+9).toString()+"，"+jsxm, v+9);
															
													String editsjxl="";
													if(teasjxl.equals("")) {
														editsjxl=vec.get(v+2).toString();
													}else {
														editsjxl=teasjxl+","+vec.get(v+2).toString();
													}
													vectea.setElementAt(Integer.parseInt(apjkcs)+1, bjrknum+5);
													vectea.setElementAt(editsjxl, bjrknum+6);
															
													for(int t=0;t<vectea.size();t=t+7) {
														vecjs.add(vectea.get(t).toString());//教师编号 1
														vecjs.add(vectea.get(t+1).toString());//教师姓名 2
														vecjs.add(vectea.get(t+2).toString());//系部 3				
														vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
														vecjs.add(vectea.get(t+4).toString());//任课教师 5
														vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
														vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
													}
													break;
												}
											}	
										}	
									}	
									
							}					
							System.out.println("err--"+teaflag+"|"+bjrknum+"|"+vectea.size());
							bjrknum=bjrknum+7;
						}while(teaflag!=0&&bjrknum<vectea.size());
						
						if(teaflag!=0&&bjrknum==vectea.size()) {
							for(int t=0;t<vectea.size();t=t+7) {
								vecjs.add(vectea.get(t).toString());//教师编号 1
								vecjs.add(vectea.get(t+1).toString());//教师姓名 2
								vecjs.add(vectea.get(t+2).toString());//系部 3				
								vecjs.add(vectea.get(t+3).toString());//最大监考次数 4
								vecjs.add(vectea.get(t+4).toString());//任课教师 5
								vecjs.add(vectea.get(t+5).toString());//已安排监考次数 6
								vecjs.add(vectea.get(t+6).toString());//已安排监考时间 7
							}
							wpks+=" <br/>"+vec.get(v+7).toString()+"  "+vec.get(v+4).toString();
						}
					}
				}	
							
				//保存到数据库
				for(int v=0;v<vec.size();v=v+14){
					//System.out.println(vec.get(v).toString()+"|"+vec.get(v+1).toString()+"|"+vec.get(v+2).toString()+"|"+vec.get(v+3).toString()+"|"+vec.get(v+4).toString()+"|"+vec.get(v+5).toString()+"|"+vec.get(v+6).toString()+"|"+vec.get(v+7).toString()+"|"+vec.get(v+8).toString()+"|"+vec.get(v+9).toString()+"|"+vec.get(v+10).toString()+"|"+vec.get(v+11).toString()+"|"+vec.get(v+12).toString()+"|"+vec.get(v+13).toString()+"|"+vec.get(v+14).toString()+"|"+vec.get(v+15).toString()+"|"+vec.get(v+16).toString()+"|"+vec.get(v+17).toString()+"|"+vec.get(v+18).toString());
					sqlupd="update dbo.V_考试管理_考场安排明细表  set [监考教师编号]='"+vec.get(v+8).toString()+"',[监考教师姓名]='"+vec.get(v+9).toString()+"' where [考场安排明细编号]='"+vec.get(v).toString()+"' and [考场安排主表编号]='"+vec.get(v+1).toString()+"' ";
					vecupd.add(sqlupd);
				}
				
				for(int t=0;t<vectea.size();t=t+7) {
					sqlupd="update dbo.V_考试管理_监考教师  set [已安排监考次数]='"+vectea.get(t+5).toString()+"',[已安排监考时间]='"+vectea.get(t+6).toString()+"' where [考场安排主表编号]='"+MyTools.fixSql(qzqm)+"' and [监考教师编号]='"+vectea.get(t).toString()+"' ";
					vecupd.add(sqlupd);
				}
			
			}else{//查不到考试数据
				this.setMSG("查不到考试信息");
			}
		}

		//System.out.println("未排考试：--"+wpks);
		
		//保存排好的监考教师 
		if(db.executeInsertOrUpdateTransaction(vecupd)){//exec2
			this.setMSG("自动安排监考教师已完成");
			this.setMSG2(wpks);
			this.setMSG3(tsxx);
		}else{
			
		}
			
	}
	
	
	/**
	 * 查询当前学年设置的每周天数和每天节数等相关信息
	 * @date:2016-06-02
	 * @author:lupengfei
	 * @return 课程表相关信息
	 * @throws SQLException
	 */
	public Vector initBlankKCAP(String PK_KSZBBH)throws SQLException{
		Vector vec = null;
		Vector vec2= null;
		Vector result = new Vector();
		String sql = "";
		String sql2= "";
		String kstime= "";
		int ksnum=0;
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
//		sql = "select t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
//				"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间," +
//				"case when convert(nvarchar(10),t1.排课截止时间,21) >= convert(nvarchar(10),getDate(),21) then '0' else '1' end as 是否过期 " +
//				"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "'";
		sql2="select [开始时间],[结束时间] from ( " + 
				"  SELECT distinct [开始时间],[结束时间] FROM [dbo].[V_考试管理_考试日期]  where [考场安排主表编号]='"+MyTools.fixSql(PK_KSZBBH)+"' " + 
				"  ) x " + 
				"  order by convert(int,substring([开始时间],0,charindex(':',[开始时间]))),convert(int,substring([结束时间],0,charindex(':',[结束时间]))) ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0) {
			for(int i=0;i<vec2.size();i=i+2) {
				kstime+=vec2.get(i).toString()+"~"+vec2.get(i+1).toString()+",";
			}
			if(!kstime.equals("")) {
				kstime=kstime.substring(0,kstime.length()-1);
			}
			ksnum=vec2.size()/2;
		}
		sql="select '5' as 每周天数,'0' as 上午节数,'0' as 中午节数,'"+ksnum+"' as 下午节数,'0' as 晚上节数 ,'"+kstime+"' as 节次时间  ";
		vec = db.GetContextVector(sql);
		result.add(vec);
		
		sql = "select top 1 (select count(*) from V_规则管理_授课计划明细表 b " +
				"left join V_规则管理_授课计划主表 c on b.授课计划主表编号=c.授课计划主表编号 " +
				"where b.状态='1' and a.班级编号=c.行政班代码 and c.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "') as 课程数 from V_基础信息_班级信息表 a";
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//				sql += " where a.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//		}
		sql += " order by 课程数 desc";
		vec = db.GetContextVector(sql);
		result.add(vec);
		
		//查询学期总周次数
		sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		result.add(MyTools.StrFiltr(vec.get(0)));
				
		sql="SELECT distinct [考试日期] FROM [dbo].[V_考试管理_考试日期] where [考场安排主表编号]='"+MyTools.fixSql(PK_KSZBBH)+"' ";
		vec = db.GetContextVector(sql);
		result.add(vec);
		
		return result;
	}
	
	/**
	 * 查询班级课程及未排课程
	 * @date:2015-05-30
	 * @author:yeq
	 * @return 班级课程及未排课程信息
	 * @throws SQLException
	 */
	public Vector loadClassKCAP()throws SQLException{
		Vector resultVec = new Vector();
		Vector vec = null;
		Vector vec2 = new Vector();
		Vector vec3 = null;
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		Vector tempVec = null;
		String jsonStr = "";
		String maxMxId="";
		
			//获取考场安排明细编号
//			String sqlmxid="select max(cast(SUBSTRING([考场安排明细编号],8,11) as bigint)) from dbo.V_考试管理_考场安排明细表";
//			Vector vecmxid = db.GetContextVector(sqlmxid);
//			if (!vecmxid.toString().equals("[]") && vecmxid.size() > 0) {
//				maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vecmxid.get(0))))+1);
//				this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
//			}else{
//				maxMxId= String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr("10000000000")))+1);
//				this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
//			}
//			
//			sql3="select 专业代码 from dbo.V_基础信息_班级信息表  where 班级编号='" + MyTools.fixSql(this.getXZBDM()) + "' ";
//			vec3=db.GetContextVector(sql3);
//			if(vec3!=null&&vec3.size()>0) {
//				sql="select m.[课程代码],m.[课程名称],m.[班级名称],m.[总人数],m.[专业代码] from " + 
//					"(SELECT distinct a.[课程代码],a.[课程名称],c.[班级名称],c.[总人数],c.[专业代码] FROM [dbo].[V_规则管理_授课计划明细表] a  left join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号  left join dbo.V_基础信息_班级信息表 c on b.行政班代码=c.[班级编号] where b.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "' and b.行政班代码='" + MyTools.fixSql(this.getXZBDM()) + "') m " + 
//					"inner join (select [考场安排主表编号],[专业编号],[课程代码] from dbo.V_考试管理_考试课程 where 专业编号 ='000000' or 专业编号 ='"+vec3.get(0).toString()+"') n on m.课程代码=n.课程代码";
//				vec=db.GetContextVector(sql);
//				if(vec!=null&&vec.size()>0) {
//					for(int i=0;i<vec.size();i=i+5) {
//						//判断课程是否存在
//						sql="select count(*) from dbo.V_考试管理_考场安排明细表 where 考场安排主表编号='" + MyTools.fixSql(this.getKCAPZBBH()) + "' and 行政班代码='" + MyTools.fixSql(this.getXZBDM()) + "' and 课程代码='" + vec.get(i).toString() + "' and 课程名称='" + vec.get(i+1).toString() + "' ";
//						if(db.getResultFromDB(sql)) {
//							
//						}else {
//							sql2="insert into V_考试管理_考场安排明细表 ([考场安排明细编号],[考场安排主表编号],[课程代码],[课程名称],[行政班代码],[行政班名称],[场地类型],[学生人数],[专业代码]) "
//								+ "values ('"+this.getKCAPMXBH()+"','"+this.getKCAPZBBH()+"','"+vec.get(i).toString()+"','"+vec.get(i+1).toString()+"','" + MyTools.fixSql(this.getXZBDM()) + "','"+vec.get(i+2).toString()+"','0','"+vec.get(i+3).toString()+"','"+vec.get(i+4).toString()+"') ";
//							vec2.add(sql2);
//							
//							//更新下一个明细编号
//							String mxid=this.getKCAPMXBH();
//							//System.out.println(mxid.substring(7,mxid.length()));
//							maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(mxid.substring(7,mxid.length()))))+1);
//							this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
//						}
//					}
//					db.executeInsertOrUpdateTransaction(vec2);
//				}
//			}
		
		//查询课程表
		sql = "SELECT a.时间序列,a.课程代码,a.课程名称,[场地类型] as 节数,[监考教师编号],[监考教师姓名],[监考教师人数]  " +
			"FROM [dbo].[V_考试管理_考场安排明细表] a left join dbo.V_考试管理_考场安排主表 b on a.考场安排主表编号=b.考场安排主表编号 " +
			"where b.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "' " +
			"and a.行政班代码='" + MyTools.fixSql(this.getXZBDM()) + "' " +
			"order by a.课程代码 ";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		//查询未排课程信息
		sql = "SELECT distinct [课程代码],[课程名称],case when [场地类型]='1' then '0' else '1' end as 节数  " + 
			"FROM [dbo].[V_考试管理_考场安排明细表] a left join dbo.V_考试管理_考场安排主表 b on a.考场安排主表编号=b.考场安排主表编号 " +
			"where b.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "' " +
			"and a.行政班代码='" + MyTools.fixSql(this.getXZBDM()) + "' " +
			"order by a.课程代码 ";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		return resultVec;
	}
	
	/**
	 * 将所有考试信息插入数据库
	 * @date:2017-12-05
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void updateKCB(String kcaparray) throws SQLException{
		Vector vec = new Vector();
		String sql = "";
		String zbbh="";
		String xzbdm="";
		String kssj="";
		String kskc="";
		String jkjsrs="";
		String bjid="";
		String sqlbj = "";
		Vector vecbj = null;
		
		//if(kcaparray.indexOf(",")>-1) {
		//所有课程考试时间改成空
			String[] kcaparr=kcaparray.split(",");
			sql="update [V_考试管理_考场安排明细表] set [考试场次编号]='',[时间序列]='',[场地类型]='0',[监考教师人数]='' "
				+ "where [考场安排主表编号]='"+kcaparr[0]+"' and [行政班代码]='"+kcaparr[1]+"' ";
			vec.add(sql);
			
			bjid="'"+kcaparr[1]+"'";
		
			sqlbj="select [班级编号] from [dbo].[V_基础信息_班级信息表] " + 
					"  where 年级代码 in (select 年级代码 from V_基础信息_班级信息表 where 班级编号='"+kcaparr[1]+"') " + 
					"  and 系部代码 in (select 系部代码 from V_基础信息_班级信息表 where 班级编号='"+kcaparr[1]+"') " + 
					"  and 专业代码 in (select 专业代码 from V_基础信息_班级信息表 where 班级编号='"+kcaparr[1]+"') ";
			vecbj=db.GetContextVector(sqlbj);
			if(vecbj!=null&&vecbj.size()>0) {
				for(int i=0;i<vecbj.size();i++) {
					if(!vecbj.get(i).toString().equals(kcaparr[1])) {
						String sqlck="select count(*) from [dbo].[V_考试管理_考场安排明细表] "
								+ "where [考场安排主表编号]='"+kcaparr[0]+"' and [行政班代码]='"+vecbj.get(i).toString()+"' and [考试场次编号]!='' ";
						if(!db.getResultFromDB(sqlck)) {
							bjid+=",'"+vecbj.get(i).toString()+"'";
						}	
					}
				}	
			}
			
			for(int i=0;i<kcaparr.length;i=i+5) {
				zbbh=kcaparr[i];
				xzbdm=kcaparr[i+1];
				kssj=kcaparr[i+2];
				kskc=kcaparr[i+4];
				if(kcaparr[i+3].equals("")) {
					String sqlrs="select [学生人数],[考试形式] from dbo.V_考试管理_考场安排明细表 where [行政班代码]='"+kcaparr[i+1]+"' and [课程名称]='"+kskc+"' ";
					Vector vecrs=db.GetContextVector(sqlrs);
					if(vecrs!=null&&vecrs.size()>0) {
						if(Integer.parseInt(vecrs.get(0).toString())<20||vecrs.get(1).toString().equals("7")) {
							jkjsrs="1";
						}else {
							jkjsrs="2";
						}
					}
				}else {
					jkjsrs=kcaparr[i+3];
				}
				
				if(!kskc.equals("请选择")) {
					sql="update [V_考试管理_考场安排明细表] set [考试场次编号]='"+kssj+"',[场地类型]='1',[监考教师人数]='"+jkjsrs+"' "
						+ "where [考场安排主表编号]='"+zbbh+"' and [行政班代码] in ("+bjid+") and [课程名称]='"+kskc+"' ";
					vec.add(sql);
				}
			}	
		//}	
		
		if(db.executeInsertOrUpdateTransaction(vec)) {
			this.setMSG("保存成功");
		}else {
			this.setMSG("保存失败");
		}
		
	}
	
	/**
	 * 查询可用监考教师列表
	 * @date:2017-12-20
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public Vector getVecTea() throws SQLException{
		//查询监考教师表中所有老师状态
		String sqltea="select [监考教师编号],[监考教师姓名],[教师类型编号],[教师类型名称],[最大监考次数],[是否任课],[已安排监考次数],[已安排监考时间] " + 
						" FROM [dbo].[V_考试管理_监考教师] where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [是否任课]!='0' order by 监考教师编号 ";
		Vector vectea=db.getConttexJONSArr(sqltea, 0, 0);
		return vectea;
	}
	
	/**
	 * 查询可用监考教师列表
	 * @date:2017-12-20
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public Vector queTeaList(int pageNum, int pageSize, String JKJSBH, String JKJSXM, String SJXL, String KCMC, String num, String jkjsarray, String ic_teaCode, String ic_teaName, String chgteaarray) throws SQLException{
		Vector vec = null;
		String sql = "";
		String sql2 = "";
		String sqljk="";
		Vector vecrk=new Vector();
		Vector vecrkxs=new Vector();
		Vector vecqt=new Vector();
		Vector vecjs=new Vector();
		int teaflag=-1;

		//查询监考教师表中所有老师状态
		String sqltea="select [监考教师编号],[监考教师姓名],[教师类型编号],[教师类型名称],[最大监考次数],[是否任课],[已安排监考次数],[已安排监考时间] " + 
								" FROM [dbo].[V_考试管理_监考教师] where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [是否任课]!='0' order by 监考教师编号 ";
		Vector vectea=db.GetContextVector(sqltea);
	
		if(!chgteaarray.equals("")) {
			String[] chgtea=chgteaarray.split(",");
			String sjxltime=chgtea[0];
			String oldteaid=chgtea[1];
			String oldteaname=chgtea[2];
			String newteaid=chgtea[3];
			String newteaname=chgtea[4];
			
			String[] oldteabh=oldteaid.split("，");
			String[] newteabh=newteaid.split("，");
			String yapjksj="";
			
			for(int i=0;i<vectea.size();i=i+8){
				for(int j=0;j<oldteabh.length;j++) {
					if(!oldteabh[j].equals(newteabh[j])) {
						if(vectea.get(i).toString().equals(oldteabh[j])){
							vectea.setElementAt((Integer.parseInt(vectea.get(i+6).toString())-1)+"", i+6);
							yapjksj=vectea.get(i+7).toString();
							yapjksj=(yapjksj+",").replaceAll(sjxltime+",", "");
							if(!yapjksj.equals("")) {
								yapjksj=yapjksj.substring(0, yapjksj.length()-1);
							}
							vectea.setElementAt(yapjksj, i+7);
						}
						if(vectea.get(i).toString().equals(newteabh[j])){
							vectea.setElementAt((Integer.parseInt(vectea.get(i+6).toString())+1)+"", i+6);
							yapjksj=vectea.get(i+7).toString();
							if(vectea.get(i+7).toString().equals("")){
								vectea.setElementAt(sjxltime, i+7);
							}else{
								vectea.setElementAt(yapjksj+","+sjxltime, i+7);
							}	
						}
					}
				}		
			}
		}
		
		
		//去掉已排的教师
//		String[] teainfo=jkjsarray.split(",");
//		for(int i=0;i<teainfo.length;i=i+2) {
//			String inputid=teainfo[i].substring(5, teainfo[i].length());
//			String jknum=inputid.split("#")[0];
//			String teachid=inputid.split("#")[1];
//
//		}
		
		//查询班主任
		String sqlbzr="select [班主任工号] from [V_基础信息_班级信息表] where [班级编号]='"+MyTools.fixSql(this.getXZBDM())+"' ";
		Vector vecbzr=db.GetContextVector(sqlbzr);
		
		//查询该班级任课教师
		Vector vecbjrk=new Vector();//保存当前班级的任课教师 
		int teasameflag=-1;
		String sqlbj="SELECT distinct [授课教师编号],[授课教师姓名],[课程代码],[课程名称] FROM [dbo].[V_排课管理_课程表周详情表] " + 
				"  where 学年学期编码='"+MyTools.fixSql(this.getXNXQ())+"' and [授课计划明细编号]!='' and 行政班代码='"+MyTools.fixSql(this.getXZBDM())+"' ";
		Vector vecbj=db.GetContextVector(sqlbj);
		if(vecbj!=null&&vecbj.size()>0) {
			for(int k=0;k<vecbj.size();k=k+4) {
				if(vecbj.indexOf("+")>-1) {//有多个教师
					String[] teabh=vecbj.get(k).toString().split("\\+");
					String[] teaname=vecbj.get(k+1).toString().split("\\+");
					for(int l=0;l<teabh.length;l++) {
						vecbj.add(teabh[l]);
						vecbj.add(teaname[l]);
						vecbj.add(vecbj.get(k+2).toString());
						vecbj.add(vecbj.get(k+3).toString());
					}
				}
			}

			for(int k=0;k<vecbj.size();k=k+4) {
				teasameflag=0;
				for(int l=0;l<vecbjrk.size();l++) {
					if(vecbj.get(k).toString().equals(vecbjrk.get(l).toString())&&vecbj.get(k+1).toString().equals(vecbjrk.get(l+1).toString())) {//教师相同
						if(vecbjrk.get(l+2).toString().indexOf(vecbj.get(k+2).toString())>-1 && vecbjrk.get(l+3).toString().indexOf(vecbj.get(k+3).toString())>-1) {//任课课程相同
							teasameflag=1;
						}else {//添加课程到任课课程
							teasameflag=1;
							vecbjrk.setElementAt(vecbjrk.get(l+2).toString()+","+vecbj.get(k+2).toString(), l+2);
							vecbjrk.setElementAt(vecbjrk.get(l+3).toString()+","+vecbj.get(k+3).toString(), l+3);
						}
					}
				}
				if(teasameflag==0) {
					vecbjrk.add(vecbj.get(k).toString());
					vecbjrk.add(vecbj.get(k+1).toString());
					vecbjrk.add(vecbj.get(k+2).toString());
					vecbjrk.add(vecbj.get(k+3).toString());
				}
			}
		}
		
		//获取任课教师
		for(int i=0;i<vectea.size();i=i+8) {
			for(int j=0;j<vecbjrk.size();j=j+4) {
				if(vectea.get(i).toString().equals(vecbjrk.get(j).toString())&&vectea.get(i+1).toString().equals(vecbjrk.get(j+1).toString())) {
					vecrk.add(vectea.get(i).toString());
					vecrk.add(vectea.get(i+1).toString());
					vecrk.add(vectea.get(i+2).toString());
					vecrk.add(vectea.get(i+3).toString());
					vecrk.add(vectea.get(i+4).toString());
					vecrk.add(vectea.get(i+5).toString());
					vecrk.add(vectea.get(i+6).toString());
					vecrk.add(vectea.get(i+7).toString());
					vecrk.add(vecbjrk.get(j+2).toString());
					vecrk.add(vecbjrk.get(j+3).toString());
				}
			}
		}
		
		int rkjsflag=0;//是否该班级任课教师标记 
		if(num.equals("0")) {//必须是任课教师
			//判断这个老师是不是该班任课老师	
			for(int i=0;i<vecbjrk.size();i=i+4) {
				if(vecbjrk.get(i).toString().equals(JKJSBH)) {
					rkjsflag=1;
				}
			}
			
			if(rkjsflag==1) {//是该班任课教师,显示任课教师
				//判断哪些教师无法使用
				for(int i=0;i<vecrk.size();i=i+10) {
					teaflag=0;
					if(Integer.parseInt(vecrk.get(i+6).toString())>=Integer.parseInt(vecrk.get(i+4).toString())) {//已安排监考次数=最大监考次数
						teaflag=3;
					}else {
						
						if(vecrk.get(i).toString().equals(vecbzr.get(0).toString())) {//是班主任
							teaflag=4;
						}else {						
							//是该班任课教师，获取教什么课程
							String rkkcbh=vecrk.get(i+8).toString();
							String rkkcmc=vecrk.get(i+9).toString();
							
							if(rkkcbh.equals("")||rkkcmc.indexOf(KCMC)>-1) {//任课老师是教这门课的
								teaflag=5;
							}else {
								if(vecrk.get(i+7).toString().indexOf(SJXL)>-1) {//时间序列冲突
									teaflag=6;
								}else {//去掉已排教师
									int flag=-1;
									String[] teainfo=jkjsarray.split(",");
									for(int k=0;k<teainfo.length;k=k+2) {
										flag=0;
										String teachid=teainfo[k];
										if(vecrk.get(i).toString().equals(teachid)) {
											flag=1;
										}
									}
									if(flag==0) {
										vecrkxs.add(vecrk.get(i).toString());
										vecrkxs.add(vecrk.get(i+1).toString());
									}
								}
							}
						}
					}
				}
				
				if(!JKJSBH.equals("")) {
					sql="select '"+JKJSBH+"' as 工号,'"+JKJSXM+"' as 姓名 union all ";
				}else {
					sql="";
				}
				
				for(int i=0;i<vecrkxs.size();i=i+2) {
					sql+="select '"+vecrkxs.get(i).toString()+"' as 工号,'"+vecrkxs.get(i+1).toString()+"' as 姓名 union all ";
				}
				if(!sql.equals("")) {
					sql=sql.substring(0, sql.length()-10);
					sql2="select 工号,姓名 from ("+sql+") x where 1=1 ";
					if(!ic_teaCode.equals("")) {
						sql2+=" and x.工号 like '%"+ic_teaCode+"%' ";
					}
					if(!ic_teaName.equals("")) {
						sql2+=" and x.姓名 like '%"+ic_teaName+"%' ";
					}
					
				}
				vec=db.getConttexJONSArr(sql2, pageNum, pageSize);
			}
			
		}
		
		if(rkjsflag==0) {//不是该班级任课教师
			//查询班级所属系部
			String sqlxb="SELECT [系部代码] FROM [dbo].[V_基础信息_班级信息表] where [班级编号]='"+MyTools.fixSql(this.getXZBDM())+"' ";
			Vector vecxb=db.GetContextVector(sqlxb);
			if(vecxb!=null&&vecxb.size()>0) {
				if(vecxb.get(0).toString().equals("C01")||vecxb.get(0).toString().equals("C02")) {
					String sqlxbjs="select [教师编号],[系部代码] from V_基础信息_系部教师信息表 order by [教师编号],[系部代码] ";
					Vector vecxbjs=db.GetContextVector(sqlxbjs);
					if(vecxbjs!=null&&vecxbjs.size()>0) {
						//添加到vecjs,有2条记录的用C01，C02替换C00
						int flagjs=0;
						int jsnum=-1;
						for(int i=0;i<vecxbjs.size();i=i+2){
							flagjs=0;
							jsnum=-1;
							for(int j=0;j<vecjs.size();j=j+2) {
								if(vecxbjs.get(i).toString().equals(vecjs.get(j).toString())) {
									flagjs=1;
									jsnum=j;
								}
							}
							if(flagjs==1) {
								vecjs.setElementAt(vecxbjs.get(i+1).toString(), jsnum+1);
							}else {
								vecjs.add(vecxbjs.get(i).toString());
								vecjs.add(vecxbjs.get(i+1).toString());
							}
						}
						
						//获取教师 
						for(int i=0;i<vectea.size();i=i+8) {
							for(int j=0;j<vecjs.size();j=j+2) {
								if(vectea.get(i).toString().equals(vecjs.get(j).toString())&&(vecjs.get(j+1).toString().equals("C01")||vecjs.get(j+1).toString().equals("C02")||vecjs.get(j+1).toString().equals("C00"))) {
									vecqt.add(vectea.get(i).toString());
									vecqt.add(vectea.get(i+1).toString());
									vecqt.add(vectea.get(i+2).toString());
									vecqt.add(vectea.get(i+3).toString());
									vecqt.add(vectea.get(i+4).toString());
									vecqt.add(vectea.get(i+5).toString());
									vecqt.add(vectea.get(i+6).toString());
									vecqt.add(vectea.get(i+7).toString());
									vecqt.add(vecxb.get(0).toString());
								}
							}
						}
						
						//判断哪些教师无法使用
						for(int i=0;i<vecqt.size();i=i+9) {
							teaflag=0;
							if(Integer.parseInt(vecqt.get(i+6).toString())>=Integer.parseInt(vecqt.get(i+4).toString())) {//已安排监考次数=最大监考次数
								teaflag=3;
							}else {
								
								if(vecqt.get(i).toString().equals(vecbzr.get(0).toString())) {//是班主任
									teaflag=4;
								}else {						
									
										if(vecqt.get(i+7).toString().indexOf(SJXL)>-1) {//时间序列冲突
											teaflag=6;
										}else {//去掉已排教师
											int flag=-1;
											String[] teainfo=jkjsarray.split(",");
											for(int k=0;k<teainfo.length;k=k+2) {
												flag=0;
												String teachid=teainfo[k];
												if(vecqt.get(i).toString().equals(teachid)) {
													flag=1;
												}
											}
											if(flag==0) {
												vecrkxs.add(vecqt.get(i).toString());
												vecrkxs.add(vecqt.get(i+1).toString());
											}
										}
									
								}
							}
						}
						
						if(!JKJSBH.equals("")) {
							sql="select '"+JKJSBH+"' as 工号,'"+JKJSXM+"' as 姓名 union all ";
						}else {
							sql="";
						}
						
						for(int i=0;i<vecrkxs.size();i=i+2) {
							sql+="select '"+vecrkxs.get(i).toString()+"' as 工号,'"+vecrkxs.get(i+1).toString()+"' as 姓名 union all ";
						}
						if(!sql.equals("")) {
							sql=sql.substring(0, sql.length()-10);
							sql2="select 工号,姓名 from ("+sql+") x where 1=1 ";
							if(!ic_teaCode.equals("")) {
								sql2+=" and x.工号 like '%"+ic_teaCode+"%' ";
							}
							if(!ic_teaName.equals("")) {
								sql2+=" and x.姓名 like '%"+ic_teaName+"%' ";
							}
							
						}
						vec=db.getConttexJONSArr(sql2, pageNum, pageSize);
						
					}
					
				}else {//非国商部，旅游部
					String sqlxbjs="select [教师编号],[系部代码] from V_基础信息_系部教师信息表 order by [教师编号],[系部代码] ";
					Vector vecxbjs=db.GetContextVector(sqlxbjs);
					if(vecxbjs!=null&&vecxbjs.size()>0) {
						//获取教师
						for(int i=0;i<vectea.size();i=i+8) {
							for(int j=0;j<vecxbjs.size();j=j+2) {//教师编号相同，系部代码相同
								if(vectea.get(i).toString().equals(vecxbjs.get(j).toString())&&vecxbjs.get(j+1).toString().equals(vecxb.get(0).toString())) {
									vecqt.add(vectea.get(i).toString());
									vecqt.add(vectea.get(i+1).toString());
									vecqt.add(vectea.get(i+2).toString());
									vecqt.add(vectea.get(i+3).toString());
									vecqt.add(vectea.get(i+4).toString());
									vecqt.add(vectea.get(i+5).toString());
									vecqt.add(vectea.get(i+6).toString());
									vecqt.add(vectea.get(i+7).toString());
									vecqt.add(vecxb.get(0).toString());
								}
							}
						}
						
						//判断哪些教师无法使用
						for(int i=0;i<vecqt.size();i=i+9) {
							teaflag=0;
							if(Integer.parseInt(vecqt.get(i+6).toString())>=Integer.parseInt(vecqt.get(i+4).toString())) {//已安排监考次数=最大监考次数
								teaflag=3;
							}else {
								
								if(vecqt.get(i).toString().equals(vecbzr.get(0).toString())) {//是班主任
									teaflag=4;
								}else {						
									
										if(vecqt.get(i+7).toString().indexOf(SJXL)>-1) {//时间序列冲突
											teaflag=6;
										}else {//去掉已排教师
											int flag=-1;
											String[] teainfo=jkjsarray.split(",");
											for(int k=0;k<teainfo.length;k=k+2) {
												flag=0;
												String teachid=teainfo[k];
												if(vecqt.get(i).toString().equals(teachid)) {
													flag=1;
												}
											}
											if(flag==0) {
												vecrkxs.add(vecqt.get(i).toString());
												vecrkxs.add(vecqt.get(i+1).toString());
											}
										}
									
								}
							}
						}
						
						if(!JKJSBH.equals("")) {
							sql="select '"+JKJSBH+"' as 工号,'"+JKJSXM+"' as 姓名 union all ";
						}else {
							sql="";
						}
						
						for(int i=0;i<vecrkxs.size();i=i+2) {
							sql+="select '"+vecrkxs.get(i).toString()+"' as 工号,'"+vecrkxs.get(i+1).toString()+"' as 姓名 union all ";
						}
						if(!sql.equals("")) {
							sql=sql.substring(0, sql.length()-10);
							sql2="select 工号,姓名 from ("+sql+") x where 1=1 ";
							if(!ic_teaCode.equals("")) {
								sql2+=" and x.工号 like '%"+ic_teaCode+"%' ";
							}
							if(!ic_teaName.equals("")) {
								sql2+=" and x.姓名 like '%"+ic_teaName+"%' ";
							}
							
						}
						vec=db.getConttexJONSArr(sql2, pageNum, pageSize);
						
					}
					
				}
			}
			
		}
			
		return vec;
	}
	
	
	/**
	 * 查询可用监考教师列表
	 * @date:2017-12-21
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void saveJKJSInfo(String KCMC,String chgteaarray) throws SQLException{
		Vector vec = null;
		String sql = "";
		String sql2 = "";
		String sqljk="";
		Vector vecup=new Vector();
		Vector vecrk=new Vector();
		Vector vecrkxs=new Vector();
		Vector vecqt=new Vector();
		Vector vecjs=new Vector();
		int teaflag=-1;

		//查询监考教师表中所有老师状态
		String sqltea="select [监考教师编号],[监考教师姓名],[教师类型编号],[教师类型名称],[最大监考次数],[是否任课],[已安排监考次数],[已安排监考时间] " + 
								" FROM [dbo].[V_考试管理_监考教师] where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [是否任课]!='0' order by 监考教师编号 ";
		Vector vectea=db.GetContextVector(sqltea);
	
		if(!chgteaarray.equals("")) {
			String[] chgtea=chgteaarray.split(",");
			String sjxltime=chgtea[0];
			String oldteaid=chgtea[1];
			String oldteaname=chgtea[2];
			String newteaid=chgtea[3];
			String newteaname=chgtea[4];
			
			String[] oldteabh=oldteaid.split("，");
			String[] newteabh=newteaid.split("，");
			String yapjksj="";
			
			for(int i=0;i<vectea.size();i=i+8){
				for(int j=0;j<oldteabh.length;j++) {
					if(!oldteabh[j].equals(newteabh[j])) {
						if(vectea.get(i).toString().equals(oldteabh[j])){
							//vectea.setElementAt((Integer.parseInt(vectea.get(i+6).toString())-1)+"", i+6);
							//vectea.setElementAt(yapjksj, i+7);
							yapjksj=vectea.get(i+7).toString();
							yapjksj=(yapjksj+",").replaceAll(sjxltime+",", "");
							if(!yapjksj.equals("")) {
								yapjksj=yapjksj.substring(0, yapjksj.length()-1);
							}							
							sql="update dbo.V_考试管理_监考教师 set [已安排监考次数]='"+(Integer.parseInt(vectea.get(i+6).toString())-1)+""+"',[已安排监考时间]='"+yapjksj+"' "
								+ "where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [监考教师编号]='"+MyTools.fixSql(vectea.get(i).toString())+"' ";
							vecup.add(sql);
						}
						if(vectea.get(i).toString().equals(newteabh[j])){
							//vectea.setElementAt((Integer.parseInt(vectea.get(i+6).toString())+1)+"", i+6);							
							yapjksj=vectea.get(i+7).toString();
							if(vectea.get(i+7).toString().equals("")){
								//vectea.setElementAt(sjxltime, i+7);
								yapjksj=sjxltime;
							}else{
								//vectea.setElementAt(yapjksj+","+sjxltime, i+7);
								yapjksj=yapjksj+","+sjxltime;
							}
							sql="update dbo.V_考试管理_监考教师 set [已安排监考次数]='"+(Integer.parseInt(vectea.get(i+6).toString())+1)+""+"',[已安排监考时间]='"+yapjksj+"' "
									+ "where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [监考教师编号]='"+MyTools.fixSql(vectea.get(i).toString())+"' ";
							vecup.add(sql);
						}
					}
				}		
			}
			
			sql="update [dbo].[V_考试管理_考场安排明细表] set [监考教师编号]='"+newteaid+"',[监考教师姓名]='"+newteaname+"' "
				+ "where [考场安排主表编号]='"+MyTools.fixSql(this.getKCAPZBBH())+"' and [行政班代码]='"+MyTools.fixSql(this.getXZBDM())+"' and [课程名称]='"+MyTools.fixSql(KCMC)+"' and [时间序列]='"+MyTools.fixSql(sjxltime)+"' ";
			vecup.add(sql);
			if(db.executeInsertOrUpdateTransaction(vecup)) {
				this.setMSG("保存成功");
			}else {
				this.setMSG("保存失败");
			}
			
		}
	}
	
	
	/**
	 * 查询班级课程及未排课程
	 * @date:2015-05-30
	 * @author:yeq
	 * @return 班级课程及未排课程信息
	 * @throws SQLException
	 */
	public Vector loadClassKCJS(String teacherCode)throws SQLException{
		Vector resultVec = new Vector();
		String sql = "";
		Vector tempVec = null;
		String jsonStr = "";
//		String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
//		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		//查询课程表
		sql = "SELECT  a.考场安排明细编号,a.考场安排主表编号,a.考试场次编号,a.时间序列,a.课程代码,a.课程名称,a.场地类型,a.场地要求,a.场地名称,a.专业代码,a.专业名称,a.行政班代码,a.行政班名称,a.考试形式,a.监考教师编号,a.监考教师姓名,a.参考学生,a.学生人数,a.试卷类型  " +
			"FROM [dbo].[V_考试管理_考场安排明细表] a left join dbo.V_考试管理_考场安排主表 b on a.考场安排主表编号=b.考场安排主表编号 " +
			"where b.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' " +
			"and a.监考教师编号 like '%" + MyTools.fixSql(teacherCode) + "%'";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		//查询未排课程信息
//		sql = "select distinct a.授课计划明细编号,a.课程名称,a.授课教师姓名,a.授课教师编号,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情,a.节数-a.实际已排节数 as 剩余节数 from V_规则管理_授课计划明细表 a " +
//			"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
//			"where a.状态='1' and b.状态='1' " +
//			//"and a.节数-a.实际已排节数>0 " +
//			"and b.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' " +
//			"and b.行政班代码='" + MyTools.fixSql(this.getGG_XZBDM()) + "'";
		//判断是公共课还是专业课
		//if(this.getAuth().indexOf(pubTeacher) > -1)
		//	sql += " and a.课程类型='01'";
		//if(this.getAuth().indexOf(majorTeacher) > -1)
		//	sql += " and a.课程类型='02'";
//		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		//查询课表备注
//		sql = "select distinct a.备注,isnull(b.总人数,0) from V_排课管理_课程表主表 a left join V_基础信息_班级信息表 b on a.班级编号=b.行政班代码 where a.状态='1' " +
//			"and a.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' " +
//			"and a.行政班代码='" + MyTools.fixSql(this.getGG_XZBDM()) + "'";
//		tempVec = db.GetContextVector(sql);
//		if(tempVec!=null && tempVec.size()>0){
//			resultVec.add(MyTools.StrFiltr(tempVec.get(0)));
//			resultVec.add(MyTools.StrFiltr(tempVec.get(1)));
//		}else{
//			resultVec.add("");
//			resultVec.add("0");
//		}
		
		//查询已进行过调课操作的时间序列
//		String order = "";
//		String day = "";
//		String orderArray[] = new String[0];
//		String tempOrder = "";
//		
//		sql = "select 原计划星期,原计划时间序列,调整后星期,调整后时间序列 from V_调课管理_调课信息主表 where 审核状态='2' " +
//			"and 学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' and 班级编号='" + MyTools.fixSql(this.getGG_XZBDM()) + "'";
//		tempVec = db.GetContextVector(sql);
//		if(tempVec!=null && tempVec.size()>0){
//			for(int i=0; i<tempVec.size(); i+=4){
//				day = MyTools.StrFiltr(tempVec.get(i));
//				orderArray = MyTools.StrFiltr(tempVec.get(i+1)).split(",");
//				
//				for(int j=0; j<orderArray.length; j++){
//					tempOrder = day+orderArray[j];
//					if(!"".equalsIgnoreCase(tempOrder) && order.indexOf(tempOrder)<0){
//						order += tempOrder+",";
//					}
//				}
//				
//				day = MyTools.StrFiltr(tempVec.get(i+2));
//				orderArray = MyTools.StrFiltr(tempVec.get(i+3)).split(",");
//				
//				for(int j=0; j<orderArray.length; j++){
//					tempOrder = day+orderArray[j];
//					if(!"".equalsIgnoreCase(tempOrder) && order.indexOf(tempOrder)<0){
//						order += tempOrder+",";
//					}
//				}
//			}
//		}
//		if(order.length() > 0)
//			order = order.substring(0, order.length()-1);
//		resultVec.add(order);
		
		return resultVec;
	}
	
	/**
	 * 查询班级课程及未排课程
	 * @date:2015-05-30
	 * @author:yeq
	 * @return 班级课程及未排课程信息
	 * @throws SQLException
	 */
	public Vector loadClassKCRM(String roomCode)throws SQLException{
		Vector resultVec = new Vector();
		String sql = "";
		Vector tempVec = null;
		String jsonStr = "";
		String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		//查询课程表
		sql = "SELECT  a.考场安排明细编号,a.考场安排主表编号,a.考试场次编号,a.时间序列,a.课程代码,a.课程名称,a.场地类型,a.场地要求,a.场地名称,a.专业代码,a.专业名称,a.行政班代码,a.行政班名称,a.考试形式,a.监考教师编号,a.监考教师姓名,a.参考学生,a.学生人数,a.试卷类型  " +
			"FROM [dbo].[V_考试管理_考场安排明细表] a left join dbo.V_考试管理_考场安排主表 b on a.考场安排主表编号=b.考场安排主表编号 " +
			"where b.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' " +
			"and a.场地要求 = '" + MyTools.fixSql(roomCode) + "'";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		//查询未排课程信息
		sql = "select distinct a.授课计划明细编号,a.课程名称,a.授课教师姓名,a.授课教师编号,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情,a.节数-a.实际已排节数 as 剩余节数 from V_规则管理_授课计划明细表 a " +
			"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
			"where a.状态='1' and b.状态='1' " +
			//"and a.节数-a.实际已排节数>0 " +
			"and b.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' " +
			"and b.行政班代码='" + MyTools.fixSql(this.getGG_XZBDM()) + "'";
		//判断是公共课还是专业课
		//if(this.getAuth().indexOf(pubTeacher) > -1)
		//	sql += " and a.课程类型='01'";
		//if(this.getAuth().indexOf(majorTeacher) > -1)
		//	sql += " and a.课程类型='02'";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		//查询课表备注
		sql = "select distinct a.备注,isnull(b.总人数,0) from V_排课管理_课程表主表 a left join V_基础信息_班级信息表 b on a.班级编号=b.行政班代码 where a.状态='1' " +
			"and a.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' " +
			"and a.行政班代码='" + MyTools.fixSql(this.getGG_XZBDM()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			resultVec.add(MyTools.StrFiltr(tempVec.get(0)));
			resultVec.add(MyTools.StrFiltr(tempVec.get(1)));
		}else{
			resultVec.add("");
			resultVec.add("0");
		}
		
		//查询已进行过调课操作的时间序列
		String order = "";
		String day = "";
		String orderArray[] = new String[0];
		String tempOrder = "";
		
		sql = "select 原计划星期,原计划时间序列,调整后星期,调整后时间序列 from V_调课管理_调课信息主表 where 审核状态='2' " +
			"and 学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' and 班级编号='" + MyTools.fixSql(this.getGG_XZBDM()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			for(int i=0; i<tempVec.size(); i+=4){
				day = MyTools.StrFiltr(tempVec.get(i));
				orderArray = MyTools.StrFiltr(tempVec.get(i+1)).split(",");
				
				for(int j=0; j<orderArray.length; j++){
					tempOrder = day+orderArray[j];
					if(!"".equalsIgnoreCase(tempOrder) && order.indexOf(tempOrder)<0){
						order += tempOrder+",";
					}
				}
				
				day = MyTools.StrFiltr(tempVec.get(i+2));
				orderArray = MyTools.StrFiltr(tempVec.get(i+3)).split(",");
				
				for(int j=0; j<orderArray.length; j++){
					tempOrder = day+orderArray[j];
					if(!"".equalsIgnoreCase(tempOrder) && order.indexOf(tempOrder)<0){
						order += tempOrder+",";
					}
				}
			}
		}
		if(order.length() > 0)
			order = order.substring(0, order.length()-1);
		resultVec.add(order);
		
		return resultVec;
	}
	
	/**
	 * 查询当前学期已有课程表的班级信息
	 * @date:2015-05-27
	 * @author:yeq
	 * @param level
	 * @param parentCode 专业代码
	 * @return 班级信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queClassTree(String level, String parentCode, String PK_KSZBBH)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		
		sql = "select [教室编号] as id,[教室名称] as text from dbo.V_考试管理_考试教室  where [考试名称]='"+MyTools.fixSql(PK_KSZBBH)+"' order by text,id";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 保存修改的考试信息
	 * @date:2016-06-08
	 * @author:lupengfei
	 * @return 课程表相关信息
	 * @throws SQLException
	 */
	public void saveExamInfo(String teachid,String teachname,String roomid,String roomname)throws SQLException{
		Vector vec = null;
		Vector result = new Vector();
		String sql = " update dbo.V_考试管理_考场安排明细表 set [监考教师编号]='"+teachid+"',[监考教师姓名]='"+teachname+"',[场地要求]='"+roomid+"',[场地名称]='"+roomname+"' " +
			         " where [考场安排明细编号]='"+this.getGG_SKJHMXBH()+"' ";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 导入考试信息
	 * @date:2016-07-12
	 * @author:lupengfei
	 * @return 课程表相关信息
	 * @throws SQLException
	 */
	public void resitInfo()throws SQLException{
		String sql="";
		Vector vec = null;
		sql="delete from V_考试管理_补考名册 where 学年学期编码='"+MyTools.fixSql(this.getXNXQ()+this.getJXXZ())+"' ";
		sql+=" insert into V_考试管理_补考名册 " +
				" SELECT c.学年学期编码,a.学号,a.姓名,e.行政班代码,e.行政班名称,e.行政班简称,b.课程代码,b.课程名称,b.学分,'' as 补考分数,'' as 学生签名 " +
				" FROM [dbo].[V_成绩管理_学生成绩信息表] a,dbo.V_规则管理_授课计划明细表 b,dbo.V_规则管理_授课计划主表 c,dbo.V_学生基本数据子类 d,dbo.V_学校班级数据子类 e " +
				" where a.相关编号=b.授课计划明细编号 and b.授课计划主表编号=c.授课计划主表编号 and a.学号=d.学号 and d.行政班代码=e.行政班代码 and a.总评<60 and a.总评 in ('-2','-3','-4','-5','-10','-12') and c.学年学期编码='"+MyTools.fixSql(this.getXNXQ()+this.getJXXZ())+"' and d.学生状态 in ('01','05') order by b.课程名称 ";
				
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("导入完成");
		}else{
			this.setMSG("导入失败");
		}
	}
	
	public Vector resit_xnxq() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		//String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询当前学期已有课程表的班级信息
	 * @date:2015-05-27
	 * @author:yeq
	 * @param level
	 * @param parentCode 专业代码
	 * @return 班级信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queMajorTree(String level, String parentCode , String sAuth ,String zydm)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课教师
		System.out.println("level:--"+level);
		//获取专业目录
		if("0".equalsIgnoreCase(level)){
			sql = "select distinct c.专业代码 as id,c.专业名称+'('+c.专业代码+')' as text,state='closed' from V_排课管理_课程表主表 a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
				"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) +"'";
			
			if(sAuth.indexOf(majorTeacher) > -1){
				sql += " and c.专业代码='" + MyTools.fixSql(zydm) + "'";
			}
			sql += " order by text";
		}
		//获得班级子节点
		if(level.equalsIgnoreCase("1")){
			sql = "select distinct a.行政班代码 as id,b.行政班名称 as text,state='open' from V_排课管理_课程表主表 a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
				"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) +"' " +
				"and c.专业代码='" + MyTools.fixSql(parentCode) + "' order by a.行政班代码";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryExamSetTree(int pageNum, int page,String parentId) throws SQLException {
		String sql = ""; // 查询用SQL语句
		String sql1 = ""; // 查询用SQL语句
		String sql2 = ""; // 查询用SQL语句
		String sql3 = ""; // 查询用SQL语句
		String sql4 = ""; // 查询用SQL语句
		String sql5="";
		Vector vec = null; // 结果集
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		String beginTime = "";
		String endTime = "";
		String user1="";
		String user2="";
		String zydm="";
		
		sql4="select 专业代码 from dbo.V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getiUSERCODE()) + "@%'";
		vec4=db.GetContextVector(sql4);
		if(vec4!=null&&vec4.size()>0){
			if(this.iUSERCODE.equals("post")){
				
			}else{
				for(int i=0;i<vec4.size();i++){
					zydm+="'"+vec4.get(i).toString()+"',";
				}
				zydm=zydm.substring(0, zydm.length()-1);
				user1="and c.专业代码 in ("+zydm+") ";
				user2="and b.专业代码 in ("+zydm+") ";
			}
		}
		
		sql += "select c.专业代码 as id,'' as _parentId,c.专业名称+c.专业代码 as 名称,'1' as px,case when (select count(*) from V_学校班级数据子类 where c.专业代码=专业代码 )=0 then 'open' else 'closed' end as state from V_专业基本信息数据子类 c where len(c.专业代码)>4 "+user1 ;
		sql += " union select '9999990' as id,'' as _parentId,'分层班' as 名称,'2' as px,'closed' as state " +
			   " union select '9999999' as id,'' as _parentId,'选修课' as 名称,'3' as px,'closed' as state " +
			  " union " +
			  "select b.行政班代码 as id,b.专业代码 as _parentId,b.行政班名称 as 名称,'3' as px,'open' as state " +
			  "from V_专业基本信息数据子类 a right join V_学校班级数据子类 b on a.专业代码=b.专业代码 " +
			  //"left join V_规则管理_授课计划主表 c on 1=1 " +
			  "where 1=1 and b.专业代码='"+MyTools.fixSql(parentId)+"'"+user2 ;
		sql5=" select id,_parentId,名称,px,state from ("+sql+") e order by px,名称 ";
		
		if("".equalsIgnoreCase(XNXQ)){
			sql3="select distinct 学年+学期+教学性质 as termid,学年+学期,教学性质 as jxxz FROM V_规则管理_学年学期表 where 1=1 order by termid desc";
			vec3=db.GetContextVector(sql3);
			if(vec3!=null&&vec3.size()>0){
				this.setXNXQ(vec3.get(1).toString());		
				this.setJXXZ(vec3.get(2).toString());
			}
		}

//		暂时去掉查询条件20151028
//		if(!"".equalsIgnoreCase(XNXQ)){
//			sql1 = "select CONVERT(varchar(6),学期开始时间,112) as 学期开始时间," +
//					"CONVERT(varchar(6),学期结束时间,112) as 学期结束时间 from V_规则管理_学年学期表 " +
//					"where 学年学期编码='" + MyTools.fixSql(XNXQ) + MyTools.fixSql(JXXZ) + "'";
//			vec1 = db.GetContextVector(sql1);
//			if (vec1 != null && vec1.size() > 0) {
//				beginTime = MyTools.fixSql(MyTools.StrFiltr(vec1.get(0)));
//				endTime = MyTools.fixSql(MyTools.StrFiltr(vec1.get(1)));
//				sql += " and b.建班年月 between '"+ beginTime +"' and '"+ endTime +"'";
//			}else{
//				sql += " and b.建班年月 between 9999 and 99";
//			}
//		}else{
//			sql2 = "select CONVERT(varchar(6),学期开始时间,112) as 学期开始时间," +
//					"CONVERT(varchar(6),学期结束时间,112) as 学期结束时间 from V_规则管理_学年学期表 " +
//					"where 学年学期编码=(select max(学年学期编码) from V_规则管理_学年学期表)";
//			vec2 = db.GetContextVector(sql2);
//			if (vec2 != null && vec2.size() > 0) {
//				beginTime = MyTools.fixSql(MyTools.StrFiltr(vec2.get(0)));
//				endTime = MyTools.fixSql(MyTools.StrFiltr(vec2.get(1)));
//				sql += " and b.建班年月 between '"+ beginTime +"' and '"+ endTime +"'";
//			}
//		}
//		sql += " and c.学年学期编码='" + MyTools.fixSql(XNXQ) + MyTools.fixSql(JXXZ) + "'";
		
		vec = db.getConttexJONSArr(sql5, 0, 0);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取学科名称下拉框
	 * @date:2017-04-17
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector BJMCCombobox() throws SQLException{
		Vector vec = null;
		String sql="";
		
		String xn=this.getGG_XNXQBM().substring(2,4);
		sql=" select comboValue,comboName from (" +
			" select '' as comboValue,'请选择' as comboName,'' as 年级代码,'1' as px " +
			" union " +
			" select [行政班代码] as comboValue,[行政班名称] as comboName,年级代码,'2' as px FROM [dbo].[V_学校班级数据子类]" +
			" where 年级代码 in ('"+Integer.parseInt(xn)+"1"+"','"+(Integer.parseInt(xn)-1)+"1"+"','"+(Integer.parseInt(xn)-2)+"1"+"')  " +
			" ) x order by px,年级代码 desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学科名称下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector KCMCCombobox() throws SQLException{
		Vector vec = null;
		String zydm="";
		String kcbh="";
		String sql="";
		String sql2="";
		System.out.println(this.getGG_XZBDM());
		if(this.getGG_XZBDM().equals("9999999")){
			sql=" SELECT distinct 课程代码 AS comboValue,课程名称 AS comboName FROM dbo.V_规则管理_选修课授课计划主表  where [学年学期编码] = '"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
		}else if(this.getGG_XZBDM().equals("9999990")){
			sql=" SELECT distinct 课程代码 AS comboValue,课程名称 AS comboName FROM dbo.V_规则管理_分层课程信息表  where [学年学期编码] = '"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
		}else{
			/*sql2="select 专业代码 from dbo.V_学校班级数据子类 where 行政班代码 = '"+MyTools.fixSql(this.getGG_XZBDM())+"' ";
			vec=db.GetContextVector(sql2);
			if(vec!=null&vec.size()>0){
				zydm=vec.get(0).toString();
			}
	
			//System.out.println("zydm----------------------"+this.XZBDM.indexOf("_"));
			if(zydm.length()==6){
				zydm=zydm+"00";
			}
			sql = " SELECT  '' AS comboValue,'请选择' AS comboName FROM V_课程数据子类 union " +
						 " SELECT 课程号 AS comboValue,课程名称+'('+课程号+')' AS comboName FROM V_课程数据子类 where substring(课程号,0,4)='000' union "+
						 " SELECT 课程号 AS comboValue,课程名称+'('+课程号+')' AS comboName FROM V_课程数据子类  where 课程号 like '"+MyTools.fixSql(zydm)+"%'";*/
			sql = " SELECT  '' AS comboValue,'请选择' AS comboName union " +
				  " SELECT 课程代码 AS comboValue,课程名称 AS comboName FROM [dbo].[V_规则管理_授课计划明细表] a " + 
				  " left join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " + 
				  " where b.学年学期编码 = '"+MyTools.fixSql(this.getGG_XNXQBM())+"' and b.行政班代码='"+MyTools.fixSql(this.getGG_XZBDM())+"' ";
		
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取考试周期下拉框
	 * @date:2016-11-30
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector KSZQCombobox(String DRxnxq) throws SQLException{
		Vector vec = null;

		String sql="";
		sql = 	"SELECT [考场安排主表编号] as comboValue,[考试名称] as comboName  FROM [dbo].[V_考试管理_考场安排主表] " +
				"where [学年学期编码]='"+MyTools.fixSql(DRxnxq)+"' and [考试类型]='1'  ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 新建考试课程
	 * @date:2015-06-03
	 * @author:wangzh
	 * @throws SQLException
	 */
	public void saveNewExam(String KCDM,String KCMC) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String ksbh="";
		String sql="";
		String sql2="";
		String sql3="";
		String sql4="";
		String maxNewId="";
		
		sql4="select count(*) from dbo.V_考试管理_考试设置 where 授课计划明细编号='SKJHMX_80000000001'";
		if(db.getResultFromDB(sql4)){
			sql3="select max(cast(SUBSTRING(授课计划明细编号,8,11) as bigint)) from dbo.V_考试管理_考试设置";
			vec3 = db.GetContextVector(sql3);
			if (vec3 != null && vec3.size() > 0) {
				maxNewId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec3.get(0))))+1);
				ksbh="SKJHMX_"+maxNewId;//设置编号
			}
		}else{
			ksbh="SKJHMX_80000000001";//设置编号
		}
		
		if(!this.getGG_XZBDM().equals("9999999")){
			sql2="select a.[专业代码],b.[专业名称],a.[行政班名称] from dbo.V_学校班级数据子类 a,dbo.V_专业基本信息数据子类 b where a.专业代码=b.专业代码 and a.[行政班代码]='"+MyTools.fixSql(this.getGG_XZBDM())+"' ";
			vec2=db.GetContextVector(sql2);
			if(vec2!=null&&vec2.size()>0){
				sql="insert into dbo.V_考试管理_考试设置 ([授课计划明细编号],[是否考试],[课程代码],[课程名称],[专业代码],[专业名称],[场地类型],[场地要求],[上课周期],[考试场次编号],[期中期末],[学年学期编码],[行政班代码],[行政班名称],[考试形式],[监考教师],[参考学生],[试卷类型]) values (" +
					"'"+ksbh+"'," +//授课计划明细编号
					"'2'," +//是否考试
					"'"+KCDM+"'," +//课程代码
					"'"+KCMC+"'," +//课程名称
					"'"+vec2.get(0).toString()+"'," +//专业代码
					"'"+vec2.get(1).toString()+"'," +//专业名称
					"'1'," +//场地类型
					"''," +//场地要求
					"''," +//上课周期
					"'KSCCBH_"+ksbh.substring(7,ksbh.length())+"'," +//考试场次编号
					"'"+this.getQZQM()+"'," +//期中期末
					"'"+this.getXNXQ()+this.getJXXZ()+"'," +//学年学期编码
					"'"+this.getGG_XZBDM()+"'," +//行政班代码
					"'"+vec2.get(2).toString()+"'," +//行政班名称
					"'3'," +//考试形式
					"''," +//监考教师
					"'"+ksbh+"'," +//参考学生
					"'')" ;//试卷类型
			}
		}else{//新建的是选修课考试
			sql="insert into dbo.V_考试管理_考试设置 ([授课计划明细编号],[是否考试],[课程代码],[课程名称],[专业代码],[专业名称],[场地类型],[场地要求],[上课周期],[考试场次编号],[期中期末],[学年学期编码],[行政班代码],[行政班名称],[考试形式],[监考教师],[参考学生],[试卷类型]) values (" +
					"'"+ksbh+"'," +//授课计划明细编号
					"'2'," +//是否考试
					"'"+KCDM+"'," +//课程代码
					"'"+KCMC+"'," +//课程名称
					"''," +//专业代码
					"''," +//专业名称
					"'1'," +//场地类型
					"''," +//场地要求
					"''," +//上课周期
					"'KSCCBH_"+ksbh.substring(7,ksbh.length())+"'," +//考试场次编号
					"'"+this.getQZQM()+"'," +//期中期末
					"'"+this.getXNXQ()+this.getJXXZ()+"'," +//学年学期编码
					"''," +//行政班代码
					"''," +//行政班名称
					"'3'," +//考试形式
					"''," +//监考教师
					"'"+ksbh+"'," +//参考学生
					"'')" ;//试卷类型
		}
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("新建成功");
		}else{
			this.setMSG("新建失败");
		}
	}
	
	/**
	 * 未排课程
	 * @date:2016-10-12
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector showNotCourse() throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="select distinct c.行政班代码,c.行政班名称,c.学年学期编码," +
		 		"stuff((select '｜'+d.授课计划明细编号 from (select b.行政班代码,c.行政班名称,b.学年学期编码,a.授课计划明细编号,'2' as 是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 where b.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' and a.[实际已排节数]=0 ) d where c.行政班代码=d.行政班代码 and c.行政班名称=d.行政班名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称  for xml path('')),1,1,'') as 授课计划明细编号," +
		 		"c.是否考试,c.课程代码,c.课程名称,c.专业代码,c.专业名称,c.场地要求," +
		 		"stuff((select '｜'+d.上课周期 from (select b.行政班代码,c.行政班名称,b.学年学期编码,a.授课计划明细编号,'2' as 是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 where b.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' and a.[实际已排节数]=0 ) d where c.行政班代码=d.行政班代码 and c.行政班名称=d.行政班名称 and c.学年学期编码=d.学年学期编码 and c.是否考试=d.是否考试 and c.课程代码=d.课程代码 and c.专业代码=d.专业代码 and c.专业名称=d.专业名称  for xml path('')),1,1,'') as 上课周期," +
		 		"c.期中期末,c.考试形式  from (select b.行政班代码,c.行政班名称,b.学年学期编码,a.授课计划明细编号,'2' as 是否考试,a.课程代码,a.课程名称,b.专业代码,b.专业名称,'' as 场地要求,a.授课周次详情 as 上课周期,'0' as 期中期末,a.考试形式  from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 where b.学年学期编码='" + MyTools.fixSql(this.getGG_XNXQBM()) + "' and a.[实际已排节数]=0 ) c " +
		 		"order by c.行政班代码 ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 未选考试形式
	 * @date:2016-10-26
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector showNotKSXS() throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="select a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,a.专业代码,a.专业名称,a.场地类型,c.名称,a.场地要求,a.上课周期,a.考试场次编号,a.期中期末,a.学年学期编码,a.行政班代码,a.行政班名称,b.编号,b.考试形式,a.监考教师,a.参考学生,a.试卷类型 from dbo.V_考试管理_考试设置 a,dbo.V_考试形式 b,dbo.V_基础信息_教室类型 c where a.考试形式=b.编号 and a.场地类型=c.编号 and a.学年学期编码='"+MyTools.fixSql(this.getGG_XNXQBM())+"'  and (a.考试形式='' or a.考试形式 is null) order by 专业名称,行政班名称,课程名称 ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * @author:maowei
	 * @param pageNum
	 * @param pageSize
	 * @param GS_KCMC
	 * @param GS_ZYMC
	 * @param GS_KSXS
	 * @param GS_XZBMC
	 * @return
	 * @throws SQLException
	 */
	public Vector showAllKSXS(int pageNum,int pageSize, String GS_KCMC, String GS_ZYMC, String GS_KSXS, String GS_XZBMC) throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="select distinct a.授课计划明细编号,a.是否考试,a.课程代码,a.课程名称,a.专业代码,a.专业名称,a.场地类型,c.名称,a.场地要求,a.上课周期,a.考试场次编号,a.期中期末,d.考试名称,a.学年学期编码," +
				"a.行政班代码,a.行政班名称,b.编号,b.考试形式,a.监考教师,a.参考学生,a.试卷类型 from dbo.V_考试管理_考试设置 a,dbo.V_考试形式 b,dbo.V_基础信息_教室类型 c,dbo.V_考试管理_考场安排主表 d " +
				"where a.考试形式=b.编号 and a.场地类型=c.编号 and a.期中期末=d.考场安排主表编号 and a.学年学期编码='"+MyTools.fixSql(this.getGG_XNXQBM())+"' " ;
		if(!this.getQZQM().equals("0")){
			sql+=" and a.期中期末='" + MyTools.fixSql(this.getQZQM()) + "' ";
		}
		if(!"".equalsIgnoreCase(MyTools.StrFiltr(GS_KCMC))){
			sql+=" and a.课程名称 like'%"+MyTools.StrFiltr(GS_KCMC)+"%'";
		}
		if(!"0".equalsIgnoreCase(MyTools.StrFiltr(GS_ZYMC))&!"".equalsIgnoreCase(MyTools.StrFiltr(GS_ZYMC))){
			sql+=" and a. 专业代码  = '"+MyTools.StrFiltr(GS_ZYMC)+"'";
		}
		if(!"".equalsIgnoreCase(MyTools.StrFiltr(GS_KSXS))&!"-1".equalsIgnoreCase(MyTools.StrFiltr(GS_KSXS))){
			sql+=" and b.编号 = '"+MyTools.StrFiltr(GS_KSXS)+"'";
		}
		if("0".equalsIgnoreCase(MyTools.StrFiltr(GS_KSXS))){
			sql+=" and (a.考试形式='' or a.考试形式 is null) ";
		}
		if(!"".equalsIgnoreCase(MyTools.StrFiltr(GS_XZBMC))){
			sql+=" and a.行政班名称  like '%"+MyTools.StrFiltr(GS_XZBMC)+"%'";
		}
		sql+="order by 专业名称,行政班名称,课程名称 ";
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 补考查询
	 * @date:2016-11-14
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public String showBUKAO(int pageNum,int pageSize, String GS_KCMC, String GS_KSXS, String GS_XZBMC) throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sqlkssz = "";
		Vector vec = null;
		Vector vec3 = null;
		Vector vecins = new Vector();
		String xn=this.getGG_XNXQBM().substring(0,4);

		//从学期考试设置获取合并信息
		String sqlchkkscc="select count(*) from V_考试管理_补考合并信息 where 考试学期='"+this.getGG_XNXQBM()+"'";
		Vector vecchkkscc=db.GetContextVector(sqlchkkscc);
		if(vecchkkscc.get(0).toString().equals("0")){
			String sqlkscc="select [授课计划明细编号],[考试场次编号] from dbo.V_考试管理_考试设置 where [考试场次编号] in (" +
							"select [考试场次编号] from ( " +
							"SELECT a.[考试场次编号],COUNT(*) as samenum FROM [V_考试管理_考试设置] a,dbo.V_考试管理_考场安排主表 b where a.期中期末=b.考场安排主表编号 and  a.[学年学期编码]='"+this.getGG_XNXQBM()+"' and b.考试类型='1' group by a.[考试场次编号] ) e " +
							"where samenum>1 ) " +
							"group by [考试场次编号],[授课计划明细编号]";
			Vector veckscc=db.GetContextVector(sqlkscc);
			String skjhbhkscc="";
			String sqlinskscc="";
			Vector vecinskscc=new Vector();
			if(veckscc!=null&&veckscc.size()>0){
				skjhbhkscc+=veckscc.get(0).toString();
				for(int i=2;i<veckscc.size();i=i+2){
					if(veckscc.get(i-1).toString().equals(veckscc.get(i+1).toString())){
						skjhbhkscc+="@"+veckscc.get(i).toString();
					}else{
						sqlinskscc="insert into V_考试管理_补考合并信息 ([考试学期],[授课计划明细编号]) values ('"+this.getGG_XNXQBM()+"','"+skjhbhkscc+"') ";
						vecinskscc.add(sqlinskscc);
						skjhbhkscc=veckscc.get(i).toString();
					}
					if(i==(veckscc.size()-2)){
						sqlinskscc="insert into V_考试管理_补考合并信息 ([考试学期],[授课计划明细编号]) values ('"+this.getGG_XNXQBM()+"','"+skjhbhkscc+"') ";
						vecinskscc.add(sqlinskscc);
					}
				}
				db.executeInsertOrUpdateTransaction(vecinskscc);
			}
		}
		//------------------------------------------------------------------------------------
		
		//查询授课计划是空的课程，把相关编号插入到dbo.V_考试管理_考试设置 
		sqlkssz="select distinct 相关编号,授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式, " +
				"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from ( " +
				"select a.相关编号,case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when b.来源类型='3' then l.考试形式 else k.考试形式 end as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when e.年级代码=(cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式 k on f.考试形式=k.编号 left join dbo.V_考试形式 l on g.考试形式=l.编号 " +
				"where d.学生状态 in ('01','05','07','08') and 成绩状态='1' and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1')) as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')  and 授课计划明细编号 is null " +
				"group by 相关编号,授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式,系名称";
		Vector veckssz=db.GetContextVector(sqlkssz);
		if(veckssz!=null&&veckssz.size()>0){
			for(int i=0;i<veckssz.size();i=i+9){
				sql2="insert into V_考试管理_考试设置 ([授课计划明细编号],[是否考试],[课程名称],[学年学期编码],[行政班名称]) " +
					 "values ('"+veckssz.get(i).toString()+"','2','"+veckssz.get(i+3).toString()+"','"+veckssz.get(i+2).toString()+"','"+veckssz.get(i+8).toString()+"') ";
				vecins.add(sql2);
			}
			db.executeInsertOrUpdateTransaction(vecins);
		}
		
		
		sql="select 授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式编号,考试形式,系名称,行政班简称,行政班名称 from ( " +
			"select distinct 学号,姓名,授课计划明细编号,学年学期,课程名称,考试形式编号,考试形式," +
			"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 " +
			"from (select case when b.来源类型='3' then g.授课计划明细编号 when b.来源类型='2' then m.编号  else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when b.来源类型='3' then l.编号 else k.编号 end as 考试形式编号,case when b.来源类型='3' then l.考试形式 else k.考试形式 end as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) when '4' then (select t1.学分 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号 where t.分层班编号=b.相关编号) else 0 end as 学分,case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩  " +
			"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码  left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式  k on f.考试形式=k.编号 left join dbo.V_考试形式 l on g.考试形式=l.编号 left join dbo.V_排课管理_添加课程信息表 m on a.相关编号=m.编号 " +
			"where c.学年学期编码='"+this.getGG_XNXQBM()+"' and d.学生状态 in ('01','05','07','08') and 成绩状态='1' " +
			"and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1') " + //取15,16年级，下一行去掉整班未登分
			"and a.相关编号 not in (select [选修课授课计划编号] from [V_选修课与智慧树对应表]) "+
			"and a.相关编号 not in (" +
					"select 相关编号 from (select distinct a.相关编号,sum(case when 成绩<>'' then 1 else 0 end) as 已登分人数 " +
					"from V_成绩管理_登分教师信息表 a left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 left join (select ta.学号,ta.相关编号," +
					"case when (case when ta.平时 in ('-1','-5') then '' else ta.平时 end)<>'' then ta.平时 " +
					"when (case when ta.期中 in ('-1','-5') then '' else ta.期中 end)<>'' then ta.期中 " +
					"when (case when ta.实训 in ('-1','-5') then '' else ta.实训 end)<>'' then ta.实训 " +
					"when (case when ta.期末 in ('-1','-5') then '' else ta.期末 end)<>'' then ta.期末 " +
					"when (case when ta.总评 in ('-1','-5') then '' else ta.总评 end)<>'' then ta.总评 " +
					"when (case when ta.重修1 in ('-1','-5') then '' else ta.重修1 end)<>'' then ta.重修1 " +
					"when (case when ta.重修2 in ('-1','-5') then '' else ta.重修2 end)<>'' then ta.重修2 " +
					"when (case when ta.补考 in ('-1','-5') then '' else ta.补考 end)<>'' then ta.补考 " +
					"when (case when ta.大补考 in ('-1','-5') then '' else ta.大补考 end)<>'' then ta.大补考 else '' end as 成绩 " +
					"from V_成绩管理_学生成绩信息表 ta left join V_成绩管理_科目课程信息表 tb on ta.科目编号=tb.科目编号 where ta.状态='1' and ta.成绩状态='1' and tb.学年学期编码='"+this.getGG_XNXQBM()+"') c on c.相关编号=a.相关编号 " +
					"where a.状态='1' and b.状态='1' and b.学年学期编码='"+this.getGG_XNXQBM()+"' group by a.相关编号,a.行政班代码,a.来源类型) as t where t.已登分人数=0 " +
					") " +
			") as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-2','-6','-7','-8','-9','-11','-13','-15')  " +		        
		    ") x where 1=1 " ;	
			
		if(!"".equalsIgnoreCase(MyTools.StrFiltr(GS_KCMC))){
			sql+=" and 课程名称 like'%"+MyTools.StrFiltr(GS_KCMC)+"%'";
		}

		if("请选择".equalsIgnoreCase(MyTools.StrFiltr(GS_KSXS))){
			
		}else if("未选考试形式".equalsIgnoreCase(MyTools.StrFiltr(GS_KSXS))){
			sql+=" and (考试形式='' or 考试形式 is null) ";
		}else{
			sql+=" and 考试形式='"+MyTools.StrFiltr(GS_KSXS)+"' ";
		}
		if(!"".equalsIgnoreCase(MyTools.StrFiltr(GS_XZBMC))){
			sql+=" and 行政班简称  like '%"+MyTools.StrFiltr(GS_XZBMC)+"%'";
		}
		sql+="group by 授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式编号,考试形式,系名称 order by 课程名称,行政班简称 ";
		
		vec=db.GetContextVector(sql);
		
		String skjhmxs="";
		sql3="SELECT count(*) FROM [dbo].[V_考试管理_补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
		if(db.getResultFromDB(sql3)){
			sql3="SELECT [授课计划明细编号] FROM [dbo].[V_考试管理_补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
			vec3=db.GetContextVector(sql3);
			for(int j=0;j<vec3.size();j++){
				skjhmxs+=vec3.get(j).toString()+"@";
			}	
		}else{
			
		}
		
		Vector vecr=new Vector();
		Vector vecs=new Vector();
		Vector vect=new Vector();
		for(int i=0;vec.size()>1;){
			String skjhmx=vec.get(i).toString();
			String xnxqbm=vec.get(i+1).toString();
			String kcmc=vec.get(i+2).toString();
			String rs=vec.get(i+3).toString();
			String ksxsbh=vec.get(i+4).toString();
			String ksxs=vec.get(i+5).toString();
			String xmc=vec.get(i+6).toString();
			String xzbjc=vec.get(i+7).toString();
			String xzbmc=vec.get(i+8).toString();
			//在合并信息中,保存到vecs
			if(!skjhmxs.equals("")&&skjhmxs.indexOf(vec.get(i).toString())>-1){
				vecs.add(skjhmx);//授课计划明细编号 1
				vecs.add(xnxqbm);//学年学期 2
				vecs.add(kcmc);//课程名称 3
				vecs.add(rs);//人数 4
				vecs.add(ksxsbh);//考试形式编号 5
				vecs.add(ksxs);//考试形式 6
				vecs.add(xmc);//系名称 7
				vecs.add(xzbjc);//行政班简称 8
				vecs.add(xzbmc);//行政班名称 9
			}else{	
				for(int j=9;j<vec.size();j=j+9){
					//是选修课
					if(vec.get(i+2).toString().indexOf("选修")>-1){
						//课程名称 相同 合并
						if(vec.get(i+2).toString().equals(vec.get(j+2).toString())){
							skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
							//取学年学期大的 2
							if(Integer.parseInt(vec.get(i+1).toString())>Integer.parseInt(vec.get(j+1).toString())){
								xnxqbm=vec.get(i+1).toString();
							}else{
								xnxqbm=vec.get(j+1).toString();
							}
							//课程名称 3
							rs+="$"+vec.get(j+3).toString();//人数 4
							//考试形式 5
							xmc+="$"+vec.get(j+6).toString();//系名称 7
							xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
							xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
							
							for(int k=0;k<9;k++){
								vec.remove(j);
							}
							j=j-9;
						}
						
					}else{
						//课程名称,系名称,班级简称 相同 合并
						if(vec.get(i+2).toString().equals(vec.get(j+2).toString())&&vec.get(i+6).toString().equals(vec.get(j+6).toString())&&(vec.get(i+7).toString().equals(vec.get(j+7).toString())||vec.get(i+7).toString().substring(2,4).equals(vec.get(j+7).toString().substring(2,4)))){
							skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
							//取学年学期大的 2
							if(Integer.parseInt(vec.get(i+1).toString())>Integer.parseInt(vec.get(j+1).toString())){
								xnxqbm=vec.get(i+1).toString();
							}else{
								xnxqbm=vec.get(j+1).toString();
							}
							//课程名称 3
							rs+="$"+vec.get(j+3).toString();//人数 4
							//考试形式 5
							//系名称 6
							xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
							xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
							
							for(int k=0;k<9;k++){
								vec.remove(j);
							}
							j=j-9;
						}	
					}
				}
				
				vecr.add(skjhmx);//授课计划明细编号 1
				vecr.add(xnxqbm);//学年学期 2
				vecr.add(kcmc);//课程名称 3
				vecr.add(rs);//人数 4
				vecr.add(ksxsbh);//考试形式 5
				vecr.add(ksxs);//考试形式 6
				vecr.add(xmc);//系名称 7
				vecr.add(xzbjc);//行政班简称 8
				vecr.add(xzbmc);//行政班名称 9			
			}
			for(int k=0;k<9;k++){
				vec.remove(i);
			}
		}
		
//		for(int i=0;i<vecs.size();i=i+9){
//			System.out.println("s:"+"|"+vecs.get(i)+"|"+vecs.get(i+1)+"|"+vecs.get(i+2)+"|"+vecs.get(i+3)+"|"+vecs.get(i+4)+"|"+vecs.get(i+5)+"|"+vecs.get(i+6)+"|"+vecs.get(i+7)+"|"+vecs.get(i+8));
//		}
		
		//在合并信息中的合并
		if(vec3!=null&&vec3.size()>0){
			for(int i=0;i<vec3.size();i++){
				String sskjhmx="";
				String sxnxqbm="";
				String skcmc="";
				String srs="";
				String sksxsbh="";
				String sksxs="";
				String sxmc="";
				String sxzbjc="";
				String sxzbmc="";
				for(int j=0;j<vecs.size();j=j+9){
					if(vec3.get(i).toString().indexOf(vecs.get(j).toString())>-1){
						
						sxnxqbm+=vecs.get(j+1).toString()+"@";
						skcmc+=vecs.get(j+2).toString()+"@";
						srs+=vecs.get(j+3).toString()+"@";
						sksxsbh=vecs.get(j+4).toString();
						sksxs=vecs.get(j+5).toString();
						sxmc+=vecs.get(j+6).toString()+"@";
						sxzbjc+=vecs.get(j+7).toString()+"@";
						sxzbmc+=vecs.get(j+8).toString()+"@";	
					}	
				}
				
				sskjhmx=vec3.get(i).toString();
				int max=0;
				if(!sxnxqbm.equals("")){
					sxnxqbm=sxnxqbm.substring(0,sxnxqbm.length()-1);
					
					String[] xq=sxnxqbm.split("@");
					max=Integer.parseInt(xq[0]);
					for(int k=0;k<xq.length;k++){
						if(max<Integer.parseInt(xq[k])){
							max=Integer.parseInt(xq[k]);
						}
					}
				}
				if(!skcmc.equals("")){
					skcmc=skcmc.substring(0,skcmc.length()-1);
				}
				if(!srs.equals("")){
					srs=srs.substring(0,srs.length()-1);
				}
				if(!sxmc.equals("")){
					sxmc=sxmc.substring(0,sxmc.length()-1);
				}
				if(!sxzbjc.equals("")){
					sxzbjc=sxzbjc.substring(0,sxzbjc.length()-1);
				}
				if(!sxzbmc.equals("")){
					sxzbmc=sxzbmc.substring(0,sxzbmc.length()-1);
				}
				
				if(!sxnxqbm.equals("")){
					vect.add(sskjhmx);//授课计划明细编号 1
					vect.add(max+"");//学年学期 2
					vect.add(skcmc);//课程名称 3
					vect.add(srs);//人数 4
					vect.add(sksxsbh);//考试形式 5
					vect.add(sksxs);//考试形式 6
					vect.add(sxmc);//系名称 7
					vect.add(sxzbjc);//行政班简称 8
					vect.add(sxzbmc);//行政班名称 9
				}
			}
		}
		
		for(int i=0;i<vecr.size();i++){
			vect.add(vecr.get(i).toString());
		}
		
//		for(int i=0;i<vect.size();i=i+9){
//			System.out.println("t:"+"|"+vect.get(i)+"|"+vect.get(i+1)+"|"+vect.get(i+2)+"|"+vect.get(i+3)+"|"+vect.get(i+4)+"|"+vect.get(i+5)+"|"+vect.get(i+6)+"|"+vect.get(i+7)+"|"+vect.get(i+8));
//		}
		
		String revec="";
		int total=0;
		if(pageNum*pageSize*9>vect.size()){
			total=vect.size();
		}else{
			total=pageNum*pageSize*9;
		}
		 
		for(int m=(pageNum-1)*pageSize*9;m<total;m=m+9){
			revec+="{\"授课计划明细编号\":\""+vect.get(m).toString()+"\",\"学年学期\":\""+vect.get(m+1).toString()+"\",\"课程名称\":\""+vect.get(m+2).toString()+"\",\"人数\":\""+vect.get(m+3).toString()+"\",\"考试形式编号\":\""+vect.get(m+4).toString()+"\",\"考试形式\":\""+vect.get(m+5).toString()+"\",\"系名称\":\""+vect.get(m+6).toString()+"\",\"行政班简称\":\""+vect.get(m+7).toString()+"\",\"行政班名称\":\""+vect.get(m+8).toString()+"\",\"MSG\":\""+this.getMSG()+"\",\"pageNum\":\""+pageNum+"\",\"pageSize\":\""+pageSize+"\"},";
		}
		if(revec.equals("")){
			revec="[]";
		}else{
			revec="["+revec.substring(0, revec.length()-1)+"]";
		}
		this.setTATOL(vect.size()/9+"");
		//System.out.println("revec:--"+pageNum+"|"+pageSize);
		return revec;
	}
	
	/**
	 * 大补考查询
	 * @date:2016-11-14
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public String showDABUKAO(int pageNum,int pageSize, String GS_KCMC, String GS_KSXS, String GS_XZBMC) throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sqlkssz = "";
		Vector vec = new Vector();
		Vector vec3 = null;
		Vector vec4 = null;
		Vector vec5 = null;
		Vector vecins = new Vector();
		String xn=this.getGG_XNXQBM().substring(0,4);
		
		//查询授课计划是空的课程，把相关编号插入到dbo.V_考试管理_考试设置 
		sqlkssz="select distinct 相关编号,授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式, " +
				"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from ( " +
				"select a.相关编号,case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when b.来源类型='3' then l.考试形式 else k.考试形式 end as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when e.年级代码=(cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式 k on f.考试形式=k.编号 left join dbo.V_考试形式 l on g.考试形式=l.编号 " +
				"where d.学生状态 in ('01','05','07','08') and 成绩状态='1' and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1')) as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')  and 授课计划明细编号 is null " +
				"group by 相关编号,授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式,系名称";
		Vector veckssz=db.GetContextVector(sqlkssz);
		if(veckssz!=null&&veckssz.size()>0){
			for(int i=0;i<veckssz.size();i=i+9){
				sql2="insert into V_考试管理_考试设置 ([授课计划明细编号],[是否考试],[课程名称],[学年学期编码],[行政班名称]) " +
					 "values ('"+veckssz.get(i).toString()+"','2','"+veckssz.get(i+3).toString()+"','"+veckssz.get(i+2).toString()+"','"+veckssz.get(i+8).toString()+"') ";
				vecins.add(sql2);
			}
			db.executeInsertOrUpdateTransaction(vecins);
		}
		
		//查询考试信息
		sql="select distinct 授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式编号,考试形式, " +
			"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from ( " +
			"select case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when f.考试形式 in ('1','2','3','10') then '1' when f.考试形式 in ('4','5') then '4' when f.考试形式 in ('9','11','12','13','16') then '9' else f.考试形式 end as 考试形式编号,k.考试形式 as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when e.年级代码=(cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
			"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式大补考 k on f.考试形式=k.编号  " +
			"where d.学生状态 in ('01','05','08') and 成绩状态='1' " +
			"and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1') " +
			") as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " ;

		if(!"".equalsIgnoreCase(MyTools.StrFiltr(GS_KCMC))){
			sql+=" and t.课程名称 like'%"+MyTools.StrFiltr(GS_KCMC)+"%'";
		}

		if("请选择".equalsIgnoreCase(MyTools.StrFiltr(GS_KSXS))){
			
		}else if("未选考试形式".equalsIgnoreCase(MyTools.StrFiltr(GS_KSXS))){
			sql+=" and (t.考试形式='' or t.考试形式 is null) ";
		}else{
			sql+=" and t.考试形式='"+MyTools.StrFiltr(GS_KSXS)+"' ";
		}
		if(!"".equalsIgnoreCase(MyTools.StrFiltr(GS_XZBMC))){
			sql+=" and t.行政班简称  like '%"+MyTools.StrFiltr(GS_XZBMC)+"%'";
		}
		sql+=" group by 授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式编号,考试形式,系名称 order by 课程名称,行政班简称 ";
		
		vec5=db.GetContextVector(sql);
		
		//去除整班未登分
		sql4="select 相关编号 from (select distinct a.相关编号,sum(case when 成绩<>'' then 1 else 0 end) as 已登分人数 " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join (select ta.学号,ta.相关编号," +
				"case when (case when ta.平时 in ('-1','-5') then '' else ta.平时 end)<>'' then ta.平时 " +
				"when (case when ta.期中 in ('-1','-5') then '' else ta.期中 end)<>'' then ta.期中 " +
				"when (case when ta.实训 in ('-1','-5') then '' else ta.实训 end)<>'' then ta.实训 " +
				"when (case when ta.期末 in ('-1','-5') then '' else ta.期末 end)<>'' then ta.期末 " +
				"when (case when ta.总评 in ('-1','-5') then '' else ta.总评 end)<>'' then ta.总评 " +
				"when (case when ta.重修1 in ('-1','-5') then '' else ta.重修1 end)<>'' then ta.重修1 " +
				"when (case when ta.重修2 in ('-1','-5') then '' else ta.重修2 end)<>'' then ta.重修2 " +
				"when (case when ta.补考 in ('-1','-5') then '' else ta.补考 end)<>'' then ta.补考 " +
				"when (case when ta.大补考 in ('-1','-5') then '' else ta.大补考 end)<>'' then ta.大补考 else '' end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 ta " +
				"left join V_成绩管理_科目课程信息表 tb on ta.科目编号=tb.科目编号 " +
				"where ta.状态='1' and ta.成绩状态='1' and cast(left(tb.学年学期编码,4) as int) between " + (Integer.parseInt(xn)-6) + " and " + xn + " ) c on c.相关编号=a.相关编号 " +
				"where a.状态='1' and b.状态='1' and cast(left(b.学年学期编码,4) as int) between " + (Integer.parseInt(xn)-6) + " and " + xn + " " +
				"group by a.相关编号,a.行政班代码,a.来源类型) as t " +
				"where t.已登分人数=0 ";
		vec4=db.GetContextVector(sql4);
		
		if(vec4!=null&&vec4.size()>0) {
			int sametag=0;
			for(int i=0;i<vec5.size();i=i+9){
				sametag=0;
				for(int j=0;j<vec4.size();j++) {
					if(vec5.get(i).toString().equals(vec4.get(j).toString())) {//授课计划编号相同，整班未登分删掉
						sametag=1;
					}
				}
				if(sametag==0) {
					vec.add(vec5.get(i).toString());//授课计划明细编号 1
					vec.add(vec5.get(i+1).toString());//学年学期 2
					vec.add(vec5.get(i+2).toString());//课程名称 3
					vec.add(vec5.get(i+3).toString());//人数 4
					vec.add(vec5.get(i+4).toString());//考试形式 5
					vec.add(vec5.get(i+5).toString());//考试形式 6
					vec.add(vec5.get(i+6).toString());//系名称 7
					vec.add(vec5.get(i+7).toString());//行政班简称 8
					vec.add(vec5.get(i+8).toString());//行政班名称 9	
				}else {
					
				}
			}
		}
		
		
		String skjhmxs="";
		sql3="SELECT count(*) FROM [dbo].[V_考试管理_大补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
		if(db.getResultFromDB(sql3)){
			sql3="SELECT [授课计划明细编号] FROM [dbo].[V_考试管理_大补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
			vec3=db.GetContextVector(sql3);
			for(int j=0;j<vec3.size();j++){
				skjhmxs+=vec3.get(j).toString()+"@";
			}	
		}else{
			
		}
		
		Vector vecr=new Vector();
		Vector vecs=new Vector();
		Vector vect=new Vector();
		for(int i=0;vec.size()>1;){
			String skjhmx=vec.get(i).toString();
			String xnxqbm=vec.get(i+1).toString();
			String kcmc=vec.get(i+2).toString();
			String rs=vec.get(i+3).toString();
			String ksxsbh=vec.get(i+4).toString();
			String ksxs=vec.get(i+5).toString();
			String xmc=vec.get(i+6).toString();
			String xzbjc=vec.get(i+7).toString();
			String xzbmc=vec.get(i+8).toString();
			//在合并信息中,保存到vecs
			if(!skjhmxs.equals("")&&skjhmxs.indexOf(vec.get(i).toString())>-1){
				vecs.add(skjhmx);//授课计划明细编号 1
				vecs.add(xnxqbm);//学年学期 2
				vecs.add(kcmc);//课程名称 3
				vecs.add(rs);//人数 4
				vecs.add(ksxsbh);//考试形式编号 5
				vecs.add(ksxs);//考试形式 6
				vecs.add(xmc);//系名称 7
				vecs.add(xzbjc);//行政班简称 8
				vecs.add(xzbmc);//行政班名称 9
			}else{	
				for(int j=9;j<vec.size();j=j+9){
					//是选修课
					if(vec.get(i+2).toString().indexOf("选修")>-1){
						//课程名称 相同 合并
						if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+2).toString().equals(vec.get(j+2).toString())){
							skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1					
							xnxqbm=vec.get(j+1).toString();//学年学期 2					
							//课程名称 3
							rs+="$"+vec.get(j+3).toString();//人数 4
							//考试形式 5
							xmc+="$"+vec.get(j+6).toString();//系名称 7
							xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
							xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
							
							for(int k=0;k<9;k++){
								vec.remove(j);
							}
							j=j-9;
						}
						
					}else{
						//课程名称,系名称,班级简称 相同 合并
						if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+2).toString().equals(vec.get(j+2).toString())&&vec.get(i+6).toString().equals(vec.get(j+6).toString())&&(vec.get(i+7).toString().equals(vec.get(j+7).toString())||vec.get(i+7).toString().substring(2,4).equals(vec.get(j+7).toString().substring(2,4)))){
							skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
							xnxqbm=vec.get(j+1).toString();//学年学期 2
							//课程名称 3
							rs+="$"+vec.get(j+3).toString();//人数 4
							//考试形式 5
							//系名称 6
							xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
							xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
							
							for(int k=0;k<9;k++){
								vec.remove(j);
							}
							j=j-9;
						}	
					}
				}
				
				vecr.add(skjhmx);//授课计划明细编号 1
				vecr.add(xnxqbm);//学年学期 2
				vecr.add(kcmc);//课程名称 3
				vecr.add(rs);//人数 4
				vecr.add(ksxsbh);//考试形式 5
				vecr.add(ksxs);//考试形式 6
				vecr.add(xmc);//系名称 7
				vecr.add(xzbjc);//行政班简称 8
				vecr.add(xzbmc);//行政班名称 9			
			}
			for(int k=0;k<9;k++){
				vec.remove(i);
			}
		}
		
//		for(int i=0;i<vecs.size();i=i+9){
//			System.out.println(i/9+":--"+vecs.get(i)+" | "+vecs.get(i+2)+" | "+vecs.get(i+3)+" | "+vecs.get(i+4)+" | "+vecs.get(i+5));
//		}
		
		if(vec3!=null&&vec3.size()>0){
			for(int i=0;i<vec3.size();i++){
				String sskjhmx="";
				String sxnxqbm="";
				String skcmc="";
				String srs="";
				String sksxsbh="";
				String sksxs="";
				String sxmc="";
				String sxzbjc="";
				String sxzbmc="";
				for(int j=0;j<vecs.size();j=j+9){
					if(vec3.get(i).toString().indexOf(vecs.get(j).toString())>-1){
						
						sxnxqbm+=vecs.get(j+1).toString()+"@";
						skcmc+=vecs.get(j+2).toString()+"@";
						srs+=vecs.get(j+3).toString()+"@";
						sksxsbh=vecs.get(j+4).toString();
						sksxs=vecs.get(j+5).toString();
						sxmc+=vecs.get(j+6).toString()+"@";
						sxzbjc+=vecs.get(j+7).toString()+"@";
						sxzbmc+=vecs.get(j+8).toString()+"@";	
					}	
				}

				sskjhmx=vec3.get(i).toString();
				int max=0;
				if(!sxnxqbm.equals("")){
					sxnxqbm=sxnxqbm.substring(0,sxnxqbm.length()-1);
					
					String[] xq=sxnxqbm.split("@");
					max=Integer.parseInt(xq[0]);
					for(int k=0;k<xq.length;k++){
						if(max<Integer.parseInt(xq[k])){
							max=Integer.parseInt(xq[k]);
						}
					}
				}
				if(!skcmc.equals("")){
					skcmc=skcmc.substring(0,skcmc.length()-1);
				}
				if(!srs.equals("")){
					srs=srs.substring(0,srs.length()-1);
				}
				if(!sxmc.equals("")){
					sxmc=sxmc.substring(0,sxmc.length()-1);
				}
				if(!sxzbjc.equals("")){
					sxzbjc=sxzbjc.substring(0,sxzbjc.length()-1);
				}
				if(!sxzbmc.equals("")){
					sxzbmc=sxzbmc.substring(0,sxzbmc.length()-1);
				}
				
				if(!sxnxqbm.equals("")){
					vect.add(sskjhmx);//授课计划明细编号 1
					vect.add(max+"");//学年学期 2
					vect.add(skcmc);//课程名称 3
					vect.add(srs);//人数 4
					vect.add(sksxsbh);//考试形式 5
					vect.add(sksxs);//考试形式 6
					vect.add(sxmc);//系名称 7
					vect.add(sxzbjc);//行政班简称 8
					vect.add(sxzbmc);//行政班名称 9	
				}
			}
		}
		
		for(int i=0;i<vecr.size();i++){
			vect.add(vecr.get(i).toString());
		}
		
		
		
		String revec="";
		int total=0;
		if(pageNum*pageSize*9>vect.size()){
			total=vect.size();
		}else{
			total=pageNum*pageSize*9;
		}
		 
		for(int m=(pageNum-1)*pageSize*9;m<total;m=m+9){
			revec+="{\"授课计划明细编号\":\""+vect.get(m).toString()+"\",\"学年学期\":\""+vect.get(m+1).toString()+"\",\"课程名称\":\""+vect.get(m+2).toString()+"\",\"人数\":\""+vect.get(m+3).toString()+"\",\"考试形式编号\":\""+vect.get(m+4).toString()+"\",\"考试形式\":\""+vect.get(m+5).toString()+"\",\"系名称\":\""+vect.get(m+6).toString()+"\",\"行政班简称\":\""+vect.get(m+7).toString()+"\",\"行政班名称\":\""+vect.get(m+8).toString()+"\",\"MSG\":\""+this.getMSG()+"\",\"pageNum\":\""+pageNum+"\",\"pageSize\":\""+pageSize+"\"},";
		}
		if(!revec.equals("")){
			revec="["+revec.substring(0, revec.length()-1)+"]";
		}
		this.setTATOL(vect.size()/9+"");
		//System.out.println("revec:--"+pageNum+"|"+pageSize);
		return revec;
	}
	
	/**
	 * 合并大补考信息
	 * @date:2016-11-14
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void addDBKinfo(String dbkarray,String bktype) throws SQLException {
		String sql = "";
		String sqldel="";
		Vector vec = new Vector();
		
		//System.out.println("dbkarray-----"+dbkarray);
		String[] lines=dbkarray.split(",");
		String xnxq="";
		String kcmc="";
		String rs="";
		String ksxs="";
		String xmc="";
		String xzbjc="";
		String xzbmc="";
		
		String hb_skjhmx="";
		String hb_xnxq="";
		String hb_kcmc="";
		String hb_rs="";
		String hb_ksxs="";
		String hb_xmc="";
		String hb_xzbjc="";
		String hb_xzbmc="";
		
		for(int i=0;i<lines.length;i++){
			hb_skjhmx+=lines[i]+"@";
			if(bktype.equals("bk")){
				sqldel+=" delete from V_考试管理_补考合并信息 where 考试学期='"+MyTools.fixSql(this.getGG_XNXQBM())+"' and 授课计划明细编号='"+MyTools.fixSql(lines[i])+"' ";
			}else {
				sqldel+=" delete from V_考试管理_大补考合并信息 where 考试学期='"+MyTools.fixSql(this.getGG_XNXQBM())+"' and 授课计划明细编号='"+MyTools.fixSql(lines[i])+"' ";
			}
			vec.add(sqldel);
		}
		
		hb_skjhmx=hb_skjhmx.substring(0, hb_skjhmx.length()-1);


		if(bktype.equals("bk")){
			sql="insert into V_考试管理_补考合并信息 ([考试学期],[授课计划明细编号]) values " +
					"('"+this.getGG_XNXQBM()+"','"+hb_skjhmx+"')";
		}else{
			sql="insert into V_考试管理_大补考合并信息 ([考试学期],[授课计划明细编号]) values " +
					"('"+this.getGG_XNXQBM()+"','"+hb_skjhmx+"')";
		}
		vec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}	
	}
	
	/**
	 * 拆分大补考信息
	 * @date:2016-11-15
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void splitDBKinfo(String dbkinfo,String bktype) throws SQLException {
		String sql = "";
		Vector vec = new Vector();
		
		//System.out.println("dbkarray-----"+dbkarray);
		String[] lines=dbkinfo.split("#");
		if(bktype.equals("bk")){
			sql=" delete from V_考试管理_补考合并信息  where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' " +
					"and [授课计划明细编号]='"+MyTools.fixSql(dbkinfo)+"' ";
		}else{
			sql=" delete from V_考试管理_大补考合并信息  where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' " +
				"and [授课计划明细编号]='"+MyTools.fixSql(dbkinfo)+"' ";
		}
		vec.add(sql);

		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("拆分成功");
		}else{
			this.setMSG("拆分失败");
		}	
	}
	
	
	/**
	 * 
	 * @date:2016-10-26
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void deleteWeipai(String weipaiarray) throws SQLException {
		String sql = "";
		Vector vec = null;
		weipaiarray=weipaiarray.substring(0, weipaiarray.length()-1);
		String[] weipaiID=weipaiarray.split(",");
		String wpskjhbh="";
		for(int i=0;i<weipaiID.length;i++){
			wpskjhbh+="'"+weipaiID[i]+"',";
		}
		if(!wpskjhbh.equals("")){
			wpskjhbh=wpskjhbh.substring(0, wpskjhbh.length()-1);
		}
		sql="delete from dbo.V_考试管理_考试设置 where [授课计划明细编号] in ("+wpskjhbh+") ";
		sql+="delete from dbo.V_规则管理_授课计划明细表 where [授课计划明细编号] in ("+wpskjhbh+") ";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}	
	}
	
	/**
	 * 显示考试形式
	 * @date:2016-10-27
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector selectKSXS() throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="SELECT [编号],[考试形式]  FROM [dbo].[V_考试形式] where [编号]!=0 ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 考试安排表导出
	 * @author lupengfei
	 * @date:2017-06-05
	 * @return savePath 下载路径
	 * @throws SQLException
	*/
	public String ExportExcelKSAPB()throws SQLException {
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sqlkssz = "";
		String savePath="";
		Vector vec = new Vector();
		Vector vec2 = null;
		Vector vecins = new Vector();
		String xn=this.getGG_XNXQBM().substring(0,4);
		
		String kssjd= MyTools.getProp(request, "Base.examTime");//考试时间段
		String[] kssjdts=kssjd.split(",");
			
//		sql="select c.[课程名称],c.考试形式,c.[行政班简称],c.[学生人数],c.[时间序列],c.[场地要求],c.[监考教师姓名] from ( "+
//				" SELECT a.[课程名称],e.考试形式,b.[行政班简称],a.[学生人数],a.[时间序列],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b,dbo.V_考试形式 e where a.行政班代码=b.行政班代码 and a.考试形式=e.编号 and a.考场安排主表编号='"+this.getQZQM()+"' " +
//				" union " +
//				" SELECT a.[课程名称],e.考试形式,a.行政班名称 as [行政班简称],a.[学生人数],a.[时间序列],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,dbo.V_考试形式 e where a.专业代码='' and a.考试形式=e.编号 and a.考场安排主表编号='"+this.getQZQM()+"' "+
//				" ) c order by c.时间序列,c.课程名称,c.[行政班简称] ";
//		vec = db.GetContextVector(sql);
		
		
		
		String sqlqzqm="select 考试名称,上课截止周数 from dbo.V_考试管理_考场安排主表 where 考场安排主表编号='"+this.getQZQM()+"' ";
		Vector vecqzqm=db.GetContextVector(sqlqzqm);
		
		if(Integer.parseInt(vecqzqm.get(1).toString())>15){//期末考试
			sql=" SELECT a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b where a.行政班代码=b.行政班代码 and a.考场安排主表编号='"+this.getQZQM()+"' and [考试形式] not in ('4','5') " +
					" union " +
					" SELECT a.[时间序列],a.[课程名称],a.行政班名称 as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a where a.专业名称='' and a.考场安排主表编号='"+this.getQZQM()+"'  and [考试形式] not in ('4','5') ";
			vec2 = db.GetContextVector(sql);
			if(vec2!=null&&vec2.size()>0){
				//如果分两个教室，拆分vec2，保存到vec里
				for(int v=0;v<vec2.size();v=v+6){
					if(vec2.get(v+4).toString().indexOf(",")>-1){//有多个教室
						String[] room=vec2.get(v+4).toString().split(",");
						//for(int r=0;r<room.length;r++){
							vec.add(vec2.get(v).toString());//时间序列
							vec.add(vec2.get(v+1).toString());//课程名称
							vec.add(vec2.get(v+2).toString());//行政班简称
							
							int rs1=Integer.parseInt(vec2.get(v+3).toString())/2;
							vec.add("1-"+rs1);//学生人数
							
							vec.add(room[0]);//场地要求
							
							if(vec2.get(v+5).toString().equals("")){
								vec.add("");//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")<0){
								vec.add(vec2.get(v+5).toString());//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")>-1){
								String[] teacher=vec2.get(v+5).toString().split(",");
								if(teacher.length>2){
									vec.add(teacher[0]+","+teacher[1]);//监考教师姓名
								}else{//teacher.length==2
									vec.add(vec2.get(v+5).toString());//监考教师姓名
								}
							}
							//=====================================第2条
							vec.add(vec2.get(v).toString());//时间序列
							vec.add(vec2.get(v+1).toString());//课程名称
							vec.add(vec2.get(v+2).toString());//行政班简称
							vec.add((rs1+1)+"-"+Integer.parseInt(vec2.get(v+3).toString()));//学生人数
							vec.add(room[1]);//场地要求
							if(vec2.get(v+5).toString().equals("")){
								vec.add("");//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")<0){
								vec.add("");//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")>-1){
								String[] teacher=vec2.get(v+5).toString().split(",");
								if(teacher.length==3){
									vec.add(teacher[2]);//监考教师姓名
								}else if(teacher.length==4){
									vec.add(teacher[2]+","+teacher[3]);//监考教师姓名
								}else{//teacher.length==2
									vec.add("");//监考教师姓名
								}
							}
						//}
					}else{
						for(int u=0;u<6;u++){
							vec.add(vec2.get(v+u).toString());
						}		
					}		

				}
			}
		}else{//前十周，前十五周考试
			sql=" SELECT a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名],c.[考试形式] FROM [dbo].[V_考试管理_考场安排明细表] a inner join [dbo].[V_学校班级数据子类] b on a.行政班代码=b.行政班代码 left join dbo.V_考试形式 c on a.[考试形式]=c.[编号] where a.考场安排主表编号='"+this.getQZQM()+"' " +
					" union " +
					" SELECT a.[时间序列],a.[课程名称],a.行政班名称 as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名],c.[考试形式] FROM [dbo].[V_考试管理_考场安排明细表] a left join dbo.V_考试形式 c on a.[考试形式]=c.[编号] where a.专业名称='' and a.考场安排主表编号='"+this.getQZQM()+"' ";
			vec2 = db.GetContextVector(sql);
			if(vec2!=null&&vec2.size()>0){
				//如果分两个教室，拆分vec2，保存到vec里
				for(int v=0;v<vec2.size();v=v+7){
					if(vec2.get(v+4).toString().indexOf(",")>-1){//有多个教室
						String[] room=vec2.get(v+4).toString().split(",");
						//for(int r=0;r<room.length;r++){
							vec.add(vec2.get(v).toString());//时间序列
							vec.add(vec2.get(v+1).toString());//课程名称
							vec.add(vec2.get(v+2).toString());//行政班简称
							
							int rs1=Integer.parseInt(vec2.get(v+3).toString())/2;
							vec.add("1-"+rs1);//学生人数
							
							vec.add(room[0]);//场地要求
							
							if(vec2.get(v+5).toString().equals("")){
								vec.add("");//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")<0){
								vec.add(vec2.get(v+5).toString());//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")>-1){
								String[] teacher=vec2.get(v+5).toString().split(",");
								if(teacher.length>2){
									vec.add(teacher[0]+","+teacher[1]);//监考教师姓名
								}else{//teacher.length==2
									vec.add(vec2.get(v+5).toString());//监考教师姓名
								}
							}
							//=====================================第2条
							vec.add(vec2.get(v).toString());//时间序列
							vec.add(vec2.get(v+1).toString());//课程名称
							vec.add(vec2.get(v+2).toString());//行政班简称
							vec.add((rs1+1)+"-"+Integer.parseInt(vec2.get(v+3).toString()));//学生人数
							vec.add(room[1]);//场地要求
							if(vec2.get(v+5).toString().equals("")){
								vec.add("");//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")<0){
								vec.add("");//监考教师姓名
							}else if(vec2.get(v+5).toString().indexOf(",")>-1){
								String[] teacher=vec2.get(v+5).toString().split(",");
								if(teacher.length==3){
									vec.add(teacher[2]);//监考教师姓名
								}else if(teacher.length==4){
									vec.add(teacher[2]+","+teacher[3]);//监考教师姓名
								}else{//teacher.length==2
									vec.add("");//监考教师姓名
								}
							}
						//}
					}else{
						for(int u=0;u<7;u++){
							vec.add(vec2.get(v+u).toString());
						}		
					}		

				}
			}
		}
		
		
		if (vec != null && vec.size() > 0) {			
			
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);
			savePath = MyTools.getProp(request, "Base.exportExcelPath");	
			
			//创建
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			savePath +=  "/行健学院"+this.getGG_XNXQBM().substring(0,4)+"学年第"+this.getGG_XNXQBM().substring(4,5)+"学期 "+vecqzqm.get(0).toString()+" 考试安排表.xls";
			
			try {
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				
				//获取日期，时间段信息
				String sqlksrq="select [学年学期编码],[考试名称],[考试类型],[上课截止周数],[考试日期] from [dbo].[V_考试管理_考场安排主表] where [考场安排主表编号]='"+this.getQZQM()+"' ";
				Vector vecksrq=db.GetContextVector(sqlksrq);
				String ksrq=vecksrq.get(4).toString();//考试日期
				String[] ksrqts=ksrq.split(",");
				
				//获取普通考试安排规则
				String sqlksxx="select distinct [考试日期],[考试时间段] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+this.getQZQM()+"' and [考试类型]='笔试' and [考试年级]!='' and [课程类型]!='' and [教室人数]!='' order by [考试日期],[考试时间段] ";
				Vector vecksxx=db.GetContextVector(sqlksxx);
				WritableSheet[] wsheet = new WritableSheet[ksrqts.length];
				
				if(Integer.parseInt(vecqzqm.get(1).toString())>15){//期末考试

					for(int i=0;i<ksrqts.length;i++){
						wsheet[i] = wbook.createSheet(ksrqts[i], i);// 工作表名称
						// 设置列宽
//						for (int i = 0; i < titleVec.size(); i++) {
//							if(i==2){
//								wsheet.setColumnView(i, 40);
//							}
//							else{
//								wsheet.setColumnView(i, 16);
//							}
//						}
						
						//考试日期是星期几
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
						String str = ksrqts[i].trim(); 
						try {
							cal.setTime(format.parse(ksrqts[i].trim()));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Date dt=sdf.parse(str,new ParsePosition(0)); 
						Calendar rightNow = Calendar.getInstance(); 
					    rightNow.setTime(dt); 
						//rightNow.add(Calendar.DATE,1);//你要加减的日期   
						//rightNow.add(Calendar.DATE,2);//你要加减的日期   
						Date dt1=rightNow.getTime(); 
						String reStr=sdf.format(dt1);
						int week = cal.get(Calendar.DAY_OF_WEEK)-1;
						String weekday="";
						if(week==1){
							weekday="一";
						}else if(week==2){
							weekday="二";
						}else if(week==3){
							weekday="三";
						}else if(week==4){
							weekday="四";
						}else if(week==5){
							weekday="五";
						}else if(week==6){
							weekday="六";
						}else if(week==0){
							weekday="日";
						}
						
						//工作表 0------------------------------------------------------------------------
						int counum=0;//excel表中行数
						String cellContent = ""; //当前单元格的内容
						
						//第1行
	
						//设置课表标题行列字体大小
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						//计算当天用了几个时间段
						int sjdnum=0;						
						for(int j=0;j<vecksxx.size();j=j+2){
							if(vecksxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等							
								sjdnum++;
							}
						}	
						//每个时间段排第几
						int onum=0;
						String[] sjdt=new String[sjdnum];
						for(int j=0;j<vecksxx.size();j=j+2){
							if(vecksxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
								for(int s=0;s<kssjdts.length;s++){
									if(vecksxx.get(j+1).toString().equals(kssjdts[s])){//时间段相同
										sjdt[onum]=s+"";
									}
								}
								onum++;
							}
						}	
						
						
						wsheet[i].mergeCells(0, counum, 7*sjdnum-1, counum);
						cellContent = "行健学院"+this.getGG_XNXQBM().substring(0,4)+"学年度第"+this.getGG_XNXQBM().substring(0,4)+"学期"+vecqzqm.get(0).toString()+"安排表";
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[i].addCell(content);
						wsheet[i].setRowView(counum, 600);
						
						//第2行
						counum++;
						
						//设置课表标题行列字体大小
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						
						int ksnum=0;
						for(int j=0;j<vecksxx.size();j=j+2){
							if(vecksxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
								ksnum=j;
								break;
							}
						}
						
						String name=Integer.parseInt(reStr.split("-")[1])+"月"+Integer.parseInt(reStr.split("-")[2])+"日(周"+weekday+")";	
						for(int k=0;k<sjdnum;k++){
							wsheet[i].mergeCells(0+k*7, counum, 6+k*7, counum);						
							cellContent=name+" "+vecksxx.get(ksnum+1).toString();
							content = new Label(0+k*7, counum, cellContent, contentStyle);
							wsheet[i].addCell(content);
							wsheet[i].setRowView(counum, 500);
							ksnum=ksnum+2;
						}
						
						//第3行
						counum++;
						
						//生成标题
						String[] title=new String[]{"试场","科目","班级","人数","教室","监考","监考"};
						for(int k=0;k<sjdnum;k++){
							//设置列宽
							wsheet[i].setColumnView(k*7, 6);
							wsheet[i].setColumnView(k*7+1, 20);
							wsheet[i].setColumnView(k*7+2, 18);
							wsheet[i].setColumnView(k*7+3, 6);
							wsheet[i].setColumnView(k*7+4, 6);
							wsheet[i].setColumnView(k*7+5, 7);
							wsheet[i].setColumnView(k*7+6, 7);						
						}
						
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
		
						for(int colNum=0; colNum<7*sjdnum; colNum++){
							cellContent=title[colNum%7];						
							content = new Label(colNum, counum, cellContent, contentStyle);
							wsheet[i].addCell(content);	
							wsheet[i].setRowView(counum, 500);
						}
					
						for(int k=0;k<sjdnum;k++){
							//合并监考教师格子
							wsheet[i].mergeCells(k*7+5, counum, k*7+6, counum);
						}
					
						//第4行
						counum++;
						
						int[] line=new int[sjdnum];//统计每个时段的考试数量
						for(int l=0;l<sjdnum;l++){
							line[l]=1;
						}
						
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						
						for(int v=0;v<vec.size();v=v+6){
							String sjxl=vec.get(v).toString();
							if(sjxl.substring(0,sjxl.indexOf("#")).equals(i+"")){//日期相同
								int knum=-1;
								for(int j=0;j<7;j++){
									for(int k=0;k<sjdt.length;k++){
										if(sjxl.substring(sjxl.indexOf("#")+1,sjxl.length()).equals(sjdt[k])){
											knum=k;
										}
									}
									if(knum==-1){
										
									}else{
										if(j==0){
											cellContent=line[knum]+"";
											content = new Label(j+knum*7, line[knum]+2, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(line[knum]+2, 500);
										}else if(j==5){//监考
											if(vec.get(v+j).toString().indexOf(",")>-1){
												cellContent=vec.get(v+j).toString().split(",")[0];	
												content = new Label(j+knum*7, line[knum]+2, cellContent, contentStyle);
												wsheet[i].addCell(content);	
												
												cellContent=vec.get(v+j).toString().split(",")[1];	
												content = new Label(j+knum*7+1, line[knum]+2, cellContent, contentStyle);
												wsheet[i].addCell(content);	
											}else{
												cellContent=vec.get(v+j).toString();	
												content = new Label(j+knum*7, line[knum]+2, cellContent, contentStyle);
												wsheet[i].addCell(content);	
												
												cellContent="";	
												content = new Label(j+knum*7+1, line[knum]+2, cellContent, contentStyle);
												wsheet[i].addCell(content);	
											}
										}else if(j==6){
											
										}else{
											cellContent=vec.get(v+j).toString();	
											content = new Label(j+knum*7, line[knum]+2, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(line[knum]+2, 500);
										}
									}
								}	
								if(knum==-1){
									
								}else{
									line[knum]++;
								}
							}
						}
				
						//补齐方框
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						
						int maxline=-1;
						for(int l=0;l<sjdnum;l++){
							if(maxline<line[l]){
								maxline=line[l];
							}
						}
						for(int l=0;l<sjdnum;l++){
							for(int n=line[l];n<maxline;n++){
								for(int j=0;j<7;j++){
									cellContent="";
									content = new Label(j+l*7, n+2, cellContent, contentStyle);
									wsheet[i].addCell(content);	
								}
							}
						}
						
						//默认为横向打印
			            wsheet[i].setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,PaperSize.A3,0.5d,0.5d);
			            wsheet[i].getSettings().setScaleFactor(90); 
					}			
					//contentStyle.setShrinkToFit(true);//字体大小自适应
	
					//上机
					Vector vec1=new Vector();
					sql=" SELECT a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b where a.行政班代码=b.行政班代码 and a.考场安排主表编号='"+this.getQZQM()+"' and [考试形式] in ('4','5') " +
							" union " +
							" SELECT a.[时间序列],a.[课程名称],a.行政班名称 as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a where a.专业名称='' and a.考场安排主表编号='"+this.getQZQM()+"'  and [考试形式] in ('4','5') ";
					vec2 = db.GetContextVector(sql);
					if(vec2!=null&&vec2.size()>0){
						//如果分两个教室，拆分vec2，保存到vec里
						for(int v=0;v<vec2.size();v=v+6){
							if(vec2.get(v+4).toString().indexOf(",")>-1){//有多个教室
								String[] room=vec2.get(v+4).toString().split(",");
								//for(int r=0;r<room.length;r++){
									vec1.add(vec2.get(v).toString());//时间序列
									vec1.add(vec2.get(v+1).toString());//课程名称
									vec1.add(vec2.get(v+2).toString());//行政班简称
									
									int rs1=Integer.parseInt(vec2.get(v+3).toString())/2;
									vec1.add("1-"+rs1);//学生人数
									
									vec1.add(room[0]);//场地要求
									
									if(vec2.get(v+5).toString().equals("")){
										vec1.add("");//监考教师姓名
									}else if(vec2.get(v+5).toString().indexOf(",")<0){
										vec1.add(vec2.get(v+5).toString());//监考教师姓名
									}else if(vec2.get(v+5).toString().indexOf(",")>-1){
										String[] teacher=vec2.get(v+5).toString().split(",");
										if(teacher.length>2){
											vec1.add(teacher[0]+","+teacher[1]);//监考教师姓名
										}else{//teacher.length==2
											vec1.add(vec2.get(v+5).toString());//监考教师姓名
										}
									}
									//=====================================第2条
									vec1.add(vec2.get(v).toString());//时间序列
									vec1.add(vec2.get(v+1).toString());//课程名称
									vec1.add(vec2.get(v+2).toString());//行政班简称
									vec1.add((rs1+1)+"-"+Integer.parseInt(vec2.get(v+3).toString()));//学生人数
									vec1.add(room[1]);//场地要求
									if(vec2.get(v+5).toString().equals("")){
										vec1.add("");//监考教师姓名
									}else if(vec2.get(v+5).toString().indexOf(",")<0){
										vec1.add("");//监考教师姓名
									}else if(vec2.get(v+5).toString().indexOf(",")>-1){
										String[] teacher=vec2.get(v+5).toString().split(",");
										if(teacher.length==3){
											vec1.add(teacher[2]);//监考教师姓名
										}else if(teacher.length==4){
											vec1.add(teacher[2]+","+teacher[3]);//监考教师姓名
										}else{//teacher.length==2
											vec1.add("");//监考教师姓名
										}
									}
								//}
							}else{
								for(int u=0;u<6;u++){
									vec1.add(vec2.get(v+u).toString());
								}		
							}		
	
						}
					}
					
					//获取上机考试安排规则
					String sqlkssjxx="select distinct [考试日期],[考试时间段] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+this.getQZQM()+"' and [考试类型]='上机' and [考试年级]!='' and [课程类型]!='' and [教室人数]!='' order by [考试日期],[考试时间段] ";
					Vector veckssjxx=db.GetContextVector(sqlkssjxx);
					
					if(veckssjxx!=null&&veckssjxx.size()>0){
						WritableSheet wsheet1 = wbook.createSheet("上机", ksrqts.length);// 工作表名称
						int counum1=0;//excel表中行数
						String cellContent1 = ""; //当前单元格的内容
						
						//第1行
	
						//设置课表标题行列字体大小
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						for(int i=0;i<ksrqts.length;i++){
							//考试日期是星期几
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							Calendar cal = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
							String str = ksrqts[i].trim(); 
							try {
								cal.setTime(format.parse(ksrqts[i].trim()));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Date dt=sdf.parse(str,new ParsePosition(0)); 
							Calendar rightNow = Calendar.getInstance(); 
						    rightNow.setTime(dt); 
							//rightNow.add(Calendar.DATE,1);//你要加减的日期   
							//rightNow.add(Calendar.DATE,2);//你要加减的日期   
							Date dt1=rightNow.getTime(); 
							String reStr=sdf.format(dt1);
							int week = cal.get(Calendar.DAY_OF_WEEK)-1;
							String weekday="";
							if(week==1){
								weekday="一";
							}else if(week==2){
								weekday="二";
							}else if(week==3){
								weekday="三";
							}else if(week==4){
								weekday="四";
							}else if(week==5){
								weekday="五";
							}else if(week==6){
								weekday="六";
							}else if(week==0){
								weekday="日";
							}
							
							//计算当天用了几个时间段
							int sjdnum=0;						
							for(int j=0;j<veckssjxx.size();j=j+2){
								if(veckssjxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等							
									sjdnum++;
								}
							}	
					
							int[] linenum=new int[sjdnum];
							if(sjdnum==0){
								
							}else{
								//每个时间段排第几
								int onum=0;
								String[] sjdt=new String[sjdnum];
								for(int j=0;j<veckssjxx.size();j=j+2){
									if(veckssjxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
										for(int s=0;s<kssjdts.length;s++){
											if(veckssjxx.get(j+1).toString().equals(kssjdts[s])){//时间段相同
												sjdt[onum]=s+"";
											}
										}
										onum++;
									}
								}	
							
								if(i==0){
									wsheet1.mergeCells(0, counum1, 7*sjdnum-1, counum1);
									cellContent1 = "行健学院"+this.getGG_XNXQBM().substring(0,4)+"学年度第"+this.getGG_XNXQBM().substring(4,5)+"学期"+vecqzqm.get(0).toString()+"安排表（上机）";
									content = new Label(0, counum1, cellContent1, contentStyle);
									wsheet1.addCell(content);
									wsheet1.setRowView(counum1, 600);
								}
								
								//第2行
								counum1++;
								
								//设置课表标题行列字体大小
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setShrinkToFit(true);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								
								int ksnum=0;
								for(int j=0;j<veckssjxx.size();j=j+2){
									if(veckssjxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
										ksnum=j;
										break;
									}
								}
	
								String name=Integer.parseInt(reStr.split("-")[1])+"月"+Integer.parseInt(reStr.split("-")[2])+"日(周"+weekday+")";	
								for(int k=0;k<sjdnum;k++){
									wsheet1.mergeCells(0+k*7, counum1, 6+k*7, counum1);						
									cellContent1=name+" "+veckssjxx.get(ksnum+1).toString();
									content = new Label(0+k*7, counum1, cellContent1, contentStyle);
									wsheet1.addCell(content);
									wsheet1.setRowView(counum1, 500);
									ksnum=ksnum+2;
								}
								
								//第3行
								counum1++;
								
								//生成标题
								String[] title=new String[]{"试场","科目","班级","人数","教室","监考","监考"};
								for(int k=0;k<sjdnum;k++){
									//设置列宽
									wsheet1.setColumnView(k*7, 6);
									wsheet1.setColumnView(k*7+1, 20);
									wsheet1.setColumnView(k*7+2, 18);
									wsheet1.setColumnView(k*7+3, 6);
									wsheet1.setColumnView(k*7+4, 6);
									wsheet1.setColumnView(k*7+5, 7);
									wsheet1.setColumnView(k*7+6, 7);						
								}
								
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setShrinkToFit(true);
								//contentStyle.setWrap(true);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				
								for(int colNum=0; colNum<7*sjdnum; colNum++){
									cellContent1=title[colNum%7];						
									content = new Label(colNum, counum1, cellContent1, contentStyle);
									wsheet1.addCell(content);	
									wsheet1.setRowView(counum1, 500);
								}
				
								for(int k=0;k<sjdnum;k++){
									//合并监考教师格子
									wsheet1.mergeCells(k*7+5, counum1, k*7+6, counum1);
								}
				
								//第4行
								//counum1++;
								
								int[] line=new int[sjdnum];//统计每个时段的考试数量
								for(int l=0;l<sjdnum;l++){
									line[l]=1;
								}
								
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setShrinkToFit(true);
								//contentStyle.setWrap(true);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
													
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								
								for(int v=0;v<vec1.size();v=v+6){
									String sjxl=vec1.get(v).toString();
									if(sjxl.substring(0,sjxl.indexOf("#")).equals(i+"")){//日期相同
										int knum=-1;
										for(int j=0;j<7;j++){
											for(int k=0;k<sjdt.length;k++){
												if(sjxl.substring(sjxl.indexOf("#")+1,sjxl.length()).equals(sjdt[k])){
													knum=k;
												}
											}
											if(knum==-1){
												
											}else{
												if(j==0){
													cellContent1=line[knum]+"";
													content = new Label(j+knum*7, line[knum]+counum1, cellContent1, contentStyle);
													wsheet1.addCell(content);	
													wsheet1.setRowView(line[knum]+2, 500);
												}else if(j==5){//监考
													if(vec1.get(v+j).toString().indexOf(",")>-1){
														cellContent1=vec1.get(v+j).toString().split(",")[0];	
														content = new Label(j+knum*7, line[knum]+counum1, cellContent1, contentStyle);
														wsheet1.addCell(content);	
														
														cellContent1=vec1.get(v+j).toString().split(",")[1];	
														content = new Label(j+knum*7+1, line[knum]+counum1, cellContent1, contentStyle);
														wsheet1.addCell(content);	
													}else{
														cellContent1=vec1.get(v+j).toString();	
														content = new Label(j+knum*7, line[knum]+counum1, cellContent1, contentStyle);
														wsheet1.addCell(content);	
														
														cellContent1="";	
														content = new Label(j+knum*7+1, line[knum]+counum1, cellContent1, contentStyle);
														wsheet1.addCell(content);	
													}
												}else if(j==6){
													
												}else{
													cellContent1=vec1.get(v+j).toString();	
													content = new Label(j+knum*7, line[knum]+counum1, cellContent1, contentStyle);
													wsheet1.addCell(content);	
													wsheet1.setRowView(line[knum]+counum1, 500);
												}
											}
										}	
										if(knum==-1){
											
										}else{
											line[knum]++;
										}
									}
								}
								
								//补齐方框
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setShrinkToFit(true);
								//contentStyle.setWrap(true);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								
								int maxline=-1;
								for(int l=0;l<sjdnum;l++){
									if(maxline<line[l]){
										maxline=line[l];
									}
								}
	
								for(int l=0;l<sjdnum;l++){
									for(int n=line[l];n<maxline;n++){
										if(line[l]<maxline){
											for(int j=0;j<7;j++){
												cellContent1="";
												content = new Label(j+l*7, n+counum1, cellContent1, contentStyle);
												wsheet1.addCell(content);	
											}
										}	
									}
								}
								
								counum1=maxline+2+1;	
							}
						}
						
						//默认为横向打印
			            wsheet1.setPageSetup(PageOrientation.LANDSCAPE.LANDSCAPE,PaperSize.A3,0.5d,0.5d);
			            wsheet1.getSettings().setScaleFactor(90); 
					}
				
				}else{//前十周，前十五周考试
						wsheet[0] = wbook.createSheet(vecqzqm.get(0).toString(), 0);// 工作表名称
						// 设置列宽
//						for (int i = 0; i < titleVec.size(); i++) {
//							if(i==2){
//								wsheet.setColumnView(i, 40);
//							}
//							else{
//								wsheet.setColumnView(i, 16);
//							}
//						}
															
						
						//工作表 0------------------------------------------------------------------------
						int counum=0;//excel表中行数
						String cellContent = ""; //当前单元格的内容
						
						//第1行
	
						//设置课表标题行列字体大小
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中		
						
						wsheet[0].mergeCells(0, counum, 8, counum);
						cellContent = "行健学院"+this.getGG_XNXQBM().substring(0,4)+"学年度第"+this.getGG_XNXQBM().substring(4,5)+"学期"+vecqzqm.get(0).toString()+"安排表";
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[0].addCell(content);
						wsheet[0].setRowView(counum, 600);
						
						//第2行
						counum++;
						
						//生成标题
						String[] title=new String[]{"序号","课程名称","考试形式","班级名称","人数","考试时间","教室","监考","监考"};
						
						//设置列宽
						wsheet[0].setColumnView(0, 6);
						wsheet[0].setColumnView(1, 20);
						wsheet[0].setColumnView(2, 10);
						wsheet[0].setColumnView(3, 10);
						wsheet[0].setColumnView(4, 6);
						wsheet[0].setColumnView(5, 25);
						wsheet[0].setColumnView(6, 6);	
						wsheet[0].setColumnView(7, 8);		
						wsheet[0].setColumnView(8, 8);								
						
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
		
						for(int colNum=0; colNum<9; colNum++){
							cellContent=title[colNum%9];						
							content = new Label(colNum, counum, cellContent, contentStyle);
							wsheet[0].addCell(content);	
							wsheet[0].setRowView(counum, 500);
						}
					
						//合并监考教师格子
						wsheet[0].mergeCells(7, counum, 8, counum);
					
						//第3行
						counum++;
						
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);

						for(int v=0;v<vec.size();v=v+7){
							for(int j=0;j<9;j++){ 
								if(j==0){
									cellContent=(counum-1)+"";
									content = new Label(j, counum, cellContent, contentStyle);
									wsheet[0].addCell(content);	
									wsheet[0].setRowView(counum, 500);
								}else if(j==1){//课程名称
									cellContent=vec.get(v+j).toString();	
									content = new Label(j, counum, cellContent, contentStyle);
									wsheet[0].addCell(content);	
				
								}else if(j==2){//考试形式
									cellContent=vec.get(v+6).toString();	
									content = new Label(j, counum, cellContent, contentStyle);
									wsheet[0].addCell(content);	

								}else if(j==3||j==4){//班级名称，人数
									cellContent=vec.get(v+j-1).toString();	
									content = new Label(j, counum, cellContent, contentStyle);
									wsheet[0].addCell(content);	
						
								}else if(j==5){//考试时间
									String sj=vec.get(v).toString();
									String day=sj.substring(0,sj.indexOf("#"));
									String rq=""; 
									String st="";
									
									for(int t=0;t<ksrqts.length;t++){
										if(t==Integer.parseInt(day)){
											rq=ksrqts[t];
										}
									}
					
									//考试日期是星期几
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									Calendar cal = Calendar.getInstance();
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
									String str = rq.trim(); 
									try {
										cal.setTime(format.parse(rq.trim()));
									} catch (ParseException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									Date dt=sdf.parse(str,new ParsePosition(0)); 
									Calendar rightNow = Calendar.getInstance(); 
								    rightNow.setTime(dt); 
									//rightNow.add(Calendar.DATE,1);//你要加减的日期   
									//rightNow.add(Calendar.DATE,2);//你要加减的日期   
									Date dt1=rightNow.getTime(); 
									String reStr=sdf.format(dt1);
									int week = cal.get(Calendar.DAY_OF_WEEK)-1;
									String weekday="";
									if(week==1){
										weekday="一";
									}else if(week==2){
										weekday="二";
									}else if(week==3){
										weekday="三";
									}else if(week==4){
										weekday="四";
									}else if(week==5){
										weekday="五";
									}else if(week==6){
										weekday="六";
									}else if(week==0){
										weekday="日";
									}
									
									String sd=sj.substring(sj.indexOf("#")+1,sj.length());
									for(int s=0;s<kssjdts.length;s++){
										if(s==Integer.parseInt(sd)){
											st=kssjdts[s];
										}
									}
									
									cellContent=Integer.parseInt(reStr.split("-")[1])+"月"+Integer.parseInt(reStr.split("-")[2])+"日（周"+weekday+"）"+st;	
									content = new Label(j, counum, cellContent, contentStyle);
									wsheet[0].addCell(content);	

								}else if(j==6){//教室,第2个监考教师位置
									cellContent="";	
									content = new Label(j, counum, cellContent, contentStyle);
									wsheet[0].addCell(content);	
								}else if(j==7){//监考
									if(vec.get(v+j-2).toString().indexOf(",")>-1){
										cellContent=vec.get(v+j-2).toString().split(",")[0];	
										content = new Label(j, counum, cellContent, contentStyle);
										wsheet[0].addCell(content);	
													
										cellContent=vec.get(v+j-2).toString().split(",")[1];	
										content = new Label(j+1, counum, cellContent, contentStyle);
										wsheet[0].addCell(content);	
									}else{
										cellContent=vec.get(v+j-2).toString();	
										content = new Label(j, counum, cellContent, contentStyle);
										wsheet[0].addCell(content);	
													
										cellContent="";	
										content = new Label(j+1, counum, cellContent, contentStyle);
										wsheet[0].addCell(content);	
									}
								}else if(j==8){
									
								}
							}
							counum++;
						}

						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						wsheet[0].mergeCells(0, counum, 8, counum);
						cellContent="注意：";	
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[0].addCell(content);	
						wsheet[0].setRowView(counum, 500);
						counum++;
						
						wsheet[0].mergeCells(0, counum, 8, counum);
						cellContent="1、考试的同学必须带好校园一卡通，若一卡通遗失，必须带学生证、身份证（或住宿证、社保卡）二证参加考试，不带证件者将取消考试资格。";	
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[0].addCell(content);	
						wsheet[0].setRowView(counum, 500);
						counum++;
						
						wsheet[0].mergeCells(0, counum, 8, counum);
						cellContent="2、考试时间、形式以卷面考试时间、形式为准，请各位监考老师提醒学生。";	
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[0].addCell(content);	
						wsheet[0].setRowView(counum, 500);
						counum++;
						
						wsheet[0].mergeCells(0, counum, 8, counum);
						cellContent="3、选修考试课请各辅导员务必通知到每位学生。";	
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[0].addCell(content);	
						wsheet[0].setRowView(counum, 500);
						counum++;
						
						wsheet[0].mergeCells(0, counum, 8, counum);
						cellContent="4、请各系按考试表上的要求派老师前来监考，并请于考前15分钟到教务处报到。";	
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[0].addCell(content);	
						wsheet[0].setRowView(counum, 500);
						counum++;
						
						wsheet[0].mergeCells(0, counum, 8, counum);
						cellContent="5、上机考的监考老师请各系派会计算机操作的老师监考，并请于考前20分钟到教务处报到。";	
						content = new Label(0, counum, cellContent, contentStyle);
						wsheet[0].addCell(content);	
						wsheet[0].setRowView(counum, 500);
			
						//默认为横向打印
			            wsheet[0].setPageSetup(PageOrientation.PORTRAIT.PORTRAIT,PaperSize.A4,0.5d,0.5d);
			            wsheet[0].getSettings().setScaleFactor(86); 
					
				}
				
				// 写入数据
				wbook.write();
				// 关闭文件
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
		} else {
			this.setMSG("没有考试信息");
		}
		return savePath;
	}
	
	/**
	 * 导出试卷标签
	 * @author lupengfei
	 * @date:2017-06-05
	 * @return savePath 下载路径
	 * @throws SQLException
	*/
	public String ExportExcelSJBQ()throws SQLException {
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sqlkssz = "";
		String savePath="";
		Vector vec = new Vector();
		Vector vec2 = null;
		Vector vecins = new Vector();
		String xn=this.getGG_XNXQBM().substring(0,4);
		
		String kssjd= MyTools.getProp(request, "Base.examTime");//考试时间段
		String[] kssjdts=kssjd.split(",");
			
//		sql="select c.[课程名称],c.考试形式,c.[行政班简称],c.[学生人数],c.[时间序列],c.[场地要求],c.[监考教师姓名] from ( "+
//				" SELECT a.[课程名称],e.考试形式,b.[行政班简称],a.[学生人数],a.[时间序列],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b,dbo.V_考试形式 e where a.行政班代码=b.行政班代码 and a.考试形式=e.编号 and a.考场安排主表编号='"+this.getQZQM()+"' " +
//				" union " +
//				" SELECT a.[课程名称],e.考试形式,a.行政班名称 as [行政班简称],a.[学生人数],a.[时间序列],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,dbo.V_考试形式 e where a.专业代码='' and a.考试形式=e.编号 and a.考场安排主表编号='"+this.getQZQM()+"' "+
//				" ) c order by c.时间序列,c.课程名称,c.[行政班简称] ";
//		vec = db.GetContextVector(sql);
		
		sql=" SELECT a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b where a.行政班代码=b.行政班代码 and a.考场安排主表编号='"+this.getQZQM()+"' and [考试形式] not in ('4','5') " +
				" union " +
				" SELECT a.[时间序列],a.[课程名称],a.行政班名称 as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a where a.专业名称='' and a.考场安排主表编号='"+this.getQZQM()+"'  and [考试形式] not in ('4','5') ";
		vec2 = db.GetContextVector(sql);
		if(vec2!=null&&vec2.size()>0){
			//如果分两个教室，拆分vec2，保存到vec里
			for(int v=0;v<vec2.size();v=v+6){
				if(vec2.get(v+4).toString().indexOf(",")>-1){//有多个教室
					String[] room=vec2.get(v+4).toString().split(",");
					//for(int r=0;r<room.length;r++){
						vec.add(vec2.get(v).toString());//时间序列
						vec.add(vec2.get(v+1).toString());//课程名称
						vec.add(vec2.get(v+2).toString());//行政班简称
						
						int rs1=Integer.parseInt(vec2.get(v+3).toString())/2;
						vec.add("1-"+rs1);//学生人数
						
						vec.add(room[0]);//场地要求
						
						if(vec2.get(v+5).toString().equals("")){
							vec.add("");//监考教师姓名
						}else if(vec2.get(v+5).toString().indexOf(",")<0){
							vec.add(vec2.get(v+5).toString());//监考教师姓名
						}else if(vec2.get(v+5).toString().indexOf(",")>-1){
							String[] teacher=vec2.get(v+5).toString().split(",");
							if(teacher.length>2){
								vec.add(teacher[0]+","+teacher[1]);//监考教师姓名
							}else{//teacher.length==2
								vec.add(vec2.get(v+5).toString());//监考教师姓名
							}
						}
						//=====================================第2条
						vec.add(vec2.get(v).toString());//时间序列
						vec.add(vec2.get(v+1).toString());//课程名称
						vec.add(vec2.get(v+2).toString());//行政班简称
						vec.add((rs1+1)+"-"+Integer.parseInt(vec2.get(v+3).toString()));//学生人数
						vec.add(room[1]);//场地要求
						if(vec2.get(v+5).toString().equals("")){
							vec.add("");//监考教师姓名
						}else if(vec2.get(v+5).toString().indexOf(",")<0){
							vec.add("");//监考教师姓名
						}else if(vec2.get(v+5).toString().indexOf(",")>-1){
							String[] teacher=vec2.get(v+5).toString().split(",");
							if(teacher.length==3){
								vec.add(teacher[2]);//监考教师姓名
							}else if(teacher.length==4){
								vec.add(teacher[2]+","+teacher[3]);//监考教师姓名
							}else{//teacher.length==2
								vec.add("");//监考教师姓名
							}
						}
					//}
				}else{
					for(int u=0;u<6;u++){
						vec.add(vec2.get(v+u).toString());
					}		
				}		

			}
		}
		
		String sqlqzqm="select 考试名称 from dbo.V_考试管理_考场安排主表 where 考场安排主表编号='"+this.getQZQM()+"' ";
		Vector vecqzqm=db.GetContextVector(sqlqzqm);
		
		if (vec != null && vec.size() > 0) {			
			
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);
			savePath = MyTools.getProp(request, "Base.exportExcelPath");	
			
			//创建
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			savePath +=  "/行健学院"+this.getGG_XNXQBM().substring(0,4)+"学年第"+this.getGG_XNXQBM().substring(4,5)+"学期 "+vecqzqm.get(0).toString()+" 试卷标签.xls";
			
			try {
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				
				//获取日期，时间段信息
				String sqlksrq="select [学年学期编码],[考试名称],[考试类型],[上课截止周数],[考试日期] from [dbo].[V_考试管理_考场安排主表] where [考场安排主表编号]='"+this.getQZQM()+"' ";
				Vector vecksrq=db.GetContextVector(sqlksrq);
				String ksrq=vecksrq.get(4).toString();//考试日期
				String[] ksrqts=ksrq.split(",");
				
				//获取普通考试安排规则
				String sqlrqxx="select distinct [考试日期] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+this.getQZQM()+"' and [考试类型]='笔试' and [考试年级]!='' and [课程类型]!='' and [教室人数]!='' order by [考试日期] ";
				Vector vecrqxx=db.GetContextVector(sqlrqxx);
				String sqlksxx="select distinct [考试日期],[考试时间段] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+this.getQZQM()+"' and [考试类型]='笔试' and [考试年级]!='' and [课程类型]!='' and [教室人数]!='' order by [考试日期],[考试时间段] ";
				Vector vecksxx=db.GetContextVector(sqlksxx);
				WritableSheet[] wsheet = new WritableSheet[ksrqts.length];
				
				int bsnum=vecksxx.size()/2;//笔试sheet数量
				//for(int i=0;i<1;i++){
				for(int i=0;i<vecrqxx.size();i++){
					//计算当天用了几个时间段
					int sjdnum=0;						
					for(int j=0;j<vecksxx.size();j=j+2){
						if(vecksxx.get(j).toString().equals(vecrqxx.get(i).toString().trim())){//日期相等							
							sjdnum++;
						}
					}
					
					for(int d=0;d<sjdnum;d++){
						
						wsheet[i] = wbook.createSheet(vecrqxx.get(i).toString()+"("+(d+1)+")"+"笔试", i*sjdnum+d);// 工作表名称
						
						// 设置列宽
	//					for (int i = 0; i < titleVec.size(); i++) {
	//						if(i==2){
	//							wsheet.setColumnView(i, 40);
	//						}
	//						else{
	//							wsheet.setColumnView(i, 16);
	//						}
	//					}
						
						//考试日期是星期几
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
						String str = ksrqts[i].trim(); 
						try {
							cal.setTime(format.parse(ksrqts[i].trim()));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Date dt=sdf.parse(str,new ParsePosition(0)); 
						Calendar rightNow = Calendar.getInstance(); 
					    rightNow.setTime(dt); 
						//rightNow.add(Calendar.DATE,1);//你要加减的日期   
						//rightNow.add(Calendar.DATE,2);//你要加减的日期   
						Date dt1=rightNow.getTime(); 
						String reStr=sdf.format(dt1);
						int week = cal.get(Calendar.DAY_OF_WEEK)-1;
						String weekday="";
						if(week==1){
							weekday="一";
						}else if(week==2){
							weekday="二";
						}else if(week==3){
							weekday="三";
						}else if(week==4){
							weekday="四";
						}else if(week==5){
							weekday="五";
						}else if(week==6){
							weekday="六";
						}else if(week==0){
							weekday="日";
						}
						
						//工作表 0------------------------------------------------------------------------
						int counum=-1;//excel表中行数
						String cellContent = ""; //当前单元格的内容
						
							
						//每个时间段排第几
						int onum=0;
						String[] sjdt=new String[sjdnum];
						for(int j=0;j<vecksxx.size();j=j+2){
							if(vecksxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
								for(int s=0;s<kssjdts.length;s++){
									if(vecksxx.get(j+1).toString().equals(kssjdts[s])){//时间段相同
										sjdt[onum]=s+"";
									}
								}
								onum++;
							}
						}	
						
						String time="";
						for(int r=0;r<kssjdts.length;r++){
							if(Integer.parseInt(sjdt[d])==r){
								time=kssjdts[r];
							}
						}
						String name=Integer.parseInt(reStr.split("-")[1])+"月"+Integer.parseInt(reStr.split("-")[2])+"日(周"+weekday+")"+time;	
						
						//第2行
										
	//					int ksnum=0;
	//					for(int j=0;j<vecksxx.size();j=j+2){
	//						if(vecksxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
	//							ksnum=j;
	//							break;
	//						}
	//					}
	//					
	//					String name=Integer.parseInt(reStr.split("-")[1])+"月"+Integer.parseInt(reStr.split("-")[2])+"日(周"+weekday+")";	
	//					for(int k=0;k<sjdnum;k++){
	//						wsheet[i].mergeCells(0+k*7, counum, 6+k*7, counum);						
	//						cellContent=name+" "+vecksxx.get(ksnum+1).toString();
	//						content = new Label(0+k*7, counum, cellContent, contentStyle);
	//						wsheet[i].addCell(content);
	//						wsheet[i].setRowView(counum, 500);
	//						ksnum=ksnum+2;
	//					}
						
						
						//生成标题
						String[] title=new String[]{"试场","考试时间","科目","班级","人数","教室","监考","监考"};
						for(int k=0;k<8;k++){
							//设置列宽
							wsheet[i].setColumnView(0, 5);
							wsheet[i].setColumnView(1, 29);
							wsheet[i].setColumnView(2, 13);
							wsheet[i].setColumnView(3, 8);
							wsheet[i].setColumnView(4, 5);
							wsheet[i].setColumnView(5, 5);
							wsheet[i].setColumnView(6, 6);	
							wsheet[i].setColumnView(7, 6);		
						}
						
		
	//					for(int k=0;k<sjdnum;k++){
	//						//合并监考教师格子
	//						wsheet[i].mergeCells(k*7+5, counum, k*7+6, counum);
	//					}
					
						//第4行
						counum++;
									
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						
						for(int v=0;v<vec.size();v=v+6){
							String sjxl=vec.get(v).toString();
							//System.out.println(sjxl+"|"+i);
							if(sjxl.substring(0,sjxl.indexOf("#")).equals(i+"")){//日期相同
								int knum=-1;
								
								if(sjxl.substring(sjxl.indexOf("#")+1,sjxl.length()).equals(sjdt[d])){
									knum=d;
								}
								
								if(knum==-1){
									
								}else{
									//标题
									for(int colNum=0; colNum<8; colNum++){
										cellContent=title[colNum];						
										content = new Label(colNum, counum, cellContent, contentStyle);
										wsheet[i].addCell(content);	
										wsheet[i].setRowView(counum, 500);
									}
									counum++;
									
									for(int j=0;j<8;j++){	
										if(j==0){
											cellContent=counum/2+1+"";
											content = new Label(j, counum, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(counum, 500);
										}else if(j==1){
											cellContent=name;
											content = new Label(j, counum, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(counum, 500);
										}else if(j==6){//监考
											if(vec.get(v+j-1).toString().indexOf(",")>-1){
												cellContent=vec.get(v+j-1).toString().split(",")[0];	
												content = new Label(j, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
												
												cellContent=vec.get(v+j-1).toString().split(",")[1];	
												content = new Label(j+1, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
											}else{
												cellContent=vec.get(v+j-1).toString();	
												content = new Label(j, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
												
												cellContent="";	
												content = new Label(j+1, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
											}
										}else if(j==7){
											
										}else{
											cellContent=vec.get(v+j-1).toString();	
											content = new Label(j, counum, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(counum, 500);
										}
									}
									counum++;
								}	
							}
						}
			
						//默认为横向打印
			            wsheet[i].setPageSetup(PageOrientation.PORTRAIT.PORTRAIT,PaperSize.A4,0.5d,0.5d);  
			            wsheet[i].getSettings().setScaleFactor(100);
					}
				}			
				//contentStyle.setShrinkToFit(true);//字体大小自适应

				//上机
				Vector vec1=new Vector();
				sql=" SELECT a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b where a.行政班代码=b.行政班代码 and a.考场安排主表编号='"+this.getQZQM()+"' and [考试形式] in ('4','5') " +
						" union " +
						" SELECT a.[时间序列],a.[课程名称],a.行政班名称 as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a where a.专业名称='' and a.考场安排主表编号='"+this.getQZQM()+"'  and [考试形式] in ('4','5') ";
				vec2 = db.GetContextVector(sql);
				if(vec2!=null&&vec2.size()>0){
					//如果分两个教室，拆分vec2，保存到vec里
					for(int v=0;v<vec2.size();v=v+6){
						if(vec2.get(v+4).toString().indexOf(",")>-1){//有多个教室
							String[] room=vec2.get(v+4).toString().split(",");
							//for(int r=0;r<room.length;r++){
								vec1.add(vec2.get(v).toString());//时间序列
								vec1.add(vec2.get(v+1).toString());//课程名称
								vec1.add(vec2.get(v+2).toString());//行政班简称
								
								int rs1=Integer.parseInt(vec2.get(v+3).toString())/2;
								vec1.add("1-"+rs1);//学生人数
								
								vec1.add(room[0]);//场地要求
								
								if(vec2.get(v+5).toString().equals("")){
									vec1.add("");//监考教师姓名
								}else if(vec2.get(v+5).toString().indexOf(",")<0){
									vec1.add(vec2.get(v+5).toString());//监考教师姓名
								}else if(vec2.get(v+5).toString().indexOf(",")>-1){
									String[] teacher=vec2.get(v+5).toString().split(",");
									if(teacher.length>2){
										vec1.add(teacher[0]+","+teacher[1]);//监考教师姓名
									}else{//teacher.length==2
										vec1.add(vec2.get(v+5).toString());//监考教师姓名
									}
								}
								//=====================================第2条
								vec1.add(vec2.get(v).toString());//时间序列
								vec1.add(vec2.get(v+1).toString());//课程名称
								vec1.add(vec2.get(v+2).toString());//行政班简称
								vec1.add((rs1+1)+"-"+Integer.parseInt(vec2.get(v+3).toString()));//学生人数
								vec1.add(room[1]);//场地要求
								if(vec2.get(v+5).toString().equals("")){
									vec1.add("");//监考教师姓名
								}else if(vec2.get(v+5).toString().indexOf(",")<0){
									vec1.add("");//监考教师姓名
								}else if(vec2.get(v+5).toString().indexOf(",")>-1){
									String[] teacher=vec2.get(v+5).toString().split(",");
									if(teacher.length==3){
										vec1.add(teacher[2]);//监考教师姓名
									}else if(teacher.length==4){
										vec1.add(teacher[2]+","+teacher[3]);//监考教师姓名
									}else{//teacher.length==2
										vec1.add("");//监考教师姓名
									}
								}
							//}
						}else{
							for(int u=0;u<6;u++){
								vec1.add(vec2.get(v+u).toString());
							}		
						}		

					}
				}
				
								
				//获取普通考试安排规则
				String sqlrqsjxx="select distinct [考试日期] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+this.getQZQM()+"' and [考试类型]='上机' and [考试年级]!='' and [课程类型]!='' and [教室人数]!='' order by [考试日期] ";
				Vector vecrqsjxx=db.GetContextVector(sqlrqsjxx);
				String sqlkssjxx="select distinct [考试日期],[考试时间段] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+this.getQZQM()+"' and [考试类型]='上机' and [考试年级]!='' and [课程类型]!='' and [教室人数]!='' order by [考试日期],[考试时间段] ";
				Vector veckssjxx=db.GetContextVector(sqlkssjxx);			
				
				//for(int i=0;i<1;i++){
				for(int i=0;i<vecrqsjxx.size();i++){
					//计算当天用了几个时间段
					int sjdnum=0;						
					for(int j=0;j<veckssjxx.size();j=j+2){
						if(veckssjxx.get(j).toString().equals(vecrqsjxx.get(i).toString().trim())){//日期相等							
							sjdnum++;
						}
					}
					
					for(int d=0;d<sjdnum;d++){
						
						wsheet[i] = wbook.createSheet(vecrqsjxx.get(i).toString()+"("+(d+1)+")"+"上机", i*sjdnum+d+bsnum);// 工作表名称
						
						// 设置列宽
	//					for (int i = 0; i < titleVec.size(); i++) {
	//						if(i==2){
	//							wsheet.setColumnView(i, 40);
	//						}
	//						else{
	//							wsheet.setColumnView(i, 16);
	//						}
	//					}
						
						//考试日期是星期几
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
						String str = ksrqts[i].trim(); 
						try {
							cal.setTime(format.parse(ksrqts[i].trim()));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Date dt=sdf.parse(str,new ParsePosition(0)); 
						Calendar rightNow = Calendar.getInstance(); 
					    rightNow.setTime(dt); 
						//rightNow.add(Calendar.DATE,1);//你要加减的日期   
						//rightNow.add(Calendar.DATE,2);//你要加减的日期   
						Date dt1=rightNow.getTime(); 
						String reStr=sdf.format(dt1);
						int week = cal.get(Calendar.DAY_OF_WEEK)-1;
						String weekday="";
						if(week==1){
							weekday="一";
						}else if(week==2){
							weekday="二";
						}else if(week==3){
							weekday="三";
						}else if(week==4){
							weekday="四";
						}else if(week==5){
							weekday="五";
						}else if(week==6){
							weekday="六";
						}else if(week==0){
							weekday="日";
						}
						
						//工作表 0------------------------------------------------------------------------
						int counum=-1;//excel表中行数
						String cellContent = ""; //当前单元格的内容
						
							
						//每个时间段排第几
						int onum=0;
						String[] sjdt=new String[sjdnum];
						for(int j=0;j<veckssjxx.size();j=j+2){
							if(veckssjxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
								for(int s=0;s<kssjdts.length;s++){
									if(veckssjxx.get(j+1).toString().equals(kssjdts[s])){//时间段相同
										sjdt[onum]=s+"";
									}
								}
								onum++;
							}
						}	
						
						String time="";
						for(int r=0;r<kssjdts.length;r++){
							if(Integer.parseInt(sjdt[d])==r){
								time=kssjdts[r];
							}
						}
						String name=Integer.parseInt(reStr.split("-")[1])+"月"+Integer.parseInt(reStr.split("-")[2])+"日(周"+weekday+")"+time;	
						
						//第2行
										
	//					int ksnum=0;
	//					for(int j=0;j<vecksxx.size();j=j+2){
	//						if(vecksxx.get(j).toString().equals(ksrqts[i].trim())){//日期相等
	//							ksnum=j;
	//							break;
	//						}
	//					}
	//					
	//					String name=Integer.parseInt(reStr.split("-")[1])+"月"+Integer.parseInt(reStr.split("-")[2])+"日(周"+weekday+")";	
	//					for(int k=0;k<sjdnum;k++){
	//						wsheet[i].mergeCells(0+k*7, counum, 6+k*7, counum);						
	//						cellContent=name+" "+vecksxx.get(ksnum+1).toString();
	//						content = new Label(0+k*7, counum, cellContent, contentStyle);
	//						wsheet[i].addCell(content);
	//						wsheet[i].setRowView(counum, 500);
	//						ksnum=ksnum+2;
	//					}
						
						
						//生成标题
						String[] title=new String[]{"试场","考试时间","科目","班级","人数","教室","监考","监考"};
						for(int k=0;k<8;k++){
							//设置列宽
							wsheet[i].setColumnView(0, 5);
							wsheet[i].setColumnView(1, 29);
							wsheet[i].setColumnView(2, 13);
							wsheet[i].setColumnView(3, 8);
							wsheet[i].setColumnView(4, 5);
							wsheet[i].setColumnView(5, 5);
							wsheet[i].setColumnView(6, 6);	
							wsheet[i].setColumnView(7, 6);		
						}
						
		
	//					for(int k=0;k<sjdnum;k++){
	//						//合并监考教师格子
	//						wsheet[i].mergeCells(k*7+5, counum, k*7+6, counum);
	//					}
					
						//第4行
						counum++;
									
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						//contentStyle.setWrap(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						
						for(int v=0;v<vec1.size();v=v+6){
							String sjxl=vec1.get(v).toString();
							
							if(sjxl.substring(0,sjxl.indexOf("#")).equals(i+"")){//日期相同
								int knum=-1;
								
								if(sjxl.substring(sjxl.indexOf("#")+1,sjxl.length()).equals(sjdt[d])){
									knum=d;
								}
								
								if(knum==-1){
									
								}else{
									//标题
									for(int colNum=0; colNum<8; colNum++){
										cellContent=title[colNum];						
										content = new Label(colNum, counum, cellContent, contentStyle);
										wsheet[i].addCell(content);	
										wsheet[i].setRowView(counum, 500);
									}
									counum++;
									
									for(int j=0;j<8;j++){	
										if(j==0){
											cellContent=counum/2+1+"";
											content = new Label(j, counum, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(counum, 500);
										}else if(j==1){
											cellContent=name;
											content = new Label(j, counum, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(counum, 500);
										}else if(j==6){//监考
											if(vec1.get(v+j-1).toString().indexOf(",")>-1){
												cellContent=vec1.get(v+j-1).toString().split(",")[0];	
												content = new Label(j, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
												
												cellContent=vec1.get(v+j-1).toString().split(",")[1];	
												content = new Label(j+1, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
											}else{
												cellContent=vec1.get(v+j-1).toString();	
												content = new Label(j, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
												
												cellContent="";	
												content = new Label(j+1, counum, cellContent, contentStyle);
												wsheet[i].addCell(content);	
											}
										}else if(j==7){
											
										}else{
											cellContent=vec1.get(v+j-1).toString();	
											content = new Label(j, counum, cellContent, contentStyle);
											wsheet[i].addCell(content);	
											wsheet[i].setRowView(counum, 500);
										}
									}
									counum++;
								}	
							}
						}
			
						//默认为横向打印
			            wsheet[i].setPageSetup(PageOrientation.PORTRAIT.PORTRAIT,PaperSize.A4,0.5d,0.5d);  
			            wsheet[i].getSettings().setScaleFactor(100);  
					}
				}

				// 写入数据
				wbook.write();
				// 关闭文件
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
		} else {
			this.setMSG("没有考试信息");
		}
		return savePath;
	}
	
	/**
	 * 大补考答疑表导出
	 * @author lupengfei
	 * @date:2016-11-16
	 * @return savePath 下载路径
	 * @throws SQLException
	*/
	public String ExportExcelDBKDY()throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sqlkssz = "";
		Vector vec = new Vector();
		Vector vec3 = null;
		Vector vec4 = null;
		Vector vec5 = null;
		Vector vecins = new Vector();
		String savePath="";
		String xn=this.getGG_XNXQBM().substring(0,4);
		
			
		sql="select distinct 授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式编号,考试形式, " +
				"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from ( " +
				"select case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when f.考试形式 in ('1','2','3','10') then '1' when f.考试形式 in ('4','5') then '4' when f.考试形式 in ('9','11','12','13','16') then '9' else f.考试形式 end as 考试形式编号,k.考试形式 as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when c.年级代码=(cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式大补考 k on f.考试形式=k.编号  " +
				"where d.学生状态 in ('01','05','08') and 成绩状态='1' " +
				"and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1') " +
				") as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " ;

		sql+=" group by 授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式编号,考试形式,系名称 order by 课程名称,行政班简称 ";
			
		vec5=db.GetContextVector(sql);
			
		//去除整班未登分
		sql4="select 相关编号 from (select distinct a.相关编号,sum(case when 成绩<>'' then 1 else 0 end) as 已登分人数 " +
					"from V_成绩管理_登分教师信息表 a " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
					"left join (select ta.学号,ta.相关编号," +
					"case when (case when ta.平时 in ('-1','-5') then '' else ta.平时 end)<>'' then ta.平时 " +
					"when (case when ta.期中 in ('-1','-5') then '' else ta.期中 end)<>'' then ta.期中 " +
					"when (case when ta.实训 in ('-1','-5') then '' else ta.实训 end)<>'' then ta.实训 " +
					"when (case when ta.期末 in ('-1','-5') then '' else ta.期末 end)<>'' then ta.期末 " +
					"when (case when ta.总评 in ('-1','-5') then '' else ta.总评 end)<>'' then ta.总评 " +
					"when (case when ta.重修1 in ('-1','-5') then '' else ta.重修1 end)<>'' then ta.重修1 " +
					"when (case when ta.重修2 in ('-1','-5') then '' else ta.重修2 end)<>'' then ta.重修2 " +
					"when (case when ta.补考 in ('-1','-5') then '' else ta.补考 end)<>'' then ta.补考 " +
					"when (case when ta.大补考 in ('-1','-5') then '' else ta.大补考 end)<>'' then ta.大补考 else '' end as 成绩 " +
					"from V_成绩管理_学生成绩信息表 ta " +
					"left join V_成绩管理_科目课程信息表 tb on ta.科目编号=tb.科目编号 " +
					"where ta.状态='1' and ta.成绩状态='1' and cast(left(tb.学年学期编码,4) as int) between " + (Integer.parseInt(xn)-6) + " and " + xn + " ) c on c.相关编号=a.相关编号 " +
					"where a.状态='1' and b.状态='1' and cast(left(b.学年学期编码,4) as int) between " + (Integer.parseInt(xn)-6) + " and " + xn + " " +
					"group by a.相关编号,a.行政班代码,a.来源类型) as t " +
					"where t.已登分人数=0 ";
		vec4=db.GetContextVector(sql4);
			
		if(vec4!=null&&vec4.size()>0) {
				int sametag=0;
				for(int i=0;i<vec5.size();i=i+9){
					sametag=0;
					for(int j=0;j<vec4.size();j++) {
						if(vec5.get(i).toString().equals(vec4.get(j).toString())) {//授课计划编号相同，整班未登分删掉
							sametag=1;
						}
					}
					if(sametag==0) {
						vec.add(vec5.get(i).toString());//授课计划明细编号 1
						vec.add(vec5.get(i+1).toString());//学年学期 2
						vec.add(vec5.get(i+2).toString());//课程名称 3
						vec.add(vec5.get(i+3).toString());//人数 4
						vec.add(vec5.get(i+4).toString());//考试形式 5
						vec.add(vec5.get(i+5).toString());//考试形式 6
						vec.add(vec5.get(i+6).toString());//系名称 7
						vec.add(vec5.get(i+7).toString());//行政班简称 8
						vec.add(vec5.get(i+8).toString());//行政班名称 9	
					}else {
						
					}
				}
			}
			
			String skjhmxs="";
			sql3="SELECT count(*) FROM [dbo].[V_考试管理_大补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
			if(db.getResultFromDB(sql3)){
				sql3="SELECT [授课计划明细编号] FROM [dbo].[V_考试管理_大补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
				vec3=db.GetContextVector(sql3);
				for(int j=0;j<vec3.size();j++){
					skjhmxs+=vec3.get(j).toString()+"@";
				}	
			}else{
				
			}
			
			Vector vecr=new Vector();
			Vector vecs=new Vector();
			Vector vect=new Vector();
			Vector vecq=new Vector();
			for(int i=0;vec.size()>1;){
				String skjhmx=vec.get(i).toString();
				String xnxqbm=vec.get(i+1).toString();
				String kcmc=vec.get(i+2).toString();
				String rs=vec.get(i+3).toString();
				String ksxsbh=vec.get(i+4).toString();
				String ksxs=vec.get(i+5).toString();
				String xmc=vec.get(i+6).toString();
				String xzbjc=vec.get(i+7).toString();
				String xzbmc=vec.get(i+8).toString();
				//在合并信息中,保存到vecs
				if(!skjhmxs.equals("")&&skjhmxs.indexOf(vec.get(i).toString())>-1){
					vecs.add(skjhmx);//授课计划明细编号 1
					vecs.add(xnxqbm);//学年学期 2
					vecs.add(kcmc);//课程名称 3
					vecs.add(rs);//人数 4
					vecs.add(ksxsbh);//考试形式编号 5
					vecs.add(ksxs);//考试形式 6
					vecs.add(xmc);//系名称 7
					vecs.add(xzbjc);//行政班简称 8
					vecs.add(xzbmc);//行政班名称 9
				}else{	
					for(int j=9;j<vec.size();j=j+9){
						//是选修课
						if(vec.get(i+2).toString().indexOf("选修")>-1){
							//课程名称 相同 合并
							if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+2).toString().equals(vec.get(j+2).toString())){
								skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
								xnxqbm=vec.get(j+1).toString();//学年学期 2
								//课程名称 3
								rs+="$"+vec.get(j+3).toString();//人数 4
								//考试形式 5
								xmc+="$"+vec.get(j+6).toString();//系名称 7
								xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
								xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
								
								for(int k=0;k<9;k++){
									vec.remove(j);
								}
								j=j-9;
							}
							
						}else{
							//课程名称,系名称,班级简称 相同 合并
							if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+2).toString().equals(vec.get(j+2).toString())&&vec.get(i+6).toString().equals(vec.get(j+6).toString())&&(vec.get(i+7).toString().equals(vec.get(j+7).toString())||vec.get(i+7).toString().substring(2,4).equals(vec.get(j+7).toString().substring(2,4)))){
								skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
								xnxqbm=vec.get(j+1).toString();//学年学期 2
								//课程名称 3
								rs+="$"+vec.get(j+3).toString();//人数 4
								//考试形式 5
								//系名称 6
								xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
								xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
								
								for(int k=0;k<9;k++){
									vec.remove(j);
								}
								j=j-9;
							}	
						}
					}
					
					vecr.add(skjhmx);//授课计划明细编号 1
					vecr.add(xnxqbm);//学年学期 2
					vecr.add(kcmc);//课程名称 3
					vecr.add(rs);//人数 4
					//vecr.add(ksxsbh);//考试形式 5
					vecr.add(ksxs);//考试形式 6
					vecr.add(xmc);//系名称 7
					vecr.add(xzbjc);//行政班简称 8
					vecr.add(xzbmc);//行政班名称 9			
				}
				for(int k=0;k<9;k++){
					vec.remove(i);
				}
		}	
			
		if(vec3!=null&&vec3.size()>0){
				for(int i=0;i<vec3.size();i++){
					String sskjhmx="";
					String sxnxqbm="";
					String skcmc="";
					String srs="";
					String sksxsbh="";
					String sksxs="";
					String sxmc="";
					String sxzbjc="";
					String sxzbmc="";
					for(int j=0;j<vecs.size();j=j+9){
						if(vec3.get(i).toString().indexOf(vecs.get(j).toString())>-1){
							sxnxqbm+=vecs.get(j+1).toString()+"@";
							skcmc+=vecs.get(j+2).toString()+"@";
							srs+=vecs.get(j+3).toString()+"@";
							sksxsbh=vecs.get(j+4).toString();
							sksxs=vecs.get(j+5).toString();
							sxmc+=vecs.get(j+6).toString()+"@";
							sxzbjc+=vecs.get(j+7).toString()+"@";
							sxzbmc+=vecs.get(j+8).toString()+"@";	
						}	
					}
					
					sskjhmx=vec3.get(i).toString();
					int max=0;
					if(!sxnxqbm.equals("")){
						sxnxqbm=sxnxqbm.substring(0,sxnxqbm.length()-1);
						
						String[] xq=sxnxqbm.split("@");
						max=Integer.parseInt(xq[0]);
						for(int k=0;k<xq.length;k++){
							if(max<Integer.parseInt(xq[k])){
								max=Integer.parseInt(xq[k]);
							}
						}
					}
					if(!skcmc.equals("")){
						skcmc=skcmc.substring(0,skcmc.length()-1);
					}
					if(!srs.equals("")){
						srs=srs.substring(0,srs.length()-1);
					}
					if(!sxmc.equals("")){
						sxmc=sxmc.substring(0,sxmc.length()-1);
					}
					if(!sxzbjc.equals("")){
						sxzbjc=sxzbjc.substring(0,sxzbjc.length()-1);
					}
					if(!sxzbmc.equals("")){
						sxzbmc=sxzbmc.substring(0,sxzbmc.length()-1);
					}
					
					if(!sxnxqbm.equals("")){
						vecq.add(sskjhmx);//授课计划明细编号 1
						vecq.add(max+"");//学年学期 2
						vecq.add(skcmc);//课程名称 3
						vecq.add(srs);//人数 4
						//vect.add(sksxsbh);//考试形式 5
						vecq.add(sksxs);//考试形式 6
						vecq.add(sxmc);//系名称 7
						vecq.add(sxzbjc);//行政班简称 8
						vecq.add(sxzbmc);//行政班名称 9	
					}
				}
		}		
			
		for(int i=0;i<vecr.size();i++){
			vecq.add(vecr.get(i).toString());
		}	
		
		//总人数
//				int totalnum=0;
//				for(int i=0;i<vect.size();i=i+8){
//					
//					String region=vect.get(i+3).toString();
//					region=region.replaceAll("@", "\\$");
//					int showrs=0;
//					if(region.indexOf("$")>-1){
//						String[] kcmcinfo=region.split("\\$");
//						if(region.indexOf("$")>-1){
//							for(int k=0;k<kcmcinfo.length;k++){
//								showrs+=Integer.parseInt(kcmcinfo[k]);
//							}
//						}else{
//							showrs=Integer.parseInt(region);
//						}
//					}else{
//						showrs=Integer.parseInt(vect.get(i+3).toString());
//					}
//					totalnum+=showrs;
//				}
//				System.out.println("rst:--"+totalnum);
	
		String sqlorder="";
		sqlorder="select * from (";
		for(int i=0;i<vecq.size();i=i+8) {
			sqlorder+="select '"+vecq.get(i).toString()+"' as skjh,'"+vecq.get(i+1).toString()+"' as xnxq,'"+vecq.get(i+2).toString()+"' as kcmc,'"+vecq.get(i+3).toString()+"' as rs,'"+vecq.get(i+4).toString()+"' as ksxs,'"+vecq.get(i+5).toString()+"' as xmc,'"+vecq.get(i+6).toString()+"' as xzbjc,'"+vecq.get(i+7).toString()+"' as xzbmc union ";
		}
		if(!sqlorder.equals("")) {
			sqlorder=sqlorder.substring(0, sqlorder.length()-6);
		}
		sqlorder+=" ) x order by kcmc ";
		vect=db.GetContextVector(sqlorder);
		
		if (vect != null && vect.size() > 0) {
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);
			savePath = MyTools.getProp(request, "Base.exportExcelPath");	
			
			//创建
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			savePath +=  "/" + xn + "大补考答疑表安排表.xls";
			
			try {
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				// 设置列宽
//				for (int i = 0; i < titleVec.size(); i++) {
//					if(i==2){
//						wsheet.setColumnView(i, 40);
//					}
//					else{
//						wsheet.setColumnView(i, 16);
//					}
//				}

				//生成标题
				String[] title=new String[]{"序号","学年学期","课程名称","人数","考核形式","系","专业","辅导教师","时间","地点"};
				int counum=0;//excel表中行数
				String cellContent = ""; //当前单元格的内容
				
				//设置列宽
				wsheet.setColumnView(0, 5);
				wsheet.setColumnView(1, 8);
				wsheet.setColumnView(2, 37);
				wsheet.setColumnView(3, 5);
				wsheet.setColumnView(4, 8);
				wsheet.setColumnView(5, 8);
				wsheet.setColumnView(6, 20);
				wsheet.setColumnView(7, 8);
				wsheet.setColumnView(8, 24);
				wsheet.setColumnView(9, 10);
				
				//第1行
				//设置课表标题行列字体大小
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet.mergeCells(0, counum, 9, counum);
				cellContent = (Integer.parseInt(xn.substring(2,4))-2)+"级1-4学期（含"+(Integer.parseInt(xn.substring(2,4))-4)+"、"+(Integer.parseInt(xn.substring(2,4))-3)+"级）大补考辅导答疑安排表";
				content = new Label(0, counum, cellContent, contentStyle);
				wsheet.addCell(content);
				wsheet.setRowView(counum, 600);	
				
				//第2行
				counum++;
				wsheet.mergeCells(0, counum, 9, counum);
				cellContent="注：大作业、口试、听力、实训、随堂考试直接由辅导教师组织考试，请辅导教师于12月25日前将大补考成绩单及大作业、口试表等交教务处。";
				content = new Label(0, counum, cellContent, contentStyle);
				wsheet.addCell(content);
				wsheet.setRowView(counum, 500);
			
				//第3行
				counum++;
				for(int colNum=0; colNum<10; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setShrinkToFit(true);
					//contentStyle.setWrap(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==9){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}								
					cellContent=title[colNum];						
					content = new Label(colNum, counum, cellContent, contentStyle);
					wsheet.addCell(content);	
				}
				wsheet.setRowView(counum, 500);
				
				//第4行
				counum++;
				
				for(int colNum=0; colNum<10; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setWrap(true);
					contentStyle.setShrinkToFit(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==9){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}
					cellContent=title[colNum];						
					content = new Label(colNum, counum, cellContent, contentStyle);
					wsheet.addCell(content);	
				}
					
				for(int i=0;i<vect.size();i=i+8){ 
					for(int colNum=0; colNum<10; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent=(counum-2)+"";
						}else if(colNum>6){
							cellContent="";
						}else if(colNum==1){
							cellContent=vect.get(i+colNum).toString();
						}else if(colNum==3){
							String region=vect.get(i+colNum).toString();
							region=region.replaceAll("@", "\\$");
							int showrs=0;
							if(region.indexOf("$")>-1){
								String[] kcmcinfo=region.split("\\$");
								if(region.indexOf("$")>-1){
									for(int k=0;k<kcmcinfo.length;k++){
										showrs+=Integer.parseInt(kcmcinfo[k]);
									}
								}else{
									showrs=Integer.parseInt(region);
								}
							}else{
								showrs=Integer.parseInt(vect.get(i+colNum).toString());
							}
							cellContent=showrs+"";
						}else{
							String region=vect.get(i+colNum).toString();
							region=region.replaceAll("@", "\\$");
							String showkcmc="";
							if(region.indexOf("$")>-1){
								String[] kcmcinfo=region.split("\\$");
								for(int k=0;k<kcmcinfo.length;k++){
									if(showkcmc.equals("")){
										showkcmc=kcmcinfo[k]+"、";
									}
									if(showkcmc.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
									
									}else{
										showkcmc+=kcmcinfo[k]+"、";
									}
								}
								showkcmc=showkcmc.substring(0,showkcmc.length()-1);
							}else{
								showkcmc=vect.get(i+colNum).toString();
							}
							cellContent=showkcmc;
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vect.size()-8){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}						
						}else if(colNum==9){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vect.size()-8){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vect.size()-8){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
						}
						content = new Label(colNum, counum, cellContent, contentStyle);
						wsheet.addCell(content);
						
					}
					wsheet.setRowView(counum, 480);
					counum++;	
				}
				//contentStyle.setShrinkToFit(true);//字体大小自适应
				
				// 写入数据
				wbook.write();
				// 关闭文件
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
			} else {
				this.setMSG("没有符合条件的成绩信息");
			}
			return savePath;
	}
	
	/**
	 * 大补考安排表导出
	 * @author lupengfei
	 * @date:2016-11-24
	 * @return savePath 下载路径
	 * @throws SQLException
	*/
	public String ExportExcelDBKAP(String tiyutime,String qtlxtime)throws SQLException {
		HttpSession session = request.getSession();
		Vector classVec = null;
		Vector tempVec = null;
		Vector allWpkcVec = new Vector();//未排课程信息
		Vector sqlVec = new Vector();
		String sql = "";
		String sql2 = "";
		String sql4 = "";
		String sql5 = "";
		String result = "";//返回结果
		String xqzc = "";//学期周次范围 
		int dayNum = 0; //每周天数
		int lessonNum = 0; //每天节数
		int monNum = 0;//上午节数
		int noonNum = 0;//中午节数
		int afternoonNum = 0;//下午节数
		int eveNum = 0;//晚上节数
		String wpks="";//未排考试
		Vector vec2=null;
		Vector vec4=null;
		Vector vec5=null;
		Vector vec=new Vector(); //排教室信息
		Vector vecsjxl=new Vector(); //保存已随机到的时间序列
		String maxMxId="";
		String sqladd="";
		Vector vecadd=new Vector(); 
		Vector vecupr=new Vector(); 
		Vector vecupd=new Vector(); 
		String sqlcls="";//大于50人教室
		Vector veccls=null;
		String sqlcls2="";//小于50人教室
		Vector veccls2=null;
		String sqlcls3="";//计算机房
		Vector veccls3=null;
		int overnum=0;
		int errjs=0;
		String xidm="";//分层班系代码
		String xinj="";//分层班年级
		String xiorder="";//分层班所排位置

		String xnxqbm=this.getGG_XNXQBM();
		String qzqm=this.getKCAPZBBH();
		//开始排考试--------------------------------------------------------------------------------
		
		String sqlxi="select [系代码] from dbo.V_基础信息_系基础信息表  where len([系代码])>1";
		Vector vecxi=db.GetContextVector(sqlxi);
		
		String sqlmajor="select [系代码],[专业代码] from dbo.V_基础信息_系专业关系表  where len([系代码])>1";
		Vector vecmajor=db.GetContextVector(sqlmajor);
		
		//获取日期，时间段信息
		String sqlksrq="select [学年学期编码],[考试名称],[考试类型],[上课截止周数],[考试日期] from [dbo].[V_考试管理_考场安排主表] where [考场安排主表编号]='"+qzqm+"' ";
		Vector vecksrq=db.GetContextVector(sqlksrq);
		String ksrq=vecksrq.get(4).toString();//考试日期
		String[] ksrqts=ksrq.split(",");

		String kssjd= MyTools.getProp(request, "Base.examTime");//考试时间段
		String[] kssjdts=kssjd.split(",");
		
		//获取特殊课程安排规则
		String sqltskcxx="select [考试场次编号],[日期时间段] from V_考试管理_考试特殊课程规则 where [考试编号]='"+qzqm+"' ";
		Vector vectskcxx=db.GetContextVector(sqltskcxx);
		Vector vecspkcxx=new Vector();
		String tsksrqid="";
		String tskssjdid="";
		if(vectskcxx!=null&&vectskcxx.size()>0){
			for(int i=0;i<vectskcxx.size();i=i+2){
				String[] rqsjd=vectskcxx.get(i+1).toString().split(",");
				for(int m=0;m<rqsjd.length;m++){
					for(int j=0;j<ksrqts.length;j++){ 
						if(rqsjd[m].split("#")[0].equals(ksrqts[j].trim())){
							tsksrqid=j+"";
						}
					}
					for(int k=0;k<kssjdts.length;k++){
						if(rqsjd[m].split("#")[1].equals(kssjdts[k])){
							tskssjdid=k+"";
						}
					}
					vecspkcxx.add(vectskcxx.get(i).toString());
					vecspkcxx.add(tsksrqid);
					vecspkcxx.add(tskssjdid);			
				}
			}
		}
		
//		for(int i=0;i<vecspkcxx.size();i=i+3){
//			System.out.println(vecspkcxx.get(i+0).toString()+"|"+vecspkcxx.get(i+1).toString()+"|"+vecspkcxx.get(i+2).toString());
//		}
		
		//获取普通考试安排规则
		String sqlksxx="select [考试日期],[考试时间段],[考试类型],[考试年级],[课程类型],[教室人数],[考场数量],[可用大教室] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+qzqm+"' and [考试年级]!='' and [课程类型]!='' and [教室人数]!=''";
		Vector vecksxx=db.GetContextVector(sqlksxx);
		String ksrqid="";
		String kssjdid="";
		int maxsjd=0;//用到最大时间段数量
		int[] sjdn=new int[ksrqts.length];
		for(int i=0;i<vecksxx.size();i=i+8){
			for(int j=0;j<ksrqts.length;j++){ 
				if(vecksxx.get(i).toString().equals(ksrqts[j].trim())){
					sjdn[j]++;
				}
			}
		}
		for(int s=0;s<sjdn.length;s++){
			if(maxsjd<sjdn[s]){
				maxsjd=sjdn[s];
			}
		}
		
		//把考试日期和考试时间段替换成对应的数字编号
		for(int i=0;i<vecksxx.size();i=i+8){
			for(int j=0;j<ksrqts.length;j++){ 
				if(vecksxx.get(i).toString().equals(ksrqts[j].trim())){
					ksrqid=j+"";
				}
			}
			for(int k=0;k<kssjdts.length;k++){
				if(vecksxx.get(i+1).toString().equals(kssjdts[k])){
					kssjdid=k+"";
				}
			}
			vecksxx.setElementAt(ksrqid, i);
			vecksxx.setElementAt(kssjdid, i+1);
		}
		
		//分开笔试和上机
		Vector vecbsxx=new Vector();//笔试
		Vector vecsjxx=new Vector();//上机
		for(int i=0;i<vecksxx.size();i=i+8){
			if(vecksxx.get(i+2).toString().equals("笔试")){
				for(int v=0;v<8;v++){
					vecbsxx.add(vecksxx.get(i+v).toString());
				}
			}else if(vecksxx.get(i+2).toString().equals("上机")){
				for(int v=0;v<8;v++){
					vecsjxx.add(vecksxx.get(i+v).toString());
				}
			}
		}
		
//		for(int i=0;i<vecksxx.size();i=i+8){
//			System.out.println(vecksxx.get(i+0).toString()+"|"+vecksxx.get(i+1).toString()+"|"+vecksxx.get(i+2).toString()+"|"+vecksxx.get(i+3).toString()+"|"+vecksxx.get(i+4).toString()+"|"+vecksxx.get(i+5).toString()+"|"+vecksxx.get(i+6).toString()+"|"+vecksxx.get(i+7).toString());
//		}
		
		//取相同考试场次编号大于1的,需要同时开考的
		String sqlcc="select a.考试场次编号 ,a.num from (select 考试场次编号,COUNT(*) as num from dbo.V_考试管理_考试设置 where [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [期中期末]='"+MyTools.fixSql(qzqm)+"' group by 考试场次编号) a where a.num!=1 ";
		Vector veccc=db.GetContextVector(sqlcc);

		//获取所有教室
		sqlcls="select a.教室编号,a.教室名称,case when b.教室类型代码='4' then '4' else '1' end as 教室类型代码,a.教室容量  from dbo.V_考试管理_考试教室 a,dbo.V_教室数据类 b where a.教室编号=b.教室编号  and a.考试名称='"+MyTools.fixSql(qzqm)+"' ";
		veccls=db.GetContextVector(sqlcls);
//		sqlcls2="select 教室编号,教室名称,教室类型代码,教室容量  from dbo.V_考试管理_考试教室  where 教室容量='2' and [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [考试周期]='"+MyTools.fixSql(qzqm)+"' ";
//		veccls2=db.GetContextVector(sqlcls2);
		
		//获取所有监考教师
		String sqltea="select [监考教师编号],[监考教师姓名],[监考类型],[监考日期]  from dbo.V_考试管理_监考教师  where [考试名称]='"+MyTools.fixSql(qzqm)+"' order by 监考类型,[监考日期] desc ";
		Vector vectea=db.GetContextVector(sqltea);
		if(vectea!=null&&vectea.size()>0){
			for(int i=0;i<vectea.size();i=i+4){
				for(int j=0;j<ksrqts.length;j++){ 
					if(vectea.get(i+3).toString().equals(ksrqts[j].trim())){
						ksrqid=j+"";
						vectea.setElementAt(ksrqid, i+3);
					}
				}	
			}
		}
		//获取只能排一天的监考教师
		String sqltea1="select count(*)  from dbo.V_考试管理_监考教师  where [监考类型]!='5' and [考试名称]='"+MyTools.fixSql(qzqm)+"' ";
		Vector vectea1=db.GetContextVector(sqltea1);
		int teachnum=Integer.parseInt(vectea1.get(0).toString());

		String sqlptks="SELECT distinct [考试日期] FROM [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+MyTools.fixSql(qzqm)+"' and [课程类型]='普通课程' ";
		Vector vecptks=db.GetContextVector(sqlptks);
		int ptksts=1;
		if(vecptks!=null&vecptks.size()>0){
			ptksts=vecptks.size();
		}
		
		int tearc=teachnum*3/ptksts;//平均每天多少监考教师人次	
		int day=tearc+(6-tearc%6);
		
//		int day2=teachnum2*3;//02
//		int day3=(teachnum-teachnum2*2)*2;//03
		int[] usednum=new int[ksrqts.length];//存各天已排教师数量
		for(int d=0;d<usednum.length;d++){
			usednum[d]=0;
		}

		//删除已有排考场信息
//		String sqldel="delete from dbo.V_考试管理_考场安排明细表 where [考场安排主表编号] ='"+MyTools.fixSql(qzqm)+"' ";
//		db.executeInsertOrUpdate(sqldel);
		
		//设置考场安排主表	
		this.setKCAPZBBH(qzqm);
		//获取考场安排明细编号
//		String sqlmxid="select max(cast(SUBSTRING([考场安排明细编号],8,11) as bigint)) from dbo.V_考试管理_考场安排明细表";
//		Vector vecmxid = db.GetContextVector(sqlmxid);
//		if (!vecmxid.toString().equals("[]") && vecmxid.size() > 0) {
//			maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vecmxid.get(0))))+1);
//			this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
//		}else{
//			maxMxId= String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr("10000000000")))+1);
//			this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
//		}
		
		//补考
		//if(vecksrq.get(2).toString().equals("2")){
		
			String sql3 = "";
			String sqlkssz = "";
			String savePath="";
			Vector vec3 = null;
			Vector vecins = new Vector();
			String xn=xnxqbm.substring(0,4);
			
			//查询授课计划是空的课程，把相关编号插入到dbo.V_考试管理_考试设置 
//			sqlkssz="select distinct 相关编号,授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式, " +
//					"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from ( " +
//					"select a.相关编号,case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when b.来源类型='3' then l.考试形式 else k.考试形式 end as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when e.年级代码=(cast(cast(right('2016',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
//					"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式 k on f.考试形式=k.编号 left join dbo.V_考试形式 l on g.考试形式=l.编号 " +
//					"where d.学生状态 in ('01','05','08') and 成绩状态='1' and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1')) as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')  and 授课计划明细编号 is null " +
//					"group by 相关编号,授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式,系名称";
//			Vector veckssz=db.GetContextVector(sqlkssz);
//			if(veckssz!=null&&veckssz.size()>0){
//				for(int i=0;i<veckssz.size();i=i+9){
//					sql2="insert into V_考试管理_考试设置 ([授课计划明细编号],[是否考试],[课程名称],[学年学期编码],[行政班名称]) " +
//						 "values ('"+veckssz.get(i).toString()+"','2','"+veckssz.get(i+3).toString()+"','"+veckssz.get(i+2).toString()+"','"+veckssz.get(i+8).toString()+"') ";
//					vecins.add(sql2);
//				}
//				db.executeInsertOrUpdateTransaction(vecins);
//			}
				
			
//			String[] timeordersj=new String[ksrqts.length*kssjdts.length];//保存时间段排序
//			for(int r=0;r<ksrqts.length;r++){
//				for(int s=0;s<kssjdts.length;s++){
//					for(int k=0;k<vecksxx.size();k=k+8){
//						//是笔试，日期+时间段相同
//						if(vecksxx.get(k+2).toString().equals("上机")&&vecksxx.get(k).toString().equals(ksrqts[r].trim())&&vecksxx.get(k+1).toString().equals(kssjdts[s].trim())){
//							int kyroom=0;
//							timeordersj[r*kssjdts.length+s]=r+"#"+s+"@";
//							kyroom=Integer.parseInt(vecksxx.get(k+6).toString());
//							if(kyroom==0){
//								
//							}else{
//								for(int e=0;e<kyroom;e++){
//									timeordersj[r*kssjdts.length+s]+=0+",";
//								}
//								timeordersj[r*kssjdts.length+s]=timeordersj[r*kssjdts.length+s].substring(0, timeordersj[r*kssjdts.length+s].length()-1);
//							}	
//						}
//					}	
//				}
//			}
					
			//从学期考试设置获取合并信息
//			String sqlchkkscc="select count(*) from V_考试管理_大补考合并信息 where 考试学期='"+xnxqbm+"'";
//			if(db.getResultFromDB(sqlchkkscc)){
//				
//			}else{
//				String sqlkscc="select [授课计划明细编号],[考试场次编号] from dbo.V_考试管理_考试设置 where [考试场次编号] in (" +
//						"select [考试场次编号] from ( " +
//						"SELECT [考试场次编号],COUNT(*) as samenum FROM [V_考试管理_考试设置] where [学年学期编码]='"+xnxqbm+"' group by [考试场次编号] ) a " +
//						"where samenum>1 ) " +
//						"group by [考试场次编号],[授课计划明细编号]";
//				Vector veckscc=db.GetContextVector(sqlkscc);
//				String skjhbhkscc="";
//				String sqlinskscc="";
//				Vector vecinskscc=new Vector();
//				if(veckscc!=null&&veckscc.size()>0){
//					skjhbhkscc+=veckscc.get(0).toString();
//					for(int i=2;i<veckscc.size();i=i+2){
//						if(veckscc.get(i-1).toString().equals(veckscc.get(i+1).toString())){
//							skjhbhkscc+="@"+veckscc.get(i).toString();
//						}else{
//							sqlinskscc="insert into V_考试管理_大补考合并信息 ([考试学期],[授课计划明细编号]) values ('"+xnxqbm+"','"+skjhbhkscc+"') ";
//							vecinskscc.add(sqlinskscc);
//							skjhbhkscc=veckscc.get(i).toString();
//						}
//						if(i==(veckscc.size()-2)){
//							sqlinskscc="insert into V_考试管理_大补考合并信息 ([考试学期],[授课计划明细编号]) values ('"+xnxqbm+"','"+skjhbhkscc+"') ";
//							vecinskscc.add(sqlinskscc);
//						}
//					}
//					db.executeInsertOrUpdateTransaction(vecinskscc);
//				}
//			}
			//------------------------------------------------------------------------------------
			int njm1=Integer.parseInt(xnxqbm.substring(2,4));//年级码
			int njm2=njm1-1;
			String nj1="";
			String nj2="";
			if(njm1<10){
				nj1="0"+njm1+"1";
			}else{
				nj1=njm1+"1";
			}
			if(njm2<10){
				nj2="0"+njm2+"1";
			}else{
				nj2=njm2+"1";
			}
					
			//查询所有要考试的学生
			String sqlstu="select distinct 学号,姓名,授课计划明细编号,课程名称,行政班名称 " +
					"from ( " +
					"select distinct case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when f.考试形式 in ('1','2','3','10') then '1' when f.考试形式 in ('4','5') then '4' when f.考试形式 in ('9','11','12','13','16') then '9' else f.考试形式 end as 考试形式编号,k.考试形式 as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when c.年级代码=(cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
					"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式大补考 k on f.考试形式=k.编号  " +
					"where d.学生状态 in ('01','05','08') and 成绩状态='1' and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1') " +
					"and a.相关编号 not in (select [选修课授课计划编号] from [V_选修课与智慧树对应表]) "+
					") as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') "+
					"group by 学号,姓名,授课计划明细编号,课程名称,行政班名称 order by 授课计划明细编号 " ;
			Vector vecstu=db.GetContextVector(sqlstu);
			
			//查询考试信息
			sql="select distinct 授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式编号,考试形式, " +
				"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from ( " +
				"select case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when f.考试形式 in ('1','2','3','10') then '1' when f.考试形式 in ('4','5') then '4' when f.考试形式 in ('9','11','12','13','16') then '9' else f.考试形式 end as 考试形式编号,k.考试形式 as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when c.年级代码=(cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式大补考 k on f.考试形式=k.编号  " +
				"where d.学生状态 in ('01','05','08') and 成绩状态='1' " +
				"and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1') " +
				") as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " ;

			sql+=" group by 授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式编号,考试形式,系名称 order by 课程名称,行政班简称 ";
			
			vec5=db.GetContextVector(sql);
			
			//去除整班未登分
			sql4="select 相关编号 from (select distinct a.相关编号,sum(case when 成绩<>'' then 1 else 0 end) as 已登分人数 " +
					"from V_成绩管理_登分教师信息表 a " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
					"left join (select ta.学号,ta.相关编号," +
					"case when (case when ta.平时 in ('-1','-5') then '' else ta.平时 end)<>'' then ta.平时 " +
					"when (case when ta.期中 in ('-1','-5') then '' else ta.期中 end)<>'' then ta.期中 " +
					"when (case when ta.实训 in ('-1','-5') then '' else ta.实训 end)<>'' then ta.实训 " +
					"when (case when ta.期末 in ('-1','-5') then '' else ta.期末 end)<>'' then ta.期末 " +
					"when (case when ta.总评 in ('-1','-5') then '' else ta.总评 end)<>'' then ta.总评 " +
					"when (case when ta.重修1 in ('-1','-5') then '' else ta.重修1 end)<>'' then ta.重修1 " +
					"when (case when ta.重修2 in ('-1','-5') then '' else ta.重修2 end)<>'' then ta.重修2 " +
					"when (case when ta.补考 in ('-1','-5') then '' else ta.补考 end)<>'' then ta.补考 " +
					"when (case when ta.大补考 in ('-1','-5') then '' else ta.大补考 end)<>'' then ta.大补考 else '' end as 成绩 " +
					"from V_成绩管理_学生成绩信息表 ta " +
					"left join V_成绩管理_科目课程信息表 tb on ta.科目编号=tb.科目编号 " +
					"where ta.状态='1' and ta.成绩状态='1' and cast(left(tb.学年学期编码,4) as int) between " + (Integer.parseInt(xn)-6) + " and " + xn + " ) c on c.相关编号=a.相关编号 " +
					"where a.状态='1' and b.状态='1' and cast(left(b.学年学期编码,4) as int) between " + (Integer.parseInt(xn)-6) + " and " + xn + " " +
					"group by a.相关编号,a.行政班代码,a.来源类型) as t " +
					"where t.已登分人数=0 ";
			vec4=db.GetContextVector(sql4);
			
			if(vec4!=null&&vec4.size()>0) {
				int sametag=0;
				for(int i=0;i<vec5.size();i=i+9){
					sametag=0;
					for(int j=0;j<vec4.size();j++) {
						if(vec5.get(i).toString().equals(vec4.get(j).toString())) {//授课计划编号相同，整班未登分删掉
							sametag=1;
						}
					}
					if(sametag==0) {
						vec.add(vec5.get(i).toString());//授课计划明细编号 1
						vec.add(vec5.get(i+1).toString());//学年学期 2
						vec.add(vec5.get(i+2).toString());//课程名称 3
						vec.add(vec5.get(i+3).toString());//人数 4
						vec.add(vec5.get(i+4).toString());//考试形式 5
						vec.add(vec5.get(i+5).toString());//考试形式 6
						vec.add(vec5.get(i+6).toString());//系名称 7
						vec.add(vec5.get(i+7).toString());//行政班简称 8
						vec.add(vec5.get(i+8).toString());//行政班名称 9	
					}else {
						
					}
				}
			}
			
			
			String skjhmxs="";
			sql3="SELECT count(*) FROM [dbo].[V_考试管理_大补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
			if(db.getResultFromDB(sql3)){
				sql3="SELECT [授课计划明细编号] FROM [dbo].[V_考试管理_大补考合并信息] where [考试学期]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
				vec3=db.GetContextVector(sql3);
				for(int j=0;j<vec3.size();j++){
					skjhmxs+=vec3.get(j).toString()+"@";
				}	
			}else{
				
			}
			
			Vector vecr=new Vector();
			Vector vecs=new Vector();
			Vector vect=new Vector();
			for(int i=0;vec.size()>1;){
				String skjhmx=vec.get(i).toString();
				String xnxqbm2=vec.get(i+1).toString();
				String kcmc=vec.get(i+2).toString();
				String rs=vec.get(i+3).toString();
				String ksxsbh=vec.get(i+4).toString();
				String ksxs=vec.get(i+5).toString();
				String xmc=vec.get(i+6).toString();
				String xzbjc=vec.get(i+7).toString();
				String xzbmc=vec.get(i+8).toString();
				//在合并信息中,保存到vecs
				if(!skjhmxs.equals("")&&skjhmxs.indexOf(vec.get(i).toString())>-1){
					vecs.add(skjhmx);//授课计划明细编号 1
					vecs.add(xnxqbm2);//学年学期 2
					vecs.add(kcmc);//课程名称 3
					vecs.add(rs);//人数 4
					vecs.add(ksxsbh);//考试形式编号 5
					vecs.add(ksxs);//考试形式 6
					vecs.add(xmc);//系名称 7
					vecs.add(xzbjc);//行政班简称 8
					vecs.add(xzbmc);//行政班名称 9
				}else{	
					for(int j=9;j<vec.size();j=j+9){
						//是选修课
						if(vec.get(i+2).toString().indexOf("选修")>-1){
							//课程名称 相同 合并
							if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+2).toString().equals(vec.get(j+2).toString())){
								skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1					
								xnxqbm2=vec.get(j+1).toString();//学年学期 2					
								//课程名称 3
								rs+="$"+vec.get(j+3).toString();//人数 4
								//考试形式 5
								xmc+="$"+vec.get(j+6).toString();//系名称 7
								xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
								xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
								
								for(int k=0;k<9;k++){
									vec.remove(j);
								}
								j=j-9;
							}
							
						}else{
							//课程名称,系名称,班级简称 相同 合并
							if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+2).toString().equals(vec.get(j+2).toString())&&vec.get(i+6).toString().equals(vec.get(j+6).toString())&&(vec.get(i+7).toString().equals(vec.get(j+7).toString())||vec.get(i+7).toString().substring(2,4).equals(vec.get(j+7).toString().substring(2,4)))){
								skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
								xnxqbm2=vec.get(j+1).toString();//学年学期 2
								//课程名称 3
								rs+="$"+vec.get(j+3).toString();//人数 4
								//考试形式 5
								//系名称 6
								xzbjc+="$"+vec.get(j+7).toString();//行政班简称 8
								xzbmc+="$"+vec.get(j+8).toString();//行政班名称 9
								
								for(int k=0;k<9;k++){
									vec.remove(j);
								}
								j=j-9;
							}	
						}
					}
					
					vecr.add(skjhmx);//授课计划明细编号 1
					vecr.add(xnxqbm2);//学年学期 2
					vecr.add(kcmc);//课程名称 3
					vecr.add(rs);//人数 4
					//vecr.add(ksxsbh);//考试形式 5
					vecr.add(ksxs);//考试形式 6
					vecr.add(xmc);//系名称 7
					vecr.add(xzbjc);//行政班简称 8
					vecr.add(xzbmc);//行政班名称 9			
				}
				for(int k=0;k<9;k++){
					vec.remove(i);
				}
			}
			
			if(vec3!=null&&vec3.size()>0){
				for(int i=0;i<vec3.size();i++){
					String sskjhmx="";
					String sxnxqbm="";
					String skcmc="";
					String srs="";
					String sksxsbh="";
					String sksxs="";
					String sxmc="";
					String sxzbjc="";
					String sxzbmc="";
					for(int j=0;j<vecs.size();j=j+9){
						if(vec3.get(i).toString().indexOf(vecs.get(j).toString())>-1){
							
							sxnxqbm+=vecs.get(j+1).toString()+"@";
							skcmc+=vecs.get(j+2).toString()+"@";
							srs+=vecs.get(j+3).toString()+"@";
							sksxsbh=vecs.get(j+4).toString();
							sksxs=vecs.get(j+5).toString();
							sxmc+=vecs.get(j+6).toString()+"@";
							sxzbjc+=vecs.get(j+7).toString()+"@";
							sxzbmc+=vecs.get(j+8).toString()+"@";	
						}	
					}
					
					sskjhmx=vec3.get(i).toString();
					int max=0;
					if(!sxnxqbm.equals("")){
						sxnxqbm=sxnxqbm.substring(0,sxnxqbm.length()-1);
						
						String[] xq=sxnxqbm.split("@");
						max=Integer.parseInt(xq[0]);
						for(int k=0;k<xq.length;k++){
							if(max<Integer.parseInt(xq[k])){
								max=Integer.parseInt(xq[k]);
							}
						}
					}
					if(!skcmc.equals("")){
						skcmc=skcmc.substring(0,skcmc.length()-1);
					}
					if(!srs.equals("")){
						srs=srs.substring(0,srs.length()-1);
					}
					if(!sxmc.equals("")){
						sxmc=sxmc.substring(0,sxmc.length()-1);
					}
					if(!sxzbjc.equals("")){
						sxzbjc=sxzbjc.substring(0,sxzbjc.length()-1);
					}
					if(!sxzbmc.equals("")){
						sxzbmc=sxzbmc.substring(0,sxzbmc.length()-1);
					}
					
					if(!sxnxqbm.equals("")){
						vect.add(sskjhmx);//授课计划明细编号 1
						vect.add(max+"");//学年学期 2
						vect.add(skcmc);//课程名称 3
						vect.add(srs);//人数 4
						//vect.add(sksxsbh);//考试形式 5
						vect.add(sksxs);//考试形式 6
						vect.add(sxmc);//系名称 7
						vect.add(sxzbjc);//行政班简称 8
						vect.add(sxzbmc);//行政班名称 9	
					}
				}
			}
			
			for(int i=0;i<vecr.size();i++){
				vect.add(vecr.get(i).toString());
			}
			
//			for(int i=0;i<vect.size();i=i+8){
//				System.out.println(i/8+":--"+vect.get(i+2)+" | "+vect.get(i+3)+" | "+vect.get(i+4));
//			}

			//开始排考试
			Vector veca=new Vector();
			Vector vecb=new Vector();
			Vector vecc=new Vector();
			Vector vecty=new Vector();
			Vector vece=new Vector();
			Vector vecf=new Vector();
			Vector vecg=new Vector();
			Vector vech=new Vector();
			Vector veci=new Vector();
			Vector vecj=new Vector();
			//简化输出名称
			for(int i=0;i<vect.size();i=i+8){
				String region1=vect.get(i+2).toString();
				region1=region1.replaceAll("@", "\\$");
				String showkcmc="";
				if(region1.indexOf("$")>-1){
					String[] kcmcinfo=region1.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showkcmc.equals("")){
							showkcmc=kcmcinfo[k]+"、";
						}
						if(showkcmc.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showkcmc+=kcmcinfo[k]+"、";
						}
					}
					showkcmc=showkcmc.substring(0,showkcmc.length()-1);
				}else{
					showkcmc=vect.get(i+2).toString();
				}
				veca.add(showkcmc);//课程名称 3
				
				String region=vect.get(i+3).toString();
				region=region.replaceAll("@", "\\$");
				int showrs=0;
				if(region.indexOf("$")>-1){
					String[] kcmcinfo=region.split("\\$");
					if(region.indexOf("$")>-1){
						for(int k=0;k<kcmcinfo.length;k++){
							showrs+=Integer.parseInt(kcmcinfo[k]);
						}
					}else{
						showrs=Integer.parseInt(region);
					}
				}else{
					showrs=Integer.parseInt(vect.get(i+3).toString());
				}
				veca.add(showrs+"");//人数 4
				
				String region2=vect.get(i+4).toString();
				region2=region2.replaceAll("@", "\\$");
				String showksxs="";
				if(region2.indexOf("$")>-1){
					String[] kcmcinfo=region2.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showksxs.equals("")){
							showksxs=kcmcinfo[k]+"、";
						}
						if(showksxs.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showksxs+=kcmcinfo[k]+"、";
						}
					}
					showksxs=showksxs.substring(0,showksxs.length()-1);
				}else{
					showksxs=vect.get(i+4).toString();
				}
				veca.add(showksxs);//考试形式 5
				
				String region3=vect.get(i+5).toString();
				region3=region3.replaceAll("@", "\\$");
				String showxmc="";
				if(region3.indexOf("$")>-1){
					String[] kcmcinfo=region3.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showxmc.equals("")){
							showxmc=kcmcinfo[k]+"、";
						}
						if(showxmc.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showxmc+=kcmcinfo[k]+"、";
						}
					}
					showxmc=showxmc.substring(0,showxmc.length()-1);
				}else{
					showxmc=vect.get(i+5).toString();
				}
				veca.add(showxmc);//系名称 6
				
				String region4=vect.get(i+6).toString();
				region4=region4.replaceAll("@", "\\$");
				String showbjjc="";
				if(region4.indexOf("$")>-1){
					String[] kcmcinfo=region4.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showbjjc.equals("")){
							showbjjc=kcmcinfo[k]+"、";
						}
						if(showbjjc.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showbjjc+=kcmcinfo[k]+"、";
						}
					}
					showbjjc=showbjjc.substring(0,showbjjc.length()-1);
				}else{
					showbjjc=vect.get(i+6).toString();
				}
				veca.add(showbjjc);//行政班简称 7
				veca.add(vect.get(i).toString());//授课计划明细编号 1
			}
			
//			for(int i=0;i<veca.size();i=i+6){
//				System.out.println(i/6+":--"+veca.get(i)+" | "+veca.get(i+1)+" | "+veca.get(i+2)+" | "+veca.get(i+3)+" | "+veca.get(i+4)+" | "+veca.get(i+5));
//			}
			
			//按考试类型不同分别保存
			for(int i=0;i<veca.size();i=i+6){
				if(veca.get(i).toString().indexOf("体育")>-1){
					vecty.add(veca.get(i).toString());//课程名称 1
					vecty.add(veca.get(i+1).toString());//人数 2
					vecty.add(veca.get(i+2).toString());//考试形式 3
					vecty.add(veca.get(i+4).toString());//行政班简称 4
					vecty.add("ty");//地点 5
					vecty.add("ty");//时间 6
					vecty.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}
			}

			for(int i=0;i<veca.size();i=i+6){ 
				if(veca.get(i+2).toString().equals("上机")){
					vecb.add(veca.get(i).toString());//课程名称 1
					vecb.add(veca.get(i+1).toString());//人数 2
					vecb.add(veca.get(i+2).toString());//考试形式 3
					vecb.add(veca.get(i+3).toString());//系名称 4
					vecb.add(veca.get(i+4).toString());//行政班简称 5
					vecb.add(veca.get(i+5).toString());//授课计划编号6
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("笔试")){
					vecc.add(veca.get(i).toString());//课程名称 1
					vecc.add(veca.get(i+1).toString());//人数 2
					vecc.add(veca.get(i+2).toString());//考试形式 3
					vecc.add(veca.get(i+3).toString());//系名称 4
					vecc.add(veca.get(i+4).toString());//行政班简称 5
					vecc.add(veca.get(i+5).toString());//授课计划编号6
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("口试")){
					vece.add(veca.get(i).toString());//课程名称 1
					vece.add(veca.get(i+1).toString());//人数 2
					vece.add(veca.get(i+2).toString());//考试形式 3
					vece.add(veca.get(i+4).toString());//行政班简称 4
					vece.add("ks");//地点 5
					vece.add("ks");//时间 6
					vece.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("听力")){
					vecf.add(veca.get(i).toString());//课程名称 1
					vecf.add(veca.get(i+1).toString());//人数 2
					vecf.add(veca.get(i+2).toString());//考试形式 3
					vecf.add(veca.get(i+4).toString());//行政班简称 4
					vecf.add("tl");//地点 5
					vecf.add("tl");//时间 6
					vecf.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("实训")){
					vecg.add(veca.get(i).toString());//课程名称 1
					vecg.add(veca.get(i+1).toString());//人数 2
					vecg.add(veca.get(i+2).toString());//考试形式 3
					vecg.add(veca.get(i+4).toString());//行政班简称 4
					vecg.add("sx");//地点 5
					vecg.add("sx");//时间 6
					vecg.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("随堂")){
					vech.add(veca.get(i).toString());//课程名称 1
					vech.add(veca.get(i+1).toString());//人数 2
					vech.add(veca.get(i+2).toString());//考试形式 3
					vech.add(veca.get(i+4).toString());//行政班简称 4
					vech.add("st");//地点 5
					vech.add("st");//时间 6
					vech.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("大作业")){
					veci.add(veca.get(i).toString());//课程名称 1
					veci.add(veca.get(i+1).toString());//人数 2
					veci.add(veca.get(i+2).toString());//考试形式 3
					veci.add(veca.get(i+4).toString());//行政班简称 4
					veci.add("dzy");//地点 5
					veci.add("dzy");//时间 6
					veci.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("")||veca.get(i+2).toString().equals("其它")){
					vecj.add(veca.get(i).toString());//课程名称 1
					vecj.add(veca.get(i+1).toString());//人数 2
					vecj.add(veca.get(i+2).toString());//考试形式 3
					vecj.add(veca.get(i+4).toString());//行政班简称 4
					vecj.add("kong");//地点 5
					vecj.add("kong");//时间 6
					vecj.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}
			}

//			for(int i=0;i<veca.size();i=i+6){
//				System.out.println("a:--"+veca.get(i)+" | "+veca.get(i+1)+" | "+veca.get(i+2)+" | "+veca.get(i+3)+" | "+veca.get(i+4)+" | "+veca.get(i+5));
//			}
			
//			for(int i=0;i<vecb.size();i=i+6){
//				System.out.println(vecb.get(i)+" | "+vecb.get(i+1)+" | "+vecb.get(i+2)+" | "+vecb.get(i+3)+" | "+vecb.get(i+4));
//			}

			//相同课程合并
			for(int i=7;i<vecty.size();i=i+7){
				while(vecty.get(i-7).toString().equals(vecty.get(i).toString())){
					int hbnum=Integer.parseInt(vecty.get(i+1-7).toString())+Integer.parseInt(vecty.get(i+1).toString());
					vecty.setElementAt(hbnum, i+1-7);
					vecty.setElementAt(vecty.get(i+3-7).toString()+"、"+vecty.get(i+3).toString(), i+3-7);
					for(int k=0;k<7;k++){
						vecty.remove(i);
					}
					if(i==vecty.size()){
						break;
					}
				}
			}
			
//			for(int i=7;i<vece.size();i=i+7){
//				while(vece.get(i-7).toString().equals(vece.get(i).toString())){
//					int hbnum=Integer.parseInt(vece.get(i+1-7).toString())+Integer.parseInt(vece.get(i+1).toString());
//					vece.setElementAt(hbnum, i+1-7);
//					vece.setElementAt(vece.get(i+3-7).toString()+"、"+vece.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vece.remove(i);
//					}
//					if(i==vece.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<vecf.size();i=i+7){
//				while(vecf.get(i-7).toString().equals(vecf.get(i).toString())){
//					int hbnum=Integer.parseInt(vecf.get(i+1-7).toString())+Integer.parseInt(vecf.get(i+1).toString());
//					vecf.setElementAt(hbnum, i+1-7);
//					vecf.setElementAt(vecf.get(i+3-7).toString()+"、"+vecf.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vecf.remove(i);
//					}
//					if(i==vecf.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<vecg.size();i=i+7){
//				while(vecg.get(i-7).toString().equals(vecg.get(i).toString())){
//					int hbnum=Integer.parseInt(vecg.get(i+1-7).toString())+Integer.parseInt(vecg.get(i+1).toString());
//					vecg.setElementAt(hbnum, i+1-7);
//					vecg.setElementAt(vecg.get(i+3-7).toString()+"、"+vecg.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vecg.remove(i);
//					}
//					if(i==vecg.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<vech.size();i=i+7){
//				while(vech.get(i-7).toString().equals(vech.get(i).toString())){
//					int hbnum=Integer.parseInt(vech.get(i+1-7).toString())+Integer.parseInt(vech.get(i+1).toString());
//					vech.setElementAt(hbnum, i+1-7);
//					vech.setElementAt(vech.get(i+3-7).toString()+"、"+vech.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vech.remove(i);
//					}
//					if(i==vech.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<veci.size();i=i+7){
//				while(veci.get(i-7).toString().equals(veci.get(i).toString())){
//					int hbnum=Integer.parseInt(veci.get(i+1-7).toString())+Integer.parseInt(veci.get(i+1).toString());
//					veci.setElementAt(hbnum, i+1-7);
//					veci.setElementAt(veci.get(i+3-7).toString()+"、"+veci.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						veci.remove(i);
//					}
//					if(i==veci.size()){
//						break;
//					}
//				}
//			}

			//查询特殊课程规则
						
//			for(int i=0;i<vecz5.size();i=i+6){
//				System.out.println(vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+4));
//			}
			
//			for(int i=0;i<vecc.size();i=i+6){
//				System.out.println("cc:--"+vecc.get(i)+" | "+vecc.get(i+1)+" | "+vecc.get(i+2)+" | "+vecc.get(i+3)+" | "+vecc.get(i+4)+" | "+vecc.get(i+5));
//			}
			
		
		//排考试完毕----------------------------------------------------------------------	
		String sqlsj="select [课程名称],[学生人数],[试卷类型] as 考试形式,[行政班名称],[场地要求],[时间序列] from dbo.V_考试管理_考场安排明细表  " +
				"where [考场安排主表编号]='"+qzqm+"' and 考试形式='4' " +
				"order by cast(substring(时间序列,0,CHARINDEX('#',时间序列)) as int),cast(substring(时间序列,CHARINDEX('#',时间序列)+1,LEN(时间序列)) as int),[场地要求],[试卷类型] ";	
		Vector vecbksj=db.GetContextVector(sqlsj);
			
		String sqlbs="select [课程名称],[学生人数],[试卷类型] as 考试形式,[行政班名称],[场地要求],[时间序列] from dbo.V_考试管理_考场安排明细表  " +
				"where [考场安排主表编号]='"+qzqm+"' and 考试形式='3' " +
				"order by cast(substring(时间序列,0,CHARINDEX('#',时间序列)) as int),cast(substring(时间序列,CHARINDEX('#',时间序列)+1,LEN(时间序列)) as int),[场地要求],[试卷类型] ";	
		Vector vecbspx=db.GetContextVector(sqlbs);
			
//		for(int i=0;i<vecbspx.size();i=i+6){
//			System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		}
		
		//--------------------------------------------------------------------
			
		//生成excel表
		if (vect != null && vect.size() > 0) {
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);
			savePath = MyTools.getProp(request, "Base.exportExcelPath");	
			
			//创建
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			savePath +=  "/" + xn + "大补考考试安排表.xls";
			
			try {
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				WritableSheet wsheet1 = wbook.createSheet("考试安排表", 0);// 工作表名称
				WritableSheet wsheet2 = wbook.createSheet("监考签到表", 1);// 工作表名称
				WritableSheet wsheet3 = wbook.createSheet("试卷标签", 2);// 工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;

				//考试安排表 1
				//生成标题
				String[] title1=new String[]{"序号","课程名称","人数","考核形式","专业","地点","考试时间"};
				int counum1=0;//excel表中行数
				String cellContent1 = ""; //当前单元格的内容
				
				//设置列宽
				wsheet1.setColumnView(0, 5);
				wsheet1.setColumnView(1, 37);
				wsheet1.setColumnView(2, 5);
				wsheet1.setColumnView(3, 8);
				wsheet1.setColumnView(4, 37);
				wsheet1.setColumnView(5, 8);
				wsheet1.setColumnView(6, 32);
				
				
				//第1行
				//设置课表标题行列字体大小
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet1.mergeCells(0, counum1, 6, counum1);
				cellContent1 = xn+"学年度第"+this.getGG_XNXQBM().substring(4,5)+"学期补考考试安排表";
				content = new Label(0, counum1, cellContent1, contentStyle);
				wsheet1.addCell(content);
				wsheet1.setRowView(counum1, 760);	
				
				//第2行
				counum1++;
				wsheet1.mergeCells(0, counum1, 6, counum1);
				cellContent1="注：1、补考的同学必须带好一卡通（或学生证、身份证二证）参加考试("+(Integer.parseInt(xn.substring(2,4))-4)+"、"+(Integer.parseInt(xn.substring(2,4))-3)+"级带身份证），不带证件者将取消补考资格。";
				content = new Label(0, counum1, cellContent1, contentStyle);
				wsheet1.addCell(content);
				wsheet1.setRowView(counum1, 760);
			
				//第3行
				counum1++;
				wsheet1.mergeCells(0, counum1, 6, counum1);
				cellContent1="2、大作业、口试、听力、实训、随堂考试直接由辅导教师组织考试，请辅导教师于12月25日前将补考成绩单及大作业、口试表等交教务处。";
				content = new Label(0, counum1, cellContent1, contentStyle);
				wsheet1.addCell(content);
				wsheet1.setRowView(counum1, 760);
				
				//第4行
				counum1++;
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				wsheet1.mergeCells(0, counum1, 6, counum1);
				cellContent1="   3、大补考辅导安排表和考试安排表下载网址：jwc.shxj.edu.cn;联系电话：66115178";
				content = new Label(0, counum1, cellContent1, contentStyle);
				wsheet1.addCell(content);
				wsheet1.setRowView(counum1, 760);

				//第5行
				counum1++;
				for(int colNum=0; colNum<7; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setShrinkToFit(true);
					//contentStyle.setWrap(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==6){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}								
					cellContent1=title1[colNum];						
					content = new Label(colNum, counum1, cellContent1, contentStyle);
					wsheet1.addCell(content);	
				}
				wsheet1.setRowView(counum1, 500);
				
				
				//第6行
				counum1++;	
				
//				for(int i=0;i<vecz5.size();i=i+6){
//					System.out.println("sj:---"+vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+5));
//				}
				
				int satline=0;
				int endline=0;			
			
				//vecz5 周五上机
				satline=counum1;
				endline=counum1+vecbksj.size()/6-1;
				//wsheet1.mergeCells(3, satline, 3, endline);
				//wsheet1.mergeCells(5, satline, 5, endline);
				wsheet1.mergeCells(6, satline, 6, endline);
				
//				int roomz5=1;
//				for(int i=6;i<vecz5.size();i=i+6){ 
//					if(vecz5.get(i+4-6).toString().equals(vecz5.get(i+4).toString())){
//						
//					}else{
//						roomz5++;
//					}
//				}
//				//vecz5按教室排序
//				Vector vecbksj=new Vector();
//				for(int i=0;i<roomz5;i++){
//					for(int j=0;j<vecz5.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							vecbksj.add(vecz5.get(j).toString());//课程名称 1
//							vecbksj.add(vecz5.get(j+1).toString());//人数 2
//							vecbksj.add(vecz5.get(j+2).toString());//考试形式 3
//							vecbksj.add(vecz5.get(j+3).toString());//行政班简称 4
//							vecbksj.add(vecz5.get(j+4).toString());//教室 5
//							vecbksj.add(vecz5.get(j+5).toString());//时间 6
//						}
//					}
//				}
				
				
//				for(int i=0;i<vecbksj.size();i=i+6){
//					System.out.println("sj:---"+vecbksj.get(i)+" | "+vecbksj.get(i+1)+" | "+vecbksj.get(i+2)+" | "+vecbksj.get(i+3)+" | "+vecbksj.get(i+4)+" | "+vecbksj.get(i+5));
//				}
				
				//计算考试日期
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				String xxksrq="";
				String xxkssjd="";
				for(int m=0;m<ksrqts.length;m++){
					if(m==Integer.parseInt(vecbksj.get(5).toString().split("#")[0])){
						xxksrq=ksrqts[m].trim();
					}
				}
				for(int n=0;n<kssjdts.length;n++){
					if(n==Integer.parseInt(vecbksj.get(5).toString().split("#")[1])){
						xxkssjd=kssjdts[n].trim();
					}
				}
				
				try {
					cal.setTime(format.parse(xxksrq));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				int week=0;
				//考试日期是星期几
				week = cal.get(Calendar.DAY_OF_WEEK)-1;

				String weekday="";
				if(week==1){
					weekday="一";
				}else if(week==2){
					weekday="二";
				}else if(week==3){
					weekday="三";
				}else if(week==4){
					weekday="四";
				}else if(week==5){
					weekday="五";
				}else if(week==6){
					weekday="六";
				}else if(week==0){
					weekday="日";
				}
				
//				int linesat=0;
//				int[] linenum=new int[roomz5];//教室合并数量
//				for(int i=0;i<linenum.length;i++){
//					for(int j=0;j<vecbksj.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							linenum[i]++;
//						}
//					}
//				}
				
				//计算合并单元格数量
				int sjtime=0;
				int sjclas=0;
				int sjksxs=0;
				int timesj=0;
				int classj=0;
				int ksxssj=0;
				int sjbs=1;
				int sjjs=1;
				int sjks=1;
				Vector vecsjtime=new Vector();
				Vector vecsjclas=new Vector();
				Vector vecsjksxs=new Vector();
				for(int i=6;i<vecbksj.size();i=i+6){ 
					//考试日期
					if(vecbksj.get(i+5-6).toString().equals(vecbksj.get(i+5).toString())){
						sjbs++;
						//教室
						if(vecbksj.get(i+4-6).toString().equals(vecbksj.get(i+4).toString())){
							sjjs++;
							//考试形式
							if(vecbksj.get(i+2-6).toString().equals(vecbksj.get(i+2).toString())){
								sjks++;
							}else{
								vecsjksxs.add(sjks);
								sjks=1;
							}
						}else{
							vecsjclas.add(sjjs);
							sjjs=1;
							vecsjksxs.add(sjks);
							sjks=1;
						}
					}else{
						vecsjtime.add(sjbs);
						sjbs=1;
						vecsjclas.add(sjjs);
						sjjs=1;
						vecsjksxs.add(sjks);
						sjks=1;
					}
				}
				vecsjtime.add(sjbs);
				sjbs=1;
				vecsjclas.add(sjjs);
				sjjs=1;
				vecsjksxs.add(sjks);
				sjks=1;
				
				int hbnum=0;
				for(int i=0;i<vecbksj.size();i=i+6){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent1=vecbksj.get(i+colNum-1).toString();				
							if(i/6==sjksxs){
								wsheet1.mergeCells(3, sjksxs+satline, 3, sjksxs+satline+Integer.parseInt(vecsjksxs.get(ksxssj).toString())-1);
								sjksxs=sjksxs+Integer.parseInt(vecsjksxs.get(ksxssj).toString());
								ksxssj++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent1="";//地点
							if(i/6==sjclas){
								wsheet1.mergeCells(5, sjclas+satline, 5, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								sjclas=sjclas+Integer.parseInt(vecsjclas.get(classj).toString());
								classj++;
							}
						}else if(colNum==6){ 
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent1=name1+xxkssjd+"(若有两门或两门以上的补考生，考试时间顺延，每门考试时间为2小时。)";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecbksj.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+=","+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecbksj.get(i+colNum-1).toString();
							}	
						}
						
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);	
						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
			
				//vecbspx 笔试
				
//				for(int i=0;i<vecbspx.size();i=i+6){
//					System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		   		}
				
				satline=counum1;
				endline=counum1+vecbspx.size()/6-1;
				//计算合并单元格数量
				int linetime=0;
				int lineclas=0;
				int lineksxs=0;
				int timenum=0;
				int clasnum=0;
				int ksxsnum=0;
				int numbs=1;
				int numjs=1;
				int numks=1;
				Vector vectime=new Vector();
				Vector vecclas=new Vector();
				Vector vecksxs=new Vector();
				
				for(int i=6;i<vecbspx.size();i=i+6){ 
					//考试日期
					if(vecbspx.get(i+5-6).toString().equals(vecbspx.get(i+5).toString())){
						numbs++;
						//教室
						if(vecbspx.get(i+4-6).toString().equals(vecbspx.get(i+4).toString())){
							numjs++;
							//考试形式
							if(vecbspx.get(i+2-6).toString().equals(vecbspx.get(i+2).toString())){
								numks++;
							}else{
								vecksxs.add(numks);
								numks=1;
							}
						}else{
							vecclas.add(numjs);
							numjs=1;
							vecksxs.add(numks);
							numks=1;
						}
					}else{
						vectime.add(numbs);
						numbs=1;
						vecclas.add(numjs);
						numjs=1;
						vecksxs.add(numks);
						numks=1;
					}
				}
				vectime.add(numbs);
				numbs=1;
				vecclas.add(numjs);
				numjs=1;
				vecksxs.add(numks);
				numks=1;
	

				//生成excel
				for(int i=0;i<vecbspx.size();i=i+6){ 
					//计算考试日期
					//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					//Calendar cal = Calendar.getInstance();
					xxksrq="";
					xxkssjd="";
					for(int m=0;m<ksrqts.length;m++){
						if(m==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[0])){
							xxksrq=ksrqts[m].trim();
						}
					}
					for(int n=0;n<kssjdts.length;n++){
						if(n==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[1])){
							xxkssjd=kssjdts[n].trim();
						}
					}
					
					try {
						cal.setTime(format.parse(xxksrq));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					week=0;
					//考试日期是星期几
					week = cal.get(Calendar.DAY_OF_WEEK)-1;
					
					weekday="";
					if(week==1){
						weekday="一";
					}else if(week==2){
						weekday="二";
					}else if(week==3){
						weekday="三";
					}else if(week==4){
						weekday="四";
					}else if(week==5){
						weekday="五";
					}else if(week==6){
						weekday="六";
					}else if(week==0){
						weekday="日";
					}
					
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent1=vecbspx.get(i+colNum-1).toString();				
							if(i/6==lineksxs){
								wsheet1.mergeCells(3, lineksxs+satline, 3, lineksxs+satline+Integer.parseInt(vecksxs.get(ksxsnum).toString())-1);
								lineksxs=lineksxs+Integer.parseInt(vecksxs.get(ksxsnum).toString());
								ksxsnum++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent1="";//地点
							if(i/6==lineclas){
								wsheet1.mergeCells(5, lineclas+satline, 5, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								lineclas=lineclas+Integer.parseInt(vecclas.get(clasnum).toString());
								clasnum++;
							}
						}else if(colNum==6){//考试日期
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent1=name1+xxkssjd;
							if(i/6==linetime){
								wsheet1.mergeCells(6, linetime+satline, 6, linetime+satline+Integer.parseInt(vectime.get(timenum).toString())-1);
								linetime=linetime+Integer.parseInt(vectime.get(timenum).toString());
								timenum++;
							}
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecbspx.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecbspx.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}		
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbspx.size()-6||(timenum==vectime.size())){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}	
						}else{
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}					
							
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
								
				//vecty 体育
				if(vecty.size()>0){
					satline=counum1;
					endline=counum1+vecty.size()/7-1;
					wsheet1.mergeCells(3, satline, 6, endline);
				}
				for(int i=0;i<vecty.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){ 
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==3){
							//String name1=xxksrq.split("-")[1]+"月"+xxksrq.split("-")[2]+"日(周"+weekday+") ";							
							cellContent1=tiyutime+" 体育馆";
						}else{
							cellContent1=vecty.get(i+colNum-1).toString();
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecty.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==3){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecty.size()-7||colNum==3){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecty.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}	
	
				//vece 口试
				if(vece.size()>0){
					satline=counum1;
					endline=counum1+vece.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vece.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vece.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vece.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vece.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vece.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vece.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
			
				//vecf 听力
				if(vecf.size()>0){
					satline=counum1;
					endline=counum1+vecf.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vecf.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecf.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecf.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecf.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecf.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecf.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
				//vecg 实训
				if(vecg.size()>0){
					satline=counum1;
					endline=counum1+vecg.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vecg.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecg.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecg.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecg.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecg.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecg.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
								
				//vech 随堂
				if(vech.size()>0){
					satline=counum1;
					endline=counum1+vech.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vech.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vech.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vech.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vech.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vech.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vech.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
				//veci 大作业
				if(veci.size()>0){
					satline=counum1;
					endline=counum1+veci.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<veci.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=veci.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=veci.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==veci.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==veci.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==veci.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
				//vecj 空
				if(vecj.size()>0){
					satline=counum1;
					endline=counum1+vecj.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vecj.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-4)+"";//序号
						}else if(colNum==3){
							cellContent1="其它";//地点
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecj.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecj.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecj.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecj.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecj.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
		
				
				//监考签到表 2------------------------------------------------------------------------
				//生成标题
				String[] title2=new String[]{"序号","课程名称","人数","考核形式","专业","地点","考试时间","监考","监考"};
				int counum2=0;//excel表中行数
				String cellContent2 = ""; //当前单元格的内容
				
				//设置列宽
				wsheet2.setColumnView(0, 5);
				wsheet2.setColumnView(1, 37);
				wsheet2.setColumnView(2, 5);
				wsheet2.setColumnView(3, 8);
				wsheet2.setColumnView(4, 37);
				wsheet2.setColumnView(5, 8);
				wsheet2.setColumnView(6, 32);
				wsheet2.setColumnView(7, 10);
				wsheet2.setColumnView(8, 10);
				
				//第1行
				//设置课表标题行列字体大小
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet2.mergeCells(0, counum2, 8, counum2);
				cellContent2 = (Integer.parseInt(xn.substring(2,4))-2)+"级1-4学期（含"+(Integer.parseInt(xn.substring(2,4))-4)+"、"+(Integer.parseInt(xn.substring(2,4))-3)+"级）大补考监考签到表";
				content = new Label(0, counum2, cellContent2, contentStyle);
				wsheet2.addCell(content);
				wsheet2.setRowView(counum2, 760);	
							
				//第2行
				counum2++;
				for(int colNum=0; colNum<9; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setShrinkToFit(true);
					//contentStyle.setWrap(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==8){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}								
					cellContent2=title2[colNum];						
					content = new Label(colNum, counum2, cellContent2, contentStyle);
					wsheet2.addCell(content);	
				}
				wsheet2.setRowView(counum2, 500);

				
				//第3行
				counum2++;	
				
//				for(int i=0;i<vecz5.size();i=i+6){
//					System.out.println("sj:---"+vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+5));
//				}
				
				satline=0;
				endline=0;			
			
				//vecz5 周五上机
				satline=counum2;
				endline=counum2+vecbksj.size()/6-1;
				//wsheet2.mergeCells(3, satline, 3, endline);
				//wsheet2.mergeCells(5, satline, 5, endline);
				wsheet2.mergeCells(6, satline, 6, endline);
				
				//vecz5排序
//				Vector vecbksj=new Vector();
//				for(int i=0;i<roomz5;i++){
//					for(int j=0;j<vecz5.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							vecbksj.add(vecz5.get(j).toString());//课程名称 1
//							vecbksj.add(vecz5.get(j+1).toString());//人数 2
//							vecbksj.add(vecz5.get(j+2).toString());//考试形式 3
//							vecbksj.add(vecz5.get(j+3).toString());//行政班简称 4
//							vecbksj.add(vecz5.get(j+4).toString());//教室 5
//							vecbksj.add(vecz5.get(j+5).toString());//时间 6
//						}
//					}
//				}
								
//				for(int i=0;i<vecbksj.size();i=i+6){
//					System.out.println("sj:---"+vecbksj.get(i)+" | "+vecbksj.get(i+1)+" | "+vecbksj.get(i+2)+" | "+vecbksj.get(i+3)+" | "+vecbksj.get(i+4)+" | "+vecbksj.get(i+5));
//				}
				
				//计算考试日期
				//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				//Calendar cal = Calendar.getInstance();
				xxksrq="";
				xxkssjd="";
				for(int m=0;m<ksrqts.length;m++){
					if(m==Integer.parseInt(vecbksj.get(5).toString().split("#")[0])){
						xxksrq=ksrqts[m].trim();
					}
				}
				for(int n=0;n<kssjdts.length;n++){
					if(n==Integer.parseInt(vecbksj.get(5).toString().split("#")[1])){
						xxkssjd=kssjdts[n].trim();
					}
				}
				
				try {
					cal.setTime(format.parse(xxksrq));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				week=0;
				//考试日期是星期几
				week = cal.get(Calendar.DAY_OF_WEEK)-1;

				weekday="";
				if(week==1){
					weekday="一";
				}else if(week==2){
					weekday="二";
				}else if(week==3){
					weekday="三";
				}else if(week==4){
					weekday="四";
				}else if(week==5){
					weekday="五";
				}else if(week==6){
					weekday="六";
				}else if(week==0){
					weekday="日";
				}
				
				sjtime=0;
				sjclas=0;
				sjksxs=0;
				timesj=0;
				classj=0;
				ksxssj=0;
				sjbs=1;
				sjjs=1;
				sjks=1;
				
				hbnum=0;
				for(int i=0;i<vecbksj.size();i=i+6){ 
					for(int colNum=0; colNum<9; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent2=(counum2-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent2=vecbksj.get(i+colNum-1).toString();				
							if(i/6==sjksxs){
								wsheet2.mergeCells(3, sjksxs+satline, 3, sjksxs+satline+Integer.parseInt(vecsjksxs.get(ksxssj).toString())-1);
								sjksxs=sjksxs+Integer.parseInt(vecsjksxs.get(ksxssj).toString());
								ksxssj++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent2="";//地点
							if(i/6==sjclas){
								wsheet2.mergeCells(5, sjclas+satline, 5, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								wsheet2.mergeCells(7, sjclas+satline, 7, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								wsheet2.mergeCells(8, sjclas+satline, 8, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								sjclas=sjclas+Integer.parseInt(vecsjclas.get(classj).toString());
								classj++;
							}
						}else if(colNum==6){ 
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent2=name1+xxkssjd+"(若有两门或两门以上的补考生，考试时间顺延，每门考试时间为2小时。)";
						}else if(colNum==7||colNum==8){//监考教师
							cellContent2="";
						}else{
							if(colNum==4){
								cellContent2="";
								String[] bjjc=vecbksj.get(i+colNum-1).toString().split("、");
								cellContent2=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent2+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent2+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent2+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent2=vecbksj.get(i+colNum-1).toString();
							}
						}
						
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==8){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						
						content = new Label(colNum, counum2, cellContent2, contentStyle);
						wsheet2.addCell(content);	
						
					}
					wsheet2.setRowView(counum2, 500);
					counum2++;	
				}
				
				//vecbspx 笔试
				
//				for(int i=0;i<vecbspx.size();i=i+6){
//					System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		   		}
				
				satline=counum2;
				endline=counum2+vecbspx.size()/6-1;
				//计算合并单元格数量
				linetime=0;
				lineclas=0;
				lineksxs=0;
				timenum=0;
				clasnum=0;
				ksxsnum=0;
				numbs=1;
				numjs=1;
				numks=1;

				//生成excel
				for(int i=0;i<vecbspx.size();i=i+6){ 
					//计算考试日期
					//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					//Calendar cal = Calendar.getInstance();
					xxksrq="";
					xxkssjd="";
					for(int m=0;m<ksrqts.length;m++){
						if(m==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[0])){
							xxksrq=ksrqts[m].trim();
						}
					}
					for(int n=0;n<kssjdts.length;n++){
						if(n==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[1])){
							xxkssjd=kssjdts[n].trim();
						}
					}
					
					try {
						cal.setTime(format.parse(xxksrq));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					week=0;
					//考试日期是星期几
					week = cal.get(Calendar.DAY_OF_WEEK)-1;
					
					weekday="";
					if(week==1){
						weekday="一";
					}else if(week==2){
						weekday="二";
					}else if(week==3){
						weekday="三";
					}else if(week==4){
						weekday="四";
					}else if(week==5){
						weekday="五";
					}else if(week==6){
						weekday="六";
					}else if(week==0){
						weekday="日";
					}

					for(int colNum=0; colNum<9; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent2=(counum2-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent2=vecbspx.get(i+colNum-1).toString();				
							if(i/6==lineksxs){
								wsheet2.mergeCells(3, lineksxs+satline, 3, lineksxs+satline+Integer.parseInt(vecksxs.get(ksxsnum).toString())-1);
								lineksxs=lineksxs+Integer.parseInt(vecksxs.get(ksxsnum).toString());
								ksxsnum++;
							}
						}else if(colNum==5){//教室
							//cellContent2=vecbspx.get(i+colNum-1).toString();
							cellContent2="";//地点
							if(i/6==lineclas){
								wsheet2.mergeCells(5, lineclas+satline, 5, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								wsheet2.mergeCells(7, lineclas+satline, 7, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								wsheet2.mergeCells(8, lineclas+satline, 8, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								lineclas=lineclas+Integer.parseInt(vecclas.get(clasnum).toString());
								clasnum++;
							}
						}else if(colNum==6){//考试日期
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent2=name1+xxkssjd;
							if(i/6==linetime){
								wsheet2.mergeCells(6, linetime+satline, 6, linetime+satline+Integer.parseInt(vectime.get(timenum).toString())-1);
								linetime=linetime+Integer.parseInt(vectime.get(timenum).toString());
								timenum++;
							}
						}else if(colNum==7||colNum==8){//监考教师
							cellContent2="";
						}else{
							if(colNum==4){
								cellContent2="";
								String[] bjjc=vecbspx.get(i+colNum-1).toString().split("、");
								cellContent2=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent2+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent2+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent2+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent2=vecbspx.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6||(timenum==vectime.size())){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}	
						}else if(colNum==8){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);							
						}else{
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}					
							
						}
						content = new Label(colNum, counum2, cellContent2, contentStyle);
						wsheet2.addCell(content);						
					}
					wsheet2.setRowView(counum2, 500);
					counum2++;	
				}
			
				//最后一行边框
				cellContent2="";
				for(int colNum=0; colNum<9; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					if(colNum==6){
						contentStyle.setWrap(true);
					}else{
						contentStyle.setShrinkToFit(true);
					}
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				
					content = new Label(colNum, counum2, cellContent2, contentStyle);
					wsheet2.addCell(content);	
				}
				
				
				//试卷标签 3
				//生成标题
				String[] title3=new String[]{"序号","课程名称","人数","考核形式","专业","地点","考试时间"};
				int counum3=0;//excel表中行数
				String cellContent3 = ""; //当前单元格的内容
				
				//设置列宽
				wsheet3.setColumnView(0, 5);
				wsheet3.setColumnView(1, 37);
				wsheet3.setColumnView(2, 5);
				wsheet3.setColumnView(3, 8);
				wsheet3.setColumnView(4, 37);
				wsheet3.setColumnView(5, 8);
				wsheet3.setColumnView(6, 32);
				
				//第1行
				//设置课表标题行列字体大小
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet3.mergeCells(0, counum3, 6, counum3);
				cellContent3 = (Integer.parseInt(xn.substring(2,4))-2)+"级1-4学期（含"+(Integer.parseInt(xn.substring(2,4))-4)+"、"+(Integer.parseInt(xn.substring(2,4))-3)+"级）大补考试卷标签";
				content = new Label(0, counum3, cellContent3, contentStyle);
				wsheet3.addCell(content);
				wsheet3.setRowView(counum3, 760);	
				
				//第2行
				counum3++;
				for(int colNum=0; colNum<7; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setShrinkToFit(true);
					//contentStyle.setWrap(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==6){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}								
					cellContent3=title3[colNum];						
					content = new Label(colNum, counum3, cellContent3, contentStyle);
					wsheet3.addCell(content);	
				}
				wsheet3.setRowView(counum3, 500);

				
				//第3行
				counum3++;	
				
//				for(int i=0;i<vecz5.size();i=i+6){
//					System.out.println("sj:---"+vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+5));
//				}
				
				satline=0;
				endline=0;			
			
				//vecz5 周五上机
				satline=counum3;
				endline=counum3+vecbksj.size()/6-1;
				//wsheet3.mergeCells(3, satline, 3, endline);
				//wsheet3.mergeCells(5, satline, 5, endline);
				wsheet3.mergeCells(6, satline, 6, endline);
				
				//vecz5排序
//				Vector vecbksj=new Vector();
//				for(int i=0;i<roomz5;i++){
//					for(int j=0;j<vecz5.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							vecbksj.add(vecz5.get(j).toString());//课程名称 1
//							vecbksj.add(vecz5.get(j+1).toString());//人数 2
//							vecbksj.add(vecz5.get(j+2).toString());//考试形式 3
//							vecbksj.add(vecz5.get(j+3).toString());//行政班简称 4
//							vecbksj.add(vecz5.get(j+4).toString());//教室 5
//							vecbksj.add(vecz5.get(j+5).toString());//时间 6
//						}
//					}
//				}
				
//				for(int i=0;i<vecbksj.size();i=i+6){
//					System.out.println("sj:---"+vecbksj.get(i)+" | "+vecbksj.get(i+1)+" | "+vecbksj.get(i+2)+" | "+vecbksj.get(i+3)+" | "+vecbksj.get(i+4)+" | "+vecbksj.get(i+5));
//				}
				
				//计算考试日期
				//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				//Calendar cal = Calendar.getInstance();
				xxksrq="";
				xxkssjd="";
				for(int m=0;m<ksrqts.length;m++){
					if(m==Integer.parseInt(vecbksj.get(5).toString().split("#")[0])){
						xxksrq=ksrqts[m].trim();
					}
				}
				for(int n=0;n<kssjdts.length;n++){
					if(n==Integer.parseInt(vecbksj.get(5).toString().split("#")[1])){
						xxkssjd=kssjdts[n].trim();
					}
				}
				
				try {
					cal.setTime(format.parse(xxksrq));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				week=0;
				//考试日期是星期几
				week = cal.get(Calendar.DAY_OF_WEEK)-1;

				weekday="";
				if(week==1){
					weekday="一";
				}else if(week==2){
					weekday="二";
				}else if(week==3){
					weekday="三";
				}else if(week==4){
					weekday="四";
				}else if(week==5){
					weekday="五";
				}else if(week==6){
					weekday="六";
				}else if(week==0){
					weekday="日";
				}
				
				sjtime=0;
				sjclas=0;
				sjksxs=0;
				timesj=0;
				classj=0;
				ksxssj=0;
				sjbs=1;
				sjjs=1;
				sjks=1;
				
				hbnum=0;
				for(int i=0;i<vecbksj.size();i=i+6){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent3=(counum3-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent3=vecbksj.get(i+colNum-1).toString();				
							if(i/6==sjksxs){
								wsheet3.mergeCells(3, sjksxs+satline, 3, sjksxs+satline+Integer.parseInt(vecsjksxs.get(ksxssj).toString())-1);
								sjksxs=sjksxs+Integer.parseInt(vecsjksxs.get(ksxssj).toString());
								ksxssj++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent3="";//地点
							if(i/6==sjclas){
								wsheet3.mergeCells(5, sjclas+satline, 5, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								sjclas=sjclas+Integer.parseInt(vecsjclas.get(classj).toString());
								classj++;
							}
						}else if(colNum==6){ 
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent3=name1+xxkssjd+"(若有两门或两门以上的补考生，考试时间顺延，每门考试时间为2小时。)";
						}else{
							if(colNum==4){
								cellContent3="";
								String[] bjjc=vecbksj.get(i+colNum-1).toString().split("、");
								cellContent3=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent3+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent3+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent3+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent3=vecbksj.get(i+colNum-1).toString();
							}
						}
						
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);							
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);						
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						
						content = new Label(colNum, counum3, cellContent3, contentStyle);
						wsheet3.addCell(content);	
						
					}
					wsheet3.setRowView(counum3, 500);
					counum3++;	
				}
			
				//vecbspx 笔试
				
//				for(int i=0;i<vecbspx.size();i=i+6){
//					System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		   		}
				
				satline=counum3;
				endline=counum3+vecbspx.size()/6-1;
				//计算合并单元格数量
				linetime=0;
				lineclas=0;
				lineksxs=0;
				timenum=0;
				clasnum=0;
				ksxsnum=0;
				numbs=1;
				numjs=1;
				numks=1;

				//生成excel
				for(int i=0;i<vecbspx.size();i=i+6){ 
					//计算考试日期
					//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					//Calendar cal = Calendar.getInstance();
					xxksrq="";
					xxkssjd="";
					for(int m=0;m<ksrqts.length;m++){
						if(m==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[0])){
							xxksrq=ksrqts[m].trim();
						}
					}
					for(int n=0;n<kssjdts.length;n++){
						if(n==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[1])){
							xxkssjd=kssjdts[n].trim();
						}
					}
					
					try {
						cal.setTime(format.parse(xxksrq));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					week=0;
					//考试日期是星期几
					week = cal.get(Calendar.DAY_OF_WEEK)-1;
					
					weekday="";
					if(week==1){
						weekday="一";
					}else if(week==2){
						weekday="二";
					}else if(week==3){
						weekday="三";
					}else if(week==4){
						weekday="四";
					}else if(week==5){
						weekday="五";
					}else if(week==6){
						weekday="六";
					}else if(week==0){
						weekday="日";
					}
					
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent3=(counum3-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent3=vecbspx.get(i+colNum-1).toString();				
							if(i/6==lineksxs){
								wsheet3.mergeCells(3, lineksxs+satline, 3, lineksxs+satline+Integer.parseInt(vecksxs.get(ksxsnum).toString())-1);
								lineksxs=lineksxs+Integer.parseInt(vecksxs.get(ksxsnum).toString());
								ksxsnum++;
							}
						}else if(colNum==5){//教室
							//cellContent3=vecbspx.get(i+colNum-1).toString();
							cellContent3="";//地点
							if(i/6==lineclas){
								wsheet3.mergeCells(5, lineclas+satline, 5, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								lineclas=lineclas+Integer.parseInt(vecclas.get(clasnum).toString());
								clasnum++;
							}
						}else if(colNum==6){//考试日期
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent3=name1+xxkssjd;
							if(i/6==linetime){
								wsheet3.mergeCells(6, linetime+satline, 6, linetime+satline+Integer.parseInt(vectime.get(timenum).toString())-1);
								linetime=linetime+Integer.parseInt(vectime.get(timenum).toString());
								timenum++;
							}
						}else{
							if(colNum==4){
								cellContent3="";
								String[] bjjc=vecbspx.get(i+colNum-1).toString().split("、");
								cellContent3=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent3+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent3+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent3+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent3=vecbspx.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbspx.size()-6||(timenum==vectime.size())){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}	
						}else{
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}					
							
						}
						content = new Label(colNum, counum3, cellContent3, contentStyle);
						wsheet3.addCell(content);						
					}
					wsheet3.setRowView(counum3, 500);
					counum3++;	
				}
				
				//最后一行边框
				cellContent3="";
				for(int colNum=0; colNum<7; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					if(colNum==6){
						contentStyle.setWrap(true);
					}else{
						contentStyle.setShrinkToFit(true);
					}
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				
					content = new Label(colNum, counum3, cellContent3, contentStyle);
					wsheet3.addCell(content);	
				}

				
				//-------------------------------------------------------------------------
							
				
				// 写入数据
				wbook.write();
				// 关闭文件
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
			} else {
				this.setMSG("没有符合条件的成绩信息");
			}
			return savePath;
	}
	
	
	/**
	 * 补考安排表导出
	 * @author lupengfei
	 * @date:2017-07-03
	 * @return savePath 下载路径
	 * @throws SQLException
	*/
	public String ExportExcelBKAP(String tiyutime,String qtlxtime)throws SQLException {
		HttpSession session = request.getSession();
		Vector classVec = null;
		Vector tempVec = null;
		Vector allWpkcVec = new Vector();//未排课程信息
		Vector sqlVec = new Vector();
		String sql = "";
		String sql2 = "";
		String result = "";//返回结果
		String xqzc = "";//学期周次范围 
		int dayNum = 0; //每周天数
		int lessonNum = 0; //每天节数
		int monNum = 0;//上午节数
		int noonNum = 0;//中午节数
		int afternoonNum = 0;//下午节数
		int eveNum = 0;//晚上节数
		String wpks="";//未排考试
		Vector vec2=null;
		Vector vec=new Vector(); //排教室信息
		Vector vecsjxl=new Vector(); //保存已随机到的时间序列
		String maxMxId="";
		String sqladd="";
		Vector vecadd=new Vector(); 
		Vector vecupr=new Vector(); 
		Vector vecupd=new Vector(); 
		String sqlcls="";//大于50人教室
		Vector veccls=null;
		String sqlcls2="";//小于50人教室
		Vector veccls2=null;
		String sqlcls3="";//计算机房
		Vector veccls3=null;
		int overnum=0;
		int errjs=0;
		String xidm="";//分层班系代码
		String xinj="";//分层班年级
		String xiorder="";//分层班所排位置

		String xnxqbm=this.getGG_XNXQBM();
		String qzqm=this.getKCAPZBBH();
		//开始排考试--------------------------------------------------------------------------------
		
		String sqlxi="select [系代码] from dbo.V_基础信息_系基础信息表  where len([系代码])>1";
		Vector vecxi=db.GetContextVector(sqlxi);
		
		String sqlmajor="select [系代码],[专业代码] from dbo.V_基础信息_系专业关系表  where len([系代码])>1";
		Vector vecmajor=db.GetContextVector(sqlmajor);
		
		//获取日期，时间段信息
		String sqlksrq="select [学年学期编码],[考试名称],[考试类型],[上课截止周数],[考试日期] from [dbo].[V_考试管理_考场安排主表] where [考场安排主表编号]='"+qzqm+"' ";
		Vector vecksrq=db.GetContextVector(sqlksrq);
		String ksrq=vecksrq.get(4).toString();//考试日期
		String[] ksrqts=ksrq.split(",");

		String kssjd= MyTools.getProp(request, "Base.examTime");//考试时间段
		String[] kssjdts=kssjd.split(",");
		
		//获取特殊课程安排规则
		String sqltskcxx="select [考试场次编号],[日期时间段] from V_考试管理_考试特殊课程规则 where [考试编号]='"+qzqm+"' ";
		Vector vectskcxx=db.GetContextVector(sqltskcxx);
		Vector vecspkcxx=new Vector();
		String tsksrqid="";
		String tskssjdid="";
		if(vectskcxx!=null&&vectskcxx.size()>0){
			for(int i=0;i<vectskcxx.size();i=i+2){
				String[] rqsjd=vectskcxx.get(i+1).toString().split(",");
				for(int m=0;m<rqsjd.length;m++){
					for(int j=0;j<ksrqts.length;j++){ 
						if(rqsjd[m].split("#")[0].equals(ksrqts[j].trim())){
							tsksrqid=j+"";
						}
					}
					for(int k=0;k<kssjdts.length;k++){
						if(rqsjd[m].split("#")[1].equals(kssjdts[k])){
							tskssjdid=k+"";
						}
					}
					vecspkcxx.add(vectskcxx.get(i).toString());
					vecspkcxx.add(tsksrqid);
					vecspkcxx.add(tskssjdid);			
				}
			}
		}
		
//		for(int i=0;i<vecspkcxx.size();i=i+3){
//			System.out.println(vecspkcxx.get(i+0).toString()+"|"+vecspkcxx.get(i+1).toString()+"|"+vecspkcxx.get(i+2).toString());
//		}
		
		//获取普通考试安排规则
		String sqlksxx="select [考试日期],[考试时间段],[考试类型],[考试年级],[课程类型],[教室人数],[考场数量],[可用大教室] from [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+qzqm+"' and [考试年级]!='' and [课程类型]!='' and [教室人数]!=''";
		Vector vecksxx=db.GetContextVector(sqlksxx);
		String ksrqid="";
		String kssjdid="";
		int maxsjd=0;//用到最大时间段数量
		int[] sjdn=new int[ksrqts.length];
		for(int i=0;i<vecksxx.size();i=i+8){
			for(int j=0;j<ksrqts.length;j++){ 
				if(vecksxx.get(i).toString().equals(ksrqts[j].trim())){
					sjdn[j]++;
				}
			}
		}
		for(int s=0;s<sjdn.length;s++){
			if(maxsjd<sjdn[s]){
				maxsjd=sjdn[s];
			}
		}
		
		//把考试日期和考试时间段替换成对应的数字编号
		for(int i=0;i<vecksxx.size();i=i+8){
			for(int j=0;j<ksrqts.length;j++){ 
				if(vecksxx.get(i).toString().equals(ksrqts[j].trim())){
					ksrqid=j+"";
				}
			}
			for(int k=0;k<kssjdts.length;k++){
				if(vecksxx.get(i+1).toString().equals(kssjdts[k])){
					kssjdid=k+"";
				}
			}
			vecksxx.setElementAt(ksrqid, i);
			vecksxx.setElementAt(kssjdid, i+1);
		}
		
		//分开笔试和上机
		Vector vecbsxx=new Vector();//笔试
		Vector vecsjxx=new Vector();//上机
		for(int i=0;i<vecksxx.size();i=i+8){
			if(vecksxx.get(i+2).toString().equals("笔试")){
				for(int v=0;v<8;v++){
					vecbsxx.add(vecksxx.get(i+v).toString());
				}
			}else if(vecksxx.get(i+2).toString().equals("上机")){
				for(int v=0;v<8;v++){
					vecsjxx.add(vecksxx.get(i+v).toString());
				}
			}
		}
		
//		for(int i=0;i<vecksxx.size();i=i+8){
//			System.out.println(vecksxx.get(i+0).toString()+"|"+vecksxx.get(i+1).toString()+"|"+vecksxx.get(i+2).toString()+"|"+vecksxx.get(i+3).toString()+"|"+vecksxx.get(i+4).toString()+"|"+vecksxx.get(i+5).toString()+"|"+vecksxx.get(i+6).toString()+"|"+vecksxx.get(i+7).toString());
//		}
		
		//取相同考试场次编号大于1的,需要同时开考的
		String sqlcc="select a.考试场次编号 ,a.num from (select 考试场次编号,COUNT(*) as num from dbo.V_考试管理_考试设置 where [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [期中期末]='"+MyTools.fixSql(qzqm)+"' group by 考试场次编号) a where a.num!=1 ";
		Vector veccc=db.GetContextVector(sqlcc);

		//获取所有教室
		sqlcls="select a.教室编号,a.教室名称,case when b.教室类型代码='4' then '4' else '1' end as 教室类型代码,a.教室容量  from dbo.V_考试管理_考试教室 a,dbo.V_教室数据类 b where a.教室编号=b.教室编号  and a.考试名称='"+MyTools.fixSql(qzqm)+"' ";
		veccls=db.GetContextVector(sqlcls);
//		sqlcls2="select 教室编号,教室名称,教室类型代码,教室容量  from dbo.V_考试管理_考试教室  where 教室容量='2' and [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [考试周期]='"+MyTools.fixSql(qzqm)+"' ";
//		veccls2=db.GetContextVector(sqlcls2);
		
		//获取所有监考教师
		String sqltea="select [监考教师编号],[监考教师姓名],[监考类型],[监考日期]  from dbo.V_考试管理_监考教师  where [考试名称]='"+MyTools.fixSql(qzqm)+"' order by 监考类型,[监考日期] desc ";
		Vector vectea=db.GetContextVector(sqltea);
		if(vectea!=null&&vectea.size()>0){
			for(int i=0;i<vectea.size();i=i+4){
				for(int j=0;j<ksrqts.length;j++){ 
					if(vectea.get(i+3).toString().equals(ksrqts[j].trim())){
						ksrqid=j+"";
						vectea.setElementAt(ksrqid, i+3);
					}
				}	
			}
		}
		//获取只能排一天的监考教师
		String sqltea1="select count(*)  from dbo.V_考试管理_监考教师  where [监考类型]!='5' and [考试名称]='"+MyTools.fixSql(qzqm)+"' ";
		Vector vectea1=db.GetContextVector(sqltea1);
		int teachnum=Integer.parseInt(vectea1.get(0).toString());

		String sqlptks="SELECT distinct [考试日期] FROM [dbo].[V_考试管理_考试规则] where [考试主表编号]='"+MyTools.fixSql(qzqm)+"' and [课程类型]='普通课程' ";
		Vector vecptks=db.GetContextVector(sqlptks);
		int ptksts=1;
		if(vecptks!=null&vecptks.size()>0){
			ptksts=vecptks.size();
		}
		
		int tearc=teachnum*3/ptksts;//平均每天多少监考教师人次	
		int day=tearc+(6-tearc%6);
		
//		int day2=teachnum2*3;//02
//		int day3=(teachnum-teachnum2*2)*2;//03
		int[] usednum=new int[ksrqts.length];//存各天已排教师数量
		for(int d=0;d<usednum.length;d++){
			usednum[d]=0;
		}

		//删除已有排考场信息
//		String sqldel="delete from dbo.V_考试管理_考场安排明细表 where [考场安排主表编号] ='"+MyTools.fixSql(qzqm)+"' ";
//		db.executeInsertOrUpdate(sqldel);
		
		//设置考场安排主表	
		this.setKCAPZBBH(qzqm);
		//获取考场安排明细编号
//		String sqlmxid="select max(cast(SUBSTRING([考场安排明细编号],8,11) as bigint)) from dbo.V_考试管理_考场安排明细表";
//		Vector vecmxid = db.GetContextVector(sqlmxid);
//		if (!vecmxid.toString().equals("[]") && vecmxid.size() > 0) {
//			maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vecmxid.get(0))))+1);
//			this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
//		}else{
//			maxMxId= String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr("10000000000")))+1);
//			this.setKCAPMXBH("KCAPMX_"+maxMxId);//设置授课计划明细主键
//		}
		
		//补考
		//if(vecksrq.get(2).toString().equals("2")){
		
			String sql3 = "";
			String sqlkssz = "";
			String savePath="";
			Vector vec3 = null;
			Vector vecins = new Vector();
			String xn=xnxqbm.substring(0,4);
			
			//查询授课计划是空的课程，把相关编号插入到dbo.V_考试管理_考试设置 
//			sqlkssz="select distinct 相关编号,授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式, " +
//					"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 from ( " +
//					"select a.相关编号,case when b.来源类型='3' then g.授课计划明细编号 else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when b.来源类型='3' then l.考试形式 else k.考试形式 end as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) else 0 end as 学分,case when e.年级代码=(cast(cast(right('2016',2) as int)-2 as varchar)+'1') then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
//					"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式 k on f.考试形式=k.编号 left join dbo.V_考试形式 l on g.考试形式=l.编号 " +
//					"where d.学生状态 in ('01','05','08') and 成绩状态='1' and left(c.学年学期编码,4)<>'"+xn+"' and e.年级代码 in (cast(cast(right('"+xn+"',2) as int)-2 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-3 as varchar)+'1',cast(cast(right('"+xn+"',2) as int)-4 as varchar)+'1')) as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')  and 授课计划明细编号 is null " +
//					"group by 相关编号,授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式,系名称";
//			Vector veckssz=db.GetContextVector(sqlkssz);
//			if(veckssz!=null&&veckssz.size()>0){
//				for(int i=0;i<veckssz.size();i=i+9){
//					sql2="insert into V_考试管理_考试设置 ([授课计划明细编号],[是否考试],[课程名称],[学年学期编码],[行政班名称]) " +
//						 "values ('"+veckssz.get(i).toString()+"','2','"+veckssz.get(i+3).toString()+"','"+veckssz.get(i+2).toString()+"','"+veckssz.get(i+8).toString()+"') ";
//					vecins.add(sql2);
//				}
//				db.executeInsertOrUpdateTransaction(vecins);
//			}
				
			
//			String[] timeordersj=new String[ksrqts.length*kssjdts.length];//保存时间段排序
//			for(int r=0;r<ksrqts.length;r++){
//				for(int s=0;s<kssjdts.length;s++){
//					for(int k=0;k<vecksxx.size();k=k+8){
//						//是笔试，日期+时间段相同
//						if(vecksxx.get(k+2).toString().equals("上机")&&vecksxx.get(k).toString().equals(ksrqts[r].trim())&&vecksxx.get(k+1).toString().equals(kssjdts[s].trim())){
//							int kyroom=0;
//							timeordersj[r*kssjdts.length+s]=r+"#"+s+"@";
//							kyroom=Integer.parseInt(vecksxx.get(k+6).toString());
//							if(kyroom==0){
//								
//							}else{
//								for(int e=0;e<kyroom;e++){
//									timeordersj[r*kssjdts.length+s]+=0+",";
//								}
//								timeordersj[r*kssjdts.length+s]=timeordersj[r*kssjdts.length+s].substring(0, timeordersj[r*kssjdts.length+s].length()-1);
//							}	
//						}
//					}	
//				}
//			}
			
			
			//从学期考试设置获取合并信息
			String sqlchkkscc="select count(*) from V_考试管理_补考合并信息 where 考试学期='"+xnxqbm+"'";
			if(db.getResultFromDB(sqlchkkscc)){
				
			}else{
				String sqlkscc="select [授课计划明细编号],[考试场次编号] from dbo.V_考试管理_考试设置 where [考试场次编号] in (" +
						"select [考试场次编号] from ( " +
						"SELECT [考试场次编号],COUNT(*) as samenum FROM [V_考试管理_考试设置] where [学年学期编码]='"+xnxqbm+"' group by [考试场次编号] ) a " +
						"where samenum>1 ) " +
						"group by [考试场次编号],[授课计划明细编号]";
				Vector veckscc=db.GetContextVector(sqlkscc);
				String skjhbhkscc="";
				String sqlinskscc="";
				Vector vecinskscc=new Vector();
				if(veckscc!=null&&veckscc.size()>0){
					skjhbhkscc+=veckscc.get(0).toString();
					for(int i=2;i<veckscc.size();i=i+2){
						if(veckscc.get(i-1).toString().equals(veckscc.get(i+1).toString())){
							skjhbhkscc+="@"+veckscc.get(i).toString();
						}else{
							sqlinskscc="insert into V_考试管理_补考合并信息 ([考试学期],[授课计划明细编号]) values ('"+xnxqbm+"','"+skjhbhkscc+"') ";
							vecinskscc.add(sqlinskscc);
							skjhbhkscc=veckscc.get(i).toString();
						}
						if(i==(veckscc.size()-2)){
							sqlinskscc="insert into V_考试管理_补考合并信息 ([考试学期],[授课计划明细编号]) values ('"+xnxqbm+"','"+skjhbhkscc+"') ";
							vecinskscc.add(sqlinskscc);
						}
					}
					db.executeInsertOrUpdateTransaction(vecinskscc);
				}
			}
			//------------------------------------------------------------------------------------
			int njm1=Integer.parseInt(xnxqbm.substring(2,4));//年级码
			int njm2=njm1-1;
			String nj1="";
			String nj2="";
			if(njm1<10){
				nj1="0"+njm1+"1";
			}else{
				nj1=njm1+"1";
			}
			if(njm2<10){
				nj2="0"+njm2+"1";
			}else{
				nj2=njm2+"1";
			}
					
			sql="select 授课计划明细编号,学年学期,课程名称,COUNT(*) as 人数,考试形式,系名称,行政班简称,行政班名称 from ( " +
					"select distinct 学号,姓名,授课计划明细编号,学年学期,课程名称,考试形式," +
					"case when 系名称='信息技术与机电工程系' then '信机系' when 系名称='应用艺术系' then '艺术系' when 系名称='经济管理系' then '经管系' when 系名称='商务外语系' then '外语系' when 系名称='学前教育系' then '学前系' else '' end as 系名称,行政班简称,行政班名称 " +
					"from (select case when b.来源类型='3' then g.授课计划明细编号 when b.来源类型='2' then m.编号  else f.授课计划明细编号 end as 授课计划明细编号,left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班名称,e.行政班简称,j.系名称,case when b.来源类型='3' then l.编号 else k.编号 end as 考试形式编号,case when b.来源类型='3' then l.考试形式 else k.考试形式 end as 考试形式,case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称,case when b.来源类型='3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称,case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) when '4' then (select t1.学分 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号 where t.分层班编号=b.相关编号) else 0 end as 学分,case when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩  " +
					"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 left join V_学生基本数据子类 d on d.学号=a.学号 left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码  left join V_考试管理_考试设置 f on a.相关编号=f.授课计划明细编号 left join V_规则管理_选修课授课计划明细表 g on a.相关编号=g.授课计划明细编号 left join V_专业基本信息数据子类 h on e.专业代码=h.专业代码 left join V_基础信息_系专业关系表 i on h.专业代码=i.专业代码 left join V_基础信息_系基础信息表 j on i.系代码=j.系代码 left join dbo.V_考试形式  k on f.考试形式=k.编号 left join dbo.V_考试形式 l on g.考试形式=l.编号 left join dbo.V_排课管理_添加课程信息表 m on a.相关编号=m.编号 " +
					"where c.学年学期编码='"+xnxqbm+"' and d.学生状态 in ('01','05','07','08') and 成绩状态='1' " +
					"and e.年级代码 in ('"+nj1+"','"+nj2+"') " +
					"and a.相关编号 not in (select [选修课授课计划编号] from [V_选修课与智慧树对应表]) "+
					"and a.相关编号 not in (" +
						"select 相关编号 from (select distinct a.相关编号,sum(case when 成绩<>'' then 1 else 0 end) as 已登分人数 " +
						"from V_成绩管理_登分教师信息表 a left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 left join (select ta.学号,ta.相关编号," +
						"case when (case when ta.平时 in ('-1','-5') then '' else ta.平时 end)<>'' then ta.平时 " +
						"when (case when ta.期中 in ('-1','-5') then '' else ta.期中 end)<>'' then ta.期中 " +
						"when (case when ta.实训 in ('-1','-5') then '' else ta.实训 end)<>'' then ta.实训 " +
						"when (case when ta.期末 in ('-1','-5') then '' else ta.期末 end)<>'' then ta.期末 " +
						"when (case when ta.总评 in ('-1','-5') then '' else ta.总评 end)<>'' then ta.总评 " +
						"when (case when ta.重修1 in ('-1','-5') then '' else ta.重修1 end)<>'' then ta.重修1 " +
						"when (case when ta.重修2 in ('-1','-5') then '' else ta.重修2 end)<>'' then ta.重修2 " +
						"when (case when ta.补考 in ('-1','-5') then '' else ta.补考 end)<>'' then ta.补考 " +
						"when (case when ta.大补考 in ('-1','-5') then '' else ta.大补考 end)<>'' then ta.大补考 else '' end as 成绩 " +
						"from V_成绩管理_学生成绩信息表 ta left join V_成绩管理_科目课程信息表 tb on ta.科目编号=tb.科目编号 where ta.状态='1' and ta.成绩状态='1' and tb.学年学期编码='"+xnxqbm+"') c on c.相关编号=a.相关编号 " +
						"where a.状态='1' and b.状态='1' and b.学年学期编码='"+xnxqbm+"' group by a.相关编号,a.行政班代码,a.来源类型) as t where t.已登分人数=0 " +
					") " +
					") as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')  " +		        
			        ") x group by 授课计划明细编号,学年学期,课程名称,行政班名称,行政班简称,考试形式,系名称 order by 课程名称,行政班简称  " ;		
			vec=db.GetContextVector(sql);
					
			String skjhmxs="";
			sql3="SELECT count(*) FROM [dbo].[V_考试管理_补考合并信息] where [考试学期]='"+MyTools.fixSql(xnxqbm)+"' ";
			if(db.getResultFromDB(sql3)){
				sql3="SELECT [授课计划明细编号] FROM [dbo].[V_考试管理_补考合并信息] where [考试学期]='"+MyTools.fixSql(xnxqbm)+"' ";
				vec3=db.GetContextVector(sql3);
				for(int j=0;j<vec3.size();j++){
					skjhmxs+=vec3.get(j).toString()+"@";
				}	
			}else{
						
			}
				
			Vector vecr=new Vector();
			Vector vecs=new Vector();
			Vector vect=new Vector();
			for(int i=0;vec.size()>1;){
				String skjhmx=vec.get(i).toString();
				String xnxqbm2=vec.get(i+1).toString();
				String kcmc=vec.get(i+2).toString();
				String rs=vec.get(i+3).toString();
				String ksxs=vec.get(i+4).toString();
				String xmc=vec.get(i+5).toString();
				String xzbjc=vec.get(i+6).toString();
				String xzbmc=vec.get(i+7).toString();
				//在合并信息中,保存到vecs
				if(!skjhmxs.equals("")&&skjhmxs.indexOf(vec.get(i).toString())>-1){ 
					vecs.add(skjhmx);//授课计划明细编号 1
					vecs.add(xnxqbm2);//学年学期 2
					vecs.add(kcmc);//课程名称 3
					vecs.add(rs);//人数 4
					vecs.add(ksxs);//考试形式 5
					vecs.add(xmc);//系名称 6
					vecs.add(xzbjc);//行政班简称 7
					vecs.add(xzbmc);//行政班名称 8
				}else{	
					for(int j=8;j<vec.size();j=j+8){
						//是选修课
						if(vec.get(i+2).toString().indexOf("选修")>-1){ 
							//课程名称 相同 合并
							if(vec.get(i+2).toString().equals(vec.get(j+2).toString())){ 
								skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
								//取学年学期大的 2
								if(Integer.parseInt(vec.get(i+1).toString())>Integer.parseInt(vec.get(j+1).toString())){
									xnxqbm2=vec.get(i+1).toString();
								}else{
									xnxqbm2=vec.get(j+1).toString();
								}
								//课程名称 3
								rs+="$"+vec.get(j+3).toString();//人数 4
								//考试形式 5
								xmc+="$"+vec.get(j+5).toString();//系名称 6
								xzbjc+="$"+vec.get(j+6).toString();//行政班简称 7
								xzbmc+="$"+vec.get(j+7).toString();//行政班名称 8
										
								for(int k=0;k<8;k++){
									vec.remove(j);
								}
								j=j-8;
							}
									
						}else{ 
							//课程名称,系名称,班级简称 相同 合并
							if(vec.get(i+2).toString().equals(vec.get(j+2).toString())&&vec.get(i+5).toString().equals(vec.get(j+5).toString())&&(vec.get(i+6).toString().equals(vec.get(j+6).toString())||vec.get(i+6).toString().substring(2,4).equals(vec.get(j+6).toString().substring(2,4)))){
								skjhmx+="$"+vec.get(j).toString();//授课计划明细编号 1
								//取学年学期大的 2
								if(Integer.parseInt(vec.get(i+1).toString())>Integer.parseInt(vec.get(j+1).toString())){
									xnxqbm2=vec.get(i+1).toString();
								}else{
									xnxqbm2=vec.get(j+1).toString();
								}
								//课程名称 3
								rs+="$"+vec.get(j+3).toString();//人数 4
								//考试形式 5
								//系名称 6
								xzbjc+="$"+vec.get(j+6).toString();//行政班简称 7
								xzbmc+="$"+vec.get(j+7).toString();//行政班名称 8
										
								for(int k=0;k<8;k++){
									vec.remove(j);
								}
								j=j-8;
							}	
						}
					}
							
					vecr.add(skjhmx);//授课计划明细编号 1
					vecr.add(xnxqbm2);//学年学期 2
					vecr.add(kcmc);//课程名称 3
					vecr.add(rs);//人数 4
					vecr.add(ksxs);//考试形式 5
					vecr.add(xmc);//系名称 6
					vecr.add(xzbjc);//行政班简称 7
					vecr.add(xzbmc);//行政班名称 8			
				}
				for(int k=0;k<8;k++){
					vec.remove(i);
				}
			}

			//vec3 补考合并信息中 授课计划编号
			if(vec3!=null&&vec3.size()>0){
				for(int i=0;i<vec3.size();i++){
					String sskjhmx="";
					String sxnxqbm="";
					String skcmc="";
					String srs="";
					String sksxs="";
					String sxmc="";
					String sxzbjc="";
					String sxzbmc="";
					for(int j=0;j<vecs.size();j=j+8){
						if(vec3.get(i).toString().indexOf(vecs.get(j).toString())>-1){
							
							sxnxqbm+=vecs.get(j+1).toString()+"@";
							skcmc+=vecs.get(j+2).toString()+"@";
							srs+=vecs.get(j+3).toString()+"@";
							sksxs=vecs.get(j+4).toString();
							sxmc+=vecs.get(j+5).toString()+"@";
							sxzbjc+=vecs.get(j+6).toString()+"@";
							sxzbmc+=vecs.get(j+7).toString()+"@";	
						}	
					}
							
					sskjhmx=vec3.get(i).toString();
					int max=0;
					if(!sxnxqbm.equals("")){
						sxnxqbm=sxnxqbm.substring(0,sxnxqbm.length()-1);
								
						String[] xq=sxnxqbm.split("@");
						max=Integer.parseInt(xq[0]);
						for(int k=0;k<xq.length;k++){
							if(max<Integer.parseInt(xq[k])){
								max=Integer.parseInt(xq[k]);
							}
						}
					}
					if(!skcmc.equals("")){
						skcmc=skcmc.substring(0,skcmc.length()-1);
					}
					if(!srs.equals("")){
						srs=srs.substring(0,srs.length()-1);
					}
					if(!sxmc.equals("")){
						sxmc=sxmc.substring(0,sxmc.length()-1);
					}
					if(!sxzbjc.equals("")){
						sxzbjc=sxzbjc.substring(0,sxzbjc.length()-1);
					}
					if(!sxzbmc.equals("")){
						sxzbmc=sxzbmc.substring(0,sxzbmc.length()-1);
					}
							
					if(!sxnxqbm.equals("")){
						vect.add(sskjhmx);//授课计划明细编号 1
						vect.add(max+"");//学年学期 2
						vect.add(skcmc);//课程名称 3
						vect.add(srs);//人数 4
						vect.add(sksxs);//考试形式 5
						vect.add(sxmc);//系名称 6
						vect.add(sxzbjc);//行政班简称 7
						vect.add(sxzbmc);//行政班名称 8	
					}
				}
			}
					
			for(int i=0;i<vecr.size();i++){
				vect.add(vecr.get(i).toString());
			}
			
			//开始排考试
			Vector veca=new Vector();
			Vector vecb=new Vector();
			Vector vecc=new Vector();
			Vector vecty=new Vector();
			Vector vece=new Vector();
			Vector vecf=new Vector();
			Vector vecg=new Vector();
			Vector vech=new Vector();
			Vector veci=new Vector();
			Vector vecj=new Vector();
			//简化输出名称
			for(int i=0;i<vect.size();i=i+8){
				String region1=vect.get(i+2).toString();
				region1=region1.replaceAll("@", "\\$");
				String showkcmc="";
				if(region1.indexOf("$")>-1){
					String[] kcmcinfo=region1.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showkcmc.equals("")){
							showkcmc=kcmcinfo[k]+"、";
						}
						if(showkcmc.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showkcmc+=kcmcinfo[k]+"、";
						}
					}
					showkcmc=showkcmc.substring(0,showkcmc.length()-1);
				}else{
					showkcmc=vect.get(i+2).toString();
				}
				veca.add(showkcmc);//课程名称 3
				
				String region=vect.get(i+3).toString();
				region=region.replaceAll("@", "\\$");
				int showrs=0;
				if(region.indexOf("$")>-1){
					String[] kcmcinfo=region.split("\\$");
					if(region.indexOf("$")>-1){
						for(int k=0;k<kcmcinfo.length;k++){
							showrs+=Integer.parseInt(kcmcinfo[k]);
						}
					}else{
						showrs=Integer.parseInt(region);
					}
				}else{
					showrs=Integer.parseInt(vect.get(i+3).toString());
				}
				veca.add(showrs+"");//人数 4
				
				String region2=vect.get(i+4).toString();
				region2=region2.replaceAll("@", "\\$");
				String showksxs="";
				if(region2.indexOf("$")>-1){
					String[] kcmcinfo=region2.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showksxs.equals("")){
							showksxs=kcmcinfo[k]+"、";
						}
						if(showksxs.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showksxs+=kcmcinfo[k]+"、";
						}
					}
					showksxs=showksxs.substring(0,showksxs.length()-1);
				}else{
					showksxs=vect.get(i+4).toString();
				}
				veca.add(showksxs);//考试形式 5
				
				String region3=vect.get(i+5).toString();
				region3=region3.replaceAll("@", "\\$");
				String showxmc="";
				if(region3.indexOf("$")>-1){
					String[] kcmcinfo=region3.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showxmc.equals("")){
							showxmc=kcmcinfo[k]+"、";
						}
						if(showxmc.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showxmc+=kcmcinfo[k]+"、";
						}
					}
					showxmc=showxmc.substring(0,showxmc.length()-1);
				}else{
					showxmc=vect.get(i+5).toString();
				}
				veca.add(showxmc);//系名称 6
				
				String region4=vect.get(i+6).toString();
				region4=region4.replaceAll("@", "\\$");
				String showbjjc="";
				if(region4.indexOf("$")>-1){
					String[] kcmcinfo=region4.split("\\$");
					for(int k=0;k<kcmcinfo.length;k++){
						if(showbjjc.equals("")){
							showbjjc=kcmcinfo[k]+"、";
						}
						if(showbjjc.indexOf(kcmcinfo[k])>-1){//名称在显示中以存在
						
						}else{
							showbjjc+=kcmcinfo[k]+"、";
						}
					}
					showbjjc=showbjjc.substring(0,showbjjc.length()-1);
				}else{
					showbjjc=vect.get(i+6).toString();
				}
				veca.add(showbjjc);//行政班简称 7
				veca.add(vect.get(i).toString());//授课计划明细编号 1
			}
			
//			for(int i=0;i<veca.size();i=i+6){
//				System.out.println(i/6+":--"+veca.get(i)+" | "+veca.get(i+1)+" | "+veca.get(i+2)+" | "+veca.get(i+3)+" | "+veca.get(i+4)+" | "+veca.get(i+5));
//			}
			
			//按考试类型不同分别保存
			for(int i=0;i<veca.size();i=i+6){
				if(veca.get(i).toString().indexOf("体育")>-1){
					vecty.add(veca.get(i).toString());//课程名称 1
					vecty.add(veca.get(i+1).toString());//人数 2
					vecty.add(veca.get(i+2).toString());//考试形式 3
					vecty.add(veca.get(i+4).toString());//行政班简称 4
					vecty.add("ty");//地点 5
					vecty.add("ty");//时间 6
					vecty.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}
			}

			for(int i=0;i<veca.size();i=i+6){ 
				if(veca.get(i+2).toString().equals("开卷上机")||veca.get(i+2).toString().equals("闭卷上机")){
					vecb.add(veca.get(i).toString());//课程名称 1
					vecb.add(veca.get(i+1).toString());//人数 2
					vecb.add(veca.get(i+2).toString());//考试形式 3
					vecb.add(veca.get(i+3).toString());//系名称 4
					vecb.add(veca.get(i+4).toString());//行政班简称 5
					vecb.add(veca.get(i+5).toString());//授课计划编号6
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("开卷")||veca.get(i+2).toString().equals("半开卷")||veca.get(i+2).toString().equals("闭卷")||veca.get(i+2).toString().equals("半闭卷")){
					vecc.add(veca.get(i).toString());//课程名称 1
					vecc.add(veca.get(i+1).toString());//人数 2
					vecc.add(veca.get(i+2).toString());//考试形式 3
					vecc.add(veca.get(i+3).toString());//系名称 4
					vecc.add(veca.get(i+4).toString());//行政班简称 5
					vecc.add(veca.get(i+5).toString());//授课计划编号6
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("口试（随堂）")){
					vece.add(veca.get(i).toString());//课程名称 1
					vece.add(veca.get(i+1).toString());//人数 2
					vece.add(veca.get(i+2).toString());//考试形式 3
					vece.add(veca.get(i+4).toString());//行政班简称 4
					vece.add("ks");//地点 5
					vece.add("ks");//时间 6
					vece.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("听力（随堂）")){
					vecf.add(veca.get(i).toString());//课程名称 1
					vecf.add(veca.get(i+1).toString());//人数 2
					vecf.add(veca.get(i+2).toString());//考试形式 3
					vecf.add(veca.get(i+4).toString());//行政班简称 4
					vecf.add("tl");//地点 5
					vecf.add("tl");//时间 6
					vecf.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("实训")){
					vecg.add(veca.get(i).toString());//课程名称 1
					vecg.add(veca.get(i+1).toString());//人数 2
					vecg.add(veca.get(i+2).toString());//考试形式 3
					vecg.add(veca.get(i+4).toString());//行政班简称 4
					vecg.add("sx");//地点 5
					vecg.add("sx");//时间 6
					vecg.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("随堂")||veca.get(i+2).toString().equals("上机（随堂）")){
					vech.add(veca.get(i).toString());//课程名称 1
					vech.add(veca.get(i+1).toString());//人数 2
					vech.add(veca.get(i+2).toString());//考试形式 3
					vech.add(veca.get(i+4).toString());//行政班简称 4
					vech.add("st");//地点 5
					vech.add("st");//时间 6
					vech.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("大作业（随堂）")){
					veci.add(veca.get(i).toString());//课程名称 1
					veci.add(veca.get(i+1).toString());//人数 2
					veci.add(veca.get(i+2).toString());//考试形式 3
					veci.add(veca.get(i+4).toString());//行政班简称 4
					veci.add("dzy");//地点 5
					veci.add("dzy");//时间 6
					veci.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}else if(veca.get(i+2).toString().equals("")||veca.get(i+2).toString().equals("其它")||veca.get(i+2).toString().equals("过程性")||veca.get(i+2).toString().equals("口试+笔试")||veca.get(i+2).toString().equals("外考")){
					vecj.add(veca.get(i).toString());//课程名称 1
					vecj.add(veca.get(i+1).toString());//人数 2
					vecj.add(veca.get(i+2).toString());//考试形式 3
					vecj.add(veca.get(i+4).toString());//行政班简称 4
					vecj.add("kong");//地点 5
					vecj.add("kong");//时间 6
					vecj.add(veca.get(i+5).toString());//授课计划编号7
					for(int k=0;k<6;k++){
						veca.remove(i);
					}
					i=i-6;
				}
			}

//			for(int i=0;i<veca.size();i=i+6){
//				System.out.println("a:--"+veca.get(i)+" | "+veca.get(i+1)+" | "+veca.get(i+2)+" | "+veca.get(i+3)+" | "+veca.get(i+4)+" | "+veca.get(i+5));
//			}
			
//			for(int i=0;i<vecb.size();i=i+6){
//				System.out.println(vecb.get(i)+" | "+vecb.get(i+1)+" | "+vecb.get(i+2)+" | "+vecb.get(i+3)+" | "+vecb.get(i+4));
//			}

			//相同课程合并
			for(int i=7;i<vecty.size();i=i+7){
				while(vecty.get(i-7).toString().equals(vecty.get(i).toString())){
					int hbnum=Integer.parseInt(vecty.get(i+1-7).toString())+Integer.parseInt(vecty.get(i+1).toString());
					vecty.setElementAt(hbnum, i+1-7);
					vecty.setElementAt(vecty.get(i+3-7).toString()+"、"+vecty.get(i+3).toString(), i+3-7);
					for(int k=0;k<7;k++){
						vecty.remove(i);
					}
					if(i==vecty.size()){
						break;
					}
				}
			}
			
//			for(int i=7;i<vece.size();i=i+7){
//				while(vece.get(i-7).toString().equals(vece.get(i).toString())){
//					int hbnum=Integer.parseInt(vece.get(i+1-7).toString())+Integer.parseInt(vece.get(i+1).toString());
//					vece.setElementAt(hbnum, i+1-7);
//					vece.setElementAt(vece.get(i+3-7).toString()+"、"+vece.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vece.remove(i);
//					}
//					if(i==vece.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<vecf.size();i=i+7){
//				while(vecf.get(i-7).toString().equals(vecf.get(i).toString())){
//					int hbnum=Integer.parseInt(vecf.get(i+1-7).toString())+Integer.parseInt(vecf.get(i+1).toString());
//					vecf.setElementAt(hbnum, i+1-7);
//					vecf.setElementAt(vecf.get(i+3-7).toString()+"、"+vecf.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vecf.remove(i);
//					}
//					if(i==vecf.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<vecg.size();i=i+7){
//				while(vecg.get(i-7).toString().equals(vecg.get(i).toString())){
//					int hbnum=Integer.parseInt(vecg.get(i+1-7).toString())+Integer.parseInt(vecg.get(i+1).toString());
//					vecg.setElementAt(hbnum, i+1-7);
//					vecg.setElementAt(vecg.get(i+3-7).toString()+"、"+vecg.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vecg.remove(i);
//					}
//					if(i==vecg.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<vech.size();i=i+7){
//				while(vech.get(i-7).toString().equals(vech.get(i).toString())){
//					int hbnum=Integer.parseInt(vech.get(i+1-7).toString())+Integer.parseInt(vech.get(i+1).toString());
//					vech.setElementAt(hbnum, i+1-7);
//					vech.setElementAt(vech.get(i+3-7).toString()+"、"+vech.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						vech.remove(i);
//					}
//					if(i==vech.size()){
//						break;
//					}
//				}
//			}
//			
//			for(int i=7;i<veci.size();i=i+7){
//				while(veci.get(i-7).toString().equals(veci.get(i).toString())){
//					int hbnum=Integer.parseInt(veci.get(i+1-7).toString())+Integer.parseInt(veci.get(i+1).toString());
//					veci.setElementAt(hbnum, i+1-7);
//					veci.setElementAt(veci.get(i+3-7).toString()+"、"+veci.get(i+3).toString(), i+3-7);
//					for(int k=0;k<7;k++){
//						veci.remove(i);
//					}
//					if(i==veci.size()){
//						break;
//					}
//				}
//			}
			
			//查询特殊课程规则
						
//			for(int i=0;i<vecz5.size();i=i+6){
//				System.out.println(vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+4));
//			}
			
//			for(int i=0;i<vecc.size();i=i+6){
//				System.out.println("cc:--"+vecc.get(i)+" | "+vecc.get(i+1)+" | "+vecc.get(i+2)+" | "+vecc.get(i+3)+" | "+vecc.get(i+4)+" | "+vecc.get(i+5));
//			}
			
		
		//排考试完毕----------------------------------------------------------------------	
		String sqlsj="select [课程名称],[学生人数],[试卷类型] as 考试形式,[行政班名称],[场地要求],[时间序列] from dbo.V_考试管理_考场安排明细表  " +
				"where [考场安排主表编号]='"+qzqm+"' and 考试形式='4' " +
				"order by cast(substring(时间序列,0,CHARINDEX('#',时间序列)) as int),cast(substring(时间序列,CHARINDEX('#',时间序列)+1,LEN(时间序列)) as int),[场地要求],[试卷类型] ";	
		Vector vecbksj=db.GetContextVector(sqlsj);
			
		String sqlbs="select [课程名称],[学生人数],[试卷类型] as 考试形式,[行政班名称],[场地要求],[时间序列] from dbo.V_考试管理_考场安排明细表  " +
				"where [考场安排主表编号]='"+qzqm+"' and 考试形式='3' " +
				"order by cast(substring(时间序列,0,CHARINDEX('#',时间序列)) as int),cast(substring(时间序列,CHARINDEX('#',时间序列)+1,LEN(时间序列)) as int),[场地要求],[试卷类型] ";	
		Vector vecbspx=db.GetContextVector(sqlbs);
			
//		for(int i=0;i<vecbspx.size();i=i+6){
//			System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		}
		
		//--------------------------------------------------------------------
			
		//生成excel表
		if (vect != null && vect.size() > 0) {
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);
			savePath = MyTools.getProp(request, "Base.exportExcelPath");	
			
			//创建
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			savePath +=  "/" + xn + "补考考试安排表.xls";
			
			try {
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				WritableSheet wsheet1 = wbook.createSheet("考试安排表", 0);// 工作表名称
				WritableSheet wsheet2 = wbook.createSheet("监考签到表", 1);// 工作表名称
				WritableSheet wsheet3 = wbook.createSheet("试卷标签", 2);// 工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;

				//考试安排表 1
				//生成标题
				String[] title1=new String[]{"序号","课程名称","人数","考核形式","专业","地点","考试时间"};
				int counum1=0;//excel表中行数
				String cellContent1 = ""; //当前单元格的内容
				
				//设置列宽
				wsheet1.setColumnView(0, 5);
				wsheet1.setColumnView(1, 37);
				wsheet1.setColumnView(2, 5);
				wsheet1.setColumnView(3, 8);
				wsheet1.setColumnView(4, 37);
				wsheet1.setColumnView(5, 8);
				wsheet1.setColumnView(6, 32);
				
				
				//第1行
				//设置课表标题行列字体大小
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet1.mergeCells(0, counum1, 6, counum1);
				cellContent1 = xn+"学年度第"+this.getGG_XNXQBM().substring(4,5)+"学期补考考试安排表";
				content = new Label(0, counum1, cellContent1, contentStyle);
				wsheet1.addCell(content);
				wsheet1.setRowView(counum1, 760);	
				
				//第2行
				counum1++;
				wsheet1.mergeCells(0, counum1, 6, counum1);
				cellContent1="注：补考的同学必须带好校园一卡通（若一卡通遗失，必须带学生证、身份证（或住宿证、社保卡）二证）参加考试，不带证件者将取消补考资格。";
				content = new Label(0, counum1, cellContent1, contentStyle);
				wsheet1.addCell(content);
				wsheet1.setRowView(counum1, 760);

				//第3行
				counum1++;
				for(int colNum=0; colNum<7; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setShrinkToFit(true);
					//contentStyle.setWrap(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==6){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}								
					cellContent1=title1[colNum];						
					content = new Label(colNum, counum1, cellContent1, contentStyle);
					wsheet1.addCell(content);	
				}
				wsheet1.setRowView(counum1, 500);
				
				
				//第4行
				counum1++;	
				
//				for(int i=0;i<vecz5.size();i=i+6){
//					System.out.println("sj:---"+vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+5));
//				}
				
				int satline=0;
				int endline=0;			
			
				//vecz5 周五上机
				satline=counum1;
				endline=counum1+vecbksj.size()/6-1;
				//wsheet1.mergeCells(3, satline, 3, endline);
				//wsheet1.mergeCells(5, satline, 5, endline);
				wsheet1.mergeCells(6, satline, 6, endline);
				
//				int roomz5=1;
//				for(int i=6;i<vecz5.size();i=i+6){ 
//					if(vecz5.get(i+4-6).toString().equals(vecz5.get(i+4).toString())){
//						
//					}else{
//						roomz5++;
//					}
//				}
//				//vecz5按教室排序
//				Vector vecbksj=new Vector();
//				for(int i=0;i<roomz5;i++){
//					for(int j=0;j<vecz5.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							vecbksj.add(vecz5.get(j).toString());//课程名称 1
//							vecbksj.add(vecz5.get(j+1).toString());//人数 2
//							vecbksj.add(vecz5.get(j+2).toString());//考试形式 3
//							vecbksj.add(vecz5.get(j+3).toString());//行政班简称 4
//							vecbksj.add(vecz5.get(j+4).toString());//教室 5
//							vecbksj.add(vecz5.get(j+5).toString());//时间 6
//						}
//					}
//				}
				
				
//				for(int i=0;i<vecbksj.size();i=i+6){
//					System.out.println("sj:---"+vecbksj.get(i)+" | "+vecbksj.get(i+1)+" | "+vecbksj.get(i+2)+" | "+vecbksj.get(i+3)+" | "+vecbksj.get(i+4)+" | "+vecbksj.get(i+5));
//				}
				
				//计算考试日期
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				String xxksrq="";
				String xxkssjd="";
				for(int m=0;m<ksrqts.length;m++){
					if(m==Integer.parseInt(vecbksj.get(5).toString().split("#")[0])){
						xxksrq=ksrqts[m].trim();
					}
				}
				for(int n=0;n<kssjdts.length;n++){
					if(n==Integer.parseInt(vecbksj.get(5).toString().split("#")[1])){
						xxkssjd=kssjdts[n].trim();
					}
				}
				
				try {
					cal.setTime(format.parse(xxksrq));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				int week=0;
				//考试日期是星期几
				week = cal.get(Calendar.DAY_OF_WEEK)-1;

				String weekday="";
				if(week==1){
					weekday="一";
				}else if(week==2){
					weekday="二";
				}else if(week==3){
					weekday="三";
				}else if(week==4){
					weekday="四";
				}else if(week==5){
					weekday="五";
				}else if(week==6){
					weekday="六";
				}else if(week==0){
					weekday="日";
				}
				
//				int linesat=0;
//				int[] linenum=new int[roomz5];//教室合并数量
//				for(int i=0;i<linenum.length;i++){
//					for(int j=0;j<vecbksj.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							linenum[i]++;
//						}
//					}
//				}
				
				//计算合并单元格数量
				int sjtime=0;
				int sjclas=0;
				int sjksxs=0;
				int timesj=0;
				int classj=0;
				int ksxssj=0;
				int sjbs=1;
				int sjjs=1;
				int sjks=1;
				Vector vecsjtime=new Vector();
				Vector vecsjclas=new Vector();
				Vector vecsjksxs=new Vector();
				for(int i=6;i<vecbksj.size();i=i+6){ 
					//考试日期
					if(vecbksj.get(i+5-6).toString().equals(vecbksj.get(i+5).toString())){
						sjbs++;
						//教室
						if(vecbksj.get(i+4-6).toString().equals(vecbksj.get(i+4).toString())){
							sjjs++;
							//考试形式
							if(vecbksj.get(i+2-6).toString().equals(vecbksj.get(i+2).toString())){
								sjks++;
							}else{
								vecsjksxs.add(sjks);
								sjks=1;
							}
						}else{
							vecsjclas.add(sjjs);
							sjjs=1;
							vecsjksxs.add(sjks);
							sjks=1;
						}
					}else{
						vecsjtime.add(sjbs);
						sjbs=1;
						vecsjclas.add(sjjs);
						sjjs=1;
						vecsjksxs.add(sjks);
						sjks=1;
					}
				}
				vecsjtime.add(sjbs);
				sjbs=1;
				vecsjclas.add(sjjs);
				sjjs=1;
				vecsjksxs.add(sjks);
				sjks=1;
				
				int hbnum=0;
				for(int i=0;i<vecbksj.size();i=i+6){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent1=vecbksj.get(i+colNum-1).toString();				
							if(i/6==sjksxs){
								wsheet1.mergeCells(3, sjksxs+satline, 3, sjksxs+satline+Integer.parseInt(vecsjksxs.get(ksxssj).toString())-1);
								sjksxs=sjksxs+Integer.parseInt(vecsjksxs.get(ksxssj).toString());
								ksxssj++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent1="";//地点
							if(i/6==sjclas){
								wsheet1.mergeCells(5, sjclas+satline, 5, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								sjclas=sjclas+Integer.parseInt(vecsjclas.get(classj).toString());
								classj++;
							}
						}else if(colNum==6){ 
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent1=name1+xxkssjd+"(若有两门或两门以上的补考生，考试时间顺延，每门考试时间为2小时。)";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecbksj.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+=","+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecbksj.get(i+colNum-1).toString();
							}	
						}
						
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);	
						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
			
				//vecbspx 笔试
				
//				for(int i=0;i<vecbspx.size();i=i+6){
//					System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		   		}
				
				satline=counum1;
				endline=counum1+vecbspx.size()/6-1;
				//计算合并单元格数量
				int linetime=0;
				int lineclas=0;
				int lineksxs=0;
				int timenum=0;
				int clasnum=0;
				int ksxsnum=0;
				int numbs=1;
				int numjs=1;
				int numks=1;
				Vector vectime=new Vector();
				Vector vecclas=new Vector();
				Vector vecksxs=new Vector();
				
				for(int i=6;i<vecbspx.size();i=i+6){ 
					//考试日期
					if(vecbspx.get(i+5-6).toString().equals(vecbspx.get(i+5).toString())){
						numbs++;
						//教室
						if(vecbspx.get(i+4-6).toString().equals(vecbspx.get(i+4).toString())){
							numjs++;
							//考试形式
							if(vecbspx.get(i+2-6).toString().equals(vecbspx.get(i+2).toString())){
								numks++;
							}else{
								vecksxs.add(numks);
								numks=1;
							}
						}else{
							vecclas.add(numjs);
							numjs=1;
							vecksxs.add(numks);
							numks=1;
						}
					}else{
						vectime.add(numbs);
						numbs=1;
						vecclas.add(numjs);
						numjs=1;
						vecksxs.add(numks);
						numks=1;
					}
				}
				vectime.add(numbs);
				numbs=1;
				vecclas.add(numjs);
				numjs=1;
				vecksxs.add(numks);
				numks=1;
	

				//生成excel
				for(int i=0;i<vecbspx.size();i=i+6){ 
					//计算考试日期
					//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					//Calendar cal = Calendar.getInstance();
					xxksrq="";
					xxkssjd="";
					for(int m=0;m<ksrqts.length;m++){
						if(m==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[0])){
							xxksrq=ksrqts[m].trim();
						}
					}
					for(int n=0;n<kssjdts.length;n++){
						if(n==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[1])){
							xxkssjd=kssjdts[n].trim();
						}
					}
					
					try {
						cal.setTime(format.parse(xxksrq));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					week=0;
					//考试日期是星期几
					week = cal.get(Calendar.DAY_OF_WEEK)-1;
					
					weekday="";
					if(week==1){
						weekday="一";
					}else if(week==2){
						weekday="二";
					}else if(week==3){
						weekday="三";
					}else if(week==4){
						weekday="四";
					}else if(week==5){
						weekday="五";
					}else if(week==6){
						weekday="六";
					}else if(week==0){
						weekday="日";
					}
					
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent1=vecbspx.get(i+colNum-1).toString();				
							if(i/6==lineksxs){
								wsheet1.mergeCells(3, lineksxs+satline, 3, lineksxs+satline+Integer.parseInt(vecksxs.get(ksxsnum).toString())-1);
								lineksxs=lineksxs+Integer.parseInt(vecksxs.get(ksxsnum).toString());
								ksxsnum++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent1="";//地点
							if(i/6==lineclas){
								wsheet1.mergeCells(5, lineclas+satline, 5, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								lineclas=lineclas+Integer.parseInt(vecclas.get(clasnum).toString());
								clasnum++;
							}
						}else if(colNum==6){//考试日期
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent1=name1+xxkssjd;
							if(i/6==linetime){
								wsheet1.mergeCells(6, linetime+satline, 6, linetime+satline+Integer.parseInt(vectime.get(timenum).toString())-1);
								linetime=linetime+Integer.parseInt(vectime.get(timenum).toString());
								timenum++;
							}
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecbspx.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecbspx.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}		
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbspx.size()-6||(timenum==vectime.size())){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}	
						}else{
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}					
							
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
								
				//vecty 体育
				if(vecty.size()>0){
					satline=counum1;
					endline=counum1+vecty.size()/7-1;
					wsheet1.mergeCells(3, satline, 6, endline);
				}
				for(int i=0;i<vecty.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){ 
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==3){
							//String name1=xxksrq.split("-")[1]+"月"+xxksrq.split("-")[2]+"日(周"+weekday+") ";							
							cellContent1=tiyutime+" 体育馆";
						}else{
							cellContent1=vecty.get(i+colNum-1).toString();
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecty.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==3){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecty.size()-7||colNum==3){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecty.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}	
	
				//vece 口试
				if(vece.size()>0){
					satline=counum1;
					endline=counum1+vece.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vece.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vece.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vece.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vece.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vece.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vece.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
			
				//vecf 听力
				if(vecf.size()>0){
					satline=counum1;
					endline=counum1+vecf.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vecf.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecf.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecf.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecf.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecf.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecf.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
				//vecg 实训
				if(vecg.size()>0){
					satline=counum1;
					endline=counum1+vecg.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vecg.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecg.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecg.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecg.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecg.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecg.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
								
				//vech 随堂
				if(vech.size()>0){
					satline=counum1;
					endline=counum1+vech.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vech.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vech.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vech.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vech.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vech.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vech.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
				//veci 大作业
				if(veci.size()>0){
					satline=counum1;
					endline=counum1+veci.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<veci.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=veci.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=veci.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==veci.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==veci.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==veci.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
				
				//vecj 空
				if(vecj.size()>0){
					satline=counum1;
					endline=counum1+vecj.size()/7-1;
					wsheet1.mergeCells(3, satline, 3, endline);
					wsheet1.mergeCells(5, satline, 5, endline);
					wsheet1.mergeCells(6, satline, 6, endline);
				}
				for(int i=0;i<vecj.size();i=i+7){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent1=(counum1-2)+"";//序号
						}else if(colNum==3){
							cellContent1="其它";//地点
						}else if(colNum==5){
							cellContent1="";//地点
						}else if(colNum==6){
							cellContent1="学生于"+qtlxtime+"找指定教师完成补考，逾期不考者作缺考处理。";
						}else{
							if(colNum==4){
								cellContent1="";
								String[] bjjc=vecj.get(i+colNum-1).toString().split("、");
								cellContent1=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent1+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent1+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent1+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent1=vecj.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecj.size()-7){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecj.size()-7||colNum==6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecj.size()-7||colNum==3||colNum==5){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						content = new Label(colNum, counum1, cellContent1, contentStyle);
						wsheet1.addCell(content);						
					}
					wsheet1.setRowView(counum1, 500);
					counum1++;	
				}
		
				
				//监考签到表 2------------------------------------------------------------------------
				//生成标题
				String[] title2=new String[]{"序号","课程名称","人数","考核形式","专业","地点","考试时间","监考","监考"};
				int counum2=0;//excel表中行数
				String cellContent2 = ""; //当前单元格的内容
				
				//设置列宽
				wsheet2.setColumnView(0, 5);
				wsheet2.setColumnView(1, 37);
				wsheet2.setColumnView(2, 5);
				wsheet2.setColumnView(3, 8);
				wsheet2.setColumnView(4, 37);
				wsheet2.setColumnView(5, 8);
				wsheet2.setColumnView(6, 32);
				wsheet2.setColumnView(7, 10);
				wsheet2.setColumnView(8, 10);
				
				//第1行
				//设置课表标题行列字体大小
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet2.mergeCells(0, counum2, 8, counum2);
				cellContent2 = (Integer.parseInt(xn.substring(2,4))-2)+"级1-4学期（含"+(Integer.parseInt(xn.substring(2,4))-4)+"、"+(Integer.parseInt(xn.substring(2,4))-3)+"级）大补考监考签到表";
				content = new Label(0, counum2, cellContent2, contentStyle);
				wsheet2.addCell(content);
				wsheet2.setRowView(counum2, 760);	
							
				//第2行
				counum2++;
				for(int colNum=0; colNum<9; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setShrinkToFit(true);
					//contentStyle.setWrap(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==8){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}								
					cellContent2=title2[colNum];						
					content = new Label(colNum, counum2, cellContent2, contentStyle);
					wsheet2.addCell(content);	
				}
				wsheet2.setRowView(counum2, 500);

				
				//第3行
				counum2++;	
				
//				for(int i=0;i<vecz5.size();i=i+6){
//					System.out.println("sj:---"+vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+5));
//				}
				
				satline=0;
				endline=0;			
			
				//vecz5 周五上机
				satline=counum2;
				endline=counum2+vecbksj.size()/6-1;
				//wsheet2.mergeCells(3, satline, 3, endline);
				//wsheet2.mergeCells(5, satline, 5, endline);
				wsheet2.mergeCells(6, satline, 6, endline);
				
				//vecz5排序
//				Vector vecbksj=new Vector();
//				for(int i=0;i<roomz5;i++){
//					for(int j=0;j<vecz5.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							vecbksj.add(vecz5.get(j).toString());//课程名称 1
//							vecbksj.add(vecz5.get(j+1).toString());//人数 2
//							vecbksj.add(vecz5.get(j+2).toString());//考试形式 3
//							vecbksj.add(vecz5.get(j+3).toString());//行政班简称 4
//							vecbksj.add(vecz5.get(j+4).toString());//教室 5
//							vecbksj.add(vecz5.get(j+5).toString());//时间 6
//						}
//					}
//				}
								
//				for(int i=0;i<vecbksj.size();i=i+6){
//					System.out.println("sj:---"+vecbksj.get(i)+" | "+vecbksj.get(i+1)+" | "+vecbksj.get(i+2)+" | "+vecbksj.get(i+3)+" | "+vecbksj.get(i+4)+" | "+vecbksj.get(i+5));
//				}
				
				//计算考试日期
				//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				//Calendar cal = Calendar.getInstance();
				xxksrq="";
				xxkssjd="";
				for(int m=0;m<ksrqts.length;m++){
					if(m==Integer.parseInt(vecbksj.get(5).toString().split("#")[0])){
						xxksrq=ksrqts[m].trim();
					}
				}
				for(int n=0;n<kssjdts.length;n++){
					if(n==Integer.parseInt(vecbksj.get(5).toString().split("#")[1])){
						xxkssjd=kssjdts[n].trim();
					}
				}
				
				try {
					cal.setTime(format.parse(xxksrq));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				week=0;
				//考试日期是星期几
				week = cal.get(Calendar.DAY_OF_WEEK)-1;

				weekday="";
				if(week==1){
					weekday="一";
				}else if(week==2){
					weekday="二";
				}else if(week==3){
					weekday="三";
				}else if(week==4){
					weekday="四";
				}else if(week==5){
					weekday="五";
				}else if(week==6){
					weekday="六";
				}else if(week==0){
					weekday="日";
				}
				
				sjtime=0;
				sjclas=0;
				sjksxs=0;
				timesj=0;
				classj=0;
				ksxssj=0;
				sjbs=1;
				sjjs=1;
				sjks=1;
				
				hbnum=0;
				for(int i=0;i<vecbksj.size();i=i+6){ 
					for(int colNum=0; colNum<9; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent2=(counum2-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent2=vecbksj.get(i+colNum-1).toString();				
							if(i/6==sjksxs){
								wsheet2.mergeCells(3, sjksxs+satline, 3, sjksxs+satline+Integer.parseInt(vecsjksxs.get(ksxssj).toString())-1);
								sjksxs=sjksxs+Integer.parseInt(vecsjksxs.get(ksxssj).toString());
								ksxssj++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent2="";//地点
							if(i/6==sjclas){
								wsheet2.mergeCells(5, sjclas+satline, 5, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								wsheet2.mergeCells(7, sjclas+satline, 7, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								wsheet2.mergeCells(8, sjclas+satline, 8, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								sjclas=sjclas+Integer.parseInt(vecsjclas.get(classj).toString());
								classj++;
							}
						}else if(colNum==6){ 
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent2=name1+xxkssjd+"(若有两门或两门以上的补考生，考试时间顺延，每门考试时间为2小时。)";
						}else if(colNum==7||colNum==8){//监考教师
							cellContent2="";
						}else{
							if(colNum==4){
								cellContent2="";
								String[] bjjc=vecbksj.get(i+colNum-1).toString().split("、");
								cellContent2=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent2+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent2+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent2+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent2=vecbksj.get(i+colNum-1).toString();
							}
						}
						
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==8){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						
						content = new Label(colNum, counum2, cellContent2, contentStyle);
						wsheet2.addCell(content);	
						
					}
					wsheet2.setRowView(counum2, 500);
					counum2++;	
				}
				
				//vecbspx 笔试
				
//				for(int i=0;i<vecbspx.size();i=i+6){
//					System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		   		}
				
				satline=counum2;
				endline=counum2+vecbspx.size()/6-1;
				//计算合并单元格数量
				linetime=0;
				lineclas=0;
				lineksxs=0;
				timenum=0;
				clasnum=0;
				ksxsnum=0;
				numbs=1;
				numjs=1;
				numks=1;

				//生成excel
				for(int i=0;i<vecbspx.size();i=i+6){ 
					//计算考试日期
					//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					//Calendar cal = Calendar.getInstance();
					xxksrq="";
					xxkssjd="";
					for(int m=0;m<ksrqts.length;m++){
						if(m==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[0])){
							xxksrq=ksrqts[m].trim();
						}
					}
					for(int n=0;n<kssjdts.length;n++){
						if(n==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[1])){
							xxkssjd=kssjdts[n].trim();
						}
					}
					
					try {
						cal.setTime(format.parse(xxksrq));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					week=0;
					//考试日期是星期几
					week = cal.get(Calendar.DAY_OF_WEEK)-1;
					
					weekday="";
					if(week==1){
						weekday="一";
					}else if(week==2){
						weekday="二";
					}else if(week==3){
						weekday="三";
					}else if(week==4){
						weekday="四";
					}else if(week==5){
						weekday="五";
					}else if(week==6){
						weekday="六";
					}else if(week==0){
						weekday="日";
					}

					for(int colNum=0; colNum<9; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent2=(counum2-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent2=vecbspx.get(i+colNum-1).toString();				
							if(i/6==lineksxs){
								wsheet2.mergeCells(3, lineksxs+satline, 3, lineksxs+satline+Integer.parseInt(vecksxs.get(ksxsnum).toString())-1);
								lineksxs=lineksxs+Integer.parseInt(vecksxs.get(ksxsnum).toString());
								ksxsnum++;
							}
						}else if(colNum==5){//教室
							//cellContent2=vecbspx.get(i+colNum-1).toString();
							cellContent2="";//地点
							if(i/6==lineclas){
								wsheet2.mergeCells(5, lineclas+satline, 5, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								wsheet2.mergeCells(7, lineclas+satline, 7, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								wsheet2.mergeCells(8, lineclas+satline, 8, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								lineclas=lineclas+Integer.parseInt(vecclas.get(clasnum).toString());
								clasnum++;
							}
						}else if(colNum==6){//考试日期
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent2=name1+xxkssjd;
							if(i/6==linetime){
								wsheet2.mergeCells(6, linetime+satline, 6, linetime+satline+Integer.parseInt(vectime.get(timenum).toString())-1);
								linetime=linetime+Integer.parseInt(vectime.get(timenum).toString());
								timenum++;
							}
						}else if(colNum==7||colNum==8){//监考教师
							cellContent2="";
						}else{
							if(colNum==4){
								cellContent2="";
								String[] bjjc=vecbspx.get(i+colNum-1).toString().split("、");
								cellContent2=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent2+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent2+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent2+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent2=vecbspx.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6||(timenum==vectime.size())){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}	
						}else if(colNum==8){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);							
						}else{
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}					
							
						}
						content = new Label(colNum, counum2, cellContent2, contentStyle);
						wsheet2.addCell(content);						
					}
					wsheet2.setRowView(counum2, 500);
					counum2++;	
				}
			
				//最后一行边框
				cellContent2="";
				for(int colNum=0; colNum<9; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					if(colNum==6){
						contentStyle.setWrap(true);
					}else{
						contentStyle.setShrinkToFit(true);
					}
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				
					content = new Label(colNum, counum2, cellContent2, contentStyle);
					wsheet2.addCell(content);	
				}
				
				
				//试卷标签 3
				//生成标题
				String[] title3=new String[]{"序号","课程名称","人数","考核形式","专业","地点","考试时间"};
				int counum3=0;//excel表中行数
				String cellContent3 = ""; //当前单元格的内容
				
				//设置列宽
				wsheet3.setColumnView(0, 5);
				wsheet3.setColumnView(1, 37);
				wsheet3.setColumnView(2, 5);
				wsheet3.setColumnView(3, 8);
				wsheet3.setColumnView(4, 37);
				wsheet3.setColumnView(5, 8);
				wsheet3.setColumnView(6, 32);
				
				//第1行
				//设置课表标题行列字体大小
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setShrinkToFit(true);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet3.mergeCells(0, counum3, 6, counum3);
				cellContent3 = (Integer.parseInt(xn.substring(2,4))-2)+"级1-4学期（含"+(Integer.parseInt(xn.substring(2,4))-4)+"、"+(Integer.parseInt(xn.substring(2,4))-3)+"级）大补考试卷标签";
				content = new Label(0, counum3, cellContent3, contentStyle);
				wsheet3.addCell(content);
				wsheet3.setRowView(counum3, 760);	
				
				//第2行
				counum3++;
				for(int colNum=0; colNum<7; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setShrinkToFit(true);
					//contentStyle.setWrap(true);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					//边框
					if(colNum==0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(colNum==6){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}								
					cellContent3=title3[colNum];						
					content = new Label(colNum, counum3, cellContent3, contentStyle);
					wsheet3.addCell(content);	
				}
				wsheet3.setRowView(counum3, 500);

				
				//第3行
				counum3++;	
				
//				for(int i=0;i<vecz5.size();i=i+6){
//					System.out.println("sj:---"+vecz5.get(i)+" | "+vecz5.get(i+1)+" | "+vecz5.get(i+2)+" | "+vecz5.get(i+3)+" | "+vecz5.get(i+4)+" | "+vecz5.get(i+5));
//				}
				
				satline=0;
				endline=0;			
			
				//vecz5 周五上机
				satline=counum3;
				endline=counum3+vecbksj.size()/6-1;
				//wsheet3.mergeCells(3, satline, 3, endline);
				//wsheet3.mergeCells(5, satline, 5, endline);
				wsheet3.mergeCells(6, satline, 6, endline);
				
				//vecz5排序
//				Vector vecbksj=new Vector();
//				for(int i=0;i<roomz5;i++){
//					for(int j=0;j<vecz5.size();j=j+6){ 
//						if(vecz5.get(j+4).toString().equals((i+1)+"")){
//							vecbksj.add(vecz5.get(j).toString());//课程名称 1
//							vecbksj.add(vecz5.get(j+1).toString());//人数 2
//							vecbksj.add(vecz5.get(j+2).toString());//考试形式 3
//							vecbksj.add(vecz5.get(j+3).toString());//行政班简称 4
//							vecbksj.add(vecz5.get(j+4).toString());//教室 5
//							vecbksj.add(vecz5.get(j+5).toString());//时间 6
//						}
//					}
//				}
				
//				for(int i=0;i<vecbksj.size();i=i+6){
//					System.out.println("sj:---"+vecbksj.get(i)+" | "+vecbksj.get(i+1)+" | "+vecbksj.get(i+2)+" | "+vecbksj.get(i+3)+" | "+vecbksj.get(i+4)+" | "+vecbksj.get(i+5));
//				}
				
				//计算考试日期
				//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				//Calendar cal = Calendar.getInstance();
				xxksrq="";
				xxkssjd="";
				for(int m=0;m<ksrqts.length;m++){
					if(m==Integer.parseInt(vecbksj.get(5).toString().split("#")[0])){
						xxksrq=ksrqts[m].trim();
					}
				}
				for(int n=0;n<kssjdts.length;n++){
					if(n==Integer.parseInt(vecbksj.get(5).toString().split("#")[1])){
						xxkssjd=kssjdts[n].trim();
					}
				}
				
				try {
					cal.setTime(format.parse(xxksrq));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				week=0;
				//考试日期是星期几
				week = cal.get(Calendar.DAY_OF_WEEK)-1;

				weekday="";
				if(week==1){
					weekday="一";
				}else if(week==2){
					weekday="二";
				}else if(week==3){
					weekday="三";
				}else if(week==4){
					weekday="四";
				}else if(week==5){
					weekday="五";
				}else if(week==6){
					weekday="六";
				}else if(week==0){
					weekday="日";
				}
				
				sjtime=0;
				sjclas=0;
				sjksxs=0;
				timesj=0;
				classj=0;
				ksxssj=0;
				sjbs=1;
				sjjs=1;
				sjks=1;
				
				hbnum=0;
				for(int i=0;i<vecbksj.size();i=i+6){ 
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent3=(counum3-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent3=vecbksj.get(i+colNum-1).toString();				
							if(i/6==sjksxs){
								wsheet3.mergeCells(3, sjksxs+satline, 3, sjksxs+satline+Integer.parseInt(vecsjksxs.get(ksxssj).toString())-1);
								sjksxs=sjksxs+Integer.parseInt(vecsjksxs.get(ksxssj).toString());
								ksxssj++;
							}
						}else if(colNum==5){//教室
							//cellContent1=vecbspx.get(i+colNum-1).toString();
							cellContent3="";//地点
							if(i/6==sjclas){
								wsheet3.mergeCells(5, sjclas+satline, 5, sjclas+satline+Integer.parseInt(vecsjclas.get(classj).toString())-1);
								sjclas=sjclas+Integer.parseInt(vecsjclas.get(classj).toString());
								classj++;
							}
						}else if(colNum==6){ 
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent3=name1+xxkssjd+"(若有两门或两门以上的补考生，考试时间顺延，每门考试时间为2小时。)";
						}else{
							if(colNum==4){
								cellContent3="";
								String[] bjjc=vecbksj.get(i+colNum-1).toString().split("、");
								cellContent3=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent3+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent3+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent3+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent3=vecbksj.get(i+colNum-1).toString();
							}
						}
						
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);							
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);						
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbksj.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}
						
						content = new Label(colNum, counum3, cellContent3, contentStyle);
						wsheet3.addCell(content);	
						
					}
					wsheet3.setRowView(counum3, 500);
					counum3++;	
				}
			
				//vecbspx 笔试
				
//				for(int i=0;i<vecbspx.size();i=i+6){
//					System.out.println(vecbspx.get(i)+" | "+vecbspx.get(i+1)+" | "+vecbspx.get(i+2)+" | "+vecbspx.get(i+3)+" | "+vecbspx.get(i+4)+" | "+vecbspx.get(i+5));
//		   		}
				
				satline=counum3;
				endline=counum3+vecbspx.size()/6-1;
				//计算合并单元格数量
				linetime=0;
				lineclas=0;
				lineksxs=0;
				timenum=0;
				clasnum=0;
				ksxsnum=0;
				numbs=1;
				numjs=1;
				numks=1;

				//生成excel
				for(int i=0;i<vecbspx.size();i=i+6){ 
					//计算考试日期
					//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					//Calendar cal = Calendar.getInstance();
					xxksrq="";
					xxkssjd="";
					for(int m=0;m<ksrqts.length;m++){
						if(m==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[0])){
							xxksrq=ksrqts[m].trim();
						}
					}
					for(int n=0;n<kssjdts.length;n++){
						if(n==Integer.parseInt(vecbspx.get(i+5).toString().split("#")[1])){
							xxkssjd=kssjdts[n].trim();
						}
					}
					
					try {
						cal.setTime(format.parse(xxksrq));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					week=0;
					//考试日期是星期几
					week = cal.get(Calendar.DAY_OF_WEEK)-1;
					
					weekday="";
					if(week==1){
						weekday="一";
					}else if(week==2){
						weekday="二";
					}else if(week==3){
						weekday="三";
					}else if(week==4){
						weekday="四";
					}else if(week==5){
						weekday="五";
					}else if(week==6){
						weekday="六";
					}else if(week==0){
						weekday="日";
					}
					
					for(int colNum=0; colNum<7; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						if(colNum==6){
							contentStyle.setWrap(true);
						}else{
							contentStyle.setShrinkToFit(true);
						}
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent3=(counum3-1)+"";//序号
						}else if(colNum==3){//考试形式
							cellContent3=vecbspx.get(i+colNum-1).toString();				
							if(i/6==lineksxs){
								wsheet3.mergeCells(3, lineksxs+satline, 3, lineksxs+satline+Integer.parseInt(vecksxs.get(ksxsnum).toString())-1);
								lineksxs=lineksxs+Integer.parseInt(vecksxs.get(ksxsnum).toString());
								ksxsnum++;
							}
						}else if(colNum==5){//教室
							//cellContent3=vecbspx.get(i+colNum-1).toString();
							cellContent3="";//地点
							if(i/6==lineclas){
								wsheet3.mergeCells(5, lineclas+satline, 5, lineclas+satline+Integer.parseInt(vecclas.get(clasnum).toString())-1);
								lineclas=lineclas+Integer.parseInt(vecclas.get(clasnum).toString());
								clasnum++;
							}
						}else if(colNum==6){//考试日期
							String name1=Integer.parseInt(xxksrq.split("-")[1])+"月"+Integer.parseInt(xxksrq.split("-")[2])+"日(周"+weekday+") ";
							cellContent3=name1+xxkssjd;
							if(i/6==linetime){
								wsheet3.mergeCells(6, linetime+satline, 6, linetime+satline+Integer.parseInt(vectime.get(timenum).toString())-1);
								linetime=linetime+Integer.parseInt(vectime.get(timenum).toString());
								timenum++;
							}
						}else{
							if(colNum==4){
								cellContent3="";
								String[] bjjc=vecbspx.get(i+colNum-1).toString().split("、");
								cellContent3=bjjc[0];
								for(int j=1;j<bjjc.length;j++){
									if(bjjc[j-1].substring(0, bjjc[j-1].length()-1).equalsIgnoreCase(bjjc[j].substring(0, bjjc[j].length()-1))){
										cellContent3+=","+bjjc[j].substring(bjjc[j].length()-1,bjjc[j].length());
									}else{
										if(bjjc[j-1].substring(0, 2).equalsIgnoreCase(bjjc[j].substring(0, 2))){
											cellContent3+="、"+bjjc[j].substring(2,bjjc[j].length());
										}else{
											cellContent3+="、"+bjjc[j];
										}		
									}
								}
							}else{
								cellContent3=vecbspx.get(i+colNum-1).toString();
							}
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}
						}else if(colNum==6){
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vecbspx.size()-6||(timenum==vectime.size())){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}	
						}else{
							if(i==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							}
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vecbspx.size()-6){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);						
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);						
							}					
							
						}
						content = new Label(colNum, counum3, cellContent3, contentStyle);
						wsheet3.addCell(content);						
					}
					wsheet3.setRowView(counum3, 500);
					counum3++;	
				}
				
				//最后一行边框
				cellContent3="";
				for(int colNum=0; colNum<7; colNum++){
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					if(colNum==6){
						contentStyle.setWrap(true);
					}else{
						contentStyle.setShrinkToFit(true);
					}
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				
					content = new Label(colNum, counum3, cellContent3, contentStyle);
					wsheet3.addCell(content);	
				}

				
				//-------------------------------------------------------------------------
							
				
				// 写入数据
				wbook.write();
				// 关闭文件
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
			} else {
				this.setMSG("没有符合条件的成绩信息");
			}
			return savePath;
	}
	
	/**
	 * 读取查询方式下拉框
	 * @date:2017-12-07
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector CXFSCombobx() throws SQLException{
		Vector vec = null;
		String sql = " select 'jscx' AS comboValue,'教师查询' AS comboName " + 
					 " union " + 
					 " select 'bjcx' AS comboValue,'班级查询' AS comboName ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	
	//GET && SET 方法
	public String getXNXQ() {
		return XNXQ;
	}

	public String getGG_BH() {
		return GG_BH;
	}

	public void setGG_BH(String gG_BH) {
		GG_BH = gG_BH;
	}

	public String getGG_LX() {
		return GG_LX;
	}

	public void setGG_LX(String gG_LX) {
		GG_LX = gG_LX;
	}

	public String getGG_XNXQBM() {
		return GG_XNXQBM;
	}

	public void setGG_XNXQBM(String gG_XNXQBM) {
		GG_XNXQBM = gG_XNXQBM;
	}

	public String getGG_XZBDM() {
		return GG_XZBDM;
	}

	public void setGG_XZBDM(String gG_XZBDM) {
		GG_XZBDM = gG_XZBDM;
	}

	public String getGG_SJXL() {
		return GG_SJXL;
	}

	public void setGG_SJXL(String gG_SJXL) {
		GG_SJXL = gG_SJXL;
	}

	public String getGG_SKJHMXBH() {
		return GG_SKJHMXBH;
	}

	public void setGG_SKJHMXBH(String gG_SKJHMXBH) {
		GG_SKJHMXBH = gG_SKJHMXBH;
	}

	public String getGG_LJXGBH() {
		return GG_LJXGBH;
	}

	public void setGG_LJXGBH(String gG_LJXGBH) {
		GG_LJXGBH = gG_LJXGBH;
	}

	public String getGG_ZT() {
		return GG_ZT;
	}

	public void setGG_ZT(String gG_ZT) {
		GG_ZT = gG_ZT;
	}

	public void setXNXQ(String xNXQ) {
		XNXQ = xNXQ;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getXZBDM() {
		return XZBDM;
	}

	public void setXZBDM(String xZBDM) {
		XZBDM = xZBDM;
	}

	public String getKCJS() {
		return KCJS;
	}

	public void setKCJS(String kCJS) {
		KCJS = kCJS;
	}

	public String getJXXZ() {
		return JXXZ;
	}

	public void setJXXZ(String jXXZ) {
		JXXZ = jXXZ;
	}

	public String getSKJHMXBH() {
		return SKJHMXBH;
	}

	public void setSKJHMXBH(String sKJHMXBH) {
		SKJHMXBH = sKJHMXBH;
	}

	public String getQCXX() {
		return QCXX;
	}

	public void setQCXX(String qCXX) {
		QCXX = qCXX;
	}

	public String getLCS() {
		return LCS;
	}

	public void setLCS(String lCS) {
		LCS = lCS;
	}

	public String getAOD() {
		return AOD;
	}

	public void setAOD(String aOD) {
		AOD = aOD;
	}

	public String getiUSERCODE() {
		return iUSERCODE;
	}

	public void setiUSERCODE(String iUSERCODE) {
		this.iUSERCODE = iUSERCODE;
	}

	public String getMSG2() {
		return MSG2;
	}

	public void setMSG2(String mSG2) {
		MSG2 = mSG2;
	}

	public String getMSG3() {
		return MSG3;
	}

	public void setMSG3(String mSG3) {
		MSG3 = mSG3;
	}

	public String getQZQM() {
		return QZQM;
	}

	public void setQZQM(String qZQM) {
		QZQM = qZQM;
	}

	public String getKCAPZBBH() {
		return KCAPZBBH;
	}

	public void setKCAPZBBH(String kCAPZBBH) {
		KCAPZBBH = kCAPZBBH;
	}

	public String getKCAPMXBH() {
		return KCAPMXBH;
	}

	public void setKCAPMXBH(String kCAPMXBH) {
		KCAPMXBH = kCAPMXBH;
	}

	public String getTATOL() {
		return TATOL;
	}

	public void setTATOL(String tATOL) {
		TATOL = tATOL;
	}

	public String getEx_xnxq() {
		return ex_xnxq;
	}

	public void setEx_xnxq(String ex_xnxq) {
		this.ex_xnxq = ex_xnxq;
	}

	public String getEx_jxxz() {
		return ex_jxxz;
	}

	public void setEx_jxxz(String ex_jxxz) {
		this.ex_jxxz = ex_jxxz;
	}

	public String getEx_ksmc() {
		return ex_ksmc;
	}

	public void setEx_ksmc(String ex_ksmc) {
		this.ex_ksmc = ex_ksmc;
	}

	public String getEx_kslx() {
		return ex_kslx;
	}

	public void setEx_kslx(String ex_kslx) {
		this.ex_kslx = ex_kslx;
	}

	public String getEx_jzzs() {
		return ex_jzzs;
	}

	public void setEx_jzzs(String ex_jzzs) {
		this.ex_jzzs = ex_jzzs;
	}

	public String getEx_kszbbh() {
		return ex_kszbbh;
	}

	public void setEx_kszbbh(String ex_kszbbh) {
		this.ex_kszbbh = ex_kszbbh;
	}

	public String getEx_ksrq() {
		return ex_ksrq;
	}

	public void setEx_ksrq(String ex_ksrq) {
		this.ex_ksrq = ex_ksrq;
	}

	public String getKSRQBH() {
		return KSRQBH;
	}

	public void setKSRQBH(String kSRQBH) {
		KSRQBH = kSRQBH;
	}
	public String getAuth() {
		return Auth;
	}

	public void setAuth(String auth) {
		Auth = auth;
	}
	
	
	
	
	
}
