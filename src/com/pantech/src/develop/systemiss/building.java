package com.pantech.src.develop.systemiss;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

import net.sf.json.JSONArray;

public class building {
	//校区
	private String XQDM;//校区代码
	private String XQMC;//校区名称
	private String XQDZ;//校区地址
	
	//建筑
	private String JZWH;//建筑号
	private String JZWMC;//建筑物名称
	private String XQH;//校区号
	
	//教室
	private String JSMC;//教室名称
	private String JSBH;//教室编号
	private String JSLX;//教师类型
	private String LXBH;//类型编号
	private String SFKY;//是否可用
	
	private String MSG;
	
	Vector vec = null;
	JSONArray jal=null;
	private HttpServletRequest request;
	//构造器
	public building(HttpServletRequest request){
		this.request = request;
		this.initialData();
	}
	//初始化
	public void initialData(){
		XQDM="";
		XQMC="";
		XQDZ="";
		JZWH="";
		JZWMC="";
		XQH="";
		JSMC="";
		JSBH="";
		MSG="";
		JSLX="";
		LXBH="";
	}
//加载表1
	public Vector loadlist1() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="";
		if(!(MyTools.fixSql(this.getXQDM()).equalsIgnoreCase(""))){
			sql="SELECT [XQDM],[XQMC],[XQDZ] FROM [ZZXX02] where [XQDM]='"+MyTools.fixSql(this.getXQDM())+"'";
		}else{
			sql="SELECT [XQDM],[XQMC],[XQDZ] FROM [ZZXX02]";
		}
		vec=db.getConttexJONSArr(sql,0,0);
		System.out.println(vec);
		return vec;
	}
//加载表2
	public Vector loadlist2() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="";
		System.out.println(MyTools.fixSql(this.getJZWH()));
		if(!MyTools.fixSql(this.getJZWH()).equalsIgnoreCase("")){
			sql="SELECT  [JZWH] ,[JZWMC] ,[XQH],[XQMC]  FROM [ZZFC02] a join [ZZXX02] b on a.XQH=b.XQDM where a.XQH='"+MyTools.fixSql(this.getXQH())+"' and [JZWH]='"+MyTools.fixSql(this.getJZWH())+"'";
		}else{
			sql="SELECT  [JZWH] ,[JZWMC] ,[XQH],[XQMC]  FROM [ZZFC02] a join [ZZXX02] b on a.XQH=b.XQDM where a.XQH='"+MyTools.fixSql(this.getXQH())+"'";
			
		}
		vec=db.getConttexJONSArr(sql,0,0);
		System.out.println(vec);
		return vec;
	}
//加载表3
	public Vector loadlist3() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="";
		if(!(MyTools.fixSql(this.getJSBH()).equalsIgnoreCase(""))){
			sql="SELECT  c.XQMC ,d.JZWMC ,[JSBH] ,[JSMC] ,b.JJ_MC,[SFKY],[JSLXDM]  FROM [ZZFC09] a join [JCXX_JSLX] b on a.JSLXDM=b.JJ_BH join [ZZXX02] c on a.XQDM=c.XQDM join [ZZFC02] d on d.JZWH=a.JZWH  where a.XQDM='"+MyTools.fixSql(this.getXQH())+"' and a.JZWH='"+MyTools.fixSql(this.getJZWH())+"' and [JSBH]='"+MyTools.fixSql(this.getJSBH())+"'";
		}else{
			sql="SELECT  c.XQMC ,d.JZWMC ,[JSBH] ,[JSMC] ,b.JJ_MC,[SFKY],[JSLXDM]  FROM [ZZFC09] a join [JCXX_JSLX] b on a.JSLXDM=b.JJ_BH join [ZZXX02] c on a.XQDM=c.XQDM join [ZZFC02] d on d.JZWH=a.JZWH where a.XQDM='"+MyTools.fixSql(this.getXQH())+"' and a.JZWH='"+MyTools.fixSql(this.getJZWH())+"'";
		}
		vec=db.getConttexJONSArr(sql,0,0);
		System.out.println(vec);
		return vec;
	}
//保存校区
	public void savexq() throws SQLException{
		DBSource db = new DBSource(request);
		System.out.println(MyTools.fixSql(this.getXQDM())+"123");
		String sql="select count(*) from [V_校区数据类] where 校区代码='"+MyTools.fixSql(this.getXQDM())+"' or 校区名称    like '"+MyTools.fixSql(this.getXQMC())+"'";
		if(!db.getResultFromDB(sql)){
			String sql1="insert into [V_校区数据类] ([校区代码],[校区名称],[校区地址],校区面积) values (" +
					"'"+MyTools.fixSql(this.getXQDM())+"'," +
							"'"+MyTools.fixSql(this.getXQMC())+"'," +
									"'"+MyTools.fixSql(this.getXQDZ())+"',1200)";
			db.executeInsertOrUpdate(sql1);
			System.out.println(sql1);
			this.setMSG("保存成功");
		}else{
			this.setMSG("校区已存在");
		}
	}
//校区下拉菜单
//	public JSONArray xqcombox() throws SQLException{
//		DBSource db = new DBSource(request);
//		String sql="select '' 校区代码,'请选择' 校区名称   union all select 校区代码, 校区名称     from [V_校区数据类]";
//		jal=db.getConttexJONSArr(sql);
//		return jal;
//	}
//保存建筑
	public void savejz() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="select count(*) from [V_建筑物基本数据类] where [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"'";
		String sql2="select count(*) from [V_建筑物基本数据类] where [建筑物名称] like '"+MyTools.fixSql(this.getJZWMC())+"' and [校区号]='"+MyTools.fixSql(this.getXQH())+"'";
		if(db.getResultFromDB(sql2)){
			this.setMSG("教学楼已存在");
			return;
		}
//判定建筑是否存在		
		if(!db.getResultFromDB(sql)){
			String sql1="insert into [V_建筑物基本数据类] ([建筑物号],[建筑物名称],[校区号],资产代码,使用状况码,房屋产权,建筑物分类码,建筑物结构码,建筑物层数,总建筑面积,总使用面积,抗震设防烈度码1,抗震设防标准码,建筑物地址,建筑物状况码,建筑物位置状况,规划审批文号,产权证号,是否有构造柱,圈梁" +
					",教学及辅助用房,其中教室,其中实验室,其中图书室,其中微机室,其中语音室,其中体育活动室,其中其他教辅用房,生活用房,其中学生宿舍,其中食堂,其中厕所,[其中锅炉房(开水房)],其中浴室,其中教工宿舍,其中其他生活用房,行政办公用房,其中教职工办公室,其中卫生保健室,其中其他行政办公用房,其他用房) values(" +
					"'"+MyTools.fixSql(this.getJZWH())+"'," +
							"'"+MyTools.fixSql(this.getJZWMC())+"'," +
									"'"+MyTools.fixSql(this.getXQH())+"','1','1','1','1','1',5,100.00,90.00,'1','1','1','1','2','1','1','1','1',1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)";
			db.executeInsertOrUpdate(sql1);
			this.setMSG("保存成功");
		}else{
			this.setMSG("建筑已存在");
		}
	}
//combobox建筑物
	public JSONArray jzcombox() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="select '' 建筑物号, '全部教学楼' 建筑物名称   union all select 建筑物号 , 建筑物名称   from [V_建筑物基本数据类] where 校区号='"+MyTools.fixSql(this.getXQH())+"'";
		jal=db.getConttexJONSArr(sql);
		return jal;
	}
//combobox教室类型
		public JSONArray lxcombox() throws SQLException{
			DBSource db = new DBSource(request);
			String sql="select '' 编号, '请选择' 名称   union all select 编号, 名称    from [V_基础信息_教室类型]";
			jal=db.getConttexJONSArr(sql);
			return jal;
		}
//保存教室
		public void savejs() throws SQLException{
			DBSource db = new DBSource(request);
//判定教室是否存在
			String sql="select count(*) from [V_教室数据类] WHERE [教室编号]='"+MyTools.fixSql(this.getJSBH())+"'and [校区代码]='"+MyTools.fixSql(this.getXQDM())+"'";			
			String sql2="select count(*) from [V_教室数据类] where [教室名称] like '"+MyTools.fixSql(this.getJSMC())+"' and [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"'";
			System.out.println("判定是否存在"+sql+sql2);
			if(db.getResultFromDB(sql2)){
				this.setMSG("教室已存在");
				return;
			}
			if(!db.getResultFromDB(sql)){
				String sql1="insert into [V_教室数据类] ([校区代码],[建筑物号],[教室编号],[教室名称],[教室类型代码],[实际容量],[最大排课容量],[网络状态],[是否多媒体教室],[是否可用]) values ('"
					+MyTools.fixSql(this.getXQDM())+
					"','"+MyTools.fixSql(this.getJZWH())+"'," +
								"'"+MyTools.fixSql(this.getJSBH())+"'," +
										"'"+MyTools.fixSql(this.getJSMC())+"'," +
												"'"+MyTools.fixSql(this.getLXBH())+"',50,50,'0','0'," +
														"'"+MyTools.fixSql(this.getSFKY())+"')";
				db.executeInsertOrUpdate(sql1);
				this.setMSG("保存成功");
			}else{
				this.setMSG("教室已存在");
			}
		}
		public void queryupdatejs() throws SQLException{
			DBSource db = new DBSource(request);
			String sql1="select count(*) from [V_教室数据类] where [教室编号]='"+MyTools.fixSql(this.getJSBH())+"'and [校区代码]='"+MyTools.fixSql(this.getXQDM())+"'";
			String sql2="select count(*) from [V_教室数据类] where [教室名称] like '"+MyTools.fixSql(this.getJSMC())+"' and [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"'";
//判定是否存在
			if(!(MyTools.fixSql(this.getJSBH()).equalsIgnoreCase(MyTools.fixSql(request.getParameter("numberjs"))))){
				if(db.getResultFromDB(sql1)){
					this.setMSG("编号已存在");
					return;
				}
			}
			if(!(MyTools.fixSql(this.getJSMC()).equalsIgnoreCase(MyTools.fixSql(request.getParameter("namejs"))))){
				if(db.getResultFromDB(sql2)){
					this.setMSG("教室名称已存在");
					return;
				}
			}					
			String sql="update [V_教室数据类] set [教室编号]='"+MyTools.fixSql(this.getJSBH())+"',[教室名称]='"+MyTools.fixSql(this.getJSMC())+"',[教室类型代码]='"+MyTools.fixSql(this.getLXBH())+"',[是否可用]='"+MyTools.fixSql(this.getSFKY())+"' where [教室编号]='"+request.getParameter("numberjs")+"'";
			System.out.println(sql);
			db.executeInsertOrUpdate(sql);
			this.setMSG("修改成功");
		}
		public void queryupdatejz() throws SQLException{
			DBSource db = new DBSource(request);
			String sql2="select count(*) from [V_建筑物基本数据类] where [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"' and [校区号]='"+MyTools.fixSql(this.getXQH())+"'";
			System.out.println(request.getParameter("numberjz"));
//判定是否存在
			if(!(MyTools.fixSql(this.getJZWH()).equalsIgnoreCase(request.getParameter("numberjz")))){
				if(db.getResultFromDB(sql2)){
					this.setMSG("教学楼已存在");
					return;
				}
			}
			String sql3="select count(*) from [V_建筑物基本数据类] where [建筑物名称]='"+MyTools.fixSql(this.getJZWMC())+"' and [校区号]='"+MyTools.fixSql(this.getXQH())+"'";
			if(!(MyTools.fixSql(this.getJZWMC()).equalsIgnoreCase(request.getParameter("namejz")))){
				if(db.getResultFromDB(sql3)){
					this.setMSG("教学楼名称已经存在");
					return;
				}
			}
			String sql = "update [V_建筑物基本数据类] set [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"' ,[建筑物名称]='"+MyTools.fixSql(this.getJZWMC())+"' where [建筑物号]='"+request.getParameter("numberjz")+"'" ;
			System.out.println(sql);
			String sql1= "update [V_教室数据类] set [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"' where [建筑物号]='"+request.getParameter("numberjz")+"'" ;
			System.out.println(sql1);
			db.executeInsertOrUpdate(sql1);
			db.executeInsertOrUpdate(sql);
			this.setMSG("修改成功");
		}
		public void queryupdatexq() throws SQLException{
			DBSource db = new DBSource(request);
//判定校区代码是否相同
			String sql="select count(*) from [V_校区数据类] where [校区代码]='"+MyTools.fixSql(this.getXQDM())+"'";
			if(db.getResultFromDB(sql)){
				if(!(MyTools.fixSql(this.getXQDM()).equalsIgnoreCase(request.getParameter("numberxq")))){
					this.setMSG("校区代码已经存在");
					return;
				}
			}
//判定校区名称是否相同
			String sql1="select count(*) from [V_校区数据类] where [校区名称]='"+MyTools.fixSql(this.getXQMC())+"'";
			if(!(MyTools.fixSql(this.getXQMC()).equalsIgnoreCase(request.getParameter("namexq")))){
				if(db.getResultFromDB(sql1)){
					this.setMSG("名称已经存在");
					return;
				}
			}
			String sql3="update [V_校区数据类] set [校区代码]='"+MyTools.fixSql(this.getXQDM())+"',[校区名称]='"+MyTools.fixSql(this.getXQMC())+"',[校区地址]='"+MyTools.fixSql(this.getXQDZ())+"' where [校区代码]='"+MyTools.fixSql(request.getParameter("numberxq"))+"'";
			String sql4="update [V_建筑物基本数据类] set [校区号]='"+MyTools.fixSql(this.getXQDM())+"' where [校区号]='"+MyTools.fixSql(request.getParameter("numberxq"))+"'";
			String sql5="update [V_教室数据类] set [校区代码]='"+MyTools.fixSql(this.getXQDM())+"' where [校区代码]='"+MyTools.fixSql(request.getParameter("numberxq"))+"'";
			db.executeInsertOrUpdate(sql5);
			db.executeInsertOrUpdate(sql4);
			db.executeInsertOrUpdate(sql3);
			this.setMSG("修改成功");
		}
		public JSONArray querycomboxxqmc() throws SQLException{
			DBSource db = new DBSource(request);
			String sql = "select '' 校区代码, '全部校区' 校区名称    union all select 校区代码,校区名称   from V_校区数据类" ;
			jal=db.getConttexJONSArr(sql);
			return jal;
		}
		public JSONArray querycomboxjsmc() throws SQLException{
			DBSource db = new DBSource(request);
			String sql = "select '' 教室编号, '全部教室' 教室名称    union all select 教室编号,教室名称   from [V_教室数据类] where [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"' and [校区代码]='"+MyTools.fixSql(this.getXQH())+"'" ;
			jal=db.getConttexJONSArr(sql);
			return jal;
		}
		
		public void deljs() throws SQLException{
			DBSource db = new DBSource(request);
//判定是否存在
			String sql1="select count(*) from [V_教室数据类]  where [校区代码]='"+MyTools.fixSql(this.getXQH())+"' and [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"' and [教室编号]='"+MyTools.fixSql(this.getJSBH())+"'" ;
			if(!(db.getResultFromDB(sql1))){
				this.setMSG("请选择教室");
				return;
			}
			String sql = "delete [V_教室数据类] where [校区代码]='"+MyTools.fixSql(this.getXQH())+"' and [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"' and [教室编号]='"+MyTools.fixSql(this.getJSBH())+"'" ;
			System.out.println(sql);
			db.executeInsertOrUpdate(sql);
			this.setMSG("删除成功");
		}
		public void deljz() throws SQLException{
			DBSource db = new DBSource(request);
			String sql = "delete [V_建筑物基本数据类] where [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"' and [校区号]='"+MyTools.fixSql(this.getXQH())+"'";
			String sql1=" delete [V_教室数据类] where [校区代码]='"+MyTools.fixSql(this.getXQH())+"' and  [建筑物号]='"+MyTools.fixSql(this.getJZWH())+"'";
			System.out.println(sql+"&"+sql1);
			db.executeInsertOrUpdate(sql);
			db.executeInsertOrUpdate(sql1);
			this.setMSG("删除成功");
		}
		public void delxq() throws SQLException{
			DBSource db = new DBSource(request);
			String sql3="select count(*) from [V_校区数据类] where [校区代码]='"+MyTools.fixSql(request.getParameter("numberxq1"))+"'" ;
			if(!(db.getResultFromDB(sql3))){
				this.setMSG("请选择校区");
				return;
			}
			String sql = "delete [V_校区数据类] where [校区代码]='"+MyTools.fixSql(request.getParameter("numberxq1"))+"'" ;
			String sql1 = "delete [V_建筑物基本数据类] where [校区号]='"+MyTools.fixSql(request.getParameter("numberxq1"))+"'" ;
			String sql2 = "delete [V_教室数据类] where [校区代码]='"+MyTools.fixSql(request.getParameter("numberxq1"))+"'" ;
			db.executeInsertOrUpdate(sql);
			db.executeInsertOrUpdate(sql1);
			db.executeInsertOrUpdate(sql2);
			this.setMSG("删除成功");
		}
		
		
		
		public String getXQDM() {
			return XQDM;
		}
		public void setXQDM(String xQDM) {
			XQDM = xQDM;
		}
		public String getXQMC() {
			return XQMC;
		}
		public void setXQMC(String xQMC) {
			XQMC = xQMC;
		}
		public String getXQDZ() {
			return XQDZ;
		}
		public void setXQDZ(String xQDZ) {
			XQDZ = xQDZ;
		}
		public String getJZWH() {
			return JZWH;
		}
		public void setJZWH(String jZWH) {
			JZWH = jZWH;
		}
		public String getJZWMC() {
			return JZWMC;
		}
		public void setJZWMC(String jZWMC) {
			JZWMC = jZWMC;
		}
		public String getXQH() {
			return XQH;
		}
		public void setXQH(String xQH) {
			XQH = xQH;
		}
		public String getJSMC() {
			return JSMC;
		}
		public void setJSMC(String jSMC) {
			JSMC = jSMC;
		}
		public String getJSBH() {
			return JSBH;
		}
		public void setJSBH(String jSBH) {
			JSBH = jSBH;
		}
		public String getJSLX() {
			return JSLX;
		}
		public void setJSLX(String jSLX) {
			JSLX = jSLX;
		}
		public String getLXBH() {
			return LXBH;
		}
		public void setLXBH(String lXBH) {
			LXBH = lXBH;
		}
		public String getMSG() {
			return MSG;
		}
		public void setMSG(String mSG) {
			MSG = mSG;
		}
		public String getSFKY() {
			return SFKY;
		}
		public void setSFKY(String sFKY) {
			SFKY = sFKY;
		}
		
	
	
	
	
}
