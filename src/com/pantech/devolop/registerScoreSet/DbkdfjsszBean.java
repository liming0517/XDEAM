package com.pantech.devolop.registerScoreSet;
/*
@date 2016.11.09
@author yeq
模块：M6.7 大补考登分教师设置
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class DbkdfjsszBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	private String CD_ID; //编号
	private String CD_LYLX; //来源类型
	private String CD_KCDM; //课程代码
	private String CD_KCMC; //课程名称
	private String CD_DFJSBH; //登分教师编号
	private String CD_DFJSXM; //登分教师姓名
	private String CD_CJR; //创建人
	private String CD_CJSJ; //创建时间
	private String CD_ZT; //状态

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public DbkdfjsszBean(HttpServletRequest request) {
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
		CD_LYLX = ""; //来源类型
		CD_KCDM = ""; //课程代码
		CD_KCMC = ""; //课程名称
		CD_DFJSBH = ""; //登分教师编号
		CD_DFJSXM = ""; //登分教师姓名
		CD_CJR = ""; //创建人
		CD_CJSJ = ""; //创建时间
		CD_ZT = ""; //状态
	}

	/**
	 * 分页查询 课程列表
	 * @date:2016-11-09
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param DBKXN_CX 大补考
	 * @param JXXZ_CX 教学性质
	 * @param ZYMC_CX 专业名称
	 * @param XZBMC_CX 行政班名称
	 * @param KCMC_CX 课程名称
	 * @param KCLX_CX 课程类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queCourseList(int pageNum, int pageSize, String DBKXN_CX, String ZYMC_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select a.编号,a.大补考学年,b.专业名称,a.课程名称," +
			"case a.来源类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as 课程类型," +
			"replace(a.登分教师编号,'@','') as 登分教师编号,replace(a.登分教师姓名,'@','') as 登分教师姓名 " +
			"from V_成绩管理_大补考登分教师信息表 a " +
			"left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
			"left join V_学校班级数据子类 c on c.行政班代码=a.专业代码 " +
			"left join V_基础信息_教学班信息表 d on d.教学班编号=a.专业代码 " +
			"where 1=1";
		
		//判断权限
		if(this.getAUTH().indexOf(admin) < 0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"d.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			
			if(this.getAUTH().indexOf(majorTeacher) > -1){
				sql += " or b.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
			}
			sql += ")";
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(DBKXN_CX)){
			sql += " and a.大补考学年='" + MyTools.fixSql(DBKXN_CX) + "'";
		}
		if(!"".equalsIgnoreCase(ZYMC_CX)){
			sql += " and b.专业名称 like '%" + MyTools.fixSql(ZYMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCLX_CX)){
			sql += " and a.来源类型='" + MyTools.fixSql(KCLX_CX) + "'";
		}
		sql += " order by a.课程名称,b.专业名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取当前学年
	 * @date:2016-11-03
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadCurXn() throws SQLException{
		Vector vec = null;
		String curXnxq = "";
		String sql = "select top 1 学年 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		return curXnxq;
	}
	
	/**
	 * 读取学年下拉框
	 * @date:2016-11-03
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct 学年 as comboName,学年 as comboValue from V_规则管理_学年学期表 order by comboValue desc";
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
		String sql = "update V_成绩管理_大补考登分教师信息表 set " +
				"登分教师编号='" + MyTools.fixSql("@"+this.getCD_DFJSBH().replaceAll(",","@,@")+"@") + "'," +
				"登分教师姓名='" + MyTools.fixSql("@"+this.getCD_DFJSXM().replaceAll(",","@,@")+"@") + "' " +
				"where 编号='" + MyTools.fixSql(this.getCD_ID()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
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

	public String getCD_LYLX() {
		return CD_LYLX;
	}

	public void setCD_LYLX(String cD_LYLX) {
		CD_LYLX = cD_LYLX;
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

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}