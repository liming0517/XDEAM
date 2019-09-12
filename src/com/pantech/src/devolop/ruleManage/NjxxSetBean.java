package com.pantech.src.devolop.ruleManage;
/*
@date 2016.01.08
@author yeq
模块：M1.8年级设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class NjxxSetBean {
	private String USERCODE;//用户编号
	private String NJDM;//年级代码
	private String NJMC;//年级名称
	private String SSNF;//所属年份
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public NjxxSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2016-01-08
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		NJDM = "";//年级代码
		NJMC = "";//年级名称
		SSNF = "";//所属年份
		MSG = "";    //提示信息
	}
	
	/**
	 * 分页查询 年级列表
	 * @date:2016-01-08
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @param NJDM_CX 年级代码
	 * @param NJMC_CX 年级名称
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String NJDM_CX, String NJMC_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select 年级代码 as NJDM,年级名称 as NJMC,所属年份 as SSNF from V_学校年级数据子类 where 1=1";
		
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and 年级代码 like '%" + MyTools.fixSql(NJDM_CX) + "%'";
		}
		System.out.println("fixSql == "+MyTools.fixSql(NJDM_CX));
		if(!"".equalsIgnoreCase(NJMC_CX)){
			sql += " and 年级名称 like '%" + MyTools.fixSql(NJMC_CX) + "%'";
		}
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
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
		
		sql = "select count(*) from V_学校年级数据子类 where 年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
		System.out.println("年级代码="+this.getNJDM()+"年级名称="+this.getNJMC());
		//判断数据是否存在
		if(db.getResultFromDB(sql)){
			this.setMSG("当前填写的专业代码已存在");
		}else{
			sql = "select count(*) from V_学校年级数据子类 where 年级名称='" + MyTools.fixSql(this.getNJMC()) + "'";
			//判断数据是否存在
			if(db.getResultFromDB(sql)){
				this.setMSG("当前填写的专业名称已存在");
			}else{
				sql = "insert into V_学校年级数据子类 (年级代码,年级名称,所属年份,年级状态) values (" +
					"'" + MyTools.fixSql(this.getNJDM()) + "'," +
					"'" + MyTools.fixSql(this.getNJMC()) + "'," +
					"'" + MyTools.fixSql(this.getSSNF()) + "'," +
					"'1')";
				
				if(db.executeInsertOrUpdate(sql)){
					this.setMSG("保存成功");
				}else{
					this.setMSG("保存失败");
				}
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
		
		sql = "select count(*) from V_学校年级数据子类 where 年级名称='" + MyTools.fixSql(this.getNJMC()) + "'";
		//判断数据是否存在
		if(db.getResultFromDB(sql)){
			this.setMSG("当前填写的专业名称已存在");
		}else{
			sql = "update V_学校年级数据子类 set " +
				"年级名称='" + MyTools.fixSql(this.getNJMC()) + "'," +
				"所属年份='" + MyTools.fixSql(this.getSSNF()) + "' " +
				"where 年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
				
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
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
		sql = "select count(*) from V_学校班级数据子类 where 年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("当前选择的年级已经与班级关联，无法删除！");
		}else{
			sql = "delete from V_学校年级数据子类 where 年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
			
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

	public String getNJDM() {
		return NJDM;
	}

	public void setNJDM(String nJDM) {
		NJDM = nJDM;
	}

	public String getNJMC() {
		return NJMC;
	}

	public void setNJMC(String nJMC) {
		NJMC = nJMC;
	}

	public String getSSNF() {
		return SSNF;
	}

	public void setSSNF(String sSNF) {
		SSNF = sSNF;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}