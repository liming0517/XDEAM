<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：lupengfei
		Create date: 2016.02.04
		功能说明：用于设置课程信息
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="java.util.Vector"%>
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
%>
<%
		/*
			获得角色信息
		*/
		
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
		sAuth = MyTools.StrFiltr(v.get(0));
		// 如果无法获取人员信息，则自动跳转到登陆界面
%>
<html>
  <head>
	<title>学生选课</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
  	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/form/css/uploadify.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/form/File/jquery.uploadify-v2.1.4/swfobject.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/form/File/jquery.uploadify-v2.1.4/jquery.uploadify.v2.1.4.min.js"></script>

  </head>
  <style>
  
  </style>
<body  class="easyui-layout">
	<div id="north" region="north" title="课程信息" style="height:61px;" >
		<table>
			<tr>
<!-- 				<td><a href="#" id="baoming" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">报名</a></td> -->
				<td><a href="#" id="stuinfo" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">个人报名信息</a></td>
<!-- 				<td><a href="#" id="return" class="easyui-linkbutton" plain="true" iconcls="icon-return" onClick="doToolbar(this.id);" title="">返回</a></td> -->
			</tr>
		</table>
	</div>
	<div region="center">
		<table id="selectionList" style="width:100%;"></table>
	</div>
	
	<div id="tsc" title="编辑" style="overflow-x:hidden;">
			<table id="tsctable" class = "tablestyle" width="566px">
														
			</table>		
	</div>
	
	
	
</body>
<script type="text/javascript">
	var iUSERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var sAuth="<%=sAuth%>";
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var NJDM_CX = '';//查询条件
	var ZYDM_CX = '';
	var KCDM_CX = '';
	var KCMC_CX = '';
	var saveType = '';
	
	var xnxqbm="";
	var xxbmc="";
	var skjsbh="";
	var skjsxm="";
	var kcdm="";
	var cdyq="";
	var cdmc="";
	var skzc="";
	var sksj="";
	var bmrs="";
	var zrs="";
	var bkbmzybh="";
	var skjhzbbh="";

	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_outsideExam',
			data : 'active=loadSelection&page=' + pageNum + '&rows=' + pageSize+'&iUSERCODE='+iUSERCODE,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
				//initCombobox(data[0].kclxData, data[0].zydmData);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(){
		
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#courseInfo').dialog({   
			width: 300,//宽度设置   
			height: 168,//高度设置 
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
		
		$('#tsc').dialog({   
				title: '个人报名信息',   
				width: 680,//宽度设置   
				height: 350,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					initData();
				}
		});
			
		$('#uploadPhoto').dialog({   
				title: '上传照片',   
				width: 250,//宽度设置   
				height: 318,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					//loadGrid();
				}
		});
			
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#selectionList').datagrid({
			//url:'semesterList.json',
			data:listData,
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'外考考试名称',title:'外考考试名称',width:fillsize(0.15),align:'center'},
				{field:'报名开始时间',title:'报名开始时间',width:fillsize(0.1),align:'center'},
				{field:'报名结束时间',title:'报名结束时间',width:fillsize(0.1),align:'center'},
				{field:'考试日期',title:'考试日期',width:fillsize(0.1),align:'center'},
				{field:'报名费用',title:'报名费用',width:fillsize(0.1),align:'center'},
				{field:'报名人数',title:'报名人数',width:fillsize(0.1),align:'center'},
				{field:'备注',title:'备注',width:fillsize(0.2),align:'center'},
				{field:'是否报名',title:'报名情况',width:fillsize(0.08),align:'center',
					formatter:function(value,rec){
						var sfbm="";
						if(rec.是否报名=="1"){
							sfbm="已报名";
						}else{
							sfbm='<input id="'+rec.外考考试编号+'" type="button" value="[报名]" style="cursor:pointer;display:block;" onclick="candidate();" />';
						}
						return sfbm;
					}
				},
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.外考考试编号;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';	
			}
		});
		
		$("#selectionList").datagrid("getPager").pagination({
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) { 
				pageNum = pageNo;
				pageSize = pageSize_1;
				initData();
			} 
		});
	};
	
	function goEnterPage(){
		var e = jQuery.Event("keydown");//模拟一个键盘事件
		e.keyCode = 13;//keyCode=13是回车 
		$("input.pagination-num").trigger(e);//模拟页码框按下回车 
	}
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize + 
				'&KCDM_CX=' + encodeURI(KCDM_CX) + 
				'&KCMC_CX=' + encodeURI(KCMC_CX) + 
				'&ZYDM_CX=' + encodeURI(ZYDM_CX),
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	function loadGridBMInfo(){
		$('#tsctable').datagrid({
			url : '<%=request.getContextPath()%>/Svl_outsideExam?active=loadGridBMInfo&iUSERCODE='+iUSERCODE,
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:false,
			pageSize:pageSize,
			singleSelect:true,
			pageNumber:pageNum,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'外考考试名称',title:'外考考试名称',width:fillsize(0.2),align:'center'},
				{field:'考试日期',title:'考试日期',width:fillsize(0.1),align:'center'},
				{field:'是否交费',title:'是否交费',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						var sfjf="";
						if(rec.是否交费=="0"){
							sfjf="否";
						}else{
							sfjf="是";
						}
						return sfjf;
					}
				},				
				{field:'del',title:'操作',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						var delbutton="";
						if(rec.是否交费=="0"&&rec.可操作=="1"){
							delbutton='<input id="'+rec.外考考试编号+'" type="button" value="[删除]" style="cursor:pointer;display:block;" onclick="delWKExam(this.id);" />';
						}
						return delbutton;
					}
				}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				$('#tsctable').datagrid("unselectRow", rowIndex);
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
	};
	
	//删除外考考试
	var examid="";
	function delWKExam(id){
		examid=id;
		ConfirmMsg("确定删除报名吗？","delWKSelection()","");
	}
	
	//报名
	function candidate(){
		if(sAuth!="99"){
			alertMsg("对不起，只有学生可以报名");
		}else{
			ConfirmMsg("确定报名这门考试吗？","checkBM()","");
		}	
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
	
		//学生选课查询
		if(id == 'stuinfo'){
			$('#tsc').show();
			$('#tsc').dialog("open");
			loadGridBMInfo();				
		}
		
		
		//查询
		if(id == 'query'){
			KCDM_CX = $('#KCDM_CX').textbox('getValue'); 
			KCMC_CX = $('#KCMC_CX').textbox('getValue');
			ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
			loadData();
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'new'){
			//打开dialog
			$('#courseInfo').dialog({   
				title: '新建课程信息'
			});
			saveType = 'new';
			$('#courseInfo').dialog('open');
			$('#ZYDM').combobox("disable");
			$('#KCLX').combobox("enable");
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			
			//打开dialog
			$('#courseInfo').dialog({   
				title: '编辑班级信息'   
			});
			if(row!=undefined && row!=''){
				$('#form1').form('load', row);
				$('#KCDM').html(row.KCDM);
				if(row.ZYDM=="000000"){
					$('#ZYDM').combobox('setValue','');
					$('#KCLX').combobox('setValue','01');		
				}else if(row.ZYDM=="900000"){
					$('#ZYDM').combobox('setValue','');
					$('#KCLX').combobox('setValue','03');	
				}else{
					$('#KCLX').combobox('setValue','02');		
				}
				$('#ZYDM').combobox("disable");	
				$('#KCLX').combobox("disable");			
			}
			saveType = 'edit';
			$('#courseInfo').dialog('open');
		}
		
		if(id == 'return'){
			window.location = "<%=request.getContextPath()%>/form/homePage.jsp";
		}
		
		if(id == 'del'){
			if(iKeyCode == ''){
				alertMsg('请选择一行数据');
				return;
			}
			ConfirmMsg('是否确定要删除当前选择的课程', 'delClass();', '');
		}
		
		//判断获取参数为save，执行保存操作
		if(id == 'save'){
			var KCMC = $('#KCMC').textbox('getValue');

			if(KCMC == ''){
				alertMsg('请填写课程名称');
				return;
			}
			if(KCMC.length > 50){
				alertMsg('课程名称长度超出50位');
				return;
			}
			if($('#KCLX').combobox('getValue') == '' ){
				alertMsg('请选择课程类型');
				return;
			}
			if($('#KCLX').combobox('getValue') == '02' && $('#ZYDM').combobox('getValue') == ''){
				alertMsg('请选择所属专业');
				return;
			}
			
			$('#kcbh').val($('#KCDM').html());
			$('#active').val(saveType);
			$("#form1").submit();
		}
		//上传照片
		if(id == "uploadPic"){
			var obj = document.getElementById('browse') ; 
			obj.outerHTML=obj.outerHTML;  
			getPhotoPath();		
		}
		//上传
		if(id == "upload"){
			var filename=$('#browse').val();
			if(filename==''||filename==null){//未选择文件
					alertMsg('请选择文件!',0) ;
					//alert('请选择文件');
					return;
			}
			$("#active").val("uploadPhoto");
			$('#path').val(escape(filename));
			$('#iUSERCODE').val(iUSERCODE);
			$("#form1").attr("enctype","multipart/form-data");
			$("#form1").attr("encoding", "multipart/form-data");
			$("#form1").submit();
		}
	}
	
	//保存选课
	function saveSelection(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_outsideExam',
			data : 'active=saveSelection&iKeyCode='+iKeyCode+'&iUSERCODE='+iUSERCODE,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '报名成功'){
					showMsg(data[0].MSG);
					initData();
				}else{
					showMsg(data[0].MSG);
				}
			}
		});
	}
	
	//删除外考考试报名
	function delWKSelection(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_outsideExam',
			data : 'active=delWKSelection&iKeyCode='+examid+'&iUSERCODE='+iUSERCODE,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					$('#tsc').dialog("close");
					loadGridBMInfo();
				}else{
					showMsg(data[0].MSG);
				}
			}
		});
	}
	
	//检查报名情况
	function checkBM(){
		$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_outsideExam',
				data : 'active=checkBM&iKeyCode='+iKeyCode+'&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data){
					if(data[0].MSG == "yes"){
						saveSelection();
					}else{
						alertMsg(data[0].MSG);
					}	
				}
		});
	}
	
	function getPhotoPath(){
		$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_outsideExam',
				data : 'active=getPhotoPath&iUSERCODE='+iUSERCODE,
				dataType:"json",
				success : function(data){
					var filename=data[0].MSG;
					if(data[0].MSG==""){//未上传过照片
						$("#selpic").attr('src','<%=request.getContextPath()%>/images/qsczp/qsczp.jpg');		
						$('#uploadPhoto').dialog('open');
					}else{
						//var extName = filename.substring(filename.lastIndexOf("."),filename.length);  
	    				fipath='<%=MyTools.getProp(request, "Base.upLoadPathFile")%>'+'/studentPhoto/'+filename;
						$("#selpic").attr('src','<%=request.getContextPath()%>/Svl_outsideExam?active=showImg&name='+filename+'&path='+fipath);		
						$('#uploadPhoto').dialog('open');
					}
				}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_outsideExam?active=uploadPhoto&iUSERCODE='+iUSERCODE+'&path='+$('#path').val(),
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			$("#form1").attr("enctype","application/x-www-form-urlencoded");
			$("#form1").attr("encoding", "application/x-www-form-urlencoded");
			var json = eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				getPhotoPath();
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	/**清空Dialog中表单元素数据**/
	function emptyDialog(){
		saveType = '';
		$('#KCDM').html("");
		$('#KCMC').textbox('setValue', '');
		$('#ZYDM').combobox('setValue', '');
		$('#KCLX').combobox('setValue', '');
	}
	
	//防JS注入
	/*
	function checkData(str) {
        var  entry = { "'": "&apos;", '"': '&quot;', '<': '&lt;', '>': '&gt;' };
        str = v.replace(/(['")-><&\\\/\.])/g, function ($0){
        	return entry[$0] || $0;
        });
        return str;
    }*/
	
	
	//处理键盘事件
	// 禁止后退键（Backspace）密码或单行、多行文本框除外
	function banBackSpace(e){
	    var ev = e || window.event;//获取event对象
	    var obj = ev.target || ev.srcElement;//获取事件源
	    var t = obj.type || obj.getAttribute('type');//获取事件源类型
	    
	    //获取作为判断条件的事件类型
	    var vReadOnly = obj.getAttribute('readonly');
	    var vEnabled = obj.getAttribute('enabled');
	
	    //处理null值情况
	    vReadOnly = (vReadOnly == null)?false:vReadOnly;
	    vEnabled = (vEnabled == null)?true:vEnabled;
	    
	    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，
	    //并且readonly属性为true或enabled属性为false的，则退格键失效
	    var flag1=(ev.keyCode == 8 && (t=="password"|| t=="text"|| t=="textarea") && (vReadOnly==true|| vEnabled!=true))?true:false;
	
	    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
	    var flag2=(ev.keyCode == 8 && t != "password"&& t != "text"&& t != "textarea")?true:false;
	
	    //判断
	    if(flag2)
	        return false;
	
	    if(flag1)
	        return false;
	}
	
	//禁止后退键(作用于Firefox、Opera)
	document.onkeypress=banBackSpace;
	//禁止后退键 (作用于IE、Chrome)
	document.onkeydown=banBackSpace;
</script>
</html>