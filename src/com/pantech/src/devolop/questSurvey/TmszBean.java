package com.pantech.src.devolop.questSurvey;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class TmszBean {

	private String AUTHCODE;// 用户权限
	private String TT_TMBH;// 题目编号
	private String TT_TMNR;// 题目内容
	private String TT_TMYD;// 题目要点
	private String TT_FZ;// 分值
	private String TT_CJR;// 创建人
	private String TT_CJSJ;// 创建时间
	private String TT_TMLX;// 题目类型
	private String TT_TMZT;// 题目状态
	private String TT_XXBH;//选择题选项编号
	private String TT_XX;//选择题选项
	private String TT_XXNR;//选择题选项内容
	

	private String USERCODE;
	private String listsql;// 查询sql语句

	// 以下变量为此类的固定变量
	protected HttpServletRequest request;
	private DBSource db;
	private String MSG;

	/**
	 * 初始化变量
	 * 
	 * @date:2016-6-8
	 * @author:
	 */
	private void initialData() {
		// TODO Auto-generated method stub
		AUTHCODE = "";// 用户权限
		TT_TMBH = "";// 题目编号
		TT_TMNR = "";// 题目内容
		TT_TMYD = "";// 题目要点
		TT_FZ = "";// 分值
		TT_CJR = "";// 创建人
		TT_CJSJ = "";// 创建时间
		TT_TMZT = "";// 状态
		USERCODE = "";// 用户编号
		TT_XXBH="";//选择题选项编号
		TT_XX="";//选择题选项
		TT_XXNR="";//选择题选项
	}

	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public TmszBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}

	/**
	 * 分页查询 题目信息列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select distinct * from "+
		"(select 题目编号,题目内容,'03' as 题目类型,'表单题' as 类型名称,题目要点,分值 FROM V_问卷调查_题目信息_表单题"+
		" union all select [题目编号],[题目内容],题目类型,case when 题目类型='01' then '单选题' when 题目类型='02' then '多选题' end as 类型名称,'','' "+
		"FROM V_问卷调查_题目信息_选择题) as t where 1=1";

		//判断查询条件
		if(!"".equalsIgnoreCase(this.getTT_TMBH())){
			sql += " and 题目编号 like '%" + MyTools.fixSql(this.getTT_TMBH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getTT_TMNR())){
			sql += " and 题目内容 like '%" + MyTools.fixSql(this.getTT_TMNR()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getTT_TMLX())){
			sql += " and 题目类型='" + MyTools.fixSql(this.getTT_TMLX()) + "'";
		}
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	//查询表单题一条记录
	public Vector queryOne(int pageNum, int page) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集

		sql = "select 题目编号,题目内容,题目要点,分值,创建人,convert(varchar(10), 创建时间,21) as 创建时间,case "
				+ " when(状态='01') then '单选题' "
				+ " when(状态='02') then '多选题' "
				+ " else '表单题' "
				+ " end as 状态"
				+ " from dbo.V_问卷调查_题目信息_表单题  WHERE 1=1 ";
		// 设置查询条件
		// 仅使用指定条件

		sql = sql + " AND 题目编号 ='" + MyTools.fixSql(this.getTT_TMBH()) + "'";

		TraceLog.Trace("============sql1==========" + sql);
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	/**
	 * 读取一条题目信息
	 * 
	 * @param icode
	 *            主关键字
	 * @throws SQLException
	 */
	public void loadData() throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String icode;
		// 主关键字 可变
		icode = this.getTT_TMBH();
		// SQL语句 可变
		// System.out.println(icode);
		sql = "select 题目编号,题目内容 ,题目要点 ,分值 ,创建人,创建时间 ,case "
				+ " when(状态='01') then '单选题' " + " when(状态='02') then '多选题' "
				+ " else '表单题' " + " end as 题目类型"
				+ " from dbo.V_问卷调查_题目信息_表单题  WHERE 题目编号='"
				+ MyTools.fixSql(icode) + "'";
		vec = db.GetContextVector(sql);
		if (vec != null && vec.size() > 0) {
			// 初始化BEAN 可变
			this.TT_TMBH = MyTools.StrFiltr(vec.get(0));
			this.TT_TMNR = MyTools.StrFiltr(vec.get(1));
			this.TT_TMYD = MyTools.StrFiltr(vec.get(2));
			this.TT_FZ = MyTools.StrFiltr(vec.get(3));
			this.TT_CJR = MyTools.StrFiltr(vec.get(4));
			this.TT_CJSJ = MyTools.StrFiltr(vec.get(5));
			this.TT_TMZT = MyTools.StrFiltr(vec.get(6));

		}
	}

	/**
	 * 2c-保存记录(新增或修改) 表单题
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void saveRec() throws WrongSQLException, SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// SQL语句 可变
		// 根据实际情况修改 视图/表名 及主关键子名称
		sql = "select count(*) from dbo.V_问卷调查_题目信息_表单题  where 题目编号='" + MyTools.fixSql(this.getTT_TMBH()) + "'";
		
		if(!db.getResultFromDB(sql)){
			this.AddRec();
		}else{
			this.ModRec();
		}
	}

	/**
	 * 2d-新增记录 题目信息  表单题
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddRec() throws WrongSQLException, SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// 主关键字 可变
		if ("".equalsIgnoreCase(this.getTT_TMBH())) {
			this.setTT_TMBH(db.getMaxID("dbo.V_问卷调查_题目信息_表单题", "题目编号",
					"WJBDT_", 8));// 获取主关键字
		}
		// SQL语句 可变
		sql = "INSERT INTO dbo.V_问卷调查_题目信息_表单题(题目编号,题目内容,题目要点,分值,创建人,创建时间,状态)"
				+ "VALUES(" + "'" + MyTools.fixSql(this.getTT_TMBH()) + "',"
				+ "'" + MyTools.fixSql(this.getTT_TMNR()) + "'," + "'"
				+ MyTools.fixSql(this.getTT_TMYD()) + "'," + "'"
				+ MyTools.fixSql(this.getTT_FZ()) + "'," + "'"
				+ MyTools.fixSql(this.USERCODE) + "'," + "GETDATE(),"
				+"'1')";

		// 判断最近提醒时间是否为nulls
		if (db.executeInsertOrUpdate(sql)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("操作失败");
		}
		
		db.DBLog(sql);
	}

	/**
	 * 2e-修改记录 题目信息  表单题
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void ModRec() throws WrongSQLException, SQLException {
		System.out.println("Come on ModRec+进入修改");
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// 判断是否可修改
		TraceLog.Trace("sql===" + sql);
		// SQL语句 可变

		sql = "update dbo.V_问卷调查_题目信息_表单题  set " + "题目内容='"
				+ MyTools.fixSql(this.getTT_TMNR()) + "'," + "题目要点='"
				+ MyTools.fixSql(this.getTT_TMYD()) + "'," + "分值='"
				+ MyTools.fixSql(this.getTT_FZ()) + "'"
				+ " WHERE 题目编号='" + this.getTT_TMBH() + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "保存成功";
		} else {
			this.MSG = "操作失败";
		}
		
		db.DBLog(sql);
	}
	
	
	//删除题目信息 表单题 
	public void DelRec() throws SQLException, WrongSQLException {
		DBSource db = new DBSource(request);
		String sql = "delete from dbo.V_问卷调查_题目信息_表单题  where 题目编号='"
				+ MyTools.fixSql(this.getTT_TMBH()) + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "删除成功";
		} else {
			this.MSG = "删除失败";
		}
		db.DBLog(sql);
	}
	
	//查询选择题的选项
	public Vector queryXZTXX(int pageNum, int page) throws SQLException, WrongSQLException {
		System.out.println("题目编号================================="+this.getTT_TMBH());
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 选项编号,题目编号,选项,选项内容,创建人,创建时间,状态  from V_问卷调查_选项信息表 where 1=1 and  题目编号='"+MyTools.fixSql(this.getTT_TMBH())+"'";
		sql=sql+" ORDER BY 选项 ";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	
	
	/**
	 * 2c-保存记录(新增或修改) 选择题
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void saveXzt() throws WrongSQLException, SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// SQL语句 可变
		// 根据实际情况修改 视图/表名 及主关键子名称
		sql = "select count(*) from dbo.V_问卷调查_题目信息_选择题  where 题目编号='" + MyTools.fixSql(this.getTT_TMBH()) + "'";
		
		if(!db.getResultFromDB(sql)){
			this.AddXzt();
		}else{
			this.ModXzt();
		}
	}

	/**
	 * 2d-新增记录 题目信息 选择题
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddXzt() throws WrongSQLException, SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// 主关键字 可变
		if ("".equalsIgnoreCase(this.getTT_TMBH())) {
			this.setTT_TMBH(db.getMaxID("dbo.V_问卷调查_题目信息_选择题", "题目编号",
					"WJXZT_", 8));// 获取主关键字
		}
		// SQL语句 可变
		sql = "INSERT INTO dbo.V_问卷调查_题目信息_选择题(题目编号,题目类型,题目内容,创建人,创建时间,状态)"
				+ "VALUES(" + "'" + MyTools.fixSql(this.getTT_TMBH()) + "',"
				+ "'" + MyTools.fixSql(this.getTT_TMLX()) + "'," + "'"
				+ MyTools.fixSql(this.getTT_TMNR()) + "'," + "'"
				+ MyTools.fixSql(this.USERCODE) + "'," + "GETDATE()," + "'"+"1')";

		// 判断最近提醒时间是否为nulls
		if (db.executeInsertOrUpdate(sql)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("操作失败");
		}
		
		db.DBLog(sql);
	}

	/**
	 * 2e-修改记录 题目信息 选择题
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void ModXzt() throws WrongSQLException, SQLException {
		System.out.println("Come on ModXzt+进入修改");
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// 判断是否可修改
		TraceLog.Trace("sql===" + sql);
		// SQL语句 可变

		sql = "update dbo.V_问卷调查_题目信息_选择题  set " + "题目内容='"
				+ MyTools.fixSql(this.getTT_TMNR()) + "',";
				
		sql += "创建时间='" + MyTools.fixSql(this.getTT_CJSJ()) + "',";
		sql += "状态='" + MyTools.fixSql(this.getTT_TMZT()) + "'"
				+ " WHERE 题目编号='" + this.getTT_TMBH() + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "保存成功";
		} else {
			this.MSG = "操作失败";
		}
		
		db.DBLog(sql);
	}
	
	
	//删除题目信息 选择题 ======================================================================================================
	public void DelXzt() throws SQLException, WrongSQLException {
		DBSource db = new DBSource(request);
		String sql = "delete from dbo.V_问卷调查_题目信息_选择题   where 题目编号='"
				+ MyTools.fixSql(this.getTT_TMBH()) + "'";
		String sql1="delete from V_问卷调查_选项信息表 where 题目编号='"
				+ MyTools.fixSql(this.getTT_TMBH()) + "'";
		if (db.executeInsertOrUpdate(sql)&&db.executeInsertOrUpdate(sql1)) {
			this.MSG = "删除成功";
		} else {
			this.MSG = "删除失败";
		}
		db.DBLog(sql);
		db.DBLog(sql1);
	}
	
	
	
	/**
	 * 2c-保存记录(新增或修改) 选择题选项 
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void saveXztxx() throws WrongSQLException, SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// SQL语句 可变
		// 根据实际情况修改 视图/表名 及主关键子名称
		sql = "select count(*) from dbo.V_问卷调查_选项信息表  where 1=1 and 选项编号='" + MyTools.fixSql(this.getTT_XXBH()) + "'";
		
		if(!db.getResultFromDB(sql)){
			this.AddXztxx();
		}else{
			this.ModXztxx();
		}
	}

	/**
	 * 2d-新增记录 题目信息 选择题选项 
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddXztxx() throws WrongSQLException, SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// 主关键字 可变
		if ("".equalsIgnoreCase(this.getTT_XXBH())) {
			this.setTT_XXBH(db.getMaxID("dbo.V_问卷调查_选项信息表", "选项编号",
					"TMXX_", 8));// 获取主关键字
		}
		// SQL语句 可变
		sql = "INSERT INTO dbo.V_问卷调查_选项信息表(选项编号,题目编号,选项,选项内容,创建人,创建时间,状态)"
				+ "VALUES(" + "'" + MyTools.fixSql(this.getTT_XXBH()) + "',"
				+ "'" + MyTools.fixSql(this.getTT_TMBH()) + "'," + "'"
				 + MyTools.fixSql(this.getTT_XX()) + "'," + "'"
				 + MyTools.fixSql(this.getTT_XXNR()) + "'," + "'"
				+ MyTools.fixSql(this.USERCODE) + "'," + "GETDATE(),"
				 +"'1')";

		// 判断最近提醒时间是否为nulls
		if (db.executeInsertOrUpdate(sql)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("操作失败");
		}
		
		db.DBLog(sql);
	}

	/**
	 * 2e-修改记录 题目信息 选择题选项 
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void ModXztxx() throws WrongSQLException, SQLException {
		System.out.println("Come on ModXztxx+进入修改");
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 执行用SQL语句
		// 判断是否可修改
		TraceLog.Trace("sql===" + sql);
		// SQL语句 可变
		
		sql="update  dbo.V_问卷调查_选项信息表 set 选项='"+MyTools.fixSql(this.getTT_XX()) + "',"+"选项内容='"+MyTools.fixSql(this.getTT_XXNR()) + "'"+
				" WHERE 选项编号='" + this.getTT_XXBH() + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "保存成功";
		} else {
			this.MSG = "操作失败";
		}
		
		db.DBLog(sql);
	}
	
	
	//删除题目信息 选择题选项 
	public void DelXztxx() throws SQLException, WrongSQLException {
		
		DBSource db = new DBSource(request);
		String sql = "delete from dbo.V_问卷调查_选项信息表   where 选项编号='"
				+ MyTools.fixSql(this.getTT_XXBH()) + "'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "删除成功";
		} else {
			this.MSG = "删除失败";
		}
		db.DBLog(sql);
	}
	
	
	
	
	
	

	// 获取角色编号
	public void AuthCode() throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		int i;
		boolean F = false;
		String usercode = MyTools.getSessionUserCode(request);
		// System.out.println("*************usercode"+usercode);
		if (!"".equalsIgnoreCase(usercode)) {
			// 根据当前登录人找出角色编号
			String sql1 = "select AuthCode from dbo.V_USER_AUTH WHERE USERCODE='"
					+ MyTools.fixSql(usercode) + "'";
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

	public String getTT_TMBH() {
		return TT_TMBH;
	}

	public void setTT_TMBH(String tT_TMBH) {
		TT_TMBH = tT_TMBH;
	}

	public String getTT_TMNR() {
		return TT_TMNR;
	}

	public void setTT_TMNR(String tT_TMNR) {
		TT_TMNR = tT_TMNR;
	}

	public String getTT_TMYD() {
		return TT_TMYD;
	}

	public void setTT_TMYD(String tT_TMYD) {
		TT_TMYD = tT_TMYD;
	}

	public String getTT_FZ() {
		return TT_FZ;
	}

	public void setTT_FZ(String tT_FZ) {
		TT_FZ = tT_FZ;
	}

	public String getTT_CJR() {
		return TT_CJR;
	}

	public void setTT_CJR(String tT_CJR) {
		TT_CJR = tT_CJR;
	}

	public String getTT_CJSJ() {
		return TT_CJSJ;
	}

	public void setTT_CJSJ(String tT_CJSJ) {
		TT_CJSJ = tT_CJSJ;
	}

	public String getTT_TMZT() {
		return TT_TMZT;
	}

	public void setTT_TMZT(String tT_TMZT) {
		TT_TMZT = tT_TMZT;
	}

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

	public String getListsql() {
		return listsql;
	}

	public void setListsql(String listsql) {
		this.listsql = listsql;
	}

	public String getTT_TMLX() {
		return TT_TMLX;
	}

	public void setTT_TMLX(String tT_TMLX) {
		TT_TMLX = tT_TMLX;
	}
	public String getTT_XXBH() {
		return TT_XXBH;
	}

	public void setTT_XXBH(String tT_XXBH) {
		TT_XXBH = tT_XXBH;
	}

	public String getTT_XX() {
		return TT_XX;
	}

	public void setTT_XX(String tT_XX) {
		TT_XX = tT_XX;
	}

	public String getTT_XXNR() {
		return TT_XXNR;
	}

	public void setTT_XXNR(String tT_XXNR) {
		TT_XXNR = tT_XXNR;
	}

}
