package com.pantech.develop.pageoffice;
/*
@date 2015.10.198
@author yeq
模块：M3课表导出
说明:
重要及特殊方法：
*/
import com.zhuozhengsoft.pageoffice.*;
import com.zhuozhengsoft.pageoffice.excelwriter.*;

import java.awt.*;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.*;

import java.sql.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;
import com.pantech.devolop.timetableQuery.KbcxBean;

public class ExportExcelBean {
	/**
	 * 课程表导出
	 * @date:2015-10-19
	 * @author:yeq
	 * @param xnxqbm 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	public static void exportSingleTimetable(HttpServletRequest request, String sAuth, String userCode, String xnxqbm, String exportType, String parentId, String code, String timetableName) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String sql = "";
		Vector timeVec = null;
		Vector allExportDataVec = new Vector();
		Vector curExportDataVec = null;
		Vector vec = new Vector();
		Vector tempVec = null;
		Vector bzInfoVec = null;
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		Vector oddVec = new Vector();//单周周次
		Vector evenVec = new Vector();//双周周次
		String xnxqmc = "";//学年学期名称
		int mzts = 0;//每周天数
		int sw = 0;//上午节数
		int zw = 0;//中午节数
		int xw = 0;//下午节数
		int ws = 0;//晚上节数
		int zjs = 0;//总节数
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 50;
		float fontSize = 0;
		
		Workbook wb = new Workbook();
		Cell cell;
		String cellContent = ""; //当前单元格的内容
		
		Vector tempAllData = new Vector();
		//Vector hbSetVec = new Vector();
		Vector allCourseVec = new Vector();
		Vector curCourseVec = new Vector();
		String curName = "";
		String bzCellContent = "";
		
		String tempOrder = "";//时间序列
		int tempIndex = -1;
		int mergeNum = 0;//单元格合并数
		boolean flag = true;
		String timeOrderFlag = "";
		int tempTime = 0;
		
		String sjxl = "";
		String skjhmxbh = "";
		String kcbh = "";
		String kcmc = "";
		String xzbdm = "";
		String xzbmc = "";
		String skjsbh = "";
		String skjsxm = "";
		String cdbh = "";
		String cdmc = "";
		String skzc = "";
		String tempSkjhmxbh = "";//授课计划明细编号
		String timeOrder = "";//时间序列
		String tempCourseName = "";//课程名称
		String tempClassName = "";//班级名称
		String tempTeaCode = "";//教师编号
		String tempTeaName = "";//教师姓名
		String tempSiteCode = "";//场地编号
		String tempSiteName = "";//场地名称
		String tempSkzc = "";//授课周次
		Vector hbSetVec = null;
		String hbSet = "";
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		String skjhmxbhArray[] = new String[0];
		
		if("classKcbAll".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType) || "courseKcbAll".equalsIgnoreCase(exportType))
			timetableName = "";
		
		//获取当前学年学期的上课时间相关信息
		sql = "select distinct (select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as 学期周次," +
			"t1.学年学期名称,t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
			"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间 " +
			"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		timeVec = db.GetContextVector(sql);
		if(timeVec!=null && timeVec.size()>0){
			int xqzc = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(0)));
			for(int i=1; i<xqzc+1; i+=2){
				oddVec.add(i);
			}
			for(int i=2; i<xqzc+1; i+=2){
				evenVec.add(i);
			}
			
			xnxqmc = MyTools.StrFiltr(timeVec.get(1));
			mzts = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(2)));
			sw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(3)));
			zw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(4)));
			xw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(5)));
			ws = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(6)));
			zjs = sw+zw+xw+ws;
			timeArray = MyTools.StrFiltr(timeVec.get(7)).split(",", -1);
		}
		
		//获取当前选择班级的课表信息
		if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType)){
			String tempHbClass = "";
			String hbSetInfo = "";
			Vector classVec = new Vector();
			Vector curClassData = null;
			String tempClass = "";
			String curClassCode = "";
			boolean existFlag = false;
			
			//判断如果是导出所有班级课表,获取需要导出数据的班级名单
			if("classKcbAll".equalsIgnoreCase(exportType)){
//				sql = "select distinct a.行政班代码,b.行政班名称 from V_排课管理_课程表主表 a " +
//					"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//					"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
//					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//					"order by a.行政班代码";
				sql = "select distinct a.行政班代码,b.班级名称 from V_排课管理_课程表主表 a " +
					"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
					"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码 " +
					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				//权限判断
				if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
					sql += " and (";
					//班主任
					if(sAuth.indexOf(bzr) > -1){
						sql += "b.班主任工号='" + MyTools.fixSql(userCode) + "'";
					}
					//系部教务人员
					if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
						if(sAuth.indexOf(bzr) > -1){
							sql += " or ";
						}
						sql += "b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
					}
					sql += ")";
				}
				sql += " order by a.行政班代码";
				classVec = db.GetContextVector(sql);
			}else{
				classVec.add(code);
				classVec.add(timetableName);
			}
			
			/**读取班级课表start*/
//			sql = "select distinct 行政班代码,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) " +
//				"from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
//			if("classKcb".equalsIgnoreCase(exportType)){
//				sql += " and 行政班代码='" + MyTools.fixSql(code) + "'";
//			}
//			sql += " and 授课计划明细编号<>'' order by 行政班代码,时间序列,cast(授课周次 as int)";
			sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
				"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " + 
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			if("classKcb".equalsIgnoreCase(exportType)){
				sql += " and a.行政班代码='" + MyTools.fixSql(code) + "'";
			}
			sql += " and a.授课计划明细编号<>'') as t order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				flag = true;
				//拼接课程周次
				while(flag){
					xzbdm = MyTools.StrFiltr(tempVec.get(0));
					sjxl = MyTools.StrFiltr(tempVec.get(1));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
					kcbh = MyTools.StrFiltr(tempVec.get(3));
					kcmc = MyTools.StrFiltr(tempVec.get(4));
					skjsbh = MyTools.StrFiltr(tempVec.get(5));
					skjsxm = MyTools.StrFiltr(tempVec.get(6));
					cdbh = MyTools.StrFiltr(tempVec.get(7));
					cdmc = MyTools.StrFiltr(tempVec.get(8));
					skzc = MyTools.StrFiltr(tempVec.get(9));
					for(int i=0; i<10; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=10){
						if(xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) 
							&& skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+3))) 
							&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+5))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+7)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+9));
							for(int j=0; j<10; j++){
								tempVec.remove(i);
							}
							i -= 10;
						}
					}
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(kcmc);
					vec.add(skjsbh);
					vec.add(skjsxm);
					vec.add(cdbh);
					vec.add(cdmc);
					vec.add(skzc);
					vec.add(xzbdm);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
				
				sjxl = "";
				//拼接课程
				for(int i=0; i<vec.size(); i+=9){
					skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
					kcmc = MyTools.StrFiltr(vec.get(i+2));
					skjsbh = MyTools.StrFiltr(vec.get(i+3));
					skjsxm = MyTools.StrFiltr(vec.get(i+4));
					cdbh = MyTools.StrFiltr(vec.get(i+5));
					cdmc = MyTools.StrFiltr(vec.get(i+6));
					skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+7)), oddVec, evenVec);
					
					if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+8))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
						if(i > 0){
							tempAllData.add(sjxl);
							
							for(int j=0; j<tempVec.size(); j+=7){
								tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(j))+"｜";
								tempCourseName += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
								tempTeaCode += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
								tempTeaName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
								tempSiteCode += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
								tempSiteName += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
								tempSkzc += MyTools.StrFiltr(tempVec.get(j+6))+"｜";
							}
							
							tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
							tempAllData.add(tempCourseName.substring(0, tempCourseName.length()-1));
							tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length()-1));
							tempAllData.add(tempTeaName.substring(0, tempTeaName.length()-1));
							tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
							tempAllData.add(tempSiteName.substring(0, tempSiteName.length()-1));
							tempAllData.add(tempSkzc.substring(0, tempSkzc.length()-1));
							tempAllData.add(xzbdm);
							tempSkjhmxbh = "";
							tempCourseName = "";
							tempTeaCode = "";
							tempTeaName = "";
							tempSiteCode = "";
							tempSiteName = "";
							tempSkzc = "";
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						xzbdm = MyTools.StrFiltr(vec.get(i+8));
						tempVec = new Vector();
						tempVec.add(skjhmxbh);
						tempVec.add(kcmc);
						tempVec.add(skjsbh);
						tempVec.add(skjsxm);
						tempVec.add(cdbh);
						tempVec.add(cdmc);
						tempVec.add(skzc);
					}else{
						for(int j=0; j<tempVec.size(); j+=7){
							if(skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j)))){
								tempVec.set(j+2, MyTools.StrFiltr(tempVec.get(j+2))+"&"+skjsbh);
								tempVec.set(j+3, MyTools.StrFiltr(tempVec.get(j+3))+"&"+skjsxm);
								tempVec.set(j+4, MyTools.StrFiltr(tempVec.get(j+4))+"&"+cdbh);
								tempVec.set(j+5, MyTools.StrFiltr(tempVec.get(j+5))+"&"+cdmc);
								tempVec.set(j+6, MyTools.StrFiltr(tempVec.get(j+6))+"&"+skzc);
								existFlag = true;
							}
						}
						
						if(existFlag == false){
							tempVec.add(skjhmxbh);
							tempVec.add(kcmc);
							tempVec.add(skjsbh);
							tempVec.add(skjsxm);
							tempVec.add(cdbh);
							tempVec.add(cdmc);
							tempVec.add(skzc);
						}
						existFlag = false;
					}
				}
				
				tempAllData.add(sjxl);
				
				for(int j=0; j<tempVec.size(); j+=7){
					tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(j))+"｜";
					tempCourseName += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
					tempTeaCode += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
					tempTeaName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
					tempSiteCode += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
					tempSiteName += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
					tempSkzc += MyTools.StrFiltr(tempVec.get(j+6))+"｜";
				}
				
				tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
				tempAllData.add(tempCourseName.substring(0, tempCourseName.length()-1));
				tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length()-1));
				tempAllData.add(tempTeaName.substring(0, tempTeaName.length()-1));
				tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
				tempAllData.add(tempSiteName.substring(0, tempSiteName.length()-1));
				tempAllData.add(tempSkzc.substring(0, tempSkzc.length()-1));
				tempAllData.add(xzbdm);
			}
			/**读取班级课表end*/
			
			//添加体锻课  lupengfei 20170928			
			int tytag=0;//体育课标记
			int tdcs=0;//体锻课次数
			int zwyk=0;//中午是否有課
			
			//获取操场编号
			String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(code) + "' and [教室名称]='操场' ) ";
			Vector veccc=db.GetContextVector(sqlcc);
			
			//获取实际上课周数
			String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
			Vector vecskzs=db.GetContextVector(sqlskzs);
				
			//获取每周天数
			int mztsx=0;
			String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(xnxqbm) + "' ";
			Vector vecmzts=db.GetContextVector(sqlmzts);
			if(vecmzts!=null&&vecmzts.size()>0){
				mztsx=Integer.parseInt(vecmzts.get(0).toString());
			}
			
			//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
			
			//循环班级
			for(int b=0;b<classVec.size();b=b+2){
				tdcs=0;//体锻课次数		
				//判断哪天没体育课,中午添加体锻课
				String ts="";
				for(int i=1;i<=mztsx;i++){
					tytag=0;//体育课标记
					zwyk=0;//中午是否有課
					if(i<10){
						ts="0"+i;
					}else{
						ts=i+"";
					}
					for(int j=0;j<tempAllData.size();j=j+9){
						if(tempAllData.get(j+8).toString().equals(classVec.get(b).toString())){//班级相同
							if(ts.equals(tempAllData.get(j).toString().substring(0, 2))){//天数相同
								if(tempAllData.get(j+2).toString().indexOf("体育")>-1){//有体育课
									tytag=1;
								}
							}	
						}
					}
					if(tytag==0){
						for(int k=0;k<tempAllData.size();k=k+9){
							if(tempAllData.get(k+8).toString().equals(classVec.get(b).toString())){//班级相同
								if((ts+"05").equals(tempAllData.get(k).toString())){//中午有课
									zwyk=1;							
								}
							}
						}
						if(zwyk==0){
							if(tdcs<2){
								//添加体锻课
								int existkc=0;//班级有课
								for(int t=0;t<tempAllData.size();t=t+9){
									if(tempAllData.get(t+8).toString().equals(classVec.get(b).toString())){//班级相同
										existkc=1;
										break;
									}
								}
								if(existkc==1){  
									//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
									tempAllData.add(ts+"05");//时间序列
									tempAllData.add("SKJHMXBH_TD");//授课计划明细编号
									tempAllData.add("体锻");//课程名称
									tempAllData.add("");//授课教师编号
									tempAllData.add("");//授课教师姓名
									tempAllData.add("");//实际场地编号
									tempAllData.add("");//实际场地名称	
									tempAllData.add("");//授课周次详情
									tempAllData.add(classVec.get(b).toString());//行政班代码
									
									tdcs++;
								}
							}
						}else{
							zwyk=0;
						}
					}
				}
			}

//			for(int i=0;i<tempAllData.size();i=i+9){
//				System.out.println("1:"+tempAllData.get(i).toString()+" 2:"+tempAllData.get(i+1).toString()+" 3:"+tempAllData.get(i+2).toString()+" 4:"+tempAllData.get(i+3).toString()+" 5:"+tempAllData.get(i+4).toString()+" 6:"+tempAllData.get(i+5).toString()+" 7:"+tempAllData.get(i+6).toString()+" 8:"+tempAllData.get(i+7).toString()+" 9:"+tempAllData.get(i+8).toString());
//			}
			
			//=============================================================================================
			
			//查询合班信息
////			sql = "select distinct b.时间序列,b.行政班代码,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from V_规则管理_授课计划明细表 a " +
////				"inner join V_排课管理_课程表明细详情表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
////				"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
////				"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
////			if("classKcb".equalsIgnoreCase(exportType)){
////				sql += "and b.行政班代码<>'" + MyTools.fixSql(code) + "' ";
////			}
////			sql += "order by b.时间序列,c.授课计划明细编号";
//			sql = "with " +
//				"tempSkjh as (" +
//				"select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
//				"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'" +
//				")," +
//				"tempKcb as (" +
//				"select 时间序列,行政班代码,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 " +
//				"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') " +
//				"select b.时间序列,b.行政班代码,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from tempSkjh a " +
//				"left join tempKcb b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//				"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//				"where 1=1";
//			if("classKcb".equalsIgnoreCase(exportType)){
//				sql += "and b.行政班代码<>'" + MyTools.fixSql(code) + "' ";
//			}
//			sql += " order by b.时间序列,c.授课计划明细编号";
//			hbSetVec = db.GetContextVector(sql);
			
			//查询合班信息
			//20170125修改yeq 查询效率优化
			Vector classKcbVec = null;
			Vector classSkjhVec = null;
			Vector hbSkjhVec = null;
			tempVec = new Vector();
			hbSetVec = new Vector();
			
			sql = "select 时间序列,行政班代码,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 " +
				"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			if("classKcb".equalsIgnoreCase(exportType)){
				sql += " and 行政班代码<>'" + MyTools.fixSql(code) + "'";
			}
			sql += " and 授课计划明细编号<>'' order by 时间序列";
			classKcbVec = db.GetContextVector(sql);
			
			if(classKcbVec!=null && classKcbVec.size()>0){
				sql = "select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
					"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				if("classKcb".equalsIgnoreCase(exportType)){
					sql += " and b.行政班代码<>'" + MyTools.fixSql(code) + "'";
				}
				classSkjhVec = db.GetContextVector(sql);
				
				if(classSkjhVec!=null && classSkjhVec.size()>0){
					for(int i=0; i<classKcbVec.size(); i+=4){
						tempSkjhmxbh = MyTools.StrFiltr(classKcbVec.get(i+3));
						
						for(int j=0; j<classSkjhVec.size(); j++){
							if(tempSkjhmxbh.indexOf(MyTools.StrFiltr(classSkjhVec.get(j))) > -1){
								tempVec.add(MyTools.StrFiltr(classKcbVec.get(i)));
								tempVec.add(MyTools.StrFiltr(classKcbVec.get(i+1)));
								tempVec.add(MyTools.StrFiltr(classKcbVec.get(i+2)));
								tempVec.add(MyTools.StrFiltr(classSkjhVec.get(j)));
							}
						}
					}
					
					//查询所有合班设置信息
					sql = "select 授课计划明细编号 from V_规则管理_合班表";
					hbSkjhVec = db.GetContextVector(sql);
					
					if(hbSkjhVec!=null && hbSkjhVec.size()>0){
						for(int i=0; i<tempVec.size(); i+=4){
							tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(i+3));
							
							for(int j=0; j<hbSkjhVec.size(); j++){
								if(MyTools.StrFiltr(hbSkjhVec.get(j)).indexOf(tempSkjhmxbh) > -1){
									hbSetVec.add(tempVec.get(i));
									hbSetVec.add(tempVec.get(i+1));
									hbSetVec.add(tempVec.get(i+2));
									hbSetVec.add(tempVec.get(i+3));
									hbSetVec.add(MyTools.StrFiltr(hbSkjhVec.get(j)));
									break;
								}
							}
						}
					}
				}
			}
			
			//整理班级课程数据
			for(int i=0; i<classVec.size(); i+=2){
				curClassCode = MyTools.StrFiltr(classVec.get(i));
				curClassData = new Vector();
				
				for(int j=0; j<tempAllData.size(); j+=9){
					tempClass = MyTools.StrFiltr(tempAllData.get(j+8));

					//判断是否当前班级的课程
					if(tempClass.equalsIgnoreCase(curClassCode)){
						for(int k=0; k<8; k++){
							curClassData.add(MyTools.StrFiltr(tempAllData.get(j+k)));
						}
					}
				}
				
				allCourseVec.add(curClassCode);
				allCourseVec.add(curClassData);
			}
			
			//遍历所有数据
			for(int i=0; i<classVec.size(); i+=2){
				curCourseVec = (Vector)allCourseVec.get(allCourseVec.indexOf(classVec.get(i)) + 1);
				curExportDataVec = new Vector();
				
				for(int j=0; j<curCourseVec.size(); j+=8){
					tempHbClass = "";
					tempOrder = MyTools.StrFiltr(curCourseVec.get(j));
					skjhmxbh = MyTools.StrFiltr(curCourseVec.get(j+1));
					skjhmxbhArray = skjhmxbh.split("｜", -1);
					
					//检查合班信息
					for(int k=0; k<skjhmxbhArray.length; k++){
						for(int a=0; a<hbSetVec.size(); a+=5){
							//判断是否同一个时间序列
							if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a)))){
								hbSetInfo = MyTools.StrFiltr(hbSetVec.get(a+4));
								
								//判断是当前课程的合班信息并且不是当前授课计划的数据
								if(!skjhmxbhArray[k].equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a+3))) && hbSetInfo.indexOf(skjhmxbhArray[k])>-1){
									tempHbClass += MyTools.StrFiltr(hbSetVec.get(a+2))+"、";
								}
							}
						}
						
						if(!"".equalsIgnoreCase(tempHbClass) && "、".equalsIgnoreCase(tempHbClass.substring(tempHbClass.length()-1))){
							tempHbClass = tempHbClass.substring(0, tempHbClass.length()-1);
						}
						tempHbClass += "｜";
					}
					tempHbClass = tempHbClass.substring(0, tempHbClass.length()-1);
					
					curExportDataVec.add(tempOrder);
					curExportDataVec.add(skjhmxbh);
					curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+2)));
					curExportDataVec.add(tempHbClass);
					curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+3)));
					curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+4)));
					curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+5)));
					curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+6)));
					curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+7)));
				}
				
				allExportDataVec.add(classVec.get(i));
				allExportDataVec.add(classVec.get(i+1));
				allExportDataVec.add(curExportDataVec);
			}
		}
		
		//获取当前选择教师的课表信息
		if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType)){
			Vector teaVec = new Vector();
			String curTeaCode = "";
			Vector xxkVec = new Vector();
			
			//判断如果是导出所有教师个人课表,获取需要导出数据的教师名单
			if("teaKcbAll".equalsIgnoreCase(exportType)){
				sql = "with cte1 as (" +
					"select distinct a.授课教师编号 from V_排课管理_课程表周详情表 a " +
					//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
					"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
					"where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and a.状态='1'";
				//权限判断
				if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
					sql += " and (1=2";
					//班主任
					if(sAuth.indexOf(bzr) > -1){
						sql += " or b.班主任工号='" + MyTools.fixSql(userCode) + "'";
					}
					//系部教务人员
					if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
						sql += " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
					}
					sql += ")";
				}
				sql += 	") select distinct * from (select a.工号,a.姓名 from V_教职工基本数据子类 a " +
					"inner join cte1 b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' ";
				//判断如果是管理员或全校教务权限，需查询选修课教师信息
				if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1){
					sql += "union all " +
						"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
						"left join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%' " +
						"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				}
				sql += ") as t order by 姓名";
				teaVec = db.GetContextVector(sql);
			}else{
				teaVec.add(code);
				teaVec.add(timetableName);
			}
			
			//获取选修课信息
			sql = "select c.时间序列,a.授课计划明细编号,b.课程名称,a.选修班名称,a.授课教师编号,a.授课教师姓名,c.实际场地编号,c.实际场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a " +
				"inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
				"left join V_排课管理_选修课课程表信息表 c on c.授课计划明细编号=a.授课计划明细编号 " +
				"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			if("teaKcb".equalsIgnoreCase(exportType)){
				sql += "and a.授课教师编号 like '%" + MyTools.fixSql(code) + "%' ";
			}
			sql += " order by a.授课教师姓名,c.时间序列,cast((case substring(a.授课周次, 2, 1) when '｜' then substring(a.授课周次, 1, 1) when '&' then substring(a.授课周次, 1, 1) when '#' then substring(a.授课周次, 1, 1) " +
				"when '-' then substring(a.授课周次, 1, 1) when 'd' then '1' when 'v' then '2' else substring(a.授课周次, 1, 2) end) as int)";
			xxkVec = db.GetContextVector(sql);
			
			//获取当前教师本学期的合班设置
			sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a " +
				"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
				"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
				"where c.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
			if("teaKcb".equalsIgnoreCase(exportType)){
				sql += "and b.授课教师编号 like '%" + MyTools.fixSql(code) + "%'";
			}
			hbSetVec = db.GetContextVector(sql);
			
			//获取所有相关授课信息
//			sql = "select distinct 时间序列,授课计划明细编号,课程代码,课程名称,行政班代码,行政班名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) as 授课周次 " +
//				"from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 授课计划明细编号<>''";
//			if("teaKcb".equalsIgnoreCase(exportType)){
//				sql += "and 授课教师编号 like '%" + MyTools.fixSql(code) + "%'";
//			}
//			sql += " order by 时间序列,cast(授课周次 as int),授课计划明细编号,课程代码";
			sql = "select t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.行政班代码,t.行政班名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
				"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " + 
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			if("teaKcb".equalsIgnoreCase(exportType)){
				sql += " and 授课教师编号 like '%" + MyTools.fixSql(code) + "%'";
			}
			sql += ") as t order by t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号,课程代码";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				flag = true;
				//拼接同一课程周次
				while(flag){
					sjxl = MyTools.StrFiltr(tempVec.get(0));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
					kcbh = MyTools.StrFiltr(tempVec.get(2));
					kcmc = MyTools.StrFiltr(tempVec.get(3));
					xzbdm = MyTools.StrFiltr(tempVec.get(4));
					xzbmc = MyTools.StrFiltr(tempVec.get(5));
					skjsbh = MyTools.StrFiltr(tempVec.get(6));
					skjsxm = MyTools.StrFiltr(tempVec.get(7));
					cdbh = MyTools.StrFiltr(tempVec.get(8));
					cdmc = MyTools.StrFiltr(tempVec.get(9));
					skzc = MyTools.StrFiltr(tempVec.get(10));
					for(int i=0; i<11; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=11){
						if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))
							&& xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) 
							&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+8)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+10));
							for(int j=0; j<11; j++){
								tempVec.remove(i);
							}
							i -= 11;
						}
					}
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(xzbmc);
					vec.add(kcmc);
					vec.add(skjsbh);
					vec.add(skjsxm);
					vec.add(cdbh);
					vec.add(cdmc);
					vec.add(skzc);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
				
				sjxl = "";
				tempVec = new Vector();
				for(int i=0; i<vec.size(); i+=9){
					skjsbh = MyTools.StrFiltr(vec.get(i+4));
					skjsxm = MyTools.StrFiltr(vec.get(i+5));
					cdbh = MyTools.StrFiltr(vec.get(i+6));
					cdmc = MyTools.StrFiltr(vec.get(i+7));
					skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+8)), oddVec, evenVec);
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
						if(i > 0){
							tempVec.add(sjxl);
							tempVec.add(skjhmxbh);
							tempVec.add(xzbmc);
							tempVec.add(kcmc);
							tempVec.add(tempTeaCode);
							tempVec.add(tempTeaName);
							tempVec.add(tempSiteCode);
							tempVec.add(tempSiteName);
							tempVec.add(tempSkzc);
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
						xzbmc = MyTools.StrFiltr(vec.get(i+2));
						kcmc = MyTools.StrFiltr(vec.get(i+3));
						tempTeaCode = skjsbh;
						tempTeaName = skjsxm;
						tempSiteCode = cdbh;
						tempSiteName = cdmc;
						tempSkzc = skzc;
					}else{
						tempTeaCode += "&"+skjsbh;
						tempTeaName += "&"+skjsxm;
						tempSiteCode += "&"+cdbh;
						tempSiteName += "&"+cdmc;
						tempSkzc += "&"+skzc;
					}
				}
				tempVec.add(sjxl);
				tempVec.add(skjhmxbh);
				tempVec.add(xzbmc);
				tempVec.add(kcmc);
				tempVec.add(tempTeaCode);
				tempVec.add(tempTeaName);
				tempVec.add(tempSiteCode);
				tempVec.add(tempSiteName);
				tempVec.add(tempSkzc);
				
				//拼接合班课程
				for(int i=0; i<tempVec.size(); i+=9){
					skjhmxbh = MyTools.StrFiltr(tempVec.get(i+1));
					
					for(int j=(i+9); j<tempVec.size(); j+=9){
						if(MyTools.StrFiltr(tempVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j)))){
							tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(j+1));
							
							for(int k=0; k<hbSetVec.size(); k++){
								hbSet = MyTools.StrFiltr(hbSetVec.get(k));
								
								if(hbSet.indexOf(skjhmxbh)>-1 && hbSet.indexOf(tempSkjhmxbh)>-1){
									tempVec.set(i+2, MyTools.StrFiltr(tempVec.get(i+2))+"、"+MyTools.StrFiltr(tempVec.get(j+2)));
									for(int a=0; a<9; a++){
										tempVec.remove(j);
									}
									j -= 9;
									break;
								}
							}
						}
					}
				}
			}
			
			//遍历所有数据
			for(int i=0; i<teaVec.size(); i+=2){
				curTeaCode = MyTools.StrFiltr(teaVec.get(i));
				curExportDataVec = new Vector();
				
				for(int j=1; j<mzts+1; j++){
					for(int k=1; k<zjs+1; k++){
						tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
						tempSkjhmxbh = "";
						tempCourseName = "";
						tempTeaName = "";
						tempClassName = "";
						tempSiteCode = "";
						tempSiteName = "";
						tempSkzc = "";
						
						//查询普通课程
						for(int a=0; a<tempVec.size(); a+=9){
							if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a))) && MyTools.StrFiltr(tempVec.get(a+4)).indexOf(curTeaCode)>-1){
								tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(a+1))+"｜";
								tempCourseName += MyTools.StrFiltr(tempVec.get(a+3))+"｜";
								tempTeaName += "｜";
								tempClassName += MyTools.StrFiltr(tempVec.get(a+2))+"｜";
								tempSiteCode += MyTools.StrFiltr(tempVec.get(a+6))+"｜";
								tempSiteName += MyTools.StrFiltr(tempVec.get(a+7))+"｜";
								tempSkzc += MyTools.StrFiltr(tempVec.get(a+8))+"｜";
							}
						}
						
						//查询选修课
						for(int a=0; a<xxkVec.size(); a+=9){
							if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(xxkVec.get(a))) && MyTools.StrFiltr(xxkVec.get(a+4)).indexOf(curTeaCode)>-1){
								tempSkjhmxbh += MyTools.StrFiltr(xxkVec.get(a+1))+"｜";
								tempCourseName += MyTools.StrFiltr(xxkVec.get(a+2))+"｜";
								tempTeaName += "｜";
								tempClassName += MyTools.StrFiltr(xxkVec.get(a+3))+"｜";
								tempSiteCode += MyTools.StrFiltr(xxkVec.get(a+6))+"｜";
								tempSiteName += MyTools.StrFiltr(xxkVec.get(a+7))+"｜";
								tempSkzc += MyTools.StrFiltr(xxkVec.get(a+8))+"｜";
							}
						}
						
						if(!"".equalsIgnoreCase(tempSkjhmxbh)){
							curExportDataVec.add(tempOrder);
							curExportDataVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
							curExportDataVec.add(tempCourseName.substring(0, tempCourseName.length()-1));
							curExportDataVec.add(tempTeaName.substring(0, tempTeaName.length()-1));
							curExportDataVec.add("");
							curExportDataVec.add(tempClassName.substring(0, tempClassName.length()-1));
							curExportDataVec.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
							curExportDataVec.add(tempSiteName.substring(0, tempSiteName.length()-1));
							curExportDataVec.add(tempSkzc.substring(0, tempSkzc.length()-1));
						}
					}
				}
				
				allExportDataVec.add(curTeaCode);
				allExportDataVec.add(teaVec.get(i+1));
				allExportDataVec.add(curExportDataVec);
			}
		}
		
		//课程课表
		if("courseKcb".equalsIgnoreCase(exportType) || "courseKcbAll".equalsIgnoreCase(exportType)){
			String siteName = "";
			String siteNameArray[] = new String[0];
			String siteCode = "";
			String siteCodeArray[] = new String[0];
			Vector courseVec = new Vector();
			Vector curCourseData = null;
			String tempCourse = "";
			String curCourseCode = "";
			
			//判断如果是导出所有课程课表,获取需要导出数据的课程名单
			if("courseKcbAll".equalsIgnoreCase(exportType)){
//				sql = "select distinct a.课程代码,b.课程名称+'('+b.课程号+')' from V_排课管理_课程表周详情表 a " +
//					"left join V_课程数据子类 b on b.课程号=a.课程代码 " +
//					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and a.课程类型 in ('01','02') order by a.课程代码";
				sql = "select a.课程号,case when a.课程类型='01' then a.课程名称 else a.课程名称+'（'+c.专业名称+'）' end as 课程名称 " +
					"from V_课程数据子类 a " +
					"inner join (select distinct t1.课程代码 from V_排课管理_课程表周详情表 t1 " +
					//"left join V_学校班级_数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
					"left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 " +
					"where t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				//权限判断
				if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
					sql += " and ('@'+replace(t1.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(userCode) + "@%'";
					//班主任
					if(sAuth.indexOf(bzr) > -1){
						sql += " or t2.班主任工号='" + MyTools.fixSql(userCode) + "'";
					}
					//系部教务人员
					if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
						sql += " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
					}
					sql += ")";
				}
				sql += ") b on b.课程代码=a.课程号 " +
					"left join V_专业基本信息数据子类 c on c.专业代码=a.专业代码 " +
					"order by a.课程类型,a.课程名称";
				courseVec = db.GetContextVector(sql);
			}else{
				courseVec.add(code);
				courseVec.add(timetableName);
			}
			
			//获取合班设置
			sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a " +
				"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
				"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
				"where c.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
			if("courseKcb".equalsIgnoreCase(exportType)){
				sql += "and b.课程代码='" + MyTools.fixSql(code) + "'";
			}
			hbSetVec = db.GetContextVector(sql);
			
//			sql = "select distinct 时间序列,授课计划明细编号,行政班名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int),课程代码 from V_排课管理_课程表周详情表 " +
//				"where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
//			if("courseKcb".equalsIgnoreCase(exportType)){
//				sql += " and 课程代码='" + MyTools.fixSql(code) + "'";
//				
//				if(!"01".equalsIgnoreCase(parentId)){
//					sql += " and 课程类型='02' and 专业代码='" + MyTools.fixSql(parentId) + "'";
//				}else{
//					sql += " and 课程类型='01'";
//				}
//			}else{
//				sql += " and 课程类型 in ('01','02')";
//			}
//			sql += " order by 课程代码,时间序列,授课计划明细编号,cast(授课周次 as int)";
//			sql = "select distinct a.时间序列,a.授课计划明细编号,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int),课程代码 " +
//				"from V_排课管理_课程表周详情表 a " +
//				//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
//				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
//			if("courseKcb".equalsIgnoreCase(exportType)){
//				sql += " and a.课程代码='" + MyTools.fixSql(code) + "'";
//			}
//			//权限判断
//			if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
//				sql += " and ('@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(userCode) + "@%'";
//				//班主任
//				if(sAuth.indexOf(bzr) > -1){
//					sql += " or b.班主任工号='" + MyTools.fixSql(userCode) + "'";
//				}
//				//系部教务人员
//				if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
//					sql += " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
//				}
//				sql += ")";
//			}
//			sql += " order by a.时间序列,a.授课计划明细编号,cast(a.授课周次 as int)";
			sql = "select t.时间序列,t.授课计划明细编号,t.行政班名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次,t.课程代码 " + 
				"from (select distinct a.时间序列,a.授课计划明细编号,a.行政班名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次,a.课程代码," + 
				"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
				"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
				"from V_排课管理_课程表周详情表 a " + 
				"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
				"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			if("courseKcb".equalsIgnoreCase(exportType)){
				sql += " and a.课程代码='" + MyTools.fixSql(code) + "'";
			}
			//权限判断
			if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
				sql += " and ('@'+replace(a.授课教师编号,'+','@+@')+'@' like '%@" + MyTools.fixSql(userCode) + "@%'";
				//班主任
				if(sAuth.indexOf(bzr) > -1){
					sql += " or b.班主任工号='" + MyTools.fixSql(userCode) + "'";
				}
				//系部教务人员
				if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
					sql += " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
				}
				sql += ")";
			}
			sql += ") as t order by t.课程代码,t.时间序列,t.授课计划明细编号,t.num_1,t.num_2,t.授课周次";
			tempVec = db.GetContextVector(sql);
			
			if(tempVec!=null && tempVec.size()>0){
				flag = true;
				//拼接同一课程周次
				while(flag){
					sjxl = MyTools.StrFiltr(tempVec.get(0));
					skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
					xzbmc = MyTools.StrFiltr(tempVec.get(2));
					skjsbh = MyTools.StrFiltr(tempVec.get(3));
					skjsxm = MyTools.StrFiltr(tempVec.get(4));
					cdbh = MyTools.StrFiltr(tempVec.get(5));
					cdmc = MyTools.StrFiltr(tempVec.get(6));
					skzc = MyTools.StrFiltr(tempVec.get(7));
					kcbh = MyTools.StrFiltr(tempVec.get(8));
					for(int i=0; i<9; i++){
						tempVec.remove(0);
					}
					
					for(int i=0; i<tempVec.size(); i+=9){
						if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))
							&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+3))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+5)))
							&& kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+8)))){
							skzc += "#"+MyTools.StrFiltr(tempVec.get(i+7));
							for(int j=0; j<9; j++){
								tempVec.remove(i);
							}
							i -= 9;
						}
					}
					vec.add(sjxl);
					vec.add(skjhmxbh);
					vec.add(xzbmc);
					vec.add(skjsbh);
					vec.add(skjsxm);
					vec.add(cdbh);
					vec.add(cdmc);
					vec.add(skzc);
					vec.add(kcbh);
					
					if(tempVec.size() == 0){
						flag = false;
					}
				}
				
				sjxl = "";
				tempVec = new Vector();
				for(int i=0; i<vec.size(); i+=9){
					skjsbh = MyTools.StrFiltr(vec.get(i+3));
					skjsxm = MyTools.StrFiltr(vec.get(i+4));
					cdbh = MyTools.StrFiltr(vec.get(i+5));
					cdmc = MyTools.StrFiltr(vec.get(i+6));
					skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+7)), oddVec, evenVec);
					
					if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1)))){
						if(i > 0){
							tempVec.add(sjxl);
							tempVec.add(skjhmxbh);
							tempVec.add(xzbmc);
							tempVec.add(tempTeaCode);
							tempVec.add(tempTeaName);
							tempVec.add(tempSiteCode);
							tempVec.add(tempSiteName);
							tempVec.add(tempSkzc);
							tempVec.add(kcbh);
						}
						
						sjxl = MyTools.StrFiltr(vec.get(i));
						skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
						xzbmc = MyTools.StrFiltr(vec.get(i+2));
						tempTeaCode = skjsbh;
						tempTeaName = skjsxm;
						tempSiteCode = cdbh;
						tempSiteName = cdmc;
						tempSkzc = skzc;
						kcbh = MyTools.StrFiltr(vec.get(i+8));
					}else{
						tempTeaCode += "&"+skjsbh;
						tempTeaName += "&"+skjsxm;
						tempSiteCode += "&"+cdbh;
						tempSiteName += "&"+cdmc;
						tempSkzc += "&"+skzc;
					}
				}
				tempVec.add(sjxl);
				tempVec.add(skjhmxbh);
				tempVec.add(xzbmc);
				tempVec.add(tempTeaCode);
				tempVec.add(tempTeaName);
				tempVec.add(tempSiteCode);
				tempVec.add(tempSiteName);
				tempVec.add(tempSkzc);
				tempVec.add(kcbh);
				
				//拼接合班课程
				for(int i=0; i<tempVec.size(); i+=9){
					skjhmxbh = MyTools.StrFiltr(tempVec.get(i+1));
					
					for(int j=(i+9); j<tempVec.size(); j+=9){
						if(MyTools.StrFiltr(tempVec.get(i)).equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j)))){
							tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(j+1));
							
							for(int k=0; k<hbSetVec.size(); k++){
								hbSet = MyTools.StrFiltr(hbSetVec.get(k));
								
								if(hbSet.indexOf(skjhmxbh)>-1 && hbSet.indexOf(tempSkjhmxbh)>-1){
									tempVec.set(i+2, MyTools.StrFiltr(tempVec.get(i+2))+"、"+MyTools.StrFiltr(tempVec.get(j+2)));
									for(int a=0; a<9; a++){
										tempVec.remove(j);
									}
									j -= 9;
									break;
								}
							}
						}
					}
				}
				
				for(int i=0; i<courseVec.size(); i+=2){
					curCourseCode = MyTools.StrFiltr(courseVec.get(i));
					curExportDataVec = new Vector();
					
					for(int j=1; j<mzts+1; j++){
						for(int k=1; k<zjs+1; k++){
							tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
							tempSkjhmxbh = "";
							tempCourseName = "";
							tempClassName = "";
							tempTeaCode = "";
							tempTeaName = "";
							tempSiteCode = "";
							tempSiteName = "";
							tempSkzc = "";
							
							//查询普通课程
							for(int a=0; a<tempVec.size(); a+=9){
								if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a))) && MyTools.StrFiltr(tempVec.get(a+8)).indexOf(curCourseCode)>-1){
									tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(a+1))+"｜";
									tempCourseName += "｜";
									tempClassName += MyTools.StrFiltr(tempVec.get(a+2))+"｜";
									tempTeaCode += MyTools.StrFiltr(tempVec.get(a+3))+"｜";
									tempTeaName += MyTools.StrFiltr(tempVec.get(a+4))+"｜";
									tempSiteCode += MyTools.StrFiltr(tempVec.get(a+5))+"｜";
									tempSiteName += MyTools.StrFiltr(tempVec.get(a+6))+"｜";
									tempSkzc += MyTools.StrFiltr(tempVec.get(a+7))+"｜";
								}
							}
							
							if(!"".equalsIgnoreCase(tempSkjhmxbh)){
								curExportDataVec.add(tempOrder);
								curExportDataVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
								curExportDataVec.add(tempClassName.substring(0, tempClassName.length()-1));
								curExportDataVec.add(tempCourseName.substring(0, tempCourseName.length()-1));
								curExportDataVec.add(tempTeaCode.substring(0, tempTeaCode.length()-1));
								curExportDataVec.add(tempTeaName.substring(0, tempTeaName.length()-1));
								curExportDataVec.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
								curExportDataVec.add(tempSiteName.substring(0, tempSiteName.length()-1));
								curExportDataVec.add(tempSkzc.substring(0, tempSkzc.length()-1));
							}
						}
					}
					
					allExportDataVec.add(courseVec.get(i));
					allExportDataVec.add(courseVec.get(i+1));
					allExportDataVec.add(curExportDataVec);
				}
			}
		}
		
		//获取当前选择班级的课表信息
		//20170223修改lupengfei 导出学生课表
		if("studentKcb".equalsIgnoreCase(exportType) ){
					String tempHbClass = "";
					String hbSetInfo = "";
					Vector classVec = new Vector();
					Vector curClassData = null;
					String tempClass = "";
					String curClassCode = "";
					boolean existFlag = false;
					
					//添加学生课表查询 20160209 lupengfei
					String userid="";
					String classid="";

					String sqlcls="select 行政班代码 from dbo.V_学生基本数据子类 where 学号='"+MyTools.fixSql(code)+"' ";
					Vector veccls=db.GetContextVector(sqlcls);
					if(veccls!=null&&veccls.size()>0){
						userid=code;
						classid=veccls.get(0).toString();
					}

					//-----------------------------------------------------
					
					//判断如果是导出所有班级课表,获取需要导出数据的班级名单
					if("classKcbAll".equalsIgnoreCase(exportType)){
						sql = "select distinct a.行政班代码,b.行政班名称 from V_排课管理_课程表主表 a " +
							//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
							"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
							"left join V_专业基本信息数据子类 c on c.专业代码=b.专业代码 " +
							"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
							"order by a.行政班代码";
						classVec = db.GetContextVector(sql);
					}else{
						classVec.add(classid);
						classVec.add(timetableName);
					}
						
					/**读取班级课表start*/
//					sql = "select distinct 行政班代码,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) " +
//						"from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
//					if("studentKcb".equalsIgnoreCase(exportType)){
//						sql += " and 行政班代码='" + MyTools.fixSql(classid) + "'";
//					}
//					sql += " and 授课计划明细编号<>'' order by 行政班代码,时间序列,cast(授课周次 as int)";
					
//					sql = "select * from (select distinct 行政班代码,时间序列,授课计划明细编号,课程代码,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,授课周次 " +
//							"from V_排课管理_课程表周详情表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//							"and 行政班代码='" + MyTools.fixSql(classid) + "' and 授课计划明细编号<>'') as b " +
//							"order by 时间序列,cast(授课周次 as int) ";
					sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程代码,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
						"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程代码,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
						"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
						"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
						"from V_排课管理_课程表周详情表 a " + 
						"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
						"and a.行政班代码='" + MyTools.fixSql(classid) + "' and a.授课计划明细编号<>'') as t " + 
						"order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
					tempVec = db.GetContextVector(sql);
					
					if(tempVec!=null && tempVec.size()>0){
						flag = true;
						//拼接课程周次
						while(flag){
							xzbdm = MyTools.StrFiltr(tempVec.get(0));
							sjxl = MyTools.StrFiltr(tempVec.get(1));
							skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
							kcbh = MyTools.StrFiltr(tempVec.get(3));
							kcmc = MyTools.StrFiltr(tempVec.get(4));
							skjsbh = MyTools.StrFiltr(tempVec.get(5));
							skjsxm = MyTools.StrFiltr(tempVec.get(6));
							cdbh = MyTools.StrFiltr(tempVec.get(7));
							cdmc = MyTools.StrFiltr(tempVec.get(8));
							skzc = MyTools.StrFiltr(tempVec.get(9));
							for(int i=0; i<10; i++){
								tempVec.remove(0);
							}
							
							for(int i=0; i<tempVec.size(); i+=10){
								if(xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) 
									&& skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+3))) 
									&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+5))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+7)))){
									skzc += "#"+MyTools.StrFiltr(tempVec.get(i+9));
									for(int j=0; j<10; j++){
										tempVec.remove(i);
									}
									i -= 10;
								}
							}
							vec.add(sjxl);
							vec.add(skjhmxbh);
							vec.add(kcmc);
							vec.add(skjsbh);
							vec.add(skjsxm);
							vec.add(cdbh);
							vec.add(cdmc);
							vec.add(skzc);
							vec.add(xzbdm);
							
							if(tempVec.size() == 0){
								flag = false;
							}
						}
												
						//拼接课程
						for(int i=0; i<vec.size(); i+=9){
							skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
							kcmc = MyTools.StrFiltr(vec.get(i+2));
							skjsbh = MyTools.StrFiltr(vec.get(i+3));
							skjsxm = MyTools.StrFiltr(vec.get(i+4));
							cdbh = MyTools.StrFiltr(vec.get(i+5));
							cdmc = MyTools.StrFiltr(vec.get(i+6));
							skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+7)), oddVec, evenVec);
							
							if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+8))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
								if(i > 0){
									tempAllData.add(sjxl);
									
									for(int j=0; j<tempVec.size(); j+=7){
										tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(j))+"｜";
										tempCourseName += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
										tempTeaCode += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
										tempTeaName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
										tempSiteCode += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
										tempSiteName += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
										tempSkzc += MyTools.StrFiltr(tempVec.get(j+6))+"｜";
									}
									
									tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
									tempAllData.add(tempCourseName.substring(0, tempCourseName.length()-1));
									tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length()-1));
									tempAllData.add(tempTeaName.substring(0, tempTeaName.length()-1));
									tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
									tempAllData.add(tempSiteName.substring(0, tempSiteName.length()-1));
									tempAllData.add(tempSkzc.substring(0, tempSkzc.length()-1));
									tempAllData.add(xzbdm);
									tempSkjhmxbh = "";
									tempCourseName = "";
									tempTeaCode = "";
									tempTeaName = "";
									tempSiteCode = "";
									tempSiteName = "";
									tempSkzc = "";
								}
								
								sjxl = MyTools.StrFiltr(vec.get(i));
								xzbdm = MyTools.StrFiltr(vec.get(i+8));
								tempVec = new Vector();
								tempVec.add(skjhmxbh);
								tempVec.add(kcmc);
								tempVec.add(skjsbh);
								tempVec.add(skjsxm);
								tempVec.add(cdbh);
								tempVec.add(cdmc);
								tempVec.add(skzc);
							}else{
								for(int j=0; j<tempVec.size(); j+=7){
									if(skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(j)))){
										tempVec.set(j+2, MyTools.StrFiltr(tempVec.get(j+2))+"&"+skjsbh);
										tempVec.set(j+3, MyTools.StrFiltr(tempVec.get(j+3))+"&"+skjsxm);
										tempVec.set(j+4, MyTools.StrFiltr(tempVec.get(j+4))+"&"+cdbh);
										tempVec.set(j+5, MyTools.StrFiltr(tempVec.get(j+5))+"&"+cdmc);
										tempVec.set(j+6, MyTools.StrFiltr(tempVec.get(j+6))+"&"+skzc);
										existFlag = true;
									}
								}
								
								if(existFlag == false){
									tempVec.add(skjhmxbh);
									tempVec.add(kcmc);
									tempVec.add(skjsbh);
									tempVec.add(skjsxm);
									tempVec.add(cdbh);
									tempVec.add(cdmc);
									tempVec.add(skzc);
								}
								existFlag = false;
							}
						}
						
						tempAllData.add(sjxl);
						
						for(int j=0; j<tempVec.size(); j+=7){
							tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(j))+"｜";
							tempCourseName += MyTools.StrFiltr(tempVec.get(j+1))+"｜";
							tempTeaCode += MyTools.StrFiltr(tempVec.get(j+2))+"｜";
							tempTeaName += MyTools.StrFiltr(tempVec.get(j+3))+"｜";
							tempSiteCode += MyTools.StrFiltr(tempVec.get(j+4))+"｜";
							tempSiteName += MyTools.StrFiltr(tempVec.get(j+5))+"｜";
							tempSkzc += MyTools.StrFiltr(tempVec.get(j+6))+"｜";
						}
						
						tempAllData.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
						tempAllData.add(tempCourseName.substring(0, tempCourseName.length()-1));
						tempAllData.add(tempTeaCode.substring(0, tempTeaCode.length()-1));
						tempAllData.add(tempTeaName.substring(0, tempTeaName.length()-1));
						tempAllData.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
						tempAllData.add(tempSiteName.substring(0, tempSiteName.length()-1));
						tempAllData.add(tempSkzc.substring(0, tempSkzc.length()-1));
						tempAllData.add(xzbdm);
					}
					/**读取班级课表end*/

					
					//查询合班信息
////					sql = "select distinct b.时间序列,b.行政班代码,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from V_规则管理_授课计划明细表 a " +
////						"inner join V_排课管理_课程表明细详情表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
////						"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
////						"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
////					if("classKcb".equalsIgnoreCase(exportType)){
////						sql += "and b.行政班代码<>'" + MyTools.fixSql(code) + "' ";
////					}
////					sql += "order by b.时间序列,c.授课计划明细编号";
//					sql = "with " +
//						"tempSkjh as (" +
//						"select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
//						"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'" +
//						")," +
//						"tempKcb as (" +
//						"select 时间序列,行政班代码,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 " +
//						"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') " +
//						"select b.时间序列,b.行政班代码,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from tempSkjh a " +
//						"left join tempKcb b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//						"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//						"where 1=1";
//					if("classKcb".equalsIgnoreCase(exportType)){
//						sql += "and b.行政班代码<>'" + MyTools.fixSql(code) + "' ";
//					}
//					sql += " order by b.时间序列,c.授课计划明细编号";
//					hbSetVec = db.GetContextVector(sql);
					
					//查询合班信息
					//20170125修改yeq 查询效率优化
					Vector classKcbVec = null;
					Vector classSkjhVec = null;
					Vector hbSkjhVec = null;
					tempVec = new Vector();
					hbSetVec = new Vector();
					
					sql = "select 时间序列,行政班代码,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 " +
						"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
					
					sql += " and 行政班代码<>'" + MyTools.fixSql(classid) + "'";
					
					sql += " and 授课计划明细编号<>'' order by 时间序列";
					classKcbVec = db.GetContextVector(sql);
					
					if(classKcbVec!=null && classKcbVec.size()>0){
						sql = "select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
							"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
						
						sql += " and b.行政班代码<>'" + MyTools.fixSql(classid) + "'";
						
						classSkjhVec = db.GetContextVector(sql);
						
						if(classSkjhVec!=null && classSkjhVec.size()>0){
							for(int i=0; i<classKcbVec.size(); i+=4){
								tempSkjhmxbh = MyTools.StrFiltr(classKcbVec.get(i+3));
								
								for(int j=0; j<classSkjhVec.size(); j++){
									if(tempSkjhmxbh.indexOf(MyTools.StrFiltr(classSkjhVec.get(j))) > -1){
										tempVec.add(MyTools.StrFiltr(classKcbVec.get(i)));
										tempVec.add(MyTools.StrFiltr(classKcbVec.get(i+1)));
										tempVec.add(MyTools.StrFiltr(classKcbVec.get(i+2)));
										tempVec.add(MyTools.StrFiltr(classSkjhVec.get(j)));
									}
								}
							}
							
							//查询所有合班设置信息
							sql = "select 授课计划明细编号 from V_规则管理_合班表";
							hbSkjhVec = db.GetContextVector(sql);
							
							if(hbSkjhVec!=null && hbSkjhVec.size()>0){
								for(int i=0; i<tempVec.size(); i+=4){
									tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(i+3));
									
									for(int j=0; j<hbSkjhVec.size(); j++){
										if(MyTools.StrFiltr(hbSkjhVec.get(j)).indexOf(tempSkjhmxbh) > -1){
											hbSetVec.add(tempVec.get(i));
											hbSetVec.add(tempVec.get(i+1));
											hbSetVec.add(tempVec.get(i+2));
											hbSetVec.add(tempVec.get(i+3));
											hbSetVec.add(MyTools.StrFiltr(hbSkjhVec.get(j)));
											break;
										}
									}
								}
							}
						}
					}
					
					//整理班级课程数据
					for(int i=0; i<classVec.size(); i+=2){
						curClassCode = MyTools.StrFiltr(classVec.get(i));
						curClassData = new Vector();
						
						for(int j=0; j<tempAllData.size(); j+=9){
							tempClass = MyTools.StrFiltr(tempAllData.get(j+8));
							
							//判断是否当前班级的课程
							if(tempClass.equalsIgnoreCase(curClassCode)){
								for(int k=0; k<8; k++){
									curClassData.add(MyTools.StrFiltr(tempAllData.get(j+k)));
								}
							}
						}
						
						allCourseVec.add(curClassCode);
						allCourseVec.add(curClassData);
					}
					
					//遍历所有数据
					for(int i=0; i<classVec.size(); i+=2){
						curCourseVec = (Vector)allCourseVec.get(allCourseVec.indexOf(classVec.get(i)) + 1);
						curExportDataVec = new Vector();
						
						for(int j=0; j<curCourseVec.size(); j+=8){
							tempHbClass = "";
							tempOrder = MyTools.StrFiltr(curCourseVec.get(j));
							skjhmxbh = MyTools.StrFiltr(curCourseVec.get(j+1));
							skjhmxbhArray = skjhmxbh.split("｜", -1);
							
							//检查合班信息
							for(int k=0; k<skjhmxbhArray.length; k++){
								for(int a=0; a<hbSetVec.size(); a+=5){
									//判断是否同一个时间序列
									if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a)))){
										hbSetInfo = MyTools.StrFiltr(hbSetVec.get(a+4));
										
										//判断是当前课程的合班信息并且不是当前授课计划的数据
										if(!skjhmxbhArray[k].equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(a+3))) && hbSetInfo.indexOf(skjhmxbhArray[k])>-1){
											tempHbClass += MyTools.StrFiltr(hbSetVec.get(a+2))+"、";
										}
									}
								}
								
								if(!"".equalsIgnoreCase(tempHbClass) && "、".equalsIgnoreCase(tempHbClass.substring(tempHbClass.length()-1))){
									tempHbClass = tempHbClass.substring(0, tempHbClass.length()-1);
								}
								tempHbClass += "｜";
							}
							tempHbClass = tempHbClass.substring(0, tempHbClass.length()-1);
							
							curExportDataVec.add(tempOrder);
							curExportDataVec.add(skjhmxbh);
							curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+2)));
							curExportDataVec.add(tempHbClass);
							curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+3)));
							curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+4)));
							curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+5)));
							curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+6)));
							curExportDataVec.add(MyTools.StrFiltr(curCourseVec.get(j+7)));
						}
						
						allExportDataVec.add(classVec.get(i));
						allExportDataVec.add(classVec.get(i+1));
						allExportDataVec.add(curExportDataVec);
					}

			//选修课+添加课程
			String sqlxxk="select distinct a.上课时间 as 时间序列,a.授课计划明细编号,b.课程代码,b.课程名称,a.授课教师编号,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次,'' as 行政班代码 " +
					"from [V_规则管理_选修课授课计划明细表] a,dbo.V_规则管理_选修课授课计划主表 b,V_基础信息_选修课程信息表 c " +
					"where a.授课计划主表编号=b.授课计划主表编号  and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and a.上课时间!='' and a.授课计划明细编号 in ( " +
					"SELECT [授课计划明细编号] FROM [V_规则管理_学生选修课关系表] where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 学号='" + MyTools.fixSql(userid) + "' )";
			Vector vecxxk=db.GetContextVector(sqlxxk);
			if(vecxxk!=null&&vecxxk.size()>0){
				for(int i=0;i<vecxxk.size();i=i+10){
					if(vecxxk.get(i).toString().indexOf(",")>-1){
						String[] sjxls=vecxxk.get(i).toString().split(",");
						for(int j=0;j<sjxls.length;j++){
							curExportDataVec.add(sjxls[j]);
							curExportDataVec.add(vecxxk.get(i+1).toString());
							curExportDataVec.add(vecxxk.get(i+3).toString());
							curExportDataVec.add("");
							curExportDataVec.add(vecxxk.get(i+4).toString());
							curExportDataVec.add(vecxxk.get(i+5).toString());
							curExportDataVec.add(vecxxk.get(i+6).toString());
							curExportDataVec.add(vecxxk.get(i+7).toString());
							curExportDataVec.add(vecxxk.get(i+8).toString());
						}
					}else{
						curExportDataVec.add(vecxxk.get(i).toString());
						curExportDataVec.add(vecxxk.get(i+1).toString());
						curExportDataVec.add(vecxxk.get(i+3).toString());
						curExportDataVec.add("");
						curExportDataVec.add(vecxxk.get(i+4).toString());
						curExportDataVec.add(vecxxk.get(i+5).toString());
						curExportDataVec.add(vecxxk.get(i+6).toString());
						curExportDataVec.add(vecxxk.get(i+7).toString());
						curExportDataVec.add(vecxxk.get(i+8).toString());
					}
				}
			}
			
			String sqlgr="  SELECT a.时间序列,a.相关课程表编号,b.课程代码,b.课程名称,b.授课教师编号,b.授课教师姓名,b.场地要求,b.场地名称,b.授课周次,'' as 行政班代码  FROM [dbo].[V_排课管理_添加课程信息表] a,V_排课管理_课程表明细详情表 b " +
					"where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and a.相关课程表编号=b.课程表明细编号  and a.类型='3' and a.学号='" + MyTools.fixSql(userid) + "'";
			Vector vecgr=db.GetContextVector(sqlgr);
			if(vecgr!=null&&vecgr.size()>0){
				for(int i=0;i<vecgr.size();i=i+10){
					curExportDataVec.add(vecgr.get(i).toString());
					curExportDataVec.add(vecgr.get(i+1).toString());
					curExportDataVec.add(vecgr.get(i+3).toString());
					curExportDataVec.add("");
					curExportDataVec.add(vecgr.get(i+4).toString());
					curExportDataVec.add(vecgr.get(i+5).toString());
					curExportDataVec.add(vecgr.get(i+6).toString());
					curExportDataVec.add(vecgr.get(i+7).toString());
					curExportDataVec.add(vecgr.get(i+8).toString());
				}
			}	
		}
		
		//查询备注信息
		if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) || "studentKcb".equalsIgnoreCase(exportType)){
			sql = "select distinct 行政班代码,备注 from V_排课管理_课程表主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			bzInfoVec = db.GetContextVector(sql);
		}else if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType)){
			sql = "select distinct 开学日期,结束日期,周次一,联系方式,周次二,考试一,周次三,考试二,周次四,考试三,年月 from V_规则管理_教师课表备注表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
			bzInfoVec = db.GetContextVector(sql);
			if(bzInfoVec!=null && bzInfoVec.size()>0){
				//备注内容
				bzCellContent = "" +
						"注：1、本学期所授课程 理论课时_________实训课时_________。（具体可咨询教务处或专业组长）" +
						"\n    2、本学期开学日期 " + MyTools.StrFiltr(bzInfoVec.get(0)) + " 结束日期 " + MyTools.StrFiltr(bzInfoVec.get(1)) +
						"\n    3、开学第" + MyTools.StrFiltr(bzInfoVec.get(2)) + "周交授课计划文字版、电子版各一份" +
						"\n    4、关于多媒体教室和实训室的问题请联系" + MyTools.StrFiltr(bzInfoVec.get(3)) + "。" +
						"\n    5、本学期 " + MyTools.StrFiltr(bzInfoVec.get(4)) + " 周课程考试为" + MyTools.StrFiltr(bzInfoVec.get(5)) + "；" +
						"\n       本学期 " + MyTools.StrFiltr(bzInfoVec.get(6)) + " 周课程考试为" + MyTools.StrFiltr(bzInfoVec.get(7)) + "；" +
						"\n       本学期 " + MyTools.StrFiltr(bzInfoVec.get(8)) + " 周课程考试为" + MyTools.StrFiltr(bzInfoVec.get(9)) + "；" +
						"\n    6、如有特殊原因需变更授课教师或地点，请先在教务处填写变更申请。" +
						"\n\n                                   教务处长签名：             教学校长签名：             " + MyTools.StrFiltr(bzInfoVec.get(10));
			}
		}
		
		//遍历所有数据
		for(int i=0; i<allExportDataVec.size(); i+=3){
			curName = MyTools.StrFiltr(allExportDataVec.get(i+1));
			curExportDataVec = (Vector)allExportDataVec.get(i+2);
			Sheet tempSheet = wb.openSheet("Sheet1");
			
			if(allExportDataVec.size() > 3){
				tempSheet = wb.createSheet(curName.length()>30?curName.substring(0, 30):curName, SheetInsertType.Before, "Sheet1");
			}
			
			for(int colNum=1; colNum<mzts+2; colNum++){
				for(int rowNum=1; rowNum<zjs+3; rowNum++){
					//生成标题
					if(colNum==1 && rowNum==1){
						tempSheet.openTable(colName[0]+"1:"+colName[mzts]+"1").merge();
						cell = tempSheet.openCell("A1");
						
						if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) || "studentKcb".equalsIgnoreCase(exportType))
							cellContent = schoolName + "   " + curName + xnxqmc.replace(" ", "") + "课表";
						else if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType))
							cellContent = schoolName + "   " + xnxqmc.replace(" ", "") + " " + curName + " 教师授课计划表";
						else if("courseKcb".equalsIgnoreCase(exportType) || "courseKcbAll".equalsIgnoreCase(exportType))
							cellContent = schoolName + "   " + xnxqmc.replace(" ", "") + " " + curName + " 课程课表";
							
						maxWidth = 4*cellContent.length()/(mzts+1);
						cell.setValue(cellContent);
					}else{
						if(colNum==1 && rowNum==2){
							cell = tempSheet.openCell(colName[colNum-1]+rowNum);//当前单元格
							cell.setValue("");
						}
						//判断是否为课程时间列
						else if(colNum==1 && rowNum>2){
							cell = tempSheet.openCell(colName[colNum-1]+rowNum);//当前单元格
							
							//判断是否为中午
							if(rowNum-2<=sw || rowNum-2>sw+zw){
								if(rowNum-2<=sw){
									cell.setValue("第"+orderNameArray[rowNum-3] + "节\n" + timeArray[rowNum-3]);
								}else{
									cell.setValue("第"+orderNameArray[rowNum-zw-3] + "节\n" + timeArray[rowNum-3]);
								}
							}else{
								cell.setValue("中"+orderNameArray[rowNum-sw-3] + "\n" + timeArray[rowNum-3]);
							}
						}
						//判断是否为星期行
						else if(colNum>1 && rowNum==2){
							cell = tempSheet.openCell(colName[colNum-1]+rowNum);//当前单元格
							
							//判断是否为大周课表
							if(mzts > 7){
								cell.setValue("大周"+(colNum-1));
							}else{
								cell.setValue(weekNameArray[colNum-2]);
							}
						}
						//课表内容
						else if(colNum>1 && rowNum>2){
							tempOrder = ((colNum-1)<10?"0"+(colNum-1):""+(colNum-1)) + ((rowNum-2)<10?"0"+(rowNum-2):""+(rowNum-2));
							tempIndex = curExportDataVec.indexOf(tempOrder);
							
							if(tempIndex > -1){
								tempSkjhmxbh = MyTools.StrFiltr(curExportDataVec.get(tempIndex+1));//授课计划明细编号
								if("".equalsIgnoreCase(tempSkjhmxbh)){
									cell = tempSheet.openCell(colName[colNum-1]+rowNum);//当前单元格
									cellContent = "";
								}else{
									tempCourseName = MyTools.StrFiltr(curExportDataVec.get(tempIndex+2));//课程名称
									tempClassName = MyTools.StrFiltr(curExportDataVec.get(tempIndex+3));//合班信息
									tempTeaCode = MyTools.StrFiltr(curExportDataVec.get(tempIndex+4));//教师姓名
									tempTeaName = MyTools.StrFiltr(curExportDataVec.get(tempIndex+5));//教师姓名
									tempSiteCode = MyTools.StrFiltr(curExportDataVec.get(tempIndex+6));//场地编号
									tempSiteName = MyTools.StrFiltr(curExportDataVec.get(tempIndex+7));//场地名称
									tempSkzc = MyTools.StrFiltr(curExportDataVec.get(tempIndex+8));//授课周次
									
									mergeNum = 0;
									flag = true;
									timeOrderFlag = tempOrder;
									
									//判断单元格是否可以合并
									while(flag){
										tempIndex += 9;
										if(tempIndex < curExportDataVec.size()){
											tempTime = MyTools.StringToInt(timeOrderFlag.substring(2))+1;
											
											//判断是否同一列并且授课计划明细编号和场地编号是否完全相同
											//if((colNum-1)==MyTools.StringToInt(MyTools.StrFiltr(curExportDataVec.get(tempIndex)).substring(0, 2))
											if(MyTools.StrFiltr(curExportDataVec.get(tempIndex)).equalsIgnoreCase(timeOrderFlag.substring(0, 2)+(tempTime<10?"0"+tempTime:""+tempTime))
													&& tempSkjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(curExportDataVec.get(tempIndex+1))) 
													&& tempTeaCode.equalsIgnoreCase( MyTools.StrFiltr(curExportDataVec.get(tempIndex+4)))
													&& tempSiteCode.equalsIgnoreCase(MyTools.StrFiltr(curExportDataVec.get(tempIndex+6)))
													&& tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(curExportDataVec.get(tempIndex+8)))){
												timeOrderFlag = MyTools.StrFiltr(curExportDataVec.get(tempIndex));
												mergeNum++;
											}else{
												flag = false;
											}
										}else{
											flag = false;
										}
									}
									//合并单元格
									if(mergeNum > 0){
										tempSheet.openTable(colName[colNum-1]+rowNum+":"+colName[colNum-1]+(rowNum+mergeNum)).merge();
									}
									cell = tempSheet.openCell(colName[colNum-1]+rowNum);//当前单元格
									rowNum += mergeNum;
									
									tempVec = parseCourseInfo(exportType, tempCourseName, tempTeaName, tempSiteName, tempSkzc, tempClassName, oddVec, evenVec);
									cellContent = MyTools.StrFiltr(tempVec.get(0));
									if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1))) > maxWidth){
										maxWidth = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1)));
									}
									if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2))) > maxHeight){
										maxHeight = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2)));
									}
								}
								cell.setValue(cellContent);
							}
						}
					}
				}
			}
			
			//备注信息
			if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) || "studentKcb".equalsIgnoreCase(exportType)){
				//备注标题
				cell = tempSheet.openCell(colName[0]+(zjs+3));//当前单元格
				cellContent = "备注";
				cell.setValue(cellContent);
				
				//备注内容
				tempSheet.openTable(colName[1]+(zjs+3)+":"+colName[mzts]+(zjs+3)).merge();
				cell = tempSheet.openCell(colName[1]+(zjs+3));//当前单元格
				cell.setValue(MyTools.StrFiltr(bzInfoVec.get(bzInfoVec.indexOf(allExportDataVec.get(i)) + 1)));
			}else if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType)){
				if(bzInfoVec!=null && bzInfoVec.size()>0){
					//备注内容
					tempSheet.openTable(colName[0]+(zjs+3)+":"+colName[mzts]+(zjs+3)).merge();
					tempSheet.openTable(colName[0]+(zjs+3)+":"+colName[mzts]+(zjs+3)).setVerticalAlignment(XlVAlign.xlVAlignTop);
					cell = tempSheet.openCell(colName[0]+(zjs+3));//当前单元格
					cell.setValue(bzCellContent);
				}
			}
			
			//设置单元格的水平对齐方式
			tempSheet.openTable(colName[0]+"1:"+colName[mzts]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
			tempSheet.openTable(colName[0]+"2:"+colName[mzts]+(zjs+2)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
			if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) || "studentKcb".equalsIgnoreCase(exportType)) 
				tempSheet.openCell(colName[0]+(zjs+3)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
			if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType)) 
				tempSheet.openCell(colName[0]+(zjs+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
			//设置单元格的垂直对齐方式
			//tempSheet.openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
			
			//设置课表边框线
			Border border = tempSheet.openTable(colName[0]+"2:"+colName[mzts]+(zjs+2)).getBorder();
			//设置表格边框的宽度、颜色
			border.setWeight(XlBorderWeight.xlThin);
			border.setLineColor(Color.black);
			
			//设置标题字体大小
			cell = tempSheet.openCell("A1");
			cell.getFont().setBold(true);
			if("courseKcb".equalsIgnoreCase(exportType) || "courseKcbAll".equalsIgnoreCase(exportType)){
				fontSize = 10;
			}else{
				fontSize = 12;
			}
			cell.getFont().setSize(fontSize);
			
			//设置课表标题行列字体大小
			fontSize = 10;
			tempSheet.openTable(colName[1]+"2:"+colName[5]+"2").getFont().setSize(fontSize);
			tempSheet.openTable(colName[0]+"3:"+colName[0]+(zjs+3)).getFont().setSize(fontSize);
			//设置课表内容行列字体大小
			fontSize = 9;
			tempSheet.openTable(colName[1]+"3:"+colName[mzts]+(zjs+3)).getFont().setSize(fontSize);
			//教师课表备注字体大小
			if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType)){
				//fontSize = 7.5f;
				tempSheet.openCell(colName[0]+(zjs+3)).getFont().setSize(fontSize);
			}
			
			//设置表格列宽
			double pageWidth = 64;
			maxWidth = pageWidth/mzts;
			tempSheet.openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(11);
			tempSheet.openTable(colName[1]+"1:"+colName[mzts]+"1").setColumnWidth(maxWidth);
			
			//设置表格行高
			double pageHeight = 0;
			if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) || "studentKcb".equalsIgnoreCase(exportType))
				pageHeight = 535;
			if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType))
				pageHeight = 500;
			if("courseKcb".equalsIgnoreCase(exportType) || "courseKcbAll".equalsIgnoreCase(exportType))
				pageHeight = 620;
			maxHeight = pageHeight/zjs;
			//设置单元格的垂直对齐方式
			//tempSheet.openTable(colName[0]+"1:"+colName[mzts]+"2").setVerticalAlignment(XlVAlign.xlVAlignTop);
			tempSheet.openTable(colName[0]+"1:"+colName[mzts]+"2").setRowHeight(30);
			tempSheet.openTable(colName[0]+"3:"+colName[mzts]+(zjs+2)).setRowHeight(maxHeight);
			
			//设置备注栏样式
			if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) || "studentKcb".equalsIgnoreCase(exportType)) 
				tempSheet.openTable(colName[0]+(zjs+3)+":"+colName[0]+(zjs+3)).setRowHeight(85);
			if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType)) 
				tempSheet.openTable(colName[0]+(zjs+3)+":"+colName[0]+(zjs+3)).setRowHeight(120);
		}
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "template.xls";
		String captionName = "";
		
		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("下载", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		if("classKcb".equalsIgnoreCase(exportType) || "classKcbAll".equalsIgnoreCase(exportType) || "studentKcb".equalsIgnoreCase(exportType))
			captionName = schoolName + "   " + timetableName + xnxqmc.replace(" ", "") + "课表";
		else if("teaKcb".equalsIgnoreCase(exportType) || "teaKcbAll".equalsIgnoreCase(exportType))
			captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 教师授课计划表";
		else if("courseKcb".equalsIgnoreCase(exportType) || "courseKcbAll".equalsIgnoreCase(exportType))
			captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 课程课表";
		
		poCtrl1.setCaption(captionName);
		poCtrl1.setFileTitle(captionName);//设置另存为窗口默认文件名
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
	}
	
	/**
	 * 课程表导出
	 * @date:2015-10-19
	 * @author:yeq
	 * @param xnxqbm 学年学期编码
	 * @param exportType 导出课表类型
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	public static void exportMultipleTimetable(HttpServletRequest request, String sAuth, String userCode, String xnxqbm, String exportType, String code, String timetableName) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String sql = "";
		Vector timeVec = null;
		Vector vec = new Vector();
		Vector hbSetVec = new Vector();
		Vector allKcbVec = new Vector();
		Vector curKcbVec = new Vector();
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"一","二","三","四","五","六","日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		Vector oddVec = new Vector();//单周周次
		Vector evenVec = new Vector();//双周周次
		String xnxqmc = "";//学年学期名称
		int mzts = 0;//每周天数
		int sw = 0;//上午节数
		int zw = 0;//中午节数
		int xw = 0;//下午节数
		int ws = 0;//晚上节数
		int zjs = 0;//总节数
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 50;
		float fontSize = 0;
		String curColName = "";
		
		Workbook wb = new Workbook();
		Cell cell;
		String cellContent = ""; //当前单元格的内容
		int weekIndex = 0;
		int jsIndex = 1;
		int tempJsIndex = 1;
		int tempZwjsIndex = 0;
		Vector tempVec = new Vector();
		
		String tempXq = "";
		String tempJc = "";
		int tempIndex = -1;
		int mergeNum = 0;//单元格合并数
		boolean flag = true;//用于判断是否可合并
		int jc = 0;
		
		String xzbdm = "";
		String xzbmc = "";
		String sjxl = "";
		String skjhmxbh = "";
		String kcbh = "";
		String kcmc = "";
		String hbClass = "";
		String skjsbh = "";
		String skjsxm = "";
		String cdbh = "";
		String cdmc = "";
		String skzc = "";
		String tempSkjhmxbh = "";//授课计划明细编号
		String tempOrder = "";//时间序列
		String tempCourseName = "";//课程名称
		String tempClassCode = "";//班级编号
		String tempClassName = "";//班级名称
		String tempTeaCode = "";//教师编号
		String tempTeaName = "";//教师姓名
		String tempSiteCode = "";//场地编号
		String tempSiteName = "";//场地名称
		String tempSkzc = "";//授课周次
		
		String skjhmxbhArray[] = new String[0];
		String captionName = "";
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		//获取当前学年学期的上课时间相关信息
		sql = "select distinct (select count(*) from V_规则管理_学期周次表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as 学期周次," +
			"t1.学年学期名称,t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
			"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间 " +
			"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
		timeVec = db.GetContextVector(sql);
		if(timeVec!=null && timeVec.size()>0){
			int xqzc = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(0)));
			for(int i=1; i<xqzc+1; i+=2){
				oddVec.add(i);
			}
			for(int i=2; i<xqzc+1; i+=2){
				evenVec.add(i);
			}
			
			xnxqmc = MyTools.StrFiltr(timeVec.get(1));
			mzts = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(2)));
			sw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(3)));
			zw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(4)));
			xw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(5)));
			ws = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(6)));
			zjs = sw+zw+xw+ws;
			timeArray = MyTools.StrFiltr(timeVec.get(7)).split(",", -1);
		}
		
		//获取当前专业所有班级的课表信息
		if("allClassKcb".equalsIgnoreCase(exportType)){
			String tempNum = "";
			String tempRemark = "";
			String tempHbClass = "";
			String hbSetInfo = "";
			Vector classInfoVec = null;
			Vector courseVec = new Vector();
			
			//查询相关行政班信息
//			sql = "select a.行政班代码,a.行政班名称,(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数,b.备注 from V_学校班级_数据子类 a " +
//				"left join V_排课管理_课程表主表 b on b.行政班代码=a.行政班代码 " +
//				"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//				"and a.专业代码='" + MyTools.fixSql(code) + "' order by a.行政班代码";
			sql = "select a.班级编号,a.班级名称," +
				//"(select count(*) from V_学生基本数据子类 where 行政班代码=a.行政班代码 and 学生状态 in ('01','05')) as 总人数," +
				"a.总人数,c.教室名称,isnull(b.备注,'') " +
				//"from V_学校班级_数据子类 a " +
				"from V_基础信息_班级信息表 a " +
				"left join V_排课管理_课程表主表 b on b.行政班代码=a.班级编号 and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
				"left join V_教室数据类 c on c.教室编号=a.教室编号 " +
				"where a.系部代码='" + MyTools.fixSql(code) + "' " +
				"and cast(left(a.年级代码,2) as int) in (" +
				"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)-2," +
				"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)-1," +
				"cast(right(left('" + MyTools.fixSql(xnxqbm) + "',4),2) as int)) " +
				"order by a.班级编号";
			classInfoVec = db.GetContextVector(sql);
			
			if(classInfoVec!=null && classInfoVec.size()>0){
				//查询合班信息
//				sql = "select distinct b.时间序列,b.行政班名称,a.授课计划明细编号,c.授课计划明细编号 from V_规则管理_授课计划明细表 a " +
//					"inner join V_排课管理_课程表明细详情表 b on b.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//					"inner join V_规则管理_合班表 c on c.授课计划明细编号 like '%'+a.授课计划明细编号+'%' " +
//					"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//					"order by b.时间序列";
//				hbSetVec = db.GetContextVector(sql);
				
				//查询合班信息
				//20170125修改yeq 查询效率优化
				Vector classKcbVec = null;
				Vector classSkjhVec = null;
				Vector hbSkjhVec = null;
				tempVec = new Vector();
				hbSetVec = new Vector();
				
				sql = "select 时间序列,行政班名称,授课计划明细编号 from V_排课管理_课程表明细详情表 " +
					"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				sql += " and 授课计划明细编号<>'' order by 时间序列";
				classKcbVec = db.GetContextVector(sql);
				
				if(classKcbVec!=null && classKcbVec.size()>0){
					sql = "select a.授课计划明细编号 from V_规则管理_授课计划明细表 a left join V_规则管理_授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
						"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
					classSkjhVec = db.GetContextVector(sql);
					
					if(classSkjhVec!=null && classSkjhVec.size()>0){
						for(int i=0; i<classKcbVec.size(); i+=3){
							tempSkjhmxbh = MyTools.StrFiltr(classKcbVec.get(i+2));
							
							for(int j=0; j<classSkjhVec.size(); j++){
								if(tempSkjhmxbh.indexOf(MyTools.StrFiltr(classSkjhVec.get(j))) > -1){
									tempVec.add(MyTools.StrFiltr(classKcbVec.get(i)));
									tempVec.add(MyTools.StrFiltr(classKcbVec.get(i+1)));
									tempVec.add(MyTools.StrFiltr(classSkjhVec.get(j)));
								}
							}
						}
						
						//查询所有合班设置信息
						sql = "select 授课计划明细编号 from V_规则管理_合班表";
						hbSkjhVec = db.GetContextVector(sql);
						
						if(hbSkjhVec!=null && hbSkjhVec.size()>0){
							for(int i=0; i<tempVec.size(); i+=3){
								tempSkjhmxbh = MyTools.StrFiltr(tempVec.get(i+2));
								
								for(int j=0; j<hbSkjhVec.size(); j++){
									if(MyTools.StrFiltr(hbSkjhVec.get(j)).indexOf(tempSkjhmxbh) > -1){
										hbSetVec.add(tempVec.get(i));
										hbSetVec.add(tempVec.get(i+1));
										hbSetVec.add(tempVec.get(i+2));
										hbSetVec.add(MyTools.StrFiltr(hbSkjhVec.get(j)));
										break;
									}
								}
							}
						}
					}
				}
				
				//查询相关课程表信息
//				sql = "select distinct 行政班代码,时间序列,授课计划明细编号,课程名称,授课教师编号,授课教师姓名,场地编号,场地名称,cast(授课周次 as int) as 授课周次 " +
//					"from V_排课管理_课程表周详情表 where 状态='1' and 课程类型 in ('01','02') and 授课计划明细编号<>'' " +
//					"and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//					"and 专业代码='" + MyTools.fixSql(code) + "' " +
//					"order by 行政班代码,时间序列,cast(授课周次 as int)";
//				sql = "select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//					"from V_排课管理_课程表周详情表 a " +
//					//"left join V_学校班级_数据子类 b on b.行政班代码=a.行政班代码 " +
//					"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
//					"where a.状态='1' and a.授课计划明细编号<>'' " +
//					"and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
//					"and b.系部代码='" + MyTools.fixSql(code) + "' " +
//					"order by a.行政班代码,a.时间序列,cast(a.授课周次 as int)";
				sql = "select t.行政班代码,t.时间序列,t.授课计划明细编号,t.课程名称,t.授课教师编号,t.授课教师姓名,t.场地编号,t.场地名称,t.授课周次 " + 
					"from (select distinct a.行政班代码,a.时间序列,a.授课计划明细编号,a.课程名称,a.授课教师编号,a.授课教师姓名,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
					"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
					"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
					"from V_排课管理_课程表周详情表 a " + 
					"left join V_基础信息_班级信息表 b on b.班级编号=a.行政班代码 " +
					"where a.状态='1' and 授课计划明细编号<>'' " +
					"and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
					"and b.系部代码='" + MyTools.fixSql(code) + "' " +
					") as t order by t.行政班代码,t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
				tempVec = db.GetContextVector(sql);
				
				if(tempVec!=null && tempVec.size()>0){
					vec = new Vector();
					flag = true;
					
					//拼接课程周次
					while(flag){
						xzbdm = MyTools.StrFiltr(tempVec.get(0));
						sjxl = MyTools.StrFiltr(tempVec.get(1));
						skjhmxbh = MyTools.StrFiltr(tempVec.get(2));
						kcmc = MyTools.StrFiltr(tempVec.get(3));
						skjsbh = MyTools.StrFiltr(tempVec.get(4));
						skjsxm = MyTools.StrFiltr(tempVec.get(5));
						cdbh = MyTools.StrFiltr(tempVec.get(6));
						cdmc = MyTools.StrFiltr(tempVec.get(7));
						skzc = MyTools.StrFiltr(tempVec.get(8));
						for(int i=0; i<9; i++){
							tempVec.remove(0);
						}
						
						for(int i=0; i<tempVec.size(); i+=9){
							if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) 
								&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6)))){
								skzc += "#"+MyTools.StrFiltr(tempVec.get(i+8));
								for(int j=0; j<9; j++){
									tempVec.remove(i);
								}
								i -= 9;
							}
						}
						vec.add(xzbdm);
						vec.add(sjxl);
						vec.add(skjhmxbh);
						vec.add(kcmc);
						vec.add(skjsbh);
						vec.add(skjsxm);
						vec.add(cdbh);
						vec.add(cdmc);
						vec.add(skzc);
						
						if(tempVec.size() == 0){
							flag = false;
						}
					}
				}
				
				tempVec = new Vector();
				for(int i=0; i<vec.size(); i+=9){
					skjsbh = MyTools.StrFiltr(vec.get(i+4));
					skjsxm = MyTools.StrFiltr(vec.get(i+5));
					cdbh = MyTools.StrFiltr(vec.get(i+6));
					cdmc = MyTools.StrFiltr(vec.get(i+7));
					skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+8)), oddVec, evenVec);
					
					if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+2)))){
						if(i > 0){
							tempVec.add(xzbdm);
							tempVec.add(sjxl);
							tempVec.add(skjhmxbh);
							tempVec.add(kcmc);
							tempVec.add(tempTeaCode);
							tempVec.add(tempTeaName);
							tempVec.add(tempSiteCode);
							tempVec.add(tempSiteName);
							tempVec.add(tempSkzc);
						}
						
						xzbdm = MyTools.StrFiltr(vec.get(i));
						sjxl = MyTools.StrFiltr(vec.get(i+1));
						skjhmxbh = MyTools.StrFiltr(vec.get(i+2));
						kcmc = MyTools.StrFiltr(vec.get(i+3));
						tempTeaCode = skjsbh;
						tempTeaName = skjsxm;
						tempSiteCode = cdbh;
						tempSiteName = cdmc;
						tempSkzc = skzc;
					}else{
						tempTeaCode += "&"+skjsbh;
						tempTeaName += "&"+skjsxm;
						tempSiteCode += "&"+cdbh;
						tempSiteName += "&"+cdmc;
						tempSkzc += "&"+skzc;
					}
				}
				tempVec.add(xzbdm);
				tempVec.add(sjxl);
				tempVec.add(skjhmxbh);
				tempVec.add(kcmc);
				tempVec.add(tempTeaCode);
				tempVec.add(tempTeaName);
				tempVec.add(tempSiteCode);
				tempVec.add(tempSiteName);
				tempVec.add(tempSkzc);
				
				for(int i=0; i<tempVec.size(); i+=9){
					skjhmxbh = MyTools.StrFiltr(tempVec.get(i+2));
					kcmc = MyTools.StrFiltr(tempVec.get(i+3));
					skjsbh = MyTools.StrFiltr(tempVec.get(i+4));
					skjsxm = MyTools.StrFiltr(tempVec.get(i+5));
					cdbh = MyTools.StrFiltr(tempVec.get(i+6));
					cdmc = MyTools.StrFiltr(tempVec.get(i+7));
					skzc = MyTools.StrFiltr(tempVec.get(i+8));
					
					//检查合班信息
					hbClass = "";
					for(int j=0; j<hbSetVec.size(); j+=4){
						if(MyTools.StrFiltr(tempVec.get(i+1)).equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(j)))){
							hbSetInfo = MyTools.StrFiltr(hbSetVec.get(j+3));
							
							if(!skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(hbSetVec.get(j+2))) && hbSetInfo.indexOf(skjhmxbh)>-1){
								hbClass += MyTools.StrFiltr(hbSetVec.get(j+1))+"、";
								break;
							}
						}
					}
					
					if(!xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) || !sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))){
						if(i > 0){
							courseVec.add(xzbdm);
							courseVec.add(sjxl);
							courseVec.add(tempSkjhmxbh);
							courseVec.add(tempCourseName);
							courseVec.add(tempHbClass);
							courseVec.add(tempTeaCode);
							courseVec.add(tempTeaName);
							courseVec.add(tempSiteCode);
							courseVec.add(tempSiteName);
							courseVec.add(tempSkzc);
						}
						
						xzbdm = MyTools.StrFiltr(tempVec.get(i));
						sjxl = MyTools.StrFiltr(tempVec.get(i+1));
						tempSkjhmxbh = skjhmxbh;
						tempCourseName = kcmc;
						tempHbClass = hbClass;
						tempTeaCode = skjsbh;
						tempTeaName = skjsxm;
						tempSiteCode = cdbh;
						tempSiteName = cdmc;
						tempSkzc = skzc;
					}else{
						tempSkjhmxbh += "｜"+skjhmxbh;
						tempCourseName += "｜"+kcmc;
						tempHbClass += "｜"+hbClass;
						tempTeaCode += "｜"+skjsbh;
						tempTeaName += "｜"+skjsxm;
						tempSiteCode += "｜"+cdbh;
						tempSiteName += "｜"+cdmc;
						tempSkzc += "｜"+skzc;
					}
				}
				courseVec.add(xzbdm);
				courseVec.add(sjxl);
				courseVec.add(tempSkjhmxbh);
				courseVec.add(tempCourseName);
				courseVec.add(tempHbClass);
				courseVec.add(tempTeaCode);
				courseVec.add(tempTeaName);
				courseVec.add(tempSiteCode);
				courseVec.add(tempSiteName);
				courseVec.add(tempSkzc);
				
				//添加体锻课  lupengfei 20170928			
				int tytag=0;//体育课标记
				int tdcs=0;//体锻课次数
				int zwyk=0;//中午是否有課
				
				//获取操场编号
				String sqlcc="select [教室编号] from [dbo].[V_教室数据类] where [校区代码] in (select [系部代码] from dbo.V_基础信息_班级信息表  where [班级编号]='" + MyTools.fixSql(code) + "' and [教室名称]='操场' ) ";
				Vector veccc=db.GetContextVector(sqlcc);
				
				//获取实际上课周数
				String sqlskzs="select [实际上课周数] from V_规则管理_学年学期表 where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' ";
				Vector vecskzs=db.GetContextVector(sqlskzs);
					
				//获取每周天数
				int mztsx=0;
				String sqlmzts="select [每周天数] from [dbo].[V_规则管理_学年学期表] where [学年学期编码]='" + MyTools.fixSql(xnxqbm) + "' ";
				Vector vecmzts=db.GetContextVector(sqlmzts);
				if(vecmzts!=null&&vecmzts.size()>0){
					mztsx=Integer.parseInt(vecmzts.get(0).toString());
				}
				
				//时间序列,授课计划明细编号,课程名称,授课教师姓名,实际场地名称,授课周次详情
				
				//循环班级
				for(int b=0;b<classInfoVec.size();b=b+5){
					tdcs=0;//体锻课次数		
					//判断哪天没体育课,中午添加体锻课
					String ts="";
					for(int i=1;i<=mztsx;i++){
						tytag=0;//体育课标记
						zwyk=0;//中午是否有課
						if(i<10){
							ts="0"+i;
						}else{
							ts=i+"";
						}
						for(int j=0;j<courseVec.size();j=j+10){ 
							if(courseVec.get(j).toString().equals(classInfoVec.get(b).toString())){//班级相同
								if(ts.equals(courseVec.get(j+1).toString().substring(0, 2))){//天数相同
									if(courseVec.get(j+3).toString().indexOf("体育")>-1){//有体育课
										tytag=1;
									}
								}
							}					
						}
						if(tytag==0){			
							for(int k=0;k<courseVec.size();k=k+10){
								if(courseVec.get(k).toString().equals(classInfoVec.get(b).toString())){//班级相同
									if((ts+"05").equals(courseVec.get(k+1).toString())){//中午有课
										zwyk=1;									
									}
								}		
							}
							if(zwyk==0){ 
								if(tdcs<2){
									//添加体锻课
									int existkc=0;//班级有课
									for(int t=0;t<courseVec.size();t=t+10){
										if(courseVec.get(t).toString().equals(classInfoVec.get(b).toString())){//班级相同
											existkc=1;
											break;
										}
									}
									if(existkc==1){
										//时间序列,授课计划明细编号,课程名称,实际场地名称,授课教师姓名,授课周次详情
										courseVec.add(classInfoVec.get(b).toString());
										courseVec.add(ts+"05");//时间序列
										courseVec.add("SKJHMX_TD");//授课计划明细编号
										courseVec.add("体锻");//课程名称		
										courseVec.add("");//合班
										courseVec.add("");//授课教师编号
										courseVec.add("");//授课教师姓名
										courseVec.add("");//实际场地编号
										courseVec.add("");//实际场地名称
										courseVec.add("");//授课周次详情
										
										tdcs++;
									}							
								}
							}else{
								zwyk=0;
							}
						}
					}
				}

				//=============================================================================================			
				
				
				flag = false;
				for(int i=0; i<classInfoVec.size(); i+=5){
					tempClassCode = MyTools.StrFiltr(classInfoVec.get(i));
					curKcbVec = new Vector();
					
					for(int j=1; j<mzts+1; j++){
						for(int k=1; k<zjs+1; k++){
							tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
							
							flag = false;
							for(int a=0; a<courseVec.size(); a+=10){
								if(tempClassCode.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a))) && tempOrder.equalsIgnoreCase(MyTools.StrFiltr(courseVec.get(a+1)))){
									curKcbVec.add(tempOrder);//时间序列
									curKcbVec.add(courseVec.get(a+2));//授课计划明细编号
									curKcbVec.add(courseVec.get(a+3));//课程名称
									curKcbVec.add(courseVec.get(a+4));//合班班级
									curKcbVec.add(courseVec.get(a+5));//授课教师编号
									curKcbVec.add(courseVec.get(a+6));//授课教师姓名
									curKcbVec.add(courseVec.get(a+7));//场地编号
									curKcbVec.add(courseVec.get(a+8));//场地名称
									curKcbVec.add(courseVec.get(a+9));//授课周次
									flag = true;
									break;
								}
							}
							if(flag == false){
								curKcbVec.add(tempOrder);
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
							}
						}
					}
					allKcbVec.add(classInfoVec.get(i+1));
					allKcbVec.add(classInfoVec.get(i+2));
					allKcbVec.add(classInfoVec.get(i+3));
					allKcbVec.add(classInfoVec.get(i+4));
					allKcbVec.add(curKcbVec);
				}
			}
		}
		
		//获取当前层级所有教师的课表信息
		if("allTeaKcb".equalsIgnoreCase(exportType)){
			Vector teaVec = null;
			Vector xxkVec = null;
			String hbInfo = "";
			
			//获取教师名单
//			sql = "with cte1 as (select t1.工号,t1.姓名 from V_教职工基本数据子类 t1 " +
//				"inner join V_USER_AUTH t2 on t2.UserCode=t1.工号 " +
//				"inner join V_权限层级关系表 t3 on t3.权限编号=t2.AuthCode " +
//				"inner join V_层级表 t4 on t4.层级编号=t3.层级编号 where t4.层级编号='" + MyTools.fixSql(code) + "') " +
//				"select distinct * from (select a.工号,a.姓名 from cte1 a " +
//				"inner join (select distinct 专业代码,授课教师编号 from V_排课管理_课程表周详情表 " +
//				"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 课程代码<>'') b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' " +
//				"union all " +
//				"select a.工号,a.姓名 from cte1 a " +
//				"inner join V_规则管理_选修课授课计划主表 b on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%'+a.工号+'%' " +
//				"where b.状态='1' and b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "') as t where 工号 is not null order by 姓名";
			sql = "select distinct * from (" +
				"select 工号,姓名 from V_教职工基本数据子类 where 工号='" + MyTools.fixSql(userCode) + "' " +
				"union all " +
				"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
				"inner join (select distinct t1.授课教师编号 from V_排课管理_课程表周详情表 t1 " +
				//"left join V_学校班级_数据子类 t2 on t2.行政班代码=t1.行政班代码 " +
				"left join V_基础信息_班级信息表 t2 on t2.班级编号=t1.行政班代码 " +
				"where t1.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and t1.课程代码<>''";
			//权限判断
			if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
				sql += " and (1=2";
				//班主任
				if(sAuth.indexOf(bzr) > -1){
					sql += " or t2.班主任工号='" + MyTools.fixSql(userCode) + "'";
				}
				//系部教务人员
				if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
					sql += " or t2.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(userCode) + "')";
				}
				sql += ")";
			}
			sql += ") b on '@'+replace(b.授课教师编号,'+','@+@')+'@' like '%@'+a.工号+'@%' ";
			
			//判断如果是管理员或全校教务权限，需查询选修课教师信息
			if(sAuth.indexOf(admin)>-1 || sAuth.indexOf(jxzgxz)>-1 || sAuth.indexOf(qxjdzr)>-1 || sAuth.indexOf(qxjwgl)>-1){
				sql += "union all " +
					"select a.工号,a.姓名 from V_教职工基本数据子类 a " +
					"inner join (select 授课教师编号 from V_规则管理_选修课授课计划主表 where 状态='1' and 学年学期编码='" + MyTools.fixSql(xnxqbm) + "') b " +
					"on '@'+replace(replace(b.授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@'+a.工号+'@%'";
			}
			sql += ") as t where 工号 is not null order by 姓名";
			teaVec = db.GetContextVector(sql);
			
			if(teaVec!=null && teaVec.size()>0){
				//获取选修课信息
				sql = "select c.时间序列,a.授课计划明细编号,b.课程名称,a.选修班名称,a.授课教师编号,c.实际场地编号,c.实际场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a " +
					"inner join V_规则管理_选修课授课计划主表 b on b.授课计划主表编号=a.授课计划主表编号 " +
					"left join V_排课管理_选修课课程表信息表 c on c.授课计划明细编号=a.授课计划明细编号 " +
					"where b.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' " +
					"order by c.时间序列,cast((case substring(a.授课周次, 2, 1) when '｜' then substring(a.授课周次, 1, 1) when '&' then substring(a.授课周次, 1, 1) when '#' then substring(a.授课周次, 1, 1) " +
					"when '-' then substring(a.授课周次, 1, 1) when 'd' then '1' when 'v' then '2' else substring(a.授课周次, 1, 2) end) as int)";
				xxkVec = db.GetContextVector(sql);
				
				//获取合班设置信息
				sql = "select distinct a.授课计划明细编号 from V_规则管理_合班表 a " +
					"left join V_规则管理_授课计划明细表 b on a.授课计划明细编号 like '%'+b.授课计划明细编号+'%' " +
					"left join V_规则管理_授课计划主表 c on c.授课计划主表编号=b.授课计划主表编号 " +
					"where c.学年学期编码='" + MyTools.fixSql(xnxqbm) + "'";
				hbSetVec = db.GetContextVector(sql);
				
				//获取教师课程信息
//				sql = "select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次 " +
//					"from V_排课管理_课程表周详情表 a where a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and (";
//				for(int i=0; i<teaVec.size(); i+=2){
//					sql += "'@'+replace(replace(授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
//				}
//				sql = sql.substring(0, sql.length()-4);
//				sql += ") order by a.时间序列,cast(a.授课周次 as int)";
				sql = "select t.时间序列,t.授课计划明细编号,t.行政班代码,t.行政班名称,t.课程代码,t.课程名称,t.授课教师编号,t.场地编号,t.场地名称,t.授课周次 " + 
					"from (select distinct a.时间序列,a.授课计划明细编号,a.行政班代码,a.行政班名称,a.课程代码,a.课程名称,a.授课教师编号,a.场地编号,a.场地名称,cast(a.授课周次 as int) as 授课周次," + 
					"(select min(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_1," + 
					"(select max(cast(授课周次 as int)) from V_排课管理_课程表周详情表 where 时间序列=a.时间序列 and 授课计划明细编号=a.授课计划明细编号) as num_2 " + 
					"from V_排课管理_课程表周详情表 a " + 
					"where a.状态='1' and a.学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and (";
				for(int i=0; i<teaVec.size(); i+=2){
					sql += "'@'+replace(replace(授课教师编号,'+','@+@'),'&','@&@')+'@' like '%@" + MyTools.StrFiltr(teaVec.get(i)) + "@%' or ";
				}
				sql = sql.substring(0, sql.length()-4);
				sql += ")) as t order by t.时间序列,t.num_1,t.num_2,t.授课周次,t.授课计划明细编号";
				tempVec = db.GetContextVector(sql);
				
				if(tempVec!=null && tempVec.size()>0){
					vec = new Vector();
					flag = true;
					
					//拼接同一课程周次
					while(flag){
						sjxl = MyTools.StrFiltr(tempVec.get(0));
						skjhmxbh = MyTools.StrFiltr(tempVec.get(1));
						xzbdm = MyTools.StrFiltr(tempVec.get(2));
						xzbmc = MyTools.StrFiltr(tempVec.get(3));
						kcbh = MyTools.StrFiltr(tempVec.get(4));
						kcmc = MyTools.StrFiltr(tempVec.get(5));
						skjsbh = MyTools.StrFiltr(tempVec.get(6));
						cdbh = MyTools.StrFiltr(tempVec.get(7));
						cdmc = MyTools.StrFiltr(tempVec.get(8));
						skzc = MyTools.StrFiltr(tempVec.get(9));
						for(int i=0; i<10; i++){
							tempVec.remove(0);
						}
						
						for(int i=0; i<tempVec.size(); i+=10){
							if(sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i))) && skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+1)))
								&& xzbdm.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+2))) && kcbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+4))) 
								&& skjsbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+6))) && cdbh.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(i+7)))){
								skzc += "#"+MyTools.StrFiltr(tempVec.get(i+9));
								for(int j=0; j<10; j++){
									tempVec.remove(i);
								}
								i -= 10;
							}
						}
						vec.add(sjxl);
						vec.add(skjhmxbh);
						vec.add(xzbmc);
						vec.add(kcmc);
						vec.add(skjsbh);
						vec.add(cdbh);
						vec.add(cdmc);
						vec.add(skzc);
						
						if(tempVec.size() == 0){
							flag = false;
						}
					}
					tempVec = new Vector();
					for(int i=0; i<vec.size(); i+=8){
						cdmc = MyTools.StrFiltr(vec.get(i+6));
						skzc = KbcxBean.formatSkzcShow(MyTools.StrFiltr(vec.get(i+7)), oddVec, evenVec);
						
						if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !skjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+1))) 
							|| !skjsbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) || !cdbh.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
							if(i > 0){
								tempVec.add(sjxl);
								tempVec.add(skjhmxbh);
								tempVec.add(xzbmc);
								tempVec.add(kcmc);
								tempVec.add(tempTeaCode);
								tempVec.add(tempSiteCode);
								tempVec.add(tempSiteName);
								tempVec.add(tempSkzc);
							}
							
							sjxl = MyTools.StrFiltr(vec.get(i));
							skjhmxbh = MyTools.StrFiltr(vec.get(i+1));
							xzbmc = MyTools.StrFiltr(vec.get(i+2));
							kcmc = MyTools.StrFiltr(vec.get(i+3));
							skjsbh = MyTools.StrFiltr(vec.get(i+4));
							cdbh = MyTools.StrFiltr(vec.get(i+5));
							tempTeaCode = skjsbh;
							tempSiteCode = cdbh;
							tempSiteName = cdmc;
							tempSkzc = skzc;
						}else{
							tempTeaCode += "&"+MyTools.StrFiltr(vec.get(i+4));
							tempSiteCode += "&"+cdbh;
							tempSiteName += "&"+cdmc;
							tempSkzc += "&"+skzc;
						}
					}
					tempVec.add(sjxl);
					tempVec.add(skjhmxbh);
					tempVec.add(xzbmc);
					tempVec.add(kcmc);
					tempVec.add(tempTeaCode);
					tempVec.add(tempSiteCode);
					tempVec.add(tempSiteName);
					tempVec.add(tempSkzc);
					
					//合并合班课程信息
					for(int i=0; i<tempVec.size(); i+=8){
						sjxl = MyTools.StrFiltr(tempVec.get(i));
						skjhmxbh = MyTools.StrFiltr(tempVec.get(i+1));
						
						for(int j=0; j<hbSetVec.size(); j++){
							hbInfo = MyTools.StrFiltr(hbSetVec.get(j));
							
							if(hbInfo.indexOf(skjhmxbh) > -1){
								for(int k=(i+8); k<tempVec.size(); k+=8){
									if(!sjxl.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(k)))){
										break;
									}
									
									if(hbInfo.indexOf(MyTools.StrFiltr(tempVec.get(k+1))) > -1){
										tempVec.set(i+2, MyTools.StrFiltr(tempVec.get(i+2))+"、"+MyTools.StrFiltr(tempVec.get(k+2)));
										
										for(int a=0; a<8; a++){
											tempVec.remove(k);
										}
										k -= 8;
									}
								}
							}
						}
					}
				}
				
				for(int i=0; i<teaVec.size(); i+=2){
					tempTeaCode = MyTools.StrFiltr(teaVec.get(i));
					curKcbVec = new Vector();
					
					for(int j=1; j<mzts+1; j++){
						for(int k=1; k<zjs+1; k++){
							tempOrder = (j<10?"0"+j:""+j) + (k<10?"0"+k:""+k);
							tempSkjhmxbh = "";
							tempClassName = "";
							tempCourseName = "";
							tempTeaName = "";
							tempSiteCode = "";
							tempSiteName = "";
							tempSkzc = "";
							
							//添加普通课程信息
							flag = false;
							for(int a=0; a<tempVec.size(); a+=8){
								if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(tempVec.get(a)))){
									break;
								}
								
								if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(tempVec.get(a))) && MyTools.StrFiltr(tempVec.get(a+4)).indexOf(tempTeaCode)>-1){
									tempSkjhmxbh += MyTools.StrFiltr(tempVec.get(a+1))+"｜";
									tempClassName += MyTools.StrFiltr(tempVec.get(a+2))+"｜";
									tempCourseName += MyTools.StrFiltr(tempVec.get(a+3))+"｜";
									tempTeaName += "｜";
									tempSiteCode += MyTools.StrFiltr(tempVec.get(a+5))+"｜";
									tempSiteName += MyTools.StrFiltr(tempVec.get(a+6))+"｜";
									tempSkzc += MyTools.StrFiltr(tempVec.get(a+7))+"｜";
									flag = true;
								}
							}
							//添加选修课信息
							for(int a=0; a<xxkVec.size(); a+=8){
								if(tempOrder.equalsIgnoreCase(MyTools.StrFiltr(xxkVec.get(a))) && MyTools.StrFiltr(xxkVec.get(a+4)).indexOf(tempTeaCode)>-1){
									tempSkjhmxbh += MyTools.StrFiltr(xxkVec.get(a+1))+"｜";
									tempCourseName += MyTools.StrFiltr(xxkVec.get(a+2))+"｜";
									tempTeaName += "｜";
									tempClassName += MyTools.StrFiltr(xxkVec.get(a+3))+"｜";
									tempSiteCode += MyTools.StrFiltr(xxkVec.get(a+5))+"｜";
									tempSiteName += MyTools.StrFiltr(xxkVec.get(a+6))+"｜";
									tempSkzc += MyTools.StrFiltr(xxkVec.get(a+7))+"｜";
									flag = true;
									
									if(MyTools.StringToInt(tempOrder) < MyTools.StringToInt(MyTools.StrFiltr(xxkVec.get(a)))){
										break;
									}
								}
							}
							if(flag == false){
								curKcbVec.add(tempOrder);
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
								curKcbVec.add("");
							}else{
								curKcbVec.add(tempOrder);
								curKcbVec.add(tempSkjhmxbh.substring(0, tempSkjhmxbh.length()-1));
								curKcbVec.add(tempCourseName.substring(0, tempCourseName.length()-1));
								curKcbVec.add(tempTeaName.substring(0, tempTeaName.length()-1));
								curKcbVec.add("");
								curKcbVec.add(tempClassName.substring(0, tempClassName.length()-1));
								curKcbVec.add(tempSiteCode.substring(0, tempSiteCode.length()-1));
								curKcbVec.add(tempSiteName.substring(0, tempSiteName.length()-1));
								curKcbVec.add(tempSkzc.substring(0, tempSkzc.length()-1));
							}
						}
					}
					
					allKcbVec.add(MyTools.StrFiltr(teaVec.get(i+1)));
					allKcbVec.add("");
					allKcbVec.add("");
					allKcbVec.add("");
					allKcbVec.add(curKcbVec);
				}
			}else{
				allKcbVec.add("没有相关教师信息");
				allKcbVec.add("");
				allKcbVec.add("");
				allKcbVec.add("");
				allKcbVec.add(curKcbVec);
			}
		}
		
		//生成文件
		Sheet tempSheet = wb.openSheet("Sheet1");
		String totalColName = "";
		int totalNum = allKcbVec.size()/5+4;
		
		if(totalNum > colName.length){
			if(totalNum%colName.length == 0){
				totalColName = colName[(totalNum-1)/colName.length-1]+colName[colName.length-1];
			}else{
				totalColName = colName[totalNum/colName.length-1]+colName[totalNum%colName.length-1];
			}
		}else{
			totalColName = colName[totalNum-1];
		}
		
		for(int colNum=1; colNum<allKcbVec.size()/5+5; colNum++){
			if(colNum > 4){
				curKcbVec = (Vector)allKcbVec.get((colNum-5)*5+4);
			}
			
			if(colNum > colName.length){
				if(colNum%colName.length == 0){
					curColName = colName[(colNum-1)/colName.length-1]+colName[colName.length-1];
				}else{
					curColName = colName[colNum/colName.length-1]+colName[colNum%colName.length-1];
				}
			}else{
				curColName = colName[colNum-1];
			}
			
			for(int rowNum=1; rowNum<zjs*mzts+6; rowNum++){
				if(jsIndex > zjs){
					jsIndex = 1;
					tempJsIndex = 1;
					tempZwjsIndex = 0;
				}
				
				//生成标题
				if(colNum==1 && rowNum==1){
					tempSheet.openTable(colName[0]+"1:"+totalColName+"1").merge();
					cell = tempSheet.openCell("A1");
					
					if("allClassKcb".equalsIgnoreCase(exportType))
						cellContent = "                         " + schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 全日制课表";
					else if("allTeaKcb".equalsIgnoreCase(exportType))
						cellContent = "                            " + schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + "授课计划表";
					
					//maxWidth = cellContent.length()/(allKcbVec.size()/4);
					cell.setValue(cellContent);
					
					//设置标题字体大小
					cell = tempSheet.openCell("A1");
					cell.getFont().setBold(true);
					fontSize = 18;
					cell.getFont().setSize(fontSize);
				}else{
					//备注
					if(rowNum==zjs*mzts+5){
						if("allClassKcb".equalsIgnoreCase(exportType)){
							if(colNum==1){
								tempSheet.openTable(colName[0]+rowNum+":"+colName[3]+rowNum).merge();
								cell = tempSheet.openCell(colName[0]+rowNum);
								cell.setValue("备注");
							}
							
							if(colNum > 4){
								cell = tempSheet.openCell(colName[colNum-1]+rowNum);
								cellContent = MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+3));
								cell.setValue(cellContent+"\n");
							}
						}
					}
					//判断星期列
					else if(colNum==1 && rowNum>4){
						tempSheet.openTable(colName[0]+rowNum+":"+colName[0]+(rowNum+zjs-1)).merge();
						cell = tempSheet.openCell(colName[0]+rowNum);
						
						//判断是否为大周
						if(mzts > 7){
							cellContent = orderNameArray[weekIndex];
						}else{
							cellContent = weekNameArray[weekIndex];
							weekIndex++;
						}
						
						cell.setValue(cellContent);
						rowNum += (zjs-1);
					}
					//节数列
					else if(colNum==2 && rowNum>4){
						cell = tempSheet.openCell(colName[1]+rowNum);
						if(jsIndex>sw && jsIndex<=sw+zw){
							cellContent = "中"+orderNameArray[tempZwjsIndex];
							tempZwjsIndex++;
						}else{
							cellContent = tempJsIndex+"";
							tempJsIndex++;
						}
						jsIndex++;
						cell.setValue(cellContent);
					}
					//时间列
					else if(colNum==3 && rowNum>4){
						tempSheet.openTable(colName[2]+rowNum+":"+colName[3]+rowNum).merge();
						cell = tempSheet.openCell(colName[2]+rowNum);
						cell.setValue(timeArray[jsIndex-1]);
						jsIndex++;
					}
					
					//课表内容
					else if(colNum>4 && rowNum>1){
						if(allKcbVec!=null && allKcbVec.size()>0){
							if(rowNum == 2){
								if("allTeaKcb".equalsIgnoreCase(exportType))
									tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+2)).merge();
									
								cell = tempSheet.openCell(curColName+rowNum);
								cell.setValue(MyTools.StrFiltr(allKcbVec.get((colNum-5)*5)));
							}
							else if(rowNum == 3){
								cell = tempSheet.openCell(curColName+rowNum);
								cell.setValue(MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+1)) + "人");
							}
							else if(rowNum == 4){
								cell = tempSheet.openCell(curColName+rowNum);
								cell.setValue(MyTools.StrFiltr(allKcbVec.get((colNum-5)*5+2)));
							}
							else{
								//翟旭超修改pageoffice格式20171130 begin
								tempSheet.openCell(curColName+rowNum).setVerticalAlignment(XlVAlign.xlVAlignTop);
								//end
								
								tempXq = (rowNum-4)/zjs<10?"0"+((rowNum-4)/zjs+1):""+((rowNum-4)/zjs+1);
								tempJc = (rowNum-4)%zjs<10?"0"+(rowNum-4)%zjs:""+(rowNum-4)%zjs;
								if(MyTools.StringToInt(tempJc) == 0){
									tempJc = zjs<10?"0"+zjs:""+zjs;
									tempXq = MyTools.StrFiltr((MyTools.StringToInt(tempXq)-1));
									tempXq = MyTools.StringToInt(tempXq)<10?"0"+tempXq:tempXq;
								}
								tempOrder = tempXq+tempJc;
								tempIndex = curKcbVec.indexOf(tempOrder);
								
								if(tempIndex > -1){
									tempSkjhmxbh = MyTools.StrFiltr(curKcbVec.get(tempIndex+1));
									tempCourseName = MyTools.StrFiltr(curKcbVec.get(tempIndex+2));
									tempClassName = MyTools.StrFiltr(curKcbVec.get(tempIndex+3));
									tempTeaCode = MyTools.StrFiltr(curKcbVec.get(tempIndex+4));
									tempTeaName = MyTools.StrFiltr(curKcbVec.get(tempIndex+5));
									tempSiteCode = MyTools.StrFiltr(curKcbVec.get(tempIndex+6));
									tempSiteName = MyTools.StrFiltr(curKcbVec.get(tempIndex+7));
									tempSkzc = MyTools.StrFiltr(curKcbVec.get(tempIndex+8));
									
									mergeNum = 0;
									flag = true;
									
									//判断单元格是否可以合并
									while(flag){
										tempIndex += 9;
										if(tempIndex < curKcbVec.size()){
											//判断是否完全相同的课程
											if(tempXq.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex)).substring(0, 2))
												&& tempSkjhmxbh.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+1))) 
												&& tempTeaCode.equalsIgnoreCase( MyTools.StrFiltr(curKcbVec.get(tempIndex+4)))
												&& tempSiteCode.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+6)))
												&& tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(curKcbVec.get(tempIndex+8)))
												){
												
												//教师课表空单元格合并
												jc = MyTools.StringToInt(MyTools.StrFiltr(curKcbVec.get(tempIndex)).substring(2));
												
												//判断是不是上午或下午(中午和晚上不做操作)
												if(jc<=sw || jc>sw+zw+1 && jc<=sw+zw+xw){
													mergeNum++;
												}else{
													flag = false;
												}
											}else{
												flag = false;
											}
										}else{
											flag = false;
										}
									}
									//合并单元格
									if(mergeNum > 0){
										tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+mergeNum)).merge();
									}
									cell = tempSheet.openCell(curColName+rowNum);//当前单元格
									rowNum += mergeNum;
									
									if(!"".equalsIgnoreCase(tempSkjhmxbh)){
										tempVec = parseCourseInfo(exportType, tempCourseName, tempTeaName, tempSiteName, tempSkzc, tempClassName, oddVec, evenVec);
										cellContent = MyTools.StrFiltr(tempVec.get(0));
//										if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1))) > maxWidth){
//											maxWidth = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(1)));
//										}
//										if(MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2))) > maxHeight){
//											maxHeight = MyTools.StringToDouble(MyTools.StrFiltr(tempVec.get(2)));
//										}
										
									}else{
										cellContent = "";
									}
									cell.setValue(cellContent+"\n");
								}else{
									mergeNum = 0;
									flag = true;
									
									//判断单元格是否可以合并
									while(flag){
										//教师课表空单元格合并
										jc = MyTools.StringToInt(tempJc)+1;
										
										//判断是不是上午或下午(中午和晚上不做操作)
										if(jc<=sw || jc>sw+zw+1 && jc<=sw+zw+xw){
											tempJc = jc<10?"0"+jc:""+jc;
											tempOrder = tempXq+tempJc;
											//判断是否有课
											if(curKcbVec.indexOf(tempOrder) < 0){
												mergeNum++;
											}else{
												flag = false;
											}
										}else{
											flag = false;
										}
									}
									//合并单元格
									if(mergeNum > 0){
										tempSheet.openTable(curColName+rowNum+":"+curColName+(rowNum+mergeNum)).merge();
									}
									rowNum += mergeNum;
								}
							}
						}
					}
				}
			}
		}
		
		//设置单元格的水平对齐方式
		tempSheet.openTable(colName[0]+"1:"+colName[0]+(allKcbVec.size()+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		if("allClassKcb".equalsIgnoreCase(exportType))
			tempSheet.openTable(colName[0]+"2:"+totalColName+(mzts*zjs+4)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
			tempSheet.openCell(colName[0]+(mzts*zjs+5)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		if("allTeaKcb".equalsIgnoreCase(exportType))
			tempSheet.openTable(colName[0]+"2:"+totalColName+(mzts*zjs+5)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		//设置单元格的垂直对齐方式
		//tempSheet.openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置课表边框线
		Border border = tempSheet.openTable(colName[0]+"5:"+totalColName+(zjs*mzts+4)).getBorder();
		if("allClassKcb".equalsIgnoreCase(exportType))
			border = tempSheet.openTable(colName[0]+"5:"+totalColName+(zjs*mzts+5)).getBorder();
		//设置表格边框的宽度、颜色
		border.setBorderType(XlBorderType.xlFullGrid);
		border.setWeight(XlBorderWeight.xlMedium);
		//border.setWeight(XlBorderWeight.values(XlBorderWeight.xlHairline,XlBorderWeight.xlThin,XlBorderWeight.xlMedium,XlBorderWeight.xlThick));
		border.setLineColor(Color.black);
		
		border = tempSheet.openTable(colName[4]+"2:"+totalColName+(4)).getBorder();
		border.setBorderType(XlBorderType.xlFullGrid);
		border.setWeight(XlBorderWeight.xlMedium);
		border.setLineColor(Color.black);
		
		//设置课表内容行列字体大小
		if("allClassKcb".equalsIgnoreCase(exportType))
			fontSize = 10;
		if("allTeaKcb".equalsIgnoreCase(exportType))
			fontSize = 12;
		tempSheet.openTable(colName[4]+"1:"+totalColName+"4").getFont().setSize(fontSize);
		
		if("allClassKcb".equalsIgnoreCase(exportType)){
			fontSize = 8;
			tempSheet.openTable(colName[1]+"5:"+totalColName+(zjs*mzts+5)).getFont().setSize(fontSize);
		}
		if("allTeaKcb".equalsIgnoreCase(exportType)){
			fontSize = 10;
			tempSheet.openTable(colName[4]+"5:"+totalColName+(zjs*mzts+4)).getFont().setSize(fontSize);
		}
			
		//设置表格列宽
		//tempSheet.openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(15);
		if(allKcbVec.size()/5 < 2){
			maxWidth = 154/(allKcbVec.size()/5);
		}else{
			maxWidth = 77;
		}
		tempSheet.openTable(colName[4]+"1:"+totalColName+"1").setColumnWidth(maxWidth);
		
		//设置表格行高
		double pageHeight = 0;
		if("allClassKcb".equalsIgnoreCase(exportType))
			pageHeight = 500;
		if("allTeaKcb".equalsIgnoreCase(exportType))
			pageHeight = 600;
		maxHeight = pageHeight/(zjs*mzts);
		tempSheet.openTable(colName[0]+"1:"+totalColName+"1").setRowHeight(30);
		tempSheet.openTable(colName[0]+"5:"+totalColName+(zjs*mzts+4)).setRowHeight(maxHeight);
		if("allClassKcb".equalsIgnoreCase(exportType)) 
			tempSheet.openTable(colName[0]+(zjs*mzts+5)+":"+colName[0]+(zjs*mzts+5)).setRowHeight(95);
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "";
		if("allClassKcb".equalsIgnoreCase(exportType))
			fileName = "allClassTemplate.xls";
		if("allTeaKcb".equalsIgnoreCase(exportType))
			fileName = "allTeaTemplate.xls";

		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("下载", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		if("allClassKcb".equalsIgnoreCase(exportType))
			captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + " 全日制课表";
		else if("allTeaKcb".equalsIgnoreCase(exportType))
			captionName = schoolName + "   " + xnxqmc.replace(" ", "") + " " + timetableName + "授课计划表";
		
		poCtrl1.setCaption(captionName);
		poCtrl1.setFileTitle(captionName);
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
	}
	
	/**
	 * 解析课程信息格式
	 * @date:2015-10-20
	 * @author:yeq
	 * @param type 数据类型
	 * @param courseName 课程名称
	 * @param teaName 教师名称
	 * @param siteName 场地名称
	 * @param weekNum 授课周次
	 * @param hbClassName 合班班级名称
	 * @param oddVec 单周周次
	 * @param evenVec 双周周次
	 * @throws SQLException
	 */
	public static Vector parseCourseInfo(String type, String courseName, String teaName, String siteName, String weekNum, String hbClassName, Vector oddVec, Vector evenVec){
		Vector vec = new Vector();
		String str = "";
		double fontSize = 10; 
		double maxWidth = 0;
		double maxHeight = 0;
		double curHeight = 0;
		String dataType = type.substring(0, 3);
		
		String splitStr = "";
		if("all".equalsIgnoreCase(dataType)){
			splitStr = "，";
		}else{
			splitStr = "\n";
		}
		
		//判断是单门课程还是拼接的课程
		if(courseName.indexOf('｜') > -1){
			String tempCourseName[] = courseName.split("｜", -1);
			String tempHbClass[] = hbClassName.split("｜", -1);
			String tempTeaName[] = teaName.split("｜", -1);
			String tempSiteName[] = siteName.split("｜", -1);
			String tempWeekNum[] = weekNum.split("｜", -1);
			
			if("all".equalsIgnoreCase(dataType)){
				if("allClassKcb".equalsIgnoreCase(type))
					curHeight = 55*tempCourseName.length;
				else
					curHeight = 45*tempCourseName.length;
			}else{
				if("classKcb".equalsIgnoreCase(type))
					curHeight = 45*tempCourseName.length;
				else
					curHeight = 35*tempCourseName.length;
			}
			
			if("classTable".equalsIgnoreCase(type)){
				curHeight = 45*tempCourseName.length;
			}
			
			if(curHeight > maxHeight)
				maxHeight = curHeight;
			
			for(int i=0; i<tempCourseName.length; i++){
				str += tempCourseName[i];
				
				if(!"".equalsIgnoreCase(tempHbClass[i])){
					str += splitStr + "(并" + tempHbClass[i] + ")";
				}
				
				if("all".equalsIgnoreCase(dataType)){
					if(tempWeekNum[i].equals("")){
						//str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + splitStr + tempSiteName[i].replaceAll("\\+", "、");
					}else{
						str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + " (" + parseWeekShow(tempWeekNum[i], oddVec, evenVec) + ") " + splitStr + tempSiteName[i].replaceAll("\\+", "、");
					}
				}else{
					if(tempWeekNum[i].equals("")){
						//str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + tempSiteName[i].replaceAll("\\+", "、");
					}else{
						str += splitStr + tempTeaName[i].replaceAll("\\+", "、") + " (" + parseWeekShow(tempWeekNum[i], oddVec, evenVec) + ") " + tempSiteName[i].replaceAll("\\+", "、");
					}
				}
				
				if(i < tempCourseName.length-1){
					if("all".equalsIgnoreCase(dataType)){
						str += "；";
					}else{
						str += splitStr;
					}
					
				}
				
				//判断课程表最大宽度
				if(fontSize/6*courseName.length() > maxWidth)
					maxWidth = fontSize/6*courseName.length();
				if("all".equalsIgnoreCase(dataType)){
					if(fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length()) > maxWidth)
						maxWidth = fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length());
					if(siteName.length()+2 >maxWidth)
						maxWidth = fontSize/8*(tempSiteName[i].length()+2);
				}else{
					if(fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length()+tempSiteName[i].length()+2) > maxWidth)
						maxWidth = fontSize/8*(tempTeaName[i].length()+parseWeekShow(tempWeekNum[i], oddVec, evenVec).length()+tempSiteName[i].length()+2);
				}
			}
		}else{
			str = courseName;
			
			if(!"".equalsIgnoreCase(hbClassName)){
				str += splitStr + "(并" + hbClassName + ")";
			}
			
			if("all".equalsIgnoreCase(dataType)){
				if(weekNum.equals("")){
					//str += splitStr + teaName.replaceAll("\\+", "、") + splitStr + siteName.replaceAll("\\+", "、");
				}else{
					str += splitStr + teaName.replaceAll("\\+", "、") + " (" + parseWeekShow(weekNum, oddVec, evenVec) + ") " + splitStr + siteName.replaceAll("\\+", "、");
				}
			}else{
				if(weekNum.equals("")){
					//str += splitStr + teaName.replaceAll("\\+", "、") + siteName.replaceAll("\\+", "、");
				}else{
					str += splitStr + teaName.replaceAll("\\+", "、") + " (" + parseWeekShow(weekNum, oddVec, evenVec) + ") " + siteName.replaceAll("\\+", "、");
				}
			}
			
			//判断课程表最大宽度
			if(fontSize/6*courseName.length() > maxWidth)
				maxWidth = fontSize/6*courseName.length();
			if("all".equalsIgnoreCase(dataType)){
				if(fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length()) > maxWidth)
					maxWidth = fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length());
				if(siteName.length()+2 >maxWidth)
					maxWidth = fontSize/8*(siteName.length()+2);
			}else{
				if(fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length()+siteName.length()+2) > maxWidth)
					maxWidth = fontSize/8*(teaName.length()+parseWeekShow(weekNum, oddVec, evenVec).length()+siteName.length()+2);
			}
		}
		
		vec.add(str);
		vec.add(maxWidth);
		vec.add(maxHeight);
		return vec;
	}
	
	/**解析周次显示方式
	 * @param skzc 周次原数据
	 * @param oddVec 单周周次
	 * @param evenVec 双周周次
	 * **/
	public static String parseWeekShow(String skzc, Vector oddVec, Vector evenVec){
		String result = "";
		String skzcArray[] = skzc.split("&", -1);
		
		for(int i=0; i<skzcArray.length; i++){
			if(skzcArray[i].indexOf("-") > -1){
				result +=  skzcArray[i] + "周";
			}else if("odd".equalsIgnoreCase(skzcArray[i])){
				result += "单周";
			}else if("even".equalsIgnoreCase(skzcArray[i])){
				result += "双周";
			}else if(skzcArray[i].indexOf("#")>-1 || (skzcArray[i].indexOf("#")<0&&skzcArray[i].length()>0&&skzcArray[i].length()<3)){
				//判断是否为单双周
				String curWeekArray[] = skzcArray[i].split("#");
				boolean oddFlag = true;
				boolean evenFlag = true;
				
				if(oddVec.size() == curWeekArray.length){
					for(int j=0; j<oddVec.size(); j++){
						if(!MyTools.StrFiltr(oddVec.get(i)).equalsIgnoreCase(curWeekArray[i])){
							oddFlag = false;
							break;
						}
					}
				}else{
					oddFlag = false;
				}
				
				if(evenVec.size() == curWeekArray.length){
					for(int j=0; j<evenVec.size(); j++){
						if(!MyTools.StrFiltr(evenVec.get(i)).equalsIgnoreCase(curWeekArray[i])){
							evenFlag = false;
							break;
						}
					}
				}else{
					evenFlag = false;
				}
				
				if(oddFlag == true)
					result += "单周";
				else if(evenFlag == true)
					result += "双周";
				else
					result += "第" + skzcArray[i].replaceAll("#", ",") + "周";
			}else{
				result += skzcArray[i];
			}
			
			result += "&";
		}
		if(result.length() > 0){
			result = result.substring(0, result.length()-1);
		}
		
		return result;
	}
	
	
	/**
	 * 教室课表导出
	 * @date:2015-12-09
	 * @author:lupengfei
	 * @param xnxq+jxxz 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	public static void exportClassTable(HttpServletRequest request, String exportType, String school, String week, String xnxq, String jxxz, String titleinfo) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		
		String xnxqmc = "";//学年学期名称
		int mzts = 0;//每周天数
		int sw = 0;//上午节数
		int zw = 0;//中午节数
		int xw = 0;//下午节数
		int ws = 0;//晚上节数
		int zjs = 0;//总节数
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 40;
		float fontSize = 0;
		
		Workbook wb = new Workbook();
		Cell cell;
		String cellContent = ""; //当前单元格的内容
		Vector tempVec = new Vector();
		Vector hbSetVec = new Vector();
		Vector timeVec = null;
		
		String tempOrder = "";//时间序列
		int tempIndex = -1;
		int mergeNum = 0;//单元格合并数
		boolean flag = true;//用于判断是否可合并
		
		String tempSkjhmxbh = "";//授课计划明细编号
		String timeOrder = "";//时间序列
		String tempCourseName = "";//课程名称
		String tempClassName = "";//班级名称
		String tempTeaCode = "";//教师编号
		String tempTeaName = "";//教师姓名
		String tempSiteCode = "";//场地编号
		String tempSiteName = "";//场地名称
		String tempSkzc = "";//授课周次
		
		String skjhmxbhArray[] = new String[0];
		
		Vector vector = null;
		Vector vector1 = null;
		Vector vector2 = null;
		Vector vector3 = null;
		Vector vector4 = null;
		Vector vec = new Vector();
		Vector vec2 = new Vector();
		Vector vec3 = new Vector();
		Vector vec6 = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String sql7 = "";
		String classroom="";
		String timedata="";

		
		//获取当前学年学期的上课时间相关信息
		sql = "select distinct t1.学年学期名称,t1.每周天数,t1.上午节数,t1.中午节数,t1.下午节数,t1.晚上节数," +
					"stuff((select ','+开始时间+'～'+结束时间 from V_规则管理_节次时间表 t2 where t2.学年学期编码=t1.学年学期编码 order by 开始时间 for XML PATH('')),1,1,'') as 节次时间 " +
					"from V_规则管理_学年学期表 t1 where t1.状态='1' and t1.学年学期编码='" + MyTools.fixSql(xnxq+jxxz) + "'";
		timeVec = db.GetContextVector(sql);
		if(timeVec!=null && timeVec.size()>0){
					xnxqmc = MyTools.StrFiltr(timeVec.get(0));
					mzts = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(1)));
					sw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(2)));
					zw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(3)));
					xw = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(4)));
					ws = MyTools.StringToInt(MyTools.StrFiltr(timeVec.get(5)));
					zjs = sw+zw+xw+ws;
					timeArray = MyTools.StrFiltr(timeVec.get(6)).split(",", -1);
		}
		
		if(school.equals("")&&week.equals("")&&xnxq.equals("")&&jxxz.equals("")){
			school="C01";
			//house="01";
			//floor="0";
			week="01";
			jxxz="01";
			sql4="select 学年+学期 from [dbo].[V_规则管理_学年学期表] order by [学年学期编码] desc";
			vector4=db.GetContextVector(sql4);
			xnxq=vector4.get(0).toString();
		}
		
		sql = "SELECT 教室编号  FROM [dbo].[V_教室数据类] where [校区代码]='"+MyTools.fixSql(school)+"' ";			
		vector=db.GetContextVector(sql);
		if(vector!=null&&vector.size()>0){
			for(int i=0;i<vector.size();i++){
				classroom+=vector.get(i).toString()+",";
			}
			classroom=classroom.substring(0,classroom.length()-1);
			if(vector.size()<11){
				maxWidth=150/vector.size();
			}else{
				maxWidth=15;
			}
			
			//this.setMSG(classroom);
		}
		sql1 = "SELECT 教室名称  FROM [dbo].[V_教室数据类] where [校区代码]='"+MyTools.fixSql(school)+"' ";			
		vector1=db.GetContextVector(sql1);
		
		sql2 = "select 节次  from V_规则管理_节次时间表 where 学年学期编码='" + MyTools.fixSql(MyTools.StrFiltr(xnxq)) + MyTools.fixSql(MyTools.StrFiltr(jxxz)) + "' order by 开始时间";
		vector2=db.GetContextVector(sql2);
		if(vector2!=null&&vector2.size()>0){
			for(int i=0;i<vector2.size();i++){
				timedata+=vector2.get(i).toString()+",";
			}
			timedata=timedata.substring(0,timedata.length()-1);
			maxHeight=620/vector2.size();
			//this.setMSG2(timedata);
		}
		
//		if(vector!=null&&vector.size()>0){
//	
//			for(int i=0;i<vector.size();i++){
//				cdbh+="场地编号 like '%"+vector.get(i).toString()+"%' or ";
//				cdbh2+="a.场地要求 like '%"+vector.get(i).toString()+"%' or ";
//			}
//			
//			if(!cdbh.equals("")){
//				cdbh=cdbh.substring(0, cdbh.length()-3);
//				sql3="select distinct c.行政班名称,c.时间序列,c.课程名称,c.授课教师姓名,c.实际场地编号,c.实际场地名称," +
//					"stuff((select '#'+d.授课周次 from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) d " +
//					"where c.行政班名称=d.行政班名称 and c.时间序列=d.时间序列 and c.课程名称=d.课程名称 and c.授课教师姓名=d.授课教师姓名 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称 for xml path('')),1,1,'') as 授课周次详情 " +
//					"from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) c order by c.实际场地编号,c.时间序列 ";
//				vector3=db.GetContextVector(sql3);
//				if(vector3!=null&&vector3.size()>0){
//					for(int i=0;i<vector3.size();i=i+7){
//						skzcxq=merge(vector3.get(i+6).toString());
//						sqlpkgl+="select '"+vector3.get(i).toString()+"' as 行政班名称,'"+vector3.get(i+1).toString()+"' as 时间序列,'"+vector3.get(i+2).toString()+"' as 课程名称,'"+vector3.get(i+3).toString()+"' as 授课教师姓名,'"+vector3.get(i+4).toString()+"' as 实际场地编号,'"+vector3.get(i+5).toString()+"' as 实际场地名称,'"+skzcxq+"' as 授课周次详情 union ";
//					}
//					sqlpkgl=sqlpkgl.substring(0, sqlpkgl.length()-6);
//				}
//					
//				//选修课
//				if(!cdbh2.equals("")){
//					cdbh2=cdbh2.substring(0, cdbh2.length()-3);
//					sql6="select a.选修班名称 as 行政班名称,a.上课时间 as 时间序列,b.课程名称,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and ("+cdbh2+")";
//					vec6=db.GetContextVector(sql6);
//					if(vec6!=null&&vec6.size()>0){
//						for(int i=0;i<vec6.size();i=i+7){
//							String[] sjxls=vec6.get(i+1).toString().split(",");
//							for(int j=0;j<sjxls.length;j++){
//								sqlpkgl2+=" select '"+vec6.get(i).toString()+"' as 行政班名称,'"+sjxls[j]+"' as 时间序列,'"+vec6.get(i+2).toString()+"' as 课程名称,'"+vec6.get(i+3).toString()+"' as 授课教师姓名,'"+vec6.get(i+4).toString()+"' as 实际场地编号,'"+vec6.get(i+5).toString()+"' as 实际场地名称,'"+vec6.get(i+6).toString()+"' as 授课周次详情  union ";
//							}
//						}
//						sqlpkgl2=sqlpkgl2.substring(0, sqlpkgl2.length()-6);
//					}
//				}
//				if(sqlpkgl2.equals("")){
//					sql7="";
//				}else{
//					sql7="select * from ("+sqlpkgl2+") e where substring(时间序列,1,2)='"+week+"' ";
//					sqlpkgl=sqlpkgl+" union "+sql7;
//				}
//			}
//			if(!sqlpkgl.equals("")){
//				sql5="select stuff((select '｜'+d.行政班名称 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 行政班名称,c.时间序列," +
//					"stuff((select '｜'+d.课程名称 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 课程名称, " +
//					"stuff((select '｜'+d.授课教师姓名 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课教师姓名,实际场地编号,实际场地名称," +
//					"stuff((select '｜'+d.授课周次详情 from ("+sqlpkgl+") d where c.时间序列=d.时间序列 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称  for xml path('')),1,1,'') as 授课周次详情 " +
//					" from ("+sqlpkgl+") c ";
//			}else{
//				sql5=" select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表 where 1=2 "; 
//			}
//			//sql3="SELECT a.行政班名称,a.时间序列,a.课程名称,a.授课教师姓名,a.实际场地名称,a.授课周次详情 FROM ("+sqlpkgl+") a where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(a.时间序列,1,2)='"+MyTools.fixSql(week)+"' and a.实际场地编号 like '%"+MyTools.fixSql(vector.get(0).toString())+"%'";
//			vector3 = db.GetContextVector(sql5);
//			
//		}
		
		String skjhbh=null;//授课计划明细编号
		String xnxqbm=null;//学年学期编码
		String skjsbh=null;//授课教师编号
		String skjsxm=null;//授课教师姓名
		String zyname=null;//专业名称
		String kechid=null;//课程代码
		String kechna=null;//课程名称
		String clasid=null;//行政班代码
		String clasna=null;//行政班名称
		String timexl=null;//时间序列
		String sjcdbh=null;//实际场地编号
		String sjcdmc=null;//实际场地名称
		String skzcxq=null;//授课周次详情
		String zt=null;//状态
		int d=0;
		String sqlpkgl="";
		String sqlpkgl2="";
		Vector vectk = null;
		String sqltk="";//调课
		
		String cdbh="";
		String cdbh2="";
		for(int i=0;i<vector.size();i++){
			cdbh+="场地编号 like '%"+vector.get(i).toString()+"%' or ";
			cdbh2+="a.场地要求 like '%"+vector.get(i).toString()+"%' or ";
		}
		
		if(!cdbh.equals("")){
			cdbh=cdbh.substring(0, cdbh.length()-3);
//			sql3="select distinct c.行政班名称,c.时间序列,c.课程名称,c.授课教师姓名,c.实际场地编号,c.实际场地名称," +
//					"stuff((select '#'+d.授课周次 from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) d " +
//					"where c.行政班名称=d.行政班名称 and c.时间序列=d.时间序列 and c.课程名称=d.课程名称 and c.授课教师姓名=d.授课教师姓名 and c.实际场地编号=d.实际场地编号 and c.实际场地名称=d.实际场地名称 for xml path('')),1,1,'') as 授课周次详情 " +
//					"from (select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+")) c ";
//				String sql33="select f.行政班名称,f.时间序列,f.课程名称,f.授课教师姓名,f.实际场地编号,f.实际场地名称,f.授课周次详情 from (" +
//							 "select e.行政班名称,e.时间序列,e.课程名称,e.授课教师姓名,e.实际场地编号,e.实际场地名称,e.授课周次详情,substring(e.授课周次详情,0,charindex('#',e.授课周次详情)) as px from ("+sql3+") e ) f " +
//							 "order by f.实际场地编号,f.时间序列,convert(int,f.px),f.授课教师姓名 ";
			
			sql3="select 行政班名称,时间序列,课程名称,授课教师姓名,场地编号 as 实际场地编号,场地名称 as 实际场地名称,授课周次 from V_排课管理_课程表周详情表  where 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and substring(时间序列,1,2)='"+week+"' and ("+cdbh+") order by 实际场地编号,时间序列,授课教师姓名,行政班名称,cast(授课周次 as int) ";
			vector3=db.GetContextVector(sql3);
			
			if(vector3!=null&&vector3.size()>0){
				String xzbmc=vector3.get(0).toString();
				String sjxl=vector3.get(1).toString();
				String kcmc=vector3.get(2).toString();
				String jsxm=vector3.get(3).toString();
				String cdid=vector3.get(4).toString();
				String cdmc=vector3.get(5).toString();
				String skzc=vector3.get(6).toString();
				for(int r=7;vector3.size()>7;){	
					if(vector3.get(r-7).toString().equals(vector3.get(r).toString())&&
					   vector3.get(r-7+1).toString().equals(vector3.get(r+1).toString())&&
					   vector3.get(r-7+2).toString().equals(vector3.get(r+2).toString())&&
					   vector3.get(r-7+3).toString().equals(vector3.get(r+3).toString())&&
					   vector3.get(r-7+4).toString().equals(vector3.get(r+4).toString())){
							
					    skzc+="#"+vector3.get(r+6).toString();
					    
					    for(int k=0;k<7;k++){
					    	vector3.remove(r);
					    }
					}else{
						vec3.add(xzbmc);//[行政班名称] 
						vec3.add(sjxl);//[时间序列] 
						vec3.add(kcmc);//[课程名称] 
						vec3.add(jsxm);//[授课教师姓名] 
						vec3.add(cdid);//[实际场地编号] 
						vec3.add(cdmc);//[实际场地名称] 
						vec3.add(skzc);//[授课周次详情] 	
						
						xzbmc=vector3.get(r).toString();
						sjxl=vector3.get(r+1).toString();
						kcmc=vector3.get(r+2).toString();
						jsxm=vector3.get(r+3).toString();
						cdid=vector3.get(r+4).toString();
						cdmc=vector3.get(r+5).toString();
						skzc=vector3.get(r+6).toString();
						
						for(int k=0;k<7;k++){
					    	vector3.remove(r-7);
					    }
					}
				}
				vec3.add(xzbmc);//[行政班名称] 
				vec3.add(sjxl);//[时间序列] 
				vec3.add(kcmc);//[课程名称] 
				vec3.add(jsxm);//[授课教师姓名] 
				vec3.add(cdid);//[实际场地编号] 
				vec3.add(cdmc);//[实际场地名称] 
				vec3.add(skzc);//[授课周次详情] 	
				
				for(int k=0;k<7;k++){
			    	vector3.remove(0);
			    }
				
				for(int i=0;i<vec3.size();i=i+7){
					skzcxq=merge(vec3.get(i+6).toString());
					if(vec3.get(i+4).toString().indexOf("+")>-1){
						String[] cdbhs=vec3.get(i+4).toString().split("\\+");
						String[] cdmcs=vec3.get(i+5).toString().split("\\+");
						for(int j=0;j<cdbhs.length;j++){
							vec.add(vec3.get(i).toString());//[行政班名称] 
							vec.add(vec3.get(i+1).toString());//[时间序列] 
							vec.add(vec3.get(i+2).toString());//[课程名称] 
							vec.add(vec3.get(i+3).toString());//[授课教师姓名] 
							vec.add(cdbhs[j]);//[实际场地编号] 
							vec.add(cdmcs[j]);//[实际场地名称] 
							vec.add(skzcxq);//[授课周次详情] 	
						}
					}else{
						vec.add(vec3.get(i).toString());//[行政班名称] 
						vec.add(vec3.get(i+1).toString());//[时间序列] 
						vec.add(vec3.get(i+2).toString());//[课程名称] 
						vec.add(vec3.get(i+3).toString());//[授课教师姓名] 
						vec.add(vec3.get(i+4).toString());//[实际场地编号] 
						vec.add(vec3.get(i+5).toString());//[实际场地名称] 
						vec.add(skzcxq);//[授课周次详情] 		
					}
				}
			}
			
			//选修课
			if(!cdbh2.equals("")){
				cdbh2=cdbh2.substring(0, cdbh2.length()-3);
				sql6="select a.选修班名称 as 行政班名称,a.上课时间 as 时间序列,b.课程名称,a.授课教师姓名,a.场地要求,a.场地名称,a.授课周次 from V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b where a.授课计划主表编号=b.授课计划主表编号 and 学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' and ("+cdbh2+") order by a.场地要求,a.上课时间 ";
				vec6=db.GetContextVector(sql6);

				if(vec6!=null&&vec6.size()>0){ 
					for(int i=0;i<vec6.size();i=i+7){
						String[] sjxls=vec6.get(i+1).toString().split(",");
						for(int j=0;j<sjxls.length;j++){
							//System.out.println(sjxls[j].substring(0, 2)+"|"+week);
							if(sjxls[j].substring(0, 2).equals(week)){//授课周次相同
								vec.add(vec6.get(i).toString());//[行政班名称] 
								vec.add(sjxls[j]);//[时间序列] 
								vec.add(vec6.get(i+2).toString());//[课程名称] 
								vec.add(vec6.get(i+3).toString());//[授课教师姓名] 
								vec.add(vec6.get(i+4).toString());//[实际场地编号] 
								vec.add(vec6.get(i+5).toString());//[实际场地名称] 
								vec.add(vec6.get(i+6).toString());//[授课周次详情] 
							}
						}
					}
				}
			}
		}

        //场地，时间相同的合并
		for(int i=0;vec.size()>1;){
			String icxzbmc="";
			String ickcmc="";
			String icskjsxm="";
			String icskzcxq="";
			icxzbmc=vec.get(i).toString();
			ickcmc=vec.get(i+2).toString();
			icskjsxm=vec.get(i+3).toString();
			icskzcxq=vec.get(i+6).toString();
			for(int j=7;j<vec.size();j=j+7){
				//System.out.println(vec.size()+"|"+j);
				if(vec.get(i+1).toString().equals(vec.get(j+1).toString())&&vec.get(i+4).toString().equals(vec.get(j+4).toString())){
					icxzbmc+="｜"+vec.get(j).toString();
					ickcmc+="｜"+vec.get(j+2).toString();
					icskjsxm+="｜"+vec.get(j+3).toString();
					icskzcxq+="｜"+vec.get(j+6).toString();
					for(int k=0;k<7;k++){
						vec.remove(j);
					}
				}
			}
			//System.out.println(vec.toString()+"|"+i);
			
			vec2.add(icxzbmc);//[行政班名称] 
			vec2.add(vec.get(i+1).toString());//[时间序列] 
			vec2.add(ickcmc);//[课程名称] 
			vec2.add(icskjsxm);//[授课教师姓名] 
			vec2.add(vec.get(i+4).toString());//[实际场地编号] 
			vec2.add(vec.get(i+5).toString());//[实际场地名称] 
			vec2.add(icskzcxq);//[授课周次详情] 
			
			for(int k=0;k<7;k++){
				vec.remove(i);
			}
		}
		
		for(int colNum=1; colNum<vector.size()+2; colNum++){
			for(int rowNum=1; rowNum<vector2.size()+3; rowNum++){
				//生成标题
				if(colNum==1 && rowNum==1){
					wb.openSheet("Sheet1").openTable(getcolName(0)+"1:"+getcolName(vector.size())+"1").merge();
					cell = wb.openSheet("Sheet1").openCell("A1");
					cellContent = schoolName + "   " + titleinfo + " 教室课表";
						
					//maxWidth = 4*cellContent.length()/(vector.size()+1);
					cell.setValue(cellContent);
				}else{
					if(colNum==1 && rowNum==2){
						cell = wb.openSheet("Sheet1").openCell(getcolName(colNum-1)+rowNum);//当前单元格
						cell.setValue("");
					}
					//判断是否为课程时间列
					else if(colNum==1 && rowNum>2){
						cell = wb.openSheet("Sheet1").openCell(getcolName(colNum-1)+rowNum);//当前单元格
						
						//判断是否为中午
						if(rowNum-2<=sw || rowNum-2>sw+zw){
							if(rowNum-2<=sw){
								cell.setValue("第"+orderNameArray[rowNum-3] + "节\n" + timeArray[rowNum-3]);
							}else{
								cell.setValue("第"+orderNameArray[rowNum-zw-3] + "节\n" + timeArray[rowNum-3]);
							}
						}else{
							cell.setValue("中"+orderNameArray[rowNum-sw-3] + "\n" + timeArray[rowNum-3]);
						}
					}
					//判断是否为教室行
					else if(colNum>1 && rowNum==2){
						//System.out.println(colNum+":"+vector1.get(colNum-2).toString()+":"+getcolName(colNum-1));
						cell = wb.openSheet("Sheet1").openCell(getcolName(colNum-1)+rowNum);//当前单元格			
						cell.setValue(vector1.get(colNum-2).toString());	
					}
					//课表内容
					else if(colNum>1 && rowNum>2){
						
					}
				}
			}
		}
		
//		for(int i=0;i<vec2.size();i=i+7){
//			System.out.println(vec2.get(i).toString()+""+vec2.get(i+1).toString()+""+vec2.get(i+2).toString()+""+vec2.get(i+3).toString()+""+vec2.get(i+4).toString()+""+vec2.get(i+5).toString()+""+vec2.get(i+6).toString());
//		}
		
		//合并单元格
		int col=0;//第几列
		int tag=-1;
		int stnum=-1;
		for(int j=0;j<vector.size();j++){
			col=j+2;
			String[] sjxl=sjxl=new String[vector2.size()];
			for(int i=0;i<vector2.size();i++){
				sjxl[i]="";
			}
			for(int i=0;i<vec2.size();i=i+7){
				if(vector.get(j).toString().equals(vec2.get(i+4).toString())){//场地编号相等
					sjxl[Integer.parseInt(vec2.get(i+1).toString().substring(2, 4))-1] = vec2.get(i+0).toString()+"\n"+vec2.get(i+2).toString()+"\n"+vec2.get(i+3).toString()+"\n"+vec2.get(i+6).toString();
				}
			}

			//判断是否同一列并且授课计划明细编号和场地编号是否完全相同
			for(int n=0;n<vector2.size();n++){
				if(sjxl[n].equals("")){
					mergeNum=0;
				}else{
					if(n<(vector2.size()-1)){
						if(!sjxl[n].equals("")&&!sjxl[n+1].equals("")&&sjxl[n].equals(sjxl[n+1])){
							mergeNum++;	
							tag=0;
						}else{
							tag=1;
						}
					}else{
						if(sjxl[n].equals("")){
							tag=0;
							mergeNum=0;
						}else{
							tag=1;
						}	
					}
					//合并单元格
					if(tag==1&&mergeNum>0){
						//System.out.println(n+","+(n+3-mergeNum)+","+(n+3));
						wb.openSheet("Sheet1").openTable(getcolName(col-1)+(n+3-mergeNum)+":"+getcolName(col-1)+(n+3)).merge();
						cell = wb.openSheet("Sheet1").openCell(getcolName(col-1)+(n+3-mergeNum));//当前单元格
						cell.setValue(sjxl[n]);
						mergeNum=0;
						tag=0;
					}else if(tag==1&&mergeNum==0){
						cell = wb.openSheet("Sheet1").openCell(getcolName(col-1)+(n+3));//当前单元格
						cell.setValue(sjxl[n]);
					}		
				}
			}
			
		}
		
//		if(vec2!=null&&vec2.size()>0){
//			for(int i=0;i<vec2.size();i=i+7){
//				tempClassName = MyTools.StrFiltr(vec2.get(i+0));//行政班名称
//				timeOrder = MyTools.StrFiltr(vec2.get(i+1));//时间序列
//				tempCourseName = MyTools.StrFiltr(vec2.get(i+2));//课程名称
//				tempTeaName = MyTools.StrFiltr(vec2.get(i+3));//授课教师姓名
//				tempSiteCode = MyTools.StrFiltr(vec2.get(i+4));//实际场地编号
//				tempSiteName = MyTools.StrFiltr(vec2.get(i+5));//实际场地名称
//				tempSkzc = MyTools.StrFiltr(vec2.get(i+6));//授课周次详情 
//				
//				int thir=Integer.parseInt(timeOrder.substring(2, 3));
//				int four=Integer.parseInt(timeOrder.substring(3, 4));
//				int row=thir*10+four+2;//第几行
//				int col=0;//第几列
//				int rowindex=0;
//				for(int j=0;j<vector.size();j++){
//					if(tempSiteCode.indexOf(vector.get(j).toString())>-1){
//						col=j+2;
//						cellContent = MyTools.StrFiltr(tempClassName+"\n"+tempCourseName+"\n"+tempTeaName+"\n"+tempSkzc);
//						
//						mergeNum = 0;
//						flag = true;
//						//判断单元格是否可以合并
//						while(flag){
//							if((i+7+7*mergeNum)<vec2.size()){
//								//System.out.println(vector.get(j).toString()+"|"+vec2.get(i+7+7*mergeNum+4).toString());
//								//场地编号相等
//								
//									int thir2=Integer.parseInt(vec2.get(i+7+7*mergeNum+1).toString().substring(2, 3));
//									int four2=Integer.parseInt(vec2.get(i+7+7*mergeNum+1).toString().substring(3, 4));
//									int row2=thir2*10+four2+2;//第几行
//									
//									//判断是否同一列并且授课计划明细编号和场地编号是否完全相同
//									if(tempClassName.equalsIgnoreCase(MyTools.StrFiltr(vec2.get(i+7+7*mergeNum+0))) 
//										&& tempCourseName.equalsIgnoreCase( MyTools.StrFiltr(vec2.get(i+7+7*mergeNum+2)))
//										&& tempTeaName.equalsIgnoreCase(MyTools.StrFiltr(vec2.get(i+7+7*mergeNum+3)))
//										&& tempSkzc.equalsIgnoreCase(MyTools.StrFiltr(vec2.get(i+7+7*mergeNum+6)))){
//										mergeNum++;
//									}else{
//										flag = false;
//												
//									}
//	
//								
//							}else{
//								flag = false;
//							}
//						}
//						//合并单元格
//						if(mergeNum > 0){
//							wb.openSheet("Sheet1").openTable(colName[col-1]+row+":"+colName[col-1]+(row+mergeNum)).merge();
//						}
//						
//						cell = wb.openSheet("Sheet1").openCell(colName[col-1]+row);//当前单元格
//						cell.setValue(cellContent);
//						i=i+mergeNum*7;
//					}
//				}		
//			}
//		}
		
		//设置单元格的水平对齐方式
		wb.openSheet("Sheet1").openTable(getcolName(0)+"1:"+getcolName(vector.size())+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		wb.openSheet("Sheet1").openTable(getcolName(0)+"2:"+getcolName(vector.size())+(zjs+2)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		if("classTable".equalsIgnoreCase(exportType)) 
			wb.openSheet("Sheet1").openCell(getcolName(0)+(zjs+3)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		
		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置课表边框线
		Border border = wb.openSheet("Sheet1").openTable(getcolName(0)+"2:"+getcolName(vector.size())+(zjs+2)).getBorder();
		//设置表格边框的宽度、颜色
		border.setWeight(XlBorderWeight.xlThin);
		border.setLineColor(Color.black);
		
		//设置标题字体大小
		cell = wb.openSheet("Sheet1").openCell("A1");
		cell.getFont().setBold(true);
		fontSize = 18;
		cell.getFont().setSize(fontSize);
		
		//设置课表标题行列字体大小
		fontSize = 11;
		wb.openSheet("Sheet1").openTable(getcolName(0)+"2:"+getcolName(vector.size())+"2").getFont().setSize(fontSize);
		wb.openSheet("Sheet1").openTable(getcolName(0)+"2:"+getcolName(0)+(zjs+1)).getFont().setSize(fontSize);
		//设置课表内容行列字体大小
		fontSize = 10;
		//设置表格列宽
		wb.openSheet("Sheet1").openTable(getcolName(0)+"1:"+getcolName(0)+"1").setColumnWidth(20);
		maxWidth=20;
		
		if(!cdbh.equals("")){
			wb.openSheet("Sheet1").openTable(getcolName(1)+"3:"+getcolName(vector.size())+(zjs+2)).getFont().setSize(fontSize);
			wb.openSheet("Sheet1").openTable(getcolName(1)+"1:"+getcolName(vector.size())+"1").setColumnWidth(maxWidth);
			//设置表格行高
			wb.openSheet("Sheet1").openTable(getcolName(0)+"1:"+getcolName(vector.size())+"2").setRowHeight(40);
			wb.openSheet("Sheet1").openTable(getcolName(0)+"3:"+getcolName(vector.size())+(zjs+2)).setRowHeight(maxHeight);
		}else{
			wb.openSheet("Sheet1").openTable(getcolName(1)+"3:"+getcolName(11)+(10+2)).getFont().setSize(fontSize);
			wb.openSheet("Sheet1").openTable(getcolName(1)+"1:"+getcolName(11)+"1").setColumnWidth(maxWidth);
			//设置表格行高
			wb.openSheet("Sheet1").openTable(getcolName(0)+"1:"+getcolName(11)+"2").setRowHeight(40);
			wb.openSheet("Sheet1").openTable(getcolName(0)+"3:"+getcolName(11)+(10+2)).setRowHeight(maxHeight);
		}
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "template.xls";

		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("下载", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "   " + titleinfo + " 教室课表");
		poCtrl1.setFileTitle(schoolName + "   " + titleinfo + " 教室课表");//设置另存为窗口默认文件名
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
	}
	
	//合并周次
	public static String merge(String skzc) throws SQLException {
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
	
	/**
	 * 教师工作量统计
	 * @date:2015-12-09
	 * @author:lupengfei
	 * @param xnxq+jxxz 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	
	/** 
	*字符串的日期格式的计算 
	*/  
	public static int daysBetween(String smdate,String bdate) throws ParseException{  
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(sdf.parse(smdate));    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(sdf.parse(bdate));    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24);  
	            
	       return Integer.parseInt(String.valueOf(between_days));     
	} 
	
	public static void exportWorkLoad(HttpServletRequest request, String exportType, String XNXQ, String TEAID, String TEANAME) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		System.out.println("TEANAME--"+TEANAME);
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 40;
		float fontSize = 0;
		
		Workbook wb = new Workbook();
		Cell cell;
		String cellContent = ""; //当前单元格的内容
		Vector tempVec = new Vector();
		Vector hbSetVec = new Vector();
		Vector timeVec = null;
		
		int mergeNum = 0;//单元格合并数
		boolean flag = true;//用于判断是否可合并
		
		String tempYeaTERM = "";//学年学期
		String tempTeaCode = "";//教师工号
		String tempTeaName = "";//教师姓名
		String tempTeaTYPE = "";//教师类别
		//String tempCouNUMB = "";//课程代码
		String tempCouNAME = "";//课程名称
		String tempClsNAME = "";//班级名称
		//String tempMajNAME = "";//专业名称
		String tempWekHOUR = "";//周课时
		String tempClsNUMB = "";//班级人数
		String tempClsCOEF = "";//班级系数
		String tempWekSKZC = "";//授课周次
		String tempCouHOUR = "";//课程课时
		String tempWekNUMB = "";//周数
		//String tempAvgHOUR = "";//周平均课时	
		String skjhmxbhArray[] = new String[0];
		
		String sql = "";
		String sql0 = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String sql7 = "";
		Vector vec0 = null;
		Vector vec1 = null;
		Vector vec3 = null;
		Vector vec4 = null;
		Vector vec7 = null;
		Vector vectk = null;
		Vector vec=new Vector();
		Vector vecr=new Vector();
		Vector vecs=new Vector();
		Vector vect=new Vector();
		Vector vecu=new Vector();
		Vector vecv=new Vector();
		String skjhbh=null;//授课计划明细编号
		String xnxqbm=null;//学年学期编码
		String skjsbh=null;//授课教师编号
		String skjsxm=null;//授课教师姓名
		String zyname=null;//专业名称
		String kechid=null;//课程代码
		String kechna=null;//课程名称
		String clasid=null;//行政班代码
		String clasna=null;//行政班名称
		String timexl=null;//时间序列
		String sjcdmc=null;//实际场地名称
		String skzcxq=null;//授课周次详情
		
		String sqlpkgl="";
		String sqlpkgl1="";
		String sqlpkgl4="";
//		int a=0;
//		int b=0;
//		int c=0;
		int d=0;
		int f=0;
		String weeknum="";
		String heban1="";//保存第一项合班授课计划编号
		String heban2="";//保存除了第一项的合班授课计划编号
		String[] skjhmxbhhb1=null;
		String[] classnamehb=null;
		String[] classnumbhb=null;
		
		//获取本学期周数量
		String sqlweek = " select 实际上课周数 FROM dbo.V_规则管理_学年学期表  where 学年学期编码='"+MyTools.fixSql(XNXQ)+"'" ;
		Vector vecwek = db.GetContextVector(sqlweek);
		if(vecwek!=null&vecwek.size()>0){
			weeknum=vecwek.get(0).toString();			
		}
		
		//查询合班信息
		String sqlhb="select 授课计划明细编号 from dbo.V_规则管理_合班表 where SUBSTRING(授课计划明细编号,0,CHARINDEX('+',授课计划明细编号)) in ( " +
				"select 授课计划明细编号 from dbo.V_规则管理_授课计划明细表 where [授课计划主表编号] in ( " +
				"select [授课计划主表编号] from dbo.V_规则管理_授课计划主表 where [学年学期编码]='"+XNXQ+"' ) ) ";
		Vector vechb = null;
		Vector vechb3 = null;
		vechb=db.GetContextVector(sqlhb);
		if(vechb!=null&&vechb.size()>0){
			skjhmxbhhb1=new String[vechb.size()];//保存授课计划明细编号
			classnamehb=new String[vechb.size()];//保存对应的班级名称
			classnumbhb=new String[vechb.size()];//保存合班后的班级总人数
			heban1="";//保存第一项合班授课计划编号
			heban2="";//保存除了第一项的合班授课计划编号
			for(int m=0;m<vechb.size();m++){
				String skjhmxbh=vechb.get(m).toString();
				String[] skjhbhid=skjhmxbh.split("\\+");
				String sqlhb2= "";
					
				for(int n=0;n<skjhbhid.length;n++){
					if(n==0){
						sqlhb2+="'"+skjhbhid[n]+"'";
						heban1+=skjhbhid[n]+",";
						skjhmxbhhb1[m]=skjhbhid[n];
					}else{
						sqlhb2+=",'"+skjhbhid[n]+"'";
						heban2+=skjhbhid[n]+",";
					}
				}
				heban1=heban1.substring(0, heban1.length()-1);
				heban2=heban2.substring(0, heban2.length()-1);
						
				String sqlhb3="select b.学年学期编码,a.课程名称,c.行政班名称,c.总人数 from dbo.V_规则管理_授课计划明细表 a " +
					"inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
					"inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 " +
					"where a.授课计划明细编号 in ("+sqlhb2+")";
				vechb3=db.GetContextVector(sqlhb3);
				if(vechb3!=null&&vechb3.size()>0){
						String classname="";
						int classnumb=0;
					for(int k=0;k<vechb3.size();k=k+4){
						classname+=vechb3.get(k+2).toString()+"+";	
						classnumb=classnumb+Integer.parseInt(vechb3.get(k+3).toString());
					}				
					classname=classname.substring(0, classname.length()-1);	
							
					classnamehb[m]=classname;
					classnumbhb[m]=classnumb+"";
				}
			}
		}//vechb
				
		//计算本学期周一到周五节假日有几次
		String xnxqid="";//学年学期编码
		String jjrq="";//节假日期
		String xqkssj="";//学期开始时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		int week=-1;
		String week1="";
		String week2="";
		String week3="";
		String week4="";
		String week5="";
		
		String sqltm="select 学年学期编码,节假日期,学期开始时间,实际上课周数 from dbo.V_规则管理_学年学期表 where 学年学期编码='"+XNXQ+"' ";
		
		Vector vecxq=db.GetContextVector(sqltm);
		if(vecxq!=null&&vecxq.size()>0){
			//this.setMSG(vecxq.get(3).toString());	
			for(int i=0;i<vecxq.size();i=i+4){
				xnxqid=vecxq.get(i).toString();
				jjrq=vecxq.get(i+1).toString()+",";	
				xqkssj=vecxq.get(i+2).toString();	
				try {
					cal.setTime(format.parse(xqkssj));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//学期开始是一年的第几周
				int beginweek=cal.get(Calendar.WEEK_OF_YEAR);
				
				//学期开始日期是星期几
				int begindate = cal.get(Calendar.DAY_OF_WEEK)-1;
				System.out.println(begindate);
				//将学期开始日期所在星期的前几天加入到节假日期里
				for(int t=1;t<begindate;t++){
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
					 String str = xqkssj; 
					 Date dt=sdf.parse(str,new ParsePosition(0)); 
					 Calendar rightNow = Calendar.getInstance(); 
					 rightNow.setTime(dt); 
					 rightNow.add(Calendar.DATE,-(begindate-t));//你要加减的日期   
					 Date dt1=rightNow.getTime(); 
					 String reStr=sdf.format(dt1); 
					 System.out.println("reStr:--"+reStr);
					 if(!reStr.endsWith("")){
						 jjrq+=reStr+",";
					 }
					
				}
				jjrq=jjrq.substring(0, jjrq.length()-1);
				
				String[] freedate=jjrq.split(",");
				if(jjrq.equals("")){
					
				}else{
					for(int j=0;j<freedate.length;j++){
					    try {
							cal.setTime(format.parse(freedate[j]));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    week = cal.get(Calendar.DAY_OF_WEEK)-1;
	
						int weeks=0;//第几周
						try {
							weeks=(daysBetween(xqkssj,freedate[j])+begindate)/7+1;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					    if(week==6||week==0){
					    	
					    }else{
					    	if(week==1){
					    		week1+=weeks+",";
					    	}else if(week==2){
					    		week2+=weeks+",";
					    	}else if(week==3){
					    		week3+=weeks+",";
					    	}else if(week==4){
					    		week4+=weeks+",";
					    	}else if(week==5){
					    		week5+=weeks+",";
					    	}
					    }				   
					}
				}
				
				//哪几周星期几是假日
				if(!week1.equals("")){
					week1=week1.substring(0, week1.length()-1);
				}
				if(!week2.equals("")){
					week2=week2.substring(0, week2.length()-1);
				}
				if(!week3.equals("")){
					week3=week3.substring(0, week3.length()-1);
				}
				if(!week4.equals("")){
					week4=week4.substring(0, week4.length()-1);
				}
				if(!week5.equals("")){
					week5=week5.substring(0, week5.length()-1);
				}
				
			}
			//System.out.println("1--:"+week1+"|2--:"+week2+"|3--:"+week3+"|4--:"+week4+"|5--:"+week5);
		}
			
		//20160826，改用课程表周详情表查询
		sql0="SELECT a.授课计划明细编号,a.学年学期编码,a.授课教师编号,a.授课教师姓名,a.专业名称,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.时间序列,b.总人数,a.授课周次  " +
				" FROM dbo.V_排课管理_课程表周详情表 a,dbo.V_学校班级数据子类 b " +
				" where a.行政班代码=b.行政班代码 and a.授课计划明细编号 != '' and a.状态='1' and a.学年学期编码='"+XNXQ+"' order by a.授课计划明细编号,a.授课教师编号,a.行政班名称,a.时间序列,convert(int,a.授课周次)" ;

		vec0=db.GetContextVector(sql0);
		if(vec0!=null&&vec0.size()>0){
			String skzc="";
			for(int i=0;i<vec0.size()-12;){
				skzc=vec0.get(i+11).toString();
				
					while(i<(vec0.size()-12)&&vec0.get(i).toString().equals(vec0.get(i+12).toString())&&vec0.get(i+9).toString().equals(vec0.get(i+9+12).toString())){//授课计划编号相等+时间序列相等					
						skzc+="#"+vec0.get(i+12+11).toString();
						//System.out.println(i+":--"+skzc);
						i=i+12;
					}
					skzcxq=merge(skzc);
					String[] skjsbh5=vec0.get(i+2).toString().split("\\+");
					String[] skjsxm5=vec0.get(i+3).toString().split("\\+");
					for(int m=0;m<skjsbh5.length;m++){
						vec.add(vec0.get(i).toString());//授课计划明细编号
						vec.add(vec0.get(i+1).toString());//学年学期编码
						vec.add(skjsbh5[m]);//授课教师编号
						vec.add(skjsxm5[m]);//授课教师姓名
						vec.add(vec0.get(i+4).toString());//专业名称
						vec.add(vec0.get(i+5).toString());//课程代码
						vec.add(vec0.get(i+6).toString());//课程名称
						vec.add(vec0.get(i+7).toString());//行政班代码
						vec.add(vec0.get(i+8).toString());//行政班名称
						vec.add(vec0.get(i+9).toString());//时间序列
						vec.add(vec0.get(i+10).toString());//总人数
						vec.add(skzcxq);//授课周次详情
					}
					i=i+12;
			}					
		}
		
		//选修课明细表
		sql1=" SELECT  a.授课计划明细编号,b.学年学期编码,a.授课教师编号,a.授课教师姓名,'选修课' as 专业名称,b.课程代码,b.课程名称,'' as 行政班代码,a.选修班名称 as 行政班名称,c.时间序列,a.报名人数,a.授课周次 as 授课周次详情 " +
			 " FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b,V_排课管理_选修课课程表信息表 c " +
			 " where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号=c.授课计划明细编号  and b.学年学期编码='"+XNXQ+"' ";
		vec1=db.GetContextVector(sql1);
		if(vec1!=null&&vec1.size()>0){
			for(int i=0;i<vec1.size();i=i+12){
				skjhbh=vec1.get(i).toString();//授课计划明细编号
				xnxqbm=vec1.get(i+1).toString();//学年学期编码
				skjsbh=vec1.get(i+2).toString();//授课教师编号
				skjsxm=vec1.get(i+3).toString();//授课教师姓名
				zyname=vec1.get(i+4).toString();//专业名称
				kechid=vec1.get(i+5).toString();//课程代码
				kechna=vec1.get(i+6).toString();//课程名称
				clasid=vec1.get(i+7).toString();//行政班代码
				clasna=vec1.get(i+8).toString();//行政班名称
				timexl=vec1.get(i+9).toString();//时间序列
				sjcdmc=vec1.get(i+10).toString();//报名人数
				skzcxq=vec1.get(i+11).toString();//授课周次详情
				
				//System.out.println(a+":"+skjhbh+","+xnxqbm+","+skjsbh+","+skjsxm+","+zyname+","+kechid+","+kechna+","+clasid+","+clasna+","+timexl+","+sjcdmc+","+skzcxq); a++;
				String[] skjhbh2=skjhbh.split("｜");
				String[] skjsbh2=skjsbh.split("｜");
				String[] skjsxm2=skjsxm.split("｜");
				String[] kechid2=kechid.split("｜");
				String[] kechna2=kechna.split("｜");
				String[] skzcxq2=skzcxq.split("｜");
				for(int j=0;j<skjhbh2.length;j++){
					//System.out.println(b+":"+skjhbh2[j]+","+skjsbh2[j]+","+skjsxm2[j]+","+kechid2[j]+","+kechna2[j]+","+sjcdmc2[j]+","+skzcxq2[j]); b++;
					String[] skjsbh3=skjsbh2[j].split("\\&");
					String[] skjsxm3=skjsxm2[j].split("\\&");
					String[] skzcxq3=skzcxq2[j].split("\\&");
					for(int k=0;k<skjsbh3.length;k++){
						//System.out.println(c+":"+skjsbh3[k]+","+skjsxm3[k]+","+sjcdmc3[k]+","+skzcxq3[k]); c++;
						String[] skjsbh4=skjsbh3[k].split("\\+");
						String[] skjsxm4=skjsxm3[k].split("\\+");
						for(int m=0;m<skjsbh4.length;m++){
							//System.out.println(d+":"+skjsbh4[m]+","+skjsxm4[m]); 
							//System.out.println(d+":"+skjhbh2[j]+","+xnxqbm+","+skjsbh4[m]+","+skjsxm4[m]+","+zyname+","+kechid2[j]+","+kechna2[j]+","+clasid+","+clasna+","+timexl+","+sjcdmc3[k]+","+skzcxq3[k]); 
							//查询类型不是全部
							
								if(skjsbh4[m].equals("")&&skjsxm4[m].equals("")){
									
								}else{
									vec.add(skjhbh2[j]);//授课计划明细编号
									vec.add(xnxqbm);//学年学期编码
									vec.add(skjsbh4[m]);//授课教师编号
									vec.add(skjsxm4[m]);//授课教师姓名
									vec.add(zyname);//专业名称
									vec.add(kechid2[j]);//课程代码
									vec.add(kechna2[j]);//课程名称
									vec.add(clasid);//行政班代码
									vec.add(clasna);//行政班名称
									vec.add(timexl);//时间序列
									vec.add(sjcdmc);//报名人数
									vec.add(skzcxq3[k]);//授课周次详情
								}
									
						}
					}
				}
			}	
		}
			
		//查询条件	
		String usercodes="";
		int taguc1=0;
		int taguc2=0;
		int taguc3=0;

		//System.out.println("usercodes:----------"+usercodes);
		
//		for(int i=0;i<vec.size();i=i+12){
//			System.out.println("授课计划明细编号:"+vec.get(i).toString()+","+"学年学期编码:"+vec.get(i+1).toString()+","+"授课教师编号:"+vec.get(i+2).toString()+","+"授课教师姓名:"+vec.get(i+3).toString()+","+"专业名称:"+vec.get(i+4).toString()+","+"课程代码:"+vec.get(i+5).toString()+","+"课程名称:"+vec.get(i+6).toString()+","+"行政班代码:"+vec.get(i+7).toString()+","+"行政班名称:"+vec.get(i+8).toString()+","+"时间序列:"+vec.get(i+9).toString()+","+"总人数:"+vec.get(i+10).toString()+","+"授课周次详情:"+vec.get(i+11).toString()+",");
//		}
		
		for(int v=0;v<vec.size();v=v+12){
			if(!TEAID.equalsIgnoreCase("")){
				if(vec.get(v+2).toString().indexOf(TEAID)>-1){
					taguc2=1;
				}else{
					taguc2=0;
				}
			}else{
				taguc2=1;
			}
			if(!TEANAME.equalsIgnoreCase("")){
				if(vec.get(v+3).toString().indexOf(TEANAME)>-1){
					taguc3=1;
				}else{
					taguc3=0;
				}
			}else{
				taguc3=1;
			}
			if(taguc2==1&&taguc3==1){
				vecr.add(vec.get(v).toString());//授课计划明细编号 1
				vecr.add(vec.get(v+1).toString());//学年学期编码 2
				vecr.add(vec.get(v+2).toString());//授课教师编号 3
				vecr.add(vec.get(v+3).toString());//授课教师姓名 4
				
//				String tealv="";
//				for(int i=0;i<veclv.size();i++){
//					if(tealvinfo[i].indexOf("@"+vec.get(v+2).toString()+"@")>-1){
//						tealv=tealvname[i];
//					}							
//				}
				vecr.add("");//层级名称 5
				
				vecr.add(vec.get(v+4).toString());//专业名称 6
				vecr.add(vec.get(v+5).toString());//课程代码 7
				vecr.add(vec.get(v+6).toString());//课程名称 8
				vecr.add(vec.get(v+7).toString());//行政班代码 9
				vecr.add(vec.get(v+8).toString());//行政班名称 10
				vecr.add(vec.get(v+9).toString());//时间序列 11
				vecr.add(vec.get(v+10).toString());//总人数 12
				
				int rs=Integer.parseInt(vec.get(v+10).toString());
				double xs=0;
				if(rs<40){
					xs=1.0;
				}else if(40<=rs&&rs<50){
					xs=1.1;
				}else if(50<=rs&&rs<60){
					xs=1.2;
				}else if(60<=rs){
					xs=1.3;
				}
				vecr.add(xs);//系数 13
				
				vecr.add(vec.get(v+11).toString());//授课周次详情 14
				
				String jrzc="";
				if(vec.get(v+9).toString().substring(0, 2).equals("01")){
					jrzc=week1;
				}else if(vec.get(v+9).toString().substring(0, 2).equals("02")){
					jrzc=week2;
				}else if(vec.get(v+9).toString().substring(0, 2).equals("03")){
					jrzc=week3;
				}else if(vec.get(v+9).toString().substring(0, 2).equals("04")){
					jrzc=week4;
				}else if(vec.get(v+9).toString().substring(0, 2).equals("05")){
					jrzc=week5;
				}
				vecr.add(jrzc);//假日周次 15
				
				int skzs=0;
				if(vec.get(v+11).toString().equals("odd")){
					skzs=(Integer.parseInt(vecxq.get(3).toString())+1)/2;
				}else if(vec.get(v+11).toString().equals("even")){
					skzs=Integer.parseInt(vecxq.get(3).toString())/2;
				}else if(vec.get(v+11).toString().indexOf("-")>-1){
					skzs=Integer.parseInt(vec.get(v+11).toString().split("-")[1])-Integer.parseInt(vec.get(v+11).toString().split("-")[0])+1;
				}else if(vec.get(v+11).toString().indexOf("#")>-1){
					skzs=vec.get(v+11).toString().split("#").length;
				}else{
					skzs=1;
				}
				vecr.add(skzs+"");//授课周数 16
			}
		}
			
//			for(int i=0;i<vecr.size();i=i+16){
//				System.out.println("r授课计划明细编号:"+vecr.get(i).toString()+","+"学年学期编码:"+vecr.get(i+1).toString()+","+"授课教师编号:"+vecr.get(i+2).toString()+","+"授课教师姓名:"+vecr.get(i+3).toString()+","+"层级名称:"+vecr.get(i+4).toString()+","+
//						"专业名称:"+vecr.get(i+5).toString()+","+"课程代码:"+vecr.get(i+6).toString()+","+"课程名称:"+vecr.get(i+7).toString()+","+"行政班代码:"+vecr.get(i+8).toString()+","+"行政班名称:"+vecr.get(i+9).toString()+","+"时间序列:"+vecr.get(i+10).toString()+","+"总人数:"+vecr.get(i+11).toString()+","+
//						"系数:"+vecr.get(i+12).toString()+","+"授课周次详情:"+vecr.get(i+13).toString()+","+"假日周次:"+vecr.get(i+14).toString()+","+"授课周数:"+vecr.get(i+15).toString()+"," );
//			}
		
		//合班
		for(int v=0;v<vecr.size();v=v+16){		
			if(heban1.indexOf(vecr.get(v).toString())>-1){//属于第一项合班，班级名称改为合班班级
				for(int m=0;m<skjhmxbhhb1.length;m++){
					if(vecr.get(v).toString().equals(skjhmxbhhb1[m])){
						vecs.add(vecr.get(v).toString());//授课计划明细编号 1
						vecs.add(vecr.get(v+1).toString());//学年学期编码 2
						vecs.add(vecr.get(v+2).toString());//授课教师编号 3
						vecs.add(vecr.get(v+3).toString());//授课教师姓名 4
						vecs.add(vecr.get(v+4).toString());//层级名称 5
						vecs.add(vecr.get(v+5).toString());//专业名称 6
						vecs.add(vecr.get(v+6).toString());//课程代码 7
						vecs.add(vecr.get(v+7).toString());//课程名称 8
						vecs.add(vecr.get(v+8).toString());//行政班代码 9
						if(vechb!=null&&vechb.size()>0){
							vecs.add(classnamehb[m]);//行政班名称 10
						}else{
							vecs.add(vecr.get(v+9).toString());//行政班名称 10
						}
						vecs.add(vecr.get(v+10).toString());//时间序列 11
						if(vechb!=null&&vechb.size()>0){
							vecs.add(classnumbhb[m]);//总人数 12
						}else{
							vecs.add(vecr.get(v+11).toString());//总人数 12
						}
						int rs=0;
						if(vechb!=null&&vechb.size()>0){
							rs=Integer.parseInt(classnumbhb[m]);
						}else {
							rs=Integer.parseInt(vecr.get(v+11).toString());
						}
						double xs=0;
						if(rs<40){
							xs=1.0;
						}else if(40<=rs&&rs<50){
							xs=1.1;
						}else if(50<=rs&&rs<60){
							xs=1.2;
						}else if(60<=rs){
							xs=1.3;
						}
						vecs.add(xs);//系数 13		
						vecs.add(vecr.get(v+13).toString());//授课周次详情 14
						vecs.add(vecr.get(v+14).toString());//假日周次 15
						vecs.add(vecr.get(v+15).toString());//授课周数 16
					}
				}	
			}else if(heban2.indexOf(vecr.get(v).toString())>-1){//不属于第一项合班,不添加这条信息
				
			}else{
				vecs.add(vecr.get(v).toString());//授课计划明细编号 1
				vecs.add(vecr.get(v+1).toString());//学年学期编码 2
				vecs.add(vecr.get(v+2).toString());//授课教师编号 3
				vecs.add(vecr.get(v+3).toString());//授课教师姓名 4
				vecs.add(vecr.get(v+4).toString());//层级名称 5
				vecs.add(vecr.get(v+5).toString());//专业名称 6
				vecs.add(vecr.get(v+6).toString());//课程代码 7
				vecs.add(vecr.get(v+7).toString());//课程名称 8
				vecs.add(vecr.get(v+8).toString());//行政班代码 9
				vecs.add(vecr.get(v+9).toString());//行政班名称 10
				vecs.add(vecr.get(v+10).toString());//时间序列 11
				vecs.add(vecr.get(v+11).toString());//总人数 12
				vecs.add(vecr.get(v+12).toString());//系数 13		
				vecs.add(vecr.get(v+13).toString());//授课周次详情 14
				vecs.add(vecr.get(v+14).toString());//假日周次 15
				vecs.add(vecr.get(v+15).toString());//授课周数 16
			}
		}
		
//			for(int i=0;i<vecs.size();i=i+16){
//				System.out.println("s授课计划明细编号:"+vecs.get(i).toString()+","+"学年学期编码:"+vecs.get(i+1).toString()+","+"授课教师编号:"+vecs.get(i+2).toString()+","+"授课教师姓名:"+vecs.get(i+3).toString()+","+"层级名称:"+vecs.get(i+4).toString()+","+
//					"专业名称:"+vecs.get(i+5).toString()+","+"课程代码:"+vecs.get(i+6).toString()+","+"课程名称:"+vecs.get(i+7).toString()+","+"行政班代码:"+vecs.get(i+8).toString()+","+"行政班名称:"+vecs.get(i+9).toString()+","+"时间序列:"+vecs.get(i+10).toString()+","+"总人数:"+vecs.get(i+11).toString()+","+
//					"系数:"+vecs.get(i+12).toString()+","+"授课周次详情:"+vecs.get(i+13).toString()+","+"假日周次:"+vecs.get(i+14).toString()+","+"授课周数:"+vecs.get(i+15).toString()+"," );
//			}
			
		//计算总课时
		int counum=1;//每周上课节数
		for(int i=0;vecs.size()>1;){
			for(int j=16;j<vecs.size();j=j+16){
				//授课计划编号，班级，课程,教师姓名相同
				if(vecs.get(i).toString().equals(vecs.get(j).toString())&&vecs.get(i+9).toString().equals(vecs.get(j+9).toString())&&vecs.get(i+6).toString().equals(vecs.get(j+6).toString())&&vecs.get(i+2).toString().equals(vecs.get(j+2).toString())){
					counum++;
					for(int k=0;k<16;k++){
						vecs.remove(j);
					}
					j=j-16;
				}
			}
			//System.out.println(vecs.toString()+"|"+i);
			
			vect.add(vecs.get(i).toString());//授课计划明细编号 1
			vect.add(vecs.get(i+1).toString());//学年学期编码 2
			vect.add(vecs.get(i+2).toString());//授课教师编号 3
			vect.add(vecs.get(i+3).toString());//授课教师姓名 4
			vect.add(vecs.get(i+4).toString());//层级名称 5
			vect.add(vecs.get(i+5).toString());//专业名称 6
			vect.add(vecs.get(i+6).toString());//课程代码 7
			vect.add(vecs.get(i+7).toString());//课程名称 8
			vect.add(vecs.get(i+8).toString());//行政班代码 9
			vect.add(vecs.get(i+9).toString());//行政班名称 10
			vect.add(counum);//每周节数（时间序列） 11
			vect.add(vecs.get(i+11).toString());//总人数 12
			vect.add(vecs.get(i+12).toString());//系数 13		
			vect.add(vecs.get(i+13).toString());//授课周次详情 14
			
			int number=0;
			if(!vecs.get(i+14).toString().equals("")){//假日周次不为空
				String jrzc=vecs.get(i+14).toString();
				String skzc=vecs.get(i+13).toString();
				String[] jrzcnum=jrzc.split(",");
				String weeks="";
				number=0;
				for(int j=0;j<jrzcnum.length;j++){
					weeks="";
					if(skzc.indexOf("-")>-1){
						String[] wek=skzc.split("-");
						for(int r=Integer.parseInt(wek[0]);r<=Integer.parseInt(wek[1]);r++){
							weeks+=r+"#";	
						}
						weeks=weeks.substring(0,weeks.length()-1);
					}else if(skzc.indexOf("\\#")>-1){
						weeks=skzc;
					}else{
						weeks=skzc;
					}
					String[] weeks2=weeks.split("\\#");
					for(int s=0;s<weeks2.length;s++){
						if(jrzcnum[j].equals(weeks2[s])){//存在
							number++;
						}
					}
				}
			}else{
				number=0;
			}
			vect.add(number);//假日周次 15
			
			vect.add(vecs.get(i+15).toString());//授课周数 16
			counum=1;
			for(int k=0;k<16;k++){
				vecs.remove(i);
			}
		}

		//按教师排序
		for(int i=0;vect.size()>1;){
			vecv.add(vect.get(i).toString());//授课计划明细编号 1
			vecv.add(vect.get(i+1).toString());//学年学期编码 2
			vecv.add(vect.get(i+2).toString());//授课教师编号 3
			vecv.add(vect.get(i+3).toString());//授课教师姓名 4
			vecv.add(vect.get(i+4).toString());//层级名称 5
			vecv.add(vect.get(i+5).toString());//专业名称 6
			vecv.add(vect.get(i+6).toString());//课程代码 7
			vecv.add(vect.get(i+7).toString());//课程名称 8
			vecv.add(vect.get(i+8).toString());//行政班代码 9
			vecv.add(vect.get(i+9).toString());//行政班名称 10
			vecv.add(vect.get(i+10).toString());//每周节数（时间序列） 11
			vecv.add(vect.get(i+11).toString());//总人数 12
			vecv.add(vect.get(i+12).toString());//系数 13		
			vecv.add(vect.get(i+13).toString());//授课周次详情 14
			vecv.add(vect.get(i+14).toString());//假日周次 15
			vecv.add(vect.get(i+15).toString());//授课周数 16
			
			for(int j=16;j<vect.size();j=j+16){
				//教师相同
				if(vect.get(i+2).toString().equals(vect.get(j+2).toString())){
					vecv.add(vect.get(j).toString());//授课计划明细编号 1
					vecv.add(vect.get(j+1).toString());//学年学期编码 2
					vecv.add(vect.get(j+2).toString());//授课教师编号 3
					vecv.add(vect.get(j+3).toString());//授课教师姓名 4
					vecv.add(vect.get(j+4).toString());//层级名称 5
					vecv.add(vect.get(j+5).toString());//专业名称 6
					vecv.add(vect.get(j+6).toString());//课程代码 7
					vecv.add(vect.get(j+7).toString());//课程名称 8
					vecv.add(vect.get(j+8).toString());//行政班代码 9
					vecv.add(vect.get(j+9).toString());//行政班名称 10
					vecv.add(vect.get(j+10).toString());//每周节数（时间序列） 11
					vecv.add(vect.get(j+11).toString());//总人数 12
					vecv.add(vect.get(j+12).toString());//系数 13		
					vecv.add(vect.get(j+13).toString());//授课周次详情 14
					vecv.add(vect.get(j+14).toString());//假日周次 15
					vecv.add(vect.get(j+15).toString());//授课周数 16
					
					for(int k=0;k<16;k++){
						vect.remove(j);
					}
					j=j-16;
				}
			}
			//System.out.println(vecs.toString()+"|"+i);
	
			for(int k=0;k<16;k++){
				vect.remove(i);
			}
		}
		
		//按层级排序
		for(int i=0;vecv.size()>1;){
			vecu.add(vecv.get(i).toString());//授课计划明细编号 1
			vecu.add(vecv.get(i+1).toString());//学年学期编码 2
			vecu.add(vecv.get(i+2).toString());//授课教师编号 3
			vecu.add(vecv.get(i+3).toString());//授课教师姓名 4
			vecu.add(vecv.get(i+4).toString());//层级名称 5
			vecu.add(vecv.get(i+5).toString());//专业名称 6
			vecu.add(vecv.get(i+6).toString());//课程代码 7
			vecu.add(vecv.get(i+7).toString());//课程名称 8
			vecu.add(vecv.get(i+8).toString());//行政班代码 9
			vecu.add(vecv.get(i+9).toString());//行政班名称 10
			vecu.add(vecv.get(i+10).toString());//每周节数（时间序列） 11
			vecu.add(vecv.get(i+11).toString());//总人数 12
			vecu.add(vecv.get(i+12).toString());//系数 13		
			vecu.add(vecv.get(i+13).toString());//授课周次详情 14
			vecu.add(vecv.get(i+14).toString());//假日周次 15
			vecu.add(vecv.get(i+15).toString());//授课周数 16
			
			for(int j=16;j<vecv.size();j=j+16){
				//教师相同
				if(vecv.get(i+4).toString().equals(vecv.get(j+4).toString())){
					vecu.add(vecv.get(j).toString());//授课计划明细编号 1
					vecu.add(vecv.get(j+1).toString());//学年学期编码 2
					vecu.add(vecv.get(j+2).toString());//授课教师编号 3
					vecu.add(vecv.get(j+3).toString());//授课教师姓名 4
					vecu.add(vecv.get(j+4).toString());//层级名称 5
					vecu.add(vecv.get(j+5).toString());//专业名称 6
					vecu.add(vecv.get(j+6).toString());//课程代码 7
					vecu.add(vecv.get(j+7).toString());//课程名称 8
					vecu.add(vecv.get(j+8).toString());//行政班代码 9
					vecu.add(vecv.get(j+9).toString());//行政班名称 10
					vecu.add(vecv.get(j+10).toString());//每周节数（时间序列） 11
					vecu.add(vecv.get(j+11).toString());//总人数 12
					vecu.add(vecv.get(j+12).toString());//系数 13		
					vecu.add(vecv.get(j+13).toString());//授课周次详情 14
					vecu.add(vecv.get(j+14).toString());//假日周次 15
					vecu.add(vecv.get(j+15).toString());//授课周数 16
					
					for(int k=0;k<16;k++){
						vecv.remove(j);
					}
					j=j-16;
				}
			}
			//System.out.println(vecs.toString()+"|"+i);
	
			for(int k=0;k<16;k++){
				vecv.remove(i);
			}
		}
							
		//System.out.println("sql:"+sql4);
		Cell cell2;
		int row2=0;
		Sheet st2=wb.createSheet("sheet2",SheetInsertType.After,"sheet1");
		
		for(int colNum=1; colNum<11; colNum++){
			for(int rowNum=1; rowNum<3; rowNum++){
				//生成标题
				if(colNum==1 && rowNum==1){
					wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[10]+"1").merge();
					cell = wb.openSheet("Sheet1").openCell("A1");
					cellContent = schoolName + "   教师工作量统计";
						
					//maxWidth = 4*cellContent.length()/(vector.size()+1);
					cell.setValue(cellContent);
					
					wb.openSheet("Sheet2").openTable(colName[0]+"1:"+colName[10]+"1").merge();
					cell2 = wb.openSheet("Sheet2").openCell("A1");
					cell2.setValue(cellContent);
				}else{
					//第二行标题
					if(rowNum==2){
						cell = wb.openSheet("Sheet1").openCell(colName[colNum-1]+rowNum);//当前单元格
						cell2 = wb.openSheet("Sheet2").openCell(colName[colNum-1]+rowNum);//当前单元格
						if(colNum==1){
							cellContent="学年学期";
						}else if(colNum==2){
							cellContent="教师工号";
						}else if(colNum==3){
							cellContent="教师姓名";
						}else if(colNum==4){
							cellContent="课程名称";
						}else if(colNum==5){
							cellContent="班级名称";
						}else if(colNum==6){
							cellContent="授课周次";
						}else if(colNum==7){
							cellContent="周课时";
						}else if(colNum==8){
							cellContent="班级人数";
						}else if(colNum==9){
							cellContent="授课周数";
						}else if(colNum==10){
							cellContent="课程课时";
						}else if(colNum==11){
							cellContent="周平均课时";
						}
						
						cell.setValue(cellContent);	
						cell2.setValue(cellContent);	
					}
				}
			}
		}
		
				
		double sumhour=0;
		double sumavg=0;
		mergeNum = 0;
		if(vecu!=null&&vecu.size()>0){
			for(int i=0;i<vecu.size();i=i+16){
				tempYeaTERM = MyTools.StrFiltr(vecu.get(i+1));//学年学期
				tempTeaCode = MyTools.StrFiltr(vecu.get(i+2));//教师工号
				tempTeaName = MyTools.StrFiltr(vecu.get(i+3));//教师姓名
				tempTeaTYPE = MyTools.StrFiltr(vecu.get(i+4));//教师类别
				//tempCouNUMB = MyTools.StrFiltr(vecu.get(i+6));//课程代码
				tempCouNAME = MyTools.StrFiltr(vecu.get(i+7));//课程名称
				tempClsNAME = MyTools.StrFiltr(vecu.get(i+9));//班级名称
				//tempMajNAME = MyTools.StrFiltr(vecu.get(i+9));//专业名称
				tempWekHOUR = MyTools.StrFiltr(vecu.get(i+10));//每周节数
				tempClsNUMB = MyTools.StrFiltr(vecu.get(i+11));//班级人数
				tempClsCOEF = MyTools.StrFiltr(vecu.get(i+12));//班级系数
				tempWekSKZC = MyTools.StrFiltr(vecu.get(i+13));//授课周次
				tempCouHOUR = MyTools.StrFiltr(vecu.get(i+14));//假日周次
				tempWekNUMB = MyTools.StrFiltr(vecu.get(i+15));//授课周数
				
				cell = wb.openSheet("Sheet1").openCell(colName[0]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(tempYeaTERM.substring(0,4)+"学年第"+tempYeaTERM.substring(4,5)+"学期");
				for(int j=1;j<3;j++){
					cell = wb.openSheet("Sheet1").openCell(colName[j]+(i/16+3+mergeNum));//当前单元格
					cell.setValue(MyTools.StrFiltr(vecu.get(i+1+j)));
				}
				
				cell = wb.openSheet("Sheet1").openCell(colName[3]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr(vecu.get(i+7)));
				cell = wb.openSheet("Sheet1").openCell(colName[4]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr(vecu.get(i+9)));
				cell = wb.openSheet("Sheet1").openCell(colName[5]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr("'"+vecu.get(i+13)));
				cell = wb.openSheet("Sheet1").openCell(colName[6]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr("'"+vecu.get(i+10)));
				cell = wb.openSheet("Sheet1").openCell(colName[7]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr("'"+vecu.get(i+11)));

				
				double xs=Double.parseDouble(tempClsCOEF);//系数
				
				
				int zs=0;
				int ce=0;
//				if(vecu.get(i+4).toString().equals("外聘任课教师")){
					zs=Integer.parseInt(vecu.get(i+15).toString())-Integer.parseInt(vecu.get(i+14).toString());
//				}else{
//					if(tempYeaTERM.substring(2,4).equals(tempClsNAME.substring(0,2))&&tempYeaTERM.substring(4,5).equals("1")){//是第一学期
//						if(weeknum.equals("18")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==8){
//								if(vecu.get(i+13).toString().equals("2-9")){
//									zs=9;
//								}else if(vecu.get(i+13).toString().equals("10-18")){
//									zs=10;
//								}else{
//									zs=Integer.parseInt(vecu.get(i+15).toString());
//								}
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=14;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==17){
//								zs=19;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("17")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==7||Integer.parseInt(vecu.get(i+15).toString())==8){
//								if(vecu.get(i+13).toString().equals("2-9")){
//									zs=9;
//								}else if(vecu.get(i+13).toString().equals("10-17")){
//									zs=10;
//								}else{
//									zs=Integer.parseInt(vecu.get(i+15).toString());
//								}
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==12||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=14;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==16||Integer.parseInt(vecu.get(i+15).toString())==17){
//								zs=19;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("19")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==8){
//								zs=9;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=14;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19){
//								zs=19;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}
//						
//					}else{
//						if(weeknum.equals("18")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==10){
//								zs=10;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==15){
//								zs=15;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==20){
//								zs=20;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("17")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==8){
//								zs=10;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=15;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==20||Integer.parseInt(vecu.get(i+15).toString())==17){
//								zs=20;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("19")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==10){
//								zs=10;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==15){
//								zs=15;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==20){
//								zs=20;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}
//						
//					}
//				}
				//数据特殊
				if(ce==1){
					for(int c=0;c<13;c++){
						cell = wb.openSheet("Sheet1").openCell(colName[c]+(i/16+3+mergeNum));//当前单元格
						cell.setForeColor(Color.red);
					}
				}else{
					for(int c=0;c<13;c++){
						cell = wb.openSheet("Sheet1").openCell(colName[c]+(i/16+3+mergeNum));//当前单元格
						cell.setForeColor(Color.black);
					}
				}
				cell = wb.openSheet("Sheet1").openCell(colName[8]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(zs+"");
				
				double zks=0;
				double zzs=0;
				
					zks=(Integer.parseInt(vecu.get(i+10).toString())*zs);
					zzs=Double.parseDouble(weeknum);
				
				cell = wb.openSheet("Sheet1").openCell(colName[9]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(zks+"");
				
				double ks=(zks/zzs);
				String kss="";
				if(((ks+"").substring((ks+"").indexOf("."),(ks+"").length()).length()==2)){
					kss=(ks+"").substring(0,(ks+"").indexOf(".")+2)+"0";
				}else{
					kss=(ks+"").substring(0,(ks+"").indexOf(".")+3);
				}
				
				cell = wb.openSheet("Sheet1").openCell(colName[10]+(i/16+3+mergeNum));//当前单元格
				cell.setValue(kss);
				

				//判断单元格是否可以合并
				if((i+16)<vecu.size()){						
					if(vecu.get(i+2).toString().equals(vecu.get(i+2+16).toString())){//教师编号相同
						sumhour+=zks;
						sumavg+=Double.parseDouble(kss);
					}else{
						//合并单元格
						sumhour+=zks;
						sumavg+=Double.parseDouble(kss);
						mergeNum++;
						wb.openSheet("Sheet1").openTable(colName[0]+(i/16+3+mergeNum)+":"+colName[8]+(i/16+3+mergeNum)).merge();
						cell = wb.openSheet("Sheet1").openCell(colName[0]+(i/16+3+mergeNum));//当前单元格
						cell.setValue("小计");
						cell.setBackColor(Color.decode("#CDFFCD"));
						cell = wb.openSheet("Sheet1").openCell(colName[9]+(i/16+3+mergeNum));//当前单元格
						cell.setValue(sumhour+"");	
						cell.setBackColor(Color.decode("#CDFFCD"));
						cell = wb.openSheet("Sheet1").openCell(colName[10]+(i/16+3+mergeNum));//当前单元格
						cell.setValue(sumavg+"");
						cell.setBackColor(Color.decode("#CDFFCD"));
						for(int c=0;c<11;c++){
							cell = wb.openSheet("Sheet1").openCell(colName[c]+(i/16+3+mergeNum));//当前单元格
							cell.setForeColor(Color.black);
						}
												
						cell2 = wb.openSheet("Sheet2").openCell(colName[0]+(3+row2));//当前单元格
						cell2.setValue(tempYeaTERM.substring(0,4)+"学年第"+tempYeaTERM.substring(4,5)+"学期");
						for(int j=1;j<4;j++){
							cell2 = wb.openSheet("Sheet2").openCell(colName[j]+(3+row2));//当前单元格
							cell2.setValue(MyTools.StrFiltr(vecu.get(i+1+j)));

						}
						wb.openSheet("Sheet2").openTable(colName[3]+(3+row2)+":"+colName[8]+(3+row2)).merge();
						cell2 = wb.openSheet("Sheet2").openCell(colName[3]+(3+row2));//当前单元格
						cell2.setValue("总计");
	
						cell2 = wb.openSheet("Sheet2").openCell(colName[9]+(3+row2));//当前单元格
						cell2.setValue(sumhour+"");	
			
						cell2 = wb.openSheet("Sheet2").openCell(colName[10]+(3+row2));//当前单元格
						cell2.setValue(sumavg+"");
				
						row2++;
						
						sumhour=0;
						sumavg=0;
					}
				}else{ 
					if((i+16)==vecu.size()){//最后一行
						//if(vec3.get(i+1).toString().equals(vec3.get(i+1-12).toString())){//和上一行相同
							sumhour+=zks;
							sumavg+=Double.parseDouble(kss);
							mergeNum++;
							wb.openSheet("Sheet1").openTable(colName[0]+(i/16+3+mergeNum)+":"+colName[8]+(i/16+3+mergeNum)).merge();
							cell = wb.openSheet("Sheet1").openCell(colName[0]+(i/16+3+mergeNum));//当前单元格
							cell.setValue("小计");	
							cell.setBackColor(Color.decode("#CDFFCD"));
							cell = wb.openSheet("Sheet1").openCell(colName[9]+(i/16+3+mergeNum));//当前单元格
							cell.setValue(sumhour+"");	
							cell.setBackColor(Color.decode("#CDFFCD"));
							cell = wb.openSheet("Sheet1").openCell(colName[10]+(i/16+3+mergeNum));//当前单元格
							cell.setValue(sumavg+"");	
							cell.setBackColor(Color.decode("#CDFFCD"));
							for(int c=0;c<11;c++){
								cell = wb.openSheet("Sheet1").openCell(colName[c]+(i/16+3+mergeNum));//当前单元格
								cell.setForeColor(Color.black);
							}
							
						//}
							cell2 = wb.openSheet("Sheet2").openCell(colName[0]+(3+row2));//当前单元格
							cell2.setValue(tempYeaTERM.substring(0,4)+"学年第"+tempYeaTERM.substring(4,5)+"学期");
							for(int j=1;j<4;j++){
								cell2 = wb.openSheet("Sheet2").openCell(colName[j]+(3+row2));//当前单元格
								cell2.setValue(MyTools.StrFiltr(vecu.get(i+1+j)));

							}
							wb.openSheet("Sheet2").openTable(colName[3]+(3+row2)+":"+colName[8]+(3+row2)).merge();
							cell2 = wb.openSheet("Sheet2").openCell(colName[3]+(3+row2));//当前单元格
							cell2.setValue("总计");
				
							cell2 = wb.openSheet("Sheet2").openCell(colName[9]+(3+row2));//当前单元格
							cell2.setValue(sumhour+"");	
				
							cell2 = wb.openSheet("Sheet2").openCell(colName[10]+(3+row2));//当前单元格
							cell2.setValue(sumavg+"");
						
							row2++;
							
							sumhour=0;
							sumavg=0;
					}
				}
				
					
			}
		}
		
		int zjs=vecu.size()/16;//行数
			
		//设置单元格的水平对齐方式
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[10]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[10]+(zjs+2+mergeNum)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);

		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置课表边框线
		Border border = wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[10]+(zjs+2+mergeNum)).getBorder();
		//设置表格边框的宽度、颜色
		border.setWeight(XlBorderWeight.xlThin);
		border.setLineColor(Color.black);
	
		//设置标题字体大小
		cell = wb.openSheet("Sheet1").openCell("A1");
		cell.getFont().setBold(true);
		fontSize = 18;
		cell.getFont().setSize(fontSize);

		//设置课表标题行列字体大小
		fontSize = 12;
		wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[10]+"2").getFont().setSize(fontSize);
		//wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[0]+(zjs+1)).getFont().setSize(fontSize);
		//设置课表内容行列字体大小
		fontSize = 10;
		wb.openSheet("Sheet1").openTable(colName[0]+"3:"+colName[10]+(zjs+2+mergeNum)).getFont().setSize(fontSize);
		
		//设置表格列宽
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(16);
		wb.openSheet("Sheet1").openTable(colName[1]+"1:"+colName[10]+"1").setColumnWidth(10);
		wb.openSheet("Sheet1").openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(20);
		wb.openSheet("Sheet1").openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(30);
		//设置表格行高
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[10]+"2").setRowHeight(40);
		wb.openSheet("Sheet1").openTable(colName[0]+"3:"+colName[10]+(zjs+2+mergeNum)).setRowHeight(30);
		
		
		wb.openSheet("Sheet2").openTable(colName[0]+"1:"+colName[10]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		wb.openSheet("Sheet2").openTable(colName[0]+"2:"+colName[10]+(2+row2)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);

		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置课表边框线
		Border border2 = wb.openSheet("Sheet2").openTable(colName[0]+"2:"+colName[10]+(2+row2)).getBorder();
		//设置表格边框的宽度、颜色
		border2.setWeight(XlBorderWeight.xlThin);
		border2.setLineColor(Color.black);
	
		//设置标题字体大小
		cell2 = wb.openSheet("Sheet2").openCell("A1");
		cell2.getFont().setBold(true);
		fontSize = 18;
		cell2.getFont().setSize(fontSize);

		//设置课表标题行列字体大小
		fontSize = 12;
		wb.openSheet("Sheet2").openTable(colName[0]+"2:"+colName[10]+"2").getFont().setSize(fontSize);
		//wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[0]+(zjs+1)).getFont().setSize(fontSize);
		//设置课表内容行列字体大小
		fontSize = 10;
		wb.openSheet("Sheet2").openTable(colName[0]+"3:"+colName[10]+(2+row2)).getFont().setSize(fontSize);
		
		//设置表格列宽
		wb.openSheet("Sheet2").openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(16);
		wb.openSheet("Sheet2").openTable(colName[1]+"1:"+colName[10]+"1").setColumnWidth(10);
		wb.openSheet("Sheet2").openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(20);
		wb.openSheet("Sheet2").openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(30);
		//设置表格行高
		wb.openSheet("Sheet2").openTable(colName[0]+"1:"+colName[10]+"2").setRowHeight(40);
		wb.openSheet("Sheet2").openTable(colName[0]+"3:"+colName[10]+(2+row2)).setRowHeight(30);
	
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		//poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "template.xls";

		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("下载", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "   教师工作量统计");
		poCtrl1.setFileTitle(schoolName + "   教师工作量统计");//设置另存为窗口默认文件名
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
	}
	
	/**
	 * 教师授课情况统计
	 * @date:2015-12-09
	 * @author:lupengfei
	 * @param xnxq+jxxz 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	public static void exportTeachSituation(HttpServletRequest request, String exportType, String XNXQ, String TEAID, String TEANAME) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		System.out.println("TEANAME--"+TEANAME);
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 40;
		float fontSize = 0;
		
		Workbook wb = new Workbook();
		Cell cell;
		String cellContent = ""; //当前单元格的内容
		Vector tempVec = new Vector();
		Vector hbSetVec = new Vector();
		Vector timeVec = null;
		
		int mergeNum = 0;//单元格合并数
		boolean flag = true;//用于判断是否可合并
		
		String tempYeaTERM = "";//学年学期
		String tempTeaCode = "";//教师工号
		String tempTeaName = "";//教师姓名
		String tempTeaTYPE = "";//教师类别
		String tempCouNUMB = "";//课程代码
		String tempCouNAME = "";//课程名称
		String tempClsNAME = "";//班级名称
		String tempMajNAME = "";//专业名称
		String tempWekHOUR = "";//周课时
		String tempClsNUMB = "";//班级人数
		//String tempClsCOEF = "";//班级系数
		String tempCouHOUR = "";//课程课时
		String tempWekNUMB = "";//周数
		//String tempAvgHOUR = "";//周平均课时
		String tempExaFORM = "";//考试形式
		String tempWeekDAY = "";//星期
		String tempTeaNUMB = "";//授课节数
		String tempTeaCLAS = "";//教室
		
		String skjhmxbhArray[] = new String[0];
		
		String sql = "";
		String sql0 = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		String sql6 = "";
		String sql7 = "";
		Vector vec0 = null;
		Vector vec1 = null;
		Vector vec3 = null;
		Vector vec4 = null;
		Vector vec7 = null;
		Vector vectk = null;
		Vector vec=new Vector();
		Vector vecr=new Vector();
		Vector vecs=new Vector();
		Vector vect=new Vector();
		Vector vecu=new Vector();
		Vector vecv=new Vector();
		String skjhbh=null;//授课计划明细编号
		String xnxqbm=null;//学年学期编码
		String skjsbh=null;//授课教师编号
		String skjsxm=null;//授课教师姓名
		String zyname=null;//专业名称
		String kechid=null;//课程代码
		String kechna=null;//课程名称
		String clasid=null;//行政班代码
		String clasna=null;//行政班名称
		String timexl=null;//时间序列
		String sjcdmc=null;//实际场地名称
		String skzcxq=null;//授课周次详情
		String skcdmc=null;//场地名称
		String skksxs=null;//考试形式
		
		String sqlpkgl="";
		String sqlpkgl1="";
		String sqlpkgl2="";
		String sqlpkgl4="";
//		int a=0;
//		int b=0;
//		int c=0;
		int d=0;
		int f=0;
		String weeknum="";
		String[] skjhmxbhhb1=null;//保存授课计划明细编号
		String[] classnamehb=null;//保存对应的班级名称
		String[] classnumbhb=null;//保存合班后的班级总人数
		String heban1="";//保存第一项合班授课计划编号
		String heban2="";//保存除了第一项的合班授课计划编号
		
		//获取本学期周数量
		String sqlweek = " select 实际上课周数 FROM dbo.V_规则管理_学年学期表  where 学年学期编码='"+MyTools.fixSql(XNXQ)+"'" ;
		Vector vecwek = db.GetContextVector(sqlweek);
		if(vecwek!=null&vecwek.size()>0){
			weeknum=vecwek.get(0).toString();			
		}
		
		//查询合班信息
				String sqlhb="select 授课计划明细编号 from dbo.V_规则管理_合班表 where SUBSTRING(授课计划明细编号,0,CHARINDEX('+',授课计划明细编号)) in ( " +
						"select 授课计划明细编号 from dbo.V_规则管理_授课计划明细表 where [授课计划主表编号] in ( " +
						"select [授课计划主表编号] from dbo.V_规则管理_授课计划主表 where [学年学期编码]='"+XNXQ+"' ) ) ";
				Vector vechb = null;
				Vector vechb3 = null;
				vechb=db.GetContextVector(sqlhb);
				if(vechb!=null&&vechb.size()>0){
					skjhmxbhhb1=new String[vechb.size()];//保存授课计划明细编号
					classnamehb=new String[vechb.size()];//保存对应的班级名称
					classnumbhb=new String[vechb.size()];//保存合班后的班级总人数
					heban1="";//保存第一项合班授课计划编号
					heban2="";//保存除了第一项的合班授课计划编号
					for(int m=0;m<vechb.size();m++){
						String skjhmxbh=vechb.get(m).toString();
						String[] skjhbhid=skjhmxbh.split("\\+");
						String sqlhb2= "";
							
						for(int n=0;n<skjhbhid.length;n++){
							if(n==0){
								sqlhb2+="'"+skjhbhid[n]+"'";
								heban1+=skjhbhid[n]+",";
								skjhmxbhhb1[m]=skjhbhid[n];
							}else{
								sqlhb2+=",'"+skjhbhid[n]+"'";
								heban2+=skjhbhid[n]+",";
							}
						}
						heban1=heban1.substring(0, heban1.length()-1);
						heban2=heban2.substring(0, heban2.length()-1);
								
						String sqlhb3="select b.学年学期编码,a.课程名称,c.行政班名称,c.总人数 from dbo.V_规则管理_授课计划明细表 a " +
									"inner join dbo.V_规则管理_授课计划主表 b on a.授课计划主表编号=b.授课计划主表编号 " +
									"inner join dbo.V_学校班级数据子类 c on b.行政班代码=c.行政班代码 " +
									"where a.授课计划明细编号 in ("+sqlhb2+")";
						vechb3=db.GetContextVector(sqlhb3);
						if(vechb3!=null&&vechb3.size()>0){
							String classname="";
							int classnumb=0;
							for(int k=0;k<vechb3.size();k=k+4){
								classname+=vechb3.get(k+2).toString()+"+";	
								classnumb=classnumb+Integer.parseInt(vechb3.get(k+3).toString());
							}				
							classname=classname.substring(0, classname.length()-1);	
								
							classnamehb[m]=classname;
							classnumbhb[m]=classnumb+"";
						}
					}
				}//vechb
				
					//查询层级信息
//					String sqllv="select 层级名称 from dbo.V_层级表 where 状态='1' ";
//					Vector veclv=db.GetContextVector(sqllv);
//					String[] tealvinfo=new String[veclv.size()];
//					String[] tealvname=new String[veclv.size()];
//					for(int i=0;i<veclv.size();i++){
//						String sqltea="select c.usercode,e.层级名称 from V_USER_AUTH c,V_权限层级关系表 d,V_层级表 e where c.AuthCode=d.权限编号 and d.层级编号=e.层级编号 and e.层级名称='"+veclv.get(i).toString()+"' and len(usercode)>5";
//						Vector vectea=db.GetContextVector(sqltea);
//						if(vectea!=null&&vectea.size()>0){
//							for(int j=0;j<vectea.size();j=j+2){
//								tealvinfo[i]+="@"+vectea.get(j).toString()+"@,";
//							}					
//						}	
//						tealvname[i]=vectea.get(1).toString();
//					}
						
					//计算本学期周一到周五节假日有几次
					String xnxqid="";//学年学期编码
					String jjrq="";//节假日期
					String xqkssj="";//学期开始时间
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					int week=-1;
					String week1="";
					String week2="";
					String week3="";
					String week4="";
					String week5="";
					
					String sqltm="select 学年学期编码,节假日期,学期开始时间,实际上课周数 from dbo.V_规则管理_学年学期表 where 学年学期编码='"+XNXQ+"' ";
					
					Vector vecxq=db.GetContextVector(sqltm);
					if(vecxq!=null&&vecxq.size()>0){
						//this.setMSG(vecxq.get(3).toString());	
						for(int i=0;i<vecxq.size();i=i+4){
							xnxqid=vecxq.get(i).toString();
							jjrq=vecxq.get(i+1).toString()+",";	
							xqkssj=vecxq.get(i+2).toString();	
							try {
								cal.setTime(format.parse(xqkssj));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//学期开始是一年的第几周
							int beginweek=cal.get(Calendar.WEEK_OF_YEAR);
							
							//学期开始日期是星期几
							int begindate = cal.get(Calendar.DAY_OF_WEEK)-1;
							System.out.println(begindate);
							//将学期开始日期所在星期的前几天加入到节假日期里
							for(int t=1;t<begindate;t++){
								 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
								 String str = xqkssj; 
								 Date dt=sdf.parse(str,new ParsePosition(0)); 
								 Calendar rightNow = Calendar.getInstance(); 
								 rightNow.setTime(dt); 
								 rightNow.add(Calendar.DATE,-(begindate-t));//你要加减的日期   
								 Date dt1=rightNow.getTime(); 
								 String reStr=sdf.format(dt1); 
								 System.out.println("reStr:--"+reStr);
								 if(!reStr.endsWith("")){
									 jjrq+=reStr+",";
								 }
								
							}
							jjrq=jjrq.substring(0, jjrq.length()-1);
							
							String[] freedate=jjrq.split(",");
							if(jjrq.equals("")){
								
							}else{
								for(int j=0;j<freedate.length;j++){
								    try {
										cal.setTime(format.parse(freedate[j]));
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								    week = cal.get(Calendar.DAY_OF_WEEK)-1;
	
									int weeks=0;//第几周
									try {
										weeks=(daysBetween(xqkssj,freedate[j])+begindate)/7+1;
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								    if(week==6||week==0){
								    	
								    }else{
								    	if(week==1){
								    		week1+=weeks+",";
								    	}else if(week==2){
								    		week2+=weeks+",";
								    	}else if(week==3){
								    		week3+=weeks+",";
								    	}else if(week==4){
								    		week4+=weeks+",";
								    	}else if(week==5){
								    		week5+=weeks+",";
								    	}
								    }				   
								}
							}
							
							//哪几周星期几是假日
							if(!week1.equals("")){
								week1=week1.substring(0, week1.length()-1);
							}
							if(!week2.equals("")){
								week2=week2.substring(0, week2.length()-1);
							}
							if(!week3.equals("")){
								week3=week3.substring(0, week3.length()-1);
							}
							if(!week4.equals("")){
								week4=week4.substring(0, week4.length()-1);
							}
							if(!week5.equals("")){
								week5=week5.substring(0, week5.length()-1);
							}
							
						}		
					}
					
					//20160826，改用课程表周详情表查询
					sql0="SELECT a.授课计划明细编号,a.学年学期编码,a.授课教师编号,a.授课教师姓名,a.专业名称,a.课程代码,a.课程名称,a.行政班代码,a.行政班名称,a.时间序列,b.总人数,a.授课周次,a.场地名称,d.考试形式  " +
						" FROM dbo.V_排课管理_课程表周详情表 a,dbo.V_学校班级数据子类 b,V_规则管理_授课计划明细表 c,V_考试形式 d " +
						" where a.行政班代码=b.行政班代码 and a.授课计划明细编号=c.授课计划明细编号 and c.考试形式=d.编号 and a.授课计划明细编号 != '' and a.状态='1' and a.学年学期编码='"+XNXQ+"' order by a.授课计划明细编号,a.授课教师编号,a.行政班名称,a.时间序列,convert(int,a.授课周次) " ;
					
					vec0=db.GetContextVector(sql0);
					if(vec0!=null&&vec0.size()>0){
						String skzc="";
						for(int i=0;i<vec0.size()-14;){
							skzc=vec0.get(i+11).toString();
							
								while(i<(vec0.size()-14)&&vec0.get(i).toString().equals(vec0.get(i+14).toString())&&vec0.get(i+9).toString().equals(vec0.get(i+9+14).toString())){//授课计划编号相等+时间序列相等					
									skzc+="#"+vec0.get(i+14+11).toString();
									//System.out.println(i+":--"+skzc);
									i=i+14;
								}
								skzcxq=merge(skzc);
								String[] skjsbh5=vec0.get(i+2).toString().split("\\+");
								String[] skjsxm5=vec0.get(i+3).toString().split("\\+");
								for(int m=0;m<skjsbh5.length;m++){
									vec.add(vec0.get(i).toString());//授课计划明细编号
									vec.add(vec0.get(i+1).toString());//学年学期编码
									vec.add(skjsbh5[m]);//授课教师编号
									vec.add(skjsxm5[m]);//授课教师姓名
									vec.add(vec0.get(i+4).toString());//专业名称
									vec.add(vec0.get(i+5).toString());//课程代码
									vec.add(vec0.get(i+6).toString());//课程名称
									vec.add(vec0.get(i+7).toString());//行政班代码
									vec.add(vec0.get(i+8).toString());//行政班名称
									vec.add(vec0.get(i+9).toString());//时间序列
									vec.add(vec0.get(i+10).toString());//总人数
									vec.add(skzcxq);//授课周次详情
									vec.add(vec0.get(i+12).toString());//场地名称
									vec.add(vec0.get(i+13).toString());//考试形式
								}
								i=i+14;
						}					
					}
					
					//选修课明细表
					sql1=" SELECT  a.授课计划明细编号,b.学年学期编码,a.授课教师编号,a.授课教师姓名,'选修课' as 专业名称,b.课程代码,b.课程名称,'' as 行政班代码,a.选修班名称 as 行政班名称,c.时间序列,a.报名人数,a.授课周次 as 授课周次详情,a.场地名称,'' as 考试形式 " +
						 " FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b,V_排课管理_选修课课程表信息表 c " +
						 " where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号=c.授课计划明细编号  and b.学年学期编码='"+XNXQ+"' ";
					vec1=db.GetContextVector(sql1);
					if(vec1!=null&&vec1.size()>0){
						for(int i=0;i<vec1.size();i=i+14){
							skjhbh=vec1.get(i).toString();//授课计划明细编号
							xnxqbm=vec1.get(i+1).toString();//学年学期编码
							skjsbh=vec1.get(i+2).toString();//授课教师编号
							skjsxm=vec1.get(i+3).toString();//授课教师姓名
							zyname=vec1.get(i+4).toString();//专业名称
							kechid=vec1.get(i+5).toString();//课程代码
							kechna=vec1.get(i+6).toString();//课程名称
							clasid=vec1.get(i+7).toString();//行政班代码
							clasna=vec1.get(i+8).toString();//行政班名称
							timexl=vec1.get(i+9).toString();//时间序列
							sjcdmc=vec1.get(i+10).toString();//报名人数
							skzcxq=vec1.get(i+11).toString();//授课周次详情
							skcdmc=vec1.get(i+12).toString();//场地名称
							skksxs=vec1.get(i+13).toString();//考试形式
							
							//System.out.println(a+":"+skjhbh+","+xnxqbm+","+skjsbh+","+skjsxm+","+zyname+","+kechid+","+kechna+","+clasid+","+clasna+","+timexl+","+sjcdmc+","+skzcxq); a++;
							String[] skjhbh2=skjhbh.split("｜");
							String[] skjsbh2=skjsbh.split("｜");
							String[] skjsxm2=skjsxm.split("｜");
							String[] kechid2=kechid.split("｜");
							String[] kechna2=kechna.split("｜");
							String[] skzcxq2=skzcxq.split("｜");
							for(int j=0;j<skjhbh2.length;j++){
								//System.out.println(b+":"+skjhbh2[j]+","+skjsbh2[j]+","+skjsxm2[j]+","+kechid2[j]+","+kechna2[j]+","+sjcdmc2[j]+","+skzcxq2[j]); b++;
								String[] skjsbh3=skjsbh2[j].split("\\&");
								String[] skjsxm3=skjsxm2[j].split("\\&");
								String[] skzcxq3=skzcxq2[j].split("\\&");
								for(int k=0;k<skjsbh3.length;k++){
									//System.out.println(c+":"+skjsbh3[k]+","+skjsxm3[k]+","+sjcdmc3[k]+","+skzcxq3[k]); c++;
									String[] skjsbh4=skjsbh3[k].split("\\+");
									String[] skjsxm4=skjsxm3[k].split("\\+");
									for(int m=0;m<skjsbh4.length;m++){
										//System.out.println(d+":"+skjsbh4[m]+","+skjsxm4[m]); 
										//System.out.println(d+":"+skjhbh2[j]+","+xnxqbm+","+skjsbh4[m]+","+skjsxm4[m]+","+zyname+","+kechid2[j]+","+kechna2[j]+","+clasid+","+clasna+","+timexl+","+sjcdmc3[k]+","+skzcxq3[k]); 
										//查询类型不是全部
										
											if(skjsbh4[m].equals("")&&skjsxm4[m].equals("")){
												
											}else{
												vec.add(skjhbh2[j]);//授课计划明细编号
												vec.add(xnxqbm);//学年学期编码
												vec.add(skjsbh4[m]);//授课教师编号
												vec.add(skjsxm4[m]);//授课教师姓名
												vec.add(zyname);//专业名称
												vec.add(kechid2[j]);//课程代码
												vec.add(kechna2[j]);//课程名称
												vec.add(clasid);//行政班代码
												vec.add(clasna);//行政班名称
												vec.add(timexl);//时间序列
												vec.add(sjcdmc);//报名人数
												vec.add(skzcxq3[k]);//授课周次详情
												vec.add(skcdmc);//场地名称
												vec.add(skksxs);//考试形式
											}
											
									}
								}
							}
						}	
					}
					
					//查询条件	
					String usercodes="";
					int taguc1=0;
					int taguc2=0;
					int taguc3=0;

//					for(int i=0;i<vec.size();i=i+14){
//						if(vec.get(i+3).toString().equals("金勇")){
//						System.out.println("授课计划明细编号:"+vec.get(i).toString()+","+"学年学期编码:"+vec.get(i+1).toString()+","+"授课教师编号:"+vec.get(i+2).toString()+","+"授课教师姓名:"+vec.get(i+3).toString()+","+"专业名称:"+vec.get(i+4).toString()+","+"课程代码:"+vec.get(i+5).toString()+","+"课程名称:"+vec.get(i+6).toString()+","+"行政班代码:"+vec.get(i+7).toString()+","+"行政班名称:"+vec.get(i+8).toString()+","+"时间序列:"+vec.get(i+9).toString()+","+"总人数:"+vec.get(i+10).toString()+","+"授课周次详情:"+vec.get(i+11).toString()+",");
//						}
//					}
					
					for(int v=0;v<vec.size();v=v+14){
						if(!TEAID.equalsIgnoreCase("")){
							if(vec.get(v+2).toString().indexOf(TEAID)>-1){
								taguc2=1;
							}else{
								taguc2=0;
							}
						}else{
							taguc2=1;
						}
						if(!TEANAME.equalsIgnoreCase("")){
							if(vec.get(v+3).toString().indexOf(TEANAME)>-1){
								taguc3=1;
							}else{
								taguc3=0;
							}
						}else{
							taguc3=1;
						}
						if(taguc2==1&&taguc3==1){
							vecr.add(vec.get(v).toString());//授课计划明细编号 1
							vecr.add(vec.get(v+1).toString());//学年学期编码 2
							vecr.add(vec.get(v+2).toString());//授课教师编号 3
							vecr.add(vec.get(v+3).toString());//授课教师姓名 4
							
//							String tealv="";
//							for(int i=0;i<veclv.size();i++){
//								if(tealvinfo[i].indexOf("@"+vec.get(v+2).toString()+"@")>-1){
//									tealv=tealvname[i];
//								}							
//							}
							vecr.add("");//层级名称 5
							
							vecr.add(vec.get(v+4).toString());//专业名称 6
							vecr.add(vec.get(v+5).toString());//课程代码 7
							vecr.add(vec.get(v+6).toString());//课程名称 8
							vecr.add(vec.get(v+7).toString());//行政班代码 9
							vecr.add(vec.get(v+8).toString());//行政班名称 10
							vecr.add(vec.get(v+9).toString());//时间序列 11
							vecr.add(vec.get(v+10).toString());//总人数 12
							
							int rs=Integer.parseInt(vec.get(v+10).toString());					
							double xs=0;
							if(rs<40){
								xs=1.0;
							}else if(40<=rs&&rs<50){
								xs=1.1;
							}else if(50<=rs&&rs<60){
								xs=1.2;
							}else if(60<=rs){
								xs=1.3;
							}
							vecr.add(xs);//系数 13
							
							vecr.add(vec.get(v+11).toString());//授课周次详情 14
							
							String jrzc="";
							if(vec.get(v+9).toString().substring(0, 2).equals("01")){
								jrzc=week1;
							}else if(vec.get(v+9).toString().substring(0, 2).equals("02")){
								jrzc=week2;
							}else if(vec.get(v+9).toString().substring(0, 2).equals("03")){
								jrzc=week3;
							}else if(vec.get(v+9).toString().substring(0, 2).equals("04")){
								jrzc=week4;
							}else if(vec.get(v+9).toString().substring(0, 2).equals("05")){
								jrzc=week5;
							}
							vecr.add(jrzc);//假日周次 15
							
							int skzs=0;
							if(vec.get(v+11).toString().equals("odd")){
								skzs=(Integer.parseInt(vecxq.get(3).toString())+1)/2;
							}else if(vec.get(v+11).toString().equals("even")){
								skzs=Integer.parseInt(vecxq.get(3).toString())/2;
							}else if(vec.get(v+11).toString().indexOf("-")>-1){
								skzs=Integer.parseInt(vec.get(v+11).toString().split("-")[1])-Integer.parseInt(vec.get(v+11).toString().split("-")[0])+1;
							}else if(vec.get(v+11).toString().indexOf("#")>-1){
								skzs=vec.get(v+11).toString().split("#").length;
							}else{
								skzs=1;
							}
							vecr.add(skzs+"");//授课周数 16
							vecr.add(vec.get(v+12).toString());//场地名称 17
							vecr.add(vec.get(v+13).toString());//考试形式 18
						}
					}
					
//					for(int i=0;i<vecr.size();i=i+18){
//						System.out.println("r授课计划明细编号:"+vecr.get(i).toString()+","+"学年学期编码:"+vecr.get(i+1).toString()+","+"授课教师编号:"+vecr.get(i+2).toString()+","+"授课教师姓名:"+vecr.get(i+3).toString()+","+"层级名称:"+vecr.get(i+4).toString()+","+
//								"专业名称:"+vecr.get(i+5).toString()+","+"课程代码:"+vecr.get(i+6).toString()+","+"课程名称:"+vecr.get(i+7).toString()+","+"行政班代码:"+vecr.get(i+8).toString()+","+"行政班名称:"+vecr.get(i+9).toString()+","+"时间序列:"+vecr.get(i+10).toString()+","+"总人数:"+vecr.get(i+11).toString()+","+
//								"系数:"+vecr.get(i+12).toString()+","+"授课周次详情:"+vecr.get(i+13).toString()+","+"假日周次:"+vecr.get(i+14).toString()+","+"授课周数:"+vecr.get(i+15).toString()+"," );
//					}
					
					//合班
					for(int v=0;v<vecr.size();v=v+18){		
						if(heban1.indexOf(vecr.get(v).toString())>-1){//属于第一项合班，班级名称改为合班班级
							for(int m=0;m<skjhmxbhhb1.length;m++){
								if(vecr.get(v).toString().equals(skjhmxbhhb1[m])){
									vecs.add(vecr.get(v).toString());//授课计划明细编号 1
									vecs.add(vecr.get(v+1).toString());//学年学期编码 2
									vecs.add(vecr.get(v+2).toString());//授课教师编号 3
									vecs.add(vecr.get(v+3).toString());//授课教师姓名 4
									vecs.add(vecr.get(v+4).toString());//层级名称 5
									vecs.add(vecr.get(v+5).toString());//专业名称 6
									vecs.add(vecr.get(v+6).toString());//课程代码 7
									vecs.add(vecr.get(v+7).toString());//课程名称 8
									vecs.add(vecr.get(v+8).toString());//行政班代码 9
									if(vechb!=null&&vechb.size()>0){
										vecs.add(classnamehb[m]);//行政班名称 10
									}else{
										vecs.add(vecr.get(v+9).toString());//行政班名称 10
									}
									vecs.add(vecr.get(v+10).toString());//时间序列 11
									if(vechb!=null&&vechb.size()>0){
										vecs.add(classnumbhb[m]);//总人数 12
									}else{
										vecs.add(vecr.get(v+11).toString());//总人数 12
									}
									int rs=0;
									if(vechb!=null&&vechb.size()>0){
										rs=Integer.parseInt(classnumbhb[m]);
									}else {
										rs=Integer.parseInt(vecr.get(v+11).toString());
									}
									double xs=0;
									if(rs<40){
										xs=1.0;
									}else if(40<=rs&&rs<50){
										xs=1.1;
									}else if(50<=rs&&rs<60){
										xs=1.2;
									}else if(60<=rs){
										xs=1.3;
									}
									vecs.add(xs);//系数 13		
									vecs.add(vecr.get(v+13).toString());//授课周次详情 14
									vecs.add(vecr.get(v+14).toString());//假日周次 15
									vecs.add(vecr.get(v+15).toString());//授课周数 16
									vecs.add(vecr.get(v+16).toString());//场地名称 17
									vecs.add(vecr.get(v+17).toString());//考试形式 18
								}
							}	
						}else if(heban2.indexOf(vecr.get(v).toString())>-1){//不属于第一项合班,不添加这条信息
							
						}else{
							vecs.add(vecr.get(v).toString());//授课计划明细编号 1
							vecs.add(vecr.get(v+1).toString());//学年学期编码 2
							vecs.add(vecr.get(v+2).toString());//授课教师编号 3
							vecs.add(vecr.get(v+3).toString());//授课教师姓名 4
							vecs.add(vecr.get(v+4).toString());//层级名称 5
							vecs.add(vecr.get(v+5).toString());//专业名称 6
							vecs.add(vecr.get(v+6).toString());//课程代码 7
							vecs.add(vecr.get(v+7).toString());//课程名称 8
							vecs.add(vecr.get(v+8).toString());//行政班代码 9
							vecs.add(vecr.get(v+9).toString());//行政班名称 10
							vecs.add(vecr.get(v+10).toString());//时间序列 11
							vecs.add(vecr.get(v+11).toString());//总人数 12
							vecs.add(vecr.get(v+12).toString());//系数 13		
							vecs.add(vecr.get(v+13).toString());//授课周次详情 14
							vecs.add(vecr.get(v+14).toString());//假日周次 15
							vecs.add(vecr.get(v+15).toString());//授课周数 16
							vecs.add(vecr.get(v+16).toString());//场地名称 17
							vecs.add(vecr.get(v+17).toString());//考试形式 18
						}
					}

//					for(int i=0;i<vecs.size();i=i+18){
//						System.out.println("s授课计划明细编号:"+vecs.get(i).toString()+","+"学年学期编码:"+vecs.get(i+1).toString()+","+"授课教师编号:"+vecs.get(i+2).toString()+","+"授课教师姓名:"+vecs.get(i+3).toString()+","+"层级名称:"+vecs.get(i+4).toString()+","+
//							"专业名称:"+vecs.get(i+5).toString()+","+"课程代码:"+vecs.get(i+6).toString()+","+"课程名称:"+vecs.get(i+7).toString()+","+"行政班代码:"+vecs.get(i+8).toString()+","+"行政班名称:"+vecs.get(i+9).toString()+","+"时间序列:"+vecs.get(i+10).toString()+","+"总人数:"+vecs.get(i+11).toString()+","+
//							"系数:"+vecs.get(i+12).toString()+","+"授课周次详情:"+vecs.get(i+13).toString()+","+"假日周次:"+vecs.get(i+14).toString()+","+"授课周数:"+vecs.get(i+15).toString()+"," );
//					}
					
					//计算总课时
					int counum=1;//每周上课节数
					for(int i=0;vecs.size()>1;){
						for(int j=18;j<vecs.size();j=j+18){
							//授课计划编号，班级，课程,教师姓名,时间序列前2位相同
							if(vecs.get(i).toString().equals(vecs.get(j).toString())&&vecs.get(i+9).toString().equals(vecs.get(j+9).toString())&&vecs.get(i+6).toString().equals(vecs.get(j+6).toString())&&vecs.get(i+2).toString().equals(vecs.get(j+2).toString())&&vecs.get(i+10).toString().substring(0,2).equals(vecs.get(j+10).toString().substring(0,2)) ){
								counum++;
								for(int k=0;k<18;k++){
									vecs.remove(j);
								}
								j=j-18;
							}
						}
						//System.out.println(vecs.toString()+"|"+i);
						
						vect.add(vecs.get(i).toString());//授课计划明细编号 1
						vect.add(vecs.get(i+1).toString());//学年学期编码 2
						vect.add(vecs.get(i+2).toString());//授课教师编号 3
						vect.add(vecs.get(i+3).toString());//授课教师姓名 4
						vect.add(vecs.get(i+4).toString());//层级名称 5
						vect.add(vecs.get(i+5).toString());//专业名称 6
						vect.add(vecs.get(i+6).toString());//课程代码 7
						vect.add(vecs.get(i+7).toString());//课程名称 8
						vect.add(vecs.get(i+8).toString());//行政班代码 9
						vect.add(vecs.get(i+9).toString());//行政班名称 10
						vect.add(counum);//每周节数（时间序列） 11
						vect.add(vecs.get(i+11).toString());//总人数 12
						vect.add(vecs.get(i+12).toString());//系数 13		
						vect.add(vecs.get(i+13).toString());//授课周次详情 14
						
						int number=0;
						if(!vecs.get(i+14).toString().equals("")){//假日周次不为空
							String jrzc=vecs.get(i+14).toString();
							String skzc=vecs.get(i+13).toString();
							String[] jrzcnum=jrzc.split(",");
							String weeks="";
							number=0;
							for(int j=0;j<jrzcnum.length;j++){
								weeks="";
								if(skzc.indexOf("-")>-1){
									String[] wek=skzc.split("-");
									for(int r=Integer.parseInt(wek[0]);r<=Integer.parseInt(wek[1]);r++){
										weeks+=r+"#";	
									}
									weeks=weeks.substring(0,weeks.length()-1);
								}else if(skzc.indexOf("\\#")>-1){
									weeks=skzc;
								}else{
									weeks=skzc;
								}
								String[] weeks2=weeks.split("\\#");
								for(int s=0;s<weeks2.length;s++){
									if(jrzcnum[j].equals(weeks2[s])){//存在
										number++;
									}
								}
							}
						}else{
							number=0;
						}
						vect.add(number);//假日周次 15
						
						vect.add(vecs.get(i+15).toString());//授课周数 16
						vect.add(vecs.get(i+16).toString());//场地名称 17
						vect.add(vecs.get(i+17).toString());//考试形式 18
						
						String sjxlweel=vecs.get(i+10).toString().substring(0,2);
						vect.add(sjxlweel);//时间序列 19
						counum=1;
						for(int k=0;k<18;k++){
							vecs.remove(i);
						}
						
					}

//					for(int i=0;i<vect.size();i=i+19){
//						System.out.println("t授课计划明细编号:"+vect.get(i).toString()+","+"学年学期编码:"+vect.get(i+1).toString()+","+"授课教师编号:"+vect.get(i+2).toString()+","+"授课教师姓名:"+vect.get(i+3).toString()+","+"层级名称:"+vect.get(i+4).toString()+","+
//							"专业名称:"+vect.get(i+5).toString()+","+"课程代码:"+vect.get(i+6).toString()+","+"课程名称:"+vect.get(i+7).toString()+","+"行政班代码:"+vect.get(i+8).toString()+","+"行政班名称:"+vect.get(i+9).toString()+","+"每周节数:"+vect.get(i+10).toString()+","+"总人数:"+vect.get(i+11).toString()+","+
//							"系数:"+vect.get(i+12).toString()+","+"授课周次详情:"+vect.get(i+13).toString()+","+"假日周次:"+vect.get(i+14).toString()+","+"授课周数:"+vect.get(i+15).toString()+","+","+"时间序列:"+vect.get(i+18).toString() );
//					}
					
					
					//按教师排序
					for(int i=0;vect.size()>1;){
						vecv.add(vect.get(i).toString());//授课计划明细编号 1
						vecv.add(vect.get(i+1).toString());//学年学期编码 2
						vecv.add(vect.get(i+2).toString());//授课教师编号 3
						vecv.add(vect.get(i+3).toString());//授课教师姓名 4
						vecv.add(vect.get(i+4).toString());//层级名称 5
						vecv.add(vect.get(i+5).toString());//专业名称 6
						vecv.add(vect.get(i+6).toString());//课程代码 7
						vecv.add(vect.get(i+7).toString());//课程名称 8
						vecv.add(vect.get(i+8).toString());//行政班代码 9
						vecv.add(vect.get(i+9).toString());//行政班名称 10
						vecv.add(vect.get(i+10).toString());//每周节数（时间序列） 11
						vecv.add(vect.get(i+11).toString());//总人数 12
						vecv.add(vect.get(i+12).toString());//系数 13		
						vecv.add(vect.get(i+13).toString());//授课周次详情 14
						vecv.add(vect.get(i+14).toString());//假日周次 15
						vecv.add(vect.get(i+15).toString());//授课周数 16
						vecv.add(vect.get(i+16).toString());//场地名称 17
						vecv.add(vect.get(i+17).toString());//考试形式 18
						vecv.add(vect.get(i+18).toString());//时间序列 19
						
						for(int j=19;j<vect.size();j=j+19){
							//教师相同
							if(vect.get(i+2).toString().equals(vect.get(j+2).toString())){
								vecv.add(vect.get(j).toString());//授课计划明细编号 1
								vecv.add(vect.get(j+1).toString());//学年学期编码 2
								vecv.add(vect.get(j+2).toString());//授课教师编号 3
								vecv.add(vect.get(j+3).toString());//授课教师姓名 4
								vecv.add(vect.get(j+4).toString());//层级名称 5
								vecv.add(vect.get(j+5).toString());//专业名称 6
								vecv.add(vect.get(j+6).toString());//课程代码 7
								vecv.add(vect.get(j+7).toString());//课程名称 8
								vecv.add(vect.get(j+8).toString());//行政班代码 9
								vecv.add(vect.get(j+9).toString());//行政班名称 10
								vecv.add(vect.get(j+10).toString());//每周节数（时间序列） 11
								vecv.add(vect.get(j+11).toString());//总人数 12
								vecv.add(vect.get(j+12).toString());//系数 13		
								vecv.add(vect.get(j+13).toString());//授课周次详情 14
								vecv.add(vect.get(j+14).toString());//假日周次 15
								vecv.add(vect.get(j+15).toString());//授课周数 16
								vecv.add(vect.get(j+16).toString());//场地名称 17
								vecv.add(vect.get(j+17).toString());//考试形式 18
								vecv.add(vect.get(j+18).toString());//时间序列 19
								
								for(int k=0;k<19;k++){
									vect.remove(j);
								}
								j=j-19;
							}
						}
						//System.out.println(vecs.toString()+"|"+i);
				
						for(int k=0;k<19;k++){
							vect.remove(i);
						}
					}
				
					//按层级排序
					for(int i=0;vecv.size()>1;){
						vecu.add(vecv.get(i).toString());//授课计划明细编号 1
						vecu.add(vecv.get(i+1).toString());//学年学期编码 2
						vecu.add(vecv.get(i+2).toString());//授课教师编号 3
						vecu.add(vecv.get(i+3).toString());//授课教师姓名 4
						vecu.add(vecv.get(i+4).toString());//层级名称 5
						vecu.add(vecv.get(i+5).toString());//专业名称 6
						vecu.add(vecv.get(i+6).toString());//课程代码 7
						vecu.add(vecv.get(i+7).toString());//课程名称 8
						vecu.add(vecv.get(i+8).toString());//行政班代码 9
						vecu.add(vecv.get(i+9).toString());//行政班名称 10
						vecu.add(vecv.get(i+10).toString());//每周节数（时间序列） 11
						vecu.add(vecv.get(i+11).toString());//总人数 12
						vecu.add(vecv.get(i+12).toString());//系数 13		
						vecu.add(vecv.get(i+13).toString());//授课周次详情 14
						vecu.add(vecv.get(i+14).toString());//假日周次 15
						vecu.add(vecv.get(i+15).toString());//授课周数 16
						vecu.add(vecv.get(i+16).toString());//场地名称 17
						vecu.add(vecv.get(i+17).toString());//考试形式 18
						vecu.add(vecv.get(i+18).toString());//时间序列 19
						
						for(int j=19;j<vecv.size();j=j+19){
							//教师相同
							if(vecv.get(i+4).toString().equals(vecv.get(j+4).toString())){
								vecu.add(vecv.get(j).toString());//授课计划明细编号 1
								vecu.add(vecv.get(j+1).toString());//学年学期编码 2
								vecu.add(vecv.get(j+2).toString());//授课教师编号 3
								vecu.add(vecv.get(j+3).toString());//授课教师姓名 4
								vecu.add(vecv.get(j+4).toString());//层级名称 5
								vecu.add(vecv.get(j+5).toString());//专业名称 6
								vecu.add(vecv.get(j+6).toString());//课程代码 7
								vecu.add(vecv.get(j+7).toString());//课程名称 8
								vecu.add(vecv.get(j+8).toString());//行政班代码 9
								vecu.add(vecv.get(j+9).toString());//行政班名称 10
								vecu.add(vecv.get(j+10).toString());//每周节数（时间序列） 11
								vecu.add(vecv.get(j+11).toString());//总人数 12
								vecu.add(vecv.get(j+12).toString());//系数 13		
								vecu.add(vecv.get(j+13).toString());//授课周次详情 14
								vecu.add(vecv.get(j+14).toString());//假日周次 15
								vecu.add(vecv.get(j+15).toString());//授课周数 16
								vecu.add(vecv.get(j+16).toString());//场地名称 17
								vecu.add(vecv.get(j+17).toString());//考试形式 18
								vecu.add(vecv.get(j+18).toString());//时间序列 19
								
								for(int k=0;k<19;k++){
									vecv.remove(j);
								}
								j=j-19;
							}
						}
						//System.out.println(vecs.toString()+"|"+i);
				
						for(int k=0;k<19;k++){
							vecv.remove(i);
						}
					}
					
//		for(int i=0;i<vecu.size();i=i+19){
//			System.out.println("u授课计划明细编号:"+vecu.get(i).toString()+","+"学年学期编码:"+vecu.get(i+1).toString()+","+"授课教师编号:"+vecu.get(i+2).toString()+","+"授课教师姓名:"+vecu.get(i+3).toString()+","+"层级名称:"+vecu.get(i+4).toString()+","+
//				"专业名称:"+vecu.get(i+5).toString()+","+"课程代码:"+vecu.get(i+6).toString()+","+"课程名称:"+vecu.get(i+7).toString()+","+"行政班代码:"+vecu.get(i+8).toString()+","+"行政班名称:"+vecu.get(i+9).toString()+","+"每周节数:"+vecu.get(i+10).toString()+","+"总人数:"+vecu.get(i+11).toString()+","+
//				"系数:"+vecu.get(i+12).toString()+","+"授课周次详情:"+vecu.get(i+13).toString()+","+"假日周次:"+vecu.get(i+14).toString()+","+"授课周数:"+vecu.get(i+15).toString()+"," );
//		}
		
		for(int colNum=1; colNum<14; colNum++){
			for(int rowNum=1; rowNum<3; rowNum++){
				//生成标题
				if(colNum==1 && rowNum==1){
					wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[12]+"1").merge();
					cell = wb.openSheet("Sheet1").openCell("A1");
					cellContent = schoolName + "   教师授课情况统计";
						
					//maxWidth = 4*cellContent.length()/(vector.size()+1);
					cell.setValue(cellContent);
				}else{
					//第二行标题
					if(rowNum==2){
						cell = wb.openSheet("Sheet1").openCell(colName[colNum-1]+rowNum);//当前单元格
						if(colNum==1){
							cellContent="学年学期";
						}else if(colNum==2){
							cellContent="教师工号";
						}else if(colNum==3){
							cellContent="教师姓名";
						}else if(colNum==4){
							cellContent="所属专业";
						}else if(colNum==5){
							cellContent="课程代码";
						}else if(colNum==6){
							cellContent="课程名称";
						}else if(colNum==7){
							cellContent="考试形式";
						}else if(colNum==8){
							cellContent="班级名称";
						}else if(colNum==9){
							cellContent="班级人数";
						}else if(colNum==10){
							cellContent="星期";
						}else if(colNum==11){
							cellContent="授课节数";
						}else if(colNum==12){
							cellContent="周数";
						}else if(colNum==13){
							cellContent="教室";
						}
						
						cell.setValue(cellContent);	
					}
				}
			}
		}
		
				
		double sumhour=0;
		double sumavg=0;
		mergeNum = 0;
		if(vecu!=null&&vecu.size()>0){
			for(int i=0;i<vecu.size();i=i+19){
				tempYeaTERM = MyTools.StrFiltr(vecu.get(i+1));//学年学期
				tempTeaCode = MyTools.StrFiltr(vecu.get(i+2));//教师工号
				tempTeaName = MyTools.StrFiltr(vecu.get(i+3));//教师姓名
				tempTeaTYPE = MyTools.StrFiltr(vecu.get(i+4));//教师类别
				tempMajNAME = MyTools.StrFiltr(vecu.get(i+5));//所属专业
				tempCouNUMB = MyTools.StrFiltr(vecu.get(i+6));//课程代码
				tempCouNAME = MyTools.StrFiltr(vecu.get(i+7));//课程名称
				tempExaFORM = MyTools.StrFiltr(vecu.get(i+17));//考试形式
				tempClsNAME = MyTools.StrFiltr(vecu.get(i+9));//班级名称
				tempClsNUMB = MyTools.StrFiltr(vecu.get(i+11));//班级人数
				tempWeekDAY = MyTools.StrFiltr(vecu.get(i+18));//星期
				tempWekNUMB = MyTools.StrFiltr(vecu.get(i+15));//周数
				tempTeaCLAS = MyTools.StrFiltr(vecu.get(i+16));//教室
					
				cell = wb.openSheet("Sheet1").openCell(colName[0]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(tempYeaTERM.substring(0,4)+"学年第"+tempYeaTERM.substring(4,5)+"学期");
				for(int j=1;j<3;j++){
					cell = wb.openSheet("Sheet1").openCell(colName[j]+(i/19+3+mergeNum));//当前单元格
					cell.setValue(MyTools.StrFiltr(vecu.get(i+1+j)));
				}
				
				cell = wb.openSheet("Sheet1").openCell(colName[3]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr("'"+vecu.get(i+5).toString()));
				cell = wb.openSheet("Sheet1").openCell(colName[4]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr("'"+vecu.get(i+6).toString()));
				cell = wb.openSheet("Sheet1").openCell(colName[5]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr("'"+vecu.get(i+7).toString()));
				cell = wb.openSheet("Sheet1").openCell(colName[6]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr("'"+vecu.get(i+17).toString()));
				
				cell = wb.openSheet("Sheet1").openCell(colName[7]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr(vecu.get(i+9)));
				cell = wb.openSheet("Sheet1").openCell(colName[8]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr(vecu.get(i+11)));
			
				String sjxl="";
				if(tempWeekDAY.equals("01")){
					sjxl="星期一";
				}else if(tempWeekDAY.equals("02")){
					sjxl="星期二";
				}else if(tempWeekDAY.equals("03")){
					sjxl="星期三";
				}else if(tempWeekDAY.equals("04")){
					sjxl="星期四";
				}else if(tempWeekDAY.equals("05")){
					sjxl="星期五";
				}
				cell = wb.openSheet("Sheet1").openCell(colName[9]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(sjxl);
				
				double xs=Double.parseDouble(vecu.get(i+12).toString());
						
				
				int zs=0;
				int ce=0;
//				if(vecu.get(i+4).toString().equals("外聘任课教师")){
					zs=Integer.parseInt(vecu.get(i+15).toString())-Integer.parseInt(vecu.get(i+14).toString());
//				}else{
//					if(tempYeaTERM.substring(2,4).equals(tempClsNAME.substring(0,2))&&tempYeaTERM.substring(4,5).equals("1")){//是第一学期
//						if(weeknum.equals("18")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==8){
//								if(vecu.get(i+13).toString().equals("2-9")){
//									zs=9;
//								}else if(vecu.get(i+13).toString().equals("10-18")){
//									zs=10;
//								}else{
//									zs=Integer.parseInt(vecu.get(i+15).toString());
//								}
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=14;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==17){
//								zs=19;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("17")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==7||Integer.parseInt(vecu.get(i+15).toString())==8){
//								if(vecu.get(i+13).toString().equals("2-9")){
//									zs=9;
//								}else if(vecu.get(i+13).toString().equals("10-17")){
//									zs=10;
//								}else{
//									zs=Integer.parseInt(vecu.get(i+15).toString());
//								}
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==12||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=14;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==16||Integer.parseInt(vecu.get(i+15).toString())==17){
//								zs=19;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("19")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==8){
//								zs=9;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=14;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19){
//								zs=19;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}
//						
//					}else{
//						if(weeknum.equals("18")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==10){
//								zs=10;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==15){
//								zs=15;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==20){
//								zs=20;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("17")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==8){
//								zs=10;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==13){
//								zs=15;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==20||Integer.parseInt(vecu.get(i+15).toString())==17){
//								zs=20;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}else if(weeknum.equals("19")){
//							if(Integer.parseInt(vecu.get(i+15).toString())==9||Integer.parseInt(vecu.get(i+15).toString())==10){
//								zs=10;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==14||Integer.parseInt(vecu.get(i+15).toString())==15){
//								zs=15;
//							}else if(Integer.parseInt(vecu.get(i+15).toString())==18||Integer.parseInt(vecu.get(i+15).toString())==19||Integer.parseInt(vecu.get(i+15).toString())==20){
//								zs=20;
//							}else{
//								zs=Integer.parseInt(vecu.get(i+15).toString());
//								ce=1;
//							}
//						}
//						
//					}
//				}
				
				if(ce==1){
					for(int c=0;c<14;c++){
						cell = wb.openSheet("Sheet1").openCell(colName[c]+(i/19+3+mergeNum));//当前单元格
						cell.setForeColor(Color.red);
					}
				}else{
					for(int c=0;c<14;c++){
						cell = wb.openSheet("Sheet1").openCell(colName[c]+(i/19+3+mergeNum));//当前单元格
						cell.setForeColor(Color.black);
					}
				}
				
				//System.out.println(vec4.get(i+16).toString());//授课计划明细编号
//				Vector vecmx=null;
//				String sqlmx="SELECT distinct 授课计划明细编号 FROM dbo.V_调课管理_调课信息主表  where 审核状态='2' and (原计划授课周次='' or 调整后授课周次='') ";
//				vecmx=db.GetContextVector(sqlmx);
//				if(vecmx!=null&&vecmx.size()>0){
//					for(int mx=0;mx<vecmx.size();mx++){
//						if(vecmx.get(mx).toString().equals(vecu.get(i+16).toString())){//这条授课计划有停课或补课
//							for(int c=0;c<14;c++){
//								cell = wb.openSheet("Sheet1").openCell(colName[c]+(i/17+3+mergeNum));//当前单元格
//								cell.setForeColor(Color.orange);
//							}
//						}
//					}
//				}
				
				double zks=0;
				double zzs=0;
				if(vecu.get(i+4).toString().equals("外聘任课教师")){
					zks=(Integer.parseInt(vecu.get(i+10).toString())*zs)*xs;
					zzs=Double.parseDouble(weeknum);
				}else{
					zks=Integer.parseInt(vecu.get(i+10).toString())*zs*xs;
					zzs=20;
				}
				cell = wb.openSheet("Sheet1").openCell(colName[10]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(vecu.get(i+10).toString());
								
				cell = wb.openSheet("Sheet1").openCell(colName[11]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(zs+"");
				
				cell = wb.openSheet("Sheet1").openCell(colName[12]+(i/19+3+mergeNum));//当前单元格
				cell.setValue(MyTools.StrFiltr(vecu.get(i+16)));
				
				
				//判断单元格是否可以合并
//				if((i+13)<vec4.size()){						
//					if(vec4.get(i+1).toString().equals(vec4.get(i+1+13).toString())){//教师编号相同
//						sumhour+=zks;
//						sumavg+=Double.parseDouble(kss);
//					}else{
//						//合并单元格
//						sumhour+=zks;
//						sumavg+=Double.parseDouble(kss);
//						mergeNum++;
//						wb.openSheet("Sheet1").openTable(colName[0]+(i/13+3+mergeNum)+":"+colName[9]+(i/13+3+mergeNum)).merge();
//						cell = wb.openSheet("Sheet1").openCell(colName[0]+(i/13+3+mergeNum));//当前单元格
//						cell.setValue("小计");
//						cell.setBackColor(Color.decode("#CDFFCD"));
//						cell = wb.openSheet("Sheet1").openCell(colName[10]+(i/13+3+mergeNum));//当前单元格
//						cell.setValue(sumhour+"");	
//						cell.setBackColor(Color.decode("#CDFFCD"));
//						cell = wb.openSheet("Sheet1").openCell(colName[11]+(i/13+3+mergeNum));//当前单元格
//						cell.setValue(sumavg+"");
//						cell.setBackColor(Color.decode("#CDFFCD"));
//						sumhour=0;
//						sumavg=0;
//					}
//				}else{ 
//					if((i+13)==vec4.size()){//最后一行
//						//if(vec3.get(i+1).toString().equals(vec3.get(i+1-12).toString())){//和上一行相同
//							sumhour+=zks;
//							sumavg+=Double.parseDouble(kss);
//							mergeNum++;
//							wb.openSheet("Sheet1").openTable(colName[0]+(i/13+3+mergeNum)+":"+colName[9]+(i/13+3+mergeNum)).merge();
//							cell = wb.openSheet("Sheet1").openCell(colName[0]+(i/13+3+mergeNum));//当前单元格
//							cell.setValue("小计");	
//							cell.setBackColor(Color.decode("#CDFFCD"));
//							cell = wb.openSheet("Sheet1").openCell(colName[10]+(i/13+3+mergeNum));//当前单元格
//							cell.setValue(sumhour+"");	
//							cell.setBackColor(Color.decode("#CDFFCD"));
//							cell = wb.openSheet("Sheet1").openCell(colName[11]+(i/13+3+mergeNum));//当前单元格
//							cell.setValue(sumavg+"");	
//							cell.setBackColor(Color.decode("#CDFFCD"));
//							sumhour=0;
//							sumavg=0;
//						//}
//					}
//				}
				
					
			}
		}
		
		int zjs=vecu.size()/19;//行数
			
		//设置单元格的水平对齐方式
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[12]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[12]+(zjs+2+mergeNum)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);

		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);

		//设置课表边框线
		Border border = wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[12]+(zjs+2+mergeNum)).getBorder();
		//设置表格边框的宽度、颜色
		border.setWeight(XlBorderWeight.xlThin);
		border.setLineColor(Color.black);
		
		//设置标题字体大小
		cell = wb.openSheet("Sheet1").openCell("A1");
		cell.getFont().setBold(true);
		fontSize = 18;
		cell.getFont().setSize(fontSize);
		
		//设置课表标题行列字体大小
		fontSize = 12;
		wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[12]+"2").getFont().setSize(fontSize);
		//wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[0]+(zjs+1)).getFont().setSize(fontSize);
		//设置课表内容行列字体大小
		fontSize = 10;
		wb.openSheet("Sheet1").openTable(colName[0]+"3:"+colName[12]+(zjs+2+mergeNum)).getFont().setSize(fontSize);
		
		//设置表格列宽
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(16);
		wb.openSheet("Sheet1").openTable(colName[1]+"1:"+colName[12]+"1").setColumnWidth(10);
		wb.openSheet("Sheet1").openTable(colName[2]+"1:"+colName[2]+"1").setColumnWidth(12);
		wb.openSheet("Sheet1").openTable(colName[3]+"1:"+colName[5]+"1").setColumnWidth(20);
		wb.openSheet("Sheet1").openTable(colName[7]+"1:"+colName[7]+"1").setColumnWidth(30);
		wb.openSheet("Sheet1").openTable(colName[12]+"1:"+colName[12]+"1").setColumnWidth(20);
		//设置表格行高
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[12]+"2").setRowHeight(40);
		wb.openSheet("Sheet1").openTable(colName[0]+"3:"+colName[12]+(zjs+2+mergeNum)).setRowHeight(30);

		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "template.xls";

		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("下载", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "   教师授课计划统计");
		poCtrl1.setFileTitle(schoolName + "   教师授课计划统计");//设置另存为窗口默认文件名
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
	}
	
	
	/**
	 * 考试安排表导出
	 * @date:2016-06-24
	 * @author:lupengfei
	 * @param xnxq+jxxz 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	public static void exportExamSchedule(HttpServletRequest request, String xnxqbm, String kszq) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String xnxqmc = "";//学年学期名称
		int mzts = 0;//每周天数
		int sw = 0;//上午节数
		int zw = 0;//中午节数
		int xw = 0;//下午节数
		int ws = 0;//晚上节数
		int zjs = 0;//总节数
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 40;
		float fontSize = 0;
		String skzq="";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String sqlday=" select convert(varchar(10),考试日期,21) as [考试日期] from [V_考试管理_考场安排主表] where [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [考试周期]='"+MyTools.fixSql(kszq)+"' ";
		Vector vecday=db.GetContextVector(sqlday);

		String xqkssj=vecday.get(0).toString();	
		try {
			cal.setTime(format.parse(xqkssj));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//考试日期是星期几
		int week1 = cal.get(Calendar.DAY_OF_WEEK)-1;
		//将考试日期后2天加入
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String str = xqkssj; 
		Date dt=sdf.parse(str,new ParsePosition(0)); 
		Calendar rightNow = Calendar.getInstance(); 
	    rightNow.setTime(dt); 
		//rightNow.add(Calendar.DATE,1);//你要加减的日期   
		//rightNow.add(Calendar.DATE,2);//你要加减的日期   
		Date dt1=rightNow.getTime(); 
		String reStr1=sdf.format(dt1); 
		
		rightNow.add(Calendar.DATE,-1);//你要加减的日期 
		Date dt5=rightNow.getTime(); 
		String reStr5=sdf.format(dt5);
		try {
			cal.setTime(format.parse(reStr5));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week5=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,2);//你要加减的日期 
		Date dt2=rightNow.getTime(); 
		String reStr2=sdf.format(dt2); 
		try {
			cal.setTime(format.parse(reStr2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week2=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt3=rightNow.getTime(); 
		String reStr3=sdf.format(dt3);
		try {
			cal.setTime(format.parse(reStr3));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week3=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt4=rightNow.getTime(); 
		String reStr4=sdf.format(dt4);
		try {
			cal.setTime(format.parse(reStr4));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week4=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt6=rightNow.getTime(); 
		String reStr6=sdf.format(dt6);
		try {
			cal.setTime(format.parse(reStr6));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week6=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt7=rightNow.getTime(); 
		String reStr7=sdf.format(dt7);
		try {
			cal.setTime(format.parse(reStr7));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week7=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		System.out.println("reStr:--"+reStr1+","+week1+"|"+reStr2+","+week2+"|"+reStr3+","+week3);
		String weekday1="";
		String weekday2="";
		String weekday3="";
		String weekday4="";
		String weekday5="";
		String weekday6="";
		String weekday7="";
		if(week1==1){
			weekday1="一";
		}else if(week1==2){
			weekday1="二";
		}else if(week1==3){
			weekday1="三";
		}else if(week1==4){
			weekday1="四";
		}else if(week1==5){
			weekday1="五";
		}else if(week1==6){
			weekday1="六";
		}else if(week1==0){
			weekday1="日";
		}
		
		if(week2==1){
			weekday2="一";
		}else if(week2==2){
			weekday2="二";
		}else if(week2==3){
			weekday2="三";
		}else if(week2==4){
			weekday2="四";
		}else if(week2==5){
			weekday2="五";
		}else if(week2==6){
			weekday2="六";
		}else if(week2==0){
			weekday2="日";
		}
		
		if(week3==1){
			weekday3="一";
		}else if(week3==2){
			weekday3="二";
		}else if(week3==3){
			weekday3="三";
		}else if(week3==4){
			weekday3="四";
		}else if(week3==5){
			weekday3="五";
		}else if(week3==6){
			weekday3="六";
		}else if(week3==0){
			weekday3="日";
		}
		
		if(week4==1){
			weekday4="一";
		}else if(week4==2){
			weekday4="二";
		}else if(week4==3){
			weekday4="三";
		}else if(week4==4){
			weekday4="四";
		}else if(week4==5){
			weekday4="五";
		}else if(week4==6){
			weekday4="六";
		}else if(week4==0){
			weekday4="日";
		}
		
		if(week5==1){
			weekday5="一";
		}else if(week5==2){
			weekday5="二";
		}else if(week5==3){
			weekday5="三";
		}else if(week5==4){
			weekday5="四";
		}else if(week5==5){
			weekday5="五";
		}else if(week5==6){
			weekday5="六";
		}else if(week5==0){
			weekday5="日";
		}
		
		if(week6==1){
			weekday6="一";
		}else if(week6==2){
			weekday6="二";
		}else if(week6==3){
			weekday6="三";
		}else if(week6==4){
			weekday6="四";
		}else if(week6==5){
			weekday6="五";
		}else if(week6==6){
			weekday6="六";
		}else if(week6==0){
			weekday6="日";
		}
		
		if(week7==1){
			weekday7="一";
		}else if(week7==2){
			weekday7="二";
		}else if(week7==3){
			weekday7="三";
		}else if(week7==4){
			weekday7="四";
		}else if(week7==5){
			weekday7="五";
		}else if(week7==6){
			weekday7="六";
		}else if(week5==0){
			weekday7="日";
		}
		
		Workbook wb = new Workbook();

		Cell cell1;
		Cell cell2;
		Cell cell3;
		Cell celln;
		String cellContent = ""; //当前单元格的内容
		Vector tempVec = new Vector();
		Vector hbSetVec = new Vector();
		Vector timeVec = null;
		
		String tempOrder = "";//时间序列
		int tempIndex = -1;
		int mergeNum = 0;//单元格合并数
		boolean flag = true;//用于判断是否可合并
		
		String tempSkjhmxbh = "";//授课计划明细编号
		String tempSkjhzbbh = "";//授课计划明细编号
		String timeOrder = "";//时间序列
		String tempTeaCode = "";//教师编号
		String tempTeaName = "";//教师姓名
		String tempSiteCode = "";//场地编号
		String tempSkzc = "";//授课周次
		
		String tempShiChang="";//试场
		String tempCourseName = "";//课程名称
		String tempClassName = "";//班级名称
		String tempClassNum="";//班级人数
		String tempCourseCycle="";//上课周期
		String tempQizQimo="";//期中期末
		String tempExamType="";//考试形式
		String tempSiteName = "";//场地名称
		String tempInvigilate="";//监考教师
		
		
		String skjhmxbhArray[] = new String[0];
		
		Vector vector = null;
		Vector vec = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		String classroom="";
		String timedata="";
		
		String sqlzb="select [考场安排主表编号] from V_考试管理_考场安排主表 where [学年学期编码]='"+xnxqbm+"' and [考试周期]='"+kszq+"' ";
		Vector veczb=db.GetContextVector(sqlzb);
		if(veczb!=null&&veczb.size()>0){
			tempSkjhzbbh=veczb.get(0).toString();
		}
		
		
	if(kszq.equals("3")){//期末
		skzq="期末";
		String name1=reStr1.split("-")[1]+"月"+reStr1.split("-")[2]+"日(周"+weekday1+")";
		String name2=reStr2.split("-")[1]+"月"+reStr2.split("-")[2]+"日(周"+weekday2+")";
		String name3=reStr3.split("-")[1]+"月"+reStr3.split("-")[2]+"日(周"+weekday3+")";

		Sheet st1=wb.createSheet(name1,SheetInsertType.Before,"sheet1");
		Sheet st2=wb.createSheet(name2,SheetInsertType.Before,"sheet1");
		Sheet st3=wb.createSheet(name3,SheetInsertType.Before,"sheet1");
		
		sql=" SELECT ROW_NUMBER() over(order by a.[课程名称],b.[行政班简称]) as 试场,a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b where a.行政班代码=b.行政班代码 and a.考场安排主表编号='"+tempSkjhzbbh+"' and [考试形式]='5' " +
			" union " +
			" SELECT ROW_NUMBER() over(order by a.[课程名称]) as 试场,a.[时间序列],a.[课程名称],'' as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a where a.专业代码='' and a.考场安排主表编号='"+tempSkjhzbbh+"' and [考试形式]='5' ";
		vector = db.GetContextVector(sql);
		
		for(int colNum=1; colNum<22; colNum++){//21列
			for(int rowNum=1; rowNum<4; rowNum++){//1-3行
				//生成标题
				if(colNum==1 && rowNum==1){
					st1.openTable(colName[0]+"1:"+colName[20]+"1").merge();
					cell1 = st1.openCell("A1");
					st2.openTable(colName[0]+"1:"+colName[20]+"1").merge();
					cell2 = st2.openCell("A1");
					st3.openTable(colName[0]+"1:"+colName[20]+"1").merge();
					cell3 = st3.openCell("A1");
					if(kszq.equals("1-9")){
						skzq="前十周";
					}else if(kszq.equals("1-14")){
						skzq="前十五周";
					}else if(kszq.equals("1-18")){
						skzq="期末考试";
					}
					cellContent = schoolName + " "+xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期 "+skzq+"考试安排表";
						
					//maxWidth = 4*cellContent.length()/(vector.size()+1);
					cell1.setValue(cellContent);
					cell2.setValue(cellContent);
					cell3.setValue(cellContent);
				}else if(rowNum==2){
					//第二行标题
					
						st1.openTable(colName[0]+"2:"+colName[6]+"2").merge();
						cell1 = st1.openCell("A2");//当前单元格
						cellContent=name1+"09:00-11:00";
						cell1.setValue(cellContent);
						st1.openTable(colName[7]+"2:"+colName[13]+"2").merge();
						cell1 = st1.openCell("H2");//当前单元格
						cellContent=name1+"12:45-14:45";
						cell1.setValue(cellContent);
						st1.openTable(colName[14]+"2:"+colName[20]+"2").merge();
						cell1 = st1.openCell("O2");//当前单元格
						cellContent=name1+"14:45-16:45";
						cell1.setValue(cellContent);
						
						st2.openTable(colName[0]+"2:"+colName[6]+"2").merge();
						cell2 = st2.openCell("A2");//当前单元格
						cellContent=name2+"09:00-11:00";
						cell2.setValue(cellContent);
						st2.openTable(colName[7]+"2:"+colName[13]+"2").merge();
						cell2 = st2.openCell("H2");//当前单元格
						cellContent=name2+"12:45-14:45";
						cell2.setValue(cellContent);
						st2.openTable(colName[14]+"2:"+colName[20]+"2").merge();
						cell2 = st2.openCell("O2");//当前单元格
						cellContent=name2+"14:45-16:45";
						cell2.setValue(cellContent);
						
						st3.openTable(colName[0]+"2:"+colName[6]+"2").merge();
						cell3 = st3.openCell("A2");//当前单元格
						cellContent=name3+"09:00-11:00";
						cell3.setValue(cellContent);
						st3.openTable(colName[7]+"2:"+colName[13]+"2").merge();
						cell3 = st3.openCell("H2");//当前单元格
						cellContent=name3+"12:45-14:45";
						cell3.setValue(cellContent);
						st3.openTable(colName[14]+"2:"+colName[20]+"2").merge();
						cell3 = st3.openCell("O2");//当前单元格
						cellContent=name3+"14:45-16:45";
						cell3.setValue(cellContent);
						
							
					
				}else if(rowNum==3){
					st1.openTable(colName[5]+"3:"+colName[6]+"3").merge();
					st1.openTable(colName[12]+"3:"+colName[13]+"3").merge();
					st1.openTable(colName[19]+"3:"+colName[20]+"3").merge();
					st2.openTable(colName[5]+"3:"+colName[6]+"3").merge();
					st2.openTable(colName[12]+"3:"+colName[13]+"3").merge();
					st2.openTable(colName[19]+"3:"+colName[20]+"3").merge();
					st3.openTable(colName[5]+"3:"+colName[6]+"3").merge();
					st3.openTable(colName[12]+"3:"+colName[13]+"3").merge();
					st3.openTable(colName[19]+"3:"+colName[20]+"3").merge();
					cell1 = st1.openCell(colName[colNum-1]+rowNum);//当前单元格
					cell2 = st2.openCell(colName[colNum-1]+rowNum);//当前单元格
					cell3 = st3.openCell(colName[colNum-1]+rowNum);//当前单元格
					
					if(colNum==1||colNum==8||colNum==15){
						cellContent="试场";
					}else if(colNum==2||colNum==9||colNum==16){
						cellContent="科目";
					}else if(colNum==3||colNum==10||colNum==17){
						cellContent="班级";
					}else if(colNum==4||colNum==11||colNum==18){
						cellContent="人数";
					}else if(colNum==5||colNum==12||colNum==19){
						cellContent="教室";
					}else if(colNum==6||colNum==13||colNum==20){
						cellContent="监考";
					}
					
					cell1.setValue(cellContent);
					cell2.setValue(cellContent);
					cell3.setValue(cellContent);
				}
			}
		}
		
		int shichang=1;
		mergeNum = 0;
		int[] line=new int[9];
		for(int l=0;l<9;l++){
			line[l]=1;
		}
		
		if(vector!=null&&vector.size()>0){
			for(int i=0;i<vector.size();i=i+7){
				if(vector.get(i+1).toString().equals("0101")){
					for(int j=0;j<7;j++){
						cell1 = st1.openCell(colName[j]+(line[0]+3));//当前单元格
						if(j==0){
							cellContent=line[0]+"";
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell1.setValue(cellContent);
					}
					line[0]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell1 = st1.openCell(colName[j]+(line[0]+3));//当前单元格
							if(j==0){
								cellContent=line[0]+"";
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell1.setValue(cellContent);
						}
						line[0]++;
					}
				}else if(vector.get(i+1).toString().equals("0102")){
					for(int j=0;j<7;j++){
						cell1 = st1.openCell(colName[j+7]+(line[1]+3));//当前单元格
						if(j==0){
							cellContent=line[1]+"";
							cell1.setValue(cellContent);
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
							cell1.setValue(cellContent);
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell1.setValue(cellContent);
					}
					line[1]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell1 = st1.openCell(colName[j+7]+(line[1]+3));//当前单元格
							if(j==0){
								cellContent=line[1]+"";
								cell1.setValue(cellContent);
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
								cell1.setValue(cellContent);
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell1.setValue(cellContent);
						}
						line[1]++;
					}
				}else if(vector.get(i+1).toString().equals("0103")){
					for(int j=0;j<7;j++){
						cell1 = st1.openCell(colName[j+14]+(line[2]+3));//当前单元格
						if(j==0){
							cellContent=line[2]+"";
							cell1.setValue(cellContent);
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
							cell1.setValue(cellContent);
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell1.setValue(cellContent);
					}
					line[2]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell1 = st1.openCell(colName[j+14]+(line[2]+3));//当前单元格
							if(j==0){
								cellContent=line[2]+"";
								cell1.setValue(cellContent);
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
								cell1.setValue(cellContent);
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell1.setValue(cellContent);
						}
						line[2]++;
					}
				}else if(vector.get(i+1).toString().equals("0201")){
					for(int j=0;j<7;j++){
						cell2 = st2.openCell(colName[j]+(line[3]+3));//当前单元格
						if(j==0){
							cellContent=line[3]+"";
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell2.setValue(cellContent);			
					}
					line[3]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell2 = st2.openCell(colName[j]+(line[3]+3));//当前单元格
							if(j==0){
								cellContent=line[3]+"";
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell2.setValue(cellContent);			
						}
						line[3]++;	
					}
				}else if(vector.get(i+1).toString().equals("0202")){
					for(int j=0;j<7;j++){
						cell2 = st2.openCell(colName[j+7]+(line[4]+3));//当前单元格
						if(j==0){
							cellContent=line[4]+"";
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell2.setValue(cellContent);
					}
					line[4]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell2 = st2.openCell(colName[j+7]+(line[4]+3));//当前单元格
							if(j==0){
								cellContent=line[4]+"";
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell2.setValue(cellContent);
						}
						line[4]++;
					}
				}else if(vector.get(i+1).toString().equals("0203")){
					for(int j=0;j<7;j++){
						cell2 = st2.openCell(colName[j+14]+(line[5]+3));//当前单元格
						if(j==0){
							cellContent=line[5]+"";
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell2.setValue(cellContent);
					}
					line[5]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell2 = st2.openCell(colName[j+14]+(line[5]+3));//当前单元格
							if(j==0){
								cellContent=line[5]+"";
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell2.setValue(cellContent);
						}
						line[5]++;
					}
				}else if(vector.get(i+1).toString().equals("0301")){
					for(int j=0;j<7;j++){
						cell3 = st3.openCell(colName[j]+(line[6]+3));//当前单元格
						if(j==0){
							cellContent=line[6]+"";
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell3.setValue(cellContent);
					}
					line[6]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell3 = st3.openCell(colName[j]+(line[6]+3));//当前单元格
							if(j==0){
								cellContent=line[6]+"";
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell3.setValue(cellContent);
						}
						line[6]++;
					}
				}else if(vector.get(i+1).toString().equals("0302")){
					for(int j=0;j<7;j++){
						cell3 = st3.openCell(colName[j+7]+(line[7]+3));//当前单元格
						if(j==0){
							cellContent=line[7]+"";
						}else if(j>0&&j<4){
							cellContent=vector.get(i+j+1).toString();
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell3.setValue(cellContent);
					}
					line[7]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell3 = st3.openCell(colName[j+7]+(line[7]+3));//当前单元格
							if(j==0){
								cellContent=line[7]+"";
							}else if(j>0&&j<4){
								cellContent=vector.get(i+j+1).toString();
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell3.setValue(cellContent);
						}
						line[7]++;
					}
				}else if(vector.get(i+1).toString().equals("0303")){
					for(int j=0;j<7;j++){
						cell3 = st3.openCell(colName[j+14]+(line[8]+3));//当前单元格
						if(j==0){
							cellContent=line[8]+"";
						}else if(j>0&&j<4){
							String xsrs="";
							if((j+1)==4){
								if(vector.get(i+j+1).toString().equals("0")){
									xsrs="";
								}else{
									xsrs=vector.get(i+j+1).toString();
								}
								cellContent=xsrs;
							}else{
								cellContent=vector.get(i+j+1).toString();
							}	
						}else if(j==4){
							if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
								cellContent=vector.get(i+5).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+5).toString();
							}
						}else if(j==5){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell3.setValue(cellContent);
					}
					line[8]++;
					if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
						for(int j=0;j<7;j++){
							cell3 = st3.openCell(colName[j+14]+(line[8]+3));//当前单元格
							if(j==0){
								cellContent=line[8]+"";
							}else if(j>0&&j<4){
								String xsrs="";
								if((j+1)==4){
									if(vector.get(i+j+1).toString().equals("0")){
										xsrs="";
									}else{
										xsrs=vector.get(i+j+1).toString();
									}
									cellContent=xsrs;
								}else{
									cellContent=vector.get(i+j+1).toString();
								}	
							}else if(j==4){
								if(vector.get(i+5).toString().indexOf(",")>0){//有分教室
									cellContent=vector.get(i+5).toString().split(",")[1];
								}else{
									cellContent=vector.get(i+5).toString();
								}
							}else if(j==5){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[2];
								}else{
									cellContent=vector.get(i+6).toString();
								}
							}else if(j==6){
								if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
									cellContent=vector.get(i+6).toString().split(",")[3];
								}else{
									cellContent="";
								}
							}
							cell3.setValue(cellContent);
						}
						line[8]++;
					}
				}
			}
		}
		
		//zjs=vector.size()/7;//行数
		int zhs1=0;
		int zhs2=0;
		int zhs3=0;
		if(line[0]>=zhs1){
			zhs1=line[0];
		}
		if(line[1]>=zhs1){
			zhs1=line[1];
		}
		if(line[2]>=zhs1){
			zhs1=line[2];
		}
		
		if(line[3]>=zhs2){
			zhs2=line[3];
		}
		if(line[4]>=zhs2){
			zhs2=line[4];
		}
		if(line[5]>=zhs2){
			zhs2=line[5];
		}
		
		if(line[6]>=zhs3){
			zhs3=line[6];
		}
		if(line[7]>=zhs3){
			zhs3=line[7];
		}
		if(line[8]>=zhs3){
			zhs3=line[8];
		}
		
		//设置单元格的水平对齐方式
		st1.openTable(colName[0]+"1:"+colName[20]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		st1.openTable(colName[0]+"2:"+colName[20]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		st1.openTable(colName[1]+"4:"+colName[1]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st1.openTable(colName[8]+"4:"+colName[8]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st1.openTable(colName[15]+"4:"+colName[15]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st2.openTable(colName[0]+"1:"+colName[20]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		st2.openTable(colName[0]+"2:"+colName[20]+(zhs2+3)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		st2.openTable(colName[1]+"4:"+colName[1]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st2.openTable(colName[8]+"2:"+colName[8]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st2.openTable(colName[15]+"4:"+colName[15]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st3.openTable(colName[0]+"1:"+colName[20]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		st3.openTable(colName[0]+"2:"+colName[20]+(zhs3+3)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		st3.openTable(colName[1]+"4:"+colName[1]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st3.openTable(colName[8]+"4:"+colName[8]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		st3.openTable(colName[15]+"4:"+colName[15]+(zhs1+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置课表边框线
		Border border1 = st1.openTable(colName[0]+"2:"+colName[20]+(zhs1+3)).getBorder();
		Border border2 = st2.openTable(colName[0]+"2:"+colName[20]+(zhs2+3)).getBorder();
		Border border3 = st3.openTable(colName[0]+"2:"+colName[20]+(zhs3+3)).getBorder();
		//设置表格边框的宽度、颜色
		border1.setWeight(XlBorderWeight.xlThin);
		border1.setLineColor(Color.black);
		border2.setWeight(XlBorderWeight.xlThin);
		border2.setLineColor(Color.black);
		border3.setWeight(XlBorderWeight.xlThin);
		border3.setLineColor(Color.black);
		
		//设置标题字体大小
		cell1 = st1.openCell("A1");
		cell1.getFont().setBold(true);
		fontSize = 20;
		cell1.getFont().setSize(fontSize);
		cell2 = st1.openCell("A1");
		cell2.getFont().setBold(true);
		cell2.getFont().setSize(fontSize);
		cell3 = st1.openCell("A1");
		cell3.getFont().setBold(true);
		cell3.getFont().setSize(fontSize);
		//设置课表标题行列字体大小
		fontSize = 12;
		st1.openTable(colName[0]+"1:"+colName[20]+(zhs1+3)).getFont().setBold(true);
		st1.openTable(colName[0]+"2:"+colName[20]+(zhs1+3)).getFont().setSize(fontSize);
		st2.openTable(colName[0]+"1:"+colName[20]+(zhs2+3)).getFont().setBold(true);
		st2.openTable(colName[0]+"2:"+colName[20]+(zhs2+3)).getFont().setSize(fontSize);
		st3.openTable(colName[0]+"1:"+colName[20]+(zhs3+3)).getFont().setBold(true);
		st3.openTable(colName[0]+"2:"+colName[20]+(zhs3+3)).getFont().setSize(fontSize);
		//设置课表内容行列字体大小
		//fontSize = 10;
		//st1.openTable(colName[1]+"3:"+colName[9]+(zjs+2)).getFont().setSize(fontSize);
		
		//设置表格列宽
		for(int s=0;s<21;s=s+7){
			st1.openTable(colName[s]+"1:"+colName[s]+"1").setColumnWidth(4);
			st1.openTable(colName[s+1]+"1:"+colName[s+1]+"1").setColumnWidth(17);
			st1.openTable(colName[s+2]+"1:"+colName[s+2]+"1").setColumnWidth(9);
			st1.openTable(colName[s+3]+"1:"+colName[s+3]+"1").setColumnWidth(5);
			st1.openTable(colName[s+4]+"1:"+colName[s+4]+"1").setColumnWidth(5);
			st1.openTable(colName[s+5]+"1:"+colName[s+5]+"1").setColumnWidth(8);
			st1.openTable(colName[s+6]+"1:"+colName[s+6]+"1").setColumnWidth(8);
		}
		for(int s=0;s<21;s=s+7){
			st2.openTable(colName[s]+"1:"+colName[s]+"1").setColumnWidth(4);
			st2.openTable(colName[s+1]+"1:"+colName[s+1]+"1").setColumnWidth(17);
			st2.openTable(colName[s+2]+"1:"+colName[s+2]+"1").setColumnWidth(9);
			st2.openTable(colName[s+3]+"1:"+colName[s+3]+"1").setColumnWidth(5);
			st2.openTable(colName[s+4]+"1:"+colName[s+4]+"1").setColumnWidth(5);
			st2.openTable(colName[s+5]+"1:"+colName[s+5]+"1").setColumnWidth(8);
			st2.openTable(colName[s+6]+"1:"+colName[s+6]+"1").setColumnWidth(8);
		}
		for(int s=0;s<21;s=s+7){
			st3.openTable(colName[s]+"1:"+colName[s]+"1").setColumnWidth(4);
			st3.openTable(colName[s+1]+"1:"+colName[s+1]+"1").setColumnWidth(17);
			st3.openTable(colName[s+2]+"1:"+colName[s+2]+"1").setColumnWidth(9);
			st3.openTable(colName[s+3]+"1:"+colName[s+3]+"1").setColumnWidth(5);
			st3.openTable(colName[s+4]+"1:"+colName[s+4]+"1").setColumnWidth(5);
			st3.openTable(colName[s+5]+"1:"+colName[s+5]+"1").setColumnWidth(8);
			st3.openTable(colName[s+6]+"1:"+colName[s+6]+"1").setColumnWidth(8);
		}

		//设置表格行高
		st1.openTable(colName[0]+"1:"+colName[20]+"1").setRowHeight(30.75);
		st1.openTable(colName[0]+"2:"+colName[20]+"2").setRowHeight(29.25);
		st1.openTable(colName[0]+"3:"+colName[20]+(zhs1+3)).setRowHeight(25);
		st2.openTable(colName[0]+"1:"+colName[20]+"1").setRowHeight(30.75);
		st2.openTable(colName[0]+"2:"+colName[20]+"2").setRowHeight(29.25);
		st2.openTable(colName[0]+"3:"+colName[20]+(zhs2+3)).setRowHeight(25);
		st3.openTable(colName[0]+"1:"+colName[20]+"1").setRowHeight(30.75);
		st3.openTable(colName[0]+"2:"+colName[20]+"2").setRowHeight(29.25);
		st3.openTable(colName[0]+"3:"+colName[20]+(zhs3+3)).setRowHeight(25);
	
	}else{//1-9,1-14周考试
		Cell cell;
		String line="";
		if(kszq.equals("1")){
			skzq="前十周";
		}else if(kszq.equals("2")){
			skzq="前十五周";
		}else if(kszq.equals("3")){
			skzq="期末考试";
		}
		sql="select c.[课程名称],c.考试形式,c.[行政班简称],c.[学生人数],c.[时间序列],c.[场地要求],c.[监考教师姓名] from ( "+
			" SELECT a.[课程名称],e.考试形式,b.[行政班简称],a.[学生人数],a.[时间序列],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b,dbo.V_考试形式 e where a.行政班代码=b.行政班代码 and a.考试形式=e.编号 and a.考场安排主表编号='"+tempSkjhzbbh+"' " +
			" union " +
			" SELECT a.[课程名称],e.考试形式,a.行政班名称 as [行政班简称],a.[学生人数],a.[时间序列],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,dbo.V_考试形式 e where a.专业代码='' and a.考试形式=e.编号 and a.考场安排主表编号='"+tempSkjhzbbh+"' "+
			" ) c order by c.时间序列,c.课程名称,c.[行政班简称] ";
		vector = db.GetContextVector(sql);
		for(int colNum=1; colNum<9; colNum++){
			for(int rowNum=1; rowNum<3; rowNum++){
				//生成标题
				if(colNum==1 && rowNum==1){
					wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[8]+"1").merge();
					cell = wb.openSheet("Sheet1").openCell("A1");
					cellContent = xnxqbm.substring(0, 4)+"学年第"+xnxqbm.substring(4, 5)+"学期"+skzq+"考试安排表";
						
					//maxWidth = 4*cellContent.length()/(vector.size()+1);
					cell.setValue(cellContent);
				}else if(rowNum==2){
					for(int j=0;j<9;j++){
						cell = wb.openSheet("Sheet1").openCell(colName[j]+"2");//当前单元格
						if(j==0){
							cellContent="序号";
						}else if(j==1){
							cellContent="课程名称";
						}else if(j==2){
							cellContent="考试形式";
						}else if(j==3){
							cellContent="班级名称";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="考试时间";
						}else if(j==6){
							cellContent="教室";
						}else if(j==7){
							cellContent="监考";
						}else if(j==8){
							cellContent="监考";
						}
						cell.setValue(cellContent);
						cell.getFont().setBold(true);
					}
				}else{
					
				}
			}
			for(int j=0;j<vector.size();j=j+7){
				for(int k=0;k<9;k++){
					cell = wb.openSheet("Sheet1").openCell(colName[k]+(j/7+3));//当前单元格
					if(k==0){
						cell.setValue("'"+(j/7+1));
					}else if(k==5){
						String week="";
						String time="";
						if(vector.get(j+k-1).toString().substring(0,2).equals("01")){
							week=reStr1.split("-")[1]+"月"+reStr1.split("-")[2]+"日(周"+weekday1+")";
						}else if(vector.get(j+k-1).toString().substring(0,2).equals("02")){
							week=reStr2.split("-")[1]+"月"+reStr2.split("-")[2]+"日(周"+weekday2+")";
						}else if(vector.get(j+k-1).toString().substring(0,2).equals("03")){
							week=reStr3.split("-")[1]+"月"+reStr3.split("-")[2]+"日(周"+weekday3+")";
						}else if(vector.get(j+k-1).toString().substring(0,2).equals("04")){
							week=reStr4.split("-")[1]+"月"+reStr4.split("-")[2]+"日(周"+weekday4+")";
						}else if(vector.get(j+k-1).toString().substring(0,2).equals("05")){
							week=reStr5.split("-")[1]+"月"+reStr5.split("-")[2]+"日(周"+weekday5+")";
						}
						if(vector.get(j+k-1).toString().substring(2,4).equals("01")){
							time="9:00-11:00";
						}else if(vector.get(j+k-1).toString().substring(2,4).equals("02")){
							time="12:45-14:45";
						}else if(vector.get(j+k-1).toString().substring(2,4).equals("03")){
							time="14:40-16:40";
						}else if(vector.get(j+k-1).toString().substring(2,4).equals("04")){
							time="16:30-18:30";
						}
						cell.setValue("'"+week+time);
					}else if(k==8){
						cell.setValue("'"+vector.get(j+k-2).toString());
					}else{
						cell.setValue("'"+vector.get(j+k-1).toString());
					}
					if(vector.get(j+4).toString().equals("0304")){
						line+=(j/7+3)+",";
					}
				}
			}
		}
		//合并单元格
		int cournum=0;
		int timenum=0;
		for(int i=0;i<vector.size()-7;i=i+7){
			if(vector.get(i).toString().equals(vector.get(i+7).toString())){
				cournum++;
			}else{
				wb.openSheet("Sheet1").openTable(colName[1]+(i/7+3-cournum)+":"+colName[1]+(i/7+3)).merge();
				cournum=0;
			}
			if(vector.get(i+4).toString().equals(vector.get(i+4+7).toString())){
				timenum++;
			}else{
				wb.openSheet("Sheet1").openTable(colName[5]+(i/7+3-timenum)+":"+colName[5]+(i/7+3)).merge();
				timenum=0;
			}
		}
		
		
		wb.openSheet("Sheet1").openTable(colName[0]+(vector.size()/7+3)+":"+colName[8]+(vector.size()/7+3)).merge();
		wb.openSheet("Sheet1").openCell(colName[0]+(vector.size()/7+3)).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
		cell = wb.openSheet("Sheet1").openCell(colName[0]+(vector.size()/7+3));//当前单元格
		cell.getFont().setBold(true);
		cell.setValue("注意：");
		wb.openSheet("Sheet1").openTable(colName[0]+(vector.size()/7+4)+":"+colName[8]+(vector.size()/7+4)).merge();
		cell = wb.openSheet("Sheet1").openCell(colName[0]+(vector.size()/7+4));//当前单元格
		cell.getFont().setBold(true);
		cell.getFont().setSize(8);
		cell.setValue("1、考试的同学必须带好校园一卡通，若一卡通遗失，必须带学生证、身份证（或住宿证、社保卡）二证参加考试，不带证件者将取消考试资格。");
		wb.openSheet("Sheet1").openTable(colName[0]+(vector.size()/7+5)+":"+colName[8]+(vector.size()/7+5)).merge();
		cell = wb.openSheet("Sheet1").openCell(colName[0]+(vector.size()/7+5));//当前单元格
		cell.getFont().setBold(true);
		cell.getFont().setSize(10);
		cell.setValue("2、考试时间、形式以卷面考试时间、形式为准，请各位监考老师提醒学生。");
		wb.openSheet("Sheet1").openTable(colName[0]+(vector.size()/7+6)+":"+colName[8]+(vector.size()/7+6)).merge();
		cell = wb.openSheet("Sheet1").openCell(colName[0]+(vector.size()/7+6));//当前单元格
		cell.getFont().setBold(true);
		cell.getFont().setSize(10);
		cell.setValue("3、选修考试课请各辅导员务必通知到每位学生。");
		wb.openSheet("Sheet1").openTable(colName[0]+(vector.size()/7+7)+":"+colName[8]+(vector.size()/7+7)).merge();
		cell = wb.openSheet("Sheet1").openCell(colName[0]+(vector.size()/7+7));//当前单元格
		cell.getFont().setBold(true);
		cell.getFont().setSize(10);
		cell.setValue("4、请各系按考试表上的要求派老师前来监考，并请于考前15分钟到教务处报到。");
		wb.openSheet("Sheet1").openTable(colName[0]+(vector.size()/7+8)+":"+colName[8]+(vector.size()/7+8)).merge();
		cell = wb.openSheet("Sheet1").openCell(colName[0]+(vector.size()/7+8));//当前单元格
		cell.getFont().setBold(true);
		cell.getFont().setSize(10);
		cell.setValue("5、上机考的监考老师请各系派会计算机操作的老师监考，并请于考前20分钟到教务处报到。");
		
//		for(int i=0;i<vec2.size();i=i+7){
//			System.out.println(vec2.get(i).toString()+""+vec2.get(i+1).toString()+""+vec2.get(i+2).toString()+""+vec2.get(i+3).toString()+""+vec2.get(i+4).toString()+""+vec2.get(i+5).toString()+""+vec2.get(i+6).toString());
//		}
		
		zjs=vector.size()/7;
		
		//设置单元格的水平对齐方式
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[8]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[8]+(zjs+2)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);	
		
		if(!line.equals("")){
			String[] lines=line.substring(0,line.length()-1).split(",");
			for(int i=0;i<lines.length;i++){
				wb.openSheet("Sheet1").openCell(colName[3]+lines[i]).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
			}
		}
		
		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置课表边框线
		Border border = wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[8]+(zjs+2)).getBorder();
		//设置表格边框的宽度、颜色
		border.setWeight(XlBorderWeight.xlThin);
		border.setLineColor(Color.black);
		
		//设置标题字体大小
		cell = wb.openSheet("Sheet1").openCell("A1");
		cell.getFont().setBold(true);
		fontSize = 18;
		cell.getFont().setSize(fontSize);
		
		//设置课表标题行列字体大小
		fontSize = 12;
		wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[8]+"2").getFont().setSize(fontSize);
		wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[0]+(zjs+1)).getFont().setSize(fontSize);
		//设置课表内容行列字体大小
		fontSize = 10;
		//设置表格列宽
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(4);
		wb.openSheet("Sheet1").openTable(colName[1]+"1:"+colName[1]+"1").setColumnWidth(20);
		wb.openSheet("Sheet1").openTable(colName[2]+"1:"+colName[2]+"1").setColumnWidth(8);
		wb.openSheet("Sheet1").openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(10);
		wb.openSheet("Sheet1").openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(6);
		wb.openSheet("Sheet1").openTable(colName[5]+"1:"+colName[5]+"1").setColumnWidth(25);
		wb.openSheet("Sheet1").openTable(colName[6]+"1:"+colName[6]+"1").setColumnWidth(6);
		wb.openSheet("Sheet1").openTable(colName[7]+"1:"+colName[7]+"1").setColumnWidth(8);
		wb.openSheet("Sheet1").openTable(colName[8]+"1:"+colName[8]+"1").setColumnWidth(8);
		
		wb.openSheet("Sheet1").openTable(colName[1]+"3:"+colName[5]+(zjs+2)).getFont().setSize(fontSize);
		//设置表格行高
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[8]+"2").setRowHeight(30);
		wb.openSheet("Sheet1").openTable(colName[0]+"3:"+colName[8]+(zjs+2)).setRowHeight(20);
		
	}
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "template.xls";

		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		//poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "  "+xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期 "+skzq+" 考试安排表");
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
	}
	
	
	/**
	 * 试卷标签导出
	 * @date:2016-07-08
	 * @author:lupengfei
	 * @param xnxq+jxxz 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	public static void exportExamScheduleTag(HttpServletRequest request, String xnxqbm, String kszq) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String xnxqmc = "";//学年学期名称
		int mzts = 0;//每周天数
		int sw = 0;//上午节数
		int zw = 0;//中午节数
		int xw = 0;//下午节数
		int ws = 0;//晚上节数
		int zjs = 0;//总节数
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 40;
		float fontSize = 0;
		String skzq="";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String sqlday=" select convert(varchar(10),考试日期,21) as [考试日期] from [V_考试管理_考场安排主表] where [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [考试周期]='"+MyTools.fixSql(kszq)+"' ";
		Vector vecday=db.GetContextVector(sqlday);

		String xqkssj=vecday.get(0).toString();	
		try {
			cal.setTime(format.parse(xqkssj));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//考试日期是星期几
		int week1 = cal.get(Calendar.DAY_OF_WEEK)-1;
		//将考试日期后2天加入
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String str = xqkssj; 
		Date dt=sdf.parse(str,new ParsePosition(0)); 
		Calendar rightNow = Calendar.getInstance(); 
		rightNow.setTime(dt); 
		//rightNow.add(Calendar.DATE,1);//你要加减的日期   
		//rightNow.add(Calendar.DATE,2);//你要加减的日期   
		Date dt1=rightNow.getTime(); 
		String reStr1=sdf.format(dt1); 
		
		rightNow.add(Calendar.DATE,-2);//你要加减的日期 
		Date dt5=rightNow.getTime(); 
		String reStr5=sdf.format(dt5);
		try {
			cal.setTime(format.parse(reStr5));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week5=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,3);//你要加减的日期 
		Date dt2=rightNow.getTime(); 
		String reStr2=sdf.format(dt2); 
		try {
			cal.setTime(format.parse(reStr2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week2=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt3=rightNow.getTime(); 
		String reStr3=sdf.format(dt3);
		try {
			cal.setTime(format.parse(reStr3));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week3=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt4=rightNow.getTime(); 
		String reStr4=sdf.format(dt4);
		try {
			cal.setTime(format.parse(reStr4));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week4=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt6=rightNow.getTime(); 
		String reStr6=sdf.format(dt6);
		try {
			cal.setTime(format.parse(reStr6));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week6=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		rightNow.add(Calendar.DATE,1);//你要加减的日期 
		Date dt7=rightNow.getTime(); 
		String reStr7=sdf.format(dt7);
		try {
			cal.setTime(format.parse(reStr7));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int week7=cal.get(Calendar.DAY_OF_WEEK)-1;
		
		
		System.out.println("reStr:--"+reStr1+","+week1+"|"+reStr2+","+week2+"|"+reStr3+","+week3);
		String weekday1="";
		String weekday2="";
		String weekday3="";
		String weekday4="";
		String weekday5="";
		String weekday6="";
		String weekday7="";
		if(week1==1){
			weekday1="一";
		}else if(week1==2){
			weekday1="二";
		}else if(week1==3){
			weekday1="三";
		}else if(week1==4){
			weekday1="四";
		}else if(week1==5){
			weekday1="五";
		}else if(week1==6){
			weekday1="六";
		}else if(week1==0){
			weekday1="日";
		}
		
		if(week2==1){
			weekday2="一";
		}else if(week2==2){
			weekday2="二";
		}else if(week2==3){
			weekday2="三";
		}else if(week2==4){
			weekday2="四";
		}else if(week2==5){
			weekday2="五";
		}else if(week2==6){
			weekday2="六";
		}else if(week2==0){
			weekday2="日";
		}
		
		if(week3==1){
			weekday3="一";
		}else if(week3==2){
			weekday3="二";
		}else if(week3==3){
			weekday3="三";
		}else if(week3==4){
			weekday3="四";
		}else if(week3==5){
			weekday3="五";
		}else if(week3==6){
			weekday3="六";
		}else if(week3==0){
			weekday3="日";
		}
		
		if(week4==1){
			weekday4="一";
		}else if(week4==2){
			weekday4="二";
		}else if(week4==3){
			weekday4="三";
		}else if(week4==4){
			weekday4="四";
		}else if(week4==5){
			weekday4="五";
		}else if(week4==6){
			weekday4="六";
		}else if(week4==0){
			weekday4="日";
		}
		
		if(week5==1){
			weekday5="一";
		}else if(week5==2){
			weekday5="二";
		}else if(week5==3){
			weekday5="三";
		}else if(week5==4){
			weekday5="四";
		}else if(week5==5){
			weekday5="五";
		}else if(week5==6){
			weekday5="六";
		}else if(week5==0){
			weekday5="日";
		}
		
		if(week6==1){
			weekday6="一";
		}else if(week6==2){
			weekday6="二";
		}else if(week6==3){
			weekday6="三";
		}else if(week6==4){
			weekday6="四";
		}else if(week6==5){
			weekday6="五";
		}else if(week6==6){
			weekday6="六";
		}else if(week6==0){
			weekday6="日";
		}
		
		if(week7==1){
			weekday7="一";
		}else if(week7==2){
			weekday7="二";
		}else if(week7==3){
			weekday7="三";
		}else if(week7==4){
			weekday7="四";
		}else if(week7==5){
			weekday7="五";
		}else if(week7==6){
			weekday7="六";
		}else if(week5==0){
			weekday7="日";
		}
		
		String name1=reStr1.split("-")[1]+"."+reStr1.split("-")[2];
		String name2=reStr2.split("-")[1]+"."+reStr2.split("-")[2];
		String name3=reStr3.split("-")[1]+"."+reStr3.split("-")[2];
				
		Workbook wb = new Workbook();
		String cellContent = ""; //当前单元格的内容
		Vector tempVec = new Vector();
		Vector hbSetVec = new Vector();
		Vector timeVec = null;
		
		String tempOrder = "";//时间序列
		int tempIndex = -1;
		int mergeNum = 0;//单元格合并数
		boolean flag = true;//用于判断是否可合并
		
		String tempSkjhmxbh = "";//授课计划明细编号
		String timeOrder = "";//时间序列
		String tempTeaCode = "";//教师编号
		String tempTeaName = "";//教师姓名
		String tempSiteCode = "";//场地编号
		String tempSkzc = "";//授课周次
		
		String tempShiChang="";//试场
		String tempCourseName = "";//课程名称
		String tempClassName = "";//班级名称
		String tempClassNum="";//班级人数
		String tempCourseCycle="";//上课周期
		String tempQizQimo="";//期中期末
		String tempExamType="";//考试形式
		String tempSiteName = "";//场地名称
		String tempInvigilate="";//监考教师
		String tempSkjhzbbh="";//考试安排主表编号
		
		String skjhmxbhArray[] = new String[0];
		
		Vector vector = null;
		Vector vec = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		String classroom="";
		String timedata="";
		
		String sqlzb="select [考场安排主表编号] from V_考试管理_考场安排主表 where [学年学期编码]='"+xnxqbm+"' and [考试周期]='"+kszq+"' ";
		Vector veczb=db.GetContextVector(sqlzb);
		if(veczb!=null&&veczb.size()>0){
			tempSkjhzbbh=veczb.get(0).toString();
		}
		
	if(kszq.equals("3")){//期末考试	
		name3=reStr5.split("-")[1]+"."+reStr5.split("-")[2];
		skzq="期末";
		Sheet[] st=new Sheet[9];
		st[0]=wb.createSheet(name1+"(1)",SheetInsertType.Before,"sheet1");
		st[1]=wb.createSheet(name1+"(2)",SheetInsertType.Before,"sheet1");
		st[2]=wb.createSheet(name1+"(3)",SheetInsertType.Before,"sheet1");
		st[3]=wb.createSheet(name2+"(1)",SheetInsertType.Before,"sheet1");
		st[4]=wb.createSheet(name2+"(2)",SheetInsertType.Before,"sheet1");
		st[5]=wb.createSheet(name2+"(3)",SheetInsertType.Before,"sheet1");
		st[6]=wb.createSheet(name3+"(1)",SheetInsertType.Before,"sheet1");
		st[7]=wb.createSheet(name3+"(2)",SheetInsertType.Before,"sheet1");
		st[8]=wb.createSheet(name3+"(3)",SheetInsertType.Before,"sheet1");
		
		Cell[] cell=new Cell[9];
		
		sql=" SELECT ROW_NUMBER() over(order by a.[课程名称],b.[行政班简称]) as 试场,a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b,dbo.V_考试管理_考场安排主表 c where a.行政班代码=b.行政班代码  and a.[考场安排主表编号]=c.[考场安排主表编号] and a.考场安排主表编号='"+tempSkjhzbbh+"' " +
			" union " +
			" SELECT ROW_NUMBER() over(order by a.[课程名称]) as 试场,a.[时间序列],a.[课程名称],'' as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,dbo.V_考试管理_考场安排主表 c where a.专业代码='' and a.[考场安排主表编号]=c.[考场安排主表编号] and a.考场安排主表编号='"+tempSkjhzbbh+"' ";
		vector = db.GetContextVector(sql);
		
		int shichang=1;
		mergeNum = 0;
		int[] line=new int[9];//统计每个时段的考试数量
		for(int l=0;l<9;l++){
			line[l]=1;
		}
		
		if(vector!=null&&vector.size()>0){
			for(int i=0;i<vector.size();i=i+7){
				if(vector.get(i+1).toString().equals("0101")){
					for(int j=0;j<8;j++){
						cell[0] = st[0].openCell(colName[j]+(line[0]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[0].setValue(cellContent);
						
						cell[0] = st[0].openCell(colName[j]+(line[0]*2));//当前单元格
						if(j==0){
							cellContent=line[0]+"";
						}else if(j==1){
							cellContent=reStr1.split("-")[1]+"月"+reStr1.split("-")[2]+"日(周"+weekday1+")09:00-11:00";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[0]>1){
								st[0].openTable(colName[2]+(line[0]*2)+":"+colName[2]+(line[0]*2)).getFont().setSize(fontSize);
							}else{
								st[0].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[0].setValue(cellContent);
					}
					line[0]++;
				}else if(vector.get(i+1).toString().equals("0102")){
					for(int j=0;j<8;j++){
						cell[1] = st[1].openCell(colName[j]+(line[1]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[1].setValue(cellContent);
						
						cell[1] = st[1].openCell(colName[j]+(line[1]*2));//当前单元格
						if(j==0){
							cellContent=line[1]+"";
							
						}else if(j==1){
							cellContent=reStr1.split("-")[1]+"月"+reStr1.split("-")[2]+"日(周"+weekday1+")12:30-14:30";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[1]>1){
								st[1].openTable(colName[2]+(line[1]*2)+":"+colName[2]+(line[1]*2)).getFont().setSize(fontSize);
							}else{
								st[1].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
							
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[1].setValue(cellContent);
					}
					line[1]++;
				}else if(vector.get(i+1).toString().equals("0103")){
					for(int j=0;j<8;j++){
						cell[2] = st[2].openCell(colName[j]+(line[2]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[2].setValue(cellContent);
						
						cell[2] = st[2].openCell(colName[j]+(line[2]*2));//当前单元格
						if(j==0){
							cellContent=line[2]+"";
							
						}else if(j==1){
							cellContent=reStr1.split("-")[1]+"月"+reStr1.split("-")[2]+"日(周"+weekday1+")14:45-16:45";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[2]>1){
								st[2].openTable(colName[2]+(line[2]*2)+":"+colName[2]+(line[2]*2)).getFont().setSize(fontSize);
							}else{
								st[2].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
							
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[2].setValue(cellContent);
					}
					line[2]++;
				}else if(vector.get(i+1).toString().equals("0201")){
					for(int j=0;j<8;j++){
						cell[3] = st[3].openCell(colName[j]+(line[3]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[3].setValue(cellContent);
						
						cell[3] = st[3].openCell(colName[j]+(line[3]*2));//当前单元格
						if(j==0){
							cellContent=line[3]+"";
						}else if(j==1){
							cellContent=reStr2.split("-")[1]+"月"+reStr2.split("-")[2]+"日(周"+weekday2+")09:00-11:00";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[3]>1){
								st[3].openTable(colName[2]+(line[3]*2)+":"+colName[2]+(line[3]*2)).getFont().setSize(fontSize);
							}else{
								st[3].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[3].setValue(cellContent);
					}
					line[3]++;
				}else if(vector.get(i+1).toString().equals("0202")){
					for(int j=0;j<8;j++){
						cell[4] = st[4].openCell(colName[j]+(line[4]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[4].setValue(cellContent);
						
						cell[4] = st[4].openCell(colName[j]+(line[4]*2));//当前单元格
						if(j==0){
							cellContent=line[4]+"";
						}else if(j==1){
							cellContent=reStr2.split("-")[1]+"月"+reStr2.split("-")[2]+"日(周"+weekday2+")12:30-14:30";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[4]>1){
								st[4].openTable(colName[2]+(line[4]*2)+":"+colName[2]+(line[4]*2)).getFont().setSize(fontSize);
							}else{
								st[4].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[4].setValue(cellContent);
					}
					line[4]++;
				}else if(vector.get(i+1).toString().equals("0203")){
					for(int j=0;j<8;j++){
						cell[5] = st[5].openCell(colName[j]+(line[5]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[5].setValue(cellContent);
						
						cell[5] = st[5].openCell(colName[j]+(line[5]*2));//当前单元格
						if(j==0){
							cellContent=line[5]+"";
						}else if(j==1){
							cellContent=reStr2.split("-")[1]+"月"+reStr2.split("-")[2]+"日(周"+weekday2+")14:45-16:45";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[5]>1){
								st[5].openTable(colName[2]+(line[5]*2)+":"+colName[2]+(line[5]*2)).getFont().setSize(fontSize);
							}else{
								st[5].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[5].setValue(cellContent);
					}
					line[5]++;
				}else if(vector.get(i+1).toString().equals("0301")){
					for(int j=0;j<8;j++){
						cell[6] = st[6].openCell(colName[j]+(line[6]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[6].setValue(cellContent);
						
						cell[6] = st[6].openCell(colName[j]+(line[6]*2));//当前单元格
						if(j==0){
							cellContent=line[6]+"";
						}else if(j==1){
							cellContent=reStr5.split("-")[1]+"月"+reStr5.split("-")[2]+"日(周"+weekday5+")09:00-11:00";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[6]>1){
								st[6].openTable(colName[2]+(line[6]*2)+":"+colName[2]+(line[6]*2)).getFont().setSize(fontSize);
							}else{
								st[6].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[6].setValue(cellContent);
					}
					line[6]++;
				}else if(vector.get(i+1).toString().equals("0302")){
					for(int j=0;j<8;j++){
						cell[7] = st[7].openCell(colName[j]+(line[7]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[7].setValue(cellContent);
						
						cell[7] = st[7].openCell(colName[j]+(line[7]*2));//当前单元格
						if(j==0){
							cellContent=line[7]+"";
						}else if(j==1){
							cellContent=reStr5.split("-")[1]+"月"+reStr5.split("-")[2]+"日(周"+weekday5+")12:30-14:30";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[7]>1){
								st[7].openTable(colName[2]+(line[7]*2)+":"+colName[2]+(line[7]*2)).getFont().setSize(fontSize);
							}else{
								st[7].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							cellContent=vector.get(i+j).toString();
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[7].setValue(cellContent);
					}
					line[7]++;
				}else if(vector.get(i+1).toString().equals("0503")){
					for(int j=0;j<8;j++){
						cell[8] = st[8].openCell(colName[j]+(line[8]*2-1));//当前单元格
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="班级";
						}else if(j==4){
							cellContent="人数";
						}else if(j==5){
							cellContent="教室";
						}else if(j==6){
							cellContent="监考";
						}else if(j==7){
							cellContent="监考";
						}
						cell[8].setValue(cellContent);
						
						cell[8] = st[8].openCell(colName[j]+(line[8]*2));//当前单元格
						if(j==0){
							cellContent=line[8]+"";
						}else if(j==1){
							cellContent=reStr5.split("-")[1]+"月"+reStr5.split("-")[2]+"日(周"+weekday5+")14:45-16:45";
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							if(line[8]>1){
								st[8].openTable(colName[2]+(line[8]*2)+":"+colName[2]+(line[8]*2)).getFont().setSize(fontSize);
							}else{
								st[8].openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
							}	
						}else if(j>2&&j<6){
							String xsrs="";
							if(j==4){
								if(vector.get(i+j).toString().equals("0")){
									xsrs="";
								}else{
									xsrs=vector.get(i+j).toString();
								}
								cellContent=xsrs;
							}else{
								cellContent=vector.get(i+j).toString();
							}
							
						}else if(j==6){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						cell[8].setValue(cellContent);
					}
					line[8]++;
				}
			}
		}
		
		//zjs=vector.size()/7;//行数
		int zhs=0;
		int zhs1=0;
		int zhs2=0;
		int zhs3=0;
		if(line[0]>=zhs1){
			zhs1=line[0];
		}
		if(line[1]>=zhs1){
			zhs1=line[1];
		}
		if(line[2]>=zhs1){
			zhs1=line[2];
		}
		
		if(line[3]>=zhs2){
			zhs2=line[3];
		}
		if(line[4]>=zhs2){
			zhs2=line[4];
		}
		if(line[5]>=zhs2){
			zhs2=line[5];
		}
		
		if(line[6]>=zhs3){
			zhs3=line[6];
		}
		if(line[7]>=zhs3){
			zhs3=line[7];
		}
		if(line[8]>=zhs3){
			zhs3=line[8];
		}
		
		//设置单元格的水平对齐方式
		for(int t=0;t<9;t++){
			st[t].openTable(colName[0]+"1:"+colName[7]+(line[t]*2)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		}
		
		
		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置课表边框线
		Border[] border=new Border[9];
		for(int t=0;t<9;t++){
			if(line[t]>1){
				border[t] = st[t].openTable(colName[0]+"1:"+colName[7]+((line[t]-1)*2)).getBorder();
				//设置表格边框的宽度、颜色
				border[t].setWeight(XlBorderWeight.xlThin);
				border[t].setLineColor(Color.black);
				//设置标题字体大小

				//设置课表标题行列字体大小
				st[t].openTable(colName[0]+"1:"+colName[7]+(line[t]*2)).getFont().setBold(true);
				//设置课表内容行列字体大小
				
				//设置表格列宽
				st[t].openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(4);
				st[t].openTable(colName[1]+"1:"+colName[1]+"1").setColumnWidth(29);
				st[t].openTable(colName[2]+"1:"+colName[2]+"1").setColumnWidth(23);
				st[t].openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(7.5);
				st[t].openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(4.5);
				st[t].openTable(colName[5]+"1:"+colName[5]+"1").setColumnWidth(5.13);
				st[t].openTable(colName[6]+"1:"+colName[6]+"1").setColumnWidth(6.3);
				st[t].openTable(colName[7]+"1:"+colName[7]+"1").setColumnWidth(6.3);
				//设置表格行高
				st[t].openTable(colName[0]+"1:"+colName[7]+(line[t]*2)).setRowHeight(28);
			}
		}
		
	}else if(kszq.equals("1")||kszq.equals("2")){//1-9,1-14周考试
		
		Cell cell;
		String line="";
		if(kszq.equals("1")){
			skzq="前十周";
		}else if(kszq.equals("2")){
			skzq="前十五周";
		}else if(kszq.equals("3")){
			skzq="期末";
		}

		sql="select c.考试形式,c.[时间序列],c.[课程名称],c.[行政班简称],c.[学生人数],c.[场地要求],c.[监考教师姓名] from ( "+
			" SELECT e.考试形式,a.[时间序列],a.[课程名称],b.[行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,[dbo].[V_学校班级数据子类] b,dbo.V_考试形式 e where a.行政班代码=b.行政班代码 and a.考试形式=e.编号 and a.考场安排主表编号='"+tempSkjhzbbh+"' " +
			" union " +
			" SELECT e.考试形式,a.[时间序列],a.[课程名称],a.行政班名称 as [行政班简称],a.[学生人数],a.[场地要求],a.[监考教师姓名] FROM [dbo].[V_考试管理_考场安排明细表] a,dbo.V_考试形式 e where a.专业代码='' and a.考试形式=e.编号 and a.考场安排主表编号='"+tempSkjhzbbh+"' "+
			" ) c order by c.时间序列,c.课程名称,c.[行政班简称] ";
		vector = db.GetContextVector(sql);
		
		if(vector!=null&&vector.size()>0){
			for(int i=0;i<vector.size();i=i+7){
					for(int j=0;j<9;j++){
						cell = wb.openSheet("Sheet1").openCell(colName[j]+(i/7*2+1));//当前单元格()
						if(j==0){
							cellContent="试场";
						}else if(j==1){
							cellContent="考试时间";
						}else if(j==2){
							cellContent="科目";
						}else if(j==3){
							cellContent="考试形式";
						}else if(j==4){
							cellContent="班级";
						}else if(j==5){
							cellContent="人数";
						}else if(j==6){
							cellContent="教室";
						}else if(j==7){
							cellContent="监考";
						}else if(j==8){
							cellContent="监考";
						}
						cell.setValue(cellContent);
						
						cell = wb.openSheet("Sheet1").openCell(colName[j]+(i/7*2+2));//当前单元格
						if(j==0){
							cellContent=(i/7+1)+"";
						}else if(j==1){
							String week="";
							String time="";
							if(vector.get(i+j).toString().substring(0,2).equals("01")){
								week=reStr1.split("-")[1]+"月"+reStr1.split("-")[2]+"日(周"+weekday1+")";
							}else if(vector.get(i+j).toString().substring(0,2).equals("02")){
								week=reStr2.split("-")[1]+"月"+reStr2.split("-")[2]+"日(周"+weekday2+")";
							}else if(vector.get(i+j).toString().substring(0,2).equals("03")){
								week=reStr3.split("-")[1]+"月"+reStr3.split("-")[2]+"日(周"+weekday3+")";
							}else if(vector.get(i+j).toString().substring(0,2).equals("04")){
								week=reStr4.split("-")[1]+"月"+reStr4.split("-")[2]+"日(周"+weekday4+")";
							}else if(vector.get(i+j).toString().substring(0,2).equals("05")){
								week=reStr5.split("-")[1]+"月"+reStr5.split("-")[2]+"日(周"+weekday5+")";
							}
							if(vector.get(i+j).toString().substring(2,4).equals("01")){
								time="9:00-11:00";
							}else if(vector.get(i+j).toString().substring(2,4).equals("02")){
								time="12:45-14:45";
							}else if(vector.get(i+j).toString().substring(2,4).equals("03")){
								time="14:40-16:40";
							}else if(vector.get(i+j).toString().substring(2,4).equals("04")){
								time="16:30-18:30";
							}
							cellContent="'"+week+time;
						}else if(j==2){
							cellContent=vector.get(i+j).toString();
							double celllength=getLength(cellContent);
							if(celllength>10){
								if(celllength>12){
									if(celllength>14){
										fontSize = 6;
									}else{
										fontSize = 8;
									}
								}else{
									fontSize = 10;
								}
							}else{
								fontSize = 12;
							}
							
							wb.openSheet("Sheet1").openTable(colName[2]+"2:"+colName[2]+"2").getFont().setSize(fontSize);
								
						}else if(j==3){
							cellContent=vector.get(i).toString();
						}else if(j>3&&j<7){
							cellContent=vector.get(i+j-1).toString();
						}else if(j==7){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[0];
							}else{
								cellContent=vector.get(i+6).toString();
							}
						}else if(j==8){
							if(!vector.get(i+6).toString().equals("")&&vector.get(i+6).toString().indexOf(",")>0){//有监考教师
								cellContent=vector.get(i+6).toString().split(",")[1];
							}else{
								cellContent="";
							}
						}
						if(vector.get(i+1).toString().equals("0304")){
							line+=(i/7*2+2)+",";
						}
						cell.setValue(cellContent);
					}	
			}
		}
		
		//设置单元格的水平对齐方式
		for(int t=0;t<9;t++){
			wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[8]+(vector.size()/7*2)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
		}
		
		if(!line.equals("")){
			String[] lines=line.substring(0,line.length()-1).split(",");
			for(int i=0;i<lines.length;i++){
				wb.openSheet("Sheet1").openCell(colName[4]+lines[i]).setHorizontalAlignment(XlHAlign.xlHAlignLeft);
			}
		}
		
		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
				
		//设置课表边框线
		//设置课表边框线
		Border border = wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[8]+(vector.size()/7*2)).getBorder();
		//设置表格边框的宽度、颜色
		border.setWeight(XlBorderWeight.xlThin);
		border.setLineColor(Color.black);		
		
		//设置课表标题行列字体大小
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[8]+(vector.size()/7*2)).getFont().setBold(true);
		//设置课表内容行列字体大小
						
		//设置表格列宽
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(4);
		wb.openSheet("Sheet1").openTable(colName[1]+"1:"+colName[1]+"1").setColumnWidth(28);
		wb.openSheet("Sheet1").openTable(colName[2]+"1:"+colName[2]+"1").setColumnWidth(21);
		wb.openSheet("Sheet1").openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(9);
		wb.openSheet("Sheet1").openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(7.5);
		wb.openSheet("Sheet1").openTable(colName[5]+"1:"+colName[5]+"1").setColumnWidth(4.5);
		wb.openSheet("Sheet1").openTable(colName[6]+"1:"+colName[6]+"1").setColumnWidth(5.13);
		wb.openSheet("Sheet1").openTable(colName[7]+"1:"+colName[7]+"1").setColumnWidth(6.3);
		wb.openSheet("Sheet1").openTable(colName[8]+"1:"+colName[8]+"1").setColumnWidth(6.3);
		//设置表格行高
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[8]+(vector.size()/7*2)).setRowHeight(28);
			
	}

		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "template.xls";

		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		//poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "  "+xnxqbm.substring(0,4)+"学年第"+xnxqbm.substring(4,5)+"学期 "+skzq+" 试卷标签");
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
	}
	
	
	/**
	 * 学生成绩导出
	 * @date:2016-07-08
	 * @author:lupengfei
	 * @param xnxq+jxxz 学年学期编码
	 * @param exportType 导出课表类型
	 * @param parentId
	 * @param code 班级/教师编号
	 * @param timetableName 课程名称
	 * @throws SQLException
	 */
	public static void exportResit(HttpServletRequest request, String xnxq, String jxxz, String qzqm) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String xnxqmc = "";//学年学期名称
		int mzts = 0;//每周天数
		int sw = 0;//上午节数
		int zw = 0;//中午节数
		int xw = 0;//下午节数
		int ws = 0;//晚上节数
		int zjs = 0;//总节数
		String timeArray[] = new String[0];
		double maxWidth = 0;
		double maxHeight = 40;
		float fontSize = 0;
		String skzq="";
		
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar cal = Calendar.getInstance();
//		String sqlday=" select convert(varchar(10),考试日期,21) as [考试日期] from [V_考试管理_考场安排主表] where [学年学期编码]='"+MyTools.fixSql(xnxqbm)+"' and [考试周期]='"+MyTools.fixSql(kszq)+"' ";
//		Vector vecday=db.GetContextVector(sqlday);
//
//		String xqkssj=vecday.get(0).toString();	
//		try {
//			cal.setTime(format.parse(xqkssj));
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		Workbook wb = new Workbook();
		Cell cell;

		String cellContent = ""; //当前单元格的内容
		Vector tempVec = new Vector();
		Vector hbSetVec = new Vector();
		Vector timeVec = null;
		
		String tempOrder = "";//时间序列
		int tempIndex = -1;
		int mergeNum = 0;//单元格合并数
		boolean flag = true;//用于判断是否可合并
		
		String tempSkjhmxbh = "";//授课计划明细编号
		String timeOrder = "";//时间序列
		String tempTeaCode = "";//教师编号
		String tempTeaName = "";//教师姓名
		String tempSiteCode = "";//场地编号
		String tempSkzc = "";//授课周次
		
		String tempShiChang="";//试场
		String tempCourseName = "";//课程名称
		String tempClassName = "";//班级名称
		String tempClassNum="";//班级人数
		String tempCourseCycle="";//上课周期
		String tempQizQimo="";//期中期末
		String tempExamType="";//考试形式
		String tempSiteName = "";//场地名称
		String tempInvigilate="";//监考教师
		
		
		String skjhmxbhArray[] = new String[0];
		
		Vector vector = null;
		Vector vec = null;
		String sql = "";
		String sql1 = "";
		String sql2 = "";
		String sql3 = "";
		String classroom="";
		String timedata="";
		int stunum=1;//补考学生行数
		int counum=0;//excel表中行数
				
		String xn="";
		String xq="";
		if(xnxq.substring(0, 1).equals("0")){
			xn+="○";
		}else if(xnxq.substring(0, 1).equals("1")){
			xn+="一";
		}else if(xnxq.substring(0, 1).equals("2")){
			xn+="二";
		}else if(xnxq.substring(0, 1).equals("3")){
			xn+="三";
		}else if(xnxq.substring(0, 1).equals("4")){
			xn+="四";
		}else if(xnxq.substring(0, 1).equals("5")){
			xn+="五";
		}else if(xnxq.substring(0, 1).equals("6")){
			xn+="六";
		}else if(xnxq.substring(0, 1).equals("7")){
			xn+="七";
		}else if(xnxq.substring(0, 1).equals("8")){
			xn+="八";
		}else if(xnxq.substring(0, 1).equals("9")){
			xn+="九";
		}
		
		if(xnxq.substring(1, 2).equals("0")){
			xn+="○";
		}else if(xnxq.substring(1, 2).equals("1")){
			xn+="一";
		}else if(xnxq.substring(1, 2).equals("2")){
			xn+="二";
		}else if(xnxq.substring(1, 2).equals("3")){
			xn+="三";
		}else if(xnxq.substring(1, 2).equals("4")){
			xn+="四";
		}else if(xnxq.substring(1, 2).equals("5")){
			xn+="五";
		}else if(xnxq.substring(1, 2).equals("6")){
			xn+="六";
		}else if(xnxq.substring(1, 2).equals("7")){
			xn+="七";
		}else if(xnxq.substring(1, 2).equals("8")){
			xn+="八";
		}else if(xnxq.substring(1, 2).equals("9")){
			xn+="九";
		}
		
		if(xnxq.substring(2, 3).equals("0")){
			xn+="○";
		}else if(xnxq.substring(2, 3).equals("1")){
			xn+="一";
		}else if(xnxq.substring(2, 3).equals("2")){
			xn+="二";
		}else if(xnxq.substring(2, 3).equals("3")){
			xn+="三";
		}else if(xnxq.substring(2, 3).equals("4")){
			xn+="四";
		}else if(xnxq.substring(2, 3).equals("5")){
			xn+="五";
		}else if(xnxq.substring(2, 3).equals("6")){
			xn+="六";
		}else if(xnxq.substring(2, 3).equals("7")){
			xn+="七";
		}else if(xnxq.substring(2, 3).equals("8")){
			xn+="八";
		}else if(xnxq.substring(2, 3).equals("9")){
			xn+="九";
		}
		
		if(xnxq.substring(3, 4).equals("0")){
			xn+="○";
		}else if(xnxq.substring(3, 4).equals("1")){
			xn+="一";
		}else if(xnxq.substring(3, 4).equals("2")){
			xn+="二";
		}else if(xnxq.substring(3, 4).equals("3")){
			xn+="三";
		}else if(xnxq.substring(3, 4).equals("4")){
			xn+="四";
		}else if(xnxq.substring(3, 4).equals("5")){
			xn+="五";
		}else if(xnxq.substring(3, 4).equals("6")){
			xn+="六";
		}else if(xnxq.substring(3, 4).equals("7")){
			xn+="七";
		}else if(xnxq.substring(3, 4).equals("8")){
			xn+="八";
		}else if(xnxq.substring(3, 4).equals("9")){
			xn+="九";
		}
		
		if(xnxq.substring(4, 5).equals("0")){
			xq+="○";
		}else if(xnxq.substring(4, 5).equals("1")){
			xq+="一";
		}else if(xnxq.substring(4, 5).equals("2")){
			xq+="二";
		}
		
		sql = "SELECT a.学号,a.姓名,e.行政班简称,b.课程名称,b.学分,'' as 补考分数,'' as 学生签名 " +
			"FROM [dbo].[V_成绩管理_学生成绩信息表] a,dbo.V_规则管理_授课计划明细表 b,dbo.V_规则管理_授课计划主表 c,dbo.V_学生基本数据子类 d,dbo.V_基础信息_班级信息表 e " +
			"where a.相关编号=b.授课计划明细编号 and b.授课计划主表编号=c.授课计划主表编号 and a.学号=d.学号 and d.行政班代码=e.班级编号 " +
			"and a.总评<60 and a.总评 in ('-2','-3','-4','-5','-10','-12') and c.学年学期编码='"+MyTools.fixSql(xnxq+jxxz)+"' " +
			"and d.学生状态 in ('01','05') order by b.课程名称 ";
		vec = db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			for(int i=0;i<vec.size();i=i+7){
				//判断和上一条课程名称是否相同
				if(i<vec.size()){
					if(i!=0){
						if(vec.get(i+3).toString().equals(vec.get(i+3-7).toString())){//课程名称相等
							//设置课表标题行列字体大小
							fontSize = 12;
							wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).getFont().setSize(fontSize);
							
							for(int colNum=0; colNum<8; colNum++){
								cell = wb.openSheet("Sheet1").openCell(colName[colNum]+(counum+stunum));//当前单元格
								if(colNum==0){
									cellContent=stunum+"";
								}else{
									cellContent=vec.get(i+colNum-1).toString();
									if(colNum==4){//学科
										double celllength=getLength(cellContent);
										//System.out.println(counum+"--len:--"+celllength);
										if(celllength>12){
											if(celllength>15){
												if(celllength>18){
													fontSize = 7;
													wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
												}else{
													fontSize = 8;
													wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
												}
											}else{
												fontSize = 10;
												wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
											}
										}else{
											fontSize = 12;
											wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
										}
									}
								}
								cell.setValue(cellContent);
							}
							
							stunum++;
							
							//设置课表标题行列字体大小
							fontSize = 12;
							wb.openSheet("Sheet1").openTable(colName[0]+(counum+stunum)+":"+colName[7]+(counum+30)).getFont().setSize(fontSize);
							
							if(i==vec.size()-7){//最后一条
								do{
									cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum+stunum));//当前单元格
									cellContent=stunum+"";
									cell.setValue(cellContent);
									stunum++;
								}while(stunum<31);
								//31行
								wb.openSheet("Sheet1").openTable(colName[3]+(counum+stunum)+":"+colName[6]+(counum+stunum)).merge();
								cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+stunum));//当前单元格
								cellContent="阅卷教师签名 ：__________";
								cell.setValue(cellContent);
								stunum++;
								//32行
								wb.openSheet("Sheet1").openTable(colName[3]+(counum+stunum)+":"+colName[6]+(counum+stunum)).merge();
								cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+stunum));//当前单元格
								cellContent="阅 卷 日 期  ：__________";
								cell.setValue(cellContent);
								stunum++;
								//33行
								wb.openSheet("Sheet1").openTable(colName[1]+(counum+stunum)+":"+colName[6]+(counum+stunum)).merge();
								cell = wb.openSheet("Sheet1").openCell(colName[1]+(counum+stunum));//当前单元格
								cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
								cell.setValue(cellContent);
								stunum++;
								
								
								//设置课表边框线
								Border border = wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+(counum+30)).getBorder();
								//设置表格边框的宽度、颜色
								border.setWeight(XlBorderWeight.xlThin);
								border.setLineColor(Color.black);
								//设置标题字体大小
								cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum-2));
								cell.getFont().setBold(true);
								fontSize = 18;
								cell.getFont().setSize(fontSize);
								cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum-1));
								cell.getFont().setBold(true);
								fontSize = 18;
								cell.getFont().setSize(fontSize);
								cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+31));
								fontSize = 11;
								cell.getFont().setSize(fontSize);
								cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+32));
								fontSize = 11;
								cell.getFont().setSize(fontSize);
								cell = wb.openSheet("Sheet1").openCell(colName[1]+(counum+33));
								cell.getFont().setBold(true);
								fontSize = 11;
								cell.getFont().setSize(fontSize);
								
								//设置表格行高
								wb.openSheet("Sheet1").openTable(colName[0]+(counum-2)+":"+colName[7]+(counum-2)).setRowHeight(22.5);
								wb.openSheet("Sheet1").openTable(colName[0]+(counum-1)+":"+colName[7]+(counum-1)).setRowHeight(22.5);
								wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).setRowHeight(30);
								wb.openSheet("Sheet1").openTable(colName[0]+(counum+1)+":"+colName[7]+(counum+30)).setRowHeight(18);
								wb.openSheet("Sheet1").openTable(colName[0]+(counum+31)+":"+colName[7]+(counum+31)).setRowHeight(21);
								wb.openSheet("Sheet1").openTable(colName[0]+(counum+32)+":"+colName[7]+(counum+32)).setRowHeight(15);
								wb.openSheet("Sheet1").openTable(colName[0]+(counum+33)+":"+colName[7]+(counum+33)).setRowHeight(21);
							}
						}else{
							do{
								cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum+stunum));//当前单元格
								cellContent=stunum+"";
								cell.setValue(cellContent);
								stunum++;
							}while(stunum<31);
							//31行
							wb.openSheet("Sheet1").openTable(colName[3]+(counum+stunum)+":"+colName[6]+(counum+stunum)).merge();
							cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+stunum));//当前单元格
							cellContent="阅卷教师签名 ：__________";
							cell.setValue(cellContent);
							stunum++;
							//32行
							wb.openSheet("Sheet1").openTable(colName[3]+(counum+stunum)+":"+colName[6]+(counum+stunum)).merge();
							cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+stunum));//当前单元格
							cellContent="阅 卷 日 期  ：__________";
							cell.setValue(cellContent);
							stunum++;
							//33行
							wb.openSheet("Sheet1").openTable(colName[1]+(counum+stunum)+":"+colName[6]+(counum+stunum)).merge();
							cell = wb.openSheet("Sheet1").openCell(colName[1]+(counum+stunum));//当前单元格
							cellContent="注：阅卷教师应将本名册及试卷一周内直接交教务处。";
							cell.setValue(cellContent);
							stunum++;
							
							counum=counum+36-3;	
							stunum=1;	
							//生成标题
							//第一行
							counum++;
							wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).merge();
							cell = wb.openSheet("Sheet1").openCell(colName[0]+counum);
							
							cellContent = schoolName + ""+xn+"学年度第"+xq+"学期";
								
							//maxWidth = 4*cellContent.length()/(vector.size()+1);
							cell.setValue(cellContent);
							
							//第2行
							counum++;
							wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).merge();
							cell = wb.openSheet("Sheet1").openCell(colName[0]+counum);//当前单元格
							cellContent="补考考试名册";
							cell.setValue(cellContent);	
						
							//第3行
							counum++;
							for(int colNum=0; colNum<8; colNum++){
								cell = wb.openSheet("Sheet1").openCell(colName[colNum]+counum);//当前单元格
								if(colNum==0){
									cellContent="编号";
								}else if(colNum==1){
									cellContent="学号";
								}else if(colNum==2){
									cellContent="姓名";
								}else if(colNum==3){
									cellContent="班级";
								}else if(colNum==4){
									cellContent="学科";
								}else if(colNum==5){
									cellContent="学分";
								}else if(colNum==6){
									cellContent="补考分数";
								}else if(colNum==7){
									cellContent="考生签名";
								}
								cell.setValue(cellContent);		
							}
							
							//设置课表标题行列字体大小
							fontSize = 12;
							wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).getFont().setSize(fontSize);
							
							for(int colNum=0; colNum<8; colNum++){
								cell = wb.openSheet("Sheet1").openCell(colName[colNum]+(counum+stunum));//当前单元格
								if(colNum==0){
									cellContent=stunum+"";
								}else{
									cellContent=vec.get(i+colNum-1).toString();
									if(colNum==4){//学科
										double celllength=getLength(cellContent);
										//System.out.println(counum+"--len:--"+celllength);
										if(celllength>12){
											if(celllength>15){
												if(celllength>18){
													fontSize = 7;
													wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
												}else{
													fontSize = 8;
													wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
												}
											}else{
												fontSize = 10;
												wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
											}
										}else{
											fontSize = 12;
											wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
										}
									}
								}
								cell.setValue(cellContent);
							}
							
							stunum++;
							
							//设置课表标题行列字体大小
							fontSize = 12;
							wb.openSheet("Sheet1").openTable(colName[0]+(counum+stunum)+":"+colName[7]+(counum+30)).getFont().setSize(fontSize);
							//设置课表边框线
							Border border = wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+(counum+30)).getBorder();
							//设置表格边框的宽度、颜色
							border.setWeight(XlBorderWeight.xlThin);
							border.setLineColor(Color.black);
							//设置标题字体大小
							cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum-2));
							cell.getFont().setBold(true);
							fontSize = 18;
							cell.getFont().setSize(fontSize);
							cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum-1));
							cell.getFont().setBold(true);
							fontSize = 18;
							cell.getFont().setSize(fontSize);
							cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+31));
							fontSize = 11;
							cell.getFont().setSize(fontSize);
							cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+32));
							fontSize = 11;
							cell.getFont().setSize(fontSize);
							cell = wb.openSheet("Sheet1").openCell(colName[1]+(counum+33));
							cell.getFont().setBold(true);
							fontSize = 11;
							cell.getFont().setSize(fontSize);
							
							//设置表格行高
							wb.openSheet("Sheet1").openTable(colName[0]+(counum-2)+":"+colName[7]+(counum-2)).setRowHeight(22.5);
							wb.openSheet("Sheet1").openTable(colName[0]+(counum-1)+":"+colName[7]+(counum-1)).setRowHeight(22.5);
							wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).setRowHeight(30);
							wb.openSheet("Sheet1").openTable(colName[0]+(counum+1)+":"+colName[7]+(counum+30)).setRowHeight(18);
							wb.openSheet("Sheet1").openTable(colName[0]+(counum+31)+":"+colName[7]+(counum+31)).setRowHeight(21);
							wb.openSheet("Sheet1").openTable(colName[0]+(counum+32)+":"+colName[7]+(counum+32)).setRowHeight(15);
							wb.openSheet("Sheet1").openTable(colName[0]+(counum+33)+":"+colName[7]+(counum+33)).setRowHeight(21);
						}	
					}else{//第一条
						//生成标题
						//第一行
						counum++;
						wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).merge();
						cell = wb.openSheet("Sheet1").openCell(colName[0]+counum);
						
						cellContent = schoolName + ""+xn+"学年度第"+xq+"学期";
							
						//maxWidth = 4*cellContent.length()/(vector.size()+1);
						cell.setValue(cellContent);
						
						//第2行
						counum++;
						wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).merge();
						cell = wb.openSheet("Sheet1").openCell(colName[0]+counum);//当前单元格
						cellContent="补考考试名册";
						cell.setValue(cellContent);	
					
						//第3行
						counum++;
						for(int colNum=0; colNum<8; colNum++){
							cell = wb.openSheet("Sheet1").openCell(colName[colNum]+counum);//当前单元格
							if(colNum==0){
								cellContent="编号";
							}else if(colNum==1){
								cellContent="学号";
							}else if(colNum==2){
								cellContent="姓名";
							}else if(colNum==3){
								cellContent="班级";
							}else if(colNum==4){
								cellContent="学科";
							}else if(colNum==5){
								cellContent="学分";
							}else if(colNum==6){
								cellContent="补考分数";
							}else if(colNum==7){
								cellContent="考生签名";
							}
							cell.setValue(cellContent);		
						}
						
						//设置课表标题行列字体大小
						fontSize = 12;
						wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).getFont().setSize(fontSize);
						
						for(int colNum=0; colNum<8; colNum++){
							cell = wb.openSheet("Sheet1").openCell(colName[colNum]+(counum+stunum));//当前单元格
							if(colNum==0){
								cellContent=stunum+"";
							}else{
								cellContent=vec.get(i+colNum-1).toString();
								if(colNum==4){////学科
									double celllength=getLength(cellContent);
									//System.out.println(counum+"--len:--"+celllength);
									if(celllength>12){
										if(celllength>15){
											if(celllength>18){
												fontSize = 7;
												wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
											}else{
												fontSize = 8;
												wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
											}
										}else{
											fontSize = 10;
											wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
										}
									}else{
										fontSize = 12;
										wb.openSheet("Sheet1").openTable(colName[4]+(counum+stunum)+":"+colName[4]+(counum+stunum)).getFont().setSize(fontSize);
									}
								}
							}
							cell.setValue(cellContent);
						}
						
						stunum++;
						
						//设置课表标题行列字体大小
						fontSize = 12;
						wb.openSheet("Sheet1").openTable(colName[0]+(counum+stunum)+":"+colName[7]+30).getFont().setSize(fontSize);
						//设置课表边框线
						Border border = wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+(counum+30)).getBorder();
						//设置表格边框的宽度、颜色
						border.setWeight(XlBorderWeight.xlThin);
						border.setLineColor(Color.black);						
						//设置标题字体大小
						cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum-2));
						cell.getFont().setBold(true);
						fontSize = 18;
						cell.getFont().setSize(fontSize);
						cell = wb.openSheet("Sheet1").openCell(colName[0]+(counum-1));
						cell.getFont().setBold(true);
						fontSize = 18;
						cell.getFont().setSize(fontSize);
						cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+31));
						fontSize = 11;
						cell.getFont().setSize(fontSize);
						cell = wb.openSheet("Sheet1").openCell(colName[3]+(counum+32));
						fontSize = 11;
						cell.getFont().setSize(fontSize);
						cell = wb.openSheet("Sheet1").openCell(colName[1]+(counum+33));
						cell.getFont().setBold(true);
						fontSize = 11;
						cell.getFont().setSize(fontSize);
						
						//设置表格行高
						wb.openSheet("Sheet1").openTable(colName[0]+(counum-2)+":"+colName[7]+(counum-2)).setRowHeight(22.5);
						wb.openSheet("Sheet1").openTable(colName[0]+(counum-1)+":"+colName[7]+(counum-1)).setRowHeight(22.5);
						wb.openSheet("Sheet1").openTable(colName[0]+counum+":"+colName[7]+counum).setRowHeight(30);
						wb.openSheet("Sheet1").openTable(colName[0]+(counum+1)+":"+colName[7]+(counum+30)).setRowHeight(18);
						wb.openSheet("Sheet1").openTable(colName[0]+(counum+31)+":"+colName[7]+(counum+31)).setRowHeight(21);
						wb.openSheet("Sheet1").openTable(colName[0]+(counum+32)+":"+colName[7]+(counum+32)).setRowHeight(15);
						wb.openSheet("Sheet1").openTable(colName[0]+(counum+33)+":"+colName[7]+(counum+33)).setRowHeight(21);
					}
				}			
			}
		}
		
		

		zjs=counum+33;//行数
			
		//设置单元格的水平对齐方式
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[7]+zjs).setHorizontalAlignment(XlHAlign.xlHAlignCenter);

		//设置单元格的垂直对齐方式
		//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
		
		//设置表格列宽
		wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(4);
		wb.openSheet("Sheet1").openTable(colName[1]+"1:"+colName[1]+"1").setColumnWidth(9.25);
		wb.openSheet("Sheet1").openTable(colName[2]+"1:"+colName[2]+"1").setColumnWidth(8.38);
		wb.openSheet("Sheet1").openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(11);
		wb.openSheet("Sheet1").openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(24);
		wb.openSheet("Sheet1").openTable(colName[5]+"1:"+colName[5]+"1").setColumnWidth(4.25);
		wb.openSheet("Sheet1").openTable(colName[6]+"1:"+colName[6]+"1").setColumnWidth(10);
		wb.openSheet("Sheet1").openTable(colName[7]+"1:"+colName[7]+"1").setColumnWidth(10);
		

		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		poCtrl1.setWriter(wb);
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String fileName = "template.xls";

		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		//poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(schoolName + "  "+(xnxq+jxxz).substring(0,4)+"学年第"+(xnxq+jxxz).substring(4,5)+"学期 "+skzq+" 补考考试名册");
		
		//打开文件
		poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
	}
	
	/**
	 * 未选考试形式导出
	 * @date:2016-10-27
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public static void exportWXksxs(HttpServletRequest request,String exportType ,String xnxqbm) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		DBSource db = new DBSource(request); //数据库对象
		
		final String weekNameArray[] = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
		final String orderNameArray[] = {"一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十"};
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		String xnxqmc = "";//学年学期名称		
		Workbook wb = new Workbook();
		Cell cell;
		String cellContent = ""; //当前单元格的内容
		String sql="";
		Vector vector=null;
		int zjs=0;
		float fontSize = 0;
		
		sql="select a.课程代码,a.课程名称,a.专业名称,b.考试形式,a.行政班名称,a.上课周期 from dbo.V_考试管理_考试设置 a,dbo.V_考试形式 b,dbo.V_基础信息_教室类型 c where a.考试形式=b.编号 and a.场地类型=c.编号 and a.学年学期编码='"+MyTools.fixSql(xnxqbm)+"'  and (a.考试形式='' or a.考试形式 is null) order by 专业名称,行政班名称,课程名称  ";
		vector=db.GetContextVector(sql);
		if(vector!=null&&vector.size()>0){
			for(int colNum=1; colNum<6; colNum++){
				for(int rowNum=1; rowNum<3; rowNum++){
					//生成标题
					if(colNum==1 && rowNum==1){
						wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[5]+"1").merge();
						cell = wb.openSheet("Sheet1").openCell("A1");
						cellContent = xnxqbm.substring(0, 4)+"学年第"+xnxqbm.substring(4, 5)+"学期 未选考试形式课程";
							
						//maxWidth = 4*cellContent.length()/(vector.size()+1);
						cell.setValue(cellContent);
					}else if(rowNum==2){
						for(int j=0;j<6;j++){
							cell = wb.openSheet("Sheet1").openCell(colName[j]+"2");//当前单元格
							if(j==0){
								cellContent="课程代码";
							}else if(j==1){
								cellContent="课程名称";
							}else if(j==2){
								cellContent="专业名称";
							}else if(j==3){
								cellContent="考试形式";
							}else if(j==4){
								cellContent="行政班名称";
							}else if(j==5){
								cellContent="上课周期";
							}
							cell.setValue(cellContent);
						}
					}else{
					
						
					}
				}
				for(int j=0;j<vector.size();j=j+6){
					for(int k=0;k<6;k++){
						cell = wb.openSheet("Sheet1").openCell(colName[k]+(j/6+3));//当前单元格
						cell.setValue("'"+vector.get(j+k).toString());
					}
				}
			}
			
	//		for(int i=0;i<vec2.size();i=i+7){
	//			System.out.println(vec2.get(i).toString()+""+vec2.get(i+1).toString()+""+vec2.get(i+2).toString()+""+vec2.get(i+3).toString()+""+vec2.get(i+4).toString()+""+vec2.get(i+5).toString()+""+vec2.get(i+6).toString());
	//		}
			
			zjs=vector.size()/6;
			
			//设置单元格的水平对齐方式
			wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[5]+"1").setHorizontalAlignment(XlHAlign.xlHAlignCenter);
			wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[5]+(zjs+2)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
			
			wb.openSheet("Sheet1").openCell(colName[0]+(zjs+3)).setHorizontalAlignment(XlHAlign.xlHAlignCenter);
			
			//设置单元格的垂直对齐方式
			//wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[mzts]+(zjs+2)).setVerticalAlignment(XlVAlign.xlVAlignCenter);
			
			//设置课表边框线
			Border border = wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[5]+(zjs+2)).getBorder();
			//设置表格边框的宽度、颜色
			border.setWeight(XlBorderWeight.xlThin);
			border.setLineColor(Color.black);
			
			//设置标题字体大小
			cell = wb.openSheet("Sheet1").openCell("A1");
			cell.getFont().setBold(true);
			fontSize = 18;
			cell.getFont().setSize(fontSize);
			
			//设置课表标题行列字体大小
			fontSize = 12;
			wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[5]+"2").getFont().setSize(fontSize);
			wb.openSheet("Sheet1").openTable(colName[0]+"2:"+colName[0]+(zjs+1)).getFont().setSize(fontSize);
			//设置课表内容行列字体大小
			fontSize = 10;
			//设置表格列宽
			wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[0]+"1").setColumnWidth(15);
	
			wb.openSheet("Sheet1").openTable(colName[1]+"3:"+colName[5]+(zjs+2)).getFont().setSize(fontSize);
			wb.openSheet("Sheet1").openTable(colName[1]+"1:"+colName[2]+"1").setColumnWidth(25);
			wb.openSheet("Sheet1").openTable(colName[4]+"1:"+colName[4]+"1").setColumnWidth(30);
			wb.openSheet("Sheet1").openTable(colName[3]+"1:"+colName[3]+"1").setColumnWidth(10);
			wb.openSheet("Sheet1").openTable(colName[5]+"1:"+colName[5]+"1").setColumnWidth(10);
			//设置表格行高
			wb.openSheet("Sheet1").openTable(colName[0]+"1:"+colName[5]+"2").setRowHeight(40);
			wb.openSheet("Sheet1").openTable(colName[0]+"3:"+colName[5]+(zjs+2)).setRowHeight(20);
			
			PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
			poCtrl1.setWriter(wb);
			poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
			
			String fileName = "template.xls";

			//创建自定义菜单栏
			poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
			//poCtrl1.addCustomToolButton("-", "", 0);
			//poCtrl1.addCustomToolButton("打印", "print()", 6);
			poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
			//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
			poCtrl1.setMenubar(false);//隐藏菜单栏
			poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
			
			poCtrl1.setCaption(xnxqbm.substring(0, 4)+"学年第"+xnxqbm.substring(4, 5)+"学期 未选考试形式课程");
			
			//打开文件
			poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
			poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		}else{
			
			PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
			poCtrl1.setWriter(wb);
			poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
			
			String fileName = "template.xls";

			//创建自定义菜单栏
			poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
			//poCtrl1.addCustomToolButton("-", "", 0);
			//poCtrl1.addCustomToolButton("打印", "print()", 6);
			poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
			//poCtrl1.addCustomToolButton("返回", "goBack()", 3);
			poCtrl1.setMenubar(false);//隐藏菜单栏
			poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
			
			poCtrl1.setCaption(xnxqbm.substring(0, 4)+"学年第"+xnxqbm.substring(4, 5)+"学期 未选考试形式课程");
			
			//打开文件
			poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
			poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		}
		
		
		
	}
	
	
	/**  
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5  
	 * @param String s 需要得到长度的字符串  
	 * @return int 得到的字符串长度  
	 */   
	public static double getLength(String s) {  
	    double valueLength = 0;    
	       String chinese = "[\u4e00-\u9fa5]";    
	       // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1    
	       for (int i = 0; i < s.length(); i++) {    
	           // 获取一个字符    
	           String temp = s.substring(i, i + 1);    
	           // 判断是否为中文字符    
	           if (temp.matches(chinese)) {    
	               // 中文字符长度为1    
	               valueLength += 1;    
	           } else {    
	               // 其他字符长度为0.5    
	               valueLength += 0.5;    
	           }    
	       }    
	       //进位取整    
	       return  Math.ceil(valueLength);    
	 }  
	
	/**  
	 * 获取列的字母
	 * @param 
	 * @return String 得到的字符串长度  
	 */   
	public static String getcolName(int num) { 
		//excel列 
		final String colName[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int count = 26;
		num=num+1;
		//System.out.println("num/count=" + num/count);  
		String out = "";  
		if(num/count != 0){   		
			if(num%count == 0) {    
				if(num/count==1){
					out = colName[num%count+25];    
				}else{
					out = colName[num/count-2]; 
					out = out + colName[num%count+25];    
				}	
				//System.out.println(out);   
			}else{    
				out = colName[num/count-1];   
				out = out + colName[num%count-1];    
				//System.out.println(out);   
			}  
		}else{   
			out = colName[num-1];   
			//System.out.println(out);  
		}
		
		return out;
	}
	
}
