package com.pantech.devolop.baseInfoManage;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.jspsmart.upload.SmartFiles;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.excelwriter.Cell;
import com.zhuozhengsoft.pageoffice.excelwriter.SheetInsertType;
import com.zhuozhengsoft.pageoffice.excelwriter.XlBorderType;
import com.zhuozhengsoft.pageoffice.excelwriter.XlBorderWeight;
import com.zhuozhengsoft.pageoffice.excelwriter.XlHAlign;
import com.zhuozhengsoft.pageoffice.excelwriter.XlVAlign;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;



/*
@date 2016-07-15
@author Maowei
模块：M1.2年级学科设置
说明:
重要及特殊方法：
*/

/*
@date 2017-03-22
@author YangDa
模块：M1.2年级学科设置
说明:
重要及特殊方法：
修改：导出功能添加字段，并测试其他功能
*/

public class XsxxSetBean  {
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
	public XsxxSetBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request); // 数据库对象
		this.initialData(); // 初始化参数
	}
	
	/**
	 * 初始化变量
	 * @date:2016-07-15
	 * @author:Maowei
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
		if(!"ALL".equalsIgnoreCase(XSLX)){
			sql+=" and a.学生状态 in ('01')";
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
	 * 分页查询学生异动个人信息Info
	 * @param pageNum
	 * @param rows
	 * @return
	 * @throws SQLException
	 */
	public Vector queStudent_AbnInfo(int pageNum, int pagSize) throws SQLException {
		sql = "select a.编号,a.学号,a.姓名 ,异动日期  as 异动日期," +
			"a.异动类别码,a.异动说明,b.类别名称,a.异动来源学校码,a.原班号,a.异动去向学校码,a.现班号,a.异动说明 " +
			"from V_学籍异动数据子类 a "+
			"left join V_信息类别_类别操作 b on b.描述=a.异动类别码 and b.父类别代码='XSYDZTM' " +
			"where 学号='" + this.getJX_XH() + "' order by 异动日期 desc";
		
		vec = db.getConttexJONSArr(sql, pageNum, pagSize);
		return vec;
	}
	
	/**
	 * 分页查询学生异动信息
	 * @param pageNum
	 * @param rows
	 * @return
	 * @throws SQLException
	 */
	public Vector queStudentAbn(int pageNum, int pagSize) throws SQLException {
		sql = "select a.学籍号,a.学号,a.班内学号,b.系部代码,e.系部名称,a.行政班代码,b.行政班名称,b.年级代码,a.姓名,d.专业名称,d.专业代码,a.英文姓名,a.姓名拼音,a.曾用名,a.身份证件类型码,a.身份证件号,a.性别码,a.血型码,a.出生日期,a.出生地码,a.籍贯,a.民族码,a.婚姻状况码,a.信仰宗教码,a.港澳台侨外码,a.健康状况码,a.政治面貌码,a.户口所在地行政区划码,a.户口类别码, a.是否是流动人口, a.[国籍/ 地区码] as 国籍,a.电子信箱,a.照片,a.照片路径,a.常住地址,a.户籍地址,a.户籍邮编,a.家庭邮编,a.家长手机,a.是否本市户籍,a.学生状态,c.类别名称,a.家庭电话,a.奖惩,CONVERT(date,a.毕业离校日期) as 毕业离校日期,a.人员类型,a.学生类别,a.招生类别,CONVERT(nvarchar,a.入学日期,21) as 入学日期,a.毕业年份,a.拍照号 ,a.备注,a.所属派出所,a.毕业学校,a.父亲 ,a.父亲年龄, a.父亲政治面貌,a.父亲工作单位,a.父亲电话,a.母亲,a.母亲年龄,a.母亲政治面貌,a.母亲工作单位,a.母亲电话,a.其他成员称谓,a.其他成员姓名,a.其他成员年龄,a.其他成员政治面貌,a.其他成员工作单位,a.其他成员电话 from V_学生基本数据子类 a "+
			"left join dbo.V_学校班级数据子类 b on a.行政班代码=b.行政班代码 "+
			"left join dbo.V_信息类别_类别操作 c on a.学生状态=c.描述  and c.父类别代码='XSZTM' "+
			"left join V_专业基本信息数据子类 d on a.专业代码=d.专业代码 "+
			"left join V_基础信息_系部信息表 e on e.系部代码=b.系部代码 " +
			"where 学号 in(select 学号 from V_学籍异动数据子类)";
		if(!"".equalsIgnoreCase(this.getJX_XH())){
			sql += " and a.学号 like '%" + MyTools.fixSql(this.getJX_XH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getJX_XM())){
			sql += " and a.姓名 like '%" + MyTools.fixSql(this.getJX_XM()) + "%'";
		}
		sql += " order by a.学号";		
		vec = db.getConttexJONSArr(sql, pageNum, pagSize);
		return vec;
	}	

	/**
	 * 读取异动信息下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadYDCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a " +
				"where a.父类别代码='XSYDZTM' and a.描述 in ('02','06','08','10','11','18') order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
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
	 * 新增异动信息
	 * @throws SQLException
	 */
	public void AddAbn() throws SQLException {
		Vector vec = new Vector();
		String sql = "";
		Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if("08".equalsIgnoreCase(this.getJX_YDLXM()) || "09".equalsIgnoreCase(this.getJX_YDLXM()) || "10".equalsIgnoreCase(this.getJX_YDLXM()) 
//			|| "11".equalsIgnoreCase(this.getJX_YDLXM()) || "12".equalsIgnoreCase(this.getJX_YDLXM()) || "13".equalsIgnoreCase(this.getJX_YDLXM()) 
//			|| "14".equalsIgnoreCase(this.getJX_YDLXM()) || "15".equalsIgnoreCase(this.getJX_YDLXM()) || "16".equalsIgnoreCase(this.getJX_YDLXM()) 
//			|| "17".equalsIgnoreCase(this.getJX_YDLXM())){
//			this.setJX_XBJDM("");
//		}
		
		sql = "insert into V_学籍异动数据子类 (编号,异动类别码,异动日期,异动去向学校码,异动说明,原专业码,原班号,现专业码,现班号,学号,姓名,身份证号) values(" +
			"'" + MyTools.fixSql(db.getNewGUID(request)) + "'," +//编号
			"'" + MyTools.fixSql(this.getJX_YDLXM()) + "'," + //异动类别
			"'" + sdf.format(d)+ "'," + //异动日期
			//"'" + MyTools.fixSql(this.getJX_YDRQ()) + "'," + //异动日期
			"'" + MyTools.fixSql(this.getJX_YDQXXX()) + "'," + //异动去向学校
			"'" + MyTools.fixSql(this.getJX_YDYY()) + "'," + //异动说明
			"'" + MyTools.fixSql(this.getJX_YZYM()) + "'," + //原专业码
			"'" + MyTools.fixSql(this.getJX_YBH()) + "'," + //原班号
			"'" + MyTools.fixSql(this.getJX_XZYM()) + "'," + //现专业码
			"'" + MyTools.fixSql(this.getJX_XBH()) + "'," + //现班号
			"'" + MyTools.fixSql(this.getJX_XH()) + "'," + //学号
			"'" + MyTools.fixSql(this.getJX_XM()) + "'," + //姓名
			"'" + MyTools.fixSql(this.getJX_SFZJH()) + "')";//身份证号
		vec.add(sql);
		
		sql = "update dbo.V_学生基本数据子类 set ";
		if("02".equalsIgnoreCase(this.getJX_YDLXM()) || "06".equalsIgnoreCase(this.getJX_YDLXM()) || "18".equalsIgnoreCase(this.getJX_YDLXM())){
			sql += "学生状态='01'," +
				"专业代码='" + MyTools.fixSql(this.getJX_XZYM()) + "'," +
				"行政班代码='" + MyTools.fixSql(this.getJX_XBH()) + "'," +
				"班内学号=(select case when len(班内学号)=1 then '0'+班内学号 else 班内学号 end from (select isnull((select top 1 cast(cast(班内学号 as int)+1 as nvarchar) from V_学生基本数据子类 where 行政班代码='" + MyTools.fixSql(this.getJX_XBH()) + "' order by cast(班内学号 as int) desc), '01') as 班内学号) as t)";
		}else{
			sql += "学生状态='" + MyTools.fixSql(this.getJX_YDLXM()) + "' ";
		}
		sql += "where 学号='" + MyTools.fixSql(this.getJX_XH()) + "'";
		vec.add(sql);
		
		if(db.executeInsertOrUpdateTransaction(vec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		};
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
	 * 读取身份证件类型下拉框
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSFZJLXCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='SFZJLXM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	/**
	 * 读取婚姻状况下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadHYZKCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='HYZKM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取民族下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMZCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='MZDM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取国别下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGBCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='GBDM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取政治面貌下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadZZMMCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='ZZMMDM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取港澳台侨下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadGATQCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='GATQDM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取血型下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXXCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='XXDM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取健康状况下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadJKZKCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='JKZTM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取城市代码下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadCSDMCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='CSDM'";
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
	/**
	 * 读取招生类别码下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadZSLBCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='ZSLBM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取户口性质码下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadHKXZCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='HKXZM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 读取流动人口状况下拉框
	 * 
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadLDRKCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue   " +
				"union all " +
				"select a.类别名称 as comboName , a.描述 as comboValue  from V_信息类别_类别操作 a where a.父类别代码='LDRKZKM'";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}
	
	/**
	 * 新增一个学生
	 * @return
	 * @throws SQLException 
	 */
	public	void AddStudent() throws SQLException {
		Vector sqlVec = new Vector();
		//String stuAuth = MyTools.getProp(request, "Base.stu");
		//String stuParentAuth = MyTools.getProp(request, "Base.stuParent");
		
		if(!"".equalsIgnoreCase(this.getJX_BYNF())){
			this.setJX_BYNF(this.getJX_BYNF().subSequence(0, 4).toString());
		}else{
			this.setJX_BYNF("");
		}
		
		sql = "select count(*) from dbo.V_学生基本数据子类  where 学籍号='" + MyTools.fixSql(this.getJX_XJH()) + "' or 学号='" + MyTools.fixSql(this.getJX_XH()) + "'";
		if(db.getResultFromDB(sql)){
			this.setMSG("学籍号或学号已存在，无法新增！");
		}else {
			sql = "insert into dbo.V_学生基本数据子类 values(" +
				"'"+MyTools.fixSql(this.getJX_XJH())+"'," +//学籍号
				"'"+MyTools.fixSql(this.getJX_XH())+"'," +//学号
				"'"+MyTools.fixSql(request.getParameter("JX_XZZYM"))+"'," +//专业代码
				"'"+MyTools.fixSql(this.getJX_XZBDM())+"'," +//行政班
				"'"+MyTools.fixSql(this.getJX_BNXH())+"'," +//班内学号
				"replace('"+MyTools.fixSql(this.getJX_XM())+"',' ','')," +//姓名
				"'"+MyTools.fixSql(this.getJX_YWXM())+"'," +//英文姓名
				"'"+MyTools.fixSql(this.getJX_XMPY())+"'," +//姓名拼音
				"'"+MyTools.fixSql(this.getJX_CYM())+"'," +//曾用名
				"'"+MyTools.fixSql(this.getJX_SFZJLXM())+"'," +//身份证件类型
				"'"+MyTools.fixSql(this.getJX_SFZJH())+"'," +//身份证件
				"'"+MyTools.fixSql(this.getJX_XBM())+"'," +//性别码
				"'"+MyTools.fixSql(this.getJX_XXM())+"'," +//血型码
				"'"+MyTools.fixSql(this.getJX_CSNY())+"'," +//出生日期
				"'"+MyTools.fixSql(this.getJX_CSDZM())+"'," +//出生地
				"'"+MyTools.fixSql(this.getJX_JGM())+"',"+//籍贯
				"'"+MyTools.fixSql(this.getJX_MZM())+"'," +//民族
				"'"+MyTools.fixSql(this.getJX_HYZK())+"'," +//婚姻状况
				"'"+MyTools.fixSql(this.getJX_ZJXY())+"'," +//宗教信仰
				"'"+MyTools.fixSql(this.getJX_GATQM())+"'," +//港澳台侨
				"'"+MyTools.fixSql(this.getJX_JKZKM())+"'," +//家健康状况
				"'"+MyTools.fixSql(this.getJX_ZZMMM())+"'," +//政治面貌
				"''," +//户口所在地行政区划码
				"'"+MyTools.fixSql(this.getJX_HKLBM())+"'," +//户口类别
				"'"+MyTools.fixSql(this.getJX_LDRKZK())+"'," +//是否流动人口
				"'"+MyTools.fixSql(this.getJX_GBM())+"'," +//国籍
				"'"+MyTools.fixSql("")+"'," +//特长
				"'"+MyTools.fixSql("")+"'," +//联系电话
				"'"+MyTools.fixSql("")+"'," +//网络地址
				"'"+MyTools.fixSql("")+"'," +//即时通讯号
				"'"+MyTools.fixSql("")+"'," +//电子信箱
				"'',"+//照片
				"'"+MyTools.fixSql(this.getJX_ZPLJ())+"'," +//照片路径
				"'',"+//是否锁定
				"''," +//学历
				"''," +//升高
				"''," +//体重
				"''," +//常住地址
				"'null','null','null','null','null','null',"+//英语、计算机、五笔字型、英语打字、其他掌握掌握程度及已获证书级别 社会实践
				"'"+MyTools.fixSql(this.getJX_HKSZD())+"'," +//户籍地址
				"'"+MyTools.fixSql(this.getJX_YZBM())+"'," +//户籍邮编
				"''," +//家庭住址
				"''," +//家庭邮编
				"''," +//家长姓名
				"''," +//家长手机
				"'"+MyTools.fixSql(this.getJX_SFBSHJ())+"'," +//是否本市户籍
				"'"+MyTools.fixSql(getJX_BRSJ())+"'," +//本人手机
				"'01',"+//学生状态//
				"''," +//家庭电话
				"''," ;//奖惩
				if("".equalsIgnoreCase(this.getJX_BYLXRQ())||"null".equalsIgnoreCase(this.getJX_BYLXRQ())){
					sql +=""+MyTools.fixSql("null")+"," ;//毕业离校日期
				}else{
					sql +="'"+MyTools.fixSql(this.getJX_BYLXRQ())+"'," ;//毕业离校日期
				}
				
				sql += "'01',"+//人员类型
				"'"+MyTools.fixSql(this.getJX_XSLBM())+"'," +//学生类别
				"'"+MyTools.fixSql(this.getJX_ZSLBM())+"',";//招生类别
				if("".equalsIgnoreCase(this.getJX_RXRQ())||"null".equalsIgnoreCase(this.getJX_RXRQ())){
					sql +=""+MyTools.fixSql("null")+"," ;//入学日期
				}else{
					sql +="'"+MyTools.fixSql(this.getJX_RXRQ())+"'," ;//入学日期
				}
				sql+="'"+MyTools.fixSql(this.getJX_BYNF())+"'," +//毕业年份
				"'"+MyTools.fixSql(this.getJX_PZH())+"'," +//拍照号
				"'"+MyTools.fixSql(this.getJX_BZ())+"'," +//备注
				"'"+MyTools.fixSql(this.getJX_SSPCS())+"'," +//所属派出所
				"'"+MyTools.fixSql(this.getJX_BYXX())+"'," +//毕业学校
				"'"+MyTools.fixSql(this.getJX_FQXM())+"'," +//父亲姓名
				"'"+MyTools.fixSql(this.getJX_FQNL())+"'," +//父亲年龄
				"'"+MyTools.fixSql(this.getJX_FQZZMMM())+"'," +//父亲政治面貌
				"'"+MyTools.fixSql(this.getJX_FQGZDW())+"'," +//父亲工作单位
				"'"+MyTools.fixSql(this.getJX_FQDH())+"'," +//父亲电话
				"'"+MyTools.fixSql(this.getJX_MQXM())+"'," +//母亲姓名
				"'"+MyTools.fixSql(this.getJX_MQNL())+"'," +//母亲年龄
				"'"+MyTools.fixSql(this.getJX_MQZZMMM())+"'," +//母亲政治面貌
				"'"+MyTools.fixSql(this.getJX_MQGZDW())+"'," +//母亲工作单位
				"'"+MyTools.fixSql(this.getJX_MQDH())+"'," +//母亲电话
				"'"+MyTools.fixSql(this.getJX_QTCYCW())+"'," +//其他成员称谓
				"'"+MyTools.fixSql(this.getJX_QTCYXM())+"'," +//其他成员姓名
				"'"+MyTools.fixSql(this.getJX_QTCYNL())+"'," +//其他成员年龄
				"'"+MyTools.fixSql(this.getJX_QTCYZZMMM())+"'," +//其他成员政治面貌
				"'"+MyTools.fixSql(this.getJX_QTCYGZDW())+"'," +//其他成员工作单位
				"'"+MyTools.fixSql(this.getJX_QTCYDH())+"')";//其他成员电话
			sqlVec.add(sql);
			
			/*sql = "insert into sysUserinfo (UserName,UserCode,Password,state,email) values(" +
				"'" + MyTools.fixSql(this.getJX_XM()) + "'," +
				"'" + MyTools.fixSql(this.getJX_KH()) + "'," +
				"'" + Base64Util.encode("111111") + "'," +
				"'Y','')";
			sqlVec.add(sql);
			
			sql = "insert into sysUserinfo (UserName,UserCode,Password,state,email) values(" +
				"'" + MyTools.fixSql(this.getJX_XM()+"家长") + "'," +
				"'P" + MyTools.fixSql(this.getJX_KH()) + "'," +
				"'" + Base64Util.encode("111111") + "'," +
				"'Y','')";
			sqlVec.add(sql);
			
			sql = "insert into sysUserAuth (UserCode,AuthCode,state) values(" +
				"'" + MyTools.fixSql(this.getJX_KH()) + "','" + MyTools.fixSql(stuAuth) + "','Y')";
			sqlVec.add(sql);
			sql = "insert into sysUserAuth (UserCode,AuthCode,state) values(" +
				"'P" + MyTools.fixSql(this.getJX_KH()) + "','" + MyTools.fixSql(stuParentAuth) + "','Y')";
			sqlVec.add(sql);*/
			
			if(db.executeInsertOrUpdateTransaction(sqlVec)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
		}
	}
	
	/**
	 * 上传照片到临时路径
	 */
	public void uploadImg(SmartUpload mySmartUpload)throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		try {
			//获取配置路径
			String url=MyTools.getProp(request, "Base.headImg");
			File file = new File(url);
			// 根据配置路径来判断没有文件则创建文件
			if (!file.exists()) {
				file.mkdirs();
			}
			//文件路径
			String path= unescape(mySmartUpload.getRequest().getParameter("path"));
			File f=new File(path);
			//获取文件名称
			String filename=f.getName();
			//System.out.println("bean:文件名："+filename);
			String prefix=filename.substring(filename.lastIndexOf("."));
			File oldFile=new File(url+filename);
			//System.out.println("bean:文件路径名称是："+url+oldFile.getName());
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date newdate=new Date();//给图片加时间为防止页面图片不刷新
			String temp1=form.format(newdate);
//			System.out.println("时间+++++++++++"+temp1);
			//文件重命名，改成用户名
			oldFile.renameTo(new File(url+getUSERCODE()+temp1+prefix));
			//存放路径
			mySmartUpload.getFiles().getFile(0).saveAs(url+getUSERCODE()+temp1+prefix);
			this.setMSG(getUSERCODE()+temp1+prefix);
		} catch (Exception e) {
			e.printStackTrace();
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
	
	
	//保存图片
	public void saveImp(String SFZJH,String savePath){
		try {
			//获取配置路径
			String url=MyTools.getProp(request, "Base.headImg");
			String url1=MyTools.getProp(request, "Base.Photo");
			File file = new File(url1);
			// 根据配置路径来判断没有文件则创建文件
			if (!file.exists()) {
				file.mkdirs();
			}
			//文件路径
			String path= unescape(savePath);
			File f=new File(path);
			//获取文件名称
			String filename=f.getName();
			File oldFile=new File(url+filename);
			String prefix=filename.substring(filename.lastIndexOf("."));
			//删除此人原来有的照片以便移动
			File folder = new File(url1);
			File temp=null;
			File[] filelist= folder.listFiles();//列出文件里所有的文件
			for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
				temp=filelist[i];
				if(temp.getName().indexOf(SFZJH)>=0){
					temp.delete();//删除文件
				}
			}
			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date newdate=new Date();//给图片加时间为防止页面图片不刷新
			String temp1=form.format(newdate);
			//System.out.println("时间+++++++++++"+temp1);
			if(oldFile.renameTo(new File(url1+SFZJH+"-"+temp1+prefix))){
				//删除临时文件夹里面的所有照片
				filelist=file.listFiles();
				for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
					temp=filelist[i];
					if(temp.getName().indexOf(getUSERCODE())>=0){
						temp.delete();//删除文件
					}
				}
				this.setMSG(SFZJH+"-"+temp1+prefix);
			}else{
				this.setMSG("");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.setMSG("系统正在维护");//防止系统出现不知名的错误
		}
	}
	
	/**
	 *修改一个学生 
	 *@param code 原学籍号
	 *@param stuCode 原学号
	 *@param stuName 原姓名
	 * @return
	 * @throws SQLException 
	 */
	public void ModStudent(String code, String stuCode, String stuName) throws SQLException  {
		Vector sqlVec = new Vector();
		
		//判断学籍号或学号是否做了修改
		if(!code.equalsIgnoreCase(this.getJX_XJH())) {
			sql = "select count(*) from V_学生基本数据子类 where 学籍号='" + MyTools.fixSql(this.getJX_XJH()) + "'";
			
			if(db.getResultFromDB(sql)){
				this.setMSG("修改的学籍号已存在，保存失败！");
				return;
			}
		}
		
		if(!stuCode.equalsIgnoreCase(this.getJX_XH())) {
			sql = "select count(*) from V_学生基本数据子类 where 学号='" + MyTools.fixSql(this.getJX_XH()) + "'";
			
			if(db.getResultFromDB(sql)){
				this.setMSG("修改的学号已存在，保存失败！");
				return;
			}
		}
		
		if(!"".equalsIgnoreCase(this.getJX_BYNF())){
			this.setJX_BYNF(this.getJX_BYNF().subSequence(0, 4).toString());
		}else{
			this.setJX_BYNF("");
		}
		if("".equalsIgnoreCase(this.getJX_BYLXRQ())){
			this.setJX_BYLXRQ("");
		}
		sql = "update dbo.V_学生基本数据子类 set " +
			"学籍号 = '"+MyTools.fixSql(this.getJX_XJH())+"'," +//学籍号
			"学号 = '"+MyTools.fixSql(this.getJX_XH())+"'," +//学号
			"专业代码 = '"+MyTools.StrFiltr(request.getParameter("JX_XZZYM"))+"'," +//专业代码
			"行政班代码 = '"+MyTools.fixSql(this.getJX_XZBDM())+"'," +//行政班
			"班内学号= '"+MyTools.fixSql(this.getJX_BNXH())+"'," +//班内学号
			"姓名=replace('"+MyTools.fixSql(this.getJX_XM())+"',' ','')," +//姓名
			"英文姓名='"+MyTools.fixSql(this.getJX_YWXM())+"'," +//英文姓名
			"姓名拼音='"+MyTools.fixSql(this.getJX_XMPY())+"'," +//姓名拼音
			"曾用名='"+MyTools.fixSql(this.getJX_CYM())+"'," +//曾用名
			"身份证件类型码='"+MyTools.fixSql(this.getJX_SFZJLXM())+"'," +//身份证件类型
			"身份证件号='"+MyTools.fixSql(this.getJX_SFZJH())+"'," +//身份证件
			"性别码='"+MyTools.fixSql(this.getJX_XBM())+"'," +//性别码
			"血型码='"+MyTools.fixSql(this.getJX_XXM())+"'," +//血型码
			"出生日期='"+MyTools.fixSql(this.getJX_CSNY())+"'," +//出生日期
			"出生地码='"+MyTools.fixSql(this.getJX_CSDZM())+"'," +//出生地
			"籍贯='"+MyTools.fixSql(this.getJX_JGM())+"',"+//籍贯
			"民族码='"+MyTools.fixSql(this.getJX_MZM())+"'," +//民族
			"婚姻状况码='"+MyTools.fixSql(this.getJX_HYZK())+"'," +//婚姻状况
			"信仰宗教码='"+MyTools.fixSql(this.getJX_ZJXY())+"'," +//宗教信仰
			"港澳台侨外码='"+MyTools.fixSql(this.getJX_GATQM())+"'," +//港澳台侨
			"健康状况码='"+MyTools.fixSql(this.getJX_JKZKM())+"'," +//家健康状况
			"政治面貌码='"+MyTools.fixSql(this.getJX_ZZMMM())+"'," +//政治面貌
		//	"''," +//户口所在地行政区划码
			"户口类别码='"+MyTools.fixSql(this.getJX_HKLBM())+"'," +//户口类别
			"是否是流动人口='"+MyTools.fixSql(this.getJX_LDRKZK())+"'," +//是否流动人口
			"[国籍/ 地区码] = '"+MyTools.fixSql(this.getJX_GBM())+"'," ;//国籍
			//特长
			//联系电话
			//网络地址
			//即时通讯号
			//电子信箱
			//"通讯地址='"+MyTools.fixSql(this.getJX_TXDZ())+"',";
			//"'',"+//照片
		if(!"".equalsIgnoreCase(this.getJX_ZPLJ())){
			sql+="照片路径='"+MyTools.fixSql(this.getJX_ZPLJ())+"'," ;//照片路径
		}
		//"'',"+//是否锁定
		sql +=//学历
		//升高
		//体重
		"常住地址='"+MyTools.fixSql(this.getJX_XZZZ())+"'," +//常住地址
		//"'','','','','','',"+//英语、计算机、五笔字型、英语打字、其他掌握掌握程度及已获证书级别 社会实践
		"户籍地址='"+MyTools.fixSql(this.getJX_HKSZD())+"'," +//户籍地址
		"户籍邮编='"+MyTools.fixSql(this.getJX_YZBM())+"'," +//户籍邮编
		//家庭住址
		//家庭邮编
		//家长姓名
		//家长手机
		"是否本市户籍='"+MyTools.fixSql(this.getJX_SFBSHJ())+"'," +//是否本市户籍
		"本人手机='"+MyTools.fixSql(getJX_BRSJ())+"'," //本人手机
		//"'01',"+//学生状态//
		//家庭电话
		 ;//奖惩
		if("".equalsIgnoreCase(this.getJX_BYLXRQ())){
			sql+="毕业离校日期="+MyTools.fixSql("null")+"," ;//毕业离校日期
		}else{
			sql+="毕业离校日期='"+MyTools.fixSql(this.getJX_BYLXRQ())+"'," ;//毕业离校日期
		}
		
		//"'',"+//人员类型
		sql += "学生类别='"+MyTools.fixSql(this.getJX_XSLBM())+"'," +//学生类别
			"招生类别='"+MyTools.fixSql(this.getJX_ZSLBM())+"',";//招生类别
		if("".equalsIgnoreCase(this.getJX_RXRQ())){
			sql +="入学日期="+MyTools.fixSql("null")+"," ;//入学日期
		}else{
			sql +="入学日期='"+MyTools.fixSql(this.getJX_RXRQ())+"'," ;//入学日期
		}
		sql += "毕业年份='"+MyTools.fixSql(this.getJX_BYNF())+"'," +//毕业年份
			"拍照号='"+MyTools.fixSql(this.getJX_PZH())+"'," +//拍照号
			"备注='"+MyTools.fixSql(this.getJX_BZ())+"'," +//备注
			"所属派出所='"+MyTools.fixSql(this.getJX_SSPCS())+"'," +//所属派出所
			"毕业学校='"+MyTools.fixSql(this.getJX_BYXX())+"'," +//毕业学校
			"父亲='"+MyTools.fixSql(this.getJX_FQXM())+"'," +//父亲姓名
			"父亲年龄='"+MyTools.fixSql(this.getJX_FQNL())+"'," +//父亲年龄
			"父亲政治面貌='"+MyTools.fixSql(this.getJX_FQZZMMM())+"'," +//父亲政治面貌
			"父亲工作单位='"+MyTools.fixSql(this.getJX_FQGZDW())+"'," +//父亲工作单位
			"父亲电话='"+MyTools.fixSql(this.getJX_FQDH())+"'," +//父亲电话
			"母亲='"+MyTools.fixSql(this.getJX_MQXM())+"'," +//母亲姓名
			"母亲年龄='"+MyTools.fixSql(this.getJX_MQNL())+"'," +//母亲年龄
			"母亲政治面貌='"+MyTools.fixSql(this.getJX_MQZZMMM())+"'," +//母亲政治面貌
			"母亲工作单位='"+MyTools.fixSql(this.getJX_MQGZDW())+"'," +//母亲工作单位
			"母亲电话='"+MyTools.fixSql(this.getJX_MQDH())+"'," +//母亲电话
			"其他成员称谓='"+MyTools.fixSql(this.getJX_QTCYCW())+"'," +//其他成员称谓
			"其他成员姓名='"+MyTools.fixSql(this.getJX_QTCYXM())+"'," +//其他成员姓名
			"其他成员年龄='"+MyTools.fixSql(this.getJX_QTCYNL())+"'," +//其他成员年龄
			"其他成员政治面貌='"+MyTools.fixSql(this.getJX_QTCYZZMMM())+"'," +//其他成员政治面貌
			"其他成员工作单位='"+MyTools.fixSql(this.getJX_QTCYGZDW())+"'," +//其他成员工作单位
			"其他成员电话='"+MyTools.fixSql(this.getJX_QTCYDH())+"' " +//其他成员电话
			"where 学籍号='" + MyTools.fixSql(code) + "' and 学号='" + MyTools.fixSql(stuCode) + "' ";
		sqlVec.add(sql);
		
		//更新成绩信息相关学号姓名
		if(!stuCode.equalsIgnoreCase(this.getJX_XH()) || !stuName.equalsIgnoreCase(this.getJX_XM())){
			sql = "update V_成绩管理_学生成绩信息表 set 学号='" + MyTools.fixSql(this.getJX_XH()) + "',姓名='" + MyTools.fixSql(this.getJX_XM()) + "' where 学号='" + MyTools.fixSql(stuCode) + "'";
			sqlVec.add(sql);
			
			sql = "update V_成绩管理_学生补考成绩信息表 set 学号='" + MyTools.fixSql(this.getJX_XH()) + "',姓名='" + MyTools.fixSql(this.getJX_XM()) + "' where 学号='" + MyTools.fixSql(stuCode) + "'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	//班级下拉框赋值
	public Vector loadBJCombo() throws SQLException {
		/*sql = "select '' as comboValue,'请选择' as comboName,'' as gradeNum,'' as classNum " +
			"union all " +
			"select a.班级代码 as comboValue,a.班级名称 as comboName,年级代码,班级序号 from V_基础信息_班级信息表 a  " +
			"where cast((" +
			"select top 1 学年 from V_基础信息_学年学期表 " +
			"where 学期结束时间>=getDate() order by 学期开始时间) as int)-cast(left(班级代码,4) as int)<7 " +
			"order by gradeNum,classNum";*/
		sql = "select '' as comboValue,'请选择' as comboName " +
			"union all " +
			"select a.行政班代码 as comboValue ,a.行政班名称 as comboName  from V_学校班级数据子类 a order by comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}

	/**
	 * 删除学生
	 * @param khInfo 
	 */
	public void DelRec(String xhInfo) throws SQLException{
		Vector sqlVec = new Vector();
		String xhInfoArray[] = xhInfo.split(",", -1);
		
		if(xhInfoArray.length > 0){
			for(int a=0;a<xhInfoArray.length;a++){
				sqlVec.clear();
				
				sql = "delete dbo.V_学生基本数据子类   where 学号='" + MyTools.fixSql(xhInfoArray[a]) + "'";
				sqlVec.add(sql);
				
				/*sql = "delete sysUserinfo where UserCode='" + MyTools.fixSql(khInfoArray[a]) + "'";
				sqlVec.add(sql);
				
				sql = "delete sysUserinfo where UserCode='P" + MyTools.fixSql(khInfoArray[a]) + "'";
				sqlVec.add(sql);
				
				sql = "delete sysUserAuth where UserCode='" + MyTools.fixSql(khInfoArray[a]) + "'";
				sqlVec.add(sql);*/
				
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					//删除该学生的照片
					String url1 = MyTools.getProp(request, "Base.Photo");
					File folder = new File(url1);
					if(folder.exists()){
						File temp = null;
						File[] filelist= folder.listFiles();//列出文件里所有的文件
						for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
							temp=filelist[i];
							if(temp.getName().indexOf(xhInfoArray[a])>=0){
								temp.delete();//删除文件
							}
						}
					}
					this.setMSG("删除成功");
				}else{
					this.setMSG("删除失败");
				}
			}
		}else{
			this.setMSG("请选择一条数据");
		}
	}
	
	/**
	 * 学生学籍状态切换
	 * @author yeq 
	 * @date:2016-10-12
	 * @throws SQLException
	 */
	public void changeState() throws SQLException{
		sql = "update V_基础信息_学生信息表 set 学籍状态=(case when 学籍状态='1' then '0' else '1' end) " +
			"where 学号='" + MyTools.fixSql(this.getJX_XH()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("保存成功");
		}else{
			this.setMSG("保存失败");
		}
	}
	
	/**
	 * ExportExcelStudent 学生信息设置导出
	 * @author maowei 
	 * @date:2016-09-26
	 * @param testName 考试名称
	 * @param njName 年级名称
	 * @return savePath 下载路径
	 * @throws SQLException
	 */
	public String ExportExcelStudent() throws SQLException{
		String sql = "";
		String savePath = "";
		Vector titleVec = new Vector();//有用
		
		titleVec.add("学号");
		titleVec.add("学籍号");
		titleVec.add("姓名");
		titleVec.add("行政班代码");
				
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
			savePath += "/"+" 学生信息模板 "+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
			//savePath += "/test_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date) + ".xls";
			OutputStream os = new FileOutputStream(savePath);
			WritableWorkbook wbook = Workbook.createWorkbook(os);//建立excel文件
			WritableSheet wsheet = wbook.createSheet("Sheet1", 0);//工作表名称
			WritableFont fontStyle;
			WritableCellFormat contentStyle;
			Label content;
			//设置列宽
			for(int i=0; i<titleVec.size(); i++){
				wsheet.setColumnView(i, 15);
			}

			//设置title
			fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//垂直居中
			
			for(int i=0;i<titleVec.size();i++){   
			     // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z   
			     // 在Label对象的子对象中指明单元格的位置和内容   
				content = new Label(i,0,titleVec.get(i).toString(),contentStyle); 
			     // 将定义好的单元格添加到工作表中   
				wsheet.addCell(content);   
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
		return savePath;
	}
	
	/**
	 * Savestudentimportxls 学生信息设置导入
	 * @createDate 2017-3-8
	 * @author ZouYu
	 * @description:导入
	 * @param mySmartUpload
	 * @throws SQLException
	 * @throws IOException
	 * @throws SmartUploadException
	 */
	public void Savestudentimportxls(SmartUpload mySmartUpload) throws IOException, SmartUploadException{
		Vector sqlVec = new Vector();
		String sql = "";
		Vector titleVec = new Vector();
		Vector curTitleVec = new Vector();
		Vector resultVec =new Vector();
		Vector MsgVec = new Vector();
//		String stuAuth = MyTools.getProp(request, "Base.stu");
		String url = MyTools.getProp(request, "Base.UploadExcelPath");
		File filep = new File(url);
		//根据配置路径来判断没有文件则创建文件
		if (!filep.exists()) {
			filep.mkdirs();
		}
		Workbook workbook = null;
		SmartFiles files = mySmartUpload.getFiles();
		com.jspsmart.upload.SmartFile file = null;
		
		//判断
		String path = unescape(mySmartUpload.getRequest().getParameter("sxpath"));
		//获取文件名称
		File f = new File(path);
		String filename = f.getName();
		//判断
		if(files.getCount() > 0){
			file = files.getFile(0);
			if(file.getSize()<=0){
				this.MSG="文件内容为空，请确认！";
			}
			file.saveAs(url +"/"+filename);
		}
		url=url +"/"+filename;
//		System.out.println("file=:"+file.getFileName());
//		System.out.println("term=:"+term);
		File file1=new File(url);
		InputStream is = new FileInputStream(file1);
		
		//定义允许出现的列
		titleVec.add("学籍号");
		titleVec.add("学号");
		titleVec.add("证件号");
		titleVec.add("证件类型");
		titleVec.add("姓名");
		titleVec.add("性别");
		titleVec.add("出生日期");
		titleVec.add("招生类别");
		titleVec.add("专业代码");
		titleVec.add("专业名称");
		titleVec.add("班号");
		titleVec.add("班内学号");
		titleVec.add("民族");
		titleVec.add("籍贯");
		titleVec.add("国别");
		titleVec.add("政治面貌");
		titleVec.add("港澳台侨胞");
		titleVec.add("户口性质");
		titleVec.add("户口所在详细地址");
		titleVec.add("学生类别");
		titleVec.add("入学日期");
		titleVec.add("毕业年份");
		titleVec.add("拍照号");
		titleVec.add("邮政编码");
		titleVec.add("备注");
		titleVec.add("所属派出所");
		titleVec.add("毕业学校");
		titleVec.add("父亲");
		titleVec.add("父亲年龄");
		titleVec.add("父亲政治面貌");
		titleVec.add("父亲的工作单位");
		titleVec.add("父亲的电话");
		titleVec.add("母亲");
		titleVec.add("母亲政治面貌");
		titleVec.add("母亲年龄");
		titleVec.add("母亲工作单位");
		titleVec.add("母亲的电话");
		titleVec.add("其他成员称谓");
		titleVec.add("其他成员的姓名");
		titleVec.add("其他成员的年龄");
		titleVec.add("其他成员的政治面貌");
		titleVec.add("其他成员的工作单位");
		titleVec.add("其他成员的手机号码");
		
		try {
			workbook = Workbook.getWorkbook(is);
			if(workbook.getNumberOfSheets() == 0){
				this.setMSG("未找到sheet");
				return;
			}
			
			Sheet sheet = workbook.getSheet(0); //获取sheet(k)的数据对象
			int rsRows = sheet.getRows(); //获取sheet(k)中的总行数
			int rscolumns = sheet.getColumns(); //获取总列数
			
			//获取当前表格中的列名
			for(int i=0; i<rscolumns; i++){
				if(!"".equalsIgnoreCase(sheet.getCell(i,0).getContents().trim())){
					curTitleVec.add(sheet.getCell(i,0).getContents().trim());
				}
			}
			
			//判断导入文件内容字段数量与模板是否一致
			if(titleVec.size() != curTitleVec.size()){
				this.setMSG("导入文件格式错误，请检查导入文件格式与模板是否一致！");
				return;
			}
			
			if(curTitleVec!=null && curTitleVec.size()>0){
				//判断是否存在学籍号信息列
				if(!"学籍号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(0)))){
					this.setMSG("格式不符合规范，表格中第一列必须为学籍号信息列！");
					return;
				}else{
					for(int i=1; i<curTitleVec.size(); i++){
						if("学籍号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(i)))){
							this.setMSG("格式不符合规范，表格中只能有一列学籍号信息！");
							return;
						}
					}
				}
				
				//查询基础信息
				//班级
				sql = "select 行政班代码,行政班名称 from V_学校班级数据子类";
				Vector classVec = db.GetContextVector(sql);
				//专业
				sql = "select 专业代码,专业名称  from V_专业基本信息数据子类";
				Vector ZYVec = db.GetContextVector(sql);
				//证件类型
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='SFZJLXM'";
				Vector ZJLXVec = db.GetContextVector(sql);
				//性别
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='XBM'";
				Vector XBVec = db.GetContextVector(sql);
				//招生类别
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='ZSLBM'";
				Vector ZSLBVec = db.GetContextVector(sql);
				//民族
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='MZDM'";
				Vector MZVec = db.GetContextVector(sql);
				//籍贯
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='CSDM'";
				Vector JGVec = db.GetContextVector(sql);
				//国别
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='GBDM'";
				Vector GBVec = db.GetContextVector(sql);
				//政治面貌
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='ZZMMDM'";
				Vector ZZMMVec = db.GetContextVector(sql);
				//港澳台侨
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='GATQDM'";
				Vector GATQVec = db.GetContextVector(sql);
				//户口性质
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='HKXZM'";
				Vector HKXZVec = db.GetContextVector(sql);
				//学生类别
				sql = "select 类别名称,描述 from V_信息类别_类别操作 where 父类别代码='XSLBM'";
				Vector XSLBVec = db.GetContextVector(sql);
				
				String tempColName = "";
				String tempContent = "";
				int curIndex = 0;
				int tempIndex = 0;
				boolean existFlag = true;
				
				//遍历表格内容
				for(int i=1; i<rsRows; i++){
					/*
					for(int j=0; j<curTitleVec.size(); j++){
						tempColName = MyTools.StrFiltr(curTitleVec.get(j));
						tempContent = sheet.getCell(j, i).getContents().trim();
						
						if(j == 9){
							//判断如果列名为学籍号且不为空，添加默认空数据
							if("班号".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent) && tempContent.length()==5
								&& "班内学号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(j+1))) && !"".equalsIgnoreCase(sheet.getCell(j+1, i).getContents().trim()) && sheet.getCell(j+1, i).getContents().trim().length()==2){
								for(int k=0; k<titleVec.size(); k++){
									resultVec.add("");
									curIndex++;
								}
							}else{
								break;
							}
						}
						
						tempIndex = titleVec.indexOf(tempColName);
						if(tempIndex > -1){
							resultVec.set(curIndex-titleVec.size()+tempIndex, tempContent);
						}
					}
					*/
					//判断如果列名为学籍号且不为空，添加默认空数据
//					if(("学籍号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(0))) && !"".equalsIgnoreCase(sheet.getCell(0, i).getContents().trim()))
//						|| ("班号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(9))) && !"".equalsIgnoreCase(sheet.getCell(9, i).getContents().trim()) && sheet.getCell(9, i).getContents().trim().length()==5
//						&& "班内学号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(10))) && !"".equalsIgnoreCase(sheet.getCell(10, i).getContents().trim()) && sheet.getCell(10, i).getContents().trim().length()==2)){
//					if(("学籍号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(0))) && !"".equalsIgnoreCase(sheet.getCell(0, i).getContents().trim()))
//						|| ("学号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(1))) && !"".equalsIgnoreCase(sheet.getCell(1, i).getContents().trim()))){
					if("学号".equalsIgnoreCase(MyTools.StrFiltr(curTitleVec.get(1))) && !"".equalsIgnoreCase(sheet.getCell(1, i).getContents().trim())){
						for(int k=0; k<titleVec.size(); k++){
							resultVec.add(sheet.getCell(k, i).getContents().trim());
						}
					}else{
						break;
					}
				}
				
				//根据学号去除重复数据，以最后一条数据为准
				String tempStuCode = "";
				for(int i=0; i<(resultVec.size()-titleVec.size()); i+=titleVec.size()){
					tempStuCode = MyTools.StrFiltr(resultVec.get(i+1));
					
					for(int j=(i+titleVec.size()); j<resultVec.size(); j+=titleVec.size()){
						if(tempStuCode.equalsIgnoreCase(MyTools.StrFiltr(resultVec.get(j+1)))){
							for(int k=0; k<titleVec.size(); k++){
								resultVec.remove(i);
							}
							i-=titleVec.size();
							break;
						}
					}
				}
				
				//判断部分数据列是否存在并将中文内容转换为代码形式
				for(int i=0; i<resultVec.size(); i+=titleVec.size()){
					for(int j=0; j<titleVec.size(); j++){
						tempColName = MyTools.StrFiltr(titleVec.get(j));
						tempContent = MyTools.StrFiltr(resultVec.get(i+j));
						
						//判断专业是否存在
						if("专业代码".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent)){
							existFlag = false;
						
							for(int k=0; k<ZYVec.size(); k+=2){
								if(tempContent.equalsIgnoreCase(MyTools.StrFiltr(ZYVec.get(k)))){
									existFlag = true;
									break;
								}
							}
							if(existFlag==false && MsgVec.indexOf(tempContent)<0)
								MsgVec.add(tempContent);
						}
						
						//判断行政班是否存在
						if("班号".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent)){
							existFlag = false;
							
							for(int k=0; k<classVec.size(); k+=2){
								if(tempContent.equalsIgnoreCase(MyTools.StrFiltr(classVec.get(k)))){
									existFlag = true;
									break;
								}
							}
							if(existFlag==false && MsgVec.indexOf(tempContent)<0)
								MsgVec.add(tempContent);
						}
						
						if("证件类型".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(ZJLXVec, tempContent, MsgVec);
						
						if("性别".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(XBVec, tempContent, MsgVec);
						
						if("招生类别".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(ZSLBVec, tempContent, MsgVec);
						
						if("民族".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(MZVec, tempContent, MsgVec);
						
						if("籍贯".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(JGVec, tempContent, MsgVec);
						
						if("国别".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(GBVec, tempContent, MsgVec);
						
						if("政治面貌".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(ZZMMVec, tempContent, MsgVec);
						
						if("港澳台侨胞".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(GATQVec, tempContent, MsgVec);
						
						if("户口性质".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(HKXZVec, tempContent, MsgVec);
						
						if("学生类别".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(XSLBVec, tempContent, MsgVec);
						
						if("父亲政治面貌".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(ZZMMVec, tempContent, MsgVec);
						
						if("母亲政治面貌".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(ZZMMVec, tempContent, MsgVec);
						
						if("其他成员的政治面貌".equalsIgnoreCase(tempColName) && !"".equalsIgnoreCase(tempContent))
							tempContent = this.parseStrToCode(ZZMMVec, tempContent, MsgVec);
						
						resultVec.set(i+j, tempContent);
					}
				}
			
				//添加学生数据
				for(int i=0; i<resultVec.size(); i+=titleVec.size()){
					//判断如果有学籍号，以学籍号为主键，否则与学号为主键进行查询
//					if(!"".equalsIgnoreCase(MyTools.StrFiltr(resultVec.get(i)))){
//						sql = "select count(*) from V_学生基本数据子类 where 学籍号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "'";
//					}else{
//						sql = "select count(*) from V_学生基本数据子类 where ";
//					}
					
//					sql = "select count(*) from V_学生基本数据子类 where 学籍号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "' " +
//						"or 学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'";
					sql = "select count(*) from V_学生基本数据子类 where 学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'";
					
					if(!db.getResultFromDB(sql)){
						sql = "insert into dbo.V_学生基本数据子类 (学籍号,学号,身份证件号,身份证件类型码,姓名,性别码,出生日期,招生类别,专业代码,行政班代码,班内学号,民族码,籍贯,[国籍/ 地区码]," +
							"政治面貌码,港澳台侨外码,户口类别码,户籍地址,学生类别,入学日期,毕业年份,拍照号,户籍邮编,备注,所属派出所,毕业学校," +
							"父亲,父亲年龄,父亲政治面貌,父亲工作单位,父亲电话,母亲,母亲年龄,母亲政治面貌,母亲工作单位,母亲电话," +
							"其他成员称谓,其他成员姓名,其他成员年龄,其他成员政治面貌,其他成员工作单位,其他成员电话," +
							"学生状态) values(" +
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "'," +//学籍号
							//"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+10))+MyTools.StrFiltr(resultVec.get(i+11))) + "'," +//学号
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'," +//学号
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+2))) + "'," +//身份证件号
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+3))) + "'," +//身份证件类型码
							"replace('" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+4))) + "',' ','')," +//姓名
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+5))) + "'," +//性别码
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+6))) + "'," +//出生日期
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+7))) + "'," +//招生类别
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+8))) + "'," +//专业代码
//							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+8))) + "'," +//专业名称
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+10))) + "'," +//行政班代码
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+11))) + "'," +//班内学号
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+12))) + "'," +//民族码
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+13))) + "'," +//籍贯
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+14))) + "'," +//[国籍/ 地区码]
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+15))) + "'," +//政治面貌码
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+16))) + "'," +//港澳台侨外码
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+17))) + "'," +//户口类别码
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+18))) + "'," +//户籍地址
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+19))) + "'," +//学生类别
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+20))) + "'," +//入学日期
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+21))) + "'," +//毕业年份
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+22))) + "'," +//拍照号
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+23))) + "'," +//户籍邮编
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+24))) + "'," +//备注
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+25))) + "'," +//所属派出所
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+26))) + "'," +//毕业学校
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+27))) + "'," +//父亲
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+28))) + "'," +//父亲年龄
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+29))) + "'," +//父亲政治面貌
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+30))) + "'," +//父亲工作单位
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+31))) + "'," +//父亲电话
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+32))) + "'," +//母亲
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+34))) + "'," +//母亲年龄,
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+33))) + "'," +//母亲政治面貌,
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+35))) + "'," +//母亲工作单位
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+36))) + "'," +//母亲电话,
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+37))) + "'," +//其他成员称谓
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+38))) + "'," +//其他成员姓名
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+40))) + "'," +//其他成员年龄
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+39))) + "'," +//其他成员政治面貌
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+41))) + "'," +//其他成员工作单位
							"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+42))) + "'," +//其他成员电话
							"'01')";//学生状态
					}else{
						sql = "update dbo.V_学生基本数据子类  set " +
							"学籍号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "'," +
//							"学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'," +
							"身份证件号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+2))) + "'," +
							"身份证件类型码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+3))) + "'," +
							"姓名=replace('" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+4))) + "',' ','')," +
							"性别码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+5))) + "'," +
							"出生日期='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+6))) + "'," +
							"招生类别='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+7))) + "'," +
							"专业代码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+8))) + "'," +
//							"专业名称'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+8))) + "'," +//专业名称
							"行政班代码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+10))) + "'," +
							"班内学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+11))) + "'," +
							"民族码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+12))) + "'," +
							"籍贯='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+13))) + "'," +
							"[国籍/ 地区码]='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+14))) + "'," +
							"政治面貌码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+15))) + "'," +
							"港澳台侨外码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+16))) + "'," +
							"户口类别码='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+17))) + "'," +
							"户籍地址='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+18))) + "'," +
							"学生类别='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+19))) + "'," +
							"入学日期='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+20))) + "'," +
							"毕业年份='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+21))) + "'," +
							"拍照号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+22))) + "'," +
							"户籍邮编='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+23))) + "'," +
							"备注='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+24))) + "'," +
							"所属派出所='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+25))) + "'," +
							"毕业学校='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+26))) + "'," +
							"父亲='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+27))) + "'," +
							"父亲年龄='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+28))) + "'," +
							"父亲政治面貌='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+29))) + "'," +
							"父亲工作单位='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+30))) + "'," +
							"父亲电话='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+31))) + "'," +
							"母亲='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+32))) + "'," +
							"母亲年龄='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+34))) + "'," +
							"母亲政治面貌='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+33))) + "'," +
							"母亲工作单位='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+35))) + "'," +
							"母亲电话='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+36))) + "'," +
							"其他成员称谓='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+37))) + "'," +
							"其他成员姓名='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+38))) + "'," +
							"其他成员年龄='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+40))) + "'," +
							"其他成员政治面貌='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+39))) + "'," +
							"其他成员工作单位='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+41))) + "'," +
							"其他成员电话='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+42))) + "' ";
						//判断如果有学籍号，以学籍号为主键，否则与学号为主键
//						if(!"".equalsIgnoreCase(MyTools.StrFiltr(resultVec.get(i)))){
//							sql += "where 学籍号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "'";
//						}else{
//							sql += "where 学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'";
//						}
//						sql += "where 学籍号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "' " +
//							"or 学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'";
						sql += "where 学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'";
					}
					sqlVec.add(sql);
				}
				
				if(db.executeInsertOrUpdateTransaction(sqlVec)){
					this.setMSG("导入成功");
					
					if(MsgVec!=null && MsgVec.size()>0){
						this.setMSG2(MsgVec.toString().substring(1,MsgVec.toString().length()-1) + "@以上数据不正确,相关信息已导入为空,可修改后重新上传或在学生设置模块设置。");
					}
				}else {
					this.setMSG("导入失败");
				}
			}
		}catch (Exception e) {
			this.setMSG("导入失败");
			e.printStackTrace();
		}finally {
			workbook.close();
			is.close();
			//删除服务器上文件
			//路径为文件且不为空则进行删除  
		    if (file1.isFile() && file1.exists()) {  
		    	file1.delete();
		    }
		}
	}
	
	public String parseStrToCode(Vector vec, String str, Vector MsgVec){
		boolean flag = false;
		String result = "";
		
		for(int i=0; i<vec.size(); i+=2){
			if(str.equalsIgnoreCase(MyTools.StrFiltr(vec.get(i)))){
				result = MyTools.StrFiltr(vec.get(i+1));
				flag = true;
				break;
			}
		}
		if(flag==false && MsgVec.indexOf(str)<0)
			MsgVec.add(str);
		return result;
	}

	/**
	 * Savestudentimportxls 学生信息设置导入
	 * @createDate 2016-09-26
	 * @author maowei
	 * @description:导入
	 * @param mySmartUpload
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 * @throws SmartUploadException
	 * @throws WrongSQLException
	 */
	/*public void Savestudentimportxls(SmartUpload mySmartUpload) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		Vector sqlVec = new Vector();
		String sql = "";
		String tempsoin="";//第1列
		String importflag="";
		Vector titleVec = new Vector();
		Vector resultVec =new Vector();
		Vector MsgVec = new Vector();
		Vector classVec = new Vector();//班级信息
		Vector ZJLXVec = new Vector();//证件类型信息
		Vector XBVec = new Vector();//性别信息
		Vector ZSLBVec = new Vector();//招生类别信息
		Vector ZYVec = new Vector();//专业信息
		Vector MZVec = new Vector();//民族信息
		Vector JGVec = new Vector();//籍贯信息
		Vector GBVec = new Vector();//国别信息
		Vector ZZMMVec = new Vector();//政治面貌信息
		Vector GATQVec = new Vector();//港澳台侨信息
		Vector HKXZVec = new Vector();//户口性质信息
		Vector XSLBVec = new Vector();//学生类别信息
		
		String stuAuth = MyTools.getProp(request, "Base.stu");
		titleVec.add("学籍号");
		titleVec.add("证件号");
		titleVec.add("证件类型");
		titleVec.add("姓名");
		titleVec.add("性别");
		titleVec.add("出生日期");
		titleVec.add("招生类别");
		titleVec.add("专业代码");
		titleVec.add("专业名称");
		titleVec.add("班号");
		titleVec.add("民族");
		titleVec.add("籍贯");
		titleVec.add("国别");
		titleVec.add("政治面貌");
		titleVec.add("港澳台侨胞");
		titleVec.add("户口性质");
		titleVec.add("户口所在详细地址");
		titleVec.add("学生类别");
		titleVec.add("入学日期");
		titleVec.add("毕业年份");
		titleVec.add("拍照号");
		titleVec.add("邮政编码");
		titleVec.add("备注");
		titleVec.add("所属派出所");
		titleVec.add("毕业学校");
		titleVec.add("父亲");
		titleVec.add("父亲年龄");
		titleVec.add("父亲政治面貌");
		titleVec.add("父亲的工作单位");
		titleVec.add("父亲的电话");
		titleVec.add("母亲");
		titleVec.add("母亲政治面貌");
		titleVec.add("母亲年龄");
		titleVec.add("母亲工作单位");
		titleVec.add("母亲的电话");
		titleVec.add("其他成员称谓");
		titleVec.add("其他成员的姓名");
		titleVec.add("其他成员的政治面貌");
		titleVec.add("其他成员的年龄");
		titleVec.add("其他成员的工作单位");
		titleVec.add("其他成员的手机号码");
		
		String url = MyTools.getProp(request, "Base.UploadExcelPath");
		File filep = new File(url);
		//根据配置路径来判断没有文件则创建文件
		if (!filep.exists()) {
			filep.mkdirs();
		}
		Workbook workbook = null;
		Vector vectormx=new Vector();
		
		//Files files = mySmartUpload.getFiles(); //获取文件
		SmartFiles files = mySmartUpload.getFiles();
		com.jspsmart.upload.SmartFile file = null;
		//com.jspsmart.upload.File file= null;
		
		//判断
		String path= unescape(mySmartUpload.getRequest().getParameter("sxpath"));
		//获取文件名称
		File f=new File(path);
		String filename=f.getName();
		//判断
		if(files.getCount() > 0){
			file = files.getFile(0);
			if(file.getSize()<=0){
				this.MSG="文件内容为空，请确认！";
			}
			file.saveAs(url +"/"+filename);
		}
		url=url +"/"+filename;
//		System.out.println("file=:"+file.getFileName());
//		System.out.println("term=:"+term);
		File file1=new File(url);
		InputStream is = new FileInputStream(file1);
		
		try {
			workbook = Workbook.getWorkbook(is);
			
			if(workbook.getNumberOfSheets()==0){
				this.setMSG("未找到sheet");
				return;
			}
			
			//for(int k=0;k<workbook.getNumberOfSheets();k++){
				Sheet sheet = workbook.getSheet(0); //获取sheet(k)的数据对象
				int rsRows = sheet.getRows(); //获取sheet(k)中的总行数
				int rscolumns = sheet.getColumns(); //获取总列数
				//tempsheet = workbook.getSheet(k).getName();//获取sheet名
				//if("sheet1".equalsIgnoreCase(tempsheet.trim().toLowerCase())){
				
				//判断导入Excel的列长是否和导入格式中一致
				if(titleVec.size() == (rscolumns-2)){    
					for(int x=0; x<titleVec.size(); x++){
						tempsoin = sheet.getCell(x, 0).getContents().trim(); //第2行
						
						if(tempsoin.equalsIgnoreCase(MyTools.StrFiltr(titleVec.get(x)))){
							importflag = "true";
						}else{
							importflag = "false";
							String msg = titleVec.toString().substring(1,titleVec.toString().length()-1);
							this.setMSG("格式不正确（Excel应该为--"+msg+"--格式）");
							return;
						}
					}
				}else{
					importflag = "false";
					String msg = titleVec.toString().substring(1,titleVec.toString().length()-1);
					this.setMSG("格式不正确（Excel应该为--"+msg+"--格式）");
					return;
				}
				
				if(importflag.equalsIgnoreCase("true")){
					//查询所有行政班代码
//					sql2="select distinct 班级代码 from dbo.V_基础信息_班级信息表";
//					classno=db.GetContextVector(sql2);
					
					//查询当前学年相关的所有班级信息
					//班级
					sql = "select 行政班代码 , 行政班名称 a from dbo.V_学校班级数据子类";
					classVec = db.GetContextVector(sql);
					//专业
					sql="select a.专业代码,a.专业名称  from V_专业基本信息数据子类 a";
					ZYVec=db.GetContextVector(sql);
					//证件类型
					sql="select a.类别名称  , a.描述 from V_信息类别_类别操作 a where a.父类别代码='SFZJLXM'";
					ZJLXVec=db.GetContextVector(sql);
					//性别
					sql="select a.类别名称   , a.描述    from V_信息类别_类别操作 a where a.父类别代码='XBM'";
					XBVec=db.GetContextVector(sql);
					//招生类别
					sql="select a.类别名称  , a.描述   from V_信息类别_类别操作 a where a.父类别代码='ZSLBM'";
					ZSLBVec=db.GetContextVector(sql);
					//民族
					sql="select a.类别名称  , a.描述   from V_信息类别_类别操作 a where a.父类别代码='MZDM'";
					MZVec=db.GetContextVector(sql);
					//籍贯
					sql="select a.类别名称  , a.描述   from V_信息类别_类别操作 a where a.父类别代码='CSDM'";
					JGVec=db.GetContextVector(sql);
					//国别
					sql="select a.类别名称  , a.描述   from V_信息类别_类别操作 a where a.父类别代码='GBDM'";
					GBVec=db.GetContextVector(sql);
					//政治面貌
					sql="select a.类别名称  , a.描述   from V_信息类别_类别操作 a where a.父类别代码='ZZMMDM'";
					ZZMMVec=db.GetContextVector(sql);
					//港澳台侨
					sql="select a.类别名称  , a.描述   from V_信息类别_类别操作 a where a.父类别代码='GATQDM'";
					GATQVec=db.GetContextVector(sql);
					//户口性质
					sql="select a.类别名称   , a.描述    from V_信息类别_类别操作 a where a.父类别代码='HKXZM'";
					HKXZVec=db.GetContextVector(sql);
					//学生类别
					sql="select a.类别名称  , a.描述   from V_信息类别_类别操作 a where a.父类别代码='XSLBM'";
					XSLBVec=db.GetContextVector(sql);
					
					String classCode = "";
					String classNum = "";
					String className = "";
					String ZYDMCode = "";
					String checkSql = "";
					String ZYCode="";
					String ZYNname="";
					String ZJLXCode="";
					String ZJLXName="";
					String XBCode="";
					String XBName="";
					String ZSLBCode="";
					String ZSLBName="";
					String MZCode= "";
					String MZName="";
					String JGCode="";
					String JGName="";
					String GBCode="";
					String GBName="";
					String ZZMMCode="";
					String ZZMMName="";
					String FQZZMMCode="";
					String FQZZMMName="";
					String MQZZMMCode="";
					String MQZZMMName="";
					String QTZZMMCode="";
					String QTZZMMName="";
					String GATQCode="";
					String GATQName="";
					String HKXZCode="";
					String HKXZName="";
					String XSLBCode="";
					String XSLBName="";
					
					for(int a=2; a<rsRows; a++){
						//判断学号姓名是否为空
						if(sheet.getCell(0, a).getContents().trim().equalsIgnoreCase("")||sheet.getCell(3, a).getContents().trim().equalsIgnoreCase("")){
							this.setMSG("学籍号,姓名不能为空,请修改后重新上传");
							return;
						}
						
						for(int j=0; j<titleVec.size(); j++){
							tempsoin=sheet.getCell(j, a).getContents().trim();
							resultVec.add(tempsoin);
						}
					}
					//添加学生信息
					for(int i =0;i<resultVec.size();i+=titleVec.size()){
						//判断该数据是否已存在
						checkSql="select count(*) from dbo.V_学生基本数据子类  a " +
								"where a.学号='" + MyTools.fixSql(resultVec.get(i).toString()) + "' ";
						ZYDMCode = MyTools.StrFiltr(resultVec.get(i+7).toString());//专业代码
						classNum=MyTools.StrFiltr(resultVec.get(i+9).toString());//班号
						classCode = classNum.substring(0, 2)+ZYDMCode+classNum.substring(2,5);//行政班代码
						className="";
						ZYNname=MyTools.StrFiltr(resultVec.get(i+8).toString());
						ZYCode="";
						ZJLXName=MyTools.StrFiltr(resultVec.get(i+2).toString());
						ZJLXCode="";
						XBName=MyTools.StrFiltr(resultVec.get(i+4).toString());
						XBCode="";
						ZSLBName=MyTools.StrFiltr(resultVec.get(i+6).toString());
						ZSLBCode="";
						MZName=MyTools.StrFiltr(resultVec.get(i+10).toString());
						MZCode="";
						JGName=MyTools.StrFiltr(resultVec.get(i+11).toString());
						JGCode="";
						GBName=MyTools.StrFiltr(resultVec.get(i+12).toString());
						GBCode="";
						ZZMMName=MyTools.StrFiltr(resultVec.get(i+13).toString());
						ZZMMCode="";
						FQZZMMName=MyTools.StrFiltr(resultVec.get(i+27).toString());;
						FQZZMMCode="";
						MQZZMMName=MyTools.StrFiltr(resultVec.get(i+31).toString());;
						MQZZMMCode="";
						QTZZMMName=MyTools.StrFiltr(resultVec.get(i+37).toString());;
						QTZZMMCode="";
						GATQName=MyTools.StrFiltr(resultVec.get(i+14).toString());
						GATQCode="";
						HKXZName=MyTools.StrFiltr(resultVec.get(i+15).toString());
						HKXZCode="";
						XSLBName=MyTools.StrFiltr(resultVec.get(i+17).toString());
						XSLBName="";
						boolean flag = false;
						
						//判断行政班是否存在
						for(int w=0; w<classVec.size(); w+=2){
							if(MyTools.StrFiltr(classVec.get(w)).equalsIgnoreCase(classCode)){
								classCode = MyTools.StrFiltr(classVec.get(w));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(classCode)<0 && !"".equalsIgnoreCase(classCode)){
								MsgVec.add(classCode);
							}
						}
						
						//判断港澳台侨是否存在
						for(int w=0; w<GATQVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(GATQVec.get(w)).equalsIgnoreCase(GATQName)){
								GATQCode = MyTools.StrFiltr(GATQVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(GATQName)<0 && !"".equalsIgnoreCase(GATQName)){
								MsgVec.add(GATQName);
							}
						}

						//判断性别是否存在
						for(int w=0; w<XBVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(XBVec.get(w)).equalsIgnoreCase(XBName)){
								XBCode = MyTools.StrFiltr(XBVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(XBName)<0 && !"".equalsIgnoreCase(XBName)){
								MsgVec.add(XBName);
							}
						}

						//判断证件类型是否存在
						for(int w=0; w<ZJLXVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(ZJLXVec.get(w)).equalsIgnoreCase(ZJLXName)){
								ZJLXCode = MyTools.StrFiltr(ZJLXVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(ZJLXName)<0 && !"".equalsIgnoreCase(ZJLXName)){
								MsgVec.add(ZJLXName);
							}
						}
						
						//判断民族是否存在
						for(int w=0; w<MZVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(MZVec.get(w)).equalsIgnoreCase(MZName)){
								MZCode = MyTools.StrFiltr(MZVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(MZName)<0 && !"".equalsIgnoreCase(MZName)){
								MsgVec.add(MZName);
							}
						}
						
						//判断籍贯是否存在
						for(int w=0; w<JGVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(JGVec.get(w)).equalsIgnoreCase(JGName)){
								JGCode = MyTools.StrFiltr(JGVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(JGName)<0 && !"".equalsIgnoreCase(JGName)){
								MsgVec.add(JGName);
							}
						}
						
						//判断国别是否存在
						for(int w=0; w<GBVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(GBVec.get(w)).equalsIgnoreCase(GBName)){
								GBCode = MyTools.StrFiltr(GBVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(GBName)<0 && !"".equalsIgnoreCase(GBName)){
								MsgVec.add(GBName);
							}
						}
						
						//判断政治面貌是否存在
						for(int w=0; w<ZZMMVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(ZZMMVec.get(w)).equalsIgnoreCase(ZZMMName)){
								ZZMMCode = MyTools.StrFiltr(ZZMMVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(ZZMMName)<0 && !"".equalsIgnoreCase(ZZMMName)){
								MsgVec.add(ZZMMName);
							}
						}
						
						//判断父亲政治面貌是否存在
						for(int w=0; w<ZZMMVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(ZZMMVec.get(w)).equalsIgnoreCase(FQZZMMName)){
								FQZZMMCode = MyTools.StrFiltr(ZZMMVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(FQZZMMName)<0 && !"".equalsIgnoreCase(FQZZMMName)){
								MsgVec.add(FQZZMMName);
							}
						}
						
						//母亲判断政治面貌是否存在
						for(int w=0; w<ZZMMVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(ZZMMVec.get(w)).equalsIgnoreCase(MQZZMMName)){
								MQZZMMCode = MyTools.StrFiltr(ZZMMVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(MQZZMMName)<0 && !"".equalsIgnoreCase(MQZZMMName)){
								MsgVec.add(MQZZMMName);
							}
						}
						
						//其他判断政治面貌是否存在
						for(int w=0; w<ZZMMVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(ZZMMVec.get(w)).equalsIgnoreCase(QTZZMMName)){
								QTZZMMCode = MyTools.StrFiltr(ZZMMVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag==false){
							if(MsgVec.indexOf(QTZZMMName)<0 && !"".equalsIgnoreCase(QTZZMMName)){
								MsgVec.add(QTZZMMName);
							}
						}
						//判断招生类别是否存在
						for(int w=0; w<ZSLBVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(ZSLBVec.get(w)).equalsIgnoreCase(ZSLBName)){
								ZSLBCode = MyTools.StrFiltr(ZSLBVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(HKXZName)<0 && !"".equalsIgnoreCase(HKXZName)){
								MsgVec.add(HKXZName);
							}
						}
						
						//判断户口性质是否存在
						for(int w=0; w<HKXZVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(HKXZVec.get(w)).equalsIgnoreCase(HKXZName)){
								HKXZCode = MyTools.StrFiltr(HKXZVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(HKXZName)<0 && !"".equalsIgnoreCase(HKXZName)){
								MsgVec.add(HKXZName);
							}
						}
						
						//判断学生类别是否存在
						for(int w=0; w<XSLBVec.size(); w+=2){
							flag = false;
							if(MyTools.StrFiltr(XSLBVec.get(w)).equalsIgnoreCase(XSLBName)){
								XSLBCode = MyTools.StrFiltr(XSLBVec.get(w+1));
								flag = true;
								break;
							}
						}
						if(flag == false){
							if(MsgVec.indexOf(XSLBName)<0 && !"".equalsIgnoreCase(XSLBName)){
								MsgVec.add(XSLBName);
							}
						}
												
						if(!db.getResultFromDB(checkSql)){
							sql = "insert into dbo.V_学生基本数据子类 (学号,身份证件号,身份证件类型码,姓名,性别码,招生类别,民族码,籍贯,[国籍/ 地区码],政治面貌码," +
									"港澳台侨外码,户口类别码,户籍地址,学生类别,入学日期,毕业年份,拍照号,户籍邮编,备注,所属派出所,毕业学校,父亲,父亲年龄,父亲政治面貌,父亲工作单位,父亲电话," +
									"母亲,母亲政治面貌,母亲年龄,母亲工作单位,母亲电话,其他成员称谓,其他成员姓名,其他成员年龄,其他成员政治面貌,其他成员工作单位,其他成员电话," +
									"行政班代码,学生状态) values(" +
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i))) + "'," +//学籍号
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'," +//身份证件号
								"'" + MyTools.fixSql(ZJLXCode) + "'," +//身份证件类型码
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+3))) + "'," +//姓名
								"'" + MyTools.fixSql(XBCode) + "'," +//性别
								"'" + MyTools.fixSql(ZSLBCode) + "'," +//招生类别
								"'" + MyTools.fixSql(MZCode) + "'," +//民族
								"'" + MyTools.fixSql(JGCode) + "'," +//籍贯
								"'" + MyTools.fixSql(GBCode) + "'," +//国籍
								"'" + MyTools.fixSql(ZZMMCode) + "'," +//政治面貌
								"'" + MyTools.fixSql(GATQCode) + "'," +//港澳台侨
								"'" + MyTools.fixSql(HKXZCode) + "'," +//户口性质
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+16))) + "'," +//户口所在详细地址
								"'" + MyTools.fixSql(XSLBCode) + "'," +//学生类别
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+18))) + "'," +//入学日期
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+19))) + "'," +//毕业年份
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+20))) + "'," +//拍照号
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+21))) + "'," +//邮政编码
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+22))) + "'," +//备注
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+23))) + "'," +//所属派出所
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+24))) + "'," +//毕业学校
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+25))) + "'," +//父亲
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+26))) + "'," +//父亲年龄
								"'" + MyTools.fixSql(FQZZMMCode) + "'," +//父亲政治面貌
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+28))) + "'," +//父亲工作
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+29))) + "'," +//父亲电话
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+30))) + "'," +//母亲
								"'" + MyTools.fixSql(MQZZMMCode) + "'," +//母亲政治面貌
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+32))) + "'," +//母亲年龄
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+33))) + "'," +//母亲工作单位
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+34))) + "'," +//母亲电话
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+35))) + "'," +//其他成员称谓
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+36))) + "'," +//其他成姓名
								"'" + MyTools.fixSql(QTZZMMCode) + "'," +//其他成员的政治面貌
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+38))) + "'," +//其他成员的年龄
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+39))) + "'," +//其他成员的工作单位
								"'" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+40))) + "'," +//其他成员的其他成员的
								"'" + MyTools.fixSql(classCode) + "'," +//行政班代码
								"'01')";//状态
							sqlVec.add(sql);
						}else{
							sql = "update dbo.V_学生基本数据子类  set " +
								"身份证件号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+1))) + "'," +
								"身份证件类型码='" + MyTools.fixSql(ZJLXCode) + "'," +
								"姓名='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+3))) + "'," +
								"行政班代码='" + MyTools.fixSql(classCode) + "'," +
								"性别码='"+MyTools.fixSql(XBCode) + "'," +
								"出生日期='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+5))) + "'," +
								"招生类别='"+MyTools.fixSql(ZSLBCode) + "'," +
								"民族码='"+MyTools.fixSql(MZCode) + "'," +
								"籍贯='"+MyTools.fixSql(JGCode) + "'," +
								"[国籍/ 地区码]='"+MyTools.fixSql(GBCode) + "'," +
								"政治面貌码='"+MyTools.fixSql(ZZMMCode) + "'," +
								"港澳台侨外码='"+MyTools.fixSql(GATQCode) + "'," +
								"户口类别码='"+MyTools.fixSql(HKXZCode) + "'," +
								"户籍地址='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+16))) + "'," +
								"学生类别='"+MyTools.fixSql(XSLBCode) + "'," +
								"入学日期='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+18))) + "'," +
								"毕业年份='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+19))) + "'," +
								"拍照号='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+20))) + "'," +
								"户籍邮编='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+21))) + "'," +
								"备注='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+22))) + "'," +
								"所属派出所='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+23))) + "'," +
								"毕业学校='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+24))) + "'," +
								"父亲='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+25))) + "'," +
								"父亲年龄='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+26))) + "'," +
								"父亲政治面貌='"+MyTools.fixSql(FQZZMMCode) + "'," +
								"父亲工作单位='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+28))) + "'," +
								"父亲电话='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+29))) + "'," +
								"母亲='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+30))) + "'," +
								"母亲政治面貌='"+MyTools.fixSql(MQZZMMCode) + "'," +
								"母亲年龄='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+32))) + "'," +
								"母亲工作单位='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+33))) + "'," +
								"母亲电话='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+34))) + "'," +
								"其他成员称谓='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+35))) + "'," +
								"其他成员姓名='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+36))) + "'," +
								"其他成员政治面貌='"+MyTools.fixSql(QTZZMMCode) + "'," +
								"其他成员年龄='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+38))) + "'," +
								"其他成员工作单位='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+39))) + "'," +
								"其他成员电话='"+MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i+40))) + "'" +
								"where 学号='" + MyTools.fixSql(MyTools.StrFiltr(resultVec.get(i)))+"'";
							sqlVec.add(sql);
							
						}
					}
					if(db.executeInsertOrUpdateTransaction(sqlVec)){
						this.setMSG("导入成功");
						
						//this.setMSG2("以下班级名称不正确或不存在，请确认后重新导入该班级学生（"+msg+"）");
						if(MsgVec!=null && MsgVec.size()>0){
							String msg = MsgVec.toString().substring(1,MsgVec.toString().length()-1);
							this.setMSG2(""+msg+"@以上名称错误,相关信息已导入为空,可修改后重新上传或在学生设置模块设置。");
						}
					}else{
						this.setMSG("导入失败");
					}
					return;
				}
			//}
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
	}*/
	
	//学生信息导出班级下拉框给
	public Vector loadStuClassCombo() throws SQLException {
		sql =" select '' as comboValue,'全部' as comboName,'' as yearNum" +
				" union all " +
				" select 行政班代码 as comboValue , 行政班名称 as comboName , 年级代码 as yearNum from dbo.V_学校班级数据子类" +
				" order by yearNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取年级下拉框
	 * @date:2017-09-20
	 * @author:zouyu
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
	 * @date:2017-09-20
	 * @author:zouyu
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
	* @date:2017-09-20
	 * @author:zouyu
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

	/**
	 * ExportExcelStuScore 学生信息导出
	 * @author maowei
	 * @date:2016-10-25
	 * @param
	 * @modify ZouYu 
	 * @date:2017-3-6
	 * @return savePath 下载路径
	 * @throws SQLException
	 */
	public String ExportExcelStuScoreall(String lieInfo,String bjbh,String bjdm) throws SQLException {
		String sql = "";
		Vector stuVec = null;
		Vector vec = null;
		Vector resultVec = new Vector();
		String savePath = "";
		
		boolean existFlag = false;
		String internalStu = MyTools.getProp(request, "Base.internalStu");
		String lieInfoArray[] = lieInfo.split(",", -1);
		sql = "select ";
		for(int i=0;i<lieInfoArray.length;i++){
			//sql += MyTools.fixSql(MyTools.StrFiltr(lieInfoArray[i]))+",";
			if("学籍号".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="distinct a.学籍号 as 学籍号,";
			}
			
			if("学号".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +=" a.学号 as 学号,";
			}
			
			if("证件号".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.身份证件号   as 证件号,";
			}
			if("证件类型".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="b.类别名称   as 证件类型,";
			}
			if("姓名".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.姓名  as 姓名,";
			}
			if("性别".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="c.类别名称  as 性别,";
			}
			if("出生日期".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.出生日期  as 出生日期,";
			}
			if("招生类别".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="d.类别名称  as 招生类别,";
			}
			if("专业代码".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.专业代码 as 专业代码,";
			}
			if("专业名称".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="e.专业名称  as 专业名称,";
			}
			if("班号".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.行政班代码 as 班号,";
			}
			if("民族".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="f.类别名称  as 民族,";
			}
			if("籍贯".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="g.类别名称  as 籍贯,";
			}
			if("国别".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="h.类别名称  as 国别,";
			}
			if("政治面貌".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="i.类别名称  as 政治面貌,";
			}
			
			if("港澳台侨胞".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="j.类别名称  as 港澳台侨胞,";
			}
			if("户口性质".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="k.类别名称 as 户口性质,";
			}
			
			if("户口所在详细地址".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.户籍地址 as 户口所在详细地址,";
			}
			if("学生类别".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="l.类别名称  as 学生类别,";
			}
			if("入学日期".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.入学日期 as 入学日期,";
			}
			if("毕业年份".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.毕业年份 as 毕业年份,";
			}
			if("拍照号".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.拍照号 as 拍照号,";
			}
			if("邮政编码".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.户籍邮编  as 邮政编码,";
			}
			if("备注".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.备注 as 备注,";
			}
			if("所属派出所".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.所属派出所 as 所属派出所,";
			}
			if("毕业学校".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.毕业学校 as 毕业学校,";
			}
			if("父亲".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.父亲 as 父亲,";
			}
			if("父亲年龄".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.父亲年龄 as 父亲年龄,";
			}
			if("父亲政治面貌".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="m.类别名称 as 父亲政治面貌,";
			}
			if("父亲的工作单位".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.父亲工作单位  as 父亲的工作单位,";
			}
			if("父亲的电话".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.父亲电话 as 父亲的电话,";
			}
			if("母亲".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.母亲 as 母亲,";
			}
			if("母亲年龄".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.母亲年龄  as 母亲年龄,";
			}
			if("母亲政治面貌".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="n.类别名称 as 母亲政治面貌,";
			}
			if("母亲工作单位".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.母亲工作单位 as 母亲工作单位,";
			}
			if("母亲的电话".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.母亲电话  as 母亲的电话,";
			}
			if("其他成员称谓".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.其他成员称谓  as 其他成员称谓,";
			}
			if("其他成员的姓名".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.其他成员姓名  as 其他成员的姓名,";
			}
			if("其他成员的年龄".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.其他成员年龄  as 其他成员的年龄,";
			}
			if("其他成员的政治面貌".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="o.类别名称  as 其他成员的政治面貌,";
			}
			if("其他成员的工作单位".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.其他成员工作单位  as 其他成员的工作单位,";
			}
			if("其他成员的手机号码".equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="a.其他成员电话  as 其他成员的手机号码,";
			}
			/*if(("性别").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="b.类别名称 as 性别,";
			}else if(("民族").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="c.类别名称 as 民族,";
			}else if(("招生类别").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="d.类别名称 as 招生类别,";
			}
			else if(("港澳台侨外码").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="e.类别名称 as 港澳台侨外,";
			}
			else if(("血型码").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="f.类别名称 as 血型,";
			}
			else if(("政治面貌码").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="g.类别名称 as 政治面貌,";
			}
			else if(("[国籍/ 地区码]").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="h.类别名称 as [国籍/ 地区],";
			}
			else if(("健康状况码").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="i.类别名称 as 健康状况,";
			}
			else if(("籍贯").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="j.类别名称 as 籍贯,";
			}
			else if(("婚姻状况码").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="k.类别名称 as 婚姻状况,";
			}
			else if(("出生地码").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="j.类别名称 as 出生地,";
			}
			else if(("户口类别码").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="l.类别名称 as 户口类别,";
			}
			else if(("是否是流动人口").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="m.类别名称 as 是否是流动人口,";
			}
			else if(("父亲政治面貌").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="x.类别名称 as 父亲政治面貌,";
			}
			else if(("母亲政治面貌").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="w.类别名称 as 母亲政治面貌,";
			}
			else if(("其他成员政治面貌").equalsIgnoreCase(MyTools.StrFiltr(lieInfoArray[i]))){
				sql +="y.类别名称 as 其他成员政治面貌,";
			}
			else{
				sql += MyTools.fixSql(MyTools.StrFiltr(lieInfoArray[i]))+",";
			}*/
		}
		sql = sql.substring(0, sql.length()-1);
		/*sql+=" from dbo.V_学生基本数据子类 a " +
			"left join dbo.V_信息类别_类别操作 b on a.性别码=b.描述 and b.父类别代码='XBM'"+
			"left join dbo.V_信息类别_类别操作 c on a.民族码=c.描述 and c.父类别代码='MZDM'" +
			"left join V_信息类别_类别操作  d on a.招生类别=d.描述 and d.父类别代码='ZSLBM'" +
			"left join dbo.V_信息类别_类别操作 g on a.政治面貌码=g.描述     and g.父类别代码='ZZMMDM'" +
			"left join dbo.V_信息类别_类别操作 M on a.是否是流动人口=M.描述 and M.父类别代码='LDRKZKM'" +
			"left join dbo.V_信息类别_类别操作 L on a.户口类别码=l.描述 and l.父类别代码='HKXZM'" +
			"left join dbo.V_信息类别_类别操作 j on a.籍贯=j.描述  and j.父类别代码='CSDM'" +
			"left join dbo.V_信息类别_类别操作 p on a.出生地码=p.描述  and p.父类别代码='CSDM'" +
			"left join dbo.V_信息类别_类别操作 i on a.健康状况码=i.描述 and i.父类别代码='JKZTM'" +
			"left join dbo.V_信息类别_类别操作 K on a.婚姻状况码=k.描述 and k.父类别代码='HYZKM'" +
			"left join dbo.V_信息类别_类别操作 h on a.[国籍/ 地区码]=h.描述 and h.父类别代码='GBDM'" +
			"left join dbo.V_信息类别_类别操作 f on a.血型码=f.描述 and f.父类别代码='XXDM'" +
			"left join dbo.V_信息类别_类别操作 e on a.港澳台侨外码=e.描述 and e.父类别代码='GATQDM'" +
			"left join dbo.V_信息类别_类别操作 y on a.其他成员政治面貌=y.描述     and y.父类别代码='ZZMMDM'" +
			"left join dbo.V_信息类别_类别操作 w on a.母亲政治面貌=w.描述     and w.父类别代码='ZZMMDM'" +ZSLBM
			"left join dbo.V_信息类别_类别操作 x on a.父亲政治面貌=x.描述     and x.父类别代码='ZZMMDM'" +
			"left join dbo.V_学校班级数据子类 z on z.行政班代码=a.行政班代码";*/
		sql+=" from dbo.V_学生基本数据子类 a " +
				"left join dbo.V_信息类别_类别操作 b on a.身份证件类型码=b.描述 and b.父类别代码='SFZJLXM'"+
				"left join dbo.V_信息类别_类别操作 c on a.性别码=c.描述 and c.父类别代码='XBM'"+
				"left join V_信息类别_类别操作  d on a.招生类别=d.描述 and d.父类别代码='ZSLBM'" +
				"left join V_专业基本信息数据子类  e on a.专业代码=e.专业代码 "+
				"left join dbo.V_信息类别_类别操作 f on a.民族码=f.描述 and f.父类别代码='MZDM'" +
				"left join dbo.V_信息类别_类别操作 g on a.籍贯=g.描述  and g.父类别代码='CSDM'" +
				"left join dbo.V_信息类别_类别操作 h on a.[国籍/ 地区码]=h.描述  and h.父类别代码='GBDM'" +
				"left join dbo.V_信息类别_类别操作 i on a.政治面貌码=i.描述  and i.父类别代码='ZZMMDM'" +
				"left join dbo.V_信息类别_类别操作 j on a.港澳台侨外码=j.描述 and j.父类别代码='GATQDM'" +
				"left join dbo.V_信息类别_类别操作 k on a.户口类别码=k.描述 and k.父类别代码='HKXZM'" +
				"left join dbo.V_信息类别_类别操作 l on a.学生类别=l.描述 and l.父类别代码='XSLBM'" +
				"left join dbo.V_信息类别_类别操作 m on a.母亲政治面貌=m.描述 and m.父类别代码='ZZMMDM'" +
				"left join dbo.V_信息类别_类别操作 n on a.父亲政治面貌=n.描述 and n.父类别代码='ZZMMDM'" +
				"left join dbo.V_信息类别_类别操作 o on a.其他成员政治面貌=o.描述 and o.父类别代码='ZZMMDM'";
				if(!"".equalsIgnoreCase(bjdm)){
			sql += " where a.行政班代码='"+bjdm+"'";
		}
		vec = db.GetContextVector(sql);		
		if (vec != null && vec.size() > 0) {
			
			for(int i=0;i<vec.size();i++){
				if(vec.get(i).equals("null")){
					vec.set(i, "");
				}
				resultVec.add(MyTools.StrFiltr(vec.get(i)));
			}
		
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);

			savePath = MyTools.getProp(request, "Base.exportExcelPath");

			// 创建
			try {
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 输出的excel的路径
				// filePath +=
				// "d:\\年级学生成绩汇总信息_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)
				// + ".xls";
				// 输出流
				savePath +=   bjbh
						+ "学生信息" + year+ ((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+ (date < 10 ? "0" + date : date) + ".xls";
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				// WritableSheet wsheet =
				// wbook.createSheet(njName+""+testName+""+scienceName+"成绩分段统计信息（全部学生）",
				// 0);//工作表名称
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;

				// 设置列宽
				wsheet.setColumnView(0, 20);
				wsheet.setColumnView(1, 20);
				wsheet.setColumnView(2, 20);
				wsheet.setColumnView(3, 20);
				wsheet.setColumnView(4, 15);
				wsheet.setColumnView(5, 20);
				wsheet.setColumnView(6, 15);
				wsheet.setColumnView(7, 20);
				wsheet.setColumnView(8, 15);
				wsheet.setColumnView(9, 20);
				wsheet.setColumnView(10, 20);
				wsheet.setColumnView(11, 20);
				wsheet.setColumnView(12, 15);
				wsheet.setColumnView(13, 20);
				wsheet.setColumnView(14, 15);
				wsheet.setColumnView(15, 15);
				wsheet.setColumnView(16, 45);
				wsheet.setColumnView(17, 15);
				wsheet.setColumnView(18, 25);
				wsheet.setColumnView(19, 15);
				wsheet.setColumnView(20, 15);
				wsheet.setColumnView(21, 15);
				wsheet.setColumnView(22, 15);
				wsheet.setColumnView(23, 20);
				wsheet.setColumnView(24, 20);
				wsheet.setColumnView(25, 15);
				wsheet.setColumnView(26, 15);
				wsheet.setColumnView(27, 15);
				wsheet.setColumnView(28, 20);
				wsheet.setColumnView(29, 20);
				wsheet.setColumnView(30, 15);
				wsheet.setColumnView(31, 15);
				wsheet.setColumnView(32, 15);
				wsheet.setColumnView(33, 15);
				wsheet.setColumnView(34, 15);
				wsheet.setColumnView(35, 15);
				wsheet.setColumnView(36, 20);
				wsheet.setColumnView(37, 20);
				wsheet.setColumnView(38, 25);
				wsheet.setColumnView(39, 25);
				wsheet.setColumnView(40, 25);
				// 设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,WritableFont.BOLD);
				contentStyle=new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				//加的标题
//				wsheet.mergeCells(0,0,lieInfoArray.length-1,0);
//				content = new Label(0, 0, "学生信息（"+bjbh+"）",contentStyle);
//				wsheet.addCell(content);
//				
//				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,WritableFont.BOLD);
//				contentStyle = new WritableCellFormat(fontStyle);
//				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
//				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
//				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				
				
				
				for (int i = 0; i < lieInfoArray.length; i++) {
					// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
					// 在Label对象的子对象中指明单元格的位置和内容
					content = new Label(i, 0, lieInfoArray[i].toString(),contentStyle);
					// 将定义好的单元格添加到工作表中
					wsheet.addCell(content);
				}
				
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 10);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);

				// 填充数据
				// k:用于循环时Excel的行号
				// 外层for用于循环行,内曾for用于循环列'
				  for(int i=0,k=1; i<vec.size();i+=lieInfoArray.length,k++){ 
					  for(int j=0;j<lieInfoArray.length;j++){ 
						 content = new Label(j, k, vec.get(i+j).toString(),contentStyle);
						  wsheet.addCell(content); 
						  } 
					  }
				  
				  

				// 写入数据
				wbook.write();
				// 关闭文件
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
		} else {
			Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int date = c.get(Calendar.DATE);

			savePath = MyTools.getProp(request, "Base.exportExcelPath");

			// 创建
			try {
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				// 输出的excel的路径
				// filePath +=
				// "d:\\年级学生成绩汇总信息_"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)
				// + ".xls";
				// 输出流
				savePath += "/" + bjbh
						+ "学生信息（全部学生）" + year+ ((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+ (date < 10 ? "0" + date : date) + ".xls";
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
				// WritableSheet wsheet =
				// wbook.createSheet(njName+""+testName+""+scienceName+"成绩分段统计信息（全部学生）",
				// 0);//工作表名称
				WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 工作表名称
				WritableFont fontStyle;
				WritableCellFormat contentStyle;
				Label content;

				// 设置列宽
//				wsheet.setColumnView(0, 15);
//				wsheet.setColumnView(1, 15);
//				wsheet.setColumnView(2, 15);
//				wsheet.setColumnView(3, 20);
//				wsheet.setColumnView(4, 15);
//				wsheet.setColumnView(5, 20);
//				wsheet.setColumnView(6, 15);
//				wsheet.setColumnView(7, 20);
//				wsheet.setColumnView(8, 15);
//				wsheet.setColumnView(9, 20);
//				wsheet.setColumnView(10, 15);
//				wsheet.setColumnView(11, 20);
//				wsheet.setColumnView(12, 15);
//				wsheet.setColumnView(13, 15);
				wsheet.setColumnView(0, 20);
				wsheet.setColumnView(1, 20);
				wsheet.setColumnView(2, 20);
				wsheet.setColumnView(3, 20);
				wsheet.setColumnView(4, 15);
				wsheet.setColumnView(5, 20);
				wsheet.setColumnView(6, 15);
				wsheet.setColumnView(7, 20);
				wsheet.setColumnView(8, 15);
				wsheet.setColumnView(9, 20);
				wsheet.setColumnView(10, 20);
				wsheet.setColumnView(11, 20);
				wsheet.setColumnView(12, 15);
				wsheet.setColumnView(13, 20);
				wsheet.setColumnView(14, 15);
				wsheet.setColumnView(15, 15);
				wsheet.setColumnView(16, 45);
				wsheet.setColumnView(17, 15);
				wsheet.setColumnView(18, 25);
				wsheet.setColumnView(19, 15);
				wsheet.setColumnView(20, 15);
				wsheet.setColumnView(21, 15);
				wsheet.setColumnView(22, 15);
				wsheet.setColumnView(23, 20);
				wsheet.setColumnView(24, 20);
				wsheet.setColumnView(25, 15);
				wsheet.setColumnView(26, 15);
				wsheet.setColumnView(27, 15);
				wsheet.setColumnView(28, 20);
				wsheet.setColumnView(29, 20);
				wsheet.setColumnView(30, 15);
				wsheet.setColumnView(31, 15);
				wsheet.setColumnView(32, 15);
				wsheet.setColumnView(33, 15);
				wsheet.setColumnView(34, 15);
				wsheet.setColumnView(35, 15);
				wsheet.setColumnView(36, 20);
				wsheet.setColumnView(37, 20);
				wsheet.setColumnView(38, 25);
				wsheet.setColumnView(39, 25);
				wsheet.setColumnView(40, 25);
				// 设置title
				
				//加的标题
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				wsheet.mergeCells(0,0,lieInfoArray.length-1,0);
				content = new Label(0, 0, "学生信息（"+bjbh+"）",contentStyle);
				wsheet.addCell(content);
				
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,WritableFont.BOLD);
				contentStyle = new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				contentStyle.setBorder(Border.ALL,  BorderLineStyle.THIN,Colour.BLACK);
				
				for (int i = 0; i < lieInfoArray.length; i++) {
					// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
					// 在Label对象的子对象中指明单元格的位置和内容
					content = new Label(i, 1, lieInfoArray[i].toString(),contentStyle);
					// 将定义好的单元格添加到工作表中
					wsheet.addCell(content);
				}
				wbook.write();
				// 关闭文件
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
			 //this.setMSG("没有符合条件的成绩信息");
		}
		return savePath;
	}
	
	

	
	public  String exportSingleTimetable(HttpServletRequest request,String exportType,String JX_XJH,String BJDM,String XSLX,String XM) throws SQLException, UnsupportedEncodingException{
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
	
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		int secondTime = c.get(Calendar.SECOND);
		filePath = request.getSession().getServletContext().getRealPath("/")+"form/registerScoreManage/";
		filePath = filePath.replaceAll("\\\\", "/");
		
		
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		/*filePath +=JX_XJH+" "+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls";
		String savePath=JX_XJH+" "+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls";
		*/
		
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
				"where  c.课程类型='01' and d.学号='"+MyTools.fixSql(JX_XJH)+"'  order by b.课程代码 ";
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
				
				//查询学业水平测试成绩
				sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
					"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
					"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
					"where  a.学号='"+MyTools.fixSql(JX_XJH)+"' "+
					"and c.类别编号='03' and a.状态='1' and b.状态='1' and c.状态='1'" +
					"order by a.学号,b.课程代码,c.学年学期编码 desc";	
				xyspVec = db.GetContextVector(sql);
				
				
				for(int i=0;i<xnVec.size();i+=2){			
					sql = "select a.学号,b.课程代码 "+
							"from V_成绩管理_学生成绩信息表 a  "+
							"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
							"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
							"left join V_学生基本数据子类 d on d.学号=a.学号 "+
							"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
							"where c.学年学期编码='" +  xnVec.get(i) + "' " +
							"and  e.课程类型='01' and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
							"order by b.课程代码";
						firstVec = db.GetContextVector(sql);
				
						
						sql = "select a.学号,b.课程代码 "+
								"from V_成绩管理_学生成绩信息表 a  "+
								"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
								"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
								"left join V_学生基本数据子类 d on d.学号=a.学号 "+
								"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
								"where c.学年学期编码='" +  xnVec.get(i+1) + "' " +
								"and  e.课程类型='01' and a.学号='"+MyTools.fixSql(JX_XJH)+"' " +
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
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("".equalsIgnoreCase(firstScore) || "作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "-5".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
									
										//上学期
										}else if(first==true && second==false){
											 firstScore=zp1;
											 scoreVec.add(firstScore);
											 scoreVec.add("");
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "缓考".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											//下学期
										}else{
											   secondScore=zp1;
											   scoreVec.add("");
											   scoreVec.add(secondScore);
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if( "作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
										}
										String bk="";
										//计算总评
										if(firstFlag==true && secondFlag==true){
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											firstScore="".equalsIgnoreCase(firstScore)?"0":firstScore;
											secondScore="".equalsIgnoreCase(secondScore)?"0":secondScore;
											if(type == 0){
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*0.4+MyTools.StringToDouble(secondScore)*0.6);
											}else{
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*sxqzb/100+MyTools.StringToDouble(secondScore)*xxqzb/100);
											}
											
										}else if(firstFlag==true && secondFlag==false){
												tempXnzp=firstScore;
										}else if(firstFlag==false && secondFlag==true){
												tempXnzp=secondScore;
										}else{
												tempXnzp="";
										}
										
										scoreVec.add(tempXnzp);
										bk=tempXnzp;
										if("".equalsIgnoreCase(bk)){
											bk="0";
										}
										if(MyTools.StringToDouble(bk)<60.0){
											boolean xyspcsFlag = false;
											Vector xyspVec1=new Vector();
											if(xyspVec!=null && xyspVec.size()>0){
												for(int kk=0; kk<xyspVec.size(); kk+=4){
													String xh=MyTools.StrFiltr(xyspVec.get(kk));
													String xnxqm=MyTools.StrFiltr(xyspVec.get(kk+3));
													if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i)).equalsIgnoreCase(xnxqm)){
														for(int dd=0;dd<4;dd++){
															xyspVec1.add(xyspVec.get(kk+dd));
														}
														xyspcsFlag=true;
													}
													if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i+1)).equalsIgnoreCase(xnxqm)){
														for(int dd=0;dd<4;dd++){
															xyspVec1.add(xyspVec.get(kk+dd));
														}
														xyspcsFlag=true;
														
													}
												}
											}
											if(xyspcsFlag){
												for(int w=0;w<xyspVec1.size();w+=4){
													String xyspcj="";
													if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+1)))){
														if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))){
															xyspcj="0";
															
														}else{
															xyspcj=MyTools.StrFiltr(xyspVec1.get(w+2));
														}
														if(MyTools.StringToDouble(xyspcj)>=60.0){
															bk=xyspcj;
															break;
														}else{
															bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
														}
														
													}else{
														bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
													}
												}
												
											}else{
												bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
											}
											
										}else{
											bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
											
										}
										scoreVec.add(bk);
										//scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+9)));
										existFlag = true;
										break;
									}
							}
								//没有该课程则增加成绩信息为空
									if(!existFlag){
										scoreVec.add("");
										scoreVec.add("");
										scoreVec.add("");
										scoreVec.add("");
									}
						}
					}else{
						for(int j=0;j<subVec.size()/2;j++){
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
							scoreVec.add("");
						}
					}
				}
	
				Vector scoreVec2=new Vector();
				Vector subVec2=new Vector();
				Vector scoreInfoVec2=new Vector();
				Vector xyspVec2=new Vector();
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
					"where   c.课程类型='02' and d.学号='"+MyTools.fixSql(JX_XJH)+"'  order by b.课程代码 ";
				subVec2 = db.GetContextVector(sql);
				if(subVec2.size()==0){
					if(subVec2.size()==0){
						for(int i=0;i<14;i++){
							subVec2.add("");
						}
					}
				}
				
				int subVecSize2=(int)(subVec2.size()/2);
				if(subVecSize2<5){
						for(int j=0;j<(5-subVecSize2);j++){
							subVec2.add("");
							subVec2.add("");
							
						}
					}
				for(int i=0;i<xnVec.size();i+=2){
					
					sql = "select distinct a.学号,b.课程代码 "+
							"from V_成绩管理_学生成绩信息表 a  "+
							"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
							"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
							"left join V_学生基本数据子类 d on d.学号=a.学号 "+
							"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
							"where c.学年学期编码='" +  xnVec.get(i) + "' " +
							"and  e.课程类型='02' and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
							"order by b.课程代码";
					firstVec = db.GetContextVector(sql);
				
						
					sql = "select distinct a.学号,b.课程代码 "+
								"from V_成绩管理_学生成绩信息表 a  "+
								"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
								"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
								"left join V_学生基本数据子类 d on d.学号=a.学号 "+
								"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
								"where c.学年学期编码='" +  xnVec.get(i+1) + "' " +
								"and  e.课程类型='02' and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
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
						for(int j=0; j<subVec2.size(); j+=2){
							curSubCode = MyTools.StrFiltr(subVec2.get(j));//课程代码
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
											scoreVec2.add(firstScore);
											scoreVec2.add(secondScore);
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("".equalsIgnoreCase(firstScore) || "作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "-5".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
									
										//上学期
										}else if(first==true && second==false){
											 firstScore=zp1;
											 scoreVec2.add(firstScore);
											 scoreVec2.add("");
											if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
												firstFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if("作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "缓考".equalsIgnoreCase(firstScore)){
												firstScore = "0";
											}
											//下学期
										}else{
											   secondScore=zp1;
											   scoreVec2.add("");
											   scoreVec2.add(secondScore);
											if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
												secondFlag = true;
											}
											//判断如果是其他文字成绩，转换分数
											if( "作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
												secondScore = "0";
											}
										}
										String bk="";
										//计算总评
										if(firstFlag==true && secondFlag==true){
											String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
											String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
											Double sxqzb=MyTools.StringToDouble(SXQzb);
											Double xxqzb=MyTools.StringToDouble(XXQzb);
											firstScore="".equalsIgnoreCase(firstScore)?"0":firstScore;
											secondScore="".equalsIgnoreCase(secondScore)?"0":secondScore;
											if(type == 0){
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*0.4+MyTools.StringToDouble(secondScore)*0.6);
											}else{
												tempXnzp = f.format(MyTools.StringToDouble(firstScore)*sxqzb/100+MyTools.StringToDouble(secondScore)*xxqzb/100);
											}
											
										}else if(firstFlag==true && secondFlag==false){
												tempXnzp=firstScore;
										}else if(firstFlag==false && secondFlag==true){
												tempXnzp=secondScore;
										}else{
												tempXnzp="";
										}
										
										scoreVec2.add(tempXnzp);
										bk=tempXnzp;
										if("".equalsIgnoreCase(bk)){
											bk="0";
										}
										if(MyTools.StringToDouble(bk)<60.0){
											boolean xyspcsFlag = false;
											Vector xyspVec1=new Vector();
											if(xyspVec!=null && xyspVec.size()>0){
												for(int kk=0; kk<xyspVec.size(); kk+=4){
													String xh=MyTools.StrFiltr(xyspVec.get(kk));
													String xnxqm=MyTools.StrFiltr(xyspVec.get(kk+3));
													if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i)).equalsIgnoreCase(xnxqm)){
														for(int dd=0;dd<4;dd++){
															xyspVec1.add(xyspVec.get(kk+dd));
														}
														xyspcsFlag=true;
													}
													if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i+1)).equalsIgnoreCase(xnxqm)){
														for(int dd=0;dd<4;dd++){
															xyspVec1.add(xyspVec.get(kk+dd));
														}
														xyspcsFlag=true;
														
													}
												}
											}
											
											
											if(xyspcsFlag){
												for(int w=0;w<xyspVec1.size();w+=4){
													String xyspcj="";
													if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+1)))){
														if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))){
															xyspcj="0";
															
														}else{
															xyspcj=MyTools.StrFiltr(xyspVec1.get(w+2));
														}
														if(MyTools.StringToDouble(xyspcj)>=60.0){
															bk=xyspcj;
															break;
														}else{
															bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
														}
														
													}else{
														bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
													}
												}
												
											}else{
												bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
											}
											
										}else{
											bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
											
										}
										scoreVec2.add(bk);
										//scoreVec2.add(MyTools.StrFiltr(scoreInfoVec2.get(k+9)));
										existFlag = true;
										break;
									}
							}
								//没有该课程则增加成绩信息为空
									if(!existFlag){
										scoreVec2.add("");
										scoreVec2.add("");
										scoreVec2.add("");
										scoreVec2.add("");
									}
						}
					}else{
						for(int j=0;j<subVec2.size()/2;j++){
							scoreVec2.add("");
							scoreVec2.add("");
							scoreVec2.add("");
							scoreVec2.add("");
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
					for(int i=0;i<52;i++){
						wsheet.setRowView(i,240);//设置行高
					}
					for(int i=52;i<58;i++){
						wsheet.setRowView(i,350);//设置行高
					}
					wsheet.setColumnView(0,5);//设置列宽
					wsheet.setColumnView(1,7);//设置列宽
					for(int i=2;i<18;i++){
						wsheet.setColumnView(i,5);//设置列宽
					}
					wsheet.setColumnView(20,4);//设置列宽
					for(int i=22;i<39;i++){
						wsheet.setColumnView(i,5);//设置列宽
					}
					wsheet.setColumnView(19,4);//设置列宽
					wsheet.setColumnView(0,3);//设置列宽
					wsheet.setColumnView(1,5);//设置列宽
					wsheet.setColumnView(8,3);//设置列宽
					wsheet.setColumnView(9,4);//设置列宽
					wsheet.setColumnView(10,7);//设置列宽
					wsheet.setColumnView(13,8);//设置列宽
					wsheet.setColumnView(14,6);//设置列宽
					wsheet.setColumnView(15,4);//设置列宽
					wsheet.setColumnView(16,4);//设置列宽
					wsheet.setColumnView(17,3);//设置列宽
					wsheet.setColumnView(18,5);//设置列宽
					wsheet.setColumnView(21,6);//设置列宽
					if((60+(subVec.size()/2)+(subVec2.size()/2))<84){
						for(int i=58;i<84;i++){
							wsheet.setRowView(i,460);//设置行高
						}
					}else{
						for(int i=58;i<(60+(subVec.size()/2)+(subVec2.size()/2));i++){
							wsheet.setRowView(i,460);//设置行高
						}
					}
					
					wsheet.setRowView(59,540);//设置行高
					wsheet.setRowView(75,540);//设置行高
					wsheet.setRowView(85,500);//设置行高
					wsheet.setRowView(86,500);//设置行高
					wsheet.setRowView(87,500);//设置行高
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					contentStyle.setWrap(true);
					
					fontStyle_left = new WritableFont(
							WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle_left = new WritableCellFormat(fontStyle_left);
					contentStyle_left.setAlignment(jxl.format.Alignment.LEFT);// 左对齐
					contentStyle_left.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle_left.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle_left.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle_left.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					//contentStyle_left.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					contentStyle_left.setWrap(true);
					

					fontStyle_leftAndTopNo= new WritableFont(
							WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					fontStyle_leftAndTopNoStyle = new WritableCellFormat(fontStyle_leftAndTopNo);
					fontStyle_leftAndTopNoStyle.setAlignment(jxl.format.Alignment.LEFT);// 水平居左
					fontStyle_leftAndTopNoStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					fontStyle_leftAndTopNoStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					fontStyle_leftAndTopNoStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					fontStyle_leftAndTopNoStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					fontStyle_leftAndTopNoStyle.setWrap(true);
					
					fontStyle_right = new WritableFont(
							WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle_right = new WritableCellFormat(fontStyle_right);
					contentStyle_right.setAlignment(jxl.format.Alignment.RIGHT);// 左对齐
					contentStyle_right.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);// 垂直居中
					contentStyle_right.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle_right.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle_right.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle_right.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
					contentStyle_right.setWrap(true);
					wsheet.mergeCells(1, 0, 9, 1);
					content = new Label(1, 0, "第 一 学 期 思 想 品 德 评 语", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(10, 0, 18, 1);
					content = new Label(10, 0, "第 二 学 期 思 想 品 德 评 语", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(0, 0, 0, 13);
					content = new Label(0, 0, "一\n年\n级", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(0, 14, 0, 25);
					content = new Label(0, 14, "二\n年\n级", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(0, 26, 0, 37);
					content = new Label(0, 26, "三\n年\n级", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(0, 38, 0, 49);
					content = new Label(0, 38, "四\n年\n级", contentStyle);
					wsheet.addCell(content);
					
					
					for(int i=1;i<5;i++){
						sql=" select b.类别名称,a.评语 from   V_成绩管理_学生评语信息表 a "+
							"left join  V_信息类别_类别操作 b on a.等第=b.描述 "+	
							"where b.父类别代码='DJ' and a.学号='"+JX_XJH+"' and a.年级='"+i+"'";
							pyVec=db.GetContextVector(sql);
							if(pyVec!=null&&pyVec.size()>0){
								pySave.add(pyVec.get(0));
								pySave.add(pyVec.get(1));
							}else{
								pySave.add("");
								pySave.add("");
							}
						}
					int countPY=0;
					for(int i=2;i<50;i+=12){//1 13 25 37
							wsheet.mergeCells(1, i, 9, i+9);
							content = new Label(1, i, MyTools.StrFiltr(pySave.get(countPY++)), contentStyle_left);
							wsheet.addCell(content);
							
							wsheet.mergeCells(1, i+10, 9, i+11);
							content = new Label(1, i+10, "班主任(签名)          年    月    日", fontStyle_leftAndTopNoStyle);
							wsheet.addCell(content);
							
							wsheet.mergeCells(10, i, 18, i+9);
							content = new Label(10, i, MyTools.StrFiltr(pySave.get(countPY++)), contentStyle_left);
							wsheet.addCell(content);
							
							wsheet.mergeCells(10, i+10, 18, i+11);
							content = new Label(10, i+10, "班主任(签名)            年     月     日", fontStyle_leftAndTopNoStyle);
							wsheet.addCell(content);
						}
					wsheet.mergeCells(0, 50, 18, 53);
					content = new Label(0, 50, "毕业证书号码", fontStyle_leftAndTopNoStyle);
					wsheet.addCell(content);
					
					//第二部分
					
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 20, WritableFont.BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setWrap(true);
					
					wsheet.mergeCells(32, 1, 33, 2);
					content = new Label(32, 1, "编号", contentStyle);
					wsheet.addCell(content);
					wsheet.mergeCells(24, 10, 34, 11);
					content = new Label(24, 10, "上海市职业技术学校学生学籍卡", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(23, 20, 25, 21);
					content = new Label(23, 20, "区(县)", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(23, 25, 25, 26);
					content = new Label(23, 25, "学校", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(23, 30, 25, 31);
					content = new Label(23, 30, "学制", contentStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(23, 35, 25, 36);
					content = new Label(23, 35, "专业", contentStyle);
					wsheet.addCell(content);
					
					fontStyle = new WritableFont(
							WritableFont.createFont("宋体"), 20, WritableFont.BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
					contentStyle = new WritableCellFormat(fontStyle);
					contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
					wsheet.mergeCells(34, 1, 36, 2);
					content = new Label(34, 1, "", contentStyle);
					wsheet.addCell(content);
					wsheet.mergeCells(26, 20, 31, 21);
					content = new Label(26, 20, "长宁", contentStyle);
					wsheet.addCell(content);
					wsheet.mergeCells(26, 25, 31, 26);
					content = new Label(26, 25, "现代职校", contentStyle);
					wsheet.addCell(content);
					wsheet.mergeCells(26, 30, 31, 31);
					content = new Label(26, 30, "三", contentStyle);
					wsheet.addCell(content);
					wsheet.mergeCells(26, 35, 31, 36);
					content = new Label(26, 35, "", contentStyle);
					wsheet.addCell(content);
				
					//第三部分
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
					sql = "select a.姓名,f.类别名称,g.类别名称,h.类别名称,CONVERT(varchar(100), a.出生日期, 23),i.类别名称,a.户籍地址,户籍邮编,a.家庭电话,a.所属派出所,a.身份证件号,a.父亲,a.父亲年龄,a.父亲政治面貌,a.父亲工作单位,a.父亲电话,a.母亲,a.母亲年龄,a.母亲政治面貌,a.母亲工作单位,a.母亲电话 "+                
							",(select 校区名称 from V_校区数据类) as 学校,CONVERT(varchar(100), a.入学日期, 112) as 入校时间 ,CONVERT(varchar(100), a.毕业离校日期, 112) as 毕业离校日期,a.照片路径 "+	
							"from V_学生基本数据子类 a  "+		
							"left join dbo.V_信息类别_类别操作 c on a.学生状态=c.描述  and c.父类别代码='XSZTM'  "+	
							"left join dbo.V_信息类别_类别操作 f on a.性别码=f.描述  and f.父类别代码='XBM' "+		
							"left join dbo.V_信息类别_类别操作 g on a.民族码=g.描述  and g.父类别代码='MZDM'  "+		
							"left join dbo.V_信息类别_类别操作 h on a.籍贯=h.描述  and h.父类别代码='CSDM'  "+	
							"left join dbo.V_信息类别_类别操作 i on a.户口类别码=i.描述  and i.父类别代码='HKXZM'  "+	
							"where 1=1 " ;
							sql+=" and 学号='"+JX_XJH+"'  order by a.学籍号" ;		
					 Vec1 = db.GetContextVector(sql);
					 if(Vec1.size()==0){
						 for(int i=0;i<25;i++){
							 Vec1.add("");
						 }
					 }
					//姓名
					 wsheet.mergeCells(0, 58, 1, 58);
					 content = new Label(0, 58, "姓名", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(2, 58, 3, 58);
					 content = new Label(2, 58, MyTools.StrFiltr(Vec1.get(0)), contentStyle);
					 wsheet.addCell(content);
					
					//性别
					 wsheet.mergeCells(4, 58, 5, 58);
					 content = new Label(4, 58, "性别", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(6, 58, 7, 58);
					 content = new Label(6, 58, MyTools.StrFiltr(Vec1.get(1)), contentStyle);
					 wsheet.addCell(content);
					 
					//名族
					 wsheet.mergeCells(8, 58, 9, 58);
					 content = new Label(8, 58, "名族", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(10, 58, MyTools.StrFiltr(Vec1.get(2)), contentStyle);
					 wsheet.addCell(content);
					 
					//籍贯
					 wsheet.mergeCells(11, 58, 12, 58);
					 content = new Label(11, 58, "籍贯", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(13, 58, 14, 58);
					 content = new Label(13, 58, MyTools.StrFiltr(Vec1.get(3)), contentStyle);
					 wsheet.addCell(content);
					 
					 
					//照片
					   wsheet.mergeCells(15, 58, 18, 61);
						
					 File imgFile = new File(MyTools.getProp(request, "Base.Photo")+MyTools.StrFiltr(Vec1.get(24)));
					if (!imgFile.exists()||"".equalsIgnoreCase(MyTools.StrFiltr(Vec1.get(24)))) {
						 content = new Label(15, 58, "照片", contentStyle);
						 wsheet.addCell(content);
					}else{
						BufferedImage picImage = ImageIO.read(imgFile);  
				        // 取得图片的像素高度，宽度  
						 ByteArrayOutputStream baos = new ByteArrayOutputStream();
						 ImageIO.write(picImage, "png", baos);
						 wsheet.addImage(new WritableImage(15.1,58.18,3.8,3.72 ,baos.toByteArray()));
					 	}
					
					  WritableFont fontStyle333;
					  WritableCellFormat contentStyle333;
					 fontStyle333 = new WritableFont(
									WritableFont.createFont("宋体"), 20, WritableFont.NO_BOLD,
									false, jxl.format.UnderlineStyle.NO_UNDERLINE,
									jxl.format.Colour.BLACK);
						 contentStyle333 = new WritableCellFormat(fontStyle333);
						 contentStyle333.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
						 content = new Label(19, 58, "", contentStyle333);
						 wsheet.addCell(content);
						 content = new Label(19, 59, "", contentStyle333);
						 wsheet.addCell(content);
						 content = new Label(19, 60, "", contentStyle333);
						 wsheet.addCell(content);
						 content = new Label(19, 61, "", contentStyle333);
						 wsheet.addCell(content);
					
						
				      
					 
					
					 
					//出生年月
					 wsheet.mergeCells(0, 59, 2, 59);
					 content = new Label(0, 59, "出生年月", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 59, 7, 59);
					 content = new Label(3, 59, MyTools.StrFiltr(Vec1.get(4)), contentStyle);
					 wsheet.addCell(content);
					 
					 	//户口
					 wsheet.mergeCells(8, 59, 12, 59);
					 content = new Label(8, 59, "户口属农业户或城镇户", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(13, 59, 14, 59);
					 content = new Label(13, 59, MyTools.StrFiltr(Vec1.get(5)), contentStyle);
					 wsheet.addCell(content);
					 WritableFont fontStyle2;
					 WritableCellFormat contentStyle2;
					 Label content2;
					fontStyle2 = new WritableFont(
							WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
							contentStyle2 = new WritableCellFormat(fontStyle2);
							contentStyle2.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
							contentStyle2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
							contentStyle2.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
							contentStyle2.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
							contentStyle2.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
							contentStyle2.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
							contentStyle2.setWrap(true);
					
					//家庭住址
					 wsheet.mergeCells(0, 60, 2, 60);
					 content = new Label(0, 60, "家庭住址", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 60, 7, 60);
					 if(MyTools.StrFiltr(Vec1.get(6)).length()>14){
						 content = new Label(3, 60, MyTools.StrFiltr(Vec1.get(6)), contentStyle2);
					 }else
						 content = new Label(3, 60, MyTools.StrFiltr(Vec1.get(6)), contentStyle);
					 
					 	wsheet.addCell(content);
					
					//邮编
					 wsheet.mergeCells(8, 60, 9, 60);
					 content = new Label(8, 60, "邮编", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(10, 60, MyTools.StrFiltr(Vec1.get(7)), contentStyle);
					 wsheet.addCell(content);
					
					//电话
					 wsheet.mergeCells(11, 60, 12, 60);
					 content = new Label(11, 60, "电话", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(13, 60, 14, 60);
					 content = new Label(13, 60, MyTools.StrFiltr(Vec1.get(8)), contentStyle);
					 wsheet.addCell(content);
						
					//所属派出所
					 wsheet.mergeCells(0, 61, 2, 61);
					 content = new Label(0, 61, "所属派出所", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 61, 7, 61);
					 content = new Label(3, 61, MyTools.StrFiltr(Vec1.get(9)), contentStyle);
					 wsheet.addCell(content);
					//何时何地参加共青团
					 wsheet.mergeCells(8, 61, 12, 61);
					 content = new Label(8, 61, "何时何地参加共青团", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(13, 61, 14, 61);
					 content = new Label(13, 61,"", contentStyle);
					 wsheet.addCell(content);
					 
					 
					//身份证号码
					 wsheet.mergeCells(0, 62, 2, 62);
					 content2 = new Label(0, 62, "身份证号码", contentStyle);
					 wsheet.addCell(content2);
					 wsheet.mergeCells(3, 62, 18, 62);
					 content = new Label(3, 62, MyTools.StrFiltr(Vec1.get(10)), contentStyle);
					 wsheet.addCell(content); 
					 
					//原毕(肄)业学校
					 wsheet.mergeCells(0, 63, 2, 63);
					 content2 = new Label(0, 63, "原毕(肄)业学校", contentStyle2);
					 wsheet.addCell(content2);
					 wsheet.mergeCells(3, 63, 10, 63);
					 content = new Label(3, 63, MyTools.StrFiltr(Vec1.get(21)), contentStyle);
					 wsheet.addCell(content); 
					 
					//入学时间
					 wsheet.mergeCells(11, 63, 14, 63);
					 content = new Label(11, 63, "入学时间", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(15, 63, 18, 63);
				//	 content = new Label(15, 63,(MyTools.StrFiltr(Vec1.get(22)).substring(0,4)+"年"+(MyTools.StrFiltr(Vec1.get(22)).substring(4,6)+"月", contentStyle);
					if("".equalsIgnoreCase(MyTools.StrFiltr(Vec1.get(22)))){
						 content = new Label(15, 63,"  年     月", contentStyle);
					}else
						content = new Label(15, 63,MyTools.StrFiltr(Vec1.get(22)).substring(0,4)+"年"+MyTools.StrFiltr(Vec1.get(22)).substring(4,6)+"月", contentStyle);
						
					 wsheet.addCell(content); 
					 
					 
					//转学
					sql="select top 1  异动日期+CAST(异动说明 as varchar ),异动去向学校码 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='11' order by  异动日期 desc ";
					Vec2 = db.GetContextVector(sql);

					if(Vec2.size()==0){
						Vec2.add("");
						Vec2.add("");
					}
					//转学日期及原因
					 wsheet.mergeCells(0, 64, 2, 64);
					 content2 = new Label(0, 64, "转学日期及原因", contentStyle2);
					 wsheet.addCell(content2);
					 wsheet.mergeCells(3, 64, 10, 64);
					 content = new Label(3, 64, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
					 wsheet.addCell(content);
					 
					//去向
					 wsheet.mergeCells(11, 64, 12, 64);
					 content = new Label(11, 64, "去向", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(13, 64, 18, 64);
					 content = new Label(13, 64, MyTools.StrFiltr(Vec2.get(1)), contentStyle);
					 wsheet.addCell(content); 
					 
					//退学日期及原因
					//退学
					sql="select top 1  异动日期+CAST(异动说明 as varchar ),异动去向学校码 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='10' order by  异动日期 desc ";
					
					Vec2 = db.GetContextVector(sql);
					if(Vec2.size()==0){
						Vec2.add("");
						Vec2.add("");
					}
					 
					//退学日期及原因
					 wsheet.mergeCells(0, 65, 2, 65);
					 content2 = new Label(0, 65, "退学日期及原因", contentStyle2);
					 wsheet.addCell(content2);
					 wsheet.mergeCells(3, 65, 10, 65);
					 content = new Label(3, 65, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
					 wsheet.addCell(content); 
					 
					//去向
					 wsheet.mergeCells(11, 65, 12, 65);
					 content = new Label(11, 65, "去向", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(13, 65, 18, 65);
					 content = new Label(13, 65, MyTools.StrFiltr(Vec2.get(1)), contentStyle);
					 wsheet.addCell(content); 
					 
					//休学
					sql="select top 1  异动日期+CAST(异动说明 as varchar ),异动去向学校码 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='08' order by  异动日期 desc ";
					Vec2 = db.GetContextVector(sql);

					if(Vec2.size()==0){
						Vec2.add("");
						Vec2.add("");
					} 
					//休学日期及原因
					 wsheet.mergeCells(0, 66, 2, 66);
					 content2 = new Label(0, 66, "休学日期及原因", contentStyle2);
					 wsheet.addCell(content2);
					 wsheet.mergeCells(3, 66, 10, 66);
					 content = new Label(3, 66, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
					 wsheet.addCell(content); 
					 
					//去向
					 wsheet.mergeCells(11, 66, 12, 66);
					 content = new Label(11, 66, "去向", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(13, 66, 18, 66);
					 content = new Label(13, 66, MyTools.StrFiltr(Vec2.get(1)), contentStyle);
					 wsheet.addCell(content); 
					 
					 
					//复学日期
					sql="select top 1  异动日期 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='08' order by  异动日期 desc ";
					Vec2 = db.GetContextVector(sql);

					if(Vec2.size()==0){
						Vec2.add("");
						Vec2.add("");
					} 
					//休学日期及原因
					 wsheet.mergeCells(0, 67, 2, 67);
					 content = new Label(0, 67, "复学日期", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 67, 10, 67);
					 content = new Label(3, 67, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
					 wsheet.addCell(content); 
					 
					//去向
					 wsheet.mergeCells(11, 67, 14, 67);
					 content = new Label(11, 67, "毕业日期", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(15, 67, 18, 67);
					 content = new Label(15, 67, "  年     月", contentStyle);
					 wsheet.addCell(content);
					 
					//离校去向
					 wsheet.mergeCells(0, 68, 2, 68);
					 content = new Label(0, 68, "离校去向", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 68, 18, 68);
					 content = new Label(3, 68, "", contentStyle);
					 wsheet.addCell(content);	 
					 
					//家庭主要成员
					 wsheet.mergeCells(0, 69, 0, 73);
					 content = new Label(0, 69, "家\n庭\n主\n要\n成\n员", contentStyle);
					 wsheet.addCell(content);
					 
					//称呼
					 wsheet.mergeCells(1, 69, 2, 69);
					 content = new Label(1, 69, "称呼", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 69, 4, 69);
					 content = new Label(3, 69, "姓名", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 69, "年龄", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(6, 69, 9, 69);
					 content = new Label(6, 69, "政治面貌", contentStyle);
					 wsheet.addCell(content); 
					 wsheet.mergeCells(10, 69, 15, 69);
					 content = new Label(10, 69, "工作单位职务及地址", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(16, 69, 18, 69);
					 content = new Label(16, 69, "电话", contentStyle);
					 wsheet.addCell(content);
					 
					 
					//父亲
					 wsheet.mergeCells(1, 70, 2, 70);
					 content = new Label(1, 70, "父亲", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 70, 4, 70);
					 content = new Label(3, 70, MyTools.StrFiltr(Vec1.get(11)), contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 70, MyTools.StrFiltr(Vec1.get(12)), contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(6, 70, 9, 70);
					 content = new Label(6, 70, MyTools.StrFiltr(Vec1.get(13)), contentStyle);
					 wsheet.addCell(content); 
					 wsheet.mergeCells(10, 70, 15, 70);
					 content = new Label(10, 70, MyTools.StrFiltr(Vec1.get(14)), contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(16, 70, 18, 70);
					 content = new Label(16, 70, MyTools.StrFiltr(Vec1.get(15)), contentStyle);
					 wsheet.addCell(content);
					 
					//母亲
					 wsheet.mergeCells(1, 71, 2, 71);
					 content = new Label(1, 71, "母亲", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 71, 4, 71);
					 content = new Label(3, 71, MyTools.StrFiltr(Vec1.get(16)), contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 71, MyTools.StrFiltr(Vec1.get(17)), contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(6, 71, 9, 71);
					 content = new Label(6, 71, MyTools.StrFiltr(Vec1.get(18)), contentStyle);
					 wsheet.addCell(content); 
					 wsheet.mergeCells(10, 71, 15, 71);
					 content = new Label(10, 71, MyTools.StrFiltr(Vec1.get(19)), contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(16, 71, 18, 71);
					 content = new Label(16, 71, MyTools.StrFiltr(Vec1.get(20)), contentStyle);
					 wsheet.addCell(content);
				
					 wsheet.mergeCells(1, 72, 2, 72);
					 content = new Label(1, 72, "", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 72, 4, 72);
					 content = new Label(3, 72, "", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 72,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(6, 72, 9, 72);
					 content = new Label(6, 72, "", contentStyle);
					 wsheet.addCell(content); 
					 wsheet.mergeCells(10, 72, 15, 72);
					 content = new Label(10, 72, "", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(16, 72, 18, 72);
					 content = new Label(16, 72,"", contentStyle);
					 wsheet.addCell(content);
					
					 wsheet.mergeCells(1, 73, 2, 73);
					 content = new Label(1, 73, "", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(3, 73, 4, 73);
					 content = new Label(3, 73, "", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 73,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(6, 73, 9, 73);
					 content = new Label(6, 73, "", contentStyle);
					 wsheet.addCell(content); 
					 wsheet.mergeCells(10, 73, 15, 73);
					 content = new Label(10, 73, "", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(16, 73, 18, 73);
					 content = new Label(16, 73,"", contentStyle);
					 wsheet.addCell(content);
					 
					//空白
					 wsheet.mergeCells(0, 74, 2, 75);
					 content = new Label(0, 74, "", contentStyle);
					 wsheet.addCell(content);
					 
					//考勤统计
					 wsheet.mergeCells(3, 74, 7, 74);
					 content = new Label(3, 74, "考勤统计", contentStyle);
					 wsheet.addCell(content);
					 
					
					 content = new Label(3, 75, "事假\n(天)", contentStyle);
					 wsheet.addCell(content);
					 
					 content = new Label(4, 75, "病假\n(天)", contentStyle);
					 wsheet.addCell(content);
					 
					 content = new Label(5, 75, "旷课\n(节)", contentStyle);
					 wsheet.addCell(content);
					 
					 content = new Label(6, 75, "迟到\n(次)", contentStyle);
					 wsheet.addCell(content);
					 
					 content = new Label(7, 75, "早退\n(次)", contentStyle);
					 wsheet.addCell(content);
					 
					//担任社会工作
					 wsheet.mergeCells(8, 74, 10, 75);
					 content = new Label(8, 74,"担任社会工作", contentStyle);
					 wsheet.addCell(content);
					 
					//何时何地何故受到何种奖\n励或处分，何时撤销处分
					 wsheet.mergeCells(11, 74, 18, 75);
					 content = new Label(11, 74,"何时何地何故受到何种奖\n励或处分，何时撤销处分", contentStyle);
					 wsheet.addCell(content);
					 
					//一年级
					 wsheet.mergeCells(0, 76, 0, 77);
					 content = new Label(0, 76,"一\n年\n级", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 76, 2, 76);
					 content = new Label(1, 76,"第一学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 76,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 76,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 76,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 76,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 76,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 76, 10, 76);
					 content = new Label(8, 76,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 76, 18, 76);
					 content = new Label(11, 76,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 77, 2, 77);
					 content = new Label(1, 77,"第二学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 77,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 77,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 77,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 77,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 77,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 77, 10, 77);
					 content = new Label(8, 77,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 77, 18, 77);
					 content = new Label(11, 77,"", contentStyle);
					 wsheet.addCell(content);
					 
				 
					//二年级
					 wsheet.mergeCells(0, 78, 0, 79);
					 content = new Label(0, 78,"二\n年\n级", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 78, 2, 78);
					 content = new Label(1, 78,"第一学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 78,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 78,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 78,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 78,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 78,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 78, 10, 78);
					 content = new Label(8, 78,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 78, 18, 78);
					 content = new Label(11, 78,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 79, 2, 79);
					 content = new Label(1, 79,"第二学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 79,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 79,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 79,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 79,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 79,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 79, 10, 79);
					 content = new Label(8, 79,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 79, 18, 79);
					 content = new Label(11, 79,"", contentStyle);
					 wsheet.addCell(content);
					 
					//三年级
					 wsheet.mergeCells(0, 80, 0, 81);
					 content = new Label(0, 80,"三\n年\n级", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 80, 2, 80);
					 content = new Label(1, 80,"第一学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 80,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 80,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 80,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 80,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 80,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 80, 10, 80);
					 content = new Label(8, 80,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 80, 18, 80);
					 content = new Label(11, 80,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 81, 2, 81);
					 content = new Label(1, 81,"第二学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 81,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 81,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 81,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 81,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 81,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 81, 10, 81);
					 content = new Label(8, 81,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 81, 18, 81);
					 content = new Label(11, 81,"", contentStyle);
					 wsheet.addCell(content);
					 
					//四年级
					 wsheet.mergeCells(0, 82, 0, 83);
					 content = new Label(0, 82,"四\n年\n级", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 82, 2, 82);
					 content = new Label(1, 82,"第一学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 82,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 82,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 82,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 82,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 82,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 82, 10, 82);
					 content = new Label(8, 82,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 82, 18, 82);
					 content = new Label(11, 82,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(1, 83, 2, 83);
					 content = new Label(1, 83,"第二学期", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(3, 83,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(4, 83,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(5, 83,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(6, 83,"", contentStyle);
					 wsheet.addCell(content);
					 content = new Label(7, 83,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(8, 83, 10, 83);
					 content = new Label(8, 83,"", contentStyle);
					 wsheet.addCell(content);
					 wsheet.mergeCells(11, 83, 18, 83);
					 content = new Label(11, 83,"", contentStyle);
					 wsheet.addCell(content);
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
					
					
					
					 wsheet.mergeCells(20, 58, 22, 59);
					 content = new Label(20, 58,"", contentStyle);
					 wsheet.addCell(content);
					//年级 
					count=0;
					for(int i=23;i<39;i+=4){
						 wsheet.mergeCells(i, 58, (i+3), 58);
						 content = new Label(i, 58,nj[count++]+"年级", contentStyle);
						 wsheet.addCell(content);
						 
						 content = new Label(i, 59,"第一\n学期", contentStyle);
						 wsheet.addCell(content);
						 content = new Label(i+1, 59,"第二\n学期", contentStyle);
						 wsheet.addCell(content);
						 content = new Label(i+2, 59,"学年\n总评", contentStyle);
						 wsheet.addCell(content);
						 content = new Label(i+3, 59,"补\n考", contentStyle);
						 wsheet.addCell(content);
					}
					//文化课学业成绩
					wsheet.mergeCells(20, 60, 20, (60+(subVec.size()/2-1)));
					content = new Label(20, 60,"文\n化\n课\n学\n业\n成\n绩", contentStyle);
					wsheet.addCell(content);
					int kemu=(int)(subVec.size()/2)+(int)(subVec2.size()/2);
					for(int i=0;i<kemu;i++){
						wsheet.mergeCells(21, (60+i), 22, (60+i));
					}
					count=60;
					WritableFont fontStyle3;
					WritableCellFormat contentStyle3;
					fontStyle3 = new WritableFont(
							WritableFont.createFont("宋体"), 8, WritableFont.NO_BOLD,
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
							wsheet.setRowView(count,560);//设置行高
							content = new Label(21, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle3);
							wsheet.addCell(content);
						}else if(MyTools.StrFiltr(subVec.get(i+1)).length()>5){
							content = new Label(21, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle4);
							wsheet.addCell(content);	
						}else{
							content = new Label(21, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle);
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
					
					//遍历文化课成绩
					count=0;
					String fs="";
					double fsz=0;
					for(int i=0;i<16;i+=4){
						for(int k=0;k<subVec.size()/2;k++){
							if(count<scoreVec.size()){
									fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(23+i, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
									}else
								 	content = new Label(23+i, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
								 	wsheet.addCell(content);	
								 	
								 	count+=1;
								 	fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(23+i+1, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
									}else
								 	content = new Label(23+i+1, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
								 	wsheet.addCell(content);	
								 	
									count+=1;
									fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(23+i+2, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
									}else
									content = new Label(23+i+2, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);	
									wsheet.addCell(content);	
									count+=1;
									fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
									fsz=MyTools.StringToDouble(fs);
									if("不及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec.get(count)))){
										content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)),  contentStyle_red2);
									}else if("及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec.get(count)))){
										content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle4);	
									}else if(fsz<60){
										content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red2);	
									}else
									content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle4);	
									wsheet.addCell(content);	
									count+=1;
								}
						}
					}
					
					//专业课学业成绩
					wsheet.mergeCells(20, (60+(subVec.size()/2-1)+1), 20, (60+(subVec.size()/2-1)+1+(subVec2.size()/2-1)));
					content = new Label(20, (60+(subVec.size()/2-1)+1),"专\n业\n课\n学\n业\n成\n绩", contentStyle);
					wsheet.addCell(content);
					
					count=60+(subVec.size()/2-1)+1;
					for(int i=0;i<subVec2.size();i+=2){			
						if(MyTools.StrFiltr(subVec2.get(i+1)).length()>10){
							wsheet.setRowView(count,560);//设置行高
							content = new Label(21, count++,MyTools.StrFiltr(subVec2.get(i+1)), contentStyle3);
							wsheet.addCell(content);
						}else if(MyTools.StrFiltr(subVec2.get(i+1)).length()>5){
							content = new Label(21, count++,MyTools.StrFiltr(subVec2.get(i+1)), contentStyle4);
							wsheet.addCell(content);	
						}else{
							content = new Label(21, count++,MyTools.StrFiltr(subVec2.get(i+1)), contentStyle);
							wsheet.addCell(content);
							}			
					}
					//遍历专业课学业成绩
					count=0;
					for(int i=0;i<16;i+=4){
						for(int k=0;k<subVec2.size()/2;k++){
							if(count<scoreVec2.size()){
									fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(23+i, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red1);
									}else
									content = new Label(23+i, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle);
								 	wsheet.addCell(content);	
								 	
								 	count+=1;
									fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(23+i+1, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red1);
									}else
								 	content = new Label(23+i+1,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle);
								 	wsheet.addCell(content);	
								 	
									count+=1;
									fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
									fsz=MyTools.StringToDouble(fs);
									if(fsz<60){
										content = new Label(23+i+2, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red1);
									}else
									content = new Label(23+i+2,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle);	
									wsheet.addCell(content);	
									
									count+=1;
									fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
									fsz=MyTools.StringToDouble(fs);
									if("不及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec2.get(count)))){
										content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red2);
									}else if("及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec2.get(count)))){
										content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle4);	
									}else if(fsz<60){
										content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red2);	
									}else 
										content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle4);	
									wsheet.addCell(content);	
									count+=1;
								}
						}
					}
					//实习情况
					int cellnum=0;
					if((60+subVec.size()/2+subVec2.size()/2)<82){
						cellnum=84;
					}else{
						cellnum=(61+subVec.size()/2+subVec2.size()/2+3);
					}
					
					 wsheet.mergeCells(20, (60+(subVec.size()/2)+(subVec2.size()/2)), 20, cellnum);
					 content = new Label(20, (60+(subVec.size()/2)+(subVec2.size()/2)),"实\n习\n状\n况", contentStyle);
					 wsheet.addCell(content);
					
					 wsheet.mergeCells(21, (60+(subVec.size()/2)+(subVec2.size()/2)), 38, cellnum);
					 content = new Label(21, (60+(subVec.size()/2)+(subVec2.size()/2)),"", contentStyle);
					 wsheet.addCell(content);
					
				
				   
				
			
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
	
	
public String exportScoreInfo(HttpServletRequest request,String XBDM,String NJDM,String SSZY,String BJBH) throws SQLException, IOException{	
	DBSource db = new DBSource(request); //数据库对象
	String sql="";
	String filePath="";
	String XM="学籍卡";
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
		sql="select 学号 from V_学生基本数据子类 where 行政班代码='"+BJVec.get(i)+"' order by 学号";
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
			"where   c.课程类型='01' and d.学号 in ('" +XSXH + "')  " +
			"order by d.学号,b.课程代码 ";
			subWhVec = db.GetContextVector(sql);
	
			//获取所有专业课的课程
			sql="with tempClassInfo as ("+
				"select distinct b.课程代码,b.课程名称 ,b.行政班代码,a.学号  "+	
				"from V_成绩管理_学生成绩信息表 a  "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +			
				"left join V_学生基本数据子类 d on d.学号=a.学号  "+						
				"where   a.学号 in ('" +XSXH + "')	) "+			
				"select distinct b.课程代码,b.课程名称,d.学号  "+
				"from  V_规则管理_授课计划主表 a   "+
				"left join V_规则管理_授课计划明细表 b on a.授课计划主表编号=b.授课计划主表编号    "+
				"left join V_课程数据子类 c on c.课程号=b.课程代码   "+
				"left join tempClassInfo d on d.课程代码=b.课程代码 and d.行政班代码=a.行政班代码 "+
				"where   c.课程类型='02' and d.学号 in ('" +XSXH + "')  " +
				"order by d.学号,b.课程代码 ";
			subZyVec = db.GetContextVector(sql);
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
				

				//查询学业水平测试成绩
				sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
					"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
					"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
					"where  a.学号 in ('" +XSXH + "') "+
					"and c.类别编号='03' and a.状态='1' and b.状态='1' and c.状态='1'" +
					"order by a.学号,b.课程代码,c.学年学期编码 desc";	
				xyspVec = db.GetContextVector(sql);
				int count=0;
				if(XhAndBj!=null&&XhAndBj.size()>0){
					for(int i=0;i<XhAndBj.size();i+=2){
						exportScoreInfo_2(wbook,MyTools.StrFiltr(XhAndBj.get(i)),MyTools.StrFiltr(XhAndBj.get(i+1)) ,count++,"",scoreInfoVec,xyspVec,subWhVec,subZyVec);
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
	
	/**
	 * createTime:2017.08.25
	 * createUser:zhaixuchao
	 * description:学生上传照片
	 */
//	public void uploadAllstuPhoto(ServletConfig sc,HttpServletResponse response) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
//		SmartUpload su = new SmartUpload("UTF-8");
//		try {
//			//初始化
//			su.initialize(sc, request, response);
//			su.upload();
//			//获取配置路径
//			String savePath = MyTools.getProp(request, "Base.Photo");
//			//String url = savePath + "/studentPhoto/" ;
//			String url = savePath;
//			File file = new File(url);
//			// 根据配置路径来判断没有文件则创建文件
//			if (!file.exists()) {
//				file.mkdirs();
//			}
//			//删除该用户名原来的文件
//			File folder = new File(url);//d:/upload/headPhoto/
//			File temp=null;
//			File[] filelist= folder.listFiles();//列出文件里所有的文件
//			int loc = 0;
//			for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
//				temp=filelist[i];
//				loc = temp.getName().indexOf(getUSERCODE());//获取用户名字符的位置
//				if(loc!=-1){//去掉后缀，如果文件名为用户名的话就删除
//					//temp.delete();//删除文件
//				}
//			}
//			//文件路径
//			String path= unescape(su.getRequest().getParameter("pathPL"));
//			
//			File f=new File(path);
//			//获取文件名称
//			String filename=f.getName();
//			//获取文件后缀名
//			String firname=filename.substring(0,filename.lastIndexOf("."));
//			
//			String prefix=filename.substring(filename.lastIndexOf(".")+1);
//			//File oldFile=new File(url+filename);
//			/*System.out.println("bean:修改前文件名称是："+oldFile.getName());//2041089_822249.jpg
//			System.out.println("bean:用户名:"+getUSERCODE());//120150002
//			System.out.println("bean:文件后缀名"+"."+prefix);//.jpg
//*/			SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//			Date newdate=new Date();//给图片加时间为防止页面图片不刷新
//			String temp1=form.format(newdate);
//			//文件重命名，改成用户名
//			//oldFile.renameTo(new File(url+filename));//
//			//System.out.println("bean:修改后文件名称是："+url+"/"+iUSERCODE+temp1+"."+prefix);
//			
//			//存放路径
//			su.getFiles().getFile(0).saveAs(url+filename);//
//			//解压压缩文件
//			File zipFile=new File(url+filename);
//			File zipDir=new File(url+firname);
//			if(prefix.equalsIgnoreCase("zip")){
//				unzip(url+filename,url+firname);
//			}else if(prefix.equalsIgnoreCase("rar")){
//				unrar(zipFile,zipDir);
//			}
//			//将文件数据插入数据库中
//			String sqlpic="";
//			int uploadstunum=0;
//			int samestunum=0;
//			int sameflag=0;
//			String errorname="";
//			String showmsg="";
//			
//			File readDir=new File(url+firname+"/"+firname);
//			Vector vecpic=new Vector();
//			
//			String sql="select [学号],学籍号 from dbo.V_学生基本数据子类 ";
//			Vector vec=db.GetContextVector(sql);
//			File[] showfile=readDir.listFiles();
//			for(File photofile:showfile){
//				sameflag=0;
//				for(int i=0;i<vec.size();i+=2){
//					//学号和图片名称相同
//					if(vec.get(i).toString().equalsIgnoreCase(photofile.getName().substring(0, photofile.getName().indexOf(".")))||vec.get(i+1).toString().equalsIgnoreCase(photofile.getName().substring(0, photofile.getName().indexOf(".")))){
//						samestunum++;
//						sameflag=1;
//					}else{
//					
//					}
//				}
//				if(sameflag==0){
//					errorname+=photofile.getName().substring(0, photofile.getName().indexOf("."))+",";
//				}
//				
//				uploadstunum++;
//				String photoname=photofile.getName();
//				String photoprefix=photoname.substring(photoname.indexOf(".")+1,photoname.length());
//				sqlpic=" update dbo.V_学生基本数据子类 set [照片路径]='"+MyTools.fixSql(photoname.substring(0, photoname.indexOf("."))+"-"+temp1+"."+photoprefix)+"' where 学号='"+MyTools.fixSql(photoname.substring(0, photoname.indexOf(".")))+"' or 学籍号='" + MyTools.fixSql(photoname.substring(0, photoname.indexOf("."))) + "' ";
//				vecpic.add(sqlpic);
//				
//				//将文件另存为filename+时间+prefix形式
//				File oldfile=new File(url+firname+"/"+filename.substring(0,filename.indexOf("."))+"/"+photoname);
//				File newfile=new File(url+photoname.substring(0, photoname.indexOf("."))+"-"+temp1+"."+photoprefix);
//				oldfile.renameTo(newfile);
//			}
//			if(!errorname.equals("")){
//				errorname=errorname.substring(0, errorname.length()-1);
//				showmsg="上传成功<br />共上传 "+uploadstunum+" 张照片<br />匹配成功 "+samestunum+" 张照片<br />"+errorname+"未匹配成功";
//			}else{
//				showmsg="上传成功<br />共上传 "+uploadstunum+" 张照片<br />匹配成功 "+samestunum+" 张照片";
//			}
//
//			readDir.delete();
//			zipDir.delete();//删除解压的文件夹
//			zipFile.delete();//删除压缩文件
//			
//			if(db.executeInsertOrUpdateTransaction(vecpic)){
//				this.setMSG(showmsg);
//			}else{
//				this.setMSG("上传失败");
//			}			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////			System.out.println("上传失败");
//			//this.setMsg("上传失败");
//		}
//	}
	
	/**
	 * createTime:2017.08.25
	 * createUser:zhaixuchao
	 * description:学生上传照片
	 */
	public void uploadAllstuPhoto(ServletConfig sc,HttpServletResponse response) throws SQLException, ServletException, IOException, SmartUploadException, WrongSQLException{
		SmartUpload su = new SmartUpload("UTF-8");
		try {
			//初始化
			su.initialize(sc, request, response);
			su.upload();
			//获取配置路径
			String savePath = MyTools.getProp(request, "Base.Photo");
			//String url = savePath + "/studentPhoto/" ;
			String url = savePath;
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
			//文件路径
			String path= unescape(su.getRequest().getParameter("pathPL"));
			
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
			int photofix=0;
			
			//File readDir=new File(url+firname+"/"+firname);
			File readDir=new File(url+firname+"/"+firname);
			Vector vecpic=new Vector();
			
			String sql="select distinct 身份证件号 from dbo.V_学生基本数据子类 ";
			Vector vec=db.GetContextVector(sql);
			File[] showfile=readDir.listFiles();
			
			String gjd="";//根节点
			
			for(File photofile:showfile){
				if (photofile.isDirectory()) {
					File[] showfile2=photofile.listFiles();
					for(File photofile2:showfile2){
						sameflag=0;
						gjd=photofile.getParentFile().getParentFile().toString().replace("\\", "\\\\");//根节点
						
						String sfz=photofile2.getName().substring(0, photofile2.getName().indexOf("."));
						delXTZP(sfz,url);
						File oldfile=new File("");
						File newfile=new File("");
						//=====================先判断再修改名字
						String photoname=photofile2.getName();
						String photoprefix=photoname.substring(photoname.indexOf(".")+1,photoname.length());
						if(photoprefix.equalsIgnoreCase("png")|photoprefix.equalsIgnoreCase("gif")|photoprefix.equalsIgnoreCase("jpeg")|photoprefix.equalsIgnoreCase("jpg")|photoprefix.equalsIgnoreCase("bmp")){
							for(int i=0;i<vec.size();i++){
								//身份证件号和图片名称相同
								if(vec.get(i).toString().equalsIgnoreCase(photofile2.getName().substring(0, photofile2.getName().indexOf(".")))){
									samestunum++;
									sameflag=1;
								}
							}
							if(sameflag==0){
								errorname+=photofile2.getName().substring(0, photofile2.getName().indexOf("."))+",";
							}
							
							//将文件另存为filename+时间+prefix形式
							oldfile=new File(photofile2.toString().replace("\\", "\\\\"));
							newfile=new File(photofile2.toString().substring(0,photofile2.toString().indexOf("."))+"-"+temp1+"."+photofile2.toString().substring(photofile2.toString().indexOf(".")+1,photofile2.toString().length()));
							oldfile.renameTo(newfile);
							sqlpic=" update dbo.V_学生基本数据子类 set [照片路径]='"+MyTools.fixSql(newfile.getName())+"' where 身份证件号='"+MyTools.fixSql(photoname.substring(0, photoname.indexOf(".")))+"' ";
							vecpic.add(sqlpic);
							
						}else{
							photofix++;
						}
						cutGeneralFile(newfile.toString().replace("\\", "\\\\"), url.replace("\\", "\\\\"));
						uploadstunum++;
					 }
				} else {
					sameflag=0;	
					gjd=photofile.getParentFile().toString().replace("\\", "\\\\");//根节点
					String sfz=photofile.getName().substring(0, photofile.getName().indexOf("."));
					delXTZP(sfz,url);
					
					File oldfile=new File("");
					File newfile=new File("");
					//=====================先判断再修改名字
					String photoname=photofile.getName();
					String photoprefix=photoname.substring(photoname.indexOf(".")+1,photoname.length());
					if(photoprefix.equalsIgnoreCase("png")|photoprefix.equalsIgnoreCase("gif")|photoprefix.equalsIgnoreCase("jpeg")|photoprefix.equalsIgnoreCase("jpg")|photoprefix.equalsIgnoreCase("bmp")){
						for(int i=0;i<vec.size();i++){
							//身份证件号和图片名称相同
							if(vec.get(i).toString().equalsIgnoreCase(photofile.getName().substring(0, photofile.getName().indexOf(".")))){
								samestunum++;
								sameflag=1;
							}
						}
						if(sameflag==0){
							errorname+=photofile.getName().substring(0, photofile.getName().indexOf("."))+",";
						}
						
						//将文件另存为filename+时间+prefix形式
						oldfile=new File(photofile.toString().replace("\\", "\\\\"));
						newfile=new File(photofile.toString().substring(0,photofile.toString().indexOf("."))+"-"+temp1+"."+photofile.toString().substring(photofile.toString().indexOf(".")+1,photofile.toString().length()));
						oldfile.renameTo(newfile);
						sqlpic=" update dbo.V_学生基本数据子类 set [照片路径]='"+MyTools.fixSql(newfile.getName())+"' where 身份证件号='"+MyTools.fixSql(photoname.substring(0, photoname.indexOf(".")))+"' ";
						vecpic.add(sqlpic);
						
					}else{
						photofix++;
					}
					cutGeneralFile(newfile.toString().replace("\\", "\\\\"), url.replace("\\", "\\\\"));
					uploadstunum++;
				}
			}
			
			readDir=new File(url);
			readDir.delete();
			zipDir.delete();//删除解压的文件夹
			zipFile.delete();//删除压缩文件
			showfile=readDir.listFiles();
			System.out.println(url.replace("\\", "\\\\")+firname);
			deleteGeneralFile(url.toString().replace("\\", "\\\\")+firname);//删除文件
			
			/*for(File photofile:showfile){
				if (photofile.isDirectory()) {
					deleteGeneralFile(photofile.toString().replace("\\", "\\\\"));//删除文件
				}
			}*/
			
			
		
			if(!errorname.equals("")){
				errorname=errorname.substring(0, errorname.length()-1);
				showmsg="上传成功<br />共上传 "+uploadstunum+" 张照片<br />匹配成功 "+samestunum+" 张照片<br />"+errorname+"未匹配成功";
			}else{
				showmsg="上传成功<br />共上传 "+uploadstunum+" 张照片<br />匹配成功 "+samestunum+" 张照片";
			}
			
			if(photofix>0){
				showmsg+=",其中"+photofix+"张照片格式不正确";
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
		}
	}
	
	

	public boolean deleteGeneralFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) { // 文件不存在
			System.out.println("要删除的文件不存在！");
		}
		if (file.isDirectory()) { // 如果是目录，则单独处理
			flag = deleteDirectory(file.getAbsolutePath());
		} else if (file.isFile()) {
			flag = deleteFile(file);
		}
		if (flag) {
			System.out.println("删除文件或文件夹成功!");
		}
			return flag;
	}
	/**
	 * 删除文件
	 * @param file
	 * @return boolean
	 */
	private boolean deleteFile(File file) {
		return file.delete();
	}
	/**
	 * 删除目录及其下面的所有子文件和子文件夹，注意一个目录下如果还有其他文件或文件夹
	 * 则直接调用delete方法是不行的，必须待其子文件和子文件夹完全删除了才能够调用delete
	 * @param path
	 *            path为该目录的路径
	 */
	private boolean deleteDirectory(String path) {
		boolean flag = true;
		File dirFile = new File(path);
		if (!dirFile.isDirectory()) {
			return flag;
		}
		File[] files = dirFile.listFiles();
		for (File file : files) { // 删除该文件夹下的文件和文件夹
			// Delete file.
			if (file.isFile()) {
				flag = deleteFile(file);
			} else if (file.isDirectory()) {// Delete folder
				flag = deleteDirectory(file.getAbsolutePath());
			}
			if (!flag) { // 只要有一个失败就立刻不再继续
				break;
			}
		}
		flag = dirFile.delete(); // 删除空目录
		return flag;
	}
	/**
	 * 由上面方法延伸出剪切方法：复制+删除
	 * @param  destDir 同上
	 */
	public boolean cutGeneralFile(String srcPath, String destDir) {
		if(!copyGeneralFile(srcPath, destDir)) {
			//System.out.println("复制失败导致剪切失败!");
			return false;
		}
		if(!deleteGeneralFile(srcPath)) {
			//System.out.println("删除源文件(文件夹)失败导致剪切失败!");
			return false;
		}
		//System.out.println("剪切成功!");
		return true;
	}
	/**
	 * 复制文件或文件夹
	 * @param srcPath
	 * @param destDir 目标文件所在的目录
	 * @return
	 */
	public boolean copyGeneralFile(String srcPath, String destDir) {
		boolean flag = false;
		File file = new File(srcPath);
		if(!file.exists()) {
			//System.out.println("源文件或源文件夹不存在!");
			return false;
		}
		if(file.isFile()) {	//源文件
			//System.out.println("下面进行文件复制!");
			flag = copyFile(srcPath, destDir);
		}

		else if(file.isDirectory()) {
			//System.out.println("下面进行文件夹复制!");

			flag = copyDirectory(srcPath, destDir);

		}
		return flag;

	}
	private String getDirName(String dir) {
		if(dir.endsWith(File.separator)) {	//如果文件夹路径以"\\"结尾，则先去除末尾的"\\"
			dir = dir.substring(0, dir.lastIndexOf(File.separator));
		}
		return dir.substring(dir.lastIndexOf(File.separator)+1);
	}
	private boolean copyDirectory(String srcPath, String destDir) {
		//System.out.println("复制文件夹开始!");
		boolean flag = false;
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) { // 源文件夹不存在
			//System.out.println("源文件夹不存在");
			return false;
		}
		//获得待复制的文件夹的名字，比如待复制的文件夹为"E:\\dir"则获取的名字为"dir"
		String dirName = getDirName(srcPath);
		//目标文件夹的完整路径
		String destPath = destDir + File.separator + dirName;
//		System.out.println("目标文件夹的完整路径为：" + destPath);
		if(destPath.equals(srcPath)) {
			//System.out.println("目标文件夹与源文件夹重复");
			return false;
		}
		File destDirFile = new File(destPath);
		if(destDirFile.exists()) {	//目标位置有一个同名文件夹
			//System.out.println("目标位置已有同名文件夹!");
			return false;
		}

		destDirFile.mkdirs();	//生成目录
		File[] fileList = srcFile.listFiles();	//获取源文件夹下的子文件和子文件夹
		if(fileList.length==0) {	//如果源文件夹为空目录则直接设置flag为true，这一步非常隐蔽，debug了很久
			flag = true;
		}
		else {
			for(File temp: fileList) {
				if(temp.isFile()) {	//文件
				flag = copyFile(temp.getAbsolutePath(), destPath);
				}
				else if(temp.isDirectory()) {	//文件夹
					flag = copyDirectory(temp.getAbsolutePath(), destPath);
				}
				if(!flag) {
					break;
				}
			}
		}
		if(flag) {
			//System.out.println("复制文件夹成功!");
		}
		return flag;

	}

	private boolean copyFile(String srcPath, String destDir) {

		boolean flag = false;
		File srcFile = new File(srcPath);
		if (!srcFile.exists()) { // 源文件不存在
			//System.out.println("源文件不存在");
			return false;
		}
		//获取待复制文件的文件名
		String fileName = srcPath.substring(srcPath.lastIndexOf(File.separator));
		String destPath = destDir + fileName;
		if (destPath.equals(srcPath)) { // 源文件路径和目标文件路径重复

			//System.out.println("源文件路径和目标文件路径重复!");

			return false;

		}

		File destFile = new File(destPath);
		if(destFile.exists() && destFile.isFile()) {	//该路径下已经有一个同名文件
			//System.out.println("目标目录下已有同名文件!");
			return false;
		}
		File destFileDir = new File(destDir);
		destFileDir.mkdirs();
		try {
			FileInputStream fis = new FileInputStream(srcPath);
			FileOutputStream fos = new FileOutputStream(destFile);
			byte[] buf = new byte[1024];
			int c;
			while ((c = fis.read(buf)) != -1) {
				fos.write(buf, 0, c);
			}
			fis.close();
			fos.close();
			flag = true;
		} catch (IOException e) {
		}
		if(flag) {
			//System.out.println("复制文件成功!");
		}
		return flag;

	}
	
	//==============================================

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
		filePath += "/" +"学籍卡"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls"; 
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
				sql="select 学号 from V_学生基本数据子类 where 行政班代码='"+BJVec.get(i)+"'order by 学号 ";
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
				"where   c.课程类型='01' and d.学号 in ('" +XSXH + "')  " +
				"order by d.学号,b.课程代码 ";
				subWhVec = db.GetContextVector(sql);
		
				//获取所有专业课的课程
				sql="with tempClassInfo as ("+
					"select distinct b.课程代码,b.课程名称 ,b.行政班代码,a.学号  "+	
					"from V_成绩管理_学生成绩信息表 a  "+
					"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
					"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +			
					"left join V_学生基本数据子类 d on d.学号=a.学号  "+						
					"where   a.学号 in ('" +XSXH + "')	) "+			
					"select distinct b.课程代码,b.课程名称,d.学号  "+
					"from  V_规则管理_授课计划主表 a   "+
					"left join V_规则管理_授课计划明细表 b on a.授课计划主表编号=b.授课计划主表编号    "+
					"left join V_课程数据子类 c on c.课程号=b.课程代码   "+
					"left join tempClassInfo d on d.课程代码=b.课程代码 and d.行政班代码=a.行政班代码 "+
					"where   c.课程类型='02' and d.学号 in ('" +XSXH + "')  " +
					"order by d.学号,b.课程代码 ";
				subZyVec = db.GetContextVector(sql);
		
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
		

		//查询学业水平测试成绩
		sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
			"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
			"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
			"where  a.学号 in ('" +XSXH + "') "+
			"and c.类别编号='03' and a.状态='1' and b.状态='1' and c.状态='1'" +
			"order by a.学号,b.课程代码,c.学年学期编码 desc";	
		xyspVec = db.GetContextVector(sql);
		int count=0;
		if(XhAndBj!=null&&XhAndBj.size()>0){
			for(int i=0;i<XhAndBj.size();i+=2){
				exportScoreInfo_2(wbook,MyTools.StrFiltr(XhAndBj.get(i)),MyTools.StrFiltr(XhAndBj.get(i+1)) ,count++,"",scoreInfoVec,xyspVec,subWhVec,subZyVec);
			}
		}
			wbook.write();
			wbook.close();
			os.close();
			this.setMSG("文件生成成功");
		return filePath;
	}
	public void exportScoreInfo_2(WritableWorkbook wbook,String JX_XJH,String BJDM,int n,String XM,Vector scoreInfoVec,Vector xyspVec,Vector subWhVec,Vector subZyVec) throws SQLException, IOException{
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
		
		//获取所有公共课的课程
		/*sql="with tempClassInfo as ("+
			"select distinct b.课程代码,b.课程名称 ,b.行政班代码,a.学号 "+	
			"from V_成绩管理_学生成绩信息表 a  "+
			"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
			"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +			
			"left join V_学生基本数据子类 d on d.学号=a.学号  "+						
			"where   d.行政班代码='"+MyTools.fixSql(BJDM)+"' and a.学号='"+MyTools.fixSql(JX_XJH)+"')	 "+			
			"select distinct b.课程代码,b.课程名称 "+
			"from  V_规则管理_授课计划主表 a   "+
			"left join V_规则管理_授课计划明细表 b on a.授课计划主表编号=b.授课计划主表编号    "+
			"left join V_课程数据子类 c on c.课程号=b.课程代码   "+
			"left join tempClassInfo d on d.课程代码=b.课程代码 and d.行政班代码=a.行政班代码 "+
			"where a.行政班代码='"+MyTools.fixSql(BJDM)+"' and  c.课程类型='01' and d.学号='"+MyTools.fixSql(JX_XJH)+"'  " +
			"order by b.课程代码 ";
			subVec = db.GetContextVector(sql);*/
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
						"and e.课程类型='01' and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
						"order by b.课程代码";
					firstVec = db.GetContextVector(sql);
			
					
					sql = "select a.学号,b.课程代码 "+
							"from V_成绩管理_学生成绩信息表 a  "+
							"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
							"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
							"left join V_学生基本数据子类 d on d.学号=a.学号 "+
							"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
							"where c.学年学期编码='" +  xnVec.get(i+1) + "' " +
							"and  e.课程类型='01' and a.学号='"+MyTools.fixSql(JX_XJH)+"' " +
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
										if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
											firstFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if("".equalsIgnoreCase(firstScore) || "作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "-5".equalsIgnoreCase(firstScore)){
											firstScore = "0";
										}
										
										if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
											secondFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if("作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
											secondScore = "0";
										}
								
									//上学期
									}else if(first==true && second==false){
										 firstScore=zp1;
										 scoreVec.add(firstScore);
										 scoreVec.add("");
										if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
											firstFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if("作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "缓考".equalsIgnoreCase(firstScore)){
											firstScore = "0";
										}
										//下学期
									}else{
										   secondScore=zp1;
										   scoreVec.add("");
										   scoreVec.add(secondScore);
										if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
											secondFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if( "作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
											secondScore = "0";
										}
									}
									String bk="";
									//计算总评
									if(firstFlag==true && secondFlag==true){
										String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
										String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
										Double sxqzb=MyTools.StringToDouble(SXQzb);
										Double xxqzb=MyTools.StringToDouble(XXQzb);
										firstScore="".equalsIgnoreCase(firstScore)?"0":firstScore;
										secondScore="".equalsIgnoreCase(secondScore)?"0":secondScore;
										if(type == 0){
											tempXnzp = f.format(MyTools.StringToDouble(firstScore)*0.4+MyTools.StringToDouble(secondScore)*0.6);
										}else{
											tempXnzp = f.format(MyTools.StringToDouble(firstScore)*sxqzb/100+MyTools.StringToDouble(secondScore)*xxqzb/100);
										}
										
									}else if(firstFlag==true && secondFlag==false){
											tempXnzp=firstScore;
									}else if(firstFlag==false && secondFlag==true){
											tempXnzp=secondScore;
									}else{
											tempXnzp="";
									}
									
									scoreVec.add(tempXnzp);
									bk=tempXnzp;
									if("".equalsIgnoreCase(bk)){
										bk="0";
									}
									if(MyTools.StringToDouble(bk)<60.0){
										boolean xyspcsFlag = false;
										Vector xyspVec1=new Vector();
										if(xyspVec!=null && xyspVec.size()>0){
											for(int kk=0; kk<xyspVec.size(); kk+=4){
												String xh=MyTools.StrFiltr(xyspVec.get(kk));
												String xnxqm=MyTools.StrFiltr(xyspVec.get(kk+3));
												if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i)).equalsIgnoreCase(xnxqm)){
													for(int dd=0;dd<4;dd++){
														xyspVec1.add(xyspVec.get(kk+dd));
													}
													xyspcsFlag=true;
												}
												if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i+1)).equalsIgnoreCase(xnxqm)){
													for(int dd=0;dd<4;dd++){
														xyspVec1.add(xyspVec.get(kk+dd));
													}
													xyspcsFlag=true;
													
												}
											}
										}
										if(xyspcsFlag){
											for(int w=0;w<xyspVec1.size();w+=4){
												String xyspcj="";
												if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+1)))){
													if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))){
														xyspcj="0";
														
													}else{
														xyspcj=MyTools.StrFiltr(xyspVec1.get(w+2));
													}
													if(MyTools.StringToDouble(xyspcj)>=60.0){
														bk=xyspcj;
														break;
													}else{
														bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
													}
													
												}else{
													bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
												}
											}
											
										}else{
											bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
										}
										
									}else{
										bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
										
									}
									scoreVec.add(bk);
									//scoreVec.add(MyTools.StrFiltr(scoreInfoVec.get(k+9)));
									existFlag = true;
									break;
								}
						}
							//没有该课程则增加成绩信息为空
								if(!existFlag){
									scoreVec.add("");
									scoreVec.add("");
									scoreVec.add("");
									scoreVec.add("");
								}
					}
				}else{
					for(int j=0;j<subVec.size()/2;j++){
						scoreVec.add("");
						scoreVec.add("");
						scoreVec.add("");
						scoreVec.add("");
					}
				}
			}
			
			Vector scoreVec2=new Vector();
			Vector subVec2=new Vector();
			Vector scoreInfoVec2=new Vector();
			Vector xyspVec2=new Vector();
			//获取所有公共课的课程
		/*	sql="with tempClassInfo as ("+
				"select distinct b.课程代码,b.课程名称 ,b.行政班代码,a.学号 "+	
				"from V_成绩管理_学生成绩信息表 a  "+
				"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
				"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +			
				"left join V_学生基本数据子类 d on d.学号=a.学号  "+						
				"where   d.行政班代码='"+MyTools.fixSql(BJDM)+"' and a.学号='"+MyTools.fixSql(JX_XJH)+"')	 "+			
				"select distinct b.课程代码,b.课程名称 "+
				"from  V_规则管理_授课计划主表 a   "+
				"left join V_规则管理_授课计划明细表 b on a.授课计划主表编号=b.授课计划主表编号    "+
				"left join V_课程数据子类 c on c.课程号=b.课程代码   "+
				"left join tempClassInfo d on d.课程代码=b.课程代码 and d.行政班代码=a.行政班代码 "+
				"where a.行政班代码='"+MyTools.fixSql(BJDM)+"' and  c.课程类型='02' and d.学号='"+MyTools.fixSql(JX_XJH)+"'  order by b.课程代码 ";
			subVec2 = db.GetContextVector(sql);*/
			
			if(subZyVec!=null&&subZyVec.size()>0){
				for(int i=0;i<subZyVec.size();i+=3){
					if(MyTools.StrFiltr(subZyVec.get(i+2)).equalsIgnoreCase(JX_XJH)){
						subVec2.add(MyTools.StrFiltr(subZyVec.get(i)));
						subVec2.add(MyTools.StrFiltr(subZyVec.get(i+1)));
					}
				}
				
			}else  {
					for(int i=0;i<14;i++){
						subVec2.add("");
					}
				}
			
			
			int subVecSize2=(int)(subVec2.size()/2);
			if(subVecSize2<5){
					for(int j=0;j<(5-subVecSize2);j++){
						subVec2.add("");
						subVec2.add("");
						
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
						"and  e.课程类型='02' and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
						"order by b.课程代码";
				firstVec = db.GetContextVector(sql);
			
					
				sql = "select a.学号,b.课程代码 "+
							"from V_成绩管理_学生成绩信息表 a  "+
							"left join V_成绩管理_登分教师信息表 b on b.相关编号=a.相关编号 "+
							"left join V_成绩管理_科目课程信息表 c on b.科目编号=c.科目编号 " +
							"left join V_学生基本数据子类 d on d.学号=a.学号 "+
							"left join  V_课程数据子类   e on b.课程代码=e.课程号 "+
							"where c.学年学期编码='" +  xnVec.get(i+1) + "' " +
							"and  e.课程类型='02' and a.学号='"+MyTools.fixSql(JX_XJH)+"'" +
							"order by b.课程代码";	
				secondVec = db.GetContextVector(sql);
				
				//获取成绩
		/*	sql = "with tempClassInfo as ("+
						 "select b.学年, a.学号,b.课程代码," +
						 "(case when a.补考='-1' then '免修' when a.补考='-2' then '作弊' when a.补考='-3' then '取消资格' when a.补考='-4' then '缺考'  when a.补考='-5' then '缓考' when a.补考='-9' then '及格' when a.补考='-10' then '不及格'  when a.补考='-17' then '免考' else a.补考 end ) as 补考  " +
						 "from V_成绩管理_学生补考成绩信息表  a "+	
						 "left join  V_成绩管理_补考登分教师信息表 b on a.补考编号=b.编号 "+	
						 "where b.班级代码='"+MyTools.fixSql(BJDM)+"' and b.学年='"+MyTools.StrFiltr(xnVec.get(i)).substring(0,4) +"'"+	
						 ") "+	
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
						"left join tempClassInfo cc on a.学号=cc.学号 and cc.课程代码=b.课程代码 "+
						"where  c.学年学期编码  in ('" + xnVec.get(i) + "','" + xnVec.get(i+1) + "') " +
						"and e.行政班代码='"+MyTools.fixSql(BJDM)+"' and g.课程类型='02' and a.学号='"+MyTools.fixSql(JX_XJH)+"66' " +
						"order by b.课程代码,c.学年学期编码";
					scoreInfoVec2 = db.GetContextVector(sql);
					

					//查询学业水平测试成绩
					sql = "select a.学号,b.课程代码 ,a.成绩,c.学年学期编码   from V_自设考试管理_学生成绩信息表 a "+ 
						"left join V_自设考试管理_考试学科信息表 b on a.考试学科编号=b.考试学科编号  "+	
						"left join V_自设考试管理_考试信息表 c on b.考试编号=c.考试编号 "+	
						"where 班级代码='" + MyTools.fixSql(BJDM) + "' " +
						"and  c.学年学期编码  in ('" + xnVec.get(i) + "','" + xnVec.get(i+1) + "') " +
						"and c.类别编号='03' and a.状态='1' and b.状态='1' and c.状态='1'  and a.学号='"+MyTools.fixSql(JX_XJH)+"' " +
						"order by c.学年学期编码 desc";	
					xyspVec2 = db.GetContextVector(sql);*/
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
					for(int j=0; j<subVec2.size(); j+=2){
						curSubCode = MyTools.StrFiltr(subVec2.get(j));//课程代码
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
										scoreVec2.add(firstScore);
										scoreVec2.add(secondScore);
										if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
											firstFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if("".equalsIgnoreCase(firstScore) || "作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "-5".equalsIgnoreCase(firstScore)){
											firstScore = "0";
										}
										
										if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
											secondFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if("作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
											secondScore = "0";
										}
								
									//上学期
									}else if(first==true && second==false){
										 firstScore=zp1;
										 scoreVec2.add(firstScore);
										 scoreVec2.add("");
										if(!"免修".equalsIgnoreCase(firstScore) && !"免考".equalsIgnoreCase(firstScore)){
											firstFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if("作弊".equalsIgnoreCase(firstScore) || "取消资格".equalsIgnoreCase(firstScore) || "缺考".equalsIgnoreCase(firstScore) || "缓考".equalsIgnoreCase(firstScore)){
											firstScore = "0";
										}
										//下学期
									}else{
										   secondScore=zp1;
										   scoreVec2.add("");
										   scoreVec2.add(secondScore);
										if(!"免修".equalsIgnoreCase(secondScore) && !"免考".equalsIgnoreCase(secondScore)){
											secondFlag = true;
										}
										//判断如果是其他文字成绩，转换分数
										if( "作弊".equalsIgnoreCase(secondScore) || "取消资格".equalsIgnoreCase(secondScore) || "缺考".equalsIgnoreCase(secondScore) || "缓考".equalsIgnoreCase(secondScore)){
											secondScore = "0";
										}
									}
									String bk="";
									//计算总评
									if(firstFlag==true && secondFlag==true){
										String SXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(1)))?"0":MyTools.StrFiltr(xqzbVec.get(1));
										String XXQzb="".equalsIgnoreCase(MyTools.StrFiltr(xqzbVec.get(2)))?"0":MyTools.StrFiltr(xqzbVec.get(2));
										Double sxqzb=MyTools.StringToDouble(SXQzb);
										Double xxqzb=MyTools.StringToDouble(XXQzb);
										firstScore="".equalsIgnoreCase(firstScore)?"0":firstScore;
										secondScore="".equalsIgnoreCase(secondScore)?"0":secondScore;
										if(type == 0){
											tempXnzp = f.format(MyTools.StringToDouble(firstScore)*0.4+MyTools.StringToDouble(secondScore)*0.6);
										}else{
											tempXnzp = f.format(MyTools.StringToDouble(firstScore)*sxqzb/100+MyTools.StringToDouble(secondScore)*xxqzb/100);
										}
										
									}else if(firstFlag==true && secondFlag==false){
											tempXnzp=firstScore;
									}else if(firstFlag==false && secondFlag==true){
											tempXnzp=secondScore;
									}else{
											tempXnzp="";
									}
									
									scoreVec2.add(tempXnzp);
									bk=tempXnzp;
									if("".equalsIgnoreCase(bk)){
										bk="0";
									}
									if(MyTools.StringToDouble(bk)<60.0){
										
										boolean xyspcsFlag = false;
										Vector xyspVec1=new Vector();
										if(xyspVec!=null && xyspVec.size()>0){
											for(int kk=0; kk<xyspVec.size(); kk+=4){
												String xh=MyTools.StrFiltr(xyspVec.get(kk));
												String xnxqm=MyTools.StrFiltr(xyspVec.get(kk+3));
												if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i)).equalsIgnoreCase(xnxqm)){
													for(int dd=0;dd<4;dd++){
														xyspVec1.add(xyspVec.get(kk+dd));
													}
													xyspcsFlag=true;
												}
												if(MyTools.fixSql(JX_XJH).equalsIgnoreCase(xh)&&MyTools.StrFiltr(xnVec.get(i+1)).equalsIgnoreCase(xnxqm)){
													for(int dd=0;dd<4;dd++){
														xyspVec1.add(xyspVec.get(kk+dd));
													}
													xyspcsFlag=true;
													
												}
											}
										}
										
										
										if(xyspcsFlag){
											for(int w=0;w<xyspVec1.size();w+=4){
												String xyspcj="";
												if(curSubCode.equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+1)))){
													if("".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))||"null".equalsIgnoreCase(MyTools.StrFiltr(xyspVec1.get(w+2)))){
														xyspcj="0";
														
													}else{
														xyspcj=MyTools.StrFiltr(xyspVec1.get(w+2));
													}
													if(MyTools.StringToDouble(xyspcj)>=60.0){
														bk=xyspcj;
														break;
													}else{
														bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
													}
													
												}else{
													bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
												}
											}
											
										}else{
											bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
										}
										
									}else{
										bk=MyTools.StrFiltr(scoreInfoVec1.get(k+9));
										
									}
									scoreVec2.add(bk);
									//scoreVec2.add(MyTools.StrFiltr(scoreInfoVec2.get(k+9)));
									existFlag = true;
									break;
								}
						}
							//没有该课程则增加成绩信息为空
								if(!existFlag){
									scoreVec2.add("");
									scoreVec2.add("");
									scoreVec2.add("");
									scoreVec2.add("");
								}
					}
				}else{
					for(int j=0;j<subVec2.size()/2;j++){
						scoreVec2.add("");
						scoreVec2.add("");
						scoreVec2.add("");
						scoreVec2.add("");
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
			for(int i=0;i<52;i++){
				wsheet.setRowView(i,240);//设置行高
			}
			for(int i=52;i<58;i++){
				wsheet.setRowView(i,350);//设置行高
			}
			wsheet.setColumnView(0,5);//设置列宽
			wsheet.setColumnView(1,7);//设置列宽
			for(int i=2;i<18;i++){
				wsheet.setColumnView(i,5);//设置列宽
			}
			wsheet.setColumnView(20,4);//设置列宽
			for(int i=22;i<39;i++){
				wsheet.setColumnView(i,5);//设置列宽
			}
			wsheet.setColumnView(19,4);//设置列宽
			wsheet.setColumnView(0,3);//设置列宽
			wsheet.setColumnView(1,5);//设置列宽
			wsheet.setColumnView(8,3);//设置列宽
			wsheet.setColumnView(9,4);//设置列宽
			wsheet.setColumnView(10,7);//设置列宽
			wsheet.setColumnView(13,8);//设置列宽
			wsheet.setColumnView(14,6);//设置列宽
			wsheet.setColumnView(15,4);//设置列宽
			wsheet.setColumnView(16,4);//设置列宽
			wsheet.setColumnView(17,3);//设置列宽
			wsheet.setColumnView(18,5);//设置列宽
			wsheet.setColumnView(21,6);//设置列宽
			if((60+(subVec.size()/2)+(subVec2.size()/2))<84){
				for(int i=58;i<84;i++){
					wsheet.setRowView(i,460);//设置行高
				}
			}else{
				for(int i=58;i<(60+(subVec.size()/2)+(subVec2.size()/2));i++){
					wsheet.setRowView(i,460);//设置行高
				}
			}
			wsheet.setRowView(59,540);//设置行高
			wsheet.setRowView(75,540);//设置行高
			wsheet.setRowView(75,540);//设置行高
			wsheet.setRowView(85,500);//设置行高
			wsheet.setRowView(86,500);//设置行高
			wsheet.setRowView(87,500);//设置行高
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentStyle.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			contentStyle.setWrap(true);
			
			fontStyleCell_9 = new WritableFont(
					WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			fontStyleFont_9 = new WritableCellFormat(fontStyleCell_9);
			fontStyleFont_9.setAlignment(jxl.format.Alignment.CENTRE);//水平居中
			fontStyleFont_9.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			fontStyleFont_9.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
			fontStyleFont_9.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			fontStyleFont_9.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			fontStyleFont_9.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			fontStyleFont_9.setWrap(true);
			
			
			
			fontStyle_left = new WritableFont(
					WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle_left = new WritableCellFormat(fontStyle_left);
			contentStyle_left.setAlignment(jxl.format.Alignment.LEFT);// 水平居左
			contentStyle_left.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentStyle_left.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
			contentStyle_left.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			contentStyle_left.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			//contentStyle_left.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			contentStyle_left.setWrap(true);
			
			
			

			fontStyle_leftAndTopNo= new WritableFont(
					WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			fontStyle_leftAndTopNoStyle = new WritableCellFormat(fontStyle_leftAndTopNo);
			fontStyle_leftAndTopNoStyle.setAlignment(jxl.format.Alignment.LEFT);// 水平居左
			fontStyle_leftAndTopNoStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			fontStyle_leftAndTopNoStyle.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			fontStyle_leftAndTopNoStyle.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			fontStyle_leftAndTopNoStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			fontStyle_leftAndTopNoStyle.setWrap(true);
			
			
			
			fontStyle_right = new WritableFont(
					WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle_right = new WritableCellFormat(fontStyle_right);
			contentStyle_right.setAlignment(jxl.format.Alignment.RIGHT);// 水平居右
			contentStyle_right.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);// 垂直居中
			contentStyle_right.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
			contentStyle_right.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
			contentStyle_right.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
			contentStyle_right.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
			contentStyle_right.setWrap(true);
			
			wsheet.mergeCells(1, 0, 9, 1);
			content = new Label(1, 0, "第 一 学 期 思 想 品 德 评 语", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(10, 0, 18, 1);
			content = new Label(10, 0, "第 二 学 期 思 想 品 德 评 语", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(0, 0, 0, 13);
			content = new Label(0, 0, "一\n年\n级", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(0, 14, 0, 25);
			content = new Label(0, 14, "二\n年\n级", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(0, 26, 0, 37);
			content = new Label(0, 26, "三\n年\n级", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(0, 38, 0, 49);
			content = new Label(0, 38, "四\n年\n级", contentStyle);
			wsheet.addCell(content);
			
		
			
			for(int i=1;i<5;i++){
				sql=" select b.类别名称,a.评语 from   V_成绩管理_学生评语信息表 a "+
					"left join  V_信息类别_类别操作 b on a.等第=b.描述 "+	
					"where b.父类别代码='DJ' and a.学号='"+JX_XJH+"' and a.年级='"+i+"'";
					pyVec=db.GetContextVector(sql);
					if(pyVec!=null&&pyVec.size()>0){
						pySave.add(pyVec.get(0));
						pySave.add(pyVec.get(1));
					}else{
						pySave.add("");
						pySave.add("");
					}
				}
			
			int countPY=0;
			
			for(int i=2;i<50;i+=12){//1 13 25 37
					wsheet.mergeCells(1, i, 9, i+9);
					content = new Label(1, i, MyTools.StrFiltr(pySave.get(countPY++)), contentStyle_left);
					wsheet.addCell(content);
					
					wsheet.mergeCells(1, i+10, 9, i+11);
					content = new Label(1, i+10, "班主任(签名)          年    月    日", fontStyle_leftAndTopNoStyle);
					wsheet.addCell(content);
					
					wsheet.mergeCells(10, i, 18, i+9);
					content = new Label(10, i, MyTools.StrFiltr(pySave.get(countPY++)), contentStyle_left);
					wsheet.addCell(content);
					
					wsheet.mergeCells(10, i+10, 18, i+11);
					content = new Label(10, i+10, "班主任(签名)            年     月     日", fontStyle_leftAndTopNoStyle);
					wsheet.addCell(content);
				
				}
		
			wsheet.mergeCells(0, 50, 18, 53);
			content = new Label(0, 50, "毕业证书号码", fontStyle_leftAndTopNoStyle);
			wsheet.addCell(content);
			
			//第二部分
			
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 20, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentStyle.setWrap(true);
			
			wsheet.mergeCells(32, 1, 33, 2);
			content = new Label(32, 1, "编号", contentStyle);
			wsheet.addCell(content);
			wsheet.mergeCells(24, 10, 34, 11);
			content = new Label(24, 10, "上海市职业技术学校学生学籍卡", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(23, 20, 25, 21);
			content = new Label(23, 20, "区(县)", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(23, 25, 25, 26);
			content = new Label(23, 25, "学校", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(23, 30, 25, 31);
			content = new Label(23, 30, "学制", contentStyle);
			wsheet.addCell(content);
			
			wsheet.mergeCells(23, 35, 25, 36);
			content = new Label(23, 35, "专业", contentStyle);
			wsheet.addCell(content);
			
			fontStyle = new WritableFont(
					WritableFont.createFont("宋体"), 20, WritableFont.BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
			contentStyle = new WritableCellFormat(fontStyle);
			contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
			contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
			contentStyle.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THICK);
			wsheet.mergeCells(34, 1, 36, 2);
			content = new Label(34, 1, "", contentStyle);
			wsheet.addCell(content);
			wsheet.mergeCells(26, 20, 31, 21);
			content = new Label(26, 20, "长宁", contentStyle);
			wsheet.addCell(content);
			wsheet.mergeCells(26, 25, 31, 26);
			content = new Label(26, 25, "现代职校", contentStyle);
			wsheet.addCell(content);
			wsheet.mergeCells(26, 30, 31, 31);
			content = new Label(26, 30, "三", contentStyle);
			wsheet.addCell(content);
			wsheet.mergeCells(26, 35, 31, 36);
			content = new Label(26, 35, "", contentStyle);
			wsheet.addCell(content);
		
			//第三部分
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
			sql = "select a.姓名,f.类别名称,g.类别名称,h.类别名称,CONVERT(varchar(100), a.出生日期, 23),i.类别名称,a.户籍地址,户籍邮编,a.家庭电话,a.所属派出所,a.身份证件号,a.父亲,a.父亲年龄,a.父亲政治面貌,a.父亲工作单位,a.父亲电话,a.母亲,a.母亲年龄,a.母亲政治面貌,a.母亲工作单位,a.母亲电话 "+                
					",(select 校区名称 from V_校区数据类) as 学校,CONVERT(varchar(100), a.入学日期, 112) as 入校时间 ,CONVERT(varchar(100), a.毕业离校日期, 112) as 毕业离校日期,a.照片路径  "+	
					"from V_学生基本数据子类 a  "+		
					"left join dbo.V_信息类别_类别操作 c on a.学生状态=c.描述  and c.父类别代码='XSZTM'  "+	
					"left join dbo.V_信息类别_类别操作 f on a.性别码=f.描述  and f.父类别代码='XBM' "+		
					"left join dbo.V_信息类别_类别操作 g on a.民族码=g.描述  and g.父类别代码='MZDM'  "+		
					"left join dbo.V_信息类别_类别操作 h on a.籍贯=h.描述  and h.父类别代码='CSDM'  "+	
					"left join dbo.V_信息类别_类别操作 i on a.户口类别码=i.描述  and i.父类别代码='HKXZM'  "+	
					"where 1=1 " ;
					sql+=" and 学号='"+JX_XJH+"'  order by a.学籍号" ;		
			 Vec1 = db.GetContextVector(sql);
			 if(Vec1.size()==0){
				 for(int i=0;i<25;i++){
					 Vec1.add("");
				 }
			 }
			//姓名
			 wsheet.mergeCells(0, 58, 1, 58);
			 content = new Label(0, 58, "姓名", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(2, 58, 3, 58);
			 content = new Label(2, 58, MyTools.StrFiltr(Vec1.get(0)), contentStyle);
			 wsheet.addCell(content);
			
			//性别
			 wsheet.mergeCells(4, 58, 5, 58);
			 content = new Label(4, 58, "性别", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(6, 58, 7, 58);
			 content = new Label(6, 58, MyTools.StrFiltr(Vec1.get(1)), contentStyle);
			 wsheet.addCell(content);
			 
			//名族
			 wsheet.mergeCells(8, 58, 9, 58);
			 content = new Label(8, 58, "名族", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(10, 58, MyTools.StrFiltr(Vec1.get(2)), contentStyle);
			 wsheet.addCell(content);
			 
			//籍贯
			 wsheet.mergeCells(11, 58, 12, 58);
			 content = new Label(11, 58, "籍贯", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(13, 58, 14, 58);
			 content = new Label(13, 58, MyTools.StrFiltr(Vec1.get(3)), contentStyle);
			 wsheet.addCell(content);
			 
			 
			//照片
			 wsheet.mergeCells(15, 58, 18, 61);
			
			 File imgFile = new File(MyTools.getProp(request, "Base.Photo")+MyTools.StrFiltr(Vec1.get(24)));
				
				if (!imgFile.exists()||"".equalsIgnoreCase(MyTools.StrFiltr(Vec1.get(24)))) {
				 content = new Label(15, 58, "照片", contentStyle);
				 wsheet.addCell(content);
			}else{
				BufferedImage picImage = ImageIO.read(imgFile);  
		        // 取得图片的像素高度，宽度  
				 ByteArrayOutputStream baos = new ByteArrayOutputStream();
				 ImageIO.write(picImage, "png", baos);
				 wsheet.addImage(new WritableImage(15.1,58.18,3.8,3.72 ,baos.toByteArray()));
			 	}
			
			  WritableFont fontStyle333;
			 WritableCellFormat contentStyle333;
			 fontStyle333 = new WritableFont(
							WritableFont.createFont("宋体"), 20, WritableFont.NO_BOLD,
							false, jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.BLACK);
				 contentStyle333 = new WritableCellFormat(fontStyle333);
				 contentStyle333.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
				 content = new Label(19, 58, "", contentStyle333);
				 wsheet.addCell(content);
				 content = new Label(19, 59, "", contentStyle333);
				 wsheet.addCell(content);
				 content = new Label(19, 60, "", contentStyle333);
				 wsheet.addCell(content);
				 content = new Label(19, 61, "", contentStyle333);
				 wsheet.addCell(content);
			
			
			
			 
			//出生年月
			 wsheet.mergeCells(0, 59, 2, 59);
			 content = new Label(0, 59, "出生年月", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 59, 7, 59);
			 content = new Label(3, 59, MyTools.StrFiltr(Vec1.get(4)), contentStyle);
			 wsheet.addCell(content);
			 
			 	//户口
			 wsheet.mergeCells(8, 59, 12, 59);
			 content = new Label(8, 59, "户口属农业户或城镇户", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(13, 59, 14, 59);
			 content = new Label(13, 59, MyTools.StrFiltr(Vec1.get(5)), contentStyle);
			 wsheet.addCell(content);
			 
			//家庭住址
			 wsheet.mergeCells(0, 60, 2, 60);
			 content = new Label(0, 60, "家庭住址", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 60, 7, 60);
			 if(MyTools.StrFiltr(Vec1.get(6)).length()>14){
				 content = new Label(3, 60, MyTools.StrFiltr(Vec1.get(6)),fontStyleFont_9);
			 }else
			 content = new Label(3, 60, MyTools.StrFiltr(Vec1.get(6)), contentStyle);
			 wsheet.addCell(content);
			
			//邮编
			 wsheet.mergeCells(8, 60, 9, 60);
			 content = new Label(8, 60, "邮编", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(10, 60, MyTools.StrFiltr(Vec1.get(7)), contentStyle);
			 wsheet.addCell(content);
			
			//电话
			 wsheet.mergeCells(11, 60, 12, 60);
			 content = new Label(11, 60, "电话", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(13, 60, 14, 60);
			 content = new Label(13, 60, MyTools.StrFiltr(Vec1.get(8)), contentStyle);
			 wsheet.addCell(content);
			 WritableFont fontStyle2;
			 WritableCellFormat contentStyle2;
			 Label content2;
			fontStyle2 = new WritableFont(
					WritableFont.createFont("宋体"), 9, WritableFont.NO_BOLD,
					false, jxl.format.UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK);
					contentStyle2 = new WritableCellFormat(fontStyle2);
					contentStyle2.setAlignment(jxl.format.Alignment.CENTRE);// 左对齐
					contentStyle2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
					contentStyle2.setBorder(jxl.format.Border.TOP, BorderLineStyle.THIN);
					contentStyle2.setBorder(jxl.format.Border.RIGHT, BorderLineStyle.THIN);
					contentStyle2.setBorder(jxl.format.Border.LEFT, BorderLineStyle.THIN);
					contentStyle2.setBorder(jxl.format.Border.BOTTOM, BorderLineStyle.THIN);
				
			//所属派出所
			 wsheet.mergeCells(0, 61, 2, 61);
			 content2 = new Label(0, 61, "所属派出所", contentStyle2);
			 wsheet.addCell(content2);
			 wsheet.mergeCells(3, 61, 7, 61);
			 content = new Label(3, 61, MyTools.StrFiltr(Vec1.get(9)), contentStyle);
			 wsheet.addCell(content);
			//何时何地参加共青团
			 wsheet.mergeCells(8, 61, 12, 61);
			 content = new Label(8, 61, "何时何地参加共青团", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(13, 61, 14, 61);
			 content = new Label(13, 61,"", contentStyle);
			 wsheet.addCell(content);
			 
			 
			//身份证号码
			 wsheet.mergeCells(0, 62, 2, 62);
			 content2 = new Label(0, 62, "身份证号码", contentStyle2);
			 wsheet.addCell(content2);
			 wsheet.mergeCells(3, 62, 18, 62);
			 content = new Label(3, 62, MyTools.StrFiltr(Vec1.get(10)), contentStyle);
			 wsheet.addCell(content); 
			 
			//原毕(肄)业学校
			 wsheet.mergeCells(0, 63, 2, 63);
			 content2 = new Label(0, 63, "原毕(肄)业学校", contentStyle2);
			 wsheet.addCell(content2);
			 wsheet.mergeCells(3, 63, 10, 63);
			 content = new Label(3, 63, MyTools.StrFiltr(Vec1.get(21)), contentStyle);
			 wsheet.addCell(content); 
			 
			//入学时间
			 wsheet.mergeCells(11, 63, 14, 63);
			 content = new Label(11, 63, "入学时间", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(15, 63, 18, 63);
		//	 content = new Label(15, 63,(MyTools.StrFiltr(Vec1.get(22)).substring(0,4)+"年"+(MyTools.StrFiltr(Vec1.get(22)).substring(4,6)+"月", contentStyle);
			if("".equalsIgnoreCase(MyTools.StrFiltr(Vec1.get(22)))){
				 content = new Label(15, 63,"  年     月", contentStyle);
			}else
				content = new Label(15, 63,MyTools.StrFiltr(Vec1.get(22)).substring(0,4)+"年"+MyTools.StrFiltr(Vec1.get(22)).substring(4,6)+"月", contentStyle);
				
			 wsheet.addCell(content); 
			 
			 
			//转学
			sql="select top 1  异动日期+CAST(异动说明 as varchar ),异动去向学校码 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='11' order by  异动日期 desc ";
			Vec2 = db.GetContextVector(sql);

			if(Vec2.size()==0){
				Vec2.add("");
				Vec2.add("");
			}
			//转学日期及原因
			 wsheet.mergeCells(0, 64, 2, 64);
			 content2 = new Label(0, 64, "转学日期及原因", contentStyle2);
			 wsheet.addCell(content2);
			 wsheet.mergeCells(3, 64, 10, 64);
			 content = new Label(3, 64, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
			 wsheet.addCell(content);
			 
			//去向
			 wsheet.mergeCells(11, 64, 12, 64);
			 content = new Label(11, 64, "去向", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(13, 64, 18, 64);
			 content = new Label(13, 64, MyTools.StrFiltr(Vec2.get(1)), contentStyle);
			 wsheet.addCell(content); 
			 
			//退学日期及原因
			//退学
			sql="select top 1  异动日期+CAST(异动说明 as varchar ),异动去向学校码 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='10' order by  异动日期 desc ";
			
			Vec2 = db.GetContextVector(sql);
			if(Vec2.size()==0){
				Vec2.add("");
				Vec2.add("");
			}
			 
			//退学日期及原因
			 wsheet.mergeCells(0, 65, 2, 65);
			 content2 = new Label(0, 65, "退学日期及原因", contentStyle2);
			 wsheet.addCell(content2);
			 wsheet.mergeCells(3, 65, 10, 65);
			 content = new Label(3, 65, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
			 wsheet.addCell(content); 
			 
			//去向
			 wsheet.mergeCells(11, 65, 12, 65);
			 content = new Label(11, 65, "去向", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(13, 65, 18, 65);
			 content = new Label(13, 65, MyTools.StrFiltr(Vec2.get(1)), contentStyle);
			 wsheet.addCell(content); 
			 
			//休学
			sql="select top 1  异动日期+CAST(异动说明 as varchar ),异动去向学校码 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='08' order by  异动日期 desc ";
			Vec2 = db.GetContextVector(sql);

			if(Vec2.size()==0){
				Vec2.add("");
				Vec2.add("");
			} 
			//休学日期及原因
			 wsheet.mergeCells(0, 66, 2, 66);
			 content2 = new Label(0, 66, "休学日期及原因", contentStyle2);
			 wsheet.addCell(content2);
			 wsheet.mergeCells(3, 66, 10, 66);
			 content = new Label(3, 66, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
			 wsheet.addCell(content); 
			 
			//去向
			 wsheet.mergeCells(11, 66, 12, 66);
			 content = new Label(11, 66, "去向", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(13, 66, 18, 66);
			 content = new Label(13, 66, MyTools.StrFiltr(Vec2.get(1)), contentStyle);
			 wsheet.addCell(content); 
			 
			 
			//复学日期
			sql="select top 1  异动日期 from V_学籍异动数据子类 where 学号='"+JX_XJH+"' and 异动类别码='08' order by  异动日期 desc ";
			Vec2 = db.GetContextVector(sql);

			if(Vec2.size()==0){
				Vec2.add("");
				Vec2.add("");
			} 
			//休学日期及原因
			 wsheet.mergeCells(0, 67, 2, 67);
			 content = new Label(0, 67, "复学日期", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 67, 10, 67);
			 content = new Label(3, 67, MyTools.StrFiltr(Vec2.get(0)), contentStyle);
			 wsheet.addCell(content); 
			 
			//去向
			 wsheet.mergeCells(11, 67, 14, 67);
			 content = new Label(11, 67, "毕业日期", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(15, 67, 18, 67);
			 content = new Label(15, 67, "  年     月", contentStyle);
			 wsheet.addCell(content);
			 
			//离校去向
			 wsheet.mergeCells(0, 68, 2, 68);
			 content = new Label(0, 68, "离校去向", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 68, 18, 68);
			 content = new Label(3, 68, "", contentStyle);
			 wsheet.addCell(content);	 
			 
			//家庭主要成员
			 wsheet.mergeCells(0, 69, 0, 73);
			 content = new Label(0, 69, "家\n庭\n主\n要\n成\n员", contentStyle);
			 wsheet.addCell(content);
			 
			//称呼
			 wsheet.mergeCells(1, 69, 2, 69);
			 content = new Label(1, 69, "称呼", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 69, 4, 69);
			 content = new Label(3, 69, "姓名", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 69, "年龄", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(6, 69, 9, 69);
			 content = new Label(6, 69, "政治面貌", contentStyle);
			 wsheet.addCell(content); 
			 wsheet.mergeCells(10, 69, 15, 69);
			 content = new Label(10, 69, "工作单位职务及地址", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(16, 69, 18, 69);
			 content = new Label(16, 69, "电话", contentStyle);
			 wsheet.addCell(content);
			 
			 
			//父亲
			 wsheet.mergeCells(1, 70, 2, 70);
			 content = new Label(1, 70, "父亲", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 70, 4, 70);
			 content = new Label(3, 70, MyTools.StrFiltr(Vec1.get(11)), contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 70, MyTools.StrFiltr(Vec1.get(12)), contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(6, 70, 9, 70);
			 content = new Label(6, 70, MyTools.StrFiltr(Vec1.get(13)), contentStyle);
			 wsheet.addCell(content); 
			 wsheet.mergeCells(10, 70, 15, 70);
			 content = new Label(10, 70, MyTools.StrFiltr(Vec1.get(14)), contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(16, 70, 18, 70);
			 content = new Label(16, 70, MyTools.StrFiltr(Vec1.get(15)), contentStyle);
			 wsheet.addCell(content);
			 
			//母亲
			 wsheet.mergeCells(1, 71, 2, 71);
			 content = new Label(1, 71, "母亲", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 71, 4, 71);
			 content = new Label(3, 71, MyTools.StrFiltr(Vec1.get(16)), contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 71, MyTools.StrFiltr(Vec1.get(17)), contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(6, 71, 9, 71);
			 content = new Label(6, 71, MyTools.StrFiltr(Vec1.get(18)), contentStyle);
			 wsheet.addCell(content); 
			 wsheet.mergeCells(10, 71, 15, 71);
			 content = new Label(10, 71, MyTools.StrFiltr(Vec1.get(19)), contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(16, 71, 18, 71);
			 content = new Label(16, 71, MyTools.StrFiltr(Vec1.get(20)), contentStyle);
			 wsheet.addCell(content);
		
			 wsheet.mergeCells(1, 72, 2, 72);
			 content = new Label(1, 72, "", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 72, 4, 72);
			 content = new Label(3, 72, "", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 72,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(6, 72, 9, 72);
			 content = new Label(6, 72, "", contentStyle);
			 wsheet.addCell(content); 
			 wsheet.mergeCells(10, 72, 15, 72);
			 content = new Label(10, 72, "", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(16, 72, 18, 72);
			 content = new Label(16, 72,"", contentStyle);
			 wsheet.addCell(content);
			
			 wsheet.mergeCells(1, 73, 2, 73);
			 content = new Label(1, 73, "", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(3, 73, 4, 73);
			 content = new Label(3, 73, "", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 73,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(6, 73, 9, 73);
			 content = new Label(6, 73, "", contentStyle);
			 wsheet.addCell(content); 
			 wsheet.mergeCells(10, 73, 15, 73);
			 content = new Label(10, 73, "", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(16, 73, 18, 73);
			 content = new Label(16, 73,"", contentStyle);
			 wsheet.addCell(content);
			 
			//空白
			 wsheet.mergeCells(0, 74, 2, 75);
			 content = new Label(0, 74, "", contentStyle);
			 wsheet.addCell(content);
			 
			//考勤统计
			 wsheet.mergeCells(3, 74, 7, 74);
			 content = new Label(3, 74, "考勤统计", contentStyle);
			 wsheet.addCell(content);
			 
			
			 content = new Label(3, 75, "事假\n(天)", contentStyle);
			 wsheet.addCell(content);
			 
			 content = new Label(4, 75, "病假\n(天)", contentStyle);
			 wsheet.addCell(content);
			 
			 content = new Label(5, 75, "旷课\n(节)", contentStyle);
			 wsheet.addCell(content);
			 
			 content = new Label(6, 75, "迟到\n(次)", contentStyle);
			 wsheet.addCell(content);
			 
			 content = new Label(7, 75, "早退\n(次)", contentStyle);
			 wsheet.addCell(content);
			 
			//担任社会工作
			 wsheet.mergeCells(8, 74, 10, 75);
			 content = new Label(8, 74,"担任社会工作", contentStyle);
			 wsheet.addCell(content);
			 
			//何时何地何故受到何种奖\n励或处分，何时撤销处分
			 wsheet.mergeCells(11, 74, 18, 75);
			 content = new Label(11, 74,"何时何地何故受到何种奖\n励或处分，何时撤销处分", contentStyle);
			 wsheet.addCell(content);
			 
			//一年级
			 wsheet.mergeCells(0, 76, 0, 77);
			 content = new Label(0, 76,"一\n年\n级", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 76, 2, 76);
			 content = new Label(1, 76,"第一学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 76,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 76,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 76,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 76,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 76,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 76, 10, 76);
			 content = new Label(8, 76,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 76, 18, 76);
			 content = new Label(11, 76,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 77, 2, 77);
			 content = new Label(1, 77,"第二学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 77,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 77,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 77,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 77,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 77,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 77, 10, 77);
			 content = new Label(8, 77,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 77, 18, 77);
			 content = new Label(11, 77,"", contentStyle);
			 wsheet.addCell(content);
			 
		 
			//二年级
			 wsheet.mergeCells(0, 78, 0, 79);
			 content = new Label(0, 78,"二\n年\n级", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 78, 2, 78);
			 content = new Label(1, 78,"第一学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 78,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 78,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 78,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 78,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 78,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 78, 10, 78);
			 content = new Label(8, 78,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 78, 18, 78);
			 content = new Label(11, 78,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 79, 2, 79);
			 content = new Label(1, 79,"第二学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 79,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 79,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 79,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 79,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 79,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 79, 10, 79);
			 content = new Label(8, 79,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 79, 18, 79);
			 content = new Label(11, 79,"", contentStyle);
			 wsheet.addCell(content);
			 
			//三年级
			 wsheet.mergeCells(0, 80, 0, 81);
			 content = new Label(0, 80,"三\n年\n级", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 80, 2, 80);
			 content = new Label(1, 80,"第一学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 80,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 80,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 80,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 80,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 80,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 80, 10, 80);
			 content = new Label(8, 80,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 80, 18, 80);
			 content = new Label(11, 80,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 81, 2, 81);
			 content = new Label(1, 81,"第二学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 81,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 81,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 81,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 81,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 81,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 81, 10, 81);
			 content = new Label(8, 81,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 81, 18, 81);
			 content = new Label(11, 81,"", contentStyle);
			 wsheet.addCell(content);
			 
			//四年级
			 wsheet.mergeCells(0, 82, 0, 83);
			 content = new Label(0, 82,"四\n年\n级", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 82, 2, 82);
			 content = new Label(1, 82,"第一学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 82,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 82,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 82,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 82,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 82,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 82, 10, 82);
			 content = new Label(8, 82,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 82, 18, 82);
			 content = new Label(11, 82,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(1, 83, 2, 83);
			 content = new Label(1, 83,"第二学期", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(3, 83,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(4, 83,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(5, 83,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(6, 83,"", contentStyle);
			 wsheet.addCell(content);
			 content = new Label(7, 83,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(8, 83, 10, 83);
			 content = new Label(8, 83,"", contentStyle);
			 wsheet.addCell(content);
			 wsheet.mergeCells(11, 83, 18, 83);
			 content = new Label(11, 83,"", contentStyle);
			 wsheet.addCell(content);
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
			 wsheet.mergeCells(20, 58, 22, 59);
			 content = new Label(20, 58,"", contentStyle);
			 wsheet.addCell(content);
			//年级 
			count=0;
			for(int i=23;i<39;i+=4){
				 wsheet.mergeCells(i, 58, (i+3), 58);
				 content = new Label(i, 58,nj[count++]+"年级", contentStyle);
				 wsheet.addCell(content);
				 
				 content = new Label(i, 59,"第一\n学期", contentStyle);
				 wsheet.addCell(content);
				 content = new Label(i+1, 59,"第二\n学期", contentStyle);
				 wsheet.addCell(content);
				 content = new Label(i+2, 59,"学年\n总评", contentStyle);
				 wsheet.addCell(content);
				 content = new Label(i+3, 59,"补\n考", contentStyle);
				 wsheet.addCell(content);
			}
			//文化课学业成绩
			wsheet.mergeCells(20, 60, 20, (60+(subVec.size()/2-1)));
			content = new Label(20, 60,"文\n化\n课\n学\n业\n成\n绩", contentStyle);
			wsheet.addCell(content);
			int kemu=(int)(subVec.size()/2)+(int)(subVec2.size()/2);
			for(int i=0;i<kemu;i++){
				wsheet.mergeCells(21, (60+i), 22, (60+i));
			}
			count=60;
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
					wsheet.setRowView(count,560);//设置行高
					content = new Label(21, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle3);
					wsheet.addCell(content);
				}else if(MyTools.StrFiltr(subVec.get(i+1)).length()>5){
					content = new Label(21, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle4);
					wsheet.addCell(content);	
				}else{
					content = new Label(21, count++,MyTools.StrFiltr(subVec.get(i+1)), contentStyle);
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
			
			//遍历文化课成绩
			count=0;
			for(int i=0;i<16;i+=4){
				for(int k=0;k<subVec.size()/2;k++){
					if(count<scoreVec.size()){
							fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
							fsz=MyTools.StringToDouble(fs);
							if(fsz<60){
								content = new Label(23+i, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
							}else
						 	content = new Label(23+i, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
						 	wsheet.addCell(content);	
						 	count+=1;
						 	fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
							fsz=MyTools.StringToDouble(fs);
							if(fsz<60){
								content = new Label(23+i+1, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
							}else
						 	content = new Label(23+i+1, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);
						 	wsheet.addCell(content);	
							count+=1;
							fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
							fsz=MyTools.StringToDouble(fs);
							if(fsz<60){
								content = new Label(23+i+2, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red1);
							}else
							content = new Label(23+i+2, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle);	
							wsheet.addCell(content);	
							count+=1;
							fs=MyTools.StrFiltr(scoreVec.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec.get(count));
							fsz=MyTools.StringToDouble(fs);
							if("不及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec.get(count)))){
								content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red2);
							}if("及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec.get(count)))){
								content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle4);
							}else if(fsz<60){
								content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle_red2);
							}else
							content = new Label(23+i+3, (60+k),MyTools.StrFiltr(scoreVec.get(count)), contentStyle4);
							
							wsheet.addCell(content);	
							count+=1;
						}
				}
			}
			
			//专业课学业成绩
			wsheet.mergeCells(20, (60+(subVec.size()/2-1)+1), 20, (60+(subVec.size()/2-1)+1+(subVec2.size()/2-1)));
			content = new Label(20, (60+(subVec.size()/2-1)+1),"专\n业\n课\n学\n业\n成\n绩", contentStyle);
			wsheet.addCell(content);
			
			count=60+(subVec.size()/2-1)+1;
			for(int i=0;i<subVec2.size();i+=2){			
				if(MyTools.StrFiltr(subVec2.get(i+1)).length()>10){
					wsheet.setRowView(count,560);//设置行高
					content = new Label(21, count++,MyTools.StrFiltr(subVec2.get(i+1)), contentStyle3);
					wsheet.addCell(content);
				}else if(MyTools.StrFiltr(subVec2.get(i+1)).length()>5){
					content = new Label(21, count++,MyTools.StrFiltr(subVec2.get(i+1)), contentStyle4);
					wsheet.addCell(content);	
				}else{
					content = new Label(21, count++,MyTools.StrFiltr(subVec2.get(i+1)), contentStyle);
					wsheet.addCell(content);
					}			
			}
			//遍历专业课学业成绩
			
			//遍历文化课成绩
			count=0;
			for(int i=0;i<16;i+=4){
				for(int k=0;k<subVec2.size()/2;k++){
					if(count<scoreVec2.size()){
							fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
							fsz=MyTools.StringToDouble(fs);
							if(fsz<60){
								content = new Label(23+i, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red1);
							}else
							content = new Label(23+i, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle);
						 	wsheet.addCell(content);	
						 	count+=1;
						 	fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
							fsz=MyTools.StringToDouble(fs);
							if(fsz<60){
								content = new Label(23+i+1, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red1);
							}else
						 	content = new Label(23+i+1,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle);
						 	wsheet.addCell(content);	
							count+=1;
							fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
							fsz=MyTools.StringToDouble(fs);
							if(fsz<60){
								content = new Label(23+i+2, (60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red1);
							}else
							content = new Label(23+i+2,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle);	
							wsheet.addCell(content);	
							count+=1;
							
							fs=MyTools.StrFiltr(scoreVec2.get(count)).equalsIgnoreCase("")?"0":MyTools.StrFiltr(scoreVec2.get(count));
							fsz=MyTools.StringToDouble(fs);
							if("不及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec2.get(count)))){
								content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red2);	
							}else if("及格".equalsIgnoreCase(MyTools.StrFiltr(scoreVec2.get(count)))){
								content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle4);	
							}else if(fsz<60){
								content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle_red2);	
							}else
								content = new Label(23+i+3,(60+subVec.size()/2+k),MyTools.StrFiltr(scoreVec2.get(count)), contentStyle4);	
							
							wsheet.addCell(content);	
							count+=1;
						}
				}
			}
			//实习情况
			int cellnum=0;
			if((60+subVec.size()/2+subVec2.size()/2)<82){
				cellnum=84;
			}else{
				cellnum=(61+subVec.size()/2+subVec2.size()/2+3);
			}
			
			 wsheet.mergeCells(20, (60+(subVec.size()/2)+(subVec2.size()/2)), 20, cellnum);
			 content = new Label(20, (60+(subVec.size()/2)+(subVec2.size()/2)),"实\n习\n状\n况", contentStyle);
			 wsheet.addCell(content);
			
			 wsheet.mergeCells(21, (60+(subVec.size()/2)+(subVec2.size()/2)), 38, cellnum);
			 content = new Label(21, (60+(subVec.size()/2)+(subVec2.size()/2)),"", contentStyle);
			 wsheet.addCell(content);
			
		
		   
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
					 viewFilePath2=viewFilePath1+"学籍卡"+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls"; 
				 }else{
					 viewFilePath2=viewFilePath1+URLDecoder.decode(URLDecoder.decode(StringEscapeUtils.escapeJava(MyTools.StrFiltr(this.getJX_XM())), "utf-8"), "utf-8")+year+((month+1)<10?"0"+(month+1):(month+1))+(date<10?"0"+date:date)+hour+minute+secondTime+".xls"; 
				 }
				
				File file2=new File(viewFilePath2);
				file.renameTo(file2);
				this.setMSG("修改成功");
			} 
			 return viewFilePath2;
	    }
		
	 //删除照片
	public void delZPLJ(String JX_XHZP) throws SQLException {
		// TODO Auto-generated method stub
		String url=MyTools.getProp(request, "Base.headImg");
		String url1=MyTools.getProp(request, "Base.Photo");
		sql="update V_学生基本数据子类 set 照片路径='' where  身份证件号='"+MyTools.fixSql(JX_XHZP)+"'";
		if("".equalsIgnoreCase(JX_XHZP)){
			File folder = new File(url);
			File temp=null;
			File[] filelist= folder.listFiles();//列出文件里所有的文件
			for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
				temp=filelist[i];
				if(temp.getName().indexOf(JX_XHZP)>=0){
					temp.delete();//删除文件
				}
			}
			
		}else{
			File folder = new File(url1);
			File temp=null;
			File[] filelist= folder.listFiles();//列出文件里所有的文件
			for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
				temp=filelist[i];
				if(temp.getName().indexOf(JX_XHZP)>=0){
					temp.delete();//删除文件
				}
			}
		}
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	
	 //删除预览照片
		public void emptyfile() throws SQLException {
			// TODO Auto-generated method stub
			String savePath = MyTools.getProp(request, "Base.headImg");
			//String url = savePath + "/studentPhoto/" ;
			String url = savePath;
			File file = new File(url);
			File[] showfile=file.listFiles();
			if(showfile.length>0){
				for(File photofile:showfile){
					photofile.delete();
				}
			}
		}
	
	 //删除根目录重复照片
	public void delXTZP(String sfz,String url) throws SQLException {		
		File readDir=new File(url);
		File showfile[]=readDir.listFiles();
		for(File photofile:showfile){
			if (photofile.isDirectory()) {
			}else {
				String gmlsfz[]=photofile.getName().toString().split("-");
				if(gmlsfz[0].equalsIgnoreCase(sfz)) {
					deleteGeneralFile(photofile.toString().replace("\\", "\\\\"));//删除文件
				}
			}
		}
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
	
	
}