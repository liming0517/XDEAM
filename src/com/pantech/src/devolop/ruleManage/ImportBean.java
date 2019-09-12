package com.pantech.src.devolop.ruleManage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import jxl.Sheet;
import jxl.Workbook;
import com.jspsmart.upload.*;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class ImportBean {
	
	private HttpServletRequest request; 
	private DBSource db;
	private String MSG;//提示消息
	private String MSG2;//提示未导入课程
	private String SKTERM;//提示消息
	
	private String GS_XNXQBM;//学年学期编码
	private String GS_XZBDM;//行政班代码
	private String GS_SKJHZBBH;//授课计划主表编号
	private String GS_SKJHMXBH;//授课计划明细编号
	private String GS_ZYBH;//专业编号
	private String GS_KCDM;//课程代码
	private String GS_KCMC;//课程名称
	private String GS_KCLX;//课程类型
	private String ic_XNXQ;//学年学期
	private String ic_ZYMC;//专业
	
	/** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param request 
	*/
	public ImportBean(HttpServletRequest request) {
		// TODO 自动生成的构造函数存根
		
		this.request = request;
	}
		
	//获取excel信息
		public Vector getExcelContent(SmartUpload smartUpload) throws SmartUploadException, IOException{
			Vector vector = new Vector();
			String tempstr = "" ;//
			String path = "c:/temp/upload";
			File f1 = new File(path);  
			//当文件夹不存在时创建
			if (!f1.exists()) {  
			    f1.mkdirs();  
			}
			Workbook workbook = null;
			
			SmartFiles files = smartUpload.getFiles(); //获取文件
			
			SmartFile file= null;
			
			if(files.getCount() > 0){
				 file = files.getFile(0);
				if(file.getSize()<=0){
					MSG = "文件内容为空，请确认！";
				}
				file.saveAs(path +"/"+file.getFileName());
			}
			path=path +"/"+file.getFileName();
			System.out.println("path=:"+path);
					
			File file1=new File(path);
			try {
				InputStream is = new FileInputStream(file1);
				workbook = Workbook.getWorkbook(is);
				Sheet sheet = workbook.getSheet(0); //获取sheet1的数据对象
				int rsRows = sheet.getRows(); //获取sheet1中的总行数
				int rscolumns=sheet.getColumns(); //获取总列数
				System.out.println("rows="+rsRows+"//columns="+rscolumns);
				String temp = "";
				for (int i = 1; i < rsRows; i++) {// 遍历excel文件中的所有行返回一个数组vector数组对象
					tempstr="";
					String cell = MyTools.StrFiltr(sheet.getCell(1, i).getContents()); //获取当前行的第一个字段的内容
					if (!cell.equals("")&&cell!=null){//如果当前行的第一个字段值不为空 
						
						if(sheet.getCell(0, i).getContents()!=""){
							temp =sheet.getCell(0, i).getContents();
						}
						for(int j=1;j<rscolumns;j++){
							tempstr=tempstr+','+sheet.getCell(j, i).getContents();
						}
						tempstr = temp+tempstr;
						System.out.println("tempstr="+tempstr);
					}
					else{
						continue;
					}
					vector.add(tempstr.substring(0,tempstr.toString().length()));
					System.out.println("vector="+vector);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				workbook.close();
			}
			return vector;
		}
	
	/**
	 * @Title: saveimpxls
	 * @Description: 新教学大纲模版解析及保存
	 * @author lupengfei
	 * @date 2015-11-19
	 */
	@SuppressWarnings("unchecked")
	public void saveimportxls(SmartUpload mySmartUpload,String term,String weeks,String ic_XNXQ,String ic_ZYMC) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		//ic_XNXQ:年级表中的所属年份
		DBSource db = new DBSource(request);
		String sql="";
		String sql1="";
		String sql2="";
		String sql3="";
		String sql4="";
		String sql5="";
		String sqlkc="";
		String sqlzy="";
		String sqlbj="";
		String sqlzb="";
		String sqlmx="";
		String sqlex="";
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		Vector veckc = null; // 结果集
		Vector veczy = null; // 结果集
		Vector vecbj = null; // 结果集
		
		String tempsheet="";//sheet名
		String tempsoin="";//第1列
		String tempsoin2="";
		String templine="";//第3列
		String xueqi="";//学期
		String skjhzbbh="";//授课计划主表编号
		String xnxqbm="";//学年学期编码
		String xzbdm="";//行政班代码
		String zydm="";//专业代码
		String zymc="";//专业名称
		String creater="post";//创建人
		String createtime="";//创建时间
		String zt="1";//状态
		String skjhmxbh="";//授课计划明细编号
		String kcdm="";//课程代码
		String kcmc="";//课程名称
		String kclx="";//课程类型(01,02)
		String xuef="";//学分
		String kcsx="";//课程属性
		String kcfx="";//课程类型(A,B,C)
		String kcxz="";//课程性质
		String zoks="";//总课时
		String sysx="";//实验实训
		String skjsbh="000";//授课教师编号
		String skjsxm="请选择";//授课教师姓名
		String js="";//节数
		String lj="1";//连节
		String lc="1";//连次
		String cdyq="";//场地要求
		String cdmc="";//场地名称
		String skzc="";//授课周次
		String skzcxq="";//授课周次详情
		String maxZbId="";//主表id
		String maxMxId="";//明细表id
		int sheetnum=0;//sheet计数
		int zbnum=0;//主表计数
		int mxnum=0;//明细表计数
		String skjhterm="";
		String unImportCourse="";//未导入课程
		skzc="1-"+weeks;
		skzcxq="1-"+weeks;
		
		String path = "c:/temp/upload";
		File f1 = new File(path);  
		//当文件夹不存在时创建
		if (!f1.exists()) {  
		    f1.mkdirs();  
		}
		Workbook workbook = null;
		Vector vectorzb=new Vector();
		Vector vectormx=new Vector();

		SmartFiles files = mySmartUpload.getFiles(); //获取文件
		
		SmartFile file= null;

		//判断
		
		if(files.getCount() > 0){
			file = files.getFile(0);
			if(file.getSize()<=0){
				MSG = "文件内容为空，请确认！";
			}
			file.saveAs(path +"/"+file.getFileName());
		}
		System.out.println("file:"+file);
		path=path +"/"+file.getFileName();
		System.out.println("file=:"+file.getFileName());
		System.out.println("term=:"+term);
		File file1=new File(path);
		
		
		try {
			InputStream is = new FileInputStream(file1);
			workbook = Workbook.getWorkbook(is);
			System.out.println(workbook.getNumberOfSheets());
			
							
			//导入
			for(int k=0;k<workbook.getNumberOfSheets();k++){
				
					Sheet sheet = workbook.getSheet(k); //获取sheet(k)的数据对象
					int rsRows = sheet.getRows(); //获取sheet(k)中的总行数
					int rscolumns=sheet.getColumns(); //获取总列数
					tempsheet=workbook.getSheet(k).getName();//获取sheet名
					System.out.println("sheet:"+tempsheet+"rows="+rsRows+"//columns="+rscolumns);
					if("sheet1".equalsIgnoreCase(tempsheet.trim().toLowerCase())){
						sheetnum++;
						tempsoin=sheet.getCell(0, 0).getContents().trim(); //第1行

						String sqlzymc="select 专业名称 from dbo.V_专业基本信息数据子类 where 专业代码 ='"+MyTools.fixSql(ic_ZYMC)+"' ";
						Vector veczymc=db.GetContextVector(sqlzymc);
						zymc=veczymc.get(0).toString();
							
						
						int lockcdm=-1;
						int lockcmc=-1;
						int lockxuf=-1;
						int lockcsx=-1;
						int lockclx=-1;
						int lockcxz=-1;
						int lockzks=-1;
						int lockssx=-1;
						int[] lockcxq=new int[6];
					
						for(int n=0;n<rscolumns;n++){
							tempsoin=sheet.getCell(n, 1).getContents().trim(); //第2行
							tempsoin2=sheet.getCell(n, 2).getContents().trim(); //第3行

							if(tempsoin.equalsIgnoreCase("课程代码")){
								lockcdm=n;
							}else if(tempsoin.equalsIgnoreCase("课程名称")){
								lockcmc=n;
							}else if(tempsoin.equalsIgnoreCase("学分")){
								lockxuf=n;
							}else if(tempsoin.equalsIgnoreCase("课程属性")){
								lockcsx=n;
							}else if(tempsoin.equalsIgnoreCase("课程类型")){
								lockclx=n;
							}else if(tempsoin.equalsIgnoreCase("课程性质")){
								lockcxz=n;
							}
							
							if("总课时".equalsIgnoreCase(tempsoin2.replaceAll(" ", ""))){
								lockzks=n;
							}else if("实验实训".equalsIgnoreCase(tempsoin2.replaceAll(" ", ""))){
								lockssx=n;
							}else if(tempsoin2.equalsIgnoreCase("一")){
								lockcxq[0]=n;
							}else if(tempsoin2.equalsIgnoreCase("二")){
								lockcxq[1]=n;
							}else if(tempsoin2.equalsIgnoreCase("三")){
								lockcxq[2]=n;
							}else if(tempsoin2.equalsIgnoreCase("四")){
								lockcxq[3]=n;
							}else if(tempsoin2.equalsIgnoreCase("五")){
								lockcxq[4]=n;
							}else if(tempsoin2.equalsIgnoreCase("六")){
								lockcxq[5]=n;
							}		
						}
						
//						for (int i = 3; i < rsRows; i++) {//从第4行遍历excel文件
//							
//							kcdm=sheet.getCell(lockcdm, i).getContents().trim(); //lockcdm列
//							System.out.println(kcdm);
//							if(kcdm.length()==10){
//								if(!kcdm.substring(0, 6).equals("000000")){
//									if(kcdm.indexOf("_")>-1){
//										this.setGS_ZYBH(kcdm.substring(0, 8));
//									}else{
//										this.setGS_ZYBH(kcdm.substring(0, 6));
//									}
//								}
//							}
//						}
						this.setGS_ZYBH(ic_ZYMC);
						
						for (int i = 3; i < rsRows; i++) {//从第4行遍历excel文件
							tempsoin=sheet.getCell(0, i).getContents().trim(); //A列
							if(tempsoin.equalsIgnoreCase("合计")){
								break;
							}
							templine=sheet.getCell(2, i).getContents().trim(); //C列
							if(templine.equalsIgnoreCase("小计")){
								continue;
							}
							
							if(lockcdm==-1){
								kcdm=""; //lockcdm列
							}else{
								kcdm=sheet.getCell(lockcdm, i).getContents().trim(); //lockcdm列
							}
							if(lockcmc==-1){
								kcmc=""; //lockcmc列
							}else{
								kcmc=sheet.getCell(lockcmc, i).getContents().trim(); //lockcmc列
							}
							
							if(lockcsx==-1){
								kcsx=""; //课程属性
							}else{
								kcsx=sheet.getCell(lockcsx, i).getContents().trim(); //课程属性
							}
							if(lockclx==-1){
								kcfx=""; //课程类型(A,B,C)
							}else{
								kcfx=sheet.getCell(lockclx, i).getContents().trim(); //课程类型(A,B,C)
							}
							if(lockcxz==-1){
								kcxz=""; //课程性质
							}else{
								kcxz=sheet.getCell(lockcxz, i).getContents().trim(); //课程性质
							}
							
							if(lockssx==-1){
								sysx=""; //实验实训
							}else{
								sysx=sheet.getCell(lockssx, i).getContents().trim(); //实验实训
							}
							
							
							if(kcmc.indexOf("-")>-1){
								kcmc=kcmc.substring(0, kcmc.indexOf("-")-1);
							}
							this.setGS_KCMC(kcmc);
							
							if(!kcmc.equals("")){//课程名称不为空
								//判断课程子类表中课程名称不存在则添加
//								String sqlkcdm=" select count(*) from dbo.V_课程数据子类 where [专业代码]='' and 课程号='"+MyTools.fixSql(kcdm)+"'";
//								if(db.getResultFromDB(sqlkcdm)){//课程名称存在
//									
//								}else{
//									String insertkcdm=" insert into dbo.V_课程数据子类 (课程号,课程名称,专业代码) values ('"+kcdm+"','"+kcmc+"','"+ic_ZYMC+"') ";
//									db.executeInsertOrUpdate(insertkcdm);
//								}
								
								//判断课程子类表中课程名是否存在
								int isexist=0;
								sqlkc = "select [课程号],[课程名称],[课程类型] from [dbo].[V_课程数据子类] where [课程类型]='01' or ([课程类型]='02' and [专业代码]='"+MyTools.fixSql(ic_ZYMC)+"') ";
								veckc=db.GetContextVector(sqlkc);
								if(veckc!=null&&veckc.size()>0){
									for(int j=0;j<veckc.size();j=j+3){
										//课程名称相等
										if(kcmc.equalsIgnoreCase(veckc.get(j+1).toString())){
											this.setGS_KCDM(veckc.get(j).toString());
											this.setGS_KCLX(veckc.get(j+2).toString());
											isexist=1;
											break;
										}
									}		
								}	
								
								if(isexist==1){
									
									sqlkc=" select 课程名称 from V_课程数据子类 where 课程号='"+MyTools.fixSql(this.getGS_KCDM())+"'";
									veckc=db.GetContextVector(sqlkc);
									if(veckc!=null&&veckc.size()>0){
										this.setGS_KCMC(veckc.get(0).toString());
									}
		//							sqlzy=" select 专业代码 from dbo.V_专业基本信息数据子类 where 专业名称='"+MyTools.fixSql(zymc)+"'";
		//							veczy=db.GetContextVector(sqlzy);
		//							if(veczy!=null&&veczy.size()>0){
		//								this.setGS_ZYBH(veczy.get(0).toString());
		//							}
									//System.out.println(this.getGS_ZYBH()+"|"+xnxqbm);
									
																	
									sqlbj=" select a.行政班代码,a.行政班名称,a.教室编号,b.教室名称 from dbo.V_学校班级数据子类 a left join dbo.V_教室数据类 b on a.教室编号=b.教室编号  where a.专业代码='"+MyTools.fixSql(this.getGS_ZYBH())+"' and a.年级代码='"+ic_XNXQ.substring(2,4)+"1'";
									vecbj=db.GetContextVector(sqlbj);
									if(vecbj!=null&&vecbj.size()>0){
										for(int m=0;m<vecbj.size();m=m+4){
											this.setGS_XZBDM(vecbj.get(m).toString());
											cdyq=vecbj.get(m+2).toString();
											cdmc=vecbj.get(m+3).toString();
											vectorzb=new Vector();	
											zbnum=0;
											int num=0;
											int allnum=0;//总数量
											int firstnum=0;   
											String[] xqnum=new String[6];//课程名后加的数字
										
											//取主表最大id
											sql1="select max(cast(SUBSTRING(授课计划主表编号,6,8) as bigint)) from V_规则管理_授课计划主表";
											vec1 = db.GetContextVector(sql1);
											
											for(int j=0;j<6;j++){
												js=sheet.getCell((lockcxq[j]), i).getContents().trim(); //L-Q列
												if(!js.equalsIgnoreCase("")){//本学期有课程
													num++;
													xqnum[j]=num+"";
													allnum++;	
												}else{
													xqnum[j]="";
												}
												if(term.indexOf(j+"")>-1){//j属于选择的学期则导入
													//js=sheet.getCell((lockcxq[j]), i).getContents().trim(); //L-Q列
													if(!js.equalsIgnoreCase("")){//本学期有课程
														if(num==1){
															firstnum=j;
														}	
													}else{
												
													}
													
													if(j==0){
														this.setGS_XNXQBM(ic_XNXQ+"1"+"01");	
													}else if(j==1){
														this.setGS_XNXQBM(ic_XNXQ+"2"+"01");	
													}else if(j==2){
														this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+1)+"1"+"01");	
													}else if(j==3){
														this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+1)+"2"+"01");		
													}else if(j==4){
														this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+2)+"1"+"01");	
													}else if(j==5){
														this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+2)+"2"+"01");		
													}
													//this.setGS_XNXQBM(ic_XNXQ);
													
													if(i==3){
														/**
														 * 当一个学年学期编码已存在V_规则管理_特殊规则表中，则直接查询；
														 * 否则将V_教职工基本数据子类中所有教师工号插入V_规则管理_特殊规则表的这个学年学期编码下，
														 * 当插入成功后，最后执行查询。
														 * 20170124 lupengfei
														 */
														//if(db.getResultFromDB(sql1)){
														//每次查询之前,先更新一遍当前学年学期下的教师列表,如果有新增的教师编号,则插入,否则直接进行查询列表操作
														//并按照每个查询出来的教师进行插入对应的特殊规则数值
														String sqlts = "select a.工号, d.层级编号 from V_教职工基本数据子类 a" +
																" inner join dbo.sysUserAuth b" +
																" on b.UserCode = a.工号" +
																" inner join dbo.V_权限层级关系表 c" +
																" on b.AuthCode = c.权限编号" +
																" inner join dbo.V_层级表 d" +
																" on c.层级编号=d.层级编号" +
																" where 工号 not in (select 教师编号 from V_规则管理_特殊规则表  where 学年学期编码='"+ this.getGS_XNXQBM() +"')";
														Vector vects = db.GetContextVector(sqlts);
														Vector vecsub=new Vector();
														if(vects!=null && vects.size()>0) {
															String[] shuzhi = new String[5];
															for(int r=0; r<vects.size(); r=r+2) {
																if(vects.get(r+1).toString().equalsIgnoreCase(MyTools.getProp(request, "Base.LingDaoName"))) {
																	System.out.println("1");
																	shuzhi = MyTools.getProp(request, "Base.LingDao").split(",");
																}
																if(vects.get(r+1).toString().equalsIgnoreCase(MyTools.getProp(request, "Base.ZhongGaoName"))) {
																	System.out.println("2");
																	shuzhi = MyTools.getProp(request, "Base.ZhongGao").split(",");
																}
																if(vects.get(r+1).toString().equalsIgnoreCase(MyTools.getProp(request, "Base.ZaiBianXingZhenName"))) {
																	System.out.println("3");
																	shuzhi = MyTools.getProp(request, "Base.ZaiBianXingZhen").split(",");
																}
																if(vects.get(r+1).toString().equalsIgnoreCase(MyTools.getProp(request, "Base.RenShiDaiLiName"))) {
																	System.out.println("4");
																	shuzhi = MyTools.getProp(request, "Base.RenShiDaiLi").split(",");
																}
																if(vects.get(r+1).toString().equalsIgnoreCase(MyTools.getProp(request, "Base.PuTongJSName"))) {
																	System.out.println("5");
																	shuzhi = MyTools.getProp(request, "Base.PuTongJS").split(",");
																}
																if(vects.get(r+1).toString().equalsIgnoreCase(MyTools.getProp(request, "Base.WaiPingJSName"))) {
																	shuzhi = MyTools.getProp(request, "Base.WaiPingJS").split(",");
																}
																String sqlins = "insert into V_规则管理_特殊规则表 values('"+ this.getGS_XNXQBM() +"', '"+ vects.get(r) +"', '"+ shuzhi[0] +"', '"+ shuzhi[1] +"', '"+ shuzhi[2] +"', '"+ shuzhi[3] +"', '"+ shuzhi[4] +"', '"+ MyTools.getSessionUserCode(request) +"', getDate(), 1)";
																vecsub.add(sqlins);
															}
															db.executeInsertOrUpdateTransaction(vecsub);
														}
														//------------------------------------------------
														
														String sqlchcekterm="select count(*) from V_规则管理_学年学期表 where [学年学期编码] = '"+MyTools.fixSql(this.getGS_XNXQBM())+"' ";
														if(!db.getResultFromDB(sqlchcekterm)&&m==0){
															skjhterm+="系统里查询不到"+this.getGS_XNXQBM().substring(0,4)+"年第"+this.getGS_XNXQBM().substring(4,5)+"学期，无法导入。<br />";
															continue;
														}else{	
															String sqlimp=" select count(*) from dbo.V_规则管理_授课计划明细表 where 授课计划主表编号 in " +
																"(select 授课计划主表编号 from dbo.V_规则管理_授课计划主表 where 学年学期编码='"+MyTools.fixSql(this.getGS_XNXQBM())+"' " +
																"and 专业代码='"+MyTools.fixSql(this.getGS_ZYBH())+"' " +
																"and 行政班代码 like '"+ic_XNXQ.substring(2,4)+"%' )";
															if(db.getResultFromDB(sqlimp)&&m==0){
																skjhterm+=this.getGS_XNXQBM().substring(0,4)+"年第"+this.getGS_XNXQBM().substring(4,5)+"学期已导入过授课计划，无法再次导入！如需要重新导入,则需要先删除该专业该学期所有授课计划。<br />";
																continue;
															}
														
															//获取最大主表id
															sql3="select count(*) from V_规则管理_授课计划主表 where " +
																 "学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XNXQBM()))+"' and " +
																 "行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XZBDM()))+"'";
															if(db.getResultFromDB(sql3)){//主表中存在
																sql4="select 授课计划主表编号 from V_规则管理_授课计划主表 where " +
																	 "学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XNXQBM()))+"' and " +
																	 "行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XZBDM()))+"'";
																vec4 = db.GetContextVector(sql4);
																if (vec4 != null && vec4.size() > 0) {
																	this.setGS_SKJHZBBH(vec4.get(0).toString());	
																}
						
															}else{//主表中不存在										
																
																if(vec1.size() > 0){
																	if (vec1.get(0).toString().equals("NULL")||vec1.get(0).toString().equals("") ) {
																		maxZbId = String.format("%08d", (1+zbnum));
																		this.setGS_SKJHZBBH("SKJH_"+maxZbId);
																		zbnum++;
																	}else{
																		maxZbId = String.format("%08d",(Integer.parseInt(MyTools.StrFiltr(vec1.get(0)))+1+zbnum));
																		this.setGS_SKJHZBBH("SKJH_"+maxZbId);
																		zbnum++;
																	}
																}
																
																sqlzb=" insert into dbo.V_规则管理_授课计划主表 (授课计划主表编号,学年学期编码,行政班代码,专业代码,专业名称,创建人,创建时间,状态) values (" +
																	 	"'"+MyTools.fixSql(this.getGS_SKJHZBBH())+"'," +
																	 	"'"+MyTools.fixSql(this.getGS_XNXQBM())+"'," +
																	 	"'"+MyTools.fixSql(this.getGS_XZBDM())+"'," +
																	 	"'"+MyTools.fixSql(this.getGS_ZYBH())+"'," +
																	 	"'"+MyTools.fixSql(zymc)+"'," +
																	 	"'post',getdate(),'1') ";
																vectorzb.add(sqlzb);	
															}
														}
													}
												}
											}
											if(db.executeInsertOrUpdateTransaction(vectorzb)){
												this.MSG="导入完成!";
											}else{
												this.MSG="导入失败";
											}
											
											//判断是否已经有这门课程的授课计划,不存再导入
											sqlex = "select count(*) from [dbo].[V_规则管理_授课计划明细表] a,dbo.V_规则管理_授课计划主表 b where a.[授课计划主表编号]=b.[授课计划主表编号] " +
													"and b.[学年学期编码]='"+MyTools.fixSql(this.getGS_XNXQBM())+"' and b.[行政班代码]='"+MyTools.fixSql(this.getGS_XZBDM())+"' and a.[课程名称]='"+MyTools.fixSql(this.getGS_KCMC())+"' ";
											if(!db.getResultFromDB(sqlex)){
										
												if(num==1&&allnum==1){//只有1个学期上课
													for(int j=0;j<6;j++){
														xqnum[j]="";
													}
												}
												
												int zxqks=0;//总学期课时
												for(int j=0;j<6;j++){
													if(!sheet.getCell((lockcxq[j]), i).getContents().trim().equals("")){
														zxqks+=Double.parseDouble(sheet.getCell((lockcxq[j]), i).getContents().trim());
													}
													
												}
												
												//获取最大明细表id											
												sql2="select max(cast(SUBSTRING(授课计划明细编号,8,11) as bigint)) from V_规则管理_授课计划明细表";
												vec2 = db.GetContextVector(sql2);
												//导入
												for(int j=0;j<6;j++){
													//System.out.println(term+"|"+j);
													if(term.indexOf(j+"")>-1){//j属于选择的学期则导入
														//kcmc=this.getGS_KCMC()+xqnum[j];
														kcmc=this.getGS_KCMC();
														js=sheet.getCell((lockcxq[j]), i).getContents().trim(); //L-Q列
														//System.out.println("js:--"+js+"|"+lockcxq[j]+"|"+j+"|"+i+"|"+lockxuf+"|"+lockzks);
														if(js.equals("")){
															xuef=""; //学分
														}else{
															//if(sheet.getCell(lockxuf, i).getContents().trim().equals("")){
															//	xuef="0";
															//}else{
																//xuef=Double.parseDouble(sheet.getCell(lockxuf, i).getContents().trim())*Double.parseDouble(js)/zxqks+""; //学分
																xuef=js;
															//}
															
														}
														if(js.equals("")){
															zoks=""; //总课时
														}else{
															//if(sheet.getCell(lockzks, i).getContents().trim().equals("")){
															//	zoks="0";
															//}else{
																//zoks=Integer.parseInt(sheet.getCell(lockzks, i).getContents().trim())*Integer.parseInt(js)/zxqks+""; //总课时
																zoks=Integer.parseInt(js)*Integer.parseInt(weeks)+"";
															//}
															
														}
														if(j==0){
															this.setGS_XNXQBM(ic_XNXQ+"1"+"01");	
														}else if(j==1){
															this.setGS_XNXQBM(ic_XNXQ+"2"+"01");	
														}else if(j==2){
															this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+1)+"1"+"01");	
														}else if(j==3){
															this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+1)+"2"+"01");		
														}else if(j==4){
															this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+2)+"1"+"01");	
														}else if(j==5){
															this.setGS_XNXQBM((Integer.parseInt(ic_XNXQ)+2)+"2"+"01");		
														}
														
														//导入2015201学期数据
		//												if(!js.equalsIgnoreCase("")){//本学期有课程	
		//													sqlmx=" update [dbo].[V_规则管理_授课计划明细表] set " +
		//															"[学分]='"+MyTools.fixSql(xuef)+"'," +
		//															"[课程属性]='"+MyTools.fixSql(kcsx)+"'," +
		//															"[课程分类]='"+MyTools.fixSql(kcfx)+"'," +
		//															"[课程性质]='"+MyTools.fixSql(kcxz)+"'," +
		//															"[总课时]='"+MyTools.fixSql(zoks)+"'," +
		//															"[实验实训]='"+MyTools.fixSql(sysx)+"' " +
		//															"where [授课计划明细编号] in ( SELECT [授课计划明细编号] FROM [dbo].[V_规则管理_授课计划明细表] a,dbo.V_规则管理_授课计划主表 b " +
		//															"where a.授课计划主表编号=b.授课计划主表编号 " +
		//															" and b.学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XNXQBM()))+"' " +
		//															" and b.专业代码='"+MyTools.fixSql(this.getGS_ZYBH())+"' " +
		//															" and a.课程代码='"+MyTools.fixSql(kcdm)+"' " +
		//															" and b.行政班代码 like '"+MyTools.fixSql(xnxqbm.substring(2, 4))+"%' ) ";
		//													vectormx.add(sqlmx);
		//												}
														
														if(!js.equalsIgnoreCase("")){//本学期有课程									
																//获取最大主表id
																sql3="select count(*) from V_规则管理_授课计划主表 where " +
																		"学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XNXQBM()))+"' and " +
																		"行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XZBDM()))+"'";
																if(db.getResultFromDB(sql3)){//主表中存在
																	sql4="select 授课计划主表编号 from V_规则管理_授课计划主表 where " +
																		 "学年学期编码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XNXQBM()))+"' and " +
																		 "行政班代码='"+MyTools.StrFiltr(MyTools.fixSql(this.getGS_XZBDM()))+"'";
																	vec4 = db.GetContextVector(sql4);
																	if (vec4 != null && vec4.size() > 0) {
																		this.setGS_SKJHZBBH(vec4.get(0).toString());	
																	}
							
																}else{//主表中不存在										
																	
																}
																
																
																if(vec2.size() > 0){ 
																	if (vec2.get(0).toString().equals("NULL")||vec2.get(0).toString().equals("") ) {
																		maxMxId = String.format("%011d", (1+mxnum));
																		this.setGS_SKJHMXBH("SKJHMX_"+maxMxId);//设置授课计划明细主键
																		mxnum++;
																	}else{
																		maxMxId = String.format("%011d",(Long.parseLong(MyTools.fixSql(MyTools.StrFiltr(vec2.get(0))))+1+mxnum));
																		this.setGS_SKJHMXBH("SKJHMX_"+maxMxId);//设置授课计划明细主键
																		mxnum++;
																	}	
																	
																	String sqlch="select COUNT(*) from dbo.V_规则管理_授课计划明细表 where 授课计划主表编号='"+MyTools.fixSql(this.getGS_SKJHZBBH())+"' and 课程代码='"+MyTools.fixSql(kcdm)+"'";
																	if(db.getResultFromDB(sqlch)){//存在
																	
																	}else{
																		sqlmx=" insert into dbo.V_规则管理_授课计划明细表 (授课计划明细编号,授课计划主表编号,课程代码,课程名称,课程类型,授课教师编号,授课教师姓名,节数,固排已排节数,实际已排节数,连节,连次,固排连次次数,实际连次次数,场地要求,场地名称,授课周次,授课周次详情,创建人,创建时间,状态,考试形式,学分,课程属性,课程分类,课程性质,总课时,实验实训,是否考试) values (" +
																			 	"'"+MyTools.fixSql(this.getGS_SKJHMXBH())+"'," +
																			 	"'"+MyTools.fixSql(this.getGS_SKJHZBBH())+"'," +
																			 	"'"+MyTools.fixSql(this.getGS_KCDM())+"'," +
																			    "'"+MyTools.fixSql(this.getGS_KCMC())+"'," +
																			    "'"+MyTools.fixSql(this.getGS_KCLX())+"'," +
																			    "'"+MyTools.fixSql(skjsbh)+"'," +
																			    "'"+MyTools.fixSql(skjsxm)+"'," +
																			    "'"+MyTools.fixSql(js)+"'," +
																			    "'0','0'," +
																			    "'"+MyTools.fixSql(lj)+"'," +
																			    "'"+MyTools.fixSql(lc)+"'," +
																			    "'0','0'," +
																			    "'"+MyTools.fixSql(cdyq)+"'," +
																			    "'"+MyTools.fixSql(cdmc)+"'," +
																			    "'"+MyTools.fixSql(skzc)+"'," +
																			    "'"+MyTools.fixSql(skzcxq)+"'," +
																			    "'post',getdate(),'1'," +
																			    "''," +
																			    "'"+MyTools.fixSql(xuef)+"'," +
																			    "'"+MyTools.fixSql(kcsx)+"'," +
																			    "'"+MyTools.fixSql(kcfx)+"'," +
																			    "'"+MyTools.fixSql(kcxz)+"'," +
																			    "'"+MyTools.fixSql(zoks)+"'," +
																			    "'"+MyTools.fixSql(sysx)+"'," +
																				"'1') ";
																		vectormx.add(sqlmx);													
																	}
																}
															
															//System.out.println(this.getGS_SKJHZBBH()+" | "+ this.GS_SKJHMXBH +" | " +tempsoin+" | "+templine+" | "+kcdm+" | "+kcmc+" | "+kclx);
															
														}
													}
												}
											}
											//一个班级完成
										}
									}
								}else{//isexist==0 提示用户不存在这门课程
									unImportCourse+=kcmc+"，";
								}
							}//kcdm.length()==10
						}
					}else{//其它页不导入		
						vectormx.add("");	
					}		
			}
			
			//System.out.println("skjhterm="+skjhterm);
			this.SKTERM=skjhterm;
			if(db.executeInsertOrUpdateTransaction(vectormx)){
				this.MSG="导入完成!";
				this.MSG2=unImportCourse;
			}else{
				this.MSG="导入失败";
			}
			if(sheetnum==0){
				this.MSG="未找到sheet1";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			workbook.close();
		}
	}

	/**
	 * @Title: saveimpxls
	 * @Description: 导入交费信息
	 * @author lupengfei
	 * @date 2016-7-19
	 */
	@SuppressWarnings("unchecked")
	public void uploadImportCost(SmartUpload mySmartUpload) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		DBSource db = new DBSource(request);
		String sql="";
		String sql1="";
		String sql2="";
		String sql3="";
		String sql4="";
		String sql5="";
		String sqlkc="";
		String sqlzy="";
		String sqlbj="";
		String sqlzb="";
		String sqlmx="";
		Vector vec1 = null; // 结果集
		Vector vec2 = null; // 结果集
		Vector vec3 = null; // 结果集
		Vector vec4 = null; // 结果集
		Vector veckc = null; // 结果集
		Vector veczy = null; // 结果集
		Vector vecbj = null; // 结果集
		
		String tempsheet="";//sheet名
		String tempsoin="";//第1列
		String tempsoin2="";
		String templine="";//第3列
		int sheetnum=0;//sheet计数
		String KSMC="";//考试名称
		String KSXH="";//学号
		String KSQM="";//考生签名
		
		String path = "c:/temp/upload";
		File f1 = new File(path);  
		//当文件夹不存在时创建
		if (!f1.exists()) {  
		    f1.mkdirs();  
		}
		Workbook workbook = null;
		Vector vectormx=new Vector();

		SmartFiles files = mySmartUpload.getFiles(); //获取文件
		
		SmartFile file= null;

		//判断
		
		if(files.getCount() > 0){
			file = files.getFile(0);
			if(file.getSize()<=0){
				MSG = "文件内容为空，请确认！";
			}
			file.saveAs(path +"/"+file.getFileName());
		}
		path=path +"/"+file.getFileName();
		System.out.println("file=:"+file.getFileName());

		File file1=new File(path);
		
		
		try {
			InputStream is = new FileInputStream(file1);
			workbook = Workbook.getWorkbook(is);
			System.out.println(workbook.getNumberOfSheets());
			
			for(int k=0;k<workbook.getNumberOfSheets();k++){
				
					Sheet sheet = workbook.getSheet(k); //获取sheet(k)的数据对象
					int rsRows = sheet.getRows(); //获取sheet(k)中的总行数
					int rscolumns=sheet.getColumns(); //获取总列数
					tempsheet=workbook.getSheet(k).getName();//获取sheet名
					System.out.println("sheet:"+tempsheet+"rows="+rsRows+"//columns="+rscolumns);
					if("sheet1".equalsIgnoreCase(tempsheet.trim().toLowerCase())){
						sheetnum++;
						tempsoin=sheet.getCell(0, 0).getContents().trim(); //第1行
						KSMC=tempsoin.substring(tempsoin.indexOf("上海行健学院")+6, tempsoin.indexOf("报名表"));
						//获取考试编号
						sql2="select [外考考试编号] from [dbo].[V_外考管理_外考科目设置] where [外考考试名称]='"+MyTools.fixSql(KSMC)+"'";
						vec2=db.GetContextVector(sql2);
						if(vec2!=null&vec2.size()>0){
							this.setGS_SKJHMXBH(vec2.get(0).toString());
						}
						
						int locksxh=-1;//学号
						int locksqm=-1;//考生签名
						for(int n=0;n<rscolumns;n++){
							tempsoin=sheet.getCell(n, 1).getContents().trim(); //第2行
							if(tempsoin.equalsIgnoreCase("学号")){
								locksxh=n;
							}else if(tempsoin.equalsIgnoreCase("考生签名")){
								locksqm=n;
							}
						}
						
						for (int i = 2; i < rsRows; i++) {//从第3行遍历excel文件
							tempsoin=sheet.getCell(0, i).getContents().trim(); //A列
							if(tempsoin.equalsIgnoreCase("人数：")){
								break;
							}
							if(locksxh==-1){
								KSXH=""; //lockcdm列
							}else{
								KSXH=sheet.getCell(locksxh, i).getContents().trim(); //locksxh列
							}
							if(locksqm==-1){
								KSQM=""; //lockcdm列
							}else{
								KSQM=sheet.getCell(locksqm, i).getContents().trim(); //locksqm列
							}
							if(!KSQM.equals("")&&!KSXH.equals("")){//考生签名不为空，已交费
								sqlmx=" update [dbo].[V_外考管理_学生报名表] set [是否交费]='1' " +
									  " where [学号]='"+MyTools.fixSql(KSXH)+"' and [外考考试编号]='"+MyTools.fixSql(this.getGS_SKJHMXBH())+"' ";
								vectormx.add(sqlmx);
							}
						}
					}	
							
			}
			
			//System.out.println("vector="+vector);
			if(db.executeInsertOrUpdateTransaction(vectormx)){
				this.MSG="导入完成!";
			}else{
				this.MSG="导入失败";
			}
			if(sheetnum==0){
				this.MSG="未找到sheet1";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			workbook.close();
		}
	}
	
	/**
	 * @Title: saveimpxls
	 * @Description: 导入考试信息
	 * @author lupengfei
	 * @date 2016-7-19
	 */
	@SuppressWarnings("unchecked")
	public void importExamInfo(SmartUpload mySmartUpload,String importType,String ic_xnxq,String ic_qzqm) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		DBSource db = new DBSource(request);
		String sql="";
		String sqlmx="";
		Vector vec = null; // 结果集
		
		String tempsheet="";//sheet名
		String tempsoin="";//第1列
		String tempsoin2="";
		String templine="";//第3列
		int sheetnum=0;//sheet计数
		String JKJSBH="";//监考教师编号
		String JKJSXM="";//监考教师姓名
		String JKLX="";//监考类型
		String KSJS="";//考试教室
		String allclass="";
		
		String path = "c:/temp/upload";
		File f1 = new File(path);  
		//当文件夹不存在时创建
		if (!f1.exists()) {  
		    f1.mkdirs();  
		}
		Workbook workbook = null;
		Vector vectormx=new Vector();

		SmartFiles files = mySmartUpload.getFiles(); //获取文件
		
		SmartFile file= null;

		//判断
		
		if(files.getCount() > 0){
			file = files.getFile(0);
			if(file.getSize()<=0){
				MSG = "文件内容为空，请确认！";
			}
			file.saveAs(path +"/"+file.getFileName());
		}
		path=path +"/"+file.getFileName();
		System.out.println("file=:"+file.getFileName());

		File file1=new File(path);
		
		
		try {
			InputStream is = new FileInputStream(file1);
			workbook = Workbook.getWorkbook(is);
			System.out.println(workbook.getNumberOfSheets());
			
			for(int k=0;k<workbook.getNumberOfSheets();k++){
				
					Sheet sheet = workbook.getSheet(k); //获取sheet(k)的数据对象
					int rsRows = sheet.getRows(); //获取sheet(k)中的总行数
					int rscolumns=sheet.getColumns(); //获取总列数
					tempsheet=workbook.getSheet(k).getName();//获取sheet名
					System.out.println("sheet:"+tempsheet+"rows="+rsRows+"//columns="+rscolumns);
					if("sheet1".equalsIgnoreCase(tempsheet.trim().toLowerCase())){
						sheetnum++;
						if(importType.equals("teacher")){//导入监考教师
							int locjsbh=-1;//监考教师编号
							int locjsxm=-1;//教师姓名
							int locjklx=-1;//监考类型
							for(int n=0;n<rscolumns;n++){
								tempsoin=sheet.getCell(n, 0).getContents().trim(); //第1行
								if(tempsoin.equalsIgnoreCase("监考教师编号")){
									locjsbh=n;
								}else if(tempsoin.equalsIgnoreCase("监考教师姓名")){
									locjsxm=n;
								}else if(tempsoin.equalsIgnoreCase("监考类型")){
									locjklx=n;
								}
							}
							
							sqlmx="delete from dbo.V_考试管理_监考教师 where [学年学期编码]='"+MyTools.fixSql(ic_xnxq)+"' and [考试周期]='"+MyTools.fixSql(ic_qzqm)+"' ";
							vectormx.add(sqlmx);
							for (int i = 1; i < rsRows; i++) {//从第2行遍历excel文件
								tempsoin=sheet.getCell(0, i).getContents().trim(); //A列
								if(tempsoin.equalsIgnoreCase("")){
									continue;
								}else if(tempsoin.equalsIgnoreCase("监考类型：")){
									break;
								}else{
									if(locjsbh==-1){
										JKJSBH=""; //locjsbh列
									}else{
										JKJSBH=sheet.getCell(locjsbh, i).getContents().trim(); //locjsbh列
									}
									if(locjsxm==-1){
										JKJSXM=""; //locjsxm列
									}else{
										JKJSXM=sheet.getCell(locjsxm, i).getContents().trim(); //locjsxm列
									}
									if(locjklx==-1){
										JKLX=""; //locjklx列
									}else{
										JKLX=sheet.getCell(locjklx, i).getContents().trim(); //locjklx列
									}

									sqlmx=" insert into dbo.V_考试管理_监考教师 ([学年学期编码],[考试周期],[监考教师编号],[监考教师姓名],[监考类型]) values (" +
											"'"+MyTools.fixSql(ic_xnxq)+"'," +//学年学期编码
											"'"+MyTools.fixSql(ic_qzqm)+"'," +//考试周期
											"'"+MyTools.fixSql(JKJSBH)+"'," +//监考教师编号
											"'"+MyTools.fixSql(JKJSXM)+"'," +//监考教师姓名
											"'"+MyTools.fixSql(JKLX)+"') " ;//监考类型
											 
									vectormx.add(sqlmx);
								}
							}
						}else if(importType.equals("classroom")){//导入考试教室
							int locksjs=-1;//考试教室
							for(int n=0;n<rscolumns;n++){
								tempsoin=sheet.getCell(n, 0).getContents().trim(); //第1行
								if(tempsoin.equalsIgnoreCase("考试教室")){
									locksjs=n;
								}
							}
							
							sqlmx="delete from dbo.V_考试管理_考试教室 where [学年学期编码]='"+MyTools.fixSql(ic_xnxq)+"' and [考试周期]='"+MyTools.fixSql(ic_qzqm)+"' ";
							vectormx.add(sqlmx);
							for (int i = 1; i < rsRows; i++) {//从第2行遍历excel文件
								tempsoin=sheet.getCell(0, i).getContents().trim(); //A列
								if(tempsoin.equalsIgnoreCase("")){
									continue;
								}else{
									if(locksjs==-1){
										KSJS=""; //locksjs列
									}else{
										KSJS=sheet.getCell(locksjs, i).getContents().trim(); //locksjs列
									}
									allclass+="'"+KSJS+"',";
								}
							}
							allclass=allclass.substring(0,allclass.length()-1);
							sql="select [教室编号],[教室名称],[教室类型代码],case when 实际容量>48 then '1' else '2' end as 教室容量 from [dbo].[V_教室数据类] where [教室编号] in ("+allclass+")";
							vec=db.GetContextVector(sql);
							if(vec!=null&&vec.size()>0){
								for(int i=0;i<vec.size();i=i+4){			
									sqlmx=" insert into dbo.V_考试管理_考试教室 ([学年学期编码],[考试周期],[教室编号],[教室名称],[教室类型代码],[教室容量]) values (" +
										"'"+MyTools.fixSql(ic_xnxq)+"'," +//学年学期编码
										"'"+MyTools.fixSql(ic_qzqm)+"'," +//考试周期
										"'"+MyTools.fixSql(vec.get(i).toString())+"'," +//教室编号
										"'"+MyTools.fixSql(vec.get(i+1).toString())+"'," +//教室名称
										"'"+MyTools.fixSql(vec.get(i+2).toString())+"'," +//教室类型代码
										"'"+MyTools.fixSql(vec.get(i+3).toString())+"') " ;//教室容量
										 
									vectormx.add(sqlmx);
								}
							}			
						}else{
							this.MSG="导入失败";
						}					
					}				
			}
			
			//System.out.println("vector="+vector);
			if(db.executeInsertOrUpdateTransaction(vectormx)){
				this.MSG="导入完成!";
			}else{
				this.MSG="导入失败";
			}
			if(sheetnum==0){
				this.MSG="未找到sheet1";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			workbook.close();
		}
	}
	
	
	
	
	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getGS_XNXQBM() {
		return GS_XNXQBM;
	}

	public void setGS_XNXQBM(String gS_XNXQBM) {
		GS_XNXQBM = gS_XNXQBM;
	}

	public String getGS_XZBDM() {
		return GS_XZBDM;
	}

	public void setGS_XZBDM(String gS_XZBDM) {
		GS_XZBDM = gS_XZBDM;
	}

	public String getGS_SKJHMXBH() {
		return GS_SKJHMXBH;
	}

	public void setGS_SKJHMXBH(String gS_SKJHMXBH) {
		GS_SKJHMXBH = gS_SKJHMXBH;
	}

	public String getGS_ZYBH() {
		return GS_ZYBH;
	}

	public void setGS_ZYBH(String gS_ZYBH) {
		GS_ZYBH = gS_ZYBH;
	}

	public String getGS_KCMC() {
		return GS_KCMC;
	}

	public void setGS_KCMC(String gS_KCMC) {
		GS_KCMC = gS_KCMC;
	}

	public String getGS_KCLX() {
		return GS_KCLX;
	}

	public void setGS_KCLX(String gS_KCLX) {
		GS_KCLX = gS_KCLX;
	}

	public String getGS_SKJHZBBH() {
		return GS_SKJHZBBH;
	}

	public void setGS_SKJHZBBH(String gS_SKJHZBBH) {
		GS_SKJHZBBH = gS_SKJHZBBH;
	}

	public String getIc_XNXQ() {
		return ic_XNXQ;
	}

	public void setIc_XNXQ(String ic_XNXQ) {
		this.ic_XNXQ = ic_XNXQ;
	}

	public String getIc_ZYMC() {
		return ic_ZYMC;
	}

	public void setIc_ZYMC(String ic_ZYMC) {
		this.ic_ZYMC = ic_ZYMC;
	}

	public String getSKTERM() {
		return SKTERM;
	}

	public void setSKTERM(String sKTERM) {
		SKTERM = sKTERM;
	}

	public String getGS_KCDM() {
		return GS_KCDM;
	}

	public void setGS_KCDM(String gS_KCDM) {
		GS_KCDM = gS_KCDM;
	}

	public String getMSG2() {
		return MSG2;
	}

	public void setMSG2(String mSG2) {
		MSG2 = mSG2;
	}
	
	
}
