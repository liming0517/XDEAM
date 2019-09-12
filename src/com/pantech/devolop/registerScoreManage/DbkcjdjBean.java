package com.pantech.devolop.registerScoreManage;
/*
@date 2016.11.03
@author yeq
模块：M7.3 大补考成绩登记
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class DbkcjdjBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限

	private String CX_ID; //编号
	private String CX_XH; //学号
	private String CX_XM; //姓名
	private String CX_XGBH; //相关编号
	private String CX_ZP; //总评
	private String CX_CX1; //重修1
	private String CX_CX2; //重修2
	private String CX_BK; //补考
	private String CX_DBK; //大补考
	private String CX_DYCJ; //打印成绩
	private String CX_CJZT; //成绩状态
	private String CX_CJR; //创建人
	private String CX_CJSJ; //创建时间
	private String CX_ZT; //状态
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public DbkcjdjBean(HttpServletRequest request) {
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
		CX_ID = ""; //编号
		CX_XH = ""; //学号
		CX_XM = ""; //姓名
		CX_XGBH = ""; //相关编号
		CX_ZP = ""; //总评
		CX_CX1 = ""; //重修1
		CX_CX2 = ""; //重修2
		CX_BK = ""; //补考
		CX_DBK = ""; //大补考
		CX_DYCJ = ""; //打印成绩
		CX_CJZT = ""; //成绩状态
		CX_CJR = ""; //创建人
		CX_CJSJ = ""; //创建时间
		CX_ZT = ""; //状态
	}

	/**
	 * 读取当前学年
	 * @date:2016-11-03
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadCurXn() throws SQLException{
		Vector vec = null;
		String curXnxq = "";
		String sql = "select top 1 学年 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		return curXnxq;
	}
	
	/**
	 * 分页查询 科目列表
	 * @date:2016-11-03
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XN_CX 学年
	 * @param KCMC_CX 课程名称
	 * @param KCLX_CX 课程类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSubjectList(int pageNum, int pageSize, String XN_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String yearRange = "0";
//		sql = "select distinct '" + MyTools.fixSql(XN_CX) + "' as 大补考学年,课程名称,课程类型 from (" +
//			"select left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,d.行政班名称,c.课程名称," +
//			"case when c.课程类型='1' then '普通课程' when c.课程类型='2' then '添加课程' when c.课程类型='3' then '选修课程' else c.课程类型 end as 课程类型," +
//			"case when d.年级代码=(cast(cast(right('" + MyTools.fixSql(XN_CX) + "',2) as int)-2 as varchar)+'1') then " +
//			"(case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) " +
//			"else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
//			"from V_成绩管理_学生成绩信息表 a " +
//			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
//			"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
//			"left join V_成绩管理_大补考登分教师信息表 e on e." +
//			"where b.学生状态 in ('01','05','08') and a.成绩状态='1' and left(c.学年学期编码,4)<>'" + MyTools.fixSql(XN_CX) + "' " +
//			"and d.年级代码 in (cast(cast(right('" + MyTools.fixSql(XN_CX) + "',2) as int)-2 as varchar)+'1')";
//			"cast(cast(right('" + MyTools.fixSql(XN_CX) + "',2) as int)-3 as varchar)+'1'," +
//			"cast(cast(right('" + MyTools.fixSql(XN_CX) + "',2) as int)-4 as varchar)+'1')";
		
		//20170228添加学年范围判断（管理员无限制）
		if(this.getAUTH().indexOf(admin) < 0){
			sql = "select 大补考学年范围 from V_成绩管理_登分时间表 " +
				"where 学年学期编码=(select top 1 学年学期编码 from V_规则管理_学年学期表 " +
				"where convert(nvarchar(10),getDate(),21)>convert(nvarchar(10),学期开始时间,21) order by 学年 desc,学期 desc)";
			vec = db.GetContextVector(sql);
			if(vec!=null && vec.size()>0){
				if(!"".equalsIgnoreCase(MyTools.StrFiltr(vec.get(0)))){
					yearRange = MyTools.StrFiltr(vec.get(0));
				}
			}
		}
		
		sql = "select distinct z.大补考学年,z.课程名称,z.课程类型," +
			"case when (select count(*) from V_成绩管理_登分时间表 where 学年学期编码 like z.大补考学年+'%' " + 
			"and convert(nvarchar(10),getDate(),21) between convert(nvarchar(10),大补考开始时间,21) and convert(nvarchar(10),大补考结束时间,21))>0 then 'true' else 'false' end as 登分时间 " +
			"from (" +
			"select distinct '" + MyTools.fixSql(XN_CX) + "' as 大补考学年,case when 课程类型='3' then '' else 专业代码 end as 专业代码,课程名称," +
			"case 课程类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as 课程类型 from (" +
			"select left(c.学年学期编码,5) as 学年学期,a.学号,a.姓名,d.行政班名称,c.专业代码," +
			//"case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end as 课程名称," +
			"c.课程名称,c.课程类型," +
			"case when d.年级代码=(cast(cast(right('" + MyTools.fixSql(XN_CX) + "',2) as int)-2 as varchar)+'1') " +
			"then (case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) " +
			"else (case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) end as 成绩 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
			"where b.学生状态 in ('01','05','08') and a.成绩状态='1'";
			//20170228修改yeq 添加学期范围判断
			if("1".equalsIgnoreCase(yearRange)){
				sql += " and left(c.学年学期编码,4)<>'" + MyTools.fixSql(XN_CX) + "'";
			}else if("2".equalsIgnoreCase(yearRange)){
				sql += " and left(c.学年学期编码,4)='" + MyTools.fixSql(XN_CX) + "'";
			}
		sql += " and d.年级代码 in (cast(cast(right('" + MyTools.fixSql(XN_CX) + "',2) as int)-2 as varchar)+'1')";
		//判断查询条件
		if(!"".equalsIgnoreCase(KCLX_CX)){
			sql += " and c.课程类型='" + MyTools.fixSql(KCLX_CX) + "'";
		}
		sql += ") as t where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')";
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql += " and 课程名称 like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		sql += ") as z " +
			"left join V_成绩管理_大补考登分教师信息表 y on y.大补考学年=z.大补考学年 and y.专业代码=z.专业代码 and y.课程名称=z.课程名称 " +
			"left join V_专业基本信息数据子类 x on x.专业代码=y.专业代码 " +
			"left join V_学校班级数据子类 w on w.行政班代码=y.专业代码 " +
			"left join V_基础信息_教学班信息表 v on v.教学班编号=y.专业代码 " +
			"where 1=1";
		
		//判断权限
		if(this.getAUTH().indexOf(admin) < 0){
			sql += " and (y.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or w.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"v.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			
			if(this.getAUTH().indexOf(majorTeacher) > -1){
				sql += " or x.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
			}
			sql += ")";
		}
		sql += " order by z.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取文字成绩下拉框数据
	 * @date:2016-02-06
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadWzcjCombo() throws SQLException{
		Vector vec = null;
		String sql = "select * from (select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 名称 as comboName,代码 as comboValue from V_成绩管理_文字成绩代码表 " +
				"where 状态='1' and 代码 in ('-4','-9','-10')) as t " +
				"order by cast(comboValue as int) desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取文字成绩显示内容
	 * @date:2016-11-03
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadWzcjShowCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct cast(代码 as int) as id,名称 as text from V_成绩管理_文字成绩代码表 where 状态='1' order by cast(代码 as int) desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2016-11-03
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年 as comboName,学年 as comboValue from V_规则管理_学年学期表 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程的学生列表
	 * @date:2016-08-04
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadStuList(String dbkxn, String kcmc)throws SQLException{
		String result = "[";
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String yearRange = "0";
//		sql = "select a.学号,a.姓名,a.总评,重修1,重修2,补考,大补考 from V_成绩管理_学生成绩信息表 a " +
//			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' " +
//			"and b.学生状态 in ('01','05') and a.成绩状态='1' and cast(a.总评 as float)<60.0 " +
//			"and a.总评 not in('-1','-6','-7','-8','-9','-11','-13','-15') order by a.学号";
//		sql = "select distinct 相关编号,学号,姓名,行政班名称,学年学期,课程名称,大补考 from (" +
//			"select a.相关编号,a.学号,a.姓名,d.行政班名称,left(c.学年学期编码,5) as 学年学期,c.课程名称," +
//			"case when c.课程类型='1' then '普通课程' when c.课程类型='2' then '添加课程' when c.课程类型='3' then '选修课程' else c.课程类型 end as 课程类型," +
//			"(case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) as 成绩,大补考 " +
//			"from V_成绩管理_学生成绩信息表 a " +
//			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
//			"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
//			"where b.学生状态 in ('01','05','08') and a.成绩状态='1' and left(c.学年学期编码,4)<>'" + MyTools.fixSql(dbkxn) + "' " +
//			"and d.年级代码 in (cast(cast(right('" + MyTools.fixSql(dbkxn) + "',2) as int)-2 as varchar)+'1') " +
//			"and c.课程名称='" + MyTools.fixSql(kcmc) + "') as t " +
//			"where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15') order by 学年学期,行政班名称,学号";
		
		//20170228添加学年范围判断（管理员无限制）
		if(this.getAUTH().indexOf(admin) < 0){
			sql = "select 大补考学年范围 from V_成绩管理_登分时间表 " +
				"where 学年学期编码=(select top 1 学年学期编码 from V_规则管理_学年学期表 " +
				"where convert(nvarchar(10),getDate(),21)>convert(nvarchar(10),学期开始时间,21) order by 学年 desc,学期 desc)";
			vec = db.GetContextVector(sql);
			if(vec!=null && vec.size()>0){
				if(!"".equalsIgnoreCase(MyTools.StrFiltr(vec.get(0)))){
					yearRange = MyTools.StrFiltr(vec.get(0));
				}
			}
		}
		
		sql = "select distinct z.相关编号,ROW_NUMBER() over (order by 学年学期,行政班名称,学号) as num,z.学号,z.姓名,z.行政班名称,z.学年学期,z.课程名称,z.大补考 from (" +
			"select distinct 相关编号,学号,姓名,行政班名称,行政班简称,学年学期,课程名称,大补考,'" + MyTools.fixSql(dbkxn) + "' as 大补考学年," +
			"case when 课程类型='3' then '' else 专业代码 end as 专业代码 from (" +
			"select a.相关编号,a.学号,a.姓名,c.专业代码,d.行政班名称,d.行政班简称,left(c.学年学期编码,5) as 学年学期,c.课程名称,c.课程类型," +
			"(case when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end) as 成绩,大补考 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=a.科目编号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=b.行政班代码 " +
			"where b.学生状态 in ('01','05','08') and a.成绩状态='1'";
			//20170228修改yeq 添加学期范围判断
			if("1".equalsIgnoreCase(yearRange)){
				sql += " and left(c.学年学期编码,4)<>'" + MyTools.fixSql(dbkxn) + "'";
			}else if("2".equalsIgnoreCase(yearRange)){
				sql += " and left(c.学年学期编码,4)='" + MyTools.fixSql(dbkxn) + "'";
			}
			sql += " and d.年级代码 in (cast(cast(right('" + MyTools.fixSql(dbkxn) + "',2) as int)-2 as varchar)+'1') " +
			//"and (case when isnumeric(right(c.课程名称,1))=1 then rtrim(substring(c.课程名称, 0, len(c.课程名称))) else c.课程名称 end)='" + MyTools.fixSql(kcmc) + "') as t " +
			"and c.课程名称='" + MyTools.fixSql(kcmc) + "') as t " +
			"where cast(成绩 as float)<60.0 and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')) as z " +
			"left join V_成绩管理_大补考登分教师信息表 y on y.大补考学年=z.大补考学年 and y.专业代码=z.专业代码 and y.课程名称=z.课程名称 " +
			"where 1=1";
		if(this.getAUTH().indexOf(admin) < 0){
			sql += " and y.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
		}
		sql += " order by 学年学期,行政班名称,学号";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=8){
				result += "{\"xgbh\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
						"\"num\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
						"\"stuCode\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
						"\"stuName\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
						"\"className\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"," +
						"\"xnxq\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"," +
						"\"kcmc\":\"" + MyTools.StrFiltr(vec.get(i+6)) + "\"," +
						"\"dbk\":\"" + MyTools.StrFiltr(vec.get(i+7)) + "\"},";
			}
		}
		
		if(result.length() > 1)
			result = result.substring(0, result.length()-1);
		result += "]";
		return result;
	}
	
	/**
	 * 保存成绩
	 * @date:2016-02-15
	 * @author:yeq
	 * @param updateInfo 更新的成绩信息
	 * @throws SQLException
	 */
	public void saveStuScore(String updateInfo)throws SQLException{
		Vector sqlVec = new Vector();
		String sql = "";
		String[] updateArray = updateInfo.split(",", -1);
		
		for(int i=0; i<updateArray.length; i+=3){
			sql = "update V_成绩管理_学生成绩信息表 set " +
				"大补考='" + MyTools.fixSql(updateArray[i+2]) + "' " +
				"where 相关编号='" + MyTools.fixSql(updateArray[i+1]) + "' " +
				"and 学号='" + MyTools.fixSql(updateArray[i]) + "'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
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
	
	public String getCX_ID() {
		return CX_ID;
	}

	public void setCX_ID(String cX_ID) {
		CX_ID = cX_ID;
	}

	public String getCX_XH() {
		return CX_XH;
	}

	public void setCX_XH(String cX_XH) {
		CX_XH = cX_XH;
	}

	public String getCX_XM() {
		return CX_XM;
	}

	public void setCX_XM(String cX_XM) {
		CX_XM = cX_XM;
	}

	public String getCX_XGBH() {
		return CX_XGBH;
	}

	public void setCX_XGBH(String cX_XGBH) {
		CX_XGBH = cX_XGBH;
	}

	public String getCX_ZP() {
		return CX_ZP;
	}

	public void setCX_ZP(String cX_ZP) {
		CX_ZP = cX_ZP;
	}

	public String getCX_CX1() {
		return CX_CX1;
	}

	public void setCX_CX1(String cX_CX1) {
		CX_CX1 = cX_CX1;
	}

	public String getCX_CX2() {
		return CX_CX2;
	}

	public void setCX_CX2(String cX_CX2) {
		CX_CX2 = cX_CX2;
	}

	public String getCX_BK() {
		return CX_BK;
	}

	public void setCX_BK(String cX_BK) {
		CX_BK = cX_BK;
	}

	public String getCX_DBK() {
		return CX_DBK;
	}

	public void setCX_DBK(String cX_DBK) {
		CX_DBK = cX_DBK;
	}

	public String getCX_DYCJ() {
		return CX_DYCJ;
	}

	public void setCX_DYCJ(String cX_DYCJ) {
		CX_DYCJ = cX_DYCJ;
	}

	public String getCX_CJZT() {
		return CX_CJZT;
	}

	public void setCX_CJZT(String cX_CJZT) {
		CX_CJZT = cX_CJZT;
	}

	public String getCX_CJR() {
		return CX_CJR;
	}

	public void setCX_CJR(String cX_CJR) {
		CX_CJR = cX_CJR;
	}

	public String getCX_CJSJ() {
		return CX_CJSJ;
	}

	public void setCX_CJSJ(String cX_CJSJ) {
		CX_CJSJ = cX_CJSJ;
	}

	public String getCX_ZT() {
		return CX_ZT;
	}

	public void setCX_ZT(String cX_ZT) {
		CX_ZT = cX_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}