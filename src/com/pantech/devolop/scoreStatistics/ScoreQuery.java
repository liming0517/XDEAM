package com.pantech.devolop.scoreStatistics;
/*
@date 2015.09.11
@author lupengfei
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class ScoreQuery {
	private String iUSERCODE;//用户编号
	private String XNXQBM;//学年学期编码
	private String XZBDM;//行政班代码
	private String XZBMC;//行政班名称
	private String sAuth;//权限
	private String ZYDM;//专业代码
	private String XH;//学号
	private String XM;//姓名
	//private int stunum;//班级学生数量
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息


	/**
	 * 构造函数
	 * @param request
	 */
	public ScoreQuery(HttpServletRequest request) {
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
		iUSERCODE="";
		XNXQBM = "";//学年学期编码
		XZBDM = "";//行政班代码
		XZBMC = "";//行政班名称
		MSG = ""; //提示信息
		sAuth="";
		ZYDM="";
		XH="";
		XM="";
	}
	
	//学年学期combobox
	public Vector XNXQCombobox() throws SQLException{
		Vector vec = null;
		
		String sql = "select '99' AS comboValue,'请选择' AS comboName union select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		//String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		
		//editTime：20150730，editor：lupengfei，description：需要获取学年学期编码
		//String sql = "select distinct 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		System.out.println("学年+学期:"+vec.get(0).toString());
		
		return vec;
	}
	
	/**
	 * @author 2016-7-7
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */

	public Vector ZYDMCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		sql = " select '' as comboValue, '请选择' as comboName,'1' as px union select [专业代码] as comboValue, [专业名称]+[专业代码] as comboName,'2' as px from dbo.V_专业基本信息数据子类 where len([专业代码])!=3 order by px,comboName ";			
			
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * @author 2015-9-11
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */

	public Vector XZBDMCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		String sql2="";
		if(this.getiUSERCODE().equals("post")){//管理员
			sql2="";
		}else if(this.getsAuth().equals("99")){//学生
			sql2=" where 行政班代码 in (select [行政班代码] from dbo.V_学生基本数据子类 where 学号='"+this.getiUSERCODE()+"' )";
		}else{
			sql2=" where 班主任工号='"+this.getiUSERCODE()+"'";
		}
		sql = " select '' as comboValue, '请选择' as comboName,'1' as px union select 行政班代码 as comboValue, 行政班名称 as comboName,'2' as px from [dbo].[V_学校班级数据子类] "+sql2+" order by px,comboName ";			
			
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	/**
	 * 查询班级下的学生
	 * @author 2016-7-6
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */

	public Vector loadStudent(int pageNum, int pageSize) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		String sql2="";
		
		sql = " select a.行政班代码, b.行政班名称, a.学号 as xh, a.姓名 as xm from dbo.V_学生基本数据子类 a,dbo.V_学校班级数据子类 b where a.行政班代码=b.行政班代码  ";			
		if(!this.getZYDM().equals("")){
			sql2+=" and b.专业代码='"+MyTools.fixSql(this.getZYDM())+"' ";
		}
		if(!this.getXZBDM().equals("")){
			sql2+=" and a.行政班代码='"+MyTools.fixSql(this.getXZBDM())+"' ";
		}
		if(!this.getXH().equals("")){
			sql2+=" and a.学号 like '%"+MyTools.fixSql(this.getXH())+"%' ";
		}
		if(!this.getXM().equals("")){
			sql2+=" and a.姓名 like '%"+MyTools.fixSql(this.getXM())+"%' ";
		}
		sql=sql+sql2+" order by a.学号 ";
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	/**
	 * 查询学生成绩
	 * @author 2016-7-6
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadScore(int pageNum, int pageSize) throws SQLException {
		DBSource db = new DBSource(request);
		Vector vec = null;
		Vector vecwz = null;
		Vector vec4 = null;
		String sql = "";
		String sql2="";
		String sql3="";
		String sql4="";
		
		if(this.getXNXQBM().equals("99")){
			sql2="";
		}else{
			sql2=" and b.学年学期编码='"+this.getXNXQBM()+"'";
		}
		sql = " SELECT [学号],[姓名],b.学年学期编码,b.课程名称,[相关编号],[总评],[重修1],[重修2],[补考],[大补考] " +
			" FROM [dbo].[V_成绩管理_学生成绩信息表] a,[dbo].V_成绩管理_科目课程信息表 b " +
			" where a.科目编号=b.科目编号 and a.学号='"+this.getiUSERCODE()+"'"+sql2+" order by b.学年学期编码 desc ";			
			
		vec = db.GetContextVector(sql);

		sql3="select [代码],[名称] from [dbo].[V_成绩管理_文字成绩代码表] where [状态]='1' ";
		vecwz=db.GetContextVector(sql3);
		if(vecwz!=null&&vecwz.size()>0){
			for(int v=0;v<vec.size();v=v+10){
				//System.out.println(vec.get(v+5).toString().indexOf("-"));
				if(vec.get(v+5).toString().indexOf("-")>-1){//负数代码，转换成中文 5
					for(int i=0;i<vecwz.size();i=i+2){
						if(vec.get(v+5).toString().equals(vecwz.get(i).toString())){//代号相同
							vec.setElementAt(vecwz.get(i+1).toString(), v+5);
						}
					}
				}
				if(vec.get(v+6).toString().indexOf("-")>-1){//负数代码，转换成中文 6
					for(int i=0;i<vecwz.size();i=i+2){
						if(vec.get(v+6).toString().equals(vecwz.get(i).toString())){//代号相同
							vec.setElementAt(vecwz.get(i+1).toString(), v+6);
						}
					}
				}
				if(vec.get(v+7).toString().indexOf("-")>-1){//负数代码，转换成中文 7
					for(int i=0;i<vecwz.size();i=i+2){
						if(vec.get(v+7).toString().equals(vecwz.get(i).toString())){//代号相同
							vec.setElementAt(vecwz.get(i+1).toString(), v+7);
						}
					}
				}
				if(vec.get(v+8).toString().indexOf("-")>-1){//负数代码，转换成中文 8
					for(int i=0;i<vecwz.size();i=i+2){
						if(vec.get(v+8).toString().equals(vecwz.get(i).toString())){//代号相同
							vec.setElementAt(vecwz.get(i+1).toString(), v+8);
						}
					}
				}
				if(vec.get(v+9).toString().indexOf("-")>-1){//负数代码，转换成中文 9
					for(int i=0;i<vecwz.size();i=i+2){
						if(vec.get(v+9).toString().equals(vecwz.get(i).toString())){//代号相同
							vec.setElementAt(vecwz.get(i+1).toString(), v+9);
						}
					}
				}
			}
		}
		for(int v=0;v<vec.size();v=v+10){
			sql4+="select '"+vec.get(v).toString()+"' as 学号,'"+vec.get(v+1).toString()+"' as 姓名,'"+vec.get(v+2).toString()+"' as 学年学期编码,'"+vec.get(v+3).toString()+"' as 课程名称,'"+vec.get(v+4).toString()+"' as 相关编号," +
				"'"+vec.get(v+5).toString()+"' as 总评,'"+vec.get(v+6).toString()+"' as 重修1,'"+vec.get(v+7).toString()+"' as 重修2,'"+vec.get(v+8).toString()+"' as 补考,'"+vec.get(v+9).toString()+"' as 大补考 union ";
		}
		sql4=sql4.substring(0, sql4.length()-6);
		sql4="select * from ("+sql4+") a order by 学年学期编码 desc ";
		vec4=db.getConttexJONSArr(sql4, 0, 0);
		return vec4;
//		{"total":0,"rows":[{"行政班代码":"1550401","行政班名称":"15级学前教育（幼儿保健）1班","xh":"15504001","xm":"解菁云"},{"行政班代码":"1"
	}
	

	public String getXNXQBM() {
		return XNXQBM;
	}

	public void setXNXQBM(String xNXQBM) {
		XNXQBM = xNXQBM;
	}

	public String getXZBDM() {
		return XZBDM;
	}

	public void setXZBDM(String xZBDM) {
		XZBDM = xZBDM;
	}

	public String getXZBMC() {
		return XZBMC;
	}

	public void setXZBMC(String xZBMC) {
		XZBMC = xZBMC;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getiUSERCODE() {
		return iUSERCODE;
	}

	public void setiUSERCODE(String iUSERCODE) {
		this.iUSERCODE = iUSERCODE;
	}

	public String getsAuth() {
		return sAuth;
	}

	public void setsAuth(String sAuth) {
		this.sAuth = sAuth;
	}

	public String getZYDM() {
		return ZYDM;
	}

	public void setZYDM(String zYDM) {
		ZYDM = zYDM;
	}

	public String getXH() {
		return XH;
	}

	public void setXH(String xH) {
		XH = xH;
	}

	public String getXM() {
		return XM;
	}

	public void setXM(String xM) {
		XM = xM;
	}
	
	
	
	
	
}