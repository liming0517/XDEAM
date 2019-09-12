package com.pantech.devolop.registerScoreManage;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.servlet.ServletContext;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.base.common.tools.TraceLog;
import com.sun.org.apache.xml.internal.serializer.utils.StringToIntTable;

/**
 * 成绩同步巡检定时任务类
 * 20170220
 * @author yeq
 *
 */
public class Task extends TimerTask {
	private DBSource db;
	private ServletContext conf;
	private String MSG;

	/**
	 * 初始化构造方法
	 */
	public Task(ServletContext conf) {
		this.db = new DBSource(conf);
		this.conf = conf;
		this.MSG = "";
	}
	
	@Override
	public void run() {
		try{
			this.addYKRec();
			this.syncData();
		}catch (SQLException e) {
			e.printStackTrace();
		} catch (WrongSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 同步查分数据
	 * @date:2016-07-26
	 * @author: yeq
	 * @throws SQLException
	 */
	public void syncData() throws SQLException{
		String sql = "";
		Vector sqlVec = new Vector();
//		String linkStr = MyTools.getProp(request, "Base.linkInfo");
//		String sysName = MyTools.getProp(request, "Base.sysName");
		String linkStr = "";
		String sysName = "XDEAM";
		String queSysName = "XDSSQUE";
		
		if(!"".equalsIgnoreCase(linkStr))
			linkStr += ".";
		
		//用户信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysUserinfo]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysUserinfo] select * from [" + sysName + "].[dbo].[sysUserinfo]";
		sqlVec.add(sql);
		
		//用户权限信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysAuthority]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysAuthority] select * from [" + sysName + "].[dbo].[sysAuthority]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysUserAuth]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysUserAuth] select * from [" + sysName + "].[dbo].[sysUserAuth]";
		sqlVec.add(sql);
		
		//模块信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysAuthModule]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysAuthModule] select * from [" + sysName + "].[dbo].[sysAuthModule]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysAuModule]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysAuModule] select * from [" + sysName + "].[dbo].[sysAuModule]";
		sqlVec.add(sql);
		
		//部门信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysDepartment]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysDepartment] select * from [" + sysName + "].[dbo].[sysDepartment]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysUserDept]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysUserDept] select * from [" + sysName + "].[dbo].[sysUserDept]";
		sqlVec.add(sql);
		
		//其他
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[sysPermissionData]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[sysPermissionData] select * from [" + sysName + "].[dbo].[sysPermissionData]";
		sqlVec.add(sql);
		
		//专业信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[ZZJX0101]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[ZZJX0101] select * from [" + sysName + "].[dbo].[ZZJX0101]";
		sqlVec.add(sql);
		
		//班级信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[ZZJX0202]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[ZZJX0202] select * from [" + sysName + "].[dbo].[ZZJX0202]";
		sqlVec.add(sql);
		
		//学生信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[ZZXS0101]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[ZZXS0101] select * from [" + sysName + "].[dbo].[ZZXS0101]";
		sqlVec.add(sql);
		
		//学年学期信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[GZGL_XNXQ]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[GZGL_XNXQ] select * from [" + sysName + "].[dbo].[GZGL_XNXQ]";
		sqlVec.add(sql);
		
		//课程信息
		//普通课程
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[GZGL_SKJHMX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[GZGL_SKJHMX] select * from [" + sysName + "].[dbo].[GZGL_SKJHMX]";
		sqlVec.add(sql);
		//选修课
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[GZGL_XXKSKJH]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[GZGL_XXKSKJH] select * from [" + sysName + "].[dbo].[GZGL_XXKSKJH]";
		sqlVec.add(sql);
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[GZGL_XXKSKJHMX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[GZGL_XXKSKJHMX] select * from [" + sysName + "].[dbo].[GZGL_XXKSKJHMX]";
		sqlVec.add(sql);
		//添加课程
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[PKGL_TJKCXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[PKGL_TJKCXX] select * from [" + sysName + "].[dbo].[PKGL_TJKCXX]";
		sqlVec.add(sql);
		
		//查分设置
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[CJGL_CFSZXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[CJGL_CFSZXX] select * from [" + sysName + "].[dbo].[CJGL_CFSZXX]";
		sqlVec.add(sql);
		
		//科目信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[CJGL_KMKCXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[CJGL_KMKCXX] select * from [" + sysName + "].[dbo].[CJGL_KMKCXX]";
		sqlVec.add(sql);
		
		//学生成绩信息
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[CJGL_XSCJXX]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[CJGL_XSCJXX] select * from [" + sysName + "].[dbo].[CJGL_XSCJXX]";
		sqlVec.add(sql);
		
		//文字代码
		sql = "truncate table " + linkStr + "[" + queSysName + "].[dbo].[CJGL_WZCJDM]";
		sqlVec.add(sql);
		sql = "insert into " + linkStr + "[" + queSysName + "].[dbo].[CJGL_WZCJDM] select * from [" + sysName + "].[dbo].[CJGL_WZCJDM]";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("同步成功");
		}else{
			this.setMSG("同步失败");
		}
	}
	
	/**
	 * 新增月考考试
	 * @date:2017-07-31
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void addYKRec() throws WrongSQLException, SQLException {
		String sql = "";
		Vector ykVec = new Vector();//月考设置信息表的所有数据
		int year;
		int month;
		int day;
		int currentMaxDays;//一个月有几天
		String maxksbh="";//生成最大的考试编号
		String curXnxq = "";//当前学年学期
		Vector xnxqVec = new Vector();
		String tempXn = "";
		String sqlCondition = "";
		Vector xnVec = new Vector();
		Vector xnbmVec = new Vector();
		
		sql = "select COUNT(*) from V_规则管理_学年学期表 where GETDATE() between 学期开始时间 and 学期结束时间";
		if(db.getResultFromDB(sql)){
			Calendar cal = Calendar.getInstance();
			year=cal.get(Calendar.YEAR); 
			month=cal.get(Calendar.MONTH) + 1;
			day=cal.get(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DATE, 1);  
			cal.roll(Calendar.DATE, -1);  
		    currentMaxDays = cal.get(Calendar.DATE); 
		    
		    //获取当前学年学期
			sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc";
			xnxqVec = db.GetContextVector(sql);
			
			if(xnxqVec!=null && xnxqVec.size()>0){
				curXnxq = MyTools.StrFiltr(xnxqVec.get(0));
			}
			String XN = curXnxq.substring(0, 4);
			int examXn = MyTools.StringToInt(XN);
			for(int i=0; i<3; i++){
				tempXn = MyTools.StrFiltr(examXn-i);
				sqlCondition += "'"+tempXn+"',";
				xnVec.add(tempXn);
			}
			sqlCondition = sqlCondition.substring(0, sqlCondition.length()-1);
			
			
			sql="select a.考试名称,a.课程代码,a.年级,a.等级考学生类别,a.每月创建时间 from dbo.V_自设考试管理_月考设置信息表 as a where a.状态='1' ";
			ykVec=db.GetContextVector(sql);
			for(int i=0;i<ykVec.size();i+=5){
				if(day!=currentMaxDays){//今天不是最后一天
					//if(day==Integer.parseInt(MyTools.StrFiltr(ykVec.get(i+3)))){
					if(day==Integer.parseInt(MyTools.StrFiltr(ykVec.get(i+4)))){
						maxksbh=(db.getMaxID("V_自设考试管理_考试信息表", "考试编号", "KS_", 10));// 获取主关键字
						addRec(maxksbh,MyTools.StrFiltr(ykVec.get(i)),curXnxq,MyTools.StrFiltr(ykVec.get(i+3)));
						String xnArray[] = MyTools.StrFiltr(ykVec.get(i+2)).split(",");
						for(int k=0;k<xnArray.length;k++){
							if(xnArray[k].equalsIgnoreCase("1")){
								xnArray[k]=MyTools.StrFiltr(xnVec.get(0));
							}
							if(xnArray[k].equalsIgnoreCase("2")){
								xnArray[k]=MyTools.StrFiltr(xnVec.get(1));
							}
							if(xnArray[k].equalsIgnoreCase("3")){
								xnArray[k]=MyTools.StrFiltr(xnVec.get(2));
							}
						}
						for(int j=0;j<xnArray.length;j++){
							xnbmVec.add(xnArray[j]);
						}
						saveSelXkPL(xnbmVec,MyTools.StrFiltr(ykVec.get(i+1)),maxksbh);
					}
				}
				if(day==currentMaxDays){//今天是最后一天
					//if(Integer.parseInt(MyTools.StrFiltr(ykVec.get(i+3)))>day){
					if(Integer.parseInt(MyTools.StrFiltr(ykVec.get(i+4)))>day || Integer.parseInt(MyTools.StrFiltr(ykVec.get(i+4)))==day){
						maxksbh=(db.getMaxID("V_自设考试管理_考试信息表", "考试编号", "KS_", 10));// 获取主关键字
						addRec(maxksbh,MyTools.StrFiltr(ykVec.get(i)),curXnxq,MyTools.StrFiltr(ykVec.get(i+3)));
						String xnArray[] = MyTools.StrFiltr(ykVec.get(i+2)).split(",");
						for(int k=0;k<xnArray.length;k++){
							if(xnArray[k].equalsIgnoreCase("1")){
								xnArray[k]=MyTools.StrFiltr(xnVec.get(0));
							}
							if(xnArray[k].equalsIgnoreCase("2")){
								xnArray[k]=MyTools.StrFiltr(xnVec.get(1));
							}
							if(xnArray[k].equalsIgnoreCase("3")){
								xnArray[k]=MyTools.StrFiltr(xnVec.get(2));
							}
						}
						for(int j=0;j<xnArray.length;j++){
							xnbmVec.add(xnArray[j]);
						}
						saveSelXkPL(xnbmVec,MyTools.StrFiltr(ykVec.get(i+1)),maxksbh);
					}
				}
			}
			
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 新增
	 * @date:2017-06-28
	 * @author:zhaixuchao
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void addRec(String ksbh,String ksmc ,String xnxqbm, String djkxslb) throws WrongSQLException, SQLException {
		String sql = "";
		Vector zslbVec = new Vector();
		String zslbString="";
		
		/*sql="insert into V_自设考试管理_考试信息表 values (" +
				"'" + MyTools.fixSql(ksbh) + "'," +
				"'" + MyTools.fixSql(ksmc) + "'," +
				"'" + MyTools.fixSql(xnxqbm) + "'," +
				"'01'," +
				"'post'," +
				"getDate(),'1')" ;*/
		sql = "insert into V_自设考试管理_考试信息表 (考试编号,考试名称,学年学期编码,类别编号,招生类别,等级考学生类别,创建人,创建时间,状态) values (" +
			"'" + MyTools.fixSql(ksbh) + "'," +
			"'" + MyTools.fixSql(ksmc) + "'," +
			"'" + MyTools.fixSql(xnxqbm) + "'," +
			"'01'," ;
		
			/*String sqlZSLB = "select a.描述 from V_信息类别_类别操作 a where a.父类别代码='ZSLBM' ";
			zslbVec = db.GetContextVector(sqlZSLB);
			
			if(zslbVec.size()>0){
				for(int i=0;i<zslbVec.size();i++){
					zslbString += zslbVec.get(i) + ",";
				}
				zslbString.substring(0, zslbString.length()-1);
				String bjStr[] = zslbString.split(",");
				
				zslbString="";
				for(int i=0;i<bjStr.length;i++){
					zslbString += "" + bjStr[i] + ",";
				}
				if(!zslbString.equals("")){
					zslbString=zslbString.substring(0, zslbString.length()-1);
				}
				zslbString+="";
			}else{
				zslbString="";
			}*/
		sql += "'all'," +
			"'" + MyTools.fixSql(djkxslb) + "'," + 
			"'post',getDate(),'1')" ;
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 保存班级已选学科信息(批量设置)
	 * @date:2017-07-03
	 * @author:zhaixuchao
	 * @param selXkInfo 选择的学科信息
	 * @throws SQLException
	 */
	public void saveSelXkPL(Vector xnbmVec,String xk,String ksbh) throws SQLException {
		Vector sqlVec = new Vector();
		Vector vec = null;
		String sql = "";
		String xkArray[] = xk.split(",");
		boolean existFlag = false;
		String id = "";
		String classno = "";
		Vector addXkVec = new Vector();
		Vector updateXkVec = new Vector();
		
		Vector bjVec = new Vector();
		Vector bjfzVec = new Vector();
		
		Vector KCDMVec=new Vector();
		
		Vector bjcopyVec = new Vector();
		
		String xndm="";
		String xndmString="";
		
		for(int i=0;i<xkArray.length;i++){
			KCDMVec.add(xkArray[i]);
		}
		
		for(int i=0;i<xnbmVec.size();i++){
			xndm += xnbmVec.get(i) + ",";
		}
		xndm.substring(0, xndm.length()-1);
		String kcdmStr[] = xndm.split(",");
		
		xndmString="";
		for(int i=0;i<kcdmStr.length;i++){
			xndmString += "'" + kcdmStr[i] + "',";
		}
		if(!xndmString.equals("")){
			xndmString=xndmString.substring(0, xndmString.length()-1);
		}
		
		//读取当前考试班级所有已选学科
		sql="select a.考试学科编号,a.课程代码,a.班级代码  from V_自设考试管理_考试学科信息表 as a " +
				"left join V_自设考试管理_考试信息表 as b on a.考试编号=b.考试编号 " +
				"left join V_学校班级数据子类 as c on a.班级代码=c.行政班代码 " +
				"left join V_学校年级数据子类 as d on c.年级代码=d.年级代码 "+
				"where a.状态='1' and b.状态='1' and c.状态='1' and d.年级状态='1' "+
				" and d.所属年份 in (" + xndmString + ") ";
		
		/*if (!"all".equalsIgnoreCase(plzyInfo) && !"".equalsIgnoreCase(plzyInfo)) {
			sql += " and c.系部代码 in ('" + MyTools.fixSql(plzyInfo).replaceAll(",", "','") + "') ";
		}
		else{
			sql += " and c.系部代码 in (" + zydmString + ") ";
		}*/
		/*if (!"all".equalsIgnoreCase(plkcInfo) && !"".equalsIgnoreCase(plkcInfo)) {
			sql += " and a.课程代码 in ('" + MyTools.fixSql(plkcInfo).replaceAll(",", "','") + "') ";
		}else{*/
			sql += " and a.课程代码 in ('" +  MyTools.fixSql(xk).replaceAll(",", "','") + "') ";
		//}
		sql+=" and b.考试编号='" + MyTools.fixSql(ksbh) + "' ";
		
		vec = db.GetContextVector(sql);
		
		
		sql="update a set a.状态='0' from V_自设考试管理_考试学科信息表 as a " +
				"left join V_自设考试管理_考试信息表 as b on a.考试编号=b.考试编号 " +
				"left join V_学校班级数据子类 as c on a.班级代码=c.行政班代码 " +
				"left join V_学校年级数据子类 as d on c.年级代码=d.年级代码 "+
				"where a.状态='1' and b.状态='1' and c.状态='1' and d.年级状态='1' "+
				" and d.所属年份  in (" + xndmString + ") ";
		/*if (!"all".equalsIgnoreCase(plzyInfo) && !"".equalsIgnoreCase(plzyInfo)) {
			sql += " and c.系部代码 in ('" + MyTools.fixSql(plzyInfo).replaceAll(",", "','") + "') ";
		}else{
			sql += " and c.系部代码 in (" + zydmString + ") ";
		}*/
		
		sql+=" and b.考试编号='" + MyTools.fixSql(ksbh) + "' ";
		sqlVec.add(sql);
		
		sql="select a.行政班代码 from V_学校班级数据子类 as a " +
				"left join dbo.V_学校年级数据子类 as b on a.年级代码=b.年级代码 "+
				"where a.状态='1' and b.年级状态='1' and b.所属年份 in (" +  xndmString + ") ";
		/*if (!"all".equalsIgnoreCase(plzyInfo) && !"".equalsIgnoreCase(plzyInfo)) {
			sql += " and a.系部代码 in ('" + MyTools.fixSql(plzyInfo).replaceAll(",", "','") + "') ";
		}else{
			sql += " and a.系部代码 in (" + zydmString + ") ";
		}*/
		
		bjVec = db.GetContextVector(sql);
		for(int i =0;i<bjVec.size();i++){
			bjfzVec.add(bjVec.get(i));
		}

		if(!xk.equalsIgnoreCase("")&&!xk.equalsIgnoreCase("null")){
				
			for(int i=0; i<KCDMVec.size(); i++){
				existFlag = false;
				
				for(int k =0;k<bjVec.size();k++){
					bjcopyVec.add(bjVec.get(k));
				}
				for(int k=0;k<vec.size();k++){
					bjcopyVec.remove(vec.get(k));
				}
				
				//判断已选学科是否已存在
				for(int j=0; j<vec.size(); j+=3){
					if(MyTools.StrFiltr(KCDMVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1)))){
						id = MyTools.StrFiltr(vec.get(j));
						classno = MyTools.StrFiltr(vec.get(j+2));
						existFlag = true;
						break;
					}
				}
				if(existFlag == false){
					int num=0;
						if(vec.size()==0){
							for(int k=0;k<KCDMVec.size();k++){
								for(int r=0;r<bjfzVec.size();r++){
									
									id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
									classno=MyTools.StrFiltr(bjfzVec.get(r));
									//bjfzVec.remove(r);
									sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
											"'" + MyTools.fixSql(id) + "'," +
											"'" + MyTools.fixSql(ksbh) + "'," +
											"'" + MyTools.fixSql(classno) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(k))) + "'," +
											"'post'," +
											"getDate(),'1')";
									addXkVec.add(id);
									addXkVec.add(MyTools.StrFiltr(KCDMVec.get(k)));
									sqlVec.add(sql);
								}
								num++;
							}
							if(num==KCDMVec.size()){
								break;
							}
						}else{
							for(int r=0;r<bjfzVec.size();r++){
								id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
								classno=MyTools.StrFiltr(bjfzVec.get(r));
								
								sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
										"'" + MyTools.fixSql(id) + "'," +
										"'" + MyTools.fixSql(ksbh) + "'," +
										"'" + MyTools.fixSql(classno) + "'," +
										"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(i))) + "'," +
										"'post'," +
										"getDate(),'1')";
									addXkVec.add(id);
									addXkVec.add(MyTools.StrFiltr(KCDMVec.get(i)));
									sqlVec.add(sql);
							}
						}
					
				}else{
					for(int j=0; j<vec.size(); j+=3){
						String classid="";
						if(MyTools.StrFiltr(KCDMVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(vec.get(j+1)))){
							id = MyTools.StrFiltr(vec.get(j));
							classid=MyTools.StrFiltr(vec.get(j+2));
							
							sql = "update V_自设考试管理_考试学科信息表 " +
									"set 状态='1' " +
									"where 考试学科编号='" + MyTools.fixSql(id) + "'";
							updateXkVec.add(id);
							sqlVec.add(sql);
							
							if(bjcopyVec.size()>0){
								for(int k=0;k<bjcopyVec.size();k++){
									id = db.getMaxID("V_自设考试管理_考试学科信息表", "考试学科编号", "KSXK_", 10);
									sql = "insert into V_自设考试管理_考试学科信息表 (考试学科编号,考试编号,班级代码,课程代码,创建人,创建时间,状态) values(" +
											"'" + MyTools.fixSql(id) + "'," +
											"'" + MyTools.fixSql(ksbh) + "'," +
											"'" + bjcopyVec.get(k) + "'," +
											"'" + MyTools.fixSql(MyTools.StrFiltr(KCDMVec.get(i))) + "'," +
											"'post'," +
											"getDate(),'1')";
									addXkVec.add(id);
									addXkVec.add(MyTools.StrFiltr(KCDMVec.get(i)));
									sqlVec.add(sql);	
									bjcopyVec.remove(k);
								}
								bjcopyVec.clear();
							}
						}
					}
				}
			}
		}

		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			sqlVec.clear();
			
			//处理学生成绩信息
			for(int i=0;i<bjVec.size();i++){
				sql="update a set a.状态='0' from V_自设考试管理_学生成绩信息表 as a left join V_自设考试管理_考试学科信息表 as b on a.考试学科编号=b.考试学科编号 " +
						"where b.考试编号='" + MyTools.fixSql(ksbh) + "' and b.班级代码 = '" + bjVec.get(i) + "'";
				/*if (!"all".equalsIgnoreCase(plkcInfo) && !"".equalsIgnoreCase(plkcInfo)) {
					sql += " and b.课程代码 in ('" + MyTools.fixSql(plkcInfo).replaceAll(",", "','") + "') ";
				}else{*/
					sql += " and b.课程代码 in ('" + MyTools.fixSql(xk).replaceAll(",", "','") + "') ";
				//}
				sqlVec.add(sql);
			}
			
			for(int i=0; i<updateXkVec.size(); i++){
				sql = "update V_自设考试管理_学生成绩信息表  set 状态='1' where 考试学科编号='" + MyTools.fixSql(MyTools.StrFiltr(updateXkVec.get(i))) + "'";
				sqlVec.add(sql);
			}
			int k=-1;
			for(int i=0; i<addXkVec.size(); i+=2){
				k++;
				if(bjVec.size()==vec.size()/3){
					for(int f=0;f<bjVec.size();f++){
						bjcopyVec.add(bjVec.get(f));
					}
				}else{
					for(int f=0;f<bjVec.size();f++){
						bjcopyVec.add(bjVec.get(f));
					}
					for(int f=0;f<vec.size();f++){
						bjcopyVec.remove(vec.get(f));
					}
				}
				
				if(k==bjcopyVec.size()){
					k=0;
				}
				for(int j=k;j<bjcopyVec.size();){
					sql="insert into V_自设考试管理_学生成绩信息表 (学号,姓名,考试学科编号,创建人,创建时间,状态) " +
							"select a.学号,a.姓名,'" + MyTools.fixSql(MyTools.StrFiltr(addXkVec.get(i))) + "' as 考试学科编号,'post',GETDATE(),'1' "+
							"from V_学生基本数据子类 as a " +
							"where a.学生状态 in ('01','05') and a.行政班代码='" + bjcopyVec.get(j) + "' order by a.姓名 ";
					sqlVec.add(sql);
					break;
				}
			}
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}else{
			this.setMSG("保存失败");
		}
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}