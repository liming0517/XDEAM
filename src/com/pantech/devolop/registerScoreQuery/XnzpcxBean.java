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
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;

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

public class XnzpcxBean {
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
	private String XN;//学年
	private String CX_XQ1; //学期一
	private String CX_XQ2; //学期二
	private String CX_JSFS; //计算方式

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public XnzpcxBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2017-04-13
	 * @author:yeq
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
		CX_XQ1 = ""; //学期一
		CX_XQ2 = ""; //学期二
		CX_JSFS = ""; //计算方式
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
	/*	sql = "with tempClassInfo as (" +
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 from V_学校班级数据子类 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"union all " +
			"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 from V_基础信息_教学班信息表 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码) " +
			"select distinct d.学年,c.班级代码,c.班级名称,c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称 from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码 "+
			"left join tempClassInfo c on c.班级代码=a.行政班代码 " +
			"where 1=1";
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
		if(!"".equalsIgnoreCase(this.getXN())){
			sql += " and d.学年='" + MyTools.fixSql(this.getXN()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getBJBH())){
			sql += " and c.班级代码 like '%" + MyTools.fixSql(this.getBJBH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getBJMC())){
			sql += " and c.班级名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXBDM())){
			sql += " and c.系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getNJDM())){
			sql += " and c.年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getSSZY())){
			sql += " and c.专业代码='" + MyTools.fixSql(this.getSSZY()) + "'";
		}
		sql += " order by d.学年 desc ,c.班级代码 desc";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）*/
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
			"union all select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号 as 班主任工号 from V_规则管理_选修课授课计划明细表) "+	
			"select distinct t.学年,t.班级代码,t.班级名称,t.系部代码,t.系部名称,t.年级代码,t.年级名称,t.专业代码,t.专业名称 " +
			"from ("+
			"select distinct d.学年," +
			"case a.来源类型 when '3' then ff.班级代码 else c.班级代码 end as 班级代码," +
			"case a.来源类型 when '3' then ff.班级名称 else c.班级名称 end as 班级名称," +
			"c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称,b.课程代码,b.课程名称," +
			"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号,a.登分教师编号,"+		
			"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号 "+		
			"from V_成绩管理_登分教师信息表 a  left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
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
				sql += " or 系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		
		if(!"".equalsIgnoreCase(this.getXN())){
			sql += " and t.学年='" + MyTools.fixSql(this.getXN()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getBJBH())){
			sql += " and t.班级代码 like '%" + MyTools.fixSql(this.getBJBH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getBJMC())){
			sql += " and t.班级名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
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
		sql += " order by t.学年 desc,t.班级名称";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取学年总评占比列表数据
	 * @date:2017.04.13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queXnzpzbList() throws SQLException{
		Vector vec = null;
		String sql = "select a.系部代码 ,a.系部名称,isnull(b.学期一,'') as 学期一 ,isnull(b.学期二,'') as 学期二,isnull(b.计算方式,'') as 计算方式  " +
				"from V_基础信息_系部信息表 a "+
				"left join V_成绩管理_系部学年总评设置表 b on b.系部代码=a.系部代码 "+
				"where a.系部代码<>'C00'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2017.06.02
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 学年 as comboName,学年 as comboValue,'1' as orderNum " +
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
	 * 读取班级下拉框
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
			"select distinct d.学年,case a.来源类型 when '3' then ff.班级代码 else c.班级代码 end as 班级代码," +
			"c.班级名称,c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称,b.课程代码,b.课程名称," +
			"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号," +
			"a.登分教师编号,case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   "+	
			"left join tempClassInfo c on c.班级代码=a.行政班代码 "+		  
			"left join tempClassInfo ff on ff.班级代码=a.相关编号  "+	
			"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  "+	
			"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  "+	
			"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号  ) as t  "+	
			"where 1=1";
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
				sql += " or 系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		if(!"".equalsIgnoreCase(this.getXN())){
			sql += " and t.学年='" + MyTools.fixSql(this.getXN()) + "'";
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
	 * 读取考试学科信息
	 * @date:2017-04-13
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public Vector loadExamSubData() throws SQLException {
		Vector vec = new Vector();
		Vector resultVec = new Vector();
		String tempStr = "";
		String jsonStr = "[";
		String sql = "select a.相关编号,a.课程名称 from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
				"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' order by a.课程名称";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=2){
				tempStr += MyTools.StrFiltr(vec.get(i))+",";
				jsonStr += "{\"code\":\"" + MyTools.StrFiltr(vec.get(i)) + "\",\"subName\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"},";
			}
			
			tempStr = tempStr.substring(0, tempStr.length()-1);
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		}
		jsonStr += "]";
		resultVec.add(tempStr);
		resultVec.add(jsonStr);
		return resultVec;
	}
	
	/**
	 * 保存学年总评占比设置
	 * @date:2017-04-13
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public void saveXnzpzb() throws SQLException {
		Vector checkVec = null;
		String sql = "select count(*) from V_成绩管理_系部学年总评设置表 where 系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
		
		if(db.getResultFromDB(sql)){
			sql = "update V_成绩管理_系部学年总评设置表 set " +
				"计算方式='" + MyTools.fixSql(this.getCX_JSFS()) + "'," +
				"学期一='" + MyTools.fixSql(this.getCX_XQ1()) + "'," +
				"学期二='" + MyTools.fixSql(this.getCX_XQ2()) + "' " +
				"where 系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
		}else{
			sql = "insert into V_成绩管理_系部学年总评设置表(系部代码,学期一,学期二,计算方式,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getXBDM()) + "'," +
				"'" + MyTools.fixSql(this.getCX_XQ1()) + "'," +
				"'" + MyTools.fixSql(this.getCX_XQ2()) + "'," +
				"'" + MyTools.fixSql(this.getCX_JSFS()) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			setMSG("保存成功");
		}else{
			setMSG("保存失败");
		}
	}
	
	/**
	 * 查询学生学年总评成绩信息
	 * @date:2017-05-18
	 * @author:zouyu
	 * @param jS_SFTXN 
	 * @param jS_XBMC 
	 * @param xXQZB 
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadStuScoreInfo()throws SQLException{
		String sql = "";
		Vector subVec = null;
		Vector xqzbVec = new Vector();//学期占比设置的信息
		Vector scoreInfoVec = null;//学生成绩信息
		Vector countInfoVec = null;//学生人员信息
		Vector resultVec = new Vector();//存放查询出来所有的信息
		Vector firstVec = new Vector();//存放上学期课程
		Vector secondVec = new Vector();//存放下学期课程
		Vector xyspVec = new Vector();//
		String colInfo = "[";
		String scoreInfo = "[";
		String firstSem = "";//上学期
		String secondSem = "";//下学期
		int type = 0;
		String firstSemPercent = "";
		String secondSemPercent = "";
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP); 
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		//查询学年总评占比设置信息
		sql = "select 学期一,学期二,计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码='" + MyTools.StrFiltr(this.getXBDM()) + "'";
		xqzbVec = db.GetContextVector(sql);
		
		//判断计算方式		
		if(xqzbVec!=null && xqzbVec.size()>0){
			firstSemPercent = MyTools.StrFiltr(xqzbVec.get(0));
			secondSemPercent = MyTools.StrFiltr(xqzbVec.get(1));
			
			if("1".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))){
				type = 1;//同学年
			}else{
				type = 2;//跨学年
			}
		}else{
			firstSemPercent = "40";
			secondSemPercent = "60";
			type = 1;
		}
			
		if(type == 1){//同学年
			firstSem = MyTools.fixSql(this.getXN()) + "101";
			secondSem = MyTools.fixSql(this.getXN()) + "201";
		}else{//跨学年
			firstSem = MyTools.fixSql(this.getXN()) + "201";
			secondSem = MyTools.parseString((MyTools.StringToInt(this.getXN())+1))+"101";
		}
		
		//查询所有学科信息
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
			"select distinct t.课程代码,t.课程名称 " +
			"from ("+
			"select distinct b.课程代码,b.课程名称," +
			"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号,a.登分教师编号,"+		
			"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号,c.系部代码  "+		
			"from V_成绩管理_学生成绩信息表 aa " +
			"left join V_成绩管理_登分教师信息表 a on aa.相关编号=a.相关编号   " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +		
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   " +	
			"left join tempClassInfo c on c.班级代码=a.行政班代码 " +		  
			"left join tempClassInfo ff on ff.班级代码=a.相关编号  " +	
			"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  " +
			"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  " +	
			"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号 ";
			//"where aa.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH())+"') and d.学年学期编码 in('" + firstSem + "','" + secondSem + "')  "+
			//判断班级类型
		if(this.getBJBH().indexOf("JXB_") > -1){
			sql += "left join (select 学号,教学班编号 as 班级编号 from V_基础信息_教学班学生信息表) i on i.学号=aa.学号 ";
		}else if(this.getBJBH().indexOf("XXKMX_") > -1){
			sql += "left join (select 学号,授课计划明细编号 as 班级编号 from V_规则管理_学生选修课关系表) i on i.学号=aa.学号 ";
		}else{
			sql += "left join (select 学号,行政班代码 as 班级编号 from V_学生基本数据子类) i on i.学号=aa.学号 ";
		}
		sql += "where d.学年学期编码 in ('" + firstSem + "','" + secondSem + "') ";
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
		
		//添加课程
		if(subVec!=null && subVec.size()>0){
			for(int i=0; i<subVec.size(); i+=2){
				colInfo += "{\"colField\":\"" + MyTools.StrFiltr(subVec.get(i)) + "\"," +
				"\"colName\":\"" + MyTools.StrFiltr(subVec.get(i+1)) + "\"},";
			}
			colInfo = colInfo.substring(0, colInfo.length()-1);
			
			//查询跨学年的上学期课程
//			sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a "+
//				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
//				"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
//				"left join V_规则管理_学年学期表 d on d.学年学期编码=c.学年学期编码 " +
//				"where d.学年学期编码='" + MyTools.fixSql(firstSem) + "' " +
//				"and b.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
//				"order by a.姓名,a.相关编号,c.学年学期编码";
			/*sql = "select distinct a.课程代码  from V_成绩管理_登分教师信息表 a "+
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+
				"where b.学年学期编码='" + MyTools.fixSql(firstSem) + "' " +
				"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"order by a.课程代码";*/
			sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  ";
			//判断班级类型
			if(this.getBJBH().indexOf("JXB_") > -1){
				sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else{
				sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
			}
			sql += "where c.学年学期编码='" + MyTools.fixSql(firstSem) + "' ";
			if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}else{
				sql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}
			sql += "order by b.课程代码";
			firstVec = db.GetContextVector(sql);
								
			//查询跨学年的下学期课程
//			sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a "+
//				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
//				"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
//				"left join V_规则管理_学年学期表 d on d.学年学期编码=c.学年学期编码 " +
//				"where d.学年学期编码='" + MyTools.fixSql(secondSem) + "' " +
//				"and b.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
//				"order by a.姓名,a.相关编号,c.学年学期编码";
			/*sql = "select distinct a.课程代码  from V_成绩管理_登分教师信息表 a "+
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+
				"where b.学年学期编码='" + MyTools.fixSql(secondSem) + "' " +
				"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"order by a.课程代码";*/
			sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  ";
			//判断班级类型
			if(this.getBJBH().indexOf("JXB_") > -1){
				sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else{
				sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
			}
			sql += "where c.学年学期编码='" + MyTools.fixSql(secondSem) + "' ";
			if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}else{
				sql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}
			sql += "order by b.课程代码";
			secondVec=db.GetContextVector(sql);
		
			//查询该班级学生成绩信息
			sql = "with tempClassInfo as ("+
				"select b.学年,a.学号,b.课程代码," +
				"(case when a.补考='-1' then '免修' when a.补考='-2' then '作弊' when a.补考='-3' then '取消资格' when a.补考='-4' then '缺考' when a.补考='-5' then '缓考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格' when a.补考='-17' then '免考' else a.补考 end ) as 补考  " +
				"from V_成绩管理_学生补考成绩信息表  a "+	
				"left join V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+	
				"where b.班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"and b.学年='" + MyTools.fixSql(this.getXN()) + "'" +	
				") "+	
				"select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
				"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
				"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
				"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
				"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
				"f.补考 ,e.平时比例,e.期中比例,e.期末比例,d.学生状态,e.总评比例选项 "+
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 ";
			//判断班级类型
			if(this.getBJBH().indexOf("JXB_") > -1){
				sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
			}else{
				sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
			}
			sql += "left join V_成绩管理_登分设置信息表 e on e.相关编号=b.相关编号 "+
				"left join tempClassInfo f on f.学号=a.学号 and f.课程代码=b.课程代码 "+
				"where c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') ";
				//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH()) +"') " +
				//20170703去除异动学生过滤
				//"and e.学生状态 in ('01','05','07','08') " +
			if(this.getBJBH().indexOf("XXKMX_") > -1){
				sql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}else{
				sql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			}
			sql += " order by a.学号,b.课程代码,c.学年学期编码";
			scoreInfoVec = db.GetContextVector(sql);
						
			//查询学业水平测试成绩
			sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
				"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
				"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
				"where b.班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"and c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
				"and c.类别编号='03' and a.状态='1' and b.状态='1' and c.状态='1' "+
				"order by c.学年学期编码 desc";	
			xyspVec = db.GetContextVector(sql);
			
			//查询该班级所有学生			
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
			
			//遍历每一个学生每一个课程
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
						
						for(int k=0; k<scoreInfoVec.size(); k+=15){
							if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+2)))){
								boolean first = false;
								boolean second = false;
								//用来判断上学期是否有课程
								if(firstVec!=null && firstVec.size()>0){
									for(int s=0; s<firstVec.size(); s+=2){
										if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
											first = true;
											break;
										}
									}
								}
								//用来判断下学期是否有课程
								if(secondVec!=null&&secondVec.size()>0){
									for(int x=0; x<secondVec.size();x+=2){
										if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
											second=true;
											break;
										} 
									 }
								}
								String zp1 = MyTools.StrFiltr(scoreInfoVec.get(k+8));
								String PS = MyTools.StrFiltr(scoreInfoVec.get(k+5));
								String QZ = MyTools.StrFiltr(scoreInfoVec.get(k+6));
								String QM = MyTools.StrFiltr(scoreInfoVec.get(k+7));
								String psbl = MyTools.StrFiltr(scoreInfoVec.get(k+10));
								String qzbl = MyTools.StrFiltr(scoreInfoVec.get(k+11));
								String qmbl = MyTools.StrFiltr(scoreInfoVec.get(k+12));
								String xsZt = MyTools.StrFiltr(scoreInfoVec.get(k+13));
								String zpBl = MyTools.StrFiltr(scoreInfoVec.get(k+14));
								
								if("10".equalsIgnoreCase(xsZt)){
									if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
										if("".equalsIgnoreCase(PS) || ("".equalsIgnoreCase(QM))){
											zp1 = "";
										}
									}else {
										if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS))) || (!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) || (!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
											zp1 = "";
									  }
									}
								}
								
								if(first==true && second==true){//两学期都有该课程的情况
									String zp2 = MyTools.StrFiltr(scoreInfoVec.get(k+23));
									String PS2 = MyTools.StrFiltr(scoreInfoVec.get(k+20));
									String QZ2 = MyTools.StrFiltr(scoreInfoVec.get(k+21));
									String QM2 = MyTools.StrFiltr(scoreInfoVec.get(k+22));
								
									String psbl2 = MyTools.StrFiltr(scoreInfoVec.get(k+25));
									String qzbl2 = MyTools.StrFiltr(scoreInfoVec.get(k+26));
									String qmbl2 = MyTools.StrFiltr(scoreInfoVec.get(k+27));
									String xsZt2 = MyTools.StrFiltr(scoreInfoVec.get(k+28));
									String zpBl2 = MyTools.StrFiltr(scoreInfoVec.get(k+29));
									if("10".equalsIgnoreCase(xsZt2)){
										if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
											if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
												zp2="";
											}
										}else {
											if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
												zp2="";
										  }
										}
									}
									
									scoreInfo = this.second(scoreInfo, scoreInfoVec, k, firstSemPercent, secondSemPercent, zp1, zp2,f,xyspVec,curStuCode,curSubCode);
								}else if(first==true && second==false){//上学期有该课程,下学期没有
									scoreInfo = this.first(scoreInfo, scoreInfoVec, k, zp1,xyspVec,curStuCode,curSubCode);
								}else{//上学期没该课程,下学期有
									scoreInfo = this.thrid(scoreInfo, scoreInfoVec, k, zp1,xyspVec,curStuCode,curSubCode);
								}
								existFlag = true;
								break;
							}
						}
						if(!existFlag){
							scoreInfo += "{\"XGBH\":\"\"," +
										"\"SXQ\":\"\"," +
										"\"XXQ\":\"\"," +
										"\"XNZP\":\"\"," +
										"\"B\":\"\"},";
						}
					}
					scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
					scoreInfo+="]},";
					
				}
				scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
				this.setMSG("读取成功");
			}else{
				this.setMSG("没有相关成绩信息");
			}
		}
		
		colInfo += "]";
		resultVec.add(colInfo);
		
		scoreInfo += "]";
		resultVec.add(scoreInfo);
		return resultVec;
	}
	
	//用来增加上学期课程
	public String first(String scoreInfo, Vector scoreInfoVec, int k, String zp1,Vector xyspVec,String curStuCode,String curSubCode){
		scoreInfo+= "{\"XGBH\":\"\"," +
					"\"SXQ\":\"" +zp1+ "\"," +
					"\"XXQ\":\"\"," ;
		String bk="";
//		if(("免修".equalsIgnoreCase(zp1)||"免考".equalsIgnoreCase(zp1))||"作弊".equalsIgnoreCase(zp1)||"取消资格".equalsIgnoreCase(zp1)||"缺考".equalsIgnoreCase(zp1)||"缓考".equalsIgnoreCase(zp1)||"".equalsIgnoreCase(zp1)){
//				 scoreInfo+="\"XNZP\":\"\",";
//		}else{
//			scoreInfo+="\"XNZP\":\"" + zp1 + "\",";
//		}
		if("免修".equalsIgnoreCase(zp1)){
			zp1 = "60";
		}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
			zp1 = "";
		}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
			zp1 = "0";
		}
		scoreInfo+="\"XNZP\":\"" + zp1 + "\",";
		if("".equalsIgnoreCase(zp1)){
			zp1="0";
		}
		if(MyTools.StringToDouble(zp1)<60.0){
			if(xyspVec!=null && xyspVec.size()>0){
				for(int w=0;w<xyspVec.size();w++){
					String xyspcj="";
					if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
						if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))){
							xyspcj="0";
							
						}else{
							xyspcj=MyTools.StrFiltr(xyspVec.get(w+2));
						}
						if(MyTools.StringToDouble(xyspcj)>=60.0){
							bk=xyspcj;
							break;
						}else{
							bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
						}
						
					}else{
						bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
					}
				}
				
			}else{
				bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
			}
			
		}else{
			bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
			
		}
		
		scoreInfo+= "\"B\":\""+bk+"\"},";
		
		return scoreInfo;
	}
		
	//用来增加两个学期课程
	 public String second(String scoreInfo, Vector scoreInfoVec, int k, String firstSemPercent, String secondSemPercent, String zp1, String zp2,DecimalFormat f,Vector xyspVec,String curStuCode,String curSubCode){
		int sxqzb = MyTools.StringToInt(firstSemPercent);
		int xxqzb = MyTools.StringToInt(secondSemPercent);
		String bk="";
		scoreInfo+= "{\"XGBH\":\"\"," +
				"\"SXQ\":\"" + zp1+ "\"," +
				"\"XXQ\":\"" + zp2 + "\"," ;
		if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
			zp1 = "0";
		}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
			zp1 = "";
		}
		if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
			zp2 = "0";
		}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
			zp2 = "";
		}
		if("免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
			bk="60";
			scoreInfo+="\"XNZP\":\"60\",";
		}else if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
			bk=zp1;
			scoreInfo+="\"XNZP\":\"" + zp1 + "\",";
		}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
			bk=zp2;
			scoreInfo+="\"XNZP\":\"" + zp2 + "\",";
		}else{
			if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
				bk="";
				scoreInfo+="\"XNZP\":\"\",";
			}else{
				zp1 = "".equalsIgnoreCase(zp1)?"0":zp1;
				zp2 = "".equalsIgnoreCase(zp2)?"0":zp2;
				bk=f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100);
				scoreInfo+="\"XNZP\":\"" + f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100) + "\",";
			}
		}
		if("".equalsIgnoreCase(bk)){
			bk="0";
		}
		if(MyTools.StringToDouble(bk)<60.0){
			if(xyspVec!=null && xyspVec.size()>0){
				for(int w=0;w<xyspVec.size();w++){
					String xyspcj="";
					if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
						if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))){
							xyspcj="0";
							
						}else{
							xyspcj=MyTools.StrFiltr(xyspVec.get(w+2));
						}
						if(MyTools.StringToDouble(xyspcj)>=60.0){
							bk=xyspcj;
							break;
						}else{
							bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
						}
						
					}else{
						bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
					}
				}
				
			}else{
				bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
			}
			
		}else{
			bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
			
		}
		
		scoreInfo+= "\"B\":\""+bk+"\"},";
		return scoreInfo;
	}
	 
	 //用来增加下学期课程
	 public String thrid(String scoreInfo, Vector scoreInfoVec, int k, String zp2,Vector xyspVec,String curStuCode,String curSubCode){
		 scoreInfo+= "{\"XGBH\":\"\"," +
					"\"SXQ\":\"\"," +
					"\"XXQ\":\"" +zp2+ "\"," ;
		 String bk="";
//		if("免修".equalsIgnoreCase(zp2)||"免考".equalsIgnoreCase(zp2)||"作弊".equalsIgnoreCase(zp2)||"取消资格".equalsIgnoreCase(zp2)||"缺考".equalsIgnoreCase(zp2)||"缓考".equalsIgnoreCase(zp2)||"".equalsIgnoreCase(zp2)){
//			scoreInfo+="\"XNZP\":\"\",";
//		}else{
//			 		scoreInfo+="\"XNZP\":\"" + zp2+ "\",";
//		}
		if("免修".equalsIgnoreCase(zp2)){
			zp2 = "60";
		}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
			zp2 = "";
		}else if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
			zp2 = "0";
		}
		scoreInfo+="\"XNZP\":\"" + zp2 + "\",";
		if("".equalsIgnoreCase(zp2)){
			zp2="0";
		}
		if(MyTools.StringToDouble(zp2)<60.0){
			if(xyspVec!=null && xyspVec.size()>0){
				for(int w=0;w<xyspVec.size();w++){
					String xyspcj="";
					if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
						if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))){
							xyspcj="0";
							
						}else{
							xyspcj=MyTools.StrFiltr(xyspVec.get(w+2));
						}
						if(MyTools.StringToDouble(xyspcj)>=60.0){
							bk=xyspcj;
							break;
						}else{
							bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
						}
						
					}else{
						bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
					}
				}
				
			}else{
				bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
			}
			
		}else{
			bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
			
		}
		
		scoreInfo+= "\"B\":\""+bk+"\"},";
		return scoreInfo;
	}
 
	public String exportScoreInfo(String XNMC,String XNDM,String SXQZB, String XXQZB, String ZB_XBMC, String ZB_SFTXN) throws SQLException{
		String sql = "";
		Vector BJJVec=new Vector();//存放所有查询的课程
		Vector BJVec = new Vector();//存放遍历的每一个课程
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		String[] bjList=this.getBJBH().split(",",-1);
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP);  
		
		//判断班级是否为全部
		if("all".equalsIgnoreCase(bjList[0])){
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
				"select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号  as 班主任工号  from V_规则管理_选修课授课计划明细表) " +
				"select distinct t.班级名称,t.班级代码  " +
				"from ("+
				"select distinct d.学年,case a.来源类型 when '3' then ff.班级代码 else c.班级代码 end as 班级代码," +
				"c.班级名称,c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称,b.课程代码,b.课程名称," +
				"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号," +
				"a.登分教师编号,case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号 " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +		
				"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码 " +	
				"left join tempClassInfo c on c.班级代码=a.行政班代码 " +		  
				"left join tempClassInfo ff on ff.班级代码=a.相关编号 " +	
				"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号 "+	
				"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号 "+	
				"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号) as t  "+	
				"where 1=1";
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
			if(!"".equalsIgnoreCase(this.getXN())){
				sql += " and t.学年='" + MyTools.fixSql(this.getXN()) + "'";
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
			sql += " order by t.班级代码";
		
			BJJVec = db.GetContextVector(sql);	
			if(BJJVec!=null && BJJVec.size()>0){
				for(int i=0; i<BJJVec.size(); i+=2){
					BJVec.add(BJJVec.get(i+1));	
					}
				}
			}else{
				for(int i=0; i<bjList.length; i++){
					BJVec.add(bjList[i]);
				}
			}	
		
			String filePath = "";
			String title = "学生成绩";
			//创建
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			filePath += "/" + this.getXN()+"学年 学生总评信息"+".xls";
			
			try {
				//输出流
				OutputStream os = new FileOutputStream(filePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
				
				for(int n=0; n<BJVec.size(); n++){
					Vector xyspVec = new Vector();
					Vector titleVec = new Vector();	//存放所有课程
					Vector scoreVec = new Vector();//存放分数
					Vector subVec = new Vector();//查询所有课程
					Vector scoreInfoVec = new Vector();//查询学生成绩
					Vector countInfoVec = new Vector();//查询班级学生
					Vector firstVec = new Vector();//查询上学期课程
					Vector secondVec = new Vector();//查询下学期课程
					Vector xqzbVec =new Vector();//查询学期设置信息
					Vector bjmcmc =new Vector();//查询班级名称
					String firstSem = "";//上学期
					String secondSem = "";//下学期
					int type=0;
				
					//查询班级名称
					//sql = "select 行政班名称  from V_学校班级数据子类 where 行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					sql = "select 班级名称 from (select 班级编号,班级名称  from V_基础信息_班级信息表 " +
						"union all " +
						"select 授课计划明细编号 as 班级编号,选修班名称 as 班级名称 from V_规则管理_选修课授课计划明细表) a " +
						"where 班级编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					bjmcmc = db.GetContextVector(sql);
					//设置标题
					title = this.getXN()+"学年"+MyTools.StrFiltr(bjmcmc.get(0))+"汇总表";
					
					//查询学期占比设置信息
					sql = "select 系部代码,学期一 ,学期二,计算方式 from V_成绩管理_系部学年总评设置表 " +
						"where 系部代码=(select 系部代码 from V_基础信息_班级信息表 " +
						"where 班级编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "')";
					xqzbVec= db.GetContextVector(sql);
					if(xqzbVec!=null && xqzbVec.size()>0){
						if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
							type = 2;//跨学年2
						}else{
							type = 1;//同学年有系部
						}
					}else{
						xqzbVec.add("");
						xqzbVec.add("40");
						xqzbVec.add("60");
						xqzbVec.add("");
						type = 0;//同学年无系部
					}
					if(type == 2){//跨学年
						firstSem = MyTools.fixSql(this.getXN()) + "201";
						secondSem = MyTools.parseString((MyTools.StringToInt(this.getXN())+1))+"101";
					}else{//同学年
						firstSem = MyTools.fixSql(this.getXN()) + "101";
						secondSem = MyTools.fixSql(this.getXN()) + "201";
					}
				
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
						"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号,c.系部代码  "+		
						"from V_成绩管理_学生成绩信息表 aa " +
						"left join V_成绩管理_登分教师信息表 a on aa.相关编号=a.相关编号   " +
						"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+		
						"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   "+	
						"left join tempClassInfo c on c.班级代码=a.行政班代码 "+		  
						"left join tempClassInfo ff on ff.班级代码=a.相关编号  "+	
						"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  "+	//c.学年学期编码 in ('" + firstSem + "','" + secondSem + "')
						"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  "+	
						"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号 ";
					//判断班级类型
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("JXB_") > -1){
						sql += "left join (select 学号,教学班编号 as 班级编号 from V_基础信息_教学班学生信息表) i on i.学号=aa.学号 ";
					}else if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "left join (select 学号,授课计划明细编号 as 班级编号 from V_规则管理_学生选修课关系表) i on i.学号=aa.学号 ";
					}else{
						sql += "left join (select 学号,行政班代码 as 班级编号 from V_学生基本数据子类) i on i.学号=aa.学号 ";
					}
					//"where aa.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ BJVec.get(n)+"') and d.学年学期编码 in('" + firstSem + "','" + secondSem + "')  "+
					sql += "where d.学年学期编码 in('" + firstSem + "','" + secondSem + "') ";
						//20170703去除异动学生过滤
						//"and i.学生状态 in ('01','05','07','08') " +
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "and a.相关编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					}else{
						sql += "and i.班级编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
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
					//存放课程
					if(subVec!=null && subVec.size()>0){
						for(int i=0; i<subVec.size(); i+=2){
							titleVec.add(subVec.get(i+1));	
						}
					}
				
					sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
						"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  ";
					//判断班级类型
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("JXB_") > -1){
						sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else{
						sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
					}
					sql += "where c.学年学期编码='" + MyTools.fixSql(firstSem) + "' ";
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "and b.相关编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					}else{
						sql += "and d.班级编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					}
					sql += "order by b.课程代码";
					firstVec = db.GetContextVector(sql);
			
					sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
						"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  ";
					//判断班级类型
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("JXB_") > -1){
						sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else{
						sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
					}
					sql += "where c.学年学期编码='" + MyTools.fixSql(secondSem) + "' ";
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "and b.相关编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					}else{
						sql += "and d.班级编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					}
					sql += "order by b.课程代码";
					secondVec = db.GetContextVector(sql);
			
					sql = "with tempClassInfo as ("+
						"select b.学年,a.学号,b.课程代码," +
						"(case when a.补考='-1' then '免修' when a.补考='-2' then '作弊' when a.补考='-3' then '取消资格' when a.补考='-4' then '缺考'  when a.补考='-5' then '缓考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格'  when a.补考='-17' then '免考' else a.补考 end ) as 补考  " +
						"from V_成绩管理_学生补考成绩信息表  a "+	
						"left join  V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+	
						"where b.班级代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
						"and b.学年='"+ MyTools.fixSql(this.getXN()) +"'"+	
						") "+	
						"select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
						"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
						"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
						"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
						"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
						"f.补考 ,e.平时比例,e.期中比例,e.期末比例,d.学生状态,e.总评比例选项 " +
						"from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
						"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 ";
					//判断班级类型
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("JXB_") > -1){
						sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
					}else{
						sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
					}
					sql += "left join V_成绩管理_登分设置信息表 e on e.相关编号=b.相关编号 " +
						"left join tempClassInfo f on f.学号=a.学号 and f.课程代码=b.课程代码 " +
						"where c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') ";
						//"and a.学号 in (select 学号 from V_学生基本数据子类  where 学生状态 in ('01','05','07','08') and 行政班代码='"+  BJVec.get(n) +"') " +
						//20170703去除异动学生过滤
						//"and e.学生状态 in ('01','05','07','08') " +
					//判断班级类型
					if(MyTools.StrFiltr(BJVec.get(n)).indexOf("XXKMX_") > -1){
						sql += "and b.相关编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					}else{
						sql += "and d.班级编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'";
					}
					sql += " order by a.学号,b.课程代码,c.学年学期编码";
					scoreInfoVec = db.GetContextVector(sql);
			
					//查询学业水平测试成绩
					sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
						"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
						"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
						"where 班级代码='" +  MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n)))+ "' " +
						"and c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
						"and c.类别编号='03' and a.状态='1' and b.状态='1' and c.状态='1' "+
						"order by c.学年学期编码 desc";	
					xyspVec = db.GetContextVector(sql);
					
					//查询学生信息
					sql = "select distinct cast(a.班内学号 as int),a.姓名,a.学号 from V_学生基本数据子类 a " +
						"left join V_基础信息_教学班学生信息表 b on b.学号=a.学号 " +
						"left join V_规则管理_学生选修课关系表 c on c.学号=a.学号 " +
						"where " +
						//20170703去除异动学生过滤
						//"学生状态 in ('01','05','07','08') and " +
						"a.行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
						"or b.教学班编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
						"or c.授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "'" +
						"order by cast(a.班内学号 as int)";
					countInfoVec = db.GetContextVector(sql);
			       
					//对每一个学生遍历增加课程
					if(countInfoVec!=null && countInfoVec.size()>0){
						String curStuCode = "";
						String curSubCode = "";
						boolean existFlag = false;
						
						for(int i=0; i<countInfoVec.size(); i+=3){
							curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));
							scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i)));
							scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
							
							for(int j=0; j<subVec.size(); j+=2){
								curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
								existFlag = false;
								//开始
								for(int k=0; k<scoreInfoVec.size(); k+=15){
									if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+2)))){
										boolean first=false;
										boolean second=false;
										//用来判断上学期是否有课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										
										String zp1 = MyTools.StrFiltr(scoreInfoVec.get(k+8));
										String PS = MyTools.StrFiltr(scoreInfoVec.get(k+5));
										String QZ = MyTools.StrFiltr(scoreInfoVec.get(k+6));
										String QM = MyTools.StrFiltr(scoreInfoVec.get(k+7));
										String psbl = MyTools.StrFiltr(scoreInfoVec.get(k+10));
										String qzbl = MyTools.StrFiltr(scoreInfoVec.get(k+11));
										String qmbl = MyTools.StrFiltr(scoreInfoVec.get(k+12));
										String xsZt = MyTools.StrFiltr(scoreInfoVec.get(k+13));
										String zpBl = MyTools.StrFiltr(scoreInfoVec.get(k+14));
										
										if("10".equalsIgnoreCase(xsZt)){
											if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
												if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
													zp1="";
												}
											}else {
												if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
													zp1="";
											  }
											}
										}
					
										String bk = "";
										if(first==true && second==true){//两个学期都有该课程
											String SXQzb = "".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));//上学期占比
											String XXQzb = "".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));//下学期占比
											int sxqzb=MyTools.StringToInt(SXQzb);
											int xxqzb=MyTools.StringToInt(XXQzb);
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+23));
											String PS2= MyTools.StrFiltr(scoreInfoVec.get(k+20));
											String QZ2= MyTools.StrFiltr(scoreInfoVec.get(k+21));
											String QM2= MyTools.StrFiltr(scoreInfoVec.get(k+22));
										
											String psbl2= MyTools.StrFiltr(scoreInfoVec.get(k+25));
											String qzbl2= MyTools.StrFiltr(scoreInfoVec.get(k+26));
											String qmbl2= MyTools.StrFiltr(scoreInfoVec.get(k+27));
											String xsZt2=MyTools.StrFiltr(scoreInfoVec.get(k+28));
											String zpBl2=MyTools.StrFiltr(scoreInfoVec.get(k+29));
											if("10".equalsIgnoreCase(xsZt2)){
												if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
													if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
														zp2 = "";
													}
												}else {
													if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
														zp2 = "";
												  }
												}
											}
											scoreVec.add(zp1);
											scoreVec.add(zp2);
											
											//计算总分
											if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}else if("缓考".equalsIgnoreCase(zp1) ||"免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}
											if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}else if("缓考".equalsIgnoreCase(zp2) ||"免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}
											
											if("免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
												bk="60";
												scoreVec.add("60");
											}else if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
												bk=zp1;
												scoreVec.add(zp1);
											}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
												bk=zp2;
												scoreVec.add(zp2);
											}else{
												if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
													bk="";
													scoreVec.add("");
												}else{
													zp1="".equalsIgnoreCase(zp1)?"0":zp1;
													zp2="".equalsIgnoreCase(zp2)?"0":zp2;
													if(type==0){
														bk=f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6);
														scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
													}else{
														bk=f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100);
														scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
													}
												}
											}
										//scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+9)));
									}else if(first==true && second==false){//上学期有课程
										scoreVec.add(zp1);
									 	scoreVec.add("");
									 	
									 	//计算总分
									 	if("免修".equalsIgnoreCase(zp1)){
									 		bk="60";
									 		scoreVec.add("60");
									 	}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
									 		bk="";
									 		scoreVec.add("");
									 	}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
									 		bk="0";
									 		scoreVec.add("0");
									 	}else{
									 		bk=zp1;
									 		scoreVec.add(zp1);
									 	}
									 	//scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+9)));
									}else{//下学期有课程的情况
										scoreVec.add("");
									 	scoreVec.add(zp1);
									 	//计算总分
									 	if("免修".equalsIgnoreCase(zp1)){
									 		bk="60";
									 		scoreVec.add("60");
									 	}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
									 		bk="";
									 		scoreVec.add("");
									 	}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
									 		bk="0";
								 			scoreVec.add("0");
								 		}else{
								 			bk=zp1;
								 			scoreVec.add(zp1);
								 		}
									 	//scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+9)));
									}
									if("".equalsIgnoreCase(bk)){
										bk="0";
									}
									if(MyTools.StringToDouble(bk)<60.0){
										if(xyspVec!=null && xyspVec.size()>0){
											for(int w=0;w<xyspVec.size();w++){
												String xyspcj="";
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
													if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))){
														xyspcj="0";
														
													}else{
														xyspcj=MyTools.StrFiltr(xyspVec.get(w+2));
													}
													if(MyTools.StringToDouble(xyspcj)>=60.0){
														bk=xyspcj;
														break;
													}else{
														bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
													}
													
												}else{
													bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
												}
											}
											
										}else{
											bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
										}
										
									}else{
										bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
										
									}
									scoreVec.add(bk);
									existFlag = true;
									break;
								}
							}
								
							//没有该课程则增加成绩信息为空
							if(!existFlag){
								scoreVec.add("");
								scoreVec.add("");
								scoreVec.add("");
								scoreVec.add("");
							}
						}
					}
				}
				//导出excel	
			    this.exportScore("printView", wbook, scoreVec, titleVec, title, n, MyTools.StrFiltr(BJVec.get(n)));
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
	
	public String exportSingle(String SXQZB,String XXQZB,String ZB_XBMC,String ZB_SFTXN) throws SQLException{
		String filePath ="";
		String title = "学生成绩";
		String sql="";
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP);  
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		String firstSem = "";//上学期
		String secondSem = "";//下学期
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		try {
			Vector bjmcmc = new Vector();//查询班级名称
			
			//查询行政班名称
//			sql="select 行政班名称  from V_学校班级数据子类 where 行政班代码='"+this.getBJBH()+"'";
			sql = "select 班级名称 from (select 班级编号,班级名称  from V_基础信息_班级信息表 " +
				"union all " +
				"select 授课计划明细编号 as 班级编号,选修班名称 as 班级名称 from V_规则管理_选修课授课计划明细表) a " +
				"where 班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			bjmcmc = db.GetContextVector(sql);
			filePath += "/" + this.getXN()+"学年"+MyTools.StrFiltr(bjmcmc.get(0)) + "总评信息.xls";
			//设置标题
			title=this.getXN()+"学年"+bjmcmc.get(0)+"汇总表";
			
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			Vector titleVec = new Vector();	//存放所有课程
			Vector scoreVec = new Vector();//存放分数
			Vector subVec = new Vector();//查询所有课程
			Vector scoreInfoVec = new Vector();//查询学生成绩
			Vector countInfoVec = new Vector();//查询班级学生
			Vector firstVec = new Vector();//查询上学期课程
			Vector secondVec = new Vector();//查询下学期课程
			Vector xqzbVec = new Vector();//查询学期设置信息
			Vector xyspVec = new Vector();//查询学期设置信息
			String firstSemPercent = "";
			String secondSemPercent = "";
			int type=0;
			
			//查询学期设置信息
			sql="select 系部代码,学期一,学期二,计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码='" + MyTools.StrFiltr(this.getXBDM()) + "'";
			   xqzbVec= db.GetContextVector(sql);
				if(xqzbVec!=null && xqzbVec.size()>0){
					if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
						type = 2;//跨学年2
					}else{
						type = 1;//同学年有系部
					}
				}else{
					xqzbVec.add("");
					xqzbVec.add("40");
					xqzbVec.add("60");
					xqzbVec.add("1");
					type = 0;//同学年无系部
				}
				if(type == 2){//跨学年
					firstSem = MyTools.fixSql(this.getXN()) + "201";
					secondSem = MyTools.parseString((MyTools.StringToInt(this.getXN())+1))+"101";
				}else{//同学年
					firstSem = MyTools.fixSql(this.getXN()) + "101";
					secondSem = MyTools.fixSql(this.getXN()) + "201";
				}
	
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
					"select distinct t.课程代码,t.课程名称 " +
					"from ("+
					"select distinct b.课程代码,b.课程名称," +
					"case a.来源类型 when '1' then '@'+replace(f.授课教师编号,'+','@,@')+'@' when '2' then '@'+replace(g.授课教师编号,'+','@,@')+'@' else '@'+replace(h.授课教师编号,'+','@,@')+'@' end as 教师编号,a.登分教师编号,"+		
					"case a.来源类型 when '3' then ff.班主任工号 else c.班主任工号 end as 班主任工号,c.系部代码  "+		
					"from V_成绩管理_学生成绩信息表 aa " +
					"left join V_成绩管理_登分教师信息表 a on aa.相关编号=a.相关编号   " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +		
					"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码   " +	
					"left join tempClassInfo c on c.班级代码=a.行政班代码 " +		  
					"left join tempClassInfo ff on ff.班级代码=a.相关编号  " +	
					"left join V_规则管理_授课计划明细表 f on f.授课计划明细编号=a.相关编号  " +	
					"left join V_排课管理_添加课程信息表 g on g.编号=a.相关编号  " +	
					"left join V_规则管理_选修课授课计划明细表 h on h.授课计划明细编号=a.相关编号 ";
					//"where aa.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH())+"') and d.学年学期编码 in ('" + firstSem + "','" + secondSem + "')   "+
				//判断班级类型
				if(this.getBJBH().indexOf("JXB_") > -1){
					sql += "left join (select 学号,教学班编号 as 班级编号 from V_基础信息_教学班学生信息表) i on i.学号=aa.学号 ";
				}else if(this.getBJBH().indexOf("XXKMX_") > -1){
					sql += "left join (select 学号,授课计划明细编号 as 班级编号 from V_规则管理_学生选修课关系表) i on i.学号=aa.学号 ";
				}else{
					sql += "left join (select 学号,行政班代码 as 班级编号 from V_学生基本数据子类) i on i.学号=aa.学号 ";
				}
				sql += "where d.学年学期编码 in ('" + firstSem + "','" + secondSem + "') ";
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

				sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  ";
				//判断班级类型
				if(this.getBJBH().indexOf("JXB_") > -1){
					sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
				}else if(this.getBJBH().indexOf("XXKMX_") > -1){
					sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
				}else{
					sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
				}
				sql += "where c.学年学期编码='" + MyTools.fixSql(firstSem) + "' ";
				if(this.getBJBH().indexOf("XXKMX_") > -1){
					sql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
				}else{
					sql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
				}
				sql += "order by b.课程代码";
				firstVec = db.GetContextVector(sql);
					
				sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  ";
				//判断班级类型
				if(this.getBJBH().indexOf("JXB_") > -1){
					sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
				}else if(this.getBJBH().indexOf("XXKMX_") > -1){
					sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
				}else{
					sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
				}
				sql += "where c.学年学期编码='" + MyTools.fixSql(secondSem) + "' ";
				if(this.getBJBH().indexOf("XXKMX_") > -1){
					sql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
				}else{
					sql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
				}
				sql += "order by b.课程代码";
				secondVec = db.GetContextVector(sql);
	
				sql = "with tempClassInfo as (" +
					"select b.学年, a.学号,b.课程代码," +
					"(case when a.补考='-1' then '免修' when a.补考='-2' then '作弊' when a.补考='-3' then '取消资格' when a.补考='-4' then '缺考'  when a.补考='-5' then '缓考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格' when a.补考='-17' then '免考' else a.补考 end ) as 补考  " +
					"from V_成绩管理_学生补考成绩信息表  a " +	
					"left join  V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 " +	
					"where b.班级代码='"+ MyTools.fixSql(this.getBJBH()) + "' and b.学年='" + MyTools.fixSql(this.getXN()) + "'" +	
					") "+	
					"select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
					"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
					"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
					"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
					"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
					"f.补考 ,e.平时比例,e.期中比例,e.期末比例,d.学生状态,e.总评比例选项 "+
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 ";
				//判断班级类型
				if(this.getBJBH().indexOf("JXB_") > -1){
					sql += "left join (select t1.学号,t1.教学班编号 as 班级编号,t2.学生状态 from V_基础信息_教学班学生信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
				}else if(this.getBJBH().indexOf("XXKMX_") > -1){
					sql += "left join (select t1.学号,t1.授课计划明细编号 as 班级编号,t2.学生状态 from V_规则管理_学生选修课关系表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号) d on d.学号=a.学号 ";
				}else{
					sql += "left join (select 学号,行政班代码 as 班级编号,学生状态 from V_学生基本数据子类) d on d.学号=a.学号 ";
				}
				sql += "left join V_成绩管理_登分设置信息表 e on e.相关编号=b.相关编号 "+
					"left join tempClassInfo f on f.学号=a.学号 and f.课程代码=b.课程代码 "+
					"where c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') ";
					//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH()) +"') " +
					//20170703去除异动学生过滤
					//"and e.学生状态 in ('01','05','07','08') " +
				//判断班级类型
				if(this.getBJBH().indexOf("XXKMX_") > -1){
					sql += "and b.相关编号='" + MyTools.fixSql(this.getBJBH()) + "'";
				}else{
					sql += "and d.班级编号='" + MyTools.fixSql(this.getBJBH()) + "'";
				}
				sql += " order by a.学号,b.课程代码,c.学年学期编码";
				scoreInfoVec = db.GetContextVector(sql);
				//存放课程
				if(subVec!=null && subVec.size()>0){
					for(int i=0; i<subVec.size(); i+=2){
						titleVec.add(subVec.get(i+1));	
					}
				}
			
				//查询学业水平测试成绩
				sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
					"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
					"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
					"where 班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
					"and c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
					"and c.类别编号='03' and a.状态='1' and b.状态='1' and c.状态='1' "+
					"order by c.学年学期编码 desc";	
				xyspVec = db.GetContextVector(sql);
			
				//查询学生信息
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
				
			   	//对每一个学生遍历增加课程
				if(countInfoVec!=null && countInfoVec.size()>0){
					String curStuCode = "";
					String curSubCode = "";
					boolean existFlag = false;
					
					for(int i=0; i<countInfoVec.size(); i+=3){
						curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));
						scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i)));
						scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
						
						for(int j=0; j<subVec.size(); j+=2){
							curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
							existFlag = false;
							//开始
							for(int k=0; k<scoreInfoVec.size(); k+=15){
								if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+2)))){
									boolean first=false;
									boolean second=false;
									//判断是否上学期有改课程
									if(firstVec!=null && firstVec.size()>0){
										for(int s=0; s<firstVec.size(); s+=2){
											if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
												first = true;
												break;
											}
										}
									}
									//用来判断下学期是否有课程
									if(secondVec!=null&&secondVec.size()>0){
										for(int x=0; x<secondVec.size();x+=2){
											if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
												second=true;
												break;
											} 
										 }
									}
										
									String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+8));
									String PS= MyTools.StrFiltr(scoreInfoVec.get(k+5));
									String QZ= MyTools.StrFiltr(scoreInfoVec.get(k+6));
									String QM= MyTools.StrFiltr(scoreInfoVec.get(k+7));
									String psbl= MyTools.StrFiltr(scoreInfoVec.get(k+10));
									String qzbl= MyTools.StrFiltr(scoreInfoVec.get(k+11));
									String qmbl= MyTools.StrFiltr(scoreInfoVec.get(k+12));
									String xsZt=MyTools.StrFiltr(scoreInfoVec.get(k+13));
									String zpBl=MyTools.StrFiltr(scoreInfoVec.get(k+14));
									
									if("10".equalsIgnoreCase(xsZt)){
										if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
											if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
												zp1="";
											}
										}else {
											if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
												zp1="";
										  }
										}
									}
									
								String bk="";
								if(first==true && second==true){//两个学期都有该课程
									String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));//上学期占比
									String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));//下学期占比
									
									int sxqzb=MyTools.StringToInt(SXQzb);
									int xxqzb=MyTools.StringToInt(XXQzb);
									String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+23));
									String PS2= MyTools.StrFiltr(scoreInfoVec.get(k+20));
									String QZ2= MyTools.StrFiltr(scoreInfoVec.get(k+21));
									String QM2= MyTools.StrFiltr(scoreInfoVec.get(k+22));
								
									String psbl2= MyTools.StrFiltr(scoreInfoVec.get(k+25));
									String qzbl2= MyTools.StrFiltr(scoreInfoVec.get(k+26));
									String qmbl2= MyTools.StrFiltr(scoreInfoVec.get(k+27));
									String xsZt2=MyTools.StrFiltr(scoreInfoVec.get(k+28));
									String zpBl2=MyTools.StrFiltr(scoreInfoVec.get(k+29));
									if("10".equalsIgnoreCase(xsZt2)){
										if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
											if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
												zp2="";
											}
										}else {
											if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
												zp2="";
										  }
										}
									}
									scoreVec.add(zp1);
									scoreVec.add(zp2);
									
									//计算总分
									if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
										zp1 = "0";
									}else if("缓考".equalsIgnoreCase(zp1) ||"免考".equalsIgnoreCase(zp1)){
										zp1 = "";
									}
									if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
										zp2 = "0";
									}else if("缓考".equalsIgnoreCase(zp2) ||"免考".equalsIgnoreCase(zp2)){
										zp2 = "";
									}
									
									if("免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
										bk="60";
										scoreVec.add("60");
									}else if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
										bk=zp1;
										scoreVec.add(zp1);
									}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
										bk=zp2;
										scoreVec.add(zp2);
									}else{
										if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
											bk="";
											scoreVec.add("");
										}else{
											zp1="".equalsIgnoreCase(zp1)?"0":zp1;
											zp2="".equalsIgnoreCase(zp2)?"0":zp2;
											if(type==0){
												bk=f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6);
												scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
											}else{
												bk=f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100);
												scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
											}
										}
									}
								}else if(first==true && second==false){//上学期有课程
									scoreVec.add(zp1);
									scoreVec.add("");
									//计算总分
									if("免修".equalsIgnoreCase(zp1)){
										bk="60";
										scoreVec.add("60");
									}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
										bk="";
										scoreVec.add("");
									}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
										bk="0";
										scoreVec.add("0");
									}else{
										bk=zp1;
										scoreVec.add(zp1);
									}
								}else{//下学期有课程的情况
									scoreVec.add("");
									scoreVec.add(zp1);
									//计算总分
									if("免修".equalsIgnoreCase(zp1)){
										bk="60";
										scoreVec.add("60");
									}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
										bk="";
										scoreVec.add("");
									}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
										bk="0";
										scoreVec.add("0");
									}else{
										bk=zp1;
										scoreVec.add(zp1);
									}
								}
								if("".equalsIgnoreCase(bk)){
									bk="0";
								}
								if(MyTools.StringToDouble(bk)<60.0){
									if(xyspVec!=null && xyspVec.size()>0){
										for(int w=0;w<xyspVec.size();w++){
											String xyspcj="";
											if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
												if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+2)))){
													xyspcj="0";
												}else{
													xyspcj=MyTools.StrFiltr(xyspVec.get(w+2));
												}
												if(MyTools.StringToDouble(xyspcj)>=60.0){
													bk=xyspcj;
													break;
												}else{
													bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
												}
											}else{
												bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
											}
										}
									}else{
										bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
									}
								}else{
									bk=MyTools.StrFiltr(scoreInfoVec.get(k+9));
								}
								scoreVec.add(bk);
								
								existFlag = true;
								break;
							}
						}
								
						//没有该课程则增加成绩信息为空
						if(!existFlag){
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
						}
					}
				}
			}		
			this.exportScore("printView", wbook,scoreVec,titleVec,title,0,MyTools.StrFiltr(this.getBJBH()));
			
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
		WritableCellFormat contentStyle;
		Label content;
		int maxRowNum = 0;
		String curScore = "";
		wsheet.setColumnView(0, 1);
		wsheet.setColumnView(1, 10);//行宽字符
		wsheet.setRowView(1,600);//列高磅
		wsheet.setRowView(2,1110);
		//设置列宽
		for(int i=2; i<titleVec.size(); i++){
			wsheet.setColumnView(i, 4);
		}
		
		//标题
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
		if(titleVec.size()<2){
			wsheet.mergeCells(0, 0, 8, 0);
		}else{
			wsheet.mergeCells(0, 0, titleVec.size()*4+1, 0);
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
		content = new Label(1, 1, "姓名", contentStyle);
		wsheet.addCell(content);
			//表格标题行
	
		
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
				wsheet.mergeCells(i*4+2, 1, i*4+5, 1);
				content = new Label(i*4+2, 1, MyTools.StrFiltr(titleVec.get(i)) , contentStyle);
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
				wsheet.mergeCells(i*4+2, 1, i*4+5, 1);
				content = new Label(i*4+2, 1, MyTools.StrFiltr(titleVec.get(i)) , contentStyle);
				wsheet.addCell(content);
			}
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			contentStyle.setWrap(true);
			content = new Label(i*4+2, 2, "上\n学\n期", contentStyle);
			wsheet.setColumnView(i*4+2, 5);
			wsheet.addCell(content);
			
			
			content = new Label(i*4+3, 2, "下\n学\n期", contentStyle);
			wsheet.setColumnView(i*4+3, 5);
			wsheet.addCell(content);
			
			
			content = new Label(i*4+4, 2, "学\n年\n总\n评", contentStyle);
			wsheet.setColumnView(i*4+4, 5);
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
					content = new Label(i*4+5, 2, "补", contentStyle);
					wsheet.setColumnView(i*4+5, 5);
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
				//contentStyle2.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				content = new Label(i*4+5, 2, "补", contentStyle);
				wsheet.setColumnView(i*4+5, 5);
				wsheet.addCell(content);
			}
		}
		
		//表格内容
		//k:用于循环时Excel的行号
		//外层for用于循环行,内曾for用于循环列
		int aa=titleVec.size()*4+2;
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
				}else if(MyTools.StringToDouble(score)>=0&&MyTools.StringToDouble(score)<60&&j>1){
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
	public String getXN() {
		return XN;
	}

	public void setXN(String xN) {
		XN = xN;
	}

	public String getCX_XQ1() {
		return CX_XQ1;
	}

	public void setCX_XQ1(String cX_XQ1) {
		CX_XQ1 = cX_XQ1;
	}

	public String getCX_XQ2() {
		return CX_XQ2;
	}

	public void setCX_XQ2(String cX_XQ2) {
		CX_XQ2 = cX_XQ2;
	}

	public String getCX_JSFS() {
		return CX_JSFS;
	}

	public void setCX_JSFS(String cX_JSFS) {
		CX_JSFS = cX_JSFS;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}