package com.pantech.devolop.customExamManage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import net.sf.json.JSONArray;

import com.jspsmart.upload.SmartFiles;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;

public class CjdjBean {
	private String AUTHCODE;// 用户权限
	private String CJ_KSBH; //考试编号
	private String CJ_BH; //编号
	private String CJ_XH; //学号
	private String CJ_XM; //姓名
	private String CJ_KSXKBH; //考试学科编号
	private String CJ_KCDM; //课程代码
	private String CJ_BJDM; //班级代码
	private String CJ_CJ; //成绩
	private String CJ_CJR; //创建人
	private String CJ_CJSJ; //创建时间
	private String CJ_ZT; //状态
	
	private String CJ_XN = "";// 学年
	private String CJ_XQ = "";// 学期
	private String CJ_XNXQBM;// 学年学期编码
	
	private String USERCODE;
	private String listsql;// 查询sql语句

	// 以下变量为此类的固定变量
	protected HttpServletRequest request;
	private DBSource db;
	private String MSG;

	/**
	 * 初始化变量
	 * 
	 * @date:2016-7-13
	 * @author:翟旭超
	 */
	private void initialData() {
		AUTHCODE = "";// 用户权限
		CJ_KSBH = "";// 考试编号
		CJ_BH = "";// 编号
		CJ_XH = "";// 学号
		CJ_XM = "";// 姓名
		CJ_KSXKBH = "";// 考试学科编号
		CJ_KCDM = "";// 课程代码
		CJ_BJDM = "";// 班级代码
		CJ_CJ = "";// 成绩
		CJ_CJR = "";// 创建人
		CJ_CJSJ="";//创建时间
		CJ_ZT="";//状态
		
		USERCODE = "";// 用户编号
	}


	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public CjdjBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}

	/**
	 * 分页查询 考试信息列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String XN_CX, String XQ_CX, String KSMC_CX, String KSLB_CX ) throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select a.考试编号,a.考试名称,a.学年学期编码,c.学年学期名称,a.类别编号,b.类别名称," +
			"convert(nvarchar(10),a.登分开始时间,21) as 登分开始时间," +
			"convert(nvarchar(10),a.登分结束时间,21) as 登分结束时间," +
			"case when getDate() between 登分开始时间 and 登分结束时间 then 'true' else 'false' end as 登分状态 " +
			"from V_自设考试管理_考试信息表 as a " +
			"left join V_信息类别_类别操作 as b on a.类别编号=b.描述 " +
			"left join V_规则管理_学年学期表 as c on a.学年学期编码=c.学年学期编码 " +
			"where b.父类别代码='KSLBDM' and a.状态='1' and c.状态='1' ";
		// 判断查询条件
		if (!"".equalsIgnoreCase(XN_CX)) {
			sql += " and c.学年= '" + MyTools.fixSql(XN_CX) + "'";
		}
		if (!"".equalsIgnoreCase(XQ_CX)) {
			sql += " and c.学期= '" + MyTools.fixSql(XQ_CX)+ "'";
		}
		if (!"".equalsIgnoreCase(KSMC_CX)) {
			sql += " and a.考试名称  like '%" + MyTools.fixSql(KSMC_CX) + "%'";
		}
		if (!"".equalsIgnoreCase(KSLB_CX)) {
			sql += " and a.类别编号 = '" + MyTools.fixSql(KSLB_CX) + "'";
		}

		sql = sql + " ORDER BY a.考试编号 desc ";

		vec = db.getConttexJONSArr(sql, pageNum, page);// 执行sql语句，赋值给vec
		return vec;
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
		Vector vec = null;
		String sql="select '请选择' as comboName,'' as comboValue,0  as orderNum " +
				" union all" +
				" select distinct 学年 as comboName,学年 as comboValue,1" +
				" from V_规则管理_学年学期表" +
				" where 状态='1' order by orderNum,comboValue desc";
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学期下拉框
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct case when 学期='1' then '第一学期' else '第二学期' end as comboName,学期 as comboValue " +
				"from V_规则管理_学年学期表  where 状态='1' order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取当前学年学期
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		String curXnxq = "";
		Vector vec = null;
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		
		return curXnxq;
	}
	
	/**
	 * 读取考试类别下拉框
	 * @date:2017-06-20
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadLbCombo() throws SQLException{
		Vector vec = null;
		
		String sql="select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 类别名称 , 描述  from  V_信息类别_类别操作 where 父类别代码='KSLBDM' " +
				"order by comboValue";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询所选考试所有相关班级课程信息
	 * @date:2017-06-30
	 * @author:zhaixuchao
	 * @return 班级信息
	 * @throws SQLException
	 */
	public Vector loadExamClassTree(String level, String parentCode)throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		//获取班级类型目录
		if("0".equalsIgnoreCase(level)){
			String result = "[";
			
			//判断是否有需要显示的行政班
			sql = "select COUNT(distinct a.班级代码) from V_自设考试管理_考试学科信息表 as a " +
				"left join V_学校班级数据子类 as b on a.班级代码=b.行政班代码 " +
				"where a.状态='1' and b.状态='1' and a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' ";
			/*if(this.getAUTHCODE().indexOf(aa) < 0){
				sql += " and a.班级代码 in (" +
						"select distinct a.班级代码 " +
						"from V_自设考试管理_考试学科信息表 as a " +
						"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码 " +
						"left join V_规则管理_授课计划明细表 as c on b.授课计划主表编号=b.授课计划主表编号 " +
						"where a.状态='1' and a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' "+
						" and c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%')";
			}*/
			
			//权限判断
			/*if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(jyzzz)<0){
				sql += " and a.班级代码 in (" +
						"select distinct a.班级代码 " +
						"from V_自设考试管理_考试学科信息表 as a " +
						"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码 " +
						"left join V_规则管理_授课计划明细表 as c on c.授课计划主表编号=b.授课计划主表编号 " +
						"left join V_学校班级数据子类 as d on d.行政班代码=a.班级代码 " +
						"left join V_基础信息_系部教师信息表 as e on e.系部代码=d.系部代码 " +
						"where a.状态='1' and b.状态='1' and c.状态='1' and d.状态='1' and a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' ";
				//班主任
				//if(this.getAUTHCODE().indexOf(admin) > -1){
					sql += " and c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				//}
				//系部教务人员
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					//if(this.getAUTHCODE().indexOf(admin) > -1){
						sql += " or ";
					//}
					sql += " e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}*/
			
			//9/21改权限
			if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
				sql += " and a.班级代码 in (" +
					"select distinct a.班级代码 " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_学校班级数据子类 as b on b.行政班代码=a.班级代码 " +
					"left join V_课程数据子类 c  on a.课程代码 = c.课程号 "+
					"left join V_排课管理_课程表明细详情表 d  on b.行政班代码 = d.行政班代码 and c.课程号 = d.课程代码  " +
					"left join V_基础信息_教研组信息表 as e on a.课程代码=e.学科代码  " +
					"left join V_基础信息_系部教师信息表 as f on f.系部代码=b.系部代码  " +
					"left join (select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 from V_规则管理_授课计划主表 as t1 "+
					"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as h on h.行政班代码=a.班级代码 and h.课程代码=a.课程代码 "+
					"where a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' " +
					"and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or h.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
						
					if(this.getAUTHCODE().indexOf(jyzzz)>-1){
						sql+=" or e.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
					}
					
					//班主任
					if(this.getAUTHCODE().indexOf(bzr) > -1){
						sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
					}
					if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
						sql += " or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
					}
					sql += ")";
				sql += ")";
			}
			vec = db.GetContextVector(sql);
			
			if(MyTools.StringToInt(MyTools.StrFiltr(vec.get(0))) > 0){
				result += "{\"id\":\"xzb\",\"text\":\"行政班\",\"state\":\"closed\"},";
			}
			if(result.length() > 1){
				result = result.substring(0, result.length()-1);
			}else{
				result += "{\"id\":\"noClass\",\"text\":\"没有班级信息\",\"state\":\"open\"}";
			}
			result += "]";
			vec.clear();
			vec.add(result);
		}
		
		//获得班级子节点
		if("1".equalsIgnoreCase(level)){
			sql = "select distinct a.班级代码 as id,b.行政班名称 as text,b.年级代码,state='open' " +
				"from V_自设考试管理_考试学科信息表 as a " +
				"left join V_学校班级数据子类 as b on a.班级代码=b.行政班代码 " +
				"where a.状态='1' and b.状态='1' and a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' ";
			
			/*if(this.getAUTHCODE().indexOf(aa) < 0){
				sql += " and a.班级代码 in (" +
						"select distinct a.班级代码 " +
						"from V_自设考试管理_考试学科信息表 as a " +
						"left join V_规则管理_授课计划主表 as b on a.班级代码=b.行政班代码 " +
						"left join V_规则管理_授课计划明细表 as c on b.授课计划主表编号=b.授课计划主表编号 " +
						"where a.状态='1' and a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' "+
						" and c.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%')";
			}*/
			
			//权限判断
			if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
				sql += " and a.班级代码 in (" +
					"select distinct a.班级代码 " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_学校班级数据子类 as b on b.行政班代码=a.班级代码 " +
					"left join V_课程数据子类 c  on a.课程代码 = c.课程号 "+
					"left join V_排课管理_课程表明细详情表 d  on b.行政班代码 = d.行政班代码 and c.课程号 = d.课程代码  " +
					"left join V_基础信息_教研组信息表 as e on a.课程代码=e.学科代码  " +
					"left join V_基础信息_系部教师信息表 as f on f.系部代码=b.系部代码  " +
					"left join (select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 from V_规则管理_授课计划主表 as t1 "+
					"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as h on h.行政班代码=a.班级代码 and h.课程代码=a.课程代码 "+
					"where a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' " +
					"and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or h.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				if(this.getAUTHCODE().indexOf(jyzzz)>-1){
					sql+=" or e.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
				}
				//班主任
				if(this.getAUTHCODE().indexOf(bzr) > -1){
					sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
				}
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					sql += " or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += "))";
			}
			sql += " order by b.年级代码";
			vec = db.getConttexJONSArr(sql, 0, 0);
		}
		return vec;
	}
	
	/**
	 * 查询考试班级信息
	 * @date:2017-06-30
	 * @author:zhaixuchao
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadExamInfo(String dfFlag)throws SQLException{
		System.out.println("loadexaminfo进入");
		String sql = "";
		Vector vec = null;
		Vector resultVec = new Vector();
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		String stuState = MyTools.getProp(request, "Base.stuState");
		String colInfo = "[";
		String scoreInfo = "[";
		String aa=admin.substring(1,3);
		
		//Vector fhtjxsVec = new Vector();//符合条件学生
		//Vector bjxsVec = new Vector();//班级学生
		
		Vector zslb = new Vector();//招生类别
		Vector zslbVec = new Vector();//招生类别
		
		//读取列名信息
		/*sql="select distinct a.课程代码,b.课程名称 " +
				"from V_自设考试管理_考试学科信息表 as a " +
				"left join V_课程数据子类 as b on a.课程代码=b.课程号 " +
				"left join V_规则管理_授课计划主表 as c on a.班级代码=c.行政班代码 " +
				"left join V_规则管理_授课计划明细表 as d on c.授课计划主表编号=d.授课计划主表编号 and b.课程号=d.课程代码  " +
				"left join V_基础信息_教研组信息表 as e on a.课程代码=e.学科代码 "+
				"where a.状态='1' and a.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' and a.班级代码='"+MyTools.fixSql(this.getCJ_BJDM())+"' ";
		
		//权限判断
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0 && this.getAUTHCODE().indexOf(xbjwgl)<0){
			if(this.getAUTHCODE().indexOf(jyzzz)>0){
				sql += " and c.状态='1' and d.状态='1' and e.状态='1' and e.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
			}else{
				sql += " and c.状态='1' and d.状态='1' and d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			}
		}
		
		sql += " order by a.课程代码";*/
		
		//==========================================9/21
		sql="select distinct a.课程代码,g.课程名称 " +
				"from V_自设考试管理_考试学科信息表 as a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
				"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
				
				"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 ) d on d.行政班代码=a.班级代码 and d.课程代码 like '%'+a.课程代码+'%' " +
				
				"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
				"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 ) e on e.行政班代码=a.班级代码 and e.课程代码=a.课程代码 " +

				"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  " +
				"left join V_课程数据子类 g on g.课程号=a.课程代码 " +
		
				"where a.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' and a.班级代码='"+MyTools.fixSql(this.getCJ_BJDM())+"' ";
		//2017921翟旭超添加权限判断代码
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0&&this.getAUTHCODE().indexOf(xbjwgl)<0){
			sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
		
			if(this.getAUTHCODE().indexOf(jyzzz)>-1){
				sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
			}
			//系部
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			sql+=" ) ";
		}
		sql += " order by a.课程代码";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=2){
				colInfo += "{\"colField\":\"" + MyTools.StrFiltr(vec.get(i))+"_1" + "\"," +
						"\"colName\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "(百分制)\"," +
						"\"colType\":\"" + "1" + "\"},"+
						"{\"colField\":\"" + MyTools.StrFiltr(vec.get(i))+"_2" + "\"," +
						"\"colName\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "(等级制)\","+
						"\"colType\":\"" + "2" + "\"},";
			}
			if(colInfo.length() > 1){
				colInfo = colInfo.substring(0, colInfo.length()-1);
			}
		}
		colInfo += "]";
		resultVec.add(colInfo);
		
		sql="select 等级考学生类别 from V_自设考试管理_考试信息表 where 状态='1' and 考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"'";
		zslb=db.GetContextVector(sql);
		if(!MyTools.StrFiltr(zslb.get(0)).equals("all")){
			String [] aslbString=zslb.get(0).toString().split(",");
			for(int i=0;i<aslbString.length;i++){
				zslbVec.add(aslbString[i]);
			}
		}
		
		//读取成绩信息
		/*sql="select distinct f.班内学号,a.学号,a.姓名,c.课程号,a.成绩,a.等级,f.招生类别 " +
				"from V_自设考试管理_学生成绩信息表 as a " +
				"left join V_自设考试管理_考试学科信息表 as b on a.考试学科编号=b.考试学科编号 " +
				"left join V_课程数据子类 as c on b.课程代码=c.课程号 " +
				"left join V_规则管理_授课计划主表 as d on b.班级代码=d.行政班代码 " +
				"left join V_规则管理_授课计划明细表 as e on e.授课计划主表编号=d.授课计划主表编号 and c.课程号=e.课程代码 " +
				"left join V_学生基本数据子类 as f on a.学号=f.学号 " +
				"left join V_基础信息_教研组信息表 as g on b.课程代码=g.学科代码 "+
				"where a.状态='1' and b.状态='1' and f.学生状态 in ('01','05') " +
				"and b.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' and b.班级代码='"+MyTools.fixSql(this.getCJ_BJDM())+"' ";
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0 && this.getAUTHCODE().indexOf(xbjwgl)<0){
			if(this.getAUTHCODE().indexOf(jyzzz)>0){
				sql += " and d.状态='1' and e.状态='1' and g.状态='1' and g.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
			}else{
				sql += " and d.状态='1' and e.状态='1' and e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			}
			
		}
		sql+="order by f.班内学号,a.姓名,c.课程号 ";*/
		//2017921翟旭超添加权限判断代码
		sql = "select distinct h.班内学号,a.学号,a.姓名,g.课程号,a.成绩,a.等级,h.招生类别 " +
			"from V_自设考试管理_学生成绩信息表 as a " +
			"left join V_自设考试管理_考试学科信息表 as i on a.考试学科编号=i.考试学科编号 " +
			"left join V_学校班级数据子类 b on b.行政班代码=i.班级代码  " +
			"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
			"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 ) d on d.行政班代码=i.班级代码 and d.课程代码 like '%'+i.课程代码+'%' " +
			"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
			"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 ) e on e.行政班代码=i.班级代码 and e.课程代码=i.课程代码 " +
			"left join V_基础信息_教研组信息表 f on f.学科代码=i.课程代码  " +
			"left join V_课程数据子类 g on g.课程号=i.课程代码 " +
			"left join V_学生基本数据子类 as h on a.学号=h.学号 " +
			"where h.学生状态 in ('01','05') " +
			"and i.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' and i.班级代码='"+MyTools.fixSql(this.getCJ_BJDM())+"' ";
		
		//2017919翟旭超添加权限判断代码
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0&&this.getAUTHCODE().indexOf(xbjwgl)<0){
			sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
		
			if(this.getAUTHCODE().indexOf(jyzzz)>-1){
				sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
			}
			//系部
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			sql+=" ) ";
		}
		sql+="order by h.班内学号,a.姓名,g.课程号 ";
		
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			String curStuCode = "";
			String flag="0";
			
			String djkcj="";//等级考成绩
			String bfbcj="";//百分比成绩
			for(int i=0; i<vec.size(); i+=7){
				bfbcj=MyTools.StrFiltr(vec.get(i+4));
				djkcj=MyTools.StrFiltr(vec.get(i+5));
				if(dfFlag.equalsIgnoreCase("view")){
					if(bfbcj.equalsIgnoreCase("")){
						bfbcj="缺考";
					}
					if(djkcj.equalsIgnoreCase("")){
						djkcj="缺考";
					}
				}
				
				if(!curStuCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
					curStuCode = MyTools.StrFiltr(vec.get(i+1));
					if(zslb.get(0).equals("all")){
						flag="1";
					}else{
						for(int j=0;j<zslbVec.size();j++){
							if(MyTools.StrFiltr(vec.get(i+6)).equalsIgnoreCase(MyTools.StrFiltr(zslbVec.get(j)))){
								flag="1";
								break;
							}else{
								flag="0";
								continue;
							}
						}
					}
					if(i > 0){
						scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1)+"},";
					}
					scoreInfo += "{\"CX_KH\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
								"\"CX_XH\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
								"\"CX_XM\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
								"\"" + MyTools.StrFiltr(vec.get(i+3))+"_1" + "\":\"" + bfbcj + "\","+
								"\"" + MyTools.StrFiltr(vec.get(i+3))+"_2" + "\":\"" + djkcj + "\",";
				}else{
					scoreInfo += "\"" + MyTools.StrFiltr(vec.get(i+3))+"_1" + "\":\"" + bfbcj + "\",";
					scoreInfo += "\"" + MyTools.StrFiltr(vec.get(i+3))+"_2" + "\":\"" + djkcj + "\",";
				}
				scoreInfo+="\"flag\":\"" + MyTools.StrFiltr(vec.get(i+3))+"_2"+flag + "\",";
			}
			
			if(scoreInfo.length() > 1){
				scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1)+"}";
			}
		}
		scoreInfo += "]";
		resultVec.add(scoreInfo);
		return resultVec;
	}
	
	/**
	 * 保存成绩
	 * @date:2017-06-30
	 * @author:zhaixuchao
	 * @param scoreData 成绩信息
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void saveScore(String scoreData) throws SQLException{
		Vector sqlVec = new Vector();
		String sql = "";
		String scoreArray[] = scoreData.split(",", -1);
		String cjScore = "";
		
		String ddScore = "";
		
		for(int i=0; i<scoreArray.length; i+=4){
			cjScore = scoreArray[i+2];
			ddScore = scoreArray[i+3];
			sql="update a set " ;
				if("".equalsIgnoreCase(ddScore)){
					sql += "等级=null ,";
				}
				else{
					sql += "等级='" + MyTools.fixSql(scoreArray[i+3]) + "' ,";
				}
				if("".equalsIgnoreCase(cjScore)){
					sql += "成绩=null ";
				}
				else{
					sql += "成绩='" + MyTools.fixSql(scoreArray[i+2]) + "' ";
				}
			
			sql+="from V_自设考试管理_学生成绩信息表 a "+
				"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号 " +
				"where a.状态='1' and b.状态='1' " +
				"and a.学号='"+MyTools.fixSql(scoreArray[i])+"' " +
				"and b.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' " +
				"and b.课程代码='"+MyTools.fixSql(scoreArray[i+1])+"' ";
				
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * ExportExcelClassScore 班级成绩导出
	 * @author zhaixuchao
	 * @date:2017-08-04
	 * @param examName 考试名称
	 * @param className 班级名称
	 * @return savePath 下载路径
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */
	public String ExportExcelClassScore(String examName ,String className ,String dfFlag) throws SQLException, UnsupportedEncodingException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		Vector resultVec = new Vector();
		String savePath = "";
		ArrayList title = new ArrayList();
		Vector DJvec = null;
		title.add("学号");
		title.add("姓名");
		
		//读取学科信息
		/*sql="select distinct a.课程代码,b.课程名称 " +
				"from V_自设考试管理_考试学科信息表 as a " +
				"left join V_课程数据子类 as b on a.课程代码=b.课程号 " +
				"left join V_规则管理_授课计划主表 as c on a.班级代码=c.行政班代码 " +
				"left join V_规则管理_授课计划明细表 as d on c.授课计划主表编号=d.授课计划主表编号 and b.课程号=d.课程代码  " +
				"left join V_基础信息_教研组信息表 as e on a.课程代码=e.学科代码 "+
				"where a.状态='1' and a.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' and a.班级代码='"+MyTools.fixSql(this.getCJ_BJDM())+"' ";
		//权限判断
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0 && this.getAUTHCODE().indexOf(xbjwgl)<0){
			if(this.getAUTHCODE().indexOf(jyzzz)>0){
				sql += " and c.状态='1' and d.状态='1' and e.状态='1' and e.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
			}else{
				sql += " and c.状态='1' and d.状态='1' and d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			}
		}
		sql += " order by a.课程代码";*/
		sql = "select distinct a.课程代码,g.课程名称 " +
			"from V_自设考试管理_考试学科信息表 as a " +
			"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
			"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
			"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 ) d on d.行政班代码=a.班级代码 and d.课程代码 like '%'+a.课程代码+'%' " +
			"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
			"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 ) e on e.行政班代码=a.班级代码 and e.课程代码=a.课程代码 " +
			"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  " +
			"left join V_课程数据子类 g on g.课程号=a.课程代码 " +
			"where a.考试编号='" + MyTools.fixSql(this.getCJ_KSBH()) + "' " +
			"and a.班级代码='" + MyTools.fixSql(this.getCJ_BJDM()) + "' ";
		//2017921翟旭超添加权限判断代码
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0&&this.getAUTHCODE().indexOf(xbjwgl)<0){
			sql += " and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
		
			if(this.getAUTHCODE().indexOf(jyzzz)>-1){
				sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
			}
			//系部
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			sql+=" ) ";
		}
		sql += " order by a.课程代码";
		Vector kcvec = db.GetContextVector(sql);
		
		//将学科添加到数组里面
		if(kcvec!=null && kcvec.size()>0){
			for(int i = 0; i<kcvec.size();i+=2){
				title.add(kcvec.get(i+1).toString()+"(百分制)");
				title.add(kcvec.get(i+1).toString()+"(等级制)");
			}
		}
		
		//读取成绩信息
		/*sql="select distinct f.班内学号,a.学号,a.姓名,c.课程号,a.成绩,a.等级 " +
				"from V_自设考试管理_学生成绩信息表 as a " +
				"left join V_自设考试管理_考试学科信息表 as b on a.考试学科编号=b.考试学科编号 " +
				"left join V_课程数据子类 as c on b.课程代码=c.课程号 " +
				"left join V_规则管理_授课计划主表 as d on b.班级代码=d.行政班代码 " +
				"left join V_规则管理_授课计划明细表 as e on e.授课计划主表编号=d.授课计划主表编号 and c.课程号=e.课程代码 " +
				"left join V_学生基本数据子类 as f on a.学号=f.学号 " +
				"left join V_基础信息_教研组信息表 as g on b.课程代码=g.学科代码 "+
				"where a.状态='1' and b.状态='1'  and f.学生状态 in ('01','05') " +
				"and b.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' and b.班级代码='"+MyTools.fixSql(this.getCJ_BJDM())+"' ";
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0 && this.getAUTHCODE().indexOf(xbjwgl)<0){
			if(this.getAUTHCODE().indexOf(jyzzz)>0){
				sql += " and d.状态='1' and e.状态='1' and g.状态='1' and g.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
			}else{
				sql += " and d.状态='1' and e.状态='1' and e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			}
		}
		sql+="order by f.班内学号,a.姓名,c.课程号 ";*/
		
		//2017921翟旭超添加权限判断代码
		sql = "select distinct h.班内学号,a.学号,a.姓名,g.课程号,a.成绩,a.等级 " +
			"from V_自设考试管理_学生成绩信息表 as a " +
			"left join V_自设考试管理_考试学科信息表 as i on a.考试学科编号=i.考试学科编号 " +
			"left join V_学校班级数据子类 b on b.行政班代码=i.班级代码  " +
			"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
			"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 ) d on d.行政班代码=i.班级代码 and d.课程代码 like '%'+i.课程代码+'%' " +
			"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
			"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 ) e on e.行政班代码=i.班级代码 and e.课程代码=i.课程代码 " +
			"left join V_基础信息_教研组信息表 f on f.学科代码=i.课程代码  " +
			"left join V_课程数据子类 g on g.课程号=i.课程代码 " +
			"left join V_学生基本数据子类 as h on a.学号=h.学号 " +
			"where h.学生状态 in ('01','05') " +
			"and i.考试编号='"+MyTools.fixSql(this.getCJ_KSBH())+"' and i.班级代码='"+MyTools.fixSql(this.getCJ_BJDM())+"' ";
				
		//2017919翟旭超添加权限判断代码
		if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0 && this.getAUTHCODE().indexOf(xbjdzr)<0&&this.getAUTHCODE().indexOf(xbjwgl)<0){
			sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				
			if(this.getAUTHCODE().indexOf(jyzzz)>-1){
				sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
			}
			//系部
			if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			//班主任
			if(this.getAUTHCODE().indexOf(bzr) > -1){
				sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			}
			sql+=" ) ";
		}
		sql += "order by h.班内学号,a.姓名,g.课程号 ";
		vec = db.GetContextVector(sql);
		
		sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='DJ'";
		DJvec = db.GetContextVector(sql);
		
		//整理数据
		if(vec!=null && vec.size()>0){
			String stuCode = "";
			for(int i=0; i<vec.size(); i+=6){
				if(!stuCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
					resultVec.add(MyTools.StrFiltr(vec.get(i+1)));
					resultVec.add(MyTools.StrFiltr(vec.get(i+2)));
					
					stuCode = MyTools.StrFiltr(vec.get(i+1));
				}
				
				//2017/10/12翟旭超注释原始代码
				/*String score = MyTools.StrFiltr(vec.get(i+4));
				String dj =  MyTools.StrFiltr(vec.get(i+5));
				resultVec.add(score);
				for(int j=0;j<DJvec.size();j+=2){
					if(dj.equalsIgnoreCase(MyTools.StrFiltr(DJvec.get(j+1)))){
						dj=MyTools.StrFiltr(DJvec.get(j));
					}
				}
				resultVec.add(dj);*/
				
				String score = MyTools.StrFiltr(vec.get(i+4));
				String dj =  MyTools.StrFiltr(vec.get(i+5));
				for(int j=0;j<DJvec.size();j+=2){
					if(dj.equalsIgnoreCase(MyTools.StrFiltr(DJvec.get(j+1)))){
						dj=MyTools.StrFiltr(DJvec.get(j));
					}
				}
				if(dfFlag.equalsIgnoreCase("view")){
					if(score.equalsIgnoreCase("")){
						score="缺考";
					}
					if(dj.equalsIgnoreCase("")){
						dj="缺考";
					}
				}
				resultVec.add(score);
				resultVec.add(dj);
				
			}
		
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			
			savePath = MyTools.getProp(request, "Base.exportExcelPath");
			
			//创建
			try {
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				// 输出的excel的路径   
				//filePath += "d:\\年级学生成绩汇总信息_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls"; 
				//输出流
				savePath += "/"+examName+className+"学生成绩信息_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);//工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				
				//设置列宽
				for(int i=0; i<title.size(); i++){
					if(i==0){
						wsheet.setColumnView(i, 25);
					}else{
						wsheet.setColumnView(i, 15);
					}
					if(i>1){
						wsheet.setColumnView(i, 20);
					}
				}
				//设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				
				for(int i=0;i<title.size();i++){   
				     // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z   
				     // 在Label对象的子对象中指明单元格的位置和内容   
					content = new Label(i,0,title.get(i).toString(),contentStyle); 
				     // 将定义好的单元格添加到工作表中   
					wsheet.addCell(content);   
				 }  
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				//填充数据
				//k:用于循环时Excel的行号
				//外层for用于循环行,内曾for用于循环列'
				for(int i=0,k=1; i<resultVec.size();i+=title.size(), k++){
					for(int j=0;j<title.size();j++){ 
						content = new Label(j, k, resultVec.get(i+j) + "",contentStyle);
						wsheet.addCell(content);
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
	
	/***
	 * saveimportxlsZSKSGL 保存excel数据
	 * @createDate 2017-08-04
	 * @author zhaixuchao
	 * @description:导入
	 * @param mySmartUpload
	 * @param examCode 考试编号
	 * @param classCode 班级代码
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 * @throws SmartUploadException
	 * @throws WrongSQLException
	 */
	public void saveimportxlsZSKSGL(SmartUpload mySmartUpload ,String examCode ,String classCode, String usercode, String authcode) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		Vector sqlVec = new Vector();
		String sql="";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		boolean flag=true;
		String tempsheet="";//sheet名
		String tempsoin="";//第1列
		int sheetnum=0;//sheet计数
		String importflag="";
		Vector titleVec = new Vector();
		Vector resultVec =new Vector();
		Vector kcvec = new Vector();
		
		Vector DJvec = new Vector();
		int titleLeft = 0;//可目前的固定数据长度
		titleVec.add("学号");
		titleVec.add("姓名");
		titleLeft = titleVec.size();
		String url=MyTools.getProp(request, "Base.upLoadPathFile");
		File filep = new File(url);
		// 根据配置路径来判断没有文件则创建文件
		if (!filep.exists()) {
			filep.mkdirs();
		}
		Workbook workbook = null;
		Vector vectormx=new Vector();
		
		SmartFiles files = mySmartUpload.getFiles();
		com.jspsmart.upload.SmartFile file = null;
		
		//判断
		String path= unescape(mySmartUpload.getRequest().getParameter("sxpath"));
		//获取文件名称
		File f=new File(path);
		String filename=f.getName();
		//判断
		if(files.getCount() > 0){
			file = files.getFile(0);
			if(file.getSize()<=0){
				this.MSG="文件内容为空，请确认！";
			}
			file.saveAs(url +"/"+filename);
		}
		url=url +"/"+filename;
		File file1=new File(url);
		InputStream is = new FileInputStream(file1);
		
		try {
			workbook = Workbook.getWorkbook(is);
			
			//读取学科信息
			/*sql="select distinct a.课程代码,b.课程名称 " +
					"from V_自设考试管理_考试学科信息表 as a " +
					"left join V_课程数据子类 as b on a.课程代码=b.课程号 " +
					"left join V_规则管理_授课计划主表 as c on a.班级代码=c.行政班代码 " +
					"left join V_规则管理_授课计划明细表 as d on c.授课计划主表编号=d.授课计划主表编号 and b.课程号=d.课程代码  " +
					"left join V_基础信息_教研组信息表 as e on a.课程代码=e.学科代码 "+
					"where a.状态='1' and a.考试编号='"+MyTools.fixSql(examCode)+"' and a.班级代码='"+MyTools.fixSql(classCode)+"' ";
			//权限判断
			if(authcode.indexOf(admin)<0 && authcode.indexOf(jxzgxz)<0 && authcode.indexOf(qxjdzr)<0 && authcode.indexOf(qxjwgl)<0 && authcode.indexOf(xbjdzr)<0 && authcode.indexOf(xbjwgl)<0){
				if(authcode.indexOf(jyzzz)>0){
					sql += " and c.状态='1' and d.状态='1' and e.状态='1' and e.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
				}else{
					sql += " and c.状态='1' and d.状态='1' and d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				}
				
				//sql += " and c.状态='1' and d.状态='1' and d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
			}
			sql += " order by a.课程代码";*/
			sql = "select distinct a.课程代码,g.课程名称 " +
				"from V_自设考试管理_考试学科信息表 as a " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
				"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
				"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 ) d on d.行政班代码=a.班级代码 and d.课程代码 like '%'+a.课程代码+'%' " +
				"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
				"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 ) e on e.行政班代码=a.班级代码 and e.课程代码=a.课程代码 " +
				"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  " +
				"left join V_课程数据子类 g on g.课程号=a.课程代码 " +
				"where a.考试编号='"+MyTools.fixSql(examCode)+"' " +
				"and a.班级代码='"+MyTools.fixSql(classCode)+"' ";
			//2017921翟旭超添加权限判断代码
			if(authcode.indexOf(admin)<0 && authcode.indexOf(jxzgxz)<0 && authcode.indexOf(qxjdzr)<0 && authcode.indexOf(qxjwgl)<0 && authcode.indexOf(xbjdzr)<0&&authcode.indexOf(xbjwgl)<0){
				sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			
				if(authcode.indexOf(jyzzz)>-1){
					sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
				}
				//系部
				if(authcode.indexOf(xbjdzr)>-1 || authcode.indexOf(xbjwgl)>-1){
					sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				//班主任
				if(authcode.indexOf(bzr) > -1){
					sql += " or b.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
				}
				sql+=" ) ";
			}
			sql += " order by a.课程代码";
			kcvec = db.GetContextVector(sql);
			
			//将学科添加到数组里面
			if(kcvec!=null && kcvec.size()>0){
				for(int i = 0; i<kcvec.size();i+=2){
					System.out.println(kcvec.get(i+1));
					titleVec.add(kcvec.get(i+1).toString()+"(百分制)");
					titleVec.add(kcvec.get(i+1).toString()+"(等级制)");
				}
			}
			sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='DJ' ";
			DJvec = db.GetContextVector(sql);
			
			for(int k=0;k<workbook.getNumberOfSheets();k++){
				Sheet sheet = workbook.getSheet(k); //获取sheet(k)的数据对象
				int rsRows = sheet.getRows(); //获取sheet(k)中的总行数
				int rscolumns=sheet.getColumns(); //获取总列数
				tempsheet=workbook.getSheet(k).getName();//获取sheet名
				//System.out.println("rows="+rsRows+"//columns="+rscolumns);
				//System.out.println("tempsheet="+tempsheet);
				//if("sheet1".equalsIgnoreCase(tempsheet.trim().toLowerCase())){
				sheetnum++;
				
				//判断导入Excel的列长是否和数据库中一致
				if(titleVec.size() == rscolumns){    
					for(int x=0;x<titleVec.size();x++){
						tempsoin=sheet.getCell(x, 0).getContents().trim(); //第1行
						if(tempsoin.equalsIgnoreCase((String) titleVec.get(x))){
							importflag = "true";
							
						}else{
							importflag = "false";
							String msg = titleVec.toString().substring(1,titleVec.toString().length()-1);
							this.setMSG("格式不正确（Excel应该为--"+msg+"--格式）");
							return;
						}
					}
				} else{
					importflag = "false";
					String msg = titleVec.toString().substring(1,titleVec.toString().length()-1);
					this.setMSG("格式不正确（Excel应该为--"+msg+"--格式）");
					return;
				}
				if(importflag.equalsIgnoreCase("true")){
					for(int y=1;y<rsRows-1;y++){
						for(int j=0;j<titleVec.size();j++){ 
							tempsoin=sheet.getCell(j, y).getContents().trim();
							resultVec.add(tempsoin);
						}
						/*for(int j=titleLeft;j<titleVec.size();j++){
							resultVec.add(titleVec.get(j));
						}*/
					}
					String tempScore = "";
					//String Score ="";
					for(int i=0;i<resultVec.size();i+=titleVec.size()){
						tempScore = parseScoreValue(MyTools.StrFiltr(resultVec.get(i)));
						for(int j=0;j<titleVec.size()-2;j+=2){
							sql ="update a set ";
							String xk="";
							String BfString=parseScoreValue(MyTools.StrFiltr(resultVec.get(i+j+2)));
							String DjString=MyTools.StrFiltr(resultVec.get(i+j+3));
							if("".equalsIgnoreCase(BfString) || "null".equalsIgnoreCase(BfString)){
								sql += "a.成绩=null ,";
								
								if("null".equalsIgnoreCase(BfString)){
									flag=false;
								}
							}else{
								if(Double.parseDouble(BfString) > 100){
									BfString = "100";
								}
								sql += "a.成绩='" + MyTools.fixSql(BfString) + "', ";
							}
							//等级 
							if("".equalsIgnoreCase(DjString) || "null".equalsIgnoreCase(DjString)){
								sql += "a.等级=null ";
								
							}else{
								Boolean djFlag=false;
								for(int m=0;m<DJvec.size();m+=2){
									if(DJvec.get(m).equals(DjString.toUpperCase())){
										DjString=MyTools.StrFiltr(DJvec.get(m+1));
										djFlag=true;
									}
								}
								if(!djFlag){
									if(!"".equalsIgnoreCase(DjString)){
										flag=false;
										DjString=null;
										sql += "a.等级=null ";
									}
								}else{
									sql += "a.等级='" + MyTools.fixSql(DjString) + "' ";
								}
								//sql += "a.等级='" + MyTools.fixSql(DjString) + "' ";
							}
							sql += "from V_自设考试管理_学生成绩信息表 a " +
								"left join V_自设考试管理_考试学科信息表 b on b.考试学科编号=a.考试学科编号 " +
								"where a.状态='1' and b.状态='1' and a.学号='" + MyTools.fixSql(resultVec.get(i).toString()) + "' " +
								"and b.考试编号='" + MyTools.fixSql(examCode) + "' " +
								"and b.课程代码 in (select 课程号 from V_课程数据子类  where " +
								"课程名称='" + MyTools.fixSql(titleVec.get(j+2).toString().substring(0, titleVec.get(j+2).toString().indexOf("(百分制)"))) +"')";
							sqlVec.add(sql);
						}
					}
					if(db.executeInsertOrUpdateTransaction(sqlVec)){
						if(flag){
							this.setMSG("导入成功");
						}else{
							this.setMSG("有部分成绩信息不符合规范，未成功导入");
						}
					}else{
						this.setMSG("导入失败");
					}
					return;
				}
			}
			if(sheetnum==0){
				this.setMSG("未找到sheet");
				return;
			}
		}catch (Exception e) {
			this.setMSG("导入失败");
			e.printStackTrace();
		}
		finally {
			workbook.close();
			is.close();
			//删除服务器上文件
			//路径为文件且不为空则进行删除  
		    if (file1.isFile() && file1.exists()) {  
		    	file1.delete();
		    }
		}
	}
	
	/***
	 * saveimportxlsZSKSGL 保存excel数据
	 * @createDate 2017-08-04
	 * @author zhaixuchao
	 * @description:导入
	 * @param mySmartUpload
	 * @param examCode
	 * @throws IOException
	 * @throws SmartUploadException
	 */

	//转换文件路径中的中文字符
	public String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0, nLen = src.length();
		char ch;
		while (lastPos < nLen) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(
							src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(
							src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = nLen;
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	
	/**
	 * 解析文字成绩对应值
	 * @param score
	 * @return
	 */
	public static String parseScoreValue(String score){
		String resultScore = "";
		double tempScore = 0;
		
		if(!"".equalsIgnoreCase(score)){
			if(isNumeric(score)){
				tempScore = Double.parseDouble(score);
				if(tempScore > 100){
					score = "100";
				}
				if(tempScore < 0){
					score = "null";
				}
				
				resultScore = score;
			}else{
				/*for(int i=0; i<tempArray.length; i+=2){
					if(score.equalsIgnoreCase(tempArray[i])){
						resultScore = tempArray[i+1];
					}*/
				resultScore="null";
			}
		}
		return resultScore;
	}
		
	/**
     *  判断是否为数字
     * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
     * @return
     * @author yutao
     * @date 2016年11月14日下午7:41:22
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    
    /**
	 * 读取等级下拉框
	 * @date:2017-09-08
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadDJCombo() throws SQLException{
		Vector vec = null;
		
		String sql="select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 类别名称 , 描述  from  V_信息类别_类别操作 where 父类别代码='DJ' " +
				"order by comboValue";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	public String getAUTHCODE() {
		return AUTHCODE;
	}

	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
	}

	public String getCJ_KSBH() {
		return CJ_KSBH;
	}

	public void setCJ_KSBH(String cJ_KSBH) {
		CJ_KSBH = cJ_KSBH;
	}

	public String getCJ_BH() {
		return CJ_BH;
	}

	public void setCJ_BH(String cJ_BH) {
		CJ_BH = cJ_BH;
	}

	public String getCJ_XH() {
		return CJ_XH;
	}

	public void setCJ_XH(String cJ_XH) {
		CJ_XH = cJ_XH;
	}

	public String getCJ_XM() {
		return CJ_XM;
	}

	public void setCJ_XM(String cJ_XM) {
		CJ_XM = cJ_XM;
	}

	public String getCJ_KSXKBH() {
		return CJ_KSXKBH;
	}

	public void setCJ_KSXKBH(String cJ_KSXKBH) {
		CJ_KSXKBH = cJ_KSXKBH;
	}

	public String getCJ_KCDM() {
		return CJ_KCDM;
	}

	public void setCJ_KCDM(String cJ_KCDM) {
		CJ_KCDM = cJ_KCDM;
	}

	public String getCJ_BJDM() {
		return CJ_BJDM;
	}

	public void setCJ_BJDM(String cJ_BJDM) {
		CJ_BJDM = cJ_BJDM;
	}

	public String getCJ_CJ() {
		return CJ_CJ;
	}

	public void setCJ_CJ(String cJ_CJ) {
		CJ_CJ = cJ_CJ;
	}

	public String getCJ_CJR() {
		return CJ_CJR;
	}

	public void setCJ_CJR(String cJ_CJR) {
		CJ_CJR = cJ_CJR;
	}

	public String getCJ_CJSJ() {
		return CJ_CJSJ;
	}

	public void setCJ_CJSJ(String cJ_CJSJ) {
		CJ_CJSJ = cJ_CJSJ;
	}

	public String getCJ_ZT() {
		return CJ_ZT;
	}

	public void setCJ_ZT(String cJ_ZT) {
		CJ_ZT = cJ_ZT;
	}

	public String getCJ_XN() {
		return CJ_XN;
	}

	public void setCJ_XN(String cJ_XN) {
		CJ_XN = cJ_XN;
	}

	public String getCJ_XQ() {
		return CJ_XQ;
	}

	public void setCJ_XQ(String cJ_XQ) {
		CJ_XQ = cJ_XQ;
	}

	public String getCJ_XNXQBM() {
		return CJ_XNXQBM;
	}

	public void setCJ_XNXQBM(String cJ_XNXQBM) {
		CJ_XNXQBM = cJ_XNXQBM;
	}

	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getListsql() {
		return listsql;
	}

	public void setListsql(String listsql) {
		this.listsql = listsql;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}