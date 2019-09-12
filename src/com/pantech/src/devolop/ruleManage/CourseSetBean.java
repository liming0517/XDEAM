package com.pantech.src.devolop.ruleManage;
/*
@date 2016.01.08
@author yeq
模块：M1.9班级设置
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class CourseSetBean {
	private String USERCODE;//用户编号
	private String KCDM;//课程代码
	private String KCMC;//课程名称
	private String KCLX;//课程类型
	private String ZYDM;//专业代码
	private String ZYMC;//专业名称
	private String XBBH;//系部代码
	private String XBMC;//系部名称
	private String KSXS;//考试形式
	private String XNXQBM;//学年学期编码
	private String XNXQMC;//学年学期名称
	private String KSXKSJ;//开始选课日期
	private String JSXKSJ;//结束选课日期
	private String KSXKXS;//开始选课时间
	private String JSXKXS;//结束选课时间
	private String KMBH;//科目编号
	
	private String XX_KCDM;//课程代码
	private String XX_KCMC;//课程名称
	private String XX_KCLX;//课程类型
	private String XX_XNXQ;//学年学期
	private String XX_BJRS;//班级人数
	private String XX_BJMC;//班级人数
	private String XX_SKRQ;//上课日期
	private String XX_SKSJ;//上课时间
	private String XX_XUEF;//学分
	private String XX_ZOKS;//总课时
	private String XX_JSBH;//教师编号
	private String XX_SKJS;//教师姓名
	private String XX_CDYQ;//场地要求
	private String XX_CDMC;//场地名称
	private String XX_SKZC;//授课周次
	private String XX_SKZCXQ;//授课周次
	private String XX_ZYBH;//可报名专业编号
	private String XX_ZYNA;//可报名专业名称
	private String XX_XXKZBBH="";//选修课主表编号
	private String XX_XXKMXBH="";//选修课明细编号
	private String XX_SJXL="";//时间序列
	private String XX_KSXS="";//考试形式
	private String XX_WEEK="";//学期总周数
	private String first_APP="";//第一志愿
	private String second_APP="";//第二志愿
	private String third_APP="";//第三志愿
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	private String MSG2;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public CourseSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-15
	 * @author:yeq
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加每周天数,上午节数,下午节数,晚上节数以及节次时间设置基础信息
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		KCDM = "";//课程代码
		KCMC = "";//课程名称
		KCLX = "";//课程类型
		ZYDM = "";//专业代码
		ZYMC = "";//专业名称
		XX_KSXS="";//考试形式

		MSG = "";    //提示信息
	}
	
	/**
	 * 分页查询 班级列表
	 * @date:2016-01-08
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @param BJBH_CX 行政班代码
	 * @param BJMC_CX 行政班名称
	 * @param NJDM_CX 年级代码
	 * @param SSZY_CX 所属专业
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String KCDM_CX, String KCMC_CX, String ZYDM_CX, String KCLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		System.out.println("CourseSetBean-------------------------------------------------------");
//		sql = "SELECT [课程号] as KCDM,[课程名称] as KCMC," +
//			"case when charindex('_',a.课程号)>0 then SUBSTRING(a.课程号,1,8) when charindex('_',a.课程号)=0 then SUBSTRING(a.课程号,1,6) else '' end as ZYDM," +
//			"case when charindex('_',a.课程号)>0 then (select 专业名称 from dbo.V_专业基本信息数据子类 where 专业代码=SUBSTRING(a.课程号,1,8)) when charindex('_',a.课程号)=0 then (select 专业名称 from dbo.V_专业基本信息数据子类 where 专业代码=SUBSTRING(a.课程号,1,6)) else '' end as ZYMC " +
//			"FROM [dbo].[V_课程数据子类] a  where LEN(课程号)=10"+
//			" union all select 课程代码 as KCDM,课程名称 as KCMC,'900000' as ZYDM,'' as ZYMC from dbo.V_基础信息_选修课程信息表 ";
//		sql2=" select b.KCDM,b.KCMC,b.ZYDM,b.ZYMC from ("+sql+") b where 1=1 "; 
		
		sql2="select b.KCDM,b.KCMC,b.ZYDM,b.ZYMC,b.KCLX,b.KCLXMC,b.XBDM,b.XBMC from ( " +
			"SELECT a.课程号 as KCDM,a.课程名称 as KCMC,c.专业代码 as ZYDM,c.专业名称 as ZYMC,a.课程类型 as KCLX,case when a.课程类型='01' then '公共课' else '专业课' end as KCLXMC,a.系部代码 as XBDM,a.系部名称 as XBMC " +
			"FROM [dbo].[V_课程数据子类] a left join dbo.V_专业基本信息数据子类 c on a.专业代码=c.专业代码  " +
			"union all select 课程代码 as KCDM,课程名称 as KCMC,'' as ZYDM,'' as ZYMC,'03' as KCLX,'选修课' as KCLXMC,系部代码 as XBDM,系部名称 as XBMC from dbo.V_基础信息_选修课程信息表  " +
			") b where 1=1 ";
		
		if(!"".equalsIgnoreCase(KCDM_CX)){
			sql2 += " and b.KCDM like '%" + MyTools.fixSql(KCDM_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql2 += " and b.KCMC like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(ZYDM_CX)){
			sql2 += " and b.ZYDM='" + MyTools.fixSql(ZYDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(KCLX_CX)){
			sql2 += " and b.KCLX='" + MyTools.fixSql(KCLX_CX) + "'";
		}
		vec = db.getConttexJONSArr(sql2, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取年级下拉框
	 * @date:2016-01-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKCLXCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select '公共课' as comboName,'01' as comboValue " +
				"union all " +
				"select '专业课' as comboName,'02' as comboValue " +
				"union all " +
				"select '选修课' as comboName,'03' as comboValue " ;

		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2016-01-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 专业名称+专业代码 as comboName,专业代码 as comboValue " +
				"from V_专业基本信息数据子类  ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-09-05
	 * @author:lupengfe
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXBDMCombo() throws SQLException{
		Vector vec = null;
		String allXBDM="";
		String allXBMC="";
		
		String sql2="select distinct 系部代码,系部名称  from V_基础信息_系部信息表 where 系部代码!='C00' order by 系部代码 ";
		Vector vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			for(int i=0;i<vec2.size();i=i+2){
				allXBDM+=vec2.get(i).toString()+",";
				allXBMC+=vec2.get(i+1).toString()+",";
			}
			if(!allXBDM.equals("")){
				allXBDM=allXBDM.substring(0, allXBDM.length()-1);
			}
			if(!allXBMC.equals("")){
				allXBMC=allXBMC.substring(0, allXBMC.length()-1);
			}
			this.setMSG(vec2.size()/2+"");
		}
		
		
		
		String sql ="select '全部' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 系部名称 as comboName,系部代码 as comboValue " +
				"from V_基础信息_系部信息表 where 系部代码!='C00' ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	
	/**
	 * 读取考试形式下拉框
	 * @date:2016-02-04
	 * @author:lpengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKSXSCombo() throws SQLException{
		Vector vec = null;
		String sql = "SELECT  '-1' AS comboValue,'请选择' AS comboName FROM V_考试形式 union " +
					 "SELECT 编号 AS comboValue,case when 考试形式='' then '未选考试形式' else 考试形式 end AS comboName FROM V_考试形式 ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取大补考考试形式下拉框
	 * @date:2016-11-21
	 * @author:lpengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadDBKKSXSCombo() throws SQLException{
		Vector vec = null;
		String sql = "SELECT  '-1' AS comboValue,'请选择' AS comboName FROM V_考试形式大补考 union " +
					 "SELECT 编号 AS comboValue,case when 考试形式='' then '未选考试形式' else 考试形式 end AS comboName FROM V_考试形式大补考  ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 新建方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void addRec() throws SQLException {		 
		String sql = "";
		String sql2= "";
		Vector vec=null;
		Vector vec2=null;
		String maxMxId="";
		String zydm="";
		
		if(this.getKCLX().equals("03")){
			sql = "select count(*) from dbo.V_基础信息_选修课程信息表  where 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' ";
		}else{
			sql = "select count(*) from dbo.V_课程数据子类 where 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' ";
		}
		vec=db.GetContextVector(sql);
		
		//判断数据是否存在
		if(!vec.get(0).toString().equals("0")){
			this.setMSG("当前填写的课程名称已存在");
		}else{
			if(this.getKCLX().equals("01")){
				sql2="select max(cast(SUBSTRING([课程号],9,10) as bigint)) from V_课程数据子类 ";
				zydm="00000000";
				sql2+=" where 课程号 like '"+zydm+"%'";
			}else if(this.getKCLX().equals("02")){
				sql2="select max(cast(SUBSTRING([课程号],9,10) as bigint)) from V_课程数据子类 ";
				if(this.getZYDM().indexOf("_")>-1){
					zydm=this.getZYDM();
				}else{
					zydm=this.getZYDM()+"00";
				}
				sql2+=" where 课程号 like '"+zydm+"%'";
			}else if(this.getKCLX().equals("03")){
				sql2="select max(cast(SUBSTRING([课程代码],9,10) as bigint)) from V_基础信息_选修课程信息表 ";
				zydm="90000000";
			}
			
			vec2 = db.GetContextVector(sql2);
			if (vec2.size() > 0) {
				if(vec2.get(0).equals("")){
					this.setKCDM(zydm+"01");//设置授课计划明细主键
				}else{
					maxMxId = String.format("%02d", (Long.parseLong(vec2.get(0).toString())+1));
					this.setKCDM(zydm+maxMxId);//设置授课计划明细主键
				}
			}
			
			if(this.getKCLX().equals("03")){
				sql = "insert into V_基础信息_选修课程信息表 (课程代码,课程名称,系部代码,系部名称,专业代码,考试形式) values (" +
						"'" + MyTools.fixSql(this.getKCDM()) + "'," +
						"'" + MyTools.fixSql(this.getKCMC()) + "'," +
						"'" + MyTools.fixSql(this.getXBBH()) + "'," +
						"'" + MyTools.fixSql(this.getXBMC()) + "'," +
						"'" + MyTools.fixSql(this.getZYDM()) + "'," +
						"'" + MyTools.fixSql(this.getKSXS()) + "')" ;
			}else{
				if(this.getKCLX().equals("01")){
					zydm="000000";
				}else{
					zydm=this.getZYDM();
				}
				sql = "insert into V_课程数据子类 (课程号,课程名称,课程类型,系部代码,系部名称,专业代码,考试形式) values (" +
						"'" + MyTools.fixSql(this.getKCDM()) + "'," +
						"'" + MyTools.fixSql(this.getKCMC()) + "'," +
						"'" + MyTools.fixSql(this.getKCLX()) + "'," +
						"'" + MyTools.fixSql(this.getXBBH()) + "'," +
						"'" + MyTools.fixSql(this.getXBMC()) + "'," +
						"'" + MyTools.fixSql(zydm) + "'," +
						"'" + MyTools.fixSql(this.getKSXS()) + "')" ;
			}
			
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 编辑方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void modRec(String kcbh,String oldKCMC) throws SQLException {		 
		String sql="";
		String sql2="";
		String sql3="";
		Vector vec=new Vector();
		Vector vec2=null;
		Vector vec3=null;
		String zydm="";
		
		if(this.getKCLX().equals("03")){
			sql2 = "select count(*) from dbo.V_基础信息_选修课程信息表  where 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' and 课程代码!='" + MyTools.fixSql(kcbh) + "' ";
		}else{
			sql2 = "select count(*) from dbo.V_课程数据子类 where 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' and 课程号!='" + MyTools.fixSql(kcbh) + "' ";
		}
		vec2=db.GetContextVector(sql2);
		
		//判断数据是否存在
		if(!vec2.get(0).toString().equals("0")){
			this.setMSG("当前填写的课程名称已存在");
		}else{
			//sql2="select count(*) from V_规则管理_选修课授课计划明细表 where [授课计划主表编号] in (select [授课计划主表编号] from [V_规则管理_选修课授课计划主表] where 课程代码='"+MyTools.fixSql(kcbh)+"') ";
			//vec2=db.GetContextVector(sql2);
			
			//sql3="select count(*) from V_规则管理_授课计划明细表 where 课程代码='"+MyTools.fixSql(kcbh)+"' ";
			//vec3=db.GetContextVector(sql3);
			
			//if(!vec2.get(0).toString().equals("0")||!vec3.get(0).toString().equals("0")){
			//	this.setMSG("该课程已设置授课计划，无法修改");
			//}else{
				if(this.getKCLX().equals("03")){
					sql = " update V_基础信息_选修课程信息表 set " +
							" 课程名称='" + MyTools.fixSql(this.getKCMC()) + "'," +
							" 系部代码='" + MyTools.fixSql(this.getXBBH()) + "'," +
							" 系部名称='" + MyTools.fixSql(this.getXBMC()) + "'," +
							" 专业代码='" + MyTools.fixSql(this.getZYDM()) + "'," +
							" 考试形式='" + MyTools.fixSql(this.getKSXS()) + "' " +
							" where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					//修改其它有课程名称的表
					sql="update V_规则管理_选修课授课计划主表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					
				}else{
					if(this.getKCLX().equals("01")){
						zydm="000000";
					}else{
						zydm=this.getZYDM();
					}
					sql = " update V_课程数据子类 set " +
							" 课程名称='" + MyTools.fixSql(this.getKCMC()) + "'," +
							" 课程类型='" + MyTools.fixSql(this.getKCLX()) + "'," +
							" 系部代码='" + MyTools.fixSql(this.getXBBH()) + "'," +
							" 系部名称='" + MyTools.fixSql(this.getXBMC()) + "'," +
							" 专业代码='" + MyTools.fixSql(zydm) + "'," +
							" 考试形式='" + MyTools.fixSql(this.getKSXS()) + "' " +
							" where 课程号='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					//修改其它有课程名称的表
					sql="update V_调课管理_调课信息明细表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程编号='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					sql="update dbo.V_规则管理_分层课程信息表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					sql="update V_规则管理_授课计划明细表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					sql="update V_教室查询_课程表明细详情表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					sql="update V_排课管理_添加课程信息表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程编号='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					sql="update V_排课管理_添加课程信息表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程编号='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					sql="update V_数据对接_课程表明细详情表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					sql="update dbo.V_排课管理_课程表周详情表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
					vec.add(sql);
					
					//V_排课管理_课程表明细详情表  有合并情况
					sql3="select [课程表明细编号],[课程代码],[课程名称] from [V_排课管理_课程表明细详情表] where 课程代码  like '%"+MyTools.fixSql(kcbh)+"%' ";
					vec3=db.GetContextVector(sql3);
					if(vec3!=null&&vec3.size()>0){
						String newkcdm="";
						String newkcmc="";
								
						for(int i=0;i<vec3.size();i=i+3){
							newkcmc="";
							if(vec3.get(i+2).toString().indexOf("｜")>-1){//有合并
								String[] kcdm=vec3.get(i+1).toString().split("｜");
								String[] kcmc=vec3.get(i+2).toString().split("｜");
										
								for(int j=0;j<kcdm.length;j++){
									if(kcmc[j].equals(oldKCMC)){//课程名称相同
										newkcmc+=this.getKCMC()+"｜";
									}else{
										newkcmc+=kcmc[j]+"｜";
									}
								}
								if(!newkcmc.equals("")){
									newkcmc=newkcmc.substring(0, newkcmc.length()-1);
								}
		
								sql="update [V_排课管理_课程表明细详情表] set [课程名称]='"+newkcmc+"' where [课程表明细编号]='"+vec3.get(i).toString()+"' ";
								vec.add(sql);
							}else{
								sql="update [V_排课管理_课程表明细详情表] set [课程名称]='" + MyTools.fixSql(this.getKCMC()) + "' where [课程表明细编号]='"+vec3.get(i).toString()+"' ";
								vec.add(sql);
							}
						}
					}
					
					
				}
				
				//修改其它有课程名称的表
				sql="update V_成绩管理_补考登分教师信息表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
				vec.add(sql);
				
				sql="update V_成绩管理_大补考登分教师信息表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程名称='" + MyTools.fixSql(oldKCMC) + "' ";
				vec.add(sql);
				
				sql="update dbo.V_成绩管理_登分教师信息表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程名称='" + MyTools.fixSql(oldKCMC) + "' ";
				vec.add(sql);
				
				sql="update V_成绩管理_分卷信息主表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程名称='" + MyTools.fixSql(oldKCMC) + "' ";
				vec.add(sql);
				
				sql="update V_成绩管理_科目课程信息表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
				vec.add(sql);
				
				sql="update V_成绩管理_历史留级科目详情表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程名称='" + MyTools.fixSql(oldKCMC) + "' ";
				vec.add(sql);
				
				sql="update V_考试管理_考场安排明细表 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
				vec.add(sql);
				
				sql="update dbo.V_考试管理_考试设置 set 课程名称='" + MyTools.fixSql(this.getKCMC()) + "' where 课程代码='" + MyTools.fixSql(kcbh) + "' ";
				vec.add(sql);
				
				if(db.executeInsertOrUpdateTransaction(vec)){
					this.setMSG("保存成功");
				}else{
					this.setMSG("保存失败");
				}
			//}
				
		}
	}
	
	/**
	 * 检查课程是否被使用
	 * @date:2017-11-02
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void checkCourseUsed() throws SQLException {	
		String sql2="";
		String sql3="";
		Vector vec2=null;
		Vector vec3=null;
		
		sql2="select count(*) from V_规则管理_选修课授课计划明细表 where [授课计划主表编号] in (select [授课计划主表编号] from [V_规则管理_选修课授课计划主表] where 课程代码='"+MyTools.fixSql(this.getKCDM())+"') ";
		vec2=db.GetContextVector(sql2);
		
		sql3="select count(*) from V_规则管理_授课计划明细表 where 课程代码='"+MyTools.fixSql(this.getKCDM())+"' ";
		vec3=db.GetContextVector(sql3);
		
		if(!vec2.get(0).toString().equals("0")||!vec3.get(0).toString().equals("0")){
			this.setMSG("该课程已设置授课计划，无法修改");
		}else{
			this.setMSG("0");
		}
	}
	
	/**
	 * 删除方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void delRec() throws SQLException {		 
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		Vector vec2=null;
		Vector vec3=null;
		
		sql2="select count(*) from V_规则管理_选修课授课计划主表 where 课程代码='"+MyTools.fixSql(this.getKCDM())+"' ";
		vec2=db.GetContextVector(sql2);
		
		sql3="select count(*) from V_规则管理_授课计划明细表 where 课程代码='"+MyTools.fixSql(this.getKCDM())+"' ";
		vec3=db.GetContextVector(sql3);
		
		if(!vec2.get(0).toString().equals("0")||!vec3.get(0).toString().equals("0")){
			this.setMSG("该课程已设置授课计划，无法删除");
		}else{
			if(this.getKCLX().equals("03")){
				sql = "delete from V_基础信息_选修课程信息表  where 课程代码='" + MyTools.fixSql(this.getKCDM()) + "'";
			}else{
				sql = "delete from V_课程数据子类 where 课程号='" + MyTools.fixSql(this.getKCDM()) + "'";
			}
			
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("删除成功");
			}else{
				this.setMSG("删除失败");
			}
		}
	}
	
	/**
	 * 查询选课信息
	 * @date:2016-04-18
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector loadSelection(int pageNum, int page, String iUSERCODE) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec14=null;
		String xnxqbm="";
		
		//查询当前时间是否可以选课
		sql2="select 学年学期编码,convert(varchar(10),开始选课日期,21),convert(varchar(10),结束选课日期,21),开始选课时间,结束选课时间 from [dbo].[V_基础信息_选修课时间表] ";
		vec2=db.GetContextVector(sql2);
		String day="";
		if(vec2!=null&&vec2.size()>0){
			//for(int i=0;i<vec2.size();i=i+5){
				Calendar cal =Calendar.getInstance();
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        //System.out.println(df.format(cal.getTime()));
		        //System.out.println(vec.get(0).toString());
		        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        day=df.format(cal.getTime());
		        try {
					Date dt1 = df.parse(df.format(cal.getTime()));
					Date dt2 = df.parse(vec2.get(1).toString());
					Date dt3 = df.parse(vec2.get(2).toString());
					if (dt1.getTime() >= dt2.getTime() && dt1.getTime() <= dt3.getTime()) {
						 xnxqbm=vec2.get(0).toString();
			        }else{
			        	 
			        } 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//}
		}
		
		//查询志愿信息
		String sqlapp="select [志愿编号],[志愿明细] from [V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [学号]='"+MyTools.fixSql(iUSERCODE)+"' order by [志愿编号] ";
		Vector vecapp=db.GetContextVector(sqlapp);
		if(vecapp!=null&&vecapp.size()>0){
			for(int i=0;i<vecapp.size();i=i+2){
				if(vecapp.get(i).toString().equals("1")){
					this.setFirst_APP(vecapp.get(i+1).toString());
				}else if(vecapp.get(i).toString().equals("2")){
					this.setSecond_APP(vecapp.get(i+1).toString());
				}else if(vecapp.get(i).toString().equals("3")){
					this.setThird_APP(vecapp.get(i+1).toString());
				}else{
					
				}
			}
		}else{
			this.setFirst_APP("");
			this.setSecond_APP("");
			this.setThird_APP("");
		}

		Calendar cal =Calendar.getInstance();
		SimpleDateFormat dfh = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		System.out.println(dfh.format(cal.getTime()));
		try {
			Date dt6 = dfh.parse(dfh.format(cal.getTime()));
			Date dt4 = dfh.parse(day+" "+vec2.get(3).toString());
			Date dt5 = dfh.parse(day+" "+vec2.get(4).toString());
			if (dt6.getTime() >= dt4.getTime() && dt6.getTime() <= dt5.getTime()) { //可以选课
				this.setMSG(xnxqbm);
				//查询学生所在班级，系部
				String sqlbj="select a.行政班代码,b.系部代码  from V_学生基本数据子类 a,V_基础信息_班级信息表 b where a.行政班代码=b.班级编号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"'";
				Vector vecbj=db.GetContextVector(sqlbj);
				if(vecbj!=null&&vecbj.size()>0){
					String xbdm=vecbj.get(1).toString();
					if(!xnxqbm.equals("")){
//						if((Integer.parseInt(vecbj.get(0).toString().substring(0,2))+1)==Integer.parseInt(xnxqbm.substring(2, 4))){//是2年级
							sql = " select b.学年学期编码,b.课程代码,b.课程名称,a.授课计划明细编号,a.授课计划主表编号,a.选修班名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.状态,a.上课时间," +
									" (select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.总人数,a.可报名专业编号 " +
									" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') "; 
//						}else{
							String sql14=" select 学号 from V_选修课_补选学生名单 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' ";
							vec14=db.GetContextVector(sql14);
							int tt=0;
							if(vec14!=null&&vec14.size()>0){
								for(int j=0;j<vec14.size();j++){
									if(iUSERCODE.equals(vec14.get(j).toString())){
										tt=1;
									}
								}
							}
							//20161208 计算机中高职贯通专业一年级的学生也可以选选修课
//							if(zymc.equals("计算机网络技术（中高职贯通）")){
//								tt=1;
//							}
							if(tt==1){
								sql = " select b.学年学期编码,b.课程代码,b.课程名称,a.授课计划明细编号,a.授课计划主表编号,a.选修班名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.状态,a.上课时间," +
										" (select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.总人数,a.可报名专业编号 " +
										" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号  and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') "; 
							}
//						}
					}
				}
	        }else{
	        	this.setMSG("");
	        } 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(sql.equals("")){
			sql = " select b.学年学期编码,b.课程代码,b.课程名称,a.授课计划明细编号,a.选修班名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.状态,a.上课时间," +
					" (select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.总人数,a.可报名专业编号 " +
					  " FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where 1=2"; 
		}
		
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 获取学生详细信息
	 * @author 2016-09-12
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector getStuInfo(String iUSERCODE) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集

		sql = " select 姓名,case when [性别码]='1' then '男' else '女' end as 性别,学号,b.[行政班名称] as 班级,[出生日期] as 出生年月,[身份证件号] as 身份证号,[民族码] as 民族,[政治面貌码] as 政治面貌,[本人手机] as 联系电话,[电子信箱] as 电子邮箱,[家庭住址] from dbo.V_学生基本数据子类 a,dbo.V_学校班级数据子类 b where a.行政班代码=b.行政班代码 and a.[学号]='"+MyTools.fixSql(iUSERCODE)+"' ";
		vec=db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 查询选课信息
	 * @date:2016-08-02
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector loadAdminSelection(int pageNum, int page ,String SC_XNXQ,String xxkskjhmx) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集

		sql = " select b.学年学期编码,b.课程代码,b.课程名称,a.授课计划明细编号,a.授课计划主表编号,a.选修班名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.状态,a.上课时间," +
				" (select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.总人数,a.可报名专业编号 " +
			  " FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' "; 
		if(!SC_XNXQ.equals("")){
			sql+=" and b.学年学期编码='"+MyTools.fixSql(SC_XNXQ)+"' ";
		}	
		if(!xxkskjhmx.equals("")){
			sql+=" and a.授课计划明细编号!='"+MyTools.fixSql(xxkskjhmx)+"' ";
		}
		sql+=" order by b.学年学期编码 desc ";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 查询选课信息
	 * @date:2017-10-27
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector loadGridClassSingle(int pageNum, int page ,String SC_XNXQ,String xh,String xzbdm,String xxkskjhmx) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		String sql3 = ""; 
		String sql4 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		String xbdm="";
		
		sql2="select 系部代码  from V_基础信息_班级信息表 b where 班级编号='"+MyTools.fixSql(xzbdm)+"'";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			xbdm=vec2.get(0).toString();
		}
		
		sql = " select b.学年学期编码,b.课程代码,b.课程名称,a.授课计划明细编号,a.授课计划主表编号,a.选修班名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.状态,a.上课时间," +
				" (select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.总人数,a.可报名专业编号 " +
			  " FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' "; 
		if(!SC_XNXQ.equals("")){
			sql+=" and b.学年学期编码='"+MyTools.fixSql(SC_XNXQ)+"' ";
		}
		if(!xzbdm.equals("")){
			sql+=" and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') ";
		}
		if(!xxkskjhmx.equals("")){
			sql+=" and a.授课计划明细编号!='"+MyTools.fixSql(xxkskjhmx)+"' ";
		}
		
		sql3 = " SELECT a.授课计划明细编号,COUNT(*) as 人数 " +
				"FROM [dbo].[V_规则管理_学生选修课关系表] a " +
				"left join dbo.V_规则管理_选修课授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号 " +
				"left join dbo.V_学生基本数据子类 c on a.学号=c.学号 " +
				"left join dbo.V_基础信息_班级信息表 d on c.行政班代码=d.班级编号 " +
				"where a.学年学期编码='"+MyTools.fixSql(SC_XNXQ)+"' and d.班级编号='"+MyTools.fixSql(xzbdm)+"' " +
				"group by a.授课计划明细编号 " ;
		
		sql4="select 志愿明细,志愿编号 from dbo.V_规则管理_学生选修课志愿表 where 学年学期编码='"+MyTools.fixSql(SC_XNXQ)+"' and 学号='"+MyTools.fixSql(xh)+"'";
		
		String sqlrs="select m.*,n.人数,p.志愿编号  from ("+sql+") m left join ("+sql3+") n on m.授课计划明细编号=n.授课计划明细编号 left join ("+sql4+") p on m.授课计划明细编号=p.志愿明细";
		sqlrs+=" order by m.授课计划明细编号 ";
		
		vec = db.getConttexJONSArr(sqlrs, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 学生选课
	 * @date:2016-04-20
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveSelection(String iKeyCode,String iUSERCODE,String xnxqbm) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		
		sql2="select  (select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.[总人数] from dbo.V_规则管理_选修课授课计划明细表 a where a.授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"'"; 
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			if(vec2.get(0).toString().equals(vec2.get(1).toString())){//报名人数满
				this.setMSG("报名人数已满");
			}else{
				String sql3="select count(*) from dbo.V_规则管理_学生选修课关系表 where 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"' and 学号='"+MyTools.fixSql(iUSERCODE)+"' ";
				if(db.getResultFromDB(sql3)) {
					
				}else {
					sql=" insert into dbo.V_规则管理_学生选修课关系表 ([学年学期编码],[学号],[授课计划明细编号],[创建人],[创建时间],[状态]) values (" +
							"'"+MyTools.fixSql(xnxqbm)+"'," +
							"'"+MyTools.fixSql(iUSERCODE)+"'," +
							"'"+MyTools.fixSql(iKeyCode)+"'," +
							"'"+MyTools.fixSql(iUSERCODE)+"'," +
							"getdate(),'1') ";
					vec.add(sql);
					
					//预选功能，保存到  学生选修课关系表

					//sql += " update dbo.V_规则管理_选修课授课计划明细表 set 报名人数=报名人数+1 where 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"' "; 
					//不需要插入成绩表了 20160721
//					sql += " insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) " +
//							" select a.学号,a.姓名,c.科目编号,b.授课计划明细编号 as 相关编号,'1' as 成绩状态,'post' as 创建人,GETDATE() as 创建时间,'1' as 状态" +
//							" from dbo.V_学生基本数据子类 a,dbo.V_规则管理_学生选修课关系表 b,V_成绩管理_登分教师信息表 c" +
//							" where a.学号=b.学号 and b.授课计划明细编号=c.相关编号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"' and b.授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"'" ;
					if(db.executeInsertOrUpdateTransaction(vec)){
						this.setMSG("报名成功");
					}else{
						this.setMSG("报名失败");
					}
				}
						
			}
		}
	}
	
	/**
	 * 删除学生选课
	 * @date:2016-04-20
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void delSelection(String iKeyCode,String iUSERCODE) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = new Vector(); // 结果集
		Vector vec2 = null; // 结果集
		String xnxqbm="";
		
		//查询当前时间是否可以选课
		sql2="select 学年学期编码,convert(varchar(10),开始选课日期,21),convert(varchar(10),结束选课日期,21) from [dbo].[V_基础信息_选修课时间表] ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
					for(int i=0;i<vec2.size();i=i+3){
						Calendar cal =Calendar.getInstance();
				        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				        //System.out.println(df.format(cal.getTime()));
				        //System.out.println(vec.get(0).toString());
				        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				        try {
							Date dt1 = df.parse(df.format(cal.getTime()));
							Date dt2 = df.parse(vec2.get(i+1).toString());
							Date dt3 = df.parse(vec2.get(i+2).toString());
							 if (dt1.getTime() >= dt2.getTime() && dt1.getTime() <= dt3.getTime()) {
								 xnxqbm=vec2.get(i).toString();
					         }else{
					        	 
					         } 
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		}
		
		
		sql=" delete from dbo.V_规则管理_学生选修课关系表" +
			" where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' " +
			" and 学号= '"+MyTools.fixSql(iUSERCODE)+"' " +
			" and 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"' ";
		vec.add(sql);
//		sql = " update dbo.V_规则管理_选修课授课计划明细表 set 报名人数=报名人数-1 where 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"' "; 
//		vec.add(sql);
		sql = " delete from V_成绩管理_学生成绩信息表  where 学号= '"+MyTools.fixSql(iUSERCODE)+"' and 相关编号='"+MyTools.fixSql(iKeyCode)+"'" ;
		vec.add(sql);	
		
		if(db.executeInsertOrUpdateTransaction(vec)){
				this.setMSG("删除成功");
		}else{
				this.setMSG("删除失败");
		}
	}
	
	/**
	 * 检查是否可以选课
	 * @date:2016-04-20
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void checkSelection(String iKeyCode,String iUSERCODE,String xnxqbm,String skzc,String sksj,String kcdm) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		String sql3 = ""; 
		String sql4 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		
		sql="SELECT COUNT(*) FROM [dbo].[V_规则管理_学生选修课关系表] where 学号='"+MyTools.fixSql(iUSERCODE)+"'"; 
		vec=db.GetContextVector(sql);
		sql2="SELECT COUNT(*) FROM [dbo].[V_规则管理_学生选修课关系表] where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 学号='"+MyTools.fixSql(iUSERCODE)+"'"; 
		vec2=db.GetContextVector(sql2);
		
		if(vec!=null&&vec.size()>0&&vec2!=null&&vec2.size()>0){
			//用智慧树系统，选课数量不作限制 20161110
			if(vec2.get(0).toString().equals("2")){//当前学期选课总数是2
				this.setMSG("0");
			}else if(vec.get(0).toString().equals("3")){//选课总数是3
				this.setMSG("0");
			}else{
				//判断一学期2门课程是否有冲突
				sql3="select 授课周次,上课时间  from dbo.V_规则管理_选修课授课计划明细表 where 授课计划明细编号 in (select [授课计划明细编号] from dbo.V_规则管理_学生选修课关系表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 学号='"+MyTools.fixSql(iUSERCODE)+"') ";
				vec3=db.GetContextVector(sql3);
				if(vec3!=null&&vec3.size()>0){
					for(int k=0;k<vec3.size();k=k+2){
						String skzcn=vec3.get(k).toString();
						String sksjn=vec3.get(k+1).toString();
						if(sksj.equals("")||sksjn.equals("")){

						}else{
							int sametag=0;
							String[] sksj2=sksj.split(",");
							String[] sksjn2=sksjn.split(",");
							for(int m=0;m<sksj2.length;m++){
								for(int n=0;n<sksjn2.length;n++){
									if(sksj2[m].equals(sksjn2[n])){
										sametag=1;
									}
								}
							}
							if(sametag==1){//上课时间有相同
								//比较周次是否有相同
								int tag1=compareweek(skzc,skzcn,"20");
								if(tag1==1){//有重复
									this.setMSG("4");
								}else{
									
								}
							}else{
								
							}
						}
					}
					//主表编号相等
					sql4="select b.课程代码  from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号 in (select [授课计划明细编号] from dbo.V_规则管理_学生选修课关系表 where 学号='"+MyTools.fixSql(iUSERCODE)+"' and 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' ) ";
					vec4=db.GetContextVector(sql4);
					if(vec4!=null&&vec4.size()>0){
						for(int i=0;i<vec4.size();i++){
							if(kcdm.equals(vec4.get(i).toString())){//已选过该课程
								this.setMSG("5");
							}
						}
					}
					
				}
				
			}
		}		
	}
	
	/**
	 * 检查是否可以选课,添加
	 * @date:2016-08-09
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public String checkSelectionAdd(String iKeyCode,String iUSERCODE,String xnxqbm,String skzc,String sksj,String kcdm) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		String sql3 = ""; 
		String sql4 = ""; 
		String sql5 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		Vector vec5 = null; // 结果集
		String message="";
		
		sql="SELECT COUNT(*) FROM [dbo].[V_规则管理_学生选修课关系表] where 学号='"+MyTools.fixSql(iUSERCODE)+"'"; 
		vec=db.GetContextVector(sql);
		sql2="SELECT COUNT(*) FROM [dbo].[V_规则管理_学生选修课关系表] where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 学号='"+MyTools.fixSql(iUSERCODE)+"'"; 
		vec2=db.GetContextVector(sql2);
		//sql3=" select b.专业代码 from dbo.V_学生基本数据子类 a,dbo.V_基础信息_班级信息表 b where a.行政班代码=b.行政班代码 and a.学号='"+MyTools.fixSql(iUSERCODE)+"' and a.[学号] not in ( select 学号 from dbo.V_选修课_补选学生名单 ) "; 
		sql3=" select b.系部代码 from dbo.V_学生基本数据子类 a,dbo.V_基础信息_班级信息表 b where a.行政班代码=b.班级编号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"' and a.[学号] not in ( select 学号 from dbo.V_选修课_补选学生名单 ) "; 
		vec3=db.GetContextVector(sql3);
		
		if(vec!=null&&vec.size()>0&&vec2!=null&&vec2.size()>0){
//			if(vec2.get(0).toString().equals("2")){//当前学期选课总数是2
//				message="2";
//			}else if(vec.get(0).toString().equals("3")){//选课总数是3
//				message="3";
//			}else{
				//判断一学期2门课程是否有冲突
				sql5="select 授课周次,上课时间  from dbo.V_规则管理_选修课授课计划明细表 where 授课计划明细编号 in (select [授课计划明细编号] from dbo.V_规则管理_学生选修课关系表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 学号='"+MyTools.fixSql(iUSERCODE)+"') ";
				vec5=db.GetContextVector(sql5);
				if(vec5!=null&&vec5.size()>0){
					for(int k=0;k<vec5.size();k=k+2){
						String skzcn=vec5.get(k).toString();
						String sksjn=vec5.get(k+1).toString();
						if(sksj.equals("")||sksjn.equals("")){

						}else{
							int sametag=0;
							String[] sksj2=sksj.split(",");
							String[] sksjn2=sksjn.split(",");
							for(int m=0;m<sksj2.length;m++){
								for(int n=0;n<sksjn2.length;n++){
									if(sksj2[m].equals(sksjn2[n])){
										sametag=1;
									}
								}
							}
							if(sametag==1){//上课时间有相同
								//比较周次是否有相同
								int tag1=compareweek(skzc,skzcn,"20");
								if(tag1==1){//有重复
									message="4";
								}else{
									
								}
							}else{
								
							}
						}
					}
					//主表编号相等
					sql4="select b.课程代码  from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号 in (select [授课计划明细编号] from dbo.V_规则管理_学生选修课关系表 where 学号='"+MyTools.fixSql(iUSERCODE)+"' and 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' ) ";
					vec4=db.GetContextVector(sql4);
					if(vec4!=null&&vec4.size()>0){
						for(int i=0;i<vec4.size();i++){
							if(kcdm.equals(vec4.get(i).toString())){//已选过该课程
								message="5";
							}
						}
					}
					
				}
				
			//}
		}	
		if(vec3!=null&&vec3.size()>0){
			if(this.getXX_ZYBH().indexOf(vec3.get(0).toString())>-1||this.getXX_ZYBH().equals("")){//可以报名
				
			}else{
				message="6";
			}
		}
		return message;
	}
	
	/**
	 * 检查是否可以选课,添加
	 * @date:2016-08-09
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public String checkSelectionChange(String xxkskjhmx,String iKeyCode,String iUSERCODE,String xnxqbm,String skzc,String sksj,String kcdm,String yskzc,String ysksj) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		String sql3 = ""; 
		String sql4 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		String message="";
		System.out.println(skzc+"|"+yskzc);
		//判断一学期2门课程是否有冲突
		sql3="select 授课周次,上课时间  from dbo.V_规则管理_选修课授课计划明细表 where 授课计划明细编号 in (select [授课计划明细编号] from dbo.V_规则管理_学生选修课关系表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 学号='"+MyTools.fixSql(iUSERCODE)+"') and [授课计划明细编号]!='"+MyTools.fixSql(xxkskjhmx)+"' and 授课周次!='' ";
		vec3=db.GetContextVector(sql3);
		if(vec3!=null&&vec3.size()>0){
			for(int k=0;k<vec3.size();k=k+2){
				String skzcn=vec3.get(k).toString();
				String sksjn=vec3.get(k+1).toString();
				if(sksj.equals("")||sksjn.equals("")){

				}else{
					int sametag=0;
					String[] sksj2=sksj.split(",");
					String[] sksjn2=sksjn.split(",");
					for(int m=0;m<sksj2.length;m++){
						for(int n=0;n<sksjn2.length;n++){
							if(sksj2[m].equals(sksjn2[n])){
								sametag=1;
							}
						}
					}
					if(sametag==1){//上课时间有相同
						//比较周次是否有相同
						int tag1=compareweek(skzc,skzcn,"20");
						if(tag1==1){//有重复
							//message="4";
						}else{
							
						}
					}else{
						
					}
				}
			}		
		}
		
		//主表编号相等
		sql4="select b.课程代码 ,a.授课计划明细编号  from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号 in (select [授课计划明细编号] from dbo.V_规则管理_学生选修课关系表 where 学号='"+MyTools.fixSql(iUSERCODE)+"' and 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' ) ";
		vec4=db.GetContextVector(sql4);
		if(vec4!=null&&vec4.size()>0){
			for(int i=0;i<vec4.size();i=i+2){
				if(kcdm.equals(vec4.get(i).toString())&&this.getXX_XXKMXBH().equals(vec4.get(i+1).toString())){//已选过该课程
					message="5";
				}
			}
		}
		
		sql2=" select b.系部代码 from dbo.V_学生基本数据子类 a,dbo.V_学校班级数据子类 b where a.行政班代码=b.行政班代码 and a.学号='"+MyTools.fixSql(iUSERCODE)+"' and a.学号 not in ( select 学号 from dbo.V_选修课_补选学生名单 ) "; 
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			if(this.getXX_ZYBH().indexOf(vec2.get(0).toString())>-1||this.getXX_ZYBH().equals("")){//可以报名
				
			}else{
				message="6";
			}
		}
		return message;
	}
	
	/**
	 * 查询选课信息
	 * @date:2016-04-18
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector showSelection(int pageNum, int page, String iUSERCODE) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		String xnxqbm="";
		
		//判断当前时间是否在允许选课时间范围内
		sql2="select 学年学期编码,convert(varchar(10),开始选课日期,21),convert(varchar(10),结束选课日期,21) from [dbo].[V_基础信息_选修课时间表] ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			for(int i=0;i<vec2.size();i=i+3){
				Calendar cal =Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				//System.out.println(df.format(cal.getTime()));
				//System.out.println(vec.get(0).toString());
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
						Date dt1 = df.parse(df.format(cal.getTime()));
						Date dt2 = df.parse(vec2.get(i+1).toString());
						Date dt3 = df.parse(vec2.get(i+2).toString());
						if (dt1.getTime() >= dt2.getTime() && dt1.getTime() <= dt3.getTime()) {
							xnxqbm=vec2.get(i).toString();
					    }else{
					      	 
					    } 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		sql="SELECT a.学年学期编码,a.学号,c.姓名,b.选修班名称,a.授课计划明细编号,'"+xnxqbm+"' as 当前学年学期,b.学分  FROM dbo.V_规则管理_学生选修课关系表 a,dbo.V_规则管理_选修课授课计划明细表 b,dbo.V_学生基本数据子类 c " +
			" where a.授课计划明细编号=b.授课计划明细编号 and a.学号=c.学号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"' ";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 查询选课信息
	 * @date:2016-04-18
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector showSelectionAll(int pageNum, int page,String xxbm, String xh,String xm) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		String xnxqbm="";
			
		sql = " select c.学号,d.姓名,b.学年学期编码,b.课程代码,b.课程名称,a.授课计划明细编号,a.授课计划主表编号,a.选修班名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.上课时间  " +
			  " FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b,dbo.V_规则管理_学生选修课关系表 c,dbo.V_学生基本数据子类 d where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号=c.授课计划明细编号 and c.学号=d.学号 and b.课程类型!='01' ";
		if(!this.getXX_XNXQ().equals("")){
			sql+=" and b.学年学期编码='"+MyTools.fixSql(this.getXX_XNXQ())+"' ";
		}
		if(!xxbm.equals("")){
			sql+=" and a.选修班名称 like '%"+MyTools.fixSql(xxbm)+"%' ";
		}
		if(!xh.equals("")){
			sql+=" and c.学号 like '%"+MyTools.fixSql(xh)+"%' ";
		}
		if(!xm.equals("")){
			sql+=" and d.姓名 like '%"+MyTools.fixSql(xm)+"%' ";
		}
		sql+=" order by c.学号 ";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 查询选课信息
	 * @date:2016-04-18
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void electiveTime() throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql=" select a.学年学期编码,b.学年学期名称,convert(varchar(10),a.开始选课日期,21),convert(varchar(10),a.结束选课日期,21),开始选课时间,结束选课时间 from V_基础信息_选修课时间表 a,V_规则管理_学年学期表 b where a.学年学期编码=b.学年学期编码 ";
		vec = db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			this.setXNXQBM(vec.get(0).toString());
			this.setXNXQMC(vec.get(1).toString());
			this.setKSXKSJ(vec.get(2).toString());
			this.setJSXKSJ(vec.get(3).toString());
			this.setKSXKXS(vec.get(4).toString());
			this.setJSXKXS(vec.get(5).toString());
		}
		
	}
	
	/**
	 * 学生选课
	 * @date:2017-09-14
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveApplication(String iUSERCODE,String term,String first_APP,String second_APP,String third_APP) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		Vector vec = new Vector(); // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		
		sql2="select count(*) from V_规则管理_学生选修课志愿表 where [学年学期编码]='"+MyTools.fixSql(term)+"' and [学号]='"+MyTools.fixSql(iUSERCODE)+"'";
		if(db.getResultFromDB(sql2)){//存在
			sql=" update dbo.V_规则管理_学生选修课志愿表  set [志愿明细]='"+MyTools.fixSql(first_APP)+"',[创建时间]=getdate() "+
					"where [学年学期编码]='"+MyTools.fixSql(term)+"' and [学号]='"+MyTools.fixSql(iUSERCODE)+"' and [志愿编号]='1' ";
			vec.add(sql);
			
			sql=" update dbo.V_规则管理_学生选修课志愿表  set [志愿明细]='"+MyTools.fixSql(second_APP)+"',[创建时间]=getdate() "+
					"where [学年学期编码]='"+MyTools.fixSql(term)+"' and [学号]='"+MyTools.fixSql(iUSERCODE)+"' and [志愿编号]='2' ";
			vec.add(sql);
			
			sql=" update dbo.V_规则管理_学生选修课志愿表  set [志愿明细]='"+MyTools.fixSql(third_APP)+"',[创建时间]=getdate() "+
					"where [学年学期编码]='"+MyTools.fixSql(term)+"' and [学号]='"+MyTools.fixSql(iUSERCODE)+"' and [志愿编号]='3' ";
			vec.add(sql);
		}else{//不存在
			sql=" insert into dbo.V_规则管理_学生选修课志愿表 ([学年学期编码],[学号],[志愿编号],[志愿明细],[创建时间]) values (" +
					"'"+MyTools.fixSql(term)+"'," +
					"'"+MyTools.fixSql(iUSERCODE)+"'," +
					"'1'," +
					"'"+MyTools.fixSql(first_APP)+"'," +
					"getdate()) ";
			vec.add(sql);
			
			sql=" insert into dbo.V_规则管理_学生选修课志愿表 ([学年学期编码],[学号],[志愿编号],[志愿明细],[创建时间]) values (" +
					"'"+MyTools.fixSql(term)+"'," +
					"'"+MyTools.fixSql(iUSERCODE)+"'," +
					"'2'," +
					"'"+MyTools.fixSql(second_APP)+"'," +
					"getdate()) ";
			vec.add(sql);
			
			sql=" insert into dbo.V_规则管理_学生选修课志愿表 ([学年学期编码],[学号],[志愿编号],[志愿明细],[创建时间]) values (" +
					"'"+MyTools.fixSql(term)+"'," +
					"'"+MyTools.fixSql(iUSERCODE)+"'," +
					"'3'," +
					"'"+MyTools.fixSql(third_APP)+"'," +
					"getdate()) ";
			vec.add(sql);
		}
		
		//预排
		sql="delete from dbo.V_规则管理_学生选修课关系表  where [学年学期编码]='"+MyTools.fixSql(term)+"' and [学号]='"+MyTools.fixSql(iUSERCODE)+"'";
		vec.add(sql);
		
		int bmrs1=-1;
		int zrs1=-1;
		int bmrs2=-1;
		int zrs2=-1;
		int bmrs3=-1;
		int zrs3=-1;
		int flag1=0;
		int flag2=0;
		int flag3=-1;
		String zybh="";//志愿编号
		
		sql4="SELECT a.[授课计划明细编号],b.行政班代码,a.[学号],c.志愿编号,c.志愿明细  FROM [dbo].[V_规则管理_学生选修课关系表] a " +
				" inner join dbo.V_学生基本数据子类 b on a.学号=b.学号 " +
				" left join dbo.V_规则管理_学生选修课志愿表 c on a.学号=c.学号  " +
				" where b.行政班代码  in (select 行政班代码 from dbo.V_学生基本数据子类 where 学号='"+MyTools.fixSql(iUSERCODE)+"' ) " +
				" and a.学年学期编码='"+MyTools.fixSql(term)+"' and c.学年学期编码='"+MyTools.fixSql(term)+"' and c.志愿编号='1' " +
				" group by a.[授课计划明细编号],b.行政班代码,a.[学号],c.志愿编号,c.志愿明细 ";
		vec4=db.GetContextVector(sql4);
		
		//第一志愿
		sql2="select count(*) from [V_规则管理_学生选修课关系表] where [授课计划明细编号]='"+MyTools.fixSql(first_APP)+"' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			bmrs1=Integer.parseInt(vec2.get(0).toString());
		}
		
		sql3="select [总人数] from dbo.V_规则管理_选修课授课计划明细表  where [授课计划明细编号]='"+MyTools.fixSql(first_APP)+"' ";
		vec3=db.GetContextVector(sql3);
		if(vec3!=null&&vec3.size()>0){
			zrs1=Integer.parseInt(vec3.get(0).toString());
		}
		
		//报名人数<总人数  
		if(bmrs1<zrs1){
			if(vec4!=null&&vec4.size()>0){
				for(int i=0;i<vec4.size();i=i+5){
					if(vec4.get(i).toString().equals(first_APP)){//有同班同学
						flag1=0;
						//zybh=first_APP;
						break;
					}
					if(vec4.get(i+3).toString().equals("1")&&vec4.get(i+4).toString().equals(first_APP)){//是第一志愿，与自己第一志愿相同
						flag1=1;
					}
				}	
			}else{
				zybh=first_APP;
			}
		}else{
			flag1=2;
		}
		System.out.println("flag1:"+flag1);
		if(flag1==0){
			zybh=first_APP;
		}else{
			//第二志愿
			sql2="select count(*) from [V_规则管理_学生选修课关系表] where [授课计划明细编号]='"+MyTools.fixSql(second_APP)+"' ";
			vec2=db.GetContextVector(sql2);
			if(vec2!=null&&vec2.size()>0){
				bmrs2=Integer.parseInt(vec2.get(0).toString());
			}
			
			sql3="select [总人数] from dbo.V_规则管理_选修课授课计划明细表  where [授课计划明细编号]='"+MyTools.fixSql(second_APP)+"' ";
			vec3=db.GetContextVector(sql3);
			if(vec3!=null&&vec3.size()>0){
				zrs2=Integer.parseInt(vec3.get(0).toString());
			}
			
			if(bmrs2<zrs2){
				if(vec4!=null&&vec4.size()>0){
					for(int i=0;i<vec4.size();i=i+5){
						if(vec4.get(i).toString().equals(second_APP)){//有同班同学
							flag2=0;
							//zybh=second_APP;
							break;
						}
						if(vec4.get(i+3).toString().equals("1")&&vec4.get(i+4).toString().equals(second_APP)){//是第一志愿，与自己第二志愿相同
							flag2=1;
						}
					}	
				}else{
					zybh=second_APP;
				}
			}else{
				flag2=2;
			}
				
			if(flag2==0){
				zybh=second_APP;
			}else{
				//第三志愿
				sql2="select count(*) from [V_规则管理_学生选修课关系表] where [授课计划明细编号]='"+MyTools.fixSql(third_APP)+"' ";
				vec2=db.GetContextVector(sql2);
				if(vec2!=null&&vec2.size()>0){
					bmrs3=Integer.parseInt(vec2.get(0).toString());
				}
				
				sql3="select [总人数] from dbo.V_规则管理_选修课授课计划明细表  where [授课计划明细编号]='"+MyTools.fixSql(third_APP)+"' ";
				vec3=db.GetContextVector(sql3);
				if(vec3!=null&&vec3.size()>0){
					zrs3=Integer.parseInt(vec3.get(0).toString());
				}
				
				if(bmrs3<zrs3){
					if(vec4!=null&&vec4.size()>0){
						for(int i=0;i<vec4.size();i=i+5){
							if(vec4.get(i).toString().equals(third_APP)){//有同班同学
								flag3=0;
								//zybh=third_APP;
								break;
							}
						}	
					}else{
						zybh=third_APP;
					}
				}else{
					flag3=2;
				}
				
				if(flag3==0){
					zybh=third_APP;
				}else{
					
				}
				
			}		
		}
		
		if(zybh.equals("")){//3个志愿都排不进去，按志愿顺序进
			if(bmrs1<zrs1){
				zybh=first_APP;
			}else{
				if(bmrs2<zrs2){
					zybh=second_APP;
				}else{
					if(bmrs3<zrs3){
						zybh=third_APP;
					}else{
						
					}
				}
			}
		}
		
		if(!zybh.equals("")){
			sql = " insert into dbo.V_规则管理_学生选修课关系表 ([学年学期编码],[学号],[授课计划明细编号],[创建人],[创建时间],[状态]) values (" +
					"'"+MyTools.fixSql(term)+"'," +
					"'"+MyTools.fixSql(iUSERCODE)+"'," +
					"'"+MyTools.fixSql(zybh)+"'," +
					"'"+MyTools.fixSql("post")+"'," +
					"getdate(),'1') ";
			vec.add(sql);
		}else{
			//还是排不进去，作为未排学生
		}

		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	
	/**
	 * @author 2016-4-22
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */

	public Vector xnxqCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = " select [学年学期编码] as comboValue, [学年学期名称] as comboName from V_规则管理_学年学期表   order by 学年学期编码 desc ";			
			
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2016-4-22
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */

	public void saveTime() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		String sql2="";
		sql2="SELECT count(*) FROM [dbo].[V_基础信息_选修课时间表] ";
		vector=db.GetContextVector(sql2);
		if(vector.get(0).toString().equals("0")){
			sql = "insert into dbo.V_基础信息_选修课时间表 ([学年学期编码],[开始选课日期],[结束选课日期],[开始选课时间],[结束选课时间]) values " +
					"('"+MyTools.fixSql(this.getXNXQBM())+"'," +
					"'"+MyTools.fixSql(this.getKSXKSJ())+"'," +
					"'"+MyTools.fixSql(this.getJSXKSJ())+"'," +
					"'"+MyTools.fixSql(this.getKSXKXS())+"'," +
					"'"+MyTools.fixSql(this.getJSXKXS())+"') ";
		}else{
			sql = "update dbo.V_基础信息_选修课时间表 set " +
					"[学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"'," +
					"[开始选课日期]='"+MyTools.fixSql(this.getKSXKSJ())+"'," +
					"[结束选课日期]='"+MyTools.fixSql(this.getJSXKSJ())+"'," +
					"[开始选课时间]='"+MyTools.fixSql(this.getKSXKXS())+"'," +
					"[结束选课时间]='"+MyTools.fixSql(this.getJSXKXS())+"' ";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 读取选修课开班信息
	 * @author 2016-7-27
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadElective(int pageNum,int pageSize,String SC_XNXQ,String SC_KCMC) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		sql = " SELECT a.[授课计划明细编号],a.[授课计划主表编号],b.[学年学期编码] as XX_XNXQ,b.[课程代码] as XX_KCDM,b.[课程名称] as XX_KCMC,b.[课程类型] as XX_KCLX,a.[选修班名称],a.[授课教师编号] as XX_JSBH,a.[授课教师姓名] as XX_SKJS,a.[场地要求] as XX_CDYQ,a.[场地名称] as XX_CDMC,a.[授课周次] as XX_SKZC,a.[上课时间] as XX_SJXL," +
				" (select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.[总人数] as XX_BJRS,a.[可报名专业编号] as XX_ZYBH,a.[可报名专业名称] as XX_ZYNA,a.[学分] as XX_XUEF,a.[总课时] as XX_ZOKS,a.[考试形式] as XX_KSXS " +
				" FROM [dbo].[V_规则管理_选修课授课计划明细表] a,dbo.V_规则管理_选修课授课计划主表 b " +
				" where a.授课计划主表编号=b.授课计划主表编号 " ;
			
		if(!SC_XNXQ.equals("")){
			sql+=" and b.学年学期编码 = '"+MyTools.fixSql(SC_XNXQ)+"' ";
		}
		if(!SC_KCMC.equals("")){
			sql+=" and b.课程名称 like '%"+MyTools.fixSql(SC_KCMC)+"%' ";
		}
		sql+=" order by b.[学年学期编码] desc,a.[授课计划明细编号],a.[选修班名称] ";
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	//学年学期combobox
	public Vector XNXQElecCombobox() throws SQLException{
		Vector vec = null;	
		String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName,'2' as px  FROM V_规则管理_学年学期表 where 1=1) e order by e.px,e.comboValue desc ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//课程名称combobox
	public Vector XX_KCDMCombobox() throws SQLException{
		Vector vec = null;	
		String sql = " select '' as comboValue,'请选择' as comboName union select [课程代码] as comboValue,[课程名称] as comboName from [dbo].[V_基础信息_选修课程信息表] where len([课程代码])>7 ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//课程类型combobox
	public Vector XX_KCLXCombobox() throws SQLException{
		Vector vec = null;	
		String sql = " select '' as comboValue,'请选择' as comboName " +    
				  	 " union select '03' as comboValue,'选修课' as comboName " +
				  	 " union select '04' as comboValue,'班级选修课' as comboName " +
				  	 " union select '05' as comboValue,'备用' as comboName ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
		
	//XX学年学期combobox
	public Vector XX_XNXQCombobox() throws SQLException{
		Vector vec = null;	
		String sql2="";
		
		String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName,'2' as px  FROM V_规则管理_学年学期表 where 1=1) e order by e.px,e.comboValue desc ";
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
	public void getWeeknum(String XNXQBM) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String sql3="";
		if(XNXQBM.equalsIgnoreCase("")){
			sql3="select distinct 学年+学期+教学性质 as termid,学年+学期,教学性质 as jxxz FROM V_规则管理_学年学期表 where 1=1 order by termid desc";
			vec3=db.GetContextVector(sql3);
			if(vec3!=null&&vec3.size()>0){
				XNXQBM=vec3.get(0).toString();
			}
		}
		String sql = " select [实际上课周数] FROM V_规则管理_学年学期表 where 学年学期编码='"+MyTools.fixSql(XNXQBM)+"'" ;
		vec = db.GetContextVector(sql);
		if(vec!=null&vec.size()>0){
			this.setMSG(vec.get(0).toString());			
		}
		String sql2 = " select COUNT(*) from [dbo].[V_规则管理_学期周次表] where 学年学期编码='"+MyTools.fixSql(XNXQBM)+"'" ;
		vec2 = db.GetContextVector(sql2);
		if(vec2!=null&vec2.size()>0){
			this.setMSG2(vec2.get(0).toString());			
		}
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017-03-27
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo() throws SQLException{
		Vector vec = null;
		String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName,'2' as px  FROM V_规则管理_学年学期表 where 1=1) e order by e.px,e.comboValue desc";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//XX上课日期combobox
	public Vector XX_SKRQCombobox() throws SQLException{
			Vector vec = null;	
			String sql2="";
				
			String sql = " select '01' as comboValue,'周一' as comboName union " +
						 " select '02' as comboValue,'周二' as comboName union " +
						 " select '03' as comboValue,'周三' as comboName union " +
						 " select '04' as comboValue,'周四' as comboName union " +
						 " select '05' as comboValue,'周五' as comboName " ;
			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
	}
	
	//XX上课时间combobox
	public Vector XX_SKSJCombobox() throws SQLException{
			Vector vec = null;	
			String sql2="";
			
			String sql = " select '01' as comboValue,'01' as comboName union " +
						 " select '02' as comboValue,'02' as comboName union " +
						 " select '03' as comboValue,'03' as comboName union " +
						 " select '04' as comboValue,'04' as comboName union " +
						 " select '05' as comboValue,'05' as comboName union " +
						 " select '06' as comboValue,'06' as comboName union " +
						 " select '07' as comboValue,'07' as comboName union " +
						 " select '08' as comboValue,'08' as comboName union " +
						 " select '09' as comboValue,'09' as comboName union " +
						 " select '10' as comboValue,'10' as comboName " ;
			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
	}

	//XX上课日期combobox
	public Vector CP_SKRQCombobox() throws SQLException{
				Vector vec = null;	
					
				String sql = " select '01' as comboValue,'周一' as comboName union " +
							 " select '02' as comboValue,'周二' as comboName union " +
							 " select '03' as comboValue,'周三' as comboName union " +
							 " select '04' as comboValue,'周四' as comboName union " +
							 " select '05' as comboValue,'周五' as comboName union " +
							 " select '06' as comboValue,'周六' as comboName union " +
							 " select '07' as comboValue,'周日' as comboName " ;
				vec = db.getConttexJONSArr(sql, 0, 0);
				return vec;
	}
		
	//XX上课时间combobox
	public Vector CP_SKSJCombobox() throws SQLException{
				Vector vec = null;	
				
				String sql = " select '01' as comboValue,'01' as comboName union " +
							 " select '02' as comboValue,'02' as comboName union " +
							 " select '03' as comboValue,'03' as comboName union " +
							 " select '04' as comboValue,'04' as comboName union " +
							 " select '05' as comboValue,'05' as comboName union " +
							 " select '06' as comboValue,'06' as comboName union " +
							 " select '07' as comboValue,'07' as comboName union " +
							 " select '08' as comboValue,'08' as comboName union " +
							 " select '09' as comboValue,'09' as comboName union " +
							 " select '10' as comboValue,'10' as comboName union " +
							 " select '11' as comboValue,'11' as comboName union " +
							 " select '12' as comboValue,'12' as comboName " ;
				vec = db.getConttexJONSArr(sql, 0, 0);
				return vec;
	}	

	/**
	 * 
	 * @date:2016-04-06
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGridMajor(int pageNum ,int pageSize,String ZYDM,String ZYMC,String majorarr) throws SQLException {
		String sql = "";
		Vector vec = null;
		System.out.println("majorarr----------"+majorarr);
		String majorid="";
		if(majorarr.equals("")){//没有教师数据
			majorid="''";
		}else{//选中的教师排在前面显示
			String[] majors=majorarr.split(",");
			for(int i=0;i<majors.length;i++){
				majorid+="'"+majors[i]+"',";
			}
			majorid=majorid.substring(0, majorid.length()-1);
		}
		sql=" SELECT [专业代码],[专业名称]+[专业代码] as 专业名称,'1' as px FROM [dbo].[V_专业基本信息数据子类] where len([专业代码])!=3 and 专业代码 in ("+majorid+") " +
			" union " +
			" SELECT [专业代码],[专业名称]+[专业代码] as 专业名称,'2' as px FROM [dbo].[V_专业基本信息数据子类] where len([专业代码])!=3 and 专业代码 not in ("+majorid+") "; 
		if(!ZYDM.equals("")){
			sql+=" and 专业代码 like '%"+MyTools.fixSql(ZYDM)+"%'";
		}
		if(!ZYMC.equals("")){
			sql+=" and 专业名称 like '%"+MyTools.fixSql(ZYMC)+"%'";
		}
		sql+=" order by px,专业名称 ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 选修课开班
	 * @date:2016-07-29
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void newElective() throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		Vector vecsql=new Vector();
		String maxNewId="";
		final String classID[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int classnum=0;//同一门课程班级数量
		String classname="";//选修班名称
		String sjxl="";//上课时间序列
		int xl=0;
		int classtag=0;
		
		//时间序列
		String[] sksjx=this.getXX_SJXL().split(",");

		for(int i=0;i<sksjx.length;i++){
			sjxl+=this.getXX_SKRQ()+sksjx[i]+",";
			xl++;
		}
		sjxl=sjxl.substring(0, sjxl.length()-1);
		
		//选修课主表编号
		sql="select count(*),a.[授课计划主表编号] from dbo.V_规则管理_选修课授课计划主表 a,dbo.V_规则管理_选修课授课计划明细表 b where a.授课计划主表编号=b.授课计划主表编号 and a.[学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"' and a.[课程代码]='"+MyTools.fixSql(this.getXX_KCDM())+"' group by a.[授课计划主表编号] ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			classnum=Integer.parseInt(vec.get(0).toString());
			this.setXX_XXKZBBH(vec.get(1).toString());
			classtag=1;
		}else{
			sql2="select max(cast(SUBSTRING([授课计划主表编号],5,9) as bigint)) from [V_规则管理_选修课授课计划主表] where 授课计划主表编号 not like 'XXKMX_9%'";
			vec2 = db.GetContextVector(sql2);
			if (!vec2.toString().equals("[]") && vec2.size() > 0) {
				maxNewId=String.format("%08d", (Long.parseLong(vec2.get(0).toString())+1));  
				this.setXX_XXKZBBH("XXK_"+maxNewId);//设置授课计划明细主键
			}else{
				this.setXX_XXKZBBH("XXK_100000001");//设置授课计划明细主键
			}
		}
		
		//选修课明细编号
		sql4="select count(*) from [dbo].[V_规则管理_选修课授课计划明细表] ";
		if(db.getResultFromDB(sql4)){
			sql3="select max(cast(SUBSTRING(授课计划明细编号,7,9) as bigint)) from [dbo].[V_规则管理_选修课授课计划明细表] where 授课计划明细编号 not like 'XXKMX_9%' ";
			vec3 = db.GetContextVector(sql3);
			if (vec3 != null && vec3.size() > 0) {
				maxNewId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec3.get(0))))+1);
				this.setXX_XXKMXBH("XXKMX_"+maxNewId);//设置编号
			}
		}else{
			this.setXX_XXKMXBH("XXKMX_100000001");//设置编号
		}
		
		classname=(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-0)+this.getXX_KCMC()+"选修"+classID[classnum]+"班";
		
		
		//添加选修课授课计划
		sql5=" insert into [dbo].[V_规则管理_选修课授课计划明细表] ([授课计划明细编号],[授课计划主表编号],[选修班名称],[授课教师编号],[授课教师姓名],[场地要求],[场地名称],[授课周次],[创建人],[创建时间],[状态],[上课时间],[报名人数],[总人数],[可报名专业编号],[可报名专业名称],[总课时],[考试形式]) values (" +
			 "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +//授课计划明细编号
			 "'"+MyTools.fixSql(this.getXX_XXKZBBH())+"'," +//授课计划主表编号
			 "'"+MyTools.fixSql(classname)+"'," +//选修班名称
			 "'"+MyTools.fixSql(this.getXX_JSBH())+"'," +//授课教师编号
			 "'"+MyTools.fixSql(this.getXX_SKJS())+"'," +//授课教师姓名
			 "'"+MyTools.fixSql(this.getXX_CDYQ())+"'," +//场地要求
			 "'"+MyTools.fixSql(this.getXX_CDMC())+"'," +//场地名称
			 "'"+MyTools.fixSql(this.getXX_SKZC())+"'," +//授课周次
			 "'post'," +//创建人
			 "getdate()," +//创建时间
			 "'1'," +//状态
			 "'"+MyTools.fixSql(sjxl)+"'," +//上课时间
			 "'0'," +//报名人数
			 "'"+MyTools.fixSql(this.getXX_BJRS())+"'," +//总人数
			 "'"+MyTools.fixSql(this.getXX_ZYBH())+"'," +//可报名专业编号
			 "'"+MyTools.fixSql(this.getXX_ZYNA())+"'," +//可报名专业名称

			 "'"+MyTools.fixSql(this.getXX_ZOKS())+"',"+//总课时
			 "'"+MyTools.fixSql(this.getXX_KSXS())+"') ";//考试形式
		vecsql.add(sql5);
		
		if(classtag==0){//添加选修课主表
			sql5=" insert into dbo.V_规则管理_选修课授课计划主表  ([授课计划主表编号],[学年学期编码],[课程代码],[课程名称],[课程类型],[授课教师编号],[授课教师姓名],[节数],[场地要求],[场地名称],[授课周次],[授课周次详情],[创建人],[创建时间],[状态]) values (" +
				  "'"+MyTools.fixSql(this.getXX_XXKZBBH())+"'," +//授课计划主表编号
				  "'"+MyTools.fixSql(this.getXX_XNXQ())+"'," +//学年学期编码
				  "'"+MyTools.fixSql(this.getXX_KCDM())+"'," +//课程代码
				  "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程名称
				  "'"+MyTools.fixSql(this.getXX_KCLX())+"'," +//课程类型
				  "'"+MyTools.fixSql(this.getXX_JSBH())+"'," +//授课教师编号
				  "'"+MyTools.fixSql(this.getXX_SKJS())+"'," +//授课教师姓名
				  "'"+MyTools.fixSql(xl+"")+"'," +//节数
				  "'"+MyTools.fixSql(this.getXX_CDYQ())+"'," +//场地要求
				  "'"+MyTools.fixSql(this.getXX_CDMC())+"'," +//场地名称
				  "'"+MyTools.fixSql(this.getXX_SKZC())+"'," +//授课周次
				  "'"+MyTools.fixSql(this.getXX_SKZC())+"'," +//授课周次详情
				  "'post'," +//创建人
				  "getdate()," +//创建时间
				  "'1') ";//状态
			vecsql.add(sql5);
		}else{//更新选修课主表
			sql5=" update dbo.V_规则管理_选修课授课计划主表 set  " +
				  " 授课教师编号=授课教师编号+'&"+MyTools.fixSql(this.getXX_JSBH())+"'," +
				  " 授课教师姓名=授课教师姓名+'&"+MyTools.fixSql(this.getXX_SKJS())+"'," +
				  " 场地要求=场地要求+'&"+MyTools.fixSql(this.getXX_CDYQ())+"'," +
				  " 场地名称=场地名称+'&"+MyTools.fixSql(this.getXX_CDMC())+"'," +
				  " 授课周次=授课周次+'&"+MyTools.fixSql(this.getXX_SKZC())+"'," +
				  " 授课周次详情=授课周次详情+'&"+MyTools.fixSql(this.getXX_SKZC())+"'" +
				  " where [学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"' and [课程代码]='"+MyTools.fixSql(this.getXX_KCDM())+"' and [课程类型]='"+MyTools.fixSql(this.getXX_KCLX())+"' ";
			vecsql.add(sql5);
		}
		
		//添加V_排课管理_选修课课程表信息表
		String[] sjxln=sjxl.split(",");
		for(int i=0;i<sjxln.length;i++){
			sql5="insert into V_排课管理_选修课课程表信息表 ([授课计划明细编号],[时间序列],[授课教师编号],[授课教师名称],[实际场地编号],[实际场地名称],[创建人],[状态]) values (" +
				 "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +
				 "'"+MyTools.fixSql(sjxln[i])+"'," +
				 "'"+MyTools.fixSql(this.getXX_JSBH())+"'," +
				 "'"+MyTools.fixSql(this.getXX_SKJS())+"'," +
				 "'"+MyTools.fixSql(this.getXX_CDYQ())+"'," +
				 "'"+MyTools.fixSql(this.getXX_CDMC())+"'," +
				 "'post','1') ";
			vecsql.add(sql5);
		}
		
		//添加科目课程信息表
		sql3="select [学年学期名称] from [dbo].[V_规则管理_学年学期表] where [学年学期编码] = '"+MyTools.fixSql(this.getXX_XNXQ())+"' ";
		vec3=db.GetContextVector(sql3);
		
		sql5=" insert into [dbo].[V_成绩管理_科目课程信息表] ([学年学期编码],[学年学期名称],[年级代码],[年级名称],[专业代码],[专业名称],[课程代码],[课程名称],[课程类型],[科目类型],[是否参与绩点],[是否参与留级],[总评比例选项],[平时比例],[期中比例],[实训比例],[期末比例],[成绩类型],[实训],[创建人],[创建时间],[状态]) values (" +
			  "'"+MyTools.fixSql(this.getXX_XNXQ())+"'," +//学年学期编码
			  "'"+MyTools.fixSql(vec3.get(0).toString())+"'," +//学年学期名称
			  "'"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"1'," +//年级代码
			  "'"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"级'," +//年级名称
			  "''," +//专业代码
			  "''," +//专业名称
			  "'"+MyTools.fixSql(this.getXX_KCDM())+"'," +//课程代码
			  "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程名称
			  "'"+MyTools.fixSql(Integer.parseInt(this.getXX_KCLX())+"")+"'," +//课程类型
			  "'1'," +//科目类型
			  "'1'," +//是否参与绩点
			  "'1'," +//是否参与留级
			  "'1'," +//总评比例选项
			  "'20'," +//平时比例
			  "''," +//期中比例
			  "''," +//实训比例
			  "'80'," +//期末比例
			  "'1'," +//成绩类型
			  "'0'," +//实训
			  "'post'," +//创建人
			  "getdate()," +//创建时间
			  "'1') ";//状态
		vecsql.add(sql5);
		
		sql5=" insert into dbo.V_成绩管理_登分设置信息表 ([相关编号],[考试类型],[总评比例选项],[平时比例],[期中比例],[实训比例],[期末比例],[成绩类型],[实训],[创建人],[创建时间],[状态]) values (" +
				  "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +//相关编号
				  "'1'," +//考试类型
				  "'1'," +//总评比例选项
				  "'20'," +//平时比例
				  "''," +//期中比例
				  "''," +//实训比例
				  "'80'," +//期末比例
				  "'1'," +//成绩类型
				  "'0'," +//实训
				  "'post'," +//创建人
				  "getdate()," +//创建时间
				  "'1') ";//状态
		vecsql.add(sql5);
		
		//添加登分教师信息表
		sql2="select max(cast([科目编号] as bigint)) from [dbo].[V_成绩管理_科目课程信息表]";
		vec2 = db.GetContextVector(sql2);
		if (vec2 != null && vec2.size() > 0) {
			maxNewId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec2.get(0))))+1);
			this.setKMBH(maxNewId);//设置编号
		}
		
		String[] dfjsbh=this.getXX_JSBH().split("\\+");
		String[] dfjsxm=this.getXX_SKJS().split("\\+");
		String dfjsid="";
		String dfjsname="";
		for(int j=0;j<dfjsbh.length;j++){
			dfjsid+="@"+dfjsbh[j]+"@,";
			dfjsname+="@"+dfjsxm[j]+"@,";
		}
		dfjsid=dfjsid.substring(0, dfjsid.length()-1);
		dfjsname=dfjsname.substring(0, dfjsname.length()-1);
		sql5=" insert into [dbo].[V_成绩管理_登分教师信息表] ([科目编号],[来源类型],[相关编号],[行政班代码],[行政班名称],[课程代码],[课程名称],[登分教师编号],[登分教师姓名],[打印锁定],[创建人],[创建时间],[状态]) values (" +
			  "'"+MyTools.fixSql(this.getKMBH())+"'," +//科目编号
			  "'3'," +//来源类型
			  "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +//相关编号
			  "''," +//行政班代码
			  "'"+MyTools.fixSql(classname)+"'," +//行政班名称
			  "'"+MyTools.fixSql(this.getXX_KCDM())+"'," +//课程代码
			  "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程名称
			  "'"+MyTools.fixSql(dfjsid)+"'," +//登分教师编号
			  "'"+MyTools.fixSql(dfjsname)+"'," +//登分教师姓名
			  "'0'," +//打印锁定
			  "'post'," +//创建人
			  "getdate()," +//创建时间
			  "'1')";//状态
		vecsql.add(sql5);
		
		if(db.executeInsertOrUpdateTransaction(vecsql)){
			this.setMSG("新建成功");
			//更新授课周次
			String sql6="select [授课周次] from dbo.V_规则管理_选修课授课计划主表 a,dbo.V_规则管理_选修课授课计划明细表 b where a.授课计划主表编号=b.授课计划主表编号 and a.[学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"' and a.[课程代码]='"+MyTools.fixSql(this.getXX_KCDM())+"' and a.[课程类型]='"+MyTools.fixSql(this.getXX_KCLX())+"' and b.[上课时间]='"+MyTools.fixSql(sjxl)+"' group by a.[授课计划主表编号] ";
//			Vector vec6=db.GetContextVector(sql6);
//			if(vec6!=null&&vec6.size()>0){
//				if(vec6.get(0).toString().indexOf("\\&")>-1){
//					
//				}
//			}
		}else{
			this.setMSG("新建失败");
		}

	}
	
	/**
	 * 编辑选修课班级
	 * @date:2016-07-29
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void editElective() throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		Vector vecsql=new Vector();
		String maxNewId="";
		final String classID[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int classnum=0;//同一门课程班级数量
		String classname="";//选修班名称
		String sjxl="";//上课时间序列
		int xl=0;
		int classtag=0;
		
		//时间序列
		String[] sksjx=this.getXX_SJXL().split(",");

		for(int i=0;i<sksjx.length;i++){
			sjxl+=this.getXX_SKRQ()+sksjx[i]+",";
			xl++;
		}
		sjxl=sjxl.substring(0, sjxl.length()-1);

		String kbmzybh="";
		if(this.getXX_ZYBH().equals("")){//可报名专业编号为空
			kbmzybh="";
		}else{
			kbmzybh=this.getXX_ZYBH();
		}
		//编辑选修课授课计划
		sql5=" update [dbo].[V_规则管理_选修课授课计划明细表] set " +
			 "[授课教师编号]='"+MyTools.fixSql(this.getXX_JSBH())+"'," +
			 "[授课教师姓名]='"+MyTools.fixSql(this.getXX_SKJS())+"'," +
			 "[场地要求]='"+MyTools.fixSql(this.getXX_CDYQ())+"'," +
			 "[场地名称]='"+MyTools.fixSql(this.getXX_CDMC())+"'," +
			 "[授课周次]='"+MyTools.fixSql(this.getXX_SKZC())+"'," +
			 "[上课时间]='"+MyTools.fixSql(sjxl)+"'," +
			 "[总人数]='"+MyTools.fixSql(this.getXX_BJRS())+"'," +
			 "[可报名专业编号]='"+MyTools.fixSql(kbmzybh)+"'," +
			 "[可报名专业名称]='"+MyTools.fixSql(this.getXX_ZYNA())+"'," +
			 "[总课时]='"+MyTools.fixSql(this.getXX_ZOKS())+"'," +
			 
			 "[考试形式]='"+MyTools.fixSql(this.getXX_KSXS())+"'," +
			 
			 "[创建时间]=getdate() " +
			 "where [授课计划明细编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ";
		vecsql.add(sql5);
		
		//编辑选修课主表
		String skjsbh="";
		String skjsxm="";
		String cdyq="";
		String cdmc="";
		String skzc="";
		String skzcxq="";
		sql2="select [授课计划明细编号],[授课教师编号],[授课教师姓名],[场地要求],[场地名称],[授课周次] from [dbo].[V_规则管理_选修课授课计划明细表] where [授课计划主表编号]='"+MyTools.fixSql(this.getXX_XXKZBBH())+"' order by 选修班名称 ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			for(int i=0;i<vec2.size();i=i+6){
				if(vec2.get(i).toString().equals(this.getXX_XXKMXBH())){//授课计划编号相同
					skjsbh+=this.getXX_JSBH()+"&";
					skjsxm+=this.getXX_SKJS()+"&";
					cdyq+=this.getXX_CDYQ()+"&";
					cdmc+=this.getXX_CDMC()+"&";
					skzc+=this.getXX_SKZC()+"&";
				}else{
					skjsbh+=vec2.get(i+1).toString()+"&";
					skjsxm+=vec2.get(i+2).toString()+"&";
					cdyq+=vec2.get(i+3).toString()+"&";
					cdmc+=vec2.get(i+4).toString()+"&";
					skzc+=vec2.get(i+5).toString()+"&";
				}
			}
			skjsbh=skjsbh.substring(0,skjsbh.length()-1);
			skjsxm=skjsxm.substring(0,skjsxm.length()-1);
			cdyq=cdyq.substring(0,cdyq.length()-1);
			cdmc=cdmc.substring(0,cdmc.length()-1);
			skzc=skzc.substring(0,skzc.length()-1);
			skzcxq=skzc;
		}
		
		int[] nums=new int[20];
		String skzcnum="";
		if(skzc.indexOf("\\&")>-1){
			String[] skzc2=skzc.split("&");
			for(int i=0;i<skzc2.length;i++){
				if(skzc2[i].indexOf("-")>-1){
					int num1=Integer.parseInt(skzc2[0].substring(0,skzc2[0].indexOf("-")));
					int num2=Integer.parseInt(skzc2[0].substring(skzc2[0].indexOf("-")+1,skzc2[0].length()));
					int num3=Integer.parseInt(skzc2[1].substring(0,skzc2[1].indexOf("-")));
					int num4=Integer.parseInt(skzc2[1].substring(skzc2[1].indexOf("-")+1,skzc2[1].length()));
					if(num1==1){
						if(num2+1==num3){
							skzcnum=num1+"-"+num4;
						}else{
							skzcnum=skzc;
						}
					}else if(num3==1){
						if(num4+1==num1){
							skzcnum=num3+"-"+num2;
						}else{
							skzcnum=skzc;
						}
					}
				}else{
					
				}
			}
		}
		
		
		sql5="update [dbo].[V_规则管理_选修课授课计划主表] set " +
			  "[课程类型]='"+MyTools.fixSql(this.getXX_KCLX())+"'," +
			  "[授课教师编号]='"+skjsbh+"'," +
			  "[授课教师姓名]='"+skjsxm+"'," +
			  "[节数]='"+xl+"'," +
			  "[场地要求]='"+cdyq+"'," +
			  "[场地名称]='"+cdmc+"'," +
			  "[授课周次]='"+skzcnum+"'," +
			  "[授课周次详情]='"+skzcxq+"'," +
			  "[创建时间]=getdate() " +
			  "where [授课计划主表编号]='"+MyTools.fixSql(this.getXX_XXKZBBH())+"' ";
		vecsql.add(sql5);
		
		//编辑V_排课管理_选修课课程表信息表
		sql5=" delete from [dbo].[V_排课管理_选修课课程表信息表] where [授课计划明细编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ";
		vecsql.add(sql5);
		String[] sjxln=sjxl.split(",");
		for(int i=0;i<sjxln.length;i++){
			sql5="insert into V_排课管理_选修课课程表信息表 ([授课计划明细编号],[时间序列],[授课教师编号],[授课教师名称],[实际场地编号],[实际场地名称],[创建人],[状态]) values (" +
						 "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +
						 "'"+MyTools.fixSql(sjxln[i])+"'," +
						 "'"+MyTools.fixSql(this.getXX_JSBH())+"'," +
						 "'"+MyTools.fixSql(this.getXX_SKJS())+"'," +
						 "'"+MyTools.fixSql(this.getXX_CDYQ())+"'," +
						 "'"+MyTools.fixSql(this.getXX_CDMC())+"'," +
						 "'post','1') ";
			vecsql.add(sql5);
		}
		
		
		//编辑科目课程信息表
//		sql3="select [学年学期名称] from [dbo].[V_规则管理_学年学期表] where [学年学期编码] = '"+MyTools.fixSql(this.getXX_XNXQ())+"' ";
//		vec3=db.GetContextVector(sql3);
		
		sql5="update [dbo].[V_成绩管理_科目课程信息表] set " +
			  "[课程类型]='"+MyTools.fixSql(this.getXX_KCLX().replaceAll("0", ""))+"'," +
			  "[创建时间]=getdate() " +
			  " where [科目编号] in (select [科目编号] from [dbo].[V_成绩管理_登分教师信息表] where [相关编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ) ";	
		vecsql.add(sql5);
		
		//编辑登分教师信息表
		String[] dfjsbh=this.getXX_JSBH().split("\\+");
		String[] dfjsxm=this.getXX_SKJS().split("\\+");
		String dfjsid="";
		String dfjsname="";
		for(int j=0;j<dfjsbh.length;j++){
			dfjsid+="@"+dfjsbh[j]+"@,";
			dfjsname+="@"+dfjsxm[j]+"@,";
		}
		dfjsid=dfjsid.substring(0, dfjsid.length()-1);
		dfjsname=dfjsname.substring(0, dfjsname.length()-1);
		sql5="update [dbo].[V_成绩管理_登分教师信息表] set " +
			  "[登分教师编号]='"+MyTools.fixSql(dfjsid)+"',[登分教师姓名]='"+MyTools.fixSql(dfjsname)+"',[创建时间]=getdate() " +
			  "where [相关编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ";
		vecsql.add(sql5);
		
		if(db.executeInsertOrUpdateTransaction(vecsql)){
			this.setMSG("编辑成功");
			//更新授课周次
			String sql6="select [授课周次] from dbo.V_规则管理_选修课授课计划主表 a,dbo.V_规则管理_选修课授课计划明细表 b where a.授课计划主表编号=b.授课计划主表编号 and a.[学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"' and a.[课程代码]='"+MyTools.fixSql(this.getXX_KCDM())+"' and a.[课程类型]='"+MyTools.fixSql(this.getXX_KCLX())+"' and b.[上课时间]='"+MyTools.fixSql(sjxl)+"' group by a.[授课计划主表编号] ";
//			Vector vec6=db.GetContextVector(sql6);
//			if(vec6!=null&&vec6.size()>0){
//				if(vec6.get(0).toString().indexOf("\\&")>-1){
//					
//				}
//			}
		}else{
			this.setMSG("编辑失败");
		}

	}
	
	/**
	 * 删除选修课开班
	 * @date:2016-08-01
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void deleteElective(String skjhmxbh) throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		Vector vec = new Vector();
		Vector vec2 = null;
		Vector vec3 = null;

		sql="select count(*) from dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
		sql2="select count(*) from [dbo].[V_成绩管理_学生成绩信息表] where [相关编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("该选修班已有学生选择，无法删除");
		}else if(db.getResultFromDB(sql)){//该班级已添加成绩信息，无法删除
			this.setMSG("该选修班已添加成绩信息，无法删除");
		}else{//删除开班信息
			String skjsbh="";
			String skjsxm="";
			String cdyq="";
			String cdmc="";
			String skzc="";
			String skzcxq="";
			
			sql3=" select [授课教师编号] from [V_规则管理_选修课授课计划主表] where [授课计划主表编号] in (select [授课计划主表编号] from [V_规则管理_选修课授课计划明细表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"') ";
			vec3=db.GetContextVector(sql3);
			if(vec3!=null&&vec3.size()>0){
				if(vec3.get(0).toString().indexOf("&")>-1){//有2门以上
					sql2="select [授课计划明细编号],[授课教师编号],[授课教师姓名],[场地要求],[场地名称],[授课周次] from [dbo].[V_规则管理_选修课授课计划明细表] where [授课计划主表编号] in (select [授课计划主表编号] from [V_规则管理_选修课授课计划明细表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"') order by 选修班名称 ";
					vec2=db.GetContextVector(sql2);
					if(vec2!=null&&vec2.size()>0){
						for(int i=0;i<vec2.size();i=i+6){
							if(vec2.get(i).toString().equals(skjhmxbh)){//授课计划编号相同
								skjsbh+="";
								skjsxm+="";
								cdyq+="";
								cdmc+="";
								skzc+="";
							}else{
								skjsbh+=vec2.get(i+1).toString()+"&";
								skjsxm+=vec2.get(i+2).toString()+"&";
								cdyq+=vec2.get(i+3).toString()+"&";
								cdmc+=vec2.get(i+4).toString()+"&";
								skzc+=vec2.get(i+5).toString()+"&";
							}
						}
						skjsbh=skjsbh.substring(0,skjsbh.length()-1);
						skjsxm=skjsxm.substring(0,skjsxm.length()-1);
						cdyq=cdyq.substring(0,cdyq.length()-1);
						cdmc=cdmc.substring(0,cdmc.length()-1);
						skzc=skzc.substring(0,skzc.length()-1);
						skzcxq=skzc;
					}
						
					sql5="update [dbo].[V_规则管理_选修课授课计划主表] set " +
						  "[授课教师编号]='"+skjsbh+"'," +
						  "[授课教师姓名]='"+skjsxm+"'," +
						  "[场地要求]='"+cdyq+"'," +
						  "[场地名称]='"+cdmc+"'," +
						  "[授课周次]='"+skzc+"'," +
						  "[授课周次详情]='"+skzcxq+"'," +
						  "[创建时间]=getdate() " +
						  "where [授课计划主表编号] in (select [授课计划主表编号] from [V_规则管理_选修课授课计划明细表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"') ";
					vec.add(sql5);
				}else{
					sql5="delete from [V_规则管理_选修课授课计划主表] where [授课计划主表编号] in (select [授课计划主表编号] from [V_规则管理_选修课授课计划明细表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"') ";
					vec.add(sql5);
				}	
			}
			
			sql2=" delete from [dbo].[V_成绩管理_科目课程信息表] where [科目编号] in (select [科目编号] from [dbo].[V_成绩管理_登分教师信息表] where [相关编号]='"+MyTools.fixSql(skjhmxbh)+"' ) ";
			vec.add(sql2);
			sql2=" delete from [dbo].[V_成绩管理_登分教师信息表] where [相关编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
			vec.add(sql2);
			sql2=" delete from [dbo].[V_成绩管理_登分设置信息表] where [相关编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
			vec.add(sql2);
			sql2=" delete from [dbo].[V_排课管理_选修课课程表信息表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
			vec.add(sql2);
			sql2=" delete from [dbo].[V_规则管理_选修课授课计划明细表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
			vec.add(sql2);
			
			if(db.executeInsertOrUpdateTransaction(vec)){
				this.setMSG("删除成功");
			}else{
				this.setMSG("删除失败");
			}
		}
	}
	
	/**
	 * 读取选修课信息
	 * @author 2016-8-8
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridClass(int pageNum,int pageSize) throws SQLException {
		DBSource dbSource = new DBSource(request);
		String sql = "";
		Vector vec = null;
				
		sql = "select x.学年学期编码,x.学号,x.姓名,x.班级名称,x.志愿编号," +
				"case when x.志愿详情 is null then '<br />未填志愿<br /><br />' else x.志愿详情 end as 志愿详情," +
				"case when x.志愿编号='1' then '1' when x.志愿编号='2' then '2' when x.志愿编号='3' then '3' when x.志愿编号 is null and x.志愿详情!='' then '4' else '5' end as px  from ( " +
					"select distinct a.[学年学期编码],a.[学号],c.[姓名],d.[班级名称]," +
					"志愿编号=(select m.志愿编号 from dbo.V_规则管理_学生选修课志愿表 m where m.学年学期编码=a.学年学期编码 and m.学号=a.学号 and m.志愿明细=a.授课计划明细编号 ), " +
					"志愿详情='（1） '+(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='1' and m.学号=a.学号 and m.学年学期编码=e.学年学期编码 )+'<br />'+" +
					"'（2） '+(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='2' and m.学号=a.学号 and m.学年学期编码=e.学年学期编码 )+'<br />'+" +
					"'（3） '+(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='3' and m.学号=a.学号 and m.学年学期编码=e.学年学期编码 ) " +
					"from dbo.V_规则管理_学生选修课关系表 a " +
					"left join dbo.V_规则管理_选修课授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号 " +
					"left join dbo.V_学生基本数据子类 c on a.学号=c.学号 " +
					"left join dbo.V_基础信息_班级信息表 d on c.行政班代码=d.班级编号 " +
					"left join dbo.V_规则管理_学生选修课志愿表 e on a.授课计划明细编号=e.志愿明细	" +
					"where a.授课计划明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' " +
				") x order by px,x.学号 " ;
		vec = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 显示补选学生
	 * @author 2016-8-10
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridBX(int pageNum,int pageSize) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		sql = " SELECT a.学年学期编码,a.学号,b.姓名,c.行政班名称   FROM V_选修课_补选学生名单 a,dbo.V_学生基本数据子类 b,dbo.V_学校班级数据子类 c " +
			  " where a.学号=b.学号 and b.行政班代码=c.行政班代码 " ;
		if(!this.getXX_XNXQ().equals("")){
			sql+=" and a.学年学期编码='"+MyTools.fixSql(this.getXX_XNXQ())+"'"; 
		}
		sql+=" order by a.学号 ";
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	/**
	 * 显示所有学生
	 * @author 2016-8-3
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridStu(int pageNum,int pageSize,String stuid,String stuname) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		sql = "SELECT  a.[学号],a.[姓名],b.[班级名称] FROM [dbo].[V_学生基本数据子类] a,dbo.V_基础信息_班级信息表 b " +
			  " where a.[行政班代码]=b.[班级编号] and a.学生状态 in ('01','05') " +
			  " and a.学号 not in (select 学号 from dbo.V_选修课_补选学生名单 where 学年学期编码='"+MyTools.fixSql(this.getXX_XNXQ())+"' ) " ;
			
		if(!stuid.equals("")){
			sql+=" and a.[学号] like '%"+MyTools.fixSql(stuid)+"%' ";
		}
		if(!stuname.equals("")){
			sql+=" and a.[姓名] like '%"+MyTools.fixSql(stuname)+"%' ";
		}
		sql+=" order by a.学号 ";
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	/**
	 * 查询选修班学生，按专业
	 * @author 2016-8-8
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridStuClass(int pageNum,int pageSize,String stuid,String stuname,String stuxibu,String stuzhiy,String stuinfo) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String kbmzybh="";
		String nj="";
		String lastterm="";
		
		//获取可报名系部编号
		sql2="select [可报名专业编号] from dbo.V_规则管理_选修课授课计划明细表 where [授课计划明细编号] ='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			kbmzybh=vec2.get(0).toString();
		}
		nj=this.getXNXQBM().substring(2,4);
		
		//获取上学期学期编号
		sql3="select [学年学期编码] from [dbo].[V_规则管理_学年学期表] " +
			"where [学年学期编码] < '"+MyTools.fixSql(this.getXNXQBM())+"' order by [学年学期编码] desc ";
		vec3=db.GetContextVector(sql3);
		if(vec3!=null&&vec3.size()>0){
			lastterm=vec3.get(0).toString();
		}
	
//		sql = "SELECT  a.[学号],a.[姓名],b.[班级名称]," +
//			  "志愿明细1=(select m.志愿明细 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='1' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "志愿明细2=(select m.志愿明细 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='2' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "志愿明细3=(select m.志愿明细 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='3' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "第一志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='1' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "第二志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='2' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "第三志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='3' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 ) " +
//			  "FROM [dbo].[V_学生基本数据子类] a,dbo.V_基础信息_班级信息表 b,dbo.V_规则管理_学生选修课志愿表 c  " +
//			  "where a.[行政班代码]=b.[班级编号] and a.学号=c.学号 and c.学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"' and a.学生状态 in ('01','05') and CHARINDEX(b.系部代码,'"+kbmzybh+"')>0 " +
//			  "and a.学号 not in ( select 学号 from [V_规则管理_学生选修课关系表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"') and "+nj+"-left(年级代码,2) between 0 and 2 " +
//			  "union  " +
//			  "SELECT  a.[学号],a.[姓名],b.[班级名称]," +
//			  "志愿明细1=(select m.志愿明细 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='1' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "志愿明细2=(select m.志愿明细 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='2' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "志愿明细3=(select m.志愿明细 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='3' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "第一志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='1' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "第二志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='2' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 )," +
//			  "第三志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='3' and m.学号=a.学号 and m.学年学期编码=c.学年学期编码 ) " +
//			  "FROM [dbo].[V_学生基本数据子类] a,dbo.V_基础信息_班级信息表  b,dbo.V_规则管理_学生选修课志愿表 c " +
//			  "where a.[行政班代码]=b.[班级编号] and a.学号=c.学号 and c.学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"' and a.学生状态 in ('01','05') and a.学号 not in ( select 学号 from [V_规则管理_学生选修课关系表] where [授课计划明细编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"')  and a.[学号] in ( select 学号 from dbo.V_选修课_补选学生名单) ";
//		sql3="select k.[学号],k.[姓名],k.[班级名称],k.志愿明细1,k.志愿明细2,k.志愿明细3,k.第一志愿,k.第二志愿,k.第三志愿 from ("+sql+") k where 1=1 ";
	
		if(stuinfo.equals("1")){//过滤已安排学生
			sql6=" and a.学号 not in ( select 学号 from [V_规则管理_学生选修课关系表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"') " ;
		}else{
			sql6=" and a.学号 not in ( select 学号 from [V_规则管理_学生选修课关系表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' and 授课计划明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"') " ;
		}
		
		sql2 = "select b.学年学期名称,a.学号,c.姓名,d.班级名称,d.系部代码,f.系部名称,a.志愿编号,a.志愿明细,e.选修班名称,a.px " +
				//"上学期第几志愿=isnull((select n.志愿编号 FROM dbo.V_规则管理_学生选修课关系表 m left join dbo.V_规则管理_学生选修课志愿表 n on m.学号=n.学号 where m.学年学期编码='"+lastterm+"' and n.学年学期编码='"+lastterm+"' and m.学号=a.学号 and m.授课计划明细编号=n.志愿明细 ),'')," +
				//"上学期选修情况=(select p.选修班名称 FROM dbo.V_规则管理_学生选修课关系表 m left join dbo.V_规则管理_选修课授课计划明细表 p on m.授课计划明细编号=p.授课计划明细编号 where m.学年学期编码='"+lastterm+"' and m.学号=a.学号  ) " +
				"from ( " +
				"SELECT  [学年学期编码],[学号],[志愿编号],[志愿明细],'1' as px FROM [dbo].[V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' and [志愿编号]='1' and [志愿明细]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' " +
				"union SELECT  [学年学期编码],[学号],[志愿编号],[志愿明细],'2' as px FROM [dbo].[V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' and [志愿编号]='2' and [志愿明细]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' " +
				"union SELECT  [学年学期编码],[学号],[志愿编号],[志愿明细],'3' as px FROM [dbo].[V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' and [志愿编号]='3' and [志愿明细]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' " +
				"union SELECT distinct [学年学期编码],[学号],'4' as 志愿编号,'' as 志愿明细,'4' as px FROM [dbo].[V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' and [志愿明细]!='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' and 学号 not in (select 学号 from V_规则管理_学生选修课志愿表 where [志愿明细]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"') " +
				"union SELECT '"+MyTools.fixSql(this.getXNXQBM())+"' as 学年学期编码,m.学号,'4' as 志愿编号,'' as 志愿明细,'5' as px FROM dbo.V_学生基本数据子类 m left join dbo.V_基础信息_班级信息表 n on m.行政班代码=n.班级编号  where "+nj+"-left(n.年级代码,2) between 0 and 2  and m.学号 not in (select distinct [学号] from [dbo].[V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' ) " +
			  ") a " +
			  "left join dbo.V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
			  "left join dbo.V_学生基本数据子类 c on a.学号=c.学号 " +
			  "left join dbo.V_基础信息_班级信息表 d on c.行政班代码=d.班级编号 " +
			  "left join dbo.V_规则管理_选修课授课计划明细表 e on a.志愿明细=e.授课计划明细编号 " +
			  "left join dbo.V_基础信息_系部信息表 f on d.系部代码=f.系部代码 " +
			  "where c.学生状态 in ('01','05') and (CHARINDEX(d.系部代码,'"+kbmzybh+"')>0 or '"+kbmzybh+"'='') " +	  
			  "and "+nj+"-left(年级代码,2) between 0 and 2 " +sql6 ;
		
		//查询上学期志愿详情
		sql4="select r.学号,r.姓名,r.选修班名称+' （'+r.志愿详情+'）' as 志愿详情 from ( " +
				"select s.学号,s.姓名,s.选修班名称,case when s.志愿='' then '非所选志愿' when s.志愿='1' then '第一志愿' when s.志愿='2' then '第二志愿' when s.志愿='3' then '第三志愿' else '' end as 志愿详情 from ( " +
				"select b.学年学期名称,a.学号,c.姓名,d.选修班名称,志愿=isnull((select m.志愿编号 from dbo.V_规则管理_学生选修课志愿表 m where m.学年学期编码=a.学年学期编码 and m.学号=a.学号 and m.志愿明细=a.授课计划明细编号 ),'') " +
				"from dbo.V_规则管理_学生选修课关系表 a left join dbo.V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 left join dbo.V_学生基本数据子类 c on a.学号=c.学号 left join dbo.V_规则管理_选修课授课计划明细表 d on a.授课计划明细编号=d.授课计划明细编号 where a.学年学期编码='"+lastterm+"' " +
				") s ) r ";
		
		sql5="select distinct q.学号,q.姓名, " +
				"STUFF((select '#'+p.志愿详情 from ("+sql4+") p where q.学号=p.学号 and q.姓名=p.姓名 for xml path('')),1,1,'') as 志愿详情 " +
				"from ("+sql4+") q ";
		
		sql="select u.*,v.志愿详情 as 上学期选修情况 from ("+sql2+") u left join ("+sql5+") v on u.学号=v.学号 where 1=1 ";
		
		if(!stuid.equals("")){
			sql+=" and u.[学号] like '%"+MyTools.fixSql(stuid)+"%' ";
		}
		if(!stuname.equals("")){
			sql+=" and u.[姓名] like '%"+MyTools.fixSql(stuname)+"%' ";
		}
		if(!stuxibu.equals("")){
			sql+=" and u.[系部代码] = '"+MyTools.fixSql(stuxibu)+"' ";
		}
		if(!stuzhiy.equals("")){
			sql+=" and u.[志愿编号] = '"+MyTools.fixSql(stuzhiy)+"' ";
		}
		
		sql+=" order by u.px,u.学号 " ;

		vec = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 查询选修班学生，按专业
	 * @author 2016-8-8
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridnotWalkThrough(int pageNum,int pageSize,String stuid,String stuname,String stuxibu) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String nj="";
		String lastterm="";
		
		//年级
		nj=this.getXNXQBM().substring(2,4);
		
		//获取上学期学期编号
		sql3="select [学年学期编码] from [dbo].[V_规则管理_学年学期表] " +
			"where [学年学期编码] < '"+MyTools.fixSql(this.getXNXQBM())+"' order by [学年学期编码] desc ";
		vec3=db.GetContextVector(sql3);
		if(vec3!=null&&vec3.size()>0){
			lastterm=vec3.get(0).toString();
		}
		
		//过滤已安排学生
		sql6=" and a.学号 not in ( select 学号 from [V_规则管理_学生选修课关系表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"') " ;
		
		sql2 = "select a.学年学期编码,b.学年学期名称,a.学号,c.姓名,d.班级编号,d.班级名称,d.系部代码,f.系部名称,a.px " +
				//"上学期第几志愿=isnull((select n.志愿编号 FROM dbo.V_规则管理_学生选修课关系表 m left join dbo.V_规则管理_学生选修课志愿表 n on m.学号=n.学号 where m.学年学期编码='"+lastterm+"' and n.学年学期编码='"+lastterm+"' and m.学号=a.学号 and m.授课计划明细编号=n.志愿明细 ),'')," +
				//"上学期选修情况=(select p.选修班名称 FROM dbo.V_规则管理_学生选修课关系表 m left join dbo.V_规则管理_选修课授课计划明细表 p on m.授课计划明细编号=p.授课计划明细编号 where m.学年学期编码='"+lastterm+"' and m.学号=a.学号  ) " +
				"from ( " +
				"SELECT distinct [学年学期编码],[学号],'4' as px FROM [dbo].[V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' " + //填了志愿未被预排的学生
				"union SELECT '"+MyTools.fixSql(this.getXNXQBM())+"' as 学年学期编码,m.学号,'5' as px FROM dbo.V_学生基本数据子类 m left join dbo.V_基础信息_班级信息表 n on m.行政班代码=n.班级编号  where "+nj+"-left(n.年级代码,2) between 0 and 2  and m.学号 not in (select distinct [学号] from [dbo].[V_规则管理_学生选修课志愿表] where [学年学期编码]='"+MyTools.fixSql(this.getXNXQBM())+"' ) " + //未填过志愿的学生
			  ") a " +
			  "left join dbo.V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
			  "left join dbo.V_学生基本数据子类 c on a.学号=c.学号 " +
			  "left join dbo.V_基础信息_班级信息表 d on c.行政班代码=d.班级编号 " +
			  "left join dbo.V_基础信息_系部信息表 f on d.系部代码=f.系部代码 " +
			  "where c.学生状态 in ('01','05') " +	  
			  "and "+nj+"-left(年级代码,2) between 0 and 2 " +sql6 ;
		
		//查询上学期志愿详情
		sql4="select r.学号,r.姓名,r.选修班名称+' （'+r.志愿详情+'）' as 志愿详情 from ( " +
				"select s.学号,s.姓名,s.选修班名称,case when s.志愿='' then '非所选志愿' when s.志愿='1' then '第一志愿' when s.志愿='2' then '第二志愿' when s.志愿='3' then '第三志愿' else '' end as 志愿详情 from ( " +
				"select b.学年学期名称,a.学号,c.姓名,d.选修班名称,志愿=isnull((select m.志愿编号 from dbo.V_规则管理_学生选修课志愿表 m where m.学年学期编码=a.学年学期编码 and m.学号=a.学号 and m.志愿明细=a.授课计划明细编号 ),'') " +
				"from dbo.V_规则管理_学生选修课关系表 a left join dbo.V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 left join dbo.V_学生基本数据子类 c on a.学号=c.学号 left join dbo.V_规则管理_选修课授课计划明细表 d on a.授课计划明细编号=d.授课计划明细编号 where a.学年学期编码='"+lastterm+"' " +
				") s ) r ";
		
		sql5="select distinct q.学号,q.姓名, " +
				"STUFF((select '#'+p.志愿详情 from ("+sql4+") p where q.学号=p.学号 and q.姓名=p.姓名 for xml path('')),1,1,'') as 志愿详情 " +
				"from ("+sql4+") q ";
		
		sql="select u.*,v.志愿详情 as 上学期选修情况 from ("+sql2+") u left join ("+sql5+") v on u.学号=v.学号 where 1=1 ";
		
		if(!stuid.equals("")){
			sql+=" and u.[学号] like '%"+MyTools.fixSql(stuid)+"%' ";
		}
		if(!stuname.equals("")){
			sql+=" and u.[姓名] like '%"+MyTools.fixSql(stuname)+"%' ";
		}
		if(!stuxibu.equals("")){
			sql+=" and u.[系部代码] = '"+MyTools.fixSql(stuxibu)+"' ";
		}
		
		sql+=" order by u.px,u.学号 " ;

		vec = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 查询志愿信息
	 * @author 2017-9-19
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridstuZY(int pageNum,int pageSize,String stuid) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		String sql = "";
					
		sql = "select distinct b.学年学期名称,a.学号,c.姓名," +
			"第一志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='1' and m.学号=a.学号 and m.学年学期编码=a.学年学期编码 )," +
			"第二志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='2' and m.学号=a.学号 and m.学年学期编码=a.学年学期编码 )," +
			"第三志愿=(select n.选修班名称 from dbo.V_规则管理_学生选修课志愿表 m,dbo.V_规则管理_选修课授课计划明细表 n where m.志愿明细=n.授课计划明细编号 and m.志愿编号='3' and m.学号=a.学号 and m.学年学期编码=a.学年学期编码 ) " +
			"from dbo.V_规则管理_学生选修课志愿表 a " +
			"left join dbo.V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
			"left join dbo.V_学生基本数据子类 c on a.学号=c.学号 " +
			"where a.学号='"+MyTools.fixSql(stuid)+"' ";
				
		vec = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 查询选修课信息
	 * @author 2017-9-19
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridstuXQ(int pageNum,int pageSize,String stuid) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		String sql = "";
					
		sql = "select b.学年学期名称,a.学号,c.姓名,d.选修班名称," +
			"志愿=(select m.志愿编号 from dbo.V_规则管理_学生选修课志愿表 m where m.学年学期编码=a.学年学期编码 and m.学号=a.学号 and m.志愿明细=a.授课计划明细编号 ) " +
			"from dbo.V_规则管理_学生选修课关系表 a " +
			"left join dbo.V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
			"left join dbo.V_学生基本数据子类 c on a.学号=c.学号 " +
			"left join dbo.V_规则管理_选修课授课计划明细表 d on a.授课计划明细编号=d.授课计划明细编号 " +
			"where a.学号='"+MyTools.fixSql(stuid)+"' ";
				
		vec = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 查询选修班仅一人选择列表
	 * @author 2017-10-26
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridOneStudent(int pageNum,int pageSize) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		String sql = "";
					
		sql = "SELECT a.学年学期编码,d.班级编号,d.班级名称" +
			",(select m.学号 from V_规则管理_学生选修课关系表 m left join V_学生基本数据子类 n on m.学号=n.学号 where m.学年学期编码='"+this.getXNXQBM()+"' and m.授课计划明细编号=a.授课计划明细编号 and n.行政班代码=d.班级编号) as 学号" +
			",(select n.姓名 from V_规则管理_学生选修课关系表 m left join V_学生基本数据子类 n on m.学号=n.学号 where m.学年学期编码='"+this.getXNXQBM()+"' and m.授课计划明细编号=a.授课计划明细编号 and n.行政班代码=d.班级编号) as 姓名" +
			",a.授课计划明细编号,b.选修班名称,COUNT(*) as 人数 FROM [dbo].[V_规则管理_学生选修课关系表] a " +
			"left join dbo.V_规则管理_选修课授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号 " +
			"left join dbo.V_学生基本数据子类 c on a.学号=c.学号 " +
			"left join dbo.V_基础信息_班级信息表 d on c.行政班代码=d.班级编号 " +
			"where a.学年学期编码='"+this.getXNXQBM()+"' " +
			"group by a.学年学期编码,d.班级编号,d.班级名称,a.授课计划明细编号,b.选修班名称 " +
			"having COUNT(*)='1' " +
			"order by d.班级编号,学号";
				
		vec = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 删除所选学生
	 * @author 2016-8-8
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void delStudent(int pageNum,int pageSize,String stuidarray) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = new Vector();
		Vector vec2 = new Vector();
		String sql = "";
		String sql2 = "";
		String stunums="";
		
		String[] stuid=stuidarray.split(",");
		for(int i=0;i<stuid.length;i++){
			stunums+="'"+stuid[i]+"',";
		}
		stunums=stunums.substring(0, stunums.length()-1);
		sql = " delete from dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' and 学号 in ("+stunums+") " ;
		vector.add(sql);
//		sql = " update dbo.V_规则管理_选修课授课计划明细表 set 报名人数=报名人数-"+stuid.length+" where 授课计划明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' "; 
//		vector.add(sql);
		sql = " delete from V_成绩管理_学生成绩信息表  where 学号 in ("+stunums+") and 相关编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"'" ;
		vector.add(sql);		
		if(db.executeInsertOrUpdateTransaction(vector)){
			//查询更新后报名人数
			sql2="select count(*) from dbo.V_规则管理_学生选修课关系表 where 授课计划明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' and 学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"' ";
			vec2=db.GetContextVector(sql2);
			if(vec2!=null&&vec2.size()>0){
				this.setMSG2(vec2.get(0).toString());
			}
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 学生换班
	 * @author 2016-8-9
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void changeStudent(int pageNum,int pageSize,String xxkskjhmx,String stuidarray,String stuxmarray,String bmrs,String zrs,String skzc,String sksj) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = new Vector();
		Vector vec2 = new Vector();
		String sql = "";
		String sql2 = "";
		String stunums="";
		String result="";
		String msginfo="";
		
		String[] stuid=stuidarray.split(",");
		String[] stuxm=stuxmarray.split(",");
		for(int i=0;i<stuid.length;i++){
			stunums+="'"+stuid[i]+"',";
		}
		stunums=stunums.substring(0, stunums.length()-1);
		//可报名人数小于选择学生数量
		if((Integer.parseInt(zrs)-Integer.parseInt(bmrs))<stuid.length){
			this.setMSG("选择报名人数大于总人数");
		}else{
			for(int i=0;i<stuid.length;i++){
				stunums+="'"+stuid[i]+"',";
				result=checkSelectionChange(xxkskjhmx,this.getXX_XXKMXBH(),stuid[i],this.getXX_XNXQ(),this.getXX_SKZC(),this.getXX_SJXL(),this.getXX_KCDM(),skzc,sksj);
				if(result.equals("3")){
					//msginfo+=stuxm[i]+" 已经选了3门课程! <br/>";
				}else if(result.equals("2")){
					//msginfo+="该学期 "+stuxm[i]+" 已经选了2门课程! <br/>";
				}else if(result.equals("4")){
					msginfo+="该课程授课周次和 "+stuxm[i]+" 的另一课程有冲突! <br/>";
				}else if(result.equals("5")){
					msginfo+=stuxm[i]+" 已经报过该课程! <br/>";
				}else if(result.equals("6")){
					msginfo+=stuxm[i]+" 所属系部不能报名该课程! <br/>";
				}else{
							
				}
			}
			stunums=stunums.substring(0, stunums.length()-1);
			if(msginfo.equals("")){//没有冲突可以保存
				for(int i=0;i<stuid.length;i++){
					sql = " delete from dbo.V_规则管理_学生选修课关系表 where 授课计划明细编号='"+MyTools.fixSql(xxkskjhmx)+"' and 学号='"+MyTools.fixSql(stuid[i])+"' ";
					vector.add(sql);
					sql = " insert into dbo.V_规则管理_学生选修课关系表 ([学年学期编码],[学号],[授课计划明细编号],[创建人],[创建时间],[状态]) values (" +
							"'"+MyTools.fixSql(this.XX_XNXQ)+"'," +
							"'"+MyTools.fixSql(stuid[i])+"'," +
							"'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +
							"'"+MyTools.fixSql("post")+"'," +
							"getdate(),'1') ";
					vector.add(sql);
//					sql = " update dbo.V_规则管理_选修课授课计划明细表 set 报名人数=报名人数-1 where 授课计划明细编号='"+MyTools.fixSql(xxkskjhmx)+"' ";
//					vector.add(sql);
//					sql = " update dbo.V_规则管理_选修课授课计划明细表 set 报名人数=报名人数+1 where 授课计划明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' "; 
//					vector.add(sql);
					sql = " delete from V_成绩管理_学生成绩信息表  where 学号='"+MyTools.fixSql(stuid[i])+"' and 相关编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"'" ;
					vector.add(sql);
				}
				if(db.executeInsertOrUpdateTransaction(vector)){
					//查询更新后报名人数
					sql2="select count(*) from dbo.V_规则管理_学生选修课关系表 where 授课计划明细编号='"+MyTools.fixSql(xxkskjhmx)+"' and 学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"' ";
					vec2=db.GetContextVector(sql2);
					if(vec2!=null&&vec2.size()>0){
						this.setMSG2(vec2.get(0).toString());
					}
					this.setMSG("保存成功");
				}else{
					this.setMSG("保存失败");
				}
			}else{
				this.setMSG(msginfo);
			}
		}
	}
	
	/**
	 * 添加所选学生
	 * @author 2016-8-8
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void submitStu(int pageNum,int pageSize,String stunumarray,String stunamearray,String bmrs,String zrs) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = new Vector();
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		String stunums="";
		String result="";
		String msginfo="";
		
		String[] stunum=stunumarray.split(",");
		String[] stuname=stunamearray.split(",");
		//可报名人数小于选择学生数量
		if((Integer.parseInt(zrs)-Integer.parseInt(bmrs))<stunum.length){
			this.setMSG("选择报名人数大于总人数");
		}else{
			for(int i=0;i<stunum.length;i++){
				stunums+="'"+stunum[i]+"',";
				result=checkSelectionAdd(this.getXX_XXKMXBH(),stunum[i],this.getXX_XNXQ(),this.getXX_SKZC(),this.getXX_SJXL(),this.getXX_KCDM());
				if(result.equals("3")){
					//msginfo+=stuname[i]+" 已经选了3门课程! <br/>";
				}else if(result.equals("2")){
					//msginfo+="该学期 "+stuname[i]+" 已经选了2门课程! <br/>";
				}else if(result.equals("4")){
					msginfo+="该课程授课周次和 "+stuname[i]+" 的另一课程有冲突! <br/>";
				}else if(result.equals("5")){
					msginfo+=stuname[i]+" 已经报过该课程! <br/>";
				}else if(result.equals("6")){
					msginfo+=stuname[i]+" 所属系部不能报名该课程! <br/>";
				}else{
					
				}
			}
			stunums=stunums.substring(0, stunums.length()-1);
			if(msginfo.equals("")){//没有冲突可以保存
				for(int i=0;i<stunum.length;i++){
					sql = " delete from dbo.V_规则管理_学生选修课关系表 where 学年学期编码='"+MyTools.fixSql(this.getXX_XNXQ())+"' and 学号='"+MyTools.fixSql(stunum[i])+"' ";
					vector.add(sql);
					sql += " insert into dbo.V_规则管理_学生选修课关系表 ([学年学期编码],[学号],[授课计划明细编号],[创建人],[创建时间],[状态]) values (" +
							"'"+MyTools.fixSql(this.XX_XNXQ)+"'," +
							"'"+MyTools.fixSql(stunum[i])+"'," +
							"'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +
							"'"+MyTools.fixSql("post")+"'," +
							"getdate(),'1') ";
					vector.add(sql);
					//sql += " update dbo.V_规则管理_选修课授课计划明细表 set 报名人数=报名人数+1 where 授课计划明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' "; 
				}		
				if(db.executeInsertOrUpdateTransaction(vector)){
					//查询更新后报名人数
					sql2="select count(*) from dbo.V_规则管理_学生选修课关系表 where 授课计划明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' and 学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"' ";
					vec2=db.GetContextVector(sql2);
					if(vec2!=null&&vec2.size()>0){
						this.setMSG2(vec2.get(0).toString());
					}
					this.setMSG("添加成功");
				}else{
					this.setMSG("添加失败");
				}
			}else{
				this.setMSG(msginfo);
			}
		}
	}
	
	/**
	 * 添加所选学生
	 * @author 2016-8-10
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void addBXStu(int pageNum,int pageSize,String addbxstuarray) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		String stunums="";
		String result="";
		String msginfo="";
		
		String[] stunum=addbxstuarray.split(",");
		for(int i=0;i<stunum.length;i++){
			sql += " insert into dbo.V_选修课_补选学生名单  ([学年学期编码],[学号]) values (" +
					"'"+MyTools.fixSql(this.XX_XNXQ)+"'," +
					"'"+MyTools.fixSql(stunum[i])+"') " ;
		}
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("添加成功");
		}else{
			this.setMSG("添加失败");
		}
	}
	
	/**
	 * 删除补选学生
	 * @author 2016-8-10
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void delbxStudent(int pageNum,int pageSize,String delbxstuarray) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		String stunums="";
		
		String[] stuid=delbxstuarray.split(",");
		for(int i=0;i<stuid.length;i++){
			stunums+="'"+stuid[i]+"',";
		}
		stunums=stunums.substring(0, stunums.length()-1);
		sql = " delete from dbo.V_选修课_补选学生名单 where [学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"' and 学号 in ("+stunums+") " ;
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 读取机房信息
	 * @author 2016-7-27
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadComputer(int pageNum,int pageSize,String SC_XNXQ,String SC_KCMC) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		sql = " SELECT [机房明细编号],[学年学期编码] as XX_XNXQ,[课程考试名称] as XX_KCMC,[班级名称],[使用教师] as XX_SKJS,[使用教室] as XX_CDMC,[使用周次] as XX_SKZC,[使用时间] as XX_SJXL,[人数] as XX_BJRS,[学分] as XX_XUEF,[总课时] as XX_ZOKS " +
				" FROM [dbo].[V_机房课表_课程考试明细表] where 1=1 " ;
		
		if(!SC_XNXQ.equals("")){
			sql+=" and 学年学期编码 = '"+MyTools.fixSql(SC_XNXQ)+"' ";
		}
		if(!SC_KCMC.equals("")){
			sql+=" and 课程考试名称 like '%"+MyTools.fixSql(SC_KCMC)+"%' ";
		}
		sql+=" order by [学年学期编码] desc,[机房明细编号] ";
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	
	/**
	 * 新建机房信息
	 * @date:2016-07-29
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void newComputer() throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String maxNewId="";
		final String classID[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int classnum=0;//同一门课程班级数量
		String classname="";//选修班名称
		String sjxl="";//上课时间序列
		String sjxlck="";
		int xl=0;
		int classtag=0;
		
		//时间序列
		String[] sksjx=this.getXX_SJXL().split(",");
		//System.out.println("sksj:--"+this.getXX_SJXL()+sksjx.length);
		for(int i=0;i<sksjx.length;i++){
			sjxl+=this.getXX_SKRQ()+sksjx[i]+",";
			sjxlck+="'"+this.getXX_SKRQ()+sksjx[i]+"',";
			xl++;
		}
		if(!sjxl.equals("")){
			sjxl=sjxl.substring(0, sjxl.length()-1);
		}
		if(!sjxlck.equals("")){
			sjxlck=sjxlck.substring(0, sjxlck.length()-1);
		}
		
		//机房明细编号
		sql4="select count(*) from [dbo].[V_机房课表_课程考试明细表] ";
		if(db.getResultFromDB(sql4)){
			sql3="select max(cast(SUBSTRING(机房明细编号,7,9) as bigint)) from [dbo].[V_机房课表_课程考试明细表] ";
			vec3 = db.GetContextVector(sql3);
			if (vec3 != null && vec3.size() > 0) {
				maxNewId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec3.get(0))))+1);
				this.setXX_XXKMXBH("JSFMX_"+maxNewId);//设置编号
			}
		}else{
			this.setXX_XXKMXBH("JSFMX_100000001");//设置编号
		}
		if(this.getXX_XUEF().equals("")){//学分为空
			this.setXX_XUEF("0");
		}
		if(this.getXX_ZOKS().equals("")){//总人数为空
			this.setXX_ZOKS("0");
		}
		
		String timeorder=checkTeaCls("",this.getXX_SKZC(),this.getXX_WEEK(),this.getXX_CDMC(),"",this.getXX_XNXQ(),"","",sjxl,sjxlck,this.getXX_XXKMXBH());
		
		if(!timeorder.equals("")){
			this.setMSG2("您设置的周次、时间内所选教室已被使用，请重新调整");
		}else{
			//添加选修课授课计划
			sql5=" insert into [dbo].[V_机房课表_课程考试明细表] ([学年学期编码],[机房明细编号],[课程考试名称],[班级名称],[使用教师],[使用教室],[使用周次],[创建人],[创建时间],[状态],[使用时间],[人数],[学分],[总课时]) values (" +
				 "'"+MyTools.fixSql(this.getXX_XNXQ())+"'," +//学年学期编码
				 "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +//机房明细编号
				 "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程考试名称
				 "'"+MyTools.fixSql(this.getXX_BJMC())+"'," +//班级名称
				 "'"+MyTools.fixSql(this.getXX_SKJS())+"'," +//使用教师
				 "'"+MyTools.fixSql(this.getXX_CDMC())+"'," +//使用教室
				 "'"+MyTools.fixSql(this.getXX_SKZC())+"'," +//使用周次
				 "'post'," +//创建人
				 "getdate()," +//创建时间
				 "'1'," +//状态
				 "'"+MyTools.fixSql(sjxl)+"'," +//使用时间
				 "'"+MyTools.fixSql(this.getXX_BJRS())+"'," +//人数
				 "'"+MyTools.fixSql(this.getXX_XUEF())+"'," +//学分
				 "'"+MyTools.fixSql(this.getXX_ZOKS())+"') ";//总课时
		
			
	//		//添加科目课程信息表
	//		sql3="select [学年学期名称] from [dbo].[V_规则管理_学年学期表] where [学年学期编码] = '"+MyTools.fixSql(this.getXX_XNXQ())+"' ";
	//		vec3=db.GetContextVector(sql3);
	//		
	//		sql5+=" insert into [dbo].[V_成绩管理_科目课程信息表] ([学年学期编码],[学年学期名称],[年级代码],[年级名称],[专业代码],[专业名称],[课程代码],[课程名称],[课程类型],[创建人],[创建时间],[状态]) values (" +
	//			  "'"+MyTools.fixSql(this.getXX_XNXQ())+"'," +//学年学期编码
	//			  "'"+MyTools.fixSql(vec3.get(0).toString())+"'," +//学年学期名称
	//			  "'"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"1'," +//年级代码
	//			  "'"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"级'," +//年级名称
	//			  "''," +//专业代码
	//			  "''," +//专业名称
	//			  "'"+MyTools.fixSql(this.getXX_KCDM())+"'," +//课程代码
	//			  "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程名称
	//			  "'"+MyTools.fixSql(this.getXX_KCLX())+"'," +//课程类型
	//			  "'post'," +//创建人
	//			  "getdate()," +//创建时间
	//			  "'1') ";//状态
	//		
	//		sql5+=" insert into dbo.V_成绩管理_登分设置信息表 ([相关编号],[考试类型],[总评比例选项],[平时比例],[期中比例],[实训比例],[期末比例],[成绩类型],[实训],[创建人],[创建时间],[状态]) values (" +
	//				  "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +//相关编号
	//				  "''," +//考试类型
	//				  "'4'," +//总评比例选项
	//				  "'40'," +//平时比例
	//				  "''," +//期中比例
	//				  "''," +//实训比例
	//				  "'60'," +//期末比例
	//				  "'1'," +//成绩类型
	//				  "'0'," +//实训
	//				  "'post'," +//创建人
	//				  "getdate()," +//创建时间
	//				  "'1') ";//状态
	//		
	//		//添加登分教师信息表
	//		sql2="select max(cast([科目编号] as bigint)) from [dbo].[V_成绩管理_科目课程信息表]";
	//		vec2 = db.GetContextVector(sql2);
	//		if (vec2 != null && vec2.size() > 0) {
	//			maxNewId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec2.get(0))))+1);
	//			this.setKMBH(maxNewId);//设置编号
	//		}
	//		
	//		String[] dfjsbh=this.getXX_JSBH().split("\\+");
	//		String[] dfjsxm=this.getXX_SKJS().split("\\+");
	//		String dfjsid="";
	//		String dfjsname="";
	//		for(int j=0;j<dfjsbh.length;j++){
	//			dfjsid+="@"+dfjsbh[j]+"@,";
	//			dfjsname+="@"+dfjsxm[j]+"@,";
	//		}
	//		dfjsid=dfjsid.substring(0, dfjsid.length()-1);
	//		dfjsname=dfjsname.substring(0, dfjsname.length()-1);
	//		sql5+=" insert into [dbo].[V_成绩管理_登分教师信息表] ([科目编号],[来源类型],[相关编号],[行政班代码],[行政班名称],[课程代码],[课程名称],[登分教师编号],[登分教师姓名],[打印锁定],[创建人],[创建时间],[状态]) values (" +
	//			  "'"+MyTools.fixSql(this.getKMBH())+"'," +//科目编号
	//			  "'1'," +//来源类型
	//			  "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +//相关编号
	//			  "''," +//行政班代码
	//			  "'"+MyTools.fixSql(classname)+"'," +//行政班名称
	//			  "'"+MyTools.fixSql(this.getXX_KCDM())+"'," +//课程代码
	//			  "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程名称
	//			  "'"+MyTools.fixSql(dfjsid)+"'," +//登分教师编号
	//			  "'"+MyTools.fixSql(dfjsname)+"'," +//登分教师姓名
	//			  "'0'," +//打印锁定
	//			  "'post'," +//创建人
	//			  "getdate()," +//创建时间
	//			  "'1')";//状态
	//		
			
			if(db.executeInsertOrUpdate(sql5)){
				this.setMSG("新建成功");
			}else{
				this.setMSG("新建失败");
			}
		}

	}
	
	/**
	 * 编辑机房信息
	 * @date:2016-09-22
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void editComputer() throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String maxNewId="";
		final String classID[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int classnum=0;//同一门课程班级数量
		String classname="";//选修班名称
		String sjxl="";//上课时间序列
		String sjxlck="";
		int xl=0;
		int classtag=0;
		
		//时间序列
		String[] sksjx=this.getXX_SJXL().split(",");
		//System.out.println("sksj:--"+this.getXX_SJXL()+sksjx.length);
		for(int i=0;i<sksjx.length;i++){
			sjxl+=this.getXX_SKRQ()+sksjx[i]+",";
			sjxlck+="'"+this.getXX_SKRQ()+sksjx[i]+"',";
			xl++;
		}
		if(!sjxl.equals("")){
			sjxl=sjxl.substring(0, sjxl.length()-1);
		}
		if(!sjxlck.equals("")){
			sjxlck=sjxlck.substring(0, sjxlck.length()-1);
		}
		
		if(this.getXX_XUEF().equals("")){//学分为空
			this.setXX_XUEF("0");
		}
		if(this.getXX_ZOKS().equals("")){//总人数为空
			this.setXX_ZOKS("0");
		}

		String timeorder="";
		timeorder=checkTeaCls("",this.getXX_SKZC(),this.getXX_WEEK(),this.getXX_CDMC(),"",this.getXX_XNXQ(),"","",sjxl,sjxlck,this.getXX_XXKMXBH());
		
		if(!timeorder.equals("")){
			this.setMSG2("您设置的周次、时间内所选教室已被使用，请重新调整");
		}else{
			//编辑选修课授课计划
			sql5=" update [dbo].[V_机房课表_课程考试明细表] set " +
				 "[学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"'," +
				 "[课程考试名称]='"+MyTools.fixSql(this.getXX_KCMC())+"'," +
				 "[班级名称]='"+MyTools.fixSql(this.getXX_BJMC())+"'," +
				 "[使用教师]='"+MyTools.fixSql(this.getXX_SKJS())+"'," +
				 "[使用教室]='"+MyTools.fixSql(this.getXX_CDMC())+"'," +
				 "[使用周次]='"+MyTools.fixSql(this.getXX_SKZC())+"'," +
				 "[使用时间]='"+MyTools.fixSql(sjxl)+"'," +
				 "[人数]='"+MyTools.fixSql(this.getXX_BJRS())+"'," +
				 "[学分]='"+MyTools.fixSql(this.getXX_XUEF())+"'," +
				 "[总课时]='"+MyTools.fixSql(this.getXX_ZOKS())+"'," +
				 "[创建时间]=getdate() " +
				 "where 机房明细编号='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ";
				
			//编辑选修课主表
	//		String skjsbh="";
	//		String skjsxm="";
	//		String cdyq="";
	//		String cdmc="";
	//		String skzc="";
	//		sql2="select [授课计划明细编号],[授课教师编号],[授课教师姓名],[场地要求],[场地名称],[授课周次] from [dbo].[V_规则管理_选修课授课计划明细表] where [授课计划主表编号]='"+MyTools.fixSql(this.getXX_XXKZBBH())+"' order by 选修班名称 ";
	//		vec2=db.GetContextVector(sql2);
	//		if(vec2!=null&&vec2.size()>0){
	//			for(int i=0;i<vec2.size();i=i+6){
	//				if(vec2.get(i).toString().equals(this.getXX_XXKMXBH())){//授课计划编号相同
	//					skjsbh+=this.getXX_JSBH()+",";
	//					skjsxm+=this.getXX_SKJS()+",";
	//					cdyq+=this.getXX_CDYQ()+",";
	//					cdmc+=this.getXX_CDMC()+",";
	//					skzc+=this.getXX_SKZC()+",";
	//				}else{
	//					skjsbh+=vec2.get(i+1).toString()+",";
	//					skjsxm+=vec2.get(i+2).toString()+",";
	//					cdyq+=vec2.get(i+3).toString()+",";
	//					cdmc+=vec2.get(i+4).toString()+",";
	//					skzc+=vec2.get(i+5).toString()+",";
	//				}
	//			}
	//			skjsbh=skjsbh.substring(0,skjsbh.length()-1);
	//			skjsxm=skjsxm.substring(0,skjsxm.length()-1);
	//			cdyq=cdyq.substring(0,cdyq.length()-1);
	//			cdmc=cdmc.substring(0,cdmc.length()-1);
	//			skzc=skzc.substring(0,skzc.length()-1);
	//		}
	//		
	//		sql5+="update [dbo].[V_规则管理_选修课授课计划主表] set " +
	//			  "[学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"'," +
	//			  "[课程类型]='"+MyTools.fixSql(this.getXX_KCLX())+"'," +
	//			  "[授课教师编号]='"+skjsbh+"'," +
	//			  "[授课教师姓名]='"+skjsxm+"'," +
	//			  "[节数]='"+xl+"'," +
	//			  "[场地要求]='"+cdyq+"'," +
	//			  "[场地名称]='"+cdmc+"'," +
	//			  "[授课周次]='"+skzc+"'," +
	//			  "[授课周次详情]='"+skzc+"'," +
	//			  "[创建时间]=getdate() " +
	//			  "where [授课计划主表编号]='"+MyTools.fixSql(this.getXX_XXKZBBH())+"' ";
	//		
	//		//编辑科目课程信息表
	//		sql3="select [学年学期名称] from [dbo].[V_规则管理_学年学期表] where [学年学期编码] = '"+MyTools.fixSql(this.getXX_XNXQ())+"' ";
	//		vec3=db.GetContextVector(sql3);
	//		
	//		sql5+="update [dbo].[V_成绩管理_科目课程信息表] set " +
	//			  "[学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"'," +
	//			  "[学年学期名称]='"+MyTools.fixSql(vec3.get(0).toString())+"'," +
	//			  "[年级代码]='"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"1'," +
	//			  "[年级名称]='"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"级'," +
	//			  "[课程类型]='"+MyTools.fixSql(this.getXX_KCLX().replaceAll("0", ""))+"'," +
	//			  "[创建时间]=getdate() " +
	//			  " where [科目编号] in (select [科目编号] from [dbo].[V_成绩管理_登分教师信息表] where [相关编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ) ";	
	//		
	//		//编辑登分教师信息表
	//		String[] dfjsbh=this.getXX_JSBH().split("\\+");
	//		String[] dfjsxm=this.getXX_SKJS().split("\\+");
	//		String dfjsid="";
	//		String dfjsname="";
	//		for(int j=0;j<dfjsbh.length;j++){
	//			dfjsid+="@"+dfjsbh[j]+"@,";
	//			dfjsname+="@"+dfjsxm[j]+"@,";
	//		}
	//		dfjsid=dfjsid.substring(0, dfjsid.length()-1);
	//		dfjsname=dfjsname.substring(0, dfjsname.length()-1);
	//		sql5+="update [dbo].[V_成绩管理_登分教师信息表] set " +
	//			  "[登分教师编号]='"+MyTools.fixSql(dfjsid)+"',[登分教师姓名]='"+MyTools.fixSql(dfjsname)+"',[创建时间]=getdate() " +
	//			  "where [相关编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ";
		
			if(db.executeInsertOrUpdate(sql5)){
				this.setMSG("编辑成功");
			}else{
				this.setMSG("编辑失败");
			}
		}
	}
	
	/**
	 * 删除机房信息
	 * @date:2016-08-01
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void deleteComputer(String skjhmxbh) throws SQLException {
		String sql = "";
		Vector vec = null;

//		sql2+=" delete from [dbo].[V_成绩管理_科目课程信息表] where [科目编号] in (select [科目编号] from [dbo].[V_成绩管理_登分教师信息表] where [相关编号]='"+MyTools.fixSql(skjhmxbh)+"' ) ";
//		sql2+=" delete from [dbo].[V_成绩管理_登分教师信息表] where [相关编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
		sql=" delete from [dbo].[V_机房课表_课程考试明细表] where [机房明细编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}			
	}
	
	//XX考试形式combobox
	public Vector XX_KSXSCombobox() throws SQLException{
		Vector vec = null;	
		//String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName,'2' as px  FROM V_规则管理_学年学期表 where 1=1) e order by e.px,e.comboValue desc ";
		String sql="select 0 as comboValue,'请选择' as comboName" +
				" union all	" +
				" select 编号," +
				" case" +
				" when 考试形式='' then '请选择'" +
				" else 考试形式" +
				" end " +
				" as 考试形式" +
				" from dbo.V_考试形式 where 编号!=0";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//XX考试形式combobox
	public Vector XX_KBMXBCombobox() throws SQLException{
		Vector vec = null;	
		String sql="select 0 as comboValue,'请选择' as comboName" +
					" union all	" +
					" select 编号," +
					" case" +
					" when 考试形式='' then '请选择'" +
					" else 考试形式" +
					" end " +
					" as 考试形式" +
					" from dbo.V_考试形式 where 编号!=0";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//第一志愿combobox
	public Vector first_APPCombobox(String iUSERCODE,String term,String firsel,String secsel,String thisel) throws SQLException{
		Vector vec = null;	
		String sql="";
		String sql2="";
		
		sql="select '' as comboValue,'请选择' as comboName ";
		
		if(term.equals("")){//可以选课
			
		}else{
			String sqlbj="select a.行政班代码,b.系部代码  from V_学生基本数据子类 a,V_基础信息_班级信息表 b where a.行政班代码=b.班级编号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"'";
			Vector vecbj=db.GetContextVector(sqlbj);
			if(vecbj!=null&&vecbj.size()>0){
				String xbdm=vecbj.get(1).toString();
				if(!term.equals("")){
						sql2 = " select a.授课计划明细编号,a.选修班名称 " +
							" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b " +
							" where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(term)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') " ;

						String sql14=" select 学号 from V_选修课_补选学生名单 where 学年学期编码='"+MyTools.fixSql(term)+"' ";
						Vector vec14=db.GetContextVector(sql14);
						int tt=0;
						if(vec14!=null&&vec14.size()>0){
							for(int j=0;j<vec14.size();j++){
								if(iUSERCODE.equals(vec14.get(j).toString())){
									tt=1;
								}
							}
						}

						if(tt==1){
							sql2 = " select a.授课计划明细编号 as comboValue,a.选修班名称  as comboName" +
									" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b " +
									" where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(term)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') "; 

						}
						sql=sql+" union "+sql2;
				}
			}
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//第二志愿combobox
	public Vector second_APPCombobox(String iUSERCODE,String term,String firsel,String secsel,String thisel) throws SQLException{
			Vector vec = null;	
			String sql="";
			String sql2="";
			
			sql="select '' as comboValue,'请选择' as comboName ";
			
			if(term.equals("")){//可以选课
				
			}else{
				String sqlbj="select a.行政班代码,b.系部代码  from V_学生基本数据子类 a,V_基础信息_班级信息表 b where a.行政班代码=b.班级编号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"'";
				Vector vecbj=db.GetContextVector(sqlbj);
				if(vecbj!=null&&vecbj.size()>0){
					String xbdm=vecbj.get(1).toString();
					if(!term.equals("")){
							sql2 = " select a.授课计划明细编号,a.选修班名称 " +
								" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b " +
								" where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(term)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') "; 
							if(!firsel.equals("")){
								sql2+=" and 授课计划明细编号!='"+firsel+"' ";
							}
							
							String sql14=" select 学号 from V_选修课_补选学生名单 where 学年学期编码='"+MyTools.fixSql(term)+"' ";
							Vector vec14=db.GetContextVector(sql14);
							int tt=0;
							if(vec14!=null&&vec14.size()>0){
								for(int j=0;j<vec14.size();j++){
									if(iUSERCODE.equals(vec14.get(j).toString())){
										tt=1;
									}
								}
							}

							if(tt==1){
								sql2 = " select a.授课计划明细编号 as comboValue,a.选修班名称  as comboName" +
										" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b " +
										" where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(term)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') "; 
								if(!firsel.equals("")){
									sql2+=" and 授课计划明细编号!='"+firsel+"' ";
								}

							}
							sql=sql+" union "+sql2;
					}
				}
			}
			
			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
	}
		
	//第三志愿combobox
	public Vector third_APPCombobox(String iUSERCODE,String term,String firsel,String secsel,String thisel) throws SQLException{
			Vector vec = null;	
			String sql="";
			String sql2="";
			
			sql="select '' as comboValue,'请选择' as comboName ";
			
			if(term.equals("")){//可以选课
				
			}else{
				String sqlbj="select a.行政班代码,b.系部代码  from V_学生基本数据子类 a,V_基础信息_班级信息表 b where a.行政班代码=b.班级编号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"'";
				Vector vecbj=db.GetContextVector(sqlbj);
				if(vecbj!=null&&vecbj.size()>0){
					String xbdm=vecbj.get(1).toString();
					if(!term.equals("")){
							sql2 = " select a.授课计划明细编号,a.选修班名称 " +
								" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b " +
								" where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(term)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') "; 
							if(!firsel.equals("")){
								sql2+=" and 授课计划明细编号!='"+firsel+"' ";
							}
							if(!secsel.equals("")){
								sql2+=" and 授课计划明细编号!='"+secsel+"' ";
							}
							
							String sql14=" select 学号 from V_选修课_补选学生名单 where 学年学期编码='"+MyTools.fixSql(term)+"' ";
							Vector vec14=db.GetContextVector(sql14);
							int tt=0;
							if(vec14!=null&&vec14.size()>0){
								for(int j=0;j<vec14.size();j++){
									if(iUSERCODE.equals(vec14.get(j).toString())){
										tt=1;
									}
								}
							}

							if(tt==1){
								sql2 = " select a.授课计划明细编号 as comboValue,a.选修班名称  as comboName" +
										" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b " +
										" where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' and b.学年学期编码='"+MyTools.fixSql(term)+"' and (CHARINDEX('"+xbdm+"',a.可报名专业编号)>0 or a.可报名专业编号='') "; 
								if(!firsel.equals("")){
									sql2+=" and 授课计划明细编号!='"+firsel+"' ";
								}
								if(!secsel.equals("")){
									sql2+=" and 授课计划明细编号!='"+secsel+"' ";
								}
							}
							sql=sql+" union "+sql2;
					}
				}
			}
			
			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
	}
	
	
	//检查不能安排的时间
	public String checkTeaCls(String jsxm,String skzcxq,String weeks,String cdyq,String iKeyCode,String termid,String classId,String jsbh,String sjxl,String sjxlck,String jfkbmxbh) throws SQLException {
			Vector vec = new Vector();
			Vector vec0 = null;
			Vector vec2 = null;
			Vector vec4 = null;
			Vector vec5 = null;
			Vector vec6 = null;
			Vector vec8 = null;
			String sql = "";
			String sql2 = "";
			String sql0 = "";
			String sql4 = "";
			String sql5 = "";
			String sql6 = "";
			String sql7 = "";
			String sql8 = "";
			String times="";//不能排的时间序列位置
			String sames="";//时间序列对应的冲突信息
			String rooms="";//班级相同的冲突信息
			String jinpai="";//教师，场地禁排
			ArrayList teaarray=new ArrayList();
			ArrayList wekarray=new ArrayList();
			ArrayList cdyarray=new ArrayList();
			//查询所有要排课老师和他们的上课周次
			//System.out.println("教师周次场地：------"+cdyq);
			String[] teac=jsbh.split("\\&");
			String[] week=skzcxq.split("\\&");
			String[] room=cdyq.split("\\&");
			
			//Map<String, String> map=new HashMap<String, String>();   
			for(int i=0;i<teac.length;i++){
				teaarray.add(teac[i]);  
			}
			for(int k=0;k<week.length;k++){
				wekarray.add(week[k]);
			}
			for(int l=0;l<room.length;l++){
				cdyarray.add(room[l]);
			}
			
			//查询所有要排课的老师
			String teachers=jsbh.replaceAll("\\+", "\\&");
			
			//课程表明细详情表与授课计划比较
//			String sqlall="";
//			sql="select a.时间序列,"+
//				"stuff((select '|'+b.授课教师编号 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 授课教师编号,"+
//				"stuff((select '|'+b.授课教师姓名 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 授课教师姓名,"+
//				"stuff((select '|'+b.授课周次详情 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 授课周次详情,"+
//				"stuff((select '|'+b.实际场地编号 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 实际场地编号,"+
//				"stuff((select '|'+b.实际场地名称 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 实际场地名称,"+
//				"a.行政班名称,a.课程名称 from V_排课管理_课程表明细详情表 a ";
//				sql2+=" where 授课教师姓名 != '' and a.行政班代码!='"+MyTools.fixSql(classId)+"' and a.学年学期编码='"+MyTools.fixSql(termid)+"' and a.时间序列 in ("+sjxlck+")";
//			sql3=" group by a.时间序列,a.学年学期编码,a.行政班代码,a.行政班名称,a.课程名称 ";
//			sql7=" union all SELECT 上课时间 = substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number),a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,a.课程名称 " +
//					" FROM  (select a.上课时间,a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,b.课程名称 from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.学年学期编码='"+MyTools.fixSql(termid)+"') a JOIN" +
//					" master..spt_values b ON b.type = 'P' " +
//					" WHERE substring(',' + a.上课时间, b.number, 1) = ',' ";
//			sqlall=sql+sql2+sql3+sql7;
//			vec=db.GetContextVector(sqlall);
			
			
			//查询课程表周详情表
			sql0="SELECT [时间序列],[授课教师编号],[授课教师姓名],[授课周次],[场地编号],[场地名称],[行政班名称],[课程名称],[授课计划明细编号] " +
				"FROM [dbo].[V_排课管理_课程表周详情表] " +
				"where 学年学期编码='"+MyTools.fixSql(termid)+"' and 行政班代码!='"+MyTools.fixSql(classId)+"' and 授课教师姓名 != '' and [场地编号]!='' " +
				"order by [时间序列],[行政班名称],[课程名称],convert(int,授课周次)";
			vec0=db.GetContextVector(sql0);
			if(vec0!=null&&vec0.size()>0){
				String skzc="";
				for(int i=0;i<vec0.size()-9;){
					skzc=vec0.get(i+3).toString();			
						while(i<(vec0.size()-9)&&vec0.get(i+8).toString().equals(vec0.get(i+8+9).toString())&&vec0.get(i).toString().equals(vec0.get(i+9).toString())){//授课计划编号相等+时间序列相等					
							skzc+="#"+vec0.get(i+3+9).toString();
							//System.out.println(i+":--"+skzc);
							i=i+9;
						}
						skzcxq=merge(skzc);
						String[] skjsbh5=vec0.get(i+1).toString().split("\\+");
						String[] skjsxm5=vec0.get(i+2).toString().split("\\+");
						for(int m=0;m<skjsbh5.length;m++){
							vec.add(vec0.get(i).toString());//时间序列
							vec.add(skjsbh5[m]);//授课教师编号
							vec.add(skjsxm5[m]);//授课教师姓名
							vec.add(skzcxq);//授课周次
							vec.add(vec0.get(i+4).toString());//场地编号
							vec.add(vec0.get(i+5).toString());//场地名称
							vec.add(vec0.get(i+6).toString());//行政班名称
							vec.add(vec0.get(i+7).toString());//课程名称
						}
						i=i+9;
				}					
			}
					
//					for(int i=0;i<vec.size();i=i+8){
//						System.out.println(vec.get(i).toString()+"|"+vec.get(i+1).toString()+"|"+vec.get(i+2).toString()+"|"+vec.get(i+3).toString()+"|"+vec.get(i+4).toString()+"|"+vec.get(i+5).toString()+"|"+vec.get(i+6).toString()+"|"+vec.get(i+7).toString());
//					}
					
			//查询选修课
			sql2="SELECT 上课时间 = substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number),a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,a.课程名称 " +
				" FROM  (select a.上课时间,a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,b.课程名称 from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.学年学期编码='"+MyTools.fixSql(termid)+"') a JOIN" +
				" master..spt_values b ON b.type = 'P' " +
				" WHERE     substring(',' + a.上课时间, b.number, 1) = ',' ";
			vec2=db.GetContextVector(sql2);
			if(vec2!=null&&vec2.size()>0){
				for(int m=0;m<vec2.size();m=m+8){
					vec.add(vec2.get(m).toString());//时间序列
					vec.add(vec2.get(m+1).toString());//授课教师编号
					vec.add(vec2.get(m+2).toString());//授课教师姓名
					vec.add(vec2.get(m+3).toString());//授课周次
					vec.add(vec2.get(m+4).toString());//场地编号
					vec.add(vec2.get(m+5).toString());//场地名称
					vec.add(vec2.get(m+6).toString());//行政班名称
					vec.add(vec2.get(m+7).toString());//课程名称
				}
			}
					
			if(vec!=null&vec.size()>0){
				for(int i=0;i<vec.size();i=i+8){
					//System.out.println("时间序列：--"+vec.get(i).toString()+" 授课教师姓名:--"+vec.get(i+1).toString()+" 授课周次详情:--"+vec.get(i+2).toString()+" 场地要求:--"+vec.get(i+3).toString());
					//for (Map.Entry<String, String> entry : map.entrySet()) {  
					for (int k=0;k<wekarray.size();k++) { 
						//if(vec.get(i+1).toString().indexOf(teaarray.get(k).toString())>-1){//存在
//							String t1=vec.get(i+1).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//							String t2=vec.get(i+2).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//							String t3=vec.get(i+3).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//							String t4=vec.get(i+4).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//							String t5=vec.get(i+5).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//											
//							String[] selteabh2=t1.split("\\&");
//							String[] selteaname2=t2.split("\\&");
//							String[] selwektime2=t3.split("\\&");
//							String[] selclsbh2=t4.split("\\&");
//							String[] selclsname2=t5.split("\\&");
							//for(int m=0;m<selteaname2.length;m++){
									
							//比较周次是否有相同
							int tag1=compareweek(wekarray.get(k).toString(),vec.get(i+3).toString(),weeks);
							if(tag1==1){//有重复	
								//比较场地是否有相同
								int tag2=compareroom(cdyarray.get(k).toString(),vec.get(i+5).toString());
								if(tag2==1){//场地相同
									times+=vec.get(i).toString()+",";
									sames+=vec.get(i).toString()+","+vec.get(i+2).toString()+","+vec.get(i+6).toString()+","+vec.get(i+5).toString()+","+vec.get(i+3).toString()+",";;
								}else{
															
								}						
							}else{//没有重复
												
							}
							//}			
						//}
			        }
				}	
			}
			
			//System.out.println("课程表明细：----------------"+times);
			
			//固排禁排表查询禁排的时间序列
			//班级,教师，场地禁排
			sql5=" select a.时间序列,a.行政班代码,a.禁排类型,case when a.禁排类型='bj' then c.行政班名称 when a.禁排类型='js' then b.姓名 when a.禁排类型='cd' then d.教室名称 else '' end as 名称 from dbo.V_规则管理_固排禁排表 a left join dbo.V_教职工基本数据子类 b on a.行政班代码=b.工号 left join dbo.V_学校班级数据子类 c on a.行政班代码=c.行政班代码 left join dbo.V_教室数据类 d on a.行政班代码=d.教室编号   where 学年学期编码='"+MyTools.fixSql(termid)+"' and 类型='3' " ;
			vec5=db.GetContextVector(sql5);
			if(vec5!=null&&vec5.size()>0){
				for(int i=0;i<vec5.size();i=i+4){
					if(vec5.get(i+2).toString().equals("bj")){
						if(vec5.get(i+1).toString().equals(classId)){
							times+=vec5.get(i).toString()+",";
						}
					}else if(vec5.get(i+2).toString().equals("js")){
						String[] teabhid=jsbh.split("\\&");
						for(int j=0;j<teabhid.length;j++){
							String[] teabhid2=teabhid[j].split("\\+");
							for(int k=0;k<teabhid2.length;k++){
								if(teabhid2[k].equals(vec5.get(i+1).toString())){
									times+=vec5.get(i).toString()+",";
									jinpai+=vec5.get(i).toString()+","+vec5.get(i+3).toString()+","+vec5.get(i+2).toString()+",";
								}
							}
						}
					}else if(vec5.get(i+2).toString().equals("cd")){
						String[] cdbhid=cdyq.split("\\&");
						for(int j=0;j<cdbhid.length;j++){ 
							if(cdbhid[j].indexOf(vec5.get(i+1).toString())>-1){
								times+=vec5.get(i).toString()+",";
								jinpai+=vec5.get(i).toString()+","+vec5.get(i+3).toString()+","+vec5.get(i+2).toString()+",";
							}
						}	
					}		
				}
			}
			
			//检查机房课表有没有冲突
			sql6="SELECT [使用教室],[使用周次],[使用时间] FROM [dbo].[V_机房课表_课程考试明细表] where [学年学期编码]='"+MyTools.fixSql(termid)+"' and [机房明细编号]!='"+jfkbmxbh+"' ";
			vec6=db.GetContextVector(sql6);
			if(vec6!=null&&vec6.size()>0){
				for(int i=0;i<vec6.size();i=i+3){ 
					//判断时间序列是否有相同
					String[] sysj=vec6.get(i+2).toString().split(",");
					String[] sjxlsel=sjxl.split(",");
					int sjxlflag=0;
					for(int m=0;m<sysj.length;m++){
						for(int n=0;n<sjxlsel.length;n++){
							if(sysj[m].equals(sjxlsel[n])){//时间序列有相同
								sjxlflag=1;
							}
						}
					}
					if(sjxlflag==1){
						for (int k=0;k<wekarray.size();k++) { 
							//if(vec.get(i+1).toString().indexOf(teaarray.get(k).toString())>-1){//存在
								String t1=vec6.get(i).toString();
								String t2=vec6.get(i+1).toString();
								
								//比较周次是否有相同
								int tag1=compareweek(wekarray.get(k).toString(),t2,weeks);
								if(tag1==1){//有重复
									//比较场地是否有相同
									int tag2=compareroom(cdyarray.get(k).toString(),t1);
									if(tag2==1){//场地相同
										times+=vec6.get(i+2).toString()+",";
										//System.out.println("er---"+vec6.get(i).toString()+"|"+vec6.get(i+1).toString()+"|"+vec6.get(i+2).toString());
										//sames+=vec.get(i).toString()+","+selteaname2[m]+","+vec.get(i+6).toString()+","+selclsname2[m]+","+selwektime2[m]+",";;
									}else{
														
									}
								}else{//没有重复
											
								}			
							//}
				        }
					}
				}
			}
			
			//教师，场地禁排 
//			sql8=" select a.时间序列,b.授课教师编号,b.场地名称 from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and a.学年学期编码='"+MyTools.fixSql(termid)+"' and a.授课计划明细编号<>'' and 类型='3' " ;
//			vec8=db.GetContextVector(sql8);
//			for(int i=0;i<vec8.size();i=i+3){
//				if(jsbh.indexOf(vec8.get(i+1).toString())>-1||cdyq.indexOf(vec8.get(i+2).toString())>-1){
//					times+=vec8.get(i).toString()+",";
//					jinpai+=vec8.get(i).toString()+","+vec8.get(i+1).toString()+","+vec8.get(i+2).toString()+",";
//				}
//			}
			
	        //查询可用教室
	        //System.out.println("预排教室：--"+cdyq);
//	        String yysjxl="";//保存已占用时间序列
//	        for (int k=0;k<cdyarray.size();k++) {
//	        	if(cdyarray.get(k).toString().equals("1")||cdyarray.get(k).toString().equals("5")){
//		        	
//	        	}else{
//	        		String sqlkyjs="select 教室编号 from dbo.V_教室数据类 where [教室类型代码] in (select [教室类型代码] from dbo.V_教室数据类 where 教室编号='"+cdyarray.get(k).toString()+"')";
//		        	Vector veckyjs=db.GetContextVector(sqlkyjs);
//		        	if(veckyjs!=null&&veckyjs.size()>0){
//		        		for(int i=0;i<veckyjs.size();i++){
//		        			String sqlsjxl="select 时间序列 from dbo.V_规则管理_固排禁排表 where [预设场地编号]='"+veckyjs.get(i).toString()+"' and 学年学期编码='"+termid+"' " +
//		        						   "union select 时间序列 from dbo.V_排课管理_课程表明细详情表 where [实际场地编号]='"+veckyjs.get(i).toString()+"' and 学年学期编码='"+termid+"'";
//		        			Vector vecsjxl=db.GetContextVector(sqlsjxl);
//		        			yysjxl+="@"+veckyjs.get(i).toString()+"#";
//		        			for(int j=0;j<vecsjxl.size();j++){
//		        				yysjxl+=vecsjxl.get(j).toString()+",";
//		        			}
//		        		}
//		        	}
//	        	}
//	        }
	       
//	        String[] sameinfo=sames.split(",");
//	        for(int i=0;i<sameinfo.length;i=i+5){
//	        	System.out.println("same:--"+sameinfo[i]+"|"+sameinfo[i+1]+"|"+sameinfo[i+2]+"|"+sameinfo[i+3]+"|"+sameinfo[i+4]);
//	        }
	        
			if(!times.equalsIgnoreCase("")){
				times=times.substring(0,times.length()-1);
			}
			if(!sames.equalsIgnoreCase("")){
				sames=sames.substring(0,sames.length()-1);
			}
			if(!rooms.equalsIgnoreCase("")){
				rooms=rooms.substring(0,rooms.length()-1);
			}
			if(!jinpai.equalsIgnoreCase("")){
				jinpai=jinpai.substring(0,jinpai.length()-1);
			}
			
			return times;
			
			//this.setMSG(times);	
			//this.setMSG2(sames);
			//this.setMSG3(rooms);
			//this.setMSG4(yysjxl);
			//this.setMSG5(jinpai);
	}
	
	//合并周次
	public String merge(String skzc) throws SQLException {
		String skzcxq="";
		int tag=0;
		if(skzc.indexOf("#")>-1){
			String[] skzc2=skzc.split("#");
			for(int i=0;i<skzc2.length-1;i++){
				if(Integer.parseInt(skzc2[i])+1==Integer.parseInt(skzc2[i+1])){
									
				}else{
					tag=1;
				}
			}
			if(tag==1){
				skzcxq=skzc;
			}else{
				skzcxq=skzc2[0]+"-"+skzc2[skzc2.length-1];
			}
		}else{//单个周
			skzcxq=skzc;
		}
		return skzcxq;
	}
	
	//比较教师是否有相同
	public int compareteacher(String arrteach,String selteach) throws SQLException {
			int result=2;//不相同	
				//System.out.println("教师："+arrteach+"---"+selteach);	
				String[] arrteach2=arrteach.split("\\&");
				String[] selteach2=selteach.split("\\&");
				for(int m=0;m<arrteach2.length;m++){
					for(int n=0;n<selteach2.length;n++){
						String[] arrteach3=arrteach2[m].split("\\+");
						String[] selteach3=selteach2[n].split("\\+");
						for(int i=0;i<arrteach3.length;i++){
							for(int j=0;j<selteach3.length;j++){
								if(arrteach3[i].equalsIgnoreCase(selteach3[j])){
									result=1;
								}else{
										
								}
							}
						}
					}
				}
				return result;
	}
			
	//比较周次是否有相同
	public int compareweek(String arrweek,String selweek,String weeks) throws SQLException {
				int result=2;
				
				String week1="";
				String week2="";
				if(arrweek.equalsIgnoreCase("odd")){
					for(int i=1;i<=Integer.parseInt(weeks);i++){
						if(i%2!=0){
							week1+=i+"#";
						}	
					}
					week1=week1.substring(0,week1.length()-1);
				}else if(arrweek.equalsIgnoreCase("even")){
					for(int i=1;i<=Integer.parseInt(weeks);i++){
						if(i%2==0){
							week1+=i+"#";
						}	
					}
					week1=week1.substring(0,week1.length()-1);
				}else if(arrweek.indexOf("\\#")>-1){
					
				}else if(arrweek.indexOf("-")>-1){
					String[] wek=arrweek.split("-");
					for(int i=Integer.parseInt(wek[0]);i<=Integer.parseInt(wek[1]);i++){
						week1+=i+"#";	
					}
					week1=week1.substring(0,week1.length()-1);
				}else{
					week1=arrweek;
				}
				
				if(selweek.equalsIgnoreCase("odd")){
					for(int i=1;i<=Integer.parseInt(weeks);i++){
						if(i%2!=0){
							week2+=i+"#";
						}	
					}
					week2=week2.substring(0,week2.length()-1);
				}else if(selweek.equalsIgnoreCase("even")){
					for(int i=1;i<=Integer.parseInt(weeks);i++){
						if(i%2==0){
							week2+=i+"#";
						}	
					}
					week2=week2.substring(0,week2.length()-1);
				}else if(selweek.indexOf("\\#")>-1){
					
				}else if(selweek.indexOf("-")>-1){
					//System.out.println("selweek:----"+selweek);
					String[] wek=selweek.split("-");
					for(int i=Integer.parseInt(wek[0]);i<=Integer.parseInt(wek[1]);i++){
						week2+=i+"#";	
					}
					week2=week2.substring(0,week2.length()-1);
				}else{
					week2=selweek;
				}
				
				String[] weeks1=week1.split("\\#");
				String[] weeks2=week2.split("\\#");
				for(int i=0;i<weeks1.length;i++){
					for(int j=0;j<weeks2.length;j++){
						//System.out.println("week1:"+weeks1[i]);
						//System.out.println("week2:"+weeks2[j]);
						if(weeks2[j].equals(weeks1[i])){//存在
							result=1;
						}
					}
				}
//				if(result==1){
//					System.out.println("周次："+arrweek+"---"+selweek);	
//				}
				return result;
	}
			
	//比较教室是否有相同
	public int compareroom(String arrcdyq,String selcdyq) throws SQLException {
				int result=2;	
				//System.out.println("场地："+arrcdyq+"---"+selcdyq);	
				if(arrcdyq.length()==1){//授课计划场地类型选的1或5，普通教师或多媒体教室
					
				}else{//具体教室编号
					String[] arrcdyq2=arrcdyq.split("\\&");
					String[] selcdyq2=selcdyq.split("\\&");
					for(int m=0;m<arrcdyq2.length;m++){
						for(int n=0;n<selcdyq2.length;n++){
							String[] arrcdyq3=arrcdyq2[m].split("\\+");
							String[] selcdyq3=selcdyq2[n].split("\\+");
							for(int i=0;i<arrcdyq3.length;i++){
								for(int j=0;j<selcdyq3.length;j++){
									if(arrcdyq3[i].equalsIgnoreCase(selcdyq3[j])){
										if(selcdyq3[j].equals("0000")||selcdyq3[j].equals("0001")||selcdyq3[j].equals("0002")){
											
										}else{
											result=1;
										}
									}else{
										
									}
								}
							}
						}
					}
				}
//				if(result==1){
//					System.out.println("场地："+arrcdyq+"---"+selcdyq);	
//				}
				return result;
	}
	
	/**
	 *	计算学分
	 * @date:2016-11-09
	 * @author: zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void calculateCredits(String iUSERCODE) throws SQLException {
		String sql = "";
		Vector vec = new Vector();
		Vector sourceVec=new Vector();
		Vector xk= new Vector();
		Vector xkMsgVec= new Vector();
		
		double xuefen=0;
		double jisuan=0;
		boolean flag=false;
		sql="select b.专业代码,c.专业名称" +
				" from  V_学生基本数据子类 as a" +
				" left join dbo.V_学校班级数据子类 as b on a.行政班代码=b.行政班代码" +
				" left join dbo.V_专业基本信息数据子类 as c on c.专业代码=b.专业代码" +
				" where a.学号='"+MyTools.fixSql(iUSERCODE)+"'";
		vec=db.GetContextVector(sql);
		if(vec.get(0)!="580405"&&vec.get(0)!="630702"&&vec.get(0)!="520104"&&vec.get(0)!="600209"&&vec.get(0)!="610202_1"){
			xuefen=6.0;
		}else{
			xuefen=4.0;
		}
		sql="SELECT convert(float,sum(b.学分))" +
				" FROM dbo.V_规则管理_学生选修课关系表 a,dbo.V_规则管理_选修课授课计划明细表 b" +
				" where a.授课计划明细编号=b.授课计划明细编号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"'";
		sourceVec=db.GetContextVector(sql);
		
		sql="select a.选修班名称,a.学分 " +
				" from V_规则管理_选修课授课计划明细表 as a " +
				" left join dbo.V_规则管理_学生选修课关系表 as b on a.授课计划明细编号=b.授课计划明细编号 " +
				" where b.学号='"+MyTools.fixSql(iUSERCODE)+"'";
		xk=db.GetContextVector(sql); ;
		if(xk != null && xk.size() > 0){
			for (int i = 0; i < xk.size(); i++) {
				xkMsgVec.add(xk.get(i));
			}
		}
		if(xuefen==6.0){
			
			if(Float.parseFloat(MyTools.StrFiltr(sourceVec.get(0)))>=6.0){
				//this.setMSG("需要"+MyTools.StrFiltr(sourceVec.get(0))+"学分，已达标");
				jisuan=xuefen;
				flag=true;
			}else{
				jisuan=6.0-Float.parseFloat(MyTools.StrFiltr(sourceVec.get(0)));
				//this.setMSG("需要"+MyTools.StrFiltr(sourceVec.get(0))+"学分，还差"+jisuan+"学分");
			}
		}
		if(xuefen==4.0){
			if(Float.parseFloat(MyTools.StrFiltr(sourceVec.get(0)))>=4.0){
				jisuan=xuefen;
				flag=true;
				//this.setMSG("需要"+MyTools.StrFiltr(sourceVec.get(0))+"学分，已达标");
			}else{
				jisuan=4.0-Float.parseFloat(MyTools.StrFiltr(sourceVec.get(0)));
				//this.setMSG("需要"+MyTools.StrFiltr(sourceVec.get(0))+"学分，还差"+jisuan+"学分");
			}
		}
		String xkmsg = "";
		String xfmsg="";
		if(xkMsgVec!=null && xkMsgVec.size()>0){
			xkmsg = "已选课程如下：@"+xkMsgVec.toString().substring(1,xkMsgVec.toString().length()-1);
		}
		if(flag==true){
			xfmsg="共计 "+sourceVec.get(0)+" 分"+",已达标";
		}else{
			xfmsg="共计 "+sourceVec.get(0)+" 分"+",达标还需 "+jisuan+" 分";
		}
		String str = "{\"sourceInfo\":\""+xfmsg+"\",\"subInfo\":\""+xkmsg+"\"}";
		this.setMSG2(str);
	}
	
	/**
	 * 选修课确认
	 * @date:2017-10-26
	 * @author: lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void checkElectiveSubmit() throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="select [选修课确认] from dbo.V_基础信息_选修课时间表 ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			if(vec.get(0).toString().equals("0")){
				this.setMSG("0");		
			}else{
				this.setMSG("本学期选修课已经确认");
			}
		}
		
	}
	
	public void electiveSubmit() throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql="update V_基础信息_选修课时间表 set [选修课确认]='1' ";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("确认完成");
		}else{
			this.setMSG("确认失败");
		}				
	}
	
	
	  /**
		 * ExportExcelXXKSubject 导出课程信息
		 * 
		 * @author zhaixuchao
		 * @date:2016-11-8
		 * @return savePath 下载路径
		 * @throws SQLException
		 */
		public String ExportExcelXXKSubject(String SC_XNXQ,String xnxqname)throws SQLException {
			String sql = "";
			Vector vec = null;
			Vector titleVec = new Vector();
			Vector wzcjVec = new Vector();
			String tempStr = "";
			String savePath = "";

			titleVec.add("开课班级");
			titleVec.add("课程名称");
			titleVec.add("实际人数");
			titleVec.add("上课周");
			titleVec.add("上课日");
			titleVec.add("教师");
			titleVec.add("教室");
			
			int xnxqmc=Integer.parseInt(xnxqname.substring(2, 4))-1;
			// 读取班级学生成绩信息
			sql = "select a.选修班名称,b.课程名称," +
					"(select COUNT(*) from  dbo.V_规则管理_学生选修课关系表 where [授课计划明细编号]=a.授课计划明细编号) as [报名人数],a.授课周次,a.上课时间,a.授课教师姓名,a.场地名称" +
					" FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.课程类型!='01' ";
			if(!SC_XNXQ.equals("")){
				sql+=" and b.学年学期编码='"+SC_XNXQ+"' ";
			}
			sql+=" order by b.课程名称 ";
			
			vec = db.GetContextVector(sql);
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
			savePath += "/" + xnxqname +xnxqmc+"级任意选修课情况表"+ year+ ((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+ (date < 10 ? "0" + date : date) + ".xls";
			try {
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				// 设置列宽
				for (int i = 0; i < titleVec.size(); i++) {
					if(i==0||i==1||i==4){
						wsheet.setColumnView(i, 40);
					}
					else{
						wsheet.setColumnView(i, 15);
					}
				}
				// 设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
				//加的标题
				wsheet.mergeCells(0,0,6,0);
				content = new Label(0, 0, xnxqname +xnxqmc+"级任意选修课情况表",contentStyle);
				wsheet.addCell(content);
				// 设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 13,WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
				
				for (int i = 0; i < titleVec.size(); i++) {
					// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
					// 在Label对象的子对象中指明单元格的位置和内容
					content = new Label(i, 1, MyTools.StrFiltr(titleVec.get(i)),contentStyle);
					// 将定义好的单元格添加到工作表中
					//把教室改为红色
					/*if(i==6){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 13,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
						contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
						content = new Label(i, 1, MyTools.StrFiltr(titleVec.get(i)),contentStyle);
						wsheet.addCell(content);
						break;
					}*/
					wsheet.addCell(content);
				}
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
				contentStyle.setShrinkToFit(true);//字体大小自适应
				//绑定所有数据
				for (int i = 0, k = 1; i < vec.size(); i += titleVec.size(), k++) {
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
					contentStyle.setShrinkToFit(true);//字体大小自适应
					for (int j = 0; j < titleVec.size(); j++) {
						tempStr = MyTools.StrFiltr(vec.get(i+j));

						if(j == 4){
							if(vec.get(i+j)==""||MyTools.StrFiltr(vec.get(i+j)).equalsIgnoreCase("")){
								tempStr="";
							}
							else{
								String scoreArray[] = tempStr.split(",", -1);
								int week=Integer.parseInt(scoreArray[0].substring(0,2));
								String week2="";
								if(week==01){
									week2="星期一";
								}else if(week==02){
									week2="星期二";
								}else if(week==03){
									week2="星期三";
								}else if(week==04){
									week2="星期四";
								}else if(week==05){
									week2="星期五";
								}else{
									week2="";
								}
								int apm=Integer.parseInt(scoreArray[0].substring(2,4));
								String apm2="";
								if(apm<5){
									apm2="上午";
								}else if(apm>6){
									apm2="下午";
								}
								String num=(Integer.parseInt(scoreArray[0].substring(2,4))-2)+"-"+(Integer.parseInt(scoreArray[scoreArray.length-1].substring(2,4))-2);
								
								tempStr =week2+apm2+"第"+num+"节课";
							}
						}
						//把教室改为红色
						/*if(j==6){
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
							contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
							contentStyle.setShrinkToFit(true);//字体大小自适应
							content = new Label(j, k+1,tempStr + "", contentStyle);
							wsheet.addCell(content);
							break;
						}*/
						content = new Label(j, k+1,tempStr + "", contentStyle);
						wsheet.addCell(content);
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
				this.setMSG("没有符合条件的信息");
			}
			return savePath;
		}
		
		
		
		 /**
		 * ExportExcelStudianming 导出选修课学生点名单
		 * 
		 * @author zhaixuchao
		 * @date:2016-11-8
		 * @return savePath 下载路径
		 * @throws SQLException
		 */
		public String ExportExcelStudianming(String SC_XNXQ,String xnxqname)throws SQLException {
			String sql = "";
			Vector vec = null;
			Vector titleVec = new Vector();
			Vector wzcjVec = new Vector();
			String tempStr = "";
			String savePath = "";

			titleVec.add("序号");
			titleVec.add("姓名");
			titleVec.add("学号");
			titleVec.add("班级");
			titleVec.add("开课班级");
			titleVec.add("上课周");
			titleVec.add("教师");
			int rownum=0;
			
			boolean flag=false;
			
			int xnxqmc=Integer.parseInt(xnxqname.substring(2, 4))-1;
			
			sql="select  row_number() over (partition by a.授课计划明细编号 order by b.学年学期编码 desc) as 序号, d.姓名,d.学号,e.行政班名称,a.选修班名称,a.授课周次,a.授课教师姓名" +
					" FROM V_规则管理_选修课授课计划明细表 a" +
					" inner join V_规则管理_选修课授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号" +
					" left join dbo.V_规则管理_学生选修课关系表 as c on c.授课计划明细编号=a.授课计划明细编号" +
					" left join dbo.V_学生基本数据子类 as d on d.学号=c.学号" +
					" left join dbo.V_学校班级数据子类 as e on e.行政班代码=d.行政班代码" +
					" where b.课程类型!='01' ";
			if(!SC_XNXQ.equals("")){
				sql+=" and b.学年学期编码='"+SC_XNXQ+"' ";
			}
			sql+=" order by a.选修班名称 ";
			
			vec = db.GetContextVector(sql);
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
				savePath += "/" + xnxqname +xnxqmc+"级任意选修课名单"+ year+ ((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+ (date < 10 ? "0" + date : date) + ".xls";
				
				try {
					OutputStream os = new FileOutputStream(savePath);
					WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
					WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 工作表名称
					WritableFont fontStyle;
					WritableCellFormat contentStyle;
					Label content;
					// 设置列宽
					for (int i = 0; i < titleVec.size(); i++) {
						if(i==2){
							wsheet.setColumnView(i, 25);
						}else if(i==3||i==4){
							wsheet.setColumnView(i, 35);
						}
						else{
							wsheet.setColumnView(i, 15);
						}
					}
	
					// 设置标题	
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
					//加的标题
					wsheet.mergeCells(0,0,6,0);
					content = new Label(0, 0, xnxqname +xnxqmc+"级任意选修课名单",contentStyle);
					wsheet.addCell(content);
					
	
					// 设置title
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 13,WritableFont.BOLD);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
					
					for (int i = 0; i < titleVec.size(); i++) {
						// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
						// 在Label对象的子对象中指明单元格的位置和内容
						content = new Label(i, 1, MyTools.StrFiltr(titleVec.get(i)),contentStyle);
						// 将定义好的单元格添加到工作表中
						wsheet.addCell(content);
					}
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11);			
					
					//绑定所有数据
					for (int i = 0, k = 1; i < vec.size()-titleVec.size(); i += titleVec.size(), k++) {
						for (int j = 0; j < titleVec.size(); j++) {
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
							contentStyle.setWrap(true);// 自动换行
							contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
							if((!(vec.get(i+4+7)).equals(vec.get(i+4)))){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK,Colour.BLUE);
							}
							else{
								contentStyle.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN,Colour.BLACK);
							}
							
							tempStr = MyTools.StrFiltr(vec.get(i+j));
							content = new Label(j, k+1,tempStr + "", contentStyle);
							wsheet.addCell(content);
							if(i== vec.size()-titleVec.size()-7){
								for(int r=vec.size()-titleVec.size()+1,w=1;r<vec.size();r+=titleVec.size(),w++){
									for(int q=0;q<titleVec.size();q++){
										content = new Label(q, k+1+1,MyTools.StrFiltr(vec.get(r+q-1)) + "", contentStyle);
										wsheet.addCell(content);
									}
								}
							}
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
				this.setMSG("没有符合条件的信息");
			}
			return savePath;
		}
	
		/**
		 * 查询学分不满的学生信息
		 * @date:2016-11-08
		 * @author:zhaixuchao
		 * @return Vector 结果集
		 * @throws SQLException
		 */
		public Vector querySourcebuman(int pageNum, int page,String nj,String xh,String xm,String banji) throws SQLException {
			String sql = ""; // 查询用SQL语句
			Vector vec = new Vector(); // 结果集
			int xuefen=0;
			int jisuan=0;
			sql="select * from (select a.学号,c.姓名,d.行政班名称,convert(float,SUM(b.学分)) as 已选课的学分,4.0 as 达标学分," +
					" case" +
					" when convert(float,SUM(b.学分))<4.0 then 4.0-convert(float,SUM(b.学分))" +
					" else 0" +
					" end as 还差多少学分,d.年级代码" +
					" FROM dbo.V_规则管理_学生选修课关系表 a" +
					" inner join dbo.V_规则管理_选修课授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号" +
					" left join dbo.V_学生基本数据子类 c on a.学号=c.学号" +
					" left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码" +
					" left join V_学校年级数据子类 e on e.年级代码=d.年级代码"+
					" where d.专业代码 in ('580405','630702','520104','600209')";
					
					if(!"".equalsIgnoreCase(nj)){
						sql += " and d.年级代码='" + MyTools.fixSql(nj) + "'";
					}
					if(!"".equalsIgnoreCase(xh)){
						sql += " and a.学号  like '%" + MyTools.fixSql(xh) + "%'";
					}
					if(!"".equalsIgnoreCase(xm)){
						sql += " and c.姓名  like'%" + MyTools.fixSql(xm) + "%'";
					}
					if(!"".equalsIgnoreCase(banji)){
						sql += " and d.行政班名称  like'%" + MyTools.fixSql(banji) + "%'";
					}
					sql+=" group by a.学号,c.姓名,d.行政班名称,d.年级代码" +
						" having convert(float,SUM(b.学分))<4.0" +
						" union all" +
						" select a.学号,c.姓名,d.行政班名称,convert(float,SUM(b.学分)) as 已选课的学分,6.0 as 达标学分," +
						" case" +
						" when convert(float,SUM(b.学分))<6.0 then 6.0-convert(float,SUM(b.学分))" +
						" else 0" +
						" end as 还差多少学分,d.年级代码" +
						" FROM dbo.V_规则管理_学生选修课关系表 a"+
						" inner join dbo.V_规则管理_选修课授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号" +
						" left join dbo.V_学生基本数据子类 c on a.学号=c.学号" +
						" left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码" +
						" left join V_学校年级数据子类 e on e.年级代码=d.年级代码"+
						" where d.专业代码 not in ('580405','630702','520104','600209')";
					if(!"".equalsIgnoreCase(nj)){
						sql += " and d.年级代码='" + MyTools.fixSql(nj) + "'";
					}
					if(!"".equalsIgnoreCase(xh)){
						sql += " and a.学号  like '%" + MyTools.fixSql(xh) + "%'";
					}
					if(!"".equalsIgnoreCase(xm)){
						sql += " and c.姓名  like '%" + MyTools.fixSql(xm) + "%'";
					}
					if(!"".equalsIgnoreCase(banji)){
						sql += " and d.行政班名称  like '%" + MyTools.fixSql(banji) + "%'";
					}
					sql+=" group by a.学号,c.姓名,d.行政班名称,d.年级代码" +
						" having convert(float,SUM(b.学分))<6.0)as t order by t.年级代码 desc,t.行政班名称";
					
			vec = db.getConttexJONSArr(sql,pageNum, page);// 带分页返回数据(json格式）
			return vec;
		}
		
		//查询年级combobox
		public Vector XF_NJCombobox() throws SQLException{
			Vector vec = null;	
			String sql="select '' as comboValue,'请选择' as comboName,'0' as 排序" +
					" union all	" +
					" select a.年级代码,a.年级名称,'1' as 排序" +
					" from dbo.V_学校年级数据子类 as a " +
					" order by 排序 asc,comboValue desc";
			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
		}
		
		//查询年级combobox
		public Vector XIBUCombobox() throws SQLException{
			Vector vec = null;	
			String sql="select '' as comboValue,'请选择' as comboName,'0' as 排序" +
					" union all	" +
					" select 系部代码,系部名称,排序 from dbo.V_基础信息_系部信息表 where 系部代码!='C00' " +
					" order by 排序 " ;

			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
		}
		
		//查询年级combobox
		public Vector wtXIBUCombobox() throws SQLException{
			Vector vec = null;	
			String sql="select '' as comboValue,'请选择' as comboName,'0' as 排序" +
					" union all	" +
					" select 系部代码,系部名称,排序 from dbo.V_基础信息_系部信息表 where 系部代码!='C00' " +
					" order by 排序 " ;

			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
		}
		
		//查询年级combobox
		public Vector ZHIYCombobox() throws SQLException{
			Vector vec = null;	
			String sql="select '' as comboValue,'请选择' as comboName " +
					" union all	" +
					" select '1' as comboValue,'第一志愿' as comboName " +
					" union all	" +
					" select '2' as comboValue,'第二志愿' as comboName " +
					" union all	" +
					" select '3' as comboValue,'第三志愿' as comboName " +
					" union all	" +
					" select '4' as comboValue,'非所选志愿' as comboName " ;
			
			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
		}
		
		//查询年级combobox
		public Vector INFOCombobox() throws SQLException{
			Vector vec = null;	
			String sql="select '1' as comboValue,'过滤已安排学生' as comboName " +
					" union all	" +
					" select '2' as comboValue,'显示所有学生' as comboName " ;
					
			vec = db.getConttexJONSArr(sql, 0, 0);
			return vec;
		}
		
		/**
		 * 查询选课信息
		 * @date:2016-11-10
		 * @throws WrongSQLException
		 * @throws SQLException
		 */
		public Vector showSelectiongeren(int pageNum, int page, String iUSERCODE) throws SQLException {		 
			String sql = ""; // 查询用SQL语句
			Vector vec = null; // 结果集
			
			sql="SELECT a.学年学期编码,a.学号,c.姓名,b.选修班名称,a.授课计划明细编号,b.学分" +
					" FROM dbo.V_规则管理_学生选修课关系表 a,dbo.V_规则管理_选修课授课计划明细表 b,dbo.V_学生基本数据子类 c  " +
					" where a.授课计划明细编号=b.授课计划明细编号 and a.学号=c.学号 and a.学号='"+MyTools.fixSql(iUSERCODE)+"' ";
			sql+="  order by a.学年学期编码 desc";
			vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
			return vec;
		}
		
		
		/**
		 * ExportExcelStuXFBM 导出学分不满的学生信息
		 * 
		 * @author zhaixuchao
		 * @date:2016-11-11
		 * @return savePath 下载路径
		 * @throws SQLException
		*/
		public String ExportExcelStuXFBM(String nj,String xh,String xm,String banji)throws SQLException {
			
			String sql = "";
			Vector vec = null;
			Vector titleVec = new Vector();
			Vector wzcjVec = new Vector();
			String tempStr = "";
			String savePath = "";

			titleVec.add("学号");
			titleVec.add("姓名");
			titleVec.add("行政班名称");
			titleVec.add("已选课的学分");
			titleVec.add("达标学分");
			titleVec.add("还差多少学分");
				
			// 读取班级学生成绩信息
			sql="select * from (select d.年级代码,a.学号,c.姓名,d.行政班名称,convert(float,SUM(b.学分)) as 已选课的学分,4.0 as 达标学分," +
					" case" +
					" when convert(float,SUM(b.学分))<4.0 then 4.0-convert(float,SUM(b.学分))" +
					" else 0" +
					" end as 还差多少学分" +
					" FROM dbo.V_规则管理_学生选修课关系表 a" +
					" inner join dbo.V_规则管理_选修课授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号" +
					" left join dbo.V_学生基本数据子类 c on a.学号=c.学号" +
					" left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码" +
					" left join V_学校年级数据子类 e on e.年级代码=d.年级代码"+
					" where d.专业代码 in ('580405','630702','520104','600209')";
					
					if(!"".equalsIgnoreCase(nj)){
						sql += " and d.年级代码='" + MyTools.fixSql(nj) + "'";
					}
					if(!"".equalsIgnoreCase(xh)){
						sql += " and a.学号  like '%" + MyTools.fixSql(xh) + "%'";
					}
					if(!"".equalsIgnoreCase(xm)){
						sql += " and c.姓名  like'%" + MyTools.fixSql(xm) + "%'";
					}
					if(!"".equalsIgnoreCase(banji)){
						sql += " and d.行政班名称  like'%" + MyTools.fixSql(banji) + "%'";
					}
					sql+=" group by a.学号,c.姓名,d.行政班名称,d.年级代码" +
						" having convert(float,SUM(b.学分))<4.0" +
						" union all" +
						" select d.年级代码,a.学号,c.姓名,d.行政班名称,convert(float,SUM(b.学分)) as 已选课的学分,6.0 as 达标学分," +
						" case" +
						" when convert(float,SUM(b.学分))<6.0 then 6.0-convert(float,SUM(b.学分))" +
						" else 0" +
						" end as 还差多少学分" +
						" FROM dbo.V_规则管理_学生选修课关系表 a"+
						" inner join dbo.V_规则管理_选修课授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号" +
						" left join dbo.V_学生基本数据子类 c on a.学号=c.学号" +
						" left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码" +
						" left join V_学校年级数据子类 e on e.年级代码=d.年级代码"+
						" where d.专业代码 not in ('580405','630702','520104','600209')";
					if(!"".equalsIgnoreCase(nj)){
						sql += " and d.年级代码='" + MyTools.fixSql(nj) + "'";
					}
					if(!"".equalsIgnoreCase(xh)){
						sql += " and a.学号  like '%" + MyTools.fixSql(xh) + "%'";
					}
					if(!"".equalsIgnoreCase(xm)){
						sql += " and c.姓名  like '%" + MyTools.fixSql(xm) + "%'";
					}
					if(!"".equalsIgnoreCase(banji)){
						sql += " and d.行政班名称  like '%" + MyTools.fixSql(banji) + "%'";
					}
					sql+=" group by a.学号,c.姓名,d.行政班名称,d.年级代码" +
						" having convert(float,SUM(b.学分))<6.0) as t order by t.年级代码 desc,t.行政班名称";
					
			vec = db.GetContextVector(sql);
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
				savePath += "/" + "学分未达标的学生信息表"+ year+ ((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+ (date < 10 ? "0" + date : date) + ".xls";
				
				try {
					OutputStream os = new FileOutputStream(savePath);
					WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
					WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 工作表名称
					WritableFont fontStyle;
					WritableCellFormat contentStyle;
					Label content;
					// 设置列宽
					for (int i = 0; i < titleVec.size(); i++) {
						if(i==2){
							wsheet.setColumnView(i, 40);
						}
						else{
							wsheet.setColumnView(i, 16);
						}
					}

					// 设置title
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
					//加的标题
					wsheet.mergeCells(0,0,5,0);
					content = new Label(0, 0, "学分未达标的学生信息表",contentStyle);
					wsheet.addCell(content);

					// 设置title
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 13,WritableFont.BOLD);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
					
					for (int i = 0; i < titleVec.size(); i++) {
						// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
						// 在Label对象的子对象中指明单元格的位置和内容
						content = new Label(i, 1, MyTools.StrFiltr(titleVec.get(i)),contentStyle);
						// 将定义好的单元格添加到工作表中
						wsheet.addCell(content);
					}
					fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
					contentStyle.setShrinkToFit(true);//字体大小自适应
					//绑定所有数据
					for (int i = 0, k = 1; i < vec.size(); i += titleVec.size(), k++) {
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
						contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
						contentStyle.setShrinkToFit(true);//字体大小自适应
						i++;
						for (int j = 0; j < titleVec.size(); j++) {
							tempStr = MyTools.StrFiltr(vec.get(i+j));
							if(j==5){
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
								contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//表格线
								contentStyle.setShrinkToFit(true);//字体大小自适应
								content = new Label(j, k+1,tempStr + "", contentStyle);
								wsheet.addCell(content);
								break;
							}
							content = new Label(j, k+1,tempStr + "", contentStyle);
							wsheet.addCell(content);
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
					this.setMSG("没有符合条件的成绩信息");
				}
				return savePath;
			}
		
		/**
		 * 删除留级和不在校学生选课信息
		 * @author 2017-1-23
		 * @author lupengfei
		 * @return
		 * @throws SQLException
		 */
		public void delelecSubmit() throws SQLException {
			DBSource db = new DBSource(request);
			Vector vector = null;
			String sql = "";
			String sqlxxk="";
			String sqldel="";
			Vector vec = null; // 结果集
			Vector vecxxk = null; 
			Vector vecdel=new Vector(); 
			
			//压缩文件
			try {
				File file = new File("D:/test/test.rar");    //下载文件路径
				if (!file.exists()){   
					file.createNewFile();   
				}
				String zipfile="D:/test/test.rar";
				ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipfile));
				out.setEncoding("GBK");
				
				File f=new File("D:/test/test.pdf");
				String base="test.pdf";
				out.putNextEntry(new ZipEntry(base));
				FileInputStream in = new FileInputStream(f);
				int b;
				System.out.println(base);
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//删除大一学生选课信息，成绩信息
			sqlxxk="select distinct [学年学期编码],[学号],[授课计划明细编号] from dbo.V_规则管理_学生选修课关系表 where [学号] in ( " +
				 	"SELECT a.[学号] FROM [dbo].[V_规则管理_学生选修课关系表] a left join [dbo].V_学生基本数据子类 b on a.学号=b.学号 " +
				 	"left join [dbo].V_学校班级数据子类 c on b.行政班代码=c.行政班代码 " +
				 	"left join dbo.V_专业基本信息数据子类 d on c.专业代码=d.专业代码 " +
				 	"where SUBSTRING(b.行政班代码,1,2)='"+this.getXX_XNXQ().substring(2,4)+"' and d.专业名称!='计算机网络技术（中高职贯通）' and b.[学生状态] not in ('01','05','07','08','09') " +
				 	"union " +
				 	//删除不在小学生选课信息，成绩信息    
				 	"SELECT a.[学号] FROM [dbo].[V_规则管理_学生选修课关系表] a " +
				 	"left join [dbo].V_学生基本数据子类 b on a.学号=b.学号 " +
				 	"where b.[学生状态] not in ('01','05','07','08','09')	" +
				 	") " +
				 	"and [授课计划明细编号] not in (select distinct 相关编号 from dbo.V_成绩管理_学生成绩信息表 ) "; 					        
				 
			vecxxk=db.GetContextVector(sqlxxk);System.out.println("size--"+vecxxk.size());
			if(vecxxk!=null&&vecxxk.size()>0){
				for(int i=0;i<vecxxk.size();i=i+3){
//					sqldel=" update [dbo].[V_规则管理_选修课授课计划明细表] set 报名人数=报名人数-1 where 授课计划明细编号='"+MyTools.fixSql(vecxxk.get(i+2).toString())+"' ";
//				    vecdel.add(sqldel);
				    sqldel=" delete FROM [dbo].[V_规则管理_学生选修课关系表]  where 授课计划明细编号='"+MyTools.fixSql(vecxxk.get(i+2).toString())+"' and [学号] ='"+MyTools.fixSql(vecxxk.get(i+1).toString())+"' ";
				    vecdel.add(sqldel);
				    sqldel=" delete FROM [dbo].[V_成绩管理_学生成绩信息表] where 相关编号='"+MyTools.fixSql(vecxxk.get(i+2).toString())+"' and [学号] ='"+MyTools.fixSql(vecxxk.get(i+1).toString())+"' ";
				    vecdel.add(sqldel); 
				}
			}else{
				this.setMSG("没有需要删除的信息");
			}     
				 	
			if(vecdel.size()>0){
			    if(db.executeInsertOrUpdateTransaction(vecdel)){
			    	this.setMSG("删除成功");
				}else{
					this.setMSG("删除失败");
				}
			}
			
		}	
	
		/**
		 * 
		 * @date:2015-07-23
		 * @author:lupengfei
		 * @throws WrongSQLException
		 * @throws SQLException
		 */
		public Vector openTeacher(int pageNum,int pageSize,String teaId,String teaName,String teaLevel,String teacharr) throws WrongSQLException,SQLException{
			String sql2 = "";
			String sql3 = "";
			String sql4 = "";
			String sql5 = "";
			Vector vec = null;
			Vector vec2 = null;
			Vector vec3 = null;
			String maxNewId="";
			String teaid="";
			String sjxl="";//上课时间序列
			String sjxlck="";
			int xl=0;
			int classtag=0;
			
			//按授课周次，上课日期，上课时间过滤不能选的教师
			//时间序列
			String[] sksjx=this.getXX_SKSJ().split(",");
			//System.out.println("sksj:--"+this.getXX_SJXL()+sksjx.length);
			for(int i=0;i<sksjx.length;i++){
				sjxl+=this.getXX_SKRQ()+sksjx[i]+",";
				sjxlck+="'"+this.getXX_SKRQ()+sksjx[i]+"',";
				xl++;
			}
			if(!sjxl.equals("")){
				sjxl=sjxl.substring(0, sjxl.length()-1);
			}
			if(!sjxlck.equals("")){
				sjxlck=sjxlck.substring(0, sjxlck.length()-1);
			}
			
			String timeorder="";
			timeorder=checkTeacher("",this.getXX_SKZC(),"","","",this.getXX_XNXQ(),"","",sjxl,sjxlck,"");

			if(timeorder.equals("")){
				timeorder="''";
			}
			
			//System.out.println("tea:---"+timeorder);
						
			if(teacharr.equals("")){//没有教师数据
				teaid="''";
			}else{//选中的教师排在前面显示
				String[] teachers=teacharr.split("\\+");
				for(int i=0;i<teachers.length;i++){
					teaid+="'"+teachers[i]+"',";
				}
				teaid=teaid.substring(0, teaid.length()-1);
			}
			String sql = "select 工号,姓名,'1' as px from dbo.V_教职工基本数据子类 where 工号 in ("+teaid+") " +
						 " union " +
						 " select 工号,姓名,'2' as px from dbo.V_教职工基本数据子类 where 工号 not in ("+teaid+") ";
			if(!teaId.equalsIgnoreCase("")){
				sql += " and 工号 like '%"+MyTools.fixSql(teaId)+"%' ";
			}	
			if(!teaName.equalsIgnoreCase("")){
				sql += " and 姓名 like '%"+MyTools.fixSql(teaName)+"%' ";
			}
			
			sql += " and 工号 not in ("+timeorder+") ";
			sql += " order by px,工号 ";
			vec = db.getConttexJONSArr(sql, pageNum, pageSize);
			return vec;
		}
		
		/**
		 * 选修
		 * @date:2015-07-23
		 * @author:lupengfei
		 * @throws WrongSQLException
		 * @throws SQLException
		 */
		public Vector openRoom(String seltype,String roomarr) throws WrongSQLException,SQLException{
			String sql2 = "";
			String sql3 = "";
			String sql4 = "";
			String sql5 = "";
			Vector vec = null;
			Vector vec2 = null;
			Vector vec3 = null;
			String maxNewId="";
			String teaid="";
			String sjxl="";//上课时间序列
			String sjxlck="";
			int xl=0;
			int classtag=0;
			
			//按授课周次，上课日期，上课时间过滤不能选的教师
			//时间序列
			String[] sksjx=this.getXX_SKSJ().split(",");
			//System.out.println("sksj:--"+this.getXX_SJXL()+sksjx.length);
			for(int i=0;i<sksjx.length;i++){
				sjxl+=this.getXX_SKRQ()+sksjx[i]+",";
				sjxlck+="'"+this.getXX_SKRQ()+sksjx[i]+"',";
				xl++;
			}
			if(!sjxl.equals("")){
				sjxl=sjxl.substring(0, sjxl.length()-1);
			}
			if(!sjxlck.equals("")){
				sjxlck=sjxlck.substring(0, sjxlck.length()-1);
			}
			
			String timeorder="";
			timeorder=checkRoom("",this.getXX_SKZC(),"","","",this.getXX_XNXQ(),"","",sjxl,sjxlck,"");
			
			if(timeorder.equals("")){
				timeorder="''";
			}
			
			//System.out.println("room:---"+timeorder);

			String roomid="";
			if(roomarr.equals("")){//没有教师数据
				roomid="''";
			}else{//选中的教师排在前面显示
				String[] rooms=roomarr.split("\\+");
				for(int i=0;i<rooms.length;i++){
					roomid+="'"+rooms[i]+"',";
				}
				roomid=roomid.substring(0, roomid.length()-1);
			}
			String sql = "select distinct d.名称,a.教室编号,a.教室名称,a.实际容量,'1' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室编号 in ("+roomid+") " +
					" union " +
					" select distinct d.名称,a.教室编号,a.教室名称,a.实际容量,'2' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室编号 not in ("+roomid+") " ;
					
//			if(!selschool.equals("")){
//				sql+=" and a.校区代码='"+MyTools.StrFiltr(MyTools.fixSql(selschool))+"'";
//			}
//			if(!selhouse.equals("")){
//				sql+=" and a.建筑物号='"+MyTools.StrFiltr(MyTools.fixSql(selhouse))+"'";
//			}
			if(!seltype.equals("")){
				sql+=" and d.编号='"+MyTools.StrFiltr(MyTools.fixSql(seltype))+"'";
			}
			
			sql += " and a.教室编号 not in ("+timeorder+") ";
			sql += " and a.是否可用='1' order by px,a.教室编号 ";
			vec = db.getConttexJONSArr(sql, 0, 0);
			
			return vec;
		}
		
		//检查不能安排的时间
		public String checkTeacher(String jsxm,String skzcxq,String weeks,String cdyq,String iKeyCode,String termid,String classId,String jsbh,String sjxl,String sjxlck,String jfkbmxbh) throws SQLException {
				Vector vec = new Vector();
				Vector vec0 = null;
				Vector vec2 = null;
				Vector vec4 = null;
				Vector vec5 = null;
				Vector vec6 = null;
				Vector vec8 = null;
				String sql = "";
				String sql2 = "";
				String sql0 = "";
				String sql4 = "";
				String sql5 = "";
				String sql6 = "";
				String sql7 = "";
				String sql8 = "";
				String times="";//不能排的时间序列位置
				String sames="";//时间序列对应的冲突信息
				String rooms="";//班级相同的冲突信息
				String jinpai="";//教师，场地禁排
				ArrayList teaarray=new ArrayList();
				ArrayList wekarray=new ArrayList();
				ArrayList cdyarray=new ArrayList();
				//查询所有要排课老师和他们的上课周次
				//System.out.println("教师周次场地：------"+cdyq);
				String[] teac=jsbh.split("\\&");
				String[] week=skzcxq.split("\\&");
				String[] room=cdyq.split("\\&");
				
				//Map<String, String> map=new HashMap<String, String>();   
				for(int i=0;i<teac.length;i++){
					teaarray.add(teac[i]);  
				}
				for(int k=0;k<week.length;k++){
					wekarray.add(week[k]);
				}
				for(int l=0;l<room.length;l++){
					cdyarray.add(room[l]);
				}
				
				//查询所有要排课的老师
				String teachers=jsbh.replaceAll("\\+", "\\&");
				
				//查询课程表周详情表
				sql0="SELECT [时间序列],[授课教师编号],[授课教师姓名],[授课周次],[场地编号],[场地名称],[行政班名称],[课程名称],[授课计划明细编号] " +
					"FROM [dbo].[V_排课管理_课程表周详情表] " +
					"where 学年学期编码='"+MyTools.fixSql(termid)+"' and 行政班代码!='"+MyTools.fixSql(classId)+"' and 授课教师姓名 != '' and [场地编号]!='' and [时间序列] in ("+sjxlck+") " +
					"order by [时间序列],[行政班名称],[课程名称],convert(int,授课周次)";
				vec0=db.GetContextVector(sql0);
				if(vec0!=null&&vec0.size()>0){
					String skzc="";
					for(int i=0;i<vec0.size()-9;){
						skzc=vec0.get(i+3).toString();			
							while(i<(vec0.size()-9)&&vec0.get(i+8).toString().equals(vec0.get(i+8+9).toString())&&vec0.get(i).toString().equals(vec0.get(i+9).toString())){//授课计划编号相等+时间序列相等					
								skzc+="#"+vec0.get(i+3+9).toString();
								//System.out.println(i+":--"+skzc);
								i=i+9;
							}
							skzcxq=merge(skzc);
							String[] skjsbh5=vec0.get(i+1).toString().split("\\+");
							String[] skjsxm5=vec0.get(i+2).toString().split("\\+");
							for(int m=0;m<skjsbh5.length;m++){
								vec.add(vec0.get(i).toString());//时间序列
								vec.add(skjsbh5[m]);//授课教师编号
								vec.add(skjsxm5[m]);//授课教师姓名
								vec.add(skzcxq);//授课周次
								vec.add(vec0.get(i+4).toString());//场地编号
								vec.add(vec0.get(i+5).toString());//场地名称
								vec.add(vec0.get(i+6).toString());//行政班名称
								vec.add(vec0.get(i+7).toString());//课程名称
							}
							i=i+9;
					}					
				}
						
//						for(int i=0;i<vec.size();i=i+8){
//							System.out.println(vec.get(i).toString()+"|"+vec.get(i+1).toString()+"|"+vec.get(i+2).toString()+"|"+vec.get(i+3).toString()+"|"+vec.get(i+4).toString()+"|"+vec.get(i+5).toString()+"|"+vec.get(i+6).toString()+"|"+vec.get(i+7).toString());
//						}
						
				//查询选修课
				sql2=" SELECT 上课时间 = substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number),a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,a.课程名称 " +
					" FROM  (select a.上课时间,a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,b.课程名称 from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.学年学期编码='"+MyTools.fixSql(termid)+"') a JOIN" +
					" master..spt_values b ON b.type = 'P' " +
					" WHERE     substring(',' + a.上课时间, b.number, 1) = ',' " +
					" and substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number) in ("+sjxlck+") ";
				vec2=db.GetContextVector(sql2);
				if(vec2!=null&&vec2.size()>0){
					for(int m=0;m<vec2.size();m=m+8){
						vec.add(vec2.get(m).toString());//时间序列
						vec.add(vec2.get(m+1).toString());//授课教师编号
						vec.add(vec2.get(m+2).toString());//授课教师姓名
						vec.add(vec2.get(m+3).toString());//授课周次
						vec.add(vec2.get(m+4).toString());//场地编号
						vec.add(vec2.get(m+5).toString());//场地名称
						vec.add(vec2.get(m+6).toString());//行政班名称
						vec.add(vec2.get(m+7).toString());//课程名称
					}
				}
						
				if(vec!=null&vec.size()>0){
					for(int i=0;i<vec.size();i=i+8){
						for (int k=0;k<wekarray.size();k++) { 
							//比较周次是否有相同
							int tag1=compareweek(wekarray.get(k).toString(),vec.get(i+3).toString(),weeks);
							if(tag1==1){//有重复
								if(vec.get(i+1).toString().indexOf("+")>-1){
									String[] teas=vec.get(i+1).toString().split("\\+");
									for(int s=0;s<teas.length;s++){
										times+="'"+teas[s]+"',";
									}
								}else{
									times+="'"+vec.get(i+1).toString()+"',";
								}
							}else{//没有重复
													
							}	
				        }
					}	
				}
				
				//System.out.println("课程表明细：----------------"+times);
				
				//固排禁排表查询禁排的时间序列
				//班级,教师，场地禁排
//				sql5=" select a.时间序列,a.行政班代码,a.禁排类型,case when a.禁排类型='bj' then c.行政班代码 when a.禁排类型='js' then b.工号 when a.禁排类型='cd' then d.教室编号 else '' end as 名称 from dbo.V_规则管理_固排禁排表 a left join dbo.V_教职工基本数据子类 b on a.行政班代码=b.工号 left join dbo.V_学校班级数据子类 c on a.行政班代码=c.行政班代码 left join dbo.V_教室数据类 d on a.行政班代码=d.教室编号   where 学年学期编码='"+MyTools.fixSql(termid)+"' and 类型='3' and a.[时间序列] in ("+sjxlck+") " ;
//				vec5=db.GetContextVector(sql5);
//				if(vec5!=null&&vec5.size()>0){
//					for(int i=0;i<vec5.size();i=i+4){
//						if(vec5.get(i+2).toString().equals("js")){
//							if(vec5.get(i+3).toString().indexOf("+")>-1){
//								String[] teas=vec.get(i+1).toString().split("\\+");
//								for(int s=0;s<teas.length;s++){
//									times+="'"+teas[s]+"',";
//								}
//							}else{
//								times+="'"+vec5.get(i+3).toString()+"',";
//							}
//						}	
//					}
//				}
				
				//检查机房课表有没有冲突
//				sql6="SELECT [使用教室],[使用周次],[使用时间] FROM [dbo].[V_机房课表_课程考试明细表] where [学年学期编码]='"+MyTools.fixSql(termid)+"' and [机房明细编号]!='"+jfkbmxbh+"' ";
//				vec6=db.GetContextVector(sql6);
//				if(vec6!=null&&vec6.size()>0){
//					for(int i=0;i<vec6.size();i=i+3){ 
//						//判断时间序列是否有相同
//						String[] sysj=vec6.get(i+2).toString().split(",");
//						String[] sjxlsel=sjxl.split(",");
//						int sjxlflag=0;
//						for(int m=0;m<sysj.length;m++){
//							for(int n=0;n<sjxlsel.length;n++){
//								if(sysj[m].equals(sjxlsel[n])){//时间序列有相同
//									sjxlflag=1;
//								}
//							}
//						}
//						if(sjxlflag==1){
//							for (int k=0;k<wekarray.size();k++) { 
//								//if(vec.get(i+1).toString().indexOf(teaarray.get(k).toString())>-1){//存在
//									String t1=vec6.get(i).toString();
//									String t2=vec6.get(i+1).toString();
//									
//									//比较周次是否有相同
//									int tag1=compareweek(wekarray.get(k).toString(),t2,weeks);
//									if(tag1==1){//有重复
//										//比较场地是否有相同
//										int tag2=compareroom(cdyarray.get(k).toString(),t1);
//										if(tag2==1){//场地相同
//											times+=vec6.get(i+2).toString()+",";
//											//System.out.println("er---"+vec6.get(i).toString()+"|"+vec6.get(i+1).toString()+"|"+vec6.get(i+2).toString());
//											//sames+=vec.get(i).toString()+","+selteaname2[m]+","+vec.get(i+6).toString()+","+selclsname2[m]+","+selwektime2[m]+",";;
//										}else{
//															
//										}
//									}else{//没有重复
//												
//									}			
//								//}
//					        }
//						}
//					}
//				}

				if(!times.equalsIgnoreCase("")){
					times=times.substring(0,times.length()-1);
				}
		
				return times;
		}
		
		//检查不能安排的时间
		public String checkRoom(String jsxm,String skzcxq,String weeks,String cdyq,String iKeyCode,String termid,String classId,String jsbh,String sjxl,String sjxlck,String jfkbmxbh) throws SQLException {
						Vector vec = new Vector();
						Vector vec0 = null;
						Vector vec2 = null;
						Vector vec4 = null;
						Vector vec5 = null;
						Vector vec6 = null;
						Vector vec8 = null;
						String sql = "";
						String sql2 = "";
						String sql0 = "";
						String sql4 = "";
						String sql5 = "";
						String sql6 = "";
						String sql7 = "";
						String sql8 = "";
						String times="";//不能排的时间序列位置
						String sames="";//时间序列对应的冲突信息
						String rooms="";//班级相同的冲突信息
						String jinpai="";//教师，场地禁排
						ArrayList teaarray=new ArrayList();
						ArrayList wekarray=new ArrayList();
						ArrayList cdyarray=new ArrayList();
						//查询所有要排课老师和他们的上课周次
						//System.out.println("教师周次场地：------"+cdyq);
						String[] teac=jsbh.split("\\&");
						String[] week=skzcxq.split("\\&");
						String[] room=cdyq.split("\\&");
						
						//Map<String, String> map=new HashMap<String, String>();   
						for(int i=0;i<teac.length;i++){
							teaarray.add(teac[i]);  
						}
						for(int k=0;k<week.length;k++){
							wekarray.add(week[k]);
						}
						for(int l=0;l<room.length;l++){
							cdyarray.add(room[l]);
						}
						
						//查询所有要排课的老师
						String teachers=jsbh.replaceAll("\\+", "\\&");
						
						//查询课程表周详情表
						sql0="SELECT [时间序列],[授课教师编号],[授课教师姓名],[授课周次],[场地编号],[场地名称],[行政班名称],[课程名称],[授课计划明细编号] " +
							"FROM [dbo].[V_排课管理_课程表周详情表] " +
							"where 学年学期编码='"+MyTools.fixSql(termid)+"' and 行政班代码!='"+MyTools.fixSql(classId)+"' and 授课教师姓名 != '' and [场地编号]!='' and [时间序列] in ("+sjxlck+") " +
							"order by [时间序列],[行政班名称],[课程名称],convert(int,授课周次)";
						vec0=db.GetContextVector(sql0);
						if(vec0!=null&&vec0.size()>0){
							String skzc="";
							for(int i=0;i<vec0.size()-9;){
								skzc=vec0.get(i+3).toString();			
									while(i<(vec0.size()-9)&&vec0.get(i+8).toString().equals(vec0.get(i+8+9).toString())&&vec0.get(i).toString().equals(vec0.get(i+9).toString())){//授课计划编号相等+时间序列相等					
										skzc+="#"+vec0.get(i+3+9).toString();
										//System.out.println(i+":--"+skzc);
										i=i+9;
									}
									skzcxq=merge(skzc);
									String[] skjsbh5=vec0.get(i+1).toString().split("\\+");
									String[] skjsxm5=vec0.get(i+2).toString().split("\\+");
									for(int m=0;m<skjsbh5.length;m++){
										vec.add(vec0.get(i).toString());//时间序列
										vec.add(skjsbh5[m]);//授课教师编号
										vec.add(skjsxm5[m]);//授课教师姓名
										vec.add(skzcxq);//授课周次
										vec.add(vec0.get(i+4).toString());//场地编号
										vec.add(vec0.get(i+5).toString());//场地名称
										vec.add(vec0.get(i+6).toString());//行政班名称
										vec.add(vec0.get(i+7).toString());//课程名称
									}
									i=i+9;
							}					
						}
								
//								for(int i=0;i<vec.size();i=i+8){
//									System.out.println(vec.get(i).toString()+"|"+vec.get(i+1).toString()+"|"+vec.get(i+2).toString()+"|"+vec.get(i+3).toString()+"|"+vec.get(i+4).toString()+"|"+vec.get(i+5).toString()+"|"+vec.get(i+6).toString()+"|"+vec.get(i+7).toString());
//								}
								
						//查询选修课
						sql2="SELECT 上课时间 = substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number),a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,a.课程名称 " +
							" FROM  (select a.上课时间,a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,b.课程名称 from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.学年学期编码='"+MyTools.fixSql(termid)+"') a JOIN" +
							" master..spt_values b ON b.type = 'P' " +
							" WHERE     substring(',' + a.上课时间, b.number, 1) = ',' " +
							" and substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number) in ("+sjxlck+") ";
						vec2=db.GetContextVector(sql2);
						if(vec2!=null&&vec2.size()>0){
							for(int m=0;m<vec2.size();m=m+8){
								vec.add(vec2.get(m).toString());//时间序列
								vec.add(vec2.get(m+1).toString());//授课教师编号
								vec.add(vec2.get(m+2).toString());//授课教师姓名
								vec.add(vec2.get(m+3).toString());//授课周次
								vec.add(vec2.get(m+4).toString());//场地编号
								vec.add(vec2.get(m+5).toString());//场地名称
								vec.add(vec2.get(m+6).toString());//行政班名称
								vec.add(vec2.get(m+7).toString());//课程名称
							}
						}
								
						if(vec!=null&vec.size()>0){
							for(int i=0;i<vec.size();i=i+8){
								for (int k=0;k<wekarray.size();k++) { 
									//比较周次是否有相同
									int tag1=compareweek(wekarray.get(k).toString(),vec.get(i+3).toString(),weeks);
									if(tag1==1){//有重复	
										if(vec.get(i+4).toString().indexOf("+")>-1){
											String[] teas=vec.get(i+4).toString().split("\\+");
											for(int s=0;s<teas.length;s++){
												times+="'"+teas[s]+"',";
											}
										}else{
											times+="'"+vec.get(i+4).toString()+"',";
										}
									}else{//没有重复
															
									}	
						        }
							}	
						}
						
						//System.out.println("课程表明细：----------------"+times);
						
						//固排禁排表查询禁排的时间序列
						//班级,教师，场地禁排
//						sql5=" select a.时间序列,a.行政班代码,a.禁排类型,case when a.禁排类型='bj' then c.行政班代码 when a.禁排类型='js' then b.工号 when a.禁排类型='cd' then d.教室编号 else '' end as 名称 from dbo.V_规则管理_固排禁排表 a left join dbo.V_教职工基本数据子类 b on a.行政班代码=b.工号 left join dbo.V_学校班级数据子类 c on a.行政班代码=c.行政班代码 left join dbo.V_教室数据类 d on a.行政班代码=d.教室编号   where 学年学期编码='"+MyTools.fixSql(termid)+"' and 类型='3' and [时间序列] in ("+sjxlck+") " ;
//						vec5=db.GetContextVector(sql5);
//						if(vec5!=null&&vec5.size()>0){
//							for(int i=0;i<vec5.size();i=i+4){
//								if(vec5.get(i+2).toString().equals("js")){
//									if(vec5.get(i+3).toString().indexOf("+")>-1){
//										String[] teas=vec.get(i+1).toString().split("\\+");
//										for(int s=0;s<teas.length;s++){
//											times+="'"+teas[s]+"',";
//										}
//									}else{
//										times+="'"+vec5.get(i+3).toString()+"',";
//									}
//								}	
//							}
//						}
						
						//检查机房课表有没有冲突
						sql6="SELECT [使用教室],[使用周次],[使用时间] FROM [dbo].[V_机房课表_课程考试明细表] where [学年学期编码]='"+MyTools.fixSql(termid)+"' and [机房明细编号]!='"+jfkbmxbh+"' ";
						vec6=db.GetContextVector(sql6);
						if(vec6!=null&&vec6.size()>0){
							for(int i=0;i<vec6.size();i=i+3){ 
								//判断时间序列是否有相同
								String[] sysj=vec6.get(i+2).toString().split(",");
								String[] sjxlsel=sjxl.split(",");
								int sjxlflag=0;
								for(int m=0;m<sysj.length;m++){
									for(int n=0;n<sjxlsel.length;n++){
										if(sysj[m].equals(sjxlsel[n])){//时间序列有相同
											sjxlflag=1;
										}
									}
								}
								if(sjxlflag==1){
									for (int k=0;k<wekarray.size();k++) { 
										//if(vec.get(i+1).toString().indexOf(teaarray.get(k).toString())>-1){//存在
											String t1=vec6.get(i).toString();
											String t2=vec6.get(i+1).toString();
											
											//比较周次是否有相同
											int tag1=compareweek(wekarray.get(k).toString(),t2,weeks);
											if(tag1==1){//有重复
												if(vec6.get(i).toString().indexOf("+")>-1){
													String[] teas=vec.get(i+1).toString().split("\\+");
													for(int s=0;s<teas.length;s++){
														times+="'"+teas[s]+"',";
													}
												}else{
													times+="'"+vec6.get(i).toString()+"',";
												}
											}else{//没有重复
														
											}			
										//}
							        }
								}
							}
						}

						if(!times.equalsIgnoreCase("")){
							times=times.substring(0,times.length()-1);
						}
				
						return times;
		}
		
		
		/**
	    * 把接受的全部文件打成压缩包 
	    * @param List<File>;  
	    * @param org.apache.tools.zip.ZipOutputStream  
	    */
		
		private void zipout(ZipOutputStream out,File f,String base) throws IOException{
			
				
				System.out.println("base:------------------------"+base);
				out.putNextEntry(new ZipEntry(base));
				FileInputStream in = new FileInputStream(f);
				int b;
				System.out.println(base);
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				in.close();
			
		}
			
		/**
		 * 导出课程设置表
		 * @author 2017-03-27
		 * @author yangda
		 * @return
		 * @throws SQLException
		 */
		public String exportExcel(String ic_xnxq) throws SQLException {
			Vector vector = null;
			String savePath = "";
			savePath = MyTools.getProp(request, "Base.exportExcelPath");
			
			String sql = "select SUBSTRING(a.学年学期编码,1,5) as 学年学期编码, a.专业代码 as 专业代码, a.专业名称 as [专业名称(全称)], '' as 专业方向代码, " +
					"'' as [专业方向名称（全称）], a.课程代码 as 课程代码, a.课程名称 as 课程名称, a.课程分类 as 课程类型, " +
					"(case a.课程属性 when 'A' then '公共课' when 'B' then '专业基础课' when 'C' then '专业课' else a.课程属性 end) as 课程属性, " +
					"(case a.课程性质 when 'A' then '必修课' when 'B' then '专业选修课' when 'C' then '公共选修课' else a.课程性质 end) as 课程性质, " +
					"a.总课时 as [教学计划规定课时数(学时)], a.实验实训 as 课时数, (cast((cast(a.实验实训 as float)/cast(a.总课时 as float)) as Numeric(18,2))*100) as 比例, " +
					"'' as 是否专业核心课程, '' as 是否校企合作开发课程, '' as 精品课程, '' as [教材名称(全称)], '' as [版本日期（年）], '' as 出版社, '' as 第一作者, '' as 教材性质, '' as 教材类型, " +
					"a.年级名称 as 授课年级, '' as 主要授课地点, '' as 主要授课方式, b.考试形式 as [考试/考核主要方法], '' as 课证融通课程 " +
					"from dbo.V_规则管理_培养计划信息表 a left join dbo.V_考试形式 b on a.考试形式 = b.编号 " +
					"where 学年学期编码 = '" + MyTools.fixSql(ic_xnxq) + "'";
			
			vector = db.GetContextVector(sql);
			   
			try {
				//声明工作簿jxl.write.WritableWorkbook  
				WritableWorkbook wwb;  
				//创建文件目录
				File file = new File(savePath);
				//判断文件目录是否存在
				if (!file.exists()) {
					file.mkdir();
				}
				if(vector != null && vector.size() > 0){
					//获取学年学期编码
					String str = (String) vector.get(0);
					//截取年数
					String year = str.substring(0, 4);
					//截取第几学期
					String xq = str.substring(4);
					if("1".equalsIgnoreCase(xq)){
						xq = "第一学期";
					}
					if("2".equalsIgnoreCase(xq)){
						xq = "第二学期";
					}
					savePath += "/"+ year + xq +"课程设置表.xls";
				}else{
					savePath += "/"+"课程设置表.xls";
				}
				
				OutputStream os = new FileOutputStream(savePath);
				    //根据传进来的file对象创建可写入的Excel工作薄  
				    wwb = Workbook.createWorkbook(os);  
  
				    /* 
				     * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表 
				     * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样 
				     * 代码中的"0"就是sheet1、其它的一一对应。 
				     * createSheet(sheetName, 0)一个是工作表的名称，另一个是工作表在工作薄中的位置 
				     */  
				    WritableSheet ws = wwb.createSheet("课程设置表导出", 0);  
				    WritableFont fontStyle;
				    WritableFont fontTitleStyle;
					WritableCellFormat contentStyle;
					WritableCellFormat contentTitleStyle;
  
				    //创建单元格样式  
				    fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
					
					fontTitleStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD);
					contentTitleStyle = new WritableCellFormat(fontTitleStyle);
					contentTitleStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
					contentTitleStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentTitleStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//边框颜色
					contentTitleStyle.setBackground(Colour.YELLOW);
  
				   
				    //合并单元格： 第一位与第三位代表列，第二位与第四位代表行    
		            ws.mergeCells(0, 0, 0, 1);     
		            ws.mergeCells(1, 0, 1, 1);     
		            ws.mergeCells(2, 0, 2, 1);
		            ws.mergeCells(3, 0, 3, 1);
		            ws.mergeCells(4, 0, 4, 1);
		            ws.mergeCells(5, 0, 5, 1);
		            ws.mergeCells(6, 0, 6, 1);
		            ws.mergeCells(7, 0, 7, 1);
		            ws.mergeCells(8, 0, 8, 1);
		            ws.mergeCells(9, 0, 9, 1);
		            ws.mergeCells(10, 0, 10, 1);
		            ws.mergeCells(11, 0, 12, 0);
		            ws.mergeCells(13, 0, 13, 1);
		            ws.mergeCells(14, 0, 14, 1);
		            ws.mergeCells(15, 0, 15, 1);
		            ws.mergeCells(16, 0, 21, 0);
		            ws.mergeCells(22, 0, 22, 1);
		            ws.mergeCells(23, 0, 23, 1);
		            ws.mergeCells(24, 0, 24, 1);
		            ws.mergeCells(25, 0, 25, 1);
		            ws.mergeCells(26, 0, 26, 1);
				              
		            /* 
		             * 添加单元格(Cell)内容addCell() 
		             * 添加Label对象Label() 
		             * 数据的类型有很多种、在这里你需要什么类型就导入什么类型 
		             * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label 
		             * Label(i, 0, columns[i], wcf) 
		             * 其中i为列、0为行、columns[i]为数据、wcf为样式 
		             * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中 
		             */  
		            ws.addCell(new Label(0, 0, "学年学期编码", contentTitleStyle));
		            ws.addCell(new Label(1, 0, "专业代码", contentTitleStyle));
		            ws.addCell(new Label(2, 0, "专业名称（全称）", contentTitleStyle));
		            ws.addCell(new Label(3, 0, "专业方向代码", contentTitleStyle));
		            ws.addCell(new Label(4, 0, "专业方向名称（全称）", contentTitleStyle));
		            ws.addCell(new Label(5, 0, "课程代码", contentTitleStyle));
		            ws.addCell(new Label(6, 0, "课程名称", contentTitleStyle));
		            ws.addCell(new Label(7, 0, "课程类型", contentTitleStyle));
		            ws.addCell(new Label(8, 0, "课程属性", contentTitleStyle));
		            ws.addCell(new Label(9, 0, "课程性质", contentTitleStyle));
		            ws.addCell(new Label(10, 0, "教学计划规定课时数（学时）", contentTitleStyle));
		            ws.addCell(new Label(11, 0, "实践课程", contentTitleStyle));
		            ws.addCell(new Label(11, 1, "课时数", contentTitleStyle));
		            ws.addCell(new Label(12, 1, "比例（%）", contentTitleStyle));
		            ws.addCell(new Label(13, 0, "是否专业核心课程", contentTitleStyle));
		            ws.addCell(new Label(14, 0, "是否校企合作开发课程 ", contentTitleStyle));
		            ws.addCell(new Label(15, 0, "精品课程", contentTitleStyle));
		            ws.addCell(new Label(16, 0, "使用教材", contentTitleStyle));
		            ws.addCell(new Label(16, 1, "教材名称(全称)", contentTitleStyle));
		            ws.addCell(new Label(17, 1, "版本日期（年）", contentTitleStyle));
		            ws.addCell(new Label(18, 1, "出版社", contentTitleStyle));
		            ws.addCell(new Label(19, 1, "第一作者", contentTitleStyle));
		            ws.addCell(new Label(20, 1, "教材性质", contentTitleStyle));
		            ws.addCell(new Label(21, 1, "教材类型", contentTitleStyle));
		            ws.addCell(new Label(22, 0, "授课年级", contentTitleStyle));
		            ws.addCell(new Label(23, 0, "主要授课地点", contentTitleStyle));
		            ws.addCell(new Label(24, 0, "主要授课方式", contentTitleStyle));
		            ws.addCell(new Label(25, 0, "考试/考核主要方法", contentTitleStyle));
		            ws.addCell(new Label(26, 0, "课证融通课程", contentTitleStyle));
		            
		           
		            
		            ws.setColumnView(0, 15); // 设置列的宽度    
		            ws.setColumnView(1, 15); // 设置列的宽度    
		            ws.setColumnView(2, 25); // 设置列的宽度    
		            ws.setColumnView(3, 15); // 设置列的宽度    
		            ws.setColumnView(4, 25); // 设置列的宽度    
		            ws.setColumnView(5, 15); // 设置列的宽度    
		            ws.setColumnView(6, 30); // 设置列的宽度    
		            ws.setColumnView(7, 15); // 设置列的宽度    
		            ws.setColumnView(8, 15); // 设置列的宽度    
		            ws.setColumnView(9, 15); // 设置列的宽度    
		            ws.setColumnView(10, 35); // 设置列的宽度    
		            ws.setColumnView(11, 15); // 设置列的宽度    
		            ws.setColumnView(12, 15); // 设置列的宽度    
		            ws.setColumnView(13, 20); // 设置列的宽度    
		            ws.setColumnView(14, 25); // 设置列的宽度    
		            ws.setColumnView(15, 15); // 设置列的宽度    
		            ws.setColumnView(16, 20); // 设置列的宽度    
		            ws.setColumnView(17, 20); // 设置列的宽度    
		            ws.setColumnView(18, 15); // 设置列的宽度    
		            ws.setColumnView(19, 15); // 设置列的宽度    
		            ws.setColumnView(20, 15); // 设置列的宽度    
		            ws.setColumnView(21, 15); // 设置列的宽度  
		            ws.setColumnView(22, 15); // 设置列的宽度    
		            ws.setColumnView(23, 15); // 设置列的宽度    
		            ws.setColumnView(24, 15); // 设置列的宽度    
		            ws.setColumnView(25, 20); // 设置列的宽度    
		            ws.setColumnView(26, 15); // 设置列的宽度    
				   
		            
			        //判断表中是否有数据  
			        if (vector != null && vector.size() > 0) {  
			              
			            //循环写入表中数据  
			            for (int i = 0, k = 0 ; k < vector.size(); k+=27,i++) {  
  
			            	for (int j = 0; j < 27; j++) {
			            	   
			                   
		            		   //这里不引用样式了、j为列、(i+1)为行、因为表头占去了一行、所以后面的就+1  
		            		   ws.addCell(new Label(j, i + 2, MyTools.StrFiltr(vector.get(k+j)).toString(), contentStyle));  
		                 
			                   
			            	} 
			            	
			            }
			        } 
			       
			        

			        //写入Exel工作表  
			        wwb.write();  
			        //关闭Excel工作薄对象   
			        wwb.close();
			        //关闭输出
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
			return savePath;   
		}
	
	//转换含有html的字符串
//	public String changeString(String str){
//		str = str.replaceAll("<","&lt;");
//		str = str.replaceAll(">","&gt;");
//		str = str.replaceAll(" ","&nbsp;");
//		str = str.replaceAll("&","&amp;");
//		str = str.replaceAll("'","&apos;");
//		str = str.replaceAll("\"","&quot;");
//		return str;
//	}
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getKCDM() {
		return KCDM;
	}

	public void setKCDM(String kCDM) {
		KCDM = kCDM;
	}

	public String getKCMC() {
		return KCMC;
	}

	public void setKCMC(String kCMC) {
		KCMC = kCMC;
	}

	public String getKCLX() {
		return KCLX;
	}

	public void setKCLX(String kCLX) {
		KCLX = kCLX;
	}

	public String getZYDM() {
		return ZYDM;
	}

	public void setZYDM(String zYDM) {
		ZYDM = zYDM;
	}

	public String getZYMC() {
		return ZYMC;
	}

	public void setZYMC(String zYMC) {
		ZYMC = zYMC;
	}

	public String getXNXQBM() {
		return XNXQBM;
	}

	public void setXNXQBM(String xNXQBM) {
		XNXQBM = xNXQBM;
	}
	
	public String getXNXQMC() {
		return XNXQMC;
	}

	public void setXNXQMC(String xNXQMC) {
		XNXQMC = xNXQMC;
	}

	public String getKSXKSJ() {
		return KSXKSJ;
	}

	public void setKSXKSJ(String kSXKSJ) {
		KSXKSJ = kSXKSJ;
	}

	public String getJSXKSJ() {
		return JSXKSJ;
	}

	public void setJSXKSJ(String jSXKSJ) {
		JSXKSJ = jSXKSJ;
	}

	public String getKSXKXS() {
		return KSXKXS;
	}

	public void setKSXKXS(String kSXKXS) {
		KSXKXS = kSXKXS;
	}

	public String getJSXKXS() {
		return JSXKXS;
	}

	public void setJSXKXS(String jSXKXS) {
		JSXKXS = jSXKXS;
	}

	public String getXX_KCDM() {
		return XX_KCDM;
	}

	public void setXX_KCDM(String xX_KCDM) {
		XX_KCDM = xX_KCDM;
	}

	public String getXX_KCMC() {
		return XX_KCMC;
	}

	public void setXX_KCMC(String xX_KCMC) {
		XX_KCMC = xX_KCMC;
	}

	public String getXX_KCLX() {
		return XX_KCLX;
	}

	public void setXX_KCLX(String xX_KCLX) {
		XX_KCLX = xX_KCLX;
	}

	public String getXX_XNXQ() {
		return XX_XNXQ;
	}

	public void setXX_XNXQ(String xX_XNXQ) {
		XX_XNXQ = xX_XNXQ;
	}

	public String getXX_BJRS() {
		return XX_BJRS;
	}

	public void setXX_BJRS(String xX_BJRS) {
		XX_BJRS = xX_BJRS;
	}

	public String getXX_SKRQ() {
		return XX_SKRQ;
	}

	public void setXX_SKRQ(String xX_SKRQ) {
		XX_SKRQ = xX_SKRQ;
	}

	public String getXX_SKSJ() {
		return XX_SKSJ;
	}

	public void setXX_SKSJ(String xX_SKSJ) {
		XX_SKSJ = xX_SKSJ;
	}

	public String getXX_XUEF() {
		return XX_XUEF;
	}

	public void setXX_XUEF(String xX_XUEF) {
		XX_XUEF = xX_XUEF;
	}

	public String getXX_ZOKS() {
		return XX_ZOKS;
	}

	public void setXX_ZOKS(String xX_ZOKS) {
		XX_ZOKS = xX_ZOKS;
	}

	public String getXX_JSBH() {
		return XX_JSBH;
	}

	public void setXX_JSBH(String xX_JSBH) {
		XX_JSBH = xX_JSBH;
	}

	public String getXX_SKJS() {
		return XX_SKJS;
	}

	public void setXX_SKJS(String xX_SKJS) {
		XX_SKJS = xX_SKJS;
	}

	public String getXX_CDYQ() {
		return XX_CDYQ;
	}

	public void setXX_CDYQ(String xX_CDYQ) {
		XX_CDYQ = xX_CDYQ;
	}

	public String getXX_CDMC() {
		return XX_CDMC;
	}

	public void setXX_CDMC(String xX_CDMC) {
		XX_CDMC = xX_CDMC;
	}

	public String getXX_SKZC() {
		return XX_SKZC;
	}

	public void setXX_SKZC(String xX_SKZC) {
		XX_SKZC = xX_SKZC;
	}

	public String getXX_ZYBH() {
		return XX_ZYBH;
	}

	public void setXX_ZYBH(String xX_ZYBH) {
		XX_ZYBH = xX_ZYBH;
	}
	
	public String getXX_ZYNA() {
		return XX_ZYNA;
	}

	public void setXX_ZYNA(String xX_ZYNA) {
		XX_ZYNA = xX_ZYNA;
	}

	public String getXX_XXKZBBH() {
		return XX_XXKZBBH;
	}

	public void setXX_XXKZBBH(String xX_XXKZBBH) {
		XX_XXKZBBH = xX_XXKZBBH;
	}

	public String getXX_XXKMXBH() {
		return XX_XXKMXBH;
	}

	public void setXX_XXKMXBH(String xX_XXKMXBH) {
		XX_XXKMXBH = xX_XXKMXBH;
	}

	public String getXX_SKZCXQ() {
		return XX_SKZCXQ;
	}

	public void setXX_SKZCXQ(String xX_SKZCXQ) {
		XX_SKZCXQ = xX_SKZCXQ;
	}

	public String getXX_SJXL() {
		return XX_SJXL;
	}

	public void setXX_SJXL(String xX_SJXL) {
		XX_SJXL = xX_SJXL;
	}

	public String getKMBH() {
		return KMBH;
	}

	public void setKMBH(String kMBH) {
		KMBH = kMBH;
	}

	public String getMSG2() {
		return MSG2;
	}

	public void setMSG2(String mSG2) {
		MSG2 = mSG2;
	}

	public String getXX_BJMC() {
		return XX_BJMC;
	}

	public void setXX_BJMC(String xX_BJMC) {
		XX_BJMC = xX_BJMC;
	}
	public String getXX_KSXS() {
		return XX_KSXS;
	}

	public void setXX_KSXS(String xX_KSXS) {
		XX_KSXS = xX_KSXS;
	}

	public String getXX_WEEK() {
		return XX_WEEK;
	}

	public void setXX_WEEK(String xX_WEEK) {
		XX_WEEK = xX_WEEK;
	}

	public String getXBBH() {
		return XBBH;
	}

	public void setXBBH(String xBBH) {
		XBBH = xBBH;
	}
	
	public String getXBMC() {
		return XBMC;
	}

	public void setXBMC(String xBMC) {
		XBMC = xBMC;
	}

	public String getKSXS() {
		return KSXS;
	}

	public void setKSXS(String kSXS) {
		KSXS = kSXS;
	}

	public String getFirst_APP() {
		return first_APP;
	}

	public void setFirst_APP(String first_APP) {
		this.first_APP = first_APP;
	}

	public String getSecond_APP() {
		return second_APP;
	}

	public void setSecond_APP(String second_APP) {
		this.second_APP = second_APP;
	}

	public String getThird_APP() {
		return third_APP;
	}

	public void setThird_APP(String third_APP) {
		this.third_APP = third_APP;
	}
	
	
}