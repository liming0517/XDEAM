package com.pantech.devolop.registerScoreSet;
/*
@date 2016.03.11
@author yeq
模块：M6.2 登分设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class DfszBean {
	private String USERCODE;//用户编号
	private String Auth;//用户权限
	private String CD_ID; //编号
	private String CD_XNXQBM; //学年学期编码
	private String CD_KSSJ; //开始时间
	private String CD_JSSJ; //结束时间
	private String CD_DBKXNFW; //大补考学期范围
	private String CJ_JSBH; //教师编号
	private String CD_CJR; //创建人
	private String CD_CJSJ; //创建时间
	private String CD_ZT; //状态
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public DfszBean(HttpServletRequest request) {
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
		Auth = "";//用户权限
		CD_ID = ""; //编号
		CD_XNXQBM = ""; //学年学期编码
		CD_KSSJ = ""; //开始时间
		CD_JSSJ = ""; //结束时间
		CD_DBKXNFW = ""; //大补考学期范围
		CJ_JSBH = ""; //教师编号
		CD_CJR = ""; //创建人
		CD_CJSJ = ""; //创建时间
		CD_ZT = ""; //状态
	}

	/**
	 * 分页查询 查询学年学期列表
	 * @date:2016-03-11
	 * @author:yeq
	 * @param pageNum 页数
	 * @param page 每页数据条数
	 * @param XNXQMC_CX 学年学期名称
	 * @param JXXZ_CX 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSemList(int pageNum, int page, String XNXQMC_CX, String JXXZ_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select distinct a.学年学期编码 as CD_XNXQBM,a.学年学期名称 as CD_XNXQMC,b.教学性质 as jxxz," +
			"isnull(convert(nvarchar(10),c.开始时间,21), '未设置') as CD_KSSJ,isnull(convert(nvarchar(10),c.结束时间,21), '未设置') as CD_JSSJ," +
			"isnull(convert(nvarchar(10),c.补考开始时间,21), '未设置') as CD_BKKSSJ,isnull(convert(nvarchar(10),c.补考结束时间,21), '未设置') as CD_BKJSSJ," +
			"isnull(convert(nvarchar(10),c.大补考开始时间,21), '未设置') as CD_DBKKSSJ,isnull(convert(nvarchar(10),c.大补考结束时间,21), '未设置') as CD_DBKJSSJ," +
			"case c.大补考学年范围 when '0' then '全部学年' when '1' then '不包含当前学年' when '2' then '仅当前学年' else '未设置' end as CD_DBKXNFW," +
			"isnull(stuff((select ','+isnull(教师编号,'') from V_成绩管理_教师登分权限表 where 状态='1' and 学年学期编码=a.学年学期编码 for XML PATH('')),1,1,''),'') as CJ_JSBH " +
			"from V_规则管理_学年学期表 a " +
			"left join V_基础信息_教学性质 b on b.编号=a.教学性质 " +
			"left join V_成绩管理_登分时间表 c on c.学年学期编码=a.学年学期编码 " +
			"where a.状态='1'";
		
		//判断查询条件
		if(!"".equalsIgnoreCase(XNXQMC_CX)){
			sql += " and left(a.学年学期编码,5)='" + MyTools.fixSql(XNXQMC_CX) + "'";
		}
		if(!"".equalsIgnoreCase(JXXZ_CX)){
			sql += " and a.教学性质='" + MyTools.fixSql(JXXZ_CX) + "'";
		}
		sql += " order by a.学年学期编码 desc";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2016-06-29
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
	 * @date:2016-03-11
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJXXZCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
				"from V_基础信息_教学性质 where 状态='1'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**查询教师下拉框数据
	 * @author 2016-11-16
	 * @author yeq
	 * @return
	 * @throws SQLException
	 */
	public Vector loadBeforeTeaCombo() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName,0 as orderNum " +
				"union all " +
				"select 工号,姓名+'（'+工号+'）',1 from V_教职工基本数据子类 " +
				"where 姓名 in (select distinct 姓名 from (select 姓名,count(*) as num from V_教职工基本数据子类 group by 姓名) as t where t.num>1) " +
				"order by orderNum,comboName,comboValue";			
		vec = dbSource.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 保存登分时间
	 * @date:2016-03-11
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveTime() throws SQLException{
		String sql = "";
		
		sql = "select count(*) from V_成绩管理_登分时间表 where 学年学期编码='" + MyTools.fixSql(this.getCD_XNXQBM()) + "'";
		
		if(db.getResultFromDB(sql)){
			sql = "update V_成绩管理_登分时间表 set " +
				"开始时间='" + MyTools.fixSql(this.getCD_KSSJ()) + "'," +
				"结束时间='" + MyTools.fixSql(this.getCD_JSSJ()) + "'," +
				"创建人='" + MyTools.fixSql(this.getUSERCODE())  +"',创建时间=getDate() " +
				"where 学年学期编码='" + MyTools.fixSql(this.getCD_XNXQBM()) + "'";
		}else{
			sql = "insert into V_成绩管理_登分时间表(学年学期编码,开始时间,结束时间,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getCD_XNXQBM()) + "'," +
				"'" + MyTools.fixSql(this.getCD_KSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getCD_JSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 保存补考登分时间
	 * @date:2016-08-03
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveBkTime() throws SQLException{
		String sql = "";
		
		sql = "select count(*) from V_成绩管理_登分时间表 where 学年学期编码='" + MyTools.fixSql(this.getCD_XNXQBM()) + "'";
		
		if(db.getResultFromDB(sql)){
			sql = "update V_成绩管理_登分时间表 set " +
				"补考开始时间='" + MyTools.fixSql(this.getCD_KSSJ()) + "'," +
				"补考结束时间='" + MyTools.fixSql(this.getCD_JSSJ()) + "'," +
				"创建人='" + MyTools.fixSql(this.getUSERCODE())  +"',创建时间=getDate() " +
				"where 学年学期编码='" + MyTools.fixSql(this.getCD_XNXQBM()) + "'";
		}else{
			sql = "insert into V_成绩管理_登分时间表(学年学期编码,补考开始时间,补考结束时间,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getCD_XNXQBM()) + "'," +
				"'" + MyTools.fixSql(this.getCD_KSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getCD_JSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 保存大补考登分时间
	 * @date:2016-12-26
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveDbkTime() throws SQLException{
		String sql = "";
		
		sql = "select count(*) from V_成绩管理_登分时间表 where 学年学期编码='" + MyTools.fixSql(this.getCD_XNXQBM()) + "'";
		
		if(db.getResultFromDB(sql)){
			sql = "update V_成绩管理_登分时间表 set " +
				"大补考开始时间='" + MyTools.fixSql(this.getCD_KSSJ()) + "'," +
				"大补考结束时间='" + MyTools.fixSql(this.getCD_JSSJ()) + "'," +
				"大补考学年范围='" + MyTools.fixSql(this.getCD_DBKXNFW()) + "'," +
				"创建人='" + MyTools.fixSql(this.getUSERCODE())  +"',创建时间=getDate() " +
				"where 学年学期编码='" + MyTools.fixSql(this.getCD_XNXQBM()) + "'";
		}else{
			sql = "insert into V_成绩管理_登分时间表(学年学期编码,大补考开始时间,大补考结束时间,大补考学年范围,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getCD_XNXQBM()) + "'," +
				"'" + MyTools.fixSql(this.getCD_KSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getCD_JSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getCD_DBKXNFW()) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 查询教师列表
	 * @date:2016-03-11
	 * @author:yeq
	 * @param DFJSBH_CX 教师编号
	 * @param DFJSMC_CX 教师名称
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadTeaList(int pageNum, int pageSize, String DFJSBH_CX, String DFJSMC_CX) throws SQLException{
		Vector vec = null;
		String sql = "";
		
		//获取所有教师信息
		sql = "select * from (select distinct top 100 percent 工号,姓名 from V_教职工基本数据子类 " +
			"where 工号 in ('" + this.getCJ_JSBH().replaceAll(",", "','") + "') order by 工号) as t1 " +
			"union all " +
			"select * from (select distinct top 100 percent 工号,姓名 from V_教职工基本数据子类 " +
			"where 工号 not in ('" + this.getCJ_JSBH().replaceAll(",", "','") + "')";
		//判断查询条件
		if(!"".equalsIgnoreCase(DFJSBH_CX)){
			sql += " and 工号 like '%" + MyTools.fixSql(DFJSBH_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(DFJSMC_CX)){
			sql += " and 姓名 like '%" + MyTools.fixSql(DFJSMC_CX) + "%'";
		}
		sql += " order by 工号) as t2";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	/**
	 * 保存教师登分权限
	 * @date:2016-03-11
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveTea() throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		
		sql = "delete from V_成绩管理_教师登分权限表 where 学年学期编码='" + MyTools.fixSql(this.getCD_XNXQBM()) + "'";
		sqlVec.add(sql);
		
		if(!"".equalsIgnoreCase(this.getCJ_JSBH())){
			String teaCodeArray[] = this.getCJ_JSBH().split(",");
			
			for(int i=0; i<teaCodeArray.length; i++){
				sql = "insert into V_成绩管理_教师登分权限表 (学年学期编码,教师编号,创建人,创建时间,状态) values(" +
					"'" + MyTools.fixSql(this.getCD_XNXQBM()) + "'," +
					"'" + MyTools.fixSql(teaCodeArray[i]) + "'," +
					"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
					"getDate(),'1')";
				sqlVec.add(sql);
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 读取同名教师下拉框数据
	 * @date:2016-11-16
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSameNameTeaCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName,0 as orderNum " +
				"union all " +
				"select 工号,姓名+'（'+工号+'）',1 from V_教职工基本数据子类 " +
				"where 工号<>'" + MyTools.fixSql(this.getCJ_JSBH()) + "' " +
				"and 姓名=(select 姓名 from V_教职工基本数据子类 where 工号='" + MyTools.fixSql(this.getCJ_JSBH()) + "') " +
				"order by orderNum,comboValue";			
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 重置教师账号
	 * @date:2016-11-28
	 * @author:yeq
	 * @param beforeTea 原教师账号
	 * @param afterTea 现教师账号
	 * @throws SQLException
	 */
	public void resetTea(String beforeTea, String afterTea) throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		String tempStr = "'@'+replace(replace(replace(replace(授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@'";
		
		//基础信息模块
		sql = "update V_学校班级数据子类 set 班主任工号='" + MyTools.fixSql(afterTea) + "' " +
			"where 班主任工号='" + MyTools.fixSql(beforeTea) + "'";
		sqlVec.add(sql);
		
		sql = "update V_专业组组长信息 " +
			"set 教师代码=replace(教师代码,'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@') " +
			"where 教师代码 like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		//排课管理模块
		sql = "update V_规则管理_授课计划明细表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_排课管理_课程表明细详情表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_排课管理_课程表周详情表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_排课管理_添加课程信息表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_数据对接_课程表明细详情表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		//调课管理
		sql = "update V_调课管理_调课信息主表 " +
			"set 原计划授课教师编号=replace(replace('@'+replace(replace(replace(replace(原计划授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@','@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','')," +
			"调整后授课教师编号=replace(replace('@'+replace(replace(replace(replace(调整后授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@','@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " + 
			"where '@'+replace(replace(replace(replace(原计划授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@" + MyTools.fixSql(beforeTea) + "@%' " +
			"or '@'+replace(replace(replace(replace(调整后授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_调课管理_调课信息明细表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		//选修课管理模块
		sql = "update V_规则管理_选修课授课计划主表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_规则管理_选修课授课计划明细表 " +
			"set 授课教师编号=replace(replace(" + tempStr + ",'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where " + tempStr + " like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		//分层班模块
		sql = "update V_规则管理_分层班信息表 " +
			"set 授课教师编号=replace(授课教师编号,'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@')" +
			"where 授课教师编号 like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		//评教管理模块
		sql = "update V_问卷调查_辅导员信息表 set 工号='" + MyTools.fixSql(afterTea) + "' " +
			"where 工号='" + MyTools.fixSql(beforeTea) + "'";
		sqlVec.add(sql);
		
		sql = "update V_问卷调查_教师满意度评分表 set 教师编号='" + MyTools.fixSql(afterTea) + "' " +
			"where 教师编号='" + MyTools.fixSql(beforeTea) + "'";
		sqlVec.add(sql);
		
		//考务管理模块
		sql = "update V_考试管理_监考教师 set 监考教师编号='" + MyTools.fixSql(afterTea) + "' " +
			"where 监考教师编号='" + MyTools.fixSql(beforeTea) + "'";
		sqlVec.add(sql);
		
		sql = "update V_考试管理_考场安排明细表 " +
			"set 监考教师编号=replace(replace('@'+replace(监考教师编号,',','@,@')+'@','@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@'),'@','') " +
			"where '@'+replace(监考教师编号,',','@,@')+'@' like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		//成绩管理系统
		sql = "update V_成绩管理_登分教师信息表 " +
			"set 登分教师编号=replace(登分教师编号,'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@') " +
			"where 登分教师编号 like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_成绩管理_补考登分教师信息表 " +
			"set 登分教师编号=replace(登分教师编号,'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@') " +
			"where 登分教师编号 like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_成绩管理_大补考登分教师信息表 " +
			"set 登分教师编号=replace(登分教师编号,'@" + MyTools.fixSql(beforeTea) + "@','@" + MyTools.fixSql(afterTea) + "@') " +
			"where 登分教师编号 like '%@" + MyTools.fixSql(beforeTea) + "@%'";
		sqlVec.add(sql);
		
		sql = "update V_成绩管理_教师登分权限表 set 教师编号='" + MyTools.fixSql(afterTea) + "' " +
			"where 教师编号='" + MyTools.fixSql(beforeTea) + "'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("替换成功");
		}else{
			this.setMSG("替换失败");
		}
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

	public String getCD_ID() {
		return CD_ID;
	}

	public void setCD_ID(String cD_ID) {
		CD_ID = cD_ID;
	}

	public String getCD_XNXQBM() {
		return CD_XNXQBM;
	}

	public void setCD_XNXQBM(String cD_XNXQBM) {
		CD_XNXQBM = cD_XNXQBM;
	}

	public String getCD_KSSJ() {
		return CD_KSSJ;
	}

	public void setCD_KSSJ(String cD_KSSJ) {
		CD_KSSJ = cD_KSSJ;
	}

	public String getCD_JSSJ() {
		return CD_JSSJ;
	}

	public void setCD_JSSJ(String cD_JSSJ) {
		CD_JSSJ = cD_JSSJ;
	}

	public String getCD_DBKXNFW() {
		return CD_DBKXNFW;
	}

	public void setCD_DBKXNFW(String cD_DBKXNFW) {
		CD_DBKXNFW = cD_DBKXNFW;
	}

	public String getCJ_JSBH() {
		return CJ_JSBH;
	}

	public void setCJ_JSBH(String cJ_JSBH) {
		CJ_JSBH = cJ_JSBH;
	}

	public String getCD_CJR() {
		return CD_CJR;
	}

	public void setCD_CJR(String cD_CJR) {
		CD_CJR = cD_CJR;
	}

	public String getCD_CJSJ() {
		return CD_CJSJ;
	}

	public void setCD_CJSJ(String cD_CJSJ) {
		CD_CJSJ = cD_CJSJ;
	}

	public String getCD_ZT() {
		return CD_ZT;
	}

	public void setCD_ZT(String cD_ZT) {
		CD_ZT = cD_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}