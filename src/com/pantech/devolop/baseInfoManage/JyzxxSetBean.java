package com.pantech.devolop.baseInfoManage;
/*
@date 2016-11-18
@author maowei
模块：M1.10教研组信息设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class JyzxxSetBean {
	private String USERCODE;//用户编号
	private String JN_JYZDM; //教研组编号
	private String JN_JYZMC; //教研组名称
	private String JN_JYZXK; //教研组学科
	private String JN_JYXZZBH; //教研组组长
	private String JN_CJR; //创建人
	private String JN_CJSJ; //创建时间
	private String JN_ZT; //状态
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public JyzxxSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request); // 数据库对象
		this.initialData(); // 初始化参数
	}
	
	/**
	 * 初始化变量
	 */
	public void initialData() {
		JN_JYZDM = ""; //年级代码
		JN_JYZMC = ""; //年级名称
		JN_JYZXK = "";//教研组学科
		JN_JYXZZBH = "";//教研组组长
		JN_CJR = ""; //创建人
		JN_CJSJ = ""; //创建时间
		JN_ZT = ""; //状态
	}
	
	// 分页查询 教研组信息
	
	public Vector queJYZList(int pageNum,int rows) throws SQLException{
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集	
		sql = "select distinct a.教研组编号,a.教研组名称,a.学科代码,b.课程名称 as 学科名称,replace(a.教研组组长编号,'@','') as 教研组组长编号  " +
			"from V_基础信息_教研组信息表 a " +
			"left join dbo.V_课程数据子类 b on a.学科代码=b.课程号 " +
			"left join dbo.V_教职工基本数据子类 c on a.教研组组长编号 like '%@'+c.工号+'@%' " +
			"where 1=1";
		if(!"".equalsIgnoreCase(this.getJN_JYZMC())){
			sql += " and a.教研组名称 like '%"+ MyTools.fixSql(this.getJN_JYZMC()) +"%'";
		}
		if(!"".equalsIgnoreCase(this.getJN_JYZXK())){
			sql += " and b.课程名称 like '%"+ MyTools.fixSql(this.getJN_JYZXK()) +"%'";
		}
		if(!"".equalsIgnoreCase(this.getJN_JYXZZBH())){
			sql += " and c.姓名 like '%"+ MyTools.fixSql(this.getJN_JYXZZBH()) +"%'";
		}
		sql += " order by a.教研组编号";
		vec = db.getConttexJONSArr(sql, pageNum, rows);
		return vec;
	}
	
	/**
	 * 读取教师下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJYZzzCombo() throws SQLException{
		Vector vec = null;
		String jyzzz = MyTools.getProp(request, "Base.jyzzz").replaceAll("@", "");
		String sql = "select '' as comboValue,'请选择' as comboName "+ 
			"union all " +
			"select a.工号 as comboValue,a.姓名 as comboName "+ 
			"from V_教职工基本数据子类 a " +
			"left join sysUserAuth b on b.UserCode=a.工号 " +
			"where a.是否有效='1' and b.AuthCode='" + jyzzz + "' order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教研组长下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXKzzCombo() throws SQLException{
		Vector vec = null;
		String sql ="select '' as comboValue,'请选择' as comboName "+ 
			"union all " +
			"select a.课程号 as comboboValue ,a.课程名称 as comboboxName from dbo.V_课程数据子类 a";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 新增记录 教研组信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void AddRec() throws WrongSQLException, SQLException {
		String sql = "";
		
		 sql = "select count(*) from dbo.V_基础信息_教研组信息表 a where a.学科代码='"+this.getJN_JYZXK()+"';";
		 if(!db.getResultFromDB(sql)){
			 this.setJN_JYZDM(db.getMaxID("V_基础信息_教研组信息表", "教研组编号", "JYZ_", 6));// 获取主关键字
			 
			 sql = "insert into V_基础信息_教研组信息表 values (" +
		 		"'" + MyTools.fixSql(this.getJN_JYZDM())+"'," +
				"'" + MyTools.fixSql(this.getJN_JYZMC()) + "教研组'," +
				"'" + MyTools.fixSql(this.getJN_JYZXK()) + "'," +
				"'@" + MyTools.fixSql(this.getJN_JYXZZBH().replaceAll(",", "@,@")) + "@'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "',getdate(),'1')";
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("保存成功");
				
				//this.updateAuth();
			}else{
				this.setMSG("保存失败");
			}
		 }else{
			 this.setMSG(this.getJN_JYZMC()+"教研组已存在");
		 }
	}
	
	/**
	 * 修改记录  教研组信息
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void ModRec() throws WrongSQLException, SQLException {
		String sql = "update V_基础信息_教研组信息表 set " +
				"学科代码='"+MyTools.fixSql(this.getJN_JYZXK()) + "', " +
				"教研组组长编号='@" + MyTools.fixSql(this.getJN_JYXZZBH().replaceAll(",", "@,@")) + "@'," +
				"教研组名称='" + MyTools.fixSql(this.getJN_JYZMC()) + "教研组'" +
				"where 教研组编号='" + MyTools.fixSql(this.getJN_JYZDM()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
			
			//this.updateAuth();
		}else{
			this.setMSG("保存失败");
		}
	}
	
	//删除单条年级信息
	public void DelRec() throws SQLException,WrongSQLException{
		String sql = "delete from V_基础信息_教研组信息表 where 教研组编号='"+this.getJN_JYZDM()+"'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
			
			//this.updateAuth();
		}else{
			this.setMSG("删除失败");
		}
	} 
	
	/**
	 * 更新教研组组长权限
	 * @throws SQLException
	 */
	public boolean updateAuth() throws SQLException {
		boolean flag = false;
		String sql = "";
		Vector sqlVec = new Vector();
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");
		
		sql = "delete from sysUserAuth where AuthCode='" + MyTools.fixSql(jyzzz) + "'";
		sqlVec.add(sql);
			sql = "insert into sysUserAuth (UserCode,AuthCode,state) " +
			"select distinct a.UserCode,'" + MyTools.fixSql(jyzzz) + "','Y' from sysUserInfo a " +
			"inner join V_基础信息_教研组信息表 b on b.教研组组长编号 like '%@'+a.UserCode+'@%'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			flag = true;
		}else{
			flag = false;
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

	public String getJN_JYZDM() {
		return JN_JYZDM;
	}

	public void setJN_JYZDM(String jN_JYZDM) {
		JN_JYZDM = jN_JYZDM;
	}

	public String getJN_JYZMC() {
		return JN_JYZMC;
	}

	public void setJN_JYZMC(String jN_JYZMC) {
		JN_JYZMC = jN_JYZMC;
	}

	public String getJN_JYZXK() {
		return JN_JYZXK;
	}

	public void setJN_JYZXK(String jN_JYZXK) {
		JN_JYZXK = jN_JYZXK;
	}

	public String getJN_JYXZZBH() {
		return JN_JYXZZBH;
	}

	public void setJN_JYXZZBH(String jN_JYXZZBH) {
		JN_JYXZZBH = jN_JYXZZBH;
	}

	public String getJN_CJR() {
		return JN_CJR;
	}

	public void setJN_CJR(String jN_CJR) {
		JN_CJR = jN_CJR;
	}

	public String getJN_CJSJ() {
		return JN_CJSJ;
	}

	public void setJN_CJSJ(String jN_CJSJ) {
		JN_CJSJ = jN_CJSJ;
	}

	public String getJN_ZT() {
		return JN_ZT;
	}

	public void setJN_ZT(String jN_ZT) {
		JN_ZT = jN_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	
}