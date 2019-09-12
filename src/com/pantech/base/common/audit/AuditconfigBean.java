package com.pantech.base.common.audit;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;


import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class AuditconfigBean {

	protected HttpServletRequest request;
	/*  
	 * 审核配置部分变量
	 */
	private String MODEID;  //模块编号
	private String WAY; //审核方式 
	private String REPEAT; //重复提交
	private String deleteauditid;//需要的删除审核id
	private String modename;  
	private String EDITIONS;
	private String REMIND;
	private String STARTDATE;
	private String TYPE;
	private String LEAP;
	private String REJECT;
	private boolean auditlist=false;//判断是否进行对datagrid数据写入数据库
	
	private String[] AUDITID; //审核编号
	private String[] ID; //编号
	private String[] IDTYPE; //编号类型
	private String[] CONDITION; //步骤
	private String[] ROLENAME; //角色名称
	private String[] EMPLOYEENAME; //人员名称	
	private String[] AUDITNAME; //审核名称
	private String[] FIELDNAME; //字段名称
	/*
	 * 审核前置条件配置部分变量
	 */
	private String deletespauditid;//需要的删除审核id
	private boolean preauditlist=false;//判断是否进行对datagrid数据写入数据库
	
	private String[] ACTIVENUM;
	private String[] SPID; //特殊编号
	private String[] SPFIELD; //特殊字段
	private String[] TERMID; //条件编号
	private String[] TERM; //条件
	private String[] VALUES; //数值
	/*
	 * 审核通过条件配置部分变量
	 */
	private String deletetgauditid;//需要的删除审核id
	private boolean passauditlist=false;//判断是否进行对datagrid数据写入数据库
	
	private String[] TGID; //通过编号
	private String[] TGNAMEID; //通过人员名称
	private String[] TGNAME; //通过人员名称
	/*
	 * 审核模块配置部分变量
	 */
	private String Module; //通过编号
	private String VIEW; //通过人员名称
	private String VIEWIKEY; //通过人员名称
	private boolean modulelist=false;
	/*
	 * 公共变量
	 */
	private String MSG;//输出信息
	private String AUDITIDS;
	
	
	public AuditconfigBean(HttpServletRequest req) {
		this.request = req;
		this.InitialData(); // 初始化参数
	}

	/**
	 * @author mayue 初始化变量
	 */
	public void InitialData() {
		MODEID=""; //模块编号
		WAY=""; //审核方式
		REPEAT=""; //重复提交	
		deleteauditid="";
		modename="";
		AUDITIDS="";
		EDITIONS="";
		REMIND="";
		STARTDATE="";
		LEAP="";
		TYPE="";
		REJECT="";
	}

	
	public Vector queauditRec(int pageNum, int rows) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 审核编号,编号,编号类型,步骤,人员名称+字段名称 +角色名称  as 审核角色,审核名称,审核类型 " +
			  "from V_审核标签配置  where 模块编号='"+this.getMODEID()+"' and 版本号='"+this.getEDITIONS()+"'";
		vec = db.getConttexJONSArr(sql, pageNum, rows);
		return vec;
	}
	
	public Vector quemodeRec(int pageNum, int rows) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select distinct b.模块编号,a.模块名称,b.审核方式,b.重复提交,b.版本号,b.审核提醒,b.开启时间,b.是否跳步  from " +
			  "dbo.V_模块初始化定义 a join dbo.V_审核标签配置 b on a.模块编号=b.模块编号 " +
			  "where 1=1";
		if(!"".equals(this.getModename())){
			sql+=" and a.模块名称 like '%"+this.getModename()+"%'";
		}
		if(!"".equals(this.getWAY())){
			sql+=" and b.审核方式 like '%"+this.getWAY()+"%'";
		}
		if(!"".equals(this.getREPEAT())){
			sql+=" and b.重复提交 like '%"+this.getREPEAT()+"%'";
		}
		sql+=" union select distinct b.模块编号,a.模板名称 as 模块名称,b.审核方式,b.重复提交,b.版本号,b.审核提醒,b.开启时间,b.是否跳步  from " +
			"dbo.V_模版配置表 a join dbo.V_审核标签配置 b on a.模版编号=b.模块编号 " +
			"where 1=1";
		if(!"".equals(this.getModename())){
			sql+=" and a.模板名称 like '%"+this.getModename()+"%'";
		}
		if(!"".equals(this.getWAY())){
			sql+=" and b.审核方式 like '%"+this.getWAY()+"%'";
		}
		if(!"".equals(this.getREPEAT())){
			sql+=" and b.重复提交 like '%"+this.getREPEAT()+"%'";
		}
		vec = db.getConttexJONSArr(sql, 0, 0);

		//publicTools.printArray(bean.getAUDITID());	
		return vec;
	}
	
	public void saveAudit() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		//Vector vec = new Vector(); // 结果集
		String sql="";
		sql = "UPDATE V_审核标签配置  set " +
			  "审核方式='"+MyTools.fixSql(this.getWAY())+"',"+
			  "开启时间='"+MyTools.fixSql(this.getSTARTDATE())+"',"+
			  "审核提醒='"+MyTools.fixSql(this.getREMIND())+"',"+
			  "重复提交='"+MyTools.fixSql(this.getREPEAT())+"', "+
			  "是否跳步='"+MyTools.fixSql(this.getLEAP())+"', "+
			  "驳回='"+MyTools.fixSql(this.getREJECT())+"' "+
			  "where 模块编号='"+MyTools.fixSql(this.getMODEID())+"' and 版本号='"+this.getEDITIONS()+"'";
		//vec.add(sql);
		//MyTools.PrintVector(vec);
		db.executeInsertOrUpdate(sql);
		this.setMSG("保存成功");
	}
	/*
	public String addAudit(int i,String auditids,int edition){
		System.out.println("auditids==============="+auditids);
		String sql = ""; // 查询用SQL语句
		sql = "INSERT INTO  V_审核标签配置(模块编号,审核方式,重复提交,审核编号,编号,编号类型,"+
			"步骤,角色名称,审核名称,字段名称,版本号,人员名称) VALUES("+
			"'" + MyTools.fixSql(this.getMODEID())+ "'," + 
			"'" + MyTools.fixSql(this.getWAY())+ "'," + 
			"'" + MyTools.fixSql(this.getREPEAT())+ "'," + 
			"'" + auditids + "',"+
			//"'" + MyTools.fixSql(AUDITID[i])+ "'," + 
			"'" + MyTools.fixSql(ID[i])+ "'," + 
			"'" + MyTools.fixSql(IDTYPE[i])+ "'," + 
			"'" + MyTools.fixSql(CONDITION[i])+ "',"+ 
			"'" + MyTools.fixSql(ROLENAME[i])+ "',"+ 
			"'" + MyTools.fixSql(AUDITNAME[i])+ "',"+ 
			"'" + MyTools.fixSql(FIELDNAME[i])+ "',"+ 
			"'" + edition+ "',"+ 
			"'" + MyTools.fixSql(EMPLOYEENAME[i])+ "')"; 
		System.out.println("sql==============="+sql);
		return sql;
	}
	
	public String modAudit(int i){
		String sql = ""; // 查询用SQL语句
		sql = "UPDATE V_审核标签配置  set " +
			  //"审核方式='" + MyTools.fixSql(this.getWAY())+"'," + 
			  //"重复提交='" + MyTools.fixSql(this.getREPEAT())+"'," + 
			  "编号='" + MyTools.fixSql(ID[i])+"'," + 
			  "编号类型='" + MyTools.fixSql(IDTYPE[i])+"'," + 
			  "步骤='" + MyTools.fixSql(CONDITION[i])+"',"+
			  "角色名称='"+MyTools.fixSql(ROLENAME[i])+ "',"+ 
			  "人员名称='"+MyTools.fixSql(EMPLOYEENAME[i])+ "', "+ 
			  "审核名称='"+MyTools.fixSql(AUDITNAME[i])+ "', "+ 
			  "字段名称='"+MyTools.fixSql(FIELDNAME[i])+ "' "+ 
			  "where 审核编号='"+MyTools.fixSql(AUDITID[i])+"'";
		return sql;
	}
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
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
	*/
	public void deleteAudit() throws SQLException{
		DBSource db = new DBSource(request);
		Vector vec = new Vector(); // 结果集
		String sql="";
		/*
		 * 需要扩展删除关联表
		 */
		sql="delete V_审核前置条件配置 where 审核编号='"+this.getDeleteauditid()+"'";
		vec.add(sql);
		sql="delete V_审核通过条件配置 where 审核编号='"+this.getDeleteauditid()+"'";
		vec.add(sql);
		sql="delete V_审核标签配置 where 审核编号='"+this.getDeleteauditid()+"'";
		vec.add(sql);
		db.executeInsertOrUpdateTransaction(vec);
		this.setMSG("删除成功");
	}
	
	
	public void deleteMode() throws SQLException{
		DBSource db = new DBSource(request);
		Vector vec = new Vector(); // 结果集
		String sql="";
		/*
		 * 需要扩展删除关联表
		 */
		sql="delete V_审核前置条件配置 where 审核编号 in (select 审核编号 from  V_审核标签配置 where 模块编号='"+this.getMODEID()+"' and 版本号='"+this.getEDITIONS()+"')";
		vec.add(sql);
		sql="delete V_审核通过条件配置 where 审核编号 in (select 审核编号 from  V_审核标签配置 where 模块编号='"+this.getMODEID()+"' and 版本号='"+this.getEDITIONS()+"')";
		vec.add(sql);
		sql="delete V_审核标签配置 where 模块编号='"+this.getMODEID()+"' and 版本号='"+this.getEDITIONS()+"'";
		vec.add(sql);
		db.executeInsertOrUpdateTransaction(vec);
		this.setMSG("删除成功");
	}
	
	public Vector queauditspRec(int pageNum, int rows) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 审核编号,特殊编号,特殊字段,条件编号,条件,数值,所需激活编号  from V_审核前置条件配置 where 审核编号='"+this.getAUDITIDS()+"'";
		vec = db.getConttexJONSArr(sql, pageNum, rows);

		return vec;
	}
	/*
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
	
	
	public void savespAudit() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		Vector vec = new Vector(); // 结果集
		String sql="";
		int curspaudit=this.curspaudit();
		if(preauditlist){
			for(int i=0;i<this.getSPID().length;i++){
				System.out.println("SPID========="+SPID[i]);
				if("".equals(SPID[i])){
					System.out.println("curaudit==============="+curspaudit);
					vec.add(this.addspAudit(i,AUDITIDS+"_"+curspaudit));
					curspaudit++;
				}else{
					vec.add(this.modspAudit(i));
				}
			}
		}
		MyTools.PrintVector(vec);
		db.executeInsertOrUpdateTransaction(vec);
		this.setMSG("保存成功");
	}
	
	public String addspAudit(int i,String spauditids){
		System.out.println("auditids==============="+spauditids);
		String sql = ""; // 查询用SQL语句
		sql = "INSERT INTO  V_审核前置条件配置(审核编号,所需激活编号,特殊编号,特殊字段,"+
			"条件编号,条件,数值) VALUES("+
			"'" + MyTools.fixSql(this.getAUDITIDS())+ "'," + 
			"'" + MyTools.fixSql(ACTIVENUM[i])+ "'," + 
			"'" + spauditids + "',"+
			"'" + MyTools.fixSql(SPFIELD[i])+ "'," + 
			"'" + MyTools.fixSql(TERMID[i])+ "'," + 
			"'" + MyTools.fixSql(TERM[i])+ "',"+ 
			"'" + MyTools.fixSql(VALUES[i])+ "')"; 
		System.out.println("sql==============="+sql);
		return sql;
	}
	
	public String modspAudit(int i){
		String sql = ""; // 查询用SQL语句
		sql = "UPDATE V_审核前置条件配置  set " +
			  "特殊字段='" + MyTools.fixSql(SPFIELD[i])+"'," + 
			  "所需激活编号='"+ MyTools.fixSql(ACTIVENUM[i])+"'," + 
			  "条件编号='" + MyTools.fixSql(TERMID[i])+"',"+
			  "条件='"+MyTools.fixSql(TERM[i])+ "',"+ 
			  "数值='"+MyTools.fixSql(VALUES[i])+ "' "+ 
			  "where 特殊编号='"+MyTools.fixSql(SPID[i])+"'";
		return sql;
	}
	*/
	public void deletespAudit() throws SQLException{
		DBSource db = new DBSource(request);
		Vector vec = new Vector(); // 结果集
		String sql="";
		/*
		 * 需要扩展删除关联表
		 */
		sql="delete V_审核前置条件配置 where 特殊编号='"+this.getDeletespauditid()+"'";
		vec.add(sql);
		db.executeInsertOrUpdateTransaction(vec);
		this.setMSG("删除成功");
	}
	
	public Vector queaudittgRec(int pageNum, int rows) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 审核编号,通过编号,通过人员名称,通过人员编号  from V_审核通过条件配置 where 审核编号='"+this.getAUDITIDS()+"'";
		vec = db.getConttexJONSArr(sql, pageNum, rows);
		return vec;
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
	
	/*
	public void savetgAudit() throws SQLException{
		DBSource db = new DBSource(request);// 数据库对象
		Vector vec = new Vector(); // 结果集
		String sql="";
		int curtgaudit=this.curtgaudit();
		if(passauditlist){
			for(int i=0;i<this.getTGID().length;i++){
				System.out.println("TGID========="+TGID[i]);
				if("".equals(TGID[i])){
					System.out.println("curaudit==============="+curtgaudit);
					vec.add(this.addtgAudit(i,AUDITIDS+"_"+curtgaudit));
					curtgaudit++;
				}else{
					vec.add(this.modtgAudit(i));
				}
			}
		}
		MyTools.PrintVector(vec);
		db.executeInsertOrUpdateTransaction(vec);
		this.setMSG("保存成功");
	}
	
	public String addtgAudit(int i,String tgauditids){
		System.out.println("auditids==============="+tgauditids);
		String sql = ""; // 查询用SQL语句
		sql = "INSERT INTO  V_审核通过条件配置(审核编号,通过编号,通过人员编号,"+
			"通过人员名称) VALUES("+
			"'" + MyTools.fixSql(this.getAUDITIDS())+ "'," + 
			"'" + tgauditids + "',"+
			"'" + MyTools.fixSql(TGNAMEID[i])+ "'," + 
			"'" + MyTools.fixSql(TGNAME[i])+ "')"; 
		System.out.println("sql==============="+sql);
		return sql;
	}
	
	public String modtgAudit(int i){
		String sql = ""; // 查询用SQL语句
		sql = "UPDATE V_审核通过条件配置  set " +
			  "通过人员编号='" + MyTools.fixSql(TGNAMEID[i])+"'," + 
			  "通过人员名称='" + MyTools.fixSql(TGNAME[i])+"' "+
			  "where 通过编号='"+MyTools.fixSql(TGID[i])+"'";
		return sql;
	}
	*/
	
	public void deletetgAudit() throws SQLException{
		DBSource db = new DBSource(request);
		Vector vec = new Vector(); // 结果集
		String sql="";
		/*
		 * 需要扩展删除关联表
		 */
		sql="delete V_审核通过条件配置 where 通过编号='"+this.getDeletetgauditid()+"'";
		vec.add(sql);
		db.executeInsertOrUpdateTransaction(vec);
		this.setMSG("删除成功");
	}
	
	public Vector quedefRec(int pageNum, int rows) throws SQLException{
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 模块编号,模块名称,相关视图,主键名称  from V_模块初始化定义";
		vec = db.getConttexJONSArr(sql, pageNum, rows);
		return vec;
	}
	


	public void deletedefmod() throws SQLException{
		DBSource db = new DBSource(request);
		Vector vec = new Vector(); // 结果集
		String sql="";
		/*
		 * 需要扩展删除关联表
		 */
		sql="delete V_模块初始化定义 where 模块编号='"+this.getDeletetgauditid()+"'";
		db.executeInsertOrUpdate(sql);
		this.setMSG("删除成功");
	}
	
	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMODEID(String mODEID) {
		MODEID = mODEID;
	}

	public String getMODEID() {
		return MODEID;
	}
	
	public String getWAY() {
		return WAY;
	}

	public void setWAY(String way) {
		WAY = way;
	}

	public String getREPEAT() {
		return REPEAT;
	}

	public void setREPEAT(String repeat) {
		REPEAT = repeat;
	}

	public String[] getID() {
		return ID;
	}

	public void setID(String[] id) {
		ID = id;
	}

	public String[] getIDTYPE() {
		return IDTYPE;
	}

	public void setIDTYPE(String[] idtype) {
		IDTYPE = idtype;
	}

	public String[] getCONDITION() {
		return CONDITION;
	}

	public void setCONDITION(String[] condition) {
		CONDITION = condition;
	}

	public String[] getROLENAME() {
		return ROLENAME;
	}

	public void setROLENAME(String[] rolename) {
		ROLENAME = rolename;
	}

	public String[] getEMPLOYEENAME() {
		return EMPLOYEENAME;
	}

	public void setEMPLOYEENAME(String[] employeename) {
		EMPLOYEENAME = employeename;
	}

	public void setAUDITID(String aUDITID[]) {
		AUDITID = aUDITID;
	}

	public String[] getAUDITID() {
		return AUDITID;
	}

	public void setAuditlist(boolean auditlist) {
		this.auditlist = auditlist;
	}

	public boolean getAuditlist() {
		return auditlist;
	}

	public void setDeleteauditid(String deleteauditid) {
		this.deleteauditid = deleteauditid;
	}

	public String getDeleteauditid() {
		return deleteauditid;
	}

	public void setModename(String modename) {
		this.modename = modename;
	}

	public String getModename() {
		return modename;
	}

	public void setAUDITNAME(String[] aUDITNAME) {
		AUDITNAME = aUDITNAME;
	}

	public String[] getAUDITNAME() {
		return AUDITNAME;
	}

	public void setFIELDNAME(String[] fIELDNAME) {
		FIELDNAME = fIELDNAME;
	}

	public String[] getFIELDNAME() {
		return FIELDNAME;
	}

	public void setAUDITIDS(String aUDITIDS) {
		AUDITIDS = aUDITIDS;
	}

	public String getAUDITIDS() {
		return AUDITIDS;
	}
	public String[] getSPID() {
		return SPID;
	}

	public void setSPID(String[] spid) {
		SPID = spid;
	}

	public String[] getSPFIELD() {
		return SPFIELD;
	}

	public void setSPFIELD(String[] spfield) {
		SPFIELD = spfield;
	}

	public String[] getTERMID() {
		return TERMID;
	}

	public void setTERMID(String[] termid) {
		TERMID = termid;
	}

	public String[] getTERM() {
		return TERM;
	}

	public void setTERM(String[] term) {
		TERM = term;
	}

	public String[] getVALUES() {
		return VALUES;
	}

	public void setVALUES(String[] values) {
		VALUES = values;
	}

	public void setPreauditlist(boolean preauditlist) {
		this.preauditlist = preauditlist;
	}

	public boolean getPreauditlist() {
		return preauditlist;
	}

	public void setDeletespauditid(String deletespauditid) {
		this.deletespauditid = deletespauditid;
	}

	public String getDeletespauditid() {
		return deletespauditid;
	}
	
	public void setDeletetgauditid(String deletetgauditid) {
		this.deletetgauditid = deletetgauditid;
	}

	public String getDeletetgauditid() {
		return deletetgauditid;
	}
	
	public void setPassauditlist(boolean passauditlist) {
		this.passauditlist = passauditlist;
	}

	public boolean getPassauditlist() {
		return passauditlist;
	}
	
	public String[] getTGID() {
		return TGID;
	}
	public void setTGID(String[] tGID) {
		TGID = tGID;
	}
	public String[] getTGNAMEID() {
		return TGNAMEID;
	}
	public void setTGNAMEID(String[] tGNAMEID) {
		TGNAMEID = tGNAMEID;
	}
	public String[] getTGNAME() {
		return TGNAME;
	}
	public void setTGNAME(String[] tGNAME) {
		TGNAME = tGNAME;
	}

	public void setACTIVENUM(String[] aCTIVENUM) {
		ACTIVENUM = aCTIVENUM;
	}

	public String[] getACTIVENUM() {
		return ACTIVENUM;
	}

	public void setEDITIONS(String eDITIONS) {
		EDITIONS = eDITIONS;
	}

	public String getEDITIONS() {
		return EDITIONS;
	}

	public void setREMIND(String rEMIND) {
		REMIND = rEMIND;
	}

	public String getREMIND() {
		return REMIND;
	}

	public void setSTARTDATE(String sTARTDATE) {
		STARTDATE = sTARTDATE;
	}

	public String getSTARTDATE() {
		return STARTDATE;
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

	public void setVIEW(String view) {
		VIEW = view;
	}
 
	public String getVIEWIKEY() {
		return VIEWIKEY;
	}

	public void setVIEWIKEY(String viewikey) {
		VIEWIKEY = viewikey;
	}

	public boolean getModulelist() {
		return modulelist;
	}

	public void setModulelist(boolean modulelist) {
		this.modulelist = modulelist;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getLEAP() {
		return LEAP;
	}

	public void setLEAP(String lEAP) {
		LEAP = lEAP;
	}

	public String getREJECT() {
		return REJECT;
	}

	public void setREJECT(String rEJECT) {
		REJECT = rEJECT;
	}
}
