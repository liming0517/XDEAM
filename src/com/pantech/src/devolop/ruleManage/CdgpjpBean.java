package com.pantech.src.devolop.ruleManage;
/*
@date 2015.06.02
@author wangzh
模块：M1.2授课计划
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class CdgpjpBean {
	private String GG_BH;//编号
	private String GG_LX;//类型
	private String GG_XNXQBM;//学年学期编码
	private String GG_XZBDM;//行政班代码
	private String GG_SJXL;//时间序列
	private String GG_SKJHMXBH;//授课计划明细编号
	private String GG_LJXGBH;//连节相关编号
	private String GG_ZT;//状态
	private String SKJHMXBH;//授课计划明细编号
	private String XZBDM;//行政班代码
	private String XNXQ;//学年学期
	private String JXXZ;//教学性质
	private String KCJS;//课程教师
	private String QCXX;//清除的信息
	private String JSBH;//教师编号
	private String JSXM;//教师姓名
	
	private HttpServletRequest request;
	private DBSource db;
	private String iUSERCODE;//用户编号
	private String sAuth;//用户权限
	private String MSG;  //提示信息
	private String MSG2;  //提示信息
	private String MSG3;  //提示信息
	private String MSG4;  //提示信息
	private String MSG5;  //提示信息
	private String LCS;  //已排连次次数
	private String AOD;  //判断添加或删除
	
	/**
	 * 构造函数
	 * @param request
	 */
	public CdgpjpBean(HttpServletRequest request) {
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
		GG_BH = "";//编号
		GG_LX = "";//类型
		GG_XNXQBM = "";//学年学期编码
		GG_XZBDM = "";//行政班代码
		GG_SJXL = "";//时间序列
		GG_SKJHMXBH = "";//授课计划明细编号
        GG_LJXGBH = "";//连节相关编号
        GG_ZT = "";//状态
        SKJHMXBH = "";//授课计划明细编号
		XZBDM = "";//行政班代码
		XNXQ = "";//学年学期
		JXXZ = "";//教学性质
		KCJS = "";//课程教师
		QCXX = "";//清除的信息
		MSG = ""; //提示信息
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

		String[] auth=this.getsAuth().split(",");
		String sAuthCode="";
		for(int i=0;i<auth.length;i++){
			sAuthCode+="@"+auth[i]+"@,";
		}
		if(!sAuthCode.equals("")){
			sAuthCode=sAuthCode.substring(0, sAuthCode.length()-1);
		}

		if(level.equals("0")){
			sql = "select 系部代码 as id,系部名称 as text,'closed' as state from V_基础信息_系部信息表 where 系部代码<>'C00'";
			if((sAuthCode).indexOf(adminAuth)<0 && (sAuthCode).indexOf(qxjwgl)<0){
				sql += " and 系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getiUSERCODE()) + "')";
			}
		}
		if(level.equals("1")){
			sql = "select [教室编号] as id,[教室名称] as text,'open' as state from V_教室数据类  where 校区代码='" + MyTools.fixSql(parentId) + "' order by text";
		}

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
		String sql2= "";
		String sql3= "";
		Vector vec = null;
		Vector vec3 = null;
		
		String skjhbh=null;//授课计划明细编号
		String xnxqbm=null;//学年学期编码
		String skjsbh=null;//授课教师编号
		String skjsxm=null;//授课教师姓名
		String skzbbh=null;//授课计划主表编号
		String kechid=null;//课程代码
		String kechna=null;//课程名称
		String clasid=null;//行政班代码
		String clasna=null;//行政班名称
		String sjcdyq=null;//场地要求
		String sjcdmc=null;//场地名称
		String skzcmx=null;//授课周次
		String skzcxq=null;//授课周次详情
		String skgsjs=null;//节数
		String skgslj=null;//连节
		String skypjs=null;//固排已排节数
		String skmpjs=null;//未排节数
		String skgslc=null;//连次
		String skkclx=null;//课程类型
		String sqlpkgl="";
		String sqlpkgl4="";
		String sqlpkgl3="";
		int a=0;
		int b=0;
		int c=0;
		int d=0;
		
		//通过教师编号查询所有有该教师的授课计划明细,有'&'连接的分割开，去掉没有该教师的部分再合成一条数据
		if(this.JSBH.equalsIgnoreCase("")){
			sql="select * from V_规则管理_授课计划明细表 where 1=2 ";
		}else{		
			sql="SELECT a.授课计划明细编号,b.学年学期编码,a.授课教师编号,a.授课教师姓名,b.授课计划主表编号,a.课程代码 ,a.课程名称,b.行政班代码,c.班级名称,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情,a.节数,a.连节,a.固排已排节数,(cast(a.节数 as int)-cast(a.固排已排节数 as int)) as 未排节数,a.连次,课程类型 " +
				" FROM V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b,dbo.V_基础信息_班级信息表 c " +
				" where a.授课计划明细编号 != '' and a.授课计划主表编号=b.授课计划主表编号 and b.行政班代码=c.班级编号 and 学年学期编码='"+this.XNXQ+this.JXXZ+"' and a.场地要求 like '%"+this.JSBH+"%' and a.授课教师编号!='000' ";
			vec=db.GetContextVector(sql);		
		}
		
//		if(this.getsAuth().indexOf("09")>-1){
//			sql3 = "select b.行政班代码 from dbo.V_专业组组长信息 a,dbo.V_学校班级数据子类 b " +
//				"where a.专业代码=b.专业代码  and a.教师代码 like '%@" + MyTools.fixSql(this.getiUSERCODE()) + "@%' ";
//			vec3=db.GetContextVector(sql3);
//			sqlpkgl3+=" and ( ";
//			if(vec3!=null&&vec3.size()>0){
//				for(int i=0;i<vec3.size();i++){
//					if(i==0){
//						sqlpkgl3+=" a.行政班代码 like '%"+vec3.get(i).toString()+"%' ";
//					}else{
//						sqlpkgl3+=" or a.行政班代码 like '%"+vec3.get(i).toString()+"%' ";
//					}
//					
//				}
//			}
//			sqlpkgl3+=" ) ";
//		}
		
//		if(sqlpkgl.equals("")){
//			sql2="select * from V_规则管理_授课计划明细表 where 1=2 ";
//		}else{
//			sql2="select a.授课计划明细编号,a.学年学期编码,a.授课教师编号,a.授课教师姓名,a.授课计划主表编号,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情,a.节数,a.连节,a.固排已排节数,a.未排节数,a.连次,a.课程类型  from ("+sqlpkgl+") a where a.场地要求  like '%"+this.JSBH+"%' "+sqlpkgl3;
//		}
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
				
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
		Vector vec3 = null;
		String sql3="";
		if(termid.equalsIgnoreCase("")){
			sql3="select distinct 学年+学期+教学性质 as termid,学年+学期,教学性质 FROM V_规则管理_学年学期表 where 1=1 order by termid desc";
			vec3=db.GetContextVector(sql3);
			if(vec3!=null&&vec3.size()>0){
				termid=vec3.get(0).toString();
			}
		}
		String sql = " select COUNT(*) FROM dbo.V_规则管理_学期周次表 where 学年学期编码='"+MyTools.fixSql(termid)+"'" ;
		vec = db.GetContextVector(sql);
		if(vec!=null&vec.size()>0){
			this.setMSG(vec.get(0).toString());			
		}
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
		String sql = "select distinct 学年+学期 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		//String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
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
		String sql = "select distinct 教学性质 as comboName,cast(编号 as nvarchar) as comboValue " +
					 "from V_基础信息_教学性质 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取校区下拉框
	 * @date:2015-09-17
	 * @author:lupengfei
	 */
	public Vector schoolCombobox() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName union select 校区代码 AS comboValue,校区名称 AS comboName FROM dbo.V_校区数据类  where 1=1 ";
	
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教室类型下拉框
	 * @date:2015-09-17
	 * @author:lupengfei
	 */
	public Vector classtypeCombobox() throws SQLException{
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName union select 编号 AS comboValue,名称 AS comboName FROM dbo.V_基础信息_教室类型  where 编号!='1' and 编号!='5' ";
	
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJCSJ() throws SQLException{
		Vector vec = null;
		String sql = "";
		sql = "select 节次,开始时间,结束时间 from V_规则管理_节次时间表 where 学年学期编码='"
			  + MyTools.fixSql(MyTools.StrFiltr(this.XNXQ)) + MyTools.fixSql(MyTools.StrFiltr(this.JXXZ)) + "' order by 开始时间";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 
	 * @date:2015-06-03
	 * @author:wangzh
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGPJP() throws SQLException{
		Vector vec = null;
		String sql = "";
		String sql2 = "";
		
		if(this.JSBH.equalsIgnoreCase("")){
			sql="select * from V_规则管理_固排禁排表  where 1=2";
		}else{
			sql2="select a.类型,a.学年学期编码,a.行政班代码,a.时间序列,b.课程名称,c.班级名称,a.预设场地名称 as 场地名称,b.授课周次详情,b.授课教师姓名 from V_规则管理_固排禁排表 a left join V_规则管理_授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号 left join dbo.V_基础信息_班级信息表 c on a.行政班代码=c.班级编号 where a.学年学期编码='" + MyTools.fixSql(this.getXNXQ()+this.getJXXZ()) + "' and b.场地要求 like '%" + MyTools.fixSql(this.getJSBH()) + "%' and 类型='2' or (a.学年学期编码='" + MyTools.fixSql(this.getXNXQ()+this.getJXXZ()) + "' and a.行政班代码 ='" + MyTools.fixSql(this.getJSBH()) + "' and 类型='3' ) ";
				
				//"union " +
				//"select '2' as 类型,a.学年学期编码,a.行政班代码,a.时间序列,a.课程名称,c.行政班名称,a.实际场地名称,a.授课周次,a.授课教师编号 from dbo.V_排课管理_课程表明细详情表 a  left join dbo.V_学校班级数据子类 c on a.行政班代码=c.行政班代码 where a.学年学期编码='" + MyTools.fixSql(this.getXNXQ()+this.getJXXZ()) + "' and a.实际场地编号 like '%" + MyTools.fixSql(this.getJSBH()) + "%' ";
			
			sql = "select c.类型,c.时间序列, "+
				"stuff((select '｜'+d.课程名称 from ("+sql2+") d where c.学年学期编码=d.学年学期编码 and c.时间序列=d.时间序列 for xml path('')),1,1,'') as 课程名称, "+
				"stuff((select '｜'+d.班级名称 from ("+sql2+") d where c.学年学期编码=d.学年学期编码 and c.时间序列=d.时间序列 for xml path('')),1,1,'') as 行政班名称, "+
				"stuff((select '｜'+d.授课教师姓名 from ("+sql2+") d where c.学年学期编码=d.学年学期编码 and c.时间序列=d.时间序列 for xml path('')),1,1,'') as 预设场地名称, "+
				"stuff((select '｜'+d.授课周次详情 from ("+sql2+") d where c.学年学期编码=d.学年学期编码 and c.时间序列=d.时间序列 for xml path('')),1,1,'') as 授课周次  "+
				"from ("+sql2+") c group by c.类型,c.学年学期编码,c.场地名称,c.时间序列 ";
		}	
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	//判断哪些格子可以排课
	public void checkTeaCls(String jsxm,String skzcxq,String weeks,String cdyq,String iKeyCode,String termid,String cdyqId,String classdm,String jsbh) throws SQLException {
		Vector vec = new Vector();
		Vector vec2 = null;
		Vector vec0 = null;
		Vector vec4 = null;
		Vector vec5 = null;
		Vector vec6 = null;
		String sql = "";
		String sql2 = "";
		String sql0 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String sql7 = "";
		String times="";
		String sames="";
		String rooms="";
		String jinpai="";//教师，场地禁排
		ArrayList teaarray=new ArrayList();
		ArrayList wekarray=new ArrayList();
		ArrayList cdyarray=new ArrayList();
		//查询所有要排课老师和他们的上课周次
		//System.out.println("教师周次场地：------"+cdyq);
		String[] teac=jsbh.split("\\&");
		String[] week=skzcxq.split("\\&");
		String[] room=cdyq.split("\\&");
		
		//Map<String, String> map=new HashMap<String, String>();   
		for(int i=0;i<teac.length;i++){
			teaarray.add(teac[i]);  
		}
		for(int k=0;k<week.length;k++){
			wekarray.add(week[k]);
		}
		for(int l=0;l<room.length;l++){
			cdyarray.add(room[l]);
		}
		    

		//查询所有要排课的老师
		String teachers=jsxm.replaceAll("\\+", "\\&");		
				
		//查询所有其它类型的教室
		String jsbhqt="";
		String jsmcqt="";
		String othercode="";
		String[] specialCode=MyTools.getProp(request, "Base.specialCode").split(",");
		for(int i=0;i<specialCode.length;i++){
			othercode="'"+specialCode[i]+"',";
		}
		if(!othercode.equals("")){
			othercode=othercode.substring(0, othercode.length()-1);
			String sqlother="SELECT [教室编号],[教室名称] FROM [dbo].[V_教室数据类] where 教室类型代码 in ("+othercode+") and 是否可用='1' ";
			Vector vecother=db.GetContextVector(sqlother);
			if(vecother!=null&&vecother.size()>0){
				for(int i=0;i<vecother.size();i=i+2){
					jsbhqt+="@"+vecother.get(i).toString()+"@,";
					jsmcqt+="@"+vecother.get(i).toString()+"@,";
				}
				if(!jsbhqt.equals("")){
					jsbhqt=jsbhqt.substring(0, jsbhqt.length()-1);
				}
				if(!jsmcqt.equals("")){
					jsmcqt=jsmcqt.substring(0, jsmcqt.length()-1);
				}
			}
		}else{
					
		}
				
		//课程表明细详情表与授课计划比较
//				String sqlall="";
//				sql2+=" where 授课教师姓名 != '' and a.授课计划明细编号!='"+iKeyCode+"' and a.学年学期编码='"+MyTools.fixSql(termid)+"' ";
//				sql="select a.时间序列,"+
//					"stuff((select '|'+b.授课教师编号 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 授课教师编号,"+
//					"stuff((select '|'+b.授课教师姓名 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 授课教师姓名,"+
//					"stuff((select '|'+b.授课周次详情 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 授课周次详情,"+
//					"stuff((select '|'+b.实际场地编号 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 实际场地编号,"+
//					"stuff((select '|'+b.实际场地名称 from V_排课管理_课程表明细详情表 b where a.学年学期编码=b.学年学期编码 and a.行政班代码=b.行政班代码 and a.时间序列=b.时间序列 for xml path('')),1,1,'') as 实际场地名称,"+
//					"a.行政班名称,a.课程名称 from V_排课管理_课程表明细详情表 a ";
//				sql3=" group by a.时间序列,a.学年学期编码,a.行政班代码,a.行政班名称,a.课程名称 ";
//				sql7=" union all SELECT 上课时间 = substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number),a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,a.课程名称 " +
//						" FROM  (select a.上课时间,a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,b.课程名称 from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.学年学期编码='"+MyTools.fixSql(termid)+"') a JOIN" +
//						" master..spt_values b ON b.type = 'P' " +
//						" WHERE     substring(',' + a.上课时间, b.number, 1) = ',' ";
//				sqlall=sql+sql2+sql3+sql7;
//				vec=db.GetContextVector(sqlall);
				
		
		//查询课程表周详情表
		sql0="SELECT [时间序列],[授课教师编号],[授课教师姓名],[授课周次],[场地编号],[场地名称],[行政班名称],[课程名称],[授课计划明细编号] " +
					"FROM [dbo].[V_排课管理_课程表周详情表] " +
					"where 学年学期编码='"+MyTools.fixSql(termid)+"' and 授课教师姓名 != '' and [场地编号]!='' " +
					"order by [时间序列],[行政班名称],[课程名称],convert(int,授课周次)";
		vec0=db.GetContextVector(sql0);
		if(vec0!=null&&vec0.size()>0){
			String skzc="";
			for(int i=0;i<vec0.size()-9;){
				skzc=vec0.get(i+3).toString();			
					while(i<(vec0.size()-9)&&vec0.get(i+8).toString().equals(vec0.get(i+8+9).toString())&&vec0.get(i).toString().equals(vec0.get(i+9).toString())){//授课计划编号相等+时间序列相等					
						skzc+="#"+vec0.get(i+3+9).toString();
						//System.out.println(i+":--"+skzc);
						i=i+9;
					}
					skzcxq=merge(skzc);
					String[] skjsbh5=vec0.get(i+1).toString().split("\\+");
					String[] skjsxm5=vec0.get(i+2).toString().split("\\+");
					for(int m=0;m<skjsbh5.length;m++){
						vec.add(vec0.get(i).toString());//时间序列
						vec.add(skjsbh5[m]);//授课教师编号
						vec.add(skjsxm5[m]);//授课教师姓名
						vec.add(skzcxq);//授课周次
						vec.add(vec0.get(i+4).toString());//场地编号
						vec.add(vec0.get(i+5).toString());//场地名称
						vec.add(vec0.get(i+6).toString());//行政班名称
						vec.add(vec0.get(i+7).toString());//课程名称
					}
					i=i+9;
			}					
		}
								
//				for(int i=0;i<vec.size();i=i+8){
//					System.out.println(vec.get(i).toString()+"|"+vec.get(i+1).toString()+"|"+vec.get(i+2).toString()+"|"+vec.get(i+3).toString()+"|"+vec.get(i+4).toString()+"|"+vec.get(i+5).toString()+"|"+vec.get(i+6).toString()+"|"+vec.get(i+7).toString());
//				}
								
		//查询选修课
		sql2="SELECT 上课时间 = substring(a.上课时间, b.number, charindex(',', a.上课时间 + ',', b.number) - b.number),a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,a.课程名称 " +
					" FROM  (select a.上课时间,a.授课教师编号,a.授课教师姓名,a.授课周次,a.场地要求,a.场地名称,a.选修班名称,b.课程名称 from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and b.学年学期编码='"+MyTools.fixSql(termid)+"') a JOIN" +
					" master..spt_values b ON b.type = 'P' " +
					" WHERE     substring(',' + a.上课时间, b.number, 1) = ',' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			for(int m=0;m<vec2.size();m=m+8){
				vec.add(vec2.get(m).toString());//时间序列
				vec.add(vec2.get(m+1).toString());//授课教师编号
				vec.add(vec2.get(m+2).toString());//授课教师姓名
				vec.add(vec2.get(m+3).toString());//授课周次
				vec.add(vec2.get(m+4).toString());//场地编号
				vec.add(vec2.get(m+5).toString());//场地名称
				vec.add(vec2.get(m+6).toString());//行政班名称
				vec.add(vec2.get(m+7).toString());//课程名称
			}
		}
								
		if(vec!=null&vec.size()>0){
			for(int i=0;i<vec.size();i=i+8){
				for (int k=0;k<wekarray.size();k++) { 
					//if(vec.get(i+1).toString().indexOf(teaarray.get(k).toString())>-1){//存在
//						String t1=vec.get(i+1).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//						String t2=vec.get(i+2).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//						String t3=vec.get(i+3).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//						String t4=vec.get(i+4).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//						String t5=vec.get(i+5).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
//														
//						String[] selteabh2=t1.split("\\&");
//						String[] selteaname2=t2.split("\\&");
//						String[] selwektime2=t3.split("\\&");
//						String[] selclsbh2=t4.split("\\&");
//						String[] selclsname2=t5.split("\\&");
						//for(int m=0;m<selteaname2.length;m++){
														
						//比较周次是否有相同
						int tag1=compareweek(wekarray.get(k).toString(),vec.get(i+3).toString(),weeks);
						if(tag1==1){//有重复
							//比较教师是否有相同
							int tag3=compareteacher(teaarray.get(k).toString(),vec.get(i+1).toString().trim());
							if(tag3==1){//教师相同	
								times+=vec.get(i).toString()+",";
								sames+=vec.get(i).toString()+","+vec.get(i+2).toString()+","+vec.get(i+6).toString()+","+vec.get(i+5).toString()+","+vec.get(i+3).toString()+",";;
							}else{//没有相同
								//比较场地是否有相同
								int tag2=compareroom(cdyarray.get(k).toString(),vec.get(i+4).toString(),jsbhqt);
								if(tag2==1){//场地相同
									times+=vec.get(i).toString()+",";
									sames+=vec.get(i).toString()+","+vec.get(i+2).toString()+","+vec.get(i+6).toString()+","+vec.get(i+5).toString()+","+vec.get(i+3).toString()+",";;
								}else{
																		
								}
							}
						}else{//没有重复
															
						}
						//}			
					//}
				}
			}	
		}	
		
		//固排表与授课计划比较	
		sql4="select a.时间序列,b.授课教师编号,b.授课教师姓名,b.授课周次详情,a.预设场地编号,d.班级名称,b.课程名称,c.行政班代码  from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b,dbo.V_规则管理_授课计划主表 c,dbo.V_基础信息_班级信息表 d where a.授课计划明细编号=b.授课计划明细编号  and b.授课计划主表编号=c.授课计划主表编号 and c.行政班代码=d.班级编号  and a.学年学期编码='"+MyTools.fixSql(termid)+"' ";
		vec4=db.GetContextVector(sql4);
		if(vec4!=null&vec4.size()>0){
			for(int i=0;i<vec4.size();i=i+8){
				//System.out.println("时间序列：--"+vec4.get(i).toString()+" 授课教师姓名:--"+vec4.get(i+1).toString()+" 授课周次详情:--"+vec4.get(i+2).toString()+" 场地要求:--"+vec4.get(i+3).toString());
				//for (Map.Entry<String, String> entry : map.entrySet()) {  
				for (int k=0;k<wekarray.size();k++) { 
		            //System.out.println("1:"+teaarray.get(k).toString()+"2:"+wekarray.get(k).toString()+"3:"+cdyarray.get(k).toString());  
					//if(vec4.get(i+1).toString().indexOf(teaarray.get(k).toString())>-1){//存在
						String t1=vec4.get(i+1).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
						String t2=vec4.get(i+2).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
						String t3=vec4.get(i+3).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
						String t4=vec4.get(i+4).toString().replaceAll("\\&amp;","\\&").replaceAll("\\|","\\&");
						String[] selteabh=t1.split("\\&");
						String[] selteaname=t2.split("\\&");
						String[] selwektime=t3.split("\\&");
						String[] selclsname=t4.split("\\&");
						//System.out.println(wekarray.size()+"|"+selteaname.length);
						for(int j=0;j<selteabh.length;j++){
							//System.out.println("时间序列：--"+vec4.get(i).toString()+" 教师姓名："+selteaname[j]+" 教师姓名2:"+teaarray.get(k).toString());
							//班级是否相同
							if(vec4.get(i+7).toString().equals(classdm)){
								//比较周次是否有相同
								int tag1=compareweek(wekarray.get(k).toString(),selwektime[j],weeks);
								if(tag1==1){
									times+=vec4.get(i).toString()+",";
									sames+=vec4.get(i).toString()+","+selteaname[j]+","+vec4.get(i+5).toString()+","+selclsname[j]+","+selwektime[j]+",";	
								}else{
									//比较教师是否有相同
									int tag3=compareteacher(teaarray.get(k).toString(),selteabh[j].trim());
									if(tag3==1){//教师相同								
										times+=vec4.get(i).toString()+",";
										sames+=vec4.get(i).toString()+","+selteaname[j]+","+vec4.get(i+5).toString()+","+selclsname[j]+","+selwektime[j]+",";	
									}else{
										//比较场地是否有相同
										int tag2=compareroom(cdyarray.get(k).toString(),t4,jsbhqt);
										//System.out.println("tag2:"+tag2);
										if(tag2==1){
											times+=vec4.get(i).toString()+",";
											sames+=vec4.get(i).toString()+","+selteaname[j]+","+vec4.get(i+5).toString()+","+selclsname[j]+","+selwektime[j]+",";
										}else{
											
										}
									}
								}
							}else{
								//比较周次是否有相同
								int tag1=compareweek(wekarray.get(k).toString(),selwektime[j],weeks);
								if(tag1==1){
									//比较教师是否有相同
									int tag3=compareteacher(teaarray.get(k).toString(),selteabh[j].trim());
									if(tag3==1){//教师相同								
										times+=vec4.get(i).toString()+",";
										sames+=vec4.get(i).toString()+","+selteaname[j]+","+vec4.get(i+5).toString()+","+selclsname[j]+","+selwektime[j]+",";	
									}else{
										//比较场地是否有相同
										int tag2=compareroom(cdyarray.get(k).toString(),t4,jsbhqt);
										//System.out.println("tag2:"+tag2);
										if(tag2==1){
											times+=vec4.get(i).toString()+",";
											sames+=vec4.get(i).toString()+","+selteaname[j]+","+vec4.get(i+5).toString()+","+selclsname[j]+","+selwektime[j]+",";
										}else{
											
										}
									}
								}else{
									
								}
							}
						}
					//}
		        }
			}
		}
		//System.out.println("固排禁排表：----------------"+times);
		//System.out.println("sames：----------------"+sames);
		
		//固排禁排表查询禁排的时间序列
		//班级,教师，场地禁排
		sql5=" select a.时间序列,a.行政班代码,a.禁排类型,case when a.禁排类型='bj' then c.班级名称 when a.禁排类型='js' then b.姓名 when a.禁排类型='cd' then d.教室名称 else '' end as 名称 from dbo.V_规则管理_固排禁排表 a left join dbo.V_教职工基本数据子类 b on a.行政班代码=b.工号 left join dbo.V_基础信息_班级信息表 c on a.行政班代码=c.班级编号 left join dbo.V_教室数据类 d on a.行政班代码=d.教室编号   where 学年学期编码='"+MyTools.fixSql(termid)+"' and 类型='3' " ;
		vec5=db.GetContextVector(sql5);
		for(int i=0;i<vec5.size();i=i+4){
			if(vec5.get(i+2).toString().equals("bj")){
				if(vec5.get(i+1).toString().equals(classdm)){
					times+=vec5.get(i).toString()+",";
					jinpai+=vec5.get(i).toString()+","+vec5.get(i+3).toString()+","+vec5.get(i+2).toString()+",";
				}
			}else if(vec5.get(i+2).toString().equals("js")){
				String[] teabhid=jsbh.split("\\&");
				for(int j=0;j<teabhid.length;j++){
					String[] teabhid2=teabhid[j].split("\\+");
					for(int k=0;k<teabhid2.length;k++){
						if(teabhid2[k].equals(vec5.get(i+1).toString())){
							times+=vec5.get(i).toString()+",";
							jinpai+=vec5.get(i).toString()+","+vec5.get(i+3).toString()+","+vec5.get(i+2).toString()+",";
						}
					}
				}
			}else if(vec5.get(i+2).toString().equals("cd")){
				String[] cdbhid=cdyq.split("\\&");
				for(int j=0;j<cdbhid.length;j++){ 
					if(cdbhid[j].indexOf(vec5.get(i+1).toString())>-1){
						times+=vec5.get(i).toString()+",";
						//jinpai+=vec5.get(i).toString()+","+vec5.get(i+1).toString()+","+vec5.get(i+2).toString()+",";
					}
				}	
			}		
		}
				
				//天山二中
//				for(int i=0;i<vec5.size();i=i+2){
//					if(vec5.get(i+1).toString().length()==4){
//						if(Integer.parseInt(vec5.get(i+1).toString().substring(1, 2))>5){//班级禁排
//							if(classdm.equals(vec5.get(i+1).toString())){
//								times+=vec5.get(i).toString()+",";
//							}
//						}else if(Integer.parseInt(vec5.get(i+1).toString().substring(1, 2))<6){//场地禁排
//							if(cdyq.equals(vec5.get(i+1).toString())){
//								times+=vec5.get(i).toString()+",";
//							}
//						}
//					}else if(vec5.get(i+1).toString().length()==6){//教师禁排
//						if(jsbh.equals(vec5.get(i+1).toString())){
//							times+=vec5.get(i).toString()+",";
//						}
//					}
//				}
		
		//合班表查询不能排的时间序列
		String sqlbh="select 授课计划明细编号 from dbo.V_规则管理_合班表 where 授课计划明细编号 like '%"+MyTools.fixSql(iKeyCode)+"%'";
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
		        	skjhhb=vecbj.get(0).toString();
		        	
		        	sql6=" select a.时间序列,a.行政班代码,a.禁排类型,case when a.禁排类型='bj' then c.班级名称 when a.禁排类型='js' then b.姓名 when a.禁排类型='cd' then d.教室名称 else '' end as 名称 from dbo.V_规则管理_固排禁排表 a left join dbo.V_教职工基本数据子类 b on a.行政班代码=b.工号 left join dbo.V_基础信息_班级信息表 c on a.行政班代码=c.班级编号 left join dbo.V_教室数据类 d on a.行政班代码=d.教室编号   where 学年学期编码='"+MyTools.fixSql(termid)+"' and a.行政班代码='"+MyTools.fixSql(skjhhb)+"' and 类型='3' " ;
		        	vec6=db.GetContextVector(sql6);
		        	for(int k=0;k<vec6.size();k=k+4){
		        		times+=vec6.get(k).toString()+",";
		        		if(classdm.equalsIgnoreCase(skjhhb)){//与自己班级编号不同，查询该班级禁排的时间序列
		        			jinpai+=vec6.get(k).toString()+","+vec6.get(k+3).toString()+","+vec6.get(k+2).toString()+",";
		        		}else{
		        			jinpai+=vec6.get(k).toString()+","+vec6.get(k+3).toString()+",hb,";	
				        }
		        	}	
		        }
		    }
		}
		
		if(!times.equalsIgnoreCase("")){
			times=times.substring(0,times.length()-1);
		}
		if(!sames.equalsIgnoreCase("")){
			sames=sames.substring(0,sames.length()-1);
		}
		if(!rooms.equalsIgnoreCase("")){
			rooms=rooms.substring(0,rooms.length()-1);
		}
		if(!jinpai.equalsIgnoreCase("")){
			jinpai=jinpai.substring(0,jinpai.length()-1);
		}
		
		//System.out.println("times:------------------------------"+times);
		this.setMSG(times);
		this.setMSG2(sames);
		this.setMSG3(rooms);
		this.setMSG4("");
		this.setMSG5(jinpai);	
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
	
	//比较教师是否有相同
	public int compareteacher(String arrteach,String selteach) throws SQLException {
		int result=2;//不相同	
		//System.out.println("场地："+arrcdyq+"---"+selcdyq);	
		String[] arrteach2=arrteach.split("\\&");
		String[] selteach2=selteach.split("\\&");
		for(int m=0;m<arrteach2.length;m++){
			for(int n=0;n<selteach2.length;n++){
				String[] arrteach3=arrteach2[m].split("\\+");
				String[] selteach3=selteach2[n].split("\\+");
				for(int i=0;i<arrteach3.length;i++){
					for(int j=0;j<selteach3.length;j++){
						if(arrteach3[i].equalsIgnoreCase(selteach3[j])){
							result=1;
						}else{
											
						}
					}
				}
			}
		}
		return result;
	}
	
	//比较周次是否有相同
	public int compareweek(String arrweek,String selweek,String weeks) throws SQLException {
		int result=0;
		
		String week1="";
		String week2="";
		if(arrweek.equalsIgnoreCase("odd")){
			for(int i=1;i<=Integer.parseInt(weeks);i++){
				if(i%2!=0){
					week1+=i+"#";
				}	
			}
			week1=week1.substring(0,week1.length()-1);
		}else if(arrweek.equalsIgnoreCase("even")){
			for(int i=1;i<=Integer.parseInt(weeks);i++){
				if(i%2==0){
					week1+=i+"#";
				}	
			}
			week1=week1.substring(0,week1.length()-1);
		}else if(arrweek.indexOf("\\#")>-1){
			
		}else if(arrweek.indexOf("-")>-1){
			String[] wek=arrweek.split("-");
			for(int i=Integer.parseInt(wek[0]);i<=Integer.parseInt(wek[1]);i++){
				week1+=i+"#";	
			}
			week1=week1.substring(0,week1.length()-1);
		}else{
			week1=arrweek;
		}
		
		if(selweek.equalsIgnoreCase("odd")){
			for(int i=1;i<=Integer.parseInt(weeks);i++){
				if(i%2!=0){
					week2+=i+"#";
				}	
			}
			week2=week2.substring(0,week2.length()-1);
		}else if(selweek.equalsIgnoreCase("even")){
			for(int i=1;i<=Integer.parseInt(weeks);i++){
				if(i%2==0){
					week2+=i+"#";
				}	
			}
			week2=week2.substring(0,week2.length()-1);
		}else if(selweek.indexOf("\\#")>-1){
			
		}else if(selweek.indexOf("-")>-1){
			//System.out.println("selweek:----"+selweek);
			String[] wek=selweek.split("-");
			for(int i=Integer.parseInt(wek[0]);i<=Integer.parseInt(wek[1]);i++){
				week2+=i+"#";	
			}
			week2=week2.substring(0,week2.length()-1);
		}else{
			week2=selweek;
		}
		//System.out.println("week1:"+week1);
		//System.out.println("week2:"+week2);
		String[] weeks1=week1.split("\\#");
		String[] weeks2=week2.split("\\#");
		for(int i=0;i<weeks1.length;i++){
			for(int j=0;j<weeks2.length;j++){
				//System.out.println("week1:"+weeks1[i]);
				//System.out.println("week2:"+weeks2[j]);
				
				if(weeks2[j].equals(weeks1[i])){//存在
					result=1;
				}
			}
		}
		return result;
	}
		
	//比较教室是否有相同
		public int compareroom(String arrcdyq,String selcdyq,String jsbhqt) throws SQLException {
			int result=0;
			
			//System.out.println("场地："+arrcdyq+"---"+selcdyq);	
			if(arrcdyq.length()==1){//场地类型
				
			}else if(jsbhqt.indexOf("@"+arrcdyq+"@")>-1){//是其他类型教师
				
			}else{//具体教室编号
				String[] arrcdyq2=arrcdyq.split("\\&");
				String[] selcdyq2=selcdyq.split("\\&");
				for(int m=0;m<arrcdyq2.length;m++){
					for(int n=0;n<selcdyq2.length;n++){
						String[] arrcdyq3=arrcdyq2[m].split("\\+");
						String[] selcdyq3=selcdyq2[n].split("\\+");
						for(int i=0;i<arrcdyq3.length;i++){
							for(int j=0;j<selcdyq3.length;j++){
								if(arrcdyq3[i].equalsIgnoreCase(selcdyq3[j])){
									if(jsbhqt.indexOf("@"+selcdyq3[j]+"@")>-1){
										result=2;
									}else{
										result=1;
									}
								}else{
									if(result==1){
										
									}else{
										result=2;
									}
								}
							}
						}
					}
				}
			}

			return result;
		}
	
	//检查是添加还是删除，及可以排课的数量
	public void checkRec(String idVal) throws SQLException {
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		String js = "";
		String lj = "";
		String lc = "";
		String zjs= "";
		String lcs= "";
		String skjhbh="";
		int ys;
	
		
		sql2="select count(*) from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and a.授课计划明细编号='"+ MyTools.fixSql(MyTools.StrFiltr(this.getSKJHMXBH())) + "' and a.时间序列='"+ idVal + "' ";
		vec2=db.GetContextVector(sql2);
		if(vec2!=null&&vec2.size()>0){
			this.setAOD(vec2.get(0).toString());
		}
		
			sql = "select 固排已排节数,连节,连次,节数,固排连次次数 from V_规则管理_授课计划明细表 where 授课计划明细编号 = '"+ MyTools.fixSql(MyTools.StrFiltr(this.getSKJHMXBH())) + "'";
			vec = db.GetContextVector(sql);
			if (vec != null && vec.size() > 0) {
				js = MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
				lj = MyTools.fixSql(MyTools.StrFiltr(vec.get(1)));
				lc = MyTools.fixSql(MyTools.StrFiltr(vec.get(2)));
				zjs= MyTools.fixSql(MyTools.StrFiltr(vec.get(3)));
				lcs= MyTools.fixSql(MyTools.StrFiltr(vec.get(4)));
			}
			this.setLCS(lcs);
			if(Integer.parseInt(zjs)>Integer.parseInt(js)){//可以排节数
				int jss=Integer.parseInt(zjs)-Integer.parseInt(js);
				this.setMSG(jss+"");//可以排几节+已用连次次数
			}else if(Integer.parseInt(js)==0){//已排节数为0，不能再减
				this.setMSG("minus");
			}else{
				this.setMSG("no");
			}
			
	}
	
	public void addRec(String gpjs) throws SQLException {
		Vector vec = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		String js = "";
		String lj = "";
		String lc = "";
		String zjs= "";
		String lcs= "";
		int ys;
		int zs;
		
		//查询合班信息
        String sqlbh="select 授课计划明细编号 from dbo.V_规则管理_合班表 where 授课计划明细编号 like '%"+MyTools.fixSql(this.SKJHMXBH)+"%'";
        Vector vecbh=db.GetContextVector(sqlbh);
        String[] skjhsz;
        if(vecbh!=null&&vecbh.size()>0){
        	skjhsz=vecbh.get(0).toString().split("\\+");
        }else{
        	skjhsz=this.SKJHMXBH.split("\\+");
        }
        
        for(int i=0;i<skjhsz.length;i++){
    		this.setSKJHMXBH(skjhsz[i]);
			sql = "select 固排已排节数,连节,连次,节数,固排连次次数 from V_规则管理_授课计划明细表 where 授课计划明细编号 = '"+ MyTools.fixSql(MyTools.StrFiltr(this.SKJHMXBH)) + "'";
			vec = db.GetContextVector(sql);
			if (vec != null && vec.size() > 0) {
				js = MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
				lj = MyTools.fixSql(MyTools.StrFiltr(vec.get(1)));
				lc = MyTools.fixSql(MyTools.StrFiltr(vec.get(2)));
				zjs= MyTools.fixSql(MyTools.StrFiltr(vec.get(3)));
				lcs= MyTools.fixSql(MyTools.StrFiltr(vec.get(4)));
			}
			
			if(Integer.parseInt(zjs)==Integer.parseInt(js)){//节数排完
				this.setMSG("3");
			}else{
				ys = Integer.parseInt(js) + Integer.parseInt(gpjs);
				if(Integer.parseInt(gpjs)>1){
					zs = Integer.parseInt(lcs) + 1;
					sql2=",固排连次次数='"+ MyTools.fixSql(MyTools.StrFiltr(zs)) +"' ";
				}
				
				sql1 = "update V_规则管理_授课计划明细表  " +
					   "set 固排已排节数='"+ MyTools.fixSql(MyTools.StrFiltr(ys)) + "' " + sql2 +
					   "where 授课计划明细编号 = '"+ MyTools.fixSql(MyTools.StrFiltr(this.SKJHMXBH)) + "'";
				
				if (db.executeInsertOrUpdate(sql1)) {
					this.setMSG("1");
				} else {
					this.setMSG("2");
				}
			}
        }
	}
	
	public void delRec(String idValcon) throws SQLException {
		Vector vec = null;
		Vector vec3 = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		String js = "";
		String lj = "";
		String lc = "";
		String lcs = "";
		String skjhid="";
		String sjypjs="";//实际已排节数
		String sjlccs="";//实际连次次数
		int ys=0;
		int zs=0;
		
		//查询合班信息
        String sqlbh="select 授课计划明细编号 from dbo.V_规则管理_合班表 where 授课计划明细编号 like '%"+MyTools.fixSql(this.SKJHMXBH)+"%'";
        Vector vecbh=db.GetContextVector(sqlbh);
        String[] skjhsz;
        if(vecbh!=null&&vecbh.size()>0){
        	skjhsz=vecbh.get(0).toString().split("\\+");
        }else{
        	skjhsz=this.SKJHMXBH.split("\\+");
        }
        
		for(int i=0;i<skjhsz.length;i++){
    		this.setSKJHMXBH(skjhsz[i]);
			sql = "select 固排已排节数,连节,连次,固排连次次数,授课计划明细编号,实际已排节数,实际连次次数 " +
				  "from V_规则管理_授课计划明细表 a " +
				  "left join V_规则管理_授课计划主表 b " +
				  "on a.授课计划主表编号=b.授课计划主表编号 " +
				  "where b.学年学期编码='" + MyTools.fixSql(XNXQ) + MyTools.fixSql(JXXZ) + "' " +
				  "and a.授课教师编号 like '%" + MyTools.fixSql(this.JSBH) +"%' " +
				  "and a.授课计划明细编号='" + MyTools.fixSql(this.SKJHMXBH) +"'";
			vec = db.GetContextVector(sql);
			if (vec != null && vec.size() > 0) {
				js = MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
				lj = MyTools.fixSql(MyTools.StrFiltr(vec.get(1)));
				lc = MyTools.fixSql(MyTools.StrFiltr(vec.get(2)));
				lcs = MyTools.fixSql(MyTools.StrFiltr(vec.get(3)));
				skjhid = MyTools.fixSql(MyTools.StrFiltr(vec.get(4)));
				sjypjs = MyTools.fixSql(MyTools.StrFiltr(vec.get(5)));
  				sjlccs = MyTools.fixSql(MyTools.StrFiltr(vec.get(6)));
			}
			
			if(Integer.parseInt(js)==0){//已排节数为0，不能再减
				this.setMSG("3");
			}else{
				String sqllj="select a.连节相关编号,b.连次 from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and a.授课计划明细编号='"+MyTools.fixSql(MyTools.StrFiltr(this.SKJHMXBH))+"' and a.时间序列 in ("+idValcon+")";
	    		vec3=db.GetContextVector(sqllj);
	    		String editlj="";
	    		int delnum=0;
	    		if(vec3!=null&&vec3.size()>0){
	    			if(vec3.get(0).toString().equalsIgnoreCase("")){
	    				delnum=1;
	    				ys = Integer.parseInt(js) - delnum;
	    				
	    				if(!sjypjs.equals("0")){
          					sql2+=",实际已排节数='"+ MyTools.fixSql(MyTools.StrFiltr(ys)) +"' ";
          				}
	    			}else{
	    				String[] ljbh=vec3.get(0).toString().split(",");
	        			delnum=ljbh.length+1;
	        			zs = Integer.parseInt(lcs) - 1;
	        			ys = Integer.parseInt(js) - delnum;
	        			
	        			sql2=",固排连次次数='"+ MyTools.fixSql(MyTools.StrFiltr(zs)) +"' ";
	        			
	        			if(!sjypjs.equals("0")){
          					sql2+=",实际已排节数='"+ MyTools.fixSql(MyTools.StrFiltr(ys)) +"' ";
          				}
          				if(!sjlccs.equals("0")){
          					sql2+=",实际连次次数='"+ MyTools.fixSql(MyTools.StrFiltr(zs)) +"' ";
          				}
	    			}	
	    			
	    			sql1 += "update V_规则管理_授课计划明细表  " +
	 					   "set 固排已排节数='"+ MyTools.fixSql(MyTools.StrFiltr(ys)) + "' " + sql2 +
	 					   "where 授课计划明细编号 = '"+ MyTools.fixSql(MyTools.StrFiltr(skjhid)) + "'";
	    		}

				if (db.executeInsertOrUpdate(sql1)) {
					this.setMSG("1");
					this.setMSG2(delnum+"");
				} else {
					this.setMSG("2");
				}
			}	
		}
	}
	
	/**
	 * 保存方法
	 * @date:2015-06-03
	 * @author:wangzh
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveRec(String editsjxl,String cdyq,String cdmc,String skzc,String savetype) throws WrongSQLException, SQLException {
		String sql = "";
		String sql1 = "";
		Vector vec = null; // 结果集
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		String maxMxId="";
		String xzbdm=this.GG_XZBDM;
		Vector veckyzc=new Vector();//可用周次
        Vector vecbkyzc=new Vector();//不可用周次
        
		int k=0;
		int j=0;
		int v=0;
		int flag=0;
		
		StringTokenizer sjxl = new StringTokenizer(this.GG_SJXL,",");
		String[] sjxlArray = new String[sjxl.countTokens()];
        while(sjxl.hasMoreTokens()){
        	sjxlArray[k++]=sjxl.nextToken();
        }
        
        String kcjshtml=this.KCJS;
        kcjshtml=kcjshtml.replaceAll("&amp;", "&");
        StringTokenizer kcjs = new StringTokenizer(kcjshtml,",");
		String[] kcjsArray = new String[kcjs.countTokens()];
        while(kcjs.hasMoreTokens()){
        	kcjsArray[j++]=kcjs.nextToken();
        }
		
//        sql= "delete from V_规则管理_固排禁排表 where " +
//        	 "学年学期编码 = '"+ MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM)) +"' and " +
//        	 "行政班代码 = '"+ MyTools.StrFiltr(MyTools.fixSql(this.GG_XZBDM)) +"'";
        
      //查询合班信息
        String sqlbh="select 授课计划明细编号 from dbo.V_规则管理_合班表 where 授课计划明细编号 like '%"+MyTools.fixSql(this.GG_SKJHMXBH)+"%'";
        Vector vecbh=db.GetContextVector(sqlbh);
        Vector vecbj=null;
        String[] skjhsz;
        String sqlbj="";
        String skjhhb="";
        
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
        	skjhsz=this.GG_SKJHMXBH.split("\\+");
        	skjhhb="'"+this.GG_XZBDM+"'";
        }
        
      //选修课使用教室
        String xxkroom="";
        Vector vecxxk=null;
    	String sqlxxk="  select distinct a.场地要求 from dbo.V_规则管理_选修课授课计划明细表 a,dbo.V_规则管理_选修课授课计划主表 b where a.[授课计划主表编号]=b.[授课计划主表编号] and b.[学年学期编码]='"+MyTools.fixSql(this.getGG_XNXQBM())+"' and charindex('"+editsjxl.replaceAll("'", "")+"',a.上课时间)>0 ";
        vecxxk=db.GetContextVector(sqlxxk);
        if(vecxxk!=null&&vecxxk.size()>0){
        	for(int x=0;x<vecxxk.size();x++){
        		xxkroom+=",'"+vecxxk.get(x).toString()+"'";
        	}	
        }
        
        //20170425 增加教室第几周可用功能，分配教室是需要添加判断
        //获取当前学期 实际上课周数
        String sqlsjskzs="select [实际上课周数] from dbo.V_规则管理_学年学期表 where 学年学期编码='"+MyTools.fixSql(this.getGG_XNXQBM())+"' ";
        Vector vecsjskzs=db.GetContextVector(sqlsjskzs);
        String sjskzs="";
        if(vecsjskzs!=null&&vecsjskzs.size()>0){
        	sjskzs=vecsjskzs.get(0).toString();
        }
        //计算每间教室不能使用周次
        String[] kynum=new String[Integer.parseInt(sjskzs)];
        
//        String sqlbkyzc="select [教室编号],[不可用周次] from [V_教室可用状态表] where 学年学期编码='"+MyTools.fixSql(this.getGG_XNXQBM())+"' and [不可用周次]!='' ";
//        vecbkyzc=db.GetContextVector(sqlbkyzc);

        
        //==================================================================================================================
        
        
        //按每个班级处理
        for(int b=0;b<skjhsz.length;b++){
        	sqlbj="select b.行政班代码 from dbo.V_规则管理_授课计划明细表 a,dbo.V_规则管理_授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号='"+skjhsz[b]+"'";
    		vecbj=db.GetContextVector(sqlbj);
    		if(vecbj!=null&&vecbj.size()>0){
    			this.setGG_SKJHMXBH(skjhsz[b]);
    			this.setGG_XZBDM(vecbj.get(0).toString());
    		}
    		//执行操作
	        if(savetype.equals("add")){
	        	for (int i = 0; i < sjxlArray.length; i++) {
	            	sql1="select max(cast(编号 as int)) from V_规则管理_固排禁排表";
	            	vec1 = db.GetContextVector(sql1);
	            	
	            	
	            	if(kcjsArray[i]!=""){
	            		if("禁排".equalsIgnoreCase(kcjsArray[i])){
	            			this.setGG_LX("3");
	                	}else{
	                		this.setGG_LX("2");
	                		
	                		String sql2="select a.授课计划明细编号 from V_规则管理_授课计划明细表 a " +
	            				    "left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
	            				    "where b.学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"' " +
	            				    "and b.行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XZBDM))+"' " +
	            				    "and a.课程名称+a.授课教师姓名='"+kcjsArray[i]+"'";
	    	        		vec = db.GetContextVector(sql2);
	    	        		if (vec != null && vec.size() > 0) {
	    	        			GG_SKJHMXBH = MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
	    	    			}
	                	}
	            	}
	            	
	            	System.out.println("CDYQ:--"+cdyq);
	            	System.out.println("CDMC:--"+cdmc);
	            	int jslxdm=0;//教室类型代码
	            	String rs="";
	            	String rt="";
	            	String roomnum="";
	            	String roomname="";
	            	String[] cdyq1=cdyq.split("｜");
	            	String[] cdmc1=cdmc.split("｜");
	            	for(int r=0;r<cdyq1.length;r++){
	            		String[] cdyq2=cdyq1[r].split("\\&");
	            		String[] cdmc2=cdmc1[r].split("\\&");
	            		for(int s=0;s<cdyq2.length;s++){
	            			String[] cdyq3=cdyq2[s].split("\\+");
	            			String[] cdmc3=cdmc2[s].split("\\+");
	            			
	            			String selroomid="";
	            			for(int t=0;t<cdyq3.length;t++){
	            				if(cdyq3[t].equals("5")){//普通教室
	            					jslxdm=5;
	            					String sqlnum="select 总人数  from V_基础信息_班级信息表 where 班级编号='"+MyTools.fixSql(this.GG_XZBDM)+"'";
	            		        	vec2=db.GetContextVector(sqlnum);
	            		        	String sqlrom="select 实际场地编号,授课周次详情 from dbo.V_排课管理_课程表明细详情表 where 时间序列 in ("+editsjxl+")";
	            		        	vec4=db.GetContextVector(sqlrom);
	            		        	
	            		        	//添加vecbkyzc
//	            		        	for(int d=0;d<vecbkyzc.size();d=d+2){
//	            		        		vec4.add(vecbkyzc.get(d).toString());
//	            		        		vec4.add(vecbkyzc.get(d+1).toString());
//	            		        	}
	            		        	
	            		        	String userooms="";
	            		        	if(vec2!=null&&vec2.size()>0&&vec4!=null&&vec4.size()>0){
	            		        		for(int m=0;m<vec4.size();m=m+2){
	            		        			//判断授课周次是否冲突  
	            		        			int res=checkskzc(skzc,vec4.get(m+1).toString());
	            		        			//System.out.println("vec4:-----"+skzc+"|"+vec4.get(m+1).toString()+"|"+res);
	            		        			//授课周次有冲突
	            		        			if(res==1){
	            		        				String useroom=vec4.get(m).toString();
		            		        			useroom=useroom.replaceAll("｜", "\\&").replaceAll("\\+", "\\&");
		            		        			//System.out.println("useroom:-----"+useroom);
		            		        			String[] useroomnum=useroom.split("\\&");
		            		        			for(int n=0;n<useroomnum.length;n++){
		            		        				userooms+="'"+useroomnum[n]+"',";
		            		        			}
		            		        		
	            		        			}		
	            		        		}
	            		        		if(userooms.equals("")){
	            		        			userooms="''";
	            		        		}else{
	            		        			userooms=userooms.substring(0, userooms.length()-1);
	            		        		}
	            		        		
	            		        		String sqlcls="select 教室编号,教室名称,最大排课容量 from dbo.V_教室数据类 where 最大排课容量>='"+Integer.parseInt(vec2.get(0).toString())+"' and 教室类型代码='"+jslxdm+"' and 教室编号 not in ("+userooms+selroomid+xxkroom+") order by 最大排课容量 asc";
	            		        		vec3=db.GetContextVector(sqlcls);
	            		        		if(vec3!=null&&vec3.size()>0){
	            		        			roomnum=vec3.get(0).toString();
	            		        			roomname=vec3.get(1).toString();
	            		        		}
	            		        	}else{
	            		        		String sqlcls="select 教室编号,教室名称,最大排课容量 from dbo.V_教室数据类 where 最大排课容量>='"+Integer.parseInt(vec2.get(0).toString())+"' and 教室类型代码='"+jslxdm+"' and 教室编号 not in (''"+selroomid+xxkroom+") order by 最大排课容量 asc";
	            		        		vec3=db.GetContextVector(sqlcls);
	            		        		if(vec3!=null&&vec3.size()>0){
	            		        			roomnum=vec3.get(0).toString();
	            		        			roomname=vec3.get(1).toString();
	            		        		}
	            		        	}
	            		        	if(roomnum.equals("")){
	            		        		flag=1;
	            		        	}
	            					rs+=roomnum+"+";
	            					rt+=roomname+"+";
	            				}else if(cdyq3[t].equals("1")){//多媒体教室
	            					jslxdm=1;
	            					String sqlnum="select 总人数  from V_基础信息_班级信息表 where 班级编号='"+MyTools.fixSql(this.GG_XZBDM)+"'";
	            		        	vec2=db.GetContextVector(sqlnum);
	            		        	String sqlrom="select 实际场地编号,授课周次详情 from dbo.V_排课管理_课程表明细详情表 where 时间序列 in ("+editsjxl+")";
	            		        	vec4=db.GetContextVector(sqlrom);
	            		        	
	            		        	
	            		        	String userooms="";
	            		        	if(vec2!=null&&vec2.size()>0&&vec4!=null&&vec4.size()>0){
	            		        		for(int m=0;m<vec4.size();m=m+2){
	            		        			//判断授课周次是否冲突  
	            		        			int res=checkskzc(skzc,vec4.get(m+1).toString());
	            		        			//System.out.println("vec4:-----"+skzc+"|"+vec4.get(m+1).toString()+"|"+res);
	            		        			//授课周次有冲突
	            		        			if(res==1){
	            		        				String useroom=vec4.get(m).toString();
		            		        			useroom=useroom.replaceAll("｜", "\\&").replaceAll("\\+", "\\&");
		            		        			
		            		        			String[] useroomnum=useroom.split("\\&");
		            		        			for(int n=0;n<useroomnum.length;n++){
		            		        				userooms+="'"+useroomnum[n]+"',";
		            		        			}
		            		        			
	            		        			}
	            		        		}
	            		        		if(userooms.equals("")){
	            		        			userooms="''";
	            		        		}else{
	            		        			userooms=userooms.substring(0, userooms.length()-1);
	            		        		}
	            		        		
	            		        		String sqlcls="select 教室编号,教室名称,最大排课容量 from dbo.V_教室数据类 where 最大排课容量>='"+Integer.parseInt(vec2.get(0).toString())+"' and 教室类型代码='"+jslxdm+"' and 教室编号 not in ("+userooms+selroomid+xxkroom+") order by 最大排课容量 asc";
	            		        		vec3=db.GetContextVector(sqlcls);
	            		        		if(vec3!=null&&vec3.size()>0){
	            		        			roomnum=vec3.get(0).toString();
	            		        			roomname=vec3.get(1).toString();
	            		        		}
	            		        	}else{
	            		        		String sqlcls="select 教室编号,教室名称,最大排课容量 from dbo.V_教室数据类 where 最大排课容量>='"+Integer.parseInt(vec2.get(0).toString())+"' and 教室类型代码='"+jslxdm+"' and 教室编号 not in ("+"''"+selroomid+xxkroom+") order by 最大排课容量 asc";
	            		        		vec3=db.GetContextVector(sqlcls);
	            		        		if(vec3!=null&&vec3.size()>0){
	            		        			roomnum=vec3.get(0).toString();
	            		        			roomname=vec3.get(1).toString();
	            		        		}
	            		        	}
	            		        	if(roomnum.equals("")){
	            		        		flag=1;
	            		        	}
	            		        	rs+=roomnum+"+";
	            		        	rt+=roomname+"+";
	            				}else{
	            					rs+=cdyq3[t]+"+";
	            					rt+=cdmc3[t]+"+";
	            				}
	            				selroomid+=",'"+rs.substring(0,rs.length()-1)+"'";
	            			}
	            			rs=rs.substring(0,rs.length()-1);
	            			rt=rt.substring(0,rt.length()-1);
	            			rs+="&";
	            			rt+="&";
	            		}
	            		rs=rs.substring(0,rs.length()-1);
	            		rt=rt.substring(0,rt.length()-1);
	            		rs+="｜";
	            		rt+="｜";
	            	}
	            	rs=rs.substring(0,rs.length()-1);
	            	rt=rt.substring(0,rt.length()-1);
	            	//System.out.println("rs:--"+rs);
	            	//System.out.println("rt:--"+rt);
	            	
	            	String ljxgbh="";
	            	if(editsjxl.indexOf(",")>-1){
	            		ljxgbh=editsjxl.replaceAll("\\'", "");
	                	ljxgbh=ljxgbh+",";
	                	ljxgbh=ljxgbh.replaceAll(sjxlArray[i]+",", "");
	                	ljxgbh=ljxgbh.substring(0, ljxgbh.length()-1);
	            	}
	            	//System.out.println("editsjxl:--"+ljxgbh);
	            	
	            		if(editsjxl.indexOf(sjxlArray[i])>-1){
	            			if (vec1 != null && vec1.size() > 0) {
			            		if(vec1.get(0)!=""){
			            			System.out.println("v:-----------------------------------------------"+v);
			    					maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec1.get(0))))+(v+1));
			    					this.setGG_BH(maxMxId);//设置主键
			    					v++;
			            		}else{
			            			this.setGG_BH(String.valueOf(v+1));//设置主键
			            			v++;
			            		}
			    			}else{
			    				this.setGG_BH(String.valueOf(v+1));//设置主键
			    				v++;
			    			}
	            			sql +=" insert into V_规则管理_固排禁排表(编号,类型,学年学期编码,行政班代码,时间序列,授课计划明细编号,预设场地编号,预设场地名称,连节相关编号,状态)" +
	                				"values('"+MyTools.StrFiltr(MyTools.fixSql(this.GG_BH))+"'," +
	                						"'"+MyTools.StrFiltr(MyTools.fixSql(this.GG_LX))+"'," +
	                						"'"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"'," +
	                						"'"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XZBDM))+"'," +
	                						"'"+sjxlArray[i]+"'," +
	                						"'"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"'," +
	                						"'"+MyTools.StrFiltr(MyTools.fixSql(rs))+"'," +
	                						"'"+MyTools.StrFiltr(MyTools.fixSql(rt))+"'," +
	                						"'"+MyTools.StrFiltr(MyTools.fixSql(ljxgbh))+"'," +
	                						"'1') ";
	            		}
		
	    		}
	    	}else if(savetype.equals("del")){
	    		String editlj="";
	    		if(this.GG_SKJHMXBH.equalsIgnoreCase("")){
	    			editlj=editsjxl;
	    		}else{
	    			String sqllj="select a.连节相关编号,b.连次 from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and a.授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"' and a.时间序列 in ("+editsjxl+")";
	        		vec3=db.GetContextVector(sqllj);
	        		if(vec3!=null&&vec3.size()>0){
	        			if(vec3.get(0).toString().equalsIgnoreCase("")){
	        				editlj=editsjxl;
	        			}else{
	        				String[] ljbh=vec3.get(0).toString().split(",");
	            			for(int i=0;i<ljbh.length;i++){
	            				editlj+="'"+ljbh[i]+"',";
	            			}
	            			editlj+=editsjxl;   			
	        			}		
	        		}else{
	        			editlj=editsjxl;
	        		}
	    		}
	    		
	    		sql +=" delete from V_规则管理_固排禁排表 " +
	        			"where 学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"' " +
	        			"and 行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XZBDM))+"' " +
	        			"and 授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"' " +
	        			"and 时间序列 in ("+editlj+")";
	    		//删除课程表
	    		String sql7 = "";
	    		Vector vec7 = null; // 结果集
	    		
	    		sql7="select 时间序列,班级排课状态,连节相关编号,授课计划明细编号,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,场地要求,场地名称,实际场地编号,实际场地名称,授课周次,授课周次详情,状态  from dbo.V_排课管理_课程表明细详情表 " +
	    			 "where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"%' " +
	        		 "and 时间序列 in ("+editlj+") "; 
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
	    						if(rs3[t].equals(this.GG_SKJHMXBH)){
	    							
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
	    					
//	    					if(vec7.get(i+3).toString().indexOf(vec8.get(i+3).toString())==0){//在第1个位置
//	    						result3=vec7.get(i+3).toString().replaceAll(vec8.get(i+3).toString()+"｜", "");
//	    						result4=vec7.get(i+4).toString().replaceAll(vec8.get(i+4).toString()+"｜", "");
//	    						result5=vec7.get(i+5).toString().replaceAll(vec8.get(i+5).toString()+"｜", "");
//	    						result6=vec7.get(i+6).toString().replaceAll(vec8.get(i+6).toString()+"｜", "");
//	    						result7=vec7.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result7=result7.replaceAll("!", "&").replaceAll("@", "+");
//	    						result8=vec7.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result8=result8.replaceAll("!", "&").replaceAll("@", "+");
//	    						result9=vec7.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result9=result9.replaceAll("!", "&").replaceAll("@", "+");
//	    						result10=vec7.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result10=result10.replaceAll("!", "&").replaceAll("@", "+");
//	    						result11=vec7.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result11=result11.replaceAll("!", "&").replaceAll("@", "+");
//	    						result12=vec7.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result12=result12.replaceAll("!", "&").replaceAll("@", "+");
//	    						result13=vec7.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result13=result13.replaceAll("!", "&").replaceAll("@", "+");
//	    						result14=vec7.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll(vec8.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@")+"｜", "");
//	    						result14=result14.replaceAll("!", "&").replaceAll("@", "+");
//	    					}else{
//	    						result3=vec7.get(i+3).toString().replaceAll("｜"+vec8.get(i+3).toString(), "");
//	    						result4=vec7.get(i+4).toString().replaceAll("｜"+vec8.get(i+4).toString(), "");
//	    						result5=vec7.get(i+5).toString().replaceAll("｜"+vec8.get(i+5).toString(), "");
//	    						result6=vec7.get(i+6).toString().replaceAll("｜"+vec8.get(i+6).toString(), "");
//	    						result7=vec7.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+7).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result7=result7.replaceAll("!", "&").replaceAll("@", "+");
//	    						result8=vec7.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+8).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result8=result8.replaceAll("!", "&").replaceAll("@", "+");
//	    						result9=vec7.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+9).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result9=result9.replaceAll("!", "&").replaceAll("@", "+");
//	    						result10=vec7.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+10).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result10=result10.replaceAll("!", "&").replaceAll("@", "+");
//	    						result11=vec7.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+11).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result11=result11.replaceAll("!", "&").replaceAll("@", "+");
//	    						result12=vec7.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+12).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result12=result12.replaceAll("!", "&").replaceAll("@", "+");
//	    						result13=vec7.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+13).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result13=result13.replaceAll("!", "&").replaceAll("@", "+");
//	    						result14=vec7.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@").replaceAll("｜"+vec8.get(i+14).toString().replaceAll("\\&", "!").replaceAll("\\+", "@"), "");
//	    						result14=result14.replaceAll("!", "&").replaceAll("@", "+");
//	    					}
	    					
	    					sql +=" update dbo.V_排课管理_课程表明细表 set 班级排课状态='"+MyTools.fixSql(vec7.get(i+1).toString())+"',连节相关编号='"+MyTools.fixSql(vec7.get(i+2).toString())+"',授课计划明细编号='"+MyTools.fixSql(result3)+"',实际场地编号='"+MyTools.fixSql(result11)+"',实际场地名称='"+MyTools.fixSql(result12)+"' " +
	    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"%' " +
	    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' ";  
	    					sql +=" update dbo.V_排课管理_课程表明细详情表 set 班级排课状态='"+MyTools.fixSql(vec7.get(i+1).toString())+"',连节相关编号='"+MyTools.fixSql(vec7.get(i+2).toString())+"',授课计划明细编号='"+MyTools.fixSql(result3)+"',课程代码='"+MyTools.fixSql(result4)+"',课程名称='"+MyTools.fixSql(result5)+"',课程类型='"+MyTools.fixSql(result6)+"',授课教师编号='"+MyTools.fixSql(result7)+"',授课教师姓名='"+MyTools.fixSql(result8)+"',场地要求='"+MyTools.fixSql(result9)+"',场地名称='"+MyTools.fixSql(result10)+"',实际场地编号='"+MyTools.fixSql(result11)+"',实际场地名称='"+MyTools.fixSql(result12)+"',授课周次='"+MyTools.fixSql(result13)+"',授课周次详情='"+MyTools.fixSql(result14)+"' " +
	    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"%' " +
	    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' ";
	    				}else{
	    					sql +=" update dbo.V_排课管理_课程表明细表 set 班级排课状态='',连节相关编号='',授课计划明细编号='',实际场地编号='',实际场地名称='' " +
	    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"%' " +
	    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' "; 
	    					sql +=" update dbo.V_排课管理_课程表明细详情表 set 班级排课状态='',连节相关编号='',授课计划明细编号='',课程代码='',课程名称='',课程类型='',授课教师编号='',授课教师姓名='',场地要求='',场地名称='',实际场地编号='',实际场地名称='',授课周次='',授课周次详情='' " +
	    		        			"where 授课计划明细编号  like '%"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"%' " +
	    		        			"and 时间序列 ='"+vec7.get(i).toString()+"' ";  
	    				}
	    			}
	    		}
	    	}else{
	    		
	    	}
        }
    	
        if(savetype.equals("addjin")){
        	this.setGG_XZBDM(xzbdm);
    		for (int i = 0; i < sjxlArray.length; i++) {
            	sql1="select max(cast(编号 as int)) from V_规则管理_固排禁排表";
            	vec1 = db.GetContextVector(sql1);
            	if(editsjxl.indexOf(sjxlArray[i])>-1){
        			if (vec1 != null && vec1.size() > 0) {
	            		if(vec1.get(0)!=""){
	    					maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec1.get(0))))+(v+1));
	    					this.setGG_BH(maxMxId);//设置主键
	    					v++;
	            		}else{
	            			this.setGG_BH(String.valueOf(v+1));//设置主键
	            			v++;
	            		}
	    			}else{
	    				this.setGG_BH(String.valueOf(v+1));//设置主键
	    				v++;
	    			}
        			sql +=" insert into V_规则管理_固排禁排表(编号,类型,学年学期编码,行政班代码,禁排类型,时间序列,授课计划明细编号,状态)" +
            				"values('"+MyTools.StrFiltr(MyTools.fixSql(this.GG_BH))+"'," +
            				"'3'," +
            				"'"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"'," +
            				"'"+MyTools.StrFiltr(MyTools.fixSql(this.JSBH))+"'," +
            				"'cd'," +
            				"'"+sjxlArray[i]+"'," +
            				"''," +
            				"'1') ";
    			}
    		}
        }else if(savetype.equals("deljin")){
        	sql +=" delete from V_规则管理_固排禁排表 " +
    				"where 学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"' " +
    				"and 行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.JSBH))+"' " +
    				"and 时间序列 in ("+editsjxl+") "+
    				"and 类型='3' " +
    				"and 授课计划明细编号='' ";     
//    		String editlj="";
//    		if(this.GG_SKJHMXBH.equalsIgnoreCase("")){
//    			editlj=editsjxl;
//    		}else{
//    			String sqllj="select a.连节相关编号,b.连次 from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and a.授课计划明细编号='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_SKJHMXBH))+"' and a.时间序列 in ("+editsjxl+")";
//        		vec3=db.GetContextVector(sqllj);
//        		if(vec3!=null&&vec3.size()>0){
//        			if(vec3.get(0).toString().equalsIgnoreCase("")){
//        				editlj=editsjxl;
//        			}else{
//        				String[] ljbh=vec3.get(0).toString().split(",");
//            			for(int i=0;i<ljbh.length;i++){
//            				editlj+="'"+ljbh[i]+"',";
//            			}
//            			editlj+=editsjxl;   			
//        			}		
//        		}else{
//        			editlj=editsjxl;
//        		}
//    		}
//    		
//    		sql +=" delete from V_规则管理_固排禁排表  where 授课计划明细编号 in "+
//    				"(select b.授课计划明细编号 from V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and a.学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"' and a.时间序列 in ("+editlj+") and a.类型='3' and b.场地要求 like '%"+this.JSBH+"%') "+
//    				"and 学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"' and 时间序列 in ("+editlj+") and 类型='3' ";
    	}else{
    		
    	}	
        
        //天山二中
//        if(savetype.equals("addjin")){
//        	this.setGG_XZBDM(xzbdm);
//    		for (int i = 0; i < sjxlArray.length; i++) {
//            	sql1="select max(cast(编号 as int)) from V_规则管理_固排禁排表";
//            	vec1 = db.GetContextVector(sql1);
//            	if(editsjxl.indexOf(sjxlArray[i])>-1){
//        			if (vec1 != null && vec1.size() > 0) {
//	            		if(vec1.get(0)!=""){
//	    					maxMxId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec1.get(0))))+(v+1));
//	    					this.setGG_BH(maxMxId);//设置主键
//	    					v++;
//	            		}else{
//	            			this.setGG_BH(String.valueOf(v+1));//设置主键
//	            			v++;
//	            		}
//	    			}else{
//	    				this.setGG_BH(String.valueOf(v+1));//设置主键
//	    				v++;
//	    			}
//        			sql +=" insert into V_规则管理_固排禁排表(编号,类型,学年学期编码,行政班代码,时间序列,授课计划明细编号,状态)" +
//            				"values('"+MyTools.StrFiltr(MyTools.fixSql(this.GG_BH))+"'," +
//            						"'3'," +
//            						"'"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"'," +
//            						"'"+MyTools.StrFiltr(MyTools.fixSql(this.JSBH))+"'," +
//            						"'"+sjxlArray[i]+"'," +
//            						"''," +
//            						"'1') ";
//    			}
//    		}
//        }else if(savetype.equals("deljin")){
// 
//    		sql +=" delete from V_规则管理_固排禁排表 " +
//    				"where 学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.GG_XNXQBM))+"' " +
//    				"and 行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.JSBH))+"' " +
//    				"and 时间序列 in ("+editsjxl+") "+
//    				"and 类型='3' " +
//    				"and 授课计划明细编号='' ";
//    	}else{
//    		
//    	}	
		
        if(flag==1){
			this.setMSG("没有可以使用的教室");
        }else{
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
        }
	}
	
	//判断授课周次是否有冲突
	public int checkskzc(String skzc1,String skzc2) throws SQLException {
			String sql = "";
			int result=0;
			String orgskzc="";
			String chkskzc="";
			
			if(skzc1.indexOf("｜")>-1){
				String[] fgzc=skzc1.split("｜");
				for(int k=0;k<fgzc.length;k++){
					if(fgzc[k].indexOf("&")>-1){
						String[] cfzc=fgzc[k].split("&");
						for(int j=0;j<cfzc.length;j++){
							if(cfzc[j].indexOf("-")>-1){
								for(int i=Integer.parseInt(cfzc[j].split("-")[0]);i<=Integer.parseInt(cfzc[j].split("-")[1]);i++){
									orgskzc+=i+"#";
								}
							}else{
								orgskzc+=cfzc[j]+"#";
							}
						}
						orgskzc.substring(0, orgskzc.length()-1);
					}else{
						if(fgzc[k].indexOf("-")>-1){
							String[] znum=fgzc[k].split("-");
							for(int i=Integer.parseInt(znum[0]);i<=Integer.parseInt(znum[1]);i++){
								orgskzc+=i+"#";
							}	
						}else{
							orgskzc+=skzc1+"#";
						}
						orgskzc.substring(0, orgskzc.length()-1);
					}
				}
			}else{
				if(skzc1.indexOf("&")>-1){
					String[] cfzc=skzc1.split("&");
					for(int j=0;j<cfzc.length;j++){
						if(cfzc[j].indexOf("-")>-1){
							for(int i=Integer.parseInt(cfzc[j].split("-")[0]);i<=Integer.parseInt(cfzc[j].split("-")[1]);i++){
								orgskzc+=i+"#";
							}
						}else{
							orgskzc+=cfzc[j]+"#";
						}
					}
					orgskzc.substring(0, orgskzc.length()-1);
				}else{
					if(skzc1.indexOf("-")>-1){
						String[] znum=skzc1.split("-");
						for(int i=Integer.parseInt(znum[0]);i<=Integer.parseInt(znum[1]);i++){
							orgskzc+=i+"#";
						}	
					}else{
						orgskzc+=skzc1+"#";
					}
					orgskzc.substring(0, orgskzc.length()-1);
				}
			}
			
			if(skzc2.indexOf("｜")>-1){
				String[] fgzc=skzc2.split("｜");
				for(int k=0;k<fgzc.length;k++){
					if(fgzc[k].indexOf("&")>-1){
						String[] cfzc=fgzc[k].split("&");
						for(int j=0;j<cfzc.length;j++){
							if(cfzc[j].indexOf("-")>-1){
								for(int i=Integer.parseInt(cfzc[j].split("-")[0]);i<=Integer.parseInt(cfzc[j].split("-")[1]);i++){
									chkskzc+=i+"#";
								}					
							}else{
								chkskzc+=cfzc[j]+"#";
							}	
						}	
						chkskzc.substring(0, chkskzc.length()-1);
					}else{ 
						if(fgzc[k].indexOf("-")>-1){
							String[] znum=fgzc[k].split("-");
							for(int i=Integer.parseInt(znum[0]);i<=Integer.parseInt(znum[1]);i++){
								chkskzc+=i+"#";
							}
						}else{
							chkskzc+=skzc2+"#";
						}
						chkskzc.substring(0, chkskzc.length()-1);
					}
				}
			}else{
				if(skzc2.indexOf("&")>-1){
					String[] cfzc=skzc2.split("&");
					for(int j=0;j<cfzc.length;j++){
						if(cfzc[j].indexOf("-")>-1){
							for(int i=Integer.parseInt(cfzc[j].split("-")[0]);i<=Integer.parseInt(cfzc[j].split("-")[1]);i++){
								chkskzc+=i+"#";
							}					
						}else{
							chkskzc+=cfzc[j]+"#";
						}	
					}	
					chkskzc.substring(0, chkskzc.length()-1);
				}else{ 
					if(skzc2.indexOf("-")>-1){
						String[] znum=skzc2.split("-");
						for(int i=Integer.parseInt(znum[0]);i<=Integer.parseInt(znum[1]);i++){
							chkskzc+=i+"#";
						}
					}else{
						chkskzc+=skzc2+"#";
					}
					chkskzc.substring(0, chkskzc.length()-1);
				}
			}
			System.out.println(orgskzc+"*"+chkskzc);
			
			String[] spskzc=orgskzc.split("#");
			String[] sqskzc=chkskzc.split("#");
			for(int i=0;i<sqskzc.length;i++){
				for(int j=0;j<spskzc.length;j++){
					if(spskzc[j].equals(sqskzc[i])){
						result=1;
					}
				}	
			}
			
			return result;
	}
	
	
	//GET && SET 方法
	public String getXNXQ() {
		return XNXQ;
	}

	public String getGG_BH() {
		return GG_BH;
	}

	public void setGG_BH(String gG_BH) {
		GG_BH = gG_BH;
	}

	public String getGG_LX() {
		return GG_LX;
	}

	public void setGG_LX(String gG_LX) {
		GG_LX = gG_LX;
	}

	public String getGG_XNXQBM() {
		return GG_XNXQBM;
	}

	public void setGG_XNXQBM(String gG_XNXQBM) {
		GG_XNXQBM = gG_XNXQBM;
	}

	public String getGG_XZBDM() {
		return GG_XZBDM;
	}

	public void setGG_XZBDM(String gG_XZBDM) {
		GG_XZBDM = gG_XZBDM;
	}

	public String getGG_SJXL() {
		return GG_SJXL;
	}

	public void setGG_SJXL(String gG_SJXL) {
		GG_SJXL = gG_SJXL;
	}

	public String getGG_SKJHMXBH() {
		return GG_SKJHMXBH;
	}

	public void setGG_SKJHMXBH(String gG_SKJHMXBH) {
		GG_SKJHMXBH = gG_SKJHMXBH;
	}

	public String getGG_LJXGBH() {
		return GG_LJXGBH;
	}

	public void setGG_LJXGBH(String gG_LJXGBH) {
		GG_LJXGBH = gG_LJXGBH;
	}

	public String getGG_ZT() {
		return GG_ZT;
	}

	public void setGG_ZT(String gG_ZT) {
		GG_ZT = gG_ZT;
	}

	public void setXNXQ(String xNXQ) {
		XNXQ = xNXQ;
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

	public String getKCJS() {
		return KCJS;
	}

	public void setKCJS(String kCJS) {
		KCJS = kCJS;
	}

	public String getJXXZ() {
		return JXXZ;
	}

	public void setJXXZ(String jXXZ) {
		JXXZ = jXXZ;
	}

	public String getSKJHMXBH() {
		return SKJHMXBH;
	}

	public void setSKJHMXBH(String sKJHMXBH) {
		SKJHMXBH = sKJHMXBH;
	}

	public String getQCXX() {
		return QCXX;
	}

	public void setQCXX(String qCXX) {
		QCXX = qCXX;
	}

	public String getLCS() {
		return LCS;
	}

	public void setLCS(String lCS) {
		LCS = lCS;
	}

	public String getAOD() {
		return AOD;
	}

	public void setAOD(String aOD) {
		AOD = aOD;
	}

	public String getJSBH() {
		return JSBH;
	}

	public void setJSBH(String jSBH) {
		JSBH = jSBH;
	}

	public String getJSXM() {
		return JSXM;
	}

	public void setJSXM(String jSXM) {
		JSXM = jSXM;
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

	public String getMSG2() {
		return MSG2;
	}

	public void setMSG2(String mSG2) {
		MSG2 = mSG2;
	}

	public String getMSG3() {
		return MSG3;
	}

	public void setMSG3(String mSG3) {
		MSG3 = mSG3;
	}

	public String getMSG4() {
		return MSG4;
	}

	public void setMSG4(String mSG4) {
		MSG4 = mSG4;
	}

	public String getMSG5() {
		return MSG5;
	}

	public void setMSG5(String mSG5) {
		MSG5 = mSG5;
	}
	
	
	
	
	
}
