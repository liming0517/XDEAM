package com.pantech.src.develop.systemiss;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

import net.sf.json.JSONArray;

public class Teamleaderbean {
		private String XQDM;
		private String ZYDM;
		private String JSDM;
		private String xq1;
		private String tyzy;
		private String tyzz1;
		
		private String MSG;
		Vector jsonV = null;
		JSONArray jal=null;
		private HttpServletRequest request;
		//构造器
		public Teamleaderbean(HttpServletRequest request){
			this.request = request;
			this.initialData();
		}
		public void initialData(){
			XQDM="";
			ZYDM="";
			JSDM="";
		}
		public Vector querycomboxxqmc() throws SQLException{
			DBSource db = new DBSource(request);
			Vector vector = null;
			String sql = "select '' as comboValue, '请选择' as comboName  union all select 校区代码 as comboValue,校区名称  as comboName  from V_校区数据类  " ;
			vector = db.getConttexJONSArr(sql, 0, 0);
			return vector;
		}
		public Vector querycomboxjsmc() throws SQLException{
			DBSource db = new DBSource(request);
			Vector vector = null;
			String sql = "select '' as  comboValue, '请选择' as comboName,0 as orderNum " +
					"union all " +
					"select 工号 as comboValue,姓名  as comboName,1 from V_教职工基本数据子类   where len(工号)=10 order by orderNum,comboName" ;
			vector = db.getConttexJONSArr(sql, 0, 0);
			return vector;
		}
		public Vector querycomboxzy() throws SQLException{
			DBSource db = new DBSource(request);
			Vector vector = null;
			String sql = "select '' as  comboValue,'请选择' as comboName union all select 专业代码 as comboValue,专业名称+专业代码 as comboName from V_专业基本信息数据子类 where len(专业代码)!=3" ;
			vector = db.getConttexJONSArr(sql, 0, 0);
			return vector;
		}
		public void savezuzhang(String JSMC) throws SQLException{
			DBSource db = new DBSource(request);
			String jsdm="";
			String jsmc="";
			jsdm="@"+MyTools.StrFiltr(this.getJSDM()).replaceAll(",", "@,@")+"@";
			jsmc="@"+MyTools.StrFiltr(JSMC).replaceAll(",", "@,@")+"@";
			
			String sql ="SELECT  count(*) FROM V_专业组组长信息 where [校区代码]='"+MyTools.fixSql(this.getXQDM())+"' and [专业代码]='"+MyTools.fixSql(this.getZYDM())+"'";
			if(!db.getResultFromDB(sql)){
				String sql1="insert into V_专业组组长信息     (校区代码,专业代码,教师代码,教师姓名) values ('"+MyTools.fixSql(this.getXQDM())+"','"+MyTools.fixSql(this.getZYDM())+"','"+MyTools.fixSql(jsdm)+"','"+MyTools.fixSql(jsmc)+"')";
				db.executeInsertOrUpdate(sql1);
				this.setMSG("保存成功");
			}else{
				this.setMSG("专业已存在 保存失败");
			}
				
		}
		public JSONArray querycomboxxqmcd() throws SQLException{
			DBSource db = new DBSource(request);
			String sql="SELECT '' [校区代码],'请选择' [校区名称] UNION ALL SELECT [校区代码],[校区名称] FROM [V_校区数据类]";
			jal=db.getConttexJONSArr(sql);
			return jal;
		}
		public JSONArray querycomboxjsmcd() throws SQLException{
			DBSource db = new DBSource(request);
			String sql = "select '' 工号, '教师名称' 姓名   union all select 工号,姓名     from V_教职工基本数据子类  where len(工号)=10 " ;
			jal=db.getConttexJONSArr(sql);
			return jal;
		}
		public JSONArray querylist() throws SQLException{
			DBSource db = new DBSource(request);
			
			String sql="SELECT a.校区代码,b.校区名称,c.专业名称,a.专业代码,d.姓名,a.教师代码 FROM [V_专业组组长信息] a join V_校区数据类 b on a.校区代码=b.校区代码 join [V_专业基本信息数据子类] c on a.专业代码=c.专业代码 join [V_教职工基本数据子类] d on a.教师代码=d.工号";
			jal=db.getConttexJONSArr(sql);
			return jal;
		}
		public void queryupdate(String JSMC) throws SQLException{
			DBSource db = new DBSource(request);
			String jsdm="";
			String jsmc="";
			if(!"".equalsIgnoreCase(MyTools.StrFiltr(this.getJSDM()))){
				jsdm="@"+this.getJSDM().replaceAll(",", "@,@")+"@";
			}
			if(!"请选择".equalsIgnoreCase(JSMC)){
				jsmc="@"+JSMC.replaceAll(",", "@,@")+"@";
			}
			
			String sql="update  [V_专业组组长信息] set 教师代码='"+MyTools.fixSql(jsdm)+"',教师姓名='"+MyTools.fixSql(jsmc)+"'" +
					" where 校区代码='"+MyTools.fixSql(this.getXq1())+"' " +
					" and 专业代码='"+MyTools.fixSql(this.getTyzy())+"' " ;
				if(db.executeInsertOrUpdate(sql)){
					this.setMSG("编辑成功");
				}else{
					this.setMSG("编辑失败");
				}
			/*if(db.getResultFromDB(sql1)){
				String sql="update  [V_专业组组长信息] set 教师代码='"+MyTools.fixSql(jsdm)+"',教师姓名='"+MyTools.fixSql(jsmc)+"'" +
						" where 校区代码='"+MyTools.fixSql(this.getXq1())+"' " +
						" and 专业代码='"+MyTools.fixSql(this.getTyzy())+"' " ;
					if(db.executeInsertOrUpdate(sql)){
						this.setMSG("编辑成功");
					}else{
						this.setMSG("编辑失败");
					}
			}else{
				String sql="insert into V_专业组组长信息 (校区代码,专业代码,教师代码,教师姓名) values('"+MyTools.fixSql(this.getXQDM())+"'," +
						"'"+MyTools.fixSql(this.getZYDM())+"'," +
						"'"+MyTools.fixSql(jsdm)+"'," +
						"'"+MyTools.fixSql(jsmc)+"')";
				if(db.executeInsertOrUpdate(sql)){
					this.setMSG("保存成功");
				}else{
					this.setMSG("新增失败");
				}
			}*/
		}
		
		public  Vector query(String  queryjs) throws SQLException{
			DBSource db = new DBSource(request);
			//String sql="SELECT a.XQDM,b.XQMC,c.ZYMC+a.ZYDM as ZYMC,a.ZYDM,d.XM,a.JSDM FROM Professionalteamleader a join ZZXX02 b on a.XQDM=b.XQDM join ZZJX0101 c on a.ZYDM=c.ZYDM join ZZJG0101 d on a.JSDM=d.GH where 1=1  ";
			String sql="select a.校区代码  as XQDM,b.校区名称 as XQMC,a.专业代码 as ZYDM,c.专业名称 as ZYMC ,replace(a.教师姓名,'@','') as XM,replace(a.教师代码,'@','') as JSDM  " +
					"from V_专业组组长信息 a " +
					"left join V_校区数据类 b on a.校区代码=b.校区代码 " +
					"left join  V_专业基本信息数据子类 c on a.专业代码=c.专业代码 " +
					"where 1=1";
			if(!(MyTools.fixSql(request.getParameter("queryxq"))=="")){
				//sql+=" and a.XQDM='"+MyTools.fixSql(request.getParameter("queryxq"))+"' ";
				sql+=" and a.校区代码='"+MyTools.fixSql(request.getParameter("queryxq"))+"' ";
			}
			if(!"".equalsIgnoreCase(MyTools.StrFiltr(queryjs))){
				//sql+=" and a.JSDM='"+MyTools.fixSql(request.getParameter("queryjs"))+"'";
				sql+=" and a.教师姓名 like'%"+MyTools.fixSql(queryjs)+"%'";
			}	
			sql +="order by c.专业名称";
		    jsonV=db.getConttexJONSArr(sql,0,0);
			this.setMSG("查询");
			return jsonV;
		}
		public void del() throws SQLException{
			DBSource db = new DBSource(request);
			String jsdm="";
			if(!"".equalsIgnoreCase(MyTools.StrFiltr(this.getJSDM()))){
				jsdm="@"+this.getJSDM().replaceAll(",", "@,@")+"@";
			}
			String sql = "delete [V_专业组组长信息] where 校区代码='"+MyTools.fixSql(this.getXQDM())+"' and 专业代码='"+MyTools.fixSql(this.getZYDM())+"' and 教师代码='"+MyTools.fixSql(jsdm)+"'";
			db.executeInsertOrUpdate(sql);
			this.setMSG("删除成功");
		}
		
		public void importTeacher(String teaName) throws SQLException{
			DBSource db = new DBSource(request);
			String sql1="select count(*) from V_教职工基本数据子类 where 姓名='"+MyTools.fixSql(teaName)+"'";
			if(!db.getResultFromDB(sql1)){
				String sql = "insert into V_教职工基本数据子类 (工号,姓名,是否有效) " +
						"select GH as 工号,XM as 姓名,DQZTM as 是否有效 from openquery(myserver,'SELECT * FROM v_js_jzg_jw  where XM=\""+teaName+"\" ') where GH not in (select 工号 from V_教职工基本数据子类) ";

					sql+="insert into sysUserinfo ([UserName],[UserCode],[Password],[state],[email]) " +
						"select XM as [UserName],Gh as [UserCode],'MTExMTEx' as [Password],'Y' as [state],'abc@qq.com' as [email] from openquery(myserver,'SELECT * FROM v_js_jzg_jw  XM=\""+teaName+"\" ') where GH not in (select UserCode from sysUserinfo) ";

					sql+="insert into sysUserAuth ([UserCode],[AuthCode],[state]) " +
						"select GH as [UserCode],'90' as [AuthCode],'Y' as [state] from openquery(myserver,'SELECT * FROM v_js_jzg_jw  XM=\""+teaName+"\" ') where GH not in (select UserCode from sysUserAuth) ";
				
				if(db.executeInsertOrUpdate(sql)){
					this.setMSG("导入成功");
				}else{
					this.setMSG("导入失败");
				}
			}else{
				this.setMSG("教师已存在不可导入");
			}
			
			
		}
		
		public String getXQDM() {
			return XQDM;
		}

		public void setXQDM(String xQDM) {
			XQDM = xQDM;
		}

		public String getZYDM() {
			return ZYDM;
		}

		public void setZYDM(String zYDM) {
			ZYDM = zYDM;
		}

		public String getJSDM() {
			return JSDM;
		}

		public void setJSDM(String jSDM) {
			JSDM = jSDM;
		}

		public String getMSG() {
			return MSG;
		}

		public void setMSG(String mSG) {
			MSG = mSG;
		}
		public String getXq1() {
			return xq1;
		}
		public void setXq1(String xq1) {
			this.xq1 = xq1;
		}
		public String getTyzy() {
			return tyzy;
		}
		public void setTyzy(String tyzy) {
			this.tyzy = tyzy;
		}
		public String getTyzz1() {
			return tyzz1;
		}
		public void setTyzz1(String tyzz1) {
			this.tyzz1 = tyzz1;
		}
		
		
}
