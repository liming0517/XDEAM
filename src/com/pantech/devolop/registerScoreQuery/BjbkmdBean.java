package com.pantech.devolop.registerScoreQuery;
/**
 * 班级补考名单
 *@date:2017-06-02
 * @author:zouyu
 * @return Vector
 * @throws SQLException
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import jxl.write.biff.RowsExceededException;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class BjbkmdBean {
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
	private String XN;//学年

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public BjbkmdBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2017-05-04
	 * @author:zouyu
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
	 * @date:2017-5-4
	 * @author:zouyu
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
			"select distinct d.学年,c.班级代码,c.班级名称,c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码 "+
			"left join tempClassInfo c on c.班级代码=a.行政班代码 " +
			"where 1=1 and a.来源类型 in ('1','2')";
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
		
		if(!"".equalsIgnoreCase(this.getXN())){
			sql += " and d.学年='" + MyTools.fixSql(this.getXN()) + "'";
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
		sql += " order by d.学年 desc,c.班级名称";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017.05.04
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
			Vector vec = null;
			String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
					"union all " +
					"select distinct 学年 as comboName,学年 as comboValue,'1' as orderNum " +
					"from V_规则管理_学年学期表 where 状态='1' order by orderNum,comboValue desc";
			vec = db.getConttexJONSArr(sql, 0, 0);
			
			return vec;
		}
	
	/**
	 * 读取年级下拉框
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNjdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select  年级名称 as comboName,年级代码 as comboValue,1 " +
				"from V_学校年级数据子类 where 年级状态='1' " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取年级下拉框
	 * @date:2017-6-1
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKcCombo() throws SQLException{
		String sql = "";
		Vector subVec = null;
		Vector xqzbVec = new Vector();//学期占比设置的信息
		int  type=0;
		String firstSem = "";//上学期
		String secondSem = "";//下学期
		
		//查询学期设置信息
		sql = "select 系部代码,学期一,学期二,计算方式 " +
			"from V_成绩管理_系部学年总评设置表 where 系部代码='"+MyTools.StrFiltr(this.getXBDM())+"'";
		xqzbVec= db.GetContextVector(sql);
		
		//判断计算方式		
		if(xqzbVec!=null && xqzbVec.size()>0){
			if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
				type=2;//跨学年2
			}else{
				type=1;//同学年
			}
		}else{
			type=0;
		}
		if(type == 2){//跨学年
			firstSem = MyTools.fixSql(this.getXN()) + "201";
			secondSem = MyTools.parseString((MyTools.StringToInt(this.getXN())+1))+"101";
		}else{//同学年
			firstSem = MyTools.fixSql(this.getXN()) + "101";
			secondSem = MyTools.fixSql(this.getXN()) + "201";
		}
		sql="select '请选择' as comboName,'' as comboValue,'0' as orderNum "+
			"union all "+
			"select distinct a.课程名称 as comboName,a.课程代码  as comboValue,'1' as orderNum  "+
			"from V_成绩管理_登分教师信息表 a "+
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 "+
			"left join V_规则管理_学年学期表 c on  b.学年学期编码=c.学年学期编码 "+
			"where a.状态='1' and b.状态='1' and b.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
			"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			"and a.相关编号 in (select distinct b.相关编号 from V_学生基本数据子类 a "+
			"left join V_成绩管理_学生成绩信息表 b on a.姓名=b.姓名  where a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "') " +
			//20170907添加选修课过滤条件yeq
			"and a.来源类型 in ('1','2') " +			
			"order by orderNum,comboName ";
		subVec = db.getConttexJONSArr(sql, 0, 0);
		
		return subVec;
	}
	
	/**
	 * 读取导出年级下拉框
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportNjCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,'1' as orderNum " +
				"from V_学校年级数据子类 where 年级状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 读取系部下拉框
	 * @date:2017-05-04
	 * @author:zouyu
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
	 * 读取导出系部下拉框
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportXbCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 系部名称 as comboName,系部代码 as comboValue,'1' as orderNum " +
				"from V_基础信息_系部信息表 where 系部代码<>'C00' order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportBjCombo(String nj,String zy,String xb,String xn) throws SQLException{
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "with tempClassInfo as ("+
			"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 "+
			"from V_学校班级数据子类 a "+
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 "+
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 "+
			"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,"+
			"a.班主任工号 from V_基础信息_教学班信息表 a  "+
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码  "+
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码)  "+
			"select '全部' as comboName,'all' as comboValue,'0' as orderNum "+
			"union all  "+
			"select distinct c.班级名称,c.班级代码 ,'1' as orderNum "+
			"from V_成绩管理_登分教师信息表 a  "+
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号   "+
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码    "+
			"left join tempClassInfo c on c.班级代码=a.行政班代码 where 1=1  ";
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
		if(!"".equalsIgnoreCase(xn)){
					sql += " and d.学年='" +xn + "'";
				}
		if(!"all".equalsIgnoreCase(xb)){
			sql += " and c.系部代码 in ('" + xb.replaceAll(",", "','") + "')";
			
		}
		if(!"all".equalsIgnoreCase(nj)){
			sql += " and c.年级代码 in ('" + nj.replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(zy)){
			sql += " and c.专业代码 in ('" + zy.replaceAll(",", "','") + "')";
		}
		sql +=	"order by orderNum,comboName";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2017-05-04
	 * @author:zouyu
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
	 * 读取专业下拉框
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportZyCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类 where 状态='1' and len(专业代码)>3 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询学生成绩信息
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadStuScoreInfo(String XH,String KCMC)throws SQLException{
		String sql = "";
		Vector subVec = null;
		Vector xqzbVec = new Vector();//学期占比设置的信息
		Vector scoreInfoVec = null;//学生成绩信息
		Vector countInfoVec = null;//学生人员信息
		Vector resultVec = new Vector();//存放查询出来所有的信息
		Vector firstVec = new Vector();//存放上学期课程
		Vector secondVec = new Vector();//存放下学期课程
		Vector xyspVec = new Vector();//存放学业水平成绩信息
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP);  
		String colInfo = "[";
		String scoreInfo = "[";
		int  type=0;
		String firstSem = "";//上学期
		String secondSem = "";//下学期
		boolean xhpd=false;
		boolean kcmcpd=false;
		if("".equalsIgnoreCase(XH)){
			xhpd=true;
		}
		if("".equalsIgnoreCase(KCMC)){
			kcmcpd=true;
		}
		//查询学期设置信息
		sql = "select 系部代码,学期一,学期二,计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码='"+MyTools.StrFiltr(this.getXBDM())+"'";
		xqzbVec= db.GetContextVector(sql);
		
		//判断计算方式		
		if(xqzbVec!=null && xqzbVec.size()>0){
			if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
				type=2;//跨学年2
			}else{
				type=1;//同学年
			}
		}else{
			xqzbVec.add("");
			xqzbVec.add("40");
			xqzbVec.add("60");
			xqzbVec.add("1");
			type=0;
		}
		if(type == 2){//跨学年
			firstSem = MyTools.fixSql(this.getXN()) + "201";
			secondSem = MyTools.parseString((MyTools.StringToInt(this.getXN())+1))+"101";
		}else{//同学年
			firstSem = MyTools.fixSql(this.getXN()) + "101";
			secondSem = MyTools.fixSql(this.getXN()) + "201";
		}
		
		//查询学年的所有课程
	/*	sql="select distinct a.课程代码,a.课程名称 from V_成绩管理_登分教师信息表 a left join "+
			"V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 c on  b.学年学期编码=c.学年学期编码 "+
			"left join V_学校班级数据子类 d on a.行政班代码=d.行政班代码 "+
			" where a.状态='1' and b.状态='1' "+
			"and b.学年学期编码 in ('" + firstSem + "','" + secondSem + "') and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'  and a.相关编号"+
			" in (select distinct b.相关编号 from V_学生基本数据子类 a left join V_成绩管理_学生成绩信息表 b on a.姓名=b.姓名  " +
			" where a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "') "+
			"order by a.课程代码";*/
		sql = "select distinct b.课程代码,b.课程名称 from V_学生基本数据子类 a " +
			"left join V_成绩管理_登分教师信息表 b on a.行政班代码=b.行政班代码 " +
			"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  " +
			"where c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
			//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
			"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			//20170907添加选修课过滤条件yeq
			"and b.来源类型 in ('1','2') " +
			"order by b.课程代码";
		subVec = db.GetContextVector(sql);
		
		//查询学年的上学期课程
		sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
			"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  " +
			"left join V_学生基本数据子类 d on d.学号=a.学号 "+
			"where c.学年学期编码='" + MyTools.fixSql(firstSem) + "' " +
			//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
			"and d.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			//20170907添加选修课过滤条件yeq
			"and b.来源类型 in ('1','2') " +
			"order by b.课程代码";
		firstVec = db.GetContextVector(sql);
		
		//查询学年的下学期课程
		sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
			"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  " +
			"left join V_学生基本数据子类 d on d.学号=a.学号 "+
			"where c.学年学期编码='" + MyTools.fixSql(secondSem) + "' " +
			//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
			"and d.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			//20170907添加选修课过滤条件yeq
			"and b.来源类型 in ('1','2') " +
			"order by b.课程代码";
		secondVec = db.GetContextVector(sql);
				
		//查询该班级学生成绩信息
		sql = "select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
			"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
			"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
			"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
			"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
			"a.相关编号 ,f.平时比例,f.期中比例,f.期末比例,e.学生状态,f.总评比例选项,e.学籍号 "+
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
			"left join V_规则管理_学年学期表 d on d.学年学期编码=c.学年学期编码 " +
			"left join V_学生基本数据子类 e on e.学号=a.学号 "+
			"left join V_成绩管理_登分设置信息表 f on a.相关编号=f.相关编号 "+
			"where c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
			//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+ MyTools.fixSql(this.getBJBH()) +"') " +
			"and e.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			//20170907添加选修课过滤条件yeq
			"and b.来源类型 in ('1','2') " +
			"order by a.学号,b.课程代码,c.学年学期编码";
		scoreInfoVec = db.GetContextVector(sql);	
		
		//查询学业水平测试成绩
		sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
			"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
			"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
			"where 班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			"and c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
			"and c.类别编号='03' "+
			"order by c.学年学期编码 desc";	
		xyspVec = db.GetContextVector(sql);
			
		//添加课程
		if(subVec!=null && subVec.size()>0){
			for(int i=0; i<subVec.size(); i+=2){
				colInfo += "{\"colField\":\"" + MyTools.StrFiltr(subVec.get(i)) + "\"," +
				"\"colName\":\"" + MyTools.StrFiltr(subVec.get(i+1)) + "\"},";
			}
			colInfo = colInfo.substring(0, colInfo.length()-1);
		}
		colInfo += "]";
		resultVec.add(colInfo);
						
		//查询该班级所有学生			
		sql = "select case when LEFT(班内学号,1)='0' then RIGHT(班内学号,LEN(班内学号)-1) else 班内学号 end as 班内学号  ,姓名,学号 " +
			"from V_学生基本数据子类 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			"order by cast(班内学号 as int) ";
		countInfoVec = db.GetContextVector(sql);	
				//遍历每一个学生每一个课程
		if(countInfoVec!=null && countInfoVec.size()>0){
			String curStuCode = "";
			String curSubCode = "";
			boolean existFlag = false;
				for(int i=0; i<countInfoVec.size(); i+=3){
					curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));//学号
					
					if((xhpd==false&&XH.equalsIgnoreCase(curStuCode))||xhpd){
						for(int j=0; j<subVec.size(); j+=2){
							curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
							 if((kcmcpd==false&&KCMC.equalsIgnoreCase(curSubCode))||kcmcpd){
								 existFlag = false;
								for(int k=0; k<scoreInfoVec.size(); k+=16){
									String tempXnzp = "";
									String firstScore="";
									String secondScore="";
									if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+2)))){
										boolean first = false;
										boolean second = false;
										boolean firstFlag = false;
										boolean secondFlag = false;
										//用来判断上学期是否有课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+8));
										String PS= MyTools.StrFiltr(scoreInfoVec.get(k+5));
										String QZ= MyTools.StrFiltr(scoreInfoVec.get(k+6));
										String QM= MyTools.StrFiltr(scoreInfoVec.get(k+7));
										String psbl= MyTools.StrFiltr(scoreInfoVec.get(k+10));
										String qzbl= MyTools.StrFiltr(scoreInfoVec.get(k+11));
										String qmbl= MyTools.StrFiltr(scoreInfoVec.get(k+12));
										String xsZt=MyTools.StrFiltr(scoreInfoVec.get(k+13));
										String zpBl=MyTools.StrFiltr(scoreInfoVec.get(k+14));
										if("10".equalsIgnoreCase(xsZt)){
											if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
												if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
													zp1="";
												}
											}else {
												if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
													zp1="";
											  }
											}
										}
										
										//上学期和下学期都有
										if(first==true && second==true){
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+24));
											String PS2= MyTools.StrFiltr(scoreInfoVec.get(k+21));
											String QZ2= MyTools.StrFiltr(scoreInfoVec.get(k+22));
											String QM2= MyTools.StrFiltr(scoreInfoVec.get(k+23));
										
											String psbl2= MyTools.StrFiltr(scoreInfoVec.get(k+26));
											String qzbl2= MyTools.StrFiltr(scoreInfoVec.get(k+27));
											String qmbl2= MyTools.StrFiltr(scoreInfoVec.get(k+28));
											String xsZt2=MyTools.StrFiltr(scoreInfoVec.get(k+29));
											String zpBl2=MyTools.StrFiltr(scoreInfoVec.get(k+30));
											if("10".equalsIgnoreCase(xsZt2)){
												if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
													if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
														zp2="";
													}
												}else {
													if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
														zp2="";
												  }
												}
											}
											
											 firstScore=zp1;
											 secondScore=zp2;
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("".equalsIgnoreCase(firstScore) || "作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "-5".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
									
										//上学期
										}else if(first==true && second==false){
											 firstScore=zp1;
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "缓考".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											//下学期
										}else{
											   secondScore=zp1;
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if( "作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
										}
										
										//计算总评
										if(firstFlag==true && secondFlag==true){
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											firstScore="".equalsIgnoreCase(firstScore)?"0":firstScore;
											secondScore="".equalsIgnoreCase(secondScore)?"0":secondScore;
											if(type == 0){
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*0.4+MyTools.StringToDouble(secondScore)*0.6);
											}else{
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*sxqzb/100+MyTools.StringToDouble(secondScore)*xxqzb/100);
											}
											
										}else if(firstFlag==true && secondFlag==false){
												tempXnzp=firstScore;
										}else if(firstFlag==false && secondFlag==true){
												tempXnzp=secondScore;
										}else{
												tempXnzp="";
										}
										
										//对学业水平成绩遍历
										if(xyspVec!=null && xyspVec.size()>0){
											for(int w=0;w<xyspVec.size();w++){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
													tempXnzp=MyTools.StrFiltr(xyspVec.get(w+2));
													break;
												}
											}
											
										}
									 
										if("".equalsIgnoreCase(tempXnzp)){
											scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(scoreInfoVec.get(k+15)) + "\"," +
													"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
													"\"XK\":\"" + MyTools.StrFiltr(subVec.get(j+1)) + "\"," +
													"\"CJ\":\"\"},";
										}else if(MyTools.StringToDouble(tempXnzp)< 60){
											scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(scoreInfoVec.get(k+15)) + "\"," +
													"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
													"\"XK\":\"" + MyTools.StrFiltr(subVec.get(j+1)) + "\"," +
													"\"CJ\":\"" + tempXnzp + "\"},";
										}
								
										existFlag = true;
										break;
										
									/*	if(first==true&&second==true){//两学期都有该课程的情况
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
											
											//计算学年总评
											if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}
											if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}
											
											if(!"免修".equalsIgnoreCase(zp1) || !"免修".equalsIgnoreCase(zp2)){
												if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp1) < 60.0){
														scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\"," +
																"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
																"\"XK\":\"" + subVec.get(j+1)+ "\"," +
																"\"CJ\":\"" + zp1 + "\"},";
													}
												}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp2) < 60.0){
														scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\"," +
																"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
																"\"XK\":\"" + subVec.get(j+1)+ "\"," +
																"\"CJ\":\"" + zp2 + "\"},";
													}
												}else{
													if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
														scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\"," +
																"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
																"\"XK\":\"" + subVec.get(j+1)+ "\"," +
																"\"CJ\":\"\"},";
													}else{
														zp1 = "".equalsIgnoreCase(zp1)?"0":zp1;
														zp2 = "".equalsIgnoreCase(zp2)?"0":zp2;
														if(type == 0){
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
																scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\"," +
																			"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
																			"\"XK\":\"" + subVec.get(j+1)+ "\","+
																			"\"CJ\":\"" + f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6) + "\"},";
															}	 
														}else{
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
																scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\"," +
																			"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
																			"\"XK\":\"" + subVec.get(j+1)+ "\","+
																			"\"CJ\":\"" + f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100) + "\"},";
															}
														}
													}
												}
											} 
											

									}else if(first==true&&second==false){//上学期有该课程,下学期没有
										String zp1 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("免修".equalsIgnoreCase(zp1)){
											zp1 = "60";
										}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
											zp1 = "";
										}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
											zp1 = "0";
										}
										
										if(MyTools.StringToDouble(zp1)<60){
											scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\"," +
														"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
														"\"XK\":\"" + subVec.get(j+1)+ "\"," +
														"\"CJ\":\"" +zp1+ "\"},";
										}
									 }else{//上学期没该课程,下学期有
										 String zp2 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
										 if("免修".equalsIgnoreCase(zp2)){
											 zp2 = "60";
										 }else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
											 zp2 = "";
										 }else if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
											 zp2 = "0";
										 }
										 if(MyTools.StringToDouble(zp2)<60){
											 scoreInfo += "{\"XJH\":\"" + MyTools.StrFiltr(countInfoVec.get(i+2)) + "\"," +
														"\"XM\":\"" + MyTools.StrFiltr(countInfoVec.get(i+1)) + "\","+
														"\"XK\":\"" + subVec.get(j+1)+ "\"," +
														"\"CJ\":\"" +zp2+ "\"},";
										 }
										 

									}//方案三
									existFlag = true;
									break;*/
								}
							}													
						}
					}
				}
			}
			scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
		}
		scoreInfo += "]";
		if("]".equalsIgnoreCase(scoreInfo)){
			scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
			scoreInfo += "[]";
			resultVec.add(scoreInfo);
			
		}else{
			resultVec.add(scoreInfo);
		}
		return resultVec;
}
	/**
	 * 批量导出补考学生成绩信息
	 *@date:2017-05-04
	 * @author:zouyu
	 * @return Vector
	 * @throws SQLException
	 */
	
	public String exportScoreInfo() throws SQLException{
		String sql = "";
		Vector BJJVec=new Vector();//存放所有查询的课程
		Vector BJVec = new Vector();//存放遍历的每一个课程
		Vector xyspVec = new Vector();//存放学业水平成绩信息
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP); 
		String[] titleArray = new String[]{"学籍号","姓名","课程名称","学年总评"};
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		String[] bjList=this.getBJBH().split(",",-1);
		//判断课程是否为全部
		if("all".equalsIgnoreCase(bjList[0])){
			 sql = "with tempClassInfo as ("+
				"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 "+
				"from V_学校班级数据子类 a "+
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 "+
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 "+
				"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,"+
				"a.班主任工号 from V_基础信息_教学班信息表 a  "+
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 "+
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码  "+
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码)  "+
				"select distinct c.班级名称,c.班级代码  "+
				"from V_成绩管理_登分教师信息表 a  "+
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号   "+
				"left join V_规则管理_学年学期表 d on  d.学年学期编码=b.学年学期编码    "+
				"left join tempClassInfo c on c.班级代码=a.行政班代码 where 1=1  ";
				
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
		if(!"".equalsIgnoreCase(this.getXN())){
			sql += " and d.学年='" +this.getXN() + "'";
		}
		if(!"all".equalsIgnoreCase(this.getXBDM())){
			sql += " and c.系部代码 in ('" + this.getXBDM().replaceAll(",", "','") + "')";
			
		}
		if(!"all".equalsIgnoreCase(this.getNJDM())){
			sql += " and c.年级代码 in ('" + this.getNJDM().replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(this.getSSZY())){
			sql += " and c.专业代码 in ('" + this.getSSZY().replaceAll(",", "','") + "')";
		}
		sql +=	"order by c.班级代码  ";
		BJJVec = db.GetContextVector(sql);	
		if(BJJVec!=null && BJJVec.size()>0){
			for(int i=0; i<BJJVec.size(); i+=2){
				BJVec.add(BJJVec.get(i+1));	
				}
			}
		}else{
			for(int i=0; i<bjList.length; i++){
				BJVec.add(bjList[i]);
			}
		}	
		String filePath = MyTools.getProp(request, "Base.exportExcelPath");
		String title = "学生成绩";
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/"+this.getXN()+"学年补考成绩表 "+".xls";
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
		for(int n=0;n<BJVec.size();n++){
			String tempSql = "";
			Vector titleVec = new Vector();	//存放所有课程
			Vector scoreVec = new Vector();//存放分数
			Vector subVec = new Vector();//查询所有课程
			Vector scoreInfoVec = new Vector();//查询学生成绩
			Vector countInfoVec = new Vector();//查询班级学生
			Vector firstVec = new Vector();//查询上学期课程
			Vector secondVec = new Vector();//查询下学期课程
			Vector xqzbVec =new Vector();//查询学期设置信息
			Vector bjmcmc =new Vector();//查询班级名称
			int type=0;
			String firstSem = "";//上学期
			String secondSem = "";//下学期
			//查询行政班名称
			sql="select 行政班名称,系部代码  from V_学校班级数据子类 where 行政班代码='"+BJVec.get(n)+"'";
			bjmcmc = db.GetContextVector(sql);
			//设置标题
			title=this.getXN()+"学年"+bjmcmc.get(0)+"补考成绩表";
			//查询学期设置信息
			sql="select  系部代码,学期一,学期二," +
					" 计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码='"+bjmcmc.get(1)+"'";
				xqzbVec= db.GetContextVector(sql);
					if(xqzbVec!=null && xqzbVec.size()>0){
						if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
							type=2;//跨学年2
						}else{
							type=1;//同学年有系部
						}
					}else{
						xqzbVec.add("");
						xqzbVec.add("40");
						xqzbVec.add("60");
						xqzbVec.add("1");
						type=0;//同学年无系部
					}
				if(type == 2){//跨学年
					firstSem = MyTools.fixSql(this.getXN()) + "201";
					secondSem = MyTools.parseString((MyTools.StringToInt(this.getXN())+1))+"101";
				}else{//同学年
					firstSem = MyTools.fixSql(this.getXN()) + "101";
					secondSem = MyTools.fixSql(this.getXN()) + "201";
					
				}
				//查询所有学科信息
			/*	sql = "select distinct a.课程代码,a.课程名称 " +
					"from V_成绩管理_登分教师信息表 a " +
					"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
					"left join V_规则管理_学年学期表 c on  b.学年学期编码=c.学年学期编码 "+
					"where a.状态='1' and b.状态='1' and b.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
					"and a.行政班代码='" +BJVec.get(n) + "' " +
					"and a.相关编号 in (select distinct b.相关编号 from V_学生基本数据子类 a left join V_成绩管理_学生成绩信息表 b on a.姓名=b.姓名 " +
					"where a.行政班代码='" +BJVec.get(n) + "') " +
					"order by a.课程代码";*/
				sql = "select distinct b.课程代码,b.课程名称   "+
					"from V_学生基本数据子类 a  "+
					"left join V_成绩管理_登分教师信息表 b on a.行政班代码=b.行政班代码 "+
					"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
					"left join V_学生基本数据子类 d on d.学号=a.学号 "+
					"where c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
					//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+BJVec.get(n)+"') "+
					"and d.行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
					//20170907添加选修课过滤条件yeq
					"and b.来源类型 in ('1','2') " +
					"order by b.课程代码";
				  subVec = db.GetContextVector(sql);
		  
				//查询跨学年的上学期课程
			  sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  " +
				"left join V_学生基本数据子类 d on d.学号=a.学号 "+
				"where c.学年学期编码='" + MyTools.fixSql(firstSem) + "' " +
				//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+BJVec.get(n)+"') "+
				"and d.行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
				//20170907添加选修课过滤条件yeq
				"and b.来源类型 in ('1','2') " +
				"order by b.课程代码";
			  firstVec = db.GetContextVector(sql);
			
			 sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  " +
				"left join V_学生基本数据子类 d on d.学号=a.学号 "+
				"where c.学年学期编码='" + MyTools.fixSql(secondSem) + "' " +
				//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+BJVec.get(n)+"') "+
				"and d.行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
				//20170907添加选修课过滤条件yeq
				"and b.来源类型 in ('1','2') " +
				"order by b.课程代码";
				secondVec = db.GetContextVector(sql);
				
				//查询学生成绩
				sql = "select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
					"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
					"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
					"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
					"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
					"a.相关编号 ,f.平时比例,f.期中比例,f.期末比例,e.学生状态,f.总评比例选项,e.学籍号 "+
					"from V_成绩管理_学生成绩信息表 a " +
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
					"left join V_规则管理_学年学期表 d on  d.学年学期编码=c.学年学期编码 " +
					"left join V_学生基本数据子类 e on e.学号=a.学号 "+
					"left join V_成绩管理_登分设置信息表 f on a.相关编号=f.相关编号 "+
					"where  c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
					//"and a.学号 in (select 学号 from V_学生基本数据子类  where 学生状态 in ('01','05','07','08') and 行政班代码='"+ BJVec.get(n)+"') " +
					"and e.行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
					//20170907添加选修课过滤条件yeq
					"and b.来源类型 in ('1','2') " +
					"order by a.学号,b.课程代码,c.学年学期编码";
				scoreInfoVec = db.GetContextVector(sql);
				
				//查询学业水平测试成绩
				sql="select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   " +
					"from V_自设考试管理_学生成绩信息表 a "+ 
					"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
					"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
					"where 班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
					"and c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
					"and c.类别编号='03' "+
					"order by c.学年学期编码 desc ";	
					xyspVec=db.GetContextVector(sql);
				//存放课程
				if(subVec!=null && subVec.size()>0){
					for(int i=0; i<subVec.size(); i+=2){
						titleVec.add(subVec.get(i+1));	
					}
				}
			//查询学生信息
				sql = "select case when LEFT(班内学号,1)='0' then RIGHT(班内学号,LEN(班内学号)-1) else 班内学号 end as 班内学号  ,姓名,学号 " +
					"from V_学生基本数据子类 where  行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
					"order by cast(班内学号 as int)";
				countInfoVec = db.GetContextVector(sql);
			       
				//对每一个学生遍历增加课程
					if(countInfoVec!=null && countInfoVec.size()>0){
						String curStuCode = "";
						String curSubCode = "";
						boolean existFlag = false;
						for(int i=0; i<countInfoVec.size(); i+=3){
							curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));//学号
							
							for(int j=0; j<subVec.size(); j+=2){
								curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
								existFlag = false;
								//开始
								for(int k=0; k<scoreInfoVec.size(); k+=16){
									String tempXnzp = "";
									String firstScore="";
									String secondScore="";
									if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+2)))){

										boolean first = false;
										boolean second = false;
										boolean firstFlag = false;
										boolean secondFlag = false;
										boolean youwuFlag = true;
										//用来判断上学期是否有课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+8));
										String PS= MyTools.StrFiltr(scoreInfoVec.get(k+5));
										String QZ= MyTools.StrFiltr(scoreInfoVec.get(k+6));
										String QM= MyTools.StrFiltr(scoreInfoVec.get(k+7));
										String psbl= MyTools.StrFiltr(scoreInfoVec.get(k+10));
										String qzbl= MyTools.StrFiltr(scoreInfoVec.get(k+11));
										String qmbl= MyTools.StrFiltr(scoreInfoVec.get(k+12));
										String xsZt=MyTools.StrFiltr(scoreInfoVec.get(k+13));
										String zpBl=MyTools.StrFiltr(scoreInfoVec.get(k+14));
										if("10".equalsIgnoreCase(xsZt)){
											if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
												if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
													zp1 = "";
												}
											}else {
												if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
													zp1 = "";
												}
											}
										}
										
										//上学期和下学期都有
										if(first==true && second==true){
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+24));
											String PS2= MyTools.StrFiltr(scoreInfoVec.get(k+21));
											String QZ2= MyTools.StrFiltr(scoreInfoVec.get(k+22));
											String QM2= MyTools.StrFiltr(scoreInfoVec.get(k+23));
										
											String psbl2= MyTools.StrFiltr(scoreInfoVec.get(k+26));
											String qzbl2= MyTools.StrFiltr(scoreInfoVec.get(k+27));
											String qmbl2= MyTools.StrFiltr(scoreInfoVec.get(k+28));
											String xsZt2=MyTools.StrFiltr(scoreInfoVec.get(k+29));
											String zpBl2=MyTools.StrFiltr(scoreInfoVec.get(k+30));
											if("10".equalsIgnoreCase(xsZt2)){
												if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
													if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
														zp2 = "";
													}
												}else {
													if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
														zp2 = "";
												  }
												}
											}
											firstScore=zp1;
											secondScore=zp2;
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("".equalsIgnoreCase(firstScore) || "作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "-5".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
									
										//上学期
										}else if(first==true && second==false){
											 firstScore=zp1;
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "缓考".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											//下学期
										}else{
											   secondScore=zp1;
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if( "作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
										}
										
										//计算总评
										if(firstFlag==true && secondFlag==true){
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											firstScore="".equalsIgnoreCase(firstScore)?"0":firstScore;
											secondScore="".equalsIgnoreCase(secondScore)?"0":secondScore;
											if(type == 0){
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*0.4+MyTools.StringToDouble(secondScore)*0.6);
											}else{
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*sxqzb/100+MyTools.StringToDouble(secondScore)*xxqzb/100);
											}
											
										}else if(firstFlag==true && secondFlag==false){
												tempXnzp=firstScore;
										}else if(firstFlag==false && secondFlag==true){
												tempXnzp=secondScore;
										}else{
												tempXnzp="";
										}
										//对学业水平成绩遍历
										if(xyspVec!=null && xyspVec.size()>0){
											for(int w=0;w<xyspVec.size();w++){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
													tempXnzp=MyTools.StrFiltr(xyspVec.get(w+2));
													break;
												}
											}
											
										}
										if("".equalsIgnoreCase(tempXnzp)){
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+15)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
											scoreVec.add("");
										}else if(MyTools.StringToDouble(tempXnzp)< 60){
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+15)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
											scoreVec.add(tempXnzp);
										}
								
										existFlag = true;
										break;
									
										/*
										boolean first=false;
										boolean second=false;
										
										//判断是否上学期有改课程
										if(firstVec!=null&&firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first=true;
													break;
												}
											}
										}
										
										//判断是否下学期有改课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												}
											}
									   }
									
									if(first==true&&second==true){
										//两学期都有该课程的情况
										String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
										String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
										Double sxqzb=MyTools.StringToDouble(SXQzb);
										Double xxqzb=MyTools.StringToDouble(XXQzb);
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
										if("作弊".equalsIgnoreCase(zp1)||"取消资格".equalsIgnoreCase(zp1)||"缺考".equalsIgnoreCase(zp1)||"缓考".equalsIgnoreCase(zp1)){
											zp1="0";
										}
										if("作弊".equalsIgnoreCase(zp2)||"取消资格".equalsIgnoreCase(zp2)||"缺考".equalsIgnoreCase(zp2)||"缓考".equalsIgnoreCase(zp2)){
											zp2="0";
										}
										if(("免修".equalsIgnoreCase(zp1)||"免考".equalsIgnoreCase(zp1))&&
												("免修".equalsIgnoreCase(zp2)||"免考".equalsIgnoreCase(zp2))){
										}else if(("免修".equalsIgnoreCase(zp1)||"免考".equalsIgnoreCase(zp1))&&
												(!"免修".equalsIgnoreCase(zp2)||!"免考".equalsIgnoreCase(zp2))){
											if("".equalsIgnoreCase(zp2)){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add("");
											}else {
												if(MyTools.StringToDouble(zp2)<60){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add(zp2);
												}
											}
										}else if(("免修".equalsIgnoreCase(zp2)||"免考".equalsIgnoreCase(zp2))&&
												(!"免修".equalsIgnoreCase(zp1)||!"免考".equalsIgnoreCase(zp1))){
											if("".equalsIgnoreCase(zp1)){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add("");
											}else {
												if(MyTools.StringToDouble(zp1)<60){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add(zp1);
												}
											}
										}else{
											 zp1="".equalsIgnoreCase(zp1)?"0":zp1;
											 zp2="".equalsIgnoreCase(zp2)?"0":zp2;
											if("".equalsIgnoreCase(zp1)&&"".equalsIgnoreCase(zp2)){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
												scoreVec.add("");
										}else if(type==0){
											if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
												scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
											}	 
											}else{
												if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
												}
											}
										
										}
									}else if(first==true&&second==false){//上学期有该课程,下学期没有
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("作弊".equalsIgnoreCase(zp1)||"取消资格".equalsIgnoreCase(zp1)||"缺考".equalsIgnoreCase(zp1)||"缓考".equalsIgnoreCase(zp1)){
											zp1="0";
										}
										if(("免修".equalsIgnoreCase(zp1))||"免考".equalsIgnoreCase(zp1)){
												}else{
													if("".equalsIgnoreCase(zp1)){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
														scoreVec.add("");
													}else if(MyTools.StringToDouble(zp1)<60){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
														scoreVec.add(zp1);
													}
												}
									
											}
									else{//上学期有该课程,下学期没有
										String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("作弊".equalsIgnoreCase(zp2)||"取消资格".equalsIgnoreCase(zp2)||"缺考".equalsIgnoreCase(zp2)||"缓考".equalsIgnoreCase(zp2)){
											zp2="0";
										}
										if(("免修".equalsIgnoreCase(zp2))||"免考".equalsIgnoreCase(zp2)){
											}else{
												if("".equalsIgnoreCase(zp2)){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add("");
												}else if(MyTools.StringToDouble(zp2)<60){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add(zp2);
												}
											}
									  }
									  
										if(first==true&&second==true){//两学期都有该课程的情况
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
											
											//计算学年总评
											if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}
											if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}
											
											if(!"免修".equalsIgnoreCase(zp1) || !"免修".equalsIgnoreCase(zp2)){
												if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp1) < 60.0){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
														scoreVec.add(zp1);
													}
												}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp2) < 60.0){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
														scoreVec.add(zp2);
													}
												}else{
													if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
														scoreVec.add("");
													}else{
														zp1 = "".equalsIgnoreCase(zp1)?"0":zp1;
														zp2 = "".equalsIgnoreCase(zp2)?"0":zp2;
														if(type == 0){
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
																scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
																scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
															}	 
														}else{
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
																scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
																scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
															}
														}
													}
												}
											} 
										}else if(first==true&&second==false){//上学期有该课程,下学期没有
											String zp1 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
											if("免修".equalsIgnoreCase(zp1)){
												zp1 = "60";
											}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}
											
											if(MyTools.StringToDouble(zp1) < 60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
												scoreVec.add(zp1);
											}
										}else{//上学期有该课程,下学期没有
											String zp2 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
											if("免修".equalsIgnoreCase(zp2)){
												zp2 = "60";
											}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}else if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}
											
											if(MyTools.StringToDouble(zp2) < 60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
												scoreVec.add(zp2);
											}
										}
										existFlag = true;
										break;
									*/
										/*
										boolean first=false;
										boolean second=false;
										//判断是否上学期有改课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										
										
								if(first==true&&second==true){//两学期都有该课程的情况
									String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
									String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
									Double sxqzb=MyTools.StringToDouble(SXQzb);
									Double xxqzb=MyTools.StringToDouble(XXQzb);
									String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
									String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
									if("作弊".equalsIgnoreCase(zp1)||"取消资格".equalsIgnoreCase(zp1)||"缺考".equalsIgnoreCase(zp1)||"缓考".equalsIgnoreCase(zp1)){
										zp1="0";
									}
									if("作弊".equalsIgnoreCase(zp2)||"取消资格".equalsIgnoreCase(zp2)||"缺考".equalsIgnoreCase(zp2)||"缓考".equalsIgnoreCase(zp2)){
										zp2="0";
									}
									if(("免修".equalsIgnoreCase(zp1)||"免考".equalsIgnoreCase(zp1))&&
											("免修".equalsIgnoreCase(zp2)||"免考".equalsIgnoreCase(zp2))){
									}else if(("免修".equalsIgnoreCase(zp1)||"免考".equalsIgnoreCase(zp1))&&
											(!"免修".equalsIgnoreCase(zp2)||!"免考".equalsIgnoreCase(zp2))){
										if("".equalsIgnoreCase(zp2)){
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
											scoreVec.add("");
										}else {
											if(MyTools.StringToDouble(zp2)<60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(zp2);
											}
										}
									}else if(("免修".equalsIgnoreCase(zp2)||"免考".equalsIgnoreCase(zp2))&&
											(!"免修".equalsIgnoreCase(zp1)||!"免考".equalsIgnoreCase(zp1))){
										if("".equalsIgnoreCase(zp1)){
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
											scoreVec.add("");
										}else {
											if(MyTools.StringToDouble(zp1)<60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(zp1);
											}
										}
									}else{
										 zp1="".equalsIgnoreCase(zp1)?"0":zp1;
										 zp2="".equalsIgnoreCase(zp2)?"0":zp2;
										if("".equalsIgnoreCase(zp1)&&"".equalsIgnoreCase(zp2)){
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
											scoreVec.add("");
										}else if(type==0){
											if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
											}	 
										}else{
											if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
										
											}
										}
									
									}
									
								}else if(first==true&&second==false){//上学期有该课程,下学期没有
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("作弊".equalsIgnoreCase(zp1)||"取消资格".equalsIgnoreCase(zp1)||"缺考".equalsIgnoreCase(zp1)||"缓考".equalsIgnoreCase(zp1)){
											zp1="0";
										}
										if(("免修".equalsIgnoreCase(zp1))||"免考".equalsIgnoreCase(zp1)){
												}else{
													if("".equalsIgnoreCase(zp1)){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add("");
													}else if(MyTools.StringToDouble(zp1)<60){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add(zp1);
													}
												}
									
											}
									else{//上学期有该课程,下学期没有
										String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("作弊".equalsIgnoreCase(zp2)||"取消资格".equalsIgnoreCase(zp2)||"缺考".equalsIgnoreCase(zp2)||"缓考".equalsIgnoreCase(zp2)){
											zp2="0";
										}
										if(("免修".equalsIgnoreCase(zp2))||"免考".equalsIgnoreCase(zp2)){
												}else{
													if("".equalsIgnoreCase(zp2)){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add("");
													}else if(MyTools.StringToDouble(zp2)<60){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add(zp2);
													}
												}
									
											}
											
										if(first==true&&second==true){//两学期都有该课程的情况
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
											
											//计算学年总评
											if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}
											if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}
											
											if(!"免修".equalsIgnoreCase(zp1) || !"免修".equalsIgnoreCase(zp2)){
												if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp1) < 60.0){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add(zp1);
													}
												}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp2) < 60.0){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add(zp2);
													}
												}else{
													if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add("");
													}else{
														zp1 = "".equalsIgnoreCase(zp1)?"0":zp1;
														zp2 = "".equalsIgnoreCase(zp2)?"0":zp2;
														if(type == 0){
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
																scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
																scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
															}	 
														}else{
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
																scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
																scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
															}
														}
													}
												}
											} 
										}else if(first==true&&second==false){//上学期有该课程,下学期没有
											String zp1 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
											if("免修".equalsIgnoreCase(zp1)){
												zp1 = "60";
											}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}
											
											if(MyTools.StringToDouble(zp1) < 60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(zp1);
											}
										}else{//上学期有该课程,下学期没有
											String zp2 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
											if("免修".equalsIgnoreCase(zp2)){
												zp2 = "60";
											}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}else if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}
											
											if(MyTools.StringToDouble(zp2) < 60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(zp2);
											}
										}
										existFlag = true;
										break;
									*/}
								}
							}
						}
					}
				
				//导出excel	
				this.exportScore( wbook,scoreVec,titleArray,title,n,MyTools.StrFiltr(BJVec.get(n)));
			 }
			wbook.write();
			wbook.close();
			os.close();
			
			this.setMSG("文件生成成功");
			
		} catch (RowsExceededException e1) {
			// TODO Auto-generated catch block
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/**
	 * 导出补考学生成绩信息
	 *@date:2017-06-02
	 * @author:zouyu
	 * @return Vector
	 * @throws SQLException
	 */
	public String exportSingle() throws SQLException{
		String filePath = "";
		String title = "";
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP);  
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/"+this.getXN()+"学年 "+this.getBJBH()+"班补考成绩表 "+".xls";
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			String sql="";
			String tempSql = "";
			Vector titleVec = new Vector();	//存放所有课程
			Vector scoreVec = new Vector();//存放分数
			Vector subVec = new Vector();//查询所有课程
			Vector scoreInfoVec = new Vector();//查询学生成绩
			Vector countInfoVec = new Vector();//查询班级学生
			Vector firstVec = new Vector();//查询上学期课程
			Vector secondVec = new Vector();//查询下学期课程
			Vector xqzbVec = new Vector();//查询学期设置信息
			Vector bjmcmc = new Vector();//查询班级名称
			Vector xyspVec = new Vector();//存放学业水平成绩信息
			String firstSem = "";//上学期
			String secondSem = "";//下学期
			int type=0;
			//查询行政班名称
			sql="select 行政班名称  from V_学校班级数据子类 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "'";
			bjmcmc = db.GetContextVector(sql);
			//设置标题
			title=this.getXN()+"学年"+bjmcmc.get(0)+"补考成绩表";
			//查询学期设置信息
			sql = "select 系部代码,学期一,学期二, 计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
			xqzbVec = db.GetContextVector(sql);
			if(xqzbVec!=null && xqzbVec.size()>0){
				if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
					type=2;//跨学年2
				}else{
					type=1;//同学年有系部
				}
			}else{
				xqzbVec.add("");
				xqzbVec.add("40");
				xqzbVec.add("60");
				xqzbVec.add("");
				type=0;//同学年无系部
			}
			if(type == 2){//跨学年
				firstSem = MyTools.fixSql(this.getXN()) + "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(this.getXN())+1))+"101";
				
			}else{//同学年
				firstSem = MyTools.fixSql(this.getXN()) + "101";
				secondSem = MyTools.fixSql(this.getXN()) + "201";
			}
			
			//查询所有学科信息
			/*sql = "select distinct a.课程代码,a.课程名称 " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"left join V_规则管理_学年学期表 c on  b.学年学期编码=c.学年学期编码 "+
				"where a.状态='1' and b.状态='1' and b.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
				"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"and a.相关编号 in (select distinct b.相关编号 from V_学生基本数据子类 a left join V_成绩管理_学生成绩信息表 b on a.姓名=b.姓名 " +
				"where a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "') " +
				"order by a.课程代码";*/
			sql = "select distinct b.课程代码,b.课程名称   "+
				"from V_学生基本数据子类 a "+
				"left join V_成绩管理_登分教师信息表 b on a.行政班代码=b.行政班代码 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
				"left join V_学生基本数据子类 d on d.学号=a.学号 " +
				"where c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
				//"and a.学号 in (select 学号 from V_学生基本数据子类  where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
				"and d.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				//20170907添加选修课过滤条件yeq
				"and b.来源类型 in ('1','2') " +
				"order by b.课程代码";
			 subVec = db.GetContextVector(sql);
		     
//			sql = "select a.学号,b.课程代码 from V_成绩管理_学生成绩信息表 a "+
//					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
//					"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
//					"left join V_规则管理_学年学期表 d on d.学年学期编码=c.学年学期编码 " +
//					"where d.学年学期编码='" + MyTools.fixSql(firstSem) + "' " +
//					"and b.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
//					"order by a.姓名,a.相关编号,c.学年学期编码";
			 sql = "select a.学号, b.课程代码 "+
				"from V_成绩管理_学生成绩信息表 a  "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  "+
				"left join V_学生基本数据子类 d on d.学号=a.学号 " +
				"where c.学年学期编码='" + MyTools.fixSql(firstSem) + "' " +
				//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
				"and d.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				//20170907添加选修课过滤条件yeq
				"and b.来源类型 in ('1','2') " +
				"order by b.课程代码";
			firstVec = db.GetContextVector(sql);
			
			 sql = "select a.学号, b.课程代码 "+
				"from V_成绩管理_学生成绩信息表 a  "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号  "+
				"left join V_学生基本数据子类 d on d.学号=a.学号 " +
				"where c.学年学期编码='" + MyTools.fixSql(secondSem) + "' " +
				//"and a.学号 in (select 学号 from V_学生基本数据子类 where 学生状态 in ('01','05','07','08') and 行政班代码='"+MyTools.fixSql(this.getBJBH())+"') "+
				"and d.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				//20170907添加选修课过滤条件yeq
				"and b.来源类型 in ('1','2') " +
				"order by b.课程代码";
			secondVec = db.GetContextVector(sql);
					
			//查询该班级学生成绩信息
			sql = "select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
				"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
				"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
				"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
				"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
				"a.相关编号 ,f.平时比例,f.期中比例,f.期末比例,e.学生状态,f.总评比例选项,e.学籍号 "+
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
				"left join V_规则管理_学年学期表 d on  d.学年学期编码=c.学年学期编码 " +
				"left join V_学生基本数据子类 e on e.学号=a.学号 "+
				"left join V_成绩管理_登分设置信息表 f on a.相关编号=f.相关编号 "+
				"where  c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
				//"and a.学号 in (select 学号 from V_学生基本数据子类  where 学生状态 in ('01','05','07','08') and 行政班代码='"+ BJVec.get(n)+"') " +
				"and e.行政班代码='" +  MyTools.fixSql(this.getBJBH()) + "' " +
				//20170907添加选修课过滤条件yeq
				"and b.来源类型 in ('1','2') " +
				"order by a.学号,b.课程代码,c.学年学期编码";
			scoreInfoVec = db.GetContextVector(sql);
			
			//查询学业水平测试成绩
		   sql="select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   " +
				"from V_自设考试管理_学生成绩信息表 a "+ 
				"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
				"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
				"where 班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"and c.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
				"and c.类别编号='03' "+
				"order by c.学年学期编码 desc ";	
			xyspVec=db.GetContextVector(sql);
			//存放课程
			if(subVec!=null && subVec.size()>0){
				for(int i=0; i<subVec.size(); i+=2){
					titleVec.add(subVec.get(i+1));	
				}
			}
			//查询学生信息
			sql = "select case when LEFT(班内学号,1)='0' then RIGHT(班内学号,LEN(班内学号)-1) else 班内学号 end as 班内学号  ,姓名,学号 " +
				"from V_学生基本数据子类 where 行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
				"order by cast(班内学号 as int) ";
			countInfoVec = db.GetContextVector(sql);
			   	//对每一个学生遍历增加课程
					if(countInfoVec!=null && countInfoVec.size()>0){
						String curStuCode = "";
						String curSubCode = "";
						boolean existFlag = false;
						for(int i=0; i<countInfoVec.size(); i+=3){
							curStuCode = MyTools.StrFiltr(countInfoVec.get(i+2));//学号
							for(int j=0; j<subVec.size(); j+=2){
								curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
								existFlag = false;
								//开始
								for(int k=0; k<scoreInfoVec.size(); k+=16){
									String tempXnzp = "";
									String firstScore="";
									String secondScore="";
									if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k))) && curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec.get(k+2)))){
										boolean first = false;
										boolean second = false;
										boolean firstFlag = false;
										boolean secondFlag = false;
										boolean youwuFlag = true;
										//用来判断上学期是否有课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+8));
										String PS= MyTools.StrFiltr(scoreInfoVec.get(k+5));
										String QZ= MyTools.StrFiltr(scoreInfoVec.get(k+6));
										String QM= MyTools.StrFiltr(scoreInfoVec.get(k+7));
										String psbl= MyTools.StrFiltr(scoreInfoVec.get(k+10));
										String qzbl= MyTools.StrFiltr(scoreInfoVec.get(k+11));
										String qmbl= MyTools.StrFiltr(scoreInfoVec.get(k+12));
										String xsZt=MyTools.StrFiltr(scoreInfoVec.get(k+13));
										String zpBl=MyTools.StrFiltr(scoreInfoVec.get(k+14));
										if("10".equalsIgnoreCase(xsZt)){
											if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
												if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
													zp1="";
												}
											}else {
												if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
													zp1="";
											  }
											}
										}
										//上学期和下学期都有
										if(first==true && second==true){
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+24));
											String PS2= MyTools.StrFiltr(scoreInfoVec.get(k+21));
											String QZ2= MyTools.StrFiltr(scoreInfoVec.get(k+22));
											String QM2= MyTools.StrFiltr(scoreInfoVec.get(k+23));
										
											String psbl2= MyTools.StrFiltr(scoreInfoVec.get(k+26));
											String qzbl2= MyTools.StrFiltr(scoreInfoVec.get(k+27));
											String qmbl2= MyTools.StrFiltr(scoreInfoVec.get(k+28));
											String xsZt2=MyTools.StrFiltr(scoreInfoVec.get(k+29));
											String zpBl2=MyTools.StrFiltr(scoreInfoVec.get(k+30));
											if("10".equalsIgnoreCase(xsZt2)){
												if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
													if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
														zp2="";
													}
												}else {
													if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
														zp2="";
												  }
												}
											}
											 firstScore=zp1;
											 secondScore=zp2;
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("".equalsIgnoreCase(firstScore) || "作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "-5".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
									
										//上学期
										}else if(first==true && second==false){
											 firstScore=zp1;
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "缓考".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											//下学期
										}else{
											   secondScore=zp1;
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if( "作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
										}
										
										//计算总评
										if(firstFlag==true && secondFlag==true){
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											firstScore="".equalsIgnoreCase(firstScore)?"0":firstScore;
											secondScore="".equalsIgnoreCase(secondScore)?"0":secondScore;
											if(type == 0){
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*0.4+MyTools.StringToDouble(secondScore)*0.6);
											}else{
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*sxqzb/100+MyTools.StringToDouble(secondScore)*xxqzb/100);
											}
											
										}else if(firstFlag==true && secondFlag==false){
												tempXnzp=firstScore;
										}else if(firstFlag==false && secondFlag==true){
												tempXnzp=secondScore;
										}else{
												tempXnzp="";
										}
										//对学业水平成绩遍历
										if(xyspVec!=null && xyspVec.size()>0){
											for(int w=0;w<xyspVec.size();w++){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec.get(w+1)))){
													tempXnzp=MyTools.StrFiltr(xyspVec.get(w+2));
													break;
												}
											}
											
										}
										if("".equalsIgnoreCase(tempXnzp)){
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+15)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
											scoreVec.add("");
										}else if(MyTools.StringToDouble(tempXnzp)< 60){
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+15)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
											scoreVec.add(tempXnzp);
										}
								
										existFlag = true;
										break;
									
										/*
											boolean first=false;
											boolean second=false;
											//判断是否上学期有改课程
											if(firstVec!=null&&firstVec.size()>0){
												for(int s=0; s<firstVec.size(); s+=2){
													if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
														first=true;
														break;
													}
												}
											}
											//判断是否下学期有改课程
											if(secondVec!=null&&secondVec.size()>0){
												for(int x=0; x<secondVec.size();x+=2){
													if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
														second=true;
														break;
													}
												}
										}
									if(first==true&&second==true){
										//两学期都有该课程的情况
										String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
										String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
										Double sxqzb=MyTools.StringToDouble(SXQzb);
										Double xxqzb=MyTools.StringToDouble(XXQzb);
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
										
										//计算学年总评
										if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
											zp1 = "0";
										}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
											zp1 = "";
										}
										if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
											zp2 = "0";
										}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
											zp2 = "";
										}
										
										if(!"免修".equalsIgnoreCase(zp1) || !"免修".equalsIgnoreCase(zp2)){
											if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
												if(MyTools.StringToDouble(zp1) < 60.0){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add(zp1);
												}
											}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
												if(MyTools.StringToDouble(zp2) < 60.0){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add(zp2);
												}
											}else{
												if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
													scoreVec.add("");
												}else{
													zp1 = "".equalsIgnoreCase(zp1)?"0":zp1;
													zp2 = "".equalsIgnoreCase(zp2)?"0":zp2;
													if(type == 0){
														if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
															scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
															scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
															scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
															scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
														}	 
													}else{
														if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
															scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
															scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
															scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
															scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
														}
													}
												}
											}
										} 
									}else if(first==true&&second==false){//上学期有该课程,下学期没有
										String zp1 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("免修".equalsIgnoreCase(zp1)){
											zp1 = "60";
										}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
											zp1 = "";
										}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
											zp1 = "0";
										}
										
										if(MyTools.StringToDouble(zp1) < 60){
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
											scoreVec.add(zp1);
										}
									}else{//上学期有该课程,下学期没有
										String zp2 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("免修".equalsIgnoreCase(zp2)){
											zp2 = "60";
										}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
											zp2 = "";
										}else if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
											zp2 = "0";
										}
										
										if(MyTools.StringToDouble(zp2) < 60){
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
											scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
											scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+3)));
											scoreVec.add(zp2);
										}
									}
									existFlag = true;
									break;
								*/
										/*
										boolean first=false;
										boolean second=false;
										//判断是否上学期有改课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curStuCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x)))&&curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										
										
									if(first==true&&second==true){
										//两学期都有该课程的情况
										String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
										String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
										Double sxqzb=MyTools.StringToDouble(SXQzb);
										Double xxqzb=MyTools.StringToDouble(XXQzb);
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
										if("作弊".equalsIgnoreCase(zp1)||"取消资格".equalsIgnoreCase(zp1)||"缺考".equalsIgnoreCase(zp1)||"缓考".equalsIgnoreCase(zp1)){
											zp1="0";
										}
										if("作弊".equalsIgnoreCase(zp2)||"取消资格".equalsIgnoreCase(zp2)||"缺考".equalsIgnoreCase(zp2)||"缓考".equalsIgnoreCase(zp2)){
											zp2="0";
										}
										if(("免修".equalsIgnoreCase(zp1)||"免考".equalsIgnoreCase(zp1))&&
												("免修".equalsIgnoreCase(zp2)||"免考".equalsIgnoreCase(zp2))){
										}else if(("免修".equalsIgnoreCase(zp1)||"免考".equalsIgnoreCase(zp1))&&
												(!"免修".equalsIgnoreCase(zp2)||!"免考".equalsIgnoreCase(zp2))){
											if("".equalsIgnoreCase(zp2)){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add("");
											}else {
												if(MyTools.StringToDouble(zp2)<60){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
													scoreVec.add(zp2);
												}
											}
										}else if(("免修".equalsIgnoreCase(zp2)||"免考".equalsIgnoreCase(zp2))&&
												(!"免修".equalsIgnoreCase(zp1)||!"免考".equalsIgnoreCase(zp1))){
											if("".equalsIgnoreCase(zp1)){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add("");
											}else {
												if(MyTools.StringToDouble(zp1)<60){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
													scoreVec.add(zp1);
												}
											}
										}else{
												 zp1="".equalsIgnoreCase(zp1)?"0":zp1;
												 zp2="".equalsIgnoreCase(zp2)?"0":zp2;
												if("".equalsIgnoreCase(zp1)&&"".equalsIgnoreCase(zp2)){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
													scoreVec.add("");
											}else if(type==0){
												if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
													scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
												}	 
											}else{
												if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
													scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
													scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
													scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
												}
											}											
										}
										
									
									}else if(first==true&&second==false){//上学期有该课程,下学期没有
										String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("作弊".equalsIgnoreCase(zp1)||"取消资格".equalsIgnoreCase(zp1)||"缺考".equalsIgnoreCase(zp1)||"缓考".equalsIgnoreCase(zp1)){
											zp1="0";
										}
										if(("免修".equalsIgnoreCase(zp1))||"免考".equalsIgnoreCase(zp1)){
												}else{
													if("".equalsIgnoreCase(zp1)){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add("");
													}else if(MyTools.StringToDouble(zp1)<60){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add(zp1);
													}
												}
									}else{//上学期有该课程,下学期没有
										String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+5));
										if("作弊".equalsIgnoreCase(zp2)||"取消资格".equalsIgnoreCase(zp2)||"缺考".equalsIgnoreCase(zp2)){
											zp2="0";
										}
										if(("免修".equalsIgnoreCase(zp2))||"免考".equalsIgnoreCase(zp2)){
										}else{
											if("".equalsIgnoreCase(zp2)){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add("");
											}else if(MyTools.StringToDouble(zp2)<60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(zp2);
											}
										}
									}
								
										if(first==true&&second==true){
											//两学期都有该课程的情况
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											String zp1=MyTools.StrFiltr(scoreInfoVec.get(k+5));
											String zp2=MyTools.StrFiltr(scoreInfoVec.get(k+11));
											
											//计算学年总评
											if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}
											if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}
											
											if(!"免修".equalsIgnoreCase(zp1) || !"免修".equalsIgnoreCase(zp2)){
												if(!"免修".equalsIgnoreCase(zp1) && "免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp1) < 60.0){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add(zp1);
													}
												}else if("免修".equalsIgnoreCase(zp2) && !"免修".equalsIgnoreCase(zp2)){
													if(MyTools.StringToDouble(zp2) < 60.0){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add(zp2);
													}
												}else{
													if("".equalsIgnoreCase(zp1) && "".equalsIgnoreCase(zp2)){
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
														scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
														scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
														scoreVec.add("");
													}else{
														zp1 = "".equalsIgnoreCase(zp1)?"0":zp1;
														zp2 = "".equalsIgnoreCase(zp2)?"0":zp2;
														if(type == 0){
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6))<60.0){
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
																scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
																scoreVec.add(f.format(MyTools.StringToDouble(zp1)*0.4+MyTools.StringToDouble(zp2)*0.6));
															}	 
														}else{
															if(MyTools.StringToDouble(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100))<60.0){
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
																scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
																scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
																scoreVec.add(f.format(MyTools.StringToDouble(zp1)*sxqzb/100+MyTools.StringToDouble(zp2)*xxqzb/100));
															}
														}
													}
												}
											} 
										}else if(first==true&&second==false){//上学期有该课程,下学期没有
											String zp1 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
											if("免修".equalsIgnoreCase(zp1)){
												zp1 = "60";
											}else if("缓考".equalsIgnoreCase(zp1) || "免考".equalsIgnoreCase(zp1)){
												zp1 = "";
											}else if("作弊".equalsIgnoreCase(zp1) || "取消资格".equalsIgnoreCase(zp1) || "缺考".equalsIgnoreCase(zp1)){
												zp1 = "0";
											}
											
											if(MyTools.StringToDouble(zp1) < 60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(zp1);
											}
										}else{//上学期有该课程,下学期没有
											String zp2 = MyTools.StrFiltr(scoreInfoVec.get(k+5));
											if("免修".equalsIgnoreCase(zp2)){
												zp2 = "60";
											}else if("缓考".equalsIgnoreCase(zp2) || "免考".equalsIgnoreCase(zp2)){
												zp2 = "";
											}else if("作弊".equalsIgnoreCase(zp2) || "取消资格".equalsIgnoreCase(zp2) || "缺考".equalsIgnoreCase(zp2)){
												zp2 = "0";
											}
											
											if(MyTools.StringToDouble(zp2) < 60){
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+2)));
												scoreVec.add(MyTools.StrFiltr(countInfoVec.get(i+1)));
												scoreVec.add(MyTools.StrFiltr(subVec.get(j+1)));
												scoreVec.add(zp2);
											}
										}
										existFlag = true;
										break;
									*/}
								}
							}
						}
					}
			String[] titleArray = new String[]{"学籍号","姓名","课程名称","学年总评"};
			this.exportScore(wbook,scoreVec,titleArray,title,0,MyTools.StrFiltr(this.getBJBH()));
			
			wbook.write();
			wbook.close();
			os.close();
			this.setMSG("文件生成成功");
		} catch (RowsExceededException e1) {
			// TODO Auto-generated catch block
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	
	}
	
	/**
	 * 导出成绩xls
	 * @date:2017-6-2
	 * @author:zouyu
	* @throws WriteException, FileNotFoundException 
	 */
	public void exportScore(WritableWorkbook wbook, Vector scoreVec, String[] titleArray, String title,int a,String sheet) throws IOException, WriteException, FileNotFoundException{
		WritableSheet wsheet = wbook.createSheet(sheet, a);//工作表名称
		WritableFont fontStyle;
		WritableCellFormat contentStyle;
		Label content;
		wsheet.setColumnView(0, 25);
		wsheet.setColumnView(1, 15);
		wsheet.setColumnView(2, 45);
		wsheet.setColumnView(3,12);
		wsheet.setRowView(1,500);
		//标题
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 20, WritableFont.NO_BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
		contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
		wsheet.mergeCells(0, 0, titleArray.length-1, 0);
		content = new Label(0, 0, title, contentStyle);
		wsheet.addCell(content);
		
		
			//表格标题行
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		/*contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
		contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);*/
		for (int i=0; i<titleArray.length; i++) {
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			contentStyle.setWrap(true);// 自动换行
			contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			if(i==titleArray.length-1){
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
			}else{
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			}
			content = new Label(i, 1, titleArray[i], contentStyle);
			wsheet.addCell(content);
		}
		
		//表格内容
		//k:用于循环时Excel的行号
		//外层for用于循环行,内曾for用于循环列
		for (int i=0, k=2; i<scoreVec.size(); i+=titleArray.length, k++) {
			for (int j=0; j<titleArray.length; j++) {
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setWrap(true);// 自动换行
				contentStyle.setShrinkToFit(true);//字体大小自适应
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				if(j == titleArray.length-1){
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				}
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				if(i+titleArray.length==scoreVec.size()){
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}
				content = new Label(j, k, MyTools.StrFiltr(scoreVec.get(i+j)), contentStyle);
				wsheet.addCell(content);
			}
			wsheet.setRowView(k,400);
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
	public String getXN() {
		return XN;
	}

	public void setXN(String xN) {
		XN = xN;
	}

}