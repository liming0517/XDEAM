package com.pantech.devolop.registerScoreSet;
/*
@date 2016.02.03
@author yeq
模块：M6.3 免修设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class MxszBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限

	private String CX_XNXQ; //学年学期
	private String CX_XH; //学号
	private String CX_XM; //姓名
	private String CX_XGBH; //相关编号
	private String CX_CJZT; //成绩状态
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public MxszBean(HttpServletRequest request) {
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
		CX_XNXQ = ""; //学年学期
		CX_XH = ""; //学号
		CX_XM = ""; //姓名
		CX_XGBH = ""; //相关编号
		CX_CJZT = ""; //成绩状态
	}

	/**
	 * 分页查询 班级列表
	 * @date:2016-02-04
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param ZYMC_CX 专业名称
	 * @param XZBMC_CX 班级名称
	 * @param BJLX_CX 班级类型
	 * @param XNXQMC_CX 学年学期名称
	 * @param XH_CX 学号
	 * @param XM_CX 姓名
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queClassList(int pageNum, int pageSize, String ZYMC_CX, String XZBMC_CX, String BJLX_CX, String XNXQMC_CX, String XH_CX, String XM_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select t.班级代码,t.班级名称,t.专业名称,t.班级类型代码,t.班级类型,t.学年学期编码,t.学年学期名称 from (";
		//行政班
		sql += "select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.专业名称,'xzb' as 班级类型代码,'行政班' as 班级类型,'' as 学年学期编码,'' as 学年学期名称,1 as orderNum " +
			"from V_学校班级数据子类 a " +
			"inner join V_专业基本信息数据子类 b on b.专业代码=a.专业代码";
		//教学班
		sql += " union all " +
			"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.专业名称,'jxb' as 班级类型代码,'教学班' as 班级类型,'' as 学年学期编码,'' as 学年学期名称,1 as orderNum " +
			"from V_基础信息_教学班信息表 a " +
			"inner join V_专业基本信息数据子类 b on b.专业代码=a.专业代码";
		//选修班
		sql += " union all " +
			"select 授课计划明细编号,选修班名称,'','xxb','选修班',b.学年学期编码,c.学年学期名称,'2' from V_规则管理_选修课授课计划明细表 a " +
			"left join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
			"left join V_规则管理_学年学期表 c on c.学年学期编码=b.学年学期编码";
		//分层班
		sql += " union all " +
			"select 分层班编号,分层班名称,'','fcb','分层班',b.学年学期编码,c.学年学期名称,'3' from V_规则管理_分层班信息表 a " +
			"left join V_规则管理_分层课程信息表 b on b.分层课程编号=a.分层课程编号 " +
			"left join V_规则管理_学年学期表 c on c.学年学期编码=b.学年学期编码";
		sql += ") as t " +
			"left join V_学校班级数据子类 t1 on t1.行政班代码=t.班级代码 " +
			"left join V_基础信息_教学班信息表 t2 on t2.教学班编号=t.班级代码 " +
			"where 1=1";
		
		//判断权限
		if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
			sql += " and (t1.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
				"or " +
				"t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "'))";
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(ZYMC_CX)){
			sql += " and t.专业名称 like '%" + MyTools.fixSql(ZYMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(XZBMC_CX)){
			sql += " and t.班级名称 like '%" + MyTools.fixSql(XZBMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(BJLX_CX)){
			sql += " and t.班级类型代码='" + MyTools.fixSql(BJLX_CX) + "'";
		}
		if(!"".equalsIgnoreCase(XNXQMC_CX)){
			sql += " and t.学年学期名称 like '%" + MyTools.fixSql(XNXQMC_CX) + "%'";
		}
		if(!"请输入学生完整学号".equalsIgnoreCase(XH_CX) || !"请输入学生完整姓名".equalsIgnoreCase(XM_CX)){
			this.addStuScoreInfo(XH_CX, XM_CX, "");
			
			sql += " and t.班级代码 in (select distinct case when b.来源类型 in ('3','4') then b.相关编号 else b.行政班代码 end as 班级代码 " +
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
				"where 1=1";
			
			if(!"请输入学生完整学号".equalsIgnoreCase(XH_CX))
				sql += " and a.学号='" + MyTools.fixSql(XH_CX) + "'";
			if(!"请输入学生完整姓名".equalsIgnoreCase(XM_CX))
				sql += " and a.姓名='" + MyTools.fixSql(XM_CX) + "'";
			sql += ")";
		}
		sql += " order by t.orderNum,t.学年学期编码 desc,t.班级代码";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取当前学年学期下拉框
	 * @date:2017-02-28
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		Vector vec = null;
		String curXnxq = "";
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		return curXnxq;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2016-02-01
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,'1' as orderNum " +
				"from V_规则管理_学年学期表 where 状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程下拉框
	 * @date:2016-02-04
	 * @author:yeq
	 * @param classCode 行政班代码
	 * @param xnxqbm 学年学期编码
	 * @param classType 班级类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCourseCombo(String classCode, String xnxqbm, String classType) throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue union all ";
		
		if("xzb".equalsIgnoreCase(classType) || "jxb".equalsIgnoreCase(classType)){
			sql += "select a.课程名称,a.相关编号 from V_成绩管理_登分教师信息表 a " +
				"inner join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.行政班代码='" + MyTools.fixSql(classCode) + "' and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		}else{
			sql += "select 课程名称,相关编号 from V_成绩管理_登分教师信息表 where 相关编号='" + MyTools.fixSql(classCode) + "'"; 
		}
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取所选课程学生免修名单数据
	 * @date:2016-02-04
	 * @author:yeq
	 * @throws SQLException
	 */
	public Vector loadStuMxmd()throws SQLException{
		Vector vec = null;
		Vector resultVec = new Vector();
		String sql = "";
		String stuCode = "";
		String stuName = "";
		
		sql = "select 学号,姓名 from V_成绩管理_学生成绩信息表 where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' and 成绩状态='2'";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=2){
				stuCode += MyTools.StrFiltr(vec.get(i))+",";
				stuName += MyTools.StrFiltr(vec.get(i+1))+"、";
			}
		}
		
		if(stuCode.length() > 0){
			stuCode = stuCode.substring(0, stuCode.length()-1);
			stuName = stuName.substring(0, stuName.length()-1);
		}
		resultVec.add(stuCode);
		resultVec.add(stuName);
		return resultVec;
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
		String sql = "select b.班内学号,b.学籍号,a.姓名,a.学号 from V_成绩管理_学生成绩信息表 a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " +
				"where a.成绩状态 in ('1','2') and a.相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' " +
				"order by (case when b.班内学号='' then '9999' else b.班内学号 end)";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		
		return vec;
	}
	
	/**
	 * 保存免修学生信息
	 * @date:2016-02-04
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveMxStu()throws SQLException{
		Vector sqlVec = new Vector();
		String sql = ""; 
		
		sql = "update V_成绩管理_学生成绩信息表 set 平时='-1',期中='-1',实训='-1',期末='-1',总评='-1',成绩状态='2' " +
			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' " +
			"and 学号 in ('" + this.getCX_XH().replaceAll(",", "','") + "')";
		sqlVec.add(sql);
		
		sql = "update V_成绩管理_学生成绩信息表 set " +
			"平时='',期中='',实训='',期末='',总评='',成绩状态='1' " +
			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' and 成绩状态='2' " +
			"and 学号 not in ('" + this.getCX_XH().replaceAll(",", "','") + "')";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**  
	 * 添加查询学生最近一个学期的成绩基础信息
	 * @date:2017-01-04
	 * @author:yeq
	 * @param xh 学号
	 * @param xm 姓名
	 * @throws SQLException 
	 */   
	public boolean addStuScoreInfo(String xh, String xm, String xnxq) throws SQLException {
		boolean flag = true;
		Vector vec = null;
		Vector sqlVec = new Vector();
		String sql = "";
		Vector stuVec = null;
		Vector courseVec = null;
		Vector tempVec = null;
		String curXnxq = xnxq;
		String stuCode = "";
		String stuName = "";
		String classCode = "";
		String state = "";
		boolean addFlag = true;
		
		//当前需要查询的所有学生
		sql = "select distinct 学号,姓名,行政班代码,case when left(学号,2)<>left(行政班代码,2) then '1' else '0' end as 异动状态 from V_学生基本数据子类 " +
			"where 学生状态 in ('01','05')";
		if(!"请输入学生完整学号".equalsIgnoreCase(xh))
			sql += " and 学号='" + MyTools.fixSql(xh) + "'";
		if(!"请输入学生完整姓名".equalsIgnoreCase(xm))
			sql += " and 姓名='" + MyTools.fixSql(xm) + "'";
		sql += " order by 学号";
		stuVec = db.GetContextVector(sql);
		
		if(stuVec!=null && stuVec.size()>0){
			if("".equalsIgnoreCase(curXnxq)){
				//查询当前学年学期
				sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
				vec = db.GetContextVector(sql);
				
				if(vec!=null && vec.size()>0)
					curXnxq = MyTools.StrFiltr(vec.get(0));
			}
				
			for(int i=0; i<stuVec.size(); i+=4){
				stuCode = MyTools.StrFiltr(stuVec.get(i));
				stuName = MyTools.StrFiltr(stuVec.get(i+1));
				classCode = MyTools.StrFiltr(stuVec.get(i+2));
				state = MyTools.StrFiltr(stuVec.get(i+3));
				
				sql = "select * from (select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
					"where a.行政班代码='" + MyTools.fixSql(classCode) + "' and b.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "' " +
					"union all " +
					"select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
					"left join V_规则管理_学生选修课关系表 b on b.授课计划明细编号=a.相关编号 " +
					"left join V_规则管理_选修课授课计划明细表 c on c.授课计划明细编号=b.授课计划明细编号 " +
					"left join V_规则管理_选修课授课计划主表 d on d.授课计划主表编号=c.授课计划主表编号 " +
					"where b.学号='" + MyTools.fixSql(stuCode) + "' and d.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "'" +
					"union all " +
					"select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
					"left join V_规则管理_分层班学生信息表 b on b.分层班编号=a.相关编号 " +
					"left join V_规则管理_分层班信息表 c on c.分层班编号=b.分层班编号 " +
					"left join V_规则管理_分层课程信息表 d on d.分层课程编号=c.分层课程编号 " +
					"where b.学号='" + MyTools.fixSql(stuCode) + "' and d.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "') as t " +
					"where 相关编号 not in (select a.相关编号 from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
					"where b.学年学期编码='" + MyTools.StrFiltr(curXnxq) + "' and 学号='" + MyTools.fixSql(stuCode) + "')";
				courseVec = db.GetContextVector(sql);
				
				if(courseVec!=null && courseVec.size()>0){
					for(int j=0; j<courseVec.size(); j+=4){
						/*20170315修改yeq，无需判断是否异动学生（留级学生需要重读所有课程，无论是否及格）
						addFlag = false;
						
						//判断课程类型,如果是选修课直接添加成绩信息
						if("1".equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(j+3))) || "2".equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(j+3)))){
							//判断是否为异动的学生，如果没有异动，直接添加成绩信息
							if("0".equalsIgnoreCase(state)){
								addFlag = true;
							}else{
								//查询当前课程是否有成绩信息
								//如果没有，直接添加成绩信息
								//如果有，判断若已通过，不添加成绩信息，若没有通过，更新原成绩状态为无效，并添加本学期成绩信息
								sql = "select 编号,case when ((cast(成绩 as float)<60.0) " +
									"and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')) then '0' else '1' end 状态 from (" +
									"select a.编号,学号,case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 " +
									"when 重修1<>'' then 重修1 else 总评 end as 成绩 from V_成绩管理_学生成绩信息表 a " +
									"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
									"where a.学号='" + MyTools.fixSql(stuCode) + "' " +
									"and a.成绩状态='1' and b.课程名称='" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+2))) + "') as t";
								tempVec = db.GetContextVector(sql);
								
								if(tempVec!=null && tempVec.size()>0){
									if("0".equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(1)))){
										sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='3' " +
											"where 编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(0))) + "'";
										sqlVec.add(sql);
										
										addFlag = true;
									}
								}else{
									addFlag = true;
								}
							}
						}else{
							addFlag = true;
						}
						
						if(addFlag){
							sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
								"'" + MyTools.fixSql(stuCode) + "'," +
								"'" + MyTools.fixSql(stuName) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+1))) + "'," +
								"'1'," +
								"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
								"getDate(),'1')";
							sqlVec.add(sql);
						}
						*/
						sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
							"'" + MyTools.fixSql(stuCode) + "'," +
							"'" + MyTools.fixSql(stuName) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+1))) + "'," +
							"'1'," +
							"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
							"getDate(),'1')";
						sqlVec.add(sql);
					}
				}
			}
		}
		
		if(sqlVec.size() > 0){
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				flag = true;
			}else{
				flag = false;
			}
		}
		
		return flag;
	}
	
	/**
	 * 读取学生课程列表数据
	 * @date:2017-03-01
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadStuCourseListData(int pageNum, int pageSize) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		//添加学生当前选择学期的课程成绩基础信息
		this.addStuScoreInfo(this.getCX_XH(), this.getCX_XM(), this.getCX_XNXQ());
		
		sql = "select distinct a.编号,a.学号,a.姓名,b.课程名称,b.来源类型," +
			"case b.来源类型 when '2' then '添加课程' when '3' then '选修课程' when '4' then '分层课程' else '普通课程' end as 课程类型,a.成绩状态 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
			"left join V_学生基本数据子类 d on d.学号=a.学号 " +
			"where a.状态='1' and b.状态='1' and c.状态='1' " +
			"and a.成绩状态 in ('1','2') and d.学生状态 in ('01','05')";
		
		if(!"".equalsIgnoreCase(this.getCX_XNXQ()))
			sql += " and c.学年学期编码='" + MyTools.fixSql(this.getCX_XNXQ()) + "'";
		if(!"请输入学生完整学号".equalsIgnoreCase(this.getCX_XH()))
			sql += " and a.学号='" + MyTools.fixSql(this.getCX_XH()) + "'";
		if(!"请输入学生完整姓名".equalsIgnoreCase(this.getCX_XM()))
			sql += " and a.姓名='" + MyTools.fixSql(this.getCX_XM()) + "'";
		
		sql += " order by a.学号,b.来源类型,b.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 保存指定学生免修课程信息
	 * @date:2017-03-02
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveStuMxCourse()throws SQLException{
		Vector sqlVec = new Vector();
		String sql = ""; 
		
		sql = "update V_成绩管理_学生成绩信息表 set 期末='-1',总评='-1',成绩状态='2' where 编号 in ('" + this.getCX_XGBH().replaceAll(",", "','") + "') ";
		sqlVec.add(sql);
		
		sql = "update V_成绩管理_学生成绩信息表 set 期末=(case 期末 when '-1' then '' else 期末 end),总评=(case 总评 when '-1' then '' else 总评 end),成绩状态='1' " +
			"where 编号 in (select a.编号 from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_学生基本数据子类 c on c.学号=a.学号 " +
			"where a.状态='1' and b.状态='1' and a.成绩状态='2' and c.学生状态 in ('01','05') " +
			"and b.学年学期编码='" + MyTools.fixSql(this.getCX_XNXQ()) + "'";
		if(!"请输入学生完整学号".equalsIgnoreCase(this.getCX_XH()))
			sql += " and a.学号='" + MyTools.fixSql(this.getCX_XH()) + "'";
		if(!"请输入学生完整姓名".equalsIgnoreCase(this.getCX_XM()))
			sql += " and a.姓名='" + MyTools.fixSql(this.getCX_XM()) + "'";
		sql += " and 编号 not in ('" + this.getCX_XGBH().replaceAll(",", "','") + "'))";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**  
	 * 添加查询指定学生指定学期的成绩基础信息
	 * @date:2017-03-01
	 * @author:yeq
	 * @throws SQLException 
	 */   
//	public boolean addStuSemCourseInfo() throws SQLException {
//		boolean flag = true;
//		Vector sqlVec = new Vector();
//		String sql = "";
//		Vector courseVec = null;
//		Vector tempVec = null;
//		String stuCode = "";
//		String stuName = "";
//		String classCode = "";
//		String state = "";
//		boolean addFlag = true;
//		
//		//当前需要查询的所有学生
//		sql = "select distinct 学号,姓名,行政班代码,case when left(学号,2)<>left(行政班代码,2) then '1' else '0' end as 异动状态 from V_学生基本数据子类 " +
//			"where 学生状态 in ('01','05')";
//		if(!"请输入学生完整学号".equalsIgnoreCase(this.getCX_XH()))
//			sql += " and 学号='" + MyTools.fixSql(this.getCX_XH()) + "'";
//		if(!"请输入学生完整姓名".equalsIgnoreCase(this.getCX_XM()))
//			sql += " and 姓名='" + MyTools.fixSql(this.getCX_XM()) + "'";
//		sql += " order by 学号";
//		tempVec = db.GetContextVector(sql);
//		
//		if(tempVec!=null && tempVec.size()>0){
//			stuCode = MyTools.StrFiltr(tempVec.get(0));
//			stuName = MyTools.StrFiltr(tempVec.get(1));
//			classCode = MyTools.StrFiltr(tempVec.get(2));
//			state = MyTools.StrFiltr(tempVec.get(3));
//				
//			sql = "select * from (select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
//				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//				"where a.行政班代码='" + MyTools.fixSql(classCode) + "' " +
//				"and b.学年学期编码='" + MyTools.StrFiltr(this.getCX_XNXQ()) + "' " +
//				"union all " +
//				"select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
//				"left join V_规则管理_学生选修课关系表 b on b.授课计划明细编号=a.相关编号 " +
//				"left join V_规则管理_选修课授课计划明细表 c on c.授课计划明细编号=b.授课计划明细编号 " +
//				"left join V_规则管理_选修课授课计划主表 d on d.授课计划主表编号=c.授课计划主表编号 " +
//				"where b.学号='" + MyTools.fixSql(stuCode) + "' " +
//				"and d.学年学期编码='" + MyTools.StrFiltr(this.getCX_XNXQ()) + "'" +
//				"union all " +
//				"select a.科目编号,a.相关编号,a.课程名称,a.来源类型 from V_成绩管理_登分教师信息表 a " +
//				"left join V_规则管理_分层班学生信息表 b on b.分层班编号=a.相关编号 " +
//				"left join V_规则管理_分层班信息表 c on c.分层班编号=b.分层班编号 " +
//				"left join V_规则管理_分层课程信息表 d on d.分层课程编号=c.分层课程编号 " +
//				"where b.学号='" + MyTools.fixSql(stuCode) + "' " +
//				"and d.学年学期编码='" + MyTools.StrFiltr(this.getCX_XNXQ()) + "') as t " +
//				"where 相关编号 not in (select a.相关编号 from V_成绩管理_学生成绩信息表 a " +
//				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
//				"where a.学号='" + MyTools.fixSql(stuCode) + "' and " +
//				"b.学年学期编码='" + MyTools.StrFiltr(this.getCX_XNXQ()) + "')";
//			courseVec = db.GetContextVector(sql);
//					
//			if(courseVec!=null && courseVec.size()>0){
//				for(int j=0; j<courseVec.size(); j+=4){
//					addFlag = false;
//					
//					//判断课程类型,如果是选修课直接添加成绩信息
//					if("1".equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(j+3))) || "2".equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(j+3)))){
//						//判断是否为异动的学生，如果没有异动，直接添加成绩信息
//						if("0".equalsIgnoreCase(state)){
//							addFlag = true;
//						}else{
//							//查询当前课程是否有成绩信息
//							//如果没有，直接添加成绩信息
//							//如果有，判断若已通过，不添加成绩信息，若没有通过，更新原成绩状态为无效，并添加本学期成绩信息
//							sql = "select 编号,case when ((cast(成绩 as float)<60.0) " +
//								"and 成绩 not in ('-1','-6','-7','-8','-9','-11','-13','-15')) then '0' else '1' end 状态 from (" +
//								"select a.编号,学号,case when 大补考<>'' then 大补考 when 补考<>'' then 补考 when 重修2<>'' then 重修2 " +
//								"when 重修1<>'' then 重修1 else 总评 end as 成绩 from V_成绩管理_学生成绩信息表 a " +
//								"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
//								"where a.学号='" + MyTools.fixSql(stuCode) + "' " +
//								"and a.成绩状态='1' and b.课程名称='" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+2))) + "') as t";
//							tempVec = db.GetContextVector(sql);
//							
//							if(tempVec!=null && tempVec.size()>0){
//								if("0".equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(1)))){
//									sql = "update V_成绩管理_学生成绩信息表 set 成绩状态='3' " +
//										"where 编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(0))) + "'";
//									sqlVec.add(sql);
//									
//									addFlag = true;
//								}
//							}else{
//								addFlag = true;
//							}
//						}
//					}else{
//						addFlag = true;
//					}
//					
//					if(addFlag){
//						sql = "insert into V_成绩管理_学生成绩信息表 (学号,姓名,科目编号,相关编号,成绩状态,创建人,创建时间,状态) values(" +
//							"'" + MyTools.fixSql(stuCode) + "'," +
//							"'" + MyTools.fixSql(stuName) + "'," +
//							"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j))) + "'," +
//							"'" + MyTools.fixSql(MyTools.StrFiltr(courseVec.get(j+1))) + "'," +
//							"'1'," +
//							"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//							"getDate(),'1')";
//						sqlVec.add(sql);
//					}
//				}
//			}
//		}
//		
//		if(sqlVec.size() > 0){
//			if(db.executeInsertOrUpdateTransaction(sqlVec)){
//				flag = true;
//			}else{
//				flag = false;
//			}
//		}
//		
//		return flag;
//	}
	
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
	
	public String getCX_XH() {
		return CX_XH;
	}

	public void setCX_XH(String cX_XH) {
		CX_XH = cX_XH;
	}

	public String getCX_XNXQ() {
		return CX_XNXQ;
	}

	public void setCX_XNXQ(String cX_XNXQ) {
		CX_XNXQ = cX_XNXQ;
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

	public String getCX_CJZT() {
		return CX_CJZT;
	}

	public void setCX_CJZT(String cX_CJZT) {
		CX_CJZT = cX_CJZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}