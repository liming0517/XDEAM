package com.pantech.devolop.registerScoreStatistics;
/*
@date 2017.03.28
@author yeq
模块：M8.3 综合分析
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

public class ZhfxBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	
	private String XNXQBM;//学年学期编码
	private String CLASSCODE;//班级编号
	private String CLASSNAME;//班级名称
	private String CLASSTYPE;//班级类型
	private String XBDM;//系部代码
	private String NJDM;//年级代码
	private String ZYDM;//专业名称
	private String EXAMTYPE;//考试类型

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public ZhfxBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2017.03.16
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		AUTH = "";//用户权限
		XNXQBM = "";//学年学期编码
		CLASSCODE = "";//班级编号
		CLASSNAME = "";//班级名称
		CLASSTYPE = "";//班级类型
		XBDM = "";//系部代码
		NJDM = "";//年级代码
		ZYDM = "";//专业代码
		EXAMTYPE = "";//考试类型
		MSG = "";
	}

	/**
	 * 分页查询 班级课程信息
	 * @date:2017.03.28
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queClassCourseList(int pageNum, int pageSize) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "select distinct t.学年学期编码,t.学年学期名称,t.行政班代码 as 班级代码,t.行政班名称 as 班级名称,t.班级类型 from (" +
			"select b.学年学期编码,b.学年学期名称,a.行政班代码,a.行政班名称," +
			"case when a.来源类型='1' then (case when a.行政班代码 like 'JXB%' then d.系部代码 else c.系部代码 end) " +
			"else '' end as 系部代码,a.课程名称,a.登分教师编号 as 授课教师编号," +
			"case a.来源类型 when '3' then '选修班' when '4' then '分层班' else (case when a.行政班代码 like 'JXB%' then '教学班' else '行政班' end) end as 班级类型," +
			"case a.来源类型 when '3' then 'xxb' when '4' then 'fcb' else (case when a.行政班代码 like 'JXB%' then 'jxb' else 'xzb' end) end as 班级类型代码 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_学校班级数据子类 c on c.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 d on d.教学班编号=a.行政班代码" +
			") as t where 1=1";
		
		//权限判断
		//班主任查询自己班级信息
		if(this.getAUTH().indexOf(bzr) > -1){
			sql += " and t.行政班代码 in (select 行政班代码 from V_学校班级数据子类 where 班主任工号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
		}else if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			//判断如果不是全校查询权限，需添加查询范围限制
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " and t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}else{
				sql += " and t.授课教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			}
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(this.getXNXQBM())){
			sql += " and t.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getCLASSNAME())){
			sql += " and t.行政班名称 like '%" + MyTools.fixSql(this.getCLASSNAME()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getCLASSTYPE())){
			sql += " and t.班级类型代码='" + MyTools.fixSql(this.getCLASSTYPE()) + "'";
		}
		sql += " order by t.学年学期编码 desc,t.行政班名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取当前学年学期信息
	 * @date:2017-04-12
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		Vector vec = null;
		String curXnxq = "";
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		return curXnxq;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017.03.28
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,'1' as orderNum " +
				"from V_规则管理_学年学期表 where 状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出学年学期下拉框
	 * @date:2017.04.12
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,'1' as orderNum " +
				"from V_规则管理_学年学期表 where 状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出系部下拉框
	 * @date:2017-04-12
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportXbdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 系部名称,系部代码,1 from V_基础信息_系部信息表 where 系部代码<>'C00' " +
				"order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出年级下拉框
	 * @date:2017-04-12
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportNjdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 年级名称,年级代码,1 from V_学校年级数据子类 where 年级状态='1' " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取导出专业下拉框
	 * @date:2017-04-12
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportZydmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'（'+专业代码+'）',专业代码,1 from V_专业基本信息数据子类 where 状态='1' " +
				"order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取统计信息
	 * @date:2017.03.16
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadStatisticsInfo(int pageNum, int pageSize) throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "with tempScoreInfo as (" +
			"select b.行政班代码 as 班级代码,b.行政班名称 as 班级名称,a.学号,replace(b.登分教师姓名,'@','') as 授课教师,b.相关编号,b.课程名称,";
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "a.期中 as 成绩 ";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "a.期末 as 成绩 ";
		}else{
			sql += "a.平时 as 成绩 ";
		}
		sql += "from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
			"where a.状态='1' and 成绩状态 in ('1','2') " +
			"and c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and b.行政班代码='" + MyTools.fixSql(this.getCLASSCODE()) + "'";
		//根据教师权限判断查询学科范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0 && this.getAUTH().indexOf(xbjdzr)<0 && this.getAUTH().indexOf(xbjwgl)<0 && this.getAUTH().indexOf(bzr)<0){
			sql += " and b.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
		}
		sql += ") " +
			"select 班级名称,授课教师,课程名称,";
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "'期中' as 成绩类型,";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "'期末' as 成绩类型,";
		}else{
			sql += "'平时' as 成绩类型,";
		}
		sql += "班级人数,缺考人数,作弊人数,免考人数,免修人数,缓考人数,不及格人数,班级总分,班级平均分,最高分,最低分," +
			"case when 实际考试人数>1 then cast(Round(cast(([90分以上]+[80-89]) as float)/cast(实际考试人数 as float),4)*100 as nvarchar)+'%' else '0.00%' end as 优秀率," +
			"case when 实际考试人数>1 then cast(Round(cast(([90分以上]+[80-89]+[70-79]+[60-69]) as float)/cast(实际考试人数 as float),4)*100 as nvarchar)+'%' else '0.00%' end as 及格率," +
			"[90分以上],[80-89],[70-79],[60-69],[60分以下] from (" +
			"select distinct 班级名称,授课教师,课程名称," +
			"count(学号) as 班级人数," +
			"sum(case when 成绩<>'' and cast(成绩 as float)>-1 then 1 else 0 end) as 实际考试人数," +
			"sum(case when 成绩='-4' then 1 else 0 end) as 缺考人数," +
			"sum(case when 成绩='-2' then 1 else 0 end) as 作弊人数," +
			"sum(case when 成绩='-17' then 1 else 0 end) as 免考人数," +
			"sum(case when 成绩='-1' then 1 else 0 end) as 免修人数," +
			"sum(case when 成绩='-5' then 1 else 0 end) as 缓考人数," +
			"sum(case when 成绩<>'' and cast(成绩 as float) between 0 and 60 then 1 else 0 end) as 不及格人数," +
			"sum(case when 成绩<>'' and cast(成绩 as float)>-1 then cast(成绩 as float) else 0 end) as 班级总分," +
			"(select cast(avg(cast(成绩 as float)) as numeric(18,2)) from tempScoreInfo where 班级代码=t.班级代码 and 相关编号=t.相关编号 and 成绩<>'' and cast(成绩 as float)>-1) as 班级平均分," +
			"(select max(cast(成绩 as float)) from tempScoreInfo where 班级代码=t.班级代码 and 相关编号=t.相关编号 and 成绩<>'' and cast(成绩 as float)>-1) as 最高分," +
			"(select min(cast(成绩 as float)) from tempScoreInfo where 班级代码=t.班级代码 and 相关编号=t.相关编号 and 成绩<>'' and cast(成绩 as float)>-1) as 最低分," +
			"sum(case when cast(成绩 as float)>89 then 1 else 0 end) as [90分以上]," +
			"sum(case when cast(成绩 as float) between 80 and 89 then 1 else 0 end) as [80-89]," +
			"sum(case when cast(成绩 as float) between 70 and 79 then 1 else 0 end) as [70-79]," +
			"sum(case when cast(成绩 as float) between 60 and 69 then 1 else 0 end) as [60-69]," +
			"sum(case when 成绩<>'' and cast(成绩 as float) between 0 and 60 then 1 else 0 end) as [60分以下] " +
			"from tempScoreInfo t group by 班级代码,班级名称,授课教师,相关编号,课程名称) as z order by 课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	/**
	 * 导出统计信息文件
	 * @date:2017.03.31
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public String exportStatisticsInfo(String exportType) throws SQLException{
		Vector vec = null;
		String sql = "";
		String titleArray[] = {"班级名称","授课教师","课程名称","成绩类型","班级人数","缺考人数","作弊人数","免考人数","免修人数","缓考人数","不及格人数","班级总分","班级平均分","最高分","最低分","及格率","优秀率","90以上","80-90","70-80","60-70","60分以下"};
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		sql = "with tempScoreInfo as (" +
			"select * from (select b.行政班代码 as 班级代码,b.行政班名称 as 班级名称,c.年级代码,c.专业代码," +
			"case b.来源类型 when '3' then 'xxb' when '4' then 'fcb' else (case when b.行政班代码 like 'JXB%' then 'jxb' else 'xzb' end) end as 班级类型代码," +
			"case b.来源类型 when '3' then '' when '4' then '' " +
			"else (case when b.行政班代码 like 'JXB%' then e.系部代码 else d.系部代码 end) end as 系代码," +
			"a.学号,replace(b.登分教师姓名,'@','') as 授课教师,b.相关编号,b.课程名称,";
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "a.期中 as 成绩 ";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "a.期末 as 成绩 ";
		}else{
			sql += "a.平时 as 成绩 ";
		}
		sql += "from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
			"left join V_基础信息_教学班信息表 e on e.教学班编号=b.行政班代码 " +
			"where a.状态='1' and 成绩状态 in ('1','2')" +
			"and c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' ";
		//根据教师权限判断查询学科范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0 && this.getAUTH().indexOf(xbjdzr)<0 && this.getAUTH().indexOf(xbjwgl)<0 && this.getAUTH().indexOf(bzr)<0){
			sql += " and b.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
		}
		sql += ") as t where 1=1";
		if("single".equalsIgnoreCase(exportType)){
			sql += " and t.班级代码='" + MyTools.fixSql(this.getCLASSCODE()) + "'";
		}else{
			if(!"all".equalsIgnoreCase(this.getXBDM()))
				sql += " and t.系代码='" + MyTools.fixSql(this.getXBDM()) + "'";
			if(!"all".equalsIgnoreCase(this.getNJDM()))
				sql += " and t.年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
			if(!"all".equalsIgnoreCase(this.getZYDM()))
				sql += " and t.专业代码='" + MyTools.fixSql(this.getZYDM()) + "'";
			if(!"all".equalsIgnoreCase(this.getCLASSTYPE()))
				sql += " and t.班级类型代码='" + MyTools.fixSql(this.getCLASSTYPE()) + "'";
		}
		sql += ") " +
			"select 班级名称,授课教师,课程名称,";
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "'期中' as 成绩类型,";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "'期末' as 成绩类型,";
		}else{
			sql += "'平时' as 成绩类型,";
		}
		sql += "班级人数,缺考人数,作弊人数,免考人数,免修人数,缓考人数,不及格人数,班级总分,班级平均分,最高分,最低分," +
			"case when 实际考试人数>1 then cast(Round(cast(([90分以上]+[80-89]) as float)/cast(实际考试人数 as float),4)*100 as nvarchar)+'%' else '0.00%' end as 优秀率," +
			"case when 实际考试人数>1 then cast(Round(cast(([90分以上]+[80-89]+[70-79]+[60-69]) as float)/cast(实际考试人数 as float),4)*100 as nvarchar)+'%' else '0.00%' end as 及格率," +
			"[90分以上],[80-89],[70-79],[60-69],[60分以下] from (" +
			"select distinct 班级名称,授课教师,课程名称," +
			"count(学号) as 班级人数," +
			"sum(case when 成绩<>'' and cast(成绩 as float)>-1 then 1 else 0 end) as 实际考试人数," +
			"sum(case when 成绩='-4' then 1 else 0 end) as 缺考人数," +
			"sum(case when 成绩='-2' then 1 else 0 end) as 作弊人数," +
			"sum(case when 成绩='-17' then 1 else 0 end) as 免考人数," +
			"sum(case when 成绩='-1' then 1 else 0 end) as 免修人数," +
			"sum(case when 成绩='-5' then 1 else 0 end) as 缓考人数," +
			"sum(case when 成绩<>'' and cast(成绩 as float) between 0 and 60 then 1 else 0 end) as 不及格人数," +
			"sum(case when 成绩<>'' and cast(成绩 as float)>-1 then cast(成绩 as float) else 0 end) as 班级总分," +
			"(select cast(avg(cast(成绩 as float)) as numeric(18,2)) from tempScoreInfo where 班级代码=t.班级代码 and 相关编号=t.相关编号 and 成绩<>'' and cast(成绩 as float)>-1) as 班级平均分," +
			"(select max(cast(成绩 as float)) from tempScoreInfo where 班级代码=t.班级代码 and 相关编号=t.相关编号 and 成绩<>'' and cast(成绩 as float)>-1) as 最高分," +
			"(select min(cast(成绩 as float)) from tempScoreInfo where 班级代码=t.班级代码 and 相关编号=t.相关编号 and 成绩<>'' and cast(成绩 as float)>-1) as 最低分," +
			"sum(case when cast(成绩 as float)>89 then 1 else 0 end) as [90分以上]," +
			"sum(case when cast(成绩 as float) between 80 and 89 then 1 else 0 end) as [80-89]," +
			"sum(case when cast(成绩 as float) between 70 and 79 then 1 else 0 end) as [70-79]," +
			"sum(case when cast(成绩 as float) between 60 and 69 then 1 else 0 end) as [60-69]," +
			"sum(case when 成绩<>'' and cast(成绩 as float) between 0 and 60 then 1 else 0 end) as [60分以下] " +
			"from tempScoreInfo t group by 班级代码,班级名称,授课教师,相关编号,课程名称) as z order by 班级名称,课程名称";
		vec = db.GetContextVector(sql);
		
		//生成XLS文件
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
//		int hour = c.get(Calendar.HOUR);
//		int minute = c.get(Calendar.MINUTE);
//		int second = c.get(Calendar.SECOND);
		String filePath = MyTools.getProp(request, "Base.exportExcelPath");
		String title = schoolName+this.getXNXQBM().substring(0, 4)+"学年第"+("1".equalsIgnoreCase(this.getXNXQBM().substring(4, 5))?"一":"二")+"学期 "+("qz".equalsIgnoreCase(this.getEXAMTYPE())?"期中":"期末");
		if("all".equalsIgnoreCase(exportType)){
			title += "行政班单科统计表";
		}else{
			title += this.getCLASSNAME()+"单科统计表";
		}
		
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/" + title+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+".xls";
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			
			//设置列宽
			wsheet.setColumnView(0, 15);
			wsheet.setColumnView(1, 15);
			wsheet.setColumnView(2, 30);
			for(int i=3; i<22; i++){
				wsheet.setColumnView(i, 11);
			}
			
			//标题
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			wsheet.mergeCells(0, 0, titleArray.length-1, 0);
			content = new Label(0, 0, title, contentStyle);
			wsheet.addCell(content);
			wsheet.setRowView(0, 500);
			
			//表格标题行
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			
			for(int i=0; i<titleArray.length; i++){
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
			
			//表格内容
			//k:用于循环时Excel的行号
			//外层for用于循环行,内曾for用于循环列
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			
			for(int i=0, k=2; i<vec.size(); i+=titleArray.length, k++){
				for(int j=0; j<titleArray.length; j++){
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setWrap(true);// 自动换行
					//contentStyle.setShrinkToFit(true);//字体大小自适应
						
					if(j == 0){
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
					}else if(j == titleArray.length-1){
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
					}else{
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					}
					
					if(k == (vec.size()/titleArray.length+1)){
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
					}else{
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}
					
					content = new Label(j, k, MyTools.StrFiltr(vec.get(i+j)), contentStyle);
					wsheet.addCell(content);
				}
				wsheet.setRowView(k, 400);
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
		
		return filePath;
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
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getAUTH() {
		return AUTH;
	}

	public void setAUTH(String aUTH) {
		AUTH = aUTH;
	}

	public String getXNXQBM() {
		return XNXQBM;
	}

	public void setXNXQBM(String xNXQBM) {
		XNXQBM = xNXQBM;
	}

	public String getCLASSCODE() {
		return CLASSCODE;
	}

	public void setCLASSCODE(String cLASSCODE) {
		CLASSCODE = cLASSCODE;
	}

	public String getCLASSNAME() {
		return CLASSNAME;
	}

	public void setCLASSNAME(String cLASSNAME) {
		CLASSNAME = cLASSNAME;
	}

	public String getCLASSTYPE() {
		return CLASSTYPE;
	}

	public void setCLASSTYPE(String cLASSTYPE) {
		CLASSTYPE = cLASSTYPE;
	}

	public String getXBDM() {
		return XBDM;
	}

	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}

	public String getNJDM() {
		return NJDM;
	}

	public void setNJDM(String nJDM) {
		NJDM = nJDM;
	}

	public String getZYDM() {
		return ZYDM;
	}

	public void setZYDM(String zYDM) {
		ZYDM = zYDM;
	}

	public String getEXAMTYPE() {
		return EXAMTYPE;
	}

	public void setEXAMTYPE(String eXAMTYPE) {
		EXAMTYPE = eXAMTYPE;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}