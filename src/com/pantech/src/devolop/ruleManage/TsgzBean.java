package com.pantech.src.devolop.ruleManage;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

/*
@date 2015.06.03
@author wangzh
模块：M1.3特殊规则设置
说明:
重要及特殊方法：
*/
public class TsgzBean {
	private String GT_XNXQBM; //学年学期编码
	private String GT_XNXQBM1; //学年学期编码
	private String GT_JSBH; //教师编号
	private String GT_JXXZ; //教学性质
	private String GT_JSBH1; //教师编号
	private String GT_JSXM; //教师姓名
	private String GT_MTCS; //每天次数
	private String GT_MZCS; //每周次数
	private String GT_MTJC; //每天节次
	private String GT_MZJC; //每周节次
	private String GT_ZDZJKCS; //最大执教课程数
	private String  GT_ZT; //状态
	private String GT_JS;//角色
	private HttpServletRequest request;
	private DBSource db;
	private String MSG; //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public TsgzBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-06-03
	 * @author:wangzh
	 */
	public void initialData() {
		GT_XNXQBM = ""; //学年学期编码
		GT_JSBH = ""; //教师编号
		GT_XNXQBM1 = ""; //学年学期编码
		GT_JSBH1 = ""; //教师编号
		GT_JXXZ = ""; //教学性质
		GT_JSXM = ""; //教师姓名
		GT_MTCS = ""; //每天次数
		GT_MZCS = ""; //每周次数
		GT_MTJC = ""; //每天节次
		GT_MZJC = ""; //每周节次
	    GT_ZDZJKCS = ""; //最大执教课程数
	    GT_ZT = ""; //状态
		MSG = ""; //提示信息
	}
	
	/**
	 * 分页查询 有效的特殊规则列表（状态为0则无效，1则有效）
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		String sql1 = ""; // 查询用SQL语句
		String sql2 = ""; // 查询用SQL语句
		String sql3 = ""; // 查询用SQL语句
		String sql4 = ""; // 查询用SQL语句
		Vector sqlVec = new Vector();
		String Xnxqbm = "";
		Vector vec = null; // 结果集
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = new Vector(); // 结果集
		
		/**
		 * 当前台页面的查询条件学年学期编码为空时，查询V_规则管理_学年学期表中学年学期编码的最大值，并设置为查询条件；
		 * 不为空则将前台页面的值作为查询条件。
		 */
		sql4 = "select max(学年学期编码) from V_规则管理_学年学期表";
		vec1 = db.GetContextVector(sql4);
		if (vec1 != null && vec1.size() > 0) {
			Xnxqbm = MyTools.fixSql(MyTools.StrFiltr(vec1.get(0)));
		}
//		if(!"".equalsIgnoreCase(GT_XNXQBM)){
//			sql1 = "select count(*) from V_规则管理_特殊规则表 where 学年学期编码='" + MyTools.fixSql(GT_XNXQBM) + MyTools.fixSql(GT_JXXZ) + "'";
//		}else{
//			sql1 = "select count(*) from V_规则管理_特殊规则表 where 学年学期编码=(select max(学年学期编码) FROM V_规则管理_学年学期表)";
//		}
		/**
		 * 当一个学年学期编码已存在V_规则管理_特殊规则表中，则直接查询；
		 * 否则将V_教职工基本数据子类中所有教师工号插入V_规则管理_特殊规则表的这个学年学期编码下，
		 * 当插入成功后，最后执行查询。
		 */
		//if(db.getResultFromDB(sql1)){
		//每次查询之前,先更新一遍当前学年学期下的教师列表,如果有新增的教师编号,则插入,否则直接进行查询列表操作
		//并按照每个查询出来的教师进行插入对应的特殊规则数值
		String sqlts = "select distinct a.工号  from V_教职工基本数据子类 a " +
						" where 工号 not in (select 教师编号 from V_规则管理_特殊规则表  where 学年学期编码='"+ Xnxqbm +"' and 状态=1 )";
						
				Vector vects = db.GetContextVector(sqlts);
				Vector vecsub=new Vector();
				if(vects!=null && vects.size()>0) {
					String[] shuzhi = new String[5];
					for(int i=0; i<vects.size(); i++) {
						shuzhi = MyTools.getProp(request, "Base.PuTongJS").split(",");
						String sqlins = "insert into V_规则管理_特殊规则表 values ('"+ Xnxqbm +"', '"+ vects.get(i) +"', '"+ shuzhi[0] +"', '"+ shuzhi[1] +"', '"+ shuzhi[2] +"', '"+ shuzhi[3] +"', '"+ shuzhi[4] +"', '"+ MyTools.getSessionUserCode(request) +"', getDate(), 1)";
						vecsub.add(sqlins);
					}
					db.executeInsertOrUpdateTransaction(vecsub);
		}
		//------------------------------------------------
		sql = "select distinct a.工号,a.姓名 as GT_JSXM,b.学年学期编码,b.每天次数 as GT_MTCS,b.每周次数 as GT_MZCS," +
				"b.每天节次 as GT_MTJC,b.每周节次 as GT_MZJC,b.最大执教课程数 as GT_ZDZJKCS,b.状态 " +
				" from V_教职工基本数据子类 a left join V_规则管理_特殊规则表 b on a.工号=b.教师编号 " +
				" where 1=1  ";
		/**
		 * 当前台页面的查询条件学年学期编码为空时，查询V_规则管理_学年学期表中学年学期编码的最大值，并设置为查询条件；
		 * 不为空则将前台页面的值作为查询条件。
		 */
		if(!"".equalsIgnoreCase(GT_XNXQBM)){
			//if(!"".equalsIgnoreCase(GT_JXXZ)){
				sql += " and b.学年学期编码 = '" + MyTools.fixSql(GT_XNXQBM) + MyTools.fixSql(GT_JXXZ) + "'";
			//}else{
				//sql += " and b.学年学期编码 like '%" + MyTools.fixSql(GT_XNXQBM) + "%'";
			//}
		}else{
			sql += " and b.学年学期编码='"+Xnxqbm+"'";
		}
		if(!"".equalsIgnoreCase(GT_JSBH)){
			sql += " and a.工号 like '%" + MyTools.fixSql(GT_JSBH) + "%'";
		}
		if(!"".equalsIgnoreCase(GT_JSXM)){
			sql += " and a.姓名 like '%" + MyTools.fixSql(GT_JSXM) + "%'";
		}

		sql += " and b.状态=1 order by a.工号";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
		//}
//		}else{
//			sql2 = "select 工号 from V_教职工基本数据子类";
//			vec2 = db.GetContextVector(sql2);
//			if (vec2 != null && vec2.size() > 0) {
//				for(int i=0;i<vec2.size();i++){
//					if(!"".equalsIgnoreCase(GT_XNXQBM)){
//						sql3 = "insert into V_规则管理_特殊规则表 (学年学期编码,教师编号,状态) values (" +
//								"'" + MyTools.StrFiltr(MyTools.fixSql(this.GT_XNXQBM)) + MyTools.StrFiltr(MyTools.fixSql(this.GT_JXXZ)) +"'," +
//								"'" + MyTools.fixSql(MyTools.StrFiltr(vec2.get(i))) + "'," +
//								"1)";
//					}else{
//						sql3 = "insert into V_规则管理_特殊规则表 (学年学期编码,教师编号,状态) values (" +
//								"'" +Xnxqbm+ "'," +
//								"'" + MyTools.fixSql(MyTools.StrFiltr(vec2.get(i))) + "'," +
//								"1)";
//					}
//					sqlVec.add(sql3);
//				}
//				if(db.executeInsertOrUpdateTransaction(sqlVec)){
//					sql = "select a.工号,a.姓名 as GT_JSXM,b.学年学期编码,b.每天次数 as GT_MTCS,b.每周次数 as GT_MZCS," +
//						  "b.每天节次 as GT_MTJC,b.每周节次 as GT_MZJC,b.最大执教课程数 as GT_ZDZJKCS,b.状态 " +
//						  "from V_教职工基本数据子类 a left join V_规则管理_特殊规则表 b on a.工号=b.教师编号 " +
//						  "where 1=1";
//					/**
//					 * 当前台页面的查询条件学年学期编码为空时，查询V_规则管理_学年学期表中学年学期编码的最大值，并设置为查询条件；
//					 * 不为空则将前台页面的值作为查询条件。
//					 */
//					if(!"".equalsIgnoreCase(GT_XNXQBM)){
//						if(!"".equalsIgnoreCase(GT_JXXZ)){
//							sql += " and b.学年学期编码 = '" + MyTools.fixSql(GT_XNXQBM) + MyTools.fixSql(GT_JXXZ) + "'";
//						}else{
//							sql += " and b.学年学期编码 like '%" + MyTools.fixSql(GT_XNXQBM) + "%'";
//						}
//					}else{
//						sql += " and b.学年学期编码=(select max(学年学期编码) from V_规则管理_学年学期表)";
//					}
//					if(!"".equalsIgnoreCase(GT_JSBH)){
//						sql += " and a.工号 like '%" + MyTools.fixSql(GT_JSBH) + "%'";
//					}
//				}
//			}
	}
	
	/**
	 * 分页查询 无效的特殊规则为列表（状态为0则无效，1则有效）
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector quehisRec(int pageNum ,int pageSize) throws SQLException {
		Vector vec = null;
		String sql="";
		sql = "select a.工号,a.姓名,b.学年学期编码,b.每天次数,b.每周次数,b.每天节次,b.每周节次,b.最大执教课程数,b.状态 " +
			  "from V_教职工基本数据子类 a left join V_规则管理_特殊规则表 b on a.工号=b.教师编号 " +
			  "where b.状态=0 order by a.工号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年+学期 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJXXZCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
					 "from V_基础信息_教学性质 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教师编号下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJSBHCombo() throws SQLException{
		Vector vec = null;
		String sql = "SELECT  '' AS comboValue,'请选择' AS comboName FROM V_教职工基本数据子类 union " +
				     "SELECT 工号 AS comboValue,工号 AS comboName FROM V_教职工基本数据子类 where 1=1";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 保存方法
	 * @date:2015-06-03
	 * @author:wangzh
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveRec() throws WrongSQLException, SQLException {
		String sql = "";
		sql = "update V_规则管理_特殊规则表 set " +
			  "每天次数='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_MTCS))+"'," +
			  "每周次数='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_MZCS))+"'," +
			  "每天节次='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_MTJC))+"'," +
			  "每周节次='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_MZJC))+"'," +
			  "最大执教课程数='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_ZDZJKCS))+"' " +
			  "where 学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_XNXQBM1))+"' " +
			  "and 教师编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_JSBH1))+"'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除方法
	 * @date:2015-06-03
	 * @author:wangzh
	 * @throws SQLException
	 */
	public void delRec() throws  SQLException {
		String sql = "";
		Vector sqlVec = new Vector();
		String[] JSBH = GT_JSBH.split("\\,");//将主键字符串分割为主键字符串数组
		for(int i=0;i<JSBH.length;i++){
			sql = "update V_规则管理_特殊规则表 set 状态=0 " +
				  "where 学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_XNXQBM))+"' " +
				  "and 教师编号='"+MyTools.StrFiltr(MyTools.fixSql(JSBH[i]))+"'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 还原方法
	 * @date:2015-06-03
	 * @author:wangzh
	 * @throws SQLException
	 */
	public void reduRec() throws  SQLException {
		String sql = "";
		sql = "update V_规则管理_特殊规则表 set 状态=1 " +
			  "where 学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_XNXQBM))+"' " +
			  "and 教师编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GT_JSBH))+"'";
		   if(db.executeInsertOrUpdate(sql)){
			   this.setMSG("还原成功");
		   }else{
			   this.setMSG("还原失败");
		   }
	}
	
	/**
	 * GET && SET 方法
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return
	 */
	public String getGT_XNXQBM() {
		return GT_XNXQBM;
	}

	public void setGT_XNXQBM(String gT_XNXQBM) {
		GT_XNXQBM = gT_XNXQBM;
	}

	public String getGT_JSBH() {
		return GT_JSBH;
	}

	public void setGT_JSBH(String gT_JSBH) {
		GT_JSBH = gT_JSBH;
	}

	public String getGT_MTCS() {
		return GT_MTCS;
	}

	public void setGT_MTCS(String gT_MTCS) {
		GT_MTCS = gT_MTCS;
	}

	public String getGT_MZCS() {
		return GT_MZCS;
	}

	public void setGT_MZCS(String gT_MZCS) {
		GT_MZCS = gT_MZCS;
	}

	public String getGT_MTJC() {
		return GT_MTJC;
	}

	public void setGT_MTJC(String gT_MTJC) {
		GT_MTJC = gT_MTJC;
	}

	public String getGT_MZJC() {
		return GT_MZJC;
	}

	public void setGT_MZJC(String gT_MZJC) {
		GT_MZJC = gT_MZJC;
	}

	public String getGT_ZDZJKCS() {
		return GT_ZDZJKCS;
	}

	public void setGT_ZDZJKCS(String gT_ZDZJKCS) {
		GT_ZDZJKCS = gT_ZDZJKCS;
	}

	public String getGT_ZT() {
		return GT_ZT;
	}

	public void setGT_ZT(String gT_ZT) {
		GT_ZT = gT_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getGT_JSXM() {
		return GT_JSXM;
	}

	public void setGT_JSXM(String gT_JSXM) {
		GT_JSXM = gT_JSXM;
	}

	public String getGT_XNXQBM1() {
		return GT_XNXQBM1;
	}

	public void setGT_XNXQBM1(String gT_XNXQBM1) {
		GT_XNXQBM1 = gT_XNXQBM1;
	}

	public String getGT_JSBH1() {
		return GT_JSBH1;
	}

	public void setGT_JSBH1(String gT_JSBH1) {
		GT_JSBH1 = gT_JSBH1;
	}

	public String getGT_JXXZ() {
		return GT_JXXZ;
	}

	public void setGT_JXXZ(String gT_JXXZ) {
		GT_JXXZ = gT_JXXZ;
	}

	public String getGT_JS() {
		return GT_JS;
	}

	public void setGT_JS(String gT_JS) {
		GT_JS = gT_JS;
	}
	
	
}
