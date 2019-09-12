package com.pantech.devolop.registerScoreQuery;
/*
@date 2017.04.13
@author yeq
模块：M8.4整班成绩查询
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class ZbcjcxBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	private String XNXQBM;//学年学期编码
	private String XNXQMC;//学年学期名称
	private String BJBH;//班级编号
	private String BJMC;//班级名称
	private String XBDM;//系部代码
	private String NJDM;//年级代码
	private String SSZY;//所属专业
	private String EXAMTYPE;//考试类型
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public ZbcjcxBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2017-04-13
	 * @author:yeq
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加每周天数,上午节数,下午节数,晚上节数以及节次时间设置基础信息
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		AUTH = "";//用户权限
		XNXQBM = "";//学年学期编码
		XNXQMC = "";//学年学期名称
		BJBH = "";//班级编号
		BJMC = "";//班级名称
		XBDM = "";//系部代码
		NJDM = "";//年级代码
		SSZY = "";//所属专业
		EXAMTYPE = "";//考试类型
		MSG = "";//提示信息
	}
	
	/**
	 * 分页查询 学期班级列表
	 * @date:2017-04-13
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "with tempClassInfo as (" +
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 from V_学校班级数据子类 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
			"union all " +
			"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 from V_基础信息_教学班信息表 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码) " +
			"select distinct b.学年学期编码,b.学年学期名称,c.班级代码,c.班级名称,c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称 from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join tempClassInfo c on c.班级代码=a.行政班代码 " +
			"where 1=1 and a.来源类型<>'3'";
		//权限判断
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (";
			//班主任
			if(this.getAUTH().indexOf(bzr) > -1){
				sql += "c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			//系部教务人员
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				if(this.getAUTH().indexOf(bzr) > -1){
					sql += " or ";
				}
				sql += "系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
			
		if(!"".equalsIgnoreCase(this.getXNXQBM())){
			sql += " and b.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getBJBH())){
			sql += " and c.班级代码 like '%" + MyTools.fixSql(this.getBJBH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getBJMC())){
			sql += " and c.班级名称 like '%" + MyTools.fixSql(this.getBJMC()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXBDM())){
			sql += " and c.系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getNJDM())){
			sql += " and c.年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getSSZY())){
			sql += " and c.专业代码='" + MyTools.fixSql(this.getSSZY()) + "'";
		}
		sql += " order by b.学年学期编码 desc,c.班级代码";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017.04.13
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
	 * 读取年级下拉框
	 * @date:2017-04-13
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
	 * 读取文字成绩显示内容
	 * @date:2017-03-20
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
	 * 读取考试学科信息
	 * @date:2017-04-13
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public Vector loadExamSubData() throws SQLException {
		Vector vec = new Vector();
		Vector resultVec = new Vector();
		String tempStr = "";
		String jsonStr = "[";
		String sql = "select a.相关编号,a.课程名称 from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
				"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' order by a.课程名称";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=2){
				tempStr += MyTools.StrFiltr(vec.get(i))+",";
				jsonStr += "{\"code\":\"" + MyTools.StrFiltr(vec.get(i)) + "\",\"subName\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"},";
			}
			
			tempStr = tempStr.substring(0, tempStr.length()-1);
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		}
		jsonStr += "]";
		resultVec.add(tempStr);
		resultVec.add(jsonStr);
		return resultVec;
	}
	
	/**
	 * 查询学生成绩信息
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadStuScoreInfo(String countSub)throws SQLException{
		String tempSql = "";
		String sql = "";
		Vector subVec = null;
		Vector scoreInfoVec = null;
		Vector countInfoVec = null;
		Vector resultVec = new Vector();
//		String admin = MyTools.getProp(request, "Base.admin");//管理员
//		String stuState = MyTools.getProp(request, "Base.stuState");
		String colInfo = "[";
		String scoreInfo = "[";
		
		//读取学科列名信息
		sql = "select a.相关编号,a.课程名称 from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' order by a.课程名称";
		subVec = db.GetContextVector(sql);
		
		if(subVec!=null && subVec.size()>0){
			for(int i=0; i<subVec.size(); i+=2){
				colInfo += "{\"colField\":\"" + MyTools.StrFiltr(subVec.get(i)) + "\"," +
						"\"colName\":\"" + MyTools.StrFiltr(subVec.get(i+1)) + "\"},";
			}
			colInfo = colInfo.substring(0, colInfo.length()-1);
		}
		colInfo += "]";
		resultVec.add(colInfo);
		
		tempSql = "with tempScoreInfo as (" +
			"select a.学号,a.相关编号,b.课程名称,";
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			tempSql += "a.期中 as 成绩";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			tempSql += "a.期末 as 成绩";
		}else{
			tempSql += "a.平时 as 成绩";
		}
		tempSql += " from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
			"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态 in ('1','2') " +
			"and c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and b.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "')";
		//读取成绩信息
		sql = tempSql + " select * from tempScoreInfo";
		scoreInfoVec = db.GetContextVector(sql);
		
		//读取统计信息
		sql = tempSql + ",tempStuInfo as (";
		//判断班级类型
		if(this.getBJBH().indexOf("JXB_") > -1){
			sql += "select b.班内学号,b.姓名,b.学号,b.学籍号 from V_基础信息_教学班学生信息表 a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " + 
				"where a.教学班编号='" + MyTools.fixSql(this.getBJBH()) + "'";
		}else{
			sql += "select 班内学号,姓名,学号,学籍号 from V_学生基本数据子类 " +
				"where " +
				//20170703去除异动学生过滤
				//"学生状态 in ('01','07','08') and " +
				"行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
		}
		sql += ") " +
			"select 班内学号,姓名,学号,学籍号,总分,rank()OVER(ORDER BY 总分 DESC) as 班级排名,不及格科目数,不及格科目详情 " +
			"from (select a.班内学号,a.姓名,a.学号,a.学籍号,(select sum(case when cast(成绩 as float)<0 then 0 else cast(成绩 as float) end) from tempScoreInfo " +
			"where 学号=a.学号 and 相关编号 in ('" + countSub.replaceAll(",", "','") + "')) as 总分," +
			"(select count(*) from tempScoreInfo where 学号=a.学号 " +
			"and 成绩<>'' and ((cast(成绩 as float) between 0 and 59) or cast(成绩 as float) in (-2,-3,-4)) " +
			"and 相关编号 in ('" + countSub.replaceAll(",", "','") + "')) as 不及格科目数," +
			"stuff((select '、'+课程名称 from tempScoreInfo where 学号=a.学号 " +
			"and 成绩<>'' and ((cast(成绩 as float) between 0 and 59) or cast(成绩 as float) in (-2,-3,-4)) " +
			"and 相关编号 in ('" + countSub.replaceAll(",", "','") + "') order by 课程名称 for xml path('')),1,1,'') as 不及格科目详情 " +
			"from tempStuInfo a) as z order by 班内学号";
		countInfoVec = db.GetContextVector(sql);
		
		if(countInfoVec!=null && countInfoVec.size()>0){
			String curStuCode = "";
			String curSubCode = "";
			boolean existFlag = false;
			
			for(int i=0; i<countInfoVec.size(); i+=8){
				curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));
				scoreInfo += "{\"XH\":\"" + MyTools.StrFiltr(countInfoVec.get(i)) + "\"," +
							"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\"," + 
							"\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+3)) + "\",";
				
				for(int j=0; j<subVec.size(); j+=2){
					curSubCode = MyTools.StrFiltr(subVec.get(j));
					existFlag = false;
					scoreInfo += "\"" + curSubCode + "\":";
					
					for(int k=0; k<scoreInfoVec.size(); k+=4){
						if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+1)))){
							scoreInfo += "\"" + MyTools.StrFiltr(scoreInfoVec.get(k+3)) + "\",";
							existFlag = true;
							break;
						}
					}
					if(!existFlag){
						scoreInfo += "\"\",";
					}
				}
				
				scoreInfo += "\"ZF\":\"" + MyTools.StrFiltr(countInfoVec.get(i+4)) + "\"," +
						"\"RANK\":\"" + MyTools.StrFiltr(countInfoVec.get(i+5)) + "\"," + 
						"\"bjgNum\":\"" + MyTools.StrFiltr(countInfoVec.get(i+6)) + "\"," +
						"\"bjgContent\":\"" + MyTools.StrFiltr(countInfoVec.get(i+7)) + "\"},";
			}
			scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
		}
		scoreInfo += "]";
		resultVec.add(scoreInfo);
		return resultVec;
	}
	
	/**
	 * 导出统计信息文件
	 * @date:2017.04.13
	 * @author:yeq
	 * @param countSub 计算总分的科目
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public String exportScoreInfo(String countSub) throws SQLException{
		String tempSql = "";
		String sql = "";
		Vector subVec = null;
		Vector scoreInfoVec = null;
		Vector countInfoVec = null;
		Vector wzcjVec = null;
		Vector resultVec = new Vector();
//		String schoolName = MyTools.getProp(request, "Base.schoolName");
		Vector titleVec = new Vector();
		
		titleVec.add("学号");
		titleVec.add("姓名");
		titleVec.add("学籍号");
		titleVec.add("总分");
		titleVec.add("班级排名");
		titleVec.add("不及格科目数");
		titleVec.add("不及格科目详情");
		
		//读取学科列名信息
		sql = "select a.相关编号,a.课程名称 from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' order by a.课程名称";
		subVec = db.GetContextVector(sql);
		
		if(subVec!=null && subVec.size()>0){
			for(int i=0; i<subVec.size(); i+=2){
				titleVec.add(subVec.get(i+1));	
			}
		}
		
		tempSql = "with tempScoreInfo as (" +
			"select a.学号,a.相关编号,b.课程名称,";
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			tempSql += "a.期中 as 成绩";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			tempSql += "a.期末 as 成绩";
		}else{
			tempSql += "a.平时 as 成绩";
		}
		tempSql += " from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
			"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态 in ('1','2') " +
			"and c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and b.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "')";
		//读取成绩信息
		sql = tempSql + " select * from tempScoreInfo";
		scoreInfoVec = db.GetContextVector(sql);
		
		//读取统计信息
		sql = tempSql + ",tempStuInfo as (";
		//判断班级类型
		if(this.getBJBH().indexOf("JXB_") > -1){
			sql += "select b.班内学号,b.姓名,b.学号,b.学籍号 from V_基础信息_教学班学生信息表 a " +
				"left join V_学生基本数据子类 b on b.学号=a.学号 " + 
				"where a.教学班编号='" + MyTools.fixSql(this.getBJBH()) + "'";
		}else{
			sql += "select 班内学号,姓名,学号,学籍号 from V_学生基本数据子类 " +
				"where " +
				//20170703去除异动学生过滤
				//"学生状态 in ('01','07','08') and " +
				"行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
		}
		sql += ") " +
			"select 班内学号,姓名,学号,学籍号,总分,rank()OVER(ORDER BY 总分 DESC) as 班级排名,不及格科目数,不及格科目详情 " +
			"from (select a.班内学号,a.姓名,a.学号,a.学籍号,(select sum(case when cast(成绩 as float)<0 then 0 else cast(成绩 as float) end) from tempScoreInfo " +
			"where 学号=a.学号 and 相关编号 in ('" + countSub.replaceAll(",", "','") + "')) as 总分," +
			"(select count(*) from tempScoreInfo where 学号=a.学号 " +
			"and 成绩<>'' and ((cast(成绩 as float) between 0 and 59) or cast(成绩 as float) in (-2,-3,-4)) " +
			"and 相关编号 in ('" + countSub.replaceAll(",", "','") + "')) as 不及格科目数," +
			"stuff((select '、'+课程名称 from tempScoreInfo where 学号=a.学号 " +
			"and 成绩<>'' and ((cast(成绩 as float) between 0 and 59) or cast(成绩 as float) in (-2,-3,-4)) " +
			"and 相关编号 in ('" + countSub.replaceAll(",", "','") + "') order by 课程名称 for xml path('')),1,1,'') as 不及格科目详情 " +
			"from tempStuInfo a) as z order by 班内学号";
		countInfoVec = db.GetContextVector(sql);
		
		if(countInfoVec!=null && countInfoVec.size()>0){
			//获取文字成绩代码信息
			sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1'";
			wzcjVec = db.GetContextVector(sql);
			
			String curStuCode = "";
			String curSubCode = "";
			boolean existFlag = false;
			
			for(int i=0; i<countInfoVec.size(); i+=8){
				curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));
				
				resultVec.add(countInfoVec.get(i));
				resultVec.add(countInfoVec.get(i+1));
				resultVec.add(countInfoVec.get(i+3));
				resultVec.add(countInfoVec.get(i+4));
				resultVec.add(countInfoVec.get(i+5));
				resultVec.add(countInfoVec.get(i+6));
				resultVec.add(countInfoVec.get(i+7));
				
				for(int j=0; j<subVec.size(); j+=2){
					curSubCode = MyTools.StrFiltr(subVec.get(j));
					existFlag = false;
					
					for(int k=0; k<scoreInfoVec.size(); k+=4){
						if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+1)))){
							resultVec.add(scoreInfoVec.get(k+3));
							existFlag = true;
							break;
						}
					}
					if(!existFlag){
						resultVec.add("");
					}
				}
			}
		}
		
		//生成XLS文件
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
//		int hour = c.get(Calendar.HOUR);
//		int minute = c.get(Calendar.MINUTE);
//		int second = c.get(Calendar.SECOND);
		String filePath = MyTools.getProp(request, "Base.exportExcelPath");
		String title = this.getBJMC()+" "+this.getXNXQMC().replaceAll(" ", "")+" "+("qz".equalsIgnoreCase(this.getEXAMTYPE())?"期中":"期末")+"考试学生成绩信息";
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/" + title+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+".xls";
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			String curContent = "";
			
			//设置列宽
			wsheet.setColumnView(0, 8);
			wsheet.setColumnView(1, 10);
			wsheet.setColumnView(2, 20);
			wsheet.setColumnView(3, 8);
			wsheet.setColumnView(4, 10);
			wsheet.setColumnView(5, 15);
			wsheet.setColumnView(6, 35);
			for(int i=0; i<titleVec.size()-7; i++){
				wsheet.setColumnView(7+i, 18);
			}
			
			//标题
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			wsheet.mergeCells(0, 0, titleVec.size()-1, 0);
			content = new Label(0, 0, title, contentStyle);
			wsheet.addCell(content);
			wsheet.setRowView(0, 500);
			
			//表格标题行
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			
			for(int i=0; i<titleVec.size(); i++){
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setWrap(true);// 自动换行
				
				if(i == 0){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}else if(i == titleVec.size()-1){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}else{
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}
				content = new Label(i, 1, MyTools.StrFiltr(titleVec.get(i)), contentStyle);
				wsheet.addCell(content);
			}
			wsheet.setRowView(1, 400);
			
			//表格内容
			//k:用于循环时Excel的行号
			//外层for用于循环行,内曾for用于循环列
			for(int i=0, k=2; i<resultVec.size(); i+=titleVec.size(), k++){
				for(int j=0; j<titleVec.size(); j++){
					if(j > 6){
						curContent = this.parseScore(wzcjVec, MyTools.StrFiltr(resultVec.get(i+j)));
					}else{
						curContent = MyTools.StrFiltr(resultVec.get(i+j));
					}
					
					if(j>6 && !"".equalsIgnoreCase(curContent) && ((MyTools.StringToInt(curContent)>0&&MyTools.StringToInt(curContent)<60) 
							|| "作弊".equalsIgnoreCase(curContent) || "取消资格".equalsIgnoreCase(curContent) || "缺考".equalsIgnoreCase(curContent) 
							|| "不及格".equalsIgnoreCase(curContent) || "不合格".equalsIgnoreCase(curContent))){
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.RED);
					}else{
						fontStyle = new WritableFont(
								WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
								jxl.format.Colour.BLACK);
					}
					
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setWrap(true);// 自动换行
					//contentStyle.setShrinkToFit(true);//字体大小自适应
						
					if(j == 0){
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
					}else if(j == titleVec.size()-1){
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
					}else{
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					}
					
					if(k == (resultVec.size()/titleVec.size()+1)){
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
					}else{
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					}
					
					content = new Label(j, k, curContent, contentStyle);
					wsheet.addCell(content);
				}
				wsheet.setRowView(k, 400);
			}
			
			//写入文件
			wbook.write();
			wbook.close();
			os.close();
			this.setMSG("文件生成成功");
		} catch (FileNotFoundException e) {
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e) {
			this.setMSG("文件生成失败");
		} catch (IOException e) {
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/**
	 * 解析文字成绩
	 * @date:2017-04-13
	 * @author:yeq
	 * @param wzcjVec 文字成绩代码信息
	 * @param score 成绩
	 */
	public String parseScore(Vector wzcjVec, String score){
		String result = "";
		
		if(MyTools.StringToInt(score) < 0){
			for(int i=0; i<wzcjVec.size(); i+=2){
				if(MyTools.StrFiltr(wzcjVec.get(i)).equalsIgnoreCase(score)){
					result = MyTools.StrFiltr(wzcjVec.get(i+1));
					break;
				}
			}
		}
		if("".equalsIgnoreCase(result))
			result = score;
		
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

	public String getXNXQBM() {
		return XNXQBM;
	}

	public void setXNXQBM(String xNXQBM) {
		XNXQBM = xNXQBM;
	}

	public String getXNXQMC() {
		return XNXQMC;
	}

	public void setXNXQMC(String xNXQMC) {
		XNXQMC = xNXQMC;
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

	public String getEXAMTYPE() {
		return EXAMTYPE;
	}

	public void setEXAMTYPE(String eXAMTYPE) {
		EXAMTYPE = eXAMTYPE;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}