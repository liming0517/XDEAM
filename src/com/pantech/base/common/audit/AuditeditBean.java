package com.pantech.base.common.audit;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class AuditeditBean {
	protected HttpServletRequest request;
	
	private String EDITIONS;
	private String MODEID; //模块编号
	
	private String ID; //编号
	private String AUDITID; //审核编号
	private String IDTYPE; //编号类型
	private String CONDITION; //步骤
	private String ROLENAME; //角色名称
	private String EMPLOYEENAME; //人员名称	
	private String AUDITNAME; //审核名称
	private String FIELDNAME; //字段名称
	//private String IDname;
	private String AUDITIDS; //审核编号
	private String ACTIVENUM;
	private String SPID; //特殊编号
	private String SPFIELD; //特殊字段
	private String TERMID; //条件编号
	private String TERM; //条件
	private String VALUES; //数值
	
	private String TGID; //通过编号
	private String TYPE;

	private String TGNAMEID; //通过人员名称
	private String TGNAME; //通过人员名称
	
	private String Module; //通过编号
	private String VIEW; //通过人员名称
	private String VIEWIKEY; //通过人员名称
	public AuditeditBean(HttpServletRequest req) {
		this.request = req;
		this.InitialData(); // 初始化参数
	}
	
	public void InitialData() {
		EDITIONS="";
		MODEID="";
		ID="";
		IDTYPE="";
		CONDITION="";
		ROLENAME="";
		EMPLOYEENAME="";
		AUDITNAME="";
		FIELDNAME="";
		AUDITIDS=""; //审核编号
		ACTIVENUM="";
		SPID=""; //特殊编号
		SPFIELD=""; //特殊字段
		TERMID=""; //条件编号
		TERM=""; //条件
		VALUES=""; //数值
		TGID="";
		TGNAMEID="";
		TGNAME="";
		TYPE="";
		Module=""; //通过编号
		VIEW=""; //通过人员名称
		VIEWIKEY=""; //通过人员名称
	}
	
	public void add() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		String sql="";
		int curaudit=this.curaudit();
		int edition=0;
		if("".equals(this.getEDITIONS())){
			edition=this.edition();
			this.setEDITIONS(MyTools.parseString(edition));
		}else{
			edition=MyTools.StringToInt(this.getEDITIONS());
		}
		sql = "INSERT INTO  V_审核标签配置(模块编号,审核编号,编号,编号类型,"+
		"步骤,角色名称,审核名称,字段名称,版本号,审核类型,人员名称) VALUES("+
		"'" + MyTools.fixSql(this.getMODEID())+ "'," + 
		"'" + edition+"*"+MODEID+"_"+curaudit + "',"+
		"'" + MyTools.fixSql(ID)+ "'," + 
		"'" + MyTools.fixSql(IDTYPE)+ "'," + 
		"'" + MyTools.fixSql(CONDITION)+ "',"+ 
		"'" + MyTools.fixSql(ROLENAME)+ "',"+ 
		"'" + MyTools.fixSql(AUDITNAME)+ "',"+ 
		"'" + MyTools.fixSql(FIELDNAME)+ "',"+ 
		"'" + edition+ "',"+ 
		"'" +  MyTools.fixSql(TYPE)+ "',"+ 
		"'" + MyTools.fixSql(EMPLOYEENAME)+ "')"; 
		db.executeInsertOrUpdate(sql);
	}
	
	public void edit() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		String sql="";
		sql = "UPDATE V_审核标签配置  set " +
		  //"审核方式='" + MyTools.fixSql(this.getWAY())+"'," + 
		  //"重复提交='" + MyTools.fixSql(this.getREPEAT())+"'," + 
		  "编号='" + MyTools.fixSql(ID)+"'," + 
		  "编号类型='" + MyTools.fixSql(IDTYPE)+"'," + 
		  "步骤='" + MyTools.fixSql(CONDITION)+"',"+
		  "角色名称='"+MyTools.fixSql(ROLENAME)+ "',"+ 
		  "人员名称='"+MyTools.fixSql(EMPLOYEENAME)+ "', "+ 
		  "审核名称='"+MyTools.fixSql(AUDITNAME)+ "', "+ 
		  "字段名称='"+MyTools.fixSql(FIELDNAME)+ "', "+ 
		  "审核类型='"+MyTools.fixSql(TYPE)+ "' "+ 
		  "where 审核编号='"+MyTools.fixSql(AUDITID)+"'";
		db.executeInsertOrUpdate(sql);
	}
	
	
	public void spadd() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		String sql="";
		int curaudit=this.curaudit();
		int edition=0;
		int spauditids=this.curspaudit();
		sql = "INSERT INTO  V_审核前置条件配置(审核编号,所需激活编号,特殊编号,特殊字段,"+
		"条件编号,条件,数值) VALUES("+
		"'" + MyTools.fixSql(this.getAUDITIDS())+ "'," + 
		"'" + MyTools.fixSql(ACTIVENUM)+ "'," + 
		"'" + AUDITIDS+"_"+spauditids + "',"+
		"'" + MyTools.fixSql(SPFIELD)+ "'," + 
		"'" + MyTools.fixSql(TERMID)+ "'," + 
		"'" + MyTools.fixSql(TERM)+ "',"+ 
		"'" + MyTools.fixSql(VALUES)+ "')"; 
		db.executeInsertOrUpdate(sql);
	}
	
	public void spedit() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		String sql="";
		sql = "UPDATE V_审核前置条件配置  set " +
		  "特殊字段='" + MyTools.fixSql(SPFIELD)+"'," + 
		  "所需激活编号='"+ MyTools.fixSql(ACTIVENUM)+"'," + 
		  "条件编号='" + MyTools.fixSql(TERMID)+"',"+
		  "条件='"+MyTools.fixSql(TERM)+ "',"+ 
		  "数值='"+MyTools.fixSql(VALUES)+ "' "+ 
		  "where 特殊编号='"+MyTools.fixSql(SPID)+"'";
		db.executeInsertOrUpdate(sql);
	}
	
	public void tgadd() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		String sql="";
		int curaudit=this.curaudit();
		int edition=0;
		int curtgaudit=this.curtgaudit();
		sql = "INSERT INTO  V_审核通过条件配置(审核编号,通过编号,通过人员编号,"+
		"通过人员名称) VALUES("+
		"'" + MyTools.fixSql(this.getAUDITIDS())+ "'," + 
		"'" + AUDITIDS+"_"+curtgaudit + "',"+
		"'" + MyTools.fixSql(TGNAMEID)+ "'," + 
		"'" + MyTools.fixSql(TGNAME)+ "')"; 
		db.executeInsertOrUpdate(sql);
	}
	
	public void tgedit() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		String sql="";
		sql = "UPDATE V_审核通过条件配置  set " +
		  "通过人员编号='" + MyTools.fixSql(TGNAMEID)+"'," + 
		  "通过人员名称='" + MyTools.fixSql(TGNAME)+"' "+
		  "where 通过编号='"+MyTools.fixSql(TGID)+"'";
		db.executeInsertOrUpdate(sql);
	}
	
	public int curtgaudit() throws SQLException{
		int curtgaudit;
		DBSource db = new DBSource(request);
		String sql="";
		Vector vec = null;
		sql="select max(convert(int,SubString(通过编号,CHARINDEX('_',通过编号,CHARINDEX('_',通过编号)+1)+1," +
			"len(通过编号)))) as 审核 from V_审核通过条件配置 where 审核编号='"+this.getAUDITIDS()+"'";
		vec = db.GetContextVector(sql);
		if (vec != null && vec.size() > 0) {
			curtgaudit=MyTools.StringToInt((vec.get(0).toString()))+1;
		}else{
			curtgaudit=1;
		}
		return curtgaudit;
	}
	
	public int curaudit() throws SQLException{
		int curaudit;
		DBSource db = new DBSource(request);
		String sql="";
		Vector vec = null;
		sql="select max(convert(int,SubString(审核编号,1,case 0 when CHARINDEX('_',审核编号) then 1 else CHARINDEX('_',审核编号) end))) from "+
			"(select SubString(审核编号,CHARINDEX('_',审核编号)+1,len(审核编号)) as 审核编号 " +
			"from V_审核标签配置 where 模块编号='"+MyTools.fixSql(this.getMODEID())+"' and 版本号='"+this.getEDITIONS()+"') as a";
		vec = db.GetContextVector(sql);
		if (vec != null && vec.size() > 0) {
			curaudit=MyTools.StringToInt((vec.get(0).toString()))+1;
		}else{
			curaudit=1;
		}
		return curaudit;
	}
	
	public int curspaudit() throws SQLException{
		int curspaudit;
		DBSource db = new DBSource(request);
		String sql="";
		Vector vec = null;
		sql="select max(convert(int,SubString(特殊编号,CHARINDEX('_',特殊编号,CHARINDEX('_',特殊编号)+1)+1," +
			"len(特殊编号)))) as 审核 from V_审核前置条件配置 where 审核编号='"+this.getAUDITIDS()+"'";
		vec = db.GetContextVector(sql);
		if (vec != null && vec.size() > 0) {
			curspaudit=MyTools.StringToInt((vec.get(0).toString()))+1;
		}else{
			curspaudit=1;
		}
		return curspaudit;
	}
	
	public int edition() throws SQLException{//版本号
		int edition;
		DBSource db = new DBSource(request);
		String sql="";
		Vector vec = null;
		sql="select max(convert(int,版本号))+1 as 版本号  " +
			"from V_审核标签配置 where 模块编号='"+MyTools.fixSql(this.getMODEID())+"'";
		vec = db.GetContextVector(sql);
		edition=MyTools.StringToInt((vec.get(0).toString()));
		return edition;
	}
	
	public void savedefmod() throws WrongSQLException, SQLException{
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		sql="";
		sql="insert V_模块初始化定义(模块编号,模块名称,相关视图,审核地址,主键名称) " +
			"select distinct 模块编号,节点名 as 模块名称,'"+ 
			MyTools.fixSql(VIEW)+"' as 相关视图,url as 审核地址,'"+ 
			MyTools.fixSql(VIEWIKEY)+"' as 主键名称 from V_Node_AuthModule " +
			"where 模块编号='"+ 
			MyTools.fixSql(Module)+"'";
		db.executeInsertOrUpdate(sql);
		//Vector vec = null; // 结果集
		//Vector<String> vec1 = new Vector<String>(); // 结果集

	}
	
	public void editdefmod() throws WrongSQLException, SQLException{
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		//Vector vec = null; // 结果集
		sql="update V_模块初始化定义 set " +
			"相关视图='"+MyTools.fixSql(VIEW)+"',"+
			"主键名称='"+MyTools.fixSql(VIEWIKEY)+"' where 模块编号='"+
			MyTools.fixSql(Module)+"'";
		db.executeInsertOrUpdate(sql);
		//Vector<String> vec1 = new Vector<String>(); // 结果集

	}
	public void setEDITIONS(String eDITIONS) {
		EDITIONS = eDITIONS;
	}

	public String getEDITIONS() {
		return EDITIONS;
	}

	public void setMODEID(String mODEID) {
		MODEID = mODEID;
	}

	public String getMODEID() {
		return MODEID;
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	public String getIDTYPE() {
		return IDTYPE;
	}

	public void setIDTYPE(String idtype) {
		IDTYPE = idtype;
	}

	public String getCONDITION() {
		return CONDITION;
	}

	public void setCONDITION(String condition) {
		CONDITION = condition;
	}

	public String getROLENAME() {
		return ROLENAME;
	}

	public void setROLENAME(String rolename) {
		ROLENAME = rolename;
	}

	public String getEMPLOYEENAME() {
		return EMPLOYEENAME;
	}

	public void setEMPLOYEENAME(String employeename) {
		EMPLOYEENAME = employeename;
	}

	public String getAUDITNAME() {
		return AUDITNAME;
	}

	public void setAUDITNAME(String auditname) {
		AUDITNAME = auditname;
	}

	public String getFIELDNAME() {
		return FIELDNAME;
	}

	public void setFIELDNAME(String fieldname) {
		FIELDNAME = fieldname;
	}

	public void setAUDITID(String aUDITID) {
		AUDITID = aUDITID;
	}

	public String getAUDITID() {
		return AUDITID;
	}

	public String getAUDITIDS() {
		return AUDITIDS;
	}

	public void setAUDITIDS(String auditids) {
		AUDITIDS = auditids;
	}

	public String getACTIVENUM() {
		return ACTIVENUM;
	}

	public void setACTIVENUM(String activenum) {
		ACTIVENUM = activenum;
	}

	public String getSPID() {
		return SPID;
	}

	public void setSPID(String spid) {
		SPID = spid;
	}

	public String getSPFIELD() {
		return SPFIELD;
	}

	public void setSPFIELD(String spfield) {
		SPFIELD = spfield;
	}

	public String getTERMID() {
		return TERMID;
	}

	public void setTERMID(String termid) {
		TERMID = termid;
	}

	public String getTERM() {
		return TERM;
	}

	public void setTERM(String term) {
		TERM = term;
	}

	public String getVALUES() {
		return VALUES;
	}

	public void setVALUES(String values) {
		VALUES = values;
	}

	public String getTGID() {
		return TGID;
	}

	public void setTGID(String tgid) {
		TGID = tgid;
	}

	public String getTGNAMEID() {
		return TGNAMEID;
	}

	public void setTGNAMEID(String tgnameid) {
		TGNAMEID = tgnameid;
	}

	public String getTGNAME() {
		return TGNAME;
	}

	public void setTGNAME(String tgname) {
		TGNAME = tgname;
	}
	
	public String getModule() {
		return Module;
	}

	public void setModule(String module) {
		Module = module;
	}

	public String getVIEW() {
		return VIEW;
	}

	public void setVIEW(String vIEW) {
		VIEW = vIEW;
	}

	public String getVIEWIKEY() {
		return VIEWIKEY;
	}

	public void setVIEWIKEY(String vIEWIKEY) {
		VIEWIKEY = vIEWIKEY;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
}
