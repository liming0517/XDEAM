package com.pantech.devolop.registerScoreManage;
/*
@date 2016.03.25
@author yeq
模块：M7.2成绩查询
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Calendar;
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
import jxl.write.biff.RowsExceededException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;
import com.pantech.devolop.registerScoreSet.DfryszBean;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

import org.apache.tools.ant.Project;  
import org.apache.tools.ant.taskdefs.Zip;  
import org.apache.tools.ant.types.FileSet; 

public class CjcxBean {
	private String USERCODE; //用户编号
	private String Auth; //用户权限
	private String CODE; //编号
	private String STUCODE; //学号
	private String STUNAME; //姓名
	private String NJDM; //年级代码
	private String ZYMC; //专业名称
	private String BJMC; //班级名称
	private String XNXQ; //学年学期
	private String KCMC; //课程名称
	private String JSXM; //教师姓名
	private String CJFW; //成绩范围
	private String CJLX; //查询成绩范围成绩类型
	private String PSCJ; //平时成绩
	private String QZCJ; //期中成绩
	private String SXCJ; //实训成绩
	private String QMCJ; //期末成绩
	private String ZPCJ; //总评成绩
	private String CXCJ1; //重修成绩1
	private String CXCJ2; //重修成绩2
	private String BKCJ; //补考成绩
	private String DBKCJ; //大补考成绩
	private String DYCJ; //打印成绩
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public CjcxBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2016-03-25
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = ""; //用户编号
		Auth = ""; //用户权限
		CODE = ""; //编号
		STUCODE = ""; //学号
		STUNAME = ""; //姓名
		NJDM = ""; //年级代码
		ZYMC = ""; //专业名称
		BJMC = ""; //班级名称
		XNXQ = ""; //学年学期
		KCMC = ""; //课程名称
		JSXM = ""; //教师姓名
		CJFW = ""; //成绩范围
		CJLX = ""; //查询成绩范围成绩类型
		PSCJ = ""; //平时成绩
		QZCJ = ""; //期中成绩
		SXCJ = ""; //实训成绩
		QMCJ = ""; //期末成绩
		ZPCJ = ""; //总评成绩
		CXCJ1 = ""; //重修成绩1
		CXCJ2 = ""; //重修成绩2
		BKCJ = ""; //补考成绩
		DBKCJ = ""; //大补考成绩
		DYCJ = ""; //打印成绩
	}
	
	/**
	 * 查询查分设置
	 * @date:2016-07-14
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadQueryConfig() throws SQLException{
		Vector vec = new Vector();
		String sql = "select top 1 开放学年学期,开放成绩类型,convert(nvarchar(10),开始时间,21),convert(nvarchar(10),结束时间,21) from V_成绩管理_查分设置信息表";
		vec = db.GetContextVector(sql);
		
		if(vec.size() == 0){
			vec.add("all");
			vec.add("all");
			vec.add("");
			vec.add("");
		}
		
		return vec;
	}
	
	/**
	 * 读取年级下拉框
	 * @date:2016-03-25
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNJDMCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,'1' as orderNum " +
				"from V_学校年级数据子类 where 年级状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2016-03-25
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSemCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,1 as orderNum " +
				"from V_规则管理_学年学期表 " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取当前学年下拉框
	 * @date:2016-07-21
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadCurXn() throws SQLException{
		Vector vec = null;
		String curXnxq = "";
		String sql = "select top 1 left(学年学期编码,5) from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		return curXnxq;
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2016-07-21
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年 as comboName,学年 as comboValue from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取留级名单列表数据
	 * @date:2016-07-21
	 * @author:yeq
	 * @param type 类型(now当前数据/his历史数据)
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queLjmdList(String type, int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "";
		
//		sql = "select 学号,姓名,行政班名称,不及格数 from (select 学号,姓名,行政班名称,留级标准,count(*) as 不及格数 from " +
//			"(select a.学号,a.姓名,d.行政班名称,case when left(b.年级代码,2)='" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "' then 8 else 5 end as 留级标准," +
//			"case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end as 成绩 " +
//			"from V_成绩管理_学生成绩信息表 a " +
//			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//			"left join V_学生基本数据子类 c on c.学号=a.学号 " +
//			"left join V_学校班级_数据子类 d on d.行政班代码=c.行政班代码 " +
//			"where left(b.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' " +
//			"and c.学生状态 in ('01','05','07','08') and a.成绩状态<>'0' " +
//			"and left(d.年级代码,2) in ('" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "','" + MyTools.fixSql(MyTools.StrFiltr(gradeNum)) + "')) as t " +
//			"where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
//			"group by 学号,姓名,行政班名称,留级标准) as t where 不及格数>=留级标准 order by 行政班名称,学号";
		if("now".equalsIgnoreCase(type)){
			int gradeNum = MyTools.StringToInt(this.getXNXQ().substring(2));
			
			sql = "select 学号,姓名,系别,行政班名称,不及格科目数 from (select 学号,姓名,行政班名称,系名称 as 系别,留级标准,count(*) as 不及格科目数 from (" +
				"select a.学号,a.姓名,f.系名称,d.行政班名称,case when left(d.年级代码,2)='" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "' then 8 else 5 end as 留级标准,case when 大补考<>'' then 大补考 " +
				"when 补考<>'' then 补考 when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end as 成绩 from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_学生基本数据子类 c on c.学号=a.学号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
				"left join V_基础信息_系专业关系表 e on e.专业代码=d.专业代码 " +
				"left join V_基础信息_系基础信息表 f on f.系代码=e.系代码 " +
				"where cast(left(b.学年学期编码,4) as int)<=" + MyTools.fixSql(this.getXNXQ()) + " and b.是否参与留级='1' and c.学生状态 in ('01','05') and a.成绩状态='1' and " +
				"left(d.年级代码,2) in ('" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "','" + MyTools.fixSql(MyTools.StrFiltr(gradeNum)) + "')) as t " +
				"where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
				"group by 学号,姓名,系名称,行政班名称,留级标准) as t where 不及格科目数>=留级标准 order by 系别,行政班名称,学号";
		}else if("his".equalsIgnoreCase(type)){
			sql = "select 学号,姓名,系别,行政班名称,不及格科目数 from V_成绩管理_历史留级名单信息表 where 学年='" + MyTools.fixSql(this.getXNXQ()) + "' order by 系别,行政班名称,学号";
		}
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取不及格科目详情列表数据
	 * @date:2016-07-22
	 * @author:yeq
	 * @param type 类型(now当前数据/his历史数据)
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queBjgDetialList(String type, int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "";
		
		if("now".equalsIgnoreCase(type)){
			sql = "select 课程名称,学年学期,课程类型,学分,总评,重修1,重修2,补考,大补考 from (select b.课程名称,left(b.学年学期编码,5) as 学年学期," +
				"case b.课程类型 when '1' then '普通课程' " +
				"when '2' then '添加课程' " +
				"when '3' then '选修课程' " +
				"when '4' then '分层课程' " +
				"else '未知' end as 课程类型," +
				"case b.课程类型 when '1' then c.学分 " +
				"when '2' then d.学分 " +
				"when '3' then e.学分 " +
				"when '4' then f.学分 " +
				"else 0 end as 学分," +
				"a.总评,a.重修1,a.重修2,a.补考,a.大补考," +
				"case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_规则管理_授课计划明细表 c on c.授课计划明细编号=a.相关编号 " +
				"left join V_排课管理_添加课程信息表 d on d.编号=a.相关编号 " +
				"left join V_规则管理_选修课授课计划明细表 e on e.授课计划明细编号=a.相关编号 " +
				"left join (select t.分层班编号,t1.学分 from V_规则管理_分层班信息表  t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号) f on f.分层班编号=a.相关编号 " +
				"where b.是否参与留级='1' and a.学号='" + MyTools.fixSql(this.getSTUCODE()) + "' and cast(left(b.学年学期编码,4) as int)<=" + MyTools.fixSql(this.getXNXQ()) + " " +
				"and a.成绩状态='1') as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') order by 学年学期,课程名称";
		}else if("his".equalsIgnoreCase(type)){
			sql = "select 课程名称,学年学期,课程类型,总评,重修1,重修2,补考,大补考 from V_成绩管理_历史留级科目详情表 " +
				"where 学年='" + MyTools.fixSql(this.getXNXQ()) + "' and 学号='" + MyTools.fixSql(this.getSTUCODE()) + "' order by 学年学期,课程名称";
		}
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取导出学年学期下拉框
	 * @date:2016-03-28
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportSemCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,1 as orderNum " +
				"from V_规则管理_学年学期表 " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出年级下拉框
	 * @date:2016-03-28
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportNjCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,'1' as orderNum " +
				"from V_学校年级数据子类 where 年级状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出系部下拉框
	 * @date:2017-04-19
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportXbCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 系部名称 as comboName,系部代码 as comboValue,'1' as orderNum " +
				"from V_基础信息_系部信息表 where 系部代码<>'C00' order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出专业下拉框
	 * @date:2016-03-28
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportZyCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类 where 状态='1' and len(专业代码)>3 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出招生类别下拉框
	 * @date:2017-04-19
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportStuTypeCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select 类别名称,描述,'1' from V_信息类别_类别操作 where 父类别代码='ZSLBM' " +
				"order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出学科名称下拉框
	 * @date:2017-09-06
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportSubCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select 课程名称,课程号,'1' from V_课程数据子类 where 课程名称 in ('语文','数学','英语') " +
				"union all " +
				"select 课程名称,课程号,'2' from V_课程数据子类 where 课程名称 not in ('语文','数学','英语') " +
				"order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出班级下拉框
	 * @date:2016-03-28
	 * @author:yeq
	 * @param exportXb 系部
	 * @param exportNj 年级
	 * @param exportZy 专业
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportBjCombo(String exportXb, String exportNj, String exportZy) throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue " +
				"union all " +
				"select distinct 行政班名称 as comboName,行政班代码 as comboValue " +
				"from V_学校班级数据子类 where 状态='1' and right(行政班名称,3)<>'OLD'";
		if(!"all".equalsIgnoreCase(exportXb)){
			sql += " and 系部代码 in ('" + exportXb.replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(exportNj)){
			sql += " and 年级代码 in ('" + exportNj.replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(exportZy)){
			sql += " and 专业代码 in ('" + exportZy.replaceAll(",", "','") + "')";
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出班级下拉框
	 * @date:2016-09-18
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadPrintBjCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,999 as orderNum " +
				"union all " +
				"select distinct 行政班名称 as comboName,行政班代码 as comboValue,年级代码 " +
				"from V_学校班级数据子类 where 状态='1' order by orderNum desc,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取文字成绩显示内容
	 * @date:2017-03-20
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadWzcjShowCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct cast(代码 as int) as id,名称 as text from V_成绩管理_文字成绩代码表 where 状态='1' order by cast(代码 as int) desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取成绩单打印学生下拉框
	 * @date:2016-09-18
	 * @author:yeq
	 * @param stuState 学籍状态
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadPrintXsCombo(String state) throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'' as comboValue,'0' orderNum " +
				"union all " +
				"select 学号+'--'+姓名,学号,(case when 班内学号='' then '9999' else 班内学号 end) " +
				"from V_学生基本数据子类 " +
				"where 行政班代码='" + MyTools.fixSql(this.getBJMC()) + "'";
		if("normal".equalsIgnoreCase(state)){
			sql += " and 学生状态 in ('01','05','07','08')";
		}
		sql += " order by orderNum";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取打印教师下拉框
	 * @date:2016-03-29
	 * @author:yeq
	 * @return String 结果集
	 * @throws SQLException
	 */
	public String loadPrintJsCombo() throws SQLException{
		Vector vec = null;
		Vector teaVec = null;
		String sql = "";
		String result = "[{\"comboName\":\"请选择\",\"comboValue\":\"\"},";
		String tempCode = "";
//		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
//				"union all " +
//				"select distinct b.姓名 as comboName,b.工号 as comboValue,1 as orderNum  from V_成绩管理_登分教师信息表 a " +
//				"left join V_教职工基本数据子类 b on a.登分教师编号 like '%'+b.工号+'%' " +
//				"where a.登分教师编号<>'' and a.登分教师编号<>'@000@' order by orderNum,comboName";
//		vec = db.getConttexJONSArr(sql, 0, 0);
		sql = "select 工号,姓名 from V_教职工基本数据子类 order by 姓名";
		teaVec = db.GetContextVector(sql);
		
		sql = "select distinct 登分教师编号 from V_成绩管理_登分教师信息表 where 登分教师编号<>'' and 登分教师编号<>'@000@'";
		vec = db.GetContextVector(sql);
		
		for(int i=0; i<teaVec.size(); i+=2){
			tempCode = MyTools.StrFiltr(teaVec.get(i));
			
			for(int j=0; j<vec.size(); j++){
				if(MyTools.StrFiltr(vec.get(j)).indexOf(tempCode) > -1){
					result += "{\"comboName\":\"" + MyTools.StrFiltr(teaVec.get(i+1))+"（" + tempCode + "）" + "\",\"comboValue\":\"" + tempCode + "\"},";
					break;
				}
			}
		}
		result = result.substring(0, result.length()-1)+"]";
		return result;
	}
	
	/**
	 * 读取开课班下拉框数据
	 * @date:2016-03-29
	 * @author:yeq
	 * @param xnxq 学年学期
	 * @param teaCode 教师编号
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadPrintKkbCombo(String xnxq, String teaCode) throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct a.行政班名称+a.课程名称 as comboName,a.相关编号 as comboValue from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"inner join V_成绩管理_学生成绩信息表 c on c.相关编号=a.相关编号 " +
				"left join V_学生基本数据子类 d on d.学号=c.学号 " +
				"where a.状态='1' and d.学生状态 in ('01','05','07','08') and a.登分教师编号 like '%@" + MyTools.fixSql(teaCode) + "@%'";
		if(!"all".equalsIgnoreCase(xnxq)){
			sql += " and b.学年学期编码 in ('" + xnxq.replaceAll(",", "','") + "')";
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取成绩列表数据
	 * @date:2016-03-28
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param type 查询类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queScoreList(int pageNum, int pageSize, String type) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select id,stuCode,stuName,CJZT,XZBMC,KCMC,XNXQ,PSCJ,QZCJ,SXCJ,QMCJ,ZPCJ,CXCJ1,CXCJ2,BKCJ,DBKCJ,DYCJ from (" +
			"select a.编号 as id,a.学号 as stuCode,a.姓名 as stuName,a.成绩状态 as CJZT,c.行政班名称 as XZBMC,c.课程名称 as KCMC,b.学年学期名称 as XNXQ," +
			"a.平时 as PSCJ,a.期中 as QZCJ,a.实训 as SXCJ,a.期末 as QMCJ,a.总评 as ZPCJ,a.重修1 as CXCJ1,a.重修2 as CXCJ2,a.补考 as BKCJ,a.大补考 as DBKCJ,a.打印成绩 as DYCJ," +
			"b.学年学期编码,b.专业名称,";
		//判断成绩类型
		if("all".equalsIgnoreCase(this.getCJLX())){
			sql += "case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩";
		}else{
			sql += "a.总评 as 成绩";
		}
		sql += " from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_成绩管理_登分教师信息表 c on c.相关编号=a.相关编号 " +
			"left join V_学生基本数据子类 d on d.学号=a.学号 " +
			"where a.成绩状态 in ('1','2') ";
			//20170703去除异动学生过滤
			//"and d.学生状态 in ('01','05','07','08','98')";
		
		//判断查询条件
		if("queStuScore".equalsIgnoreCase(type)){
			//添加查询学生最近一个学期的成绩基础信息
			this.addStuScoreInfo();
			
			if(!"".equalsIgnoreCase(this.getSTUCODE())){
				sql += " and a.学号 like '%" + MyTools.fixSql(this.getSTUCODE()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getSTUNAME())){
				sql += " and a.姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
			}
			sql += ") as t order by stuCode,学年学期编码,KCMC";
		}else if("queScore".equalsIgnoreCase(type)){
			if(!"".equalsIgnoreCase(this.getNJDM())){
				sql += " and b.年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
			}
			if(!"".equalsIgnoreCase(this.getZYMC())){
				sql += " and b.专业名称 like '%" + MyTools.fixSql(this.getZYMC()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getBJMC())){
				sql += " and c.行政班名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getXNXQ())){
				sql += " and b.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "'";
			}
			if(!"".equalsIgnoreCase(this.getKCMC())){
				sql += " and b.课程名称 like '%" + MyTools.fixSql(this.getKCMC()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getJSXM())){
				sql += " and c.登分教师姓名 like '%" + MyTools.fixSql(this.getJSXM()) + "%'";
			}
			sql += ") as t";
			if("bjg".equalsIgnoreCase(this.getCJFW())){
				sql += " where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')";
			}
			sql += " order by 学年学期编码,专业名称,XZBMC,stuCode";
		}else{
			sql += " and 1=2) as t";
		}
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 保存修改的学生成绩
	 * @date:2016-03-28
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveScore()throws SQLException{
		String sql = "";
		String scoreState = "1";
		
		if("-1".equalsIgnoreCase(this.getPSCJ()) || "-1".equalsIgnoreCase(this.getQZCJ()) || "-1".equalsIgnoreCase(this.getSXCJ()) 
			|| "-1".equalsIgnoreCase(this.getQMCJ()) || "-1".equalsIgnoreCase(this.getZPCJ())){
			this.setPSCJ("-1");
			this.setQZCJ("-1");
			this.setSXCJ("-1");
			this.setQMCJ("-1");
			this.setZPCJ("-1");
			scoreState = "2";
		}else if(("-5".equalsIgnoreCase(this.getQMCJ())&&"".equalsIgnoreCase(this.getZPCJ())) || "-5".equalsIgnoreCase(this.getZPCJ())){
			this.setQMCJ("-5");
			this.setZPCJ("-5");
		}
		
		sql = "update V_成绩管理_学生成绩信息表 set " +
			"平时='" + MyTools.fixSql(this.getPSCJ()) + "'," +
			"期中='" + MyTools.fixSql(this.getQZCJ()) + "'," +
			"实训='" + MyTools.fixSql(this.getSXCJ()) + "'," +
			"期末='" + MyTools.fixSql(this.getQMCJ()) + "'," +
			"总评='" + MyTools.fixSql(this.getZPCJ()) + "'," +
			"重修1='" + MyTools.fixSql(this.getCXCJ1()) + "'," +
			"重修2='" + MyTools.fixSql(this.getCXCJ2()) + "'," +
			"补考='" + MyTools.fixSql(this.getBKCJ()) + "'," +
			"大补考='" + MyTools.fixSql(this.getDBKCJ()) + "'," +
			//"打印成绩='" + MyTools.fixSql(this.getDYCJ()) + "'," +
			"成绩状态='" + MyTools.fixSql(scoreState) + "' " +
			"where 编号='" + MyTools.fixSql(this.getCODE()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 成绩导出
	 * @date:2016-03-28
	 * @author:yeq
	 * @param startSemCode 起始学年学期编码
	 * @param endSemCode 结束学年学期编码
	 * @param deptCode 系部代码
	 * @param gradeCode 年级代码
	 * @param majorCode 专业代码
	 * @param classCode 班级代码
	 * @param cjfw 成绩范围
	 * @param zbwdfFlag 整班未登分判断
	 * @param cjlx 成绩类型
	 * @param zslb 招生类别
	 * @param xkmc 学科名称
	 * @throws SQLException
	 */
	public String scoreExport(String startSemCode, String endSemCode, String deptCode, String gradeCode, String majorCode, String classCode, String cjfw, String zbwdfFlag, String cjlx, String zslb, String xkmc) throws SQLException{
		Vector vec = null;
		Vector wzcjVec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String filePath = "";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
//		sql = "select 学年学期编码,学年学期名称,专业名称,行政班代码,行政班名称,课程代码,课程名称,总课时,学分,任课教师,学号,姓名,班级,平时,期中,实训,期末,总评,相关编号 from (" +
//			"select a.相关编号,b.学年学期编码,b.学年学期名称,b.专业名称,c.行政班代码,c.行政班名称,c.课程代码,c.课程名称," +
//			"case c.来源类型 when '3' then isnull(f.总课时,'') when '4' then isnull(j.总课时,'') else isnull(e.总课时,'') end as 总课时," +
//			"case c.来源类型 when '2' then cast(cast(i.学分 as float) as varchar) " +
//			"when '3' then (case when f.学分 is null then '' else cast(cast(f.学分 as float) as varchar) end) " +
//			"when '4' then (case when j.学分 is null then '' else cast(cast(j.学分 as float) as varchar) end) " +
//			"else (case when e.学分 is null then '' else cast(cast(e.学分 as float) as varchar) end) end as 学分," +
//			"replace(replace(c.登分教师姓名,'@,@',','),'@','') as 任课教师,a.学号,a.姓名,h.行政班名称 as 班级,a.平时,a.期中,a.实训,a.期末,a.总评," +
//			"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩 " +
//			"from V_成绩管理_学生成绩信息表 a " +
//			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//			"left join V_成绩管理_登分教师信息表 c on c.相关编号=a.相关编号 " +
//			"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
//			"left join V_规则管理_培养计划信息表 e on e.学年学期编码=b.学年学期编码 and e.年级代码=d.年级代码 and e.专业代码=d.专业代码 and e.课程代码=b.课程代码 " +
//			"left join V_规则管理_选修课授课计划明细表 f on f.授课计划明细编号=a.相关编号 " +
//			"left join V_排课管理_添加课程信息表 i on i.编号=a.相关编号 " +
//			"left join (select ta.分层班编号,tb.学分,tb.总课时 from V_规则管理_分层班信息表 ta left join V_规则管理_分层课程信息表 tb on tb.分层课程编号=ta.分层课程编号) j on j.分层班编号=a.相关编号 " +
//			"left join V_学生基本数据子类 g on g.学号=a.学号 " +
//			"left join V_学校班级数据子类 h on h.行政班代码=g.行政班代码 " +
//			"where a.成绩状态 in ('1','2') and g.学生状态 in ('01','05','07','08')";
		sql = "select distinct 学年学期编码,学年学期名称,case when 专业名称='' then '选修课' else 专业名称 end as 专业名称," +
			"行政班代码,行政班名称,课程代码,课程名称,总课时,学分,任课教师,学号,姓名,班级,平时,期中,实训,期末,总评,相关编号 from (" +
			"select a.相关编号,b.学年学期编码,b.学年学期名称,b.专业名称,c.行政班代码,c.行政班名称,c.课程代码,c.课程名称," +
			"case c.来源类型 when '2' then isnull(f.总课时,'') when '3' then isnull(e.总课时,'') when '4' then isnull(g.总课时,'') else isnull(d.总课时,'') end as 总课时," +
			"case c.来源类型 when '2' then cast(cast(f.学分 as float) as varchar) " +
			"when '3' then (case when e.学分 is null then '' else cast(cast(e.学分 as float) as varchar) end) " +
			"when '4' then (case when g.学分 is null then '' else cast(cast(g.学分 as float) as varchar) end) " +
			"else (case when d.学分 is null then '' else cast(cast(d.学分 as float) as varchar) end) end as 学分," +
			"replace(replace(c.登分教师姓名,'@,@',','),'@','') as 任课教师,h.班内学号 as 学号,a.姓名,i.行政班名称 as 班级,a.平时,a.期中,a.实训,a.期末,a.总评," +
			"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_成绩管理_登分教师信息表 c on c.相关编号=a.相关编号 " +
			"left join V_规则管理_授课计划明细表 d on d.授课计划明细编号=a.相关编号 " +
			"left join V_规则管理_选修课授课计划明细表 e on e.授课计划明细编号=a.相关编号 " +
			"left join V_排课管理_添加课程信息表 f on f.编号=a.相关编号 " +
			"left join (select ta.分层班编号,tb.学分,tb.总课时 from V_规则管理_分层班信息表  ta " +
			"left join V_规则管理_分层课程信息表 tb on tb.分层课程编号=ta.分层课程编号) g on g.分层班编号=a.相关编号 " +
			"left join V_学生基本数据子类 h on h.学号=a.学号 " +
			"left join V_学校班级数据子类 i on i.行政班代码=h.行政班代码 " +
			"where a.成绩状态 in ('1','2') ";
			//20170703去除异动学生过滤
			//"and h.学生状态 in ('01','05','07','08')";
		
		if(!"all".equalsIgnoreCase(startSemCode))
			sql += " and cast(b.学年学期编码 as int)>=" + startSemCode;
			//sql += " and b.学年学期编码 in ('" + semCode.replaceAll(",", "','") + "') ";
		if(!"all".equalsIgnoreCase(endSemCode))
			sql += " and cast(b.学年学期编码 as int)<=" + endSemCode;
		if(!"all".equalsIgnoreCase(gradeCode))
			sql += " and i.系部代码 in ('" + deptCode.replaceAll(",", "','") + "') ";
		if(!"all".equalsIgnoreCase(gradeCode))
			sql += " and i.年级代码 in ('" + gradeCode.replaceAll(",", "','") + "') ";
		if(!"all".equalsIgnoreCase(majorCode))
			sql += " and b.专业代码 in ('" + majorCode.replaceAll(",", "','") + "') ";
		if(!"all".equalsIgnoreCase(classCode))
			sql += " and c.行政班代码 in ('" + classCode.replaceAll(",", "','") + "') ";
		//判断是否需要过滤整班未登分成绩信息
		if("exclude".equalsIgnoreCase(zbwdfFlag)){
			sql += " and a.相关编号 not in (select distinct 相关编号 from (select b.相关编号," +
				"case when b.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=b.相关编号) " +
				"when b.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=b.相关编号) " +
				"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.行政班代码) end as 需登分人数," +
				"(select count(*) from V_成绩管理_学生成绩信息表 " +
				"where 相关编号=a.相关编号 and ((case when 平时 in ('-1','-5') then '' else 平时 end)<>'' " +
				"or (case when 期中 in ('-1','-5') then '' else 期中 end)<>'' " +
				"or (case when 实训 in ('-1','-5') then '' else 实训 end)<>'' " +
				"or (case when 期末 in ('-1','-5') then '' else 期末 end)<>'' " +
				"or (case when 总评 in ('-1','-5') then '' else 总评 end)<>'' " +
				"or (case when 重修1 in ('-1','-5') then '' else 重修1 end)<>'' " +
				"or (case when 重修2 in ('-1','-5') then '' else 重修2 end)<>'' " +
				"or (case when 补考 in ('-1','-5') then '' else 补考 end)<>'' " +
				"or (case when 大补考 in ('-1','-5') then '' else 补考 end)<>'')) as 已登分人数 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态='1'";
			if(!"all".equalsIgnoreCase(startSemCode))
				sql += " and cast(c.学年学期编码 as int)>=" + startSemCode;
			if(!"all".equalsIgnoreCase(endSemCode))
				sql += " and cast(c.学年学期编码 as int)<=" + endSemCode;
			sql += ") as t where 已登分人数=0)";
		}
		//招生类别
		if(!"all".equalsIgnoreCase(zslb))
			sql += " and h.招生类别 in ('" + zslb.replaceAll(",", "','") + "')";
		//学科
		if(!"all".equalsIgnoreCase(xkmc))
			sql += " and b.课程代码 in ('" + xkmc.replaceAll(",", "','") + "')";
		sql += ") as z";
		
		if("bjg".equalsIgnoreCase(cjfw))
			sql += " where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')";
		
		sql += " order by 行政班代码,学年学期编码,课程代码";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//获取文字成绩代码信息
			sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1'";
			wzcjVec = db.GetContextVector(sql);
			
			String xnxqbm = "";//学年学期编码
			String xnxqmc = "";//学年学期名称
			String zymc = "";//专业名称
			String xzbdm = "";//行政班代码
			String xzbmc = "";//行政班名称
			String kcdm = "";//课程代码
			String kcmc = "";//课程名称
			String zks = "";//总课时
			String xf = "";//学分
			String rkjs = "";//任课教师
			String xh = "";//学号
			String xm = "";//姓名
			String bj = "";//班级
			String pscj = "";//平时成绩
			String qzcj = "";//期中成绩
			String sxcj = "";//实训成绩
			String qmcj = "";//期末成绩
			String zpcj = "";//总评成绩
			String xgbh = "";//相关编号
			int num = 1;//序号
			
			//整理数据
			for(int i=0; i<vec.size(); i+=19){
				if(i == 0){
					xnxqbm = MyTools.StrFiltr(vec.get(i));
					xnxqmc = MyTools.StrFiltr(vec.get(i+1));
					zymc = MyTools.StrFiltr(vec.get(i+2));
					xzbdm = MyTools.StrFiltr(vec.get(i+3));
					xzbmc = MyTools.StrFiltr(vec.get(i+4));
					kcdm = MyTools.StrFiltr(vec.get(i+5));
					kcmc = MyTools.StrFiltr(vec.get(i+6));
					zks = MyTools.StrFiltr(vec.get(i+7));
					xf = MyTools.StrFiltr(vec.get(i+8));
					rkjs = MyTools.StrFiltr(vec.get(i+9));
					xgbh = MyTools.StrFiltr(vec.get(i+18));
					
					resultVec.add(xgbh);
					resultVec.add(xnxqmc);
					resultVec.add(zymc);
					resultVec.add(xzbmc);
					resultVec.add(kcmc);
					resultVec.add(zks);
					resultVec.add(xf);
					resultVec.add(rkjs);
				}else{
					//判断是否同一门课程
					if(!xnxqbm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+3))) || !xzbmc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) || !kcdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
						//添加2条空数据（文件预留两行）
						for(int a=0; a<2; a++){
							tempVec.add(num);
							num++;
							
							for(int b=0; b<8; b++){
								tempVec.add("");
							}
						}
						resultVec.add(tempVec);
						
						tempVec = new Vector();
						num = 1;
						xnxqbm = MyTools.StrFiltr(vec.get(i));
						xnxqmc = MyTools.StrFiltr(vec.get(i+1));
						zymc = MyTools.StrFiltr(vec.get(i+2));
						xzbdm = MyTools.StrFiltr(vec.get(i+3));
						xzbmc = MyTools.StrFiltr(vec.get(i+4));
						kcdm = MyTools.StrFiltr(vec.get(i+5));
						kcmc = MyTools.StrFiltr(vec.get(i+6));
						zks = MyTools.StrFiltr(vec.get(i+7));
						xf = MyTools.StrFiltr(vec.get(i+8));
						rkjs = MyTools.StrFiltr(vec.get(i+9));
						xgbh = MyTools.StrFiltr(vec.get(i+18));
						
						resultVec.add(xgbh);
						resultVec.add(xnxqmc);
						resultVec.add(zymc);
						resultVec.add(xzbmc);
						resultVec.add(kcmc);
						resultVec.add(zks);
						resultVec.add(xf);
						resultVec.add(rkjs);
					}
				}
				
				xh = MyTools.StrFiltr(vec.get(i+10));
				xm = MyTools.StrFiltr(vec.get(i+11));
				bj = MyTools.StrFiltr(vec.get(i+12));
				pscj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+13)));
				qzcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+14)));
				sxcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+15)));
				qmcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+16)));
				zpcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+17)));
				
				tempVec.add(num);
				tempVec.add(xh);
				tempVec.add(xm);
				tempVec.add(pscj);
				tempVec.add(qzcj);
				tempVec.add(sxcj);
				tempVec.add(qmcj);
				tempVec.add(zpcj);
				tempVec.add(bj);
				num++;
			}
			//添加2条空数据（文件预留两行）
			for(int a=0; a<2; a++){
				tempVec.add(num);
				num++;
				
				for(int b=0; b<8; b++){
					tempVec.add("");
				}
			}
			resultVec.add(tempVec);
			
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			
			String rootPath = MyTools.getProp(request, "Base.exportExcelPath");
			rootPath += "scoreExport/成绩信息"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date);
			String title = "";
			String savePath = "";
			String[] titleArray = new String[0];
			String examType = "";
			String sxFlag = "";
			Vector infoVec = null;
			
			//查询相关课程的考试类型及是否有实训
			sql = "select a.相关编号,a.考试类型,a.实训 from V_成绩管理_登分设置信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
				"where a.状态='1' and b.状态='1' and c.状态='1'";
			if(!"all".equalsIgnoreCase(startSemCode))
				sql += " and cast(c.学年学期编码 as int)>=" + startSemCode;
			if(!"all".equalsIgnoreCase(endSemCode))
				sql += " and cast(c.学年学期编码 as int)<=" + endSemCode;
			infoVec = db.GetContextVector(sql);
			if(infoVec == null)
				infoVec = new Vector();
			
			//导出成绩信息
			for(int a=0; a<resultVec.size(); a+=9){
				xgbh = MyTools.StrFiltr(resultVec.get(a));
				xnxqmc = MyTools.StrFiltr(resultVec.get(a+1));
				zymc = MyTools.StrFiltr(resultVec.get(a+2));
				xzbmc = MyTools.StrFiltr(resultVec.get(a+3));
				kcmc = MyTools.StrFiltr(resultVec.get(a+4));
				zks = MyTools.StrFiltr(resultVec.get(a+5));
				xf = MyTools.StrFiltr(resultVec.get(a+6));
				rkjs = MyTools.StrFiltr(resultVec.get(a+7));
				tempVec = (Vector)resultVec.get(a+8);
				
				xnxqmc = xnxqmc.replace(" ", "度").replace("一", "1").replace("二", "2");
				savePath = rootPath+"/"+xnxqmc+"/"+zymc+"/"+xzbmc;
				title = schoolName+xnxqmc+"成绩登记表";
				
				//判断是否有相关设置信息
				if(infoVec.indexOf(xgbh) > -1){
					examType = MyTools.StrFiltr(infoVec.get(infoVec.indexOf(xgbh)+1));
					sxFlag = MyTools.StrFiltr(infoVec.get(infoVec.indexOf(xgbh)+2));
					
					if("1".equalsIgnoreCase(examType)){
						titleArray = new String[]{"序号","学号","姓名","平时","期中考试","实训","期末考试","学期成绩","所属班级"};
					}else{
						if("0".equalsIgnoreCase(sxFlag)){
							titleArray = new String[]{"序号","学号","姓名","阶段一","阶段二","阶段三","学期成绩","所属班级"};
						}else{
							titleArray = new String[]{"序号","学号","姓名","阶段一","阶段二","阶段三","阶段四","学期成绩","所属班级"};
						}
					}
				}else{
					examType = "1";
					sxFlag = "0";
					titleArray = new String[]{"序号","学号","姓名","平时","期中考试","实训","期末考试","学期成绩","所属班级"};
				}
				
				try {
					//创建
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
					}
					
					//输出流
					OutputStream os = new FileOutputStream(savePath + "/" + kcmc + ".xls");
					WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
					
					this.exportScore("exportExcel", wbook, tempVec, titleArray, title, xzbmc, kcmc, zks, xf, rkjs, examType, sxFlag);
					
					//写入文件
					wbook.write();
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
			}
			
			filePath = rootPath+".zip";
			//打包文件
			this.compressZip(rootPath, filePath);
			//删除原文件
			this.deleteDirectory(rootPath);
			
			this.setMSG("成绩文件生成成功");
		}else{
			this.setMSG("没有符合条件的成绩信息");
		}
		
		return filePath;
	}
	
	/**
	 * 解析文字成绩
	 * @date:2016-03-28
	 * @author:yeq
	 * @param wzcjVec 文字成绩代码信息
	 * @param score 成绩
	 */
	public String parseScore(Vector wzcjVec, String score){
		String result = "";
		
		if(MyTools.StringToInt(score) < 0){
			for(int i=0; i<wzcjVec.size(); i+=2){
				if(MyTools.StrFiltr(wzcjVec.get(i)).equalsIgnoreCase(score)){
					result = MyTools.StrFiltr(wzcjVec.get(i+1));
					break;
				}
			}
		}
		if("".equalsIgnoreCase(result))
			result = score;
		
		return result;
	}
	
	/**
	 * 解析学分
	 * @date:2016-09-18
	 * @author:yeq
	 * @param score 成绩
	 */
	public String parseXf(String score){
		if(!"".equalsIgnoreCase(score)){
			boolean flag = true;
			while(flag){
				if(score.indexOf(".")>-1 && "0".equalsIgnoreCase(score.substring(score.length()-1))){
					score = score.substring(0, score.length()-1);
					
					if("0".equalsIgnoreCase(score)){
						flag = false;
					}
				}else{
					flag = false;
				}
			}
			
			if(".".equalsIgnoreCase(score.substring(score.length()-1)))
				score = score.substring(0, score.length()-1);
		}
		return score;
	}
	
	/**
	 * 导出成绩xls
	 * @date:2016-03-29
	 * @author:yeq
	 * @param type 导出/打印
	 * @param wbook
	 * @param scoreVec 成绩信息
	 * @param titleArray 表格标题数组
	 * @param title 标题
	 * @param xzbmc 行政班名称
	 * @param kcmc 课程名称
	 * @param zks 总课时
	 * @param xf 学分
	 * @param rkjs 任课教师
	 * @param examType 考试类型
	 * @param sxFlag 实训
	 * @throws WriteException, FileNotFoundException 
	 */
	public void exportScore(String type, WritableWorkbook wbook, Vector scoreVec, String[] titleArray, String title, String xzbmc, String kcmc, String zks, String xf, String rkjs, String examType, String sxFlag) throws IOException, WriteException, FileNotFoundException{
		WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
		WritableFont fontStyle;
		WritableCellFormat contentStyle;
		Label content;
		int maxRowNum = 0;
		String curScore = "";
		
		//设置列宽
		wsheet.setColumnView(0, 6);
		wsheet.setColumnView(1, 9);
		wsheet.setColumnView(2, 9);
		if("1".equalsIgnoreCase(examType)){
			wsheet.setColumnView(3, 6);
			wsheet.setColumnView(4, 9);
			wsheet.setColumnView(5, 6);
			wsheet.setColumnView(6, 9);
			wsheet.setColumnView(7, 9);
			wsheet.setColumnView(8, 25);
		}else{
			if("0".equalsIgnoreCase(sxFlag)){
				wsheet.setColumnView(3, 8);
				wsheet.setColumnView(4, 8);
				wsheet.setColumnView(5, 8);
				wsheet.setColumnView(6, 10);
				wsheet.setColumnView(7, 30);
				
				//删除实训（阶段三）数据
				for(int i=0; i<scoreVec.size(); i+=9){
					scoreVec.remove(i+5);
					i--;
				}
			}else{
				wsheet.setColumnView(3, 7);
				wsheet.setColumnView(4, 7);
				wsheet.setColumnView(5, 7);
				wsheet.setColumnView(6, 7);
				wsheet.setColumnView(7, 10);
				wsheet.setColumnView(8, 26);
			}
		}
		
		wsheet.setRowView(1, 400);
		wsheet.setRowView(2, 400);
		wsheet.setRowView(3, 400);
		
		//标题
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
		
		wsheet.mergeCells(0, 0, titleArray.length-1, 0);
		content = new Label(0, 0, title, contentStyle);
		wsheet.addCell(content);
		
		//标题信息
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.LEFT);//左对齐
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
		
		wsheet.mergeCells(0, 1, 5, 1);
		content = new Label(0, 1, "学科："+kcmc, contentStyle);
		wsheet.addCell(content);
		
		if("2".equalsIgnoreCase(examType) && "0".equalsIgnoreCase(sxFlag)){
			wsheet.mergeCells(6, 1, 7, 1);
			content = new Label(6, 1, "总学时："+zks+"     学分："+xf, contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(0, 2, 6, 2);
			content = new Label(0, 2, "上课班级："+xzbmc, contentStyle);
			wsheet.addCell(content);
			
			content = new Label(7, 2, "任课教师："+rkjs, contentStyle);
			wsheet.addCell(content);
		}else{
			wsheet.mergeCells(6, 1, 7, 1);
			content = new Label(6, 1, "总学时："+zks, contentStyle);
			wsheet.addCell(content);
			
			content = new Label(8, 1, "学分："+xf, contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(0, 2, 6, 2);
			content = new Label(0, 2, "上课班级："+xzbmc, contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(7, 2, 8, 2);
			content = new Label(7, 2, "任课教师："+rkjs, contentStyle);
			wsheet.addCell(content);
		}
		
		//表格标题行
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		
		for (int i=0; i<titleArray.length; i++) {
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			//contentStyle.setWrap(true);// 自动换行
			contentStyle.setShrinkToFit(true);//字体大小自适应
			
			if(i == 0){
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}else if(i == titleArray.length-1){
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}else{
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}
			content = new Label(i, 3, titleArray[i], contentStyle);
			wsheet.addCell(content);
		}
		
		//表格内容
		//k:用于循环时Excel的行号
		//外层for用于循环行,内曾for用于循环列
		for (int i=0, k=4; i<scoreVec.size(); i+=titleArray.length, k++) {
			for (int j=0; j<titleArray.length; j++) {
//				if(j == titleArray.length-1){
//					if(MyTools.StrFiltr(scoreVec.get(i+titleArray.length-1)).length() < 15){
//						fontStyle = new WritableFont(
//								WritableFont.createFont("宋体"), 8, WritableFont.NO_BOLD,
//								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//								jxl.format.Colour.BLACK);
//					}else if(MyTools.StrFiltr(scoreVec.get(i+titleArray.length-1)).length() < 19){
//						fontStyle = new WritableFont(
//								WritableFont.createFont("宋体"), 7, WritableFont.NO_BOLD,
//								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//								jxl.format.Colour.BLACK);
//					}else{
//						fontStyle = new WritableFont(
//								WritableFont.createFont("宋体"), 6, WritableFont.NO_BOLD,
//								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//								jxl.format.Colour.BLACK);
//					}
//				}else{
//					fontStyle = new WritableFont(
//							WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
//							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//							jxl.format.Colour.BLACK);
//				}
				curScore = MyTools.StrFiltr(scoreVec.get(i+j));
				
				if(j>2 && !"".equalsIgnoreCase(curScore) && ((MyTools.StringToInt(curScore)>0&&MyTools.StringToInt(curScore)<60) 
						|| "作弊".equalsIgnoreCase(curScore) || "取消资格".equalsIgnoreCase(curScore) || "缺考".equalsIgnoreCase(curScore) || "不及格".equalsIgnoreCase(curScore) || "不合格".equalsIgnoreCase(curScore))){
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.RED);
				}else{
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
				}
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				//contentStyle.setWrap(true);// 自动换行
				contentStyle.setShrinkToFit(true);//字体大小自适应
				
				if(j == 0){
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
				}else if(j == titleArray.length-1){
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				}
				
				if((i+titleArray.length)==scoreVec.size() || ("printView".equalsIgnoreCase(type) && i>0 && i/titleArray.length%39==0)){
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}
				
				if("printView".equalsIgnoreCase(type) && i>0 && i/titleArray.length%40 == 0){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				}
				
				content = new Label(j, k, curScore, contentStyle);
				wsheet.addCell(content);
			}
			
			if("printView".equalsIgnoreCase(type) && scoreVec.size()/titleArray.length>40){
				if(i>0 && i/titleArray.length%39==0){
					if(i < titleArray.length*40){
						k += 4;
					}else{
						k += 10;
					}
				}
			}
			
			maxRowNum = k;
			wsheet.setRowView(k, 300);
		}
		
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.LEFT);//左对齐
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
		
		if("2".equalsIgnoreCase(examType) && "0".equalsIgnoreCase(sxFlag)){
			wsheet.mergeCells(6, maxRowNum+2, 7, maxRowNum+2);
			content = new Label(6, maxRowNum+2, "     阅卷教师签名：", contentStyle);
		}else{
			wsheet.mergeCells(7, maxRowNum+2, 8, maxRowNum+2);
			content = new Label(7, maxRowNum+2, "     阅卷教师签名：", contentStyle);
		}
		wsheet.addCell(content);
	}
	
	/**
	 * 压缩文件
	 * @date:2016-03-29
	 * @author:yeq
	 * @param fileName 需要被压缩的文件/文件夹 
	 * @param zipFileName 最终压缩生成的压缩文件：目录+压缩文件名.zip 
	 */
    public void compressZip(String fileName, String zipFileName) {  
        File srcdir = new File(fileName);  
        if (!srcdir.exists())  
            throw new RuntimeException(fileName + "不存在！");
        
        File zipFile = new File(zipFileName);
        Project prj = new Project();  
        Zip zip = new Zip();  
        zip.setProject(prj);  
        zip.setDestFile(zipFile);  
        FileSet fileSet = new FileSet();  
        fileSet.setProject(prj);  
        fileSet.setDir(srcdir);  
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");  
        //fileSet.setExcludes(...); 排除哪些文件或文件夹  
        zip.addFileset(fileSet);  
          
        zip.execute();  
    }  
	
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     */  
    public boolean deleteDirectory(String sPath){
    	boolean flag = true;
    	
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)){  
            sPath = sPath + File.separator;
        }
        
        File dirFile = new File(sPath);
        
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()){
            return false;
        }
        
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
    
    /** 
     * 删除单个文件 
     * @param sPath 被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public boolean deleteFile(String sPath) {
    	boolean flag = false;
        File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }
        return flag;  
    }
    
    /**
	 * 锁定成绩
	 * @date:2016-03-30
	 * @author:yeq
	 * @throws SQLException
	 */
	public void lockScore()throws SQLException{
		String sql = "";
		String sqlCondition = "";
		
//		if("all".equalsIgnoreCase(this.getCODE())){
//			sqlCondition = "select distinct a.相关编号 from V_成绩管理_登分教师信息表 a " +
//					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//					"where a.状态='1' and a.登分教师编号 like '%" + MyTools.fixSql(this.getJSXM()) + "%'";
//			if(!"all".equalsIgnoreCase(this.getXNXQ())){
//				sqlCondition += " and b.学年学期编码 in ('" + this.getXNXQ().replaceAll(",", "','") + "')";
//			}
//		}else{
//			sqlCondition = "'" + this.getCODE().replaceAll(",", "','") + "'";
//		}
//		sql = "update V_成绩管理_登分教师信息表 set 打印锁定='1' " +
//			"where 相关编号 in (" + sqlCondition + ")";
		
		sql = "update V_成绩管理_登分教师信息表 set 打印锁定='1' " +
			"where 编号 in (select 编号 from V_成绩管理_登分教师信息表 where 科目编号=(select 科目编号 from V_成绩管理_登分教师信息表 " +
			"where 相关编号='" + MyTools.fixSql(this.getCODE()) + "') " +
			"and 登分教师编号='@" + MyTools.fixSql(this.getJSXM()) + "@')";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("锁定成功");
		}else{
			this.setMSG("锁定失败");
		}
	}
    
    /**
	 * 解锁
	 * @date:2016-03-29
	 * @author:yeq
	 * @throws SQLException
	 */
	public void unlock()throws SQLException{
		String sql = "";
		String sqlCondition = "";
		
//		if("all".equalsIgnoreCase(this.getCODE())){
//			sqlCondition = "select distinct a.相关编号 from V_成绩管理_登分教师信息表 a " +
//					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//					"where a.状态='1' and a.登分教师编号 like '%@" + MyTools.fixSql(this.getJSXM()) + "@%'";
//			if(!"all".equalsIgnoreCase(this.getXNXQ())){
//				sqlCondition += " and b.学年学期编码 in ('" + this.getXNXQ().replaceAll(",", "','") + "')";
//			}
//		}else{
//			sqlCondition = "'" + this.getCODE().replaceAll(",", "','") + "'";
//		}
//		sql = "update V_成绩管理_登分教师信息表 set 打印锁定='0' " +
//			"where 相关编号 in (" + sqlCondition + ")";
		sql = "update V_成绩管理_登分教师信息表 set 打印锁定='0' " +
			"where 编号 in (select 编号 from V_成绩管理_登分教师信息表 where 科目编号=(select 科目编号 from V_成绩管理_登分教师信息表 " +
			"where 相关编号='" + MyTools.fixSql(this.getCODE()) + "') " +
			"and 登分教师编号='@" + MyTools.fixSql(this.getJSXM()) + "@')";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("解锁成功");
		}else{
			this.setMSG("解锁失败");
		}
	}
    
	/**
	 * 学生成绩单打印预览
	 * @date:2016-09-18
	 * @author:yeq
	 * @param startSemCode 起始学年学期
	 * @param endSemCode 结束学年学期
	 * @param classCode 班级编号
	 * @param stuState 学籍状态
	 * @param stuCode 学号
	 * @retnun String 预览文件路径
	 * @throws SQLException
	 */
	public String loadStuReportPrintView(HttpServletRequest request, String startSemCode, String endSemCode, String classCode, String stuState, String stuCode) throws SQLException{
		Vector vec = null;
		Vector wzcjVec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String filePath = "";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		sql = "select j.系名称,h.专业名称,g.行政班名称,a.姓名,a.学号,b.课程名称," +
			"case b.课程类型 when '1' then c.学分 " +
			"when '2' then d.学分 " +
			"when '3' then e.学分 " +
			"when '4' then k.学分 " +
			"else 0 end as 学分," +
			"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩,left(b.学年学期编码,5) as 学年学期 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_授课计划明细表 c on c.授课计划明细编号=a.相关编号 " +
			"left join V_排课管理_添加课程信息表 d on d.编号=a.相关编号 " +
			"left join V_规则管理_选修课授课计划明细表 e on e.授课计划明细编号=a.相关编号 " +
			"left join (select t.分层班编号,t1.学分 from V_规则管理_分层班信息表  t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号) k on k.分层班编号=a.相关编号 " +
			"left join V_学生基本数据子类 f on f.学号=a.学号 " +
			"left join V_学校班级数据子类 g on g.行政班代码=f.行政班代码 " +
			"left join V_专业基本信息数据子类 h on h.专业代码=g.专业代码 " +
			"left join V_基础信息_系专业关系表 i on i.专业代码=h.专业代码 " +
			"left join V_基础信息_系基础信息表 j on j.系代码=i.系代码 " +
			"where a.成绩状态 in ('1','2')";
		if("normal".equalsIgnoreCase(stuState))
			sql += " and f.学生状态 in ('01','05','07','08')";
		
		if(!"all".equalsIgnoreCase(startSemCode))
			sql += " and cast(b.学年学期编码 as int)>=" + startSemCode;
		if(!"all".equalsIgnoreCase(endSemCode))
			sql += " and cast(b.学年学期编码 as int)<=" + endSemCode;
		
		//判断是单个学生还是整班
		if(!"".equalsIgnoreCase(stuCode)){
//			if(!"".equalsIgnoreCase(stuCode))
				sql += " and a.学号='" + MyTools.fixSql(stuCode) + "'";
			
//			if(!"".equalsIgnoreCase(stuName))
//				sql += " and a.姓名 like '%" + MyTools.fixSql(stuName) + "%'";
		}else{
			sql += " and f.行政班代码='" + MyTools.fixSql(classCode) + "'";
		}
		sql += " order by (case when f.班内学号='' then '9999' else f.班内学号 end),b.学年学期编码,b.课程名称";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//获取文字成绩代码信息
			sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1'";
			wzcjVec = db.GetContextVector(sql);
			
			String xmc = "";//系名称
			String zymc = "";//专业名称
			String bjmc = "";//班级名称
			String xm = "";//姓名
			String xh = "";//学号
			String kmmc = "";//科目名称
			String xf = "";//学分
			String cj = "";//成绩
			String xnxq = "";//学年学期
			int addNum = 0;
			int maxNum = 31;
			
			//整理数据
			for(int i=0; i<vec.size(); i+=9){
				kmmc = MyTools.StrFiltr(vec.get(i+5));
				xf = this.parseXf(MyTools.StrFiltr(vec.get(i+6)));
				cj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+7)));
				xnxq = MyTools.StrFiltr(vec.get(i+8));
				
				//判断是否同一门课程
				if(!xmc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !zymc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) 
					|| !bjmc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2))) || !xm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+3))) 
					|| !xh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4)))){
					if(i > 0){
						//判断成绩数据内容是否满足一页，不足的话补足
						addNum = 0;
						if(tempVec.size()%8 > 0){
							for(int a=0; a<4; a++){
								tempVec.add("");
							}
						}
						if(tempVec.size()/8 < maxNum){
							addNum = maxNum - tempVec.size()/8;
						}else if(tempVec.size()/8 > maxNum){
							addNum = maxNum+4 - tempVec.size()/8%maxNum;
						}
						if(addNum > 0){
							for(int a=0; a<addNum; a++){
								for(int b=0; b<8; b++){
									tempVec.add("");
								}
							}
						}
						resultVec.add(tempVec);
					}
					tempVec = new Vector();
					xmc = MyTools.StrFiltr(vec.get(i));
					zymc = MyTools.StrFiltr(vec.get(i+1));
					bjmc = MyTools.StrFiltr(vec.get(i+2));
					xm = MyTools.StrFiltr(vec.get(i+3));
					xh = MyTools.StrFiltr(vec.get(i+4));
					
					resultVec.add(xmc);
					resultVec.add(zymc);
					resultVec.add(bjmc);
					resultVec.add(xm);
					resultVec.add(xh);
				}
				
				tempVec.add(kmmc);
				tempVec.add(xf);
				tempVec.add(cj);
				tempVec.add(xnxq);
			}
			
			//判断成绩数据内容是否满足一页，不足的话补足
			addNum = 0;
			if(tempVec.size()%8 > 0){
				for(int i=0; i<4; i++){
					tempVec.add("");
				}
			}
			if(tempVec.size()/8 < maxNum){
				addNum = maxNum - tempVec.size()/8;
			}else if(tempVec.size()/8 > 30){
				addNum = maxNum+4 - tempVec.size()/8%maxNum;
			}
			if(addNum > 0){
				for(int i=0; i<addNum; i++){
					for(int j=0; j<8; j++){
						tempVec.add("");
					}
				}
			}
			
			resultVec.add(tempVec);
		}
			
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		filePath = request.getSession().getServletContext().getRealPath("/")+"form/registerScoreManage/printView";
		filePath = filePath.replaceAll("\\\\", "/");
		String title = "";
		String[] titleArray = new String[]{"科目","学分","成绩","学期","科目","学分","成绩","学期"};
		int curRowNum = 0;
		String curContent = "";
		
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		String fileName = "reportView"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+second+".xls";
		filePath += "/" + fileName;
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			title = schoolName+"全日制高职各科成绩";
			//设置列宽
			wsheet.setColumnView(0, 23);
			wsheet.setColumnView(1, 6);
			wsheet.setColumnView(2, 7);
			wsheet.setColumnView(3, 8);
			wsheet.setColumnView(4, 23);
			wsheet.setColumnView(5, 6);
			wsheet.setColumnView(6, 7);
			wsheet.setColumnView(7, 8);
			
			if(resultVec.size() > 0){
				//导出成绩信息
				for(int a=0; a<resultVec.size(); a+=6){
					tempVec = (Vector)resultVec.get(a+5);
					
					//标题
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					
					wsheet.mergeCells(0, curRowNum, titleArray.length-1, curRowNum);
					content = new Label(0, curRowNum, title, contentStyle);
					wsheet.addCell(content);
					curRowNum++;
					
					//学生信息
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 11, WritableFont.BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.LEFT);//左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					
					wsheet.mergeCells(0, curRowNum, 2, curRowNum);
					content = new Label(0, curRowNum, "系："+MyTools.StrFiltr(resultVec.get(a)), contentStyle);
					wsheet.addCell(content);
					wsheet.mergeCells(3, curRowNum, 7, curRowNum);
					content = new Label(3, curRowNum, "专业："+MyTools.StrFiltr(resultVec.get(a+1)), contentStyle);
					wsheet.addCell(content);
					wsheet.setRowView(curRowNum, 400);
					curRowNum++;
					
					wsheet.mergeCells(0, curRowNum, titleArray.length-1, curRowNum);
					content = new Label(0, curRowNum, "班级："+MyTools.StrFiltr(resultVec.get(a+2)), contentStyle);
					wsheet.addCell(content);
					wsheet.setRowView(curRowNum, 400);
					curRowNum++;
					
					wsheet.mergeCells(0, curRowNum, 2, curRowNum);
					content = new Label(0, curRowNum, "姓名："+MyTools.StrFiltr(resultVec.get(a+3)), contentStyle);
					wsheet.addCell(content);
					wsheet.mergeCells(3, curRowNum, 7, curRowNum);
					content = new Label(3, curRowNum, "学号："+MyTools.StrFiltr(resultVec.get(a+4)), contentStyle);
					wsheet.addCell(content);
					wsheet.setRowView(curRowNum, 400);
					curRowNum++;
					
					//表格标题行
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					
					for (int i=0; i<titleArray.length; i++) {
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						contentStyle.setWrap(true);// 自动换行
						
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						if(i==0 || i==titleArray.length/2){
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}else if(i == titleArray.length-1){
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}else{
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}
						content = new Label(i, curRowNum, titleArray[i], contentStyle);
						wsheet.addCell(content);
					}
					curRowNum++;
					
					//表格内容
					//k:用于循环时Excel的行号
					//外层for用于循环行,内曾for用于循环列
					for (int i=0; i<tempVec.size(); i+=titleArray.length) {
						for (int j=0; j<titleArray.length; j++) {
							curContent = MyTools.StrFiltr(tempVec.get(i+j));
							
							if("成绩".equalsIgnoreCase(titleArray[j]) && !"".equalsIgnoreCase(curContent) && ((MyTools.StringToInt(curContent)>0&&MyTools.StringToInt(curContent)<60) 
									|| "作弊".equalsIgnoreCase(curContent) || "取消资格".equalsIgnoreCase(curContent) || "缺考".equalsIgnoreCase(curContent) || "不及格".equalsIgnoreCase(curContent) || "不合格".equalsIgnoreCase(curContent))){
								fontStyle = new WritableFont(
										WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
										false, jxl.format.UnderlineStyle.NO_UNDERLINE,
										jxl.format.Colour.RED);
							}else{
								fontStyle = new WritableFont(
										WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
										false, jxl.format.UnderlineStyle.NO_UNDERLINE,
										jxl.format.Colour.BLACK);
							}
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
							//contentStyle.setWrap(true);// 自动换行
							contentStyle.setShrinkToFit(true);//字体大小自适应
							
							if(j==0 || j==titleArray.length/2){
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							}else if(j == titleArray.length-1){
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							}
							if((i+titleArray.length) == tempVec.size()){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
							
							content = new Label(j, curRowNum, curContent, contentStyle);
							wsheet.addCell(content);
						}
						
						wsheet.setRowView(curRowNum, 400);
						curRowNum++;
					}
				}
			}else{
				//标题
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet.mergeCells(0, 0, titleArray.length-1, 0);
				content = new Label(0, 0, "没有成绩数据", contentStyle);
				wsheet.addCell(content);
			}
			
			//写入文件
			wbook.write();
			wbook.close();
			os.close();
		} catch (FileNotFoundException e) {
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e) {
			this.setMSG("文件生成失败");
		} catch (IOException e) {
			this.setMSG("文件生成失败");
		}
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		//poCtrl1.setWriter(wb);
		poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
		poCtrl1.setJsFunction_AfterDocumentClosed("AfterDocumentClosed()");
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		//String fileName = "template.xls";
		
		//创建自定义菜单栏
		//poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("下载", "download()", 3);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("关闭", "closeWindow()", 4);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "全日制高职各科成绩");
		poCtrl1.setFileTitle(schoolName + "全日制高职各科成绩");//设置另存为窗口默认文件名
		
		//打开文件
		poCtrl1.webOpen("printView/"+fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
		return filePath;
	}
	
	/**
	 * 成绩打印预览
	 * @date:2016-03-29
	 * @author:yeq
	 * @param xnxq 学年学期
	 * @param teaCode 教师编号
	 * @param kkb 开课班
	 * @retnun String 预览文件路径
	 * @throws SQLException
	 */
	public String loadScorePrintView(HttpServletRequest request, String xnxq, String teaCode, String kkb) throws SQLException, UnsupportedEncodingException{
		Vector vec = null;
		Vector wzcjVec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String sqlCondition = "";
		String filePath = "";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		String fileName = "view"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+second+".xls";
		
		sql = "select b.学年学期编码,b.学年学期名称,b.专业名称,c.行政班代码,c.行政班名称,c.课程代码,c.课程名称," +
			"case c.来源类型 when '3' then isnull(f.总课时,'') when '4' then isnull(j.总课时,'') else isnull(e.总课时,'') end as 总课时," +
			"case c.来源类型 when '2' then cast(cast(i.学分 as float) as varchar) " +
			"when '3' then (case when f.学分 is null then '' else cast(cast(f.学分 as float) as varchar) end) " +
			"when '4' then (case when j.学分 is null then '' else cast(cast(j.学分 as float) as varchar) end) " +
			"else (case when e.学分 is null then '' else cast(cast(e.学分 as float) as varchar) end) end as 学分," +
			"replace(replace(c.登分教师姓名,'@,@',','),'@','') as 任课教师,a.学号,a.姓名,h.行政班名称,a.平时,a.期中,a.实训,a.期末,a.总评 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_成绩管理_登分教师信息表 c on c.相关编号=a.相关编号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
			//"left join V_规则管理_培养计划信息表 e on e.学年学期编码=b.学年学期编码 and e.年级代码=d.年级代码 and e.专业代码=d.专业代码 and e.课程代码=b.课程代码 " +
			"left join V_规则管理_授课计划明细表 e on e.授课计划明细编号=a.相关编号 " +
			"left join V_规则管理_选修课授课计划明细表 f on f.授课计划明细编号=a.相关编号 " +
			"left join V_排课管理_添加课程信息表 i on i.编号=a.相关编号 " +
			"left join (select ta.分层班编号,tb.学分,tb.总课时 from V_规则管理_分层班信息表  ta left join V_规则管理_分层课程信息表 tb on tb.分层课程编号=ta.分层课程编号) j on j.分层班编号=a.相关编号 " +
			"left join V_学生基本数据子类 g on g.学号=a.学号 " +
			"left join V_学校班级数据子类 h on h.行政班代码=g.行政班代码 " +
			"where a.成绩状态 in ('1','2') " +
			//20170703去除异动学生过滤
			//"and g.学生状态 in ('01','05','07','08') " +
			"and c.登分教师编号 like '%@" + teaCode + "@%'";
		
		if(!"all".equalsIgnoreCase(xnxq))
			sql += " and b.学年学期编码 in ('" + xnxq.replaceAll(",", "','") + "') ";
		
		if("all".equalsIgnoreCase(kkb)){
			sqlCondition = "select distinct a.相关编号 from V_成绩管理_登分教师信息表 a " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
					"where a.状态='1' and a.登分教师编号 like '%@" + MyTools.fixSql(teaCode) + "@%'";
			if(!"all".equalsIgnoreCase(xnxq)){
				sqlCondition += " and b.学年学期编码 in ('" + xnxq.replaceAll(",", "','") + "')";
			}
		}else{
			sqlCondition = "'" + kkb.replaceAll(",", "','") + "'";
		}
		sql += " and c.相关编号 in (" + sqlCondition + ") " +
			//"order by c.行政班代码,b.课程代码,班级";
			"order by b.学年学期编码,c.课程代码,c.行政班代码,(case when g.班内学号='' then '9999' else g.班内学号 end)";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//获取文字成绩代码信息
			sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1'";
			wzcjVec = db.GetContextVector(sql);
			
			String xnxqbm = "";//学年学期编码
			String xnxqmc = "";//学年学期名称
			String zymc = "";//专业名称
			String xzbdm = "";//行政班代码
			String xzbmc = "";//行政班名称
			String kcdm = "";//课程代码
			String kcmc = "";//课程名称
			String zks = "";//总课时
			String xf = "";//学分
			String rkjs = "";//任课教师
			String xh = "";//学号
			String xm = "";//姓名
			String bj = "";//班级
			String pscj = "";//平时成绩
			String qzcj = "";//期中成绩
			String sxcj = "";//实训成绩
			String qmcj = "";//期末成绩
			String zpcj = "";//总评成绩
			int num = 1;//序号
			
			//整理数据
			for(int i=0; i<vec.size(); i+=18){
				if(i == 0){
					xnxqbm = MyTools.StrFiltr(vec.get(i));
					xnxqmc = MyTools.StrFiltr(vec.get(i+1));
					zymc = MyTools.StrFiltr(vec.get(i+2));
					xzbdm = MyTools.StrFiltr(vec.get(i+3));
					xzbmc = MyTools.StrFiltr(vec.get(i+4));
					kcdm = MyTools.StrFiltr(vec.get(i+5));
					kcmc = MyTools.StrFiltr(vec.get(i+6));
					zks = MyTools.StrFiltr(vec.get(i+7));
					xf = MyTools.StrFiltr(vec.get(i+8));
					rkjs = MyTools.StrFiltr(vec.get(i+9));
					
					resultVec.add(xnxqmc);
					resultVec.add(zymc);
					resultVec.add(xzbmc);
					resultVec.add(kcmc);
					resultVec.add(zks);
					resultVec.add(xf);
					resultVec.add(rkjs);
				}else{
					//判断是否同一门课程
					if(!xnxqbm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+3))) || !kcdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
						//判断需要预留的行数，如果大于等于40行不添加空行
						int tempNum = 0;
						if(vec.size()/18 < 40){
							tempNum = 40-vec.size()/18;
							if(tempNum > 5){
								tempNum = 5;
							}
							
							for(int a=0; a<tempNum; a++){
								tempVec.add(num);
								num++;
								
								for(int b=0; b<8; b++){
									tempVec.add("");
								}
							}
						}
						resultVec.add(tempVec);
						
						tempVec = new Vector();
						num = 1;
						xnxqbm = MyTools.StrFiltr(vec.get(i));
						xnxqmc = MyTools.StrFiltr(vec.get(i+1));
						zymc = MyTools.StrFiltr(vec.get(i+2));
						xzbdm = MyTools.StrFiltr(vec.get(i+3));
						xzbmc = MyTools.StrFiltr(vec.get(i+4));
						kcdm = MyTools.StrFiltr(vec.get(i+5));
						kcmc = MyTools.StrFiltr(vec.get(i+6));
						zks = MyTools.StrFiltr(vec.get(i+7));
						xf = MyTools.StrFiltr(vec.get(i+8));
						rkjs = MyTools.StrFiltr(vec.get(i+9));
						
						resultVec.add(xnxqmc);
						resultVec.add(zymc);
						resultVec.add(xzbmc);
						resultVec.add(kcmc);
						resultVec.add(zks);
						resultVec.add(xf);
						resultVec.add(rkjs);
					}
				}
				
				xh = MyTools.StrFiltr(vec.get(i+10));
				xm = MyTools.StrFiltr(vec.get(i+11));
				bj = MyTools.StrFiltr(vec.get(i+12));
				pscj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+13)));
				qzcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+14)));
				sxcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+15)));
				qmcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+16)));
				zpcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+17)));
				
				tempVec.add(num);
				tempVec.add(xh);
				tempVec.add(xm);
				tempVec.add(pscj);
				tempVec.add(qzcj);
				tempVec.add(sxcj);
				tempVec.add(qmcj);
				tempVec.add(zpcj);
				tempVec.add(bj);
				num++;
			}
			//判断需要预留的行数，如果大于等于40行不添加空行
			int tempNum = 0;
			if(vec.size()/18 < 40){
				tempNum = 40-vec.size()/18;
				if(tempNum > 5){
					tempNum = 5;
				}
				
				for(int a=0; a<tempNum; a++){
					tempVec.add(num);
					num++;
					
					for(int b=0; b<8; b++){
						tempVec.add("");
					}
				}
			}
			resultVec.add(tempVec);
			
			filePath = request.getSession().getServletContext().getRealPath("/")+"form/registerScoreManage/printView";
			filePath = filePath.replaceAll("\\\\", "/");
			String title = "";
			String[] titleArray = new String[0];
			
			//查询当前课程考试类型
			String examType = "1";
			String sxFlag = "0";
			sql = "select 考试类型,实训 from V_成绩管理_登分设置信息表 where 相关编号='" + MyTools.fixSql(kkb) + "'";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				examType = MyTools.StrFiltr(tempVec.get(0));
				sxFlag = MyTools.StrFiltr(tempVec.get(1));
				
				if("1".equalsIgnoreCase(examType)){
					titleArray = new String[]{"序号","学号","姓名","平时","期中考试","实训","期末考试","学期成绩","所属班级"};
				}else{
					if("0".equalsIgnoreCase(sxFlag)){
						titleArray = new String[]{"序号","学号","姓名","阶段一","阶段二","阶段三","学期成绩","所属班级"};
					}else{
						titleArray = new String[]{"序号","学号","姓名","阶段一","阶段二","阶段三","阶段四","学期成绩","所属班级"};
					}
				}
			}else{
				titleArray = new String[]{"序号","学号","姓名","平时","期中考试","实训","期末考试","学期成绩","所属班级"};
			}
			
			//创建
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			
			filePath += "/" + fileName;
			
			try {
				//输出流
				OutputStream os = new FileOutputStream(filePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
				
				//导出成绩信息
				for(int a=0; a<resultVec.size(); a+=8){
					xnxqmc = MyTools.StrFiltr(resultVec.get(a));
					zymc = MyTools.StrFiltr(resultVec.get(a+1));
					xzbmc = MyTools.StrFiltr(resultVec.get(a+2));
					kcmc = MyTools.StrFiltr(resultVec.get(a+3));
					zks = MyTools.StrFiltr(resultVec.get(a+4));
					xf = MyTools.StrFiltr(resultVec.get(a+5));
					rkjs = MyTools.StrFiltr(resultVec.get(a+6));
					tempVec = (Vector)resultVec.get(a+7);
					
					xnxqmc = xnxqmc.replace(" ", "度").replace("一", "1").replace("二", "2");
					title = schoolName+xnxqmc+"成绩登记表";
					
					this.exportScore("printView", wbook, tempVec, titleArray, title, xzbmc, kcmc, zks, xf, rkjs, examType, sxFlag);
					this.setMSG("文件生成成功");
				}
				
				//写入文件
				wbook.write();
				wbook.close();
				os.close();
			} catch (FileNotFoundException e) {
				this.setMSG("导出前请先关闭相关EXCEL");
			} catch (WriteException e) {
				this.setMSG("文件生成失败");
			} catch (IOException e) {
				this.setMSG("文件生成失败");
			}
			
			//生成预览
//			com.zhuozhengsoft.pageoffice.excelwriter.Workbook wb = new com.zhuozhengsoft.pageoffice.excelwriter.Workbook();
//			Cell cell;
//			Sheet tempSheet;
//			String cellContent = ""; //当前单元格的内容
//			String[] titleArray = new String[]{"序号","学号","姓名","平时","期中考试","实训","期末考试","学期成绩"};
//			final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
//			
//			//遍历所有成绩信息
//			for(int i=0; i<resultVec.size(); i+=8){
//				xnxqmc = MyTools.StrFiltr(resultVec.get(i));
//				xnxqmc = xnxqmc.replace(" ", "度").replace("一", "1").replace("二", "2");
//				zymc = MyTools.StrFiltr(resultVec.get(i+1));
//				xzbmc = MyTools.StrFiltr(resultVec.get(i+2));
//				kcmc = MyTools.StrFiltr(resultVec.get(i+3));
//				zks = MyTools.StrFiltr(resultVec.get(i+4));
//				xf = MyTools.StrFiltr(resultVec.get(i+5));
//				rkjs = MyTools.StrFiltr(resultVec.get(i+6));
//				tempVec = (Vector)resultVec.get(i+7);
//				
//				if(i == 0){
//					tempSheet = wb.openSheet("Sheet1");
//				}else{
//					tempSheet = wb.createSheet(xzbmc+kcmc, SheetInsertType.After, "Sheet1");
//				}
//				
//				//生成标题信息
//				tempSheet.openTable("A1:"+colName[titleArray.length-1]+"1").merge();
//				cell = tempSheet.openCell("A1");
//				cellContent = schoolName+xnxqmc+"成绩登记表";
//				cell.setValue(cellContent);
//				
//				tempSheet.openTable(colName[0]+"2:"+colName[4]+"2").merge();
//				cell = tempSheet.openCell("A2");
//				cellContent = "学科：" + kcmc;
//				cell.setValue(cellContent);
//				
//				tempSheet.openTable(colName[4]+"2:"+colName[5]+"2").merge();
//				cell = tempSheet.openCell("E2");
//				cellContent = "总学时：" + zks;
//				cell.setValue(cellContent);
//				
//				tempSheet.openTable(colName[6]+"2:"+colName[7]+"2").merge();
//				cell = tempSheet.openCell("G2");
//				cellContent = "学分：" + xf;
//				cell.setValue(cellContent);
//				
//				tempSheet.openTable(colName[0]+"3:"+colName[4]+"3").merge();
//				cell = tempSheet.openCell("A3");
//				cellContent = "上课班级：" + xzbmc;
//				cell.setValue(cellContent);
//				
//				tempSheet.openTable(colName[5]+"3:"+colName[7]+"3").merge();
//				cell = tempSheet.openCell("F3");
//				cellContent = "任课教师：" + rkjs;
//				cell.setValue(cellContent);
//				
//				//表格标题栏
//				for(int a=1; a<titleArray.length+1; a++){
//					cell = tempSheet.openCell(colName[a-1]+"4");
//					cell.setValue(titleArray[a-1]);
//				}
//				//表格内容
//				for(int a=1; a<titleArray.length+1; a++){
//					for(int b=5; b<tempVec.size()/8+6; b++){
//						cell = tempSheet.openCell(colName[a-1]+b);//当前单元格
//						cell.setValue("");
//					}
//				}
//				
//				int colNum = 1;
//				int rowNum = 5;
//				
//				for(int a=0; a<tempVec.size(); a++){
//					cell = tempSheet.openCell(colName[colNum-1]+rowNum);//当前单元格
//					cell.setValue(MyTools.StrFiltr(tempVec.get(a)));
//					
//					colNum++;
//					if(colNum > titleArray.length){
//						colNum = 1;
//						rowNum++;
//					}
//				}
//				
//				tempSheet.openTable(colName[5]+(rowNum+1)+":"+colName[titleArray.length-1]+(rowNum+1)).merge();
//				cell = tempSheet.openCell(colName[5]+(rowNum+1));
//				cellContent = "阅卷教师签名：";
//				cell.setValue(cellContent);
//				
//				//设置单元格的水平对齐方式
//				tempSheet.openTable(colName[0]+"1:"+colName[titleArray.length-1]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
//				tempSheet.openTable(colName[0]+"2:"+colName[titleArray.length-1]+"3").setHorizontalAlignment(XlHAlign.xlHAlignLeft);
//				tempSheet.openTable(colName[0]+"4:"+colName[titleArray.length-1]+(tempVec.size()/8+5)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
//				tempSheet.openTable(colName[5]+rowNum+":"+colName[7]+rowNum).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
//				
//				//设置单元格的垂直对齐方式
//				//tempSheet.openTable(colName[0]+"1:"+colName[titleArray.length-1]+(tempVec.size()/8+6)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
//				
//				//设置课表边框线
//				Border border = tempSheet.openTable(colName[0]+"4:"+colName[titleArray.length-1]+(tempVec.size()/8+4)).getBorder();
//				//设置表格边框的宽度、颜色
//				border.setWeight(XlBorderWeight.xlThin);
//				border.setLineColor(Color.black);
//				
//				//设置标题字体大小
//				cell = tempSheet.openCell("A1");
//				cell.getFont().setBold(true);
//				cell.getFont().setSize(18);
//				
//				//设置表格内容字体大小
//				tempSheet.openTable(colName[0]+"2:"+colName[titleArray.length-1]+"4").getFont().setBold(true);
//				tempSheet.openTable(colName[0]+"2:"+colName[titleArray.length-1]+(tempVec.size()/8+6)).getFont().setSize(12);
//				
//				//设置表格列宽
//				tempSheet.openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(6);
//				tempSheet.openTable(colName[1]+"1:"+colName[2]+"1").setColumnWidth(12);
//				tempSheet.openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(8);
//				tempSheet.openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(10);
//				tempSheet.openTable(colName[5]+"1:"+colName[5]+"1").setColumnWidth(8);
//				tempSheet.openTable(colName[6]+"1:"+colName[7]+"1").setColumnWidth(10);
//				
//				//设置表格行高
//				tempSheet.openTable(colName[0]+"1:"+colName[0]+"1").setRowHeight(22);
//				tempSheet.openTable(colName[0]+"2:"+colName[0]+"3").setRowHeight(19);
//				tempSheet.openTable(colName[0]+"4:"+colName[0]+(tempVec.size()+6)).setRowHeight(16);
//			}
		}
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		//poCtrl1.setWriter(wb);
		poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
		poCtrl1.setJsFunction_AfterDocumentClosed("AfterDocumentClosed()");
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		//String fileName = "template.xls";
		
		//创建自定义菜单栏
		//poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印并锁定", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("关闭", "closeWindow()", 4);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "成绩登记表");
		poCtrl1.setFileTitle(schoolName + "成绩登记表");//设置另存为窗口默认文件名
		
		//打开文件
		poCtrl1.webOpen("printView/"+fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
		return filePath;
	}
	
	/**
	 * 导出学生成绩汇总
	 * @date:2016-03-29
	 * @author:yeq
	 * @param startSemCode 起始学年学期编码
	 * @param endSemCode 结束学年学期编码
	 * @param deptCode 系部代码
	 * @param gradeCode 年级代码
	 * @param majorCode 专业代码
	 * @param classCode 班级代码
	 * @param cjfw 成绩范围
	 * @param zbwdfFlag 整班未登分判断
	 * @param cjlx 成绩类型
	 * @param zslb 招生类别
	 * @param xkmc 学科名称
	 * @return String
	 */
	public String hzcjExport(String startSemCode, String endSemCode, String deptCode, String gradeCode, String majorCode, String classCode, String cjfw, String zbwdfFlag, String cjlx, String zslb, String xkmc) throws SQLException{
		String sql = "";
		Vector vec = null;
		String savePath = "";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		sql = "select 学籍号,班内学号,姓名,身份证件号,年级名称,行政班名称,学年学期编码,课程代码,课程名称,学分,";
//			"case when 平时='' then '0' else 平时 end as 平时,case when 期中='' then '0' else 期中 end as 期中," +
//			"case when 实训='' then '0' else 实训 end as 实训,case when 期末='' then '0' else 期末 end as 期末," +
//			"case when 总评='' then '0' else 总评 end as 总评,case when 重修1='' then '0' else 重修1 end as 重修1," +
//			"case when 重修2='' then '0' else 重修2 end as 重修2,case when 补考='' then '0' else 补考 end as 补考," +
//			"case when 大补考='' then '0' else 大补考 end as 大补考 " +
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("ps")>-1) sql += "平时,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("qz")>-1) sql += "期中,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("sx")>-1) sql += "实训,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("qm")>-1) sql += "期末,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("zp")>-1) sql += "总评,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("cx1")>-1) sql += "重修1,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("cx2")>-1) sql += "重修2,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("bk")>-1) sql += "补考,";
		if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("dbk")>-1) sql += "大补考,";
		sql = sql.substring(0, sql.length()-1);
		sql += " from (" +
			"select c.学籍号,c.班内学号,a.姓名,c.身份证件号,k.类别名称 as 招生类别,z.课程代码,z.课程名称," +
			"case z.来源类型 when '2' then cast(cast(i.学分 as float) as varchar) " +
			"when '3' then (case when g.学分 is null then '' else cast(cast(g.学分 as float) as varchar) end) " +
			"when '4' then (case when j.学分 is null then '' else cast(cast(j.学分 as float) as varchar) end) " +
			"else (case when f.学分 is null then '' else cast(cast(f.学分 as float) as varchar) end) end as 学分," +
			"substring(b.学年学期编码,1,5) as 学年学期编码,m.系部代码,m.系部名称,e.年级代码,e.年级名称," +
			"case when z.来源类型='4' then j.分层班编号 else d.行政班代码 end as 行政班代码," +
			"case when z.来源类型='4' then j.分层班名称 else d.行政班名称 end as 行政班名称," +
			"a.平时,a.期中,a.实训,a.期末,a.总评,a.重修1,a.重修2,a.补考,a.大补考," +
			"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 z on z.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_学生基本数据子类 c on c.学号=a.学号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
			"left join V_学校年级数据子类 e on e.年级代码=d.年级代码 " +
			"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号 " +
			"left join V_规则管理_选修课授课计划明细表 g on g.授课计划明细编号=a.相关编号 " +
			"left join V_排课管理_添加课程信息表 i on i.编号=a.相关编号 " +
			"left join (select ta.分层班编号,ta.分层班名称,tb.学分 from V_规则管理_分层班信息表 ta left join V_规则管理_分层课程信息表 tb on tb.分层课程编号=ta.分层课程编号) j on j.分层班编号=a.相关编号 " +
			"left join V_信息类别_类别操作 k on k.描述=c.招生类别 and k.父类别代码='ZSLBM' " +
			"left join V_基础信息_系部信息表 m on m.系部代码=d.系部代码 " +
			"where a.成绩状态 in ('1','2') ";
			//20170703去除异动学生过滤
			//"and c.学生状态 in ('01','05','07','08')";
		
		if(!"all".equalsIgnoreCase(startSemCode))
			sql += " and cast(b.学年学期编码 as int)>=" + startSemCode;
		if(!"all".equalsIgnoreCase(endSemCode))
			sql += " and cast(b.学年学期编码 as int)<=" + endSemCode;
		if(!"all".equalsIgnoreCase(deptCode))
			sql += " and m.系部代码 in ('" + deptCode.replaceAll(",", "','") + "')";
		if(!"all".equalsIgnoreCase(gradeCode))
			sql += " and d.年级代码 in ('" + gradeCode.replaceAll(",", "','") + "')";
		if(!"all".equalsIgnoreCase(majorCode))
			sql += " and d.专业代码 in ('" + majorCode.replaceAll(",", "','") + "')";
		if(!"all".equalsIgnoreCase(classCode))
			sql += " and d.行政班代码 in ('" + classCode.replaceAll(",", "','") + "')";
		//判断是否需要过滤整班未登分成绩信息
		if("exclude".equalsIgnoreCase(zbwdfFlag)){
			sql += " and a.相关编号 not in (select distinct 相关编号 from (select b.相关编号," +
				"case when b.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=b.相关编号) " +
				"when b.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=b.相关编号) " +
				"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.行政班代码) end as 需登分人数," +
				"(select count(*) from V_成绩管理_学生成绩信息表 " +
				"where 相关编号=a.相关编号 and ((case when 平时 in ('-1','-5','-17') then '' else 平时 end)<>'' " +
				"or (case when 期中 in ('-1','-5','-17') then '' else 期中 end)<>'' " +
				"or (case when 实训 in ('-1','-5','-17') then '' else 实训 end)<>'' " +
				"or (case when 期末 in ('-1','-5','-17') then '' else 期末 end)<>'' " +
				"or (case when 总评 in ('-1','-5','-17') then '' else 总评 end)<>'' " +
				"or (case when 重修1 in ('-1','-5','-17') then '' else 重修1 end)<>'' " +
				"or (case when 重修2 in ('-1','-5','-17') then '' else 重修2 end)<>'' " +
				"or (case when 补考 in ('-1','-5','-17') then '' else 补考 end)<>'' " +
				"or (case when 大补考 in ('-1','-5','-17') then '' else 补考 end)<>'')) as 已登分人数 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态='1'";
			if(!"all".equalsIgnoreCase(startSemCode))
				sql += " and cast(c.学年学期编码 as int)>=" + startSemCode;
			if(!"all".equalsIgnoreCase(endSemCode))
				sql += " and cast(c.学年学期编码 as int)<=" + endSemCode;
			sql += ") as t where 已登分人数=0)";
		}
		//招生类别
		if(!"all".equalsIgnoreCase(zslb))
			sql += " and c.招生类别 in ('" + zslb.replaceAll(",", "','") + "')";
		//学科名称
		if(!"all".equalsIgnoreCase(xkmc))
			sql += " and b.课程代码 in ('" + xkmc.replaceAll(",", "','") + "')";
		sql += ") as z";
		
		if("bjg".equalsIgnoreCase(cjfw))
			sql += " where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') ";
		
		sql += " order by 年级代码,行政班代码,学年学期编码,课程代码,(case when 班内学号='' then '9999' else 班内学号 end)";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//获取文字成绩代码信息
			sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1'";
			Vector wzcjVec = db.GetContextVector(sql);
			
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			
			savePath = MyTools.getProp(request, "Base.exportExcelPath")+"scoreExport";
			String title = "";
			//String[] titleArray = new String[]{"学号","姓名","课程代码","课程名称","学分","学期","年级","班级","平时成绩","期中成绩","实训成绩","期末成绩","总评成绩","重修成绩1","重修成绩2","补考","大补考"};
			Vector titleVec = new Vector();
			titleVec.add("学籍号");
			titleVec.add("班内学号");
			titleVec.add("姓名");
			titleVec.add("身份证号");
			titleVec.add("年级");
			titleVec.add("班级");
			titleVec.add("学期");
			titleVec.add("课程代码");
			titleVec.add("课程名称");
			titleVec.add("学分");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("ps")>-1) titleVec.add("平时成绩");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("qz")>-1) titleVec.add("期中成绩");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("sx")>-1) titleVec.add("实训成绩");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("qm")>-1) titleVec.add("期末成绩");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("zp")>-1) titleVec.add("总评成绩");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("cx1")>-1) titleVec.add("重修成绩1");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("cx2")>-1) titleVec.add("重修成绩2");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("bk")>-1) titleVec.add("补考");
			if("all".equalsIgnoreCase(cjlx) || cjlx.indexOf("dbk")>-1) titleVec.add("大补考");
			title = schoolName + " 学生成绩汇总信息";
			
			try {
				//创建
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				
				//输出流
				savePath += "/学生成绩汇总信息_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);//工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				String curContent = "";
				
				//设置列宽
				wsheet.setColumnView(0, 25);
				wsheet.setColumnView(1, 15);
				wsheet.setColumnView(2, 15);
				wsheet.setColumnView(3, 25);
				wsheet.setColumnView(4, 15);
				wsheet.setColumnView(5, 20);
				wsheet.setColumnView(6, 15);
				wsheet.setColumnView(7, 15);
				wsheet.setColumnView(8, 30);
				wsheet.setColumnView(9, 15);
				for(int i=10; i<titleVec.size(); i++){
					wsheet.setColumnView(i, 15);
				}
				//设置行高
				wsheet.setRowView(1, 400);
				
				//标题
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet.mergeCells(0, 0, titleVec.size()-1, 0);
				content = new Label(0, 0, title, contentStyle);
				wsheet.addCell(content);
				
				//表格标题行
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				
				for (int i=0; i<titleVec.size(); i++) {
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					contentStyle.setWrap(true);// 自动换行
					
					if(i == 0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(i == titleVec.size()-1){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}
					content = new Label(i, 1, MyTools.StrFiltr(titleVec.get(i)), contentStyle);
					wsheet.addCell(content);
				}
				
				//表格内容
				//k:用于循环时Excel的行号
				//外层for用于循环行,内曾for用于循环列
				for (int i=0, k=2; i<vec.size(); i+=titleVec.size(), k++) {
					for (int j=0; j<titleVec.size(); j++) {
						if(j > 8){
							curContent = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+j)));
						}else{
							curContent = MyTools.StrFiltr(vec.get(i+j));
						}
						
						if(j>9 && !"".equalsIgnoreCase(curContent) && ((MyTools.StringToInt(curContent)>0&&MyTools.StringToInt(curContent)<60) 
								|| "作弊".equalsIgnoreCase(curContent) || "取消资格".equalsIgnoreCase(curContent) || "缺考".equalsIgnoreCase(curContent) || "不及格".equalsIgnoreCase(curContent) || "不合格".equalsIgnoreCase(curContent))){
							fontStyle = new WritableFont(
									WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
									false, jxl.format.UnderlineStyle.NO_UNDERLINE,
									jxl.format.Colour.RED);
						}else{
							fontStyle = new WritableFont(
									WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
									false, jxl.format.UnderlineStyle.NO_UNDERLINE,
									jxl.format.Colour.BLACK);
						}
						
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
						//contentStyle.setWrap(true);// 自动换行
						contentStyle.setShrinkToFit(true);//字体大小自适应
						
						if(j == 0){
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						}else if(j == titleVec.size()-1){
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						}
						
						if(k == (vec.size()/titleVec.size()+1)){
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}
						
						content = new Label(j, k, curContent, contentStyle);
						wsheet.addCell(content);
					}
				}
				
				//写入文件
				wbook.write();
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
		
			this.setMSG("成绩文件生成成功");
		}else{
			this.setMSG("没有符合条件的成绩信息");
		}
		
		return savePath;
	}
	
	/**
	 * 保存查分时间
	 * @date:2016-07-14
	 * @author:yeq
	 * @param kfxnxq 开放学年学期
	 * @param kfcjlx 开放成绩类型
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @throws SQLException
	 */
	public void saveConfig(String kfxnxq, String kfcjlx, String beginDate, String endDate) throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		String syncTargetSysName = MyTools.getProp(request, "Base.syncTargetSysName");
		
		if(!"all".equalsIgnoreCase(kfcjlx)){
			//排序
			String scoreType[] = {"zp","cx1","cx2","bk","dbk"};
			String cjlxArray[] = kfcjlx.split(",", -1);
			kfcjlx = "";
			
			for(int i=0; i<scoreType.length; i++){
				for(int j=0; j<cjlxArray.length; j++){
					if(scoreType[i].equalsIgnoreCase(cjlxArray[j])){
						kfcjlx += scoreType[i]+",";
						break;
					}
				}
			}
			kfcjlx = kfcjlx.substring(0, kfcjlx.length()-1);
		}
		
		sql = "select count(*) from V_成绩管理_查分设置信息表";
		if(db.getResultFromDB(sql)){
			sql = "update V_成绩管理_查分设置信息表 set " +
				"开放学年学期='" + MyTools.fixSql(kfxnxq) + "'," +
				"开放成绩类型='" + MyTools.fixSql(kfcjlx) + "'," +
				"开始时间='" + MyTools.fixSql(beginDate) + "'," +
				"结束时间='" + MyTools.fixSql(endDate) + "'," +
				"创建人='" + MyTools.fixSql(this.getUSERCODE())  +"'," +
				"创建时间=getDate()";
		}else{
			sql = "insert into V_成绩管理_查分设置信息表(开放学年学期,开放成绩类型,开始时间,结束时间,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(kfxnxq) + "'," +
				"'" + MyTools.fixSql(kfcjlx) + "'," +
				"'" + MyTools.fixSql(beginDate) + "'," +
				"'" + MyTools.fixSql(endDate) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}
		sqlVec.add(sql);
		
		//同步查分设置
		String linkStr = MyTools.getProp(request, "Base.linkInfo");
		String sysName = MyTools.getProp(request, "Base.sysName");
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_CFSZXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_CFSZXX] select * from [" + sysName + "].[dbo].[CJGL_CFSZXX]";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 读取学年学期列表数据
	 * @date:2016-07-21
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queXnxqList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select 学年学期编码,学年学期名称 from V_规则管理_学年学期表 where 1=1";
		
		if(!"".equalsIgnoreCase(this.getXNXQ())){
			sql += " and 学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "'";
		}
		sql += " order by 学年学期编码 desc";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取学年列表数据
	 * @date:2016-10-13
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queXnList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select distinct 学年,学年+'学年' as 学年名称 from V_规则管理_学年学期表 where 1=1";
		
		if(!"".equalsIgnoreCase(this.getXNXQ())){
			sql += " and 学年='" + MyTools.fixSql(this.getXNXQ()) + "'";
		}
		sql += " order by 学年 desc";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取补考信息列表数据
	 * @date:2016-07-21
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param zbwdfFlag 整班未登分判断
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queBkInfoList(int pageNum, int pageSize, String zbwdfFlag) throws SQLException {
		Vector vec = null;
		String sql = "select * from (" +
				"select a.学号,a.姓名,case when e.来源类型='4' then e.行政班名称 else d.行政班名称 end as 行政班名称," +
				"case when c.课程类型='3' then c.课程名称+'（选修）' else c.课程名称 end as 课程名称," +
				"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
				"left join V_成绩管理_登分教师信息表 e on e.相关编号=a.相关编号 " +
				"where a.状态='1' and b.学生状态 in ('01','05','07','08') and a.成绩状态='1' " +
				"and c.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "'";
		//判断是否需要过滤整班未登分成绩信息
		if("exclude".equalsIgnoreCase(zbwdfFlag)){
			sql += " and a.相关编号 not in (select distinct 相关编号 from (select b.相关编号," +
				"case when b.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=b.相关编号) " +
				"when b.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=b.相关编号) " +
				"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.行政班代码) end as 需登分人数," +
				"(select count(*) from V_成绩管理_学生成绩信息表 " +
				"where 相关编号=a.相关编号 and ((case when 平时 in ('-1','-5') then '' else 平时 end)<>'' " +
				"or (case when 期中 in ('-1','-5') then '' else 期中 end)<>'' " +
				"or (case when 实训 in ('-1','-5') then '' else 实训 end)<>'' " +
				"or (case when 期末 in ('-1','-5') then '' else 期末 end)<>'' " +
				"or (case when 总评 in ('-1','-5') then '' else 总评 end)<>'' " +
				"or (case when 重修1 in ('-1','-5') then '' else 重修1 end)<>'' " +
				"or (case when 重修2 in ('-1','-5') then '' else 重修2 end)<>'' " +
				"or (case when 补考 in ('-1','-5') then '' else 补考 end)<>'' " +
				"or (case when 大补考 in ('-1','-5') then '' else 补考 end)<>'')) as 已登分人数 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态='1' and c.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "') as t " +
				"where 已登分人数=0)";
		}
		sql += ") as z where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-2','-6','-7','-8','-9','-11','-13','-15')";
		
		if(!"".equalsIgnoreCase(this.getSTUCODE())){
			sql += " and 学号 like '%" + MyTools.fixSql(this.getSTUCODE()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getSTUNAME())){
			sql += " and 姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getBJMC())){
			sql += " and 行政班名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getKCMC())){
			sql += " and 课程名称 like '%" + MyTools.fixSql(this.getKCMC()) + "%'";
		}
		sql += " order by 课程名称,学号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 补考名册导出
	 * @date:2016-07-14
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public String bkmdExport(String zbwdfFlag) throws SQLException{
		String filepath="";
		Vector vec = null;
		Vector wzcjVec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String savePath = MyTools.getProp(request, "Base.exportExcelPath")+"resitExport";
		String filePath=savePath+"/"+this.getXNXQ().substring(0, 4)+"学年第"+this.getXNXQ().substring(4, 5)+"学期"+"补考考试名册.xls";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		//创建
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		OutputStream os;
		WritableWorkbook wbook;
		try {
			os = new FileOutputStream(filePath);
			wbook = Workbook.createWorkbook(os);
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			String[] title=new String[]{"编号","学号","姓名","班级","学科","学分","补考分数","考生签名"};
			int stunum=1;//补考学生行数
			int stuxlh=1;//补考学生序号
			int counum=0;//excel表中行数
			String cellContent = ""; //当前单元格的内容
			
			String xn="";
			String xq="";
			String xnxq=this.getXNXQ();
			if(xnxq.substring(0, 1).equals("0")){
				xn+="○";
			}else if(xnxq.substring(0, 1).equals("1")){
				xn+="一";
			}else if(xnxq.substring(0, 1).equals("2")){
				xn+="二";
			}else if(xnxq.substring(0, 1).equals("3")){
				xn+="三";
			}else if(xnxq.substring(0, 1).equals("4")){
				xn+="四";
			}else if(xnxq.substring(0, 1).equals("5")){
				xn+="五";
			}else if(xnxq.substring(0, 1).equals("6")){
				xn+="六";
			}else if(xnxq.substring(0, 1).equals("7")){
				xn+="七";
			}else if(xnxq.substring(0, 1).equals("8")){
				xn+="八";
			}else if(xnxq.substring(0, 1).equals("9")){
				xn+="九";
			}
			
			if(xnxq.substring(1, 2).equals("0")){
				xn+="○";
			}else if(xnxq.substring(1, 2).equals("1")){
				xn+="一";
			}else if(xnxq.substring(1, 2).equals("2")){
				xn+="二";
			}else if(xnxq.substring(1, 2).equals("3")){
				xn+="三";
			}else if(xnxq.substring(1, 2).equals("4")){
				xn+="四";
			}else if(xnxq.substring(1, 2).equals("5")){
				xn+="五";
			}else if(xnxq.substring(1, 2).equals("6")){
				xn+="六";
			}else if(xnxq.substring(1, 2).equals("7")){
				xn+="七";
			}else if(xnxq.substring(1, 2).equals("8")){
				xn+="八";
			}else if(xnxq.substring(1, 2).equals("9")){
				xn+="九";
			}
			
			if(xnxq.substring(2, 3).equals("0")){
				xn+="○";
			}else if(xnxq.substring(2, 3).equals("1")){
				xn+="一";
			}else if(xnxq.substring(2, 3).equals("2")){
				xn+="二";
			}else if(xnxq.substring(2, 3).equals("3")){
				xn+="三";
			}else if(xnxq.substring(2, 3).equals("4")){
				xn+="四";
			}else if(xnxq.substring(2, 3).equals("5")){
				xn+="五";
			}else if(xnxq.substring(2, 3).equals("6")){
				xn+="六";
			}else if(xnxq.substring(2, 3).equals("7")){
				xn+="七";
			}else if(xnxq.substring(2, 3).equals("8")){
				xn+="八";
			}else if(xnxq.substring(2, 3).equals("9")){
				xn+="九";
			}
			
			if(xnxq.substring(3, 4).equals("0")){
				xn+="○";
			}else if(xnxq.substring(3, 4).equals("1")){
				xn+="一";
			}else if(xnxq.substring(3, 4).equals("2")){
				xn+="二";
			}else if(xnxq.substring(3, 4).equals("3")){
				xn+="三";
			}else if(xnxq.substring(3, 4).equals("4")){
				xn+="四";
			}else if(xnxq.substring(3, 4).equals("5")){
				xn+="五";
			}else if(xnxq.substring(3, 4).equals("6")){
				xn+="六";
			}else if(xnxq.substring(3, 4).equals("7")){
				xn+="七";
			}else if(xnxq.substring(3, 4).equals("8")){
				xn+="八";
			}else if(xnxq.substring(3, 4).equals("9")){
				xn+="九";
			}
			
			if(xnxq.substring(4, 5).equals("0")){
				xq+="○";
			}else if(xnxq.substring(4, 5).equals("1")){
				xq+="一";
			}else if(xnxq.substring(4, 5).equals("2")){
				xq+="二";
			}
			
//			sql=" select a.学号,a.姓名,e.行政班简称,b.课程名称,case when right(cast(b.学分 as varchar),2)='.0' " +
//					"then substring(cast(b.学分 as varchar),1,len(b.学分)-2) else cast(b.学分 as varchar) end as 学分,'' as 补考分数,'' as 学生签名 " +
//					"from V_成绩管理_学生成绩信息表 a,V_规则管理_授课计划明细表 b,V_规则管理_授课计划主表 c,V_学生基本数据子类 d,V_学校班级数据子类 e " +
//					"where a.相关编号=b.授课计划明细编号 and b.授课计划主表编号=c.授课计划主表编号 and a.学号=d.学号 and d.行政班代码=e.行政班代码 " +
//					"and (a.总评<60 and a.总评<>'' and a.总评 not in ('-1','-6','-7','-8','-9','-11','-13','-15'))  and c.学年学期编码='"+MyTools.fixSql(this.getXNXQ())+"' " +
//					"and d.学生状态 in ('01','05','07','08') order by b.课程名称,e.行政班简称 ";
			sql = "select distinct 学号,姓名,行政班简称,课程名称,学分," +
				//"case when right(cast(学分 as varchar),2)='.0' then substring(cast(学分 as varchar),1,len(学分)-2) else cast(学分 as varchar) end as 学分," +
				"'' as 补考分数,'' as 学生签名 from (select a.编号,a.学号,a.姓名,e.行政班简称," +
				"case b.来源类型 when '4' then b.行政班名称+'-'+b.课程名称 when '3' then b.课程名称+'（选修）' else b.课程名称 end as 课程名称," +
				"case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) " +
				"when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) " +
				"when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) " +
				"when '4' then (select t1.学分 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号 where t.分层班编号=b.相关编号) " +
				"else 0 end as 学分," +
				"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
				"left join V_学生基本数据子类 d on d.学号=a.学号 " +
				"left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 " +
				"where a.状态='1' and c.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "' " +
				"and d.学生状态 in ('01','05','07','08') and 成绩状态='1'";
			if(!"all".equalsIgnoreCase(this.getNJDM()))
				sql += " and e.年级代码 in ('" + this.getNJDM().replaceAll(",", "','") + "')";
			if(!"all".equalsIgnoreCase(this.getZYMC()))
				sql += " and e.专业代码 in ('" + this.getZYMC().replaceAll(",", "','") + "')";
			if(!"all".equalsIgnoreCase(this.getBJMC()))
				sql += " and e.行政班代码 in ('" + this.getBJMC().replaceAll(",", "','") + "')";
			//判断是否需要过滤整班未登分成绩信息
			if("exclude".equalsIgnoreCase(zbwdfFlag)){
				sql += " and a.相关编号 not in (select distinct 相关编号 from (select b.相关编号," +
					"case when b.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=b.相关编号) " +
					"when b.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=b.相关编号) " +
					"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.行政班代码) end as 需登分人数," +
					"(select count(*) from V_成绩管理_学生成绩信息表 " +
					"where 相关编号=a.相关编号 and ((case when 平时 in ('-1','-5') then '' else 平时 end)<>'' " +
					"or (case when 期中 in ('-1','-5') then '' else 期中 end)<>'' " +
					"or (case when 实训 in ('-1','-5') then '' else 实训 end)<>'' " +
					"or (case when 期末 in ('-1','-5') then '' else 期末 end)<>'' " +
					"or (case when 总评 in ('-1','-5') then '' else 总评 end)<>'' " +
					"or (case when 重修1 in ('-1','-5') then '' else 重修1 end)<>'' " +
					"or (case when 重修2 in ('-1','-5') then '' else 重修2 end)<>'' " +
					"or (case when 补考 in ('-1','-5') then '' else 补考 end)<>'' " +
					"or (case when 大补考 in ('-1','-5') then '' else 补考 end)<>'')) as 已登分人数 " +
					"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
					"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态='1' and c.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "') as t " +
					"where 已登分人数=0)";
			}
			sql += ") as z where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-2','-6','-7','-8','-9','-11','-13','-15') " +
				"order by 课程名称,行政班简称,学号";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				//设置列宽
				wsheet.setColumnView(0, 4);
				wsheet.setColumnView(1, 11);
				wsheet.setColumnView(2, 10);
				wsheet.setColumnView(3, 10);
				wsheet.setColumnView(4, 30);
				wsheet.setColumnView(5, 6);
				wsheet.setColumnView(6, 6);
				wsheet.setColumnView(7, 11);

				for(int i=0; i<vec.size(); i+=7){
					if(i < vec.size()){
						if(i != 0){
							//判断课程名称是否相同，或者同层级分层班
							if(MyTools.StrFiltr(vec.get(i+3)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+3-7))) 
								|| (this.parseFcbName(MyTools.StrFiltr(vec.get(i+3))).equalsIgnoreCase(this.parseFcbName(MyTools.StrFiltr(vec.get(i+3-7)))) 
								&& this.parseFcbCourse(MyTools.StrFiltr(vec.get(i+3))).equalsIgnoreCase(this.parseFcbCourse(MyTools.StrFiltr(vec.get(i+3-7)))))){
								//一门课补考人数>30,添加到下一页
								if(stunum>30){
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									//31行
									wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
									cellContent="阅卷教师签名 ：__________";
									content = new Label(3, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
									//32行
									wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
									cellContent="阅 卷 日 期  ：__________";
									content = new Label(3, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
									//33行
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									wsheet.mergeCells(1, counum+stunum, 6, counum+stunum);
									cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
									content = new Label(1, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
									counum=counum+36-3;	
									stunum=1;	
									//生成标题
									//第1行
									counum++;
									//设置课表标题行列字体大小
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									wsheet.mergeCells(0, counum, 7, counum);
									cellContent = schoolName+xn+"学年度第"+xq+"学期";
									content = new Label(0, counum, cellContent, contentStyle);
									wsheet.addCell(content);
																	
									//第2行
									counum++;
									wsheet.mergeCells(0, counum, 7, counum);
									cellContent="补考考试名册";
									content = new Label(0, counum, cellContent, contentStyle);
									wsheet.addCell(content);
								
									//第3行
									counum++;
									
									for(int colNum=0; colNum<8; colNum++){
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setWrap(true);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										//边框
										if(colNum==0){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else if(colNum==7){
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
											
									for(int colNum=0; colNum<8; colNum++){
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setShrinkToFit(true);//字体大小自适应
										
										if(colNum==0){
											cellContent=stuxlh+"";
										}else{
											cellContent=vec.get(i+colNum-1).toString();
											
											if(colNum==5){
												cellContent = this.parseXf(cellContent);
											}
										}
										//边框
										if(colNum==0){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else if(colNum==7){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else{
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);	
													
									}
									wsheet.setRowView((counum+stunum), 380);
									stunum++;
									stuxlh++;
																			
									//设置表格行高
									wsheet.setRowView((counum-2), 460);
									wsheet.setRowView((counum-1), 440);
									wsheet.setRowView(counum, 620);

									wsheet.setRowView((counum+31), 420);
									wsheet.setRowView((counum+32), 300);
									wsheet.setRowView((counum+33), 420);
								}else{
									//添加下一行
									for(int colNum=0; colNum<8; colNum++){
										//设置课表标题行列字体大小
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setShrinkToFit(true);//字体大小自适应
											
										if(colNum==0){
											cellContent=stuxlh+"";
											
										}else{
											cellContent=vec.get(i+colNum-1).toString();
											
											if(colNum==5){
												cellContent = this.parseXf(cellContent);
											}
										}
										//边框
										if(colNum==0){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											if(stunum == 30){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}else if(colNum==7){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											if(stunum == 30){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}else{
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											if(stunum == 30){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}
									wsheet.setRowView((counum+stunum), 380);
									stunum++;
									stuxlh++;
								}
								if(i==vec.size()-7){//最后一条
									do{	
										for(int colNum=0; colNum<8; colNum++){
											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
											contentStyle = new WritableCellFormat(fontStyle);
											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											cellContent = "";
											
											//边框
											if(colNum==0){
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum==30){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else if(colNum==7){
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
												if(stunum==30){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else{
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum==30){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}
											
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
										
										wsheet.setRowView((counum+stunum), 380);
										stunum++;
										stuxlh++;	
									}while(stunum<31);
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									//31行
									wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
									cellContent="阅卷教师签名 ：__________";
									content = new Label(3, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
									//32行
									wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
									cellContent="阅 卷 日 期  ：__________";
									content = new Label(3, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
									//33行
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									wsheet.mergeCells(1, counum+stunum, 6, counum+stunum);
									cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
									content = new Label(1, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
								}
								//设置表格行高
								wsheet.setRowView((counum-2), 460);
								wsheet.setRowView((counum-1), 440);
								wsheet.setRowView(counum, 620);
								
								wsheet.setRowView((counum+31), 420);
								wsheet.setRowView((counum+32), 300);
								wsheet.setRowView((counum+33), 420);
							}else{			
								while(stunum<31){
									for(int colNum=0; colNum<8; colNum++){
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										cellContent = "";
										//边框
										if(colNum==0){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											if(stunum==30){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}else if(colNum==7){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											if(stunum==30){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}else{
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											if(stunum==30){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}
										
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}
									
									wsheet.setRowView((counum+stunum), 380);
									stunum++;
									stuxlh++;
								};
								
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
								//31行
								wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
								cellContent="阅卷教师签名 ：__________";
								content = new Label(3, counum+stunum, cellContent, contentStyle);
								wsheet.addCell(content);
								stunum++;
								
								//32行
								wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
								cellContent="阅 卷 日 期  ：__________";
								content = new Label(3, counum+stunum, cellContent, contentStyle);
								wsheet.addCell(content);
								stunum++;
								
								//33行
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								wsheet.mergeCells(1, counum+stunum, 6, counum+stunum);
								cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
								content = new Label(1, counum+stunum, cellContent, contentStyle);
								wsheet.addCell(content);
								stunum++;
								
								counum=counum+36-3;	
								stunum=1;stuxlh=1;	
								//生成标题
								//第1行
								counum++;
								//设置课表标题行列字体大小
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
								wsheet.mergeCells(0, counum, 7, counum);
								cellContent = schoolName+xn+"学年度第"+xq+"学期";
								content = new Label(0, counum, cellContent, contentStyle);
								wsheet.addCell(content);
																
								//第2行
								counum++;
								wsheet.mergeCells(0, counum, 7, counum);
								cellContent="补考考试名册";
								content = new Label(0, counum, cellContent, contentStyle);
								wsheet.addCell(content);
							
								//第3行
								counum++;
								
								for(int colNum=0; colNum<8; colNum++){
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setWrap(true);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									//边框
									if(colNum==0){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}else if(colNum==7){
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
										
								for(int colNum=0; colNum<8; colNum++){
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									contentStyle.setShrinkToFit(true);//字体大小自适应
									
									if(colNum==0){
										cellContent=stuxlh+"";
									}else{
										cellContent=vec.get(i+colNum-1).toString();
										
										if(colNum==5){
											cellContent = this.parseXf(cellContent);
										}
									}
									//边框
									if(colNum==0){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										if(stunum == 30){
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
										}else{
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}
									}else if(colNum==7){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
										if(stunum == 30){
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
										}else{
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}
									}else{
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										if(stunum == 30){
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
										}else{
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}
									}
									content = new Label(colNum, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);	
												
								}
								wsheet.setRowView((counum+stunum), 380);
								stunum++;
								stuxlh++;
								
								if(i==vec.size()-7){//最后一条
									do{	
										for(int colNum=0; colNum<8; colNum++){
											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
											contentStyle = new WritableCellFormat(fontStyle);
											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											//边框
											if(colNum==0){
												cellContent=stuxlh+"";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum == 30){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else if(colNum==7){
												cellContent="";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
												if(stunum == 30){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else{
												cellContent="";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum == 30){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}
											
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
										
										wsheet.setRowView((counum+stunum), 380);
										stunum++;
										stuxlh++;	
									}while(stunum<31);
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									//31行
									wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
									cellContent="阅卷教师签名 ：__________";
									content = new Label(3, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
									//32行
									wsheet.mergeCells(3, counum+stunum, 6, counum+stunum);
									cellContent="阅 卷 日 期  ：__________";
									content = new Label(3, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
									//33行
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									wsheet.mergeCells(1, counum+stunum, 6, counum+stunum);
									cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
									content = new Label(1, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;

								}
								
								//设置表格行高
								wsheet.setRowView((counum-2), 460);
								wsheet.setRowView((counum-1), 440);
								wsheet.setRowView(counum, 620);

								wsheet.setRowView((counum+31), 420);
								wsheet.setRowView((counum+32), 300);
								wsheet.setRowView((counum+33), 420);
							}	
						}else{//第一条
							//生成标题
							//第1行
							//设置课表标题行列字体大小
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							
							wsheet.mergeCells(0, counum, 7, counum);
							cellContent = schoolName+xn+"学年度第"+xq+"学期";
							content = new Label(0, counum, cellContent, contentStyle);
							wsheet.addCell(content);
															
							//第2行
							counum++;
							wsheet.mergeCells(0, counum, 7, counum);
							cellContent="补考考试名册";
							content = new Label(0, counum, cellContent, contentStyle);
							wsheet.addCell(content);
						
							//第3行
							counum++;
							for(int colNum=0; colNum<8; colNum++){
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setWrap(true);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								//边框
								if(colNum==0){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}else if(colNum==7){
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
									
							for(int colNum=0; colNum<8; colNum++){
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								contentStyle.setShrinkToFit(true);//字体大小自适应
								
								if(colNum==0){
									cellContent=stuxlh+"";
								}else{
									cellContent=vec.get(i+colNum-1).toString();
									
									if(colNum==5){
										cellContent = this.parseXf(cellContent);
									}
								}
								//边框
								if(colNum==0){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									if(stunum == 30){
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
									}else{
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}
								}else if(colNum==7){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									if(stunum == 30){
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
									}else{
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}
								}else{
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									if(stunum == 30){
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
									}else{
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}
								}
								content = new Label(colNum, counum+stunum, cellContent, contentStyle);
								wsheet.addCell(content);
							}
							wsheet.setRowView((counum+stunum), 380);
							stunum++;
							stuxlh++;
																	
							//设置表格行高
							wsheet.setRowView((counum-2), 460);
							wsheet.setRowView((counum-1), 440);
							wsheet.setRowView(counum, 620);

							wsheet.setRowView((counum+31), 420);
							wsheet.setRowView((counum+32), 300);
							wsheet.setRowView((counum+33), 420);
						}
					}			
				}
				
				//输出流					
				//this.exportScore("exportExcel", wbook, tempVec, titleArray, title, xzbmc, kcmc, zks, xf, rkjs);

				//写入文件
				wbook.write();
				wbook.close();
				os.close();
				
				this.setMSG("文件生成成功");
				
				//添加补考登分教师信息
//				sql = "select count(*) from V_成绩管理_补考登分教师信息表 a " +
//					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 where b.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "'";
				
//				if(!db.getResultFromDB(sql)){
					sql = "insert into V_成绩管理_补考登分教师信息表 (科目编号,来源类型,相关编号,行政班代码,行政班名称,课程代码,课程名称,登分教师编号,登分教师姓名,创建人,创建时间,状态) " +
						"select a.科目编号,a.来源类型,a.相关编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.登分教师编号,a.登分教师姓名,'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
						"getDate(),a.状态 from V_成绩管理_登分教师信息表 a " +
						"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
						"where b.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "' " +
						"and a.相关编号 not in (select t.相关编号 from V_成绩管理_补考登分教师信息表 t " +
						"left join V_成绩管理_科目课程信息表 t1 on t1.科目编号=t.科目编号 " +
						"where t1.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "')";
					db.executeInsertOrUpdate(sql);
//				}
			}else{
				this.setMSG("没有相关补考信息");
			}
		} catch (RowsExceededException e1) {
			// TODO Auto-generated catch block
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/**
	 * 解析分层班名称
	 * @author yeq
	 * @date 20170214
	 * @param fcbmc 分层班名称
	 * @return String
	 */
	public String parseFcbName(String str){
		String result = "";
		
		//判断是否为分层班
		if(str.indexOf("-") > -1){
			result = str.split("-",-1)[0];
			
			for(int i=0; i<10; i++){
				result = result.replaceAll(String.valueOf(i), "");
			}
		}else{
			result = str;
		}
		
		return result;
	}
	
	/**
	 * 解析分层班课程名称
	 * @author yeq
	 * @date 20170214
	 * @param fcbmc 分层班名称
	 * @return String
	 */
	public String parseFcbCourse(String str){
		String result = "";
		String tempArray[] = new String[0];
		String fcbmc = "";
		
		//判断是否为分层班
		if(str.indexOf("-") > -1){
			tempArray = str.split("-",-1);
			fcbmc = tempArray[tempArray.length-1];
		}else{
			result = str;
		}
		
		return result;
	}
	
	/**  
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5  
	 * @param String s 需要得到长度的字符串  
	 * @return int 得到的字符串长度  
	 */   
	public static double getLength(String s) {  
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		
		//获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1    
		for(int i=0; i<s.length(); i++) {    
			//获取一个字符    
			String temp = s.substring(i, i+1);    
			//判断是否为中文字符
			if(temp.matches(chinese)) {    
				//中文字符长度为1    
				valueLength += 1;    
			}else{    
				//其他字符长度为0.5    
				valueLength += 0.5;    
			}    
		}
		
		//进位取整
		return Math.ceil(valueLength);    
	}
	
	/**
	 * 留级名单导出
	 * @date:2016-07-22
	 * @author:yeq
	 * @param type 类型(now当前数据/his历史数据)
	 * @throws SQLException
	 */
	public String ljmdExport(String type) throws SQLException{
		String filepath="";
		Vector wzcjVec = null;
		Vector vec = null;
		Vector sqlVec = new Vector();
		String sql = "";
		String savePath = MyTools.getProp(request, "Base.exportExcelPath")+"ljmdExport";
		String tempSubInfo[] = new String[0];
		String tempDetail[] = new String[0];
		int curRowNum = 0;
		String gradeName = "";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		//创建
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
		String tempArray[] = {"○","一","二","三","四","五","六","七","八","九"};
		int gradeNum = MyTools.StringToInt(this.getXNXQ().substring(2));
		String xnmc = this.getXNXQ();
		String tempXnName = "";
		for(int i=0; i<xnmc.length(); i++){
			tempXnName += tempArray[MyTools.StringToInt(xnmc.substring(i, i+1))];
		}
		String title = "";
		String filePath = savePath+"/"+schoolName+xnmc+"学年学生留级名单"+".xls";
		
		try {
//			sql = "select 学号,姓名,系名称,行政班名称,不及格数 from (select 学号,姓名,行政班名称,系名称,留级标准,count(*) as 不及格数 from (" +
//				"select a.学号,a.姓名,f.系名称,d.行政班名称,case when left(b.年级代码,2)='" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "' then 8 else 5 end as 留级标准,case when 大补考<>'' then 大补考 " +
//				"when 补考<>'' then 补考 when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end as 成绩 from V_成绩管理_学生成绩信息表 a " +
//				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//				"left join V_学生基本数据子类 c on c.学号=a.学号 " +
//				"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
//				"left join V_基础信息_系专业关系表 e on e.专业代码=d.专业代码 " +
//				"left join V_基础信息_系基础信息表 f on f.系代码=e.系代码 " +
//				"where left(b.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' and c.学生状态 in ('01','05','07','08') and a.成绩状态<>'0' and " +
//				"left(d.年级代码,2) in ('" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "','" + MyTools.fixSql(MyTools.StrFiltr(gradeNum)) + "')) as t " +
//				"where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
//				"group by 学号,姓名,系名称,行政班名称,留级标准) as t where 不及格数>=留级标准 order by 行政班名称,学号";
			if("now".equalsIgnoreCase(type)){
				sql = "select row_number() over (partition by 年级 order by 系名称,行政班名称,学号) as num,学号,姓名,系名称,行政班名称,不及格科目数," +
					"stuff((select ','+t2.课程名称+':'+left(t2.学年学期编码,5)+':'+" +
					"cast((case t2.课程类型 when '1' then t3.学分 " +
					"when '2' then t4.学分 " +
					"when '3' then t5.学分 " +
					"when '4' then t6.学分 " +
					"else 0 end) as varchar)+':'+" +
					"(case t1.总评 when '' then '0' else t1.总评 end)+':'+t1.补考 from V_成绩管理_学生成绩信息表 t1 " +
					"left join V_成绩管理_科目课程信息表 t2 on t2.科目编号=t1.科目编号 " +
					"left join V_规则管理_授课计划明细表 t3 on t3.授课计划明细编号=t1.相关编号 " +
					"left join V_排课管理_添加课程信息表 t4 on t4.编号=t1.相关编号 " +
					"left join V_规则管理_选修课授课计划明细表 t5 on t5.授课计划明细编号=t1.相关编号 " +
					"left join (select ta.分层班编号,tb.学分 from V_规则管理_分层班信息表  ta left join V_规则管理_分层课程信息表 tb on tb.分层课程编号=ta.分层课程编号) t6 on t6.分层班编号=t1.相关编号 " +
					"where t1.学号=t.学号 and t2.是否参与留级='1' and cast(left(t2.学年学期编码,4) as int)<=" + MyTools.fixSql(this.getXNXQ()) + " " +
					"and cast((case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end) as float)<60.0 " +
					"and (case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end) " +
					"not in ('-1','-6','-7','-8','-9','-11','-13','-15') and 成绩状态='1' order by t2.学年学期编码,t2.课程名称 for XML PATH('')),1,1,'') " +
					"from (select 学号,姓名,年级,行政班名称,系名称,留级标准,count(*) as 不及格科目数 " +
					"from (select a.学号,a.姓名,left(d.年级代码,2) as 年级,f.系名称,d.行政班名称,case when left(d.年级代码,2)='" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "' then 8 else 5 end as 留级标准," +
					"case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end as 成绩 " +
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
					"left join V_学生基本数据子类 c on c.学号=a.学号 " +
					"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
					"left join V_基础信息_系专业关系表 e on e.专业代码=d.专业代码 " +
					"left join V_基础信息_系基础信息表 f on f.系代码=e.系代码 " +
					"where b.是否参与留级='1' and cast(left(b.学年学期编码,4) as int)<=" + MyTools.fixSql(this.getXNXQ()) + " and c.学生状态 in ('01','05') and a.成绩状态='1' and " +
					"left(d.年级代码,2) in ('" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "','" + MyTools.fixSql(MyTools.StrFiltr(gradeNum)) + "')) as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
					"group by 学号,姓名,年级,系名称,行政班名称,留级标准) as t where 不及格科目数>=留级标准 " +
					"order by 年级,系名称,行政班名称,学号";
				vec = db.GetContextVector(sql);
				
				if(vec!=null && vec.size()>0){
					String stuStr = "";
					for(int i=0; i<vec.size(); i+=7){
						stuStr += "'" + MyTools.StrFiltr(vec.get(i+1)) + "',";
					}
					stuStr = stuStr.substring(0, stuStr.length()-1);
					
					//添加历史信息
					sql = "delete from V_成绩管理_历史留级名单信息表 where 学年='" + MyTools.fixSql(this.getXNXQ()) + "'";
					sqlVec.add(sql);
					
					sql = "delete from V_成绩管理_历史留级科目详情表 where 学年='" + MyTools.fixSql(this.getXNXQ()) + "'";
					sqlVec.add(sql);
					
					sql = "insert into V_成绩管理_历史留级名单信息表 " +
						"select '" + MyTools.fixSql(this.getXNXQ()) + "',row_number() over (partition by 年级 order by 系名称,行政班名称,学号) as num,学号,姓名,系名称,行政班名称,不及格科目数,'" + MyTools.fixSql(this.getUSERCODE()) + "',getDate(),'1' " +
						"from (select 学号,姓名,年级,行政班名称,系名称,留级标准,count(*) as 不及格科目数 from (select a.学号,a.姓名,left(d.年级代码,2) as 年级,f.系名称,d.行政班名称," +
						"case when left(d.年级代码,2)='" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "' then 8 else 5 end as 留级标准,case when 大补考<>'' then 大补考 when 补考<>'' then 补考 " +
						"when 重修2<>'' then 重修2 when 重修1<>'' then 重修1 else 总评 end as 成绩 from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
						"left join V_学生基本数据子类 c on c.学号=a.学号 " +
						"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
						"left join V_基础信息_系专业关系表 e on e.专业代码=d.专业代码 " +
						"left join V_基础信息_系基础信息表 f on f.系代码=e.系代码 " +
						"where b.是否参与留级='1' and cast(left(b.学年学期编码,4) as int)<=" + MyTools.fixSql(this.getXNXQ()) + " and c.学生状态 in ('01','05') and a.成绩状态='1' " +
						"and left(d.年级代码,2) in ('" + MyTools.fixSql(MyTools.StrFiltr(gradeNum-1)) + "','" + MyTools.fixSql(MyTools.StrFiltr(gradeNum)) + "')) as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
						"group by 学号,姓名,年级,系名称,行政班名称,留级标准) as t where 不及格科目数>=留级标准 order by 年级,系名称,行政班名称,学号";
					sqlVec.add(sql);
					
					sql = "insert into V_成绩管理_历史留级科目详情表 " +
						"select '" + MyTools.fixSql(this.getXNXQ()) + "',a.学号,b.课程名称,left(c.学年学期编码,5)," +
						"case c.课程类型 when '1' then '普通课程' " +
						"when '2' then '添加课程' " +
						"when '3' then '选修课程' " +
						"when '4' then '分层课程' " +
						"else '未知' end as 课程类型," +
						"case c.课程类型 when '1' then d.学分 " +
						"when '2' then e.学分 " +
						"when '3' then f.学分 " +
						"when '4' then g.学分 " +
						"else 0 end as 学分," +
						"a.总评,a.重修1,a.重修2,a.补考,a.大补考,'" + MyTools.fixSql(this.getUSERCODE()) + "',getDate(),'1' " +
						"from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
						"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
						"left join V_规则管理_授课计划明细表 d on d.授课计划明细编号=a.相关编号 " +
						"left join V_排课管理_添加课程信息表 e on e.编号=a.相关编号 " +
						"left join V_规则管理_选修课授课计划明细表 f on f.授课计划明细编号=a.相关编号 " +
						"left join (select t.分层班编号,t1.学分 from V_规则管理_分层班信息表  t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号) g on g.分层班编号=a.相关编号 " +
						"where cast(left(c.学年学期编码,4) as int)<=" + MyTools.fixSql(this.getXNXQ()) + " " +
						"and cast((case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) as float)<60.0 " +
						"and (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
						"and c.是否参与留级='1' and a.成绩状态='1' and a.学号 in (" + stuStr + ") order by a.学号,c.学年学期编码,b.课程名称";
					sqlVec.add(sql);
					db.executeInsertOrUpdateTransaction(sqlVec);
				}
			}else if("his".equalsIgnoreCase(type)){
				sql = "select a.序号,a.学号,a.姓名,a.系别,a.行政班名称,不及格科目数," +
					"stuff((select ','+课程名称+':'+学年学期+':'+学分+':'+(case 总评 when '' then '0' else 总评 end)+':'+补考 from V_成绩管理_历史留级科目详情表 " +
					"where 学号=a.学号 and 学年=a.学年 order by 学年学期,课程名称 for XML PATH('')),1,1,'') from V_成绩管理_历史留级名单信息表 a " +
					"where 学年='" + MyTools.fixSql(this.getXNXQ()) + "' order by a.系别,a.行政班名称,a.学号";
				vec = db.GetContextVector(sql);
			}
			
			if(vec!=null && vec.size()>0){
				//获取文字成绩代码信息
				sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1'";
				wzcjVec = db.GetContextVector(sql);
				
				OutputStream os = new FileOutputStream(filePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				String titleArray[] = {"序号","学号","姓名","系别","班级","不及格科目数","科目名称","学年学期","学分","总评","补考"};
				String cellContent = ""; //当前单元格的内容
				gradeName = MyTools.StrFiltr(vec.get(4)).substring(0, 3);
				WritableSheet wsheet = wbook.createSheet(gradeName, wbook.getNumberOfSheets());//工作表名称
				title = schoolName+tempXnName+"学年 "+gradeName+"学生留级名单";
				
				//设置列宽
				wsheet.setColumnView(0, 8);
				wsheet.setColumnView(1, 14);
				wsheet.setColumnView(2, 14);
				wsheet.setColumnView(3, 25);
				wsheet.setColumnView(4, 40);
				wsheet.setColumnView(5, 18);
				wsheet.setColumnView(6, 40);
				wsheet.setColumnView(7, 15);
				wsheet.setColumnView(8, 10);
				wsheet.setColumnView(9, 10);
				wsheet.setColumnView(10, 10);

				//标题
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet.mergeCells(0, curRowNum, titleArray.length-1, curRowNum);
				content = new Label(0, curRowNum, title, contentStyle);
				wsheet.addCell(content);
				curRowNum++;
					
				//表格标题行
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
					
				for(int j=0; j<titleArray.length; j++) {
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					contentStyle.setWrap(true);// 自动换行
					
					if(j == 0){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else if(j == titleArray.length-1){
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}else{
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}
					content = new Label(j, curRowNum, titleArray[j], contentStyle);
					wsheet.addCell(content);
				}
				curRowNum++;
				wsheet.setRowView(1, 400);
				
				//表格内容
				for(int i=0; i<vec.size(); i+=7){
					if(!gradeName.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4)).substring(0, 3))){
						gradeName = MyTools.StrFiltr(vec.get(i+4)).substring(0, 3);
						curRowNum = 0;
						title = schoolName+tempXnName+"学年 "+gradeName+"学生留级名单";
						wsheet = wbook.createSheet(gradeName, wbook.getNumberOfSheets());//工作表名称
						
						//设置列宽
						wsheet.setColumnView(0, 8);
						wsheet.setColumnView(1, 14);
						wsheet.setColumnView(2, 14);
						wsheet.setColumnView(3, 25);
						wsheet.setColumnView(4, 40);
						wsheet.setColumnView(5, 18);
						wsheet.setColumnView(6, 40);
						wsheet.setColumnView(7, 15);
						wsheet.setColumnView(8, 10);
						wsheet.setColumnView(9, 10);
						wsheet.setColumnView(10, 10);
						
						//标题
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						wsheet.mergeCells(0, curRowNum, titleArray.length-1, curRowNum);
						content = new Label(0, curRowNum, title, contentStyle);
						wsheet.addCell(content);
						curRowNum++;
							
						//表格标题行
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 12, WritableFont.BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.BLACK);
							
						for (int j=0; j<titleArray.length; j++) {
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							contentStyle.setWrap(true);// 自动换行
							
							if(j == 0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}else if(j == titleArray.length-1){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
							content = new Label(j, curRowNum, titleArray[j], contentStyle);
							wsheet.addCell(content);
						}
						curRowNum++;
						wsheet.setRowView(1, 400);
					}
					
					for(int j=0; j<titleArray.length-4; j++){
						if(j < titleArray.length-5){
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
									false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						}else{
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
									false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						}
						
						if(j < titleArray.length-5){
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
							contentStyle.setWrap(true);// 自动换行
							
							if(j == 0)
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							else
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							
							wsheet.mergeCells(j, curRowNum, j, curRowNum+(MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+5))))-1);
							content = new Label(j, curRowNum, MyTools.StrFiltr(vec.get(i+j)), contentStyle);
							wsheet.addCell(content);
						}else{
							tempSubInfo = MyTools.StrFiltr(vec.get(i+6)).split(",");
							
							for(int k=0; k<tempSubInfo.length; k++){
								tempDetail = tempSubInfo[k].split(":", -1);
								
								for(int a=0; a<tempDetail.length; a++){
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
									contentStyle.setWrap(true);// 自动换行
									
									if(a < 4){
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									}else{
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									}
									
									if(k == tempSubInfo.length-1)
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
									else
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									
									if(a == 2){
										content = new Label(j+a, curRowNum, this.parseXf(tempDetail[a]), contentStyle);
									}else if(a==3 || a==4){
										content = new Label(j+a, curRowNum, this.parseScore(wzcjVec, tempDetail[a]), contentStyle);
									}else{
										content = new Label(j+a, curRowNum, tempDetail[a], contentStyle);
									}
									wsheet.addCell(content);
								}
								
								wsheet.setRowView(curRowNum, 300);
								curRowNum++;
							}
						}
					}
				}
				
				//写入文件
				wbook.write();
				wbook.close();
				os.close();
				
				this.setMSG("文件生成成功");
			}else{
				this.setMSG("当前学期没有留级学生信息");
			}
		} catch (RowsExceededException e1) {
			// TODO Auto-generated catch block
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/**
	 * 未打印的课程信息导出
	 * @date:2016-07-22
	 * @author:yeq
	 * @throws SQLException
	 */
	public String wdyExport() throws SQLException{
		String filepath="";
		Vector vec = null;
		String sql = "";
		String savePath = MyTools.getProp(request, "Base.exportExcelPath")+"wdyExport";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		//创建
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		String xnxqName = "";
		if(!"all".equalsIgnoreCase(this.getXNXQ())){
			String tempXq = this.getXNXQ().substring(4, 5);
			xnxqName = this.getXNXQ().substring(0, 4)+("1".equalsIgnoreCase(tempXq)?"第一学期":"第二学期");
		}else{
			xnxqName = "全部学年学期";
		}
		String title = schoolName+xnxqName+"未打印成绩信息清单";
		String filePath = savePath+"/"+schoolName+xnxqName+"未打印成绩信息清单"+".xls";
		
		try {
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			String titleArray[] = {"课程名称","班级名称","登分教师"};
			String cellContent = ""; //当前单元格的内容
			
			sql = "select a.课程名称,a.行政班名称,replace((case when a.登分教师姓名='@请选择@' then '@未设置@' else a.登分教师姓名 end),'@','') " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.打印锁定='0'";
			
			if(!"all".equalsIgnoreCase(this.getXNXQ())){
				sql += " and b.学年学期编码 in ('" + this.getXNXQ().replaceAll(",", "','") + "')";
			}
			sql += " order by a.登分教师姓名";
			
			vec = db.GetContextVector(sql);
			
			//设置列宽
			wsheet.setColumnView(0, 40);
			wsheet.setColumnView(1, 40);
			wsheet.setColumnView(2, 30);

			//标题
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 14, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			
			wsheet.mergeCells(0, 0, titleArray.length-1, 0);
			content = new Label(0, 0, title, contentStyle);
			wsheet.addCell(content);
				
			//表格标题行
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 12, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
				
			for (int i=0; i<titleArray.length; i++) {
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setWrap(true);// 自动换行
				
				if(i == 0){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}else if(i == titleArray.length-1){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}else{
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}
				content = new Label(i, 1, titleArray[i], contentStyle);
				wsheet.addCell(content);
			}
			wsheet.setRowView(1, 400);
			
			if(vec!=null && vec.size()>0){
				//表格内容
				//k:用于循环时Excel的行号
				//外层for用于循环行,内曾for用于循环列
				for (int i=0, k=2; i<vec.size(); i+=titleArray.length, k++) {
					for (int j=0; j<titleArray.length; j++) {
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
						contentStyle.setWrap(true);// 自动换行
						
						if(j == 0){
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						}else if(j == titleArray.length-1){
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						}
						
						if(k == vec.size()/3+1){
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}
						
						content = new Label(j, k, vec.get(i+j) + "", contentStyle);
						wsheet.addCell(content);
					}
					
					//设置表格行高
					wsheet.setRowView(k, 400);
				}
			}
				
			//写入文件
			wbook.write();
			wbook.close();
			os.close();
			
			this.setMSG("文件生成成功");
		} catch (RowsExceededException e1) {
			// TODO Auto-generated catch block
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/**
	 * 修改成绩状态
	 * @date:2016-07-20
	 * @author: yeq
	 * @throws SQLException
	 */
	public void changeStuScoreState() throws SQLException{
		String sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='3' where 编号='" + MyTools.fixSql(this.getCODE()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 批量修改成绩状态
	 * @date:2016-12-01
	 * @author: yeq
	 * @throws SQLException
	 */
	public void changeMultiStuScoreState() throws SQLException{
		String sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='3' where 编号 in ('" + this.getCODE().replaceAll(",", "','") + "')";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 同步查分数据
	 * @date:2016-07-26
	 * @author: yeq
	 * @throws SQLException
	 */
	public void syncData() throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		String linkStr = MyTools.getProp(request, "Base.linkInfo");
		String sysName = MyTools.getProp(request, "Base.sysName");
		String syncTargetSysName = MyTools.getProp(request, "Base.syncTargetSysName");
		
		if(!"".equalsIgnoreCase(linkStr))
			linkStr += ".";
		
		//用户信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysUserinfo]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysUserinfo] select * from [" + sysName + "].[dbo].[sysUserinfo]";
		sqlVec.add(sql);
		
		//用户权限信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysAuthority]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysAuthority] select * from [" + sysName + "].[dbo].[sysAuthority]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysUserAuth]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysUserAuth] select * from [" + sysName + "].[dbo].[sysUserAuth]";
		sqlVec.add(sql);
		
		//模块信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysAuthModule]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysAuthModule] select * from [" + sysName + "].[dbo].[sysAuthModule]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysAuModule]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysAuModule] select * from [" + sysName + "].[dbo].[sysAuModule]";
		sqlVec.add(sql);
		
		//部门信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysDepartment]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysDepartment] select * from [" + sysName + "].[dbo].[sysDepartment]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysUserDept]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysUserDept] select * from [" + sysName + "].[dbo].[sysUserDept]";
		sqlVec.add(sql);
		
		//其他
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysPermissionData]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[sysPermissionData] select * from [" + sysName + "].[dbo].[sysPermissionData]";
		sqlVec.add(sql);
		
		//专业信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[ZZJX0101]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[ZZJX0101] select * from [" + sysName + "].[dbo].[ZZJX0101]";
		sqlVec.add(sql);
		
		//班级信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[ZZJX0202]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[ZZJX0202] select * from [" + sysName + "].[dbo].[ZZJX0202]";
		sqlVec.add(sql);
		
		//学生信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[ZZXS0101]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[ZZXS0101] select * from [" + sysName + "].[dbo].[ZZXS0101]";
		sqlVec.add(sql);
		
		//学年学期信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_XNXQ]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_XNXQ] select * from [" + sysName + "].[dbo].[GZGL_XNXQ]";
		sqlVec.add(sql);
		
		//课程信息
		//普通课程
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_SKJHMX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_SKJHMX] select * from [" + sysName + "].[dbo].[GZGL_SKJHMX]";
		sqlVec.add(sql);
		//选修课
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_XXKSKJH]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_XXKSKJH] select * from [" + sysName + "].[dbo].[GZGL_XXKSKJH]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_XXKSKJHMX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[GZGL_XXKSKJHMX] select * from [" + sysName + "].[dbo].[GZGL_XXKSKJHMX]";
		sqlVec.add(sql);
		//添加课程
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[PKGL_TJKCXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[PKGL_TJKCXX] select * from [" + sysName + "].[dbo].[PKGL_TJKCXX]";
		sqlVec.add(sql);
		
		//查分设置
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_CFSZXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_CFSZXX] select * from [" + sysName + "].[dbo].[CJGL_CFSZXX]";
		sqlVec.add(sql);
		
		//科目信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_KMKCXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_KMKCXX] select * from [" + sysName + "].[dbo].[CJGL_KMKCXX]";
		sqlVec.add(sql);
		
		//学生成绩信息
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_XSCJXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_XSCJXX] select * from [" + sysName + "].[dbo].[CJGL_XSCJXX]";
		sqlVec.add(sql);
		
		//文字代码
		sql = "truncate table " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_WZCJDM]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + syncTargetSysName + "].[dbo].[CJGL_WZCJDM] select * from [" + sysName + "].[dbo].[CJGL_WZCJDM]";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("同步成功");
		}else{
			this.setMSG("同步失败");
		}
	}
	
	/**  
	 * 添加查询学生最近一个学期的成绩基础信息
	 * @date:2016-07-20
	 * @author:yeq
	 * @throws SQLException 
	 */   
	public boolean addStuScoreInfo() throws SQLException {
		boolean flag = true;
		Vector vec = null;
		Vector sqlVec = new Vector();
		String sql = "";
		Vector stuVec = null;
		Vector courseVec = null;
		Vector tempVec = null;
		String curXnxq = "";
		String stuCode = "";
		String stuName = "";
		String classCode = "";
		String state = "";
		boolean addFlag = true;
		
		//当前需要查询的所有学生
		sql = "select distinct 学号,姓名,行政班代码,case when left(学号,2)<>left(行政班代码,2) then '1' else '0' end as 异动状态 from V_学生基本数据子类 " +
			"where 学生状态 in ('01','05')";
		if(!"".equalsIgnoreCase(this.getSTUCODE()))
			sql += " and 学号 like '%" + MyTools.fixSql(this.getSTUCODE()) + "%'";
		if(!"".equalsIgnoreCase(this.getSTUNAME()))
			sql += " and 姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
		sql += " order by 学号";
		stuVec = db.GetContextVector(sql);
		
		if(stuVec!=null && stuVec.size()>0){
			//查询当前学年学期
			sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				curXnxq = MyTools.StrFiltr(vec.get(0));
				
				for(int i=0; i<stuVec.size(); i+=4){
					stuCode = MyTools.StrFiltr(stuVec.get(i));
					stuName = MyTools.StrFiltr(stuVec.get(i+1));
					classCode = MyTools.StrFiltr(stuVec.get(i+2));
					state = MyTools.StrFiltr(stuVec.get(i+3));
					
					sql = "select * from (select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
						"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
						"where a.行政班代码='" + MyTools.fixSql(classCode) + "' and b.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "' " +
						"union all " +
						"select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
						"left join V_规则管理_学生选修课关系表 b on b.授课计划明细编号=a.相关编号 " +
						"left join V_规则管理_选修课授课计划明细表 c on c.授课计划明细编号=b.授课计划明细编号 " +
						"left join V_规则管理_选修课授课计划主表 d on d.授课计划主表编号=c.授课计划主表编号 " +
						"where b.学号='" + MyTools.fixSql(stuCode) + "' and d.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "'" +
						"union all " +
						"select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
						"left join V_规则管理_分层班学生信息表 b on b.分层班编号=a.相关编号 " +
						"left join V_规则管理_分层班信息表 c on c.分层班编号=b.分层班编号 " +
						"left join V_规则管理_分层课程信息表 d on d.分层课程编号=c.分层课程编号 " +
						"where b.学号='" + MyTools.fixSql(stuCode) + "' and d.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "') as t " +
						"where 相关编号 not in (select a.相关编号 from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
						"where b.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "' and 学号='" + MyTools.fixSql(stuCode) + "')";
					courseVec = db.GetContextVector(sql);
					
					if(courseVec!=null && courseVec.size()>0){
						for(int j=0; j<courseVec.size(); j+=4){
							/*20170315修改yeq，无需判断是否异动学生（留级学生需要重读所有课程，无论是否及格）
							addFlag = false;
							
							//判断课程类型,如果是选修课直接添加成绩信息
							if("1".equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(j+3))) || "2".equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(j+3)))){
								//判断是否为异动的学生，如果没有异动，直接添加成绩信息
								if("0".equalsIgnoreCase(state)){
									addFlag = true;
								}else{
									//查询当前课程是否有成绩信息
									//如果没有，直接添加成绩信息
									//如果有，判断若已通过，不添加成绩信息，若没有通过，更新原成绩状态为无效，并添加本学期成绩信息
									sql = "select 编号,case when ((cast(成绩 as float)<60.0) " +
										"and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')) then '0' else '1' end 状态 from (" +
										"select a.编号,学号,case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 " +
										"when 重修1<>'' then 重修1 else 总评 end as 成绩 from V_成绩管理_学生成绩信息表 a " +
										"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
										"where a.学号='" + MyTools.fixSql(stuCode) + "' " +
										"and a.成绩状态='1' and b.课程名称='" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+2))) + "') as t";
									tempVec = db.GetContextVector(sql);
									
									if(tempVec!=null && tempVec.size()>0){
										if("0".equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(1)))){
											sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='3' " +
												"where 编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(0))) + "'";
											sqlVec.add(sql);
											
											addFlag = true;
										}
									}else{
										addFlag = true;
									}
								}
							}else{
								addFlag = true;
							}
							
							if(addFlag){
								sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
									"'" + MyTools.fixSql(stuCode) + "'," +
									"'" + MyTools.fixSql(stuName) + "'," +
									"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j))) + "'," +
									"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+1))) + "'," +
									"'1'," +
									"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
									"getDate(),'1')";
								sqlVec.add(sql);
							}
							*/
							sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
								"'" + MyTools.fixSql(stuCode) + "'," +
								"'" + MyTools.fixSql(stuName) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+1))) + "'," +
								"'1'," +
								"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
								"getDate(),'1')";
							sqlVec.add(sql);
						}
					}
				}
			}
		}
		
		if(sqlVec.size() > 0){
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				flag = true;
			}else{
				flag = false;
			}
		}
		
		return flag;
	}
	
	/**
	 * 读取无效成绩信息列表数据
	 * @date:2016-08-08
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queInvalidScoreList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select a.编号,a.学号,a.姓名,c.行政班名称,c.课程名称,left(b.学年学期编码,5) as 学年学期,a.平时,a.期中,a.实训,a.期末,a.总评,a.重修1,a.重修2,a.补考,a.大补考 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_成绩管理_登分教师信息表 c on c.相关编号=a.相关编号 " +
				"left join V_学生基本数据子类 d on d.学号=a.学号 " +
				"where a.成绩状态='3'";
		
		if(!"".equalsIgnoreCase(this.getXNXQ())){
			sql += " and b.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getKCMC())){
			sql += " and c.课程名称 like '%" + MyTools.fixSql(this.getKCMC()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getSTUCODE())){
			sql += " and a.学号 like '%" + MyTools.fixSql(this.getSTUCODE()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getSTUNAME())){
			sql += " and a.姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
		}
		sql += " order by a.学号,b.学年学期编码";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 恢复成绩信息状态
	 * @date:2016-08-08
	 * @author: yeq
	 * @throws SQLException
	 */
	public void recoveryScoreInfo() throws SQLException{
		String sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='1' where 编号 in ('" + this.getCODE().replace(",", "','") + "')";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("恢复成功");
		}else{
			this.setMSG("恢复失败");
		}
	}
	
	/**
	 * 删除成绩信息
	 * @date:2016-08-08
	 * @author: yeq
	 * @throws SQLException
	 */
//	public void delScoreInfo() throws SQLException{
//		String sql = "delete V_成绩管理_学生成绩信息表 where 编号 in ('" + this.getCODE().replace(",", "','") + "')";
//		
//		if(db.executeInsertOrUpdate(sql)){
//			this.setMSG("删除成功");
//		}else{
//			this.setMSG("删除失败");
//		}
//	}
	
	/**
	 * 读取学年下拉框(绩点统计模块)
	 * @date:2016-08-10
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJdXnCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " + 
				"select distinct 学年 as comboName,学年 as comboValue,'1' as orderNum " +
				"from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业下拉框(绩点统计模块)
	 * @date:2016-08-10
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJdZydmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类 where 状态='1' and len(专业代码)>3 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取绩点统计信息列表数据
	 * @date:2016-08-10
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queJdMajorList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select distinct left(b.学年学期编码,4) as 学年,b.专业代码,b.专业名称 from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where b.专业代码<>''";
		
		if(!"".equalsIgnoreCase(this.getXNXQ())){
			sql += " and left(b.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getZYMC())){
			sql += " and b.专业名称 like '%" + MyTools.fixSql(this.getZYMC()) + "%'";
		}
		sql += " order by left(b.学年学期编码,4),专业名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取绩点统计详情列表数据
	 * @date:2016-08-11
	 * @author:yeq
	 * @param type 查询类型
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queJdInfoList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "";
		String gradeInfo = this.getXNXQ().substring(2);
		gradeInfo = MyTools.StringToInt(gradeInfo)-1+"','"+gradeInfo;
		String type = "now";
		String wjcjStr = "when '-1' then 60 " + 
						"when '-2' then 0 " +
						"when '-3' then 0 " +
						"when '-4' then 0 " +
						"when '-5' then 0 " +
						"when '-6' then 90 " +
						"when '-7' then 80 " +
						"when '-8' then 70 " +
						"when '-9' then 60 " +
						"when '-10' then 59 " +
						"when '-11' then 60 " +
						"when '-12' then 59 " +
						"when '-13' then 60 " +
						"when '-15' then 50 " +
						"when '-16' then 0 ";
		
		//判断是查询即时数据还是历史数据
//		sql = "select count(*) from V_成绩管理_历史绩点统计信息表 where 学年='" + MyTools.fixSql(this.getXNXQ()) + "'";
//		
//		if(db.getResultFromDB(sql)){
//			sql = "select count(*) from V_成绩管理_学生成绩信息表 a " +
//				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//				"where left(b.学年学期编码,4)='" + MyTools.fixSql(MyTools.StrFiltr(MyTools.StringToInt(this.getXNXQ())+1)) + "'";
//			
//			if(db.getResultFromDB(sql)){
//				type = "his";
//			}
//		}
		sql = "select top 1 学年 from V_规则管理_学年学期表 where getDate()>=学期开始时间 order by 学年 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			if(MyTools.StringToInt(this.getXNXQ()) < (MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)))-1)){
				type = "his";
			}
		}
		
		if("now".equalsIgnoreCase(type)){
			sql = "with tempInfo as(" +
				"select 学号,姓名,课程名称,cast(总评 as float) as 总评," +
				"cast(case 科目类型 when '1' then (case when 成绩 between 90 and 100 then 4 " +
				"when 成绩 between 80 and 89 then 3 " +
				"when 成绩 between 70 and 79 then 2 " +
				"when 成绩 between 60 and 69 then 1 else 0 end) " +
				"when '2' then (case when 成绩>59 then (成绩-50)/10 else 0 end) end as float) as 绩点," +
				"cast(学分 as float) as 学分 " +
				"from (select a.学号,a.姓名,b.课程名称,case when (b.课程类型='1' or b.课程类型='2' or b.课程类型='3') then b.科目类型 " +
				"else (select top 1 t4.科目类型 from V_学生基本数据子类 t1 " +
				"left join V_学校班级数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
				"left join V_专业基本信息数据子类 t3 on t3.专业代码=t2.专业代码 " +
				"left join V_基础信息_专业科目类型信息表 t4 on t4.专业代码=t3.专业代码 " +
				"where t1.学号=a.学号) end as 科目类型,a.总评," +
				"case a.总评 " + wjcjStr + "else cast(a.总评 as float) end as 成绩," +
				"case b.课程类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=a.相关编号) " +
				"when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=a.相关编号) " +
				"when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=a.相关编号)	" +
				"when '4' then (select t1.学分 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号 where t.分层班编号=a.相关编号) " +
				"else 0 end as 学分 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_学生基本数据子类 c on c.学号=a.学号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
				"where c.学生状态 in ('01','05') and a.成绩状态='1' and b.是否参与绩点='1' " +
				"and left(b.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' " +
				"and left(d.年级代码,2) in ('" + gradeInfo + "') " +
				"and d.专业代码='" + MyTools.fixSql(this.getZYMC()) + "') as t)," +
				"tempInfo_sport as(select 学号,姓名,课程名称,cast(avg(cast(总评1 as float)) as varchar) as zp1,cast(avg(cast(总评2 as float)) as varchar) as zp2 " +
				"from (select a.学号,a.姓名,case when len(b.课程名称)<5 then '体育' else '体育新标准' end as 课程名称," +
				"case a.总评 " + wjcjStr + "else a.总评 end as 总评1," +
				"case a.总评 " + wjcjStr + "else a.总评 end as 总评2 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_学生基本数据子类 c on c.学号=a.学号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
				"where c.学生状态 in ('01','05') and a.成绩状态='1' and b.是否参与绩点='1' " +
				"and left(b.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' " +
				"and left(d.年级代码,2) in ('" + gradeInfo + "') and left(b.课程名称,2)='体育' " +
				"and b.专业代码='" + MyTools.fixSql(this.getZYMC()) + "') as t " +
				"group by 学号,姓名,课程名称) " +
				"select t.学号,t.姓名,cast(cast(Round(t.平均绩点,4) as numeric(18,4)) as nvarchar) as 平均绩点,t.不及格科目数," +
				"case t.体育num when 0 then '' when 1 then (case t1.zp1 when '-1' then '免修' when '-15' then '达标' else t1.zp1 end ) " +
				"else (case t1.zp2 when '-1' then '免修' when '-15' then '达标' else t1.zp2 end) end 体育," +
				"case t.体育新标准num when 0 then '' when 1 then (case t2.zp1 when '-1' then '免修' when '-15' then '达标' else t2.zp1 end) " +
				"else (case t2.zp2 when '-1' then '免修' when '-15' then '达标' else t2.zp2 end) end 体育新标准,行政班简称 " +
				"from (select a.学号,a.姓名,sum(a.绩点*a.学分)/sum(a.学分) as 平均绩点," +
				"SUM(case when a.绩点=0 and a.总评<>'-15' then 1 else 0 end) as 不及格科目数," +
				"(select count(*) from tempInfo_sport where 学号=a.学号 and left(课程名称,2)='体育' and right(课程名称,3)<>'新标准') as 体育num," +
				"(select count(*) from tempInfo_sport where 学号=a.学号 and right(课程名称,3)='新标准') as 体育新标准num,c.行政班简称 " +
				"from tempInfo a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
				"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 where a.总评<>'-1' group by a.学号,a.姓名,c.行政班简称) as t " +
				"left join tempInfo_sport t1 on t1.学号=t.学号 and t1.课程名称='体育' " +
				"left join tempInfo_sport t2 on t2.学号=t.学号 and t2.课程名称='体育新标准' where 1=1";
			
			if(!"".equalsIgnoreCase(this.getSTUCODE())){
				sql += " and t.学号 like '%" + MyTools.fixSql(this.getSTUCODE()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getSTUNAME())){
				sql += " and t.姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getBJMC())){
				sql += " and t.行政班简称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
			}
			sql += " order by t.平均绩点 desc";
		}else if("his".equalsIgnoreCase(type)){
			sql = "select 学号,姓名,平均绩点,不及格科目数,体育,体育新标准,行政班名称 as 行政班简称 from V_成绩管理_历史绩点统计信息表 " +
				"where 学年='" + MyTools.fixSql(this.getXNXQ()) + "' and 专业代码='" + MyTools.fixSql(this.getZYMC()) + "'";
			
			if(!"".equalsIgnoreCase(this.getSTUCODE())){
				sql += " and 学号 like '%" + MyTools.fixSql(this.getSTUCODE()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getSTUNAME())){
				sql += " and 姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
			}
			if(!"".equalsIgnoreCase(this.getBJMC())){
				sql += " and 行政班名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
			}
			
			sql += " order by 平均绩点 desc";
		}
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 绩点统计名单导出
	 * @date:2016-08-11
	 * @author:yeq
	 * @throws SQLException
	 */
	public String jdtjExport() throws SQLException{
		String filePath = "";
		Vector vec = null;
		String sql = "";
		String savePath = MyTools.getProp(request, "Base.exportExcelPath")+"jdtjExport/" + this.getXNXQ() + "学年绩点统计信息";
		String wjcjStr = "when '-2' then 0 " +
				"when '-3' then 0 " +
				"when '-4' then 0 " +
				"when '-5' then 0 " +
				"when '-6' then 90	" +
				"when '-7' then 80 " +
				"when '-8' then 70 " +
				"when '-9' then 60 " +
				"when '-10' then 59 " +
				"when '-11' then 60 " +
				"when '-12' then 59 " +
				"when '-13' then 60 " +
				"when '-15' then 50 " +
				"when '-16' then 0 ";
		
		//创建
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		String type = "now";
		
		//判断是查询即时数据还是历史数据
//		sql = "select count(*) from V_成绩管理_历史绩点统计信息表 where 学年='" + MyTools.fixSql(this.getXNXQ()) + "'";
//		
//		if(db.getResultFromDB(sql)){
//			sql = "select count(*) from V_成绩管理_学生成绩信息表 a " +
//				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//				"where left(b.学年学期编码,4)='" + MyTools.fixSql(MyTools.StrFiltr(MyTools.StringToInt(this.getXNXQ())+1)) + "'";
//			
//			if(db.getResultFromDB(sql)){
//				type = "his";
//			}
//		}
		sql = "select top 1 学年 from V_规则管理_学年学期表 where getDate()>=学期开始时间 order by 学年 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			if(MyTools.StringToInt(this.getXNXQ()) < (MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)))-1)){
				type = "his";
			}
		}
		
		//读取导出的统计信息
		String gradeInfo = this.getXNXQ().substring(2);
		gradeInfo = MyTools.StringToInt(gradeInfo)-1+"','"+gradeInfo;
		if("now".equalsIgnoreCase(type)){
			sql = "with tempInfo as(" +
				"select 学号,姓名,课程名称,cast(总评 as float) as 总评," +
				"case 科目类型 when '1' then (case when 成绩 between 90 and 100 then 4 " +
				"when 成绩 between 80 and 89 then 3 " +
				"when 成绩 between 70 and 79 then 2 " +
				"when 成绩 between 60 and 69 then 1 " +
				"else 0 end) " +
				"when '2' then (case when 成绩>59 then (成绩-50)/10 else 0 end) end as 绩点," +
				"cast(学分 as float) as 学分 " +
				"from (select a.学号,a.姓名,b.课程名称,case when (b.课程类型='1' or b.课程类型='2' or b.课程类型='4') then b.科目类型 " +
				"else (select top 1 t4.科目类型 from V_学生基本数据子类 t1 " +
				"left join V_学校班级数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
				"left join V_专业基本信息数据子类 t3 on t3.专业代码=t2.专业代码 " +
				"left join V_基础信息_专业科目类型信息表 t4 on t4.专业代码=t3.专业代码 " +
				"where t1.学号=a.学号) end as 科目类型,a.总评," +
				"case a.总评 " + wjcjStr + "else cast(a.总评 as float) end as 成绩," +
				"case b.课程类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=a.相关编号) " +
				"when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=a.相关编号) " +
				"when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=a.相关编号)	" +
				"when '4' then (select t1.学分 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号 where t.分层班编号=a.相关编号) " +
				"else 0 end as 学分 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_学生基本数据子类 c on c.学号=a.学号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
				"where c.学生状态 in ('01','05') and a.成绩状态='1' and b.是否参与绩点='1' " +
				"and left(b.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' " +
				"and left(d.年级代码,2) in ('" + gradeInfo + "')) as t)," +
				"tempInfo_sport as(select 学号,姓名,课程名称,cast(avg(cast(总评1 as float)) as varchar) as zp1," +
				"cast(avg(cast(总评2 as float)) as varchar) as zp2 " +
				"from (select a.学号,a.姓名,case when len(b.课程名称)<5 then '体育' else '体育新标准' end as 课程名称," +
				"case a.总评 " + wjcjStr + "else a.总评 end as 总评1," +
				"case a.总评 " + wjcjStr + "else a.总评 end as 总评2 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_学生基本数据子类 c on c.学号=a.学号 " +
				"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
				"where c.学生状态 in ('01','05') and a.成绩状态='1' and b.是否参与绩点='1' " +
				"and left(b.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' " +
				"and left(d.年级代码,2) in ('" + gradeInfo + "') and left(b.课程名称,2)='体育') as t " +
				"group by 学号,姓名,课程名称) " +
				"select t.专业代码,t.专业名称,t.学号,t.姓名,cast(Round(t.平均绩点,4) as numeric(18,4)) as 平均绩点,t.不及格科目数," +
				"case t.体育num when 0 then '' when 1 then (case t1.zp1 when '-1' then '免修' when '-15' then '达标' else t1.zp1 end ) " +
				"else (case t1.zp2 when '-1' then '免修' when '-15' then '达标' else t1.zp2 end) end 体育," +
				"case t.体育新标准num when 0 then '' when 1 then (case t2.zp1 when '-1' then '免修' when '-15' then '达标' else t2.zp1 end) " +
				"else (case t2.zp2 when '-1' then '免修' when '-15' then '达标' else t2.zp2 end) end 体育新标准,行政班简称 " +
				"from (select c.专业代码,d.专业名称,a.学号,a.姓名,sum(a.绩点*a.学分)/sum(a.学分) as 平均绩点," +
				"SUM(case when a.绩点=0 and a.总评<>'-15' then 1 else 0 end) as 不及格科目数," +
				"(select count(*) from tempInfo_sport where 学号=a.学号 and left(课程名称,2)='体育' and right(课程名称,3)<>'新标准') as 体育num," +
				"(select count(*) from tempInfo_sport where 学号=a.学号 and right(课程名称,3)='新标准') as 体育新标准num,c.行政班简称 " +
				"from tempInfo a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
				"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
				"where a.总评<>'-1' group by a.学号,a.姓名,c.行政班简称,c.专业代码,d.专业名称) as t " +
				"left join tempInfo_sport t1 on t1.学号=t.学号 and t1.课程名称='体育' " +
				"left join tempInfo_sport t2 on t2.学号=t.学号 and t2.课程名称='体育新标准' order by t.专业代码,t.平均绩点 desc";
		}else if("his".equalsIgnoreCase(type)){
			sql = "select a.专业代码,b.专业名称,a.学号,a.姓名,a.平均绩点,a.不及格科目数,a.体育,a.体育新标准,a.行政班名称 from V_成绩管理_历史绩点统计信息表 a " +
				"left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
				"where 学年='" + MyTools.fixSql(this.getXNXQ()) + "' order by a.专业代码,a.平均绩点 desc";
		}
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			String[] titleArray = {"学号","姓名","平均绩点","不及格科目数","体育","体育新标准","班级"};
			String tempMajorCode = "";
			String tempMajorName = "";
			String tempFileName = "";
			Vector tempVec = null;
			
			for(int i=0; i<vec.size(); i+=9){
				if(!tempMajorCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						if(!this.createJdtjFile(savePath+"/"+tempFileName, tempVec, titleArray)){
							return filePath;
						}
					}
					
					tempMajorCode = MyTools.StrFiltr(vec.get(i));
					tempMajorName = MyTools.StrFiltr(vec.get(i+1));
					tempFileName = this.getXNXQ()+"学年"+tempMajorName+"（"+tempMajorCode+"）绩点统计信息.xls";
					tempVec = new Vector();
					tempVec.add(MyTools.StrFiltr(vec.get(i+2)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+3)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+4)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+8)));
				}else{
					tempVec.add(MyTools.StrFiltr(vec.get(i+2)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+3)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+4)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+8)));
				}
			}
			if(!this.createJdtjFile(savePath+"/"+tempFileName, tempVec, titleArray)){
				return filePath;
			}
			
			filePath = savePath+".zip";
			//打包文件
			this.compressZip(savePath, filePath);
			//删除原文件
			this.deleteDirectory(savePath);
			
			this.setMSG("文件生成成功");
			
			//判断是否需要添加历史数据
			if("now".equalsIgnoreCase(type)){
				Vector sqlVec = new Vector();
				
				sql = "delete from V_成绩管理_历史绩点统计信息表 where 学年='" + MyTools.fixSql(this.getXNXQ()) + "'";
				sqlVec.add(sql);
				
				if(vec!=null && vec.size()>0){
					for(int i=0; i<vec.size(); i+=9){
						sql = "insert into V_成绩管理_历史绩点统计信息表 (学年,学号,姓名,专业代码,平均绩点,不及格科目数,体育,体育新标准,行政班名称,创建人,创建时间,状态) values(" +
							"'" + MyTools.fixSql(this.getXNXQ()) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+2))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+3))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+4))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+5))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+6))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+7))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+8))) + "'," +
							"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
							"getDate(),'1')";
						sqlVec.add(sql);
					}
				}
				
				db.executeInsertOrUpdateTransaction(sqlVec);
			}
		}else{
			this.setMSG("没有符合条件的绩点统计信息");
		}
		
		return filePath;
	}
	
	/**
	 * 生成绩点统计文件
	 * @date:2016-08-11
	 * @param savePath 保存路径
	 * @param jdtjVec 绩点统计信息
	 * @author:yeq
	 */
	public boolean createJdtjFile(String savePath, Vector jdtjVec, String[] titleArray){
		boolean flag = true;
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		try {
			OutputStream os = new FileOutputStream(savePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			String cellContent = ""; //当前单元格的内容
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			String title = schoolName+this.getXNXQ()+"学年学分绩点表";
			
			//设置列宽
			wsheet.setColumnView(0, 12);
			wsheet.setColumnView(1, 12);
			wsheet.setColumnView(2, 12);
			wsheet.setColumnView(3, 8);
			wsheet.setColumnView(4, 10);
			wsheet.setColumnView(5, 13);
			wsheet.setColumnView(6, 20);
			wsheet.setRowView(1, 800);
			
			//标题
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			
			wsheet.mergeCells(0, 0, titleArray.length-1, 0);
			content = new Label(0, 0, title, contentStyle);
			wsheet.addCell(content);
				
			//表格标题行
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 12, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
				
			for (int j=0; j<titleArray.length; j++) {
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setWrap(true);// 自动换行
				
				if(j == 0){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}else if(j == titleArray.length-1){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}else{
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}
				content = new Label(j, 1, titleArray[j], contentStyle);
				wsheet.addCell(content);
			}
			
			//表格内容
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			
			//k:用于循环时Excel的行号
			//外层for用于循环行,内曾for用于循环列
			for (int i=0, k=2; i<jdtjVec.size(); i+=titleArray.length, k++) {
				for (int j=0; j<titleArray.length; j++) {
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setWrap(true);// 自动换行
					
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					if(j == 0){
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
					}else if(j == titleArray.length-1){
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
					}else{
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					}
					
					if(k == (jdtjVec.size()/titleArray.length+1)){
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
					}else{
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}
					
					content = new Label(j, k, jdtjVec.get(i+j) + "", contentStyle);
					wsheet.addCell(content);
				}
			}
			
			//写入文件
			wbook.write();
			wbook.close();
			os.close();
		} catch (RowsExceededException e1) {
			flag = false;
			// TODO Auto-generated catch block
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
			flag = false;
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
			flag = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 学生成绩单导出
	 * @date:2016-09-19
	 * @author:yeq
	 * @param startSemCode 起始学年学期编码
	 * @param endSemCode 结束学年学期编码
	 * @throws SQLException
	 */
	public String exportStuReport(String startSemCode, String endSemCode) throws SQLException{
		Vector vec = null;
		Vector wzcjVec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String filePath = "";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		sql = "select j.系名称,h.专业名称,g.行政班名称,a.姓名,a.学号,b.课程名称," +
			"case b.课程类型 when '1' then c.学分 " +
			"when '2' then d.学分 " +
			"when '3' then e.学分 " +
			"when '4' then k.学分 " +
			"else 0 end as 学分," +
			"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩,left(b.学年学期编码,5) as 学年学期 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_授课计划明细表 c on c.授课计划明细编号=a.相关编号 " +
			"left join V_排课管理_添加课程信息表 d on d.编号=a.相关编号 " +
			"left join V_规则管理_选修课授课计划明细表 e on e.授课计划明细编号=a.相关编号 " +
			"left join (select t.分层班编号,t1.学分 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号) k on k.分层班编号=a.相关编号 " +
			"left join V_学生基本数据子类 f on f.学号=a.学号 " +
			"left join V_学校班级数据子类 g on g.行政班代码=f.行政班代码 " +
			"left join V_专业基本信息数据子类 h on h.专业代码=g.专业代码 " +
			"left join V_基础信息_系专业关系表 i on i.专业代码=h.专业代码 " +
			"left join V_基础信息_系基础信息表 j on j.系代码=i.系代码 " +
			"where a.成绩状态 in ('1','2') ";
			//20170703去除异动学生过滤
			//"and f.学生状态 in ('01','05','07','08')";
		if(!"all".equalsIgnoreCase(startSemCode))
			sql += " and cast(b.学年学期编码 as int)>=" + startSemCode;
		if(!"all".equalsIgnoreCase(endSemCode))
			sql += " and cast(b.学年学期编码 as int)<=" + endSemCode;
		
		//判断是单个学生还是整班
		if(!"".equalsIgnoreCase(this.getSTUCODE())){
			sql += " and a.学号='" + MyTools.fixSql(this.getSTUCODE()) + "'";
		}else{
			sql += " and f.行政班代码='" + MyTools.fixSql(this.getBJMC()) + "'";
		}
		sql += " order by (case when f.班内学号='' then '9999' else f.班内学号 end),b.学年学期编码,b.课程名称";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//获取文字成绩代码信息
			sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1'";
			wzcjVec = db.GetContextVector(sql);
			
			String xmc = "";//系名称
			String zymc = "";//专业名称
			String bjmc = "";//班级名称
			String xm = "";//姓名
			String xh = "";//学号
			String kmmc = "";//科目名称
			String xf = "";//学分
			String cj = "";//成绩
			String xnxq = "";//学年学期
			int addNum = 0;
			int maxNum = 31;
			
			//整理数据
			for(int i=0; i<vec.size(); i+=9){
				kmmc = MyTools.StrFiltr(vec.get(i+5));
				xf = this.parseXf(MyTools.StrFiltr(vec.get(i+6)));
				cj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+7)));
				xnxq = MyTools.StrFiltr(vec.get(i+8));
				
				//判断是否同一门课程
				if(!xmc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !zymc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) 
					|| !bjmc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2))) || !xm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+3))) 
					|| !xh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4)))){
					if(i > 0){
						//判断成绩数据内容是否满足一页，不足的话补足
						addNum = 0;
						if(tempVec.size()%8 > 0){
							for(int a=0; a<4; a++){
								tempVec.add("");
							}
						}
						if(tempVec.size()/8 < maxNum){
							addNum = maxNum - tempVec.size()/8;
						}else if(tempVec.size()/8 > maxNum){
							addNum = maxNum+4 - tempVec.size()/8%maxNum;
						}
						if(addNum > 0){
							for(int a=0; a<addNum; a++){
								for(int b=0; b<8; b++){
									tempVec.add("");
								}
							}
						}
						resultVec.add(tempVec);
					}
					tempVec = new Vector();
					xmc = MyTools.StrFiltr(vec.get(i));
					zymc = MyTools.StrFiltr(vec.get(i+1));
					bjmc = MyTools.StrFiltr(vec.get(i+2));
					xm = MyTools.StrFiltr(vec.get(i+3));
					xh = MyTools.StrFiltr(vec.get(i+4));
					
					resultVec.add(xmc);
					resultVec.add(zymc);
					resultVec.add(bjmc);
					resultVec.add(xm);
					resultVec.add(xh);
				}
				
				tempVec.add(kmmc);
				tempVec.add(xf);
				tempVec.add(cj);
				tempVec.add(xnxq);
			}
			
			//判断成绩数据内容是否满足一页，不足的话补足
			addNum = 0;
			if(tempVec.size()%8 > 0){
				for(int i=0; i<4; i++){
					tempVec.add("");
				}
			}
			if(tempVec.size()/8 < maxNum){
				addNum = maxNum - tempVec.size()/8;
			}else if(tempVec.size()/8 > 30){
				addNum = maxNum+4 - tempVec.size()/8%maxNum;
			}
			if(addNum > 0){
				for(int i=0; i<addNum; i++){
					for(int j=0; j<8; j++){
						tempVec.add("");
					}
				}
			}
			resultVec.add(tempVec);
			
			String timeStr = "";
//			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
//			int year = c.get(Calendar.YEAR); 
//			int month = c.get(Calendar.MONTH); 
//			int date = c.get(Calendar.DATE);
//			timeStr = "_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date);
			
			String rootPath = MyTools.getProp(request, "Base.exportExcelPath");
			//判断是单个学生还是整班
			if(!"".equalsIgnoreCase(this.getSTUCODE())){
				rootPath += "scoreExport";
			}else{
				rootPath += "scoreExport/" + MyTools.StrFiltr(resultVec.get(2)) + timeStr;
			}
			String title = schoolName+"全日制高职各科成绩";
			String savePath = "";
			String[] titleArray = new String[]{"科目","学分","成绩","学期","科目","学分","成绩","学期"};
			
			//导出成绩单信息
			for(int a=0; a<resultVec.size(); a+=6){
				xmc = MyTools.StrFiltr(resultVec.get(a));
				zymc = MyTools.StrFiltr(resultVec.get(a+1));
				bjmc = MyTools.StrFiltr(resultVec.get(a+2));
				xm = MyTools.StrFiltr(resultVec.get(a+3));
				xh = MyTools.StrFiltr(resultVec.get(a+4));
				tempVec = (Vector)resultVec.get(a+5);
				
				savePath = rootPath;
				
				try {
					//创建
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
					}
					savePath = savePath + "/" + xh + "--"+ xm + timeStr + ".xls";
					//输出流
					OutputStream os = new FileOutputStream(savePath);
					WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
					
					this.exportSingleReport(wbook, tempVec, titleArray, title, xmc, zymc, bjmc, xm, xh);
					
					//写入文件
					wbook.write();
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
			}
			
			//判断是单个学生还是整班
			if(!"".equalsIgnoreCase(this.getSTUCODE())){
				filePath = savePath;
			}else{
				filePath = rootPath+".zip";
				//打包文件
				this.compressZip(rootPath, filePath);
				//删除原文件
				this.deleteDirectory(rootPath);
			}
			
			this.setMSG("文件生成成功");
		}else{
			this.setMSG("没有符合条件的成绩信息");
		}
		
		return filePath;
	}
	
	/**
	 * 导出成绩xls
	 * @date:2016-09-19
	 * @author:yeq
	 * @param wbook
	 * @param scoreVec 成绩信息
	 * @param titleArray 表格标题数组
	 * @param title 标题
	 * @param xmc 系名称
	 * @param zymc 专业名称
	 * @param bjmc 班级名称
	 * @param xm 姓名
	 * @param xh 学号
	 * @throws WriteException, FileNotFoundException 
	 */
	public void exportSingleReport(WritableWorkbook wbook, Vector scoreVec, String[] titleArray, String title, String xmc, String zymc, String bjmc, String xm, String xh) throws IOException, WriteException, FileNotFoundException{
		WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
		WritableFont fontStyle;
		WritableCellFormat contentStyle;
		Label content;
		int curRowNum = 0;
		
		//设置列宽
		wsheet.setColumnView(0, 23);
		wsheet.setColumnView(1, 6);
		wsheet.setColumnView(2, 7);
		wsheet.setColumnView(3, 8);
		wsheet.setColumnView(4, 23);
		wsheet.setColumnView(5, 6);
		wsheet.setColumnView(6, 7);
		wsheet.setColumnView(7, 8);
		
		//标题
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
		
		wsheet.mergeCells(0, curRowNum, titleArray.length-1, curRowNum);
		content = new Label(0, curRowNum, title, contentStyle);
		wsheet.addCell(content);
		curRowNum++;
		
		//学生信息
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 11, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.LEFT);//左对齐
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
		
		wsheet.mergeCells(0, curRowNum, 2, curRowNum);
		content = new Label(0, curRowNum, "系："+MyTools.StrFiltr(xmc), contentStyle);
		wsheet.addCell(content);
		wsheet.mergeCells(3, curRowNum, 7, curRowNum);
		content = new Label(3, curRowNum, "专业："+MyTools.StrFiltr(zymc), contentStyle);
		wsheet.addCell(content);
		wsheet.setRowView(curRowNum, 400);
		curRowNum++;
		
		wsheet.mergeCells(0, curRowNum, titleArray.length-1, curRowNum);
		content = new Label(0, curRowNum, "班级："+MyTools.StrFiltr(bjmc), contentStyle);
		wsheet.addCell(content);
		wsheet.setRowView(curRowNum, 400);
		curRowNum++;
		
		wsheet.mergeCells(0, curRowNum, 2, curRowNum);
		content = new Label(0, curRowNum, "姓名："+MyTools.StrFiltr(xm), contentStyle);
		wsheet.addCell(content);
		wsheet.mergeCells(3, curRowNum, 7, curRowNum);
		content = new Label(3, curRowNum, "学号："+MyTools.StrFiltr(xh), contentStyle);
		wsheet.addCell(content);
		wsheet.setRowView(curRowNum, 400);
		curRowNum++;
		
		//表格标题行
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		
		for (int i=0; i<titleArray.length; i++) {
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			contentStyle.setWrap(true);// 自动换行
			
			contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
			if(i==0 || i==titleArray.length/2){
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}else if(i == titleArray.length-1){
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}else{
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}
			content = new Label(i, curRowNum, titleArray[i], contentStyle);
			wsheet.addCell(content);
		}
		curRowNum++;
		
		//表格内容
		//k:用于循环时Excel的行号
		//外层for用于循环行,内曾for用于循环列
		for (int i=0; i<scoreVec.size(); i+=titleArray.length) {
			for (int j=0; j<titleArray.length; j++) {
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				//contentStyle.setWrap(true);// 自动换行
				contentStyle.setShrinkToFit(true);//字体大小自适应
				
				if(j==0 || j==titleArray.length/2){
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
				}else if(j == titleArray.length-1){
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				}
				if((i+titleArray.length) == scoreVec.size()){
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}
				
				content = new Label(j, curRowNum, scoreVec.get(i+j) + "", contentStyle);
				wsheet.addCell(content);
			}
			
			wsheet.setRowView(curRowNum, 400);
			curRowNum++;
		}
	}
	
	/**
	 * 读取大补考信息列表数据
	 * @date:2016-10-13
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param zbwdfFlag 整班未登分判断
	 * @param yearRange 学年范围
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queDbkInfoList(int pageNum, int pageSize, String zbwdfFlag, String yearRange) throws SQLException {
		Vector vec = null; // 结果集
//		String sql = "select * from (select a.学号,a.姓名,d.行政班名称,case when c.课程类型='3' then c.课程名称+'（选修）' else c.课程名称 end as 课程名称," +
//				"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩 " +
//				"from V_成绩管理_学生成绩信息表 a " +
//				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
//				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
//				"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
//				"where b.学生状态 in ('01','05','07','08') and a.成绩状态='1' " +
//				"and c.学年学期编码='" + MyTools.fixSql(this.getXNXQ()) + "') as t " +
//				"where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')";
		String sql = "select * from (" +
			"select left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名," +
			"case when e.来源类型='4' then e.行政班名称 else d.行政班名称 end as 行政班名称," +
			"case when c.课程类型='3' then c.课程名称+'（选修）' else c.课程名称 end as 课程名称," +
			"case when d.年级代码=(cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-2 as varchar)+'1') then " +
			"(case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) " +
			"else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
			"left join V_成绩管理_登分教师信息表 e on e.相关编号=a.相关编号 " +
			"where b.学生状态 in ('01','05','08') and a.成绩状态='1' ";
		//判断导出的学年范围
		if("current".equalsIgnoreCase(yearRange)){
			sql += "and left(c.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' ";
		}else if("other".equalsIgnoreCase(yearRange)){
			sql += "and left(c.学年学期编码,4)<>'" + MyTools.fixSql(this.getXNXQ()) + "' ";
		}
		sql += "and d.年级代码 in (cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-2 as varchar)+'1'," +
			"cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-3 as varchar)+'1'," +
			"cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-4 as varchar)+'1')";
		if("exclude".equalsIgnoreCase(zbwdfFlag)){
			sql += " and a.相关编号 not in (select distinct 相关编号 from (select b.相关编号," +
				"case when b.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=b.相关编号) " +
				"when b.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=b.相关编号) " +
				"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.行政班代码) end as 需登分人数," +
				"(select count(*) from V_成绩管理_学生成绩信息表 " +
				"where 相关编号=a.相关编号 and ((case when 平时 in ('-1','-5') then '' else 平时 end)<>'' " +
				"or (case when 期中 in ('-1','-5') then '' else 期中 end)<>'' " +
				"or (case when 实训 in ('-1','-5') then '' else 实训 end)<>'' " +
				"or (case when 期末 in ('-1','-5') then '' else 期末 end)<>'' " +
				"or (case when 总评 in ('-1','-5') then '' else 总评 end)<>'' " +
				"or (case when 重修1 in ('-1','-5') then '' else 重修1 end)<>'' " +
				"or (case when 重修2 in ('-1','-5') then '' else 重修2 end)<>'' " +
				"or (case when 补考 in ('-1','-5') then '' else 补考 end)<>'' " +
				"or (case when 大补考 in ('-1','-5') then '' else 补考 end)<>'')) as 已登分人数 " +
				"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态='1' " +
				"and cast(left(c.学年学期编码,4) as int) between " + MyTools.fixSql(String.valueOf((MyTools.StringToInt(this.getXNXQ())-6))) + " and " + MyTools.fixSql(this.getXNXQ()) + ") as t " +
				"where 已登分人数=0)";
		}
		sql += ") as z where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')";
		
		if(!"".equalsIgnoreCase(this.getSTUCODE())){
			sql += " and 学号 like '%" + MyTools.fixSql(this.getSTUCODE()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getSTUNAME())){
			sql += " and 姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getBJMC())){
			sql += " and 行政班名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getKCMC())){
			sql += " and 课程名称 like '%" + MyTools.fixSql(this.getKCMC()) + "%'";
		}
		sql += " order by 学年学期,课程名称,行政班名称,学号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取分卷学科信息列表数据
	 * @date:2016-11-15
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param examType 考试类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSubInfoList(int pageNum, int pageSize, String examType) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select 主表编号,课程名称 from V_成绩管理_分卷信息主表 where 学年学期='" + MyTools.fixSql(this.getXNXQ()) + "' " +
				"and 考试类型='" + MyTools.fixSql(examType) + "'";
		
		if(!"".equalsIgnoreCase(this.getKCMC())){
			sql += " and 课程名称 like '%" + MyTools.fixSql(this.getKCMC()) + "%'";
		}
		sql += " order by 课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取可添加的学科下拉框数据
	 * @date:2016-11-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSubCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 课程名称 as comboName,课程名称 as comboValue " +
				"from V_成绩管理_科目课程信息表 " +
				"where 课程名称<>'' and 课程名称 not in (select 课程名称 from V_成绩管理_分卷信息主表 " +
				"where 学年学期='" + MyTools.fixSql(this.getXNXQ()) + "'";
		if(!"".equalsIgnoreCase(this.getKCMC())){
			sql += " and 主表编号<>'" + MyTools.fixSql(this.getKCMC()) + "'";
		}
		sql += ") order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 大补考名册导出
	 * @date:2016-10-13
	 * @author:yeq
	 * @param zbwdfFlag 整班未登分判断
	 * @param yearRange 学年范围
	 * @throws SQLException
	 */
	public String dbkmdExport(String zbwdfFlag, String yearRange) throws SQLException{
		Vector vec = null;
		Vector wzcjVec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String savePath = MyTools.getProp(request, "Base.exportExcelPath")+"resitExport";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		String filePath = savePath+"/"+schoolName+this.getXNXQ()+"学年"+"大补考考试名册.xls";

		//创建
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		OutputStream os;
		WritableWorkbook wbook;
		try {
			os = new FileOutputStream(filePath);
			wbook = Workbook.createWorkbook(os);
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			String[] title = new String[]{"编号","学号","姓名","班级","学年学期","学科","学分","补考分数","考生签名"};
			//int stunum=1;//补考学生行数
			//int stuxlh=1;//补考学生序号
			//int counum=0;//excel表中行数
			String cellContent = ""; //当前单元格的内容
			String[] numExchange = new String[]{"0", "○", "1", "一", "2", "二", "3", "三", "4", "四", "5", "五", "6", "六", "7", "七", "8", "八", "9", "九"};
			String xn = this.getXNXQ();
			
			for(int i=0; i<numExchange.length; i+=2){
				if(xn.indexOf(numExchange[i]) > -1){
					xn = xn.replaceAll(numExchange[i], numExchange[i+1]);
				}
			}
			
			sql = "select distinct 学号,姓名,行政班简称,学年学期,学科名称,课程名称,学分,'' as 补考分数,'' as 学生签名 " +
				"from (select left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,e.行政班简称," +
				//"case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 学科名称," +
				"case when b.来源类型='4' then b.行政班名称+'-'+b.课程名称 else b.课程名称+isnull(f.分卷名称,'') end as 学科名称," +
				"case when b.来源类型='4' then b.行政班名称+'-'+b.课程名称 when b.来源类型='3' then b.课程名称+isnull('（'+f.分卷名称+'）','')+'（选修）' else b.课程名称+isnull('（'+f.分卷名称+'）','') end as 课程名称," +
				"case b.来源类型 when '1' then (select 学分 from V_规则管理_授课计划明细表 where 授课计划明细编号=b.相关编号) " +
				"when '2' then (select 学分 from V_排课管理_添加课程信息表 where 编号=b.相关编号) " +
				"when '3' then (select 学分 from V_规则管理_选修课授课计划明细表 where 授课计划明细编号=b.相关编号) " +
				"when '4' then (select t1.学分 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号 where t.分层班编号=b.相关编号) " +
				"else 0 end as 学分," +
				"case when e.年级代码=(cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-2 as varchar)+'1') then " +
				"(case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) " +
				"else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
				"left join V_学生基本数据子类 d on d.学号=a.学号 " +
				"left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 " +
				"left join (select t1.课程名称,t2.分卷名称,t2.专业代码 from V_成绩管理_分卷信息主表 t1 " +
				"left join V_成绩管理_分卷信息明细表 t2 on t2.主表编号=t1.主表编号 where t1.考试类型='3' and t1.学年学期='" + MyTools.fixSql(this.getXNXQ()) + "') f " +
				"on f.课程名称=b.课程名称 and f.专业代码 like '%'+e.专业代码+'%' " +
				"where d.学生状态 in ('01','05','08') and 成绩状态='1' ";
			//判断导出的学年范围
			if("current".equalsIgnoreCase(yearRange)){
				sql += "and left(c.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' ";
			}else if("other".equalsIgnoreCase(yearRange)){
				sql += "and left(c.学年学期编码,4)<>'" + MyTools.fixSql(this.getXNXQ()) + "' ";
			}
			sql += "and e.年级代码 in (cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-2 as varchar)+'1'," +
				"cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-3 as varchar)+'1'," +
				"cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-4 as varchar)+'1')";
			if(!"all".equalsIgnoreCase(this.getNJDM()))
				sql += " and e.年级代码 in ('" + this.getNJDM().replaceAll(",", "','") + "')";
			if(!"all".equalsIgnoreCase(this.getZYMC()))
				sql += " and e.专业代码 in ('" + this.getZYMC().replaceAll(",", "','") + "')";
			if(!"all".equalsIgnoreCase(this.getBJMC()))
				sql += " and e.行政班代码 in ('" + this.getBJMC().replaceAll(",", "','") + "')";
			if("exclude".equalsIgnoreCase(zbwdfFlag)){
				sql += " and a.相关编号 not in (select distinct 相关编号 from (select b.相关编号," +
					"case when b.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=b.相关编号) " +
					"when b.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=b.相关编号) " +
					"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.行政班代码) end as 需登分人数," +
					"(select count(*) from V_成绩管理_学生成绩信息表 " +
					"where 相关编号=a.相关编号 and ((case when 平时 in ('-1','-5') then '' else 平时 end)<>'' " +
					"or (case when 期中 in ('-1','-5') then '' else 期中 end)<>'' " +
					"or (case when 实训 in ('-1','-5') then '' else 实训 end)<>'' " +
					"or (case when 期末 in ('-1','-5') then '' else 期末 end)<>'' " +
					"or (case when 总评 in ('-1','-5') then '' else 总评 end)<>'' " +
					"or (case when 重修1 in ('-1','-5') then '' else 重修1 end)<>'' " +
					"or (case when 重修2 in ('-1','-5') then '' else 重修2 end)<>'' " +
					"or (case when 补考 in ('-1','-5') then '' else 补考 end)<>'' " +
					"or (case when 大补考 in ('-1','-5') then '' else 补考 end)<>'')) as 已登分人数 " +
					"from V_成绩管理_学生成绩信息表 a left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
					"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态='1' " +
					"and cast(left(c.学年学期编码,4) as int) between " + MyTools.fixSql(String.valueOf((MyTools.StringToInt(this.getXNXQ())-6))) + " and " + MyTools.fixSql(this.getXNXQ()) + ") as t " +
					"where 已登分人数=0)";
			}
			sql += ") as z where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') " +
				"order by 学科名称,课程名称,学年学期,行政班简称,学号 ";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				//设置列宽
				wsheet.setColumnView(0, 4);
				wsheet.setColumnView(1, 10);
				wsheet.setColumnView(2, 8);
				wsheet.setColumnView(3, 8);
				wsheet.setColumnView(4, 6);
				wsheet.setColumnView(5, 28);
				wsheet.setColumnView(6, 6);
				wsheet.setColumnView(7, 6);
				wsheet.setColumnView(8, 11);
				
				String xnxq = "";
				String xk = "";
				int stuNum = 0;
				int curRowNum = 0;
				
				for(int i=0; i<vec.size(); i+=9){
					//判断是否同门学科(如果是分层班需判断是否同层级分层班)
					if(!xk.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4)))){
						if(xk.indexOf("-")<0 || MyTools.StrFiltr(vec.get(i+4)).indexOf("-")<0 || (this.parseFcbCourse(xk).equalsIgnoreCase(this.parseFcbCourse(MyTools.StrFiltr(vec.get(i+4)))) 
								&& !this.parseFcbName(xk).equalsIgnoreCase(this.parseFcbName(MyTools.StrFiltr(vec.get(i+4)))))){
							if(i > 0){
								//判断不满30行补足
								if(stuNum%30 > 0){
									cellContent = "";
									
									for(int num=0; num<(30-stuNum%30); num++){
										for(int colNum=0; colNum<title.length; colNum++){
											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
											contentStyle = new WritableCellFormat(fontStyle);
											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											
											//边框
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											if(colNum==0){
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											}else if(colNum == 8){
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											}
											if(num == (30-stuNum%30-1)){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
											
											content = new Label(colNum, curRowNum, cellContent, contentStyle);
											wsheet.addCell(content);
											wsheet.setRowView(curRowNum, 380);
										}
										curRowNum++;
									}
								}
								
								/**--------页面结尾栏 start-------*/
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								//第一行
								wsheet.mergeCells(3, curRowNum, 6, curRowNum);
								cellContent="阅卷教师签名 ：__________";
								content = new Label(3, curRowNum, cellContent, contentStyle);
								wsheet.addCell(content);
								wsheet.setRowView(curRowNum, 420);
								curRowNum++;
								//第二行
								wsheet.mergeCells(3, curRowNum, 6, curRowNum);
								cellContent="阅 卷 日 期  ：__________";
								content = new Label(3, curRowNum, cellContent, contentStyle);
								wsheet.addCell(content);
								wsheet.setRowView(curRowNum, 300);
								curRowNum++;
								//第三行
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								wsheet.mergeCells(1, curRowNum, 6, curRowNum);
								cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
								content = new Label(1, curRowNum, cellContent, contentStyle);
								wsheet.addCell(content);
								wsheet.setRowView(curRowNum, 420);
								curRowNum++;
								/**--------页面结尾栏 end-------*/
							}
							
							/**----------标题栏 start ----------*/
							//第一行
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							wsheet.mergeCells(0, curRowNum, title.length-1, curRowNum);
							cellContent = schoolName+(MyTools.StringToInt(this.getXNXQ().substring(2))-2)+"级毕业前大补考";
							content = new Label(0, curRowNum, cellContent, contentStyle);
							wsheet.addCell(content);
							wsheet.setRowView(curRowNum, 440);
							curRowNum++;
							
							//第二行
							wsheet.mergeCells(0, curRowNum, title.length-1, curRowNum);
							cellContent="补 考 名 册";
							content = new Label(0, curRowNum, cellContent, contentStyle);
							wsheet.addCell(content);
							wsheet.setRowView(curRowNum, 480);
							curRowNum++;
							
							//第三行
							for(int colNum=0; colNum<title.length; colNum++){
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setWrap(true);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
								//边框
								if(colNum == 0){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}else if(colNum == 8){
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
								content = new Label(colNum, curRowNum, cellContent, contentStyle);
								wsheet.addCell(content);
							}
							wsheet.setRowView(curRowNum, 620);
							curRowNum++;
							/**----------标题栏 end ----------*/
							
							stuNum = 0;
							xnxq = MyTools.StrFiltr(vec.get(i+3));
							xk = MyTools.StrFiltr(vec.get(i+4));
						}
					}
					
					//表格内容
					stuNum++;
					for(int colNum=0; colNum<title.length; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setShrinkToFit(true);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent = MyTools.StrFiltr(stuNum);
						}else{
							if(colNum < 5){
								cellContent = MyTools.StrFiltr(vec.get(i+colNum-1));
							}else{
								cellContent = MyTools.StrFiltr(vec.get(i+colNum));
							}
							
							if(colNum == 6){
								cellContent = this.parseXf(cellContent);
							}
						}
						
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						
						//边框
						if(colNum == 0){
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						}else if(colNum == 8){
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						}
						
						if(stuNum%30 == 0){
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}
						
						content = new Label(colNum, curRowNum, cellContent, contentStyle);
						wsheet.addCell(content);
						wsheet.setRowView(curRowNum, 380);
					}
					curRowNum++;
					
					//判断如果满30行执行换页操作
					if(stuNum>0 && stuNum%30==0 && (xk.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+9+4))) 
						|| (xk.indexOf("-")>-1 
								&& MyTools.StrFiltr(vec.get(i+9+4)).indexOf("-")>-1 
								&& this.parseFcbCourse(xk).equalsIgnoreCase(this.parseFcbCourse(MyTools.StrFiltr(vec.get(i+9+4)))) 
								&& this.parseFcbName(xk).equalsIgnoreCase(this.parseFcbName(MyTools.StrFiltr(vec.get(i+9+4))))))){
						/**--------页面结尾栏 start-------*/
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						//行1
						wsheet.mergeCells(3, curRowNum, 6, curRowNum);
						cellContent="阅卷教师签名 ：__________";
						content = new Label(3, curRowNum, cellContent, contentStyle);
						wsheet.addCell(content);
						wsheet.setRowView(curRowNum, 420);
						curRowNum++;
						//行2
						wsheet.mergeCells(3, curRowNum, 6, curRowNum);
						cellContent="阅 卷 日 期  ：__________";
						content = new Label(3, curRowNum, cellContent, contentStyle);
						wsheet.addCell(content);
						wsheet.setRowView(curRowNum, 300);
						curRowNum++;
						//行3
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						wsheet.mergeCells(1, curRowNum, 6, curRowNum);
						cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
						content = new Label(1, curRowNum, cellContent, contentStyle);
						wsheet.addCell(content);
						wsheet.setRowView(curRowNum, 420);
						curRowNum++;
						/**--------页面结尾栏 end-------*/
						
						/**----------标题栏 start ----------*/
						//第一行
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						wsheet.mergeCells(0, curRowNum, title.length-1, curRowNum);
						cellContent = schoolName+(MyTools.StringToInt(this.getXNXQ().substring(2))-2)+"级毕业前大补考";
						content = new Label(0, curRowNum, cellContent, contentStyle);
						wsheet.addCell(content);
						wsheet.setRowView(curRowNum, 440);
						curRowNum++;
						//第二行
						wsheet.mergeCells(0, curRowNum, title.length-1, curRowNum);
						cellContent="补 考 名 册";
						content = new Label(0, curRowNum, cellContent, contentStyle);
						wsheet.addCell(content);
						wsheet.setRowView(curRowNum, 480);
						curRowNum++;
						//第三行
						for(int colNum=0; colNum<title.length; colNum++){
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setWrap(true);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							
							//边框
							if(colNum == 0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}else if(colNum == 8){
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
							content = new Label(colNum, curRowNum, cellContent, contentStyle);
							wsheet.addCell(content);	
						}
						wsheet.setRowView(curRowNum, 620);
						curRowNum++;
						/**----------标题栏 end ----------*/
					}
				}
				
				//判断不满30行补足
				if(stuNum%30 > 0){
					cellContent = "";
					
					for(int num=0; num<(30-stuNum%30); num++){
						for(int colNum=0; colNum<title.length; colNum++){
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							
							//边框
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							if(colNum==0){
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							}else if(colNum == 8){
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							}
							if(num == (30-stuNum%30-1)){
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
							
							content = new Label(colNum, curRowNum, cellContent, contentStyle);
							wsheet.addCell(content);
							wsheet.setRowView(curRowNum, 380);
						}
						curRowNum++;
					}
				}
				
				/**--------页面结尾栏 start-------*/
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				//第一行
				wsheet.mergeCells(3, curRowNum, 6, curRowNum);
				cellContent="阅卷教师签名 ：__________";
				content = new Label(3, curRowNum, cellContent, contentStyle);
				wsheet.addCell(content);
				wsheet.setRowView(curRowNum, 420);
				curRowNum++;
				//第二行
				wsheet.mergeCells(3, curRowNum, 6, curRowNum);
				cellContent="阅 卷 日 期  ：__________";
				content = new Label(3, curRowNum, cellContent, contentStyle);
				wsheet.addCell(content);
				wsheet.setRowView(curRowNum, 300);
				curRowNum++;
				//第三行
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				wsheet.mergeCells(1, curRowNum, 6, curRowNum);
				cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
				content = new Label(1, curRowNum, cellContent, contentStyle);
				wsheet.addCell(content);
				wsheet.setRowView(curRowNum, 420);
				/**--------页面结尾栏 end-------*/
	
				//写入文件
				wbook.write();
				wbook.close();
				os.close();
				this.setMSG("文件生成成功");
				
				//添加大补考登分教师信息
				sql = "insert into V_成绩管理_大补考登分教师信息表 (大补考学年,来源类型,专业代码,课程名称,登分教师编号,登分教师姓名,创建人,创建时间,状态) " +
					"select distinct z.大补考学年,z.来源类型,z.专业代码,z.课程名称," +
					"isnull(case when z.来源类型 in ('3','4') then z.登分教师编号 else y.教师代码 end,'') as 登分教师编号," +
					"isnull(case when z.来源类型 in ('3','4') then z.登分教师姓名 else y.教师姓名 end,'') as 登分教师姓名," +
					"'" + MyTools.fixSql(this.getUSERCODE()) + "',getDate(),'1' from (" +
					"select '" + MyTools.fixSql(this.getXNXQ()) + "' as 大补考学年,来源类型," +
					"case when t.来源类型='3' then '' else t.专业代码 end as 专业代码,t.课程名称,t.相关编号,t.登分教师编号,t.登分教师姓名 from (" +
					"select c.专业代码,b.来源类型,b.课程代码,b.课程名称,case when e.年级代码=(cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-2 as varchar)+'1') " +
					"then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) " +
					"else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩,a.相关编号,b.登分教师编号,b.登分教师姓名 " +
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
					"left join V_学生基本数据子类 d on d.学号=a.学号 " +
					"left join V_学校班级数据子类 e on e.行政班代码=d.行政班代码 " +
					"where d.学生状态 in ('01','05','08') and 成绩状态='1' ";
				//判断导出的学年范围
				if("current".equalsIgnoreCase(yearRange)){
					sql += "and left(c.学年学期编码,4)='" + MyTools.fixSql(this.getXNXQ()) + "' ";
				}else if("other".equalsIgnoreCase(yearRange)){
					sql += "and left(c.学年学期编码,4)<>'" + MyTools.fixSql(this.getXNXQ()) + "' ";
				}
				sql += "and e.年级代码 in (cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-2 as varchar)+'1', " +
					"cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-3 as varchar)+'1'," +
					"cast(cast(right('" + MyTools.fixSql(this.getXNXQ()) + "',2) as int)-4 as varchar)+'1')) as t " +
					"where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')) as z " +
					"left join V_专业组组长信息 y on y.专业代码=z.专业代码 " +
					"left join V_成绩管理_大补考登分教师信息表 x on x.大补考学年=z.大补考学年 and x.专业代码=z.专业代码 and x.来源类型=z.来源类型 and x.课程名称=z.课程名称 " +
					"where x.编号 is null " +
					"order by z.来源类型,z.课程名称";
				db.executeInsertOrUpdate(sql);
			}else{
				this.setMSG("没有相关大补考信息");
			}
		} catch (RowsExceededException e1) {
			// TODO Auto-generated catch block
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/**
	 * 查询覆盖成绩的科目下拉框数据
	 * @date:2016-11-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCoverSubject() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName " +
				"union all " +
				"select distinct 课程名称,课程名称 from V_成绩管理_科目课程信息表 where 课程名称<>'' order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取覆盖学生成绩预览信息
	 * @date:2016-11-15
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param subName 科目名称
	 * @param scoreType 成绩类型
	 * @param scoreStart 起始分数
	 * @param scoreEnd 结束分数
	 * @param scoreTarget 目标成绩
	 * @param stuStart 起始学号
	 * @param stuEnd 结束学号
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queStuScoreList(int pageNum, int pageSize, String subName, String scoreType, String scoreStart, String scoreEnd, String scoreTarget, String stuStart, String stuEnd) throws SQLException {
		Vector vec = null; // 结果集
		String tempField = "";
		String sql = "";
		
		if("0".equalsIgnoreCase(scoreType)){
			tempField = "总评";
		}else if("1".equalsIgnoreCase(scoreType)){
			tempField = "补考";
		}else{
			tempField = "大补考";
		}
		
		sql = "select a.学号,a.姓名,b.行政班名称,left(c.学年学期编码,5) as 学年学期,b.课程名称,a." + tempField + ",'" + scoreTarget + "' as 覆盖成绩 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
			"where 1=1";
		
		if(!"".equalsIgnoreCase(subName)){
			sql += " and b.课程名称='" + MyTools.fixSql(subName) +"'";
		}
		if(!"".equalsIgnoreCase(scoreStart)){
			sql += " and cast(a." + tempField + " as float)>=" + scoreStart + "";
		}
		if(!"".equalsIgnoreCase(scoreEnd)){
			sql += " and cast(a." + tempField + " as float)<=" + scoreEnd + "";
		}
		if(!"".equalsIgnoreCase(stuStart)){
			sql += " and cast(a.学号 as bigint)>=" + stuStart + "";
		}
		if(!"".equalsIgnoreCase(stuEnd)){
			sql += " and cast(a.学号 as bigint)<=" + stuEnd + "";
		}
		sql += " order by c.学年学期编码,a.学号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 覆盖成绩
	 * @date:2016-11-15
	 * @author: yeq
	 * @param subName 科目名称
	 * @param scoreType 成绩类型
	 * @param scoreStart 起始分数
	 * @param scoreEnd 结束分数
	 * @param scoreTarget 目标成绩
	 * @param stuStart 起始学号
	 * @param stuEnd 结束学号
	 * @throws SQLException
	 */
	public void confirmScoreCover(String subName, String scoreType, String scoreStart, String scoreEnd, String scoreTarget, String stuStart, String stuEnd) throws SQLException{
		String tempField = "";
		String sql = "";
		
		if("0".equalsIgnoreCase(scoreType)){
			tempField = "总评";
		}else if("1".equalsIgnoreCase(scoreType)){
			tempField = "补考";
		}else{
			tempField = "大补考";
		}
		sql = "update V_成绩管理_学生成绩信息表 set " + tempField + "='" + scoreTarget + "' " +
			"where 编号 in (";
		sql += "select a.编号 from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
			"where 1=1";
		
		if(!"".equalsIgnoreCase(subName)){
			sql += " and b.课程名称='" + MyTools.fixSql(subName) +"'";
		}
		if(!"".equalsIgnoreCase(scoreStart)){
			sql += " and cast(a." + tempField + " as float)>=" + scoreStart + "";
		}
		if(!"".equalsIgnoreCase(scoreEnd)){
			sql += " and cast(a." + tempField + " as float)<=" + scoreEnd + "";
		}
		if(!"".equalsIgnoreCase(stuStart)){
			sql += " and cast(a.学号 as bigint)>=" + stuStart + "";
		}
		if(!"".equalsIgnoreCase(stuEnd)){
			sql += " and cast(a.学号 as bigint)<=" + stuEnd + "";
		}
		sql += ")";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("覆盖成功");
		}else{
			this.setMSG("覆盖失败");
		}
	}
	
	/**
	 * 保存分卷学科
	 * @date:2016-11-16
	 * @author: yeq
	 * @param mainCode 主表编号
	 * @param examType 考试类型
	 * @param subName 学科名称
	 * @throws SQLException
	 */
	public void saveSub(String mainCode, String examType, String subName) throws SQLException{
		String sql = "";
		
		if("".equalsIgnoreCase(mainCode)){
			String id = db.getMaxID("V_成绩管理_分卷信息主表", "主表编号", "FJBH_", 6);
			
			sql = "insert into V_成绩管理_分卷信息主表 (主表编号,考试类型,学年学期,课程代码,课程名称,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(id) + "'," +
				"'" + MyTools.fixSql(examType) + "'," +
				"'" + MyTools.fixSql(this.getXNXQ()) + "'," +
				"''," +
				"'" + MyTools.fixSql(subName) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}else{
			sql = "update V_成绩管理_分卷信息主表 set 课程名称='" + MyTools.fixSql(subName) + "' " +
				"where 主表编号='" + MyTools.fixSql(mainCode) + "'";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除分卷学科
	 * @date:2016-11-16
	 * @author: yeq
	 * @throws SQLException
	 */
	public void delSub() throws SQLException{
		Vector sqlVec = new Vector();
		String sql = "";
		
		sql = "delete from V_成绩管理_分卷信息主表 where 主表编号='" + MyTools.fixSql(this.getCODE()) + "'";
		sqlVec.add(sql);
		
		sql = "delete from V_成绩管理_分卷信息明细表 where 主表编号='" + MyTools.fixSql(this.getCODE()) + "'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 读取分卷学科详情信息列表数据
	 * @date:2016-11-16
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSubDetailList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select 明细编号,分卷名称,replace(专业代码,'@','') as 专业代码,replace(专业名称,'@','') as 专业名称 " +
				"from V_成绩管理_分卷信息明细表 where 主表编号='" + MyTools.fixSql(this.getCODE()) + "' order by 分卷名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取可添加的专业下拉框数据
	 * @date:2016-11-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName " +
				"union all " +
				"select 专业代码,专业名称+'（'+专业代码+'）' from V_专业基本信息数据子类 " +
				"where len(专业代码)>3 and 专业代码 not in (" +
				"select a.专业代码 from V_专业基本信息数据子类 a " +
				"inner join V_成绩管理_分卷信息明细表 b on b.专业代码 like '%'+a.专业代码+'%' " +
				"where b.主表编号='" + MyTools.fixSql(this.getCODE()) + "'";
		if(!"".equalsIgnoreCase(this.getZYMC())){
			sql += " and b.明细编号<>'" + MyTools.fixSql(this.getZYMC()) + "'";
		}
		sql += ") order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 保存分卷学科明细
	 * @date:2016-11-16
	 * @author: yeq
	 * @param mainCode 主表编号
	 * @param examType 考试类型
	 * @param subName 学科名称
	 * @throws SQLException
	 */
	public void saveSubDetail(String mainCode, String detailCode, String majorCode, String majorName) throws SQLException{
		Vector vec = null;
		String sql = "";
		majorCode = "@"+majorCode.replaceAll(",", "@,@")+"@";
		majorName = "@"+majorName.replaceAll(",", "@,@")+"@";
		
		if("".equalsIgnoreCase(detailCode)){
			String id = db.getMaxID("V_成绩管理_分卷信息明细表", "明细编号", "FJMXBH_", 6);
			String tempName = "";
			
			//生成分卷名称
			sql = "select right(分卷名称,len(分卷名称)-2) from V_成绩管理_分卷信息明细表 where 主表编号='" + MyTools.fixSql(mainCode) + "' order by 分卷名称 desc";
			vec = db.GetContextVector(sql);
			if(vec!=null && vec.size()>0){
				tempName = "分卷"+(MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)))+1);
			}else{
				tempName = "分卷1";
			}
			
			sql = "insert into V_成绩管理_分卷信息明细表 (明细编号,主表编号,分卷名称,专业代码,专业名称,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(id) + "'," +
				"'" + MyTools.fixSql(mainCode) + "'," +
				"'" + MyTools.fixSql(tempName) + "'," +
				"'" + MyTools.fixSql(majorCode) + "'," +
				"'" + MyTools.fixSql(majorName) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}else{
			sql = "update V_成绩管理_分卷信息明细表 set 专业代码='" + MyTools.fixSql(majorCode) + "'," +
				"专业名称='" + MyTools.fixSql(majorName) + "' " +
				"where 主表编号='" + MyTools.fixSql(mainCode) + "'";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除分卷学科明细信息
	 * @date:2016-11-16
	 * @author: yeq
	 * @throws SQLException
	 */
	public void delSubDetail() throws SQLException{
		String sql = "delete from V_成绩管理_分卷信息明细表 where 明细编号='" + MyTools.fixSql(this.getCODE()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 整班未登分信息导出
	 * @date:2016-11-16
	 * @author:yeq
	 * @param startSemCode 起始学年学期编码
	 * @param endSemCode 结束学年学期编码
	 * @param gradeCode 年级代码
	 * @param examWeek 考试周次
	 * @throws SQLException
	 */
	public String zbwdfExport(String qsxnxq, String jsxnxq, String nj, String xb,String zy) throws SQLException{
		Vector vec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String filePath = "";
		
		sql = "select 行政班名称,课程名称,学年学期,登分教师姓名,需登分人数,已登分人数,相关编号,实际数据条数 " +
			"from (select distinct a.行政班名称,case when a.来源类型='1' then c.系部代码 else '' end 系代码,"+
			"case when a.来源类型='1' then c.专业代码 else '' end 专业代码,a.相关编号,a.来源类型,a.课程名称,left(b.学年学期编码,5) as 学年学期,replace(a.登分教师姓名,'@','') as 登分教师姓名," +
			"case when a.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=a.相关编号) " +
			"when a.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=a.相关编号) " +
			"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=a.行政班代码) end as 需登分人数," +
			"(select count(*) from V_成绩管理_学生成绩信息表 where 状态='1' and 成绩状态='1' and 相关编号=a.相关编号 " +
			"and ((case when 平时 in ('-1','-5') then '' else 平时 end)<>'' or (case when 期中 in ('-1','-5') then '' else 期中 end)<>'' " +
			"or (case when 实训 in ('-1','-5') then '' else 实训 end)<>'' or (case when 期末 in ('-1','-5') then '' else 期末 end)<>'' " +
			"or (case when 总评 in ('-1','-5') then '' else 总评 end)<>'' or (case when 重修1 in ('-1','-5') then '' else 重修1 end)<>'' " +
			"or (case when 重修2 in ('-1','-5') then '' else 重修2 end)<>'' or (case when 补考 in ('-1','-5') then '' else 补考 end)<>'' " +
			"or (case when 大补考 in ('-1','-5') then '' else 补考 end)<>'')) as 已登分人数," +
			"(select count(*) from V_成绩管理_学生成绩信息表 where 状态='1' and 成绩状态='1' and 相关编号=a.相关编号) as 实际数据条数 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join (select 行政班代码,行政班名称,年级代码,系部代码,专业代码 from V_学校班级数据子类"+ 
			" union all "+ 
			"select 教学班编号,教学班名称,年级代码,系部代码,专业代码 from V_基础信息_教学班信息表) as c on a.行政班代码=c.行政班代码 "+
			"where a.状态='1' and b.状态='1'";
			/*if(!"all".equalsIgnoreCase(startSemCode))
				sql += " and cast(b.学年学期编码 as int)>=" + startSemCode;
			if(!"all".equalsIgnoreCase(endSemCode))
				sql += " and cast(b.学年学期编码 as int)<=" + endSemCode;
			if(!"all".equalsIgnoreCase(gradeCode))
				sql += " and b.年级代码 in ('" + gradeCode.replaceAll(",", "','") + "')";
			sql += ") as t where 已登分人数=0";
			if(!"all".equalsIgnoreCase(examWeek)){
				sql += " and 相关编号 in (select 授课计划明细编号 from V_考试管理_考试设置 where 期中期末='" + MyTools.fixSql(examWeek) + "')";
			}
			sql += " order by 学年学期,来源类型 desc,课程名称,行政班名称";*/
		
		if(!"all".equalsIgnoreCase(qsxnxq))
			sql += " and cast(b.学年学期编码 as int)>=" + qsxnxq;
		if(!"all".equalsIgnoreCase(jsxnxq))
			sql += " and cast(b.学年学期编码 as int)<=" + jsxnxq;
		if(!"all".equalsIgnoreCase(nj))
			sql += " and b.年级代码 in ('" + nj.replaceAll(",", "','") + "')";
		sql += ") as t where 已登分人数=0";
		if(!"all".equalsIgnoreCase(xb)){
			sql += " and 系代码  in ('" + xb.replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(zy)){
			sql += " and 专业代码  in ('" + zy.replaceAll(",", "','") + "')";
		}
		sql += " order by 学年学期,来源类型 desc,课程名称,行政班名称";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//生成整班未登分的学生成绩初始化信息
			for(int i=0; i<vec.size(); i+=8){
				//判断如果需登分人数>0并且未生成过成绩基础数据
				if(!"0".equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) && "0".equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+7)))){
					DfryszBean.addStuScoreInfo(request, MyTools.StrFiltr(vec.get(i+6)));
				}
				vec.remove(i+6);
				vec.remove(i+6);
				i-=2;
			}
			
			String title = "";
			String[] titleArray = new String[]{"行政班名称","课程名称","学年学期","登分教师姓名","需登分人数","已登分人数"};
			
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);

			filePath = MyTools.getProp(request, "Base.exportExcelPath")+"scoreExport";

			// 创建
			try {
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				filePath += "/整班未登分信息" + year+ ((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+ (date < 10 ? "0" + date : date) + ".xls";
				OutputStream os = new FileOutputStream(filePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;

				// 设置列宽
				wsheet.setColumnView(0, 50);
				wsheet.setColumnView(1, 50);
				wsheet.setColumnView(2, 15);
				wsheet.setColumnView(3, 30);
				wsheet.setColumnView(4, 15);
				wsheet.setColumnView(5, 15);
				
				// 设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 16,WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				contentStyle.setShrinkToFit(true);//字体大小自适应
				
				for (int i=0; i<titleArray.length; i++) {
					// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
					// 在Label对象的子对象中指明单元格的位置和内容
					content = new Label(i, 0, titleArray[i], contentStyle);
					// 将定义好的单元格添加到工作表中
					wsheet.addCell(content);
				}
				
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				contentStyle.setShrinkToFit(true);//字体大小自适应

				// 填充数据
				// k:用于循环时Excel的行号
				// 外层for用于循环行,内曾for用于循环列'
				  for(int i=0,k=1; i<vec.size();i+=titleArray.length,k++){ 
					  for(int j=0;j<titleArray.length;j++){ 
						  content = new Label(j, k, vec.get(i+j) + "",contentStyle);
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
			
			this.setMSG("成绩文件生成成功");
		}else{
			this.setMSG("没有符合条件的成绩信息");
		}
		
		return filePath;
	}
	
	/**
	 * 读取学年下拉框(整班未登分导出学年学期)
	 * @date:2016-05-10
	 * @author:Mowei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadzbXnXxCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " + 
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,'1' as orderNum " +
				"from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询删除整班成绩中班级信息
	 * @date:2016-12-01
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadDelClassCombo() throws SQLException{
		Vector vec = null;
		String sql = "";
		
		//判断班级类型
		if("xzb".equalsIgnoreCase(this.getZYMC())){
			sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 行政班名称,行政班代码 " +
				"from V_学校班级数据子类 where 状态='1'";
		}else if("fcb".equalsIgnoreCase(this.getZYMC())){
			sql = "select '' as comboValue,'请选择' as comboName,0 as orderNum " +
				"union all " +
				"select distinct a.相关编号,a.行政班名称,1 from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.来源类型='4'";
			if(!"all".equalsIgnoreCase(this.getXNXQ())){
				sql += " and b.学年学期编码 in ('" + this.getXNXQ().replaceAll(",", "','") + "')";
			}
			sql += " order by orderNum,comboName";
		}else{
			sql = "select '' as comboValue,'请选择' as comboName,0 as orderNum " +
				"union all " +
				"select distinct a.相关编号,a.行政班名称,1 from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.来源类型='3'";
			if(!"all".equalsIgnoreCase(this.getXNXQ())){
				sql += " and b.学年学期编码 in ('" + this.getXNXQ().replaceAll(",", "','") + "')";
			}
			sql += " order by orderNum,comboName";
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询删除整班成绩中班级相关的课程
	 * @date:2016-12-01
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadDelCourseCombo() throws SQLException{
		Vector vec = null;
		String sql = "";
		
		//判断班级类型
		if("xzb".equalsIgnoreCase(this.getZYMC())){
			sql = "select '' as comboValue,'请选择' as comboName,0 as orderNum " +
				"union all " +
				"select distinct a.相关编号,a.课程名称,1 from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where 1=1";
			if(!"all".equalsIgnoreCase(this.getXNXQ())){
				sql += " and b.学年学期编码 in ('" + this.getXNXQ().replaceAll(",", "','") + "')";
			}
			sql += " and a.行政班代码='" + MyTools.fixSql(this.getBJMC()) + "' " +
				"order by orderNum,comboName";
		}else{
			sql = "select '' as comboValue,'请选择' as comboName,0 as orderNum " +
				"union all " + 
				"select distinct 相关编号,课程名称,1 from V_成绩管理_登分教师信息表 where 相关编号='" + MyTools.fixSql(this.getBJMC()) + "' " +
				"order by orderNum,comboName";
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程整班学生成绩信息
	 * @date:2016-12-01
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queDelClassScoreList() throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select a.编号,d.班内学号,a.学号,a.姓名,b.行政班名称,left(c.学年学期编码,5) as 学年学期,b.课程名称,a.平时,a.期中,a.实训,a.期末,a.总评,a.重修1,a.重修2,a.补考,a.大补考 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
			"left join V_学生基本数据子类 d on d.学号=a.学号 " +
			"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态 in ('1','2') " +
			"and a.相关编号='" + MyTools.fixSql(this.getCODE()) + "' " +
			"order by (case when d.班内学号='' then '9999' else d.班内学号 end)";
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取整班未登分信息
	 * @date:2017-5-10
	 * @author:Maowei
	 * @param XnXq
	 * @return Vector
	 * @throws SQLException 
	 */
	public Vector queZbwdf(int pageNum,int pageSize,String XnXq) throws SQLException {
		Vector vec =null;
		String sql = "select 行政班名称,课程名称,学年学期,学年学期名称,登分教师姓名,需登分人数,已登分人数,相关编号,实际数据条数 " +
				"from (select distinct a.行政班名称,a.相关编号,a.来源类型,a.课程名称,left(b.学年学期编码,5) as 学年学期,b.学年学期名称,replace(a.登分教师姓名,'@','') as 登分教师姓名," +
				"case when a.来源类型='3' then (select count(*) from V_规则管理_学生选修课关系表 where 授课计划明细编号=a.相关编号) " +
				"when a.来源类型='4' then (select count(*) from V_规则管理_分层班学生信息表 where 分层班编号=a.相关编号) " +
				"else (select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=a.行政班代码) end as 需登分人数," +
				"(select count(*) from V_成绩管理_学生成绩信息表 where 状态='1' and 成绩状态='1' and 相关编号=a.相关编号 " +
				"and ((case when 平时 in ('-1','-5') then '' else 平时 end)<>'' or (case when 期中 in ('-1','-5') then '' else 期中 end)<>'' " +
				"or (case when 实训 in ('-1','-5') then '' else 实训 end)<>'' or (case when 期末 in ('-1','-5') then '' else 期末 end)<>'' " +
				"or (case when 总评 in ('-1','-5') then '' else 总评 end)<>'' or (case when 重修1 in ('-1','-5') then '' else 重修1 end)<>'' " +
				"or (case when 重修2 in ('-1','-5') then '' else 重修2 end)<>'' or (case when 补考 in ('-1','-5') then '' else 补考 end)<>'' " +
				"or (case when 大补考 in ('-1','-5') then '' else 补考 end)<>'')) as 已登分人数," +
				"(select count(*) from V_成绩管理_学生成绩信息表 where 状态='1' and 成绩状态='1' and 相关编号=a.相关编号) as 实际数据条数 " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1'";
		if(!XnXq.equalsIgnoreCase("")){
			sql += " and b.学年学期编码  =" + MyTools.fixSql(XnXq);
		}
		sql += ") as t where 已登分人数=0";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
 	}
	
	//GET && SET 方法
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

	public String getCODE() {
		return CODE;
	}

	public void setCODE(String cODE) {
		CODE = cODE;
	}

	public String getSTUCODE() {
		return STUCODE;
	}

	public void setSTUCODE(String sTUCODE) {
		STUCODE = sTUCODE;
	}

	public String getSTUNAME() {
		return STUNAME;
	}

	public void setSTUNAME(String sTUNAME) {
		STUNAME = sTUNAME;
	}

	public String getNJDM() {
		return NJDM;
	}

	public void setNJDM(String nJDM) {
		NJDM = nJDM;
	}

	public String getZYMC() {
		return ZYMC;
	}

	public void setZYMC(String zYMC) {
		ZYMC = zYMC;
	}

	public String getBJMC() {
		return BJMC;
	}

	public void setBJMC(String bJMC) {
		BJMC = bJMC;
	}

	public String getXNXQ() {
		return XNXQ;
	}

	public void setXNXQ(String xNXQ) {
		XNXQ = xNXQ;
	}

	public String getKCMC() {
		return KCMC;
	}

	public void setKCMC(String kCMC) {
		KCMC = kCMC;
	}

	public String getJSXM() {
		return JSXM;
	}

	public void setJSXM(String jSXM) {
		JSXM = jSXM;
	}

	public String getCJFW() {
		return CJFW;
	}

	public void setCJFW(String cJFW) {
		CJFW = cJFW;
	}

	public String getCJLX() {
		return CJLX;
	}

	public void setCJLX(String cJLX) {
		CJLX = cJLX;
	}

	public String getPSCJ() {
		return PSCJ;
	}

	public void setPSCJ(String pSCJ) {
		PSCJ = pSCJ;
	}

	public String getQZCJ() {
		return QZCJ;
	}

	public void setQZCJ(String qZCJ) {
		QZCJ = qZCJ;
	}

	public String getSXCJ() {
		return SXCJ;
	}

	public void setSXCJ(String sXCJ) {
		SXCJ = sXCJ;
	}

	public String getQMCJ() {
		return QMCJ;
	}

	public void setQMCJ(String qMCJ) {
		QMCJ = qMCJ;
	}

	public String getZPCJ() {
		return ZPCJ;
	}

	public void setZPCJ(String zPCJ) {
		ZPCJ = zPCJ;
	}

	public String getCXCJ1() {
		return CXCJ1;
	}

	public void setCXCJ1(String cXCJ1) {
		CXCJ1 = cXCJ1;
	}

	public String getCXCJ2() {
		return CXCJ2;
	}

	public void setCXCJ2(String cXCJ2) {
		CXCJ2 = cXCJ2;
	}

	public String getBKCJ() {
		return BKCJ;
	}

	public void setBKCJ(String bKCJ) {
		BKCJ = bKCJ;
	}

	public String getDBKCJ() {
		return DBKCJ;
	}

	public void setDBKCJ(String dBKCJ) {
		DBKCJ = dBKCJ;
	}

	public String getDYCJ() {
		return DYCJ;
	}

	public void setDYCJ(String dYCJ) {
		DYCJ = dYCJ;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}