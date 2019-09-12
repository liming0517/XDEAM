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
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request)); 
String userName =up.getUserName();
String userCode =up.getUserCode();
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
  </head>
  <style>
  
  </style>
<body  class="easyui-layout">
	<div id="north" region="north" title="课程信息" style="height:61px;" >
		<table>
			<tr>
				<td><a href="#" id="xuanke" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">选课</a></td>
				<td><a href="#" id="stuinfo" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">个人选课信息</a></td>
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
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 50; //datagrid初始页内行数
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
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
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
				title: '个人选课信息',   
				width: 680,//宽度设置   
				height: 300,//高度设置 
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
				{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.15),align:'center',
					formatter:function(value,rec){
						var xq=rec.学年学期编码;
						xq=xq.substring(0,4)+"年第"+xq.substring(4,5)+"学期";
						return xq;
					}
				},
				{field:'选修班名称',title:'选修班名称',width:fillsize(0.2),align:'center'},
				{field:'授课教师姓名',title:'授课教师姓名',width:fillsize(0.15),align:'center'},
				{field:'场地名称',title:'场地名称',width:fillsize(0.1),align:'center'},
				{field:'授课周次',title:'授课周次',width:fillsize(0.1),align:'center'},
				{field:'上课时间',title:'上课时间',width:fillsize(0.2),align:'center',
					formatter:function(value,rec){
							var sksj=rec.上课时间;
							var show="";
							if(sksj!=""){
								var sksj2=sksj.split(",");
								var week=sksj2[0].substring(0,2);
								if(week=="01"){
									week="周一";
								}else if(week=="02"){
									week="周二";
								}else if(week=="03"){
									week="周三";
								}else if(week=="04"){
									week="周四";
								}else if(week=="05"){
									week="周五";
								}
								var apm=parseInt(sksj2[0].substring(2,4),10);
								if(apm<5){
									apm="上午";
								}else if(apm>6){
									apm="下午";
								}
								var num=(parseInt(sksj2[0].substring(2,4),10)-2)+"-"+(parseInt(sksj2[sksj2.length-1].substring(2,4),10)-2);
								show=week+apm+"第"+num+"节课";
							}else{
							
							}
							return show;
					}
				},
				{field:'报名人数',title:'报名人数',width:fillsize(0.1),align:'center'},
				{field:'总人数',title:'总人数',width:fillsize(0.1),align:'center'}
				
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.授课计划明细编号;
				skjhzbbh = rowData.授课计划主表编号;
				xnxqbm=rowData.学年学期编码;
				kcdm=rowData.课程代码;
				skzc=rowData.授课周次;
				sksj=rowData.上课时间;
				bmrs=rowData.报名人数;
				zrs=rowData.总人数;
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
	
	function loadGrid2(){
		$('#tsctable').datagrid({
			url : '<%=request.getContextPath()%>/Svl_CourseSet?active=showSelection&iUSERCODE='+iUSERCODE+'&xnxqbm='+xnxqbm,
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
				{field:'学年学期编码',title:'学年学期编码',width:fillsize(0.15),align:'center',
					formatter:function(value,rec){
						var xq=rec.学年学期编码;
						xq=xq.substring(0,4)+"年第"+xq.substring(4,5)+"学期";
						return xq;
					}
				},
				{field:'学号',title:'学号',width:fillsize(0.1),align:'center'},
				{field:'姓名',title:'姓名',width:fillsize(0.1),align:'center'},
				{field:'选修班名称',title:'选修班名称',width:fillsize(0.2),align:'center'},
				//{field:'学分',title:'学分',width:fillsize(0.1),align:'center'},
				{field:'del',title:'操作',width:fillsize(0.1),align:'center',
					formatter:function(value,rec){
						var delbutton="";
						if(rec.学年学期编码==rec.当前学年学期){
							delbutton='<input id="'+rec.授课计划明细编号+'" type="button" value="[删除]" style="cursor:pointer;display:block;" onclick="delSelection(this.id);" />';
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
	
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//选课
		if(id == 'xuanke'){
			if(iKeyCode==""){
				alertMsg("请选择一门选修课");
			}else{
				if(bmrs==zrs){
					alertMsg("对不起，该课程报名人数已满，请选择其它课程！");
				}else{
					$.ajax({
						type : "POST",
						url : '<%=request.getContextPath()%>/Svl_CourseSet',
						data : 'active=checkSelection&iKeyCode='+iKeyCode+'&iUSERCODE='+iUSERCODE+'&xnxqbm='+xnxqbm+'&skzc='+skzc+'&sksj='+sksj+'&kcdm='+kcdm,
						dataType:"json",
						success : function(data){
							if(data[0].MSG == "3"){
								alertMsg("每位学生最多选择3门选修课，您已经选了3门课程!");
							}else if(data[0].MSG == "2"){
								alertMsg("一个学期最多选择2门选修课，"+xnxqbm.substring(0,4)+"年第"+xnxqbm.substring(4,5)+"学期您已经选了2门课程!");
							}else if(data[0].MSG == "4"){
								alertMsg("该课程的授课周次和您选的另一门课程有冲突，请选择其它课程!");
							}else if(data[0].MSG == "5"){
								alertMsg("您已经选过该课程，请选择其它课程!");
							}else{
								ConfirmMsg("选好的课程将不能修改,确定选择这门课程吗？","saveSelection()","");
							}
						}
					});
				}
			}
		}
		
		//学生选课查询
		if(id == 'stuinfo'){
			$('#tsc').show();
			$('#tsc').dialog("open");
			loadGrid2();				
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
	}
	
	//保存选课
	function saveSelection(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=saveSelection&iKeyCode='+iKeyCode+'&iUSERCODE='+iUSERCODE+'&xnxqbm='+xnxqbm,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '报名成功'){
					showMsg(data[0].MSG);
					//calculateCredits();
					initData();
				}else{
					showMsg(data[0].MSG);
				}
			}
		});
	}
	
	//删除选课
	function delSelection(id){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=delSelection&iKeyCode='+id+'&iUSERCODE='+iUSERCODE,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadGrid2();
					
				}else{
					showMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_CourseSet',
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json = eval("("+data+")");
			
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
				$("#active").val('');
				
				emptyDialog();//清空并关闭dialog
				loadData();
				$('#courseInfo').dialog('close');
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
	
	
	//判断学分
	function calculateCredits(){
		 $.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_CourseSet',
			data : 'active=calculateCredits&iUSERCODE='+iUSERCODE,
			dataType:"json",
			success : function(data){
				 	if(data[0].PromptMsg !=''){
						var msgArray = data[0].PromptMsg;
						var tempMsgArray = '';
						var xkMsgArray = new Array();
						var tips = '';
						if(msgArray.subInfo != ''){
							tempMsgArray = msgArray.subInfo.split('@');
							xkMsgArray=tempMsgArray[1].split(',');
							tips = tempMsgArray[0]+'</br>';
							for(var i=0;i<xkMsgArray.length;i+=2){
								tips += xkMsgArray[i]+xkMsgArray[i+1]+'</br>';
							}
						}
						if(msgArray.sourceInfo != ''){
							tips += '</br>'+msgArray.sourceInfo+'';
							if(tips.length>0)
								tips += '<br/><br/>';
						}
						if(tips.length > 0){
							alertMsg(tips);//返回提示信息
						}
				}
			}
		}); 
	}
</script>
</html>