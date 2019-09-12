package com.pantech.devolop.customExamManage;
/*
@date 2017.10.12
@author zhaixuchao
模块：
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;
import com.pantech.devolop.registerScoreSet.DfryszBean;

public class DfqkBean {
	private String USERCODE; //用户编号
	private String Auth; //用户权限
	
	private String DD_XNXQBH; //学年学期编号
	private String DD_XNXQMC; //学年学期名称
	private String DD_KSBH; //考试编号
	private String DD_KSMC; //考试名称
	private String DD_DFQK; //登分情况
	
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public DfqkBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2017-10-12
	 * @author:zhaixuchao
	 */
	public void initialData() {
		USERCODE = "";; //用户编号
		Auth = "";; //用户权限
		
		DD_XNXQBH = ""; //学年学期编号
		DD_XNXQMC = ""; //学年学期名称
		DD_KSBH = ""; //考试编号
		DD_KSMC = ""; //考试名称
		DD_DFQK = ""; //登分情况
	}

	
	/**
	 * 读取学年学期下拉框
	 * @date:2017-10-12
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnXqCombo() throws SQLException{
		Vector vec = null;
		/*String sql="select '请选择' as comboName,'' as comboValue,0  as orderNum " +
				" union all" +
				" select distinct 学年 as comboName,学年 as comboValue,1" +
				" from V_规则管理_学年学期表" +
				" where 状态='1' order by orderNum,comboValue desc";*/
		
		String sql="select '请选择' as comboName,'' as comboValue,0  as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,1 " +
				"from V_规则管理_学年学期表 " +
				"where 状态='1' order by orderNum,comboValue desc ";
				
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	
	/**
	 * 读取当前学年学期
	 * @date:2017-10-12
	 * @author:zhaixuchao
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException{
		String curXnxq = "";
		Vector vec = null;
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}
		
		return curXnxq;
	}
	
	
	/**
	 * 读取登分情况信息
	 * 
	 * @date:2017-10-12
	 * @author:zhaixuchao
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page, String xnxq, String ksmc, String dfqk) throws SQLException {
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		String xbjyzz = MyTools.getProp(request, "Base.xbjyzz");//系部教研组长
		
		/*String sql="select distinct d.行政班名称 as BJ,c.课程名称 as KC,d.行政班代码 as BJDM, c.课程号 as KCDM, " +
				"case when e.授课教师姓名 is null then h.授课教师姓名 else e.授课教师姓名 end as SKJSXM ,g.系部代码 as XBDM,LEFT(d.行政班代码,2) as PX " +
				"from V_自设考试管理_考试信息表 a " +
				"inner join V_自设考试管理_考试学科信息表 b  on a.考试编号 = b.考试编号 " +
				"left join V_课程数据子类 c  on b.课程代码 = c.课程号 " +
				"left join V_学校班级数据子类 d  on b.班级代码 = d.行政班代码 " +
				"left join V_排课管理_课程表明细详情表 e  on d.行政班代码 = e.行政班代码 and c.课程号 = e.课程代码 " +
				"left join V_基础信息_教研组信息表 as f on b.课程代码=f.学科代码 " +
				"left join V_基础信息_系部教师信息表 as g on g.系部代码=d.系部代码 " +
				"left join (" +
				"select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 " +
				"from V_规则管理_授课计划主表 as t1 " +
				"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as h on h.行政班代码=b.班级代码 and h.课程代码=b.课程代码 and h.学年学期编码=a.学年学期编码" +
				" where 类别编号 ='01'" ;*/
		
		String sql="select t.行政班名称,t.课程名称,t.行政班代码,t.课程号,t.授课教师姓名,t.系部代码,t.学年学期名称,t.考试名称,t.考试编号,t.需登分人数,t.已登分人数 from ("+
				"select distinct d.行政班名称,c.课程名称,d.行政班代码 , c.课程号," +
				"case when e.授课教师姓名 is null then h.授课教师姓名 else e.授课教师姓名 end as 授课教师姓名 ,g.系部代码, " +
				"LEFT(d.行政班代码,2) as 排序,i.学年学期名称,a.考试名称,a.考试编号," +
				"(select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.班级代码)  as 需登分人数," +
				"(select count(考试学科编号) from V_自设考试管理_学生成绩信息表 where 考试学科编号=b.考试学科编号 and " +
				"(convert(varchar(10),成绩) <>'' or 等级<>'')) as 已登分人数 " +
				"from V_自设考试管理_考试信息表 a " +
				"inner join V_自设考试管理_考试学科信息表 b  on a.考试编号 = b.考试编号 " +
				" left join V_课程数据子类 c  on b.课程代码 = c.课程号 " +
				"left join V_学校班级数据子类 d  on b.班级代码 = d.行政班代码 " +
				" left join V_排课管理_课程表明细详情表 e  on d.行政班代码 = e.行政班代码 and c.课程号 = e.课程代码 " +
				"left join V_基础信息_教研组信息表 as f on b.课程代码=f.学科代码 " +
				"left join V_基础信息_系部教师信息表 as g on g.系部代码=d.系部代码 " +
				" left join (" +
				"select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 " +
				"from V_规则管理_授课计划主表 as t1 " +
				"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as h on h.行政班代码=b.班级代码 and h.课程代码=b.课程代码 and h.学年学期编码=a.学年学期编码 " +
				"left join V_规则管理_学年学期表 as i on i.学年学期编码=a.学年学期编码 " +
				"where 类别编号 ='01' " ;
		
		
		//权限判断
		if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
			String authArray[]=xbjyzz.split(","); 
			for(int i=0;i<authArray.length;i++){
				if(this.getAuth().indexOf(authArray[i])>-1){
					sql += " and g.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
					break;
				}
			}
			
		}
		if (!"".equalsIgnoreCase(xnxq)) {
			sql += " AND a.学年学期编码 ='" + MyTools.fixSql(xnxq) + "'";
		}
		if (!"".equalsIgnoreCase(ksmc)) {
			sql += " AND a.考试名称  like '%" + MyTools.fixSql(ksmc) + "%' ";
		}
		sql+=") as t ";
		if (!"".equalsIgnoreCase(dfqk)) {
			sql += " where t.已登分人数=0 ";
		}
		sql += " order by 系部代码 ,排序 desc ";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	
	/**
	 * ExportExcelRegistrationMark 登分信息导出
	 * @author zhaixuchao
	 * @date:2017-10-13
	 * @return savePath 下载路径
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 */
	public String ExportExcelRegistrationMark(String xnxq, String ksmc, String dfqk, String DFQKMC) throws SQLException, UnsupportedEncodingException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		String xbjyzz = MyTools.getProp(request, "Base.xbjyzz");//系部教研组长
		
		String savePath = "";
		ArrayList title = new ArrayList();
		title.add("学年学期名称");
		title.add("考试名称");
		title.add("行政班名称");
		title.add("课程名称");
		title.add("登分教师");
		title.add("需登分人数");
		title.add("已登分人数");
		//读取登记信息
		sql="select t.学年学期名称,t.考试名称,t.行政班名称,t.课程名称,t.授课教师姓名,t.需登分人数,t.已登分人数 from ("+
				"select distinct d.行政班名称,c.课程名称,d.行政班代码 , c.课程号," +
				"case when e.授课教师姓名 is null then h.授课教师姓名 else e.授课教师姓名 end as 授课教师姓名 ,g.系部代码, " +
				"LEFT(d.行政班代码,2) as 排序,i.学年学期名称,a.考试名称,a.考试编号," +
				"(select count(*) from V_学生基本数据子类 where 学生状态 in ('01','05') and 行政班代码=b.班级代码)  as 需登分人数," +
				"(select count(考试学科编号) from V_自设考试管理_学生成绩信息表 where 考试学科编号=b.考试学科编号 and " +
				"(convert(varchar(10),成绩) <>'' or 等级<>'')) as 已登分人数 " +
				"from V_自设考试管理_考试信息表 a " +
				"inner join V_自设考试管理_考试学科信息表 b  on a.考试编号 = b.考试编号 " +
				"left join V_课程数据子类 c  on b.课程代码 = c.课程号 " +
				"left join V_学校班级数据子类 d  on b.班级代码 = d.行政班代码 " +
				"left join V_排课管理_课程表明细详情表 e  on d.行政班代码 = e.行政班代码 and c.课程号 = e.课程代码 " +
				"left join V_基础信息_教研组信息表 as f on b.课程代码=f.学科代码 " +
				"left join V_基础信息_系部教师信息表 as g on g.系部代码=d.系部代码 " +
				"left join (" +
				"select distinct t1.行政班代码,t1.学年学期编码,t2.课程代码,t2.授课教师姓名,t2.授课教师编号 " +
				"from V_规则管理_授课计划主表 as t1 " +
				"left join V_规则管理_授课计划明细表 as t2 on t2.授课计划主表编号=t1.授课计划主表编号) as h on h.行政班代码=b.班级代码 and h.课程代码=b.课程代码 and h.学年学期编码=a.学年学期编码 " +
				"left join V_规则管理_学年学期表 as i on i.学年学期编码=a.学年学期编码 " +
				"where 类别编号 ='01' " ;
		
		
		//权限判断
		if(this.getAuth().indexOf(admin)<0 && this.getAuth().indexOf(jxzgxz)<0 && this.getAuth().indexOf(qxjdzr)<0 && this.getAuth().indexOf(qxjwgl)<0){
			String authArray[]=xbjyzz.split(","); 
			for(int i=0;i<authArray.length;i++){
				if(this.getAuth().indexOf(authArray[i])>-1){
					sql += " and g.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
					break;
				}
			}
			
		}
		if (!"".equalsIgnoreCase(xnxq)) {
			sql += " AND a.学年学期编码 ='" + MyTools.fixSql(xnxq) + "'";
		}
		if (!"".equalsIgnoreCase(ksmc)) {
			sql += " AND a.考试名称  like '%" + MyTools.fixSql(ksmc) + "%' ";
		}
		sql+=") as t ";
		if (!"".equalsIgnoreCase(dfqk)) {
			sql += " where t.已登分人数=0 ";
		}
		sql += " order by 系部代码 ,排序 desc ";
		vec = db.GetContextVector(sql);
		//整理数据
		if(vec!=null && vec.size()>0){
			String stuCode = "";
		
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			
			savePath = MyTools.getProp(request, "Base.exportExcelPath");
			
			//创建
			try {
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				// 输出的excel的路径   
				//filePath += "d:\\年级学生成绩汇总信息_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls"; 
				//输出流
				savePath += "/"+this.getDD_XNXQMC()+" " + DFQKMC + " 登分情况_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);//工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;
				
				//设置列宽
				for(int i=0; i<title.size(); i++){
					if(i==0||i==1||i==2){
						wsheet.setColumnView(i, 25);
					}else{
						wsheet.setColumnView(i, 15);
					}
				}
				
				//设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 15, WritableFont.BOLD);
				contentStyle=new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				//加的标题
				wsheet.mergeCells(0, 0, 6, 0);
				content = new Label(0, 0, this.getDD_XNXQMC() + " " + DFQKMC + " 登分情况", contentStyle);
				wsheet.addCell(content);
				
				
				//设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				
				for(int i=0;i<title.size();i++){   
				     // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z   
				     // 在Label对象的子对象中指明单元格的位置和内容   
					content = new Label(i,1,title.get(i).toString(),contentStyle); 
				     // 将定义好的单元格添加到工作表中   
					wsheet.addCell(content);   
				 }  
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				//填充数据
				//k:用于循环时Excel的行号
				//外层for用于循环行,内曾for用于循环列'
				for(int i=0,k=2; i<vec.size();i+=title.size(), k++){
					for(int j=0;j<title.size();j++){ 
						content = new Label(j, k, vec.get(i+j) + "",contentStyle);
						wsheet.addCell(content);
					}
				 } 	 
				//写入数据   
				wbook.write();   
				//关闭文件   
				wbook.close();
				os.close();
				this.setMSG("文件生成成功");
			} catch (FileNotFoundException e) {
				this.setMSG("导出前请先关闭相关EXCEL");
			} catch (WriteException e) {
				this.setMSG("文件生成失败");
			} catch (IOException e) {
				this.setMSG("文件生成失败");
			} 
			
			this.setMSG("文件生成成功");
		}else{
			this.setMSG("没有符合条件的登分情况信息");
		}
			return savePath;
	}
	
	
	
	
	
	
	
	
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getAuth() {
		return Auth;
	}

	public void setAuth(String auth) {
		Auth = auth;
	}

	public String getDD_XNXQBH() {
		return DD_XNXQBH;
	}

	public void setDD_XNXQBH(String dD_XNXQBH) {
		DD_XNXQBH = dD_XNXQBH;
	}

	public String getDD_XNXQMC() {
		return DD_XNXQMC;
	}

	public void setDD_XNXQMC(String dD_XNXQMC) {
		DD_XNXQMC = dD_XNXQMC;
	}

	public String getDD_KSBH() {
		return DD_KSBH;
	}

	public void setDD_KSBH(String dD_KSBH) {
		DD_KSBH = dD_KSBH;
	}

	public String getDD_KSMC() {
		return DD_KSMC;
	}

	public void setDD_KSMC(String dD_KSMC) {
		DD_KSMC = dD_KSMC;
	}

	public String getDD_DFQK() {
		return DD_DFQK;
	}

	public void setDD_DFQK(String dD_DFQK) {
		DD_DFQK = dD_DFQK;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
	
}