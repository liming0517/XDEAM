package com.pantech.devolop.registerScoreSet;
/*
@date 2016.02.01
@author yeq
模块：M6.3 登分人员设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class DfryszBean {
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
	public DfryszBean(HttpServletRequest request) {
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
	public Vector queCourseList(int pageNum, int pageSize, String XNXQMC_CX, String JXXZ_CX, String ZYMC_CX, String XZBMC_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select a.科目编号,a.相关编号,c.学年学期名称,d.教学性质,b.专业名称,a.行政班代码,a.行政班名称,a.课程名称,case a.打印锁定 when '0' then '未锁定' when '1' then '已锁定' else a.打印锁定 end as 打印锁定," +
			"replace(a.登分教师编号,'@','') as 登分教师编号,replace(a.登分教师姓名,'@','') as 登分教师姓名," +
			//"isnull(stuff((select ','+isnull(学号,'') from V_成绩管理_学生成绩信息表 where 成绩状态='1' and 相关编号=a.相关编号 for XML PATH('')),1,1,''),'') as 学生名单," +
			"case a.来源类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as 课程类型 " +
			"from V_成绩管理_登分教师信息表 a " +
			"inner join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 c on c.学年学期编码=b.学年学期编码 " +
			"left join V_基础信息_教学性质 d on d.编号=c.教学性质 " +
			"left join V_学校班级数据子类 e on e.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 f on f.教学班编号=a.行政班代码 " +
			"where 1=1";
		
		//判断权限
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
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
	 * 读取专业下拉框
	 * @date:2016-02-01
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
		String sql = "update V_成绩管理_登分教师信息表 set " +
				"登分教师编号='" + MyTools.fixSql("@"+this.getCD_DFJSBH().replaceAll(",","@,@")+"@") + "'," +
				"登分教师姓名='" + MyTools.fixSql("@"+this.getCD_DFJSXM().replaceAll(",","@,@")+"@") + "' " +
				"where 相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 查询学生列表
	 * @date:2016-02-01
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadStuList(int pageNum, int pageSize) throws SQLException{
		Vector vec = null;
		String sql = "select c.行政班名称,b.班内学号,a.姓名,a.学号,b.学籍号 from V_成绩管理_学生成绩信息表 a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
				"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
				"where a.成绩状态 in ('0','1') and b.学生状态 in ('01','05') " +
				"and a.相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "' " +
				"order by c.行政班名称,(case when b.班内学号='' then '9999' else b.班内学号 end)";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	/**
	 * 查询删除学生列表
	 * @date:2016-02-01
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadDelStuList(int pageNum, int pageSize) throws SQLException{
		Vector vec = null;
		String sql = "";
		
		if("JXB_".equalsIgnoreCase(this.getCD_XZBDM().substring(0, 4))){
			sql = "select c.行政班名称,b.学籍号,a.学号,a.姓名 from V_成绩管理_学生成绩信息表 a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
				"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
				"left join V_基础信息_教学班学生信息表 d on d.学号=b.学号 " +
				"where a.成绩状态  in ('0','1') and a.相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "' " +
				"and isnull(d.教学班编号,'')<>'" + MyTools.fixSql(this.getCD_XZBDM()) + "' " +
				"order by c.行政班名称,a.学号";
		}else{
			sql = "select c.行政班名称,b.学籍号,a.学号,a.姓名 from V_成绩管理_学生成绩信息表 a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
				"left join V_学校班级数据子类 c on c.行政班代码=b.行政班代码 " +
				"where a.成绩状态  in ('0','1') and a.相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "' " +
				"and b.行政班代码<>'" + MyTools.fixSql(this.getCD_XZBDM()) + "' " +
				"order by c.行政班名称,a.学号";
		}
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	/**
	 * 查询同步课程列表
	 * @date:2016-12-16
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector queClassCourseList(int pageNum, int pageSize) throws SQLException{
		Vector vec = null;
		String sql = "select distinct a.科目编号,a.相关编号,a.课程名称,case a.来源类型 when '1' then '普通课程' when '2' then '添加课程' when '3' then '选修课程' when '4' then '分层课程' else '未知' end as 课程类型 " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.状态='1' and a.来源类型 in ('1','2') " +
				"and a.行政班代码='" + MyTools.fixSql(this.getCD_XZBDM()) + "' " +
				"and a.相关编号<>'" + MyTools.fixSql(this.getCD_XGBH()) + "' " +
				"and b.学年学期编码=(select t1.学年学期编码 from V_成绩管理_登分教师信息表 t " +
				"left join V_成绩管理_科目课程信息表 t1 on t1.科目编号=t.科目编号 " +
				"where t.相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "') " +
				"order by a.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	/**
	 * 保存学生
	 * @date:2016-02-01
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveStu()throws SQLException{
		Vector sqlVec = new Vector();
		String sql = ""; 
		
		sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='0' where 相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "' and 成绩状态 in ('0','1')";
		sqlVec.add(sql);
		
		sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='1' where 相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "' and 学号 in ('" + this.getCX_XH().replaceAll(",", "','") + "')";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 查询班级学生树数据
	 * @date:2016-03-08
	 * @author:yeq
	 * @param parentCode
	 * @param classCode
	 * @return 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queClassStuTree(String parentCode, String classCode)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课教师
		
		//判断层级
		if("".equalsIgnoreCase(parentCode)){
			sql = "select distinct 'zy-'+b.系部代码 as id,b.系部名称 as text,state='closed' from V_学校班级数据子类 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"where a.状态='1'";
		}else{
			String parentCodeArray[] = parentCode.split("-");
			
			if("zy".equalsIgnoreCase(parentCodeArray[0])){
				sql = "select id,text,state='closed' from (select distinct 'bj-'+行政班代码 as id,行政班名称 as text," +
					"(select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=a.行政班代码) as num " +
					"from V_学校班级数据子类 a " +
					"where 状态='1' " +
					"and 系部代码='" + MyTools.fixSql(parentCodeArray[1]) + "') as t where num>0 order by id";
					//"and 行政班代码<>'" + MyTools.fixSql(classCode) + "'";
			}else{
				sql = "select distinct 学号 as id,姓名 as text,state='open',(case when 班内学号='' then '9999' else 班内学号 end) as 班学号 " +
					"from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码='" + MyTools.fixSql(parentCodeArray[1]) + "' " +
					"and 学号 not in (select 学号 from V_成绩管理_学生成绩信息表 where 相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "') " +
					"order by 班学号";
			}
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 保存新增学生
	 * @date:2016-03-08
	 * @author:yeq
	 * @stuCode 新增学生编号
	 * @throws SQLException
	 */
	public void saveAddStu(String stuCode)throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		String stuCodeArray[] = stuCode.split(",");
		
		for(int i=0; i<stuCodeArray.length; i+=2){
			sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(stuCodeArray[i]) + "'," +
				"'" + MyTools.fixSql(stuCodeArray[i+1]) + "'," +
				"'" + MyTools.fixSql(this.getCD_KMBH()) + "'," +
				"'" + MyTools.fixSql(this.getCD_XGBH()) + "'," +
				"'1'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除非本班学生成绩信息
	 * @date:2016-05-06
	 * @author:yeq
	 * @stuCode 学生编号
	 * @throws SQLException
	 */
	public String delStu(String stuCode)throws SQLException{
		String sql = "";
		String resultCode = "";
		Vector vec = null;
		
		sql = "delete from V_成绩管理_学生成绩信息表 where 相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "' " +
			"and 学号 in ('" + stuCode.replaceAll(",", "','") + "')";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
			
			//查询当前选中学生信息
			sql = "select distinct isnull(stuff((select ','+isnull(学号,'') from V_成绩管理_学生成绩信息表 where 成绩状态='1' and 相关编号=a.相关编号 for XML PATH('')),1,1,''),'') as 学生名单 " +
				"from V_成绩管理_学生成绩信息表 a where a.相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "'";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				resultCode = MyTools.StrFiltr(vec.get(0));
			}
		}else{
			this.setMSG("删除失败");
		}
		
		return resultCode;
	}
	
	/**
	 * 添加学生成绩基础数据
	 * @date:2016-07-20
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public String addStuScoreData()throws SQLException{
		String sql = "";
		Vector vec = null;
		String stuCode = "";
		boolean flag = DfryszBean.addStuScoreInfo(request, this.getCD_XGBH());
		
		if(flag){
			this.setMSG("保存成功");
			
			sql = "select 学号 from V_成绩管理_学生成绩信息表 where 成绩状态='1' and 相关编号='" + this.getCD_XGBH() + "'";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i++){
					stuCode += MyTools.StrFiltr(vec.get(i))+",";
				}
				stuCode = stuCode.substring(0, stuCode.length()-1);
			}
		}else{
			this.setMSG("保存失败");
		}
		
		return stuCode;
	}
	
	/**
	 * 添加学生成绩基础数据
	 * @date:2016-07-20
	 * @author:yeq
	 * @param code 相关编号
	 * @return Vector
	 * @throws SQLException
	 */
	public static boolean addStuScoreInfo(HttpServletRequest request, String code)throws SQLException{
		DBSource db = new DBSource(request);
		boolean flag = true;
		Vector vec = null;
		Vector stuVec = null;
		Vector tempVec = null;
		Vector sqlVec = new Vector();
		String sql = "";
		String xnxq = "xnxq";
		String kmbh = "";
		String type = "";
		String kcmc = "";
		String curXnxq = "curXnxq";
		String tempStuCode = "";
		boolean passFlag = false;
		String tempCode = "";
		String userCode = MyTools.getSessionUserCode(request);
		
//		sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) " +
//			"select a.学号,a.姓名,b.科目编号,b.相关编号,'1' as 成绩状态,'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//			"getDate(),'1' from V_学生基本数据子类 a " +
//			"inner join V_成绩管理_登分教师信息表 b on b.行政班代码=a.行政班代码 " +
//			"where b.科目编号='" + MyTools.fixSql(this.getCX_ID()) + "' " +
//			"and a.学号 not in (select 学号 from V_成绩管理_学生成绩信息表 where 科目编号='" + MyTools.fixSql(this.getCX_ID()) + "') " +
//			"and a.学生状态 in ('01','05')";
		
		//查询课程信息
		//sql = "select 科目编号,来源类型,课程名称 from V_成绩管理_登分教师信息表 where 相关编号='" + MyTools.fixSql(code) + "'";
		sql = "select isnull(b.学年学期编码,''),a.科目编号,a.来源类型,a.课程名称 from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"where a.相关编号='" + MyTools.fixSql(code) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			xnxq = MyTools.StrFiltr(vec.get(0));
			kmbh = MyTools.StrFiltr(vec.get(1));
			type = MyTools.StrFiltr(vec.get(2));
			kcmc = MyTools.StrFiltr(vec.get(3));
		}
		
		//查询当前学年学期
		sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where getDate()>学期开始时间 order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		
		//判断是否当前学年学期
		if(xnxq.equalsIgnoreCase(curXnxq)){
			//根据不同的课程类型查询需要添加成绩信息的学生(1:普通课程/2：添加课程/3:选修课程/4:分层课程)
			if("1".equalsIgnoreCase(type)){
//				sql = "select distinct a.学号,a.姓名,case when left(a.学号,2)<>left(a.行政班代码,2) then '1' else '0' end as 异动状态 from V_学生基本数据子类 a " +
//					"left join V_规则管理_授课计划主表 b on b.行政班代码=a.行政班代码 " +
//					"left join V_规则管理_授课计划明细表 c on c.授课计划主表编号=b.授课计划主表编号 " +
//					"where a.学生状态 in ('01','05') and c.授课计划明细编号='" + MyTools.fixSql(code) + "'";
				sql = "select distinct a.学号,a.姓名 from (select a.学号,a.姓名,a.行政班代码,b.教学班编号,a.学生状态 from V_学生基本数据子类 a left join V_基础信息_教学班学生信息表 b on b.学号=a.学号) a " +
					"left join V_规则管理_授课计划主表 b on b.行政班代码=a.行政班代码  or b.行政班代码=a.教学班编号 " +
					"left join V_规则管理_授课计划明细表 c on c.授课计划主表编号=b.授课计划主表编号 " +
					"where a.学生状态 in ('01','05') and c.授课计划明细编号='" + MyTools.fixSql(code) + "'";
			}else if("2".equalsIgnoreCase(type)){
//				sql = "select distinct a.学号,a.姓名,case when left(a.学号,2)<>left(a.行政班代码,2) then '1' else '0' end as 异动状态 from V_学生基本数据子类 a " +
//					"left join V_排课管理_添加课程信息表 b on b.行政班代码=a.行政班代码 " +
//					"where a.学生状态 in ('01','05') and b.编号='" + MyTools.fixSql(code) + "'";
				sql = "select distinct a.学号,a.姓名 from (select a.学号,a.姓名,a.行政班代码,b.教学班编号,a.学生状态 from V_学生基本数据子类 a left join V_基础信息_教学班学生信息表 b on b.学号=a.学号) a " +
					"left join V_排课管理_添加课程信息表 b on b.行政班代码=a.行政班代码  or b.行政班代码=a.教学班编号 " +
					"where a.学生状态 in ('01','05') and b.编号='" + MyTools.fixSql(code) + "'";
			}else if("3".equalsIgnoreCase(type)){
				sql = "select distinct a.学号,b.姓名 from V_规则管理_学生选修课关系表 a " +
					"left join V_学生基本数据子类 b on b.学号=a.学号 " +
					"where b.学生状态 in ('01','05') and a.授课计划明细编号='" + MyTools.fixSql(code) + "'";
			}else if("4".equalsIgnoreCase(type)){
				sql = "select distinct a.学号,b.姓名 from V_规则管理_分层班学生信息表 a " +
					"left join V_学生基本数据子类 b on b.学号=a.学号 " +
					"where b.学生状态 in ('01','05') and a.分层班编号='" + MyTools.fixSql(code) + "'";
			}
			sql += " and a.学号 not in (select 学号 from V_成绩管理_学生成绩信息表 where 相关编号='" + MyTools.fixSql(code) + "') order by a.学号";
			stuVec = db.GetContextVector(sql);
			
			if(stuVec!=null && stuVec.size()>0){
				//如果是普通课程或者添加课程需要检查是否有学籍异动的学生
				//如果有异动（如留级，复学等）学生:
				// 1.如果当前课程已通过，本学期不添加该课程成绩信息
				// 2.如果当前课程未通过，修改未通过成绩信息的成绩状态为3，并添加本学期该课程成绩信息
				/*20170315修改yeq，无需判断是否异动学生（留级学生需要重读所有课程，无论是否及格）
				if("1".equalsIgnoreCase(type) || "2".equalsIgnoreCase(type)){
					//拼接所有有异动学生的学号
					for(int i=0; i<stuVec.size(); i+=3){
						if("1".equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(i+2)))){
							tempStuCode += "'" + MyTools.StrFiltr(stuVec.get(i)) + "',";
						}
					}
					if(tempStuCode.length() > 0){
						tempStuCode = tempStuCode.substring(0, tempStuCode.length()-1);
						sql = "select 编号,学号,case when ((cast(成绩 as float)<60.0) " +
							"and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')) then '0' else '1' end 状态 from (" +
							"select a.编号,学号,case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 " +
							"when 重修1<>'' then 重修1 else 总评 end as 成绩 from V_成绩管理_学生成绩信息表 a " +
							"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
							"where a.学号 in (" + tempStuCode + ") and a.成绩状态='1' and b.课程名称='" + MyTools.fixSql(kcmc) + "') as t order by 学号";
						tempVec = db.GetContextVector(sql);
					}
					if(tempVec == null) tempVec = new Vector();
					
					for(int i=0; i<stuVec.size(); i+=3){
						tempStuCode = MyTools.StrFiltr(stuVec.get(i));
						
						//判断是否异动的学生
						if("1".equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(i+2)))){
							passFlag = false;
							tempCode = "";
							
							for(int j=0; j<tempVec.size(); j+=3){
								if(tempStuCode.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j+1)))){
									if("1".equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j+2)))){
										passFlag = true;
									}else{
										tempCode = MyTools.StrFiltr(tempVec.get(j));
									}
									break;
								}
							}
							//判断成绩是否通过，如果通过不做任何操作，如未通过更改原成绩状态为无效，并添加本学生成绩信息
							if(passFlag == false){
								if(!"".equalsIgnoreCase(tempCode)){
									sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='3' where 编号='" + MyTools.fixSql(tempCode) + "'";
									sqlVec.add(sql);
								}
								
								sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
									"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i))) + "'," +
									"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i+1))) + "'," +
									"'" + MyTools.fixSql(kmbh) + "'," +
									"'" + MyTools.fixSql(code) + "'," +
									"'1'," +
									"'" + MyTools.fixSql(userCode) + "'," +
									"getDate(),'1')";
								sqlVec.add(sql);
							}
						}else{
							sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
								"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i+1))) + "'," +
								"'" + MyTools.fixSql(kmbh) + "'," +
								"'" + MyTools.fixSql(code) + "'," +
								"'1'," +
								"'" + MyTools.fixSql(userCode) + "'," +
								"getDate(),'1')";
							sqlVec.add(sql);
						}
					}
				}else{
					for(int i=0; i<stuVec.size(); i+=2){
						sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
							"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i+1))) + "'," +
							"'" + MyTools.fixSql(kmbh) + "'," +
							"'" + MyTools.fixSql(code) + "'," +
							"'1'," +
							"'" + MyTools.fixSql(userCode) + "'," +
							"getDate(),'1')";
						sqlVec.add(sql);
					}
				}
				*/
				for(int i=0; i<stuVec.size(); i+=2){
					sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
						"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(i+1))) + "'," +
						"'" + MyTools.fixSql(kmbh) + "'," +
						"'" + MyTools.fixSql(code) + "'," +
						"'1'," +
						"'" + MyTools.fixSql(userCode) + "'," +
						"getDate(),'1')";
					sqlVec.add(sql);
				}
			}
			
			if(sqlVec.size() > 0){
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					flag = true;
				}else{
					flag = false;
				}
			}else{
				flag = true;
			}
		}
		
		return flag;
	}
	
	/**
	 * 同步学生名单
	 * @date:2016-12-16
	 * @author:yeq
	 * @courseCode 相关编号
	 * @throws SQLException
	 */
	public void syncStuList(String courseCode)throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		String courseCodeArray[] = courseCode.split(",");
		Vector stuVec = null;
		Vector targetStuVec = null;
		String tempSubCode = "";
		String tempCourseCode = "";
		String stuCode = "";
		String stuName = "";
		String courseClassCode = "";
		String classCode = "";
		String scoreState = "";
		boolean existFlag = false;
		
		//读取当前课程学生名单信息
		sql = "select a.学号,a.姓名,b.行政班代码,c.行政班代码 as 班级代码,a.成绩状态 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_学生基本数据子类 c on c.学号=a.学号 " +
			"where c.学生状态 in ('01','05') and a.相关编号='" + MyTools.fixSql(this.getCD_XGBH()) + "'";
		stuVec = db.GetContextVector(sql);
		
		if(stuVec!=null && stuVec.size()>0){
			for(int i=0; i<courseCodeArray.length; i+=2){
				tempSubCode = courseCodeArray[i+1];
				tempCourseCode = courseCodeArray[i];
				
				//添加学生成绩基础信息
				if(DfryszBean.addStuScoreInfo(request, tempCourseCode)){
					//读取目标课程学生名单信息
					sql = "select a.学号,a.姓名,b.行政班代码,c.行政班代码 as 班级代码,a.成绩状态 " +
						"from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
						"left join V_学生基本数据子类 c on c.学号=a.学号 " +
						"where a.相关编号='" + MyTools.fixSql(tempCourseCode) + "'";
					targetStuVec = db.GetContextVector(sql);
					
					//重置原学生名单
					for(int j=0; j<targetStuVec.size(); j+=5){
						stuCode = MyTools.StrFiltr(targetStuVec.get(j));
						stuName = MyTools.StrFiltr(targetStuVec.get(j+1));
						courseClassCode = MyTools.StrFiltr(targetStuVec.get(j+2));
						classCode = MyTools.StrFiltr(targetStuVec.get(j+3));
						scoreState = MyTools.StrFiltr(targetStuVec.get(j+4));
						
						//判断成绩状态为普通状态（0：无效/1：有效/2：免修/3：删除），特殊状态的成绩信息不做改动
						if("0".equalsIgnoreCase(scoreState) || "1".equalsIgnoreCase(scoreState)){
							//判断是否为当前课程所属班级学生
							if(courseClassCode.equalsIgnoreCase(classCode)){
								for(int k=0; k<stuVec.size(); k+=5){
									//判断成绩状态是否相同
									if(stuCode.equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(k)))){
										if(!scoreState.equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(k+4)))){
											sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(k+4))) + "' " +
												"where 学号='" + MyTools.fixSql(stuCode) + "' and 相关编号='" + MyTools.fixSql(tempCourseCode) + "'";
											sqlVec.add(sql);
										}
										
										break;
									}
								}
							}else{
								existFlag = false;
								
								for(int k=0; k<stuVec.size(); k+=5){
									if(stuCode.equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(k)))){
										existFlag = true;
										
										if(!scoreState.equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(k+4)))){
											sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(k+4))) + "' " +
												"where 学号='" + MyTools.fixSql(stuCode) + "' " +
												"and 相关编号='" + MyTools.fixSql(tempCourseCode) + "'";
											sqlVec.add(sql);
										}
										
										for(int a=0; a<5; a++){
											stuVec.remove(k);
										}
										
										break;
									}
								}
								
								if(existFlag==false && "1".equalsIgnoreCase(scoreState)){
									sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='0' " +
										"where 学号='" + MyTools.fixSql(stuCode) + "' " +
										"and 相关编号='" + MyTools.fixSql(tempCourseCode) + "'";
									sqlVec.add(sql);
								}
							}
						}
					}
					
					for(int j=0; j<stuVec.size(); j+=5){
						if(!MyTools.StrFiltr(stuVec.get(j+2)).equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(j+3))) && "1".equalsIgnoreCase(MyTools.StrFiltr(stuVec.get(j+4)))){
							sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
								"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(j))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(stuVec.get(j+1))) + "'," +
								"'" + MyTools.fixSql(tempSubCode) + "'," +
								"'" + MyTools.fixSql(tempCourseCode) + "'," +
								"'1'," +
								"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
								"getDate(),'1')";
							sqlVec.add(sql);
						}
					}
				}
			}
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("同步成功");
			}else{
				this.setMSG("同步失败");
			}
		}else{
			this.setMSG("同步成功");
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