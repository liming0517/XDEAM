package com.pantech.devolop.registerScoreSet;
/*
@date 2016.01.29
@author yeq
模块：M6.1 科目设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class KmszBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	private String CK_ID; //科目编号
	private String CK_XNXQBM; //学年学期编码
	private String CK_XNXQMC; //学年学期名称
	private String CK_NJDM; //年级代码
	private String CK_NJMC; //年级名称
	private String CK_ZYDM; //专业代码
	private String CK_ZYMC; //专业名称
	private String CK_KCDM; //课程代码
	private String CK_KCBM; //课程名称
	private String CK_KCLX; //课程类型
	private String CK_KMLX; //科目类型
	private String CK_SFCYJD; //是否参与绩点
	private String CK_CJR; //创建人
	private String CK_CJSJ; //创建时间
	private String CK_ZT; //状态

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public KmszBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-27
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		AUTH = "";//用户权限
		CK_ID = ""; //科目编号
		CK_XNXQBM = ""; //学年学期编码
		CK_XNXQMC = ""; //学年学期名称
		CK_NJDM = ""; //年级代码
		CK_NJMC = ""; //年级名称
		CK_ZYDM = ""; //专业代码
		CK_ZYMC = ""; //专业名称
		CK_KCDM = ""; //课程代码
		CK_KCBM = ""; //课程名称
		CK_KCLX = ""; //课程类型
		CK_KMLX = ""; //科目类型
		CK_SFCYJD = ""; //是否参与绩点
		CK_CJR = ""; //创建人
		CK_CJSJ = ""; //创建时间
		CK_ZT = ""; //状态
	}

	/**
	 * 分页查询 科目列表
	 * @date:2016-01-29
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param CK_XNXQMC_CX 学年学期名称
	 * @param CK_JXXZ_CX 教学性质
	 * @param CK_ZYMC_CX 专业名称
	 * @param CK_KCMC_CX 课程名称
	 * @param CK_KMLX_CX 科目类型
	 * @param CK_CYJD_CX 参与绩点
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSubjectList(int pageNum, int pageSize, String CK_XNXQMC_CX, String CK_JXXZ_CX, String CK_ZYMC_CX, String CK_KCMC_CX, String CK_KMLX_CX, String CK_CYJD_CX, String CK_NJDM_CX, String CK_KCLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
//		sql = "select distinct a.科目编号 as CK_ID,b.学年学期编码 as CK_XNXQBM,b.学年学期名称 as CK_XNXQMC,c.教学性质 as CK_JXXZ,a.年级名称 as CK_NJMC,a.专业名称 as CK_ZYMC,a.课程名称 as CK_KCMC," +
//			"case a.科目类型 when '0' then '未设置' when '1' then '文科' when '2' then '理科' end as CK_KMLX," +
//			"case a.是否参与绩点 when '0' then '否' when '1' then '是' end as CK_SFCYJD,d.学分 as CK_XF," +
//			"case a.课程类型 when '1' then '普通课程' when '2' then '添加课程' when '3' then '选修课程' else a.课程类型 end as CK_KCLX from V_成绩管理_科目课程信息表 a " +
//			"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
//			"left join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
//			"left join V_规则管理_培养计划信息表 d on d.学年学期编码=a.学年学期编码 and d.专业代码=a.专业代码 and d.课程代码=a.课程代码 " +
//			"where 1=1";
		sql = "select distinct a.科目编号 as CK_ID,b.学年学期编码 as CK_XNXQBM,b.学年学期名称 as CK_XNXQMC,c.教学性质 as CK_JXXZ," +
			"a.年级名称 as CK_NJMC,a.专业名称 as CK_ZYMC,a.课程名称 as CK_KCMC,case a.科目类型 when '0' then '未设置' when '1' then '文科' when '2' then '理科' end as CK_KMLX," +
			"case a.是否参与绩点 when '0' then '否' when '1' then '是' end as CK_SFCYJD," +
			"case a.是否参与留级 when '0' then '否' when '1' then '是' end as CK_SFCYLJ," +
			"case when a.课程类型='1' then (select top 1 t.学分 from V_规则管理_培养计划信息表 t where t.学年学期编码=a.学年学期编码 and t.专业代码=a.专业代码 and t.年级代码=a.年级代码 and t.课程代码=a.课程代码 ) " +
			"when a.课程类型='2' then (select top 1 t.学分 from V_排课管理_添加课程信息表 t left join V_学校班级数据子类 t1 on t1.行政班代码=t.行政班代码 where t.学年学期编码=a.学年学期编码 and t1.专业代码=a.专业代码 and t1.年级代码=a.年级代码 and t.课程编号=a.课程代码) " +
			"when a.课程类型='3' then (select top 1 t.学分 from V_规则管理_选修课授课计划明细表 t left join V_规则管理_选修课授课计划主表 t1 on t1.授课计划主表编号=t.授课计划主表编号 where t1.学年学期编码=a.学年学期编码 and t1.课程代码=a.课程代码) " +
			"when a.课程类型='4' then (select top 1 t.学分 from V_规则管理_分层课程信息表 t where t.学年学期编码=a.学年学期编码 and t.系代码=a.专业代码 and t.年级代码=a.年级代码 and t.课程代码=a.课程代码) " +
			"else 0 end as CK_XF," +
			"case a.课程类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as CK_KCLX " +
			"from V_成绩管理_科目课程信息表 a " +
			"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
			"left join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
			"where 1=1";
		
		//判断查询条件
		if(!"".equalsIgnoreCase(CK_XNXQMC_CX)){
			sql += " and left(b.学年学期编码,5)='" + MyTools.fixSql(CK_XNXQMC_CX) + "'";
		}
		if(!"".equalsIgnoreCase(CK_JXXZ_CX)){
			sql += " and b.教学性质='" + MyTools.fixSql(CK_JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(CK_ZYMC_CX)){
			sql += " and a.专业名称 like '%" + MyTools.fixSql(CK_ZYMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(CK_KCMC_CX)){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(CK_KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(CK_KMLX_CX)){
			sql += " and a.科目类型='" + MyTools.fixSql(CK_KMLX_CX) + "'";
		}
		if(!"".equalsIgnoreCase(CK_CYJD_CX)){
			sql += " and a.是否参与绩点='" + MyTools.fixSql(CK_CYJD_CX) + "'";
		}
		if(!"".equalsIgnoreCase(CK_NJDM_CX)){
			sql += " and a.年级代码='" + MyTools.fixSql(CK_NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(CK_KCLX_CX)){
			sql += " and a.课程类型='" + MyTools.fixSql(CK_KCLX_CX) + "'";
		}
		sql += " order by b.学年学期编码 desc";
				
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2016-06-29
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,left(学年学期编码,5) as comboValue,1 as orderNum " +
				"from V_规则管理_学年学期表 order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2016-01-29
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJXXZCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
				"from V_基础信息_教学性质 where 状态='1'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取年级下拉框
	 * @date:2016-03-23
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNJDMCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,'1' as orderNum " +
				"from V_学校年级数据子类 where 年级状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2016-01-29
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "";
		//String admin = MyTools.getProp(request, "Base.admin");//管理员
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		sql = "select '请选择' as comboName,'' as comboValue " +
			"union all " +
			"select distinct 专业名称 as comboName,专业代码 as comboValue " +
			"from V_专业基本信息数据子类 where 状态='1'";
		
		//判断权限,管理员和排公共课的老师可看到所有专业,排专业课的老师只可以看到自己的专业
		if(this.getAUTH().indexOf(majorTeacher) > -1){
			sql += " and 专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程下拉框
	 * @date:2016-01-29
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCourseCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 课程名称 as comboName,课程号 as comboValue " +
				"from V_课程数据子类";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 设置科目相关信息
	 * @date:2016-01-29
	 * @author:yeq
	 * @param selectId 所有选择的科目id
	 * @param setType 设置的相关信息类型
	 * @throws SQLException
	 */
	public void updateSubjectInfo(String selectId, String setType)throws SQLException{
		String sql = "update V_成绩管理_科目课程信息表 set ";
		
		if("wk".equalsIgnoreCase(setType))
			sql += "科目类型='1' ";
		if("lk".equalsIgnoreCase(setType))
			sql += "科目类型='2' ";
		if("cyjd".equalsIgnoreCase(setType))
			sql += "是否参与绩点='1' ";
		if("bcyjd".equalsIgnoreCase(setType))
			sql += "是否参与绩点='0' ";
		if("cylj".equalsIgnoreCase(setType))
			sql += "是否参与留级='1' ";
		if("bcylj".equalsIgnoreCase(setType))
			sql += "是否参与留级='0' ";
		
		sql += "where 科目编号 in ('" + selectId.replaceAll(",", "','") + "')";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除重复的班级课程
	 * @date:2016-02-17
	 * @author:yeq
	 * @request
	 * @throws SQLException
	 */
	public static boolean delRepeatCourse(HttpServletRequest request) throws SQLException{
		DBSource db = new DBSource(request); //数据库对象
		String sql = "";
		Vector vec = null;
		Vector tempVec = null;
		boolean result = true;
		
		//查询当前学期所有科目信息
//		sql = "select 科目编号 from V_成绩管理_科目课程信息表 where 学年学期编码 in (select 学年学期编码 from V_规则管理_学年学期表 " +
//			"where convert(nvarchar(10),getDate(),21) between convert(nvarchar(10),学期开始时间,21) and convert(nvarchar(10),学期结束时间,21))";
		sql = "select 科目编号 from V_成绩管理_科目课程信息表 where 学年学期编码 in (select top 1 学年学期编码 from V_规则管理_学年学期表 " +
			"where convert(nvarchar(10),getDate(),21)>convert(nvarchar(10),学期开始时间,21) order by 学年学期编码 desc)";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			String kmbh = "";
			
			//过滤重复班级课程
			for(int i=0; i<vec.size(); i++){
				kmbh += "'" + MyTools.StrFiltr(vec.get(i)) + "',";
			}
			sql = "select distinct 行政班代码,课程代码,相关编号,登分教师编号,登分教师姓名 from V_成绩管理_登分教师信息表 " +
				"where 来源类型='1' and 科目编号 in (" + kmbh.substring(0, kmbh.length()-1) + ") " +
				"order by 行政班代码,课程代码,相关编号 ";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				String classCode = "";
				String courseCode = "";
				String code = "";
				String teaCode = "";
				String teaName = "";
				String tempClassCode = "";
				String tempCourseCode = "";
				String tempCode = "";
				String tempTeaCode[] = new String[0];
				String tempTeaName[] = new String[0];
				Vector delVec = new Vector();
				Vector sqlVec = new Vector();
				boolean updateFlag = false;
				
				for(int i=0; i<vec.size(); i+=5){
					tempClassCode = MyTools.StrFiltr(vec.get(i));
					tempCourseCode = MyTools.StrFiltr(vec.get(i+1));
					tempCode = MyTools.StrFiltr(vec.get(i+2));
					tempTeaCode = MyTools.StrFiltr(vec.get(i+3)).split(",");
					tempTeaName = MyTools.StrFiltr(vec.get(i+4)).split(",");
					
					if(classCode.equalsIgnoreCase(tempClassCode) && courseCode.equalsIgnoreCase(tempCourseCode)){
						delVec.add(tempCode);
						
						for(int j=0; j<tempTeaCode.length; j++){
							if(teaCode.indexOf(tempTeaCode[j]) < 0){
								teaCode += ","+tempTeaCode[j];
								teaName += ","+tempTeaName[j];
								updateFlag = true;
							}
						}
					}else{
						if(updateFlag == true){
							sql = "update V_成绩管理_登分教师信息表 set " +
								"登分教师编号='" + MyTools.fixSql(teaCode) + "'," +
								"登分教师姓名='" + MyTools.fixSql(teaName) + "' " +
								"where 相关编号='" + MyTools.fixSql(code) + "'";
							sqlVec.add(sql);
							
							updateFlag = false;
						}
						
						classCode = tempClassCode;
						courseCode = tempCourseCode;
						code = tempCode;
						teaCode = MyTools.StrFiltr(vec.get(i+3));
						teaName = MyTools.StrFiltr(vec.get(i+4));
					}
				}
				
				if(delVec.size() > 0){
					for(int i=0; i<delVec.size(); i++){
						sql = "delete from V_成绩管理_登分教师信息表 where 相关编号='" + MyTools.fixSql(MyTools.StrFiltr(delVec.get(i))) + "'";
						sqlVec.add(sql);
						
						sql = "delete from V_成绩管理_学生成绩信息表 where 相关编号='" + MyTools.fixSql(MyTools.StrFiltr(delVec.get(i))) + "'";
						sqlVec.add(sql);
					}
					if(db.executeInsertOrUpdateTransaction(sqlVec)){
						result = true;
					}else{
						result = false;
					}
				}
			}
		}
		
		return result;
	}
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getAUTH() {
		return AUTH;
	}

	public void setAUTH(String aUTH) {
		AUTH = aUTH;
	}
	
	public String getCK_ID() {
		return CK_ID;
	}
	
	public void setCK_ID(String cK_ID) {
		CK_ID = cK_ID;
	}

	public String getCK_XNXQBM() {
		return CK_XNXQBM;
	}

	public void setCK_XNXQBM(String cK_XNXQBM) {
		CK_XNXQBM = cK_XNXQBM;
	}

	public String getCK_XNXQMC() {
		return CK_XNXQMC;
	}

	public void setCK_XNXQMC(String cK_XNXQMC) {
		CK_XNXQMC = cK_XNXQMC;
	}

	public String getCK_NJDM() {
		return CK_NJDM;
	}

	public void setCK_NJDM(String cK_NJDM) {
		CK_NJDM = cK_NJDM;
	}

	public String getCK_NJMC() {
		return CK_NJMC;
	}

	public void setCK_NJMC(String cK_NJMC) {
		CK_NJMC = cK_NJMC;
	}

	public String getCK_ZYDM() {
		return CK_ZYDM;
	}

	public void setCK_ZYDM(String cK_ZYDM) {
		CK_ZYDM = cK_ZYDM;
	}

	public String getCK_ZYMC() {
		return CK_ZYMC;
	}

	public void setCK_ZYMC(String cK_ZYMC) {
		CK_ZYMC = cK_ZYMC;
	}

	public String getCK_KCDM() {
		return CK_KCDM;
	}

	public void setCK_KCDM(String cK_KCDM) {
		CK_KCDM = cK_KCDM;
	}

	public String getCK_KCBM() {
		return CK_KCBM;
	}

	public void setCK_KCBM(String cK_KCBM) {
		CK_KCBM = cK_KCBM;
	}

	public String getCK_KCLX() {
		return CK_KCLX;
	}

	public void setCK_KCLX(String cK_KCLX) {
		CK_KCLX = cK_KCLX;
	}

	public String getCK_KMLX() {
		return CK_KMLX;
	}

	public void setCK_KMLX(String cK_KMLX) {
		CK_KMLX = cK_KMLX;
	}

	public String getCK_SFCYJD() {
		return CK_SFCYJD;
	}

	public void setCK_SFCYJD(String cK_SFCYJD) {
		CK_SFCYJD = cK_SFCYJD;
	}

	public String getCK_CJR() {
		return CK_CJR;
	}

	public void setCK_CJR(String cK_CJR) {
		CK_CJR = cK_CJR;
	}

	public String getCK_CJSJ() {
		return CK_CJSJ;
	}

	public void setCK_CJSJ(String cK_CJSJ) {
		CK_CJSJ = cK_CJSJ;
	}

	public String getCK_ZT() {
		return CK_ZT;
	}

	public void setCK_ZT(String cK_ZT) {
		CK_ZT = cK_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}