package com.pantech.devolop.teachManage;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.json.types.JsonObject;

import com.pantech.base.common.db.DBSource;
import com.pantech.base.common.exception.WrongSQLException;
import com.pantech.base.common.tools.JsonUtil;
import com.pantech.base.common.tools.MyTools;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * createTime:2017-02-09
 * createUser:马晓良
 * description:教学标准信息
 * 
 *
 */
public class TeachManageBean {
	private String USERCODE;//用户名
	private String resultjson;//结果集
	private String JXBZ_JXBZBM;//教学标准编码
	private String JXBZ_JXBZMC;//教学标准名称
	private String JXBZ_ZYBM;//专业编码
	private String JXBZ_ZYMC;//专业名称
	
	private String CXJXBZ_JXBZMC;//教学标准名称(查询)
	private String CXJXBZ_ZYBM;//专业编码(查询)
	
	private String Check_JXBZBM;//教学标准编码
	private String Check_JXBZMC;//教学标准名称
	private String Check_ZYMC;//专业名称
	private String Check_path;//文件路径
	
	private String state;//状态
	private String sql;
	Vector vec=null;
	JSONArray jar;
	
	private String msg; // 消息
	private HttpServletRequest request;
	private DBSource db;// 数据库
	Vector vector=null;
	
	
	/**
	 * 构造函数
	 */
	public TeachManageBean(HttpServletRequest request){
		this.request = request;
		db = new DBSource(request);
		this.initialData();
	}
	
	/**
	 * 初始化参数
	 */
	private void initialData() {
		this.USERCODE = "";
		this.resultjson="";
		this.JXBZ_JXBZBM="";
		this.JXBZ_JXBZMC="";
		this.JXBZ_ZYBM="";
		this.JXBZ_ZYMC="";
		this.CXJXBZ_JXBZMC="";
		this.CXJXBZ_ZYBM="";
		this.Check_JXBZBM="";
		this.Check_JXBZMC="";
		this.Check_ZYMC="";
		this.Check_path="";
		this.state="";
		this.sql="";
		this.jar=null;
	}
	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:查询所有数据并当有查询参数传递进来时同过查询参数来查出相应数据
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public Vector queryListRec(int pageNum,int pageSize)throws SQLException,WrongSQLException{
		sql="select a.编号,a.教学标准名称,b.专业名称,convert(char(11),a.创建时间,21) as 创建时间,a.文件路径,a.文件名称,a.预览文件路径," +
				"case a.审核状态  when '0' then '未审核' when '1' then '已审核' when '1' then '无效' end as 状态," +
				"a.创建人 from V_教学管理_教学标准信息表  as a left outer join V_专业基本信息数据子类 as b on a.专业代码=b.专业代码   where 1=1";
		//查询条件
		if(!"".equalsIgnoreCase(CXJXBZ_JXBZMC)){
			sql+=" and a.教学标准名称 like '" +  MyTools.fixSql(MyTools.StrFiltr(this.getCXJXBZ_JXBZMC())) + "'";
			}
		if(!"".equalsIgnoreCase(CXJXBZ_ZYBM)){
			sql+=" and b.专业代码 like '" +  MyTools.fixSql(MyTools.StrFiltr(this.getCXJXBZ_ZYBM())) + "'";
		}
		vec = db.getConttexJONSArr(sql,pageNum,pageSize);
		return vec;
	}
	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:判断是添加还是更新
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void save(String path,String fileName) throws SQLException, WrongSQLException{
		/*db = new DBSource(request);
		if(db.getResultFromDB("select count(*) from V_资产管理  where 资产编号='"+MyTools.fixSql(this.assetsNo)+"'")==false){
			this.addRec();
		}else{
			this.modRec();
		}*/
		sql = "select count(*) from V_教学管理_教学标准信息表  where 编号='"+MyTools.fixSql(MyTools.StrFiltr((getJXBZ_JXBZBM())))+"'";
		if(db.getResultFromDB(sql)){//此数据在数据库中已存在
			this.modRec(path,fileName);//修改方法
		}else{
			this.addRec(path,fileName);//新增方法
		}
	}
	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:新增数据主键自增
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void addRec(String path,String fileName) throws SQLException, WrongSQLException{
		String jxbzid = db.getMaxID("JXGL_JXBZXX","JJ_BH","JXBZ", 4);
		String swfPath="";
		if(path.length()>0){
			swfPath=path.substring(0,path.lastIndexOf("."))+".swf";
		}
		this.setJXBZ_JXBZBM(jxbzid);
		sql="select count(*) from V_教学管理_教学标准信息表 where 专业代码='"+MyTools.fixSql(getJXBZ_ZYMC())+"'";
		if(db.getResultFromDB(sql)){//此数据在数据库中已存在
			this.msg="保存失败，由于您所选择的专业已存在,请重新选择专业！";//如果专业代码已存在，提醒用户改专业已存在。
		}else{
			sql="INSERT INTO V_教学管理_教学标准信息表 " +
				"([编号],[教学标准名称],[专业代码],[文件路径],[文件名称],[预览文件路径],[创建人]) VALUES ('"+MyTools.fixSql(getJXBZ_JXBZBM())+"'" +
				",'"+MyTools.fixSql(getJXBZ_JXBZMC())+"','"+MyTools.fixSql(getJXBZ_ZYMC())+"','"+MyTools.fixSql(path)+"','"+MyTools.fixSql(fileName)+"','"+MyTools.fixSql(swfPath)+"','"+MyTools.fixSql(getUSERCODE())+"')"; 
			if(db.executeInsertOrUpdate(sql)){
				this.msg="保存成功";
			}else{
				this.msg="保存失败";
			}
		}

	}
	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:修改数据根据条件修改
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void modRec(String path,String fileName) throws SQLException, WrongSQLException{
		String swfPath="";
		String filename="";
		if(path.length()>0){
			swfPath=path.substring(0,path.lastIndexOf("."))+".swf";
		}
		//查询文件路径
		sql="select 文件路径 from V_教学管理_教学标准信息表 where 编号='"+MyTools.fixSql(getJXBZ_JXBZBM())+"'";
		vec=db.GetContextVector(sql);
		if(vec.size()>0&&vec!=null){
			filename=MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
		}
		//截取文件名
		filename=filename.substring(filename.lastIndexOf("/")+1,filename.lastIndexOf("."));
//		System.out.println(filename);
		//获取配置路径
		String url=MyTools.getProp(request, "Base.upLoadPath");
		//删除该教学标准信息的上传文件及pwf文件
		File folder = new File(url);
		File temp=null;
		File[] filelist= folder.listFiles();//列出文件里所有的文件
		int loc = 0;
		for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
			temp=filelist[i];
			loc = temp.getName().indexOf(filename);//获取文件名字符的位置
			if(loc!=-1){//去掉后缀，如果文件名为该文件名的话就删除
				temp.delete();//删除文件
			}
		}
		sql="UPDATE V_教学管理_教学标准信息表 " +
				"SET [教学标准名称] = '"+MyTools.fixSql(getJXBZ_JXBZMC())
				+"',文件路径='"+MyTools.fixSql(path)
				+"',文件名称='"+MyTools.fixSql(fileName)
				+"',预览文件路径='"+MyTools.fixSql(swfPath)+"'" +
				" WHERE [编号]='"+MyTools.fixSql(getJXBZ_JXBZBM())+"'";
		
		//判断是否修改成功
		if(db.executeInsertOrUpdate(sql)){
			this.msg="保存成功";//修改成功设置消息为�?操作成功�?
		}else{
			this.msg="保存失败";//修改失败设置消息为�?操作失败�?
		}
	}
	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:删除数据通过主键来做判断
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * 
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public void deleteRec()throws SQLException, WrongSQLException{
		String filename="";
		//查询文件路径
		sql="select 文件路径 from V_教学管理_教学标准信息表 where 编号='"+MyTools.fixSql(getJXBZ_JXBZBM())+"'";
		vec=db.GetContextVector(sql);
		if(vec.size()>0&&vec!=null){
			filename=MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));
		}
		//截取文件名
		filename=filename.substring(filename.lastIndexOf("/")+1,filename.lastIndexOf("."));
//		System.out.println(filename);
		//获取配置路径
		String url=MyTools.getProp(request, "Base.upLoadPath");
		//删除该教学标准信息的上传文件及pwf文件
		File folder = new File(url);
		File temp=null;
		File[] filelist= folder.listFiles();//列出文件里所有的文件
		int loc = 0;
		for(int i=0;i<filelist.length;i++){//对这些文件进行循环遍历
			temp=filelist[i];
			loc = temp.getName().indexOf(filename);//获取文件名字符的位置
			if(loc!=-1){//去掉后缀，如果文件名为该文件名的话就删除
				temp.delete();//删除文件
			}
		}
		sql="delete from V_教学管理_教学标准信息表  where 编号='"+MyTools.fixSql(getJXBZ_JXBZBM())+"'";
		if(db.executeInsertOrUpdate(sql)){
			this.msg="删除成功";//成功设置消息为<删除成功>
		}else{
			this.msg="删除失败";//失败设置消息为<删除成功>
		}
	}
	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:根据主键来获取一条资产信息
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * @throws SQLException
	 */
	public void loadData()throws SQLException {
		Vector vec = null;
		sql="select 教学标准名称,专业代码,文件路径" +
				" from V_教学管理_教学标准信息表    where 编号 = '" +  MyTools.StrFiltr(MyTools.fixSql(getJXBZ_JXBZBM())) + "'";
		vec = db.GetContextVector(sql);
		if(vec != null && vec.size() > 0){//返回集合有值且不为空
			this.Check_JXBZMC=MyTools.fixSql(MyTools.StrFiltr(vec.get(0)));//教学标准名称
			this.Check_ZYMC=MyTools.fixSql(MyTools.StrFiltr(vec.get(1)));//专业名称
			this.Check_path=MyTools.fixSql(MyTools.StrFiltr(vec.get(2)));//文件路径
		}
	}
	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:专业下拉框
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * @return
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public String queryZYType() throws SQLException,WrongSQLException{
		JSONArray reJal=new JSONArray();
		String sql = "SELECT  '' AS comboValue,'请选择' AS comboName FROM V_专业基本信息数据子类" +
				" union " +
				"SELECT 专业代码  AS comboValue,专业名称  AS comboName FROM V_专业基本信息数据子类  where 1=1";
		reJal=db.getConttexJONSArr(sql);
		return MyTools.StrFiltr(reJal.toString());
	}
	

	
	/**
	 * M12.1  教学管理
	 * createTime:2017.02.09
	 * createUser:马晓良
	 * description:为了减少请求,所以页面一加载,前台就发送请求到servlet,
	 * 			   servlet再从后台拿到所有下拉框的数据返回到前台.
	 * 
	 * editTime:
	 * editUser:
	 * description:
	 * @return
	 * @throws SQLException
	 * @throws WrongSQLException
	 */
	public JSONArray InitJXBZCombobox()throws SQLException,WrongSQLException{
		JSONArray reJal=new JSONArray();
		reJal = JsonUtil.addJsonParams(reJal, "queryZYType",queryZYType());
		//System.out.println("InitConsumableCombobox里面的==="+reJal.toString());
		return reJal;
	}
	/**
	 * get,set方法
	 */

	public String getUSERCODE() {
		return USERCODE;
	}

	public void setUSERCODE(String uSERCODE) {
		USERCODE = uSERCODE;
	}

	public String getResultjson() {
		return resultjson;
	}

	public void setResultjson(String resultjson) {
		this.resultjson = resultjson;
	}

	public String getJXBZ_JXBZBM() {
		return JXBZ_JXBZBM;
	}

	public void setJXBZ_JXBZBM(String jXBZ_JXBZBM) {
		JXBZ_JXBZBM = jXBZ_JXBZBM;
	}

	public String getJXBZ_JXBZMC() {
		return JXBZ_JXBZMC;
	}

	public void setJXBZ_JXBZMC(String jXBZ_JXBZMC) {
		JXBZ_JXBZMC = jXBZ_JXBZMC;
	}

	public String getJXBZ_ZYBM() {
		return JXBZ_ZYBM;
	}

	public void setJXBZ_ZYBM(String jXBZ_ZYBM) {
		JXBZ_ZYBM = jXBZ_ZYBM;
	}

	public String getJXBZ_ZYMC() {
		return JXBZ_ZYMC;
	}

	public void setJXBZ_ZYMC(String jXBZ_ZYMC) {
		JXBZ_ZYMC = jXBZ_ZYMC;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCXJXBZ_JXBZMC() {
		return CXJXBZ_JXBZMC;
	}

	public void setCXJXBZ_JXBZMC(String cXJXBZ_JXBZMC) {
		CXJXBZ_JXBZMC = cXJXBZ_JXBZMC;
	}

	public String getCXJXBZ_ZYBM() {
		return CXJXBZ_ZYBM;
	}

	public void setCXJXBZ_ZYBM(String cXJXBZ_ZYBM) {
		CXJXBZ_ZYBM = cXJXBZ_ZYBM;
	}

	public String getCheck_JXBZBM() {
		return Check_JXBZBM;
	}

	public void setCheck_JXBZBM(String check_JXBZBM) {
		Check_JXBZBM = check_JXBZBM;
	}

	public String getCheck_JXBZMC() {
		return Check_JXBZMC;
	}

	public void setCheck_JXBZMC(String check_JXBZMC) {
		Check_JXBZMC = check_JXBZMC;
	}

	public String getCheck_ZYMC() {
		return Check_ZYMC;
	}

	public void setCheck_ZYMC(String check_ZYMC) {
		Check_ZYMC = check_ZYMC;
	}

	public String getCheck_path() {
		return Check_path;
	}

	public void setCheck_path(String check_path) {
		Check_path = check_path;
	}


	
	
}
