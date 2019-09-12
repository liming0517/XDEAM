package com.pantech.devolop.registerScoreQuery;
/**
 * 批量导出补考学生成绩信息
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


public class LjmdBean {
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
	private String SFLJ;//是否留级

	private String XH;//学号
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public LjmdBean(HttpServletRequest request) {
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
		sql="with tempClassInfo as (select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号  "+
			"from V_学校班级数据子类 a  "+	 
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码  "+	
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码  "+	
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码  "+	
			"union all " +
			"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号  "+	
			"from V_基础信息_教学班信息表 a  "+	 
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码  "+	
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码  "+	
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码) "+	
			"select distinct d.学年,c.班级代码,c.班级名称,c.系部代码,c.系部名称,c.年级代码,c.年级名称,c.专业代码,c.专业名称,(case when ISNUMERIC(dd.sl)<>1 then '0' else dd.sl end) as 留级人数  "+	
			"from V_成绩管理_登分教师信息表 a  "+
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 d on d.学年学期编码=b.学年学期编码 " +
			"inner join tempClassInfo c on c.班级代码=a.行政班代码  "+	
			"left join (select count(*) as sl,t.班级代码,t.学年 from ("+
			"select b.班级代码,a.学号,b.学年  from V_成绩管理_学生补考成绩信息表  a  "+ 
			"left join V_成绩管理_补考登分教师信息表 b on b.编号=a.补考编号  "+
			"where a.补考<>'-9' and a.补考<>'-13' and cast(a.补考 as int)<60 group by b.班级代码,a.学号,b.学年 having(COUNT(a.学号))>=3 ) t "+
			"group by t.班级代码,t.学年) dd on dd.班级代码=a.行政班代码 and dd.学年=d.学年 "+
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
				sql += "c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		
		if(!"".equalsIgnoreCase(this.getXN())){
			sql += " and  d.学年='" + MyTools.fixSql(this.getXN()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getBJBH())){
			sql += " and c.班级代码 like '%" + MyTools.fixSql(this.getBJBH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getSFLJ())){
			if("0".equalsIgnoreCase(this.getSFLJ())){
				sql += " and  ISNUMERIC(dd.sl)<>1  ";
			}else{
				sql += " and  ISNUMERIC(dd.sl)=1  ";
			}
		}
		if(!"".equalsIgnoreCase(this.getXBDM())){
			sql += " and c.系部代码='" + MyTools.fixSql(this.getXBDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getNJDM())){
			sql += " and c.年级代码='" + MyTools.fixSql(this.getNJDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getSSZY())){
			sql += " and c.专业代码='" + MyTools.fixSql(this.getSSZY()) + "' ";
		}
		sql += " order by  d.学年  desc,c.班级名称";
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
		sql="select  系部代码, 学期一 , 学期二, 计算方式 " +
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
		
		sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum "+
			"union all "+
			"select distinct a.课程名称 as comboName,a.课程代码  as comboValue,'1' as orderNum  "+
			"from V_成绩管理_登分教师信息表 a "+
			"left join V_成绩管理_科目课程信息表 b on "+
			"b.科目编号=a.科目编号 "+
			"left join V_规则管理_学年学期表 c on  b.学年学期编码=c.学年学期编码 "+
			"where a.状态='1' and b.状态='1' and b.学年学期编码 in ('" + firstSem + "','" + secondSem + "') " +
			"and a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			"and a.相关编号 in (select distinct b.相关编号 from V_学生基本数据子类 a "+
			"left join V_成绩管理_学生成绩信息表 b on a.姓名=b.姓名  where a.行政班代码='" + MyTools.fixSql(this.getBJBH()) + "') "+			
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
		sql="with tempClassInfo as (select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号   "+
			"from V_学校班级数据子类 a   "+	
			"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码  "+	
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 "+	  
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码   "+	
			"union all select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,b.系部代码,b.系部名称,c.年级代码,c.年级名称,d.专业代码,d.专业名称,a.班主任工号 "+	  
			"from V_基础信息_教学班信息表 a  left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码   "+	
			"left join V_学校年级数据子类 c on c.年级代码=a.年级代码   "+	
			"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码) "+	
			"select '全部' as comboName,'all' as comboValue,'0' as orderNum union all   "+
			"select  distinct c.班级名称,c.班级代码 ,'1' as orderNum  "+ 
			"from V_成绩管理_补考登分教师信息表 a   "+ 
			"left join V_成绩管理_学生补考成绩信息表 b on a.编号=b.补考编号  "+ 
			"left join tempClassInfo c on c.班级代码=a.班级代码  "+ 
			"where 1=1 and b.补考<>'-9' and b.补考<>'-13' and cast(b.补考 as int)<60 ";
				
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
				sql += "c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		if(!"".equalsIgnoreCase(xn)){
			sql += " and a.学年='" +xn + "'";
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
		sql +=	" group by c.班级名称,c.班级代码 having (COUNT(b.学号))>=3 order by orderNum,comboName";
		
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
	public Vector initStuScoreInfo()throws SQLException{
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql="SELECT a.补考编号,b.课程代码,b.课程名称," +
			"(case when a.补考='' then '' when a.补考='-4' then '缺考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格' when a.补考='-13' then '补及'  when cast(a.补考 as int )<60 then '不及格'  when cast(a.补考 as int )>=60 then '及格' else a.补考 end ) as 补考," +
			"(case when a.学年总评='-2' then '作弊' when a.学年总评='-3' then '取消资格' when a.学年总评='-4' then '缺考' when a.学年总评='-5' then '缓考' else a.学年总评 end) as 学年总评 " +
			"FROM V_成绩管理_学生补考成绩信息表 a "+
			"left join V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+	
			"where 1=1  ";
		sql +="and a.学号='" + MyTools.fixSql(this.getXH()) + "' ";
		sql +="and b.学年 = '" + MyTools.fixSql(this.getXN()) + "' ";
		sql +="and b.班级代码 = '" + MyTools.fixSql(this.getBJBH()) + "'";
		sql+="and a.补考<>'-9' and a.补考<>'-13' and cast(a.补考 as int)<60 order by a.补考编号";
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		return vec;
	}
	
	
	/**
	 * 查询学生成绩信息
	 * @date:2017-05-04
	 * @author:zouyu
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadStuInfo()throws SQLException{
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select case when LEFT(c.班内学号,1)='0' then RIGHT(c.班内学号,LEN(c.班内学号)-1) else c.班内学号 end as 班号,a.姓名,c.学籍号,a.学号,COUNT(*) as 不及格科目数量 "+
			 "from V_成绩管理_学生补考成绩信息表 a "+
			 "left join V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+
			 "left join V_学生基本数据子类 c on c.学号=a.学号  and c.学生状态 in ('01','05','07','08') " +
			 "where b.学年='" + MyTools.fixSql(this.getXN()) + "' " +
			 "and b.班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			 "and a.补考<>'-9' and a.补考<>'-13' and cast(a.补考 as int)<60 " +
			 "group by a.学号,c.学籍号,a.姓名,班内学号  having(COUNT(a.学号))>=3 " +
			 "order by 班号 ";
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		return vec;
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
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP); 
		String[] titleArray = new String[]{"班号","姓名","学籍号","不及格科目数量","不及格科目名称"};
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
				"select  distinct c.班级名称,c.班级代码  "+ 
				"from V_成绩管理_补考登分教师信息表 a   "+ 
				"left join V_成绩管理_学生补考成绩信息表 b on a.编号=b.补考编号  "+ 
				"left join tempClassInfo c on c.班级代码=a.班级代码  "+ 
				"where b.补考<>'-9' and b.补考<>'-13' and cast(b.补考 as int)<60 ";
				
				/*"select distinct c.班级名称,c.班级代码  "+
				"from V_成绩管理_登分教师信息表 a  "+
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号   "+
				"left join V_规则管理_学年学期表 d on  d.学年学期编码=b.学年学期编码    "+
				"left join tempClassInfo c on c.班级代码=a.行政班代码 where 1=1  ";*/
				
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
			sql += " and a.学年='" +this.getXN() + "'";
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
		sql += " group by c.班级名称,c.班级代码 having (COUNT(b.学号))>=3 order by c.班级代码";
		
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
		
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/"+this.getXN()+"学年留级信息表 "+".xls";
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			for(int n=0;n<BJVec.size();n++){
				Vector resultVec = new Vector();//存放分数
				Vector scoreInfoVec = new Vector();//查询学生成绩
				Vector bjmcmc = new Vector();//查询学生成绩
				//查询行政班名称
				sql = "select 行政班名称  from V_学校班级数据子类 where 行政班代码='"+BJVec.get(n)+"'";
				bjmcmc = db.GetContextVector(sql);
				//设置标题
				String title=this.getXN()+"学年"+MyTools.StrFiltr(bjmcmc.get(0))+"留级学生信息";
				
				//查询学生成绩//case when LEFT(c.班内学号,1)='0' then RIGHT(c.班内学号,LEN(c.班内学号)-1) else c.班内学号 end as 班号
				sql = "select distinct case when LEFT(c.班内学号,1)='0' then RIGHT(c.班内学号,LEN(c.班内学号)-1) else c.班内学号 end as 班号,a.姓名,c.学籍号,COUNT(*) as 不及格科目数量,"+
					"stuff((select '、' +d.课程名称 from V_成绩管理_学生补考成绩信息表 c "+
					"left join  V_成绩管理_补考登分教师信息表 d  on c.补考编号=d.编号 "+
					"where c.学号=a.学号   and d.学年='" + MyTools.fixSql(this.getXN()) + "' " +
					"and d.班级代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
					"and c.补考<>'-9' and c.补考<>'-13' and cast(c.补考 as int)<60 for xml path('')),1,1,'') as 不及格科目名称 "+ 
					"from V_成绩管理_学生补考成绩信息表 a "+ 
					"left join V_成绩管理_补考登分教师信息表 b  on a.补考编号=b.编号 "+ 
					"left join V_学生基本数据子类 c on c.学号=a.学号  and c.学生状态 in ('01','05','07','08') " + 
					"where b.学年='" + MyTools.fixSql(this.getXN()) + "' " +
					"and b.班级代码='" + MyTools.fixSql(MyTools.StrFiltr(BJVec.get(n))) + "' " +
					"and a.补考<>'-9' and a.补考<>'-13' and cast(a.补考 as int)<60  "+
					"group by c.班内学号,c.学籍号,a.学号,a.姓名 having(COUNT(a.学号))>=3 order by 班号 ";
				scoreInfoVec = db.GetContextVector(sql);
				
				//对每一个学生遍历增加课程
				if(scoreInfoVec!=null && scoreInfoVec.size()>0){
					for(int i=0; i<scoreInfoVec.size(); i+=5){
						resultVec.add(scoreInfoVec.get(i));
						resultVec.add(scoreInfoVec.get(i+1));
						resultVec.add(scoreInfoVec.get(i+2));
						resultVec.add(scoreInfoVec.get(i+3));
						resultVec.add(scoreInfoVec.get(i+4));
					}
				}
				
				//导出excel	
				this.exportScore(wbook,resultVec,titleArray,title,n,MyTools.StrFiltr(BJVec.get(n)));
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
		String sql="";
		Vector titleVec = new Vector();	//存放所有课程
		Vector scoreInfoVec = new Vector();//查询学生成绩
		Vector resultVec = new Vector();
		String[] titleArray = new String[]{"班号","姓名","学号","不及格科目数量","不及格科目名称"};
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath += "/"+this.getXN()+"学年 "+this.getBJBH()+"班留级学生信息 "+".xls";
		title=this.getXN()+"学年"+MyTools.fixSql(this.getBJMC())+"留级学生信息";
		
		//查询该班级学生成绩信息
		sql = "select distinct case when LEFT(c.班内学号,1)='0' then RIGHT(c.班内学号,LEN(c.班内学号)-1) else c.班内学号 end as 班号,a.姓名,c.学籍号,COUNT(*) as 不及格科目数量,"+
			"stuff((select '、'+d.课程名称 from V_成绩管理_学生补考成绩信息表 c "+
			"left join V_成绩管理_补考登分教师信息表 d on c.补考编号=d.编号 "+
			"where c.学号=a.学号 and d.学年='" + MyTools.fixSql(this.getXN()) + "' " +
			"and d.班级代码='" + MyTools.fixSql(this.getBJBH()) + "' " +
			"and c.补考<>'-9' and c.补考<>'-13' and cast(c.补考 as int)<60 for xml path('')),1,1,'' ) as 不及格科目名称 " + 
			"from V_成绩管理_学生补考成绩信息表 a " + 
			"left join V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 " + 
			"left join V_学生基本数据子类 c on c.学号=a.学号  and c.学生状态 in ('01','05','07','08') " + 
			"where b.学年='" + MyTools.fixSql(this.getXN()) + "' " +
			"and b.班级代码='"+MyTools.fixSql(this.getBJBH()) + "' " +
			"and a.补考<>'-9' and a.补考<>'-13' and cast(a.补考 as int)<60 " +
			"group by c.班内学号,c.学籍号,a.学号,a.姓名 having(COUNT(c.学籍号))>=3 order by 班号 ";
		scoreInfoVec = db.GetContextVector(sql);
		
		if(scoreInfoVec!=null && scoreInfoVec.size()>0){
			for(int i=0; i<scoreInfoVec.size(); i+=5){
				resultVec.add(scoreInfoVec.get(i));
				resultVec.add(scoreInfoVec.get(i+1));
				resultVec.add(scoreInfoVec.get(i+2));
				resultVec.add(scoreInfoVec.get(i+3));
				resultVec.add(scoreInfoVec.get(i+4));
			}
		}
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			this.exportScore(wbook, resultVec,  titleArray, title, 0, this.getBJBH());
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
		wsheet.setColumnView(0, 5);
		wsheet.setColumnView(1, 12);
		wsheet.setColumnView(2, 25);
		wsheet.setColumnView(3,16);
		wsheet.setColumnView(4,40);
		wsheet.setRowView(1,400);
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
			wsheet.setRowView(k,600);
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
	public String getXH() {
		return XH;
	}

	public void setXH(String xH) {
		XH = xH;
	}
	public String getSFLJ() {
		return SFLJ;
	}

	public void setSFLJ(String sFLJ) {
		SFLJ = sFLJ;
	}
}