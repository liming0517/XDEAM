package com.pantech.devolop.courseManage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
/*
@date 2015.05.27
@author yeq
模块：M2.1排课设置
说明:
重要及特殊方法：
*/
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.PublicTools;

public class PkszBean {
	private String USERCODE;//用户编号
	private String Auth;//用户权限
	private String PK_KCBZBBH; //课程表主表编号
	private String PK_XNXQBM; //学年学期编码
	private String PK_ZYDM; //专业代码
	private String PK_XZBDM; //行政班代码
	private String PK_BJPKZT; //班级排课状态
	private String PK_KCBMXBH; //课程表明细编号
	private String PK_SJXL; //时间序列
	private String PK_LJXGBH; //连节相关编号
	private String PK_SKJHMXBH; //授课计划明细编号
	private String PK_SJCDBH; //实际场地编号
	private String PK_SJCDMC; //实际场地名称
	private String PK_BZ; //备注
	private String PK_CJR; //创建人
	private String PK_CJSJ; //创建时间
	private String PK_ZT; //状态
	
	private String PK_XB;//系部
	
	private String PT_ID; //编号
	private String PT_LX; //类型
	private String PT_XNXQBM; //学年学期编码
	private String PT_XZBDM; //行政班代码
	private String PT_XH; //学号
	private String PT_KCBMXBH; //相关课程表编号
	private String PT_SJXL; //时间序列
	private String PT_KCBH; //课程编号
	private String PT_KCMC; //课程名称
	private String PT_SKJSBH; //授课教师编号
	private String PT_SKJSMC; //授课教师名称
	private String PT_CDBH; //场地编号
	private String PT_CDMC; //场地名称
	private String PT_SKZC; //授课周次
	private String PT_KSXS; //考试形式
	private String PT_XF; //学分
	private String PT_ZKS; //总课时
	private String PT_CJR; //创建人
	private String PT_CJSJ; //创建时间
	private String PT_ZT; //状态
	
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public PkszBean(HttpServletRequest request) {
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
		Auth = "";//用户权限
		PK_KCBZBBH = ""; //课程表主表编号
		PK_XNXQBM = ""; //学年学期编码
		PK_ZYDM = ""; //专业代码
		PK_XZBDM = ""; //行政班代码
		PK_BJPKZT = ""; //班级排课状态
		PK_KCBMXBH = ""; //课程表明细编号
		PK_SJXL = ""; //时间序列
		PK_LJXGBH = ""; //连节相关编号
		PK_SKJHMXBH = ""; //授课计划明细编号
		PK_SJCDBH = ""; //实际场地编号
		PK_SJCDMC = ""; //实际场地名称
		PK_BZ = ""; //备注
		PK_CJR = ""; //创建人
		PK_CJSJ = ""; //创建时间
		PK_ZT = ""; //状态
		
		PK_XB = "";//系部
		
		PT_ID = ""; //编号
		PT_LX = ""; //类型
		PT_XNXQBM = ""; //学年学期编码
		PT_XZBDM = ""; //行政班代码
		PT_XH = ""; //学号
		PT_KCBMXBH = ""; //相关课程表编号
		PT_SJXL = ""; //时间序列
		PT_KCBH = ""; //课程编号
		PT_KCMC = ""; //课程名称
		PT_SKJSBH = ""; //授课教师编号
		PT_SKJSMC = ""; //授课教师名称
		PT_CDBH = ""; //场地编号
		PT_CDMC = ""; //场地名称
		PT_SKZC = ""; //授课周次
		PT_KSXS = ""; //考试形式
		PT_XF = ""; //学分
		PT_ZKS = ""; //总课时
		PT_CJR = ""; //创建人
		PT_CJSJ = ""; //创建时间
		PT_ZT = ""; //状态
	}
	
	/**
	 * 分页查询 学年学期课程表列表
	 * @date:2015-05-27
	 * @author:yeq
	 * @param pageNum 页数
	 * @param page 每页数据条数
	 * @param PK_XNXQMC_CX 学年学期名称
	 * @param PK_JXXZ_CX 教学性质
	 * @param PK_TJZT_CX 提交状态
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSemesterList(int pageNum, int page, String PK_XNXQMC_CX, String PK_JXXZ_CX, String PK_ZY_CX, String PK_TJZT_CX) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		Vector tempVec = null;
		String existKCB = "";
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长@211@
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任@212@
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任@213@
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理@214@
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理@215@
		
		sql = "select distinct a.学年学期编码 as PK_XNXQBM,b.学年学期名称 as PK_XNXQMC,c.教学性质 as jxxz ";
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += "d.专业代码 as PK_ZYDM,e.专业名称+'('+d.专业代码+')' as PK_ZYMC,";
//		}
//		sql += "case when a.提交状态='0' then a.提交状态 else '1' end as PK_TJZT " +
		sql +=" from V_排课管理_课程表主表 a " +
			"left join V_规则管理_学年学期表 b on a.学年学期编码=b.学年学期编码 " +
			"left join V_基础信息_教学性质 c on c.编号=b.教学性质 " +
			//"left join V_学校班级_数据子类 d on d.行政班代码=a.行政班代码 " +
			//"left join V_专业基本信息数据子类 e on e.专业代码=d.专业代码 " +
			"where a.状态='1'";
		
		//判断权限,管理员和排公共课的老师可看到所有专业,排专业课的老师只可以看到自己的专业
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and d.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
//			sql += " and a.提交状态='2'";
//		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(PK_XNXQMC_CX)){
			sql += " and b.学年学期名称 like '%" + MyTools.fixSql(PK_XNXQMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(PK_JXXZ_CX)){
			sql += " and b.教学性质='" + MyTools.fixSql(PK_JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(PK_ZY_CX)){
			sql += " and d.专业代码='" + MyTools.fixSql(PK_ZY_CX) + "'";
		}
//		if(!"".equalsIgnoreCase(PK_TJZT_CX)){
//			if("0".equalsIgnoreCase(PK_TJZT_CX)){
//				sql += " and a.提交状态='" + MyTools.fixSql(PK_TJZT_CX) + "'";
//			}else{
//				sql += " and a.提交状态<>'0'";
//			}
//			
//		}
		sql += " order by a.学年学期编码 desc";
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += ",e.专业名称+'('+d.专业代码+')'";
//		}
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		
		//查询已存在课表情况
//		sql = "select distinct a.学年学期编码,b.专业代码,b.系部代码,a.提交状态 " +
		sql = "select distinct a.学年学期编码,b.系部代码,a.提交状态 " +
			"from V_排课管理_课程表主表 a " +
			//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
			"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
			"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
			"where a.状态='1'";
		
		//判断权限,管理员和排公共课的老师可看到所有专业,排专业课的老师只可以看到自己的专业
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and b.专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
//		}
		tempVec = db.GetContextVector(sql);
		
		if(tempVec!=null && tempVec.size()>0){
			for(int i=0; i<tempVec.size(); i++){
				existKCB += MyTools.StrFiltr(tempVec.get(i))+",";
			}
		}
		if(existKCB.length() > 0){
			existKCB = existKCB.substring(0, existKCB.length()-1);
		}
		vec.add(existKCB);
		
		return vec;
	}
	
	/**
	 * 查询当前学期已有课程表的班级信息
	 * @date:2015-05-27
	 * @author:yeq
	 * @param level
	 * @param parentCode 专业代码
	 * @return 班级信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queClassTree(String level, String parentCode)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		Vector vec2 = null;
		String xbdm = ""; //系部代码
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课教师
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长@211@
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任@212@
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任@213@
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理@214@
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理@215@
		
//		sql = " select 系部代码 from dbo.V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "'";
//		vec2 = db.GetContextVector(sql);
//		if(vec2.size() > 0){
//			for (int i = 0; i < vec2.size(); i++) {
//				xbdm += "'" + vec2.get(i) + "',"; 
//			}
//			xbdm = xbdm.substring(0,xbdm.length()-1);
//		}
		
		//获取专业目录
		if("0".equalsIgnoreCase(level)){
//			sql = "select distinct c.系部代码 as id,c.系部名称+'('+c.系部名称+')' as text,state='closed' from V_排课管理_课程表主表 a " +
			sql = "select distinct c.系部代码 as id,c.系部名称 as text,state='closed' from V_排课管理_课程表主表 a " +
				//" left join (select 行政班代码,系部代码 from V_学校班级_数据子类 union all select 教学班编号,系部代码 from V_基础信息_教学班信息表) b on b.行政班代码=a.行政班代码 " +
				" left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
				" left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 " +
				" where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"'";
			
			if(this.getAuth().indexOf(xbjwgl) > -1 || this.getAuth().indexOf(xbjdzr) > -1){
				//sql += " and c.系部代码 ='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
				sql += " and c.系部代码  in ( select a.系部代码 from dbo.V_基础信息_系部教师信息表 a where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += " order by text";
		}
		//获得班级子节点
		if(level.equalsIgnoreCase("1")){
			sql = "select distinct a.行政班代码 as id,b.班级名称 as text,state='open' from V_排课管理_课程表主表 a " +
				//"left join (select 行政班代码,行政班名称,系部代码 from V_学校班级_数据子类 union all select 教学班编号,教学班名称,系部代码 from V_基础信息_教学班信息表) b on b.行政班代码=a.行政班代码 " +
				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
				"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) +"' " +
				"and c.系部代码='" + MyTools.fixSql(parentCode) + "' order by a.行政班代码";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询当前学年设置的每周天数和每天节数等相关信息
	 * @date:2015-05-28
	 * @author:yeq
	 * @return 课程表相关信息
	 * @throws SQLException
	 */
	public Vector initBlankKcb()throws SQLException{
		Vector vec = null;
		Vector result = new Vector();
		String sql = "";
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		sql = "select t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
				"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间," +
				"case when convert(nvarchar(10),t1.排课截止时间,21) >= convert(nvarchar(10),getDate(),21) then '0' else '1' end as 是否过期 " +
				"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		result.add(vec);
		
		sql = "select top 1 (select count(*) from V_规则管理_授课计划明细表 b " +
			"left join V_规则管理_授课计划主表 c on b.授课计划主表编号=c.授课计划主表编号 " +
			"where b.状态='1' and a.班级编号=c.行政班代码 and c.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') as 课程数 from V_基础信息_班级信息表 a";
		if(this.getAuth().indexOf(majorTeacher) > -1){
			sql += " where a.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
		}
		sql += " order by 课程数 desc";
		vec = db.GetContextVector(sql);
		result.add(vec);
		
		//查询学期总周次数
		sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		result.add(MyTools.StrFiltr(vec.get(0)));
		
		return result;
	}
	
	/**
	 * 查询班级课程及未排课程
	 * @date:2015-05-30
	 * @author:yeq
	 * @return 班级课程及未排课程信息
	 * @throws SQLException
	 */
	public Vector loadClassKcb()throws SQLException{
		Vector resultVec = new Vector();
		String sql = "";
		Vector tempVec = null;
		String jsonStr = "";
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		//查询课程表
		sql = "select 课程表明细编号,班级排课状态,时间序列,授课计划明细编号,课程名称,课程类型,授课教师姓名,授课教师编号,实际场地编号,实际场地名称,授课周次,授课周次详情 " +
			"from V_排课管理_课程表明细详情表 " +
			"where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and 行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
				
		
		//添加体锻课  lupengfei 20170928
//		//查询课程表
//		String sqlkcb = "select 课程表明细编号,班级排课状态,时间序列,授课计划明细编号,课程名称,课程类型,授课教师姓名,授课教师编号,实际场地编号,实际场地名称,授课周次,授课周次详情 " +
//			"from V_排课管理_课程表明细详情表 " +
//			"where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//			"and 行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
//		Vector veckcb=db.GetContextVector(sqlkcb);
//		
//		int tytag=0;//体育课标记
//		int tdcs=0;//体锻课次数
//		
//		//获取操场编号
//		String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and [教室名称]='操场' ) ";
//		Vector veccc=db.GetContextVector(sqlcc);
//		
//		//获取实际上课周数
//		String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
//		Vector vecskzs=db.GetContextVector(sqlskzs);
//			
//		//获取每周天数
//		int mzts=0;
//		String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
//		Vector vecmzts=db.GetContextVector(sqlmzts);
//		if(vecmzts!=null&&vecmzts.size()>0){
//			mzts=Integer.parseInt(vecmzts.get(0).toString());
//		}
//		
//		//判断哪天没体育课,中午添加体锻课
//		String ts="";
//		for(int i=1;i<=mzts;i++){
//			tytag=0;
//			if(i<10){
//				ts="0"+i;
//			}else{
//				ts=i+"";
//			}
//			for(int j=0;j<veckcb.size();j=j+12){
//				if(ts.equals(veckcb.get(j+2).toString().substring(0, 2))){//天数相同
//					if(veckcb.get(j+4).toString().indexOf("体育")>-1){//有体育课
//						tytag=1;
//					}
//				}	
//			}
//			if(tytag==0){
//				for(int k=0;k<veckcb.size();k=k+12){
//					if((ts+"05").equals(veckcb.get(k+2).toString())&&veckcb.get(k+1).toString().equals("")){//中午没有课
//						if(tdcs<2){
//							//添加体锻课
//							//课程表明细编号,班级排课状态,时间序列,授课计划明细编号,课程名称,课程类型,授课教师姓名,授课教师编号,实际场地编号,实际场地名称,授课周次,授课周次详情
//							veckcb.setElementAt("2", k+1);//班级排课状态
//							veckcb.setElementAt("SKJHMXBH_TD", k+3);//授课计划明细编号
//							veckcb.setElementAt("体锻", k+4);//课程名称
//							veckcb.setElementAt(veccc.get(0).toString(), k+8);//实际场地编号
//							veckcb.setElementAt("操场", k+9);//实际场地名称
//							veckcb.setElementAt("1-"+vecskzs.get(0).toString(), k+10);//授课周次
//							veckcb.setElementAt("1-"+vecskzs.get(0).toString(), k+11);//授课周次详情
//							
//							tdcs++;
//						}
//					}
//				}
//			}
//		}
//		
//		String editkcb="";
//		for(int m=0;m<veckcb.size();m=m+12){
//			editkcb+="{\"课程表明细编号\":\""+veckcb.get(m).toString()+"\",\"班级排课状态\":\""+veckcb.get(m+1).toString()+"\",\"时间序列\":\""+veckcb.get(m+2).toString()+"\",\"授课计划明细编号\":\""+veckcb.get(m+3).toString()+"\",\"课程名称\":\""+veckcb.get(m+4).toString()+"\",\"课程类型\":\""+veckcb.get(m+5).toString()+"\",\"授课教师姓名\":\""+veckcb.get(m+6).toString()+"\",\"授课教师编号\":\""+veckcb.get(m+7).toString()+"\",\"实际场地编号\":\""+veckcb.get(m+8).toString()+"\",\"实际场地名称\":\""+veckcb.get(m+9).toString()+"\",\"授课周次\":\""+veckcb.get(m+10).toString()+"\",\"授课周次详情\":\""+veckcb.get(m+11).toString()+"\"},";
//		}
//		if(!editkcb.equals("")){
//			editkcb="["+editkcb.substring(0, editkcb.length()-1)+"]";
//		}else{
//			editkcb="[]";
//		}
//		
//		//editkcb="[0, rows, " + editkcb + "]";
//		//System.out.println(db.getConttexJONSArr(sql, 0, 0));
//		//System.out.println(editkcb);
//		//resultVec.add(db.getConttexJONSArr(sql, 0, 0));
//		resultVec.add(editkcb);
		
		//============================================================================================================================
		
		//查询未排课程信息
		sql = "select distinct a.授课计划明细编号,a.课程名称,a.授课教师姓名,a.授课教师编号,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情,a.节数-a.实际已排节数 as 剩余节数 from V_规则管理_授课计划明细表 a " +
			"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
			"where a.状态='1' and b.状态='1' " +
//			"and a.节数-a.实际已排节数>0 " +
			"and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and b.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		//判断是公共课还是专业课
//		if(this.getAuth().indexOf(pubTeacher) > -1)
//			sql += " and a.课程类型='01'";
//		if(this.getAuth().indexOf(majorTeacher) > -1)
//			sql += " and a.课程类型='02'";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));

		//查询课表备注
		sql = "select distinct a.备注,isnull(b.总人数,0) from V_排课管理_课程表主表 a " +
//			"left join (select 行政班代码,总人数 from V_学校班级_数据子类 " +
//			"union all " +
//			"select distinct a.教学班编号,a.系部代码,count(b.学号) from V_基础信息_教学班信息表 a " +
//			"left join V_基础信息_教学班学生信息表 b on b.教学班编号=a.教学班编号 group by a.教学班编号,a.系部代码) b on a.行政班代码=b.行政班代码 " +
			"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
			"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and a.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			resultVec.add(MyTools.StrFiltr(tempVec.get(0)));
			resultVec.add(MyTools.StrFiltr(tempVec.get(1)));
		}else{
			resultVec.add("");
			resultVec.add("0");
		}
		
		//查询已进行过调课操作的时间序列
		String order = "";
		String day = "";
		String orderArray[] = new String[0];
		String tempOrder = "";
		
		sql = "select 原计划星期,原计划时间序列,调整后星期,调整后时间序列 from V_调课管理_调课信息主表 where 审核状态='2' " +
			"and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 班级编号='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			for(int i=0; i<tempVec.size(); i+=4){
				day = MyTools.StrFiltr(tempVec.get(i));
				orderArray = MyTools.StrFiltr(tempVec.get(i+1)).split(",");
				
				for(int j=0; j<orderArray.length; j++){
					tempOrder = day+orderArray[j];
					if(!"".equalsIgnoreCase(tempOrder) && order.indexOf(tempOrder)<0){
						order += tempOrder+",";
					}
				}
				
				day = MyTools.StrFiltr(tempVec.get(i+2));
				orderArray = MyTools.StrFiltr(tempVec.get(i+3)).split(",");
				
				for(int j=0; j<orderArray.length; j++){
					tempOrder = day+orderArray[j];
					if(!"".equalsIgnoreCase(tempOrder) && order.indexOf(tempOrder)<0){
						order += tempOrder+",";
					}
				}
			}
		}
		if(order.length() > 0)
			order = order.substring(0, order.length()-1);
		resultVec.add(order);
		
		return resultVec;
	}
	
	/**
	 * 查询教师和教室已用时间信息
	 * @date:2015-06-20
	 * @author:yeq
	 * @param type 查询类型
	 * @param teaCondition 教师查询条件
	 * @return 教师和教室已用时间信息
	 * @throws SQLException
	 */
	public Vector queUsedOrderInfo(String type, String teaCondition)throws SQLException{
		Vector resultVec = new Vector();
		String sql = "";
		Vector vec = null;
		String tempTea = "";
		String tempSite = "";
		String siteMc = "";
		String siteType = "";
		String siteStuNum = "";
		String tempOrder = "";
		Vector tempVec = new Vector();
		String code = "";
		String tempCodeArray[] = new String[0];
		String teaCode = "";
		String teaName = "";
		String siteCode = "";
		String siteName = "";
		String courseName = "";
		String className = "";
		String week = "";
		String usedInfo = "";
		String jsonStr = "";
		
		String admin = MyTools.getProp(request, "Base.admin");//公共课
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		
		//查询当前班级所有任课教师的当前学期授课时间序列信息（不包含本班级）
		if("all".equalsIgnoreCase(type) || "tea".equalsIgnoreCase(type)){
			String teaList = "";
			
			//查询教师名单
			sql = "select distinct * from (select t.工号 from V_教职工基本数据子类 t " +
				"inner join V_规则管理_授课计划明细表 t1 on '@'+replace(replace(t1.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+t.工号+'@%' " +
				"inner join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 " +
				"where t1.状态='1' and t2.状态='1' and t2.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
				"and t2.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "' " +
				"union all " +
				"select t.工号 from V_教职工基本数据子类 t " +
				"inner join V_排课管理_课程表明细详情表 t1 on '@'+replace(replace(replace(replace(t1.授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@'+t.工号+'@%' " +
				"where t1.状态='1' and t1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
				"and t1.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "') as t";
			vec = db.GetContextVector(sql);
			for(int i=0; i<vec.size(); i++){
				teaList += MyTools.StrFiltr(vec.get(i))+",";
			}
			teaList = teaList.substring(0, teaList.length()-1);
			
			sql = "select distinct * from (" +
				"select 工号,'' as 时间序列,'' as 授课计划明细编号,'' as 授课教师编号,'' as 授课教师姓名,'' as 课程名称,'' as 行政班名称,'' as 授课周次详情 from V_教职工基本数据子类 " +
				"where 工号 in (" + "'"+teaList.replaceAll(",", "','")+"'" + ") " +
				"union all " +
				"select a.工号,b.时间序列,b.授课计划明细编号,b.授课教师编号,b.授课教师姓名,b.课程名称,b.行政班名称,b.授课周次详情 " +
				"from V_教职工基本数据子类 a " +
				"inner join V_排课管理_课程表明细详情表 b on '@'+replace(replace(replace(replace(b.授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@'+a.工号+'@%' " +
				"where b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
				"and a.工号 in (" + "'"+teaList.replaceAll(",", "','")+"'" + ") " +
				"and b.行政班代码<>'" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
			//判断如果是公共课教师或管理员，需要获取专业课固排禁排教师信息(不包含本班级)
			//if(this.getAuth().indexOf(pubTeacher)>-1 || this.getAuth().indexOf(admin)>-1){
				sql += " union all " +
					"select a.工号,e.时间序列,b.授课计划明细编号,b.授课教师编号,b.授课教师姓名,b.课程名称,d.班级名称,b.授课周次详情 from V_教职工基本数据子类 a " +
					"inner join V_规则管理_授课计划明细表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' " +
					"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
					//"left join (select 行政班代码,行政班名称 from V_学校班级_数据子类 union all select 教学班编号,教学班名称 from V_基础信息_教学班信息表) d on d.行政班代码=c.行政班代码 " +
					"left join V_基础信息_班级信息表 d on d.班级编号=c.行政班代码 " +
					"inner join V_规则管理_固排禁排表 e on e.授课计划明细编号=b.授课计划明细编号 " +
					"where b.状态='1' and e.状态='1' and e.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
					"and c.行政班代码<>'" + MyTools.fixSql(this.getPK_XZBDM()) + "' " +
					"and e.类型='2' and (b.节数-b.实际已排节数)>0"; //20160919/yeq添加条件，避免发生在排课设置课表中更换教室后，该教师无法被使用";
				//教师禁排
				sql += " union all " +
					"select a.工号,b.时间序列,'教师禁排' as 授课计划明细编号,a.工号,a.姓名,'教师禁排' as 课程名称,'教师禁排' as 行政班名称,'教师禁排' as 授课周次详情 from V_教职工基本数据子类 a " +
					"inner join V_规则管理_固排禁排表 b on a.工号 = b.行政班代码 " +
					"where b.状态='1' and b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
					"and b.类型='3' and b.禁排类型='js' ";
			//}
			sql += ") as z";
			if(!"".equalsIgnoreCase(teaCondition)){
				sql += " where 工号 in (" + teaCondition + ")";
			}
			sql += " order by z.工号,z.时间序列";
			vec = db.GetContextVector(sql);
			
			jsonStr = "[";
			for(int i=0; i<vec.size(); i+=8){
				//判断是否同一教师
				if(tempTea.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					//判断是否同一时间序列
					if(tempOrder.equalsIgnoreCase((MyTools.StrFiltr(vec.get(i+1))))){
						tempVec.add(MyTools.StrFiltr(vec.get(i+2)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+3)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+4)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
					}else{
						if(i > 0){
							for(int j=0; j<tempVec.size(); j+=6){
								code += MyTools.StrFiltr(tempVec.get(j))+"｜";
								teaCode += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
								teaName += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
								courseName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
								tempCodeArray = MyTools.StrFiltr(tempVec.get(j)).split("｜");
								for(int k=0; k<tempCodeArray.length; k++){
									className += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
								}
								week += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
							}
							tempVec.clear();
							
							if(code.length() > 0){
								code = code.substring(0, code.length()-1);
								teaCode = teaCode.substring(0, teaCode.length()-1);
								teaName = teaName.substring(0, teaName.length()-1);
								courseName = courseName.substring(0, courseName.length()-1);
								className = className.substring(0, className.length()-1);
								week = week.substring(0, week.length()-1);
							}
							if(!"".equalsIgnoreCase(code)){
								usedInfo += tempOrder+":"+code+"@"+teaCode+"@"+teaName+"@"+courseName+"@"+className+"@"+week+",";
							}
							
							code = "";
							teaCode = "";
							teaName = "";
							courseName = "";
							className = "";
							week = "";
						}
						
						tempOrder = MyTools.StrFiltr(vec.get(i+1));
						tempVec.add(MyTools.StrFiltr(vec.get(i+2)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+3)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+4)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
					}
				}else{
					if(i > 0){
						for(int j=0; j<tempVec.size(); j+=6){
							code += MyTools.StrFiltr(tempVec.get(j))+"｜";
							teaCode += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
							teaName += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
							courseName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
							tempCodeArray = MyTools.StrFiltr(tempVec.get(j)).split("｜");
							for(int k=0; k<tempCodeArray.length; k++){
								className += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
							}
							week += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
						}
						tempVec.clear();
						
						if(code.length() > 0){
							code = code.substring(0, code.length()-1);
							teaCode = teaCode.substring(0, teaCode.length()-1);
							teaName = teaName.substring(0, teaName.length()-1);
							courseName = courseName.substring(0, courseName.length()-1);
							className = className.substring(0, className.length()-1);
							week = week.substring(0, week.length()-1);
						}
						if(!"".equalsIgnoreCase(code)){
							usedInfo += tempOrder+":"+code+"@"+teaCode+"@"+teaName+"@"+courseName+"@"+className+"@"+week+",";
						}
						
						code = "";
						teaCode = "";
						teaName = "";
						courseName = "";
						className = "";
						week = "";
						
						if(usedInfo.length() > 0){
							usedInfo = usedInfo.substring(0, usedInfo.length()-1);
						}
						jsonStr += "{\"code\":\"" + tempTea + "\"," +
							"\"name\":\"\"," +
							"\"type\":\"\"," +
							"\"num\":\"0\"," +
							"\"usedOrder\":\"" + usedInfo + "\"},";
						usedInfo = "";
					}
					
					tempTea = MyTools.StrFiltr(vec.get(i));
					tempOrder = MyTools.StrFiltr(vec.get(i+1));
					tempVec.add(MyTools.StrFiltr(vec.get(i+2)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+3)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+4)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
				}
			}
			for(int j=0; j<tempVec.size(); j+=6){
				code += MyTools.StrFiltr(tempVec.get(j))+"｜";
				teaCode += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
				teaName += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
				courseName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
				className += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
				week += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
			}
			tempVec.clear();
			
			if(code.length() > 0){
				code = code.substring(0, code.length()-1);
				teaCode = teaCode.substring(0, teaCode.length()-1);
				teaName = teaName.substring(0, teaName.length()-1);
				courseName = courseName.substring(0, courseName.length()-1);
				className = className.substring(0, className.length()-1);
				week = week.substring(0, week.length()-1);
			}
			
			if(!"".equalsIgnoreCase(code)){
				usedInfo += tempOrder+":"+code+"@"+teaCode+"@"+teaName+"@"+courseName+"@"+className+"@"+week+",";
			}
			
			if(usedInfo.length() > 0){
				usedInfo = usedInfo.substring(0, usedInfo.length()-1);
			}
			jsonStr += "{\"code\":\"" + tempTea + "\"," +
				"\"name\":\"\"," +
				"\"type\":\"\"," +
				"\"num\":\"0\"," +
				"\"usedOrder\":\"" + usedInfo + "\"},";
			
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
			if(!"".equalsIgnoreCase(jsonStr)){
				jsonStr += "]";
			}
			resultVec.add(jsonStr);
			
			//判断如果是更改课程信息后的查询，需要返回当前班级所有授课教师名单
			if("tea".equalsIgnoreCase(type)){
				resultVec.add(teaList);
			}
		}
		
		//=============================================================================================================
		//判断当前班级是哪个系部，根据系部查询场地信息； 2017-7-1
		Vector vec2 = null;
		Vector classFixedClassroom = null;//班级固定教室
		String nj=this.getPK_XNXQBM().substring(2,4); //年级
		String xbdm = ""; //系部代码
		String gdjsbh = ""; //固定教室编号
		String xzbdm = MyTools.StrFiltr(this.getPK_XZBDM()); //行政班代码
		//sql = " select 系部代码 from (select 行政班代码,系部代码 from V_学校班级_数据子类 union all select 教学班编号,系部代码 from V_基础信息_教学班信息表) t where 行政班代码 = '" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		sql = " select 系部代码 from V_基础信息_班级信息表 where 班级编号='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		vec2 = db.GetContextVector(sql);
		if(vec2!=null && vec2.size()>0){
			xbdm = MyTools.StrFiltr(vec2.get(0));
		}
		
		sql = "select a.班级编号,a.班级名称,a.教室编号,b.教室名称 " +
			//"from (select distinct 行政班代码,教室编号,系部代码,行政班名称 from (select 行政班代码,教室编号,系部代码,行政班名称,年级代码 from V_学校班级_数据子类 union all select 教学班编号,教室编号,系部代码,教学班名称,年级代码 from V_基础信息_教学班信息表) t " +
			"from (select * from V_基础信息_班级信息表 where " + nj + "-substring(年级代码,1,2)>=0 and " + nj + "-substring(年级代码,1,2)<3) a " +
			"left join V_教室数据类 b on b.教室编号=a.教室编号 " +
			"where a.系部代码= '" + MyTools.fixSql(xbdm) + "' " +
			"order by a.班级名称 ";
		classFixedClassroom = db.GetContextVector(sql);
		
		//获取班级固定教室编号
		if(classFixedClassroom.size() > 0){
			for (int i = 0; i < classFixedClassroom.size(); i+=4) {
				if(xzbdm.equalsIgnoreCase(MyTools.StrFiltr(classFixedClassroom.get(i)))){
					
				}else{
					gdjsbh += "'" + classFixedClassroom.get(i+2) + "',";
				}
			}
			
			gdjsbh = gdjsbh.substring(0, gdjsbh.length()-1);
		}
		
		//==============================================================================================================
		
		if("all".equalsIgnoreCase(type)){
			//查询所有场地的使用时间序列信息（由于部分未排课程未指定教室，所以需要读取所有场地信息）（不包含本班级已用教室信息）
			sql = "select distinct * from (select 教室编号,教室名称,教室类型代码,实际容量,'' as 时间序列,'' as 授课计划明细编号,'' as 实际场地编号, " +
				"'' as 实际场地名称,'' as 课程名称,'' as 行政班名称,'' as 授课周次详情 from V_教室数据类 where 是否可用='1' and 校区代码 = '" + MyTools.fixSql(xbdm) + "'" +
				" union all " +
				"select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,b.时间序列,b.授课计划明细编号,b.实际场地编号, " +
				"b.实际场地名称,b.课程名称,b.行政班名称,b.授课周次详情 from V_教室数据类 a " +
				"inner join V_排课管理_课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%' " +
				"where a.是否可用='1' and b.状态='1' and b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
				"and b.行政班代码<>'" + MyTools.fixSql(this.getPK_XZBDM()) + "' and a.校区代码 = '" + MyTools.fixSql(xbdm) + "' ";
			//判断如果是公共课教师或管理员，需要获取专业课固排禁排教室信息(不包含本班级)
			//if(this.getAuth().indexOf(pubTeacher)>-1 || this.getAuth().indexOf(admin)>-1){
				sql += " union all " +
					"select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,e.时间序列,e.授课计划明细编号,b.场地要求,b.场地名称,b.课程名称,d.班级名称,b.授课周次详情 " +
					"from V_教室数据类 a " +
					"inner join V_规则管理_授课计划明细表 b on b.场地要求 like '%'+a.教室编号+'%' " +
					"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
					//"left join (select 行政班代码,行政班名称 from V_学校班级_数据子类 union all select 教学班编号,教学班名称 from V_基础信息_教学班信息表) d on d.行政班代码=c.行政班代码 " +
					"left join V_基础信息_班级信息表 d on d.班级编号=c.行政班代码 " +
					"inner join V_规则管理_固排禁排表 e on e.授课计划明细编号=b.授课计划明细编号 " +
					"where a.是否可用='1' and a.校区代码 = '" + MyTools.fixSql(xbdm) + "' and b.状态='1' and e.状态='1' and e.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
					"and c.行政班代码<>'" + MyTools.fixSql(this.getPK_XZBDM()) + "' " +
					"and e.类型='2' and (b.节数-b.实际已排节数)>0 "; //20160919/yeq添加条件，避免发生在排课设置课表中更换教室后，该教室无法被使用";";
				//场地禁排信息
				sql += " union all " +
					"select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,b.时间序列,'场地禁排' as 授课计划明细编号,a.教室编号,a.教室名称,'场地禁排' as 课程名称,'场地禁排' as 行政班名称,'场地禁排' as 授课周次详情  " +
					"from V_教室数据类 a  " +
					"inner join V_规则管理_固排禁排表 b on b.行政班代码 = a.教室编号  " +
					"where a.是否可用='1' and a.校区代码 = '" + MyTools.fixSql(xbdm) + "' and b.状态='1' and b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%'  " +
					"and b.类型='3' and b.禁排类型='cd'";
			//}
			sql += ") as t";
			if(!"".equalsIgnoreCase(gdjsbh)){
				sql += " where t.教室编号 not in (" + gdjsbh + ")";
			}
			sql += " order by t.实际容量,t.教室编号,t.时间序列";
			vec = db.GetContextVector(sql);
			
			tempOrder = "";
			code = "";
			courseName = "";
			className = "";
			week = "";
			usedInfo = "";
			jsonStr = "[";
			for(int i=0; i<vec.size(); i+=11){
				//判断是否同一教室
				if(tempSite.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					//判断是否同一时间序列
					if(tempOrder.equalsIgnoreCase((MyTools.StrFiltr(vec.get(i+1))))){
						tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+8)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+9)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+10)));
					}else{
						if(i > 0){
							for(int j=0; j<tempVec.size(); j+=6){
								code += MyTools.StrFiltr(tempVec.get(j))+"｜";
								siteCode += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
								siteName += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
								courseName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
								tempCodeArray = MyTools.StrFiltr(tempVec.get(j)).split("｜");
								for(int k=0; k<tempCodeArray.length; k++){
									className += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
								}
								week += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
							}
							tempVec.clear();
							
							if(code.length() > 0){
								code = code.substring(0, code.length()-1);
								siteCode = siteCode.substring(0, siteCode.length()-1);
								siteName = siteName.substring(0, siteName.length()-1);
								courseName = courseName.substring(0, courseName.length()-1);
								className = className.substring(0, className.length()-1);
								week = week.substring(0, week.length()-1);
							}
							if(!"".equalsIgnoreCase(code)){
								usedInfo += tempOrder+":"+code+"@"+siteCode+"@"+siteName+"@"+courseName+"@"+className+"@"+week+",";
							}
							
							code = "";
							siteCode = "";
							siteName = "";
							courseName = "";
							className = "";
							week = "";
						}
						
						tempSite = MyTools.StrFiltr(vec.get(i));
						siteMc = MyTools.StrFiltr(vec.get(i+1));
						siteType = MyTools.StrFiltr(vec.get(i+2));
						siteStuNum = MyTools.StrFiltr(vec.get(i+3));
						tempOrder = MyTools.StrFiltr(vec.get(i+4));
						
						tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+8)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+9)));
						tempVec.add(MyTools.StrFiltr(vec.get(i+10)));
					}
				}else{
					if(i > 0){
						for(int j=0; j<tempVec.size(); j+=6){
							code += MyTools.StrFiltr(tempVec.get(j))+"｜";
							siteCode += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
							siteName += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
							courseName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
							tempCodeArray = MyTools.StrFiltr(tempVec.get(j)).split("｜");
							for(int k=0; k<tempCodeArray.length; k++){
								className += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
							}
							week += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
						}
						tempVec.clear();
						
						if(code.length() > 0){
							code = code.substring(0, code.length()-1);
							siteCode = siteCode.substring(0, siteCode.length()-1);
							siteName = siteName.substring(0, siteName.length()-1);
							courseName = courseName.substring(0, courseName.length()-1);
							className = className.substring(0, className.length()-1);
							week = week.substring(0, week.length()-1);
						}
						if(!"".equalsIgnoreCase(code)){
							usedInfo += tempOrder+":"+code+"@"+siteCode+"@"+siteName+"@"+courseName+"@"+className+"@"+week+",";
						}
						
						code = "";
						siteCode = "";
						siteName = "";
						courseName = "";
						className = "";
						week = "";
						
						if(usedInfo.length() > 0){
							usedInfo = usedInfo.substring(0, usedInfo.length()-1);
						}
						jsonStr += "{\"code\":\"" + tempSite + "\"," +
								"\"name\":\"" + siteMc + "\"," +
								"\"type\":\"" + siteType + "\"," +
								"\"num\":\"" + siteStuNum + "\"," +
								"\"usedOrder\":\"" + usedInfo + "\"},";
						usedInfo = "";
					}
					
					tempSite = MyTools.StrFiltr(vec.get(i));
					siteMc = MyTools.StrFiltr(vec.get(i+1));
					siteType = MyTools.StrFiltr(vec.get(i+2));
					siteStuNum = MyTools.StrFiltr(vec.get(i+3));
					tempOrder = MyTools.StrFiltr(vec.get(i+4));
					
					tempVec.add(MyTools.StrFiltr(vec.get(i+5)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+6)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+7)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+8)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+9)));
					tempVec.add(MyTools.StrFiltr(vec.get(i+10)));
				}
			}
			for(int j=0; j<tempVec.size(); j+=6){
				code += MyTools.StrFiltr(tempVec.get(j))+"｜";
				siteCode += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
				siteName += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
				courseName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
				className += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
				week += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
			}
			tempVec.clear();
			
			if(code.length() > 0){
				code = code.substring(0, code.length()-1);
				siteCode = siteCode.substring(0, siteCode.length()-1);
				siteName = siteName.substring(0, siteName.length()-1);
				courseName = courseName.substring(0, courseName.length()-1);
				className = className.substring(0, className.length()-1);
				week = week.substring(0, week.length()-1);
			}
			
			if(!"".equalsIgnoreCase(code)){
				usedInfo += tempOrder+":"+code+"@"+siteCode+"@"+siteName+"@"+courseName+"@"+className+"@"+week+",";
			}
			
			if(usedInfo.length() > 0){
				usedInfo = usedInfo.substring(0, usedInfo.length()-1);
			}
			jsonStr += "{\"code\":\"" + tempSite + "\"," +
				"\"name\":\"" + siteMc + "\"," +
				"\"type\":\"" + siteType + "\"," +
				"\"num\":\"" + siteStuNum + "\"," +
				"\"usedOrder\":\"" + usedInfo + "\"},";
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
			if(!"".equalsIgnoreCase(jsonStr)){
				jsonStr += "]";
			}
			resultVec.add(jsonStr);
		}
		
		//====================================================================================================================
		/**教师跨系部信息*/
		Vector vec3 = null;
		Vector vec4 = null;
		Vector xbypjs = null;
		Vector tempjsxbkcxx = new Vector();
		Vector jsxbkcxx = new Vector();
		String xzb = "";
		
		sql = "select distinct(班级编号) from V_基础信息_班级信息表 where 系部代码 = '" + MyTools.fixSql(xbdm) + "'";
		vec4 = db.GetContextVector(sql);
		
		if(vec4.size() > 0){
			for (int i = 0; i < vec4.size(); i++) {
				xzb += "'" + vec4.get(i) + "',";
			}
			
			xzb = xzb.substring(0, xzb.length()-1);
		}
		
		
		//获取教师在其他系部上课的时间序列和授课周次
		sql = " select a.授课教师编号,a.时间序列,a.授课周次详情,a.授课教师姓名,c.系部名称 from dbo.V_排课管理_课程表明细详情表 a " + 
			  " left join V_基础信息_班级信息表 b " + 
		      " on a.行政班代码 = b.班级编号 " + 
			  " left join dbo.V_基础信息_系部信息表 c " + 
			  " on b.系部代码 = c.系部代码  " +
			  " where a.学年学期编码 = '" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and a.行政班代码 not in (" + xzb + ") and a.授课计划明细编号 != '' " ;
		vec3 = db.GetContextVector(sql);
		
		for (int i = 0; i < vec3.size(); i+=5) {
			String tempskjsbh = MyTools.StrFiltr(vec3.get(i));
			String tempsjxl = MyTools.StrFiltr(vec3.get(i+1));
			String tempskzcxq = MyTools.StrFiltr(vec3.get(i+2));
			String tempskjsxm = MyTools.StrFiltr(vec3.get(i+3));
			String tempskxb = MyTools.StrFiltr(vec3.get(i+4));
			String[] skjsbhArray1 = tempskjsbh.split("｜");
			String[] skjsxmArray1 = tempskjsxm.split("｜");
			String[] skzcxqArray1 = tempskzcxq.split("｜");
			for (int j = 0; j < skjsbhArray1.length; j++) {
				String skjsbh1_2 = skjsbhArray1[j];
				String skjsxm1_2 = skjsxmArray1[j];
				String skzcxq1_2 = skzcxqArray1[j];
				String[] skjsbhArray2_2 = skjsbh1_2.split("\\&");
				String[] skjsxmArray2_2 = skjsxm1_2.split("\\&");
				String[] skzcxqArray2_2 = skzcxq1_2.split("\\&");
				for (int k = 0; k < skjsbhArray2_2.length; k++) {
					String skjsbh3_3 = skjsbhArray2_2[k];
					String skjsxm3_3 = skjsxmArray2_2[k];
					String[] skjsbhArray3_3 = skjsbh3_3.split("\\+");
					String[] skjsxmArray3_3 = skjsxm3_3.split("\\+");
					for (int m = 0; m < skjsbhArray3_3.length; m++) {
						tempjsxbkcxx.add(skjsbhArray3_3[m]); //授课教师编号
						tempjsxbkcxx.add(tempsjxl); //时间序列
						tempjsxbkcxx.add(skzcxqArray2_2[k]); //授课周次详情
						tempjsxbkcxx.add(skjsxmArray3_3[m]); //授课教师姓名
						tempjsxbkcxx.add(tempskxb); //授课系部
					}
				}
			}
		}
		
		
		sql = " with tb as " +
			  " ( " +
			  " select 授课教师编号 from dbo.V_排课管理_课程表明细详情表 where 学年学期编码 = '" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 行政班代码 not in (" + xzb + ") " +
			  " ), " +
			  " tt as  " +
			  " (select [授课教师编号]=cast(left([授课教师编号],charindex('&',[授课教师编号]+'&')-1) as nvarchar(100)),Split=cast(stuff([授课教师编号]+'&',1,charindex('&',[授课教师编号]+'&'),'') as nvarchar(100)) from tb  " +
			  " union all " +
			  " select [授课教师编号]=cast(left(Split,charindex('&',Split)-1) as nvarchar(100)),Split= cast(stuff(Split,1,charindex('&',Split),'') as nvarchar(100)) from tt where split>'' " +
			  " ), " +
			  " ts as  " +
			  " (select [授课教师编号]=cast(left([授课教师编号],charindex('+',[授课教师编号]+'+')-1) as nvarchar(100)),Split=cast(stuff([授课教师编号]+'+',1,charindex('+',[授课教师编号]+'+'),'') as nvarchar(100)) from tt " +
			  " union all " +
			  " select [授课教师编号]=cast(left(Split,charindex('+',Split)-1) as nvarchar(100)),Split= cast(stuff(Split,1,charindex('+',Split),'') as nvarchar(100)) from ts where split>'' " +
			  " ), " +
			  " td as  " +
			  " (select [授课教师编号]=cast(left([授课教师编号],charindex('|',[授课教师编号]+'|')-1) as nvarchar(100)),Split=cast(stuff([授课教师编号]+'|',1,charindex('|',[授课教师编号]+'|'),'') as nvarchar(100)) from ts " +
			  " union all " +
			  " select [授课教师编号]=cast(left(Split,charindex('|',Split)-1) as nvarchar(100)),Split= cast(stuff(Split,1,charindex('|',Split),'') as nvarchar(100)) from td where split>'' " +
			  " ) " +
			  " select distinct 授课教师编号 from td option (MAXRECURSION 0)";
		xbypjs = db.GetContextVector(sql);
		
		for (int i = 0; i < xbypjs.size(); i++) {
			if(!"".equalsIgnoreCase(MyTools.StrFiltr(xbypjs.get(i)))){
				jsxbkcxx.add(MyTools.StrFiltr(xbypjs.get(i)));
				Vector vec5 = new Vector(); 
				for (int j = 0; j < tempjsxbkcxx.size(); j+=5) {
					if(xbypjs.get(i).equals(tempjsxbkcxx.get(j))){
						vec5.add(MyTools.StrFiltr(tempjsxbkcxx.get(j+1)));
						vec5.add(MyTools.StrFiltr(tempjsxbkcxx.get(j+2)));
						vec5.add(MyTools.StrFiltr(tempjsxbkcxx.get(j+3)));
						vec5.add(MyTools.StrFiltr(tempjsxbkcxx.get(j+4)));
					}
				}
				jsxbkcxx.add(vec5); //【授课教师编号，【时间序列，授课周次详情，授课教师姓名，授课系部】】
			}
		}
			
		jsonStr = "[";
		
		for (int i = 0; i < jsxbkcxx.size(); i+=2) {
			String tempskjsbh = MyTools.StrFiltr(jsxbkcxx.get(i));
			jsonStr += "{\"skjsbh\":\"" + tempskjsbh + "\"," +
					   "\"jskxb\":[ ";
			Vector Vec = (Vector) jsxbkcxx.get(i+1);
			for (int j = 0; j < Vec.size(); j+=4) {
				String sjxl = MyTools.StrFiltr(Vec.get(j));
				String skzcxq = MyTools.StrFiltr(Vec.get(j+1));
				String skjsxm = MyTools.StrFiltr(Vec.get(j+2));
				String skxb = MyTools.StrFiltr(Vec.get(j+3));
				jsonStr += "{\"sjxl\":\"" + sjxl + "\"," +
						   "\"skjsxm\":\"" + skjsxm + "\"," +
						   "\"skxb\":\"" + skxb + "\"," +
						   "\"skzcxq\":\"" + skzcxq + "\"},";
			}
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
			jsonStr += "]},";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		if(!"".equalsIgnoreCase(jsonStr)){
			jsonStr += "]";
		}
		resultVec.add(jsonStr);
		
		//====================================================================================================================
		
		
		return resultVec;
	}
	
	/**
	 * 查询合班信息
	 * @date:2015-06-20
	 * @author:yeq
	 * @return 合班信息
	 * @throws SQLException
	 */
	public Vector queHbInfo() throws SQLException{
		Vector vec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String jsonStr = "";
		String sql = "";
		String hbbh = "";//合班编号
		String xzbmc = "";//行政班名称
		int bjrs = 0;//班级人数
		String tempXzbmc = "";
		int tempBjrs = 0;
		
		//查询当前班级合班课程设置
//		sql = "select distinct a.授课计划明细编号,stuff((select '+'+t2.行政班名称 from V_规则管理_授课计划主表 t " +
//			"left join V_规则管理_授课计划明细表 t1 on t1.授课计划主表编号=t.授课计划主表编号 " +
//			"left join V_学校班级_数据子类 t2 on t2.行政班代码=t.行政班代码 where t1.状态='1' and a.授课计划明细编号 like '%'+t1.授课计划明细编号+'%' order by t1.授课计划明细编号 for XML PATH('')),1,1,'')," +
//			"(select sum(总人数) from V_学校班级_数据子类 where 行政班代码 in (select e.行政班代码 from V_规则管理_授课计划主表 e " +
//			"left join V_规则管理_授课计划明细表 d on d.授课计划主表编号=e.授课计划主表编号 " +
//			"left join V_规则管理_合班表 f on f.授课计划明细编号 like '%'+d.授课计划明细编号+'%' where f.编号=a.编号)) as 总人数 " +
//			"from V_规则管理_合班表 a " +
//			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
//			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
//			"where c.状态='1' and c.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		//性能优化
		sql = "with cte1 as(" +
			"select a.授课计划明细编号 from V_规则管理_合班表 a " +
			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
			"where c.状态='1' and c.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') " +
			"select distinct a.授课计划明细编号 as 合班编号,b.授课计划明细编号,d.班级名称,d.总人数 from cte1 a " +
			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
			//"left join (select 行政班代码,行政班名称 from V_学校班级_数据子类 union all select 教学班编号,教学班名称 from V_基础信息_教学班信息表) d on d.行政班代码=c.行政班代码 " +
			"left join V_基础信息_班级信息表 d on d.班级编号=c.行政班代码 " +
			"order by a.授课计划明细编号,b.授课计划明细编号";
		vec = db.GetContextVector(sql); 
		
		for(int i=0; i<vec.size(); i+=4){
			xzbmc = MyTools.StrFiltr(vec.get(i+2));
			bjrs = MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+3)));
			
			if(!hbbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
				if(i > 0){
					tempVec.add(hbbh);
					tempVec.add(tempXzbmc);
					tempVec.add(tempBjrs);
				}
				
				hbbh = MyTools.StrFiltr(vec.get(i));
				tempXzbmc = xzbmc;
				tempBjrs = bjrs;
			}else{
				tempXzbmc += "+"+xzbmc;
				tempBjrs += bjrs;
			}
		}
		tempVec.add(hbbh);
		tempVec.add(tempXzbmc);
		tempVec.add(tempBjrs);
		
		jsonStr = "[";
		if(tempVec!=null && tempVec.size()>0){
			for(int i=0; i<tempVec.size(); i+=3){
				jsonStr += "{\"合班授课编号\":\"" + MyTools.StrFiltr(tempVec.get(i))+"\"," +
						"\"合班班级名称\":\"" + MyTools.StrFiltr(tempVec.get(i+1))+"\"," +
						"\"总人数\":\"" + MyTools.StrFiltr(tempVec.get(i+2))+"\"},";
			}
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		}else{
			jsonStr += "{\"合班授课编号\":\"\",\"合班班级名称\":\"\",\"总人数\":\"\"}";
		}
		jsonStr += "]";
		resultVec.add(jsonStr);
		
		//查询其他班级与本班合班课程的排课信息（不包括本班）
		sql = "select distinct m.时间序列,n.授课计划明细编号,n.课程代码,n.课程名称,m.行政班名称,m.授课计划明细编号,m.实际场地编号,m.实际场地名称,n.授课周次 from V_排课管理_课程表明细详情表 m " +
			"inner join (select t.授课计划明细编号,t.课程代码,t.课程名称,case t.授课周次 when 'odd' then '1' when 'even' then '2' else t.授课周次 end as 授课周次 " +
			"from V_规则管理_授课计划明细表 t inner join (select a.授课计划明细编号 from V_规则管理_合班表 a " +
			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
			"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
			"where b.状态='1' and c.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and c.行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "') t1 on t1.授课计划明细编号 like '%'+t.授课计划明细编号+'%') n on m.授课计划明细编号 like '%'+n.授课计划明细编号+'%' " +
			"where m.行政班代码<>'" + MyTools.fixSql(this.getPK_XZBDM()) + "' order by m.时间序列,n.授课周次,n.课程代码";
		tempVec = db.GetContextVector(sql);
		String tempTimeOrder = "";
		String tempSkjhbh = "";
		String tempCourseCode = "";
		String tempCourseName = "";
		String tempClass = "";
		String pjSkjhbh[] = new String[0];
		String tempSiteCode = "";
		String tempSiteCodeArray[] = new String[0];
		String tempSiteName = "";
		String tempSiteNameArray[] = new String[0];
		jsonStr = "[";
		if(tempVec!=null && tempVec.size()>0){
			for(int i=0; i<tempVec.size(); i+=9){
				//判断是同一时间序列,整合合班信息
				if(tempTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i)))){
					//判断是否相同课程
					if(tempCourseCode.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2)))){
						tempSkjhbh += "+"+MyTools.StrFiltr(tempVec.get(i+1));
						tempClass += "、"+MyTools.StrFiltr(tempVec.get(i+4));
					}else{
						if(i > 0){
							jsonStr += "{\"时间序列\":\"" + tempTimeOrder + "\"," +
									"\"合班授课计划编号\":\"" + tempSkjhbh + "\"," +
									"\"合班课程名称\":\"" + tempCourseName + "\"," +
									"\"合班班级名称\":\"" + tempClass + "\"," +
									"\"实际场地编号\":\"" + tempSiteCode + "\"," +
									"\"实际场地名称\":\"" + tempSiteName + "\"},";
						}
						
						tempTimeOrder = MyTools.StrFiltr(tempVec.get(i));
						tempSkjhbh = MyTools.StrFiltr(tempVec.get(i+1));
						tempCourseCode = MyTools.StrFiltr(tempVec.get(i+2));
						tempCourseName = MyTools.StrFiltr(tempVec.get(i+3));
						tempClass = MyTools.StrFiltr(tempVec.get(i+4));
						pjSkjhbh = MyTools.StrFiltr(tempVec.get(i+5)).split("｜");
						//判断如果是拼接课程的话，解析当前课程实际场地
						if(pjSkjhbh.length > 1){
							tempSiteCodeArray = MyTools.StrFiltr(tempVec.get(i+6)).split("｜");
							tempSiteNameArray = MyTools.StrFiltr(tempVec.get(i+7)).split("｜");
							
							for(int a=0; a<pjSkjhbh.length; a++){
								if(tempSkjhbh.equalsIgnoreCase(pjSkjhbh[a])){
									tempSiteCode = tempSiteCodeArray[a];
									tempSiteName = tempSiteNameArray[a];
								}
							}
						}else{
							tempSiteCode = MyTools.StrFiltr(tempVec.get(i+6));
							tempSiteName = MyTools.StrFiltr(tempVec.get(i+7));
						}
					}
				}else{
					if(i > 0){
						jsonStr += "{\"时间序列\":\"" + tempTimeOrder + "\"," +
								"\"合班授课计划编号\":\"" + tempSkjhbh + "\"," +
								"\"合班课程名称\":\"" + tempCourseName + "\"," +
								"\"合班班级名称\":\"" + tempClass + "\"," +
								"\"实际场地编号\":\"" + tempSiteCode + "\"," +
								"\"实际场地名称\":\"" + tempSiteName + "\"},";
					}
					
					tempTimeOrder = MyTools.StrFiltr(tempVec.get(i));
					tempSkjhbh = MyTools.StrFiltr(tempVec.get(i+1));
					tempCourseCode = MyTools.StrFiltr(tempVec.get(i+2));
					tempCourseName = MyTools.StrFiltr(tempVec.get(i+3));
					tempClass = MyTools.StrFiltr(tempVec.get(i+4));
					pjSkjhbh = MyTools.StrFiltr(tempVec.get(i+5)).split("｜");
					//判断如果是拼接课程的话，解析当前课程实际场地
					if(pjSkjhbh.length > 1){
						tempSiteCodeArray = MyTools.StrFiltr(tempVec.get(i+6)).split("｜");
						tempSiteNameArray = MyTools.StrFiltr(tempVec.get(i+7)).split("｜");
						
						for(int a=0; a<pjSkjhbh.length; a++){
							if(tempSkjhbh.equalsIgnoreCase(pjSkjhbh[a])){
								tempSiteCode = tempSiteCodeArray[a];
								tempSiteName = tempSiteNameArray[a];
							}
						}
					}else{
						tempSiteCode = MyTools.StrFiltr(tempVec.get(i+6));
						tempSiteName = MyTools.StrFiltr(tempVec.get(i+7));
					}
				}
			}
			jsonStr += "{\"时间序列\":\"" + tempTimeOrder + "\"," +
					"\"合班授课计划编号\":\"" + tempSkjhbh + "\"," +
					"\"合班课程名称\":\"" + tempCourseName + "\"," +
					"\"合班班级名称\":\"" + tempClass + "\"," +
					"\"实际场地编号\":\"" + tempSiteCode + "\"," +
					"\"实际场地名称\":\"" + tempSiteName + "\"},";
			jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		}else{
			jsonStr += "{\"时间序列\":\"\",\"合班授课计划编号\":\"\"," +
					"\"合班课程名称\":\"\",\"合班班级名称\":\"\"," +
					"\"实际场地编号\":\"\",\"实际场地名称\":\"\"}";
		}
		jsonStr += "]";
		resultVec.add(jsonStr);
		
		return resultVec;
	}
	
	/**
	 * 查询添加课程信息
	 * @date:2015-07-23
	 * @author:yeq
	 * @return 添加课程信息
	 * @throws SQLException
	 */
	public Vector queAddCourseInfo()throws SQLException{
		Vector resultVec = new Vector();
		String sql = "";
		Vector tempVec = null;
		String jsonStr = "";
		
		//查询当前专业所有任课教师的当前学期授课时间序列信息（不包含本班级）
		sql = "select distinct 时间序列,case 类型 when '1' then '学期课程' when '2' then '整班课程' end,课程名称,授课教师姓名,场地名称 from V_排课管理_添加课程信息表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and (类型='1' or 类型='2' and 行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "') order by 时间序列";
		tempVec = db.GetContextVector(sql);
		
		jsonStr = "[";
		for(int i=0; i<tempVec.size(); i+=5){
			jsonStr += "{\"timeOrder\":\"" + MyTools.StrFiltr(tempVec.get(i)) + "\"," +
					"\"type\":\"" + MyTools.StrFiltr(tempVec.get(i+1)) + "\"," +
					"\"course\":\"" + MyTools.StrFiltr(tempVec.get(i+2)) + "\"," +
					"\"tea\":\"" + MyTools.StrFiltr(tempVec.get(i+3)) + "\"," +
					"\"site\":\"" + MyTools.StrFiltr(tempVec.get(i+4)) + "\"},";
		}
		
		jsonStr = jsonStr.substring(0, jsonStr.length()-1);
		if(!"".equalsIgnoreCase(jsonStr)){
			jsonStr += "]";
		}
		resultVec.add(jsonStr);
		
		return resultVec;
	}
	
	/**
	 * 保存课程表备注
	 * @date:2015-06-27
	 * @author:yeq
	 * @throws SQLException
	 * @throws UnsupportedEncodingException 
	 */
	public void saveRemark()throws SQLException, UnsupportedEncodingException{
		String sql = "update V_排课管理_课程表主表 set 备注='" + MyTools.fixSql(java.net.URLDecoder.decode(this.getPK_BZ(), "UTF-8")) + "' " +
				"where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 行政班代码='" + MyTools.fixSql(this.getPK_XZBDM()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-05-27
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
	 * 读取学年下拉框
	 * @date:2015-05-27
	 * @author:yeq
	 * @param type 查询类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadYearCombo(String type) throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年 as comboName,学年 as comboValue,1 " +
				"from V_规则管理_学年学期表 where 状态='1'";
		//判断是否查询排课已过期学年
		if("over".equalsIgnoreCase(type)){
			sql += " and convert(nvarchar(10),getDate(),21)>convert(nvarchar(10),排课截止时间,21)";
			
		}
		sql += " order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程下拉框
	 * @date:2015-06-27
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCourseCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 课程名称+'（'+课程号+'）' as comboName,课程号 as comboValue " +
				"from V_课程数据子类";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取考试形式下拉框
	 * @date:2017-02-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKsxsCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 考试形式 as comboName,编号 as comboValue " +
				"from V_考试形式 where 编号<>0";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教师下拉框
	 * @date:2016-06-15
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queTeaCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '' as rowNum,'请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct '1',姓名,工号 " +
				"from V_教职工基本数据子类 order by rowNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取学期下拉框
	 * @date:2015-05-27
	 * @author:yeq
	 * @param xn 学年
	 * @param type 查询类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queXqCombo(String xn, String type) throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct case 学期 when 1 then '第一学期' else '第二学期' end as comboName,学期 as comboValue " +
				"from V_规则管理_学年学期表 where 状态='1' and 学年='" + MyTools.fixSql(xn) + "'";
		//判断是否查询排课已过期学年
		if("over".equalsIgnoreCase(type)){
			sql += " and convert(nvarchar(10),getDate(),21)>convert(nvarchar(10),排课截止时间,21)";
			
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2015-05-27
	 * @author:yeq
	 * @param xn 学年
	 * @param xq 学期
	 * @param type 查询类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queJxxzCombo(String xn, String xq, String type) throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct b.教学性质 as comboName,a.教学性质 as comboValue " +
				"from V_规则管理_学年学期表 a " +
				"left join V_基础信息_教学性质 b on a.教学性质=b.编号 " +
				"where a.状态='1' and a.学年='" + MyTools.fixSql(xn) + "' and a.学期='" + MyTools.fixSql(xq) + "'";
		//判断是否查询排课已过期学年
		if("over".equalsIgnoreCase(type)){
			sql += " and convert(nvarchar(10),getDate(),21)>convert(nvarchar(10),排课截止时间,21)";
			
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2015-05-27
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "";
		//String admin = MyTools.getProp(request, "Base.admin");//管理员
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		sql = "select '' as 专业名称,'请选择' as comboName,'' as comboValue " +
			"union all " +
			"select distinct 专业名称,专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue " +
			"from V_专业基本信息数据子类 where 状态='1'";
		
		//判断权限,管理员和排公共课的老师可看到所有专业,排专业课的老师只可以看到自己的专业
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and 专业代码 in (select 专业代码 from V_专业组组长信息 where 教师代码 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%')";
//		}
		sql += " order by 专业名称";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-07-04
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXBCombo() throws SQLException{
		Vector vec = null;
		String sql = "";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长@211@
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任@212@
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任@213@
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理@214@
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理@215@
		
		//判断权限
		if(this.getAuth().indexOf(qxjwgl) > -1 || this.getAuth().indexOf(admin) > -1 || this.getAuth().indexOf(jxzgxz) > -1 || this.getAuth().indexOf(qxjdzr) > -1 ){
			sql += " select '请选择' as comboName,'' as comboValue " +
				   " union all " +
				   " select 系部名称 as comboName, 系部代码 as comboValue from [dbo].[V_基础信息_系部信息表] where 系部代码 <>'C00'";
			
			sql += " order by comboValue";
		}else if(this.getAuth().indexOf(xbjwgl) > -1 || this.getAuth().indexOf(xbjdzr) > -1){
			sql += " select '请选择' as comboName,'' as comboValue " +
				   " union all " +
				   " select 系部名称 as comboName, 系部代码 as comboValue from [dbo].[V_基础信息_系部信息表] where 系部代码 <>'C00' and 系部代码 in ( select a.系部代码 from dbo.V_基础信息_系部教师信息表 a where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')";
			
			sql += " order by comboValue";
		}
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 检查教室冲突
	 * @date:2016-02-16
	 * @author:lupengfei
	 * @return MSG
	 * @throws SQLException
	 */
	public void checkCourseArrange() throws SQLException{
			Vector classVec = null;
			String sql="";
			
			String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
			//获取所有需要排课的班级
				sql = "select distinct b.行政班代码,c.总人数 from V_规则管理_授课计划明细表 a " +
					"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
					//"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
					"left join V_基础信息_班级信息表 c on c.班级编号=b.行政班代码 " +
					"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
				//判断权限，如果是公共课老师操作所有专业班级,专业课老师操作自己选择的专业班级
				if(this.getAuth().indexOf(majorTeacher) > -1){
					sql += " and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
				}
				classVec = db.GetContextVector(sql);
				
				//检查要排课班级的固排教室与排课表中的是否有冲突  
				String sqlgp="";//查询固排禁排表
				String sqlkcb="";//查询课程表
				Vector vecgp= null;
				Vector vecgp2= null;
				Vector veckcb=null;
				Vector vec3=null;
				Vector vec4=null;
				String timexl=null;//时间序列
				String ljxgbh=null;//连节相关编号
				String skjhbh=null;//授课计划明细编号
				String sxzbdm=null;//行政班代码
				String sxzbmc=null;//行政班名称
				String skkcmc=null;//课程名称
				String szcdbh=null;//场地编号
				String szcdmc=null;//场地名称
				String sycdbh=null;//实际场地编号		
				String sycdmc=null;//实际场地名称
				String skzcxq=null;//授课周次详情
				String errinfo="";//冲突信息
				String sqlpkgl="";
				String classnum="";//保存班级编号
				String classnumber="";//班级人数
				String roomnum="";//教室编号
            	String roomname="";//教室名称
				int jslxdm=0;//教室类型代码
				int d=0;
				
				if(classVec!=null && classVec.size()>0){
					for(int c=0;c<classVec.size();c=c+2){
						classnum+="'"+classVec.get(c).toString()+"',";
					}
					classnum=classnum.substring(0, classnum.length()-1);
					sqlgp = "SELECT a.时间序列,a.连节相关编号,a.授课计划明细编号,a.行政班代码,c.班级名称,b.课程名称,b.场地要求,b.场地名称,a.预设场地编号,a.预设场地名称,b.授课周次详情  " +
						//"FROM V_规则管理_固排禁排表 a,V_规则管理_授课计划明细表 b,dbo.V_学校班级_数据子类 c " +
						"FROM V_规则管理_固排禁排表 a,V_规则管理_授课计划明细表 b,V_基础信息_班级信息表 c " +
						"where a.授课计划明细编号=b.授课计划明细编号 and a.行政班代码=c.班级编号 and a.状态='1' " +
						"and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and a.行政班代码 in (" + classnum + ") ";
					vecgp2=db.GetContextVector(sqlgp);
					if(vecgp2!=null&&vecgp2.size()>0){
						String sqlvecgp="";
						for(int s=0;s<vecgp2.size();s=s+11){
							String gpcdyq=vecgp2.get(6).toString();
							String gpcdmc=vecgp2.get(7).toString();
							String gpyscdbh=vecgp2.get(8).toString();
							String gpyscdmc=vecgp2.get(9).toString();
							String gpskzcxq=vecgp2.get(10).toString();
							String[] gpcdyq2=gpcdyq.split("\\&");
							String[] gpcdmc2=gpcdmc.split("\\&");
							String[] gpyscdbh2=gpyscdbh.split("\\&");
							String[] gpyscdmc2=gpyscdmc.split("\\&");
							String[] gpskzcxq2=gpskzcxq.split("\\&");
							for(int t=0;t<gpcdyq2.length;t++){
								sqlvecgp+=" select '"+vecgp2.get(0).toString()+"' as 时间序列,'"+vecgp2.get(1).toString()+"' as 连节相关编号,'"+vecgp2.get(2).toString()+"' as 授课计划明细编号,'"+vecgp2.get(3).toString()+"' as 行政班代码,'"+vecgp2.get(4).toString()+"' as 行政班名称,'"+vecgp2.get(5).toString()+"' as 课程名称,'"+gpcdyq2[t]+"' as 场地要求,'"+gpcdmc2[t]+"' as 场地名称,'"+gpyscdbh2[t]+"' as 预设场地编号,'"+gpyscdmc2[t]+"' as 预设场地名称,'"+gpskzcxq2[t]+"' as 授课周次详情  union ";
							}
						}
						sqlvecgp=sqlvecgp.substring(0,sqlvecgp.length()-6);
						String sqlgp2="select a.时间序列,a.连节相关编号,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程名称,a.场地要求,a.场地名称,a.预设场地编号,a.预设场地名称,a.授课周次详情  from ("+sqlvecgp+") a ";
						vecgp=db.GetContextVector(sqlgp2);

					if(vecgp!=null&&vecgp.size()>0){
						sqlkcb="SELECT 时间序列,连节相关编号,授课计划明细编号,行政班代码,行政班名称,课程名称,场地要求,场地名称,实际场地编号,实际场地名称,授课周次详情  FROM V_排课管理_课程表明细详情表  " +
							   " where 状态='1' and 学年学期编码='"+MyTools.fixSql(this.getPK_XNXQBM())+"' and LEN(行政班代码)<>9 and 行政班代码 not in ("+classnum+") ";
						//拆分课程表明细详情表
						veckcb=db.GetContextVector(sqlkcb);
						if(veckcb!=null&&veckcb.size()>0){
							for(int i=0;i<veckcb.size();i=i+11){
								timexl=veckcb.get(i).toString();//时间序列
								ljxgbh=veckcb.get(i+1).toString();//连节相关编号
								skjhbh=veckcb.get(i+2).toString();//授课计划明细编号
								sxzbdm=veckcb.get(i+3).toString();//行政班代码
								sxzbmc=veckcb.get(i+4).toString();//行政班名称
								skkcmc=veckcb.get(i+5).toString();//课程名称
								szcdbh=veckcb.get(i+6).toString();//场地编号
								szcdmc=veckcb.get(i+7).toString();//场地名称
								sycdbh=veckcb.get(i+8).toString();//实际场地编号
								sycdmc=veckcb.get(i+9).toString();//实际场地名称
								skzcxq=veckcb.get(i+10).toString();//授课周次详情
								
								//System.out.println(a+":"+skjhbh+","+xnxqbm+","+skjsbh+","+skjsxm+","+zyname+","+kechid+","+kechna+","+clasid+","+clasna+","+timexl+","+sjcdmc+","+skzcxq); a++;
								String[] skjhbh2=skjhbh.split("｜");
								String[] szcdbh2=szcdbh.split("｜");
								String[] szcdmc2=szcdmc.split("｜");
								String[] sycdbh2=sycdbh.split("｜");
								String[] sycdmc2=sycdmc.split("｜");
								String[] skzcxq2=skzcxq.split("｜");
								for(int j=0;j<skjhbh2.length;j++){
									//System.out.println(b+":"+skjhbh2[j]+","+skjsbh2[j]+","+skjsxm2[j]+","+kechid2[j]+","+kechna2[j]+","+sjcdmc2[j]+","+skzcxq2[j]); b++;
									String[] szcdbh3=szcdbh2[j].split("\\&");
									String[] szcdmc3=szcdmc2[j].split("\\&");
									String[] sycdbh3=sycdbh2[j].split("\\&");
									String[] sycdmc3=sycdmc2[j].split("\\&");
									String[] skzcxq3=skzcxq2[j].split("\\&");
									for(int k=0;k<skzcxq3.length;k++){
										//System.out.println(c+":"+skjsbh3[k]+","+skjsxm3[k]+","+sjcdmc3[k]+","+skzcxq3[k]); c++;
//										if(d==0){
//											sqlpkgl+=" select '"+skjhbh2[j]+"' as 授课计划明细编号,'"+xnxqbm+"' as 学年学期编码,'"+skjsbh4[m]+"' as 授课教师编号,'"+skjsxm4[m]+"' as 授课教师姓名,'"+zyname+"' as 专业名称,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+timexl+"' as 时间序列,'"+sjcdmc3[k]+"' as 实际场地名称,'"+skzcxq3[k]+"' as 授课周次详情 ";
//										}else{
//											sqlpkgl+=" union all select '"+skjhbh2[j]+"' as 授课计划明细编号,'"+xnxqbm+"' as 学年学期编码,'"+skjsbh4[m]+"' as 授课教师编号,'"+skjsxm4[m]+"' as 授课教师姓名,'"+zyname+"' as 专业名称,'"+kechid2[j]+"' as 课程代码,'"+kechna2[j]+"' as 课程名称,'"+clasid+"' as 行政班代码,'"+clasna+"' as 行政班名称,'"+timexl+"' as 时间序列,'"+sjcdmc3[k]+"' as 实际场地名称,'"+skzcxq3[k]+"' as 授课周次详情  ";
//										}
//										System.out.println(d+":"+timexl+","+skjhbh2[j]+","+szcdbh3[k]+","+szcdmc3[k]+","+sycdbh3[k]+","+sycdmc3[k]+","+skzcxq3[k]);
										for(int s=0;s<vecgp.size();s=s+11){
											
											//比较时间序列是否有相同
											if(vecgp.get(s).toString().equals(timexl)){//相同,继续判断
												//比较周次是否有相同
												int tag1=compareweek(vecgp.get(s+10).toString(),skzcxq3[k],"20");
												if(tag1==1){//有重复,继续判断
													//比较场地是否有相同
													int tag2=compareroom(vecgp.get(s+8).toString(),sycdbh3[k]);
													if(tag2==1){//场地相同,修改和提示
														if(vecgp.get(s+6).toString().equals("1")||vecgp.get(s+6).toString().equals("5")){//选的普通教师或多媒体教室,替换新的空闲教室
															String selroomid=",''";
															//取班级人数
															for(int c=0;c<classVec.size();c=c+2){
																if(classVec.get(c).toString().equals(vecgp.get(s+3).toString())){//班级编号相同
																	classnumber=classVec.get(c+1).toString();//班级人数
																}
															}
															//教室类型代码
															if(vecgp.get(s+6).toString().equals("1")){
																jslxdm=1;
															}else{
																jslxdm=5;
															}
															
															//获取连节相关编号
															String sjlian=vecgp.get(s).toString()+","+vecgp.get(s+1).toString();
															String[] sjxl=sjlian.split(",");
															String editsjxl="";
															for(int l=0;l<sjxl.length;l++){
																editsjxl+=sjxl[l]+",";
															}
															editsjxl=editsjxl.substring(0, editsjxl.length()-1);
																	
															//获取排课表和固排表信息
															String sqlrom="select 实际场地编号,授课周次详情 from dbo.V_排课管理_课程表明细详情表 where 时间序列 in ("+editsjxl+")"+
							            		        			" union select a.预设场地编号,b.授课周次详情 from dbo.V_规则管理_固排禁排表 a,dbo.V_规则管理_授课计划明细表 b where a.授课计划明细编号=b.授课计划明细编号 and 时间序列 in ("+editsjxl+")";
							            		        	vec4=db.GetContextVector(sqlrom);
							            		        	
							            		        	String userooms="";
							            		        	if(vec4!=null&&vec4.size()>0){
							            		        		for(int m=0;m<vec4.size();m=m+2){
							            		        			String useroom=vec4.get(m).toString();
							            		        			useroom=useroom.replaceAll("｜", "\\&").replaceAll("\\+", "\\&");
							            		        			String[] useroomnum=useroom.split("\\&");
							            		        			for(int n=0;n<useroomnum.length;n++){
							            		        				userooms+="'"+useroomnum[n]+"',";       				
							            		        			}		
							            		        		}
							            		        		userooms=userooms.substring(0,userooms.length()-1);
							            		        		String sqlcls="select 教室编号,教室名称,最大排课容量 from dbo.V_教室数据类 where 最大排课容量>='"+Integer.parseInt(classnumber)+"' and 教室类型代码='"+jslxdm+"' and 教室编号 not in ("+userooms+selroomid+") order by 最大排课容量 asc";
							            		        		vec3=db.GetContextVector(sqlcls);
							            		        		if(vec3!=null&&vec3.size()>0){
							            		        			roomnum=vec3.get(0).toString();
							            		        			roomname=vec3.get(1).toString();
							            		        		}
							            		        	}else{
							            		        		String sqlcls="select 教室编号,教室名称,最大排课容量 from dbo.V_教室数据类 where 最大排课容量>='"+Integer.parseInt(classnumber)+"' and 教室类型代码='"+jslxdm+"' and 教室编号 not in (''"+selroomid+") order by 最大排课容量 asc";
							            		        		vec3=db.GetContextVector(sqlcls);
							            		        		if(vec3!=null&&vec3.size()>0){
							            		        			roomnum=vec3.get(0).toString();
							            		        			roomname=vec3.get(1).toString();
							            		        		}
							            		        	}
							            		        	if(roomnum.equals("")){//没有符合的教室
							            		        		errinfo+=vecgp.get(s+4).toString()+",周"+vecgp.get(s).toString().substring(1,2)+",第"+vecgp.get(s).toString().substring(2,4)+"节,"+vecgp.get(s+5).toString()+"课程,"+vecgp.get(s+9).toString()+"没有可用的教室  <br>";
							            		        	}else{							            		        		
								            		        	String sqledit="";
								            		        	for(int l=0;l<sjxl.length;l++){
								            		        		sqledit+=" update dbo.V_规则管理_固排禁排表 set 预设场地编号='"+MyTools.fixSql(roomnum)+"',预设场地名称='"+MyTools.fixSql(roomname)+"' " +
								            		        	 		     " where 学年学期编码='"+MyTools.fixSql(this.getPK_XNXQBM())+"' and 行政班代码='"+MyTools.fixSql(vecgp.get(s+3).toString())+"' and 时间序列='"+MyTools.fixSql(sjxl[l])+"' and 授课计划明细编号='"+MyTools.fixSql(MyTools.fixSql(vecgp.get(s+2).toString()))+"' ";
								            		        	}
								            		        	if(db.executeInsertOrUpdate(sqledit)){
								            		        		this.setMSG("保存成功");
								            		        	}else{
								            		        		this.setMSG("保存失败");
								            		        	}
							            		        	}
														}else{//提示冲突信息
															errinfo+=vecgp.get(s+4).toString()+",周"+vecgp.get(s).toString().substring(1,2)+",第"+vecgp.get(s).toString().substring(2,4)+"节,"+vecgp.get(s+5).toString()+"课程,"+vecgp.get(s+9).toString()+"教室已被占用  <br>";
														}
													}else{//没有冲突
														
													}
												}else{//没有重复,下面不需要判断
													
												}
											}else{//不相同,下面不需要判断
												
											}
										}
										d++;						
									}
								}
							}	
						}
					}			
				}
				}
				if(errinfo.equals("")){//没有冲突信息
					errinfo="没有教室冲突";
				}else{
					errinfo+="请协调好之后重新排课！";					
				}	
				if(this.getMSG().equals("保存失败")){
					
				}else{
					this.setMSG(errinfo);	
				}
				
	}
	
	/**
	 * 自动排课
	 * @date:2015-05-27
	 * @author:yeq
	 * @param pkType 排课类型（用于判断是全新排课还是只安排剩余的课程：1为全新，2为剩余）
	 * @param tsgzFlag 用于判断是否验证特殊规则
	 * @param teaJpFlag 用于判断是否检查教师禁排
	 * @return String 课程表
	 * @throws SQLException
	 */
	public String autoCourseArrange(String pkType, String tsgzFlag, String teaJpFlag) throws SQLException{
		HttpSession session = request.getSession();
		Vector classVec = null;
		Vector tempVec = null;
		Vector allWpkcVec = new Vector();//未排课程信息
		Vector sqlVec = new Vector();
		String sql = "";
		String result = "";//返回结果
		String xqzc = "";//学期周次范围 
		int dayNum = 0; //每周天数
		int lessonNum = 0; //每天节数
		int monNum = 0;//上午节数
		int noonNum = 0;//中午节数
		int afternoonNum = 0;//下午节数
		int eveNum = 0;//晚上节数
//		String errorMsg = "";
		
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		//==========================================================================================================
		Vector vec2 = null; 
		Vector vec3 = null; 
		Vector teaskjhmxbhVector = new Vector(); //教师授课计划明细编号集合
		Vector teaypskjhmxbhVector = new Vector(); //教师已排授课计划明细编号集合
		Vector teaskjhmxbhVector2 = null; //教师授课计划明细编号集合
		Vector classFixedClassroom = null;//班级固定教室
		Vector jsxbkcxx = new Vector(); //教师系部课程信息
		Vector tempjsxbkcxx = new Vector(); //教师系部课程信息
		Vector xbypjs = null; //系部已排教师
		Vector teapdVector = null;
		String xzb = "";
		String nj = this.getPK_XNXQBM().substring(2,4);//年级
//		String zydm = "";
		
//		sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
//		vec2 = db.GetContextVector(sql);
//		if(vec2.size() > 0){
//			for (int i = 0; i < vec2.size(); i++) {
//				zydm += "'" + vec2.get(i) + "',";
//			}
//		}
//		zydm = zydm.substring(0, zydm.length()-1);
		
		//sql = "select distinct(行政班代码) from (select 行政班代码,系部代码 from V_学校班级_数据子类 union all select 教学班编号,系部代码 from V_基础信息_教学班信息表) t where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql = "select distinct(班级编号) from V_基础信息_班级信息表 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		vec2 = db.GetContextVector(sql);
		
		if(vec2.size() > 0){
			for (int i = 0; i < vec2.size(); i++) {
				xzb += "'" + vec2.get(i) + "',";
			}
			
			xzb = xzb.substring(0, xzb.length()-1);
		}
		
		//读取各班级教室编号信息（每个班都有一个固定的班级,时间是距今3年）2017-7-1
		sql = "select a.班级编号,a.班级名称,a.教室编号,b.教室名称 " +
			"from (select distinct 班级编号,教室编号,系部代码,班级名称 from V_基础信息_班级信息表 where " + nj + "-substring(年级代码,1,2)>=0 and " + nj + "-substring(年级代码,1,2)<3) a " +
			"left join dbo.V_教室数据类 b on b.教室编号=a.教室编号 " +
			"where a.系部代码= '" + MyTools.fixSql(this.getPK_XB()) + "' " +
			"order by a.班级名称 ";
		classFixedClassroom = db.GetContextVector(sql);
		
		//获取教师在其他系部上课的时间序列和授课周次
		sql = " select 授课教师编号,时间序列,授课周次详情 from dbo.V_排课管理_课程表明细详情表 where 学年学期编码 = '" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 行政班代码 not in (" + xzb + ") and 授课计划明细编号 != '' " ;
		vec3 = db.GetContextVector(sql);
		
		for (int i = 0; i < vec3.size(); i+=3) {
			String skjsbh = (String) vec3.get(i);
			String sjxl = (String) vec3.get(i+1);
			String skzcxq = (String) vec3.get(i+2);
			String [] skjsbhArray = skjsbh.split("｜");
			String [] skzcxqArray = skzcxq.split("｜");
			for (int j = 0; j < skjsbhArray.length; j++) {
				String skjsbh_2 = skjsbhArray[j];
				String skzcxq_2 = skzcxqArray[j];
				String [] skjsbhArray_2 = skjsbh_2.split("\\&");
				String [] skzcxqArray_2 = skzcxq_2.split("\\&");
				for (int k = 0; k < skjsbhArray_2.length; k++) {
					String skjsbh_3 = skjsbhArray_2[k];
					String [] skjsbhArray_3 = skjsbh_3.split("\\+");
					for (int l = 0; l < skjsbhArray_3.length; l++) {
						tempjsxbkcxx.add(skjsbhArray_3[l]); //授课教师编号
						tempjsxbkcxx.add(sjxl); //时间序列
						tempjsxbkcxx.add(skzcxqArray_2[k]); //授课周次详情
					}
				}
			}
		}
		
		sql = " with tb as " +
			  " ( " +
			  " select 授课教师编号 from dbo.V_排课管理_课程表明细详情表 where 学年学期编码 = '" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and 行政班代码 not in (" + xzb + ") " +
			  " ), " +
			  " tt as  " +
			  " (select [授课教师编号]=cast(left([授课教师编号],charindex('&',[授课教师编号]+'&')-1) as nvarchar(100)),Split=cast(stuff([授课教师编号]+'&',1,charindex('&',[授课教师编号]+'&'),'') as nvarchar(100)) from tb  " +
			  " union all " +
			  " select [授课教师编号]=cast(left(Split,charindex('&',Split)-1) as nvarchar(100)),Split= cast(stuff(Split,1,charindex('&',Split),'') as nvarchar(100)) from tt where split>'' " +
			  " ), " +
			  " ts as  " +
			  " (select [授课教师编号]=cast(left([授课教师编号],charindex('+',[授课教师编号]+'+')-1) as nvarchar(100)),Split=cast(stuff([授课教师编号]+'+',1,charindex('+',[授课教师编号]+'+'),'') as nvarchar(100)) from tt " +
			  " union all " +
			  " select [授课教师编号]=cast(left(Split,charindex('+',Split)-1) as nvarchar(100)),Split= cast(stuff(Split,1,charindex('+',Split),'') as nvarchar(100)) from ts where split>'' " +
			  " ), " +
			  " td as  " +
			  " (select [授课教师编号]=cast(left([授课教师编号],charindex('|',[授课教师编号]+'|')-1) as nvarchar(100)),Split=cast(stuff([授课教师编号]+'|',1,charindex('|',[授课教师编号]+'|'),'') as nvarchar(100)) from ts " +
			  " union all " +
			  " select [授课教师编号]=cast(left(Split,charindex('|',Split)-1) as nvarchar(100)),Split= cast(stuff(Split,1,charindex('|',Split),'') as nvarchar(100)) from td where split>'' " +
			  " ) " +
			  " select distinct 授课教师编号 from td option (MAXRECURSION 0)";
			xbypjs = db.GetContextVector(sql);
			
			for (int i = 0; i < xbypjs.size(); i++) {
				if(!"".equalsIgnoreCase(MyTools.StrFiltr(xbypjs.get(i)))){
					jsxbkcxx.add(xbypjs.get(i));
					Vector vec4 = new Vector(); 
					for (int j = 0; j < tempjsxbkcxx.size(); j+=3) {
						if(xbypjs.get(i).equals(tempjsxbkcxx.get(j))){
							vec4.add(MyTools.StrFiltr(tempjsxbkcxx.get(j+1)));
							vec4.add(MyTools.StrFiltr(tempjsxbkcxx.get(j+2)));
						}
					}
					jsxbkcxx.add(vec4); //【授课教师编号，【时间序列，授课周次详情】】
				}
			}
		
	
		
		sql = "select 授课教师编号,授课计划明细编号,授课周次详情 from dbo.V_规则管理_授课计划明细表 a " +
			"left join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号 = b.授课计划主表编号 " +
			"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' order by 授课教师编号 ";
		teaskjhmxbhVector2 = db.GetContextVector(sql); //【授课教师编号，授课计划明细编号，授课周次详情】
		
		for (int i = 0; i < teaskjhmxbhVector2.size(); i+=3) {
			String skjsbh = (String) teaskjhmxbhVector2.get(i);
			String skjhmxbh = (String) teaskjhmxbhVector2.get(i+1);
			String skzcxq = (String) teaskjhmxbhVector2.get(i+2);
			String [] skjsbhArray = skjsbh.split("｜");
			String [] skjhmxbhArray = skjhmxbh.split("｜");
			String [] skzcxqArray = skzcxq.split("｜");
			for (int j = 0; j < skjhmxbhArray.length; j++) {
				String skjsbh_2 = skjsbhArray[j];
				String skzcxq_2 = skzcxqArray[j];
				String [] skjsbhArray_2 = skjsbh_2.split("\\&");
				String [] skzcxqArray_2 = skzcxq_2.split("\\&");
				for (int k = 0; k < skjsbhArray_2.length; k++) {
					String skjsbh_3 = skjsbhArray_2[k];
					String [] skjsbhArray_3 = skjsbh_3.split("\\+");
					for (int l = 0; l < skjsbhArray_3.length; l++) {
						teaskjhmxbhVector.add(skjsbhArray_3[l]);
						teaskjhmxbhVector.add(skjhmxbhArray[j]);
						teaskjhmxbhVector.add(skzcxqArray_2[k]);
					}
				}
			}
		}
		
		//读取课程表明细表
		sql ="select distinct * from (select b.时间序列,b.授课计划明细编号,b.授课教师编号,b.连节相关编号,b.授课周次详情  from V_教职工基本数据子类 a  " + 
			 " inner join V_排课管理_课程表明细详情表 b on '@'+replace(replace(replace(replace(b.授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@'+a.工号+'@%'  " + 
			 " where b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and b.授课计划明细编号<>'' and b.行政班代码 not in (" + xzb + ") " +
			 " union all " + 
			 " select c.时间序列,c.授课计划明细编号,b.授课教师编号,c.连节相关编号,b.授课周次详情 from V_教职工基本数据子类 a  " + 
			 " inner join V_规则管理_授课计划明细表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%'  " + 
			 " inner join V_规则管理_固排禁排表 c on c.授课计划明细编号=b.授课计划明细编号  " + 
			 " where b.状态='1' and c.状态='1' and c.类型='2' and c.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and c.行政班代码 not in (" + xzb + ") ) t  " + 
			 " order by 时间序列 ";
		
		teapdVector = db.GetContextVector(sql);
	
		//拆分
		for (int i = 0; i < teapdVector.size(); i+=5) {
			String sjxl = (String) teapdVector.get(i);
			String skjsbh = (String) teapdVector.get(i+2);
			String skjhmxbh = (String) teapdVector.get(i+1);
			String skzcxq = (String) teapdVector.get(i+4);
			String ljxgbh = (String) teapdVector.get(i+3);
			String [] skjsbhArray = skjsbh.split("｜");
			String [] skjhmxbhArray = skjhmxbh.split("｜");
			String [] skzcxqArray = skzcxq.split("｜");
			for (int j = 0; j < skjhmxbhArray.length; j++) {
				String skjsbh_2 = skjsbhArray[j];
				String skzcxq_2 = skzcxqArray[j];
				String [] skjsbhArray_2 = skjsbh_2.split("\\&");
				String [] skzcxqArray_2 = skzcxq_2.split("\\&");
				for (int k = 0; k < skzcxqArray_2.length; k++) {
					String skjsbh_3 = skjsbhArray_2[k];
					String [] skjsbhArray_3 = skjsbh_3.split("\\+");
					for (int l = 0; l < skjsbhArray_3.length; l++) {
						teaypskjhmxbhVector.add(skjsbhArray_3[l]); //授课教师编号
						teaypskjhmxbhVector.add(skjhmxbhArray[j]); //授课计划明细编号
						teaypskjhmxbhVector.add(skzcxqArray_2[k]); //授课周次详情
						teaypskjhmxbhVector.add(ljxgbh); //连接相关编号
						teaypskjhmxbhVector.add(sjxl); //时间序列
					}
				}
			}
		}
		//===========================================================================================================
		
		//判断当前学期排课时间是否过期
		sql = "select count(*) from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and convert(nvarchar(10),排课截止时间,21) >= convert(nvarchar(10),getDate(),21)";
		if(!db.getResultFromDB(sql)){
			//返回出错信息
			return "{\"MSG\":\"当前选择学期的排课时间已过期\"}";
		}
		
		//获取每周天数和每天节数等信息
		sql = "select distinct (select count(*) from V_规则管理_学期周次表 where 学年学期编码=a.学年学期编码) as 周次,每周天数,上午节数+中午节数+下午节数+晚上节数 as 每天节数,上午节数,中午节数,下午节数,晚上节数 " +
			"from V_规则管理_学年学期表 a where a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and a.状态='1'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			xqzc = MyTools.StrFiltr(tempVec.get(0));//学期周次范围
			dayNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(1))); //每周天数
			lessonNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(2))); //每天节数
			monNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(3))); //上午节数
			noonNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(4))); //中午节数
			afternoonNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(5))); //下午节数
			eveNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(6))); //晚上节数
		}
		
		//==============================================================================================================================================
		
		//上午集合，中午集合，下午集合，每周天数集合
		Vector swVec= new Vector(); //上午集合
		Vector xwVec= new Vector(); //下午集合
		Vector zwVec= new Vector(); //中午集合
		Vector dayVec= new Vector(); //每周天数集合
		Vector set = new Vector(); //[01,All,02,half,03,All,04,half,05,half]周数全天半天集合
		int zsNum = monNum+noonNum;//中午节数+上午节数
		int ztNum = monNum+noonNum+afternoonNum;//中午节数+上午节数+下午节数
		for (int i = 1; i <= monNum; i++) { //上午
			swVec.add("0"+i);
		}
		for (int i = 1; i <= afternoonNum; i++) {//下午
			String str_xw = MyTools.StrFiltr((zsNum+i)>=10?(zsNum+i):"0"+(zsNum+i));
			xwVec.add(str_xw);
		}
		for (int i = 1; i <= noonNum; i++) {//中午
			zwVec.add(monNum+i);
		}
		for (int i = 1; i <= dayNum; i++) {//周数
			dayVec.add("0"+i);
		}
		
		
		
		
		//==============================================================================================================================================
		
		
		//获取所有需要排课的班级
//		sql = "select distinct b.行政班代码,c.总人数 from V_规则管理_授课计划明细表 a " +
//			"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
//			"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
//			"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and c.系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql = "select distinct b.行政班代码,c.总人数 from V_规则管理_授课计划明细表 a " +
			"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
//			"left join (select 行政班代码,系部代码,总人数 from V_学校班级_数据子类 " +
//			"union all " +
//			"select distinct a.教学班编号,a.系部代码,count(b.学号) from V_基础信息_教学班信息表 a " +
//			"left join V_基础信息_教学班学生信息表 b on b.教学班编号=a.教学班编号 group by a.教学班编号,a.系部代码) c on c.行政班代码=b.行政班代码 " +
			"left join V_基础信息_班级信息表 c on c.班级编号=b.行政班代码 " +
			"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and c.系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		//判断权限，如果是公共课老师操作所有专业班级,专业课老师操作自己选择的专业班级
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//		}
//		sql += " and b.专业代码 in (" + zydm + ")";
		classVec = db.GetContextVector(sql);
		
		if(classVec!=null && classVec.size()>0){
			session.setAttribute("pkjd", 5);//更新排课进度
			session.setAttribute("pkTips", "正在初始化课程表信息...");
			session.setAttribute("pkClass", "&nbsp;");
			session.setAttribute("pkCourse", "&nbsp;");
			Map allClassKcbMap = new HashMap();//所有课程表信息
			Map updateClassKcbMap = new HashMap();//所有课程表信息
			Map teaJpMap = new HashMap();//禁排信息
			Map allKywzMap = new HashMap();//班级可排时间序列信息
			Map allTeaUsedOrder = new HashMap();//所有教师已排课的时间序列信息
			Vector tsgzVec = new Vector();//特殊规则信息
			Vector skjhAndTeaVec = new Vector();//授课计划与授课教师的关系信息
			Vector allSiteInfoVec = new Vector();//所有场地信息
			Vector hbInfoVec = new Vector();//合班信息
//			Vector jsjpVec = new Vector();//教师禁排信息
			String sjcdbh = "";//实际场地编号
			String sjcdmc = "";//实际场地名称
			
			//获取当前专业课程合班信息
			sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a " +
				"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
				"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
				"where b.状态='1' and c.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//			if(this.getAuth().indexOf(pubTeacher) > -1){
//				sql += " and a.课程类型='01'";
//			}else if(this.getAuth().indexOf(majorTeacher) > -1){
//				sql += " and a.课程类型='02'" +
//					" and c.行政班代码 in (select 行政班代码 from V_学校班级_数据子类 where 专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "')";
//			}
//			sql += " and c.行政班代码 in (select 行政班代码 from V_学校班级_数据子类 where 系部代码='" + MyTools.fixSql(this.getPK_XB()) + "' " +
//				"union all " +
//				"select 教学班编号 from V_基础信息_教学班信息表 where 系部代码  = '" + MyTools.fixSql(this.getPK_XB()) + "')";
			sql += " and c.行政班代码 in (select 班级编号 from V_基础信息_班级信息表 where 系部代码='" + MyTools.fixSql(this.getPK_XB()) + "')";
			hbInfoVec = db.GetContextVector(sql);
			
			//初始化课表信息,判断是全新排课还是安排剩余课程（1为全新，2为剩余）
			if("1".equalsIgnoreCase(pkType)){
				String sqlCondition = "";//教室已用时间序列查询条件
				//拼接sql班级条件
				for(int i=0; i<classVec.size(); i+=2){
					sqlCondition += "'" + MyTools.StrFiltr(classVec.get(i)) + "',";
				}
				sqlCondition = sqlCondition.substring(0, sqlCondition.length()-1);
				
				//获取所有场地相关信息及已用时间序列
				session.setAttribute("pkjd", 10);//更新排课进度
				allSiteInfoVec = this.loadSiteInfo(pkType, sqlCondition, xqzc, classFixedClassroom);
				session.setAttribute("pkjd", 15);//更新排课进度
				Vector allData = this.initKcb(classVec, sqlCondition, dayNum, lessonNum, allSiteInfoVec, xqzc, hbInfoVec, classFixedClassroom);
				session.setAttribute("pkjd", 20);//更新排课进度
				allClassKcbMap = (Map)allData.get(0);
				allSiteInfoVec = (Vector)allData.get(1);
			}else{
				session.setAttribute("pkjd", 10);//更新排课进度
				//获取所有场地相关信息及已用时间序列
				allSiteInfoVec = this.loadSiteInfo(pkType, "", xqzc, classFixedClassroom);
				session.setAttribute("pkjd", 15);//更新排课进度
				allClassKcbMap = this.loadKcb(tsgzFlag, teaJpFlag);
				session.setAttribute("pkjd", 20);//更新排课进度
			}
			
			//判断如果是公共课排课，需要清空原课程表信息
//			if(this.getAuth().indexOf(pubTeacher)>-1 && "1".equalsIgnoreCase(pkType)){
			if("1".equalsIgnoreCase(pkType)){
				sql = "delete from V_排课管理_课程表明细表 where 课程表主表编号 in (select a.课程表主表编号 from V_排课管理_课程表主表 a " +
					//"left join (select 行政班代码 from V_学校班级_数据子类 union all select 教学班编号 from V_基础信息_教学班信息表) b on b.行政班代码=a.行政班代码 " +
					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
				//判断权限，如果是公共课老师操作所有专业班级,专业课老师操作自己选择的专业班级
//				if(this.getAuth().indexOf(majorTeacher) > -1){
//					sql += " and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//				}
				sql += " and a.行政班代码 in (" + xzb + ")";
				sql += ")";
				sqlVec.add(sql);
				
				sql = "delete from V_排课管理_课程表明细详情表 where 课程表主表编号 in (select a.课程表主表编号 from V_排课管理_课程表主表 a " +
					//"left join (select 行政班代码 from V_学校班级_数据子类 union all select 教学班编号 from V_基础信息_教学班信息表) b on b.行政班代码=a.行政班代码 " +
					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
				//判断权限，如果是公共课老师操作所有专业班级,专业课老师操作自己选择的专业班级
//				if(this.getAuth().indexOf(majorTeacher) > -1){
//					sql += " and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//				}
				sql += " and a.行政班代码 in (" + xzb + ")";
				sql += ")";
				sqlVec.add(sql);
				
//				sql = "delete from V_排课管理_公共课课程表明细表 where 课程表主表编号 in (select a.课程表主表编号 from V_排课管理_课程表主表 a " +
//					"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 where a.状态='1' " +
//					"and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')";
//				sqlVec.add(sql);
				
				sql = "delete from V_排课管理_课程表主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' ";
					//"and 行政班代码 in (select 行政班代码 from V_学校班级_数据子类 where 状态='1'";
				//判断权限，如果是公共课老师操作所有专业班级,专业课老师操作自己选择的专业班级
//				if(this.getAuth().indexOf(majorTeacher) > -1){
//					sql += " and 专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//				}
				sql += " and 行政班代码 in (" + xzb + ")";
				//sql += ")";
				sqlVec.add(sql);
			}
			
			//删除相关调课信息
			sql = "delete from V_调课管理_调课信息主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
			//判断权限，如果是公共课老师操作所有专业班级,专业课老师操作自己选择的专业班级
//			if(this.getAuth().indexOf(majorTeacher) > -1){
//				sql += " and 班级编号 in (select 行政班代码 from V_学校班级_数据子类  where 专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "')";
				//sql += " and 班级编号 in (select 行政班代码 from V_学校班级_数据子类  where 行政班代码 in (" + xzb + ") )";
			sql += " and 班级编号 in (" + xzb + ")";
//			}
			sqlVec.add(sql);
			
			sql = "delete from V_调课管理_调课信息明细表 where 调课信息主表编号 in (select 编号 from V_调课管理_调课信息主表 a " +
				//"left join V_学校班级_数据子类 b on b.行政班代码=a.班级编号 " +
				"where a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
			//判断权限，如果是公共课老师操作所有专业班级,专业课老师操作自己选择的专业班级
//			if(this.getAuth().indexOf(majorTeacher) > -1){
//				sql += " and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
				sql += " and a.班级编号 in (" + xzb + ")";
//			}
			sql += ")";
			sqlVec.add(sql);
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				session.setAttribute("pkjd", 25);//更新排课进度
				
				//判断课程表初始化是否成功
				if(allClassKcbMap.size() > 0){
					//获取所有未安排的课程信息并根据限制条件对结果集排序（包含该班级所有授课教师、场地、学科的限制条件）
					allWpkcVec = this.loadWpkc();
					
					//判断是否有未排课程
					if(allWpkcVec.size() > 0){
						//获取所有班级可排位置集合
						allKywzMap = this.loadKpwz(allClassKcbMap, dayNum, lessonNum, monNum, noonNum, afternoonNum);
						
						//获取所有除班级禁排外的禁排信息
						teaJpMap = this.loadTeaJp2();
//						jsjpVec = this.loadTeaJp2();
						
						//获取授课教师的已排课的时间序列信息
						allTeaUsedOrder = this.loadTeaUsedOrder(xqzc, pkType);
						
						//获取当前专业所有授课教师的特殊规则信息(添加每周次数字段)
						sql = "select distinct a.教师编号,a.每天节次,a.每周节次,a.每天次数,a.每周次数 from V_规则管理_特殊规则表 a " +
							"left join V_规则管理_授课计划明细表 b on b.授课教师编号 like '%'+a.教师编号+'%' " +
							"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
							"where a.状态='1' and b.状态='1' and c.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//						if(this.getAuth().indexOf(majorTeacher) > -1){
//							sql += " and c.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//						}
						sql += " and c.行政班代码 in (" + xzb + ") order by 教师编号 ";
						tsgzVec = db.GetContextVector(sql);
						
						//获取当前专业的所有班级授课计划与授课教师的关系
						sql = "select distinct a.授课计划明细编号,a.授课教师编号,a.授课周次详情 from V_规则管理_授课计划明细表 a " +
							"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
							"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//						if(this.getAuth().indexOf(majorTeacher) > -1){
//							sql += " and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//						}
						sql += " and b.行政班代码 in (" + xzb + ")";
						skjhAndTeaVec = db.GetContextVector(sql);
						
						session.setAttribute("pkjd", 30);//更新排课进度
						session.setAttribute("pkTips", "正在排课请稍后...");
						session.setAttribute("pkClass", "&nbsp;");
						session.setAttribute("pkCourse", "&nbsp;");
						
						System.out.println("---------------------------------------------------------------   排课准备数据     ---------------------------------------------------------------------------");
						System.out.println("课程表信息:" + allClassKcbMap.toString());
						System.out.println("班级可用位置信息:" + allKywzMap.toString());
						System.out.println("班级固定教室信息:" + classFixedClassroom.toString());
						System.out.println("未排课程（已排序）：" + allWpkcVec.toString());
						System.out.println("教师禁排信息：" + teaJpMap.toString());
//						System.out.println("教师禁排信息：" + jsjpVec.toString());
						System.out.println("教师已用信息：" + allTeaUsedOrder.toString());
						System.out.println("场地已用信息：" + allSiteInfoVec.toString());
						System.out.println("特殊规则信息：" + tsgzVec.toString());
						System.out.println("授课计划与授课教师的关系信息：" + skjhAndTeaVec.toString());
						System.out.println("合班信息：" + hbInfoVec.toString());
						System.out.println("教师授课计划明细集合：" + teaskjhmxbhVector.toString());
						System.out.println("教师已排授课计划明细集合：" + teaypskjhmxbhVector.toString());
						System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
						
						String curTimeOrder = ""; //课程表时间序列
						//String tempDayNum = "";
						int tempLesNum = 0;
						int mtcs = 0;//每天次数(特殊规则)
						Vector curKywzVec = new Vector();//当前班级可用时间序列
						Vector tempCurKywzVec = new Vector();
						Vector curTeaCourseVec = new Vector();//当前教师未排课程信息
						Map tempAllClassKcbMap = new HashMap();//临时所有课程表信息
						Vector tempAllWpkcVec = new Vector();//临时未排课程信息
						Map tempAllTeaUsedOrder = new HashMap();//临时所有教师已排课的时间序列信息
						Vector tempAllSiteInfoVec = new Vector();//临时所有场地信息
						String tempSiteCode = "";
						String tempSiteName = "";
						
						//课程属性
						String skjhmxbh = "";//授课计划明细编号
						String kcdm = "";//课程代码
						String kcmc = "";//课程名称
						String xzbmc = "";//行政班名称
						String hbSkjhmxbhArray[] = new String[0];//当前课程的合班课程授课计划编号
						boolean hbFlag = false;
						String xzbdm = "";//行政班代码
						int zrs = 0;//总人数
						int js = 0;//节数
						int sjjs = 0;//实际已排节数
						int lj = 0;//连节
						int lc = 0;//连次
						int sjlc = 0;//实际连次次数
						String skjsbhArray[] = new String[0];//授课教师编号
						String cdyqArray[] = new String[0];//场地要求
						String cdmcArray[] = new String[0];//场地名称
						String skzc = "";//授课周次
						String skzcDetailArray[] = new String[0];//授课周次详情
						Vector tempCourseInfo = new Vector();
						
						//产生随机位置
						Random random = new Random();
						int posIndex = 0;//班级集合中随机的班级位置
						boolean nextFlag = true;
						boolean pkFlag = true;//用于判断是否可排
						int randomNum = 0;//记录随机次数，如果超过(天数*节数)次结束循环
						int randomMaxNum = 0;//最大循环数
						Vector usedDayVec = new Vector();//已使用过的天
						String tempDay = "";
						String tempPart = "";
						String tempRanPos = "";
						String skjsbh = "";
						
						//开始排课，遍历所有未排课程
						for(int i=0; i<allWpkcVec.size(); i+=2){
							//获取当前授课教师编号,授课信息
							curTeaCourseVec = (Vector)allWpkcVec.get(i+1);
							skjsbh = MyTools.StrFiltr(allWpkcVec.get(i));
							
							//遍历当前授课教师所有未安排的课程
							for(int j=0; j<curTeaCourseVec.size(); j+=16){
								kcmc = MyTools.StrFiltr(curTeaCourseVec.get(j+14));
								xzbmc = MyTools.StrFiltr(curTeaCourseVec.get(j+15));
								session.setAttribute("pkClass", xzbmc);
								session.setAttribute("pkCourse", kcmc);
								
								hbFlag = false;
								js = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+4)));//节数
								sjjs = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+5)));//实际已排节数
								/**判断是否还有剩余节数未排*/
								if(js-sjjs <= 0){
									continue;
								}
								//课程信息
								skjhmxbh = MyTools.StrFiltr(curTeaCourseVec.get(j));//授课计划明细编号
								/**遍历所有合班信息，查询当前课程是否有合班设置*/
								for(int k=0; k<hbInfoVec.size(); k++){
									if(MyTools.StrFiltr(hbInfoVec.get(k)).indexOf(skjhmxbh) > -1){
										hbSkjhmxbhArray = MyTools.StrFiltr(hbInfoVec.get(k)).split("\\+");
										hbFlag = true;
									}
								}
								kcdm = MyTools.StrFiltr(curTeaCourseVec.get(j+1));//课程代码
								xzbdm = MyTools.StrFiltr(curTeaCourseVec.get(j+2));//行政班代码
								zrs = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+3)));//总人数
								lj = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+6)));//连节
								lc = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+7)));
								sjlc = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+8)));//实际连次次数
								skjsbhArray = MyTools.StrFiltr(curTeaCourseVec.get(j+9)).split("&");//授课教师编号
								cdyqArray = MyTools.StrFiltr(curTeaCourseVec.get(j+10)).split("&");//场地要求
								cdmcArray = MyTools.StrFiltr(curTeaCourseVec.get(j+11)).split("&");//场地名称
								skzc = MyTools.StrFiltr(curTeaCourseVec.get(j+12));//授课周次
								skzcDetailArray = MyTools.StrFiltr(curTeaCourseVec.get(j+13)).split("&");//授课周次详情
								/**判断当前课程是否为合班课程，如果是的话班级总人数更改为所有合班班级人数总和*/
								if(hbFlag == true){
									zrs = this.getStuTotalNum(hbSkjhmxbhArray, curTeaCourseVec, skjhmxbh, zrs);
								}
//								errorMsg += "\r\n\r\n班级名称:"+xzbmc+"---课程名称:"+kcmc+"---教师编号:"+MyTools.StrFiltr(curTeaCourseVec.get(j+9))+"---节数:"+js+"---连节："+lj+"---连次:"+lc+"\r\n";
								tempCourseInfo.clear();
								//tempCourseInfo.add(skjhmxbh);
								tempCourseInfo.add(kcdm);//课程代码
								tempCourseInfo.add(xzbdm);//行政班代码
								tempCourseInfo.add(zrs);//总人数
								//tempCourseInfo.add(js);//节数
								//tempCourseInfo.add(sjjs);//实际已排节数
								tempCourseInfo.add(lj);//连节
								//tempCourseInfo.add(lc);//连次
								//tempCourseInfo.add(sjlc);//实际连次次数
								tempCourseInfo.add(skjsbhArray);//授课教师编号
								tempCourseInfo.add(cdyqArray);//场地要求
								//tempCourseInfo.add(cdmcArray);//场地名称
								//tempCourseInfo.add(skzc);//授课周次
								tempCourseInfo.add(skzcDetailArray);//授课周次详情
								
								curKywzVec = (Vector)allKywzMap.get(xzbdm);//获取当前班级可排位置
								tempCurKywzVec = (Vector)curKywzVec.clone();
								nextFlag = true;
								randomNum = 0;
								usedDayVec.clear();
								set.clear();
								tempDay = "";
								tempPart = "";
								tempRanPos = "";
								
								boolean pd = false; //判断数据是否存在
								
								//===================================================================================
								
								boolean pdtj = false; //判断数据是否存在
								//移除时间序列中不可排的位置
								for (int z = 0; z < jsxbkcxx.size(); z+=2) {
									if(skjsbh.equalsIgnoreCase((String) jsxbkcxx.get(z))){ //教师编号相同
										pdtj = true;
										Vector jsypsjxl = (Vector) jsxbkcxx.get(z+1); //教师已排时间序列
										for (int l = 0; l < dayVec.size(); l++) { //[01,02,03,04,05]
											boolean pdsw = false;
											boolean pdxw = false;
											String tempDayVec = MyTools.StrFiltr(dayVec.get(l));
											for (int k = 0; k < jsypsjxl.size(); k+=2) {
												String xl = MyTools.StrFiltr(jsypsjxl.get(k)); //序列
												String skzcxq =  MyTools.StrFiltr(jsypsjxl.get(k+1)); //授课周次详情
												String str1_xl = xl.substring(0, 2);
												String str2_xl = xl.substring(2);
												Vector tempvec1 = this.formatSkzc(skzcxq, xqzc); //拆分其他系部已排课信息的授课周次
												Vector tempvec2 = this.formatSkzc(skzc, xqzc); //拆分当前排课信息的授课周次
												if(tempDayVec.equalsIgnoreCase(str1_xl)) {
													if(swVec.indexOf(str2_xl) > -1) {
														if(this.judgeSkzc(tempvec1, tempvec2)){
															pdsw = true;
														}
													}
													if(xwVec.indexOf(str2_xl) > -1) {
														if(this.judgeSkzc(tempvec1, tempvec2)){
															pdxw = true;
														}
													}
												}
												
											}
											
											if(pdsw==true && pdxw == true) {
												//移除不可排的时间序列
												for (int n = 0; n < swVec.size(); n++) {
													String str = tempDayVec+swVec.get(n);
													tempCurKywzVec.remove(str);
												}
												for (int n = 0; n < xwVec.size(); n++) {
													String str2 = tempDayVec+xwVec.get(n);
													tempCurKywzVec.remove(str2);
												}
												
											//该教师在其他系部上午有课，下午没课	
											}else if(pdsw==true && pdxw == false) {
												//移除不可排的时间序列
												for (int n = 0; n < swVec.size(); n++) {
													String str = tempDayVec+swVec.get(n);
													tempCurKywzVec.remove(str);
												}
												if(set.indexOf(tempDayVec) < 0) {
													set.add(tempDayVec);
													set.add("half");
												}
												
											//该教师在其他系部上午没课，下午有课
											}else if(pdsw==false && pdxw == true) {
												//移除不可排的时间序列
												for (int n = 0; n < xwVec.size(); n++) {
													String str2 = tempDayVec+xwVec.get(n);
													tempCurKywzVec.remove(str2);
												}
												if(set.indexOf(tempDayVec) < 0) {
													set.add(tempDayVec);
													set.add("half");
												}
												
											}else {
												if(set.indexOf(tempDayVec) < 0) {
													set.add(tempDayVec);
													set.add("all");
												}
											}
											
										}
										if(pdtj) {
											break;
										}
									}
								}
								
								if(pdtj == false) {
									for (int l = 0; l < dayVec.size(); l++) { //[01,02,03,04,05]
										set.add(dayVec.get(l));
										set.add("all");
									}
								}
								
								/**判断教师禁排信息*/
								//教师禁排信息
								if(teaJpMap!=null && teaJpMap.containsKey(skjsbh)){
									Vector teajpVec = (Vector) teaJpMap.get(skjsbh);
									if(teajpVec.size()>0){
										for (int m = 0; m < teajpVec.size(); m++) {
											tempCurKywzVec.remove(teajpVec.get(m));
										}
										
									}
								}
								
								//当前教师可排位置的数量
								randomMaxNum = tempCurKywzVec.size();
								
								//===================================================================================
								
								while(nextFlag){
									pkFlag = true;
									//创建临时数据
									tempAllClassKcbMap.clear();
									tempAllClassKcbMap.putAll(allClassKcbMap);//所有班级课程表信息
									tempAllWpkcVec = (Vector)allWpkcVec.clone();//复制未排课程信息
									tempAllTeaUsedOrder.clear();//复制教师授课时间信息
									tempAllTeaUsedOrder.putAll(allTeaUsedOrder);
									tempAllSiteInfoVec = (Vector)allSiteInfoVec.clone();//复制场地信息
									
									/**判断如果当前课程已经随机了所有单元格，还没能够排上的话，自动放弃*/
									//是否还有可排位置
									if(randomNum>randomMaxNum || tempCurKywzVec==null || tempCurKywzVec.size()==0){
//										errorMsg += "未成功排课";
										nextFlag = false;
										continue;
									}
									
									//判断如果可用位置只有一个的话不随机
									if(tempCurKywzVec.size() > 1){
										posIndex = random.nextInt(tempCurKywzVec.size()-1);//随机位置
									}else{
										posIndex = 0;
									}
									
									curTimeOrder = MyTools.StrFiltr(tempCurKywzVec.get(posIndex));//当前安排课程的时间序列
//									errorMsg += "----------------------------------------"+curTimeOrder+"----------------------------------------\r\n";
									tempRanPos = "";
									tempDay = curTimeOrder.substring(0, 2); //星期几序列
									tempPart = curTimeOrder.substring(2); //第几节序列
									
									
									
									//判断如果当前课程在当天已排过课，尝试在可用位置中寻找没有排过课的某天，如果没有，则使用当前随机到的位置继续排课
									if(usedDayVec.indexOf(tempDay) > -1){
										for(int z=0; z<tempCurKywzVec.size(); z++){
											if(usedDayVec.indexOf(MyTools.StrFiltr(tempCurKywzVec.get(z)).substring(0, 2)) < 0 && "all".equalsIgnoreCase(MyTools.StrFiltr(set.get(set.indexOf(MyTools.StrFiltr(tempCurKywzVec.get(z)).substring(0, 2))+1)))){
												tempRanPos = MyTools.StrFiltr(tempCurKywzVec.get(z));
												break;
											}
										}
										
										if("".equalsIgnoreCase(tempRanPos)){
											for(int z=0; z<tempCurKywzVec.size(); z++){
												if(usedDayVec.indexOf(MyTools.StrFiltr(tempCurKywzVec.get(z)).substring(0, 2)) < 0){
													tempRanPos = MyTools.StrFiltr(tempCurKywzVec.get(z));
													break;
												}
											}
										}
									}else {
										if(!"all".equalsIgnoreCase(MyTools.StrFiltr(set.get(set.indexOf(tempDay)+1))) && set.indexOf("all") > -1) {
											for(int z=0; z<tempCurKywzVec.size(); z++){
												if(usedDayVec.indexOf(MyTools.StrFiltr(tempCurKywzVec.get(z)).substring(0, 2)) < 0 && "all".equalsIgnoreCase(MyTools.StrFiltr(set.get(set.indexOf(MyTools.StrFiltr(tempCurKywzVec.get(z)).substring(0, 2))+1)))){
													tempRanPos = MyTools.StrFiltr(tempCurKywzVec.get(z));
													break;
												}
												
											}
										}
										
									}
									
									
									if(!"".equalsIgnoreCase(tempRanPos)){
										curTimeOrder = tempRanPos;
										tempDay = curTimeOrder.substring(0, 2); //星期几序列
										tempPart = curTimeOrder.substring(2); //第几节序列
									}
									tempLesNum = MyTools.StringToInt(curTimeOrder.substring(2));
									
									tempCurKywzVec.remove(curTimeOrder);
									
									

									/**判断如果连节是偶数节的话，排在时间序列单数位置*/
									/*
									if(lj%2 == 0){
										if(MyTools.StringToInt(curTimeOrder)%2 == 0){
											errorMsg += "偶数节需排在时间序列单数位置\r\n";
											randomNum++;
											continue;
										}
									}
									*/
									
									/**判断如果设置了连次，检查当前实际连次次数，如果等于每周允许连次次数，接下去该课程的安排按照单节安排（忽略连节设置）*/
									if(lc > 0){
										if(lc == sjlc){
											lj = 1;
											tempCourseInfo.set(3, lj);//连节
										}
									}
									
									/**判断设置了连节的情况*/
									if(lj > 1){
										//判断未排课程剩余节数如果小于设置的的连节数的话，该课程接下去的安排，忽略连节设置
										if((js-sjjs) < lj){
											lj = 1;
											tempCourseInfo.set(3, lj);//连节
										}else{
											//连节数量超过上午或下午，判断当前随机的到位置是否可排进所有连节课程
											if(lj>monNum && lj>afternoonNum){
												if((tempLesNum+lj-1) > (monNum+afternoonNum)){
//													errorMsg += "连节数量超过上午或下午\r\n";
													randomNum++;
													continue;
												}
											}else{
												//检查当前随机到的时间序列是否符合要求(如上午从当前时间序列开始计算，剩余节数是否足够将连节课程安排完)
												int compareNum = 0;
												//判断是上午，下午，还是晚上
												if(tempLesNum <= monNum)
													compareNum = monNum;
												else if(tempLesNum <= (monNum+noonNum))
													compareNum = monNum+noonNum;
												else if(tempLesNum <= (monNum+noonNum+afternoonNum))
													compareNum = monNum+noonNum+afternoonNum;
												else if(tempLesNum <= (monNum+noonNum+afternoonNum+eveNum))
													compareNum = monNum+noonNum+afternoonNum+eveNum;
												
												if(tempLesNum+(lj-1) > compareNum){
//													errorMsg += "连节数量超过上午或下午\r\n";
													randomNum++;
													continue;
												}
											}
										}
									}
									
									//获取所有连节时间序列
									String timeOrderArray[] = new String[lj];
									String tempOrder = curTimeOrder;
									for(int k=0; k<lj; k++){
										if(k > 0){
											tempOrder = tempOrder.substring(0, 2) + ((tempLesNum+k)<10?"0"+(tempLesNum+k):tempLesNum+k);
										}
										timeOrderArray[k] = tempOrder;
									}
									
									/**判断所有时间序列是否为可用*/
									for(int k=1; k<timeOrderArray.length; k++){
										if(tempCurKywzVec.indexOf(timeOrderArray[k]) < 0){
											pkFlag = false;
											break;
											
										}
									}
									if(pkFlag == false){
//										errorMsg += "连节的课程不是所有时间序列都可用\r\n";
										randomNum++;
										continue;
									}
									
									/**检查当前课程的教师/教室已用情况是否冲突*/
									tempVec = this.checkCourseUsed(tempCourseInfo, curTimeOrder, xqzc, lessonNum, teaJpMap, teaJpFlag, tsgzVec, 
											tsgzFlag, tempAllTeaUsedOrder, tempAllSiteInfoVec, tempAllClassKcbMap, skjhAndTeaVec, classFixedClassroom, teaskjhmxbhVector, teaypskjhmxbhVector);
									if((Boolean)tempVec.get(0) == false){
//										errorMsg += "教师/教室已用\r\n";
										randomNum++;
										continue;
									}
									tempSiteCode = MyTools.StrFiltr(tempVec.get(1));
									tempSiteName = MyTools.StrFiltr(tempVec.get(2));
									
									Vector beforePjkc = new Vector();//之前的拼接课程（授课计划明细编号）
									Vector afterPjkc = new Vector();//之后的拼接课程
									
									/**判断如果是合班课程,查询所有合班班级的课程拼接信息
									 * 如果不是，直接查询当前课程的拼接信息*/
									if(hbFlag == true){
										Vector tempClassKywz = new Vector();//临时合班班级可用位置
										
										//判断其他合班班级当前位置是否可排
										for(int a=0; a<hbSkjhmxbhArray.length; a++){
											if(!skjhmxbh.equalsIgnoreCase(hbSkjhmxbhArray[a])){
												tempClassKywz = (Vector)allKywzMap.get(MyTools.StrFiltr(curTeaCourseVec.get(curTeaCourseVec.indexOf(hbSkjhmxbhArray[a])+2)));
												//判断合班班级的课表中,当前位置是否可用
												if(tempClassKywz==null || tempClassKywz.indexOf(curTimeOrder)<0){
													pkFlag = false;
													break;
												}
											}
										}
										if(pkFlag ==  false){
											randomNum++;
											continue;
										}

										String hbClassCode = "";
										//检查所有合班课程的拼接情况
										for(int a=0; a<hbSkjhmxbhArray.length; a++){
											//获取合班课程相关信息
											for(int b=0; b<curTeaCourseVec.size(); b+=16){
												if(hbSkjhmxbhArray[a].equalsIgnoreCase(MyTools.StrFiltr(curTeaCourseVec.get(b)))){
													hbClassCode = MyTools.StrFiltr(curTeaCourseVec.get(b+2));
													
													//检查当前是否可以拼接周次 2017-7-1
													if(!("1-"+xqzc).equalsIgnoreCase(skzc)){
														Vector vec = new Vector();
														vec = this.checkPjkc(hbSkjhmxbhArray[a], curTimeOrder, xqzc, skzc, lj, tempAllWpkcVec, tempAllTeaUsedOrder, tempAllSiteInfoVec, 
																teaJpMap, teaJpFlag, tsgzVec, tsgzFlag, lessonNum, tempAllClassKcbMap, skjhAndTeaVec, curKywzVec, hbClassCode, zrs, hbInfoVec, classFixedClassroom, teaskjhmxbhVector, teaypskjhmxbhVector, jsxbkcxx, swVec, xwVec);
														if(vec.size() > 0){
															beforePjkc = (Vector)vec.get(0);
															afterPjkc = (Vector)vec.get(1);
														}
													}
													
													/**如果完全符合条件，更新相关信息临时数据*/
													//更新未排课程信息
													this.updateWpkc(tempAllWpkcVec, hbSkjhmxbhArray[a], skjsbhArray, lj);//更新未排课程信息
													//更新班级课程表信息
													this.updateClassKcb(hbClassCode, timeOrderArray, hbSkjhmxbhArray[a], tempSiteCode, tempSiteName, beforePjkc, afterPjkc, tempAllClassKcbMap, pkType, updateClassKcbMap);
													//更新班级可用位置信息
													this.updateKywz(allKywzMap, hbClassCode, timeOrderArray);
												}
											}
										}
									}else{
										//检查当前是否可以拼接周次 2017-7-1
										if(!("1-"+xqzc).equalsIgnoreCase(skzc)){
											Vector vec = new Vector();
											vec = this.checkPjkc(skjhmxbh, curTimeOrder, xqzc, skzc, lj, tempAllWpkcVec, tempAllTeaUsedOrder, tempAllSiteInfoVec, 
													teaJpMap, teaJpFlag, tsgzVec, tsgzFlag, lessonNum, tempAllClassKcbMap, skjhAndTeaVec, curKywzVec, xzbdm, zrs, hbInfoVec, classFixedClassroom, teaskjhmxbhVector, teaypskjhmxbhVector, jsxbkcxx, swVec, xwVec);
											
											if(vec.size() > 0){
												beforePjkc = (Vector)vec.get(0);
												afterPjkc = (Vector)vec.get(1);
											}
										}
										
										/**如果完全符合条件，更新相关信息临时数据*/
										//更新未排课程信息
										this.updateWpkc(tempAllWpkcVec, skjhmxbh, skjsbhArray, lj);//更新未排课程信息
										//更新班级课程表信息
										this.updateClassKcb(xzbdm, timeOrderArray, skjhmxbh, tempSiteCode, tempSiteName, beforePjkc, afterPjkc, tempAllClassKcbMap, pkType, updateClassKcbMap);
										//更新班级可用位置信息
										this.updateKywz(allKywzMap, xzbdm, timeOrderArray);
									}
									
									/**更新正式数据*/
									//更新实际已排节数
									sjjs += lj;
//									errorMsg += "OK\r\n";
									//更新实际连次次数
									if(lj > 1){
										sjlc++;
									}
									allClassKcbMap.clear();//更新所有班级课程表信息
									allClassKcbMap.putAll(tempAllClassKcbMap);
									allWpkcVec = (Vector)tempAllWpkcVec.clone();//更新未排课程信息
									allTeaUsedOrder.clear();//更新教师已用时间序列信息
									allTeaUsedOrder.putAll(tempAllTeaUsedOrder);
									allSiteInfoVec = (Vector)tempAllSiteInfoVec.clone();//更新场地已用时间序列信息
									//判断当前课程是否还有剩余节数需要排(设置的总节数等于实际已排节数),没有的话更改状态，退出循环。
									if(js <= sjjs){
										nextFlag = false;
										curTeaCourseVec = (Vector)allWpkcVec.get(i+1);
									}
									
									//判断如果当前已用星期几信息没有当前的星期几，添加信息
									if(usedDayVec.indexOf(tempDay) < 0){
										usedDayVec.add(tempDay);
									}
								}
							}
							session.setAttribute("pkjd", MyTools.StringToInt(MyTools.StrFiltr(session.getAttribute("pkjd")))+(100-35)/(allWpkcVec.size()/2));//更新排课进度
						}
					}
					
					session.setAttribute("pkjd", 95);//更新排课进度
					session.setAttribute("pkTips", "正在保存课程表信息...");
					session.setAttribute("pkClass", "&nbsp;");
					session.setAttribute("pkCourse", "&nbsp;");
					
					//将所有课程表信息插入数据库
					if(this.updateDataToDB(dayNum, lessonNum, allClassKcbMap, allWpkcVec, pkType, updateClassKcbMap)){
						this.MSG = "自动排课成功";
						
						for(int i=0; i<classVec.size(); i+=2){
							//生成周课表信息
							this.addWeekDetail(this.getPK_XNXQBM(), xqzc, MyTools.StrFiltr(classVec.get(i)));
						}
						
						session.setAttribute("pkjd", 100);//更新排课进度
					}else{
						this.MSG = "自动排课失败";
					}
				}else{
					this.MSG = "课程表初始化失败";
				}
			}else{
				this.MSG = "清空数据失败";
			}
		}else{
			this.MSG = "未设置授课计划";
		}
		
		//获取返回信息
		result = loadReturnInfo(allWpkcVec);
//		WriteStringToFile(errorMsg);
		return result;
	}
	
	/**
	 * 检查课程的教师/教室已用情况是否冲突
	 * @date:2015-08-10
	 * @author:yeq
	 * @param courseInfo 课程信息
	 * @param curTimeOrder 当前时间序列
	 * @param xqzc 学期周次数
	 * @param lessonNum 每天总节数
	 * @param teaJpMap 教师禁排信息
	 * @param teaJpFlag 用于判断是否检查教师禁排
	 * @param tsgzVec 特殊规则信息
	 * @param tsgzFlag 用于判断是否检查特殊规则
	 * @param tempAllWpkcVec 所有未排课程
	 * @param tempAllTeaUsedOrder 教师已用时间信息
	 * @param tempAllSiteInfoVec 教室已用时间信息
	 * @param tempAllClassKcbMap 所有班级课表
	 * @param skjhAndTeaVec 授课计划与教师关系
	 * @param classFixedClassroom 班级固定教室编号
	 * @param teaskjhmxbhVector 教师授课计划明细集合
	 * @param teaypskjhmxbhVector 教师已排授课计划明细集合
	 * @return Vector 课程表
	 */
	public Vector checkCourseUsed(Vector courseInfo, String curTimeOrder, String xqzc, int lessonNum, Map teaJpMap, String teaJpFlag, Vector tsgzVec, 
			String tsgzFlag, Map tempAllTeaUsedOrder, Vector tempAllSiteInfoVec, Map tempAllClassKcbMap, Vector skjhAndTeaVec, Vector classFixedClassroom, Vector teaskjhmxbhVector, Vector teaypskjhmxbhVector){
		Vector resultVec = new Vector();
		Vector tempVec = null;
		boolean pkFlag;
		//课程信息
		String kcdm = MyTools.StrFiltr(courseInfo.get(0));//课程代码
		String xzbdm = MyTools.StrFiltr(courseInfo.get(1));//行政班代码
		int zrs = MyTools.StringToInt(MyTools.StrFiltr(courseInfo.get(2)));//总人数
		int lj = MyTools.StringToInt(MyTools.StrFiltr(courseInfo.get(3)));//连节
		String skjsbhArray[] = (String[])courseInfo.get(4);//授课教师编号
		String cdyqArray[] = (String[])courseInfo.get(5);//场地要求
		String skzcDetailArray[] = (String[])courseInfo.get(6);//授课周次详情
		String tempSiteCode = "";
		String tempSiteName = "";
		
		//检查所有授课教师是否允许在当前位置排课
		//如果课程设置了连节的情况下，需要判断所有时间序列是否满足要求
		pkFlag = this.checkTea(skjsbhArray, teaJpMap, teaJpFlag, tempAllTeaUsedOrder, tsgzVec, tsgzFlag, lessonNum, tempAllClassKcbMap, 
				skjhAndTeaVec, kcdm, xzbdm, lj, curTimeOrder, skzcDetailArray, xqzc, teaskjhmxbhVector, teaypskjhmxbhVector);
		if(pkFlag == false){//判断如果教师不可用，跳出循环
			resultVec.add(false);
			return resultVec;
		}
		
		//检查当前课程是否有场地要求，有的话检查场地是否满足要求，没有的话随机分配教室
		tempVec = this.checkSite2(cdyqArray, curTimeOrder, lj, skzcDetailArray, xqzc, tempAllSiteInfoVec, zrs, classFixedClassroom, xzbdm);
		pkFlag = (Boolean)tempVec.get(0);
		if(pkFlag == false){
			resultVec.add(false);
			return resultVec;
		}
		tempSiteCode = MyTools.StrFiltr(tempVec.get(1));//实际场地编号
		tempSiteName = MyTools.StrFiltr(tempVec.get(2));//实际场地名称
		
		resultVec.add(true);
		resultVec.add(tempSiteCode);
		resultVec.add(tempSiteName);
		return resultVec;
	}
	
	/**获取合班班级学生总人数
	 * @date:2015-08-11
	 * @author:yeq
	 * @param skjhmxbhArray 所有合班的授课计划明细编号
	 * @param curTeaCourseVec 当前教师所有课程
	 * @param curSkjhmxbh 当前班级授课计划明细编号
	 * @param curStuNum 当前班级人数
	 * @return int 学生总人数
	 **/
	public int getStuTotalNum(String[] skjhmxbhArray, Vector curTeaCourseVec, String curSkjhmxbh, int curStuNum){
		int totalNum = curStuNum;
		String tempSkjhmxbh = "";
				
		for(int i=0; i<skjhmxbhArray.length; i++){
			tempSkjhmxbh = skjhmxbhArray[i];
					
			if(!curSkjhmxbh.equalsIgnoreCase(tempSkjhmxbh)){
				for(int j=0; j<curTeaCourseVec.size(); j+=16){
					if(tempSkjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(curTeaCourseVec.get(j)))){
						totalNum += MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+3)));
					}
				}
			}
		}
		
		return totalNum;
	}
	
	/**
	 * 获取初始化班级课程表
	 * 初始化所有班级课程表信息（并完成所有固排和班级禁排）
	 * @date:2015-05-29
	 * @author:yeq
	 * @param classVec 所有班级
	 * @param sqlCondition 教室已用时间序列查询条件
	 * @param dayNum 每周天数
	 * @param lessonNum 每天节数
	 * @param allSiteInfoVec 所有教室相关相关信息
	 * @param xqzc 学期周次范围
	 * @param hbInfoVec 合班信息
	 * @return Map 所有班级课程表信息
	 * @throws SQLException
	 */
	public Vector initKcb(Vector classVec, String sqlCondition, int dayNum, int lessonNum, Vector allSiteInfoVec, String xqzc, Vector hbInfoVec, Vector classFixedClassroom) throws SQLException{
		Vector vec = new Vector();
		Vector tempVec = new Vector();
		boolean flag = true;
		boolean hbFlag = false;
		String sql = "";
		Map kcbMap = new HashMap();//所有课程表信息
		Map classGpjpMap = new HashMap();//班级固排禁排集合
		String timeOrder = ""; //课程表时间序列
		String tempTimeOrder = "";
		int tempNum = 0;
		String tempDayNum = "";
		String tempLesNum = "";
		String curClass = "";//当前班级
		Map curGpjpMap = new HashMap();//当前班级固排禁排信息
		Map curClassMap = new HashMap();//当前班级课程表
		Vector curGpjpInfo = new Vector();//当前时间序列固排禁排信息
		Vector curOrderInfo = new Vector();//当前时间序列信息
		Vector pubCourseVec = new Vector();//公共课信息
		Vector curPubCourseVec = new Vector();//当前班级公共课信息
		String tempSkjhmxbh = "";//授课计划明细编号
		String tempSkjhmxbhArray[] = new String[0];
//		String sqlSkjhmxbh = "";
		Vector tempSkjhVec = new Vector();
		Vector sqlVec = new Vector();
		String tempLjbh = "";//连节相关编号
		String tempState = "";
		String skjhmxbhArray[] = new String[0];
		String cdyqArray[] = new String[0];
		String skzcArray[] = new String[0];
		String tempCdyqArray[] = new String[0];
		String tempSkzcArray[] = new String[0];
		
		String tempSiteCode = "";
		String tempSiteName = "";
		String tempSkzc = "";
		Vector siteVec = null;
		Vector resultVec = new Vector();
		int presonNum = 0;
		String type = "";
		int ljNum = 1;
		
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		//==========================================================================================================
		Vector vec2 = null;//班级代码
//		String zydm = "";
		String xzbdm = "";
		
		//sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = ( select 系部代码 from dbo.V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')";
//		sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
//		vec2 = db.GetContextVector(sql);
//		
//		if(vec2.size() > 0){
//			for (int i = 0; i < vec2.size(); i++) {
//				zydm += "'" + vec2.get(i) + "',";
//			}
//		}
//		zydm = zydm.substring(0, zydm.length()-1);
		
		//sql = "select distinct(行政班代码) from (select 行政班代码,系部代码 from V_学校班级_数据子类 union all select 教学班编号,系部代码 from V_基础信息_教学班信息表) a where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql = "select distinct 班级编号 from V_基础信息_班级信息表 where 系部代码='" + MyTools.fixSql(this.getPK_XB()) + "'";
		vec2 = db.GetContextVector(sql);
		
		if(vec2.size() > 0){
			for (int i=0; i <vec2.size(); i++) {
				xzbdm += "'" + MyTools.StrFiltr(vec2.get(i)) + "',";
			}
		}
		xzbdm = xzbdm.substring(0, xzbdm.length()-1);
		//===========================================================================================================
		
		//获取固排禁排课程信息
		//判断如果是公共课排课需要读取所有固排禁排课程信息
//		if(this.getAuth().indexOf(pubTeacher) > -1){
//			sql = "select a.行政班代码,c.总人数,a.时间序列,a.类型,a.授课计划明细编号,a.预设场地编号,a.预设场地名称,b.授课周次详情,a.连节相关编号 from V_规则管理_固排禁排表 a " +
//				"left join V_规则管理_授课计划明细表 b on b.授课计划明细编号=a.授课计划明细编号 " +
//				"left join V_学校班级_数据子类 c on c.行政班代码=a.行政班代码 " +
//				"where (b.状态 ='1' or b.状态 is null) and a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and a.行政班代码 in (" + sqlCondition + ") " +
//				"and a.类型 in ('2','3') ";
//			sql += "order by a.行政班代码,a.时间序列,cast((case substring(b.授课周次详情, 2, 1) when '#' then substring(b.授课周次详情, 1, 1) when '-' then substring(b.授课周次详情, 1, 1) when 'd' then '1' when 'v' then '2' " +
//				"else substring(b.授课周次详情, 1, 2) end) as int),a.授课计划明细编号";
//			vec = db.GetContextVector(sql);
//		}else if(this.getAuth().indexOf(majorTeacher) > -1){
//			vec = new Vector();
//		}
		//获取固排课程信息
//		sql = "select a.行政班代码,d.总人数,a.时间序列,a.类型,a.授课计划明细编号,a.预设场地编号,a.预设场地名称,b.授课周次详情,a.连节相关编号 from V_规则管理_固排禁排表 a " +
//			"left join V_规则管理_授课计划明细表 b on b.授课计划明细编号=a.授课计划明细编号 " +
//			"left join V_学校班级_数据子类 c on c.行政班代码=a.行政班代码 " +
//			" left join (select 行政班代码, COUNT(*) as 总人数 from dbo.V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码 in (" + xzbdm + " ) group by 行政班代码) d  on d.行政班代码=c.行政班代码  " + 
//			"where b.状态 ='1' and a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//			"and a.行政班代码 in (" + sqlCondition + ") " +
////			"and a.禁排类型  = 'bj'" +
////			"and (a.类型='3' or (a.类型='2'";
//			"and (a.类型='2'";
//		//判断公共课教师排课还是专业课教师排课
////		if(this.getAuth().indexOf(pubTeacher) > -1){
////			sql += " and b.课程类型='01'";
////		}else if(this.getAuth().indexOf(majorTeacher) > -1){
////			sql += " and b.课程类型='02'";
////		}
////		sql += ")) order by a.行政班代码,a.时间序列,cast((case substring(b.授课周次详情, 2, 1) " +
//		sql += ") order by a.行政班代码,a.时间序列,cast((case substring(b.授课周次详情, 2, 1) " +
//				"when '#' then substring(b.授课周次详情, 1, 1) " +
//				"when '-' then substring(b.授课周次详情, 1, 1) " +
//				"when '&' then substring(b.授课周次详情, 1, 1) " +
//				"when 'd' then '1' when 'v' then '2' " +
//				"else substring(b.授课周次详情, 1, 2) end) as int),a.授课计划明细编号";
//		vec = db.GetContextVector(sql);
		
		//固排
		sql = " select a.行政班代码,a.总人数,a.时间序列,a.类型,a.授课计划明细编号,a.预设场地编号,a.预设场地名称,a.授课周次详情,a.连节相关编号 " +
			"from ( " +
			"select a.行政班代码,c.总人数,a.时间序列,a.类型,a.授课计划明细编号,a.预设场地编号,a.预设场地名称,b.授课周次详情,a.连节相关编号 from V_规则管理_固排禁排表 a " +
			"left join V_规则管理_授课计划明细表 b on b.授课计划明细编号=a.授课计划明细编号 " +
//			"left join (select 行政班代码,系部代码,总人数 from V_学校班级_数据子类 " +
//			"union all " +
//			"select distinct a.教学班编号,a.系部代码,count(b.学号) from V_基础信息_教学班信息表 a " +
//			"left join V_基础信息_教学班学生信息表 b on b.教学班编号=a.教学班编号 group by a.教学班编号,a.系部代码) c on c.行政班代码=a.行政班代码 " +
			"left join V_基础信息_班级信息表 c on c.班级编号=a.行政班代码 " +
			"where b.状态 ='1' and a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			"and a.行政班代码 in (" + sqlCondition + ") " +
			"and a.类型='2' ";
		//禁排
		sql += " union all " + 
			"select a.行政班代码,c.总人数,a.时间序列,a.类型,a.授课计划明细编号,a.预设场地编号,a.预设场地名称,'1-" + MyTools.fixSql(xqzc) + "' as 授课周次详情,a.连节相关编号 " +
			"from V_规则管理_固排禁排表 a " +
//			"left join (select 行政班代码,系部代码,总人数 from V_学校班级_数据子类 " +
//			"union all " +
//			"select distinct a.教学班编号,a.系部代码,count(b.学号) from V_基础信息_教学班信息表 a " +
//			"left join V_基础信息_教学班学生信息表 b on b.教学班编号=a.教学班编号 group by a.教学班编号,a.系部代码) c on c.行政班代码=a.行政班代码  " +
			"left join V_基础信息_班级信息表 c on c.班级编号=a.行政班代码 " +
			" where  a.状态='1' and a.类型 ='3' and a.禁排类型 = 'bj' and a.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM()) + "%' " +
			" ) a ";
		sql += " order by a.行政班代码,a.时间序列,cast((case substring(a.授课周次详情, 2, 1) " +
			"when '#' then substring(a.授课周次详情, 1, 1) " +
			"when '-' then substring(a.授课周次详情, 1, 1) " +
			"when '&' then substring(a.授课周次详情, 1, 1) " +
			"when 'd' then '1' when 'v' then '2' " +
			"else substring(a.授课周次详情, 1, 2) end) as int),a.授课计划明细编号";
		vec = db.GetContextVector(sql);
		
		if(vec.size() > 0){ //判断是否有固排禁排信息
			String classCode = "";
			Map courseInfo = new HashMap();//用于存放当前班级所有固排禁排信息
			Vector courseInfoVec = new Vector();
			
			//遍历所有固排禁排信息
			for(int i=0; i<vec.size(); i+=9){
				//判断是否另一个班级
				if(!classCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						courseInfoVec = new Vector();
						courseInfoVec.add(presonNum);//班级人数
						courseInfoVec.add(type);//类型
						courseInfoVec.add(tempSkjhmxbh);//授课计划明细编号
						courseInfoVec.add(tempSiteCode);//预设场地编号
						courseInfoVec.add(tempSiteName);//预设场地名称
						courseInfoVec.add(tempSkzc);//授课周次
						courseInfoVec.add(tempLjbh);//连节相关编号
						courseInfo.put(timeOrder, courseInfoVec);//课程序列
						classGpjpMap.put(classCode, courseInfo);
					}
					classCode = MyTools.StrFiltr(vec.get(i)); //行政班代码
					courseInfo = new HashMap();
					
					presonNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+1))); //总人数
					type = MyTools.StrFiltr(vec.get(i+3)); //类型
					timeOrder = MyTools.StrFiltr(vec.get(i+2)); //时间序列
					tempSkjhmxbh = MyTools.StrFiltr(vec.get(i+4)); //授课计划明细编号
					tempSiteCode = MyTools.StrFiltr(vec.get(i+5)); //预设场地编号
					tempSiteName = MyTools.StrFiltr(vec.get(i+6)); //预设场地名称
					tempSkzc = MyTools.StrFiltr(vec.get(i+7)); //授课周次详情
					tempLjbh = MyTools.StrFiltr(vec.get(i+8)); //连节相关编号
				}else{
					//判断是否拼接课程
					//判断时间序列是否相同
					if(timeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
						tempSkjhmxbh += "｜"+MyTools.StrFiltr(vec.get(i+4));
						tempSiteCode += "｜"+MyTools.StrFiltr(vec.get(i+5));
						tempSiteName += "｜"+MyTools.StrFiltr(vec.get(i+6));
						tempSkzc += "｜"+MyTools.StrFiltr(vec.get(i+7));
					}else{
						if(i > 0){
							courseInfoVec = new Vector();
							courseInfoVec.add(presonNum);//班级人数
							courseInfoVec.add(type);//类型
							courseInfoVec.add(tempSkjhmxbh);//授课计划明细编号
							courseInfoVec.add(tempSiteCode);//预设场地编号
							courseInfoVec.add(tempSiteName);//预设场地名称
							courseInfoVec.add(tempSkzc);//授课周次
							courseInfoVec.add(tempLjbh);//连节相关编号
							courseInfo.put(timeOrder, courseInfoVec);//课程序列
						}
						
						presonNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+1))); //总人数
						type = MyTools.StrFiltr(vec.get(i+3)); //类型
						timeOrder = MyTools.StrFiltr(vec.get(i+2)); //时间序列
						tempSkjhmxbh = MyTools.StrFiltr(vec.get(i+4)); //授课计划明细编号
						tempSiteCode = MyTools.StrFiltr(vec.get(i+5)); //预设场地编号
						tempSiteName = MyTools.StrFiltr(vec.get(i+6)); //预设场地名称
						tempSkzc = MyTools.StrFiltr(vec.get(i+7)); //授课周次详情
						tempLjbh = MyTools.StrFiltr(vec.get(i+8)); //连节相关编号
					}
				}
				
				//判断是否连节课程
//				if(!tempSkjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) || MyTools.StringToInt(tempTimeOrder)!=(MyTools.StringToInt(MyTools.StrFiltr(vec.get(i+2)))+1)){
//					if(i > 0){
//						courseInfoVec.add(ljNum);//授课周次
//						courseInfo.put(timeOrder, courseInfoVec);//课程序列
//					}
//					
//					timeOrder = tempTimeOrder;
//					tempSkjhmxbh = MyTools.StrFiltr(vec.get(i+4));
//					timeOrder = MyTools.StrFiltr(vec.get(i+2));
//					ljNum = 1;
//					
//					courseInfoVec = new Vector();
//					courseInfoVec.add(MyTools.StrFiltr(vec.get(i+1)));//班级人数
//					courseInfoVec.add(MyTools.StrFiltr(vec.get(i+3)));//类型
//					courseInfoVec.add(tempSkjhmxbh);//授课计划明细编号
//					courseInfoVec.add(MyTools.StrFiltr(vec.get(i+5)));//预设场地编号
//					courseInfoVec.add(MyTools.StrFiltr(vec.get(i+6)));//预设场地名称
//					courseInfoVec.add(MyTools.StrFiltr(vec.get(i+7)));//授课周次
//					courseInfoVec.add(MyTools.StrFiltr(vec.get(i+8)));//连节相关编号
//				}else{
//					ljNum++;
//				}
			}
			
			courseInfoVec = new Vector();
			courseInfoVec.add(presonNum);//班级人数
			courseInfoVec.add(type);//类型
			courseInfoVec.add(tempSkjhmxbh);//授课计划明细编号
			courseInfoVec.add(tempSiteCode);//预设场地编号
			courseInfoVec.add(tempSiteName);//预设场地名称
			courseInfoVec.add(tempSkzc);//授课周次
			courseInfoVec.add(tempLjbh);//连节相关编号
			courseInfo.put(timeOrder, courseInfoVec);//课程序列
			//courseInfoVec.add(ljNum);
//			courseInfo.put(timeOrder, courseInfoVec);//课程序列
			classGpjpMap.put(classCode, courseInfo);
		}
		
		//如果是专业课排课，需要查询所有班级的公共课排课信息
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql = "select b.行政班代码,a.时间序列,case when a.班级排课状态='9' then '' else a.班级排课状态 end,授课计划明细编号,实际场地编号,实际场地名称,连节相关编号 from V_排课管理_公共课课程表明细表 a " +
//				"left join V_排课管理_课程表主表 b on b.课程表主表编号=a.课程表主表编号 " +
//				"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//				"and b.行政班代码 in (" + sqlCondition + ") and a.授课计划明细编号<>'' " +
//				"order by b.行政班代码,a.时间序列";
//			vec = db.GetContextVector(sql);
//			
//			String tempClass = "";
//			Vector tempCourseVec = null;
//			
//			for(int i=0; i<vec.size(); i+=7){
//				if(!tempClass.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
//					if(i > 0){
//						pubCourseVec.add(tempClass);
//						pubCourseVec.add(tempCourseVec);
//					}
//					
//					tempClass = MyTools.StrFiltr(vec.get(i));
//					tempCourseVec = new Vector();
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+1)));//时间序列
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+2)));//班级排课状态
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+3)));//授课计划明细编号
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+4)));//实际场地编号
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+5)));//实际场地名称
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+6)));//连节相关编号
//				}else{
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+1)));
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+2)));
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+3)));
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+4)));
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+5)));
//					tempCourseVec.add(MyTools.StrFiltr(vec.get(i+6)));
//				}
//			}
//			pubCourseVec.add(tempClass);
//			pubCourseVec.add(tempCourseVec);
//		}
		
		//初始化所有班级课程表信息（并完成所有班级固排禁排的课程安排）
		//遍历所有需要排课的班级
		for(int i=0; i<classVec.size(); i+=2){
			curClass = MyTools.StrFiltr(classVec.get(i));
			
			//获取当前班级的固排禁排信息
			if(classGpjpMap!=null && classGpjpMap.containsKey(curClass)){
				curGpjpMap = (Map)classGpjpMap.get(curClass);
			}else{
				curGpjpMap = new HashMap();
			}
			curClassMap = new HashMap();
			
			//如果是专业课排课，获取当前班级的公共课排课信息
//			if(this.getAuth().indexOf(majorTeacher) > -1){
//				if(pubCourseVec.indexOf(curClass) > -1){
//					curPubCourseVec = (Vector)pubCourseVec.get(pubCourseVec.indexOf(curClass)+1);
//				}else{
//					curPubCourseVec = new Vector();
//				}
//			}
			
			//遍历所有上课时间段
			for(int a=1; a<dayNum+1; a++){
				if(a < 10){
					tempDayNum = "0" + a;
				}else{
					tempDayNum = "" + a;
				}
				for(int b=1; b<lessonNum+1; b++){
					if(b < 10){
						tempLesNum = "0" + b;
					}else{
						tempLesNum = "" + b;
					}
					
					timeOrder = tempDayNum+tempLesNum;//当前安排课程的时间序列
					curOrderInfo = new Vector();
					flag = true;
					hbFlag = false;
					
					//判断当前时间序列是否有固排禁排信息，没有的话添加默认信息
					if(curGpjpMap!=null && curGpjpMap.containsKey(timeOrder)){
						curGpjpInfo = (Vector)curGpjpMap.get(timeOrder);
						tempState = MyTools.StrFiltr(curGpjpInfo.get(1));//课表状态
						skjhmxbhArray = MyTools.StrFiltr(curGpjpInfo.get(2)).split("｜");
						presonNum = MyTools.StringToInt(MyTools.StrFiltr(curGpjpInfo.get(0)));//班级人数
						cdyqArray = MyTools.StrFiltr(curGpjpInfo.get(3)).split("｜");
						skzcArray = MyTools.StrFiltr(curGpjpInfo.get(5)).split("｜");
						//ljNum = MyTools.StringToInt(MyTools.StrFiltr(curGpjpInfo.get(7)));
						
						//判断如果不是班级禁排
						if(!"3".equalsIgnoreCase(tempState)){
							for(int k=0; k<cdyqArray.length; k++){
								tempCdyqArray = cdyqArray[k].split("&");
								tempSkzcArray = skzcArray[k].split("&");
								
								//判断如果是合班课程不检查场地信息
								for(int y=0; y<skjhmxbhArray.length; y++){
									for(int z=0; z<hbInfoVec.size(); z++){
										if(MyTools.StrFiltr(hbInfoVec.get(z)).indexOf(skjhmxbhArray[y]) > -1){
											hbFlag = true;
											break;
										}
									}
									
									if(hbFlag == true){
										break;
									}
								}
								
								if(hbFlag == false){
									//检查当前课程设置的场地是否可用
									tempVec = this.checkSite(tempCdyqArray, timeOrder, ljNum, tempSkzcArray, xqzc, allSiteInfoVec, presonNum);
									flag = (Boolean)tempVec.get(0);
									
									if(flag == false){
										break;
									}
								}else{
									break;
								}
							}
						}else{
							flag = false;
						}
						
						//判断场地是否可用
						if(flag == true){
							tempSkjhmxbh = MyTools.StrFiltr(curGpjpInfo.get(2));//授课计划明细编号
							tempSkjhmxbhArray = tempSkjhmxbh.split("｜");
							for(int z=0; z<tempSkjhmxbhArray.length; z++){
//								if(sqlSkjhmxbh.indexOf("'"+tempSkjhmxbhArray[z]+"'") < 0){
//									sqlSkjhmxbh += "'"+tempSkjhmxbhArray[z]+"',";
//								}
								
								if(tempSkjhVec.indexOf(tempSkjhmxbhArray[z]) < 0){
									tempSkjhVec.add(tempSkjhmxbhArray[z]);
									tempSkjhVec.add(1);
								}else{
									tempSkjhVec.set(tempSkjhVec.indexOf(tempSkjhmxbhArray[z])+1, MyTools.StringToInt(MyTools.StrFiltr(tempSkjhVec.get(tempSkjhVec.indexOf(tempSkjhmxbhArray[z])+1)))+1);
								}
							}
							tempSiteCode = MyTools.StrFiltr(curGpjpInfo.get(3));//实际场地编号
							tempSiteName = MyTools.StrFiltr(curGpjpInfo.get(4));//实际场地名称
							tempLjbh = MyTools.StrFiltr(curGpjpInfo.get(6));//连节相关的时间序列
						}else{
							if(!"3".equalsIgnoreCase(tempState)){
								tempState = "";
							}
							tempSkjhmxbh = "";
							tempSiteCode = "";
							tempSiteName = "";
							tempLjbh = "";
						}
						
						//如果是专业课排课，检查当前时间序列如果有公共课，做拼接处理
//						if(this.getAuth().indexOf(majorTeacher) > -1){
//							if(curPubCourseVec.indexOf(timeOrder) > -1){
//								for(int k=0; k<curPubCourseVec.size(); k+=6){
//									if(timeOrder.equalsIgnoreCase(MyTools.StrFiltr(curPubCourseVec.get(k)))){
//										if(!"".equalsIgnoreCase(tempSkjhmxbh)){
//											tempSkjhmxbh = MyTools.StrFiltr(curPubCourseVec.get(k+2))+"｜"+tempSkjhmxbh;//授课计划明细编号
//											tempSiteCode = MyTools.StrFiltr(curPubCourseVec.get(k+3))+"｜"+tempSiteCode;//实际场地编号
//											tempSiteName = MyTools.StrFiltr(curPubCourseVec.get(k+4))+"｜"+tempSiteName;//实际场地名称
//											tempLjbh = MyTools.StrFiltr(curPubCourseVec.get(k+5))+"｜"+tempLjbh;//连节相关的时间序列
//										}else{
//											tempState = MyTools.StrFiltr(curPubCourseVec.get(k+1));
//											tempSkjhmxbh = MyTools.StrFiltr(curPubCourseVec.get(k+2));
//											tempSiteCode = MyTools.StrFiltr(curPubCourseVec.get(k+3));
//											tempSiteName = MyTools.StrFiltr(curPubCourseVec.get(k+4));
//											tempLjbh = MyTools.StrFiltr(curPubCourseVec.get(k+5));
//										}
//									}
//								}
//							}
//						}
						
//						for(int k=0; k<ljNum; k++){
//							//在课表中添加课程信息
//							curOrderInfo.add(tempState);//固排禁排状态
//							curOrderInfo.add(tempSkjhmxbh);//授课计划明细编号
//							curOrderInfo.add(tempSiteCode);//实际场地编号
//							curOrderInfo.add(tempSiteName);//实际场地名称
//							curOrderInfo.add(tempLjbh);//连节相关的时间序列
//							if(k > 0){
//								b++;
//								tempNum = MyTools.StringToInt(timeOrder.substring(2))+1;
//								timeOrder = timeOrder.substring(0, 2)+ (tempNum<10?"0"+tempNum:""+tempNum);
//							}
//							curClassMap.put(timeOrder, curOrderInfo);//key(时间序列):value(当前时间序列信息)
//						}
						
						curOrderInfo.add(tempState);//固排禁排状态
						curOrderInfo.add(tempSkjhmxbh);//授课计划明细编号
						curOrderInfo.add(tempSiteCode);//实际场地编号
						curOrderInfo.add(tempSiteName);//实际场地名称
						curOrderInfo.add(tempLjbh);//连节相关的时间序列
						curClassMap.put(timeOrder, curOrderInfo);//key(时间序列):value(当前时间序列信息)
					}else{
						//权限判断,如果是公共课排课直接添加默认空信息
						//如果是专业课排课,需要查询当前时间序列是否有公共课信息,没有的话添加空信息
//						if(this.getAuth().indexOf(pubTeacher) > -1){
//							curOrderInfo.add("");//状态 可排
//							curOrderInfo.add("");//授课计划明细编号
//							curOrderInfo.add("");//实际场地编号
//							curOrderInfo.add("");//实际场地名称
//							curOrderInfo.add("");//连节相关的时间序列
//						}else if(this.getAuth().indexOf(majorTeacher) > -1){
//							if(curPubCourseVec.indexOf(timeOrder) > -1){
//								for(int k=0; k<curPubCourseVec.size(); k+=6){
//									if(timeOrder.equalsIgnoreCase(MyTools.StrFiltr(curPubCourseVec.get(k)))){
//										curOrderInfo.add(curPubCourseVec.get(k+1));//状态 可排
//										curOrderInfo.add(curPubCourseVec.get(k+2));//授课计划明细编号
//										curOrderInfo.add(curPubCourseVec.get(k+3));//实际场地编号
//										curOrderInfo.add(curPubCourseVec.get(k+4));//实际场地名称
//										curOrderInfo.add(curPubCourseVec.get(k+5));//连节相关的时间序列
//									}
//								}
//							}else{
								curOrderInfo.add("");//状态 可排
								curOrderInfo.add("");//授课计划明细编号
								curOrderInfo.add("");//实际场地编号
								curOrderInfo.add("");//实际场地名称
								curOrderInfo.add("");//连节相关的时间序列
//							}
//						}
						curClassMap.put(timeOrder, curOrderInfo);//key(时间序列):value(当前时间序列信息)
					}
				}
			}
			kcbMap.put(curClass, curClassMap);//key(班级编号):value(当前班级课表)
		}
		
		sql = "update V_规则管理_授课计划明细表 set 实际已排节数=0,实际连次次数=0 " +
			"where 授课计划主表编号 in (select 授课计划主表编号 from V_规则管理_授课计划主表 where 状态='1' " +
			"and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		//判断公共课教师排课还是专业课教师排课
//		if(this.getAuth().indexOf(pubTeacher) > -1){
//			sql += " and 课程类型='01'";
//		}else if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and 课程类型='02' and 专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//		}
		sql += " and 行政班代码 in (" + xzbdm + ")";
		sql += ")";
		sqlVec.add(sql);
		
//		if(db.executeInsertOrUpdate(sql)){
//			//判断是否有固排课程
//			if(sqlSkjhmxbh.length() > 0){
//				sqlSkjhmxbh = sqlSkjhmxbh.substring(0, sqlSkjhmxbh.length()-1);
//				
//				//重置授课计划明细实际已排节数和实际连次次数
//				sql = "update V_规则管理_授课计划明细表 set 实际已排节数=固排已排节数,实际连次次数=固排连次次数 " +
//					"where 授课计划明细编号 in (" + sqlSkjhmxbh + ")";
//				
//				if(!db.executeInsertOrUpdate(sql)){
//					kcbMap.clear();
//				}
//			}
//		}else{
//			kcbMap.clear();
//		}
		
		//重置授课计划明细实际已排节数和实际连次次数
		for(int i=0; i<tempSkjhVec.size(); i+=2){
			sql = "update V_规则管理_授课计划明细表 set 实际已排节数=" + MyTools.fixSql(MyTools.StrFiltr(tempSkjhVec.get(i+1))) + "," +
				"实际连次次数=固排连次次数 " +
				"where 授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(tempSkjhVec.get(i))) + "'";
			sqlVec.add(sql);
		}
		
		if(!db.executeInsertOrUpdateTransaction(sqlVec)){
			kcbMap.clear();
		}
		
		resultVec.add(kcbMap);
		resultVec.add(allSiteInfoVec);
		
		return resultVec;
	}
	
	/**
	 * 获取课程表信息
	 * @date:2015-05-29
	 * @author:yeq
	 * @param tsgzFlag 用于判断是否验证特殊规则
	 * @param teaJpFlag 用于判断是否检查教师禁排
	 * @return Map 所有班级课程表信息
	 * @throws SQLException
	 */
	public Map loadKcb(String tsgzFlag, String teaJpFlag) throws SQLException{
		Vector vec = null;
		String sql = "";
		Map kcbMap = new HashMap();//所有课程表信息
		String classCode = "";
		Map curClassMap = new HashMap();//当前班级课程表
		Vector curOrderInfo = new Vector();//当前时间序列信息
		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		//==========================================================================================================
		
		
		Vector vec2 = null; 
		String xzbdm = "";
//		String zydm = "";
//		
//		//sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = ( select 系部代码 from dbo.V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')";
//		sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
//		vec2 = db.GetContextVector(sql);
//		
//		if(vec2.size() > 0){
//			for (int i = 0; i < vec2.size(); i++) {
//				zydm += "'" + vec2.get(i) + "',";
//			}
//		}
//		zydm = zydm.substring(0, zydm.length()-1);
		
		//sql = "select distinct(行政班代码) from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql = "select distinct 班级编号 from V_基础信息_班级信息表 where 系部代码='" + MyTools.fixSql(this.getPK_XB()) + "'";
		vec2 = db.GetContextVector(sql);
		
		if(vec2.size() > 0){
			for (int i = 0; i < vec2.size(); i++) {
				xzbdm += "'" + vec2.get(i) + "',";
			}
			
			xzbdm = xzbdm.substring(0, xzbdm.length()-1);
		}
		
		
		//===========================================================================================================
		
		
		//获取当前学期已经安排完的课程表信息
		sql = "select distinct 行政班代码,班级排课状态,时间序列,授课计划明细编号,实际场地编号,实际场地名称,连节相关编号 from V_排课管理_课程表明细详情表 " +
			"where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and 专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//		}
		sql += " and 行政班代码 in (" + xzbdm + ")";
		sql += " order by 行政班代码,时间序列";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//遍历已完成的课表信息，获取课程表集合
			for(int i=0; i<vec.size(); i+=7){
				//判断是否另一个班级
				if(!classCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						kcbMap.put(classCode, curClassMap);
					}
					classCode = MyTools.StrFiltr(vec.get(i));
					curClassMap = new HashMap();
				}
				
				curOrderInfo = new Vector();
				curOrderInfo.add(MyTools.StrFiltr(vec.get(i+1))); //班级排课状态
				curOrderInfo.add(MyTools.StrFiltr(vec.get(i+3))); //授课计划明细编号
				curOrderInfo.add(MyTools.StrFiltr(vec.get(i+4))); //实际场地编号
				curOrderInfo.add(MyTools.StrFiltr(vec.get(i+5))); //实际场地名称
				curOrderInfo.add(MyTools.StrFiltr(vec.get(i+6))); //连节相关编号
				curClassMap.put(MyTools.StrFiltr(vec.get(i+2)), curOrderInfo);//key(时间序列):value(当前时间序列的内容信息)
			}
			kcbMap.put(classCode, curClassMap);//key(班级编号):value(当前班级的时间序列信息)
		}else{
			autoCourseArrange("1", tsgzFlag, teaJpFlag);//全新排课
		}
		
		return kcbMap;
	}
	
	/**
	 * 获取所有班级可排位置集合
	 * @date:2015-06-02
	 * @author:yeq
	 * @param allClassKcbMap 所有班级课表
	 * @param dayNum 每周天数
	 * @param lessonNum 每天节数
	 * @param monNum 上午节数
	 * @param noonNum 中午节数
	 * @param afternoonNum 下午节数
	 * @return Map 所有班级可排位置集合
	 * @throws SQLException
	 */
	public Map loadKpwz(Map allClassKcbMap, int dayNum, int lessonNum, int monNum, int noonNum, int afternoonNum) throws SQLException{
		Map kpwzMap = new HashMap();
		Map tempMap = new HashMap();
		Vector orderInfo = new Vector();
		Vector orderVec = null;
		String classCode = "";
		String timeOrder = ""; //课程表时间序列
		String tempDayNum = "";
		String tempLesNum = "";
		
		Set keysSet = allClassKcbMap.keySet();
		Iterator iterator = keysSet.iterator();
		while(iterator.hasNext()) {
			classCode = (String)iterator.next();//key
			tempMap = (HashMap)allClassKcbMap.get(classCode);//value
			orderVec = new Vector();
			
			for(int a=1; a<dayNum+1; a++){
				tempDayNum = a<10?"0"+a:""+a;
				
				for(int b=1; b<lessonNum+1; b++){
					//判断时间序列是不是中午或晚上（中午和晚上不自动排课）
					if(b>monNum&&b<=monNum+noonNum || b>monNum+noonNum+afternoonNum){
						continue;
					}
					
					tempLesNum = b<10?"0"+b:""+b;
					timeOrder = tempDayNum+tempLesNum;//当前安排课程的时间序列
					
					if(tempMap!=null && tempMap.containsKey(timeOrder)){
						orderInfo = (Vector)tempMap.get(timeOrder);
						
						//判断是否可排位置
						if("".equalsIgnoreCase(MyTools.StrFiltr(orderInfo.get(0)))){
							orderVec.add(timeOrder);
						}
					}
				}
			}
			kpwzMap.put(classCode, orderVec);
		}
		
		return kpwzMap;
	}
	
	/**
	 * 获取当前专业所有班级的未排课程
	 * 根据教师及课程限制条件排序
	 * @date:2015-05-29
	 * @author:yeq
	 * @param xnxqbm 学年学期编码
	 * @return Map 当前专业所有班级的未排课程信息
	 * @throws SQLException
	 */
	public Vector loadWpkc() throws SQLException{
		Vector vec = null;
		String sql = "";
		Map wpkcMap = new HashMap();//所有课程表信息
		Vector wpkcVec = new Vector();
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		//获取当前专业所有班级的未安排课程
//		sql = "select distinct t.工号,a.授课计划明细编号,a.课程代码,b.行政班代码,c.总人数,a.节数,a.实际已排节数,a.连节,a.连次,a.实际连次次数,a.授课教师编号,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情,a.课程名称,c.行政班名称 " +
//			"from V_教职工基本数据子类 t " +
//			"inner join V_规则管理_授课计划明细表 a on '@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@'+t.工号+'@%' " +
//			"left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
//			"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
//			"where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//			
//			//判断权限，获取相应的未排课程
//			if(this.getAuth().indexOf(pubTeacher) > -1){
//				sql += " and a.课程类型='01'";
//			}else if(this.getAuth().indexOf(majorTeacher) > -1){
//				sql += " and a.课程类型='02'" +
//					" and b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//			}
//			sql += " and (a.节数-a.实际已排节数)>0 order by t.工号,a.连节 desc";
//		vec = db.GetContextVector(sql);
		
		//=====================================================================================================
		
		Vector vec2 = null; 
//		String zydm = ""; //专业代码
		String xzbdm = ""; //行政班代码
		
		//sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = ( select 系部代码 from dbo.V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')";
//		sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
//		vec2 = db.GetContextVector(sql);
//		
//		if(vec2.size() > 0){
//			for (int i = 0; i < vec2.size(); i++) {
//				zydm += "'" + vec2.get(i) + "',";
//			}
//			zydm = zydm.substring(0, zydm.length()-1);
//		}
		
		//sql = "select distinct(行政班代码) from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql = "select distinct 班级编号 from V_基础信息_班级信息表 where 系部代码='" + MyTools.fixSql(this.getPK_XB()) + "'";
		vec2 = db.GetContextVector(sql);
		
		if(vec2.size() > 0){
			for (int i = 0; i < vec2.size(); i++) {
				xzbdm += "'" + vec2.get(i) + "',";
			}
			xzbdm = xzbdm.substring(0, xzbdm.length()-1);
		}
		
		//获取当前专业所有班级的未安排课程
		sql = "select distinct t.工号,a.授课计划明细编号,a.课程代码,b.行政班代码,c.总人数,a.节数,a.实际已排节数,a.连节,a.连次,a.实际连次次数,a.授课教师编号,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情,a.课程名称,c.班级名称 " +
			  "from V_教职工基本数据子类 t " +
			  "inner join V_规则管理_授课计划明细表 a on '@'+replace(replace(a.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+t.工号+'@%' " +
			  "left join V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号  " +
			  //"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
			  "left join V_基础信息_班级信息表 c on c.班级编号=b.行政班代码 " +
			  "where a.状态='1' and b.状态='1' and b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			  "and c.系部代码='" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql += " and (a.节数-a.实际已排节数)>0 order by t.工号,a.连节 desc";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			Vector courseInfo = new Vector(); //课程信息
			Map tempMap = new HashMap(); //所有未排课程
			String tempTeaCode = ""; //授课教师编号
			
			//遍历未安排课程,获取为安排课程集合
			for(int i=0; i<vec.size(); i+=17){
				//判断是否另一个教师
				if(!tempTeaCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						wpkcMap.put(tempTeaCode, tempMap);
					}
					tempTeaCode = MyTools.StrFiltr(vec.get(i)); //工号
					tempMap = new HashMap();
				}
				
				courseInfo = new Vector();
				courseInfo.add(MyTools.StrFiltr(vec.get(i+2))); //课程代码
				courseInfo.add(MyTools.StrFiltr(vec.get(i+3))); //行政班代码
				courseInfo.add(MyTools.StrFiltr(vec.get(i+4))); //总人数
				courseInfo.add(MyTools.StrFiltr(vec.get(i+5))); //节数
				courseInfo.add(MyTools.StrFiltr(vec.get(i+6))); //实际已排节数
				courseInfo.add(MyTools.StrFiltr(vec.get(i+7))); //连节
				courseInfo.add(MyTools.StrFiltr(vec.get(i+8))); //连次
				courseInfo.add(MyTools.StrFiltr(vec.get(i+9))); //实际连次次数
				courseInfo.add(MyTools.StrFiltr(vec.get(i+10)));//授课教师编号
				courseInfo.add(MyTools.StrFiltr(vec.get(i+11)));//场地要求
				courseInfo.add(MyTools.StrFiltr(vec.get(i+12)));//场地名称
				courseInfo.add(MyTools.StrFiltr(vec.get(i+13)));//授课周次
				courseInfo.add(MyTools.StrFiltr(vec.get(i+14)));//授课周次详情
				courseInfo.add(MyTools.StrFiltr(vec.get(i+15)));//课程名称
				courseInfo.add(MyTools.StrFiltr(vec.get(i+16)));//行政班名称
				tempMap.put(MyTools.StrFiltr(vec.get(i+1)), courseInfo); //key(授课计划明细编号):value(课程信息)
			}
			wpkcMap.put(tempTeaCode, tempMap);//key(授课教师编号):value(所有课程信息)
			wpkcVec = this.sortWPKC(wpkcMap);//未排课程排序
		}
		return wpkcVec;
	}
	
	/**
	 * 未排课程排序
	 * 根据禁排数量排序
	 * @date:2015-05-29
	 * @author:yeq
	 * @param wpkcMap 未排课程的原始集合
	 * @return Vector 排完序的未排课程集合
	 * @throws SQLException
	 */
	public Vector sortWPKC(Map wpkcMap) throws SQLException{
		Vector wpkcVec = new Vector();
		Vector teaOrderVec = null; //教师禁排序列
		Map ypMap = new HashMap(); //优排课程集合
		String sql = "";
		String tempTeaCode = ""; //授课教师编号
//		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		Vector vec2 = null; //专业代码
		String xzbdm = "";
//		String zydm = "";
//		
//		//sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = ( select 系部代码 from dbo.V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')";
//		sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
//		vec2 = db.GetContextVector(sql);
//		
//		if(vec2.size() > 0){
//			for (int i = 0; i < vec2.size(); i++) {
//				zydm += "'" + vec2.get(i) + "',";
//			}
//		}
//		zydm = zydm.substring(0, zydm.length()-1);
		
		//sql = "select distinct(行政班代码) from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql = "select distinct 班级编号 from V_基础信息_班级信息表 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		vec2 = db.GetContextVector(sql);
		
		if(vec2.size() > 0){
			for (int i = 0; i < vec2.size(); i++) {
				xzbdm += "'" + vec2.get(i) + "',";
			}
			
			xzbdm = xzbdm.substring(0, xzbdm.length()-1);
		}
		
		
		//=======================================================================================================
		
		//获取所有优排课程
		sql = "select distinct b.授课教师编号,a.授课计划明细编号,c.行政班代码,b.连节 from V_规则管理_固排禁排表 a " +
			"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号=b.授课计划明细编号 " +
			"left join V_规则管理_授课计划主表 c on b.授课计划主表编号=c.授课计划主表编号 " +
			"where a.状态='1' and b.状态='1' and a.类型='1' and a.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//		if(this.getAuth().indexOf(majorTeacher) > -1){
			//sql += " and c.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
			sql += " and c.行政班代码 in (" + xzbdm + ")";
//		}
		sql += " order by b.授课教师编号,c.行政班代码,b.连节 desc";
		teaOrderVec = db.GetContextVector(sql);
		
		//判断是否有优排课程
		if(teaOrderVec!=null && teaOrderVec.size()>0){
			Vector tempVec = new Vector();
			//将结果集转换成MAP集合
			for(int i=0; i<teaOrderVec.size(); i+=4){
				//判断是否另一个教师
				if(!tempTeaCode.equalsIgnoreCase(MyTools.StrFiltr(teaOrderVec.get(i)))){
					if(i > 0){
						ypMap.put(tempTeaCode, tempVec);
					}
					tempTeaCode = MyTools.StrFiltr(teaOrderVec.get(i));
					tempVec = new Vector();
				}
				
				tempVec.add(MyTools.StrFiltr(teaOrderVec.get(i+1)));
			}
			ypMap.put(tempTeaCode, tempVec);
		}
		
		//获取根据教师禁排数量排序的集合
//		sql = "select 工号,(select count(*) from V_规则管理_固排禁排表 t1 " +
//			"left join V_规则管理_授课计划明细表 t2 on t1.授课计划明细编号=t2.授课计划明细编号 " +
//			"where t1.状态='1' and t2.状态='1' and t1.禁排类型 = 'js' and t1.类型='3' and t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//			"and '@'+replace(('@'+replace(t2.授课教师编号,'+','@+@')+'@'),'&','@+@')+'@' like '%@'+工号+'@%') as 禁排数量 from V_教职工基本数据子类  where 工号 in (" +
//			"select t3.工号 from V_教职工基本数据子类 t3 " +
//			"left join V_规则管理_授课计划明细表 t4 on '@'+replace(('@'+replace(t4.授课教师编号,'+','@+@')+'@'),'&','@+@')+'@' like '%@'+t3.工号+'@%' " +
//			"left join V_规则管理_授课计划主表 t5 on t5.授课计划主表编号=t4.授课计划主表编号 where t4.状态='1' and t5.状态='1' ";
//		sql = " select 工号,(select count(*) from V_规则管理_固排禁排表 t1 " +
//			  " where t1.状态='1' and t1.禁排类型 = 'js' and t1.类型='3' and t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//			  " and t1.行政班代码 = 工号) as 禁排数量 from V_教职工基本数据子类  where 工号 in ( " +
//			  " select t3.工号 from V_教职工基本数据子类 t3 " +
//			  " left join V_规则管理_授课计划明细表 t4 on '@'+replace(replace(t4.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+t3.工号+'@%' " +
//			  " left join V_规则管理_授课计划主表 t5 on t5.授课计划主表编号=t4.授课计划主表编号 where t4.状态='1' and t5.状态='1' ";
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and t5.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//		}
//		sql += " and t5.专业代码 in (" + zydm + ") and t5.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//		sql += " and t5.行政班代码 in (" + xzbdm + ") and t5.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//		sql += ") order by 禁排数量 desc";
		
		//获取根据教师跨系部数量和教师禁排数量排序的集合
		sql = " select a.工号,b.系部代码,a.禁排数量 from ( " + 
			  " select 工号,(select count(*) from V_规则管理_固排禁排表 t1  " + 
			  "	where t1.状态='1' and t1.禁排类型 = 'js' and t1.类型='3' and t1.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
			  "	and t1.行政班代码 = 工号) as 禁排数量 from V_教职工基本数据子类  where 工号 in (  " + 
			  "	select t3.工号 from V_教职工基本数据子类 t3  " + 
			  "	left join V_规则管理_授课计划明细表 t4 on '@'+replace(replace(t4.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+t3.工号+'@%'  " + 
			  "	left join V_规则管理_授课计划主表 t5 on t5.授课计划主表编号=t4.授课计划主表编号 where t4.状态='1' and t5.状态='1' " + 
			  " and t5.行政班代码 in (" + xzbdm + ") and t5.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')  " + 
			  " ) a left join  " + 
			  " ( " + 
			  " select a.工号, COUNT(a.系部代码) as 系部代码 from ( " + 
			  " select distinct a.工号,d.系部代码 from dbo.V_教职工基本数据子类 a " + 
			  " left join V_规则管理_授课计划明细表 b " + 
			  " on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%'  " + 
			  " left join V_规则管理_授课计划主表 c " + 
			  " on b.授课计划主表编号 = c.授课计划主表编号 " + 
			  " left join dbo.V_基础信息_班级信息表 d " + 
			  " on d.班级编号 = c.行政班代码 " + 
			  " where c.学年学期编码 ='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " + 
			  " ) a group by a.工号 " + 
			  " ) b on b.工号=a.工号 order by b.系部代码 desc,a.禁排数量 desc ";
			
		teaOrderVec = db.GetContextVector(sql);
		
		
		Vector courseVec = new Vector();
		Map tempMap = new HashMap();
		Vector tempVec = new Vector();
		String skjhmxbh = "";//授课计划明细编号
		Vector ypkcInfoVec = new Vector();
		
		//遍历授课教师序列，对未排课程进行排序
		for(int i=0; i<teaOrderVec.size(); i+=3){
			tempTeaCode = MyTools.StrFiltr(teaOrderVec.get(i)); //工号
			courseVec = new Vector();
			
			//判断当前教师是否有未安排课程
			if(wpkcMap!=null && wpkcMap.containsKey(tempTeaCode)){
				tempMap = (Map)wpkcMap.get(tempTeaCode); //授课教师所有未排课程
				
				//检查是否有设置了优先的课程,有的话先排设置了优先的课程
				if(ypMap!=null && ypMap.containsKey(tempTeaCode)){
					ypkcInfoVec = (Vector) ypMap.get(tempTeaCode);
					
					for(int j=0; j<ypkcInfoVec.size(); j++){
						skjhmxbh = MyTools.StrFiltr(ypkcInfoVec.get(i));
						if(tempMap!=null && tempMap.containsKey(skjhmxbh)){
							courseVec.add(MyTools.StrFiltr(ypkcInfoVec.get(i)));
							tempVec = (Vector)tempMap.get(skjhmxbh);
							
							for(int k=0; k<tempVec.size(); k+=15){
								courseVec.add(MyTools.StrFiltr(tempVec.get(k))); //课程代码
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+1))); //行政班代码
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+2))); //总人数
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+3))); //节数
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+4))); //实际已排节数
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+5))); //连节
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+6))); //连次
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+7))); //实际连次次数
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+8))); //授课教师编号
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+9))); //场地要求
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+10))); //场地名称
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+11))); //授课周次
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+12))); //授课周次详情
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+13))); //课程名称
								courseVec.add(MyTools.StrFiltr(tempVec.get(k+14))); //行政班名称
							}
							tempMap.remove(skjhmxbh);//移除优先排序的课程
						}
					}
				}
				
				//剩余课程依次排序
				Set set = tempMap.keySet();
				for (Object key : set) {
					//System.out.println("键:"+key+"  值:"+tempMap.get(key));
					courseVec.add(key);
					tempVec = (Vector)tempMap.get(MyTools.StrFiltr(key));
					for(int j=0; j<tempVec.size(); j+=15){
						courseVec.add(MyTools.StrFiltr(tempVec.get(j))); //课程代码
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+1))); //行政班代码
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+2))); //总人数
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+3))); //节数
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+4))); //实际已排节数
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+5))); //连节
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+6))); //连次
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+7))); //实际连次次数
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+8))); //授课教师编号
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+9))); //场地要求
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+10))); //场地名称
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+11))); //授课周次
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+12))); //授课周次详情
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+13))); //课程名称
						courseVec.add(MyTools.StrFiltr(tempVec.get(j+14))); //行政班名称
					}
				}
			}
			wpkcVec.add(tempTeaCode);
			wpkcVec.add(courseVec);
		}
		return wpkcVec;
	}
	
	/**
	 * 获取所有除班级禁排外的禁排信息
	 * @date:2015-06-01
	 * @author:yeq
	 * @return Map 教师禁排数据
	 * @throws SQLException
	 */
	public Map loadTeaJp() throws SQLException{
		Vector vec = null;
		String sql = "";
		String teaCode = "";
		String courseCode = "";
		String classCode = "";
		Map teaJpMap = new HashMap();
		Map courseMap = new HashMap();
		Map classMap = new HashMap();
		Vector orderInfo = new Vector();
//		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		Vector vec2 = null; 
		String xzbdm = "";
//		String zydm = "";
//		
//		//sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = ( select 系部代码 from dbo.V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')";
//		sql = " select distinct 专业代码 from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
//		vec2 = db.GetContextVector(sql);
//		
//		if(vec2.size() > 0){
//			for (int i = 0; i < vec2.size(); i++) {
//				zydm += "'" + vec2.get(i) + "',";
//			}
//		}
//		zydm = zydm.substring(0, zydm.length()-1);
		
		//sql = "select distinct(行政班代码) from dbo.V_学校班级_数据子类 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		sql = "select distinct 班级编号 from V_基础信息_班级信息表 where 系部代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'";
		vec2 = db.GetContextVector(sql);
		
		if(vec2.size() > 0){
			for (int i = 0; i < vec2.size(); i++) {
				xzbdm += "'" + vec2.get(i) + "',";
			}
			
			xzbdm = xzbdm.substring(0, xzbdm.length()-1);
		}
		
		
		//===========================================================================================================
		//2017-7-1 添加一个禁排类型条件 为 js(教师)
		
		sql = "select a.工号,b.课程代码,c.行政班代码,c.时间序列 from V_教职工基本数据子类 a " +
			"left join V_规则管理_授课计划明细表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' " +
			"left join V_规则管理_固排禁排表 c on c.授课计划明细编号=b.授课计划明细编号 " +
			"where b.状态='1' and c.状态='1' and c.类型='3' and c.禁排类型 = 'js' and a.工号 in (select t1.工号 from V_教职工基本数据子类 t1 " +
			"left join V_规则管理_授课计划明细表 t2 on '@'+replace(replace(t2.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+t1.工号+'@%' " +
			"left join V_规则管理_授课计划主表 t3 on t3.授课计划主表编号=t2.授课计划主表编号 where t2.状态='1'";
//		if(this.getAuth().indexOf(majorTeacher) > -1){
//			sql += " and t3.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//		}
		sql += " and t3.行政班代码 in (" + xzbdm + ")";
		sql += ") order by a.工号,c.行政班代码";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			//遍历所有除班级禁排外的禁排信息
			for(int i=0; i<vec.size(); i+=4){
				//判断是否另一个老师
				if(!teaCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						teaJpMap.put(teaCode, courseMap);
					}
					teaCode = MyTools.StrFiltr(vec.get(i));
					courseMap = new HashMap();
				}
				
				//判断是否另一门课程
				if(!courseCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
					if(i > 0){
						courseMap.put(courseCode, classMap);
					}
					courseCode = MyTools.StrFiltr(vec.get(i+1));
					classMap = new HashMap();
				}
				
				//判断是否另一个班级
				if(!classCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
					if(i > 0){
						classMap.put(classCode, orderInfo);
					}
					classCode = MyTools.StrFiltr(vec.get(i+2));
					orderInfo = new Vector();
				}
				
				orderInfo.add(MyTools.StrFiltr(vec.get(i+3))); //时间序列
			}
			classMap.put(classCode, orderInfo);
			courseMap.put(courseCode, classMap);
			teaJpMap.put(teaCode, courseMap);
		}
		
		return teaJpMap;
	}
	
	/**
	 * 获取教师的禁排信息
	 * @date:2015-06-01
	 * @author:yeq
	 * @return Map 教师禁排数据
	 * @throws SQLException
	 */
	public Map loadTeaJp2() throws SQLException{
		Vector vec = null;
		String sql = "";
		String teaCode = "";
		Map teaJpMap = new HashMap();
		Vector sjxlVec = new Vector();
		
		//2017-7-1 添加一个禁排类型条件 为 js(教师)
		sql = "select 行政班代码,时间序列 from V_规则管理_固排禁排表 " +
			  "where 状态='1' and 类型='3' and 禁排类型 = 'js' and 学年学期编码 like '" +  MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' order by 行政班代码 ";
		vec = db.GetContextVector(sql);
		
		
		if(vec!=null && vec.size()>0){
			//遍历所有除班级禁排外的禁排信息
			for(int i=0; i<vec.size(); i+=2){
				//判断是否另一个老师
				if(!teaCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						teaJpMap.put(teaCode, sjxlVec);
					}
					teaCode = MyTools.StrFiltr(vec.get(i));
					sjxlVec = new Vector();
				}
				sjxlVec.add(MyTools.StrFiltr(vec.get(i+1))); //时间序列
			}
			teaJpMap.put(teaCode, sjxlVec);
		}
		
		
		return teaJpMap;
	}
	
	/**
	 * 获取授课教师已排课时间序列
	 * @date:2015-06-19
	 * @author:yeq
	 * @param xqzc 学期周次
	 * @param pkType 排课类型
	 * @return Map 教师禁排数据
	 * @throws SQLException
	 */
	public Map loadTeaUsedOrder(String xqzc, String pkType) throws SQLException{
		Map usedOrderMap = new HashMap();
		Vector vec = new Vector();
		String tempTeaCode = "";
		String tempTimeOrder = "";
		String sql = "";
		String teaArray[] = new String[0];
		String tempTeaArray[] = new String[0];
		String skzcArray[] = new String[0];
		String tempSkzcArray[] = new String[0];
		String tea[] = new String[0];
		Vector tempSkzcVec = new Vector();
		Vector skzcVec = null;
		Vector tempVec = new Vector();
//		String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		

		sql = "select distinct * from (select a.工号,b.时间序列,b.授课教师编号,b.授课周次详情  from V_教职工基本数据子类 a " +
				"inner join V_排课管理_课程表明细详情表 b on '@'+replace(replace(replace(replace(b.授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@'+a.工号+'@%' " +
				"where b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and b.授课计划明细编号<>''";
		//判断是否为专业课全新排课
//		if(this.getAuth().indexOf(majorTeacher)>-1 && "1".equalsIgnoreCase(pkType)){
//			sql += " and (b.专业代码='" + MyTools.fixSql(this.getPK_ZYDM()) + "' and b.课程类型='01') or b.专业代码<>'" + MyTools.fixSql(this.getPK_ZYDM()) + "'";
//		}

		sql += " union all " +
			"select a.工号,c.时间序列,b.授课教师编号,b.授课周次详情 from V_教职工基本数据子类 a " +
			"inner join V_规则管理_授课计划明细表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' " +
			"inner join V_规则管理_固排禁排表 c on c.授课计划明细编号=b.授课计划明细编号 " +
			"where b.状态='1' and c.状态='1' and c.类型='2' and c.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%') t " +
			"order by 工号,时间序列";
		vec = db.GetContextVector(sql);
		
		if(vec.size() > 0){
			for(int i=0; i<vec.size(); i+=4){
				if(i == 0){
					tempTeaCode = MyTools.StrFiltr(vec.get(i));
					tempTimeOrder = MyTools.StrFiltr(vec.get(i+1));
					tempVec.add(tempTimeOrder);//时间序列
					skzcVec = new Vector();
				}else{
					//判断是否同一教师
					if(!tempTeaCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
						tempVec.add(skzcVec);//授课周次
						usedOrderMap.put(tempTeaCode, tempVec);
						tempTeaCode = MyTools.StrFiltr(vec.get(i));
						tempVec = new Vector();
						tempTimeOrder = MyTools.StrFiltr(vec.get(i+1));
						tempVec.add(tempTimeOrder);//时间序列
						skzcVec = new Vector();
					}else{
						//判断是否同一个时间序列
						if(!tempTimeOrder.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
							tempVec.add(skzcVec);//授课周次
							tempTimeOrder = MyTools.StrFiltr(vec.get(i+1));
							tempVec.add(tempTimeOrder);//时间序列
							skzcVec = new Vector();
						}
					}
				}
				
				//获取授课周次
				teaArray = MyTools.StrFiltr(vec.get(i+2)).split("｜");
				skzcArray = MyTools.StrFiltr(vec.get(i+3)).split("｜");
				
				for(int b=0; b<teaArray.length; b++){
					tempTeaArray = teaArray[b].split("&");
					tempSkzcArray = skzcArray[b].split("&");
					
					for(int j=0; j<tempTeaArray.length; j++){
						tea = tempTeaArray[j].split("\\+");
						
						for(int k=0; k<tea.length; k++){
							//判断是否当前教师,获取当前段授课周次
							if(tempTeaCode.equalsIgnoreCase(tea[k])){
								tempSkzcVec = this.formatSkzc(tempSkzcArray[j], xqzc);
								
								for(int a=0; a<tempSkzcVec.size(); a++){
									skzcVec.add(tempSkzcVec.get(a));
								}
							}
						}
					}
				}
			}
			
			tempVec.add(skzcVec);//授课周次
			usedOrderMap.put(tempTeaCode, tempVec);//添加最后一个
		}
		
		return usedOrderMap;
	}
	
	/**
	 * 获取所有场地相关信息
	 * @date:2015-07-01
	 * @author:yeq
	 * @pkType 排课方式
	 * @sqlCondition 教室已用时间序列查询条件
	 * @param xqzc 学期周次
	 * @param classFixedClassroom 班级教室固定信息
	 * @return Vector 场地信息
	 * @throws SQLException
	 */
	public Vector loadSiteInfo(String pkType, String sqlCondition, String xqzc, Vector classFixedClassroom) throws SQLException{
		Vector resultVec = new Vector();
		Vector vec = null;
		String tempSiteCode = "";
		Vector usedOrderVec = new Vector();
		String curCdbhArray[] = new String[0];
		String tempCdbhArray[] = new String[0];
		String cdbhArray[] = new String[0];
		String curSkzcArray[] = new String[0];
		String tempSkzcArray[] = new String[0];
		Vector tempSkzcVec = new Vector();
		Vector skzeVec = null;
		String sql = "";
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		
		//===========================================================================================================
		//获取班级固定教室编号
//		String gdjsbh = ""; //固定教室编号
//		if(classFixedClassroom.size() > 0){
//			for (int i = 0; i < classFixedClassroom.size(); i+=4) {
//				gdjsbh += "'" + classFixedClassroom.get(i+2) + "',";
//				
//				
//			}
//			
//			gdjsbh = gdjsbh.substring(0, gdjsbh.length()-1);
//		}
		
		Vector vec2 = null;
		sql = "select COUNT(*) as 学年总周次 from dbo.V_规则管理_学期周次表 where 学年学期编码 = '" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec2 = db.GetContextVector(sql);
	
		//===========================================================================================================
		
		//判断是全新排课还是继续排课（2017-7-1）
		if("1".equalsIgnoreCase(pkType)){
			//根据权限查询不同的场地信息
//			if(this.getAuth().indexOf(pubTeacher) > -1){//公共课排课
				//所有课程固排的场地占用信息
//				sql = "select distinct * from (select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,isnull(b.时间序列,'') as 时间序列,b.实际场地编号,授课周次详情 " +
//					"from V_教室数据类 a left join V_排课管理_课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%' " +
//					"and b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
//					"where a.是否可用='1' " +
//					"union all " +
//					"select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,c.时间序列,b.场地要求,isnull(b.授课周次详情, '') as 授课周次 from V_教室数据类 a " +
//					"inner join V_规则管理_授课计划明细表 b on b.场地要求 like '%'+a.教室编号+'%' " +
//					"inner join V_规则管理_固排禁排表 c on c.授课计划明细编号=b.授课计划明细编号 " +
//					"where a.是否可用='1' and b.课程类型<>'01' and c.状态='1' and c.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%') as t " +
//					"order by t.实际容量,t.教室编号,t.时间序列";
				
				//查询所有教室信息
//				sql = "select distinct 教室编号,教室名称,教室类型代码,实际容量,'','','' from V_教室数据类 where 是否可用='1' order by 实际容量,教室编号";
//			}
//			if(this.getAuth().indexOf(majorTeacher) > -1){//专业课排课
				sql = "select distinct * from (";
				
				//场地已用信息(不包含当前排课的专业班级)
				sql += "select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,isnull(b.时间序列,'') as 已用时间序列,b.实际场地编号,授课周次详情 " +
					"from V_教室数据类 a left join V_排课管理_课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%' " +
					"and b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
					//"and not EXISTS (select 1 from V_排课管理_课程表明细详情表 c where c.行政班代码=b.行政班代码 and b.行政班代码 in (" + sqlCondition + ")) " +
					"and b.行政班代码  not in (" + sqlCondition + ") " +
					"where a.是否可用='1' and a.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'" +
					"union all ";
				//本专业班级的公共课排课信息
//				sql += "select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,isnull(b.时间序列,'') as 已用时间序列,b.实际场地编号,授课周次详情 " +
//					"from V_教室数据类 a " +
//					"left join V_排课管理_公共课课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%' " +
//					"and b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
					//"and EXISTS (select 1 from V_排课管理_公共课课程表明细详情表 c where c.行政班代码=b.行政班代码 and b.行政班代码 in (" + sqlCondition + ")) " +
//					"and b.行政班代码  in (" + sqlCondition + ") " +
//					"where a.是否可用='1' " +
//					"union all ";
				//固排的场地占用信息（不包含当前排课的专业班级）
				sql += "select a1.教室编号,a1.教室名称,a1.教室类型代码,a1.实际容量,c1.时间序列,b1.场地要求,isnull(b1.授课周次详情, '') as 授课周次 from V_教室数据类 a1 " +
					"inner join V_规则管理_授课计划明细表 b1 on b1.场地要求 like '%'+a1.教室编号+'%' " +
					"inner join V_规则管理_固排禁排表 c1 on c1.授课计划明细编号=b1.授课计划明细编号 " +
					//"and not EXISTS (select 1 from V_排课管理_课程表明细详情表 c2 where c2.行政班代码=c1.行政班代码 and c2.行政班代码 in (" + sqlCondition + ")) " +
					"and c1.行政班代码  not in (" + sqlCondition + ") " +
//					"where a1.是否可用='1' and b1.状态='1' and c1.状态='1' and b1.课程类型='02' and c1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%'";
					"where a1.是否可用='1' and b1.状态='1' and c1.状态='1' and c1.类型 ='2' and c1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and a1.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'" +
					"union all ";
				//禁排的场地占用信息（不包含当前排课的专业班级）
//				sql += "select a1.教室编号,a1.教室名称,a1.教室类型代码,a1.实际容量,c1.时间序列,c1.预设场地编号,isnull(b1.授课周次详情, '') as 授课周次 from V_教室数据类 a1 " +
//					"inner join V_规则管理_授课计划明细表 b1 on b1.场地要求 like '%'+a1.教室编号+'%' " +
//					"inner join V_规则管理_固排禁排表 c1 on c1.授课计划明细编号=b1.授课计划明细编号 " +
//					"and c1.行政班代码  not in (" + sqlCondition + ") " +
//					"where a1.是否可用='1' and b1.状态='1' and c1.状态='1' and c1.类型 ='3' and c1.禁排类型 = 'cd' and c1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and a1.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "' ) as t ";
				
				sql += "select a1.教室编号,a1.教室名称,a1.教室类型代码,a1.实际容量,c1.时间序列,c1.行政班代码, '1-" + MyTools.fixSql(MyTools.StrFiltr(vec2.get(0))) + "' as 授课周次 from V_教室数据类 a1 " +
						" inner join V_规则管理_固排禁排表 c1 on c1.行政班代码=a1.教室编号  " +
						" and c1.行政班代码  not in (" + sqlCondition + ") " +
						" where a1.是否可用='1' and c1.状态='1' and c1.类型 ='3' and c1.禁排类型 = 'cd' and c1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and a1.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "' ) as t";
				
//				if(!"".equalsIgnoreCase(gdjsbh)){
//					sql += " where t.教室编号 not in (" + gdjsbh + ") ";
//				}
				
				sql += " order by t.实际容量,t.教室编号,t.已用时间序列";
//			}
		}else{
			sql = "select distinct * from (";
			
			//所有场地已用信息
			sql += "select a.教室编号,a.教室名称,a.教室类型代码,a.实际容量,isnull(b.时间序列,'') as 已用时间序列,b.实际场地编号,授课周次详情 " +
				"from V_教室数据类 a left join V_排课管理_课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%' " +
				"and b.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' " +
				"where a.是否可用='1' and a.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "' " +
				"union all ";
			//固排的场地占用信息
			sql += "select a1.教室编号,a1.教室名称,a1.教室类型代码,a1.实际容量,c1.时间序列,b1.场地要求,isnull(b1.授课周次详情, '') as 授课周次  from V_教室数据类 a1 " +
				"inner join V_规则管理_授课计划明细表 b1 on b1.场地要求 like '%'+a1.教室编号+'%' " +
				"inner join V_规则管理_固排禁排表 c1 on c1.授课计划明细编号=b1.授课计划明细编号 " +
				"where a1.是否可用='1' and b1.状态='1' and c1.状态='1' and c1.类型 ='2' and c1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and a1.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "'" +
				"union all ";
			//禁排的场地占用信息（不包含当前排课的专业班级）
//			sql += "select a1.教室编号,a1.教室名称,a1.教室类型代码,a1.实际容量,c1.时间序列,c1.预设场地编号,isnull(b1.授课周次详情, '') as 授课周次 from V_教室数据类 a1 " +
//				"inner join V_规则管理_授课计划明细表 b1 on b1.场地要求 like '%'+a1.教室编号+'%' " +
//				"inner join V_规则管理_固排禁排表 c1 on c1.授课计划明细编号=b1.授课计划明细编号 " +
////				"and c1.行政班代码  not in (" + sqlCondition + ") " +
//				"where a1.是否可用='1' and b1.状态='1' and c1.状态='1' and c1.类型 ='3' and c1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and a1.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "' ) as t";
			
			sql += "select a1.教室编号,a1.教室名称,a1.教室类型代码,a1.实际容量,c1.时间序列,c1.行政班代码, '1-" + MyTools.fixSql(MyTools.StrFiltr(vec2.get(0))) + "' as 授课周次 from V_教室数据类 a1 " +
					" inner join V_规则管理_固排禁排表 c1 on c1.行政班代码=a1.教室编号  " +
					" where a1.是否可用='1' and c1.状态='1' and c1.类型 ='3' and c1.禁排类型 = 'cd' and c1.学年学期编码 like '" + MyTools.fixSql(this.getPK_XNXQBM().substring(0, 5)) + "%' and a1.校区代码 = '" + MyTools.fixSql(this.getPK_XB()) + "' ) as t";
			
//			if(!"".equalsIgnoreCase(gdjsbh)){
//				sql += " where t.教室编号 not in (" + gdjsbh + ") ";
//			}
			
			sql += " order by t.实际容量,t.教室编号,t.已用时间序列";
		}
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=7){
				//判断是否同一场地
				if(!tempSiteCode.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
					if(i > 0){
						resultVec.add(usedOrderVec);
					}
					tempSiteCode = MyTools.StrFiltr(vec.get(i)); //教室编号
					resultVec.add(tempSiteCode);
					resultVec.add(MyTools.StrFiltr(vec.get(i+1))); //教室名称
					resultVec.add(MyTools.StrFiltr(vec.get(i+2))); //教室类型代码
					resultVec.add(MyTools.StrFiltr(vec.get(i+3))); //实际容量
					usedOrderVec = new Vector();
					tempSkzcVec = new Vector();
				}
				
				usedOrderVec.add(MyTools.StrFiltr(vec.get(i+4))); //已用时间序列
				curCdbhArray = MyTools.StrFiltr(vec.get(i+5)).split("｜"); //实际场地编号
				curSkzcArray = MyTools.StrFiltr(vec.get(i+6)).split("｜"); //授课周次详情
				skzeVec = new Vector();
				for(int j=0; j<curCdbhArray.length; j++){
					tempCdbhArray = curCdbhArray[j].split("&");
					tempSkzcArray = curSkzcArray[j].split("&");
					
					for(int k=0; k<tempCdbhArray.length; k++){
						cdbhArray = tempCdbhArray[k].split("\\+");
						
						for(int a=0; a<cdbhArray.length; a++){
							//判断是否同一个场地，获取授课周次
							if(tempSiteCode.equalsIgnoreCase(MyTools.StrFiltr(cdbhArray[a]))){
								tempSkzcVec = this.formatSkzc(MyTools.StrFiltr(tempSkzcArray[k]), xqzc);
								
								for(int b=0; b<tempSkzcVec.size(); b++){
									skzeVec.add(tempSkzcVec.get(b));
								}
							}
						}
					}
				}
				
				usedOrderVec.add(skzeVec);
			}
			resultVec.add(usedOrderVec);
		}
		
//		String classCode = "";
//		Map classOrderMap = new HashMap();
//		//判断如果是全新排课，需要将初始化课表中的固排教室使用情况放入集合中
//		if("1".equalsIgnoreCase(pkType)){
//			Set keysSet = allClassKcbMap.keySet();
//			Iterator iterator = keysSet.iterator();
//			while(iterator.hasNext()) {
//				classCode = (String)iterator.next();//key
//				classOrderMap = (HashMap)allClassKcbMap.get(classCode);//value
//				
//				
//			}
//		}
		
		/*//获取所有时间序列
		sql = "select 每周天数,上午节数+下午节数+晚上节数 from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		Vector orderVec = new Vector();
		int mzts = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));
		int mtjs = MyTools.StringToInt(MyTools.StrFiltr(vec.get(1)));
		for(int i=1; i<mzts+1; i++){
			for(int j=1; j<mtjs+1; j++){
				orderVec.add((i<10?"0"+i:""+i) + (j<10?"0"+j:""+j));
			}
		}
		
		Vector tempOrderVec = new Vector();
		//获取可用时间序列
		for(int i=0; i<resultVec.size(); i+=4){
			usedOrderVec = (Vector)resultVec.get(i+3);
			tempOrderVec = (Vector)orderVec.clone();
			
			for(int j=0; j<usedOrderVec.size(); j++){
				if(tempOrderVec.indexOf(MyTools.StrFiltr(usedOrderVec.get(j))) > -1){
					tempOrderVec.remove(tempOrderVec.indexOf(MyTools.StrFiltr(usedOrderVec.get(j))));
				}
			}
			resultVec.set(i+3, tempOrderVec);
		}*/
		return resultVec;
	}
	
	/**
	 * 检查教室在当前时间序列内是否可用(没有指定的教室的情况下自动分配教室)
	 * @date:2015-07-10
	 * @param cdyqArray 场地要求
	 * @param curTimeOrder 当前时间序列
	 * @param lj 连节数
	 * @param skzcDetailArray 授课周次
	 * @param xqzc 学期周次
	 * @param allSiteInfoVec 所有场地相关信息
	 * @param personNum 班级人数
	 * @return boolean 是否可用
	 * @author:yeq
	 */
	public Vector checkSite(String[] cdyqArray, String curTimeOrder, int lj, String[] skzcDetailArray, String xqzc, Vector tempAllSiteInfoVec, int personNum){
		Vector resultVec = new Vector();
		boolean flag = true;
		Vector curSiteInfo = null;
		String tempTimeOrder = curTimeOrder;
		int tempNum = 0;
		Vector tempSkzcVec = new Vector();
		Vector tempVec = new Vector();
		Vector curSkzcVec = new Vector();
		String curCdyqArray[] = new String[0];
		String curCdyq = "";
		String sjcdbh = "";
		String tempSjcdbh = "";
		String sjcdmc = "";
		String tempSjcdmc = "";
		String specialCode[] = MyTools.getProp(request, "Base.specialCode").split(",");//教室特殊类型代码
		boolean specialFlag = false;
		
		for(int i=0; i<cdyqArray.length; i++){
			curCdyqArray = cdyqArray[i].split("\\+");
			curSkzcVec = this.formatSkzc(skzcDetailArray[i], xqzc);
			tempSjcdbh = "";
			tempSjcdmc = "";
			int cdnum = curCdyqArray.length; //场地个数
			
			for(int j=0; j<curCdyqArray.length; j++){
				curCdyq = curCdyqArray[j];
				cdnum = cdnum-j;
				
				//判断是否指定了场地
//				if(curCdyq.length() > 1){
				if(curCdyq.length() > 2){
					//判断是否有这个教室存在
					if(tempAllSiteInfoVec.indexOf(curCdyq) > -1){
						//判断如果是特殊类型(可多个班级同时使用)的教室不检查是否可用
						specialFlag = false;
						for(int z=0; z<specialCode.length; z++){
							if(specialCode[z].equalsIgnoreCase(MyTools.StrFiltr(tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+2)))){
								specialFlag = true;
								break;
							}
						}
						
						if(specialFlag == false){
							//判断指定的场地是否可用
							curSiteInfo = (Vector)((Vector)tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+4)).clone();//获取当前场地已用时间序列信息
							
							//检查所有的连节位置
							for(int k=1; k<(lj+1); k++){
								if(k > 1){
									tempNum = MyTools.StringToInt(tempTimeOrder.substring(2))+1;
									tempTimeOrder = tempTimeOrder.substring(0, 2) + (tempNum<10?("0"+tempNum):(""+tempNum));
								}
								//判断当前时间序列是否被使用
								//如果教室可以使用，更新教室使用信息
								if(curSiteInfo.indexOf(tempTimeOrder) > -1){
									//检查教室使用的周次是否与当前授课周次冲突
									tempSkzcVec = (Vector)((Vector)curSiteInfo.get(curSiteInfo.indexOf(tempTimeOrder)+1)).clone();
									
									if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
										flag = false;
										break;
									}
									//没有冲突的话，更新使用周次
									for(int a=0; a<curSkzcVec.size(); a++){
										tempSkzcVec.add(curSkzcVec.get(a));
									}
									curSiteInfo.set(curSiteInfo.indexOf(tempTimeOrder)+1, tempSkzcVec);
								}else{
									curSiteInfo.add(tempTimeOrder);
									curSiteInfo.add(curSkzcVec);
								}
							}
							if(flag == true){//如果可用，更新场地信息
								tempAllSiteInfoVec.set(tempAllSiteInfoVec.indexOf(curCdyq)+4, curSiteInfo);
							}
							//获取实际场地编号和名称
							if("".equalsIgnoreCase(tempSjcdbh)){
								tempSjcdbh = curCdyq;
							}else{
								tempSjcdbh += "+"+curCdyq;
							}
							if("".equalsIgnoreCase(tempSjcdmc)){
								tempSjcdmc = MyTools.StrFiltr(tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+1));
							}else{
								tempSjcdmc += "+"+MyTools.StrFiltr(tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+1));
							}
						}
					}else{
						flag = false;
						break;
					}
				}else{//未指定场地的话，根据场地类型自动分配教室
					tempVec = this.autoGetSite(curCdyq, tempAllSiteInfoVec, personNum, curTimeOrder, lj, curSkzcVec, xqzc);
					flag = (Boolean)tempVec.get(0);
					
					//获取实际场地编号和名称
					if("".equalsIgnoreCase(tempSjcdbh)){
						tempSjcdbh = MyTools.StrFiltr(tempVec.get(1));
					}else{
						tempSjcdbh += "+"+MyTools.StrFiltr(tempVec.get(1));
					}
					if("".equalsIgnoreCase(tempSjcdmc)){
						tempSjcdmc = MyTools.StrFiltr(tempVec.get(2));
					}else{
						tempSjcdmc += "+"+MyTools.StrFiltr(tempVec.get(2));
					}
				}
				if(flag == false){
					break;
				}
			}
			if(flag == false){
				break;
			}
			
			//获取实际场地编号和名称
			if("".equalsIgnoreCase(sjcdbh)){
				sjcdbh = tempSjcdbh;
			}else{
				sjcdbh += "&"+tempSjcdbh;
			}
			if("".equalsIgnoreCase(sjcdmc)){
				sjcdmc = tempSjcdmc;
			}else{
				sjcdmc += "&"+tempSjcdmc;
			}
		}

		resultVec.add(flag);
		resultVec.add(sjcdbh);
		resultVec.add(sjcdmc);
		return resultVec;
	}
	
	
	/**
	 * 检查教室在当前时间序列内是否可用(没有指定的教室的情况下自动分配教室)
	 * @date:2017-07-1
	 * @param cdyqArray 场地要求
	 * @param curTimeOrder 当前时间序列
	 * @param lj 连节数
	 * @param skzcDetailArray 授课周次
	 * @param xqzc 学期周次
	 * @param allSiteInfoVec 所有场地相关信息
	 * @param personNum 班级人数
	 * @param classFixedClassroom 班级固定教室信息
	 * @param xzbdm 行政班代码
	 * @return boolean 是否可用
	 * @author:yeq
	 */
	public Vector checkSite2(String[] cdyqArray, String curTimeOrder, int lj, String[] skzcDetailArray, String xqzc, Vector tempAllSiteInfoVec, int personNum, Vector classFixedClassroom, String xzbdm){
		Vector resultVec = new Vector();
		boolean flag = true;
		Vector curSiteInfo = null;
		String tempTimeOrder = curTimeOrder;
		int tempNum = 0;
		Vector tempSkzcVec = new Vector();
		Vector tempVec = new Vector();
		Vector curSkzcVec = new Vector();
		String curCdyqArray[] = new String[0];
		String curCdyq = "";
		String sjcdbh = "";
		String tempSjcdbh = "";
		String sjcdmc = "";
		String tempSjcdmc = "";
		String specialCode[] = MyTools.getProp(request, "Base.specialCode").split(",");//教室特殊类型代码
		boolean specialFlag = false;
		
		for(int i=0; i<cdyqArray.length; i++){
			curCdyqArray = cdyqArray[i].split("\\+");
			curSkzcVec = this.formatSkzc(skzcDetailArray[i], xqzc);
			tempSjcdbh = "";
			tempSjcdmc = "";
			int cdnum = curCdyqArray.length; //场地个数
			
			for(int j=0; j<curCdyqArray.length; j++){
				curCdyq = curCdyqArray[j];
				cdnum = cdnum-j;
				
				//判断是否指定了场地
//				if(curCdyq.length() > 1){
				if(curCdyq.length() > 2){
					//判断是否有这个教室存在
					if(tempAllSiteInfoVec.indexOf(curCdyq) > -1){
						//判断如果是特殊类型(可多个班级同时使用)的教室不检查是否可用
						specialFlag = false;
						for(int z=0; z<specialCode.length; z++){
							if(specialCode[z].equalsIgnoreCase(MyTools.StrFiltr(tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+2)))){
								specialFlag = true;
								break;
							}
						}
						
						if(specialFlag == false){
							//判断指定的场地是否可用
							curSiteInfo = (Vector)((Vector)tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+4)).clone();//获取当前场地已用时间序列信息
							
							//检查所有的连节位置
							for(int k=1; k<(lj+1); k++){
								if(k > 1){
									tempNum = MyTools.StringToInt(tempTimeOrder.substring(2))+1;
									tempTimeOrder = tempTimeOrder.substring(0, 2) + (tempNum<10?("0"+tempNum):(""+tempNum));
								}
								//判断当前时间序列是否被使用
								//如果教室可以使用，更新教室使用信息
								if(curSiteInfo.indexOf(tempTimeOrder) > -1){
									//检查教室使用的周次是否与当前授课周次冲突
									tempSkzcVec = (Vector)((Vector)curSiteInfo.get(curSiteInfo.indexOf(tempTimeOrder)+1)).clone();
									
									if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
										flag = false;
										break;
									}
									//没有冲突的话，更新使用周次
									for(int a=0; a<curSkzcVec.size(); a++){
										tempSkzcVec.add(curSkzcVec.get(a));
									}
									curSiteInfo.set(curSiteInfo.indexOf(tempTimeOrder)+1, tempSkzcVec);
								}else{
									curSiteInfo.add(tempTimeOrder);
									curSiteInfo.add(curSkzcVec);
								}
							}
							if(flag == true){//如果可用，更新场地信息
								tempAllSiteInfoVec.set(tempAllSiteInfoVec.indexOf(curCdyq)+4, curSiteInfo);
							}
							//获取实际场地编号和名称
							if("".equalsIgnoreCase(tempSjcdbh)){
								tempSjcdbh = curCdyq;
							}else{
								tempSjcdbh += "+"+curCdyq;
							}
							if("".equalsIgnoreCase(tempSjcdmc)){
								tempSjcdmc = MyTools.StrFiltr(tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+1));
							}else{
								tempSjcdmc += "+"+MyTools.StrFiltr(tempAllSiteInfoVec.get(tempAllSiteInfoVec.indexOf(curCdyq)+1));
							}
						}
					}else{
						flag = false;
						break;
					}
				}else{//未指定场地的话，根据场地类型自动分配教室
					tempVec = this.autoGetSite2(curCdyq, tempAllSiteInfoVec, personNum, curTimeOrder, lj, curSkzcVec, xqzc, classFixedClassroom, cdnum, xzbdm);
					flag = (Boolean)tempVec.get(0);
					
					//获取实际场地编号和名称
					if("".equalsIgnoreCase(tempSjcdbh)){
						tempSjcdbh = MyTools.StrFiltr(tempVec.get(1));
					}else{
						tempSjcdbh += "+"+MyTools.StrFiltr(tempVec.get(1));
					}
					if("".equalsIgnoreCase(tempSjcdmc)){
						tempSjcdmc = MyTools.StrFiltr(tempVec.get(2));
					}else{
						tempSjcdmc += "+"+MyTools.StrFiltr(tempVec.get(2));
					}
				}
				if(flag == false){
					break;
				}
			}
			if(flag == false){
				break;
			}
			
			//获取实际场地编号和名称
			if("".equalsIgnoreCase(sjcdbh)){
				sjcdbh = tempSjcdbh;
			}else{
				sjcdbh += "&"+tempSjcdbh;
			}
			if("".equalsIgnoreCase(sjcdmc)){
				sjcdmc = tempSjcdmc;
			}else{
				sjcdmc += "&"+tempSjcdmc;
			}
		}

		resultVec.add(flag);
		resultVec.add(sjcdbh);
		resultVec.add(sjcdmc);
		return resultVec;
	}
	
	/**
	 * 自动分配教室
	 * @date:2015-07-10
	 * @param siteType 场地类型
	 * @param allSiteInfoVec 所有场地信息
	 * @param personNum 班级人数
	 * @param orderTime 时间序列
	 * @param lj 连节数
	 * @param skzc 授课周次
	 * @param xqzc 学期周次范围
	 * @return Vector 
	 * @author:yeq
	 * @param classFixedClassroom 班级固定教室信息
	 * @param cdnum 场地个数
	 */
	public Vector autoGetSite(String siteType, Vector allSiteInfoVec, int personNum, String orderTime, int ljNum, Vector curSkzcVec, String xqzc){
		Vector resultVec = new Vector();
		String resultSiteCode = "";
		String resultSiteName = "";
		String curSiteCode = "";//教室编号
		String curSiteName = "";//教室名称
		int curSiteSize = 0;//教室实际容量
		Vector curSiteUsedVec = null;//教室已用情况信息
		boolean flag = true;//用于判断是否分配了教室
		Vector tempSkzcVec = null;
		Vector tempAllSiteInfoVec  = null;
		String tempOrderTime = "";
		int tempNum = 0;
		
		
		
		//遍历所有场地使用情况信息
		for(int i=0; i<allSiteInfoVec.size(); i+=5){
			tempAllSiteInfoVec = (Vector)allSiteInfoVec.clone();
			flag = true;
			//判断教室类型是否和要求相符
			if(siteType.equalsIgnoreCase(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+2)))){
				curSiteSize = MyTools.StringToInt(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+3)));
				//判断最符合人数要求的场地，人数符合的情况下，检查其他信息
				if(curSiteSize >= personNum){
					curSiteCode = MyTools.StrFiltr(tempAllSiteInfoVec.get(i));
					curSiteName = MyTools.StrFiltr(tempAllSiteInfoVec.get(i+1));
					curSiteUsedVec = (Vector)tempAllSiteInfoVec.get(i+4);
					tempOrderTime = orderTime;
					
					//检查当前场地在连节的时间序列中是否可以使用
					for(int j=0; j<ljNum; j++){
						tempNum = MyTools.StringToInt(tempOrderTime.substring(2));
						if(j > 0){
							tempNum += 1;
						}
						tempOrderTime = tempOrderTime.substring(0, 2) + (tempNum<10?"0"+tempNum:""+tempNum);
						
						//判断当前教室是否已被使用
						if(curSiteUsedVec.indexOf(tempOrderTime) > -1){
							//检查教室使用的周次是否与当前授课周次冲突
							tempSkzcVec = (Vector)curSiteUsedVec.get(curSiteUsedVec.indexOf(tempOrderTime)+1);
							if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
								flag = false;
								break;
							}else{
								//整合授课周次数据
								for(int k=0; k<curSkzcVec.size(); k++){
									tempSkzcVec.add(curSkzcVec.get(k));
								}
								curSiteUsedVec.set(curSiteUsedVec.indexOf(tempOrderTime)+1, tempSkzcVec);
								tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
							}
						}else{
							curSiteUsedVec.add(tempOrderTime);
							curSiteUsedVec.add(curSkzcVec);
							tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
						}
					}
					//判断当前场地可用的情况下，获取场地信息，结束循环。
					if(flag == true){
						allSiteInfoVec = (Vector)tempAllSiteInfoVec.clone();
						resultSiteCode = curSiteCode;
						resultSiteName = curSiteName;
						break;
					}
				}
			}
		}
		
		//判断是否有可用教室
		if("".equalsIgnoreCase(resultSiteCode)){
			flag = false;
		}
		
		resultVec.add(flag);
		resultVec.add(resultSiteCode);
		resultVec.add(resultSiteName);
		return resultVec;
	}
	
	/**
	 * 自动分配教室
	 * @date:2017-07-1
	 * @param siteType 场地类型
	 * @param allSiteInfoVec 所有场地信息
	 * @param personNum 班级人数
	 * @param orderTime 时间序列
	 * @param lj 连节数
	 * @param skzc 授课周次
	 * @param xqzc 学期周次范围
	 * @return Vector 
	 * @author:yeq
	 * @param classFixedClassroom 班级固定教室信息
	 * @param cdnum 场地个数
	 * @param xzbdm 行政班代码
	 */
	public Vector autoGetSite2(String siteType, Vector allSiteInfoVec, int personNum, String orderTime, int ljNum, Vector curSkzcVec, String xqzc, Vector classFixedClassroom, int cdnum, String xzbdm){
		Vector resultVec = new Vector();
		String resultSiteCode = "";
		String resultSiteName = "";
		String curSiteCode = "";//教室编号
		String curSiteName = "";//教室名称
		int curSiteSize = 0;//教室实际容量
		Vector curSiteUsedVec = null;//教室已用情况信息
		boolean flag = true;//用于判断是否分配了教室
		Vector tempSkzcVec = null;
		Vector tempAllSiteInfoVec  = null;
		String tempOrderTime = "";
		int tempNum = 0;
		
		//--------------------------------------------------------------------------------------------------------
		
		String gdjsbh = ""; //班级固定教室编号
		String gdjsmc = ""; //班级固定教室名称
		
		for (int i = 0; i < classFixedClassroom.size(); i+=4) {
			if(xzbdm.equalsIgnoreCase(MyTools.StrFiltr(classFixedClassroom.get(i)))){
				gdjsbh = MyTools.StrFiltr(classFixedClassroom.get(i+2));
				gdjsmc = MyTools.StrFiltr(classFixedClassroom.get(i+3));
				break;
			}
		}
		
		if("".equalsIgnoreCase(gdjsbh)){
			//遍历所有场地使用情况信息
			for(int i=0; i<allSiteInfoVec.size(); i+=5){
				tempAllSiteInfoVec = (Vector)allSiteInfoVec.clone();
				flag = true;
				//判断教室类型是否和要求相符
				if(siteType.equalsIgnoreCase(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+2)))){
					curSiteSize = MyTools.StringToInt(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+3)));
					//判断最符合人数要求的场地，人数符合的情况下，检查其他信息
					if(curSiteSize >= personNum){
						curSiteCode = MyTools.StrFiltr(tempAllSiteInfoVec.get(i));
						curSiteName = MyTools.StrFiltr(tempAllSiteInfoVec.get(i+1));
						curSiteUsedVec = (Vector)tempAllSiteInfoVec.get(i+4);
						tempOrderTime = orderTime;
						
						//检查当前场地在连节的时间序列中是否可以使用
						for(int j=0; j<ljNum; j++){
							tempNum = MyTools.StringToInt(tempOrderTime.substring(2));
							if(j > 0){
								tempNum += 1;
							}
							tempOrderTime = tempOrderTime.substring(0, 2) + (tempNum<10?"0"+tempNum:""+tempNum);
							
							//判断当前教室是否已被使用
							if(curSiteUsedVec.indexOf(tempOrderTime) > -1){
								//检查教室使用的周次是否与当前授课周次冲突
								tempSkzcVec = (Vector)curSiteUsedVec.get(curSiteUsedVec.indexOf(tempOrderTime)+1);
								if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
									flag = false;
									break;
								}else{
									//整合授课周次数据
									for(int k=0; k<curSkzcVec.size(); k++){
										tempSkzcVec.add(curSkzcVec.get(k));
									}
									curSiteUsedVec.set(curSiteUsedVec.indexOf(tempOrderTime)+1, tempSkzcVec);
									tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
								}
							}else{
								curSiteUsedVec.add(tempOrderTime);
								curSiteUsedVec.add(curSkzcVec);
								tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
							}
						}
						//判断当前场地可用的情况下，获取场地信息，结束循环。
						if(flag == true){
							allSiteInfoVec = (Vector)tempAllSiteInfoVec.clone();
							resultSiteCode = curSiteCode;
							resultSiteName = curSiteName;
							break;
						}
					}
				}
			}
		}else{
			if(cdnum == 1 && "01".equalsIgnoreCase(siteType)){ //分配一间教室
				tempAllSiteInfoVec = (Vector)allSiteInfoVec.clone();
				flag = true;
				curSiteCode = gdjsbh;
				curSiteName = gdjsmc;
//				int i = tempAllSiteInfoVec.indexOf(gdjsbh);
//				curSiteUsedVec = (Vector)tempAllSiteInfoVec.get(i+4);
//				tempOrderTime = orderTime;
//				
//				//检查当前场地在连节的时间序列中是否可以使用
//				for(int j=0; j<ljNum; j++){
//					tempNum = MyTools.StringToInt(tempOrderTime.substring(2));
//					if(j > 0){
//						tempNum += 1;
//					}
//					tempOrderTime = tempOrderTime.substring(0, 2) + (tempNum<10?"0"+tempNum:""+tempNum);
//					
//					//判断当前教室是否已被使用
//					if(curSiteUsedVec.indexOf(tempOrderTime) > -1){
//						//检查教室使用的周次是否与当前授课周次冲突
//						tempSkzcVec = (Vector)curSiteUsedVec.get(curSiteUsedVec.indexOf(tempOrderTime)+1);
//						if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
//							flag = false;
//							break;
//						}else{
//							//整合授课周次数据
//							for(int k=0; k<curSkzcVec.size(); k++){
//								tempSkzcVec.add(curSkzcVec.get(k));
//							}
//							curSiteUsedVec.set(curSiteUsedVec.indexOf(tempOrderTime)+1, tempSkzcVec);
//							tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
//						}
//					}else{
//						curSiteUsedVec.add(tempOrderTime);
//						curSiteUsedVec.add(curSkzcVec);
//						tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
//					}
//				}
				//判断当前场地可用的情况下，获取场地信息，结束循环。
				if(flag == true){
					allSiteInfoVec = (Vector)tempAllSiteInfoVec.clone();
					resultSiteCode = curSiteCode;
					resultSiteName = curSiteName;
				}
			}else if(cdnum == 1 && !"01".equalsIgnoreCase(siteType)){ //分配一间教室
			
				//遍历所有场地使用情况信息
				for(int i=0; i<allSiteInfoVec.size(); i+=5){
					tempAllSiteInfoVec = (Vector)allSiteInfoVec.clone();
					flag = true;
					//判断教室类型是否和要求相符
					if(siteType.equalsIgnoreCase(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+2)))){
						curSiteSize = MyTools.StringToInt(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+3)));
						//判断最符合人数要求的场地，人数符合的情况下，检查其他信息
						if(curSiteSize >= personNum){
							curSiteCode = MyTools.StrFiltr(tempAllSiteInfoVec.get(i));
							curSiteName = MyTools.StrFiltr(tempAllSiteInfoVec.get(i+1));
							curSiteUsedVec = (Vector)tempAllSiteInfoVec.get(i+4);
							tempOrderTime = orderTime;
							
							//检查当前场地在连节的时间序列中是否可以使用
							for(int j=0; j<ljNum; j++){
								tempNum = MyTools.StringToInt(tempOrderTime.substring(2));
								if(j > 0){
									tempNum += 1;
								}
								tempOrderTime = tempOrderTime.substring(0, 2) + (tempNum<10?"0"+tempNum:""+tempNum);
								
								//判断当前教室是否已被使用
								if(curSiteUsedVec.indexOf(tempOrderTime) > -1){
									//检查教室使用的周次是否与当前授课周次冲突
									tempSkzcVec = (Vector)curSiteUsedVec.get(curSiteUsedVec.indexOf(tempOrderTime)+1);
									if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
										flag = false;
										break;
									}else{
										//整合授课周次数据
										for(int k=0; k<curSkzcVec.size(); k++){
											tempSkzcVec.add(curSkzcVec.get(k));
										}
										curSiteUsedVec.set(curSiteUsedVec.indexOf(tempOrderTime)+1, tempSkzcVec);
										tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
									}
								}else{
									curSiteUsedVec.add(tempOrderTime);
									curSiteUsedVec.add(curSkzcVec);
									tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
								}
							}
							//判断当前场地可用的情况下，获取场地信息，结束循环。
							if(flag == true){
								allSiteInfoVec = (Vector)tempAllSiteInfoVec.clone();
								resultSiteCode = curSiteCode;
								resultSiteName = curSiteName;
								break;
							}
						}
					}
				}
			}else{
				//遍历所有场地使用情况信息
				for(int i=0; i<allSiteInfoVec.size(); i+=5){
					tempAllSiteInfoVec = (Vector)allSiteInfoVec.clone();
					flag = true;
					//判断教室类型是否和要求相符
					if(siteType.equalsIgnoreCase(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+2)))){
						curSiteSize = MyTools.StringToInt(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+3)));
						//判断最符合人数要求的场地，人数符合的情况下，检查其他信息
						if(curSiteSize >= personNum){
							curSiteCode = MyTools.StrFiltr(tempAllSiteInfoVec.get(i));
							curSiteName = MyTools.StrFiltr(tempAllSiteInfoVec.get(i+1));
							curSiteUsedVec = (Vector)tempAllSiteInfoVec.get(i+4);
							tempOrderTime = orderTime;
							
							//检查当前场地在连节的时间序列中是否可以使用
							for(int j=0; j<ljNum; j++){
								tempNum = MyTools.StringToInt(tempOrderTime.substring(2));
								if(j > 0){
									tempNum += 1;
								}
								tempOrderTime = tempOrderTime.substring(0, 2) + (tempNum<10?"0"+tempNum:""+tempNum);
								
								//判断当前教室是否已被使用
								if(curSiteUsedVec.indexOf(tempOrderTime) > -1){
									//检查教室使用的周次是否与当前授课周次冲突
									tempSkzcVec = (Vector)curSiteUsedVec.get(curSiteUsedVec.indexOf(tempOrderTime)+1);
									if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
										flag = false;
										break;
									}else{
										//整合授课周次数据
										for(int k=0; k<curSkzcVec.size(); k++){
											tempSkzcVec.add(curSkzcVec.get(k));
										}
										curSiteUsedVec.set(curSiteUsedVec.indexOf(tempOrderTime)+1, tempSkzcVec);
										tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
									}
								}else{
									curSiteUsedVec.add(tempOrderTime);
									curSiteUsedVec.add(curSkzcVec);
									tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
								}
							}
							//判断当前场地可用的情况下，获取场地信息，结束循环。
							if(flag == true){
								allSiteInfoVec = (Vector)tempAllSiteInfoVec.clone();
								resultSiteCode = curSiteCode;
								resultSiteName = curSiteName;
								break;
							}
						}
					}
				}
			}
		}
		
		
		
		//-----------------------------------------------------------------------------------------------------------
		
//		//遍历所有场地使用情况信息
//		for(int i=0; i<allSiteInfoVec.size(); i+=5){
//			tempAllSiteInfoVec = (Vector)allSiteInfoVec.clone();
//			flag = true;
//			//判断教室类型是否和要求相符
//			if(siteType.equalsIgnoreCase(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+2)))){
//				curSiteSize = MyTools.StringToInt(MyTools.StrFiltr(tempAllSiteInfoVec.get(i+3)));
//				//判断最符合人数要求的场地，人数符合的情况下，检查其他信息
//				if(curSiteSize >= personNum){
//					curSiteCode = MyTools.StrFiltr(tempAllSiteInfoVec.get(i));
//					curSiteName = MyTools.StrFiltr(tempAllSiteInfoVec.get(i+1));
//					curSiteUsedVec = (Vector)tempAllSiteInfoVec.get(i+4);
//					tempOrderTime = orderTime;
//					
//					//检查当前场地在连节的时间序列中是否可以使用
//					for(int j=0; j<ljNum; j++){
//						tempNum = MyTools.StringToInt(tempOrderTime.substring(2));
//						if(j > 0){
//							tempNum += 1;
//						}
//						tempOrderTime = tempOrderTime.substring(0, 2) + (tempNum<10?"0"+tempNum:""+tempNum);
//						
//						//判断当前教室是否已被使用
//						if(curSiteUsedVec.indexOf(tempOrderTime) > -1){
//							//检查教室使用的周次是否与当前授课周次冲突
//							tempSkzcVec = (Vector)curSiteUsedVec.get(curSiteUsedVec.indexOf(tempOrderTime)+1);
//							if(this.judgeSkzc(curSkzcVec, tempSkzcVec)){
//								flag = false;
//								break;
//							}else{
//								//整合授课周次数据
//								for(int k=0; k<curSkzcVec.size(); k++){
//									tempSkzcVec.add(curSkzcVec.get(k));
//								}
//								curSiteUsedVec.set(curSiteUsedVec.indexOf(tempOrderTime)+1, tempSkzcVec);
//								tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
//							}
//						}else{
//							curSiteUsedVec.add(tempOrderTime);
//							curSiteUsedVec.add(curSkzcVec);
//							tempAllSiteInfoVec.set(i+4, curSiteUsedVec);
//						}
//					}
//					//判断当前场地可用的情况下，获取场地信息，结束循环。
//					if(flag == true){
//						allSiteInfoVec = (Vector)tempAllSiteInfoVec.clone();
//						resultSiteCode = curSiteCode;
//						resultSiteName = curSiteName;
//						break;
//					}
//				}
//			}
//		}
		
		//判断是否有可用教室
		if("".equalsIgnoreCase(resultSiteCode)){
			flag = false;
		}
		
		resultVec.add(flag);
		resultVec.add(resultSiteCode);
		resultVec.add(resultSiteName);
		return resultVec;
	}
		
	/**
	 * 检查当前教师是否在当前时间序列排过课程
	 * @date:2015-06-19
	 * @author:yeq
	 * @param skjsbhArray 所有授课教师编号
	 * @param teaJpMap 教师禁排信息
	 * @param teaJpFlag 用于判断是否需要验证教师禁排
	 * @param allTeaUsedOrder 当前教师已排课的时间序列信息
	 * @param tsgzVec 特殊规则信息
	 * @param tsgzFlag 用于判断是否需要验证特殊规则
	 * @param maxLessonNum 每天最大节数
	 * @param tempAllClassKcbMap 所有班级课表
	 * @param skjhAndTeaVec 授课计划与授课教师的关系信息
	 * @param kcdm 课程代码
	 * @param xzbdm 行政班代码
	 * @param lj 连节数
	 * @param curTimeOrder 当前时间序列
	 * @param skzcDetailArray 授课周次详情
	 * @param xqzc 学期周次范围
	 * @param teaskjhmxbhVector 教师授课计划明细集合
	 * @param teaypskjhmxbhVector 教师已排授课计划明细集合
	 * @return boolean
	 */
	public boolean checkTea(String[] skjsbhArray, Map teaJpMap, String teaJpFlag, Map tempAllTeaUsedOrder, Vector tsgzVec, String tsgzFlag, int maxLessonNum, Map tempAllClassKcbMap, Vector skjhAndTeaVec, String kcdm, String xzbdm, int lj, String curTimeOrder, String[] skzcDetailArray, String xqzc, Vector teaskjhmxbhVector, Vector teaypskjhmxbhVector){
		boolean flag = true;
		Vector tempVec = null;
		String curTeaCode = "";//当前教师编号
//		Map curTeaJpMap = new HashMap();//当前教师禁排信息
		Vector curTeaUsedOrder = null;//当前教师已排课的时间序列信息
		String tempSkjsbhArray[] = new String[0];//临时授课教师编号
		String tempLjOrder = "";//用于临时存放需要判断的连节的时间序列
		Vector usedSkzcVec = null;
		Vector curSkzcVec = null;
		int mtcs = 0;//每天次数(特殊规则)
		int mzcs = 0;//每周次数(特殊规则)
		int mtjc = 0;//每天节次(特殊规则)
		int mzjc = 0;//每周节次(特殊规则)
		String tempDayNum = "";
		int mtcsNum = 0;//用于记录当天课程安排次数
		int mzcsNum = 0;//用于记录每周课程安排次数
		int mtjcNum = 0;//用于记录当天课程安排节次
		int mzjcNum = 0;//用于记录每周课程安排节次
		
		//遍历当前课程所有授课教师,判断排课情况
		for(int i=0; i<skjsbhArray.length; i++){
			tempSkjsbhArray = skjsbhArray[i].split("\\+");
			
			for(int j=0; j<tempSkjsbhArray.length; j++){
				curTeaCode = tempSkjsbhArray[j];
//				curTeaJpMap = (Map)teaJpMap.get(curTeaCode);
				curTeaUsedOrder = (Vector)tempAllTeaUsedOrder.get(curTeaCode);
				
				//获取当前授课教师设置的特殊规则（每天节次）
				if(tsgzVec.indexOf(curTeaCode) > -1){
					mtjc = MyTools.StringToInt(MyTools.StrFiltr(tsgzVec.get(tsgzVec.indexOf(curTeaCode)+1)));
				}else{
					mtjc = 0;
				}
				
				//获取当前授课教师设置的特殊规则（每周节次）
				if(tsgzVec.indexOf(curTeaCode) > -1){
					mzjc = MyTools.StringToInt(MyTools.StrFiltr(tsgzVec.get(tsgzVec.indexOf(curTeaCode)+2)));
				}else{
					mzjc = 0;
				}
				
				//获取当前授课教师设置的特殊规则（每天次数）
				if(tsgzVec.indexOf(curTeaCode) > -1){
					mtcs = MyTools.StringToInt(MyTools.StrFiltr(tsgzVec.get(tsgzVec.indexOf(curTeaCode)+3)));
				}else{
					mtcs = 0;
				}
				
				//获取当前授课教师设置的特殊规则（每周次数）
				if(tsgzVec.indexOf(curTeaCode) > -1){
					mzcs = MyTools.StringToInt(MyTools.StrFiltr(tsgzVec.get(tsgzVec.indexOf(curTeaCode)+4)));
				}else{
					mzcs = 0;
				}
				
				
				
				//判断是否需要检查特殊规则
				if("1".equalsIgnoreCase(tsgzFlag)){
					String tempTeaCode = "";//用于临时存放教师编号
					
					//检查当前随机到的位置是否符合当前授课教师设置的特殊规则
					if(mtjc > 0){
						mtjcNum = 0;//用于记录当天课程安排次数
						tempDayNum = curTimeOrder.substring(0, 2);
						
						if(curTeaUsedOrder!=null && curTeaUsedOrder.size()>0){
							for(int k=0; k<curTeaUsedOrder.size(); k+=2){
								//判断是否同一天
								if(tempDayNum.equalsIgnoreCase(MyTools.StrFiltr(curTeaUsedOrder.get(k)).substring(0, 2))){
									//判断授课周次是否冲突
									if(this.judgeSkzc(this.formatSkzc(skzcDetailArray[i], xqzc), (Vector)curTeaUsedOrder.get(k+1))){
										mtjcNum++;
									}
								}
							}
						}
												
						//如果当天安排的节次已经大于等于规定的每天节次，跳过本次循环
						if(mtjcNum >= mtjc){
							flag = false;
							break;
						}
					}
					
					//检查当前随机到的位置是否符合当前授课教师设置的特殊规则（每周节次）
					if(mzjc > 0){
						mzjcNum = 0;//用于记录当天课程安排次数
						
						if(curTeaUsedOrder!=null && curTeaUsedOrder.size()>0){
							for(int k=0; k<curTeaUsedOrder.size(); k+=2){
								//判断授课周次是否冲突
								if(this.judgeSkzc(this.formatSkzc(skzcDetailArray[i], xqzc), (Vector)curTeaUsedOrder.get(k+1))){
									mzjcNum++;
								}
								
							}
						}						
						
						//如果这周安排的节次已经大于等于规定的这周节次，跳过本次循环
						if(mzjcNum >= mzjc){
							flag = false;
							break;
						}
					}
					
					
					
					//==================================================================================================
					/**特殊规则设置*/
					Vector zzList = new Vector(); //最终结果集合【时间序列前两位例如01，授课周次详情集合【1，2，3】】
					Vector zzList2 = new Vector(); //最终结果集合【时间序列前两位例如01，授课周次详情集合【1，2，3】】
					Vector teaSKJHMXBH = new Vector(); //【授课计划明细编号，授课周次详情集合【1，2，3】】
					Vector teaypSKJHMXBH = new Vector(); //【时间序列，授课计划明细编号，授课周次详情集合【1，2，3】，连接相关编号】
					
					//整合教师授课计划明细集合，授课周次详情拆分
					for (int k = 0; k < teaskjhmxbhVector.size(); k+=3) {
						if(curTeaCode.equalsIgnoreCase((String) teaskjhmxbhVector.get(k))){
							teaSKJHMXBH.add((String)teaskjhmxbhVector.get(k+1));
							Vector vector = this.formatSkzc(MyTools.StrFiltr(teaskjhmxbhVector.get(k+2)), xqzc);
							teaSKJHMXBH.add(vector);
						}
					}
					
					//整合教师已排授课计划明细集合，授课周次详情拆分【授课教师编号，授课计划明细编号，授课周次详情，连接相关编号，时间序列】
					for (int k = 0; k < teaypskjhmxbhVector.size(); k+=5) {
						if(curTeaCode.equalsIgnoreCase((String) teaypskjhmxbhVector.get(k))){
							teaypSKJHMXBH.add(teaypskjhmxbhVector.get(k+4));
							teaypSKJHMXBH.add(teaypskjhmxbhVector.get(k+1));
							Vector vector = this.formatSkzc(MyTools.StrFiltr(teaypskjhmxbhVector.get(k+2)), xqzc);
							teaypSKJHMXBH.add(vector);
							teaypSKJHMXBH.add(teaypskjhmxbhVector.get(k+3));
						}
					}
					
					//--【时间序列，授课计划明细编号，授课周次详情集合【1，2，3】，连接相关编号】
					for (int m = 0; m < teaSKJHMXBH.size(); m+=2) {
						for (int n = 0; n < teaypSKJHMXBH.size(); n+=4) {
							if(teaSKJHMXBH.get(m).equals(teaypSKJHMXBH.get(n+1))){
								String skjhmx = (String) teaypSKJHMXBH.get(n+1);
								String sjxl = (String) teaypSKJHMXBH.get(n);
								sjxl = sjxl.substring(0,2);
								String lxsjxl = (String) teaypSKJHMXBH.get(n+3);
								String lxsjxlArray[] = lxsjxl.split(",");
								zzList2.add(sjxl);
								zzList2.add(teaypSKJHMXBH.get(n+2));
								teaypSKJHMXBH.remove(n+3);
								teaypSKJHMXBH.remove(n+2);
								teaypSKJHMXBH.remove(n+1);
								teaypSKJHMXBH.remove(n);
								for (int k = 0; k < lxsjxlArray.length; k++) {
									for(int z=0; z<teaypSKJHMXBH.size(); z+=4){
										if(!"".equalsIgnoreCase(lxsjxlArray[k])){
//											int index = teaypSKJHMXBH.indexOf(lxsjxlArray[k]);
//											if(index > -1 && skjhmx.equalsIgnoreCase((String) teaypSKJHMXBH.get(z+1))){
//												teaypSKJHMXBH.remove(index+3);
//												teaypSKJHMXBH.remove(index+2);
//												teaypSKJHMXBH.remove(index+1);
//												teaypSKJHMXBH.remove(index);
//												z = z-4;
//											}
											if(MyTools.StrFiltr(teaypSKJHMXBH.get(z)).indexOf(lxsjxlArray[k]) > -1 && skjhmx.equalsIgnoreCase((String) teaypSKJHMXBH.get(z+1)) ) {
												teaypSKJHMXBH.remove(z+3);
												teaypSKJHMXBH.remove(z+2);
												teaypSKJHMXBH.remove(z+1);
												teaypSKJHMXBH.remove(z);
												z = z-4;
											}
										}
									}
								}
								n = n-4;
							}
						}
					}
					
					
					
					
					//获取map里的map里的list值
					for (Object in : tempAllClassKcbMap.keySet()) {
						
						Vector templist = new Vector(); //【时间序列，授课计划明细编号，连接时间序列】
						Vector kcList = new Vector(); //【时间序列，授课计划明细编号，连接时间序列】
						Vector tempmzList = new Vector(); //【时间序列，授课计划明细编号，连接时间序列】
						
						 Map<String, Vector> kcMap= (Map<String, Vector>) tempAllClassKcbMap.get(in);
						 for(String in2 : kcMap.keySet()) {
							 kcList = (Vector) kcMap.get(in2);
							 String sjxl = in2;
							 for (int k = 0; k < kcList.size(); k+=5) {
								if(!"".equalsIgnoreCase((String) kcList.get(k))){
									templist.add(sjxl); //时间序列
									templist.add(MyTools.StrFiltr(kcList.get(k+1))); //授课计划明细编号
									templist.add(MyTools.StrFiltr(kcList.get(k+4))); //连续时间序列编号
								}
							}
						 }
					 
					
						for (int m = 0; m < templist.size(); m+=3) {
							if(((String) templist.get(m+1)).indexOf("｜")>-1) {
								String[] skjhmxbhArray1 = ((String) templist.get(m+1)).split("｜");
//								String[] skjhmxbhArray2 = ((String) templist.get(m+2)).split("｜");
								for (int n = 0; n < skjhmxbhArray1.length; n++) {
									tempmzList.add(templist.get(m));
									tempmzList.add(skjhmxbhArray1[n]);
									tempmzList.add(templist.get(m+2));
//									tempmzList.add(skjhmxbhArray2[n]);
								}
							}else {
								tempmzList.add(templist.get(m));
								tempmzList.add(templist.get(m+1));
								tempmzList.add(templist.get(m+2));
							}
							
						}
						
						
						
						for (int m = 0; m < teaSKJHMXBH.size(); m+=2) {
							for (int n = 0; n < tempmzList.size(); n+=3) { //课程表已排信息
								if(teaSKJHMXBH.get(m).equals(tempmzList.get(n+1))){
									String skjhmx = (String) tempmzList.get(n+1);
									String sjxl = (String) tempmzList.get(n);
									sjxl = sjxl.substring(0,2);
									String lxsjxl = (String) tempmzList.get(n+2);
									String lxsjxlArray[] = lxsjxl.split(",");
									zzList.add(sjxl);
									zzList.add(teaSKJHMXBH.get(m+1));
									tempmzList.remove(n+2);
									tempmzList.remove(n+1);
									tempmzList.remove(n);
									for (int k = 0; k < lxsjxlArray.length; k++) {
										for(int z=0; z<tempmzList.size(); z+=3){
											if(!"".equalsIgnoreCase(lxsjxlArray[k])){
//												int index = tempmzList.indexOf(lxsjxlArray[k]);
//												if(index > -1 && skjhmx.equalsIgnoreCase((String) tempmzList.get(z+1))){
//													tempmzList.remove(index+2);
//													tempmzList.remove(index+1);
//													tempmzList.remove(index);
//													z = z-3;
//												}
												if(MyTools.StrFiltr(tempmzList.get(z)).indexOf(lxsjxlArray[k]) > -1 && skjhmx.equalsIgnoreCase((String) tempmzList.get(z+1)) ) {
													tempmzList.remove(z+2);
													tempmzList.remove(z+1);
													tempmzList.remove(z);
													z = z-3;
												}
												
											}
										}
									}
									n = n-3;
								}
							}
						}
						
					
					}
					
					
					
					//检查当前随机到的位置是否符合当前授课教师设置的特殊规则（每天次数）
					if(mtcs > 0){
						mtcsNum = 0;//用于记录当天课程安排次数
						tempDayNum = curTimeOrder.substring(0, 2);
						//每天次数
						for(int k=0; k<zzList.size(); k+=2){
							//判断是否同一天
							if(tempDayNum.equalsIgnoreCase(MyTools.StrFiltr(zzList.get(k)))){
								//判断授课周次是否冲突
								if(this.judgeSkzc(this.formatSkzc(skzcDetailArray[i], xqzc), (Vector)zzList.get(k+1))){
									mtcsNum++;
								}
							}
						}
						
						//每天次数
						for(int k=0; k<zzList2.size(); k+=2){
							//判断是否同一天
							if(tempDayNum.equalsIgnoreCase(MyTools.StrFiltr(zzList2.get(k)))){
								//判断授课周次是否冲突
								if(this.judgeSkzc(this.formatSkzc(skzcDetailArray[i], xqzc), (Vector)zzList2.get(k+1))){
									mtcsNum++;
								}
							}
						}
						
						//如果当天安排的次数已经大于等于规定的每天次数，跳过本次循环
						if(mtcsNum >= mtcs){
							flag = false;
							break;
						}
					}
					
					
					//检查当前随机到的位置是否符合当前授课教师设置的特殊规则（每周次数）
					if(mzcs > 0){
						mzcsNum = 0;//用于记录当天课程安排次数
						//每周次数
						for(int k=0; k<zzList.size(); k+=2){
							//判断授课周次是否冲突
							if(this.judgeSkzc(this.formatSkzc(skzcDetailArray[i], xqzc), (Vector)zzList.get(k+1))){
								mzcsNum++;
							}
						}
						
						//每周次数
						for(int k=0; k<zzList2.size(); k+=2){
							//判断授课周次是否冲突
							if(this.judgeSkzc(this.formatSkzc(skzcDetailArray[i], xqzc), (Vector)zzList2.get(k+1))){
								mzcsNum++;
							}
						}
						
						//如果这周安排的次数已经大于等于规定的这周次数，跳过本次循环
						if(mzcsNum >= mzcs){
							flag = false;
							break;
						}
					}
					
					
					//==================================================================================================
					
				}
				
				
				
				//判断是否检查禁排(教师禁排信息放到前面 随机时间序列时处理)
				if("1".equalsIgnoreCase(teaJpFlag)){
					//如果课程设置了连节的情况下，需要所有时间序列是否满足要求
					for(int m=0; m<lj; m++){
						tempLjOrder = (Integer.parseInt(curTimeOrder)+m)<1000?"0"+(Integer.parseInt(curTimeOrder)+m):""+(Integer.parseInt(curTimeOrder)+m);
						//判断如果是禁排，终止循环
						if(this.checkTeaJp(curTeaCode, kcdm, xzbdm, teaJpMap, tempLjOrder)){
							flag = false;
							break;
						}
					}
				}
				if(flag == false){
					break;
				}
				
				//检查授课教师当前位置是否可以排课
				if(curTeaUsedOrder!=null && curTeaUsedOrder.size()>0){
					//解析当前时间序列的授课周次格式,将不同周次类型转换为统一格式
					curSkzcVec = this.formatSkzc(skzcDetailArray[i], xqzc);
					for(int k=0; k<lj; k++){
						tempLjOrder = (Integer.parseInt(curTimeOrder)+k)<1000?"0"+(Integer.parseInt(curTimeOrder)+k):""+(Integer.parseInt(curTimeOrder)+k);
						
						if(curTeaUsedOrder.indexOf(tempLjOrder) > -1){
							//解析已用时间序列的授课周次格式,将不同周次类型转换为统一格式
							usedSkzcVec = (Vector)curTeaUsedOrder.get(curTeaUsedOrder.indexOf(tempLjOrder)+1);
							
							//判断周次是否冲突
							if(this.judgeSkzc(curSkzcVec, usedSkzcVec)){
								flag = false;
								break;
							}
						}
					}
					if(flag == false){
						break;
					}
				}
			}
			if(flag == false){
				break;
			}
		}
		if(flag == true){
			this.updateTeaUsedOrder(skjsbhArray, skzcDetailArray, tempAllTeaUsedOrder, curTimeOrder, lj, xqzc);
		}
		return flag;
	}
	
	/**
	 * 更新教师已用时间序列信息
	 * @date:2015-06-25
	 * @author:yeq
	 * @param teaCodeArray 教师编号
	 * @param skzcDetailArray 授课周次
	 * @param allTeaUsedOrder 所有教师已用时间序列信息
	 * @param allClassKcbMap 所有班级课程表
	 * @param curTimeOrder 当前时间序列
	 * @param lj 连节数
	 * @param xqzc 学期周次
	 */
	public void updateTeaUsedOrder(String[] teaCodeArray, String[] skzcDetailArray, Map allTeaUsedOrder, String curTimeOrder, int lj, String xqzc){
		String tempTeaCodeArray[] = new String[0];
		String teaCode = "";
		Vector curTeaUsedOrder = null;
		int tempLesNum = MyTools.StringToInt(curTimeOrder.substring(2));
		String tempOrder = curTimeOrder;
		Vector skzcVec = null;
		Vector tempSkzcVec = null;
		String temp = "";
		int num = 0;
		//遍历当前课程所有授课教师,判断排课情况
		for(int i=0; i<teaCodeArray.length; i++){
			tempTeaCodeArray = teaCodeArray[i].split("\\+");
			skzcVec = this.formatSkzc(skzcDetailArray[i], xqzc);
			
			for(int j=0; j<tempTeaCodeArray.length; j++){
				teaCode = tempTeaCodeArray[j];
				curTeaUsedOrder = (Vector)allTeaUsedOrder.get(teaCode);
				if(curTeaUsedOrder == null){
					curTeaUsedOrder = new Vector();
				}
				
				//获取所有连节时间序列
				for(int k=0; k<lj; k++){
					tempLesNum += k;
					tempOrder = tempOrder.substring(0, 2) + (tempLesNum<10?"0"+tempLesNum:tempLesNum);
					
					//判断当前时间序列是否有使用信息
					if(curTeaUsedOrder.indexOf(tempOrder) > -1){
						tempSkzcVec = (Vector)((Vector)curTeaUsedOrder.get(curTeaUsedOrder.indexOf(tempOrder)+1)).clone();
						
						for(int m=0; m<skzcVec.size(); m++){
							tempSkzcVec.add(MyTools.StrFiltr(skzcVec.get(m)));
						}
						curTeaUsedOrder.set(curTeaUsedOrder.indexOf(tempOrder)+1, tempSkzcVec);
					}else{
						curTeaUsedOrder.add(tempOrder);
						curTeaUsedOrder.add(skzcVec);
					}
				}
				allTeaUsedOrder.put(teaCode, curTeaUsedOrder);
			}
		}
	}
	
	/**
	 * 解析各类课程信息
	 * @date:2015-06-29
	 * @param str 需要解析的信息
	 * @return Vector 解析的课程信息集合
	 * @author:yeq
	 * @throws SQLException
	 */
	public Vector parseCourseInfo(String str) throws SQLException{
		Vector result = new Vector();
		String strArray[] = str.split("\\|");
		String tempStrArray[] = new String[0];
		
		for(int i=0; i<strArray.length; i++){
			tempStrArray = strArray[i].split("&");
			
			for(int j=0; j<tempStrArray.length; j++){
				result.add(tempStrArray[j]);
			}
		}
		
		return result;
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
	 * 判断授课周次是否冲突
	 * @date:2015-07-29
	 * @param skzc_1 授课周次
	 * @param skzc_2 授课周次
	 * @return boolean
	 * @author:yeq
	 */
	public boolean judgeSkzc(Vector skzc_1, Vector skzc_2){
		for(int i=0; i<skzc_1.size(); i++){
			for(int j=0; j<skzc_2.size(); j++){
				if(MyTools.StrFiltr(skzc_1.get(i)).equalsIgnoreCase(MyTools.StrFiltr(skzc_2.get(j)))){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 检查当前时间序列是否禁排
	 * @date:2015-06-02
	 * @author:yeq
	 * @param curTeaCode 当前授课教师编号
	 * @param kcdm 课程代码
	 * @param xzbdm 行政班代码
	 * @param curTeaJpMap 当前教师禁排信息
	 * @param curTimeOrder 当前时间序列
	 * @return boolean 是否禁排
	 */
	public boolean checkTeaJp(String curTeaCode, String kcdm, String xzbdm, Map curTeaJpMap, String curTimeOrder){
		boolean flag = false;
		Map tempTeaJpMap = new HashMap();
		Vector tempVec = new Vector();
		
		//判断当前教师是否有禁排信息
//		if(curTeaJpMap!=null && curTeaJpMap.containsKey(curTeaCode)){
//			tempTeaJpMap = (Map)curTeaJpMap.get(curTeaCode);
//			
//			//判断是否有当前课程的禁排信息
//			if(tempTeaJpMap!=null && tempTeaJpMap.containsKey(kcdm)){
//				tempTeaJpMap = (Map)tempTeaJpMap.get(kcdm);
//				
//				//判断是否有当前班级的禁排信息
//				if(tempTeaJpMap.containsKey(xzbdm)){
//					tempTeaJpMap = (Map)tempTeaJpMap.get(xzbdm);
//					
//					//判断当前时间序列是否禁排
//					if(tempVec.indexOf(curTimeOrder) > -1){
//						flag = true;
//					}
//				}
//			}
//		}
		
		//判断当前教师是否有禁排信息
		if(curTeaJpMap!=null && curTeaJpMap.containsKey(curTeaCode)){
			tempVec = (Vector)curTeaJpMap.get(curTeaCode);
			//判断当前时间序列是否禁排
			if(tempVec.indexOf(curTimeOrder) > -1){
				flag = true;
			}
		}
		
		return flag;
	}
	
	/**
	 * 查询可以周次拼接的课程
	 * @date:2015-06-03
	 * @param code 当前授课计划明细编号
	 * @param curTimeOrder 当前时间序列
	 * @param xqzc 学期周次
	 * @param skzc 授课周次
	 * @param lj 连节
	 * @param allWpkcVec 未排课程集合
	 * @param allTeaUsedOrder 授课教师已排课的时间序列信息
	 * @param allSiteInfoVec 场地信息
	 * @param teaJpMap 教师禁排信息
	 * @param teaJpFlag 用于判断是否检查教室禁排信息
	 * @param tsgzVec 教师特殊规则
	 * @param tsgzFlag 用于判断是否检查教师特殊规则
	 * @param lessonNum 每天最大节数
	 * @param tempAllClassKcbMap 所有班级课表
	 * @param skjhAndTeaVec 授课计划与教师关联信息
	 * @param curKywzVec 当前班级可排位置
	 * @param xzbdm 行政班代码
	 * @param presonNum 班级人数
	 * @param hbInfoVec 合班设置信息
	 * @param teaskjhmxbhVector 教师授课计划明细编号集合
	 * @param teaypskjhmxbhVector 教师已排授课计划明细编号集合
	 * @param jsxbkcxx 教师其他系部课程信息
	 * return Vector 可拼接课程和当前授课教师未排课程最新信息集合
	 * @author:yeq
	 */
	public Vector checkPjkc(String code, String curTimeOrder, String xqzc, String skzc, int lj, Vector tempAllWpkcVec, 
			Map tempAllTeaUsedOrder, Vector tempAllSiteInfoVec, Map teaJpMap, String teaJpFlag, Vector tsgzVec, 
			String tsgzFlag, int lessonNum, Map tempAllClassKcbMap, Vector skjhAndTeaVec, Vector curKywzVec, String xzbdm, int presonNum, Vector hbInfoVec, Vector classFixedClassroom, Vector teaskjhmxbhVector, Vector teaypskjhmxbhVector, Vector jsxbkcxx, Vector swVec, Vector xwVec){
		Vector resultVec = new Vector();//返回结果集
		Vector tempVec = new Vector();
		int tempLj = lj;
		String tempTimeOrder = curTimeOrder;
		Vector curTeaCourseVec = new Vector();//当前未排课程信息
		Vector curTeaUsedOrder = new Vector();//当前授课教师已排课的时间序列信息
		String curSkjsbhArray[] = new String[0];//当前授课教师编号
		String curSkzc = "";//当前课程的授课周次
		String curSkzcDetailArray[] = new String[0];//当前课程的授课周次详情
		String curCdyqArray[] = new String[0];//场地要求
		String curSkjhmxbh = "";//授课计划明细编号
		String curSjcdbh = "";//实际场地编号
		String curSjcdmc = "";//实际场地名称
		boolean pkFlag = true;//用于判断当前位置可否排课
		Vector tempBefore = new Vector();
		Vector tempAfter = new Vector();
		Vector siteVec = new Vector();
		int lessonPos = 0;
		int js = 0;//节数
		int sjjs = 0;//实际已排节数
		int lc = 0;//连次
		int sjlc = 0;//实际连次
		Vector curSkzcVec = new Vector();//当前周次详情
		Vector tempSkzcVec = new Vector();
		boolean flag = true;//用于判断是否退出while循环
		boolean hbFlag = false;//用于判断是否合班课程
		boolean complateFlag = false;//用于判断是否完成拼接
		String startNum = "";
		String endNum = "";
		String curStartNum = "";
		String curEndNum = "";
		Vector pjCodeVec = new Vector();//已拼接的授课计划编号
		Map curClassKcbMap = (Map)tempAllClassKcbMap.get(xzbdm);
		Vector tempCourseInfo = new Vector();
		
		//判断授课周次为连续周次的情况下，获取起始周次和结束周次
		if(skzc.indexOf("-") > -1){
			startNum = skzc.substring(0, skzc.indexOf("-"));
			endNum = skzc.substring(skzc.indexOf("-")+1);
		}
		//判断如果是特定周次类型，转换周次格式
		if(skzc.indexOf("-")<0 && (!"odd".equalsIgnoreCase(skzc)&&!"eve".equalsIgnoreCase(skzc))){
			curSkzcVec = this.formatSkzc(skzc, xqzc);
		}
		
		//判断如果授课周次不是从第一周开始的话，需要查询前面的周次是否有课程可以拼接
		if(skzc.indexOf("-")>-1 && !"1".equalsIgnoreCase(startNum)){
			while(flag){
				//遍历查询是否有符合周次拼接要求的未安排课程
				for(int i=0; i<tempAllWpkcVec.size(); i+=2){
					curTeaCourseVec = (Vector)((Vector)tempAllWpkcVec.get(i+1)).clone();
					complateFlag = false;
					
					for(int j=0; j<curTeaCourseVec.size(); j+=16){
						curSkjhmxbh = MyTools.StrFiltr(curTeaCourseVec.get(j));
						curSkjsbhArray = MyTools.StrFiltr(curTeaCourseVec.get(j+9)).split("&");
						curSkzcDetailArray = MyTools.StrFiltr(curTeaCourseVec.get(j+13)).split("&");
						hbFlag = false;
						
						//判断是否是当前课程
						if(!code.equalsIgnoreCase(curSkjhmxbh)){
							//遍历所有合班信息，查询当前课程是否有合班设置*/
							for(int k=0; k<hbInfoVec.size(); k++){
								if(MyTools.StrFiltr(hbInfoVec.get(k)).indexOf(curSkjhmxbh) > -1){
									hbFlag = true;
									break;
								}
							}
							if(hbFlag == true){//判断如果是合班课程，直接放弃
								continue;
							}
							
							//判断是否已拼接过的课程
							if(pjCodeVec.indexOf(curSkjhmxbh) < 0){
								//判断课程是否当前班级的课程
								if(xzbdm.equalsIgnoreCase(MyTools.StrFiltr(curTeaCourseVec.get(j+2)))){
									curSkzc = MyTools.StrFiltr(curTeaCourseVec.get(j+12));
									//判断如果授课周次是整个学期或者授课周次不是连续周次的话跳过本次循环
									if(curSkzc.indexOf("-")<0 || ("1-"+xqzc).equalsIgnoreCase(curSkzc)){
										continue;
									}
									
									//判断是否为连节课程,如果是的话需检查是否还允许连次
									lc = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+7)));
									sjlc = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+8)));
									if(tempLj > 1){
										//判断是否超出允许连次次数
										if(lc!=0 && sjlc>lc){
											continue;
										}
									}
									
									//判断剩余未排节数是否符合要求(剩余节数大于等于当前需要的节数)
									js = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+4)));
									sjjs = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+5)));
									if(js-sjjs < tempLj){
										continue;
									}
									
									//判断两门课程的授课周次是否允许拼接
									curEndNum = String.valueOf(Integer.parseInt(curSkzc.substring(curSkzc.indexOf("-")+1))+1);
									if(startNum.equalsIgnoreCase(curEndNum)){
										//判断连节数优先相同，不同的话从大到小递减
										if(tempLj == MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+6)))){
											tempCourseInfo.clear();
											tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+1)));//课程
											tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+2)));//行政班代码
											tempCourseInfo.add(MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+3))));//总人数
											tempCourseInfo.add(MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+6))));//连节
											tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+9)).split("&"));//授课教师编号
											tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+10)).split("&"));//场地要求
											tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+13)).split("&"));//授课周次详情
											
											//=======================================================================================
											
											/**拼接课程考虑教师跨系部情况*/
											boolean pdTeaKXB = this.teaKXB(curTimeOrder, xqzc, jsxbkcxx, swVec, xwVec, curSkjsbhArray, curSkzcDetailArray);
											if(pdTeaKXB) {
												break;
											}
											//=======================================================================================
											
											/**检查当前课程的教师/教室已用情况是否冲突*/
											tempVec = this.checkCourseUsed(tempCourseInfo, curTimeOrder, xqzc, lessonNum, teaJpMap, teaJpFlag, tsgzVec, 
													tsgzFlag, tempAllTeaUsedOrder, tempAllSiteInfoVec, tempAllClassKcbMap, skjhAndTeaVec, classFixedClassroom, teaskjhmxbhVector, teaypskjhmxbhVector);
											if((Boolean)tempVec.get(0) == false){
												continue;
											}
											curSjcdbh = MyTools.StrFiltr(tempVec.get(1));//实际场地编号
											curSjcdmc = MyTools.StrFiltr(tempVec.get(2));//实际场地名称
											
											//获取拼接课程集合
											tempBefore.add(tempTimeOrder);//时间序列
											tempBefore.add(curSkjhmxbh);//授课计划明细编号
											tempBefore.add(tempLj);//连节
											tempBefore.add(curSjcdbh);//实际场地编号
											tempBefore.add(curSjcdmc);//实际场地名称
											
											complateFlag = true;
											break;
										}
									}
								}
							}
						}
					}
					//判断是否拼接了课程
					if(complateFlag == true){
						pjCodeVec.add(curSkjhmxbh);//添加已拼接课程信息
						this.updateWpkc(tempAllWpkcVec, curSkjhmxbh, curSkjsbhArray, tempLj);//更新未排课程信息
						
						//判断如果可拼接课程连节数等于原课程连节数相等，结束循环
						//如果不同，计算剩余节数，继续查询是否有符合条件的课程
						if(tempLj == lj){
							flag = false;
						}else{
							lj = lj-tempLj;
							lessonPos = MyTools.StringToInt(tempTimeOrder.substring(2))+1;
							tempTimeOrder = tempTimeOrder.substring(0, 2)+(lessonPos<10?"0"+lessonPos:lessonPos);
						}
						break;
					}
				}
				//判断没有完成拼接的话，减少连节数，继续查询符合条件的课程
				if(complateFlag == false){
					tempLj--;
				}
				if(tempLj <= 0){
					flag = false;
				}
			}
		}
		
		flag = true;
		complateFlag = false;
		tempLj = lj;
		tempTimeOrder = curTimeOrder;
		pjCodeVec.clear();
		
		//判断其他方式的拼接课程
		while(flag){
			//遍历查询是否有符合周次拼接要求的未安排课程
			for(int i=0; i<tempAllWpkcVec.size(); i+=2){
				curTeaCourseVec = (Vector)((Vector)tempAllWpkcVec.get(i+1)).clone();
				complateFlag = false;
				
				for(int j=0; j<curTeaCourseVec.size(); j+=16){
					curSkjhmxbh = MyTools.StrFiltr(curTeaCourseVec.get(j));//授课计划明细编号
					curSkjsbhArray = MyTools.StrFiltr(curTeaCourseVec.get(j+9)).split("&");
					curSkzcDetailArray = MyTools.StrFiltr(curTeaCourseVec.get(j+13)).split("&");
					hbFlag = false;
					
					//判断是否是当前课程
					if(!code.equalsIgnoreCase(curSkjhmxbh)){
						//遍历所有合班信息，查询当前课程是否有合班设置*/
						for(int k=0; k<hbInfoVec.size(); k++){
							if(MyTools.StrFiltr(hbInfoVec.get(k)).indexOf(curSkjhmxbh) > -1){
								hbFlag = true;
								break;
							}
						}
						if(hbFlag == true){//判断如果是合班课程，直接放弃
							continue;
						}
						
						//判断是否已拼接过的课程
						if(pjCodeVec.indexOf(curSkjhmxbh) < 0){
							//判断课程是否当前班级的课程
							if(xzbdm.equalsIgnoreCase(MyTools.StrFiltr(curTeaCourseVec.get(j+2)))){
								curSkzc = MyTools.StrFiltr(curTeaCourseVec.get(j+12));
								
								//判断设置的授课周次是不是整个学期，如果是的话跳过本次循环
								if(curSkzc.indexOf("-")<0 && ("1-"+xqzc).equalsIgnoreCase(curSkzc)){
									continue;
								}
								
								//判断是否为连节课程,如果是的话需检查是否还允许连次
								lc = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+7)));
								sjlc = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+8)));
								if(tempLj > 1){
									//判断是否超出允许连次次数
									if(lc!=0 && sjlc>=lc){
										continue;
									}
								}
								
								//判断剩余未排节数是否符合要求(剩余节数大于等于当前需要的节数)
								js = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+4)));
								sjjs = MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+5)));
								if(js-sjjs < tempLj){
									continue;
								}
								
								//判断连节数优先相同，不同的话从大到小递减
								if(tempLj == MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+6)))){
									tempCourseInfo.clear();
									tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+1)));//课程
									tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+2)));//行政班代码
									tempCourseInfo.add(MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+3))));//总人数
									tempCourseInfo.add(MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(j+6))));//连节
									tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+9)).split("&"));//授课教师编号
									tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+10)).split("&"));//场地要求
									tempCourseInfo.add(MyTools.StrFiltr(curTeaCourseVec.get(j+13)).split("&"));//授课周次详情
									
									//判断设置的授课周次类型是否相同，相同的情况下检查是否可以拼接
									//连续周次类型
									if(skzc.indexOf("-")>-1 && curSkzc.indexOf("-")>-1){
										//检查是否有后面的可拼接课程
										if(!xqzc.equalsIgnoreCase(endNum)){
											//判断两门课程的授课周次是否允许拼接
											curStartNum = String.valueOf(Integer.parseInt(curSkzc.substring(0, curSkzc.indexOf("-")))-1);
											if(endNum.equalsIgnoreCase(curStartNum)){
												//=======================================================================================
												
												/**拼接课程考虑教师跨系部情况*/
												boolean pdTeaKXB = this.teaKXB(curTimeOrder, xqzc, jsxbkcxx, swVec, xwVec, curSkjsbhArray, curSkzcDetailArray);
												if(pdTeaKXB) {
													break;
												}
												//=======================================================================================
												
												/**检查当前课程的教师/教室已用情况是否冲突*/
												tempVec = this.checkCourseUsed(tempCourseInfo, curTimeOrder, xqzc, lessonNum, teaJpMap, teaJpFlag, tsgzVec, 
														tsgzFlag, tempAllTeaUsedOrder, tempAllSiteInfoVec, tempAllClassKcbMap, skjhAndTeaVec, classFixedClassroom, teaskjhmxbhVector, teaypskjhmxbhVector);
												if((Boolean)tempVec.get(0) == false){
													continue;
												}
												curSjcdbh = MyTools.StrFiltr(tempVec.get(1));//实际场地编号
												curSjcdmc = MyTools.StrFiltr(tempVec.get(2));//实际场地名称
												
												tempAfter.add(tempTimeOrder);//时间序列
												tempAfter.add(curSkjhmxbh);//授课计划明细编号
												tempAfter.add(tempLj);//连节
												tempAfter.add(curSjcdbh);//实际场地编号
												tempAfter.add(curSjcdmc);//实际场地名称
												complateFlag = true;
												break;
											}
										}
									}
									//单双周类型
									else if(("odd".equalsIgnoreCase(skzc)&&"even".equalsIgnoreCase(curSkzc)) || ("odd".equalsIgnoreCase(curSkzc)&&"even".equalsIgnoreCase(skzc))){
										
										//=======================================================================================
										
										/**拼接课程考虑教师跨系部情况*/
										boolean pdTeaKXB = this.teaKXB(curTimeOrder, xqzc, jsxbkcxx, swVec, xwVec, curSkjsbhArray, curSkzcDetailArray);
										if(pdTeaKXB) {
											break;
										}
										//=======================================================================================
										
										/**检查当前课程的教师/教室已用情况是否冲突*/ //2017-7-1
										tempVec = this.checkCourseUsed(tempCourseInfo, curTimeOrder, xqzc, lessonNum, teaJpMap, teaJpFlag, tsgzVec, 
												tsgzFlag, tempAllTeaUsedOrder, tempAllSiteInfoVec, tempAllClassKcbMap, skjhAndTeaVec, classFixedClassroom, teaskjhmxbhVector, teaypskjhmxbhVector);
										if((Boolean)tempVec.get(0) == false){
											continue;
										}
										curSjcdbh = MyTools.StrFiltr(tempVec.get(1));//实际场地编号
										curSjcdmc = MyTools.StrFiltr(tempVec.get(2));//实际场地名称
										
										if("odd".equalsIgnoreCase(skzc) && "even".equalsIgnoreCase(curSkzc)){
											tempAfter.add(tempTimeOrder);//时间序列
											tempAfter.add(curSkjhmxbh);//授课计划明细编号
											tempAfter.add(tempLj);//连节
											tempAfter.add(curSjcdbh);//实际场地编号
											tempAfter.add(curSjcdmc);//实际场地名称
										}else{
											tempBefore.add(tempTimeOrder);
											tempBefore.add(curSkjhmxbh);
											tempBefore.add(tempLj);
											tempBefore.add(curSjcdbh);
											tempBefore.add(curSjcdmc);
										}
										
										complateFlag = true;
										break;
									}
									//特定周次类型
									else if(skzc.indexOf("#")>-1 && curSkzc.indexOf("#")>-1){
										tempSkzcVec = this.formatSkzc(curSkzc, xqzc);
										
										//判断周次是否冲突
										if(this.judgeSkzc(curSkzcVec, tempSkzcVec) == false){
											
											//=======================================================================================
											
											/**拼接课程考虑教师跨系部情况*/
											boolean pdTeaKXB = this.teaKXB(curTimeOrder, xqzc, jsxbkcxx, swVec, xwVec, curSkjsbhArray, curSkzcDetailArray);
											if(pdTeaKXB) {
												break;
											}
											//=======================================================================================
											
											/**检查当前课程的教师/教室已用情况是否冲突*/
											tempVec = this.checkCourseUsed(tempCourseInfo, curTimeOrder, xqzc, lessonNum, teaJpMap, teaJpFlag, tsgzVec, 
													tsgzFlag, tempAllTeaUsedOrder, tempAllSiteInfoVec, tempAllClassKcbMap, skjhAndTeaVec, classFixedClassroom, teaskjhmxbhVector, teaskjhmxbhVector);
											if((Boolean)tempVec.get(0) == false){
												continue;
											}
											curSjcdbh = MyTools.StrFiltr(tempVec.get(1));//实际场地编号
											curSjcdmc = MyTools.StrFiltr(tempVec.get(2));//实际场地名称
											
											//判断拼接课程的前后
											if(MyTools.StringToInt(MyTools.StrFiltr(curSkzcVec.get(0))) > MyTools.StringToInt(MyTools.StrFiltr(tempSkzcVec.get(0)))){
												tempBefore.add(tempTimeOrder);//时间序列
												tempBefore.add(curSkjhmxbh);//授课计划明细编号
												tempBefore.add(tempLj);//连节
												tempBefore.add(curSjcdbh);//实际场地编号
												tempBefore.add(curSjcdmc);//实际场地名称
											}else{
												tempAfter.add(tempTimeOrder);
												tempAfter.add(curSkjhmxbh);
												tempAfter.add(tempLj);
												tempAfter.add(curSjcdbh);
												tempAfter.add(curSjcdmc);
											}
											
											complateFlag = true;
											break;
										}
									}
								}
							}
						}
					}
				}
				//判断已完成拼接课程
				if(complateFlag == true){
					pjCodeVec.add(curSkjhmxbh);//添加已拼接课程信息
					this.updateWpkc(tempAllWpkcVec, curSkjhmxbh, curSkjsbhArray, tempLj);//更新未排课程信息
					
					//判断如果可拼接课程连节数等于原课程连节数相等，结束循环
					//如果不同，计算剩余节数，继续查询是否有符合条件的课程
					if(tempLj == lj){
						flag = false;
					}else{
						lj = lj-tempLj;
						lessonPos = MyTools.StringToInt(tempTimeOrder.substring(2))+1;
						tempTimeOrder = tempTimeOrder.substring(0, 2)+(lessonPos<10?"0"+lessonPos:lessonPos);
					}
					break;
				}
			}
			//判断没有完成拼接的话，减少连节数，继续查询符合条件的课程
			if(complateFlag == false){
				//递减连节数，如为0退出循环
				tempLj--;
			}
			if(tempLj <= 0){
				flag = false;
			}
		}
		
		//整理拼接的集合
		String orderPos = "";
		String skjhmxbh = "";
		int ljNum = 0;
		int tempNum = 0;
		String tempClassCode = "";
		String tempOrder = "";
		Vector beforePjkc = new Vector();
		Vector afterPjkc = new Vector();
		
		for(int i=0; i<tempBefore.size(); i+=5){
			orderPos = MyTools.StrFiltr(tempBefore.get(i));
			skjhmxbh = MyTools.StrFiltr(tempBefore.get(i+1));
			ljNum = MyTools.StringToInt(MyTools.StrFiltr(tempBefore.get(i+2)));

			tempNum = MyTools.StringToInt(orderPos.substring(2));
			for(int j=0; j<ljNum; j++){
				orderPos = orderPos.substring(0, 2) + ((tempNum+j)<10?"0"+(tempNum+j):(tempNum+j));
				beforePjkc.add(orderPos);
				beforePjkc.add(skjhmxbh);
				beforePjkc.add(MyTools.StrFiltr(tempBefore.get(i+3)));
				beforePjkc.add(MyTools.StrFiltr(tempBefore.get(i+4)));
			}
		}
		
		for(int i=0; i<tempAfter.size(); i+=5){
			orderPos = MyTools.StrFiltr(tempAfter.get(i));
			skjhmxbh = MyTools.StrFiltr(tempAfter.get(i+1));
			ljNum = MyTools.StringToInt(MyTools.StrFiltr(tempAfter.get(i+2)));
			
			
			tempNum = MyTools.StringToInt(orderPos.substring(2));
			for(int j=0; j<ljNum; j++){
				orderPos = orderPos.substring(0, 2) + ((tempNum+j)<10?"0"+(tempNum+j):(tempNum+j));
				afterPjkc.add(orderPos);
				afterPjkc.add(skjhmxbh);
				afterPjkc.add(MyTools.StrFiltr(tempAfter.get(i+3)));
				afterPjkc.add(MyTools.StrFiltr(tempAfter.get(i+4)));
			}
		}
		
		resultVec.add(beforePjkc);
		resultVec.add(afterPjkc);
		
		return resultVec;
	}
	
	/**
	 * 更新班级课程表信息
	 * @date:2015-09-11
	 * @author:yeq
	 * @param xzbdm 行政班代码
	 * @param timeOrderArray 所有需要更新的时间序列
	 * @param curSkjhmxbh 授课计划明细编号
	 * @param curSjcdbh 实际场地编号
	 * @param curSjcdmc 实际场地名称
	 * @param beforePjkc 拼接课程
	 * @param afterPjkc
	 * @param allClassKcbMap 所有班级课表
	 * @param pkType 排课类型
	 * @param updateClassKcbMap 更新的课表内容
	 */
	public void updateClassKcb(String xzbdm, String[] timeOrderArray, String curSkjhmxbh, String curSjcdbh, String curSjcdmc, Vector beforePjkc, Vector afterPjkc, Map allClassKcbMap, String pkType, Map updateClassKcbMap){
		String skjhmxbh = "";//授课计划明细编号
		String sjcdbh = "";//实际场地编号
		String sjcdmc = "";//实际场地名称
		String tempSkjhmxbh = curSkjhmxbh;
		String tempSjcdbh = curSjcdbh;
		String tempSjcdmc = curSjcdmc;
		Vector orderInfo = null;
		Map curClassKcbMap = (Map)allClassKcbMap.get(xzbdm);
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		
		//完成课表更新
		for(int i=0; i<timeOrderArray.length; i++){
			skjhmxbh = tempSkjhmxbh;
			sjcdbh = tempSjcdbh;
			sjcdmc = tempSjcdmc;
			
			//获取周次拼接的授课计划编号信息
			if(beforePjkc.indexOf(timeOrderArray[i]) > -1){
				skjhmxbh = MyTools.StrFiltr(beforePjkc.get(beforePjkc.indexOf(timeOrderArray[i])+1))+"｜"+skjhmxbh;
				sjcdbh = MyTools.StrFiltr(beforePjkc.get(beforePjkc.indexOf(timeOrderArray[i])+2))+"｜"+sjcdbh;
				sjcdmc = MyTools.StrFiltr(beforePjkc.get(beforePjkc.indexOf(timeOrderArray[i])+3))+"｜"+sjcdmc;
			}
			if(afterPjkc.indexOf(timeOrderArray[i]) > -1){
				skjhmxbh = skjhmxbh+"｜"+MyTools.StrFiltr(afterPjkc.get(afterPjkc.indexOf(timeOrderArray[i])+1));
				sjcdbh = sjcdbh+"｜"+MyTools.StrFiltr(afterPjkc.get(afterPjkc.indexOf(timeOrderArray[i])+2));
				sjcdmc = sjcdmc+"｜"+MyTools.StrFiltr(afterPjkc.get(afterPjkc.indexOf(timeOrderArray[i])+3));
			}
			
			String connOrder = "";//连节相关时间序列
			//获取相关连节时间序列
			for(int j=0; j<timeOrderArray.length; j++){
				if(!timeOrderArray[j].equalsIgnoreCase(timeOrderArray[i])){
					connOrder += timeOrderArray[j]+",";
				}
			}
			if(connOrder.length() > 0){
				connOrder = connOrder.substring(0, connOrder.length()-1);
			}
			orderInfo = new Vector();
			orderInfo.add("4");//状态：1优先/2固排/3禁排/4随机排
			orderInfo.add(skjhmxbh);//授课计划编号
			orderInfo.add(sjcdbh);//实际场地编号
			orderInfo.add(sjcdmc);//实际场地名称
			orderInfo.add(connOrder);//连节相关时间序列
			curClassKcbMap.put(timeOrderArray[i], orderInfo);
			
//			if(!"1".equalsIgnoreCase(pkType) || this.getAuth().indexOf(majorTeacher)>-1){
			if(!"1".equalsIgnoreCase(pkType)){
				Map curUpdateClassKcbMap = new HashMap();
				
				if(updateClassKcbMap.containsKey(xzbdm)){
					curUpdateClassKcbMap = (Map)updateClassKcbMap.get(xzbdm);
					curUpdateClassKcbMap.put(timeOrderArray[i], orderInfo);
				}else{
					curUpdateClassKcbMap.put(timeOrderArray[i], orderInfo);
					updateClassKcbMap.put(xzbdm, curUpdateClassKcbMap);
				}
			}
		}
	}
	
	/**
	 * 更新班级可用位置信息
	 * @date:2015-09-11
	 * @author:yeq
	 * @param allKywzMap 所有班级可排位置信息
	 * @param xzbdm 行政班代码
	 * @param timeOrderArray 所有需要更新的时间序列
	 */
	public void updateKywz(Map allKywzMap, String xzbdm, String[] timeOrderArray){
		Vector curKywzVec = (Vector)allKywzMap.get(xzbdm);
		for(int i=0; i<timeOrderArray.length; i++){
			curKywzVec.remove(timeOrderArray[i]);//移除此位置
		}
	}
	
	/**
	 * 更新未排课程信息
	 * @date:2015-07-31
	 * @author:yeq
	 * @param allWpkcVec 未排课程信息
	 * @param skjhmxbh 授课计划明细编号
	 * @param teaCodeArray 教师编号
	 * @param ljNum 节数
	 * @return boolean
	 */
	public void updateWpkc(Vector tempAllWpkcVec, String skjhmxbh, String[] teaCodeArray, int ljNum){
		String tempTeaCodeArray[] = new String[0];
		Vector curTeaCourseVec = new Vector();
		Vector tempTeaVec = new Vector();
		
		for(int i=0; i<teaCodeArray.length; i++){
			tempTeaCodeArray = teaCodeArray[i].split("\\+");
			
			for(int j=0; j<tempTeaCodeArray.length; j++){
				//判断当前教师的是否已更新过未排课程
				if(tempTeaVec.indexOf(MyTools.StrFiltr(tempTeaCodeArray[j])) < 0){
					tempTeaVec.add(tempTeaCodeArray[j]);
					curTeaCourseVec = (Vector)((Vector)tempAllWpkcVec.get(tempAllWpkcVec.indexOf(tempTeaCodeArray[j])+1)).clone();
					
					for(int k=0; k<curTeaCourseVec.size(); k+=16){
						if(skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(curTeaCourseVec.get(k)))){
							//更新实际排课节数
							curTeaCourseVec.set(k+5, MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(k+5))) + ljNum);
							if(ljNum > 1){//判断如果是连节课程，更新实际连次次数
								curTeaCourseVec.set(k+8, MyTools.StringToInt(MyTools.StrFiltr(curTeaCourseVec.get(k+8)))+1);
							}
							break;
						}
					}
					//更新未排课程数据
					tempAllWpkcVec.set(tempAllWpkcVec.indexOf(tempTeaCodeArray[j])+1, curTeaCourseVec);
				}
			}
		}
	}
	
	/**
	 * 将所有课程表信息插入数据库
	 * @date:2015-06-01
	 * @param dayNum 每周天数
	 * @param lessonNum 每天节数
	 * @param allClassKcbMap 所有班级课程表
	 * @param allWpkcVec 未排课程最新信息
	 * @param pkType 排课类型
	 * @param updateKcbMap 更新的课表信息
	 * @author:yeq
	 * @throws SQLException
	 */
	public boolean updateDataToDB(int dayNum, int lessonNum, Map allClassKcbMap, Vector allWpkcVec, String pkType, Map updateClassKcbMap) throws SQLException{
		String sql = "";
		Vector vec = null;
		Vector tempVec = new Vector();
		Vector sqlVec = new Vector();
		boolean flag = true;
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		//String majorTeacher = MyTools.getProp(request, "Base.majorTeacher");//专业课
		int mainNum = 10000001;
		long detailNum = 1000000001L;
		
		//判断是否为公共课全新排课
//		if(this.getAuth().indexOf(pubTeacher)>-1 && "1".equalsIgnoreCase(pkType)){
		if("1".equalsIgnoreCase(pkType)){
			//获取最大课程表主编号和课程表明细编号
			sql = "select isnull((select top 1 cast(substring(课程表主表编号,5,len(课程表主表编号)) as int)+1 from V_排课管理_课程表主表 where 状态='1' order by cast(substring(课程表主表编号, 5, len(课程表主表编号)) as int) desc), '10000001') as 最大课程表主表编号," +
				"isnull((select top 1 cast(substring(课程表明细编号,7,len(课程表明细编号)) as bigint)+1 from V_排课管理_课程表明细表 where 状态='1' order by cast(substring(课程表明细编号, 7, len(课程表明细编号)) as int) desc), '1000000001') as 最大课程表明细编号";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				mainNum = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0))); //课程表主表编号
				detailNum = MyTools.StringToLong(MyTools.StrFiltr(vec.get(1))); //课程表明细编号
			}
		}
		
		//判断是公共课首次排课或者课表有更新内容
		if("1".equalsIgnoreCase(pkType) || updateClassKcbMap.size()>0){
			//更新授课计划明细中的实际排课数量和实际连节次数
			for(int i=0; i<allWpkcVec.size(); i+=2){
				vec = (Vector)allWpkcVec.get(i+1);
				for(int j=0; j<vec.size(); j+=16){
					sql = "update V_规则管理_授课计划明细表 set " +
						"实际已排节数='" + MyTools.fixSql(MyTools.StrFiltr(vec.get(j+5))) + "'," +
						"实际连次次数='" + MyTools.fixSql(MyTools.StrFiltr(vec.get(j+8))) + "' " +
						"where 授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(vec.get(j))) + "'";
					sqlVec.add(sql);
				}
			}
		}
		
		//判断如果不是公共课全新排课
		if(!"1".equalsIgnoreCase(pkType) && updateClassKcbMap.size()>0){
			allClassKcbMap.clear();
			allClassKcbMap.putAll(updateClassKcbMap);
		}
		
		//更新所有课程表数据
		String classCode = "";
		Map tempMap = new HashMap();
		String tempDayNum = "";
		String tempLesNum = "";
		Set keysSet = allClassKcbMap.keySet();
		Iterator iterator = keysSet.iterator();
		while(iterator.hasNext()) {
			classCode = (String)iterator.next();//key
			
			//判断权限，公共课添加课程表信息，专业课更新课程表信息
			if("1".equalsIgnoreCase(pkType)){
//				if(this.getAuth().indexOf(pubTeacher) > -1){
					sql = "insert into V_排课管理_课程表主表 (课程表主表编号,学年学期编码,行政班代码,提交状态,创建人,状态) values(" +
						"'" + MyTools.fixSql("kcb_" + mainNum) + "'," +
						"'" + MyTools.fixSql(this.getPK_XNXQBM()) + "'," +
						"'" + MyTools.fixSql(classCode) + "'," +
						"'0',"+
						"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
						"'1')";
					sqlVec.add(sql);
//				}else if(this.getAuth().indexOf(majorTeacher) > -1){
//					sql = "update V_排课管理_课程表主表 set 提交状态='2' " +
//						"where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//						"and 行政班代码='" + MyTools.fixSql(classCode) + "'";
//					sqlVec.add(sql);
//				}
			}
			
			//判断如果不是公共课首次排课的时候，是否有更新课程表
			if(!"1".equalsIgnoreCase(pkType) && updateClassKcbMap.size()==0){
				continue;
			}
			
			tempMap = (HashMap)allClassKcbMap.get(classCode);//value
			
			for(int a=1; a<dayNum+1; a++){
				if(a < 10){
					tempDayNum = "0" + a;
				}else{
					tempDayNum = "" + a;
				}
				for(int b=1; b<lessonNum+1; b++){
					if(b < 10){
						tempLesNum = "0" + b;
					}else{
						tempLesNum = "" + b;
					}
					
					if(tempMap.containsKey(tempDayNum+tempLesNum)){
						tempVec = (Vector)tempMap.get(tempDayNum+tempLesNum);
						for(int c=0; c<tempVec.size(); c+=5){
							//判断权限，公共课添加课程表信息，专业课更新课程表信息
//							if(this.getAuth().indexOf(pubTeacher)>-1){
								if("1".equalsIgnoreCase(pkType)){
									sql = "insert into V_排课管理_课程表明细表 (课程表明细编号,课程表主表编号,班级排课状态,时间序列,连节相关编号,授课计划明细编号,实际场地编号,实际场地名称,创建人,状态) values(" +
										"'" + MyTools.fixSql("kcbmx_" + detailNum) + "'," +
										"'" + MyTools.fixSql("kcb_" + mainNum) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c))) + "'," +
										"'" + MyTools.fixSql(tempDayNum+tempLesNum)  +"'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+4))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+1))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+2))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+3))) + "'," +
										"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
										"'1')";
									sqlVec.add(sql);
									
//									sql = "insert into V_排课管理_公共课课程表明细表 (课程表明细编号,课程表主表编号,班级排课状态,时间序列,连节相关编号,授课计划明细编号,实际场地编号,实际场地名称,创建人,状态) values(" +
//										"'" + MyTools.fixSql("kcbmx_" + detailNum) + "'," +
//										"'" + MyTools.fixSql("kcb_" + mainNum) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c))) + "'," +
//										"'" + MyTools.fixSql(tempDayNum+tempLesNum) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+4))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+1))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+2))) + "'," +
//										"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+3))) + "'," +
//										"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
//										"'1')";
//									sqlVec.add(sql);
								}else{
									sql = "update V_排课管理_课程表明细表 set " +
										"班级排课状态='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c))) + "'," +
										"连节相关编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+4))) + "'," +
										"授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+1))) + "'," +
										"实际场地编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+2))) + "'," +
										"实际场地名称='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+3))) + "' " +
										"where 课程表明细编号=(select a.课程表明细编号 from V_排课管理_课程表明细表 a " +
										"left join V_排课管理_课程表主表 b on a.课程表主表编号=b.课程表主表编号 " +
										"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
										"and b.行政班代码='" + MyTools.fixSql(classCode) + "' " +
										"and a.时间序列='" + MyTools.fixSql(tempDayNum+tempLesNum) + "')";
									sqlVec.add(sql);
									
//									sql = "update V_排课管理_公共课课程表明细表 set " +
//										"班级排课状态='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c))) + "'," +
//										"连节相关编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+4))) + "'," +
//										"授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+1))) + "'," +
//										"实际场地编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+2))) + "'," +
//										"实际场地名称='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+3))) + "' " +
//										"where 课程表明细编号=(select a.课程表明细编号 from V_排课管理_课程表明细表 a " +
//										"left join V_排课管理_课程表主表 b on a.课程表主表编号=b.课程表主表编号 " +
//										"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//										"and b.行政班代码='" + MyTools.fixSql(classCode) + "' " +
//										"and a.时间序列='" + MyTools.fixSql(tempDayNum+tempLesNum) + "')";
//									sqlVec.add(sql);
								}
//							}else if(this.getAuth().indexOf(majorTeacher) > -1){
//								sql = "update V_排课管理_课程表明细表 set " +
//									"班级排课状态='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c))) + "'," +
//									"连节相关编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+4))) + "'," +
//									"授课计划明细编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+1))) + "'," +
//									"实际场地编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+2))) + "'," +
//									"实际场地名称='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(c+3))) + "' " +
//									"where 课程表明细编号=(select a.课程表明细编号 from V_排课管理_课程表明细表 a " +
//									"left join V_排课管理_课程表主表 b on a.课程表主表编号=b.课程表主表编号 " +
//									"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//									"and b.行政班代码='" + MyTools.fixSql(classCode) + "' " +
//									"and a.时间序列='" + MyTools.fixSql(tempDayNum+tempLesNum) + "')";
//								sqlVec.add(sql);
//							}
						}
						
						detailNum++;
					}
				}
			}
			
			mainNum++;
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			sqlVec.clear();
			
			//判断是否是公共课首次排课或者有更新课程表
			if("1".equalsIgnoreCase(pkType) || updateClassKcbMap.size()>0){
//				sql = "delete from V_排课管理_课程表明细详情表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
				sql = "select * into #tempKcb from PKGL_KCBMXXQ where PK_XNXQBM<>'" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
				sqlVec.add(sql);
				sql = "truncate table PKGL_KCBMXXQ";
				sqlVec.add(sql);
				sql = "insert into PKGL_KCBMXXQ select * from #tempKcb";
				sqlVec.add(sql);
				sql = "drop table #tempKcb";
				sqlVec.add(sql);
				
//				数据增加后，执行速度过慢，故更改为程序拼接课程信息
//				sql = "insert into V_排课管理_课程表明细详情表 select * from V_排课管理_课程表明细详情临时表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
//				sqlVec.add(sql);
				//查询本学期所有课表信息
//				sql = "select a.课程表明细编号,b.课程表主表编号,b.学年学期编码,c.行政班代码,c.行政班名称,d.专业代码,d.专业名称,a.班级排课状态,a.时间序列,a.连节相关编号," +
//					"e.授课计划明细编号,e.课程代码,e.课程名称,e.课程类型,e.授课教师编号,e.授课教师姓名,e.场地要求,e.场地名称,a.实际场地编号,a.实际场地名称,e.授课周次,e.授课周次详情,a.状态 " +
//					"from V_排课管理_课程表明细表 a left join V_排课管理_课程表主表 b on b.课程表主表编号=a.课程表主表编号 " +
//					"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
//					"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
//					"left join V_规则管理_授课计划明细表 e on a.授课计划明细编号 like '%'+e.授课计划明细编号+'%' " +
//					"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' " +
//					"order by a.课程表主表编号,a.时间序列,cast((case substring(e.授课周次详情, 2, 1) " +
//					"when '#' then substring(e.授课周次详情, 1, 1) when '-' then substring(e.授课周次详情, 1, 1) " +
//					"when '&' then substring(e.授课周次详情, 1, 1) when 'd' then '1' when 'v' then '2' else substring(e.授课周次详情, 1, 2) end) as int)";
//				sql = "with cte1 as(" +
//					"select a.课程表明细编号,a.课程表主表编号,b.学年学期编码,b.行政班代码,c.行政班名称,d.专业代码,d.专业名称," +
//					"a.班级排课状态,a.时间序列,a.连节相关编号,a.授课计划明细编号,a.实际场地编号,a.实际场地名称,a.状态 from V_排课管理_课程表明细表 a " +
//					"left join V_排课管理_课程表主表 b on b.课程表主表编号=a.课程表主表编号 " +
//					"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
//					"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
//					"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')," +
//					"cte2 as(" +
//					"select a.授课计划明细编号,a.课程代码,a.课程名称,a.课程类型,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情 " +
//					"from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
//					"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') " +
//					"select a.课程表明细编号,a.课程表主表编号,a.学年学期编码,a.行政班代码,a.行政班名称,a.专业代码,a.专业名称,a.班级排课状态,a.时间序列,a.连节相关编号," +
//					"e.授课计划明细编号,e.课程代码,e.课程名称,e.课程类型,e.授课教师编号,e.授课教师姓名,e.场地要求,e.场地名称,a.实际场地编号,a.实际场地名称,e.授课周次,e.授课周次详情,a.状态 " +
//					"from cte1 a " +
//					"left join cte2 e on a.授课计划明细编号 like '%'+e.授课计划明细编号+'%' " +
//					"order by a.课程表主表编号,a.时间序列,cast((case substring(e.授课周次详情, 2, 1) " +
//					"when '#' then substring(e.授课周次详情, 1, 1) when '-' then substring(e.授课周次详情, 1, 1) " +
//					"when '&' then substring(e.授课周次详情, 1, 1) when 'd' then '1' when 'v' then '2' else substring(e.授课周次详情, 1, 2) end) as int)";
				sql = "with cte1 as(" +
					"select a.课程表明细编号,a.课程表主表编号,b.学年学期编码,b.行政班代码,c.班级名称,d.专业代码,d.专业名称," +
					"a.班级排课状态,a.时间序列,a.连节相关编号,a.授课计划明细编号," +
					"case when a.授课计划明细编号=e.授课计划明细编号 then isnull(e.授课教师编号,'') else '' end as 授课教师编号," +
					"case when a.授课计划明细编号=e.授课计划明细编号 then isnull(e.授课教师姓名,'') else '' end as 授课教师姓名," +
					"a.实际场地编号,a.实际场地名称," +
					"case when a.授课计划明细编号=e.授课计划明细编号 then isnull(e.授课周次,'') else '' end as 授课周次," +
					"case when a.授课计划明细编号=e.授课计划明细编号 then isnull(e.授课周次详情,'') else '' end as 授课周次详情,a.状态 " +
					"from V_排课管理_课程表明细表 a " +
					"left join V_排课管理_课程表主表 b on b.课程表主表编号=a.课程表主表编号 " +
					//"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
					"left join V_基础信息_班级信息表 c on c.班级编号=b.行政班代码 " +
					"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
					"left join V_排课管理_课程表明细详情表 e on e.课程表明细编号=a.课程表明细编号 " +
					"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')," +
					"cte2 as(" +
					"select a.授课计划明细编号,a.课程代码,a.课程名称,a.课程类型,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,a.授课周次详情 " +
					"from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
					"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "') " +
					"select a.课程表明细编号,a.课程表主表编号,a.学年学期编码,a.行政班代码,a.班级名称,a.专业代码,a.专业名称,a.班级排课状态,a.时间序列,a.连节相关编号," +
					"b.授课计划明细编号,b.课程代码,b.课程名称,b.课程类型," +
					"case when a.授课教师编号<>'' then dbo.Get_TeaCode(a.授课计划明细编号,a.授课教师编号,b.授课计划明细编号,'｜') else b.授课教师编号 end as 授课教师编号," +
					"case when a.授课教师姓名<>'' then dbo.Get_TeaCode(a.授课计划明细编号,a.授课教师姓名,b.授课计划明细编号,'｜') else b.授课教师姓名 end as 授课教师姓名," +
					"b.场地要求,b.场地名称," +
					"dbo.Get_TeaCode(a.授课计划明细编号,a.实际场地编号,b.授课计划明细编号,'｜') as 实际场地编号," +
					"dbo.Get_TeaCode(a.授课计划明细编号,a.实际场地名称,b.授课计划明细编号,'｜') as 实际场地名称," +
					"case when a.授课周次<>'' then dbo.Get_TeaCode(a.授课计划明细编号,a.授课周次,b.授课计划明细编号,'｜') else b.授课周次 end as 授课周次," +
					"case when a.授课周次详情<>'' then dbo.Get_TeaCode(a.授课计划明细编号,a.授课周次详情,b.授课计划明细编号,'｜') else b.授课周次详情 end as 授课周次详情,a.状态 " +
					"from cte1 a " +
					"left join cte2 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
					"order by a.课程表主表编号,a.时间序列," +
					"cast((case substring(dbo.Get_TeaCode(a.授课计划明细编号,a.授课周次详情,b.授课计划明细编号,'｜'), 2, 1) " +
					"when '#' then substring(dbo.Get_TeaCode(a.授课计划明细编号,a.授课周次详情,b.授课计划明细编号,'｜'), 1, 1) " +
					"when '-' then substring(dbo.Get_TeaCode(a.授课计划明细编号,a.授课周次详情,b.授课计划明细编号,'｜'), 1, 1) " +
					"when '&' then substring(dbo.Get_TeaCode(a.授课计划明细编号,a.授课周次详情,b.授课计划明细编号,'｜'), 1, 1) " +
					"when 'd' then '1' when 'v' then '2' " +
					"else substring(dbo.Get_TeaCode(a.授课计划明细编号,a.授课周次详情,b.授课计划明细编号,'｜'), 1, 2) end) as int)";
					

				vec = db.GetContextVector(sql);
				
				tempVec = new Vector();
				String kcbmxbh = "";
				String kcbzbbh = "";
				String xnxqbm = "";
				String xzbdm = "";
				String xzbmc = "";
				String zydm = "";
				String zymc = "";
				String bjpkzt = "";
				String sjxl = "";
				String ljxgbh = "";
				String skjhmxbh = "";
				String kcbh  = "";
				String kcmc = "";
				String kclx = "";
				String skjsbh = "";
				String skjsxm = "";
				String cdyq = "";
				String cdmc = "";
				String sjcdbh = "";
				String sjcdmc = "";
				String skzc  = "";
				String skzcxq = "";
				String zt = "";
				String tempSkjhmxbh = "";
				String tempKcbh = "";
				String tempKcmc = "";
				String tempKclx = "";
				String tempSkjsbh = "";
				String tempSkjsxm = "";
				String tempCdyq = "";
				String tempCdmc = "";
				String tempSjcdbh = "";
				String tempSjcdmc = "";
				String tempSkzc = "";
				String tempSkzcxq = "";
				
				for(int i=0; i<vec.size(); i+=23){
					skjhmxbh = MyTools.StrFiltr(vec.get(i+10));
					kcbh = MyTools.StrFiltr(vec.get(i+11));
					kcmc = MyTools.StrFiltr(vec.get(i+12));
					kclx = MyTools.StrFiltr(vec.get(i+13));
					skjsbh = MyTools.StrFiltr(vec.get(i+14));
					skjsxm = MyTools.StrFiltr(vec.get(i+15));
					cdyq = MyTools.StrFiltr(vec.get(i+16));
					cdmc = MyTools.StrFiltr(vec.get(i+17));
					sjcdbh = MyTools.StrFiltr(vec.get(i+18));
					sjcdmc = MyTools.StrFiltr(vec.get(i+19));
					skzc = MyTools.StrFiltr(vec.get(i+20));
					skzcxq = MyTools.StrFiltr(vec.get(i+21));
					
					if(!kcbmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
						if(i > 0){
							tempVec.add(kcbmxbh);
							tempVec.add(kcbzbbh);
							tempVec.add(xnxqbm);
							tempVec.add(xzbdm);
							tempVec.add(xzbmc);
							tempVec.add(zydm);
							tempVec.add(zymc);
							tempVec.add(bjpkzt);
							tempVec.add(sjxl);
							tempVec.add(ljxgbh);
							tempVec.add(tempSkjhmxbh);
							tempVec.add(tempKcbh);
							tempVec.add(tempKcmc);
							tempVec.add(tempKclx);
							tempVec.add(tempSkjsbh);
							tempVec.add(tempSkjsxm);
							tempVec.add(tempCdyq);
							tempVec.add(tempCdmc);
							tempVec.add(tempSjcdbh);
							tempVec.add(tempSjcdmc);
							tempVec.add(tempSkzc);
							tempVec.add(tempSkzcxq);
							tempVec.add(zt);
						}
						
						kcbmxbh = MyTools.StrFiltr(vec.get(i));
						kcbzbbh = MyTools.StrFiltr(vec.get(i+1));
						xnxqbm = MyTools.StrFiltr(vec.get(i+2));
						xzbdm = MyTools.StrFiltr(vec.get(i+3));
						xzbmc = MyTools.StrFiltr(vec.get(i+4));
						zydm = MyTools.StrFiltr(vec.get(i+5));
						zymc = MyTools.StrFiltr(vec.get(i+6));
						bjpkzt = MyTools.StrFiltr(vec.get(i+7));
						sjxl = MyTools.StrFiltr(vec.get(i+8));
						ljxgbh = MyTools.StrFiltr(vec.get(i+9));
						zt = MyTools.StrFiltr(vec.get(i+22));
						tempSkjhmxbh = skjhmxbh;
						tempKcbh = kcbh;
						tempKcmc = kcmc;
						tempKclx = kclx;
						tempSkjsbh = skjsbh;
						tempSkjsxm = skjsxm;
						tempCdyq = cdyq;
						tempCdmc = cdmc;
						tempSjcdbh = sjcdbh;
						tempSjcdmc = sjcdmc;
						tempSkzc = skzc;
						tempSkzcxq = skzcxq;
					}else{
						tempSkjhmxbh += "｜"+skjhmxbh;
						tempKcbh += "｜"+kcbh;
						tempKcmc += "｜"+kcmc;
						tempKclx += "｜"+kclx;
						tempSkjsbh += "｜"+skjsbh;
						tempSkjsxm += "｜"+skjsxm;
						tempCdyq += "｜"+cdyq;
						tempCdmc += "｜"+cdmc;
						tempSjcdbh += "｜"+sjcdbh;
						tempSjcdmc += "｜"+sjcdmc;
						tempSkzc += "｜"+skzc;
						tempSkzcxq += "｜"+skzcxq;
					}
				}
				tempVec.add(kcbmxbh);
				tempVec.add(kcbzbbh);
				tempVec.add(xnxqbm);
				tempVec.add(xzbdm);
				tempVec.add(xzbmc);
				tempVec.add(zydm);
				tempVec.add(zymc);
				tempVec.add(bjpkzt);
				tempVec.add(sjxl);
				tempVec.add(ljxgbh);
				tempVec.add(tempSkjhmxbh);
				tempVec.add(tempKcbh);
				tempVec.add(tempKcmc);
				tempVec.add(tempKclx);
				tempVec.add(tempSkjsbh);
				tempVec.add(tempSkjsxm);
				tempVec.add(tempCdyq);
				tempVec.add(tempCdmc);
				tempVec.add(tempSjcdbh);
				tempVec.add(tempSjcdmc);
				tempVec.add(tempSkzc);
				tempVec.add(tempSkzcxq);
				tempVec.add(zt);
				
				for(int i=0; i<tempVec.size(); i+=23){
					sql = "insert into V_排课管理_课程表明细详情表 values(" +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+1))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+2))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+3))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+4))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+5))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+6))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+7))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+8))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+9))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+10))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+11))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+12))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+13))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+14))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+15))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+16))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+17))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+18))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+19))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+20))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+21))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+22))) + "')";
					sqlVec.add(sql);
				}
			}
			
			//调整 V_排课管理_课程表明细表 授课计划编号、实际场地信息错误排序
			sql = "update a set a.连节相关编号=b.连节相关编号,a.授课计划明细编号=b.授课计划明细编号,a.实际场地编号=b.实际场地编号,a.实际场地名称=b.实际场地名称 " +
				"from V_排课管理_课程表明细表 a " +
				"left join V_排课管理_课程表明细详情表 b on b.课程表明细编号=a.课程表明细编号 " +
				"where b.学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "' and b.授课计划明细编号<>a.授课计划明细编号";
			sqlVec.add(sql);
			
			if(!db.executeInsertOrUpdateTransaction(sqlVec)){
				flag = false;
			}
		}else{
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 将所有课程表信息插入数据库
	 * @date:2015-06-04
	 * @param allWpkcVec 未排课程最新信息
	 * @return String 
	 * @author:yeq
	 * @throws SQLException
	 */
	public String loadReturnInfo(Vector allWpkcVec) throws SQLException{
		String result = "";
		
		//判断排课过程中是否有错误
		if("自动排课成功".equalsIgnoreCase(this.getMSG())){
			Vector tempVec = new Vector();
			String wpkcCode = "";
			
			for(int i=0; i<allWpkcVec.size(); i+=2){
				tempVec = (Vector)allWpkcVec.get(i+1);
				for(int j=0; j<tempVec.size(); j+=16){
					//判断是否还有剩余节数
					if(MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(j+4)))-MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(j+5))) > 0){
						wpkcCode += "'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(j))) + "',";
					}
				}
			}
			
			//判断是否还有未排课程
			if(wpkcCode.length() > 0){
				wpkcCode = wpkcCode.substring(0, wpkcCode.length()-1);
				String sql = "select d.专业名称,c.班级名称,a.课程名称,a.实际已排节数,a.节数-a.实际已排节数 as 未排节数 from V_规则管理_授课计划明细表 a " +
						"left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
						//"left join V_学校班级_数据子类 c on c.行政班代码=b.行政班代码 " +
						"left join V_基础信息_班级信息表 c on c.班级编号=b.行政班代码 " +
						"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
						"where a.状态='1' and a.授课计划明细编号 in (" + wpkcCode + ") " +
						"order by b.行政班代码";
				tempVec = db.GetContextVector(sql);
				
				//部分课程未完成
				result = "{\"MSG\":\"complatePart\",\"xnxq\":\"" + this.getPK_XNXQBM() + "\",\"wpkc\":[";
				
				for(int i=0; i<tempVec.size(); i+=5){
					result += "{\"专业\":\"" + MyTools.StrFiltr(tempVec.get(i)) + "\"," +
							"\"班级\":\"" + MyTools.StrFiltr(tempVec.get(i+1)) + "\"," +
							"\"课程名称\":\"" + MyTools.StrFiltr(tempVec.get(i+2)) + "\"," +
							"\"已排节数\":\"" + MyTools.StrFiltr(tempVec.get(i+3)) + "\"," +
							"\"未排节数\":\"" + MyTools.StrFiltr(tempVec.get(i+4)) + "\"" +
							"},";
				}
				result = result.substring(0, result.length()-1);
				result += "]}";
			}else{
				//所有课程全部完成
				result = "{\"MSG\":\"complateAll\",\"xnxq\":\"" + this.getPK_XNXQBM() + "\"}";
			}
		}else{
			//返回出错信息
			result = "{\"MSG\":\"" + this.getMSG() + "\"}";
		}
		          
		return result;
	}
	
	/**
	 * 更新课程表及未排课程信息
	 * @date:2015-06-18
	 * @param kcbInfo 课程表信息
	 * @param wpkcInfo 未配课程信息
	 * @author:yeq
	 * @throws SQLException
	 */
	public void updateKCB(String[] kcbInfo, String[] wpkcInfo) throws SQLException{
		Vector tempVec = null;
		Vector sqlVec = new Vector();
		String sql = "";
		
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		String xnxqbm = "";
		String xzbdm = "";
		
		//课程表更新语句
		if(kcbInfo.length > 1){
			for(int i=0; i<kcbInfo.length; i+=9){
				//查询当前更新课表的班级信息
				if(i == 0){
					sql = "select 学年学期编码,行政班代码 from V_排课管理_课程表明细详情表 where 课程表明细编号='" + MyTools.fixSql(kcbInfo[i]) + "'";
					tempVec = db.GetContextVector(sql);
					if(tempVec!=null && tempVec.size()>0){
						xnxqbm = MyTools.StrFiltr(tempVec.get(0));
						xzbdm = MyTools.StrFiltr(tempVec.get(1));
					}
				}
				
				sql = "update V_排课管理_课程表明细表 set " +
					"班级排课状态='" + MyTools.fixSql(kcbInfo[i+1]) + "'," +
					"授课计划明细编号='" + MyTools.fixSql(kcbInfo[i+2]) + "'," +
					"实际场地编号='" + MyTools.fixSql(kcbInfo[i+5]) + "'," +
					"实际场地名称='" + MyTools.fixSql(kcbInfo[i+6]) + "' " +
					"where 课程表明细编号='" + MyTools.fixSql(kcbInfo[i]) + "'";
				sqlVec.add(sql);
				
				sql = "update V_排课管理_课程表明细详情表 set 班级排课状态=b.班级排课状态,连节相关编号=b.连节相关编号,授课计划明细编号=b.授课计划明细编号," +
					"课程代码=b.课程代码,课程名称=b.课程名称,课程类型=b.课程类型," +
					"授课教师编号='" + MyTools.fixSql(kcbInfo[i+3]) + "'," +
					"授课教师姓名='" + MyTools.fixSql(kcbInfo[i+4]) + "'," +
					"场地要求=b.场地要求,场地名称=b.场地名称,实际场地编号=b.实际场地编号,实际场地名称=b.实际场地名称," +
					"授课周次='" + MyTools.fixSql(kcbInfo[i+7]) + "'," +
					"授课周次详情='" + MyTools.fixSql(kcbInfo[i+8]) + "' " +
					"from V_排课管理_课程表明细详情表 a " +
					"left join V_排课管理_课程表明细详情临时表 b on a.课程表明细编号=b.课程表明细编号 " +
					"where a.课程表明细编号='" + MyTools.fixSql(kcbInfo[i]) + "'";
				sqlVec.add(sql);
				
				//判断是否为公共课
//				if(this.getAuth().indexOf(pubTeacher) > -1){
//					sql = "update V_排课管理_公共课课程表明细表 set " +
//						"班级排课状态='" + MyTools.fixSql(kcbInfo[i+1]) + "'," +
//						"授课计划明细编号='" + MyTools.fixSql(kcbInfo[i+2]) + "'," +
//						"实际场地编号='" + MyTools.fixSql(kcbInfo[i+3]) + "'," +
//						"实际场地名称='" + MyTools.fixSql(kcbInfo[i+4]) + "' " +
//						"where 课程表明细编号='" + MyTools.fixSql(kcbInfo[i]) + "'";
//					sqlVec.add(sql);
//				}
			}
		}
		
		//未排课程更新语句
		if(wpkcInfo.length > 1){
			for(int i=0; i<wpkcInfo.length; i+=2){
				sql = "update V_规则管理_授课计划明细表 set 实际已排节数=cast(节数 as int) - cast('" + MyTools.fixSql(wpkcInfo[i+1]) + "' as int) where 授课计划明细编号='" + MyTools.fixSql(wpkcInfo[i]) + "'";
				sqlVec.add(sql);
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
			
			//生成周课表信息
			if(kcbInfo.length > 1){
				this.addWeekDetail(xnxqbm, "", xzbdm);
			}
		}else{
			this.setMSG("保存失败");
		}
	}
	
	//----------------------------------------------------------添加课程部分 start----------------------------------------------------------
	/**
	 * 查询学期添加课程信息
	 * @date:2015-07-22
	 * @return String
	 * @author:yeq
	 * @throws SQLException
	 */
	/*
	public String loadSemAddCourse() throws SQLException{
		String result = "";
		Vector vec = null;
		String sql = "select distinct 编号,课程编号,课程名称,授课教师姓名,场地名称,授课周次 from V_排课管理_添加课程信息表 " +
				"where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
				"and 类型='" + MyTools.fixSql(this.getPT_LX()) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			this.setMSG("已设置");
			result = "{\"MSG\":\"" + this.getMSG() + "\"," +
					"\"code\":\"" + MyTools.StrFiltr(vec.get(0)) + "\"," +
					"\"courseCode\":\"" + MyTools.StrFiltr(vec.get(1)) + "\"," +
					"\"courseName\":\"" + MyTools.StrFiltr(vec.get(2)) + "\"," +
					"\"teaName\":\"" + MyTools.StrFiltr(vec.get(3)) + "\"," +
					"\"siteName\":\"" + MyTools.StrFiltr(vec.get(4)) + "\"," +
					"\"week\":\"" + MyTools.StrFiltr(vec.get(5)) + "\"}";
		}else{
			this.setMSG("未设置");
			result = "{\"MSG\":\"" + this.getMSG() + "\"}";
		}
		
		return result;
	}
	*/
	
	/**
	 * 添加学期课程
	 * @date:2015-07-22
	 * @author:yeq
	 * @throws SQLException
	 */
	/*
	public void addSemCourse() throws SQLException{
		Vector vec = null;
		String maxId = "TJKC_10000001";
		String sql = "";
		
		//获取最大编号
		sql = "select top 1 cast(right(编号,10) as bigint)+1 from V_排课管理_添加课程信息表 order by cast(right(编号,10) as bigint) desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			maxId = "TJKC_" + MyTools.StrFiltr(vec.get(0));
		}
		
		//查询当前学年学期的最后天下午最后一个节次
		sql = "select 每周天数,上午节数,中午节数,下午节数 from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "'";
		vec = db.GetContextVector(sql);
		int mzts = MyTools.StringToInt(MyTools.StrFiltr(vec.get(0)));
		int js = MyTools.StringToInt(MyTools.StrFiltr(vec.get(1))) + MyTools.StringToInt(MyTools.StrFiltr(vec.get(2)))  + MyTools.StringToInt(MyTools.StrFiltr(vec.get(3)));
		String timeOrder = (mzts<10?"0"+mzts:""+mzts) + (js<10?"0"+js:""+js);
		
		//添加课程
		sql = "insert into V_排课管理_添加课程信息表 (编号,类型,学年学期编码,时间序列,课程编号,课程名称,创建人,创建时间,状态) values(" +
			"'" + MyTools.fixSql(maxId) + "'," +
			"'" + MyTools.fixSql(this.getPT_LX()) + "'," +
			"'" + MyTools.fixSql(this.getPT_XNXQBM()) + "'," +
			"'" + MyTools.fixSql(timeOrder) + "'," +
			"'" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
			"'" + MyTools.fixSql(this.getPT_KCMC()) + "'," +
			"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
			"getDate(),'1')";	
		
		if(db.executeInsertOrUpdate(sql)){
			this.setPT_ID(maxId);
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	*/
	
	/**
	 * 更新添加的课程
	 * @date:2015-07-22
	 * @author:yeq
	 * @throws SQLException
	 */
	/*
	public void updateSemCourse() throws SQLException{
		String sql = "update V_排课管理_添加课程信息表 set " +
				"课程编号='" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
				"课程名称='" + MyTools.fixSql(this.getPT_KCMC()) + "' " +
				"where 编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	*/
	
	/**
	 * 删除添加的课程
	 * @date:2015-07-22
	 * @author:yeq
	 * @throws SQLException
	 */
	/*
	public void delAddCourse() throws SQLException{
		String sql = "delete from V_排课管理_添加课程信息表 where 编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	*/
	
	/**
	 * 查询添加学期课程列表信息
	 * @date:2016-03-08
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param xnxqbm 学年学期编码
	 * @param zymc 专业名称
	 * @param xzbmc 班级名称
	 * @param kcmc 课程名称
	 * @return String
	 * @author:yeq
	 * @throws SQLException
	 */
	public Vector queSemCourseList(int pageNum, int pageSize, String xnxqbm, String zymc, String xzbmc, String kcmc) throws SQLException{
		Vector vec = null;
		String sql = "select distinct a.编号,b.学年学期编码,b.学年学期名称,d.专业名称+'('+d.专业代码+')' as 专业名称,c.班级名称,a.课程名称+'（'+a.课程编号+'）' as 课程名称,a.授课教师姓名 " +
				"from V_排课管理_添加课程信息表 a " +
				"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
				//"left join V_学校班级_数据子类 c on c.行政班代码=a.行政班代码 " +
				"left join V_基础信息_班级信息表 c on c.班级编号=a.行政班代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=c.专业代码 " +
				"where a.状态='1' and a.类型='1'";
		//判断查询条件
		if(!"".equalsIgnoreCase(xnxqbm)){
			sql += " and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		}
		if(!"".equalsIgnoreCase(zymc)){
			sql += " and d.专业名称 like '%" + MyTools.fixSql(zymc) + "%'";
		}
		if(!"".equalsIgnoreCase(xzbmc)){
			sql += " and c.班级名称 like '%" + MyTools.fixSql(xzbmc) + "%'";
		}
		if(!"".equalsIgnoreCase(kcmc)){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(kcmc) + "%'";
		}
		sql += " order by b.学年学期编码 desc,a.课程名称+'（'+a.课程编号+'）'";
		
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2016-03-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSemCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,1 " +
				"from V_规则管理_学年学期表 order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 添加学期课程
	 * @date:2016-03-08
	 * @author:yeq
	 * @param teaType 教师类型
	 * @param classCode 班级编号
	 * @throws SQLException
	 */
	public void addSemCourse(String teaType, String classCode) throws SQLException{
		Vector vec = null;
		String id = "";
		String sql = "";
		Vector sqlVec = new Vector();
		String classArray[] = classCode.split(",");
		Vector teaVec = null;
		String tempClassCode = "";
		String tempTeaCode = "";
		String tempTeaName = "";
		
		String tempClass = "";
		for(int i=0; i<classArray.length; i++){
			tempClass += "'"+classArray[i]+"',";
		}
		tempClass = tempClass.substring(0, tempClass.length()-1);
		
		//查询教师相关信息
		//专业组长
		if("1".equalsIgnoreCase(teaType)){
			sql = "select distinct a.班级编号,isnull(replace(b.教师代码,'@',''),'') as 工号,isnull(replace(b.教师姓名,'@',''),'') as 姓名 " +
				//"from V_学校班级_数据子类 a " +
				"from V_基础信息_班级信息表 a " +
				"left join V_专业组组长信息 b on b.专业代码=a.专业代码 " +
				"order by a.班级编号 desc";
			teaVec = db.GetContextVector(sql);
		}else if("2".equalsIgnoreCase(teaType)){
			sql = "select distinct a.行政班代码,replace(a.登分教师编号,'@','') as 工号,replace(a.登分教师姓名,'@','') as 姓名 " +
				"from V_成绩管理_登分教师信息表 a " +
				"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
				"where a.状态='1' and b.状态='1' and a.课程名称 like '体育%' and a.课程名称 not like '%新标准' " +
				"and b.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
				"and a.行政班代码 in ('" + tempClass + "') order by b.学年学期编码 desc";
			teaVec = db.GetContextVector(sql);
		}
		
		//判断是否为特殊课程(体育新标准)
		//如果是的话，需要判断添加课程的班级当前学期如果有体育2添加 体育新标准1，如果有体育3添加 体育新标准2
		//if("0000000041".equalsIgnoreCase(this.getPT_KCBH())){
		if(this.getPT_KCMC().indexOf("体育新标准") > -1){
//			sql = "select distinct a.行政班代码," +
//				"(select distinct 课程名称 from V_排课管理_课程表明细详情表 where 行政班代码=a.行政班代码  " +
//				"and 学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
//				"and (课程名称 like '%体育%' or 课程名称 like '%体育3%')) as 课程名称," +
//				"(select count(*) from V_成绩管理_登分教师信息表 t " +
//				"left join V_成绩管理_科目课程信息表 t1 on t1.科目编号=t.科目编号 where t.行政班代码=a.行政班代码 " +
//				"and t1.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
//				"and t.课程代码='" + MyTools.fixSql(this.getPT_KCBH()) + "') as 是否添加 " +
//				"from (select 行政班代码 from V_学校班级_数据子类 where 行政班代码 in (" + tempClass + ")) a";
			sql = "select distinct a.行政班代码," +
				"(select distinct 课程名称 from V_排课管理_课程表明细详情表 where 行政班代码=a.行政班代码  " +
				"and 学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
				"and 课程名称 like '%体育%') as 课程名称 " +
				//"from (select 行政班代码 from V_学校班级_数据子类 where 行政班代码 in (" + tempClass + ")) a";
				"from (select 班级编号 as 行政班代码 from V_基础信息_班级信息表 where 班级编号 in (" + tempClass + ")) a";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				String tempKcmc = "";
				
				for(int i=0; i<vec.size(); i+=2){
					tempClassCode = MyTools.StrFiltr(vec.get(i));
					
					if(!"".equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
						tempKcmc = MyTools.StrFiltr(vec.get(i+1)) + "新标准";
					}else{
						tempKcmc = "体育新标准";
					}
					
					//根据教师类型设置不同教师
					if("1".equalsIgnoreCase(teaType) || "2".equalsIgnoreCase(teaType)){
						tempTeaCode = "";
						tempTeaName = "";
						
						for(int j=0; j<teaVec.size(); j+=3){
							if(tempClassCode.equalsIgnoreCase(MyTools.StrFiltr(teaVec.get(j)))){
								tempTeaCode = MyTools.StrFiltr(teaVec.get(j+1));
								tempTeaName = MyTools.StrFiltr(teaVec.get(j+2));
								break;
							}
						}
					}else{
						tempTeaCode = this.getPT_SKJSBH();
						tempTeaName = this.getPT_SKJSMC();
					}
					
					id = db.getMaxID("V_排课管理_添加课程信息表", "编号", "TJKC_", 10);
					//添加课程
					sql = "insert into V_排课管理_添加课程信息表 (编号,类型,学年学期编码,行政班代码,课程编号,课程名称,授课教师编号,授课教师姓名,考试形式,学分,总课时,创建人,创建时间,状态) values(" +
						"'" + MyTools.fixSql(id) + "'," +
						"'1'," +
						"'" + MyTools.fixSql(this.getPT_XNXQBM()) + "'," +
						"'" + MyTools.fixSql(tempClassCode) + "'," +
						"'" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
						"'" + MyTools.fixSql(tempKcmc) + "'," +
						"'" + MyTools.fixSql(tempTeaCode) + "'," +
						"'" + MyTools.fixSql(tempTeaName) + "'," +
						"'" + MyTools.fixSql(this.getPT_KSXS()) + "'," +
						"'" + MyTools.fixSql(this.getPT_XF()) + "'," +
						"'" + MyTools.fixSql(this.getPT_ZKS()) + "'," +
						"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
						"getDate(),'1')";
					sqlVec.add(sql);
				}
			}
		}else{
			for(int i=0; i<classArray.length; i++){
				tempClassCode = classArray[i];
				
				//根据教师类型设置不同教师
				if("1".equalsIgnoreCase(teaType) || "2".equalsIgnoreCase(teaType)){
					tempTeaCode = "";
					tempTeaName = "";
					
					for(int j=0; j<teaVec.size(); j+=3){
						if(tempClassCode.equalsIgnoreCase(MyTools.StrFiltr(teaVec.get(j)))){
							tempTeaCode = MyTools.StrFiltr(teaVec.get(j+1));
							tempTeaName = MyTools.StrFiltr(teaVec.get(j+2));
							break;
						}
					}
				}else{
					tempTeaCode = this.getPT_SKJSBH();
					tempTeaName = this.getPT_SKJSMC();
				}
				
				id = db.getMaxID("V_排课管理_添加课程信息表", "编号", "TJKC_", 10);
				
				//添加课程
				sql = "insert into V_排课管理_添加课程信息表 (编号,类型,学年学期编码,行政班代码,课程编号,课程名称,授课教师编号,授课教师姓名,考试形式,学分,总课时,创建人,创建时间,状态) values(" +
					"'" + MyTools.fixSql(id) + "'," +
					"'1'," +
					"'" + MyTools.fixSql(this.getPT_XNXQBM()) + "'," +
					"'" + MyTools.fixSql(tempClassCode) + "'," +
					"'" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
					"(select 课程名称 from V_课程数据子类 where 课程号='" + MyTools.fixSql(this.getPT_KCBH()) + "')," +
					"'" + MyTools.fixSql(tempTeaCode) + "'," +
					"'" + MyTools.fixSql(tempTeaName) + "'," +
					"'" + MyTools.fixSql(this.getPT_KSXS()) + "'," +
					"'" + MyTools.fixSql(this.getPT_XF()) + "'," +
					"'" + MyTools.fixSql(this.getPT_ZKS()) + "'," +
					"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
					"getDate(),'1')";	
				sqlVec.add(sql);
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 查询添加学期课程班级tree
	 * @date:2016-03-09
	 * @author:yeq
	 * @param level
	 * @param parentCode 专业代码
	 * @return 班级信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queSemClassTree(String level, String parentCode)throws SQLException,WrongSQLException{
		String sql = "";
		Vector vec = null;
		
		//获取专业目录
		if("0".equalsIgnoreCase(level)){
//			sql = "select distinct 'zy-'+b.专业代码 as id,b.专业名称+'('+b.专业代码+')' as text,state='closed' from V_学校班级_数据子类 a " +
//				"left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
//				"where a.状态='1' and LEN(b.专业代码)<>3 order by text";
			sql = "select distinct 'zy-'+b.专业代码 as id,b.专业名称+'('+b.专业代码+')' as text,state='closed' " +
				"from V_基础信息_班级信息表 a " +
				"left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
				"where a.状态='1' and LEN(b.专业代码)<>3 order by text";
		}else{
//			sql = "select distinct 行政班代码 as id,行政班名称 as text,state='open' from V_学校班级_数据子类 a " +
//				"where 状态='1' and 专业代码='" + MyTools.fixSql(parentCode) + "'";
			sql = "select distinct 班级代码 as id,班级名称 as text,state='open' from V_基础信息_班级信息表 a " +
				"where 状态='1' and 专业代码='" + MyTools.fixSql(parentCode) + "'";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询添加学期课程列表信息
	 * @date:2016-12-02
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param zymc 专业名称
	 * @param xzbmc 班级名称
	 * @return String
	 * @author:yeq
	 * @throws SQLException
	 */
	public Vector queSemClassList(int pageNum, int pageSize, String zymc, String xzbmc) throws SQLException{
		Vector vec = null;
		String sql = "select distinct a.班级编号 as 行政班代码,a.班级名称 as 行政班名称,b.专业名称+'（'+b.专业代码+'）' as 专业名称 " +
				//"from V_学校班级_数据子类 a " +
				"from V_基础信息_班级信息表 a " +
				"left join V_专业基本信息数据子类 b on b.专业代码=a.专业代码 " +
				"where a.状态='1'";
		//判断查询条件
		if(!"".equalsIgnoreCase(xzbmc)){
			//sql += " and a.行政班名称 like '%" + MyTools.fixSql(xzbmc) + "%'";
			sql += " and a.班级名称 like '%" + MyTools.fixSql(xzbmc) + "%'";
		}
		if(!"".equalsIgnoreCase(zymc)){
			sql += " and b.专业名称 like '%" + MyTools.fixSql(zymc) + "%'";
		}
		sql += " order by b.专业名称+'（'+b.专业代码+'）',a.班级编号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 删除学期课程
	 * @date:2016-03-08
	 * @author:yeq
	 * @throws SQLException
	 */
	public void delSemCourse(String delCode) throws SQLException{
		String sql = "delete from V_排课管理_添加课程信息表 where 编号 in ('" + delCode.replaceAll(",", "','") + "')";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 删除添加的课程
	 * @date:2016-03-08
	 * @author:yeq
	 * @throws SQLException
	 */
	public void delAddCourse() throws SQLException{
		String sql = "delete from V_排课管理_添加课程信息表 where 编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 查询班级课程及添加课程信息
	 * @return 班级课程及添加课程信息
	 * @throws SQLException
	 */
	public Vector loadClassKcbInfo()throws SQLException{
		Vector resultVec = new Vector();
		String sql = "";
		
		//查询课程表
		sql = "select distinct 时间序列,班级排课状态,课程名称,授课教师姓名,授课周次 " +
			//"stuff((select '｜'+课程名称 from V_排课管理_课程表明细详情表 t2 where t2.课程表明细编号=t1.课程表明细编号 order by (case 授课周次 when 'odd' then '1' when 'even' then '2' else 授课周次 end) for XML PATH('')),1,1,'') as 课程名称," +
			//"replace(stuff((select '｜'+授课教师姓名 from V_排课管理_课程表明细详情表 t3 where t3.课程表明细编号=t1.课程表明细编号 order by (case 授课周次 when 'odd' then '1' when 'even' then '2' else 授课周次 end) for XML PATH('')),1,1,''),'&amp;','&') as 授课教师姓名," +
			//"stuff((select '｜'+授课周次 from V_排课管理_课程表明细详情表 t6 where t6.课程表明细编号=t1.课程表明细编号 order by (case 授课周次 when 'odd' then '1' when 'even' then '2' else 授课周次 end) for XML PATH('')),1,1,'') as 授课周次,实际场地名称 " +
			"from V_排课管理_课程表明细详情表 t1 " +
			"where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
			"and t1.行政班代码='" + MyTools.fixSql(this.getPT_XZBDM()) + "'";
			//"group by 课程表明细编号,班级排课状态,时间序列,实际场地名称";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		//查询添加课程信息
		sql = "select distinct 编号,类型,时间序列,课程编号,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,授课周次,考试形式,学分,总课时 from V_排课管理_添加课程信息表 " +
			"where 状态='1' and 学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' and (类型='1' " +
			"or 类型='2') and 行政班代码='" + MyTools.fixSql(this.getPT_XZBDM()) + "'";
		resultVec.add(db.getConttexJONSArr(sql, 0, 0));
		
		return resultVec;
	}
	
	/**
	 * 查询添加班级课程可用教室
	 * @date:2015-07-23
	 * @author:yeq
	 * @throws SQLException
	 */
	public String queSiteCombotree() throws SQLException{
//		Vector vec = null;
//		String sql = "select '请选择' as comboName,'' as comboValue,'' as 实际容量 " +
//				"union all " +
//				"select distinct 教室名称+'('+cast(实际容量 as nvarchar)+'人)' as comboName,教室编号,实际容量 as comboValue " +
//				"from V_教室数据类 where 是否可用='1' and 教室编号 not in (select distinct 教室编号 " +
//				"from (select a.教室编号,isnull(b.时间序列,'') as 时间序列,b.学年学期编码,b.行政班代码 from V_教室数据类 a " +
//				"left join V_排课管理_课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%') as t " +
//				"where t.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' and t.行政班代码<>'" + MyTools.fixSql(this.getPT_XZBDM()) + "' and t.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "') and 教室编号 not in (" +
//				"select c.教室编号 from V_教室数据类 c " +
//				"left join V_排课管理_添加课程信息表 d on d.场地编号 like '%'+c.教室编号+'%' " +
//				"where d.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' and d.行政班代码<>'" + MyTools.fixSql(this.getPT_XZBDM()) + "' and d.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "') order by 实际容量,comboName desc";
//
//		vec = db.getConttexJONSArr(sql, 0, 0);
//		return vec;
		
		Vector vec = null;
		String sql = "select distinct a.教室类型代码,b.名称,a.教室编号,a.教室名称,a.实际容量 from V_教室数据类 a " +
				"left join V_基础信息_教室类型 b on a.教室类型代码=b.编号 " +
				"where a.是否可用='1' and a.教室编号 not in (select distinct t.教室编号 from (" +
				"select t1.教室编号,isnull(t2.时间序列,'') as 时间序列,t2.学年学期编码,t2.行政班代码 " +
				"from V_教室数据类 t1 left join V_排课管理_课程表明细详情表 t2 on t2.实际场地编号 like '%'+t1.教室编号+'%') as t " +
				"where t.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' and t.行政班代码<>'" + MyTools.fixSql(this.getPT_XZBDM()) + "' and t.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "') and 教室编号 " +
				"not in (select c.教室编号 from V_教室数据类 c left join V_排课管理_添加课程信息表 d on d.场地编号 like '%'+c.教室编号+'%' " +
				"where d.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
				"and d.行政班代码<>'" + MyTools.fixSql(this.getPT_XZBDM()) + "' and d.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "') order by 教室类型代码,实际容量";
		vec = db.GetContextVector(sql);
		String result = "[";
		String preClassType = "";
		String curClassType = "";
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=5){
				curClassType = MyTools.StrFiltr(vec.get(i));
				if(!preClassType.equalsIgnoreCase(curClassType)){
					preClassType = curClassType;
					if(i > 0){
						result = result.substring(0, result.length()-1) + "]},";
					}
					result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\",\"children\":";
					result += "[{\"id\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+3))+"("+MyTools.StrFiltr(vec.get(i+4))+"人)"  + "\"},";
				}else{
					result += "{\"id\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\",\"text\":\"" + MyTools.StrFiltr(vec.get(i+3))+"("+MyTools.StrFiltr(vec.get(i+4))+"人)"  + "\"},";
				}
			}
			
			result = result.substring(0, result.length()-1) + "]}]";
		}
		return result;
	}
	
	/**
	 * 查询教师下拉树数据（添加班级课程）
	 * @date:2016-01-26
	 * @author:yeq
	 * @throws SQLException
	 */
	public String queTeaCombotree() throws SQLException{
		Vector vec = null;
		String sql = "select distinct d.层级编号,d.层级名称,a.工号,a.姓名 from V_教职工基本数据子类 a " +
				"inner join sysUserAuth b on b.UserCode=a.工号 " +
				"inner join V_权限层级关系表 c on c.权限编号=b.AuthCode " +
				"inner join V_层级表 d on d.层级编号=c.层级编号 " +
				"order by d.层级编号";
		vec = db.GetContextVector(sql);
		String result = "[";
		String preClassType = "";
		String curClassType = "";
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=4){
				curClassType = MyTools.StrFiltr(vec.get(i));
				if(!preClassType.equalsIgnoreCase(curClassType)){
					preClassType = curClassType;
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
		}
		return result;
	}
	
	/**
	 * 添加班级课程
	 * @date:2015-07-23
	 * @author:yeq
	 * @throws SQLException
	 */
	public void addClassCourse() throws SQLException{
		String id = "";
		String sql = "";
		Vector sqlVec = new Vector();
		Vector tempVec = null;
		String kcbmxbh = "";
		String kcbzbbh = "";
		
		this.setPT_SKJSBH(this.getPT_SKJSBH().replaceAll(",", "+"));
		this.setPT_SKJSMC(this.getPT_SKJSMC().replaceAll(",", "+"));
		id = db.getMaxID("V_排课管理_添加课程信息表", "编号", "TJKC_", 10);
		
		//添加课程
		sql = "insert into V_排课管理_添加课程信息表 (编号,类型,学年学期编码,行政班代码,时间序列,课程编号,课程名称,授课教师编号,授课教师姓名,考试形式,学分,总课时,创建人,创建时间,状态) values(" +
			"'" + MyTools.fixSql(id) + "'," +
			"'" + MyTools.fixSql(this.getPT_LX()) + "'," +
			"'" + MyTools.fixSql(this.getPT_XNXQBM()) + "'," +
			"'" + MyTools.fixSql(this.getPT_XZBDM()) + "'," +
			"'" + MyTools.fixSql(this.getPT_SJXL()) + "'," +
			"'" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
			"(select 课程名称 from V_课程数据子类 where 课程号='" + MyTools.fixSql(this.getPT_KCBH()) + "')," +
			"'" + MyTools.fixSql(this.getPT_SKJSBH()) + "'," +
			"'" + MyTools.fixSql(this.getPT_SKJSMC()) + "'," +
			"'" + MyTools.fixSql(this.getPT_KSXS()) + "'," +
			"'" + MyTools.fixSql(this.getPT_XF()) + "'," +
			"'" + MyTools.fixSql(this.getPT_ZKS()) + "'," +
			"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
			"getDate(),'1')";	
		sqlVec.add(sql);
		
		//更新周课表信息
		sql = "select 课程表明细编号,课程表主表编号 from V_排课管理_课程表明细详情表 where 学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
			"and 行政班代码='" + MyTools.fixSql(this.getPT_XZBDM()) + "' and 时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0){
			kcbmxbh = MyTools.StrFiltr(tempVec.get(0));
			kcbzbbh = MyTools.StrFiltr(tempVec.get(1));
		}
		
		//查询原课表信息
		sql = "select 授课周次,时间序列,课程代码,行政班名称,专业代码,专业名称 from V_排课管理_课程表周详情表 where 课程表明细编号='" + MyTools.fixSql(kcbmxbh) + "'";
		tempVec = db.GetContextVector(sql);
		
		if(tempVec!=null && tempVec.size()>0){
			for(int i=0; i<tempVec.size(); i+=6){
				if("".equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2)))){
					sql = "update V_排课管理_课程表周详情表 set " +
						"授课计划明细编号='" + MyTools.fixSql(id) + "'," +
						"课程代码='" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
						"课程名称='" + MyTools.fixSql(this.getPT_KCMC()) + "'," +
						"授课教师编号='" + MyTools.fixSql(this.getPT_SKJSBH()) + "'," +
						"授课教师姓名='" + MyTools.fixSql(this.getPT_SKJSMC()) + "' " +
						"where 课程表明细编号='" + MyTools.fixSql(kcbmxbh) + "' " +
						"and 授课周次='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i))) + "' " +
						"and 时间序列='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+1))) + "'";
				}else{
					sql = "insert into V_排课管理_课程表周详情表 (课程表明细编号,课程表主表编号,学年学期编码,行政班代码,行政班名称,专业代码,专业名称,授课周次," +
						"时间序列,授课计划明细编号,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,场地编号,场地名称,状态) values(" +
						"'" + MyTools.fixSql(kcbmxbh) + "'," +
						"'" + MyTools.fixSql(kcbzbbh) + "'," +
						"'" + MyTools.fixSql(this.getPT_XNXQBM()) + "'," +
						"'" + MyTools.fixSql(this.getPT_XZBDM()) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+3))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+4))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+5))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i))) + "'," +
						"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i+1))) + "'," +
						"'" + MyTools.fixSql(id) + "'," +
						"'" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
						"'" + MyTools.fixSql(this.getPT_KCMC()) + "'," +
						"''," +
						"'" + MyTools.fixSql(this.getPT_SKJSBH()) + "'," +
						"'" + MyTools.fixSql(this.getPT_SKJSMC()) + "'," +
						"'','','1')";
				}
				sqlVec.add(sql);
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setPT_ID(id);
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 更改添加的班级课程
	 * @date:2015-07-23
	 * @author:yeq
	 * @throws SQLException
	 */
	public void editClassCourse() throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		Vector tempVec = null;
		String kcbmxbh = "";
		
		this.setPT_SKJSBH(this.getPT_SKJSBH().replaceAll(",", "+"));
		this.setPT_SKJSMC(this.getPT_SKJSMC().replaceAll(",", "+"));
		
		sql = "update V_排课管理_添加课程信息表 set " +
			"课程编号='" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
			"课程名称=(select 课程名称 from V_课程数据子类 where 课程号='" + MyTools.fixSql(this.getPT_KCBH()) + "')," +
			"授课教师编号='" + MyTools.fixSql(this.getPT_SKJSBH()) + "'," +
			"授课教师姓名='" + MyTools.fixSql(this.getPT_SKJSMC()) + "', " +
			"考试形式='" + MyTools.fixSql(this.getPT_KSXS()) + "'," +
			"学分='" + MyTools.fixSql(this.getPT_XF()) + "'," +
			"总课时='" + MyTools.fixSql(this.getPT_ZKS()) + "' " +
			"where 编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		sqlVec.add(sql);
		
		//更新周课表信息
		sql = "update V_排课管理_课程表周详情表 set " +
			"课程代码='" + MyTools.fixSql(this.getPT_KCBH()) + "'," +
			"课程名称='" + MyTools.fixSql(this.getPT_KCMC()) + "'," +
			"授课教师编号='" + MyTools.fixSql(this.getPT_SKJSBH()) + "'," +
			"授课教师姓名='" + MyTools.fixSql(this.getPT_SKJSMC()) + "' " +
			"where 授课计划明细编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除添加的班级课程
	 * @date:2016-04-27
	 * @author:yeq
	 * @throws SQLException
	 */
	public void delClassCourse() throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		Vector tempVec = null;
		String kcbmxbh = "";
		
		sql = "delete from V_排课管理_添加课程信息表 where 编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		sqlVec.add(sql);
		
		//更新周课表信息
		sql = "select 课程表明细编号 from V_排课管理_课程表明细详情表 a " +
			"left join V_排课管理_添加课程信息表 b on b.学年学期编码=a.学年学期编码 and b.行政班代码=a.行政班代码 and b.时间序列=a.时间序列 " +
			"where b.编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		tempVec = db.GetContextVector(sql);
		if(tempVec!=null && tempVec.size()>0)
			kcbmxbh = MyTools.StrFiltr(tempVec.get(0));
		
		sql = "select 编号,(select count(*) from V_排课管理_课程表周详情表 where 课程表明细编号=a.课程表明细编号 and 授课周次=a.授课周次 and 时间序列=a.时间序列) as 条目数 " +
			"from V_排课管理_课程表周详情表 a where 授课计划明细编号='" + MyTools.fixSql(this.getPT_ID()) + "' " +
			"order by cast(授课周次 as int),时间序列";
		tempVec = db.GetContextVector(sql);
		
		if(tempVec!=null && tempVec.size()>0){
			for(int i=0; i<tempVec.size(); i+=2){
				if(MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(i+1))) > 1){
					sql = "delete from V_排课管理_课程表周详情表 where 编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i))) + "'";
				}else{
					sql = "update V_排课管理_课程表周详情表 set 授课计划明细编号='',课程代码='',课程名称='',课程类型='',授课教师编号='',授课教师姓名='' " +
						"where 编号='" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(i))) + "'"; 
				}
				sqlVec.add(sql);
			}
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 查询当前班级的学生信息
	 * @date:2015-07-23
	 * @author:yeq
	 * @return 学生信息
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queStuTree()throws SQLException,WrongSQLException{
		Vector vec = null;
		String sql = "select distinct 学号 as id,姓名 as text,state='open' from V_学生基本数据子类 " +
				"where 学生状态 in ('01') and 行政班代码='" + MyTools.fixSql(this.getPT_XZBDM()) + "'";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询学生添加课程信息
	 * @date:2015-07-24
	 * @return String
	 * @author:yeq
	 * @throws SQLException
	 */
	public String loadStuAddCourse() throws SQLException{
		String result = "";
		Vector vec = null;
		String sql = "select distinct a.时间序列,a.编号,a.相关课程表编号,b.行政班代码,b.行政班名称,b.课程名称,b.授课教师姓名,b.授课周次," +
				//"stuff((select '｜'+课程名称 from V_排课管理_课程表明细详情表 t1 where b.课程表明细编号=t1.课程表明细编号 order by (case t1.授课周次 when 'odd' then '1' when 'even' then '2' else t1.授课周次 end) for XML PATH('')),1,1,'') as 课程名称," +
				//"stuff((select '｜'+授课教师姓名 from V_排课管理_课程表明细详情表 t2 where b.课程表明细编号=t2.课程表明细编号 order by (case t2.授课周次 when 'odd' then '1' when 'even' then '2' else t2.授课周次 end) for XML PATH('')),1,1,'') as 授课教师姓名," +
				//"stuff((select '｜'+授课周次 from V_排课管理_课程表明细详情表 t3 where b.课程表明细编号=t3.课程表明细编号 order by (case t3.授课周次 when 'odd' then '1' when 'even' then '2' else t3.授课周次 end) for XML PATH('')),1,1,'') as 授课周次," +
				"b.实际场地名称 from V_排课管理_添加课程信息表 a " +
				"left join V_排课管理_课程表明细详情表 b on b.课程表明细编号=a.相关课程表编号 " +
				"where a.类型='3' and a.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
				"and a.行政班代码='" + MyTools.fixSql(this.getPT_XZBDM()) + "' " +
				"and a.学号='" + MyTools.fixSql(this.getPT_XH()) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			this.setMSG("已设置");
			result = "{\"MSG\":\"" + this.getMSG() + "\",\"courseInfo\":[";
			for(int i=0; i<vec.size(); i+=9){
				result += "{\"timeOrder\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
						"\"code\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
						"\"kcbCode\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
						"\"xzbdm\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
						"\"xzbmc\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"," +
						"\"kcmc\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"," +
						"\"skjsxm\":\"" + MyTools.StrFiltr(vec.get(i+6)) + "\"," +
						"\"sjcdmc\":\"" + MyTools.StrFiltr(vec.get(i+8)) + "\"," +
						"\"skzc\":\"" + MyTools.StrFiltr(vec.get(i+7)) + "\"},";
			}
			result = result.substring(0, result.length()-1);
			result += "]}";
		}else{
			this.setMSG("未设置");
			result = "{\"MSG\":\"" + this.getMSG() + "\"}";
		}
		
		return result;
	}
	
	/**
	 * 读取班级下拉框
	 * @date:2015-07-24
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadClassCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 行政班名称 as comboName,行政班代码 as comboValue from V_学校班级数据子类 " +
				"where cast(建班年月 as int)<cast((select top 1 建班年月 from V_学校班级数据子类 " +
				"where 行政班代码='" + MyTools.fixSql(this.getPT_XZBDM()) + "') as int) " +
				"and 行政班代码 not like '%_0'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取课程表课程下拉框
	 * @date:2015-07-24
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public String loadKcbCourseCombo() throws SQLException{
		String result = "";
		String comboName = "没有可用课程";
		String comboValue = "";
		Vector vec = null;
		String sql = "select distinct 课程名称 as comboName,课程表明细编号 as comboValue " +
				"from V_排课管理_课程表明细详情表 a where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "' " +
				"and a.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' and a.行政班代码='" + MyTools.fixSql(this.getPT_XZBDM()) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			if(!"".equalsIgnoreCase(MyTools.StrFiltr(vec.get(0)))){
				comboName = MyTools.StrFiltr(vec.get(0));
				comboValue = MyTools.StrFiltr(vec.get(1));
			}
		}
		
		result = "[{\"comboName\":\"" + comboName + "\",\"comboValue\":\"" + comboValue + "\"}]";
		return result;
	}
	
	/**
	 * 添加个人课程
	 * @date:2015-07-27
	 * @author:yeq
	 * @return vector 课程信息
	 * @throws SQLException
	 */
	public Vector addStuCourse() throws SQLException{
		Vector vec = null;
		String id = "";
		String sql = "";
		
		//获取最大编号
		/*
		sql = "select top 1 cast(right(编号,10) as bigint)+1 from V_排课管理_添加课程信息表 order by cast(right(编号,10) as bigint) desc";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			maxId = "TJKC_" + MyTools.StrFiltr(vec.get(0));
		}
		*/
		
		id = db.getMaxID("V_排课管理_添加课程信息表", "编号", "TJKC_", 10);
		//添加课程
		sql = "insert into V_排课管理_添加课程信息表 (编号,类型,学年学期编码,行政班代码,学号,相关课程表编号,时间序列,创建人,创建时间,状态) values(" +
			"'" + MyTools.fixSql(id) + "'," +
			"'" + MyTools.fixSql(this.getPT_LX()) + "'," +
			"'" + MyTools.fixSql(this.getPT_XNXQBM()) + "'," +
			"'" + MyTools.fixSql(this.getPT_XZBDM()) + "'," +
			"'" + MyTools.fixSql(this.getPT_XH()) + "'," +
			"'" + MyTools.fixSql(this.getPT_KCBMXBH()) + "'," +
			"'" + MyTools.fixSql(this.getPT_SJXL()) + "'," +
			"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
			"getDate(),'1')";
	
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
			
			//查询插入的课程信息
			sql = "select distinct a.编号,a.相关课程表编号,b.行政班代码,b.行政班名称,b.课程名称,b.授课教师姓名,b.授课周次," +
				//"stuff((select '｜'+课程名称 from V_排课管理_课程表明细详情表 t1 where b.课程表明细编号=t1.课程表明细编号 order by (case t1.授课周次 when 'odd' then '1' when 'even' then '2' else t1.授课周次 end) for XML PATH('')),1,1,'') as 课程名称," +
				//"stuff((select '｜'+授课教师姓名 from V_排课管理_课程表明细详情表 t2 where b.课程表明细编号=t2.课程表明细编号 order by (case t2.授课周次 when 'odd' then '1' when 'even' then '2' else t2.授课周次 end) for XML PATH('')),1,1,'') as 授课教师姓名," +
				//"stuff((select '｜'+授课周次 from V_排课管理_课程表明细详情表 t3 where b.课程表明细编号=t3.课程表明细编号 order by (case t3.授课周次 when 'odd' then '1' when 'even' then '2' else t3.授课周次 end) for XML PATH('')),1,1,'') as 授课周次," +
				"b.实际场地名称 from V_排课管理_添加课程信息表 a " +
				"left join V_排课管理_课程表明细详情表 b on b.课程表明细编号=a.相关课程表编号 " +
				"where 编号='" + MyTools.fixSql(id) + "'";
			vec = db.GetContextVector(sql);
		}else{
			this.setMSG("保存失败");
		}
		
		return vec;
	}
	
	/**
	 * 更改添加的个人课程
	 * @date:2015-07-27
	 * @author:yeq
	 * @return vector 课程信息
	 * @throws SQLException
	 */
	public Vector editStuCourse() throws SQLException{
		Vector vec = null;
		String sql = "update V_排课管理_添加课程信息表 set " +
			"相关课程表编号='" + MyTools.fixSql(this.getPT_KCBMXBH()) + "' " +
			"where 编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
			
			//查询插入的课程信息
			sql = "select distinct a.编号,a.相关课程表编号,b.行政班代码,b.行政班名称,b.课程名称,b.授课教师姓名,b.授课周次," +
				//"stuff((select '｜'+课程名称 from V_排课管理_课程表明细详情表 t1 where b.课程表明细编号=t1.课程表明细编号 order by (case t1.授课周次 when 'odd' then '1' when 'even' then '2' else t1.授课周次 end) for XML PATH('')),1,1,'') as 课程名称," +
				//"stuff((select '｜'+授课教师姓名 from V_排课管理_课程表明细详情表 t2 where b.课程表明细编号=t2.课程表明细编号 order by (case t2.授课周次 when 'odd' then '1' when 'even' then '2' else t2.授课周次 end) for XML PATH('')),1,1,'') as 授课教师姓名," +
				//"stuff((select '｜'+授课周次 from V_排课管理_课程表明细详情表 t3 where b.课程表明细编号=t3.课程表明细编号 order by (case t3.授课周次 when 'odd' then '1' when 'even' then '2' else t3.授课周次 end) for XML PATH('')),1,1,'') as 授课周次," +
				"b.实际场地名称 from V_排课管理_添加课程信息表 a " +
				"left join V_排课管理_课程表明细详情表 b on b.课程表明细编号=a.相关课程表编号 " +
				"where 编号='" + MyTools.fixSql(this.getPT_ID()) + "'";
			vec = db.GetContextVector(sql);
		}else{
			this.setMSG("保存失败");
		}
		
		return vec;
	}
	
	/**
	 * 查询可用教师列表（手动调整课表更改课程信息页面）
	 * @date:2016-01-14
	 * @author:yeq
	 * @param xqzc 学期周次
	 * @throws SQLException
	 */
//	public String loadTeaList(int pageNum, int pageSize, String SKJSBH_CX, String SKJSMC_CX, String SKJSLB_CX, String xqzc) throws SQLException{
	public String loadTeaList(int pageNum, int pageSize, String SKJSBH_CX, String SKJSMC_CX, String xqzc, String bjbh) throws SQLException{
		Vector vec = null;
		Vector tempVec = null;
		Vector hbInfoVec = null;
		String sql = "";
		
		//==========================================================================================================
		
		//查询当前班级的相关系部
//		String xbdm = "";//系部代码
//		sql = " select 系部代码 from dbo.V_学校班级_数据子类 where 行政班代码 = '" + MyTools.fixSql(bjbh) + "'";
//		vec = db.GetContextVector(sql);
//		xbdm = MyTools.StrFiltr(vec.get(0));
		
		Vector vec2 = null;
		sql = " select COUNT(*) as 授课总周次 from dbo.V_规则管理_学期周次表 where 学年学期编码 = '" + MyTools.fixSql(this.getPT_XNXQBM()) + "'";
		vec2 = db.GetContextVector(sql);
		String maxSkzc = MyTools.StrFiltr(vec2.get(0)); //学期最大授课周次
		
		
		//=============================================================================================================
		
		String result = "[";
		
		//获取所有教师信息
//		sql = "select * from (select distinct top 100 percent d.层级编号,d.层级名称,a.工号,a.姓名 from V_教职工基本数据子类 a " +
//			"inner join sysUserAuth b on b.UserCode=a.工号 " +
//			"inner join V_权限层级关系表 c on c.权限编号=b.AuthCode " +
//			"inner join V_层级表 d on d.层级编号=c.层级编号 where a.工号 in ('" + this.getPT_SKJSBH().replaceAll("[+]", "','") + "') order by d.层级编号) as t1 " +
//			"union all " +
//			"select * from (select distinct top 100 percent d.层级编号,d.层级名称,a.工号,a.姓名 from V_教职工基本数据子类 a " +
//			"inner join sysUserAuth b on b.UserCode=a.工号 " +
//			"inner join V_权限层级关系表 c on c.权限编号=b.AuthCode " +
//			"inner join V_层级表 d on d.层级编号=c.层级编号 where a.工号 not in ('" + this.getPT_SKJSBH().replaceAll("[+]", "','") + "')";
		sql = "select * from (select distinct top 100 percent a.工号,a.姓名 from V_教职工基本数据子类 a " +
				"where a.工号 in ('" + this.getPT_SKJSBH().replaceAll("[+]", "','") + "') )as t1 " +
				"union all " +
				"select * from (select distinct top 100 percent a.工号,a.姓名 from V_教职工基本数据子类 a " +
				"where a.工号 not in ('" + this.getPT_SKJSBH().replaceAll("[+]", "','") + "')";
		//判断查询条件
		if(!"".equalsIgnoreCase(SKJSBH_CX)){
			sql += " and a.工号 like '%" + MyTools.fixSql(SKJSBH_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(SKJSMC_CX)){
			sql += " and a.姓名 like '%" + MyTools.fixSql(SKJSMC_CX) + "%'";
		}
//		if(!"".equalsIgnoreCase(SKJSLB_CX)){
//			sql += " and d.层级编号='" + MyTools.fixSql(SKJSLB_CX) + "'";
//		}
//		sql += " order by d.层级编号) as t2";
		sql += " ) as t2";
		vec = db.GetContextVector(sql);
		
		//拼接datagrid数据
//		result = "{\"total\":" + vec.size()/4+ ",\"rows\":[";
		result = "{\"total\":" + vec.size()/2+ ",\"rows\":[";
		
		if(vec!=null && vec.size()>0){
			//获取已使用的教师信息
			sql = "select distinct * from (select a.工号,b.授课计划明细编号,b.授课教师编号,b.授课周次详情 from V_教职工基本数据子类 a " +
				"left join V_排课管理_课程表明细详情表 b on '@'+replace(replace(replace(replace(b.授课教师编号,'+','@+@'),'&','@&@'),'｜','@｜@'),',','@,@')+'@' like '%@'+a.工号+'@%' " +
				"where b.学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
				"and b.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' " +
				"and b.课程表明细编号<>'" + MyTools.fixSql(this.getPK_KCBMXBH()) + "' " +
				"union all " +
				//教师固排
				"select a.工号,b.授课计划明细编号,b.授课教师编号," +
				"replace(stuff((select '｜'+t.授课周次详情 from V_规则管理_授课计划明细表 t where t.状态='1' and t.授课计划明细编号=b.授课计划明细编号 for XML PATH('')),1,1,''),'&amp;','&') as 授课周次 " +
				"from V_教职工基本数据子类 a " +
				"inner join V_规则管理_授课计划明细表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' " +
				"inner join V_规则管理_固排禁排表 c on c.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
				"where b.状态='1' and c.学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
				"and c.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' and c.类型='2' " +
				"and b.授课计划明细编号<>'" + this.getPK_SKJHMXBH() + "' " +
				"and (b.节数-b.实际已排节数)>0" + //20160901/yeq添加条件，避免发生在排课设置课表中更换教师后，该教师无法被选择
				//教师禁排
				"union all " +
				"select 行政班代码 as 教室编号,'教师禁排' as 授课计划明细编号,行政班代码 as 实际场地编号,'1-" + MyTools.fixSql(maxSkzc) + "' as 授课周次详情 from dbo.V_规则管理_固排禁排表 where 时间序列 = '" + MyTools.fixSql(this.getPT_SJXL()) + "' and 类型='3' and 禁排类型 ='js' " +
				"and 学年学期编码  like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
//				"and 行政班代码 in ( select 教室编号 from V_教室数据类 where 校区代码 = '" + MyTools.fixSql(xbdm) + "') " +
				") z";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				//查询合班信息
				sql = "select 授课计划明细编号 from V_规则管理_合班表 " +
					"where 授课计划明细编号 like '%" + MyTools.fixSql(this.getPK_SKJHMXBH()) + "%'";
				hbInfoVec = db.GetContextVector(sql);
				
				String curTeaCode = "";
				String tempTeaCode = "";
				String codeArray[] = new String[0];
				String teaArray[] = new String[0];
				String skzcArray[] = new String[0];
				String tempTeaArray[] = new String[0];
				String tempSkzcArray[] = new String[0];
				Vector selSkzc = this.formatSkzc(this.getPT_SKZC(), xqzc);
				
				boolean hbFlag = false;
				String tempHbInfo = "";
				
				//过滤掉不可用教师
//				for(int i=0; i<vec.size(); i+=4){
				for(int i=0; i<vec.size(); i+=2){
//					curTeaCode = MyTools.StrFiltr(vec.get(i+2));
					curTeaCode = MyTools.StrFiltr(vec.get(i));
					
					outer:for(int j=0; j<tempVec.size(); j+=4){
						tempTeaCode = MyTools.StrFiltr(tempVec.get(j));//教师工号
						
						//判断教师编号是否相同
						if(curTeaCode.equalsIgnoreCase(tempTeaCode)){
							codeArray = MyTools.StrFiltr(tempVec.get(j+1)).split("｜");//授课计划明细编号
							teaArray = MyTools.StrFiltr(tempVec.get(j+2)).split("｜");//授课教师编号
							skzcArray = MyTools.StrFiltr(tempVec.get(j+3)).split("｜");//授课周次
							
							for(int a=0; a<codeArray.length; a++){
								tempTeaArray = teaArray[a].split("&");
								tempSkzcArray = skzcArray[a].split("&");
								
								for(int b=0; b<tempTeaArray.length; b++){
									if(tempTeaArray[b].indexOf(tempTeaCode) > -1){
										//判断是否合班课程
										for(int c=0; c<hbInfoVec.size(); c++){
											tempHbInfo = MyTools.StrFiltr(hbInfoVec.get(c));
											
											if(tempHbInfo.indexOf(this.getPK_SKJHMXBH())>-1 && tempHbInfo.indexOf(codeArray[a])>-1){
												hbFlag = true;
												break;
											}
										}
										
										//如果不是合班课程授课教师的话，判断授课周次是否冲突，如果冲突，去除当前遍历的教师
										if(hbFlag == false){
											if(this.judgeSkzc(selSkzc, this.formatSkzc(tempSkzcArray[b], xqzc))){
//												for(int d=0; d<4; d++){
												for(int d=0; d<2; d++){
													vec.remove(i);
												}
//												i-=4;
												i-=2;
												break outer;
											}
										}else{
											hbFlag = false;
										}
									}
								}
							}
						}
					}
				}
			}
			
			int startNum = 0;
			int endNum = pageSize*4-1;
			if(pageNum > 1){
				startNum = pageSize * (pageNum-1) * 4;
				endNum = startNum + pageSize*4 -1 ;
			}
			
//			for(int i=startNum; i<vec.size(); i+=4){
			for(int i=startNum; i<vec.size(); i+=2){
				if(i >= endNum){
					break;
				}
//				result += "{\"工号\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
//						"\"姓名\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
//						"\"层级\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"},";
				result += "{\"工号\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
						"\"姓名\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"},";
			}
			
			result = result.substring(0, result.length()-1);
		}
		
		result += "]}";
			
		return result;
	}
	
	/**
	 * 检查周次可用情况信息（手动调整课表更改课程信息页面）
	 * @date:2016-01-21
	 * @author:yeq
	 * @param xqzc 学期周次
	 * @throws SQLException
	 */
	public String checkWeekUsable(String xqzc) throws SQLException{
		String result = "";
		Vector vec = null;
		Vector tempVec = new Vector();
		String sql = "";
		String specialCode = MyTools.getProp(request, "Base.specialCode");
		
		if("".equalsIgnoreCase(this.getPT_SKJSBH())){
			this.setPT_SKJSBH("null");
		}
		if("".equalsIgnoreCase(this.getPT_CDBH())){
			this.setPT_CDBH("null");
		}
		
		String teaArray[] = this.getPT_SKJSBH().split(",");
		//String siteArray[] = this.getPT_CDBH().split(",");
		Vector siteVec = new Vector();
		
		//过滤不需要判断的场地（其他类型）
		sql = "select 教室编号 from V_教室数据类 where 教室类型代码 not in ('" + specialCode.replaceAll(",", "','") + "') " +
			"and 教室编号 in ('" + this.getPT_CDBH().replaceAll(",", "','") + "')";
		siteVec = db.GetContextVector(sql);
		
		sql = "select distinct * from (select 行政班名称,课程名称,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,授课周次详情 from V_排课管理_课程表明细详情表 " +
			"where 学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
			"and 时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' " +
			"and 行政班代码<>'" + MyTools.fixSql(this.getPT_XZBDM()) + "' ";
		for(int i=0; i<teaArray.length; i++){
			if(i == 0)
				sql += "and (";
			else
				sql += " or ";
			sql += "授课教师编号 like '%" + MyTools.fixSql(teaArray[i]) + "%'";
		}
		for(int i=0; i<siteVec.size(); i++){
			sql += " or 实际场地编号 like '%" + MyTools.fixSql(MyTools.StrFiltr(siteVec.get(i))) + "%'";
		}
		sql += ") " +
			"union all " +
			"select c.班级名称,b.课程名称,b.授课教师编号,b.授课教师姓名,a.预设场地编号,a.预设场地名称,b.授课周次详情 from V_规则管理_固排禁排表 a " +
			"inner join V_规则管理_授课计划明细表 b on b.授课计划明细编号=a.授课计划明细编号 " +
			//"left join V_学校班级_数据子类 c on c.行政班代码=a.行政班代码 " +
			"left join V_基础信息_班级信息表 c on c.班级编号=a.行政班代码 " +
			"where a.学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
			"and a.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' " +
			"and a.编号 not in (select distinct b.编号 from V_排课管理_课程表明细详情表 a " +
			"inner join V_规则管理_固排禁排表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' and a.时间序列=b.时间序列 " +
			"where b.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "') " +
			"and a.行政班代码<>'" + MyTools.fixSql(this.getPT_XZBDM()) + "' " +
			"and (b.节数-b.实际已排节数)>0 "; //20160901/yeq添加条件，避免发生在排课设置课表中更换周次后，该周次无法被选择
		for(int i=0; i<teaArray.length; i++){
			if(i == 0)
				sql += "and (";
			else
				sql += " or ";
			sql += "b.授课教师编号 like '%" + MyTools.fixSql(teaArray[i]) + "%'";
		}
		for(int i=0; i<siteVec.size(); i++){
			sql += " or a.预设场地编号 like '%" + MyTools.fixSql(MyTools.StrFiltr(siteVec.get(i))) + "%'";
		}
		sql += ")) as t";
		vec = db.GetContextVector(sql);
		
		result = "[";
		if(vec!=null && vec.size()>0){
			String xzbmc = "";
			String kcmcArray[] = new String[0];
			String jsbhArray[] = new String[0];
			String tempJsbhArray[] = new String[0];
			String curJsbhArray[] = new String[0];
			String jsmcArray[] = new String[0];
			String tempJsmcArray[] = new String[0];
			String curJsmcArray[] = new String[0];
			String cdbhArray[] = new String[0];
			String tempCdbhArray[] = new String[0];
			String curCdbhArray[] = new String[0];
			String cdmcArray[] = new String[0];
			String tempCdmcArray[] = new String[0];
			String curCdmcArray[] = new String[0];
			String skzcArray[] = new String[0];
			String tempSkzcArray[] = new String[0];
			
			for(int i=0; i<vec.size(); i+=7){
				xzbmc = MyTools.StrFiltr(vec.get(i));
				kcmcArray = MyTools.StrFiltr(vec.get(i+1)).split("｜");
				jsbhArray = MyTools.StrFiltr(vec.get(i+2)).split("｜");
				jsmcArray = MyTools.StrFiltr(vec.get(i+3)).split("｜");
				cdbhArray = MyTools.StrFiltr(vec.get(i+4)).split("｜");
				cdmcArray = MyTools.StrFiltr(vec.get(i+5)).split("｜");
				skzcArray = MyTools.StrFiltr(vec.get(i+6)).split("｜");
				
				for(int j=0; j<kcmcArray.length; j++){
					tempJsbhArray = jsbhArray[j].split("&");
					tempJsmcArray = jsmcArray[j].split("&");
					tempCdbhArray = cdbhArray[j].split("&");
					tempCdmcArray = cdmcArray[j].split("&");
					tempSkzcArray = skzcArray[j].split("&");
					
					for(int k=0; k<tempJsbhArray.length; k++){
						curJsbhArray = tempJsbhArray[k].split("[+]");
						curJsmcArray = tempJsmcArray[k].split("[+]");
						
						for(int a=0; a<curJsbhArray.length; a++){
							for(int b=0; b<teaArray.length; b++){
								if(curJsbhArray[a].indexOf(teaArray[b]) > -1){
									tempVec.add(curJsmcArray[a]);
									tempVec.add(xzbmc);
									tempVec.add(kcmcArray[j]);
									tempVec.add(this.formatSkzc(tempSkzcArray[k], xqzc));
								}
							}
						}
					}
					
					for(int k=0; k<tempJsbhArray.length; k++){
						curCdbhArray = tempCdbhArray[k].split("[+]");
						curCdmcArray = tempCdmcArray[k].split("[+]");
						
						for(int a=0; a<curCdbhArray.length; a++){
							for(int b=0; b<siteVec.size(); b++){
								if(curCdbhArray[a].indexOf(MyTools.StrFiltr(siteVec.get(b))) > -1){
									tempVec.add(curCdmcArray[a]);
									tempVec.add(xzbmc);
									tempVec.add(kcmcArray[j]);
									tempVec.add(this.formatSkzc(tempSkzcArray[k], xqzc));
								}
							}
						}
					}
				}
			}
			
			Vector tempWeekVec = new Vector();
			String tempTips = "";
			
			for(int i=1; i<Integer.parseInt(xqzc)+1; i++){
				tempTips = "";
				
				for(int j=0; j<tempVec.size(); j+=4){
					tempWeekVec = (Vector)tempVec.get(j+3);
					
					if(tempWeekVec.indexOf(MyTools.StrFiltr(i)) > -1){
						tempTips += MyTools.StrFiltr(tempVec.get(j)) + " 已安排" + MyTools.StrFiltr(tempVec.get(j+1)) + "上" + MyTools.StrFiltr(tempVec.get(j+2)) + ",";
					}
				}
				if(tempTips.length() > 0){
					result += "{\"weekNum\":\"" + i + "\",\"tips\":\"" + tempTips.substring(0, tempTips.length()-1) + "\"},";
				}
			}
		}
		
		if(result.length() > 1){
			result = result.substring(0, result.length()-1);
		}
		result += "]";
		
		return result;
	}
	
	/**
	 * 查询可用教室列表（手动调整课表更改课程信息页面）
	 * @date:2016-01-15
	 * @author:yeq
	 * @param CDBH_CX 场地编号
	 * @param CDMC_CX 场地名称
	 * @param CDLX_CX 场地类型
	 * @param xqzc 学期周次
	 * @throws SQLException
	 */
	public String loadSiteList(int pageNum, int pageSize, String CDBH_CX, String CDMC_CX, String CDLX_CX,String xqzc) throws SQLException{
		Vector vec = null;
		Vector tempVec = null;
		Vector hbInfoVec = null;
		String sql = "";
		String result = "[";
		
		//获取所有场地信息
		sql = "select * from (select distinct top 100 percent a.教室类型代码,b.名称,a.教室编号,a.教室名称,a.实际容量 from V_教室数据类 a " +
			"left join V_基础信息_教室类型 b on b.编号=a.教室类型代码 " +
			"where a.教室编号 in ('" + this.getPT_CDBH().replaceAll("[+]", "','") + "') order by a.教室类型代码,a.实际容量) as t1 " +
			"union all " +
			"select * from (select distinct top 100 percent a.教室类型代码,b.名称,a.教室编号,a.教室名称,a.实际容量 from V_教室数据类 a " +
			"left join V_基础信息_教室类型 b on b.编号=a.教室类型代码 " +
			"where a.是否可用='1' and a.教室编号 not in ('" + this.getPT_CDBH().replaceAll("[+]", "','") + "')";
		//判断查询条件
		if(!"".equalsIgnoreCase(CDBH_CX)){
			sql += " and a.教室编号 like '%" + MyTools.fixSql(CDBH_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(CDMC_CX)){
			sql += " and a.教室名称 like '%" + MyTools.fixSql(CDMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(CDLX_CX)){
			sql += " and a.教室类型代码='" + MyTools.fixSql(CDLX_CX) + "'";
		}
		sql += " order by a.教室类型代码,a.实际容量) as t2";
		vec = db.GetContextVector(sql);
		
		//拼接datagrid数据
		result = "{\"total\":" + vec.size()/5+ ",\"rows\":[";
		
		if(vec!=null && vec.size()>0){
			//获取已使用的场地信息
			sql = "select distinct * from (select a.教室编号,b.授课计划明细编号,b.实际场地编号,b.授课周次详情 " +
				//"replace(stuff((select '｜'+t.授课周次 from V_排课管理_课程表明细详情表 t where t.课程表明细编号=b.课程表明细编号 order by (case t.授课周次 when 'odd' then '1' when 'even' then '2' else t.授课周次 end) for XML PATH('')),1,1,''),'&amp;','&') as 授课周次 " +
				"from V_教室数据类 a left join V_排课管理_课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%' " +
				//"left join V_基础信息_教室类型 c on c.编号=a.教室类型代码 " +
				"where b.学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
				"and b.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' " +
				"and b.课程表明细编号<>'" + MyTools.fixSql(this.getPK_KCBMXBH()) + "' " +
				"union all " +
				"select a.教室编号,c.授课计划明细编号,c.预设场地编号," +
				"replace(stuff((select '｜'+t.授课周次详情 from V_规则管理_授课计划明细表 t where t.状态='1' and t.授课计划明细编号=b.授课计划明细编号 for XML PATH('')),1,1,''),'&amp;','&') as 授课周次详情 from V_教室数据类 a " +
				"inner join V_规则管理_授课计划明细表 b on b.场地要求 like '%'+a.教室编号+'%' " +
				"inner join V_规则管理_固排禁排表 c on c.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
				"where b.状态='1' and c.学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
				"and c.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' " +
				"and b.授课计划明细编号<>'" + this.getPK_SKJHMXBH() + "' " +
				"and (b.节数-b.实际已排节数)>0" + //20160901/yeq添加条件，避免发生在排课设置课表中更换教室后，该教室无法被使用
				") z";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				//查询合班信息
				sql = "select 授课计划明细编号 from V_规则管理_合班表 " +
					"where 授课计划明细编号 like '%" + MyTools.fixSql(this.getPK_SKJHMXBH()) + "%'";
				hbInfoVec = db.GetContextVector(sql);
				
				String curSiteCode = "";
				String tempSiteCode = "";
				String codeArray[] = new String[0];
				String siteArray[] = new String[0];
				String skzcArray[] = new String[0];
				String tempSiteArray[] = new String[0];
				String tempSkzcArray[] = new String[0];
				Vector selSkzc = this.formatSkzc(this.getPT_SKZC(), xqzc);
				
				boolean hbFlag = false;
				String tempHbInfo = "";
				
				//过滤掉不可用场地
				for(int i=0; i<vec.size(); i+=5){
					curSiteCode = MyTools.StrFiltr(vec.get(i+2));
					
					outer:for(int j=0; j<tempVec.size(); j+=4){
						tempSiteCode = MyTools.StrFiltr(tempVec.get(j));//场地编号
						
						//判断场地编号是否相同
						if(curSiteCode.equalsIgnoreCase(tempSiteCode)){
							codeArray = MyTools.StrFiltr(tempVec.get(j+1)).split("｜");//授课计划明细编号
							siteArray = MyTools.StrFiltr(tempVec.get(j+2)).split("｜");//预设场地编号
							skzcArray = MyTools.StrFiltr(tempVec.get(j+3)).split("｜");//授课周次
							
							for(int a=0; a<codeArray.length; a++){
								tempSiteArray = siteArray[a].split("&");
								tempSkzcArray = skzcArray[a].split("&");
								
								for(int b=0; b<tempSiteArray.length; b++){
									if(tempSiteArray[b].indexOf(tempSiteCode) > -1){
										//判断是否合班课程
										for(int c=0; c<hbInfoVec.size(); c++){
											tempHbInfo = MyTools.StrFiltr(hbInfoVec.get(c));
											
											if(tempHbInfo.indexOf(this.getPK_SKJHMXBH())>-1 && tempHbInfo.indexOf(codeArray[a])>-1){
												hbFlag = true;
												break;
											}
										}
										
										//如果不是合班课程的话，判断授课周次是否冲突，如果冲突，去除当前遍历的场地
										if(hbFlag == false){
											if(this.judgeSkzc(selSkzc, this.formatSkzc(tempSkzcArray[b], xqzc))){
												for(int d=0; d<5; d++){
													vec.remove(i);
												}
												i-=5;
												break outer;
											}
										}else{
											hbFlag = false;
										}
									}
								}
							}
						}
					}
				}
			}
			
			int startNum = 0;
			int endNum = pageSize*5-1;
			if(pageNum > 1){
				startNum = pageSize * (pageNum-1) * 5;
				endNum = startNum + pageSize*5 -1 ;
			}
			
			for(int i=startNum; i<vec.size(); i+=5){
				if(i >= endNum){
					break;
				}
				result += "{\"教室编号\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
						"\"教室名称\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
						"\"教室容量\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "人\"," +
						"\"教室类型\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"},";
			}
			result = result.substring(0, result.length()-1);
		}
		
		result += "]}";
		
		return result;
	}
	
	/**
	 * 查询可用教室列表（手动调整课表更改课程信息页面）
	 * @date:2017-07-01
	 * @author:yeq
	 * @param CDBH_CX 场地编号
	 * @param CDMC_CX 场地名称
	 * @param CDLX_CX 场地类型
	 * @param xqzc 学期周次
	 * @param bjbh 班级编号
	 * @throws SQLException
	 */
	public String loadSiteList2(int pageNum, int pageSize, String CDBH_CX, String CDMC_CX, String CDLX_CX,String xqzc, String bjbh) throws SQLException{
		Vector vec = null;
		Vector tempVec = null;
		Vector hbInfoVec = null;
		String sql = "";
		String result = "[";
		
		//===========================================================================================================
		//查询当前班级的相关系部
		String xbdm = "";//系部代码
		//sql = " select 系部代码 from (select 行政班代码,系部代码 from V_学校班级_数据子类 union all select 教学班编号,系部代码 from V_基础信息_教学班信息表) t where 行政班代码 = '" + MyTools.fixSql(bjbh) + "'";
		sql = " select 系部代码 from V_基础信息_班级信息表 where 班级编号='" + MyTools.fixSql(bjbh) + "'";
		vec = db.GetContextVector(sql);
		xbdm = MyTools.StrFiltr(vec.get(0));
		
		Vector vec2 = null;
		sql = " select COUNT(*) as 授课总周次 from dbo.V_规则管理_学期周次表 where 学年学期编码 = '" + MyTools.fixSql(this.getPT_XNXQBM()) + "'";
		vec2 = db.GetContextVector(sql);
		String maxSkzc = MyTools.StrFiltr(vec2.get(0)); //学期最大授课周次

		//============================================================================================================
		
		//获取所有场地信息
		sql = "select * from (select distinct top 100 percent a.教室类型代码,b.名称,a.教室编号,a.教室名称,a.实际容量 from V_教室数据类 a " +
			"left join V_基础信息_教室类型 b on b.编号=a.教室类型代码 " +
			"where a.教室编号 in ('" + this.getPT_CDBH().replaceAll("[+]", "','") + "') order by a.教室类型代码,a.实际容量) as t1 " +
			"union all " +
			"select * from (select distinct top 100 percent a.教室类型代码,b.名称,a.教室编号,a.教室名称,a.实际容量 from V_教室数据类 a " +
			"left join V_基础信息_教室类型 b on b.编号=a.教室类型代码 " +
			"where a.是否可用='1' and a.教室编号 not in ('" + this.getPT_CDBH().replaceAll("[+]", "','") + "')";
		sql += " and a.校区代码 = '" + MyTools.fixSql(xbdm) + "'";
		//判断查询条件
		if(!"".equalsIgnoreCase(CDBH_CX)){
			sql += " and a.教室编号 like '%" + MyTools.fixSql(CDBH_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(CDMC_CX)){
			sql += " and a.教室名称 like '%" + MyTools.fixSql(CDMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(CDLX_CX)){
			sql += " and a.教室类型代码='" + MyTools.fixSql(CDLX_CX) + "'";
		}
		sql += " order by a.教室类型代码,a.实际容量) as t2";
		vec = db.GetContextVector(sql);
		
		//拼接datagrid数据
		result = "{\"total\":" + vec.size()/5+ ",\"rows\":[";
		
		if(vec!=null && vec.size()>0){
			//获取已使用的场地信息
			sql = "select distinct * from (select a.教室编号,b.授课计划明细编号,b.实际场地编号,b.授课周次详情 " +
				//"replace(stuff((select '｜'+t.授课周次 from V_排课管理_课程表明细详情表 t where t.课程表明细编号=b.课程表明细编号 order by (case t.授课周次 when 'odd' then '1' when 'even' then '2' else t.授课周次 end) for XML PATH('')),1,1,''),'&amp;','&') as 授课周次 " +
				"from V_教室数据类 a left join V_排课管理_课程表明细详情表 b on b.实际场地编号 like '%'+a.教室编号+'%' " +
				//"left join V_基础信息_教室类型 c on c.编号=a.教室类型代码 " +
				"where b.学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
				"and b.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' " +
				"and b.课程表明细编号<>'" + MyTools.fixSql(this.getPK_KCBMXBH()) + "' " +
				"and a.校区代码='" + MyTools.fixSql(xbdm) + "' " +
				"union all " +
				//场地固排
				"select a.教室编号,c.授课计划明细编号,c.预设场地编号," +
				"replace(stuff((select '｜'+t.授课周次详情 from V_规则管理_授课计划明细表 t where t.状态='1' and t.授课计划明细编号=b.授课计划明细编号 for XML PATH('')),1,1,''),'&amp;','&') as 授课周次详情 from V_教室数据类 a " +
				"inner join V_规则管理_授课计划明细表 b on b.场地要求 like '%'+a.教室编号+'%' " +
				"inner join V_规则管理_固排禁排表 c on c.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
				"where b.状态='1' and c.学年学期编码 like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
				"and c.时间序列='" + MyTools.fixSql(this.getPT_SJXL()) + "' and c.类型='2'" +
				"and b.授课计划明细编号<>'" + this.getPK_SKJHMXBH() + "' " +
				"and a.校区代码='" + MyTools.fixSql(xbdm) + "' " +
				"and (b.节数-b.实际已排节数)>0 " + //20160901/yeq添加条件，避免发生在排课设置课表中更换教室后，该教室无法被使用
				//场地禁排信息
				"union all " +
				"select 行政班代码 as 教室编号,'场地禁排' as 授课计划明细编号,行政班代码 as 实际场地编号,'1-" + MyTools.fixSql(maxSkzc) + "' as 授课周次详情 from dbo.V_规则管理_固排禁排表 where 时间序列 = '" + MyTools.fixSql(this.getPT_SJXL()) + "' and 类型='3' and 禁排类型 ='cd' " +
				"and 学年学期编码  like '" + MyTools.fixSql(this.getPT_XNXQBM().substring(0, 5)) + "%' " +
				"and 行政班代码 in ( select 教室编号 from V_教室数据类 where 校区代码 = '" + MyTools.fixSql(xbdm) + "') " +
				") z";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				//查询合班信息
				sql = "select 授课计划明细编号 from V_规则管理_合班表 " +
					"where 授课计划明细编号 like '%" + MyTools.fixSql(this.getPK_SKJHMXBH()) + "%'";
				hbInfoVec = db.GetContextVector(sql);
				
				String curSiteCode = "";
				String tempSiteCode = "";
				String codeArray[] = new String[0];
				String siteArray[] = new String[0];
				String skzcArray[] = new String[0];
				String tempSiteArray[] = new String[0];
				String tempSkzcArray[] = new String[0];
				Vector selSkzc = this.formatSkzc(this.getPT_SKZC(), xqzc);
				
				boolean hbFlag = false;
				String tempHbInfo = "";
				
				//过滤掉不可用场地
				for(int i=0; i<vec.size(); i+=5){
					curSiteCode = MyTools.StrFiltr(vec.get(i+2));
					
					outer:for(int j=0; j<tempVec.size(); j+=4){
						tempSiteCode = MyTools.StrFiltr(tempVec.get(j));//场地编号
						
						//判断场地编号是否相同
						if(curSiteCode.equalsIgnoreCase(tempSiteCode)){
							codeArray = MyTools.StrFiltr(tempVec.get(j+1)).split("｜");//授课计划明细编号
							siteArray = MyTools.StrFiltr(tempVec.get(j+2)).split("｜");//预设场地编号
							skzcArray = MyTools.StrFiltr(tempVec.get(j+3)).split("｜");//授课周次
							
							for(int a=0; a<codeArray.length; a++){
								tempSiteArray = siteArray[a].split("&");
								tempSkzcArray = skzcArray[a].split("&");
								
								for(int b=0; b<tempSiteArray.length; b++){
									if(tempSiteArray[b].indexOf(tempSiteCode) > -1){
										//判断是否合班课程
										for(int c=0; c<hbInfoVec.size(); c++){
											tempHbInfo = MyTools.StrFiltr(hbInfoVec.get(c));
											
											if(tempHbInfo.indexOf(this.getPK_SKJHMXBH())>-1 && tempHbInfo.indexOf(codeArray[a])>-1){
												hbFlag = true;
												break;
											}
										}
										
										//如果不是合班课程的话，判断授课周次是否冲突，如果冲突，去除当前遍历的场地
										if(hbFlag == false){
											if(this.judgeSkzc(selSkzc, this.formatSkzc(tempSkzcArray[b], xqzc))){
												for(int d=0; d<5; d++){
													vec.remove(i);
												}
												i-=5;
												break outer;
											}
										}else{
											hbFlag = false;
										}
									}
								}
							}
						}
					}
				}
			}
			
			int startNum = 0;
			int endNum = pageSize*5-1;
			if(pageNum > 1){
				startNum = pageSize * (pageNum-1) * 5;
				endNum = startNum + pageSize*5 -1 ;
			}
			
			for(int i=startNum; i<vec.size(); i+=5){
				if(i >= endNum){
					break;
				}
				result += "{\"教室编号\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
						"\"教室名称\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
						"\"教室容量\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "人\"," +
						"\"教室类型\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"},";
			}
			result = result.substring(0, result.length()-1);
		}
		
		result += "]}";
		
		return result;
	}
	
	/**
	 * 解析多个授课周次信息
	 * @date:2016-01-19
	 * @param skzc 授课周次
	 * @param xqzc 学期周次范围
	 * @return String
	 * @author:yeq
	 */
	public String parseAllSkzc(String skzc, int xqzc){
		String result = "";
		String tempResult = "";
		Vector resultVec = new Vector();
		Vector tempVec = new Vector();
		String skzcArray[] = skzc.split("｜");
		String tempSkzcArray[] = new String[0];
		String tempSkzc = "";
		
		Vector oddVec = new Vector();
		for(int i=1; i<xqzc+1; i+=2){
			oddVec.add(i);
		}
		
		Vector evenVec = new Vector();
		for(int i=2; i<xqzc+1; i+=2){
			evenVec.add(i);
		}
		
		for(int i=0; i<skzcArray.length; i++){
			resultVec = new Vector();
			tempSkzcArray = skzcArray[i].split("&");
			
			for(int j=0; j<tempSkzcArray.length; j++){
				tempSkzc = tempSkzcArray[j];
				tempVec = new Vector();
				
				//判断授课周次是连续周次，还是特定周次（如单双周）
				//连续周次格式,如:1-18。
				//单双周，如:odd单、even双。
				//特定周次格式,如：1#4#7#9
				if(tempSkzc.indexOf("-") > -1){
					int tempStart = MyTools.StringToInt(tempSkzc.split("-")[0]);
					int tempEnd = MyTools.StringToInt(tempSkzc.split("-")[1]);
					
					for(int k=tempStart; k<tempEnd+1; k++){
						tempVec.add(k);
					}
				}else if("odd".equalsIgnoreCase(tempSkzc) || "even".equalsIgnoreCase(tempSkzc)){//单双周
					int tempNum = "odd".equalsIgnoreCase(tempSkzc)?1:2;
					
					for(int k=tempNum; k<xqzc+1; k+=2){
						tempVec.add(k);
					}
				}else{//特别指定的周次
					String tempArray[] = tempSkzc.split("#");
					
					for(int k=0; k<tempArray.length; k++){
						tempVec.add(tempArray[k]);
					}
				}
				
				if(resultVec.size() > 0){
					int tempNum = 0;
					boolean insertFlag = false;
					
					for(int a=0; a<tempVec.size(); a++){
						tempNum = MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(a)));
						insertFlag = false;
						
						for(int b=0; b<resultVec.size(); b++){
							if(tempNum < MyTools.StringToInt(MyTools.StrFiltr(resultVec.get(b)))){
								resultVec.add(b, tempNum);
								insertFlag = true;
								break;
							}
						}
						
						if(insertFlag == false){
							resultVec.add(tempNum);
						}
					}
				}else{
					resultVec.addAll(tempVec);
				}
			}
			
			boolean oddFlag = true;
			if(oddVec.size() == resultVec.size()){
				for(int j=0; j<oddVec.size(); j++){
					if(!MyTools.StrFiltr(oddVec.get(j)).equalsIgnoreCase(MyTools.StrFiltr(resultVec.get(j)))){
						oddFlag = false;
						break;
					}
				}
			}else{
				oddFlag = false;
			}
			
			boolean evenFlag = true;
			if(evenVec.size() == resultVec.size()){
				for(int j=0; j<evenVec.size(); j++){
					if(!MyTools.StrFiltr(evenVec.get(j)).equalsIgnoreCase(MyTools.StrFiltr(resultVec.get(j)))){
						evenFlag = false;
						break;
					}
				}
			}else{
				evenFlag = false;
			}
			
			if(oddFlag == true){
				tempResult = "odd";
			}else if(evenFlag == true){
				tempResult = "even";
			}else{
				boolean flag = true;
				
				for(int j=0; j<resultVec.size(); j++){
					tempResult += MyTools.StrFiltr(resultVec.get(j))+"#";
					if(j < resultVec.size()-1){
						if(MyTools.StringToInt(MyTools.StrFiltr(resultVec.get(j)))+1 != MyTools.StringToInt(MyTools.StrFiltr(resultVec.get(j+1)))){
							flag = false;
						}
					}
				}
				
				if(resultVec.size()>1 && flag==true){
					tempResult = MyTools.StrFiltr(resultVec.get(0)) + "-" + MyTools.StrFiltr(resultVec.get(resultVec.size()-1));
				}else{
					tempResult = tempResult.substring(0, tempResult.length()-1);
				}
			}
			
			result += tempResult + "｜";
		}
		
		result = result.substring(0, result.length()-1);
		return result;
	}
	
	/**
	 * 保存更改的课程信息
	 * @date:2015-08-02
	 * @author:yeq
	 * @param 当前学期的周次数
	 * @return String 授课周次
	 * @throws SQLException
	 */
	public String saveCourseInfo(int xqzc) throws SQLException{
		Vector vec = null;
		Vector sqlVec = new Vector();
		String sql = "";
		String sqlCondition = "";//查询条件
		//String pubTeacher = MyTools.getProp(request, "Base.pubTeacher");//公共课
		Vector tempVec = null;
		Vector classVec = new Vector();
		
		String selSkjhbhArray[] = this.getPK_SKJHMXBH().split("｜");
		String skzc = this.parseAllSkzc(this.getPT_SKZC(), xqzc);
		
		for(int i=0; i<selSkjhbhArray.length; i++){
			sqlCondition += "授课计划明细编号 like '%" + MyTools.fixSql(selSkjhbhArray[i]) + "%' or ";
		}
		sqlCondition = sqlCondition.substring(0, sqlCondition.length()-4);
		
		//查询相关的合班设置
		sql = "select 授课计划明细编号 from V_规则管理_合班表 where " + sqlCondition;
		vec = db.GetContextVector(sql);
		
		//获取所有需要更新课表的班级编号
		sql = "select distinct 学年学期编码,行政班代码 from V_排课管理_课程表明细详情表 where " + sqlCondition;
		classVec = db.GetContextVector(sql);
		
		//判断如果设置了合班，需要更新所有相关场地信息
		if(vec!=null && vec.size()>0){
			sqlCondition = "";
			String selSkjsbhArray[] = this.getPT_SKJSBH().split("｜");
			String selSkjsmcArray[] = this.getPT_SKJSMC().split("｜");
			String selCdbhArray[] = this.getPT_CDBH().split("｜");
			String selCdmcArray[] = this.getPT_CDMC().split("｜");
			String selSkzcAllArray[] = skzc.split("｜");
			String selSkzcDetailArray[] = this.getPT_SKZC().split("｜");
			
			String hbSkjhbhArray[] = new String[0];
			String hbTeaCode = "";
			String hbTeaName = "";
			String hbSiteCode = "";
			String hbSiteName = "";
			String hbSkzcAll = "";
			String hbSkzcDetail = "";
			String hbSkjhbh = "";
			Vector hbInfoVec = new Vector();
			
			String code = "";
			String skjhbhArray[] = new String[0];
			String skjsbhArray[] = new String[0];
			String skjsmcArray[] = new String[0];
			String cdbhArray[] = new String[0];
			String cdmcArray[] = new String[0];
			String skzcAllArray[] = new String[0];
			String skzcDetailArray[] = new String[0];
			String teaCode = "";
			String teaName = "";
			String siteCode = "";
			String siteName = "";
			String skzcAll = "";
			String skzcDetail = "";
			
			//整合合班课程更换场地信息
			for(int i=0; i<vec.size(); i++){
				hbSkjhbh = MyTools.StrFiltr(vec.get(i));
				hbSkjhbhArray = hbSkjhbh.split("\\+");
				
				for(int j=0; j<selSkjhbhArray.length; j++){
					if(hbSkjhbh.indexOf(selSkjhbhArray[j]) > -1){
						hbInfoVec.add(hbSkjhbh);
						hbInfoVec.add(selSkjsbhArray[j]);
						hbInfoVec.add(selSkjsmcArray[j]);
						hbInfoVec.add(selCdbhArray[j]);
						hbInfoVec.add(selCdmcArray[j]);
						hbInfoVec.add(selSkzcAllArray[j]);
						hbInfoVec.add(selSkzcDetailArray[j]);
					}
				}
				
				for(int j=0; j<hbSkjhbhArray.length; j++){
					if(this.getPK_SKJHMXBH().indexOf(hbSkjhbhArray[j]) < 0){
						sqlCondition += "授课计划明细编号 like '%" + MyTools.fixSql(hbSkjhbhArray[j]) + "%' or ";
					}
				}
			}
			sqlCondition = sqlCondition.substring(0, sqlCondition.length()-4);
			
			//获取所有需要更新课表的班级编号
			sql = "select distinct 学年学期编码,行政班代码 from V_排课管理_课程表明细详情表 where " + sqlCondition;
			tempVec = db.GetContextVector(sql);
			for(int i=0; i<tempVec.size(); i+=2){
				if(classVec.indexOf(MyTools.StrFiltr(tempVec.get(i+1))) < 0){
					classVec.add(tempVec.get(i));
					classVec.add(tempVec.get(i+1));
				}
			}
			
			//查询所有相关单元格
			sql = "select distinct 课程表明细编号,授课计划明细编号,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,授课周次,授课周次详情 from V_排课管理_课程表明细详情表 " +
				"where 时间序列='" + MyTools.fixSql(this.getPK_SJXL()) + "' and (" + sqlCondition + ")"; 
			vec = db.GetContextVector(sql);
			
			for(int i=0; i<vec.size(); i+=8){
				code = MyTools.StrFiltr(vec.get(i));
				skjhbhArray = MyTools.StrFiltr(vec.get(i+1)).split("｜");
				skjsbhArray = MyTools.StrFiltr(vec.get(i+2)).split("｜");
				skjsmcArray = MyTools.StrFiltr(vec.get(i+3)).split("｜");
				cdbhArray = MyTools.StrFiltr(vec.get(i+4)).split("｜");
				cdmcArray = MyTools.StrFiltr(vec.get(i+5)).split("｜");
				skzcAllArray = MyTools.StrFiltr(vec.get(i+6)).split("｜");
				skzcDetailArray = MyTools.StrFiltr(vec.get(i+7)).split("｜");
				teaCode = "";
				teaName = "";
				siteCode = "";
				siteName = "";
				skzcAll = "";
				skzcDetail = "";
				
				for(int j=0; j<skjhbhArray.length; j++){
					hbTeaCode = "";
					hbTeaName = "";
					hbSiteCode = "";
					hbSiteName = "";
					hbSkzcAll = "";
					hbSkzcDetail = "";
					
					for(int k=0; k<hbInfoVec.size(); k+=7){
						hbSkjhbh = MyTools.StrFiltr(hbInfoVec.get(k));
						
						if(hbSkjhbh.indexOf(skjhbhArray[j]) > -1){
							hbTeaCode = MyTools.StrFiltr(hbInfoVec.get(k+1));
							hbTeaName = MyTools.StrFiltr(hbInfoVec.get(k+2));
							hbSiteCode = MyTools.StrFiltr(hbInfoVec.get(k+3));
							hbSiteName = MyTools.StrFiltr(hbInfoVec.get(k+4));
							hbSkzcAll = MyTools.StrFiltr(hbInfoVec.get(k+5));
							hbSkzcDetail = MyTools.StrFiltr(hbInfoVec.get(k+6));
						}
					}
					
					if(!"".equalsIgnoreCase(hbSiteCode)){
						teaCode += hbTeaCode+"｜";
						teaName += hbTeaName+"｜";
						siteCode += hbSiteCode+"｜";
						siteName += hbSiteName+"｜";
						skzcAll += hbSkzcAll+"｜";
						skzcDetail += hbSkzcDetail+"｜";
					}else{
						teaCode += skjsbhArray[j]+"｜";
						teaName += skjsmcArray[j]+"｜";
						siteCode += cdbhArray[j]+"｜";
						siteName += cdmcArray[j]+"｜";
						skzcAll += skzcAllArray[j]+"｜";
						skzcDetail += skzcDetailArray[j]+"｜";
					}
				}
				teaCode = teaCode.substring(0, teaCode.length()-1);
				teaName = teaName.substring(0, teaName.length()-1);
				siteCode = siteCode.substring(0, siteCode.length()-1);
				siteName = siteName.substring(0, siteName.length()-1);
				skzcAll = skzcAll.substring(0, skzcAll.length()-1);
				skzcDetail = skzcDetail.substring(0, skzcDetail.length()-1);
				
				sql = "update V_排课管理_课程表明细表 " +
					"set 实际场地编号='" + MyTools.fixSql(siteCode) + "'," +
					"实际场地名称='" + MyTools.fixSql(siteName) + "' " +
					"where 课程表明细编号='" + MyTools.fixSql(code) + "'";
				sqlVec.add(sql);
				
				sql = "update V_排课管理_课程表明细详情表 set " +
					"授课教师编号='" + MyTools.fixSql(teaCode) + "'," +
					"授课教师姓名='" + MyTools.fixSql(teaName) + "'," +
					"实际场地编号='" + MyTools.fixSql(siteCode) + "'," +
					"实际场地名称='" + MyTools.fixSql(siteName) + "'," +
					"授课周次='" + MyTools.fixSql(skzcAll) + "'," +
					"授课周次详情='" + MyTools.fixSql(skzcDetail) + "' " +
					"where 课程表明细编号='" + MyTools.fixSql(code) + "'";
				sqlVec.add(sql);
				
				//判断是否为公共课
//				if(this.getAuth().indexOf(pubTeacher) > -1){
//					sql = "update V_排课管理_公共课课程表明细表 " +
//						"set 实际场地编号='" + MyTools.fixSql(siteCode) + "'," +
//						"实际场地名称='" + MyTools.fixSql(siteName) + "' " +
//						"where 课程表明细编号='" + MyTools.fixSql(code) + "'";
//					sqlVec.add(sql);
//				}
			}
		}
		
		sql = "update V_排课管理_课程表明细表 " +
			"set 实际场地编号='" + MyTools.fixSql(this.getPT_CDBH()) + "'," +
			"实际场地名称='" + MyTools.fixSql(this.getPT_CDMC()) + "' " +
			"where 课程表明细编号='" + MyTools.fixSql(this.getPK_KCBMXBH()) + "'";
		sqlVec.add(sql);
		
		sql = "update V_排课管理_课程表明细详情表 set " +
			"授课教师编号='" + MyTools.fixSql(this.getPT_SKJSBH()) + "'," +
			"授课教师姓名='" + MyTools.fixSql(this.getPT_SKJSMC()) + "'," +
			"实际场地编号='" + MyTools.fixSql(this.getPT_CDBH()) + "'," +
			"实际场地名称='" + MyTools.fixSql(this.getPT_CDMC()) + "'," +
			"授课周次='" + MyTools.fixSql(skzc) + "'," +
			"授课周次详情='" + MyTools.fixSql(this.getPT_SKZC()) + "' " +
			"where 课程表明细编号='" + MyTools.fixSql(this.getPK_KCBMXBH()) + "'";
		sqlVec.add(sql);
		
		//判断是否为公共课
//		if(this.getAuth().indexOf(pubTeacher) > -1){
//			sql = "update V_排课管理_公共课课程表明细表 " +
//				"set 实际场地编号='" + MyTools.fixSql(this.getPT_CDBH()) + "'," +
//				"实际场地名称='" + MyTools.fixSql(this.getPT_CDMC()) + "' " +
//				"where 课程表明细编号='" + MyTools.fixSql(this.getPK_KCBMXBH()) + "'";
//			sqlVec.add(sql);
//		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
			
			//生成周课表信息
			String xnxqbm = "";
			String xzbdm = "";
			for(int i=0; i<classVec.size(); i+=2){
				xnxqbm = MyTools.StrFiltr(classVec.get(i));
				xzbdm = MyTools.StrFiltr(classVec.get(i+1));
				this.addWeekDetail(xnxqbm, "", xzbdm);
			}
		}else{
			this.setMSG("保存失败");
		}
		
		return skzc;
	}
	
	/**
	 * 获取正在排课的教师的信息
	 * @date:2015-08-02
	 * @author:yeq
	 * @throws SQLException
	 */
	public Vector getTeaTel() throws SQLException{
		Vector vec = new Vector();
		String sql = "select 姓名 from V_教职工基本数据子类 where 工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec==null || vec.size()<=0){
			vec.add("");
		}
		return vec;
	}
	
	/**
	 * 公共课确认
	 * @date:2015-09-23
	 * @author:yeq
	 * @throws SQLException
	 */
	public void pubCourseConfirm() throws SQLException{
		String sql = "update V_排课管理_课程表主表 set 提交状态='1' where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 删除学期课程表相关信息
	 * @date:2015-09-23
	 * @author:yeq
	 * @throws SQLException
	 */
	public void delTimetable() throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
		
		sql = "delete V_排课管理_课程表明细表 where 课程表主表编号 in (select 课程表主表编号 from V_排课管理_课程表主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')";
		sqlVec.add(sql);
		
		sql = "delete V_排课管理_课程表明细详情表 where 课程表主表编号 in (select 课程表主表编号 from V_排课管理_课程表主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')";
		sqlVec.add(sql);
		
//		sql = "delete V_排课管理_公共课课程表明细表 where 课程表主表编号 in (select 课程表主表编号 from V_排课管理_课程表主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')";
//		sqlVec.add(sql);
		
		sql = "delete V_排课管理_课程表主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		sqlVec.add(sql);
		
		sql = "delete V_排课管理_添加课程信息表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		sqlVec.add(sql);
		
		sql = "delete V_调课管理_调课信息明细表 where 调课信息主表编号 in (select 编号 from V_调课管理_调课信息主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')";
		sqlVec.add(sql);
		
		sql = "delete V_调课管理_调课信息主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		sqlVec.add(sql);
		
		sql = "delete V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "'";
		sqlVec.add(sql);
		
		sql = "update V_规则管理_授课计划明细表 set 实际已排节数=0,实际连次次数=0 " +
			"where 授课计划主表编号 in (select 授课计划主表编号 from V_规则管理_授课计划主表 where 学年学期编码='" + MyTools.fixSql(this.getPK_XNXQBM()) + "')";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**查询教师层级下拉框数据
	 * @author 2016-01-14
	 * @author yeq
	 * @return
	 * @throws SQLException
	 */
	public Vector loadTeaLevelCombo() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName " +
			"union all " +
			"select 层级编号 as comboValue,层级名称 as comboName from V_层级表 " +
			"order by comboValue";			
			
		vec = dbSource.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**查询教室类型下拉框数据
	 * @author 2016-01-14
	 * @author yeq
	 * @return
	 * @throws SQLException
	 */
	public Vector loadSiteTypeCombo() throws SQLException {
		Vector vec = null;
		String sql = "select '' as comboValue,'请选择' as comboName " +
			"union all " +
			"select 编号 as comboValue,名称 as comboName from V_基础信息_教室类型 " +
			"order by comboValue";			
			
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**查询学期周次
	 * @author 2016-01-14
	 * @author yeq
	 * @return 学期周次
	 * @throws SQLException
	 */
	public String loadSemesterWeek() throws SQLException {
		Vector vec = null;
		String sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(this.getPT_XNXQBM()) + "'";			
		vec = db.GetContextVector(sql);
		String xqzc = MyTools.StrFiltr(vec.get(0));
		
		return xqzc;
	}
	
	
	//比较周次是否有相同
	public int compareweek(String arrweek,String selweek,String weeks) throws SQLException {
			int result=2;
			
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
	public int compareroom(String arrcdyq,String selcdyq) throws SQLException {
			int result=0;
			
			//System.out.println("场地："+arrcdyq+"---"+selcdyq);	
			if(arrcdyq.length()==1){//场地类型
				
			}else{//具体教室编号
				String[] arrcdyq2=arrcdyq.split("\\&");
				String[] selcdyq2=selcdyq.split("\\&");
				for(int m=0;m<arrcdyq2.length;m++){
					for(int n=0;n<selcdyq2.length;n++){
						String[] arrcdyq3=arrcdyq2[m].split("\\+");
						String[] selcdyq3=selcdyq2[n].split("\\+");
						for(int i=0;i<arrcdyq2.length;i++){
							for(int j=0;j<selcdyq2.length;j++){
								if(arrcdyq2[i].equalsIgnoreCase(selcdyq2[j])){
									if(selcdyq2[j].equals("0000")||selcdyq2[j].equals("0001")||selcdyq2[j].equals("0002")){
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
	
	/**
	 * 生成课程表周详情信息
	 * @param xnxqbm 学年学期编码
	 * @param xqzc 学期周次
	 * @PARAM xzbdm 行政班代码
	 * @throws SQLException
	 */
	public void addWeekDetail(String xnxqbm, String xqzc, String xzbdm) throws SQLException{
		String sql = "";
		String sqlStr = "";
		Vector vec = null;
		Vector tempVec = null;
		Vector kcbVec = new Vector();
		Vector courseInfo = new Vector();
		Vector sqlVec = new Vector();
		
		//删除班级原周课表信息
		sql = "delete from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 行政班代码='" + MyTools.fixSql(xzbdm) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			//判断如果学期周次为空,表示没有传值进来，读取本学期周次数
			if("".equalsIgnoreCase(xqzc)){
				sql = "select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				tempVec = db.GetContextVector(sql);
				xqzc = MyTools.StrFiltr(tempVec.get(0));
			}
			
			//读取课程表信息
			sql = "select 课程表明细编号,课程表主表编号,行政班名称,专业代码,专业名称," +
				"时间序列,授课计划明细编号,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,实际场地编号,实际场地名称,授课周次详情 from V_排课管理_课程表明细详情表 " +
				"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 行政班代码='" + MyTools.fixSql(xzbdm) + "' " +
				"order by 学年学期编码,行政班代码,时间序列";
			vec = db.GetContextVector(sql);
			
			if(vec!=null && vec.size()>0){
				//读取调课信息
				Vector tkInfoVec = new Vector();
				sql = "select a.授课周次,a.时间序列,a.授课计划明细编号,a.课程编号,a.课程名称,case when b.申请类型='1' and a.课程编号<>'' then c.课程类型 else '' end as 课程类型,a.授课教师编号," +
					"a.授课教师姓名,实际场地编号,实际场地名称 from V_调课管理_调课信息明细表 a " +
					"left join V_调课管理_调课信息主表 b on b.编号=a.调课信息主表编号 " +
					"left join V_规则管理_授课计划明细表 c on c.授课计划明细编号=b.授课计划明细编号 " +
					"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and b.班级编号='" + MyTools.fixSql(xzbdm) + "' " +
					"order by cast(a.授课周次 as int),a.时间序列,a.创建时间";
				tkInfoVec = db.GetContextVector(sql);
				boolean tkFlag = false;
				
				//读取添加课程信息
				Vector tjkcInfoVec = new Vector();
				sql = "select distinct b.周次,a.时间序列,'' as 授课计划明细编号,a.课程编号,a.课程名称,'' as 课程类型,a.授课教师编号," +
					"a.授课教师姓名,a.场地编号,a.场地名称 from V_排课管理_添加课程信息表 a " +
					"inner join V_规则管理_学期周次表 b on a.授课周次 like '%'+cast(cast(b.周次 as int) as varchar)+'%' " +
					"where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and a.行政班代码='" + MyTools.fixSql(xzbdm) + "' and a.类型='2'";
				tjkcInfoVec = db.GetContextVector(sql);
				boolean tjkcFlag = false;
				
				String kcbmxbh = "";
				String kcbzbbh = "";
				String xzbmc = "";
				String zydm = "";
				String zymc = "";
				String sjxl = "";
				String skjhmxbh = "";
				String kcdm = "";
				String kcmc = "";
				String kclx = "";
				String skjsbh = "";
				String skjsxm = "";
				String cdbh = "";
				String cdmc = "";
				String skzc = "";
				
				String skjhmxbhArray[] = new String[0];
				String kcdmArray[] = new String[0];
				String kcmcArray[] = new String[0];
				String kclxArray[] = new String[0];
				String skjsbhArray[] = new String[0];
				String skjsxmArray[] = new String[0];
				String cdbhArray[] = new String[0];
				String cdmcArray[] = new String[0];
				String skzcArray[] = new String[0];
				
				String tempSkjsbhArray[] = new String[0];
				String tempSkjsxmArray[] = new String[0];
				String tempCdbhArray[] = new String[0];
				String tempCdmcArray[] = new String[0];
				String tempSkzcArray[] = new String[0];
				
				String tempKcdm = "";
				String tempKcmc = "";
				String tempKclx = "";
				Vector weekVec = null;
				String weekNum = "";
				
				for(int i=0; i<vec.size(); i+=15){
					kcbmxbh = MyTools.StrFiltr(vec.get(i));
					kcbzbbh = MyTools.StrFiltr(vec.get(i+1));
					xzbmc = MyTools.StrFiltr(vec.get(i+2));
					zydm = MyTools.StrFiltr(vec.get(i+3));
					zymc = MyTools.StrFiltr(vec.get(i+4));
					sjxl = MyTools.StrFiltr(vec.get(i+5));
					skjhmxbh = MyTools.StrFiltr(vec.get(i+6));
					kcdm = MyTools.StrFiltr(vec.get(i+7));
					kcmc = MyTools.StrFiltr(vec.get(i+8));
					kclx = MyTools.StrFiltr(vec.get(i+9));
					skjsbh = MyTools.StrFiltr(vec.get(i+10));
					skjsxm = MyTools.StrFiltr(vec.get(i+11));
					cdbh = MyTools.StrFiltr(vec.get(i+12));
					cdmc = MyTools.StrFiltr(vec.get(i+13));
					skzc = MyTools.StrFiltr(vec.get(i+14));
					
					sqlStr = "insert into V_排课管理_课程表周详情表 (课程表明细编号,课程表主表编号,学年学期编码,行政班代码,行政班名称,专业代码," +
							"专业名称,授课周次,时间序列,授课计划明细编号,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,场地编号,场地名称,状态) values(" +
							"'" + MyTools.fixSql(kcbmxbh) + "'," +
							"'" + MyTools.fixSql(kcbzbbh) + "'," +
							"'" + MyTools.fixSql(xnxqbm) + "'," +
							"'" + MyTools.fixSql(xzbdm) + "'," +
							"'" + MyTools.fixSql(xzbmc) + "'," +
							"'" + MyTools.fixSql(zydm) + "'," +
							"'" + MyTools.fixSql(zymc) + "',";
					
					//判断当前时间序列是否有课程
					if("".equalsIgnoreCase(kcdm)){
						for(int j=1; j<MyTools.StringToInt(xqzc)+1; j++){
							weekNum = String.valueOf(j);
							
							//判断是否有调课信息
							tkFlag = false;
							for(int k=0; k<tkInfoVec.size(); k+=10){
								if(MyTools.StrFiltr(tkInfoVec.get(k)).equalsIgnoreCase(weekNum) 
									&& MyTools.StrFiltr(tkInfoVec.get(k+1)).equalsIgnoreCase(sjxl)){
									sql = sqlStr +
										"'" + MyTools.fixSql(weekNum) + "'," +
										"'" + MyTools.fixSql(sjxl) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+2))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+3))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+4))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+5))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+6))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+7))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+8))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+9))) + "'," +
										"'1')";
									sqlVec.add(sql);
									tkFlag = true;
									
									for(int a=0; a<10; a++){
										tkInfoVec.remove(k);
									}
								}
							}
							
							//判断是否有添加的整班课程
							tjkcFlag = false;
							for(int k=0; k<tjkcInfoVec.size(); k+=10){
								if(MyTools.StrFiltr(tjkcInfoVec.get(k)).equalsIgnoreCase(weekNum) 
									&& MyTools.StrFiltr(tjkcInfoVec.get(k+1)).equalsIgnoreCase(sjxl)){
									sql = sqlStr +
										"'" + MyTools.fixSql(weekNum) + "'," +
										"'" + MyTools.fixSql(sjxl) + "'," + 
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+2))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+3))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+4))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+5))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+6))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+7))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+8))) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+9))) + "'," +
										"'1')";
									sqlVec.add(sql);
									tjkcFlag = true;
									
									for(int a=0; a<10; a++){
										tjkcInfoVec.remove(k);
									}
									break;
								}
							}
								
							if(tkFlag==false && tjkcFlag==false){
								sql = sqlStr + 
									"'" + MyTools.fixSql(weekNum) + "'," +
									"'" + MyTools.fixSql(sjxl) + "'," + 
									"'','','','','','','','','1')";
								sqlVec.add(sql);
							}
						}
					}else{
						kcbVec.clear();
						skjhmxbhArray = skjhmxbh.split("｜");
						kcdmArray = kcdm.split("｜");
						kcmcArray = kcmc.split("｜");
						kclxArray = kclx.split("｜");
						skjsbhArray = skjsbh.split("｜");
						skjsxmArray = skjsxm.split("｜");
						cdbhArray = cdbh.split("｜");
						cdmcArray = cdmc.split("｜");
						skzcArray = skzc.split("｜");
						
						for(int j=0; j<skzcArray.length; j++){
							tempSkjsbhArray = skjsbhArray[j].split("&");
							tempSkjsxmArray = skjsxmArray[j].split("&");
							tempCdbhArray = cdbhArray[j].split("&");
							tempCdmcArray = cdmcArray[j].split("&");
							tempSkzcArray = skzcArray[j].split("&");
							
							for(int k=0; k<tempSkjsbhArray.length; k++){
								courseInfo = new Vector();
								courseInfo.add(skjhmxbhArray[j]);
								courseInfo.add(kcdmArray[j]);
								courseInfo.add(kcmcArray[j]);
								courseInfo.add(kclxArray[j]);
								courseInfo.add(tempSkjsbhArray[k]);
								courseInfo.add(tempSkjsxmArray[k]);
								courseInfo.add(tempCdbhArray[k]);
								courseInfo.add(tempCdmcArray[k]);
								
								weekVec = this.formatSkzc(tempSkzcArray[k], xqzc);
								
								for(int a=0; a<weekVec.size(); a++){
									kcbVec.add(MyTools.StrFiltr(weekVec.get(a)));
									kcbVec.add(courseInfo);
								}
							}
						}
						
						for(int j=1; j<MyTools.StringToInt(xqzc)+1; j++){
							weekNum = String.valueOf(j);
							
							if(kcbVec.indexOf(weekNum) < 0){
								//检查调课信息
								tkFlag = false;
								for(int k=0; k<tkInfoVec.size(); k+=10){
									if(MyTools.StrFiltr(tkInfoVec.get(k)).equalsIgnoreCase(weekNum) 
										&& MyTools.StrFiltr(tkInfoVec.get(k+1)).equalsIgnoreCase(sjxl)){
										sql = sqlStr +
											"'" + MyTools.fixSql(weekNum) + "'," +
											"'" + MyTools.fixSql(sjxl) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+2))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+3))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+4))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+5))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+6))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+7))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+8))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+9))) + "'," +
											"'1')";
										sqlVec.add(sql);
										tkFlag = true;
										
										for(int a=0; a<10; a++){
											tkInfoVec.remove(k);
										}
									}
								}
								
								//检查添加的整班课程
								tjkcFlag = false;
								for(int k=0; k<tjkcInfoVec.size(); k+=10){
									if(MyTools.StrFiltr(tjkcInfoVec.get(k)).equalsIgnoreCase(weekNum) 
										&& MyTools.StrFiltr(tjkcInfoVec.get(k+1)).equalsIgnoreCase(sjxl)){
										sql = sqlStr +
											"'" + MyTools.fixSql(weekNum) + "'," +
											"'" + MyTools.fixSql(sjxl) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+2))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+3))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+4))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+5))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+6))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+7))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+8))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tjkcInfoVec.get(k+9))) + "'," +
											"'1')";
										sqlVec.add(sql);
										tjkcFlag = true;
										
										for(int a=0; a<10; a++){
											tjkcInfoVec.remove(k);
										}
										break;
									}
								}
									
								if(tkFlag==false && tjkcFlag==false){
									sql = sqlStr +
										"'" + MyTools.fixSql(weekNum) + "'," +
										"'" + MyTools.fixSql(sjxl) + "'," + 
										"'','','','','','','','','1')";
									sqlVec.add(sql);
								}
							}else{
								//检查调课信息
								tkFlag = false;
								for(int k=0; k<tkInfoVec.size(); k+=10){
									if(MyTools.StrFiltr(tkInfoVec.get(k)).equalsIgnoreCase(weekNum) 
										&& MyTools.StrFiltr(tkInfoVec.get(k+1)).equalsIgnoreCase(sjxl)){
										sql = sqlStr +
											"'" + MyTools.fixSql(weekNum) + "'," +
											"'" + MyTools.fixSql(sjxl) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+2))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+3))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+4))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+5))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+6))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+7))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+8))) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(tkInfoVec.get(k+9))) + "'," +
											"'1')";
										sqlVec.add(sql);
										tkFlag = true;
										
										for(int b=0; b<10; b++){
											tkInfoVec.remove(k);
										}
									}
								}
								
								//判断如果没有调课信息,插入原课程信息
								if(tkFlag == false){
									for(int a=0; a<kcbVec.size(); a+=2){
										if(weekNum.equalsIgnoreCase(MyTools.StrFiltr(kcbVec.get(a)))){
											tempVec = (Vector)kcbVec.get(a+1);
											
											sql = sqlStr +
												"'" + MyTools.fixSql(weekNum) + "'," +
												"'" + MyTools.fixSql(sjxl) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(0))) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(1))) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(2))) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(3))) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(4))) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(5))) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(6))) + "'," +
												"'" + MyTools.fixSql(MyTools.StrFiltr(tempVec.get(7))) + "'," +
												"'1')";
											sqlVec.add(sql);
											
											kcbVec.remove(a);
											kcbVec.remove(a);
											a-=2;
										}
									}
								}
							}
						}
					}
				}
				
				db.executeInsertOrUpdateTransaction(sqlVec);
			}
		}else{
			this.setMSG("删除原数据失败");
		}
	}
	
	public void WriteStringToFile(String contentStr) {
        try {
            File file = new File("e:\\test.txt");
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.println(contentStr);// 往文件里写入字符串
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	
	/**
	 * 拼接课程考虑教师跨系部情况
	 * @param curTimeOrder 时间序列
	 * @param xqzc 学期周次
	 * @param jsxbkcxx 教师已排系部课程信息
	 * @param swVec 下午集合
	 * @param xwVec 上午集合
	 * @param curSkjsbhArray 授课教师编号数组
	 * @param curSkzcDetailArray 授课周次详情数组
	 * 
	 */
	public boolean teaKXB(String curTimeOrder, String xqzc, Vector jsxbkcxx, Vector swVec, Vector xwVec, String[] curSkjsbhArray, String[] curSkzcDetailArray){
		boolean pdtj = false;
		String sxw = "";
		String sjxl_1=curTimeOrder.substring(0, 2);
		String sjxl_2=curTimeOrder.substring(2);
		if(swVec.indexOf(sjxl_2) > -1) {
			sxw = "上午";
		}
		if(xwVec.indexOf(sjxl_2) > -1) {
			sxw = "下午";
		}
		for(int m=0; m<curSkjsbhArray.length; m++){
			String[] tempSkjsbhArray2 = curSkjsbhArray[m].split("\\+");
			for(int n=0; n<tempSkjsbhArray2.length; n++){
				String curTeaCode = tempSkjsbhArray2[n];
				pdtj = false;
				for (int k = 0; k < jsxbkcxx.size(); k+=2) {
					if(curTeaCode.equalsIgnoreCase(MyTools.StrFiltr(jsxbkcxx.get(k)))) {
						Vector jsypsjxl = (Vector) jsxbkcxx.get(k+1); //教师已排时间序列和授课周次
						for (int s = 0; s < jsypsjxl.size(); s+=2) {
							String xl = MyTools.StrFiltr(jsypsjxl.get(s)); //教师已排时间序列
							String str1_xl = xl.substring(0, 2);
							String str2_xl = xl.substring(2);
							String skzcxq =  MyTools.StrFiltr(jsypsjxl.get(s+1)); //教师已排授课周次详情
							Vector tempvec1 = this.formatSkzc(skzcxq, xqzc); //拆分其他系部已排课信息的授课周次
							Vector tempvec2 = this.formatSkzc(curSkzcDetailArray[m], xqzc); //拆分当前排课信息的授课周次
							if(str1_xl.indexOf(sjxl_1) > -1) {
								if("上午".equalsIgnoreCase(sxw)) {
									if(swVec.indexOf(str2_xl) > -1) {
										if(this.judgeSkzc(tempvec1, tempvec2)){
											pdtj = true;
										}
									}
								}
								if("下午".equalsIgnoreCase(sxw)) {
									if(xwVec.indexOf(str2_xl) > -1) {
										if(this.judgeSkzc(tempvec1, tempvec2)){
											pdtj = true;
										}
									}
								}
							}
							if(pdtj) {
								break;
							}
						}
						
					}
					if(pdtj) {
						break;
					}
				}
				if(pdtj) {
					break;
				}
			}
			if(pdtj) {
				break;
			}
		}
		
		//有冲突的情况下 返回false 否则true
		if(pdtj) {
			return true;
		}else {
			return false;
		}
		
	}
	
	
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}
	
	public String getPK_KCBZBBH() {
		return PK_KCBZBBH;
	}

	public void setPK_KCBZBBH(String pK_KCBZBBH) {
		PK_KCBZBBH = pK_KCBZBBH;
	}

	public String getPK_XNXQBM() {
		return PK_XNXQBM;
	}

	public void setPK_XNXQBM(String pK_XNXQBM) {
		PK_XNXQBM = pK_XNXQBM;
	}

	public String getPK_ZYDM() {
		return PK_ZYDM;
	}

	public void setPK_ZYDM(String pK_ZYDM) {
		PK_ZYDM = pK_ZYDM;
	}

	public String getPK_XZBDM() {
		return PK_XZBDM;
	}
	
	public void setPK_XZBDM(String pK_XZBDM) {
		PK_XZBDM = pK_XZBDM;
	}

	public String getPK_ZT() {
		return PK_ZT;
	}

	public void setPK_ZT(String pK_ZT) {
		PK_ZT = pK_ZT;
	}

	public String getPK_KCBMXBH() {
		return PK_KCBMXBH;
	}

	public void setPK_KCBMXBH(String pK_KCBMXBH) {
		PK_KCBMXBH = pK_KCBMXBH;
	}

	public String getPK_SJXL() {
		return PK_SJXL;
	}

	public void setPK_SJXL(String pK_SJXL) {
		PK_SJXL = pK_SJXL;
	}

	public String getPK_LJXGBH() {
		return PK_LJXGBH;
	}

	public void setPK_LJXGBH(String pK_LJXGBH) {
		PK_LJXGBH = pK_LJXGBH;
	}

	public String getPK_BJPKZT() {
		return PK_BJPKZT;
	}

	public void setPK_BJPKZT(String pK_BJPKZT) {
		PK_BJPKZT = pK_BJPKZT;
	}

	public String getPK_SKJHMXBH() {
		return PK_SKJHMXBH;
	}

	public void setPK_SKJHMXBH(String pK_SKJHMXBH) {
		PK_SKJHMXBH = pK_SKJHMXBH;
	}
	
	public String getPK_SJCDBH() {
		return PK_SJCDBH;
	}

	public void setPK_SJCDBH(String pK_SJCDBH) {
		PK_SJCDBH = pK_SJCDBH;
	}

	public String getPK_SJCDMC() {
		return PK_SJCDMC;
	}

	public void setPK_SJCDMC(String pK_SJCDMC) {
		PK_SJCDMC = pK_SJCDMC;
	}

	public String getPK_BZ() {
		return PK_BZ;
	}

	public void setPK_BZ(String pK_BZ) {
		PK_BZ = pK_BZ;
	}
	
	public String getPK_CJR() {
		return PK_CJR;
	}

	public void setPK_CJR(String pK_CJR) {
		PK_CJR = pK_CJR;
	}

	public String getPK_CJSJ() {
		return PK_CJSJ;
	}

	public void setPK_CJSJ(String pK_CJSJ) {
		PK_CJSJ = pK_CJSJ;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getPT_ID() {
		return PT_ID;
	}

	public void setPT_ID(String pT_ID) {
		PT_ID = pT_ID;
	}

	public String getPT_LX() {
		return PT_LX;
	}

	public void setPT_LX(String pT_LX) {
		PT_LX = pT_LX;
	}

	public String getPT_XNXQBM() {
		return PT_XNXQBM;
	}

	public void setPT_XNXQBM(String pT_XNXQBM) {
		PT_XNXQBM = pT_XNXQBM;
	}

	public String getPT_XZBDM() {
		return PT_XZBDM;
	}

	public void setPT_XZBDM(String pT_XZBDM) {
		PT_XZBDM = pT_XZBDM;
	}

	public String getPT_XH() {
		return PT_XH;
	}

	public void setPT_XH(String pT_XH) {
		PT_XH = pT_XH;
	}

	public String getPT_SJXL() {
		return PT_SJXL;
	}

	public void setPT_SJXL(String pT_SJXL) {
		PT_SJXL = pT_SJXL;
	}

	public String getPT_KCBH() {
		return PT_KCBH;
	}

	public void setPT_KCBH(String pT_KCBH) {
		PT_KCBH = pT_KCBH;
	}

	public String getPT_KCMC() {
		return PT_KCMC;
	}

	public void setPT_KCMC(String pT_KCMC) {
		PT_KCMC = pT_KCMC;
	}

	public String getPT_SKJSBH() {
		return PT_SKJSBH;
	}

	public void setPT_SKJSBH(String pT_SKJSBH) {
		PT_SKJSBH = pT_SKJSBH;
	}

	public String getPT_SKJSMC() {
		return PT_SKJSMC;
	}

	public void setPT_SKJSMC(String pT_SKJSMC) {
		PT_SKJSMC = pT_SKJSMC;
	}

	public String getPT_CDBH() {
		return PT_CDBH;
	}

	public void setPT_CDBH(String pT_CDBH) {
		PT_CDBH = pT_CDBH;
	}

	public String getPT_CDMC() {
		return PT_CDMC;
	}

	public void setPT_CDMC(String pT_CDMC) {
		PT_CDMC = pT_CDMC;
	}

	public String getPT_SKZC() {
		return PT_SKZC;
	}

	public void setPT_SKZC(String pT_SKZC) {
		PT_SKZC = pT_SKZC;
	}

	public String getPT_XF() {
		return PT_XF;
	}

	public void setPT_XF(String pT_XF) {
		PT_XF = pT_XF;
	}
	
	public String getPT_KSXS() {
		return PT_KSXS;
	}

	public void setPT_KSXS(String pT_KSXS) {
		PT_KSXS = pT_KSXS;
	}

	public String getPT_ZKS() {
		return PT_ZKS;
	}

	public void setPT_ZKS(String pT_ZKS) {
		PT_ZKS = pT_ZKS;
	}
	public String getPT_CJR() {
		return PT_CJR;
	}

	public void setPT_CJR(String pT_CJR) {
		PT_CJR = pT_CJR;
	}

	public String getPT_CJSJ() {
		return PT_CJSJ;
	}

	public void setPT_CJSJ(String pT_CJSJ) {
		PT_CJSJ = pT_CJSJ;
	}

	public String getPT_ZT() {
		return PT_ZT;
	}

	public void setPT_ZT(String pT_ZT) {
		PT_ZT = pT_ZT;
	}

	public String getPT_KCBMXBH() {
		return PT_KCBMXBH;
	}

	public void setPT_KCBMXBH(String pT_KCBMXBH) {
		PT_KCBMXBH = pT_KCBMXBH;
	}

	public String getAuth() {
		return Auth;
	}

	public void setAuth(String auth) {
		Auth = auth;
	}

	public String getPK_XB() {
		return PK_XB;
	}

	public void setPK_XB(String pK_XB) {
		PK_XB = pK_XB;
	}
	
	
}