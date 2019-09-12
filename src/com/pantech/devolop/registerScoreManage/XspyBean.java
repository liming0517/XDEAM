package com.pantech.devolop.registerScoreManage;

import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class XspyBean {
	private String AUTHCODE;// 用户权限
	private String PY_XM; //姓名
	private String PY_NJ; //年级
	private String PY_PY; //评语
	private String PY_CJR; //创建人
	private String PY_CJSJ; //创建时间
	private String PY_ZT; //状态
	private String PY_XZBDM; //行政班代码
	private String PY_XBDM; //系部代码
	private String PY_ZYDM; //专业代码
	private String PY_XJH;//学籍号
	private String PY_DD;//等第
	private String USERCODE;
	private String listsql;// 查询sql语句

	// 以下变量为此类的固定变量
	protected HttpServletRequest request;
	private DBSource db;
	private String MSG;

	/**
	 * 初始化变量
	 * 
	 * @date:2016-7-13
	 * @author:翟旭超
	 */
	private void initialData() {
		AUTHCODE = "";// 用户权限
		PY_XM = ""; //姓名
		PY_NJ = ""; //年级
		PY_PY = ""; //评语
		PY_CJR = ""; //创建人
		PY_CJSJ = ""; //创建时间
		PY_ZT = "";//状态
		PY_XZBDM = ""; //行政班代码
		PY_XBDM = ""; //系部代码
		PY_ZYDM = ""; //专业代码
		PY_XJH = "";//学籍号
		PY_DD="";//等第
		USERCODE = "";// 用户编号
	}

	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public XspyBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}

	/**
	 * 分页查询 学生信息列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "select a.行政班代码,a.系部代码,e.系部名称,a.行政班名称,a.班主任工号,a.年级代码,d.专业名称,d.专业代码 " +
			"from V_学校班级数据子类 a " +
			"left join V_专业基本信息数据子类 d on a.专业代码=d.专业代码  " +
			"left join V_基础信息_系部信息表 e on e.系部代码=a.系部代码 " +
			"where a.状态='1' and d.状态='1' and e.状态='Y' ";
				
		//权限判断
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
			sql += " and (";
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += "a.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			//系部教务人员
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				if(this.getAUTHCODE().indexOf(bzr) > -1){
					sql += " or ";
				}
				sql += "e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ") ";
		}
		// 判断查询条件
		if(!"".equalsIgnoreCase(this.getPY_XZBDM())){
			sql += " and a.行政班代码='" + MyTools.fixSql(this.getPY_XZBDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getPY_XBDM())){
			sql += " and a.系部代码='" + MyTools.fixSql(this.getPY_XBDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getPY_ZYDM())){
			sql += " and a.专业代码='" + MyTools.fixSql(this.getPY_ZYDM()) + "'";
		}
		sql += " order by a.行政班代码 ";

		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 读取系下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXMCCombo() throws SQLException{
		Vector vec = null;
		String sql="select '请选择' as comboName,'' as comboValue " +
				"union all select 系部名称  as comboName,系部代码 as comboValue " +
				"from V_基础信息_系部信息表 where 系部代码<>'C00' and 状态='Y' " +
				"order by comboValue ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	
	/**
	 * 读取专业下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadZYMCCombo() throws SQLException{
		Vector vec = null;
		String sql="select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select  a.专业名称 as comName ,a.专业代码 as comValue " +
				"from V_专业基本信息数据子类 a " +
				"where a.状态='1' " +
				"order by comboValue ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取行政班下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXZBCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName,'' as yearNum ,'0' as 排序 " +
				"union all " +
				"select 行政班代码 as comboValue , 行政班名称 as comboName , 年级代码 as yearNum,'1' " +
				"from V_学校班级数据子类  " +
				"where 状态='1' " +
				"order by 排序,yearNum desc,comboValue " ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 检查学生评语信息表里是否有这个学生
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void CheckStuRec() throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector(); // 结果集
		String sql = ""; // 执行用SQL语句

		sql = "select count(*) from V_成绩管理_学生评语信息表 where 学号='" + MyTools.fixSql(this.getPY_XJH()) + "'";
		if(!db.getResultFromDB(sql)){
			for(int i=1 ; i<5; i++){
				sql = "insert into V_成绩管理_学生评语信息表 (学号,年级,评语,等第,创建人,创建时间,状态) values (" +
					"'" + MyTools.fixSql(this.getPY_XJH()) + "'," +
					"'" + i +"'," +
					"''," +
					"''," +
					"'" + MyTools.fixSql(this.USERCODE) + "'," +
					"getDate(),'1')";
				sqlVec.add(sql);
			}
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("操作失败");
			}
		}
	}
	
	/**
	 * 更新学生评语
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void updateStuComment() throws WrongSQLException, SQLException {
		String sql = "update dbo.V_成绩管理_学生评语信息表 set " +
				"评语='" + MyTools.fixSql(this.getPY_PY()) + "'," +
				"等第='" + MyTools.fixSql(this.getPY_DD()) + "' " +
				"where 学号='" +  MyTools.fixSql(this.getPY_XJH()) + "' " +
				"and 年级='" + MyTools.fixSql(this.getPY_NJ()) + "'";
		
		if (db.executeInsertOrUpdate(sql)) {
			this.setMSG("保存成功");
		} else {
			this.setMSG("操作失败");
		}
	}
	
	/**
	 * 查询学生评语
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector selectStuComment() throws WrongSQLException, SQLException {
		Vector vec = new Vector();
		String sql = "select 学号,评语,等第,年级 from V_成绩管理_学生评语信息表 " +
				"where 学号='" + MyTools.fixSql(this.getPY_XJH()) + "'";
		vec = db.getConttexJONSArr(sql,0,0);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 加载班级学生的树
	 * @date:2017-09-04
	 * @author zhaixuchao
	 * @return vec
	 * @throws SQLException
	 */
	public Vector queClassStuTree(String parentCode)throws SQLException,WrongSQLException{
		Vector vec = null;
		String sql = "select 学号 as id,姓名 as text,state='open' from V_学生基本数据子类 " +
				"where 学生状态 in ('01','05') and 行政班代码='" + MyTools.fixSql(this.getPY_XZBDM()) + "'" ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	public String getAUTHCODE() {
		return AUTHCODE;
	}

	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
	}

	public String getPY_XM() {
		return PY_XM;
	}

	public void setPY_XM(String pY_XM) {
		PY_XM = pY_XM;
	}

	public String getPY_NJ() {
		return PY_NJ;
	}

	public void setPY_NJ(String pY_NJ) {
		PY_NJ = pY_NJ;
	}

	public String getPY_PY() {
		return PY_PY;
	}

	public void setPY_PY(String pY_PY) {
		PY_PY = pY_PY;
	}

	public String getPY_CJR() {
		return PY_CJR;
	}

	public void setPY_CJR(String pY_CJR) {
		PY_CJR = pY_CJR;
	}

	public String getPY_CJSJ() {
		return PY_CJSJ;
	}

	public void setPY_CJSJ(String pY_CJSJ) {
		PY_CJSJ = pY_CJSJ;
	}

	public String getPY_ZT() {
		return PY_ZT;
	}

	public void setPY_ZT(String pY_ZT) {
		PY_ZT = pY_ZT;
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

	public String getPY_XZBDM() {
		return PY_XZBDM;
	}

	public void setPY_XZBDM(String pY_XZBDM) {
		PY_XZBDM = pY_XZBDM;
	}

	public String getPY_XBDM() {
		return PY_XBDM;
	}

	public void setPY_XBDM(String pY_XBDM) {
		PY_XBDM = pY_XBDM;
	}

	public String getPY_ZYDM() {
		return PY_ZYDM;
	}

	public void setPY_ZYDM(String pY_ZYDM) {
		PY_ZYDM = pY_ZYDM;
	}

	public String getPY_XJH() {
		return PY_XJH;
	}

	public void setPY_XJH(String pY_XJH) {
		PY_XJH = pY_XJH;
	}

	public String getPY_DD() {
		return PY_DD;
	}

	public void setPY_DD(String pY_DD) {
		PY_DD = pY_DD;
	}
}