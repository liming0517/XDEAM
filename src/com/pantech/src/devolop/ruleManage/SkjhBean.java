package com.pantech.src.devolop.ruleManage;
/*
@date 2015.06.02
@author wangzh
模块：M1.2授课计划
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;

public class SkjhBean {
	private String GS_SKJHMXBH;//授课计划明细编号
	private String GS_SKJHZBBH;//授课计划主表编号
	private String GS_KCDM;//课程代码
	private String GS_KCMC;//课程名称
	private String GS_KCMCDM;//保存的课程代码
	private String GS_KCLX;//课程类型
	private String GS_KSXS;//考试形式
	private String GS_SKJSBH;//授课教师编号
	private String GS_SKJSXM;//授课教师姓名
	private String GS_JS;//节数
	private String GS_GPYPJS;//固排已排节数
	private String GS_SJYPJS;//实际已排节数
	private String GS_LJ;//连节
	private String GS_LC;//连次
	private String GS_GPLCCS;//固排连次次数
	private String GS_SJLCCS;//实际连次次数
	private String GS_CDYQ;//场地要求
	private String GS_CDMC;//场地名称
	private String GS_SKZC;//授课周次
	private String GS_SKZCXQ;//授课周次详情
	private String GS_ZT;//状态
	private String GS_XNXQBM;//学年学期编码
	private String GS_XZBDM;//行政班代码
	private String GS_ZYDM;//专业代码
	private String GS_ZYMC;//专业名称
	private String GS_XF;//学分
	private String GS_ZKS;//总课时
	
	private String XZBDM;//行政班代码
	private String XNXQ;//学年学期
	private String JXXZ;//教学性质
	private String JSKCS;//当前教师课程数
	private String SKJSBH;//授课教师编号
	private String SKJSBHXM;//授课教师姓名
	private String SKSL;//授课数量
	private String XKDM;//学科代码
	private String iUSERCODE;//用户编号
	private String GS_XNXQ2;//编辑授课计划中的学年学期
	private String GS_SFKS;//是否考试
	private String useType;//保存类型
	
	private HttpServletRequest request;
	private DBSource db;
	private String USERCODE;
	private String AUTHCODE;
	private String MSG;  //提示信息
	private String MSG2;  //提示信息
	private String TATOL;  //提示信息
	String sqlsel="select '' as comboValue,'请选择' as comboName union ";
	
	/**
	 * 构造函数
	 * @param request
	 */
	public SkjhBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-06-02
	 * @author:wangzh
	 */
	public void initialData() {
		GS_SKJHMXBH = "";//授课计划明细编号
		GS_SKJHZBBH = "";//授课计划主表编号
		GS_KCDM = "";//课程代码
		GS_KCMC = "";//课程名称
		GS_SKJSBH = "";//授课教师编号
		GS_SKJSXM = "";//授课教师姓名
		GS_JS = "";//节数
		GS_GPYPJS = "";//固排已排节数
		GS_SJYPJS = "";//实际已排节数
		GS_LJ = "";//连节
		GS_LC = "";//连次
		GS_GPLCCS = "";//固排连次次数
		GS_SJLCCS = "";//实际连次次数
		GS_CDYQ = "";//场地要求
		GS_CDMC = "";//场地名称
		GS_SKZC = "";//授课周次
		GS_SKZCXQ = "";//授课周次详情
		GS_ZT = "";//状态
		MSG = ""; //提示信息
		GS_XNXQBM = ""; //学年学期编码
		GS_XZBDM = ""; //行政班代码
		GS_ZYDM = ""; //专业代码
		GS_ZYMC = ""; //专业名称
		
		XZBDM = "";//行政班代码
		XNXQ = "";//学年学期
		JXXZ = "";//教学性质
		JSKCS = "";//当前教师课程数
		SKJSBH = "";//授课教师编号
		SKJSBHXM = "";//授课教师姓名
		SKSL = "";//授课数量
		XKDM = "";//学科代码
		USERCODE = "";
		AUTHCODE = "";
	}
	
	/**
	 * 
	 * @date:2017-01-03
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void checkzbmx() throws SQLException{
		//添加第一条信息
		String sqlchkzb="select count(*) from dbo.V_规则管理_授课计划主表 ";
		String sqlchkmx="select count(*) from dbo.V_规则管理_授课计划明细表 ";
		String sqlchkins="";
		Vector vecchk=new Vector();
		int chk=0;
		if(!db.getResultFromDB(sqlchkzb)){
			sqlchkins="insert into dbo.V_规则管理_授课计划主表  ([授课计划主表编号],[学年学期编码],[行政班代码],[专业代码],[专业名称],[创建人],[创建时间],[状态]) values ('SKJH_10000001','2000101','11111','111','111','post',getdate(),'1') ";
			vecchk.add(sqlchkins);
			chk=1;
		}
		if(!db.getResultFromDB(sqlchkmx)){
			sqlchkins="insert into dbo.V_规则管理_授课计划明细表  ([授课计划明细编号],[授课计划主表编号],[课程代码],[课程名称],[课程类型],[授课教师编号],[授课教师姓名],[节数],[固排已排节数],[实际已排节数],[连节],[连次],[固排连次次数],[实际连次次数],[场地要求],[场地名称],[授课周次],[授课周次详情],[创建人],[创建时间],[状态],[考试形式],[学分],[课程属性],[课程分类],[课程性质],[总课时],[实验实训],[是否考试]) values " +
					"('SKJHMX_10000000001','SKJH_10000001','11111','11111','1','000','000','1','0','0','0','0','0','0','0000','0000','1','1','post',getdate(),'1','0','1','','','','10','0','2') ";
			vecchk.add(sqlchkins);
			chk=1;
		}
		if(chk==1){
			db.executeInsertOrUpdateTransaction(vecchk);
		}

	}
	
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryTree(int pageNum, int page,String parentId,String level) throws SQLException {
		String sql = ""; // 查询用SQL语句
		String sql1 = ""; // 查询用SQL语句
		String sql2 = ""; // 查询用SQL语句
		String sql3 = ""; // 查询用SQL语句
		String sql4 = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		String beginTime = "";
		String endTime = "";
		String user1="";
		String user2="";
		String zydm="";
		String adminAuth = MyTools.getProp(request, "Base.admin");
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");
		
		sql4 = "select 专业代码 from dbo.V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getiUSERCODE()) + "@%'";
		vec4=db.GetContextVector(sql4);
		if(vec4!=null&&vec4.size()>0){
			if(this.iUSERCODE.equals("post")){
				
			}else{
				for(int i=0;i<vec4.size();i++){
					zydm+="'"+vec4.get(i).toString()+"',";
				}
				zydm=zydm.substring(0, zydm.length()-1);
				user1="and c.专业代码 in ("+zydm+") ";
				user2="and b.专业代码 in ("+zydm+") ";
			}
		}
		
		String nj=this.getGS_XNXQBM().substring(2,4);
		System.out.println(this.getAUTHCODE()+"|"+adminAuth+"|"+qxjwgl);
		if(level.equals("0")){
			//sql = "select c.专业代码 as id,c.专业名称+c.专业代码 as text,case when (select count(*) from V_学校班级数据子类 where c.专业代码=专业代码 )=0 then 'open' else 'closed' end as state from V_专业基本信息数据子类 c where len(c.专业代码)>4 "+user1+" order by text " ;
			sql = "select 系部代码 as id,系部名称 as text,'closed' as state from V_基础信息_系部信息表 where 系部代码<>'C00' " ;	 
			if(("@"+this.getAUTHCODE()+"@").indexOf(adminAuth)<0 && ("@"+this.getAUTHCODE()+"@").indexOf(qxjwgl)<0){
				sql += " and 系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql+= "union select 'gfb' as id,'高复班' as text,'closed' as state ";
		}
		if(level.equals("1")){
//			sql = "select b.行政班代码 as id,b.行政班名称 as text,'open' as state " +
//				"from V_专业基本信息数据子类 a right join V_学校班级数据子类 b on a.专业代码=b.专业代码  " +
//				"where 1=1 and "+nj+"-substring(b.行政班代码,1,2)>=0 and "+nj+"-substring(b.行政班代码,1,2)<3 and b.专业代码='"+MyTools.fixSql(parentId)+"'"+user2+" order by text " ;
			if(parentId.equals("gfb")){
				sql = "select 教学班编号 as id,教学班名称 as text,'open' as state from V_基础信息_教学班信息表 where "+nj+"-left(年级代码,2) between 0 and 2 and 教学班类型='2' " ;
				if(("@"+this.getAUTHCODE()+"@").indexOf(adminAuth)<0 && ("@"+this.getAUTHCODE()+"@").indexOf(qxjwgl)<0){
					sql += " and 系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql+="order by text";
			}else{
				sql = "select id,text,state from (" +
					  "select 行政班代码 as id,行政班名称 as text,'open' as state,'1' as px from V_学校班级数据子类 where "+nj+"-left(行政班代码,2) between 0 and 2 and 系部代码='" + MyTools.fixSql(parentId) + "' " +
					  "union select 教学班编号 as id,教学班名称 as text,'open' as state,'2' as px from V_基础信息_教学班信息表 where "+nj+"-left(年级代码,2) between 0 and 2 and 系部代码='" + MyTools.fixSql(parentId) + "' and 教学班类型='1' " +
					  ") x order by px,text";
			}
		}
		
//		暂时去掉查询条件20151028
//		if(!"".equalsIgnoreCase(XNXQ)){
//			sql1 = "select CONVERT(varchar(6),学期开始时间,112) as 学期开始时间," +
//					"CONVERT(varchar(6),学期结束时间,112) as 学期结束时间 from V_规则管理_学年学期表 " +
//					"where 学年学期编码='" + MyTools.fixSql(XNXQ) + MyTools.fixSql(JXXZ) + "'";
//			vec1 = db.GetContextVector(sql1);
//			if (vec1 != null && vec1.size() > 0) {
//				beginTime = MyTools.fixSql(MyTools.StrFiltr(vec1.get(0)));
//				endTime = MyTools.fixSql(MyTools.StrFiltr(vec1.get(1)));
//				sql += " and b.建班年月 between '"+ beginTime +"' and '"+ endTime +"'";
//			}else{
//				sql += " and b.建班年月 between 9999 and 99";
//			}
//		}else{
//			sql2 = "select CONVERT(varchar(6),学期开始时间,112) as 学期开始时间," +
//					"CONVERT(varchar(6),学期结束时间,112) as 学期结束时间 from V_规则管理_学年学期表 " +
//					"where 学年学期编码=(select max(学年学期编码) from V_规则管理_学年学期表)";
//			vec2 = db.GetContextVector(sql2);
//			if (vec2 != null && vec2.size() > 0) {
//				beginTime = MyTools.fixSql(MyTools.StrFiltr(vec2.get(0)));
//				endTime = MyTools.fixSql(MyTools.StrFiltr(vec2.get(1)));
//				sql += " and b.建班年月 between '"+ beginTime +"' and '"+ endTime +"'";
//			}
//		}
//		sql += " and c.学年学期编码='" + MyTools.fixSql(XNXQ) + MyTools.fixSql(JXXZ) + "'";
		
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryGrid(int pageNum ,int pageSize) throws SQLException {
		String sql = "";
		Vector vec = null;
		
		sql = "select 授课计划明细编号,课程名称,课程类型 as GS_KCLX,课程代码 as GS_KCMC,授课教师编号 as GS_SKJSBH," +
			  "授课教师姓名 as GS_SKJSXM,节数 as GS_JS,连节 as GS_LJ," +
			  "连次 as GS_LC,场地要求 as GS_CDMC,场地名称 as GS_CDYQ,授课周次 as GS_SKZCXQ,授课周次详情 as GS_SKZC,c.编号 as 考试形式编号,c.考试形式 as GS_KSXS,b.授课计划主表编号,a.学分,a.总课时,a.是否考试,b.学年学期编码 " +
			  "from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 left join V_考试形式 c on a.考试形式=c.编号 " +
			  "where b.行政班代码 ='" + MyTools.fixSql(this.getXZBDM()) + "' and b.学年学期编码='" + MyTools.fixSql(this.getGS_XNXQBM()) + "'";
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo() throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "select distinct 学年+学期 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		//String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		
		//editTime：20150730，editor：lupengfei，description：需要获取学年学期编码
		//String sql = "select distinct 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		vec2 = db.GetContextVector(sql);
		System.out.println("学年+学期:"+vec2.get(0).toString());
		this.setMSG(vec2.get(0).toString());
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJXXZCombo() throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "select distinct cast(编号 as nvarchar) as comboValue,教学性质 as comboName " +
					 "from V_基础信息_教学性质 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		vec2 = db.GetContextVector(sql);
		System.out.println("教学性质:"+vec2.get(0).toString());
		this.setMSG2(vec2.get(0).toString());
		return vec;
	}
	
	/**
	 * 是否考试
	 * @date:2016-12-07
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSFKSCombo() throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "select '1' as comboValue,'是' as comboName union " +
				 "select '2' as comboValue,'否' as comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);

		return vec;
	}
	
	/**
	 * 课程类型
	 * @date:2016-12-07
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKCLXCombo() throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "select '00' as comboValue,'请选择' as comboName union " +
				 "select '01' as comboValue,'公共课' as comboName union " +
				 "select '02' as comboValue,'专业课' as comboName ";
		vec = db.getConttexJONSArr(sql, 0, 0);

		return vec;
	}
	
	/**
	 * 课程名称
	 * @date:2017-03-14
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKCMCCombo() throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		
		if(this.getGS_XZBDM().indexOf("JXB_")>-1){//是教学班
			sql2="select [系部代码],教学班类型 FROM [dbo].[V_基础信息_教学班信息表] where [教学班编号]='"+MyTools.fixSql(this.getGS_XZBDM())+"'";
		}else{//行政班
			sql2="select [系部代码] FROM [dbo].[V_学校班级数据子类] where [行政班代码]='"+MyTools.fixSql(this.getGS_XZBDM())+"'";
		}
		
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			String sql3="";
			if(this.getGS_XZBDM().indexOf("JXB_")>-1){//是教学班
				if(vec2.get(1).toString().equals("1")){
					sql3=" and (charIndex('"+MyTools.fixSql(vec2.get(0).toString())+"',a.[系部代码])>0 or a.[系部代码]='') ";
				}else{
				
				}				
			}else{
				sql3=" and (charIndex('"+MyTools.fixSql(vec2.get(0).toString())+"',a.[系部代码])>0 or a.[系部代码]='') ";
			}
			
			if(this.getGS_KCLX().equals("01")){
				sql="select '' as comboValue,'请选择' as comboName " +
					"union " +
					"SELECT [课程号] as comboValue,[课程名称] as comboName FROM [dbo].[V_课程数据子类] a where a.课程类型='"+MyTools.fixSql(this.getGS_KCLX())+"' "+sql3 ;
			}else if(this.getGS_KCLX().equals("02")){
				sql="select comboValue,comboName from (" +
					"select '' as comboValue,'请选择' as comboName,'1' as px,'' as 专业代码  " +
					"union " +
					"SELECT a.[课程号] as comboValue,a.[课程名称]+' ['+b.专业名称+'专业]' as comboName,'2' as px,b.专业代码 FROM [dbo].[V_课程数据子类] a,dbo.V_专业基本信息数据子类 b " +
					"where a.专业代码=b.专业代码 and a.课程类型='02' "+sql3+
					") x order by px,专业代码 " ;
			}	
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-07-30
	 * @author:lupengfei
	 * @description:获取学期对应的周数量
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void getWeeknum(String termid) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String sql3="";
		if(termid.equalsIgnoreCase("")){
			sql3="select distinct 学年+学期+教学性质 as termid,学年+学期,教学性质 as jxxz,[实际上课周数] FROM V_规则管理_学年学期表 where 1=1 order by termid desc";
			vec3=db.GetContextVector(sql3);
			if(vec3!=null&&vec3.size()>0){
				termid=vec3.get(0).toString();
			}
		}
		String sql = " select [实际上课周数] FROM V_规则管理_学年学期表 where 学年学期编码='"+MyTools.fixSql(termid)+"'" ;
		vec = db.GetContextVector(sql);
		if(vec!=null&vec.size()>0){
			this.setMSG(vec.get(0).toString());			
		}
		String sql2 = " select COUNT(*) from [dbo].[V_规则管理_学期周次表] where 学年学期编码='"+MyTools.fixSql(termid)+"'" ;
		vec2 = db.GetContextVector(sql2);
		if(vec2!=null&vec2.size()>0){
			this.setMSG2(vec2.get(0).toString());			
		}
	}
	
	/**
	 * 读取学科名称下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXKMCCombo() throws SQLException{
		Vector vec = null;
		String zydm="";
		String kcbh="";
		
		String sql2="select 专业代码 from dbo.V_基础信息_班级信息表  where 班级编号 = '"+MyTools.fixSql(this.XZBDM)+"' ";
		vec=db.GetContextVector(sql2);
		if(vec!=null&vec.size()>0){
			zydm=vec.get(0).toString();
		}

		//System.out.println("zydm----------------------"+this.XZBDM.indexOf("_"));

		String sql = " SELECT  '' AS comboValue,'请选择' AS comboName FROM V_课程数据子类 union " +
					 " SELECT 课程号 AS comboValue,课程名称 AS comboName FROM V_课程数据子类 where substring(课程号,0,4)='000' union "+
					 " SELECT 课程号 AS comboValue,课程名称 AS comboName FROM V_课程数据子类  where 课程号 like '"+MyTools.fixSql(zydm)+"%'";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取考试形式下拉框
	 * @date:2016-02-04
	 * @author:lpengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKSXSCombo() throws SQLException{
		Vector vec = null;
		String sql = "SELECT 编号 AS comboValue,case when 考试形式='' then '请选择' else 考试形式 end AS comboName FROM V_考试形式 ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取任课教师下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadRKJSCombo() throws SQLException{
		Vector vec = null;
		String sql = "SELECT  '' AS comboValue,'请选择' AS comboName FROM V_教职工基本数据子类 union " +
					 "SELECT 工号 AS comboValue,姓名 AS comboName FROM V_教职工基本数据子类";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	public void check(String teacherbh,String teacherxm,String saveType) throws SQLException {
		Vector vec = null;
		Vector vec1 = null;
		Vector vec2 = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		String dqkcs = "";//执教课程数
		String kcsgz = "";//最大允许执教课程数
		
		/**
		 * 当一个学年学期编码已存在V_规则管理_特殊规则表中，则直接查询；
		 * 否则将V_教职工基本数据子类中所有教师工号插入V_规则管理_特殊规则表的这个学年学期编码下，
		 * 当插入成功后，最后执行查询。
		 * 20170124 lupengfei
		 */
		//if(db.getResultFromDB(sql1)){
		//每次查询之前,先更新一遍当前学年学期下的教师列表,如果有新增的教师编号,则插入,否则直接进行查询列表操作
		//并按照每个查询出来的教师进行插入对应的特殊规则数值
		String sqlts = "select distinct a.工号  from V_教职工基本数据子类 a " +
				" where 工号 not in (select 教师编号 from V_规则管理_特殊规则表  where 学年学期编码='"+ this.XNXQ+this.JXXZ +"' and 状态=1 )";
				
		Vector vects = db.GetContextVector(sqlts);
		Vector vecsub=new Vector();
		if(vects!=null && vects.size()>0) {
			String[] shuzhi = new String[5];
			for(int i=0; i<vects.size(); i++) {
				shuzhi = MyTools.getProp(request, "Base.PuTongJS").split(",");
				String sqlins = "insert into V_规则管理_特殊规则表 values ('"+ this.XNXQ+this.JXXZ +"', '"+ vects.get(i) +"', '"+ shuzhi[0] +"', '"+ shuzhi[1] +"', '"+ shuzhi[2] +"', '"+ shuzhi[3] +"', '"+ shuzhi[4] +"', '"+ MyTools.getSessionUserCode(request) +"', getDate(), 1)";
				vecsub.add(sqlins);
			}
			db.executeInsertOrUpdateTransaction(vecsub);
		}
		//------------------------------------------------
		
		
		
		String skjsbh=this.SKJSBH;
		skjsbh=skjsbh.replaceAll(" ", "|");
		System.out.println("授课教师编号："+skjsbh);
		String[] skjsbh1=skjsbh.split("\\|");
		String skjsbhxm=this.SKJSBHXM;
		skjsbhxm=skjsbhxm.replaceAll(" ", "|");
		System.out.println("授课教师姓名："+skjsbhxm);
		String[] skjsbhxm1=skjsbhxm.split("\\|");
		System.out.println("teacherbh："+teacherbh);
		String[] teacherbh1=teacherbh.split(",");
		String[] teacherxm1=teacherxm.split(",");
		
		String skjsbh2="";
		String skjsbhxm2="";
		this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("true"));
		for(int j=0;j<skjsbh1.length;j++){
			skjsbh2=skjsbh1[j];
			skjsbhxm2=skjsbhxm1[j];
			sql2 = "select distinct 课程代码 from V_规则管理_授课计划明细表 a " +
					"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
					"where b.学年学期编码 ='"+MyTools.StrFiltr(MyTools.fixSql(this.XNXQ))+MyTools.StrFiltr(MyTools.fixSql(this.JXXZ))+"' " +
					"and a.授课教师编号 like '%"+MyTools.StrFiltr(MyTools.fixSql(skjsbh2))+"%'";
			vec2 = db.GetContextVector(sql2);
			if (vec2 != null && vec2.size() > 0) {
				for(int i=0;i<vec2.size();i++){
					if((vec2.get(i)).equals(MyTools.StrFiltr(MyTools.fixSql(this.XKDM)))){
						//this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("true"));
						break;
					}else{
						if((i+1)==vec2.size()){
							sql = "select count(distinct 课程代码) as 数量 from V_规则管理_授课计划明细表 a " +
									"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
									"where b.学年学期编码 ='"+MyTools.StrFiltr(MyTools.fixSql(this.XNXQ))+MyTools.StrFiltr(MyTools.fixSql(this.JXXZ))+"' " +
									"and a.授课教师编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(skjsbh2))+"%'";
							vec = db.GetContextVector(sql);
							if (vec != null && vec.size() > 0) {
								dqkcs=MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
							}
							sql1 = "select 最大执教课程数 from V_规则管理_特殊规则表 " +
									"where 学年学期编码 ='"+MyTools.StrFiltr(MyTools.fixSql(this.XNXQ))+MyTools.StrFiltr(MyTools.fixSql(this.JXXZ))+"' " +
									"and 教师编号='"+MyTools.StrFiltr(MyTools.fixSql(skjsbh2))+"'";
							vec1 = db.GetContextVector(sql1);
							if (vec1 != null && vec1.size() > 0) {
								kcsgz=MyTools.fixSql(MyTools.StrFiltr(vec1.get(0)));
							
								if(!teacherbh.equals("")){
									for(int t=0;t<teacherbh1.length;t++){
										if(teacherbh1[t].indexOf(skjsbh2)>-1){
											
										}else{
											if(Integer.parseInt(dqkcs)==Integer.parseInt(kcsgz)){
												this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("false"));
												this.SKSL=MyTools.fixSql(MyTools.StrFiltr(kcsgz));
												this.MSG+=skjsbhxm2+",";
											}else{
												//this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("true"));
											}
										}
									}
								}
							}else{
								this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("false"));
								this.SKSL=MyTools.fixSql(MyTools.StrFiltr(kcsgz));
								this.MSG+=skjsbhxm2+",";
							}
						}
					}
				}
			}else{
				sql = "select count(distinct 课程代码) as 数量 from V_规则管理_授课计划明细表 a " +
						"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
						"where b.学年学期编码 ='"+MyTools.StrFiltr(MyTools.fixSql(this.XNXQ))+MyTools.StrFiltr(MyTools.fixSql(this.JXXZ))+"' " +
						"and a.授课教师编号='"+MyTools.StrFiltr(MyTools.fixSql(skjsbh2))+"'";
				vec = db.GetContextVector(sql);
				if (vec != null && vec.size() > 0) {
					dqkcs=MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
				}
				sql1 = "select 最大执教课程数 from V_规则管理_特殊规则表 " +
						"where 学年学期编码 ='"+MyTools.StrFiltr(MyTools.fixSql(this.XNXQ))+MyTools.StrFiltr(MyTools.fixSql(this.JXXZ))+"' " +
						"and 教师编号='"+MyTools.StrFiltr(MyTools.fixSql(skjsbh2))+"'";
				vec1 = db.GetContextVector(sql1);
				if (vec1 != null && vec1.size() > 0) {
					kcsgz=MyTools.fixSql(MyTools.StrFiltr(vec1.get(0).toString()));
					System.out.println(dqkcs);
					System.out.println(kcsgz);
					
					if(!teacherbh.equals("")){
						for(int t=0;t<teacherbh1.length;t++){
							if(teacherbh1[t].indexOf(skjsbh2)>-1){
									
							}else{
								if(Integer.parseInt(dqkcs)==Integer.parseInt(kcsgz)){
									if(saveType.equals("new")){
										this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("false"));
										this.SKSL=MyTools.fixSql(MyTools.StrFiltr(kcsgz));
										this.MSG+=skjsbhxm2+",";
									}
								}else{
									//this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("true"));
								}
							}
						}
					}
				}else{
					this.JSKCS=MyTools.fixSql(MyTools.StrFiltr("false"));
					this.SKSL=MyTools.fixSql(MyTools.StrFiltr(kcsgz));
					this.MSG+=skjsbhxm2+",";
				}
			}
		}
		
	}
	
	//检查授课计划是否已排过固排和做过合班设置
	public void checkChangeSKJH() throws SQLException {
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		int c1=0;
		int c2=0;
			
		sql ="select COUNT(*) from dbo.V_规则管理_固排禁排表 where 授课计划明细编号='"+MyTools.fixSql(this.GS_SKJHMXBH)+"' ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			c1=Integer.parseInt(vec.get(0).toString());
		}
		sql2="select COUNT(*) from V_规则管理_合班表 where 授课计划明细编号 like '%"+MyTools.fixSql(this.GS_SKJHMXBH)+"%'";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			c2=Integer.parseInt(vec2.get(0).toString());
		}
		if((c1+c2)==0){//没有排授课计划和合班
			this.setMSG("0");
		}else{
			this.setMSG("1");
		}
	}
	
	//检查课程表明细中是否有该授课计划（有没有排过课）
	public void checkDelSKJH(String iKeyCode) throws SQLException {
			Vector vec = null;
			Vector vec2 = null;
			String sql = "";
			String sql2 = "";
			int c1=0;
			int c2=0;
				
			sql ="SELECT COUNT(*) FROM dbo.V_排课管理_课程表明细详情表 where 授课计划明细编号 like '%"+MyTools.fixSql(iKeyCode)+"%' ";
			vec=db.GetContextVector(sql);
			if(vec!=null&&vec.size()>0){
				c1=Integer.parseInt(vec.get(0).toString());
			}
			sql2 ="SELECT COUNT(*) FROM dbo.V_规则管理_固排禁排表 where 授课计划明细编号 like '%"+MyTools.fixSql(iKeyCode)+"%' ";
			vec2=db.GetContextVector(sql2);
			if(vec2!=null&&vec2.size()>0){
				c2=Integer.parseInt(vec2.get(0).toString());
			}
			if(c1!=0){//排过课
				this.setMSG("1");
			}else if(c1==0&&c2!=0){//固排过
				this.setMSG("2");
			}else if(c1==0&&c2==0){//没有排课信息
				this.setMSG("0");
			}
	}
	
	//检查课程表明细中是否有该授课计划（有没有排过课）
	public void checkDelallSKJH() throws SQLException {
				Vector vec = null;
				Vector vec2 = null;
				String sql = "";
				String sql2 = "";
				int c1=0;
				int c2=0;
						
				sql ="SELECT COUNT(*) FROM dbo.V_排课管理_课程表明细详情表   where 学年学期编码='"+MyTools.fixSql(this.getGS_XNXQBM())+"' and 行政班代码='"+MyTools.fixSql(this.getGS_XZBDM())+"' ";
				vec=db.GetContextVector(sql);
				if(vec!=null&&vec.size()>0){
					c1=Integer.parseInt(vec.get(0).toString());
				}
					
				if(c1==0){//没有排课信息
					this.setMSG("0");
				}else{
					this.setMSG("1");
				}
	}
		
		
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void SaveRec() throws WrongSQLException,SQLException{
		String sql = "select count(*) from V_规则管理_授课计划明细表 where 授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"'";
		if(db.getResultFromDB(sql)){
			if(this.getUseType().equals("edit")){
				this.modRec();
			}else{
				this.speRec();
			}
		}else{
			this.addRec();
		}
	}
	
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @throws SQLException
	 */
	public void addRec() throws SQLException {
		String sql="";
		String sqlMx="";
		String sql1="";
		String sql2="";
		String sql3="";
		String sql4="";
		String sql5="";
		String sql6="";
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		Vector vec5 = null; // 结果集
		Vector vec6 = null; // 结果集
		Vector sqlVec = new Vector();
		String maxZbId="";
		String maxMxId="";
		String kclx=""; //课程类型
		
		//课程已存在取已有课程类型，不存在按000开头为公共课，其它专业课
//		sql5=" SELECT distinct a.课程代码,a.课程类型 FROM V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b  where a.[授课计划主表编号]=b.[授课计划主表编号] and  a.课程代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KCMCDM))+"' and b.学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XNXQBM()))+"' and 课程类型!='' ";
//		vec5=db.GetContextVector(sql5);
//		if(vec5!=null&&vec5.size()>0){
//			kclx=vec5.get(1).toString();
//		}else{
//			if(this.GS_KCMCDM.substring(0,3).equals("000")){
//				kclx="01";
//			}else{
//				kclx="02";
//			}	
//		}
		
		//单双周
		String skzcn=this.GS_SKZC;
		skzcn=skzcn.replaceAll("单周", "odd").replaceAll("双周", "even");
		this.setGS_SKZC(skzcn);
		
		//处理课程名称
		String kcmc=this.GS_KCDM;
		if(this.GS_KCLX.equals("02")){
			kcmc=kcmc.substring(0,kcmc.indexOf("["));
		}
		this.setGS_KCDM(kcmc);
		
		//取已有该学期，班级，课程编号相同的课程名称
		sql6="SELECT [课程名称] FROM [dbo].[V_规则管理_授课计划明细表] a,dbo.V_规则管理_授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 " +
			 "and b.行政班代码='"+MyTools.fixSql(this.GS_XZBDM)+"' and b.学年学期编码='"+MyTools.fixSql(this.getGS_XNXQBM())+"' and [课程代码]='"+MyTools.fixSql(this.GS_KCMCDM)+"' ";
		vec6=db.GetContextVector(sql6);
		if(vec6!=null&&vec6.size()>0){
			this.setGS_KCDM(vec6.get(0).toString());
		}else{
			
		}
		
		sql3="select count(*) from V_规则管理_授课计划主表 where " +
			 "学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_XNXQBM))+"' and " +
			 "行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_XZBDM))+"'";
		Vector veczb=db.GetContextVector(sql3);
		if(!veczb.get(0).toString().equals("0")){//主表编号已存在
			sql4="select 授课计划主表编号 from V_规则管理_授课计划主表 where " +
				 "学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_XNXQBM))+"' and " +
				 "行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_XZBDM))+"'";
			vec4 = db.GetContextVector(sql4);
			if (vec4 != null && vec4.size() > 0) {
				this.setGS_SKJHZBBH(MyTools.fixSql(MyTools.StrFiltr(vec4.get(0))));//设置授课计划主表主键
			}
			
			sql2="select max(cast(SUBSTRING(授课计划明细编号,8,11) as bigint)) from V_规则管理_授课计划明细表    ";
			vec2 = db.GetContextVector(sql2);
			//System.out.println("vec2---|"+vec2+"|"+vec2.size());
			if (!vec2.toString().equals("[]") && vec2.size() > 0) {
				maxMxId=String.format("%010d", (Long.parseLong(vec2.get(0).toString())+1));  
				this.setGS_SKJHMXBH("SKJHMX_"+maxMxId);//设置授课计划明细主键
			}else{
				this.setGS_SKJHMXBH("SKJHMX_10000000001");//设置授课计划明细主键
			}
			
			sqlMx="insert into V_规则管理_授课计划明细表 (" +
				  "授课计划明细编号,授课计划主表编号,课程代码,课程名称,课程类型,授课教师编号," +
				  "授课教师姓名,节数,连节,连次,场地要求,场地名称,授课周次,授课周次详情,创建人,状态,考试形式,学分,总课时,是否考试) values (" +
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH)) + "'," + //授课计划明细编号
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHZBBH)) + "'," + //授课计划主表编号
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_KCMCDM)) + "'," + //课程代码
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.getGS_KCDM())) + "'," + //课程名称
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_KCLX)) + "'," + //课程类型
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJSBH)) + "'," + //授课教师编号
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJSXM)) + "'," + //授课教师姓名
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_JS)) + "'," + //节数
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_LJ)) + "'," + //连节
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_LC)) + "'," + //连次
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_CDMC)) + "'," + //场地要求
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_CDYQ)) + "'," + //场地名称
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKZCXQ)) + "'," + //授课周次
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKZC)) + "'," + //授课周次详情
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.iUSERCODE)) + "'," + //创建人
				  "1," + 														//状态
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_KSXS)) + "'," + //考试形式
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_XF)) + "'," + //学分
			      "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_ZKS)) + "'," + //总课时
			 	  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SFKS)) + "') " ; //是否考试
			sqlVec.add(sqlMx);
		}else{
			sql1="select max(cast(SUBSTRING(授课计划主表编号,6,8) as bigint)) from V_规则管理_授课计划主表  where 授课计划主表编号 not like 'SKJH_8%' ";
			vec1 = db.GetContextVector(sql1);
			if (!vec1.toString().equals("[]") && vec1.size() > 0) {
				maxZbId = String.format("%07d", (Integer.parseInt(vec1.get(0).toString())+1));  
				this.setGS_SKJHZBBH("SKJH_"+maxZbId);//设置授课计划主表主键
			}else{
				this.setGS_SKJHZBBH("SKJH_10000001");//设置授课计划主表主键
			}
			
			sql2="select max(cast(SUBSTRING(授课计划明细编号,8,11) as bigint)) from V_规则管理_授课计划明细表   where 授课计划明细编号 not like 'SKJHMX_8%' ";
			vec2 = db.GetContextVector(sql2);
			//System.out.println("vec2---|"+vec2+"|"+vec2.size());
			if (!vec2.toString().equals("[]") && vec2.size() > 0) {
				maxMxId=String.format("%010d", (Long.parseLong(vec2.get(0).toString())+1));  
				this.setGS_SKJHMXBH("SKJHMX_"+maxMxId);//设置授课计划明细主键
			}else{
				this.setGS_SKJHMXBH("SKJHMX_10000000001");//设置授课计划明细主键
			}
			
			String sqlmajor="";
		
			sqlmajor="select a.专业代码,b.专业名称 from V_基础信息_班级信息表 a left join dbo.V_专业基本信息数据子类 b on a.专业代码=b.专业代码 "+
					"where a.班级编号='" + MyTools.StrFiltr(MyTools.fixSql(this.GS_XZBDM)) + "' ";
		
			vec3=db.GetContextVector(sqlmajor);
			if(vec3!=null&&vec3.size()>0){
				this.setGS_ZYDM(vec3.get(0).toString());
				this.setGS_ZYMC(vec3.get(1).toString());
			}
			
			sql="insert into V_规则管理_授课计划主表 (授课计划主表编号,学年学期编码,行政班代码,专业代码,专业名称,创建人,状态) values (" +
				"'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHZBBH)) + "'," +	//授课计划主表编号
				"'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_XNXQBM)) + "'," +	//学年学期编码
				"'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_XZBDM)) + "'," + //行政班代码
				"'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_ZYDM)) + "'," + //专业代码
				"'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_ZYMC)) + "'," + //专业名称
				"'" + MyTools.StrFiltr(MyTools.fixSql(this.iUSERCODE)) + "'," + //创建人
				"1)"; //状态
			sqlVec.add(sql);
			
			sqlMx="insert into V_规则管理_授课计划明细表 (" +
				  "授课计划明细编号,授课计划主表编号,课程代码,课程名称,课程类型,授课教师编号," +
				  "授课教师姓名,节数,连节,连次,场地要求,场地名称,授课周次,授课周次详情,创建人,状态,考试形式,学分,总课时,是否考试) values (" +
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH)) + "'," + //授课计划明细编号
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHZBBH)) + "'," + //授课计划主表编号
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_KCMCDM)) + "'," + //课程代码
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.getGS_KCDM())) + "'," + //课程名称
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_KCLX)) + "'," + //课程类型
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJSBH)) + "'," + //授课教师编号
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJSXM)) + "'," + //授课教师姓名
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_JS)) + "'," + //节数
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_LJ)) + "'," + //连节
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_LC)) + "'," + //连次
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_CDMC)) + "'," + //场地要求
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_CDYQ)) + "'," + //场地名称
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKZCXQ)) + "'," + //授课周次
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SKZC)) + "'," + //授课周次详情
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.iUSERCODE)) + "'," + //创建人
				  "1," + 														//状态
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_KSXS)) + "'," + //考试形式
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_XF)) + "'," + //学分
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_ZKS)) + "'," + //总课时
				  "'" + MyTools.StrFiltr(MyTools.fixSql(this.GS_SFKS)) + "') " ; //是否考试
			sqlVec.add(sqlMx);
		}
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 
	 * @date:2015-06-02
	 * @author:wangzh
	 * @throws SQLException
	 */
	public void modRec() throws SQLException {//对原有主键值更新数据的方法
		String skzcn=this.GS_SKZC;
		skzcn=skzcn.replaceAll("单周", "odd").replaceAll("双周", "even");
		this.setGS_SKZC(skzcn);
		
		String sql="";
		String sql2="";
		String sql3="";
		if(this.GS_KCLX.equals("")){
			sql2="";
		}else{
			sql2="课程类型 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KCLX))+"',";
		}
		
		//变更学期
//		sql3="  select [授课计划主表编号] from [V_规则管理_授课计划主表] where substring([学年学期编码],1,5)='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XNXQ2()))+"' and [行政班代码] in ( " +
//				"select [行政班代码] from  [V_规则管理_授课计划主表] where [授课计划主表编号] in ( " +
//				"select [授课计划主表编号] from V_规则管理_授课计划明细表 where 授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"' ) )";
//		Vector vec3=db.GetContextVector(sql3);
//		if(vec3!=null&&vec3.size()>0){
//			this.setGS_SKJHZBBH(vec3.get(0).toString());
//		}else{
//			this.setMSG("该学期授课计划未导入，不能变更学期");
//			return;
//		}
		
		sql = "update V_规则管理_授课计划明细表 set " +
			  //"授课计划主表编号='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_SKJHZBBH()))+"'," +
			  "课程代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KCMCDM))+"'," +
		      "课程名称 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KCDM))+"'," +
		      sql2 +
		      "授课教师编号 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJSBH))+"'," +
		      "授课教师姓名 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJSXM))+"'," +
		      "固排已排节数 ='0'," +
		      "实际已排节数 ='0'," +
		      "固排连次次数 ='0'," +
		      "实际连次次数 ='0'," +
		      "节数 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_JS))+"'," +
		      "连节 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_LJ))+"'," +
		      "连次 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_LC))+"'," +
		      "场地要求 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_CDMC))+"'," +
		      "场地名称 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_CDYQ))+"'," +
		      "授课周次 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKZCXQ))+"'," +
		      "授课周次详情 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKZC))+"'," +
		      "考试形式 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KSXS))+"'," +
		      "学分 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_XF))+"'," +
		      "总课时 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_ZKS))+"'," +
		      "是否考试 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SFKS))+"' " +
		      " where 授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"' ";	 		
		
		if(this.GS_KCLX.equals("")){//课程类型是空不修改其它课程类型
			
		}else{
			sql+=" update V_规则管理_授课计划明细表 set 课程类型='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KCLX))+"' where 课程代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KCMCDM))+"' ";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
	    }	
	}
	
	/**
	 * 特殊内容编辑
	 * @date:2016-12-15
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public void speRec() throws SQLException {//对原有主键值更新数据的方法
		String sql="";
		sql = "update V_规则管理_授课计划明细表 set " +
		      "考试形式 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_KSXS))+"'," +
		      "学分 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_XF))+"'," +
		      "总课时 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_ZKS))+"'," +
		      "是否考试 ='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SFKS))+"' " +
		      " where 授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"' ";	 		
				
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
	    }	
	}
	
	/**
	 * @date:2016-02-17
	 * @author:lupengfei
	 * @description:检查当前时间是否超过允许排课时间
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void checkpaike() throws SQLException{
		Vector vec = null;
		String sql = " select convert(varchar(10),排课截止时间,21) from V_规则管理_学年学期表 where 学年学期编码='"+MyTools.fixSql(this.getGS_XNXQBM())+"'" ;
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			Calendar cal =Calendar.getInstance();
	        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        System.out.println(df.format(cal.getTime()));//当前时间
	        System.out.println(vec.get(0).toString());//排课截止时间
	        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        try {
				Date dt1 = df.parse(df.format(cal.getTime()));
				Date dt2 = df.parse(vec.get(0).toString());
				 if (dt1.getTime() > dt2.getTime()) {
		             this.setMSG("1");
		         }else{
		        	 this.setMSG("0");
		         } 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @date:2015-08-03
	 * @author:lupengfei
	 * @description:删除授课计划
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void deleteskjh(String iKeyCode) throws SQLException{
		Vector vec = new Vector();
		Vector vec2 = null;
		String sql="";
		String sql2="";
		
		//删除周详情表
		sql2="select distinct [课程表明细编号] from [V_排课管理_课程表周详情表] where 授课计划明细编号  like '%"+MyTools.fixSql(iKeyCode)+"%' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			for(int i=0;i<vec2.size();i++){
				sql="update [V_排课管理_课程表周详情表] set [授课计划明细编号]='',[课程代码]='',[课程名称]='',[课程类型]='',[授课教师编号]='',[授课教师姓名]='',[场地编号]='',[场地名称]='' where [课程表明细编号]='"+vec2.get(i).toString()+"' ";
				vec.add(sql);
			}
		}			
		
		//删除专业课排课明细详情表
		sql2="select [课程表明细编号],[课程表主表编号],[班级排课状态],[时间序列],[连节相关编号],[授课计划明细编号][课程代码],[课程名称],[课程类型],[授课教师编号],[授课教师姓名],[场地要求],[场地名称],[实际场地编号],[实际场地名称],[授课周次],[授课周次详情] from [V_排课管理_课程表明细详情表] where 授课计划明细编号  like '%"+MyTools.fixSql(iKeyCode)+"%' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			String newskjhmxbh="";
			String newkcdm="";
			String newkcmc="";
			String newkclx="";
			String newskjsbh="";
			String newskjsxm="";
			String newcdyq="";
			String newcdmc="";
			String newsjcdbh="";
			String newsjcdmc="";
			String newskzc="";
			String newskzcxq="";
			for(int i=0;i<vec2.size();i=i+17){
				if(vec2.get(i+5).toString().indexOf("｜")>-1){//有合并
					String[] skjhmxbh=vec2.get(i+5).toString().split("｜");
					String[] kcdm=vec2.get(i+6).toString().split("｜");
					String[] kcmc=vec2.get(i+7).toString().split("｜");
					String[] kclx=vec2.get(i+8).toString().split("｜");
					String[] skjsbh=vec2.get(i+9).toString().split("｜");
					String[] skjsxm=vec2.get(i+10).toString().split("｜");
					String[] cdyq=vec2.get(i+11).toString().split("｜");
					String[] cdmc=vec2.get(i+12).toString().split("｜");
					String[] sjcdbh=vec2.get(i+13).toString().split("｜");
					String[] sjcdmc=vec2.get(i+14).toString().split("｜");
					String[] skzc=vec2.get(i+15).toString().split("｜");
					String[] skzcxq=vec2.get(i+16).toString().split("｜");
					
					for(int j=0;j<skjhmxbh.length;j++){
						if(!skjhmxbh[j].equals(iKeyCode)){//授课计划不相同
							newskjhmxbh+=skjhmxbh[j]+"｜";
							newkcdm+=kcdm[j]+"｜";
							newkcmc+=kcmc[j]+"｜";
							newkclx+=kclx[j]+"｜";
							newskjsbh+=skjsbh[j]+"｜";
							newskjsxm+=skjsxm[j]+"｜";
							newcdyq+=cdyq[j]+"｜";
							newcdmc+=cdmc[j]+"｜";
							newsjcdbh+=sjcdbh[j]+"｜";
							newsjcdmc+=sjcdmc[j]+"｜";
							newskzc+=skzc[j]+"｜";
							newskzcxq+=skzcxq[j]+"｜";
						}
					}
					newskjhmxbh=newskjhmxbh.substring(0, newskjhmxbh.length()-1);
					newkcdm=newkcdm.substring(0, newkcdm.length()-1);
					newkcmc=newkcmc.substring(0, newkcmc.length()-1);
					newkclx=newkclx.substring(0, newkclx.length()-1);
					newskjsbh=newskjsbh.substring(0, newskjsbh.length()-1);
					newskjsxm=newskjsxm.substring(0, newskjsxm.length()-1);
					newcdyq=newcdyq.substring(0, newcdyq.length()-1);
					newcdmc=newcdmc.substring(0, newcdmc.length()-1);
					newsjcdbh=newsjcdbh.substring(0, newsjcdbh.length()-1);
					newsjcdmc=newsjcdmc.substring(0, newsjcdmc.length()-1);
					newskzc=newskzc.substring(0, newskzc.length()-1);
					newskzcxq=newskzcxq.substring(0, newskzcxq.length()-1);
					
					
					sql="update [V_排课管理_课程表明细详情表] set [班级排课状态]='"+vec2.get(i+2).toString()+"',[连节相关编号]='"+vec2.get(i+3).toString()+"',[授课计划明细编号]='"+newskjhmxbh+"',[课程代码]='"+newkcdm+"',[课程名称]='"+newkcmc+"',[课程类型]='"+newkclx+"',[授课教师编号]='"+newskjsbh+"',[授课教师姓名]='"+newskjsxm+"',[场地要求]='"+newcdyq+"',[场地名称]='"+newcdmc+"',[实际场地编号]='"+newsjcdbh+"',[实际场地名称]='"+newsjcdmc+"',[授课周次]='"+newskzc+"',[授课周次详情]='"+newskzcxq+"' where [课程表明细编号]='"+vec2.get(i).toString()+"' ";
					vec.add(sql);
				}else{
					sql="update [V_排课管理_课程表明细详情表] set [班级排课状态]='',[连节相关编号]='',[授课计划明细编号]='',[课程代码]='',[课程名称]='',[课程类型]='',[授课教师编号]='',[授课教师姓名]='',[场地要求]='',[场地名称]='',[实际场地编号]='',[实际场地名称]='',[授课周次]='',[授课周次详情]='' where [课程表明细编号]='"+vec2.get(i).toString()+"' ";
					vec.add(sql);
				}
			}
		}	
		
		//删除专业课排课明细表
		sql2="select [课程表明细编号],[课程表主表编号],[班级排课状态],[时间序列],[连节相关编号],[授课计划明细编号],[实际场地编号],[实际场地名称] from [V_排课管理_课程表明细表] where 授课计划明细编号  like '%"+MyTools.fixSql(iKeyCode)+"%' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			String newskjhmxbh="";
			String newsjcdbh="";
			String newsjcdmc="";
			for(int i=0;i<vec2.size();i=i+8){
				if(vec2.get(i+5).toString().indexOf("｜")>-1){//有合并
					String[] skjhmxbh=vec2.get(i+5).toString().split("｜");
					String[] sjcdbh=vec2.get(i+6).toString().split("｜");
					String[] sjcdmc=vec2.get(i+7).toString().split("｜");
					for(int j=0;j<skjhmxbh.length;j++){
						if(!skjhmxbh[j].equals(iKeyCode)){//授课计划不相同
							newskjhmxbh+=skjhmxbh[j]+"｜";
							newsjcdbh+=sjcdbh[j]+"｜";
							newsjcdmc+=sjcdmc[j]+"｜";
						}
					}
					newskjhmxbh=newskjhmxbh.substring(0, newskjhmxbh.length()-1);
					newsjcdbh=newsjcdbh.substring(0, newsjcdbh.length()-1);
					newsjcdmc=newsjcdmc.substring(0, newsjcdmc.length()-1);
					
					sql="update [V_排课管理_课程表明细表] set [班级排课状态]='"+vec2.get(i+2).toString()+"',[连节相关编号]='"+vec2.get(i+3).toString()+"',[授课计划明细编号]='"+newskjhmxbh+"',[实际场地编号]='"+newsjcdbh+"',[实际场地名称]='"+newsjcdmc+"' where [课程表明细编号]='"+vec2.get(i).toString()+"' ";
					vec.add(sql);
				}else{
					sql="update [V_排课管理_课程表明细表] set [班级排课状态]='',[连节相关编号]='',[授课计划明细编号]='',[实际场地编号]='',[实际场地名称]='' where [课程表明细编号]='"+vec2.get(i).toString()+"' ";
					vec.add(sql);
				}
			}
		}
		
		//删除公共课排课表
		sql2="select [课程表明细编号],[课程表主表编号],[班级排课状态],[时间序列],[连节相关编号],[授课计划明细编号],[实际场地编号],[实际场地名称] from [V_排课管理_公共课课程表明细表] where 授课计划明细编号  like '%"+MyTools.fixSql(iKeyCode)+"%' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			String newskjhmxbh="";
			String newsjcdbh="";
			String newsjcdmc="";
			for(int i=0;i<vec2.size();i=i+8){
				if(vec2.get(i+5).toString().indexOf("｜")>-1){//有合并
					String[] skjhmxbh=vec2.get(i+5).toString().split("｜");
					String[] sjcdbh=vec2.get(i+6).toString().split("｜");
					String[] sjcdmc=vec2.get(i+7).toString().split("｜");
					for(int j=0;j<skjhmxbh.length;j++){
						if(!skjhmxbh[j].equals(iKeyCode)){//授课计划不相同
							newskjhmxbh+=skjhmxbh[j]+"｜";
							newsjcdbh+=sjcdbh[j]+"｜";
							newsjcdmc+=sjcdmc[j]+"｜";
						}
					}
					newskjhmxbh=newskjhmxbh.substring(0, newskjhmxbh.length()-1);
					newsjcdbh=newsjcdbh.substring(0, newsjcdbh.length()-1);
					newsjcdmc=newsjcdmc.substring(0, newsjcdmc.length()-1);
					
					sql="update [V_排课管理_公共课课程表明细表] set [班级排课状态]='"+vec2.get(i+2).toString()+"',[连节相关编号]='"+vec2.get(i+3).toString()+"',[授课计划明细编号]='"+newskjhmxbh+"',[实际场地编号]='"+newsjcdbh+"',[实际场地名称]='"+newsjcdmc+"' where [课程表明细编号]='"+vec2.get(i).toString()+"' ";
					vec.add(sql);
				}else{
					sql="update [V_排课管理_公共课课程表明细表] set [班级排课状态]='',[连节相关编号]='',[授课计划明细编号]='',[实际场地编号]='',[实际场地名称]='' where [课程表明细编号]='"+vec2.get(i).toString()+"' ";
					vec.add(sql);
				}
			}
		}	
	
		//删除固排
		sql = " delete from dbo.V_规则管理_固排禁排表 where 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"'" ;
		vec.add(sql);
		
		//删除授课计划
		sql = " delete from V_规则管理_授课计划明细表 where 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"'" ;
		vec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("删除成功");		
		}else{
			this.setMSG("删除失败");		
		}
	}
	
	/**
	 * @date:2016-02-03
	 * @author:lupengfei
	 * @description:删除同一届所有班级的授课计划
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void deleteAllskjh() throws SQLException{	
		String sql = " select [授课计划明细编号] from V_规则管理_授课计划明细表 where 授课计划主表编号 in " +
				"(select 授课计划主表编号 from dbo.V_规则管理_授课计划主表 where 学年学期编码='"+MyTools.fixSql(this.getGS_XNXQBM())+"' and 行政班代码 in " +
				"(select 班级编号 from V_基础信息_班级信息表 where substring(班级编号,1,2)='"+this.getGS_XZBDM().substring(0, 2)+"' and 系部代码 in " +
				"(SELECT 系部代码 FROM dbo.V_基础信息_班级信息表 where 班级编号='"+MyTools.fixSql(this.getGS_XZBDM())+"')) ) " ;
		Vector vec=db.GetContextVector(sql);
		for(int i=0;i<vec.size();i++){
			deleteskjh(vec.get(i).toString());
		}
		
//		if(db.executeInsertOrUpdate(sql)){
//			this.setMSG("删除成功");		
//		}else{
//			this.setMSG("删除失败");		
//		}
	}
	
	/**
	 * @date:2015-10-26
	 * @author:lupengfei
	 * @description:根据改变的授课计划删除已设置的固排与合班
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void editCD(String iKeyCode) throws SQLException{
		Vector vec = null;
		Vector vec3 = null;
		String sql="";
		
		//sql=" update V_规则管理_授课计划明细表 set 固排已排节数='0',固排连次次数='0' where 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"' ";
		//sql+= " delete from V_规则管理_固排禁排表 where 授课计划明细编号='"+MyTools.fixSql(iKeyCode)+"' " ;
		//sql+=" delete from V_规则管理_合班表 where 授课计划明细编号  like '%"+MyTools.fixSql(iKeyCode)+"%' ";
		
		//查询合班信息
        String sqlbh=" select 授课计划明细编号 from dbo.V_规则管理_合班表 where 授课计划明细编号 like '%"+MyTools.fixSql(iKeyCode)+"%'";
        Vector vecbh=db.GetContextVector(sqlbh);
        Vector vecbj=null;
        String[] skjhsz;
        String sqlbj="";
        String skjhhb="";
        int v=0;
        
        if(vecbh!=null&&vecbh.size()>0){
        	skjhsz=vecbh.get(0).toString().split("\\+");
        	for(int i=0;i<skjhsz.length;i++){
        		sqlbj="select b.行政班代码 from dbo.V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号='"+skjhsz[i]+"'";
        		vecbj=db.GetContextVector(sqlbj);
        		if(vecbj!=null&&vecbj.size()>0){
        			skjhhb+="'"+vecbj.get(0).toString()+"',";
        		}
        	}
        	skjhhb=skjhhb.substring(0, skjhhb.length()-1);
        }else{
        	skjhsz=iKeyCode.split("\\+");
        	skjhhb="'"+this.GS_XZBDM+"'";
        }
        
        //按每个班级处理
	        for(int b=0;b<skjhsz.length;b++){
	        	sqlbj="select b.行政班代码 from dbo.V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号='"+skjhsz[b]+"'";
        		vecbj=db.GetContextVector(sqlbj);
        		if(vecbj!=null&&vecbj.size()>0){
        			this.setGS_SKJHMXBH(skjhsz[b]);
        			this.setGS_XZBDM(vecbj.get(0).toString());
        		}
	        	//执行操作
//		    		String editlj="";
//		    		if(iKeyCode.equalsIgnoreCase("")){
//		    			//editlj=editsjxl;
//		    		}else{
//		    			String sqllj="select a.连节相关编号,b.连次 from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and a.授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"' ";
//		        		vec3=db.GetContextVector(sqllj);
//		        		if(vec3!=null&&vec3.size()>0){
//		        			if(vec3.get(0).toString().equalsIgnoreCase("")){
//		        				//editlj=editsjxl;
//		        			}else{
//		        				String[] ljbh=vec3.get(0).toString().split(",");
//		            			for(int i=0;i<ljbh.length;i++){
//		            				editlj+="'"+ljbh[i]+"',";
//		            			}
//		            			//editlj+=editsjxl;   			
//		        			}		
//		        		}else{
//		        			//editlj=editsjxl;
//		        		}
//		    		}
		    		
		    		//修改已排节数，连次次数
		    		sql +=" update V_规则管理_授课计划明细表 set 固排已排节数='0',固排连次次数='0' where 授课计划明细编号='"+MyTools.fixSql(this.getGS_SKJHMXBH())+"' ";
		    		
		    		//删除固排禁排
		    		sql +=" delete from V_规则管理_固排禁排表 " +
		        			"where 行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_XZBDM))+"' " +
		        			"and 授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"' ";
		    		
		    		//删除课程表
		    		String sql7 = "";
		    		Vector vec7 = null; // 结果集
		
		    		sql7="select 时间序列,班级排课状态,连节相关编号,授课计划明细编号,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,场地要求,场地名称,实际场地编号,实际场地名称,授课周次,授课周次详情,状态  from dbo.V_排课管理_课程表明细详情表 " +
		    			 "where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"%' " ;
		        		 //"and 时间序列 in ("+editlj+") "; 
		    		vec7=db.GetContextVector(sql7);
		    		if(vec7!=null&&vec7.size()>0){
		    			for(int i=0;i<vec7.size();i=i+16){
		    				String result3="";
							String result4="";
							String result5="";
							String result6="";
							String result7="";
							String result8="";
							String result9="";
							String result10="";
							String result11="";
							String result12="";
							String result13="";
							String result14="";
		    				if(vec7.get(i+3).toString().indexOf("｜")>-1){//存在合并情况
		    					String[] rs3=vec7.get(i+3).toString().split("｜");
		    					String[] rs4=vec7.get(i+4).toString().split("｜");
		    					String[] rs5=vec7.get(i+5).toString().split("｜");
		    					String[] rs6=vec7.get(i+6).toString().split("｜");
		    					String[] rs7=vec7.get(i+7).toString().split("｜");
		    					String[] rs8=vec7.get(i+8).toString().split("｜");
		    					String[] rs9=vec7.get(i+9).toString().split("｜");
		    					String[] rs10=vec7.get(i+10).toString().split("｜");
		    					String[] rs11=vec7.get(i+11).toString().split("｜");
		    					String[] rs12=vec7.get(i+12).toString().split("｜");
		    					String[] rs13=vec7.get(i+13).toString().split("｜");
		    					String[] rs14=vec7.get(i+14).toString().split("｜");
		    					
		    					for(int t=0;t<rs3.length;t++){
		    						//授课计划编号相同，删除掉
		    						if(rs3[t].equals(this.GS_SKJHMXBH)){
		    							
		    						}else{
		    							result3+=rs3[t]+"｜";
		    							result4+=rs4[t]+"｜";
		    							result5+=rs5[t]+"｜";
		    							result6+=rs6[t]+"｜";
		    							result7+=rs7[t]+"｜";
		    							result8+=rs8[t]+"｜";
		    							result9+=rs9[t]+"｜";
		    							result10+=rs10[t]+"｜";
		    							result11+=rs11[t]+"｜";
		    							result12+=rs12[t]+"｜";
		    							result13+=rs13[t]+"｜";
		    							result14+=rs14[t]+"｜";	    							
		    						}
		    					}
	    					
		    					if(!result3.equals("")){
    								result3=result3.substring(0,result3.length()-1);
    							}
		    					if(!result4.equals("")){
    								result4=result4.substring(0,result4.length()-1);
    							}
		    					if(!result5.equals("")){
    								result5=result5.substring(0,result5.length()-1);
    							}
		    					if(!result6.equals("")){
    								result6=result6.substring(0,result6.length()-1);
    							}
		    					if(!result7.equals("")){
    								result7=result7.substring(0,result7.length()-1);
    							}
		    					if(!result8.equals("")){
    								result8=result8.substring(0,result8.length()-1);
    							}
		    					if(!result9.equals("")){
    								result9=result9.substring(0,result9.length()-1);
    							}
		    					if(!result10.equals("")){
    								result10=result10.substring(0,result10.length()-1);
    							}
		    					if(!result11.equals("")){
    								result11=result11.substring(0,result11.length()-1);
    							}
		    					if(!result12.equals("")){
    								result12=result12.substring(0,result12.length()-1);
    							}
		    					if(!result13.equals("")){
    								result13=result13.substring(0,result13.length()-1);
    							}
		    					if(!result14.equals("")){
    								result14=result14.substring(0,result14.length()-1);
    							}
		    					
//		    					if(vec7.get(i+3).toString().indexOf(vec8.get(i+3).toString())==0){//在第1个位置
//		    						result3=vec7.get(i+3).toString().replaceAll(vec8.get(i+3).toString()+"｜", "");
//		    						result4=vec7.get(i+4).toString().replaceAll(vec8.get(i+4).toString()+"｜", "");
//		    						result5=vec7.get(i+5).toString().replaceAll(vec8.get(i+5).toString()+"｜", "");
//		    						result6=vec7.get(i+6).toString().replaceAll(vec8.get(i+6).toString()+"｜", "");
//		    						result7=vec7.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result7=result7.replaceAll("!", "&").replaceAll("@", "+");
//		    						result8=vec7.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result8=result8.replaceAll("!", "&").replaceAll("@", "+");
//		    						result9=vec7.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result9=result9.replaceAll("!", "&").replaceAll("@", "+");
//		    						result10=vec7.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result10=result10.replaceAll("!", "&").replaceAll("@", "+");
//		    						result11=vec7.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result11=result11.replaceAll("!", "&").replaceAll("@", "+");
//		    						result12=vec7.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result12=result12.replaceAll("!", "&").replaceAll("@", "+");
//		    						result13=vec7.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result13=result13.replaceAll("!", "&").replaceAll("@", "+");
//		    						result14=vec7.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//		    						result14=result14.replaceAll("!", "&").replaceAll("@", "+");
//		    					}else{
//		    						result3=vec7.get(i+3).toString().replaceAll("｜"+vec8.get(i+3).toString(), "");
//		    						result4=vec7.get(i+4).toString().replaceAll("｜"+vec8.get(i+4).toString(), "");
//		    						result5=vec7.get(i+5).toString().replaceAll("｜"+vec8.get(i+5).toString(), "");
//		    						result6=vec7.get(i+6).toString().replaceAll("｜"+vec8.get(i+6).toString(), "");
//		    						result7=vec7.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result7=result7.replaceAll("!", "&").replaceAll("@", "+");
//		    						result8=vec7.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result8=result8.replaceAll("!", "&").replaceAll("@", "+");
//		    						result9=vec7.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result9=result9.replaceAll("!", "&").replaceAll("@", "+");
//		    						result10=vec7.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result10=result10.replaceAll("!", "&").replaceAll("@", "+");
//		    						result11=vec7.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result11=result11.replaceAll("!", "&").replaceAll("@", "+");
//		    						result12=vec7.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result12=result12.replaceAll("!", "&").replaceAll("@", "+");
//		    						result13=vec7.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result13=result13.replaceAll("!", "&").replaceAll("@", "+");
//		    						result14=vec7.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//		    						result14=result14.replaceAll("!", "&").replaceAll("@", "+");
//		    					}
		    					
		    					sql +=" update dbo.V_排课管理_课程表明细表 set 班级排课状态='"+MyTools.fixSql(vec7.get(i+1).toString())+"',连节相关编号='"+MyTools.fixSql(vec7.get(i+2).toString())+"',授课计划明细编号='"+MyTools.fixSql(result3)+"',实际场地编号='"+MyTools.fixSql(result11)+"',实际场地名称='"+MyTools.fixSql(result12)+"' " +
		    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"%' " +
		    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' ";  
		    					sql +=" update dbo.V_排课管理_课程表明细详情表 set 班级排课状态='"+MyTools.fixSql(vec7.get(i+1).toString())+"',连节相关编号='"+MyTools.fixSql(vec7.get(i+2).toString())+"',授课计划明细编号='"+MyTools.fixSql(result3)+"',课程代码='"+MyTools.fixSql(result4)+"',课程名称='"+MyTools.fixSql(result5)+"',课程类型='"+MyTools.fixSql(result6)+"',授课教师编号='"+MyTools.fixSql(result7)+"',授课教师姓名='"+MyTools.fixSql(result8)+"',场地要求='"+MyTools.fixSql(result9)+"',场地名称='"+MyTools.fixSql(result10)+"',实际场地编号='"+MyTools.fixSql(result11)+"',实际场地名称='"+MyTools.fixSql(result12)+"',授课周次='"+MyTools.fixSql(result13)+"',授课周次详情='"+MyTools.fixSql(result14)+"' " +
		    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"%' " +
		    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' ";
		    					sql +=" update dbo.V_排课管理_课程表周详情表 set 授课计划明细编号='',课程代码='',课程名称='',课程类型='',授课教师编号='',授课教师姓名='',场地编号='',场地名称='' " +
		    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"%' " +
		    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' ";
		    				}else{
		    					sql +=" update dbo.V_排课管理_课程表明细表 set 班级排课状态='',连节相关编号='',授课计划明细编号='',实际场地编号='',实际场地名称='' " +
		    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"%' " +
		    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' "; 
		    					sql +=" update dbo.V_排课管理_课程表明细详情表 set 班级排课状态='',连节相关编号='',授课计划明细编号='',课程代码='',课程名称='',课程类型='',授课教师编号='',授课教师姓名='',场地要求='',场地名称='',实际场地编号='',实际场地名称='',授课周次='',授课周次详情='' " +
		    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"%' " +
		    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' "; 
		    					sql +=" update dbo.V_排课管理_课程表周详情表 set 授课计划明细编号='',课程代码='',课程名称='',课程类型='',授课教师编号='',授课教师姓名='',场地编号='',场地名称='' " +
		    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GS_SKJHMXBH))+"%' " +
		    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' ";
		    				}
		    			}
		    		}	
		    	
	        }
		
	    if(!iKeyCode.equals("")){
	    	sql+=" delete from V_规则管理_合班表 where 授课计划明细编号  like '%"+MyTools.fixSql(iKeyCode)+"%' ";
	    }
	      
	        
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");		
		}else{
			this.setMSG("删除失败");		
		}
		
	}
	
	/**
	 * 
	 * @date:2015-07-23
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector openTeacher(int pageNum,int pageSize,String teaId,String teaName,String teaLevel,String teacharr) throws WrongSQLException,SQLException{
		Vector vec = null;
		String teaid="";
		if(teacharr.equals("")){//没有教师数据
			teaid="''";
		}else{//选中的教师排在前面显示
			String[] teachers=teacharr.split("\\+");
			for(int i=0;i<teachers.length;i++){
				teaid+="'"+teachers[i]+"',";
			}
			teaid=teaid.substring(0, teaid.length()-1);
		}
		String sql = "select 工号,姓名,'1' as px from dbo.V_教职工基本数据子类 where 工号 in ("+teaid+") " +
					 " union " +
					 " select 工号,姓名,'2' as px from dbo.V_教职工基本数据子类 where 工号 not in ("+teaid+") ";
		if(!teaId.equalsIgnoreCase("")){
			sql += " and 工号 like '%"+MyTools.fixSql(teaId)+"%' ";
		}	
		if(!teaName.equalsIgnoreCase("")){
			sql += " and 姓名 like '%"+MyTools.fixSql(teaName)+"%' ";
		}
		sql += " order by px,工号 ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-07-23
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector openRoom(String selschool,String selhouse,String seltype,String roomarr,String classId) throws WrongSQLException,SQLException{
		Vector vec = null;
		System.out.println(selhouse+"-------------------");
		String roomid="";
		if(roomarr.equals("")){//没有教师数据
			roomid="''";
		}else{//选中的教师排在前面显示
			String[] rooms=roomarr.split("\\+");
			for(int i=0;i<rooms.length;i++){
				roomid+="'"+rooms[i]+"',";
			}
			roomid=roomid.substring(0, roomid.length()-1);
		}
		
		String sql2="";
		if(!selschool.equals("")){
			sql2=" and a.校区代码='"+MyTools.StrFiltr(MyTools.fixSql(selschool))+"'";
		}
		if(!selhouse.equals("")){
			sql2=" and a.建筑物号='"+MyTools.StrFiltr(MyTools.fixSql(selhouse))+"'";
		}
		if(!seltype.equals("")){
			sql2=" and d.编号='"+MyTools.StrFiltr(MyTools.fixSql(seltype))+"'";
		}
		
		String sql3="";
		String sql5="";

		sql3=" and a.教室编号 in (select 教室编号 from V_基础信息_班级信息表 where 班级编号='"+classId+"' ) ";
		sql5=" and a.教室编号 not in (select 教室编号 from V_基础信息_班级信息表 where 班级编号='"+classId+"' ) " +
			 " and a.校区代码 in (select 系部代码 from V_基础信息_班级信息表 where 班级编号='"+classId+"' ) " ;
			
		String sql = "select distinct a.校区代码,a.建筑物号,d.名称,a.教室编号,a.教室名称,a.实际容量,'1' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室编号 in ("+roomid+") "+ sql2 +
				" union " +
				" select distinct a.校区代码,a.建筑物号,d.名称,a.教室编号,a.教室名称,a.实际容量,'2' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号 "+sql3+" and a.教室编号 not in ("+roomid+") and a.是否可用='1' " + sql2 +
				" union " +
				" select distinct a.校区代码,a.建筑物号,d.名称,a.教室编号,a.教室名称,a.实际容量,'3' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室编号 not in (select 教室编号 from V_基础信息_班级信息表 where 教室编号 !='' )  "+sql5+" and a.教室编号 not in ("+roomid+") " + sql2 ;	
		sql+=" and a.是否可用='1' order by px,a.教室编号 ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 选修
	 * @date:2015-07-23
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector openRoomXX(String seltype,String xbdm,String roomarr) throws WrongSQLException,SQLException{
		Vector vec = null;

		String roomid="";
		if(roomarr.equals("")){//没有教师数据
			roomid="''";
		}else{//选中的教师排在前面显示
			String[] rooms=roomarr.split("\\+");
			for(int i=0;i<rooms.length;i++){
				roomid+="'"+rooms[i]+"',";
			}
			roomid=roomid.substring(0, roomid.length()-1);
		}
		String sql = "select distinct d.名称,a.教室编号,a.教室名称,a.实际容量,'1' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室编号 in ("+roomid+") " +
				" union " +
				" select distinct d.名称,a.教室编号,a.教室名称,a.实际容量,'2' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室编号 not in ("+roomid+") " ;
				
//		if(!selschool.equals("")){
//			sql+=" and a.校区代码='"+MyTools.StrFiltr(MyTools.fixSql(selschool))+"'";
//		}
//		if(!selhouse.equals("")){
//			sql+=" and a.建筑物号='"+MyTools.StrFiltr(MyTools.fixSql(selhouse))+"'";
//		}
		if(!seltype.equals("")){
			sql+=" and d.编号='"+MyTools.StrFiltr(MyTools.fixSql(seltype))+"'";
		}
		if(!xbdm.equals("")){
			sql+=" and a.校区代码='"+MyTools.StrFiltr(MyTools.fixSql(xbdm))+"'";
		}
		
		sql+=" and a.是否可用='1' order by px,a.教室编号 ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 显示计算机房信息
	 * @date:2016-09-22
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector openComRoom(String seltype,String roomarr) throws WrongSQLException,SQLException{
		Vector vec = null;

		String roomid="";
		if(roomarr.equals("")){//没有教师数据
			roomid="''";
		}else{//选中的教师排在前面显示
			String[] rooms=roomarr.split("\\+");
			for(int i=0;i<rooms.length;i++){
				roomid+="'"+rooms[i]+"',";
			}
			roomid=roomid.substring(0, roomid.length()-1);
		}
		
		String sql2="";
		if(!seltype.equals("")){
			sql2=" and d.编号='"+MyTools.StrFiltr(MyTools.fixSql(seltype))+"'";
		}
		
		String sql = "select distinct a.校区代码,a.建筑物号,d.名称,a.教室编号,a.教室名称,a.实际容量,'1' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室名称 in ("+roomid+") "+ sql2 +
				" union " +
				" select distinct a.校区代码,a.建筑物号,d.名称,a.教室编号,a.教室名称,a.实际容量,'2' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号 and a.教室名称 not in ("+roomid+") and a.是否可用='1' " + sql2 ;
				//" union " +
				//" select distinct a.校区代码,a.建筑物号,d.名称,a.教室编号,a.教室名称,a.实际容量,'3' as px from dbo.V_教室数据类 a,dbo.V_基础信息_教室类型 d where a.教室类型代码=d.编号  and a.教室编号 not in (select 教室编号 from V_学校班级数据子类 where 教室编号 !='' ) and a.教室编号 not in ("+roomid+") " + sql2 ;	
		sql+=" order by px,a.教室编号 ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	
	/**
	 * @author 2014-1-9
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector schoolCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "select 校区代码 as comboValue, 校区名称 as comboName from V_校区数据类";			
		sql=sqlsel+sql;
		
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2015-9-2
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector houseCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "select 建筑物号 as comboValue, 建筑物名称 as comboName from V_建筑物基本数据类";			
		sql=sqlsel+sql;
		
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2015-9-2
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector classtypeCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "select 编号 as comboValue, 名称 as comboName from V_基础信息_教室类型";			
		sql=sqlsel+sql;
		
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	
	/**
	 * @author 2015-9-2
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector papertypeCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "select '' as comboValue, '仅英语考试选择' as comboName union " +
			"select 'A' as comboValue, 'A' as comboName union " +
			"select 'B' as comboValue, 'B' as comboName union " +
			"select 'C' as comboValue, 'C' as comboName union " +
			"select 'D' as comboValue, 'D' as comboName ";			
		
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2014-1-9
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector schoolCombobox2(String iUSERCODE,String sAuth) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		if(sAuth.indexOf("@01@")>-1||sAuth.indexOf("@211@")>-1||sAuth.indexOf("@212@")>-1||sAuth.indexOf("@213@")>-1){
			sql = "select 系部代码 as comboValue, 系部名称 as comboName from V_基础信息_系部信息表 where 系部代码!='C00' ";	
		}else if(sAuth.indexOf("@214@")>-1||sAuth.indexOf("@215@")>-1){
			sql = "select 系部代码 as comboValue, 系部名称 as comboName from V_基础信息_系部信息表 where 系部代码!='C00' and 系部代码 in (select [系部代码] FROM [dbo].[V_基础信息_系部教师信息表] where [教师编号]='"+MyTools.fixSql(iUSERCODE)+"')";	
		}else{
			sql = "select 系部代码 as comboValue, 系部名称 as comboName from V_基础信息_系部信息表 where 1=2 ";	
		}
	
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2015-9-2
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector houseCombobox2() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "select 建筑物号 as comboValue, 建筑物名称 as comboName from V_建筑物基本数据类";			
		
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2015-11-30
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector floorCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "SELECT distinct substring(教室编号,2,1) as comboValue,case when substring(教室编号,2,1)='1' then '一楼' " +
				" when substring(教室编号,2,1)='2' then '二楼' " +
				" when substring(教室编号,2,1)='3' then '三楼' " +
				" when substring(教室编号,2,1)='4' then '四楼' " +
				" when substring(教室编号,2,1)='5' then '五楼' " +
				" else '其它' end as comboName " +
				" FROM V_教室数据类";			
		
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2015-11-30
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Vector weekCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "SELECT '01' as comboValue,'周一' as comboName " +
				" union SELECT '02' as comboValue,'周二' as comboName " +
				" union SELECT '03' as comboValue,'周三' as comboName " +
				" union SELECT '04' as comboValue,'周四' as comboName " +
				" union SELECT '05' as comboValue,'周五' as comboName ";			
		
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2015-11-30
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 * 查询教室课表
	 */
	@SuppressWarnings("unchecked")
	public String loadTable(String school,String week,String xnxq,String jxxz) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		Vector vector2 = null;
		Vector vector3 = null;
		Vector vector4 = null;
		Vector vector5 = null;
		Vector vec = new Vector();
		Vector vec2 = new Vector();
		Vector vec3 = new Vector();
		Vector vec6 = null;
		Vector vec7 = null;
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String sql7 = "";
		String classroom="";
		String classname="";
		String timedata="";
		
		if(school.equals("")&&week.equals("")&&xnxq.equals("")&&jxxz.equals("")){
			school="C01";
			//house="01";
			//floor="0";
			week="01";
			jxxz="01";
			sql4="select 学年+学期 from [dbo].[V_规则管理_学年学期表] order by [学年学期编码] desc";
			vector4=db.GetContextVector(sql4);
			xnxq=vector4.get(0).toString();
		}
		
		sql = "SELECT 教室编号,教室名称 FROM V_教室数据类 where 校区代码='"+MyTools.fixSql(school)+"' and 是否可用='1' ";			
		vector=db.GetContextVector(sql);
		if(vector!=null&&vector.size()>0){
			for(int i=0;i<vector.size();i=i+2){
				classroom+=vector.get(i).toString()+",";
				classname+=vector.get(i+1).toString()+",";
			}
			classroom=classroom.substring(0,classroom.length()-1);
			classname=classname.substring(0,classname.length()-1);
			this.setMSG(classroom);
			this.setXZBDM(classname);
		}
		
		sql2 = "select 节次  from V_规则管理_节次时间表 where 学年学期编码='" + MyTools.fixSql(MyTools.StrFiltr(xnxq)) + MyTools.fixSql(MyTools.StrFiltr(jxxz)) + "' order by 开始时间";
		vector2=db.GetContextVector(sql2);
		if(vector2!=null&&vector2.size()>0){
			for(int i=0;i<vector2.size();i++){
				timedata+=vector2.get(i).toString()+",";
			}
			timedata=timedata.substring(0,timedata.length()-1);
			this.setMSG2(timedata);
		}
		
		//20160823,原查询代码,现改用V_排课管理_课程表周详情表查询
		//查询调整后的调课信息表
//		sql7=" SELECT [编号],[授课计划明细编号],[原计划授课周次],[原计划星期],[原计划时间序列],[原计划授课教师编号],[原计划授课教师名称],[原计划场地编号],[原计划场地名称], " +
//			 " [调整后授课周次],[调整后星期],[调整后时间序列],[调整后授课教师编号],[调整后授课教师名称],[调整后场地编号],[调整后场地名称] " +
//			 " FROM V_调课管理_调课信息主表 where [审核状态]='1' and (原计划授课周次<>'' or 调整后授课周次<>'') order by 编号 ";
//		vec7=db.GetContextVector(sql7);
//			
//		if(vec7!=null&&vec7.size()>0){
//			for(int u=0;u<vec7.size();u=u+16){
//				String weekyj=vec7.get(u+2).toString().replaceAll(",", "\\#");//原计划授课周次
//				String timeyf=vec7.get(u+3).toString();//原计划星期
//				String timeyl=vec7.get(u+4).toString();//原计划时间序列
//				String weektj=vec7.get(u+9).toString().replaceAll(",", "\\#");//调整后授课周次
//				String timetf=vec7.get(u+10).toString();//调整后星期
//				String timetl=vec7.get(u+11).toString();//调整后时间序列
//				if(weekyj.equals("")||weektj.equals("")){
//					
//				}else{
//					String[] timeyls=timeyl.split(",");
//					String[] timetls=timetl.split(",");
//					for(int v=0;v<timetls.length;v++){
//						sqltk+=" select '"+vec7.get(u).toString()+"' as 编号,'"+vec7.get(u+1).toString()+"' as 授课计划明细编号,'"+weekyj+"' as 原计划授课周次,'"+(timeyf+timeyls[v])+"' as 原计划时间序列,'"+vec7.get(u+5).toString()+"' as 原计划授课教师编号,'"+vec7.get(u+6).toString()+"' as 原计划授课教师名称,'"+vec7.get(u+7).toString()+"' as 原计划场地编号,'"+vec7.get(u+8).toString()+"' as 原计划场地名称," +
//							   " '"+weektj+"' as 调整后授课周次,'"+(timetf+timetls[v])+"' as 调整后时间序列,'"+vec7.get(u+12).toString()+"' as 调整后授课教师编号,'"+vec7.get(u+13).toString()+"' as 调整后授课教师名称,'"+vec7.get(u+14).toString()+"' as 调整后场地编号,'"+vec7.get(u+15).toString()+"' as 调整后场地名称" +
//							   " union " ;
//					}
//				}
//			}
//			sqltk=sqltk.substring(0, sqltk.length()-"union ".length());
//			vectk=db.GetContextVector(sqltk);
//			
//		}
//		
//			sql=" SELECT a.授课计划明细编号,a.学年学期编码,a.授课教师编号,a.授课教师姓名,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.时间序列,a.实际场地编号,a.实际场地名称,a.授课周次详情,a.状态 " +
//				" FROM V_排课管理_课程表明细详情表 a " +
//				" where a.授课计划明细编号 != '' and a.状态='1' " ;
//			vec=db.GetContextVector(sql);
//			if(vec!=null&&vec.size()>0){
//				for(int i=0;i<vec.size();i=i+13){
//					skjhbh=vec.get(i).toString();//授课计划明细编号
//					xnxqbm=vec.get(i+1).toString();//学年学期编码
//					skjsbh=vec.get(i+2).toString();//授课教师编号
//					skjsxm=vec.get(i+3).toString();//授课教师姓名
//					kechid=vec.get(i+4).toString();//课程代码
//					kechna=vec.get(i+5).toString();//课程名称
//					clasid=vec.get(i+6).toString();//行政班代码
//					clasna=vec.get(i+7).toString();//行政班名称
//					timexl=vec.get(i+8).toString();//时间序列
//					sjcdbh=vec.get(i+9).toString();//实际场地编号
//					sjcdmc=vec.get(i+10).toString();//实际场地名称
//					skzcxq=vec.get(i+11).toString();//授课周次详情
//					zt=vec.get(i+12).toString();//状态
//					
//					
//					String[] skjhbh2=skjhbh.split("｜");
//					String[] skjsbh2=skjsbh.split("｜");
//					String[] skjsxm2=skjsxm.split("｜");
//					String[] kechid2=kechid.split("｜");
//					String[] kechna2=kechna.split("｜");
//					String[] sjcdbh2=sjcdbh.split("｜");
//					String[] sjcdmc2=sjcdmc.split("｜");
//					String[] skzcxq2=skzcxq.split("｜");
//					for(int j=0;j<skjhbh2.length;j++){
//						
//						String[] skjsbh3=skjsbh2[j].split("\\&");
//						String[] skjsxm3=skjsxm2[j].split("\\&");
//						String[] sjcdbh3=sjcdbh2[j].split("\\&");
//						String[] sjcdmc3=sjcdmc2[j].split("\\&");
//						String[] skzcxq3=skzcxq2[j].split("\\&");
//						for(int k=0;k<skjsbh3.length;k++){
//							
//							int tag=0;
//							if(!sqltk.equals("")){
//								for(int w=0;w<vectk.size();w=w+14){
//									if(vectk.get(w+1).toString().equals(skjhbh2[j])&&vectk.get(w+3).toString().equals(timexl)){//授课计划和时间序列都相等
//										System.out.println("name:--"+vectk.get(w+11).toString());
//										if(d==0){
//											sqlpkgl+=" select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+vectk.get(w+9).toString()+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+vectk.get(w+10).toString()+"' as 授课教师编号,'"+vectk.get(w+11).toString()+"' as 授课教师姓名,'"+vectk.get(w+12).toString()+"' as 实际场地编号,'"+vectk.get(w+13).toString()+"' as 实际场地名称,'"+vectk.get(w+8).toString()+"' as 授课周次详情,'"+zt+"' as 状态 ";
//										}else{
//											sqlpkgl+=" union all select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+vectk.get(w+9).toString()+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+vectk.get(w+10).toString()+"' as 授课教师编号,'"+vectk.get(w+11).toString()+"' as 授课教师姓名,'"+vectk.get(w+12).toString()+"' as 实际场地编号,'"+vectk.get(w+13).toString()+"' as 实际场地名称,'"+vectk.get(w+8).toString()+"' as 授课周次详情,'"+zt+"' as 状态 ";
//										}
//										tag=1;
//									}
//								}
//							}
//							if(tag==0){
//								if(d==0){
//									sqlpkgl+=" select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+timexl+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+skjsbh3[k]+"' as 授课教师编号,'"+skjsxm3[k]+"' as 授课教师姓名,'"+sjcdbh3[k]+"' as 实际场地编号,'"+sjcdmc3[k]+"' as 实际场地名称,'"+skzcxq3[k]+"' as 授课周次详情,'"+zt+"' as 状态 ";
//								}else{
//									sqlpkgl+=" union all select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+timexl+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+skjsbh3[k]+"' as 授课教师编号,'"+skjsxm3[k]+"' as 授课教师姓名,'"+sjcdbh3[k]+"' as 实际场地编号,'"+sjcdmc3[k]+"' as 实际场地名称,'"+skzcxq3[k]+"' as 授课周次详情,'"+zt+"' as 状态 ";
//								}
//							}
//							d++;			
//						}
//					}
//				}	
//			
//		}
//		
//		String sqlins=" delete from V_教室查询_课程表明细详情表 ";
//		sqlins+=" insert into V_教室查询_课程表明细详情表  " + sqlpkgl;
//		db.executeInsertOrUpdate(sqlins);
//		
//		if(vector!=null&&vector.size()>0){
//			for(int i=0;i<vector.size();i++){
//				if(i==0){
//					sql3+="select stuff((select '｜'+d.行政班名称 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 行政班名称," +
//						"c.时间序列,stuff((select '｜'+d.课程名称 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 课程名称," +
//						"stuff((select '｜'+d.授课教师姓名 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课教师姓名," +
//						"c.实际场地编号,c.实际场地名称,stuff((select '｜'+d.授课周次详情 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课周次详情  " +
//						"from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) c " +
//						"group by c.时间序列,c.实际场地编号,c.实际场地名称  ";
//				}else{
//					sql3+=" union select stuff((select '｜'+d.行政班名称 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 行政班名称," +
//						"c.时间序列,stuff((select '｜'+d.课程名称 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 课程名称," +
//						"stuff((select '｜'+d.授课教师姓名 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课教师姓名," +
//						"c.实际场地编号,c.实际场地名称,stuff((select '｜'+d.授课周次详情 from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) d where c.时间序列=d.时间序列   and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课周次详情  " +
//						"from (SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地编号,a.实际场地名称,a.授课周次详情 FROM V_教室查询_课程表明细详情表 a where a.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(i).toString())+"%' ) c " +
//						"group by c.时间序列,c.实际场地编号,c.实际场地名称  ";
//				}
//			}
//			sql3+=" order by c.时间序列 ";
//			vector3 = dbSource.getConttexJONSArr(sql3, 0, 0);
//			
//		}
		
		String skjhbh=null;//授课计划明细编号
		String xnxqbm=null;//学年学期编码
		String skjsbh=null;//授课教师编号
		String skjsxm=null;//授课教师姓名
		String zyname=null;//专业名称
		String kechid=null;//课程代码
		String kechna=null;//课程名称
		String clasid=null;//行政班代码
		String clasna=null;//行政班名称
		String timexl=null;//时间序列
		String sjcdbh=null;//实际场地编号
		String sjcdmc=null;//实际场地名称
		String skzcxq=null;//授课周次详情
		String zt=null;//状态
		int d=0;
		String sqlpkgl="";
		String sqlpkgl2="";
		Vector vectk = null;
		String sqltk="";//调课
		
		String cdbh="";
		String cdbh2="";
		for(int i=0;i<vector.size();i=i+2){
			cdbh+="场地编号 like '%"+vector.get(i).toString()+"%' or ";
			cdbh2+="a.场地要求 like '%"+vector.get(i).toString()+"%' or ";
		}
		
		if(!cdbh.equals("")){
			cdbh=cdbh.substring(0, cdbh.length()-3);
//			sql3="select distinct c.行政班名称,c.时间序列,c.课程名称,c.授课教师姓名,c.实际场地编号,c.实际场地名称," +
//				"stuff((select '#'+d.授课周次 from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) d " +
//				"where c.行政班名称=d.行政班名称 and c.时间序列=d.时间序列 and c.课程名称=d.课程名称 and c.授课教师姓名=d.授课教师姓名 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称 for xml path('')),1,1,'') as 授课周次详情 " +
//				"from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) c ";
//			String sql33="select f.行政班名称,f.时间序列,f.课程名称,f.授课教师姓名,f.实际场地编号,f.实际场地名称,f.授课周次详情 from (" +
//						 "select e.行政班名称,e.时间序列,e.课程名称,e.授课教师姓名,e.实际场地编号,e.实际场地名称,e.授课周次详情,substring(e.授课周次详情,0,charindex('#',e.授课周次详情)) as px from ("+sql3+") e ) f " +
//						 "order by f.实际场地编号,f.时间序列,convert(int,f.px),f.授课教师姓名 ";
//			
			sql3="select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+") order by 实际场地编号,时间序列,授课教师姓名,行政班名称,cast(授课周次 as int) ";
			vector3=db.GetContextVector(sql3);
			
			if(vector3!=null&&vector3.size()>0){	
				String xzbmc=vector3.get(0).toString();
				String sjxl=vector3.get(1).toString();
				String kcmc=vector3.get(2).toString();
				String jsxm=vector3.get(3).toString();
				String cdid=vector3.get(4).toString();
				String cdmc=vector3.get(5).toString();
				String skzc=vector3.get(6).toString();
				for(int r=7;vector3.size()>7;){	
					if(vector3.get(r-7).toString().equals(vector3.get(r).toString())&&
					   vector3.get(r-7+1).toString().equals(vector3.get(r+1).toString())&&
					   vector3.get(r-7+2).toString().equals(vector3.get(r+2).toString())&&
					   vector3.get(r-7+3).toString().equals(vector3.get(r+3).toString())&&
					   vector3.get(r-7+4).toString().equals(vector3.get(r+4).toString())){
							
					    skzc+="#"+vector3.get(r+6).toString();
					    
					    for(int k=0;k<7;k++){
					    	vector3.remove(r);
					    }
					}else{
						vec3.add(xzbmc);//[行政班名称] 
						vec3.add(sjxl);//[时间序列] 
						vec3.add(kcmc);//[课程名称] 
						vec3.add(jsxm);//[授课教师姓名] 
						vec3.add(cdid);//[实际场地编号] 
						vec3.add(cdmc);//[实际场地名称] 
						vec3.add(skzc);//[授课周次详情] 	
						
						xzbmc=vector3.get(r).toString();
						sjxl=vector3.get(r+1).toString();
						kcmc=vector3.get(r+2).toString();
						jsxm=vector3.get(r+3).toString();
						cdid=vector3.get(r+4).toString();
						cdmc=vector3.get(r+5).toString();
						skzc=vector3.get(r+6).toString();
						
						for(int k=0;k<7;k++){
					    	vector3.remove(r-7);
					    }
					}
				}
				vec3.add(xzbmc);//[行政班名称] 
				vec3.add(sjxl);//[时间序列] 
				vec3.add(kcmc);//[课程名称] 
				vec3.add(jsxm);//[授课教师姓名] 
				vec3.add(cdid);//[实际场地编号] 
				vec3.add(cdmc);//[实际场地名称] 
				vec3.add(skzc);//[授课周次详情] 	
				
				for(int k=0;k<7;k++){
			    	vector3.remove(0);
			    }
				
				for(int i=0;i<vec3.size();i=i+7){
					skzcxq=merge(vec3.get(i+6).toString());
					if(vec3.get(i+4).toString().indexOf("+")>-1){
						String[] cdbhs=vec3.get(i+4).toString().split("\\+");
						String[] cdmcs=vec3.get(i+5).toString().split("\\+");
						for(int j=0;j<cdbhs.length;j++){
							vec.add(vec3.get(i).toString());//[行政班名称] 
							vec.add(vec3.get(i+1).toString());//[时间序列] 
							vec.add(vec3.get(i+2).toString());//[课程名称] 
							vec.add(vec3.get(i+3).toString());//[授课教师姓名] 
							vec.add(cdbhs[j]);//[实际场地编号] 
							vec.add(cdmcs[j]);//[实际场地名称] 
							vec.add(skzcxq);//[授课周次详情] 	
						}
					}else{
						vec.add(vec3.get(i).toString());//[行政班名称] 
						vec.add(vec3.get(i+1).toString());//[时间序列] 
						vec.add(vec3.get(i+2).toString());//[课程名称] 
						vec.add(vec3.get(i+3).toString());//[授课教师姓名] 
						vec.add(vec3.get(i+4).toString());//[实际场地编号] 
						vec.add(vec3.get(i+5).toString());//[实际场地名称] 
						vec.add(skzcxq);//[授课周次详情] 		
					}
				}
			}
			
			//选修课
			if(!cdbh2.equals("")){
				cdbh2=cdbh2.substring(0, cdbh2.length()-3);
				sql6="select a.选修班名称 as 行政班名称,a.上课时间 as 时间序列,b.课程名称,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and ("+cdbh2+") order by a.场地要求,a.上课时间 ";
				vec6=db.GetContextVector(sql6);
				if(vec6!=null&&vec6.size()>0){
					for(int i=0;i<vec6.size();i=i+7){
						String[] sjxls=vec6.get(i+1).toString().split(",");
						for(int j=0;j<sjxls.length;j++){
							//System.out.println(sjxls[j].substring(0, 2)+"|"+week);
							if(sjxls[j].substring(0, 2).equals(week)){//授课周次相同
								vec.add(vec6.get(i).toString());//[行政班名称] 
								vec.add(sjxls[j]);//[时间序列] 
								vec.add(vec6.get(i+2).toString());//[课程名称] 
								vec.add(vec6.get(i+3).toString());//[授课教师姓名] 
								vec.add(vec6.get(i+4).toString());//[实际场地编号] 
								vec.add(vec6.get(i+5).toString());//[实际场地名称] 
								vec.add(vec6.get(i+6).toString());//[授课周次详情] 
							}
						}
					}
				}
			}
		}
		

        //场地，时间相同的合并
		for(int i=0;vec.size()>1;){
			String icxzbmc="";
			String ickcmc="";
			String icskjsxm="";
			String icskzcxq="";
			icxzbmc=vec.get(i).toString();
			ickcmc=vec.get(i+2).toString();
			icskjsxm=vec.get(i+3).toString();
			icskzcxq=vec.get(i+6).toString();
			for(int j=7;j<vec.size();j=j+7){
				//System.out.println(vec.size()+"|"+j);
				if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+4).toString().equals(vec.get(j+4).toString())){
					icxzbmc+="｜"+vec.get(j).toString();
					ickcmc+="｜"+vec.get(j+2).toString();
					icskjsxm+="｜"+vec.get(j+3).toString();
					icskzcxq+="｜"+vec.get(j+6).toString();
					for(int k=0;k<7;k++){
						vec.remove(j);
					}
				}
			}
			//System.out.println(vec.toString()+"|"+i);
			
			vec2.add(icxzbmc);//[行政班名称] 
			vec2.add(vec.get(i+1).toString());//[时间序列] 
			vec2.add(ickcmc);//[课程名称] 
			vec2.add(icskjsxm);//[授课教师姓名] 
			vec2.add(vec.get(i+4).toString());//[实际场地编号] 
			vec2.add(vec.get(i+5).toString());//[实际场地名称] 
			vec2.add(icskzcxq);//[授课周次详情] 
			
			for(int k=0;k<7;k++){
				vec.remove(i);
			}
		}
		
		//备份
//		if(!cdbh.equals("")){
//			cdbh=cdbh.substring(0, cdbh.length()-3);
//			sql3="select distinct c.行政班名称,c.时间序列,c.课程名称,c.授课教师姓名,c.实际场地编号,c.实际场地名称," +
//				"stuff((select '#'+d.授课周次 from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) d " +
//				"where c.行政班名称=d.行政班名称 and c.时间序列=d.时间序列 and c.课程名称=d.课程名称 and c.授课教师姓名=d.授课教师姓名 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称 for xml path('')),1,1,'') as 授课周次详情 " +
//				"from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) c order by c.实际场地编号,c.时间序列 ";
//			vector3=db.GetContextVector(sql3);
//			if(vector3!=null&&vector3.size()>0){
//				for(int i=0;i<vector3.size();i=i+7){
//					skzcxq=merge(vector3.get(i+6).toString());
//					sqlpkgl+="select '"+vector3.get(i).toString()+"' as 行政班名称,'"+vector3.get(i+1).toString()+"' as 时间序列,'"+vector3.get(i+2).toString()+"' as 课程名称,'"+vector3.get(i+3).toString()+"' as 授课教师姓名,'"+vector3.get(i+4).toString()+"' as 实际场地编号,'"+vector3.get(i+5).toString()+"' as 实际场地名称,'"+skzcxq+"' as 授课周次详情 union ";
//				}
//				sqlpkgl=sqlpkgl.substring(0, sqlpkgl.length()-6);
//			}
//				
//			//选修课
//			if(!cdbh2.equals("")){
//				cdbh2=cdbh2.substring(0, cdbh2.length()-3);
//				sql6="select a.选修班名称 as 行政班名称,a.上课时间 as 时间序列,b.课程名称,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and ("+cdbh2+")";
//				vec6=db.GetContextVector(sql6);
//				if(vec6!=null&&vec6.size()>0){
//					for(int i=0;i<vec6.size();i=i+7){
//						String[] sjxls=vec6.get(i+1).toString().split(",");
//						for(int j=0;j<sjxls.length;j++){
//							sqlpkgl2+=" select '"+vec6.get(i).toString()+"' as 行政班名称,'"+sjxls[j]+"' as 时间序列,'"+vec6.get(i+2).toString()+"' as 课程名称,'"+vec6.get(i+3).toString()+"' as 授课教师姓名,'"+vec6.get(i+4).toString()+"' as 实际场地编号,'"+vec6.get(i+5).toString()+"' as 实际场地名称,'"+vec6.get(i+6).toString()+"' as 授课周次详情  union ";
//						}
//					}
//					sqlpkgl2=sqlpkgl2.substring(0, sqlpkgl2.length()-6);
//				}
//			}
//			if(sqlpkgl2.equals("")){
//				sql7="";
//			}else{
//				sql7="select * from ("+sqlpkgl2+") e where substring(时间序列,1,2)='"+week+"' ";
//				sqlpkgl=sqlpkgl+" union "+sql7;
//			}
//		}
//		if(!sqlpkgl.equals("")){
//			sql5="select stuff((select '｜'+d.行政班名称 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 行政班名称,c.时间序列," +
//				"stuff((select '｜'+d.课程名称 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 课程名称, " +
//				"stuff((select '｜'+d.授课教师姓名 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课教师姓名,实际场地编号,实际场地名称," +
//				"stuff((select '｜'+d.授课周次详情 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课周次详情 " +
//				" from ("+sqlpkgl+") c ";
//		}else{
//			sql5=" select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表 where 1=2 "; 
//		}
//		vector5=db.getConttexJONSArr(sql5, 0, 0);
		String revec="";
		for(int m=0;m<vec2.size();m=m+7){
			revec+="{\"行政班名称\":\""+vec2.get(m).toString()+"\",\"时间序列\":\""+vec2.get(m+1).toString()+"\",\"课程名称\":\""+vec2.get(m+2).toString()+"\",\"授课教师姓名\":\""+vec2.get(m+3).toString()+"\",\"实际场地编号\":\""+vec2.get(m+4).toString()+"\",\"实际场地名称\":\""+vec2.get(m+5).toString()+"\",\"授课周次详情\":\""+vec2.get(m+6).toString()+"\"},";
		}
		if(!revec.equals("")){
			revec="["+revec.substring(0, revec.length()-1)+"]";
		}
		//System.out.println("revec:--"+revec);
		return revec;
	}
	
	
	/**
	 * @author 2015-11-30
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 * 查询教室课表
	 */
	
	/** 
	*字符串的日期格式的计算 
	*/  
	public static int daysBetween(String smdate,String bdate) throws ParseException{  
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(sdf.parse(smdate));    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(sdf.parse(bdate));    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));     
	} 
	
	@SuppressWarnings("unchecked")
	public String loadComputerRoom() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		Vector vector2 = null;
		Vector vector3 = null;
		Vector vector4 = null;
		Vector vector5 = null;
		Vector vec = null;
		Vector vec5 = null;
		Vector vec6 = null;
		Vector vec7 = null;
		Vector veckc = new Vector();
		Vector vecxx = new Vector();
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String sql7 = "";
		String classroom="";
		String timedata="";
        
		String school="01";//校区
		String house="05";//建筑物号
		String floor="'2','3','4'";//楼层
		int weeknum=-1;
		String xqkssj="";//学期开始时间
		String week="";//星期几
		String xnxq="";//学年学期编码
		String hour="";//小时
		String minute="";//分钟
		String sjd="";//上午，中午，晚上
		int weektime=-1;//第几周
		
		sql="select 学年学期编码,convert(varchar(10),[学期开始时间],21),convert(varchar(10),[学期结束时间],21) from V_规则管理_学年学期表 where [学年学期编码]>'2015101' ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			for(int i=0;i<vec.size();i=i+3){
				Calendar cal =Calendar.getInstance();
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        //System.out.println(df.format(cal.getTime()));//当前时间
		        //System.out.println(vec.get(i+1).toString());
		        //System.out.println(vec.get(i+2).toString());
		        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        try {
					Date dt1 = df.parse(df.format(cal.getTime()));
					Date dt2 = df.parse(vec.get(i+1).toString());
					Date dt3 = df.parse(vec.get(i+2).toString());
					 if (dt2.getTime() <= dt1.getTime()&&dt1.getTime() <= dt3.getTime()) {//获取学年学期编号
						 xnxq=vec.get(i).toString();
						 xqkssj=vec.get(i+1).toString();
			         }else{//无操作
			        	
			         } 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
			}
		}

		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(df.parse(xqkssj));
			//学期开始是一年的第几周
			int beginweek=cal.get(Calendar.WEEK_OF_YEAR);
					
			//学期开始日期是星期几
			int begindate = cal.get(Calendar.DAY_OF_WEEK)-1;
			
        	Date now = new Date(); 
        	DateFormat d1 = DateFormat.getDateTimeInstance(); 
        	String str1 = d1.format(now);
			cal.setTime(df.parse(str1));
			//System.out.println("day:--"+str1);
			
			//当天是一年的第几周
			//int curweek=cal.get(Calendar.WEEK_OF_YEAR);
			
			try {
				weektime=(daysBetween(xqkssj,str1)+begindate)/7+1;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			DateFormat d2 = DateFormat.getTimeInstance();
			String str2 = d2.format(now);
			//System.out.println("time:--"+str2);
			hour=str2.substring(0,str2.indexOf(":"));
			minute=str2.substring(str2.indexOf(":")+1,str2.indexOf(":")+3);
			System.out.println(hour+":"+minute);
 
			if(5<Integer.parseInt(hour)&&Integer.parseInt(hour+minute)<1115){//上午
				sjd="1";
			}else if(1115<Integer.parseInt(hour+minute)&&Integer.parseInt(hour+minute)<1230){//中午
				sjd="2";
			}else if(1230<Integer.parseInt(hour+minute)&&Integer.parseInt(hour)<17){//下午
				sjd="3";
			}else if(16<Integer.parseInt(hour)&&Integer.parseInt(hour)<=23||0<=Integer.parseInt(hour)&&Integer.parseInt(hour)<6){//晚上
				sjd="4";
			}else{
				sjd="0";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    weeknum = cal.get(Calendar.DAY_OF_WEEK)-1;
	    //System.out.println("week:--"+weeknum);
	    if(weeknum==1){
	    	week="01";
	    }else if(weeknum==2){
	    	week="02";
	    }else if(weeknum==3){
	    	week="03";
	    }else if(weeknum==4){
	    	week="04";
	    }else if(weeknum==5){
	    	week="05";
	    }else if(weeknum==6){
	    	week="06";
	    }else if(weeknum==0){
	    	week="07";
	    }
	   
//	    String cdbh="'5201','5202','5203','5204','5206','5207','5209','5211','5212','5302','5303','5304','5305','5307','5308','5310','5312','5313','5404','5407','5408'";
//	    classroom="5201,5202,5203,5204,5206,5207,5209,5211,5212,5302,5303,5304,5305,5307,5308,5310,5312,5313,5404,5407,5408";
//	    this.setMSG(classroom);
	    
	    String cdbh="";
		sql = "SELECT 显示教室 FROM dbo.V_机房课表_显示教室 ";			
		vector=db.GetContextVector(sql);
		if(vector!=null&&vector.size()>0){
			String[] xsjs=vector.get(0).toString().split(",");
			for(int i=0;i<xsjs.length;i++){
				cdbh+="'"+xsjs[i]+"',";
			}
			cdbh=cdbh.substring(0, cdbh.length()-1);
			this.setMSG(vector.get(0).toString());
		}
		
	    String sqlsjd="";
	    //System.out.println("sjd:--"+sjd);
	    String jieci="";
		if(sjd.equals("1")){
			sqlsjd=" and (时间序列 like '%01' or 时间序列 like '%02' or 时间序列 like '%03' or 时间序列 like '%04') ";
			jieci=" and 节次 like '上午%' ";
		}else if(sjd.equals("2")){
			sqlsjd=" and (时间序列 like '%05' or 时间序列 like '%06') ";
			jieci=" and 节次 like '中午%' ";
		}else if(sjd.equals("3")){
			sqlsjd=" and (时间序列 like '%07' or 时间序列 like '%08' or 时间序列 like '%09' or 时间序列 like '%10') ";
			jieci=" and 节次 like '下午%' ";
		}else if(sjd.equals("4")){
			sqlsjd=" and (时间序列 like '%11' or 时间序列 like '%12') ";
			jieci="";
		}
	    
		sql2 = "select 节次,开始时间,结束时间  from V_规则管理_节次时间表 where 学年学期编码='" + MyTools.fixSql(MyTools.StrFiltr(xnxq)) + "' "+jieci+"  order by 开始时间";
		vector2=db.GetContextVector(sql2);
		if(vector2!=null&&vector2.size()>0){
			for(int i=0;i<vector2.size();i=i+3){
				timedata+=vector2.get(i).toString()+"<br />"+vector2.get(i+1).toString()+"--"+vector2.get(i+2).toString()+",";
			}
			timedata=timedata.substring(0,timedata.length()-1);
			if(sjd.equals("4")){
				timedata="晚上1,晚上2";
				this.setMSG2(timedata+"#"+sjd);
			}else if(sjd.equals("2")){
				timedata="中午1,中午2";
				this.setMSG2(timedata+"#"+sjd);
			}else{
				this.setMSG2(timedata+"#"+sjd);
			}
		}
		
		String zt=null;//状态
		int d=0;
		String sqlpkgl="";
		String sqlpkgl2="";
		Vector vectk = null;
		String sqltk="";//调课
		
//		sql3="select distinct c.行政班名称,c.时间序列,c.课程名称,c.授课教师姓名,c.实际场地编号,c.实际场地名称," +
//			"stuff((select '#'+d.授课周次 from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq)+"' and substring(时间序列,1,2)='"+week+"' and 场地编号 in ("+cdbh+") "+sqlsjd+") d " +
//			"where c.行政班名称=d.行政班名称 and c.时间序列=d.时间序列 and c.课程名称=d.课程名称 and c.授课教师姓名=d.授课教师姓名 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称 for xml path('')),1,1,'') as 授课周次详情 " +
//			"from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq)+"' and substring(时间序列,1,2)='"+week+"' and 场地编号 in ("+cdbh+") "+sqlsjd+") c order by c.实际场地编号,c.时间序列";
//		vector3=db.GetContextVector(sql3);
//		if(vector3!=null&&vector3.size()>0){
//			for(int i=0;i<vector3.size();i=i+7){
//				skzcxq=merge(vector3.get(i+6).toString());
//				sqlpkgl+="select '"+vector3.get(i).toString()+"' as 行政班名称,'"+vector3.get(i+1).toString()+"' as 时间序列,'"+vector3.get(i+2).toString()+"' as 课程名称,'"+vector3.get(i+3).toString()+"' as 授课教师姓名,'"+vector3.get(i+4).toString()+"' as 实际场地编号,'"+vector3.get(i+5).toString()+"' as 实际场地名称,'"+skzcxq+"' as 授课周次详情 union ";
//			}
//			sqlpkgl=sqlpkgl.substring(0, sqlpkgl.length()-6);
//		
//			sql5="select stuff((select '｜'+d.行政班名称 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 行政班名称,c.时间序列," +
//				"stuff((select '｜'+d.课程名称 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 课程名称, " +
//				"stuff((select '｜'+d.授课教师姓名 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课教师姓名,实际场地编号,实际场地名称," +
//				"stuff((select '｜'+d.授课周次详情 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课周次详情 " +
//				" from ("+sqlpkgl+") c order by c.实际场地编号,c.时间序列";
//			vector5=db.getConttexJONSArr(sql5, 0, 0);
//		}
		
		//查询排课表
		sql5="select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq)+"' and substring(时间序列,1,2)='"+week+"' and 场地编号 in ("+cdbh+") "+sqlsjd+" and 授课周次='"+weektime+"' ";
		vec5=db.GetContextVector(sql5);
		if(vec5!=null&&vec5.size()>0){
			for(int i=0;i<vec5.size();i=i+7){
				veckc.add(vec5.get(i).toString());//行政班名称
				veckc.add(vec5.get(i+1).toString());//时间序列
				veckc.add(vec5.get(i+2).toString());//课程名称
				veckc.add(vec5.get(i+3).toString());//授课教师姓名
				veckc.add(vec5.get(i+4).toString());//实际场地编号
				veckc.add(vec5.get(i+5).toString());//实际场地名称
				veckc.add(vec5.get(i+6).toString());//授课周次
			}
		}
		
		//选修课
		sql6="select a.选修班名称 as 行政班名称,a.上课时间 as 时间序列,b.课程名称,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.学年学期编码='"+MyTools.fixSql(xnxq)+"' and a.场地要求 in ("+cdbh+") ";
		//机房自建课程考试
		sql6+=" union select [班级名称] as 行政班名称,[使用时间] as 时间序列,[课程考试名称] as 课程名称,[使用教师] as 授课教师姓名,[使用教室] as 场地要求,[使用教室] as 场地名称,[使用周次] as 授课周次 from dbo.V_机房课表_课程考试明细表 where 学年学期编码='"+MyTools.fixSql(xnxq)+"' ";
		vec6=db.GetContextVector(sql6);
		if(vec6!=null&&vec6.size()>0){
			for(int i=0;i<vec6.size();i=i+7){
				String[] sjxls=vec6.get(i+1).toString().split(",");
				for(int j=0;j<sjxls.length;j++){
					if(vec6.get(i+6).toString().indexOf("-")>-1){//连续
						String firstweek=vec6.get(i+6).toString().split("-")[0];
						String lastweek=vec6.get(i+6).toString().split("-")[1];
						if(Integer.parseInt(firstweek)<=weektime&&weektime<=Integer.parseInt(lastweek)){//在范围内，显示选修课排课
							if(vec6.get(i+4).toString().indexOf("+")>-1){//多个教室
								String[] singleroom=vec6.get(i+4).toString().split("\\+");
								for(int m=0;m<singleroom.length;m++){
									vecxx.add(vec6.get(i).toString());//行政班名称
									vecxx.add(sjxls[j]);//时间序列
									vecxx.add(vec6.get(i+2).toString());//课程名称
									vecxx.add(vec6.get(i+3).toString());//授课教师姓名
									vecxx.add(singleroom[m]);//实际场地编号
									vecxx.add(singleroom[m]);//实际场地名称
									vecxx.add(weektime);//授课周次
								}
							}else{
								vecxx.add(vec6.get(i).toString());//行政班名称
								vecxx.add(sjxls[j]);//时间序列
								vecxx.add(vec6.get(i+2).toString());//课程名称
								vecxx.add(vec6.get(i+3).toString());//授课教师姓名
								vecxx.add(vec6.get(i+4).toString());//实际场地编号
								vecxx.add(vec6.get(i+5).toString());//实际场地名称
								vecxx.add(weektime);//授课周次
							}							
						}
					}else if(vec6.get(i+6).toString().indexOf("#")>-1){//不连续
						String[] singleweek=vec6.get(i+6).toString().split("#");
						for(int k=0;k<singleweek.length;k++){
							if(vec6.get(i+6).toString().indexOf(weektime)>-1){//weektime存在于授课周次中
								if(vec6.get(i+4).toString().indexOf("+")>-1){//多个教室
									String[] singleroom=vec6.get(i+4).toString().split("\\+");
									for(int m=0;m<singleroom.length;m++){
										vecxx.add(vec6.get(i).toString());//行政班名称
										vecxx.add(sjxls[j]);//时间序列
										vecxx.add(vec6.get(i+2).toString());//课程名称
										vecxx.add(vec6.get(i+3).toString());//授课教师姓名
										vecxx.add(singleroom[m]);//实际场地编号
										vecxx.add(singleroom[m]);//实际场地名称
										vecxx.add(singleweek[k]);//授课周次
									}
								}else{
									vecxx.add(vec6.get(i).toString());//行政班名称
									vecxx.add(sjxls[j]);//时间序列
									vecxx.add(vec6.get(i+2).toString());//课程名称
									vecxx.add(vec6.get(i+3).toString());//授课教师姓名
									vecxx.add(vec6.get(i+4).toString());//实际场地编号
									vecxx.add(vec6.get(i+5).toString());//实际场地名称
									vecxx.add(weektime);//授课周次
								}
							}			
						}
					}else{//只有一周
						if(vec6.get(i+6).toString().equals(weektime+"")){//weektime等于授课周次
							//System.out.println(vec6.get(i+6).toString()+"|"+vec6.get(i+4).toString().indexOf("+")+"|"+weektime);
							if(vec6.get(i+4).toString().indexOf("+")>-1){//多个教室
								String[] singleroom=vec6.get(i+4).toString().split("\\+");
								for(int m=0;m<singleroom.length;m++){								
									vecxx.add(vec6.get(i).toString());//行政班名称
									vecxx.add(sjxls[j]);//时间序列
									vecxx.add(vec6.get(i+2).toString());//课程名称
									vecxx.add(vec6.get(i+3).toString());//授课教师姓名
									vecxx.add(singleroom[m]);//实际场地编号
									vecxx.add(singleroom[m]);//实际场地名称
									vecxx.add(vec6.get(i+6).toString());//授课周次
								}
							}else{
								vecxx.add(vec6.get(i).toString());//行政班名称
								vecxx.add(sjxls[j]);//时间序列
								vecxx.add(vec6.get(i+2).toString());//课程名称
								vecxx.add(vec6.get(i+3).toString());//授课教师姓名
								vecxx.add(vec6.get(i+4).toString());//实际场地编号
								vecxx.add(vec6.get(i+5).toString());//实际场地名称
								vecxx.add(weektime);//授课周次
							}
						}
					}
				}
			}

		}
				
		for(int i=0;i<vecxx.size();i=i+7){
			if(vecxx.get(i+1).toString().substring(0, 2).equals(week)&&cdbh.indexOf(vecxx.get(i+4).toString())>-1){
				veckc.add(vecxx.get(i).toString());//行政班名称
				veckc.add(vecxx.get(i+1).toString());//时间序列
				veckc.add(vecxx.get(i+2).toString());//课程名称
				veckc.add(vecxx.get(i+3).toString());//授课教师姓名
				veckc.add(vecxx.get(i+4).toString());//实际场地编号
				veckc.add(vecxx.get(i+5).toString());//实际场地名称
				veckc.add(vecxx.get(i+6).toString());//授课周次
			}
		}
		
		String revec="";
		int total=0;

		for(int m=0;m<veckc.size();m=m+7){
			revec+="{\"行政班名称\":\""+veckc.get(m).toString()+"\",\"时间序列\":\""+veckc.get(m+1).toString()+"\",\"课程名称\":\""+veckc.get(m+2).toString()+"\",\"授课教师姓名\":\""+veckc.get(m+3).toString()+"\",\"实际场地编号\":\""+veckc.get(m+4).toString()+"\",\"实际场地名称\":\""+veckc.get(m+5).toString()+"\",\"授课周次\":\""+veckc.get(m+6).toString()+"\",\"MSG\":\""+this.getMSG()+"\",\"MSG2\":\""+this.getMSG2()+"\"},";
		}
		if(!revec.equals("")){
			revec="["+revec.substring(0, revec.length()-1)+"]";
		}
		this.setTATOL(veckc.size()/7+"");
		//System.out.println("revec:--"+revec);
		return revec;
	}
	
	//合并周次
	public String merge(String skzc) throws SQLException {
		String skzcxq="";
		int tag=0;
		if(skzc.indexOf("#")>-1){
			String[] skzc2=skzc.split("#");
			for(int i=0;i<skzc2.length-1;i++){
				if(Integer.parseInt(skzc2[i])+1==Integer.parseInt(skzc2[i+1])){
					
				}else{
					tag=1;
				}
			}
			if(tag==1){
				skzcxq=skzc;
			}else{
				skzcxq=skzc2[0]+"-"+skzc2[skzc2.length-1];
			}
		}else{//单个周
			skzcxq=skzc;
		}
		return skzcxq;
	}
	
	/**
	 * @author 2016-03-25
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 * 生成数据对接
	 */
	@SuppressWarnings("unchecked")
	public void createSJDJ() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		Vector veczb = null;
		Vector vec3 = null;
		Vector vec4 = null;
		Vector vec7 = null;
		String sql = "";
		String sqlzb = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql7 = "";
		String sqlall = "";
		String classroom="";
		String timedata="";
		
		//拆分课程表明细详情表
		String skjhbh=null;//授课计划明细编号
		String xnxqbm=null;//学年学期编码
		String skjsbh=null;//授课教师编号
		String skjsxm=null;//授课教师姓名
		String zyname=null;//专业名称
		String kechid=null;//课程代码
		String kechna=null;//课程名称
		String clasid=null;//行政班代码
		String clasna=null;//行政班名称
		String timexl=null;//时间序列
		String sjcdbh=null;//实际场地编号
		String sjcdmc=null;//实际场地名称
		String skzcxq=null;//授课周次详情
		String zt=null;//状态
		int d=0;
		String sqlpkgl="";
		String sqlpkgl2="";
		Vector vectk = null;
		Vector vecbk = null;
		Vector vecsk = null;
		String sqltk="";//调课
		String sqlbk="";//补课
		String sqlsk="";//停课
		
		//查询调整后的调课信息表
		sql7=" SELECT [编号],[授课计划明细编号],[原计划授课周次],[原计划星期],[原计划时间序列],[原计划授课教师编号],[原计划授课教师名称],[原计划场地编号],[原计划场地名称], " +
			 " [调整后授课周次],[调整后星期],[调整后时间序列],[调整后授课教师编号],[调整后授课教师名称],[调整后场地编号],[调整后场地名称] " +
			 " FROM V_调课管理_调课信息主表 where [审核状态]='1' and 原计划授课周次<>'' and 调整后授课周次<>'' order by 编号 ";
		vec7=db.GetContextVector(sql7);
			
		if(vec7!=null&&vec7.size()>0){
			for(int u=0;u<vec7.size();u=u+16){
				String weekyj=vec7.get(u+2).toString().replaceAll(",", "\\#");//原计划授课周次
				String timeyf=vec7.get(u+3).toString();//原计划星期
				String timeyl=vec7.get(u+4).toString();//原计划时间序列
				String weektj=vec7.get(u+9).toString().replaceAll(",", "\\#");//调整后授课周次
				String timetf=vec7.get(u+10).toString();//调整后星期
				String timetl=vec7.get(u+11).toString();//调整后时间序列
				if(weekyj.equals("")||weektj.equals("")){
					
				}else{
					String[] timeyls=timeyl.split(",");
					String[] timetls=timetl.split(",");
					for(int v=0;v<timetls.length;v++){
						sqltk+=" select '"+vec7.get(u).toString()+"' as 编号,'"+vec7.get(u+1).toString()+"' as 授课计划明细编号,'"+weekyj+"' as 原计划授课周次,'"+(timeyf+timeyls[v])+"' as 原计划时间序列,'"+vec7.get(u+5).toString()+"' as 原计划授课教师编号,'"+vec7.get(u+6).toString()+"' as 原计划授课教师名称,'"+vec7.get(u+7).toString()+"' as 原计划场地编号,'"+vec7.get(u+8).toString()+"' as 原计划场地名称," +
							   " '"+weektj+"' as 调整后授课周次,'"+(timetf+timetls[v])+"' as 调整后时间序列,'"+vec7.get(u+12).toString()+"' as 调整后授课教师编号,'"+vec7.get(u+13).toString()+"' as 调整后授课教师名称,'"+vec7.get(u+14).toString()+"' as 调整后场地编号,'"+vec7.get(u+15).toString()+"' as 调整后场地名称" +
							   " union " ;
					}
				}
			}
			sqltk=sqltk.substring(0, sqltk.length()-"union ".length());
			vectk=db.GetContextVector(sqltk);
			//System.out.println("sqltk:--"+sqltk);
		
			sql=" SELECT a.授课计划明细编号,a.学年学期编码,a.授课教师编号,a.授课教师姓名,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.时间序列,a.实际场地编号,a.实际场地名称,a.授课周次详情,a.状态 " +
				" FROM V_排课管理_课程表明细详情表 a " +
				" where a.授课计划明细编号 != '' and a.状态='1' " ;
			vec=db.GetContextVector(sql);
			if(vec!=null&&vec.size()>0){
				for(int i=0;i<vec.size();i=i+13){
					skjhbh=vec.get(i).toString();//授课计划明细编号
					xnxqbm=vec.get(i+1).toString();//学年学期编码
					skjsbh=vec.get(i+2).toString();//授课教师编号
					skjsxm=vec.get(i+3).toString();//授课教师姓名
					kechid=vec.get(i+4).toString();//课程代码
					kechna=vec.get(i+5).toString();//课程名称
					clasid=vec.get(i+6).toString();//行政班代码
					clasna=vec.get(i+7).toString();//行政班名称
					timexl=vec.get(i+8).toString();//时间序列
					sjcdbh=vec.get(i+9).toString();//实际场地编号
					sjcdmc=vec.get(i+10).toString();//实际场地名称
					skzcxq=vec.get(i+11).toString();//授课周次详情
					zt=vec.get(i+12).toString();//状态
					
					//System.out.println(a+":"+skjhbh+","+xnxqbm+","+skjsbh+","+skjsxm+","+zyname+","+kechid+","+kechna+","+clasid+","+clasna+","+timexl+","+sjcdmc+","+skzcxq); a++;
					String[] skjhbh2=skjhbh.split("｜");
					String[] skjsbh2=skjsbh.split("｜");
					String[] skjsxm2=skjsxm.split("｜");
					String[] kechid2=kechid.split("｜");
					String[] kechna2=kechna.split("｜");
					String[] sjcdbh2=sjcdbh.split("｜");
					String[] sjcdmc2=sjcdmc.split("｜");
					String[] skzcxq2=skzcxq.split("｜");
					for(int j=0;j<skjhbh2.length;j++){
						//System.out.println(b+":"+skjhbh2[j]+","+skjsbh2[j]+","+skjsxm2[j]+","+kechid2[j]+","+kechna2[j]+","+sjcdmc2[j]+","+skzcxq2[j]); b++;
						String[] skjsbh3=skjsbh2[j].split("\\&");
						String[] skjsxm3=skjsxm2[j].split("\\&");
						String[] sjcdbh3=sjcdbh2[j].split("\\&");
						String[] sjcdmc3=sjcdmc2[j].split("\\&");
						String[] skzcxq3=skzcxq2[j].split("\\&");
						for(int k=0;k<skjsbh3.length;k++){
							//System.out.println(c+":"+skjsbh3[k]+","+skjsxm3[k]+","+sjcdmc3[k]+","+skzcxq3[k]); c++;
							String skzcxqcf="";
							int tag=0;
							for(int w=0;w<vectk.size();w=w+14){
								if(vectk.get(w+1).toString().equals(skjhbh2[j])&&vectk.get(w+3).toString().equals(timexl)){//授课计划和时间序列都相等,替换
									//System.out.println("name:--"+vectk.get(w+11).toString());
									skzcxqcf=vectk.get(w+8).toString();
									if(skzcxqcf.indexOf("-")>-1){
										String[] skcf=skzcxqcf.split("-");
										for(int cf=Integer.parseInt(skcf[0]);cf<=Integer.parseInt(skcf[1]);cf++){
											skzcxqcf="";
											skzcxqcf+=cf+"#";
										}
										skzcxqcf=skzcxqcf.substring(0,skzcxqcf.length()-1);
									}
									
									if(d==0){
										sqlpkgl+=" select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+vectk.get(w+9).toString()+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+vectk.get(w+10).toString()+"' as 授课教师编号,'"+vectk.get(w+11).toString()+"' as 授课教师姓名,'"+vectk.get(w+12).toString()+"' as 实际场地编号,'"+vectk.get(w+13).toString()+"' as 实际场地名称,'"+skzcxqcf+"' as 授课周次详情,'"+zt+"' as 状态 ";
									}else{
										sqlpkgl+=" union all select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+vectk.get(w+9).toString()+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+vectk.get(w+10).toString()+"' as 授课教师编号,'"+vectk.get(w+11).toString()+"' as 授课教师姓名,'"+vectk.get(w+12).toString()+"' as 实际场地编号,'"+vectk.get(w+13).toString()+"' as 实际场地名称,'"+skzcxqcf+"' as 授课周次详情,'"+zt+"' as 状态 ";
									}
									tag=1;
								}
							}
							if(tag==0){
								skzcxqcf=skzcxq3[k];
								if(skzcxqcf.indexOf("-")>-1){
									String[] skcf=skzcxqcf.split("-");
									skzcxqcf="";
									for(int cf=Integer.parseInt(skcf[0]);cf<=Integer.parseInt(skcf[1]);cf++){
										skzcxqcf+=cf+"#";
									}
									skzcxqcf=skzcxqcf.substring(0,skzcxqcf.length()-1);
								}
								
								if(d==0){
									sqlpkgl+=" select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+timexl+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+skjsbh3[k]+"' as 授课教师编号,'"+skjsxm3[k]+"' as 授课教师姓名,'"+sjcdbh3[k]+"' as 实际场地编号,'"+sjcdmc3[k]+"' as 实际场地名称,'"+skzcxqcf+"' as 授课周次详情,'"+zt+"' as 状态 ";
								}else{
									sqlpkgl+=" union all select '"+xnxqbm+"' as 学年学期编码,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+timexl+"' as 时间序列,'"+skjhbh2[j]+"' as 授课计划明细编号,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+skjsbh3[k]+"' as 授课教师编号,'"+skjsxm3[k]+"' as 授课教师姓名,'"+sjcdbh3[k]+"' as 实际场地编号,'"+sjcdmc3[k]+"' as 实际场地名称,'"+skzcxqcf+"' as 授课周次详情,'"+zt+"' as 状态 ";
								}
							}
							d++;			
						}
					}
				}	
			}
		}
		
		//String sqlins=" delete from V_教室查询_课程表明细详情表 ";
		//sqlins+=" insert into V_教室查询_课程表明细详情表  " + sqlpkgl;
		String sqlins2=" delete from V_数据对接_课程表明细详情表 ";
		sqlins2+=" insert into V_数据对接_课程表明细详情表  " + sqlpkgl;
		
		//db.executeInsertOrUpdate(sqlins);
		db.executeInsertOrUpdate(sqlins2);

		//将周次1-18拆分成1#2#3...#18
		sqlzb=" SELECT d.课程代码 AS XJKCH, d.课程名称 AS XJKCMC, SUBSTRING(d.学年学期编码, 1, 4) AS KKXND, SUBSTRING(d.学年学期编码, 5, 1) AS KKXQM, d.时间序列 AS SKSJ, d.实际场地编号 AS JXDDBH, d.实际场地名称 AS JXDD, " +
				" SUBSTRING(d.时间序列, 1, 2) AS XJSKZC, SUBSTRING(d.时间序列, 3, 2) AS XJSKJC, SUBSTRING(d.授课周次详情, e.number, CHARINDEX('#', d.授课周次详情 + '#', e.number) - e.number) AS QSZ, SUBSTRING(d.授课周次详情, e.number, CHARINDEX('#', d.授课周次详情 + '#', e.number) - e.number) AS ZZZ, " +
				" d.授课教师编号 AS JSGH, d.授课教师姓名 AS JSXM, d.行政班编号 AS SKBJH, d.行政班名称 AS JXBMC, d.授课计划明细编号 AS SKJHMXBH, CASE WHEN d.状态 = '1' THEN '0' WHEN d.状态 = '0' THEN '1' END AS XJSFSX " +
				" FROM (SELECT 学年学期编码, 行政班编号, 行政班名称, 时间序列, 授课计划明细编号, 课程代码, 课程名称, 授课教师编号, 授课教师姓名, 实际场地编号, 实际场地名称, 授课周次详情, 状态     " +
				" FROM dbo.V_数据对接_课程表明细详情表 " +
				" UNION " +
				" SELECT b.学年学期编码, '' AS 选修班代码, c.选修班名称, a.时间序列, a.授课计划明细编号, b.课程代码, b.课程名称, a.授课教师编号, a.授课教师名称, a.实际场地编号, a.实际场地名称, " +
				" CASE WHEN c.授课周次 = '1-9' THEN '1#2#3#4#5#6#7#8#9' WHEN c.授课周次 = '10-18' THEN '10#11#12#13#14#15#16#17#18' ELSE '' END AS 授课周次详情, a.状态 " +
				" FROM dbo.V_排课管理_选修课课程表信息表 AS a INNER JOIN dbo.V_规则管理_选修课授课计划明细表 AS c ON a.授课计划明细编号 = c.授课计划明细编号 INNER JOIN dbo.V_规则管理_选修课授课计划主表 AS b ON c.授课计划主表编号 = b.授课计划主表编号) AS d INNER JOIN " +
				" master.dbo.spt_values AS e ON e.type = 'P' " +
				" WHERE (SUBSTRING('#' + d.授课周次详情, e.number, 1) = '#') ";
		
		//获取所有停课授课计划编号
		String sqlskjh="";
		sql3=" SELECT distinct a.授课计划明细编号 from dbo.V_调课管理_调课信息主表 a,dbo.V_规则管理_授课计划明细表 b,dbo.V_规则管理_授课计划主表 c,dbo.V_基础信息_班级信息表 d " +
			 " where a.授课计划明细编号=b.授课计划明细编号 and b.授课计划主表编号=c.授课计划主表编号 and c.行政班代码=d.班级编号 and a.调整后授课周次='' ";
		vec3=db.GetContextVector(sql3);
		if(vec3!=null&&vec3.size()>0){
			for(int m=0;m<vec3.size();m++){
				sqlskjh+="'"+vec3.get(m).toString()+"',";
			}
			sqlskjh=sqlskjh.substring(0,sqlskjh.length()-1);
		}
		
		String sqlsk1="";
		String sqlsk2="";
		String sqlsk3="";
		Vector vecsk1=null;
		int tag2=0;
		//查询所有停课信息
		sqlsk=" SELECT b.课程代码 AS XJKCH, b.课程名称 AS XJKCMC, SUBSTRING(a.学年学期编码, 1, 4) AS KKXND, SUBSTRING(a.学年学期编码, 5, 1) AS KKXQM, a.原计划星期+a.原计划时间序列 AS SKSJ, " +
				" a.原计划场地编号 AS JXDDBH, a.原计划场地名称 AS JXDD, a.原计划星期 AS XJSKZC, a.原计划时间序列 AS XJSKJC, a.原计划授课周次 AS QSZ, a.原计划授课周次 AS ZZZ, a.原计划授课教师编号 AS JSGH, a.原计划授课教师名称 AS JSXM, " +
				" c.行政班代码 AS SKBJH, d.班级名称 AS JXBMC, a.授课计划明细编号 AS SKJHMXBH, '1' AS XJSFSX " +
				" from dbo.V_调课管理_调课信息主表 a,dbo.V_规则管理_授课计划明细表 b,dbo.V_规则管理_授课计划主表 c,dbo.V_基础信息_班级信息表 d " +
				" where a.授课计划明细编号=b.授课计划明细编号 and b.授课计划主表编号=c.授课计划主表编号 and c.行政班代码=d.班级编号 and a.调整后授课周次='' ";
		vecsk=db.GetContextVector(sqlsk);	
		if(vecsk!=null&&vecsk.size()>0){
			for(int s=0;s<vecsk.size();s=s+17){
				String xjskjc=vecsk.get(s+8).toString();//原计划时间序列
				String qsz=vecsk.get(s+9).toString();//原计划授课周次
				String[] xjskjc2=xjskjc.split(",");
				String[] qsz2=qsz.split(",");
				for(int u=0;u<xjskjc2.length;u++){
					for(int v=0;v<qsz2.length;v++){
						sqlsk1+="select '"+vecsk.get(s).toString()+"' as XJKCH,'"+vecsk.get(s+1).toString()+"' as XJKCMC,'"+vecsk.get(s+2).toString()+"' as KKXND,'"+vecsk.get(s+3).toString()+"' as KKXQM,'"+vecsk.get(s+7).toString()+xjskjc2[u]+"' as SKSJ,'"+vecsk.get(s+5).toString()+"' as JXDDBH,'"+vecsk.get(s+6).toString()+"' as JXDD,'"+vecsk.get(s+7).toString()+"' as XJSKZC,'"+xjskjc2[u]+"' as XJSKJC,'"+qsz2[v]+"' as QSZ,'"+qsz2[v]+"' as ZZZ,'"+vecsk.get(s+11).toString()+"' as JSGH,'"+vecsk.get(s+12).toString()+"' as JSXM,'"+vecsk.get(s+13).toString()+"' as SKBJH,'"+vecsk.get(s+14).toString()+"' as JXBMC,'"+vecsk.get(s+15).toString()+"' as SKJHMXBH, '"+vecsk.get(s+16).toString()+"' AS XJSFSX union ";
					}
				}	
			}
			sqlsk1=sqlsk1.substring(0, sqlsk1.length()-6);
		}
		vecsk1=db.GetContextVector(sqlsk1);
		if(vecsk1!=null&&vecsk1.size()>0){
			//列出排课表中，要停课的信息
			sql4="select * from ("+sqlzb+") h where h.SKJHMXBH in ("+sqlskjh+")";
			vec4=db.GetContextVector(sql4);
			if(vec4!=null&&vec4.size()>0){
				for(int r=0;r<vec4.size();r=r+17){
					for(int t=0;t<vecsk1.size();t=t+17){
						if(vec4.get(r+15).toString().equals(vecsk1.get(t+15).toString())&&vec4.get(r+7).toString().equals(vecsk1.get(t+7).toString())&&vec4.get(r+8).toString().equals(vecsk1.get(t+8).toString())&&vec4.get(r+9).toString().equals(vecsk1.get(t+9).toString())){//授课计划编号，时间序列，授课周次都相同，删除
							tag2=1;
						}
					}
					if(tag2==0){
						sqlsk2+="select '"+vec4.get(r).toString()+"' as XJKCH,'"+vec4.get(r+1).toString()+"' as XJKCMC,'"+vec4.get(r+2).toString()+"' as KKXND,'"+vec4.get(r+3).toString()+"' as KKXQM,'"+vec4.get(r+4).toString()+"' as SKSJ,'"+vec4.get(r+5).toString()+"' as JXDDBH,'"+vec4.get(r+6).toString()+"' as JXDD,'"+vec4.get(r+7).toString()+"' as XJSKZC,'"+vec4.get(r+8).toString()+"' as XJSKJC,'"+vec4.get(r+9).toString()+"' as QSZ,'"+vec4.get(r+10).toString()+"' as ZZZ,'"+vec4.get(r+11).toString()+"' as JSGH,'"+vec4.get(r+12).toString()+"' as JSXM,'"+vec4.get(r+13).toString()+"' as SKBJH,'"+vec4.get(r+14).toString()+"' as JXBMC,'"+vec4.get(r+15).toString()+"' as SKJHMXBH, '"+vec4.get(r+16).toString()+"' AS XJSFSX union ";	
					}
					tag2=0;
				}
				sqlsk2=sqlsk2.substring(0, sqlsk2.length()-6);
			}
		}
		
		//列出排课表中，除去停课的信息
		sql5="select * from ("+sqlzb+") h where h.SKJHMXBH not in ("+sqlskjh+")";
		
		//查询所有补课信息
		sqlbk="SELECT b.课程代码 AS XJKCH, b.课程名称 AS XJKCMC, SUBSTRING(a.学年学期编码, 1, 4) AS KKXND, SUBSTRING(a.学年学期编码, 5, 1) AS KKXQM, a.调整后星期+a.调整后时间序列 AS SKSJ, " +
				" a.调整后场地编号 AS JXDDBH, a.调整后场地名称 AS JXDD, a.调整后星期 AS XJSKZC, a.调整后时间序列 AS XJSKJC, a.调整后授课周次 AS QSZ, a.调整后授课周次 AS ZZZ, a.调整后授课教师编号 AS JSGH, a.调整后授课教师名称 AS JSXM," +
				" c.行政班代码 AS SKBJH, d.班级名称 AS JXBMC, a.授课计划明细编号 AS SKJHMXBH, '0' AS XJSFSX " +
				" from dbo.V_调课管理_调课信息主表 a,dbo.V_规则管理_授课计划明细表 b,dbo.V_规则管理_授课计划主表 c,dbo.V_基础信息_班级信息表 d " +
				" where a.授课计划明细编号=b.授课计划明细编号 and b.授课计划主表编号=c.授课计划主表编号 and c.行政班代码=d.班级编号 and a.原计划授课周次='' ";
		vecbk=db.GetContextVector(sqlbk);	
		if(vecbk!=null&&vecbk.size()>0){
			for(int s=0;s<vecbk.size();s=s+17){
				String xjskjc=vecbk.get(s+8).toString();//原计划时间序列
				String qsz=vecbk.get(s+9).toString();//原计划授课周次
				String[] xjskjc2=xjskjc.split(",");
				String[] qsz2=qsz.split(",");
				for(int u=0;u<xjskjc2.length;u++){
					for(int v=0;v<qsz2.length;v++){
						sqlsk3+="select '"+vecbk.get(s).toString()+"' as XJKCH,'"+vecbk.get(s+1).toString()+"' as XJKCMC,'"+vecbk.get(s+2).toString()+"' as KKXND,'"+vecbk.get(s+3).toString()+"' as KKXQM,'"+vecbk.get(s+7).toString()+xjskjc2[u]+"' as SKSJ,'"+vecbk.get(s+5).toString()+"' as JXDDBH,'"+vecbk.get(s+6).toString()+"' as JXDD,'"+vecbk.get(s+7).toString()+"' as XJSKZC,'"+xjskjc2[u]+"' as XJSKJC,'"+qsz2[v]+"' as QSZ,'"+qsz2[v]+"' as ZZZ,'"+vecbk.get(s+11).toString()+"' as JSGH,'"+vecbk.get(s+12).toString()+"' as JSXM,'"+vecbk.get(s+13).toString()+"' as SKBJH,'"+vecbk.get(s+14).toString()+"' as JXBMC,'"+vecbk.get(s+15).toString()+"' as SKJHMXBH, '"+vecbk.get(s+16).toString()+"' AS XJSFSX union ";
					}
				}
			}
			sqlsk3=sqlsk3.substring(0, sqlsk3.length()-6);
		}
		
		//System.out.println(sql5);
		//System.out.println(sqlsk2);
		//System.out.println(sqlsk3);
		sqlall="delete from SJDJ_ALL ";
		if(sqlsk2.equals("")){
			sqlall+=" insert into SJDJ_ALL "+sql5+" union "+sqlsk3;
		}else{
			sqlall+=" insert into SJDJ_ALL "+sql5+" union "+sqlsk2+" union "+sqlsk3;
		}
		db.executeInsertOrUpdate(sqlall);
		
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2016-05-25
	 * @author:lupengfei
	 * @description:判断公共课是否确认，如已确认不允许修改公共课授课计划
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void checkGGK() throws SQLException{
		Vector vec = null;
		Vector vec3 = null;
	
		String sql = " select distinct a.学年学期编码 as PK_XNXQBM,b.学年学期名称 as PK_XNXQMC,c.教学性质 as jxxz,case when a.提交状态='0' then a.提交状态 else '1' end as PK_TJZT from V_排课管理_课程表主表 a left join V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 left join V_基础信息_教学性质 c on c.编号=b.教学性质 left join V_基础信息_班级信息表 d on d.班级编号=a.行政班代码 left join V_专业基本信息数据子类 e on e.专业代码=d.专业代码 where a.状态='1' " +
				" and a.学年学期编码='"+MyTools.fixSql(this.getGS_XNXQBM())+"' order by a.学年学期编码 desc" ;
		vec = db.GetContextVector(sql);
		
		if(vec!=null&&vec.size()>0){
			this.setMSG(vec.get(3).toString());			
		}else{
			this.setMSG("0");			
		}
	}
	
	//学年学期combobox
	public Vector xnxqIMCombobox() throws SQLException{
		Vector vec = null;
		String sql = "select distinct [所属年份]  AS comboValue,[年级名称] AS comboName FROM V_学校年级数据子类 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
					
		return vec;
	}
			
	//专业名称combobox
	public Vector zymcIMCombobox() throws SQLException{
		Vector vec = null;
		String sql = " select ''  AS comboValue,'请选择' AS comboName,'1' as px " +
				"union select distinct 专业代码  AS comboValue,专业名称+'('+专业代码+')' AS comboName,'2' as px FROM dbo.V_专业基本信息数据子类  where 1=1 order by px,comboValue ";
		vec = db.getConttexJONSArr(sql, 0, 0);

		return vec;
	}
	
	
	
	
	//GET && SET 方法
	public String getGS_SKJHMXBH() {
		return GS_SKJHMXBH;
	}

	public void setGS_SKJHMXBH(String gS_SKJHMXBH) {
		GS_SKJHMXBH = gS_SKJHMXBH;
	}

	public String getGS_SKJHZBBH() {
		return GS_SKJHZBBH;
	}

	public void setGS_SKJHZBBH(String gS_SKJHZBBH) {
		GS_SKJHZBBH = gS_SKJHZBBH;
	}

	public String getGS_KCDM() {
		return GS_KCDM;
	}

	public void setGS_KCDM(String gS_KCDM) {
		GS_KCDM = gS_KCDM;
	}

	public String getGS_KCMC() {
		return GS_KCMC;
	}

	public void setGS_KCMC(String gS_KCMC) {
		GS_KCMC = gS_KCMC;
	}

	public String getGS_SKJSBH() {
		return GS_SKJSBH;
	}

	public void setGS_SKJSBH(String gS_SKJSBH) {
		GS_SKJSBH = gS_SKJSBH;
	}

	public String getGS_SKJSXM() {
		return GS_SKJSXM;
	}

	public void setGS_SKJSXM(String gS_SKJSXM) {
		GS_SKJSXM = gS_SKJSXM;
	}

	public String getGS_JS() {
		return GS_JS;
	}

	public void setGS_JS(String gS_JS) {
		GS_JS = gS_JS;
	}

	public String getGS_GPYPJS() {
		return GS_GPYPJS;
	}

	public void setGS_GPYPJS(String gS_GPYPJS) {
		GS_GPYPJS = gS_GPYPJS;
	}

	public String getGS_SJYPJS() {
		return GS_SJYPJS;
	}

	public void setGS_SJYPJS(String gS_SJYPJS) {
		GS_SJYPJS = gS_SJYPJS;
	}

	public String getGS_LJ() {
		return GS_LJ;
	}

	public void setGS_LJ(String gS_LJ) {
		GS_LJ = gS_LJ;
	}

	public String getGS_LC() {
		return GS_LC;
	}

	public void setGS_LC(String gS_LC) {
		GS_LC = gS_LC;
	}

	public String getGS_GPLCCS() {
		return GS_GPLCCS;
	}

	public void setGS_GPLCCS(String gS_GPLCCS) {
		GS_GPLCCS = gS_GPLCCS;
	}

	public String getGS_SJLCCS() {
		return GS_SJLCCS;
	}

	public void setGS_SJLCCS(String gS_SJLCCS) {
		GS_SJLCCS = gS_SJLCCS;
	}

	public String getGS_CDYQ() {
		return GS_CDYQ;
	}

	public void setGS_CDYQ(String gS_CDYQ) {
		GS_CDYQ = gS_CDYQ;
	}

	public String getGS_CDMC() {
		return GS_CDMC;
	}

	public void setGS_CDMC(String gS_CDMC) {
		GS_CDMC = gS_CDMC;
	}

	public String getGS_SKZC() {
		return GS_SKZC;
	}

	public void setGS_SKZC(String gS_SKZC) {
		GS_SKZC = gS_SKZC;
	}

	public String getGS_ZT() {
		return GS_ZT;
	}

	public void setGS_ZT(String gS_ZT) {
		GS_ZT = gS_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getXZBDM() {
		return XZBDM;
	}

	public void setXZBDM(String xZBDM) {
		XZBDM = xZBDM;
	}

	public String getXNXQ() {
		return XNXQ;
	}

	public void setXNXQ(String xNXQ) {
		XNXQ = xNXQ;
	}

	public String getGS_XNXQBM() {
		return GS_XNXQBM;
	}

	public void setGS_XNXQBM(String gS_XNXQBM) {
		GS_XNXQBM = gS_XNXQBM;
	}

	public String getGS_XZBDM() {
		return GS_XZBDM;
	}

	public void setGS_XZBDM(String gS_XZBDM) {
		GS_XZBDM = gS_XZBDM;
	}

	public String getGS_ZYDM() {
		return GS_ZYDM;
	}

	public void setGS_ZYDM(String gS_ZYDM) {
		GS_ZYDM = gS_ZYDM;
	}

	public String getGS_ZYMC() {
		return GS_ZYMC;
	}

	public void setGS_ZYMC(String gS_ZYMC) {
		GS_ZYMC = gS_ZYMC;
	}

	public String getJXXZ() {
		return JXXZ;
	}

	public void setJXXZ(String jXXZ) {
		JXXZ = jXXZ;
	}

	public String getJSKCS() {
		return JSKCS;
	}

	public void setJSKCS(String jSKCS) {
		JSKCS = jSKCS;
	}

	public String getSKJSBH() {
		return SKJSBH;
	}

	public void setSKJSBH(String sKJSBH) {
		SKJSBH = sKJSBH;
	}

	public String getSKSL() {
		return SKSL;
	}

	public void setSKSL(String sKSL) {
		SKSL = sKSL;
	}

	public String getXKDM() {
		return XKDM;
	}

	public void setXKDM(String xKDM) {
		XKDM = xKDM;
	}

	public String getGS_SKZCXQ() {
		return GS_SKZCXQ;
	}

	public void setGS_SKZCXQ(String gS_SKZCXQ) {
		GS_SKZCXQ = gS_SKZCXQ;
	}

	public String getMSG2() {
		return MSG2;
	}

	public void setMSG2(String mSG2) {
		MSG2 = mSG2;
	}

	public String getSKJSBHXM() {
		return SKJSBHXM;
	}

	public void setSKJSBHXM(String sKJSBHXM) {
		SKJSBHXM = sKJSBHXM;
	}

	public String getiUSERCODE() {
		return iUSERCODE;
	}

	public void setiUSERCODE(String iUSERCODE) {
		this.iUSERCODE = iUSERCODE;
	}

	public String getGS_KCLX() {
		return GS_KCLX;
	}

	public void setGS_KCLX(String gS_KCLX) {
		GS_KCLX = gS_KCLX;
	}

	public String getGS_KCMCDM() {
		return GS_KCMCDM;
	}

	public void setGS_KCMCDM(String gS_KCMCDM) {
		GS_KCMCDM = gS_KCMCDM;
	}

	public String getGS_KSXS() {
		return GS_KSXS;
	}

	public void setGS_KSXS(String gS_KSXS) {
		GS_KSXS = gS_KSXS;
	}

	public String getGS_XF() {
		return GS_XF;
	}

	public void setGS_XF(String gS_XF) {
		GS_XF = gS_XF;
	}

	public String getGS_ZKS() {
		return GS_ZKS;
	}

	public void setGS_ZKS(String gS_ZKS) {
		GS_ZKS = gS_ZKS;
	}

	public String getGS_XNXQ2() {
		return GS_XNXQ2;
	}

	public void setGS_XNXQ2(String gS_XNXQ2) {
		GS_XNXQ2 = gS_XNXQ2;
	}

	public String getGS_SFKS() {
		return GS_SFKS;
	}

	public void setGS_SFKS(String gS_SFKS) {
		GS_SFKS = gS_SFKS;
	}

	public String getUseType() {
		return useType;
	}

	public void setUseType(String useType) {
		this.useType = useType;
	}

	public String getTATOL() {
		return TATOL;
	}

	public void setTATOL(String tATOL) {
		TATOL = tATOL;
	}

	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getAUTHCODE() {
		return AUTHCODE;
	}

	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
	}
}
