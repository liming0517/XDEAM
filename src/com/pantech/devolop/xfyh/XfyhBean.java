package com.pantech.devolop.xfyh;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class XfyhBean {
	
	private String USERCODE;//用户编号
	private String XX_ID;//编号
	private String XX_XNXQBM;//学年学期编码
	private String XX_XH;// 学号
	private String XX_XM;// 姓名 
	private String XX_BJBH;// 班级编号
	private String XX_BJMC; // 班级名称
	private String XX_XGBH; // 相关编号
	private String XX_CJ;// 成绩
	private String XX_JF;// 加分
	private String XX_BZ; // 备注
	private String XX_SHZT; // 审核状态
	private String XX_SHXX;// 审核信息
	private String XX_SHR;// 审核人
	private String XX_SHSJ;// 审核时间
	private String XX_CJR;// 创建人
	private String XX_CJSJ;// 创建时间
	private String XX_ZT;// 状态
	
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
		USERCODE = "";//用户编号
		XX_ID = "";//编号
		XX_XNXQBM = "";//学年学期编码
		XX_XH = "";// 学号
		XX_XM = "";// 姓名 
		XX_BJBH = "";// 班级编号
		XX_BJMC = ""; // 班级名称
		XX_XGBH = ""; // 相关编号
		XX_CJ = "";// 成绩
		XX_JF = "";// 加分
		XX_BZ = ""; // 备注
		XX_SHZT = ""; // 审核状态
		XX_SHXX = "";// 审核信息
		XX_SHR = "";// 审核人
		XX_SHSJ = "";// 审核时间
		XX_CJR = "";// 创建人
		XX_CJSJ = "";// 创建时间
		XX_ZT = "";// 状态
	}
	
	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public XfyhBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}
	
	
	/**
	 * 读取当前学年学期
	 * @date:2017-09-29
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxq() throws SQLException {
		String curXnxq = "";
		Vector vec = null;
		String sql = "select top 1 学年学期名称 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		vec = db.GetContextVector(sql);

		if (vec != null && vec.size() > 0) {
			curXnxq = MyTools.StrFiltr(vec.get(0));
		}

		return curXnxq;
	}
	
	/**
	 * 读取当前学年学期
	 * @date:2017-09-30
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadCurXnxqbm() throws SQLException {
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
	 * 读取学年学期下拉框
	 * @date:2017-09-30
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo() throws SQLException {
		Vector vec = null;
		String sql = " select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName FROM V_规则管理_学年学期表 order by comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		
		return vec;
		
	}
	
	/**
	 * 读取学年学期下拉框(审核)
	 * @date:2017-09-30
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo2() throws SQLException{
		Vector vec = null;
		String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName,'2' as px  FROM V_规则管理_学年学期表 where 1=1) e order by e.px,e.comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取审核状态下拉框(审核)
	 * @date:2017-09-30
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadSHZTCombo2() throws SQLException{
		Vector vec = null;
		String sql = " select comboValue,comboName from ( " +
					 " select '' as comboValue,'请选择' as comboName,'1' as px " + 
					 " union  " +
					 " select '1'  AS comboValue,'待审核' AS comboName,'2' as px  " + 
					 " union  " +
					 " select '2'  AS comboValue,'审核通过' AS comboName,'2' as px   " +
					 " union  " +
					 " select '3'  AS comboValue,'审核驳回' AS comboName,'2' as px " +  
					 " FROM V_规则管理_学年学期表 where 1=1 " +
					 " ) e order by e.px,e.comboValue "; 
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取当前学年学期考试成绩
	 * @date:2017-09-29
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadKscj(String kcmc, String xnxqmc) throws SQLException {
		String curXnxq = "";
		Vector vec = null;
		String sql = "";
		
		sql = " select case when a.期末 >= 0 then a.期末 else c.名称 end,case when a.期末 >= 0 then '正常分数' else '特殊成绩无法加分' end  from V_成绩管理_学生成绩信息表 a   " +
			  " left join dbo.V_成绩管理_科目课程信息表 b " +
			  " on a.科目编号 = b.科目编号 " +
			  " left join  [V_成绩管理_文字成绩代码表] c " +
			  " on a.期末 = c.代码 " +
			  " where a.学号='" + MyTools.fixSql(this.getUSERCODE()) + "' and a.相关编号= '" + MyTools.fixSql(kcmc) + "' and b.学年学期名称 = '" + MyTools.fixSql(xnxqmc) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec.size() > 0){
			curXnxq = MyTools.StrFiltr(vec.get(0));
			this.setMSG(MyTools.StrFiltr(vec.get(1)));
		}

		return curXnxq;
	}
	
	/**
	 * 读取当前学年学期本学科最高成绩
	 * @date:2017-09-29
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadZgcj(String kcmc, String xnxqmc) throws SQLException {
		String curZgcj = "";
		Vector vec = null;
		String sql = "";
		
		sql = " select max(cast(a.期末 as int)) from V_成绩管理_学生成绩信息表 a " +
			  " left join dbo.V_成绩管理_科目课程信息表 b " +
			  " on a.科目编号 = b.科目编号 " +
			  " where a.相关编号= '" + MyTools.fixSql(kcmc) + "' and b.学年学期名称 = '" + MyTools.fixSql(xnxqmc) + "'";
		vec = db.GetContextVector(sql);
		
		if(vec.size() > 0){
			curZgcj = MyTools.StrFiltr(vec.get(0));
		}

		return curZgcj;
	}
	
	/**
	 * 读取学分申请列表
	 * @date:2017-09-29
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String xnxqbm ="";
		
		sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		vec2 = db.GetContextVector(sql);
		if(vec2.size() > 0){
			xnxqbm = MyTools.StrFiltr(vec2.get(0));
		}	
		sql = " select a.学年学期编码 as XNXQBM, d.课程名称 as KCMC, a.成绩 as QM, a.加分 as JF, a.备注 as BZ, CONVERT(varchar(100),a.创建时间,23) as CJSJ, a.审核状态 as SHZT, a.审核信息 as SHXX, a.编号 as ID, a.相关编号 as XGBH, b.学年学期名称 as XNXQMC from dbo.V_学分管理_学生加分申请信息表 a " +
			  " left join V_成绩管理_登分教师信息表 d " + 
			  " on d.相关编号 = a.相关编号   " +
			  " left join dbo.V_规则管理_学年学期表 b " +
			  " on b.学年学期编码= a.学年学期编码 " +
			  " where a.学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' and 学号='" + MyTools.fixSql(this.getUSERCODE()) + "' order by a.创建时间 desc  ";
		
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取学生学分申请列表
	 * @date:2017-09-30
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec2(int pageNum, int page, String kcmc) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String sql2 = "";
		String xgbh = "";
		
		sql = " select c.学年学期名称 as XNXQMC,a.学号 as XH,a.姓名 as XM,a.班级名称 as BJMC,b.课程名称 as KCMC,a.成绩 as CJ,a.加分 as JF,a.备注 as BZ,a.编号 as ID,a.审核状态 as SHZT from dbo.V_学分管理_学生加分申请信息表 a " +
			  " left join dbo.V_成绩管理_登分教师信息表 b " +
			  " on a.相关编号 = b.相关编号 " +
			  " left join V_成绩管理_科目课程信息表 c " +
			  " on c.科目编号 = b.科目编号 " +
			  " where 1=1 and a.审核状态 in ('1','2','3') ";
		
		//任课教师权限
		sql2 = " select 相关编号 from dbo.V_成绩管理_登分教师信息表 where 登分教师编号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' ";
		vec2 = db.GetContextVector(sql2);
		if(vec2.size() > 0){
			for (int i = 0; i < vec2.size(); i++) {
				xgbh += "'" + vec2.get(i) + "',";
			}
			xgbh = xgbh.substring(0, xgbh.length()-1);
			
			sql += " and b.相关编号  in (" + xgbh + ") ";
		}else{
			//系部教师权限
			sql2 = " select 相关编号 from dbo.V_成绩管理_登分教师信息表 where 行政班代码 in (select 行政班代码 from dbo.V_学校班级数据子类 where 系部代码 in (select 系部代码 from dbo.V_基础信息_系部教师信息表 where 教师编号 = '" + MyTools.fixSql(this.getUSERCODE()) + "')) ";
			vec2 = db.GetContextVector(sql2);
			if(vec2.size() > 0){
				for (int i = 0; i < vec2.size(); i++) {
					xgbh += "'" + vec2.get(i) + "',";
				}
				xgbh = xgbh.substring(0, xgbh.length()-1);
				
				sql += " and b.相关编号  in (" + xgbh + ") ";
			}
		}
		
		if(!"".equalsIgnoreCase(this.getXX_XNXQBM())){
			sql += " AND c.学年学期编码 ='" + MyTools.fixSql(this.getXX_XNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getXX_XH())){
			sql += " AND a.学号 like '%" + MyTools.fixSql(this.getXX_XH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXX_XM())){
			sql += " AND a.姓名 like '%" + MyTools.fixSql(this.getXX_XM()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXX_BJMC())){
			sql += " AND a.班级名称 like '%" + MyTools.fixSql(this.getXX_BJMC()) + "%'";
		}
		if(!"".equalsIgnoreCase(kcmc)){
			sql += " AND b.课程名称 like '%" + MyTools.fixSql(kcmc) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXX_SHZT())){
			sql += " AND a.审核状态 ='" + MyTools.fixSql(this.getXX_SHZT()) + "'";
		}
		
		sql += " order by a.创建时间 desc ";
		
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 读取课程名称下拉框
	 * @date:2017-09-29
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadKCMCCombo() throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String xnxqbm = "";
		String sql="";
		
		sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		vec2 = db.GetContextVector(sql);
		if(vec2.size() > 0){
			xnxqbm = MyTools.StrFiltr(vec2.get(0));
		}	
		
		
		sql = " select comboValue,comboName from ( " +
					 " select '' as comboValue,'请选择' as comboName,'1' as px  " +
					 " union  " +
					 " select distinct a.相关编号  AS comboValue,b.课程名称 AS comboName,'2' as px from dbo.V_成绩管理_学生成绩信息表 a " +
					 " left join dbo.V_成绩管理_科目课程信息表 b " +
					 " on a.科目编号 = b.科目编号 " +
					 " where a.学号 = '" + MyTools.fixSql(this.getUSERCODE()) + "'  and b.课程类型 <> '3' and b.学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "'  " +
					 " ) e order by e.px,e.comboValue desc ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取学生学分
	 * @date:2017-09-29
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadcurXf() throws SQLException {
		String curXf = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		Vector vec4 = null;
		Vector vec5 = null;
		String sql = "";
		String xnxqbm = "";
		
		sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		vec2 = db.GetContextVector(sql);
		
		if(vec2.size() > 0){
			xnxqbm = MyTools.StrFiltr(vec2.get(0));
		}	
		
		//总分
		sql = " select a.姓名,a.学号,e.行政班名称,e.行政班代码,isnull(SUM(cast(case when ISNUMERIC(a.总评) = 1 then a.总评 else 0 end as decimal(18,2))),0) as 总分 from dbo.V_成绩管理_学生成绩信息表 a " +
			  " left join dbo.V_成绩管理_科目课程信息表 b " +
			  " on a.科目编号 = b.科目编号 " +
			  " left join dbo.V_成绩管理_登分教师信息表 c " +
			  " on c.科目编号 = a.科目编号 " +
			  " left join dbo.V_学生基本数据子类 d " +
			  " on a.学号 = d.学号 " +
			  " left join dbo.V_学校班级数据子类 e " +
			  " on e.行政班代码 = d.行政班代码 " +
			  " where a.学号 = '" + MyTools.fixSql(this.getUSERCODE()) + "' and c.来源类型 ='3' and b.学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' " +
			  " group by a.姓名,a.学号,e.行政班名称,e.行政班代码  ";
		vec = db.GetContextVector(sql);
		
		//获奖获得的额外学分
		sql = " select isnull(SUM(cast(学分 as decimal(18,2))),0) from V_学分管理_教师学分设置信息表 where 学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' and 学号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' ";
		vec5 = db.GetContextVector(sql);
		
		
		//已用分数
		sql = " select isnull(SUM(CAST( 加分 as decimal(18,2))),0) as 已用分数 from dbo.V_学分管理_学生加分申请信息表 " +
			  " where 学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' and 学号 = '" + MyTools.fixSql(this.getUSERCODE()) + "' and 审核状态 = '2' ";
		
		vec3 = db.GetContextVector(sql);
		
		//占用分数
		sql = " select isnull(SUM(CAST( 加分 as decimal(18,2))),0) as 占用分数 from dbo.V_学分管理_学生加分申请信息表 " +
			  " where 学年学期编码 = '" + MyTools.fixSql(xnxqbm) + "' and 学号 = '" + MyTools.fixSql(this.getUSERCODE()) + "' and 审核状态 = '1' ";
		
		vec4 = db.GetContextVector(sql);
		
		if(vec.size() > 0){
			String xm = MyTools.StrFiltr(vec.get(0));
			String xh = MyTools.StrFiltr(vec.get(1));
			String xzbmc = MyTools.StrFiltr(vec.get(2));
			String xzbdm = MyTools.StrFiltr(vec.get(3));
			double zf_1 = Double.parseDouble(MyTools.StrFiltr(vec.get(4)));
			double yyfs_1 = Double.parseDouble(MyTools.StrFiltr(vec3.get(0)));
			double zyfs_1 = Double.parseDouble(MyTools.StrFiltr(vec4.get(0)));
			
			if(vec5.size()>0){
				zf_1 = zf_1 + Double.parseDouble(MyTools.StrFiltr(vec5.get(0)));
			}
			
			double kyfs_1 = zf_1-yyfs_1-zyfs_1;
			
			String zf = zf_1+"";
			String yyfs = yyfs_1+"";
			String kyfs = kyfs_1+"";
			
			curXf = "{\"xm\":\"" + xm + "\", " +
					"\"xh\":\"" + xh + "\", " +
					"\"xzbmc\":\"" + xzbmc + "\", " +
					"\"xzbdm\":\"" + xzbdm + "\", " +
					"\"zf\":\"" + zf + "\", " +
					"\"yyfs\":\"" + yyfs + "\", " +
					"\"kyfs\":\"" + kyfs + "\"} ";
			
		}else{
			
			String xm = "";
			String xh = "";
			String xzbmc = "";
			String xzbdm = "";
			
			sql = " select a.姓名,a.学号,b.行政班名称,b.行政班代码 from dbo.V_学生基本数据子类 a " +
				  " left join dbo.V_学校班级数据子类 b " +
				  " on a.行政班代码 = b.行政班代码 " +
				  " where a.学号 = '" + MyTools.fixSql(this.getUSERCODE()) + "' ";
			vec = db.GetContextVector(sql);
			
			if(vec.size() > 0){
				xm = MyTools.StrFiltr(vec.get(0));
				xh = MyTools.StrFiltr(vec.get(1));
				xzbmc = MyTools.StrFiltr(vec.get(2));
				xzbdm = MyTools.StrFiltr(vec.get(3));
			}
			double yyfs_1 = Double.parseDouble(MyTools.StrFiltr(vec3.get(0)));
			double zyfs_1 = Double.parseDouble(MyTools.StrFiltr(vec4.get(0)));
			double zf_1 = 0.00;
			if(vec5.size()>0){
				zf_1 = zf_1 + Double.parseDouble(MyTools.StrFiltr(vec5.get(0)));
			}
			double kyfs_1 = zf_1-yyfs_1-zyfs_1;
			
			String zf = zf_1+"";
			String yyfs = yyfs_1+"";
			String kyfs = kyfs_1+"";
			
			curXf = "{\"xm\":\"" + xm + "\", " +
					"\"xh\":\"" + xh + "\", " +
					"\"xzbmc\":\"" + xzbmc + "\", " +
					"\"xzbdm\":\"" + xzbdm + "\", " +
					"\"zf\":\"" + zf + "\", " +
					"\"yyfs\":\"" + yyfs + "\", " +
					"\"kyfs\":\"" + kyfs + "\"} ";
			
		}
		

		return curXf;
	}
	
	/**
	 * 保存方法
	 * @date:2017-05-26
	 * @author:yangda
	 * @throws SQLException
	 */
	public String saveRec(String tj, String tj2, String xnxqmc) throws SQLException{
		String sql = "";
		String bh = "";
		//判断数据是否存在
		if(!"".equalsIgnoreCase(this.getXX_ID()) && "edit".equalsIgnoreCase(tj)){
			bh = this.modRec();//修改
		}else{
			bh = this.addRec(xnxqmc,tj2);//新增数据
		}
		return bh;
	}
	
	
	/**
	 * 保存学生申请
	 * @date:2017-09-29
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String addRec(String xnxqmc, String tj2) throws SQLException {
		Vector vec = null;
		String sql="";
		String str = "";
		
		sql = " select 学年学期编码 from dbo.V_规则管理_学年学期表 where 学年学期名称 = '" + MyTools.fixSql(xnxqmc) + "' ";
		vec = db.GetContextVector(sql);
		
		String xmxqbm = MyTools.StrFiltr(vec.get(0));
		
		if("".equalsIgnoreCase(tj2)){
			sql = " insert into dbo.V_学分管理_学生加分申请信息表 (学年学期编码, 学号, 姓名, 班级编号, 班级名称, 相关编号, 成绩, 加分, 备注, 审核状态, 创建人, 创建时间, 状态 ) values ( " +
				  " '" + MyTools.fixSql(xmxqbm) +"', " +
				  " '" + MyTools.fixSql(this.getXX_XH()) + "', " +
				  " '" + MyTools.fixSql(this.getXX_XM()) + "', " +
				  " '" + MyTools.fixSql(this.getXX_BJBH()) + "', " +
				  " '" + MyTools.fixSql(this.getXX_BJMC()) + "', " +
				  " '" + MyTools.fixSql(this.getXX_XGBH()) + "', " +
				  " '" + MyTools.fixSql(this.getXX_CJ()) + "', " +
				  " '" + MyTools.fixSql(this.getXX_JF()) + "', " +
				  " '" + MyTools.fixSql(this.getXX_BZ()) + "', " +
				  " '0', " +
				  " '" + MyTools.fixSql(this.getUSERCODE()) + "', " +
				  " getdate() ,'1')  ";
			
			if(db.executeInsertOrUpdate(sql)){
				
				sql = " select top 1 编号 from V_学分管理_学生加分申请信息表 where 学年学期编码 ='" + MyTools.fixSql(xmxqbm) + "' and 学号='" + MyTools.fixSql(this.getXX_XH()) + "' and 相关编号='" + MyTools.fixSql(this.getXX_XGBH()) + "' and 审核状态 = '0' order by 创建时间 desc  ";
				vec = db.GetContextVector(sql);
				
				if(vec.size() > 0){
					str = (String) vec.get(0);
				}
				
				this.MSG = "保存成功";
			}else{
				this.MSG = "保存失败";
			}
		}else{
			
			sql = " update dbo.V_学分管理_学生加分申请信息表  set 相关编号 = '" 
					+ MyTools.fixSql(this.getXX_XGBH()) + "', 成绩 = '" 
					+ MyTools.fixSql(this.getXX_CJ()) + "', 加分 = '" 
					+ MyTools.fixSql(this.getXX_JF()) + "', 备注 = '" 
					+ MyTools.fixSql(this.getXX_BZ()) + "' where 编号 = '" 
					+ MyTools.fixSql(this.getXX_ID()) + "'";
			
			if(db.executeInsertOrUpdate(sql)){
				this.setMSG("保存成功");
			}else{
				this.setMSG("保存失败");
			}
			
			str = this.getXX_ID();
			
			
		}
		
		return str;
		
	}
	
	/**
	 * 修改学分申请
	 * @date:2017-09-30
	 * @author:yangda
	 * @throws SQLException
	 */
	public String modRec() throws SQLException{
		String str = "";
		String sql = "";
		sql = " update dbo.V_学分管理_学生加分申请信息表  set 相关编号 = '" 
				+ MyTools.fixSql(this.getXX_XGBH()) + "', 成绩 = '" 
				+ MyTools.fixSql(this.getXX_CJ()) + "', 加分 = '" 
				+ MyTools.fixSql(this.getXX_JF()) + "', 备注 = '" 
				+ MyTools.fixSql(this.getXX_BZ()) + "' where 编号 = '" 
				+ MyTools.fixSql(this.getXX_ID()) + "'";
		
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("修改成功");
		}else{
			this.setMSG("修改失败");
		}
		str = this.getXX_ID();
		
		return str;
	}
	
	/**
	 * 提交方法
	 * @date:2017-09-30
	 * @author:yangda
	 * @throws SQLException
	 */
	public void submitRec() throws SQLException{
		String sql = "";
		sql = " update dbo.V_学分管理_学生加分申请信息表  set 审核状态='1', 相关编号 = '" 
				+ MyTools.fixSql(this.getXX_XGBH()) + "', 成绩 = '" 
				+ MyTools.fixSql(this.getXX_CJ()) + "', 加分 = '" 
				+ MyTools.fixSql(this.getXX_JF()) + "', 备注 = '" 
				+ MyTools.fixSql(this.getXX_BZ()) + "' where 编号 = '" 
				+ MyTools.fixSql(this.getXX_ID()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("提交成功");
		}else{
			this.setMSG("提交失败");
		}
	}
	
	/**
	 * 删除方法
	 * @date:2017-09-30
	 * @author:yangda
	 * @throws SQLException
	 */
	public void delRec() throws SQLException{
		String sql = "";
		sql = " delete dbo.V_学分管理_学生加分申请信息表 where 编号 = '" + MyTools.fixSql(this.getXX_ID()) + "' ";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	/**
	 * 读取学分详情列表
	 * @date:2017-09-30
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXfxq(int pageNum, int page) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String xnxqbm ="";
		
		sql = "select top 1 学年学期编码 from V_规则管理_学年学期表 where 状态='1' and 学期开始时间<=getDate() order by 学年学期编码 desc ";
		vec2 = db.GetContextVector(sql);
		if(vec2.size() > 0){
			xnxqbm = MyTools.StrFiltr(vec2.get(0));
		}	
//		sql = " select d.学年学期名称 as XNXQMC,d.行政班名称 as XZBMC,d.课程名称 as KCMC, substring(d.授课教师,0,charindex('@',d.授课教师) ) as SKJS,d.总评 as ZP from ( " +
//			  " select b.学年学期名称,c.行政班名称,c.课程名称,substring(c.登分教师姓名,2,LEN(c.登分教师姓名)) as 授课教师 , a.总评 from dbo.V_成绩管理_学生成绩信息表 a " +
//		      " left join dbo.V_成绩管理_科目课程信息表 b " +
//			  " on a.科目编号 = b.科目编号 " +
//			  " left join V_成绩管理_登分教师信息表 c " +
//			  " on c.科目编号 = a.科目编号 " +
//			  " where a.学号='" + MyTools.fixSql(this.getUSERCODE()) + "' and b.学年学期编码 ='" + MyTools.fixSql(xnxqbm) + "' and b.课程类型 ='3' " +
//			  " ) d ";
		
		sql = " select d.学年学期名称 as XNXQMC,d.行政班名称 as XZBMC,d.课程名称 as KCMC, d.授课教师 as SKJS,d.总评 as ZP from ( " +
			  " select b.学年学期名称,c.行政班名称,c.课程名称,REPLACE(c.登分教师姓名,'@','') as 授课教师 , a.总评 from dbo.V_成绩管理_学生成绩信息表 a " +
		      " left join dbo.V_成绩管理_科目课程信息表 b " +
			  " on a.科目编号 = b.科目编号 " +
			  " left join V_成绩管理_登分教师信息表 c " +
			  " on c.科目编号 = a.科目编号 " +
			  " where a.学号='" + MyTools.fixSql(this.getUSERCODE()) + "' and b.学年学期编码 ='" + MyTools.fixSql(xnxqbm) + "' and b.课程类型 ='3' " +
			  " ) d ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		
		
		return vec;
	}
	
	/**
	 * 读取学分详情列表
	 * @date:2017-09-30
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryXfxq(int pageNum, int page) throws SQLException{
		Vector vec = null;
		Vector vec2 = null;
		String sql = "";
		String xnxqbm ="";
		
//		sql = " select d.学年学期名称 as XNXQMC,d.行政班名称 as XZBMC,d.课程名称 as KCMC, substring(d.授课教师,0,charindex('@',d.授课教师) ) as SKJS,d.总评 as ZP from ( " +
//			  " select b.学年学期名称,c.行政班名称,c.课程名称,substring(c.登分教师姓名,2,LEN(c.登分教师姓名)) as 授课教师 , a.总评 from dbo.V_成绩管理_学生成绩信息表 a " +
//		      " left join dbo.V_成绩管理_科目课程信息表 b " +
//			  " on a.科目编号 = b.科目编号 " +
//			  " left join V_成绩管理_登分教师信息表 c " +
//			  " on c.科目编号 = a.科目编号 " +
//			  " where a.学号='" + MyTools.fixSql(this.getUSERCODE()) + "' and b.学年学期编码 ='" + MyTools.fixSql(this.getXX_XNXQBM()) + "' and b.课程类型 ='3' " +
//			  " ) d ";
		
		sql = " select d.学年学期名称 as XNXQMC,d.行政班名称 as XZBMC,d.课程名称 as KCMC, d.授课教师 as SKJS,case when ISNUMERIC(d.总评) = 1 then 总评 else 0 end as ZP from ( " +
			  " select b.学年学期名称,c.行政班名称,c.课程名称,REPLACE(c.登分教师姓名,'@','') as 授课教师 , a.总评 from dbo.V_成绩管理_学生成绩信息表 a " +
		      " left join dbo.V_成绩管理_科目课程信息表 b " +
			  " on a.科目编号 = b.科目编号 " +
			  " left join V_成绩管理_登分教师信息表 c " +
			  " on c.科目编号 = a.科目编号 " +
			  " where a.学号='" + MyTools.fixSql(this.getUSERCODE()) + "' and b.学年学期编码 ='" + MyTools.fixSql(this.getXX_XNXQBM()) + "' and b.课程类型 ='3' " +
			  " ) d " +
			  " union " +
			  " select b.学年学期名称,d.行政班名称,a.名称,replace(a.授课教师,'@',''),a.学分 from V_学分管理_教师学分设置信息表 a " +
			  " left join dbo.V_规则管理_学年学期表 b " +
			  " on a.学年学期编码 = b.学年学期编码 " +
			  " left join dbo.V_学生基本数据子类 c " +
			  " on c.学号 = '" + MyTools.fixSql(this.getUSERCODE()) + "' " +
			  " left join dbo.V_学校班级数据子类 d " +
			  " on c.行政班代码 = d.行政班代码 " +
			  " where a.学年学期编码 = '" + MyTools.fixSql(this.getXX_XNXQBM()) +  "' and a.学号 like '%@" + MyTools.fixSql(this.getUSERCODE()) + "@%' ";
		
		vec = db.getConttexJONSArr(sql, 0, 0);// 带分页返回数据(json格式）
		
		
		return vec;
	}
	
	/**
	 * 审核通过方法
	 * @date:2017-10-09
	 * @author:yangda
	 * @throws SQLException
	 */
	public void passRec() throws SQLException{
		String sql = "";
		Vector vec =null;
		Vector vec2 = null;
		Vector vec3 = null;
		String xgbh = "";
		String zp = "";
		String psbl = "50";
		String qzbl = "20";
		String sxbl = "";
		String qmbl = "30";
		DecimalFormat f = new DecimalFormat("#"); 
		sql = " update dbo.V_学分管理_学生加分申请信息表  set 审核状态='2', 审核信息 = '" 
				+ MyTools.fixSql(this.getXX_SHXX()) + "', 审核人 = '" 
				+ MyTools.fixSql(this.getUSERCODE()) + "', 审核时间 = getdate() where 编号 = '" 
				+ MyTools.fixSql(this.getXX_ID()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			
			sql = " select 相关编号 from dbo.V_学分管理_学生加分申请信息表 where 编号 = '" + MyTools.fixSql(this.getXX_ID()) + "' ";
			vec2 = db.GetContextVector(sql);
			if(vec2.size()>0){
				xgbh = MyTools.StrFiltr(vec2.get(0));
			}
			
			sql = " select 平时比例,期中比例,实训比例,期末比例 from V_成绩管理_登分设置信息表 where 相关编号 = '" + MyTools.fixSql(xgbh) + "' ";
			vec3 = db.GetContextVector(sql);
			if(vec3.size() > 0){
				psbl = MyTools.StrFiltr(vec3.get(0));
				qzbl = MyTools.StrFiltr(vec3.get(1));
				sxbl = MyTools.StrFiltr(vec3.get(2));
				qmbl = MyTools.StrFiltr(vec3.get(3));
			}
			
			//--------------------------------------------------------------------------------------
			
			//重新计算当前科目所有学生总评结果
			sql = "select 编号,平时,期中,实训,case when ISNUMERIC(期末) = 1 then 期末 else 0 end,总评 from V_成绩管理_学生成绩信息表 " +
				"where 相关编号='" + MyTools.fixSql(xgbh) + "' and 成绩状态='1' and 学号 = '" + MyTools.fixSql(this.getXX_XH()) + "'";
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
					int zf = Integer.parseInt(MyTools.StrFiltr(vec.get(i+4))) + Integer.parseInt(MyTools.StrFiltr(this.getXX_JF()));
//					qmScore = MyTools.StrFiltr(vec.get(i+4));
					qmScore = zf+"";
					zpScore = MyTools.StrFiltr(vec.get(i+5));
					
					//转换特殊成绩
					if("-2".equalsIgnoreCase(psScore) || "-3".equalsIgnoreCase(psScore) || "-4".equalsIgnoreCase(psScore)) psScore = "0";
					if("-2".equalsIgnoreCase(qzScore) || "-3".equalsIgnoreCase(qzScore) || "-4".equalsIgnoreCase(qzScore)) qzScore = "0";
					if("-2".equalsIgnoreCase(sxScore) || "-3".equalsIgnoreCase(sxScore) || "-4".equalsIgnoreCase(sxScore)) sxScore = "0";
					if("-2".equalsIgnoreCase(qmScore) || "-3".equalsIgnoreCase(qmScore) || "-4".equalsIgnoreCase(qmScore)) psScore = "0";
					
					//判断是否需要计算总评比例，如有任何文字成绩代码不计算
					if((!"".equalsIgnoreCase(psbl) && ("".equalsIgnoreCase(psScore) || MyTools.StringToInt(psScore)<0)) ||
						(!"".equalsIgnoreCase(qzbl) && ("".equalsIgnoreCase(qzScore) || MyTools.StringToInt(qzScore)<0)) ||
						(!"".equalsIgnoreCase(sxbl) && ("".equalsIgnoreCase(sxScore) || MyTools.StringToInt(sxScore)<0)) ||
						(!"".equalsIgnoreCase(qmbl) && ("".equalsIgnoreCase(qmScore) || MyTools.StringToInt(qmScore)<0))){
						zp="";
					}else{
						if(!"".equalsIgnoreCase(psbl)){
							tempZp += MyTools.StringToFloat(psScore)*MyTools.StringToFloat(psbl)/100;
						}
						if(!"".equalsIgnoreCase(qzbl)){
							tempZp += MyTools.StringToFloat(qzScore)*MyTools.StringToFloat(qzbl)/100;
						}
						if(!"".equalsIgnoreCase(sxbl)){
							tempZp += MyTools.StringToFloat(sxScore)*MyTools.StringToFloat(sxbl)/100;
						}
						if(!"".equalsIgnoreCase(qmbl)){
							tempZp += MyTools.StringToFloat(qmScore)*MyTools.StringToFloat(qmbl)/100;
						}
						
						//判断总评是否为整数
//						resultZp = String.valueOf(tempZp);
//						resultZp = resultZp.substring(0, resultZp.indexOf(".")) + resultZp.substring(resultZp.indexOf("."), resultZp.indexOf(".")+2);
//						if(".0".equalsIgnoreCase(resultZp.substring(resultZp.length()-2))){
//							resultZp = resultZp.substring(0, resultZp.length()-2);
//						}
						resultZp = f.format(tempZp);
						zp=(String.valueOf(resultZp));
					}
					sql = "update V_成绩管理_学生成绩信息表 set 总评='" + MyTools.fixSql(zp) + "', 期末= '" + MyTools.fixSql(qmScore) + "' " +
						"where 编号='" + MyTools.fixSql(code) + "'";
					if(db.executeInsertOrUpdate(sql)){
						this.setMSG("审核成功");
					}else{
						this.setMSG("审核失败");
					}
				}
			}
			
		}else{
			this.setMSG("审核失败");
		}
	}
	
	/**
	 * 审核驳回方法
	 * @date:2017-10-09
	 * @author:yangda
	 * @throws SQLException
	 */
	public void backRec() throws SQLException{
		String sql = "";
		sql = " update dbo.V_学分管理_学生加分申请信息表  set 审核状态='3', 审核信息 = '" 
				+ MyTools.fixSql(this.getXX_SHXX()) + "', 审核人 = '" 
				+ MyTools.fixSql(this.getUSERCODE()) + "', 审核时间 = getdate() where 编号 = '" 
				+ MyTools.fixSql(this.getXX_ID()) + "'";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("审核成功");
		}else{
			this.setMSG("审核失败");
		}
	}
	
	
	/**
	 * 批量审核通过方法
	 * @date:2017-10-23
	 * @author:yangda
	 * @throws SQLException
	 */
	public void plpassRec() throws SQLException{
		String sql = "";
		Vector vec =null;
		Vector vec2 = null;
		Vector vec3 = null;
		String xgbh = "";
		String zp = "";
		DecimalFormat f = new DecimalFormat("#"); 
		String bh = this.getXX_ID();
		String [] bhArray = bh.split(",");
		String jf = this.getXX_JF();
		String [] jfArray = jf.split(",");
		String xh = this.getXX_XH();
		String [] xhArray = xh.split(",");
		Vector sqlVec = new Vector();
		Vector sqlVec2 = new Vector();
		
		for (int i = 0; i < bhArray.length; i++) {
			sql = " update dbo.V_学分管理_学生加分申请信息表  set 审核状态='2', 审核信息 = '" 
					+ MyTools.fixSql(this.getXX_SHXX()) + "', 审核人 = '" 
					+ MyTools.fixSql(this.getUSERCODE()) + "', 审核时间 = getdate() where 编号 = '" 
					+ MyTools.fixSql(bhArray[i]) + "'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			for (int k = 0; k < bhArray.length; k++) {
				sql = " select 相关编号 from dbo.V_学分管理_学生加分申请信息表 where 编号 = '" + MyTools.fixSql(bhArray[k]) + "' ";
				vec2 = db.GetContextVector(sql);
				if(vec2.size()>0){
					xgbh = MyTools.StrFiltr(vec2.get(0));
				}
				
				sql = " select 平时比例,期中比例,实训比例,期末比例 from V_成绩管理_登分设置信息表 where 相关编号 = '" + MyTools.fixSql(xgbh) + "' ";
				vec3 = db.GetContextVector(sql);
				
				String psbl = MyTools.StrFiltr(vec3.get(0));
				String qzbl = MyTools.StrFiltr(vec3.get(1));
				String sxbl = MyTools.StrFiltr(vec3.get(2));
				String qmbl = MyTools.StrFiltr(vec3.get(3));
				
				//--------------------------------------------------------------------------------------
				
				//重新计算当前科目所有学生总评结果
				sql = "select 编号,平时,期中,实训,case when ISNUMERIC(期末) = 1 then 期末 else 0 end,总评 from V_成绩管理_学生成绩信息表 " +
					"where 相关编号='" + MyTools.fixSql(xgbh) + "' and 成绩状态='1' and 学号 = '" + MyTools.fixSql(xhArray[k]) + "'";
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
						int zf = Integer.parseInt(MyTools.StrFiltr(vec.get(i+4))) + Integer.parseInt(MyTools.StrFiltr(jfArray[k]));
	//					qmScore = MyTools.StrFiltr(vec.get(i+4));
						qmScore = zf+"";
						zpScore = MyTools.StrFiltr(vec.get(i+5));
						
						//转换特殊成绩
						if("-2".equalsIgnoreCase(psScore) || "-3".equalsIgnoreCase(psScore) || "-4".equalsIgnoreCase(psScore)) psScore = "0";
						if("-2".equalsIgnoreCase(qzScore) || "-3".equalsIgnoreCase(qzScore) || "-4".equalsIgnoreCase(qzScore)) qzScore = "0";
						if("-2".equalsIgnoreCase(sxScore) || "-3".equalsIgnoreCase(sxScore) || "-4".equalsIgnoreCase(sxScore)) sxScore = "0";
						if("-2".equalsIgnoreCase(qmScore) || "-3".equalsIgnoreCase(qmScore) || "-4".equalsIgnoreCase(qmScore)) psScore = "0";
						
						//判断是否需要计算总评比例，如有任何文字成绩代码不计算
						if((!"".equalsIgnoreCase(psbl) && ("".equalsIgnoreCase(psScore) || MyTools.StringToInt(psScore)<0)) ||
							(!"".equalsIgnoreCase(qzbl) && ("".equalsIgnoreCase(qzScore) || MyTools.StringToInt(qzScore)<0)) ||
							(!"".equalsIgnoreCase(sxbl) && ("".equalsIgnoreCase(sxScore) || MyTools.StringToInt(sxScore)<0)) ||
							(!"".equalsIgnoreCase(qmbl) && ("".equalsIgnoreCase(qmScore) || MyTools.StringToInt(qmScore)<0))){
							zp="";
						}else{
							if(!"".equalsIgnoreCase(psbl)){
								tempZp += MyTools.StringToFloat(psScore)*MyTools.StringToFloat(psbl)/100;
							}
							if(!"".equalsIgnoreCase(qzbl)){
								tempZp += MyTools.StringToFloat(qzScore)*MyTools.StringToFloat(qzbl)/100;
							}
							if(!"".equalsIgnoreCase(sxbl)){
								tempZp += MyTools.StringToFloat(sxScore)*MyTools.StringToFloat(sxbl)/100;
							}
							if(!"".equalsIgnoreCase(qmbl)){
								tempZp += MyTools.StringToFloat(qmScore)*MyTools.StringToFloat(qmbl)/100;
							}
							
							//判断总评是否为整数
	//						resultZp = String.valueOf(tempZp);
	//						resultZp = resultZp.substring(0, resultZp.indexOf(".")) + resultZp.substring(resultZp.indexOf("."), resultZp.indexOf(".")+2);
	//						if(".0".equalsIgnoreCase(resultZp.substring(resultZp.length()-2))){
	//							resultZp = resultZp.substring(0, resultZp.length()-2);
	//						}
							resultZp = f.format(tempZp);
							zp=(String.valueOf(resultZp));
						}
						sql = "update V_成绩管理_学生成绩信息表 set 总评='" + MyTools.fixSql(zp) + "', 期末= '" + MyTools.fixSql(qmScore) + "' " +
							"where 编号='" + MyTools.fixSql(code) + "'";
						sqlVec2.add(sql);
						
					}
				}
				
			}
			
			if(db.executeInsertOrUpdateTransaction(sqlVec2)){
				this.setMSG("审核成功");
			}else{
				this.setMSG("审核失败");
			}
			
		}else{
			this.setMSG("审核失败");
		}
	}
	
	/**
	 * 批量审核驳回方法
	 * @date:2017-10-23
	 * @author:yangda
	 * @throws SQLException
	 */
	public void plbackRec() throws SQLException{
		String sql = "";
		String bh = this.getXX_ID();
		String [] bhArray = bh.split(",");
		Vector sqlVec = new Vector();
		
		for (int i = 0; i < bhArray.length; i++) {
			sql = " update dbo.V_学分管理_学生加分申请信息表  set 审核状态='3', 审核信息 = '" 
					+ MyTools.fixSql(this.getXX_SHXX()) + "', 审核人 = '" 
					+ MyTools.fixSql(this.getUSERCODE()) + "', 审核时间 = getdate() where 编号 = '" 
					+ MyTools.fixSql(bhArray[i]) + "'";
			sqlVec.add(sql);
		}
		
		if(db.executeInsertOrUpdateTransaction(sqlVec)){
			this.setMSG("审核成功");
		}else{
			this.setMSG("审核失败");
		}
	}
	
	//get&set方法
	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getXX_ID() {
		return XX_ID;
	}

	public void setXX_ID(String xX_ID) {
		XX_ID = xX_ID;
	}

	public String getXX_XNXQBM() {
		return XX_XNXQBM;
	}

	public void setXX_XNXQBM(String xX_XNXQBM) {
		XX_XNXQBM = xX_XNXQBM;
	}

	public String getXX_XH() {
		return XX_XH;
	}

	public void setXX_XH(String xX_XH) {
		XX_XH = xX_XH;
	}

	public String getXX_XM() {
		return XX_XM;
	}

	public void setXX_XM(String xX_XM) {
		XX_XM = xX_XM;
	}

	public String getXX_BJBH() {
		return XX_BJBH;
	}

	public void setXX_BJBH(String xX_BJBH) {
		XX_BJBH = xX_BJBH;
	}

	public String getXX_BJMC() {
		return XX_BJMC;
	}

	public void setXX_BJMC(String xX_BJMC) {
		XX_BJMC = xX_BJMC;
	}

	public String getXX_XGBH() {
		return XX_XGBH;
	}

	public void setXX_XGBH(String xX_XGBH) {
		XX_XGBH = xX_XGBH;
	}

	public String getXX_CJ() {
		return XX_CJ;
	}

	public void setXX_CJ(String xX_CJ) {
		XX_CJ = xX_CJ;
	}

	public String getXX_JF() {
		return XX_JF;
	}

	public void setXX_JF(String xX_JF) {
		XX_JF = xX_JF;
	}

	public String getXX_BZ() {
		return XX_BZ;
	}

	public void setXX_BZ(String xX_BZ) {
		XX_BZ = xX_BZ;
	}

	public String getXX_SHZT() {
		return XX_SHZT;
	}

	public void setXX_SHZT(String xX_SHZT) {
		XX_SHZT = xX_SHZT;
	}

	public String getXX_SHXX() {
		return XX_SHXX;
	}

	public void setXX_SHXX(String xX_SHXX) {
		XX_SHXX = xX_SHXX;
	}

	public String getXX_SHR() {
		return XX_SHR;
	}

	public void setXX_SHR(String xX_SHR) {
		XX_SHR = xX_SHR;
	}

	public String getXX_SHSJ() {
		return XX_SHSJ;
	}

	public void setXX_SHSJ(String xX_SHSJ) {
		XX_SHSJ = xX_SHSJ;
	}

	public String getXX_CJR() {
		return XX_CJR;
	}

	public void setXX_CJR(String xX_CJR) {
		XX_CJR = xX_CJR;
	}

	public String getXX_CJSJ() {
		return XX_CJSJ;
	}

	public void setXX_CJSJ(String xX_CJSJ) {
		XX_CJSJ = xX_CJSJ;
	}

	public String getXX_ZT() {
		return XX_ZT;
	}

	public void setXX_ZT(String xX_ZT) {
		XX_ZT = xX_ZT;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}
	
	
}
