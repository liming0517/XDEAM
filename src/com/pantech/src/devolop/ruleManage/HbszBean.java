package com.pantech.src.devolop.ruleManage;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class HbszBean {
	private String GH_BH;//编号
	private String GH_XNXQBM;//学年学期编码
	private String GH_KCDM;//课程代码
	private String GH_KCMC;//课程名称
	//private String GH_XZBDM;//行政班代码
	private String GH_XZBDM[];//行政班代码
	private String GH_XZBMC;//行政班名称
	private String GH_ZT;//状态
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG; //提示信息
	private String MSG2; //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public HbszBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-06-03
	 * @author:wangzh
	 */
	public void initialData() {
		GH_BH = "";//编号
		GH_XNXQBM = "";//学年学期编码
		GH_KCDM = "";//课程代码
		GH_KCMC = "";//课程名称
		//GH_XZBDM = "";//行政班代码
		GH_XZBMC = "";//行政班名称
	    GH_ZT = "";//状态
		
		MSG = ""; //提示信息
	}
	
	public Vector queryRec(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句	
		String sql3= "";
		String sql4= "";
		Vector vec = null; // 结果集
		Vector vec3= null;
		Vector vec4= null;
		
		//sql = "select 编号,学年学期编码,课程代码,课程名称 as 学科名称,行政班代码,行政班名称 as 合班班级 from V_规则管理_合班表 where 1=1";
		sql="select 编号, 授课计划明细编号 from dbo.V_规则管理_合班表  ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			for(int i=0;i<vec.size();i=i+2){
				String bianhao = vec.get(i).toString();
				String skjhbh=vec.get(i+1).toString();
				String[] skjhbhid=skjhbh.split("\\+");
				String sql2= "";
				for(int j=0;j<skjhbhid.length;j++){
					if(j==0){
						sql2+="'"+skjhbhid[j]+"'";
					}else{
						sql2+=",'"+skjhbhid[j]+"'";
					}
				}
				
				sql3="select b.学年学期编码,a.课程名称,c.行政班名称 from dbo.V_规则管理_授课计划明细表 a " +
					"inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
					"inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 " +
					"where a.授课计划明细编号 in ("+sql2+") ";
				vec3=db.GetContextVector(sql3);
				if(vec3!=null&&vec3.size()>0){
					String coursename="";
					String classname="";
					for(int k=0;k<vec3.size();k=k+3){
						coursename+=vec3.get(k+1).toString()+"+";
						classname+=vec3.get(k+2).toString()+"+";						
					}
					coursename=coursename.substring(0, coursename.length()-1);
					classname=classname.substring(0, classname.length()-1);
					if(i==0){
						sql4+="select "+ bianhao +" as 编号,'"+ vec3.get(0).toString() +"' as 学年学期编码,'"+ coursename +"' as 学科名称,'"+ classname +"' as 合班班级 ";
					}else{
						sql4+=" union select "+ bianhao +" as 编号,'"+ vec3.get(0).toString() +"' as 学年学期编码,'"+ coursename +"' as 学科名称,'"+ classname +"' as 合班班级 ";
					}		
				}
								
//					sql3+=" union all "+
//						  "select "+ bianhao +" as 编号, d.学年学期编码,"+
//						  "STUFF((select '+'+e.课程代码 from (select b.学年学期编码,a.课程代码,a.课程名称,b.行政班代码,c.行政班名称 from dbo.V_规则管理_授课计划明细表 a "+
//						  "inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 "+
//						  "inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 "+
//						  "where a.授课计划明细编号 in ("+sql2+")) e where d.学年学期编码=e.学年学期编码 and d.课程代码=e.课程代码 and d.课程名称=e.课程名称 for xml path('')),1,1,'') as 课程代码,"+	
//						  "STUFF((select '+'+e.课程名称 from (select b.学年学期编码,a.课程代码,a.课程名称,b.行政班代码,c.行政班名称 from dbo.V_规则管理_授课计划明细表 a "+
//						  "inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 "+
//						  "inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 "+
//						  "where a.授课计划明细编号 in ("+sql2+")) e where d.学年学期编码=e.学年学期编码 and d.课程代码=e.课程代码 and d.课程名称=e.课程名称 for xml path('')),1,1,'') as 学科名称,"+
//						  "STUFF((select '+'+e.行政班代码 from (select b.学年学期编码,a.课程代码,a.课程名称,b.行政班代码,c.行政班名称 from dbo.V_规则管理_授课计划明细表 a "+
//						  "inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 "+
//						  "inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 "+
//						  "where a.授课计划明细编号 in ("+sql2+")) e where d.学年学期编码=e.学年学期编码 and d.课程代码=e.课程代码 and d.课程名称=e.课程名称 for xml path('')),1,1,'') as 行政班代码,"+
//						  "STUFF((select '+'+e.行政班名称 from (select b.学年学期编码,a.课程代码,a.课程名称,b.行政班代码,c.行政班名称 from dbo.V_规则管理_授课计划明细表 a "+
//						  "inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 "+
//						  "inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 "+
//						  "where a.授课计划明细编号 in ("+sql2+")) e where d.学年学期编码=e.学年学期编码 and d.课程代码=e.课程代码 and d.课程名称=e.课程名称 for xml path('')),1,1,'') as 合班班级 "+
//						  "from (select b.学年学期编码,a.课程代码,a.课程名称,b.行政班代码,c.行政班名称 from dbo.V_规则管理_授课计划明细表 a "+
//						  "inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 "+
//						  "inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 "+
//						  "where a.授课计划明细编号 in ("+sql2+")) d "+
//						  "group by d.学年学期编码";	
				
			}
		}else{
			sql4="select '' as 编号,b.学年学期编码,a.课程代码,a.课程名称,b.行政班代码,c.行政班名称 as 合班班级 from V_规则管理_授课计划明细表 a " +
					"inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
					"inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 " +
					"where 1=2";
		}
		sql4+=" order by 学年学期编码 desc,编号 desc ";
		vec4 = db.getConttexJONSArr(sql4, pageNum, page);// 带分页返回数据(json格式）
		return vec4;
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
		String sql = "select '请选择' as comboName,'' as comboValue,'1' as px " +
					 "union all " +
					 "select distinct 学年学期名称 AS comboValue,学年+学期 AS comboName,'2' as px FROM V_规则管理_学年学期表 where 1=1  ";
		sql=" select comboValue,comboName from ("+sql+") a order by px,comboValue desc ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
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
		String sql = "select '请选择' as comboName,'' as comboValue " +
					 "union all " +
					 "select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
					 "from V_基础信息_教学性质 where 1=1";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业名称下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadZYMCCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
					 "union all " +
					 "select distinct [系部名称] AS comboName,[系部代码] AS comboValue FROM V_基础信息_系部信息表 where 1=1 and [系部代码]!='C00' ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取班级名称下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKCMCCombo(String xnxq,String jxxz,String zydm,String classid) throws SQLException{
		Vector vec = null;		
//		if(zydm.equals("")){
//			
//		}else{
//			String[] zhuanye=zydm.split(",");
//			for(int i=0;i<zhuanye.length;i++){
//				if(!zhuanye[i].equals("")){
//					sql2+=" 课程号  like '"+ MyTools.fixSql(zhuanye[i]) +"%' or";
//				}
//			}	
//			if(!sql2.equals("")){
//				sql2=" and ("+sql2.substring(0, sql2.length()-2)+")";
//			}
//		}
		String classnum="";
		if(!classid.equals("")){
			String[] classbj=classid.split(",");
			for(int i=0;i<classbj.length;i++){
				classnum+="'"+classbj[i]+"',";
			}	
		}
		if(!classnum.equals("")){
			classnum=classnum.substring(0, classnum.length()-1);
		}else{
			classnum="''";
		}
		String sql = "select comboValue,comboName from (" +
					 "select '' as comboValue,'请选择' as comboName,'1' as px " +
					 "union all " +
					 "SELECT distinct a.课程代码 as comboValue,a.课程名称+' ['+a.课程代码+']' as comboName,'2' as px FROM [dbo].[V_规则管理_授课计划明细表] a left join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
					 " where b.学年学期编码='"+(xnxq+jxxz)+"' and b.行政班代码 in ("+classnum+") " +
					 ") x order by px,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取班级名称下拉框
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadBJMCCombo(String xnxq,String jxxz,String zydm) throws SQLException{
		Vector vec = null;
		String sql2="";
		String sql3="";
		String sql4="";

		if(xnxq.equals("")||jxxz.equals("")){
			
		}else{
			sql2+=" and b.学年学期编码='"+ MyTools.fixSql(xnxq+jxxz) +"' ";
		}
		
		String xb="";
		String[] xbdm=zydm.split(",");
		for(int i=0;i<xbdm.length;i++){
			xb+="'"+xbdm[i]+"',";
		}
		if(!xb.equals("")){
			xb=xb.substring(0, xb.length()-1);
		}
		
//		if(!kcdm.equals("")){
//			String[] kecheng=kcdm.split(",");
//			for(int i=0;i<kecheng.length;i++){
//				if(!kecheng[i].equals("")){
//					sql3+=" a.课程代码  like '"+ MyTools.fixSql(kecheng[i]) +"%' or";
//				}
//			}
//			if(!sql3.equals("")){
//				sql3=" and ("+sql3.substring(0, sql3.length()-2)+")";
//			}
//		}
		
		String sql = "select '' as comboValue,'请选择' as comboName " +
					 "union all " +
					 "select distinct b.行政班代码 as comboValue,c.行政班名称 as comboName from dbo.V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b,V_学校班级数据子类 c where a.授课计划主表编号=b.授课计划主表编号 and b.行政班代码=c.行政班代码  and c.系部代码 in ("+xb+") "+sql2;
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	public Vector queryShouke(String year, String xz, String xk, String bj) throws SQLException {
		String kcbmAll = "";
		String bjbmAll = "";
		String[] kcbm = new String[xk.split(",").length];
		kcbm = xk.split(",");
		for(int i=0; i<kcbm.length; i++) {
			kcbmAll += "'" + kcbm[i] + "', ";
		}
		String[] bjbm = new String[bj.split(",").length];
		bjbm = bj.split(",");
		for(int i=0; i<bjbm.length; i++) {
			bjbmAll += "'" + bjbm[i] + "', ";
		}
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 授课计划明细编号, c.行政班名称, 课程代码, 课程名称, 授课教师姓名, 节数, 连节, 连次, 场地名称, 授课周次" +
				" from dbo.V_规则管理_授课计划明细表 a" +
				" inner join dbo.V_规则管理_授课计划主表 b" +
				" on a.授课计划主表编号 = b.授课计划主表编号" +
				" inner join dbo.V_学校班级数据子类 c" +
				" on b.行政班代码 = c.行政班代码" +
				" where b.学年学期编码='"+ year+xz +"'" +
				" and b.行政班代码 in ("+  bjbmAll.substring(0, bjbmAll.length()-2) +")" +
				" and a.课程代码 in ("+  kcbmAll.substring(0, kcbmAll.length()-2) +")" ;
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		return vec;
	}
	
	public Vector queryShouke2(String year, String xz, String xk, String bj,String bianhao) throws SQLException {
		String kcbmAll = "";
		String bjbmAll = "";
		String skjhAll = "";
		String[] kcbm = new String[xk.split(",").length];
		kcbm = xk.split(",");
		for(int i=0; i<kcbm.length; i++) {
			kcbmAll += "'" + kcbm[i] + "', ";
		}
		String[] bjbm = new String[bj.split(",").length];
		bjbm = bj.split(",");
		for(int i=0; i<bjbm.length; i++) {
			bjbmAll += "'" + bjbm[i] + "', ";
		}
		String[] skjh=bianhao.split(",");
		for(int i=0;i<skjh.length;i++){
			skjhAll += "'" + skjh[i] + "', ";
		}
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		sql = "select 授课计划明细编号, c.行政班名称, 课程代码, 课程名称, 授课教师姓名, 节数, 连节, 连次, 场地名称, 授课周次" +
				" from dbo.V_规则管理_授课计划明细表 a" +
				" inner join dbo.V_规则管理_授课计划主表 b" +
				" on a.授课计划主表编号 = b.授课计划主表编号" +
				" inner join dbo.V_学校班级数据子类 c" +
				" on b.行政班代码 = c.行政班代码" +
				" where b.学年学期编码='"+ year+xz +"'" +
				" and b.行政班代码 in ("+  bjbmAll.substring(0, bjbmAll.length()-2) +")" +
				" and a.课程代码 in ("+  kcbmAll.substring(0, kcbmAll.length()-2) +")" +
				" and a.授课计划明细编号 in ("+  skjhAll.substring(0, skjhAll.length()-2) +")" ;
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public void saveRec(String kcdm,String bjdm,String jxxz,String bianhao) throws SQLException {
		String sql="";
		String a = ""; 
		String b = ""; 
		String[] banji=bjdm.split(",");
		for(int i=0; i<banji.length; i++) {
			if(i==0){
				a+=" '"+ banji[i] +"'";
			}else{
				a+=" ,'"+ banji[i] +"'";
			}
		}
		String[] kecheng=kcdm.split(",");
		for(int i=0;i<kecheng.length;i++){
			if(i==0){
				b+=" '"+ kecheng[i] +"'";
			}else{
				b+=" ,'"+ kecheng[i] +"'";
			}
		}
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String gpbj="";
		String kclx="";
		int tag=0;
		
		String sql3="SELECT distinct 课程类型 FROM V_规则管理_授课计划明细表 where 课程代码='"+ kecheng[0] +"' ";
		vec3=db.GetContextVector(sql3);
		if(vec3!=null&&vec3.size()>0){
			kclx=vec3.get(0).toString();
		}
		
		String shouke="";
		String[] skjhs = bianhao.split(",");
		for(int i=0;i<skjhs.length;i++){
			String sqlsg="select COUNT(*),行政班代码 from V_规则管理_固排禁排表 where 授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(skjhs[i])) + "' group by 行政班代码 ";
			vec2=db.GetContextVector(sqlsg);
			if(vec2!=null&&vec2.size()>0){//存在固排
				for(int z=0;z<vec2.size();z=z+2){
					gpbj+=vec2.get(z).toString()+",";
					tag=1;
				}	
			}else{//不存在固排
						
			}
			shouke+=skjhs[i]+"+";
		}
		if(!shouke.equals("")){
			shouke=shouke.substring(0,shouke.length()-1);
		}
			
		if(tag==1){
			this.setMSG("列表中的班级,有一个或者多个班级的该门课程已经进行过固排禁排的操作,不能进行合班操作,如果需要进行合班,则请删除固排禁排");
			this.setMSG2(gpbj);
		}else{
			sql = "select count(*) from V_规则管理_合班表 where 授课计划明细编号 = '"+ shouke +"'";
			if(!db.getResultFromDB(sql)) {
				sql = "insert into V_规则管理_合班表(授课计划明细编号,课程类型, 创建人, 创建时间, 状态) values('"+ shouke +"', '"+kclx+"', '"+ MyTools.getSessionUserCode(request) +"', getDate(), '1')";
				if(db.executeInsertOrUpdate(sql)) {
					setMSG("保存成功");
				}else {
					setMSG("保存失败");
				}
			}else {
				setMSG("该合班数据已存在, 不能进行重复保存操作");
			}
		}	
	}
		
		/**
		 * 
		 * @return
		 * @throws SQLException 
		 */
		public void delRec(String code) throws SQLException {
			Vector vec = null;
			String sql = "delete from V_规则管理_合班表 where 编号 = '"+ code +"'";
			if(db.executeInsertOrUpdate(sql)) {
				setMSG("删除成功");
			}else {
				setMSG("删除失败");
			}
		}
		
		/**
		 * 
		 * @return
		 * @throws SQLException 
		 */
		@SuppressWarnings("unchecked")
		public void changeShoukeRec(String mainCode, String otherCode) throws SQLException {
			String[] other = new String[otherCode.split(",").length];
			String otherAll = "";
			String sql = "";
			String classname="";
			Vector vec=null;
			Vector vecMain = new Vector();
			Vector vecOther = new Vector();
			other = otherCode.split(",");
			for(int i=0; i<other.length; i++) {
				otherAll += "'" + other[i] + "', ";
			}
			otherAll = otherAll.substring(0, otherAll.length()-2);
			sql = "select count(*),b.行政班名称 from dbo.V_规则管理_固排禁排表 a,V_学校班级数据子类 b where a.行政班代码=b.行政班代码 and a.授课计划明细编号 in ('"+ mainCode +"', "+ otherAll +") group by b.行政班名称";
			vec = db.GetContextVector(sql);
			if(vec!=null&&vec.size()>0) {
				for(int j=0;j<vec.size();j=j+2){
					if(vec.get(j).toString().equals("0")){
						
					}else{
						classname+=vec.get(j+1).toString()+"，";
					}
				}
				//classname=classname.substring(0,classname.length()-1);
				setMSG2(classname);
			}else {
				sql = "select 授课教师编号, 授课教师姓名, 节数, 连节, 连次, 场地要求, 场地名称, 授课周次, 授课周次详情 from dbo.V_规则管理_授课计划明细表 where 授课计划明细编号='"+ mainCode +"'";
				vecMain = db.GetContextVector(sql);
				//MyTools.PrintVector(vecMain);
				sql = "update V_规则管理_授课计划明细表 set 授课教师编号='"+vecMain.get(0)+"', 授课教师姓名='"+vecMain.get(1)+"', 节数='"+vecMain.get(2)+"', 连节='"+vecMain.get(3)+"', 连次='"+vecMain.get(4)+"', 场地要求='"+vecMain.get(5)+"', 场地名称='"+vecMain.get(6)+"', 授课周次='"+vecMain.get(7)+"', 授课周次详情='"+vecMain.get(8)+"' where 授课计划明细编号 in ("+ otherAll +")";
				vecOther.add(sql);
				//MyTools.PrintVector(vecOther);
				if(db.executeInsertOrUpdate(sql)) {
					setMSG("更新授课计划成功,现在可以点击保存进行合班操作");
					setMSG2("0");
				}else {
					setMSG("更新授课计划失败");
					setMSG2("0");
				}
			}
		}
	
	public String getGH_BH() {
		return GH_BH;
	}

	public void setGH_BH(String gH_BH) {
		GH_BH = gH_BH;
	}

	public String getGH_XNXQBM() {
		return GH_XNXQBM;
	}

	public void setGH_XNXQBM(String gH_XNXQBM) {
		GH_XNXQBM = gH_XNXQBM;
	}

	public String getGH_KCDM() {
		return GH_KCDM;
	}

	public void setGH_KCDM(String gH_KCDM) {
		GH_KCDM = gH_KCDM;
	}

	public String getGH_KCMC() {
		return GH_KCMC;
	}

	public void setGH_KCMC(String gH_KCMC) {
		GH_KCMC = gH_KCMC;
	}

//	public String getGH_XZBDM() {
//		return GH_XZBDM;
//	}
//
//	public void setGH_XZBDM(String gH_XZBDM) {
//		GH_XZBDM = gH_XZBDM;
//	}
	
	public String[] getGH_XZBDM() {
		return GH_XZBDM;
	}

	public void setGH_XZBDM(String gH_XZBDM[]) {
		GH_XZBDM = gH_XZBDM;
	}

	public String getGH_XZBMC() {
		return GH_XZBMC;
	}

	public void setGH_XZBMC(String gH_XZBMC) {
		GH_XZBMC = gH_XZBMC;
	}

	public String getGH_ZT() {
		return GH_ZT;
	}

	public void setGH_ZT(String gH_ZT) {
		GH_ZT = gH_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getMSG2() {
		return MSG2;
	}

	public void setMSG2(String mSG2) {
		MSG2 = mSG2;
	}
	
}
