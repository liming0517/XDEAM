package com.pantech.src.devolop.outsideExam;
/*
@date 2016.08.03
@author lupengfei
模块：M11外考系统
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
  
import de.innosystec.unrar.Archive;  
import de.innosystec.unrar.rarfile.FileHeader; 

public class OutsideExamBean {
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	private String USERCODE;//用户编号
	private String WK_KSBH;  //外考科目编号
	private String WK_KSMC;  //外考科目名称
	private String WK_BMKSSJ;  //报名开始时间
	private String WK_BMJSSJ;  //报名结束时间
	private String WK_KSRQ;  //考试日期
	private String WK_BMFY;  //报名费用
	private String WK_BZ;  //备注
	

	public OutsideExamBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}

	public void initialData() {
		WK_KSBH="";  //外考科目编号
		WK_KSMC="";  //外考科目名称
		WK_BMKSSJ="";  //报名开始时间
		WK_BMJSSJ="";  //报名结束时间
		WK_KSRQ="";  //考试日期
		WK_BMFY="";  //报名费用
		WK_BZ="";  //备注
		MSG = "";    //提示信息
	}

	/**
	 * 新建外考考试
	 * @date:2016-08-04
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void newOutsideExam() throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String maxNewId="";
		final String classID[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int classnum=0;//同一门课程班级数量
		String classname="";//选修班名称
		String sjxl="";//上课时间序列
		int xl=0;
		int classtag=0;
			
		//选修课明细编号
		sql4="select count(*) from [dbo].[V_外考管理_外考科目设置] ";
		if(db.getResultFromDB(sql4)){
			sql3="select max(cast(SUBSTRING([外考考试编号],6,9) as bigint)) from [dbo].[V_外考管理_外考科目设置] ";
			vec3 = db.GetContextVector(sql3);
			if (vec3 != null && vec3.size() > 0) {
				maxNewId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec3.get(0))))+1);
				this.setWK_KSBH("WKMX_"+maxNewId);//设置编号
			}
		}else{
			this.setWK_KSBH("WKMX_100000001");//设置编号
		}
		
		//添加外考考试信息
		sql5=" insert into [dbo].[V_外考管理_外考科目设置] ([外考考试编号],[外考考试名称],[报名开始时间],[报名结束时间],[考试日期],[报名费用],[报名人数],[备注]) values (" +
			 "'"+MyTools.fixSql(this.getWK_KSBH())+"'," +//外考考试编号
			 "'"+MyTools.fixSql(this.getWK_KSMC())+"'," +//外考考试名称
			 "'"+MyTools.fixSql(this.getWK_BMKSSJ())+"'," +//报名开始时间
			 "'"+MyTools.fixSql(this.getWK_BMJSSJ())+"'," +//报名结束时间
			 "'"+MyTools.fixSql(this.getWK_KSRQ())+"'," +//考试日期
			 "'"+MyTools.fixSql(this.getWK_BMFY())+"'," +//报名费用
			 "'0'," +//报名人数
			 "'" + MyTools.fixSql(this.getWK_BZ()) + "') " ;//备注
			
		
		
		//添加科目课程信息表
//		sql3="select [学年学期名称] from [dbo].[V_规则管理_学年学期表] where [学年学期编码] = '"+MyTools.fixSql(this.getXX_XNXQ())+"' ";
//		vec3=db.GetContextVector(sql3);
//		
//		sql5+=" insert into [dbo].[V_成绩管理_科目课程信息表] ([学年学期编码],[学年学期名称],[年级代码],[年级名称],[专业代码],[专业名称],[课程代码],[课程名称],[课程类型],[科目类型],[是否参与绩点],[总评比例选项],[平时比例],[期中比例],[实训比例],[期末比例],[成绩类型],[实训],[创建人],[创建时间],[状态]) values (" +
//			  "'"+MyTools.fixSql(this.getXX_XNXQ())+"'," +//学年学期编码
//			  "'"+MyTools.fixSql(vec3.get(0).toString())+"'," +//学年学期名称
//			  "'"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"1'," +//年级代码
//			  "'"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"级'," +//年级名称
//			  "''," +//专业代码
//			  "''," +//专业名称
//			  "'"+MyTools.fixSql(this.getXX_KCDM())+"'," +//课程代码
//			  "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程名称
//			  "'3'," +//课程类型
//			  "''," +//科目类型
//			  "'1'," +//是否参与绩点
//			  "'4'," +//总评比例选项
//			  "'40'," +//平时比例
//			  "''," +//期中比例
//			  "''," +//实训比例
//			  "'60'," +//期末比例
//			  "'1'," +//成绩类型
//			  "'0'," +//实训
//			  "'post'," +//创建人
//			  "getdate()," +//创建时间
//			  "'1') ";//状态
		
		//添加登分教师信息表
//		sql2="select max(cast([科目编号] as bigint)) from [dbo].[V_成绩管理_科目课程信息表]";
//		vec2 = db.GetContextVector(sql2);
//		if (vec2 != null && vec2.size() > 0) {
//			maxNewId = String.valueOf(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec2.get(0))))+1);
//			this.setKMBH(maxNewId);//设置编号
//		}
//		
//		String[] dfjsbh=this.getXX_JSBH().split("\\+");
//		String[] dfjsxm=this.getXX_SKJS().split("\\+");
//		String dfjsid="";
//		String dfjsname="";
//		for(int j=0;j<dfjsbh.length;j++){
//			dfjsid+="@"+dfjsbh[j]+"@,";
//			dfjsname+="@"+dfjsxm[j]+"@,";
//		}
//		dfjsid=dfjsid.substring(0, dfjsid.length()-1);
//		dfjsname=dfjsname.substring(0, dfjsname.length()-1);
//		sql5+=" insert into [dbo].[V_成绩管理_登分教师信息表] ([科目编号],[来源类型],[相关编号],[行政班代码],[行政班名称],[课程代码],[课程名称],[登分教师编号],[登分教师姓名],[打印锁定],[创建人],[创建时间],[状态]) values (" +
//			  "'"+MyTools.fixSql(this.getKMBH())+"'," +//科目编号
//			  "'1'," +//来源类型
//			  "'"+MyTools.fixSql(this.getXX_XXKMXBH())+"'," +//相关编号
//			  "''," +//行政班代码
//			  "'"+MyTools.fixSql(classname)+"'," +//行政班名称
//			  "'"+MyTools.fixSql(this.getXX_KCDM())+"'," +//课程代码
//			  "'"+MyTools.fixSql(this.getXX_KCMC())+"'," +//课程名称
//			  "'"+MyTools.fixSql(dfjsid)+"'," +//登分教师编号
//			  "'"+MyTools.fixSql(dfjsname)+"'," +//登分教师姓名
//			  "'0'," +//打印锁定
//			  "'post'," +//创建人
//			  "getdate()," +//创建时间
//			  "'1')";//状态
		
		if(db.executeInsertOrUpdate(sql5)){
			this.setMSG("新建成功");
		}else{
			this.setMSG("新建失败");
		}

	}
	
	/**
	 * 读取选修课开班信息
	 * @author 2016-08-04
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadOutsideExam(int pageNum,int pageSize,String SC_KSMC) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		sql = " SELECT  [外考考试编号] as WK_KSBH,[外考考试名称] as WK_KSMC,convert(varchar(10),[报名开始时间],21) as WK_BMKSSJ,convert(varchar(10),[报名结束时间],21) as WK_BMJSSJ,convert(varchar(10),[考试日期],21) as WK_KSRQ,[报名费用] as WK_BMFY,[报名人数] as WK_BMRS,[备注] as WK_BEIZHU FROM [dbo].[V_外考管理_外考科目设置] " ;
			
		if(!SC_KSMC.equals("")){
			sql+=" where 外考考试名称 like '%"+MyTools.fixSql(SC_KSMC)+"%' ";
		}
		sql+=" order by 考试日期 desc,外考考试编号  desc ";
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	/**
	 * 编辑外考考试信息
	 * @date:2016-08-04
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void editOutsideExam() throws SQLException {
		String sql = "";
		String sql2 = "";
		String sql3 = "";
		String sql4 = "";
		String sql5 = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		String maxNewId="";
		final String classID[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		int classnum=0;//同一门课程班级数量
		String classname="";//选修班名称
		String sjxl="";//上课时间序列
		int xl=0;
		int classtag=0;
		
		//编辑选修课授课计划
		sql5=" update [dbo].[V_外考管理_外考科目设置] set " +
			 "[外考考试名称]='"+MyTools.fixSql(this.getWK_KSMC())+"'," +
			 "[报名开始时间]='"+MyTools.fixSql(this.getWK_BMKSSJ())+"'," +
			 "[报名结束时间]='"+MyTools.fixSql(this.getWK_BMJSSJ())+"'," +
			 "[考试日期]='"+MyTools.fixSql(this.getWK_KSRQ())+"'," +
			 "[报名费用]='"+MyTools.fixSql(this.getWK_BMFY())+"'," +
			 "[备注]='" + MyTools.fixSql(this.getWK_BZ()) + "' " +
			 "where [外考考试编号]='"+MyTools.fixSql(this.getWK_KSBH())+"' ";
			
		
		//编辑科目课程信息表
//		sql3="select [学年学期名称] from [dbo].[V_规则管理_学年学期表] where [学年学期编码] = '"+MyTools.fixSql(this.getXX_XNXQ())+"' ";
//		vec3=db.GetContextVector(sql3);
//		
//		sql5+="update [dbo].[V_成绩管理_科目课程信息表] set " +
//			  "[学年学期编码]='"+MyTools.fixSql(this.getXX_XNXQ())+"'," +
//			  "[学年学期名称]='"+MyTools.fixSql(vec3.get(0).toString())+"'," +
//			  "[年级代码]='"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"1'," +
//			  "[年级名称]='"+(Integer.parseInt(this.getXX_XNXQ().substring(2, 4))-1)+"级'," +
//			  "[课程类型]='"+MyTools.fixSql(this.getXX_KCLX().replaceAll("0", ""))+"'," +
//			  "[创建时间]=getdate() " +
//			  " where [科目编号] in (select [科目编号] from [dbo].[V_成绩管理_登分教师信息表] where [相关编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ) ";	
		
		//编辑登分教师信息表
//		String[] dfjsbh=this.getXX_JSBH().split("\\+");
//		String[] dfjsxm=this.getXX_SKJS().split("\\+");
//		String dfjsid="";
//		String dfjsname="";
//		for(int j=0;j<dfjsbh.length;j++){
//			dfjsid+="@"+dfjsbh[j]+"@,";
//			dfjsname+="@"+dfjsxm[j]+"@,";
//		}
//		dfjsid=dfjsid.substring(0, dfjsid.length()-1);
//		dfjsname=dfjsname.substring(0, dfjsname.length()-1);
//		sql5+="update [dbo].[V_成绩管理_登分教师信息表] set " +
//			  "[登分教师编号]='"+MyTools.fixSql(dfjsid)+"',[登分教师姓名]='"+MyTools.fixSql(dfjsname)+"',[创建时间]=getdate() " +
//			  "where [相关编号]='"+MyTools.fixSql(this.getXX_XXKMXBH())+"' ";
	
		if(db.executeInsertOrUpdate(sql5)){
			this.setMSG("编辑成功");
		}else{
			this.setMSG("编辑失败");
		}

	}
	
	/**
	 * 检查考试报名情况
	 * @date:2016-09-08
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void checkExamStu() throws SQLException {
		String sql = "";

		sql=" select count(*) from dbo.V_外考管理_学生报名表 where [外考考试编号] ='"+MyTools.fixSql(this.getWK_KSBH())+"' ";
			
		if(db.getResultFromDB(sql)){
			this.setMSG("已有学生报名考试，删除该考试将删除所有学生报名信息，是否确认删除这门考试？");
		}else{
			this.setMSG("0");
		}
	}
	
	/**
	 * 删除选修课开班
	 * @date:2016-08-05
	 * @author:lupengfei
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void delOutsideExam() throws SQLException {
		String sql = "";
		String sql2 = "";
		Vector vec = null;

		//sql="select count(*) from [dbo].[V_成绩管理_学生成绩信息表] where [相关编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
		//if(db.getResultFromDB(sql)){//该班级已添加成绩信息，无法删除
			//this.setMSG("该班级已添加成绩信息，无法删除");
		//}else{//删除开班信息

			sql=" delete from [dbo].[V_外考管理_外考科目设置] where [外考考试编号] ='"+MyTools.fixSql(this.getWK_KSBH())+"' ";
			sql+=" delete from dbo.V_外考管理_学生报名表 where [外考考试编号] ='"+MyTools.fixSql(this.getWK_KSBH())+"' ";
			//sql2+=" delete from [dbo].[V_规则管理_选修课授课计划主表] where [授课计划主表编号] in (select [授课计划主表编号] from [dbo].[V_规则管理_选修课授课计划明细表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"' ) ";
			//sql2+=" delete from [dbo].[V_规则管理_选修课授课计划明细表] where [授课计划明细编号]='"+MyTools.fixSql(skjhmxbh)+"' ";
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("删除成功");
			}else{
				this.setMSG("删除失败");
			}
		//}
	}
	
	/**
	 * 查询选课信息
	 * @date:2016-04-18
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public Vector loadSelection(int pageNum, int page, String iUSERCODE) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec14=null;
		String kbmwkbh="";//可报名考试编号
		
		//查询当前时间可以报名的考试科目
		sql2="select [外考考试编号],convert(varchar(10),[报名开始时间],21) as 报名开始时间,convert(varchar(10),[报名结束时间],21) as 报名结束时间  from [dbo].[V_外考管理_外考科目设置] ";
		vec2=db.GetContextVector(sql2);
		String day="";
		if(vec2!=null&&vec2.size()>0){
			for(int i=0;i<vec2.size();i=i+3){
				Calendar cal =Calendar.getInstance();
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		        //System.out.println(df.format(cal.getTime()));
		        //System.out.println(vec.get(0).toString());
		        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        day=df.format(cal.getTime());
		        try {
					Date dt1 = df.parse(df.format(cal.getTime()));
					Date dt2 = df.parse(vec2.get(i+1).toString());
					Date dt3 = df.parse(vec2.get(i+2).toString());
					if (dt1.getTime() >= dt2.getTime() && dt1.getTime() <= dt3.getTime()) {
						kbmwkbh+=vec2.get(i).toString()+",";
			        }else{
			        	 
			        } 
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!kbmwkbh.equals("")){
				kbmwkbh=kbmwkbh.substring(0, kbmwkbh.length()-1);
			}
		}
		if(kbmwkbh.equals("")){
			kbmwkbh="";
		}
		
		String[] kbmwkbhs=kbmwkbh.split(",");
		for(int i=0;i<kbmwkbhs.length;i++){
			sql += " SELECT  [外考考试编号],[外考考试名称],convert(varchar(10),[报名开始时间],21) as 报名开始时间,convert(varchar(10),[报名结束时间],21) as 报名结束时间,convert(varchar(10),[考试日期],21) as 考试日期,[报名费用],[报名人数],[备注]," +
					"(select COUNT(*) from V_外考管理_学生报名表 where  [外考考试编号] = '"+kbmwkbhs[i]+"' and 学号='"+MyTools.fixSql(iUSERCODE)+"' ) as 是否报名" +
					" FROM [dbo].[V_外考管理_外考科目设置] " +
					" where [外考考试编号] = '"+kbmwkbhs[i]+"' union "; 
		}
		sql=sql.substring(0,sql.length()-6);
		sql+=" order by 考试日期 desc,外考考试编号  desc ";
		
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 学生报名考试
	 * @date:2016-08-05
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void saveSelection(String iKeyCode,String iUSERCODE) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		
		sql=" insert into dbo.V_外考管理_学生报名表 ([学号],[外考考试编号],[是否交费],[成绩],[创建人],[创建时间],[状态]) values (" +
			"'"+MyTools.fixSql(iUSERCODE)+"'," +//学号
			"'"+MyTools.fixSql(iKeyCode)+"'," +//外考考试编号
			"'0'," +//是否交费
			"''," +//成绩
			"'"+MyTools.fixSql(iUSERCODE)+"'," +//创建人
			"getdate()," +//创建时间
			"'1') ";//状态
		sql += " update V_外考管理_外考科目设置 set 报名人数=报名人数+1 where [外考考试编号]='"+MyTools.fixSql(iKeyCode)+"' "; 
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("报名成功");
		}else{
			this.setMSG("报名失败");
		}
		
	}
	
	/**
	 * 读取学生报名信息
	 * @author 2016-08-10
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridBMInfo(int pageNum,int pageSize,String iUSERCODE) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		String kbmwkbh="";
		
		//查询当前时间可以报名的考试科目
		sql2="select [外考考试编号],convert(varchar(10),[报名开始时间],21) as 报名开始时间,convert(varchar(10),[报名结束时间],21) as 报名结束时间  from [dbo].[V_外考管理_外考科目设置] ";
		vec2=db.GetContextVector(sql2);
		String day="";
		if(vec2!=null&&vec2.size()>0){
			for(int i=0;i<vec2.size();i=i+3){
				Calendar cal =Calendar.getInstance();
				      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				      //System.out.println(df.format(cal.getTime()));
				      //System.out.println(vec.get(0).toString());
				      DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				      day=df.format(cal.getTime());
				      try {
							Date dt1 = df.parse(df.format(cal.getTime()));
							Date dt2 = df.parse(vec2.get(i+1).toString());
							Date dt3 = df.parse(vec2.get(i+2).toString());
							if (dt1.getTime() >= dt2.getTime() && dt1.getTime() <= dt3.getTime()) {
								kbmwkbh+="'"+vec2.get(i).toString()+"',";
					        }else{
					        	 
					        } 
				} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
				}
			}
			kbmwkbh=kbmwkbh.substring(0, kbmwkbh.length()-1);
		}
		
		sql = " SELECT a.外考考试编号,b.学号,c.姓名,a.外考考试名称,convert(varchar(10),a.考试日期,21) as 考试日期,b.是否交费,'1' as 可操作  FROM [dbo].[V_外考管理_外考科目设置] a,dbo.V_外考管理_学生报名表 b,dbo.V_学生基本数据子类 c " +
			  " where a.外考考试编号=b.外考考试编号 and b.学号=c.学号  and b.学号='"+MyTools.fixSql(iUSERCODE)+"' and a.外考考试编号 in ("+kbmwkbh+") " +
			  " union " +
			  " SELECT a.外考考试编号,b.学号,c.姓名,a.外考考试名称,convert(varchar(10),a.考试日期,21) as 考试日期,b.是否交费,'0' as 可操作  FROM [dbo].[V_外考管理_外考科目设置] a,dbo.V_外考管理_学生报名表 b,dbo.V_学生基本数据子类 c " +
			  " where a.外考考试编号=b.外考考试编号 and b.学号=c.学号  and b.学号='"+MyTools.fixSql(iUSERCODE)+"' and a.外考考试编号 not in ("+kbmwkbh+") " +
			  " order by 考试日期 desc " ;
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	/**
	 * 删除外考报名
	 * @date:2016-08-11
	 * @author:lupengfei
	 * @throws WrongSQLException
	 * @throws SQLException
	 */
	public void delWKSelection(String iKeyCode,String iUSERCODE) throws SQLException {		 
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
			
		sql=" delete from dbo.V_外考管理_学生报名表 " +
			" where 学号= '"+MyTools.fixSql(iUSERCODE)+"' " +
			" and 外考考试编号='"+MyTools.fixSql(iKeyCode)+"' ";
	
		sql += " update dbo.V_外考管理_外考科目设置 set 报名人数=报名人数-1 where 外考考试编号='"+MyTools.fixSql(iKeyCode)+"' "; 
		if(db.executeInsertOrUpdate(sql)){
				this.setMSG("删除成功");
		}else{
				this.setMSG("删除失败");
		}
	}
	
	/**
	 * 读取外考报名学生
	 * @author 2016-8-11
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridWKStu(int pageNum,int pageSize) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		
		sql = "select a.[外考考试编号],b.[外考考试名称],b.[考试日期],a.[是否交费],a.[学号],c.[姓名] from dbo.V_外考管理_学生报名表 a,dbo.V_外考管理_外考科目设置 b,dbo.V_学生基本数据子类 c " +
			  " where a.学号=c.学号 and a.外考考试编号=b.外考考试编号 and a.外考考试编号='"+MyTools.fixSql(this.getWK_KSBH())+"' order by a.学号 " ;
		vector = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vector;
	}
	
	/**
	 * 查询外考未报名学生
	 * @author 2016-8-11
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridStuExam(int pageNum,int pageSize,String stuid,String stuname) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		String kbmzybh="";

		sql = "SELECT  a.[学号],a.[姓名],b.[行政班名称] FROM [dbo].[V_学生基本数据子类] a,dbo.V_学校班级数据子类 b " +
			  "where a.[行政班代码]=b.[行政班代码] and a.学生状态 in ('01','05') " +
			  "and a.学号 not in ( select 学号 from [V_外考管理_学生报名表] where [外考考试编号]='"+MyTools.fixSql(this.getWK_KSBH())+"') " ;
			
		if(!stuid.equals("")){
			sql+=" and a.[学号] like '%"+MyTools.fixSql(stuid)+"%' ";
		}
		if(!stuname.equals("")){
			sql+=" and a.[姓名] like '%"+MyTools.fixSql(stuname)+"%' ";
		}
		sql+=" order by a.学号 ";
		vec = dbSource.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 添加所选学生
	 * @author 2016-8-11
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void submitStuExam(int pageNum,int pageSize,String stunumarray,String stunamearray) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		String stunums="";
		String result="";
		String msginfo="";
		
		String[] stunum=stunumarray.split(",");
		String[] stuname=stunamearray.split(",");
		
		for(int i=0;i<stunum.length;i++){
			sql += " insert into dbo.V_外考管理_学生报名表 ([学号],[外考考试编号],[是否交费],[成绩],[创建人],[创建时间],[状态]) values (" +
							"'"+MyTools.fixSql(stunum[i])+"'," +
							"'"+MyTools.fixSql(this.getWK_KSBH())+"'," +
							"'0'," +
							"''," +
							"'"+MyTools.fixSql("post")+"'," +
							"getdate(),'1') ";
			sql += " update dbo.V_外考管理_外考科目设置 set 报名人数=报名人数+1 where 外考考试编号='"+MyTools.fixSql(this.getWK_KSBH())+"' "; 
		}
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("添加成功");
		}else{
			this.setMSG("添加失败");
		}
	}
	
	/**
	 * 删除外考报名学生
	 * @author 2016-8-11
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void delStudentExam(int pageNum,int pageSize,String stuidarray) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		String stunums="";
		
		String[] stuid=stuidarray.split(",");
		for(int i=0;i<stuid.length;i++){
			stunums+="'"+stuid[i]+"',";
		}
		stunums=stunums.substring(0, stunums.length()-1);
		sql = " delete from dbo.V_外考管理_学生报名表 where [外考考试编号]='"+MyTools.fixSql(this.getWK_KSBH())+"' and 学号 in ("+stunums+") " ;
		sql+= " update dbo.V_外考管理_外考科目设置 set 报名人数=报名人数-"+stuid.length+" where [外考考试编号]='"+MyTools.fixSql(this.getWK_KSBH())+"' "; 
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 读取学生报名信息
	 * @author 2016-08-10
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector loadGridWKInfo(int pageNum,int pageSize,String xh,String xm,String wkmc) throws SQLException {
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		String xnxqbm="";
			
		sql = " SELECT a.外考考试编号,b.学号,c.姓名,a.外考考试名称,convert(varchar(10),a.考试日期,21) as 考试日期,a.报名费用,b.是否交费,b.成绩,'1' as 可操作  FROM [dbo].[V_外考管理_外考科目设置] a,dbo.V_外考管理_学生报名表 b,dbo.V_学生基本数据子类 c " +
				  " where a.外考考试编号=b.外考考试编号 and b.学号=c.学号  " ;
		if(!xh.equals("")){
			sql+=" and b.学号 like '%"+MyTools.fixSql(xh)+"%' ";
		}
		if(!xm.equals("")){
			sql+=" and c.姓名 like '%"+MyTools.fixSql(xm)+"%' ";
		}
		if(!wkmc.equals("")){
			sql+=" and a.外考考试名称 like '%"+MyTools.fixSql(wkmc)+"%' ";
		}
		sql+=" order by b.学号,a.考试日期 desc ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 检查报名情况
	 * @author 2016-09-02
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void checkBM(int pageNum,int pageSize,String iKeyCode,String iUSERCODE) throws SQLException {
		String sql = ""; // 查询用SQL语句
		String sql2 = ""; 
		Vector vec = null; // 结果集
		Vector vec2 = null; // 结果集
		String xnxqbm="";
			
		sql = " select count(*) from V_外考管理_学生报名表 where [学号]='"+MyTools.fixSql(iUSERCODE)+"' and [外考考试编号]='"+MyTools.fixSql(iKeyCode)+"' ";
		if(db.getResultFromDB(sql)){
			this.setMSG("您已经报名过这门考试了");
		}else{
			this.setMSG("yes");
		}
	}
	
	/**
	 * 获取照片路径
	 * @author 2016-8-15
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void getPhotoPath(String iUSERCODE) throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vec = null;
		String sql = "";
		sql="select [照片路径] from V_学生基本数据子类 where 学号='"+MyTools.fixSql(iUSERCODE)+"' ";
		vec=db.GetContextVector(sql);
		if(vec!=null&&vec.size()>0){
			this.setMSG(vec.get(0).toString());
		}
	}
	
	/**
	 * Uploadify上传图片    
	 * 2014/3/31  lupengfei
	 * @param savePath
	 * @param request
	 * @param response
	 */
	public void uploadpic(String savePath, HttpServletRequest request, HttpServletResponse response,String ic_USERCODE){
		
		String icpath="";
		String filePath = "";  //文件路径
		String savepath = savePath + "/studentPhoto" ;
		File f1 = new File(savepath);  
		//当文件夹不存在时创建
		if (!f1.exists()) {  
		    f1.mkdirs();  
		}  
		File folder = new File(savepath);//d:/upload/headPhoto/
		File temp=null;
		File[] filelist= folder.listFiles();//列出文件里所有的文件
		int loc = 0;
		for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
			temp=filelist[i];
			loc = temp.getName().indexOf(getUSERCODE());//获取用户名字符的位置
			if(loc!=-1){//去掉后缀，如果文件名为用户名的话就删除
				temp.delete();//删除文件
			}
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();  
		ServletFileUpload upload = new ServletFileUpload(fac);  
		upload.setHeaderEncoding("utf-8");  
		List fileList = null;  
		try {  
		    fileList = upload.parseRequest(request);  
		    Iterator<FileItem> it = fileList.iterator();  
		    String name = "";   //文件名
		    String extName = "";  //文件后缀名
			while (it.hasNext()) {  
			    FileItem item = it.next();  
			    if (!item.isFormField()) {  
			        name = item.getName();
			        if (name == null || name.trim().equals("")) {  
			            continue;  
			        }  
			        
			        //扩展名格式    
			        if (name.lastIndexOf(".") >= 0) {  
			            extName = name.substring(name.lastIndexOf("."));  
			        }  
			        File file = null;
			        filePath = savepath +"/"+ ic_USERCODE+extName ;  
			        File saveFile = new File(filePath);  
			        item.write(saveFile);
			        
			        //判断上传的文件类型
			        FileInputStream head = new FileInputStream(filePath);
					byte[] b = new byte[4]; 
					head.read(b, 0, b.length); 
					StringBuilder builder = new StringBuilder(); 
					String value = bytesToHexString(b); 
					System.out.println("read:-----------------------"+value);
					head.close(); 
					
					//if(value.equals("FFD8FFE1")||value.equals("FFD8FFE0")||value.equals("89504E47")||value.equals("424D464A")||value.equals("47494638")){
				        this.setMSG(filePath);
					//}else{
					//	this.setMSG("上传失败，上传文件不是图片类型");
					//}
			        
			        
			    }  
			}  
			String sqlpic="update dbo.V_学生基本数据子类 set [照片路径]='"+MyTools.fixSql(ic_USERCODE+extName)+"' where 学号='"+MyTools.fixSql(ic_USERCODE)+"' ";
			if(db.executeInsertOrUpdate(sqlpic)){
				
			}else{
				this.setMSG("保存失败");
			}
			response.getWriter().print("1");
					
		} catch(Exception e){
			try {
				response.getWriter().println("0");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private static String bytesToHexString(byte[] src) { 
		StringBuilder builder = new StringBuilder(); 
		if (src == null || src.length <= 0) { 
		return null; 
		} 
		String hv; 
		for (int i = 0; i < src.length; i++) { 
		// 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写 
		hv = Integer.toHexString(src[i] & 0xFF).toUpperCase(); 
		if (hv.length() < 2) { 
		builder.append(0); 
		} 
		builder.append(hv); 
		} 
		return builder.toString(); 
    } 
	
	/**
	 * createTime:2016.08.16
	 * createUser:lupengfei
	 * description:学生上传照片
	 */
	public void uploadImg(ServletConfig sc,HttpServletResponse response,String iUSERCODE) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		SmartUpload su = new SmartUpload("UTF-8");
		try {
			//初始化
			su.initialize(sc, request, response);
			su.upload();
			//获取配置路径
			String savePath = MyTools.getProp(request, "Base.upLoadPathFile");
			String url = savePath + "/studentPhoto" ;
			File file = new File(url);
			// 根据配置路径来判断没有文件则创建文件
			if (!file.exists()) {
				file.mkdirs();
			}
			//删除该用户名原来的文件
			File folder = new File(url);//d:/upload/headPhoto/
			File temp=null;
			File[] filelist= folder.listFiles();//列出文件里所有的文件
			int loc = 0;
			for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
				temp=filelist[i];
				loc = temp.getName().indexOf(getUSERCODE());//获取用户名字符的位置
				if(loc!=-1){//去掉后缀，如果文件名为用户名的话就删除
					temp.delete();//删除文件
				}
			}
			System.out.println(su.getRequest().getParameter("path"));
			//文件路径
			String path= unescape(su.getRequest().getParameter("path"));
			File f=new File(path);
			//获取文件名称
			String filename=f.getName();
			//获取文件后缀名
			
			String prefix=filename.substring(filename.lastIndexOf(".")+1);
			//File oldFile=new File(url+filename);
			/*System.out.println("bean:修改前文件名称是："+oldFile.getName());//2041089_822249.jpg
			System.out.println("bean:用户名:"+getUSERCODE());//120150002
			System.out.println("bean:文件后缀名"+"."+prefix);//.jpg
*/			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date newdate=new Date();//给图片加时间为防止页面图片不刷新
			String temp1=form.format(newdate);
			System.out.println("时间+++++++++++"+temp1);
			//文件重命名，改成用户名
			//oldFile.renameTo(new File(url+iUSERCODE+temp1+"."+prefix));//
			System.out.println("bean:修改后文件名称是："+url+"/"+iUSERCODE+temp1+"."+prefix);
			
			//存放路径
			su.getFiles().getFile(0).saveAs(url+"/"+iUSERCODE+temp1+"."+prefix);//
			//将文件数据插入数据库中
			String sqlpic="update dbo.V_学生基本数据子类 set [照片路径]='"+MyTools.fixSql(iUSERCODE+temp1+"."+prefix)+"' where 学号='"+MyTools.fixSql(iUSERCODE)+"' ";
			if(db.executeInsertOrUpdate(sqlpic)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			System.out.println("上传失败");
			//this.setMsg("上传失败");
		}
	}
	
	/**
	 * createTime:2016.08.16
	 * createUser:lupengfei
	 * description:学生上传照片
	 */
	public void uploadAllstuPhoto(ServletConfig sc,HttpServletResponse response) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		SmartUpload su = new SmartUpload("UTF-8");
		try {
			//初始化
			su.initialize(sc, request, response);
			su.upload();
			//获取配置路径
			String savePath = MyTools.getProp(request, "Base.upLoadPathFile");
			String url = savePath + "/studentPhoto/" ;
			File file = new File(url);
			// 根据配置路径来判断没有文件则创建文件
			if (!file.exists()) {
				file.mkdirs();
			}
			//删除该用户名原来的文件
			File folder = new File(url);//d:/upload/headPhoto/
			File temp=null;
			File[] filelist= folder.listFiles();//列出文件里所有的文件
			int loc = 0;
			for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
				temp=filelist[i];
				loc = temp.getName().indexOf(getUSERCODE());//获取用户名字符的位置
				if(loc!=-1){//去掉后缀，如果文件名为用户名的话就删除
					//temp.delete();//删除文件
				}
			}
			System.out.println(su.getRequest().getParameter("path"));
			//文件路径
			String path= unescape(su.getRequest().getParameter("path"));
			File f=new File(path);
			//获取文件名称
			String filename=f.getName();
			//获取文件后缀名
			String firname=filename.substring(0,filename.lastIndexOf("."));
			String prefix=filename.substring(filename.lastIndexOf(".")+1);
			//File oldFile=new File(url+filename);
			/*System.out.println("bean:修改前文件名称是："+oldFile.getName());//2041089_822249.jpg
			System.out.println("bean:用户名:"+getUSERCODE());//120150002
			System.out.println("bean:文件后缀名"+"."+prefix);//.jpg
*/			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date newdate=new Date();//给图片加时间为防止页面图片不刷新
			String temp1=form.format(newdate);
			System.out.println("时间+++++++++++"+temp1);
			//文件重命名，改成用户名
			//oldFile.renameTo(new File(url+filename));//
			//System.out.println("bean:修改后文件名称是："+url+"/"+iUSERCODE+temp1+"."+prefix);
			
			//存放路径
			su.getFiles().getFile(0).saveAs(url+filename);//
			
			//解压压缩文件
			File zipFile=new File(url+filename);
			File zipDir=new File(url+firname);
			if(prefix.equalsIgnoreCase("zip")){
				unzip(url+filename,url+firname);
			}else if(prefix.equalsIgnoreCase("rar")){
				unrar(zipFile,zipDir);
			}
			
			//将文件数据插入数据库中
			String sqlpic="";
			int uploadstunum=0;
			int samestunum=0;
			int sameflag=0;
			String errorname="";
			String showmsg="";
			
			File readDir=new File(url+firname+"/"+firname);
			Vector vecpic=new Vector();
			
			String sql="select [学号] from dbo.V_学生基本数据子类 ";
			Vector vec=db.GetContextVector(sql);
			File[] showfile=readDir.listFiles();
			
			for(File photofile:showfile){
				sameflag=0;
				for(int i=0;i<vec.size();i++){
					//学号和图片名称相同
					if(vec.get(i).toString().equalsIgnoreCase(photofile.getName().substring(0, photofile.getName().indexOf(".")))){
						samestunum++;
						sameflag=1;
					}else{
					
					}
				}
				if(sameflag==0){
					errorname+=photofile.getName().substring(0, photofile.getName().indexOf("."))+",";
				}
				
				uploadstunum++;
				String photoname=photofile.getName();
				String photoprefix=photoname.substring(photoname.indexOf(".")+1,photoname.length());
				sqlpic=" update dbo.V_学生基本数据子类 set [照片路径]='"+MyTools.fixSql(photoname.substring(0, photoname.indexOf("."))+"-"+temp1+"."+photoprefix)+"' where 学号='"+MyTools.fixSql(photoname.substring(0, photoname.indexOf(".")))+"' ";
				vecpic.add(sqlpic);
				
				//将文件另存为filename+时间+prefix形式
				File oldfile=new File(url+firname+"/"+filename.substring(0,filename.indexOf("."))+"/"+photoname);
				File newfile=new File(url+photoname.substring(0, photoname.indexOf("."))+"-"+temp1+"."+photoprefix);
				oldfile.renameTo(newfile);
			}
			
			if(!errorname.equals("")){
				errorname=errorname.substring(0, errorname.length()-1);
				showmsg="上传成功<br />共上传 "+uploadstunum+" 张照片<br />匹配成功 "+samestunum+" 张照片<br />"+errorname+"未匹配成功";
			}else{
				showmsg="上传成功<br />共上传 "+uploadstunum+" 张照片<br />匹配成功 "+samestunum+" 张照片";
			}

			readDir.delete();
			zipDir.delete();//删除解压的文件夹
			zipFile.delete();//删除压缩文件
			
			if(db.executeInsertOrUpdateTransaction(vecpic)){
				this.setMSG(showmsg);
			}else{
				this.setMSG("上传失败");
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			System.out.println("上传失败");
			//this.setMsg("上传失败");
		}
	}
	
	/**   
	 * 解压zip格式压缩包   
	 * 对应的是ant.jar   
	 */    
	private static void unzip(String sourceZip,String destDir) throws Exception{     
	    try{     
	        Project p = new Project();     
	        Expand e = new Expand();     
	        e.setProject(p);     
	        e.setSrc(new File(sourceZip));     
	        e.setOverwrite(false);     
	        e.setDest(new File(destDir));     
	    /*   
	     ant下的zip工具默认压缩编码为UTF-8编码，   
	               而winRAR软件压缩是用的windows默认的GBK或者GB2312编码   
	               所以解压缩时要制定编码格式   
	    */    
	        e.setEncoding("gbk");     
	        e.execute();     
	    }catch(Exception e){     
	        throw e;     
	    }     
	} 
	
	/**   
	 * 解压rar格式压缩包。   
	 * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar   
	 */ 
	public void unrar(File sourceRar, File destDir) throws Exception {  
        Archive archive = null;  
        FileOutputStream fos = null;  
        System.out.println("Starting...");  
        try {  
            archive = new Archive(sourceRar);  
            FileHeader fh = archive.nextFileHeader();  
            int count = 0;  
            File destFileName = null;  
            while (fh != null) {  
                //System.out.println((++count) + ") " + fh.getFileNameString());  
                String compressFileName = fh.getFileNameString().trim();  
                destFileName = new File(destDir.getAbsolutePath() + "/" + compressFileName);  
                if (fh.isDirectory()) {  
                    if (!destFileName.exists()) {  
                        destFileName.mkdirs();  
                    }  
                    fh = archive.nextFileHeader();  
                    continue;  
                }   
                if (!destFileName.getParentFile().exists()) {  
                    destFileName.getParentFile().mkdirs();  
                }  
                fos = new FileOutputStream(destFileName);  
                archive.extractFile(fh, fos);  
                fos.close();  
                fos = null;  
                fh = archive.nextFileHeader();  
            }  
  
            archive.close();  
            archive = null;  
            System.out.println("Finished !");  
        } catch (Exception e) {  
            throw e;  
        } finally {  
            if (fos != null) {  
                try {  
                    fos.close();  
                    fos = null;  
                } catch (Exception e) {  
                    //ignore  
                }  
            }  
            if (archive != null) {  
                try {  
                    archive.close();  
                    archive = null;  
                } catch (Exception e) {  
                    //ignore  
                }  
            }  
        }  
    } 
	
	
	//转换文件路径中的中文字符
	public String unescape(String src) {
			StringBuffer tmp = new StringBuffer();
			tmp.ensureCapacity(src.length());
			int lastPos = 0, pos = 0, nLen = src.length();
			char ch;
			while (lastPos < nLen) {
				pos = src.indexOf("%", lastPos);
				if (pos == lastPos) {
					if (src.charAt(pos + 1) == 'u') {
						ch = (char) Integer.parseInt(
								src.substring(pos + 2, pos + 6), 16);
						tmp.append(ch);
						lastPos = pos + 6;
					} else {
						ch = (char) Integer.parseInt(
								src.substring(pos + 1, pos + 3), 16);
						tmp.append(ch);
						lastPos = pos + 3;
					}
				} else {
					if (pos == -1) {
						tmp.append(src.substring(lastPos));
						lastPos = nLen;
					} else {
						tmp.append(src.substring(lastPos, pos));
						lastPos = pos;
					}
				}
			}
			return tmp.toString();
	}
	
	/**
	 * 获取学生详细信息
	 * @author 2016-09-12
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public Vector getStuInfo(String iUSERCODE) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集

		sql = " select 姓名,case when [性别码]='1' then '男' else '女' end as 性别,学号,b.[行政班名称] as 班级,[出生日期] as 出生年月,[身份证件号] as 身份证号,c.类别名称 as 民族,d.类别名称 as 政治面貌,[本人手机] as 联系电话,[电子信箱] as 电子邮箱,[家庭住址] " +
				"from dbo.V_学生基本数据子类 a " +
				"left join dbo.V_学校班级数据子类 b on a.行政班代码=b.行政班代码  " +
				"left join (select * from [dbo].[V_信息类别_类别操作] where [父类别代码]='MZDM') c on a.民族码=c.描述 " +
				"left join (select * from [dbo].[V_信息类别_类别操作] where [父类别代码]='ZZMMDM') d on a.民族码=d.描述 " +
				"where a.[学号]='"+MyTools.fixSql(iUSERCODE)+"' ";
		vec=db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 检查报名人数
	 * @author 2016-09-18
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public void checkWKBMRS() throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集

		sql = "SELECT COUNT(*) FROM [dbo].[V_外考管理_学生报名表] where [外考考试编号]='"+MyTools.fixSql(this.getWK_KSBH())+"' ";
		if(db.getResultFromDB(sql)){
			this.setMSG("1");
		}else{
			this.setMSG("0");
		}
	}
	
	/**
	 * 外考报名导出
	 * @date:2016-09-13
	 * @author:lupengfei
	 * @throws SQLException
	 */
	public String exportWKClass(String exYear,String exMonth,String exDate,String exTime) throws SQLException{
		String filepath="";
		
		Vector vec = null;
		Vector vec2 = null;
		Vector wzcjVec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String sql2 = "";
		String savePath = MyTools.getProp(request, "Base.exportExcelPath")+"outsideExamExport";
		String filePath=savePath+"/"+exYear+exMonth+exDate+"外考报名确认表.xls";
		System.out.println(filePath);
		
		//创建
		File file = new File(savePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		OutputStream os;
		WritableWorkbook wbook;
		try {
			os = new FileOutputStream(filePath);
			wbook = Workbook.createWorkbook(os);
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableSheet wsheet2 = wbook.createSheet("Sheet2", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			String[] title=new String[]{"序号","学号","身份证号","姓名","性别","班级","系别","学生签字"};
			int stunum=1;//学生行数
			int stuxlh=1;//学生序号
			int stuxlh2=1;//学生序号
			int counum=0;//excel表中行数
			String cellContent = ""; //当前单元格的内容
			int nj=0;
			String xn="";
			String xq="";
			
			sql2="select [外考考试名称],[报名费用] from [V_外考管理_外考科目设置] where [外考考试编号]='"+MyTools.fixSql(this.getWK_KSBH())+"'";
			vec2=db.GetContextVector(sql2);
			
			sql = " select a.学号,a.身份证件号,a.姓名,case when a.性别码='1' then '男' else '女' end as 性别,b.行政班简称 as 班级,e.系名称 as 系别,'' as 学生签字,b.年级代码 from V_学生基本数据子类 a " +
					"left join V_学校班级数据子类 b on a.行政班代码=b.行政班代码  " +
					"left join V_专业基本信息数据子类 c on b.专业代码=c.专业代码 " +
					"left join V_基础信息_系专业关系表 d on c.专业代码=d.专业代码 " +
					"left join V_基础信息_系基础信息表 e on d.系代码=e.系代码 " +
					"left join V_外考管理_学生报名表 f on a.学号=f.学号 " +
					"where f.外考考试编号='"+MyTools.fixSql(this.getWK_KSBH())+"' " +
					"order by b.行政班名称,a.学号 ";
			vec = db.GetContextVector(sql);
			
			if(vec!=null&&vec.size()>0){
				//设置列宽
				//sheet1
				wsheet.setColumnView(0, 6);
				wsheet.setColumnView(1, 10);
				wsheet.setColumnView(2, 18);
				wsheet.setColumnView(3, 10);
				wsheet.setColumnView(4, 6);
				wsheet.setColumnView(5, 10);
				wsheet.setColumnView(6, 14);
				wsheet.setColumnView(7, 11);
				
				//sheet2
				wsheet2.setColumnView(0, 6);
				wsheet2.setColumnView(1, 12);
				wsheet2.setColumnView(2, 22);
				wsheet2.setColumnView(3, 12);
				wsheet2.setColumnView(4, 8);
				wsheet2.setColumnView(5, 18);
				wsheet2.setColumnView(6, 26);
				wsheet2.setColumnView(7, 11);

				for(int i=0;i<vec.size();i=i+8){
					//sheet1
					//判断和上一条班级名称是否相同
					if(i<vec.size()){
						if(i!=0){
							if(vec.get(i+4).toString().equals(vec.get(i+4-8).toString())){//班级名称相等
								//一个班级考试人数>35,添加到下一页
								if(stunum>35){
									//36行
									//边框
									for(int colNum=0; colNum<8; colNum++){	
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										if(colNum==0){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(0, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==1||colNum==4){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==2){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
											cellContent="辅导员签名 ：________";
											content = new Label(2, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==5){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
											cellContent="共计 ：______ 人";
											content = new Label(5, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
									}
									stunum++;
									
									//37行
									for(int colNum=0; colNum<8; colNum++){	
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										if(colNum==0){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(0, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==1||colNum==4){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==2){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
											cellContent="系盖章 ：        ";
											content = new Label(2, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==5){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
											cellContent="共计 ：______ 元";
											content = new Label(5, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
									}
									stunum++;
									
									//38行
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									//边框
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
									
									wsheet.mergeCells(0, counum+stunum, 7, counum+stunum);
									cellContent="注：报名费每人"+vec2.get(1).toString()+"元，请在于"+exYear+"年"+exMonth+"月"+exDate+"日"+exTime+"前将表格和报名费交教务处曹老师。";
									content = new Label(0, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;

									counum=counum+40-2;	
									stunum=1;	
									//生成标题
									//第1行
									counum++;
									//设置课表标题行列字体大小
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									//边框
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									
									wsheet.mergeCells(0, counum, 7, counum);
									cellContent = vec2.get(1).toString()+"考试报名确认表";
									content = new Label(0, counum, cellContent, contentStyle);
									wsheet.addCell(content);
																								
									//第2行
									counum++;
									
									for(int colNum=0; colNum<8; colNum++){
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setWrap(true);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										//边框
										if(colNum==0){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else if(colNum==7){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else{
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}
										cellContent=title[colNum];						
										content = new Label(colNum, counum, cellContent, contentStyle);
										wsheet.addCell(content);	
									}
											
									//字体大小
									for(int colNum=0; colNum<8; colNum++){
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										
										if(colNum==0){
											cellContent=stuxlh+"";
										}else{
											cellContent=vec.get(i+colNum-1).toString();
										}
										//边框
										if(colNum==0){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else if(colNum==7){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else{
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);	
													
									}
									wsheet.setRowView((counum+stunum), 330);
									stunum++;stuxlh++;
																			
									//设置表格行高
									wsheet.setRowView((counum-1), 500);
									wsheet.setRowView(counum, 500);

									wsheet.setRowView((counum+36), 500);
									wsheet.setRowView((counum+37), 500);
									wsheet.setRowView((counum+38), 500);
								}else{
									//添加下一行
									for(int colNum=0; colNum<8; colNum++){
										//设置课表标题行列字体大小
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											
										if(colNum==0){
											cellContent=stuxlh+"";
											
										}else{
											cellContent=vec.get(i+colNum-1).toString();
										}
										//边框
										if(colNum==0){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else if(colNum==7){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}else{
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										}
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}
									wsheet.setRowView((counum+stunum), 330);
									stunum++;stuxlh++;
								}
								if(i==vec.size()-8){//最后一条
									do{	
										for(int colNum=0; colNum<8; colNum++){
											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
											contentStyle = new WritableCellFormat(fontStyle);
											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											//边框
											if(colNum==0){
												cellContent=stuxlh+"";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum==40){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else if(colNum==7){
												cellContent="";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
												if(stunum==40){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else{
												cellContent="";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum==40){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}
											
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
										
										wsheet.setRowView((counum+stunum), 330);
										stunum++;stuxlh++;	
									}while(stunum<36);
									
									//36行
									//边框
									for(int colNum=0; colNum<8; colNum++){	
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										if(colNum==0){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(0, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==1||colNum==4){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==2){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
											cellContent="辅导员签名 ：________";
											content = new Label(2, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==5){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
											cellContent="共计 ：______ 人";
											content = new Label(5, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
									}
									stunum++;
									
									//37行
									for(int colNum=0; colNum<8; colNum++){	
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										if(colNum==0){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(0, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==1||colNum==4){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==2){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
											cellContent="系盖章 ：        ";
											content = new Label(2, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==5){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
											cellContent="共计 ：______ 元";
											content = new Label(5, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
									}
									stunum++;
									
									//38行
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									//边框
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
									
									wsheet.mergeCells(0, counum+stunum, 7, counum+stunum);
									cellContent="注：报名费每人"+vec2.get(1).toString()+"元，请在于"+exYear+"年"+exMonth+"月"+exDate+"日"+exTime+"前将表格和报名费交教务处曹老师。";
									content = new Label(0, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
								}
								//设置表格行高
								wsheet.setRowView((counum-1), 500);
								wsheet.setRowView(counum, 500);

								wsheet.setRowView((counum+36), 500);
								wsheet.setRowView((counum+37), 500);
								wsheet.setRowView((counum+38), 500);
									
							}else{//课程名称不相同				
							
								while(stunum<36){
									for(int colNum=0; colNum<8; colNum++){
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										//边框
										if(colNum==0){
											cellContent=stuxlh+"";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											if(stunum==40){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}else if(colNum==7){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											if(stunum==40){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}else{
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											if(stunum==40){
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
											}else{
												contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											}
										}
										
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}
									
									wsheet.setRowView((counum+stunum), 330);
									stunum++;stuxlh++;
								};
								
								//36行
								//边框
								for(int colNum=0; colNum<8; colNum++){	
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									if(colNum==0){
										cellContent="";
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										content = new Label(0, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}else if(colNum==1||colNum==4){
										cellContent="";
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}else if(colNum==2){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
										cellContent="辅导员签名 ：________";
										content = new Label(2, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}else if(colNum==5){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
										cellContent="共计 ：______ 人";
										content = new Label(5, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}
								}
								stunum++;
								
								//37行
								for(int colNum=0; colNum<8; colNum++){	
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									if(colNum==0){
										cellContent="";
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										content = new Label(0, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}else if(colNum==1||colNum==4){
										cellContent="";
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										content = new Label(colNum, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}else if(colNum==2){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
										cellContent="系盖章 ：        ";
										content = new Label(2, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}else if(colNum==5){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
										wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
										cellContent="共计 ：______ 元";
										content = new Label(5, counum+stunum, cellContent, contentStyle);
										wsheet.addCell(content);
									}
								}
								stunum++;
								
								//38行
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
								//边框
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
								
								wsheet.mergeCells(0, counum+stunum, 7, counum+stunum);
								cellContent="注：报名费每人"+vec2.get(1).toString()+"元，请在于"+exYear+"年"+exMonth+"月"+exDate+"日"+exTime+"前将表格和报名费交教务处曹老师。";
								content = new Label(0, counum+stunum, cellContent, contentStyle);
								wsheet.addCell(content);
								stunum++;
								
								counum=counum+40-2;	
								stunum=1;stuxlh=1;	
								//生成标题
								//第1行
								counum++;
								//设置课表标题行列字体大小
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
								//边框
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								
								wsheet.mergeCells(0, counum, 7, counum);
								cellContent = vec2.get(0).toString()+"考试报名确认表";
								content = new Label(0, counum, cellContent, contentStyle);
								wsheet.addCell(content);
							
								//第2行
								counum++;
								
								for(int colNum=0; colNum<8; colNum++){
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setWrap(true);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									//边框
									if(colNum==0){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}else if(colNum==7){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}else{
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}
									cellContent=title[colNum];						
									content = new Label(colNum, counum, cellContent, contentStyle);
									wsheet.addCell(content);	
								}
										
								for(int colNum=0; colNum<8; colNum++){
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									if(colNum==0){
										cellContent=stuxlh+"";
									}else{
										cellContent=vec.get(i+colNum-1).toString();
//										if(colNum==4){//学科
//											double celllength=getLength(cellContent);
//											//System.out.println(counum+"--len:--"+celllength);
//											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
//											contentStyle = new WritableCellFormat(fontStyle);
//											contentStyle.setShrinkToFit(true);
//											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
//											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
//										}else{
//											
//										}
									}
									//边框
									if(colNum==0){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}else if(colNum==7){
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}else{
										contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
									}
									content = new Label(colNum, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);	
												
								}
								wsheet.setRowView((counum+stunum), 330);
								stunum++;stuxlh++;
								
								if(i==vec.size()-8){//最后一条
									do{	
										for(int colNum=0; colNum<8; colNum++){
											fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
											contentStyle = new WritableCellFormat(fontStyle);
											contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
											contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
											//边框
											if(colNum==0){
												cellContent=stuxlh+"";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum==40){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else if(colNum==7){
												cellContent="";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
												if(stunum==40){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}else{
												cellContent="";
												contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
												contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
												if(stunum==40){
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
												}else{
													contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
												}
											}
											
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
										
										wsheet.setRowView((counum+stunum), 330);
										stunum++;stuxlh++;	
									}while(stunum<36);
									
									//36行
									//边框
									for(int colNum=0; colNum<8; colNum++){	
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										if(colNum==0){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(0, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==1||colNum==4){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==2){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
											cellContent="辅导员签名 ：________";
											content = new Label(2, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==5){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
											cellContent="共计 ：______ 人";
											content = new Label(5, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
									}
									stunum++;
									
									//37行
									for(int colNum=0; colNum<8; colNum++){	
										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
										contentStyle = new WritableCellFormat(fontStyle);
										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										if(colNum==0){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(0, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==1||colNum==4){
											cellContent="";
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											content = new Label(colNum, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==2){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(2, counum+stunum, 3, counum+stunum);
											cellContent="系盖章 ：        ";
											content = new Label(2, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}else if(colNum==5){
											contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
											contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
											contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
											wsheet.mergeCells(5, counum+stunum, 7, counum+stunum);
											cellContent="共计 ：______ 元";
											content = new Label(5, counum+stunum, cellContent, contentStyle);
											wsheet.addCell(content);
										}
									}
									stunum++;
									
									//38行
									fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
									contentStyle = new WritableCellFormat(fontStyle);
									contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
									contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
									
									//边框
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
									
									wsheet.mergeCells(0, counum+stunum, 7, counum+stunum);
									cellContent="注：报名费每人"+vec2.get(1).toString()+"元，请在于"+exYear+"年"+exMonth+"月"+exDate+"日"+exTime+"前将表格和报名费交教务处曹老师。";
									content = new Label(0, counum+stunum, cellContent, contentStyle);
									wsheet.addCell(content);
									stunum++;
									
								}
								
								//设置表格行高
								wsheet.setRowView((counum-1), 500);
								wsheet.setRowView(counum, 500);

								wsheet.setRowView((counum+36), 500);
								wsheet.setRowView((counum+37), 500);
								wsheet.setRowView((counum+38), 500);
							}	
						}else{//第一条
							//生成标题
							//第1行
							
							//设置课表标题行列字体大小
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							
							//边框
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							
							wsheet.mergeCells(0, counum, 7, counum);
							cellContent = vec2.get(0).toString()+"考试报名确认表";
							content = new Label(0, counum, cellContent, contentStyle);
							wsheet.addCell(content);
						
							//第2行
							counum++;
							
							for(int colNum=0; colNum<8; colNum++){
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setWrap(true);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								//边框
								if(colNum==0){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}else if(colNum==7){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}else{
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}								
								cellContent=title[colNum];						
								content = new Label(colNum, counum, cellContent, contentStyle);
								wsheet.addCell(content);	
							}
									
							for(int colNum=0; colNum<8; colNum++){
								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
								contentStyle = new WritableCellFormat(fontStyle);
								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
								
								
								if(colNum==0){
									cellContent=stuxlh+"";
									
								}else{
									cellContent=vec.get(i+colNum-1).toString();
									if(colNum==4){//学科
//										double celllength=getLength(cellContent);
//										//System.out.println(counum+"--len:--"+celllength);
//										fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
//										contentStyle = new WritableCellFormat(fontStyle);
//										contentStyle.setShrinkToFit(true);
//										contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
//										contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
										
									}else{
										
									}
								}
								//边框
								if(colNum==0){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}else if(colNum==7){
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}else{
									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
									contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
								}
								content = new Label(colNum, counum+stunum, cellContent, contentStyle);
								wsheet.addCell(content);
							}
							wsheet.setRowView((counum+stunum), 330);
							stunum++;stuxlh++;
																	
							//设置表格行高
							wsheet.setRowView((counum-1), 500);
							wsheet.setRowView(counum, 500);

							wsheet.setRowView((counum+36), 500);
							wsheet.setRowView((counum+37), 500);
							wsheet.setRowView((counum+38), 500);
					
						}
					}	
					
					//sheet2
					if(i==0){
						//第1行
						wsheet2.mergeCells(0, 0, 7, 0);
						//设置课表标题行列字体大小
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 18, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						//边框
						contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
						contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
						
						cellContent = vec2.get(0).toString()+"考试报名信息表";
						content = new Label(0, 0, cellContent, contentStyle);
						wsheet2.addCell(content);
						wsheet2.setRowView(0, 500);
						
						//第2行					
						for(int colNum=0; colNum<8; colNum++){
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setWrap(true);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							//边框
							if(colNum==0){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}else if(colNum==7){
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}else{
								contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}								
							cellContent=title[colNum];						
							content = new Label(colNum, 1, cellContent, contentStyle);
							wsheet2.addCell(content);	
						}
						wsheet2.setRowView(1, 500);
					}
					
					for(int colNum=0; colNum<8; colNum++){
						fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
						contentStyle = new WritableCellFormat(fontStyle);
						contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
						contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
						
						if(colNum==0){
							cellContent=stuxlh2+"";	
						}else{
							cellContent=vec.get(i+colNum-1).toString();
						}
						//边框
						if(colNum==0){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vec.size()-8){//最后一条
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
						}else if(colNum==7){
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vec.size()-8){//最后一条
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
						}else{
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							if(i==vec.size()-8){//最后一条
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
						}
						content = new Label(colNum, 1+stuxlh2+nj, cellContent, contentStyle);
						wsheet2.addCell(content);
					}
					//设置表格行高
					wsheet2.setRowView((1+stuxlh2+nj), 330);
					stuxlh2++;
					if(i<vec.size()-8){
						if(vec.get(i+7).toString().equals(vec.get(i+7+8).toString())){
							
						}else{
							wsheet2.mergeCells(0, 1+stuxlh2+nj, 7, 1+stuxlh2+nj);
							fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
							contentStyle = new WritableCellFormat(fontStyle);
							contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
							contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
							contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
							contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
							if(i==vec.size()-8){//最后一条
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
							}else{
								contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							}
							content = new Label(0, 1+stuxlh2+nj, cellContent, contentStyle);
							wsheet2.addCell(content);
							
//							for(int colNum=0; colNum<8; colNum++){
//								fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD,false, jxl.format.UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);
//								contentStyle = new WritableCellFormat(fontStyle);
//								contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
//								contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
//								//边框
//								if(colNum==0){
//									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
//									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
//									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
//									if(i==vec.size()-8){//最后一条
//										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
//									}else{
//										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
//									}
//								}else if(colNum==7){
//									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
//									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
//									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
//									if(i==vec.size()-8){//最后一条
//										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
//									}else{
//										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
//									}
//								}else{
//									contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
//									contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
//									contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
//									if(i==vec.size()-8){//最后一条
//										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
//									}else{
//										contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
//									}
//								}
//								content = new Label(colNum, 1+stuxlh2+nj, cellContent, contentStyle);
//								wsheet2.addCell(content);
//							}
							nj++;
						}		
					}
				}//for

			}
			//输出流					
			//this.exportScore("exportExcel", wbook, tempVec, titleArray, title, xzbmc, kcmc, zks, xf, rkjs);

			//写入文件
			wbook.write();
			wbook.close();
			os.close();
			
			this.setMSG("文件生成成功");
			
		} catch (RowsExceededException e1) {
			// TODO Auto-generated catch block
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			this.setMSG("文件生成失败");
		}
		
		return filePath;
	}
	
	/**  
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5  
	 * @param String s 需要得到长度的字符串  
	 * @return int 得到的字符串长度  
	 */   
	public static double getLength(String s) {  
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		
		//获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1    
		for(int i=0; i<s.length(); i++) {    
			//获取一个字符    
			String temp = s.substring(i, i+1);    
			//判断是否为中文字符
			if(temp.matches(chinese)) {    
				//中文字符长度为1    
				valueLength += 1;    
			}else{    
				//其他字符长度为0.5    
				valueLength += 0.5;    
			}    
		}
		
		//进位取整
		return Math.ceil(valueLength);    
	}
	
	
	
	
	//get,set
	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getWK_KSMC() {
		return WK_KSMC;
	}

	public void setWK_KSMC(String wK_KSMC) {
		WK_KSMC = wK_KSMC;
	}

	public String getWK_BMKSSJ() {
		return WK_BMKSSJ;
	}

	public void setWK_BMKSSJ(String wK_BMKSSJ) {
		WK_BMKSSJ = wK_BMKSSJ;
	}

	public String getWK_BMJSSJ() {
		return WK_BMJSSJ;
	}

	public void setWK_BMJSSJ(String wK_BMJSSJ) {
		WK_BMJSSJ = wK_BMJSSJ;
	}

	public String getWK_KSRQ() {
		return WK_KSRQ;
	}

	public void setWK_KSRQ(String wK_KSRQ) {
		WK_KSRQ = wK_KSRQ;
	}

	public String getWK_BMFY() {
		return WK_BMFY;
	}

	public void setWK_BMFY(String wK_BMFY) {
		WK_BMFY = wK_BMFY;
	}

	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getWK_KSBH() {
		return WK_KSBH;
	}

	public void setWK_KSBH(String wK_KSBH) {
		WK_KSBH = wK_KSBH;
	}

	public String getWK_BZ() {
		return WK_BZ;
	}

	public void setWK_BZ(String wK_BZ) {
		WK_BZ = wK_BZ;
	}
}