package com.pantech.src.devolop.ruleManage;
/*
@date 2016.01.08
@author yeq
模块：M1.9专业设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class MajorSetBean {
	private String USERCODE;//用户编号
	private String ZYDM;//专业代码
	private String ZYMC;//专业名称
	private String KMLX;//科目类型
	private String XZ;//学制
	private String ZYZZ;//专业组长
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public MajorSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-15
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		ZYDM = "";//专业代码
		ZYMC = "";//专业名称
		KMLX = "";//科目类型
		XZ = "";//学制
		MSG = "";    //提示信息
		ZYZZ="";//专业组长
	}
	
	/**
	 * 分页查询 专业列表
	 * @date:2016-01-08
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @param ZYDM_CX 专业代码
	 * @param ZYMC_CX 专业名称
	 * @param KMLX_CX 科目类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String ZYDM_CX, String ZYMC_CX, String KMLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String JN_JYZZZBH1=MyTools.StrFiltr(request.getParameter("JN_JYZZZBH1"));
		sql = "select a.专业代码 as ZYDM,a.专业名称 as ZYMC,b.科目类型 as KMLX,replace(a.专业组长编号,'@','') as ZYZZ,a.学制 as XZ from V_专业基本信息数据子类 a " +
			"inner join V_基础信息_专业科目类型信息表 b on b.专业代码=a.专业代码 where len(a.专业代码)>3";
		
		if(!"".equalsIgnoreCase(ZYDM_CX)){
			sql += " and a.专业代码 like '%" + MyTools.fixSql(ZYDM_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(ZYMC_CX)){
			sql += " and a.专业名称 like '%" + MyTools.fixSql(ZYMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KMLX_CX)){
			sql += " and b.科目类型='" + MyTools.fixSql(KMLX_CX) + "'";
		}
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	/**
	 * 读取教师下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJYZzzCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName union all select 工号 as comboValue,姓名 as comboName from V_教职工基本数据子类  order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 新建方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws SQLException
	 */
	public void addRec() throws SQLException {		 
		String sql = "";
		Vector sqlVec = new Vector();
		String zzxm=MyTools.StrFiltr(request.getParameter("JN_JYZZZBH"));
		String JN_JYZZZBH1=MyTools.StrFiltr(request.getParameter("JN_JYZZZBH1"));
		sql = "select count(*) from V_专业基本信息数据子类 where 专业代码='" + MyTools.fixSql(this.getZYDM()) + "'";
			//判断数据是否存在
		if(db.getResultFromDB(sql)){
			this.setMSG("当前填写的专业代码已存在");
		}else{
			
				sql = "insert into V_专业基本信息数据子类 (专业代码,专业名称,学制,专业组长编号,专业组长姓名,状态) values (" +
						"'" + MyTools.fixSql(this.getZYDM()) + "'," +
						"'" + MyTools.fixSql(this.getZYMC()) + "'," +
						"'" + MyTools.fixSql(this.getXZ()) + "'," +
						"'@" + MyTools.fixSql(JN_JYZZZBH1.replaceAll(",", "@,@")) + "@'," +
						"'@" + MyTools.fixSql(zzxm.replaceAll(",", "@,@")) + "@'," +
						"'1')";
					sqlVec.add(sql);
			sql = "insert into V_基础信息_专业科目类型信息表 (专业代码,科目类型,创建人,创建时间,状态) values (" +
				"'" + MyTools.fixSql(this.getZYDM()) + "'," +
				"'" + MyTools.fixSql(this.getKMLX()) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
			sqlVec.add(sql);
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 编辑方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws SQLException
	 */
	public void modRec() throws SQLException {		 
		String sql = "";
		Vector sqlVec = new Vector();
		String JN_JYZZZBH1=MyTools.StrFiltr(request.getParameter("JN_JYZZZBH1"));
		String zyzz=MyTools.StrFiltr(request.getParameter("JN_JYZZZBH"));
		
			sql = "update V_专业基本信息数据子类 set " +
					"专业名称='" + MyTools.fixSql(this.getZYMC()) + "'," +
					"学制='" + MyTools.fixSql(this.getXZ()) + "'," +
					"专业组长编号='@" + MyTools.fixSql(JN_JYZZZBH1.replaceAll(",", "@,@")) + "@'," +
					"专业组长姓名='@" + MyTools.fixSql(zyzz.replaceAll(",", "@,@")) + "@' " +
					"where 专业代码='" + MyTools.fixSql(this.getZYDM()) + "'";
				sqlVec.add(sql);
		
		sql = "update V_基础信息_专业科目类型信息表 set " +
			"科目类型='" + MyTools.fixSql(this.getKMLX()) + "' " +
			"where 专业代码='" + MyTools.fixSql(this.getZYDM()) + "'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws SQLException
	 */
	public void delRec() throws SQLException {		 
		String sql = "";
		
		//检查当前要删除的专业是否已班级关联
		sql = "select count(*) from V_学校班级数据子类 where 专业代码='" + MyTools.fixSql(this.getZYDM()) + "'";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("当前选择的专业已经与班级关联，无法删除！");
		}else{
			sql = "delete from V_专业基本信息数据子类 where 专业代码='" + MyTools.fixSql(this.getZYDM()) + "'";
			
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("删除成功");
			}else{
				this.setMSG("删除失败");
			}
		}
	}
	
	//转换含有html的字符串
//	public String changeString(String str){
//		str = str.replaceAll("<","&lt;");
//		str = str.replaceAll(">","&gt;");
//		str = str.replaceAll(" ","&nbsp;");
//		str = str.replaceAll("&","&amp;");
//		str = str.replaceAll("'","&apos;");
//		str = str.replaceAll("\"","&quot;");
//		return str;
//	}
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getZYDM() {
		return ZYDM;
	}

	public void setZYDM(String zYDM) {
		ZYDM = zYDM;
	}

	public String getZYMC() {
		return ZYMC;
	}

	public void setZYMC(String zYMC) {
		ZYMC = zYMC;
	}

	public String getKMLX() {
		return KMLX;
	}

	public void setKMLX(String kMLX) {
		KMLX = kMLX;
	}

	public String getXZ() {
		return XZ;
	}

	public void setXZ(String xZ) {
		XZ = xZ;
	}
	public String getZYZZ() {
		return ZYZZ;
	}

	public void setZYZZ(String zYZZ) {
		ZYZZ = zYZZ;
	}
	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}