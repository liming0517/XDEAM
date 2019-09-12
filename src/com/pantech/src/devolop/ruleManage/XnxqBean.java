package com.pantech.src.devolop.ruleManage;
/*
@date 2015.05.15
@author yeq
模块：M1.1学年学期设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.PublicTools;
import com.pantech.src.develop.manage.workremind.WorkRemind;

public class XnxqBean {
	private String USERCODE;//用户编号
	private String GX_XNXQBM; //学年学期编码
	private String GX_XN; //学年
	private String GX_XQ; //学期
	private String GX_XNXQMC; //学年学期名称
	private String GX_JXXZ; //教学性质
	private String GX_XQKSSJ; //学期开始时间
	private String GX_XQJSSJ; //学期结束时间
	private String GX_JJRQ; //节假日期
	private String GX_PKJZSJ; //排课截止时间

	private String GX_ZCBH; //周次编号
	private String GX_ZC; //周次
	private String GX_KSRQ; //开始日期
	private String GX_JSRQ; //结束日期
	/**
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加每周天数,上午节数,下午节数,晚上节数以及节次时间设置基础信息
	 */
	private String GX_MZTS; //每周天数
	private String GX_SWJS; //上午节数
	private String GX_ZWJS; //中午节数
	private String GX_XWJS; //下午节数
	private String GX_WSJS; //晚上节数
	private String GX_SJSKZS; //实际上课周数
	private String GX_XQFW; //学期范围是否改动过
	
	private String GJ_CODE; //编号
	private String GJ_XNXQBM; //学年学期编码
	private String GJ_JC; //节次
	private String GJ_KSSJ; //开始时间
	private String GJ_JSSJ; //结束时间
	
	
	//批量参数
	private String GJ_KSSJ1[]; //开始时间
	private String GJ_JSSJ1[]; //结束时间
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public XnxqBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-15
	 * @author:yeq
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加每周天数,上午节数,下午节数,晚上节数以及节次时间设置基础信息
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		GX_XNXQBM = ""; //学年学期编码
		GX_XN = ""; //学年
		GX_XQ = ""; //学期
		GX_XNXQMC = ""; //学年学期名称
		GX_JXXZ = ""; //教学性质
		GX_XQKSSJ = ""; //学期开始时间
		GX_XQJSSJ = ""; //学期结束时间
		GX_PKJZSJ = ""; //排课截止时间
		GX_ZCBH = ""; //周次编号
		GX_ZC = ""; //周次
		GX_KSRQ = ""; //开始日期
		GX_JSRQ = ""; //结束日期
		GX_JJRQ =""; //节假日期
		GX_MZTS = ""; //每周天数
		GX_SWJS = ""; //上午节数
		GX_ZWJS = ""; //上午节数
		GX_XWJS = ""; //下午节数
		GX_WSJS = ""; //晚上节数
		GJ_CODE = ""; //编号
		GJ_XNXQBM = ""; //学年学期编码
		GJ_JC = ""; //节次
		GJ_KSSJ = ""; //开始时间
		GJ_JSSJ = ""; //结束时间
		MSG = "";    //提示信息
	}
	
	/**
	 * 分页查询 学年学期列表
	 * @date:2015-05-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加查询每周天数,上午节数,下午节数,晚上节数
	 */
	public Vector queryRec(int pageNum, int page, String GX_XNXQMC_CX, String GX_JXXZ_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		sql = "select 学年 as GX_XN,学期 as GX_XQ,学年学期编码 as GX_XNXQBM,学年学期名称 as GX_XNXQMC,教学性质 as GX_JXXZ," +
			"convert(nvarchar(10),学期开始时间,21) as GX_XQKSSJ,convert(nvarchar(10),学期结束时间,21) as GX_XQJSSJ," +
			"(select count(*) from V_规则管理_学期周次表 b where b.学年学期编码=a.学年学期编码) as weekNum,节假日期 as GX_JJRQ," +
			"每周天数 as GX_MZTS,上午节数 as GX_SWJS,中午节数 as GX_ZWJS,下午节数 as GX_XWJS,晚上节数 as GX_WSJS,convert(nvarchar(10),排课截止时间,21) as GX_PKJZSJ,实际上课周数 as GX_SJSKZS " +
			"from V_规则管理_学年学期表 a where 1=1";
		
		if(!"".equalsIgnoreCase(GX_XNXQMC_CX)){
			sql += " and 学年学期名称 like '%" + MyTools.fixSql(GX_XNXQMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(GX_JXXZ_CX)){
			sql += " and 教学性质='" + MyTools.fixSql(GX_JXXZ_CX) + "'";
		}
		sql += " order by 学年学期编码 desc";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-05-28
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadTime() throws SQLException {
		DBSource db = new DBSource(request); // 数据库对象
		Vector vec = null; // 结果集
		// SQL语句 可变
		String sql = "select 节次,开始时间,结束时间 from V_规则管理_节次时间表 where 学年学期编码='"
				+ MyTools.fixSql(MyTools.StrFiltr(this.GJ_XNXQBM)) + "' order by 开始时间";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2015-05-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
//	public Vector loadXNXQCombo() throws SQLException{
//		Vector vec = null;
//		String sql = "select '请选择' as comboName,'' as comboValue " +
//				"union all " +
//				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue " +
//				"from V_规则管理_学年学期表";
//		vec = db.getConttexJONSArr(sql, 0, 0);
//		
//		return vec;
//	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-05-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJXXZCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
				"from V_基础信息_教学性质";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 保存方法
	 * @date:2015-05-15
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveRec() throws WrongSQLException, SQLException {		 
		String sql = "select count(*) from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getGX_XNXQBM()) + "'";
		//判断数据是否存在
		if(!db.getResultFromDB(sql)){
			this.AddRec();//新增数据
		}else{
			this.ModRec();//修改
		}
	}
	
	/**
	 * 添加方法
	 * @date:2015-05-15
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:保存时添加每周天数,上午节数,下午节数,晚上节数
	 */
	public void AddRec() throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector();
		String sql = "select count(*) from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) + "'";
		String sql1="";
		String sql2="";
		String sql3="";
		String sql4="";
		String sqlall="";
		
		//判断要修改的学年学期是否存在
		if(!db.getResultFromDB(sql)){
			sql = "insert into V_规则管理_学年学期表 (学年学期编码,学年,学期,学年学期名称,教学性质,学期开始时间,学期结束时间,排课截止时间,每周天数,上午节数,中午节数,下午节数,晚上节数,创建人,创建时间,状态,实际上课周数)" +
				" values (" +
				"'" + MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) + "'," +
				"'" + MyTools.fixSql(this.getGX_XN()) + "'," +                   
				"'" + MyTools.fixSql(this.getGX_XQ()) + "'," +
				"'" + MyTools.fixSql(this.getGX_XN()+"学年 " + ("1".equalsIgnoreCase(this.getGX_XQ())?"第一学期":"第二学期")) + "'," +
				"'" + MyTools.fixSql(this.getGX_JXXZ()) + "'," +
				"'" + MyTools.fixSql(this.getGX_XQKSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getGX_XQJSSJ()) + "'," +
				"'" + MyTools.fixSql(this.getGX_PKJZSJ()) + "'," +
				"'" + MyTools.fixSql(this.getGX_MZTS()) + "'," +
				"'" + MyTools.fixSql(this.getGX_SWJS()) + "'," +
				"'" + MyTools.fixSql(this.getGX_ZWJS()) + "'," +
				"'" + MyTools.fixSql(this.getGX_XWJS()) + "'," +
				"'" + MyTools.fixSql(this.getGX_WSJS()) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate()," +
				"'1'," +
				"'" + MyTools.fixSql(this.getGX_SJSKZS()) + "'" +
				")";
			
			String courseTimes=MyTools.getProp(request, "Base.CourseTime");
			System.out.println(courseTimes);
			String[] courseTime=courseTimes.split(",");
			String[] courseJC;
			String[] courseBE;
			String jcTime="";
			String bgTime="";
			String edTime="";
			int sw=0;
			int zw=0;
			int xw=0;
			int ws=0;
			int r=1;
			int u=1;
			int s=1;
			int t=1;
			
			
			for(int i=0;i<courseTime.length;i++){
				if(courseTime[i].substring(0,2).equals("sw")){
					sw++;
				}else if(courseTime[i].substring(0,2).equals("zw")){
					zw++;
				}else if(courseTime[i].substring(0,2).equals("xw")){
					xw++;
				}else if(courseTime[i].substring(0,2).equals("ws")){
					ws++;
				}
			}
			
			for(int i=0;i<courseTime.length;i++){
				courseJC=courseTime[i].split("\\=");
				jcTime=courseJC[0];
				courseBE=courseJC[1].split("\\|");
				bgTime=courseBE[0];
				edTime=courseBE[1];
				//System.out.println(jcTime+"|"+bgTime+"|"+edTime);
							
				if(Integer.parseInt(this.getGX_SWJS())>0&&jcTime.substring(0,2).equals("sw")&&r<=sw){
					sql1 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'上午"+r+"','"+bgTime+"','"+edTime+"') ";
					r++;
				}
				
				while(r>sw&&r<=Integer.parseInt(this.getGX_SWJS())){
					sql1 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'上午"+r+"','','') ";
					r++;
				}
				
				if(Integer.parseInt(this.getGX_ZWJS())>0&&jcTime.substring(0,2).equals("zw")&&u<=Integer.parseInt(this.getGX_ZWJS())){
					sql2 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'中午"+u+"','"+bgTime+"','"+edTime+"') ";
					u++;
				}
				
				while(u>zw&&u<=Integer.parseInt(this.getGX_ZWJS())){
					sql2 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'中午"+u+"','','') ";
					u++;
				}
				
				if(Integer.parseInt(this.getGX_XWJS())>0&&jcTime.substring(0,2).equals("xw")&&s<=xw){
					sql3 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'下午"+s+"','"+bgTime+"','"+edTime+"') ";
					s++;
				}
				
				while(s>xw&&s<=Integer.parseInt(this.getGX_XWJS())){
					sql3 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'下午"+s+"','','') ";
					s++;
				}
				
				if(Integer.parseInt(this.getGX_WSJS())>0&&jcTime.substring(0,2).equals("ws")&&t<=Integer.parseInt(this.getGX_WSJS())){
					sql4 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'晚上"+t+"','"+bgTime+"','"+edTime+"') ";
					t++;
				}
				
				while(t>ws&&t<=Integer.parseInt(this.getGX_WSJS())){
					sql4 +=" insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间)" +
							" values ('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'晚上"+t+"','','') ";
					t++;
				}
			}
			
			sqlall=sql+sql1+sql2+sql3+sql4;
			
			
//			if(Integer.parseInt(this.getGX_SWJS())>0){
//				if(Integer.parseInt(this.getGX_SWJS())==1){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==2){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==3){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==4){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==5){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午5','','') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==6){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午5','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午6','','') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==7){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午5','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午6','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午7','','') ";
//				}
//			}
//			if(Integer.parseInt(this.getGX_XWJS())>0){
//				if(Integer.parseInt(this.getGX_XWJS())==1){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==2){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==3){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==4){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午4','','') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==5){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午4','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午5','','') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==6){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午4','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午5','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午6','','') ";
//				}
//			}
//			if(Integer.parseInt(this.getGX_WSJS())>0){
//				if(Integer.parseInt(this.getGX_WSJS())==1){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==2){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==3){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上3','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==4){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上3','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上4','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==5){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上3','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上4','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上5','','') ";
//				}
//			}
			
			sqlVec.add(sqlall);
			this.GX_XNXQBM = this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ();
			

			sqlVec.addAll(this.getWeeklyDetail(this.getGX_XQKSSJ(), this.getGX_XQJSSJ(),"1"));
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.MSG = "保存成功";
			}else{
				this.MSG = "保存失败";
			}
		}else{
			this.MSG = "新建的学年学期已存在，请重新编辑。";
		}
	}
	
	/**
	 * 节次时间保存方法
	 * @date:2015-05-29
	 * @author:wangzh
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveTime() throws SQLException, WrongSQLException {
		String sql="";
		
		int k=0;
		int j=0;
		int z=0;
		
		StringTokenizer ks = new StringTokenizer(this.GJ_KSSJ,",");
		String[] ksArray = new String[ks.countTokens()];
        while(ks.hasMoreTokens()){
        	ksArray[k++]=ks.nextToken();
        }
        
        StringTokenizer js = new StringTokenizer(this.GJ_JSSJ,",");
		String[] jsArray = new String[js.countTokens()];
        while(js.hasMoreTokens()){
        	jsArray[j++]=js.nextToken();
        }
        
        StringTokenizer jc = new StringTokenizer(this.GJ_JC,",");
		String[] jcArray = new String[jc.countTokens()];
        while(jc.hasMoreTokens()){
        	jcArray[z++]=jc.nextToken();
        }
        
		sql= "delete from V_规则管理_节次时间表 where 学年学期编码 = '"+ MyTools.StrFiltr(MyTools.fixSql(this.GJ_XNXQBM)) +"'";
		
		for (int i = 0; i < jcArray.length; i++) {
			sql +="insert into V_规则管理_节次时间表 (学年学期编码,节次,开始时间,结束时间) " +
				"values('"+MyTools.StrFiltr(MyTools.fixSql(this.GJ_XNXQBM))+"'," +
						"'"+jcArray[i]+"'," +
						"'"+ksArray[i]+"'," +
						"'"+jsArray[i]+"') ";
		}
		if(db.executeInsertOrUpdate(sql)){
			this.MSG = "保存成功";
		}else{
			this.MSG = "保存失败";
		}
	}
	
	/**
	 * 修改方法
	 * @date:2015-05-15
	 * @author:yeq
	 * @throws WrongSQLException
	 * @throws SQLException
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:修改时添加每周天数,上午节数,下午节数,晚上节数
	 */
	public void ModRec() throws WrongSQLException, SQLException {
		Vector sqlVec = new Vector();
		String sql = "select count(*) from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) + "'";
		
		//判断编辑后的学年学期是否被更改，如被更改是否已存在
		if(this.getGX_XNXQBM().equalsIgnoreCase(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) || !db.getResultFromDB(sql)){
			sql= "delete from V_规则管理_节次时间表 where 学年学期编码 = '"+ MyTools.StrFiltr(MyTools.fixSql(this.GX_XNXQBM)) +"'";
			sqlVec.add(sql);
			
			sql = "update V_规则管理_学年学期表 set " +
				"学年学期编码='" + MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) + "'," +
				"学年='" + MyTools.fixSql(this.getGX_XN()) + "'," +
				"学期='" + MyTools.fixSql(this.getGX_XQ()) + "'," +
				"学年学期名称='" + MyTools.fixSql(this.getGX_XN()+"学年 " + ("1".equalsIgnoreCase(this.getGX_XQ())?"第一学期":"第二学期")) + "'," +
				"教学性质='" + MyTools.fixSql(this.getGX_JXXZ()) + "'," +
				"学期开始时间='" + MyTools.fixSql(this.getGX_XQKSSJ()) + "'," +
				"学期结束时间='" + MyTools.fixSql(this.getGX_XQJSSJ()) + "'," +
				"排课截止时间='" + MyTools.fixSql(this.getGX_PKJZSJ()) + "'," +
				"每周天数='" + MyTools.fixSql(this.getGX_MZTS()) + "'," +
				"上午节数='" + MyTools.fixSql(this.getGX_SWJS()) + "'," +
				"中午节数='" + MyTools.fixSql(this.getGX_ZWJS()) + "'," +
				"下午节数='" + MyTools.fixSql(this.getGX_XWJS()) + "'," +
				"晚上节数='" + MyTools.fixSql(this.getGX_WSJS()) + "'," +
				"实际上课周数='" + MyTools.fixSql(this.getGX_SJSKZS()) + "' " +
				"where 学年学期编码='" + MyTools.fixSql(this.getGX_XNXQBM()) + "' ";
			
			String courseTimes=MyTools.getProp(request, "Base.CourseTime");
			System.out.println(courseTimes);
			String[] courseTime=courseTimes.split(",");
			String[] courseJC;
			String[] courseBE;
			String jcTime="";
			String bgTime="";
			String edTime="";
			int sw=0;
			int zw=0;
			int xw=0;
			int ws=0;
			int r=1;
			int u=1;
			int s=1;
			int t=1;
			
			
			for(int i=0;i<courseTime.length;i++){
				if(courseTime[i].substring(0,2).equals("sw")){
					sw++;
				}else if(courseTime[i].substring(0,2).equals("zw")){
					zw++;
				}else if(courseTime[i].substring(0,2).equals("xw")){
					xw++;
				}else if(courseTime[i].substring(0,2).equals("ws")){
					ws++;
				}
			}
			
			for(int i=0;i<courseTime.length;i++){
				courseJC=courseTime[i].split("\\=");
				jcTime=courseJC[0];
				courseBE=courseJC[1].split("\\|");
				bgTime=courseBE[0];
				edTime=courseBE[1];
				//System.out.println(jcTime+"|"+bgTime+"|"+edTime);
							
				if(Integer.parseInt(this.getGX_SWJS())>0&&jcTime.substring(0,2).equals("sw")&&r<=Integer.parseInt(this.getGX_SWJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'上午"+r+"','"+bgTime+"','"+edTime+"') ";
					r++;
				}
				
				while(r>sw&&r<=Integer.parseInt(this.getGX_SWJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'上午"+r+"','','') ";
					r++;
				}
				
				if(Integer.parseInt(this.getGX_ZWJS())>0&&jcTime.substring(0,2).equals("zw")&&u<=Integer.parseInt(this.getGX_ZWJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'中午"+u+"','"+bgTime+"','"+edTime+"') ";
					u++;
				}
				
				while(u>zw&&u<=Integer.parseInt(this.getGX_ZWJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'中午"+u+"','','') ";
					u++;
				}
				
				if(Integer.parseInt(this.getGX_XWJS())>0&&jcTime.substring(0,2).equals("xw")&&s<=Integer.parseInt(this.getGX_XWJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'下午"+s+"','"+bgTime+"','"+edTime+"') ";
					s++;
				}
				
				while(s>xw&&s<=Integer.parseInt(this.getGX_XWJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'下午"+s+"','','') ";
					s++;
				}
				
				if(Integer.parseInt(this.getGX_WSJS())>0&&jcTime.substring(0,2).equals("ws")&&t<=Integer.parseInt(this.getGX_WSJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'晚上"+t+"','"+bgTime+"','"+edTime+"') ";
					t++;
				}
				
				while(t>ws&&t<=Integer.parseInt(this.getGX_WSJS())){
					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
							"'晚上"+t+"','','') ";
					t++;
				}
			}
			sqlVec.add(sql);
			
//			if(Integer.parseInt(this.getGX_SWJS())>0){
//				if(Integer.parseInt(this.getGX_SWJS())==1){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==2){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==3){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==4){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==5){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午5','','') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==6){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午5','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午6','','') ";
//				}
//				if(Integer.parseInt(this.getGX_SWJS())==7){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午1','8:20','9:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午2','9:20','10:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午3','10:20','11:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午4','11:20','12:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午5','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午6','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'上午7','','') ";
//				}
//			}
//			if(Integer.parseInt(this.getGX_XWJS())>0){
//				if(Integer.parseInt(this.getGX_XWJS())==1){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==2){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==3){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==4){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午4','','') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==5){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午4','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午5','','') ";
//				}
//				if(Integer.parseInt(this.getGX_XWJS())==6){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午1','14:20','15:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午2','15:20','16:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午3','16:20','17:00') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午4','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午5','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'下午6','','') ";
//				}
//			}
//			if(Integer.parseInt(this.getGX_WSJS())>0){
//				if(Integer.parseInt(this.getGX_WSJS())==1){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==2){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==3){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上3','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==4){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上3','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上4','','') ";
//				}
//				if(Integer.parseInt(this.getGX_WSJS())==5){
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上1','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上2','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上3','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上4','','') ";
//					sql +="insert into V_规则管理_节次时间表(学年学期编码,节次,开始时间,结束时间)" +
//							"values('"+ MyTools.fixSql(this.getGX_XN() + this.getGX_XQ() + this.getGX_JXXZ()) +"'," +
//									"'晚上5','','') ";
//				}
//			}
//			sqlVec.add(sql);
			
			sql = "delete from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getGX_XNXQBM()) + "'";
			sqlVec.add(sql);
			
			//sql = "delete from SYS_SEQUENCE where SEQ_TABLE='V_规则管理_学期周次表' and SEQ_LEAD like'"+MyTools.fixSql(this.getGX_XNXQBM())+"%'";
			//sqlVec.add(sql);
			
			sqlVec.addAll(this.getWeeklyDetail(this.getGX_XQKSSJ(), this.getGX_XQJSSJ(),this.getGX_XQFW()));
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.MSG = "保存成功";
			}else{
				this.MSG = "保存失败";
			}
		}else{
			this.MSG = "修改的学年学期已存在，请重新编辑。";
		}
	}
	
	/**
	 * 获得周次详情
	 * @date:2015-05-18
	 * @author:yeq
	 * @param startTime 起始日期
	 * @param endTime 结束日期
	 * @return 周次详情
	 * @throws SQLException
	 */
	public Vector getWeeklyDetail(String startTime, String endTime, String xqfw) throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		String holiday[] = MyTools.getProp(request, "Base.JJRQ").split(","); //默认节假日期
		String tempHoliday[];
		String tempMonth = "";
		String tempDay = "";
		int tempNum = 0;
		String holidayResult = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = MyTools.StringtoDate(startTime);
		Date endDate = MyTools.StringtoDate(endTime);
		Date tempDate = MyTools.StringtoDate(startTime);
		
		Vector vectorEndDate = new Vector();
		Vector vectorStartDate = new Vector();
		
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		vectorStartDate.add(startDate);
		boolean holidayFlag = true;
		
		//判断学期开始的日期是否是星期日
		if(c.get(Calendar.DAY_OF_WEEK) == 1){
			vectorEndDate.add(startDate);
		}
		
		//遍历整个学期，设置周次开始时间与结束时间
		while(tempDate.before(endDate)){
			c.add(c.DATE, 1);
			tempDate = c.getTime();
			c.setTime(tempDate);
			
			//判断如果是周一的话设置为周次开始日期，如果是周日的话设置为周次结束时间
			if(c.get(Calendar.DAY_OF_WEEK)==1){
				vectorEndDate.add(sdf.format(tempDate));
			}else if(c.get(Calendar.DAY_OF_WEEK)==2){
				vectorStartDate.add(sdf.format(tempDate));
			}
			
			//判断是否为双休日
			if(c.get(Calendar.DAY_OF_WEEK)==1 || c.get(Calendar.DAY_OF_WEEK)==7){
				holidayFlag = false;
				tempNum = c.get(Calendar.MONTH)+1;
				tempMonth = tempNum<10?"0"+tempNum:""+tempNum;
				tempNum = c.get(Calendar.DAY_OF_MONTH);
				tempDay = tempNum<10?"0"+tempNum:""+tempNum;
				holidayResult += c.get(Calendar.YEAR)+"-"+tempMonth+"-"+tempDay+",";
			}else{
				holidayFlag = true;
			}
			
			//判断是否为双休日
			if(holidayFlag){
				//判断是否默认节假日
				for(int i=0; i<holiday.length; i++){
					tempHoliday = holiday[i].split("-");
					if((c.get(Calendar.MONTH)+1)==MyTools.StringToInt(tempHoliday[0]) && c.get(Calendar.DAY_OF_MONTH)==MyTools.StringToInt(tempHoliday[1])){
						tempNum = c.get(Calendar.MONTH)+1;
						tempMonth = tempNum<10?"0"+tempNum:""+tempNum;
						tempNum = c.get(Calendar.DAY_OF_MONTH);
						tempDay = tempNum<10?"0"+tempNum:""+tempNum;
						holidayResult += c.get(Calendar.YEAR)+"-"+tempMonth+"-"+tempDay+",";
					}
				}
			}
		}
		//判断如果学期结束时间不是周日的话，设置为学期结束时间
		if(c.get(Calendar.DAY_OF_WEEK)!=1){
			vectorEndDate.add(endDate);
		}
		
		String weekNum = "";
		//将所有周次信息插入数据库
		for (int i = 0; i < vectorStartDate.size(); i++) {
			//this.setGX_ZCBH(db.getMaxID("V_规则管理_学期周次表", "周次编号", this.GX_XNXQBH, 2));
			//weekNum = this.GX_ZCBH.substring(this.GX_ZCBH.length()-2, this.GX_ZCBH.length());
			weekNum = (i+1)<10?"0"+(i+1):String.valueOf(i+1);
			this.setGX_ZCBH(this.getGX_XNXQBM() + weekNum);
			sql = "insert into V_规则管理_学期周次表 (" +
				"周次编号,学年学期编码,周次,开始日期,结束日期,创建人,创建时间,状态) " +
				"values(" +
				"'" + MyTools.fixSql(this.getGX_ZCBH()) + "'," +
				"'" + MyTools.fixSql(this.getGX_XNXQBM()) + "'," +
				"'" + MyTools.fixSql(weekNum) + "'," +
				"'" + MyTools.fixSql(MyTools.StrFiltr(vectorStartDate.get(i))) + "'," +
				"'" + MyTools.fixSql(MyTools.StrFiltr(vectorEndDate.get(i))) + "'," +
				"'" + MyTools.fixSql(this.USERCODE) + "'," +
				"getDate(),'1')";
			sqlVec.add(sql);
		}
		
		//更新节假日期
		if(holidayResult.length() > 0){
			holidayResult = holidayResult.substring(0, holidayResult.length()-1);
		}
		
		if(xqfw.equals("1")){//改动过
			//改动排课日期不重置节假日期 20170308 lupengfei
			sql = "update V_规则管理_学年学期表 set 节假日期='" + MyTools.fixSql(holidayResult) + "' where 学年学期编码='" + MyTools.fixSql(this.getGX_XNXQBM()) + "'";
			sqlVec.add(sql);
		}else{//未改动
			
		}
		
		
		return sqlVec;
	}
	
	/**
	 * 更新节假日信息
	 * @date:2015-05-19
	 * @author:yeq
	 * @throws SQLException
	 */
	public void updateHoliday() throws SQLException{
		String sql = "update V_规则管理_学年学期表 set 节假日期='" + MyTools.fixSql(this.getGX_JJRQ()) + "' " +
				"where 学年学期编码='" + MyTools.fixSql(this.getGX_XNXQBM()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.MSG = "保存成功";
		}else{
			this.MSG = "保存失败";
		}
	}
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getGX_XNXQBM() {
		return GX_XNXQBM;
	}

	public void setGX_XNXQBM(String gX_XNXQBM) {
		GX_XNXQBM = gX_XNXQBM;
	}

	public String getGX_XN() {
		return GX_XN;
	}

	public void setGX_XN(String gX_XN) {
		GX_XN = gX_XN;
	}

	public String getGX_XQ() {
		return GX_XQ;
	}

	public void setGX_XQ(String gX_XQ) {
		GX_XQ = gX_XQ;
	}

	public String getGX_XNXQMC() {
		return GX_XNXQMC;
	}

	public void setGX_XNXQMC(String gX_XNXQMC) {
		GX_XNXQMC = gX_XNXQMC;
	}

	public String getGX_JXXZ() {
		return GX_JXXZ;
	}

	public void setGX_JXXZ(String gX_JXXZ) {
		GX_JXXZ = gX_JXXZ;
	}

	public String getGX_XQKSSJ() {
		return GX_XQKSSJ;
	}

	public void setGX_XQKSSJ(String gX_XQKSSJ) {
		GX_XQKSSJ = gX_XQKSSJ;
	}

	public String getGX_XQJSSJ() {
		return GX_XQJSSJ;
	}

	public void setGX_XQJSSJ(String gX_XQJSSJ) {
		GX_XQJSSJ = gX_XQJSSJ;
	}
	
	public String getGX_PKJZSJ() {
		return GX_PKJZSJ;
	}

	public void setGX_PKJZSJ(String gX_PKJZSJ) {
		GX_PKJZSJ = gX_PKJZSJ;
	}

	public String getGX_ZCBH() {
		return GX_ZCBH;
	}

	public void setGX_ZCBH(String gX_ZCBH) {
		GX_ZCBH = gX_ZCBH;
	}

	public String getGX_ZC() {
		return GX_ZC;
	}

	public void setGX_ZC(String gX_ZC) {
		GX_ZC = gX_ZC;
	}

	public String getGX_KSRQ() {
		return GX_KSRQ;
	}

	public void setGX_KSRQ(String gX_KSRQ) {
		GX_KSRQ = gX_KSRQ;
	}

	public String getGX_JSRQ() {
		return GX_JSRQ;
	}

	public void setGX_JSRQ(String gX_JSRQ) {
		GX_JSRQ = gX_JSRQ;
	}

	public String getGX_JJRQ() {
		return GX_JJRQ;
	}

	public void setGX_JJRQ(String gX_JJRQ) {
		GX_JJRQ = gX_JJRQ;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getGX_MZTS() {
		return GX_MZTS;
	}

	public void setGX_MZTS(String gX_MZTS) {
		GX_MZTS = gX_MZTS;
	}

	public String getGX_SWJS() {
		return GX_SWJS;
	}

	public void setGX_SWJS(String gX_SWJS) {
		GX_SWJS = gX_SWJS;
	}

	public String getGX_XWJS() {
		return GX_XWJS;
	}

	public void setGX_XWJS(String gX_XWJS) {
		GX_XWJS = gX_XWJS;
	}

	public String getGX_WSJS() {
		return GX_WSJS;
	}

	public void setGX_WSJS(String gX_WSJS) {
		GX_WSJS = gX_WSJS;
	}
	
	public String getGJ_CODE() {
		return GJ_CODE;
	}

	public String getGJ_XNXQBM() {
		return GJ_XNXQBM;
	}

	public void setGJ_XNXQBM(String gJ_XNXQBM) {
		GJ_XNXQBM = gJ_XNXQBM;
	}

	public String getGJ_JC() {
		return GJ_JC;
	}

	public void setGJ_JC(String gJ_JC) {
		GJ_JC = gJ_JC;
	}

	public String getGJ_KSSJ() {
		return GJ_KSSJ;
	}

	public void setGJ_KSSJ(String gJ_KSSJ) {
		GJ_KSSJ = gJ_KSSJ;
	}

	public String getGJ_JSSJ() {
		return GJ_JSSJ;
	}

	public void setGJ_JSSJ(String gJ_JSSJ) {
		GJ_JSSJ = gJ_JSSJ;
	}

	public void setGJ_CODE(String gJ_CODE) {
		GJ_CODE = gJ_CODE;
	}

	public String[] getGJ_KSSJ1() {
		return GJ_KSSJ1;
	}

	public void setGJ_KSSJ1(String[] gJ_KSSJ1) {
		GJ_KSSJ1 = gJ_KSSJ1;
	}

	public String[] getGJ_JSSJ1() {
		return GJ_JSSJ1;
	}

	public void setGJ_JSSJ1(String[] gJ_JSSJ1) {
		GJ_JSSJ1 = gJ_JSSJ1;
	}

	public String getGX_ZWJS() {
		return GX_ZWJS;
	}

	public void setGX_ZWJS(String gX_ZWJS) {
		GX_ZWJS = gX_ZWJS;
	}

	public String getGX_SJSKZS() {
		return GX_SJSKZS;
	}

	public void setGX_SJSKZS(String gX_SJSKZS) {
		GX_SJSKZS = gX_SJSKZS;
	}

	public String getGX_XQFW() {
		return GX_XQFW;
	}

	public void setGX_XQFW(String gX_XQFW) {
		GX_XQFW = gX_XQFW;
	}
	
}
