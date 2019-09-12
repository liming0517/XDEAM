package com.pantech.devolop.registerScoreManage;
/*
@date 2016.08.04
@author yeq
模块：M7.2 补考成绩登记
说明:
重要及特殊方法：
*/
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class BkcjdjBean {
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
	public BkcjdjBean(HttpServletRequest request) {
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
	 * 分页查询 科目列表
	 * @date:2016-08-04
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
	public Vector queSubjectList(int pageNum, int pageSize, String XN_CX, String XBDM_CX, String ZYDM_CX, String NJDM_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "with tempClassInfo as (select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称," +
			"d.专业代码,d.专业名称,a.班主任工号 from V_学校班级数据子类 a " +
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
			"select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号  as 班主任工号  from V_规则管理_选修课授课计划明细表 ) " +
			"select distinct t.学年,t.年级代码,t.年级名称,t.系部代码,t.系部名称,t.专业代码,t.专业名称,t.课程代码,t.课程名称,t.来源类型,t.课程类型," +
			"case when (select count(*) from V_成绩管理_登分时间表 where left(学年学期编码,4)=t.学年 and (convert(nvarchar(10),getDate(),21) between convert(nvarchar(10),补考开始时间,21) and convert(nvarchar(10),补考结束时间,21)))>0 then 'true' else 'false' end as 登分时间 " +
			"from (" +
			"select a.学年,b.年级代码,b.年级名称,b.系部代码,b.系部名称,b.专业代码,b.专业名称,a.班级代码,a.班级名称,a.来源类型," +
			"case a.来源类型 when '1' then '普通课程' when '2' then '添加课程' when '3' then '选修课程' when '4' then '分层课程' else '未知' end as 课程类型," +
			"a.课程代码,a.课程名称,a.登分教师编号,b.班主任工号 " +
			"from V_成绩管理_补考登分教师信息表 a " +
			"left join tempClassInfo b on b.班级代码=a.班级代码" +
			") as t where 1=1";
		
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
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
			sql += " and t.专业代码 like '%" + MyTools.fixSql(ZYDM_CX) + "%'";
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
		sql += " order by t.学年 desc,t.年级名称,t.系部名称,t.专业名称,t.课程名称";
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
	 * 查询所选课程所有相关班级信息
	 * @date:2017-06-14
	 * @author:yeq
	 * @param xn 学年
	 * @param xbdm 系部代码
	 * @param zydm 专业代码
	 * @param njdm 年级代码
	 * @param kcdm 课程代码
	 * @return 班级信息
	 * @throws SQLException
	 */
	public Vector queClassTree(String xn, String xbdm, String zydm, String njdm, String kcdm)throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
//		sql = "select a.相关编号 as id,case a.来源类型 when '1' then a.行政班名称+' '+a.课程名称 " +
//			"when '2' then a.行政班名称+' '+a.课程名称+'(添加课程)' " +
//			"when '3' then a.行政班名称 " +
//			"when '4' then a.行政班名称+' '+a.课程名称 else a.课程名称 end as text,state='open' " +
//			"from V_成绩管理_补考登分教师信息表 a " +
//			"left join (select t1.相关编号,count(*) as 不及格人数 from V_成绩管理_学生成绩信息表 t1 left join V_学生基本数据子类 t2 on t2.学号=t1.学号 " +
//			"where t2.学生状态 in ('01','05') and t1.成绩状态='1' and cast(t1.总评 as float)<60.0 " +
//			"and t1.总评 not in ('-1','-6','-7','-8','-9','-11','-13','-15') group by t1.相关编号) as b on b.相关编号=a.相关编号 " +
//			"where a.科目编号='" + MyTools.fixSql(this.getCX_ID()) + "' and b.不及格人数>0";
//		if(this.getAUTH().indexOf(admin) < 0){
//			sql += " and a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
//		}
		
		sql = "with tempClassInfo as (select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称," +
			"d.专业代码,d.专业名称,a.班主任工号 from V_学校班级数据子类 a " +
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
			"select 授课计划明细编号 as 班级代码,选修班名称 as 班级名称,'','','','','','',授课教师编号  as 班主任工号  from V_规则管理_选修课授课计划明细表 ) " +
			"select distinct t.编号 as id,t.班级名称 as text,state='open' " +
			"from (" +
			"select a.编号,a.学年,b.年级代码,b.年级名称,b.系部代码,b.系部名称,b.专业代码,b.专业名称,a.班级代码,a.班级名称,a.来源类型," +
			"case a.来源类型 when '1' then '普通课程' when '2' then '添加课程' when '3' then '选修课程' when '4' then '分层课程' else '未知' end as 课程类型," +
			"a.课程代码,a.课程名称,a.登分教师编号,b.班主任工号 " +
			"from V_成绩管理_补考登分教师信息表 a " +
			"left join tempClassInfo b on b.班级代码=a.班级代码" +
			") as t where 1=1 and t.学年='" + MyTools.fixSql(xn) + "' " +
			"and t.专业代码='" + MyTools.fixSql(zydm) + "' " +
			"and t.系部代码='" + MyTools.fixSql(xbdm) + "' " +
			"and t.年级代码='" + MyTools.fixSql(njdm) + "' " +
			"and t.课程代码='" + MyTools.fixSql(kcdm) + "'";
		
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (t.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"t.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += " order by t.班级名称";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询科目相关信息
	 * @date:2016-08-04
	 * @author:yeq
	 * @param xn 学年
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadSubjectInfo(String xn)throws SQLException{
		Vector vec = null;
		String sql = "select case when (select count(*) from V_成绩管理_登分时间表 where left(学年学期编码,4)='" + MyTools.fixSql(xn) + "' " +
				"and (convert(nvarchar(10),getDate(),21) between convert(nvarchar(10),补考开始时间,21) and convert(nvarchar(10),补考结束时间,21)))>0 then 'true' else 'false' end as 登分时间";
		vec = db.GetContextVector(sql);
		return vec;
	}
	
	/**
	 * 查询登分配置信息
	 * @date:2016-08-31
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadDfConfig()throws SQLException{
		String sql = "";
		Vector vec = null;
		
		sql = "select 考试类型,总评比例选项,平时比例,期中比例,实训比例,期末比例,成绩类型,case when 成绩类型='1' then '数字成绩' else '文字成绩' end as 成绩类型名称," +
			"实训,case when 实训='0' then '无' else '有' end as 实训 from V_成绩管理_登分设置信息表 where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
		vec = db.GetContextVector(sql);
		
		return vec;
	}
	
	/**
	 * 初始化学生补考成绩信息
	 * @date:2017-06-14
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void initStuBkInfo() throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		Vector bkVec = null;
		Vector tempVec = null;
		boolean existFlag = false;
		String tempStuCode = "";
		String tempXnzp = "";
		
		//获取最新补考名单
		bkVec = loadCourseBkmd();
				
		//查询原补考信息
		sql = "select a.编号,a.学号,a.学年总评 from V_成绩管理_学生补考成绩信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"where b.学生状态 in ('01','05','07','08') and a.补考编号='" + MyTools.fixSql(this.getCX_ID()) + "' " +
			"order by b.班内学号";
		tempVec = db.GetContextVector(sql);
		
		if(bkVec!=null && bkVec.size()>0){
			//校验补考信息是否与当前补考名单相符
			if(tempVec!=null && tempVec.size()>0){
				//1、判断是否有需要删除的数据（不需要补考）、并比较学年总评数据是否需要更新
				for(int i=0; i<tempVec.size(); i+=3){
					existFlag = false;
					tempStuCode = MyTools.StrFiltr(tempVec.get(i+1));
					tempXnzp = MyTools.StrFiltr(tempVec.get(i+2));
					
					for(int j=0; j<bkVec.size(); j+=3){
						if(tempStuCode.equalsIgnoreCase(MyTools.StrFiltr(bkVec.get(j)))){
							existFlag = true;
							
							//判断是否需要更新学年总评数据
							if(!tempXnzp.equalsIgnoreCase(MyTools.StrFiltr(bkVec.get(j+2)))){
								sql = "update V_成绩管理_学生补考成绩信息表 set 学年总评='" + MyTools.StrFiltr(bkVec.get(j+2)) + "' " +
									"where 编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i))) + "'";
								sqlVec.add(sql);
							}
							
							//移除当前补考名单中已经存在补考信息的学生数据
							for(int k=0; k<3; k++){
								bkVec.remove(j);
							}
							
							break;
						}
					}
					
					if(existFlag == false){
						sql = "delete from V_成绩管理_学生补考成绩信息表 where 编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i))) + "'";
						sqlVec.add(sql);
					}
				}
			}
			
			//2、插入新增的学生补考基础信息
			for(int i=0; i<bkVec.size(); i+=3){
				sql = "insert into V_成绩管理_学生补考成绩信息表 (学号,姓名,补考编号,学年总评,创建人,创建时间,状态) values (" +
					"'" + MyTools.fixSql(MyTools.StrFiltr(bkVec.get(i))) + "'," +
					"'" + MyTools.fixSql(MyTools.StrFiltr(bkVec.get(i+1))) + "'," +
					"'" + MyTools.fixSql(this.getCX_ID()) + "'," +
					"'" + MyTools.fixSql(MyTools.StrFiltr(bkVec.get(i+2))) + "'," +
					"'" + MyTools.fixSql(this.getUSERCODE()) + "',getDate(),'1')";
				sqlVec.add(sql);
			}
		}else{
			sql = "delete from V_成绩管理_学生补考成绩信息表 where 补考编号='" + MyTools.fixSql(this.getCX_ID()) + "'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("初始化成功");
		}else{
			this.setMSG("初始化失败");
		}
	}
	
	/**
	 * @date:2017-06-14
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException 
	 */
	public Vector loadCourseBkmd() throws SQLException{
		Vector resultVec = new Vector();
		String sql = "";
		Vector tempVec = null;
		String year = "";
		String classCode = "";
		String courseCode = "";
		String courseType = "";
		Vector xqzbVec = new Vector();//学期占比设置的信息
		String firstSemPercent = "";
		String secondSemPercent = "";
		String countType = "";
		String firstSem = "";//上学期
		String secondSem = "";//下学期
		Vector stuVec = null;//学生信息
		Vector scoreInfoVec = null;//学生成绩信息
		Vector testScoreInfoVec = null;//学业水平测试成绩
		String stuCode = "";
		String stuName = "";
		boolean firstFlag = false;
		String firstScore = "";
		boolean secondFlag = false;
		String secondScore = "";
		String tempXnzp = "";
		
		//读取需要查询的补考相关信息
		sql = "select 学年,班级代码,课程代码,来源类型 from V_成绩管理_补考登分教师信息表 where 编号='" + MyTools.fixSql(this.getCX_ID()) + "'";
		tempVec = db.GetContextVector(sql);
		
		if(tempVec!=null && tempVec.size()>0){
			year = MyTools.StrFiltr(tempVec.get(0));
			classCode = MyTools.StrFiltr(tempVec.get(1));
			courseCode = MyTools.StrFiltr(tempVec.get(2));
			courseType = MyTools.StrFiltr(tempVec.get(3));
			
			DecimalFormat f = new DecimalFormat("#");  
			f.setRoundingMode(RoundingMode.HALF_UP);
			
			//查询学年总评设置信息
			firstSemPercent = "40";
			secondSemPercent = "60";
			countType = "1";
			//判断课程类型(1.普通课程/2.添加课程/3.选修课程/4.分层课程)
			if("1".equalsIgnoreCase(courseType) || "2".equalsIgnoreCase(courseType)){
				sql = "select top 1 学期一,学期二,计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码=";
				if(classCode.indexOf("JXB_") < 0){
					sql += "(select 系部代码 from V_学校班级数据子类 where 行政班代码='" + MyTools.fixSql(classCode) + "')";
				}else{
					sql += "(select 系部代码 from V_基础信息_教学班信息表 where 教学班编号='" + MyTools.fixSql(classCode) + "')";
				}
				xqzbVec= db.GetContextVector(sql);
				
				//判断计算方式		
				if(xqzbVec!=null && xqzbVec.size()>0){
					firstSemPercent = MyTools.StrFiltr(xqzbVec.get(0));
					secondSemPercent = MyTools.StrFiltr(xqzbVec.get(1));
					countType = MyTools.StrFiltr(xqzbVec.get(2));
				}
			}
			
			//判断计算方式
			if("2".equalsIgnoreCase(countType)){//跨学年
				firstSem = year + "201";
				secondSem = (MyTools.StringToInt(year)+1)+"101";
			}else{//同学年
				firstSem = year + "101";
				secondSem = year + "201";
			}
			
			//查询该班级所有学生
			//判断课程类型(1.普通课程/2.添加课程/3.选修课程/4.分层课程)
			sql = "select distinct a.学号,a.姓名 from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
				"left join V_学生基本数据子类 d on d.学号=a.学号 " +
				"where d.学生状态 in ('01','05','07','08') and a.相关编号 in (";
			if("1".equalsIgnoreCase(courseType) || "2".equalsIgnoreCase(courseType)){
				sql += "select t.相关编号 from V_成绩管理_登分教师信息表 t " +
					"left join V_成绩管理_科目课程信息表 t1 on t1.科目编号=t.科目编号 " +
					"where t1.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
					"and t.行政班代码='" + MyTools.fixSql(classCode) + "' " +
					"and t.课程代码='" + MyTools.fixSql(courseCode) + "'";
			}else{
				sql += "'" + classCode + "'";
			}
			sql += ") order by a.学号";
			stuVec = db.GetContextVector(sql);
			
			if(stuVec!=null && stuVec.size()>0){
				//查询所有相关学生成绩信息
				sql = "select a.学号,c.学年学期编码,a.总评 " +
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
					"left join V_学生基本数据子类 d on d.学号=a.学号 " +
					"where d.学生状态 in ('01','05','07','08') and a.相关编号 in (";
				if("1".equalsIgnoreCase(courseType) || "2".equalsIgnoreCase(courseType)){
					sql += "select t.相关编号 from V_成绩管理_登分教师信息表 t " +
						"left join V_成绩管理_科目课程信息表 t1 on t1.科目编号=t.科目编号 " +
						"where t1.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
						"and t.行政班代码='" + MyTools.fixSql(classCode) + "' " +
						"and t.课程代码='" + MyTools.fixSql(courseCode) + "'";
				}else{
					sql += "'" + classCode + "'";
				}
				sql += ") order by a.学号,c.学年学期编码";
				scoreInfoVec = db.GetContextVector(sql);
				
				//查询学年水平测试成绩
				sql = "select a.学号,a.成绩 from V_自设考试管理_学生成绩信息表 a " +
					"left join V_自设考试管理_考试学科信息表 b on b.考试学科编号=a.考试学科编号 " +
					"left join V_自设考试管理_考试信息表 c on c.考试编号=b.考试编号 " +
					"where a.状态='1' and b.状态='1' and c.状态='1' and c.类别编号='03' " +
					"and c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
					"and b.班级代码='" + MyTools.fixSql(classCode) + "' " +
					"and b.课程代码='" + MyTools.fixSql(courseCode) + "' " +
					"order by c.学年学期编码 desc,c.创建时间 desc";
				testScoreInfoVec = db.GetContextVector(sql);
				
				for(int i=0; i<stuVec.size(); i+=2){
					stuCode = MyTools.StrFiltr(stuVec.get(i));
					stuName = MyTools.StrFiltr(stuVec.get(i+1));
					firstFlag = false;
					firstScore = "";
					secondFlag = false;
					secondScore = "";
					tempXnzp = "null";
					
					//判断学业水平测试成绩，如有学业水平测试成绩，以此成绩为准
					for(int j=0; j<testScoreInfoVec.size(); j+=2){
						if(stuCode.equalsIgnoreCase(MyTools.StrFiltr(testScoreInfoVec.get(j)))){
							tempXnzp = MyTools.StrFiltr(testScoreInfoVec.get(j+1));
							break;
						}
					}
					
					//判断如果没有学业水平测试成绩则计算学年总评
					if("null".equalsIgnoreCase(tempXnzp)){
						tempXnzp = "";
						
						//第一学期
						for(int j=0; j<scoreInfoVec.size(); j+=3){
							if(stuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(j))) && firstSem.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(j+1)))){
								firstFlag = true;
								firstScore = MyTools.StrFiltr(scoreInfoVec.get(j+2));
								
								//判断是否为缓考或免考
								if("-5".equalsIgnoreCase(firstScore) || "-17".equalsIgnoreCase(firstScore)){
									firstScore = "";
								}
								//判断如果是其他文字成绩，转换分数
								if("-2".equalsIgnoreCase(firstScore) || "-3".equalsIgnoreCase(firstScore) || "-4".equalsIgnoreCase(firstScore)){
									firstScore = "0";
								}
								
								break;
							}
						}
						
						//第二学期
						for(int j=0; j<scoreInfoVec.size(); j+=3){
							if(stuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(j))) && secondSem.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(j+1)))){
								secondFlag = true;
								secondScore = MyTools.StrFiltr(scoreInfoVec.get(j+2));
								
								//判断是否为缓考或免考
								if("-5".equalsIgnoreCase(secondScore) || "-17".equalsIgnoreCase(secondScore)){
									secondScore = "";
								}
								//判断如果是其他文字成绩，转换分数
								if("-2".equalsIgnoreCase(secondScore) || "-3".equalsIgnoreCase(secondScore) || "-4".equalsIgnoreCase(secondScore)){
									secondScore = "0";
								}
								
								break;
							}
						}
						
						//计算总评
						if(firstFlag==true && secondFlag==true){
							if("-1".equalsIgnoreCase(firstScore) && "-1".equalsIgnoreCase(secondScore)){
								tempXnzp = "60";
							}else if("-1".equalsIgnoreCase(firstScore) && !"-1".equalsIgnoreCase(secondScore)){
								tempXnzp = secondScore;
							}else if(!"-1".equalsIgnoreCase(firstScore) && "-1".equalsIgnoreCase(secondScore)){
								tempXnzp = firstScore;
							}else{
								tempXnzp = f.format(MyTools.StringToDouble(firstScore)*MyTools.StringToDouble(firstSemPercent)/100+MyTools.StringToDouble(secondScore)*MyTools.StringToDouble(secondSemPercent)/100);
							}
						}else if(firstFlag==true && secondFlag==false){
							if("-1".equalsIgnoreCase(firstScore))
								firstScore = "60";
							tempXnzp = firstScore;
						}else{
							if("-1".equalsIgnoreCase(secondScore))
								secondScore = "60";
							tempXnzp = secondScore;
						}
					}
					
					if(MyTools.StringToDouble(tempXnzp) < 60){
						resultVec.add(stuCode);
						resultVec.add(stuName);
						resultVec.add(tempXnzp);
					}
				}
			}
		}
		
		return resultVec;
	}
	
	/**
	 * 读取当前选择班级课程的学生列表
	 * @date:2017-06-14
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadStuList()throws SQLException{
		String result = "[";
		String sql = "";
		Vector vec = null;
		
//		sql = "select ROW_NUMBER() over (order by a.学号) as num,a.学号,a.姓名,a.总评,重修1,重修2,补考,大补考 from V_成绩管理_学生成绩信息表 a " +
//			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' " +
//			"and b.学生状态 in ('01','05') and a.成绩状态='1' and cast(a.总评 as float)<60.0 " +
//			"and a.总评 not in('-1','-6','-7','-8','-9','-11','-13','-15') order by (case when b.班内学号='' then '9999' else b.班内学号 end)";
//		vec = db.GetContextVector(sql);
		
		sql = "select a.编号,b.班内学号,b.学籍号,a.学号,a.姓名,a.学年总评,a.补考 " +
			"from V_成绩管理_学生补考成绩信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"where b.学生状态 in ('01','05','07','08') " +
			"and a.补考编号='" + MyTools.fixSql(this.getCX_ID()) + "' " +
			"order by b.班内学号";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=7){
				result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
						"\"num\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
						"\"xjh\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
						"\"stuCode\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
						"\"stuName\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"," +
						"\"zp\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"," +
						"\"bk\":\"" + MyTools.StrFiltr(vec.get(i+6)) + "\"},";
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
		
		for(int i=0; i<updateArray.length; i+=2){
			sql = "update V_成绩管理_学生补考成绩信息表 set " +
				"补考='" + MyTools.fixSql(updateArray[i+1]) + "' " +
				"where 补考编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' " +
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