package com.pantech.devolop.baseInfoManage;
/*
@date 2016.01.08
@author yeq
模块：M1.9班级设置
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
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.MyTools;

public class BjxsxxBean {
	private String USERCODE;//用户编号
	private String BJBH;//班级编号
	private String BJMC;//班级名称
	private String XBDM;//系部代码
	private String NJDM;//年级代码
	private String SSZY;//所属专业
	private String ZRS;//总人数
	private String BZR;//班主任
	private String BJJC;//班级简称
	private String BJLX;//班级类型
	private String JSBH;//教室编号
	private String Auth;//用户权限
	private String XJH;//学籍号
	private HttpServletRequest request;
	private DBSource db;
	private String MSG;  //提示信息
	
	/**
	 * 构造函数
	 * @param request
	 */
	public BjxsxxBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化变量
	 * @date:2015-05-15
	 * @author:yeq
	 * 
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加每周天数,上午节数,下午节数,晚上节数以及节次时间设置基础信息
	 */
	public void initialData() {
		USERCODE = "";//用户编号
		Auth = "";//用户权限
		BJBH = "";//班级编号
		BJMC = "";//班级名称
		XBDM = "";//系部代码
		NJDM = "";//年级代码
		SSZY = "";//所属专业
		ZRS = "";//总人数
		BZR = "";//班主任
		BJJC = "";//班级简称
		XJH = "";//学籍号
		MSG = "";    //提示信息
	}




	
	/**
	 * 
	 * @date:2017-03-17
	 * @author:Zouyu
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector StuXq(String XH)throws SQLException,WrongSQLException{
		String sql = "";
		String sql2= "";
		Vector vec = null;
		sql="select a.学籍号,a.学号,a.班内学号,a.行政班代码,a.姓名, " +
			"a.英文姓名,a.姓名拼音,a.曾用名,a.身份证件类型码,a.身份证件号," +
			"a.性别码,a.血型码,a.出生日期,a.出生地码,a.籍贯,a.民族码,a.婚姻状况码,a.信仰宗教码,a.港澳台侨外码,a.健康状况码," +
			"a.政治面貌码,a.户口所在地行政区划码,a.户口类别码, a.是否是流动人口, a.[国籍/ 地区码] as 国籍,a.电子信箱,a.照片," +
			"a.照片路径,a.常住地址,a.户籍地址,a.户籍邮编,a.家庭邮编,a.家长手机,a.是否本市户籍,a.学生状态,a.家庭电话," +
			"a.奖惩,CONVERT(date,a.毕业离校日期) as 毕业离校日期,a.人员类型,a.学生类别,a.招生类别,c.类别名称 as 性别名称," +
			"CONVERT(nvarchar,a.入学日期,21) as 入学日期,a.毕业年份,a.拍照号 ,a.备注,a.所属派出所,a.毕业学校,a.父亲 ,a.父亲年龄, " +
			"a.父亲政治面貌,a.父亲工作单位,a.父亲电话,a.母亲,a.母亲年龄,a.母亲政治面貌,a.母亲工作单位,a.母亲电话,a.其他成员称谓," +
			"a.其他成员姓名,a.其他成员年龄,a.其他成员政治面貌,a.其他成员工作单位,a.其他成员电话,c.类别名称 as 身份证件," +
			"d.类别名称 as 学生类别,e.类别名称 as 民族,f.类别名称 as 政治面貌,g.类别名称 as 健康状况,h.类别名称 as 婚姻状况,i.类别名称 as 是否是流动人口,j.类别名称 as 港澳台侨外,k.类别名称 as 血型,l.类别名称 as 国籍,m.类别名称 as 籍贯,n.类别名称 as 出生地,o.类别名称 as 户口类别,p.类别名称 as 母亲政治面貌,q.类别名称 as 父亲政治面貌,r.类别名称 as 其他成员政治面貌 "+
			"from V_学生基本数据子类 a " +
			"left join dbo.V_信息类别_类别操作 b on a.性别码=b.描述  and b.父类别代码='XBM' " +
			"left join dbo.V_信息类别_类别操作 c on a.身份证件类型码=c.描述  and c.父类别代码='SFZJLXM' " +
			"left join dbo.V_信息类别_类别操作 d on a.学生类别=d.描述  and d.父类别代码='XSLBM' " +
			"left join dbo.V_信息类别_类别操作 e on a.民族码=e.描述  and e.父类别代码='MZDM' " +
			"left join dbo.V_信息类别_类别操作 f on a.政治面貌码=f.描述  and f.父类别代码='ZZMMDM' " +
			"left join dbo.V_信息类别_类别操作 g on a.健康状况码=g.描述  and g.父类别代码='JKZTM' " +
			"left join dbo.V_信息类别_类别操作 h on a.婚姻状况码=h.描述  and h.父类别代码='HYZKM' " +
			"left join dbo.V_信息类别_类别操作 i on a.是否是流动人口=i.描述  and i.父类别代码='LDRKZKM' " +
			"left join dbo.V_信息类别_类别操作 j on a.港澳台侨外码=j.描述  and j.父类别代码='GATQDM' " +
			"left join dbo.V_信息类别_类别操作 k on a.血型码=k.描述  and k.父类别代码='XXDM' " +
			"left join dbo.V_信息类别_类别操作 l on a.[国籍/ 地区码]=l.描述  and l.父类别代码='GBDM' " +
			"left join dbo.V_信息类别_类别操作 m on a.籍贯=m.描述  and m.父类别代码='CSDM' " +
			"left join dbo.V_信息类别_类别操作 n on a.出生地码=n.描述  and n.父类别代码='GBDM' " +
			"left join dbo.V_信息类别_类别操作 o on a.户口类别码=o.描述  and o.父类别代码='HKXZM' " +
			"left join dbo.V_信息类别_类别操作 p on a.母亲政治面貌=c.描述  and p.父类别代码='ZZMMDM'  " +
			"left join dbo.V_信息类别_类别操作 q on a.父亲政治面貌=c.描述  and q.父类别代码='ZZMMDM' " +
			"left join dbo.V_信息类别_类别操作 r on a.其他成员政治面貌=c.描述  and r.父类别代码='ZZMMDM' "+
			"where a.学号='"+ MyTools.fixSql(XH)+"'" ;
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	/**
	 * 分页查询 班级列表
	 * @date:2016-01-08
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @param BJBH_CX 行政班代码
	 * @param BJMC_CX 行政班名称
	 * @param NJDM_CX 年级代码
	 * @param SSZY_CX 所属专业
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec = null; // 结果集
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		sql+="select distinct t.班级代码 as BJBH,t.班级名称 as BJMC ,t.年级代码 as NJDM,t.年级名称 AS NJMC,t.系部代码 AS XBDM,t.系部名称 AS XBMC,t.班主任工号 AS BZRGH,t.专业代码 AS ZYDM,t.专业名称 AS ZYMC from ( " +
				"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称,c.年级代码,c.年级名称,b.系部代码,b.系部名称,a.班主任工号,d.专业代码,d.专业名称 " +
				"from V_学校班级数据子类 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
				"union all " +
				"select distinct a.教学班编号 as 班级代码,a.教学班名称 as 班级名称,c.年级代码,c.年级名称,b.系部代码,b.系部名称,a.班主任工号,d.专业代码,d.专业名称 " +
				"from V_基础信息_教学班信息表 a " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码 " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
				") t where 1=1 " ;
				
			if(this.getAuth().indexOf(bzr) > -1){
				sql += "and  t.班主任工号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%' ";
			}
		
				
		if(!"".equalsIgnoreCase(this.BJBH)){
			sql += " and t.班级代码 = '" + MyTools.fixSql(this.BJBH) + "'";
		}
		if(!"".equalsIgnoreCase(this.XBDM)){
			sql += " and t.系部代码='" + MyTools.fixSql(this.XBDM) + "'";
		}
		if(!"".equalsIgnoreCase(this.NJDM)){
			sql += " and t.年级代码='" + MyTools.fixSql(this.NJDM) + "'";
		}
		if(!"".equalsIgnoreCase(this.SSZY)){
			sql += " and t.专业代码='" + MyTools.fixSql(this.SSZY) + "'";
		}
		sql+=" order by t.班级代码 desc ";
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		return vec;
	}
	
	/**
	 * 读取年级下拉框
	 * @date:2016-01-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadNjdmCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,0 as orderNum " +
				"union all " +
				"select distinct 年级名称 as comboName,年级代码 as comboValue,1 " +
				"from V_学校年级数据子类 where 年级状态='1' " +
				"order by orderNum,comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取系部下拉框
	 * @date:2017-03-10
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
	 * 读取系部下拉框
	 * @date:2017-03-10
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadBJLXCombo() throws SQLException{
		String bzr = MyTools.getProp(request, "Base.bzr");//班主任
		Vector vec = null;
		String sql="";
			sql="select '' as comboValue ,'请选择' as comboName,'0' as orderNum " +
				"union all " +
				"select distinct t.班级代码,t.班级名称,'1' as orderNum from ( " +
				"select a.行政班代码 as 班级代码,a.行政班名称 as 班级名称 " +
				"from V_学校班级数据子类 a  " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码  " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码 " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码 " +
				"where 1=1 " ;
			//权限判断
			if(this.getAuth().indexOf(bzr) > -1){
				sql += " and a.班主任工号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%'";
			}
				
			sql+="union all " +
				"select a.教学班编号 as 班级代码,a.教学班名称 as 班级名称 " +
				"from V_基础信息_教学班信息表 a  " +
				"left join V_基础信息_系部信息表 b on b.系部代码=a.系部代码  " +
				"left join V_学校年级数据子类 c on c.年级代码=a.年级代码  " +
				"left join V_专业基本信息数据子类 d on d.专业代码=a.专业代码  " +
				"left join V_教职工基本数据子类 e on a.班主任工号=e.工号 " +
				"where 1=1";
				//权限判断
				if(this.getAuth().indexOf(bzr) > -1){
					sql += " and a.班主任工号 like '%" + MyTools.fixSql(this.getUSERCODE()) + "%'";
				}
			sql+=	") t " +
				"order by orderNum,comboValue desc ";
					
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取专业下拉框
	 * @date:2016-01-08
	 * @author:yeq
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadMajorCombo() throws SQLException{
		Vector vec = null;
		String sql = "select '请选择' as comboName,'' as comboValue,'0' as orderNum " +
				"union all " +
				"select distinct 专业名称+'('+专业代码+')' as comboName,专业代码 as comboValue,'1' as orderNum " +
				"from V_专业基本信息数据子类  order by orderNum,comboValue";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
	}


	
	/**
	 * 查询班级学生列表
	 * @date:2017-03-06
	 * @author:yeq
	 * @param pageNum
	 * @param page
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadStuList(int pageNum, int pageSize) throws SQLException {
		String sql = ""; // 查询用SQL语句
		Vector vec=new Vector(); // 结果集
		sql="select distinct a.学号 as XH,a.姓名 AS XM,b.专业名称 AS ZYMC,c.类别名称  as ZSLB " +
				"from V_学生基本数据子类 a "+
				"left join V_专业基本信息数据子类 b on a.专业代码=b.专业代码 "+
				"left join V_信息类别_类别操作 c on a.招生类别=c.描述 and c.父类别代码='ZSLBM' ";
		//教学班
		if("JXB_".equalsIgnoreCase(MyTools.StrFiltr(this.getBJBH()).substring(0, 4))){
			sql+="where  a.学号 in (select 学号 from V_基础信息_教学班学生信息表 where 教学班编号='"+MyTools.fixSql(this.getBJBH())+"')";
		//行政班			
		}else{
			sql+="where  a.行政班代码='"+MyTools.fixSql(this.getBJBH())+"'";
			
		}
		vec=db.getConttexJONSArr(sql, pageNum,pageSize);
	
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
		}
		sql = sql.substring(0, sql.length()-1);
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
			//教学班
			if("JXB_".equalsIgnoreCase(MyTools.StrFiltr(bjdm).substring(0, 4))){
				sql+="where  a.学号 in (select 学号 from V_基础信息_教学班学生信息表 where 教学班编号='"+MyTools.fixSql(bjdm)+"')";
			//行政班			
			}else{
				sql+="where  a.行政班代码='"+MyTools.fixSql(bjdm)+"'";
				
			}
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
				WritableSheet wsheet = wbook.createSheet(bjdm, 0);// 工作表名称
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
				wsheet.setColumnView(14, 20);
				wsheet.setColumnView(15, 15);
				wsheet.setColumnView(16, 30);
				wsheet.setColumnView(17, 45);
				wsheet.setColumnView(18, 25);
				wsheet.setColumnView(19, 20);
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
				wsheet.setColumnView(41, 30);
				// 设置title
				fontStyle = new WritableFont(WritableFont.createFont("宋体"), 12,WritableFont.BOLD);
				contentStyle=new WritableCellFormat(fontStyle);
				contentStyle.setAlignment(jxl.format.Alignment.CENTRE);// 水平居中
				contentStyle.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 垂直居中
				//加的标题				
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
				// 输出流
				savePath += "/" + bjbh
						+ "学生信息（全部学生）" + year+ ((month + 1) < 10 ? "0" + (month + 1) : (month + 1))+ (date < 10 ? "0" + date : date) + ".xls";
				OutputStream os = new FileOutputStream(savePath);
				WritableWorkbook wbook = Workbook.createWorkbook(os);// 建立excel文件
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
	
	//GET && SET 方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getBJBH() {
		return BJBH;
	}

	public void setBJBH(String bJBH) {
		BJBH = bJBH;
	}

	public String getBJMC() {
		return BJMC;
	}

	public void setBJMC(String bJMC) {
		BJMC = bJMC;
	}

	public String getXBDM() {
		return XBDM;
	}

	public void setXBDM(String xBDM) {
		XBDM = xBDM;
	}

	public String getNJDM() {
		return NJDM;
	}

	public void setNJDM(String nJDM) {
		NJDM = nJDM;
	}

	public String getSSZY() {
		return SSZY;
	}

	public void setSSZY(String sSZY) {
		SSZY = sSZY;
	}

	public String getZRS() {
		return ZRS;
	}

	public void setZRS(String zRS) {
		ZRS = zRS;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getBZR() {
		return BZR;
	}

	public void setBZR(String bZR) {
		BZR = bZR;
	}

	public String getBJJC() {
		return BJJC;
	}

	public void setBJJC(String bJJC) {
		BJJC = bJJC;
	}

	public String getBJLX() {
		return BJLX;
	}

	public void setBJLX(String bJLX) {
		BJLX = bJLX;
	}

	public String getJSBH() {
		return JSBH;
	}

	public void setJSBH(String jSBH) {
		JSBH = jSBH;
	}

	public String getAuth() {
		return Auth;
	}

	public void setAuth(String auth) {
		Auth = auth;
	}

	public String getXJH() {
		return XJH;
	}

	public void setXJH(String xJH) {
		XJH = xJH;
	}

	
	
	
}