package com.pantech.devolop.registerScoreQuery;
/*
@date 2017.04.13
@author yeq
模块：M8.4整班成绩查询
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
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
import jxl.write.biff.RowsExceededException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class XqzpcxBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	private String XNXQBM;//学年学期编码
	private String XNXQMC;//学年学期名称
	private String BJBH;//班级编号
	private String BJMC;//班级名称
	private String XBDM;//系部代码
	private String NJDM;//年级代码
	private String SSZY;//所属专业
	private String EXAMTYPE;//考试类型
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public XqzpcxBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2017-04-13
	 * @author:yeq
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加每周天数,上午节数,下午节数,晚上节数以及节次时间设置基础信息
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		AUTH = "";//用户权限
		XNXQBM = "";//学年学期编码
		XNXQMC = "";//学年学期名称
		BJBH = "";//班级编号
		BJMC = "";//班级名称
		XBDM = "";//系部代码
		NJDM = "";//年级代码
		SSZY = "";//所属专业
		EXAMTYPE = "";//考试类型
		MSG = "";//提示信息
	}
	
	/**
	 * 分页查询 学期班级列表
	 * @date:2017-04-13
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "with tempClassInfo as (" +
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称, d.专业代码,d.专业名称,a.班主任工号 "+
			"from V_学校班级数据子类 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
			"from V_基础信息_教学班信息表 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +	
			"union all select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号 as 班主任工号  " +
			"from V_规则管理_选修课授课计划明细表) "+	
			"select distinct t.学年学期编码,t.学年学期名称,t.班级代码,t.班级名称,t.系部代码,t.系部名称,t.年级代码,t.年级名称,t.专业代码,t.专业名称 " +
			"from ("+
			"select distinct d.学年学期编码,d.学年学期名称," +
			"case a.来源类型 when '3' then ff.班级代码 else c.班级代码 end as 班级代码," +
			"case a.来源类型 when '3' then ff.班级名称 else c.班级名称 end as 班级名称," +
			"c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称,b.课程代码,b.课程名称," +
			"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号,a.登分教师编号,"+		
			"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号 "+		
			"from V_成绩管理_登分教师信息表 a  " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   "+	
			"left join tempClassInfo c on c.班级代码=a.行政班代码 "+		  
			"left join tempClassInfo ff on ff.班级代码=a.相关编号  "+	
			"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  "+	
			"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  "+	
			"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号) as t  "+	
			"where 1=1";
		//权限判断
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' " +
				"or t.教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			//班主任
			if(this.getAUTH().indexOf(bzr) > -1){
				sql += " or t.班主任工号= '" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			//系部教务人员
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		
		if(!"".equalsIgnoreCase(this.getXNXQBM())){
			sql += " and t.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getXBDM())){
			sql += " and t.系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getNJDM())){
			sql += " and t.年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getSSZY())){
			sql += " and t.专业代码='" + MyTools.fixSql(this.getSSZY()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getBJBH())){
			sql += " and t.班级代码 like '%" + MyTools.fixSql(this.getBJBH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getBJMC())){
			sql += " and t.班级名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
		}
		sql += " order by t.学年学期编码 desc,t.班级名称";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017.04.13
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
	 * 读取年级下拉框
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNjdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,1 " +
				"from V_学校年级数据子类 where 年级状态='1' " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取导出年级下拉框
	 * @date:2017-05-04
	 * @author:zouyu
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
	 * 读取系部下拉框
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXbdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 系部名称,系部代码 from V_基础信息_系部信息表 where 系部代码<>'C00' " +
				"order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取导出系部下拉框
	 * @date:2017-05-04
	 * @author:zouyu
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
	 * 读取系部下拉框
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportBjCombo() throws SQLException{
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "with tempClassInfo as (" +
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
			"from V_学校班级数据子类 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"union all " +
			"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 "+		
			"from V_基础信息_教学班信息表 a " +	
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +		
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +		
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +	
			"union all " +
			"select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号  as 班主任工号  from V_规则管理_选修课授课计划明细表)" +
			"select 'all' as comboValue,'全部' as comboName,'0' as orderNum " +
			"union all "+
			"select distinct t.班级代码 as comboValue,t.班级名称 as comboName,'1' as orderNum " +
			"from ("+
			"select distinct d.学年学期编码,case a.来源类型 when '3' then ff.班级代码 else c.班级代码 end as 班级代码," +
			"c.班级名称,c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称,b.课程代码,b.课程名称," +
			"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号," +
			"a.登分教师编号,case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号 " +
			"from V_成绩管理_学生成绩信息表 aa " +
			"left join V_成绩管理_登分教师信息表 a on aa.相关编号=a.相关编号   " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   "+	
			"left join tempClassInfo c on c.班级代码=a.行政班代码 "+		  
			"left join tempClassInfo ff on ff.班级代码=a.相关编号  "+	
			"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  "+	
			"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  "+	
			"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号 " +
			//20170703去除异动学生过滤
//			"left join V_学生基本数据子类 i on i.学号=aa.学号 " +
			") as t  where 1=1";
		//权限判断
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' " +
				"or t.教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			//班主任
			if(this.getAUTH().indexOf(bzr) > -1){
				sql += " or t.班主任工号= '" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			//系部教务人员
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		
		if(!"".equalsIgnoreCase(this.getXNXQBM())){
			sql += " and t.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "'";
		}
		if(!"all".equalsIgnoreCase(this.getXBDM())){
			sql += " and t.系部代码 in ('" + this.getXBDM().replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(this.getNJDM())){
			sql += " and t.年级代码 in ('" + this.getNJDM().replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(this.getSSZY())){
			sql += " and t.专业代码 in ('" + this.getSSZY().replaceAll(",", "','") + "')";
		}
		sql += " order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2017-04-13
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
	 * 查询学生成绩信息
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadStuScoreInfo()throws SQLException{
		String tempSql = "";
		String sql = "";
		Vector subVec = null;
		Vector scoreInfoVec = null;
		Vector countInfoVec = null;
		Vector jfVec = null;
		Vector resultVec = new Vector();
//		String stuState = MyTools.getProp(request, "Base.stuState");
		String colInfo = "[";
		String scoreInfo = "[";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		//读取学科列名信息
		sql = "with tempClassInfo as (" +
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称, d.专业代码,d.专业名称,a.班主任工号 "+
			"from V_学校班级数据子类 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
			"from V_基础信息_教学班信息表 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +	
			"union all select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号 as 班主任工号  " +
			"from V_规则管理_选修课授课计划明细表) "+	
			"select distinct  t.课程代码,t.课程名称 " +
			"from ("+
			"select distinct b.课程代码,b.课程名称," +
			"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号,a.登分教师编号,"+		
			"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号,c.系部代码 "+		
			"from V_成绩管理_学生成绩信息表 aa " +
			"left join V_成绩管理_登分教师信息表 a on aa.相关编号=a.相关编号   " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   "+	
			"left join tempClassInfo c on c.班级代码=a.行政班代码 "+		  
			"left join tempClassInfo ff on ff.班级代码=a.相关编号  "+	
			"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  "+	
			"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  "+	
			"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号 ";
			//20170619性能优化
			//"where aa.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH())+"') and d.学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"'  "+
			//判断班级类型
			if(this.getBJBH().indexOf("JXB_") > -1){
				sql += "left join (select 学号,教学班编号 as 班级编号 from V_基础信息_教学班学生信息表) i on i.学号=aa.学号 ";
			}else if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "left join (select 学号,授课计划明细编号 as 班级编号 from V_规则管理_学生选修课关系表) i on i.学号=aa.学号 ";
			}else{
				sql += "left join (select 学号,行政班代码 as 班级编号 from V_学生基本数据子类) i on i.学号=aa.学号 ";
			}
		sql += "where d.学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"' ";
			//20170703去除异动学生过滤
			//"and i.学生状态 in ('01','05','07','08') " +
			if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "and a.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}else{
				sql += "and i.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}
		sql += ") as t where 1=1";
		//权限判断
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' " +
				"or t.教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			//班主任
			if(this.getAUTH().indexOf(bzr) > -1){
				sql += " or t.班主任工号= '" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			//系部教务人员
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += " order by t.课程代码  ";
		subVec = db.GetContextVector(sql);
		
		if(subVec!=null && subVec.size()>0){
			for(int i=0; i<subVec.size(); i+=2){
				colInfo += "{\"colField\":\"" + MyTools.StrFiltr(subVec.get(i)) + "\"," +
						"\"colName\":\"" + MyTools.StrFiltr(subVec.get(i+1)) + "\"},";
			}
			colInfo = colInfo.substring(0, colInfo.length()-1);
			
			tempSql = "select a.学号,b.课程代码,b.课程名称," +
					"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
					"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
					"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
					"(case when a.总评='-1' then '免修' when a.总评='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.总评='-4' then '缺考'  when a.总评='-5' then '缓考'  when a.总评='-17' then '免考' else a.总评 end ) as 总评,"+
					"e.平时比例,e.期中比例,e.期末比例,d.学生状态,e.总评比例选项 "+
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 ";
					//判断班级类型
					if(this.getBJBH().indexOf("JXB_") > -1){
						tempSql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else if(this.getBJBH().indexOf("XXKMX_") > -1){
						tempSql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else{
						tempSql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
					}
			tempSql += "left join V_成绩管理_登分设置信息表 e on a.相关编号=e.相关编号 "+
					"where c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' ";
					//20170619性能优化
					//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
					//20170703去除异动学生过滤
					//"and d.学生状态 in ('01','05','07','08') " +
					if(this.getBJBH().indexOf("XXKMX_") > -1){
						tempSql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
					}else{
						tempSql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
					}
			tempSql += "order by a.学号,b.课程代码";
			scoreInfoVec = db.GetContextVector(tempSql);
			
			sql = "select distinct cast(a.班内学号 as int),a.姓名,a.学号 from V_学生基本数据子类 a " +
				"left join V_基础信息_教学班学生信息表 b on b.学号=a.学号 " +
				"left join V_规则管理_学生选修课关系表 c on c.学号=a.学号 " +
				"where " +
				//20170703去除异动学生过滤
				//"学生状态 in ('01','05','07','08') and " +
				"a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"or b.教学班编号='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"or c.授课计划明细编号='" + MyTools.fixSql(this.getBJBH()) + "'" +
				"order by cast(a.班内学号 as int)";
			countInfoVec = db.GetContextVector(sql);
			
			sql = "select a.学号,b.课程代码,sum(CAST(a.加分 as int)) as 加分 from dbo.V_学分管理_学生加分申请信息表 a " +
				  "left join dbo.V_成绩管理_登分教师信息表 b on a.相关编号 = b.相关编号 " +
				  "where a.学年学期编码 = '" + MyTools.fixSql(this.getXNXQBM()) + "' and a.审核状态='2' " +
				  "group by a.学号,b.课程代码 ";
			jfVec = db.GetContextVector(sql);
			
			if(countInfoVec!=null && countInfoVec.size()>0){
				String curStuCode = "";
				String curSubCode = "";
				boolean existFlag = false;
				
				for(int i=0; i<countInfoVec.size(); i+=3){
					curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));//学号
					scoreInfo += "{\"XH\":\"" + MyTools.StrFiltr(countInfoVec.get(i)) + "\"," +
								"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\"," + 
								"\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\",";
					scoreInfo +=  "\"SCORE\":";
					scoreInfo += "[";
					
					for(int j=0; j<subVec.size(); j+=2){
						curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
						existFlag = false;
						for(int k=0; k<scoreInfoVec.size(); k+=12){
							if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+1)))){
								String XGBH= MyTools.StrFiltr(scoreInfoVec.get(k+1));
								String PS= MyTools.StrFiltr(scoreInfoVec.get(k+3));
								String QZ= MyTools.StrFiltr(scoreInfoVec.get(k+4));
								String QM= MyTools.StrFiltr(scoreInfoVec.get(k+5));
							
								String psbl= MyTools.StrFiltr(scoreInfoVec.get(k+7));
								String qzbl= MyTools.StrFiltr(scoreInfoVec.get(k+8));
								String qmbl= MyTools.StrFiltr(scoreInfoVec.get(k+9));
								String xsZt=MyTools.StrFiltr(scoreInfoVec.get(k+10));
								String zpBl=MyTools.StrFiltr(scoreInfoVec.get(k+11));
								
								String JF ="";
								for(int m=0; m<jfVec.size(); m+=3){
									if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(jfVec.get(m))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(jfVec.get(m+1)))){
										JF = MyTools.StrFiltr(jfVec.get(m+2));
									}
								}
								
								scoreInfo+= "{\"XGBH\":\"" + XGBH + "\"," +
										"\"PS\":\"" + PS + "\"," +
										"\"QZ\":\"" + QZ + "\"," +
										"\"QM\":\"" + QM + "\"," +
										"\"JF\":\"" + JF + "\"," ;
								
								if(!"10".equalsIgnoreCase(xsZt)){
									scoreInfo+= "\"ZP\":\"" + MyTools.StrFiltr(scoreInfoVec.get(k+6)) + "\"},";
								}else{
									if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
										if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
											scoreInfo+= "\"ZP\":\"\"},";
										}else{
											scoreInfo+= "\"ZP\":\"" + MyTools.StrFiltr(scoreInfoVec.get(k+6)) + "\"},";
										}
									}else {
										if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS))) || (!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) || (!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
											scoreInfo+= "\"ZP\":\"\"},";
										}else{
											scoreInfo+= "\"ZP\":\"" + MyTools.StrFiltr(scoreInfoVec.get(k+6)) + "\"},";
										}
									}
								}
								existFlag = true;
								break;
							}
						}
					
						if(!existFlag){
							scoreInfo += "{\"XGBH\":\"" + curSubCode + "\"," +
										"\"PS\":\"\"," +
										"\"QZ\":\"\"," +
										"\"QM\":\"\"," +
										"\"JF\":\"\"," +
										"\"ZP\":\"\"},";
						}
					}
				
					scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
					scoreInfo += "]},";
				}
			}
			
			this.setMSG("读取成功");
		}else{
			this.setMSG("没有相关成绩信息");
		}
		
		colInfo += "]";
		resultVec.add(colInfo);
		
		if(scoreInfo.length() > 1)
			scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
		scoreInfo += "]";
		resultVec.add(scoreInfo);
		return resultVec;
	}
	
	public String exportScoreInfo() throws SQLException{
		String sql = "";
		Vector classVec = new Vector();
		Vector bjmcmc = new Vector();
		Vector BJJVec=new Vector();//存放所有查询的课程
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		//String schoolName = MyTools.getProp(request, "Base.schoolName");
		String[] bjList=this.getBJBH().split(",",-1);
		
		if("all".equalsIgnoreCase(bjList[0])){
			 sql = "with tempClassInfo as ("+
				"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 "+
				"from V_学校班级数据子类 a "+
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 "+
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 "+
				"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,"+
				"a.班主任工号 from V_基础信息_教学班信息表 a  "+
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码  "+
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码) " +
				"select distinct c.班级名称,c.班级代码  " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_规则管理_学年学期表 d on  d.学年学期编码=b.学年学期编码 "+
				"left join tempClassInfo c on c.班级代码=a.行政班代码 where 1=1 ";
				
			//权限判断
			if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
				sql += " and (";
				//班主任
				if(this.getAUTH().indexOf(bzr) > -1){
					sql += "c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
				}
				//系部教务人员
				if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
					if(this.getAUTH().indexOf(bzr) > -1){
						sql += " or ";
					}
					sql += "c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
			if(!"".equalsIgnoreCase(this.getXNXQBM())){
				sql += " and b.学年学期编码='" +this.getXNXQBM() + "'";
			}
			if(!"all".equalsIgnoreCase(this.getXBDM())){
				sql += " and c.系部代码 in ('" + this.getXBDM().replaceAll(",", "','") + "')";
			}
			if(!"all".equalsIgnoreCase(this.getNJDM())){
				sql += " and c.年级代码 in ('" + this.getNJDM().replaceAll(",", "','") + "')";
			}
			if(!"all".equalsIgnoreCase(this.getSSZY())){
				sql += " and c.专业代码 in ('" + this.getSSZY().replaceAll(",", "','") + "')";
			}
			sql += "order by c.班级代码  ";
			BJJVec = db.GetContextVector(sql);
			if(BJJVec!=null && BJJVec.size()>0){
				for(int i=0; i<BJJVec.size(); i+=2){
					classVec.add(BJJVec.get(i+1));	
				}
			}
		}else{
			for(int i=0; i<bjList.length; i++){
				classVec.add(bjList[i]);
			}
		}	
		/*
		sql = "with tempClassInfo as ("+
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 "+
			"from V_学校班级数据子类 a "+
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 "+
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 "+
			"union all " +
			"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
			"from V_基础信息_教学班信息表 a "+
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码  "+
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码) ";
		
		if("all".equalsIgnoreCase(bjList[0])){
			 sql += "select distinct c.班级名称,c.班级代码  "+
				"from V_成绩管理_登分教师信息表 a "+
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号   "+
				"left join tempClassInfo c on c.班级代码=a.行政班代码 " +
				"where 1=1 ";
			//权限判断
			if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
				sql += " and (";
				//班主任
				if(this.getAUTH().indexOf(bzr) > -1){
					sql += "c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
				}
				//系部教务人员
				if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
					if(this.getAUTH().indexOf(bzr) > -1){
						sql += " or ";
					}
					sql += "系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
			if(!"".equalsIgnoreCase(this.getXNXQBM())){
				sql += " and b.学年学期编码='" +this.getXNXQBM() + "'";
			}
			if(!"all".equalsIgnoreCase(this.getXBDM())){
				sql += " and c.系部代码 in ('" + this.getXBDM().replaceAll(",", "','") + "')";
				
			}
			if(!"all".equalsIgnoreCase(this.getNJDM())){
				sql += " and c.年级代码 in ('" + this.getNJDM().replaceAll(",", "','") + "')";
			}
			if(!"all".equalsIgnoreCase(this.getSSZY())){
				sql += " and c.专业代码 in ('" + this.getSSZY().replaceAll(",", "','") + "')";
			}
			sql += " order by c.班级代码  ";
			BJJVec = db.GetContextVector(sql);	
			if(BJJVec!=null && BJJVec.size()>0){
				for(int i=0; i<BJJVec.size(); i+=2){
					classVec.add(BJJVec.get(i+1));	
					}
				}
			}else{
			for(int i=0; i<bjList.length; i++){
				classVec.add(bjList[i]);
			}
			//sql = "select 班级代码,班级名称 from tempClassInfo where 班级代码 in ('" + this.getBJBH().replaceAll(",", "','") + "')";
		}
		classVec = db.GetContextVector(sql);	
			*/
		String filePath = MyTools.getProp(request, "Base.exportExcelPath");
		String title = "";
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/" + this.getXNXQMC().replace(" ", "") + "成绩表.xls";
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			
		for(int n=0; n<classVec.size(); n++){
			String tempSql = "";
			Vector titleVec = new Vector();	
			Vector scoreVec = new Vector();
			Vector subVec = new Vector();
			Vector scoreInfoVec = new Vector();
			Vector countInfoVec = new Vector();
			Vector jfVec = new Vector();
			
			//查询班级名称
			//sql="select 行政班名称  from V_学校班级数据子类 where 行政班代码='"+classVec.get(n)+"'";
			sql = "select 班级名称 from (select 班级编号,班级名称  from V_基础信息_班级信息表 " +
				"union all " +
				"select 授课计划明细编号 as 班级编号,选修班名称 as 班级名称 from V_规则管理_选修课授课计划明细表) a " +
				"where 班级编号='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "'";
			bjmcmc = db.GetContextVector(sql);
			title =  this.getXNXQMC().replace(" ", "") + MyTools.StrFiltr(bjmcmc.get(0)) + "成绩表";
			
			//查询班级学科信息
			sql = "with tempClassInfo as (" +
				"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称, d.专业代码,d.专业名称,a.班主任工号 "+
				"from V_学校班级数据子类 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
				"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
				"from V_基础信息_教学班信息表 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +	
				"union all select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号 as 班主任工号  " +
				"from V_规则管理_选修课授课计划明细表) "+	
				"select distinct  t.课程代码,t.课程名称 " +
				"from ("+
				"select distinct b.课程代码,b.课程名称," +
				"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号,a.登分教师编号,"+		
				"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号,c.系部代码 "+		
				"from V_成绩管理_学生成绩信息表 aa " +
				"left join V_成绩管理_登分教师信息表 a on aa.相关编号=a.相关编号   " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
				"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   "+	
				"left join tempClassInfo c on c.班级代码=a.行政班代码 "+		  
				"left join tempClassInfo ff on ff.班级代码=a.相关编号  "+	
				"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  "+	
				"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  "+	
				"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号 ";
				//20170619性能优化
				//"where aa.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH())+"') and d.学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"'  "+
			//判断班级类型
			if(MyTools.StrFiltr(classVec.get(n)).indexOf("JXB_") > -1){
				sql += "left join (select 学号,教学班编号 as 班级编号 from V_基础信息_教学班学生信息表) i on i.学号=aa.学号 ";
			}else if(MyTools.StrFiltr(classVec.get(n)).indexOf("XXKMX_") > -1){
				sql += "left join (select 学号,授课计划明细编号 as 班级编号 from V_规则管理_学生选修课关系表) i on i.学号=aa.学号 ";
			}else{
				sql += "left join (select 学号,行政班代码 as 班级编号 from V_学生基本数据子类) i on i.学号=aa.学号 ";
			}
			sql += "where d.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' ";
			//20170703去除异动学生过滤
			//"and i.学生状态 in ('01','05','07','08') " +
			if(MyTools.StrFiltr(classVec.get(n)).indexOf("XXKMX_") > -1){
				sql += "and a.相关编号='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "'";
			}else{
				sql += "and i.班级编号='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "'";
			}
			sql += ") as t where 1=1";
			//权限判断
			if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
				sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' " +
					"or t.教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
				//班主任
				if(this.getAUTH().indexOf(bzr) > -1){
					sql += " or t.班主任工号= '" + MyTools.fixSql(this.getUSERCODE()) + "' ";
				}
				//系部教务人员
				if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
					sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
			sql += " order by t.课程代码";
			subVec = db.GetContextVector(sql);
			
			if(subVec!=null && subVec.size()>0){
				for(int i=0; i<subVec.size(); i+=2){
					titleVec.add(subVec.get(i+1));	
				}
			}
			/*tempSql="with tempScoreInfo as (" +
					"select a.学号,a.相关编号,b.课程名称," +
					"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
					"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
					"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
					"(case when a.总评='-1' then '免修' when a.总评='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.总评='-4' then '缺考'  when a.总评='-5' then '缓考'  when a.总评='-17' then '免考' else a.总评 end ) as 总评 "+
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
					"where c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
					"and b.行政班代码='" + MyTools.StrFiltr(classVec.get(n)) + "')";
			sql = tempSql + " select * from tempScoreInfo order by 学号,相关编号";*/
			tempSql = "select a.学号,b.课程代码,b.课程名称," +
					"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
					"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
					"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
					"(case when a.总评='-1' then '免修' when a.总评='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.总评='-4' then '缺考'  when a.总评='-5' then '缓考'  when a.总评='-17' then '免考' else a.总评 end ) as 总评 "+
					",e.平时比例,e.期中比例,e.期末比例,d.学生状态,e.总评比例选项 "+
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 ";
			//判断班级类型
			if(MyTools.StrFiltr(classVec.get(n)).indexOf("JXB_") > -1){
				tempSql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else if(MyTools.StrFiltr(classVec.get(n)).indexOf("XXKMX_") > -1){
				tempSql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else{
				tempSql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
			}
			tempSql += "left join V_成绩管理_登分设置信息表 e on a.相关编号=e.相关编号 "+
					"where c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' ";
					//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.StrFiltr(classVec.get(n))+"') "+
					//20170619性能优化
					//20170703去除异动学生过滤
					//"and d.学生状态 in ('01','05','07','08') " +
			//判断班级类型
			if(MyTools.StrFiltr(classVec.get(n)).indexOf("XXKMX_") > -1){
				tempSql += "and b.相关编号='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "'";
			}else{
				tempSql += "and d.班级编号='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "'";
			}
			tempSql += "order by a.学号,b.课程代码";
			scoreInfoVec = db.GetContextVector(tempSql);
			
			sql = "select distinct cast(a.班内学号 as int),a.姓名,a.学号 from V_学生基本数据子类 a " +
				"left join V_基础信息_教学班学生信息表 b on b.学号=a.学号 " +
				"left join V_规则管理_学生选修课关系表 c on c.学号=a.学号 " +
				"where " +
				//20170703去除异动学生过滤
				//"学生状态 in ('01','05','07','08') and " +
				"a.行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "' " +
				"or b.教学班编号='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "' " +
				"or c.授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(classVec.get(n))) + "'" +
				"order by cast(a.班内学号 as int)";
			countInfoVec = db.GetContextVector(sql);
			
			sql = "select a.学号,b.课程代码,sum(CAST(a.加分 as int)) as 加分 from dbo.V_学分管理_学生加分申请信息表 a " +
				  "left join dbo.V_成绩管理_登分教师信息表 b on a.相关编号 = b.相关编号 " +
				  "where a.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
				  "and a.审核状态='2' " +
				  "group by a.学号,b.课程代码";
			jfVec = db.GetContextVector(sql);
			
			if(countInfoVec!=null && countInfoVec.size()>0){
				String curStuCode = "";
				String curSubCode = "";
				boolean existFlag = false;
				
				for(int i=0; i<countInfoVec.size(); i+=3){
					curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));//学号
					scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i)));
					scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
					for(int j=0; j<subVec.size(); j+=2){
						curSubCode = MyTools.StrFiltr(subVec.get(j));//相关编号
						existFlag = false;
						for(int k=0; k<scoreInfoVec.size(); k+=12){
							if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+1)))){
								String PS= MyTools.StrFiltr(scoreInfoVec.get(k+3));
								String QZ= MyTools.StrFiltr(scoreInfoVec.get(k+4));
								String QM= MyTools.StrFiltr(scoreInfoVec.get(k+5));
								
								String JF ="";
								for(int m=0; m<jfVec.size(); m+=3){
									if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(jfVec.get(m))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(jfVec.get(m+1)))){
										JF = MyTools.StrFiltr(jfVec.get(m+2));
									}
								}
							
								String psbl= MyTools.StrFiltr(scoreInfoVec.get(k+7));
								String qzbl= MyTools.StrFiltr(scoreInfoVec.get(k+8));
								String qmbl= MyTools.StrFiltr(scoreInfoVec.get(k+9));
								String xsZt=MyTools.StrFiltr(scoreInfoVec.get(k+10));
								String zpBl=MyTools.StrFiltr(scoreInfoVec.get(k+11));
								
								scoreVec.add(MyTools.StrFiltr(PS));
								scoreVec.add(MyTools.StrFiltr(QZ));
								scoreVec.add(MyTools.StrFiltr(QM));
								scoreVec.add(MyTools.StrFiltr(JF));
								
								if(!"10".equalsIgnoreCase(xsZt)){
									scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+6)));
									}else{
										if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
											if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
												scoreVec.add(MyTools.StrFiltr(""));
											}else{
												scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+6)));
											}
										}else {
											if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
												scoreVec.add(MyTools.StrFiltr(""));
										  }else{
											  scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+6)));
										 }
									}
								}
								existFlag = true;
								break;
							}
						}
					
						if(!existFlag){
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
							}
						
						}
					}
				}
				this.exportScore("printView", wbook, scoreVec, titleVec, title, n, MyTools.StrFiltr(classVec.get(n)));
			}
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
	
	public String exportSingle() throws SQLException{
		String sql = "";
	//	String schoolName = MyTools.getProp(request, "Base.schoolName");
		String filePath = MyTools.getProp(request, "Base.exportExcelPath");
		String title = "";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/" + this.getXNXQMC().replace(" ", "") + this.getBJMC() + "成绩表.xls";
		
	try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
		
			String tempSql = "";
			Vector titleVec = new Vector();	
			Vector scoreVec = new Vector();
			Vector subVec = new Vector();
			Vector scoreInfoVec = new Vector();
			Vector countInfoVec = new Vector();
			Vector jfVec = new Vector();
			title = this.getXNXQMC().replace(" ", "") + this.getBJMC() + "成绩表";
			
			
			sql = "with tempClassInfo as (" +
				"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称, d.专业代码,d.专业名称,a.班主任工号 "+
				"from V_学校班级数据子类 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
				"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
				"from V_基础信息_教学班信息表 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +	
				"union all select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号 as 班主任工号  " +
				"from V_规则管理_选修课授课计划明细表) "+	
				"select distinct  t.课程代码,t.课程名称 " +
				"from ("+
				"select distinct b.课程代码,b.课程名称," +
				"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号,a.登分教师编号,"+		
				"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号,c.系部代码 "+		
				"from V_成绩管理_学生成绩信息表 aa " +
				"left join V_成绩管理_登分教师信息表 a on aa.相关编号=a.相关编号   " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
				"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   "+	
				"left join tempClassInfo c on c.班级代码=a.行政班代码 "+		  
				"left join tempClassInfo ff on ff.班级代码=a.相关编号  "+	
				"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  "+	
				"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  "+	
				"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号 ";
				//"where aa.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH())+"') and d.学年学期编码='"+MyTools.fixSql(this.getXNXQBM())+"'  "+
				//20170619性能优化
			//判断班级类型
			if(this.getBJBH().indexOf("JXB_") > -1){
				sql += "left join (select 学号,教学班编号 as 班级编号 from V_基础信息_教学班学生信息表) i on i.学号=aa.学号 ";
			}else if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "left join (select 学号,授课计划明细编号 as 班级编号 from V_规则管理_学生选修课关系表) i on i.学号=aa.学号 ";
			}else{
				sql += "left join (select 学号,行政班代码 as 班级编号 from V_学生基本数据子类) i on i.学号=aa.学号 ";
			}
			sql += "where d.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' ";
			//20170703去除异动学生过滤
			//"and i.学生状态 in ('01','05','07','08') " +
			if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "and a.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}else{
				sql += "and i.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}
			sql += ") as t where 1=1";
			//权限判断
			if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
				sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' " +
					"or t.教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
				//班主任
				if(this.getAUTH().indexOf(bzr) > -1){
					sql += " or t.班主任工号= '" + MyTools.fixSql(this.getUSERCODE()) + "' ";
				}
				//系部教务人员
				if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
					sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
			sql += " order by t.课程代码";
			subVec = db.GetContextVector(sql);
			
			if(subVec!=null && subVec.size()>0){
				for(int i=0; i<subVec.size(); i+=2){
					titleVec.add(subVec.get(i+1));	
				}
			}
			
			tempSql = "select a.学号,b.课程代码,b.课程名称," +
					"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
					"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
					"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
					"(case when a.总评='-1' then '免修' when a.总评='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.总评='-4' then '缺考'  when a.总评='-5' then '缓考'  when a.总评='-17' then '免考' else a.总评 end ) as 总评 "+
					",e.平时比例,e.期中比例,e.期末比例,d.学生状态,e.总评比例选项 "+
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 ";
					//判断班级类型
					if(this.getBJBH().indexOf("JXB_") > -1){
						tempSql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else if(this.getBJBH().indexOf("XXKMX_") > -1){
						tempSql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else{
						tempSql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
					}
			tempSql += "left join V_成绩管理_登分设置信息表 e on a.相关编号=e.相关编号 "+
					"where c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' ";
					//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
					//20170619性能优化
					//20170703去除异动学生过滤
					//"and d.学生状态 in ('01','05','07','08') " +
					if(this.getBJBH().indexOf("XXKMX_") > -1){
						tempSql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
					}else{
						tempSql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
					}
			tempSql += "order by a.学号,b.课程代码";
			scoreInfoVec = db.GetContextVector(tempSql);
			
			sql = "select distinct cast(a.班内学号 as int),a.姓名,a.学号 from V_学生基本数据子类 a " +
				"left join V_基础信息_教学班学生信息表 b on b.学号=a.学号 " +
				"left join V_规则管理_学生选修课关系表 c on c.学号=a.学号 " +
				"where " +
				//20170703去除异动学生过滤
				//"学生状态 in ('01','05','07','08') and " +
				"a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"or b.教学班编号='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"or c.授课计划明细编号='" + MyTools.fixSql(this.getBJBH()) + "'" +
				"order by cast(a.班内学号 as int)";
			countInfoVec = db.GetContextVector(sql);
			
			//查询学生申请加分信息
			sql = "select a.学号,b.课程代码,sum(CAST(a.加分 as int)) as 加分 from dbo.V_学分管理_学生加分申请信息表 a " +
				  "left join dbo.V_成绩管理_登分教师信息表 b on a.相关编号 = b.相关编号 " +
				  "where a.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' and a.审核状态='2' " + 
				  "group by a.学号,b.课程代码 ";
			jfVec = db.GetContextVector(sql);
			
			if(countInfoVec!=null && countInfoVec.size()>0){
				String curStuCode = "";
				String curSubCode = "";
				boolean existFlag = false;
				for(int i=0; i<countInfoVec.size(); i+=3){
					curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));//学号
					scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i)));
					scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
					
					for(int j=0; j<subVec.size(); j+=2){
						curSubCode = MyTools.StrFiltr(subVec.get(j));//相关编号
						existFlag = false;
						for(int k=0; k<scoreInfoVec.size(); k+=12){
							if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+1)))){
								String PS= MyTools.StrFiltr(scoreInfoVec.get(k+3));
								String QZ= MyTools.StrFiltr(scoreInfoVec.get(k+4));
								String QM= MyTools.StrFiltr(scoreInfoVec.get(k+5));
								
								//------------------------------------------------------------------------------
								String JF ="";
								for(int m=0; m<jfVec.size(); m+=3){
									if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(jfVec.get(m))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(jfVec.get(m+1)))){
										JF = MyTools.StrFiltr(jfVec.get(m+2));
									}
								}
								
								//------------------------------------------------------------------------------
							
							
								String psbl= MyTools.StrFiltr(scoreInfoVec.get(k+7));
								String qzbl= MyTools.StrFiltr(scoreInfoVec.get(k+8));
								String qmbl= MyTools.StrFiltr(scoreInfoVec.get(k+9));
								String xsZt=MyTools.StrFiltr(scoreInfoVec.get(k+10));
								String zpBl=MyTools.StrFiltr(scoreInfoVec.get(k+11));
								scoreVec.add(MyTools.StrFiltr(PS));
								scoreVec.add(MyTools.StrFiltr(QZ));
								scoreVec.add(MyTools.StrFiltr(QM));
								scoreVec.add(MyTools.StrFiltr(JF));
								if(!"10".equalsIgnoreCase(xsZt)){
									scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+6)));
								}else{
									if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
										if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
											scoreVec.add(MyTools.StrFiltr(""));
										}else{
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+6)));
										}
									}else {
										if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
											scoreVec.add(MyTools.StrFiltr(""));
									  }else{
										  scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+6)));
									  }
									}
								}
								existFlag = true;
								break;
							}
						}
					
						if(!existFlag){
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
						}
					}
				}
			}
			this.exportScore("printView", wbook, scoreVec, titleVec, title, 0, this.getBJBH());
		
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
	 * 导出成绩xls
	 * @date:2016-03-29
	 * @author:yeq
	 * @param type 导出/打印
	 * @param wbook
	 * @param scoreVec 成绩信息
	 * @param titleVec 表格标题数组
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
	public void exportScore(String type, WritableWorkbook wbook, Vector scoreVec, Vector titleVec, String title,int a,String sheet) throws IOException, WriteException, FileNotFoundException{
		WritableSheet wsheet = wbook.createSheet(sheet, a);//工作表名称
		WritableFont fontStyle;
		WritableFont fontStyle1;
		WritableCellFormat contentStyle;
		WritableCellFormat contentStyle1;
		Label content;
		wsheet.setColumnView(0, 1);
		wsheet.setColumnView(1, 10);//行宽字符
		wsheet.setRowView(1,600);//列高磅
		wsheet.setRowView(2,1110);
		
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 16, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.LEFT);// 左对齐
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
		contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
		
		if(titleVec.size()<3){
			wsheet.mergeCells(0, 0, 12, 0);
		}else{
			wsheet.mergeCells(0, 0, titleVec.size()*5+1, 0);
		}
		//wsheet.mergeCells(0, 0, titleVec.size()*4+1, 0);
		content = new Label(0, 0, title, contentStyle);
		wsheet.addCell(content);
		
		//标题信息
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
		contentStyle.setWrap(true);
		contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
		contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
		
		wsheet.mergeCells(0, 1, 0, 2);
		wsheet.setColumnView(0, 4);
		content = new Label(0, 1, "学\n号", contentStyle);
		wsheet.addCell(content);
		
		wsheet.mergeCells(1, 1, 1, 2);
		content = new Label(1, 1, "学生\n姓名", contentStyle);
		wsheet.addCell(content);
		
	for (int i=0; i<titleVec.size(); i++) {
		if(i==titleVec.size()-1){
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setWrap(true);
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			System.out.println("titleVec.get(i) "+titleVec.get(i));
			wsheet.mergeCells(i*5+2, 1, i*5+6, 1);
			content = new Label(i*5+2, 1, MyTools.StrFiltr(titleVec.get(i)) , contentStyle);
			wsheet.addCell(content);
		}else{
				fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setWrap(true);
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				wsheet.mergeCells(i*5+2, 1, i*5+6, 1);
				content = new Label(i*5+2, 1, MyTools.StrFiltr(titleVec.get(i)) , contentStyle);
				wsheet.addCell(content);
			}
			
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			contentStyle.setWrap(true);
			contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			
			content = new Label(i*5+2, 2, "平\n时", contentStyle);
			wsheet.setColumnView(i*5+2, 5);
			wsheet.setRowView(2, 600);
			wsheet.addCell(content);
			
			content = new Label(i*5+3, 2, "期\n中", contentStyle);
			wsheet.setColumnView(i*5+3, 5);
			wsheet.setRowView(2, 600);
			wsheet.addCell(content);
			
			content = new Label(i*5+4, 2, "期\n末", contentStyle);
			wsheet.setColumnView(i*5+4, 5);
			wsheet.setRowView(2, 600);
			wsheet.addCell(content);
			
			content = new Label(i*5+5, 2, "加\n分", contentStyle);
			wsheet.setColumnView(i*5+5, 5);
			wsheet.setRowView(2, 600);
			wsheet.addCell(content);
			
			if(i==titleVec.size()-1){
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					contentStyle.setWrap(true);
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					content = new Label(i*5+6, 2, "总\n评", contentStyle);
					wsheet.setColumnView(i*5+6, 5);
					wsheet.setRowView(2, 600);
					wsheet.addCell(content);
			}else{
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
					contentStyle.setWrap(true);
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					content = new Label(i*5+6, 2, "总\n评", contentStyle);
					wsheet.setColumnView(i*5+6, 5);
					wsheet.setRowView(2, 600);
					wsheet.addCell(content);
			}
		}
		
		//表格内容
		//k:用于循环时Excel的行号
		//外层for用于循环行,内曾for用于循环列
		int aa=titleVec.size()*5+2;
		for(int i=0, k=3; i<scoreVec.size(); i+=aa, k++){
			for(int j=0; j<aa; j++){
				String score=MyTools.StrFiltr(scoreVec.get(i+j));
				if("免修".equalsIgnoreCase(score)||"作弊".equalsIgnoreCase(score)||"取消资格".equalsIgnoreCase(score)||"缺考".equalsIgnoreCase(score)||"缓考".equalsIgnoreCase(score)||"免考".equalsIgnoreCase(score)||"及格".equalsIgnoreCase(score)||"补及".equalsIgnoreCase(score)){
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						if(j == aa-1){
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						}
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						if(i+aa==scoreVec.size()){
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}
						contentStyle.setWrap(true);
						content = new Label(j, k, MyTools.StrFiltr(scoreVec.get(i+j)) , contentStyle);
						wsheet.addCell(content);
					}else if(MyTools.StringToInt(score)>=0&&MyTools.StringToInt(score)<60&&j>1){
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.RED);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						if(j == aa-1){
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						}
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						if(i+aa==scoreVec.size()){
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}
						contentStyle.setWrap(true);
						content = new Label(j, k, MyTools.StrFiltr(scoreVec.get(i+j)) , contentStyle);
						wsheet.addCell(content);
					}else{
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
						if(j == aa-1){
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
						}
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						if(i+aa==scoreVec.size()){
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
						}else{
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						}
						contentStyle.setWrap(true);
						content = new Label(j, k, MyTools.StrFiltr(scoreVec.get(i+j)), contentStyle);
						wsheet.addCell(content);
					}
				}
			wsheet.setRowView(k,600);
		}
	}
	
	/**
	 * 解析文字成绩
	 * @date:2017-04-13
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

	public String getXNXQMC() {
		return XNXQMC;
	}

	public void setXNXQMC(String xNXQMC) {
		XNXQMC = xNXQMC;
	}

	public String getBJBH() {
		return BJBH;
	}

	public void setBJBH(String bJBH) {
		BJBH = bJBH;
	}

	public String getBJMC() {
		return BJMC;
	}

	public void setBJMC(String bJMC) {
		BJMC = bJMC;
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

	public String getSSZY() {
		return SSZY;
	}

	public void setSSZY(String sSZY) {
		SSZY = sSZY;
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