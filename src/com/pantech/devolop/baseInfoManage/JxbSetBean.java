package com.pantech.devolop.baseInfoManage;
/*
@date 2016.01.08
@author yeq
模块：M1.9班级设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class JxbSetBean {
	private String USERCODE;//用户编号
	private String BJBH;//班级编号
	private String BJMC;//班级名称
	private String XBDM;//系部代码
	private String NJDM;//年级代码
	private String SSZY;//所属专业
	private String ZRS;//总人数
	private String BZR;//班主任
	private String BJJC;//班级简称
	private String BJLX;//班级类型
	private String JSBH;//教室编号
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public JxbSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-15
	 * @author:yeq
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加每周天数,上午节数,下午节数,晚上节数以及节次时间设置基础信息
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		BJBH = "";//班级编号
		BJMC = "";//班级名称
		XBDM = "";//系部代码
		NJDM = "";//年级代码
		SSZY = "";//所属专业
		ZRS = "";//总人数
		BZR = "";//班主任
		BJJC = "";//班级简称
		MSG = "";    //提示信息
	}
	
	/**
	 * 换班级
	 * @date:2017-03-17
	 * @author:Zouyu
	 * @param xsmd_xh 学生学号
	 * @param xsmd_yjxbmc 原班级名称
	 * @param xsmd_xjxbmc 先班级名称
	 * @param xsmd_nj 年级
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void change(String HBH,String bdtARRAY) throws SQLException {		 
		String sql = "update  V_基础信息_教学班学生信息表 set 教学班编号='"+HBH+"' where 学号 in ("+bdtARRAY+") and 教学班编号='"+MyTools.fixSql(this.getBJBH())+"'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("换班成功");
		}else{
			this.setMSG("换班失败");
		}
	}
	
	/**
	 * 删除学生
	 * @date:2017-03-17
	 * @author:Zouyu
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void delmoreRec(String BHARRAY,String jxbbh) throws SQLException {		 
		String sql = "delete from V_基础信息_教学班学生信息表 where 学号 in ("+BHARRAY+") and 教学班编号='"+jxbbh+"'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}

	/**
	 * 
	 * @date:2017-03-17
	 * @author:Zouyu
	 * @param XSMDXZXH 学生名单新增学号
	 * @param XSMDXZXM 学生名单新增姓名
	 * @param JXBH 教学编号
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void addXSMD(String JXBH,String XSMDXZXH) throws SQLException {
		String sql = "";
		String stuCodeArray[] = XSMDXZXH.split(",");
		
		for(int i=0; i<stuCodeArray.length; i+=2){
			sql = "select count(*)  from V_基础信息_教学班学生信息表  where 学号='"+stuCodeArray[i]+"' and 教学班编号='"+JXBH+"'";
			if(db.getResultFromDB(sql)){
				this.setMSG("该学生已经存在");
			}else{
				sql="insert into V_基础信息_教学班学生信息表 values('"+JXBH+"','"+stuCodeArray[i]+"','',GETDATE(),'1')";
				if(db.executeInsertOrUpdate(sql)){
					this.setMSG("增加成功");
				}else{
					this.setMSG("增加失败");
				}
			}
		}
	}
	
	/**
	 * 
	 * @date:2017-03-17
	 * @author:Zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queClassStuTree(String parentCode, String classCode)throws SQLException,WrongSQLException{
		String sql = "";
		String sql2= "";
		Vector vec = null;
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课教师
		
		if(this.getBJLX().equals("1")){
			sql2="and 系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
		}
		//判断层级
		if("".equalsIgnoreCase(parentCode)){
			sql = "select distinct 'bj-'+行政班代码  as id,行政班名称+'（'+行政班代码+'）' as text,state='closed' from V_学校班级数据子类 " +
				"where 年级代码='" + MyTools.fixSql(this.getNJDM()) + "' " + sql2 ;
		}else{
			String parentCodeArray[] = parentCode.split("-");
			sql = "select 学号 as id,姓名 as text ,state='open' from V_学生基本数据子类 " +
				"where 行政班代码='" + parentCodeArray[1] + "' " +
				"and 学号 not in (select 学号 from V_基础信息_教学班学生信息表) order by (case when 班内学号='' then '9999' else 班内学号 end)";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 分页查询 班级列表
	 * @date:2016-01-08
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @param BJBH_CX 行政班代码
	 * @param BJMC_CX 行政班名称
	 * @param NJDM_CX 年级代码
	 * @param SSZY_CX 所属专业
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String BJMC_CX, String XBDM_CX, String NJDM_CX, String SSZY_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select a.教学班编号  as BJBH,a.教学班名称 as BJMC,a.系部代码 as XBDM,b.系部名称 as XBMC,a.年级代码 as NJDM,c.年级名称 as NJMC," +
			"a.专业代码 as SSZY,d.专业名称 as ZYMC,(select count(*) from V_基础信息_教学班学生信息表  where 教学班编号=a.教学班编号  and 状态 in ('1','5')) as ZRS," +
			"a.班主任工号 as BZRGH,e.姓名 as BZR,a.教学班简称 as BJJC,a.教学班类型 as BJLX,a.教室编号 as JSBH,f.教室名称 as JSMC " +
			"from V_基础信息_教学班信息表 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"left join V_教职工基本数据子类 e on e.工号=a.班主任工号 " +
			"left join V_教室数据类 f on a.教室编号=f.教室编号 " +
			"where 1=1";
		
		/*if(!"".equalsIgnoreCase(BJBH_CX)){
			sql += " and a.教学班编号 like '%" + MyTools.fixSql(BJBH_CX) + "%'";
		}*/
		if(!"".equalsIgnoreCase(BJMC_CX)){
			sql += " and a.教学班名称 like '%" + MyTools.fixSql(BJMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(XBDM_CX)){
			sql += " and a.系部代码='" + MyTools.fixSql(XBDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and a.年级代码='" + MyTools.fixSql(NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(SSZY_CX)){
			sql += " and a.专业代码='" + MyTools.fixSql(SSZY_CX) + "'";
		}
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取年级下拉框
	 * @date:2016-01-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNjdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,1 " +
				"from V_学校年级数据子类 where 年级状态='1' " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-03-10
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXbdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 系部名称,系部代码 from V_基础信息_系部信息表 where 系部代码<>'C00' " +
				"order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-03-10
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadBJLXCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select '普通教学班' as comboName,'1' as comboValue " +
				"union all " +
				"select '高复班' as comboName,'2' as comboValue " ;
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2016-01-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类  order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	/**
	 * 读取专业下拉框
	 * @date:2017-03-17
	 * @author:Xouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadClassCombo() throws SQLException{
		Vector vec = null;
		String sql2="";
		
		if(!this.getBJLX().equals("2")){
			//if(!this.getXBDM().equals("")){
				sql2=" and 教学班类型='2' ";
			//}
		}else{
			sql2=" and (系部代码='"+MyTools.fixSql(this.getXBDM())+"' or 系部代码='') ";
		}
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 教学班名称+'  ('+教学班编号+')' as comboName,教学班编号 as comboValue,'1' as orderNum " +
				"from V_基础信息_教学班信息表  where 教学班编号 <> '"+MyTools.fixSql(this.getBJBH())+"' "+sql2+" and 年级代码='"+MyTools.fixSql(this.getNJDM())+"'  order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 新建方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void addRec(String BZRGH) throws SQLException {		 
		String sql = "";
		Vector vec = null;
		String className = "";
		String id="";
		id = db.getMaxID("V_基础管理_教学班信息表", "教学班编号", "JXB_", 6);
		
		sql = "insert into V_基础信息_教学班信息表 (教学班编号,教学班名称,教学班简称,教学班类型,系部代码,专业代码,年级代码,班主任工号" +
				",教室编号,创建时间,状态) values(" +
			"'" + id + "'," +
			"'" + MyTools.fixSql(this.getBJMC()) + "'," +
			"'" + MyTools.fixSql(this.getBJJC()) + "'," +
			"'" + MyTools.fixSql(this.getBJLX()) + "'," +
			"'" + MyTools.fixSql(this.getXBDM()) + "'," +
			"'" + MyTools.fixSql(this.getSSZY()) + "'," +
			"'" + MyTools.fixSql(this.getNJDM()) + "'," +
			"'" + MyTools.fixSql(this.getBZR()) + "'," +		
			"'" + MyTools.fixSql(this.getJSBH()) + "'," +
			"getDate()," +
			//"'" + MyTools.fixSql(this.getZRS()) + "'," +
			"'1')";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 编辑方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void modRec() throws SQLException {		
		String sql = "update V_基础信息_教学班信息表 set " +
			//"年级代码='" + MyTools.fixSql(this.getNJDM()) + "'," +
//			"行政班名称='" + MyTools.fixSql(classname) + "'," +		
			"教学班名称='" + MyTools.fixSql(this.getBJMC()) + "'," +
			"教学班类型='" + MyTools.fixSql(this.getBJLX()) + "'," +
			"专业代码='" + MyTools.fixSql(this.getSSZY()) + "'," +
			"系部代码='" + MyTools.fixSql(this.getXBDM()) + "'," +
			//"总人数='" + MyTools.fixSql(this.getZRS()) + "'," +
			"教室编号='" + MyTools.fixSql(this.getJSBH()) + "'," +
			"班主任工号='" + MyTools.fixSql(this.getBZR()) + "'," +
			"教学班简称='" + MyTools.fixSql(this.getBJJC()) + "' " +
			"where 教学班编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除方法
	 * @date:2016-01-08
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void delRec() throws SQLException {		 
		String sql = "";
		
		//检查当前要删除的专业是否已班级关联
		sql = "select count(*) from V_规则管理_授课计划明细表 where 授课计划主表编号 in (select 授课计划主表编号 from dbo.V_规则管理_授课计划主表 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "') ";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("当前选择的班级已经设置授课计划，无法删除！");
		}else{
			sql = "delete from V_基础信息_教学班信息表 where 教学班编号='" + MyTools.fixSql(this.getBJBH()) + "'";
			if(db.executeInsertOrUpdate(sql)){
				sql="delete from V_基础信息_教学班学生信息表 where 教学班编号='" + MyTools.fixSql(this.getBJBH()) + "'";
				if(db.executeInsertOrUpdate(sql)){
					this.setMSG("删除成功");
				}else{
				this.setMSG("删除失败");
			}
			}
		}
	}
	
	/**
	 * 查询班级学生列表
	 * @date:2017-03-06
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadStuList(int pageNum, int pageSize) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec=new Vector(); // 结果集
		
		/*sql = "select 学号 from V_基础信息_教学班学生信息表 where 教学班编号='"+MyTools.fixSql(this.getBJBH())+"'";
		vec=db.GetContextVector(sql);
		String b="";
		if(vec.size()==0){
			b+="";
		}else if(vec.size()==1){
			b+= vec.get(0);
		}else {
		for(int i=0;i<vec.size();i++){
			 b+=vec.get(i)+"','";
		} 
		b=b.substring(0,b.length()-3);
		}*/
		sql="select distinct a.学号,姓名,b.专业名称,c.类别名称  as 招生类别 from V_学生基本数据子类 a "+
					"left join V_专业基本信息数据子类 b on a.专业代码=b.专业代码 "+
					"left join V_信息类别_类别操作 c on a.招生类别=c.描述 and c.父类别代码='ZSLBM' "+
					"left join V_基础信息_教学班学生信息表 d on a.学号=d.学号  where  d.教学班编号='"+MyTools.fixSql(this.getBJBH())+"'";
			vec=db.getConttexJONSArr(sql, pageNum,pageSize);
	
		return vec;	
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

	public String getBJBH() {
		return BJBH;
	}

	public void setBJBH(String bJBH) {
		BJBH = bJBH;
	}

	public String getBJMC() {
		return BJMC;
	}

	public void setBJMC(String bJMC) {
		BJMC = bJMC;
	}

	public String getXBDM() {
		return XBDM;
	}

	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}

	public String getNJDM() {
		return NJDM;
	}

	public void setNJDM(String nJDM) {
		NJDM = nJDM;
	}

	public String getSSZY() {
		return SSZY;
	}

	public void setSSZY(String sSZY) {
		SSZY = sSZY;
	}

	public String getZRS() {
		return ZRS;
	}

	public void setZRS(String zRS) {
		ZRS = zRS;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getBZR() {
		return BZR;
	}

	public void setBZR(String bZR) {
		BZR = bZR;
	}

	public String getBJJC() {
		return BJJC;
	}

	public void setBJJC(String bJJC) {
		BJJC = bJJC;
	}

	public String getBJLX() {
		return BJLX;
	}

	public void setBJLX(String bJLX) {
		BJLX = bJLX;
	}

	public String getJSBH() {
		return JSBH;
	}

	public void setJSBH(String jSBH) {
		JSBH = jSBH;
	}
	
	
}