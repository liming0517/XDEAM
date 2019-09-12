package com.pantech.src.devolop.questSurvey;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class WjjgcxBean {

	private String AUTHCODE;// 用户权限
	private String WJ_WJBH;// 问卷编号
	private String WJ_WJLX;// 问卷类型
	private String WJ_BT;// 标题
	private String WJ_XNXQBM;// 学年学期编码
	private String WJ_KSSJ;// 开始时间
	private String WJ_JSSJ;// 结束时间
	private String WJ_CJR;// 创建人
	private String WJ_CJSJ;// 创建时间
	private String WJ_ZT;// 状态

	private String WJ_NJ;// 年级
	private String WJ_XZBMC;// 行政班名称
	private String WJ_WCQK;// 完成情况

	private String WJ_XNXQBH;// 学年学期编号
	
	private String WJ_XZBDM;// 行政班代码

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
		// TODO Auto-generated method stub
		AUTHCODE = "";// 用户权限
		WJ_WJBH = "";// 问卷编号
		WJ_WJLX = "";// 问卷类型
		WJ_BT = "";// 标题
		WJ_XNXQBM = "";// 学年学期编码
		WJ_KSSJ = "";// 开始时间
		WJ_JSSJ = "";// 结束时间
		WJ_CJR = "";// 创建人
		USERCODE = "";// 用户编号
		WJ_CJSJ = "";// 创建时间
		WJ_ZT = "";// 状态

		WJ_NJ = "";// 年级
		WJ_XZBMC = "";// 行政班名称
		WJ_WCQK = "";// 完成情况

		WJ_XNXQBH = "";// 学年学期编号
		
		WJ_XZBDM="";// 行政班代码

	}


	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public WjjgcxBean(HttpServletRequest request) {
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
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select distinct * from(select 问卷编号,问卷类型,"
				+ " case when 问卷类型='01' then '单选题/多选题' when 问卷类型='02' then '表单题1' when 问卷类型='03' then '表单题2' end as 问卷类型名称,"
				+ "标题,a.学年学期编码,学年学期名称,CONVERT(nvarchar(10),开始时间,21) as 开始时间,CONVERT(nvarchar(10),结束时间,21) as 结束时间,"
				+ "a.创建人,CONVERT(nvarchar(10),a.创建时间,21) as 创建时间,a.状态 "
				+ "from V_问卷调查_问卷信息表  as a left join dbo.V_规则管理_学年学期表 as b on a.学年学期编码=b.学年学期编码) as t where 1=1 and 状态='1' ";
		// 判断查询条件
		if (!"".equalsIgnoreCase(this.getWJ_BT())) {
			sql += " and 标题 like '%" + MyTools.fixSql(this.getWJ_BT()) + "%'";
		}
		if (!"".equalsIgnoreCase(this.getWJ_XNXQBM())||!"null".equalsIgnoreCase(this.getWJ_XNXQBM())) {
			sql += " and 学年学期编码 like '%" + MyTools.fixSql(this.getWJ_XNXQBM())+ "%'";
		}
		if (!"".equalsIgnoreCase(this.getWJ_KSSJ())) {
			sql += " and 开始时间 >= '" + MyTools.fixSql(this.getWJ_KSSJ()) + "'";
		}
		if (!"".equalsIgnoreCase(this.getWJ_JSSJ())) {
			sql += " and 结束时间 <= '" + MyTools.fixSql(this.getWJ_JSSJ()) + "'";
		}

		sql = sql + " ORDER BY 创建时间 desc,问卷编号 desc";

		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}

	/**
	 * 分页查询 查看结果信息列表 分页查询
	 * @return
	 * @throws SQLException
	 */
	public Vector queryRecCKJG(int pageNum, int page) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		int curGrade = 0;
		curGrade = MyTools.StringToInt(this.getWJ_XNXQBM().substring(2, 4));
		String tempStr = "'"+(curGrade)+"1','"+(curGrade-1)+"1'";

		sql = "select b.行政班代码,b.行政班名称,b.总人数,b.已完成,b.未完成,b.年级代码,b.完成情况 from " +
			"(select t.行政班代码,t.行政班名称,t.总人数,t.已完成,t.未完成,t.年级代码,case when t.未完成='0' then '1' else '2' end as 完成情况 from (" +
			"select b.行政班代码,b.行政班名称," +
			"(select count(*) from V_学生基本数据子类 a where a.行政班代码 = b.行政班代码 and 学生状态 in ('01','05','07')) as 总人数,";
		//判断问卷类型
		if("02".equalsIgnoreCase(this.getWJ_WJLX())){
			sql += "(select count(distinct t1.创建人) from V_问卷调查_教师满意度评分表 t1 " +
				"left join V_学生基本数据子类 t2 on t1.创建人=t2.学号 " +
				"where t2.行政班代码=b.行政班代码 and t1.问卷编号='" + MyTools.fixSql(this.getWJ_WJBH()) + "' and t2.学生状态 in ('01','05','07'))";
		}else{
			sql += "(select count(distinct t1.创建人) from V_问卷调查_结果信息表 t1 " +
				"left join V_学生基本数据子类 t2 on t1.创建人=t2.学号 " +
				"where t2.行政班代码=b.行政班代码 and t1.问卷编号='" + MyTools.fixSql(this.getWJ_WJBH()) + "' and t2.学生状态 in ('01','05','07'))";
		}
		sql += " as 已完成,";
		//判断问卷类型
		if("02".equalsIgnoreCase(this.getWJ_WJLX())){
			sql += "(select count(学号) from V_学生基本数据子类 " +
				"where 学号 not in (select distinct 创建人 from V_问卷调查_教师满意度评分表 where 问卷编号='" + MyTools.fixSql(this.getWJ_WJBH()) + "') " +
				"and 行政班代码=b.行政班代码 and 学生状态 in ('01','05','07'))";
		}else{
			sql += "(select count(学号) from V_学生基本数据子类 " +
				"where 学号 not in (select distinct 创建人 from V_问卷调查_结果信息表 where 问卷编号='" + MyTools.fixSql(this.getWJ_WJBH()) + "') " +
				"and 行政班代码=b.行政班代码 and 学生状态 in ('01','05','07'))";
		}
		sql += " as 未完成,b.年级代码 from V_学校班级数据子类 b where b.年级代码 in("+ tempStr+ ")) as t) as b where 1=1";
		
		// 判断查询条件
		if (!"".equalsIgnoreCase(this.getWJ_NJ())) {
			sql += " and 年级代码 ='" + MyTools.fixSql(this.getWJ_NJ()) + "'";
		}
		if (!"".equalsIgnoreCase(this.getWJ_XZBMC())) {
			sql += " and 行政班名称 like '%" + MyTools.fixSql(this.getWJ_XZBMC())+ "%'";
		}
		if (!"".equalsIgnoreCase(this.getWJ_WCQK())) {
			sql += " and 完成情况= '" + MyTools.fixSql(this.getWJ_WCQK()) + "'";
		}
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	
	/**
	 * 读取年级下拉框
	 * @date:2016-07-19
	 * @author:
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNjCombo() throws SQLException{
		Vector vec = null;
		int curYear = 0;
		curYear = MyTools.StringToInt(this.getWJ_XNXQBM().substring(2, 4));
		String tempStr = "'"+(curYear)+"1','"+(curYear-1)+"1'";
		String sql1="select '' as comboValue,'请选择' as comboName  from dbo.V_学校年级数据子类 union select 年级代码 as comboValue,年级名称 as  comboName from dbo.V_学校年级数据子类 where 年级代码 in("+tempStr+")";
		vec = db.getConttexJONSArr(sql1, 0, 0);
		
		return vec;
	}
	
	// 查询一个班级中详细信息
	public Vector queryOne(int pageNum, int page) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select a.行政班代码,b.行政班名称,a.学号,a.姓名," +
			"case when ";
		if("02".equalsIgnoreCase(this.getWJ_WJLX())){
			sql += "(select COUNT(*) from V_问卷调查_教师满意度评分表 where 创建人=a.学号 and 问卷编号='" + MyTools.fixSql(this.getWJ_WJBH()) + "')";
		}else{
			sql += "(select COUNT(*) from V_问卷调查_结果信息表 where 创建人=a.学号 and 问卷编号='" + MyTools.fixSql(this.getWJ_WJBH()) + "')";
		}
		sql += ">0 then '已完成' else '未完成' end as 完成情况 " +
			"from V_学生基本数据子类 as a " +
			"left join V_学校班级数据子类 b on a.行政班代码=b.行政班代码 " +
			"where a.学生状态 in ('01','05','07') " +
			"and b.行政班代码='" + MyTools.fixSql(this.getWJ_XZBDM()) + "' order by 完成情况";
		
		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
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
	
	/**
	 * 读取当前学年学期
	 * @date:2017-06-05
	 * @author:zhaixuchao
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		String curXnxq = "";
		Vector vec = null;
		String sql = "select top 1 学年学期编码 from dbo.V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		
		return curXnxq;
	}
	
	
	/**
	 * 获取重置按钮里的问卷名称
	 * 
	 * @date:2017-06-05
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCZWJMCCombo() throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql="select '' as comboValue,'请选择' as comboName,'0' as 排序 " +
				"union all " +
				"select 问卷编号,标题,'1' from V_问卷调查_问卷信息表  where 状态='1'";
		if (!"".equalsIgnoreCase(this.getWJ_XNXQBM())&&!"null".equalsIgnoreCase(this.getWJ_XNXQBM())) {
			sql += " and 学年学期编码 = '" + MyTools.fixSql(this.getWJ_XNXQBM())+ "'";
		}

		vec = db.getConttexJONSArr(sql, 0, 0);// 执行sql语句，赋值给vec
		return vec;
	}
	
	
	/**
	 * 获取重置按钮里的班级
	 * 
	 * @date:2017-06-05
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCZBJCombo() throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		Vector wjlxVec = new Vector();// 结果集
		
		sql="select 问卷类型 from dbo.V_问卷调查_问卷信息表 where 问卷编号='"+MyTools.fixSql(this.getWJ_WJBH())+"'";
		wjlxVec=db.GetContextVector(sql);
		
		sql="select '' as comboValue,'全部' as comboName,'0' as 排序  "+
				"union all "+
				"select distinct c.行政班代码,d.行政班名称,'1' from V_学生基本数据子类 as c " +
				"left join dbo.V_学校班级数据子类 as d on c.行政班代码=d.行政班代码  " +
				"where 学号 in ( " +
				"select distinct b.创建人 from dbo.V_问卷调查_问卷信息表 as a ";
		
		if("02".equalsIgnoreCase(MyTools.StrFiltr(wjlxVec.get(0)))){
			sql += "left join dbo.V_问卷调查_教师满意度评分表 as b on a.问卷编号=b.问卷编号  ";
		}else{
			sql += "left join dbo.V_问卷调查_结果信息表 as b on a.问卷编号=b.问卷编号  ";
		}
		sql+="left join  V_学生基本数据子类 as e on e.学号=b.创建人 "+
				"where 1=1 and a.状态='1' ";
		
		if (!"".equalsIgnoreCase(this.getWJ_XNXQBM())&&!"null".equalsIgnoreCase(this.getWJ_XNXQBM())) {
			sql += " and 学年学期编码 = '" + MyTools.fixSql(this.getWJ_XNXQBM())+ "' ";
		}
		if (!"".equalsIgnoreCase(this.getWJ_WJBH())&&!"null".equalsIgnoreCase(this.getWJ_WJBH())) {
			sql += " and a.问卷编号 = '" + MyTools.fixSql(this.getWJ_WJBH())+ "' ";
		}
		sql+=") ";
		sql+="order by 排序 asc,comboValue desc";
		
		vec = db.getConttexJONSArr(sql, 0, 0);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 获取重置按钮班级里的所有学生
	 * @date:2017-06-05
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadBJXSCombo() throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		Vector wjlxVec = new Vector();// 结果集
		sql = "select 问卷类型 from dbo.V_问卷调查_问卷信息表 where 问卷编号='"+MyTools.fixSql(this.getWJ_WJBH())+"'";
		wjlxVec=db.GetContextVector(sql);
		
		sql = "select '' as comboValue,'全部' as comboName,0 as orderNum " +
			"union all " +
			"select distinct a.创建人,b.姓名,1 from " ;
		if("02".equalsIgnoreCase(MyTools.StrFiltr(wjlxVec.get(0)))){
			sql += "V_问卷调查_教师满意度评分表 as a ";
		}else{
			sql += "V_问卷调查_结果信息表 as a ";
		}
		sql += "left join V_学生基本数据子类 as b on a.创建人=b.学号 " +
			"where a.问卷编号='"+MyTools.fixSql(this.getWJ_WJBH())+"'";
		
		if (!"".equalsIgnoreCase(this.getWJ_XZBDM()) && !"null".equalsIgnoreCase(this.getWJ_XZBDM())) {
			sql+=" and a.创建人 in (select 学号 from V_学生基本数据子类 where 行政班代码 in ('" + this.getWJ_XZBDM().replaceAll(",", "','")+"'))";
		}
		sql += " order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 重置选择数据的问卷信息
	 * @date:2017-06-05
	 * @author:zhaixuchao
	 * @throws SQLException
	 */
	public void delCZWJXX(String CZ_XSXM) throws SQLException {
		String sql="";
		Vector wjlxVec = new Vector();// 结果集
		
		sql = "select 问卷类型 from dbo.V_问卷调查_问卷信息表 where 问卷编号='"+MyTools.fixSql(this.getWJ_WJBH())+"'";
		wjlxVec = db.GetContextVector(sql);
		
		if(wjlxVec!=null && wjlxVec.size()>0){
			sql  = "delete from ";
			if("02".equalsIgnoreCase(MyTools.StrFiltr(wjlxVec.get(0)))){
				sql += "V_问卷调查_教师满意度评分表 ";
			}else{
				sql += "V_问卷调查_结果信息表 ";
			}
			sql += "where 状态='1' and 问卷编号 ='"+MyTools.fixSql(this.getWJ_WJBH()) + "'";
			
			if(!"".equalsIgnoreCase(MyTools.StrFiltr(CZ_XSXM)) && !"null".equalsIgnoreCase(MyTools.StrFiltr(CZ_XSXM))) {
				sql += " and 创建人 in ( '" + MyTools.StrFiltr(CZ_XSXM).replaceAll(",", "','")+ "') ";
			}else{
				if (!"".equalsIgnoreCase(this.getWJ_XZBDM()) && !"null".equalsIgnoreCase(this.getWJ_XZBDM())) {
					sql += " and 创建人 in (select 学号 from V_学生基本数据子类 where 行政班代码 in ('"+this.getWJ_XZBDM().replaceAll(",", "','")+"'))";
				}
			}
			
			if(db.executeInsertOrUpdate(sql)) {
				this.setMSG("重置成功");
			}else{
				this.setMSG("重置失败");
			}
		}else{
			this.setMSG("重置成功");
		}
	}

	public String getAUTHCODE() {
		return AUTHCODE;
	}

	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
	}

	public String getWJ_WJBH() {
		return WJ_WJBH;
	}

	public void setWJ_WJBH(String wJ_WJBH) {
		WJ_WJBH = wJ_WJBH;
	}

	public String getWJ_WJLX() {
		return WJ_WJLX;
	}

	public void setWJ_WJLX(String wJ_WJLX) {
		WJ_WJLX = wJ_WJLX;
	}

	public String getWJ_BT() {
		return WJ_BT;
	}

	public void setWJ_BT(String wJ_BT) {
		WJ_BT = wJ_BT;
	}

	public String getWJ_XNXQBM() {
		return WJ_XNXQBM;
	}

	public void setWJ_XNXQBM(String wJ_XNXQBM) {
		WJ_XNXQBM = wJ_XNXQBM;
	}

	public String getWJ_KSSJ() {
		return WJ_KSSJ;
	}

	public void setWJ_KSSJ(String wJ_KSSJ) {
		WJ_KSSJ = wJ_KSSJ;
	}

	public String getWJ_JSSJ() {
		return WJ_JSSJ;
	}

	public void setWJ_JSSJ(String wJ_JSSJ) {
		WJ_JSSJ = wJ_JSSJ;
	}

	public String getWJ_CJR() {
		return WJ_CJR;
	}

	public void setWJ_CJR(String wJ_CJR) {
		WJ_CJR = wJ_CJR;
	}

	public String getWJ_CJSJ() {
		return WJ_CJSJ;
	}

	public void setWJ_CJSJ(String wJ_CJSJ) {
		WJ_CJSJ = wJ_CJSJ;
	}

	public String getWJ_ZT() {
		return WJ_ZT;
	}

	public void setWJ_ZT(String wJ_ZT) {
		WJ_ZT = wJ_ZT;
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

	public String getWJ_NJ() {
		return WJ_NJ;
	}

	public void setWJ_NJ(String wJ_NJ) {
		WJ_NJ = wJ_NJ;
	}

	public String getWJ_XZBMC() {
		return WJ_XZBMC;
	}

	public void setWJ_XZBMC(String wJ_XZBMC) {
		WJ_XZBMC = wJ_XZBMC;
	}

	public String getWJ_WCQK() {
		return WJ_WCQK;
	}

	public void setWJ_WCQK(String wJ_WCQK) {
		WJ_WCQK = wJ_WCQK;
	}

	public String getWJ_XNXQBH() {
		return WJ_XNXQBH;
	}

	public void setWJ_XNXQBH(String wJ_XNXQBH) {
		WJ_XNXQBH = wJ_XNXQBH;
	}
	public String getWJ_XZBDM() {
		return WJ_XZBDM;
	}

	public void setWJ_XZBDM(String wJ_XZBDM) {
		WJ_XZBDM = wJ_XZBDM;
	}

}