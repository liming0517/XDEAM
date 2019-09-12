package com.pantech.devolop.courseAdjust;
/*
@date 2015.08.21
@author yeq
模块：M3调课管理
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.PublicTools;

public class TkglBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	private String TT_ID; //编号
	private String TT_SXLX; //时限类型
	private String TT_SQLX; //申请类型
	private String TT_XNXQBM; //学年学期编码
	private String TT_ZYDM; //专业代码
	private String TT_KCBH; //课程编号
	private String TT_BJBH; //班级编号
	private String TT_SKJHMXBH; //授课计划明细编号
	private String TT_BGLY; //变更理由
	private String TT_YJHSKZC; //原计划授课周次
	private String TT_YJHXQ; //原计划星期
	private String TT_YJHSJXL; //原计划时间序列
	private String TT_YJHSKJSBH; //原计划授课教师编号
	private String TT_YJHSKJSMC; //原计划授课教师名称
	private String TT_YJHCDBH; //原计划场地编号
	private String TT_YJHCDMC; //原计划场地名称
	private String TT_TZHSKZC; //调整后授课周次
	private String TT_TZHXQ; //调整后星期
	private String TT_TZHSJXL; //调整后时间序列
	private String TT_TZHSKJSBH; //调整后授课教师编号
	private String TT_TZHSKJSMC; //调整后授课教师名称
	private String TT_TZHCDBH; //调整后场地编号
	private String TT_TZHCDMC; //调整后场地名称
	private String TT_QTSM; //其他说明
	private String TT_SHZT; //审核状态
	private String TT_CJR; //创建人
	private String TT_CJSJ; //创建时间
	private String TT_ZT; //状态
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public TkglBean(HttpServletRequest request) {
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
		TT_ID = ""; //编号
		TT_SXLX = ""; //时限类型
		TT_SQLX = ""; //申请类型
		TT_XNXQBM = ""; //学年学期编码
		TT_ZYDM = ""; //专业代码
		TT_KCBH = ""; //课程编号
		TT_BJBH = ""; //班级编号
		TT_SKJHMXBH = ""; //授课计划明细编号
		TT_BGLY = ""; //变更理由
		TT_YJHSKZC = ""; //原计划授课周次
		TT_YJHXQ = ""; //原计划星期
		TT_YJHSJXL = ""; //原计划时间序列
		TT_YJHSKJSBH = ""; //原计划授课教师编号
		TT_YJHSKJSMC = ""; //原计划授课教师名称
		TT_YJHCDBH = ""; //原计划场地编号
		TT_YJHCDMC = ""; //原计划场地名称
		TT_TZHSKZC = ""; //调整后授课周次
		TT_TZHXQ = ""; //调整后星期
		TT_TZHSJXL = ""; //调整后时间序列
		TT_TZHSKJSBH = ""; //调整后授课教师编号
		TT_TZHSKJSMC = ""; //调整后授课教师名称
		TT_TZHCDBH = ""; //调整后场地编号
		TT_TZHCDMC = ""; //调整后场地名称
		TT_QTSM = ""; //其他说明
		TT_SHZT = ""; //审核状态
		TT_CJR = ""; //创建人
		TT_CJSJ = ""; //创建时间
		TT_ZT = ""; //状态
	}
	
	/**
	 * 分页查询 学年学期课程表列表
	 * @date:2015-05-27
	 * @author:yeq
	 * @param pageNum 页数
	 * @param page 每页数据条数
	 * @param TT_XNXQMC_CX 学年学期名称
	 * @param TT_JXXZ_CX 教学性质
	 * @param TT_XBMC_CX 系部名称
	 * @param TT_KCMC_CX 课程名称
	 * @param TT_BJMC_CX 班级名称
	 * @param TT_SQLX_CX 申请类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queRequestNoteList(int pageNum, int page, String TT_XNXQMC_CX, String TT_JXXZ_CX, String TT_XBMC_CX, String TT_KCMC_CX, String TT_BJMC_CX, String TT_SQLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select a.编号,a.申请类型,a.学年学期编码,b.学年学期名称,d.系部代码,a.课程编号,c.课程名称,a.班级编号,d.班级名称 as 行政班名称,a.授课计划明细编号,a.变更理由," +
			"a.原计划授课周次,a.原计划星期,a.原计划时间序列,a.原计划授课教师名称,a.原计划场地名称,a.调整后授课周次,a.调整后星期,a.调整后时间序列,a.调整后授课教师编号,a.调整后场地编号,a.其他说明,e.UserName as 申请人,a.审核状态 " +
			"from V_调课管理_调课信息主表 a " +
			"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
			"left join V_规则管理_授课计划明细表 c on c.授课计划明细编号=a.授课计划明细编号 " +
			//"left join V_学校班级_数据子类 d on d.行政班代码=a.班级编号 " +
			"left join V_基础信息_班级信息表 d on d.班级编号=a.班级编号 " +
			"left join sysUserinfo e on e.UserCode=a.创建人 " +
			"where a.状态='1' and a.时限类型='" + MyTools.fixSql(this.getTT_SXLX()) + "'";
		
		//权限判断
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (a.创建人='" + MyTools.fixSql(this.getUSERCODE()) + "'";
			//系部教务人员
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or d.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(TT_XNXQMC_CX)){
			sql += " and b.学年学期名称 like '%" + MyTools.fixSql(TT_XNXQMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(TT_JXXZ_CX)){
			sql += " and right(a.学年学期编码,2)='" + MyTools.fixSql(TT_JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(TT_XBMC_CX)){
			sql += " and d.系部代码='" + MyTools.fixSql(TT_XBMC_CX) + "'";
		}
		if(!"".equalsIgnoreCase(TT_KCMC_CX)){
			sql += " and c.课程名称 like '%" + MyTools.fixSql(TT_KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(TT_BJMC_CX)){
			sql += " and d.班级名称 like '%" + MyTools.fixSql(TT_BJMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(TT_SQLX_CX)){
			sql += " and a.申请类型='" + MyTools.fixSql(TT_SQLX_CX) + "'";
		}
		sql += " order by a.创建时间 desc";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 查询当前学年学期相关信息
	 * @date:2015-08-21
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public String loadCurSemseter() throws SQLException{
		Vector vec = null;
		String result = "";
		String sql = "select a.学年+a.学期 from V_规则管理_学年学期表 a where getDate() between a.排课截止时间 and a.学期结束时间";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			result = MyTools.StrFiltr(vec.get(0));
		}
		
		return result;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-08-21
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
	 * 读取系部下拉框
	 * @date:2017-07-20
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadDeptCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 系部名称 as comboName,系部代码 as comboValue " +
				"from V_基础信息_系部信息表 where 系部代码<>'C00' order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2015-08-21
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 专业名称 as comboName,专业代码 as comboValue " +
				"from V_专业基本信息数据子类 where 状态='1'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取当前学期专业下拉框
	 * @date:2015-08-21
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
//	public String loadCurMajorCombo() throws SQLException{
//		Vector vec = null;
//		String result = "";
//		String sql = "select distinct 专业名称,专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue " +
//				"from V_排课管理_课程表明细详情表  where 状态='1' " +
//				"and 学年学期编码='" + MyTools.fixSql(this.getTT_XNXQBM()) + "' and 专业代码 is not null order by 专业名称";
//		vec = db.GetContextVector(sql);
//		
//		result = "[{\"comboName\":\"请选择\",\"comboValue\":\"\"}";
//		
//		for(int i=0; i<vec.size(); i+=3){
//			result += ",{\"comboName\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\",\"comboValue\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"}";
//		}
//		
//		result += "]";
//		return result;
//	}
	
	/**
	 * 读取当前学期系部下拉框
	 * @date:2017-07-20
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public String loadCurDeptCombo() throws SQLException{
		Vector vec = null;
		String result = "";
		String sql = "select distinct c.系部名称 as comboName,b.系部代码 as comboValue " +
				"from V_排课管理_课程表明细详情表 a " +
				//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
				"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 where a.状态='1' and c.系部名称 is not null " +
				"and 学年学期编码='" + MyTools.fixSql(this.getTT_XNXQBM()) + "' order by comboName";
		vec = db.GetContextVector(sql);
		
		result = "[{\"comboName\":\"请选择\",\"comboValue\":\"\"}";
		
		for(int i=0; i<vec.size(); i+=2){
			result += ",{\"comboName\":\"" + MyTools.StrFiltr(vec.get(i)) + "\",\"comboValue\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"}";
		}
		
		result += "]";
		return result;
	}
	
	/**
	 * 读取课程下拉框
	 * @date:2015-08-21
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCourseCombo() throws SQLException{
		Vector vec = null;
		String sql = "";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
			"union all " +
			"select distinct a.课程名称 as comboName,a.授课计划明细编号 as comboValue,1 from V_规则管理_授课计划明细表 a " +
			"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
			"where a.状态='1' and b.状态='1'";
		if(!"".equalsIgnoreCase(this.getTT_XNXQBM())){
			sql += " and b.学年学期编码='" + MyTools.fixSql(this.getTT_XNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getTT_ZYDM())){
			sql += " and b.行政班代码='" + MyTools.fixSql(this.getTT_BJBH()) + "'";
		}
		//权限判断（如果不是系部教师，仅查询教师自己授课的课程）
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			//判断如果系部教务人员，查询当前需要设置调课的班级是否为自己负责的相关系部，如果不是，仅获取自己授课的课程信息
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				String tempSql = "select count(*) from V_基础信息_系部教师信息表 " +
						"where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "' and 系部代码='" + MyTools.fixSql(this.getTT_ZYDM()) + "'";
				if(!db.getResultFromDB(tempSql)){
					sql += " and '@'+replace(replace(a.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
				}
			}else{
				sql += " and '@'+replace(replace(a.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			}
		}
		sql += " order by orderNum,comboName";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取班级下拉框
	 * @date:2015-08-21
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadClassCombo() throws SQLException{
		Vector vec = null;
		String sql = "";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select '请选择' as comboName,'' as comboValue " +
			"union all " +
			"select distinct c.班级名称 as comboName,b.行政班代码 as comboValue from V_规则管理_授课计划明细表 a " +
			"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
			//"left join (select 行政班代码,行政班名称,系部代码 from V_学校班级_数据子类 union all select 教学班编号,教学班名称,系部代码 from V_基础信息_教学班信息表) c on c.行政班代码=b.行政班代码 " +
			"left join V_基础信息_班级信息表 c on c.班级编号=b.行政班代码 " +
			"where a.状态='1' and b.状态='1'";
		if(!"".equalsIgnoreCase(this.getTT_XNXQBM())){
			sql += " and b.学年学期编码='" + MyTools.fixSql(this.getTT_XNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getTT_ZYDM())){
			sql += "and c.系部代码='" + MyTools.fixSql(this.getTT_ZYDM()) + "'";
		}
		
		//权限判断
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and ('@'+replace(replace(a.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			//系部教务人员
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += " and cast(a.实际已排节数 as int)>0";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询当前课程的授课周次
	 * @date:2015-08-24
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSkzcInfo() throws SQLException{
		Vector vec = null;
		Vector resultVec = new Vector();
		String tempJson = "";
		String sql = "";
		
		if("".equalsIgnoreCase(this.getTT_SHZT()) || "0".equalsIgnoreCase(this.getTT_SHZT())){
			sql = "select distinct cast(授课周次 as int) from V_排课管理_课程表周详情表 " +
				"where 授课计划明细编号='" + MyTools.fixSql(this.getTT_SKJHMXBH()) + "' " +
				"order by cast(授课周次 as int)";
			vec = db.GetContextVector(sql);
		}else{
			sql = "select 原计划授课周次 from V_调课管理_调课信息主表 where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			vec = db.GetContextVector(sql);
			
			String skzcArray[] = MyTools.StrFiltr(vec.get(0)).split(",");
			vec.clear();
			
			for(int i=0; i<skzcArray.length; i++){
				vec.add(skzcArray[i]);
			}
		}
		
		tempJson = "[";
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i++){
				tempJson += "{\"comboName\":\"" + MyTools.StrFiltr(vec.get(i)) + "\",\"comboValue\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"},";
			}
			tempJson = tempJson.substring(0, tempJson.length()-1);
		}
		tempJson += "]";
		resultVec.add(tempJson);
		
		return resultVec;
	}
	
	/**
	 * 查询当前课程的选择周次星期几
	 * @date:2015-08-24
	 * @author:yeq
	 * @param selWeek 选择的周次
	 * @return String
	 * @throws SQLException
	 */
	public String loadWeekday(String selWeek) throws SQLException{
		Vector resultVec = new Vector();
		String resultJson = "";
		String sql = "";
		
		if("".equalsIgnoreCase(this.getTT_SHZT()) || "".equalsIgnoreCase(this.getTT_SHZT())){
			//如果有多个周次,对比所有周次的课程信息，必须要有完全相同的课程（包括时间序列，教师，场地），才将星期信息添加到结果
			if(selWeek.indexOf(",") > -1){
				Vector vec = null;
				String weekArray[] = selWeek.split(",");
				String curOrder = "";
				String curTea = "";
				String curSite = "";
				int num = 0;
				String weekDay = "";
				
				sql = "select distinct cast(授课周次 as int) as 授课周次,时间序列,授课教师编号,场地编号 from V_排课管理_课程表周详情表 " +
					"where 授课计划明细编号='" + MyTools.fixSql(this.getTT_SKJHMXBH()) + "' " +
					"and 授课周次 in ('" + selWeek.replaceAll(",", "','") + "') " +
					"order by 时间序列,cast(授课周次 as int)";
				vec = db.GetContextVector(sql);
				
				for(int i=0; i<vec.size(); i+=4){
					curOrder = MyTools.StrFiltr(vec.get(i+1));
					curTea = MyTools.StrFiltr(vec.get(i+2));
					curSite = MyTools.StrFiltr(vec.get(i+3));
					num = 0;
					weekDay = curOrder.substring(0, 2);
					
					for(int j=0; j<vec.size(); j+=4){
						if(curOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1)))
							&& curTea.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+2)))
							&& curSite.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+3)))){
							num++;
						}
						
						if(num == weekArray.length){
							break;
						}
					}
					
					if(num == weekArray.length){
						if(resultVec.indexOf(weekDay) < 0){
							resultVec.add(weekDay);
						}
					}
				}
			}else{
				sql = "select * from (select distinct left(时间序列,2) as 星期 from V_排课管理_课程表周详情表 " +
					"where 授课计划明细编号='" + MyTools.fixSql(this.getTT_SKJHMXBH()) + "' " +
					"and 授课周次='" + MyTools.fixSql(selWeek) + "') as t " +
					"order by cast(星期 as int)";
				resultVec = db.GetContextVector(sql);
			}
		}else{
			sql = "select 原计划星期 from V_调课管理_调课信息主表 where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			resultVec = db.GetContextVector(sql);
		}
		
		resultJson = "[";
		for(int i=0; i<resultVec.size(); i++){
			resultJson += "{\"comboName\":\"" + MyTools.StringToInt(MyTools.StrFiltr(resultVec.get(i))) + "\"," +
					"\"comboValue\":\"" + MyTools.StrFiltr(resultVec.get(i)) + "\"},";
		}
		resultJson = resultJson.substring(0, resultJson.length()-1);
		resultJson += "]";
		
		return resultJson;
	}
	
	/**
	 * 查询当前课程时间序列
	 * @date:2015-08-24
	 * @author:yeq
	 * @param selWeek 选择的周次
	 * @param weekDay 星期几
	 * @return String
	 * @throws SQLException
	 */
	public String loadOrder(String selWeek, String weekDay) throws SQLException{
		Vector resultVec = new Vector();
		String resultJson = "";
		String sql = "";
		
		if("".equalsIgnoreCase(this.getTT_SHZT()) || "0".equalsIgnoreCase(this.getTT_SHZT())){
			sql = "select * from (select distinct right(时间序列,2) as 时间序列 from V_排课管理_课程表周详情表 " +
				"where 授课计划明细编号='" + MyTools.fixSql(this.getTT_SKJHMXBH()) + "' " +
				"and 授课周次 in ('" + selWeek.replaceAll(",", "','") + "') " +
				"and left(时间序列,2)='" + MyTools.fixSql(weekDay) + "') as t " +
				"order by cast(时间序列 as int)";
			resultVec = db.GetContextVector(sql);
		}else{
			sql = "select 原计划时间序列 from V_调课管理_调课信息主表 where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			Vector tempVec = db.GetContextVector(sql);
			
			String orderArray[] = MyTools.StrFiltr(tempVec.get(0)).split(",");
			for(int i=0; i<orderArray.length; i++){
				resultVec.add(orderArray[i]);
			}
		}
		
		resultJson = "[";
		for(int i=0; i<resultVec.size(); i++){
			resultJson += "{\"comboName\":\"" + MyTools.StringToInt(MyTools.StrFiltr(resultVec.get(i))) + "\"," +
					"\"comboValue\":\"" + MyTools.StrFiltr(resultVec.get(i)) + "\"},";
		}
		resultJson = resultJson.substring(0, resultJson.length()-1);
		resultJson += "]";
		
		return resultJson;
	}
	
	/**
	 * 读取教师下拉框
	 * @date:2017-04-25
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadTeaCombo() throws SQLException{
		Vector vec = null;
		String sql = "select 姓名 as comboText,工号 as comboValue from V_教职工基本数据子类 where 是否有效='1' order by 姓名";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取班级所属系部相关教室信息
	 * @date:2017-07-20
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadDeptSiteInfo() throws SQLException{
		Vector vec = null;
		String sql = "select 教室名称+'（'+cast(实际容量 as nvarchar)+'人）' as comboText,教室编号 as comboValue from V_教室数据类  " +
				"where 校区代码='" + MyTools.fixSql(this.getTT_ZYDM()) + "' order by 实际容量,教室编号";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取教室下拉框
	 * @date:2017-04-25
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSiteCombo() throws SQLException{
		Vector vec = null;
		String sql = "select 教室名称+'（'+cast(实际容量 as nvarchar)+'人）' as comboText,教室编号 as comboValue from V_教室数据类 order by 实际容量,教室编号";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取相应的教师和场地信息
	 * @date:2016-04-28
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadTeaAndSiteCombo() throws SQLException{
		Vector vec = null;
		Vector resultVec = new Vector();
		String sql = "";
		
		//获取调整教师信息
		sql = "select d.层级编号,d.层级名称,a.工号,a.姓名 from V_教职工基本数据子类 a " +
			"inner join sysUserAuth b on b.UserCode=a.工号 " +
			"inner join V_权限层级关系表 c on c.权限编号=b.AuthCode " +
			"inner join V_层级表 d on d.层级编号=c.层级编号 " +
			"where a.状态='1' order by d.层级编号,a.姓名";
		vec = db.GetContextVector(sql);
		//获取combotree数据
		String preTeaType = "";
		String curTeaType = "";
		String result = "[";
		for(int i=0; i<vec.size(); i+=4){
			curTeaType = MyTools.StrFiltr(vec.get(i));
			if(!preTeaType.equalsIgnoreCase(curTeaType)){
				preTeaType = curTeaType;
				if(i > 0){
					result = result.substring(0, result.length()-1) + "]},";
				}
				result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\",\"children\":";
				result += "[{\"id\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"},";
			}else{
				result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"},";
			}
		}
		result = result.substring(0, result.length()-1) + "]}]";
		resultVec.add(result);
		
		//获取调整场地信息
		sql = "select a.教室类型代码,b.名称,a.教室编号,a.教室名称,a.实际容量 " +
			"from V_教室数据类 a " +
			"left join V_基础信息_教室类型 b on b.编号=a.教室类型代码 " +
			"where a.是否可用='1' order by a.教室类型代码,a.实际容量";
		vec = db.GetContextVector(sql);
		//获取combotree数据
		String preClassType = "";
		String curClassType = "";
		result = "[";
		for(int i=0; i<vec.size(); i+=5){
			curClassType = MyTools.StrFiltr(vec.get(i));
			if(!preClassType.equalsIgnoreCase(curClassType)){
				preClassType = curClassType;
				if(i > 0){
					result = result.substring(0, result.length()-1) + "]},";
				}
				result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\",\"children\":";
				result += "[{\"id\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+3))+"("+MyTools.StrFiltr(vec.get(i+4))+"人)" + "\"},";
			}else{
				result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+3))+"("+MyTools.StrFiltr(vec.get(i+4))+"人)" + "\"},";
			}
		}
		result = result.substring(0, result.length()-1) + "]}]";
		resultVec.add(result);
		
		return resultVec;
	}
	
	/**
	 * 读取相应的教师和场地信息
	 * @date:2016-04-28
	 * @author:yeq
	 * @param selWeek 选择的周次
	 * @param weekDay 星期几
	 * @param order 时间序列
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadTeaAndSiteInfo(String selWeek, String weekDay, String order) throws SQLException{
		Vector vec = null;
		Vector resultVec = new Vector();
		String sql = "";
		
		String teaCode = "";
		String teaName = "";
		String siteCode = "";
		String siteName = "";
		boolean flag = true;
		
		if("".equalsIgnoreCase(this.getTT_SHZT())){
			sql = "select * from (select distinct cast(授课周次 as int) as 授课周次,时间序列,授课教师编号,授课教师姓名,场地编号,场地名称 from V_排课管理_课程表周详情表 " +
				"where 授课计划明细编号='" + MyTools.fixSql(this.getTT_SKJHMXBH()) + "' " +
				"and 授课周次 in ('" + selWeek.replaceAll(",", "','") + "') " +
				"and left(时间序列,2)='" + MyTools.fixSql(weekDay) + "' " +
				"and right(时间序列,2) in ('" + order.replaceAll(",", "','") + "')) as t " +
				"order by cast(时间序列 as int),授课周次";
			vec = db.GetContextVector(sql);
			
			teaCode = MyTools.StrFiltr(vec.get(2));
			teaName = MyTools.StrFiltr(vec.get(3));
			siteCode = MyTools.StrFiltr(vec.get(4));
			siteName = MyTools.StrFiltr(vec.get(5));
			
			for(int i=0; i<vec.size(); i+=6){
				if(!teaCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2))) 
					|| !teaName.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+3))) 
					|| !siteCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4)))
					|| !siteName.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
					flag = false;
				}
			}
		}else{
			sql = "select 原计划授课教师编号,原计划授课教师名称,原计划场地编号,原计划场地名称 from V_调课管理_调课信息主表 where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			vec = db.GetContextVector(sql);
			
			teaCode = MyTools.StrFiltr(vec.get(0));
			teaName = MyTools.StrFiltr(vec.get(1));
			siteCode = MyTools.StrFiltr(vec.get(2));
			siteName = MyTools.StrFiltr(vec.get(3));
		}
		
		resultVec.add(flag);
		resultVec.add(teaCode.replaceAll("\\+", ","));
		resultVec.add(teaName.replaceAll("\\+", ","));
		resultVec.add(siteCode.replaceAll("\\+", ","));
		resultVec.add(siteName.replaceAll("\\+", ","));
//		
//		resultJson = "[";
//		for(int i=0; i<resultVec.size(); i++){
//			resultJson += "{\"comboName\":\"" + MyTools.StringToInt(MyTools.StrFiltr(resultVec.get(i))) + "\"," +
//					"\"comboValue\":\"" + MyTools.StrFiltr(resultVec.get(i)) + "\"},";
//		}
//		resultJson = resultJson.substring(0, resultJson.length()-1);
//		resultJson += "]";
		
		return resultVec;
	}
	
	/**
	 * 查询当前学期周次，天数等信息
	 * @date:2015-08-24
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadWeekInfo() throws SQLException{
		Vector vec = null;
		Vector resultVec = new Vector();
		String sql = "";
		String tempJson = "";
		String tempValue = "";
		
		sql = "select (select count(*) from V_规则管理_学期周次表 where 学年学期编码=a.学年学期编码) as 总周数," +
			"每周天数,上午节数+中午节数+下午节数+晚上节数 as 总节数 from V_规则管理_学年学期表 a where 学年学期编码='" + MyTools.fixSql(this.getTT_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		
		//周次下拉框数据
		int weekNum = 1;
		if(vec!=null && vec.size()>0)
			weekNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));
		tempJson = "[";
		for(int i=1; i<weekNum+1; i++){
			tempJson += "{\"comboName\":\"" + i + "\",\"comboValue\":\"" + i + "\"},";
		}
		tempJson = tempJson.substring(0, tempJson.length()-1);
		tempJson += "]";
		resultVec.add(tempJson);
		
		//星期下拉框数据
		int dayNum = 1;
		if(vec!=null && vec.size()>0)
			dayNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(1)));
		tempJson = "[";
		for(int i=1; i<dayNum+1; i++){
			tempValue = i<10?"0"+i:""+i;
			tempJson += "{\"comboName\":\"" + i + "\",\"comboValue\":\"" + tempValue + "\"},";
		}
		tempJson = tempJson.substring(0, tempJson.length()-1);
		tempJson += "]";
		resultVec.add(tempJson);
		
		//节数下拉框数据
		int lessonNum = 1;
		if(vec!=null && vec.size()>0)
			lessonNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(2)));
		tempJson = "[";
		for(int i=1; i<lessonNum+1; i++){
			tempValue = i<10?"0"+i:""+i;
			tempJson += "{\"comboName\":\"" + i + "\",\"comboValue\":\"" + tempValue + "\"},";
		}
		tempJson = tempJson.substring(0, tempJson.length()-1);
		tempJson += "]";
		resultVec.add(tempJson);

		return resultVec;
	}
	
	/**
	 * 保存方法
	 * @date:2015-08-25
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveRec() throws SQLException{
		String sql = "select count(*) from V_调课管理_调课信息主表 where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
		
		//检查调整后的教师和场地是否有冲突
		if(this.checkTeaAndSite("save")){
			this.setMSG("调整后的教师或场地有时间冲突，无法保存！");
		}else{
			//判断数据是否存在
			if(!db.getResultFromDB(sql)){
				this.addRec();//新增数据
			}else{
				this.modRec();//修改
			}
		}
	}
	
	/**
	 * 新建调整课程信息
	 * @date:2015-08-25
	 * @author:yeq
	 * @throws SQLException
	 */
	public void addRec() throws SQLException{
		String sql = "";
		
		//查询当前课程是否有未提交或未审核的课程调整申请
		sql = "select count(*) from V_调课管理_调课信息主表 where 授课计划明细编号='" + MyTools.fixSql(this.getTT_SKJHMXBH())+ "' and 审核状态 in ('0','1')";
		
		if(db.getResultFromDB(sql)){
			this.setMSG("当前课程有未提交或未审核的课程调整申请，暂时不能进行调整操作。");
		}else{
			this.setTT_ID(db.getMaxID("V_调课管理_调课信息主表", "编号", "TKBH_", 8));
			
			sql = "insert into V_调课管理_调课信息主表 select " +
				"'" + MyTools.fixSql(this.getTT_ID())+ "'," + //编号
				"'" + MyTools.fixSql(this.getTT_SXLX())+ "'," + //时限类型
				"'" + MyTools.fixSql(this.getTT_SQLX())+ "'," + //申请类型
				"'" + MyTools.fixSql(this.getTT_XNXQBM())+ "'," + //学年学期编码
				"(select 课程代码 from V_规则管理_授课计划明细表 where 授课计划明细编号='" + MyTools.fixSql(this.getTT_KCBH())+ "') as 课程编号," + //课程编号
				"'" + MyTools.fixSql(this.getTT_BJBH())+ "'," + //班级编号
				"'" + MyTools.fixSql(this.getTT_SKJHMXBH())+ "'," + //授课计划明细编号
				"'" + MyTools.fixSql(this.getTT_BGLY())+ "'," + //变更理由
				"'" + MyTools.fixSql(this.getTT_YJHSKZC())+ "'," + //原计划授课周次
				"'" + MyTools.fixSql(this.getTT_YJHXQ())+ "'," + //原计划星期
				"'" + MyTools.fixSql(this.getTT_YJHSJXL())+ "'," + //原计划时间序列
				"'" + MyTools.fixSql(this.getTT_YJHSKJSBH())+ "'," + //原计划授课教师编号
				"'" + MyTools.fixSql(this.getTT_YJHSKJSMC())+ "'," + //原计划授课教师名称
				"'" + MyTools.fixSql(this.getTT_YJHCDBH())+ "'," + //原计划场地编号
				"'" + MyTools.fixSql(this.getTT_YJHCDMC())+ "'," + //原计划场地名称
				"'" + MyTools.fixSql(this.getTT_TZHSKZC())+ "'," + //调整后授课周次
				"'" + MyTools.fixSql(this.getTT_TZHXQ())+ "'," + //调整后星期
				"'" + MyTools.fixSql(this.getTT_TZHSJXL())+ "'," + //调整后时间序列
				"'" + MyTools.fixSql(this.getTT_TZHSKJSBH())+ "'," + //调整后授课教师编号
				"'" + MyTools.fixSql(this.getTT_TZHSKJSMC())+ "'," + //调整后授课教师名称
				"'" + MyTools.fixSql(this.getTT_TZHCDBH())+ "'," + //调整后场地编号
				"'" + MyTools.fixSql(this.getTT_TZHCDMC())+ "'," + //调整后场地名称
				"'" + MyTools.fixSql(this.getTT_QTSM())+ "'," + //其他说明
				"'0'," + //审核状态
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," + //创建人
				"getDate()," + //创建时间
				"'1'"; //状态
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 修改调整课程信息
	 * @date:2015-08-25
	 * @author:yeq
	 * @throws SQLException
	 */
	public void modRec() throws SQLException{
		String sql = "update V_调课管理_调课信息主表 set " +
			//"申请类型='" + MyTools.fixSql(this.getTT_SQLX()) + "'," + 
			//"学年学期编码='" + MyTools.fixSql(this.getTT_XNXQBM()) + "'," + 
			//"课程编号='" + MyTools.fixSql(this.getTT_KCBH()) + "'," + 
			//"班级编号='" + MyTools.fixSql(this.getTT_BJBH()) + "'," + 
			//"授课计划明细编号='" + MyTools.fixSql(this.getTT_SKJHMXBH()) + "'," + 
			"变更理由='" + MyTools.fixSql(this.getTT_BGLY()) + "'," + 
			"原计划授课周次='" + MyTools.fixSql(this.getTT_YJHSKZC()) + "'," + 
			"原计划星期='" + MyTools.fixSql(this.getTT_YJHXQ()) + "'," + 
			"原计划时间序列='" + MyTools.fixSql(this.getTT_YJHSJXL()) + "'," + 
			"原计划授课教师编号='" + MyTools.fixSql(this.getTT_YJHSKJSBH()) + "'," + 
			"原计划授课教师名称='" + MyTools.fixSql(this.getTT_YJHSKJSMC()) + "'," + 
			"原计划场地编号='" + MyTools.fixSql(this.getTT_YJHCDBH()) + "'," + 
			"原计划场地名称='" + MyTools.fixSql(this.getTT_YJHCDMC()) + "'," + 
			"调整后授课周次='" + MyTools.fixSql(this.getTT_TZHSKZC()) + "'," + 
			"调整后星期='" + MyTools.fixSql(this.getTT_TZHXQ()) + "'," + 
			"调整后时间序列='" + MyTools.fixSql(this.getTT_TZHSJXL()) + "'," + 
			"调整后授课教师编号='" + MyTools.fixSql(this.getTT_TZHSKJSBH()) + "'," + 
			"调整后授课教师名称='" + MyTools.fixSql(this.getTT_TZHSKJSMC()) + "'," + 
			"调整后场地编号='" + MyTools.fixSql(this.getTT_TZHCDBH()) + "'," + 
			"调整后场地名称='" + MyTools.fixSql(this.getTT_TZHCDMC()) + "'," + 
			"其他说明='" + MyTools.fixSql(this.getTT_QTSM()) + "' " + 
			"where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";

		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 提交调整课程信息
	 * @date:2015-08-25
	 * @author:yeq
	 * @throws SQLException
	 */
	public void submitRec() throws SQLException{
		String sql = ""; 
		Vector vec = null;
		Vector sqlVec = new Vector();
		boolean flag = true;
		
		//检查调整后的教师和场地是否有冲突
		if(this.checkTeaAndSite("submit")){
			this.setMSG("调整后的教师或教室有时间冲突，无法提交！");
		}else{
			//更新审核状态
			sql = "update V_调课管理_调课信息主表 set 审核状态='1' " +
				"where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			sqlVec.add(sql);
			
			//获取所有调整过的周次及时间序列
			sql = "select 申请类型,学年学期编码,班级编号,a.授课计划明细编号,a.课程编号,b.课程名称,原计划授课教师编号,原计划场地编号,调整后授课教师编号,调整后授课教师名称,调整后场地编号,调整后场地名称," +
				"原计划授课周次,原计划星期,原计划时间序列,调整后授课周次,调整后星期,调整后时间序列 from V_调课管理_调课信息主表 a " +
				"left join V_规则管理_授课计划明细表 b on b.授课计划明细编号=a.授课计划明细编号 " +
				"where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				String weekNum = "";
				String sqlx = MyTools.StrFiltr(vec.get(0));//申请类型
				String xnxq = MyTools.StrFiltr(vec.get(1));//学年学期
				String classCode = MyTools.StrFiltr(vec.get(2));//班级编号
				String skjhCode = MyTools.StrFiltr(vec.get(3));//授课计划明细编号
				String courseCode = MyTools.StrFiltr(vec.get(4));//课程编号
				String courseName = MyTools.StrFiltr(vec.get(5));//课程名称
				String yjhjsCode = MyTools.StrFiltr(vec.get(6));//原计划授课教师编号
				String yjhcdCode = MyTools.StrFiltr(vec.get(7));//原计划授课场地编号
				String tzhjsCode = MyTools.StrFiltr(vec.get(8));//调整后授课教师编号
				String tzhjsName = MyTools.StrFiltr(vec.get(9));//调整后授课教师名称
				String tzhcdCode = MyTools.StrFiltr(vec.get(10));//调整后场地编号
				String tzhcdName = MyTools.StrFiltr(vec.get(11));//调整后场地名称
				String yjhskzcArray[] = MyTools.StrFiltr(vec.get(12)).split(",");//原计划授课周次
				String yjhxq = MyTools.StrFiltr(vec.get(13));//原计划星期
				String yjhsjxlArray[] = MyTools.StrFiltr(vec.get(14)).split(",");//原计划时间序列
				String tzhskzcArray[] = MyTools.StrFiltr(vec.get(15)).split(",");//调整后授课周次
				String tzhxq = MyTools.StrFiltr(vec.get(16));//调整后星期
				String tzhsjxlArray[] = MyTools.StrFiltr(vec.get(17)).split(",");//调整后时间序列
				String timeOrderAll = "";
				String id = "";
				
				String yjhskzc = "";
				String yjhTimeOrder = "";
				String tzhskzc = "";
				String tzhTimeOrder = "";
				
				//原计划授课周次
				for(int i=0; i<yjhskzcArray.length; i++){
					yjhskzc += "'"+yjhskzcArray[i]+"',";
				}
				yjhskzc = yjhskzc.substring(0, yjhskzc.length()-1);
				
				//原计划时间序列
				for(int i=0; i<yjhsjxlArray.length; i++){
					yjhTimeOrder += "'"+yjhxq+yjhsjxlArray[i]+"',";
				}
				yjhTimeOrder = yjhTimeOrder.substring(0, yjhTimeOrder.length()-1);
				
				//调整后授课周次
				for(int i=0; i<tzhskzcArray.length; i++){
					tzhskzc += "'"+tzhskzcArray[i]+"',";
				}
				tzhskzc = tzhskzc.substring(0, tzhskzc.length()-1);
				
				//调整后时间序列
				for(int i=0; i<tzhsjxlArray.length; i++){
					tzhTimeOrder += "'"+tzhxq+tzhsjxlArray[i]+"',";
				}
				tzhTimeOrder = tzhTimeOrder.substring(0, tzhTimeOrder.length()-1);
				
				//判断是1调课/2停课/3补课
				if("1".equalsIgnoreCase(sqlx)){
					String yjhTimeOrderArray[] = yjhTimeOrder.replaceAll("'", "").split(",");
					String tzhTimeOrderArray[] = tzhTimeOrder.replaceAll("'", "").split(",");
					Vector skzcCompareVec = new Vector();
					
					//查询原课程表信息
					sql = "select cast(授课周次 as int) as 授课周次,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称 from V_排课管理_课程表周详情表 " +
						"where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxq) + "' " +
						"and 行政班代码='" + MyTools.fixSql(classCode) + "' " +
						"and ((授课周次 in (" + yjhskzc + ") and 时间序列 in (" + yjhTimeOrder + ")) " +
						"or (授课周次 in(" + tzhskzc + ") and 时间序列 in (" + tzhTimeOrder + "))) " +
						"order by cast(授课周次 as int),时间序列";
					vec = db.GetContextVector(sql);
					
					//获取时间序列对比数据
					for(int i=0; i<yjhskzcArray.length; i++){
						for(int j=0; j<yjhTimeOrderArray.length; j++){
							skzcCompareVec.add(yjhskzcArray[i]);
							skzcCompareVec.add(yjhTimeOrderArray[j]);
							skzcCompareVec.add(tzhskzcArray[i]);
							skzcCompareVec.add(tzhTimeOrderArray[j]);
						}
					}
					
					String tempYjhSkzc = "";
					String tempYjhTimeOrder = "";
					String tempTzhSkzc = "";
					String tempTzhTimeOrder = "";
					Vector tempVec = (Vector)vec.clone();
					Vector resultVec = new Vector();
					
					for(int i=0; i<skzcCompareVec.size(); i+=4){
						tempYjhSkzc = MyTools.StrFiltr(skzcCompareVec.get(i));
						tempYjhTimeOrder = MyTools.StrFiltr(skzcCompareVec.get(i+1));
						tempTzhSkzc = MyTools.StrFiltr(skzcCompareVec.get(i+2));
						tempTzhTimeOrder = MyTools.StrFiltr(skzcCompareVec.get(i+3));
						
						for(int j=0; j<vec.size(); j+=9){
							//调整后位置信息替换到原计划课程信息
							if(tempYjhSkzc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j))) 
								&& tempYjhTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1))) 
								&& skjhCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+2)))){
								tempVec.set(j, tempTzhSkzc);
								tempVec.set(j+1, tempTzhTimeOrder);
								tempVec.set(j+5, tzhjsCode);
								tempVec.set(j+6, tzhjsName);
								tempVec.set(j+7, tzhcdCode);
								tempVec.set(j+8, tzhcdName);
							}
							
							//原计划替换到调整后
							if(tempTzhSkzc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j))) 
								&& tempTzhTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1)))){
								tempVec.set(j, tempYjhSkzc);
								tempVec.set(j+1, tempYjhTimeOrder);
							}
						}
					}
					
					String tempSkzc = "";
					String tempTimeOrder = "";
					//重新排序
					for(int i=0; i<vec.size(); i+=9){
						if(!tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !tempTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
							tempSkzc = MyTools.StrFiltr(vec.get(i));
							tempTimeOrder = MyTools.StrFiltr(vec.get(i+1));
							
							for(int j=0; j<tempVec.size(); j+=9){
								if(tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j))) && tempTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j+1)))){
									resultVec.add(tempVec.get(j));
									resultVec.add(tempVec.get(j+1));
									resultVec.add(tempVec.get(j+2));
									resultVec.add(tempVec.get(j+3));
									resultVec.add(tempVec.get(j+4));
									resultVec.add(tempVec.get(j+5));
									resultVec.add(tempVec.get(j+6));
									resultVec.add(tempVec.get(j+7));
									resultVec.add(tempVec.get(j+8));
								}
							}
						}
					}
					
					for(int i=0; i<resultVec.size(); i+=9){
						id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
						sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
							"'" + MyTools.fixSql(id) + "'," + //编号
							"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+2))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+3))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+4))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+5))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+6))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+7))) + "'," +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+8))) + "'," +
							"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
							"getDate(),'1')";
						sqlVec.add(sql);
					}
				}else if("2".equalsIgnoreCase(sqlx)){
					//查询原课程表信息
					sql = "select cast(授课周次 as int) as 授课周次,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称 from V_排课管理_课程表周详情表 " +
						"where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxq) + "' " +
						"and 行政班代码='" + MyTools.fixSql(classCode) + "' " +
						"and 授课周次 in (" + yjhskzc + ") " +
						"and 时间序列 in (" + yjhTimeOrder + ") " +
						"order by cast(授课周次 as int),时间序列";
					vec = db.GetContextVector(sql);
					
					for(int i=0; i<vec.size(); i+=9){
						id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
						sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称," +
								"授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
								"'" + MyTools.fixSql(id) + "'," + //编号
								"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+1))) + "',";
						
						if(skjhCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
							sql += "'','','','','','','',";
						}else{
							sql += "'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+2))) + "'," + 
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+3))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+4))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+5))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+6))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+7))) + "'," +
								"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+8))) + "',";
						}
						
						sql += "'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
							"getDate(),'1')";
						sqlVec.add(sql);
					}
				}else if("3".equalsIgnoreCase(sqlx)){
//					//查询原课程表信息
//					sql = "select cast(授课周次 as int) as 授课周次,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称 from V_排课管理_课程表周详情表 " +
//						"where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxq) + "' " +
//						"and 行政班代码='" + MyTools.fixSql(classCode) + "' " +
//						"and 授课周次 in (" + tzhskzc + ") " +
//						"and 时间序列 in (" + tzhTimeOrder + ") " +
//						"order by cast(授课周次 as int),时间序列";
//					vec = db.GetContextVector(sql);
//					
//					String timeOrderArray[] = tzhTimeOrder.replaceAll("'", "").split(",");
//					for(int i=0; i<tzhskzcArray.length; i++){
//						for(int j=0; j<timeOrderArray.length; j++){
//							//添加补课信息
//							id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//							sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
//								"'" + MyTools.fixSql(id) + "'," + //编号
//								"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//								"'" + MyTools.fixSql(tzhskzcArray[i]) + "'," +
//								"'" + MyTools.fixSql(timeOrderArray[j]) + "'," +
//								"'" + MyTools.fixSql(skjhCode) + "'," +
//								"'" + MyTools.fixSql(courseCode) + "'," +
//								"'" + MyTools.fixSql(courseName) + "'," +
//								"'" + MyTools.fixSql(tzhjsCode) + "'," +
//								"'" + MyTools.fixSql(tzhjsName) + "'," +
//								"'" + MyTools.fixSql(tzhcdCode) + "'," +
//								"'" + MyTools.fixSql(tzhcdName) + "'," +
//								"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//								"getDate(),'1')";
//							sqlVec.add(sql);
//							
//							for(int k=0; k<vec.size(); k+=9){
//								if(tzhskzcArray[i].equalsIgnoreCase(MyTools.StrFiltr(vec.get(k))) 
//									&& timeOrderArray[j].equalsIgnoreCase(MyTools.StrFiltr(vec.get(k+1))) 
//									&& !"".equalsIgnoreCase(MyTools.StrFiltr(vec.get(k+3)))){
//									id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//									sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
//										"'" + MyTools.fixSql(id) + "'," + //编号
//										"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//										"'" + MyTools.fixSql(tzhskzcArray[i]) + "'," +
//										"'" + MyTools.fixSql(timeOrderArray[j]) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(k+2))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(k+3))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(k+4))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(k+5))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(k+6))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(k+7))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(k+8))) + "'," +
//										"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//										"getDate(),'1')";
//									sqlVec.add(sql);
//								}
//							}
//						}
//					}
//				}
				
				//20170425修改逻辑yeq，在进行补课提交操作时，需判断补课位置在原课表是否有课程以及是否其他有提交待审核的调课信息
				//查询其他调课信息	
				sql = "select count(*) from V_调课管理_调课信息明细表 a " +
					"left join V_调课管理_调课信息主表 b on b.编号=a.调课信息主表编号 " +
					"where b.审核状态='1' and b.学年学期编码='" + MyTools.fixSql(xnxq) + "' " +
					"and b.班级编号='" + MyTools.fixSql(classCode) + "' " +
					"and a.授课周次 in (" + tzhskzc + ") " +
					"and a.时间序列 in (" + tzhTimeOrder + ") ";
				
				if(!db.getResultFromDB(sql)){
					//查询原课程表是否有课程
					sql = "select count(*) from V_排课管理_课程表周详情表 " +
						"where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxq) + "' " +
						"and 行政班代码='" + MyTools.fixSql(classCode) + "' " +
						"and 授课周次 in (" + tzhskzc + ") " +
						"and 时间序列 in (" + tzhTimeOrder + ") " +
						"and 授课计划明细编号<>''";
					
					if(!db.getResultFromDB(sql)){
						String timeOrderArray[] = tzhTimeOrder.replaceAll("'", "").split(",");
						for(int i=0; i<tzhskzcArray.length; i++){
							for(int j=0; j<timeOrderArray.length; j++){
								//添加补课信息
								id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
								sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
									"'" + MyTools.fixSql(id) + "'," + //编号
									"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
									"'" + MyTools.fixSql(tzhskzcArray[i]) + "'," +
									"'" + MyTools.fixSql(timeOrderArray[j]) + "'," +
									"'" + MyTools.fixSql(skjhCode) + "'," +
									"'" + MyTools.fixSql(courseCode) + "'," +
									"'" + MyTools.fixSql(courseName) + "'," +
									"'" + MyTools.fixSql(tzhjsCode) + "'," +
									"'" + MyTools.fixSql(tzhjsName) + "'," +
									"'" + MyTools.fixSql(tzhcdCode) + "'," +
									"'" + MyTools.fixSql(tzhcdName) + "'," +
									"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
									"getDate(),'1')";
								sqlVec.add(sql);
							}
						}
					}else{
						flag = false;
						this.setMSG("补课所选时间已有课程，请修改后重新提交！");
					}
				}else{
					flag = false;
					this.setMSG("补课所选时间目前尚有提交审核中的其他调课信息，请修改时间后或等待其他调课申请审核完毕后重新提交！");
				}
			}
				
//				if("1".equalsIgnoreCase(sqlx)){
//					//获取被调换的时间序列
//					for(int i=0; i<yjhsjxlArray.length; i++){
//						timeOrderAll += "'"+yjhxq+yjhsjxlArray[i]+"',";
//						timeOrderAll += "'"+tzhxq+tzhsjxlArray[i]+"',";
//					}
//					timeOrderAll = timeOrderAll.substring(0, timeOrderAll.length()-1);
//					
//					//获取当前学年学期总周次数
//					sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(xnxq) + "'";
//					vec = db.GetContextVector(sql);
//					weekNum = MyTools.StrFiltr(vec.get(0));
//					
//					//查询原来课程内容
//					sql = "select 时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,授课周次详情 from V_排课管理_课程表明细详情表 " +
//						"where 学年学期编码='" + MyTools.fixSql(xnxq) + "' " +
//						"and 行政班代码='" + MyTools.fixSql(classCode) + "' " +
//						"and 时间序列 in(" + timeOrderAll + ") order by 时间序列";
//					vec = db.GetContextVector(sql);
//					
//					String curSkjhmxbh[] = new String[0];
//					String curCourseCode[] = new String[0];
//					String curCourseName[] = new String[0];
//					
//					String curTeaCode[] = new String[0];
//					String tempCurTeaCode[] = new String[0];
//					String teaCodeArray[] = new String[0];
//					String teaCode = "";
//					
//					String curTeaName[] = new String[0];
//					String tempCurTeaName[] = new String[0];
//					String teaNameArray[] = new String[0];
//					String teaName = "";
//					
//					String curSiteCode[] = new String[0];
//					String tempCurSiteCode[] = new String[0];
//					String siteCodeArray[] = new String[0];
//					String siteCode = "";
//					
//					String curSiteName[] = new String[0];
//					String siteNameArray[] = new String[0];
//					String tempCurSiteName[] = new String[0];
//					String siteName = "";
//					
//					String curSkzc[] = new String[0];
//					String tempCurSkzc[] = new String[0];
//					Vector skzcVec = new Vector();
//					
//					for(int i=0; i<yjhskzcArray.length; i++){
//						for(int j=0; j<yjhsjxlArray.length; j++){
//							yjhTimeOrder = yjhxq+yjhsjxlArray[j];
//							tzhTimeOrder = tzhxq+tzhsjxlArray[j];
//							
//							for(int k=0; k<vec.size(); k+=9){
//								curSkjhmxbh = MyTools.StrFiltr(vec.get(k+1)).split("｜");
//								curCourseCode = MyTools.StrFiltr(vec.get(k+2)).split("｜");
//								curCourseName = MyTools.StrFiltr(vec.get(k+3)).split("｜");
//								curTeaCode = MyTools.StrFiltr(vec.get(k+4)).split("｜");
//								curTeaName = MyTools.StrFiltr(vec.get(k+5)).split("｜");
//								curSiteCode = MyTools.StrFiltr(vec.get(k+6)).split("｜");
//								curSiteName = MyTools.StrFiltr(vec.get(k+7)).split("｜");
//								curSkzc = MyTools.StrFiltr(vec.get(k+8)).split("｜");
//								
//								if(yjhTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(k)))){
//									//更换原课程的授课教师和场地
//									for(int a=0; a<curSkjhmxbh.length; a++){
//										tempCurTeaCode = curTeaCode[a].split("&");
//										tempCurTeaName = curTeaName[a].split("&");
//										tempCurSiteCode = curSiteCode[a].split("&");
//										tempCurSiteName = curSiteName[a].split("&");
//										tempCurSkzc = curSkzc[a].split("&");
//										
//										//判断当前授课周次与原课程信息中的授课周次是否匹配
//										for(int b=0; b<tempCurSkzc.length; b++){
//											skzcVec = this.formatSkzc(tempCurSkzc[b], weekNum);
//											
//											if(skzcVec.indexOf(yjhskzcArray[i]) > -1){
//												if(courseCode.equalsIgnoreCase(curCourseCode[a])){
//													//替换教师和场地信息
//													teaCodeArray = tempCurTeaCode[b].split("\\+");
//													teaNameArray = tempCurTeaName[b].split("\\+");
//													siteCodeArray = tempCurSiteCode[b].split("\\+");
//													siteNameArray = tempCurSiteName[b].split("\\+");
//													teaCode = "";
//													teaName = "";
//													siteCode = "";
//													siteName = "";
//													
//													for(int c=0; c<teaCodeArray.length; c++){
//														if(yjhjsCode.equalsIgnoreCase(teaCodeArray[c])){
//															teaCode += tzhjsCode+"+";
//															teaName += tzhjsName+"+";
//														}else{
//															teaCode += teaCodeArray[c]+"+";
//															teaName += teaNameArray[c]+"+";
//														}
//													}
//													teaCode = teaCode.substring(0, teaCode.length()-1);
//													teaName = teaName.substring(0, teaName.length()-1);
//													
//													for(int c=0; c<siteCodeArray.length; c++){
//														if(yjhcdCode.equalsIgnoreCase(siteCodeArray[c])){
//															siteCode += tzhcdCode+"+";
//															siteName += tzhcdName+"+";
//														}else{
//															siteCode += siteCodeArray[c]+"+";
//															siteName += siteNameArray[c]+"+";
//														}
//													}
//													siteCode = siteCode.substring(0, siteCode.length()-1);
//													siteName = siteName.substring(0, siteName.length()-1);
//													
//													id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//													sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称," +
//														"授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
//														"'" + MyTools.fixSql(id) + "'," + //编号
//														"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//														"'" + MyTools.fixSql(tzhskzcArray[i]) + "'," +
//														"'" + MyTools.fixSql(tzhTimeOrder) + "'," +
//														"'" + MyTools.fixSql(skjhCode) + "'," +
//														"'" + MyTools.fixSql(courseCode) + "'," +
//														"'" + MyTools.fixSql(courseName) + "'," +
//														"'" + MyTools.fixSql(teaCode) + "'," +
//														"'" + MyTools.fixSql(teaName) + "'," +
//														"'" + MyTools.fixSql(siteCode) + "'," +
//														"'" + MyTools.fixSql(siteName) + "'," +
//														"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//														"getDate(),'1')";
//													sqlVec.add(sql);
//												}
//											}
//										}
//									}
//								}
//								
//								if(tzhTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(k)))){
//									//更换原课程的授课教师和场地
//									for(int a=0; a<curCourseCode.length; a++){
//										tempCurTeaCode = curTeaCode[a].split("&");
//										tempCurTeaName = curTeaName[a].split("&");
//										tempCurSiteCode = curSiteCode[a].split("&");
//										tempCurSiteName = curSiteName[a].split("&");
//										tempCurSkzc = curSkzc[a].split("&");
//										
//										//判断是否有课程
//										if("".equalsIgnoreCase(curCourseCode[a])){
//											id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//											sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,创建人,创建时间,状态) values(" +
//												"'" + MyTools.fixSql(id) + "'," + //编号
//												"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//												"'" + MyTools.fixSql(yjhskzcArray[i]) + "'," +
//												"'" + MyTools.fixSql(yjhTimeOrder) + "'," +
//												"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//												"getDate(),'1')";
//											sqlVec.add(sql);
//										}else{
//											//判断当前授课周次与原课程信息中的授课周次是否匹配
//											for(int b=0; b<tempCurSkzc.length; b++){
//												skzcVec = this.formatSkzc(tempCurSkzc[b], weekNum);
//												if(skzcVec.indexOf(tzhskzcArray[i]) > -1){
//													id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//													sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称," +
//														"授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
//														"'" + MyTools.fixSql(id) + "'," + //编号
//														"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//														"'" + MyTools.fixSql(yjhskzcArray[i]) + "'," +
//														"'" + MyTools.fixSql(yjhTimeOrder) + "'," +
//														"'" + MyTools.fixSql(curSkjhmxbh[a]) + "'," +
//														"'" + MyTools.fixSql(curCourseCode[a]) + "'," +
//														"'" + MyTools.fixSql(curCourseName[a]) + "'," +
//														"'" + MyTools.fixSql(tempCurTeaCode[b]) + "'," +
//														"'" + MyTools.fixSql(tempCurTeaName[b]) + "'," +
//														"'" + MyTools.fixSql(tempCurSiteCode[b]) + "'," +
//														"'" + MyTools.fixSql(tempCurSiteName[b]) + "'," +
//														"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//														"getDate(),'1')";
//													sqlVec.add(sql);
//													flag = true;
//													break;
//												}
//											}
//											
//											//判断当前课程的授课周次如果与原课程不冲突
//											if(flag == false){
//												id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//												sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,创建人,创建时间,状态) values(" +
//													"'" + MyTools.fixSql(id) + "'," + //编号
//													"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//													"'" + MyTools.fixSql(yjhskzcArray[i]) + "'," +
//													"'" + MyTools.fixSql(yjhTimeOrder) + "'," +
//													"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//													"getDate(),'1')";
//												sqlVec.add(sql);
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}else if("2".equalsIgnoreCase(sqlx)){//停课
//					for(int i=0; i<yjhskzcArray.length; i++){
//						for(int j=0; j<yjhsjxlArray.length; j++){
//							yjhTimeOrder = yjhxq+yjhsjxlArray[j];
//							
//							id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//							sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,创建人,创建时间,状态) values(" +
//								"'" + MyTools.fixSql(id) + "'," + //编号
//								"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//								"'" + MyTools.fixSql(yjhskzcArray[i]) + "'," +
//								"'" + MyTools.fixSql(yjhTimeOrder) + "'," +
//								"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//								"getDate(),'1')";
//							sqlVec.add(sql);
//						}
//					}
//				}else if("3".equalsIgnoreCase(sqlx)){//补课
//					for(int i=0; i<tzhskzcArray.length; i++){
//						for(int j=0; j<tzhsjxlArray.length; j++){
//							tzhTimeOrder = tzhxq+tzhsjxlArray[j];
//							
//							id = db.getMaxID("V_调课管理_调课信息明细表", "编号", "TKMXBH_", 10);
//							sql = "insert into V_调课管理_调课信息明细表(编号,调课信息主表编号,授课周次,时间序列,授课计划明细编号,课程编号,课程名称,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,创建人,创建时间,状态) values(" +
//								"'" + MyTools.fixSql(id) + "'," + //编号
//								"'" + MyTools.fixSql(this.getTT_ID()) + "'," +
//								"'" + MyTools.fixSql(tzhskzcArray[i]) + "'," +
//								"'" + MyTools.fixSql(tzhTimeOrder) + "'," +
//								"'" + MyTools.fixSql(skjhCode) + "'," +
//								"'" + MyTools.fixSql(courseCode) + "'," +
//								"'" + MyTools.fixSql(courseName) + "'," +
//								"'" + MyTools.fixSql(tzhjsCode) + "'," +
//								"'" + MyTools.fixSql(tzhjsName) + "'," +
//								"'" + MyTools.fixSql(tzhcdCode) + "'," +
//								"'" + MyTools.fixSql(tzhcdName) + "'," +
//								"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//								"getDate(),'1')";
//							sqlVec.add(sql);
//						}
//					}
//				}
			}
			
			if(flag){
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					this.setMSG("保存成功");
				}else{
					this.setMSG("保存失败");
				}
			}
		}
	}

	/**
	 * 审核调整课程信息
	 * @date:2016-03-24
	 * @author:yeq
	 * @throws SQLException
	 */
	public void auditRec(String type) throws SQLException{
		String result = "";
		String sql = "";
		Vector sqlVec = new Vector();
		Vector vec = null;
		
		if("pass".equalsIgnoreCase(type)) result = "2";
		if("reject".equalsIgnoreCase(type)) result = "3";
		
		//更新审核状态
		sql = "update V_调课管理_调课信息主表 set " +
			"审核状态='" + MyTools.fixSql(result) + "' " +
			"where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
		sqlVec.add(sql);
		
		sql = "update V_调课管理_调课信息明细表 set 创建时间=getDate() " +
			"where 调课信息主表编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
		sqlVec.add(sql);
		
		//判断如果通过审核,更新课程表周详情
		if("pass".equalsIgnoreCase(type)){
			//查询所有相关信息
			sql = "select distinct (select distinct 课程表明细编号 from V_排课管理_课程表周详情表 where 学年学期编码=b.学年学期编码 and 行政班代码=b.班级编号 and 授课周次=a.授课周次 and 时间序列=a.时间序列) as 课程表明细编号," +
				"(select distinct 课程表主表编号 from V_排课管理_课程表周详情表 where 学年学期编码=b.学年学期编码 and 行政班代码=b.班级编号 and 授课周次=a.授课周次 and 时间序列=a.时间序列) as 课程表主表编号," +
				"b.学年学期编码,b.班级编号,c.班级名称,d.专业代码,d.专业名称,a.授课周次,a.时间序列,a.授课计划明细编号,a.课程编号,a.课程名称,e.课程类型,a.授课教师编号,a.授课教师姓名,a.实际场地编号,a.实际场地名称 from V_调课管理_调课信息明细表 a " +
				"inner join V_调课管理_调课信息主表 b on b.编号=a.调课信息主表编号 " +
				//"left join V_学校班级_数据子类 c on c.行政班代码=b.班级编号 " +
				"left join V_基础信息_班级信息表 c on c.班级编号=b.班级编号 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
				"left join V_规则管理_授课计划明细表 e on e.授课计划明细编号=a.授课计划明细编号 " +
				"where a.调课信息主表编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				//删除原数据
				sql = "delete from V_排课管理_课程表周详情表 where 编号 in (select a.编号 from V_排课管理_课程表周详情表 a " +
					"left join (select d.编号,d.学年学期编码,d.班级编号,c.授课周次,c.时间序列 from V_调课管理_调课信息明细表 c " +
					"left join V_调课管理_调课信息主表 d on c.调课信息主表编号=d.编号) as b " +
					"on b.学年学期编码=a.学年学期编码 and b.班级编号=a.行政班代码 and b.授课周次=a.授课周次 and b.时间序列=a.时间序列 " +
					"where b.编号='" + MyTools.fixSql(this.getTT_ID()) + "')";
				sqlVec.add(sql);
				
				for(int i=0; i<vec.size(); i+=17){
					sql = "insert into V_排课管理_课程表周详情表 (课程表明细编号,课程表主表编号,学年学期编码,行政班代码,行政班名称,专业代码,专业名称,授课周次," +
						"时间序列,授课计划明细编号,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,场地编号,场地名称,状态) values(" +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+1))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+2))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+3))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+4))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+5))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+6))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+7))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+8))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+9))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+10))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+11))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+12))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+13))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+14))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+15))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i+16))) + "'," +
						"'1')";
					sqlVec.add(sql);
				}
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
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
	 * 检查调整后的教师和场地是否存在冲突
	 * @date:2017-07-20
	 * @param type 类型
	 * @return boolean
	 * @author:yeq
	 * @throws SQLException 
	 */
	public boolean checkTeaAndSite(String type) throws SQLException{
		boolean flag = false;
		Vector vec = null;
		String sql = "";
		
		if("submit".equalsIgnoreCase(type)){
			//获取所有调整过的周次及时间序列
			sql = "select 申请类型,学年学期编码,班级编号,a.授课计划明细编号,调整后授课教师编号,调整后场地编号,调整后授课周次,调整后星期,调整后时间序列 " +
				"from V_调课管理_调课信息主表 a " +
				"left join V_规则管理_授课计划明细表 b on b.授课计划明细编号=a.授课计划明细编号 " +
				"where 编号='" + MyTools.fixSql(this.getTT_ID()) + "'";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				this.setTT_SQLX(MyTools.StrFiltr(vec.get(0)));
				this.setTT_XNXQBM(MyTools.StrFiltr(vec.get(1)));
				this.setTT_BJBH(MyTools.StrFiltr(vec.get(2)));
				this.setTT_SKJHMXBH(MyTools.StrFiltr(vec.get(3)));
				this.setTT_TZHSKJSBH(MyTools.StrFiltr(vec.get(4)));
				this.setTT_TZHCDBH(MyTools.StrFiltr(vec.get(5)));
				this.setTT_TZHSKZC(MyTools.StrFiltr(vec.get(6)));
				this.setTT_TZHXQ(MyTools.StrFiltr(vec.get(7)));
				this.setTT_TZHSJXL(MyTools.StrFiltr(vec.get(8)));
			}
		}
		
		if("1".equalsIgnoreCase(this.getTT_SQLX()) || "3".equalsIgnoreCase(this.getTT_SQLX())){
			//查询调整后的教师教室信息是否已使用
			sql = "select count(*) from V_排课管理_课程表周详情表 " +
				"where 学年学期编码='" + MyTools.fixSql(this.getTT_XNXQBM()) + "' " +
				"and 行政班代码<>'" + MyTools.fixSql(this.getTT_BJBH()) + "' " +
				"and 授课计划明细编号<>'" + MyTools.fixSql(this.getTT_SKJHMXBH()) + "' " +
				"and 授课周次 in ('" + this.getTT_TZHSKZC().replaceAll(",", "','") + "') " +
				"and left(时间序列,2) in ('" + this.getTT_TZHXQ().replaceAll(",", "','") + "') " +
				"and right(时间序列,2) in ('" + this.getTT_TZHSJXL().replaceAll(",", "','") + "')";
			if(!"".equalsIgnoreCase(this.getTT_TZHSKJSBH()) && !"".equalsIgnoreCase(this.getTT_TZHSKJSBH())){
				sql += " and (";
				
				if(!"".equalsIgnoreCase(this.getTT_TZHSKJSBH())){
					String tempTeaArray[] = this.getTT_TZHSKJSBH().split(",");

					sql += "(";
					for(int i=0; i<tempTeaArray.length; i++){
						sql += "'@'+replace(授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(tempTeaArray[i]) + "@%' or ";
					}
					sql = sql.substring(0, sql.length()-4);
					sql += ")";
				}
				
				if(!"".equalsIgnoreCase(this.getTT_TZHSKJSBH())){
					String tempSiteArray[] = this.getTT_TZHCDBH().split(",");

					if(!"".equalsIgnoreCase(this.getTT_TZHSKJSBH())){
						sql += " or ";
					}
					
					sql += "(";
					for(int i=0; i<tempSiteArray.length; i++){
						sql += "'@'+replace(场地编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(tempSiteArray[i]) + "@%' or ";
					}
					sql = sql.substring(0, sql.length()-4);
					sql += ")";
				}
				
				sql += ")";
			}
			
			if(db.getResultFromDB(sql)){
				flag = true;
			}
		}
		
		return flag;
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

	public String getTT_ID() {
		return TT_ID;
	}

	public void setTT_ID(String tT_ID) {
		TT_ID = tT_ID;
	}

	public String getTT_SXLX() {
		return TT_SXLX;
	}

	public void setTT_SXLX(String tT_SXLX) {
		TT_SXLX = tT_SXLX;
	}

	public String getTT_SQLX() {
		return TT_SQLX;
	}

	public void setTT_SQLX(String tT_SQLX) {
		TT_SQLX = tT_SQLX;
	}

	public String getTT_XNXQBM() {
		return TT_XNXQBM;
	}

	public void setTT_XNXQBM(String tT_XNXQBM) {
		TT_XNXQBM = tT_XNXQBM;
	}

	public String getTT_ZYDM() {
		return TT_ZYDM;
	}

	public void setTT_ZYDM(String tT_ZYDM) {
		TT_ZYDM = tT_ZYDM;
	}

	public String getTT_KCBH() {
		return TT_KCBH;
	}

	public void setTT_KCBH(String tT_KCBH) {
		TT_KCBH = tT_KCBH;
	}

	public String getTT_BJBH() {
		return TT_BJBH;
	}

	public void setTT_BJBH(String tT_BJBH) {
		TT_BJBH = tT_BJBH;
	}

	public String getTT_SKJHMXBH() {
		return TT_SKJHMXBH;
	}

	public void setTT_SKJHMXBH(String tT_SKJHMXBH) {
		TT_SKJHMXBH = tT_SKJHMXBH;
	}

	public String getTT_BGLY() {
		return TT_BGLY;
	}

	public void setTT_BGLY(String tT_BGLY) {
		TT_BGLY = tT_BGLY;
	}

	public String getTT_YJHSKZC() {
		return TT_YJHSKZC;
	}

	public void setTT_YJHSKZC(String tT_YJHSKZC) {
		TT_YJHSKZC = tT_YJHSKZC;
	}

	public String getTT_YJHXQ() {
		return TT_YJHXQ;
	}

	public void setTT_YJHXQ(String tT_YJHXQ) {
		TT_YJHXQ = tT_YJHXQ;
	}

	public String getTT_YJHSJXL() {
		return TT_YJHSJXL;
	}

	public void setTT_YJHSJXL(String tT_YJHSJXL) {
		TT_YJHSJXL = tT_YJHSJXL;
	}

	public String getTT_YJHSKJSBH() {
		return TT_YJHSKJSBH;
	}

	public void setTT_YJHSKJSBH(String tT_YJHSKJSBH) {
		TT_YJHSKJSBH = tT_YJHSKJSBH;
	}

	public String getTT_YJHSKJSMC() {
		return TT_YJHSKJSMC;
	}

	public void setTT_YJHSKJSMC(String tT_YJHSKJSMC) {
		TT_YJHSKJSMC = tT_YJHSKJSMC;
	}

	public String getTT_YJHCDBH() {
		return TT_YJHCDBH;
	}

	public void setTT_YJHCDBH(String tT_YJHCDBH) {
		TT_YJHCDBH = tT_YJHCDBH;
	}

	public String getTT_YJHCDMC() {
		return TT_YJHCDMC;
	}

	public void setTT_YJHCDMC(String tT_YJHCDMC) {
		TT_YJHCDMC = tT_YJHCDMC;
	}

	public String getTT_TZHSKZC() {
		return TT_TZHSKZC;
	}

	public void setTT_TZHSKZC(String tT_TZHSKZC) {
		TT_TZHSKZC = tT_TZHSKZC;
	}

	public String getTT_TZHXQ() {
		return TT_TZHXQ;
	}

	public void setTT_TZHXQ(String tT_TZHXQ) {
		TT_TZHXQ = tT_TZHXQ;
	}

	public String getTT_TZHSJXL() {
		return TT_TZHSJXL;
	}

	public void setTT_TZHSJXL(String tT_TZHSJXL) {
		TT_TZHSJXL = tT_TZHSJXL;
	}

	public String getTT_TZHSKJSBH() {
		return TT_TZHSKJSBH;
	}

	public void setTT_TZHSKJSBH(String tT_TZHSKJSBH) {
		TT_TZHSKJSBH = tT_TZHSKJSBH;
	}

	public String getTT_TZHSKJSMC() {
		return TT_TZHSKJSMC;
	}

	public void setTT_TZHSKJSMC(String tT_TZHSKJSMC) {
		TT_TZHSKJSMC = tT_TZHSKJSMC;
	}

	public String getTT_TZHCDBH() {
		return TT_TZHCDBH;
	}

	public void setTT_TZHCDBH(String tT_TZHCDBH) {
		TT_TZHCDBH = tT_TZHCDBH;
	}

	public String getTT_TZHCDMC() {
		return TT_TZHCDMC;
	}

	public void setTT_TZHCDMC(String tT_TZHCDMC) {
		TT_TZHCDMC = tT_TZHCDMC;
	}

	public String getTT_QTSM() {
		return TT_QTSM;
	}

	public void setTT_QTSM(String tT_QTSM) {
		TT_QTSM = tT_QTSM;
	}

	public String getTT_SHZT() {
		return TT_SHZT;
	}

	public void setTT_SHZT(String tT_SHZT) {
		TT_SHZT = tT_SHZT;
	}

	public String getTT_CJR() {
		return TT_CJR;
	}

	public void setTT_CJR(String tT_CJR) {
		TT_CJR = tT_CJR;
	}

	public String getTT_CJSJ() {
		return TT_CJSJ;
	}

	public void setTT_CJSJ(String tT_CJSJ) {
		TT_CJSJ = tT_CJSJ;
	}

	public String getTT_ZT() {
		return TT_ZT;
	}

	public void setTT_ZT(String tT_ZT) {
		TT_ZT = tT_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}