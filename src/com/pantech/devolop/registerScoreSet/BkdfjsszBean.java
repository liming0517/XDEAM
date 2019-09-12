package com.pantech.devolop.registerScoreSet;
/*
@date 2016.08.03
@author yeq
模块：M6.6 补考登分教师设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class BkdfjsszBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	private String CD_ID; //编号
	private String CD_KMBH; //科目编号
	private String CD_LYLX; //来源类型
	private String CD_XGBH; //相关编号
	private String CD_XZBDM; //行政班代码
	private String CD_XZBMC; //行政班名称
	private String CD_KCDM; //课程代码
	private String CD_KCMC; //课程名称
	private String CD_DFJSBH; //登分教师编号
	private String CD_DFJSXM; //登分教师姓名
	private String CD_CJR; //创建人
	private String CD_CJSJ; //创建时间
	private String CD_ZT; //状态

	private String CX_XH; //学号
	private String CX_XM; //姓名

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public BkdfjsszBean(HttpServletRequest request) {
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
		CD_ID = ""; //编号
		CD_KMBH = ""; //科目编号
		CD_LYLX = ""; //来源类型
		CD_XGBH = ""; //相关编号
		CD_XZBDM = ""; //行政班代码
		CD_XZBMC = ""; //行政班名称
		CD_KCDM = ""; //课程代码
		CD_KCMC = ""; //课程名称
		CD_DFJSBH = ""; //登分教师编号
		CD_DFJSXM = ""; //登分教师姓名
		CD_CJR = ""; //创建人
		CD_CJSJ = ""; //创建时间
		CD_ZT = ""; //状态
		CX_XH = ""; //学号
		CX_XM = ""; //姓名
	}

	/**
	 * 分页查询 课程列表
	 * @date:2016-02-01
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XNXQMC_CX 学年学期名称
	 * @param JXXZ_CX 教学性质
	 * @param ZYMC_CX 专业名称
	 * @param XZBMC_CX 行政班名称
	 * @param KCMC_CX 课程名称
	 * @param KCLX_CX 课程类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	/*
	public Vector queCourseList(int pageNum, int pageSize, String XNXQMC_CX, String JXXZ_CX, String ZYMC_CX, String XZBMC_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select a.科目编号,a.相关编号,c.学年学期名称,d.教学性质,b.专业名称,a.行政班代码,a.行政班名称,a.课程名称," +
			"replace(a.登分教师编号,'@','') as 登分教师编号,replace(a.登分教师姓名,'@','') as 登分教师姓名," +
			"case a.来源类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as 课程类型 " +
			"from V_成绩管理_补考登分教师信息表 a " +
			"inner join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 c on c.学年学期编码=b.学年学期编码 " +
			"left join V_基础信息_教学性质 d on d.编号=c.教学性质 " +
			"left join V_学校班级数据子类 e on e.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 f on f.教学班编号=a.行政班代码 " +
			"where (select count(*) from V_成绩管理_学生成绩信息表 t left join V_学生基本数据子类 t1 on t1.学号=t.学号 " +
			"where t.成绩状态='1' and t1.学生状态 in ('01','05') and t.相关编号=a.相关编号 " +
			"and cast(总评 as float)<60.0 and 总评 not in ('-1','-6','-7','-8','-9','-11','-13','-15'))>0";
		
		//判断权限
		if(this.getAUTH().indexOf(admin) < 0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			
			if(this.getAUTH().indexOf(majorTeacher) > -1){
				sql += " or b.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
			}
			sql += ")";
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(XNXQMC_CX)){
			sql += " and left(c.学年学期编码,5)='" + MyTools.fixSql(XNXQMC_CX) + "'";
		}
		if(!"".equalsIgnoreCase(JXXZ_CX)){
			sql += " and c.教学性质='" + MyTools.fixSql(JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(ZYMC_CX)){
			sql += " and b.专业名称 like '%" + MyTools.fixSql(ZYMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(XZBMC_CX)){
			sql += " and a.行政班名称 like '%" + MyTools.fixSql(XZBMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql += " and b.课程名称 like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCLX_CX)){
			sql += " and a.来源类型='" + MyTools.fixSql(KCLX_CX) + "'";
		}
		sql += " order by c.学年学期编码 desc,b.专业代码,a.行政班代码,a.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	*/
	
	/**
	 * 分页查询 课程列表
	 * @date:2017-06-13
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XN_CX 学年
	 * @param XBDM_CX 系部代码
	 * @param ZYDM_CX 专业代码
	 * @param NJDM_CX 年级代码
	 * @param KCMC_CX 课程名称
	 * @param KCLX_CX 课程类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queCourseList(int pageNum, int pageSize, String XN_CX, String XBDM_CX, String ZYDM_CX, String NJDM_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "with tempClassInfo as (" +
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
			"from V_学校班级数据子类 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"union all " +
			"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
			"from V_基础信息_教学班信息表 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"union all " +
			"select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号 as 班主任工号 from V_规则管理_选修课授课计划明细表) " +
			"select t.编号,t.学年,t.班级名称,t.年级名称,t.系部名称,t.专业名称,t.课程名称,replace(t.登分教师编号,'@','') as 登分教师编号,t.登分教师姓名,t.课程类型 " +
			"from (select a.编号,a.学年,a.班级代码,a.班级名称,b.年级代码,b.年级名称,b.系部代码,b.系部名称,b.专业代码,b.专业名称,a.课程名称,a.登分教师编号,replace(a.登分教师姓名,'@','') as 登分教师姓名," +
			"a.来源类型,case a.来源类型 when '1' then '普通课程' when '2' then '添加课程' when '3' then '选修课程' when '4' then '分层课程' else '未知' end as 课程类型 " +
			"from V_成绩管理_补考登分教师信息表 a " +
			"left join tempClassInfo b on b.班级代码=a.班级代码) as t where 1=1";
		
		//判断权限
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			if(this.getAUTH().indexOf(majorTeacher) > -1){
				sql += " or b.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
			}
			sql += ")";
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(XN_CX)){
			sql += " and t.学年='" + MyTools.fixSql(XN_CX) + "'";
		}
		if(!"".equalsIgnoreCase(XBDM_CX)){
			sql += " and t.系部代码='" + MyTools.fixSql(XBDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(ZYDM_CX)){
			sql += " and t.专业代码='" + MyTools.fixSql(ZYDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and t.年级代码='" + MyTools.fixSql(NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql += " and t.课程名称 like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCLX_CX)){
			sql += " and t.来源类型='" + MyTools.fixSql(KCLX_CX) + "'";
		}
		sql += " order by t.学年 desc,t.专业代码,t.班级代码,t.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 初始化补考登分信息
	 * @date：2017-06-12
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void initBkCourseInfo() throws SQLException{
		String sql = "";
		Vector vec = null;
		String curXn = "";
		
		//查询当前学年
		sql = "select top 1 学年 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			curXn = MyTools.StrFiltr(vec.get(0));
			
			sql = "with tempClassInfo as (" +
				"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
				"from V_学校班级数据子类 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
				"union all " +
				"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 " +
				"from V_基础信息_教学班信息表 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
				"union all " +
				"select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号 as 班主任工号 " +
				"from V_规则管理_选修课授课计划明细表)" +
				"insert into V_成绩管理_补考登分教师信息表 (学年,来源类型,班级代码,班级名称,课程代码,课程名称,登分教师编号,登分教师姓名,创建人,创建时间,状态) " +
				"select distinct t.学年,t.来源类型,t.班级代码,t.班级名称,t.课程代码,t.课程名称," +
				"case t.来源类型 when '3' then (select top 1 t1.登分教师编号 from V_成绩管理_登分教师信息表 t1 left join V_成绩管理_科目课程信息表 t2 on t2.科目编号=t1.科目编号 " +
				"where left(t2.学年学期编码,4)='" + MyTools.fixSql(curXn) + "' and t1.课程代码=t.课程代码 and t1.相关编号=t.班级代码 order by t2.学年学期编码 desc) " +
				"else (select top 1 t1.登分教师编号 from V_成绩管理_登分教师信息表 t1 left join V_成绩管理_科目课程信息表 t2 on t2.科目编号=t1.科目编号 " +
				"where left(t2.学年学期编码,4)='" + MyTools.fixSql(curXn) + "' and t1.课程代码=t.课程代码 and t1.行政班代码=t.班级代码 order by t2.学年学期编码 desc) end as 登分教师编号," +
				"case t.来源类型 when '3' then (select top 1 t1.登分教师姓名 from V_成绩管理_登分教师信息表 t1 left join V_成绩管理_科目课程信息表 t2 on t2.科目编号=t1.科目编号 " +
				"where left(t2.学年学期编码,4)='" + MyTools.fixSql(curXn) + "' and t1.课程代码=t.课程代码 and t1.相关编号=t.班级代码 order by t2.学年学期编码 desc) " +
				"else (select top 1 t1.登分教师姓名 from V_成绩管理_登分教师信息表 t1 left join V_成绩管理_科目课程信息表 t2 on t2.科目编号=t1.科目编号 " +
				"where left(t2.学年学期编码,4)='" + MyTools.fixSql(curXn) + "' and t1.课程代码=t.课程代码 and t1.行政班代码=t.班级代码 order by t2.学年学期编码 desc) end as 登分教师姓名," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "',getDate(),'1' from (" +
				"select c.学年,a.来源类型,case a.来源类型 when '3' then e.班级代码 else d.班级代码 end as 班级代码," +
				"case a.来源类型 when '3' then e.班级名称 else d.班级名称 end as 班级名称,b.课程代码,b.课程名称 " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_规则管理_学年学期表 c on c.学年学期编码=b.学年学期编码 " +
				"left join tempClassInfo d on d.班级代码=a.行政班代码 " +
				"left join tempClassInfo e on e.班级代码=a.相关编号" +
				") as t " +
				"left join V_成绩管理_补考登分教师信息表 z on z.学年=t.学年 and z.来源类型=t.来源类型 and z.班级代码=t.班级代码 and z.课程代码=t.课程代码 " +
				"where t.学年='" + MyTools.fixSql(curXn) + "' " +
				//20170907添加选修课过滤条件yeq
				"and t.来源类型 in ('1','2') " +
				"and z.编号 is null " +
				"order by t.学年,t.班级代码,t.课程代码";
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("初始化成功");
			}else{
				this.setMSG("初始化失败");
			}
		}
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2017-06-13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年 as comboName,学年 as comboValue,1 as orderNum " +
				"from V_规则管理_学年学期表 order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2016-02-01
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
	 * 读取系部下拉框
	 * @date:2017-04-13
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
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取行政班下拉框
	 * @date:2016-02-01
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadClassCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 行政班名称 as comboName,行政班代码 as comboValue " +
				"from V_学校班级数据子类 where 行政班代码 not like '%_0'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程下拉框
	 * @date:2016-02-01
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
	 * 查询教师列表
	 * @date:2016-02-01
	 * @author:yeq
	 * @param DFJSBH_CX 教师编号
	 * @param DFJSMC_CX 教师名称
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadTeaList(int pageNum, int pageSize, String DFJSBH_CX, String DFJSMC_CX) throws SQLException{
		Vector vec = null;
		String sql = "";
		
		//获取所有教师信息
		sql = "select * from (select distinct top 100 percent 工号,姓名 from V_教职工基本数据子类 " +
			"where 工号 in ('" + this.getCD_DFJSBH().replaceAll(",", "','") + "') order by 工号) as t1 " +
			"union all " +
			"select * from (select distinct top 100 percent 工号,姓名 from V_教职工基本数据子类 " +
			"where 工号 not in ('" + this.getCD_DFJSBH().replaceAll(",", "','") + "')";
		//判断查询条件
		if(!"".equalsIgnoreCase(DFJSBH_CX)){
			sql += " and 工号 like '%" + MyTools.fixSql(DFJSBH_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(DFJSMC_CX)){
			sql += " and 姓名 like '%" + MyTools.fixSql(DFJSMC_CX) + "%'";
		}
		sql += " order by 工号) as t2";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	/**
	 * 保存登分教师
	 * @date:2016-02-01
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveTea()throws SQLException{
		String sql = "update V_成绩管理_补考登分教师信息表 set " +
				"登分教师编号='" + MyTools.fixSql("@"+this.getCD_DFJSBH().replaceAll(",","@,@")+"@") + "'," +
				"登分教师姓名='" + MyTools.fixSql("@"+this.getCD_DFJSXM().replaceAll(",","@,@")+"@") + "' " +
				"where 编号='" + MyTools.fixSql(this.getCD_XGBH()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
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

	public String getCD_ID() {
		return CD_ID;
	}

	public void setCD_ID(String cD_ID) {
		CD_ID = cD_ID;
	}

	public String getCD_KMBH() {
		return CD_KMBH;
	}

	public void setCD_KMBH(String cD_KMBH) {
		CD_KMBH = cD_KMBH;
	}

	public String getCD_LYLX() {
		return CD_LYLX;
	}

	public void setCD_LYLX(String cD_LYLX) {
		CD_LYLX = cD_LYLX;
	}

	public String getCD_XGBH() {
		return CD_XGBH;
	}

	public void setCD_XGBH(String cD_XGBH) {
		CD_XGBH = cD_XGBH;
	}

	public String getCD_XZBDM() {
		return CD_XZBDM;
	}

	public void setCD_XZBDM(String cD_XZBDM) {
		CD_XZBDM = cD_XZBDM;
	}

	public String getCD_XZBMC() {
		return CD_XZBMC;
	}

	public void setCD_XZBMC(String cD_XZBMC) {
		CD_XZBMC = cD_XZBMC;
	}

	public String getCD_KCDM() {
		return CD_KCDM;
	}

	public void setCD_KCDM(String cD_KCDM) {
		CD_KCDM = cD_KCDM;
	}

	public String getCD_KCMC() {
		return CD_KCMC;
	}

	public void setCD_KCMC(String cD_KCMC) {
		CD_KCMC = cD_KCMC;
	}

	public String getCD_DFJSBH() {
		return CD_DFJSBH;
	}

	public void setCD_DFJSBH(String cD_DFJSBH) {
		CD_DFJSBH = cD_DFJSBH;
	}

	public String getCD_DFJSXM() {
		return CD_DFJSXM;
	}

	public void setCD_DFJSXM(String cD_DFJSXM) {
		CD_DFJSXM = cD_DFJSXM;
	}

	public String getCD_CJR() {
		return CD_CJR;
	}

	public void setCD_CJR(String cD_CJR) {
		CD_CJR = cD_CJR;
	}

	public String getCD_CJSJ() {
		return CD_CJSJ;
	}

	public void setCD_CJSJ(String cD_CJSJ) {
		CD_CJSJ = cD_CJSJ;
	}

	public String getCD_ZT() {
		return CD_ZT;
	}

	public void setCD_ZT(String cD_ZT) {
		CD_ZT = cD_ZT;
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

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}