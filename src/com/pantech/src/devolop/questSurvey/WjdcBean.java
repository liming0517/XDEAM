package com.pantech.src.devolop.questSurvey;
/*
@date 2015.11.25
@author yeq
模块：调查问卷
说明:
重要及特殊方法：
*/
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Pie;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class WjdcBean {
	private String USERCODE;//用户编号
	private String Auth;//用户权限
	
	private String WW_WJBH; //问卷编号
	private String WW_WJLX; //问卷类型
	private String WW_BT; //标题
	private String WW_XNXQBM; //学年学期编码
	private String WW_KSSJ; //开始时间
	private String WW_JSSJ; //结束时间
	
	private String WT_TMBH; //题目编号
	private String WT_TMLX; //题目类型
	private String WT_TMNR; //题目内容
	
	private String CJR; //创建人
	private String CJSJ; //创建时间
	private String ZT; //状态

	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public WjdcBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-11-25
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		Auth = "";//用户权限
		WW_WJBH = ""; //问卷编号
		WW_WJLX = ""; //问卷类型
		WW_BT = ""; //标题
		WW_XNXQBM = ""; //学年学期编码
		WW_KSSJ = ""; //开始时间
		WW_JSSJ = ""; //结束时间
		WT_TMBH = ""; //题目编号
		WT_TMLX = ""; //题目类型
		WT_TMNR = ""; //题目内容
		CJR = ""; //创建人
		CJSJ = ""; //创建时间
		ZT = ""; //状态
	}
	
	/**
	 * 添加授课计划
	 * @date:2017-05-22
	 * @author:yeq
	 * @throws SQLException
	 */
	/*
	public void addSkjh() throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vecMain = null; // 结果集
		Vector vecDetail = null;
		Vector tempVec = new Vector();
		Vector sqlVec = new Vector();
		
		int mainNum = 0;
		String mainCode = "";
		long detailNum = 0;
		String detailCode = "";
		String xnxqbm = "";
		String classCode = "";
		String kcbh = "";
		String kcmc = "";
		String kclx = "";
		String jsbh = "";
		String jsmc = "";
		
		int mainTotalNum = 0;
		int detailTotalNum = 0;
		int scoreTotalNum = 0;
		
		//其他数据
//		insert into [XDEAM].[dbo].[V_学校班级数据子类] (行政班代码,年级代码,行政班名称,系部代码,专业代码,建班年月,状态)
//		select 班级代码,年级代码,班级名称,
//		case left(班级名称,3) when '国商部' then 'C01' when '旅游部' then 'C02' when '汽修部' then 'C03' when '烹饪部' then 'C04' when '信息部' then 'C05' else 'C00' end as 系部代码,
//		专业代码,'20'+left(班级代码,2)+'05' as 建班年月,'1' as 状态 
//		from [XDEAM_OLD].[dbo].[XDEAM_tempClassInfo]
//		where 班级代码 not in (select 行政班代码 from [XDEAM].[dbo].[V_学校班级数据子类])

//		insert into [XDEAM].[dbo].V_学生基本数据子类 (学籍号,学号,专业代码,行政班代码,班内学号,姓名,学生状态)
//		select distinct a.学籍号,a.学号,b.专业代码,a.班级代码,right(a.学号,2) as 班内学号,a.姓名,'07' as 学生状态 from [XDEAM_OLD].[dbo].XDEAM_tempStuInfo a
//		left join [XDEAM_OLD].[dbo].XDEAM_tempClassInfo b on b.班级代码=a.班级代码
//		where a.学籍号 not in (select 学籍号 from [XDEAM].[dbo].V_学生基本数据子类)
//		order by a.班级代码,a.学籍号

		
//		delete from V_基础信息_专业科目类型信息表
//		insert into V_基础信息_专业科目类型信息表 (专业代码,科目类型,创建人,创建时间,状态)
//		select 专业代码,'1','post',getDate(),'1' from V_专业基本信息数据子类
		
		//查询主表编号
		sql = "select top 1 substring(授课计划主表编号,6,len(授课计划主表编号)) from [XDEAM].[dbo].V_规则管理_授课计划主表 order by 授课计划主表编号 desc";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			mainNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(0)));
		}else{
			mainNum = 10000000;
		}
		
		//查询明细编号
		sql = "select top 1 substring(授课计划明细编号,8,len(授课计划明细编号)) from [XDEAM].[dbo].V_规则管理_授课计划明细表 order by 授课计划明细编号 desc";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			detailNum = MyTools.StringToLong(MyTools.StrFiltr(tempVec.get(0)));
		}else{
			detailNum = 10000000000L;
		}
		
		//授课计划主表信息
		sql = "select distinct 学年学期编码,班级代码,专业代码,专业名称 from [XDEAM_OLD].[dbo].V_成绩信息结果表 order by 学年学期编码,班级代码";
		vecMain = db.GetContextVector(sql);
		
		for(int i=0; i<vecMain.size(); i+=4){
			mainNum++;
			mainCode = "SKJH_" + mainNum;
			
			xnxqbm = MyTools.StrFiltr(vecMain.get(i));
			classCode = MyTools.StrFiltr(vecMain.get(i+1));
			
			sql = "insert into V_规则管理_授课计划主表 (授课计划主表编号,学年学期编码,行政班代码,专业代码,专业名称,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(mainCode) + "'," +
				"'" + MyTools.fixSql(xnxqbm) + "'," +
				"'" + MyTools.fixSql(classCode) + "'," +
				"'" + MyTools.fixSql(MyTools.StrFiltr(vecMain.get(i+2))) + "'," +
				"'" + MyTools.fixSql(MyTools.StrFiltr(vecMain.get(i+3))) + "'," +
				"'post',getDate(),'1')";
			db.executeInsertOrUpdate(sql);
			mainTotalNum++;
			
			//授课计划明细表信息
			sql = "select 学年学期编码,班级代码,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名," +
				"case when 平时比例='50' and 期中比例='20' and 期末比例='30' then '1' when 平时比例='60' and 期末比例='40' then '2' else '3' end as 总评比例选项," +
				"平时比例,期中比例,期末比例 from (select distinct 学年学期编码,班级代码,课程代码,课程名称,case when left(课程代码,1)='0' then '01' else '02' end as 课程类型," +
				"isnull(教师编号,'') as 授课教师编号,isnull(教师姓名,'') as 授课教师姓名," +
				"case 平时比例 when '0' then '' when '' then '' else cast(cast(平时比例 as float)*100 as nvarchar) end as 平时比例," +
				"case 期中比例 when '0' then '' when '' then '' else cast(cast(期中比例 as float)*100 as nvarchar) end as 期中比例," +
				"case 期末比例 when '0' then '' when '' then '' else cast(cast(期末比例 as float)*100 as nvarchar) end as 期末比例 " +
				"from [XDEAM_OLD].[dbo].V_成绩信息结果表) as t " +
				"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
				"and 班级代码='" + MyTools.fixSql(classCode) + "' " +
				"order by 课程代码";
			vecDetail = db.GetContextVector(sql);
			
			for(int j=0; j<vecDetail.size(); j+=11){
				detailNum++;
				detailCode = "SKJHMX_" + detailNum;
				sqlVec.clear();
				
				kcbh = MyTools.StrFiltr(vecDetail.get(j+2));
				kcmc = MyTools.StrFiltr(vecDetail.get(j+3));
				kclx = MyTools.StrFiltr(vecDetail.get(j+4));
				jsbh = MyTools.StrFiltr(vecDetail.get(j+5));
				jsmc = MyTools.StrFiltr(vecDetail.get(j+6));
				
				sql = "insert into V_规则管理_授课计划明细表 values(" +
					"'" + detailCode + "'," +
					"'" + mainCode + "'," +
					"'" + MyTools.fixSql(kcbh) + "'," +
					"'" + MyTools.fixSql(kcmc) + "'," +
					"'" + MyTools.fixSql(kclx) + "'," +
					"'" + MyTools.fixSql(jsbh) + "'," +
					"'" + MyTools.fixSql(jsmc) + "'," +
					"'0','0','0','0','0','0','0'," +
					"'','','',''," +
					"'post',getDate(),'1','1',null,null,null,null,null,null,'1')";
				db.executeInsertOrUpdate(sql);
				detailTotalNum++;
				
				sql = "select distinct 学籍号,姓名,PSOriginalResult,QZOriginalResult,SXOriginalResult," +
					"QMOriginalResult,ZPOriginalResult,'','','','','' from [XDEAM_OLD].[dbo].V_成绩信息结果表 " +
					"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
					"and 班级代码='" + MyTools.fixSql(classCode) + "' " +
					"and 课程代码='" + MyTools.fixSql(kcbh) + "'";
				tempVec = db.GetContextVector(sql);
							
				if(tempVec!=null && tempVec.size()>0){
					for(int m=0; m<tempVec.size(); m+=12){
						sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,相关编号,平时,期中,实训,期末,总评,重修1,重修2,补考,大补考,打印成绩,成绩状态,创建人,创建时间,状态) values(" +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+1))) + "'," +
							"'" + detailCode + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+2))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+3))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+4))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+5))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+6))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+7))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+8))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+9))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+10))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(m+11))) + "'," +
							"'1','post',getDate(),'1'" +
							")";
						sqlVec.add(sql);
						scoreTotalNum++;
					}
				}
				
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					//更新成绩比例设置信息
					sql = "update V_成绩管理_登分设置信息表 set " +
						"总评比例选项='" + MyTools.fixSql(MyTools.StrFiltr(vecDetail.get(j+7))) + "'," +
						"平时比例='" + MyTools.fixSql(MyTools.StrFiltr(vecDetail.get(j+8))) + "'," +
						"期中比例='" + MyTools.fixSql(MyTools.StrFiltr(vecDetail.get(j+9))) + "'," +
						"期末比例='" + MyTools.fixSql(MyTools.StrFiltr(vecDetail.get(j+10))) + "' " +
						"where 相关编号='" + MyTools.fixSql(detailCode) + "'";
					db.executeInsertOrUpdate(sql);
				}
			}
		}
		
		//更新 V_成绩管理_学生成绩信息表 科目编号
		sql = "update a set a.科目编号=b.科目编号 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号";
		db.executeInsertOrUpdate(sql);
	
		System.out.println("======================主表条目数："+mainTotalNum+"----明细条目数:"+detailTotalNum+"-----------成绩条目数:"+scoreTotalNum);
	}
	*/
	/**
	 * 添加选修课相关信息
	 * @date:2016-07-15
	 * @author:yeq
	 * @throws SQLException
	 */
	/*
	public void addXXK() throws SQLException {
		Vector vec = null;
		Vector sqlVec = new Vector();
		Vector tempVec = null;
		String sql = "";
		
		int mainNum = 900000000;
		String mianCode = "XXK_900000000";
		long detailNum = 900000000L;
		String detailCode = "XXKMX_900000000";
		
		String xnxqbm = "";
		String xnxqmc = "";
		String kcbh = "";
		String kcmc = "";
		String skjsbhArray[] = new String[0];
		String tempSkjsbh = "";
		String skjsxmArray[] = new String[0];
		String tempSkjsxm = "";
		String kkbjArray[] = new String[0];
		String tempSkzcmx = "";
		Vector skzcmxVec = null;
		String kmCode = "";
		
		sql = "with cte as(" +
			"select 学年学期编码,课程编号,课程名称,(select top 1 工号 from V_教职工基本数据子类 where 姓名=t.教师 order by 工号 desc) as 工号,t.教师,开课班级 " +
			"from (select distinct a.学期+'01' as 学年学期编码,a.开课班级,b.课程编号,b.课程名称," +
			"(select top 1 教师 from XJJW.cw0a185.开课课程20040901 where 开课班级=a.开课班级) as 教师 from XJJW.cw0a185.课程明细20040901 a " +
			"left join XJJW.cw0a185.课程表 b on b.课程编号=a.课程编号 " +
			"where a.开课班级 like '%选修%' and left(a.学期,4)<2014) as t) " +
			"select distinct t.学年学期编码,t.课程编号,t.课程名称," +
			"stuff((select ','+工号 from cte where 学年学期编码=t.学年学期编码 and 课程编号=t.课程编号 for XML PATH('')),1,1,'') as 工号," +
			"stuff((select ','+教师 from cte where 学年学期编码=t.学年学期编码 and 课程编号=t.课程编号 for XML PATH('')),1,1,'') as 教师," +
			"stuff((select ','+开课班级 from cte where 学年学期编码=t.学年学期编码 and 课程编号=t.课程编号 for XML PATH('')),1,1,'') as 开课班级 " +
			"from cte as t order by 学年学期编码";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=6){
				mainNum++;
				mianCode = "XXK_" + mainNum;
				
				xnxqbm = MyTools.StrFiltr(vec.get(i));
				xnxqmc = xnxqbm.substring(0, 4)+"学年 "+(xnxqbm.substring(5,6).equalsIgnoreCase("1")?"第一学期":"第二学期");
				kcbh = MyTools.StrFiltr(vec.get(i+1));
				kcmc = MyTools.StrFiltr(vec.get(i+2));
				skjsbhArray = MyTools.StrFiltr(vec.get(i+3)).split(",");
				skjsxmArray = MyTools.StrFiltr(vec.get(i+4)).split(",");
				kkbjArray = MyTools.StrFiltr(vec.get(i+5)).split(",");
				tempSkjsbh = "";
				tempSkjsxm = "";
				tempSkzcmx = "";
				skzcmxVec = new Vector();
				
				for(int j=0; j<skjsbhArray.length; j++){
					tempSkjsbh += skjsbhArray[j]+"&";
					tempSkjsxm += skjsxmArray[j]+"&";
				}
				tempSkjsbh = tempSkjsbh.substring(0, tempSkjsbh.length()-1);
				tempSkjsxm = tempSkjsxm.substring(0, tempSkjsxm.length()-1);
				
				if(skjsbhArray.length == 1){
					tempSkzcmx = "1-18";
					skzcmxVec.add("1-18");
				}else if(skjsbhArray.length == 2){
					tempSkzcmx = "1-9&10-18";
					skzcmxVec.add("1-9");
					skzcmxVec.add("10-18");
				}else if(skjsbhArray.length == 3){
					tempSkzcmx = "1-6&7-12&13-18";
					skzcmxVec.add("1-6");
					skzcmxVec.add("7-12");
					skzcmxVec.add("13-18");
				}else if(skjsbhArray.length == 4){
					tempSkzcmx = "1-5&6-10&11-14&15-18";
					skzcmxVec.add("1-5");
					skzcmxVec.add("6-10");
					skzcmxVec.add("11-14");
					skzcmxVec.add("15-18");
				}
				
				sql = "insert into V_规则管理_选修课授课计划主表 (授课计划主表编号,学年学期编码,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,节数," +
					"固排已排节数,实际已排节数,连节,连次,固排连次次数,实际连次次数,场地要求,场地名称,授课周次,授课周次详情,创建人,创建时间,状态) values(" +
					"'" + MyTools.fixSql(mianCode) + "'," +
					"'" + MyTools.fixSql(xnxqbm) + "'," +
					"'" + MyTools.fixSql(kcbh) + "'," +
					"'" + MyTools.fixSql(kcmc) + "'," +
					"'03'," +
					"'" + MyTools.fixSql(tempSkjsbh.replaceAll(",","&")) + "'," +
					"'" + MyTools.fixSql(tempSkjsxm.replaceAll(",","&")) + "'," +
					"'4','0','0','0','0','0','0','','','1-18'," +
					"'" + MyTools.fixSql(tempSkzcmx) + "'," +
					"'post',getDate(),'1')";
				sqlVec.add(sql);
				
				sql = "insert into V_成绩管理_科目课程信息表 (学年学期编码,学年学期名称,课程代码,课程名称,课程类型,是否参与绩点,总评比例选项,平时比例," +
					"期中比例,实训比例,期末比例,成绩类型,实训,创建人,创建时间,状态) values (" +
					"'" + MyTools.fixSql(xnxqbm) + "'," +
					"'" + MyTools.fixSql(xnxqmc) + "'," +
					"'" + MyTools.fixSql(kcbh) + "'," +
					"'" + MyTools.fixSql(kcmc) + "'," +
					"'3','1','1','20','','','80','1','0','post',getDate(),'1'" +
					")";
				db.executeInsertOrUpdate(sql);
				
				sql = "select top 1 科目编号 from V_成绩管理_科目课程信息表 order by 科目编号 desc";
				tempVec = db.GetContextVector(sql);
				if(tempVec!=null && tempVec.size()>0){
					kmCode = MyTools.StrFiltr(tempVec.get(0));
				}else{
					kmCode = "";
				}
				
				for(int j=0; j<skjsbhArray.length; j++){
					detailNum++;
					detailCode = "XXKMX_" + detailNum;
					
					sql = "insert into V_规则管理_选修课授课计划明细表 (授课计划明细编号,授课计划主表编号,选修班名称,授课教师编号,授课教师姓名,场地要求," +
						"场地名称,授课周次,创建人,创建时间,状态,报名人数,学分,总课时) values(" +
						"'" + MyTools.fixSql(detailCode) + "'," +
						"'" + MyTools.fixSql(mianCode) + "'," +
						"'" + MyTools.fixSql(kkbjArray[j]) + "'," +
						"'" + MyTools.fixSql(skjsbhArray[j]) + "'," +
						"'" + MyTools.fixSql(skjsxmArray[j]) + "'," +
						"'',''," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(skzcmxVec.get(j))) + "'," +
						"'post',getDate(),'1','0','2.0','40')";
					sqlVec.add(sql);
					
					sql = "insert into V_规则管理_学生选修课关系表 " +
						"select '" + xnxqbm + "',学号,'','post',getDate(),'1' from XJJW.cw0a185.课程明细20040901 " +
						"where 开课班级='" + MyTools.fixSql(kkbjArray[j]) + "' " +
						"and 学期='" + MyTools.fixSql(xnxqbm.substring(0, 5)) + "'";
					sqlVec.add(sql);
					
					sql = "insert into V_成绩管理_登分教师信息表 (科目编号,来源类型,相关编号,行政班名称,课程代码,课程名称,登分教师编号,登分教师姓名,打印锁定,创建人,创建时间,状态) values (" +
						"'" + MyTools.fixSql(kmCode) + "'," +
						"'3'," +
						"'" + MyTools.fixSql(detailCode) + "'," +
						"'" + MyTools.fixSql(kkbjArray[j]) + "'," +
						"'" + MyTools.fixSql(kcbh) + "'," +
						"'" + MyTools.fixSql(kcmc) + "'," +
						"'" + MyTools.fixSql("@"+skjsbhArray[j]+"@") + "'," +
						"'" + MyTools.fixSql("@"+skjsxmArray[j]+"@") + "'," +
						"'1','post',getDate(),'1')";
					sqlVec.add(sql);
					
					sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,平时,期中,实训,期末,总评,重修1,重修2,补考,大补考,打印成绩,成绩状态,创建人,创建时间,状态) " +
						"select a.学号,b.姓名,'" + MyTools.fixSql(kmCode) + "','" + MyTools.fixSql(detailCode) + "'," +
						"a.平时成绩,a.期中成绩,a.实训成绩,a.期末成绩,a.总评成绩,a.重修成绩1,a.重修成绩2,a.补考,a.大补考,a.打印成绩," +
						"'1','post',getDate(),'1' from XJJW.cw0a185.课程明细20040901 a " +
						"left join XJJW.cw0a185.学生表 b on b.学号=a.学号 " +
						"where a.开课班级='" + MyTools.fixSql(kkbjArray[j]) + "' " +
						"and a.学期='" + MyTools.fixSql(xnxqbm.substring(0, 5)) + "' and 姓名 is not null";
					sqlVec.add(sql);
				}
				
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					sqlVec.clear();
				}
			}
			
			sql = "update V_成绩管理_学生成绩信息表 set " +
				"平时=(case 平时 when '0' then '' else 平时 end)," +
				"期中=(case 期中 when '0' then '' else 期中 end)," +
				"实训=(case 实训 when '0' then '' else 实训 end)," +
				"期末=(case 期末 when '0' then '' else 期末 end)," +
				"总评=(case 总评 when '0' then '' else 总评 end)," +
				"重修1=(case 重修1 when '0' then '' else 重修1 end)," +
				"重修2=(case 重修2 when '0' then '' else 重修2 end)," +
				"补考=(case 补考 when '0' then '' else 补考 end)," +
				"大补考=(case 大补考 when '0' then '' else 大补考 end)," +
				"打印成绩=(case 打印成绩 when '0' then '' else 打印成绩 end) " +
				"where 相关编号 like 'XXKMX_9%'";
			db.executeInsertOrUpdate(sql);
		}
	}
	*/
	
	/**
	 * 查询当前学年学期
	 * @date:2016-07-18
	 * @author: yeq
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		Vector vec = null;
		String curXnxq = "";
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where convert(nvarchar(10),getDate(),21)>=学期开始时间 order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0)
			curXnxq = MyTools.StrFiltr(vec.get(0));
		
		return curXnxq;
	}
	
	/**
	 * 查询学年学期下拉框数据
	 * @date:2016-07-18
	 * @author: yeq
	 * @throws SQLException
	 */
	public Vector loadSemCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年学期编码 as comboValue,学年学期名称 as comboName from V_规则管理_学年学期表 " +
				"order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取问卷调查列表
	 * @date:2015-12-02
	 * @author:yeq
	 * @return String 结果集
	 * @throws SQLException
	 */
	public String loadWjdcList() throws SQLException{
		Vector vec = new Vector();
		String resultJson = "[";
		String startDateArray[] = new String[0];
		String startDate = "";
		String endDateArray[] = new String[0];
		String endDate = "";
		String sql = "";
		String admin = MyTools.getProp(request, "Base.admin");
		String teaAuth = MyTools.getProp(request, "Base.wjdcTea");
		String stuAuth = MyTools.getProp(request, "Base.wjdcStu");
		
		//学生查看问卷/教师查看统计
		if(this.getAuth().indexOf(admin)>-1 || this.getAuth().indexOf(teaAuth)>-1){
			sql = "select 问卷编号,标题,convert(nvarchar(10),开始时间,21) as 开始时间,convert(nvarchar(10),结束时间,21) as 结束时间,'2' as 状态 " +
				"from V_问卷调查_问卷信息表 where 状态='1' and getDate()>结束时间 " +
				"and 学年学期编码='" + MyTools.fixSql(this.getWW_XNXQBM()) + "'" +
				"order by 创建时间 desc";
			vec = db.GetContextVector(sql);
		}else if(this.getAuth().indexOf(stuAuth) > -1){
			sql = "select a.问卷编号,a.标题,convert(nvarchar(10),a.开始时间,21) as 开始时间,convert(nvarchar(10),a.结束时间,21) as 结束时间," +
				"case when convert(nvarchar(10),getDate(),21)<convert(nvarchar(10),a.开始时间,21) then '0' " +
				"when (case a.问卷类型 when '02' then (select count(*) from V_问卷调查_教师满意度评分表 where 问卷编号=a.问卷编号 and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
				"else (select count(*) from V_问卷调查_结果信息表 where 问卷编号=a.问卷编号 and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "') end)>0 then '2' " +
				"when convert(nvarchar(10),getDate(),21) not between convert(nvarchar(10),a.开始时间,21) and convert(nvarchar(10),a.结束时间,21) then '3' " +
				"else '1' end as 状态 " +
				"from V_问卷调查_问卷信息表 a where a.状态='1' " +
				"and a.学年学期编码='" + MyTools.fixSql(this.getWW_XNXQBM()) + "' " +
				"order by a.开始时间 desc";
			vec = db.GetContextVector(sql);
		}
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=5){
				resultJson += "{\"code\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
						"\"title\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\",";
				
				startDateArray = MyTools.StrFiltr(vec.get(i+2)).split("-");
				endDateArray = MyTools.StrFiltr(vec.get(i+3)).split("-");
				startDate = startDateArray[0]+"年"+startDateArray[1]+"月"+startDateArray[2]+"日";
				endDate = endDateArray[0]+"年"+endDateArray[1]+"月"+endDateArray[2]+"日";
				
				resultJson += "\"date\":\"" + startDate+" － "+endDate + "\"," +
						"\"state\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"" +
						"},";
			}
		}else{
			resultJson += ",";
		}
		resultJson = resultJson.substring(0, resultJson.length()-1) + "]";
		return resultJson;
	}
	
	/**
	 * 读取问卷调查
	 * @date:2015-11-30
	 * @author:yeq
	 * @return String 结果集
	 * @throws SQLException
	 */
	public String loadWjdc() throws SQLException{
		Vector vec = null;
		String resultJson = "";
		String sql = "";
		String doFlag = "0";
		String limit = "0";
		boolean flag = true;
		int addIndex = 0;
		
		//查询当前用户是否做过该问卷调查
//		sql = "select a.标题,a.问卷类型,a.学年学期编码," +
//			"case when (case 问卷类型 when '02' then (select count(*) from V_问卷调查_教师满意度评分表 where 状态='1' and 问卷编号=a.问卷编号 " +
//			"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
//			"else (select count(*) from V_问卷调查_结果信息表 where 状态='1' and 问卷编号=a.问卷编号 " +
//			"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "') end)>0 then '1' " +
//			"when convert(nvarchar(10),getDate(),21) not between convert(nvarchar(10),a.开始时间,21) and convert(nvarchar(10),a.结束时间,21) then '2' " +
//			"else '0' end as 是否完成 from V_问卷调查_问卷信息表 a where a.状态='1' " +
//			"and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		sql = "select t.标题,t.问卷类型,t.学年学期编码," +
			"case when (case 问卷类型 when '02' then (select count(*) from V_问卷调查_教师满意度评分表 where 状态='1' and 问卷编号=t.问卷编号 " +
			"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "') else (select count(*) from V_问卷调查_结果信息表 where 状态='1' and 问卷编号=t.问卷编号 " +
			"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "') end)>0 then '1' " +
			"when convert(nvarchar(10),getDate(),21) not between convert(nvarchar(10),t.开始时间,21) and convert(nvarchar(10),t.结束时间,21) then '2' else '0' end as 是否完成," +
			"(select count(*) from V_问卷调查_专业教师无限制信息表 a left join V_学校班级数据子类 b on b.专业代码=a.专业代码 left join V_学生基本数据子类 c on c.行政班代码=b.行政班代码 " +
			"where a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' and c.学号='" + MyTools.fixSql(this.getUSERCODE()) + "') as 专业教师无限制 from V_问卷调查_问卷信息表 t where t.状态='1' " +
			"and t.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			this.setWW_BT(MyTools.StrFiltr(vec.get(0)));
			this.setWW_WJLX(MyTools.StrFiltr(vec.get(1)));
			this.setWW_XNXQBM(MyTools.StrFiltr(vec.get(2)));
			doFlag = MyTools.StrFiltr(vec.get(3));
			limit = MyTools.StrFiltr(vec.get(4));
		}
		
		resultJson = "{\"title\":\"" + this.getWW_BT() + "\"," +
				"\"type\":\"" + this.getWW_WJLX() + "\"," +
				"\"doFlag\":\"" + doFlag + "\"," +
				"\"limit\":\"" + limit + "\",";
		
		//判断问卷类型
		if("01".equalsIgnoreCase(this.getWW_WJLX())){
			resultJson += "\"allQuestion\":[";
			sql = "select a.题目编号,a.题目类型,a.题目内容,c.选项,c.选项内容,isnull(d.答案,'') from V_问卷调查_题目信息_选择题 a " +
				"inner join V_问卷调查_问卷题目关系表 b on a.题目编号=b.题目编号 " +
				"inner join V_问卷调查_选项信息表 c on c.题目编号=a.题目编号 " +
				"left join V_问卷调查_结果信息表 d on d.问卷编号=b.问卷编号 and d.题目编号=a.题目编号  and d.创建人='" + MyTools.fixSql(this.getUSERCODE()) + "' and d.状态='1' " +
				"where a.状态='1' and b.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i+=6){
					addIndex = 0;
					flag = true;
					
					resultJson += "{\"code\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
								"\"quesType\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
								"\"question\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
								"\"allOptions\":[";
					while(flag){
						//判断是否同一道题
						if(i+addIndex<vec.size() && MyTools.StrFiltr(vec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+addIndex)))){
							resultJson += "{\"option\":\"" + MyTools.StrFiltr(vec.get(i+addIndex+3))+"."+MyTools.StrFiltr(vec.get(i+addIndex+4)) + "\"},";
							addIndex += 6;
						}else{
							flag = false;
							resultJson = resultJson.substring(0, resultJson.length()-1) + "],";
							addIndex -= 6;
						}
					}
					resultJson +=  "\"answer\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"},";
					
					i += addIndex;
				}
			}else{
				resultJson += ",";
			}
		}
		
		//判断如果是教师满意度调查表，只需要查询填写的结果
		if("02".equalsIgnoreCase(this.getWW_WJLX())){
			Vector stuInfoVec = null;
			//查询学生信息
			sql = "select a.姓名,e.系名称,b.行政班名称 from V_学生基本数据子类 a " +
				"inner join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
				"inner join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
				"inner join V_基础信息_系专业关系表 d on d.专业代码=c.专业代码 " +
				"inner join V_基础信息_系基础信息表 e on e.系代码=d.系代码 " +
				"where a.学号='" + this.getUSERCODE() + "'";
			stuInfoVec = db.GetContextVector(sql);
			
			resultJson += "\"stuName\":\"" + MyTools.StrFiltr(stuInfoVec.get(0)) + "\"," +
					"\"majorName\":\"" + MyTools.StrFiltr(stuInfoVec.get(1)) + "\"," +
					"\"className\":\"" + MyTools.StrFiltr(stuInfoVec.get(2)) + "\"," +
					"\"allQuestion\":[";
			
			//授课计划中已排课程
			sql = "select distinct * from (select distinct b.工号,b.姓名,a.课程代码,a.课程名称,isnull(d.分数,'') as 分数,isnull(d.意见,'') as 意见 " +
				"from V_排课管理_课程表周详情表 a " +
				"inner join V_教职工基本数据子类 b on '@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@'+b.工号+'@%' " +
				"inner join V_学生基本数据子类 c on c.行政班代码=a.行政班代码 " +
				"left join V_问卷调查_教师满意度评分表 d on d.课程代码=a.课程代码 and d.教师编号=b.工号 and d.创建人=c.学号 and d.状态='1' and d.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"where a.状态='1' and c.学号='" + this.getUSERCODE() + "' and a.学年学期编码='" + this.getWW_XNXQBM() + "' ";
			//授课计划中未排课程
			sql += "union all " +
				"select distinct d.工号,d.姓名,a.课程代码,a.课程名称,isnull(e.分数,'') as 分数,isnull(e.意见,'') as 意见 " +
				"from V_规则管理_授课计划明细表 a " +
				"inner join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
				"inner join V_学生基本数据子类 c on c.行政班代码=b.行政班代码 " +
				"inner join V_教职工基本数据子类 d on '@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@'+d.工号+'@%' " +
				"left join V_问卷调查_教师满意度评分表 e on e.课程代码=a.课程代码 and e.教师编号=d.工号 and e.创建人=c.学号 and e.状态='1' and e.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"where a.状态='1' and c.学号='" + this.getUSERCODE() + "' and b.学年学期编码='" + this.getWW_XNXQBM() + "' and (a.节数-a.实际已排节数)>0 ";
			//选修课程
			sql += "union all " +
				"select distinct d.工号,d.姓名,b.课程代码,b.课程名称+'（选修）',isnull(e.分数,'') as 分数,isnull(e.意见,'') as 意见 " +
				"from V_规则管理_选修课授课计划明细表 a " +
				"inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
				"inner join V_规则管理_学生选修课关系表 c on c.授课计划明细编号=a.授课计划明细编号 " +
				"inner join V_教职工基本数据子类 d on '@'+replace(replace(replace(replace(a.授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@'+d.工号+'@%' " +
				"left join V_问卷调查_教师满意度评分表 e on e.课程代码=b.课程代码 and e.教师编号=d.工号 and e.创建人=c.学号 and e.状态='1' and e.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"where a.状态='1' and c.学号='" + this.getUSERCODE() + "' " +
				"and b.学年学期编码='" + this.getWW_XNXQBM() + "' ";
			//分层课程
			sql += "union all " +
				"select distinct d.工号,d.姓名,a.课程代码,a.课程名称,isnull(e.分数,'') as 分数,isnull(e.意见,'') as 意见 " +
				"from V_规则管理_分层课程信息表 a " +
				"inner join V_规则管理_分层班信息表 b on b.分层课程编号=a.分层课程编号 " +
				"inner join V_规则管理_分层班学生信息表 c on c.分层班编号=b.分层班编号 " +
				"inner join V_教职工基本数据子类 d on b.授课教师编号 like '%@'+d.工号+'@%' " +
				"left join V_问卷调查_教师满意度评分表 e on e.课程代码=a.课程代码 and e.教师编号=d.工号 and e.创建人=c.学号 and e.状态='1' and e.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"where a.状态='1' and c.学号='" + this.getUSERCODE() + "' " +
				"and a.学年学期编码='" + this.getWW_XNXQBM() + "'";
			
			sql += ") as t order by t.课程代码";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i+=6){
					resultJson += "{\"teaCode\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
								"\"teaName\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
								"\"courseCode\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
								"\"courseName\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
								"\"score\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"," +
								"\"suggest\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"},";
				}
			}else{
				resultJson += ",";
			}
		}
		
		//辅导员评测表
		if("03".equalsIgnoreCase(this.getWW_WJLX())){
			Vector tempVec = null;
			String myd = "";
			String question = "";
			String score = "";
			
			//查询辅导员等信息
//			sql = "select a.工号,a.姓名,d.专业名称,case when (select distinct convert(nvarchar(10),创建时间,21) from V_问卷调查_结果信息表 " +
//				"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' and 创建人='" + MyTools.StrFiltr(this.getUSERCODE()) + "') is null then convert(nvarchar(10),getDate(),21) " +
//				"else (select distinct convert(nvarchar(10),创建时间,21) from V_问卷调查_结果信息表 where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
//				"and 创建人='" + MyTools.StrFiltr(this.getUSERCODE()) + "') end as 评测时间 " +
//				"from V_问卷调查_辅导员信息表 a " +
//				"left join V_学生基本数据子类 b on b.行政班代码=a.行政班代码 " +
//				"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
//				"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
//				"where b.学号='" + MyTools.StrFiltr(this.getUSERCODE()) + "' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
			sql = "select isnull(d.工号,''),isnull(d.姓名,''),c.专业名称," +
				"case when (select distinct convert(nvarchar(10),创建时间,21) from V_问卷调查_结果信息表 where 问卷编号=d.问卷编号 and 创建人=a.学号) is null " +
				"then convert(nvarchar(10),getDate(),21) " +
				"else (select distinct convert(nvarchar(10),创建时间,21) from V_问卷调查_结果信息表 where 问卷编号=d.问卷编号 and 创建人=a.学号) end as 评测时间 " +
				"from V_学生基本数据子类 a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
				"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
				"left join (select * from V_问卷调查_辅导员信息表 where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "') d on d.行政班代码=a.行政班代码 " +
				"where a.学号='" + MyTools.StrFiltr(this.getUSERCODE()) + "'";
			tempVec = db.GetContextVector(sql);
			
			resultJson += "\"teaCode\":\"" + MyTools.StrFiltr(tempVec.get(0)) + "\"," +
					"\"teaName\":\"" + MyTools.StrFiltr(tempVec.get(1)) + "\"," +
					"\"majorName\":\"" + MyTools.StrFiltr(tempVec.get(2)) + "\"," +
					"\"date\":\"" + MyTools.StrFiltr(tempVec.get(3)) + "\",";
			
			question = "\"allQuestion\":[";
			
			sql = "select a.题目编号,a.题目内容,a.题目要点,a.分值,isnull(c.答案,'') as 答案 from V_问卷调查_题目信息_表单题 a " +
				"inner join V_问卷调查_问卷题目关系表 b on b.题目编号=a.题目编号 " +
				"left join V_问卷调查_结果信息表 c on c.问卷编号=b.问卷编号 and c.题目编号=a.题目编号  and c.创建人='" + MyTools.StrFiltr(this.getUSERCODE()) + "'" +
				"where b.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"union all " +
				"select '','','','',答案 from V_问卷调查_结果信息表 where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' and 题目编号='' " +
				"and 创建人='" + MyTools.StrFiltr(this.getUSERCODE()) + "'";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i+=5){
					score = MyTools.StrFiltr(vec.get(i+3));
					
					if("".equalsIgnoreCase(score)){
						myd = "\"myd\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\",";
					}else{
						question += "{\"code\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
								"\"question\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
								"\"point\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
								"\"score\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
								"\"answer\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"},";
					}
				}
			}else{
				resultJson += ",";
			}
			
			if("".equalsIgnoreCase(myd)){
				myd = "\"myd\":\"\",";
			}
			resultJson += myd+question;
		}
		
		resultJson = resultJson.substring(0, resultJson.length()-1) + "]}";
		
		return resultJson;
	}
	

	/**
	 * 保存问卷结果
	 * @date:2015-12-01
	 * @param result 问卷结果
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveResult(String result) throws SQLException{
		String sql = "";
		boolean successFlag = true;
		String allResult[] = result.split("｜｜");
		String resultArray[] = new String[0];
		
		if(!"".equalsIgnoreCase(this.getUSERCODE())){
			if("01".equalsIgnoreCase(this.getWW_WJLX()) || "03".equalsIgnoreCase(this.getWW_WJLX())){
				for(int i=0; i<allResult.length; i++){
					resultArray = allResult[i].split(":");
					
					sql = "select count(*) from V_问卷调查_结果信息表 " +
						"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
						"and 题目编号='" + MyTools.fixSql(resultArray[0]) + "' " +
						"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "' " +
						"and 状态='1'";
					
					if(db.getResultFromDB(sql)){
						sql = "update V_问卷调查_结果信息表 set 答案='" + MyTools.fixSql(resultArray[1]) + "'," +
							"其他信息=(select 行政班代码 from V_学生基本数据子类 where 学号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
							"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
							"and 题目编号='" + MyTools.fixSql(resultArray[0]) + "' " +
							"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "' " +
							"and 状态='1'";
					}else{
						sql = "insert into V_问卷调查_结果信息表 (问卷编号,题目编号,答案,其他信息,创建人,创建时间,状态) values(" +
							"'" + MyTools.fixSql(this.getWW_WJBH()) + "'," +
							"'" + MyTools.fixSql(resultArray[0]) + "'," +
							"'" + MyTools.fixSql(resultArray[1]) + "'," +
							"(select 行政班代码 from V_学生基本数据子类 where 学号='" + MyTools.fixSql(this.getUSERCODE()) + "')," +
							"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
							"getDate()," +
							"'1')";
					}
					if(!db.executeInsertOrUpdate(sql)){
						successFlag = false;
						
						sql = "delete from V_问卷调查_结果信息表 " +
							"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
							"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
						db.executeInsertOrUpdate(sql);
						break;
					}
				}
			}else if("02".equalsIgnoreCase(this.getWW_WJLX())){
				for(int i=0; i<allResult.length; i++){
					resultArray = allResult[i].split("＼", -1);
					
					sql = "select count(*) from V_问卷调查_教师满意度评分表 " +
						"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
						"and 课程代码='" + MyTools.fixSql(resultArray[0]) + "' " +
						"and 教师编号='" + MyTools.fixSql(resultArray[1]) + "' " +
						"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "' " +
						"and 状态='1'";
					
					if(db.getResultFromDB(sql)){
						sql = "update V_问卷调查_教师满意度评分表 set " +
							"分数='" +  MyTools.fixSql(resultArray[3]) + "'," +
							"意见='" +  MyTools.fixSql(resultArray[4]) + "' " +
							"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
							"and 课程代码='" + MyTools.fixSql(resultArray[0]) + "' " +
							"and 教师编号='" + MyTools.fixSql(resultArray[1]) + "' " +
							"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "' " +
							"and 状态='1'";
					}else{
						sql = "insert into V_问卷调查_教师满意度评分表(问卷编号,课程代码,教师编号,教师姓名,分数,意见,创建人,创建时间,状态) values(" +
							"'" + MyTools.fixSql(this.getWW_WJBH()) + "'," +
							"'" + MyTools.fixSql(resultArray[0]) + "'," +
							"'" + MyTools.fixSql(resultArray[1]) + "'," +
							"'" + MyTools.fixSql(resultArray[2]) + "'," +
							"'" + MyTools.fixSql(resultArray[3]) + "'," +
							"'" + MyTools.fixSql(resultArray[4]) + "'," +
							"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
							"getDate()," +
							"'1')";
					}
					if(!db.executeInsertOrUpdate(sql)){
						successFlag = false;
						
						sql = "delete from V_问卷调查_教师满意度评分表 " +
							"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
							"and 创建人='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
						db.executeInsertOrUpdate(sql);
						break;
					}
				}
			}
			
			if(successFlag){
				this.setMSG("提交成功");
			}else{
				this.setMSG("提交失败");
			}
		}else{
			this.setMSG("登录信息失效，请重新登录后完成问卷！");
		}
	}
	
	/**
	 * 读取问卷调查统计结果
	 * @date:2015-12-03
	 * @author:yeq
	 * @throws SQLException
	 */
	public String loadStatistics() throws SQLException{
		Vector vec = null;
		String sql = "";
		String resultJson = "";
		String semeterName = "";
		String tableData = "";
		String chartData = "";
		Vector dataVec = new Vector();
		String admin = MyTools.getProp(request, "Base.admin");
		
		sql = "select a.标题,a.问卷类型,a.学年学期编码,b.学年学期名称 from V_问卷调查_问卷信息表 a " +
			"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
			"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			this.setWW_BT(MyTools.StrFiltr(vec.get(0)));
			this.setWW_WJLX(MyTools.StrFiltr(vec.get(1)));
			this.setWW_XNXQBM(MyTools.StrFiltr(vec.get(2)));
			semeterName = MyTools.StrFiltr(vec.get(3));
		}
		
		resultJson = "{\"title\":\"" + this.getWW_BT() + "\"," +
				"\"wjType\":\"" + this.getWW_WJLX() + "\"," +
				"\"semeter\":\"" + semeterName + "\"," +
				"\"data\":{";
		
		if("01".equalsIgnoreCase(this.getWW_WJLX())){
			String code = "";
			String title = "";
			String type = "";
			String option = "";
			int stuNum = 0;
			int totalStuNum = 0;
			String percent = "";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
			
			sql = "with " +
				"cte1 as(" +
				"select a.题目编号,a.选项编号,count(*) as 投票人数 from V_问卷调查_选项信息表 a " +
				"left join (select distinct 问卷编号,题目编号,答案,其他信息,创建人,状态 from V_问卷调查_结果信息表) b on b.题目编号=a.题目编号 and b.答案 like '%'+a.选项+'%' " +
				"where b.状态='1' and b.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' group by a.题目编号,a.选项编号" +
				")," +
				"cte2 as(" +
				"select 题目编号,count(distinct 创建人) as 总投票人数 from V_问卷调查_结果信息表 where 状态='1' " +
				"and 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' group by 题目编号) " +
				"select distinct c.编号,b.题目编号,b.题目内容,b.题目类型,a.选项编号,a.选项+'.'+a.选项内容 as 选项,isnull(d.投票人数,0),isnull(e.总投票人数,0) " +
				"from V_问卷调查_选项信息表 a " +
				"inner join V_问卷调查_题目信息_选择题 b on b.题目编号=a.题目编号 " +
				"inner join V_问卷调查_问卷题目关系表 c on c.题目编号=b.题目编号 " +
				"left join cte1 d on d.题目编号=a.题目编号 and d.选项编号=a.选项编号 " +
				"left join cte2 e on e.题目编号=a.题目编号 " +
				"where c.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' order by c.编号,a.选项编号";
			vec = db.GetContextVector(sql);
			
			tableData = "\"tableData\":[";
			chartData = "\"chartData\":[";
			
			for(int i=0; i<vec.size(); i+=8){
				option = MyTools.StrFiltr(vec.get(i+5));
				stuNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+6)));
				totalStuNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+7)));
				if(totalStuNum == 0){
					percent = "0.00%";
				}else{
					percent = String.valueOf(df.format((float)stuNum/(float)totalStuNum*100))+"%";
					if(".00%".equalsIgnoreCase(percent)){
						percent = "0"+percent;
					}
				}
				
				//判断是否同一道题目
				if(code.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
					if(i > 0){
						tableData += ",{\"option\":\"" + option + "\"," +
								"\"stuNum\":\"" + stuNum + "\"," +
								"\"percent\":\"" + percent + "\"}";
						dataVec.add(option);
						dataVec.add(stuNum);
						dataVec.add(percent);
					}
				}else{
					if(i > 0){
						tableData += "]},";
						chartData += parseEchartsData(title, totalStuNum, type, dataVec)+",";
						dataVec.clear();
					}
					
					code = MyTools.StrFiltr(vec.get(i+1));
					title = MyTools.StrFiltr(vec.get(i+2));
					type = MyTools.StrFiltr(vec.get(i+3));
					
					tableData += "{\"title\":\"" + title + "\"," +
								"\"type\":\"" + type + "\"," +
								"\"totalStuNum\":\"" + totalStuNum + "\"," +
								"\"options\":[{\"option\":\"" + option + "\"," +
										"\"stuNum\":\"" + stuNum + "\"," +
										"\"percent\":\"" + percent + "\"}";
					dataVec.add(option);
					dataVec.add(stuNum);
					dataVec.add(percent);
				}
			}
			chartData += parseEchartsData(title, totalStuNum, type, dataVec)+"";
			tableData += "]}";
			resultJson += tableData+"],"+chartData+"]}}";
		}
		
		if("02".equalsIgnoreCase(this.getWW_WJLX())){
			sql = "select ROW_NUMBER() over (order by 系名称,班级,教师编号) as id,课程名称,教师姓名,系名称,班级,平均分 from (" +
				"select c.课程名称,a.教师姓名,g.系名称," +
				"e.行政班名称 as 班级,Convert(decimal(18,1),avg(cast(a.分数 as float))) as 平均分,a.教师编号 " +
				"from V_问卷调查_教师满意度评分表 a " +
				"inner join V_学生基本数据子类 b on b.学号=a.创建人 " +
				"inner join V_课程数据子类 c on c.课程号=a.课程代码 " +
				"inner join V_学校班级数据子类 e on e.行政班代码=b.行政班代码 " +
				"inner join V_基础信息_系专业关系表 f on f.专业代码=e.专业代码 " +
				"inner join V_基础信息_系基础信息表 g on g.系代码=f.系代码 " +
				"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"group by a.课程代码,c.课程名称,b.行政班代码,e.行政班名称,a.教师姓名,g.系名称,a.教师编号 ";
			//选修课程
			sql += "union all " +
				"select b.课程名称,a.教师姓名,'' as 系名称,c.选修班名称,Convert(decimal(18,1),avg(cast(a.分数 as float))) as 平均分,a.教师编号 " +
				"from V_问卷调查_教师满意度评分表 a " +
				"inner join V_规则管理_选修课授课计划主表 b on b.课程代码=a.课程代码 " +
				"inner join V_规则管理_选修课授课计划明细表 c on c.授课计划主表编号=b.授课计划主表编号 " +
				"inner join V_规则管理_学生选修课关系表 e on e.学号=a.创建人 and e.授课计划明细编号=c.授课计划明细编号 " +
				"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"group by b.课程代码,b.课程名称,c.选修班名称,a.教师姓名,a.教师编号 ";
			//分层课程
			sql += "union all " +
				"select b.课程名称,a.教师姓名,f.系名称,c.分层班名称,Convert(decimal(18,1),avg(cast(a.分数 as float))) as 平均分,a.教师编号 " +
				"from V_问卷调查_教师满意度评分表 a " +
				"inner join V_规则管理_分层课程信息表 b on b.课程代码=a.课程代码 " +
				"inner join V_规则管理_分层班信息表 c on c.分层课程编号=b.分层课程编号 " +
				"inner join V_规则管理_分层班学生信息表 e on e.学号=a.创建人 and e.分层班编号=c.分层班编号 " +
				"left join V_基础信息_系基础信息表 f on f.系代码=b.系代码 " +
				"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"group by b.课程代码,b.课程名称,c.分层班名称,a.教师姓名,f.系名称,a.教师编号";
			
			sql += ") as t ";
			
			if(Auth.indexOf(admin) < 0){
				sql += "where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			
			sql += "order by 系名称,班级,教师编号";
			vec = db.GetContextVector(sql);
			
			resultJson += "\"tableData\":[";
			
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i+=6){
					resultJson += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
							"\"courseName\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
							"\"teaName\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
							"\"majorName\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
							"\"className\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"," +
							"\"score\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"},";
				}
			}else{
				resultJson += ",";
			}
			
			resultJson = resultJson.substring(0, resultJson.length()-1)+"]}}";
		}
		
		if("03".equalsIgnoreCase(this.getWW_WJLX())){
			sql = "select ROW_NUMBER() over (order by 姓名,行政班名称) as id,姓名,行政班名称,cast(avg(合计) as decimal(18,1)) as 平均分 " +
				"from (select isnull(c.姓名,'') as 姓名,d.行政班名称,a.创建人 as 学号,sum(cast(a.答案 as float)) as 合计 from V_问卷调查_结果信息表 a " +
				"left join V_问卷调查_问卷信息表 b on b.问卷编号=a.问卷编号 " +
				"left join V_问卷调查_辅导员信息表 c on c.行政班代码=a.其他信息 and c.问卷编号=a.问卷编号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=a.其他信息 " +
				"where a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
			if(Auth.indexOf(admin) < 0){
				sql += " and (c.工号='" + MyTools.fixSql(this.getUSERCODE()) + "' or d.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += " and a.题目编号<>'' and a.创建人<>'' and d.行政班代码 is not null group by c.姓名,d.行政班名称,a.创建人) as t group by 姓名,行政班名称";
			vec = db.GetContextVector(sql);
			
//			sql = "select ROW_NUMBER() over (order by b.行政班名称) as id,b.行政班名称," +
//				"cast(avg(a.合计) as decimal(18,1)) as 平均分 from (" +
//				"select 创建人 as 学号,其他信息 as 行政班代码,sum(cast(答案 as float)) as 合计 from V_问卷调查_结果信息表 " +
//				"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' and 题目编号<>'' and 创建人<>'' group by 创建人,其他信息) as a " +
//				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 group by b.行政班名称";
//			vec = db.GetContextVector(sql);
			
			resultJson += "\"tableData\":[";
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i+=4){
					resultJson += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
							"\"name\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
							"\"className\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
							"\"score\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"},";
				}
			}else{
				resultJson += ",";
			}
			resultJson = resultJson.substring(0, resultJson.length()-1)+"]}}";
		}
		
		return resultJson;
	}
	
	/**
	 * 获取图标数据
	 * @date:2015-12-03
	 * @author:yeq
	 * @param title 标题
	 * @param type 题目类型
	 * @param data 题目数据
	 * @throws SQLException
	 */
	public String parseEchartsData(String title, int stuNum, String type, Vector dataVec){
		String legendArray[] = new String[dataVec.size()/3];
		int index = 0;
		GsonOption option = new GsonOption();//创建Option
		
		//判断标题长度,如果过长添加换行符
		String tempTitle = title+"（总投票数："+stuNum+"人）";
		String resultTitle = "";
		int tempNum = 0;
		if(tempTitle.length() > 35){
			tempNum = tempTitle.length()/35;
			if(tempTitle.length()%35 > 0){
				tempNum += 1;
			}
			
			for(int i=0; i<tempNum; i++){
				if(i < tempNum-1){
					resultTitle += tempTitle.substring(35*i, 35*(i+1))+"\n";
				}else{
					resultTitle += tempTitle.substring(35*i);
				}
			}
		}else{
			resultTitle = tempTitle;
		}
		
		option.title().text(resultTitle).x("center").textStyle();
		option.legend().orient(Orient.vertical);
		option.setAnimation(false);
		
		//单选饼图
		if("01".equalsIgnoreCase(type)){
			option.tooltip().show(true).formatter("{b}<br/>投票人数 : {c}人({d}%)");
			option.legend().x(580).y(85);
			//option.toolbox().show(true).feature(Tool.saveAsImage);
			
			//饼图数据
			Pie pie = new Pie(title);
			
			//循环数据
			for(int i=0; i<dataVec.size(); i+=3){
				//饼图数据
				pie.data(new PieData(MyTools.StrFiltr(dataVec.get(i)), MyTools.StringToInt(MyTools.StrFiltr(dataVec.get(i+1)))));
				legendArray[index] = MyTools.StrFiltr(dataVec.get(i));
				index++;
			}
			option.legend(legendArray);
			
			//饼图的圆心和半径
			pie.center("50%", "60%").radius(110);
			option.series(pie);
		}
		
		//多选柱状图
		if("02".equalsIgnoreCase(type)){
			option.tooltip().show(true).formatter("{a} 投票人数 : {c}人");
			option.legend().x(700).y(60);
			//横轴为值轴
			option.xAxis(new ValueAxis().boundaryGap(0d, 0.01).max(stuNum));
			//创建类目轴
			CategoryAxis category = new CategoryAxis();  
			//设置类目
			category.data("");
			
			//柱状数据
			Bar bar;
			Bar barArray[] = new Bar[dataVec.size()/3];
			//循环数据
			for(int i=dataVec.size()-3; i>=0; i-=3){
				bar = new Bar(MyTools.StrFiltr(dataVec.get(i)).substring(0, 1));
				//类目对应的柱状图
				bar.data(MyTools.StringToInt(MyTools.StrFiltr(dataVec.get(i+1))));
				bar.itemStyle().normal().label().show(true).position("right").textStyle().color("#000000");
				bar.itemStyle().normal().label().formatter(MyTools.StrFiltr(dataVec.get(i)).substring(0, 1)+" "+MyTools.StrFiltr(dataVec.get(i+2)));
				barArray[index] = bar;
				legendArray[index] = MyTools.StrFiltr(dataVec.get(index*3)).substring(0, 1);
				index++;
			}
			option.legend(legendArray);
			
			//设置类目轴
			option.yAxis(category);
			//设置数据
			option.series(barArray);
			
			//图表距离左侧距离设置180，关于grid可以看ECharts的官方文档  
			//option.grid().x(180);
		}
		
		return option.toString();
	}  
	
	/**
	 * 查询导出数据
	 * @date:2015-12-07
	 * @author:yeq
	 * @param savePath 
	 * @param imageData 图片数据
	 * @throws SQLException
	 * @throws IOException 
	 * @throws WriteException 
	 * @return String 生成的文件路径
	 */
	public String queExportData(String savePath, String imageData) throws SQLException{
		Vector vec = null;
		Vector exportData = new Vector();
		Vector optionVec = new Vector();
		String sql = "";
		String semeterName = "";
		String[] title = new String[0];
		String picData[] = imageData.split(",");
		String filePath = "";
		String admin = MyTools.getProp(request, "Base.admin");

		sql = "select a.标题,a.问卷类型,a.学年学期编码,b.学年学期名称 from V_问卷调查_问卷信息表 a " +
			"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
			"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			this.setWW_BT(MyTools.StrFiltr(vec.get(0)));
			this.setWW_WJLX(MyTools.StrFiltr(vec.get(1)));
			this.setWW_XNXQBM(MyTools.StrFiltr(vec.get(2)));
			semeterName = MyTools.StrFiltr(vec.get(3));
		}
		
		//判断问卷调查类型
		if("01".equalsIgnoreCase(this.getWW_WJLX())){
			title = new String[]{"选项","投票数","百分比"};
			
			sql = "with " +
				"cte1 as(" +
				"select a.题目编号,a.选项编号,count(*) as 投票人数 from V_问卷调查_选项信息表 a " +
				"left join (select distinct 问卷编号,题目编号,答案,其他信息,创建人,状态 from V_问卷调查_结果信息表) b on b.题目编号=a.题目编号 and b.答案 like '%'+a.选项+'%' " +
				"where b.状态='1' and b.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' group by a.题目编号,a.选项编号" +
				")," +
				"cte2 as(" +
				"select 题目编号,count(distinct 创建人) as 总投票人数 from V_问卷调查_结果信息表 where 状态='1' " +
				"and 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' group by 题目编号) " +
				"select distinct c.编号,b.题目编号,b.题目内容,b.题目类型,a.选项编号,a.选项+'.'+a.选项内容 as 选项,isnull(d.投票人数,0),isnull(e.总投票人数,0) " +
				"from V_问卷调查_选项信息表 a " +
				"inner join V_问卷调查_题目信息_选择题 b on b.题目编号=a.题目编号 " +
				"inner join V_问卷调查_问卷题目关系表 c on c.题目编号=b.题目编号 " +
				"left join cte1 d on d.题目编号=a.题目编号 and d.选项编号=a.选项编号 " +
				"left join cte2 e on e.题目编号=a.题目编号 " +
				"where c.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' order by c.编号,a.选项编号";
			vec = db.GetContextVector(sql);
			
			String code = "";
			String question = "";
			String option = "";
			String type = "";
			int stuNum = 0;
			int totalStuNum = 0;
			String percent = "";
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			int questNum = 1;
			
			for(int i=0; i<vec.size(); i+=8){
				option = MyTools.StrFiltr(vec.get(i+5));
				stuNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+6)));
				totalStuNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+7)));
				if(totalStuNum == 0){
					percent = "0.00%";
				}else{
					percent = String.valueOf(df.format((float)stuNum/(float)totalStuNum*100))+"%";
					
					if(".00%".equalsIgnoreCase(percent)){
						percent = "0"+percent;
					}
				}
				
				//判断是否同一道题目
				if(code.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
					if(i > 0){
						optionVec.add(option);
						optionVec.add(stuNum);
						optionVec.add(percent);
					}
				}else{
					if(i > 0){
						exportData.add(optionVec);
						optionVec = new Vector();
					}
					
					code = MyTools.StrFiltr(vec.get(i+1));
					question = MyTools.StrFiltr(vec.get(i+2));
					type = MyTools.StrFiltr(vec.get(i+3));
					
					exportData.add(questNum+"、"+question);
					questNum++;
					exportData.add(type);
					exportData.add(totalStuNum);
					optionVec.add(option);
					optionVec.add(stuNum);
					optionVec.add(percent);
				}
			}
			exportData.add(optionVec);
		}
		
		if("02".equalsIgnoreCase(this.getWW_WJLX())){
			title = new String[]{"序号","课程","教师","系别","班级","平均分"};
			
			sql = "select ROW_NUMBER() over (order by 系名称,班级,教师编号) as id,课程名称,教师姓名,系名称,班级,平均分 from (" +
				"select c.课程名称,a.教师姓名,g.系名称," +
				"e.行政班名称 as 班级,Convert(decimal(18,1),avg(cast(a.分数 as float))) as 平均分,a.教师编号 " +
				"from V_问卷调查_教师满意度评分表 a " +
				"inner join V_学生基本数据子类 b on b.学号=a.创建人 " +
				"inner join V_课程数据子类 c on c.课程号=a.课程代码 " +
				"inner join V_学校班级数据子类 e on e.行政班代码=b.行政班代码 " +
				"inner join V_基础信息_系专业关系表 f on f.专业代码=e.专业代码 " +
				"inner join V_基础信息_系基础信息表 g on g.系代码=f.系代码 " +
				"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"group by a.课程代码,c.课程名称,b.行政班代码,e.行政班名称,a.教师姓名,g.系名称,a.教师编号 ";
			//选修课程
			sql += "union all " +
				"select b.课程名称,a.教师姓名,'' as 系名称,c.选修班名称,Convert(decimal(18,1),avg(cast(a.分数 as float))) as 平均分,a.教师编号 " +
				"from V_问卷调查_教师满意度评分表 a " +
				"inner join V_规则管理_选修课授课计划主表 b on b.课程代码=a.课程代码 " +
				"inner join V_规则管理_选修课授课计划明细表 c on c.授课计划主表编号=b.授课计划主表编号 " +
				"inner join V_规则管理_学生选修课关系表 e on e.学号=a.创建人 and e.授课计划明细编号=c.授课计划明细编号 " +
				"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"group by b.课程代码,b.课程名称,c.选修班名称,a.教师姓名,a.教师编号 ";
			//分层课程
			sql += "union all " +
				"select b.课程名称,a.教师姓名,f.系名称,c.分层班名称,Convert(decimal(18,1),avg(cast(a.分数 as float))) as 平均分,a.教师编号 " +
				"from V_问卷调查_教师满意度评分表 a " +
				"inner join V_规则管理_分层课程信息表 b on b.课程代码=a.课程代码 " +
				"inner join V_规则管理_分层班信息表 c on c.分层课程编号=b.分层课程编号 " +
				"inner join V_规则管理_分层班学生信息表 e on e.学号=a.创建人 and e.分层班编号=c.分层班编号 " +
				"left join V_基础信息_系基础信息表 f on f.系代码=b.系代码 " +
				"where a.状态='1' and a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"group by b.课程代码,b.课程名称,c.分层班名称,a.教师姓名,f.系名称,a.教师编号";	
				
			sql += ") as t ";
			
			if(Auth.indexOf(admin) < 0){
				sql += "where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			
			sql += "order by 系名称,班级,教师编号";
			exportData = db.GetContextVector(sql);
		}
		
		if("03".equalsIgnoreCase(this.getWW_WJLX())){
			title = new String[]{"序号","辅导员姓名","班级","平均分"};
			
			sql = "select ROW_NUMBER() over (order by 姓名,行政班名称) as id,姓名,行政班名称,cast(avg(合计) as decimal(18,1)) as 平均分 " +
				"from (select isnull(c.姓名,'') as 姓名,d.行政班名称,a.创建人 as 学号,sum(cast(a.答案 as float)) as 合计 from V_问卷调查_结果信息表 a " +
				"left join V_问卷调查_问卷信息表 b on b.问卷编号=a.问卷编号 " +
				"left join V_问卷调查_辅导员信息表 c on c.行政班代码=a.其他信息 and c.问卷编号=a.问卷编号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=a.其他信息 " +
				"where a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
			if(Auth.indexOf(admin) < 0){
				sql += " and c.工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			sql += " and a.题目编号<>'' and a.创建人<>'' group by c.姓名,d.行政班名称,a.创建人) as t group by 姓名,行政班名称";
			exportData = db.GetContextVector(sql);
			
//			sql = "select ROW_NUMBER() over (order by b.行政班名称) as id,'' as 辅导员姓名,b.行政班名称," +
//				"cast(avg(a.合计) as decimal(18,1)) as 平均分 from (" +
//				"select 创建人 as 学号,其他信息 as 行政班代码,sum(cast(答案 as float)) as 合计 from V_问卷调查_结果信息表 " +
//				"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' and 题目编号<>'' and 创建人<>'' group by 创建人,其他信息) as a " +
//				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 group by b.行政班名称";
//			exportData = db.GetContextVector(sql);
		}
		
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
		
		filePath = this.exportExcel(savePath, this.getWW_BT()+"_"+year+(month+1)+date+".xls", this.getWW_BT()+"统计信息", title, exportData, picData, this.getWW_WJLX());
		return filePath;
	}
	
	/**
	 * 导出统计结果excel
	 * @date:2015-12-07
	 * @author:yeq
	 * @param savePath 保存路径
	 * @param filename 文件名
	 * @param sheetName 工作簿名称
	 * @param title 标题数组
	 * @param exportData 导出数据
	 * @param imageData 图片数据
	 * @param wjType
	 * @throws IOException 
	 * @throws WriteException
	 * @return String 保存的文件路径
	 */
	public String exportExcel(String savePath, String filename, String sheetName, String[] title, Vector exportData, String[] imageData, String wjType){
		String filePath = "";
		
		//创建文件
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
	
		OutputStream os;
		try {
			os = new FileOutputStream(savePath + "/" + filename);
			//输出流
			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			WritableSheet wsheet = wbook.createSheet(sheetName, 0); // 工作表名称
			Label tableTitle;
			Label content;
			
			if("01".equalsIgnoreCase(wjType)){
				wsheet.setColumnView(0, 50);// 设置第一列的宽度
				wsheet.setColumnView(1, 20);// 设置第二列的宽度
				wsheet.setColumnView(2, 15);// 设置第三列的宽度
				wsheet.setColumnView(5, 4);// 设置第三列的宽度
				
				//去掉整个sheet中的网格线
				wsheet.getSettings().setShowGridLines(false);
				
				//表格标题样式
				WritableFont font = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				WritableCellFormat titleFormat = new WritableCellFormat(font);
				titleFormat.setBackground(getNearestColour("#CCFFFF"));
				titleFormat.setAlignment(jxl.format.Alignment.CENTRE);//居中
				titleFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				titleFormat.setWrap(true);// 自动换行
				titleFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);// 单元格边框实线
				
				//选项内容样式
				WritableCellFormat optionFormat = new WritableCellFormat(font);
				optionFormat.setBackground(getNearestColour("#CCFFFF"));
				optionFormat.setAlignment(jxl.format.Alignment.LEFT);//左对齐
				optionFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				optionFormat.setWrap(true);// 自动换行
				optionFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);// 单元格边框实线
				
				//表格内容样式
				WritableCellFormat contentFormat = new WritableCellFormat(font);
				contentFormat.setAlignment(jxl.format.Alignment.CENTRE);//居中
				contentFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentFormat.setWrap(true);// 自动换行
				contentFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);// 单元格边框实线
				
				//题目样式
				WritableCellFormat quesFormat = new WritableCellFormat(font);
				quesFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				quesFormat.setWrap(true);// 自动换行
				
				//提示字体样式
				//表格标题样式
				WritableFont tipsfont = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.RED);
				WritableCellFormat tipsFormat = new WritableCellFormat(tipsfont);
				String tipsStr = "提示：单选题显示饼图；多选题显示柱状图；";
				
				String question = "";
				int quesRowNum = 0;
				int rowNum = 0;
				int picIndex = 0;
				Vector optionVec = null;
				
				for(int i=0; i<exportData.size(); i+=4){
					question = MyTools.StrFiltr(exportData.get(i)) + "（总投票数："+MyTools.StrFiltr(exportData.get(i+2))+"人）";
					wsheet.mergeCells(0, rowNum, 2, rowNum);//合并单元格
					
					quesRowNum = question.length()/35;
					if(question.length()%35 > 0){
						quesRowNum++;
					}
					wsheet.setRowView(rowNum, 500*quesRowNum);//设置行高
					
					Label curQuestion = new Label(0, rowNum, question, quesFormat);
					wsheet.addCell(curQuestion);
					rowNum++;
					
					optionVec = (Vector)exportData.get(i+3);
					
					//标题行
					wsheet.setRowView(rowNum, 300);
					for (int j=0; j<title.length; j++) {
						tableTitle = new Label(j, rowNum, title[j], titleFormat);
						wsheet.addCell(tableTitle);
					}
					rowNum++;
					
					for(int j=0; j<optionVec.size(); j+=3){
						for(int k=0; k<title.length; k++){
							//设置行高
							quesRowNum = MyTools.StrFiltr(optionVec.get(j+k)).length()/20;
							if(question.length()%20 > 0){
								quesRowNum++;
							}
							wsheet.setRowView(rowNum, 300*quesRowNum);
							
							if(k == 0){
								content = new Label(k, rowNum, MyTools.StrFiltr(optionVec.get(j+k)), optionFormat);
							}else{
								content = new Label(k, rowNum, MyTools.StrFiltr(optionVec.get(j+k)), contentFormat);
							}
							wsheet.addCell(content);
						}
						rowNum++;
					}
					rowNum++;
					
					if(imageData.length>0 && !"".equalsIgnoreCase(imageData[0])){
						//插入提示行
						wsheet.mergeCells(0, rowNum, 5, rowNum);//合并单元格
						content = new Label(0, rowNum, tipsStr, tipsFormat);
						wsheet.addCell(content);
						rowNum++;
						
						base64TOpic(decodeBase64(imageData[picIndex]), "pic_"+picIndex, savePath+"/tempPic");//生成图片
						
						// 插入 PNG 图片至 Excel  
						String imgPath = savePath+"/tempPic/" + "pic_"+picIndex + ".png";  
						File imgFile = new File(imgPath);
						//col row是图片的起始行起始列  width height是定义图片跨越的行数与列数  
						WritableImage image = new WritableImage(0, rowNum, 6, 1,imgFile);
						wsheet.addImage(image);
						wsheet.setRowView(rowNum, 6000);//设置行高
						
						rowNum++;
						picIndex++;
					}
					
					rowNum+=3;
				}
			}
			
			if("02".equalsIgnoreCase(wjType) || "03".equalsIgnoreCase(wjType)){
				//标题样式
				WritableFont titleFont = new WritableFont(
						WritableFont.createFont("宋体"), 15, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
				titleFormat.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				
				wsheet.mergeCells(0, 0, title.length-1, 0);     
				Label excelTitle = new Label(0, 0, sheetName, titleFormat);
				wsheet.addCell(excelTitle);
				
				if("02".equalsIgnoreCase(wjType)){
					wsheet.setColumnView(0, 10);// 设置第一列的宽度
					wsheet.setColumnView(1, 35);// 设置第二列的宽度
					wsheet.setColumnView(2, 15);// 设置第三列的宽度
					wsheet.setColumnView(3, 30);// 设置第四列的宽度
					wsheet.setColumnView(4, 40);// 设置第五列的宽度
					wsheet.setColumnView(5, 15);// 设置第六列的宽度
				}else{
					wsheet.setColumnView(0, 8);// 设置第一列的宽度
					wsheet.setColumnView(1, 15);// 设置第二列的宽度
					wsheet.setColumnView(2, 52);// 设置第三列的宽度
					wsheet.setColumnView(3, 12);// 设置第四列的宽度
				}
				
				//表格内容样式
				WritableFont contentFont = new WritableFont(
						WritableFont.createFont("宋体"), 14, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				WritableCellFormat contentFormat = new WritableCellFormat(contentFont);
				contentFormat.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentFormat.setWrap(true);// 自动换行
				contentFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);// 单元格边框实线
				
				//标题行
				for (int i=0; i<title.length; i++) {
					tableTitle = new Label(i, 1, title[i], contentFormat);
					wsheet.addCell(tableTitle);
				}
				
				//c:用于循环时Excel的行号
				//外层for用于循环行,内曾for用于循环列
				for (int i=0, k=2; i<exportData.size(); i+=title.length, k++) {
					for (int j=0; j<title.length; j++) {
						content = new Label(j, k, exportData.get(i+j) + "", contentFormat);
						wsheet.addCell(content);
					}
				}
			}
			
			//写入文件
			wbook.write();
			wbook.close();
			os.close();
			this.setMSG("文件生成成功");
			filePath = savePath + "/" + filename;
		} catch (FileNotFoundException e) {
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e) {
			this.setMSG("文件生成失败");
		} catch (IOException e) {
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/** 
	 * 将十六进制颜色转换为jxl可用的颜色 
	 */
	public static Colour getNearestColour(String strColor) {
		Color cl = Color.decode(strColor);
		Colour color = null;
		Colour[] colors = Colour.getAllColours();
		if ((colors != null) && (colors.length > 0)) {
			Colour crtColor = null;
			int[] rgb = null;
			int diff = 0;
			int minDiff = 999;
			for (int i = 0; i < colors.length; i++) {
				crtColor = colors[i];
				rgb = new int[3];
				rgb[0] = crtColor.getDefaultRGB().getRed();
				rgb[1] = crtColor.getDefaultRGB().getGreen();
				rgb[2] = crtColor.getDefaultRGB().getBlue();
				diff = Math.abs(rgb[0] - cl.getRed())
				+ Math.abs(rgb[1] - cl.getGreen())
				+ Math.abs(rgb[2] - cl.getBlue());
				if (diff < minDiff) {
					minDiff = diff;
					color = crtColor;
				}
			}
		}
		if (color == null)
		color = Colour.BLACK;
		return color;
	}
	
	/**
	 * base64解码生成图片
	 * @param imgsUrl
	 * @param fileName
	 * @param savePath
	 */
	public void base64TOpic(String imgsUrl, String fileName, String savePath) {
		//创建文件
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		//对字节数组字符串进行Base64解码并生成图片
		if (imgsUrl == null) //图像数据为空
			return ;
		
		//BASE64Decoder decoder = new BASE64Decoder();
		try{
			//Base64解码
			byte[] buffer = new BASE64Decoder().decodeBuffer(imgsUrl);
			//生成图片
			OutputStream out = new FileOutputStream(new File(savePath+"/"+fileName+".png"));    
			out.write(buffer);
			out.flush();
			out.close();
			return;
		}catch (Exception e){
			return;
		}
	}
	
	/**
	 * base64特殊字符解码
	 * @return
	 */
	public String decodeBase64(String str){
		String codeArray[]  = {"/", "+", "="};
		String regArray[] = {"_a", "_b", "_c"};
		
		for(int i=0; i<codeArray.length; i++){
			str = str.replaceAll(regArray[i], codeArray[i]);
		}
		return str;
	}
	
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getAuth() {
		return Auth;
	}

	public void setAuth(String auth) {
		Auth = auth;
	}

	public String getWW_WJBH() {
		return WW_WJBH;
	}

	public void setWW_WJBH(String wW_WJBH) {
		WW_WJBH = wW_WJBH;
	}

	public String getWW_WJLX() {
		return WW_WJLX;
	}

	public void setWW_WJLX(String wW_WJLX) {
		WW_WJLX = wW_WJLX;
	}

	public String getWW_XNXQBM() {
		return WW_XNXQBM;
	}

	public void setWW_XNXQBM(String wW_XNXQBM) {
		WW_XNXQBM = wW_XNXQBM;
	}

	public String getWW_BT() {
		return WW_BT;
	}

	public void setWW_BT(String wW_BT) {
		WW_BT = wW_BT;
	}

	public String getWW_KSSJ() {
		return WW_KSSJ;
	}

	public void setWW_KSSJ(String wW_KSSJ) {
		WW_KSSJ = wW_KSSJ;
	}

	public String getWW_JSSJ() {
		return WW_JSSJ;
	}

	public void setWW_JSSJ(String wW_JSSJ) {
		WW_JSSJ = wW_JSSJ;
	}

	public String getWT_TMBH() {
		return WT_TMBH;
	}

	public void setWT_TMBH(String wT_TMBH) {
		WT_TMBH = wT_TMBH;
	}

	public String getWT_TMLX() {
		return WT_TMLX;
	}

	public void setWT_TMLX(String wT_TMLX) {
		WT_TMLX = wT_TMLX;
	}

	public String getWT_TMNR() {
		return WT_TMNR;
	}

	public void setWT_TMNR(String wT_TMNR) {
		WT_TMNR = wT_TMNR;
	}

	public String getCJR() {
		return CJR;
	}

	public void setCJR(String cJR) {
		CJR = cJR;
	}

	public String getCJSJ() {
		return CJSJ;
	}

	public void setCJSJ(String cJSJ) {
		CJSJ = cJSJ;
	}

	public String getZT() {
		return ZT;
	}

	public void setZT(String zT) {
		ZT = zT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}