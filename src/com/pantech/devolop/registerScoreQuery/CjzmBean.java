package com.pantech.devolop.registerScoreQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringEscapeUtils;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;



/*
@date 2017-12-25
@author zouyu
模块：M1.18学期学籍卡
说明:
重要及特殊方法：
*/



public class CjzmBean  {
	private String USERCODE;//用户编号
	private String JX_XJH;//学籍号
	private String JX_XM;//姓名
	private String JX_XH;//学号
	private String JX_YWXM;//英文姓名
	private String JX_XZBDM;//行政班代码
	private String JX_BNXH;//班内学号
	private String JX_XMPY;//姓名拼音
	private String JX_CYM;//曾用名
	private String JX_XBM;//性别码
	private String JX_RXRQ;//入学日期
	private String JX_BRSJ;//本人手机
	private String JX_JTYB;//家庭邮编
	private String JX_SFBSHJ;//是否本市户籍
	private String JX_BYLXRQ;//毕业离校日期
	private String JX_BYNF;//毕业年份
	private String JX_XSLBM;//学生类别码
	private String JX_SFZJH;//身份证件号
	private String JX_CSNY;//出身年月
	private String JX_MZM;//民族码
	private String JX_XXM;//血型码
	private String JX_ZJXY;//宗教信仰
	private String JX_GATQM;//港澳台侨
	private String JX_ZZMMM;//政治面貌
	private String JX_FQZZMMM;//父亲政治面貌
	private String JX_MQZZMMM;//母亲政治面貌
	private String JX_QTCYZZMMM;//其他成员政治面貌
	private String JX_GBM;//国籍
	private String JX_JKZKM;//健康状况码
	private String JX_JGM;//籍贯码
	private String JX_HYZK;//婚姻状况
	private String JX_CSDZM;//出生地址
	private String JX_BYXX;//毕业学校
	private String JX_XZZZ;//现在住址
	private String JX_HKSZD;//户口所在地
	private String JX_HKLBM;//户口类别码
	private String JX_SSPCS;//所属派出所
	private String JX_TXDZ;//通讯地址
	private String JX_YZBM;//邮政编码
	private String JX_ZSLBM;//招生类别码
	private String JX_PZH;//拍照号
	private String JX_SFZJLXM;//身份证件类型码
	private String JX_QTCYDH;//其他成员电话/
	private String JX_QTCYNL;//其他成员年龄
	private String JX_QTCYGZDW;//其他成员工作单位
	private String JX_QTCYCW;//其他成员称谓
	private String JX_QTCYXM;//其他成员姓名
	private String JX_MQDH;//母亲电话
	private String JX_MQNL;//母亲年龄
	private String JX_MQGZDW;//母亲工作单位
	private String JX_MQXM;//母亲姓名
	private String JX_FQXM;//父亲姓名
	private String JX_FQNL;//父亲年龄
	private String JX_FQDH;//父亲电话父亲电话
	private String JX_FQGZDW;//父亲工作单位
	private String JX_ZPLJ;//照片路径
	private String JX_LDRKZK;//流动人口
	private String JX_BZ;//备注 
	
	private String JX_XSZT;//学生状态 
	private String JX_YDLXM;//异动类型码
	private String JX_YDRQ;//异动日期
	private String JX_YBJDM;//原班级代码
	private String JX_XBJDM;//新班级代码
	private String JX_YDYY;//异动原因
	private String JX_YDLYXX;//异来源学校
	private String JX_YDQXXX;//异动去向学校
	private String JX_YYXSH;//原院系所好
	private String JX_YZYM;//原专业吗
	private String JX_YBH;//原班号
	private String JX_YNJ;//原年级
	private String JX_XYXSH;//现院系所号
	private String JX_XZYM;//现专业码
	private String JX_XBH;//现班号
	private String JX_XNJ;//现年级
	
	private String sql = ""; // 查询用SQL语句
	private Vector vec = null;

	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	private String MSG2;
	
	/**
	 * 构造函数
	 * @param request
	 */
	public CjzmBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request); // 数据库对象
		this.initialData(); // 初始化参数
	}
	
	/**
	 * 初始化变量
		@date 2017-12-25
		@author zouyu
	 */
	public void initialData() {
		JX_XJH ="";//学籍号
		JX_XM ="";//姓名
		JX_XH ="";//学号
		JX_YWXM ="";//英文姓名
		JX_XZBDM ="";//行政班代码
		JX_BNXH = "";//班内学号
		JX_XMPY ="";//姓名拼音
		JX_CYM ="";//曾用名
		JX_XBM ="";//性别码
		JX_RXRQ ="";//入学日期
		JX_BRSJ ="";//本人手机
		JX_JTYB ="";//家庭邮编
		JX_SFBSHJ ="";//是否本市户籍
		JX_BYLXRQ ="";//毕业离校日期
		JX_BYNF ="";//毕业年份
		JX_XSLBM ="";//学生类别码
		JX_SFZJH ="";//身份证件号
		JX_CSNY ="";//出身年月
		JX_MZM ="";//民族码
		JX_XXM ="";//血型码
		JX_ZJXY ="";//宗教信仰
		JX_GATQM ="";//港澳台侨
		JX_ZZMMM ="";//政治面貌
		JX_FQZZMMM ="";//父亲政治面貌
		JX_MQZZMMM ="";//母亲政治面貌
		JX_QTCYZZMMM ="";//其他成员政治面貌
		JX_GBM ="";//国籍
		JX_JKZKM ="";//健康状况码
		JX_JGM ="";//籍贯码
		JX_HYZK ="";//婚姻状况
		JX_CSDZM ="";//出生地址
		JX_BYXX ="";//毕业学校
		JX_XZZZ ="";//现在住址
		JX_HKSZD ="";//户口所在地
		JX_HKLBM ="";//户口类别码
		JX_SSPCS ="";//所属派出所
		JX_TXDZ ="";//通讯地址
		JX_YZBM ="";//邮政编码
		JX_ZSLBM ="";//招生类别码
		JX_PZH ="";//拍照号
		JX_SFZJLXM ="";//身份证件类型码
		JX_QTCYDH ="";//其他成员电话/
		JX_QTCYNL ="";//其他成员年龄
		JX_QTCYGZDW ="";//其他成员工作单位
		JX_QTCYCW ="";//其他成员称谓
		JX_QTCYXM ="";//其他成员姓名
		JX_MQDH ="";//母亲电话
		JX_MQNL ="";//母亲年龄
		JX_MQGZDW ="";//母亲工作单位
		JX_MQXM ="";//母亲姓名
		JX_FQXM ="";//父亲姓名
		JX_FQNL ="";//父亲年龄
		JX_FQDH ="";//父亲电话父亲电话
		JX_BZ ="";//备注
		JX_FQGZDW ="";//父亲工作单位
		JX_ZPLJ ="";//照片路径
		JX_LDRKZK="";//流动人口
		JX_XSZT="";//学生状态
		JX_YDLXM="";//异动类型码
		JX_YDRQ="";//异动日期
		JX_YBJDM="";//原班级代码
		JX_XBJDM="";//新班级代码
		JX_YDYY="";//异动原因
		JX_YDLYXX="";//异来源学校
		JX_YDQXXX="";//异动去向学校
		JX_YYXSH="";//原院系所好
		JX_YZYM="";//原专业吗
		JX_YBH="";//原班号
		JX_YNJ="";//原年级
		JX_XYXSH="";//现院系所号
		JX_XZYM="";//现专业码
		JX_XBH="";//现班号
		JX_XNJ="";//现年级
	}
	
	/**
	 * 分页查询学生信息
	 * @param pageNum
	 * @param rows
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector queStudent(int pageNum,int rows) throws SQLException{
		String XSLX = MyTools.StrFiltr(request.getParameter("XSLX"));
		
		sql = "select a.学籍号,a.学号,a.班内学号,b.系部代码,e.系部名称,a.行政班代码,b.行政班名称,b.年级代码,a.姓名,isnull(d.专业名称,'') as 专业名称,d.专业代码,a.英文姓名,a.姓名拼音,a.曾用名,a.身份证件类型码,a.身份证件号," +
			"a.性别码,a.血型码,a.出生日期,a.出生地码,a.籍贯,a.民族码,a.婚姻状况码,a.信仰宗教码,a.港澳台侨外码,a.健康状况码,a.政治面貌码," +
			"a.户口所在地行政区划码,a.户口类别码, a.是否是流动人口, a.[国籍/ 地区码] as 国籍,a.电子信箱,a.照片,a.照片路径,a.常住地址,a.户籍地址,a.户籍邮编," +
			"a.家庭邮编,a.家长手机,a.是否本市户籍,a.学生状态,c.类别名称,a.家庭电话,a.奖惩,CONVERT(date,a.毕业离校日期) as 毕业离校日期,a.人员类型,a.学生类别,a.招生类别," +
			"CONVERT(nvarchar,a.入学日期,21) as 入学日期,a.毕业年份,a.拍照号 ,a.备注,a.所属派出所,a.毕业学校," +
			"a.父亲 ,a.父亲年龄, a.父亲政治面貌,a.父亲工作单位,a.父亲电话,a.母亲,a.母亲年龄,a.母亲政治面貌,a.母亲工作单位,a.母亲电话," +
			"a.其他成员称谓,a.其他成员姓名,a.其他成员年龄,a.其他成员政治面貌,a.其他成员工作单位,a.其他成员电话 " +
			"from V_学生基本数据子类 a " +
			"left join dbo.V_学校班级数据子类 b on a.行政班代码=b.行政班代码 " +
			"left join dbo.V_信息类别_类别操作 c on a.学生状态=c.描述  and c.父类别代码='XSZTM' " +
			"left join V_专业基本信息数据子类 d on a.专业代码=d.专业代码 " +
			"left join V_基础信息_系部信息表 e on e.系部代码=b.系部代码 " +
			"where 1=1 " ;
		if(!"".equalsIgnoreCase(this.getJX_XSZT())){
			sql+=" and a.学生状态 = ('" + MyTools.fixSql(this.getJX_XSZT()) + "')";
		}
		if(!"".equalsIgnoreCase(this.getJX_XJH())){
			sql += " and a.学籍号 like '%" + MyTools.fixSql(this.getJX_XJH()) + "%'";
		}
		
		if(!"".equalsIgnoreCase(this.getJX_XH())){
			sql += " and a.学号 like '%" + MyTools.fixSql(this.getJX_XH()) + "%'";
		}
		
		if(!"".equalsIgnoreCase(this.getJX_XM())){
			sql += " and a.姓名 like '%" + MyTools.fixSql(this.getJX_XM()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getJX_XZBDM())){
			sql += " and a.行政班代码='" + MyTools.fixSql(this.getJX_XZBDM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getJX_YYXSH())){
			sql += " and b.系部代码='" + MyTools.fixSql(this.getJX_YYXSH()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getJX_YZYM())){
			sql += " and a.专业代码='" + MyTools.fixSql(this.getJX_YZYM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getJX_YNJ())){
			sql += " and b.年级代码='" + MyTools.fixSql(this.getJX_YNJ()) + "'";
		}
		sql += " order by a.学籍号";
		vec = db.getConttexJONSArr(sql, pageNum, rows);
		return vec;
	}
	
	
	/**
	 * 读取系下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXMCCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue " +
				"union all " +
				"select 系部名称  as comboName,系部代码 as comboValue from V_基础信息_系部信息表 where 系部代码<>'C00' " +
				"order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadZYMCCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select  a.专业名称 as comName ,a.专业代码 as comValue from dbo.V_专业基本信息数据子类 a order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	/**
	 * 读取学生状态下拉框
	 * @date 2017-12-25
	 * @author zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXszt() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue ,'0' as  orderNum " +
				"union all " +
				"select 类别名称 as comName,描述 as comValue,'1' as orderNum from V_信息类别_类别操作 where 父类别代码='XSYDZTM' " +
				"order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	/**
	 * 读取年级下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNJMCCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.年级名称 as comName,a.年级代码 as comValue from dbo.V_学校年级数据子类 a order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	

	
	/**
	 * 读取学生类别下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXSLBCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='XSLBM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}

	
	/**
	 * 读取性别代码下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXBCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='XBM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}


	

	//班级下拉框赋值
	public Vector loadBJCombo() throws SQLException {
		sql = "select '' as comboValue,'请选择' as comboName " +
			"union all " +
			"select a.行政班代码 as comboValue ,a.行政班名称 as comboName  from V_学校班级数据子类 a order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
/**
	 * 读取年级下拉框
	 * @date 2017-12-25
	 * @author zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportNjCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,'1' as orderNum " +
				"from V_学校年级数据子类 where 年级状态='1' order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取导出系部下拉框
	 * @date 2017-12-25
	 * @author zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportXbCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 系部名称 as comboName,系部代码 as comboValue,'1' as orderNum " +
				"from V_基础信息_系部信息表 where 系部代码<>'C00' order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	public Vector loadExportBjCombo(String NJDM,String XBDM,String SSZY) throws SQLException{
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		
		
		sql ="select 'all' as comboValue,'全部' as comboName,'0' as orderNum "+
				"union all "+ 
				"select distinct 行政班代码 as comboValue,行政班名称 as comboName,'1' as orderNum   " +
				"from V_学校班级数据子类 a " +
				"where 1=1 ";
		if(!"all".equalsIgnoreCase(XBDM)){
			sql += " and 系部代码 in ('" + XBDM.replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(NJDM)){
			sql += " and 年级代码 in ('" + NJDM.replaceAll(",", "','") + "')";
		}
		if(!"all".equalsIgnoreCase(SSZY)){
			sql += " and 专业代码 in ('" + SSZY.replaceAll(",", "','") + "')";
		}
		sql += "order by orderNum,comboValue";
		
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	/**
	 * 读取专业下拉框
	 * @date 2017-12-25
	 * @author zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadExportZyCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '全部' as comboName,'all' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类 where 状态='1' and len(专业代码)>3 order by orderNum,comboName";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	//学生学期卡打印预览
	public  String stuXqkView(HttpServletRequest request,String exportType,String JX_XJH,String BJDM,String XM) throws SQLException, UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8"); //设置字符集
		String sql = "";
		String filePath = "";
		String title = "";
		Vector pyVec=new Vector();
		Vector pySave=new Vector();
		Vector Vec1=new Vector();
		Vector Vec2=new Vector();
		Vector xyspVec = new Vector();//查询学期设置信息
		DBSource db = new DBSource(request); //数据库对象
		Vector stuInfo=new Vector();//学生基本信息
		Vector Vec3=new Vector();//学生基本信息
		Vector subVec = new Vector();
		int count=0;
		String nj[] = {"一","二","三","四"};
		String xq[] = {"一","二"};
		Vector xqzbVec = new Vector();//学期占比设置的信息
		Vector xnVec = new Vector();//学期占比设置的信息
		Vector firstVec = new Vector();//查询上学期课程
		Vector secondVec = new Vector();//查询下学期课程
		Vector scoreInfoVec = new Vector();//查询学生成绩
		Vector scoreVec = new Vector();//存放分数
		String firstSem = "";//上学期
		String secondSem = "";//下学期
		int type = 0;
		int whkType=0;
		int zykType=0;
		String firstSemPercent = "";
		String secondSemPercent = "";
		DecimalFormat f = new DecimalFormat("#");  
		f.setRoundingMode(RoundingMode.HALF_UP); 
		sql = "select 系部代码,学期一,学期二,计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码=(select 系部代码 from V_学校班级数据子类 where 行政班代码='"+BJDM+"')";
	    xqzbVec= db.GetContextVector(sql);
		title =XM;
		title=URLDecoder.decode(URLDecoder.decode(StringEscapeUtils.escapeJava(title), "utf-8"), "utf-8");
	
		filePath = request.getSession().getServletContext().getRealPath("/")+"form/registerScoreManage/";
		filePath = filePath.replaceAll("\\\\", "/");
		
		
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		filePath +=JX_XJH+".xls";
		String savePath=JX_XJH+".xls";
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
		
			//判断计算方式		
			sql = "select 系部代码,学期一,学期二,计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码=(select 系部代码 from V_学校班级数据子类 where 行政班代码='"+BJDM+"')";
		    xqzbVec= db.GetContextVector(sql);
		    if(xqzbVec!=null && xqzbVec.size()>0){
				if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
					type=2;//跨学年2
					}else{
						type=1;//同学年有系部
					}
				}else{
					xqzbVec.add("");
					xqzbVec.add("40");
					xqzbVec.add("60");
					xqzbVec.add("1");
					type=0;//同学年无系部
				}
		    String xn_1="20"+MyTools.fixSql(BJDM).substring(0,2);
			String xn_2=MyTools.parseString(MyTools.StringToInt(xn_1)+1);
			String xn_3=MyTools.parseString(MyTools.StringToInt(xn_1)+2);
			String xn_4=MyTools.parseString(MyTools.StringToInt(xn_1)+3);
			if(type == 2){//跨学年
				firstSem = xn_1+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_1)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem = xn_2+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_2)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem = xn_3+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_3)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem = xn_4+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_4)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
			}else{//同学年
				firstSem =xn_1 + "101";
				secondSem =xn_1+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem =xn_2 + "101";
				secondSem =xn_2+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem =xn_3 + "101";
				secondSem =xn_3+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem =xn_4 + "101";
				secondSem =xn_4+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
			}
			//获取所有公共课的课程
			sql="with tempClassInfo as ("+
				"select distinct b.课程代码,b.课程名称 ,b.行政班代码,a.学号 "+	
				"from V_成绩管理_学生成绩信息表 a  "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +			
				"left join V_学生基本数据子类 d on d.学号=a.学号  "+						
				"where    a.学号='"+MyTools.fixSql(JX_XJH)+"')	 "+			
				"select distinct b.课程代码,b.课程名称 "+
				"from  V_规则管理_授课计划主表 a   "+
				"left join V_规则管理_授课计划明细表 b on a.授课计划主表编号=b.授课计划主表编号    "+
				"left join V_课程数据子类 c on c.课程号=b.课程代码   "+
				"left join tempClassInfo d on d.课程代码=b.课程代码 and d.行政班代码=a.行政班代码 "+
				"where  d.学号='"+MyTools.fixSql(JX_XJH)+"'  order by b.课程代码 ";
				subVec = db.GetContextVector(sql);
				if(subVec.size()==0){
					for(int i=0;i<14;i++){
						subVec.add("");
					}
				}
				int subVecSize=(int)(subVec.size()/2);
				if(subVecSize<5){
						for(int j=0;j<(5-subVecSize);j++){
							subVec.add("");
							subVec.add("");
							
						}
					}
		//获取成绩
				sql = "with tempClassInfo as ("+
						 "select b.学年, a.学号,b.课程代码," +
						 "(case when a.补考='-1' then '免修' when a.补考='-2' then '作弊' when a.补考='-3' then '取消资格' when a.补考='-4' then '缺考'  when a.补考='-5' then '缓考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格'  when a.补考='-17' then '免考' else a.补考 end ) as 补考  " +
						 "from V_成绩管理_学生补考成绩信息表  a "+	
						 "left join  V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+	
						 "where a.学号='"+MyTools.fixSql(JX_XJH)+"')"+
						"select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
						"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
						"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
						"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
						"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
						"cc.补考 ,f.平时比例,f.期中比例,f.期末比例,e.学生状态,f.总评比例选项 "+
						"from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
						"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
						"left join V_规则管理_学年学期表 d on d.学年学期编码=c.学年学期编码 " +
						"left join V_学生基本数据子类 e on e.学号=a.学号 "+
						"left join V_成绩管理_登分设置信息表 f on a.相关编号=f.相关编号 "+
						"left join  V_课程数据子类   g on b.课程代码=g.课程号 "+
						"left join tempClassInfo cc on a.学号=cc.学号 and cc.课程代码=b.课程代码  and SUBSTRING(c.学年学期编码,0,5)=cc.学年  "+
						"where   a.学号='"+MyTools.fixSql(JX_XJH)+"' "+
						"order by a.学号,b.课程代码,c.学年学期编码";
					scoreInfoVec = db.GetContextVector(sql);
				
				for(int i=0;i<xnVec.size();i+=2){			
					sql = "select a.学号,b.课程代码 "+
							"from V_成绩管理_学生成绩信息表 a  "+
							"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
							"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
							"left join V_学生基本数据子类 d on d.学号=a.学号 "+
							"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
							"where c.学年学期编码='" +  xnVec.get(i) + "' " +
							" and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
							"order by b.课程代码";
						firstVec = db.GetContextVector(sql);
				
						
						sql = "select a.学号,b.课程代码 "+
								"from V_成绩管理_学生成绩信息表 a  "+
								"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
								"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
								"left join V_学生基本数据子类 d on d.学号=a.学号 "+
								"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
								"where c.学年学期编码='" +  xnVec.get(i+1) + "' " +
								" and a.学号='"+MyTools.fixSql(JX_XJH)+"' " +
								"order by b.课程代码";	
						secondVec = db.GetContextVector(sql);
						
			
						String curStuCode = "";
						String curSubCode = "";
						boolean existFlag = false;
						boolean xsFlag = false;
						Vector scoreInfoVec1=new Vector();
						if(scoreInfoVec!=null && scoreInfoVec.size()>0){
							for(int k=0; k<scoreInfoVec.size(); k+=15){
								String xh=MyTools.StrFiltr(scoreInfoVec.get(k));
								String xnxqm=MyTools.StrFiltr(scoreInfoVec.get(k+4));
								if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i)).equalsIgnoreCase(xnxqm)){
									for(int dd=0;dd<15;dd++){
										scoreInfoVec1.add(scoreInfoVec.get(k+dd));
									}
									xsFlag=true;
								}
								if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i+1)).equalsIgnoreCase(xnxqm)){
									for(int dd=0;dd<15;dd++){
										scoreInfoVec1.add(scoreInfoVec.get(k+dd));
									}
									xsFlag=true;
									
								}
							}
						}
						
					if(xsFlag){
						for(int j=0; j<subVec.size(); j+=2){
							curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
							existFlag = false;
								//开始
								for(int k=0; k<scoreInfoVec1.size(); k+=15){
									String firstScore="";
									String secondScore="";
									if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec1.get(k+2)))){
										boolean first = false;
										boolean second = false;
										boolean firstFlag = false;
										boolean secondFlag = false;
										boolean youwuFlag = true;
										//用来判断上学期是否有课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										
										String zp1=MyTools.StrFiltr(scoreInfoVec1.get(k+8));
										String PS= MyTools.StrFiltr(scoreInfoVec1.get(k+5));
										String QZ= MyTools.StrFiltr(scoreInfoVec1.get(k+6));
										String QM= MyTools.StrFiltr(scoreInfoVec1.get(k+7));
										String psbl= MyTools.StrFiltr(scoreInfoVec1.get(k+10));
										String qzbl= MyTools.StrFiltr(scoreInfoVec1.get(k+11));
										String qmbl= MyTools.StrFiltr(scoreInfoVec1.get(k+12));
										String xsZt=MyTools.StrFiltr(scoreInfoVec1.get(k+13));
										String zpBl=MyTools.StrFiltr(scoreInfoVec1.get(k+14));
									if("10".equalsIgnoreCase(xsZt)){
											if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
												if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
													zp1="";
												}
											}else {
												if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
													zp1="";
											  }
											}
										}
										//上学期和下学期都有
										if(first==true && second==true){
											String zp2=MyTools.StrFiltr(scoreInfoVec1.get(k+23));
											String PS2= MyTools.StrFiltr(scoreInfoVec1.get(k+20));
											String QZ2= MyTools.StrFiltr(scoreInfoVec1.get(k+21));
											String QM2= MyTools.StrFiltr(scoreInfoVec1.get(k+22));
										
											String psbl2= MyTools.StrFiltr(scoreInfoVec1.get(k+25));
											String qzbl2= MyTools.StrFiltr(scoreInfoVec1.get(k+26));
											String qmbl2= MyTools.StrFiltr(scoreInfoVec1.get(k+27));
											String xsZt2=MyTools.StrFiltr(scoreInfoVec1.get(k+28));
											String zpBl2=MyTools.StrFiltr(scoreInfoVec1.get(k+29));
											if("10".equalsIgnoreCase(xsZt2)){
												if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
													if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
														zp2="";
													}
												}else {
													if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
														zp2="";
												  }
												}
											}
											firstScore=zp1;
											secondScore=zp2;
											scoreVec.add(firstScore);
											scoreVec.add(secondScore);
										//上学期
										}else if(first==true && second==false){
											 firstScore=zp1;
											 scoreVec.add(firstScore);
											 scoreVec.add("");
										//下学期
										}else{
											   secondScore=zp1;
											   scoreVec.add("");
											   scoreVec.add(secondScore);
										}
										existFlag = true;
										break;
									}
							}
								//没有该课程则增加成绩信息为空
									if(!existFlag){
										scoreVec.add("");
										scoreVec.add("");
									}
						}
					}else{
						for(int j=0;j<subVec.size()/2;j++){
							scoreVec.add("");
							scoreVec.add("");
						}
					}
				}
				//第一部分

					WritableSheet wsheet = wbook.createSheet(JX_XJH, 0);//工作表名称
					WritableFont fontStyle;
					WritableCellFormat contentStyle;
					WritableFont fontStyle_left;
					WritableCellFormat contentStyle_left;
					WritableFont fontStyle_right;
					WritableCellFormat contentStyle_right;
					WritableFont fontStyle_leftAndTopNo;
					WritableCellFormat fontStyle_leftAndTopNoStyle;
					Label content;
					
					for(int i=0;i<10;i++){
						wsheet.setColumnView(i,6);//设置列宽
					}
					for(int i=0;i<(subVec.size()/2+2);i++){
						wsheet.setRowView(i,560);//设置行高
					}
					wsheet.setRowView(1,600);//设置行高
				 //第四部分
					 fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					contentStyle.setWrap(true);
					wsheet.mergeCells(0, 0, 1, 1);
					content = new Label(0, 0,"", contentStyle);
				    wsheet.addCell(content);
					//年级 
					count=0;
					for(int i=2;i<10;i+=2){
						 wsheet.mergeCells(i, 0, (i+1), 0);
						 content = new Label(i, 0,nj[count++]+"年级", contentStyle);
						 wsheet.addCell(content);
						  content = new Label(i, 1,"第一\n学期", contentStyle);
						 wsheet.addCell(content);
						 content = new Label(i+1, 1,"第二\n学期", contentStyle);
						 wsheet.addCell(content);
						}
					//科目
					int kemu=(int)(subVec.size()/2)+2;
					for(int i=0;i<kemu;i++){
						wsheet.mergeCells(0, i, 1, i);
					}
					count=2;
					WritableFont fontStyle3;
					WritableCellFormat contentStyle3;
					fontStyle3 = new WritableFont(
							WritableFont.createFont("宋体"), 7, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle3 = new WritableCellFormat(fontStyle3);
					contentStyle3.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle3.setWrap(true);// 自动换行
					contentStyle3.setShrinkToFit(true);//字体大小自适应
					contentStyle3.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle3.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle3.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle3.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					
					WritableFont fontStyle4;
					WritableCellFormat contentStyle4;
					fontStyle4 = new WritableFont(
							WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle4 = new WritableCellFormat(fontStyle4);
					contentStyle4.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle4.setWrap(true);// 自动换行
					contentStyle4.setShrinkToFit(true);//字体大小自适应
					contentStyle4.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle4.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle4.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle4.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					for(int i=0;i<subVec.size();i+=2){			
						if(MyTools.StrFiltr(subVec.get(i+1)).length()>10){
							content = new Label(0, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle3);
							wsheet.addCell(content);
						}else if(MyTools.StrFiltr(subVec.get(i+1)).length()>5){
							content = new Label(0, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle4);
							wsheet.addCell(content);	
						}else{
							content = new Label(0, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle);
							wsheet.addCell(content);
							}			
					}
					
					WritableFont fontStyle_red1;
					WritableCellFormat contentStyle_red1;
					WritableFont fontStyle_red2;
					WritableCellFormat contentStyle_red2;
					
					fontStyle_red1 = new WritableFont(
							WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.RED);
					contentStyle_red1 = new WritableCellFormat(fontStyle_red1);
					contentStyle_red1.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle_red1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle_red1.setWrap(true);// 自动换行
					contentStyle_red1.setShrinkToFit(true);//字体大小自适应
					contentStyle_red1.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle_red1.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle_red1.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle_red1.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					
					fontStyle_red2 = new WritableFont(
							WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.RED);
					contentStyle_red2 = new WritableCellFormat(fontStyle_red2);
					contentStyle_red2.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle_red2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle_red2.setWrap(true);// 自动换行
					contentStyle_red2.setShrinkToFit(true);//字体大小自适应
					contentStyle_red2.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle_red2.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle_red2.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle_red2.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					
					//遍历课成绩
					count=0;
					String fs="";
					double fsz=0;
					for(int i=0;i<8;i+=2){
						for(int k=0;k<subVec.size()/2;k++){
							if(count<scoreVec.size()){
									fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(2+i, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
									}else
								 	content = new Label(2+i, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
								 	wsheet.addCell(content);	
								 	
								 	count+=1;
								 	fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(2+i+1, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
									}else
								 	content = new Label(2+i+1, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
								 	wsheet.addCell(content);	
								 	count+=1;
								}
						}
					}
			//写入文件
			wbook.write();
			wbook.close();
			os.close();
		} catch (FileNotFoundException e) {
			this.setMSG("导出前请先关闭相关EXCEL");
		} catch (WriteException e) {
			this.setMSG("文件生成失败");
		} catch (IOException e) {
			this.setMSG("文件生成失败");
		}
		
		PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
		//poCtrl1.setWriter(wb);
		poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
		poCtrl1.setJsFunction_AfterDocumentClosed("AfterDocumentClosed()");
		poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
		
		String captionName = title;
		//创建自定义菜单栏
		//poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
		//poCtrl1.addCustomToolButton("-", "", 0);
		poCtrl1.addCustomToolButton("打印", "print()", 6);
		poCtrl1.addCustomToolButton("下载", "download()", 3);
		poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
		//poCtrl1.addCustomToolButton("关闭", "closeWindow()", 4);
		poCtrl1.setMenubar(false);//隐藏菜单栏
		poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
		poCtrl1.setCaption(captionName);
		poCtrl1.setFileTitle(captionName);//设置另存为窗口默认文件名
		//打开文件
		poCtrl1.webOpen(savePath, OpenModeType.xlsNormalEdit, "");
		poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
		return filePath;
	 }
	//批量预览
	public String stuXqkMuchView(HttpServletRequest request,String XBDM,String NJDM,String SSZY,String BJBH) throws SQLException, IOException{	
		DBSource db = new DBSource(request); //数据库对象
		String sql="";
		String filePath="";
		String XM="成绩信息";
		String title="";
		Vector XhAndBj = new Vector();//学号和班级
		Vector scoreInfoVec = new Vector();//查询学生成绩
		Vector xyspVec = new Vector();//查询学业水平分数
		Vector BJJVec=new Vector();//班级
		Vector BJVec=new Vector();
		Vector XSVec=new Vector();//学生
		Vector subWhVec=new Vector();
		Vector subZyVec=new Vector();
		String XSXH="";//学生学号
		title =XM;
		title=new String (title.getBytes("ISO-8859-1"),"UTF-8");
		filePath = request.getSession().getServletContext().getRealPath("/")+"form/registerScoreManage/";
		filePath = filePath.replaceAll("\\\\", "/");
		
		//创建
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName="temp"+".xls";
		//filePath += title+"设备.xls";
		filePath+= "temp"+".xls";
		
		try {
			//输出流
			OutputStream os = new FileOutputStream(filePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
		
		com.zhuozhengsoft.pageoffice.excelwriter.Workbook wb = new com.zhuozhengsoft.pageoffice.excelwriter.Workbook();
		
		String[] bjList=BJBH.split(",",-1);
		if("all".equalsIgnoreCase(bjList[0])){
			sql ="select distinct 行政班名称,行政班代码  from V_学校班级数据子类 a where 1=1 ";
			if(!"all".equalsIgnoreCase(XBDM)){
				sql += " and 系部代码 in ('" + XBDM.replaceAll(",", "','") + "')";
			}
			if(!"all".equalsIgnoreCase(NJDM)){
				sql += " and 年级代码 in ('" + NJDM.replaceAll(",", "','") + "')";
			}
			if(!"all".equalsIgnoreCase(SSZY)){
				sql += " and 专业代码 in ('" + SSZY.replaceAll(",", "','") + "')";
			}
			sql += "order by 行政班代码";
		BJJVec = db.GetContextVector(sql);	
		if(BJJVec!=null && BJJVec.size()>0){
			for(int i=0; i<BJJVec.size(); i+=2){
				BJVec.add(BJJVec.get(i+1));	
				}
			}
		}else{
			for(int i=0; i<bjList.length; i++){
				BJVec.add(bjList[i]);
			}
			
		}	
		
		
		for(int i=0;i<BJVec.size();i++){//循环班级,
			sql="select 学号 from V_学生基本数据子类 where 行政班代码='"+BJVec.get(i)+"'";
			XSVec=db.GetContextVector(sql);	
			
			if(XSVec!=null&&XSVec.size()>0){
				for(int j=0;j<XSVec.size();j++){
					XhAndBj.add(MyTools.StrFiltr(XSVec.get(j)));
					XhAndBj.add(MyTools.StrFiltr(BJVec.get(i)));
					XSXH+=MyTools.StrFiltr(XSVec.get(j))+",";
				}
			}
		}
			XSXH=XSXH.substring(0, XSXH.length()-1);
			XSXH=XSXH.replaceAll(",","','");
			
			//获取所有文化课的课程
			sql="with tempClassInfo as ("+
				"select distinct b.课程代码,b.课程名称 ,b.行政班代码,a.学号 "+	
				"from V_成绩管理_学生成绩信息表 a  "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +			
				"left join V_学生基本数据子类 d on d.学号=a.学号  "+						
				"where   a.学号 in ('" +XSXH + "')	) "+			
				"select distinct b.课程代码,b.课程名称,d.学号   "+
				"from  V_规则管理_授课计划主表 a   "+
				"left join V_规则管理_授课计划明细表 b on a.授课计划主表编号=b.授课计划主表编号    "+
				"left join V_课程数据子类 c on c.课程号=b.课程代码   "+
				"left join tempClassInfo d on d.课程代码=b.课程代码 and d.行政班代码=a.行政班代码 "+
				"where   d.学号 in ('" +XSXH + "')  " +
				"order by d.学号,b.课程代码 ";
				subWhVec = db.GetContextVector(sql);
		
				
			//所有学生成绩
					sql = "with tempClassInfo as ("+
						 "select b.学年, a.学号,b.课程代码," +
						 "(case when a.补考='-1' then '免修' when a.补考='-2' then '作弊' when a.补考='-3' then '取消资格' when a.补考='-4' then '缺考'  when a.补考='-5' then '缓考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格'  when a.补考='-17' then '免考' else a.补考 end ) as 补考  " +
						 "from V_成绩管理_学生补考成绩信息表  a "+	
						 "left join  V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+	
						 "where a.学号 in ('" +XSXH + "') )"+
						"select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
						"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
						"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
						"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
						"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
						"cc.补考 ,f.平时比例,f.期中比例,f.期末比例,e.学生状态,f.总评比例选项 "+
						"from V_成绩管理_学生成绩信息表 a " +
						"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
						"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
						"left join V_规则管理_学年学期表 d on d.学年学期编码=c.学年学期编码 " +
						"left join V_学生基本数据子类 e on e.学号=a.学号 "+
						"left join V_成绩管理_登分设置信息表 f on a.相关编号=f.相关编号 "+
						"left join  V_课程数据子类   g on b.课程代码=g.课程号 "+
						"left join tempClassInfo cc on a.学号=cc.学号 and cc.课程代码=b.课程代码  and SUBSTRING(c.学年学期编码,0,5)=cc.学年  "+
						"where  a.学号 in ('" +XSXH + "') "+
						"order by a.学号,b.课程代码,c.学年学期编码";
					scoreInfoVec = db.GetContextVector(sql);
					

					int count=0;
					if(XhAndBj!=null&&XhAndBj.size()>0){
						for(int i=0;i<XhAndBj.size();i+=2){
							exportScoreInfo_2(wbook,MyTools.StrFiltr(XhAndBj.get(i)),MyTools.StrFiltr(XhAndBj.get(i+1)) ,count++,"",scoreInfoVec,subWhVec);
							}
					}
				//写入文件
				wbook.write();
				wbook.close();
				os.close();
			} catch (FileNotFoundException e) {
				this.setMSG("导出前请先关闭相关EXCEL");
			} catch (WriteException e) {
				this.setMSG("文件生成失败");
			} catch (IOException e) {
				this.setMSG("文件生成失败");
			}
			
			PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
			//poCtrl1.setWriter(wb);
			poCtrl1.setJsFunction_AfterDocumentOpened("AfterDocumentOpened()");
			poCtrl1.setJsFunction_AfterDocumentClosed("AfterDocumentClosed()");
			poCtrl1.setServerPage(request.getContextPath()+"/poserver.do"); //此行必须
			
			String captionName = title;
			//创建自定义菜单栏
			//poCtrl1.addCustomToolButton("保存", "exportExcel()", 1);
			//poCtrl1.addCustomToolButton("-", "", 0);
			poCtrl1.addCustomToolButton("打印", "print()", 6);
			poCtrl1.addCustomToolButton("下载", "download()", 3);
			poCtrl1.addCustomToolButton("全屏切换", "SetFullScreen()", 4);
			//poCtrl1.addCustomToolButton("关闭", "closeWindow()", 4);
			poCtrl1.setMenubar(false);//隐藏菜单栏
			poCtrl1.setOfficeToolbars(false);//隐藏Office工具栏
			/*poCtrl1.setCaption(captionName);
			poCtrl1.setFileTitle(captionName);//设置另存为窗口默认文件名
			*///打开文件
			poCtrl1.webOpen(fileName, OpenModeType.xlsNormalEdit, "");
			poCtrl1.setTagId("PageOfficeCtrl1"); //此行必须
			return filePath;
		}

	
	//批量导出
		public String exportScoreInfo2(String XBDM,String NJDM,String SSZY,String BJBH,String exportType,String JX_XJH,String XM) throws SQLException, IOException, WriteException{
			String sql="";
			Vector BJJVec=new Vector();//班级
			Vector BJVec=new Vector();
			Vector XSVec=new Vector();//学生
			Vector XhAndBj = new Vector();//学号和班级
			Vector scoreInfoVec = new Vector();//查询学生成绩
			Vector xyspVec = new Vector();//查询学业水平分数
			Vector subWhVec = new Vector();//所有文化课程
			Vector subZyVec = new Vector();//所有专业课程
			String[] bjList=BJBH.split(",",-1);
			XM=new String (XM.getBytes("ISO-8859-1"),"UTF-8");
			String filePath = "";
			String XSXH="";//学生学号
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			int hour = c.get(Calendar.HOUR);
			int minute = c.get(Calendar.MINUTE);
			int secondTime = c.get(Calendar.SECOND);
			
			//创建
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			filePath += "/" +"成绩信息"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls"; 
			//输出流
				OutputStream os = new FileOutputStream(filePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
				
			if("classKcb".equalsIgnoreCase(exportType)){
				XSXH=JX_XJH+",";
				XhAndBj.add(JX_XJH);
				XhAndBj.add(BJBH);
			}else{
				if("all".equalsIgnoreCase(bjList[0])){
					sql ="select distinct 行政班名称,行政班代码  from V_学校班级数据子类 a where 1=1 ";
					if(!"all".equalsIgnoreCase(XBDM)){
						sql += " and 系部代码 in ('" + XBDM.replaceAll(",", "','") + "')";
					}
					if(!"all".equalsIgnoreCase(NJDM)){
						sql += " and 年级代码 in ('" + NJDM.replaceAll(",", "','") + "')";
					}
					if(!"all".equalsIgnoreCase(SSZY)){
						sql += " and 专业代码 in ('" + SSZY.replaceAll(",", "','") + "')";
					}
					sql += "order by 行政班代码";
				BJJVec = db.GetContextVector(sql);	
				if(BJJVec!=null && BJJVec.size()>0){
					for(int i=0; i<BJJVec.size(); i+=2){
						BJVec.add(BJJVec.get(i+1));	
						}
					}
				}else{
					for(int i=0; i<bjList.length; i++){
						BJVec.add(bjList[i]);
					}
					
				}	
				
				for(int i=0;i<BJVec.size();i++){//循环班级,
					sql="select 学号 from V_学生基本数据子类 where 行政班代码='"+BJVec.get(i)+"'";
					XSVec=db.GetContextVector(sql);	
					if(XSVec!=null&&XSVec.size()>0){
						for(int j=0;j<XSVec.size();j++){
							XhAndBj.add(MyTools.StrFiltr(XSVec.get(j)));
							XhAndBj.add(MyTools.StrFiltr(BJVec.get(i)));
							XSXH+=MyTools.StrFiltr(XSVec.get(j))+",";
						}
					}
				}
			}
			
			
			
			XSXH=XSXH.substring(0, XSXH.length()-1);
			XSXH=XSXH.replaceAll(",","','");
			
			   //获取所有课的课程
				sql="with tempClassInfo as ("+
					"select distinct b.课程代码,b.课程名称 ,b.行政班代码,a.学号 "+	
					"from V_成绩管理_学生成绩信息表 a  "+
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
					"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +			
					"left join V_学生基本数据子类 d on d.学号=a.学号  "+						
					"where   a.学号 in ('" +XSXH + "')	) "+			
					"select distinct b.课程代码,b.课程名称,d.学号   "+
					"from  V_规则管理_授课计划主表 a   "+
					"left join V_规则管理_授课计划明细表 b on a.授课计划主表编号=b.授课计划主表编号    "+
					"left join V_课程数据子类 c on c.课程号=b.课程代码   "+
					"left join tempClassInfo d on d.课程代码=b.课程代码 and d.行政班代码=a.行政班代码 "+
					"where    d.学号 in ('" +XSXH + "')  " +
					"order by d.学号,b.课程代码 ";
					subWhVec = db.GetContextVector(sql);
			
			
			
			//所有学生成绩
			sql = "with tempClassInfo as ("+
				 "select b.学年, a.学号,b.课程代码," +
				 "(case when a.补考='-1' then '免修' when a.补考='-2' then '作弊' when a.补考='-3' then '取消资格' when a.补考='-4' then '缺考'  when a.补考='-5' then '缓考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格'  when a.补考='-17' then '免考' else a.补考 end ) as 补考  " +
				 "from V_成绩管理_学生补考成绩信息表  a "+	
				 "left join  V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+	
				 "where a.学号 in ('" +XSXH + "') )"+
				"select a.学号,a.姓名,b.课程代码,b.课程名称,c.学年学期编码," +
				"(case when a.平时='-1' then '免修' when a.平时='-2' then '作弊' when  a.平时='-3' then '取消资格' when a.平时='-4' then '缺考'  when a.平时='-5' then '缓考'  when a.平时='-17' then '免考' else a.平时 end ) as 平时," +
				"(case when a.期中='-1' then '免修' when a.期中='-2' then '作弊' when  a.期中='-3' then '取消资格' when a.期中='-4' then '缺考'  when a.期中='-5' then '缓考'  when a.期中='-17' then '免考' else a.期中 end ) as 期中," +
				"(case when a.期末='-1' then '免修' when a.期末='-2' then '作弊' when  a.期末='-3' then '取消资格' when a.期末='-4' then '缺考'  when a.期末='-5' then '缓考'  when a.期末='-17' then '免考' else a.期末 end ) as 期末," +
				"(case when 总评='-1' then '免修' when 总评='-2' then '作弊' when  总评='-3' then '取消资格' when 总评='-4' then '缺考'  when 总评='-5' then '缓考'  when 总评='-17' then '免考' else 总评 end ) as 总评," +
				"cc.补考 ,f.平时比例,f.期中比例,f.期末比例,e.学生状态,f.总评比例选项 "+
				"from V_成绩管理_学生成绩信息表 a " +
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on c.科目编号=b.科目编号 "+
				"left join V_规则管理_学年学期表 d on d.学年学期编码=c.学年学期编码 " +
				"left join V_学生基本数据子类 e on e.学号=a.学号 "+
				"left join V_成绩管理_登分设置信息表 f on a.相关编号=f.相关编号 "+
				"left join  V_课程数据子类   g on b.课程代码=g.课程号 "+
				"left join tempClassInfo cc on a.学号=cc.学号 and cc.课程代码=b.课程代码  and SUBSTRING(c.学年学期编码,0,5)=cc.学年  "+
				"where  a.学号 in ('" +XSXH + "') "+
				"order by a.学号,b.课程代码,c.学年学期编码";
			scoreInfoVec = db.GetContextVector(sql);
			
			int count=0;
			if(XhAndBj!=null&&XhAndBj.size()>0){
				for(int i=0;i<XhAndBj.size();i+=2){
					exportScoreInfo_2(wbook,MyTools.StrFiltr(XhAndBj.get(i)),MyTools.StrFiltr(XhAndBj.get(i+1)) ,count++,"",scoreInfoVec,subWhVec);
				}
			}
				wbook.write();
				wbook.close();
				os.close();
				this.setMSG("文件生成成功");
			return filePath;
		}
		public void exportScoreInfo_2(WritableWorkbook wbook,String JX_XJH,String BJDM,int n,String XM,Vector scoreInfoVec,Vector subWhVec) throws SQLException, IOException{
			String sql = "";
			String filePath = "";                                      
			String title = "";
			Vector pyVec=new Vector();
			Vector pySave=new Vector();
			Vector xqzbVec = new Vector();//学期占比设置的信息
			Vector Vec1=new Vector();
			Vector Vec2=new Vector();
			DBSource db = new DBSource(request); //数据库对象
			Vector stuInfo=new Vector();//学生基本信息
			Vector Vec3=new Vector();//学生基本信息
			int count=0;
			String nj[] = {"一","二","三","四"};
			String xq[] = {"一","二"};
			Vector subVec = new Vector();//学期占比设置的信息
			Vector xnVec = new Vector();//学期占比设置的信息
			Vector firstVec = new Vector();//查询上学期课程
			Vector secondVec = new Vector();//查询下学期课程
			Vector scoreVec = new Vector();//存放分数
			String firstSem = "";//上学期
			String secondSem = "";//下学期
			int type = 0;
			int whkType=0;
			int zykType=0;
			String firstSemPercent = "";
			String secondSemPercent = "";
			DecimalFormat f = new DecimalFormat("#");  
			f.setRoundingMode(RoundingMode.HALF_UP); 
			//判断计算方式		
			sql = "select 系部代码,学期一,学期二,计算方式 from V_成绩管理_系部学年总评设置表 where 系部代码=(select 系部代码 from V_学校班级数据子类 where 行政班代码='"+BJDM+"')";
		    xqzbVec= db.GetContextVector(sql);
		    if(xqzbVec!=null && xqzbVec.size()>0){
			if("2".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(3)))){
				type=2;//跨学年2
				}else{
					type=1;//同学年有系部
				}
			}else{
				xqzbVec.add("");
				xqzbVec.add("40");
				xqzbVec.add("60");
				xqzbVec.add("1");
				type=0;//同学年无系部
			}		
			String xn_1="20"+MyTools.fixSql(BJDM).substring(0,2);
			String xn_2=MyTools.parseString(MyTools.StringToInt(xn_1)+1);
			String xn_3=MyTools.parseString(MyTools.StringToInt(xn_1)+2);
			String xn_4=MyTools.parseString(MyTools.StringToInt(xn_1)+3);
			if(type == 2){//跨学年
				firstSem = xn_1+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_1)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem = xn_2+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_2)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem = xn_3+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_3)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem = xn_4+ "201";
				secondSem = MyTools.parseString((MyTools.StringToInt(xn_4)+1))+"101";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
			}else{//同学年
				firstSem =xn_1 + "101";
				secondSem =xn_1+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem =xn_2 + "101";
				secondSem =xn_2+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem =xn_3 + "101";
				secondSem =xn_3+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
				
				firstSem =xn_4 + "101";
				secondSem =xn_4+ "201";
				xnVec.add(firstSem);
				xnVec.add(secondSem);
			}
			
			if(subWhVec!=null&&subWhVec.size()>0){
					for(int i=0;i<subWhVec.size();i+=3){
						if(MyTools.StrFiltr(subWhVec.get(i+2)).equalsIgnoreCase(JX_XJH)){
							subVec.add(MyTools.StrFiltr(subWhVec.get(i)));
							subVec.add(MyTools.StrFiltr(subWhVec.get(i+1)));
						}
					}
					
				}else{
					for(int i=0;i<14;i++){
						subVec.add("");
					}
				}
				int subVecSize=(int)(subVec.size()/2);
				if(subVecSize<5){
						for(int j=0;j<(5-subVecSize);j++){
							subVec.add("");
							subVec.add("");
							
						}
					}
				
					for(int i=0;i<xnVec.size();i+=2){			
					
					sql = "select a.学号,b.课程代码 "+
							"from V_成绩管理_学生成绩信息表 a  "+
							"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
							"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
							"left join V_学生基本数据子类 d on d.学号=a.学号 "+
							"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
							"where c.学年学期编码='" +  xnVec.get(i) + "' " +
							"and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
							"order by b.课程代码";
						firstVec = db.GetContextVector(sql);
				
						
						sql = "select a.学号,b.课程代码 "+
								"from V_成绩管理_学生成绩信息表 a  "+
								"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
								"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
								"left join V_学生基本数据子类 d on d.学号=a.学号 "+
								"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
								"where c.学年学期编码='" +  xnVec.get(i+1) + "' " +
								"and a.学号='"+MyTools.fixSql(JX_XJH)+"' " +
								"order by b.课程代码";	
						secondVec = db.GetContextVector(sql);
						
		
						String curStuCode = "";
						String curSubCode = "";
						boolean existFlag = false;
						boolean xsFlag = false;
						Vector scoreInfoVec1=new Vector();
						if(scoreInfoVec!=null && scoreInfoVec.size()>0){
							for(int k=0; k<scoreInfoVec.size(); k+=15){
								String xh=MyTools.StrFiltr(scoreInfoVec.get(k));
								String xnxqm=MyTools.StrFiltr(scoreInfoVec.get(k+4));
								if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i)).equalsIgnoreCase(xnxqm)){
									for(int dd=0;dd<15;dd++){
										scoreInfoVec1.add(scoreInfoVec.get(k+dd));
									}
									xsFlag=true;
								}
								if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i+1)).equalsIgnoreCase(xnxqm)){
									for(int dd=0;dd<15;dd++){
										scoreInfoVec1.add(scoreInfoVec.get(k+dd));
									}
									xsFlag=true;
									
								}
							}
						}
					if(xsFlag){
						for(int j=0; j<subVec.size(); j+=2){
							curSubCode = MyTools.StrFiltr(subVec.get(j));//课程代码
							existFlag = false;
									//开始
								for(int k=0; k<scoreInfoVec1.size(); k+=15){
									String tempXnzp = "";
									String firstScore="";
									String secondScore="";
									if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(scoreInfoVec1.get(k+2)))){
										boolean first = false;
										boolean second = false;
										boolean firstFlag = false;
										boolean secondFlag = false;
										boolean youwuFlag = true;
										//用来判断上学期是否有课程
										if(firstVec!=null && firstVec.size()>0){
											for(int s=0; s<firstVec.size(); s+=2){
												if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(firstVec.get(s+1)))){
													first = true;
													break;
												}
											}
										}
										//用来判断下学期是否有课程
										if(secondVec!=null&&secondVec.size()>0){
											for(int x=0; x<secondVec.size();x+=2){
												if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(secondVec.get(x+1)))){
													second=true;
													break;
												} 
											 }
										}
										
										String zp1=MyTools.StrFiltr(scoreInfoVec1.get(k+8));
										String PS= MyTools.StrFiltr(scoreInfoVec1.get(k+5));
										String QZ= MyTools.StrFiltr(scoreInfoVec1.get(k+6));
										String QM= MyTools.StrFiltr(scoreInfoVec1.get(k+7));
										String psbl= MyTools.StrFiltr(scoreInfoVec1.get(k+10));
										String qzbl= MyTools.StrFiltr(scoreInfoVec1.get(k+11));
										String qmbl= MyTools.StrFiltr(scoreInfoVec1.get(k+12));
										String xsZt=MyTools.StrFiltr(scoreInfoVec1.get(k+13));
										String zpBl=MyTools.StrFiltr(scoreInfoVec1.get(k+14));
									if("10".equalsIgnoreCase(xsZt)){
											if("2".equalsIgnoreCase(zpBl)){//总评比率选项为二
												if("".equalsIgnoreCase(PS)||("".equalsIgnoreCase(QM))){
													zp1="";
												}
											}else {
												if((!"".equalsIgnoreCase(psbl)&&("".equalsIgnoreCase(PS)))||(!"".equalsIgnoreCase(qzbl)&&("".equalsIgnoreCase(QZ))) ||(!"".equalsIgnoreCase(qmbl)&&("".equalsIgnoreCase(QM)))){
													zp1="";
											  }
											}
										}
										//上学期和下学期都有
										if(first==true && second==true){
											String zp2=MyTools.StrFiltr(scoreInfoVec1.get(k+23));
											String PS2= MyTools.StrFiltr(scoreInfoVec1.get(k+20));
											String QZ2= MyTools.StrFiltr(scoreInfoVec1.get(k+21));
											String QM2= MyTools.StrFiltr(scoreInfoVec1.get(k+22));
										
											String psbl2= MyTools.StrFiltr(scoreInfoVec1.get(k+25));
											String qzbl2= MyTools.StrFiltr(scoreInfoVec1.get(k+26));
											String qmbl2= MyTools.StrFiltr(scoreInfoVec1.get(k+27));
											String xsZt2=MyTools.StrFiltr(scoreInfoVec1.get(k+28));
											String zpBl2=MyTools.StrFiltr(scoreInfoVec1.get(k+29));
											if("10".equalsIgnoreCase(xsZt2)){
												if("2".equalsIgnoreCase(zpBl2)){//总评比率选项为二
													if("".equalsIgnoreCase(PS2)||("".equalsIgnoreCase(QM2))){
														zp2="";
													}
												}else {
													if((!"".equalsIgnoreCase(psbl2)&&("".equalsIgnoreCase(PS2)))||(!"".equalsIgnoreCase(qzbl2)&&("".equalsIgnoreCase(QZ2))) ||(!"".equalsIgnoreCase(qmbl2)&&("".equalsIgnoreCase(QM2)))){
														zp2="";
												  }
												}
											}
											firstScore=zp1;
											secondScore=zp2;
											scoreVec.add(firstScore);
											scoreVec.add(secondScore);
										//上学期
										}else if(first==true && second==false){
											 firstScore=zp1;
											 scoreVec.add(firstScore);
											 scoreVec.add("");
											//下学期
										}else{
											   secondScore=zp1;
											   scoreVec.add("");
											   scoreVec.add(secondScore);
										}
										existFlag = true;
										break;
									}
							}
								//没有该课程则增加成绩信息为空
									if(!existFlag){
										scoreVec.add("");
										scoreVec.add("");
									}
						}
					}else{
						for(int j=0;j<subVec.size()/2;j++){
							scoreVec.add("");
							scoreVec.add("");
						}
					}
				}
			try {
			//第一部分

				WritableSheet wsheet = wbook.createSheet(JX_XJH, n);//工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				
				WritableFont fontStyle_leftAndTopNo;
				WritableCellFormat fontStyle_leftAndTopNoStyle;
				WritableFont fontStyle_left;
				
				WritableFont fontStyleCell_9;
				WritableCellFormat fontStyleFont_9;
				
				WritableCellFormat contentStyle_left;
				WritableFont fontStyle_right;
				WritableCellFormat contentStyle_right;
				Label content;
				for(int i=0;i<10;i++){
					wsheet.setColumnView(i,6);//设置列宽
				}
				for(int i=0;i<(subVec.size()/2+2);i++){
					wsheet.setRowView(i,560);//设置行高
				}
				wsheet.setRowView(1,600);//设置行高
				 //第四部分
				 fontStyle = new WritableFont(
						WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				contentStyle.setWrap(true);
				wsheet.mergeCells(0, 0, 1, 1);
				content = new Label(0, 0,"", contentStyle);
			    wsheet.addCell(content);
				//年级 
				count=0;
				for(int i=2;i<10;i+=2){
					 wsheet.mergeCells(i, 0, (i+1), 0);
					 content = new Label(i, 0,nj[count++]+"年级", contentStyle);
					 wsheet.addCell(content);
					  content = new Label(i, 1,"第一\n学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(i+1, 1,"第二\n学期", contentStyle);
					 wsheet.addCell(content);
				}
				//科目
				int kemu=(int)(subVec.size()/2)+2;
				for(int i=0;i<kemu;i++){
					wsheet.mergeCells(0, i, 1, i);
				}
				count=2;
				WritableFont fontStyle3;
				WritableCellFormat contentStyle3;
				fontStyle3 = new WritableFont(
						WritableFont.createFont("宋体"), 7, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle3 = new WritableCellFormat(fontStyle3);
				contentStyle3.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle3.setWrap(true);// 自动换行
				contentStyle3.setShrinkToFit(true);//字体大小自适应
				contentStyle3.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle3.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle3.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle3.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				
				WritableFont fontStyle4;
				WritableCellFormat contentStyle4;
				fontStyle4 = new WritableFont(
						WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.BLACK);
				contentStyle4 = new WritableCellFormat(fontStyle4);
				contentStyle4.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle4.setWrap(true);// 自动换行
				contentStyle4.setShrinkToFit(true);//字体大小自适应
				contentStyle4.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle4.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle4.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle4.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				for(int i=0;i<subVec.size();i+=2){			
					if(MyTools.StrFiltr(subVec.get(i+1)).length()>10){
						content = new Label(0, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle3);
						wsheet.addCell(content);
					}else if(MyTools.StrFiltr(subVec.get(i+1)).length()>5){
						content = new Label(0, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle4);
						wsheet.addCell(content);	
					}else{
						content = new Label(0, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle);
						wsheet.addCell(content);
						}			
				}
				
				
				WritableFont fontStyle_red1;
				WritableCellFormat contentStyle_red1;
				WritableFont fontStyle_red2;
				WritableCellFormat contentStyle_red2;
							
				fontStyle_red1 = new WritableFont(
						WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.RED);
				contentStyle_red1 = new WritableCellFormat(fontStyle_red1);
				contentStyle_red1.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle_red1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle_red1.setWrap(true);// 自动换行
				contentStyle_red1.setShrinkToFit(true);//字体大小自适应
				contentStyle_red1.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle_red1.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle_red1.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle_red1.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				
				fontStyle_red2 = new WritableFont(
						WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD,
						false, jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.RED);
				contentStyle_red2 = new WritableCellFormat(fontStyle_red2);
				contentStyle_red2.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
				contentStyle_red2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle_red2.setWrap(true);// 自动换行
				contentStyle_red2.setShrinkToFit(true);//字体大小自适应
				contentStyle_red2.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
				contentStyle_red2.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
				contentStyle_red2.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				contentStyle_red2.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				
				
				String fs="";
				double fsz=0;
				
				//遍历课成绩
				count=0;
				for(int i=0;i<8;i+=2){
					for(int k=0;k<subVec.size()/2;k++){
						if(count<scoreVec.size()){
								fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
								fsz=MyTools.StringToDouble(fs);
								if(fsz<60){
									content = new Label(2+i, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
								}else
							 	content = new Label(2+i, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
							 	wsheet.addCell(content);	
							 	
							 	count+=1;
							 	fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
								fsz=MyTools.StringToDouble(fs);
								if(fsz<60){
									content = new Label(2+i+1, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
								}else
							 	content = new Label(2+i+1, (2+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
							 	wsheet.addCell(content);	
							 	count+=1;
							}
					}
				}
			
			   
			} catch (RowsExceededException e1) {
			} catch (WriteException e1) {
				this.setMSG("文件生成失败");
			}
			
			
		}
	/** 
     * 修改单个文件名称 
     * @param sPath 被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
	 * @throws UnsupportedEncodingException 
     */  
	   public String renameFile(String sPath) throws UnsupportedEncodingException {
		   Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			int date = c.get(Calendar.DATE);
			int hour = c.get(Calendar.HOUR);
			int minute = c.get(Calendar.MINUTE);
			int secondTime = c.get(Calendar.SECOND);
			String viewFilePath1="";
		    String viewFilePath2="";
	        File file =new File(sPath);
	       if(file.isFile()&&file.exists()){
				 viewFilePath1=new String(sPath.substring(0,sPath.lastIndexOf("/")+1));
				 if("".equalsIgnoreCase(MyTools.StrFiltr(this.getJX_XM()))){
					 viewFilePath2=viewFilePath1+"成绩信息"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls"; 
				 }else{
					 viewFilePath2=viewFilePath1+URLDecoder.decode(URLDecoder.decode(StringEscapeUtils.escapeJava(MyTools.StrFiltr(this.getJX_XM())), "utf-8"), "utf-8")+"成绩信息"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls"; 
				 }
				
				File file2=new File(viewFilePath2);
				file.renameTo(file2);
				this.setMSG("修改成功");
			} 
			 return viewFilePath2;
	    }
	

	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getJX_XM() {
		return JX_XM;
	}

	public void setJX_XM(String jX_XM) {
		JX_XM = jX_XM;
	}

	public String getJX_YWXM() {
		return JX_YWXM;
	}

	public void setJX_YWXM(String jX_YWXM) {
		JX_YWXM = jX_YWXM;
	}

	public String getJX_XZBDM() {
		return JX_XZBDM;
	}

	public void setJX_XZBDM(String jX_XZBDM) {
		JX_XZBDM = jX_XZBDM;
	}

	public String getJX_BNXH() {
		return JX_BNXH;
	}

	public void setJX_BNXH(String jX_BNXH) {
		JX_BNXH = jX_BNXH;
	}

	public String getJX_XMPY() {
		return JX_XMPY;
	}

	public void setJX_XMPY(String jX_XMPY) {
		JX_XMPY = jX_XMPY;
	}

	public String getJX_CYM() {
		return JX_CYM;
	}

	public void setJX_CYM(String jX_CYM) {
		JX_CYM = jX_CYM;
	}

	public String getJX_XBM() {
		return JX_XBM;
	}

	public void setJX_XBM(String jX_XBM) {
		JX_XBM = jX_XBM;
	}

	public String getJX_RXRQ() {
		return JX_RXRQ;
	}

	public void setJX_RXRQ(String jX_RXRQ) {
		JX_RXRQ = jX_RXRQ;
	}


	public String getJX_BRSJ() {
		return JX_BRSJ;
	}

	public void setJX_BRSJ(String jX_BRSJ) {
		JX_BRSJ = jX_BRSJ;
	}


	public String getJX_JTYB() {
		return JX_JTYB;
	}

	public void setJX_JTYB(String jX_JTYB) {
		JX_JTYB = jX_JTYB;
	}


	public String getJX_SFBSHJ() {
		return JX_SFBSHJ;
	}

	public void setJX_SFBSHJ(String jX_SFBSHJ) {
		JX_SFBSHJ = jX_SFBSHJ;
	}


	public String getJX_BYLXRQ() {
		return JX_BYLXRQ;
	}

	public void setJX_BYLXRQ(String jX_BYLXRQ) {
		JX_BYLXRQ = jX_BYLXRQ;
	}

	public String getJX_BYNF() {
		return JX_BYNF;
	}

	public void setJX_BYNF(String jX_BYNF) {
		JX_BYNF = jX_BYNF;
	}

	public String getJX_XSLBM() {
		return JX_XSLBM;
	}

	public void setJX_XSLBM(String jX_XSLBM) {
		JX_XSLBM = jX_XSLBM;
	}

	public String getJX_SFZJH() {
		return JX_SFZJH;
	}

	public void setJX_SFZJH(String jX_SFZJH) {
		JX_SFZJH = jX_SFZJH;
	}

	public String getJX_CSNY() {
		return JX_CSNY;
	}

	public void setJX_CSNY(String jX_CSNY) {
		JX_CSNY = jX_CSNY;
	}

	public String getJX_MZM() {
		return JX_MZM;
	}

	public void setJX_MZM(String jX_MZM) {
		JX_MZM = jX_MZM;
	}

	public String getJX_XXM() {
		return JX_XXM;
	}

	public void setJX_XXM(String jX_XXM) {
		JX_XXM = jX_XXM;
	}

	public String getJX_ZJXY() {
		return JX_ZJXY;
	}

	public void setJX_ZJXY(String jX_ZJXY) {
		JX_ZJXY = jX_ZJXY;
	}

	public String getJX_GATQM() {
		return JX_GATQM;
	}

	public void setJX_GATQM(String jX_GATQM) {
		JX_GATQM = jX_GATQM;
	}

	public String getJX_ZZMMM() {
		return JX_ZZMMM;
	}

	public void setJX_ZZMMM(String jX_ZZMMM) {
		JX_ZZMMM = jX_ZZMMM;
	}

	public String getJX_FQZZMMM() {
		return JX_FQZZMMM;
	}

	public void setJX_FQZZMMM(String jX_FQZZMMM) {
		JX_FQZZMMM = jX_FQZZMMM;
	}

	public String getJX_MQZZMMM() {
		return JX_MQZZMMM;
	}

	public void setJX_MQZZMMM(String jX_MQZZMMM) {
		JX_MQZZMMM = jX_MQZZMMM;
	}

	public String getJX_QTCYZZMMM() {
		return JX_QTCYZZMMM;
	}

	public void setJX_QTCYZZMMM(String jX_QTCYZZMMM) {
		JX_QTCYZZMMM = jX_QTCYZZMMM;
	}

	public String getJX_GBM() {
		return JX_GBM;
	}

	public void setJX_GBM(String jX_GBM) {
		JX_GBM = jX_GBM;
	}

	public String getJX_JKZKM() {
		return JX_JKZKM;
	}

	public void setJX_JKZKM(String jX_JKZKM) {
		JX_JKZKM = jX_JKZKM;
	}

	public String getJX_JGM() {
		return JX_JGM;
	}

	public void setJX_JGM(String jX_JGM) {
		JX_JGM = jX_JGM;
	}

	public String getJX_HYZK() {
		return JX_HYZK;
	}

	public void setJX_HYZK(String jX_HYZK) {
		JX_HYZK = jX_HYZK;
	}

	public String getJX_CSDZM() {
		return JX_CSDZM;
	}

	public void setJX_CSDZM(String jX_CSDZM) {
		JX_CSDZM = jX_CSDZM;
	}

	public String getJX_BYXX() {
		return JX_BYXX;
	}

	public void setJX_BYXX(String jX_BYXX) {
		JX_BYXX = jX_BYXX;
	}

	public String getJX_XZZZ() {
		return JX_XZZZ;
	}

	public void setJX_XZZZ(String jX_XZZZ) {
		JX_XZZZ = jX_XZZZ;
	}

	public String getJX_HKSZD() {
		return JX_HKSZD;
	}

	public void setJX_HKSZD(String jX_HKSZD) {
		JX_HKSZD = jX_HKSZD;
	}

	public String getJX_HKLBM() {
		return JX_HKLBM;
	}

	public void setJX_HKLBM(String jX_HKLBM) {
		JX_HKLBM = jX_HKLBM;
	}

	public String getJX_SSPCS() {
		return JX_SSPCS;
	}

	public void setJX_SSPCS(String jX_SSPCS) {
		JX_SSPCS = jX_SSPCS;
	}

	public String getJX_TXDZ() {
		return JX_TXDZ;
	}

	public void setJX_TXDZ(String jX_TXDZ) {
		JX_TXDZ = jX_TXDZ;
	}

	public String getJX_YZBM() {
		return JX_YZBM;
	}

	public void setJX_YZBM(String jX_YZBM) {
		JX_YZBM = jX_YZBM;
	}

	public String getJX_ZSLBM() {
		return JX_ZSLBM;
	}

	public void setJX_ZSLBM(String jX_ZSLBM) {
		JX_ZSLBM = jX_ZSLBM;
	}

	public String getJX_PZH() {
		return JX_PZH;
	}

	public void setJX_PZH(String jX_PZH) {
		JX_PZH = jX_PZH;
	}

	public String getJX_SFZJLXM() {
		return JX_SFZJLXM;
	}

	public void setJX_SFZJLXM(String jX_SFZJLXM) {
		JX_SFZJLXM = jX_SFZJLXM;
	}


	public String getJX_QTCYDH() {
		return JX_QTCYDH;
	}

	public void setJX_QTCYDH(String jX_QTCYDH) {
		JX_QTCYDH = jX_QTCYDH;
	}

	public String getJX_QTCYNL() {
		return JX_QTCYNL;
	}

	public void setJX_QTCYNL(String jX_QTCYNL) {
		JX_QTCYNL = jX_QTCYNL;
	}

	public String getJX_QTCYGZDW() {
		return JX_QTCYGZDW;
	}

	public void setJX_QTCYGZDW(String jX_QTCYGZDW) {
		JX_QTCYGZDW = jX_QTCYGZDW;
	}

	public String getJX_QTCYCW() {
		return JX_QTCYCW;
	}

	public void setJX_QTCYCW(String jX_QTCYCW) {
		JX_QTCYCW = jX_QTCYCW;
	}

	public String getJX_QTCYXM() {
		return JX_QTCYXM;
	}

	public void setJX_QTCYXM(String jX_QTCYXM) {
		JX_QTCYXM = jX_QTCYXM;
	}

	public String getJX_MQDH() {
		return JX_MQDH;
	}

	public void setJX_MQDH(String jX_MQDH) {
		JX_MQDH = jX_MQDH;
	}

	public String getJX_MQNL() {
		return JX_MQNL;
	}

	public void setJX_MQNL(String jX_MQNL) {
		JX_MQNL = jX_MQNL;
	}

	public String getJX_MQGZDW() {
		return JX_MQGZDW;
	}

	public void setJX_MQGZDW(String jX_MQGZDW) {
		JX_MQGZDW = jX_MQGZDW;
	}

	public String getJX_MQXM() {
		return JX_MQXM;
	}

	public void setJX_MQXM(String jX_MQXM) {
		JX_MQXM = jX_MQXM;
	}

	public String getJX_FQXM() {
		return JX_FQXM;
	}

	public void setJX_FQXM(String jX_FQXM) {
		JX_FQXM = jX_FQXM;
	}

	public String getJX_FQNL() {
		return JX_FQNL;
	}

	public void setJX_FQNL(String jX_FQNL) {
		JX_FQNL = jX_FQNL;
	}

	public String getJX_FQDH() {
		return JX_FQDH;
	}

	public void setJX_FQDH(String jX_FQDH) {
		JX_FQDH = jX_FQDH;
	}

	public String getJX_FQGZDW() {
		return JX_FQGZDW;
	}

	public void setJX_FQGZDW(String jX_FQGZDW) {
		JX_FQGZDW = jX_FQGZDW;
	}


	public String getJX_YDLXM() {
		return JX_YDLXM;
	}

	public void setJX_YDLXM(String jX_YDLXM) {
		JX_YDLXM = jX_YDLXM;
	}

	public String getJX_YDRQ() {
		return JX_YDRQ;
	}

	public void setJX_YDRQ(String jX_YDRQ) {
		JX_YDRQ = jX_YDRQ;
	}

	public String getJX_YBJDM() {
		return JX_YBJDM;
	}

	public void setJX_YBJDM(String jX_YBJDM) {
		JX_YBJDM = jX_YBJDM;
	}


	public String getJX_XBJDM() {
		return JX_XBJDM;
	}

	public void setJX_XBJDM(String jX_XBJDM) {
		JX_XBJDM = jX_XBJDM;
	}


	public String getJX_YDYY() {
		return JX_YDYY;
	}

	public void setJX_YDYY(String jX_YDYY) {
		JX_YDYY = jX_YDYY;
	}

	public String getMSG2() {
		return MSG2;
	}

	public void setMSG2(String mSG2) {
		MSG2 = mSG2;
	}

	public String getJX_ZPLJ() {
		return JX_ZPLJ;
	}

	public void setJX_ZPLJ(String jX_ZPLJ) {
		JX_ZPLJ = jX_ZPLJ;
	}

	public String getJX_LDRKZK() {
		return JX_LDRKZK;
	}

	public void setJX_LDRKZK(String jX_LDRKZK) {
		JX_LDRKZK = jX_LDRKZK;
	}

	public String getJX_YDLYXX() {
		return JX_YDLYXX;
	}

	public void setJX_YDLYXX(String jX_YDLYXX) {
		JX_YDLYXX = jX_YDLYXX;
	}

	public String getJX_YDQXXX() {
		return JX_YDQXXX;
	}

	public void setJX_YDQXXX(String jX_YDQXXX) {
		JX_YDQXXX = jX_YDQXXX;
	}

	public String getJX_YYXSH() {
		return JX_YYXSH;
	}

	public void setJX_YYXSH(String jX_YYXSH) {
		JX_YYXSH = jX_YYXSH;
	}

	public String getJX_YZYM() {
		return JX_YZYM;
	}

	public void setJX_YZYM(String jX_YZYM) {
		JX_YZYM = jX_YZYM;
	}

	public String getJX_YBH() {
		return JX_YBH;
	}

	public void setJX_YBH(String jX_YBH) {
		JX_YBH = jX_YBH;
	}

	public String getJX_YNJ() {
		return JX_YNJ;
	}

	public void setJX_YNJ(String jX_YNJ) {
		JX_YNJ = jX_YNJ;
	}

	public String getJX_XYXSH() {
		return JX_XYXSH;
	}

	public void setJX_XYXSH(String jX_XYXSH) {
		JX_XYXSH = jX_XYXSH;
	}

	public String getJX_XZYM() {
		return JX_XZYM;
	}

	public void setJX_XZYM(String jX_XZYM) {
		JX_XZYM = jX_XZYM;
	}

	public String getJX_XBH() {
		return JX_XBH;
	}

	public void setJX_XBH(String jX_XBH) {
		JX_XBH = jX_XBH;
	}

	public String getJX_XNJ() {
		return JX_XNJ;
	}

	public void setJX_XNJ(String jX_XNJ) {
		JX_XNJ = jX_XNJ;
	}

	public String getJX_BZ() {
		return JX_BZ;
	}

	public void setJX_BZ(String jX_BZ) {
		JX_BZ = jX_BZ;
	}

	public String getJX_XJH() {
		return JX_XJH;
	}

	public void setJX_XJH(String jX_XJH) {
		JX_XJH = jX_XJH;
	}

	public String getJX_XH() {
		return JX_XH;
	}

	public void setJX_XH(String jX_XH) {
		JX_XH = jX_XH;
	}

	public String getJX_XSZT() {
		return JX_XSZT;
	}

	public void setJX_XSZT(String jX_XSZT) {
		JX_XSZT = jX_XSZT;
	}
	
	
}