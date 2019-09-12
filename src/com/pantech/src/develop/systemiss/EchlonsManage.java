package com.pantech.src.develop.systemiss;
/**
编制日期：2015.07.21
创建人：shenlei
模块名称：S1.13 层级管理
说明:
	 
功能索引:
	1-新建
	2-编辑
	3-查询
**/
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class EchlonsManage {
	private String EcheCode;//层级编号
	private String EcheName;//层级名称
	private String state;//状态
	private String AuthCode;
	private String AR_AuthCode[];
	private String MSG;//消息信息
	private HttpServletRequest request;//当前的request
	Vector vec = null;
	JSONArray jal=null;
	//构造器
	public EchlonsManage(HttpServletRequest request){
		this.request = request;
		this.initialData();
	}
	//初始化
	public void initialData(){
		AuthCode="";
		EcheCode="";
		EcheName="";
		state="";
		MSG="";
	
		
	}
	//新建按钮 
	public void saveRec() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="select count(*) from V_层级表   where 层级编号='"+MyTools.fixSql(this.getEcheCode())+"'";
		System.out.println(sql);
		if(db.getResultFromDB(sql)){
			this.setMSG("层级已存在");
		}else{
			String sqlsave="insert into [V_层级表] ("+
					"[层级编号]" +
					",[层级名称]" +
					",[状态])" +
					" VALUES" +
					"(" +
					"'C"+MyTools.fixSql(this.getEcheCode())+"'" +
					",'"+MyTools.fixSql(this.getEcheName())+"'" +
					",'"+MyTools.fixSql(this.getState())+"')";
			System.out.println(sqlsave);
			if(db.executeInsertOrUpdate(sqlsave)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	//下拉菜单
	public  JSONArray querycombox() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="select '' 层级编号,'全选' 层级名称,'1' 状态  union all select 层级编号, 层级名称,状态 from [V_层级表] where 状态='1'";
		jal=db.getConttexJONSArr(sql);
		return jal;
	}
	//编辑功能
	public void queryupdate() throws SQLException{
		DBSource db = new DBSource(request);
// 判定状态是否为“0” “0”为删除  “1”为修改
		if(MyTools.fixSql(this.getState()).equalsIgnoreCase("0")){
			System.out.println(MyTools.fixSql(this.getState()));
			String sql1="delete  from V_权限层级关系表  where 层级编号='C"+MyTools.fixSql(this.getEcheCode())+"'";
			db.executeInsertOrUpdate(sql1);
			this.setMSG("编辑成功");
		}else{
		String sql="update V_层级表 set 层级编号=" +
				"'C" +MyTools.fixSql(this.getEcheCode())+"', "+
				"层级名称='" +MyTools.fixSql(this.getEcheName())+"',状态='" +MyTools.fixSql(this.getState())
						+"' where 层级编号='"+
				MyTools.fixSql(request.getParameter("EcheCode1"))+
				"'";
		db.executeInsertOrUpdate(sql);
		String sql1= "select count(*) from V_权限层级关系表  where 层级编号='"+MyTools.fixSql(request.getParameter("EcheCode1"))+"'";
		if(db.getResultFromDB(sql1)){
			String sql2="update V_权限层级关系表  set 层级编号='C"+MyTools.fixSql(this.getEcheCode())+"' where 层级编号='"+MyTools.fixSql(request.getParameter("EcheCode1"))+"'";
			db.executeInsertOrUpdate(sql2);
		}
		this.setMSG("编辑成功");
		}
	}
	//权限列表加载左表
	public Vector queryload() throws SQLException{
		DBSource db = new DBSource(request);
		String sql1="select";
		String sql="SELECT [AuthCode],[state],[AuthDesc],[isBasePosition],[AuthPicture],[AuthType] FROM [sysAuthority] where  [AuthCode] not in (SELECT a.权限编号 FROM [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode WHERE a.状态=1)";
		vec=db.getConttexJONSArr(sql,0,0);
		System.out.println(vec);
		return vec;
	}
	//权限列表加载1右表
	public Vector queryload1() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="SELECT * FROM [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode WHERE a.层级编号='"+MyTools.fixSql(request.getParameter("EcheCode"))+"' and a.状态=1";
		vec=db.getConttexJONSArr(sql,0,0);
		System.out.println(vec);
		return vec;
	}
	//保存权限层级关系
	public void saveAuth() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="select * from V_权限层级关系表    where 权限编号='"+MyTools.fixSql(this.AuthCode)+"'";
		if(!db.getResultFromDB(sql)){
			String sql1="insert into V_权限层级关系表  ( [权限编号] ,[层级编号],[状态] ) values (" +
					"'"+MyTools.fixSql(this.getAuthCode())+"'" +
					",'"+MyTools.fixSql(this.getEcheCode())+"'" +
					",1)";
			db.executeInsertOrUpdate(sql1);
			this.setMSG("保存成功");
		}else{
			String sql2=" update V_权限层级关系表   set 层级编号='"+MyTools.fixSql(this.getEcheCode())+"', 状态=1 where 权限编号='"+MyTools.fixSql(this.getAuthCode())+"'";
			db.executeInsertOrUpdate(sql2);
			this.setMSG("保存成功");
		}
	
	
	
	}
	//加载层级/权限关系表
	public Vector loadlist() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="select 层级编号 as id,'' as _parentId,层级名称 as 名称 from [V_层级表] union all select a.[层级编号] as id,'' as _parentId, c.层级名称 as 名称 from [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode join [V_层级表] c on a.层级编号=c.层级编号 union select a.[权限编号] as id,c.层级编号 as _parentId,b.AuthDesc as 名称 FROM [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode join [V_层级表] c on a.层级编号=c.层级编号  where a.状态=1 and c.状态=1";                                                
		vec=db.getConttexJONSArr(sql,0,0);
		System.out.println(vec);
		return vec;
	}
	//删除
	public void del() throws SQLException{
		DBSource db = new DBSource(request);
		if(!MyTools.fixSql(this.getEcheCode()).equalsIgnoreCase("")){
			System.out.println("层级"+MyTools.fixSql(this.getEcheCode()));
			String sql1="delete  from V_权限层级关系表  where 层级编号='"+MyTools.fixSql(this.getEcheCode())+"'";
			db.executeInsertOrUpdate(sql1);
			this.setMSG("删除成功");
		}else{
			if(MyTools.fixSql(this.AuthCode)==""){
			this.setMSG("不能为空");
			return;
			}
			String sql="update [V_权限层级关系表] set 状态='0'  where 权限编号="+MyTools.fixSql(this.AuthCode);
			db.executeInsertOrUpdate(sql);
			this.setMSG("删除成功");
		}
	}
	//查询
	public Vector check() throws SQLException{
		DBSource db = new DBSource(request);
		String sql="";
		if(!(MyTools.fixSql(this.EcheCode)=="")){
			sql = " select * from (select a.[层级编号] as id,'' as _parentId, c.层级名称 as 名称 from [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode join [V_层级表] c on a.层级编号=c.层级编号 union select a.[权限编号] as id,c.层级编号 as _parentId,b.AuthDesc as 名称 FROM [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode join [V_层级表] c on a.层级编号=c.层级编号  where a.状态=1) a where a.id='"+MyTools.fixSql(this.EcheCode)+"' or _parentId='"+MyTools.fixSql(this.EcheCode)+"'" ;
		}else{
			sql="select 层级编号 as id,'' as _parentId,层级名称 as 名称 from [V_层级表] union all select a.[层级编号] as id,'' as _parentId, c.层级名称 as 名称 from [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode join [V_层级表] c on a.层级编号=c.层级编号 union select a.[权限编号] as id,c.层级编号 as _parentId,b.AuthDesc as 名称 FROM [V_权限层级关系表] a join [sysAuthority] b on a.权限编号=b.AuthCode join [V_层级表] c on a.层级编号=c.层级编号  where a.状态=1 and c.状态=1";
		}
		vec=db.getConttexJONSArr(sql,0,0);
		return vec;
	}

	public String getAuthCode() {
		return AuthCode;
	}
	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}
	public String getEcheCode() {
		return EcheCode;
	}
	public void setEcheCode(String echeCode) {
		EcheCode = echeCode;
	}
	public String getEcheName() {
		return EcheName;
	}
	public void setEcheName(String echeName) {
		EcheName = echeName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMSG() {
		return MSG;
	}
	public void setMSG(String mSG) {
		MSG = mSG;
	}	
}
