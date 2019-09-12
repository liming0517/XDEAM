package com.pantech.devolop.xfyh;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.tools.MyTools;

public class XfszBean {
	
	private String USERCODE;//用户编号
	private String XX_ID;//编号
	private String XX_XNXQBM;//学年学期编码
	private String XX_XH;// 学号
	private String XX_XM;// 姓名 
	private String XX_MC;// 名称
	private String XX_SKJS; // 授课教师
	private String XX_SKJSGH; // 授课教师
	private String XX_LX; // 类型
	private String XX_XF;// 学分
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
		XX_MC = "";// 名称
		XX_SKJSGH = "";// 授课教师工号
		XX_SKJS = ""; // 授课教师
		XX_LX = ""; // 类型
		XX_XF = "";// 学分
		XX_CJR = "";// 创建人
		XX_CJSJ = "";// 创建时间
		XX_ZT = "";// 状态
	}
	
	/**
	 * 类初始化，数据库操作必有 此时无主关键字
	 */
	public XfszBean(HttpServletRequest request) {
		this.request = request;
		this.db = new DBSource(request);
		this.MSG = "";
		this.initialData();
	}

	
	
	
	/**
	 * 读取学年学期下拉框
	 * @date:2017-10-20
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadXNXQCombo() throws SQLException{
		Vector vec = null;
		String sql = " select comboValue,comboName from (select '' as comboValue,'请选择' as comboName,'1' as px union select distinct 学年学期编码  AS comboValue,学年学期名称 AS comboName,'2' as px  FROM V_规则管理_学年学期表 where 1=1) e order by e.px,e.comboValue desc";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	/**
	 * 读取类型下拉框
	 * @date:2017-10-20
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector loadLXCombo() throws SQLException{
		Vector vec = null;
		String sql = " select comboValue,comboName from ( " +
					 " select '' as comboValue,'请选择' as comboName,'1' as px  " +
					 " union  " +
					 " select 类别代码 AS comboValue,类别名称 AS comboName,'2' as px from dbo.V_信息类别_类别操作 where 父类别代码 = 'XFLX' " +
					 " ) e order by e.px,e.comboValue desc ";
		vec = db.getConttexJONSArr(sql, 0, 0);
		return vec;
	}
	
	
	/**
	 * 读取教师学分设置列表
	 * @date:2017-10-20
	 * @author:yangda
	 * @return Vector 结果集
	 * @throws SQLException
	 */
	public Vector queryRec(int pageNum, int page) throws SQLException{
		Vector vec = null;
		String sql = "";
		
		sql = " select a.学年学期编码 as XNXQBM,c.学年学期名称 as XNXQMC,replace(a.学号,'@','') as XH,replace(a.姓名,'@','') as XM,a.名称 as MC,b.类别名称 as LXMC,a.类型 as LXDM,replace(a.授课教师,'@','') as SKJS,replace(a.授课教师工号,'@','') as SKJSGH,a.学分 as XF,a.编号 as ID from V_学分管理_教师学分设置信息表 a " +
			  " left join V_信息类别_类别操作 b " +
			  " on a.类型 = b.类别代码 " + 
			  " left join V_规则管理_学年学期表 c " + 
			  " on a.学年学期编码 = c.学年学期编码 " + 
			  " where 1 = 1 ";
		
		
		if(!"".equalsIgnoreCase(this.getXX_XNXQBM())){
			sql += " AND a.学年学期编码 ='" + MyTools.fixSql(this.getXX_XNXQBM()) + "'";
		}
		if(!"".equalsIgnoreCase(this.getXX_XH())){
			sql += " AND a.学号 like '@%" + MyTools.fixSql(this.getXX_XH()) + "%@'";
		}
		if(!"".equalsIgnoreCase(this.getXX_XM())){
			sql += " AND a.姓名 like '@%" + MyTools.fixSql(this.getXX_XM()) + "%@'";
		}
		if(!"".equalsIgnoreCase(this.getXX_MC())){
			sql += " AND a.名称 like '%" + MyTools.fixSql(this.getXX_MC()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXX_SKJS())){
			sql += " AND a.授课教师 like '@%" + MyTools.fixSql(this.getXX_SKJS()) + "%@'";
		}
		if(!"".equalsIgnoreCase(this.getXX_LX())){
			sql += " AND a.类型 ='" + MyTools.fixSql(this.getXX_LX()) + "'";
		}
		
		sql += " order by a.创建时间 desc ";
		
		vec = db.getConttexJONSArr(sql, pageNum, page);// 带分页返回数据(json格式）
		
		return vec;
	}
	
	/**
	 * 查询学生列表
	 * @date:2017-10-20
	 * @author:yangda
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadStuList(int pageNum, int pageSize, String xzxsxh) throws SQLException{
		Vector vec = null;
		String stuid="";
		if(xzxsxh.equals("")){//没有教师数据
			stuid="''";
		}else{//选中的教师排在前面显示
			String[] teachers=xzxsxh.split(",");
			for(int i=0;i<teachers.length;i++){
				stuid+="'"+teachers[i]+"',";
			}
			stuid=stuid.substring(0, stuid.length()-1);
		}
		
		String sql = " select 学号  as XH,姓名 as XM,'1' as px from dbo.V_学生基本数据子类 where 学号 in ("+stuid+") " +
					 " union " +
					 " select 学号  as XH,姓名 as XM,'2' as px from dbo.V_学生基本数据子类 where 学号 not in ("+stuid+") ";
			
		if(!"".equalsIgnoreCase(this.getXX_XH())){
			sql += " AND 学号 like'%" + MyTools.fixSql(this.getXX_XH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXX_XM())){
			sql += " AND 姓名 like'%" + MyTools.fixSql(this.getXX_XM()) + "%'";
		}
		sql += " order by px,学号 ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 查询教师列表
	 * @date:2017-10-20
	 * @author:yangda
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector loadTeaList(int pageNum, int pageSize, String xzskjsgh) throws SQLException{
		Vector vec = null;
		String teaid="";
		if(xzskjsgh.equals("")){//没有教师数据
			teaid="''";
		}else{//选中的教师排在前面显示
			String[] teachers=xzskjsgh.split(",");
			for(int i=0;i<teachers.length;i++){
				teaid+="'"+teachers[i]+"',";
			}
			teaid=teaid.substring(0, teaid.length()-1);
		}
		
		String sql = " select 工号 as GH,姓名 as XM,'1' as px from dbo.V_教职工基本数据子类 where 工号 in ("+teaid+") " +
					 " union " +
					 " select 工号 as GH,姓名 as XM,'2' as px from dbo.V_教职工基本数据子类 where 工号 not in ("+teaid+") ";
		
		if(!"".equalsIgnoreCase(this.getXX_SKJSGH())){
			sql += " AND 工号 like'%" + MyTools.fixSql(this.getXX_SKJSGH()) + "%'";
		}
		if(!"".equalsIgnoreCase(this.getXX_SKJS())){
			sql += " AND 姓名 like'%" + MyTools.fixSql(this.getXX_SKJS()) + "%'";
		}
		sql += " order by px,工号 ";
		vec = db.getConttexJONSArr(sql, pageNum, pageSize);
		return vec;
	}
	
	/**
	 * 保存学分设置
	 * @date:2017-10-20
	 * @author:yangda
	 * @return Vector
	 * @throws SQLException
	 */
	public void saveRec() throws SQLException{
		Vector vec = null;
		String sql = "";
		String stuCode = this.getXX_XH();//学生学号
		String stuName = this.getXX_XM();//学生姓名
		String teaCode = this.getXX_SKJSGH();//教师工号
		String teaName = this.getXX_SKJS();//教师姓名
		
		String stu_xm="";
		String stu_xh="";
		String tea_xm="";
		String tea_gh="";
		
		String[] str_1 = stuCode.split(",");
		String[] str_2 = stuName.split(",");
		
		String[] str_3 = teaCode.split(",");
		String[] str_4 = teaName.split(",");
		
		for (int i = 0; i < str_1.length; i++) {
			stu_xh += "@"+str_1[i]+"@,";
			stu_xm += "@"+str_2[i]+"@,";
			
		}
		for (int i = 0; i < str_3.length; i++) {
			tea_gh += "@"+str_3[i]+"@,";
			tea_xm += "@"+str_4[i]+"@,";
			
		}
		
		stu_xh = stu_xh.substring(0, stu_xh.length()-1);
		stu_xm = stu_xm.substring(0, stu_xm.length()-1);
		tea_gh = tea_gh.substring(0, tea_gh.length()-1);
		tea_xm = tea_xm.substring(0, tea_xm.length()-1);
		
		
		sql = " insert into V_学分管理_教师学分设置信息表 (学年学期编码,学号,姓名,名称,授课教师工号,授课教师,类型,学分,创建人,创建时间,状态) values ( " +
			  " '" + MyTools.fixSql(this.getXX_XNXQBM()) + "', " + 
			  " '" + MyTools.fixSql(stu_xh) + "', " + 
			  " '" + MyTools.fixSql(stu_xm) + "', " + 
			  " '" + MyTools.fixSql(this.getXX_MC()) + "', " + 
			  " '" + MyTools.fixSql(tea_gh) + "', " + 
			  " '" + MyTools.fixSql(tea_xm) + "', " + 
			  " '" + MyTools.fixSql(this.getXX_LX()) + "', " + 
			  " '" + MyTools.fixSql(this.getXX_XF()) + "', " + 
			  " '" + MyTools.fixSql(this.getUSERCODE()) + "', getdate(),'1' " +
			  "  )";
		
		if(db.executeInsertOrUpdate(sql)){
			this.MSG = "保存成功";
		}else{
			this.MSG = "保存失败";
		}
		
	}
	
	/**
	 * 查询可用学分
	 * @date:2017-10-23
	 * @author:
	 * @return String 学年学期
	 * @throws SQLException
	 */
	public String loadKyxf() throws SQLException {
		String curXf = "";
		Vector vec = null;
		Vector vec2 = null;
		Vector vec3 = null;
		Vector vec4 = null;
		Vector vec5 = null;
		Vector vec6 = new Vector();
		int tempKyfs = 0;
		String sql = "";
		String stu_xh = "";
		String stuCode = this.getXX_XH();//学生学号
		String[] str_1 = stuCode.split(",");
//		for (int i = 0; i < str_1.length; i++) {
//			stu_xh += "@"+str_1[i]+"@,";
//		}
//		stu_xh = stu_xh.substring(0, stu_xh.length()-1);
		
		for (int i = 0; i < str_1.length; i++) {
			stu_xh = "@"+str_1[i]+"@";
			//获奖获得的额外学分
			sql = " select isnull(SUM(cast(学分 as int)),0) as 学分 from V_学分管理_教师学分设置信息表 where 学年学期编码 = '" + MyTools.fixSql(this.getXX_XNXQBM()) + "' and 学号 like '%" + MyTools.fixSql(stu_xh) + "%' ";
			vec5 = db.GetContextVector(sql);
			
			//总分
			sql = " select a.姓名,a.学号,c.行政班名称,c.行政班代码,isnull(SUM(cast(a.总评 as int)),0) as 总分 from dbo.V_成绩管理_学生成绩信息表 a " +
				  " left join dbo.V_成绩管理_科目课程信息表 b " +
				  " on a.科目编号 = b.科目编号 " +
				  " left join dbo.V_成绩管理_登分教师信息表 c " +
				  " on c.科目编号 = a.科目编号 " +
				  " where a.学号 = '" + MyTools.fixSql(str_1[i]) + "' and c.来源类型 ='3' and b.学年学期编码 = '" + MyTools.fixSql(this.getXX_XNXQBM()) + "' " +
				  " group by a.姓名,a.学号,c.行政班名称,c.行政班代码  ";
			vec = db.GetContextVector(sql);
			
			//已用分数
			sql = " select isnull(SUM(CAST( 加分 as int)),0) as 已用分数 from dbo.V_学分管理_学生加分申请信息表 " +
				  " where 学年学期编码 = '" + MyTools.fixSql(this.getXX_XNXQBM()) + "' and 学号 = '" + MyTools.fixSql(str_1[i]) + "' and 审核状态 = '2' ";
			
			vec3 = db.GetContextVector(sql);
			
			//占用分数
			sql = " select isnull(SUM(CAST( 加分 as int)),0) as 占用分数 from dbo.V_学分管理_学生加分申请信息表 " +
				  " where 学年学期编码 = '" + MyTools.fixSql(this.getXX_XNXQBM()) + "' and 学号 = '" + MyTools.fixSql(str_1[i]) + "' and 审核状态 = '1' ";
			
			vec4 = db.GetContextVector(sql);
			
			if(vec.size() > 0){
				
				String xm = MyTools.StrFiltr(vec.get(0));
				String xh = MyTools.StrFiltr(vec.get(1));
				String xzbmc = MyTools.StrFiltr(vec.get(2));
				String xzbdm = MyTools.StrFiltr(vec.get(3));
				int zf_1 = Integer.parseInt(MyTools.StrFiltr(vec.get(4)));
				int yyfs_1 = Integer.parseInt(MyTools.StrFiltr(vec3.get(0)));
				int zyfs_1 = Integer.parseInt(MyTools.StrFiltr(vec4.get(0)));
				int ewfx_1 = Integer.parseInt(MyTools.StrFiltr(vec5.get(0)));
				
				zf_1 = zf_1 + Integer.parseInt(MyTools.StrFiltr(vec5.get(0)));
	
				int kyfs_1 = zf_1-yyfs_1-zyfs_1;
				
				if(i == 0){
					tempKyfs = kyfs_1;
				}else{
					if(tempKyfs > kyfs_1){
						tempKyfs = kyfs_1;
					}
				}
				
			}else{
				int zf_1 = Integer.parseInt(MyTools.StrFiltr(vec5.get(0)));
				int yyfs_1 = Integer.parseInt(MyTools.StrFiltr(vec3.get(0)));
				int zyfs_1 = Integer.parseInt(MyTools.StrFiltr(vec4.get(0)));
				
				int kyfs_1 = zf_1-yyfs_1-zyfs_1;
				
				if(i == 0){
					tempKyfs = kyfs_1;
				}else{
					if(tempKyfs > kyfs_1){
						tempKyfs = kyfs_1;
					}
				}
				
			}
			
		}
		
		curXf = tempKyfs+"";
		
		return curXf;
	}
	
	
	/**
	 * 修改学分设置
	 * @date:2017-10-23
	 * @author:yangda
	 * @return Vector
	 * @throws SQLException
	 */
	public void editRec() throws SQLException{
		Vector vec = null;
		String sql = "";
		String stuCode = this.getXX_XH();//学生学号
		String stuName = this.getXX_XM();//学生姓名
		String teaCode = this.getXX_SKJSGH();//教师工号
		String teaName = this.getXX_SKJS();//教师姓名
		
		String stu_xm="";
		String stu_xh="";
		String tea_xm="";
		String tea_gh="";
		
		String[] str_1 = stuCode.split(",");
		String[] str_2 = stuName.split(",");
		
		String[] str_3 = teaCode.split(",");
		String[] str_4 = teaName.split(",");
		
		for (int i = 0; i < str_1.length; i++) {
			stu_xh += "@"+str_1[i]+"@,";
			stu_xm += "@"+str_2[i]+"@,";
			
		}
		for (int i = 0; i < str_3.length; i++) {
			tea_gh += "@"+str_3[i]+"@,";
			tea_xm += "@"+str_4[i]+"@,";
			
		}
		
		stu_xh = stu_xh.substring(0, stu_xh.length()-1);
		stu_xm = stu_xm.substring(0, stu_xm.length()-1);
		tea_gh = tea_gh.substring(0, tea_gh.length()-1);
		tea_xm = tea_xm.substring(0, tea_xm.length()-1);
		
		
		sql = " update V_学分管理_教师学分设置信息表  set 学年学期编码 = '"
				+ MyTools.fixSql(this.getXX_XNXQBM()) + "', 学号 = '" 
				+ MyTools.fixSql(stu_xh) + "', 姓名 = '" 
				+ MyTools.fixSql(stu_xm) + "', 授课教师工号 = '" 
				+ MyTools.fixSql(tea_gh) + "', 授课教师 = '" 
				+ MyTools.fixSql(tea_xm) + "', 类型 = '" 
				+ MyTools.fixSql(this.getXX_LX()) + "', 学分 = '" 
				+ MyTools.fixSql(this.getXX_XF()) + "', 名称 = '"
				+ MyTools.fixSql(this.getXX_MC()) + "' where 编号 = '" 
				+ MyTools.fixSql(this.getXX_ID()) + "'";
		
		
		if(db.executeInsertOrUpdate(sql)){
			this.MSG = "修改成功";
		}else{
			this.MSG = "修改失败";
		}
		
	}
	
	/**
	 * 删除方法
	 * @date:2017-10-23
	 * @author:yangda
	 * @throws SQLException
	 */
	public void delRec() throws SQLException{
		String sql = "";
		sql = " delete V_学分管理_教师学分设置信息表 where 编号 = '" + MyTools.fixSql(this.getXX_ID()) + "' ";
		if(db.executeInsertOrUpdate(sql)){
			this.setMSG("删除成功");
		}else{
			this.setMSG("删除失败");
		}
	}
	
	//get&set 方法
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

	public String getXX_MC() {
		return XX_MC;
	}

	public void setXX_MC(String xX_MC) {
		XX_MC = xX_MC;
	}

	public String getXX_SKJS() {
		return XX_SKJS;
	}

	public void setXX_SKJS(String xX_SKJS) {
		XX_SKJS = xX_SKJS;
	}

	public String getXX_LX() {
		return XX_LX;
	}

	public void setXX_LX(String xX_LX) {
		XX_LX = xX_LX;
	}

	public String getXX_XF() {
		return XX_XF;
	}

	public void setXX_XF(String xX_XF) {
		XX_XF = xX_XF;
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

	public String getXX_SKJSGH() {
		return XX_SKJSGH;
	}

	public void setXX_SKJSGH(String xX_SKJSGH) {
		XX_SKJSGH = xX_SKJSGH;
	}
	
	
}
