package com.pantech.src.devolop.questSurvey;

import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class WjszBean {
	private String AUTHCODE;// 用户权限
	private String WW_WJBH;// 问卷编号
	private String WW_WJLX;// 问卷类型
	private String WW_BT;// 标题
	private String WW_XNXQBM;// 学年学期编码
	private String WW_KSSJ;// 开始时间
	private String WW_JSSJ;// 结束时间
	private String WW_CJR;// 创建人
	private String WW_CJSJ;// 创建时间
	private String WW_ZT;// 状态
	private String WW_TMBH;// 题目编号
	private String WW_GH;// 辅导员工号
	private String WW_XM;// 辅导员姓名
	private String WW_XZBDM;// 行政班代码
	private String WW_XZBMC;// 行政班名称
	private String WW_ZYDM;// 专业代码
	private String WW_WJBH1;// 测试问卷编号
	private String WW_XZBDM1;// 行政班代码之前的

	private String USERCODE;
	private String listsql;// 查询sql语句

	// 以下变量为此类的固定变量
	protected HttpServletRequest request;
	private DBSource db;
	private String MSG;

	/**
	 * 初始化变量
	 * @date:2016-6-8
	 * @author:
	 */
	private void initialData() {
		// TODO Auto-generated method stub
		AUTHCODE = "";// 用户权限
		WW_WJBH = "";// 问卷编号
		WW_WJLX = "";// 问卷类型
		WW_BT = "";// 标题
		WW_XNXQBM = "";// 学年学期编码
		WW_KSSJ = "";// 开始时间
		WW_JSSJ = "";// 结束时间
		WW_CJR = "";// 创建人
		USERCODE = "";// 用户编号
		WW_CJSJ = "";// 创建时间
		WW_ZT = "";// 状态
		WW_TMBH = "";// 题目编号
		WW_GH = "";// 辅导员工号
		WW_XM = "";// 辅导员姓名
		WW_XZBDM = "";// 行政班代码
		WW_XZBMC = "";// 行政班名称
		WW_ZYDM = "";// 专业代码
		WW_WJBH1 = "";// 测试问卷编号
		WW_XZBDM1="";// 行政班代码之前的
	}

	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public WjszBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}

	/**
	 * 分页查询 问卷信息列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select distinct * from(select 问卷编号,问卷类型,"
				+ " case when 问卷类型='01' then '单选题/多选题' when 问卷类型='02' then '表单题1' when 问卷类型='03' then '表单题2' end as 问卷类型名称,"
				+ "标题,a.学年学期编码,学年学期名称,CONVERT(nvarchar(10),开始时间,21) as 开始时间,CONVERT(nvarchar(10),结束时间,21) as 结束时间,"
				+ "a.创建人,CONVERT(nvarchar(10),a.创建时间,21) as 创建时间,a.状态 "
				+ "from V_问卷调查_问卷信息表  as a left join dbo.V_规则管理_学年学期表 as b on a.学年学期编码=b.学年学期编码) as t where 1=1 and 状态='1' ";
		// 判断查询条件
		if (!"".equalsIgnoreCase(this.getWW_BT())) {
			sql += " and 标题 like '%" + MyTools.fixSql(this.getWW_BT()) + "%'";
		}
		if (!"".equalsIgnoreCase(this.getWW_XNXQBM())
				&& !"null".equalsIgnoreCase(this.getWW_XNXQBM())) {
			sql += " and 学年学期编码 like '%" + MyTools.fixSql(this.getWW_XNXQBM())
					+ "%'";
		}
		if (!"".equalsIgnoreCase(this.getWW_KSSJ())) {
			sql += " and 开始时间 >= '" + MyTools.fixSql(this.getWW_KSSJ()) + "'";
		}
		if (!"".equalsIgnoreCase(this.getWW_JSSJ())) {
			sql += " and 结束时间 <= '" + MyTools.fixSql(this.getWW_JSSJ()) + "'";
		}

		sql = sql + " ORDER BY 创建时间 desc,问卷编号 desc";

		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	// 查询表单题一条记录
	public Vector queryOne(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select distinct * from(select 问卷编号,问卷类型,"
				+ " case when 问卷类型='01' then '单选题/多选题' when 问卷类型='02' then '表单题1' when 问卷类型='03' then '表单题2' end as 问卷类型名称,"
				+ "标题,a.学年学期编码,学年学期名称,CONVERT(nvarchar(10),开始时间,21) as 开始时间,CONVERT(nvarchar(10),结束时间,21) as 结束时间,"
				+ "a.创建人,CONVERT(nvarchar(10),a.创建时间,21) as 创建时间,a.状态 "
				+ "from V_问卷调查_问卷信息表 as a,dbo.V_规则管理_学年学期表 as b where 1=1 and a.学年学期编码=b.学年学期编码) as t where 1=1 and 问卷编号='"
				+ MyTools.fixSql(this.getWW_WJBH()) + "'";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	/**
	 * 2c-保存记录(新增或修改) 问卷
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void saveRec() throws WrongSQLException, SQLException {
		String sql = ""; // 执行用SQL语句
		// SQL语句 可变
		// 根据实际情况修改 视图/表名 及主关键子名称
		sql = "select count(*) from V_问卷调查_问卷信息表  where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		if (!db.getResultFromDB(sql)) {
			this.AddRec();
		} else {
			this.ModRec();
		}
	}

	/**
	 * 2d-新增记录 题目信息 问卷
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddRec() throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector(); // 结果集
		String sql = ""; // 执行用SQL语句
		//int curGrade = 0;

		if ("".equalsIgnoreCase(this.getWW_WJBH())) {
			this.setWW_WJBH(db.getMaxID("dbo.V_问卷调查_问卷信息表", "问卷编号", "WJDC_", 6));// 获取主关键字
		}
		sql = "insert into V_问卷调查_问卷信息表 (问卷编号,问卷类型,标题,学年学期编码,开始时间,结束时间,创建人,创建时间,状态) " +
			"values (" + "'"+ MyTools.fixSql(this.getWW_WJBH())+ "'," +
			"'" + MyTools.fixSql(this.getWW_WJLX()) + "'," +
			"'" + MyTools.fixSql(this.getWW_BT()) + "'," +
			"'" + MyTools.fixSql(this.getWW_XNXQBM()) + "'," +
			"'" + MyTools.fixSql(this.getWW_KSSJ()) + "'," +
			"'" + MyTools.fixSql(this.getWW_JSSJ()) + "'," +
			"'"+ MyTools.fixSql(this.USERCODE) + "'," +
			"getDate(),'1')";
		sqlVec.add(sql);
		
		// 生成辅导员信息
		/* 2016/10/31 yeq 客户要求不需要设置辅导员
		if ("03".equalsIgnoreCase(this.getWW_WJLX())) {
			curGrade = MyTools.StringToInt(this.getWW_XNXQBM().substring(2, 4));
			String tempStr = "'" + (curGrade) + "1','" + (curGrade - 1)
					+ "1'";
			sql = "insert into dbo.V_问卷调查_辅导员信息表 (问卷编号,工号,姓名,行政班代码,创建人,创建时间,状态) "
					+ "select '"
					+ MyTools.fixSql(this.getWW_WJBH())
					+ "',a.班主任工号,b.姓名,a.行政班代码,'"
					+ MyTools.fixSql(this.USERCODE)
					+ "',GETDATE(),'1' from V_学校班级数据子类 a "
					+ "left join V_教职工基本数据子类 b on b.工号=a.班主任工号 "
					+ "where a.年级代码 in ("
					+ tempStr
					+ ") and a.班主任工号 is not null";
			sqlVec.add(sql);
		}
		*/

		if (db.executeInsertOrUpdateTransaction(sqlVec)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("操作失败");
		}
	}

	/**
	 * 2e-修改记录 题目信息 问卷
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void ModRec() throws WrongSQLException, SQLException {
		String sql = "update dbo.V_问卷调查_问卷信息表  set " +
			"问卷类型='" + MyTools.fixSql(this.getWW_WJLX()) + "'," +
			"标题='" + MyTools.fixSql(this.getWW_BT()) + "'," +
			"学年学期编码='" + MyTools.fixSql(this.getWW_XNXQBM()) + "'," +
			"开始时间='" + MyTools.fixSql(this.getWW_KSSJ()) + "'," +
			"结束时间='" + MyTools.fixSql(this.getWW_JSSJ()) + "' " +
			"where 问卷编号='" + this.getWW_WJBH() + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "保存成功";
		} else {
			this.MSG = "操作失败";
		}
	}

	// 删除题目信息 问卷
	public void DelRec() throws SQLException, WrongSQLException {
		Vector sqlVec = new Vector(); // 结果集
		String sql = "";
		
		sql = "update dbo.V_问卷调查_辅导员信息表 set 状态='0' where 问卷编号='"+ MyTools.fixSql(this.getWW_WJBH()) +"'";
		sqlVec.add(sql);
		
		sql = "update dbo.V_问卷调查_教师满意度评分表 set 状态='0' where 问卷编号='"+ MyTools.fixSql(this.getWW_WJBH()) +"'";
		sqlVec.add(sql);
		
		sql = "update dbo.V_问卷调查_结果信息表 set 状态='0' where 问卷编号='"+ MyTools.fixSql(this.getWW_WJBH()) +"'";
		sqlVec.add(sql);
		
		sql = "update dbo.V_问卷调查_问卷题目关系表 set 状态='0' where 问卷编号='"+ MyTools.fixSql(this.getWW_WJBH()) +"'";
		sqlVec.add(sql);
		
		sql = "update dbo.V_问卷调查_问卷信息表 set 状态='0' where 问卷编号='"+ MyTools.fixSql(this.getWW_WJBH()) +"'";
		sqlVec.add(sql);
		
		sql = "update dbo.V_问卷调查_专业教师无限制信息表 set 状态='0' where 问卷编号='"+ MyTools.fixSql(this.getWW_WJBH()) +"'";
		sqlVec.add(sql);
		
		if (db.executeInsertOrUpdateTransaction(sqlVec)) {
			this.MSG = "删除成功";
		} else {
			this.MSG = "删除失败";
		}
	}

	// 查询问卷里的选择题
	public Vector queryWJXZT(int pageNum, int page) throws SQLException, WrongSQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集

		sql = "select a.问卷编号,a.题目编号,case when b.题目类型='01' then '单选题' when b.题目类型='02' then '多选题' else '表单题' end as 题目类型名称,b.题目内容 " +
			"from dbo.V_问卷调查_问卷题目关系表 as a left join dbo.V_问卷调查_题目信息_选择题 as b on a.题目编号=b.题目编号 " +
			"where a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		vec = db.getConttexJONSArr(sql, pageNum, page);
		return vec;
	}

	// 查询问卷里的表单题
	public Vector queryWJBDT(int pageNum, int page) throws SQLException, WrongSQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select a.问卷编号,a.题目编号,题目类型名称='表单题',b.题目内容,b.题目要点,b.分值 " +
			"from dbo.V_问卷调查_问卷题目关系表 as a left join dbo.V_问卷调查_题目信息_表单题 as b on a.题目编号=b.题目编号 " +
			"where a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	/**
	 * 2d-新增记录 题目信息 问卷里的题目(选择题)
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddWjtm(String xztARRAY) throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector();
		String sql = ""; // 执行用SQL语句
		String ARRAY[] = xztARRAY.split(",");
		for (int i = 0; i < ARRAY.length; i++) {
			sql = "INSERT INTO V_问卷调查_问卷题目关系表(问卷编号,题目编号,创建人,创建时间,状态)"
					+ "VALUES(" + "'" + MyTools.StrFiltr(this.getWW_WJBH())
					+ "'," + "'" + MyTools.StrFiltr(ARRAY[i]) + "'," + "'"
					+ MyTools.StrFiltr(this.USERCODE) + "'," + "GETDATE(),"
					+ "'1')";
			sqlVec.add(sql);
		}
		if (db.executeInsertOrUpdateTransaction(sqlVec)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("保存失败");
		}
	}

	/**
	 * 2d-新增记录 题目信息 问卷里的题目(表单题)
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddWjtmbdt(String bdtARRAY) throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector();
		String sql = ""; // 执行用SQL语句
		String ARRAY[] = bdtARRAY.split(",");
		
		for (int i = 0; i < ARRAY.length; i++) {
			sql = "INSERT INTO V_问卷调查_问卷题目关系表(问卷编号,题目编号,创建人,创建时间,状态)"
					+ "VALUES(" + "'" + MyTools.StrFiltr(this.getWW_WJBH())
					+ "'," + "'" + MyTools.StrFiltr(ARRAY[i]) + "'," + "'"
					+ MyTools.StrFiltr(this.USERCODE) + "'," + "GETDATE(),"
					+ "'1')";
			sqlVec.add(sql);
		}
		
		if (db.executeInsertOrUpdateTransaction(sqlVec)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("保存失败");
		}
	}

	// 删除题目信息 问卷里的题目
	public void DelWjtm() throws SQLException, WrongSQLException {
		String sql = "delete from dbo.V_问卷调查_问卷题目关系表 " +
				"where 题目编号='" + MyTools.fixSql(this.getWW_TMBH()) + "' " +
				"and 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "删除成功";
		} else {
			this.MSG = "删除失败";
		}
	}

	// 查询所有选择题
	public Vector querySyxzt(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 题目编号,题目类型, case when 题目类型='01' then '单选题' when 题目类型='02' then '多选题' end as 题目类型名称,题目内容 " +
			"from dbo.V_问卷调查_题目信息_选择题   where 题目编号 not in(select 题目编号 from dbo.V_问卷调查_问卷题目关系表  " +
			"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "')";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	// 查询所有表单题
	public Vector querySybdt(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 题目编号,题目类型='03',题目类型名称='表单题',题目内容,题目要点,分值 " +
			"from V_问卷调查_题目信息_表单题  where 题目编号 not in(select 题目编号 from dbo.V_问卷调查_问卷题目关系表 " +
			"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "')";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	// 查询所有辅导员
	public Vector querySyfdy(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 问卷编号,工号,姓名,a.行政班代码,b.行政班名称  from V_问卷调查_辅导员信息表 as a,V_学校班级数据子类 as b " +
			"where a.行政班代码=b.行政班代码 and 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' ";
		// 判断查询条件
		if (!"".equalsIgnoreCase(this.getWW_XM())) {
			sql += " and 姓名  like '%" + MyTools.fixSql(this.getWW_XM()) + "%'";
		}
		if (!"".equalsIgnoreCase(this.getWW_XZBMC())) {
			sql += " and b.行政班名称  like '%" + MyTools.fixSql(this.getWW_XZBMC())	+ "%'";
		}
		sql += " ORDER BY 工号";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	// 用来绑定行政班名称combobox
	public Vector querySybjfdy(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 行政班代码,行政班名称 from dbo.V_学校班级数据子类 where 行政班代码  not in (select 行政班代码 from dbo.V_问卷调查_辅导员信息表)";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	// 查询所有行政班
	public Vector querySyxzb(int pageNum, int page) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select 行政班代码,行政班名称  from dbo.V_学校班级数据子类 " +
				"where 行政班代码='" + MyTools.fixSql(this.getWW_XZBDM()) + "'";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	/**
	 * 2d-新增记录 题目信息 问卷里的辅导员
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddBJFDY() throws WrongSQLException, SQLException {
		String sql = "insert into dbo.V_问卷调查_辅导员信息表 (问卷编号,工号,姓名,行政班代码,创建人,创建时间,状态) "
				+ " values('" + MyTools.StrFiltr(this.getWW_WJBH()) + "',"
				+ "'" + MyTools.StrFiltr(this.getWW_GH()) + "'," + "'"
				+ MyTools.StrFiltr(this.getWW_XM()) + "'," + "'"
				+ MyTools.StrFiltr(this.getWW_XZBDM()) + "'," + "'"
				+ MyTools.StrFiltr(this.USERCODE) + "'," + "GETDATE(),"
				+ "'1')";

		if (db.executeInsertOrUpdate(sql)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("保存失败");
		}
	}

	// 删除题目信息 问卷里的题目
	public void Delfdy() throws SQLException, WrongSQLException {
		String sql = "delete dbo.V_问卷调查_辅导员信息表  " +
				"where 行政班代码='" + MyTools.fixSql(this.getWW_XZBDM()) + "' " +
				"and 问卷编号='" + MyTools.StrFiltr(this.getWW_WJBH()) + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "删除成功";
		} else {
			this.MSG = "删除失败";
		}
	}

	/**
	 * 分页查询 无限制专业列表
	 * @date:2016-05-09
	 * @author:
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @throws SQLException
	 */
	public Vector queMajorList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "select b.专业代码,b.专业名称 from V_问卷调查_专业教师无限制信息表 a "
				+ "left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 "
				+ "where  a.问卷编号='" + MyTools.fixSql(this.getWW_WJBH())
				+ "' order by a.问卷编号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）

		return vec;
	}

	/**
	 * 读取专业下拉框
	 * @date:2016-05-09
	 * @author:
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException {
		Vector vec = null;
		String sql = "select '' as 专业名称,'请选择' as comboName,'' as comboValue "
				+ "union all "
				+ "select 专业名称,专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue from V_专业基本信息数据子类 "
				+ "where 专业代码 not in (select 专业代码 from V_问卷调查_专业教师无限制信息表 where 问卷编号='"
				+ MyTools.fixSql(this.getWW_WJBH()) + "') " + "order by 专业名称";
		vec = db.getConttexJONSArr(sql, 0, 0);

		return vec;
	}

	/**
	 * 添加无限制专业
	 * 
	 * @date:2016-05-09
	 * @author:
	 * @throws SQLException
	 */
	public void addRec() throws SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		String majorCodeArray[] = this.getWW_ZYDM().split(",");

		for (int i = 0; i < majorCodeArray.length; i++) {
			sql = "insert into V_问卷调查_专业教师无限制信息表 values(" + "'"
					+ MyTools.fixSql(this.getWW_WJBH()) + "'," + "'"
					+ MyTools.fixSql(majorCodeArray[i]) + "'," + "'"
					+ MyTools.fixSql(this.getUSERCODE()) + "',"
					+ "getDate(),'1')";
			sqlVec.add(sql);
		}

		if (db.executeInsertOrUpdateTransaction(sqlVec)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("保存失败");
		}
	}

	/**
	 * 删除无限制专业
	 * 
	 * @date:2016-05-09
	 * @author:
	 * @throws SQLException
	 */
	public void delRec() throws SQLException {
		String sql = "delete from V_问卷调查_专业教师无限制信息表 " + "where 问卷编号='"
				+ MyTools.fixSql(this.getWW_WJBH()) + "' and 专业代码 in ('"
				+ this.getWW_ZYDM().replaceAll(",", "','") + "')";

		if (db.executeInsertOrUpdate(sql)) {
			this.setMSG("删除成功");
		} else {
			this.setMSG("删除失败");
		}
	}

	/**
	 * 2d-新增记录 复制单选多选表单题
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void Adddxdxbdt() throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector();// 结果集
		String sql = ""; // 执行用SQL语句

		if ("".equalsIgnoreCase(this.getWW_WJBH())) {
			this.setWW_WJBH(db.getMaxID("V_问卷调查_问卷信息表", "问卷编号", "WJDC_", 6));// 获取主关键字
		}

		if (MyTools.fixSql(this.getWW_WJLX()).equalsIgnoreCase("03")) {
			// 问卷信息表
			sql = "insert into V_问卷调查_问卷信息表(问卷编号,问卷类型,标题,学年学期编码,开始时间,结束时间,创建人,创建时间,状态) " + 
				"select '" + MyTools.fixSql(this.getWW_WJBH()) + "',"+"问卷类型,标题,学年学期编码,开始时间,结束时间," +
					"'" + MyTools.fixSql(this.USERCODE)+"',GETDATE(),'1' " +
					"from V_问卷调查_问卷信息表 where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH1()) + "'";
			
			sqlVec.add(sql);
			
			sql = "insert into V_问卷调查_问卷题目关系表 (问卷编号,题目编号,创建人,创建时间,状态) " + 
				"select '" + MyTools.fixSql(this.getWW_WJBH()) + "',题目编号," +
				"'" + MyTools.fixSql(this.USERCODE) + "',getDate(),'1' " +
				"from dbo.V_问卷调查_问卷题目关系表  where 问卷编号='"	+ MyTools.fixSql(this.getWW_WJBH1()) + "'";;
			sqlVec.add(sql);

			// 辅导员信息表
			/* 2016/10/31 客户要求不设置辅导员信息
			sql4 = "select '" + MyTools.fixSql(this.getWW_WJBH()) + "',"
					+ "工号," + "姓名," + "行政班代码,'" + MyTools.fixSql(this.USERCODE)
					+ "'," + "GETDATE(),"
					+ "'1' from dbo.V_问卷调查_辅导员信息表  where 问卷编号='"
					+ MyTools.fixSql(this.getWW_WJBH1()) + "'";
			sql5 = "insert into V_问卷调查_辅导员信息表 (问卷编号,工号,姓名,行政班代码,创建人,创建时间,状态) "
					+ sql4;
			sqlVec3.add(sql5);
			*/
		} else {
			// 问卷信息表
			sql = "insert into V_问卷调查_问卷信息表(问卷编号,问卷类型,标题,学年学期编码,开始时间,结束时间,创建人,创建时间,状态) " +
				"select '"+ MyTools.fixSql(this.getWW_WJBH())+"',"+"问卷类型,标题,学年学期编码,开始时间,结束时间," +
					"'" + MyTools.fixSql(this.USERCODE) + "',getDate(),'1' " +
					"from V_问卷调查_问卷信息表 where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH1()) + "'";;
			sqlVec.add(sql);

			sql = "insert into V_问卷调查_问卷题目关系表 (问卷编号,题目编号,创建人,创建时间,状态) " +
				"select '" + MyTools.fixSql(this.getWW_WJBH()) + "',题目编号," +
				"'" + MyTools.fixSql(this.USERCODE) + "',getDate(),'1' from dbo.V_问卷调查_问卷题目关系表 " +
				"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH1()) + "'";
			sqlVec.add(sql);
		}
		
		if (db.executeInsertOrUpdateTransaction(sqlVec)) {
			this.setMSG("复制成功");
		} else {
			this.setMSG("操作失败");
		}
	}

	/**
	 * 2d-新增记录 复制专业教师无限制表
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void Addzyjswxzb() throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector(); // 结果集
		String sql = ""; // 执行用SQL语句

		if ("".equalsIgnoreCase(this.getWW_WJBH())) {
			this.setWW_WJBH(db.getMaxID("V_问卷调查_问卷信息表", "问卷编号", "WJDC_", 6));// 获取主关键字
		}

		// 问卷信息表
		sql = "insert into V_问卷调查_问卷信息表(问卷编号,问卷类型,标题,学年学期编码,开始时间,结束时间,创建人,创建时间,状态) " +
			"select '" + MyTools.fixSql(this.getWW_WJBH()) + "',"+"问卷类型,标题,学年学期编码,开始时间,结束时间," +
			"'" + MyTools.fixSql(this.USERCODE) + "',getDate(),'1' " +
			"from V_问卷调查_问卷信息表 where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH1()) + "'";
		sqlVec.add(sql);

		sql = "insert into dbo.V_问卷调查_专业教师无限制信息表 (问卷编号,专业代码,创建人,创建时间,状态) " +
			"select '" + MyTools.fixSql(this.getWW_WJBH()) + "'," + "专业代码," +
			"'" + MyTools.fixSql(this.USERCODE) + "'," + "getDate(),'1' " +
			"from dbo.V_问卷调查_专业教师无限制信息表  where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH1()) + "'";
		sqlVec.add(sql);
		
		if (db.executeInsertOrUpdateTransaction(sqlVec)) {
			this.setMSG("复制成功");
		} else {
			this.setMSG("操作失败");
		}
	}
	
	
	/**
	 * 读取学年学期名称下拉框
	 * @date:2016-07-19
	 * @author:
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector XNXQMCcombobox() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName,'0' as 排序 " +
				"union all " +
				"select 学年学期编码,学年学期名称,'1' as 排序 from V_规则管理_学年学期表 order by 排序,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取行政班名称下拉框
	 * @date:2016-07-19
	 * @author:
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector XZBMCcombobox() throws SQLException{
		Vector vec = null;
		int curGrade = 0;
		curGrade = MyTools.StringToInt(this.getWW_XNXQBM().substring(2, 4));
		String tempStr = "'" + (curGrade) + "1','" + (curGrade - 1) + "1'";
		String sql1="select '' as comboValue,'请选择' as comboName ,'0' as 排序"+
					" union all"+
					" select 行政班代码,行政班名称,'1'"+
					" from dbo.V_学校班级数据子类 as a"+
					" where 行政班代码 not in (select 行政班代码 from dbo.V_问卷调查_辅导员信息表 as b where 问卷编号='"+MyTools.fixSql(this.getWW_WJBH())+"')"+
					" and 年级代码 in ("+tempStr+")"+
					" union all select 行政班代码,行政班名称,'1' from dbo.V_学校班级数据子类 where 行政班代码='"+MyTools.fixSql(this.getWW_XZBDM())+"'"+
					" order by 排序,comboValue desc";
		vec = db.getConttexJONSArr(sql1, 0, 0);
		return vec;
	}
	
	/**
	 * 读取辅导员姓名下拉框
	 * @date:2016-07-19
	 * @author:
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector FDYXMcombobox() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName,0 as num "+
					"union all " +
					"select 工号,姓名,'1' from dbo.V_教职工基本数据子类 order by num,comboName";
					//" where 工号 not in (select 工号 from dbo.V_问卷调查_辅导员信息表 where 问卷编号='"+MyTools.fixSql(this.getWW_WJBH())+"')";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 删除单选多选题和表单题
	 * 
	 * @date:2016-05-09
	 * @author:
	 * @throws SQLException
	 */
	public void delDXDXT() throws SQLException {
		String sql = "delete from dbo.V_问卷调查_问卷题目关系表 " +
				"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
				"and 题目编号 in ('" + this.getWW_ZYDM().replaceAll(",", "','") + "')";
		if (db.executeInsertOrUpdate(sql)) {
			this.setMSG("删除成功");
		} else {
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 2e-修改记录 问卷里的辅导员信息
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void ModRecFDY() throws WrongSQLException, SQLException {
		String sql = "update dbo.V_问卷调查_辅导员信息表 set " +
			"工号='" + MyTools.fixSql(this.getWW_GH()) + "'," +
			"姓名='" + MyTools.fixSql(this.getWW_XM()) + "'," +
			"行政班代码='" + MyTools.fixSql(this.getWW_XZBDM()) + "' "+
			"where 问卷编号='" + MyTools.fixSql(this.getWW_WJBH()) + "' " +
			"and 行政班代码='" + MyTools.fixSql(this.getWW_XZBDM1()) + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "保存成功";
		} else {
			this.MSG = "操作失败";
		}
	}

	// 获取角色编号
	public void AuthCode() throws SQLException {
		int i;
		boolean F = false;
		String usercode = MyTools.getSessionUserCode(request);
		// System.out.println("*************usercode"+usercode);
		if (!"".equalsIgnoreCase(usercode)) {
			// 根据当前登录人找出角色编号
			String sql1 = "select AuthCode from dbo.V_USER_AUTH WHERE USERCODE='" + MyTools.fixSql(usercode) + "'";
			Vector ve = db.GetContextVector(sql1);
			if (ve != null && ve.size() > 0) {
				for (i = 0; i < ve.size(); i++) {
					String auth = MyTools.StrFiltr(ve.get(i));
					if ("01".equalsIgnoreCase(auth)) {// 如果是系统管理员
						this.setAUTHCODE("01");// 角色代码设为系统管理员
					}
				}
			}
		}
	}

	public String getAUTHCODE() {
		return AUTHCODE;
	}

	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
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

	public String getWW_BT() {
		return WW_BT;
	}

	public void setWW_BT(String wW_BT) {
		WW_BT = wW_BT;
	}

	public String getWW_XNXQBM() {
		return WW_XNXQBM;
	}

	public void setWW_XNXQBM(String wW_XNXQBM) {
		WW_XNXQBM = wW_XNXQBM;
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

	public String getWW_CJR() {
		return WW_CJR;
	}

	public void setWW_CJR(String wW_CJR) {
		WW_CJR = wW_CJR;
	}

	public String getWW_CJSJ() {
		return WW_CJSJ;
	}

	public void setWW_CJSJ(String wW_CJSJ) {
		WW_CJSJ = wW_CJSJ;
	}

	public String getWW_ZT() {
		return WW_ZT;
	}

	public void setWW_ZT(String wW_ZT) {
		WW_ZT = wW_ZT;
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

	public String getWW_TMBH() {
		return WW_TMBH;
	}

	public void setWW_TMBH(String wW_TMBH) {
		WW_TMBH = wW_TMBH;
	}

	public String getWW_GH() {
		return WW_GH;
	}

	public void setWW_GH(String wW_GH) {
		WW_GH = wW_GH;
	}

	public String getWW_XM() {
		return WW_XM;
	}

	public void setWW_XM(String wW_XM) {
		WW_XM = wW_XM;
	}

	public String getWW_XZBDM() {
		return WW_XZBDM;
	}

	public void setWW_XZBDM(String wW_XZBDM) {
		WW_XZBDM = wW_XZBDM;
	}

	public String getWW_XZBMC() {
		return WW_XZBMC;
	}

	public void setWW_XZBMC(String wW_XZBMC) {
		WW_XZBMC = wW_XZBMC;
	}

	public String getWW_ZYDM() {
		return WW_ZYDM;
	}

	public void setWW_ZYDM(String wW_ZYDM) {
		WW_ZYDM = wW_ZYDM;
	}

	public String getWW_WJBH1() {
		return WW_WJBH1;
	}

	public void setWW_WJBH1(String wW_WJBH1) {
		WW_WJBH1 = wW_WJBH1;
	}
	public String getWW_XZBDM1() {
		return WW_XZBDM1;
	}

	public void setWW_XZBDM1(String wW_XZBDM1) {
		WW_XZBDM1 = wW_XZBDM1;
	}
}