package com.pantech.devolop.registerScoreStatistics;
/*
@date 2017.03.16
@author yeq
模块：M8.1 学考考试质量分析
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

public class XkkszlfxBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限
	
	private String XNXQBM;//学年学期编码
	private String TEACODE;//教师工号
	private String TEANAME;//教师姓名
	private String COURSENAME;//课程名称
	private String EXAMTYPE;//考试类型

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public XkkszlfxBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2017.03.16
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		AUTH = "";//用户权限
		XNXQBM = "";//学年学期编码
		TEACODE = "";//教师工号
		TEANAME = "";//教师姓名
		COURSENAME = "";//课程名称
		EXAMTYPE = "";//考试类型
		MSG = "";
	}

	/**
	 * 分页查询 教师授课信息
	 * @date:2017.03.16
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param enterType 进入方式
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queTeaCourseList(int pageNum, int pageSize, String enterType) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "select distinct b.学年学期编码,b.学年学期名称,a.课程名称,a.登分教师编号,replace(a.登分教师姓名,'@','') as 任课教师 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_学校班级数据子类 c on c.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 d on d.教学班编号=a.行政班代码 " +
			"where 1=1";
		
		//根据进入该模块的方式判断需要查询的内容，如果是从教师登分页面进入的话，仅查询自己授课登分的课程
		if("scoreRegister".equalsIgnoreCase(enterType)){
			sql += " and a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
		}else{
			//判断如果不是全校信息查询权限
			if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
				sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
				if(this.getAUTH().indexOf(bzr) > -1){
					sql += " or a.行政班代码 in (select 行政班代码 from V_学校班级数据子类 where 班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' " +
						"union all select 教学班编号 from V_基础信息_教学班信息表 where 班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
					sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
						"or " +
						"d.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
				sql += ")";
			}
		}
		
		//判断查询条件
		if(!"".equalsIgnoreCase(this.getXNXQBM())){
			sql += " and b.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getCOURSENAME())){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(this.getCOURSENAME()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getTEACODE())){
			sql += " and a.登分教师编号 like '%" + MyTools.fixSql(this.getTEACODE()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getTEANAME())){
			sql += " and a.登分教师姓名 like '%" + MyTools.fixSql(this.getTEANAME()) + "%'";
		}
		sql += " order by b.学年学期编码 desc,a.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017.03.16
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,学年学期编码 as comboValue,'1' as orderNum " +
				"from V_规则管理_学年学期表 where 状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 生成考试质量统计信息文件
	 * @date:2017.03.16
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadStatisticsInfo(String filePath, String fileName, String title) throws SQLException{
		Vector vec = null;
		Vector classVec = null;
		String sql = "";
		String tempSql = "";
		String titleArray[] = {"班    级","考试人数","80-100\n（人数）","60-79\n（人数）","0-59\n（人数）","平均分","优秀率","合格率"};
		Vector resultVec = new Vector();
		Vector tempVec = new Vector();
		Vector pageContent = new Vector();
		String problem = "";
		String measure = "";
		
		sql = "with tempScoreInfo as (select a.学号,b.行政班代码,b.行政班名称,";
		//判断查询的考试类型
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "cast(a.期中 as float) as 成绩 ";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += "cast(a.期末 as float) as 成绩 ";
		}else{
			sql += "cast(a.平时 as float) as 成绩 ";
		}
		sql += " from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
			"left join V_学生基本数据子类 d on d.学号=a.学号 " +
			"where a.状态='1' and b.状态='1' and c.状态='1' and a.成绩状态='1' " +
			//20170703去除异动学生过滤
			//"and d.学生状态 in ('01','05') " +
			"and c.学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and b.课程名称='" + MyTools.fixSql(this.getCOURSENAME()) + "' " +
			"and b.登分教师编号='" + MyTools.fixSql(this.getTEACODE()) + "'";
		if("qz".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += " and a.期中<>'' and cast(a.期中 as float)>-1";
		}else if("qm".equalsIgnoreCase(this.getEXAMTYPE())){
			sql += " and a.期末<>'' and cast(a.期末 as float)>-1";
		}else{
			sql += " and a.平时<>'' and cast(a.平时 as float)>-1";
		}
		sql += ") ";
		tempSql = "select 行政班名称 as className,考试人数 as totalNum,num1,num2,num3,平均分," +
			"cast(Round(cast(num1 as float)/cast(考试人数 as float),4)*100 as nvarchar)+'%' as 优秀率," +
			"cast(Round(cast((num1+num2) as float)/cast(考试人数 as float),4)*100 as nvarchar)+'%' as 及格率  " +
			"from (select distinct 行政班代码,行政班名称,count(学号) as 考试人数," +
			"sum(case when cast(成绩 as float) between 80 and 100 then 1 else 0 end) as num1," +
			"sum(case when cast(成绩 as float) between 60 and 79 then 1 else 0 end) as num2," +
			"sum(case when cast(成绩 as float)<60 then 1 else 0 end) as num3," +
			"cast(avg(成绩) as numeric(18,2)) as 平均分 from tempScoreInfo group by 行政班代码,行政班名称) as z";
		vec = db.GetContextVector(sql+tempSql);
		
		//读取主要问题和措施内容
		tempSql = "select 主要问题,措施 from V_成绩管理_学科考试质量分析信息表 " +
			"where 学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and 课程名称='" + MyTools.fixSql(this.getCOURSENAME()) + "' " +
			"and 教师编号='" + MyTools.fixSql(this.getTEACODE()) + "' " +
			"and 成绩类型='" + MyTools.fixSql(this.getEXAMTYPE()) + "'";
		tempVec = db.GetContextVector(tempSql);
		if(tempVec!=null && tempVec.size()>0){
			problem = MyTools.StrFiltr(tempVec.get(0));
			measure = MyTools.StrFiltr(tempVec.get(1));
		}
		
		if(vec!=null && vec.size()>0){
			tempVec.clear();
			tempSql = "select distinct 行政班名称 from tempScoreInfo";
			classVec = db.GetContextVector(sql+tempSql);
			
			//整理数据
			for(int i=0; i<titleArray.length; i++){
				for(int j=0; j<vec.size(); j+=8){
					if(i == 0){
						tempVec.add(MyTools.StrFiltr(vec.get(j+i)).substring(0, 3)+"\n"+MyTools.StrFiltr(vec.get(j+i)).substring(3));
					}else{
						tempVec.add(MyTools.StrFiltr(vec.get(j+i)));
					}
				}
				
				//6个班级一页如果不足补充空白数据
				for(int k=0; k<(6-vec.size()/8%6); k++){
					tempVec.add("");
				}
			}
			int tempIndex = 0;
			int tempNum = 0;
			int pageNum = tempVec.size()/8/6;
			pageNum = tempVec.size()/8/6+(tempVec.size()/8%6==0?0:1);
			
			//分页处理
			for(int i=0; i<pageNum; i++){
				pageContent = new Vector();
				tempIndex = 0;
				tempNum = tempVec.size()/8;
				
				for(int j=0; j<tempVec.size(); j+=tempNum){
					pageContent.add(titleArray[tempIndex]);
					
					for(int k=0; k<6; k++){
						pageContent.add(tempVec.get(j));
						tempVec.remove(j);
					}
					j -= 6;
					tempIndex++;
				}
				resultVec.add(pageContent);
			}
		}else{
			//空白页
			for(int i=0; i<titleArray.length; i++){
				pageContent.add(titleArray[i]);
				
				for(int j=0; j<6; j++){
					pageContent.add("");
				}
			}
			resultVec.add(pageContent);
		}
		
		//生成XLS文件
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
//		int hour = c.get(Calendar.HOUR);
//		int minute = c.get(Calendar.MINUTE);
//		int second = c.get(Calendar.SECOND);
		int curRowNum = 0;
		
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		if("".equalsIgnoreCase(fileName)){
			fileName = title+".xls";
		}
		filePath += "/" + fileName;
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			
			//设置列宽
			wsheet.setColumnView(0, 15);
			wsheet.setColumnView(1, 12);
			wsheet.setColumnView(2, 12);
			wsheet.setColumnView(3, 12);
			wsheet.setColumnView(4, 12);
			wsheet.setColumnView(5, 12);
			wsheet.setColumnView(6, 12);
			
			for(int i=0; i<resultVec.size(); i++){
				pageContent = (Vector)resultVec.get(i);
				
				//标题
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 18, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				
				wsheet.mergeCells(0, curRowNum, 6, curRowNum);
				content = new Label(0, curRowNum, title, contentStyle);
				wsheet.addCell(content);
				wsheet.setRowView(curRowNum, 500);
				curRowNum++;
				
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 15, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				wsheet.mergeCells(0, curRowNum, 6, curRowNum);
				content = new Label(0, curRowNum, "学科："+this.getCOURSENAME(), contentStyle);
				wsheet.addCell(content);
				wsheet.setRowView(curRowNum, 400);
				curRowNum++;
				
				//表格内容
				//k:用于循环时Excel的行号
				//外层for用于循环行,内曾for用于循环列
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setWrap(true);// 自动换行
//				contentStyle.setShrinkToFit(true);//字体大小自适应
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				
				for (int j=0; j<pageContent.size(); j+=7) {
					for (int k=0; k<7; k++) {
						content = new Label(k, curRowNum, MyTools.StrFiltr(pageContent.get(j+k)), contentStyle);
						wsheet.addCell(content);
					}
					
					wsheet.setRowView(curRowNum, 900);
					curRowNum++;
				}
				
				wsheet.mergeCells(0, curRowNum, 0, curRowNum+2);
				content = new Label(0, curRowNum, "主要问题", contentStyle);
				wsheet.addCell(content);
				
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle, NumberFormats.TEXT);
				contentStyle.setAlignment(jxl.format.Alignment.LEFT);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setWrap(true);// 自动换行
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				wsheet.mergeCells(1, curRowNum, 6, curRowNum+2);
				content = new Label(1, curRowNum, problem, contentStyle);
				wsheet.addCell(content);
				for(int j=0; j<3; j++){
					wsheet.setRowView(curRowNum, 900);
					curRowNum++;
				}
				
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setWrap(true);// 自动换行
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				wsheet.mergeCells(0, curRowNum, 0, curRowNum+2);
				content = new Label(0, curRowNum, "措施", contentStyle);
				wsheet.addCell(content);
				
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle, NumberFormats.TEXT);
				contentStyle.setAlignment(jxl.format.Alignment.LEFT);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setWrap(true);// 自动换行
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				wsheet.mergeCells(1, curRowNum, 6, curRowNum+2);
				content = new Label(1, curRowNum, measure, contentStyle);
				wsheet.addCell(content);
				for(int j=0; j<3; j++){
					wsheet.setRowView(curRowNum, 900);
					curRowNum++;
				}
				
				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 15, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.LEFT);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				wsheet.mergeCells(0, curRowNum, 3, curRowNum);
				content = new Label(0, curRowNum, "授课教师："+this.getTEANAME(), contentStyle);
				wsheet.addCell(content);
				

				fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 15, WritableFont.BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.RIGHT);//水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
				wsheet.mergeCells(4, curRowNum, 6, curRowNum);
				content = new Label(4, curRowNum, year+"年"+(month+1)+"月"+date+"日", contentStyle);
				wsheet.addCell(content);
				
				wsheet.setRowView(curRowNum, 400);
				curRowNum++;
			}
			
			//写入文件
			wbook.write();
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
		
		return filePath;
	}
	
	/**
	 * 预览学科考试质量分析表
	 * @date:2017.03.16
	 * @author:yeq
	 * @return
	 * @throws SQLException
	 */
	public Vector loadPrintView() throws SQLException{
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		Vector resultVec = new Vector();
		
		String filePath = request.getSession().getServletContext().getRealPath("/")+"form/registerScoreStatistics/printView";
		filePath = filePath.replaceAll("\\\\", "/");
		String sem = this.getXNXQBM().substring(4, 5);
		String title = this.getXNXQBM().substring(0, 4)+"学年度 第"+("1".equalsIgnoreCase(sem)?"一":"二")+"学期 "+("qz".equalsIgnoreCase(this.getEXAMTYPE())?"期中":"期末")+"考试质量分析表";
		String fileName = "view"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+second+".xls";
		filePath = this.loadStatisticsInfo(filePath, fileName, title);
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		//poCtrl1.setWriter(wb);
		poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
		poCtrl1.setJsFunction_AfterDocumentClosed("AfterDocumentClosed()");
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		//创建自定义菜单栏
		poCtrl1.addCustomToolButton("保存", "saveDocumentContent()", 1);
		poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("下载", "download()", 3);
		poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("关闭", "closeWindow()", 4);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		
		poCtrl1.setCaption(title);
		poCtrl1.setFileTitle(title);//设置另存为窗口默认文件名
		
		//打开文件
		poCtrl1.webOpen("printView/"+fileName, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		
		resultVec.add(filePath);
		return resultVec;
	}
	
	/**
	 * 导出学科考试质量分析表
	 * @date:2017.03.16
	 * @author:yeq
	 * @return
	 * @throws SQLException
	 */
	public String exportReport() throws SQLException{
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
		String filePath = MyTools.getProp(request, "Base.exportExcelPath");
		String sem = this.getXNXQBM().substring(4, 5);
		String fileName = this.getCOURSENAME()+"("+this.getTEANAME()+")"+this.getXNXQBM().substring(0, 5)+("qz".equalsIgnoreCase(this.getEXAMTYPE())?"期中":"期末")+"考试质量分析表"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+".xls";
		String title = this.getXNXQBM().substring(0, 4)+"学年度 第"+("1".equalsIgnoreCase(sem)?"一":"二")+"学期 "+("qz".equalsIgnoreCase(this.getEXAMTYPE())?"期中":"期末")+"考试质量分析表";
		filePath = this.loadStatisticsInfo(filePath, fileName, title);
		return filePath;
	}
	
	 /** 
     * 删除单个文件 
     * @param sPath 被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public boolean deleteFile(String sPath) {
    	boolean flag = false;
        File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }
        return flag;  
    }
	
    /**
	 * 保存质量分析表信息
	 * @date:2017.03.27
	 * @author:yeq
	 * @param 主要问题
	 * @measure 措施
	 * @return
	 * @throws SQLException
	 */
	public void saveInfo(String problem, String measure) throws SQLException{
		String sql = "";
		
		if(problem.length() > 500)
			problem = problem.substring(0, 500);
		if(measure.length() > 500)
			measure = measure.substring(0, 500);
		
		sql = "select count(*) from V_成绩管理_学科考试质量分析信息表 " +
			"where 学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
			"and 课程名称='" + MyTools.fixSql(this.getCOURSENAME()) + "' " +
			"and 教师编号='" + MyTools.fixSql(this.getTEACODE()) + "' " +
			"and 成绩类型='" + MyTools.fixSql(this.getEXAMTYPE()) + "'";
		if(!db.getResultFromDB(sql)){
			sql = "insert into V_成绩管理_学科考试质量分析信息表 (学年学期编码,课程名称,教师编号,成绩类型,主要问题,措施,创建人,创建时间,状态) values(" +
				"'" + MyTools.fixSql(this.getXNXQBM()) + "'," +
				"'" + MyTools.fixSql(this.getCOURSENAME()) + "'," +
				"'" + MyTools.fixSql(this.getTEACODE()) + "'," +
				"'" + MyTools.fixSql(this.getEXAMTYPE()) + "'," +
				"'" + MyTools.fixSql(problem) + "'," +
				"'" + MyTools.fixSql(measure) + "'," +
				"'" + MyTools.fixSql(this.getUSERCODE()) + "'," +
				"getDate(),'1')";
		}else{
			sql = "update V_成绩管理_学科考试质量分析信息表 " +
				"set 主要问题='" + MyTools.fixSql(problem) + "'," +
				"措施='" + MyTools.fixSql(measure) + "' " +
				"where 学年学期编码='" + MyTools.fixSql(this.getXNXQBM()) + "' " +
				"and 课程名称='" + MyTools.fixSql(this.getCOURSENAME()) + "' " +
				"and 教师编号='" + MyTools.fixSql(this.getTEACODE()) + "' " +
				"and 成绩类型='" + MyTools.fixSql(this.getEXAMTYPE()) + "'";
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
    
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getAUTH() {
		return AUTH;
	}

	public void setAUTH(String aUTH) {
		AUTH = aUTH;
	}

	public String getXNXQBM() {
		return XNXQBM;
	}

	public void setXNXQBM(String xNXQBM) {
		XNXQBM = xNXQBM;
	}

	public String getTEACODE() {
		return TEACODE;
	}

	public void setTEACODE(String tEACODE) {
		TEACODE = tEACODE;
	}

	public String getTEANAME() {
		return TEANAME;
	}

	public void setTEANAME(String tEANAME) {
		TEANAME = tEANAME;
	}

	public String getCOURSENAME() {
		return COURSENAME;
	}

	public void setCOURSENAME(String cOURSENAME) {
		COURSENAME = cOURSENAME;
	}

	public String getEXAMTYPE() {
		return EXAMTYPE;
	}

	public void setEXAMTYPE(String eXAMTYPE) {
		EXAMTYPE = eXAMTYPE;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
}