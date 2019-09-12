package com.pantech.devolop.customExamManage;

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

public class KsszBean {

	private String AUTHCODE;//用户权限
	private String ZK_KSBH;//考试编号
	private String ZK_KSMC;//考试名称
	
	private String ZK_XN = "";//学年
	private String ZK_XQ = "";//学期
	
	private String ZK_XNXQBM;//学年学期编码
	private String ZK_LBBH;//类别编号
	private String ZK_CJR;//创建人
	private String ZK_CJSJ;//创建时间
	private String ZK_ZT;//状态
	
	private String ZK_BJBH;//班级编号
	private String ZK_KSLB;//考试类别
	private String ZK_ZSLB;//招生类别
	private String ZK_DJKXSLB;//等级考学生类别
	private String ZK_DFKSSJ;//登分开始时间
	private String ZK_DFJSSJ;//登分结束时间

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
		ZK_KSBH = "";// 考试编号
		ZK_KSMC = "";// 考试名称
		
		ZK_XN = "";// 学年
		ZK_XQ = "";// 学期
		
		ZK_XNXQBM = "";// 学年学期编码
		ZK_LBBH = "";// 类别编号
		ZK_CJR = "";// 创建人
		ZK_CJSJ = "";// 创建时间
		ZK_ZT = "";// 状态
		USERCODE = "";// 用户编号
		ZK_BJBH = "";// 班级编号
		ZK_KSLB = "";// 考试类别
		ZK_ZSLB = "";// 招生类别
		ZK_DJKXSLB = "";// 等级考学生类别
		ZK_DFKSSJ = "";//登分开始时间
		ZK_DFJSSJ = "";//登分结束时间
	}

	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public KsszBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}

	/**
	 * 分页查询 考试信息列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String XN_CX, String XQ_CX, String KSMC_CX, String KSLB_CX ) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select a.考试编号,a.考试名称,a.学年学期编码,c.学年学期名称,a.类别编号,b.类别名称,a.招生类别,a.等级考学生类别," +
				"convert(nvarchar(10),a.登分开始时间,21) as 登分开始时间," +
				"convert(nvarchar(10),a.登分结束时间,21) as 登分结束时间 " +
				"from V_自设考试管理_考试信息表 as a " +
				"left join V_信息类别_类别操作 as b on a.类别编号=b.描述 " +
				"left join V_规则管理_学年学期表 as c on a.学年学期编码=c.学年学期编码 " +
				"where b.父类别代码='KSLBDM' and a.状态='1' and c.状态='1'";
		// 判断查询条件
		if (!"".equalsIgnoreCase(XN_CX)) {
			sql += " and c.学年= '" + MyTools.fixSql(XN_CX) + "'";
		}
		if (!"".equalsIgnoreCase(XQ_CX)) {
			sql += " and c.学期= '" + MyTools.fixSql(XQ_CX)+ "'";
		}
		if (!"".equalsIgnoreCase(KSMC_CX)) {
			sql += " and a.考试名称  like '%" + MyTools.fixSql(KSMC_CX) + "%'";
		}
		if (!"".equalsIgnoreCase(KSLB_CX)) {
			sql += " and a.类别编号 = '" + MyTools.fixSql(KSLB_CX) + "'";
		}

		sql = sql + " ORDER BY a.学年学期编码 desc,a.创建时间 desc ";

		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0  as orderNum " +
				"union all " +
				"select distinct 学年 as comboName,学年 as comboValue,1 " +
				"from V_规则管理_学年学期表 " +
				"where 状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学期下拉框
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct case when 学期='1' then '第一学期' else '第二学期' end as comboName,学期 as comboValue " +
				"from V_规则管理_学年学期表  where 状态='1' order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取当前学年学期
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		String curXnxq = "";
		Vector vec = null;
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		
		return curXnxq;
	}
	
	/**
	 * 读取考试类别下拉框
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadLbCombo() throws SQLException{
		Vector vec = null;
		String sql="select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 类别名称 , 描述  from  V_信息类别_类别操作 where 父类别代码='KSLBDM' " +
				"order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学期下拉框
	 * @date:2017-06-28
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnXqCombo() throws SQLException{
		Vector vec = null;
		String sql="select '请选择' as comboName,'' as comboValue ,0  as orderNum " +
				"union all " +
				"select 学年学期名称 as comboName,学年学期编码 as comboValue,1 " +
				"from V_规则管理_学年学期表  where 状态='1' " +
				"order by orderNum,comboValue desc" ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 保存
	 * @date:2017-06-28
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void saveRec() throws WrongSQLException, SQLException {
		String sql = "select count(*) from V_自设考试管理_考试信息表  where 状态='1' and 考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "'";
		
		if(!db.getResultFromDB(sql)){
			addRec();
		}else{
			modRec();
		}
	}
	
	/**
	 * 新增
	 * @date:2017-06-28
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void addRec() throws WrongSQLException, SQLException {
		String sql = "";
		Vector zslbVec = new Vector();
		String zslbString="";
		
		this.setZK_KSBH(db.getMaxID("V_自设考试管理_考试信息表", "考试编号", "KS_", 10));// 获取主关键字
		sql = "insert into V_自设考试管理_考试信息表 (考试编号,考试名称,学年学期编码,类别编号,招生类别,等级考学生类别,登分开始时间,登分结束时间,创建人,创建时间,状态) values (" +
			"'" + MyTools.fixSql(this.getZK_KSBH()) + "'," +
			"'" + MyTools.fixSql(this.getZK_KSMC()) + "'," +
			"'" + MyTools.fixSql(this.getZK_XNXQBM()) + "'," +
			"'" + MyTools.fixSql(this.getZK_KSLB()) + "'," +
		    "'" + MyTools.fixSql(this.getZK_ZSLB()) + "'," +
		    "'" + MyTools.fixSql(this.getZK_DJKXSLB()) + "' ," +
		    "'" + MyTools.fixSql(this.getZK_DFKSSJ()) + "' ," +
		    "'" + MyTools.fixSql(this.getZK_DFJSSJ()) + "' ," +
			"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
			"getDate(),'1')" ;
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 修改
	 * @date:2016-06-28
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void modRec() throws WrongSQLException, SQLException {
		String sql = "update V_自设考试管理_考试信息表 set " +
			"考试名称='" + MyTools.fixSql(this.getZK_KSMC()) + "'," +
			"学年学期编码='" + MyTools.fixSql(this.getZK_XNXQBM()) + "'," +
			"类别编号='" + MyTools.fixSql(this.getZK_KSLB()) + "'," +
			"招生类别='" + MyTools.fixSql(this.getZK_ZSLB()) + "'," +
			"等级考学生类别='" + MyTools.fixSql(this.getZK_DJKXSLB()) + "'," +
			"登分开始时间='" + MyTools.fixSql(this.getZK_DFKSSJ()) + "'," +
			"登分结束时间='" + MyTools.fixSql(this.getZK_DFJSSJ()) + "' " +
			"where 考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	
	/**
	 * 删除
	 * @date:2017-06-28
	 * @author:zhaixuchao
	 * @throws SQLException
	 */
	public void delRec() throws SQLException {
		Vector sqlVec = new Vector();
		String sql = ""; 
				
		sql = "delete from V_自设考试管理_考试信息表   where 考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "'";
		sqlVec.add(sql);
		
		sql="delete from V_自设考试管理_考试学科信息表  where 考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "'";
		sqlVec.add(sql);
		
		sql="delete from V_自设考试管理_学生成绩信息表 where 考试学科编号 in (select 考试学科编号 from V_自设考试管理_考试学科信息表 where 考试编号='"+MyTools.fixSql(this.getZK_KSBH())+"')";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 分页查询 月考考试列表
	 * @return
	 * @throws SQLException
	 */
	public Vector queryykksRec(int pageNum, int page, String ksmc) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		//sql="select a.编号,a.考试名称,a.课程代码,a.班级代码,convert(varchar(50),a.每月创建时间,23) as 每月创建时间,convert(varchar(20),datepart (DD,convert(varchar(100),a.每月创建时间,23)))+'日' as 每月创建日期 ,a.状态 from V_自设考试管理_月考设置信息表 as a where 1=1 ";
		
		sql="select a.编号,a.考试名称,a.课程代码,a.年级,a.等级考学生类别,a.每月创建时间+'日' as 每月创建日期 ,a.状态 from V_自设考试管理_月考设置信息表 as a where 1=1 "; 
		// 判断查询条件
		if (!"".equalsIgnoreCase(ksmc)) {
			sql += " and a.考试名称 like '%" + MyTools.fixSql(ksmc) + "%'";
		}

		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 修改记录 月考状态
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void ModYKRec(String bh, String zt) throws WrongSQLException, SQLException {
		String zhuangtai="";//状态
		String sql = "update V_自设考试管理_月考设置信息表  set ";
		if ("1".equalsIgnoreCase(zt)) {
			zhuangtai="0";
		}
		if("0".equalsIgnoreCase(zt)){
			zhuangtai="1";
		}
		sql+="状态='"+ MyTools.fixSql(zhuangtai)+"' where 编号='"+MyTools.fixSql(bh)+"'";
		if (db.executeInsertOrUpdate(sql)) {
			this.MSG = "修改成功";
		} else {
			this.MSG = "修改失败";
		}
	}
	
	//年级下拉框赋值
	public Vector loadBJCombo() throws SQLException {
		Vector vec = null;
		String sql = "";
		Vector xnVec = null;
		String xn = "";
		sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		xnVec = db.GetContextVector(sql);
		xn = xnVec.get(0).toString().substring(0, 4);
		sql = "select '全部' as comboName ,'all' as comboValue " +
			"union all " +
			"select comboName,comboValue  from (select top 3  case " +
			"when 所属年份='"+xn+"' then'一年级' " +
			"when 所属年份='"+(MyTools.StringToInt(xn)-1)+"' then'二年级' " +
			"when 所属年份='"+(MyTools.StringToInt(xn)-2)+"' then'三年级' "+
			"end as comboName ,所属年份 as comboValue " +
			"from  V_学校年级数据子类 " +
			"where 年级状态='1' "+
			"order by comboValue desc) as t" ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	//课程下拉框赋值
	public Vector loadKCMCCombo() throws SQLException {
		Vector vec = null;
		String sql="";
		
		sql = "select 'all' as comboValue,'全部' as comboName,0  as orderNum " +
			"union all " +
			"select 课程号 as comboValue , 课程名称 as comboName , 1 from V_课程数据子类 where LEFT(课程号,1)=0 " +
			"order by orderNum,comboValue ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 保存月考信息
	 * @date:2017-06-29
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void saveYKXX(String bh,String ksmc,String bj,String kcmc,String cjsj) throws WrongSQLException, SQLException {
		String sql = "select count(*) from V_自设考试管理_月考设置信息表  where 状态='1' and  编号='" + MyTools.fixSql(bh) + "'";
		
		if(!db.getResultFromDB(sql)){
			addYKXXRec(bh,ksmc,bj,kcmc,cjsj);
		}else{
			modYKXXRec(bh,ksmc,bj,kcmc,cjsj);
		}
	}
	
	/**
	 * 新增月考信息
	 * @date:2017-06-29
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void addYKXXRec(String bh,String ksmc,String bj,String kcmc,String cjsj) throws WrongSQLException, SQLException {
		String sql = "";
		Vector kcvec = new Vector();
		
		Vector xnVec = new Vector();
		
		Vector zslbVec = new Vector();
		String zslbString="";
		String xn="";
		String quanbuxn="";
		
		if(kcmc.equalsIgnoreCase("all")){
			sql = "select 课程号  from V_课程数据子类 where LEFT(课程号,1)=0";
			kcvec = db.GetContextVector(sql);
		}
		if(bj.equalsIgnoreCase("all")){
			//sql="select 行政班代码   from dbo.V_学校班级数据子类 where 状态='1'";
			
			sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
			xnVec = db.GetContextVector(sql);
			xn = xnVec.get(0).toString().substring(0, 4);
			quanbuxn= xn+","+(MyTools.StringToInt(xn)-1)+","+(MyTools.StringToInt(xn)-2);
		}
		
		sql="insert into V_自设考试管理_月考设置信息表 values(";
		sql+="'" + MyTools.fixSql(ksmc);
		if(kcvec != null && kcvec.size() > 0){
			sql+="','";
			for(int i=0;i<kcvec.size();i++){
				if(i<kcvec.size()){
					sql+= ""+kcvec.get(i)+"," ;
				}
				if(i==kcvec.size()-1){
					sql+= ""+kcvec.get(i)+"" ;
				}
			}
			sql+="',";
		}
		else{
			sql+="',";
			sql+="'" + MyTools.fixSql(kcmc) + "',";
		}
		
		if(quanbuxn != null && quanbuxn!=""){
			sql+="'";
			//for(int i=0;i<bjvec.size();i++){
				/*if(i<bjvec.size()){
					sql+= ""+bjvec.get(i)+"," ;
				}
				if(i==bjvec.size()-1){
					sql+= ""+bjvec.get(i)+"" ;
				}*/
				sql+=""+quanbuxn+"";
			//}
			sql+="',";
		}
		else{
			sql+="'" + MyTools.fixSql(bj) + "',";
		}
		sql+="'" + MyTools.fixSql(this.getZK_DJKXSLB()) + "',";
		sql+="'" + MyTools.fixSql(cjsj) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')" ;
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 修改月考信息
	 * @date:2016-06-28
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void modYKXXRec(String bh,String ksmc,String bj,String kcmc,String cjsj) throws WrongSQLException, SQLException {
		String sql = "";
		Vector kcvec = new Vector();
		Vector xnVec = new Vector();
		String xn="";
		String quanbuxn="";
		
		Vector zslbVec = new Vector();
		String zslbString="";
		
		if(kcmc.equalsIgnoreCase("all")){
			sql = "select 课程号  from V_课程数据子类 where LEFT(课程号,1)=0";
			kcvec = db.GetContextVector(sql);
		}
		if(bj.equalsIgnoreCase("all")){
			sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
			xnVec = db.GetContextVector(sql);
			xn = xnVec.get(0).toString().substring(0, 4);
			quanbuxn = xn+","+(MyTools.StringToInt(xn)-1)+","+(MyTools.StringToInt(xn)-2);
		}
		
		sql = "update V_自设考试管理_月考设置信息表  set 考试名称='" + MyTools.fixSql(ksmc) + "',";
		if(kcvec != null && kcvec.size() > 0){
			sql += "课程代码='" + kcvec + "',";
		}else{
			sql += "课程代码='" + MyTools.fixSql(kcmc) + "',";
		}
		
		if(quanbuxn != null && quanbuxn!=""){
			sql += "年级='" + quanbuxn + "',";
		}else{
			sql += "年级='" + MyTools.fixSql(bj) + "',";
		}
		sql+="等级考学生类别='" + MyTools.fixSql(this.getZK_DJKXSLB()) + "',";
		sql += "每月创建时间='" + MyTools.fixSql(cjsj) + "'" ;
		sql += " where 编号='"+bh+"'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除月考信息
	 * @date:2017-06-29
	 * @author:zhaixuchao
	 * @throws SQLException
	 */
	public void delYKRec(String bh) throws SQLException {
		String sql = ""; 
				
		sql = "delete from V_自设考试管理_月考设置信息表   where 编号='" + MyTools.fixSql(bh) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 查询班级信息
	 * @date:2017-07-03
	 * @author:zhaixuchao
	 * @return 班级信息
	 * @throws SQLException
	 */
	public String loadClassTree()throws SQLException{
		Vector vec = null;
		String sql = "";
		String sqlCondition = "";
		String XN = this.getZK_XNXQBM().substring(0, 4);
		int examXn = MyTools.StringToInt(XN);
		String tempXn = "";
		String result = "[";
		
		for(int i=0; i<3; i++){
			tempXn = MyTools.StrFiltr(examXn-i);
			sqlCondition += "'"+tempXn+"',";
		}
		sqlCondition = sqlCondition.substring(0, sqlCondition.length()-1);
		
		sql="select distinct a.行政班代码,a.行政班名称 " +
				"from V_学校班级数据子类 as a " +
				"left join V_学校年级数据子类 as b on a.年级代码=b.年级代码 " +
				"left join V_规则管理_学年学期表 as c on b.所属年份=c.学年 " +
				"where a.状态='1' and b.年级状态='1' and c.状态='1' and  c.学年 in (" + sqlCondition + ") " +
				"order by a.行政班代码";
		
		
		vec = db.GetContextVector(sql);
				
		for(int j=0; j<vec.size(); j+=2){
			result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(j)) + "\"," +
					"\"text\":\"" + MyTools.StrFiltr(vec.get(j+1)) + "\"," +
					"\"state\":\"open\"},";
		}
		if(result.length() > 1){
			result = result.substring(0, result.length()-1);
		}
		result += "]";
		return result;
	}
	
	/**
	 * 查询班级已选学科信息
	 * @date:2016-07-01
	 * @author:zhaixuchao
	 * @return String 结果集
	 * @throws SQLException
	 */
	public String loadClassSelSubject() throws SQLException {
		String result = "[";
		String sql = ""; // 查询用SQL语句
		Vector vec = null;
		Vector xkInfoVec = null;
		boolean xkFlag = false;
		String tempXkdm = "";
		//读取所有学科信息
		sql="select 课程号,'2'  from V_课程数据子类 where LEFT(课程号,1)=0"; 
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//读取已选学科信息
			sql="select a.课程代码 from V_自设考试管理_考试学科信息表 as a " +
							"where a.状态='1' and a.考试编号='"+ MyTools.fixSql(this.getZK_KSBH()) +"' " +
							"and a.班级代码='" + MyTools.fixSql(this.getZK_BJBH()) + "'";
			xkInfoVec = db.GetContextVector(sql);
			
			
			for(int i=0;i<vec.size();i+=2){
				xkFlag = false;
				tempXkdm = MyTools.StrFiltr(vec.get(i));
				for(int j=0;j<xkInfoVec.size();j++){
					if(tempXkdm.equalsIgnoreCase(MyTools.StrFiltr(xkInfoVec.get(j)))){
						vec.set(i+1, "1");
						xkFlag = true;
						break;
					}
				}
			}
		}
			
		for(int i=0; i<vec.size(); i+=2){
				result += "{\"xkdm\":\"" + MyTools.StrFiltr(vec.get(i)) + "\","+
						"\"state\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"},";
			}
				
		if(result.length() > 1){
				result = result.substring(0, result.length()-1);
			}
		result += "]";
		return result;
	}
	
	/**
	 * 查询学科信息列表
	 * @date:2017-07-03
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector querySubjectList(int pageNum, int pageSize) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql="select 课程号,课程名称 from V_课程数据子类 where LEFT(课程号,1)=0 order by 课程号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	
	/**
	 * 保存班级已选学科信息
	 * @date:2017-07-03
	 * @author:zhaixuchao
	 * @param selXkInfo 选择的学科信息
	 * @throws SQLException
	 */
	public void saveSelXk(String selXkInfo) throws SQLException {
		Vector sqlVec = new Vector();
		Vector vec = null;
		String sql = "";
		String xkArray[] = selXkInfo.split("｜");
		boolean existFlag = false;
		String id = "";
		Vector addXkVec = new Vector();
		Vector updateXkVec = new Vector();
		//String internalStu = MyTools.getProp(request, "Base.internalStu");
		
		//读取当前考试班级所有已选学科
		sql="select a.考试学科编号,a.课程代码 from V_自设考试管理_考试学科信息表 as a " +
				"where a.状态='1' and a.考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "' " +
				"and a.班级代码='" + MyTools.fixSql(this.getZK_BJBH()) + "'";
		vec = db.GetContextVector(sql);
		
		
		sql="update V_自设考试管理_考试学科信息表 set 状态='0' " +
				"where 考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "' and 班级代码='" + MyTools.fixSql(this.getZK_BJBH()) + "'" ;
		sqlVec.add(sql);
		
		if(selXkInfo.length() > 0){
			for(int i=0; i<xkArray.length; i++){
				existFlag = false;
				//判断已选学科是否已存在
				for(int j=0; j<vec.size(); j+=2){
					if(xkArray[i].equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1)))){
						id = MyTools.StrFiltr(vec.get(j));
						existFlag = true;
						break;
					}
				}
				
				if(existFlag == false){
					id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
					
					sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
						"'" + MyTools.fixSql(id) + "'," +
						"'" + MyTools.fixSql(this.getZK_KSBH()) + "'," +
						"'" + MyTools.fixSql(this.getZK_BJBH()) + "'," +
						"'" + MyTools.fixSql(xkArray[i]) + "'," +
						"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
						"getDate(),'1')";
					addXkVec.add(id);
					addXkVec.add(xkArray[i]);
				}else{
					sql = "update V_自设考试管理_考试学科信息表 " +
						"set 状态='1' " +
						"where 考试学科编号='" + MyTools.fixSql(id) + "'";
					updateXkVec.add(id);
				}
				
				sqlVec.add(sql);
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			sqlVec.clear();
			
			//处理学生成绩信息
			
			sql="update a set a.状态='0' from V_自设考试管理_学生成绩信息表 as a left join V_自设考试管理_考试学科信息表 as b on a.考试学科编号=b.考试学科编号 " +
					"where b.考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "' and b.班级代码='" + MyTools.fixSql(this.getZK_BJBH()) + "'";
			sqlVec.add(sql);
			
			for(int i=0; i<updateXkVec.size(); i++){
				sql = "update V_自设考试管理_学生成绩信息表  set 状态='1' where 考试学科编号='" + MyTools.fixSql(MyTools.StrFiltr(updateXkVec.get(i))) + "'";
				sqlVec.add(sql);
			}
			
			for(int i=0; i<addXkVec.size(); i+=2){
				
				sql="insert into V_自设考试管理_学生成绩信息表 (学号,姓名,考试学科编号,创建人,创建时间,状态) " +
						"select a.学号,a.姓名,'" + MyTools.fixSql(MyTools.StrFiltr(addXkVec.get(i))) + "' as 考试学科编号,'" + MyTools.fixSql(this.getUSERCODE()) + "',GETDATE(),'1' " +
						"from V_学生基本数据子类 as a " +
						"where a.学生状态 in ('01','05') and a.行政班代码='" + MyTools.fixSql(this.getZK_BJBH())+"' ";
				if(!this.getZK_ZSLB().equalsIgnoreCase("all")){
					sql+="' and a.招生类别 in ('"+ MyTools.fixSql(this.getZK_ZSLB()).replaceAll(",", "','") +"') ";
				}
						
				sql+=" order by a.班内学号 ";
				sqlVec.add(sql);
			}
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}else{
			this.setMSG("保存失败");
		}
	}
	
	
	//考试学科批量设置学年combobox赋值
	public Vector loadXKPLCombo() throws SQLException {
		Vector vec = null;
		String sql="";
		String xn="";
		Vector xnVec = null;
		String tempXn="";
		String sqlCondition="";
			
		/*sql="select '请选择' as comboName,'' as comboValue,0 as yearNum,0  as orderNum " +
				"union all " +
				"select a.年级名称,a.所属年份,a.年级代码,1 from V_学校年级数据子类 as a where a.年级状态='1' " +
				"order by orderNum,comboValue desc" ;*/
		
		sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		xnVec=db.GetContextVector(sql);
		xn=xnVec.get(0).toString().substring(0, 4);
		
		for(int i=0; i<3; i++){
			tempXn = MyTools.StrFiltr(MyTools.StringToInt(xn)-i);
			sqlCondition += "'"+tempXn+"',";
		}
		sqlCondition = sqlCondition.substring(0, sqlCondition.length()-1);
		
		sql="select '请选择' as comboName,'' as comboValue,0  as orderNum " +
				"union all " +
				"select a.年级名称,a.年级代码,1 from V_学校年级数据子类 as a where a.年级状态='1' and a.所属年份 in (" + sqlCondition + ") " +
				"order by orderNum,comboValue desc ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	//考试学科批量设置专业combobox赋值
	public Vector loadXKPLZYCombo(String xn) throws SQLException {
		Vector vec = null;
		String sql="";
		
		/*sql="select 'all' as comboValue,'全部' as comboName,0 as orderNum " +
				"union all " +
				"select distinct a.专业代码,a.专业名称,'1' " +
				"from V_规则管理_授课计划主表 as a " +
				"left join V_规则管理_授课计划明细表 as b on a.授课计划主表编号=b.授课计划主表编号 " +
				" where 1=1  ";
		
		// 判断查询条件
		if (!"".equalsIgnoreCase(xn)) {
			sql += " and left(a.学年学期编码,4) in ('" + MyTools.fixSql(xn).replaceAll(",", "','") + "') ";
		}*/
		
		/*sql="select 'all' as comboValue,'全部' as comboName,0 as orderNum " +
				"union all " +
				"select distinct a.专业代码,a.专业名称,1 " +
				"from V_专业基本信息数据子类 as a " +
				"left join V_学校班级数据子类 as b on a.专业代码=a.专业代码 " +
				"where a.状态='1' and b.状态='1' ";*/
		
		//==========================
		sql="select 'all' as comboValue,'全部' as comboName,0 as orderNum " +
				"union all " +
				"select distinct a.系部代码,a.系部名称,1 " +
				"from dbo.V_基础信息_系部信息表 as a " +
				"left join dbo.V_学校班级数据子类 as b on a.系部代码=b.系部代码 " +
				"where a.状态='Y' and b.状态='1' ";
		//=-===========
		
		// 判断查询条件
		if (!"".equalsIgnoreCase(xn)) {
			sql += " and b.年级代码  in ('" + MyTools.fixSql(xn).replaceAll(",", "','") + "') ";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	//考试学科批量设置课程combobox赋值
	public Vector loadXKPLKCCombo(String xn,String zy) throws SQLException {
		Vector vec = null;
		String sql="";
			
		/*sql="select 'all' as comboValue,'全部' as comboName,0 as orderNum " +
				"union all " +
				"select distinct b.课程代码,b.课程名称,1 " +
				"from V_规则管理_授课计划主表 as a " +
				"left join V_规则管理_授课计划明细表 as b on a.授课计划主表编号=b.授课计划主表编号 " +
				"where a.状态='1' and b.状态='1' and LEFT(b.课程代码,1)=0 ";
		// 判断查询条件
		if (!"".equalsIgnoreCase(xn)) {
			sql += " and left(a.学年学期编码,4) in ('" + MyTools.fixSql(xn).replaceAll(",", "','") + "') ";
		}
		
		if (!"all".equalsIgnoreCase(zy) && !"".equalsIgnoreCase(zy)) {
			sql += " and a.专业代码 in ('" + MyTools.fixSql(zy).replaceAll(",", "','") + "') ";
		}*/
		
		sql="select 'all' as comboValue,'全部' as comboName,0  as orderNum " +
				"union all " +
				"select 课程号 as comboValue , 课程名称 as comboName , 1 from V_课程数据子类 where LEFT(课程号,1)=0 " +
				"order by orderNum,comboValue ";
			
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 保存班级已选学科信息(批量设置)
	 * @date:2017-07-03
	 * @author:zhaixuchao
	 * @param selXkInfo 选择的学科信息
	 * @throws SQLException
	 */
	public void saveSelXkPL(String plnjInfo,String plzyInfo,String plkcInfo) throws SQLException {
		Vector sqlVec = new Vector();
		Vector vec = null;
		String sql = "";
		String xkArray[] = plkcInfo.split(",");
		boolean existFlag = false;
		String id = "";
		String classno = "";
		Vector addXkVec = new Vector();
		Vector updateXkVec = new Vector();
		
		Vector bjVec = new Vector();
		Vector bjfzVec = new Vector();
		
		Vector zydmVec = new Vector();
		String zydm = "";
		
		Vector kcdmVec = new Vector();
		String kcdm = "";
		
		Vector KCDMVec=new Vector();
		
		String kcdmString="";
		
		Vector bjcopyVec = new Vector();
		
		//String internalStu = MyTools.getProp(request, "Base.internalStu");
		/*sql="select distinct a.专业代码 " +
				"from V_规则管理_授课计划主表 as a " +
				"left join V_规则管理_授课计划明细表 as b on a.授课计划主表编号=b.授课计划主表编号 " +
				"where a.状态='1' and b.状态='1' and LEFT(a.学年学期编码,4) in ('" + MyTools.fixSql(plnjInfo).replaceAll(",", "','") + "')" ;
		*/
		/*sql="select distinct a.专业代码 " +
				"from V_专业基本信息数据子类 as a " +
				"left join V_学校班级数据子类 as b on a.专业代码=a.专业代码 " +
				"where a.状态='1' and b.状态='1' and b.年级代码 in ('" + MyTools.fixSql(plnjInfo).replaceAll(",", "','") + "') ";*/
		
		sql = "select distinct a.系部代码,a.系部名称 " +
			"from dbo.V_基础信息_系部信息表 as a " +
			"left join dbo.V_学校班级数据子类 as b on a.系部代码=b.系部代码 " +
			"where a.状态='Y' and b.状态='1'  and b.年级代码  in ('" + MyTools.fixSql(plnjInfo).replaceAll(",", "','") + "') ";
		//===============
		
		zydmVec=db.GetContextVector(sql);
		for(int i=0;i<zydmVec.size();i++){
			zydm += zydmVec.get(i) + ",";
		}
		zydm.substring(0, zydm.length()-1);
		String zydmStr[] = zydm.split(",");
		
		String zydmString="";
		for(int i=0;i<zydmStr.length;i++){
			zydmString += "'" + zydmStr[i] + "',";
		}
		if(!zydmString.equals("")){
			zydmString=zydmString.substring(0, zydmString.length()-1);
		}
		if ("all".equalsIgnoreCase(plkcInfo) && !"".equalsIgnoreCase(plkcInfo)) {
			//====================================================================================
			/*sql="select distinct b.课程代码  " +
					"from V_规则管理_授课计划主表 as a " +
					"left join V_规则管理_授课计划明细表 as b on a.授课计划主表编号=b.授课计划主表编号 " +
					"where a.状态='1' and b.状态='1' and LEFT(b.课程代码,1)=0 and LEFT(a.学年学期编码,4) in ('" + MyTools.fixSql(plnjInfo).replaceAll(",", "','") + "')" ;
			if (!"all".equalsIgnoreCase(plzyInfo) && !"".equalsIgnoreCase(plzyInfo)) {
				sql += " and a.专业代码 in ('" + MyTools.fixSql(plzyInfo).replaceAll(",", "','") + "') ";
			}else{
				sql += " and a.专业代码 in (" + zydmString + ") ";
			}*/
			
			
			sql="select 课程号  from V_课程数据子类 where LEFT(课程号,1)=0 order by 课程号";
			//===================================================================
			kcdmVec=db.GetContextVector(sql);
			if(kcdmVec!=null && kcdmVec.size()>0){
				for(int i=0; i<kcdmVec.size(); i++){
					KCDMVec.add(kcdmVec.get(i));	
				}
			}
			for(int i=0;i<kcdmVec.size();i++){
				kcdm += kcdmVec.get(i) + ",";
			}
			kcdm.substring(0, kcdm.length()-1);
			String kcdmStr[] = kcdm.split(",");
			
			//==============
			kcdmString="";
			for(int i=0;i<kcdmStr.length;i++){
				kcdmString += "'" + kcdmStr[i] + "',";
			}
			if(!kcdmString.equals("")){
				kcdmString=kcdmString.substring(0, kcdmString.length()-1);
			}
		}else{
			for(int i=0;i<xkArray.length;i++){
				KCDMVec.add(xkArray[i]);
			}
		}
		
		//=============================================
		//kcdmVec=db.GetContextVector(sql);
		//===============================================
		//读取当前考试班级所有已选学科
		sql="select a.考试学科编号,a.课程代码,a.班级代码  from V_自设考试管理_考试学科信息表 as a " +
				"left join V_自设考试管理_考试信息表 as b on a.考试编号=b.考试编号 " +
				"left join V_学校班级数据子类 as c on a.班级代码=c.行政班代码 " +
				//"left join V_学校年级数据子类 as d on c.年级代码=d.年级代码 "+
				"where a.状态='1' and b.状态='1' and c.状态='1' "+
				" and c.年级代码 in ('" + MyTools.fixSql(plnjInfo).replaceAll(",", "','") + "') ";
		if (!"all".equalsIgnoreCase(plzyInfo) && !"".equalsIgnoreCase(plzyInfo)) {
			sql += " and c.系部代码 in ('" + MyTools.fixSql(plzyInfo).replaceAll(",", "','") + "') ";
		}
		else{
			sql += " and c.系部代码 in (" + zydmString + ") ";
		}
		if (!"all".equalsIgnoreCase(plkcInfo) && !"".equalsIgnoreCase(plkcInfo)) {
			sql += " and a.课程代码 in ('" + MyTools.fixSql(plkcInfo).replaceAll(",", "','") + "') ";
		}else{
			sql += " and a.课程代码 in (" + kcdmString + ") ";
		}
		sql+=" and b.考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "' ";
		
		vec = db.GetContextVector(sql);
		
		sql="update a set a.状态='0' from V_自设考试管理_考试学科信息表 as a " +
				"left join V_自设考试管理_考试信息表 as b on a.考试编号=b.考试编号 " +
				"left join V_学校班级数据子类 as c on a.班级代码=c.行政班代码 " +
				"where a.状态='1' and b.状态='1' and c.状态='1' "+
				" and c.年级代码  in ('" + MyTools.fixSql(plnjInfo).replaceAll(",", "','") + "') ";
		if (!"all".equalsIgnoreCase(plzyInfo) && !"".equalsIgnoreCase(plzyInfo)) {
			sql += " and c.系部代码 in ('" + MyTools.fixSql(plzyInfo).replaceAll(",", "','") + "') ";
		}else{
			sql += " and c.系部代码 in (" + zydmString + ") ";
		}
		
		/*if (!"all".equalsIgnoreCase(plkcInfo) && !"".equalsIgnoreCase(plkcInfo)) {
			sql += " and a.课程代码 in ('" + MyTools.fixSql(plkcInfo).replaceAll(",", "','") + "') ";
		}else{
			sql += " and a.课程代码 in (" + kcdmString + ") ";
		}*/
		sql+=" and b.考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "' ";
		sqlVec.add(sql);
		
		sql="select a.行政班代码 from V_学校班级数据子类 as a " +
				"where a.状态='1' and a.年级代码 in ('" +  MyTools.fixSql(plnjInfo).replaceAll(",", "','") + "') ";
		if (!"all".equalsIgnoreCase(plzyInfo) && !"".equalsIgnoreCase(plzyInfo)) {
			sql += " and a.系部代码 in ('" + MyTools.fixSql(plzyInfo).replaceAll(",", "','") + "') ";
		}else{
			sql += " and a.系部代码 in (" + zydmString + ") ";
		}
		
		bjVec = db.GetContextVector(sql);
		for(int i =0;i<bjVec.size();i++){
			bjfzVec.add(bjVec.get(i));
		}
		
		if(plkcInfo.length() > 0 ){
			for(int i=0; i<KCDMVec.size(); i++){
				existFlag = false;
				//================
				for(int k =0;k<bjVec.size();k++){
					bjcopyVec.add(bjVec.get(k));
				}
				for(int k=0;k<vec.size();k++){
					bjcopyVec.remove(vec.get(k));
				}
				//判断已选学科是否已存在
				for(int j=0; j<vec.size(); j+=3){
					if(MyTools.StrFiltr(KCDMVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1)))){
						id = MyTools.StrFiltr(vec.get(j));
						classno = MyTools.StrFiltr(vec.get(j+2));
						existFlag = true;
						break;
					}
				}
				if(existFlag == false){
					int num=0;
						if(vec.size()==0){
							for(int k=0;k<KCDMVec.size();k++){
								for(int r=0;r<bjfzVec.size();r++){
									
									id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
									classno=MyTools.StrFiltr(bjfzVec.get(r));
									//bjfzVec.remove(r);
									sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
											"'" + MyTools.fixSql(id) + "'," +
											"'" + MyTools.fixSql(this.getZK_KSBH()) + "'," +
											"'" + MyTools.fixSql(classno) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(k))) + "'," +
											"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
											"getDate(),'1')";
									addXkVec.add(id);
									addXkVec.add(MyTools.StrFiltr(KCDMVec.get(k)));
									sqlVec.add(sql);
								}
								num++;
							}
							if(num==KCDMVec.size()){
								break;
							}
						}else{
							for(int r=0;r<bjfzVec.size();r++){
								id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
								classno=MyTools.StrFiltr(bjfzVec.get(r));
								
								sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
										"'" + MyTools.fixSql(id) + "'," +
										"'" + MyTools.fixSql(this.getZK_KSBH()) + "'," +
										"'" + MyTools.fixSql(classno) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(i))) + "'," +
										"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
										"getDate(),'1')";
									addXkVec.add(id);
									addXkVec.add(MyTools.StrFiltr(KCDMVec.get(i)));
									sqlVec.add(sql);
							}
						}
					
				}else{
					for(int j=0; j<vec.size(); j+=3){
						String classid="";
						if(MyTools.StrFiltr(KCDMVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1)))){
							id = MyTools.StrFiltr(vec.get(j));
							classid=MyTools.StrFiltr(vec.get(j+2));
							sql = "update V_自设考试管理_考试学科信息表 " +
									"set 状态='1' " +
									"where 考试学科编号='" + MyTools.fixSql(id) + "'";
							updateXkVec.add(id);
							sqlVec.add(sql);
							
							if(bjcopyVec.size()>0){
								for(int k=0;k<bjcopyVec.size();k++){
									id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
									sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
											"'" + MyTools.fixSql(id) + "'," +
											"'" + MyTools.fixSql(this.getZK_KSBH()) + "'," +
											"'" + bjcopyVec.get(k) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(i))) + "'," +
											"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
											"getDate(),'1')";
									addXkVec.add(id);
									addXkVec.add(MyTools.StrFiltr(KCDMVec.get(i)));
									sqlVec.add(sql);	
									bjcopyVec.remove(k);
								}
								bjcopyVec.clear();
							}
							//=====================================
							
							/*for(int k=0;k<bjfzVec.size();k++){
								//System.out.println(bjfzVec.get(k)+"===="+classid);
								if(!bjfzVec.get(k).equals(classid)){
									id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
									sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
											"'" + MyTools.fixSql(id) + "'," +
											"'" + MyTools.fixSql(this.getZK_KSBH()) + "'," +
											"'" + bjfzVec.get(k) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(i))) + "'," +
											"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
											"getDate(),'1')";
										addXkVec.add(id);
										addXkVec.add(MyTools.StrFiltr(KCDMVec.get(i)));
										sqlVec.add(sql);							
								}
							}*/
						}
								
							//=======================================
								
							/*sql = "update dbo.V_自设考试管理_考试学科信息表 " +
									"set 状态='1' " +
									"where 考试学科编号='" + MyTools.fixSql(id) + "'";
							updateXkVec.add(id);
							sqlVec.add(sql);*/
						/*}else{
							for(int r=0;r<bjfzVec.size();r++){
								System.out.println(bjfzVec.get(r)+"====="+classid);
								if(!bjfzVec.get(r).equals(classid)){
									id = db.getMaxID("dbo.V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
									sql = "insert into dbo.V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
											"'" + MyTools.fixSql(id) + "'," +
											"'" + MyTools.fixSql(this.getZK_KSBH()) + "'," +
											"'" + bjfzVec.get(r) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(i))) + "'," +
											"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
											"getDate(),'1')";
										addXkVec.add(id);
										addXkVec.add(MyTools.StrFiltr(KCDMVec.get(i)));
										sqlVec.add(sql);
								}
							}
						}*/
					}
				}
			}
			/*}else{
				for(int i=0;i<kcdmVec.size();i++){
					xkArray[i].equals(kcdmVec.get(i).toString()) ;
				}
				System.out.println(xkArray);
			}*/
		}
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			sqlVec.clear();
			
			//处理学生成绩信息
			for(int i=0;i<bjVec.size();i++){
				sql="update a set a.状态='0' from V_自设考试管理_学生成绩信息表 as a left join V_自设考试管理_考试学科信息表 as b on a.考试学科编号=b.考试学科编号 " +
						"where b.考试编号='" + MyTools.fixSql(this.getZK_KSBH()) + "' and b.班级代码 = '" + bjVec.get(i) + "'";
				if (!"all".equalsIgnoreCase(plkcInfo) && !"".equalsIgnoreCase(plkcInfo)) {
					sql += " and b.课程代码 in ('" + MyTools.fixSql(plkcInfo).replaceAll(",", "','") + "') ";
				}else{
					sql += " and b.课程代码 in (" + kcdmString + ") ";
				}
				sqlVec.add(sql);
			}
			
			for(int i=0; i<updateXkVec.size(); i++){
				sql = "update V_自设考试管理_学生成绩信息表  set 状态='1' where 考试学科编号='" + MyTools.fixSql(MyTools.StrFiltr(updateXkVec.get(i))) + "'";
				sqlVec.add(sql);
			}
			int k=-1;
			for(int i=0; i<addXkVec.size(); i+=2){
				k++;
				if(bjVec.size()==vec.size()/3){
					for(int f=0;f<bjVec.size();f++){
						bjcopyVec.add(bjVec.get(f));
					}
				}else{
					for(int f=0;f<bjVec.size();f++){
						bjcopyVec.add(bjVec.get(f));
					}
					for(int f=0;f<vec.size();f++){
						bjcopyVec.remove(vec.get(f));
					}
				}
				
				if(k==bjcopyVec.size()){
					k=0;
				}
				for(int j=k;j<bjcopyVec.size();){
					sql = "insert into V_自设考试管理_学生成绩信息表 (学号,姓名,考试学科编号,创建人,创建时间,状态) " +
						"select a.学号,a.姓名,'" + MyTools.fixSql(MyTools.StrFiltr(addXkVec.get(i))) + "' as 考试学科编号,'" + MyTools.fixSql(this.getUSERCODE()) + "',GETDATE(),'1' "+
						"from V_学生基本数据子类 as a " +
						"where a.学生状态 in ('01','05') and a.行政班代码='" + bjcopyVec.get(j) +"' ";
					if(!this.getZK_ZSLB().equalsIgnoreCase("all")){
						sql+="' and a.招生类别 in ('"+ MyTools.fixSql(this.getZK_ZSLB()).replaceAll(",", "','") +"') ";
					}
									
					sql+=" order by a.班内学号 ";
					sqlVec.add(sql);
					break;
				}
			}
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}else{
			this.setMSG("保存失败");
		}
	}
	
	//招生类别下拉框赋值
	public Vector loadZSLBCombo() throws SQLException {
		Vector vec = null;
		String sql = "";
			
		sql = "select '全部' as comboName,'all' as comboValue " +
			"union all " +
			"select a.类别名称 as comboName , a.描述 as comboValue " +
			"from V_信息类别_类别操作 a " +
			"where a.父类别代码='ZSLBM' ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取新建编辑页面里的考试类别下拉框
	 * @date:2017-08-31
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	/*public Vector loadLbXJBJCombo() throws SQLException{
		Vector vec = null;
		
		String sql="select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 类别名称 , 描述  from  V_信息类别_类别操作 where 父类别代码='KSLBDM' and 描述!='01' " +
				"order by comboValue";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}*/
	
	/**
	 * 获取一共有几个招生类型
	 * @date:2017-09-01
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void loadLbXJBJCombo() throws SQLException{
		Vector vec = null;
		
		String sql="select COUNT(*) from  V_信息类别_类别操作 a where a.父类别代码='ZSLBM'  ";
		
		vec = db.GetContextVector(sql);
		this.setMSG(MyTools.StrFiltr(vec.get(0)));
	}
	
	public String getAUTHCODE() {
		return AUTHCODE;
	}

	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
	}

	public String getZK_KSBH() {
		return ZK_KSBH;
	}

	public void setZK_KSBH(String zK_KSBH) {
		ZK_KSBH = zK_KSBH;
	}

	public String getZK_KSMC() {
		return ZK_KSMC;
	}

	public void setZK_KSMC(String zK_KSMC) {
		ZK_KSMC = zK_KSMC;
	}

	public String getZK_XNXQBM() {
		return ZK_XNXQBM;
	}

	public void setZK_XNXQBM(String zK_XNXQBM) {
		ZK_XNXQBM = zK_XNXQBM;
	}

	public String getZK_LBBH() {
		return ZK_LBBH;
	}

	public void setZK_LBBH(String zK_LBBH) {
		ZK_LBBH = zK_LBBH;
	}

	public String getZK_CJR() {
		return ZK_CJR;
	}

	public void setZK_CJR(String zK_CJR) {
		ZK_CJR = zK_CJR;
	}

	public String getZK_CJSJ() {
		return ZK_CJSJ;
	}

	public void setZK_CJSJ(String zK_CJSJ) {
		ZK_CJSJ = zK_CJSJ;
	}

	public String getZK_ZT() {
		return ZK_ZT;
	}

	public void setZK_ZT(String zK_ZT) {
		ZK_ZT = zK_ZT;
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

	public String getZK_XN() {
		return ZK_XN;
	}

	public void setZK_XN(String zK_XN) {
		ZK_XN = zK_XN;
	}

	public String getZK_XQ() {
		return ZK_XQ;
	}

	public void setZK_XQ(String zK_XQ) {
		ZK_XQ = zK_XQ;
	}

	public String getZK_BJBH() {
		return ZK_BJBH;
	}

	public void setZK_BJBH(String zK_BJBH) {
		ZK_BJBH = zK_BJBH;
	}

	public String getZK_KSLB() {
		return ZK_KSLB;
	}

	public void setZK_KSLB(String zK_KSLB) {
		ZK_KSLB = zK_KSLB;
	}

	public String getZK_ZSLB() {
		return ZK_ZSLB;
	}

	public void setZK_ZSLB(String zK_ZSLB) {
		ZK_ZSLB = zK_ZSLB;
	}

	public String getZK_DJKXSLB() {
		return ZK_DJKXSLB;
	}

	public void setZK_DJKXSLB(String zK_DJKXSLB) {
		ZK_DJKXSLB = zK_DJKXSLB;
	}

	public String getZK_DFKSSJ() {
		return ZK_DFKSSJ;
	}

	public void setZK_DFKSSJ(String zK_DFKSSJ) {
		ZK_DFKSSJ = zK_DFKSSJ;
	}


	public String getZK_DFJSSJ() {
		return ZK_DFJSSJ;
	}

	public void setZK_DFJSSJ(String zK_DFJSSJ) {
		ZK_DFJSSJ = zK_DFJSSJ;
	}
}