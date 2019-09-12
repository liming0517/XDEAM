<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="java.io.File"%>
<%
	/**
		创建人：马晓良
		Create date: 2017.02.09
		功能说明：
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>教学标准信息列表</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/script/ajaxfileupload.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/script/FlexPaper/flexpaper_handlers.js"></script>
  	<script type="text/javascript" src="<%=request.getContextPath()%>/script/FlexPaper/flexpaper.js"></script>
  </head>
  <%
	/*
		获得角色信息
	*/
	UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));
	String userName =up.getUserName();
	if(userName==null){
		userName="";
	}
	String root = request.getContextPath();
	//out.println("当前在线人数： "+SessionListener.getCount());
	//人员对象		
	Vector v = new Vector(); 
	String usercode = "";
	usercode = MyTools.getSessionUserCode(request);
	String sAuth = "";
	//int i=0;
	AuthObject auth;
	//获取人员所属角色
	v = up.getUserAuth();
	//up.setUserAuth();
	if(v!=null && v.size()>0){
		for(int i=0; i<v.size(); i++){
			sAuth += "@"+MyTools.StrFiltr(v.get(i))+"@O";
		}
		sAuth = sAuth.substring(0, sAuth.length()-1);
	}
	// 如果无法获取人员信息，则自动跳转到登陆界面
%>
  <style>
		/*对角线矩形分别对应border-top和border-left两个属性来设定*/
		.d1{border-top:50px threedlightshadow solid;border-left:122px windowframe solid;width:0;height:0;position:relative;}
		.s1,.s2{display:block;width:40px;height:22px;}
		.s1{position:absolute;top:-40px;left:-55px;}
		.s2{position:absolute;bottom:1px;right:60px}
		
		#xlHead,#xlDetail,#xlTime{
			width:100%;
			border-right:1px solid gray;
		}
		#xlHead,#xlTime{
			width:99.9%;
			border-top:1px solid gray;
		}
		
		#xlHead td, #xlDetail td{
			border-left:1px solid gray;
			border-bottom:1px solid gray;
			text-align:center;
		}
		
		#xlTime td, #xlTime th{
			border-left:1px solid gray;
			border-bottom:1px solid gray;
			text-align:center;
			empty-cells:show;
		}
		
		.titleFont{
			font-weight:bold;
		}
		.tdWidth{
			width:12%;
		}
  </style>
<body  class="easyui-layout">
	<div id="ic_flexPaperZhezhao" style="position:absolute; z-index:99999; width:100%; height:100%; background-image:url('<%=request.getContextPath()%>/images/course/pic5.png'); display:none;  text-align:center;" onclick="$('#ic_flexPaperDiv').hide();">
			<font color="white" style="position:relative; top:45%; left:0;">文件预览加载中<br/><img src="<%=request.getContextPath()%>/images/course/loading.gif" /></font>
	</div>
			
	<!-- 文件预览 flexpaper -->
	<div id="ic_flexPaperDiv" style="position:absolute; z-index:99999; width:100%; height:100%; background-image:url('<%=request.getContextPath()%>/images/course/pic5.png'); display:none;" onclick="$('#ic_flexPaperDiv').hide();">
			<div id="documentViewer" class="flexpaper_viewer" style="width:90%;height:90%; position:relative; top:5%; left:5%;">
			</div>
	</div>	
	<div id="north" region="north" title="教学标准信息" style="height:91px;" >
		<table>
			<tr>
				<td><a href="#" id="upload" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">上传设置</a></td>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
				<td><a href="#" id="sh" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">审核</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">教学标准名称</td>
				<td>
					<input name="icCXJXBZ_JXBZMC" id="icCXJXBZ_JXBZMC" class="easyui-textbox" style="width:250px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">专业名称</td>
				<td>
					<select name="icCXJXBZ_ZYMC" id="icCXJXBZ_ZYMC" class="easyui-combobox" panelHeight="auto" style="width:250px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="jxbzList" style="width:100%;"></table>
	</div>
	
	<!-- 教学标准信息新建编辑 -->
	<div id="jxbzInfo" style="overflow:hidden;">
		<form id="form1" method='post'>
			<table style="width:100%;" class="tablestyle">
				<tr>
					<td class="titlestyle" width="35%">教学标准名称</td>
					<td>
						<input name="icJXBZ_JXBZMC" id="icJXBZ_JXBZMC" class="easyui-textbox" style="width:280px;" maxlength="50" required="true">
					</td>
				</tr>
				<tr>
					<td class="titlestyle">专业名称</td>
					<td>
						<select name="icJXBZ_ZYMC" id="icJXBZ_ZYMC" class="easyui-combobox" panelHeight="auto" editable="false" style="width:280px;"  required="true">
								
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">附件上传</td>
					<td>
						<!-- edittime:20170220 editname:马晓良 为了上传样式统一，换成附件管理样式 -->
						<input type="file" name="file1" id="icJXBZ_UPLOAD" style="width:280px;" />
						<!-- <table style="width:100%; ">
							<tr>
								<td style="width:120px; text-align:center; vertical-align:middle; border:0px; border-right:1px #CCCCCC solid;">
									<a href="#" onclick="openfj()" class="easyui-linkbutton" plain="true" iconCls="icon-new">查看/添加附件</a>
								<td id="filelist" style="border:0px;"></td>
							</tr>
						</table> -->
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="icJXBZ_JXBZBM" name="icJXBZ_JXBZBM"/>
			<input type="hidden" id="path" name="path"/>
			<input type="hidden" id="fileUpLoadName" name="fileUpLoadName"/>
		</form>
	</div>

</body>

<script type="text/javascript">
	var iKeyCode = ''; //定义主键
	var userCode = '<%=usercode%>';
	var count=0;//计数
	//上传路径
	var uploadPath='<%=MyTools.getProp(request, "Base.upLoadPath")%>';
	//预览路径
	var filePreview='<%=MyTools.getProp(request, "Base.filePreview")%>';
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		initCombobox();
		loadGrid();
	}
	
	/**加载combobox控件
	**/
	function initCombobox(){
		$.ajax({
		  type:'post',
		  url:'<%=request.getContextPath()%>/Svl_TeachManage',
		  data:"active=JXBZCombobox",
		  dataType:'json',
		  success:function(datas){
			//从后台拿到json对象赋值给变量
			var ZYCombobox=datas[0].queryZYType;
			$('#icCXJXBZ_ZYMC').combobox({
				data:ZYCombobox,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
	
			$('#icJXBZ_ZYMC').combobox({
				data:ZYCombobox,
				valueField:'comboValue',
				textField:'comboName',
				editable:false,
				panelHeight:'140', //combobox高度
				onLoadSuccess:function(data){
					//判断data参数是否为空
					if(data != ''){
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				},
				//下拉列表值改变事件
				onChange:function(data){}
			});
		  }
		});
	}

	/**加载 dialog控件**/
	function initDialog(){
		$('#jxbzInfo').dialog({   
			width: 480,//宽度设置   
			height: 143,//高度设置 
			modal:true,
			closed: true,   
			cache: false, 
			draggable:false,//是否可移动dialog框设置
			toolbar:[{
				//保存编辑
				text:'保存',
				iconCls:'icon-save',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('save');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				emptyDialog();
			}
		});

	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(){
		$('#jxbzList').datagrid({
			url:'<%=request.getContextPath()%>/Svl_TeachManage',
			queryParams:{
				"active":"queryList", //查询参数(1~2)
				"icCXJXBZ_JXBZMC" : $('#icCXJXBZ_JXBZMC').val(), //1,教学标准名称
				"icCXJXBZ_ZYMC" : $('#icCXJXBZ_ZYMC').combobox('getValue') //2,专业代码
			},
			loadMsg : "信息加载中请稍侯!", //载入时信息
			title:'教学标准信息列表',
			width : '100%', //宽度        
			nowrap : false, //截取当数据长度超出列宽时会自动截取
			fit : true, //自适应
			fitColumns : true, //自适应列宽防止出现水平滚动
			striped : true, //隔行变色
			pagination : true, //分页
			showFooter:true, //显示页脚信息
			pageSize : 10, //每页记录数
			singleSelect : true, //单选模式 这里是指只能选一行不能多选
			pageNumber :1, //当前页码
			rownumbers:true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'编号',title:'教学标准编号',hidden:true},
				{field:'教学标准名称',title:'教学标准名称',width:fillsize(0.12),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.12),align:'center'},
				{field:'创建人',title:'上传人',width:fillsize(0.15),align:'center'},
				{field:'创建时间',title:'上传时间',width:fillsize(0.15),align:'center'},
				{field:'状态',title:'状态',width:fillsize(0.15),align:'center'},
				{field:'col3',title:'附件',width:fillsize(0.15),align:'center',
						formatter:function(value,rec){
							return "<input type='button' style='cursor:pointer;' value='[查看]' onclick='preview(\""+rec.文件路径+"\",\""+rec.预览文件路径+"\");'>&nbsp;<input type='button' style='cursor:pointer;' value='[下载]' onclick='downupload(\""+uploadPath+rec.文件路径+"\",\""+rec.文件名称+"\");'>";
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.编号;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
		
	};
	
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'query'){
			loadGrid();
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'new'){
			//取消选中行
			iKeyCode = "";
			count=0;
			//打开dialog
			$('#jxbzInfo').dialog({   
				title: '新建教学标准信息'
			});
			$('#jxbzInfo').dialog('open');
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			//如果没有选中，则给予用户提示
			if(iKeyCode == ''){
				//提示信息
				alertMsg('请选择一行数据');
				return;
			}else{
				loadData();
				count=1;
				//打开dialog
				$('#jxbzInfo').dialog({   
					title: '编辑教学标准信息'   
				});
				$('#jxbzInfo').dialog('open');
			}
		}
		
		
		//判断获取参数为save，执行保存操作
		if(id == 'save'){ 
			var jxbzmc = $('#icJXBZ_JXBZMC').textbox('getValue');
			
			if(jxbzmc == ''){
				alertMsg('请填写教学标准名称');
				return;
			}
			
			if($('#icJXBZ_ZYMC').combobox('getValue') == ''){
				alertMsg("请选择专业名称");
				return;
			}
			
			var filename=$('#icJXBZ_UPLOAD').val();
			if(filename==""&&count==0){
				alertMsg("请选择上传文件");
				return;
			}else if(filename==""&&count==1){
				$("#form1").submit();
			}
			$('#active').val("save");
			bindPhotEvent();//保存路径，返回路径
			//ConfirmMsg("设置信息有改变，将重置节次时间设置，是否确定？","dosave()","");
			
		}
		
		if(id=="del"){//删除操作 
			if(iKeyCode==""){
	        	alertMsg("请先选择一行数据");
	        	return;
	     	}
	     	ConfirmMsg("删除后，将无法恢复，是否继续？","delRec();","");
		}
	}
	
	//打开教学标准信息编辑,并载入数据库中已存在的数据	
	function loadData(){
		$.ajax({
			type:'post',//传输方式为post
			url:'<%=request.getContextPath()%>/Svl_TeachManage',	
			data:"active=view&icJXBZ_JXBZBM=" + iKeyCode,	
			dataType:'json',//数据类型 
			success:function(datas){		
				var data = datas[0];
				if(data.check_JXBZMC != null && data.check_JXBZMC != ""){
					$('#icJXBZ_JXBZBM').val(data.JXBZ_JXBZBM);
					$('#icJXBZ_JXBZMC').textbox("setValue",data.check_JXBZMC); //资产编号
					$('#icJXBZ_ZYMC').combobox("setValue",data.check_ZYMC); //使用人
					$('#icJXBZ_ZYMC').combobox('disable'); 
					$('#path').val(data.check_path);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_TeachManage',
		//当点击事件后触发的事件
		onSubmit: function(data){
			
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				loadGrid();
				//提示信息
				showMsg(json[0].MSG);
				$("#active").val("");
				//清空并关闭dialog
				emptyDialog();
				$('#jxbzInfo').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	
	/**清空Dialog中表单元素数据
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:初始化每周天数,上午节数,下午节数,晚上节数
	 */
	function emptyDialog(){
		$('#icJXBZ_JXBZMC').textbox('setValue', '');
		$('#icJXBZ_ZYMC').combobox('setValue', '');
		$('#icJXBZ_UPLOAD').val('');
		$('#path').val('');
		$('#icJXBZ_ZYMC').combobox('enable'); 
	}
	
	function bindPhotEvent(){
	    var filePath=$("#icJXBZ_UPLOAD").val();
	    var fpath=$('#path').val();
		//alert(filePath);
		var pattern=/^[^\s]*$/;
		var fname=filePath.substring(filePath.lastIndexOf("\\")+1, filePath.length);
		if(!pattern.test(fname)){//匹配文件名
			alertMsg('文件名中不能有空格!',0);
			return;
		}
		//var extStart=filePath.lastIndexOf(".");
		//var ext=filePath.substring(extStart,filePath.length).toUpperCase();
		//if(filePath!=""){
			//if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){//图片类型
				//alertMsg('图片限于png,gif,jpeg,jpg,bmp格式!',0) ;
				//return;
			//}
		//}
		
	    var arr=filePath.split('\\');
	    var fileName=arr[arr.length-1];
	    //alert($("#ic_TP").attr("id"));
	   	
	   	if(fpath!=""&&filePath!=""){
			ConfirmMsg("您已上传过文件，再次上传将覆盖原文件，是否继续？","takephoto($('#icJXBZ_UPLOAD').attr('id'));","");
		}else if(fpath==""&&filePath!=""){
			takephoto($('#icJXBZ_UPLOAD').attr('id'));
		}
	      //  $(".showFileName").html("");
	      //  $(".fileerrorTip").html("您未上传文件，或者您上传文件类型有误！").show();
	     //   return false 
	}
function takephoto(fileId) {   //拍照上传图片
	//alert(fileId);
    $.ajaxFileUpload({
              url:'<%=request.getContextPath()%>/AjaxFileupload', //服务器地址
              secureuri:false,
              type:"post",
              data:{},
              fileElementId:fileId,//文件选择框的id属性
              dataType: 'json',//服务器返回的格式，可以是json
              success: function (data){    //相当于java中try语句块的用法
             		//alert(33333);
              		//alert(fileId);       //data是从服务器返回来的值   
              		//savePhotoInfo(fileId,data[0].src);
              		//form1Url=data[0].src;
              		//alert(data[0].src);
        			$("#path").val(data[0].src);
        			$("#fileUpLoadName").val(data[0].filename);
        			//$("#form1").attr("enctype","multipart/form-data");
        			//$("#form1").attr("encoding", "multipart/form-data");
        			//$('#iKeyCode').val(iKeyCode);
        			$("#form1").submit();
              //    $('#result').html('上传图片成功!请复制图片地址<br/>'+data.
               },error: function (data){            //相当于java中catch语句块的用法
               //alert(data);
               	 var json = eval("(" + data + ")");
               //	alert(json.src);
             	 // alert('上传图片失败');
               }
               });
   }
	
	//显示文本文件预览
	function preview(path,previewpath){
		$('#ic_flexPaperZhezhao').show();
		//alert(path);
		//var path1=document.location.href;
		var swfPath =filePreview+previewpath;
		//var swfPath = filePreview+previewpath;
		//alert(swfPath);
		//console.log(swfPath);
		//console.log(path);
		$.ajax({
		   type: 'POST',
		   url: '<%=request.getContextPath()%>/Svl_TeachManage',   
		   data: 'active=fileToSwf&filePath='+uploadPath+path, 
		   //dataType: 'json',	
		   success: function(datas){
				if(datas != '') {
		   			var json = eval("("+datas+")");
		   			//判断文件是否转换成功
		   			if(json[0].MSG == '转换成功'){ 
		   				
		   				$('#documentViewer').FlexPaperViewer({
		   					config : {
		   						//需要使用FlexPaper打开的文档
				                SWFFile : escape(swfPath), 
				                //初始化缩放比例
				                Scale : 0.7,    
				                //缩放样式,其他可选值包括：easenone，easeout，linear，easeoutquad
				                ZoomTransition : 'easeout',  
				                //从一个缩放比例变为另外一个缩放比例需要花费的时间，该参数值应该为0或更大
				                ZoomTime : 0.5,  
				                //缩放比例之间间隔
				                ZoomInterval : 0.2, 
				                //初始化时自适应页面
				                FitPageOnLoad : false,
				                 //初始化时自适应页面宽度  
				                FitWidthOnLoad : false, 
				                 //当设置为true时，单击全屏按钮会打开一个FlexPaper最大化的新窗口而不是全屏，当由于flash播放器因为安全而禁止全屏，而使用flexpaper作为独立的flash播放器的时候设置为true是个优先选择
				                FullScreenAsMaxWindow : false,
				                //当设置为true时，展示文档时不会加载完整个文档，而是逐步加载，但是需要将文档中转化为9以上的版本（使用pdf2swf的时候使用-T 9标签）
				                ProgressiveLoading : false, 
				                //设置最小的缩放比例
				                MinZoomSize : 0.5,  
				                //设置最大的缩放比例        
				                MaxZoomSize : 3,            
				                //设置为true时，单击搜索所有符合条件的地方高亮显示
				                SearchMatchAll : false,    
				                //设置启动模式如“Portrait”或“TowPage” 
				                InitViewMode : 'Portrait',  
				                RenderingOrder : 'flash',
				                StartAtPage : '',
				                //工具栏上是否显示样式选择框
				                ViewModeToolsVisible : true, 
				                //工具栏上时候显示缩放工具
				                ZoomToolsVisible : true,  
				                //工具栏上是否显示导航工具   
				                NavToolsVisible : true,  
				                //工具栏上是否显示光标工具    
				                CursorToolsVisible : true,   
				                //工具栏上是否显示搜索工具
				                SearchToolsVisible : false,  
				                WMode : 'window',
				                //设置地区（语言）
				                localeChain: 'zh_CN'  
				            }}
			    		);
						$('#ic_flexPaperZhezhao').hide();
			    		$('#ic_flexPaperDiv').show();
			    					   							   				
		   			}else if(json[0].MSG == '转换失败'){
		   				//判断serverMsg值
		   				if(json[0].serverMsg == 'openoffice服务未启动'){
		   					alertMsg('openoffice服务已重新启动，点击确定后可重新预览！');
		   				}else if(json[0].serverMsg == 'openofficeBat文件不存在'){
		   					alertMsg('openoffice服务启动文件不存在，如有疑问请联系管理员！');
		   				}else{
		   					alertMsg('文件暂时无法预览，如有疑问请联系管理员！');
		   				}
		   				$('#ic_flexPaperZhezhao').hide();
		   			}
		   		}
			}
		});			
    }

	//将iKeyCode(保存路径)传给download.jsp进行指定路径的下载
	function downupload(filepath,fileName){
		//需先选择数据,否则无法获得保存路径进行下载
		//if(iKeyCode == ""){
		//	alertMsg("请选择一行数据进行下载");
		//}else{
			window.open('<%=request.getContextPath()%>/form/elective/fileDownload.jsp?filePath=' + filepath+'&fileName=' +encodeURI(encodeURI(fileName)));
			//下载后重新加载列表数据
			//doSubmit();
		//}
	}
	
	//删除成功给予用户提示 删除成功语句
	function delRec(){
	//alert(iKeyCode);
			$.ajax({
			type:'post',
			url:"<%=request.getContextPath()%>/Svl_TeachManage",
			data:"active=deleteRow&icJXBZ_JXBZBM="+iKeyCode,
			dataType:'json',
				success:function(datas){
				var data=datas[0];
				showMsg(data["msg"]);
				loadGrid();
				}
			});
	}
	

</script>
</html>