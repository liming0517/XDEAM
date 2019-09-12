package com.pantech.src.devolop.queryStatistics;
/*
@date 2015.09.11
@author lupengfei
说明:
重要及特殊方法：
*/
import java.sql.SQLException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class QueryStateBean {
	private String BH;//编号
	private String LX;//类型
	private String XNXQBM;//学年学期编码
	private String XZBDM;//行政班代码
	private String SJXL;//时间序列
	private String SKJHMXBH;//授课计划明细编号
	private String LJXGBH;//连节相关编号
	private String ZT;//状态
	private String XNXQ;//学年学期
	private String TEAID;//学年学期
	private String TEANAME;//学年学期
	private String JXXZ;//教学性质
	private String KCJS;//课程教师
	private String QCXX;//清除的信息
	private String iUSERCODE;//用户编号
	
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	private String TATOL;  //提示信息

	/**
	 * 构造函数
	 * @param request
	 */
	public QueryStateBean(HttpServletRequest request) {
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
		BH = "";//编号
		LX = "";//类型
		XNXQBM = "";//学年学期编码
		XZBDM = "";//行政班代码
		SJXL = "";//时间序列
		SKJHMXBH = "";//授课计划明细编号
	    LJXGBH = "";//连节相关编号
	    ZT = "";//状态
		XNXQ = "";//学年学期
		TEAID="";//教师编号
		TEANAME="";//教师姓名
		JXXZ = "";//教学性质
		KCJS = "";//课程教师
		QCXX = "";//清除的信息
		MSG = ""; //提示信息
	}
	
	//学年学期combobox
	public Vector XNXQCombobox() throws SQLException{
		Vector vec = null;
		
		String sql = "select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		//String sql = "SELECT 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		
		//editTime：20150730，editor：lupengfei，description：需要获取学年学期编码
		//String sql = "select distinct 学年学期编码 AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 where 1=1 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		System.out.println("学年+学期:"+vec.get(0).toString());
		
		return vec;
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
	
	/**
	 * 
	 * @date:2015-09-11
	 * @author:lupengfei
	 * @throws SQLException
	 * @description 教师工作量统计
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
	
	public String queryDate(int pageNum,int pageSize) throws SQLException {
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
		String sjcdbh=null;//实际场地编号
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
		String heban1="";//保存第一项合班授课计划编号
		String heban2="";//保存除了第一项的合班授课计划编号
		String[] skjhmxbhhb1=null;
		String[] classnamehb=null;
		String[] classnumbhb=null;
		
		//查询合班信息
		String sqlhb="select 授课计划明细编号 from dbo.V_规则管理_合班表 where SUBSTRING(授课计划明细编号,0,CHARINDEX('+',授课计划明细编号)) in ( " +
				"select 授课计划明细编号 from dbo.V_规则管理_授课计划明细表 where [授课计划主表编号] in ( " +
				"select [授课计划主表编号] from dbo.V_规则管理_授课计划主表 where [学年学期编码]='"+this.getXNXQBM()+"' ) ) ";
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
				
				String sqltm="select 学年学期编码,节假日期,学期开始时间,实际上课周数 from dbo.V_规则管理_学年学期表 where 学年学期编码='"+this.getXNXQBM()+"' ";
				
				Vector vecxq=db.GetContextVector(sqltm);
				if(vecxq!=null&&vecxq.size()>0){
					this.setMSG(vecxq.get(3).toString());	
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
						" where a.行政班代码=b.行政班代码 and a.授课计划明细编号 != '' and a.状态='1' and a.学年学期编码='"+this.getXNXQBM()+"' order by a.授课计划明细编号,a.授课教师编号,a.行政班名称,a.时间序列,convert(int,a.授课周次)" ;

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
					 " where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号=c.授课计划明细编号  and b.学年学期编码='"+this.getXNXQBM()+"' ";
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
									if(!this.getKCJS().equalsIgnoreCase("请选择")&&!this.getKCJS().equalsIgnoreCase("")){
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
				
//				for(int i=0;i<vec.size();i=i+12){
//					System.out.println("授课计划明细编号:"+vec.get(i).toString()+","+"学年学期编码:"+vec.get(i+1).toString()+","+"授课教师编号:"+vec.get(i+2).toString()+","+"授课教师姓名:"+vec.get(i+3).toString()+","+"专业名称:"+vec.get(i+4).toString()+","+"课程代码:"+vec.get(i+5).toString()+","+"课程名称:"+vec.get(i+6).toString()+","+"行政班代码:"+vec.get(i+7).toString()+","+"行政班名称:"+vec.get(i+8).toString()+","+"时间序列:"+vec.get(i+9).toString()+","+"总人数:"+vec.get(i+10).toString()+","+"授课周次详情:"+vec.get(i+11).toString()+",");
//				}
				
				for(int v=0;v<vec.size();v=v+12){
//					if(!this.getKCJS().equalsIgnoreCase("请选择")&&!this.getKCJS().equalsIgnoreCase("")){
//						if(usercodes.indexOf(vec.get(v+2).toString()+",")>-1){//属于要查询的层级
//							taguc1=1;
//						}else{
//							taguc1=0;
//						}	
//					}else{
//						taguc1=1;
//					}
					if(!this.getTEAID().equalsIgnoreCase("")){
						if(vec.get(v+2).toString().indexOf(this.getTEAID())>-1){
							taguc2=1;
						}else{
							taguc2=0;
						}
					}else{
						taguc2=1;
					}
					if(!this.getTEANAME().equalsIgnoreCase("")){
						if(vec.get(v+3).toString().indexOf(this.getTEANAME())>-1){
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
						
//						String tealv="";
//						for(int i=0;i<veclv.size();i++){
//							if(tealvinfo[i].indexOf("@"+vec.get(v+2).toString()+"@")>-1){
//								tealv=tealvname[i];
//							}							
//						}
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
				
//				for(int i=0;i<vecr.size();i=i+16){
//					if(vecr.get(i).toString().equals("SKJHMX_80000014274")){
//					System.out.println("r授课计划明细编号:"+vecr.get(i).toString()+","+"学年学期编码:"+vecr.get(i+1).toString()+","+"授课教师编号:"+vecr.get(i+2).toString()+","+"授课教师姓名:"+vecr.get(i+3).toString()+","+"层级名称:"+vecr.get(i+4).toString()+","+
//							"专业名称:"+vecr.get(i+5).toString()+","+"课程代码:"+vecr.get(i+6).toString()+","+"课程名称:"+vecr.get(i+7).toString()+","+"行政班代码:"+vecr.get(i+8).toString()+","+"行政班名称:"+vecr.get(i+9).toString()+","+"时间序列:"+vecr.get(i+10).toString()+","+"总人数:"+vecr.get(i+11).toString()+","+
//							"系数:"+vecr.get(i+12).toString()+","+"授课周次详情:"+vecr.get(i+13).toString()+","+"假日周次:"+vecr.get(i+14).toString()+","+"授课周数:"+vecr.get(i+15).toString()+"," );
//					}
//				}
				
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

//				for(int i=0;i<vecs.size();i=i+16){
//					if(vecs.get(i+3).toString().equals("谭颖")){
//					System.out.println("s授课计划明细编号:"+vecs.get(i).toString()+","+"学年学期编码:"+vecs.get(i+1).toString()+","+"授课教师编号:"+vecs.get(i+2).toString()+","+"授课教师姓名:"+vecs.get(i+3).toString()+","+"层级名称:"+vecs.get(i+4).toString()+","+
//						"专业名称:"+vecs.get(i+5).toString()+","+"课程代码:"+vecs.get(i+6).toString()+","+"课程名称:"+vecs.get(i+7).toString()+","+"行政班代码:"+vecs.get(i+8).toString()+","+"行政班名称:"+vecs.get(i+9).toString()+","+"时间序列:"+vecs.get(i+10).toString()+","+"总人数:"+vecs.get(i+11).toString()+","+
//						"系数:"+vecs.get(i+12).toString()+","+"授课周次详情:"+vecs.get(i+13).toString()+","+"假日周次:"+vecs.get(i+14).toString()+","+"授课周数:"+vecs.get(i+15).toString()+"," );
//					}
//				}
				
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

//				for(int i=0;i<vect.size();i=i+16){
//					if(vect.get(i+3).toString().equals("谭颖")){
//					System.out.println("t授课计划明细编号:"+vect.get(i).toString()+","+"学年学期编码:"+vect.get(i+1).toString()+","+"授课教师编号:"+vect.get(i+2).toString()+","+"授课教师姓名:"+vect.get(i+3).toString()+","+"层级名称:"+vect.get(i+4).toString()+","+
//						"专业名称:"+vect.get(i+5).toString()+","+"课程代码:"+vect.get(i+6).toString()+","+"课程名称:"+vect.get(i+7).toString()+","+"行政班代码:"+vect.get(i+8).toString()+","+"行政班名称:"+vect.get(i+9).toString()+","+"每周节数:"+vect.get(i+10).toString()+","+"总人数:"+vect.get(i+11).toString()+","+
//						"系数:"+vect.get(i+12).toString()+","+"授课周次详情:"+vect.get(i+13).toString()+","+"假日周次:"+vect.get(i+14).toString()+","+"授课周数:"+vect.get(i+15).toString()+"," );
//					}
//				}
				
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
		
		
		//System.out.println(pageNum+"|"+pageSize);
		String revec="";
		int total=0;
		if(pageNum*pageSize*16>vecu.size()){
			total=vecu.size();
		}else{
			total=pageNum*pageSize*16;
		}
		 
		for(int m=(pageNum-1)*pageSize*16;m<total;m=m+16){
			revec+="{\"授课计划明细编号\":\""+vecu.get(m).toString()+"\",\"学年学期编码\":\""+vecu.get(m+1).toString()+"\",\"授课教师编号\":\""+vecu.get(m+2).toString()+"\",\"授课教师姓名\":\""+vecu.get(m+3).toString()+"\",\"层级名称\":\""+vecu.get(m+4).toString()+"\",\"专业名称\":\""+vecu.get(m+5).toString()+"\",\"课程代码\":\""+vecu.get(m+6).toString()+"\",\"课程名称\":\""+vecu.get(m+7).toString()+"\",\"行政班代码\":\""+vecu.get(m+8).toString()+"\",\"行政班名称\":\""+vecu.get(m+9).toString()+"\",\"每周节数\":\""+vecu.get(m+10).toString()+"\",\"总人数\":\""+vecu.get(m+11).toString()+"\",\"系数\":\""+vecu.get(m+12).toString()+"\",\"授课周次详情\":\""+vecu.get(m+13).toString()+"\",\"假日周次\":\""+vecu.get(m+14).toString()+"\",\"授课周数\":\""+vecu.get(m+15).toString()+"\",\"MSG\":\""+this.getMSG()+"\"},";
		}
		if(!revec.equals("")){
			revec="["+revec.substring(0, revec.length()-1)+"]";
		}else{
			revec="[]";
		}
		this.setTATOL(vecu.size()/16+"");
		//System.out.println("revec:--"+revec);
		return revec;
	}
	
	/**
	 * 
	 * @date:2015-09-14
	 * @author:lupengfei
	 * @throws SQLException
	 * @description 教师授课情况统计
	 */
	public String queryInfo(int pageNum,int pageSize) throws SQLException {
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
		String[] skjhmxbhhb1=null;//保存授课计划明细编号
		String[] classnamehb=null;//保存对应的班级名称
		String[] classnumbhb=null;//保存合班后的班级总人数
		String heban1="";//保存第一项合班授课计划编号
		String heban2="";//保存除了第一项的合班授课计划编号
		
		//查询合班信息
		String sqlhb="select 授课计划明细编号 from dbo.V_规则管理_合班表 where SUBSTRING(授课计划明细编号,0,CHARINDEX('+',授课计划明细编号)) in ( " +
				"select 授课计划明细编号 from dbo.V_规则管理_授课计划明细表 where [授课计划主表编号] in ( " +
				"select [授课计划主表编号] from dbo.V_规则管理_授课计划主表 where [学年学期编码]='"+this.getXNXQBM()+"' ) ) ";
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
//			String sqllv="select 层级名称 from dbo.V_层级表 where 状态='1' ";
//			Vector veclv=db.GetContextVector(sqllv);
//			String[] tealvinfo=new String[veclv.size()];
//			String[] tealvname=new String[veclv.size()];
//			for(int i=0;i<veclv.size();i++){
//				String sqltea="select c.usercode,e.层级名称 from V_USER_AUTH c,V_权限层级关系表 d,V_层级表 e where c.AuthCode=d.权限编号 and d.层级编号=e.层级编号 and e.层级名称='"+veclv.get(i).toString()+"' and len(usercode)>5";
//				Vector vectea=db.GetContextVector(sqltea);
//				if(vectea!=null&&vectea.size()>0){
//					for(int j=0;j<vectea.size();j=j+2){
//						tealvinfo[i]+="@"+vectea.get(j).toString()+"@,";
//					}					
//				}	
//				tealvname[i]=vectea.get(1).toString();
//			}
				
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
			
			String sqltm="select 学年学期编码,节假日期,学期开始时间,实际上课周数 from dbo.V_规则管理_学年学期表 where 学年学期编码='"+this.getXNXQBM()+"' ";
			
			Vector vecxq=db.GetContextVector(sqltm);
			if(vecxq!=null&&vecxq.size()>0){
				this.setMSG(vecxq.get(3).toString());	
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
				" where a.行政班代码=b.行政班代码 and a.授课计划明细编号=c.授课计划明细编号 and c.考试形式=d.编号 and a.授课计划明细编号 != '' and a.状态='1' and a.学年学期编码='"+this.getXNXQBM()+"' order by a.授课计划明细编号,a.授课教师编号,a.行政班名称,a.时间序列,convert(int,a.授课周次) " ;
			
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
				 " where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号=c.授课计划明细编号  and b.学年学期编码='"+this.getXNXQBM()+"' ";
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
								if(!this.getKCJS().equalsIgnoreCase("请选择")&&!this.getKCJS().equalsIgnoreCase("")){
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

//			for(int i=0;i<vec.size();i=i+14){
//				if(vec.get(i+3).toString().equals("金勇")){
//				System.out.println("授课计划明细编号:"+vec.get(i).toString()+","+"学年学期编码:"+vec.get(i+1).toString()+","+"授课教师编号:"+vec.get(i+2).toString()+","+"授课教师姓名:"+vec.get(i+3).toString()+","+"专业名称:"+vec.get(i+4).toString()+","+"课程代码:"+vec.get(i+5).toString()+","+"课程名称:"+vec.get(i+6).toString()+","+"行政班代码:"+vec.get(i+7).toString()+","+"行政班名称:"+vec.get(i+8).toString()+","+"时间序列:"+vec.get(i+9).toString()+","+"总人数:"+vec.get(i+10).toString()+","+"授课周次详情:"+vec.get(i+11).toString()+",");
//				}
//			}
			
			for(int v=0;v<vec.size();v=v+14){
//				if(!this.getKCJS().equalsIgnoreCase("请选择")&&!this.getKCJS().equalsIgnoreCase("")){
//					if(usercodes.indexOf(vec.get(v+2).toString()+",")>-1){//属于要查询的层级
//						taguc1=1;
//					}else{
//						taguc1=0;
//					}	
//				}else{
//					taguc1=1;
//				}
				if(!this.getTEAID().equalsIgnoreCase("")){
					if(vec.get(v+2).toString().indexOf(this.getTEAID())>-1){
						taguc2=1;
					}else{
						taguc2=0;
					}
				}else{
					taguc2=1;
				}
				if(!this.getTEANAME().equalsIgnoreCase("")){
					if(vec.get(v+3).toString().indexOf(this.getTEANAME())>-1){
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
					
//					String tealv="";
//					for(int i=0;i<veclv.size();i++){
//						if(tealvinfo[i].indexOf("@"+vec.get(v+2).toString()+"@")>-1){
//							tealv=tealvname[i];
//						}							
//					}
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
			
//			for(int i=0;i<vecr.size();i=i+18){
//				System.out.println("r授课计划明细编号:"+vecr.get(i).toString()+","+"学年学期编码:"+vecr.get(i+1).toString()+","+"授课教师编号:"+vecr.get(i+2).toString()+","+"授课教师姓名:"+vecr.get(i+3).toString()+","+"层级名称:"+vecr.get(i+4).toString()+","+
//						"专业名称:"+vecr.get(i+5).toString()+","+"课程代码:"+vecr.get(i+6).toString()+","+"课程名称:"+vecr.get(i+7).toString()+","+"行政班代码:"+vecr.get(i+8).toString()+","+"行政班名称:"+vecr.get(i+9).toString()+","+"时间序列:"+vecr.get(i+10).toString()+","+"总人数:"+vecr.get(i+11).toString()+","+
//						"系数:"+vecr.get(i+12).toString()+","+"授课周次详情:"+vecr.get(i+13).toString()+","+"假日周次:"+vecr.get(i+14).toString()+","+"授课周数:"+vecr.get(i+15).toString()+"," );
//			}
			
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

//			for(int i=0;i<vecs.size();i=i+18){
//				System.out.println("s授课计划明细编号:"+vecs.get(i).toString()+","+"学年学期编码:"+vecs.get(i+1).toString()+","+"授课教师编号:"+vecs.get(i+2).toString()+","+"授课教师姓名:"+vecs.get(i+3).toString()+","+"层级名称:"+vecs.get(i+4).toString()+","+
//					"专业名称:"+vecs.get(i+5).toString()+","+"课程代码:"+vecs.get(i+6).toString()+","+"课程名称:"+vecs.get(i+7).toString()+","+"行政班代码:"+vecs.get(i+8).toString()+","+"行政班名称:"+vecs.get(i+9).toString()+","+"时间序列:"+vecs.get(i+10).toString()+","+"总人数:"+vecs.get(i+11).toString()+","+
//					"系数:"+vecs.get(i+12).toString()+","+"授课周次详情:"+vecs.get(i+13).toString()+","+"假日周次:"+vecs.get(i+14).toString()+","+"授课周数:"+vecs.get(i+15).toString()+"," );
//			}
			
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

//			for(int i=0;i<vect.size();i=i+19){
//				System.out.println("t授课计划明细编号:"+vect.get(i).toString()+","+"学年学期编码:"+vect.get(i+1).toString()+","+"授课教师编号:"+vect.get(i+2).toString()+","+"授课教师姓名:"+vect.get(i+3).toString()+","+"层级名称:"+vect.get(i+4).toString()+","+
//					"专业名称:"+vect.get(i+5).toString()+","+"课程代码:"+vect.get(i+6).toString()+","+"课程名称:"+vect.get(i+7).toString()+","+"行政班代码:"+vect.get(i+8).toString()+","+"行政班名称:"+vect.get(i+9).toString()+","+"每周节数:"+vect.get(i+10).toString()+","+"总人数:"+vect.get(i+11).toString()+","+
//					"系数:"+vect.get(i+12).toString()+","+"授课周次详情:"+vect.get(i+13).toString()+","+"假日周次:"+vect.get(i+14).toString()+","+"授课周数:"+vect.get(i+15).toString()+","+","+"时间序列:"+vect.get(i+18).toString() );
//			}
			
			
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
//				"系数:"+vecu.get(i+12).toString()+","+"授课周次详情:"+vecu.get(i+13).toString()+","+"假日周次:"+vecu.get(i+14).toString()+","+"授课周数:"+vecu.get(i+15).toString()+","+","+"时间序列:"+vecu.get(i+18).toString() );
//		}
		
		
		//System.out.println(pageNum+"|"+pageSize);
		String revec="";
		int total=0;
		if(pageNum*pageSize*19>vecu.size()){
			total=vecu.size();
		}else{
			total=pageNum*pageSize*19;
		}
				 
		for(int m=(pageNum-1)*pageSize*19;m<total;m=m+19){
			revec+="{\"授课计划明细编号\":\""+vecu.get(m).toString()+"\",\"学年学期编码\":\""+vecu.get(m+1).toString()+"\",\"授课教师编号\":\""+vecu.get(m+2).toString()+"\",\"授课教师姓名\":\""+vecu.get(m+3).toString()+"\",\"层级名称\":\""+vecu.get(m+4).toString()+"\",\"专业名称\":\""+vecu.get(m+5).toString()+"\",\"课程代码\":\""+vecu.get(m+6).toString()+"\",\"课程名称\":\""+vecu.get(m+7).toString()+"\",\"行政班代码\":\""+vecu.get(m+8).toString()+"\",\"行政班名称\":\""+vecu.get(m+9).toString()+"\",\"每周节数\":\""+vecu.get(m+10).toString()+"\",\"总人数\":\""+vecu.get(m+11).toString()+"\",\"系数\":\""+vecu.get(m+12).toString()+"\",\"授课周次详情\":\""+vecu.get(m+13).toString()+"\",\"假日周次\":\""+vecu.get(m+14).toString()+"\",\"授课周数\":\""+vecu.get(m+15).toString()+"\",\"场地名称\":\""+vecu.get(m+16).toString()+"\",\"考试形式\":\""+vecu.get(m+17).toString()+"\",\"时间序列\":\""+vecu.get(m+18).toString()+"\",\"MSG\":\""+this.getMSG()+"\"},";
		}
		if(!revec.equals("")){
			revec="["+revec.substring(0, revec.length()-1)+"]";
		}else{
			revec="[]";
		}
		this.setTATOL(vecu.size()/19+"");
		//System.out.println("revec:--"+revec);
		return revec;
					
	}
	
	/**
	 * 教师课时费统计
	 * @author 2017-06-22
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */
	public String querySubsidy(int pageNum,int pageSize) throws SQLException {
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
		Vector vecw=new Vector();
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
		
		String sqlpkgl="";
		String sqlpkgl1="";
		String sqlpkgl4="";
//		int a=0;
//		int b=0;
//		int c=0;
		int d=0;
		int f=0;
		
		//查询职称信息
		String sqlzc="SELECT a.[工号],b.[职称],b.[系数] FROM [dbo].[V_教职工基本数据子类] a,[dbo].[V_基础信息_教师职称] b " +
					 "where a.[职称]=b.[编号] and a.状态='1' ";
		Vector veczc=db.GetContextVector(sqlzc);
		
		//查询合班信息
		String sqlhb="select 授课计划明细编号 from dbo.V_规则管理_合班表 where SUBSTRING(授课计划明细编号,0,CHARINDEX('+',授课计划明细编号)) in ( " +
				"select 授课计划明细编号 from dbo.V_规则管理_授课计划明细表 where [授课计划主表编号] in ( " +
				"select [授课计划主表编号] from dbo.V_规则管理_授课计划主表 where [学年学期编码]='"+this.getXNXQBM()+"' ) ) ";
		Vector vechb = null;
		Vector vechb3 = null;
		vechb=db.GetContextVector(sqlhb);
		if(vechb!=null&&vechb.size()>0){
			String[] skjhmxbhhb1=new String[vechb.size()];//保存授课计划明细编号
			String[] classnamehb=new String[vechb.size()];//保存对应的班级名称
			String[] classnumbhb=new String[vechb.size()];//保存合班后的班级总人数
			String heban1="";//保存第一项合班授课计划编号
			String heban2="";//保存除了第一项的合班授课计划编号
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
						
				//查询层级信息
				String sqllv="select 层级名称 from dbo.V_层级表 where 状态='1' ";
				Vector veclv=db.GetContextVector(sqllv);
				String[] tealvinfo=new String[veclv.size()];
				String[] tealvname=new String[veclv.size()];
				for(int i=0;i<veclv.size();i++){
					String sqltea="select c.usercode,e.层级名称 from V_USER_AUTH c,V_权限层级关系表 d,V_层级表 e where c.AuthCode=d.权限编号 and d.层级编号=e.层级编号 and e.层级名称='"+veclv.get(i).toString()+"' and len(usercode)>5";
					Vector vectea=db.GetContextVector(sqltea);
					if(vectea!=null&&vectea.size()>0){
						for(int j=0;j<vectea.size();j=j+2){
							tealvinfo[i]+="@"+vectea.get(j).toString()+"@,";
						}					
					}	
					tealvname[i]=vectea.get(1).toString();
				}
					
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
				
				String sqltm="select 学年学期编码,节假日期,学期开始时间,实际上课周数 from dbo.V_规则管理_学年学期表 where 学年学期编码='"+this.getXNXQBM()+"' ";
				
				Vector vecxq=db.GetContextVector(sqltm);
				if(vecxq!=null&&vecxq.size()>0){
					this.setMSG(vecxq.get(3).toString());	
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
						" where a.行政班代码=b.行政班代码 and a.授课计划明细编号 != '' and a.状态='1' and a.学年学期编码='"+this.getXNXQBM()+"' order by a.授课计划明细编号,a.授课教师编号,a.行政班名称,a.时间序列,convert(int,a.授课周次)" ;

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
				sql1=" SELECT  a.授课计划明细编号,b.学年学期编码,a.授课教师编号,a.授课教师姓名,'选修课' as 专业名称,b.课程代码,b.课程名称,a.选修班名称 as 行政班代码,a.选修班名称 as 行政班名称,c.时间序列,a.报名人数,a.授课周次 as 授课周次详情 " +
					 " FROM V_规则管理_选修课授课计划明细表 a,V_规则管理_选修课授课计划主表 b,V_排课管理_选修课课程表信息表 c " +
					 " where a.授课计划主表编号=b.授课计划主表编号 and a.授课计划明细编号=c.授课计划明细编号  and b.学年学期编码='"+this.getXNXQBM()+"' ";
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
									if(!this.getKCJS().equalsIgnoreCase("请选择")&&!this.getKCJS().equalsIgnoreCase("")){
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
				if(!this.getKCJS().equalsIgnoreCase("请选择")&&!this.getKCJS().equalsIgnoreCase("")){
					sql3="select usercode from V_USER_AUTH c,V_权限层级关系表 d,V_层级表 e where c.AuthCode=d.权限编号 and d.层级编号=e.层级编号 and e.层级名称='"+this.getKCJS()+"' and len(usercode)>5";
					vec3=db.GetContextVector(sql3);
					if(vec3!=null&&vec3.size()>0){
						for(int i=0;i<vec3.size();i++){
							usercodes+=vec3.get(i).toString()+",";
						}
					}
				}
				//System.out.println("usercodes:----------"+usercodes);
				
//				for(int i=0;i<vec.size();i=i+12){
//					System.out.println("授课计划明细编号:"+vec.get(i).toString()+","+"学年学期编码:"+vec.get(i+1).toString()+","+"授课教师编号:"+vec.get(i+2).toString()+","+"授课教师姓名:"+vec.get(i+3).toString()+","+"专业名称:"+vec.get(i+4).toString()+","+"课程代码:"+vec.get(i+5).toString()+","+"课程名称:"+vec.get(i+6).toString()+","+"行政班代码:"+vec.get(i+7).toString()+","+"行政班名称:"+vec.get(i+8).toString()+","+"时间序列:"+vec.get(i+9).toString()+","+"总人数:"+vec.get(i+10).toString()+","+"授课周次详情:"+vec.get(i+11).toString()+",");
//				}
				
				for(int v=0;v<vec.size();v=v+12){
					if(!this.getKCJS().equalsIgnoreCase("请选择")&&!this.getKCJS().equalsIgnoreCase("")){
						if(usercodes.indexOf(vec.get(v+2).toString()+",")>-1){//属于要查询的层级
							taguc1=1;
						}else{
							taguc1=0;
						}	
					}else{
						taguc1=1;
					}
					if(!this.getTEAID().equalsIgnoreCase("")){
						if(vec.get(v+2).toString().indexOf(this.getTEAID())>-1){
							taguc2=1;
						}else{
							taguc2=0;
						}
					}else{
						taguc2=1;
					}
					if(!this.getTEANAME().equalsIgnoreCase("")){
						if(vec.get(v+3).toString().indexOf(this.getTEANAME())>-1){
							taguc3=1;
						}else{
							taguc3=0;
						}
					}else{
						taguc3=1;
					}
					if(taguc1==1&&taguc2==1&&taguc3==1){
						vecr.add(vec.get(v).toString());//授课计划明细编号 1
						vecr.add(vec.get(v+1).toString());//学年学期编码 2
						vecr.add(vec.get(v+2).toString());//授课教师编号 3
						vecr.add(vec.get(v+3).toString());//授课教师姓名 4
						
						String tealv="";
						for(int i=0;i<veclv.size();i++){
							if(tealvinfo[i].indexOf("@"+vec.get(v+2).toString()+"@")>-1){
								tealv=tealvname[i];
							}							
						}
						vecr.add(tealv);//层级名称 5
						
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
				
//				for(int i=0;i<vecr.size();i=i+16){
//					if(vecr.get(i).toString().equals("SKJHMX_80000014274")){
//					System.out.println("r授课计划明细编号:"+vecr.get(i).toString()+","+"学年学期编码:"+vecr.get(i+1).toString()+","+"授课教师编号:"+vecr.get(i+2).toString()+","+"授课教师姓名:"+vecr.get(i+3).toString()+","+"层级名称:"+vecr.get(i+4).toString()+","+
//							"专业名称:"+vecr.get(i+5).toString()+","+"课程代码:"+vecr.get(i+6).toString()+","+"课程名称:"+vecr.get(i+7).toString()+","+"行政班代码:"+vecr.get(i+8).toString()+","+"行政班名称:"+vecr.get(i+9).toString()+","+"时间序列:"+vecr.get(i+10).toString()+","+"总人数:"+vecr.get(i+11).toString()+","+
//							"系数:"+vecr.get(i+12).toString()+","+"授课周次详情:"+vecr.get(i+13).toString()+","+"假日周次:"+vecr.get(i+14).toString()+","+"授课周数:"+vecr.get(i+15).toString()+"," );
//					}
//				}
				
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
								int rs=Integer.parseInt(classnumbhb[m]);
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

//				for(int i=0;i<vecs.size();i=i+16){
//					if(vecs.get(i+3).toString().equals("谭颖")){
//					System.out.println("s授课计划明细编号:"+vecs.get(i).toString()+","+"学年学期编码:"+vecs.get(i+1).toString()+","+"授课教师编号:"+vecs.get(i+2).toString()+","+"授课教师姓名:"+vecs.get(i+3).toString()+","+"层级名称:"+vecs.get(i+4).toString()+","+
//						"专业名称:"+vecs.get(i+5).toString()+","+"课程代码:"+vecs.get(i+6).toString()+","+"课程名称:"+vecs.get(i+7).toString()+","+"行政班代码:"+vecs.get(i+8).toString()+","+"行政班名称:"+vecs.get(i+9).toString()+","+"时间序列:"+vecs.get(i+10).toString()+","+"总人数:"+vecs.get(i+11).toString()+","+
//						"系数:"+vecs.get(i+12).toString()+","+"授课周次详情:"+vecs.get(i+13).toString()+","+"假日周次:"+vecs.get(i+14).toString()+","+"授课周数:"+vecs.get(i+15).toString()+"," );
//					}
//				}
				
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

//				for(int i=0;i<vect.size();i=i+16){
//					if(vect.get(i+3).toString().equals("谭颖")){
//					System.out.println("t授课计划明细编号:"+vect.get(i).toString()+","+"学年学期编码:"+vect.get(i+1).toString()+","+"授课教师编号:"+vect.get(i+2).toString()+","+"授课教师姓名:"+vect.get(i+3).toString()+","+"层级名称:"+vect.get(i+4).toString()+","+
//						"专业名称:"+vect.get(i+5).toString()+","+"课程代码:"+vect.get(i+6).toString()+","+"课程名称:"+vect.get(i+7).toString()+","+"行政班代码:"+vect.get(i+8).toString()+","+"行政班名称:"+vect.get(i+9).toString()+","+"每周节数:"+vect.get(i+10).toString()+","+"总人数:"+vect.get(i+11).toString()+","+
//						"系数:"+vect.get(i+12).toString()+","+"授课周次详情:"+vect.get(i+13).toString()+","+"假日周次:"+vect.get(i+14).toString()+","+"授课周数:"+vect.get(i+15).toString()+"," );
//					}
//				}
				
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
					vecw.add(vecv.get(i).toString());//授课计划明细编号 1
					vecw.add(vecv.get(i+1).toString());//学年学期编码 2
					vecw.add(vecv.get(i+2).toString());//授课教师编号 3
					vecw.add(vecv.get(i+3).toString());//授课教师姓名 4
					vecw.add(vecv.get(i+4).toString());//层级名称 5
					vecw.add(vecv.get(i+5).toString());//专业名称 6
					vecw.add(vecv.get(i+6).toString());//课程代码 7
					vecw.add(vecv.get(i+7).toString());//课程名称 8
					vecw.add(vecv.get(i+8).toString());//行政班代码 9
					vecw.add(vecv.get(i+9).toString());//行政班名称 10
					vecw.add(vecv.get(i+10).toString());//每周节数（时间序列） 11
					vecw.add(vecv.get(i+11).toString());//总人数 12
					vecw.add(vecv.get(i+12).toString());//系数 13		
					vecw.add(vecv.get(i+13).toString());//授课周次详情 14
					vecw.add(vecv.get(i+14).toString());//假日周次 15
					vecw.add(vecv.get(i+15).toString());//授课周数 16
					
					for(int j=16;j<vecv.size();j=j+16){
						//层级相同
						if(vecv.get(i+4).toString().equals(vecv.get(j+4).toString())){
							vecw.add(vecv.get(j).toString());//授课计划明细编号 1
							vecw.add(vecv.get(j+1).toString());//学年学期编码 2
							vecw.add(vecv.get(j+2).toString());//授课教师编号 3
							vecw.add(vecv.get(j+3).toString());//授课教师姓名 4
							vecw.add(vecv.get(j+4).toString());//层级名称 5
							vecw.add(vecv.get(j+5).toString());//专业名称 6
							vecw.add(vecv.get(j+6).toString());//课程代码 7
							vecw.add(vecv.get(j+7).toString());//课程名称 8
							vecw.add(vecv.get(j+8).toString());//行政班代码 9
							vecw.add(vecv.get(j+9).toString());//行政班名称 10
							vecw.add(vecv.get(j+10).toString());//每周节数（时间序列） 11
							vecw.add(vecv.get(j+11).toString());//总人数 12
							vecw.add(vecv.get(j+12).toString());//系数 13		
							vecw.add(vecv.get(j+13).toString());//授课周次详情 14
							vecw.add(vecv.get(j+14).toString());//假日周次 15
							vecw.add(vecv.get(j+15).toString());//授课周数 16
							
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
				
//				for(int i=0;i<vecw.size();i=i+16){
//					if(vecw.get(i+3).toString().equals("曾恬")){
//					System.out.println("w授课计划明细编号:"+vecw.get(i).toString()+","+"学年学期编码:"+vecw.get(i+1).toString()+","+"授课教师编号:"+vecw.get(i+2).toString()+","+"授课教师姓名:"+vecw.get(i+3).toString()+","+"层级名称:"+vecw.get(i+4).toString()+","+
//						"专业名称:"+vecw.get(i+5).toString()+","+"课程代码:"+vecw.get(i+6).toString()+","+"课程名称:"+vecw.get(i+7).toString()+","+"行政班代码:"+vecw.get(i+8).toString()+","+"行政班名称:"+vecw.get(i+9).toString()+","+"每周节数:"+vecw.get(i+10).toString()+","+"总人数:"+vecw.get(i+11).toString()+","+
//						"系数:"+vecw.get(i+12).toString()+","+"授课周次详情:"+vecw.get(i+13).toString()+","+"假日周次:"+vecw.get(i+14).toString()+","+"授课周数:"+vecw.get(i+15).toString()+"," );
//					}
//				}
					
				//合并
				double zongks=0;//总课时
				double zpjks=0;//周平均课时
				for(int i=0;vecw.size()>1;){
					//计算总课时，周平均课时
					int zs=0;
					int ce=0;
					if(vecw.get(i+4).toString().equals("外聘任课教师")){
						zs=Integer.parseInt(vecw.get(i+15).toString())-Integer.parseInt(vecw.get(i+14).toString());
					}else{
						if(vecw.get(i+1).toString().substring(2,4).equals(vecw.get(i+8).toString().substring(0,2))&&vecw.get(i+1).toString().substring(4,5).equals("1")){//是第一学期
							if(this.getMSG().equals("18")){
								if(Integer.parseInt(vecw.get(i+15).toString())==9||Integer.parseInt(vecw.get(i+15).toString())==8){
									if(vecw.get(i+13).toString().equals("2-9")){
										if(vecw.get(i+13).toString().equals("2-9")){
											zs=9;
										}else if(vecw.get(i+13).toString().equals("10-18")){
											zs=10;
										}else{
											zs=Integer.parseInt(vecw.get(i+15).toString());
										}
									}else if(vecw.get(i+13).toString().equals("10-18")){
										zs=10;
									}else{
										zs=Integer.parseInt(vecw.get(i+15).toString());
									}
								}else if(Integer.parseInt(vecw.get(i+15).toString())==14||Integer.parseInt(vecw.get(i+15).toString())==13){
									zs=14;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==18||Integer.parseInt(vecw.get(i+15).toString())==19||Integer.parseInt(vecw.get(i+15).toString())==17){
									zs=19;
								}else{
									zs=Integer.parseInt(vecw.get(i+15).toString());
									ce=1;
								}
							}else if(this.getMSG().equals("17")){
								if(Integer.parseInt(vecw.get(i+15).toString())==9||Integer.parseInt(vecw.get(i+15).toString())==7||Integer.parseInt(vecw.get(i+15).toString())==8){
									if(vecw.get(i+13).toString().equals("2-9")){
										zs=9;
									}else if(vecw.get(i+13).toString().equals("10-17")){
										zs=10;
									}else{
										zs=Integer.parseInt(vecw.get(i+15).toString());
									}
								}else if(Integer.parseInt(vecw.get(i+15).toString())==14||Integer.parseInt(vecw.get(i+15).toString())==12||Integer.parseInt(vecw.get(i+15).toString())==13){
									zs=14;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==18||Integer.parseInt(vecw.get(i+15).toString())==16||Integer.parseInt(vecw.get(i+15).toString())==17){
									zs=19;
								}else{
									zs=Integer.parseInt(vecw.get(i+15).toString());
									ce=1;
								}
							}else if(this.getMSG().equals("19")){
								if(Integer.parseInt(vecw.get(i+15).toString())==9||Integer.parseInt(vecw.get(i+15).toString())==8){
									zs=9;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==14||Integer.parseInt(vecw.get(i+15).toString())==13){
									zs=14;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==18||Integer.parseInt(vecw.get(i+15).toString())==19){
									zs=19;
								}else{
									zs=Integer.parseInt(vecw.get(i+15).toString());
									ce=1;
								}
							}
							
						}else{
							if(this.getMSG().equals("18")){
								if(Integer.parseInt(vecw.get(i+15).toString())==9||Integer.parseInt(vecw.get(i+15).toString())==10){
									zs=10;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==14||Integer.parseInt(vecw.get(i+15).toString())==15){
									zs=15;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==18||Integer.parseInt(vecw.get(i+15).toString())==19||Integer.parseInt(vecw.get(i+15).toString())==20){
									zs=20;
								}else{
									zs=Integer.parseInt(vecw.get(i+15).toString());
									ce=1;
								}
							}else if(this.getMSG().equals("17")){
								if(Integer.parseInt(vecw.get(i+15).toString())==9||Integer.parseInt(vecw.get(i+15).toString())==8){
									zs=10;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==14||Integer.parseInt(vecw.get(i+15).toString())==13){
									zs=15;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==18||Integer.parseInt(vecw.get(i+15).toString())==19||Integer.parseInt(vecw.get(i+15).toString())==20||Integer.parseInt(vecw.get(i+15).toString())==17){
									zs=20;
								}else{
									zs=Integer.parseInt(vecw.get(i+15).toString());
									ce=1;
								}
							}else if(this.getMSG().equals("19")){
								if(Integer.parseInt(vecw.get(i+15).toString())==9||Integer.parseInt(vecw.get(i+15).toString())==10){
									zs=10;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==14||Integer.parseInt(vecw.get(i+15).toString())==15){
									zs=15;
								}else if(Integer.parseInt(vecw.get(i+15).toString())==18||Integer.parseInt(vecw.get(i+15).toString())==19||Integer.parseInt(vecw.get(i+15).toString())==20){
									zs=20;
								}else{
									zs=Integer.parseInt(vecw.get(i+15).toString());
									ce=1;
								}
							}
							
						}
					}
					
					double xs=Double.parseDouble(vecw.get(i+12).toString());//系数
					double zks=0;
					double zzs=0;
					double pjks=0;
					if(vecw.get(i+4).toString().equals("外聘任课教师")){
						zks=(Integer.parseInt(vecw.get(i+10).toString())*zs)*xs;
						zzs=Double.parseDouble(this.getMSG());
						zongks=zks;
						pjks=(zks/zzs);
						zpjks=pjks;
					}else if(vecw.get(i+4).toString().equals("")){
						zks=0;
						zzs=0;
						zongks=zks;
						pjks=0;
						zpjks=pjks;
					}else{
						zks=Integer.parseInt(vecw.get(i+10).toString())*zs*xs;
						zzs=20;
						zongks=zks;
						pjks=(zks/zzs);
						zpjks=pjks;
					}
					//System.out.println("1:"+zongks+"|"+zpjks);
							
					for(int j=16;j<vecw.size();j=j+16){
						//教师相同
						if(vecw.get(i+2).toString().equals(vecw.get(j+2).toString())){							
							//计算总课时
							zs=0;
							ce=0;
							if(vecw.get(i+4).toString().equals("外聘任课教师")){
								zs=Integer.parseInt(vecw.get(j+15).toString())-Integer.parseInt(vecw.get(j+14).toString());
							}else{
								if(vecw.get(j+1).toString().substring(2,4).equals(vecw.get(j+8).toString().substring(0,2))&&vecw.get(j+1).toString().substring(4,5).equals("1")){//是第一学期
									if(this.getMSG().equals("18")){
										if(Integer.parseInt(vecw.get(j+15).toString())==9||Integer.parseInt(vecw.get(j+15).toString())==8){
											if(vecw.get(j+13).toString().equals("2-9")){
												zs=9;
											}else if(vecw.get(j+13).toString().equals("10-18")){
												zs=10;
											}else{
												zs=Integer.parseInt(vecw.get(j+15).toString());
											}
										}else if(Integer.parseInt(vecw.get(j+15).toString())==14||Integer.parseInt(vecw.get(j+15).toString())==13){
											zs=14;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==18||Integer.parseInt(vecw.get(j+15).toString())==19||Integer.parseInt(vecw.get(j+15).toString())==17){
											zs=19;
										}else{
											zs=Integer.parseInt(vecw.get(j+15).toString());
											ce=1;
										}
									}else if(this.getMSG().equals("17")){
										if(Integer.parseInt(vecw.get(j+15).toString())==9||Integer.parseInt(vecw.get(j+15).toString())==7||Integer.parseInt(vecw.get(j+15).toString())==8){
											if(vecw.get(j+13).toString().equals("2-9")){
												zs=9;
											}else if(vecw.get(j+13).toString().equals("10-17")){
												zs=10;
											}else{
												zs=Integer.parseInt(vecw.get(j+15).toString());
											}
										}else if(Integer.parseInt(vecw.get(j+15).toString())==14||Integer.parseInt(vecw.get(j+15).toString())==12||Integer.parseInt(vecw.get(j+15).toString())==13){
											zs=14;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==18||Integer.parseInt(vecw.get(j+15).toString())==16||Integer.parseInt(vecw.get(j+15).toString())==17){
											zs=19;
										}else{
											zs=Integer.parseInt(vecw.get(j+15).toString());
											ce=1;
										}
									}else if(this.getMSG().equals("19")){
										if(Integer.parseInt(vecw.get(j+15).toString())==9||Integer.parseInt(vecw.get(j+15).toString())==8){
											zs=9;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==14||Integer.parseInt(vecw.get(j+15).toString())==13){
											zs=14;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==18||Integer.parseInt(vecw.get(j+15).toString())==19){
											zs=19;
										}else{
											zs=Integer.parseInt(vecw.get(j+15).toString());
											ce=1;
										}
									}
									
								}else{
									if(this.getMSG().equals("18")){
										if(Integer.parseInt(vecw.get(j+15).toString())==9||Integer.parseInt(vecw.get(j+15).toString())==10){
											zs=10;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==14||Integer.parseInt(vecw.get(j+15).toString())==15){
											zs=15;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==18||Integer.parseInt(vecw.get(j+15).toString())==19||Integer.parseInt(vecw.get(j+15).toString())==20){
											zs=20;
										}else{
											zs=Integer.parseInt(vecw.get(j+15).toString());
											ce=1;
										}
									}else if(this.getMSG().equals("17")){
										if(Integer.parseInt(vecw.get(j+15).toString())==9||Integer.parseInt(vecw.get(j+15).toString())==8){
											zs=10;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==14||Integer.parseInt(vecw.get(j+15).toString())==13){
											zs=15;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==18||Integer.parseInt(vecw.get(j+15).toString())==19||Integer.parseInt(vecw.get(j+15).toString())==20||Integer.parseInt(vecw.get(j+15).toString())==17){
											zs=20;
										}else{
											zs=Integer.parseInt(vecw.get(j+15).toString());
											ce=1;
										}
									}else if(this.getMSG().equals("19")){
										if(Integer.parseInt(vecw.get(j+15).toString())==9||Integer.parseInt(vecw.get(j+15).toString())==10){
											zs=10;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==14||Integer.parseInt(vecw.get(j+15).toString())==15){
											zs=15;
										}else if(Integer.parseInt(vecw.get(j+15).toString())==18||Integer.parseInt(vecw.get(j+15).toString())==19||Integer.parseInt(vecw.get(j+15).toString())==20){
											zs=20;
										}else{
											zs=Integer.parseInt(vecw.get(j+15).toString());
											ce=1;
										}
									}
									
								}
							}
							
							xs=Double.parseDouble(vecw.get(j+12).toString());//系数
							zks=0;
							zzs=0;
							if(vecw.get(i+4).toString().equals("外聘任课教师")){
								zks=(Integer.parseInt(vecw.get(j+10).toString())*zs)*xs;
								zzs=Double.parseDouble(this.getMSG());
								zongks+=zks;
								pjks=(zks/zzs);
								zpjks+=pjks;
							}else if(vecw.get(i+4).toString().equals("")){
								zks=0;
								zzs=0;
								zongks+=zks;
								pjks=0;
								zpjks+=pjks;
							}else{
								zks=Integer.parseInt(vecw.get(j+10).toString())*zs*xs;
								zzs=20;
								zongks+=zks;
								pjks=(zks/zzs);
								zpjks+=pjks;
							}
							//System.out.println("2:"+zongks+"|"+zpjks);
							for(int k=0;k<16;k++){
								vecw.remove(j);
							}
							j=j-16;
						}
					}
					//System.out.println(vecs.toString()+"|"+i);
			
					//学年学期编码,教师代码、教师姓名、层级名称、教师职称、总课时、周平均课时、课时费总额
					vecu.add(vecw.get(i+1).toString());//学年学期编码 1
					vecu.add(vecw.get(i+2).toString());//授课教师编号 2
					vecu.add(vecw.get(i+3).toString());//授课教师姓名 3
					vecu.add(vecw.get(i+4).toString());//层级名称 4
					
					double ksxs=0;//课时系数
					double ksf=0;//课时费
					if(vecw.get(i+4).toString().equals("")){
						vecu.add("");//教师职称 5
						vecu.add("");//总课时 6
						vecu.add("");//周数 7
						vecu.add("");//周平均课时 8
						vecu.add("");//课时费 9
					}else{
						for(int t=0;t<veczc.size();t=t+3){//工号，职称，系数
							if(veczc.get(t).toString().trim().equals(vecw.get(i+2).toString().trim())){//教师编号相同
								vecu.add(veczc.get(t+1).toString());//教师职称 5
								vecu.add(zongks+"");//总课时 6
								vecu.add(zzs+"");//周数 7
								vecu.add(zpjks+"");//周平均课时 8
								if(vecw.get(i+4).toString().equals("外聘任课教师")){
									ksxs=Double.parseDouble(veczc.get(t+2).toString());//系数
									if(zpjks<=6.0){
										ksf=67*zpjks*ksxs*zzs;
									}else if(6.0<zpjks&&zpjks<=18.0){
										ksf=(67*6+71*(zpjks-6))*ksxs*zzs;
									}else if(18.0<zpjks){
										ksf=(67*6+71*12+69*(zpjks-18))*ksxs*zzs;
									}
									vecu.add(ksf+"");//课时费 9
								}else{
									ksxs=Double.parseDouble(veczc.get(t+2).toString());//系数
									if(zpjks<=6.0){
										ksf=67*zpjks*ksxs*20/5;
									}else if(6.0<zpjks&&zpjks<=18.0){
										ksf=(67*6+71*(zpjks-6))*ksxs*20/5;
									}else if(18.0<zpjks){
										ksf=(67*6+71*12+69*(zpjks-18))*ksxs*20/5;
									}
									vecu.add(ksf+"");//课时费 9
								}
								break;
							}
						}
					}
					
					for(int k=0;k<16;k++){
						vecw.remove(i);
					}
				}
					
		}//vechb
		
//		for(int i=0;i<vecu.size();i=i+9){
//			
//			System.out.println("u:学年学期编码:"+vecu.get(i).toString()+","+"授课教师编号:"+vecu.get(i+1).toString()+","+"授课教师姓名:"+vecu.get(i+2).toString()+","+"层级名称:"+vecu.get(i+3).toString()+","+
//			"教师职称:"+vecu.get(i+4).toString()+","+"总课时 :"+vecu.get(i+5).toString()+","+"周数:"+vecu.get(i+6).toString()+","+"周平均课时:"+vecu.get(i+7).toString()+","+"课时费:"+vecu.get(i+8).toString() );	
//			
//		}
		
		//System.out.println(pageNum+"|"+pageSize);
		String revec="";
		int total=0;
		if(pageNum*pageSize*9>vecu.size()){
			total=vecu.size();
		}else{
			total=pageNum*pageSize*9;
		}
		 
		for(int m=(pageNum-1)*pageSize*9;m<total;m=m+9){
			revec+="{\"学年学期编码\":\""+vecu.get(m).toString()+"\",\"授课教师编号\":\""+vecu.get(m+1).toString()+"\",\"授课教师姓名\":\""+vecu.get(m+2).toString()+"\",\"层级名称\":\""+vecu.get(m+3).toString()+"\",\"教师职称\":\""+vecu.get(m+4).toString()+"\",\"总课时\":\""+vecu.get(m+5).toString()+"\",\"周数\":\""+vecu.get(m+6).toString()+"\",\"周平均课时\":\""+vecu.get(m+7).toString()+"\",\"课时费\":\""+vecu.get(m+8).toString()+"\",\"MSG\":\""+this.getMSG()+"\"},";
		}
		if(!revec.equals("")){
			revec="["+revec.substring(0, revec.length()-1)+"]";
		}else{
			revec="[]";
		}
		this.setTATOL(vecu.size()/9+"");
		//System.out.println("revec:--"+revec);
		return revec;
	}
	
	/**
	 * @author 2015-9-11
	 * @author lupengfei
	 * @return
	 * @throws SQLException
	 */

	public Vector JSLBHCombobox() throws SQLException {
		DBSource dbSource = new DBSource(request);
		Vector vector = null;
		String sql = "";
		sql = "select '' as comboValue,'请选择' as comboName union select 层级编号 as comboValue, 层级名称 as comboName from V_层级表  ";			
			
		vector = dbSource.getConttexJONSArr(sql, 0, 0);
		return vector;
	}
	
	
	public String getBH() {
		return BH;
	}

	public void setBH(String bH) {
		BH = bH;
	}

	public String getLX() {
		return LX;
	}

	public void setLX(String lX) {
		LX = lX;
	}

	public String getXNXQBM() {
		return XNXQBM;
	}

	public void setXNXQBM(String xNXQBM) {
		XNXQBM = xNXQBM;
	}

	public String getXZBDM() {
		return XZBDM;
	}

	public void setXZBDM(String xZBDM) {
		XZBDM = xZBDM;
	}

	public String getSJXL() {
		return SJXL;
	}

	public void setSJXL(String sJXL) {
		SJXL = sJXL;
	}

	public String getSKJHMXBH() {
		return SKJHMXBH;
	}

	public void setSKJHMXBH(String sKJHMXBH) {
		SKJHMXBH = sKJHMXBH;
	}

	public String getLJXGBH() {
		return LJXGBH;
	}

	public void setLJXGBH(String lJXGBH) {
		LJXGBH = lJXGBH;
	}

	public String getZT() {
		return ZT;
	}

	public void setZT(String zT) {
		ZT = zT;
	}

	public String getXNXQ() {
		return XNXQ;
	}

	public void setXNXQ(String xNXQ) {
		XNXQ = xNXQ;
	}

	public String getJXXZ() {
		return JXXZ;
	}

	public void setJXXZ(String jXXZ) {
		JXXZ = jXXZ;
	}

	public String getKCJS() {
		return KCJS;
	}

	public void setKCJS(String kCJS) {
		KCJS = kCJS;
	}

	public String getQCXX() {
		return QCXX;
	}

	public void setQCXX(String qCXX) {
		QCXX = qCXX;
	}

	public String getiUSERCODE() {
		return iUSERCODE;
	}

	public void setiUSERCODE(String iUSERCODE) {
		this.iUSERCODE = iUSERCODE;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getTEAID() {
		return TEAID;
	}

	public void setTEAID(String tEAID) {
		TEAID = tEAID;
	}

	public String getTEANAME() {
		return TEANAME;
	}

	public void setTEANAME(String tEANAME) {
		TEANAME = tEANAME;
	}

	public String getTATOL() {
		return TATOL;
	}

	public void setTATOL(String tATOL) {
		TATOL = tATOL;
	}
	
	
	
}