package com.pantech.src.devolop.ruleManage;
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

public class ClassSetBean {
	private String USERCODE;//用户编号
	private String BJBH;//班级编号
	private String BJMC;//班级名称
	private String XBDM;//系部代码
	private String NJDM;//年级代码
	private String SSZY;//所属专业
	private String ZRS;//总人数
	private String BZR;//班主任
	private String BJJC;//班级简称
	private String JSBH;//教室编号
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public ClassSetBean(HttpServletRequest request) {
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
		JSBH = "";//教室编号
		MSG = "";//提示信息
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
	public Vector queryRec(int pageNum, int page, String BJBH_CX, String BJMC_CX, String XBDM_CX, String NJDM_CX, String SSZY_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select a.行政班代码 as BJBH,a.行政班名称 as BJMC,a.系部代码 as XBDM,b.系部名称 as XBMC,a.年级代码 as NJDM,c.年级名称 as NJMC," +
			"a.专业代码 as SSZY,d.专业名称 as ZYMC,a.总人数 as ZRS,isnull(a.班主任工号,'') as BZRGH,isnull(e.姓名,'') as BZR,isnull(a.行政班简称,'') as BJJC," +
			"isnull(a.教室编号,'') as JSBH,isnull(f.教室名称,'') as JSMC," +
			"(select count(*) from V_规则管理_授课计划明细表 ta left join V_规则管理_授课计划主表 tb on tb.授课计划主表编号=ta.授课计划主表编号 where tb.行政班代码=a.行政班代码) as num " +
			"from V_学校班级数据子类 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"left join V_教职工基本数据子类 e on e.工号=a.班主任工号 " +
			"left join V_教室数据类 f on f.教室编号=a.教室编号 " +
			"where 1=1";
		if(!"".equalsIgnoreCase(BJBH_CX)){
			sql += " and a.行政班代码 like '%" + MyTools.fixSql(BJBH_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(BJMC_CX)){
			sql += " and a.行政班名称 like '%" + MyTools.fixSql(BJMC_CX) + "%'";
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
		sql += " order by a.行政班代码";
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
				"from V_专业基本信息数据子类 where len(专业代码)>3 order by orderNum,comboName";
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
		Vector vec1 = null;
		String classCode = "";
		String className = "";
		sql = "select (select top 1 right(a.行政班代码,2) from V_学校班级数据子类 a " +
			"inner join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
			"where a.年级代码='" + MyTools.fixSql(this.getNJDM()) + "' " +//and b.专业代码='" + MyTools.fixSql(this.getSSZY()) + "'
			" and a.系部代码='"+request.getParameter("XBDM1")+"' order by cast (a.行政班代码 as bigint) desc) as 行政班代码," +
			"(select 系部名称 from V_基础信息_系部信息表  where 系部代码='"+MyTools.StrFiltr(request.getParameter("XBDM1"))+"') as 系部名称,"+
			"(select 年级名称 from V_学校年级数据子类 where 年级代码='" + MyTools.fixSql(this.getNJDM()) + "') as 年级名称," +
			"(select 专业名称 from V_专业基本信息数据子类 where 专业代码='" + MyTools.fixSql(this.getSSZY()) + "') as 专业名称";
		vec = db.GetContextVector(sql);
			
		sql = "select 系部代码 from V_基础信息_系部信息表 where 系部名称='"+vec.get(1)+"'";
		vec1 = db.GetContextVector(sql);
		String xb = MyTools.StrFiltr(vec1.get(0));
		classCode = this.getNJDM().substring(0, 2);
		classCode += MyTools.StringToInt(xb.substring(1,3));
		
		if(vec!=null && vec.size()>0){
			className = MyTools.StrFiltr(vec.get(1))+MyTools.StrFiltr(vec.get(2));
			
			if(!"".equalsIgnoreCase(MyTools.StrFiltr(vec.get(0)))){
				int tempNum = 0;
				tempNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)))+1;
				classCode += tempNum<10?"0"+tempNum:tempNum;
				className += tempNum<10?"0"+tempNum:tempNum;
				className += "班";
			}else{
				classCode += "01";
				className += "01班";
			}
		}
		this.setBJBH(classCode);
		this.setBJMC(className);
		
		sql = "insert into V_学校班级数据子类 (行政班代码,年级代码,行政班名称,行政班简称,系部代码,专业代码,建班年月,班主任工号,教室编号,状态) values(" +
			"'" + MyTools.fixSql(this.getBJBH()) + "'," +
			"'" + MyTools.fixSql(this.getNJDM()) + "'," +
			"'" + MyTools.fixSql(this.getBJMC()) + "'," +
			"'" + MyTools.fixSql(this.getBJJC()) + "'," +
			"'" + MyTools.fixSql(this.getXBDM()) + "'," +
			"'" + MyTools.fixSql(this.getSSZY()) + "'," +
			"cast(year(getDate()) as varchar)+(case when month(getDate())<10 then '0'+cast(month(getDate()) as varchar) else cast(month(getDate()) as varchar) end)," +
			"'" + MyTools.fixSql(this.getBZR()) + "'," +
			"'" + MyTools.fixSql(this.getJSBH()) + "'," +
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
		String sql = "";
		Vector vec = null;
		String classCode = "";
		String className = "";
		String tempClassCode = "";
		String tempNjdm = "";
		String tempXbdm = "";
		String bzrAuth = MyTools.getProp(request, "Base.bzr");
		Vector sqlVec = new Vector();
		
		//读取班级原信息
		sql = "select 年级代码,系部代码 from V_学校班级数据子类 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			tempNjdm = MyTools.StrFiltr(vec.get(0));
			tempXbdm = MyTools.StrFiltr(vec.get(1));
		}
		
		//判断如果修改了年级或系部，需重新生成班级代码
		if(!tempNjdm.equalsIgnoreCase(this.getNJDM()) || !tempXbdm.equalsIgnoreCase(this.getXBDM())){
			sql = "select top 1 right(行政班代码,2) from V_学校班级数据子类 where 年级代码='" + MyTools.fixSql(this.getNJDM()) + "' " +
				"and 系部代码='" + MyTools.fixSql(this.getXBDM()) + "' order by 行政班代码 desc";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				int tempNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));
				
				tempNum++;
				tempClassCode = tempNum<10?"0"+tempNum:""+tempNum;
			}else{
				tempClassCode = "01";
			}
			classCode = this.getNJDM().substring(0, 2)+this.getXBDM().substring(2)+tempClassCode;
			
			sql = "select 系部名称 from V_基础信息_系部信息表 where 系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
			vec = db.GetContextVector(sql); 
			if(vec!=null && vec.size()>0){
				className = MyTools.StrFiltr(vec.get(0))+this.getNJDM().substring(0, 2)+"级"+tempClassCode+"班";
			}else{
				className = this.getNJDM().substring(0, 2)+"级"+tempClassCode+"班";
			}
			
			sql = "update V_学校班级数据子类 set " +
				"行政班代码='" + MyTools.fixSql(classCode) + "'," +
				"年级代码='" + MyTools.fixSql(this.getNJDM()) + "'," +
				"行政班名称='" + MyTools.fixSql(className) + "'," +
				"系部代码='" + MyTools.fixSql(this.getXBDM()) + "'," +
				"专业代码='" + MyTools.fixSql(this.getSSZY()) + "'," +
				"班主任工号='" + MyTools.fixSql(this.getBZR()) + "'," +
				"行政班简称='" + MyTools.fixSql(this.getBJJC()) + "'," +
				"教室编号='" + MyTools.fixSql(this.getJSBH()) + "' " +
				"where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
		}else{
			sql = "update V_学校班级数据子类 set " +
				"年级代码='" + MyTools.fixSql(this.getNJDM()) + "'," +
				"系部代码='" + MyTools.fixSql(this.getXBDM()) + "'," +
				"专业代码='" + MyTools.fixSql(this.getSSZY()) + "'," +
				"班主任工号='" + MyTools.fixSql(this.getBZR()) + "'," +
				"行政班简称='" + MyTools.fixSql(this.getBJJC()) + "'," +
				"教室编号='" + MyTools.fixSql(this.getJSBH()) + "' " +
				"where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
		}
		sqlVec.add(sql);
		
		//修改班主任权限
		sql = "delete from sysUserAuth where AuthCode='" + MyTools.fixSql(bzrAuth.replaceAll("@", "")) + "'";
		sqlVec.add(sql);
		
		sql = "insert into sysUserAuth (UserCode,AuthCode,state) " +
			"select distinct 班主任工号,'" + MyTools.fixSql(bzrAuth.replaceAll("@", "")) + "','Y' from V_学校班级数据子类 where 班主任工号 is not null and 班主任工号<>''";
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
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void delRec() throws SQLException {		 
		String sql = "";
		String sql2 = "";
		//检查当前要删除的专业是否已班级关联
		sql = "select count(*) from V_规则管理_授课计划主表 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("当前选择的班级已经设置授课计划，无法删除！");
		}else{
			sql2 = "delete from V_规则管理_授课计划主表  where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' ";
			sql2 += " delete from V_学校班级数据子类 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
			sql2 += " delete from V_排课管理_课程表主表 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
			
			if(db.executeInsertOrUpdate(sql2)){
				this.setMSG("删除成功");
			}else{
				this.setMSG("删除失败");
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
		Vector vec = null; // 结果集
		
		sql = "select a.学籍号,a.班内学号,a.姓名,b.专业名称,c.类别名称 as 招生类别 from V_学生基本数据子类 a " +
			"left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
			"left join V_信息类别_类别操作 c on c.描述=a.招生类别 and c.父类别代码='ZSLBM' " +
			"where a.学生状态 in ('01','05') and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			"order by (case when a.班内学号='' then '9999' else a.班内学号 end)";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
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
	
	/**
	 * 读取班级教室下拉框
	 * @date:2017-08-02
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadClassroomCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 教室名称 as comboName,教室编号 as comboValue " +
				"from V_教室数据类 where 教室类型代码='01' and 校区代码='" + MyTools.fixSql(this.getXBDM()) + "' " +
				"and 教室编号 not in (select distinct isnull(教室编号,'') from V_学校班级数据子类 " +
				"where 行政班代码<>'" + MyTools.fixSql(this.getBJBH()) + "')";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-07-23
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector openTeacher(int pageNum, int pageSize, String teaId, String teaName, String teacharr) throws SQLException{
		Vector vec = null;
		String teaid = "";
		String bzrAuth = MyTools.getProp(request, "Base.bzr").replaceAll("@", "");
		
		if(teacharr.equals("")){//没有教师数据
			teaid = "''";
		}else{//选中的教师排在前面显示
			String[] teachers = teacharr.split("\\+");
			for(int i=0; i<teachers.length; i++){
				teaid += "'"+teachers[i]+"',";
			}
			teaid = teaid.substring(0, teaid.length()-1);
		}
		String sql = "select t.工号,t.姓名 from (" +
				"select a.工号,a.姓名,'1' as px from V_教职工基本数据子类 a " +
				"left join sysUserAuth b on b.UserCode=a.工号 " +
				"where b.AuthCode='" + MyTools.fixSql(bzrAuth) + "' and 工号 in (" + teaid + ") " +
				"union all " +
				"select a.工号,a.姓名,'2' as px from V_教职工基本数据子类 a " +
				"left join sysUserAuth b on b.UserCode=a.工号 " +
				"where b.AuthCode='" + MyTools.fixSql(bzrAuth) + "' and 工号 not in ("+teaid+")";
		if(!teaId.equalsIgnoreCase("")){
			sql += " and 工号 like '%"+MyTools.fixSql(teaId)+"%' ";
		}	
		if(!teaName.equalsIgnoreCase("")){
			sql += " and 姓名 like '%"+MyTools.fixSql(teaName)+"%' ";
		}
		sql += ") t order by px,工号 ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
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

	public String getJSBH() {
		return JSBH;
	}

	public void setJSBH(String jSBH) {
		JSBH = jSBH;
	}
}