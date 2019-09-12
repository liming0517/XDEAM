package com.pantech.devolop.customExamManage;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
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

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.code.Y;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.excelwriter.XlBorderWeight;
import com.zhuozhengsoft.pageoffice.excelwriter.XlHAlign;



public class YktjBean {

	private String BJDM;//班级代码 
	private String KCDM;//课程代码 
	private String XNXQBM;//学年学期编码
	private String QSXNXQ;// 起始学年学期
	private String JSXNXQ;// 结束学年学期
	private String STUNAME;// 姓名
	private String KSXKBH; //考试学科编号 
	private String KSBH; //考试编号 
	
	private String AUTHCODE;// 用户权限
	
	
	private String USERCODE;

	// 以下变量为此类的固定变量
	protected HttpServletRequest request;
	private DBSource db;
	private String MSG;
	
	/**
	 * 初始化变量
	 * 
	 * @date:2017-8-31
	 */
	private void initialData() {
		// TODO Auto-generated method stub
		BJDM = "";// 班级代码 
		KCDM = ""; //课程代码 
		XNXQBM = ""; //学年学期编码
		USERCODE = "";// 用户编号
		QSXNXQ = "";// 起始学年学期
		JSXNXQ = "";// 结束学年学期
		STUNAME = "";// 姓名
		KSXKBH = ""; //考试学科编号
		KSBH = ""; //考试编号
		
		AUTHCODE = ""; // 用户权限
	}
	
	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public YktjBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}

	
	/**
	 * 读取月考班级课程列表
	 * 
	 * @date:2017-08-31
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page)
			throws SQLException {
		Vector vec = null;
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		/*String sql = "select distinct d.行政班名称 as BJ,c.课程名称 as KC,d.行政班代码 as BJDM, c.课程号 as KCDM, e.授课教师姓名 as SKJSXM from V_自设考试管理_考试信息表 a " +
					" left join V_自设考试管理_考试学科信息表 b " +
					" on a.考试编号 = b.考试编号 " +
					" left join V_课程数据子类 c " +
					" on b.课程代码 = c.课程号 " +
					" left join V_学校班级数据子类 d " +
					" on b.班级代码 = d.行政班代码 " +
					" left join V_排课管理_课程表明细详情表 e " +
					" on d.行政班代码 = e.行政班代码 and c.课程号 = e.课程代码 " +
					"left join V_基础信息_教研组信息表 as f on b.课程代码=f.学科代码 "+
					"left join V_基础信息_系部教师信息表 as g on g.系部代码=d.系部代码  " +
					
					" where 类别编号 ='01' ";*/
		
		String sql="select distinct d.行政班名称 as BJ,c.课程名称 as KC,d.行政班代码 as BJDM, c.课程号 as KCDM, " +
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
				" where 类别编号 ='01'" ;
		//权限判断
			if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
				sql+=" and (e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' or h.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				
				if(this.getAUTHCODE().indexOf(jyzzz)>-1){
					sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(this.getUSERCODE()) + "@%') ";
				}
				
				//班主任
				if(this.getAUTHCODE().indexOf(bzr) > -1){
					sql += " or d.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "' ";
				}
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					sql += " or g.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
			
//			if(this.getAUTHCODE().indexOf(jyzzz)>0){
//				sql+="  f.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
//			}else{
//						
//				//班主任
//				if(this.getAUTHCODE().indexOf(bzr) > -1){
//					sql += " d.班主任工号='" + MyTools.fixSql(this.getUSERCODE()) + "'";
//				}
//				//系部教务人员
//				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
//					if(this.getAUTHCODE().indexOf(bzr) > -1){
//						sql += " or ";
//					}
//					sql += " g.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
//				}else{
//					if(this.getAUTHCODE().indexOf(bzr) > -1){
//						sql += " or ";
//					}
//					//sql+=" b.课程代码 in (select distinct a.课程代码 from V_规则管理_授课计划明细表 as a where a.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%') ";
//					sql += " e.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
//				}
//			}
			sql+=" ) ";
		}
		if (!"".equalsIgnoreCase(this.getBJDM())) {
			sql += " AND b.班级代码 ='" + MyTools.fixSql(this.getBJDM()) + "'";
		}
		if (!"".equalsIgnoreCase(this.getKCDM())) {
			sql += " AND b.课程代码 ='" + MyTools.fixSql(this.getKCDM()) + "'";
		}
		
		//=====================================================================================9/15
		
		/*if(this.getAUTHCODE().indexOf(admin)<0 && this.getAUTHCODE().indexOf(jxzgxz)<0 && this.getAUTHCODE().indexOf(qxjdzr)<0 && this.getAUTHCODE().indexOf(qxjwgl)<0){
			if(this.getAUTHCODE().indexOf(jyzzz)>0){
				sql+=" and f.教研组组长编号 in ('@" + MyTools.fixSql(this.getUSERCODE()).replaceAll(",", "@','@") + "@') ";
			}else{
				sql+=" and b.课程代码 in (select distinct a.课程代码 from V_规则管理_授课计划明细表 as a where a.授课教师编号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
				//系部教务人员
				if(this.getAUTHCODE().indexOf(xbjdzr)>-1 || this.getAUTHCODE().indexOf(xbjwgl)>-1){
					//if(this.getAUTHCODE().indexOf(admin) > -1){
						sql += " or ";
					//}
					sql += " e.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(this.getUSERCODE()) + "')";
				}
			}
			//sql += ")";
		}*/
//---------------------------------------------------------------------		
		
		
		sql += " order by XBDM ,PX desc ";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	
	/**
	 * 读取班级下拉框
	 * 
	 * @date:2017-08-31
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadBJCombo() throws SQLException {
		Vector vec = null;
//		String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 行政班代码  AS comboValue,行政班名称 AS comboName,'2' as px  FROM V_学校班级数据子类 where 1=1) e order by e.px,e.comboValue desc ";
		String sql = " select comboValue,comboName from ( " +
					" select '' as comboValue,'请选择' as comboName,'1' as px  " +
					" union  " +
					" select distinct d.行政班代码 as comboValue,d.行政班名称 as comboName, '2' as px  from V_自设考试管理_考试信息表 a  " +
					" left join V_自设考试管理_考试学科信息表 b " +
					" on a.考试编号 = b.考试编号 " +
					" left join V_学校班级数据子类 d " +
					" on b.班级代码 = d.行政班代码  " +
					" where 类别编号 ='01'  " +
					" ) e order by e.px,e.comboName desc  ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取课程下拉框
	 * 
	 * @date:2017-08-31
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKCCombo() throws SQLException {
		Vector vec = null;
//		String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 课程号  AS comboValue,课程名称 AS comboName,'2' as px  FROM V_课程数据子类 where 1=1) e order by e.px,e.comboValue desc ";
		String sql = " select comboValue,comboName from ( " +
					" select '' as comboValue,'请选择' as comboName,'1' as px  " +
					" union  " +
					" select distinct c.课程号 as comboValue,c.课程名称 as comboName, '2' as px  from V_自设考试管理_考试信息表 a  " +
					" left join V_自设考试管理_考试学科信息表 b " +
					" on a.考试编号 = b.考试编号 " +
					" left join V_课程数据子类 c " +
					" on b.课程代码 = c.课程号  " +
					" where 类别编号 ='01'  " +
					" ) e order by e.px,e.comboName desc  ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017-09-01
	 * @author:
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXnxqCombo() throws SQLException {
		Vector vec = null;
		String sql = " select distinct 学年学期名称 as comboName,学年学期编码 as comboValue from V_规则管理_学年学期表 where 状态='1' order by comboValue ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取当前学年学期
	 * @date:2017-09-01
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException {
		String curXnxq = "";
		Vector vec = null;
		String sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		vec = db.GetContextVector(sql);

		if (vec != null && vec.size() > 0) {
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}

		return curXnxq;
	}
	
	/**
	 * 读取月考统计信息（图表）
	 * @date:2017-09-01
	 * @author:
	 * @throws SQLException
	 * @param bj 班级
	 * @param kc 课程
	 */
	public Vector loadGradeyktjChart(String bj, String kc) throws SQLException {
		Vector resultVec = new Vector();
		Vector vec = null;
		Vector KSBHMCvec = null; //考试名称
		Vector KSCJCvec = null; //考试成绩
		Vector dataVec = new Vector(); //考试成绩
		Vector zgfVec = new Vector(); //最高分集合
		Vector zdfVec = new Vector(); //最低分集合
		Vector pjfVec = new Vector(); //平均分集合
		Vector jglVec = new Vector(); //及格率集合
		String xnxq = "";
		String ksbh = "";
		
		String sql = " select 学年学期编码 from V_规则管理_学年学期表 where 学年学期编码 between '" + MyTools.fixSql(this.getQSXNXQ()) + "' and '" + MyTools.fixSql(this.getJSXNXQ()) + "' ";
		vec = db.GetContextVector(sql);
		
		if(vec.size() > 0){
			for (int i = 0; i < vec.size(); i++) {
				xnxq += "'" + vec.get(i) + "',";
			}
			xnxq = xnxq.substring(0, xnxq.length()-1);
		}
		
		if(!"".equalsIgnoreCase(xnxq)){
			//月考名称
			sql = " select a.考试编号,a.考试名称 from V_自设考试管理_考试信息表  a " +
					"left join V_自设考试管理_考试学科信息表  b on a.考试编号=b.考试编号 " +
					" where 类别编号 ='01' and 学年学期编码 in(" + xnxq + ") and b.班级代码 = '" + MyTools.fixSql(this.getBJDM()) + "' " +
					"and b.课程代码 = '" + MyTools.fixSql(this.getKCDM()) + "' order by a.考试编号";
			KSBHMCvec = db.GetContextVector(sql);
			
			
			if(KSBHMCvec.size() > 0){
				for (int i = 0; i < KSBHMCvec.size(); i+=2) {
					ksbh += "'" + KSBHMCvec.get(i) + "',";
				}
				ksbh = ksbh.substring(0, ksbh.length()-1);
			
				//考试编号，月考最高分，最低分，平均分,及格率
				sql = " select b.考试编号,a.最高分,a.最低分,a.平均分,a.及格率 from( " +
					" select 考试学科编号, cast(max(成绩) as numeric(18,2)) as 最高分, cast(min(成绩) as numeric(18,2)) as 最低分, cast(avg(成绩) as numeric(18,2)) as 平均分, " +
					//" cast(CAST(SUM(case when 成绩>=60 then 1 end) as float)/COUNT(成绩)as numeric(18,2))as 及格率 " +
					" convert(varchar,cast(CAST(SUM(case when 成绩>=60 then 1 end) as float)/COUNT(成绩)as numeric(18,2))* 100) as 及格率   "+
					" from V_自设考试管理_学生成绩信息表 where 考试学科编号  in ( " +
					" select distinct 考试学科编号 from V_自设考试管理_考试学科信息表 where 班级代码 = '" + MyTools.fixSql(this.getBJDM()) + "' and 课程代码 = '" + MyTools.fixSql(this.getKCDM()) + "' and 考试编号 in (" + ksbh + ")" +
					" ) group by 考试学科编号 ) a " +
					" left join V_自设考试管理_考试学科信息表 b " +
					" on a.考试学科编号 = b.考试学科编号 " +
					" order by 考试编号 ";
				
				KSCJCvec = db.GetContextVector(sql);
				
				int ksIndex = 0;
				for(int i=0;i<KSBHMCvec.size();i+=2){
					ksIndex = KSCJCvec.indexOf(KSBHMCvec.get(i));
					
					if(ksIndex > -1){
						zgfVec.add(KSCJCvec.get(ksIndex+1)); //最高分集合
						zdfVec.add(KSCJCvec.get(ksIndex+2)); //最低分集合
						pjfVec.add(KSCJCvec.get(ksIndex+3)); //平均分集合
						jglVec.add(KSCJCvec.get(ksIndex+4)); //及格率集合
					}else{
						zgfVec.add(""); //最高分集合
						zdfVec.add(""); //最低分集合
						pjfVec.add(""); //平均分集合
						jglVec.add(""); //及格率集合
					}
				}
				
				
//				if(KSCJCvec.size() > 0){
//					for (int i = 0; i < KSCJCvec.size(); i+=5) {
//						zgfVec.add(KSCJCvec.get(i+1)); //最高分集合
//						zdfVec.add(KSCJCvec.get(i+2)); //最低分集合
//						pjfVec.add(KSCJCvec.get(i+3)); //平均分集合
//						jglVec.add(KSCJCvec.get(i+4)); //及格率集合
//					}
//				}
				
				dataVec.add("最高分");
				dataVec.add(zgfVec);
				dataVec.add("最低分");
				dataVec.add(zdfVec);
				dataVec.add("平均分");
				dataVec.add(pjfVec);
				dataVec.add("及格率");
				dataVec.add(jglVec);
				
				resultVec = this.parseEchartsData(bj+kc+"月考统计信息", KSBHMCvec, dataVec);
			}else{
				resultVec.add("");
				resultVec.add("");
			}
		}else{
			resultVec.add("");
			resultVec.add("");
		}
		
		return resultVec;
	}
	
	/**
	 * 获取图标数据
	 * @date:2017-09-01
	 * @author:
	 * @param title 标题
	 * @param ksbhVec 考试
	 * @param dataVec 数据
	 * @throws SQLException
	 */
	public Vector parseEchartsData(String title, Vector ksbhVec, Vector dataVec) {
		String legendArray[] = new String[dataVec.size()/2];
		String examCode = "";
		String examArray[] = new String[ksbhVec.size()/2];
		int index = 0;
		GsonOption option = new GsonOption();//创建Option
		Line line = null;
		Vector tempVec = null;
		String dataArray[] = new String[0];
		Vector resultVec = new Vector();

		for(int i=0; i<dataVec.size(); i+=2){
			legendArray[index] = MyTools.StrFiltr(dataVec.get(i));
			index++;
		}
		index = 0;
		for(int i=0; i<ksbhVec.size(); i+=2){
			examCode += MyTools.StrFiltr(ksbhVec.get(i)) + ",";
			examArray[index] = MyTools.StrFiltr(ksbhVec.get(i+1));
			index++;
		}
		if(examCode.length() > 0)
			examCode = examCode.substring(0, examCode.length()-1);
		resultVec.add(examCode);

		option.title().text(title).subtext("(点击分数可查看成绩信息详情)").x("center").textStyle();
		option.tooltip().trigger(Trigger.axis);
		option.tooltip().axisPointer().type(PointerType.shadow);
		// 工具栏设置
		option.toolbox().show(true).feature(new MagicType(Magic.line, Magic.bar), Tool.restore, Tool.saveAsImage);
		option.setAnimation(true);//设置动画(不支持HTML5建议关闭)
		option.legend().x(X.center).y(Y.bottom).borderWidth(1);
		option.legend(legendArray);
		// 设置只选中第一个班级
		// for(int i=0; i<legendArray.length; i++){
		// if(i > 0){
		// option.legend().selected(legendArray[i], false);
		// }
		// }
		// option.legend().setOrient(Orient.vertical);//设置图例垂直/水平显示
		option.calculable(false);//禁止拖拽
		
		CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.boundaryGap(false);
        categoryAxis.data(examArray);
        categoryAxis.axisLabel().interval("0");
        option.xAxis(categoryAxis);
		//option.xAxis(new CategoryAxis().boundaryGap(false).data(examArray));
		option.yAxis(new ValueAxis());
		
		//option.tooltip().formatter(examArray[0]+"<br/>"+dataVec.get(0)+":" + dataVec.get(1).toString().substring(1,dataVec.get(1).toString().indexOf("]")) + "<br/>"+""+dataVec.get(2)+":" + dataVec.get(3) + "<br/>"+""+dataVec.get(4)+":" + dataVec.get(5) + "<br/>"+""+dataVec.get(6)+":" + dataVec.get(7) + "%");

		for(int i=0; i<dataVec.size(); i+=2){
			line = new Line();
			line.smooth(false);//平滑曲线显示，smooth为true时lineStyle不支持虚线
			line.name(MyTools.StrFiltr(dataVec.get(i)));
			
			index = 0;
			tempVec = (Vector) dataVec.get(i + 1);
			dataArray = new String[tempVec.size()];
			for (int j = 0; j < tempVec.size(); j++) {
				//System.out.println("tempVec.size():"+tempVec.size());
				dataArray[index] = MyTools.StrFiltr(tempVec.get(j));
				//System.out.println("dataArray:"+dataArray[index]);
				index++;

			}
			System.out.println(dataArray[0]+"----");//值
			line.data(dataArray);
			option.series(line);
			line.itemStyle().normal().label().setShow(true);// 显示折线上的数值
		}
		resultVec.add(option.toString());
		return resultVec;
	}
	
	/**
	 * 读取月考统计datagrid数据
	 * @date:2017-09-01
	 * @author:
	 * @throws SQLException
	 */
	public Vector loadGradeyktjList() throws SQLException {
		Vector resultVec = new Vector();
		Vector vec = null;
		Vector KSBHMCvec = null; //考试名称
		Vector KSCJCvec = null; //考试成绩
		String xnxq = "";
		String ksbh = "";
		String scoreInfo = "[";
		
		String sql = " select 学年学期编码 from V_规则管理_学年学期表 where 学年学期编码 between '" + MyTools.fixSql(this.getQSXNXQ()) + "' and '" + MyTools.fixSql(this.getJSXNXQ()) + "' ";
		vec = db.GetContextVector(sql);
		
		if(vec.size() > 0){
			for (int i = 0; i < vec.size(); i++) {
				xnxq += "'" + vec.get(i) + "',";
			}
			xnxq = xnxq.substring(0, xnxq.length()-1);
		
		
			//月考名称
			//sql = " select 考试编号,考试名称 from V_自设考试管理_考试信息表 where 类别编号 ='01' and 学年学期编码 in(" + xnxq + ") order by 考试编号 ";
			sql = " select a.考试编号,a.考试名称 from V_自设考试管理_考试信息表  a " +
					"left join V_自设考试管理_考试学科信息表  b on a.考试编号=b.考试编号 " +
					" where 类别编号 ='01' and 学年学期编码 in(" + xnxq + ") and b.班级代码 = '" + MyTools.fixSql(this.getBJDM()) + "' " +
					"and b.课程代码 = '" + MyTools.fixSql(this.getKCDM()) + "' order by a.考试编号";
			
			KSBHMCvec = db.GetContextVector(sql);
			
			if(KSBHMCvec.size() > 0){
				for (int i = 0; i < KSBHMCvec.size(); i+=2) {
					ksbh += "'" + KSBHMCvec.get(i) + "',";
				}
				ksbh = ksbh.substring(0, ksbh.length()-1);
			
			
				//考试编号，月考最高分，最低分，平均分,及格率
	//			sql = " select b.考试编号 as ksbh,c.考试名称 as ksmc,a.最高分 as zgf,a.最低分 as zdf,a.平均分 as pjf,a.及格率 as jgl from( " +
				sql = " select b.考试编号,c.考试名称,b.考试学科编号 ,a.最高分 ,a.最低分 ,a.平均分 ,a.及格率  from( " +
					" select 考试学科编号, cast(max(成绩) as numeric(18,2)) as 最高分, cast(min(成绩) as numeric(18,2)) as 最低分, cast(avg(成绩) as numeric(18,2)) as 平均分, " +
					" cast(CAST(SUM(case when 成绩>=60 then 1 end) as float)/COUNT(成绩)as numeric(18,2))as 及格率 " +
					" from V_自设考试管理_学生成绩信息表 where 考试学科编号  in ( " +
					" select distinct 考试学科编号 from V_自设考试管理_考试学科信息表 where 班级代码 = '" + MyTools.fixSql(this.getBJDM()) + "' and 课程代码 = '" + MyTools.fixSql(this.getKCDM()) + "' and 考试编号 in (" + ksbh + ")" +
					" ) group by 考试学科编号 ) a " +
					" left join V_自设考试管理_考试学科信息表 b " +
					" on a.考试学科编号 = b.考试学科编号  " +
					" left join V_自设考试管理_考试信息表 c " +
					" on b.考试编号 = c.考试编号 " + 
					" order by 考试编号 ";
				
				KSCJCvec = db.GetContextVector(sql);
				
				if(KSCJCvec.size() > 0){
					for (int i = 0; i < KSCJCvec.size(); i+=7) {
						String KSBH =  MyTools.StrFiltr(KSCJCvec.get(i));
						String KSMC = MyTools.StrFiltr(KSCJCvec.get(i+1));
						String KSXKBH = MyTools.StrFiltr(KSCJCvec.get(i+2));
						String ZGF = MyTools.StrFiltr(KSCJCvec.get(i+3));
						String ZDF = MyTools.StrFiltr(KSCJCvec.get(i+4));
						String PJF = MyTools.StrFiltr(KSCJCvec.get(i+5));
						String JGL = MyTools.StrFiltr(KSCJCvec.get(i+6));
						scoreInfo +=  "{\"KSBH\":\"" + KSBH + "\",";
						scoreInfo += "\"KSMC\":\"" + KSMC + "\",";
						scoreInfo += "\"KSXKBH\":\"" + KSXKBH + "\",";
						scoreInfo += "\"ZGF\":\"" + ZGF + "\",";
						scoreInfo += "\"ZDF\":\"" + ZDF + "\",";
						scoreInfo += "\"PJF\":\"" + PJF + "\",";
						scoreInfo += "\"JGL\":\"" + JGL + "\"},";
					}
					scoreInfo = scoreInfo.substring(0, scoreInfo.length()-1);
					scoreInfo += "]";
					resultVec.add(scoreInfo);
				}else{
					resultVec.add("{\"total\":0,\"rows\":[]}}");
				}
			}else{
				resultVec.add("{\"total\":0,\"rows\":[]}");
			}
		}else{
			resultVec.add("{\"total\":0,\"rows\":[]}");
		}
		
		return resultVec;
		
	}
	
	
	/**
	 * 查询班级学生月考考试成绩列表信息
	 * @date:2017-09-03
	 * @author:yeq
	 * @param pageNum页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queClassStuScoreList(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "";
		
//		sql = " select 姓名 as xm,成绩 as cj,RANK() over (PARTITION BY 考试学科编号 order by 成绩 desc) as bjpm from V_自设考试管理_学生成绩信息表 where 考试学科编号 = '" + MyTools.fixSql(this.getKSXKBH()) + "' ";
		sql = " with aa as( " +
			" select 编号,姓名 as xm,成绩 as cj,RANK() over (PARTITION BY 考试学科编号 order by 成绩 desc) as bjpm from V_自设考试管理_学生成绩信息表 " +
			" where 考试学科编号 = '" + MyTools.fixSql(this.getKSXKBH()) + "' " +
			" ) " +
			" select a.xm,a.cj,b.bjpm from( " +
			" select 编号,姓名 as xm,case when convert(varchar(5),成绩) IS NULL then '缺考' else convert(varchar(5),成绩) end as cj,RANK() over (PARTITION BY 考试学科编号 order by 成绩 desc) as bjpm from V_自设考试管理_学生成绩信息表 " +
			" where 考试学科编号 = '" + MyTools.fixSql(this.getKSXKBH()) + "'";
		
		
		if (!"".equalsIgnoreCase(MyTools.StrFiltr((this.getSTUNAME())))) {
			sql += " and 姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
		}
		
		sql += 	" ) a " +
				" left join aa b " +
				" on a.编号 = b.编号 order by a.bjpm" ;
		
//		sql += " order by 班级排名";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);//带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 查询班级学生月考考试成绩列表信息
	 * @date:2017-09-03
	 * @author:
	 * @param pageNum页数
	 * @param pageSize 每页数据条数
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	
	public Vector queClassStuScoreList2(int pageNum, int pageSize) throws SQLException {
		Vector vec = null; // 结果集
		String sql = "";
		
		sql = " select 考试学科编号 from V_自设考试管理_考试学科信息表 where 考试编号 ='" + MyTools.fixSql(this.getKSBH()) + "' and 班级代码='" + MyTools.fixSql(this.getBJDM()) + "' and 课程代码 = '" + MyTools.fixSql(this.getKCDM()) + "' ";
		vec = db.GetContextVector(sql);
		String ksxkbh = "";
		
		
		if(vec.size() > 0 ){
//			sql = " select 姓名 as xm,成绩 as cj,RANK() over (PARTITION BY 考试学科编号 order by 成绩 desc) as bjpm from V_自设考试管理_学生成绩信息表 where 考试学科编号 = '" + MyTools.fixSql(this.getKSXKBH()) + "' ";
			ksxkbh = MyTools.StrFiltr(vec.get(0));
		}
		
		sql = " with aa as( " +
				" select 编号,姓名 as xm,成绩 as cj,RANK() over (PARTITION BY 考试学科编号 order by 成绩 desc) as bjpm from V_自设考试管理_学生成绩信息表 " +
				" where 考试学科编号 = '" + MyTools.fixSql(ksxkbh) + "' " +
				" ) " +
				" select a.xm,a.cj,b.bjpm from( " +
				" select 编号,姓名 as xm,case when convert(varchar(5),成绩) IS NULL then '缺考' else convert(varchar(5),成绩) end as cj,RANK() over (PARTITION BY 考试学科编号 order by 成绩 desc) as bjpm from V_自设考试管理_学生成绩信息表 " +
				" where 考试学科编号 = '" + MyTools.fixSql(ksxkbh) + "'";
			
			
			if (!"".equalsIgnoreCase(MyTools.StrFiltr((this.getSTUNAME())))) {
				sql += " and 姓名 like '%" + MyTools.fixSql(this.getSTUNAME()) + "%'";
			}
			
			sql += 	" ) a " +
					" left join aa b " +
					" on a.编号 = b.编号 order by a.bjpm" ;
			
//			sql += " order by 班级排名";
			vec = db.getConttexJONSArr(sql, pageNum, pageSize);//带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 导出月考信息
	 * @date:2017-09-03
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	
	public String exportYK(String bj, String kc) throws SQLException{
		String savePath = "";
		savePath = MyTools.getProp(request, "Base.exportExcelPath");
		String titleName = "";
		String sql = "";
		Vector vec = null;
		String xnxq = ""; //学年学期
		String ksbh = ""; //考试编号
		Vector KSBHMCvec = null; //考试名称
		int index = 0;
		
		sql = " select 学年学期编码 from V_规则管理_学年学期表 where 学年学期编码 between '" + MyTools.fixSql(this.getQSXNXQ()) + "' and '" + MyTools.fixSql(this.getJSXNXQ()) + "' ";
		vec = db.GetContextVector(sql);
		
		if(vec.size() > 0){
			for (int i = 0; i < vec.size(); i++) {
				xnxq += "'" + vec.get(i) + "',";
			}
			xnxq = xnxq.substring(0, xnxq.length()-1);
		}
		
		if(!"".equalsIgnoreCase(xnxq)){
			//月考名称
			//sql = " select 考试编号,考试名称 from V_自设考试管理_考试信息表 where 类别编号 ='01' and 学年学期编码 in(" + xnxq + ") order by 考试编号 ";
			
			sql = " select a.考试编号,a.考试名称 from V_自设考试管理_考试信息表  a " +
					"left join V_自设考试管理_考试学科信息表  b on a.考试编号=b.考试编号 " +
					" where 类别编号 ='01' and 学年学期编码 in(" + xnxq + ") and b.班级代码 = '" + MyTools.fixSql(this.getBJDM()) + "' " +
					"and b.课程代码 = '" + MyTools.fixSql(this.getKCDM()) + "' order by a.考试编号";
			KSBHMCvec = db.GetContextVector(sql);
			
			
			if(KSBHMCvec.size() > 0){
				for (int i = 0; i < KSBHMCvec.size(); i+=2) {
					ksbh += "'" + KSBHMCvec.get(i) + "',";
				}
				ksbh = ksbh.substring(0, ksbh.length()-1);
				
				//考试编号，月考最高分，最低分，平均分,及格率
				sql = " select c.考试名称 ,a.最高分 ,a.最低分 ,a.平均分 ,a.及格率  from( " +
					" select 考试学科编号, cast(max(成绩) as numeric(18,2)) as 最高分, cast(min(成绩) as numeric(18,2)) as 最低分, cast(avg(成绩) as numeric(18,2)) as 平均分, " +
					" cast(CAST(SUM(case when 成绩>=60 then 1 end) as float)/COUNT(成绩)as numeric(18,2))as 及格率 " +
					" from V_自设考试管理_学生成绩信息表 where 考试学科编号  in ( " +
					" select distinct 考试学科编号 from V_自设考试管理_考试学科信息表 where 班级代码 = '" + MyTools.fixSql(this.getBJDM()) + "' and 课程代码 = '" + MyTools.fixSql(this.getKCDM()) + "' and 考试编号 in (" + ksbh + ")" +
					" ) group by 考试学科编号 ) a " +
					" left join V_自设考试管理_考试学科信息表 b " +
					" on a.考试学科编号 = b.考试学科编号  " +
					" left join V_自设考试管理_考试信息表 c " +
					" on b.考试编号 = c.考试编号 ";
				
				vec = db.GetContextVector(sql);
			}
		}
		
		//获取起始学年学期编码
		String qsxnxq = this.getQSXNXQ();
		//截取年数
		String year_qs = qsxnxq.substring(0, 4);
		//截取第几学期
		String xq_qs = qsxnxq.substring(4,5);
		if("1".equalsIgnoreCase(xq_qs)){
			xq_qs = "第一学期";
		}
		if("2".equalsIgnoreCase(xq_qs)){
			xq_qs = "第二学期";
		}
		
		//获取起始学年学期编码
		String jsxnxq = this.getJSXNXQ();
		//截取年数
		String year_js = jsxnxq.substring(0, 4);
		//截取第几学期
		String xq_js = jsxnxq.substring(4,5);
		if("1".equalsIgnoreCase(xq_js)){
			xq_js = "第一学期";
		}
		if("2".equalsIgnoreCase(xq_js)){
			xq_js = "第二学期";
		}
		
		try {
			//声明工作簿jxl.write.WritableWorkbook  
			WritableWorkbook wwb;  
			//创建文件目录
			File file = new File(savePath);
			//判断文件目录是否存在
			if (!file.exists()) {
				file.mkdir();
			}
			
			savePath += "/"+ year_qs + "年" + xq_qs + "至" + year_js + "年" + xq_js + bj + kc +"月考信息.xls";
			titleName = year_qs + "年" + xq_qs + "至" + year_js + "年" + xq_js + bj + kc +"月考信息";
			OutputStream os = new FileOutputStream(savePath);
		    //根据传进来的file对象创建可写入的Excel工作薄  
		    wwb = Workbook.createWorkbook(os);  

		    /* 
		     * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表 
		     * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样 
		     * 代码中的"0"就是sheet1、其它的一一对应。 
		     * createSheet(sheetName, 0)一个是工作表的名称，另一个是工作表在工作薄中的位置 
		     */  
		    
		    WritableSheet ws = wwb.createSheet("Sheet1", 0);// 工作表名称
		    WritableFont fontStyle;
			WritableCellFormat contentStyle;
			WritableFont fontTitleStyle;
			WritableCellFormat contentTitleStyle;
			WritableFont fontStyle2;
			WritableCellFormat contentTitle_2Style;
			
			 //创建单元格样式  
		    fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
			
			fontTitleStyle = new WritableFont(WritableFont.createFont("宋体"), 24, WritableFont.BOLD);
			contentTitleStyle = new WritableCellFormat(fontTitleStyle);
			contentTitleStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
			contentTitleStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentTitleStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//边框颜色
			contentTitleStyle.setBackground(Colour.YELLOW);
			
			fontStyle2 = new WritableFont(WritableFont.createFont("宋体"), 16, WritableFont.BOLD);
			contentTitle_2Style = new WritableCellFormat(fontStyle2);
			contentTitle_2Style.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
			contentTitle_2Style.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentTitle_2Style.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//边框颜色

			
			//合并单元格： 第一位与第三位代表列，第二位与第四位代表行    
            ws.mergeCells(0, 0, 4, 0);
            
            
            /* 
             * 添加单元格(Cell)内容addCell() 
             * 添加Label对象Label() 
             * 数据的类型有很多种、在这里你需要什么类型就导入什么类型 
             * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label 
             * Label(i, 0, columns[i], wcf) 
             * 其中i为列、0为行、columns[i]为数据、wcf为样式 
             * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中 
             */  
           
            ws.addCell(new Label(0, 0, titleName, contentTitleStyle));
            ws.addCell(new Label(0, 1, "考试名称", contentTitle_2Style));
            ws.addCell(new Label(1, 1, "最高分", contentTitle_2Style));
            ws.addCell(new Label(2, 1, "最低分", contentTitle_2Style));
            ws.addCell(new Label(3, 1, "平均分", contentTitle_2Style));
            ws.addCell(new Label(4, 1, "及格率", contentTitle_2Style));
            
            //设置行高
            ws.setRowView(0, 1000, false); //设置行高 25 * 40(需要的行高) = 1000;
            
            //写入数据
           for (int i = 0; i < vec.size(); i+=5) {
        	   ws.addCell(new Label(0, 2+index, (String) vec.get(i), contentStyle));
        	   ws.addCell(new Label(1, 2+index, (String) vec.get(i+1), contentStyle));
        	   ws.addCell(new Label(2, 2+index, (String) vec.get(i+2), contentStyle));
        	   ws.addCell(new Label(3, 2+index, (String) vec.get(i+3), contentStyle));
        	   Double jgl = Double.parseDouble((String) vec.get(i+4));
        	   String jgl2 = changeTwoDecimal_f2(jgl)+"%";
        	   ws.addCell(new Label(4, 2+index, jgl2, contentStyle));
        	   index++;
           }
           
           //设置列宽
           ws.setColumnView(0, 30); // 设置列的宽度    
           ws.setColumnView(1, 30); // 设置列的宽度    
           ws.setColumnView(2, 30); // 设置列的宽度    
           ws.setColumnView(3, 30); // 设置列的宽度    
           ws.setColumnView(4, 30); // 设置列的宽度    
           
			
		    //写入Exel工作表  
	        wwb.write();  
	        //关闭Excel工作薄对象   
	        wwb.close();
	        //关闭输出
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
		
		return savePath;
	}
	
	
	/**
	 * 月考汇总统计导出(pageoffice)
	 * @date:2017-09-03
	 * @author:
	 * @param xnxqbm 学年学期编码
	 * @param 
	 * @param 
	 * @throws SQLException
	 */
	public String exportHztjb(HttpServletRequest request, String xnxqbm, String sAuth, String usercode) throws SQLException, UnsupportedEncodingException{
		String savePath = "";
//		savePath = MyTools.getProp(request, "Base.exportExcelPath");
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		String titleName = "";
		String sql = "";
		String ksbh = "";
		String filePath = "";
		int index = 0;
		int xbindex = 0;
		int allIndex = 0;
		Vector kcVec = null; //课程 [课程名称]
		Vector kcVec2 = null; //课程 [课程代码,课程名称]
		Vector ykVec = null; //月考 [考试编号,考试名称,招生类别,等级考学生类别]
		Vector njVec = null; //年级 [年级]
		Vector xbVec = null; //系部 [系部]
		Vector xbbjjsVec = null; //系部班级教师[系部名称,行政班名称,行政班代码,授课教师姓名,年级]
		Vector ykxxVec = null;
		Vector djVec = null;
		
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = " select 考试编号,考试名称,招生类别,等级考学生类别 from V_自设考试管理_考试信息表 where 学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' and 类别编号 = '01' order by 创建时间 ";
		ykVec = db.GetContextVector(sql);

		if(ykVec.size() > 0){
			for (int i = 0; i < ykVec.size(); i+=4) {
				ksbh += "'" + ykVec.get(i) + "',";
			}
			ksbh = ksbh.substring(0, ksbh.length()-1);
			
			
			sql = " select distinct a.课程代码,b.课程名称 from ( " +
				" select * from V_自设考试管理_考试学科信息表 where 考试编号 in (" + ksbh + ") " +
				" ) a " +
				" left join V_课程数据子类 b " +
				" on a.课程代码 = b.课程号 ";
			
			kcVec2 = db.GetContextVector(sql);
			
			
			/*sql = " select distinct b.课程名称 from ( " +
				" select * from V_自设考试管理_考试学科信息表 where 考试编号 in (" + ksbh + ") " +
				" ) a " +
				" left join V_课程数据子类 b " +
				" on a.课程代码 = b.课程号 "+
			
			"left join V_学校班级数据子类 c  on a.班级代码 = c.行政班代码 " +
			"left join V_排课管理_课程表明细详情表 d  on a.班级代码 = d.行政班代码 and a.课程代码  = d.课程代码 " +
			"left join V_规则管理_授课计划主表 as f on a.班级代码=f.行政班代码 " +
			"left join V_规则管理_授课计划明细表 as g on f.授课计划主表编号=g.授课计划主表编号 and a.课程代码=g.课程代码  " +
			"left join V_基础信息_教研组信息表 as h on a.课程代码=h.学科代码 " ;
			*/
			
			
			sql="select distinct g.课程名称 " +
					"from (select * from V_自设考试管理_考试学科信息表 where 考试编号 in (" + ksbh + ")) a  " +
					"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
					"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
					"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"') d on d.行政班代码=a.班级代码 and d.课程代码 like '%'+a.课程代码+'%'" +
					"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
					"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 " +
					"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"') e on e.行政班代码=a.班级代码 and e.课程代码=a.课程代码 " +
					"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  " +
					"left join V_课程数据子类 g on g.课程号=a.课程代码" ;
					
			//2017919翟旭超添加权限判断代码
			if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0 && sAuth.indexOf(xbjdzr)<0&&sAuth.indexOf(xbjwgl)<0){
				sql+=" where 1=1 and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
			
				if(sAuth.indexOf(jyzzz)>-1){
					sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
				}
				//系部
				if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
					sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
				}
				//班主任
				if(sAuth.indexOf(bzr) > -1){
					sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
				}
				sql+=" ) ";
			}
			
			kcVec = db.GetContextVector(sql);
			
			try {
				
				Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
				int year = c.get(Calendar.YEAR); 
				int month = c.get(Calendar.MONTH); 
				int date = c.get(Calendar.DATE);
				int hour = c.get(Calendar.HOUR);
				int minute = c.get(Calendar.MINUTE);
				int second = c.get(Calendar.SECOND);
				
				//截取年数
				String year_1 = xnxqbm.substring(0, 4);
				//截取第几学期
				String xq = xnxqbm.substring(4,5);
				if("1".equalsIgnoreCase(xq)){
					xq = "第一学期";
				}
				if("2".equalsIgnoreCase(xq)){
					xq = "第二学期";
				}
				
//				savePath += "/"+ year + "学年" + xq +"月考成绩统计表.xls";
				filePath = request.getSession().getServletContext().getRealPath("/")+"form/customExamManage/printView";
				filePath = filePath.replaceAll("\\\\", "/");
				
				savePath = "reportView"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+second+".xls";
				savePath = "/" + savePath;
				
				//声明工作簿jxl.write.WritableWorkbook  
				WritableWorkbook wwb;  
				//创建文件目录
				File file = new File(filePath);
				//判断文件目录是否存在
				if (!file.exists()) {
					file.mkdir();
				}
				
				filePath += savePath;
				
				OutputStream os = new FileOutputStream(filePath);
			    //根据传进来的file对象创建可写入的Excel工作薄  
			    wwb = Workbook.createWorkbook(os);  
			    
			    WritableFont fontStyle;
				WritableCellFormat contentStyle;
				WritableFont fontTitleStyle;
				WritableCellFormat contentTitleStyle;
				WritableFont fontStyle2;
				WritableCellFormat contentTitle_2Style;
				WritableCellFormat contentTitle_3Style; //系部标题
				WritableCellFormat contentStyle2;
				
				
				 //创建单元格样式  
			    fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				
				contentStyle2 = new WritableCellFormat(fontStyle);
				contentStyle2.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle2.setBorder(Border.RIGHT,  BorderLineStyle.MEDIUM,Colour.BLACK);
				
				fontTitleStyle = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD);
				contentTitleStyle = new WritableCellFormat(fontTitleStyle);
//				contentTitleStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentTitleStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentTitleStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//边框颜色
//				contentTitleStyle.setBackground(Colour.YELLOW);
				
				fontStyle2 = new WritableFont(WritableFont.createFont("宋体"), 14, WritableFont.BOLD);
				contentTitle_2Style = new WritableCellFormat(fontStyle2);
				contentTitle_2Style.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentTitle_2Style.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentTitle_2Style.setBorder(Border.ALL,  BorderLineStyle.MEDIUM,Colour.BLACK);//边框颜色
				
				
				contentTitle_3Style = new WritableCellFormat(fontStyle2);
				contentTitle_3Style.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentTitle_3Style.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentTitle_3Style.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//边框颜色
				
				
				

			    /* 
			     * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表 
			     * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样 
			     * 代码中的"0"就是sheet1、其它的一一对应。 
			     * createSheet(sheetName, 0)一个是工作表的名称，另一个是工作表在工作薄中的位置 
			     */  
			
				for (int i = 0; i < kcVec.size(); i++) { //课程
					WritableSheet ws = wwb.createSheet((String) kcVec.get(i), i);
					String kcmc = (String) kcVec.get(i);
					String kcdm = "";
					allIndex = 0;
					index = 0;
					xbindex = 0;
					
					for (int j = 0; j < kcVec2.size(); j+=2) {
						if(kcmc.equalsIgnoreCase((String) kcVec2.get(j+1))){
							kcdm = (String) kcVec2.get(j);
						}
					}
					
					/*sql = " select distinct SUBSTRING(convert(varchar(50),班级代码),1,2) as 年级 from V_自设考试管理_考试学科信息表 a " +

							"left join V_学校班级数据子类 c  on a.班级代码 = c.行政班代码 " +
							"left join V_排课管理_课程表明细详情表 d  on a.班级代码 = d.行政班代码 and a.课程代码  = d.课程代码 " +
							"left join V_规则管理_授课计划主表 as f on a.班级代码=f.行政班代码 " +
							"left join V_规则管理_授课计划明细表 as g on f.授课计划主表编号=g.授课计划主表编号 and a.课程代码=g.课程代码  " +
							"left join V_基础信息_教研组信息表 as h on a.课程代码=h.学科代码 " +
							
							"where a.课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 in(" + ksbh + ") ";
					*/
					
					sql="select distinct left(b.年级代码,2) as 年级 " +
							"from (select * from V_自设考试管理_考试学科信息表 where 课程代码='" + MyTools.fixSql(kcdm) + "' and 考试编号 in (" + ksbh + ")) a " +
							"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码   " +
							"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码   " +
							"left join (select 行政班代码,授课教师编号,授课教师姓名 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码 like '%"  + MyTools.fixSql(kcdm) + "%') d on d.行政班代码=a.班级代码 " +
							"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名 from V_规则管理_授课计划明细表 t1  " +
							"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  " +
							"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码='" + MyTools.fixSql(kcdm) + "') e on e.行政班代码=a.班级代码 " +
							"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  ";
					//2017919翟旭超添加权限判断代码		
					if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
								sql+=" where 1=1 and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
							
								//班主任
								if(sAuth.indexOf(bzr) > -1){
									sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
								}
								
								if(sAuth.indexOf(jyzzz)>-1){
									sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
								}
								//系部
								if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
									sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
								}
								sql+=" ) ";
							}		

					njVec = db.GetContextVector(sql);
					
					for (int j = 0; j < njVec.size(); j++) { //年级 如16级
						
						Vector vector = new Vector();//如【部门，【班级，授课教师，最高分，最低分，平均分，及格率】，】
						Vector vector2 = new Vector();//如【部门，【班级，授课教师】，】
						titleName = year_1 + "学年" + xq + njVec.get(j) + "级月考成绩统计表";
						
						int Exceltitleindex = (ykVec.size()/4)*11+2;//改为+2原来+1
						int Exceltitleindex_yk = (ykVec.size()/4);
						//合并单元格： 第一位与第三位代表列，第二位与第四位代表行    
			            ws.mergeCells(0, 0+allIndex, Exceltitleindex, 0+allIndex);
			            ws.mergeCells(0, 1+allIndex, Exceltitleindex, 1+allIndex);
			            ws.mergeCells(0, 2+allIndex, 0, 3+allIndex);
			            ws.mergeCells(1, 2+allIndex, 1, 3+allIndex);
			            //2017/10/11翟旭超改
			            ws.mergeCells(2, 2+allIndex, 2, 3+allIndex);
			            
			            int index_1 = 1;//int index_1 = 0
			            for (int k = 1; k <= Exceltitleindex_yk; k++) {
			            	ws.mergeCells(2+index_1, 2+allIndex, k*11+1+1, 2+allIndex);//k*11+1
			            	index_1 +=11;
						}
			            
			            /* 
			             * 添加单元格(Cell)内容addCell() 
			             * 添加Label对象Label() 
			             * 数据的类型有很多种、在这里你需要什么类型就导入什么类型 
			             * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label 
			             * Label(i, 0, columns[i], wcf) 
			             * 其中i为列、0为行、columns[i]为数据、wcf为样式 
			             * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中 
			             */  
			           
			            ws.addCell(new Label(0, 0+allIndex, schoolName, contentTitleStyle));
			            ws.addCell(new Label(0, 1+allIndex, titleName, contentTitleStyle));
			            ws.addCell(new Label(0, 2+allIndex, "班级", contentTitle_2Style));
			            
			            //2017/10/11翟旭超加的列
			            ws.addCell(new Label(1, 2+allIndex, "班级人数", contentTitle_2Style));
			            
			            ws.addCell(new Label(2, 2+allIndex, "任课教师", contentTitle_2Style));
			            
			            index_1 = 1;//原来是index_1 = 0
			            for (int k = 0; k < ykVec.size(); k+=4) {
			            	 ws.addCell(new Label(2+index_1, 2+allIndex, (String) ykVec.get(k+1), contentTitle_2Style));
			            	 //ws.addCell(new Label(2+index_1, 3+allIndex, "总人数", contentTitle_2Style));
			            	 //2017/10/11翟旭超改
			            	 ws.addCell(new Label(2+index_1, 3+allIndex, "实考人数", contentTitle_2Style));
			            	 
					         ws.addCell(new Label(3+index_1, 3+allIndex, "等级考人数", contentTitle_2Style));
					         ws.addCell(new Label(4+index_1, 3+allIndex, "最高分", contentTitle_2Style));
					         ws.addCell(new Label(5+index_1, 3+allIndex, "最低分", contentTitle_2Style));
					         ws.addCell(new Label(6+index_1, 3+allIndex, "平均分", contentTitle_2Style));
					         ws.addCell(new Label(7+index_1, 3+allIndex, "及格率", contentTitle_2Style));
					         ws.addCell(new Label(8+index_1, 3+allIndex, "A等", contentTitle_2Style));
					         ws.addCell(new Label(9+index_1, 3+allIndex, "B等", contentTitle_2Style));
					         ws.addCell(new Label(10+index_1, 3+allIndex, "C等", contentTitle_2Style));
					         ws.addCell(new Label(11+index_1, 3+allIndex, "D等", contentTitle_2Style));
					         ws.addCell(new Label(12+index_1, 3+allIndex, "E等", contentTitle_2Style));
					         
			            	 index_1 +=11;
						}
			            
			            
			            
			            
						//系部
						/*sql=" select distinct c.系部名称  from  " +
							" (select * from V_自设考试管理_考试学科信息表 where 课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 in(" + ksbh + ") )a " +
							" left join  " +
							" V_学校班级数据子类 b on a.班级代码 = b.行政班代码 " +
							" left join V_基础信息_系部信息表 c on b.系部代码 = c.系部代码  " +
							
							"left join V_排课管理_课程表明细详情表 f  on b.行政班代码 = f.行政班代码 and a.课程代码  = f.课程代码  "+
							"left join V_规则管理_授课计划主表 as g on b.行政班代码=g.行政班代码   "+
							"left join V_规则管理_授课计划明细表 as h on g.授课计划主表编号=h.授课计划主表编号 and a.课程代码=h.课程代码  "+
							"left join V_基础信息_教研组信息表 as i on a.课程代码=i.学科代码   ";
							//"left join V_基础信息_系部教师信息表 as j on j.教师编号=f.授课教师编号  ";
							
							
							
							sql+=" where substring(b.行政班代码,1,2) = '" + MyTools.fixSql((String) njVec.get(j)) + "' ";
							
							*/
			            
			            sql="select distinct c.系部名称 " +
			            		"from (select * from V_自设考试管理_考试学科信息表 where 课程代码='" + MyTools.fixSql(kcdm) + "' and 考试编号 in (" + ksbh + ")) a " +
			            		"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
			            		"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码   " +
			            		"left join (select 行政班代码,授课教师编号,授课教师姓名 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码 like '%"  + MyTools.fixSql(kcdm) + "%') d on d.行政班代码=a.班级代码  " +
			            		"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名 from V_规则管理_授课计划明细表 t1  " +
			            		"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  " +
			            		"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码='" + MyTools.fixSql(kcdm) + "') e on e.行政班代码=a.班级代码 " +
			            		"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码   " +
			            		" where substring(b.行政班代码,1,2)='" + MyTools.fixSql((String) njVec.get(j)) + "' ";
			          //2017919翟旭超添加权限判断代码		
			            if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
							sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
						
							//班主任
							if(sAuth.indexOf(bzr) > -1){
								sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
							}
							
							if(sAuth.indexOf(jyzzz)>-1){
								sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
							}
							//系部
							if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
								sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
							}
							sql+=" ) ";
						}	


						xbVec = db.GetContextVector(sql);
						//获取系部 班级 教师
						/*sql = " select  d.系部名称,d.行政班名称,d.行政班代码,d.授课教师姓名,d.年级,d.课程代码,d.授课教师编号,d.班主任工号 from( " +
							" select distinct c.系部名称,b.行政班名称,e.授课教师姓名,b.行政班代码,substring(b.行政班代码,1,2) as 年级 ,e.授课教师编号,e.课程代码,b.班主任工号  from   " +
							" (select * from V_自设考试管理_考试学科信息表 where 课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 in(" + ksbh + ") )a " +
							" left join  " +
							" V_学校班级数据子类 b " +
							" on b.行政班代码 = a.班级代码 " +
							" left join " +
							" (select * from V_基础信息_系部信息表 where 系部代码 <> 'C00') c " +
							" on c.系部代码 = b.系部代码 " +
							" left join " +
							" (select * from V_排课管理_课程表明细详情表 where 学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' and 课程代码 = '" + MyTools.fixSql(kcdm) + "')e " +
							" on e.行政班代码 = a.班级代码 " +
							
							
							" ) d " +*/
						
						sql="select distinct c.系部名称,b.行政班名称,b.行政班代码, " +
								"case when d.授课教师姓名 is null then e.授课教师姓名 else d.授课教师姓名 end 授课教师姓名,left(b.年级代码,2) as 年级 ,b.总人数 " +
								"from (select * from V_自设考试管理_考试学科信息表 where 课程代码='" + MyTools.fixSql(kcdm) + "' and 考试编号 in (" + ksbh + ")) a " +
								"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码 " +
								"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
								"left join (select 行政班代码,授课教师编号,授课教师姓名 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码 like '%"  + MyTools.fixSql(kcdm) + "%') d on d.行政班代码=a.班级代码 " +
								"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名 from V_规则管理_授课计划明细表 t1 " +
								"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  " +
								"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码='"  + MyTools.fixSql(kcdm) + "') e on e.行政班代码=a.班级代码 " +
								"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码   " +
								" where substring(b.行政班代码,1,2)='" + MyTools.fixSql((String) njVec.get(j)) + "' " ;
								
						//2017919翟旭超添加权限判断代码
								if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
									sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
									
									//班主任
									if(sAuth.indexOf(bzr) > -1){
										sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
									}
									
									if(sAuth.indexOf(jyzzz)>-1){
										sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
									}
									//系部
									if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
										sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
									}
									sql+=" ) ";
								}
						
						
						xbbjjsVec = db.GetContextVector(sql);
						
						
						for (int k = 0; k < xbVec.size(); k++) {
							vector2.add(xbVec.get(k)); //部门
							Vector vector3 = new Vector();//如【班级，授课教师】
							for (int u = 0; u < xbbjjsVec.size(); u+=6) {
								if(xbVec.get(k).equals(xbbjjsVec.get(u))){
									vector3.add(xbbjjsVec.get(u+1)); //班级
									vector3.add(xbbjjsVec.get(u+2)); //班级代码
									
									//2017/10/11翟旭超加
									vector3.add(xbbjjsVec.get(u+5)); //班级人数
									
									vector3.add(xbbjjsVec.get(u+3)); //授课教师
									
								}
							}
							vector2.add(vector3);
						}
						//-------------------------------------------------------------------------------------------------
						//数据整合
						for (int l = 0; l < vector2.size(); l+=2) { //如【部门，【班级，班级代码，授课教师,班级人数】，】
							vector.add(vector2.get(l));//部门
							Vector tempVec2 = new Vector();
							Vector vec2 = (Vector) vector2.get(l+1);
							for (int m = 0; m < vec2.size(); m+=4) {
								Vector tempVec = new Vector();
								tempVec.add(vec2.get(m));
								
								//2017/10/11翟旭超加
								tempVec.add(vec2.get(m+2));
								
								tempVec.add(vec2.get(m+3));
								
								for (int o = 0; o < ykVec.size(); o+=4) {
									
									sql = " select b.班级代码,a.总人数 ,a.最高分 ,a.最低分 ,a.平均分 ,a.及格率, a.A等个数, a.B等个数, a.C等个数, a.D等个数, a.E等个数  from( " +
									 " select 考试学科编号, cast(max(成绩) as numeric(18,2)) as 最高分, cast(min(成绩) as numeric(18,2)) as 最低分, cast(avg(成绩) as numeric(18,2)) as 平均分, " + 
									 " cast(CAST(SUM(case when 成绩>=60 then 1 end) as float)/COUNT(成绩)as numeric(18,2))as 及格率, COUNT(成绩) as 总人数,  " +
									 " SUM(case when 等级='A' then 1 end ) as A等个数, " +
									 " SUM(case when 等级='B' then 1 end ) as B等个数, " +
									 " SUM(case when 等级='C' then 1 end ) as C等个数, " +
									 " SUM(case when 等级='D' then 1 end ) as D等个数, " +
									 " SUM(case when 等级='E' then 1 end ) as E等个数 " +
									 " from V_自设考试管理_学生成绩信息表 where 考试学科编号  in (  " +
									 " select distinct 考试学科编号 from V_自设考试管理_考试学科信息表 where 班级代码 = '" + MyTools.fixSql((String) vec2.get(m+1)) + "' and 课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 ='" + MyTools.fixSql((String) ykVec.get(o)) + "'" +
									 " ) group by 考试学科编号 ) a  " +
									 " left join V_自设考试管理_考试学科信息表 b  " +
									 " on a.考试学科编号 = b.考试学科编号   " +
									 " left join V_自设考试管理_考试信息表 c  " +
									 " on b.考试编号 = c.考试编号  ";
//									 " order by 班级代码  ";
									ykxxVec = db.GetContextVector(sql);
									
									for (int n = 0; n < ykxxVec.size(); n+=11) {
//										if(djVec.size() > 0){
//											for (int k = 0; k < djVec.size(); k+=2) {
//												if(vec2.get(m+1).equals(djVec.get(k))){
												if(vec2.get(m+1).equals(ykxxVec.get(n))){
													if(!"0".equalsIgnoreCase((String) ykxxVec.get(n+1))){
														tempVec.add(ykxxVec.get(n+1)); //总人数
													}else{
														tempVec.add(""); //总人数
													}
//													tempVec.add(djVec.get(k+1)); //等级考人数
//													Double djkrs = Double.parseDouble((String) djVec.get(k+1));
													int djkrs = 0;
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+6))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+6));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+7))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+7));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+8))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+8));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+9))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+9));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+10))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+10));
													}
													if(!"0".equalsIgnoreCase(djkrs+"")){
														tempVec.add(djkrs+""); //等级考人数
													}else{
														tempVec.add(""); //等级考人数
													}
													
													tempVec.add(ykxxVec.get(n+2)); //最高分
													tempVec.add(ykxxVec.get(n+3)); //最低分
													tempVec.add(ykxxVec.get(n+4)); //平均分
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+5))){
														Double jgl = Double.parseDouble((String) ykxxVec.get(n+5)) * 100;
														String jglbfb = String.format("%.2f", jgl) + "%";
														tempVec.add(jglbfb); //及格率
													}else if(!"0".equalsIgnoreCase((String) ykxxVec.get(n+1))&&ykxxVec.get(n+5).equals("")){
														tempVec.add("0.00%"); //及格率
														
													}else{
														tempVec.add(ykxxVec.get(n+5)); //及格率
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+6))){
														Double dj_A = (Double.parseDouble((String) ykxxVec.get(n+6)) / djkrs)  * 100;
														String dj_Abfb = String.format("%.2f", dj_A) + "%";
														tempVec.add(dj_Abfb); //A等
													}else{
														tempVec.add(ykxxVec.get(n+6)); //A等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+7))){
														Double dj_B = (Double.parseDouble((String) ykxxVec.get(n+7)) / djkrs)  * 100;
														String dj_Bbfb = String.format("%.2f", dj_B) + "%";
														tempVec.add(dj_Bbfb); //B等
													}else{
														tempVec.add(ykxxVec.get(n+7)); //B等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+8))){
														Double dj_C = (Double.parseDouble((String) ykxxVec.get(n+8)) / djkrs)  * 100;
														String dj_Cbfb = String.format("%.2f", dj_C) + "%";
														tempVec.add(dj_Cbfb); //C等
													}else{
														tempVec.add(ykxxVec.get(n+8)); //C等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+9))){
														Double dj_D = (Double.parseDouble((String) ykxxVec.get(n+9)) / djkrs)  * 100;
														String dj_Dbfb = String.format("%.2f", dj_D) + "%";
														tempVec.add(dj_Dbfb); //D等
													}else{
														tempVec.add(ykxxVec.get(n+9)); //D等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+10))){
														Double dj_E = (Double.parseDouble((String) ykxxVec.get(n+10)) / djkrs)  * 100;
														String dj_Ebfb = String.format("%.2f", dj_E) + "%";
														tempVec.add(dj_Ebfb); //E等
													}else{
														tempVec.add(ykxxVec.get(n+10)); //E等
													}
													
												}
//											}
//										}else{
//											tempVec.add(ykxxVec.get(n+1)); //总人数
//											tempVec.add(""); //等级考人数
//											tempVec.add(ykxxVec.get(n+2)); //最高分
//											tempVec.add(ykxxVec.get(n+3)); //最低分
//											tempVec.add(ykxxVec.get(n+4)); //平均分
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+5))){
//												Double jgl = Double.parseDouble((String) ykxxVec.get(n+5)) * 100;
//												String jglbfb = String.format("%.2f", jgl) + "%";
//												tempVec.add(jglbfb); //及格率
//											}else{
//												tempVec.add(ykxxVec.get(n+5)); //及格率
//											}
//											tempVec.add(""); //A等
//											tempVec.add(""); //B等
//											tempVec.add(""); //C等
//											tempVec.add(""); //D等
//											tempVec.add(""); //E等
//										}
									}
									if(ykxxVec.size() == 0){
										tempVec.add(""); //总人数
										tempVec.add(""); //等级考人数
										tempVec.add(""); //最高分
										tempVec.add(""); //最低分
										tempVec.add(""); //平均分
										tempVec.add(""); //及格率
										tempVec.add(""); //A等
										tempVec.add(""); //B等
										tempVec.add(""); //C等
										tempVec.add(""); //D等
										tempVec.add(""); //E等
									}
									
								}
								tempVec2.add(tempVec);
							}
							vector.add(tempVec2);
							
						}
						
						//填充单元格
						int tcindex = xbVec.size() + xbbjjsVec.size()/5;
						for (int k = 0; k <= Exceltitleindex; k++) {
							for (int k2 = 0; k2 < tcindex; k2++) {
								ws.addCell(new Label(k, 4+k2+allIndex, "", contentStyle));
							}
						}
						
						//填充单元格右实线
//						for (int k2 = 0; k2 < tcindex; k2++) {
//							for (int k = 1; k <= Exceltitleindex_yk; k++) {
//								ws.addCell(new Label(k*11+1, 4+k2+allIndex, "", contentStyle2));
//							}
//						}
						
						
						//-----------------------------------------------------------------------------------------------
						
						//写入数据
						int index2 = 0;
//						if(j > 0){
							index = 0;
							xbindex = 0;
							
							for (int l = 0; l < vector.size(); l+=2) {
								if(l > 0){
									index2++;
								}
								
								ws.addCell(new Label(0, 4+allIndex+index2, (String) vector.get(l), contentTitle_3Style));
								
								xbindex++;
								Vector tempVector = (Vector) vector.get(l+1);
								for (int m = 0; m < tempVector.size(); m++) {
									Vector lsVec = (Vector) tempVector.get(m);
									for (int n = 0; n < lsVec.size(); n++) {
										ws.addCell(new Label(0+n, 5+allIndex+index2, (String) lsVec.get(n), contentStyle));
									}
									index2++;
								}
								index += tempVector.size();
							}
							
//						}else{
//							for (int l = 0; l < vector.size(); l+=2) {
//								if(l > 0){
//									index2++;
//								}
//								ws.addCell(new Label(0, 4+allIndex+index2, (String) vector.get(l), contentTitle_2Style));
//								
//								xbindex++;
//								Vector tempVector = (Vector) vector.get(l+1);
//								for (int m = 0; m < tempVector.size(); m++) {
//									Vector lsVec = (Vector) tempVector.get(m);
//									for (int n = 0; n < lsVec.size(); n++) {
//										ws.addCell(new Label(0+n, 5+allIndex+index2, (String) lsVec.get(n), contentTitle_2Style));
//									}
//									index2++;
//									index += tempVector.size();
//								}
//							}
//							
//						}
						
						allIndex += index + xbindex + 4;
						index = 0;
						xbindex = 0;
						
					}
					ws.setColumnView(0, 20); // 设置列的宽度    
		            ws.setColumnView(1, 15); // 设置列的宽度    
		            
		            //2017/10/11翟旭超改
		            ws.setColumnView(2, 15); // 设置列的宽度
		            int index_1 = 0;
		            for (int k = 0; k < ykVec.size(); k+=4) {
		            	ws.setColumnView(3+index_1, 15); // 设置列的宽度
		            	ws.setColumnView(4+index_1, 20); // 设置列的宽度
		            	index_1 +=11;
		            }
					
				}
				
				//写入Exel工作表  
		        wwb.write();  
		        //关闭Excel工作薄对象   
		        wwb.close();
		        //关闭输出
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
			
			PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
			//poCtrl1.setWriter(wb);
			poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
			poCtrl1.setJsFunction_AfterDocumentClosed("AfterDocumentClosed()");
			poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
			
			//String fileName = "template.xls";
			
			//创建自定义菜单栏
//			poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
			//poCtrl1.addCustomToolButton("-", "", 0);
			poCtrl1.addCustomToolButton("打印", "print()", 6);
			poCtrl1.addCustomToolButton("下载", "download()", 3);
			poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
			//poCtrl1.addCustomToolButton("关闭", "closeWindow()", 4);
			poCtrl1.setMenubar(false);//隐藏菜单栏
			poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
			
			poCtrl1.setCaption(titleName);
//			poCtrl1.setFileTitle(schoolName + "全日制高职各科成绩");//设置另存为窗口默认文件名
			
			//打开文件
			poCtrl1.webOpen("printView"+savePath, OpenModeType.xlsNormalEdit, "");
			poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
			
			return filePath;
		}
		
		
		return filePath;
	}
	
	
	/**
	 * 月考汇总统计导出
	 * @date:2017-09-08
	 * @author:
	 * @param xnxqbm 学年学期编码
	 * @throws SQLException
	 */
	public String exportYKXQ(String xnxqbm,String sAuth,String usercode) throws SQLException, UnsupportedEncodingException{
		String savePath = "";
		savePath = MyTools.getProp(request, "Base.exportExcelPath");
		String schoolName = MyTools.getProp(request, "Base.schoolName");
		String titleName = "";
		String sql = "";
		String ksbh = "";
		String filePath = "";
		int index = 0;
		int xbindex = 0;
		int allIndex = 0;
		Vector kcVec = null; //课程 [课程名称]
		Vector kcVec2 = null; //课程 [课程代码,课程名称]
		Vector ykVec = null; //月考 [考试编号,考试名称,招生类别,等级考学生类别]
		Vector njVec = null; //年级 [年级]
		Vector xbVec = null; //系部 [系部]
		Vector xbbjjsVec = null; //系部班级教师[系部名称,行政班名称,行政班代码,授课教师姓名,年级]
		Vector ykxxVec = null; //[班级代码,总人数 ,最高分 ,最低分 ,平均分 ,及格率, A等个数, B等个数, C等个数, D等个数, E等个数]
		Vector djVec = null; //[行政班代码,COUNT(学籍号)]
		String admin = MyTools.getProp(request, "Base.admin");//管理员
		String jxzgxz = MyTools.getProp(request, "Base.jxzgxz");//教学主管校长
		String qxjdzr = MyTools.getProp(request, "Base.qxjdzr");//全校教导主任
		String qxjwgl = MyTools.getProp(request, "Base.qxjwgl");//全校教务管理
		String xbjdzr = MyTools.getProp(request, "Base.xbjdzr");//系部教导主任
		String xbjwgl = MyTools.getProp(request, "Base.xbjwgl");//系部教务管理
		String jyzzz = MyTools.getProp(request, "Base.jyzzz");//教研组组长
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		
		sql = "select 考试编号,考试名称,招生类别,等级考学生类别 from V_自设考试管理_考试信息表 " +
			"where 学年学期编码='" + MyTools.fixSql(xnxqbm) + "' and 类别编号='01' order by 创建时间 ";
		ykVec = db.GetContextVector(sql);
		
		if(ykVec.size() > 0){
			for (int i = 0; i < ykVec.size(); i+=4) {
				ksbh += "'" + ykVec.get(i) + "',";
			}
			ksbh = ksbh.substring(0, ksbh.length()-1);
			
			sql = "select distinct a.课程代码,b.课程名称 " +
				"from (select * from V_自设考试管理_考试学科信息表 where 考试编号 in (" + ksbh + ")) a " +
				"left join V_课程数据子类 b on a.课程代码 = b.课程号 ";
			kcVec2 = db.GetContextVector(sql);
			
			sql = "select distinct g.课程名称 " +
				"from (select * from V_自设考试管理_考试学科信息表 where 考试编号 in (" + ksbh + ")) a  " +
				"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
				"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
				"left join (select 行政班代码,授课教师编号,授课教师姓名,课程代码 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"') d on d.行政班代码=a.班级代码 and d.课程代码 like '%'+a.课程代码+'%'" +
				"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名,t1.课程代码 from V_规则管理_授课计划明细表 t1  " +
				"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号 " +
				"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"') e on e.行政班代码=a.班级代码 and e.课程代码=a.课程代码 " +
				"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  " +
				"left join V_课程数据子类 g on g.课程号=a.课程代码" ;
					
			//2017919翟旭超添加权限判断代码
			if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0 && sAuth.indexOf(xbjdzr)<0&&sAuth.indexOf(xbjwgl)<0){
				sql+=" where 1=1 and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
			
				if(sAuth.indexOf(jyzzz)>-1){
					sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
				}
				//系部
				if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
					sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
				}
				//班主任
				if(sAuth.indexOf(bzr) > -1){
					sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
				}
				sql+=" ) ";
			}
			kcVec = db.GetContextVector(sql);
			
			try {
//				Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
//				int year = c.get(Calendar.YEAR); 
//				int month = c.get(Calendar.MONTH); 
//				int date = c.get(Calendar.DATE);
//				int hour = c.get(Calendar.HOUR);
//				int minute = c.get(Calendar.MINUTE);
//				int second = c.get(Calendar.SECOND);
				
				//截取年数
				String year = xnxqbm.substring(0, 4);
				//截取第几学期
				String xq = xnxqbm.substring(4,5);
				if("1".equalsIgnoreCase(xq)){
					xq = "第一学期";
				}
				if("2".equalsIgnoreCase(xq)){
					xq = "第二学期";
				}
				
				savePath += "/"+ year + "学年" + xq +"月考成绩统计表.xls";
//				filePath = request.getSession().getServletContext().getRealPath("/")+"form/customExamManage/printView";
//				filePath = filePath.replaceAll("\\\\", "/");
				
//				savePath = "reportView"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+second+".xls";
//				savePath = "/" + savePath;
				
				//声明工作簿jxl.write.WritableWorkbook  
				WritableWorkbook wwb;  
				//创建文件目录
				File file = new File(filePath);
				//判断文件目录是否存在
				if (!file.exists()) {
					file.mkdir();
				}
				
				filePath += savePath;
				
				OutputStream os = new FileOutputStream(filePath);
			    //根据传进来的file对象创建可写入的Excel工作薄  
			    wwb = Workbook.createWorkbook(os);  
			    
			    WritableFont fontStyle;
				WritableCellFormat contentStyle;
				WritableFont fontTitleStyle;
				WritableCellFormat contentTitleStyle;
				WritableFont fontStyle2;
				WritableCellFormat contentTitle_2Style;
				WritableCellFormat contentTitle_3Style; //系部标题
				WritableCellFormat contentStyle2;
				
				
				 //创建单元格样式  
			    fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				
				contentStyle2 = new WritableCellFormat(fontStyle);
				contentStyle2.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle2.setBorder(Border.RIGHT,  BorderLineStyle.MEDIUM,Colour.BLACK);
				
				fontTitleStyle = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD);
				contentTitleStyle = new WritableCellFormat(fontTitleStyle);
//				contentTitleStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentTitleStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentTitleStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//边框颜色
//				contentTitleStyle.setBackground(Colour.YELLOW);
				
				fontStyle2 = new WritableFont(WritableFont.createFont("宋体"), 14, WritableFont.BOLD);
				contentTitle_2Style = new WritableCellFormat(fontStyle2);
				contentTitle_2Style.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentTitle_2Style.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentTitle_2Style.setBorder(Border.ALL,  BorderLineStyle.MEDIUM,Colour.BLACK);//边框颜色
				
				
				contentTitle_3Style = new WritableCellFormat(fontStyle2);
				contentTitle_3Style.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentTitle_3Style.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentTitle_3Style.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);//边框颜色
				
				
				

			    /* 
			     * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表 
			     * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样 
			     * 代码中的"0"就是sheet1、其它的一一对应。 
			     * createSheet(sheetName, 0)一个是工作表的名称，另一个是工作表在工作薄中的位置 
			     */  
			
				for (int i = 0; i < kcVec.size(); i++) { //课程
					WritableSheet ws = wwb.createSheet((String) kcVec.get(i), i);
					String kcmc = (String) kcVec.get(i);
					String kcdm = "";
					allIndex = 0;
					index = 0;
					xbindex = 0;
					
					for (int j = 0; j < kcVec2.size(); j+=2) {
						if(kcmc.equalsIgnoreCase((String) kcVec2.get(j+1))){
							kcdm = (String) kcVec2.get(j);
						}
					}
					
					//sql = " select distinct SUBSTRING(convert(varchar(50),班级代码),1,2) as 年级 from V_自设考试管理_考试学科信息表 where 课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 in(" + ksbh + ") ";
					sql="select distinct left(b.年级代码,2) as 年级 " +
							"from (select * from V_自设考试管理_考试学科信息表 where 课程代码='" + MyTools.fixSql(kcdm) + "' and 考试编号 in (" + ksbh + ")) a " +
							"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码   " +
							"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码   " +
							"left join (select 行政班代码,授课教师编号,授课教师姓名 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码 like '%"  + MyTools.fixSql(kcdm) + "%') d on d.行政班代码=a.班级代码 " +
							"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名 from V_规则管理_授课计划明细表 t1  " +
							"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  " +
							"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码='" + MyTools.fixSql(kcdm) + "') e on e.行政班代码=a.班级代码 " +
							"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码  ";
					//2017919翟旭超添加权限判断代码		
					if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
								sql+=" where 1=1 and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
							
								//班主任
								if(sAuth.indexOf(bzr) > -1){
									sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
								}
								
								if(sAuth.indexOf(jyzzz)>-1){
									sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
								}
								//系部
								if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
									sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
								}
								sql+=" ) ";
							}		

					
					njVec = db.GetContextVector(sql);
					
					for (int j = 0; j < njVec.size(); j++) { //年级 如16级
						
						Vector vector = new Vector();//如【部门，【班级，授课教师，最高分，最低分，平均分，及格率】，】
						Vector vector2 = new Vector();//如【部门，【班级，授课教师】，】
						titleName = year + "学年" + xq + njVec.get(j) + "级月考成绩统计表";
						
						//int Exceltitleindex = (ykVec.size()/4)*11+1;
						int Exceltitleindex = (ykVec.size()/4)*11+2;//改为+2原来+1
						int Exceltitleindex_yk = (ykVec.size()/4);
						//合并单元格： 第一位与第三位代表列，第二位与第四位代表行    
			            ws.mergeCells(0, 0+allIndex, Exceltitleindex, 0+allIndex);
			            ws.mergeCells(0, 1+allIndex, Exceltitleindex, 1+allIndex);
			            ws.mergeCells(0, 2+allIndex, 0, 3+allIndex);
			            ws.mergeCells(1, 2+allIndex, 1, 3+allIndex);
			            
			            //2017/10/11翟旭超改
			            ws.mergeCells(2, 2+allIndex, 2, 3+allIndex);
			            
			            int index_1 = 1;//int index_1 = 0
			            for (int k = 1; k <= Exceltitleindex_yk; k++) {
			            	ws.mergeCells(2+index_1, 2+allIndex, k*11+1+1, 2+allIndex);//k*11+1
			            	index_1 +=11;
						}
			            
			            /* 
			             * 添加单元格(Cell)内容addCell() 
			             * 添加Label对象Label() 
			             * 数据的类型有很多种、在这里你需要什么类型就导入什么类型 
			             * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label 
			             * Label(i, 0, columns[i], wcf) 
			             * 其中i为列、0为行、columns[i]为数据、wcf为样式 
			             * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中 
			             */  
			           
			            ws.addCell(new Label(0, 0+allIndex, schoolName, contentTitleStyle));
			            ws.addCell(new Label(0, 1+allIndex, titleName, contentTitleStyle));
			            ws.addCell(new Label(0, 2+allIndex, "班级", contentTitle_2Style));
			            
			            //2017/10/11翟旭超加的列
			            ws.addCell(new Label(1, 2+allIndex, "班级人数", contentTitle_2Style));
			            
			            ws.addCell(new Label(2, 2+allIndex, "任课教师", contentTitle_2Style));
			            index_1 = 1;//原来是index_1 = 0
			            for (int k = 0; k < ykVec.size(); k+=4) {
			            	 ws.addCell(new Label(2+index_1, 2+allIndex, (String) ykVec.get(k+1), contentTitle_2Style));
			            	 //ws.addCell(new Label(2+index_1, 3+allIndex, "总人数", contentTitle_2Style));
			            	 
			            	//2017/10/11翟旭超改
			            	 ws.addCell(new Label(2+index_1, 3+allIndex, "实考人数", contentTitle_2Style));
			            	 
					         ws.addCell(new Label(3+index_1, 3+allIndex, "等级考人数", contentTitle_2Style));
					         ws.addCell(new Label(4+index_1, 3+allIndex, "最高分", contentTitle_2Style));
					         ws.addCell(new Label(5+index_1, 3+allIndex, "最低分", contentTitle_2Style));
					         ws.addCell(new Label(6+index_1, 3+allIndex, "平均分", contentTitle_2Style));
					         ws.addCell(new Label(7+index_1, 3+allIndex, "及格率", contentTitle_2Style));
					         ws.addCell(new Label(8+index_1, 3+allIndex, "A等", contentTitle_2Style));
					         ws.addCell(new Label(9+index_1, 3+allIndex, "B等", contentTitle_2Style));
					         ws.addCell(new Label(10+index_1, 3+allIndex, "C等", contentTitle_2Style));
					         ws.addCell(new Label(11+index_1, 3+allIndex, "D等", contentTitle_2Style));
					         ws.addCell(new Label(12+index_1, 3+allIndex, "E等", contentTitle_2Style));
					         
			            	 index_1 +=11;
						}
			            
			            
			            
			            
						//系部
						/*sql = " select distinct d.系部名称 from ( " +
							" select distinct c.系部名称,substring(b.行政班代码,1,2) as 年级 from  " +
							" (select * from V_自设考试管理_考试学科信息表 where 课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 in(" + ksbh + ") )a " +
							" left join  " +
							" V_学校班级数据子类 b " +
							" on a.班级代码 = b.行政班代码 " +
							" left join " +
							" V_基础信息_系部信息表 c " +
							" on b.系部代码 = c.系部代码 " +
							" ) d " +
							" where d.年级 = '" + MyTools.fixSql((String) njVec.get(j)) + "' ";*/
			            
			          //系部
			            sql="select distinct c.系部名称 " +
			            		"from (select * from V_自设考试管理_考试学科信息表 where 课程代码='" + MyTools.fixSql(kcdm) + "' and 考试编号 in (" + ksbh + ")) a " +
			            		"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码  " +
			            		"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码   " +
			            		"left join (select 行政班代码,授课教师编号,授课教师姓名 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码 like '%"  + MyTools.fixSql(kcdm) + "%') d on d.行政班代码=a.班级代码  " +
			            		"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名 from V_规则管理_授课计划明细表 t1  " +
			            		"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  " +
			            		"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码='" + MyTools.fixSql(kcdm) + "') e on e.行政班代码=a.班级代码 " +
			            		"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码   " +
			            		"where  substring(b.行政班代码,1,2)='" + MyTools.fixSql((String) njVec.get(j)) + "' ";
			          //2017919翟旭超添加权限判断代码		
			            if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
							sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
						
							//班主任
							if(sAuth.indexOf(bzr) > -1){
								sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
							}
							
							if(sAuth.indexOf(jyzzz)>-1){
								sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
							}
							//系部
							if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
								sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
							}
							sql+=" ) ";
						}	
			            
			            
			            
						xbVec = db.GetContextVector(sql);
						
						//获取系部 班级 教师
						/*sql = " select d.系部名称,d.行政班名称,d.行政班代码,d.授课教师姓名,d.年级 from( " +
							" select distinct c.系部名称,b.行政班名称,e.授课教师姓名,b.行政班代码,substring(b.行政班代码,1,2) as 年级 from   " +
							" (select * from V_自设考试管理_考试学科信息表 where 课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 in(" + ksbh + ") )a " +
							" left join  " +
							" V_学校班级数据子类 b " +
							" on b.行政班代码 = a.班级代码 " +
							" left join " +
							" (select * from V_基础信息_系部信息表 where 系部代码 <> 'C00') c " +
							" on c.系部代码 = b.系部代码 " +
							" left join " +
							" (select * from V_排课管理_课程表明细详情表 where 学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' and 课程代码 = '" + MyTools.fixSql(kcdm) + "')e " +
							" on e.行政班代码 = a.班级代码 " +
							" ) d " +
							
							" where d.年级 ='" + MyTools.fixSql((String) njVec.get(j)) + "' ";*/
						
						sql="select distinct c.系部名称,b.行政班名称,b.行政班代码, " +
								"case when d.授课教师姓名 is null then e.授课教师姓名 else d.授课教师姓名 end 授课教师姓名,left(b.年级代码,2) as 年级 ,b.总人数 " +
								"from (select * from V_自设考试管理_考试学科信息表 where 课程代码='" + MyTools.fixSql(kcdm) + "' and 考试编号 in (" + ksbh + ")) a " +
								"left join V_学校班级数据子类 b on b.行政班代码=a.班级代码 " +
								"left join V_基础信息_系部信息表 c on c.系部代码=b.系部代码  " +
								"left join (select 行政班代码,授课教师编号,授课教师姓名 from V_排课管理_课程表明细详情表 where 学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码 like '%"  + MyTools.fixSql(kcdm) + "%') d on d.行政班代码=a.班级代码 " +
								"left join (select t2.行政班代码,t1.授课教师编号,t1.授课教师姓名 from V_规则管理_授课计划明细表 t1 " +
								"left join V_规则管理_授课计划主表 t2 on t2.授课计划主表编号=t1.授课计划主表编号  " +
								"where t2.学年学期编码='"+MyTools.fixSql(xnxqbm)+"' and 课程代码='"  + MyTools.fixSql(kcdm) + "') e on e.行政班代码=a.班级代码 " +
								"left join V_基础信息_教研组信息表 f on f.学科代码=a.课程代码   " +
								"where  substring(b.行政班代码,1,2)='" + MyTools.fixSql((String) njVec.get(j)) + "' " ;
								
						//2017919翟旭超添加权限判断代码
								if(sAuth.indexOf(admin)<0 && sAuth.indexOf(jxzgxz)<0 && sAuth.indexOf(qxjdzr)<0 && sAuth.indexOf(qxjwgl)<0){
									sql+=" and (d.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' or e.授课教师编号 like '%" + MyTools.fixSql(usercode) + "%' ";
									
									//班主任
									if(sAuth.indexOf(bzr) > -1){
										sql += " or b.班主任工号='" + MyTools.fixSql(usercode) + "' ";
									}
									
									if(sAuth.indexOf(jyzzz)>-1){
										sql+=" or f.教研组组长编号 like ('%@" + MyTools.fixSql(usercode) + "@%') ";
									}
									//系部
									if(sAuth.indexOf(xbjdzr)>-1 || sAuth.indexOf(xbjwgl)>-1){
										sql += " or c.系部代码 in (select 系部代码 from V_基础信息_系部教师信息表 where 教师编号='" + MyTools.fixSql(usercode) + "')";
									}
									sql+=" ) ";
								}
						
						
						
						xbbjjsVec = db.GetContextVector(sql);
						
						
						for (int k = 0; k < xbVec.size(); k++) {
							vector2.add(xbVec.get(k)); //部门
							Vector vector3 = new Vector();//如【班级，授课教师】
							for (int u = 0; u < xbbjjsVec.size(); u+=6) {
								if(xbVec.get(k).equals(xbbjjsVec.get(u))){
									vector3.add(xbbjjsVec.get(u+1)); //班级
									vector3.add(xbbjjsVec.get(u+2)); //班级代码
									
									//2017/10/11翟旭超加
									vector3.add(xbbjjsVec.get(u+5)); //班级人数
									
									vector3.add(xbbjjsVec.get(u+3)); //授课教师
								}
							}
							vector2.add(vector3);
						}
						
						
						//数据整合
						for (int l = 0; l < vector2.size(); l+=2) { //如【部门，【班级，班级代码，授课教师,班级人数】，】
							vector.add(vector2.get(l));//部门
							Vector tempVec2 = new Vector();
							Vector vec2 = (Vector) vector2.get(l+1);
							for (int m = 0; m < vec2.size(); m+=4) {
								Vector tempVec = new Vector();
								tempVec.add(vec2.get(m));
								
								//2017/10/11翟旭超加
								tempVec.add(vec2.get(m+2));
								
								tempVec.add(vec2.get(m+3));
								
								for (int o = 0; o < ykVec.size(); o+=4) {
									
									sql = " select b.班级代码,a.总人数 ,a.最高分 ,a.最低分 ,a.平均分 ,a.及格率, a.A等个数, a.B等个数, a.C等个数, a.D等个数, a.E等个数  from( " +
									 " select 考试学科编号, cast(max(成绩) as numeric(18,2)) as 最高分, cast(min(成绩) as numeric(18,2)) as 最低分, cast(avg(成绩) as numeric(18,2)) as 平均分, " + 
									 " cast(CAST(SUM(case when 成绩>=60 then 1 end) as float)/COUNT(成绩)as numeric(18,2))as 及格率, COUNT(成绩) as 总人数,  " +
									 " SUM(case when 等级='A' then 1 end ) as A等个数, " +
									 " SUM(case when 等级='B' then 1 end ) as B等个数, " +
									 " SUM(case when 等级='C' then 1 end ) as C等个数, " +
									 " SUM(case when 等级='D' then 1 end ) as D等个数, " +
									 " SUM(case when 等级='E' then 1 end ) as E等个数 " +
									 " from V_自设考试管理_学生成绩信息表 where 考试学科编号  in (  " +
									 " select distinct 考试学科编号 from V_自设考试管理_考试学科信息表 where 班级代码 = '" + MyTools.fixSql((String) vec2.get(m+1)) + "' and 课程代码 = '" + MyTools.fixSql(kcdm) + "' and 考试编号 ='" + MyTools.fixSql((String) ykVec.get(o)) + "'" +
									 " ) group by 考试学科编号 ) a  " +
									 " left join V_自设考试管理_考试学科信息表 b  " +
									 " on a.考试学科编号 = b.考试学科编号   " +
									 " left join V_自设考试管理_考试信息表 c  " +
									 " on b.考试编号 = c.考试编号  ";
//									 " order by 班级代码  ";
									
									ykxxVec = db.GetContextVector(sql);
									
//									String djkxslb = (String) ykVec.get(o+3); // 等级考学生类别
//									String[] str = djkxslb.split(",");
//									djkxslb = "";
//									for (int k = 0; k < str.length; k++) {
//										djkxslb += "'" + str[k] + "',";
//									}
//									djkxslb = djkxslb.substring(0, djkxslb.length()-1);
//									
//									sql = " select 行政班代码,COUNT(学籍号) as 等级考人数 from V_学生基本数据子类 where 学生类别 in(" + djkxslb + ") group by 行政班代码 ";
//									djVec = db.GetContextVector(sql);
									
									for (int n = 0; n < ykxxVec.size(); n+=11) {
//										if(djVec.size() > 0){
//											for (int k = 0; k < djVec.size(); k+=2) {
//												if(vec2.get(m+1).equals(djVec.get(k))){
												if(vec2.get(m+1).equals(ykxxVec.get(n))){
													if(!"0".equalsIgnoreCase((String) ykxxVec.get(n+1))){
														tempVec.add(ykxxVec.get(n+1)); //总人数
													}else{
														tempVec.add(""); //总人数
													}
//													tempVec.add(djVec.get(k+1)); //等级考人数
//													Double djkrs = Double.parseDouble((String) djVec.get(k+1));
													int djkrs = 0;
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+6))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+6));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+7))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+7));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+8))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+8));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+9))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+9));
													}
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+10))){
														djkrs += Integer.parseInt((String) ykxxVec.get(n+10));
													}
													if(!"0".equalsIgnoreCase(djkrs+"")){
														tempVec.add(djkrs+""); //等级考人数
													}else{
														tempVec.add(""); //等级考人数
													}
													
													tempVec.add(ykxxVec.get(n+2)); //最高分
													tempVec.add(ykxxVec.get(n+3)); //最低分
													tempVec.add(ykxxVec.get(n+4)); //平均分
													/*if(!"".equalsIgnoreCase((String) ykxxVec.get(n+5))){
														Double jgl = Double.parseDouble((String) ykxxVec.get(n+5)) * 100;
														String jglbfb = String.format("%.2f", jgl) + "%";
														tempVec.add(jglbfb); //及格率
													}else{
														tempVec.add(ykxxVec.get(n+5)); //及格率
													}*/
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+5))){
														Double jgl = Double.parseDouble((String) ykxxVec.get(n+5)) * 100;
														String jglbfb = String.format("%.2f", jgl) + "%";
														tempVec.add(jglbfb); //及格率
													}else if(!"0".equalsIgnoreCase((String) ykxxVec.get(n+1))&&ykxxVec.get(n+5).equals("")){
														tempVec.add("0.00%"); //及格率
														
													}else{
														tempVec.add(ykxxVec.get(n+5)); //及格率
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+6))){
														Double dj_A = (Double.parseDouble((String) ykxxVec.get(n+6)) / djkrs)  * 100;
														String dj_Abfb = String.format("%.2f", dj_A) + "%";
														tempVec.add(dj_Abfb); //A等
													}else{
														tempVec.add(ykxxVec.get(n+6)); //A等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+7))){
														Double dj_B = (Double.parseDouble((String) ykxxVec.get(n+7)) / djkrs)  * 100;
														String dj_Bbfb = String.format("%.2f", dj_B) + "%";
														tempVec.add(dj_Bbfb); //B等
													}else{
														tempVec.add(ykxxVec.get(n+7)); //B等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+8))){
														Double dj_C = (Double.parseDouble((String) ykxxVec.get(n+8)) / djkrs)  * 100;
														String dj_Cbfb = String.format("%.2f", dj_C) + "%";
														tempVec.add(dj_Cbfb); //C等
													}else{
														tempVec.add(ykxxVec.get(n+8)); //C等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+9))){
														Double dj_D = (Double.parseDouble((String) ykxxVec.get(n+9)) / djkrs)  * 100;
														String dj_Dbfb = String.format("%.2f", dj_D) + "%";
														tempVec.add(dj_Dbfb); //D等
													}else{
														tempVec.add(ykxxVec.get(n+9)); //D等
													}
													
													if(!"".equalsIgnoreCase((String) ykxxVec.get(n+10))){
														Double dj_E = (Double.parseDouble((String) ykxxVec.get(n+10)) / djkrs)  * 100;
														String dj_Ebfb = String.format("%.2f", dj_E) + "%";
														tempVec.add(dj_Ebfb); //E等
													}else{
														tempVec.add(ykxxVec.get(n+10)); //E等
													}
													
												}
//											}
//										}else{
//											tempVec.add(ykxxVec.get(n+1)); //总人数
////											tempVec.add(""); //等级考人数
//											int djkrs = 0;
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+6))){
//												djkrs += Integer.parseInt((String) ykxxVec.get(n+6));
//											}
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+7))){
//												djkrs += Integer.parseInt((String) ykxxVec.get(n+7));
//											}
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+8))){
//												djkrs += Integer.parseInt((String) ykxxVec.get(n+8));
//											}
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+9))){
//												djkrs += Integer.parseInt((String) ykxxVec.get(n+9));
//											}
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+10))){
//												djkrs += Integer.parseInt((String) ykxxVec.get(n+10));
//											}
//											tempVec.add(djkrs+""); //等级考人数
//											
//											tempVec.add(ykxxVec.get(n+2)); //最高分
//											tempVec.add(ykxxVec.get(n+3)); //最低分
//											tempVec.add(ykxxVec.get(n+4)); //平均分
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+5))){
//												Double jgl = Double.parseDouble((String) ykxxVec.get(n+5)) * 100;
//												String jglbfb = String.format("%.2f", jgl) + "%";
//												tempVec.add(jglbfb); //及格率
//											}else{
//												tempVec.add(ykxxVec.get(n+5)); //及格率
//											}
////											tempVec.add(""); //A等
////											tempVec.add(""); //B等
////											tempVec.add(""); //C等
////											tempVec.add(""); //D等
////											tempVec.add(""); //E等
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+6))){
//												Double dj_A = (Double.parseDouble((String) ykxxVec.get(n+6)) / djkrs)  * 100;
//												String dj_Abfb = String.format("%.2f", dj_A) + "%";
//												tempVec.add(dj_Abfb); //A等
//											}else{
//												tempVec.add(ykxxVec.get(n+6)); //A等
//											}
//											
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+7))){
//												Double dj_B = (Double.parseDouble((String) ykxxVec.get(n+7)) / djkrs)  * 100;
//												String dj_Bbfb = String.format("%.2f", dj_B) + "%";
//												tempVec.add(dj_Bbfb); //B等
//											}else{
//												tempVec.add(ykxxVec.get(n+7)); //B等
//											}
//											
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+8))){
//												Double dj_C = (Double.parseDouble((String) ykxxVec.get(n+8)) / djkrs)  * 100;
//												String dj_Cbfb = String.format("%.2f", dj_C) + "%";
//												tempVec.add(dj_Cbfb); //C等
//											}else{
//												tempVec.add(ykxxVec.get(n+8)); //C等
//											}
//											
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+9))){
//												Double dj_D = (Double.parseDouble((String) ykxxVec.get(n+9)) / djkrs)  * 100;
//												String dj_Dbfb = String.format("%.2f", dj_D) + "%";
//												tempVec.add(dj_Dbfb); //D等
//											}else{
//												tempVec.add(ykxxVec.get(n+9)); //D等
//											}
//											
//											if(!"".equalsIgnoreCase((String) ykxxVec.get(n+10))){
//												Double dj_E = (Double.parseDouble((String) ykxxVec.get(n+10)) / djkrs)  * 100;
//												String dj_Ebfb = String.format("%.2f", dj_E) + "%";
//												tempVec.add(dj_Ebfb); //E等
//											}else{
//												tempVec.add(ykxxVec.get(n+10)); //E等
//											}
//										}
									}
									if(ykxxVec.size() == 0){
										tempVec.add(""); //总人数
										tempVec.add(""); //等级考人数
										tempVec.add(""); //最高分
										tempVec.add(""); //最低分
										tempVec.add(""); //平均分
										tempVec.add(""); //及格率
										tempVec.add(""); //A等
										tempVec.add(""); //B等
										tempVec.add(""); //C等
										tempVec.add(""); //D等
										tempVec.add(""); //E等
									}
									
								}
								tempVec2.add(tempVec);
							}
							vector.add(tempVec2);
							
						}
						
						//填充单元格
						System.out.println(xbVec.size()+"=="+xbbjjsVec.size()/5+"=="+Exceltitleindex);
						int tcindex = xbVec.size() + xbbjjsVec.size()/5; 
						for (int k = 0; k <= Exceltitleindex; k++) {
							for (int k2 = 0; k2 < tcindex; k2++) {
								ws.addCell(new Label(k, 4+k2+allIndex, "", contentStyle));
							}
						}
						
						//填充单元格右实线
//						for (int k2 = 0; k2 < tcindex; k2++) {
//							for (int k = 1; k <= Exceltitleindex_yk; k++) {
//								ws.addCell(new Label(k*11+1, 4+k2+allIndex, "", contentStyle2));
//							}
//						}
						
						
						//-----------------------------------------------------------------------------------------------
						
						//写入数据
						int index2 = 0;
//						if(j > 0){
							index = 0;
							xbindex = 0;
							
							for (int l = 0; l < vector.size(); l+=2) {
								if(l > 0){
									index2++;
								}
								
								ws.addCell(new Label(0, 4+allIndex+index2, (String) vector.get(l), contentTitle_3Style));
								
								xbindex++;
								Vector tempVector = (Vector) vector.get(l+1);
								for (int m = 0; m < tempVector.size(); m++) {
									Vector lsVec = (Vector) tempVector.get(m);
									for (int n = 0; n < lsVec.size(); n++) {
										ws.addCell(new Label(0+n, 5+allIndex+index2, (String) lsVec.get(n), contentStyle));
									}
									index2++;
								}
								index += tempVector.size();
								
							}
							
//						}else{
//							for (int l = 0; l < vector.size(); l+=2) {
//								if(l > 0){
//									index2++;
//								}
//								ws.addCell(new Label(0, 4+allIndex+index2, (String) vector.get(l), contentTitle_2Style));
//								
//								xbindex++;
//								Vector tempVector = (Vector) vector.get(l+1);
//								for (int m = 0; m < tempVector.size(); m++) {
//									Vector lsVec = (Vector) tempVector.get(m);
//									for (int n = 0; n < lsVec.size(); n++) {
//										ws.addCell(new Label(0+n, 5+allIndex+index2, (String) lsVec.get(n), contentTitle_2Style));
//									}
//									index2++;
//									index += tempVector.size();
//								}
//							}
//							
//						}
						
						allIndex += index + xbindex + 4;
						index = 0;
						xbindex = 0;
						
						
					}
					ws.setColumnView(0, 20); // 设置列的宽度    
		            ws.setColumnView(1, 15); // 设置列的宽度   
		            
		            //2017/10/11翟旭超改
		            ws.setColumnView(2, 15); // 设置列的宽度
		            
		            int index_1 = 0;
		            for (int k = 0; k < ykVec.size(); k+=4) {
		            	ws.setColumnView(3+index_1, 15); // 设置列的宽度
		            	ws.setColumnView(4+index_1, 20); // 设置列的宽度
		            	index_1 +=11;
		            }
					
				}
				
				//写入Exel工作表  
		        wwb.write();  
		        //关闭Excel工作薄对象   
		        wwb.close();
		        //关闭输出
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
			
			return savePath;
		}
		
		this.setMSG("文件生成失败");
		
		return savePath;
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
     * 判断当前学年学期是否有月考信息
     * @param xnxqbm 学年学期编码
     * @return 
     * @throws SQLException 
     */  
    public void pdykxx(String xnxqbm) throws SQLException {
    	String sql = " select COUNT(*) from V_自设考试管理_考试信息表 where 类别编号 = '01' and 学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' ";
    	if(db.getResultFromDB(sql)){
			this.setMSG("当前学年学期有月考数据信息");
		}else{
			this.setMSG("当前学年学期无月考数据信息");
		}
    }
	
	
	
	//计算 百分比后添加两位小数
	public String changeTwoDecimal_f2 (double x){
		String s_x = "";
		if(x != 0.0){
			double f_x = x * 100;
			s_x = String.format("%.2f", f_x);
		}else{
			s_x = "0.00";
		}
		return s_x;
		
	}
	
	
	
	//get&set 方法
	public String getBJDM() {
		return BJDM;
	}

	public void setBJDM(String bJDM) {
		BJDM = bJDM;
	}

	public String getKCDM() {
		return KCDM;
	}

	public void setKCDM(String kCDM) {
		KCDM = kCDM;
	}

	public String getXNXQBM() {
		return XNXQBM;
	}

	public void setXNXQBM(String xNXQBM) {
		XNXQBM = xNXQBM;
	}

	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getQSXNXQ() {
		return QSXNXQ;
	}

	public void setQSXNXQ(String qSXNXQ) {
		QSXNXQ = qSXNXQ;
	}

	public String getJSXNXQ() {
		return JSXNXQ;
	}

	public void setJSXNXQ(String jSXNXQ) {
		JSXNXQ = jSXNXQ;
	}

	public String getSTUNAME() {
		return STUNAME;
	}

	public void setSTUNAME(String sTUNAME) {
		STUNAME = sTUNAME;
	}

	public String getKSXKBH() {
		return KSXKBH;
	}

	public void setKSXKBH(String kSXKBH) {
		KSXKBH = kSXKBH;
	}

	public String getKSBH() {
		return KSBH;
	}

	public void setKSBH(String kSBH) {
		KSBH = kSBH;
	}

	public String getAUTHCODE() {
		return AUTHCODE;
	}

	public void setAUTHCODE(String aUTHCODE) {
		AUTHCODE = aUTHCODE;
	}

	
}
