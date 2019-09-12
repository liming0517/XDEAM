package com.pantech.devolop.registerScoreManage;
/*
@date 2016.02.04
@author yeq
模块：M7.1 学期成绩登记
说明:
重要及特殊方法：
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import com.jspsmart.upload.SmartFiles;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.pantech.devolop.registerScoreSet.DfryszBean;

public class XqcjdjBean {
	private String USERCODE;//用户编号
	private String AUTH;//用户权限

	private String CX_ID; //编号
	private String CX_XH; //学号
	private String CX_XM; //姓名
	private String CX_XGBH; //相关编号
	private String CX_PS; //平时
	private String CX_QZ; //期中
	private String CX_SX; //实训
	private String CX_QM; //期末
	private String CX_ZP; //总评
	private String CX_CX1; //重修1
	private String CX_CX2; //重修2
	private String CX_BK; //补考
	private String CX_DBK; //大补考
	private String CX_DYCJ; //打印成绩
	private String CX_CJZT; //成绩状态
	private String CX_CJR; //创建人
	private String CX_CJSJ; //创建时间
	private String CX_ZT; //状态
	
	private String CK_KSLX; //考试类型
	private String CK_ZPBLXX; //总评比例选项
	private String CK_PSBL; //平时比例
	private String CK_QZBL; //期中比例
	private String CK_SXBL; //实训比例
	private String CK_QMBL; //期末比例
	private String CK_CJLX; //成绩类型
	private String CK_SX; //实训
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	private String MSG2;

	/**
	 * 构造函数
	 * @param request
	 */
	public XqcjdjBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.MSG2 = "";
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-27
	 * @author:yeq
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		AUTH = "";//用户权限
		CX_ID = ""; //编号
		CX_XH = ""; //学号
		CX_XM = ""; //姓名
		CX_XGBH = ""; //相关编号
		CX_PS = ""; //平时
		CX_QZ = ""; //期中
		CX_SX = ""; //实训
		CX_QM = ""; //期末
		CX_ZP = ""; //总评
		CX_CX1 = ""; //重修1
		CX_CX2 = ""; //重修2
		CX_BK = ""; //补考
		CX_DBK = ""; //大补考
		CX_DYCJ = ""; //打印成绩
		CX_CJZT = ""; //成绩状态
		CX_CJR = ""; //创建人
		CX_CJSJ = ""; //创建时间
		CX_ZT = ""; //状态
		
		CK_KSLX = ""; //考试类型
		CK_ZPBLXX = ""; //总评比例选项
		CK_PSBL = ""; //平时比例
		CK_QZBL = ""; //期中比例
		CK_SXBL = ""; //实训比例
		CK_QMBL = ""; //期末比例
		CK_CJLX = ""; //成绩类型
		CK_SX = ""; //实训
	}
	
	/**
	 * 导入学生成绩列表
	 * @date:2017-01-11
	 * @author:zouyu
	 */
	public void Savestudentimportxls(SmartUpload mySmartUpload, String xkmc) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		Vector sqlVec = new Vector();
		Vector wzcjVec = null;
		String sql = "";
		int errorNum = 0;
		String tempScore = "";
		String scoreStr = "";
		boolean importFlag = true;
		boolean panduan = true;
		Vector titleVec = new Vector();
		Vector resultVec =new Vector();
		String CK_KSLX1 = mySmartUpload.getRequest().getParameter("CK_KSLX1");//考试类型
		String TEMPSX1 = mySmartUpload.getRequest().getParameter("TEMPSX1");//实训
		String CK_CJLX1 = mySmartUpload.getRequest().getParameter("CK_CJLX1");//成绩类型
		String CX_XGBH1 = mySmartUpload.getRequest().getParameter("CX_XGBH1");//相关编号
		String CK_ZPBLXX1 = mySmartUpload.getRequest().getParameter("CK_ZPBLXX1");//总评计算比率
		String CK_PSBL1 = mySmartUpload.getRequest().getParameter("CK_PSBL1");//平时比率
		String CK_QZBL1 = mySmartUpload.getRequest().getParameter("CK_QZBL1");//期中比率
		String CK_SXBL1 = mySmartUpload.getRequest().getParameter("CK_SXBL1");//实训比率
		String CK_QMBL1 = mySmartUpload.getRequest().getParameter("CK_QMBL1");//期末比率 
		DecimalFormat ft = new DecimalFormat("#");  
		ft.setRoundingMode(RoundingMode.HALF_UP);
		
		titleVec.add("班内学号");
		titleVec.add("姓名");
		titleVec.add("学号");
		if(!"5".equalsIgnoreCase(CK_ZPBLXX1)){
			if("1".equalsIgnoreCase(CK_KSLX1)){
				titleVec.add("平时");
				titleVec.add("期中");
				if("1".equalsIgnoreCase(TEMPSX1)){//判断是否有实训
					titleVec.add("实训");
				}
				titleVec.add("期末");
			}else{
				titleVec.add("阶段一");
				titleVec.add("阶段二");
				titleVec.add("阶段三");
				if("1".equalsIgnoreCase(TEMPSX1)){//判断是否有实训
					titleVec.add("阶段四");
				}
			}
		}
		if("4".equalsIgnoreCase(CK_ZPBLXX1) || "5".equalsIgnoreCase(CK_ZPBLXX1)){
			titleVec.add("总评");
		}
		
		String url = MyTools.getProp(request, "Base.UploadExcelPath");
		File filep = new File(url);
		//根据配置路径来判断没有文件则创建文件
		if (!filep.exists()) {
			filep.mkdirs();
		}
		Workbook workbook = null;
		SmartFiles files = mySmartUpload.getFiles();
		com.jspsmart.upload.SmartFile file = null;
		
		//获取文件名称
		String path= unescape(mySmartUpload.getRequest().getParameter("sxpath"));
		File f = new File(path);
		String filename=f.getName();
		//判断
		if(files.getCount() > 0){
			file = files.getFile(0);
			if(file.getSize()<=0){
				this.MSG="文件内容为空，请确认！";
			}
			file.saveAs(url +"/"+filename);
		}
		url = url +"/"+filename;
		File file1 = new File(url);
		InputStream is = new FileInputStream(file1);
		
		try {
			workbook = Workbook.getWorkbook(is);
			if(workbook.getNumberOfSheets()==0){
				this.setMSG("未找到sheet");
				return;
			}
			
			Sheet sheet = workbook.getSheet(0); //获取sheet(k)的数据对象
			int rsRows = sheet.getRows(); //获取sheet(k)中的总行数
			int rscolumns = sheet.getColumns(); //获取总列数
			
			//判断导入Excel的列长是否和导入格式中一致
			if(titleVec.size() == rscolumns){    
				for(int i=0; i<titleVec.size(); i++){
					tempScore = sheet.getCell(i, 3).getContents().trim(); //第2行
					if(!tempScore.equalsIgnoreCase(MyTools.StrFiltr(titleVec.get(i)))){
						importFlag = false;
					}
				}
			}else{
				importFlag = false;
			}
			
			String xk= sheet.getCell(0, 1).getContents().trim();
			xk=xk.substring(3);
			String bj = sheet.getCell(3, 1).getContents().trim();
			bj=bj.substring(5);
			/*if(xkmc.equalsIgnoreCase(xk)){
				
			}else {
				importFlag = false;
			}*/
			if(!xkmc.equalsIgnoreCase(xk) && !xkmc.equalsIgnoreCase(bj+" "+xk)){
				panduan = false;
			}
			
			if(importFlag && panduan){
				//查询文字代码
				sql = "select 代码,名称 from V_成绩管理_文字成绩代码表 where 状态='1' and 代码 in ('-1','-2','-3','-4','-5','-13','-17')";
				wzcjVec = db.GetContextVector(sql);
				boolean rowValid = false;//用于检测是否为有效行
				
				//获取表格数据				
				for(int a=4; a<rsRows; a++){
					rowValid = false;
					
					//判断是否为有效行
					for(int b=0; b<titleVec.size(); b++){
						if(!"".equalsIgnoreCase(sheet.getCell(b, a).getContents().trim())){
							rowValid = true;
							break;
						}
					}
					if(rowValid){
						//判断学号是否为空
						if("".equalsIgnoreCase(sheet.getCell(2, a).getContents().trim())){
							this.setMSG("学号不能为空,请修改后重新上传！");
							return;
						}
						
						for(int j=0; j<titleVec.size(); j++){
							tempScore = sheet.getCell(j, a).getContents().trim();
							
							if(j > 2){
								scoreStr = this.checkScoreData(CK_CJLX1, tempScore, wzcjVec);
							}else{
								scoreStr = tempScore;
							}
							
							if(!"".equalsIgnoreCase(tempScore) && "".equalsIgnoreCase(scoreStr)) errorNum++;
							resultVec.add(scoreStr);
						}
					}
				}
				
				String stuCode= "";
				String pscj = "";
				String qzcj = "";
				String sxcj = "";
				String qmcj = "";
				String zpcj = "";
				float tempZp = 0;
				String resultZp = "";
				
				//处理成绩信息
				for(int i =0; i<resultVec.size(); i+=titleVec.size()){
					stuCode = MyTools.StrFiltr(resultVec.get(i+2));
					tempZp = 0;
					resultZp = "";
					
					//仅输总评
					if("5".equalsIgnoreCase(CK_ZPBLXX1)){
						zpcj = MyTools.fixSql(resultVec.get(i+3).toString());//总评成绩
					}else if("4".equalsIgnoreCase(CK_ZPBLXX1)){//手动输入全部
						pscj = MyTools.fixSql(resultVec.get(i+3).toString());//平时成绩
						qzcj = MyTools.fixSql(resultVec.get(i+4).toString());//期中成绩
						if("1".equalsIgnoreCase(TEMPSX1)){
							sxcj = MyTools.fixSql((resultVec.get(i+5).toString()));//实训成绩
							qmcj = MyTools.fixSql((resultVec.get(i+6).toString()));//期末成绩
							zpcj = MyTools.fixSql((resultVec.get(i+7).toString()));//总评成绩
						}else{
							qmcj = MyTools.fixSql((resultVec.get(i+5).toString()));//期末成绩
							zpcj = MyTools.fixSql((resultVec.get(i+6).toString()));//总评成绩
						}
					}else{//自动计算总评
						pscj = MyTools.fixSql(resultVec.get(i+3).toString());//平时成绩
						qzcj = MyTools.fixSql(resultVec.get(i+4).toString());//期中成绩
						if("1".equalsIgnoreCase(TEMPSX1)){
							sxcj = MyTools.fixSql((resultVec.get(i+5).toString()));//实训成绩
							qmcj = MyTools.fixSql((resultVec.get(i+6).toString()));//期末成绩
						}else{
							qmcj = MyTools.fixSql((resultVec.get(i+5).toString()));//期末成绩
						}
						
						//判断是否需要计算总评
						if((!"".equalsIgnoreCase(CK_PSBL1) && ("".equalsIgnoreCase(pscj) || MyTools.StringToInt(pscj)<0)) ||
							 (!"".equalsIgnoreCase(CK_QZBL1) && ("".equalsIgnoreCase(qzcj) || MyTools.StringToInt(qzcj)<0)) ||
							 (!"".equalsIgnoreCase(CK_SXBL1) && ("".equalsIgnoreCase(sxcj) || MyTools.StringToInt(sxcj)<0)) ||
							 (!"".equalsIgnoreCase(CK_QMBL1) && ("".equalsIgnoreCase(qmcj) || MyTools.StringToInt(qmcj)<0))){
								zpcj = "";
						}else{
							if(!"".equalsIgnoreCase(CK_PSBL1))
								tempZp += MyTools.StringToFloat(pscj)*MyTools.StringToFloat(CK_PSBL1)/100;
							if(!"".equalsIgnoreCase(CK_QZBL1))
								tempZp += MyTools.StringToFloat(qzcj)*MyTools.StringToFloat(CK_QZBL1)/100;
							if(!"".equalsIgnoreCase(CK_SXBL1))
								tempZp += MyTools.StringToFloat(sxcj)*MyTools.StringToFloat(CK_SXBL1)/100;
							if(!"".equalsIgnoreCase(CK_QMBL1))
								tempZp += MyTools.StringToFloat(qmcj)*MyTools.StringToFloat(CK_QMBL1)/100;
							
							//判断总评是否为整数
//							resultZp = String.valueOf(tempZp);
//							resultZp = resultZp.substring(0, resultZp.indexOf(".")) + resultZp.substring(resultZp.indexOf("."), resultZp.indexOf(".")+2);
//							if(".0".equalsIgnoreCase(resultZp.substring(resultZp.length()-2))){
//								resultZp = resultZp.substring(0, resultZp.length()-2);
//							}
							resultZp = ft.format(tempZp);
							zpcj = String.valueOf(resultZp);
						}
					}
					
					sql = "update V_成绩管理_学生成绩信息表 set " +
						"平时=case when 平时 in ('-1') then 平时 else '" + MyTools.fixSql(pscj) + "' end," +
						"期中=case when 期中 in ('-1') then 期中 else '" + MyTools.fixSql(qzcj) + "' end," +
						"实训=case when 实训 in ('-1') then 实训 else '" + MyTools.fixSql(sxcj) + "' end," +
						"期末=case when 期末 in ('-1') then 期末 else '" + MyTools.fixSql(qmcj) + "' end," +
						"总评=case when 总评 in ('-1') then 总评 else '" + MyTools.fixSql(zpcj) + "' end " +
						"where 相关编号='" + MyTools.fixSql(CX_XGBH1) + "' " +
						"and 学号='" + MyTools.fixSql(stuCode) + "'";
					sqlVec.add(sql);
				}
					
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					this.setMSG("导入成功");
					if(errorNum > 0){
						this.setMSG2("部分成绩信息不符合规范，未完全导入功能！");
					}
				}else{
					this.setMSG("导入失败");
				}
				return;
			}else{
				String msg = "";
				//this.setMSG("格式不正确（Excel应该为--"+titleVec.toString().substring(1,titleVec.toString().length()-1)+"--格式）");
				if(importFlag == false && panduan == false){
					msg = "格式不正确（Excel应该为--"+titleVec.toString().substring(1,titleVec.toString().length()-1)+"--格式）,并且导入的成绩信息与当前选择的课程不一致";
				}else {
					if(panduan == false){
						msg = "导入的成绩信息与当前选择的课程不一致,请重新导入";
					}
					if(importFlag == false){
						msg = "格式不正确（Excel应该为--"+titleVec.toString().substring(1,titleVec.toString().length()-1)+"--格式）";
					}
				}
				this.setMSG(msg);
			}
		}catch (Exception e) {
			this.setMSG("导入失败");
			e.printStackTrace();
		}
		finally {
			workbook.close();
			is.close();
			//删除服务器上文件
			//路径为文件且不为空则进行删除  
		    if (file1.isFile() && file1.exists()) {  
		    	file1.delete();
		    }
		}
	}
	
	/**校验成绩数据
	 * @date:2017-03-21
	 * @author:yeq
	 */
	public String checkScoreData(String scoreType, String scoreStr, Vector wzcjVec){
		String result = scoreStr;
		int tempScore = 0;
		
		if(!"".equalsIgnoreCase(result)){
			for(int i=0; i<wzcjVec.size(); i+=2){
				if(result.equalsIgnoreCase(MyTools.StrFiltr(wzcjVec.get(i+1)))){
					result = MyTools.StrFiltr(wzcjVec.get(i));
					break;
				}
			}
		}
		
		//判断是否为数字
		if(this.isInteger(result)){
			tempScore = MyTools.StringToInt(result);

			if(tempScore > 100) 
				result = "100";
			if("2".equalsIgnoreCase(scoreType) && (tempScore>0 || (tempScore!=-2 && tempScore!=-3 && tempScore!=-4 && tempScore!=-5 && tempScore!=-17))) 
				result = "";
		}else{
			result = "";
		}
		
		return result;
	}
	
	/**
	 * 导出学生成绩列表
	 * @date:2017-1-6
	 * @author:zouyu
	 */
	public String scoreExport() throws SQLException{
		Vector vec = null;
		Vector tempVec = new Vector();
		Vector resultVec = new Vector();
		String sql = "";
		String filePath = "";
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		
		sql = "select 学年学期编码,学年学期名称,专业名称,行政班代码,行政班名称,课程代码,课程名称,总课时,学分,任课教师,班内学号,学号,姓名,班级,平时,期中,实训,期末,总评 from (" +
			"select b.学年学期编码,b.学年学期名称,b.专业名称,c.行政班代码,c.行政班名称,c.课程代码,c.课程名称," +
			"case when a.相关编号 like 'XXKMX_%' then isnull(f.总课时,'') else isnull(e.总课时,'') end as 总课时," +
			"case when a.相关编号 like 'XXKMX_%' then (case when f.学分 is null then '' else cast(cast(f.学分 as float) as varchar) end) " +
			"when a.相关编号 like 'TJKC_%' then cast(cast(i.学分 as float) as varchar) " +
			"else (case when e.学分 is null then '' else cast(cast(e.学分 as float) as varchar) end) end as 学分," +
			"replace(replace(c.登分教师姓名,'@,@',','),'@','') as 任课教师,g.班内学号,a.学号,a.姓名,h.行政班名称 as 班级,a.平时,a.期中,a.实训,a.期末,a.总评," +
			"case when a.大补考<>'' then a.大补考 when a.补考<>'' then a.补考 when a.重修2<>'' then a.重修2 when a.重修1<>'' then a.重修1 else a.总评 end as 成绩 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_成绩管理_登分教师信息表 c on c.相关编号=a.相关编号 " +
			"left join V_学校班级数据子类 d on d.行政班代码=c.行政班代码 " +
			//翟旭超2018/1/19改
			//"left join V_规则管理_培养计划信息表 e on e.学年学期编码=b.学年学期编码 and e.年级代码=d.年级代码 and e.专业代码=d.专业代码 and e.课程代码=b.课程代码 " +
			"left join dbo.V_规则管理_授课计划明细表 e on e.授课计划明细编号=a.相关编号 " +
			"left join V_规则管理_选修课授课计划明细表 f on f.授课计划明细编号=a.相关编号 " +
			"left join V_排课管理_添加课程信息表 i on i.编号=a.相关编号 " +
			"left join V_学生基本数据子类 g on g.学号=a.学号 " +
			"left join V_学校班级数据子类 h on h.行政班代码=g.行政班代码 " +
			"where a.成绩状态 not in ('0','3') and g.学生状态 in ('01','05','07','08') and a.相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "') as t " +
			"order by (case when 班内学号='' then '9999' else 班内学号 end)";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			String xnxqbm = "";//学年学期编码
			String xnxqmc = "";//学年学期名称
			String zymc = "";//专业名称
			String xzbdm = "";//行政班代码
			String xzbmc = "";//行政班名称
			String kcdm = "";//课程代码
			String kcmc = "";//课程名称
			String zks = "";//总课时
			String xf = "";//学分
			String rkjs = "";//任课教师
			String bnxh = "";//班内学号
			String xh = "";//学号
			String xm = "";//姓名
			String bj = "";//班级
			String pscj = "";//平时成绩
			String qzcj = "";//期中成绩
			String sxcj = "";//实训成绩
			String qmcj = "";//期末成绩
			String zpcj = "";//总评成绩
			int num = 1;//序号
			//整理数据
			for(int i=0; i<vec.size(); i+=19){
				if(i == 0){
					xnxqbm = MyTools.StrFiltr(vec.get(i));
					xnxqmc = MyTools.StrFiltr(vec.get(i+1));
					zymc = MyTools.StrFiltr(vec.get(i+2));
					xzbdm = MyTools.StrFiltr(vec.get(i+3));
					xzbmc = MyTools.StrFiltr(vec.get(i+4));
					kcdm = MyTools.StrFiltr(vec.get(i+5));
					kcmc = MyTools.StrFiltr(vec.get(i+6));
					zks = MyTools.StrFiltr(vec.get(i+7));
					xf = MyTools.StrFiltr(vec.get(i+8));
					rkjs = MyTools.StrFiltr(vec.get(i+9));
					
					resultVec.add(xnxqmc);
					resultVec.add(zymc);
					resultVec.add(xzbmc);
					resultVec.add(kcmc);
					resultVec.add(zks);
					resultVec.add(xf);
					resultVec.add(rkjs);
				}else{
					//判断是否同一门课程
					if(!xnxqbm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i))) || !xzbdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+3))) || !xzbmc.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+4))) || !kcdm.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i+5)))){
						//添加2条空数据（文件预留两行）
						for(int a=0; a<2; a++){
							tempVec.add(num);
							num++;
							
							for(int b=0; b<8; b++){
								tempVec.add("");
							}
						}
						resultVec.add(tempVec);
						
						tempVec = new Vector();
						num = 1;
						xnxqbm = MyTools.StrFiltr(vec.get(i));
						xnxqmc = MyTools.StrFiltr(vec.get(i+1));
						zymc = MyTools.StrFiltr(vec.get(i+2));
						xzbdm = MyTools.StrFiltr(vec.get(i+3));
						xzbmc = MyTools.StrFiltr(vec.get(i+4));
						kcdm = MyTools.StrFiltr(vec.get(i+5));
						kcmc = MyTools.StrFiltr(vec.get(i+6));
						zks = MyTools.StrFiltr(vec.get(i+7));
						xf = MyTools.StrFiltr(vec.get(i+8));
						rkjs = MyTools.StrFiltr(vec.get(i+9));
						
						resultVec.add(xnxqmc);
						resultVec.add(zymc);
						resultVec.add(xzbmc);
						resultVec.add(kcmc);
						resultVec.add(zks);
						resultVec.add(xf);
						resultVec.add(rkjs);
					}
				}
				
				bnxh = MyTools.StrFiltr(vec.get(i+10));
				xh = MyTools.StrFiltr(vec.get(i+11));
				xm = MyTools.StrFiltr(vec.get(i+12));
				bj = MyTools.StrFiltr(vec.get(i+13));
				pscj =  MyTools.StrFiltr(vec.get(i+14));
				qzcj =  MyTools.StrFiltr(vec.get(i+15));
				sxcj =  MyTools.StrFiltr(vec.get(i+16));
				qmcj =  MyTools.StrFiltr(vec.get(i+17));
				zpcj =  MyTools.StrFiltr(vec.get(i+18));
				
//				pscj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+13)));
//				qzcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+14)));
//				sxcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+15)));
//				qmcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+16)));
//				zpcj = this.parseScore(wzcjVec, MyTools.StrFiltr(vec.get(i+17)));
				
				//tempVec.add(num);
				tempVec.add(bnxh);
				tempVec.add(xm);
				tempVec.add(xh);
				if(!"5".equalsIgnoreCase(this.getCK_ZPBLXX())){
					tempVec.add(pscj);
					tempVec.add(qzcj);
					if("1".equalsIgnoreCase(this.getCK_SX())) tempVec.add(sxcj);//判断是否有实训
					tempVec.add(qmcj);
					if("4".equalsIgnoreCase(this.getCK_ZPBLXX())) tempVec.add(zpcj);
				}else{
					tempVec.add(zpcj);
				}
				num++;
			}
			resultVec.add(tempVec);
			
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			
			String savePath = MyTools.getProp(request, "Base.exportExcelPath");
			String title = "";
			Vector titleVec = new Vector();
			
			//生成表头信息
			//titleVec.add("序号");
			titleVec.add("班内学号");
			titleVec.add("姓名");
			titleVec.add("学号");
			if(!"5".equalsIgnoreCase(this.getCK_ZPBLXX())){
				if("1".equalsIgnoreCase(this.getCK_KSLX())){
					titleVec.add("平时");
					titleVec.add("期中");
					if("1".equalsIgnoreCase(this.getCK_SX())){//判断是否有实训
						titleVec.add("实训");
					}
					titleVec.add("期末");
				}else{
					titleVec.add("阶段一");
					titleVec.add("阶段二");
					titleVec.add("阶段三");
					if("1".equalsIgnoreCase(this.getCK_SX())){//判断是否有实训
						titleVec.add("阶段四");
					}
				}
			}
			if("4".equalsIgnoreCase(this.getCK_ZPBLXX()) || "5".equalsIgnoreCase(this.getCK_ZPBLXX())){
				titleVec.add("总评");
			}
			
			//导出成绩信息
			for(int a=0; a<resultVec.size(); a+=8){
				xnxqmc = MyTools.StrFiltr(resultVec.get(a));
				zymc = MyTools.StrFiltr(resultVec.get(a+1));
				xzbmc = MyTools.StrFiltr(resultVec.get(a+2));
				kcmc = MyTools.StrFiltr(resultVec.get(a+3));
				zks = MyTools.StrFiltr(resultVec.get(a+4));
				xf = MyTools.StrFiltr(resultVec.get(a+5));
				rkjs = MyTools.StrFiltr(resultVec.get(a+6));
				tempVec = (Vector)resultVec.get(a+7);
				xnxqmc = xnxqmc.replace(" ", "度");
				//savePath = rootPath;                 
				title = schoolName+xnxqmc+"成绩登记表";
				
				try {
					//创建
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
						file.createNewFile();
					}
					savePath += "/scoreExport/" + xzbmc + " " + kcmc + "_" + year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
					//输出流
					OutputStream os = new FileOutputStream(savePath);
					WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
					this.exportScore("exportExcel", wbook, tempVec, titleVec, title, xzbmc, kcmc, zks,xf, this.getCK_ZPBLXX());
					
					//写入文件
					wbook.write();
					wbook.close();
					os.close();
					
					this.setMSG("成绩文件生成成功");
				} catch (FileNotFoundException e) {
					this.setMSG("导出前请先关闭相关EXCEL");
				} catch (WriteException e) {
					this.setMSG("文件生成失败");
				} catch (IOException e) {
					this.setMSG("文件生成失败");
				}
			}
			filePath = savePath;
			this.setMSG("成绩文件生成成功");
		}else{
			this.setMSG("没有符合条件的成绩信息");
		}
		
		return filePath;
	}

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
	 
	public void exportScore(String type, WritableWorkbook wbook, Vector scoreVec, Vector titleVec, String title, String xzbmc, String kcmc, String zks, String xf, String zpblxx) throws IOException, WriteException, FileNotFoundException{
		WritableSheet wsheet = wbook.createSheet("Sheet1", wbook.getNumberOfSheets());//工作表名称
		WritableFont fontStyle;
		WritableCellFormat contentStyle;
		Label content;
		int maxRowNum = 0;
		String curScore = "";
		
		//设置列宽
		wsheet.setColumnView(0, 9);
		wsheet.setColumnView(1, 15);
		if("5".equalsIgnoreCase(zpblxx)){
			wsheet.setColumnView(2, 27);
			wsheet.setColumnView(3, 40);
			wsheet.setColumnView(4, 10);
			wsheet.setRowView(1, 400);
			wsheet.setRowView(2, 400);
		}else{
			wsheet.setColumnView(2, 26);
			wsheet.setColumnView(3, 10);
			wsheet.setColumnView(4, 10);
			wsheet.setColumnView(5, 10);
			wsheet.setColumnView(6, 10);
			wsheet.setColumnView(7, 10);
			wsheet.setColumnView(8, 10);
			wsheet.setColumnView(9, 10);
			wsheet.setRowView(1, 400);
			wsheet.setRowView(2, 400);
		}
		
		//标题
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 12, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
		
		wsheet.mergeCells(0, 0, titleVec.size()-1, 0);
		content = new Label(0, 0, title, contentStyle);
		wsheet.addCell(content);
		
		//标题信息
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		contentStyle = new WritableCellFormat(fontStyle);
		contentStyle.setAlignment(jxl.format.Alignment.LEFT);//左对齐
		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
		
		wsheet.mergeCells(0, 1, 2, 1);
		content = new Label(0, 1, "学科："+kcmc, contentStyle);
		wsheet.addCell(content);
		
		wsheet.mergeCells(0, 2, 2, 2);
		content = new Label(0, 2, "总学时："+zks, contentStyle);
		wsheet.addCell(content);
		
		if("5".equalsIgnoreCase(zpblxx)){
			content = new Label(3, 1, "上课班级："+xzbmc, contentStyle);
			wsheet.addCell(content);
			content = new Label(3, 2, "学分："+xf, contentStyle);
			wsheet.addCell(content);
		}else{
			wsheet.mergeCells(3, 1, 5, 1);
			content = new Label(3, 1, "上课班级："+xzbmc, contentStyle);
			wsheet.addCell(content);
			wsheet.mergeCells(3, 2, 5, 2);
			content = new Label(3, 2, "学分："+xf, contentStyle);
			wsheet.addCell(content);
		}
		
		/*wsheet.mergeCells(4, 2, 3, 2);
		content = new Label(4, 2, "任课教师："+rkjs, contentStyle);
		wsheet.addCell(content);
		*/
		//表格标题行
		fontStyle = new WritableFont(
				WritableFont.createFont("宋体"), 10, WritableFont.BOLD,
				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
				jxl.format.Colour.BLACK);
		
		for (int i=0; i<titleVec.size(); i++) {
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			//contentStyle.setWrap(true);// 自动换行
			contentStyle.setShrinkToFit(true);//字体大小自适应
			
			if(i == 0){
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}else if(i == titleVec.size()-1){
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}else{
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			}
			content = new Label(i, 3, MyTools.StrFiltr(titleVec.get(i)), contentStyle);
			wsheet.addCell(content);
		}
		
		//表格内容
		//k:用于循环时Excel的行号
		//外层for用于循环行,内曾for用于循环列
		for (int i=0, k=4; i<scoreVec.size(); i+=titleVec.size(), k++) {
			for (int j=0; j<titleVec.size(); j++) {
//				if(j == titleArray.length-1){
//					if(MyTools.StrFiltr(scoreVec.get(i+titleArray.length-1)).length() < 15){
//						fontStyle = new WritableFont(
//								WritableFont.createFont("宋体"), 8, WritableFont.NO_BOLD,
//								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//								jxl.format.Colour.BLACK);
//					}else if(MyTools.StrFiltr(scoreVec.get(i+titleArray.length-1)).length() < 19){
//						fontStyle = new WritableFont(
//								WritableFont.createFont("宋体"), 7, WritableFont.NO_BOLD,
//								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//								jxl.format.Colour.BLACK);
//					}else{
//						fontStyle = new WritableFont(
//								WritableFont.createFont("宋体"), 6, WritableFont.NO_BOLD,
//								false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//								jxl.format.Colour.BLACK);
//					}
//				}else{
//					fontStyle = new WritableFont(
//							WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
//							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//							jxl.format.Colour.BLACK);
//				}
				
				curScore = MyTools.StrFiltr(scoreVec.get(i+j));
				
				if(j>2 && !"".equalsIgnoreCase(curScore) && ((MyTools.StringToInt(curScore)>0&&MyTools.StringToInt(curScore)<60) 
						|| "-2".equalsIgnoreCase(curScore) || "-3".equalsIgnoreCase(curScore) || "-4".equalsIgnoreCase(curScore) || "-10".equalsIgnoreCase(curScore) || "-12".equalsIgnoreCase(curScore))){
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.RED);
				}else{
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
				}
				
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				//contentStyle.setWrap(true);// 自动换行
				contentStyle.setShrinkToFit(true);//字体大小自适应
				
				if(j == 0){
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THICK);
				}else if(j == titleVec.size()-1){
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				}
				
				if((i+titleVec.size())==scoreVec.size() || ("printView".equalsIgnoreCase(type) && i>0 && i/titleVec.size()%39==0)){
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
				}else{
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				}
				
				if("printView".equalsIgnoreCase(type) && i>0 && i/titleVec.size()%40 == 0){
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THICK);
				}
				
				content = new Label(j, k, curScore, contentStyle);
				wsheet.addCell(content);
			}
			
//			if("printView".equalsIgnoreCase(type) && scoreVec.size()/titleArray.length>40){
//				if(i>0 && i/titleArray.length%39==0){
//					if(i < titleArray.length*40){
//						k += 4;
//					}else{
//						k += 10;
//					}
//				}
//			}
			
			maxRowNum = k;
			wsheet.setRowView(k, 300);
		}
		
//		fontStyle = new WritableFont(
//				WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
//				false, jxl.format.UnderlineStyle.NO_UNDERLINE,
//				jxl.format.Colour.BLACK);
//		contentStyle = new WritableCellFormat(fontStyle);
//		contentStyle.setAlignment(jxl.format.Alignment.LEFT);//左对齐
//		contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
		
//		wsheet.mergeCells(7, maxRowNum+2, 8, maxRowNum+2);
//		content = new Label(7, maxRowNum+2, "     阅卷教师签名：", contentStyle);
//		wsheet.addCell(content);
	}
	
	public String parseScore(Vector wzcjVec, String score){
		String result = "";
		
		if(MyTools.StringToInt(score) < 0){
			for(int i=0; i<wzcjVec.size(); i+=2){
				if(MyTools.StrFiltr(wzcjVec.get(i)).equalsIgnoreCase(score)){
					result = MyTools.StrFiltr(wzcjVec.get(i+1));
					break;
				}
			}
		}
		if("".equalsIgnoreCase(result))
			result = score;
		
		return result;
	}
	
	public void compressZip(String fileName, String zipFileName) {  
        File srcdir = new File(fileName);  
        if (!srcdir.exists())  
            throw new RuntimeException(fileName + "不存在！");
        
        File zipFile = new File(zipFileName);
        Project prj = new Project();  
        Zip zip = new Zip();  
        zip.setProject(prj);  
        zip.setDestFile(zipFile);  
        FileSet fileSet = new FileSet();  
        fileSet.setProject(prj);  
        fileSet.setDir(srcdir);  
        //fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");  
        //fileSet.setExcludes(...); 排除哪些文件或文件夹  
        zip.addFileset(fileSet);  
        zip.execute();  
    }  
	
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param sPath 被删除目录的文件路径 
     */  
    public boolean deleteDirectory(String sPath){
    	boolean flag = true;
    	
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)){  
            sPath = sPath + File.separator;
        }
        
        File dirFile = new File(sPath);
        
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()){
            return false;
        }
        
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }
    
    /**
	 * 转换文件路径中的中文字符
	 * @param src
	 * @return
	 */
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
	 * 分页查询 科目列表
	 * @date:2016-02-01
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XNXQMC_CX 学年学期名称
	 * @param JXXZ_CX 教学性质
	 * @param ZYMC_CX 专业名称
	 * @param NJDM_CX 年级代码
	 * @param KCMC_CX 课程名称
	 * @param KCLX_CX 课程类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queSubjectList(int pageNum, int pageSize, String XNXQMC_CX, String JXXZ_CX, String ZYMC_CX, String NJDM_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select distinct a.科目编号,a.学年学期编码,c.学年学期名称,d.教学性质,a.年级代码,a.年级名称,a.专业代码,a.专业名称,a.课程名称," +
			"case b.来源类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as 课程类型 " +
			//20170110yeq修改查询是否可登分相关代码
//			"case when convert(nvarchar(10),getDate(),21) between convert(nvarchar(10),e.开始时间,21) and convert(nvarchar(10),e.结束时间,21) then 'true' else 'false' end as 登分时间," +
//			"case when (select count(*) from V_成绩管理_教师登分权限表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')>0 then 'true' else 'false' end as 登分权限," +
//			"case when b.打印锁定='0' then 'true' else 'false' end as 打印锁定 " +
			"from V_成绩管理_科目课程信息表 a " +
			"inner join V_成绩管理_登分教师信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 c on c.学年学期编码=a.学年学期编码 " +
			"left join V_基础信息_教学性质 d on d.编号=c.教学性质 " +
			"left join V_成绩管理_登分时间表 e on e.学年学期编码=a.学年学期编码 " +
			"left join V_学校班级数据子类 f on f.行政班代码=b.行政班代码 " +
			"left join V_基础信息_教学班信息表 g on g.教学班编号=b.行政班代码 " +
			"where 1=1";
		
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (b.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"g.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		//判断查询条件
		if(!"".equalsIgnoreCase(XNXQMC_CX)){
			sql += " and left(c.学年学期编码,5)='" + MyTools.fixSql(XNXQMC_CX) + "'";
		}
		if(!"".equalsIgnoreCase(JXXZ_CX)){
			sql += " and c.教学性质='" + MyTools.fixSql(JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(ZYMC_CX)){
			sql += " and a.专业名称 like '%" + MyTools.fixSql(ZYMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and a.年级代码='" + MyTools.fixSql(NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCLX_CX)){
			sql += " and b.来源类型='" + MyTools.fixSql(KCLX_CX) + "'";
		}
		sql += " order by a.学年学期编码 desc,a.专业代码,a.年级代码,a.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
    
	/**
	 * 分页查询 科目列表
	 * @date:2016-02-01
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XNXQMC_CX 学年学期名称
	 * @param JXXZ_CX 教学性质
	 * @param ZYMC_CX 专业名称
	 * @param NJDM_CX 年级代码
	 * @param KCMC_CX 课程名称
	 * @param KCLX_CX 课程类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queMkSubjectList(int pageNum, int pageSize, String XNXQMC_CX, String JXXZ_CX, String ZYMC_CX, String NJDM_CX, String KCMC_CX, String KCLX_CX) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select distinct a.科目编号,a.学年学期编码,c.学年学期名称,d.教学性质,a.年级代码,a.年级名称,a.专业代码,a.专业名称,a.课程名称," +
			"case b.来源类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as 课程类型 " +
			//20170110yeq修改查询是否可登分相关代码
//			"case when convert(nvarchar(10),getDate(),21) between convert(nvarchar(10),e.开始时间,21) and convert(nvarchar(10),e.结束时间,21) then 'true' else 'false' end as 登分时间," +
//			"case when (select count(*) from V_成绩管理_教师登分权限表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')>0 then 'true' else 'false' end as 登分权限," +
//			"case when b.打印锁定='0' then 'true' else 'false' end as 打印锁定 " +
			"from V_成绩管理_科目课程信息表 a " +
			"inner join V_成绩管理_登分教师信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 c on c.学年学期编码=a.学年学期编码 " +
			"left join V_基础信息_教学性质 d on d.编号=c.教学性质 " +
			"left join V_成绩管理_登分时间表 e on e.学年学期编码=a.学年学期编码 " +
			"left join V_学校班级数据子类 f on f.行政班代码=b.行政班代码 " +
			"left join V_基础信息_教学班信息表 g on g.教学班编号=b.行政班代码 " +
			"left join V_成绩管理_学生成绩信息表 h on h.科目编号=a.科目编号 " +
			"where (h.平时='-17' or h.期中='-17' or h.实训='-17' or h.期末='-17' or h.总评='-17')";
		
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (b.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or f.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"g.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		//判断查询条件
		if(!"".equalsIgnoreCase(XNXQMC_CX)){
			sql += " and left(c.学年学期编码,5)='" + MyTools.fixSql(XNXQMC_CX) + "'";
		}
		if(!"".equalsIgnoreCase(JXXZ_CX)){
			sql += " and c.教学性质='" + MyTools.fixSql(JXXZ_CX) + "'";
		}
		if(!"".equalsIgnoreCase(ZYMC_CX)){
			sql += " and a.专业名称 like '%" + MyTools.fixSql(ZYMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and a.年级代码='" + MyTools.fixSql(NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(KCMC_CX)){
			sql += " and a.课程名称 like '%" + MyTools.fixSql(KCMC_CX) + "%'";
		}
		if(!"".equalsIgnoreCase(KCLX_CX)){
			sql += " and b.来源类型='" + MyTools.fixSql(KCLX_CX) + "'";
		}
		sql += " order by a.学年学期编码 desc,a.专业代码,a.年级代码,a.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 分页查询 班级列表
	 * @date:2017-05-09
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XNXQBM_CX 学年学期名称
	 * @param XBDM_CX 系部代码
	 * @param ZYDM_CX 专业代码
	 * @param NJDM_CX 年级代码
	 * @param BJDM_CX 班级代码
	 * @param BJLX_CX 班级类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queClassList(int pageNum, int pageSize, String XNXQBM_CX, String XBDM_CX, String ZYDM_CX, String NJDM_CX, String BJDM_CX, String BJLX_CX) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select t.学年学期编码,t.学年学期名称,t.年级代码,t.年级名称,t.系部代码,t1.系部名称,t.专业代码,t.专业名称,t.班级代码,t.班级名称,t.班级类型代码,t.班级类型 " +
			"from (select distinct left(b.学年学期编码,5) as 学年学期编码,b.学年学期名称,b.年级代码,b.年级名称," +
			"case when a.来源类型 in ('1','2') then (case when a.行政班代码 like 'JXB%' then d.系部代码 else c.系部代码 end) else '' end as 系部代码," +
			"case when a.来源类型 in ('1','2') then (case when a.行政班代码 like 'JXB%' then d.专业代码 else c.专业代码 end) else '' end as 专业代码," +
			"case when a.来源类型 in ('1','2') then (case when a.行政班代码 like 'JXB%' then d.专业名称 else c.专业名称 end) else '' end as 专业名称," +
			"case when a.来源类型 in ('3','4') then a.相关编号 else a.行政班代码 end as 班级代码,a.行政班名称 as 班级名称," +
			"case a.来源类型 when '3' then 'xxb' when '4' then 'fcb' else (case when a.行政班代码 like 'JXB_%' then 'jxb' else 'xzb' end) end as 班级类型代码," +
			"case a.来源类型 when '3' then '选修班' when '4' then '分层班' else (case when a.行政班代码 like 'JXB_%' then '教学班' else '行政班' end) end as 班级类型 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join (select aa.行政班代码,aa.系部代码,aa.专业代码,bb.专业名称 from V_学校班级数据子类 aa left join V_专业基本信息数据子类 bb on bb.专业代码=aa.专业代码) c on c.行政班代码=a.行政班代码 " +
			"left join (select aa.教学班编号,aa.系部代码,aa.专业代码,bb.专业名称 from V_基础信息_教学班信息表 aa left join V_专业基本信息数据子类 bb on bb.专业代码=aa.专业代码) d on d.教学班编号=a.行政班代码 " +
			"left join V_成绩管理_学生成绩信息表 e on e.相关编号=a.相关编号 " +
			"where 1=1";
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or d.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") as t " +
			"left join V_基础信息_系部信息表 t1 on t1.系部代码=t.系部代码 " +
			"where 1=1";
		//判断查询条件
		if(!"".equalsIgnoreCase(XNXQBM_CX)){
			sql += " and t.学年学期编码='" + MyTools.fixSql(XNXQBM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(XBDM_CX)){
			sql += " and t.系部代码='" + MyTools.fixSql(XBDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(ZYDM_CX)){
			sql += " and t.专业代码='" + MyTools.fixSql(ZYDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and t.年级代码='" + MyTools.fixSql(NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(BJDM_CX)){
			sql += " and t.班级代码='" + MyTools.fixSql(BJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(BJLX_CX)){
			sql += " and t.班级类型代码='" + MyTools.fixSql(BJLX_CX) + "'";
		}
		sql += " order by t.学年学期编码 desc,t.系部代码,t.年级代码,t.班级代码";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 分页查询 免考班级列表
	 * @date:2017-05-05
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param XNXQBM_CX 学年学期名称
	 * @param XBDM_CX 系部代码
	 * @param ZYDM_CX 专业代码
	 * @param NJDM_CX 年级代码
	 * @param BJDM_CX 班级代码
	 * @param BJLX_CX 班级类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queMkClassList(int pageNum, int pageSize, String XNXQBM_CX, String XBDM_CX, String ZYDM_CX, String NJDM_CX, String BJDM_CX, String BJLX_CX) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select t.学年学期编码,t.学年学期名称,t.年级代码,t.年级名称,t.系部代码,t1.系部名称,t.专业代码,t.专业名称,t.班级代码,t.班级名称,t.班级类型代码,t.班级类型 " +
			"from (select distinct left(b.学年学期编码,5) as 学年学期编码,b.学年学期名称,b.年级代码,b.年级名称," +
			"case when a.来源类型 in ('1','2') then (case when a.行政班代码 like 'JXB%' then c.系部代码 else c.系部代码 end) else '' end as 系部代码,b.专业代码,b.专业名称," +
			"case when a.来源类型 in ('3','4') then a.相关编号 else a.行政班代码 end as 班级代码,a.行政班名称 as 班级名称," +
			"case a.来源类型 when '3' then 'xxb' when '4' then 'fcb' else (case when a.行政班代码 like 'JXB_%' then 'jxb' else 'xzb' end) end as 班级类型代码," +
			"case a.来源类型 when '3' then '选修班' when '4' then '分层班' else (case when a.行政班代码 like 'JXB_%' then '教学班' else '行政班' end) end as 班级类型 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_学校班级数据子类 c on c.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 d on d.教学班编号=a.行政班代码 " +
			"left join V_成绩管理_学生成绩信息表 e on e.相关编号=a.相关编号 " +
			"where e.成绩状态='1' and (e.平时='-17' or e.期中='-17' or e.实训='-17' or e.期末='-17' or e.总评='-17')";
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or d.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") as t " +
			"left join V_基础信息_系部信息表 t1 on t1.系部代码=t.系部代码 " +
			"where 1=1";
		//判断查询条件
		if(!"".equalsIgnoreCase(XNXQBM_CX)){
			sql += " and t.学年学期编码='" + MyTools.fixSql(XNXQBM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(XBDM_CX)){
			sql += " and t.系部代码='" + MyTools.fixSql(XBDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(ZYDM_CX)){
			sql += " and t.专业代码='" + MyTools.fixSql(ZYDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(NJDM_CX)){
			sql += " and t.年级代码='" + MyTools.fixSql(NJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(BJDM_CX)){
			sql += " and t.班级代码='" + MyTools.fixSql(BJDM_CX) + "'";
		}
		if(!"".equalsIgnoreCase(BJLX_CX)){
			sql += " and t.班级类型代码='" + MyTools.fixSql(BJLX_CX) + "'";
		}
		sql += " order by t.学年学期编码 desc,t.系部代码,t.年级代码,t.班级代码";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 分页查询 登分教师科目列表
	 * @date:2017-04-18
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queTeaSubjectList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		
		sql = "select distinct a.科目编号,a.学年学期编码,c.学年学期名称,d.教学性质,a.年级代码,a.年级名称,a.专业代码,a.专业名称,a.课程名称," +
			"case b.来源类型 when '1' then '普通课程' " +
			"when '2' then '添加课程' " +
			"when '3' then '选修课程' " +
			"when '4' then '分层课程' " +
			"else '未知' end as 课程类型 " +
			"from V_成绩管理_科目课程信息表 a " +
			"inner join V_成绩管理_登分教师信息表 b on b.科目编号=a.科目编号 " +
			"left join V_规则管理_学年学期表 c on c.学年学期编码=a.学年学期编码 " +
			"left join V_基础信息_教学性质 d on d.编号=c.教学性质 " +
			"left join V_成绩管理_登分时间表 e on e.学年学期编码=a.学年学期编码 " +
			"where b.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' " +
			"and (a.学年学期编码=(select top 1 学年学期编码 from V_规则管理_学年学期表 where 学期开始时间<=getDate() order by 学年学期编码 desc) " +
			"or a.学年学期编码 in (select 学年学期编码 from V_成绩管理_教师登分权限表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')) " +
			"order by a.学年学期编码 desc,a.专业代码,a.年级代码,a.课程名称";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取文字成绩下拉框数据
	 * @date:2016-02-06
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadWzcjCombo() throws SQLException{
		Vector vec = null;
		String sql = "select * from (select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select distinct 名称 as comboName,代码 as comboValue from V_成绩管理_文字成绩代码表 where 状态='1' and 代码 in ('-2','-3','-4','-5','-17')) as t " +
				"order by cast(comboValue as int) desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取文字成绩显示内容
	 * @date:2016-11-03
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadWzcjShowCombo() throws SQLException{
		Vector vec = null;
		String sql = "select distinct cast(代码 as int) as id,名称 as text from V_成绩管理_文字成绩代码表 where 状态='1' order by cast(代码 as int) desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取班级下拉框数据
	 * @date:2017-05-05
	 * @author:yeq
	 * @param xbdm 系部代码
	 * @param zydm 专业代码
	 * @param njdm 年级代码
	 * @param bjlx 班级类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadClassCombo(String xbdm, String zydm, String njdm, String bjlx) throws SQLException{
		Vector vec = null;
		String sql = "";
		
		//根据班级类型查询班级信息
		if("jxb".equalsIgnoreCase(bjlx)){
			sql = "select '请选择' as comboName,'' as comboValue,'999' as orderNum " +
				"union all " +
				"select 教学班名称,教学班编号,年级代码 from V_基础信息_教学班信息表 where 1=1";
			//判断查询条件
			if(!"".equalsIgnoreCase(xbdm)){
				sql += " and 系部代码='" + MyTools.fixSql(xbdm) + "'";
			}
			if(!"".equalsIgnoreCase(zydm)){
				sql += " and 专业代码='" + MyTools.fixSql(zydm) + "'";
			}
			if(!"".equalsIgnoreCase(njdm)){
				sql += " and 年级代码='" + MyTools.fixSql(njdm) + "'";
			}
			sql += " order by orderNum desc,comboValue";
		}else if("xxb".equalsIgnoreCase(bjlx)){
			sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select 选修班名称,授课计划明细编号,'1' from V_规则管理_选修课授课计划明细表 " +
				"order by orderNum,comboValue desc";
		}else if("fcb".equalsIgnoreCase(bjlx)){
			sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select 分层班名称,分层班编号,'1' from V_规则管理_分层班信息表 a " +
				"left join V_规则管理_分层课程信息表 b on b.分层课程编号=a.分层课程编号 where 1=1";
			//判断查询条件
			if(!"".equalsIgnoreCase(xbdm)){
				sql += " and b.系部代码='" + MyTools.fixSql(xbdm) + "'";
			}
			if(!"".equalsIgnoreCase(njdm)){
				sql += " and b.年级代码='" + MyTools.fixSql(njdm) + "'";
			}
			sql += "order by orderNum,comboValue desc";
		}else{
			sql = "select '请选择' as comboName,'' as comboValue,'999' as orderNum " +
				"union all " +
				"select 行政班名称,行政班代码,年级代码 from V_学校班级数据子类 where 1=1";
			//判断查询条件
			if(!"".equalsIgnoreCase(xbdm)){
				sql += " and 系部代码='" + MyTools.fixSql(xbdm) + "'";
			}
			if(!"".equalsIgnoreCase(zydm)){
				sql += " and 专业代码='" + MyTools.fixSql(zydm) + "'";
			}
			if(!"".equalsIgnoreCase(njdm)){
				sql += " and 年级代码='" + MyTools.fixSql(njdm) + "'";
			}
			sql += " order by orderNum desc,comboValue";
		}
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取学年学期下拉框
	 * @date:2016-06-29
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnxqCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 学年学期名称 as comboName,left(学年学期编码,5) as comboValue,'1' as orderNum " +
				"from V_规则管理_学年学期表 order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取教学性质下拉框
	 * @date:2016-02-01
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
	 * 读取年级下拉框
	 * @date:2016-03-23
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNJDMCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,'1' as orderNum " +
				"from V_学校年级数据子类 where 年级状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXbdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 系部名称,系部代码 from V_基础信息_系部信息表 where 系部代码<>'C00' " +
				"order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2017-04-13
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 查询所选科目所有相关班级课程信息
	 * @date:2016-02-04
	 * @author:yeq
	 * @return 班级信息
	 * @throws SQLException
	 */
	public Vector queClassTree()throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		//删除重复的班级课程
		delRepeatCourse();
		
		sql = "select id,case 打印锁定 when '1' then text+'<font color=\"red\">[已锁定]</font>' else text end as text,state from (" +
			"select distinct a.相关编号 as id,case a.来源类型 when '1' then a.行政班名称+' '+a.课程名称 " +
			"when '2' then a.行政班名称+' '+a.课程名称+'(添加课程)' " +
			"when '3' then a.行政班名称 " +
			"when '4' then a.行政班名称+' '+a.课程名称 else a.课程名称 end as text,state='open',a.行政班代码,a.打印锁定 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 c on c.教学班编号=a.行政班代码 " +
			"where 科目编号='" + MyTools.fixSql(this.getCX_ID()) + "'";
		//根据用户权限判断相关班级
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") as t order by t.行政班代码";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询所选科目所有相关班级课程信息
	 * @date:2017-04-20
	 * @author:yeq
	 * @return 班级信息
	 * @throws SQLException
	 */
	public Vector queMkClassTree()throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select id,case 打印锁定 when '1' then text+'<font color=\"red\">[已锁定]</font>' else text end as text,state from (" +
			"select distinct a.相关编号 as id,case a.来源类型 when '1' then a.行政班名称+' '+a.课程名称 " +
			"when '2' then a.行政班名称+' '+a.课程名称+'(添加课程)' " +
			"when '3' then a.行政班名称 " +
			"when '4' then a.行政班名称+' '+a.课程名称 else a.课程名称 end as text,state='open',a.行政班代码,a.打印锁定 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 c on c.教学班编号=a.行政班代码 " +
			"left join V_成绩管理_学生成绩信息表 d on d.相关编号=a.相关编号 " +
			"where d.成绩状态='1' and a.科目编号='" + MyTools.fixSql(this.getCX_ID()) + "' " +
			"and (d.平时='-17' or d.期中='-17' or d.实训='-17' or d.期末='-17' or d.总评='-17')";
		//根据用户权限判断相关班级
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") as t order by t.行政班代码";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询所选班级所有相关课程信息
	 * @date:2017-05-09
	 * @author:yeq
	 * @return 班级信息
	 * @throws SQLException
	 */
	public Vector loadClassCourseTree(String xnxqbm, String bjdm, String bjlx)throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select id,case 打印锁定 when '1' then text+'<font color=\"red\">[已锁定]</font>' else text end as text,state from (" +
			"select distinct a.相关编号 as id,case a.来源类型 when '1' then a.课程名称 " +
			"when '2' then a.课程名称+'(添加课程)' " +
			"else a.课程名称 end as text,state='open',a.行政班代码,a.打印锁定 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 c on c.教学班编号=a.行政班代码 " +
			"left join V_成绩管理_学生成绩信息表 d on d.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 e on e.科目编号=a.科目编号 " +
			"where 1=1";
		//判断需要查询的班级类型
		if("xzb".equalsIgnoreCase(bjlx) || "jxb".equalsIgnoreCase(bjlx)){
			sql += " and left(e.学年学期编码,5)='" + MyTools.fixSql(xnxqbm) + "' and a.行政班代码='" + MyTools.fixSql(bjdm) + "'";
		}else{
			sql += " and a.相关编号='" + MyTools.fixSql(bjdm) + "'";
		}
		//根据用户权限判断相关班级
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") as t order by t.行政班代码";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 查询所选班级所有免考相关课程信息
	 * @date:2017-05-05
	 * @author:yeq
	 * @return 班级信息
	 * @throws SQLException
	 */
	public Vector loadClassMkCourseTree(String xnxqbm, String bjdm, String bjlx)throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select id,case 打印锁定 when '1' then text+'<font color=\"red\">[已锁定]</font>' else text end as text,state from (" +
			"select distinct a.相关编号 as id,case a.来源类型 when '1' then a.课程名称 " +
			"when '2' then a.课程名称+'(添加课程)' " +
			"else a.课程名称 end as text,state='open',a.行政班代码,a.打印锁定 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_学校班级数据子类 b on b.行政班代码=a.行政班代码 " +
			"left join V_基础信息_教学班信息表 c on c.教学班编号=a.行政班代码 " +
			"left join V_成绩管理_学生成绩信息表 d on d.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 e on e.科目编号=a.科目编号 " +
			"where d.成绩状态='1' and (d.平时='-17' or d.期中='-17' or d.实训='-17' or d.期末='-17' or d.总评='-17')";
		//判断需要查询的班级类型
		if("xzb".equalsIgnoreCase(bjlx) || "jxb".equalsIgnoreCase(bjlx)){
			sql += " and left(e.学年学期编码,5)='" + MyTools.fixSql(xnxqbm) + "' and a.行政班代码='" + MyTools.fixSql(bjdm) + "'";
		}else{
			sql += " and a.相关编号='" + MyTools.fixSql(bjdm) + "'";
		}
		//根据用户权限判断相关班级
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (a.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or b.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "') " +
					"or " +
					"c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += ") as t order by t.行政班代码";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 删除重复的班级课程
	 * @date:2016-02-17
	 * @author:yeq
	 * @throws SQLException
	 */
	public void delRepeatCourse() throws SQLException{
		String sql = "";
		Vector vec = null;
		
		//过滤重复班级课程
		sql = "select 行政班代码,相关编号,登分教师编号,登分教师姓名 from V_成绩管理_登分教师信息表 where 科目编号='" + MyTools.fixSql(this.getCX_ID()) + "' order by 行政班代码,课程代码";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			String classCode = "";
			String code = "";
			String teaCode = "";
			String teaName = "";
			String tempClassCode = "";
			String tempCode = "";
			String tempTeaCode[] = new String[0];
			String tempTeaName[] = new String[0];
			Vector delVec = new Vector();
			Vector sqlVec = new Vector();
			boolean updateFlag = false;
			
			for(int i=0; i<vec.size(); i+=4){
				if(i == 0){
					classCode = MyTools.StrFiltr(vec.get(i));
					code = MyTools.StrFiltr(vec.get(i+1));
					teaCode = MyTools.StrFiltr(vec.get(i+2));
					teaName = MyTools.StrFiltr(vec.get(i+3));
				}else{
					tempClassCode = MyTools.StrFiltr(vec.get(i));
					tempCode = MyTools.StrFiltr(vec.get(i+1));
					tempTeaCode = MyTools.StrFiltr(vec.get(i+2)).split(",");
					tempTeaName = MyTools.StrFiltr(vec.get(i+3)).split(",");
				}
				
				if(i > 0){
					if(classCode.equalsIgnoreCase(tempClassCode) && tempCode.indexOf("XXKMX")>0){
						delVec.add(tempCode);
						
						for(int j=0; j<tempTeaCode.length; j++){
							if(teaCode.indexOf(tempTeaCode[j]) < 0){
								teaCode += ","+tempTeaCode[j];
								teaName += ","+tempTeaName[j];
								updateFlag = true;
							}
						}
					}else{
						if(updateFlag == true){
							sql = "update V_成绩管理_登分教师信息表 set " +
								"登分教师编号='" + MyTools.fixSql(teaCode) + "'," +
								"登分教师姓名='" + MyTools.fixSql(teaName) + "' " +
								"where 相关编号='" + MyTools.fixSql(code) + "'";
							sqlVec.add(sql);
							
							updateFlag = false;
						}
						
						classCode = MyTools.StrFiltr(vec.get(i));
						code = MyTools.StrFiltr(vec.get(i+1));
						teaCode = MyTools.StrFiltr(vec.get(i+2));
						teaName = MyTools.StrFiltr(vec.get(i+3));
					}
				}
			}
			
			if(delVec.size() > 0){
				for(int i=0; i<delVec.size(); i++){
					sql = "delete from V_成绩管理_登分教师信息表 where 相关编号='" + MyTools.fixSql(MyTools.StrFiltr(delVec.get(i))) + "'";
					sqlVec.add(sql);
					
					sql = "delete from V_成绩管理_学生成绩信息表 where 相关编号='" + MyTools.fixSql(MyTools.StrFiltr(delVec.get(i))) + "'";
					sqlVec.add(sql);
				}
				
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					this.setMSG("删除成功");
				}else{
					this.setMSG("删除失败");
				}
			}
		}
	}
	
	/**
	 * 查询科目相关信息
	 * @date:2016-02-05
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadSubjectInfo()throws SQLException{
		String sql = "";
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		sql = "select distinct a.学年学期名称,a.专业名称,a.课程名称 " +
			//20170110yeq修改查询是否可登分相关代码
			//"case when (select count(*) from V_成绩管理_教师登分权限表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')>0 then 'true' else 'false' end as 登分权限," +
			//"case when d.打印锁定='0' then 'true' else 'false' end as 打印锁定 " +
			"from V_成绩管理_科目课程信息表 a " +
			//"left join V_规则管理_学年学期表 b on b.学年学期编码=a.学年学期编码 " +
			//"left join V_成绩管理_登分时间表 c on c.学年学期编码=a.学年学期编码 " +
			//"inner join V_成绩管理_登分教师信息表 d on d.科目编号=a.科目编号 " +
			"where a.科目编号='" + MyTools.fixSql(this.getCX_ID()) + "'";
//		if(this.getAUTH().indexOf(admin) < 0){
//			sql += " and d.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
//		}
		vec = db.GetContextVector(sql);
		return vec;
	}
	
	/**
	 * 查询当前课程是否可登分状态
	 * @date:2017-01-10
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadLockState()throws SQLException{
		String sql = "";
		Vector vec = null;
		
		sql = "select distinct case when convert(nvarchar(10),getDate(),21) " +
			"between convert(nvarchar(10),c.开始时间,21) and convert(nvarchar(10),c.结束时间,21) then 'true' else 'false' end as 登分时间," +
			"case when (select count(*) from V_成绩管理_教师登分权限表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')>0 then 'true' else 'false' end as 登分权限," +
			"case when a.打印锁定='0' then 'true' else 'false' end as 打印锁定 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"left join V_成绩管理_登分时间表 c on c.学年学期编码=b.学年学期编码 " +
			"where a.相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
		vec = db.GetContextVector(sql);
		
		return vec;
	}
	
	/**
	 * 查询登分配置信息
	 * @date:2016-08-31
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadDfConfig()throws SQLException{
		String sql = "";
		Vector vec = null;
		
		//查询是否有登分设置信息，没有的话添加默认信息
		sql = "select count(*) from V_成绩管理_登分设置信息表 where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
		if(!db.getResultFromDB(sql)){
			sql = "insert into V_成绩管理_登分设置信息表 (相关编号,考试类型,总评比例选项,平时比例,期中比例,实训比例,期末比例,成绩类型,实训,创建人,创建时间,状态) " +
				"values ('" + MyTools.fixSql(this.getCX_XGBH()) + "','1','1','50','20','','30','1','0','" + MyTools.fixSql(this.getUSERCODE()) + "',getDate(),'1')";
			if(db.executeInsertOrUpdate(sql)){
				vec = new Vector();
				vec.add("1");
				vec.add("普通考试");
				vec.add("1");
				vec.add("50");
				vec.add("20");
				vec.add("");
				vec.add("30");
				vec.add("1");
				vec.add("数字成绩");
				vec.add("0");
				vec.add("无");
			}
		}else{
			sql = "select 考试类型,case when 考试类型='1' then '普通考试' else '过程性考试' end as 考试类型名称,总评比例选项,平时比例,期中比例,实训比例,期末比例," +
				"成绩类型,case when 成绩类型='1' then '数字成绩' else '文字成绩' end as 成绩类型名称," +
				"实训,case when 实训='0' then '无' else '有' end as 实训 from V_成绩管理_登分设置信息表 where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
			vec = db.GetContextVector(sql);
		}
		
		return vec;
	}
	
	/**
	 * 保存登分设置
	 * @date:2016-02-05
	 * @author:yeq
	 * @throws SQLException
	 */
	public void saveDfsz()throws SQLException{
		Vector sqlVec = new Vector();
		String sql = "";
		Vector vec = null;
		String zpblxx = "";
		String cjlx = "";
		String sx = "";
		String psbl = "";
		String qzbl = "";
		String sxbl = "";
		String qmbl = "";
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP);
		
		//查询原来的科目成绩登记配置
		sql = "select 总评比例选项,成绩类型,实训,平时比例,期中比例,实训比例,期末比例 from V_成绩管理_登分设置信息表 " +
			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
		vec = db.GetContextVector(sql);
		if(vec!=null && vec.size()>0){
			zpblxx = MyTools.StrFiltr(vec.get(0));
			cjlx = MyTools.StrFiltr(vec.get(1));
			sx = MyTools.StrFiltr(vec.get(2));
			psbl = MyTools.StrFiltr(vec.get(3));
			qzbl = MyTools.StrFiltr(vec.get(4));
			sxbl = MyTools.StrFiltr(vec.get(5));
			qmbl = MyTools.StrFiltr(vec.get(6));
		}
		
		//判断是否修改了成绩类型
		if(!cjlx.equalsIgnoreCase(this.getCK_CJLX())){
			//判断如果成绩类型为文字成绩，重置当前科目所有已登记的非文字代码成绩
			if("2".equalsIgnoreCase(this.getCK_CJLX())){
				sql = "update V_成绩管理_学生成绩信息表 set " +
					"平时=case when cast(平时 as float)>-1 then '' else 平时 end," +
					"期中=case when cast(期中 as float)>-1 then '' else 期中 end," +
					"实训=case when cast(实训 as float)>-1 then '' else 实训 end," +
					"期末=case when cast(期末 as float)>-1 then '' else 期末 end," +
					"总评=case when cast(总评 as float)>-1 then '' else 总评 end " +
					"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
				sqlVec.add(sql);
			}
		}
		
		//判断是否修改了总评比例
		if(!zpblxx.equalsIgnoreCase(this.getCK_ZPBLXX())){
			//判断如果总评为仅输总评,清空除总评所有成绩信息（除免修）
			if("5".equalsIgnoreCase(this.getCK_ZPBLXX())){
				sql = "update V_成绩管理_学生成绩信息表 set " +
					"平时=case when 平时 in ('-1') then 平时 else '' end," +
					"期中=case when 期中 in ('-1') then 期中 else '' end," +
					"实训=case when 实训 in ('-1') then 实训 else '' end," +
					"期末=case when 期末 in ('-1') then 期末 else '' end " +
					"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
				sqlVec.add(sql);
//				sql = "update V_成绩管理_学生成绩信息表 set 总评='' " +
//					"where 科目编号='" + MyTools.fixSql(this.getCX_ID()) + "' and cast(总评 as float)>-1";
//				sqlVec.add(sql);
			}
			//判断如果总评为手动输入
			else if("4".equalsIgnoreCase(this.getCK_ZPBLXX())){
//				sql = "update V_成绩管理_学生成绩信息表 set 期末=总评 " +
//					"where 科目编号='" + MyTools.fixSql(this.getCX_ID()) + "' and 总评='-5'";
//				sqlVec.add(sql);
			}
			//判断如果总评为非手动输入,重置总评内容
			else{
				/*
				sql = "update V_成绩管理_学生成绩信息表 set 总评='' " +
					"where 科目编号='" + MyTools.fixSql(this.getCX_ID()) + "'";
				sqlVec.add(sql);
				*/
				
				//判断是否修改了比例
				if(!psbl.equalsIgnoreCase(this.getCK_PSBL()) || !qzbl.equalsIgnoreCase(this.getCK_QZBL()) || !sxbl.equalsIgnoreCase(this.getCK_SXBL()) || !qmbl.equalsIgnoreCase(this.getCK_QMBL())){
					//重新计算当前科目所有学生总评结果
					sql = "select 编号,平时,期中,实训,期末,总评 from V_成绩管理_学生成绩信息表 " +
						"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' and 成绩状态='1'";
					vec = db.GetContextVector(sql);
					
					if(vec!=null && vec.size()>0){
						String code = "";
						String psScore = "";
						String qzScore = "";
						String sxScore = "";
						String qmScore = "";
						String zpScore = "";
						float tempZp = 0;
						String resultZp = "";
						
						for(int i=0; i<vec.size(); i+=6){
							code = MyTools.StrFiltr(vec.get(i));
							tempZp = 0;
							psScore = MyTools.StrFiltr(vec.get(i+1));
							qzScore = MyTools.StrFiltr(vec.get(i+2));
							sxScore = MyTools.StrFiltr(vec.get(i+3));
							qmScore = MyTools.StrFiltr(vec.get(i+4));
							zpScore = MyTools.StrFiltr(vec.get(i+5));
							
							//转换特殊成绩
							if("-2".equalsIgnoreCase(psScore) || "-3".equalsIgnoreCase(psScore) || "-4".equalsIgnoreCase(psScore)) psScore = "0";
							if("-2".equalsIgnoreCase(qzScore) || "-3".equalsIgnoreCase(qzScore) || "-4".equalsIgnoreCase(qzScore)) qzScore = "0";
							if("-2".equalsIgnoreCase(sxScore) || "-3".equalsIgnoreCase(sxScore) || "-4".equalsIgnoreCase(sxScore)) sxScore = "0";
							if("-2".equalsIgnoreCase(qmScore) || "-3".equalsIgnoreCase(qmScore) || "-4".equalsIgnoreCase(qmScore)) psScore = "0";
							
							//判断是否需要计算总评比例，如有任何文字成绩代码不计算
							if((!"".equalsIgnoreCase(this.getCK_PSBL()) && ("".equalsIgnoreCase(psScore) || MyTools.StringToInt(psScore)<0)) ||
								(!"".equalsIgnoreCase(this.getCK_QZBL()) && ("".equalsIgnoreCase(qzScore) || MyTools.StringToInt(qzScore)<0)) ||
								(!"".equalsIgnoreCase(this.getCK_SXBL()) && ("".equalsIgnoreCase(sxScore) || MyTools.StringToInt(sxScore)<0)) ||
								(!"".equalsIgnoreCase(this.getCK_QMBL()) && ("".equalsIgnoreCase(qmScore) || MyTools.StringToInt(qmScore)<0))){
								this.setCX_ZP("");
							}else{
								if(!"".equalsIgnoreCase(this.getCK_PSBL())){
									tempZp += MyTools.StringToFloat(psScore)*MyTools.StringToFloat(this.getCK_PSBL())/100;
								}
								if(!"".equalsIgnoreCase(this.getCK_QZBL())){
									tempZp += MyTools.StringToFloat(qzScore)*MyTools.StringToFloat(this.getCK_QZBL())/100;
								}
								if(!"".equalsIgnoreCase(this.getCK_SXBL())){
									tempZp += MyTools.StringToFloat(sxScore)*MyTools.StringToFloat(this.getCK_SXBL())/100;
								}
								if(!"".equalsIgnoreCase(this.getCK_QMBL())){
									tempZp += MyTools.StringToFloat(qmScore)*MyTools.StringToFloat(this.getCK_QMBL())/100;
								}
								
								//判断总评是否为整数
//								resultZp = String.valueOf(tempZp);
//								resultZp = resultZp.substring(0, resultZp.indexOf(".")) + resultZp.substring(resultZp.indexOf("."), resultZp.indexOf(".")+2);
//								if(".0".equalsIgnoreCase(resultZp.substring(resultZp.length()-2))){
//									resultZp = resultZp.substring(0, resultZp.length()-2);
//								}
								resultZp = f.format(tempZp);
								this.setCX_ZP(resultZp);
							}
							
							sql = "update V_成绩管理_学生成绩信息表 set 总评='" + MyTools.fixSql(this.getCX_ZP()) + "' " +
								"where 编号='" + MyTools.fixSql(code) + "'";
							sqlVec.add(sql);
						}
					}
				}
			}
		}else{
			//自定义总评比例如果修改了比例需重新计算
			if("4".equalsIgnoreCase(this.getCK_ZPBLXX())){
				if(!psbl.equalsIgnoreCase(this.getCK_PSBL()) || !qzbl.equalsIgnoreCase(this.getCK_QZBL()) || !sxbl.equalsIgnoreCase(this.getCK_SXBL()) || !qmbl.equalsIgnoreCase(this.getCK_QMBL())){
					//重新计算当前科目所有学生总评结果
					sql = "select 编号,平时,期中,实训,期末,总评 from V_成绩管理_学生成绩信息表 " +
						"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' and 成绩状态='1'";
					vec = db.GetContextVector(sql);
					
					if(vec!=null && vec.size()>0){
						String code = "";
						String psScore = "";
						String qzScore = "";
						String sxScore = "";
						String qmScore = "";
						String zpScore = "";
						float tempZp = 0;
						String resultZp = "";
						
						for(int i=0; i<vec.size(); i+=6){
							code = MyTools.StrFiltr(vec.get(i));
							tempZp = 0;
							psScore = MyTools.StrFiltr(vec.get(i+1));
							qzScore = MyTools.StrFiltr(vec.get(i+2));
							sxScore = MyTools.StrFiltr(vec.get(i+3));
							qmScore = MyTools.StrFiltr(vec.get(i+4));
							zpScore = MyTools.StrFiltr(vec.get(i+5));
							
							//转换特殊成绩
							if("-2".equalsIgnoreCase(psScore) || "-3".equalsIgnoreCase(psScore) || "-4".equalsIgnoreCase(psScore)) psScore = "0";
							if("-2".equalsIgnoreCase(qzScore) || "-3".equalsIgnoreCase(qzScore) || "-4".equalsIgnoreCase(qzScore)) qzScore = "0";
							if("-2".equalsIgnoreCase(sxScore) || "-3".equalsIgnoreCase(sxScore) || "-4".equalsIgnoreCase(sxScore)) sxScore = "0";
							if("-2".equalsIgnoreCase(qmScore) || "-3".equalsIgnoreCase(qmScore) || "-4".equalsIgnoreCase(qmScore)) psScore = "0";
							
							//判断是否需要计算总评比例，如有任何文字成绩代码不计算
							if((!"".equalsIgnoreCase(this.getCK_PSBL()) && ("".equalsIgnoreCase(psScore) || MyTools.StringToInt(psScore)<0)) ||
								(!"".equalsIgnoreCase(this.getCK_QZBL()) && ("".equalsIgnoreCase(qzScore) || MyTools.StringToInt(qzScore)<0)) ||
								(!"".equalsIgnoreCase(this.getCK_SXBL()) && ("".equalsIgnoreCase(sxScore) || MyTools.StringToInt(sxScore)<0)) ||
								(!"".equalsIgnoreCase(this.getCK_QMBL()) && ("".equalsIgnoreCase(qmScore) || MyTools.StringToInt(qmScore)<0))){
								this.setCX_ZP("");
							}else{
								if(!"".equalsIgnoreCase(this.getCK_PSBL())){
									tempZp += MyTools.StringToFloat(psScore)*MyTools.StringToFloat(this.getCK_PSBL())/100;
								}
								if(!"".equalsIgnoreCase(this.getCK_QZBL())){
									tempZp += MyTools.StringToFloat(qzScore)*MyTools.StringToFloat(this.getCK_QZBL())/100;
								}
								if(!"".equalsIgnoreCase(this.getCK_SXBL())){
									tempZp += MyTools.StringToFloat(sxScore)*MyTools.StringToFloat(this.getCK_SXBL())/100;
								}
								if(!"".equalsIgnoreCase(this.getCK_QMBL())){
									tempZp += MyTools.StringToFloat(qmScore)*MyTools.StringToFloat(this.getCK_QMBL())/100;
								}
								
								//判断总评是否为整数
//								resultZp = String.valueOf(tempZp);
//								resultZp = resultZp.substring(0, resultZp.indexOf(".")) + resultZp.substring(resultZp.indexOf("."), resultZp.indexOf(".")+2);
//								if(".0".equalsIgnoreCase(resultZp.substring(resultZp.length()-2))){
//									resultZp = resultZp.substring(0, resultZp.length()-2);
//								}
								resultZp = f.format(tempZp);
								this.setCX_ZP(String.valueOf(resultZp));
							}
							sql += "update V_成绩管理_学生成绩信息表 set 总评='" + MyTools.fixSql(this.getCX_ZP()) + "' " +
								"where 编号='" + MyTools.fixSql(code) + "'";
							sqlVec.add(sql);
						}
					}
				}
			}
		}
		
		//判断如果修改了实训配置并且设置当前科目没有实训,清空实训成绩
		if(!sx.equalsIgnoreCase(this.getCK_SX()) && "0".equalsIgnoreCase(this.getCK_SX())){
			sql = "update V_成绩管理_学生成绩信息表 set 实训='' " +
				"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
			sqlVec.add(sql);
		}
		
		sql = "update V_成绩管理_登分设置信息表 set " +
			"考试类型='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_KSLX())?"1":this.getCK_KSLX()) + "'," +
			"总评比例选项='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_ZPBLXX())?"1":this.getCK_ZPBLXX()) + "'," +
			"平时比例='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_ZPBLXX())?"50":this.getCK_PSBL()) + "'," +
			"期中比例='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_ZPBLXX())?"20":this.getCK_QZBL()) + "'," +
			"实训比例='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_ZPBLXX())?"":this.getCK_SXBL()) + "'," +
			"期末比例='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_ZPBLXX())?"30":this.getCK_QMBL()) + "'," +
			"成绩类型='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_CJLX())?"1":this.getCK_CJLX()) + "'," +
			"实训='" + MyTools.fixSql("".equalsIgnoreCase(this.getCK_SX())?"0":this.getCK_SX()) + "' " +
			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
		sqlVec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	  * 判断是否为整数  
	  * @param str 传入的字符串  
	  * @return 是整数返回true,否则返回false  
	*/
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();    
  }  
	
	/**
	 * 读取当前选择班级课程的学生列表
	 * @date:2016-02-05
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadStuList()throws SQLException{
		String result = "[";
		String sql = "";
		Vector vec = null;
		
		sql = "select case when b.班内学号='' then '&nbsp;' else b.班内学号 end as 班内学号," +
			"b.学籍号,a.学号,a.姓名,a.成绩状态,a.平时,a.期中,a.实训,a.期末,a.总评 from V_成绩管理_学生成绩信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' " +
			"and b.学生状态 in ('01','05') and a.成绩状态 in ('1','2') order by (case when b.班内学号='' then '9999' else b.班内学号 end)";
		//成绩状态 0无效(目前仅学生名单模块用于显示)/1有效/2免修/3删除操作或异动学生的无效成绩标识
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=10){
				result += "{\"stuClassNum\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
						"\"xjh\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
						"\"stuCode\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
						"\"stuName\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
						"\"stuState\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"," +
						"\"ps\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"," +
						"\"qz\":\"" + MyTools.StrFiltr(vec.get(i+6)) + "\"," +
						"\"sx\":\"" + MyTools.StrFiltr(vec.get(i+7)) + "\"," +
						"\"qm\":\"" + MyTools.StrFiltr(vec.get(i+8)) + "\"," +
						"\"zp\":\"" + MyTools.StrFiltr(vec.get(i+9)) + "\"},";
			}
		}
		
		if(result.length() > 1)
			result = result.substring(0, result.length()-1);
		result += "]";
		return result;
	}
	
	/**
	 * 读取当前选择班级课程的免考学生列表
	 * @date:2017-04-20
	 * @author:yeq
	 * @return String
	 * @throws SQLException
	 */
	public String loadMkStuList()throws SQLException{
		String result = "[";
		String sql = "";
		Vector vec = null;
		
		sql = "select case when b.班内学号='' then '&nbsp;' else b.班内学号 end as 班内学号," +
			"b.学籍号,a.学号,a.姓名,a.成绩状态,a.平时,a.期中,a.实训,a.期末,a.总评 from V_成绩管理_学生成绩信息表 a " +
			"left join V_学生基本数据子类 b on b.学号=a.学号 " +
			"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' " +
			"and b.学生状态 in ('01','05') and a.成绩状态='1' and (a.平时='-17' or a.期中='-17' or a.实训='-17' or a.期末='-17' or a.总评='-17')" +
			"order by (case when b.班内学号='' then '9999' else b.班内学号 end)";
		//成绩状态 0无效(目前仅学生名单模块用于显示)/1有效/2免修/3删除操作或异动学生的无效成绩标识
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i+=10){
				result += "{\"stuClassNum\":\"" + MyTools.StrFiltr(vec.get(i)) + "\"," +
						"\"xjh\":\"" + MyTools.StrFiltr(vec.get(i+1)) + "\"," +
						"\"stuCode\":\"" + MyTools.StrFiltr(vec.get(i+2)) + "\"," +
						"\"stuName\":\"" + MyTools.StrFiltr(vec.get(i+3)) + "\"," +
						"\"stuState\":\"" + MyTools.StrFiltr(vec.get(i+4)) + "\"," +
						"\"ps\":\"" + MyTools.StrFiltr(vec.get(i+5)) + "\"," +
						"\"qz\":\"" + MyTools.StrFiltr(vec.get(i+6)) + "\"," +
						"\"sx\":\"" + MyTools.StrFiltr(vec.get(i+7)) + "\"," +
						"\"qm\":\"" + MyTools.StrFiltr(vec.get(i+8)) + "\"," +
						"\"zp\":\"" + MyTools.StrFiltr(vec.get(i+9)) + "\"},";
			}
		}
		
		if(result.length() > 1)
			result = result.substring(0, result.length()-1);
		result += "]";
		return result;
	}
	
	/**
	 * 保存成绩
	 * @date:2016-02-15
	 * @author:yeq
	 * @param updateInfo 更新的成绩信息
	 * @throws SQLException
	 */
	public void saveStuScore(String updateInfo)throws SQLException{
		Vector sqlVec = new Vector();
		String sql = "";
		String[] updateArray = updateInfo.split(",", -1);
		
		for(int i=0; i<updateArray.length; i+=6){
			sql = "update V_成绩管理_学生成绩信息表 set " +
				"平时='" + MyTools.fixSql(updateArray[i+1]) + "'," +
				"期中='" + MyTools.fixSql(updateArray[i+2]) + "'," +
				"实训='" + MyTools.fixSql(updateArray[i+3]) + "'," +
				"期末='" + MyTools.fixSql(updateArray[i+4]) + "'," +
				"总评='" + MyTools.fixSql(updateArray[i+5]) + "' " +
				"where 相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' and 学号='" + MyTools.fixSql(updateArray[i]) + "'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 读取考分统计信息
	 * @date:2016-03-07
	 * @author:yeq
	 * @param countType 统计方式
	 * @return String
	 * @throws SQLException
	 */
	public String loadKftj(String countType)throws SQLException{
		String result = "";
		String sql = "";
		Vector vec = null;
		String countField = "";
		
		if("1".equalsIgnoreCase(countType))
			countField = "期末";
		else
			countField = "总评";
		
//		sql = "select t.行政班名称+' '+t.课程名称 as 开课班级," +
//			"(select count(*) from V_成绩管理_学生成绩信息表  a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and 成绩状态 in ('1','2') and a.相关编号=t.相关编号) as 总人数," +
//			"(select count(*) from V_成绩管理_学生成绩信息表 a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + " not in ('','-1','-4','-5','-17')) as 已打分人数," +
//			"(select count(*) from V_成绩管理_学生成绩信息表  a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + "='') as 未打分人数," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='2' and a.相关编号=t.相关编号 and " + countField + "='-1') as 免修人数," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + "='-4') as 缺考人数," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + "='-5') as 缓考人数," +
//			"(select count(*) from V_成绩管理_学生成绩信息表  a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + " not in ('-1','-4','-5','-17')) as 实际人数," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and (cast(" + countField + " as float) between 90.0 and 100.0 or " + countField + "='-6')) as 优," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and (cast(" + countField + " as float) between 80.0 and 89.9 or " + countField + "='-7')) as 良," +
//			"(select count(*) from V_成绩管理_学生成绩信息表  a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and (cast(" + countField + " as float) between 70.0 and 79.9 or " + countField + "='-8')) as 中," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and (cast(" + countField + " as float) between 60.0 and 69.9 or " + countField + "='-9')) as 及格," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + "<>'' and (cast(" + countField + " as float)>=0.0 and cast(" + countField + " as float)<60.0 or " + countField + "='-10')) as 不及格," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and cast(" + countField + " as float) between 50.0 and 59.9) as 不及格1," +
//			"(select count(*) from V_成绩管理_学生成绩信息表   a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + "<>'' and cast(" + countField + " as float) between 0.0 and 49.9) as 不及格2," +
//			"(select convert(decimal(18,2),avg(cast(" + countField + " as float))) from V_成绩管理_学生成绩信息表 a left join V_学生基本数据子类 b on b.学号=a.学号 " +
//			"where b.学生状态 in ('01','05') and a.成绩状态='1' and a.相关编号=t.相关编号 and " + countField + "<>'' and cast(" + countField + " as float)>=0.0 group by 相关编号) as 平均分 " +
//			"from V_成绩管理_登分教师信息表 t where t.相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "'";
		sql = "select a.行政班名称+' '+a.课程名称 as 开课班级," +
			"sum(case when b.成绩状态 in ('1','2') then 1 else 0 end) as 总人数," +
			"sum(case when b.成绩状态='1' and " + countField + " in ('','-1','-4','-5') then 0 else 1 end) as 已打分人数," +
			"sum(case when b.成绩状态='1' and " + countField + "='' then 1 else 0 end) as 未打分人数," +
			"sum(case when b.成绩状态='2' and " + countField + "='-1' then 1 else 0 end) as 免修人数," +
			"sum(case when b.成绩状态='1' and " + countField + "='-4' then 1 else 0 end) as 缺考人数," +
			"sum(case when b.成绩状态='1' and " + countField + "='-5' then 1 else 0 end) as 缓考人数," +
			"sum(case when b.成绩状态='1' and " + countField + " in ('-1','-4','-5') then 0 else 1 end) as 实际人数," +
			"sum(case when b.成绩状态='1' and (cast(" + countField + " as float) between 90.0 and 100.0 or " + countField + "='-6') then 1 else 0 end) as 优," +
			"sum(case when b.成绩状态='1' and (cast(" + countField + " as float) between 80.0 and 89.9 or " + countField + "='-7') then 1 else 0 end) as 良," +
			"sum(case when b.成绩状态='1' and (cast(" + countField + " as float) between 70.0 and 79.9 or " + countField + "='-8') then 1 else 0 end) as 中," +
			"sum(case when b.成绩状态='1' and (cast(" + countField + " as float) between 60.0 and 69.9 or " + countField + "='-9') then 1 else 0 end) as 及格," +
			"sum(case when b.成绩状态='1' and (cast(" + countField + " as float)>=0.0 and cast(" + countField + " as float)<60.0 or " + countField + "='-10') then 1 else 0 end) as 不及格," +
			"sum(case when b.成绩状态='1' and (cast(" + countField + " as float) between 50.0 and 59.9) then 1 else 0 end) as 不及格1," +
			"sum(case when b.成绩状态='1' and (cast(" + countField + " as float) between 0.0 and 49.9) then 1 else 0 end) as 不及格2," +
			"convert(decimal(18,2),avg(case when b.成绩状态='1' and " + countField + "<>'' and cast(" + countField + " as float)>=0.0 then cast(" + countField + " as float) end)) as 平均分 " +
			"from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_学生成绩信息表 b on b.相关编号=a.相关编号 " +
			"left join V_学生基本数据子类 c on c.学号=b.学号 " +
			"where a.相关编号='" + MyTools.fixSql(this.getCX_XGBH()) + "' and c.学生状态 in ('01','05','07','08') group by a.行政班名称+' '+a.课程名称";
		vec = db.GetContextVector(sql);
		
		if(vec==null || vec.size()<16){
			for(int i=0; i<16; i++){
				if(i == 7){
					vec.add(1);
				}else{
					vec.add(0);
				}
			}
		}
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");//保留两位小数
		//Float stuNum = MyTools.StringToFloat(MyTools.StrFiltr(vec.get(1)));
		Float realStuNum = MyTools.StringToFloat(MyTools.StrFiltr(vec.get(7)));
		result += "{\"className\":\"" + MyTools.StrFiltr(vec.get(0)) + "\"," +
				"\"stuNum\":\"" + MyTools.StrFiltr(vec.get(1)) + "人\"," +
				"\"ydf\":\"" + MyTools.StrFiltr(vec.get(2)) + "人\"," +
				"\"wdf\":\"" + MyTools.StrFiltr(vec.get(3)) + "人\"," +
				"\"mx\":\"" + MyTools.StrFiltr(vec.get(4)) + "人\"," +
				"\"qk\":\"" + MyTools.StrFiltr(vec.get(5)) + "人\"," +
				"\"hk\":\"" + MyTools.StrFiltr(vec.get(6)) + "人\"," +
				"\"you\":\"" + MyTools.StrFiltr(vec.get(8)) + "人\"," +
				"\"youPercent\":\"" + df.format(MyTools.StringToFloat(MyTools.StrFiltr(vec.get(8)))/realStuNum*100) + "%\"," +
				"\"liang\":\"" + MyTools.StrFiltr(vec.get(9)) + "人\"," +
				"\"liangPercent\":\"" + df.format(MyTools.StringToFloat(MyTools.StrFiltr(vec.get(9)))/realStuNum*100) + "%\"," +
				"\"zhong\":\"" + MyTools.StrFiltr(vec.get(10)) + "人\"," +
				"\"zhongPercent\":\"" + df.format(MyTools.StringToFloat(MyTools.StrFiltr(vec.get(10)))/realStuNum*100) + "%\"," +
				"\"jige\":\"" + MyTools.StrFiltr(vec.get(11)) + "人\"," +
				"\"jigePercent\":\"" + df.format(MyTools.StringToFloat(MyTools.StrFiltr(vec.get(11)))/realStuNum*100) + "%\"," +
				"\"bujige\":\"" + MyTools.StrFiltr(vec.get(12)) + "人\"," +
				"\"bujigePercent\":\"" + df.format(MyTools.StringToFloat(MyTools.StrFiltr(vec.get(12)))/realStuNum*100) + "%\"," +
				"\"bujige1\":\"" + MyTools.StrFiltr(vec.get(13)) + "人\"," +
				"\"bujige1Percent\":\"" + df.format(MyTools.StringToFloat(MyTools.StrFiltr(vec.get(13)))/realStuNum*100) + "%\"," +
				"\"bujige2\":\"" + MyTools.StrFiltr(vec.get(14)) + "人\"," +
				"\"bujige2Percent\":\"" + df.format(MyTools.StringToFloat(MyTools.StrFiltr(vec.get(14)))/realStuNum*100) + "%\"," +
				"\"pjf\":\"" + MyTools.StrFiltr(vec.get(15)) + "\"}";
		return result;
	}
	
	/**
	 * 添加科目学生成绩基础数据
	 * @date:2016-02-15
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public void addSubScoreInfo()throws SQLException{
		Vector vec = null;
		String sql = "";
		boolean flag = true;
		
		//查询当前科目所有相关编号（授课计划编号）
		sql = "select distinct 相关编号 from V_成绩管理_登分教师信息表 where 科目编号='" + MyTools.fixSql(this.getCX_ID()) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i++){
				flag = DfryszBean.addStuScoreInfo(request, MyTools.StrFiltr(vec.get(i)));
				
				if(flag == false){
					break;
				}
			}
		}
		
		if(flag){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 添加班级科目学生成绩基础数据
	 * @date:2017-06-07
	 * @author:yeq
	 * @return Vector
	 * @throws SQLException
	 */
	public void addClassSubScoreInfo(String semCode, String classCode, String classType)throws SQLException{
		Vector vec = null;
		String sql = "";
		boolean flag = true;
		
		//查询当前科目所有相关编号（授课计划编号）
		sql = "select distinct a.相关编号 from V_成绩管理_登分教师信息表 a " +
			"left join V_成绩管理_科目课程信息表 b on b.科目编号=a.科目编号 " +
			"where left(b.学年学期编码,5)='" + MyTools.fixSql(semCode) + "'";
		//判断需要查询的班级类型
		if("xzb".equalsIgnoreCase(classType) || "jxb".equalsIgnoreCase(classType)){
			sql += " and a.行政班代码='" + MyTools.fixSql(classCode) + "'";
		}else{
			sql += " and a.相关编号='" + MyTools.fixSql(classCode) + "'";
		}
		vec = db.GetContextVector(sql);
		
		if(vec!=null && vec.size()>0){
			for(int i=0; i<vec.size(); i++){
				flag = DfryszBean.addStuScoreInfo(request, MyTools.StrFiltr(vec.get(i)));
				
				if(flag == false){
					break;
				}
			}
		}
		
		if(flag){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * 分页查询 查询免考班级列表
	 * @date:2017-09-26
	 * @author:yeq
	 * @param pageNum 页数
	 * @param pageSize 每页数据条数
	 * @param PLDF_XNXQ 学年学期名称
	 * @param PLDF_CJLX 成绩类型
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queMkClassList(int pageNum, int pageSize, String PLDF_XNXQ, String PLDF_CJLX) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		sql = "select a.学号,a.姓名,c.学年学期名称,d.班级名称,b.课程名称,a.平时,a.期中,a.期末,a.总评 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 " +
			"left join V_基础信息_班级信息表 d on d.班级编号=b.行政班代码 " +
			"where a.成绩状态='1' and left(c.学年学期编码,5)='" + MyTools.fixSql(PLDF_XNXQ) + "'";
		if("1".equalsIgnoreCase(PLDF_CJLX)){
			sql += " and a.平时='-17'";
		}else if("2".equalsIgnoreCase(PLDF_CJLX)){
			sql += " and a.期中='-17'";
		}else if("3".equalsIgnoreCase(PLDF_CJLX)){
			sql += " and a.期末='-17'";
		}
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (b.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or d.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += " order by d.系部代码,d.班级编号";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 自动计算免考学生成绩
	 * @date:2017-09-27
	 * @author:yeq
	 * @param PLDF_XNXQ 学年学期名称
	 * @param PLDF_CJLX 成绩类型
	 * @param PLDF_PSZB 平时占比
	 * @param PLDF_QZZB 期中占比
	 * @param PLDF_QMZB 期末占比
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public void autoCount(String PLDF_XNXQ, String PLDF_CJLX, String PLDF_PSZB, String PLDF_QZZB, String PLDF_QMZB) throws SQLException {
		Vector vec = null; // 结果集
		String sql = ""; // 查询用SQL语句
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String psScore = "";
		String qzScore = "";
		String sxScore = "";
		String qmScore = "";
		String zpScore = "";
		String psPercent = "";
		String qzPercent = "";
		String sxPercent = "";
		String qmPercent = "";
		int scoreIndex_1 = 0;
		int scoreIndex_2 = 0;
		String score_1 = "";
		String score_2 = "";
		float percent_1 = 0;
		float percent_2 = 0;
		int scoreIndex = 0;
		DecimalFormat ft = new DecimalFormat("#");  
		ft.setRoundingMode(RoundingMode.HALF_UP);
		float tempZp = 0;
		Vector sqlVec = new Vector();
		
		//查询相关成绩信息
		sql = "select a.编号,a.平时,a.期中,a.实训,a.期末,c.平时比例,c.期中比例,c.实训比例,c.期末比例 " +
			"from V_成绩管理_学生成绩信息表 a " +
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 " +
			"left join V_成绩管理_登分设置信息表 c on c.相关编号=b.相关编号 " +
			"left join V_成绩管理_科目课程信息表 d on d.科目编号=b.科目编号 " +
			"left join V_基础信息_班级信息表 e on e.班级编号=b.行政班代码 " +
			"where a.成绩状态='1' and left(d.学年学期编码,5)='" + MyTools.fixSql(PLDF_XNXQ) + "'";
		if("1".equalsIgnoreCase(PLDF_CJLX)){
			sql += " and a.平时='-17'";
		}else if("2".equalsIgnoreCase(PLDF_CJLX)){
			sql += " and a.期中='-17'";
		}else if("3".equalsIgnoreCase(PLDF_CJLX)){
			sql += " and a.期末='-17'";
		}
		//根据用户权限判断可查询科目范围
		if(this.getAUTH().indexOf(admin)<0 && this.getAUTH().indexOf(jxzgxz)<0 && this.getAUTH().indexOf(qxjdzr)<0 && this.getAUTH().indexOf(qxjwgl)<0){
			sql += " and (b.登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%'";
			if(this.getAUTH().indexOf(xbjdzr)>-1 || this.getAUTH().indexOf(xbjwgl)>-1){
				sql += " or e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
			}
			sql += ")";
		}
		sql += " order by e.系部代码,e.班级编号";
		vec = db.GetContextVector(sql);
		
		//根据免考类型判断需要计算的成绩信息
		if("1".equalsIgnoreCase(PLDF_CJLX)){//平时
			scoreIndex = 1;
			scoreIndex_1 = 2;
			scoreIndex_2 = 4;
			percent_1 = MyTools.StringToFloat(PLDF_QZZB)/100;
			percent_2 = MyTools.StringToFloat(PLDF_QMZB)/100;
		}else if("2".equalsIgnoreCase(PLDF_CJLX)){//期中
			scoreIndex = 2;
			scoreIndex_1 = 1;
			scoreIndex_2 = 4;
			percent_1 = MyTools.StringToFloat(PLDF_PSZB)/100;
			percent_2 = MyTools.StringToFloat(PLDF_QMZB)/100;
		}else if("3".equalsIgnoreCase(PLDF_CJLX)){//期末
			scoreIndex = 4;
			scoreIndex_1 = 1;
			scoreIndex_2 = 2;
			percent_1 = MyTools.StringToFloat(PLDF_PSZB)/100;
			percent_2 = MyTools.StringToFloat(PLDF_QZZB)/100;
		}
		
		//计算免考成绩
		for(int i=0; i<vec.size(); i+=9){
			score_1 = MyTools.StrFiltr(vec.get(i+scoreIndex_1));
			score_2 = MyTools.StrFiltr(vec.get(i+scoreIndex_2));
			
			//判断如果成绩不为空的话计算免考成绩
			if(!"".equalsIgnoreCase(score_1) && MyTools.StringToInt(score_1)>0 && !"".equalsIgnoreCase(score_2) && MyTools.StringToInt(score_2)>0){
				vec.set(i+scoreIndex, ft.format(MyTools.StringToFloat(score_1)*percent_1+MyTools.StringToFloat(score_2)*percent_2));
			}
		}
		
		//计算总评成绩
		for(int i=0; i<vec.size(); i+=9){
			psScore = MyTools.StrFiltr(vec.get(i+1));
			qzScore = MyTools.StrFiltr(vec.get(i+2));
			sxScore = MyTools.StrFiltr(vec.get(i+3));
			qmScore = MyTools.StrFiltr(vec.get(i+4));
			psPercent = MyTools.StrFiltr(vec.get(i+5));
			qzPercent = MyTools.StrFiltr(vec.get(i+6));
			sxPercent = MyTools.StrFiltr(vec.get(i+7));
			qmPercent = MyTools.StrFiltr(vec.get(i+8));
			tempZp = 0;
			
			//判断占比成绩是否为空，为空不计算总评
			if((!"".equalsIgnoreCase(psPercent) && ("".equalsIgnoreCase(psScore) || MyTools.StringToInt(psScore)<0)) 
				|| (!"".equalsIgnoreCase(qzPercent) && ("".equalsIgnoreCase(qzScore) || MyTools.StringToInt(qzScore)<0))
				|| (!"".equalsIgnoreCase(sxPercent) && ("".equalsIgnoreCase(sxScore) || MyTools.StringToInt(sxScore)<0))
				|| (!"".equalsIgnoreCase(qmPercent) && ("".equalsIgnoreCase(qmScore) || MyTools.StringToInt(qmScore)<0))){
				zpScore = "";
			}else{
				if(!"".equalsIgnoreCase(psPercent))
					tempZp += MyTools.StringToFloat(psScore)*MyTools.StringToFloat(psPercent)/100;
				if(!"".equalsIgnoreCase(qzPercent))
					tempZp += MyTools.StringToFloat(qzScore)*MyTools.StringToFloat(qzPercent)/100;
				if(!"".equalsIgnoreCase(sxPercent))
					tempZp += MyTools.StringToFloat(sxScore)*MyTools.StringToFloat(sxPercent)/100;
				if(!"".equalsIgnoreCase(qmPercent))
					tempZp += MyTools.StringToFloat(qmScore)*MyTools.StringToFloat(qmPercent)/100;
				
				zpScore = String.valueOf(ft.format(tempZp));
			}
			
			sql = "update V_成绩管理_学生成绩信息表 set " +
				"平时='" + MyTools.fixSql(psScore) + "'," +
				"期中='" + MyTools.fixSql(qzScore) + "'," +
				"实训='" + MyTools.fixSql(sxScore) + "'," +
				"期末='" + MyTools.fixSql(qmScore) + "'," +
				"总评='" + MyTools.fixSql(zpScore) + "' " +
				"where 编号='" + MyTools.fixSql(MyTools.StrFiltr(vec.get(i))) + "'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
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
	
	public String getCX_ID() {
		return CX_ID;
	}

	public void setCX_ID(String cX_ID) {
		CX_ID = cX_ID;
	}

	public String getCX_XH() {
		return CX_XH;
	}

	public void setCX_XH(String cX_XH) {
		CX_XH = cX_XH;
	}

	public String getCX_XM() {
		return CX_XM;
	}

	public void setCX_XM(String cX_XM) {
		CX_XM = cX_XM;
	}

	public String getCX_XGBH() {
		return CX_XGBH;
	}

	public void setCX_XGBH(String cX_XGBH) {
		CX_XGBH = cX_XGBH;
	}

	public String getCX_PS() {
		return CX_PS;
	}

	public void setCX_PS(String cX_PS) {
		CX_PS = cX_PS;
	}

	public String getCX_QZ() {
		return CX_QZ;
	}

	public void setCX_QZ(String cX_QZ) {
		CX_QZ = cX_QZ;
	}

	public String getCX_SX() {
		return CX_SX;
	}

	public void setCX_SX(String cX_SX) {
		CX_SX = cX_SX;
	}

	public String getCX_QM() {
		return CX_QM;
	}

	public void setCX_QM(String cX_QM) {
		CX_QM = cX_QM;
	}

	public String getCX_ZP() {
		return CX_ZP;
	}

	public void setCX_ZP(String cX_ZP) {
		CX_ZP = cX_ZP;
	}

	public String getCX_CX1() {
		return CX_CX1;
	}

	public void setCX_CX1(String cX_CX1) {
		CX_CX1 = cX_CX1;
	}

	public String getCX_CX2() {
		return CX_CX2;
	}

	public void setCX_CX2(String cX_CX2) {
		CX_CX2 = cX_CX2;
	}

	public String getCX_BK() {
		return CX_BK;
	}

	public void setCX_BK(String cX_BK) {
		CX_BK = cX_BK;
	}

	public String getCX_DBK() {
		return CX_DBK;
	}

	public void setCX_DBK(String cX_DBK) {
		CX_DBK = cX_DBK;
	}

	public String getCX_DYCJ() {
		return CX_DYCJ;
	}

	public void setCX_DYCJ(String cX_DYCJ) {
		CX_DYCJ = cX_DYCJ;
	}

	public String getCX_CJZT() {
		return CX_CJZT;
	}

	public void setCX_CJZT(String cX_CJZT) {
		CX_CJZT = cX_CJZT;
	}

	public String getCX_CJR() {
		return CX_CJR;
	}

	public void setCX_CJR(String cX_CJR) {
		CX_CJR = cX_CJR;
	}

	public String getCX_CJSJ() {
		return CX_CJSJ;
	}

	public void setCX_CJSJ(String cX_CJSJ) {
		CX_CJSJ = cX_CJSJ;
	}

	public String getCX_ZT() {
		return CX_ZT;
	}

	public void setCX_ZT(String cX_ZT) {
		CX_ZT = cX_ZT;
	}

	public String getCK_KSLX() {
		return CK_KSLX;
	}

	public void setCK_KSLX(String cK_KSLX) {
		CK_KSLX = cK_KSLX;
	}

	public String getCK_ZPBLXX() {
		return CK_ZPBLXX;
	}

	public void setCK_ZPBLXX(String cK_ZPBLXX) {
		CK_ZPBLXX = cK_ZPBLXX;
	}

	public String getCK_PSBL() {
		return CK_PSBL;
	}

	public void setCK_PSBL(String cK_PSBL) {
		CK_PSBL = cK_PSBL;
	}

	public String getCK_QZBL() {
		return CK_QZBL;
	}

	public void setCK_QZBL(String cK_QZBL) {
		CK_QZBL = cK_QZBL;
	}

	public String getCK_SXBL() {
		return CK_SXBL;
	}

	public void setCK_SXBL(String cK_SXBL) {
		CK_SXBL = cK_SXBL;
	}

	public String getCK_QMBL() {
		return CK_QMBL;
	}

	public void setCK_QMBL(String cK_QMBL) {
		CK_QMBL = cK_QMBL;
	}

	public String getCK_CJLX() {
		return CK_CJLX;
	}

	public void setCK_CJLX(String cK_CJLX) {
		CK_CJLX = cK_CJLX;
	}

	public String getCK_SX() {
		return CK_SX;
	}

	public void setCK_SX(String cK_SX) {
		CK_SX = cK_SX;
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