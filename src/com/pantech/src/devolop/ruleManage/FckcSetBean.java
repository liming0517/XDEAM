package com.pantech.src.devolop.ruleManage;
/*
@date 2016.12.07
@author yeq
模块：M1.12 分层课程设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.Base64Util;
import com.pantech.base.common.tools.MyTools;

public class FckcSetBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限

	private String GF_FCKCBH; //分层课程编号
	private String GF_XNXQBM; //学年学期编码
	private String GF_JXXZ; //教学性质
	private String GF_XDM; //系代码
	private String GF_NJDM; //年级代码
	private String GF_KCDM; //课程代码
	private String GF_KCMC; //课程名称
	private String GF_SKZC; //授课周次
	private String GF_SKZCMC; //授课周次名称
	private String GF_XF; //学分
	private String GF_ZKS; //总课时
	private String GF_KSXS; //考试形式
	
	private String GF_FCBBH; //分层班编号
	private String GF_FCBMC; //分层班名称
	private String GF_SKJSBH; //授课教师编号
	private String GF_SKJSXM; //授课教师姓名

	private String GF_ID; //编号
	private String GF_XH; //学号
	
	private String GF_CJR; //创建人
	private String GF_CJSJ; //创建时间
	private String GF_ZT; //状态
	
	private String GF_KMBH;//科目编号

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public FckcSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-27
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		AUTH = "";//用户权限
		GF_FCKCBH = ""; //分层课程编号
		GF_XNXQBM = ""; //学年学期编码
		GF_JXXZ = ""; //教学性质
		GF_XDM = ""; //系代码
		GF_NJDM = ""; //年级代码
		GF_KCDM = ""; //课程代码
		GF_KCMC = ""; //课程名称
		GF_SKZC = ""; //授课周次
		GF_SKZCMC = ""; //授课周次名称
		GF_XF = ""; //学分
		GF_ZKS = ""; //总课时
		GF_KSXS = ""; //考试形式
		GF_CJR = ""; //创建人
		GF_CJSJ = ""; //创建时间
		GF_ZT = ""; //状态
		
		GF_FCBBH = ""; //分层班编号
		GF_FCBMC = ""; //分层班名称
		GF_SKJSBH = ""; //授课教师编号
		GF_SKJSXM = ""; //授课教师姓名

		GF_ID = ""; //编号
		GF_XH = ""; //学号
		
		GF_KMBH = "";//科目编号
	}

	/**
	 * 读取学年学期下拉框
	 * @date:2016-12-07
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,left(学年学期编码,5) as comboValue,1 as orderNum " +
				"from V_规则管理_学年学期表 order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2016-12-07
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJxxzCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
				"from V_基础信息_教学性质 where 状态='1'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取系名称下拉框
	 * @date:2016-12-07
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 系名称 as comboName,系代码 as comboValue " +
				"from V_基础信息_系基础信息表 where len(系代码)>1 order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取年级名称下拉框
	 * @date:2016-12-07
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNjdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,1 " +
				"from V_学校年级数据子类 " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取考试形式下拉框
	 * @date:2016-12-07
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKsxsCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 考试形式 as comboName,编号 as comboValue " +
				"from V_考试形式 where 编号<>0 " +
				"order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取授课教师下拉框
	 * @date:2016-12-07
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSkjsCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 姓名+'（'+工号+'）' as comboName,工号 as comboValue,'1' from V_教职工基本数据子类 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程下拉框
	 * @date:2016-12-07
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCourseCombo() throws SQLException{
		Vector vec = null;
		String sql = "select * from (select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct a.课程名称+'（'+a.课程代码+'）' as comboName,a.课程代码 as comboValue,1 from V_规则管理_授课计划明细表 a " +
				"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
				"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
				"left join V_基础信息_系专业关系表 d on d.专业代码=c.专业代码 " +
				"where b.学年学期编码='" + MyTools.fixSql(this.getGF_XNXQBM()+this.getGF_JXXZ()) + "' " +
				"and d.系代码='" + MyTools.fixSql(this.getGF_XDM()) + "' " +
				"and c.年级代码='" + MyTools.fixSql(this.getGF_NJDM()) + "') as t " +
				"order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取分层班下拉框
	 * @date:2016-12-19
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadClassCombo() throws SQLException{
		Vector vec = null;
		String sql = "select comboName,comboValue from (select '请选择' as comboName,'' as comboValue,0 as orderNum,'' as 学期,'' as 系,'' as 年级,'' as 课程 " +
			"union all " +
			"select a.分层班名称,a.分层班编号,1 as orderNum,b.学年学期编码,b.系代码,b.年级代码,b.课程代码 from V_规则管理_分层班信息表 a " +
			"left join V_规则管理_分层课程信息表 b on b.分层课程编号=a.分层课程编号 " +
			"where b.系代码=(select top 1 系代码 from V_规则管理_分层课程信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "') " +
			"and a.分层班编号<>'" + MyTools.fixSql(this.getGF_FCBBH()) + "') as t " +
			"order by orderNum,学期 desc,系,年级,课程";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 分页查询 课程列表
	 * @date:2016-12-07
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XNXQMC_CX 学年学期名称
	 * @param JXXZ_CX 教学性质
	 * @param XDM_CX 系代码
	 * @param NJDM_CX 年级代码
	 * @param KCMC_CX 课程名称
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queCourseList(int pageNum, int pageSize, String XNXQMC_CX, String JXXZ_CX, String XDM_CX, String NJDM_CX, String KCMC_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select a.分层课程编号,g.科目编号,left(b.学年学期编码,5) as 学年学期编码,b.学年学期名称,c.编号 as 教学性质代码,c.教学性质," +
			"d.系代码,d.系名称,e.年级代码,e.年级名称,a.课程代码,a.课程名称,a.授课周次,a.授课周次名称,a.学分,a.总课时,f.编号,f.考试形式 " +
			"from V_规则管理_分层课程信息表 a " +
			"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
			"left join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
			"left join V_基础信息_系基础信息表 d on d.系代码=a.系代码 " +
			"left join V_学校年级数据子类 e on e.年级代码=a.年级代码 " +
			"left join V_考试形式 f on f.编号=a.考试形式 " +
			"left join V_成绩管理_科目课程信息表 g on g.学年学期编码=a.学年学期编码 and g.专业代码=a.系代码 and g.年级代码=a.年级代码 and g.课程代码=a.课程代码 and g.课程类型='4' " +
			"where 1=1";		
		
		//判断查询条件
		if(!"".equalsIgnoreCase(XNXQMC_CX)){
			sql += " and left(a.学年学期编码,5)='" + MyTools.fixSql(XNXQMC_CX) + "'";
		}
		if(!"".equalsIgnoreCase(JXXZ_CX)){
			sql += " and right(a.学年学期编码,2)='" + MyTools.fixSql(JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(XDM_CX)){
			sql += " and a.系代码='" + MyTools.fixSql(XDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and a.年级代码='" + MyTools.fixSql(NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		sql += " order by a.学年学期编码 desc,a.创建时间 desc";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 新增课程
	 * @date:2016-12-07
	 * @author:yeq
	 * @return
	 * @throws SQLException 
	 */
	public void addCourse() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		
		//判断课程是否已存在
		sql = "select count(*) from V_规则管理_分层课程信息表 " +
			"where 学年学期编码='" + MyTools.fixSql(this.getGF_XNXQBM()+this.getGF_JXXZ()) + "' " +
			"and 系代码='" + MyTools.fixSql(this.getGF_XDM()) + "' " +
			"and 年级代码='" + MyTools.fixSql(this.getGF_NJDM()) + "' " +
			"and 课程代码='" + MyTools.fixSql(this.getGF_KCDM()) + "'";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("分层课程已存在，无法新增！");
		}else {
			this.setGF_FCKCBH(db.getMaxID("dbo.V_基础信息_分层课程信息表", "分层课程编号", "FCKC_", 8));// 获取主关键字
			this.setGF_XNXQBM(this.getGF_XNXQBM()+this.getGF_JXXZ());
			
			sql = "insert into V_规则管理_分层课程信息表 values(" +
				"'" + MyTools.fixSql(this.getGF_FCKCBH()) + "'," + //分层课程编号
				"'" + MyTools.fixSql(this.getGF_XNXQBM()) + "'," + //学年学期编码
				"'" + MyTools.fixSql(this.getGF_XDM()) + "'," + //系代码
				"'" + MyTools.fixSql(this.getGF_NJDM()) + "'," + //年级代码
				"'" + MyTools.fixSql(this.getGF_KCDM()) + "'," + //课程代码
				"'" + MyTools.fixSql(this.getGF_KCMC().replaceAll("（" + this.getGF_KCDM() + "）", "")) + "'," + //课程名称
				"'" + MyTools.fixSql(this.getGF_SKZC()) + "'," + //授课周次
				"'" + MyTools.fixSql(this.getGF_SKZCMC()) + "'," + //授课周次名称
				"'" + MyTools.fixSql(this.getGF_XF()) + "'," + //学分
				"'" + MyTools.fixSql(this.getGF_ZKS()) + "'," + //总课时
				"'" + MyTools.fixSql(this.getGF_KSXS()) + "'," + //考试形式
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," + //创建人
				"getDate(),'1')";
			sqlVec.add(sql);
			
			sql = "insert into V_成绩管理_科目课程信息表 (学年学期编码,学年学期名称,年级代码,年级名称,专业代码,专业名称,课程代码,课程名称,课程类型," +
				"科目类型,是否参与绩点,是否参与留级,总评比例选项,平时比例,期中比例,实训比例,期末比例,成绩类型,实训,创建人,创建时间,状态) " +
				"select '" +  MyTools.fixSql(this.getGF_XNXQBM()) + "' as 学年学期编码," +
				"(select 学年学期名称 from V_规则管理_学年学期表 where 学年学期编码='" +  MyTools.fixSql(this.getGF_XNXQBM()) + "') as 学年学期名称," +
				"'" + MyTools.fixSql(this.getGF_NJDM()) + "' as 年级代码," +
				"(select 年级名称 from V_学校年级数据子类 where 年级代码='" + MyTools.fixSql(this.getGF_NJDM()) + "') as 年级名称," +
				"'" + MyTools.fixSql(this.getGF_XDM()) + "' as 专业代码," +
				"(select 系名称 from V_基础信息_系基础信息表 where 系代码='" + MyTools.fixSql(this.getGF_XDM()) + "') as 专业名称," +
				"'" + MyTools.fixSql(this.getGF_KCDM()) + "' as 课程代码," +
				"'" + MyTools.fixSql(this.getGF_KCMC().replaceAll("（" + this.getGF_KCDM() + "）", "")) + "' as 课程名称," +
				"'4','1','1','1','1','20','','','80','1','0','" + MyTools.fixSql(this.getUSERCODE()) + "',getDate(),'1'";
			sqlVec.add(sql);
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 编辑课程
	 * @date:2016-12-07
	 * @author:yeq
	 * @return
	 * @throws SQLException 
	 */
	public void editCourse() throws SQLException {
		String sql = "";
		
		sql = "update V_规则管理_分层课程信息表 set " +
			"授课周次='" + MyTools.fixSql(this.getGF_SKZC()) + "'," +
			"授课周次名称='" + MyTools.fixSql(this.getGF_SKZCMC()) + "'," +
			"学分='" + MyTools.fixSql(this.getGF_XF()) + "'," +
			"总课时='" + MyTools.fixSql(this.getGF_ZKS()) + "'," +
			"考试形式='" + MyTools.fixSql(this.getGF_KSXS()) + "' " +
			"where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "'";
			
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除课程
	 * @date:2016-12-07
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void delCourse() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		
		sql = "delete from V_规则管理_分层班学生信息表 " +
			"where 分层班编号 in (select 分层班编号 from V_规则管理_分层班信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "')";
		sqlVec.add(sql);
		
		sql = "delete from V_成绩管理_登分设置信息表 where 相关编号 in (" +
			"select 分层班编号 from V_规则管理_分层班信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "')";
		sqlVec.add(sql);
		
		//学生成绩信息由该表触发器自动删除
		sql = "delete from V_成绩管理_登分教师信息表 where 相关编号 in (" +
			"select 分层班编号 from V_规则管理_分层班信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "')";
		sqlVec.add(sql);
		
		sql = "delete from V_规则管理_分层班信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "'";
		sqlVec.add(sql);
		
		sql = "delete from V_成绩管理_科目课程信息表 where 科目编号='" + MyTools.fixSql(this.getGF_KMBH()) + "'";
		sqlVec.add(sql);
		
		sql = "delete from V_规则管理_分层课程信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 分页查询 班级列表
	 * @date:2016-12-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queClassList() throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select 分层班编号,分层班名称,replace(授课教师编号,'@','') as 授课教师编号,replace(授课教师姓名,'@','') as 授课教师姓名 " +
			"from V_规则管理_分层班信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "'";		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 新增班级
	 * @date:2016-12-08
	 * @author:yeq
	 * @return
	 * @throws SQLException 
	 */
	public void addClass() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		
		//判断课程是否已存在
		sql = "select count(*) from V_规则管理_分层班信息表 where 分层班名称='" + MyTools.fixSql(this.getGF_FCBMC()) + "'";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("分层班名称已存在，无法新增！");
		}else {
			this.setGF_FCBBH(db.getMaxID("dbo.V_基础信息_分层班信息表", "分层班编号", "FCKCMX_", 8));// 获取主关键字
			
			//解析教师姓名
			String teaCode[] = this.getGF_SKJSBH().split(",");
			String tempTeaName = this.getGF_SKJSXM();
			for(int i=0; i<teaCode.length; i++){
				tempTeaName = tempTeaName.replaceAll("（"+teaCode[i]+"）", "");
			}
			this.setGF_SKJSXM(tempTeaName);
			
			sql = "insert into V_规则管理_分层班信息表 values(" +
				"'" + MyTools.fixSql(this.getGF_FCBBH()) + "'," + //分层班编号
				"'" + MyTools.fixSql(this.getGF_FCKCBH()) + "'," + //分层课程编号
				"'" + MyTools.fixSql(this.getGF_FCBMC()) + "'," + //分层班名称
				"'@" + MyTools.fixSql(this.getGF_SKJSBH().replaceAll(",","@,@")) + "@'," + //授课教师编号
				"'@" + MyTools.fixSql(this.getGF_SKJSXM().replaceAll(",","@,@")) + "@'," + //授课教师姓名
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," + //创建人
				"getDate(),'1')";
			sqlVec.add(sql);
			
			sql = "insert into V_成绩管理_登分设置信息表 (相关编号,考试类型,总评比例选项,平时比例,期中比例,实训比例,期末比例,成绩类型,实训,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getGF_FCBBH()) + "'," +
				"'1'," +
				"'1'," +
				"'20'," +
				"''," +
				"''," +
				"'80'," +
				"'1'," +
				"'0'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate()," +
				"'1')";
			sqlVec.add(sql);
			
			sql = "insert into V_成绩管理_登分教师信息表 (科目编号,来源类型,相关编号,行政班代码,行政班名称,课程代码,课程名称,登分教师编号,登分教师姓名,打印锁定,创建人,创建时间,状态) values(" +
				"'" +  MyTools.fixSql(this.getGF_KMBH()) + "'," +
				"'4'," +
				"'" + MyTools.fixSql(this.getGF_FCBBH()) + "'," +
				"''," +
				"'" + MyTools.fixSql(this.getGF_FCBMC()) + "'," +
				"(select 课程代码 from V_规则管理_分层班信息表 a left join V_规则管理_分层课程信息表 b on b.分层课程编号=a.分层课程编号 " +
				"where a.分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "')," +
				"(select 课程名称 from V_规则管理_分层班信息表 a left join V_规则管理_分层课程信息表 b on b.分层课程编号=a.分层课程编号 " +
				"where a.分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "')," +
				"'@" + MyTools.fixSql(this.getGF_SKJSBH().replaceAll(",", "@,@")) + "@'," +
				"'@" + MyTools.fixSql(this.getGF_SKJSXM().replaceAll(",", "@,@")) + "@'," +
				"'0'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate()," +
				"'1')";
			sqlVec.add(sql);
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 编辑班级
	 * @date:2016-12-08
	 * @author:yeq
	 * @return
	 * @throws SQLException 
	 */
	public void editClass() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		
		//判断课程是否已存在
		sql = "select count(*) from V_规则管理_分层班信息表 " +
			"where 分层班编号<>'" + MyTools.fixSql(this.getGF_FCBBH()) + "' and 分层班名称='" + MyTools.fixSql(this.getGF_FCBMC()) + "'";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("分层班名称已存在，无法修改！");
		}else {
			//解析教师姓名
			String teaCode[] = this.getGF_SKJSBH().split(",");
			String tempTeaName = this.getGF_SKJSXM();
			for(int i=0; i<teaCode.length; i++){
				tempTeaName = tempTeaName.replaceAll("（"+teaCode[i]+"）", "");
			}
			this.setGF_SKJSXM(tempTeaName);
			
			sql = "update V_规则管理_分层班信息表 set " +
				"分层班名称='" + MyTools.fixSql(this.getGF_FCBMC()) + "'," +
				"授课教师编号='@" + MyTools.fixSql(this.getGF_SKJSBH().replaceAll(",", "@,@")) + "@'," +
				"授课教师姓名='@" + MyTools.fixSql(this.getGF_SKJSXM().replaceAll(",", "@,@")) + "@' " +
				"where 分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "'";
			sqlVec.add(sql);
			
			sql = "update V_成绩管理_登分教师信息表 set " +
				"行政班名称='" + MyTools.fixSql(this.getGF_FCBMC()) + "'," +
				"登分教师编号='@" + MyTools.fixSql(this.getGF_SKJSBH().replaceAll(",", "@,@")) + "@'," +
				"登分教师姓名='@" + MyTools.fixSql(this.getGF_SKJSXM().replaceAll(",", "@,@")) + "@' " +
				"where 相关编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "'";
			sqlVec.add(sql);
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 删除班级
	 * @date:2016-12-08
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void delClass() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		
		sql = "delete from V_规则管理_分层班学生信息表 where 分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "'";
		sqlVec.add(sql);
		
		sql = "delete from V_规则管理_分层班信息表 where 分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "'";
		sqlVec.add(sql);
		
		//学生成绩信息由该表触发器自动删除
		sql = "delete from V_成绩管理_登分教师信息表 where 相关编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 分页查询 学生列表
	 * @date:2016-12-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queStuList() throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select c.行政班名称,b.学号,b.姓名 from V_规则管理_分层班学生信息表 a " +
			"left join V_学生基本数据子类  b on b.学号=a.学号 " +
			"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
			"where a.分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "' " +
			"order by c.行政班代码,b.学号";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询班级学生树数据
	 * @date:2016-03-08
	 * @author:yeq
	 * @param parentCode
	 * @param classCode
	 * @return 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queClassStuTree(String parentCode)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		
		//判断层级
		if("".equalsIgnoreCase(parentCode)){
			sql = "select distinct 'zy-'+b.专业代码 as id,b.专业名称+'（'+b.专业代码+'）' as text,state='closed' from V_学校班级数据子类 a " +
				"left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
				"left join V_基础信息_系专业关系表 c on c.专业代码=b.专业代码 " +
				"where a.状态='1' and LEN(b.专业代码)<>3 and c.系代码=(" +
				"select 系代码 from V_规则管理_分层课程信息表 where 分层课程编号='" + MyTools.fixSql(this.getGF_FCKCBH()) + "')";
		}else{
			String parentCodeArray[] = parentCode.split("-");
			
			if("zy".equalsIgnoreCase(parentCodeArray[0])){
				sql = "select id,text,state='closed' from (select distinct 'bj-'+行政班代码 as id,行政班名称 as text," +
					"(select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=a.行政班代码) as num " +
					"from V_学校班级数据子类 a " +
					"where 状态='1' " +
					"and 专业代码='" + MyTools.fixSql(parentCodeArray[1]) + "') as t where num>0 " +
					"order by id";
			}else{
				sql = "select distinct 学号 as id,姓名 as text,state='open' " +
					"from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码='" + MyTools.fixSql(parentCodeArray[1]) + "' " +
					"and 学号 not in (select 学号 from V_规则管理_分层班学生信息表 where 分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "') " +
					"order by 学号";
			}
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 保存新增学生
	 * @date:2016-12-08
	 * @author:yeq
	 * @stuCode 新增学生编号
	 * @throws SQLException
	 */
	public void saveAddStu()throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		String stuCodeArray[] = this.getGF_XH().split(",");
		
		for(int i=0; i<stuCodeArray.length; i++){
			sql = "insert into V_规则管理_分层班学生信息表 (分层班编号,学号,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getGF_FCBBH()) + "'," +
				"'" + MyTools.fixSql(stuCodeArray[i]) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除分层班学生
	 * @date:2016-12-08
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void delStu() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		
		sql = "delete from V_规则管理_分层班学生信息表 " +
			"where 分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "' " +
			"and 学号 in ('" + this.getGF_XH().replaceAll(",", "','") + "')";
		sqlVec.add(sql);
		
		sql = "delete from V_成绩管理_学生成绩信息表 " +
			"where 相关编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "' " +
			"and 学号 in ('" + this.getGF_XH().replaceAll(",", "','") + "')";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 读取学期周次
	 * @date:2016-12-09
	 * @author:yeq
	 * @throws SQLException 
	 */
	public String loadXqzc() throws SQLException {
		Vector vec = null;
		String sql = "";
		String xqzc = "20";
		
		//查询学期总周次数
		sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getGF_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			xqzc = MyTools.StrFiltr(vec.get(0));
		}
		
		return xqzc;
	}
	
	/**
	 * 导入分层班学生名单
	 * @date:2016-12-19
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void importStuList() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		Vector stuVec = null;
		
		sql = "select a.学号 from V_规则管理_分层班学生信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
			"where b.学生状态 in ('01','05') and " +
			"a.分层班编号='" + MyTools.fixSql(this.getGF_FCBMC()) + "' " +
			"and a.学号 not in (select 学号 from V_规则管理_分层班学生信息表 " +
			"where 分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "') " +
			"and c.年级代码=(select t1.年级代码 from V_规则管理_分层班信息表 t left join V_规则管理_分层课程信息表 t1 on t1.分层课程编号=t.分层课程编号 " +
			"where t.分层班编号='" + MyTools.fixSql(this.getGF_FCBBH()) + "')";
		stuVec = db.GetContextVector(sql);
		
		if(stuVec!=null && stuVec.size()>0){
			for(int i=0; i<stuVec.size(); i++){
				sql = "insert into V_规则管理_分层班学生信息表 (分层班编号,学号,创建人,创建时间,状态) values(" +
					"'" + MyTools.fixSql(this.getGF_FCBBH()) + "'," +
					"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i))) + "'," +
					"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
					"getDate(),'1')";
				sqlVec.add(sql);
			}
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("导入成功");
			}else{
				this.setMSG("导入失败");
			}
		}else{
			this.setMSG("导入成功");
		}
	}
	
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

	public String getGF_FCKCBH() {
		return GF_FCKCBH;
	}

	public void setGF_FCKCBH(String gF_FCKCBH) {
		GF_FCKCBH = gF_FCKCBH;
	}

	public String getGF_XNXQBM() {
		return GF_XNXQBM;
	}

	public void setGF_XNXQBM(String gF_XNXQBM) {
		GF_XNXQBM = gF_XNXQBM;
	}

	public String getGF_JXXZ() {
		return GF_JXXZ;
	}

	public void setGF_JXXZ(String gF_JXXZ) {
		GF_JXXZ = gF_JXXZ;
	}

	public String getGF_XDM() {
		return GF_XDM;
	}

	public void setGF_XDM(String gF_XDM) {
		GF_XDM = gF_XDM;
	}

	public String getGF_NJDM() {
		return GF_NJDM;
	}

	public void setGF_NJDM(String gF_NJDM) {
		GF_NJDM = gF_NJDM;
	}

	public String getGF_KCDM() {
		return GF_KCDM;
	}

	public void setGF_KCDM(String gF_KCDM) {
		GF_KCDM = gF_KCDM;
	}

	public String getGF_KCMC() {
		return GF_KCMC;
	}

	public String getGF_SKZC() {
		return GF_SKZC;
	}

	public void setGF_SKZC(String gF_SKZC) {
		GF_SKZC = gF_SKZC;
	}

	public String getGF_SKZCMC() {
		return GF_SKZCMC;
	}

	public void setGF_SKZCMC(String gF_SKZCMC) {
		GF_SKZCMC = gF_SKZCMC;
	}

	public void setGF_KCMC(String gF_KCMC) {
		GF_KCMC = gF_KCMC;
	}

	public String getGF_XF() {
		return GF_XF;
	}

	public void setGF_XF(String gF_XF) {
		GF_XF = gF_XF;
	}

	public String getGF_ZKS() {
		return GF_ZKS;
	}

	public void setGF_ZKS(String gF_ZKS) {
		GF_ZKS = gF_ZKS;
	}

	public String getGF_KSXS() {
		return GF_KSXS;
	}

	public void setGF_KSXS(String gF_KSXS) {
		GF_KSXS = gF_KSXS;
	}

	public String getGF_FCBBH() {
		return GF_FCBBH;
	}

	public void setGF_FCBBH(String gF_FCBBH) {
		GF_FCBBH = gF_FCBBH;
	}

	public String getGF_FCBMC() {
		return GF_FCBMC;
	}

	public void setGF_FCBMC(String gF_FCBMC) {
		GF_FCBMC = gF_FCBMC;
	}

	public String getGF_SKJSBH() {
		return GF_SKJSBH;
	}

	public void setGF_SKJSBH(String gF_SKJSBH) {
		GF_SKJSBH = gF_SKJSBH;
	}

	public String getGF_SKJSXM() {
		return GF_SKJSXM;
	}

	public void setGF_SKJSXM(String gF_SKJSXM) {
		GF_SKJSXM = gF_SKJSXM;
	}

	public String getGF_ID() {
		return GF_ID;
	}

	public void setGF_ID(String gF_ID) {
		GF_ID = gF_ID;
	}

	public String getGF_XH() {
		return GF_XH;
	}

	public void setGF_XH(String gF_XH) {
		GF_XH = gF_XH;
	}

	public String getGF_CJR() {
		return GF_CJR;
	}

	public void setGF_CJR(String gF_CJR) {
		GF_CJR = gF_CJR;
	}

	public String getGF_CJSJ() {
		return GF_CJSJ;
	}

	public void setGF_CJSJ(String gF_CJSJ) {
		GF_CJSJ = gF_CJSJ;
	}

	public String getGF_ZT() {
		return GF_ZT;
	}

	public void setGF_ZT(String gF_ZT) {
		GF_ZT = gF_ZT;
	}

	public String getGF_KMBH() {
		return GF_KMBH;
	}

	public void setGF_KMBH(String gF_KMBH) {
		GF_KMBH = gF_KMBH;
	}
	
	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}	
}