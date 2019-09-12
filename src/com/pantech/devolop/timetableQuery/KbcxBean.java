package com.pantech.devolop.timetableQuery;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
/*
@date 2015.06.24
@author yeq
模块：M3课表查询
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.zhuozhengsoft.pageoffice.excelwriter.Cell;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class KbcxBean {
	private String USERCODE;//用户编号
	private String Auth;//用户权限
	private String PK_KCBZBBH; //课程表主表编号
	private String PK_XNXQBM; //学年学期编码
	private String PK_XZBDM; //行政班代码
	private String PK_ZYDM; //专业代码
	private String PK_SKJSBH;//授课教师编号
	private String PK_KCBMXBH; //课程表明细编号
	private String PK_SJXL; //时间序列
	private String PK_LJXGBH; //连节相关编号
	private String PK_SKJHMXBH; //授课计划明细编号
	private String PK_BZ; //备注
	private String PK_ZT; //状态
	private String PK_CJBH;//层级编号
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public KbcxBean(HttpServletRequest request) {
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
		Auth = "";//用户权限
		PK_KCBZBBH = ""; //课程表主表编号
		PK_XNXQBM = ""; //学年学期编码
		PK_XZBDM = ""; //行政班代码
		PK_ZYDM = ""; //专业代码
		PK_SKJSBH = "";//授课教师编号
		PK_KCBMXBH = ""; //课程表明细编号
		PK_SJXL = ""; //时间序列
		PK_LJXGBH = ""; //连节相关编号
		PK_SKJHMXBH = ""; //授课计划明细编号
		PK_BZ = ""; //备注
		PK_ZT = ""; //状态
		PK_CJBH = "";//层级编号
	}
	
	/**
	 * 分页查询 学年学期课程表列表
	 * @date:2015-05-27
	 * @author:yeq
	 * @param pageNum 页数
	 * @param page 每页数据条数
	 * @param PK_XNXQMC_CX 学年学期名称
	 * @param PK_JXXZ_CX 教学性质
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSemesterList(int pageNum, int page, String PK_XNXQMC_CX, String PK_JXXZ_CX, String PK_ZY_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		//String admin = MyTools.getProp(request, "Base.admin");//管理员
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		sql = "select distinct a.学年学期编码 as PK_XNXQBM,b.学年学期名称 as PK_XNXQMC,c.教学性质 as jxxz,d.专业代码 as PK_ZYDM,e.专业名称 as PK_ZYMC " +
			"from V_排课管理_课程表主表 a " +
			"left join V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
			"left join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
			"left join V_学校班级数据子类 d on d.行政班代码=a.行政班代码 " +
			"left join V_专业基本信息数据子类 e on e.专业代码=d.专业代码 " +
			"where a.状态='1'";
		
		//判断权限,管理员和排公共课的老师可看到所有专业,排专业课的老师只可以看到自己的专业
		if(this.getAuth().indexOf(majorTeacher) > -1){
			sql += " and d.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(PK_XNXQMC_CX)){
			sql += " and b.学年学期名称 like '%" + MyTools.fixSql(PK_XNXQMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(PK_JXXZ_CX)){
			sql += " and b.教学性质='" + MyTools.fixSql(PK_JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(PK_ZY_CX)){
			sql += " and d.专业代码='" + MyTools.fixSql(PK_ZY_CX) + "'";
		}
		sql += " order by a.学年学期编码 desc";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 分页查询 全校班级课程表列表
	 * @date:2015-05-27
	 * @author:yeq
	 * @param pageNum 页数
	 * @param page 每页数据条数
	 * @param PK_XNXQMC_CX 学年学期名称
	 * @param PK_JXXZ_CX 教学性质
	 * @param PK_ZY_CX 专业
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queAllClassKcbList(int pageNum, int page, String PK_XNXQMC_CX, String PK_JXXZ_CX, String PK_ZY_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
//		//String admin = MyTools.getProp(request, "Base.admin");//管理员
//		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
//		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
//		
//		sql = "select distinct a.学年学期编码 as PK_XNXQBM,b.学年学期名称 as PK_XNXQMC,c.教学性质 as jxxz,d.专业代码 as PK_ZYDM,e.专业名称+'('+d.专业代码+')' as PK_ZYMC " +
//			"from V_排课管理_课程表主表 a " +
//			"left join V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
//			"left join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
//			"left join V_学校班级_数据子类 d on d.行政班代码=a.行政班代码 " +
//			"left join V_专业基本信息数据子类 e on e.专业代码=d.专业代码 " +
//			"where a.状态='1'";
//		
//		//判断权限,管理员和排公共课的老师可看到所有专业,排专业课的老师只可以看到自己的专业
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and d.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
//		}
		
		sql = "select distinct a.学年学期编码 as PK_XNXQBM,a.学年学期名称 as PK_XNXQMC,c.教学性质 as jxxz,b.系部代码 as PK_XBDM,b.系部名称 as PK_XBMC " +
			"from V_规则管理_学年学期表 a " +
			"left join V_基础信息_系部信息表 b on b.系部代码<>'C00' " +
			"left join V_基础信息_教学性质 c on c.编号=a.教学性质 " +
			"where 1=1";
		//权限判断
		if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
			//系部教务人员
			if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
				sql += " and b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
		}
		//判断查询条件
		if(!"".equalsIgnoreCase(PK_XNXQMC_CX)){
			sql += " and a.学年学期名称 like '%" + MyTools.fixSql(PK_XNXQMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(PK_JXXZ_CX)){
			sql += " and a.教学性质='" + MyTools.fixSql(PK_JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(PK_ZY_CX)){
			sql += " and b.系部代码='" + MyTools.fixSql(PK_ZY_CX) + "'";
		}
		sql += " order by a.学年学期编码 desc,b.系部代码";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 分页查询 全校教师课程表列表
	 * @date:2015-05-27
	 * @author:yeq
	 * @param pageNum 页数
	 * @param page 每页数据条数
	 * @param PK_XNXQMC_CX 学年学期名称
	 * @param PK_JXXZ_CX 教学性质
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queAllTeaKcbList(int pageNum, int page, String PK_XNXQMC_CX, String PK_JXXZ_CX, String PK_JS_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
//		sql = "select distinct a.学年学期编码 as PK_XNXQBM,b.学年学期名称 as PK_XNXQMC,c.教学性质 as jxxz,d.层级编号 as PK_CJBH,d.层级名称 as PK_CJMC " +
//			"from V_排课管理_课程表主表 a " +
//			"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
//			"inner join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
//			"left join V_层级表 d on 1=1 " +
//			"where 1=1";
//		
//		//判断查询条件
//		if(!"".equalsIgnoreCase(PK_XNXQMC_CX)){
//			sql += " and b.学年学期名称 like '%" + MyTools.fixSql(PK_XNXQMC_CX) + "%'";
//		}
//		if(!"".equalsIgnoreCase(PK_JXXZ_CX)){
//			sql += " and c.编号='" + MyTools.fixSql(PK_JXXZ_CX) + "'";
//		}
//		if(!"".equalsIgnoreCase(PK_JS_CX)){
//			sql += " and d.层级编号='" + MyTools.fixSql(PK_JS_CX) + "'";
//		}
//		sql += " order by a.学年学期编码 desc,d.层级编号";
		
		sql = "select distinct a.学年学期编码 as PK_XNXQBM,a.学年学期名称 as PK_XNXQMC,b.教学性质 as jxxz from V_规则管理_学年学期表 a " +
			"left join V_基础信息_教学性质 b on b.编号=a.教学性质 " +
			"where 1=1";
		
		//判断查询条件
		if(!"".equalsIgnoreCase(PK_XNXQMC_CX)){
			sql += " and a.学年学期名称 like '%" + MyTools.fixSql(PK_XNXQMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(PK_JXXZ_CX)){
			sql += " and b.编号='" + MyTools.fixSql(PK_JXXZ_CX) + "'";
		}
		sql += " order by a.学年学期编码 desc";
		vec = db.getConttexJONSArr(sql, pageNum, page);
		
		return vec;
	}
	
	/**
	 * 查询学期下拉框
	 * @date:2017-02-23
	 * @author:lupengfei
	 * @return vector
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo() throws SQLException{
		Vector vec = null;
		String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询学生允许查看的学年学期下拉框
	 * @date:2017-12-26
	 * @author:yeq
	 * @return vector
	 * @throws SQLException
	 */
	public Vector loadStuXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select 学年学期编码 AS comboValue,学年学期名称 AS comboName from V_规则管理_学年学期表 where getDate()>=学期开始时间 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询周次下拉框
	 * @date:2015-08-24
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadWeekCombo() throws SQLException{
		Vector vec = null;
		String tempJson = "";
		String sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		int weekNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));//学期总周次数
		tempJson = "[";
		tempJson += "{\"comboName\":\"全部\",\"comboValue\":\"all\"},";
		for(int i=1; i<weekNum+1; i++){
			tempJson += "{\"comboName\":\"第" + i + "周\",\"comboValue\":\"" + i + "\"},";
		}
		tempJson = tempJson.substring(0, tempJson.length()-1);
		tempJson += "]";
		
		return tempJson;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-05-27
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
	 * @date:2015-05-27
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		//String admin = MyTools.getProp(request, "Base.admin");//管理员
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue " +
				"from V_专业基本信息数据子类 where 状态='1'";
		
		if(this.getAuth().indexOf(majorTeacher) > -1){
			sql += " and 专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-07-18
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadDepatmentCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 系部名称 as comboName,系部代码 as comboValue " +
				"from V_基础信息_系部信息表 where 状态='Y' and 系部代码<>'C00'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取角色层级下拉框
	 * @date:2015-05-27
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadLevelCombo() throws SQLException{
		Vector vec = null;
		
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 层级名称 as comboName,层级编号 as comboValue " +
				"from V_层级表 where 状态='1' order by comboValue";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询当前学期已有课程表的班级信息树
	 * @date:2015-05-27
	 * @author:yeq
	 * @param level
	 * @param parentCode 专业代码
	 * @return 班级信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queClassTree(String level, String parentCode)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		//获取专业目录
		if("0".equalsIgnoreCase(level)){
//			sql = "select count(*) from V_排课管理_课程表主表 a " +
//				"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//				"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
//				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//			if(db.getResultFromDB(sql)){
//				sql = "select distinct c.专业代码 as id,c.专业名称+'('+c.专业代码+')' as text,state='closed' from V_排课管理_课程表主表 a " +
//					"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//					"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
//					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"' order by text";
//					//"and c.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//			}else{
//				sql = "select '' as id,'没有相关班级信息' as text,'open' as state";
//			}
			sql = "select count(*) from V_排课管理_课程表主表 a " +
				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
				"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and c.系部代码<>'C00'";
			//权限判断
			if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
				sql += " and (";
				//班主任
				if(this.getAuth().indexOf(bzr) > -1){
					sql += "b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
				}
				//系部教务人员
				if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
					if(this.getAuth().indexOf(bzr) > -1){
						sql += " or ";
					}
					sql += "b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
			
			if(db.getResultFromDB(sql)){
				sql = "select distinct c.系部代码 as id,c.系部名称 as text,state='closed' from V_排课管理_课程表主表 a " +
					//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
					"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
					"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 " +
					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"' and c.系部代码<>'C00'";
				//权限判断
				if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
					sql += " and (";
					//班主任
					if(this.getAuth().indexOf(bzr) > -1){
						sql += "b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
					}
					//系部教务人员
					if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
						if(this.getAuth().indexOf(bzr) > -1){
							sql += " or ";
						}
						sql += "b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
					}
					sql += ")";
				}
				sql += " order by text";
			}else{
				sql = "select '' as id,'没有相关班级信息' as text,'open' as state";
			}
		}
		//获得班级子节点
		if(level.equalsIgnoreCase("1")){
//			sql = "select distinct a.行政班代码 as id,b.行政班名称 as text,state='open' from V_排课管理_课程表主表 a " +
//				"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//				"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
//				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"' " +
//				"and c.专业代码='" + MyTools.fixSql(parentCode) + "' order by a.行政班代码";
			sql = "select distinct a.行政班代码 as id,b.班级名称 as text,state='open' from V_排课管理_课程表主表 a " +
				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
				"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"' " +
				"and c.系部代码='" + MyTools.fixSql(parentCode) + "'";
			if(this.getAuth().indexOf(bzr) > -1){
				sql += " and b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			sql += " order by a.行政班代码";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询当前学期已有课程表的班级信息树
	 * @date:2017-02-09
	 * @author:lupengfei
	 * @param level
	 * @param parentCode 专业代码
	 * @return 班级信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queStudentTree(String level, String parentCode)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		String sqlstu="";
		String stuAuth = MyTools.getProp(request, "Base.wjdcStu");
		
		if(this.getAuth().indexOf(stuAuth) > -1){
			sqlstu=" and d.学号 ='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
		}
		
		//获取专业目录
		if("0".equalsIgnoreCase(level)){
			sql = "select distinct c.专业代码 as id,c.专业名称+'('+c.专业代码+')' as text,state='closed','major' as choose from V_排课管理_课程表主表 a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
				"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
				"left join V_学生基本数据子类 d on b.行政班代码=d.行政班代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"' " + sqlstu +
				"order by text";
				//"and c.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
		}
		//获得班级子节点
		if(level.equalsIgnoreCase("1")){
			sql = "select distinct a.行政班代码 as id,b.行政班名称 as text,state='closed','course' as choose from V_排课管理_课程表主表 a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
				"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
				"left join V_学生基本数据子类 d on b.行政班代码=d.行政班代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"' " +
				"and c.专业代码='" + MyTools.fixSql(parentCode) + "' " + sqlstu +
				"order by a.行政班代码";
		}
		//获得学生子节点
		if(level.equalsIgnoreCase("2")){
			sql = "select distinct d.学号 as id,d.姓名 as text,state='open','student' as choose from V_排课管理_课程表主表 a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
				"left join V_学生基本数据子类 d on b.行政班代码=d.行政班代码 " +
				"where a.状态='1' and b.行政班代码='" + MyTools.fixSql(parentCode) + "' " + sqlstu +
				"order by d.学号 " ;
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询当前学年设置的每周天数和每天节数
	 * @date:2015-05-27
	 * @author:yeq
	 * @return 每周天数和每天节数信息
	 * @throws SQLException
	 */
	public Vector loadBlankKcbInfo()throws SQLException{
		String sql = "select distinct (select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') as 学期周次," +
				"t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
				"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间 " +
				"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		Vector vec = db.GetContextVector(sql);
		
		return vec;
	}
	
	/**
	 * 查询教师课表备注信息
	 * @date:2015-10-21
	 * @author:yeq
	 * @throws SQLException
	 */
	public String loadTeaRemark()throws SQLException{
		String result = "";
		Vector vec = null;
		String sql = "select * from V_规则管理_教师课表备注表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			result += "{\"startDate\":\"" + MyTools.StrFiltr(vec.get(2)) + "\"," +
					"\"endDate\":\"" + MyTools.StrFiltr(vec.get(3)) + "\"," +
					"\"weekNum_1\":\"" + MyTools.StrFiltr(vec.get(4)) + "\"," +
					"\"contactWay\":\"" + MyTools.StrFiltr(vec.get(5)) + "\"," +
					"\"weekNum_2\":\"" + MyTools.StrFiltr(vec.get(6)) + "\"," +
					"\"exam_1\":\"" + MyTools.StrFiltr(vec.get(7)) + "\"," +
					"\"weekNum_3\":\"" + MyTools.StrFiltr(vec.get(8)) + "\"," +
					"\"exam_2\":\"" + MyTools.StrFiltr(vec.get(9)) + "\"," +
					"\"weekNum_4\":\"" + MyTools.StrFiltr(vec.get(10)) + "\"," +
					"\"exam_3\":\"" + MyTools.StrFiltr(vec.get(11)) + "\"," +
					"\"year\":\"" + MyTools.StrFiltr(vec.get(12)) + "\"}";
		}else{
			result += "{\"startDate\":\"\"," +
					"\"endDate\":\"\"," +
					"\"weekNum_1\":\"\"," +
					"\"contactWay\":\"\"," +
					"\"weekNum_2\":\"\"," +
					"\"exam_1\":\"\"," +
					"\"weekNum_3\":\"\"," +
					"\"exam_2\":\"\"," +
					"\"weekNum_4\":\"\"," +
					"\"exam_3\":\"\"," +
					"\"year\":\"\"}";
		}
		
		return result;
	}
	
	/**
	 * 查询班级课程表
	 * @date:2015-05-27
	 * @author:yeq
	 * @param week 周次
	 * @return 班级课程表
	 * @throws SQLException
	 */
	public Vector loadClassKcb(String week,String nodeType)throws SQLException{
		Vector vec = new Vector();
		Vector tempVec = null;
		Vector resultVec = new Vector();
		String sql = "";
		String resultJson = "[";
		int xqzc = 0;
		Vector oddVec = new Vector();
		Vector evenVec = new Vector();
		String sjxl = "";
		String skjhmxbh = "";
		String kcbh = "";
		String kcmc = "";
		String skjsbh = "";
		String skjsxm = "";
		String cdbh = "";
		String cdmc = "";
		String skzc = "";
		String tempSkjhmxbh = "";
		String tempKcmc = "";
		String tempSkjsxm = "";
		String tempCdmc = "";
		String tempSkzc = "";
		
		//获取本学期授课周次
		sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			xqzc = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));
			
			for(int i=1; i<xqzc+1; i+=2){
				oddVec.add(i);
				evenVec.add(i+1);
			}
		}
		
		//添加学生课表查询 20160209 lupengfei
		String userid="";
		if(nodeType.equals("student")){
			String sqlcls="select 行政班代码 from dbo.V_学生基本数据子类 where 学号='"+MyTools.fixSql(this.getPK_XZBDM())+"' ";
			Vector veccls=db.GetContextVector(sqlcls);
			if(veccls!=null&&veccls.size()>0){
				userid=this.getPK_XZBDM();
				this.setPK_XZBDM(veccls.get(0).toString());
			}
		}

		//判断是否读取指定周次课表
		if("all".equalsIgnoreCase(week)){
			boolean existFlag = false;
			Vector tempResultVec = new Vector();
			vec = new Vector();
			
//			sql = "select * from (select distinct a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师姓名,a.场地名称," +
//				"stuff((select '#'+t.授课周次 from V_排课管理_课程表周详情表 t where t.学年学期编码=a.学年学期编码 and t.行政班代码=a.行政班代码 and t.时间序列=a.时间序列 " +
//				"and t.授课计划明细编号=a.授课计划明细编号 and t.授课教师编号=a.授课教师编号 and t.场地编号=a.场地编号 order by cast(t.授课周次 as int) for XML PATH('')),1,1,'') as 授课周次 " +
//				"from V_排课管理_课程表周详情表 a " +
//				"where a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and a.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and a.授课计划明细编号<>'') as b " +
//				"order by 时间序列,cast(substring(授课周次,1,case when CHARINDEX('#',授课周次)>0 then CHARINDEX('#',授课周次)-1 else len(授课周次) end) as int)";
//			sql = "select * from (select distinct 时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,授课周次 " +
//				"from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and 行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and 授课计划明细编号<>'') as b " +
//				"order by 时间序列,cast(授课周次 as int)";
			sql = "select t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
				"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " + 
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"and a.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and a.授课计划明细编号<>'') as t " + 
				"order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				boolean flag = true;
				//拼接课程周次
				while(flag){
					sjxl = MyTools.StrFiltr(tempVec.get(0));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
					kcbh = MyTools.StrFiltr(tempVec.get(2));
					kcmc = MyTools.StrFiltr(tempVec.get(3));
					skjsbh = MyTools.StrFiltr(tempVec.get(4));
					skjsxm = MyTools.StrFiltr(tempVec.get(5));
					cdbh = MyTools.StrFiltr(tempVec.get(6));
					cdmc = MyTools.StrFiltr(tempVec.get(7));
					skzc = MyTools.StrFiltr(tempVec.get(8));
					for(int i=0; i<9; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=9){
						if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) 
							&& kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) 
							&& cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+8));
							for(int j=0; j<9; j++){
								tempVec.remove(i);
							}
							i -= 9;
						}
					}
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(kcmc);
					vec.add(skjsxm);
					vec.add(cdmc);
					vec.add(skzc);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
				
				sjxl = "";
				tempVec = new Vector();
				//拼接课程
				for(int i=0; i<vec.size(); i+=6){
					skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
					kcmc = MyTools.StrFiltr(vec.get(i+2));
					skjsxm = MyTools.StrFiltr(vec.get(i+3));
					cdmc = MyTools.StrFiltr(vec.get(i+4));
					skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i+5)), oddVec, evenVec);
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
						if(i > 0){
							tempResultVec.add(sjxl);
							for(int j=0; j<tempVec.size(); j+=5){
								tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(j))+"｜";
								tempKcmc += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
								tempSkjsxm += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
								tempCdmc += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
								tempSkzc += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
							}
							tempResultVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
							tempResultVec.add(tempKcmc.substring(0, tempKcmc.length()-1));
							tempResultVec.add(tempSkjsxm.substring(0, tempSkjsxm.length()-1));
							tempResultVec.add(tempCdmc.substring(0, tempCdmc.length()-1));
							tempResultVec.add(tempSkzc.substring(0, tempSkzc.length()-1));
							tempSkjhmxbh = "";
							tempKcmc = "";
							tempSkjsxm = "";
							tempCdmc = "";
							tempSkzc = "";
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						tempVec = new Vector();
						tempVec.add(skjhmxbh);
						tempVec.add(kcmc);
						tempVec.add(skjsxm);
						tempVec.add(cdmc);
						tempVec.add(skzc);
					}else{
						for(int j=0; j<tempVec.size(); j+=5){
							if(skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j)))){
								tempVec.set(j+2, MyTools.StrFiltr(tempVec.get(j+2))+"&"+skjsxm);
								tempVec.set(j+3, MyTools.StrFiltr(tempVec.get(j+3))+"&"+cdmc);
								tempVec.set(j+4, MyTools.StrFiltr(tempVec.get(j+4))+"&"+skzc);
								existFlag = true;
							}
						}
						
						if(existFlag == false){
							tempVec.add(skjhmxbh);
							tempVec.add(kcmc);
							tempVec.add(skjsxm);
							tempVec.add(cdmc);
							tempVec.add(skzc);
						}
						existFlag = false;
					}
				}
				tempResultVec.add(sjxl);
				for(int j=0; j<tempVec.size(); j+=5){
					tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(j))+"｜";
					tempKcmc += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
					tempSkjsxm += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
					tempCdmc += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
					tempSkzc += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
				}
				tempResultVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
				tempResultVec.add(tempKcmc.substring(0, tempKcmc.length()-1));
				tempResultVec.add(tempSkjsxm.substring(0, tempSkjsxm.length()-1));
				tempResultVec.add(tempCdmc.substring(0, tempCdmc.length()-1));
				tempResultVec.add(tempSkzc.substring(0, tempSkzc.length()-1));
				
				//添加体锻课  lupengfei 20170928			
				int tytag=0;//体育课标记
				int tdcs=0;//体锻课次数
				int zwyk=0;//中午是否有課
				
				//获取操场编号
				String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and [教室名称]='操场' ) ";
				Vector veccc=db.GetContextVector(sqlcc);
				
				//获取实际上课周数
				String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
				Vector vecskzs=db.GetContextVector(sqlskzs);
					
				//获取每周天数
				int mzts=0;
				String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
				Vector vecmzts=db.GetContextVector(sqlmzts);
				if(vecmzts!=null&&vecmzts.size()>0){
					mzts=Integer.parseInt(vecmzts.get(0).toString());
				}
				
				//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
				
				//判断哪天没体育课,中午添加体锻课
				String ts="";
				for(int i=1;i<=mzts;i++){
					tytag=0;
					if(i<10){
						ts="0"+i;
					}else{
						ts=i+"";
					}
					for(int j=0;j<tempResultVec.size();j=j+6){
						if(ts.equals(tempResultVec.get(j).toString().substring(0, 2))){//天数相同
							if(tempResultVec.get(j+2).toString().indexOf("体育")>-1){//有体育课
								tytag=1;
							}
						}	
					}
					if(tytag==0){
						for(int k=0;k<tempResultVec.size();k=k+6){
							if((ts+"05").equals(tempResultVec.get(k).toString())){//中午有课
								zwyk=1;
								
							}
						}
						if(zwyk==0){
							if(tdcs<2){
								//添加体锻课
								//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
								tempResultVec.add(ts+"05");//时间序列
								tempResultVec.add("");//授课计划明细编号
								tempResultVec.add("体锻");//课程名称
								tempResultVec.add("");//实际场地名称
								tempResultVec.add("");//授课教师姓名
								tempResultVec.add("");//授课周次详情
								
								tdcs++;
							}
						}else{
							zwyk=0;
						}
					}
				}
				
				//添加学生课表查询 20160209 lupengfei
				if(nodeType.equals("student")){
					//查询选修课信息 
					String sqlxxk="select distinct a.上课时间 as 时间序列,a.授课计划明细编号,b.课程名称,a.授课教师姓名,a.场地名称,a.授课周次 " +
							"from [V_规则管理_选修课授课计划明细表] a,dbo.V_规则管理_选修课授课计划主表 b,V_基础信息_选修课程信息表 c " +
							"where a.授课计划主表编号=b.授课计划主表编号  and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and a.上课时间!='' and a.授课计划明细编号 in ( " +
							"SELECT [授课计划明细编号] FROM [V_规则管理_学生选修课关系表] where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 学号='" + MyTools.fixSql(userid) + "' )";
					Vector vecxxk=db.GetContextVector(sqlxxk);
					if(vecxxk!=null&&vecxxk.size()>0){
						for(int i=0;i<vecxxk.size();i=i+6){
							if(vecxxk.get(i).toString().indexOf(",")>-1){
								String[] sjxls=vecxxk.get(i).toString().split(",");
								for(int j=0;j<sjxls.length;j++){
									tempResultVec.add(sjxls[j]);
									tempResultVec.add(vecxxk.get(i+1).toString());
									tempResultVec.add(vecxxk.get(i+2).toString());
									tempResultVec.add(vecxxk.get(i+3).toString());
									tempResultVec.add(vecxxk.get(i+4).toString());
									tempResultVec.add(vecxxk.get(i+5).toString());
								}
							}else{
								tempResultVec.add(vecxxk.get(i).toString());
								tempResultVec.add(vecxxk.get(i+1).toString());
								tempResultVec.add(vecxxk.get(i+2).toString());
								tempResultVec.add(vecxxk.get(i+3).toString());
								tempResultVec.add(vecxxk.get(i+4).toString());
								tempResultVec.add(vecxxk.get(i+5).toString());
							}
						}
					}
					
					//查询学生 添加课程信息表
					String sqlgr="  SELECT a.时间序列,a.相关课程表编号,b.课程名称,b.授课教师姓名,b.场地名称,b.授课周次  FROM [dbo].[V_排课管理_添加课程信息表] a,V_排课管理_课程表明细详情表 b " +
							"where a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and a.相关课程表编号=b.课程表明细编号  and a.类型='3' and a.学号='" + MyTools.fixSql(userid) + "'";
					Vector vecgr=db.GetContextVector(sqlgr);
					if(vecgr!=null&&vecgr.size()>0){
						for(int i=0;i<vecgr.size();i=i+6){
							tempResultVec.add(vecgr.get(i).toString());
							tempResultVec.add(vecgr.get(i+1).toString());
							tempResultVec.add(vecgr.get(i+2).toString());
							tempResultVec.add(vecgr.get(i+3).toString());
							tempResultVec.add(vecgr.get(i+4).toString());
							tempResultVec.add(vecgr.get(i+5).toString());
						}
					}
					
				}
				
				for(int i=0; i<tempResultVec.size(); i+=6){
					resultJson +=  "{\"时间序列\": \"" + MyTools.StrFiltr(tempResultVec.get(i)) + "\"," +
							"\"授课计划明细编号\":\"" + MyTools.StrFiltr(tempResultVec.get(i+1)) + "\"," +
							"\"课程名称\":\"" + MyTools.StrFiltr(tempResultVec.get(i+2)) + "\"," +
							"\"实际场地名称\": \"" + MyTools.StrFiltr(tempResultVec.get(i+3)) + "\"," + 
					        "\"授课教师姓名\":\"" + MyTools.StrFiltr(tempResultVec.get(i+4)) + "\"," +
					        "\"授课周次详情\": \"" + MyTools.StrFiltr(tempResultVec.get(i+5)) + "\"},";
				}
				if(!resultJson.equals("")){
					resultJson = resultJson.substring(0, resultJson.length()-1);
				}
				
			}
		}else{//显示单个周次
			sql = "select 时间序列,授课计划明细编号,课程名称,授课教师姓名,场地名称 from V_排课管理_课程表周详情表 " +
				"where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"and 行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "' " +
				"and 授课周次='" + MyTools.fixSql(week) + "' and 授课计划明细编号<>'' order by 时间序列,授课计划明细编号";
			vec = db.GetContextVector(sql);
			
			//添加体锻课  lupengfei 20170928			
			int tytag=0;//体育课标记
			int tdcs=0;//体锻课次数
			int zwyk=0;//中午是否有課
			
			//获取操场编号
			String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and [教室名称]='操场' ) ";
			Vector veccc=db.GetContextVector(sqlcc);
			
			//获取实际上课周数
			String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
			Vector vecskzs=db.GetContextVector(sqlskzs);
				
			//获取每周天数
			int mzts=0;
			String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
			Vector vecmzts=db.GetContextVector(sqlmzts);
			if(vecmzts!=null&&vecmzts.size()>0){
				mzts=Integer.parseInt(vecmzts.get(0).toString());
			}
			
			//时间序列,授课计划明细编号,课程名称,授课教师姓名,实际场地名称
			
			//判断哪天没体育课,中午添加体锻课
			String ts="";
			for(int i=1;i<=mzts;i++){
				tytag=0;
				if(i<10){
					ts="0"+i;
				}else{
					ts=i+"";
				}
				for(int j=0;j<vec.size();j=j+5){
					if(ts.equals(vec.get(j).toString().substring(0, 2))){//天数相同
						if(vec.get(j+2).toString().indexOf("体育")>-1){//有体育课
							tytag=1;
						}
					}	
				}
				if(tytag==0){
					for(int k=0;k<vec.size();k=k+6){
						if((ts+"05").equals(vec.get(k).toString())){//中午有课
							zwyk=1;
							
						}
					}
					if(zwyk==0){
						if(tdcs<2){
							//添加体锻课
							//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
							vec.add(ts+"05");//时间序列
							vec.add("");//授课计划明细编号
							vec.add("体锻");//课程名称						
							vec.add("");//授课教师姓名
							vec.add("");//实际场地名称
							
							tdcs++;
						}
					}else{
						zwyk=0;
					}
				}
			}
			
			//=============================================================================================		
			
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i+=5){
					skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
					kcmc = MyTools.StrFiltr(vec.get(i+2));
					skjsxm = MyTools.StrFiltr(vec.get(i+3));
					cdmc = MyTools.StrFiltr(vec.get(i+4));
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
						if(i > 0){
							resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
									"\"授课计划明细编号\":\"" + tempSkjhmxbh + "\"," +
									"\"课程名称\":\"" + tempKcmc + "\"," +
									"\"授课教师姓名\": \"" + tempSkjsxm + "\"," + 
							        "\"实际场地名称\":\"" + tempCdmc + "\"},";
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						tempSkjhmxbh = skjhmxbh;
						tempKcmc = kcmc;
						tempSkjsxm = skjsxm;
						tempCdmc = cdmc;
					}else{
						tempSkjhmxbh += "｜"+skjhmxbh;
						tempKcmc += "｜"+kcmc;
						tempSkjsxm += "｜"+skjsxm;
						tempCdmc += "｜"+cdmc;
					}
				}
				resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
						"\"授课计划明细编号\":\"" + tempSkjhmxbh + "\"," +
						"\"课程名称\":\"" + tempKcmc + "\"," +
						"\"授课教师姓名\": \"" + tempSkjsxm + "\"," + 
				        "\"实际场地名称\":\"" + tempCdmc + "\"}";
			}
			
			//添加学生课表查询 20160209 lupengfei
			if(nodeType.equals("student")){
				//查询选修课信息 
				String sqlxxk="select distinct a.上课时间 as 时间序列,a.授课计划明细编号,b.课程名称,a.授课教师姓名,a.场地名称,a.授课周次 " +
						"from [V_规则管理_选修课授课计划明细表] a,dbo.V_规则管理_选修课授课计划主表 b,V_基础信息_选修课程信息表 c " +
						"where a.授课计划主表编号=b.授课计划主表编号  and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and a.上课时间!='' and a.授课计划明细编号 in ( " +
						"SELECT [授课计划明细编号] FROM [V_规则管理_学生选修课关系表] where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 学号='" + MyTools.fixSql(userid) + "' )";
				Vector vecxxk=db.GetContextVector(sqlxxk);
				if(vecxxk!=null&&vecxxk.size()>0){
					for(int i=0;i<vecxxk.size();i=i+6){
						if(vecxxk.get(i).toString().indexOf(",")>-1){
							String[] sjxls=vecxxk.get(i).toString().split(",");
							for(int j=0;j<sjxls.length;j++){
								if(vecxxk.get(i+5).toString().indexOf("-")>-1){
									//显示周次在授课周次范围内
									String[] skzcfw=vecxxk.get(i+5).toString().split("-");						
									if(Integer.parseInt(skzcfw[0])<=Integer.parseInt(week)&&Integer.parseInt(week)<=Integer.parseInt(skzcfw[1])){
										resultJson +=  ",{\"时间序列\": \"" + sjxls[j] + "\"," +
											"\"授课计划明细编号\":\"" + vecxxk.get(i+1).toString() + "\"," +
											"\"课程名称\":\"" + vecxxk.get(i+2).toString() + "\"," +
											"\"授课教师姓名\": \"" + vecxxk.get(i+3).toString() + "\"," + 
									        "\"实际场地名称\":\"" + vecxxk.get(i+4).toString() + "\"}";
									}
								}else{
									if(vecxxk.get(i+5).toString().equals(week)){
										resultJson +=  ",{\"时间序列\": \"" + sjxls[j] + "\"," +
												"\"授课计划明细编号\":\"" + vecxxk.get(i+1).toString() + "\"," +
												"\"课程名称\":\"" + vecxxk.get(i+2).toString() + "\"," +
												"\"授课教师姓名\": \"" + vecxxk.get(i+3).toString() + "\"," + 
										        "\"实际场地名称\":\"" + vecxxk.get(i+4).toString() + "\"}";
									}
								}
							}
						}
					}
				}
				
				//查询学生 添加课程信息表
				String sqlgr="  SELECT a.时间序列,a.相关课程表编号,b.课程名称,b.授课教师姓名,b.场地名称,b.授课周次  FROM [dbo].[V_排课管理_添加课程信息表] a,V_排课管理_课程表明细详情表 b " +
						"where a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and a.相关课程表编号=b.课程表明细编号  and a.类型='3' and a.学号='" + MyTools.fixSql(userid) + "'";
				Vector vecgr=db.GetContextVector(sqlgr);
				if(vecgr!=null&&vecgr.size()>0){
					for(int i=0;i<vecgr.size();i=i+6){
						if(vecgr.get(i+5).toString().indexOf("-")>-1){
							//显示周次在授课周次范围内
							String[] skzcfw=vecgr.get(i+5).toString().split("-");						
							if(Integer.parseInt(skzcfw[0])<=Integer.parseInt(week)&&Integer.parseInt(week)<=Integer.parseInt(skzcfw[1])){
								resultJson +=  ",{\"时间序列\": \"" + vecgr.get(i).toString() + "\"," +
									"\"授课计划明细编号\":\"" + vecgr.get(i+1).toString() + "\"," +
									"\"课程名称\":\"" + vecgr.get(i+2).toString() + "\"," +
									"\"授课教师姓名\": \"" + vecgr.get(i+3).toString() + "\"," + 
							        "\"实际场地名称\":\"" + vecgr.get(i+4).toString() + "\"}";
							}
						}else{
							if(vecgr.get(i+5).toString().equals(week)){
								resultJson +=  ",{\"时间序列\": \"" + vecgr.get(i).toString() + "\"," +
										"\"授课计划明细编号\":\"" + vecgr.get(i+1).toString() + "\"," +
										"\"课程名称\":\"" + vecgr.get(i+2).toString() + "\"," +
										"\"授课教师姓名\": \"" + vecgr.get(i+3).toString() + "\"," + 
								        "\"实际场地名称\":\"" + vecgr.get(i+4).toString() + "\"}";
							}
						}
					}
				}
				
				
			}
			//-------------------------------------------------------------------------------------------
			
		}
			
		
		resultJson += "]";
		resultVec.add(resultJson);
		
		//查询课表备注
		sql = "select distinct 备注 from V_排课管理_课程表主表 where 状态='1' " +
			"and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and 行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			resultVec.add(MyTools.StrFiltr(tempVec.get(0)));
		}else{
			resultVec.add("");
		}
		
		return resultVec;
	}
	
	/**
	 * 查询班级合班上课信息
	 * @date:2015-05-27
	 * @author:yeq
	 * @return 合班信息
	 * @throws SQLException
	 */
	public Vector loadHbInfo()throws SQLException{
		Vector tempVec = null;
		Vector vec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String jsonStr = "";
		
		//查询当前班级合班课程设置
//		sql = "select distinct a.授课计划明细编号,stuff((select '+'+t2.行政班名称 from V_规则管理_授课计划主表 t " +
//			"left join V_规则管理_授课计划明细表 t1 on t1.授课计划主表编号=t.授课计划主表编号 " +
//			"left join V_学校班级_数据子类 t2 on t2.行政班代码=t.行政班代码 where a.授课计划明细编号 like '%'+t1.授课计划明细编号+'%' order by t1.授课计划明细编号 for XML PATH('')),1,1,'')," +
//			"(select sum(总人数) from V_学校班级_数据子类 where 行政班代码 in (select e.行政班代码 from V_规则管理_授课计划主表 e " +
//			"left join V_规则管理_授课计划明细表 d on d.授课计划主表编号=e.授课计划主表编号 " +
//			"left join V_规则管理_合班表 f on f.授课计划明细编号 like '%'+d.授课计划明细编号+'%' where f.编号=a.编号)) as 总人数 " +
//			"from V_规则管理_合班表 a " +
//			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
//			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
//			"where c.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		sql = "select b.合班编号,d.行政班名称,d.总人数 from V_规则管理_授课计划明细表 a " +
			"inner join (select distinct a.授课计划明细编号 as 合班编号 from V_规则管理_合班表 a " +
			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
			"where c.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "') as b on b.合班编号 like '%'+a.授课计划明细编号+'%' " +
			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=a.授课计划主表编号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 order by 合班编号,授课计划明细编号";
		tempVec = db.GetContextVector(sql);
		
		jsonStr = "[";
		if(tempVec!=null && tempVec.size()>0){
			String hbCode = "";//合班编号
			String xzbmc = "";
			int curNum = 0;
			String tempXzbmc = "";
			int totalNum = 0;
			
			//拼接合班信息
			for(int i=0; i<tempVec.size(); i+=3){
				xzbmc = MyTools.StrFiltr(tempVec.get(i+1));
				curNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(i+2)));
				
				if(!hbCode.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i)))){
					vec.add(hbCode);
					vec.add(tempXzbmc);
					vec.add(totalNum);
					
					hbCode = MyTools.StrFiltr(tempVec.get(i));
					tempXzbmc = xzbmc;
					totalNum = curNum;
				}else{
					tempXzbmc += "+"+xzbmc;
					totalNum += curNum;
				}
			}
			vec.add(hbCode);
			vec.add(tempXzbmc);
			vec.add(totalNum);
			
			for(int i=0; i<vec.size(); i+=3){
				jsonStr += "{\"合班授课编号\":\"" + MyTools.StrFiltr(vec.get(i))+"\"," +
						"\"合班班级名称\":\"" + MyTools.StrFiltr(vec.get(i+1))+"\"," +
						"\"总人数\":\"" + MyTools.StrFiltr(vec.get(i+2))+"\"},";
			}
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		}else{
			jsonStr += "{\"合班授课编号\":\"\",\"合班班级名称\":\"\",\"总人数\":\"\"}";
		}
		
		jsonStr += "]";
		resultVec.add(jsonStr);
		
		//查询其他班级与本班合班课程的排课信息（不包括本班）
		sql = "select distinct m.时间序列,n.授课计划明细编号,n.课程代码,n.课程名称,m.行政班名称,m.授课计划明细编号,m.实际场地编号,m.实际场地名称,n.授课周次 from V_排课管理_课程表明细详情表 m " +
			"inner join (select t.授课计划明细编号,t.课程代码,t.课程名称,case t.授课周次 when 'odd' then '1' when 'even' then '2' else t.授课周次 end as 授课周次 " +
			"from V_规则管理_授课计划明细表 t inner join (select a.授课计划明细编号 from V_规则管理_合班表 a " +
			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
			"where c.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and c.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "') t1 on t1.授课计划明细编号 like '%'+t.授课计划明细编号+'%') n on m.授课计划明细编号 like '%'+n.授课计划明细编号+'%' " +
			"where m.行政班代码<>'" + MyTools.fixSql(this.getPK_XZBDM()) + "' order by m.时间序列,n.授课周次,n.课程代码";
		vec = db.GetContextVector(sql);
		
		String tempTimeOrder = "";
		String tempSkjhbh = "";
		String tempCourseCode = "";
		String tempCourseName = "";
		String tempClass = "";
		String pjSkjhbh[] = new String[0];
		String tempSiteCode = "";
		String tempSiteCodeArray[] = new String[0];
		String tempSiteName = "";
		String tempSiteNameArray[] = new String[0];
		jsonStr = "[";
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=9){
				//判断是同一时间序列,整合合班信息
				if(tempTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					//判断是否相同课程
					if(tempCourseCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
						tempSkjhbh += "+"+MyTools.StrFiltr(vec.get(i+1));
						tempClass += "、"+MyTools.StrFiltr(vec.get(i+4));
					}else{
						if(i > 0){
							jsonStr += "{\"时间序列\":\"" + tempTimeOrder + "\"," +
									"\"合班授课计划编号\":\"" + tempSkjhbh + "\"," +
									"\"合班课程名称\":\"" + tempCourseName + "\"," +
									"\"合班班级名称\":\"" + tempClass + "\"," +
									"\"实际场地编号\":\"" + tempSiteCode + "\"," +
									"\"实际场地名称\":\"" + tempSiteName + "\"},";
						}
						
						tempTimeOrder = MyTools.StrFiltr(vec.get(i));
						tempSkjhbh = MyTools.StrFiltr(vec.get(i+1));
						tempCourseCode = MyTools.StrFiltr(vec.get(i+2));
						tempCourseName = MyTools.StrFiltr(vec.get(i+3));
						tempClass = MyTools.StrFiltr(vec.get(i+4));
						pjSkjhbh = MyTools.StrFiltr(vec.get(i+5)).split("｜");
						//判断如果是拼接课程的话，解析当前课程实际场地
						if(pjSkjhbh.length > 1){
							tempSiteCodeArray = MyTools.StrFiltr(vec.get(i+6)).split("｜");
							tempSiteNameArray = MyTools.StrFiltr(vec.get(i+7)).split("｜");
							
							for(int a=0; a<pjSkjhbh.length; a++){
								if(tempSkjhbh.equalsIgnoreCase(pjSkjhbh[a])){
									tempSiteCode = tempSiteCodeArray[a];
									tempSiteName = tempSiteNameArray[a];
								}
							}
						}else{
							tempSiteCode = MyTools.StrFiltr(vec.get(i+6));
							tempSiteName = MyTools.StrFiltr(vec.get(i+7));
						}
					}
				}else{
					if(i > 0){
						jsonStr += "{\"时间序列\":\"" + tempTimeOrder + "\"," +
								"\"合班授课计划编号\":\"" + tempSkjhbh + "\"," +
								"\"合班课程名称\":\"" + tempCourseName + "\"," +
								"\"合班班级名称\":\"" + tempClass + "\"," +
								"\"实际场地编号\":\"" + tempSiteCode + "\"," +
								"\"实际场地名称\":\"" + tempSiteName + "\"},";
					}
					
					tempTimeOrder = MyTools.StrFiltr(vec.get(i));
					tempSkjhbh = MyTools.StrFiltr(vec.get(i+1));
					tempCourseCode = MyTools.StrFiltr(vec.get(i+2));
					tempCourseName = MyTools.StrFiltr(vec.get(i+3));
					tempClass = MyTools.StrFiltr(vec.get(i+4));
					pjSkjhbh = MyTools.StrFiltr(vec.get(i+5)).split("｜");
					//判断如果是拼接课程的话，解析当前课程实际场地
					if(pjSkjhbh.length > 1){
						tempSiteCodeArray = MyTools.StrFiltr(vec.get(i+6)).split("｜");
						tempSiteNameArray = MyTools.StrFiltr(vec.get(i+7)).split("｜");
						
						for(int a=0; a<pjSkjhbh.length; a++){
							if(tempSkjhbh.equalsIgnoreCase(pjSkjhbh[a])){
								tempSiteCode = tempSiteCodeArray[a];
								tempSiteName = tempSiteNameArray[a];
							}
						}
					}else{
						tempSiteCode = MyTools.StrFiltr(vec.get(i+6));
						tempSiteName = MyTools.StrFiltr(vec.get(i+7));
					}
				}
			}
			jsonStr += "{\"时间序列\":\"" + tempTimeOrder + "\"," +
					"\"合班授课计划编号\":\"" + tempSkjhbh + "\"," +
					"\"合班课程名称\":\"" + tempCourseName + "\"," +
					"\"合班班级名称\":\"" + tempClass + "\"," +
					"\"实际场地编号\":\"" + tempSiteCode + "\"," +
					"\"实际场地名称\":\"" + tempSiteName + "\"},";
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		}else{
			jsonStr += "{\"时间序列\":\"\",\"合班授课计划编号\":\"\"," +
					"\"合班课程名称\":\"\",\"合班班级名称\":\"\"," +
					"\"实际场地编号\":\"\",\"实际场地名称\":\"\"}";
		}
		jsonStr += "]";
		resultVec.add(jsonStr);
		
		return resultVec;
	}
	
	/**
	 * 格式化授课周次
	 * @date:2015-06-29
	 * @param skzc 授课周次
	 * @param xqzc 学期周次范围
	 * @return Vector 格式化后结果集[1,2,3,4,5]
	 * @author:yeq
	 */
	public Vector formatSkzc(String skzc, String xqzc){
		Vector resultVec = new Vector();
		//判断授课周次是连续周次，还是特定周次（如单双周）
		//连续周次格式,如:1-18。
		//单双周，如:odd单、even双。
		//特定周次格式,如：1#4#7#9
		if(skzc.indexOf("-") > -1){
			int tempStart = MyTools.StringToInt(skzc.split("-")[0]);
			int tempEnd = MyTools.StringToInt(skzc.split("-")[1]);
			
			for(int j=tempStart; j<tempEnd+1; j++){
				resultVec.add(String.valueOf(j));
			}
		}else if("odd".equalsIgnoreCase(skzc) || "even".equalsIgnoreCase(skzc)){//单双周
			int weekNum = Integer.parseInt(xqzc);
			int tempNum = 1;
			if("even".equalsIgnoreCase(skzc)){
				tempNum = 2;
			}
			for(int j=tempNum; j<weekNum+1; j+=2){
				resultVec.add(String.valueOf(j));
			}
		}else{//特别指定的周次
			String tempArray[] = skzc.split("#");
			
			for(int i=0; i<tempArray.length; i++){
				resultVec.add(tempArray[i]);
			}
		}
		
		return resultVec;
	}
	
	/**
	 * 格式化周次显示方式
	 * @date:2016-05-13
	 * @author:yeq
	 * @param curSkzc 授课周次
	 * @param oddArray 单周周次
	 * @param evenArray 双周周次
	 * @return String
	 * @throws SQLException
	 */
	public static String formatSkzcShow(String curSkzc, Vector oddVec, Vector evenVec){
		String result = curSkzc;
		String skzcArray[] = curSkzc.split("#");
		boolean flag = true;
		
		if(skzcArray.length == 1){
			result = skzcArray[0];
		}else{
			//判断是否为连续周次
			for(int i=0; i<skzcArray.length; i++){
				if(i < skzcArray.length-1){
					if(MyTools.StringToInt(skzcArray[i]) != MyTools.StringToInt(skzcArray[i+1])-1){
						flag = false;
						break;
					}
				}
			}
			
			if(flag == false){
				flag = true;
				
				//判断是否为单周
				for(int i=0; i<skzcArray.length; i++){
					if(skzcArray[i] != MyTools.StrFiltr(oddVec.get(i))){
						flag = false;
						break;
					}
				}
				
				if(flag == false){
					flag = true;
					
					//判断是否为双周
					for(int i=0; i<skzcArray.length; i++){
						if(skzcArray[i] != MyTools.StrFiltr(evenVec.get(i))){
							flag = false;
							break;
						}
					}
					
					if(flag == true){
						result = "even";
					}
				}else{
					result = "odd";
				}
			}else{
				result = skzcArray[0]+"-"+skzcArray[skzcArray.length-1];
			}
		}
		
		return result;
	}
	
	/**
	 * 分页查询 查询课程表列表
	 * @date:2015-09-18
	 * @author:yeq
	 * @param pageNum 页数
	 * @param page 每页数据条数
	 * @param PK_XNXQMC_CX 学年学期名称
	 * @param PK_JXXZ_CX 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queKcbList(int pageNum, int page, String PK_XNXQMC_CX, String PK_JXXZ_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select distinct a.学年学期编码 as PK_XNXQBM,b.学年学期名称 as PK_XNXQMC,c.教学性质 as jxxz " +
			"from V_排课管理_课程表主表 a " +
			"left join V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
			"left join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
			"where a.状态='1'";
		
		//判断查询条件
		if(!"".equalsIgnoreCase(PK_XNXQMC_CX)){
			sql += " and b.学年学期名称 like '%" + MyTools.fixSql(PK_XNXQMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(PK_JXXZ_CX)){
			sql += " and b.教学性质='" + MyTools.fixSql(PK_JXXZ_CX) + "'";
		}
		sql += " order by a.学年学期编码 desc";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 查询当前学期的教师信息树
	 * @date:2015-05-27
	 * @author:yeq
	 * @param level
	 * @param parentCode 专业代码
	 * @return 教师信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queTeaTree(String level, String parentCode)throws SQLException,WrongSQLException{
		Vector vec = null;
		String sql = "";
//		String admin = MyTools.getProp(request, "Base.admin");//管理员
//		String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
//		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
//		sql = "select distinct * from (select ";
//		if("0".equalsIgnoreCase(level))
//			sql += "distinct e.层级编号 as id,e.层级名称 as text,state='closed' ";
//		if("1".equalsIgnoreCase(level))
//			sql += "a.工号 as id,a.姓名 as text,state='open' ";
//		
//		sql += "from V_教职工基本数据子类 a " +
//			"inner join (select distinct 专业代码,授课教师编号 from V_排课管理_课程表周详情表 " +
//			"where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 课程代码<>'') b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' " +
//			"left join sysUserAuth c on c.UserCode=a.工号 " +
//			"left join V_权限层级关系表 d on d.权限编号=c.AuthCode " +
//			"left join V_层级表 e on e.层级编号=d.层级编号 where 1=1 ";
//		
//		if("1".equalsIgnoreCase(level))
//			sql += "and e.层级编号='" + MyTools.fixSql(parentCode) + "' ";
//		
//		if(this.getAuth().indexOf(majorTeacher) > -1)
//			sql += "and b.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
//		else if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(pubTeacher)<0)
//			sql += "and a.工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
//		
//		sql += "union all select ";
//		if("0".equalsIgnoreCase(level))
//			sql += "distinct i.层级编号 as id,i.层级名称 as text,state='closed' ";
//		if("1".equalsIgnoreCase(level))
//			sql += "a.工号 as id,a.姓名 as text,state='open' ";
//		
//		//选课信息
//		sql += "from V_教职工基本数据子类 a " +
//			"left join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' " +
//			"left join V_规则管理_选修课授课计划明细表 c on c.授课计划主表编号=b.授课计划主表编号 " +
//			"inner join V_规则管理_学生选修课关系表 d on d.授课计划明细编号=c.授课计划明细编号 " +
//			"inner join V_学生基本数据子类 e on e.学号=d.学号 " +
//			"inner join V_学校班级_数据子类 f on f.行政班代码=e.行政班代码 " +
//			"inner join sysUserAuth g on g.UserCode=a.工号 " +
//			"inner join V_权限层级关系表 h on h.权限编号=g.AuthCode " +
//			"inner join V_层级表 i on i.层级编号=h.层级编号 " +
//			"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//		
//		if("1".equalsIgnoreCase(level))
//			sql += " and i.层级编号='" + MyTools.fixSql(parentCode) + "'";
//		
//		if(this.getAuth().indexOf(majorTeacher) > -1)
//			sql += " and f.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
//		else if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(pubTeacher)<0)
//			sql += " and a.工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
//		
//		sql += ") as t where id is not null order by text,id";
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "select distinct * from (" +
			"select 工号 as id,姓名 as text,state='open' from V_教职工基本数据子类 where 工号='" + MyTools.fixSql(this.getUSERCODE()) + "' " +
			"union all " +
			"select a.工号 as id,a.姓名 as text,state='open' from V_教职工基本数据子类 a " +
			"inner join (select distinct t1.授课教师编号 from V_排课管理_课程表周详情表 t1 " +
			//"left join V_学校班级_数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
			"left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 " +
			"where t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and t1.课程代码<>''";
		//权限判断
		if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
			sql += " and (1=2";
			//班主任
			if(this.getAuth().indexOf(bzr) > -1){
				sql += " or t2.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			//系部教务人员
			if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
				sql += " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' ";
		
		//判断如果是管理员或全校教务权限，需查询选修课教师信息
		if(this.getAuth().indexOf(admin)>-1 || this.getAuth().indexOf(jxzgxz)>-1 || this.getAuth().indexOf(qxjdzr)>-1 || this.getAuth().indexOf(qxjwgl)>-1){
			sql += "union all " +
				"select a.工号 as id,a.姓名 as text,state='open' from V_教职工基本数据子类 a " +
				"inner join (select 授课教师编号 from V_规则管理_选修课授课计划主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') b " +
				"on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%'";
		}
		sql += ") as t where id is not null";
		if(!"".equalsIgnoreCase(this.getPK_SKJSBH())){
			sql += " and text like '%" + MyTools.fixSql(this.getPK_SKJSBH()) + "%'";
		}
		sql += " order by text,id";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询教师课程表
	 * @date:2015-05-27
	 * @author:yeq
	 * @param week 授课周次
	 * @return 教师课程表
	 * @throws SQLException
	 */
	public String loadTeaKcb(String week)throws SQLException{
		Vector vec = new Vector();
		Vector tempVec = null;
		String sql = "";
		String resultJson = "[";
		int xqzc = 0;
		Vector oddVec = new Vector();
		Vector evenVec = new Vector();
		String sjxl = "";
		String skjhmxbh = "";
		String kcbh = "";
		String kcmc = "";
		String xzbdm = "";
		String xzbmc = "";
		String skjsbh = "";
		String skjsxm = "";
		String cdbh = "";
		String cdmc = "";
		String skzc = "";
		String hbsz = "";
		String tempSkjhmxbh = "";
		String tempKcmc = "";
		String tempSkjsxm = "";
		String tempXzbmc = "";
		String tempCdmc = "";
		String tempSkzc = "";
		String preSkjhmxbh = "";
		Vector xxkVec = new Vector();
		int xxkIndex = 0;
		
		//获取本学期授课周次
		sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			xqzc = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(0)));
			
			for(int i=1; i<xqzc+1; i+=2){
				oddVec.add(i);
				evenVec.add(i+1);
			}
		}
		
		//获取选修课信息
		sql = "select c.时间序列,b.课程名称,a.选修班名称,a.授课教师姓名,c.实际场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a " +
			"inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
			"left join V_排课管理_选修课课程表信息表 c on c.授课计划明细编号=a.授课计划明细编号 " +
			"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and a.授课教师编号 like '%" + MyTools.fixSql(this.getPK_SKJSBH()) + "%' " +
			"order by c.时间序列,cast((case substring(a.授课周次, 2, 1) when '｜' then substring(a.授课周次, 1, 1) when '&' then substring(a.授课周次, 1, 1) when '#' then substring(a.授课周次, 1, 1) " +
			"when '-' then substring(a.授课周次, 1, 1) when 'd' then '1' when 'v' then '2' else substring(a.授课周次, 1, 2) end) as int)";
		tempVec = db.GetContextVector(sql);
		
		if("all".equalsIgnoreCase(week)){
			//合并相同时间序列选修课
			for(int i=0; i<tempVec.size(); i+=6){
				sjxl = MyTools.StrFiltr(tempVec.get(i));
				kcmc = MyTools.StrFiltr(tempVec.get(i+1));
				xzbmc = MyTools.StrFiltr(tempVec.get(i+2));
				skjsxm = MyTools.StrFiltr(tempVec.get(i+3));
				cdmc = MyTools.StrFiltr(tempVec.get(i+4));
				skzc = MyTools.StrFiltr(tempVec.get(i+5));
				xxkIndex = xxkVec.indexOf(sjxl);
				
				if(xxkIndex < 0){
					xxkVec.add(sjxl);
					xxkVec.add(kcmc);
					xxkVec.add(xzbmc);
					xxkVec.add(skjsxm);
					xxkVec.add(cdmc);
					xxkVec.add(skzc);
				}else{
					xxkVec.set(xxkIndex+1, MyTools.StrFiltr(xxkVec.get(xxkIndex+1))+"｜"+kcmc);
					xxkVec.set(xxkIndex+2, MyTools.StrFiltr(xxkVec.get(xxkIndex+2))+"｜"+xzbmc);
					xxkVec.set(xxkIndex+3, MyTools.StrFiltr(xxkVec.get(xxkIndex+3))+"｜"+skjsxm);
					xxkVec.set(xxkIndex+4, MyTools.StrFiltr(xxkVec.get(xxkIndex+4))+"｜"+cdmc);
					xxkVec.set(xxkIndex+5, MyTools.StrFiltr(xxkVec.get(xxkIndex+5))+"｜"+skzc);
				}
			}
			
//			sql = "select distinct a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次,b.授课计划明细编号 as 合班设置 " +
//				"from V_排课管理_课程表周详情表 a left join V_规则管理_合班表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//				"where a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and a.授课教师编号 like '%" + MyTools.fixSql(this.getPK_SKJSBH()) + "%' order by a.时间序列,cast(a.授课周次 as int),a.授课计划明细编号,a.课程代码";
			sql = "select t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.行政班代码,t.行政班名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次,t.合班设置 " + 
				"from (select distinct a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次,b.授课计划明细编号 as 合班设置," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " +
				"left join V_规则管理_合班表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " + 
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"and a.授课教师编号 like '%" + MyTools.fixSql(this.getPK_SKJSBH()) + "%') as t " + 
				"order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号,t.课程代码";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				boolean flag = true;
				//拼接同一课程周次
				while(flag){
					sjxl = MyTools.StrFiltr(tempVec.get(0));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
					xzbdm = MyTools.StrFiltr(tempVec.get(2));
					xzbmc = MyTools.StrFiltr(tempVec.get(3));
					kcbh = MyTools.StrFiltr(tempVec.get(4));
					kcmc = MyTools.StrFiltr(tempVec.get(5));
					skjsbh = MyTools.StrFiltr(tempVec.get(6));
					skjsxm = MyTools.StrFiltr(tempVec.get(7));
					cdbh = MyTools.StrFiltr(tempVec.get(8));
					cdmc = MyTools.StrFiltr(tempVec.get(9));
					skzc = MyTools.StrFiltr(tempVec.get(10));
					hbsz = MyTools.StrFiltr(tempVec.get(11));
					for(int i=0; i<12; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=12){
						if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))
							&& xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) 
							&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+8)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+10));
							for(int j=0; j<12; j++){
								tempVec.remove(i);
							}
							i -= 12;
						}
					}
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(xzbmc);
					vec.add(kcmc);
					vec.add(skjsxm);
					vec.add(cdmc);
					vec.add(skzc);
					vec.add(hbsz);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
				
				//拼接同一时间序列课程信息
				sjxl = "";
				for(int i=0; i<vec.size(); i+=8){
					skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
					kcmc = MyTools.StrFiltr(vec.get(i+2));
					xzbmc = MyTools.StrFiltr(vec.get(i+3));
					skjsxm = MyTools.StrFiltr(vec.get(i+4));
					cdmc = MyTools.StrFiltr(vec.get(i+5));
					skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i+6)), oddVec, evenVec);;
					hbsz = MyTools.StrFiltr(vec.get(i+7));
					
					//判断当前单元格是否有合班课程
					if(!"".equalsIgnoreCase(hbsz)){
						for(int j=(i+8); j<vec.size(); j+=8){
							if(!MyTools.StrFiltr(vec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(j)))){
								break;
							}
							
							if(hbsz.indexOf(skjhmxbh)>-1 && hbsz.indexOf(MyTools.StrFiltr(vec.get(j+1)))>-1){
								xzbmc += "、"+MyTools.StrFiltr(vec.get(j+3));
								
								for(int k=0; k<8; k++){
									vec.remove(j);
								}
								j -= 8;
							}
						}
					}
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
						if(i > 0){
							//判断是否有选修课
							xxkIndex = xxkVec.indexOf(sjxl);
							if(xxkIndex > -1){
								tempKcmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+1));
								tempXzbmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+2));
								tempSkjsxm += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+3));
								tempCdmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+4));
								tempSkzc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+5));
								
								for(int j=0; j<6; j++){
									xxkVec.remove(xxkIndex);
								}
							}
							
							resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
									"\"课程名称\":\"" + tempKcmc + "\"," +
									"\"行政班名称\":\"" + tempXzbmc + "\"," +
									"\"授课教师姓名\": \"" + tempSkjsxm + "\"," +
									"\"实际场地名称\": \"" + tempCdmc + "\"," + 
							        "\"授课周次\":\"" + tempSkzc + "\"},";
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						tempSkjhmxbh = skjhmxbh;
						tempKcmc = kcmc;
						tempXzbmc = xzbmc;
						tempSkjsxm = skjsxm;
						tempCdmc = cdmc;
						tempSkzc = skzc;
					}else{
						if(skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i-8+1)))){
							//拼接课程信息
							tempSkjsxm += "&"+skjsxm;
							tempCdmc += "&"+cdmc;
							tempSkzc += "&"+skzc;
						}else{
							tempKcmc += "｜"+kcmc;
							tempXzbmc += "｜"+xzbmc;
							tempSkjsxm += "｜"+skjsxm;
							tempCdmc += "｜"+cdmc;
							tempSkzc += "｜"+skzc;
						}
					}
				}
				//判断是否有选修课
				xxkIndex = xxkVec.indexOf(sjxl);
				if(xxkIndex > -1){
					tempKcmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+1));
					tempXzbmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+2));
					tempSkjsxm += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+3));
					tempCdmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+4));
					tempSkzc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+5));
					
					for(int j=0; j<6; j++){
						xxkVec.remove(xxkIndex);
					}
				}
				resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
						"\"课程名称\":\"" + tempKcmc + "\"," +
						"\"行政班名称\":\"" + tempXzbmc + "\"," +
						"\"授课教师姓名\": \"" + tempSkjsxm + "\"," +
						"\"实际场地名称\": \"" + tempCdmc + "\"," + 
				        "\"授课周次\":\"" + tempSkzc + "\"},";
			}
			//剩余选修课
			if(xxkVec.size() > 0){
				for(int i=0; i<xxkVec.size(); i+=6){
					resultJson +=  "{\"时间序列\": \"" + MyTools.StrFiltr(xxkVec.get(i)) + "\"," +
							"\"课程名称\":\"" + MyTools.StrFiltr(xxkVec.get(i+1)) + "\"," +
							"\"行政班名称\":\"" + MyTools.StrFiltr(xxkVec.get(i+2)) + "\"," +
							"\"授课教师姓名\": \"" + MyTools.StrFiltr(xxkVec.get(i+3)) + "\"," +
							"\"实际场地名称\": \"" + MyTools.StrFiltr(xxkVec.get(i+4)) + "\"," + 
					        "\"授课周次\":\"" + MyTools.StrFiltr(xxkVec.get(i+5)) + "\"},";
				}
			}
		}else{
			//判断当前周是否有选修课信息
			for(int i=0; i<tempVec.size(); i+=6){
				if(formatSkzc(MyTools.StrFiltr(tempVec.get(i+5)), String.valueOf(xqzc)).indexOf(week) > -1){
					xxkVec.add(MyTools.StrFiltr(tempVec.get(i)));
					xxkVec.add(MyTools.StrFiltr(tempVec.get(i+1)));
					xxkVec.add(MyTools.StrFiltr(tempVec.get(i+2)));
					xxkVec.add(MyTools.StrFiltr(tempVec.get(i+3)));
					xxkVec.add(MyTools.StrFiltr(tempVec.get(i+4)));
				}
			}
			
			sql = "select distinct a.时间序列,a.授课计划明细编号,a.课程名称,a.行政班名称,a.授课教师姓名,a.场地名称,b.授课计划明细编号 as 合班设置 " +
				"from V_排课管理_课程表周详情表 a left join V_规则管理_合班表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"and a.授课教师编号 like '%" + MyTools.fixSql(this.getPK_SKJSBH()) + "%' " +
				"and a.授课周次='" + MyTools.fixSql(week) + "' " +
				"order by a.时间序列,a.课程名称";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				for(int i=0; i<vec.size(); i+=7){
					skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
					kcmc = MyTools.StrFiltr(vec.get(i+2));
					xzbmc = MyTools.StrFiltr(vec.get(i+3));
					skjsxm = MyTools.StrFiltr(vec.get(i+4));
					cdmc = MyTools.StrFiltr(vec.get(i+5));
					hbsz = MyTools.StrFiltr(vec.get(i+6));
					
					//判断当前单元格是否有合班课程
					if(!"".equalsIgnoreCase(hbsz)){
						for(int j=(i+7); j<vec.size(); j+=7){
							if(!MyTools.StrFiltr(vec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(j)))){
								break;
							}
							
							if(hbsz.indexOf(skjhmxbh)>-1 && hbsz.indexOf(MyTools.StrFiltr(vec.get(j+1)))>-1){
								xzbmc += "、"+MyTools.StrFiltr(vec.get(j+3));
								
								for(int k=0; k<7; k++){
									vec.remove(j);
								}
								j -= 7;
							}
						}
					}
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
						if(i > 0){
							//判断是否有选修课
							xxkIndex = xxkVec.indexOf(sjxl);
							if(xxkIndex > -1){
								tempKcmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+1));
								tempXzbmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+2));
								tempSkjsxm += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+3));
								tempCdmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+3));
								
								for(int j=0; j<5; j++){
									xxkVec.remove(xxkIndex);
								}
							}
							
							resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
									"\"课程名称\":\"" + tempKcmc + "\"," +
									"\"行政班名称\":\"" + tempXzbmc + "\"," +
									"\"授课教师姓名\":\"" + tempSkjsxm + "\"," + 
									"\"实际场地名称\": \"" + tempCdmc + "\"},";
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						tempKcmc = kcmc;
						tempXzbmc = xzbmc;
						tempSkjsxm = skjsxm;
						tempCdmc = cdmc;
					}else{
						tempSkjhmxbh += "｜"+skjhmxbh;
						tempKcmc += "｜"+kcmc;
						tempXzbmc += "｜"+xzbmc;
						tempSkjsxm += "｜"+skjsxm;
						tempCdmc += "｜"+cdmc;
					}
				}
				//判断是否有选修课
				xxkIndex = xxkVec.indexOf(sjxl);
				if(xxkIndex > -1){
					tempKcmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+1));
					tempXzbmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+2));
					tempSkjsxm += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+3));
					tempCdmc += "｜"+MyTools.StrFiltr(xxkVec.get(xxkIndex+4));
					
					for(int j=0; j<5; j++){
						xxkVec.remove(xxkIndex);
					}
				}
				resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
						"\"课程名称\":\"" + tempKcmc + "\"," +
						"\"行政班名称\":\"" + tempXzbmc + "\"," +
						"\"授课教师姓名\":\"" + tempSkjsxm + "\"," + 
						"\"实际场地名称\": \"" + tempCdmc + "\"},";
			}
			//剩余选修课
			if(xxkVec.size() > 0){
				for(int i=0; i<xxkVec.size(); i+=5){
					resultJson +=  "{\"时间序列\": \"" + MyTools.StrFiltr(xxkVec.get(i)) + "\"," +
							"\"课程名称\":\"" + MyTools.StrFiltr(xxkVec.get(i+1)) + "\"," +
							"\"行政班名称\":\"" + MyTools.StrFiltr(xxkVec.get(i+2)) + "\"," +
							"\"授课教师姓名\": \"" + MyTools.StrFiltr(xxkVec.get(i+3)) + "\"," +
							"\"实际场地名称\": \"" + MyTools.StrFiltr(xxkVec.get(i+4)) + "\"},";
				}
			}
		}
		
		resultJson = resultJson.substring(0, resultJson.length()-1);
		if(resultJson.length() > 0){
			resultJson += "]";
		}
		return resultJson;
	}
	
	/**
	 * 查询班级课程表总表
	 * @date:2015-05-27
	 * @author:yeq
	 * @return 班级课程表总表
	 */
	public String loadAllClassKcb() throws SQLException{
		Vector vec = null;
		Vector tempVec = null;
		Vector classInfoVec = null;
		Vector courseVec = new Vector();
		Vector hbSetVec = null;
		String sql = "";
		String jsonStr = "{";
		String tempClassCode = "";
		int xqzc = 0;
		Vector oddVec = new Vector();
		Vector evenVec = new Vector();
		String xzbdm = "";
		String xzbmc = "";
		String sjxl = "";
		String skjhmxbh = "";
		String kcmc = "";
		String skjsbh = "";
		String skjsxm = "";
		String cdbh = "";
		String cdmc = "";
		String skzc = "";
		String tempSkjhmxbh = "";
		String tempKcmc = "";
		String tempSkjsxm = "";
		String tempCdmc = "";
		String tempSkzc = "";
		boolean flag = true;
		int mzts = 0;
		int zjs = 0;
		String mtjs = "";
		String tempOrder = "";
		
		//获取学期时间相关信息
		vec = this.loadBlankKcbInfo();
		if(vec!=null && vec.size()>0){
			mzts = MyTools.StringToInt(MyTools.StrFiltr(vec.get(1)));
			zjs = MyTools.StringToInt(MyTools.StrFiltr(vec.get(2)))+MyTools.StringToInt(MyTools.StrFiltr(vec.get(3)))+MyTools.StringToInt(MyTools.StrFiltr(vec.get(4)))+MyTools.StringToInt(MyTools.StrFiltr(vec.get(5)));
			
			jsonStr += "\"semesterInfo\":" +
					"{\"学期周次\":\"" + MyTools.StrFiltr(vec.get(0)) + "\"," +
					"\"每周天数\":\"" + MyTools.StrFiltr(vec.get(1)) + "\"," +
					"\"上午节数\":\"" + MyTools.StrFiltr(vec.get(2)) + "\"," +
					"\"中午节数\":\"" + MyTools.StrFiltr(vec.get(3)) + "\"," +
					"\"下午节数\":\"" + MyTools.StrFiltr(vec.get(4)) + "\"," +
					"\"晚上节数\":\"" + MyTools.StrFiltr(vec.get(5)) + "\"," +
					"\"节次时间\":\"" + MyTools.StrFiltr(vec.get(6)) + "\"},";
		}
		
		//获取本学期授课周次
		sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			xqzc = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));
			
			for(int i=1; i<xqzc+1; i+=2){
				oddVec.add(i);
				evenVec.add(i+1);
			}
		}
		vec.clear();
		
//		sql = "select distinct * from (" +
//			"select b.行政班代码,b.行政班名称,isnull(b.总人数,0) as 班级人数,c.备注,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师姓名,a.实际场地名称,a.授课周次详情 as 授课周次 " +
//			"from V_排课管理_课程表明细详情表 a " +
//			"left join V_学校班级_数据子类 b on a.行政班代码=b.行政班代码 " +
//			"left join V_排课管理_课程表主表 c on a.学年学期编码=c.学年学期编码 and a.行政班代码=c.行政班代码 " +
//			"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//			"and a.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "' " +
//			"and a.时间序列 not in (select 时间序列 from V_排课管理_添加课程信息表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 行政班代码=a.行政班代码) " +
//			"union all " +
//			"select a.行政班代码,'','','',a.时间序列,'TJKC_'+a.时间序列,a.课程名称,a.授课教师姓名,a.场地名称,case a.类型 when '1' then '学期课程' when '2' then '整班课程' end " +
//			"from V_排课管理_添加课程信息表 a " +
//			"inner join V_学校班级_数据子类 b on a.行政班代码=b.行政班代码 " +
//			"where a.状态='1' and a.类型='2' " +
//			"and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//			"and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'" +
//			") as t order by 行政班代码,时间序列";
		//查询相关行政班信息
//		sql = "select a.行政班代码,a.行政班名称,(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数,b.备注 from V_学校班级_数据子类 a " +
//			"left join V_排课管理_课程表主表 b on b.行政班代码=a.行政班代码 " +
//			"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//			"and a.系部代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "' order by a.行政班代码";
		sql = "select a.班级编号,a.班级名称," +
			//"(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数," +
			"a.总人数,c.教室名称,isnull(b.备注,'') " +
			//"from V_学校班级_数据子类 a " +
			"from V_基础信息_班级信息表 a " +
			"left join V_排课管理_课程表主表 b on b.行政班代码=a.班级编号 and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"left join V_教室数据类 c on c.教室编号=a.教室编号 " +
			"where a.系部代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "' " +
			"and cast(left(a.年级代码,2) as int) in (" +
			"cast(right(left('" + MyTools.fixSql(this.getPK_XNXQBM()) + "',4),2) as int)-2," +
			"cast(right(left('" + MyTools.fixSql(this.getPK_XNXQBM()) + "',4),2) as int)-1," +
			"cast(right(left('" + MyTools.fixSql(this.getPK_XNXQBM()) + "',4),2) as int)) " +
			"order by a.班级编号";
		classInfoVec = db.GetContextVector(sql);
		
		if(classInfoVec!=null && classInfoVec.size()>0){
			//查询合班信息
//			sql = "select distinct b.时间序列,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from V_规则管理_授课计划明细表 a " +
//				"inner join V_排课管理_课程表明细详情表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//				"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//				"where b.学年学期编码='" + MyTools.fixSql(" + MyTools.fixSql(this.getPK_XNXQBM()) + ") + "' " +
//				"order by b.时间序列";
//			hbSetVec = db.GetContextVector(sql);
			
			//查询相关课程表信息
//			sql = "select distinct 行政班代码,时间序列,授课计划明细编号,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) as 授课周次 " +
//				"from V_排课管理_课程表周详情表 where 状态='1' and 课程类型 in ('01','02') and 授课计划明细编号<>'' " +
//				"and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and 专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "' " +
//				"order by 行政班代码,时间序列,cast(授课周次 as int)";
//			sql = "select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//				"from V_排课管理_课程表周详情表 a " +
//				//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
//				"where a.状态='1' and a.授课计划明细编号<>'' " +
//				"and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and b.系部代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "' " +
//				"order by a.行政班代码,a.时间序列,cast(a.授课周次 as int)";
			sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
				"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " + 
				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"and b.系部代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "' " +
				"and 授课计划明细编号<>'') as t " + 
				"order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
				tempVec = db.GetContextVector(sql);
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				//拼接课程周次
				while(flag){
					xzbdm = MyTools.StrFiltr(tempVec.get(0));
					sjxl = MyTools.StrFiltr(tempVec.get(1));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
					kcmc = MyTools.StrFiltr(tempVec.get(3));
					skjsbh = MyTools.StrFiltr(tempVec.get(4));
					skjsxm = MyTools.StrFiltr(tempVec.get(5));
					cdbh = MyTools.StrFiltr(tempVec.get(6));
					cdmc = MyTools.StrFiltr(tempVec.get(7));
					skzc = MyTools.StrFiltr(tempVec.get(8));
					for(int i=0; i<9; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=9){
						if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) 
							&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+8));
							for(int j=0; j<9; j++){
								tempVec.remove(i);
							}
							i -= 9;
						}
					}
					vec.add(xzbdm);
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(kcmc);
					vec.add(skjsxm);
					vec.add(cdmc);
					vec.add(skzc);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
			}
			
			tempVec = new Vector();
			for(int i=0; i<vec.size(); i+=7){
				skjsxm = MyTools.StrFiltr(vec.get(i+4));
				cdmc = MyTools.StrFiltr(vec.get(i+5));
				skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i+6)), oddVec, evenVec);
				
				if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
					if(i > 0){
						tempVec.add(xzbdm);
						tempVec.add(sjxl);
						tempVec.add(skjhmxbh);
						tempVec.add(kcmc);
						tempVec.add(tempSkjsxm);
						tempVec.add(tempCdmc);
						tempVec.add(tempSkzc);
					}
					
					xzbdm = MyTools.StrFiltr(vec.get(i));
					sjxl = MyTools.StrFiltr(vec.get(i+1));
					skjhmxbh = MyTools.StrFiltr(vec.get(i+2));
					kcmc = MyTools.StrFiltr(vec.get(i+3));
					tempSkjsxm = skjsxm;
					tempCdmc = cdmc;
					tempSkzc = skzc;
				}else{
					tempSkjsxm += "&"+skjsxm;
					tempCdmc += "&"+cdmc;
					tempSkzc += "&"+skzc;
				}
			}
			tempVec.add(xzbdm);
			tempVec.add(sjxl);
			tempVec.add(skjhmxbh);
			tempVec.add(kcmc);
			tempVec.add(tempSkjsxm);
			tempVec.add(tempCdmc);
			tempVec.add(tempSkzc);
			
			for(int i=0; i<tempVec.size(); i+=7){
				skjhmxbh = MyTools.StrFiltr(tempVec.get(i+2));
				kcmc = MyTools.StrFiltr(tempVec.get(i+3));
				skjsxm = MyTools.StrFiltr(tempVec.get(i+4));
				cdmc = MyTools.StrFiltr(tempVec.get(i+5));
				skzc = MyTools.StrFiltr(tempVec.get(i+6));
				
				if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))){
					if(i > 0){
						courseVec.add(xzbdm);
						courseVec.add(sjxl);
						courseVec.add(tempSkjhmxbh);
						courseVec.add(tempKcmc);
						courseVec.add(tempSkjsxm);
						courseVec.add(tempCdmc);
						courseVec.add(tempSkzc);
					}
					
					xzbdm = MyTools.StrFiltr(tempVec.get(i));
					sjxl = MyTools.StrFiltr(tempVec.get(i+1));
					tempSkjhmxbh = skjhmxbh;
					tempKcmc = kcmc;
					tempSkjsxm = skjsxm;
					tempCdmc = cdmc;
					tempSkzc = skzc;
				}else{
					tempSkjhmxbh += "｜"+skjhmxbh;
					tempKcmc += "｜"+kcmc;
					tempSkjsxm += "｜"+skjsxm;
					tempCdmc += "｜"+cdmc;
					tempSkzc += "｜"+skzc;
				}
			}
			courseVec.add(xzbdm);
			courseVec.add(sjxl);
			courseVec.add(tempSkjhmxbh);
			courseVec.add(tempKcmc);
			courseVec.add(tempSkjsxm);
			courseVec.add(tempCdmc);
			courseVec.add(tempSkzc);
			
			//添加体锻课  lupengfei 20170928			
			int tytag=0;//体育课标记
			int tdcs=0;//体锻课次数
			int zwyk=0;//中午是否有課
			
			//获取操场编号
			String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and [教室名称]='操场' ) ";
			Vector veccc=db.GetContextVector(sqlcc);
			
			//获取实际上课周数
			String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
			Vector vecskzs=db.GetContextVector(sqlskzs);
				
			//获取每周天数
			int mztsx=0;
			String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
			Vector vecmzts=db.GetContextVector(sqlmzts);
			if(vecmzts!=null&&vecmzts.size()>0){
				mztsx=Integer.parseInt(vecmzts.get(0).toString());
			}
			
			//时间序列,授课计划明细编号,课程名称,授课教师姓名,实际场地名称,授课周次详情
			
			//循环班级
			for(int b=0;b<classInfoVec.size();b=b+5){
				tdcs=0;//体锻课次数		
				//判断哪天没体育课,中午添加体锻课
				String ts="";
				for(int i=1;i<=mztsx;i++){
					tytag=0;//体育课标记
					zwyk=0;//中午是否有課
					if(i<10){
						ts="0"+i;
					}else{
						ts=i+"";
					}
					for(int j=0;j<courseVec.size();j=j+7){ 
						if(courseVec.get(j).toString().equals(classInfoVec.get(b).toString())){//班级相同
							if(ts.equals(courseVec.get(j+1).toString().substring(0, 2))){//天数相同
								if(courseVec.get(j+3).toString().indexOf("体育")>-1){//有体育课
									tytag=1;
								}
							}
						}					
					}
					if(tytag==0){			
						for(int k=0;k<courseVec.size();k=k+7){
							if(courseVec.get(k).toString().equals(classInfoVec.get(b).toString())){//班级相同
								if((ts+"05").equals(courseVec.get(k+1).toString())){//中午有课
									zwyk=1;									
								}
							}		
						}
						if(zwyk==0){ 
							if(tdcs<2){
								//添加体锻课
								int existkc=0;//班级有课
								for(int t=0;t<courseVec.size();t=t+7){
									if(courseVec.get(t).toString().equals(classInfoVec.get(b).toString())){//班级相同
										existkc=1;
										break;
									}
								}
								if(existkc==1){
									//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
									courseVec.add(classInfoVec.get(b).toString());
									courseVec.add(ts+"05");//时间序列
									courseVec.add("SKJHMX_TD");//授课计划明细编号
									courseVec.add("体锻");//课程名称								
									courseVec.add("");//授课教师姓名
									courseVec.add("");//实际场地名称
									courseVec.add("");//授课周次详情
									
									tdcs++;
								}							
							}
						}else{
							zwyk=0;
						}
					}
				}
			}

			//=============================================================================================			
			
			flag = false;
			jsonStr += "\"allClassKcb\":[";
			for(int i=0; i<classInfoVec.size(); i+=5){
				tempClassCode = MyTools.StrFiltr(classInfoVec.get(i));
				
				jsonStr += "{\"班级名称\":\"" + MyTools.StrFiltr(classInfoVec.get(i+1)) + "\"," +
						"\"班级人数\":\"" + MyTools.StrFiltr(classInfoVec.get(i+2))+"人" + "\"," +
						"\"教室名称\":\"" + MyTools.StrFiltr(classInfoVec.get(i+3)) + "\"," +
						"\"备注\":\"" + MyTools.StrFiltr(classInfoVec.get(i+4)) + "\"," +
						"\"课表信息\":[";
				
				for(int j=1; j<mzts+1; j++){
					for(int k=1; k<zjs+1; k++){
						tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
						
						flag = false;
						for(int a=0; a<courseVec.size(); a+=7){
							if(tempClassCode.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a))) && tempOrder.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a+1)))){
								jsonStr += "{\"时间序列\":\"" + MyTools.StrFiltr(courseVec.get(a+1)) + "\"," +
										"\"授课计划明细编号\":\"" + MyTools.StrFiltr(courseVec.get(a+2)) + "\"," +
										"\"课程名称\":\"" + MyTools.StrFiltr(courseVec.get(a+3)) + "\"," +
										"\"授课教师姓名\":\"" + MyTools.StrFiltr(courseVec.get(a+4)) + "\"," +
										"\"实际场地名称\":\"" + MyTools.StrFiltr(courseVec.get(a+5)) + "\"," +
										"\"授课周次\":\"" + MyTools.StrFiltr(courseVec.get(a+6)) + "\"},";
								flag = true;
								break;
							}
						}
						if(flag == false){
							jsonStr += "{\"时间序列\":\"" + tempOrder + "\"," +
									"\"授课计划明细编号\":\"\",\"课程名称\":\"\"," +
									"\"授课教师姓名\":\"\",\"实际场地名称\":\"\"," +
									"\"授课周次\":\"\"},";
						}
					}
				}
				jsonStr = jsonStr.substring(0, jsonStr.length()-1);
				jsonStr += "]},";
			}
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
			jsonStr += "]";
		}
		jsonStr += "}";
		return stringToJson(jsonStr);
	}
	
	//当文本中含有如下特殊字符时，此方法可以成功处理，让其在前台被正确解析，注意：此法不能处理单引号
	public static String stringToJson(String s) {    
		StringBuffer sb = new StringBuffer ();
		
		for (int i=0; i<s.length(); i++) {     
			char c = s.charAt(i);
			
			switch (c) {     
//				case '\"':     
//					sb.append("\\\"");     
//					break;     
//				case '\\':   //如果不处理单引号，可以释放此段代码，若结合下面的方法处理单引号就必须注释掉该段代码
//					sb.append("\\\\");     
//					break;     
				case '/':     
					sb.append("\\/");     
					break;     
				case '\b':      //退格
					sb.append("\\b");     
					break;     
				case '\f':      //走纸换页
					sb.append("\\f");     
					break;     
				case '\n':     
					sb.append("\\n"); //换行    
					break;     
				case '\r':      //回车
					sb.append("\\r");     
					break;     
				case '\t':      //横向跳格
					sb.append("\\t");     
					break;     
				default:     
				sb.append(c);    
			}
		}
		return sb.toString();     
	}
	
	/**
	 * 查询教师课程表总表
	 * @date:2015-05-27
	 * @author:yeq
	 * @return 教师课程表总表
	 */
	public String loadAllTeaKcb() throws SQLException{
		String sql = "";
		String jsonStr = "{";
		Vector vec = null;
		Vector tempVec = null;
		Vector teaVec = null;
		Vector xxkVec = new Vector();
		Vector hbSetVec = null;
		int xqzc = 0;
		int mzts = 0;
		int zjs = 0;
		Vector oddVec = new Vector();
		Vector evenVec = new Vector();
		String tempTeaCode = "";
		String tempOrder = "";
		String sjxl = "";
		String skjhmxbh = "";
		String kcbh = "";
		String kcmc = "";
		String xzbdm = "";
		String xzbmc = "";
		String skjsbh = "";
		String skjsxm = "";
		String cdbh = "";
		String cdmc = "";
		String skzc = "";
		String tempSkjhmxbh = "";
		String tempKcmc = "";
		String tempXzbmc = "";
		String tempSkjsbh = "";
		String tempSkjsxm = "";
		String tempCdmc = "";
		String tempSkzc = "";
		String hbInfo = "";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		//获取当前学期信息
		vec = this.loadBlankKcbInfo();
		if(vec!=null && vec.size()>0){
			jsonStr += "\"semesterInfo\":" +
					"{\"学期周次\":\"" + MyTools.StrFiltr(vec.get(0)) + "\"," +
					"\"每周天数\":\"" + MyTools.StrFiltr(vec.get(1)) + "\"," +
					"\"上午节数\":\"" + MyTools.StrFiltr(vec.get(2)) + "\"," +
					"\"中午节数\":\"" + MyTools.StrFiltr(vec.get(3)) + "\"," +
					"\"下午节数\":\"" + MyTools.StrFiltr(vec.get(4)) + "\"," +
					"\"晚上节数\":\"" + MyTools.StrFiltr(vec.get(5)) + "\"," +
					"\"节次时间\":\"" + MyTools.StrFiltr(vec.get(6)) + "\"},";
			xqzc = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));
			mzts = MyTools.StringToInt(MyTools.StrFiltr(vec.get(1)));
			zjs = MyTools.StringToInt(MyTools.StrFiltr(vec.get(2)))+MyTools.StringToInt(MyTools.StrFiltr(vec.get(3)))+MyTools.StringToInt(MyTools.StrFiltr(vec.get(4)))+MyTools.StringToInt(MyTools.StrFiltr(vec.get(5)));
			
			for(int i=1; i<xqzc+1; i+=2){
				oddVec.add(i);
				evenVec.add(i+1);
			}
		}
		
		//获取教师名单
//		sql = "with cte1 as (select t1.工号,t1.姓名 from V_教职工基本数据子类 t1 " +
//			"inner join V_USER_AUTH t2 on t2.UserCode=t1.工号 " +
//			"inner join V_权限层级关系表 t3 on t3.权限编号=t2.AuthCode " +
//			"inner join V_层级表 t4 on t4.层级编号=t3.层级编号 where t4.层级编号='" + MyTools.fixSql(this.getPK_CJBH()) + "') " +
//			"select distinct * from (select a.工号,a.姓名 from cte1 a " +
//			"inner join (select distinct 专业代码,授课教师编号 from V_排课管理_课程表周详情表 " +
//			"where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 课程代码<>'') b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' " +
//			"union all " +
//			"select a.工号,a.姓名 from cte1 a " +
//			"inner join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' " +
//			"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') as t where 工号 is not null order by 姓名";
		sql = "select distinct * from (" +
			"select 工号,姓名 from V_教职工基本数据子类 where 工号='" + MyTools.fixSql(this.getUSERCODE()) + "' " +
			"union all " +
			"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
			"inner join (select distinct t1.授课教师编号 from V_排课管理_课程表周详情表 t1 " +
			//"left join V_学校班级_数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
			"left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 " +
			"where t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and t1.课程代码<>''";
		//权限判断
		if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
			sql += " and (1=2";
			//班主任
			if(this.getAuth().indexOf(bzr) > -1){
				sql += " or t2.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			//系部教务人员
			if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
				sql += " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' ";
		
		//判断如果是管理员或全校教务权限，需查询选修课教师信息
		if(this.getAuth().indexOf(admin)>-1 || this.getAuth().indexOf(jxzgxz)>-1 || this.getAuth().indexOf(qxjdzr)>-1 || this.getAuth().indexOf(qxjwgl)>-1){
			sql += "union all " +
				"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
				"inner join (select 授课教师编号 from V_规则管理_选修课授课计划主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') b " +
				"on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%'";
		}
		sql += ") as t where 工号 is not null order by 姓名";
		teaVec = db.GetContextVector(sql);
		
		if(teaVec!=null && teaVec.size()>0){
			//获取选修课信息
			sql = "select c.时间序列,a.授课计划明细编号,b.课程名称,a.选修班名称,a.授课教师编号,c.实际场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a " +
				"inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
				"left join V_排课管理_选修课课程表信息表 c on c.授课计划明细编号=a.授课计划明细编号 " +
				"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"order by c.时间序列,cast((case substring(a.授课周次, 2, 1) when '｜' then substring(a.授课周次, 1, 1) when '&' then substring(a.授课周次, 1, 1) when '#' then substring(a.授课周次, 1, 1) " +
				"when '-' then substring(a.授课周次, 1, 1) when 'd' then '1' when 'v' then '2' else substring(a.授课周次, 1, 2) end) as int)";
			xxkVec = db.GetContextVector(sql);
			
			//获取合班设置信息
			sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a " +
				"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
				"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
				"where c.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
			hbSetVec = db.GetContextVector(sql);
			
			//获取教师课程信息
//			sql = "select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//				"from V_排课管理_课程表周详情表 a where a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and (";
//			for(int i=0; i<teaVec.size(); i+=2){
//				sql += "'@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
//			}
//			sql = sql.substring(0, sql.length()-4);
//			sql += ") order by a.时间序列,cast(a.授课周次 as int)";
			sql = "select t.时间序列,t.授课计划明细编号,t.行政班代码,t.行政班名称,t.课程代码,t.课程名称,t.授课教师编号,t.场地编号,t.场地名称,t.授课周次 " + 
				"from (select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " + 
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and (";
			for(int i=0; i<teaVec.size(); i+=2){
				sql += "'@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
			}
			sql = sql.substring(0, sql.length()-4);
			sql += ")) as t order by t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
			tempVec = db.GetContextVector(sql);
			
			boolean flag = true;
			if(tempVec!=null && tempVec.size()>0){
				vec = new Vector();
				//拼接同一课程周次
				while(flag){
					sjxl = MyTools.StrFiltr(tempVec.get(0));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
					xzbdm = MyTools.StrFiltr(tempVec.get(2));
					xzbmc = MyTools.StrFiltr(tempVec.get(3));
					kcbh = MyTools.StrFiltr(tempVec.get(4));
					kcmc = MyTools.StrFiltr(tempVec.get(5));
					skjsbh = MyTools.StrFiltr(tempVec.get(6));
					cdbh = MyTools.StrFiltr(tempVec.get(7));
					cdmc = MyTools.StrFiltr(tempVec.get(8));
					skzc = MyTools.StrFiltr(tempVec.get(9));
					for(int i=0; i<10; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=10){
						if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))
							&& xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) 
							&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+7)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+9));
							for(int j=0; j<10; j++){
								tempVec.remove(i);
							}
							i -= 10;
						}
					}
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(xzbmc);
					vec.add(kcmc);
					vec.add(skjsbh);
					vec.add(cdbh);
					vec.add(cdmc);
					vec.add(skzc);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
				tempVec = new Vector();
				for(int i=0; i<vec.size(); i+=8){
					cdmc = MyTools.StrFiltr(vec.get(i+6));
					skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i+7)), oddVec, evenVec);
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) 
						|| !skjsbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) || !cdbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
						if(i > 0){
							tempVec.add(sjxl);
							tempVec.add(skjhmxbh);
							tempVec.add(xzbmc);
							tempVec.add(kcmc);
							tempVec.add(tempSkjsbh);
							tempVec.add(tempCdmc);
							tempVec.add(tempSkzc);
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
						xzbmc = MyTools.StrFiltr(vec.get(i+2));
						kcmc = MyTools.StrFiltr(vec.get(i+3));
						skjsbh = MyTools.StrFiltr(vec.get(i+4));
						cdbh = MyTools.StrFiltr(vec.get(i+5));
						tempSkjsbh = skjsbh;
						tempCdmc = cdmc;
						tempSkzc = skzc;
					}else{
						tempSkjsbh += "&"+MyTools.StrFiltr(vec.get(i+4));
						tempCdmc += "&"+cdmc;
						tempSkzc += "&"+skzc;
					}
				}
				tempVec.add(sjxl);
				tempVec.add(skjhmxbh);
				tempVec.add(xzbmc);
				tempVec.add(kcmc);
				tempVec.add(tempSkjsbh);
				tempVec.add(tempCdmc);
				tempVec.add(tempSkzc);
				
				//合并合班课程信息
				for(int i=0; i<tempVec.size(); i+=7){
					sjxl = MyTools.StrFiltr(tempVec.get(i));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(i+1));
					
					for(int j=0; j<hbSetVec.size(); j++){
						hbInfo = MyTools.StrFiltr(hbSetVec.get(j));
						
						if(hbInfo.indexOf(skjhmxbh) > -1){
							for(int k=(i+7); k<tempVec.size(); k+=7){
								if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(k)))){
									break;
								}
								
								if(hbInfo.indexOf(MyTools.StrFiltr(tempVec.get(k+1))) > -1){
									tempVec.set(i+2, MyTools.StrFiltr(tempVec.get(i+2))+"、"+MyTools.StrFiltr(tempVec.get(k+2)));
									
									for(int a=0; a<7; a++){
										tempVec.remove(k);
									}
									k -= 7;
								}
							}
						}
					}
				}
			}
			
			jsonStr += "\"allTeaKcb\":[";
			for(int i=0; i<teaVec.size(); i+=2){
				tempTeaCode = MyTools.StrFiltr(teaVec.get(i));
				
				jsonStr += "{\"授课教师姓名\":\"" + MyTools.StrFiltr(teaVec.get(i+1)) + "\"," +
						"\"课表信息\":[";
				
				for(int j=1; j<mzts+1; j++){
					for(int k=1; k<zjs+1; k++){
						tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
						tempSkjhmxbh = "";
						tempXzbmc = "";
						tempKcmc = "";
						tempCdmc = "";
						tempSkzc = "";
						
						//添加普通课程信息
						flag = false;
						for(int a=0; a<tempVec.size(); a+=7){
							if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(a)))){
								break;
							}
							
							if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a))) && MyTools.StrFiltr(tempVec.get(a+4)).indexOf(tempTeaCode)>-1){
								tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(a+1))+"｜";
								tempXzbmc += MyTools.StrFiltr(tempVec.get(a+2))+"｜";
								tempKcmc += MyTools.StrFiltr(tempVec.get(a+3))+"｜";
								tempCdmc += MyTools.StrFiltr(tempVec.get(a+5))+"｜";
								tempSkzc += MyTools.StrFiltr(tempVec.get(a+6))+"｜";
								flag = true;
							}
						}
						//添加选修课信息
						for(int a=0; a<xxkVec.size(); a+=7){
							if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(xxkVec.get(a))) && MyTools.StrFiltr(xxkVec.get(a+4)).indexOf(tempTeaCode)>-1){
								tempSkjhmxbh += MyTools.StrFiltr(xxkVec.get(a+1))+"｜";
								tempKcmc += MyTools.StrFiltr(xxkVec.get(a+2))+"｜";
								tempXzbmc += MyTools.StrFiltr(xxkVec.get(a+3))+"｜";
								tempCdmc += MyTools.StrFiltr(xxkVec.get(a+5))+"｜";
								tempSkzc += MyTools.StrFiltr(xxkVec.get(a+6))+"｜";
								flag = true;
								
								if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(xxkVec.get(a)))){
									break;
								}
							}
						}
						if(flag == false){
//							jsonStr += "{\"时间序列\":\"" + tempOrder + "\"," +
//									"\"授课计划明细编号\":\"\",\"课程名称\":\"\"," +
//									"\"班级名称\":\"\",\"实际场地名称\":\"\"," +
//									"\"授课周次\":\"\"},";
						}else{
							jsonStr += "{\"时间序列\":\"" + tempOrder + "\"," +
									"\"授课计划明细编号\":\"" + tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1) + "\"," +
									"\"课程名称\":\"" + tempKcmc.substring(0, tempKcmc.length()-1) + "\"," +
									"\"班级名称\":\"" + tempXzbmc.substring(0, tempXzbmc.length()-1) + "\"," +
									"\"实际场地名称\":\"" + tempCdmc.substring(0, tempCdmc.length()-1) + "\"," +
									"\"授课周次\":\"" + tempSkzc.substring(0, tempSkzc.length()-1) + "\"},";
						}
					}
				}
				if(",".equalsIgnoreCase(jsonStr.substring(jsonStr.length()-1)))
					jsonStr = jsonStr.substring(0, jsonStr.length()-1);
				jsonStr += "]},";
			}
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
			jsonStr += "]";
		}else{
			jsonStr += "\"allTeaKcb\":[{\"授课教师姓名\":\"没有相关教师信息\"," +
					"\"课表信息\":[{\"时间序列\":\"\"," +
							"\"授课计划明细编号\":\"\"," +
							"\"课程名称\":\"\"," +
							"\"班级名称\":\"\"," +
							"\"实际场地名称\":\"\"," +
							"\"授课周次\":\"\"}]}]";
		}
		jsonStr += "}";
		
		return jsonStr;
	}
	
	/**
	 * 获取教师授课信息
	 * @date:2015-05-27
	 * @author:yeq
	 * @param resultVec 教师授课信息
	 * @param skjhmxbh 授课计划明细编号
	 * @param courseCode 课程编号
	 * @param courseName 课程名称
	 * @param className 班级名称
	 * @param skzc 授课周次
	 * @param siteName 场地名称
	 */
	public void parseTeaCourseInfo(Vector resultVec, String skjhmxbh, String courseCode, String courseName, String className, String skzc, String siteName){
		boolean hbFlag = false;
		if(resultVec.size() > 0){
			//比较原来的数据中是否有相同课程（合班课程）
			for(int j=0; j<resultVec.size(); j+=7){
				hbFlag = false;
				//合班课程更新班级信息
				if(courseCode.equalsIgnoreCase(MyTools.StrFiltr(resultVec.get(j+1))) && skzc.equalsIgnoreCase(MyTools.StrFiltr(resultVec.get(j+5)))){
					resultVec.set(j, resultVec.get(j)+"&"+skjhmxbh);
					resultVec.set(j+3, resultVec.get(j+3)+"&"+className);
					hbFlag = true;
				}
				//判断如果不是合班课程，将当前课程添加到结果集
				if(hbFlag == false){
					resultVec.add(skjhmxbh);
					resultVec.add(courseCode);
					resultVec.add(courseName);
					resultVec.add(className);
					resultVec.add(siteName);
					resultVec.add(skzc);
					resultVec.add(this.parseSkzc(skzc));
					break;
				}
			}
		}else{
			resultVec.add(skjhmxbh);
			resultVec.add(courseCode);
			resultVec.add(courseName);
			resultVec.add(className);
			resultVec.add(siteName);
			resultVec.add(skzc);
			resultVec.add(this.parseSkzc(skzc));
		}
	}
	
	/**
	 * 获取授课周次的第一周数字
	 * @date:2015-08-20
	 * @author:yeq
	 * @param skzc 授课周次
	 * @return int
	 */
	public String parseSkzc(String skzc){
		String num = "";
		if(skzc.indexOf("-") > -1){
			num = skzc.substring(0, skzc.indexOf("-"));
		}
		if("odd".equalsIgnoreCase(skzc)){
			num = "1";
		}
		if("even".equalsIgnoreCase(skzc)){
			num = "2";
		}
		if(skzc.indexOf("#") > -1){
			num = skzc.substring(0, skzc.indexOf("#"));
		}
		if(skzc.length() == 1){
			num = skzc;
		}
		return num;
	}
	
	/**
	 * 教师授课信息排序
	 * @date:2015-08-20
	 * @author:yeq
	 * @param teaCourseInfoVec 教师授课信息
	 * @return Vector 教师授课信息
	 */
	public Vector sortTeaCourseInfo(Vector teaCourseInfoVec){
		int curNum = 0;
		int tempNum = 0;
		String tempCode = "";
		String tempCourseCode = "";
		String tempCourseName = "";
		String tempClassName = "";
		String tempSkzc = "";
		String tempSiteName = "";
		String tempSkzcNum = "";
		
		for (int i = 0; i < teaCourseInfoVec.size(); i+=7){
            for (int j = i; j < teaCourseInfoVec.size(); j+=7){
            	curNum = MyTools.StringToInt(MyTools.StrFiltr(teaCourseInfoVec.get(i+6)));
            	tempNum = MyTools.StringToInt(MyTools.StrFiltr(teaCourseInfoVec.get(j+6)));
                if (curNum > tempNum){
                	tempCode = MyTools.StrFiltr(teaCourseInfoVec.get(i));
            		tempCourseCode = MyTools.StrFiltr(teaCourseInfoVec.get(i+1));
            		tempCourseName = MyTools.StrFiltr(teaCourseInfoVec.get(i+2));
            		tempClassName = MyTools.StrFiltr(teaCourseInfoVec.get(i+3));
            		tempSkzc = MyTools.StrFiltr(teaCourseInfoVec.get(i+4));
            		tempSiteName = MyTools.StrFiltr(teaCourseInfoVec.get(i+5));
            		tempSkzcNum = MyTools.StrFiltr(teaCourseInfoVec.get(i+6));
            		
            		teaCourseInfoVec.set(i, MyTools.StrFiltr(teaCourseInfoVec.get(j)));
            		teaCourseInfoVec.set(i+1, MyTools.StrFiltr(teaCourseInfoVec.get(j+1)));
            		teaCourseInfoVec.set(i+2, MyTools.StrFiltr(teaCourseInfoVec.get(j+2)));
            		teaCourseInfoVec.set(i+3, MyTools.StrFiltr(teaCourseInfoVec.get(j+3)));
            		teaCourseInfoVec.set(i+4, MyTools.StrFiltr(teaCourseInfoVec.get(j+4)));
            		teaCourseInfoVec.set(i+5, MyTools.StrFiltr(teaCourseInfoVec.get(j+5)));
            		teaCourseInfoVec.set(i+6, MyTools.StrFiltr(teaCourseInfoVec.get(j+6)));
            		
            		teaCourseInfoVec.set(j, tempCode);
            		teaCourseInfoVec.set(j+1, tempCourseCode);
            		teaCourseInfoVec.set(j+2, tempCourseName);
            		teaCourseInfoVec.set(j+3, tempClassName);
            		teaCourseInfoVec.set(j+4, tempSiteName);
            		teaCourseInfoVec.set(j+5, tempSkzc);
            		teaCourseInfoVec.set(j+6, tempSkzcNum);
                }
            }
        }
		return teaCourseInfoVec;
	}
	
	/**
	 * 保存教师课表备注
	 * @date:2015-10-21
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void saveRemark(String startDate, String endDate, String weekNum_1, String contactWay, String weekNum_2, String exam_1, String weekNum_3, String exam_2, String weekNum_4, String exam_3, String year) throws SQLException{
		String sql = "select count(*) from V_规则管理_教师课表备注表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		
		if(db.getResultFromDB(sql)){
			this.modRemark(startDate, endDate, weekNum_1, contactWay, weekNum_2, exam_1, weekNum_3, exam_2, weekNum_4, exam_3, year);
		}else{
			this.addRemark(startDate, endDate, weekNum_1, contactWay, weekNum_2, exam_1, weekNum_3, exam_2, weekNum_4, exam_3, year);
		}
	}
	
	/**
	 * 保存教师课表备注
	 * @date:2015-10-21
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void addRemark(String startDate, String endDate, String weekNum_1, String contactWay, String weekNum_2, String exam_1, String weekNum_3, String exam_2, String weekNum_4, String exam_3, String year) throws SQLException{
		String sql = "insert into V_规则管理_教师课表备注表 (学年学期编码,开学日期,结束日期,周次一,联系方式,周次二,考试一,周次三,考试二,周次四,考试三,年月,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getPK_XNXQBM()) + "'," +
				"'" + MyTools.fixSql(startDate) + "'," +
				"'" + MyTools.fixSql(endDate) + "'," +
				"'" + MyTools.fixSql(weekNum_1) + "'," +
				"'" + MyTools.fixSql(contactWay) + "'," +
				"'" + MyTools.fixSql(weekNum_2) + "'," +
				"'" + MyTools.fixSql(exam_1) + "'," +
				"'" + MyTools.fixSql(weekNum_3) + "'," +
				"'" + MyTools.fixSql(exam_2) + "'," +
				"'" + MyTools.fixSql(weekNum_4) + "'," +
				"'" + MyTools.fixSql(exam_3) + "'," +
				"'" + MyTools.fixSql(year) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 修改教师课表备注
	 * @date:2015-10-21
	 * @author:yeq
	 * @throws SQLException 
	 */
	public void modRemark(String startDate, String endDate, String weekNum_1, String contactWay, String weekNum_2, String exam_1, String weekNum_3, String exam_2, String weekNum_4, String exam_3, String year) throws SQLException{
		String sql = "update V_规则管理_教师课表备注表 set " +
				"开学日期='" + MyTools.fixSql(startDate) + "'," +
				"结束日期='" + MyTools.fixSql(endDate) + "'," +
				"周次一='" + MyTools.fixSql(weekNum_1) + "'," +
				"联系方式='" + MyTools.fixSql(contactWay) + "'," +
				"周次二='" + MyTools.fixSql(weekNum_2) + "'," +
				"考试一='" + MyTools.fixSql(exam_1) + "'," +
				"周次三='" + MyTools.fixSql(weekNum_3) + "'," +
				"考试二='" + MyTools.fixSql(exam_2) + "'," +
				"周次四='" + MyTools.fixSql(weekNum_4) + "'," +
				"考试三='" + MyTools.fixSql(exam_3) + "'," +
				"年月='" + MyTools.fixSql(year) + "' " +
				"where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 查询当前学期课程信息树
	 * @date:2015-10-22
	 * @author:yeq
	 * @param level
	 * @param parentCode
	 * @return 班级信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queCourseTree(String level, String parentCode)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
//		//获取课程类型
//		if("0".equalsIgnoreCase(level)){
//			sql = "select distinct 课程类型代码 as id,课程类型名称 as text,state='closed' from V_课程类型表 where 状态='1'";
//		}
//		
//		//获得课程子节点
//		if("1".equalsIgnoreCase(level)){
//			String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
//			
//			//判断是公共课还是专业课，如果是专业课查询专业列表
//			if("01".equalsIgnoreCase(parentCode)){
//				sql = "select distinct a.课程号 as id,a.课程名称 as text,state='open' from V_课程数据子类 a " +
//					"inner join V_排课管理_课程表周详情表 b on b.课程代码=a.课程号 " +
//					"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//					"and b.课程类型='" + MyTools.fixSql(parentCode) + "'";
//			}else if("02".equalsIgnoreCase(parentCode)){
//				sql = "select distinct a.专业代码 as id,a.专业名称+'('+a.专业代码+')' as text,state='closed' from V_专业基本信息数据子类 a " +
//					"inner join V_排课管理_课程表周详情表 b on b.专业代码=a.专业代码 " +
//					"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//				if(this.getAuth().indexOf(majorTeacher) > -1){
//					sql += " and a.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
//				}
//				sql += " order by a.专业名称+'('+a.专业代码+')'";
//			}
//		}
//		
//		//获取专业课程子节点
//		if("2".equalsIgnoreCase(level)){
//			sql = "select distinct a.课程号 as id,a.课程名称 as text,state='open' from V_课程数据子类 a " +
//				"inner join V_排课管理_课程表周详情表 b on b.课程代码=a.课程号 " +
//				"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and b.专业代码='" + MyTools.fixSql(parentCode) + "' and b.课程类型='02'";
//		}
		
		sql = "select a.课程号 as id,case when a.课程类型='01' then a.课程名称 else a.课程名称+'（'+c.专业名称+'）' end as text,state='open' " +
			"from V_课程数据子类 a " +
			"inner join (select distinct t1.课程代码 from V_排课管理_课程表周详情表 t1 " +
			//"left join V_学校班级_数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
			"left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 " +
			"where t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		//权限判断
		if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
			sql += " and ('@'+replace(t1.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			//班主任
			if(this.getAuth().indexOf(bzr) > -1){
				sql += " or t2.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			}
			//系部教务人员
			if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
				sql += " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") b on b.课程代码=a.课程号 " +
			"left join V_专业基本信息数据子类 c on c.专业代码=a.专业代码 " +
			"where 1=1";
		if(!"".equalsIgnoreCase(this.getPK_SKJSBH())){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(this.getPK_SKJSBH()) + "%'";
		}
		sql += " order by a.课程类型,a.课程名称";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询课程课程表
	 * @date:2015-10-22
	 * @author:yeq
	 * @param parentId 父节点编号
	 * @param courseCode 课程编号
	 * @param week 周次
	 * @return 课程课程表
	 * @throws SQLException
	 */
	public String loadCourseKcb(String courseCode, String week)throws SQLException{
		Vector vec = new Vector();
		Vector tempVec = null;
		String sql = "";
		String resultJson = "[";
		int xqzc = 0;
		Vector oddVec = new Vector();
		Vector evenVec = new Vector();
		String sjxl = "";
		String skjhmxbh = "";
		String kcbh = "";
		String kcmc = "";
		String xzbdm = "";
		String xzbmc = "";
		String skjsbh = "";
		String skjsxm = "";
		String cdbh = "";
		String cdmc = "";
		String skzc = "";
		String hbsz = "";
		String tempSkjhmxbh = "";
		String tempKcmc = "";
		String tempSkjsxm = "";
		String tempXzbmc = "";
		String tempCdmc = "";
		String tempSkzc = "";
		String preSkjhmxbh = "";
		Vector xxkVec = new Vector();
		int xxkIndex = 0;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		//获取本学期授课周次
		sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			xqzc = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(0)));
			
			for(int i=1; i<xqzc+1; i+=2){
				oddVec.add(i);
				evenVec.add(i+1);
			}
		}
		
		//判断是否读取指定周次课表
		if("all".equalsIgnoreCase(week)){
//			sql = "select distinct a.时间序列,a.授课计划明细编号,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int),b.授课计划明细编号 as 合班设置 " +
//				"from V_排课管理_课程表周详情表 a " +
//				"left join V_规则管理_合班表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//				//"left join V_学校班级_数据子类 c on c.行政班代码=a.行政班代码 " +
//				"left join V_基础信息_班级信息表 c on c.班级编号=a.行政班代码 " +
//				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and a.课程代码='" + MyTools.fixSql(courseCode) + "'";
//			//权限判断
//			if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
//				sql += " and ('@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
//				//班主任
//				if(this.getAuth().indexOf(bzr) > -1){
//					sql += " or c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
//				}
//				//系部教务人员
//				if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
//					sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
//				}
//				sql += ")";
//			}
//			sql += " order by a.时间序列,a.授课计划明细编号,cast(a.授课周次 as int)";
			sql = "select t.时间序列,t.授课计划明细编号,t.行政班名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次,t.合班设置 " + 
				"from (select distinct a.时间序列,a.授课计划明细编号,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次,b.授课计划明细编号 as 合班设置," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " +
				"left join V_规则管理_合班表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
				"left join V_基础信息_班级信息表 c on c.班级编号=a.行政班代码 " + 
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"and a.课程代码='" + MyTools.fixSql(courseCode) + "'";
			//权限判断
			if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
				sql += " and ('@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
				//班主任
				if(this.getAuth().indexOf(bzr) > -1){
					sql += " or c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
				}
				//系部教务人员
				if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
					sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
			sql += ") as t order by t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				boolean flag = true;
				//拼接课程周次
				while(flag){
					sjxl = MyTools.StrFiltr(tempVec.get(0));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
					xzbmc = MyTools.StrFiltr(tempVec.get(2));
					skjsbh = MyTools.StrFiltr(tempVec.get(3));
					skjsxm = MyTools.StrFiltr(tempVec.get(4));
					cdbh = MyTools.StrFiltr(tempVec.get(5));
					cdmc = MyTools.StrFiltr(tempVec.get(6));
					skzc = MyTools.StrFiltr(tempVec.get(7));
					hbsz = MyTools.StrFiltr(tempVec.get(8));
					for(int i=0; i<9; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=9){
						if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) 
							&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+3))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+5)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+7));
							for(int j=0; j<9; j++){
								tempVec.remove(i);
							}
							i -= 9;
						}
					}
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(xzbmc);
					vec.add(skjsxm);
					vec.add(cdmc);
					vec.add(skzc);
					vec.add(hbsz);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
				
				sjxl = "";
				tempVec = new Vector();
				for(int i=0; i<vec.size(); i+=7){
					skjsxm = MyTools.StrFiltr(vec.get(i+3));
					cdmc = MyTools.StrFiltr(vec.get(i+4));
					skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i+5)), oddVec, evenVec);
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
						if(i > 0){
							tempVec.add(sjxl);
							tempVec.add(skjhmxbh);
							tempVec.add(xzbmc);
							tempVec.add(tempSkjsxm);
							tempVec.add(tempCdmc);
							tempVec.add(tempSkzc);
							tempVec.add(hbsz);
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
						xzbmc = MyTools.StrFiltr(vec.get(i+2));
						hbsz = MyTools.StrFiltr(vec.get(i+6));
						tempSkjsxm = skjsxm;
						tempCdmc = cdmc;
						tempSkzc = skzc;
					}else{
						tempSkjsxm += "&"+skjsxm;
						tempCdmc += "&"+cdmc;
						tempSkzc += "&"+skzc;
					}
				}
				tempVec.add(sjxl);
				tempVec.add(skjhmxbh);
				tempVec.add(xzbmc);
				tempVec.add(tempSkjsxm);
				tempVec.add(tempCdmc);
				tempVec.add(tempSkzc);
				tempVec.add(hbsz);
				
				for(int i=0; i<tempVec.size(); i+=7){
					skjhmxbh = MyTools.StrFiltr(tempVec.get(i+1));
					xzbmc = MyTools.StrFiltr(tempVec.get(i+2));
					skjsxm = MyTools.StrFiltr(tempVec.get(i+3));
					cdmc = MyTools.StrFiltr(tempVec.get(i+4));
					skzc = MyTools.StrFiltr(tempVec.get(i+5));
					hbsz = MyTools.StrFiltr(tempVec.get(i+6));
					
					//判断当前单元格是否有合班课程
					if(!"".equalsIgnoreCase(hbsz)){
						for(int j=(i+7); j<tempVec.size(); j+=7){
							if(!MyTools.StrFiltr(tempVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j)))){
								break;
							}
							
							if(hbsz.indexOf(skjhmxbh)>-1 && hbsz.indexOf(MyTools.StrFiltr(tempVec.get(j+1)))>-1){
								xzbmc += "、"+MyTools.StrFiltr(tempVec.get(j+2));
								
								for(int k=0; k<7; k++){
									tempVec.remove(j);
								}
								j -= 7;
							}
						}
					}
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i)))){
						if(i > 0){
							resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
									"\"行政班名称\":\"" + tempXzbmc + "\"," +
									"\"授课教师姓名\": \"" + tempSkjsxm + "\"," +
									"\"实际场地名称\": \"" + tempCdmc + "\"," + 
							        "\"授课周次\":\"" + tempSkzc + "\"},";
						}
						
						sjxl = MyTools.StrFiltr(tempVec.get(i));
						tempSkjhmxbh = skjhmxbh;
						tempXzbmc = xzbmc;
						tempSkjsxm = skjsxm;
						tempCdmc = cdmc;
						tempSkzc = skzc;
					}else{
						tempXzbmc += "｜"+xzbmc;
						tempSkjsxm += "｜"+skjsxm;
						tempCdmc += "｜"+cdmc;
						tempSkzc += "｜"+skzc;
					}
				}
				resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
						"\"行政班名称\":\"" + tempXzbmc + "\"," +
						"\"授课教师姓名\": \"" + tempSkjsxm + "\"," +
						"\"实际场地名称\": \"" + tempCdmc + "\"," + 
				        "\"授课周次\":\"" + tempSkzc + "\"}";
			}
		}else{
			sql = "select a.时间序列,a.授课计划明细编号,a.行政班名称,a.授课教师姓名,a.场地名称,b.授课计划明细编号 as 合班设置 " +
				"from V_排课管理_课程表周详情表 a " +
				"left join V_规则管理_合班表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
				//"left join V_学校班级_数据子类 c on c.行政班代码=a.行政班代码 " +
				"left join V_基础信息_班级信息表 c on c.班级编号=a.行政班代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
				"and a.授课周次='" + MyTools.fixSql(week) + "' " +
				"and a.课程代码='" + MyTools.fixSql(courseCode) + "'";
			//权限判断
			if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
				sql += " and ('@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
				//班主任
				if(this.getAuth().indexOf(bzr) > -1){
					sql += " or c.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
				}
				//系部教务人员
				if(this.getAuth().indexOf(xbjdzr)>-1 || this.getAuth().indexOf(xbjwgl)>-1){
					sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
			sql += "order by a.时间序列,a.授课计划明细编号";
			vec = db.GetContextVector(sql);
			
			for(int i=0; i<vec.size(); i+=6){
				skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
				xzbmc = MyTools.StrFiltr(vec.get(i+2));
				skjsxm = MyTools.StrFiltr(vec.get(i+3));
				cdmc = MyTools.StrFiltr(vec.get(i+4));
				hbsz = MyTools.StrFiltr(vec.get(i+5));
				
				//判断当前单元格是否有合班课程
				if(!"".equalsIgnoreCase(hbsz)){
					for(int j=(i+6); j<vec.size(); j+=6){
						if(!MyTools.StrFiltr(vec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(j)))){
							break;
						}
						
						if(hbsz.indexOf(skjhmxbh)>-1 && hbsz.indexOf(MyTools.StrFiltr(vec.get(j+1)))>-1){
							xzbmc += "、"+MyTools.StrFiltr(vec.get(j+2));
							
							for(int k=0; k<6; k++){
								vec.remove(j);
							}
							j -= 7;
						}
					}
				}
				
				if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
								"\"行政班名称\":\"" + tempXzbmc + "\"," +
								"\"授课教师姓名\":\"" + tempSkjsxm + "\"," +
								"\"实际场地名称\": \"" + tempCdmc + "\"},"; 
					}
					
					sjxl = MyTools.StrFiltr(vec.get(i));
					tempSkjhmxbh = skjhmxbh;
					tempXzbmc = xzbmc;
					tempSkjsxm = skjsxm;
					tempCdmc = cdmc;
				}else{
					tempXzbmc += "｜"+xzbmc;
					tempSkjsxm += "｜"+skjsxm;
					tempCdmc += "｜"+cdmc;
				}
			}
			resultJson +=  "{\"时间序列\": \"" + sjxl + "\"," +
					"\"行政班名称\":\"" + tempXzbmc + "\"," +
					"\"授课教师姓名\":\"" + tempSkjsxm + "\"," +
					"\"实际场地名称\": \"" + tempCdmc + "\"}"; 
		}
		
		resultJson += "]";
		return resultJson;
	}
	
	
	
	/**
	 * 课程表导出
	 * @date:2017-11-23
	 * @author:zhaixuchao
	 * @param xnxqbm 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	*/
		public String ExportExceldaochu(String sAuth, String userCode, String xnxqbm, String exportType, String parentId, String code, String timetableName) throws SQLException, UnsupportedEncodingException{
		    DBSource db = new DBSource(this.request);
		    String sql = "";
		    Vector timeVec = null;
		    Vector allExportDataVec = new Vector();
		    Vector curExportDataVec = null;
		    Vector vec = new Vector();
		    Vector tempVec = null;
		    Vector bzInfoVec = null;
		    String schoolName = MyTools.getProp(this.request, "Base.schoolName");
		    
		    String[] weekNameArray = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };
		    String[] orderNameArray = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十" };
		    String[] colName = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		    Vector oddVec = new Vector();
		    Vector evenVec = new Vector();
		    String xnxqmc = "";
		    int mzts = 0;
		    int sw = 0;
		    int zw = 0;
		    int xw = 0;
		    int ws = 0;
		    int zjs = 0;
		    String[] timeArray = new String[0];
		    int maxWidth = 0;
		    int maxHeight = 50;
		    int fontSize = 0;

		    String cellContent = "";
		    Vector tempAllData = new Vector();
		    Vector allCourseVec = new Vector();
		    Vector curCourseVec = new Vector();
		    String curName = "";
		    String bzCellContent = "";
		    
		    String tempOrder = "";
		    int tempIndex = -1;
		    int mergeNum = 0;
		    boolean flag = true;
		    String timeOrderFlag = "";
		    int tempTime = 0;
		    
		    String sjxl = "";
		    String skjhmxbh = "";
		    String kcbh = "";
		    String kcmc = "";
		    String xzbdm = "";
		    String xzbmc = "";
		    String skjsbh = "";
		    String skjsxm = "";
		    String cdbh = "";
		    String cdmc = "";
		    String skzc = "";
		    String tempSkjhmxbh = "";
		    String timeOrder = "";
		    String tempCourseName = "";
		    String tempClassName = "";
		    String tempTeaCode = "";
		    String tempTeaName = "";
		    String tempSiteCode = "";
		    String tempSiteName = "";
		    String tempSkzc = "";
		    Vector hbSetVec = null;
		    String hbSet = "";
		    String admin = MyTools.getProp(this.request, "Base.admin");
		    String jxzgxz = MyTools.getProp(this.request, "Base.jxzgxz");
		    String qxjdzr = MyTools.getProp(this.request, "Base.qxjdzr");
		    String qxjwgl = MyTools.getProp(this.request, "Base.qxjwgl");
		    String xbjdzr = MyTools.getProp(this.request, "Base.xbjdzr");
		    String xbjwgl = MyTools.getProp(this.request, "Base.xbjwgl");
		    String bzr = MyTools.getProp(this.request, "Base.bzr");
		    
		    String[] skjhmxbhArray = new String[0];
		    
		    String savePath = "";
		    if (("classKcbAll".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType)) || ("courseKcbAll".equalsIgnoreCase(exportType))) {
		    	timetableName = "";
		    }
		    sql = "select distinct (select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as 学期周次," + "t1.学年学期名称,t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," + "stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间 " + "from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		    timeVec = db.GetContextVector(sql);
		    if ((timeVec != null) && (timeVec.size() > 0)){
		      int xqzc = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(0)));
		      for (int i = 1; i < xqzc + 1; i += 2) {
		        oddVec.add(Integer.valueOf(i));
		      }
		      for (int i = 2; i < xqzc + 1; i += 2) {
		        evenVec.add(Integer.valueOf(i));
		      }
		      xnxqmc = MyTools.StrFiltr(timeVec.get(1));
		      mzts = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(2)));
		      sw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(3)));
		      zw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(4)));
		      xw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(5)));
		      ws = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(6)));
		      zjs = sw + zw + xw + ws;
		      timeArray = MyTools.StrFiltr(timeVec.get(7)).split(",", -1);
		    }
		    if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)))
		    {
		      String tempHbClass = "";
		      String hbSetInfo = "";
		      Vector classVec = new Vector();
		      Vector curClassData = null;
		      String tempClass = "";
		      String curClassCode = "";
		      boolean existFlag = false;
		      if ("classKcbAll".equalsIgnoreCase(exportType))
		      {
		        sql ="select distinct a.行政班代码,b.班级名称 from V_排课管理_课程表主表 a left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		        if ((sAuth.indexOf(admin) < 0) && (sAuth.indexOf(jxzgxz) < 0) && (sAuth.indexOf(qxjdzr) < 0) && (sAuth.indexOf(qxjwgl) < 0))
		        {
		          sql = sql + " and (";
		          if (sAuth.indexOf(bzr) > -1) {
		            sql = sql + "b.班主任工号='" + MyTools.fixSql(userCode) + "'";
		          }
		          if ((sAuth.indexOf(xbjdzr) > -1) || (sAuth.indexOf(xbjwgl) > -1))
		          {
		            if (sAuth.indexOf(bzr) > -1) {
		              sql = sql + " or ";
		            }
		            sql = sql + "b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
		          }
		          sql = sql + ")";
		        }
		        sql = sql + " order by a.行政班代码";
		        classVec = db.GetContextVector(sql);
		      }else{
		        classVec.add(code);
		        classVec.add(timetableName);
		      }
		      
//		      sql = "select distinct 行政班代码,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
//		      if ("classKcb".equalsIgnoreCase(exportType)) {
//		        sql = sql + " and 行政班代码='" + MyTools.fixSql(code) + "'";
//		      }
//		      sql = sql + " and 授课计划明细编号<>'' order by 行政班代码,时间序列,cast(授课周次 as int)";
		      sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
					"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
					"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
					"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
					"from V_排课管理_课程表周详情表 a " + 
					"where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				if("classKcb".equalsIgnoreCase(exportType)){
					sql += " and 行政班代码='" + MyTools.fixSql(code) + "'";
				}
				sql += " and a.授课计划明细编号<>'') as t order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
		      tempVec = db.GetContextVector(sql);
		      if ((tempVec != null) && (tempVec.size() > 0))
		      {
		        flag = true;
		        while (flag)
		        {
		          xzbdm = MyTools.StrFiltr(tempVec.get(0));
		          sjxl = MyTools.StrFiltr(tempVec.get(1));
		          skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
		          kcbh = MyTools.StrFiltr(tempVec.get(3));
		          kcmc = MyTools.StrFiltr(tempVec.get(4));
		          skjsbh = MyTools.StrFiltr(tempVec.get(5));
		          skjsxm = MyTools.StrFiltr(tempVec.get(6));
		          cdbh = MyTools.StrFiltr(tempVec.get(7));
		          cdmc = MyTools.StrFiltr(tempVec.get(8));
		          skzc = MyTools.StrFiltr(tempVec.get(9));
		          for (int i = 0; i < 10; i++) {
		            tempVec.remove(0);
		          }
		          for (int i = 0; i < tempVec.size(); i += 10){
		            if ((!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i)))) || (!sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 1)))) || 
		              (!skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 2)))) || (!kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 3)))) || 
		              (!skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 5)))) || (!cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 7))))) {
		              break;
		            }
		            skzc = skzc + "#" + MyTools.StrFiltr(tempVec.get(i + 9));
		            for (int j = 0; j < 10; j++) {
		              tempVec.remove(i);
		            }
		            i -= 10;
		          }
		          vec.add(sjxl);
		          vec.add(skjhmxbh);
		          vec.add(kcmc);
		          vec.add(skjsbh);
		          vec.add(skjsxm);
		          vec.add(cdbh);
		          vec.add(cdmc);
		          vec.add(skzc);
		          vec.add(xzbdm);
		          if (tempVec.size() == 0) {
		            flag = false;
		          }
		        }
		        sjxl = "";
		        for (int i = 0; i < vec.size(); i += 9)
		        {
		          skjhmxbh = MyTools.StrFiltr(vec.get(i + 1));
		          kcmc = MyTools.StrFiltr(vec.get(i + 2));
		          skjsbh = MyTools.StrFiltr(vec.get(i + 3));
		          skjsxm = MyTools.StrFiltr(vec.get(i + 4));
		          cdbh = MyTools.StrFiltr(vec.get(i + 5));
		          cdmc = MyTools.StrFiltr(vec.get(i + 6));
		          skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i + 7)), oddVec, evenVec);
		          if ((!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i + 8)))) || (!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))))
		          {
		            if (i > 0)
		            {
		              tempAllData.add(sjxl);
		              for (int j = 0; j < tempVec.size(); j += 7)
		              {
		                tempSkjhmxbh = tempSkjhmxbh + MyTools.StrFiltr(tempVec.get(j)) + "｜";
		                tempCourseName = tempCourseName + MyTools.StrFiltr(tempVec.get(j + 1)) + "｜";
		                tempTeaCode = tempTeaCode + MyTools.StrFiltr(tempVec.get(j + 2)) + "｜";
		                tempTeaName = tempTeaName + MyTools.StrFiltr(tempVec.get(j + 3)) + "｜";
		                tempSiteCode = tempSiteCode + MyTools.StrFiltr(tempVec.get(j + 4)) + "｜";
		                tempSiteName = tempSiteName + MyTools.StrFiltr(tempVec.get(j + 5)) + "｜";
		                tempSkzc = tempSkzc + MyTools.StrFiltr(tempVec.get(j + 6)) + "｜";
		              }
		              tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length() - 1));
		              tempAllData.add(tempCourseName.substring(0, tempCourseName.length() - 1));
		              tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length() - 1));
		              tempAllData.add(tempTeaName.substring(0, tempTeaName.length() - 1));
		              tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length() - 1));
		              tempAllData.add(tempSiteName.substring(0, tempSiteName.length() - 1));
		              tempAllData.add(tempSkzc.substring(0, tempSkzc.length() - 1));
		              tempAllData.add(xzbdm);
		              tempSkjhmxbh = "";
		              tempCourseName = "";
		              tempTeaCode = "";
		              tempTeaName = "";
		              tempSiteCode = "";
		              tempSiteName = "";
		              tempSkzc = "";
		            }
		            sjxl = MyTools.StrFiltr(vec.get(i));
		            xzbdm = MyTools.StrFiltr(vec.get(i + 8));
		            tempVec = new Vector();
		            tempVec.add(skjhmxbh);
		            tempVec.add(kcmc);
		            tempVec.add(skjsbh);
		            tempVec.add(skjsxm);
		            tempVec.add(cdbh);
		            tempVec.add(cdmc);
		            tempVec.add(skzc);
		          }
		          else
		          {
		            for (int j = 0; j < tempVec.size(); j += 7) {
		              if (skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j))))
		              {
		                tempVec.set(j + 2, MyTools.StrFiltr(tempVec.get(j + 2)) + "&" + skjsbh);
		                tempVec.set(j + 3, MyTools.StrFiltr(tempVec.get(j + 3)) + "&" + skjsxm);
		                tempVec.set(j + 4, MyTools.StrFiltr(tempVec.get(j + 4)) + "&" + cdbh);
		                tempVec.set(j + 5, MyTools.StrFiltr(tempVec.get(j + 5)) + "&" + cdmc);
		                tempVec.set(j + 6, MyTools.StrFiltr(tempVec.get(j + 6)) + "&" + skzc);
		                existFlag = true;
		              }
		            }
		            if (!existFlag)
		            {
		              tempVec.add(skjhmxbh);
		              tempVec.add(kcmc);
		              tempVec.add(skjsbh);
		              tempVec.add(skjsxm);
		              tempVec.add(cdbh);
		              tempVec.add(cdmc);
		              tempVec.add(skzc);
		            }
		            existFlag = false;
		          }
		        }
		        tempAllData.add(sjxl);
		        for (int j = 0; j < tempVec.size(); j += 7)
		        {
		          tempSkjhmxbh = tempSkjhmxbh + MyTools.StrFiltr(tempVec.get(j)) + "｜";
		          tempCourseName = tempCourseName + MyTools.StrFiltr(tempVec.get(j + 1)) + "｜";
		          tempTeaCode = tempTeaCode + MyTools.StrFiltr(tempVec.get(j + 2)) + "｜";
		          tempTeaName = tempTeaName + MyTools.StrFiltr(tempVec.get(j + 3)) + "｜";
		          tempSiteCode = tempSiteCode + MyTools.StrFiltr(tempVec.get(j + 4)) + "｜";
		          tempSiteName = tempSiteName + MyTools.StrFiltr(tempVec.get(j + 5)) + "｜";
		          tempSkzc = tempSkzc + MyTools.StrFiltr(tempVec.get(j + 6)) + "｜";
		        }
		        tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length() - 1));
		        tempAllData.add(tempCourseName.substring(0, tempCourseName.length() - 1));
		        tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length() - 1));
		        tempAllData.add(tempTeaName.substring(0, tempTeaName.length() - 1));
		        tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length() - 1));
		        tempAllData.add(tempSiteName.substring(0, tempSiteName.length() - 1));
		        tempAllData.add(tempSkzc.substring(0, tempSkzc.length() - 1));
		        tempAllData.add(xzbdm);
		      }
		      int tytag = 0;
		      int tdcs = 0;
		      int zwyk = 0;
		      

		      String sqlcc = "select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(code) + "' and [教室名称]='操场' ) ";
		      Vector veccc = db.GetContextVector(sqlcc);
		      

		      String sqlskzs = "select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
		      Vector vecskzs = db.GetContextVector(sqlskzs);
		      

		      int mztsx = 0;
		      String sqlmzts = "select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(xnxqbm) + "' ";
		      Vector vecmzts = db.GetContextVector(sqlmzts);
		      if ((vecmzts != null) && (vecmzts.size() > 0)) {
		        mztsx = Integer.parseInt(vecmzts.get(0).toString());
		      }
		      for (int b = 0; b < classVec.size(); b += 2)
		      {
		        tdcs = 0;
		        
		        String ts = "";
		        for (int i = 1; i <= mztsx; i++)
		        {
		          tytag = 0;
		          zwyk = 0;
		          if (i < 10) {
		            ts = "0" + i;
		          } else {
		            ts = i+"";
		          }
		          for (int j = 0; j < tempAllData.size(); j += 9) {
		            if ((tempAllData.get(j + 8).toString().equals(classVec.get(b).toString())) && 
		              (ts.equals(tempAllData.get(j).toString().substring(0, 2))) && 
		              (tempAllData.get(j + 2).toString().indexOf("体育") > -1)) {
		              tytag = 1;
		            }
		          }
		          if (tytag == 0)
		          {
		            for (int k = 0; k < tempAllData.size(); k += 9) {
		              if ((tempAllData.get(k + 8).toString().equals(classVec.get(b).toString())) && 
		                ((ts + "05").equals(tempAllData.get(k).toString()))) {
		                zwyk = 1;
		              }
		            }
		            if (zwyk == 0)
		            {
		              if (tdcs < 2)
		              {
		                int existkc = 0;
		                for (int t = 0; t < tempAllData.size(); t += 9) {
		                  if (tempAllData.get(t + 8).toString().equals(classVec.get(b).toString()))
		                  {
		                    existkc = 1;
		                    break;
		                  }
		                }
		                if (existkc == 1)
		                {
		                  tempAllData.add(ts + "05");
		                  tempAllData.add("SKJHMXBH_TD");
		                  tempAllData.add("体锻");
		                  tempAllData.add("");
		                  tempAllData.add("");
		                  tempAllData.add("");
		                  tempAllData.add("");
		                  tempAllData.add("");
		                  tempAllData.add(classVec.get(b).toString());
		                  
		                  tdcs++;
		                }
		              }
		            }
		            else {
		              zwyk = 0;
		            }
		          }
		        }
		      }
		      Vector classKcbVec = null;
		      Vector classSkjhVec = null;
		      Vector hbSkjhVec = null;
		      tempVec = new Vector();
		      hbSetVec = new Vector();
		      
		      sql = "select 时间序列,行政班代码,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 where 学年学期编码='" + 
		        MyTools.fixSql(xnxqbm) + "'";
		      if ("classKcb".equalsIgnoreCase(exportType)) {
		        sql = sql + " and 行政班代码<>'" + MyTools.fixSql(code) + "'";
		      }
		      sql = sql + " and 授课计划明细编号<>'' order by 时间序列";
		      classKcbVec = db.GetContextVector(sql);
		      if ((classKcbVec != null) && (classKcbVec.size() > 0))
		      {
		        sql = "select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		        if ("classKcb".equalsIgnoreCase(exportType)) {
		          sql = sql + " and b.行政班代码<>'" + MyTools.fixSql(code) + "'";
		        }
		        classSkjhVec = db.GetContextVector(sql);
		        if ((classSkjhVec != null) && (classSkjhVec.size() > 0))
		        {
		          for (int i = 0; i < classKcbVec.size(); i += 4)
		          {
		            tempSkjhmxbh = MyTools.StrFiltr(classKcbVec.get(i + 3));
		            for (int j = 0; j < classSkjhVec.size(); j++) {
		              if (tempSkjhmxbh.indexOf(MyTools.StrFiltr(classSkjhVec.get(j))) > -1)
		              {
		                tempVec.add(MyTools.StrFiltr(classKcbVec.get(i)));
		                tempVec.add(MyTools.StrFiltr(classKcbVec.get(i + 1)));
		                tempVec.add(MyTools.StrFiltr(classKcbVec.get(i + 2)));
		                tempVec.add(MyTools.StrFiltr(classSkjhVec.get(j)));
		              }
		            }
		          }
		          sql = "select 授课计划明细编号 from V_规则管理_合班表";
		          hbSkjhVec = db.GetContextVector(sql);
		          if ((hbSkjhVec != null) && (hbSkjhVec.size() > 0)) {
		            for (int i = 0; i < tempVec.size(); i += 4)
		            {
		              tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(i + 3));
		              for (int j = 0; j < hbSkjhVec.size(); j++) {
		                if (MyTools.StrFiltr(hbSkjhVec.get(j)).indexOf(tempSkjhmxbh) > -1)
		                {
		                  hbSetVec.add(tempVec.get(i));
		                  hbSetVec.add(tempVec.get(i + 1));
		                  hbSetVec.add(tempVec.get(i + 2));
		                  hbSetVec.add(tempVec.get(i + 3));
		                  hbSetVec.add(MyTools.StrFiltr(hbSkjhVec.get(j)));
		                  break;
		                }
		              }
		            }
		          }
		        }
		      }
		      for (int i = 0; i < classVec.size(); i += 2)
		      {
		        curClassCode = MyTools.StrFiltr(classVec.get(i));
		        curClassData = new Vector();
		        for (int j = 0; j < tempAllData.size(); j += 9)
		        {
		          tempClass = MyTools.StrFiltr(tempAllData.get(j + 8));
		          if (tempClass.equalsIgnoreCase(curClassCode)) {
		            for (int k = 0; k < 8; k++) {
		              curClassData.add(MyTools.StrFiltr(tempAllData.get(j + k)));
		            }
		          }
		        }
		        allCourseVec.add(curClassCode);
		        allCourseVec.add(curClassData);
		      }
		      for (int i = 0; i < classVec.size(); i += 2)
		      {
		        curCourseVec = (Vector)allCourseVec.get(allCourseVec.indexOf(classVec.get(i)) + 1);
		        curExportDataVec = new Vector();
		        for (int j = 0; j < curCourseVec.size(); j += 8)
		        {
		          tempHbClass = "";
		          tempOrder = MyTools.StrFiltr(curCourseVec.get(j));
		          skjhmxbh = MyTools.StrFiltr(curCourseVec.get(j + 1));
		          skjhmxbhArray = skjhmxbh.split("｜", -1);
		          for (int k = 0; k < skjhmxbhArray.length; k++)
		          {
		            for (int a = 0; a < hbSetVec.size(); a += 5) {
		              if (tempOrder.equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a))))
		              {
		                hbSetInfo = MyTools.StrFiltr(hbSetVec.get(a + 4));
		                if ((!skjhmxbhArray[k].equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a + 3)))) && (hbSetInfo.indexOf(skjhmxbhArray[k]) > -1)) {
		                  tempHbClass = tempHbClass + MyTools.StrFiltr(hbSetVec.get(a + 2)) + "、";
		                }
		              }
		            }
		            if ((!"".equalsIgnoreCase(tempHbClass)) && ("、".equalsIgnoreCase(tempHbClass.substring(tempHbClass.length() - 1)))) {
		              tempHbClass = tempHbClass.substring(0, tempHbClass.length() - 1);
		            }
		            tempHbClass = tempHbClass + "｜";
		          }
		          tempHbClass = tempHbClass.substring(0, tempHbClass.length() - 1);
		          
		          curExportDataVec.add(tempOrder);
		          curExportDataVec.add(skjhmxbh);
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 2)));
		          curExportDataVec.add(tempHbClass);
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 3)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 4)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 5)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 6)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 7)));
		        }
		        allExportDataVec.add(classVec.get(i));
		        allExportDataVec.add(classVec.get(i + 1));
		        allExportDataVec.add(curExportDataVec);
		      }
		    }
		    if (("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType)))
		    {
		      Vector teaVec = new Vector();
		      String curTeaCode = "";
		      Vector xxkVec = new Vector();
		      if ("teaKcbAll".equalsIgnoreCase(exportType))
		      {
		        sql ="with cte1 as (select distinct a.授课教师编号 from V_排课管理_课程表周详情表 a left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and a.状态='1'";
		        if ((sAuth.indexOf(admin) < 0) && (sAuth.indexOf(jxzgxz) < 0) && (sAuth.indexOf(qxjdzr) < 0) && (sAuth.indexOf(qxjwgl) < 0))
		        {
		          sql = sql + " and (1=2";
		          if (sAuth.indexOf(bzr) > -1) {
		            sql = sql + " or b.班主任工号='" + MyTools.fixSql(userCode) + "'";
		          }
		          if ((sAuth.indexOf(xbjdzr) > -1) || (sAuth.indexOf(xbjwgl) > -1)) {
		            sql = sql + " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
		          }
		          sql = sql + ")";
		        }
		        sql = sql + ") select distinct * from (select a.工号,a.姓名 from V_教职工基本数据子类 a inner join cte1 b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' ";
		        if ((sAuth.indexOf(admin) > -1) || (sAuth.indexOf(jxzgxz) > -1) || (sAuth.indexOf(qxjdzr) > -1) || (sAuth.indexOf(qxjwgl) > -1)) {
		          sql = 
		          

		            sql + "union all select a.工号,a.姓名 from V_教职工基本数据子类 a left join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		        }
		        sql = sql + ") as t order by 姓名";
		        teaVec = db.GetContextVector(sql);
		      }
		      else
		      {
		        teaVec.add(code);
		        teaVec.add(timetableName);
		      }
		      sql = "select c.时间序列,a.授课计划明细编号,b.课程名称,a.选修班名称,a.授课教师编号,a.授课教师姓名,c.实际场地编号,c.实际场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 left join V_排课管理_选修课课程表信息表 c on c.授课计划明细编号=a.授课计划明细编号 where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		      if ("teaKcb".equalsIgnoreCase(exportType)) {
		        sql = sql + "and a.授课教师编号 like '%" + MyTools.fixSql(code) + "%' ";
		      }
		      sql = sql + " order by a.授课教师姓名,c.时间序列,cast((case substring(a.授课周次, 2, 1) when '｜' then substring(a.授课周次, 1, 1) when '&' then substring(a.授课周次, 1, 1) when '#' then substring(a.授课周次, 1, 1) when '-' then substring(a.授课周次, 1, 1) when 'd' then '1' when 'v' then '2' else substring(a.授课周次, 1, 2) end) as int)";
		      
		      xxkVec = db.GetContextVector(sql);
		      

		      sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 where c.学年学期编码='" + 
		      

		        MyTools.fixSql(xnxqbm) + "' ";
		      if ("teaKcb".equalsIgnoreCase(exportType)) {
		        sql = sql + "and b.授课教师编号 like '%" + MyTools.fixSql(code) + "%'";
		      }
		      hbSetVec = db.GetContextVector(sql);
		      

//		      sql = "select distinct 时间序列,授课计划明细编号,课程代码,课程名称,行政班代码,行政班名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) as 授课周次 from V_排课管理_课程表周详情表 where 学年学期编码='" + 
//		        MyTools.fixSql(xnxqbm) + "' and 授课计划明细编号<>''";
//		      if ("teaKcb".equalsIgnoreCase(exportType)) {
//		        sql = sql + "and 授课教师编号 like '%" + MyTools.fixSql(code) + "%'";
//		      }
//		      sql = sql + " order by 时间序列,cast(授课周次 as int),授课计划明细编号,课程代码";
		  	sql = "select t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.行政班代码,t.行政班名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
				"from (select distinct a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " + 
				"where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			if("teaKcb".equalsIgnoreCase(exportType)){
				sql += "and a.授课教师编号 like '%" + MyTools.fixSql(code) + "%'";
			}
			sql += " and a.授课计划明细编号<>'') as t order by t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号,t.课程代码";
		      tempVec = db.GetContextVector(sql);
		      if ((tempVec != null) && (tempVec.size() > 0))
		      {
		        flag = true;
		        while (flag)
		        {
		          sjxl = MyTools.StrFiltr(tempVec.get(0));
		          skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
		          kcbh = MyTools.StrFiltr(tempVec.get(2));
		          kcmc = MyTools.StrFiltr(tempVec.get(3));
		          xzbdm = MyTools.StrFiltr(tempVec.get(4));
		          xzbmc = MyTools.StrFiltr(tempVec.get(5));
		          skjsbh = MyTools.StrFiltr(tempVec.get(6));
		          skjsxm = MyTools.StrFiltr(tempVec.get(7));
		          cdbh = MyTools.StrFiltr(tempVec.get(8));
		          cdmc = MyTools.StrFiltr(tempVec.get(9));
		          skzc = MyTools.StrFiltr(tempVec.get(10));
		          for (int i = 0; i < 11; i++) {
		            tempVec.remove(0);
		          }
		          for (int i = 0; i < tempVec.size(); i += 11) {
		            if ((sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i)))) && (skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 1)))) && 
		              (xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 4)))) && (kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 2)))) && 
		              (skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 6)))) && (cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 8)))))
		            {
		              skzc = skzc + "#" + MyTools.StrFiltr(tempVec.get(i + 10));
		              for (int j = 0; j < 11; j++) {
		                tempVec.remove(i);
		              }
		              i -= 11;
		            }
		          }
		          vec.add(sjxl);
		          vec.add(skjhmxbh);
		          vec.add(xzbmc);
		          vec.add(kcmc);
		          vec.add(skjsbh);
		          vec.add(skjsxm);
		          vec.add(cdbh);
		          vec.add(cdmc);
		          vec.add(skzc);
		          if (tempVec.size() == 0) {
		            flag = false;
		          }
		        }
		        sjxl = "";
		        tempVec = new Vector();
		        for (int i = 0; i < vec.size(); i += 9)
		        {
		          skjsbh = MyTools.StrFiltr(vec.get(i + 4));
		          skjsxm = MyTools.StrFiltr(vec.get(i + 5));
		          cdbh = MyTools.StrFiltr(vec.get(i + 6));
		          cdmc = MyTools.StrFiltr(vec.get(i + 7));
		          skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i + 8)), oddVec, evenVec);
		          if ((!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))) || (!skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i + 1)))))
		          {
		            if (i > 0)
		            {
		              tempVec.add(sjxl);
		              tempVec.add(skjhmxbh);
		              tempVec.add(xzbmc);
		              tempVec.add(kcmc);
		              tempVec.add(tempTeaCode);
		              tempVec.add(tempTeaName);
		              tempVec.add(tempSiteCode);
		              tempVec.add(tempSiteName);
		              tempVec.add(tempSkzc);
		            }
		            sjxl = MyTools.StrFiltr(vec.get(i));
		            skjhmxbh = MyTools.StrFiltr(vec.get(i + 1));
		            xzbmc = MyTools.StrFiltr(vec.get(i + 2));
		            kcmc = MyTools.StrFiltr(vec.get(i + 3));
		            tempTeaCode = skjsbh;
		            tempTeaName = skjsxm;
		            tempSiteCode = cdbh;
		            tempSiteName = cdmc;
		            tempSkzc = skzc;
		          }
		          else
		          {
		            tempTeaCode = tempTeaCode + "&" + skjsbh;
		            tempTeaName = tempTeaName + "&" + skjsxm;
		            tempSiteCode = tempSiteCode + "&" + cdbh;
		            tempSiteName = tempSiteName + "&" + cdmc;
		            tempSkzc = tempSkzc + "&" + skzc;
		          }
		        }
		        tempVec.add(sjxl);
		        tempVec.add(skjhmxbh);
		        tempVec.add(xzbmc);
		        tempVec.add(kcmc);
		        tempVec.add(tempTeaCode);
		        tempVec.add(tempTeaName);
		        tempVec.add(tempSiteCode);
		        tempVec.add(tempSiteName);
		        tempVec.add(tempSkzc);
		        for (int i = 0; i < tempVec.size(); i += 9)
		        {
		          skjhmxbh = MyTools.StrFiltr(tempVec.get(i + 1));
		          for (int j = i + 9; j < tempVec.size(); j += 9) {
		            if (MyTools.StrFiltr(tempVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j))))
		            {
		              tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(j + 1));
		              for (int k = 0; k < hbSetVec.size(); k++)
		              {
		                hbSet = MyTools.StrFiltr(hbSetVec.get(k));
		                if ((hbSet.indexOf(skjhmxbh) > -1) && (hbSet.indexOf(tempSkjhmxbh) > -1))
		                {
		                  tempVec.set(i + 2, MyTools.StrFiltr(tempVec.get(i + 2)) + "、" + MyTools.StrFiltr(tempVec.get(j + 2)));
		                  for (int a = 0; a < 9; a++) {
		                    tempVec.remove(j);
		                  }
		                  j -= 9;
		                  break;
		                }
		              }
		            }
		          }
		        }
		      }
		      for (int i = 0; i < teaVec.size(); i += 2)
		      {
		        curTeaCode = MyTools.StrFiltr(teaVec.get(i));
		        curExportDataVec = new Vector();
		        for (int j = 1; j < mzts + 1; j++) {
		          for (int k = 1; k < zjs + 1; k++)
		          {
		            tempOrder = (j < 10 ? "0" + j : new StringBuilder().append(j).toString()) + (k < 10 ? "0" + k : new StringBuilder().append(k).toString());
		            tempSkjhmxbh = "";
		            tempCourseName = "";
		            tempTeaName = "";
		            tempClassName = "";
		            tempSiteCode = "";
		            tempSiteName = "";
		            tempSkzc = "";
		            for (int a = 0; a < tempVec.size(); a += 9) {
		              if ((tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a)))) && (MyTools.StrFiltr(tempVec.get(a + 4)).indexOf(curTeaCode) > -1))
		              {
		                tempSkjhmxbh = tempSkjhmxbh + MyTools.StrFiltr(tempVec.get(a + 1)) + "｜";
		                tempCourseName = tempCourseName + MyTools.StrFiltr(tempVec.get(a + 3)) + "｜";
		                tempTeaName = tempTeaName + "｜";
		                tempClassName = tempClassName + MyTools.StrFiltr(tempVec.get(a + 2)) + "｜";
		                tempSiteCode = tempSiteCode + MyTools.StrFiltr(tempVec.get(a + 6)) + "｜";
		                tempSiteName = tempSiteName + MyTools.StrFiltr(tempVec.get(a + 7)) + "｜";
		                tempSkzc = tempSkzc + MyTools.StrFiltr(tempVec.get(a + 8)) + "｜";
		              }
		            }
		            for (int a = 0; a < xxkVec.size(); a += 9) {
		              if ((tempOrder.equalsIgnoreCase(MyTools.StrFiltr(xxkVec.get(a)))) && (MyTools.StrFiltr(xxkVec.get(a + 4)).indexOf(curTeaCode) > -1))
		              {
		                tempSkjhmxbh = tempSkjhmxbh + MyTools.StrFiltr(xxkVec.get(a + 1)) + "｜";
		                tempCourseName = tempCourseName + MyTools.StrFiltr(xxkVec.get(a + 2)) + "｜";
		                tempTeaName = tempTeaName + "｜";
		                tempClassName = tempClassName + MyTools.StrFiltr(xxkVec.get(a + 3)) + "｜";
		                tempSiteCode = tempSiteCode + MyTools.StrFiltr(xxkVec.get(a + 6)) + "｜";
		                tempSiteName = tempSiteName + MyTools.StrFiltr(xxkVec.get(a + 7)) + "｜";
		                tempSkzc = tempSkzc + MyTools.StrFiltr(xxkVec.get(a + 8)) + "｜";
		              }
		            }
		            if (!"".equalsIgnoreCase(tempSkjhmxbh))
		            {
		              curExportDataVec.add(tempOrder);
		              curExportDataVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length() - 1));
		              curExportDataVec.add(tempCourseName.substring(0, tempCourseName.length() - 1));
		              curExportDataVec.add(tempTeaName.substring(0, tempTeaName.length() - 1));
		              curExportDataVec.add("");
		              curExportDataVec.add(tempClassName.substring(0, tempClassName.length() - 1));
		              curExportDataVec.add(tempSiteCode.substring(0, tempSiteCode.length() - 1));
		              curExportDataVec.add(tempSiteName.substring(0, tempSiteName.length() - 1));
		              curExportDataVec.add(tempSkzc.substring(0, tempSkzc.length() - 1));
		            }
		          }
		        }
		        allExportDataVec.add(curTeaCode);
		        allExportDataVec.add(teaVec.get(i + 1));
		        allExportDataVec.add(curExportDataVec);
		      }
		    }
		    if (("courseKcb".equalsIgnoreCase(exportType)) || ("courseKcbAll".equalsIgnoreCase(exportType)))
		    {
		      String siteName = "";
		      String[] siteNameArray = new String[0];
		      String siteCode = "";
		      String[] siteCodeArray = new String[0];
		      Vector courseVec = new Vector();
		      Vector curCourseData = null;
		      String tempCourse = "";
		      String curCourseCode = "";
		      if ("courseKcbAll".equalsIgnoreCase(exportType))
		      {
		        sql = "select a.课程号,case when a.课程类型='01' then a.课程名称 else a.课程名称+'（'+c.专业名称+'）' end as 课程名称 from V_课程数据子类 a inner join (select distinct t1.课程代码 from V_排课管理_课程表周详情表 t1 left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 where t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		        if ((sAuth.indexOf(admin) < 0) && (sAuth.indexOf(jxzgxz) < 0) && (sAuth.indexOf(qxjdzr) < 0) && (sAuth.indexOf(qxjwgl) < 0))
		        {
		          sql = sql + " and ('@'+replace(t1.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(userCode) + "@%'";
		          if (sAuth.indexOf(bzr) > -1) {
		            sql = sql + " or t2.班主任工号='" + MyTools.fixSql(userCode) + "'";
		          }
		          if ((sAuth.indexOf(xbjdzr) > -1) || (sAuth.indexOf(xbjwgl) > -1)) {
		            sql = sql + " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
		          }
		          sql = sql + ")";
		        }
		        sql = sql + ") b on b.课程代码=a.课程号 left join V_专业基本信息数据子类 c on c.专业代码=a.专业代码 order by a.课程类型,a.课程名称";
		        

		        courseVec = db.GetContextVector(sql);
		      }
		      else
		      {
		        courseVec.add(code);
		        courseVec.add(timetableName);
		      }
		      sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 where c.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
		      if ("courseKcb".equalsIgnoreCase(exportType)) {
		        sql = sql + "and b.课程代码='" + MyTools.fixSql(code) + "'";
		      }
		      hbSetVec = db.GetContextVector(sql);

//		      sql = "select distinct a.时间序列,a.授课计划明细编号,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int),课程代码 from V_排课管理_课程表周详情表 a 
//		      left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 
//		    where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
//		      if ("courseKcb".equalsIgnoreCase(exportType)) {
//		        sql = sql + " and a.课程代码='" + MyTools.fixSql(code) + "'";
//		      }
//		      if ((sAuth.indexOf(admin) < 0) && (sAuth.indexOf(jxzgxz) < 0) && (sAuth.indexOf(qxjdzr) < 0) && (sAuth.indexOf(qxjwgl) < 0))
//		      {
//		        sql = sql + " and ('@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(userCode) + "@%'";
//		        if (sAuth.indexOf(bzr) > -1) {
//		          sql = sql + " or b.班主任工号='" + MyTools.fixSql(userCode) + "'";
//		        }
//		        if ((sAuth.indexOf(xbjdzr) > -1) || (sAuth.indexOf(xbjwgl) > -1)) {
//		          sql = sql + " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
//		        }
//		        sql = sql + ")";
//		      }
//		      sql = sql + " order by a.时间序列,a.授课计划明细编号,cast(a.授课周次 as int)";
		    sql = "select t.时间序列,t.授课计划明细编号,t.行政班名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次,t.课程代码 " + 
				"from (select distinct a.时间序列,a.授课计划明细编号,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次,a.课程代码, " + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " +
				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " + 
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			if("courseKcb".equalsIgnoreCase(exportType)){
				sql += " and a.课程代码='" + MyTools.fixSql(code) + "'";
			}
			 if ((sAuth.indexOf(admin) < 0) && (sAuth.indexOf(jxzgxz) < 0) && (sAuth.indexOf(qxjdzr) < 0) && (sAuth.indexOf(qxjwgl) < 0)){
		        sql = sql + " and ('@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(userCode) + "@%'";
		        if (sAuth.indexOf(bzr) > -1) {
		          sql = sql + " or b.班主任工号='" + MyTools.fixSql(userCode) + "'";
		        }
		        if ((sAuth.indexOf(xbjdzr) > -1) || (sAuth.indexOf(xbjwgl) > -1)) {
		          sql = sql + " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
		        }
		        sql = sql + ")";
		     }
			sql += ") as t order by t.课程代码,t.时间序列,t.授课计划明细编号,t.num_1,t.num_2,t.授课周次";
		      tempVec = db.GetContextVector(sql);
		      if ((tempVec != null) && (tempVec.size() > 0))
		      {
		        flag = true;
		        while (flag)
		        {
		          sjxl = MyTools.StrFiltr(tempVec.get(0));
		          skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
		          xzbmc = MyTools.StrFiltr(tempVec.get(2));
		          skjsbh = MyTools.StrFiltr(tempVec.get(3));
		          skjsxm = MyTools.StrFiltr(tempVec.get(4));
		          cdbh = MyTools.StrFiltr(tempVec.get(5));
		          cdmc = MyTools.StrFiltr(tempVec.get(6));
		          skzc = MyTools.StrFiltr(tempVec.get(7));
		          kcbh = MyTools.StrFiltr(tempVec.get(8));
		          for (int i = 0; i < 9; i++) {
		            tempVec.remove(0);
		          }
		          for (int i = 0; i < tempVec.size(); i += 9)
		          {
		            if ((!sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i)))) || (!skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 1)))) || 
		              (!skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 3)))) || (!cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 5)))) || 
		              (!kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i + 8))))) {
		              break;
		            }
		            skzc = skzc + "#" + MyTools.StrFiltr(tempVec.get(i + 7));
		            for (int j = 0; j < 9; j++) {
		              tempVec.remove(i);
		            }
		            i -= 9;
		          }
		          vec.add(sjxl);
		          vec.add(skjhmxbh);
		          vec.add(xzbmc);
		          vec.add(skjsbh);
		          vec.add(skjsxm);
		          vec.add(cdbh);
		          vec.add(cdmc);
		          vec.add(skzc);
		          vec.add(kcbh);
		          if (tempVec.size() == 0) {
		            flag = false;
		          }
		        }
		        sjxl = "";
		        tempVec = new Vector();
		        for (int i = 0; i < vec.size(); i += 9)
		        {
		          skjsbh = MyTools.StrFiltr(vec.get(i + 3));
		          skjsxm = MyTools.StrFiltr(vec.get(i + 4));
		          cdbh = MyTools.StrFiltr(vec.get(i + 5));
		          cdmc = MyTools.StrFiltr(vec.get(i + 6));
		          skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i + 7)), oddVec, evenVec);
		          if ((!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))) || (!skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i + 1)))))
		          {
		            if (i > 0)
		            {
		              tempVec.add(sjxl);
		              tempVec.add(skjhmxbh);
		              tempVec.add(xzbmc);
		              tempVec.add(tempTeaCode);
		              tempVec.add(tempTeaName);
		              tempVec.add(tempSiteCode);
		              tempVec.add(tempSiteName);
		              tempVec.add(tempSkzc);
		              tempVec.add(kcbh);
		            }
		            sjxl = MyTools.StrFiltr(vec.get(i));
		            skjhmxbh = MyTools.StrFiltr(vec.get(i + 1));
		            xzbmc = MyTools.StrFiltr(vec.get(i + 2));
		            tempTeaCode = skjsbh;
		            tempTeaName = skjsxm;
		            tempSiteCode = cdbh;
		            tempSiteName = cdmc;
		            tempSkzc = skzc;
		            kcbh = MyTools.StrFiltr(vec.get(i + 8));
		          }
		          else
		          {
		            tempTeaCode = tempTeaCode + "&" + skjsbh;
		            tempTeaName = tempTeaName + "&" + skjsxm;
		            tempSiteCode = tempSiteCode + "&" + cdbh;
		            tempSiteName = tempSiteName + "&" + cdmc;
		            tempSkzc = tempSkzc + "&" + skzc;
		          }
		        }
		        tempVec.add(sjxl);
		        tempVec.add(skjhmxbh);
		        tempVec.add(xzbmc);
		        tempVec.add(tempTeaCode);
		        tempVec.add(tempTeaName);
		        tempVec.add(tempSiteCode);
		        tempVec.add(tempSiteName);
		        tempVec.add(tempSkzc);
		        tempVec.add(kcbh);
		        for (int i = 0; i < tempVec.size(); i += 9)
		        {
		          skjhmxbh = MyTools.StrFiltr(tempVec.get(i + 1));
		          for (int j = i + 9; j < tempVec.size(); j += 9) {
		            if (MyTools.StrFiltr(tempVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j))))
		            {
		              tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(j + 1));
		              for (int k = 0; k < hbSetVec.size(); k++)
		              {
		                hbSet = MyTools.StrFiltr(hbSetVec.get(k));
		                if ((hbSet.indexOf(skjhmxbh) > -1) && (hbSet.indexOf(tempSkjhmxbh) > -1))
		                {
		                  tempVec.set(i + 2, MyTools.StrFiltr(tempVec.get(i + 2)) + "、" + MyTools.StrFiltr(tempVec.get(j + 2)));
		                  for (int a = 0; a < 9; a++) {
		                    tempVec.remove(j);
		                  }
		                  j -= 9;
		                  break;
		                }
		              }
		            }
		          }
		        }
		        for (int i = 0; i < courseVec.size(); i += 2)
		        {
		          curCourseCode = MyTools.StrFiltr(courseVec.get(i));
		          curExportDataVec = new Vector();
		          for (int j = 1; j < mzts + 1; j++) {
		            for (int k = 1; k < zjs + 1; k++)
		            {
		              tempOrder = (j < 10 ? "0" + j : new StringBuilder().append(j).toString()) + (k < 10 ? "0" + k : new StringBuilder().append(k).toString());
		              tempSkjhmxbh = "";
		              tempCourseName = "";
		              tempClassName = "";
		              tempTeaCode = "";
		              tempTeaName = "";
		              tempSiteCode = "";
		              tempSiteName = "";
		              tempSkzc = "";
		              for (int a = 0; a < tempVec.size(); a += 9) {
		                if ((tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a)))) && (MyTools.StrFiltr(tempVec.get(a + 8)).indexOf(curCourseCode) > -1))
		                {
		                  tempSkjhmxbh = tempSkjhmxbh + MyTools.StrFiltr(tempVec.get(a + 1)) + "｜";
		                  tempCourseName = tempCourseName + "｜";
		                  tempClassName = tempClassName + MyTools.StrFiltr(tempVec.get(a + 2)) + "｜";
		                  tempTeaCode = tempTeaCode + MyTools.StrFiltr(tempVec.get(a + 3)) + "｜";
		                  tempTeaName = tempTeaName + MyTools.StrFiltr(tempVec.get(a + 4)) + "｜";
		                  tempSiteCode = tempSiteCode + MyTools.StrFiltr(tempVec.get(a + 5)) + "｜";
		                  tempSiteName = tempSiteName + MyTools.StrFiltr(tempVec.get(a + 6)) + "｜";
		                  tempSkzc = tempSkzc + MyTools.StrFiltr(tempVec.get(a + 7)) + "｜";
		                }
		              }
		              if (!"".equalsIgnoreCase(tempSkjhmxbh))
		              {
		                curExportDataVec.add(tempOrder);
		                curExportDataVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length() - 1));
		                curExportDataVec.add(tempClassName.substring(0, tempClassName.length() - 1));
		                curExportDataVec.add(tempCourseName.substring(0, tempCourseName.length() - 1));
		                curExportDataVec.add(tempTeaCode.substring(0, tempTeaCode.length() - 1));
		                curExportDataVec.add(tempTeaName.substring(0, tempTeaName.length() - 1));
		                curExportDataVec.add(tempSiteCode.substring(0, tempSiteCode.length() - 1));
		                curExportDataVec.add(tempSiteName.substring(0, tempSiteName.length() - 1));
		                curExportDataVec.add(tempSkzc.substring(0, tempSkzc.length() - 1));
		              }
		            }
		          }
		          allExportDataVec.add(courseVec.get(i));
		          allExportDataVec.add(courseVec.get(i + 1));
		          allExportDataVec.add(curExportDataVec);
		        }
		      }
		    }
		    if ("studentKcb".equalsIgnoreCase(exportType))
		    {
		      String tempHbClass = "";
		      String hbSetInfo = "";
		      Vector classVec = new Vector();
		      Vector curClassData = null;
		      String tempClass = "";
		      String curClassCode = "";
		      boolean existFlag = false;
		      

		      String userid = "";
		      String classid = "";
		      
		      String sqlcls = "select 行政班代码 from dbo.V_学生基本数据子类 where 学号='" + MyTools.fixSql(code) + "' ";
		      Vector veccls = db.GetContextVector(sqlcls);
		      if ((veccls != null) && (veccls.size() > 0))
		      {
		        userid = code;
		        classid = veccls.get(0).toString();
		      }
		      if ("classKcbAll".equalsIgnoreCase(exportType))
		      {
		        sql = "select distinct a.行政班代码,b.行政班名称 from V_排课管理_课程表主表 a left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " + "order by a.行政班代码";
		        classVec = db.GetContextVector(sql);
		      }
		      else
		      {
		        classVec.add(classid);
		        classVec.add(timetableName);
		      }
		      //ql = "select * from (select distinct 行政班代码,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,授课周次 from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " + "and 行政班代码='" + MyTools.fixSql(classid) + "' and 授课计划明细编号<>'') as b " + "order by 时间序列,cast(授课周次 as int) ";
		      sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
					"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
					"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
					"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
					"from V_排课管理_课程表周详情表 a " + 
					"where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
					"and a.行政班代码='" + MyTools.fixSql(classid) + "'" +
					"and a.授课计划明细编号<>'') as t order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
		      tempVec = db.GetContextVector(sql);
		      if ((tempVec != null) && (tempVec.size() > 0))
		      {
		        flag = true;
		        while (flag)
		        {
		          xzbdm = MyTools.StrFiltr(tempVec.get(0));
		          sjxl = MyTools.StrFiltr(tempVec.get(1));
		          skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
		          kcbh = MyTools.StrFiltr(tempVec.get(3));
		          kcmc = MyTools.StrFiltr(tempVec.get(4));
		          skjsbh = MyTools.StrFiltr(tempVec.get(5));
		          skjsxm = MyTools.StrFiltr(tempVec.get(6));
		          cdbh = MyTools.StrFiltr(tempVec.get(7));
		          cdmc = MyTools.StrFiltr(tempVec.get(8));
		          skzc = MyTools.StrFiltr(tempVec.get(9));
		          for (int i = 0; i < 10; i++) {
		            tempVec.remove(0);
		          }
		          for(int i=0; i<tempVec.size(); i+=10){
					if(xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) 
						&& skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+3))) 
						&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+5))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+7)))){
						skzc += "#"+MyTools.StrFiltr(tempVec.get(i+9));
						for(int j=0; j<10; j++){
							tempVec.remove(i);
						}
						i -= 10;
					}
				}
		          vec.add(sjxl);
		          vec.add(skjhmxbh);
		          vec.add(kcmc);
		          vec.add(skjsbh);
		          vec.add(skjsxm);
		          vec.add(cdbh);
		          vec.add(cdmc);
		          vec.add(skzc);
		          vec.add(xzbdm);
		          if (tempVec.size() == 0) {
		            flag = false;
		          }
		        }
		        for (int i = 0; i < vec.size(); i += 9)
		        {
		          skjhmxbh = MyTools.StrFiltr(vec.get(i + 1));
		          kcmc = MyTools.StrFiltr(vec.get(i + 2));
		          skjsbh = MyTools.StrFiltr(vec.get(i + 3));
		          skjsxm = MyTools.StrFiltr(vec.get(i + 4));
		          cdbh = MyTools.StrFiltr(vec.get(i + 5));
		          cdmc = MyTools.StrFiltr(vec.get(i + 6));
		          skzc = formatSkzcShow(MyTools.StrFiltr(vec.get(i + 7)), oddVec, evenVec);
		          if ((!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i + 8)))) || (!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))))
		          {
		            if (i > 0)
		            {
		              tempAllData.add(sjxl);
		              for (int j = 0; j < tempVec.size(); j += 7)
		              {
		                tempSkjhmxbh = tempSkjhmxbh + MyTools.StrFiltr(tempVec.get(j)) + "｜";
		                tempCourseName = tempCourseName + MyTools.StrFiltr(tempVec.get(j + 1)) + "｜";
		                tempTeaCode = tempTeaCode + MyTools.StrFiltr(tempVec.get(j + 2)) + "｜";
		                tempTeaName = tempTeaName + MyTools.StrFiltr(tempVec.get(j + 3)) + "｜";
		                tempSiteCode = tempSiteCode + MyTools.StrFiltr(tempVec.get(j + 4)) + "｜";
		                tempSiteName = tempSiteName + MyTools.StrFiltr(tempVec.get(j + 5)) + "｜";
		                tempSkzc = tempSkzc + MyTools.StrFiltr(tempVec.get(j + 6)) + "｜";
		              }
		              tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length() - 1));
		              tempAllData.add(tempCourseName.substring(0, tempCourseName.length() - 1));
		              tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length() - 1));
		              tempAllData.add(tempTeaName.substring(0, tempTeaName.length() - 1));
		              tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length() - 1));
		              tempAllData.add(tempSiteName.substring(0, tempSiteName.length() - 1));
		              tempAllData.add(tempSkzc.substring(0, tempSkzc.length() - 1));
		              tempAllData.add(xzbdm);
		              tempSkjhmxbh = "";
		              tempCourseName = "";
		              tempTeaCode = "";
		              tempTeaName = "";
		              tempSiteCode = "";
		              tempSiteName = "";
		              tempSkzc = "";
		            }
		            sjxl = MyTools.StrFiltr(vec.get(i));
		            xzbdm = MyTools.StrFiltr(vec.get(i + 8));
		            tempVec = new Vector();
		            tempVec.add(skjhmxbh);
		            tempVec.add(kcmc);
		            tempVec.add(skjsbh);
		            tempVec.add(skjsxm);
		            tempVec.add(cdbh);
		            tempVec.add(cdmc);
		            tempVec.add(skzc);
		          }
		          else
		          {
		            for (int j = 0; j < tempVec.size(); j += 7) {
		              if (skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j))))
		              {
		                tempVec.set(j + 2, MyTools.StrFiltr(tempVec.get(j + 2)) + "&" + skjsbh);
		                tempVec.set(j + 3, MyTools.StrFiltr(tempVec.get(j + 3)) + "&" + skjsxm);
		                tempVec.set(j + 4, MyTools.StrFiltr(tempVec.get(j + 4)) + "&" + cdbh);
		                tempVec.set(j + 5, MyTools.StrFiltr(tempVec.get(j + 5)) + "&" + cdmc);
		                tempVec.set(j + 6, MyTools.StrFiltr(tempVec.get(j + 6)) + "&" + skzc);
		                existFlag = true;
		              }
		            }
		            if (!existFlag)
		            {
		              tempVec.add(skjhmxbh);
		              tempVec.add(kcmc);
		              tempVec.add(skjsbh);
		              tempVec.add(skjsxm);
		              tempVec.add(cdbh);
		              tempVec.add(cdmc);
		              tempVec.add(skzc);
		            }
		            existFlag = false;
		          }
		        }
		        tempAllData.add(sjxl);
		        for (int j = 0; j < tempVec.size(); j += 7)
		        {
		          tempSkjhmxbh = tempSkjhmxbh + MyTools.StrFiltr(tempVec.get(j)) + "｜";
		          tempCourseName = tempCourseName + MyTools.StrFiltr(tempVec.get(j + 1)) + "｜";
		          tempTeaCode = tempTeaCode + MyTools.StrFiltr(tempVec.get(j + 2)) + "｜";
		          tempTeaName = tempTeaName + MyTools.StrFiltr(tempVec.get(j + 3)) + "｜";
		          tempSiteCode = tempSiteCode + MyTools.StrFiltr(tempVec.get(j + 4)) + "｜";
		          tempSiteName = tempSiteName + MyTools.StrFiltr(tempVec.get(j + 5)) + "｜";
		          tempSkzc = tempSkzc + MyTools.StrFiltr(tempVec.get(j + 6)) + "｜";
		        }
		        tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length() - 1));
		        tempAllData.add(tempCourseName.substring(0, tempCourseName.length() - 1));
		        tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length() - 1));
		        tempAllData.add(tempTeaName.substring(0, tempTeaName.length() - 1));
		        tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length() - 1));
		        tempAllData.add(tempSiteName.substring(0, tempSiteName.length() - 1));
		        tempAllData.add(tempSkzc.substring(0, tempSkzc.length() - 1));
		        tempAllData.add(xzbdm);
		      }
		      Vector classKcbVec = null;
		      Vector classSkjhVec = null;
		      Vector hbSkjhVec = null;
		      tempVec = new Vector();
		      hbSetVec = new Vector();
		      
		      sql = "select 时间序列,行政班代码,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 where 学年学期编码='" + 
		        MyTools.fixSql(xnxqbm) + "'";
		      
		      sql = sql + " and 行政班代码<>'" + MyTools.fixSql(classid) + "'";
		      
		      sql = sql + " and 授课计划明细编号<>'' order by 时间序列";
		      classKcbVec = db.GetContextVector(sql);
		      if ((classKcbVec != null) && (classKcbVec.size() > 0))
		      {
		        sql = "select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		        
		        sql = sql + " and b.行政班代码<>'" + MyTools.fixSql(classid) + "'";
		        
		        classSkjhVec = db.GetContextVector(sql);
		        if ((classSkjhVec != null) && (classSkjhVec.size() > 0))
		        {
		          for (int i = 0; i < classKcbVec.size(); i += 4)
		          {
		            tempSkjhmxbh = MyTools.StrFiltr(classKcbVec.get(i + 3));
		            for (int j = 0; j < classSkjhVec.size(); j++) {
		              if (tempSkjhmxbh.indexOf(MyTools.StrFiltr(classSkjhVec.get(j))) > -1)
		              {
		                tempVec.add(MyTools.StrFiltr(classKcbVec.get(i)));
		                tempVec.add(MyTools.StrFiltr(classKcbVec.get(i + 1)));
		                tempVec.add(MyTools.StrFiltr(classKcbVec.get(i + 2)));
		                tempVec.add(MyTools.StrFiltr(classSkjhVec.get(j)));
		              }
		            }
		          }
		          sql = "select 授课计划明细编号 from V_规则管理_合班表";
		          hbSkjhVec = db.GetContextVector(sql);
		          if ((hbSkjhVec != null) && (hbSkjhVec.size() > 0)) {
		            for (int i = 0; i < tempVec.size(); i += 4)
		            {
		              tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(i + 3));
		              for (int j = 0; j < hbSkjhVec.size(); j++) {
		                if (MyTools.StrFiltr(hbSkjhVec.get(j)).indexOf(tempSkjhmxbh) > -1)
		                {
		                  hbSetVec.add(tempVec.get(i));
		                  hbSetVec.add(tempVec.get(i + 1));
		                  hbSetVec.add(tempVec.get(i + 2));
		                  hbSetVec.add(tempVec.get(i + 3));
		                  hbSetVec.add(MyTools.StrFiltr(hbSkjhVec.get(j)));
		                  break;
		                }
		              }
		            }
		          }
		        }
		      }
		      for (int i = 0; i < classVec.size(); i += 2)
		      {
		        curClassCode = MyTools.StrFiltr(classVec.get(i));
		        curClassData = new Vector();
		        for (int j = 0; j < tempAllData.size(); j += 9)
		        {
		          tempClass = MyTools.StrFiltr(tempAllData.get(j + 8));
		          if (tempClass.equalsIgnoreCase(curClassCode)) {
		            for (int k = 0; k < 8; k++) {
		              curClassData.add(MyTools.StrFiltr(tempAllData.get(j + k)));
		            }
		          }
		        }
		        allCourseVec.add(curClassCode);
		        allCourseVec.add(curClassData);
		      }
		      for (int i = 0; i < classVec.size(); i += 2)
		      {
		        curCourseVec = (Vector)allCourseVec.get(allCourseVec.indexOf(classVec.get(i)) + 1);
		        curExportDataVec = new Vector();
		        for (int j = 0; j < curCourseVec.size(); j += 8)
		        {
		          tempHbClass = "";
		          tempOrder = MyTools.StrFiltr(curCourseVec.get(j));
		          skjhmxbh = MyTools.StrFiltr(curCourseVec.get(j + 1));
		          skjhmxbhArray = skjhmxbh.split("｜", -1);
		          for (int k = 0; k < skjhmxbhArray.length; k++)
		          {
		            for (int a = 0; a < hbSetVec.size(); a += 5) {
		              if (tempOrder.equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a))))
		              {
		                hbSetInfo = MyTools.StrFiltr(hbSetVec.get(a + 4));
		                if ((!skjhmxbhArray[k].equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a + 3)))) && (hbSetInfo.indexOf(skjhmxbhArray[k]) > -1)) {
		                  tempHbClass = tempHbClass + MyTools.StrFiltr(hbSetVec.get(a + 2)) + "、";
		                }
		              }
		            }
		            if ((!"".equalsIgnoreCase(tempHbClass)) && ("、".equalsIgnoreCase(tempHbClass.substring(tempHbClass.length() - 1)))) {
		              tempHbClass = tempHbClass.substring(0, tempHbClass.length() - 1);
		            }
		            tempHbClass = tempHbClass + "｜";
		          }
		          tempHbClass = tempHbClass.substring(0, tempHbClass.length() - 1);
		          
		          curExportDataVec.add(tempOrder);
		          curExportDataVec.add(skjhmxbh);
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 2)));
		          curExportDataVec.add(tempHbClass);
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 3)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 4)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 5)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 6)));
		          curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j + 7)));
		        }
		        allExportDataVec.add(classVec.get(i));
		        allExportDataVec.add(classVec.get(i + 1));
		        allExportDataVec.add(curExportDataVec);
		      }
		      String sqlxxk = "select distinct a.上课时间 as 时间序列,a.授课计划明细编号,b.课程代码,b.课程名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,'' as 行政班代码 from [V_规则管理_选修课授课计划明细表] a,dbo.V_规则管理_选修课授课计划主表 b,V_基础信息_选修课程信息表 c where a.授课计划主表编号=b.授课计划主表编号  and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and a.上课时间!='' and a.授课计划明细编号 in ( " + 
		    		  		"SELECT [授课计划明细编号] FROM [V_规则管理_学生选修课关系表] where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 学号='" + MyTools.fixSql(userid) + "' )";
		      Vector vecxxk = db.GetContextVector(sqlxxk);
		      if ((vecxxk != null) && (vecxxk.size() > 0)) {
		        for (int i = 0; i < vecxxk.size(); i += 10) {
		          if (vecxxk.get(i).toString().indexOf(",") > -1)
		          {
		            String[] sjxls = vecxxk.get(i).toString().split(",");
		            for (int j = 0; j < sjxls.length; j++)
		            {
		              curExportDataVec.add(sjxls[j]);
		              curExportDataVec.add(vecxxk.get(i + 1).toString());
		              curExportDataVec.add(vecxxk.get(i + 3).toString());
		              curExportDataVec.add("");
		              curExportDataVec.add(vecxxk.get(i + 4).toString());
		              curExportDataVec.add(vecxxk.get(i + 5).toString());
		              curExportDataVec.add(vecxxk.get(i + 6).toString());
		              curExportDataVec.add(vecxxk.get(i + 7).toString());
		              curExportDataVec.add(vecxxk.get(i + 8).toString());
		            }
		          }
		          else
		          {
		            curExportDataVec.add(vecxxk.get(i).toString());
		            curExportDataVec.add(vecxxk.get(i + 1).toString());
		            curExportDataVec.add(vecxxk.get(i + 3).toString());
		            curExportDataVec.add("");
		            curExportDataVec.add(vecxxk.get(i + 4).toString());
		            curExportDataVec.add(vecxxk.get(i + 5).toString());
		            curExportDataVec.add(vecxxk.get(i + 6).toString());
		            curExportDataVec.add(vecxxk.get(i + 7).toString());
		            curExportDataVec.add(vecxxk.get(i + 8).toString());
		          }
		        }
		      }
		      String sqlgr = "  SELECT a.时间序列,a.相关课程表编号,b.课程代码,b.课程名称,b.授课教师编号,b.授课教师姓名,b.场地要求,b.场地名称,b.授课周次,'' as 行政班代码  FROM [dbo].[V_排课管理_添加课程信息表] a,V_排课管理_课程表明细详情表 b where a.学年学期编码='" + 
		        MyTools.fixSql(xnxqbm) + "' and a.相关课程表编号=b.课程表明细编号  and a.类型='3' and a.学号='" + MyTools.fixSql(userid) + "'";
		      Vector vecgr = db.GetContextVector(sqlgr);
		      if ((vecgr != null) && (vecgr.size() > 0)) {
		        for (int i = 0; i < vecgr.size(); i += 10)
		        {
		          curExportDataVec.add(vecgr.get(i).toString());
		          curExportDataVec.add(vecgr.get(i + 1).toString());
		          curExportDataVec.add(vecgr.get(i + 3).toString());
		          curExportDataVec.add("");
		          curExportDataVec.add(vecgr.get(i + 4).toString());
		          curExportDataVec.add(vecgr.get(i + 5).toString());
		          curExportDataVec.add(vecgr.get(i + 6).toString());
		          curExportDataVec.add(vecgr.get(i + 7).toString());
		          curExportDataVec.add(vecgr.get(i + 8).toString());
		        }
		      }
		    }
		    if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)) || ("studentKcb".equalsIgnoreCase(exportType)))
		    {
		      sql = "select distinct 行政班代码,备注 from V_排课管理_课程表主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		      bzInfoVec = db.GetContextVector(sql);
		    }
		    else if (("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType)))
		    {
		      sql = "select distinct 开学日期,结束日期,周次一,联系方式,周次二,考试一,周次三,考试二,周次四,考试三,年月 from V_规则管理_教师课表备注表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		      bzInfoVec = db.GetContextVector(sql);
		      if ((bzInfoVec != null) && (bzInfoVec.size() > 0)) {
		    	  bzCellContent = "" +
							"注：1、本学期所授课程 理论课时_________实训课时_________。（具体可咨询教务处或专业组长）" +
							"\n    2、本学期开学日期 " + MyTools.StrFiltr(bzInfoVec.get(0)) + " 结束日期 " + MyTools.StrFiltr(bzInfoVec.get(1)) +
							"\n    3、开学第" + MyTools.StrFiltr(bzInfoVec.get(2)) + "周交授课计划文字版、电子版各一份" +
							"\n    4、关于多媒体教室和实训室的问题请联系" + MyTools.StrFiltr(bzInfoVec.get(3)) + "。" +
							"\n    5、本学期 " + MyTools.StrFiltr(bzInfoVec.get(4)) + " 周课程考试为" + MyTools.StrFiltr(bzInfoVec.get(5)) + "；" +
							"\n       本学期 " + MyTools.StrFiltr(bzInfoVec.get(6)) + " 周课程考试为" + MyTools.StrFiltr(bzInfoVec.get(7)) + "；" +
							"\n       本学期 " + MyTools.StrFiltr(bzInfoVec.get(8)) + " 周课程考试为" + MyTools.StrFiltr(bzInfoVec.get(9)) + "；" +
							"\n    6、如有特殊原因需变更授课教师或地点，请先在教务处填写变更申请。" +
							"\n\n                                   教务处长签名：             教学校长签名：             " + MyTools.StrFiltr(bzInfoVec.get(10));
		      }
		    }
		    if ((allExportDataVec != null) && (allExportDataVec.size() > 0))
		    {
		      Calendar c = Calendar.getInstance();
		      int year = c.get(1);
		      int month = c.get(2);
		      int date = c.get(5);
		      
		      String captionName = "";
		      savePath = MyTools.getProp(this.request, "Base.exportExcelPath");
		      try
		      {
		        File file = new File(savePath);
		        if (!file.exists()) {
		          file.mkdirs();
		        }
		        if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)) || ("studentKcb".equalsIgnoreCase(exportType))) {
		          captionName = schoolName + "   " + timetableName + xnxqmc.replace(" ", "") + "课表";
		        } else if (("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType))) {
		          captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 教师授课计划表";
		        } else if (("courseKcb".equalsIgnoreCase(exportType)) || ("courseKcbAll".equalsIgnoreCase(exportType))) {
		          captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 课程课表";
		        }
		        savePath = savePath + "/" + captionName + year + (month + 1 < 10 ? "0" + (month + 1) : Integer.valueOf(month + 1)) + (date < 10 ? "0" + date : Integer.valueOf(date)) + ".xls";
		        OutputStream os = new FileOutputStream(savePath);
		        WritableWorkbook wbook = Workbook.createWorkbook(os);
		        

		        WritableCellFormat contentStyle = null;
		        



		        int sum = 0;
		        for (int i = 0; i < allExportDataVec.size(); i += 3)
		        {
		          int pageWidth = 64;
		          maxWidth = pageWidth / mzts;
		          curName = MyTools.StrFiltr(allExportDataVec.get(i + 1));
		          curExportDataVec = (Vector)allExportDataVec.get(i + 2);
		          
		          WritableSheet wsheet = wbook.createSheet(curName.length() > 30 ? curName.substring(0, 30) : curName, sum);
		          for (int j = 0; j < mzts + 1; j++) {
		            if (j == 0) {
		              wsheet.setColumnView(j, 11 + 2);
		            } else {
		              wsheet.setColumnView(j, maxWidth + 1 + 2);
		            }
		          }
		          sum++;
		          for (int colNum = 1; colNum < mzts + 2; colNum++) {
		            for (int rowNum = 1; rowNum < zjs + 3; rowNum++) {
		              if ((colNum == 1) && (rowNum == 1))
		              {
		                if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)) || ("studentKcb".equalsIgnoreCase(exportType))) {
		                  cellContent = schoolName + "   " + curName + xnxqmc.replace(" ", "") + "课表";
		                } else if (("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType))) {
		                  cellContent = schoolName + "   " + xnxqmc.replace(" ", "") + " " + curName + " 教师授课计划表";
		                } else if (("courseKcb".equalsIgnoreCase(exportType)) || ("courseKcbAll".equalsIgnoreCase(exportType))) {
		                  cellContent = schoolName + "   " + xnxqmc.replace(" ", "") + " " + curName + " 课程课表";
		                }
		                for (int row = 0; row < zjs + 2; row++)
		                {
		                  WritableFont fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		                  contentStyle = new WritableCellFormat(fontStyle);
		                  contentStyle.setBorder(Border.TOP, BorderLineStyle.THIN);
		                  contentStyle.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		                  contentStyle.setBorder(Border.LEFT, BorderLineStyle.THIN);
		                  contentStyle.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		                  for (int lie = 0; lie < mzts + 1; lie++)
		                  {
		                    Label content = new Label(lie, row, "", contentStyle);
		                    wsheet.addCell(content);
		                  }
		                }
		                if (("courseKcb".equalsIgnoreCase(exportType)) || ("courseKcbAll".equalsIgnoreCase(exportType))) {
		                  fontSize = 10;
		                } else {
		                  fontSize = 12;
		                }
		                WritableFont fontStyle = new WritableFont(WritableFont.createFont("宋体"), fontSize, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		                contentStyle = new WritableCellFormat(fontStyle);
		                contentStyle.setAlignment(Alignment.CENTRE);
		                contentStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
		                


		                contentStyle.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		                

		                wsheet.mergeCells(0, 0, mzts, 0);
		                Label content = new Label(0, 0, cellContent, contentStyle);
		                wsheet.addCell(content);
		              }
		              else if ((colNum == 1) && (rowNum == 2))
		              {
		                Label content = new Label(colNum - 1, rowNum, "", contentStyle);
		                wsheet.addCell(content);
		              }
		              else if ((colNum == 1) && (rowNum > 2))
		              {
		                WritableFont writableFont = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false);
		                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
		                writableCellFormat.setWrap(true);
		                writableCellFormat.setAlignment(Alignment.CENTRE);
		                writableCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		                writableCellFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
		                writableCellFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		                writableCellFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
		                writableCellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		                if ((rowNum - 2 <= sw) || (rowNum - 2 > sw + zw))
		                {
		                  if (rowNum - 2 <= sw)
		                  {
		                    Label content = new Label(colNum - 1, rowNum - 1, "第" + orderNameArray[(rowNum - 3)] + "节\n" + timeArray[(rowNum - 3)], writableCellFormat);
		                    wsheet.addCell(content);
		                  }
		                  else
		                  {
		                    Label content = new Label(colNum - 1, rowNum - 1, "第" + orderNameArray[(rowNum - zw - 3)] + "节\n" + timeArray[(rowNum - 3)], writableCellFormat);
		                    wsheet.addCell(content);
		                  }
		                }
		                else
		                {
		                  Label content = new Label(colNum - 1, rowNum - 1, "中" + orderNameArray[(rowNum - sw - 3)] + "\n" + timeArray[(rowNum - 3)], writableCellFormat);
		                  wsheet.addCell(content);
		                }
		              }
		              else if ((colNum > 1) && (rowNum == 2))
		              {
		                WritableFont fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		                contentStyle = new WritableCellFormat(fontStyle);
		                contentStyle.setAlignment(Alignment.CENTRE);
		                contentStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
		                contentStyle.setBorder(Border.TOP, BorderLineStyle.THIN);
		                contentStyle.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		                contentStyle.setBorder(Border.LEFT, BorderLineStyle.THIN);
		                contentStyle.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		                if (mzts > 7)
		                {
		                  Label content = new Label(colNum - 1, rowNum - 1, "大周" + (colNum - 1), contentStyle);
		                  wsheet.addCell(content);
		                }
		                else
		                {
		                  Label content = new Label(colNum - 1, rowNum - 1, weekNameArray[(colNum - 2)], contentStyle);
		                  wsheet.addCell(content);
		                }
		              }
		              else if ((colNum > 1) && (rowNum > 2))
		              {
		                WritableFont writableFont = new WritableFont(WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD, false);
		                WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
		                writableCellFormat.setWrap(true);
		                writableCellFormat.setAlignment(Alignment.CENTRE);
		                writableCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		                writableCellFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
		                writableCellFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
		                writableCellFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
		                writableCellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
		                

		                int pageHeight = 0;
		                if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)) || ("studentKcb".equalsIgnoreCase(exportType))) {
		                  pageHeight = 535;
		                }
		                if (("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType))) {
		                  pageHeight = 500;
		                }
		                if (("courseKcb".equalsIgnoreCase(exportType)) || ("courseKcbAll".equalsIgnoreCase(exportType))) {
		                  pageHeight = 620;
		                }
		                maxHeight = pageHeight / zjs;
		                for(int hang=0;hang<zjs+2;hang++){
							if(hang==0||hang==1){
								wsheet.setRowView(hang, pageHeight+80, false); //设置行高
							}else{
								wsheet.setRowView(hang, maxHeight*20, false); //设置行高
							}
						}
		                System.out.println(maxHeight*20);
		                tempOrder = (colNum - 1 < 10 ? "0" + (colNum - 1) : new StringBuilder().append(colNum - 1).toString()) + (rowNum - 2 < 10 ? "0" + (rowNum - 2) : new StringBuilder().append(rowNum - 2).toString());
		                tempIndex = curExportDataVec.indexOf(tempOrder);
		                if (tempIndex > -1)
		                {
		                  tempSkjhmxbh = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 1));
		                  if ("".equalsIgnoreCase(tempSkjhmxbh))
		                  {
		                    cellContent = "";
		                  }
		                  else
		                  {
		                    tempCourseName = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 2));
		                    tempClassName = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 3));
		                    tempTeaCode = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 4));
		                    tempTeaName = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 5));
		                    tempSiteCode = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 6));
		                    tempSiteName = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 7));
		                    tempSkzc = MyTools.StrFiltr(curExportDataVec.get(tempIndex + 8));
		                    
		                    mergeNum = 0;
		                    flag = true;
		                    timeOrderFlag = tempOrder;
		                    while (flag)
		                    {
		                      tempIndex += 9;
		                      if (tempIndex < curExportDataVec.size())
		                      {
		                        tempTime = MyTools.StringToInt(timeOrderFlag.substring(2)) + 1;
		                        if ((MyTools.StrFiltr(curExportDataVec.get(tempIndex)).equalsIgnoreCase(timeOrderFlag.substring(0, 2) + (tempTime < 10 ? "0" + tempTime : new StringBuilder().append(tempTime).toString()))) && 
		                          (tempSkjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(curExportDataVec.get(tempIndex + 1)))) && 
		                          (tempTeaCode.equalsIgnoreCase(MyTools.StrFiltr(curExportDataVec.get(tempIndex + 4)))) && 
		                          (tempSiteCode.equalsIgnoreCase(MyTools.StrFiltr(curExportDataVec.get(tempIndex + 6)))) && 
		                          (tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(curExportDataVec.get(tempIndex + 8)))))
		                        {
		                          timeOrderFlag = MyTools.StrFiltr(curExportDataVec.get(tempIndex));
		                          mergeNum++;
		                        }
		                        else
		                        {
		                          flag = false;
		                        }
		                      }
		                      else
		                      {
		                        flag = false;
		                      }
		                    }
		                    if (mergeNum > 0) {
		                      wsheet.mergeCells(colNum - 1, rowNum - 1, colNum - 1, rowNum + mergeNum - 1);
		                    }
		                    tempVec = parseCourseInfo(exportType, tempCourseName, tempTeaName, tempSiteName, tempSkzc, tempClassName, oddVec, evenVec);
		                    cellContent = MyTools.StrFiltr(tempVec.get(0));
		                    Label content = new Label(colNum - 1, rowNum - 1, cellContent, writableCellFormat);
		                    rowNum += mergeNum;
		                    if (MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1))) > maxWidth) {
		                      maxWidth = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(1)));
		                    }
		                    if (MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2))) > maxHeight) {
		                      maxHeight = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(2)));
		                    }
		                    wsheet.addCell(content);
		                  }
		                }
		              }
		            }
		          }
		          WritableFont fontStyle = new WritableFont(WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		          contentStyle = new WritableCellFormat(fontStyle);
		          contentStyle.setAlignment(Alignment.CENTRE);
		          contentStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
		          if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)) || ("studentKcb".equalsIgnoreCase(exportType))) {
		            wsheet.setRowView(zjs + 2, 1700, false);
		          }
		          if (("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType))) {
		            wsheet.setRowView(zjs + 2, 2400, false);
		          }
		          if (("classKcb".equalsIgnoreCase(exportType)) || ("classKcbAll".equalsIgnoreCase(exportType)) || ("studentKcb".equalsIgnoreCase(exportType)))
		          {
		            Label content = new Label(0, zjs + 2, "备注", contentStyle);
		            wsheet.addCell(content);

		            wsheet.mergeCells(1, zjs + 2, mzts, zjs + 2);
		            content = new Label(1, zjs + 2, MyTools.StrFiltr(bzInfoVec.get(bzInfoVec.indexOf(allExportDataVec.get(i)) + 1)), contentStyle);
		            wsheet.addCell(content);
		          }
		          else if ((("teaKcb".equalsIgnoreCase(exportType)) || ("teaKcbAll".equalsIgnoreCase(exportType))) && 
		            (bzInfoVec != null) && (bzInfoVec.size() > 0))
		          {
		            WritableFont writableFont = new WritableFont(WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD, false);
		            WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
		            writableCellFormat.setWrap(true);
		            writableCellFormat.setAlignment(Alignment.LEFT);
		            writableCellFormat.setVerticalAlignment(VerticalAlignment.TOP);
		            
		            wsheet.mergeCells(0, zjs + 2, mzts, zjs + 2);
		            Label content = new Label(0, zjs + 2, bzCellContent, writableCellFormat);
		            wsheet.addCell(content);
		          }
		        }
		        wbook.write();
		        
		        wbook.close();
		        os.close();
		        setMSG("文件生成成功");
		      }
		      catch (FileNotFoundException e)
		      {
		        setMSG("导出前请先关闭相关EXCEL");
		      }
		      catch (WriteException e)
		      {
		        setMSG("文件生成失败");
		      }
		      catch (IOException e)
		      {
		        setMSG("文件生成失败");
		      }
		      setMSG("文件生成成功");
		    }
		    else
		    {
		      setMSG("没有符合条件的成绩信息");
		    }
		    return savePath;
		  }
		  
		/**
		 * 解析课程信息格式
		 * @date:2015-10-20
		 * @author:yeq
		 * @param type 数据类型
		 * @param courseName 课程名称
		 * @param teaName 教师名称
		 * @param siteName 场地名称
		 * @param weekNum 授课周次
		 * @param hbClassName 合班班级名称
		 * @param oddVec 单周周次
		 * @param evenVec 双周周次
		 * @throws SQLException
		 */
		public static Vector parseCourseInfo(String type, String courseName, String teaName, String siteName, String weekNum, String hbClassName, Vector oddVec, Vector evenVec){
			Vector vec = new Vector();
			String str = "";
			double fontSize = 10; 
			double maxWidth = 0;
			double maxHeight = 0;
			double curHeight = 0;
			String dataType = type.substring(0, 3);
			
			String splitStr = "";
			if("all".equalsIgnoreCase(dataType)){
				splitStr = "，";
			}else{
				splitStr = "\n";
			}
			
			//判断是单门课程还是拼接的课程
			if(courseName.indexOf('｜') > -1){
				String tempCourseName[] = courseName.split("｜", -1);
				String tempHbClass[] = hbClassName.split("｜", -1);
				String tempTeaName[] = teaName.split("｜", -1);
				String tempSiteName[] = siteName.split("｜", -1);
				String tempWeekNum[] = weekNum.split("｜", -1);
				
				if("all".equalsIgnoreCase(dataType)){
					if("allClassKcb".equalsIgnoreCase(type))
						curHeight = 55*tempCourseName.length;
					else
						curHeight = 45*tempCourseName.length;
				}else{
					if("classKcb".equalsIgnoreCase(type))
						curHeight = 45*tempCourseName.length;
					else
						curHeight = 35*tempCourseName.length;
				}
				
				if("classTable".equalsIgnoreCase(type)){
					curHeight = 45*tempCourseName.length;
				}
				
				if(curHeight > maxHeight)
					maxHeight = curHeight;
				
				for(int i=0; i<tempCourseName.length; i++){
					str += tempCourseName[i];
					
					if(!"".equalsIgnoreCase(tempHbClass[i])){
						str += splitStr + "(并" + tempHbClass[i] + ")";
					}
					
					if("all".equalsIgnoreCase(dataType)){
						if(tempWeekNum[i].equals("")){
							//str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + splitStr + tempSiteName[i].replaceAll("\\+", "、");
						}else{
							str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + " (" + parseWeekShow(tempWeekNum[i], oddVec, evenVec) + ") " + splitStr + tempSiteName[i].replaceAll("\\+", "、");
						}
					}else{
						if(tempWeekNum[i].equals("")){
							//str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + tempSiteName[i].replaceAll("\\+", "、");
						}else{
							str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + " (" + parseWeekShow(tempWeekNum[i], oddVec, evenVec) + ") " + tempSiteName[i].replaceAll("\\+", "、");
						}
					}
					
					if(i < tempCourseName.length-1){
						if("all".equalsIgnoreCase(dataType)){
							str += "；";
						}else{
							str += splitStr;
						}
						
					}
					
					//判断课程表最大宽度
					if(fontSize/6*courseName.length() > maxWidth)
						maxWidth = fontSize/6*courseName.length();
					if("all".equalsIgnoreCase(dataType)){
						if(fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length()) > maxWidth)
							maxWidth = fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length());
						if(siteName.length()+2 >maxWidth)
							maxWidth = fontSize/8*(tempSiteName[i].length()+2);
					}else{
						if(fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length()+tempSiteName[i].length()+2) > maxWidth)
							maxWidth = fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length()+tempSiteName[i].length()+2);
					}
				}
			}else{
				str = courseName;
				
				if(!"".equalsIgnoreCase(hbClassName)){
					str += splitStr + "(并" + hbClassName + ")";
				}
				
				if("all".equalsIgnoreCase(dataType)){
					if(weekNum.equals("")){
						//str += splitStr + teaName.replaceAll("\\+", "、") + splitStr + siteName.replaceAll("\\+", "、");
					}else{
						str += splitStr + teaName.replaceAll("\\+", "、") + " (" + parseWeekShow(weekNum, oddVec, evenVec) + ") " + splitStr + siteName.replaceAll("\\+", "、");
					}
				}else{
					if(weekNum.equals("")){
						//str += splitStr + teaName.replaceAll("\\+", "、") + siteName.replaceAll("\\+", "、");
					}else{
						str += splitStr + teaName.replaceAll("\\+", "、") + " (" + parseWeekShow(weekNum, oddVec, evenVec) + ") " + siteName.replaceAll("\\+", "、");
					}
				}
				
				//判断课程表最大宽度
				if(fontSize/6*courseName.length() > maxWidth)
					maxWidth = fontSize/6*courseName.length();
				if("all".equalsIgnoreCase(dataType)){
					if(fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length()) > maxWidth)
						maxWidth = fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length());
					if(siteName.length()+2 >maxWidth)
						maxWidth = fontSize/8*(siteName.length()+2);
				}else{
					if(fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length()+siteName.length()+2) > maxWidth)
						maxWidth = fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length()+siteName.length()+2);
				}
			}
			
			vec.add(str);
			vec.add(maxWidth);
			vec.add(maxHeight);
			return vec;
		}
		
		/**解析周次显示方式
		 * @param skzc 周次原数据
		 * @param oddVec 单周周次
		 * @param evenVec 双周周次
		 * **/
		public static String parseWeekShow(String skzc, Vector oddVec, Vector evenVec){
			String result = "";
			String skzcArray[] = skzc.split("&", -1);
			
			for(int i=0; i<skzcArray.length; i++){
				if(skzcArray[i].indexOf("-") > -1){
					result +=  skzcArray[i] + "周";
				}else if("odd".equalsIgnoreCase(skzcArray[i])){
					result += "单周";
				}else if("even".equalsIgnoreCase(skzcArray[i])){
					result += "双周";
				}else if(skzcArray[i].indexOf("#")>-1 || (skzcArray[i].indexOf("#")<0&&skzcArray[i].length()>0&&skzcArray[i].length()<3)){
					//判断是否为单双周
					String curWeekArray[] = skzcArray[i].split("#");
					boolean oddFlag = true;
					boolean evenFlag = true;
					
					if(oddVec.size() == curWeekArray.length){
						for(int j=0; j<oddVec.size(); j++){
							if(!MyTools.StrFiltr(oddVec.get(i)).equalsIgnoreCase(curWeekArray[i])){
								oddFlag = false;
								break;
							}
						}
					}else{
						oddFlag = false;
					}
					
					if(evenVec.size() == curWeekArray.length){
						for(int j=0; j<evenVec.size(); j++){
							if(!MyTools.StrFiltr(evenVec.get(i)).equalsIgnoreCase(curWeekArray[i])){
								evenFlag = false;
								break;
							}
						}
					}else{
						evenFlag = false;
					}
					
					if(oddFlag == true)
						result += "单周";
					else if(evenFlag == true)
						result += "双周";
					else
						result += "第" + skzcArray[i].replaceAll("#", ",") + "周";
				}else{
					result += skzcArray[i];
				}
				
				result += "&";
			}
			if(result.length() > 0){
				result = result.substring(0, result.length()-1);
			}
			
			return result;
		}
		  
		  
		  
		  
		  /**
			 * 课程表导出
			 * @date:2017-11-02
			 * @author:zhaixuchao
			 * @param xnxqbm 学年学期编码
			 * @param exportType 导出课表类型
			 * @param code 班级/教师编号
			 * @param timetableName 课程名称
			 * @throws SQLException
			 */
			public String ExportExceldaochuquanxiao(String sAuth, String userCode, String xnxqbm, String exportType, String code, String timetableName) throws SQLException, UnsupportedEncodingException{
				//request.setCharacterEncoding("UTF-8"); //设置字符集
				DBSource db = new DBSource(request); //数据库对象
				String sql = "";
				Vector timeVec = null;
				Vector vec = new Vector();
				Vector hbSetVec = new Vector();
				Vector allKcbVec = new Vector();
				Vector curKcbVec = new Vector();
				String schoolName = MyTools.getProp(request, "Base.schoolName");
				
				final String weekNameArray[] = {"一","二","三","四","五","六","日"};
				final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
				final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
				Vector oddVec = new Vector();//单周周次
				Vector evenVec = new Vector();//双周周次
				String xnxqmc = "";//学年学期名称
				int mzts = 0;//每周天数
				int sw = 0;//上午节数
				int zw = 0;//中午节数
				int xw = 0;//下午节数
				int ws = 0;//晚上节数
				int zjs = 0;//总节数
				String timeArray[] = new String[0];
				int maxWidth = 0;
				int maxHeight = 50;
				float fontSize = 0;
				int curColName = 0;
				
				//Workbook wb = new Workbook();
				//Cell cell;
				String cellContent = ""; //当前单元格的内容
				int weekIndex = 0;
				int jsIndex = 1;
				int tempJsIndex = 1;
				int tempZwjsIndex = 0;
				Vector tempVec = new Vector();
				
				String tempXq = "";
				String tempJc = "";
				int tempIndex = -1;
				int mergeNum = 0;//单元格合并数
				boolean flag = true;//用于判断是否可合并
				int jc = 0;
				
				String xzbdm = "";
				String xzbmc = "";
				String sjxl = "";
				String skjhmxbh = "";
				String kcbh = "";
				String kcmc = "";
				String hbClass = "";
				String skjsbh = "";
				String skjsxm = "";
				String cdbh = "";
				String cdmc = "";
				String skzc = "";
				String tempSkjhmxbh = "";//授课计划明细编号
				String tempOrder = "";//时间序列
				String tempCourseName = "";//课程名称
				String tempClassCode = "";//班级编号
				String tempClassName = "";//班级名称
				String tempTeaCode = "";//教师编号
				String tempTeaName = "";//教师姓名
				String tempSiteCode = "";//场地编号
				String tempSiteName = "";//场地名称
				String tempSkzc = "";//授课周次
				
				String savePath = "";
				
				String skjhmxbhArray[] = new String[0];
				String captionName = "";
				
				Vector classInfoVec = null;
				Vector teaVec = null;
				
				String admin = MyTools.getProp(request, "Base.admin");//管理员
				String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
				String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
				String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
				String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
				String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
				String bzr = MyTools.getProp(request, "Base.bzr");//班主任
				
				//获取当前学年学期的上课时间相关信息
				sql = "select distinct (select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as 学期周次," +
					"t1.学年学期名称,t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
					"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间 " +
					"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				timeVec = db.GetContextVector(sql);
				if(timeVec!=null && timeVec.size()>0){
					int xqzc = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(0)));
					for(int i=1; i<xqzc+1; i+=2){
						oddVec.add(i);
					}
					for(int i=2; i<xqzc+1; i+=2){
						evenVec.add(i);
					}
					
					xnxqmc = MyTools.StrFiltr(timeVec.get(1));
					mzts = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(2)));
					sw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(3)));
					zw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(4)));
					xw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(5)));
					ws = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(6)));
					zjs = sw+zw+xw+ws;
					timeArray = MyTools.StrFiltr(timeVec.get(7)).split(",", -1);
				}
				
				//获取当前专业所有班级的课表信息
				if("allClassKcb".equalsIgnoreCase(exportType)){
					String tempNum = "";
					String tempRemark = "";
					String tempHbClass = "";
					String hbSetInfo = "";
					classInfoVec = null;
					Vector courseVec = new Vector();
					
					//查询相关行政班信息
//					sql = "select a.行政班代码,a.行政班名称,(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数,b.备注 from V_学校班级_数据子类 a " +
//						"left join V_排课管理_课程表主表 b on b.行政班代码=a.行政班代码 " +
//						"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//						"and a.专业代码='" + MyTools.fixSql(code) + "' order by a.行政班代码";
					sql = "select a.班级编号,a.班级名称," +
						//"(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数," +
						"a.总人数,c.教室名称,isnull(b.备注,'') " +
						//"from V_学校班级_数据子类 a " +
						"from V_基础信息_班级信息表 a " +
						"left join V_排课管理_课程表主表 b on b.行政班代码=a.班级编号 and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
						"left join V_教室数据类 c on c.教室编号=a.教室编号 " +
						"where a.系部代码='" + MyTools.fixSql(code) + "' " +
						"and cast(left(a.年级代码,2) as int) in (" +
						"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)-2," +
						"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)-1," +
						"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)) " +
						"order by a.班级编号";
					classInfoVec = db.GetContextVector(sql);
					
					if(classInfoVec!=null && classInfoVec.size()>0){
						//查询合班信息
//						sql = "select distinct b.时间序列,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from V_规则管理_授课计划明细表 a " +
//							"inner join V_排课管理_课程表明细详情表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//							"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//							"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//							"order by b.时间序列";
//						hbSetVec = db.GetContextVector(sql);
						
						//查询合班信息
						//20170125修改yeq 查询效率优化
						Vector classKcbVec = null;
						Vector classSkjhVec = null;
						Vector hbSkjhVec = null;
						tempVec = new Vector();
						hbSetVec = new Vector();
						
						sql = "select 时间序列,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 " +
							"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
						sql += " and 授课计划明细编号<>'' order by 时间序列";
						classKcbVec = db.GetContextVector(sql);
						
						if(classKcbVec!=null && classKcbVec.size()>0){
							sql = "select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
								"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
							classSkjhVec = db.GetContextVector(sql);
							
							if(classSkjhVec!=null && classSkjhVec.size()>0){
								for(int i=0; i<classKcbVec.size(); i+=3){
									tempSkjhmxbh = MyTools.StrFiltr(classKcbVec.get(i+2));
									
									for(int j=0; j<classSkjhVec.size(); j++){
										if(tempSkjhmxbh.indexOf(MyTools.StrFiltr(classSkjhVec.get(j))) > -1){
											tempVec.add(MyTools.StrFiltr(classKcbVec.get(i)));
											tempVec.add(MyTools.StrFiltr(classKcbVec.get(i+1)));
											tempVec.add(MyTools.StrFiltr(classSkjhVec.get(j)));
										}
									}
								}
								
								//查询所有合班设置信息
								sql = "select 授课计划明细编号 from V_规则管理_合班表";
								hbSkjhVec = db.GetContextVector(sql);
								
								if(hbSkjhVec!=null && hbSkjhVec.size()>0){
									for(int i=0; i<tempVec.size(); i+=3){
										tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(i+2));
										
										for(int j=0; j<hbSkjhVec.size(); j++){
											if(MyTools.StrFiltr(hbSkjhVec.get(j)).indexOf(tempSkjhmxbh) > -1){
												hbSetVec.add(tempVec.get(i));
												hbSetVec.add(tempVec.get(i+1));
												hbSetVec.add(tempVec.get(i+2));
												hbSetVec.add(MyTools.StrFiltr(hbSkjhVec.get(j)));
												break;
											}
										}
									}
								}
							}
						}
						
						//查询相关课程表信息
//						sql = "select distinct 行政班代码,时间序列,授课计划明细编号,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) as 授课周次 " +
//							"from V_排课管理_课程表周详情表 where 状态='1' and 课程类型 in ('01','02') and 授课计划明细编号<>'' " +
//							"and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//							"and 专业代码='" + MyTools.fixSql(code) + "' " +
//							"order by 行政班代码,时间序列,cast(授课周次 as int)";
//						sql = "select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//							"from V_排课管理_课程表周详情表 a " +
//							//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//							"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
//							"where a.状态='1' and a.授课计划明细编号<>'' " +
//							"and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//							"and b.系部代码='" + MyTools.fixSql(code) + "' " +
//							"order by a.行政班代码,a.时间序列,cast(a.授课周次 as int)";
						sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
							"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
							"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
							"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
							"from V_排课管理_课程表周详情表 a " + 
							"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
							"where a.状态='1' and 课程类型 in ('01','02') and a.授课计划明细编号<>'' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
							"and b.系部代码='" + MyTools.fixSql(code) + "'" +
							") as t order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
						tempVec = db.GetContextVector(sql);
						
						if(tempVec!=null && tempVec.size()>0){
							vec = new Vector();
							flag = true;
							
							//拼接课程周次
							while(flag){
								xzbdm = MyTools.StrFiltr(tempVec.get(0));
								sjxl = MyTools.StrFiltr(tempVec.get(1));
								skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
								kcmc = MyTools.StrFiltr(tempVec.get(3));
								skjsbh = MyTools.StrFiltr(tempVec.get(4));
								skjsxm = MyTools.StrFiltr(tempVec.get(5));
								cdbh = MyTools.StrFiltr(tempVec.get(6));
								cdmc = MyTools.StrFiltr(tempVec.get(7));
								skzc = MyTools.StrFiltr(tempVec.get(8));
								for(int i=0; i<9; i++){
									tempVec.remove(0);
								}
								
								for(int i=0; i<tempVec.size(); i+=9){
									if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) 
										&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6)))){
										skzc += "#"+MyTools.StrFiltr(tempVec.get(i+8));
										for(int j=0; j<9; j++){
											tempVec.remove(i);
										}
										i -= 9;
									}
								}
								vec.add(xzbdm);
								vec.add(sjxl);
								vec.add(skjhmxbh);
								vec.add(kcmc);
								vec.add(skjsbh);
								vec.add(skjsxm);
								vec.add(cdbh);
								vec.add(cdmc);
								vec.add(skzc);
								
								if(tempVec.size() == 0){
									flag = false;
								}
							}
						}
						
						tempVec = new Vector();
						for(int i=0; i<vec.size(); i+=9){
							skjsbh = MyTools.StrFiltr(vec.get(i+4));
							skjsxm = MyTools.StrFiltr(vec.get(i+5));
							cdbh = MyTools.StrFiltr(vec.get(i+6));
							cdmc = MyTools.StrFiltr(vec.get(i+7));
							skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+8)), oddVec, evenVec);
							
							if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
								if(i > 0){
									tempVec.add(xzbdm);
									tempVec.add(sjxl);
									tempVec.add(skjhmxbh);
									tempVec.add(kcmc);
									tempVec.add(tempTeaCode);
									tempVec.add(tempTeaName);
									tempVec.add(tempSiteCode);
									tempVec.add(tempSiteName);
									tempVec.add(tempSkzc);
								}
								
								xzbdm = MyTools.StrFiltr(vec.get(i));
								sjxl = MyTools.StrFiltr(vec.get(i+1));
								skjhmxbh = MyTools.StrFiltr(vec.get(i+2));
								kcmc = MyTools.StrFiltr(vec.get(i+3));
								tempTeaCode = skjsbh;
								tempTeaName = skjsxm;
								tempSiteCode = cdbh;
								tempSiteName = cdmc;
								tempSkzc = skzc;
							}else{
								tempTeaCode += "&"+skjsbh;
								tempTeaName += "&"+skjsxm;
								tempSiteCode += "&"+cdbh;
								tempSiteName += "&"+cdmc;
								tempSkzc += "&"+skzc;
							}
						}
						tempVec.add(xzbdm);
						tempVec.add(sjxl);
						tempVec.add(skjhmxbh);
						tempVec.add(kcmc);
						tempVec.add(tempTeaCode);
						tempVec.add(tempTeaName);
						tempVec.add(tempSiteCode);
						tempVec.add(tempSiteName);
						tempVec.add(tempSkzc);
						
						for(int i=0; i<tempVec.size(); i+=9){
							skjhmxbh = MyTools.StrFiltr(tempVec.get(i+2));
							kcmc = MyTools.StrFiltr(tempVec.get(i+3));
							skjsbh = MyTools.StrFiltr(tempVec.get(i+4));
							skjsxm = MyTools.StrFiltr(tempVec.get(i+5));
							cdbh = MyTools.StrFiltr(tempVec.get(i+6));
							cdmc = MyTools.StrFiltr(tempVec.get(i+7));
							skzc = MyTools.StrFiltr(tempVec.get(i+8));
							
							//检查合班信息
							hbClass = "";
							for(int j=0; j<hbSetVec.size(); j+=4){
								if(MyTools.StrFiltr(tempVec.get(i+1)).equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(j)))){
									hbSetInfo = MyTools.StrFiltr(hbSetVec.get(j+3));
									
									if(!skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(j+2))) && hbSetInfo.indexOf(skjhmxbh)>-1){
										hbClass += MyTools.StrFiltr(hbSetVec.get(j+1))+"、";
										break;
									}
								}
							}
							
							if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))){
								if(i > 0){
									courseVec.add(xzbdm);
									courseVec.add(sjxl);
									courseVec.add(tempSkjhmxbh);
									courseVec.add(tempCourseName);
									courseVec.add(tempHbClass);
									courseVec.add(tempTeaCode);
									courseVec.add(tempTeaName);
									courseVec.add(tempSiteCode);
									courseVec.add(tempSiteName);
									courseVec.add(tempSkzc);
								}
								
								xzbdm = MyTools.StrFiltr(tempVec.get(i));
								sjxl = MyTools.StrFiltr(tempVec.get(i+1));
								tempSkjhmxbh = skjhmxbh;
								tempCourseName = kcmc;
								tempHbClass = hbClass;
								tempTeaCode = skjsbh;
								tempTeaName = skjsxm;
								tempSiteCode = cdbh;
								tempSiteName = cdmc;
								tempSkzc = skzc;
							}else{
								tempSkjhmxbh += "｜"+skjhmxbh;
								tempCourseName += "｜"+kcmc;
								tempHbClass += "｜"+hbClass;
								tempTeaCode += "｜"+skjsbh;
								tempTeaName += "｜"+skjsxm;
								tempSiteCode += "｜"+cdbh;
								tempSiteName += "｜"+cdmc;
								tempSkzc += "｜"+skzc;
							}
						}
						courseVec.add(xzbdm);
						courseVec.add(sjxl);
						courseVec.add(tempSkjhmxbh);
						courseVec.add(tempCourseName);
						courseVec.add(tempHbClass);
						courseVec.add(tempTeaCode);
						courseVec.add(tempTeaName);
						courseVec.add(tempSiteCode);
						courseVec.add(tempSiteName);
						courseVec.add(tempSkzc);
						
						//添加体锻课  lupengfei 20170928			
						int tytag=0;//体育课标记
						int tdcs=0;//体锻课次数
						int zwyk=0;//中午是否有課
						
						//获取操场编号
						String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(code) + "' and [教室名称]='操场' ) ";
						Vector veccc=db.GetContextVector(sqlcc);
						
						//获取实际上课周数
						String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
						Vector vecskzs=db.GetContextVector(sqlskzs);
							
						//获取每周天数
						int mztsx=0;
						String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(xnxqbm) + "' ";
						Vector vecmzts=db.GetContextVector(sqlmzts);
						if(vecmzts!=null&&vecmzts.size()>0){
							mztsx=Integer.parseInt(vecmzts.get(0).toString());
						}
						
						//时间序列,授课计划明细编号,课程名称,授课教师姓名,实际场地名称,授课周次详情
						
						//循环班级
						for(int b=0;b<classInfoVec.size();b=b+5){
							tdcs=0;//体锻课次数		
							//判断哪天没体育课,中午添加体锻课
							String ts="";
							for(int i=1;i<=mztsx;i++){
								tytag=0;//体育课标记
								zwyk=0;//中午是否有課
								if(i<10){
									ts="0"+i;
								}else{
									ts=i+"";
								}
								for(int j=0;j<courseVec.size();j=j+10){ 
									if(courseVec.get(j).toString().equals(classInfoVec.get(b).toString())){//班级相同
										if(ts.equals(courseVec.get(j+1).toString().substring(0, 2))){//天数相同
											if(courseVec.get(j+3).toString().indexOf("体育")>-1){//有体育课
												tytag=1;
											}
										}
									}					
								}
								if(tytag==0){			
									for(int k=0;k<courseVec.size();k=k+10){
										if(courseVec.get(k).toString().equals(classInfoVec.get(b).toString())){//班级相同
											if((ts+"05").equals(courseVec.get(k+1).toString())){//中午有课
												zwyk=1;									
											}
										}		
									}
									if(zwyk==0){ 
										if(tdcs<2){
											//添加体锻课
											int existkc=0;//班级有课
											for(int t=0;t<courseVec.size();t=t+10){
												if(courseVec.get(t).toString().equals(classInfoVec.get(b).toString())){//班级相同
													existkc=1;
													break;
												}
											}
											if(existkc==1){
												//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
												courseVec.add(classInfoVec.get(b).toString());
												courseVec.add(ts+"05");//时间序列
												courseVec.add("SKJHMX_TD");//授课计划明细编号
												courseVec.add("体锻");//课程名称		
												courseVec.add("");//合班
												courseVec.add("");//授课教师编号
												courseVec.add("");//授课教师姓名
												courseVec.add("");//实际场地编号
												courseVec.add("");//实际场地名称
												courseVec.add("");//授课周次详情
												
												tdcs++;
											}							
										}
									}else{
										zwyk=0;
									}
								}
							}
						}

						//=============================================================================================			
						
						
						flag = false;
						for(int i=0; i<classInfoVec.size(); i+=5){
							tempClassCode = MyTools.StrFiltr(classInfoVec.get(i));
							curKcbVec = new Vector();
							
							for(int j=1; j<mzts+1; j++){
								for(int k=1; k<zjs+1; k++){
									tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
									
									flag = false;
									for(int a=0; a<courseVec.size(); a+=10){
										if(tempClassCode.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a))) && tempOrder.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a+1)))){
											curKcbVec.add(tempOrder);//时间序列
											curKcbVec.add(courseVec.get(a+2));//授课计划明细编号
											curKcbVec.add(courseVec.get(a+3));//课程名称
											curKcbVec.add(courseVec.get(a+4));//合班班级
											curKcbVec.add(courseVec.get(a+5));//授课教师编号
											curKcbVec.add(courseVec.get(a+6));//授课教师姓名
											curKcbVec.add(courseVec.get(a+7));//场地编号
											curKcbVec.add(courseVec.get(a+8));//场地名称
											curKcbVec.add(courseVec.get(a+9));//授课周次
											flag = true;
											break;
										}
									}
									if(flag == false){
										curKcbVec.add(tempOrder);
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
									}
								}
							}
							allKcbVec.add(classInfoVec.get(i+1));
							allKcbVec.add(classInfoVec.get(i+2));
							allKcbVec.add(classInfoVec.get(i+3));
							allKcbVec.add(classInfoVec.get(i+4));
							allKcbVec.add(curKcbVec);
						}
					}
				}
				
				//获取当前层级所有教师的课表信息
				if("allTeaKcb".equalsIgnoreCase(exportType)){
					teaVec = null;
					Vector xxkVec = null;
					String hbInfo = "";
					
					//获取教师名单
//					sql = "with cte1 as (select t1.工号,t1.姓名 from V_教职工基本数据子类 t1 " +
//						"inner join V_USER_AUTH t2 on t2.UserCode=t1.工号 " +
//						"inner join V_权限层级关系表 t3 on t3.权限编号=t2.AuthCode " +
//						"inner join V_层级表 t4 on t4.层级编号=t3.层级编号 where t4.层级编号='" + MyTools.fixSql(code) + "') " +
//						"select distinct * from (select a.工号,a.姓名 from cte1 a " +
//						"inner join (select distinct 专业代码,授课教师编号 from V_排课管理_课程表周详情表 " +
//						"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 课程代码<>'') b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' " +
//						"union all " +
//						"select a.工号,a.姓名 from cte1 a " +
//						"inner join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%'+a.工号+'%' " +
//						"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as t where 工号 is not null order by 姓名";
					sql = "select distinct * from (" +
						"select 工号,姓名 from V_教职工基本数据子类 where 工号='" + MyTools.fixSql(userCode) + "' " +
						"union all " +
						"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
						"inner join (select distinct t1.授课教师编号 from V_排课管理_课程表周详情表 t1 " +
						//"left join V_学校班级_数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
						"left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 " +
						"where t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and t1.课程代码<>''";
					//权限判断
					if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
						sql += " and (1=2";
						//班主任
						if(sAuth.indexOf(bzr) > -1){
							sql += " or t2.班主任工号='" + MyTools.fixSql(userCode) + "'";
						}
						//系部教务人员
						if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
							sql += " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
						}
						sql += ")";
					}
					sql += ") b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' ";
					
					//判断如果是管理员或全校教务权限，需查询选修课教师信息
					if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1){
						sql += "union all " +
							"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
							"inner join (select 授课教师编号 from V_规则管理_选修课授课计划主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') b " +
							"on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%'";
					}
					sql += ") as t where 工号 is not null order by 姓名";
					teaVec = db.GetContextVector(sql);
					
					if(teaVec!=null && teaVec.size()>0){
						//获取选修课信息
						sql = "select c.时间序列,a.授课计划明细编号,b.课程名称,a.选修班名称,a.授课教师编号,c.实际场地编号,c.实际场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a " +
							"inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
							"left join V_排课管理_选修课课程表信息表 c on c.授课计划明细编号=a.授课计划明细编号 " +
							"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
							"order by c.时间序列,cast((case substring(a.授课周次, 2, 1) when '｜' then substring(a.授课周次, 1, 1) when '&' then substring(a.授课周次, 1, 1) when '#' then substring(a.授课周次, 1, 1) " +
							"when '-' then substring(a.授课周次, 1, 1) when 'd' then '1' when 'v' then '2' else substring(a.授课周次, 1, 2) end) as int)";
						xxkVec = db.GetContextVector(sql);
						
						//获取合班设置信息
						sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a " +
							"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
							"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
							"where c.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
						hbSetVec = db.GetContextVector(sql);
						
						//获取教师课程信息
//						sql = "select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//							"from V_排课管理_课程表周详情表 a where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and (";
//						for(int i=0; i<teaVec.size(); i+=2){
//							sql += "'@'+replace(replace(授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
//						}
//						sql = sql.substring(0, sql.length()-4);
//						sql += ") order by a.时间序列,cast(a.授课周次 as int)";
						sql = "select t.时间序列,t.授课计划明细编号,t.行政班代码,t.行政班名称,t.课程代码,t.课程名称,t.授课教师编号,t.场地编号,t.场地名称,t.授课周次 " + 
							"from (select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
							"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
							"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
							"from V_排课管理_课程表周详情表 a " + 
							"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and (";
						for(int i=0; i<teaVec.size(); i+=2){
							sql += "'@'+replace(replace(授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
						}
						sql = sql.substring(0, sql.length()-4);
						sql += ")) as t order by t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
						tempVec = db.GetContextVector(sql);
						
						if(tempVec!=null && tempVec.size()>0){
							vec = new Vector();
							flag = true;
							
							//拼接同一课程周次
							while(flag){
								sjxl = MyTools.StrFiltr(tempVec.get(0));
								skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
								xzbdm = MyTools.StrFiltr(tempVec.get(2));
								xzbmc = MyTools.StrFiltr(tempVec.get(3));
								kcbh = MyTools.StrFiltr(tempVec.get(4));
								kcmc = MyTools.StrFiltr(tempVec.get(5));
								skjsbh = MyTools.StrFiltr(tempVec.get(6));
								cdbh = MyTools.StrFiltr(tempVec.get(7));
								cdmc = MyTools.StrFiltr(tempVec.get(8));
								skzc = MyTools.StrFiltr(tempVec.get(9));
								for(int i=0; i<10; i++){
									tempVec.remove(0);
								}
								
								for(int i=0; i<tempVec.size(); i+=10){
									if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))
										&& xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) 
										&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+7)))){
										skzc += "#"+MyTools.StrFiltr(tempVec.get(i+9));
										for(int j=0; j<10; j++){
											tempVec.remove(i);
										}
										i -= 10;
									}
								}
								vec.add(sjxl);
								vec.add(skjhmxbh);
								vec.add(xzbmc);
								vec.add(kcmc);
								vec.add(skjsbh);
								vec.add(cdbh);
								vec.add(cdmc);
								vec.add(skzc);
								
								if(tempVec.size() == 0){
									flag = false;
								}
							}
							tempVec = new Vector();
							for(int i=0; i<vec.size(); i+=8){
								cdmc = MyTools.StrFiltr(vec.get(i+6));
								skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+7)), oddVec, evenVec);
								
								if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) 
									|| !skjsbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) || !cdbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
									if(i > 0){
										tempVec.add(sjxl);
										tempVec.add(skjhmxbh);
										tempVec.add(xzbmc);
										tempVec.add(kcmc);
										tempVec.add(tempTeaCode);
										tempVec.add(tempSiteCode);
										tempVec.add(tempSiteName);
										tempVec.add(tempSkzc);
									}
									
									sjxl = MyTools.StrFiltr(vec.get(i));
									skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
									xzbmc = MyTools.StrFiltr(vec.get(i+2));
									kcmc = MyTools.StrFiltr(vec.get(i+3));
									skjsbh = MyTools.StrFiltr(vec.get(i+4));
									cdbh = MyTools.StrFiltr(vec.get(i+5));
									tempTeaCode = skjsbh;
									tempSiteCode = cdbh;
									tempSiteName = cdmc;
									tempSkzc = skzc;
								}else{
									tempTeaCode += "&"+MyTools.StrFiltr(vec.get(i+4));
									tempSiteCode += "&"+cdbh;
									tempSiteName += "&"+cdmc;
									tempSkzc += "&"+skzc;
								}
							}
							tempVec.add(sjxl);
							tempVec.add(skjhmxbh);
							tempVec.add(xzbmc);
							tempVec.add(kcmc);
							tempVec.add(tempTeaCode);
							tempVec.add(tempSiteCode);
							tempVec.add(tempSiteName);
							tempVec.add(tempSkzc);
							
							//合并合班课程信息
							for(int i=0; i<tempVec.size(); i+=8){
								sjxl = MyTools.StrFiltr(tempVec.get(i));
								skjhmxbh = MyTools.StrFiltr(tempVec.get(i+1));
								
								for(int j=0; j<hbSetVec.size(); j++){
									hbInfo = MyTools.StrFiltr(hbSetVec.get(j));
									
									if(hbInfo.indexOf(skjhmxbh) > -1){
										for(int k=(i+8); k<tempVec.size(); k+=8){
											if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(k)))){
												break;
											}
											
											if(hbInfo.indexOf(MyTools.StrFiltr(tempVec.get(k+1))) > -1){
												tempVec.set(i+2, MyTools.StrFiltr(tempVec.get(i+2))+"、"+MyTools.StrFiltr(tempVec.get(k+2)));
												
												for(int a=0; a<8; a++){
													tempVec.remove(k);
												}
												k -= 8;
											}
										}
									}
								}
							}
						}
						
						for(int i=0; i<teaVec.size(); i+=2){
							tempTeaCode = MyTools.StrFiltr(teaVec.get(i));
							curKcbVec = new Vector();
							
							for(int j=1; j<mzts+1; j++){
								for(int k=1; k<zjs+1; k++){
									tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
									tempSkjhmxbh = "";
									tempClassName = "";
									tempCourseName = "";
									tempTeaName = "";
									tempSiteCode = "";
									tempSiteName = "";
									tempSkzc = "";
									
									//添加普通课程信息
									flag = false;
									for(int a=0; a<tempVec.size(); a+=8){
										if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(a)))){
											break;
										}
										
										if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a))) && MyTools.StrFiltr(tempVec.get(a+4)).indexOf(tempTeaCode)>-1){
											tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(a+1))+"｜";
											tempClassName += MyTools.StrFiltr(tempVec.get(a+2))+"｜";
											tempCourseName += MyTools.StrFiltr(tempVec.get(a+3))+"｜";
											tempTeaName += "｜";
											tempSiteCode += MyTools.StrFiltr(tempVec.get(a+5))+"｜";
											tempSiteName += MyTools.StrFiltr(tempVec.get(a+6))+"｜";
											tempSkzc += MyTools.StrFiltr(tempVec.get(a+7))+"｜";
											flag = true;
										}
									}
									//添加选修课信息
									for(int a=0; a<xxkVec.size(); a+=8){
										if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(xxkVec.get(a))) && MyTools.StrFiltr(xxkVec.get(a+4)).indexOf(tempTeaCode)>-1){
											tempSkjhmxbh += MyTools.StrFiltr(xxkVec.get(a+1))+"｜";
											tempCourseName += MyTools.StrFiltr(xxkVec.get(a+2))+"｜";
											tempTeaName += "｜";
											tempClassName += MyTools.StrFiltr(xxkVec.get(a+3))+"｜";
											tempSiteCode += MyTools.StrFiltr(xxkVec.get(a+5))+"｜";
											tempSiteName += MyTools.StrFiltr(xxkVec.get(a+6))+"｜";
											tempSkzc += MyTools.StrFiltr(xxkVec.get(a+7))+"｜";
											flag = true;
											
											if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(xxkVec.get(a)))){
												break;
											}
										}
									}
									if(flag == false){
										curKcbVec.add(tempOrder);
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
									}else{
										curKcbVec.add(tempOrder);
										curKcbVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
										curKcbVec.add(tempCourseName.substring(0, tempCourseName.length()-1));
										curKcbVec.add(tempTeaName.substring(0, tempTeaName.length()-1));
										curKcbVec.add("");
										curKcbVec.add(tempClassName.substring(0, tempClassName.length()-1));
										curKcbVec.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
										curKcbVec.add(tempSiteName.substring(0, tempSiteName.length()-1));
										curKcbVec.add(tempSkzc.substring(0, tempSkzc.length()-1));
									}
								}
							}
							
							allKcbVec.add(MyTools.StrFiltr(teaVec.get(i+1)));
							allKcbVec.add("");
							allKcbVec.add("");
							allKcbVec.add("");
							allKcbVec.add(curKcbVec);
						}
					}else{
						allKcbVec.add("没有相关教师信息");
						allKcbVec.add("");
						allKcbVec.add("");
						allKcbVec.add("");
						allKcbVec.add(curKcbVec);
					}
				}
				
				//整理数据
				if(allKcbVec!=null && allKcbVec.size()>0){
					Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
					int year = c.get(Calendar.YEAR); 
					int month = c.get(Calendar.MONTH); 
					int date = c.get(Calendar.DATE);
					
					savePath = MyTools.getProp(request, "Base.exportExcelPath");
					
					int totalColName = 0;
					int totalNum = allKcbVec.size()/5+4;
					//声明工作簿jxl.write.WritableWorkbook  
					//创建
					try {
						File file = new File(savePath);
						if(!file.exists()){
							file.mkdirs();
						}
						
						if("allClassKcb".equalsIgnoreCase(exportType))
							captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 全日制课表";
						else if("allTeaKcb".equalsIgnoreCase(exportType))
							captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + "授课计划表";
						
						
						savePath += "/" + captionName + year + ((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
						OutputStream os = new FileOutputStream(savePath);
						
					//翟旭超2017/11/03调用模板  begin
						//converter.convert(docFile, new File(request.getSession().getServletContext().getRealPath("/")+"\\mobile\\"+pdfFile));
						
						jxl.Workbook wb=null;
						String templePath="";
						// 读取模板文件
						 //String templePath = "C:\\Users\\dell\\Desktop\\allClassTemplate.xls";
						if("allClassKcb".equalsIgnoreCase(exportType)) {
							templePath = request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\allClassTemplate.xls";
						}
						if("allTeaKcb".equalsIgnoreCase(exportType)) {
							//templePath = request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\allTeaTemplate.xls";
							
							templePath = request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\allTeaTemplate.xls";
						}
						//String templePath = request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\allClassTemplate.xls";
				        try {
							wb = jxl.Workbook.getWorkbook(new File(templePath));
						} catch (BiffException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        WritableWorkbook wbook = jxl.Workbook.createWorkbook(os, wb);
				        WritableSheet wsheet = wbook.getSheet(0);
				        SheetSettings sheetSettings= wsheet.getSettings();
				        sheetSettings.setScaleFactor(100);
					//翟旭超2017/11/03调用模板  end
				    
				    //插入图片,由于只支持png格式,需要转换成png.  翟旭超2017/11/03导入图片 begin
					        // 读入图片  
				        	//File imgFile = new File("C:\\Users\\dell\\Desktop\\无标题.png");
				        	File imgFile = new File( request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\qxbjzb.png");
					        BufferedImage picImage = ImageIO.read(imgFile);  
					           
					        //生成一个图片对象。  
					        WritableImage image = new WritableImage(0, 1, 4, 3, imgFile);  
					        // 把图片插入到sheet  
					        wsheet.addImage(image);
					    //翟旭超2017/11/03导入图片 end
				        
						
						//WritableWorkbook wbook = jxl.Workbook.createWorkbook(os);//建立excel文件
						//WritableSheet wsheet = wbook.createSheet("Sheet1", 0);//工作表名称
						WritableFont fontStyle;
						WritableCellFormat contentStyle = null;
						Label content;
						
						
						int sum=0;
						
						if(totalNum > colName.length){
							if(totalNum%colName.length == 0){
								//totalColName = colName[(totalNum-1)/colName.length-1]+colName[colName.length-1];
								
								totalColName = (totalNum-1)/colName.length-1 + colName.length-1;
							}else{
								//totalColName = colName[totalNum/colName.length-1]+colName[totalNum%colName.length-1];
								
								totalColName = totalNum/colName.length-1 + totalNum%colName.length-1;
							}
						}else{
							totalColName = totalNum-1;
						}
						for(int colNum=1; colNum<allKcbVec.size()/5+5; colNum++){
							if(colNum > 4){
								curKcbVec = (Vector)allKcbVec.get((colNum-5)*5+4);
							}
							
							if(colNum > colName.length){
								if(colNum%colName.length == 0){
									//curColName = colName[(colNum-1)/colName.length-1]+colName[colName.length-1];
									
									curColName = (colNum-1)/colName.length-1 + colName.length-1;
								}else{
									//curColName = colName[colNum/colName.length-1]+colName[colNum%colName.length-1];
									
									curColName = colNum/colName.length-1 + colNum%colName.length-1;
								}
							}else{
								curColName = colNum-1;
							}
							
							for(int rowNum=1; rowNum<zjs*mzts+6; rowNum++){
								if(jsIndex > zjs){
									jsIndex = 1;
									tempJsIndex = 1;
									tempZwjsIndex = 0;
								}
								
								//设置列宽
								/*for(int j=0;j<mzts+3;j++){
									if(j==0 || j==1 ){
										wsheet.setColumnView(j, 5);
									}else if(j==2 || j==3){
										wsheet.setColumnView(j, 7);
									}else{
										if(allKcbVec.size()/5 < 2){
											maxWidth = 154/(allKcbVec.size()/5);
										}else{
											maxWidth = 77;
										}
										wsheet.setColumnView(j, maxWidth);
									}
									System.out.println("j:"+j);
								}*/
								
								//设置列宽
								/*if("allClassKcb".equalsIgnoreCase(exportType))
									captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 全日制课表";
								else if("allTeaKcb".equalsIgnoreCase(exportType))
									captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + "授课计划表";*/
								
								if("allClassKcb".equalsIgnoreCase(exportType)) {
									for(int j=0;j<(classInfoVec.size()/2)+4;j++){
										if(j==0 || j==1 ){
											wsheet.setColumnView(j, 4);
										}else if(j==2 || j==3){
											wsheet.setColumnView(j, 7);
										}else{
											wsheet.setColumnView(j, 77);
											if(allKcbVec.size()/5 < 2){
												maxWidth = 154/(allKcbVec.size()/5);
											}else{
												maxWidth = 77;
											}
											wsheet.setColumnView(j, maxWidth);
										}
									}
								}else if("allTeaKcb".equalsIgnoreCase(exportType)){
									for(int j=0;j<(teaVec.size()/2)+4;j++){
										if(j==0 || j==1 ){
											wsheet.setColumnView(j, 4);
										}else if(j==2 || j==3){
											wsheet.setColumnView(j, 7);
										}else{
											wsheet.setColumnView(j, 77);
											if(allKcbVec.size()/5 < 2){
												maxWidth = 154/(allKcbVec.size()/5);
											}else{
												maxWidth = 77;
											}
											wsheet.setColumnView(j, maxWidth);
										}
									}
								}
								
								
								//生成标题
								if(colNum==1 && rowNum==1){
									//tempSheet.openTable(colName[0]+"1:"+totalColName+"1").merge();
									//cell = tempSheet.openCell("A1");
									
									
									if("allClassKcb".equalsIgnoreCase(exportType))
										cellContent = "                         " + schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 全日制课表";
									else if("allTeaKcb".equalsIgnoreCase(exportType))
										cellContent = "                            " + schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + "授课计划表";
									
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									//contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
									//contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									//contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									//contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
									
									wsheet.setRowView(rowNum-1, 30*20, false); //设置行高
									wsheet.setRowView(rowNum, 25*20, false); //设置行高
									
									wsheet.mergeCells(0, 0, totalColName , 0);//跨行
									
									content = new Label(0, 0, cellContent, contentStyle);
									wsheet.addCell(content);
									
									//maxWidth = cellContent.length()/(allKcbVec.size()/4);
									//cell.setValue(cellContent);
									
									//设置标题字体大小
									//cell = tempSheet.openCell("A1");
									//cell.getFont().setBold(true);
									//fontSize = 18;
									//cell.getFont().setSize(fontSize);
								}else{
									//备注
									if(rowNum==zjs*mzts+5){
										if("allClassKcb".equalsIgnoreCase(exportType)){
											if(colNum==1){
												//tempSheet.openTable(colName[0]+rowNum+":"+colName[3]+rowNum).merge();
												//cell = tempSheet.openCell(colName[0]+rowNum);
												//cell.setValue("备注");
												
												
												fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
												contentStyle = new WritableCellFormat(fontStyle);
												contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
												contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
												
												wsheet.setRowView(zjs*mzts+5-1, 95*20, false); //设置行高
												
												wsheet.mergeCells(0, rowNum-1, 3 , rowNum-1);//跨行
												content = new Label(0, rowNum-1, "备注", contentStyle);
												wsheet.addCell(content);
											}
											
											if(colNum > 4){
												//cell = tempSheet.openCell(colName[colNum-1]+rowNum);
												//cellContent = MyTools.StrFiltr(allKcbVec.get((colNum-5)*4+2));
												//cell.setValue(cellContent+"\n");
												
												// 设置单元格格式
												WritableFont writableFont = new WritableFont(WritableFont.createFont("宋体"),8, WritableFont.NO_BOLD, false);
												WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
												writableCellFormat.setWrap(true);
												writableCellFormat.setAlignment(jxl.format.Alignment.LEFT);//水平居中
												writableCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
												writableCellFormat.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM);
												
												content = new Label(colNum-1, rowNum-1, MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+3)) + "\n", writableCellFormat);
												wsheet.addCell(content);
											}
										}
									}
									//判断星期列
									else if(colNum==1 && rowNum>4){
										//tempSheet.openTable(colName[0]+rowNum+":"+colName[0]+(rowNum+zjs-1)).merge();
										//cell = tempSheet.openCell(colName[0]+rowNum);
										
										//判断是否为大周
										if(mzts > 7){
											cellContent = orderNameArray[weekIndex];
										}else{
											cellContent = weekNameArray[weekIndex];
											weekIndex++;
										}
										
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
										
										wsheet.mergeCells(0, rowNum-1, 0 , rowNum+zjs-1-1);//跨行
										content = new Label(0, rowNum-1, cellContent, contentStyle);
										wsheet.addCell(content);
										
										
										//cell.setValue(cellContent);
										rowNum += (zjs-1);
									}
									//节数列
									else if(colNum==2 && rowNum>4){
										//cell = tempSheet.openCell(colName[1]+rowNum);
										if(jsIndex>sw && jsIndex<=sw+zw){
											cellContent = "中"+orderNameArray[tempZwjsIndex];
											tempZwjsIndex++;
										}else{
											cellContent = tempJsIndex+"";
											tempJsIndex++;
										}
										jsIndex++;
										
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 8, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
										
										content = new Label(1, rowNum-1, cellContent, contentStyle);
										wsheet.addCell(content);
										
										//cell.setValue(cellContent);
									}
									//时间列
									else if(colNum==3 && rowNum>4){
										//tempSheet.openTable(colName[2]+rowNum+":"+colName[3]+rowNum).merge();
										//cell = tempSheet.openCell(colName[2]+rowNum);
										//cell.setValue(timeArray[jsIndex-1]);
										
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 8, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
										
										wsheet.mergeCells(2, rowNum-1, 3 , rowNum-1);//跨行
										content = new Label(2, rowNum-1, timeArray[jsIndex-1], contentStyle);
										wsheet.addCell(content);
										
										
										jsIndex++;
									}
									
									//课表内容
									else if(colNum>4 && rowNum>1){
										
										int pageHeight = 0;
										if("allClassKcb".equalsIgnoreCase(exportType))
											pageHeight = 500;
										if("allTeaKcb".equalsIgnoreCase(exportType))
											pageHeight = 600;
										maxHeight = pageHeight/(zjs*mzts);
										
										if(allKcbVec!=null && allKcbVec.size()>0){
											
											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 8, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
											contentStyle = new WritableCellFormat(fontStyle);
											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
											
										
											if(rowNum == 2){
												fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
												contentStyle = new WritableCellFormat(fontStyle);
												contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
												contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM);
												if("allTeaKcb".equalsIgnoreCase(exportType)){
													//tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+2)).merge();
													wsheet.mergeCells(curColName, rowNum-1, curColName , rowNum+2-1);//跨行
												}
													
												content = new Label(curColName, rowNum-1, MyTools.StrFiltr(allKcbVec.get((colNum-5)*5)), contentStyle);
												wsheet.addCell(content);
												//cell = tempSheet.openCell(curColName+rowNum);
												//cell.setValue(MyTools.StrFiltr(allKcbVec.get((colNum-5)*4)));
											}
											else if(rowNum == 3){
												//cell = tempSheet.openCell(curColName+rowNum);
												//cell.setValue(MyTools.StrFiltr(allKcbVec.get((colNum-5)*4+1)) + "人");
												
												fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
												contentStyle = new WritableCellFormat(fontStyle);
												contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
												contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM);
												
												content = new Label(curColName, rowNum-1, MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+1)) + "人", contentStyle);
												wsheet.addCell(content);
											}
											else if(rowNum == 4){
												//cell = tempSheet.openCell(curColName+rowNum);
												//cell.setValue("");
												
												fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
												contentStyle = new WritableCellFormat(fontStyle);
												contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
												contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM);
												
												content = new Label(curColName, rowNum-1, MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+2)), contentStyle);
												
												wsheet.setRowView(rowNum, maxHeight*20, false); //设置行高
												
												wsheet.addCell(content);
											}
											else{
												
												WritableFont writableFont = new WritableFont(WritableFont.createFont("宋体"),8, WritableFont.NO_BOLD, false);
												WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
												writableCellFormat.setWrap(false);//设置是否自动换行
												writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
												writableCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
												writableCellFormat.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM);
												
												//=================================================
												/*int pageHeight = 0;
												if("allClassKcb".equalsIgnoreCase(exportType))
													pageHeight = 500;
												if("allTeaKcb".equalsIgnoreCase(exportType))
													pageHeight = 600;*/
												maxHeight = pageHeight/(zjs*mzts);
												//wsheet.openTable(colName[0]+"5:"+totalColName+(zjs*mzts+4)).setRowHeight(maxHeight);
												//wsheet.setRowView(rowNum-1, maxHeight*20, false); //设置行高
												wsheet.setRowView(rowNum, maxHeight*20, false); //设置行高
												//wsheet.setRowView(rowNum, maxHeight*20, false); //设置行高
												wsheet.setRowView(zjs*mzts+5-1, 95*20, false); //设置行高
											//=================================================
												
												
												tempXq = (rowNum-4)/zjs<10?"0"+((rowNum-4)/zjs+1):""+((rowNum-4)/zjs+1);
												tempJc = (rowNum-4)%zjs<10?"0"+(rowNum-4)%zjs:""+(rowNum-4)%zjs;
												if(MyTools.StringToInt(tempJc) == 0){
													tempJc = zjs<10?"0"+zjs:""+zjs;
													tempXq = MyTools.StrFiltr((MyTools.StringToInt(tempXq)-1));
													tempXq = MyTools.StringToInt(tempXq)<10?"0"+tempXq:tempXq;
												}
												tempOrder = tempXq+tempJc;
												tempIndex = curKcbVec.indexOf(tempOrder);
												
												if(tempIndex > -1){
													tempSkjhmxbh = MyTools.StrFiltr(curKcbVec.get(tempIndex+1));
													tempCourseName = MyTools.StrFiltr(curKcbVec.get(tempIndex+2));
													tempClassName = MyTools.StrFiltr(curKcbVec.get(tempIndex+3));
													tempTeaCode = MyTools.StrFiltr(curKcbVec.get(tempIndex+4));
													tempTeaName = MyTools.StrFiltr(curKcbVec.get(tempIndex+5));
													tempSiteCode = MyTools.StrFiltr(curKcbVec.get(tempIndex+6));
													tempSiteName = MyTools.StrFiltr(curKcbVec.get(tempIndex+7));
													tempSkzc = MyTools.StrFiltr(curKcbVec.get(tempIndex+8));
													
													mergeNum = 0;
													flag = true;
													
													//判断单元格是否可以合并
													while(flag){
														tempIndex += 9;
														if(tempIndex < curKcbVec.size()){
															//判断是否完全相同的课程
															if(tempXq.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex)).substring(0, 2))
																&& tempSkjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+1))) 
																&& tempTeaCode.equalsIgnoreCase( MyTools.StrFiltr(curKcbVec.get(tempIndex+4)))
																&& tempSiteCode.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+6)))
																&& tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+8)))
																){
																
																//教师课表空单元格合并
																jc = MyTools.StringToInt(MyTools.StrFiltr(curKcbVec.get(tempIndex)).substring(2));
																
																//判断是不是上午或下午(中午和晚上不做操作)
																if(jc<=sw || jc>sw+zw+1 && jc<=sw+zw+xw){
																	mergeNum++;
																}else{
																	flag = false;
																}
															}else{
																flag = false;
															}
														}else{
															flag = false;
														}
													}
													//合并单元格
													if(mergeNum > 0){
														//tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+mergeNum)).merge();
														
														wsheet.mergeCells(curColName, rowNum-1, curColName , rowNum+mergeNum-1);//跨行
													}
													//cell = tempSheet.openCell(curColName+rowNum);//当前单元格
													//rowNum += mergeNum;
													
													
													if(!"".equalsIgnoreCase(tempSkjhmxbh)){
														tempVec = parseCourseInfo(exportType, tempCourseName, tempTeaName, tempSiteName, tempSkzc, tempClassName, oddVec, evenVec);
														cellContent = MyTools.StrFiltr(tempVec.get(0));
//														if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1))) > maxWidth){
//															maxWidth = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1)));
//														}
//														if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2))) > maxHeight){
//															maxHeight = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2)));
//														}
														
													}else{
														cellContent = "";
													}
													//cell.setValue(cellContent+"\n");
													content = new Label(curColName, rowNum-1, cellContent+"\n", writableCellFormat);
													//System.out.println(curColName+"=="+(rowNum-1)+":"+cellContent);
													wsheet.addCell(content);
													//rowNum += mergeNum;
												}else{
													mergeNum = 0;
													flag = true;
													
													//判断单元格是否可以合并
													while(flag){
														//教师课表空单元格合并
														jc = MyTools.StringToInt(tempJc)+1;
														
														//判断是不是上午或下午(中午和晚上不做操作)
														if(jc<=sw || jc>sw+zw+1 && jc<=sw+zw+xw){
															tempJc = jc<10?"0"+jc:""+jc;
															tempOrder = tempXq+tempJc;
															//判断是否有课
															if(curKcbVec.indexOf(tempOrder) < 0){
																mergeNum++;
															}else{
																flag = false;
															}
														}else{
															flag = false;
														}
													}
													//合并单元格
													if(mergeNum > 0){
														//tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+mergeNum)).merge();
														
														wsheet.mergeCells(curColName, rowNum-1, curColName , rowNum+mergeNum-1);//跨行
													}
													//rowNum += mergeNum;
												}
											}
										}
									}
								}
							}
						}
						
						//写入数据   
						wbook.write();   
						//关闭文件   
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
					
					this.setMSG("文件生成成功");
				}else{
					this.setMSG("没有符合条件的成绩信息");
				}
					return savePath;
			}
			
			
			/**
			 * 全校教师总表导出
			 * @date:2017-11-10
			 * @author:zhaixuchao
			 * @param xnxqbm 学年学期编码
			 * @param exportType 导出课表类型
			 * @param code 班级/教师编号
			 * @param timetableName 课程名称
			 * @throws SQLException
			 */
			public String ExportExceldaochualltea(String sAuth,String userCode,String xnxqbm,String exportType,String code,String timetableName) throws SQLException, UnsupportedEncodingException{
				//request.setCharacterEncoding("UTF-8"); //设置字符集
				DBSource db = new DBSource(request); //数据库对象
				String sql = "";
				Vector timeVec = null;
				Vector vec = new Vector();
				Vector hbSetVec = new Vector();
				Vector allKcbVec = new Vector();
				Vector curKcbVec = new Vector();
				String schoolName = MyTools.getProp(request, "Base.schoolName");
				
				final String weekNameArray[] = {"一","二","三","四","五","六","日"};
				final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
				final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
				Vector oddVec = new Vector();//单周周次
				Vector evenVec = new Vector();//双周周次
				String xnxqmc = "";//学年学期名称
				int mzts = 0;//每周天数
				int sw = 0;//上午节数
				int zw = 0;//中午节数
				int xw = 0;//下午节数
				int ws = 0;//晚上节数
				int zjs = 0;//总节数
				String timeArray[] = new String[0];
				int maxWidth = 0;
				int maxHeight = 50;
				float fontSize = 0;
				int curColName = 0;
				
				//Workbook wb = new Workbook();
				//Cell cell;
				String cellContent = ""; //当前单元格的内容
				int weekIndex = 0;
				int jsIndex = 1;
				int tempJsIndex = 1;
				int tempZwjsIndex = 0;
				Vector tempVec = new Vector();
				
				String tempXq = "";
				String tempJc = "";
				int tempIndex = -1;
				int mergeNum = 0;//单元格合并数
				boolean flag = true;//用于判断是否可合并
				int jc = 0;
				
				String xzbdm = "";
				String xzbmc = "";
				String sjxl = "";
				String skjhmxbh = "";
				String kcbh = "";
				String kcmc = "";
				String hbClass = "";
				String skjsbh = "";
				String skjsxm = "";
				String cdbh = "";
				String cdmc = "";
				String skzc = "";
				String tempSkjhmxbh = "";//授课计划明细编号
				String tempOrder = "";//时间序列
				String tempCourseName = "";//课程名称
				String tempClassCode = "";//班级编号
				String tempClassName = "";//班级名称
				String tempTeaCode = "";//教师编号
				String tempTeaName = "";//教师姓名
				String tempSiteCode = "";//场地编号
				String tempSiteName = "";//场地名称
				String tempSkzc = "";//授课周次
				
				String savePath = "";
				
				String skjhmxbhArray[] = new String[0];
				String captionName = "";
				
				Vector classInfoVec = null;
				Vector teaVec = null;
				
				String admin = MyTools.getProp(request, "Base.admin");//管理员
				String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
				String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
				String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
				String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
				String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
				String bzr = MyTools.getProp(request, "Base.bzr");//班主任
				
				//获取当前学年学期的上课时间相关信息
				sql = "select distinct (select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as 学期周次," +
					"t1.学年学期名称,t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
					"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间 " +
					"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				timeVec = db.GetContextVector(sql);
				if(timeVec!=null && timeVec.size()>0){
					int xqzc = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(0)));
					for(int i=1; i<xqzc+1; i+=2){
						oddVec.add(i);
					}
					for(int i=2; i<xqzc+1; i+=2){
						evenVec.add(i);
					}
					
					xnxqmc = MyTools.StrFiltr(timeVec.get(1));
					mzts = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(2)));
					sw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(3)));
					zw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(4)));
					xw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(5)));
					ws = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(6)));
					zjs = sw+zw+xw+ws;
					timeArray = MyTools.StrFiltr(timeVec.get(7)).split(",", -1);
				}
				
				//获取当前专业所有班级的课表信息
				if("allClassKcb".equalsIgnoreCase(exportType)){
					String tempNum = "";
					String tempRemark = "";
					String tempHbClass = "";
					String hbSetInfo = "";
					classInfoVec = null;
					Vector courseVec = new Vector();
					
					//查询相关行政班信息
//					sql = "select a.行政班代码,a.行政班名称,(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数,b.备注 from V_学校班级_数据子类 a " +
//						"left join V_排课管理_课程表主表 b on b.行政班代码=a.行政班代码 " +
//						"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//						"and a.专业代码='" + MyTools.fixSql(code) + "' order by a.行政班代码";
					sql = "select a.班级编号,a.班级名称," +
						//"(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数," +
						"a.总人数,c.教室名称,isnull(b.备注,'') " +
						//"from V_学校班级_数据子类 a " +
						"from V_基础信息_班级信息表 a " +
						"left join V_排课管理_课程表主表 b on b.行政班代码=a.班级编号 and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
						"left join V_教室数据类 c on c.教室编号=a.教室编号 " +
						"where a.系部代码='" + MyTools.fixSql(code) + "' " +
						"and cast(left(a.年级代码,2) as int) in (" +
						"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)-2," +
						"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)-1," +
						"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)) " +
						"order by a.班级编号";
					classInfoVec = db.GetContextVector(sql);
					
					if(classInfoVec!=null && classInfoVec.size()>0){
						//查询合班信息
//						sql = "select distinct b.时间序列,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from V_规则管理_授课计划明细表 a " +
//							"inner join V_排课管理_课程表明细详情表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//							"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//							"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//							"order by b.时间序列";
//						hbSetVec = db.GetContextVector(sql);
						
						//查询合班信息
						//20170125修改yeq 查询效率优化
						Vector classKcbVec = null;
						Vector classSkjhVec = null;
						Vector hbSkjhVec = null;
						tempVec = new Vector();
						hbSetVec = new Vector();
						
						sql = "select 时间序列,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 " +
							"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
						sql += " and 授课计划明细编号<>'' order by 时间序列";
						classKcbVec = db.GetContextVector(sql);
						
						if(classKcbVec!=null && classKcbVec.size()>0){
							sql = "select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
								"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
							classSkjhVec = db.GetContextVector(sql);
							
							if(classSkjhVec!=null && classSkjhVec.size()>0){
								for(int i=0; i<classKcbVec.size(); i+=3){
									tempSkjhmxbh = MyTools.StrFiltr(classKcbVec.get(i+2));
									
									for(int j=0; j<classSkjhVec.size(); j++){
										if(tempSkjhmxbh.indexOf(MyTools.StrFiltr(classSkjhVec.get(j))) > -1){
											tempVec.add(MyTools.StrFiltr(classKcbVec.get(i)));
											tempVec.add(MyTools.StrFiltr(classKcbVec.get(i+1)));
											tempVec.add(MyTools.StrFiltr(classSkjhVec.get(j)));
										}
									}
								}
								
								//查询所有合班设置信息
								sql = "select 授课计划明细编号 from V_规则管理_合班表";
								hbSkjhVec = db.GetContextVector(sql);
								
								if(hbSkjhVec!=null && hbSkjhVec.size()>0){
									for(int i=0; i<tempVec.size(); i+=3){
										tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(i+2));
										
										for(int j=0; j<hbSkjhVec.size(); j++){
											if(MyTools.StrFiltr(hbSkjhVec.get(j)).indexOf(tempSkjhmxbh) > -1){
												hbSetVec.add(tempVec.get(i));
												hbSetVec.add(tempVec.get(i+1));
												hbSetVec.add(tempVec.get(i+2));
												hbSetVec.add(MyTools.StrFiltr(hbSkjhVec.get(j)));
												break;
											}
										}
									}
								}
							}
						}
						
						//查询相关课程表信息
//						sql = "select distinct 行政班代码,时间序列,授课计划明细编号,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) as 授课周次 " +
//							"from V_排课管理_课程表周详情表 where 状态='1' and 课程类型 in ('01','02') and 授课计划明细编号<>'' " +
//							"and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//							"and 专业代码='" + MyTools.fixSql(code) + "' " +
//							"order by 行政班代码,时间序列,cast(授课周次 as int)";
//						sql = "select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//							"from V_排课管理_课程表周详情表 a " +
//							//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//							"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
//							"where a.状态='1' and a.授课计划明细编号<>'' " +
//							"and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//							"and b.系部代码='" + MyTools.fixSql(code) + "' " +
//							"order by a.行政班代码,a.时间序列,cast(a.授课周次 as int)";
						sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
							"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
							"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
							"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
							"from V_排课管理_课程表周详情表 a " + 
							"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
							"where a.状态='1' and a.课程类型 in ('01','02') and a.授课计划明细编号<>'' " +
							"and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
							"and b.系部代码='" + MyTools.fixSql(code) + "' " +
							") as t " + 
							"order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
						tempVec = db.GetContextVector(sql);
						
						if(tempVec!=null && tempVec.size()>0){
							vec = new Vector();
							flag = true;
							
							//拼接课程周次
							while(flag){
								xzbdm = MyTools.StrFiltr(tempVec.get(0));
								sjxl = MyTools.StrFiltr(tempVec.get(1));
								skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
								kcmc = MyTools.StrFiltr(tempVec.get(3));
								skjsbh = MyTools.StrFiltr(tempVec.get(4));
								skjsxm = MyTools.StrFiltr(tempVec.get(5));
								cdbh = MyTools.StrFiltr(tempVec.get(6));
								cdmc = MyTools.StrFiltr(tempVec.get(7));
								skzc = MyTools.StrFiltr(tempVec.get(8));
								for(int i=0; i<9; i++){
									tempVec.remove(0);
								}
								
								for(int i=0; i<tempVec.size(); i+=9){
									if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) 
										&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6)))){
										skzc += "#"+MyTools.StrFiltr(tempVec.get(i+8));
										for(int j=0; j<9; j++){
											tempVec.remove(i);
										}
										i -= 9;
									}
								}
								vec.add(xzbdm);
								vec.add(sjxl);
								vec.add(skjhmxbh);
								vec.add(kcmc);
								vec.add(skjsbh);
								vec.add(skjsxm);
								vec.add(cdbh);
								vec.add(cdmc);
								vec.add(skzc);
								
								if(tempVec.size() == 0){
									flag = false;
								}
							}
						}
						
						tempVec = new Vector();
						for(int i=0; i<vec.size(); i+=9){
							skjsbh = MyTools.StrFiltr(vec.get(i+4));
							skjsxm = MyTools.StrFiltr(vec.get(i+5));
							cdbh = MyTools.StrFiltr(vec.get(i+6));
							cdmc = MyTools.StrFiltr(vec.get(i+7));
							skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+8)), oddVec, evenVec);
							
							if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
								if(i > 0){
									tempVec.add(xzbdm);
									tempVec.add(sjxl);
									tempVec.add(skjhmxbh);
									tempVec.add(kcmc);
									tempVec.add(tempTeaCode);
									tempVec.add(tempTeaName);
									tempVec.add(tempSiteCode);
									tempVec.add(tempSiteName);
									tempVec.add(tempSkzc);
								}
								
								xzbdm = MyTools.StrFiltr(vec.get(i));
								sjxl = MyTools.StrFiltr(vec.get(i+1));
								skjhmxbh = MyTools.StrFiltr(vec.get(i+2));
								kcmc = MyTools.StrFiltr(vec.get(i+3));
								tempTeaCode = skjsbh;
								tempTeaName = skjsxm;
								tempSiteCode = cdbh;
								tempSiteName = cdmc;
								tempSkzc = skzc;
							}else{
								tempTeaCode += "&"+skjsbh;
								tempTeaName += "&"+skjsxm;
								tempSiteCode += "&"+cdbh;
								tempSiteName += "&"+cdmc;
								tempSkzc += "&"+skzc;
							}
						}
						tempVec.add(xzbdm);
						tempVec.add(sjxl);
						tempVec.add(skjhmxbh);
						tempVec.add(kcmc);
						tempVec.add(tempTeaCode);
						tempVec.add(tempTeaName);
						tempVec.add(tempSiteCode);
						tempVec.add(tempSiteName);
						tempVec.add(tempSkzc);
						
						for(int i=0; i<tempVec.size(); i+=9){
							skjhmxbh = MyTools.StrFiltr(tempVec.get(i+2));
							kcmc = MyTools.StrFiltr(tempVec.get(i+3));
							skjsbh = MyTools.StrFiltr(tempVec.get(i+4));
							skjsxm = MyTools.StrFiltr(tempVec.get(i+5));
							cdbh = MyTools.StrFiltr(tempVec.get(i+6));
							cdmc = MyTools.StrFiltr(tempVec.get(i+7));
							skzc = MyTools.StrFiltr(tempVec.get(i+8));
							
							//检查合班信息
							hbClass = "";
							for(int j=0; j<hbSetVec.size(); j+=4){
								if(MyTools.StrFiltr(tempVec.get(i+1)).equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(j)))){
									hbSetInfo = MyTools.StrFiltr(hbSetVec.get(j+3));
									
									if(!skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(j+2))) && hbSetInfo.indexOf(skjhmxbh)>-1){
										hbClass += MyTools.StrFiltr(hbSetVec.get(j+1))+"、";
										break;
									}
								}
							}
							
							if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))){
								if(i > 0){
									courseVec.add(xzbdm);
									courseVec.add(sjxl);
									courseVec.add(tempSkjhmxbh);
									courseVec.add(tempCourseName);
									courseVec.add(tempHbClass);
									courseVec.add(tempTeaCode);
									courseVec.add(tempTeaName);
									courseVec.add(tempSiteCode);
									courseVec.add(tempSiteName);
									courseVec.add(tempSkzc);
								}
								
								xzbdm = MyTools.StrFiltr(tempVec.get(i));
								sjxl = MyTools.StrFiltr(tempVec.get(i+1));
								tempSkjhmxbh = skjhmxbh;
								tempCourseName = kcmc;
								tempHbClass = hbClass;
								tempTeaCode = skjsbh;
								tempTeaName = skjsxm;
								tempSiteCode = cdbh;
								tempSiteName = cdmc;
								tempSkzc = skzc;
							}else{
								tempSkjhmxbh += "｜"+skjhmxbh;
								tempCourseName += "｜"+kcmc;
								tempHbClass += "｜"+hbClass;
								tempTeaCode += "｜"+skjsbh;
								tempTeaName += "｜"+skjsxm;
								tempSiteCode += "｜"+cdbh;
								tempSiteName += "｜"+cdmc;
								tempSkzc += "｜"+skzc;
							}
						}
						courseVec.add(xzbdm);
						courseVec.add(sjxl);
						courseVec.add(tempSkjhmxbh);
						courseVec.add(tempCourseName);
						courseVec.add(tempHbClass);
						courseVec.add(tempTeaCode);
						courseVec.add(tempTeaName);
						courseVec.add(tempSiteCode);
						courseVec.add(tempSiteName);
						courseVec.add(tempSkzc);
						
						//添加体锻课  lupengfei 20170928			
						int tytag=0;//体育课标记
						int tdcs=0;//体锻课次数
						int zwyk=0;//中午是否有課
						
						//获取操场编号
						String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(code) + "' and [教室名称]='操场' ) ";
						Vector veccc=db.GetContextVector(sqlcc);
						
						//获取实际上课周数
						String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
						Vector vecskzs=db.GetContextVector(sqlskzs);
							
						//获取每周天数
						int mztsx=0;
						String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(xnxqbm) + "' ";
						Vector vecmzts=db.GetContextVector(sqlmzts);
						if(vecmzts!=null&&vecmzts.size()>0){
							mztsx=Integer.parseInt(vecmzts.get(0).toString());
						}
						
						//时间序列,授课计划明细编号,课程名称,授课教师姓名,实际场地名称,授课周次详情
						
						//循环班级
						for(int b=0;b<classInfoVec.size();b=b+5){
							tdcs=0;//体锻课次数		
							//判断哪天没体育课,中午添加体锻课
							String ts="";
							for(int i=1;i<=mztsx;i++){
								tytag=0;//体育课标记
								zwyk=0;//中午是否有課
								if(i<10){
									ts="0"+i;
								}else{
									ts=i+"";
								}
								for(int j=0;j<courseVec.size();j=j+10){ 
									if(courseVec.get(j).toString().equals(classInfoVec.get(b).toString())){//班级相同
										if(ts.equals(courseVec.get(j+1).toString().substring(0, 2))){//天数相同
											if(courseVec.get(j+3).toString().indexOf("体育")>-1){//有体育课
												tytag=1;
											}
										}
									}					
								}
								if(tytag==0){			
									for(int k=0;k<courseVec.size();k=k+10){
										if(courseVec.get(k).toString().equals(classInfoVec.get(b).toString())){//班级相同
											if((ts+"05").equals(courseVec.get(k+1).toString())){//中午有课
												zwyk=1;									
											}
										}		
									}
									if(zwyk==0){ 
										if(tdcs<2){
											//添加体锻课
											int existkc=0;//班级有课
											for(int t=0;t<courseVec.size();t=t+10){
												if(courseVec.get(t).toString().equals(classInfoVec.get(b).toString())){//班级相同
													existkc=1;
													break;
												}
											}
											if(existkc==1){
												//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
												courseVec.add(classInfoVec.get(b).toString());
												courseVec.add(ts+"05");//时间序列
												courseVec.add("SKJHMX_TD");//授课计划明细编号
												courseVec.add("体锻");//课程名称		
												courseVec.add("");//合班
												courseVec.add("");//授课教师编号
												courseVec.add("");//授课教师姓名
												courseVec.add("");//实际场地编号
												courseVec.add("");//实际场地名称
												courseVec.add("");//授课周次详情
												
												tdcs++;
											}							
										}
									}else{
										zwyk=0;
									}
								}
							}
						}

						
						
						flag = false;
						for(int i=0; i<classInfoVec.size(); i+=5){
							tempClassCode = MyTools.StrFiltr(classInfoVec.get(i));
							curKcbVec = new Vector();
							
							for(int j=1; j<mzts+1; j++){
								for(int k=1; k<zjs+1; k++){
									tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
									
									flag = false;
									for(int a=0; a<courseVec.size(); a+=10){
										if(tempClassCode.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a))) && tempOrder.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a+1)))){
											curKcbVec.add(tempOrder);//时间序列
											curKcbVec.add(courseVec.get(a+2));//授课计划明细编号
											curKcbVec.add(courseVec.get(a+3));//课程名称
											curKcbVec.add(courseVec.get(a+4));//合班班级
											curKcbVec.add(courseVec.get(a+5));//授课教师编号
											curKcbVec.add(courseVec.get(a+6));//授课教师姓名
											curKcbVec.add(courseVec.get(a+7));//场地编号
											curKcbVec.add(courseVec.get(a+8));//场地名称
											curKcbVec.add(courseVec.get(a+9));//授课周次
											flag = true;
											break;
										}
									}
									if(flag == false){
										curKcbVec.add(tempOrder);
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
									}
								}
							}
							allKcbVec.add(classInfoVec.get(i+1));
							allKcbVec.add(classInfoVec.get(i+2));
							allKcbVec.add(classInfoVec.get(i+3));
							allKcbVec.add(classInfoVec.get(i+4));
							allKcbVec.add(curKcbVec);
						}
					}
				}
				
				//获取当前层级所有教师的课表信息
				if("allTeaKcb".equalsIgnoreCase(exportType)){
					teaVec = null;
					Vector xxkVec = null;
					String hbInfo = "";
					
					//获取教师名单
//					sql = "with cte1 as (select t1.工号,t1.姓名 from V_教职工基本数据子类 t1 " +
//						"inner join V_USER_AUTH t2 on t2.UserCode=t1.工号 " +
//						"inner join V_权限层级关系表 t3 on t3.权限编号=t2.AuthCode " +
//						"inner join V_层级表 t4 on t4.层级编号=t3.层级编号 where t4.层级编号='" + MyTools.fixSql(code) + "') " +
//						"select distinct * from (select a.工号,a.姓名 from cte1 a " +
//						"inner join (select distinct 专业代码,授课教师编号 from V_排课管理_课程表周详情表 " +
//						"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 课程代码<>'') b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' " +
//						"union all " +
//						"select a.工号,a.姓名 from cte1 a " +
//						"inner join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%'+a.工号+'%' " +
//						"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as t where 工号 is not null order by 姓名";
					sql = "select distinct * from (" +
						"select 工号,姓名 from V_教职工基本数据子类 where 工号='" + MyTools.fixSql(userCode) + "' " +
						"union all " +
						"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
						"inner join (select distinct t1.授课教师编号 from V_排课管理_课程表周详情表 t1 " +
						//"left join V_学校班级_数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
						"left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 " +
						"where t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and t1.课程代码<>''";
					//权限判断
					if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
						sql += " and (1=2";
						//班主任
						if(sAuth.indexOf(bzr) > -1){
							sql += " or t2.班主任工号='" + MyTools.fixSql(userCode) + "'";
						}
						//系部教务人员
						if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
							sql += " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
						}
						sql += ")";
					}
					sql += ") b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' ";
					
					//判断如果是管理员或全校教务权限，需查询选修课教师信息
					if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1){
						sql += "union all " +
							"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
							"inner join (select 授课教师编号 from V_规则管理_选修课授课计划主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') b " +
							"on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%'";
					}
					sql += ") as t where 工号 is not null order by 姓名";
					teaVec = db.GetContextVector(sql);
					
					if(teaVec!=null && teaVec.size()>0){
						//获取选修课信息
						sql = "select c.时间序列,a.授课计划明细编号,b.课程名称,a.选修班名称,a.授课教师编号,c.实际场地编号,c.实际场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a " +
							"inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
							"left join V_排课管理_选修课课程表信息表 c on c.授课计划明细编号=a.授课计划明细编号 " +
							"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
							"order by c.时间序列,cast((case substring(a.授课周次, 2, 1) when '｜' then substring(a.授课周次, 1, 1) when '&' then substring(a.授课周次, 1, 1) when '#' then substring(a.授课周次, 1, 1) " +
							"when '-' then substring(a.授课周次, 1, 1) when 'd' then '1' when 'v' then '2' else substring(a.授课周次, 1, 2) end) as int)";
						xxkVec = db.GetContextVector(sql);
						
						//获取合班设置信息
						sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a " +
							"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
							"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
							"where c.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
						hbSetVec = db.GetContextVector(sql);
						
						//获取教师课程信息
//						sql = "select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//							"from V_排课管理_课程表周详情表 a where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and (";
//						for(int i=0; i<teaVec.size(); i+=2){
//							sql += "'@'+replace(replace(授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
//						}
//						sql = sql.substring(0, sql.length()-4);
//						sql += ") order by a.时间序列,cast(a.授课周次 as int)";
						sql = "select t.时间序列,t.授课计划明细编号,t.行政班代码,t.行政班名称,t.课程代码,t.课程名称,t.授课教师编号,t.场地编号,t.场地名称,t.授课周次 " + 
							"from (select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
							"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
							"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
							"from V_排课管理_课程表周详情表 a " + 
							"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and (";
						for(int i=0; i<teaVec.size(); i+=2){
							sql += "'@'+replace(replace(授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
						}
						sql = sql.substring(0, sql.length()-4);
						sql += ")) as t order by t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
						tempVec = db.GetContextVector(sql);
						
						if(tempVec!=null && tempVec.size()>0){
							vec = new Vector();
							flag = true;
							
							//拼接同一课程周次
							while(flag){
								sjxl = MyTools.StrFiltr(tempVec.get(0));
								skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
								xzbdm = MyTools.StrFiltr(tempVec.get(2));
								xzbmc = MyTools.StrFiltr(tempVec.get(3));
								kcbh = MyTools.StrFiltr(tempVec.get(4));
								kcmc = MyTools.StrFiltr(tempVec.get(5));
								skjsbh = MyTools.StrFiltr(tempVec.get(6));
								cdbh = MyTools.StrFiltr(tempVec.get(7));
								cdmc = MyTools.StrFiltr(tempVec.get(8));
								skzc = MyTools.StrFiltr(tempVec.get(9));
								for(int i=0; i<10; i++){
									tempVec.remove(0);
								}
								
								for(int i=0; i<tempVec.size(); i+=10){
									if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))
										&& xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) 
										&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+7)))){
										skzc += "#"+MyTools.StrFiltr(tempVec.get(i+9));
										for(int j=0; j<10; j++){
											tempVec.remove(i);
										}
										i -= 10;
									}
								}
								vec.add(sjxl);
								vec.add(skjhmxbh);
								vec.add(xzbmc);
								vec.add(kcmc);
								vec.add(skjsbh);
								vec.add(cdbh);
								vec.add(cdmc);
								vec.add(skzc);
								
								if(tempVec.size() == 0){
									flag = false;
								}
							}
							tempVec = new Vector();
							for(int i=0; i<vec.size(); i+=8){
								cdmc = MyTools.StrFiltr(vec.get(i+6));
								skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+7)), oddVec, evenVec);
								
								if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) 
									|| !skjsbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) || !cdbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
									if(i > 0){
										tempVec.add(sjxl);
										tempVec.add(skjhmxbh);
										tempVec.add(xzbmc);
										tempVec.add(kcmc);
										tempVec.add(tempTeaCode);
										tempVec.add(tempSiteCode);
										tempVec.add(tempSiteName);
										tempVec.add(tempSkzc);
									}
									
									sjxl = MyTools.StrFiltr(vec.get(i));
									skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
									xzbmc = MyTools.StrFiltr(vec.get(i+2));
									kcmc = MyTools.StrFiltr(vec.get(i+3));
									skjsbh = MyTools.StrFiltr(vec.get(i+4));
									cdbh = MyTools.StrFiltr(vec.get(i+5));
									tempTeaCode = skjsbh;
									tempSiteCode = cdbh;
									tempSiteName = cdmc;
									tempSkzc = skzc;
								}else{
									tempTeaCode += "&"+MyTools.StrFiltr(vec.get(i+4));
									tempSiteCode += "&"+cdbh;
									tempSiteName += "&"+cdmc;
									tempSkzc += "&"+skzc;
								}
							}
							tempVec.add(sjxl);
							tempVec.add(skjhmxbh);
							tempVec.add(xzbmc);
							tempVec.add(kcmc);
							tempVec.add(tempTeaCode);
							tempVec.add(tempSiteCode);
							tempVec.add(tempSiteName);
							tempVec.add(tempSkzc);
							
							//合并合班课程信息
							for(int i=0; i<tempVec.size(); i+=8){
								sjxl = MyTools.StrFiltr(tempVec.get(i));
								skjhmxbh = MyTools.StrFiltr(tempVec.get(i+1));
								
								for(int j=0; j<hbSetVec.size(); j++){
									hbInfo = MyTools.StrFiltr(hbSetVec.get(j));
									
									if(hbInfo.indexOf(skjhmxbh) > -1){
										for(int k=(i+8); k<tempVec.size(); k+=8){
											if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(k)))){
												break;
											}
											
											if(hbInfo.indexOf(MyTools.StrFiltr(tempVec.get(k+1))) > -1){
												tempVec.set(i+2, MyTools.StrFiltr(tempVec.get(i+2))+"、"+MyTools.StrFiltr(tempVec.get(k+2)));
												
												for(int a=0; a<8; a++){
													tempVec.remove(k);
												}
												k -= 8;
											}
										}
									}
								}
							}
						}
						
						for(int i=0; i<teaVec.size(); i+=2){
							tempTeaCode = MyTools.StrFiltr(teaVec.get(i));
							curKcbVec = new Vector();
							
							for(int j=1; j<mzts+1; j++){
								for(int k=1; k<zjs+1; k++){
									tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
									tempSkjhmxbh = "";
									tempClassName = "";
									tempCourseName = "";
									tempTeaName = "";
									tempSiteCode = "";
									tempSiteName = "";
									tempSkzc = "";
									
									//添加普通课程信息
									flag = false;
									for(int a=0; a<tempVec.size(); a+=8){
										if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(a)))){
											break;
										}
										
										if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a))) && MyTools.StrFiltr(tempVec.get(a+4)).indexOf(tempTeaCode)>-1){
											tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(a+1))+"｜";
											tempClassName += MyTools.StrFiltr(tempVec.get(a+2))+"｜";
											tempCourseName += MyTools.StrFiltr(tempVec.get(a+3))+"｜";
											tempTeaName += "｜";
											tempSiteCode += MyTools.StrFiltr(tempVec.get(a+5))+"｜";
											tempSiteName += MyTools.StrFiltr(tempVec.get(a+6))+"｜";
											tempSkzc += MyTools.StrFiltr(tempVec.get(a+7))+"｜";
											flag = true;
										}
									}
									//添加选修课信息
									for(int a=0; a<xxkVec.size(); a+=8){
										if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(xxkVec.get(a))) && MyTools.StrFiltr(xxkVec.get(a+4)).indexOf(tempTeaCode)>-1){
											tempSkjhmxbh += MyTools.StrFiltr(xxkVec.get(a+1))+"｜";
											tempCourseName += MyTools.StrFiltr(xxkVec.get(a+2))+"｜";
											tempTeaName += "｜";
											tempClassName += MyTools.StrFiltr(xxkVec.get(a+3))+"｜";
											tempSiteCode += MyTools.StrFiltr(xxkVec.get(a+5))+"｜";
											tempSiteName += MyTools.StrFiltr(xxkVec.get(a+6))+"｜";
											tempSkzc += MyTools.StrFiltr(xxkVec.get(a+7))+"｜";
											flag = true;
											
											if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(xxkVec.get(a)))){
												break;
											}
										}
									}
									if(flag == false){
										curKcbVec.add(tempOrder);
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
										curKcbVec.add("");
									}else{
										curKcbVec.add(tempOrder);
										curKcbVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
										curKcbVec.add(tempCourseName.substring(0, tempCourseName.length()-1));
										curKcbVec.add(tempTeaName.substring(0, tempTeaName.length()-1));
										curKcbVec.add("");
										curKcbVec.add(tempClassName.substring(0, tempClassName.length()-1));
										curKcbVec.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
										curKcbVec.add(tempSiteName.substring(0, tempSiteName.length()-1));
										curKcbVec.add(tempSkzc.substring(0, tempSkzc.length()-1));
									}
								}
							}
							
							allKcbVec.add(MyTools.StrFiltr(teaVec.get(i+1)));
							allKcbVec.add("");
							allKcbVec.add("");
							allKcbVec.add("");
							allKcbVec.add(curKcbVec);
						}
					}else{
						allKcbVec.add("没有相关教师信息");
						allKcbVec.add("");
						allKcbVec.add("");
						allKcbVec.add("");
						allKcbVec.add(curKcbVec);
					}
				}
				
				//整理数据
				if(allKcbVec!=null && allKcbVec.size()>0){
					Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
					int year = c.get(Calendar.YEAR); 
					int month = c.get(Calendar.MONTH); 
					int date = c.get(Calendar.DATE);
					
					savePath = MyTools.getProp(request, "Base.exportExcelPath");
					
					int totalColName = 0;
					int totalNum = allKcbVec.size()/5+4;
					//声明工作簿jxl.write.WritableWorkbook  
					//创建
					try {
						File file = new File(savePath);
						if(!file.exists()){
							file.mkdirs();
						}
						
						if("allClassKcb".equalsIgnoreCase(exportType))
							captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 全日制课表";
						else if("allTeaKcb".equalsIgnoreCase(exportType))
							captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + "授课计划表";
						
						
						savePath += "/" + captionName + year + ((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
						OutputStream os = new FileOutputStream(savePath);
						
					//翟旭超2017/11/03调用模板  begin
						//converter.convert(docFile, new File(request.getSession().getServletContext().getRealPath("/")+"\\mobile\\"+pdfFile));
						
						jxl.Workbook wb=null;
						String templePath="";
						// 读取模板文件
						 //String templePath = "C:\\Users\\dell\\Desktop\\allClassTemplate.xls";
						if("allClassKcb".equalsIgnoreCase(exportType)) {
							templePath = request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\allClassTemplate.xls";
						}
						if("allTeaKcb".equalsIgnoreCase(exportType)) {
							templePath = request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\allTeaTemplate.xls";
						}
						//String templePath = request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\allClassTemplate.xls";
				        try {
							wb = jxl.Workbook.getWorkbook(new File(templePath));
						} catch (BiffException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        WritableWorkbook wbook = jxl.Workbook.createWorkbook(os, wb);
				        WritableSheet wsheet = wbook.getSheet(0);
				        SheetSettings sheetSettings= wsheet.getSettings();
				        sheetSettings.setScaleFactor(100);
					//翟旭超2017/11/03调用模板  end
				    
				        
				    //插入图片,由于只支持png格式,需要转换成png.  翟旭超2017/11/03导入图片 begin
					        // 读入图片  
				        	//File imgFile = new File("C:\\Users\\dell\\Desktop\\无标题2.png");
				        	File imgFile = new File( request.getSession().getServletContext().getRealPath("/") + "\\form\\timetableQuery\\qxjszb.png");
					        BufferedImage picImage = ImageIO.read(imgFile);  
					        // 取得图片的像素高度，宽度  
					           
					        //生成一个图片对象。  
					        WritableImage image = new WritableImage(0, 1, 4, 3, imgFile);  
					        // 把图片插入到sheet  
					        wsheet.addImage(image);
					//翟旭超2017/11/03导入图片 end
				        
						
						//WritableWorkbook wbook = jxl.Workbook.createWorkbook(os);//建立excel文件
						//WritableSheet wsheet = wbook.createSheet("Sheet1", 0);//工作表名称
						WritableFont fontStyle;
						WritableCellFormat contentStyle = null;
						Label content;
						
						
						int sum=0;
						
						if(totalNum > colName.length){
							if(totalNum%colName.length == 0){
								//totalColName = colName[(totalNum-1)/colName.length-1]+colName[colName.length-1];
								
								totalColName = (totalNum-1)/colName.length-1 + colName.length-1;
							}else{
								//totalColName = colName[totalNum/colName.length-1]+colName[totalNum%colName.length-1];
								
								totalColName = totalNum/colName.length-1 + totalNum%colName.length-1;
							}
						}else{
							totalColName = totalNum-1;
						}
						for(int colNum=1; colNum<allKcbVec.size()/5+5; colNum++){
							if(colNum > 4){
								curKcbVec = (Vector)allKcbVec.get((colNum-5)*5+4);
							}
							
							/*if(colNum > colName.length){
								if(colNum%colName.length == 0){
									//curColName = colName[(colNum-1)/colName.length-1]+colName[colName.length-1];
									
									curColName = (colNum-1)/colName.length-1 + colName.length-1;
									System.out.println("first:"+curColName);
								}else{
									//curColName = colName[colNum/colName.length-1]+colName[colNum%colName.length-1];
									
									curColName = colNum/colName.length-1 + colNum%colName.length-1;
									System.out.println("second:"+curColName);
								}
							}else{
								curColName = colNum-1;
								System.out.println("third:"+curColName);
							}*/
							curColName = colNum-1;
							
							for(int rowNum=1; rowNum<zjs*mzts+6; rowNum++){
								if(jsIndex > zjs){
									jsIndex = 1;
									tempJsIndex = 1;
									tempZwjsIndex = 0;
								}
								
								//设置列宽
								for(int j=0;j<(teaVec.size()/2)+4;j++){
									if(j==0 || j==1 ){
										wsheet.setColumnView(j, 4);
									}else if(j==2 || j==3){
										wsheet.setColumnView(j, 7);
									}else{
										wsheet.setColumnView(j, 78);
										if(allKcbVec.size()/5 < 2){
											maxWidth = 154/(allKcbVec.size()/5);
										}else{
											maxWidth = 77;
										}
										wsheet.setColumnView(j, maxWidth);
									}
								}
								
								
								
								//生成标题
								if(colNum==1 && rowNum==1){
									//tempSheet.openTable(colName[0]+"1:"+totalColName+"1").merge();
									//cell = tempSheet.openCell("A1");
									
									
									if("allClassKcb".equalsIgnoreCase(exportType))
										cellContent = "                         " + schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 全日制课表";
									else if("allTeaKcb".equalsIgnoreCase(exportType))
										cellContent = "                            " + schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + "授课计划表";
									
									
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									//contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
									//contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									//contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									//contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
									
									wsheet.setRowView(rowNum-1, 30*20, false); //设置行高
									wsheet.setRowView(rowNum, 25*20, false); //设置行高
									
									wsheet.mergeCells(0, 0, totalColName , 0);//跨行
									
									content = new Label(0, 0, cellContent, contentStyle);
									wsheet.addCell(content);
									
									//maxWidth = cellContent.length()/(allKcbVec.size()/4);
									//cell.setValue(cellContent);
									
									//设置标题字体大小
									//cell = tempSheet.openCell("A1");
									//cell.getFont().setBold(true);
									//fontSize = 18;
									//cell.getFont().setSize(fontSize);
								}else{
									//备注
									if(rowNum==zjs*mzts+5){
										
									}
									//判断星期列
									else if(colNum==1 && rowNum>4){
										//tempSheet.openTable(colName[0]+rowNum+":"+colName[0]+(rowNum+zjs-1)).merge();
										//cell = tempSheet.openCell(colName[0]+rowNum);
										
										//判断是否为大周
										if(mzts > 7){
											cellContent = orderNameArray[weekIndex];
										}else{
											cellContent = weekNameArray[weekIndex];
											weekIndex++;
										}
										
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
										
										wsheet.mergeCells(0, rowNum-1, 0 , rowNum+zjs-1-1);//跨行
										content = new Label(0, rowNum-1, cellContent, contentStyle);
										wsheet.addCell(content);
										
										
										//cell.setValue(cellContent);
										rowNum += (zjs-1);
									}
									//节数列
									else if(colNum==2 && rowNum>4){
										//cell = tempSheet.openCell(colName[1]+rowNum);
										if(jsIndex>sw && jsIndex<=sw+zw){
											cellContent = "中"+orderNameArray[tempZwjsIndex];
											tempZwjsIndex++;
										}else{
											cellContent = tempJsIndex+"";
											tempJsIndex++;
										}
										jsIndex++;
										
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
										
										content = new Label(1, rowNum-1, cellContent, contentStyle);
										wsheet.addCell(content);
										
										//cell.setValue(cellContent);
									}
									//时间列
									else if(colNum==3 && rowNum>4){
										//tempSheet.openTable(colName[2]+rowNum+":"+colName[3]+rowNum).merge();
										//cell = tempSheet.openCell(colName[2]+rowNum);
										//cell.setValue(timeArray[jsIndex-1]);
										
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
										
										wsheet.mergeCells(2, rowNum-1, 3 , rowNum-1);//跨行
										content = new Label(2, rowNum-1, timeArray[jsIndex-1], contentStyle);
										wsheet.addCell(content);
										
										
										jsIndex++;
									}
									
									//课表内容
									else if(colNum>4 && rowNum>1){
										
										int pageHeight = 0;
										if("allClassKcb".equalsIgnoreCase(exportType))
											pageHeight = 500;
										if("allTeaKcb".equalsIgnoreCase(exportType))
											pageHeight = 600;
										
										maxHeight = pageHeight/(zjs*mzts);
										for(int hang=1;hang<(zjs*mzts)+4;hang++){
											if(hang==1||hang==2||hang==3) {
												wsheet.setRowView(hang, 15*20, false); //设置行高
											}else{
												wsheet.setRowView(hang, maxHeight*20, false); //设置行高
											}
										}
										
										if(allKcbVec!=null && allKcbVec.size()>0){
											
											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
											contentStyle = new WritableCellFormat(fontStyle);
											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM); 
											
										
											if(rowNum == 2){
												if("allTeaKcb".equalsIgnoreCase(exportType)){
													//tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+2)).merge();
													wsheet.mergeCells(curColName, rowNum-1, curColName , rowNum+2-1);//跨行
												}
													
												content = new Label(curColName, rowNum-1, MyTools.StrFiltr(allKcbVec.get((colNum-5)*5)), contentStyle);
												//System.out.println("curColName:"+curColName+",rowNum-1:"+(rowNum-1)+",rowNum+2-1:"+(rowNum+2-1));
												wsheet.addCell(content);
												//cell = tempSheet.openCell(curColName+rowNum);
												//cell.setValue(MyTools.StrFiltr(allKcbVec.get((colNum-5)*4)));
											}
											else if(rowNum == 3){
												//cell = tempSheet.openCell(curColName+rowNum);
												//cell.setValue(MyTools.StrFiltr(allKcbVec.get((colNum-5)*4+1)) + "人");
												
												content = new Label(curColName, rowNum-1, MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+1)) + "人", contentStyle);
												wsheet.addCell(content);
											}
											else if(rowNum == 4){
												//cell = tempSheet.openCell(curColName+rowNum);
												//cell.setValue("");
												
												content = new Label(curColName, rowNum-1, MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+2)), contentStyle);
												
												wsheet.setRowView(rowNum, maxHeight*20, false); //设置行高
												
												wsheet.addCell(content);
											}
											else{
												WritableFont writableFont = new WritableFont(WritableFont.createFont("宋体"),10, WritableFont.NO_BOLD, false);
												WritableCellFormat writableCellFormat = new WritableCellFormat(writableFont);
												writableCellFormat.setWrap(false);//设置自动换行
												writableCellFormat.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
												writableCellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
												writableCellFormat.setBorder(jxl.format.Border.TOP, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.LEFT, BorderLineStyle.MEDIUM);
												writableCellFormat.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.MEDIUM);
												
												
												tempXq = (rowNum-4)/zjs<10?"0"+((rowNum-4)/zjs+1):""+((rowNum-4)/zjs+1);
												tempJc = (rowNum-4)%zjs<10?"0"+(rowNum-4)%zjs:""+(rowNum-4)%zjs;
												if(MyTools.StringToInt(tempJc) == 0){
													tempJc = zjs<10?"0"+zjs:""+zjs;
													tempXq = MyTools.StrFiltr((MyTools.StringToInt(tempXq)-1));
													tempXq = MyTools.StringToInt(tempXq)<10?"0"+tempXq:tempXq;
												}
												tempOrder = tempXq+tempJc;
												tempIndex = curKcbVec.indexOf(tempOrder);
												
												if(tempIndex > -1){
													tempSkjhmxbh = MyTools.StrFiltr(curKcbVec.get(tempIndex+1));
													tempCourseName = MyTools.StrFiltr(curKcbVec.get(tempIndex+2));
													tempClassName = MyTools.StrFiltr(curKcbVec.get(tempIndex+3));
													tempTeaCode = MyTools.StrFiltr(curKcbVec.get(tempIndex+4));
													tempTeaName = MyTools.StrFiltr(curKcbVec.get(tempIndex+5));
													tempSiteCode = MyTools.StrFiltr(curKcbVec.get(tempIndex+6));
													tempSiteName = MyTools.StrFiltr(curKcbVec.get(tempIndex+7));
													tempSkzc = MyTools.StrFiltr(curKcbVec.get(tempIndex+8));
													
													mergeNum = 0;
													flag = true;
													
													//判断单元格是否可以合并
													while(flag){
														tempIndex += 9;
														if(tempIndex < curKcbVec.size()){
															//判断是否完全相同的课程
															if(tempXq.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex)).substring(0, 2))
																&& tempSkjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+1))) 
																&& tempTeaCode.equalsIgnoreCase( MyTools.StrFiltr(curKcbVec.get(tempIndex+4)))
																&& tempSiteCode.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+6)))
																&& tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+8)))
																){
																
																//教师课表空单元格合并
																jc = MyTools.StringToInt(MyTools.StrFiltr(curKcbVec.get(tempIndex)).substring(2));
																
																//判断是不是上午或下午(中午和晚上不做操作)
																if(jc<=sw || jc>sw+zw+1 && jc<=sw+zw+xw){
																	mergeNum++;
																}else{
																	flag = false;
																}
															}else{
																flag = false;
															}
														}else{
															flag = false;
														}
													}
													//合并单元格
													if(mergeNum > 0){
														//tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+mergeNum)).merge();
														wsheet.mergeCells(curColName, rowNum-1, curColName , rowNum+mergeNum-1);//跨行
													}
													//cell = tempSheet.openCell(curColName+rowNum);//当前单元格
													//rowNum += mergeNum;
													
													if(!"".equalsIgnoreCase(tempSkjhmxbh)){
														tempVec = parseCourseInfo(exportType, tempCourseName, tempTeaName, tempSiteName, tempSkzc, tempClassName, oddVec, evenVec);
														cellContent = MyTools.StrFiltr(tempVec.get(0));
//														if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1))) > maxWidth){
//															maxWidth = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1)));
//														}
//														if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2))) > maxHeight){
//															maxHeight = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2)));
//														}
														
													}else{
														cellContent = "";
													}
													//cell.setValue(cellContent+"\n");
													content = new Label(curColName, rowNum-1, cellContent+"\n", writableCellFormat);
													wsheet.addCell(content);
													rowNum += mergeNum;
												}else{
													mergeNum = 0;
													flag = true;
													
													//判断单元格是否可以合并
													while(flag){
														//教师课表空单元格合并
														jc = MyTools.StringToInt(tempJc)+1;
														
														//判断是不是上午或下午(中午和晚上不做操作)
														if(jc<=sw || jc>sw+zw+1 && jc<=sw+zw+xw){
															tempJc = jc<10?"0"+jc:""+jc;
															tempOrder = tempXq+tempJc;
															//判断是否有课
															if(curKcbVec.indexOf(tempOrder) < 0){
																mergeNum++;
															}else{
																flag = false;
															}
														}else{
															flag = false;
														}
													}
													//合并单元格
													if(mergeNum > 0){
														//tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+mergeNum)).merge();
														wsheet.mergeCells(curColName, rowNum-1, curColName , rowNum+mergeNum-1);//跨行
													}
													rowNum += mergeNum;
												}
											}
										}
									}
								}
							}
						}
						
						//写入数据   
						wbook.write();   
						//关闭文件   
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
					
					this.setMSG("文件生成成功");
				}else{
					this.setMSG("没有符合条件的成绩信息");
				}
					return savePath;
			}
	
	
	
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}
	
	public String getAuth() {
		return Auth;
	}

	public void setAuth(String auth) {
		Auth = auth;
	}

	public String getPK_KCBZBBH() {
		return PK_KCBZBBH;
	}

	public void setPK_KCBZBBH(String pK_KCBZBBH) {
		PK_KCBZBBH = pK_KCBZBBH;
	}

	public String getPK_XNXQBM() {
		return PK_XNXQBM;
	}

	public void setPK_XNXQBM(String pK_XNXQBM) {
		PK_XNXQBM = pK_XNXQBM;
	}

	public String getPK_XZBDM() {
		return PK_XZBDM;
	}

	public void setPK_XZBDM(String pK_XZBDM) {
		PK_XZBDM = pK_XZBDM;
	}

	public String getPK_ZYDM() {
		return PK_ZYDM;
	}

	public void setPK_ZYDM(String pK_ZYDM) {
		PK_ZYDM = pK_ZYDM;
	}

	public String getPK_SKJSBH() {
		return PK_SKJSBH;
	}

	public void setPK_SKJSBH(String pK_SKJSBH) {
		PK_SKJSBH = pK_SKJSBH;
	}

	public String getPK_ZT() {
		return PK_ZT;
	}

	public void setPK_ZT(String pK_ZT) {
		PK_ZT = pK_ZT;
	}

	public String getPK_KCBMXBH() {
		return PK_KCBMXBH;
	}

	public void setPK_KCBMXBH(String pK_KCBMXBH) {
		PK_KCBMXBH = pK_KCBMXBH;
	}

	public String getPK_SJXL() {
		return PK_SJXL;
	}

	public void setPK_SJXL(String pK_SJXL) {
		PK_SJXL = pK_SJXL;
	}

	public String getPK_LJXGBH() {
		return PK_LJXGBH;
	}

	public void setPK_LJXGBH(String pK_LJXGBH) {
		PK_LJXGBH = pK_LJXGBH;
	}

	public String getPK_SKJHMXBH() {
		return PK_SKJHMXBH;
	}

	public void setPK_SKJHMXBH(String pK_SKJHMXBH) {
		PK_SKJHMXBH = pK_SKJHMXBH;
	}
	
	public String getPK_BZ() {
		return PK_BZ;
	}

	public void setPK_BZ(String pK_BZ) {
		PK_BZ = pK_BZ;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getPK_CJBH() {
		return PK_CJBH;
	}

	public void setPK_CJBH(String pK_CJBH) {
		PK_CJBH = pK_CJBH;
	}
}
