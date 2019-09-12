<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：yeq
		Create date: 2016.03.08
		功能说明：用于添加学期课程
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
	<title>学期课程信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
  </head>
  <style>
  
  </style>
<body class="easyui-layout">
	<div id="north" region="north" style="height:89px; overflow:hidden;">
		<table>
			<tr>
				<td><a href="#" id="add" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">添加</a></td>
				<td><a href="#" id="del" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:100px;" class="titlestyle">学年学期名称</td>
				<td>
					<select name="XNXQBM_CX" id="XNXQBM_CX" class="easyui-combobox" style="width:150px;">
					</select>
				</td>
				<td style="width:100px;" class="titlestyle">专业名称</td>
				<td>
					<input name="ZYMC_CX" id="ZYMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:100px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>
			</tr>
			<tr>
				<td style="width:100px;" class="titlestyle">行政班名称</td>
				<td>
					<input name="XZBMC_CX" id="XZBMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td style="width:100px;" class="titlestyle">课程名称</td>
				<td>
					<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:150px;"/>
				</td>
				<td></td>
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="semCourseList" style="width:100%;"></table>
	</div>
	
	<!-- 添加学期课程 -->
	<div id="semCourseDialog">
		<div class="easyui-layout" style="width:100%; height:100%;">
			<div id="north" region="north" style="height:103px; overflow:hidden;" >
				<form id="form1" method='post'>
					<table style="width:100%;" class="tablestyle">
						<tr>
							<td class="titlestyle" style="width:75px;">学年学期</td>
							<td colspan="3">
								<select name="PT_XNXQBM" id="PT_XNXQBM" class="easyui-combobox" style="width:250px;">
								</select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">课程</td>
							<td>
								<select name="PT_KCBH" id="PT_KCBH" class="easyui-combobox" style="width:250px;">
								</select>
							</td>
							<td class="titlestyle">考试形式</td>
							<td>
								<select name="PT_KSXS" id="PT_KSXS" class="easyui-combobox" style="width:250px;">
								</select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle">教师类型</td>
							<td>
								<select name="PT_SKJSLX" id="PT_SKJSLX" class="easyui-combobox" style="width:250px;">
								</select>
							</td>
							<td class="titlestyle">教师</td>
							<td>
								<select name="PT_TEACODE" id="PT_TEACODE" class="easyui-combobox" style="width:250px;">
								</select>
							</td>
						</tr>
						<tr>
							<td class="titlestyle" style="width:75px;">学分</td>
							<td>
								<input name="PT_XF" id="PT_XF" class="easyui-numberbox" style="width:250px;" min="0" precision="1"/>
							</td>
							<td class="titlestyle" style="width:75px;">总课时</td>
							<td>
								<input name="PT_ZKS" id="PT_ZKS" class="easyui-numberbox" style="width:250px;" min="0" precision="0"/>
							</td>
						</tr>
					</table>
					
					<input type="hidden" id="active" name="active"/>
					<input type="hidden" id="classCode" name="classCode"/>
					<input type="hidden" id="PT_KCMC" name="PT_KCMC"/>
					<input type="hidden" id="PT_SKJSBH" name="PT_SKJSBH"/>
					<input type="hidden" id="PT_SKJSMC" name="PT_SKJSMC"/>
				</form>
			</div>
			<div region="center" title="班级信息列表">
				<div class="easyui-layout" style="width:100%; height:100%;">
					<div id="north" region="north" style="height:31px; overflow:hidden;" >
						<table class="tablestyle" style="width:100%;">
							<tr>
								<td class="titlestyle" style="width:75px;">行政班名称</td>
								<td>
									<input name="SEM_XZBMC_CX" id="SEM_XZBMC_CX" class="easyui-textbox" style="width:150px;"/>
								</td>
								<td class="titlestyle" style="width:75px;">专业名称</td>
								<td>
									<input name="SEM_ZYMC_CX" id="SEM_ZYMC_CX" class="easyui-textbox" style="width:150px;"/>
								</td>
								<td style="width:100px;" align="center">
									<a href="#" onclick="doToolbar(this.id)" id="queryClass" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
								</td>
							</tr>
						</table>
					</div>
					<div region="center">
						<!-- 
						<div id="treeDiv" style="width:100%; height:250px; overflow:auto;">
							<ul id="classTree" class="easyui-tree">
							</ul>
						</div>
						 -->
						 <table id="classList"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var firstFlag = true;
	
	$(document).ready(function(){
		initComboData();
		initDialog();//初始化对话框
		//loadClassTree();
		loadGrid();
		loadClassGrid();
	});
	
	/**页面初始化加载数据**/
	function initComboData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=initSemComboData',
			dataType:"json",
			success : function(data) {
				initCombobox(data[0].semData, data[0].courseData, data[0].ksxsData, data[0].teaData);
			}
		});
	}
	
	/**初始化下拉框数据
		@semData 学年学期
		@courseData 课程
	**/
	function initCombobox(semData, courseData, ksxsData, teaData){
		$('#XNXQBM_CX').combobox({
			data:semData,
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
	
		$('#PT_XNXQBM').combobox({
			data:semData,
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
	
		$('#PT_KCBH').combobox({
			data:courseData,
			valueField:'comboValue',
			textField:'comboName',
			//editable:false,
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
		
		$('#PT_KSXS').combobox({
			data:ksxsData,
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
		
		var teaType = [{"comboName":"自选教师","comboValue":"0"},{"comboName":"专业组长","comboValue":"1"},{"comboName":"体育老师","comboValue":"2"}];
		$('#PT_SKJSLX').combobox({
			data:teaType,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '0');
				}
			},
			//下拉列表值改变事件
			onChange:function(data){
				if(data == '1'){
					$('#PT_TEACODE').combobox('setText', '默认专业组长');
					$('#PT_TEACODE').combobox('disable');
				}else if(data == '2'){
					$('#PT_TEACODE').combobox('setText', '默认体育老师');
					$('#PT_TEACODE').combobox('disable');
				}else{
					$('#PT_TEACODE').combobox('setValue', '');
					$('#PT_TEACODE').combobox('enable');
				}
			}
		});
		
		$('#PT_TEACODE').combobox({
			data:teaData,
			valueField:'comboValue',
			textField:'comboName',
			//editable:false,
			panelHeight:'140', //combobox高度
			multiple:true,
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					//初始化combobox时赋值
					$(this).combobox('setValue', '');
				}
			},
			onSelect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length > 1){
					$(this).combobox('unselect', '');
				}
				
				if(data.comboValue == ''){
					$(this).combobox('clear');
					$(this).combobox('setValue', '');
				}
			},
			onUnselect:function(data){
				var obj = $(this).combobox('getValues');
				
				if(obj.length < 1){
					$(this).combobox('select', '');
				}
			}
		});
	}
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#semCourseDialog').dialog({
			title: '添加', 
			width: 650,//宽度设置   
			height: 450,//高度设置 
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
					doToolbar('addSemCourse');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			onBeforeClose:function(data){
				$('#classList').datagrid('selectRow', 0);
				$('#classList').datagrid('unselectRow', 0);
			},
			//关闭事件
			onClose:function(data){
				$('#PT_XNXQBM').combobox('setValue', '');
				$('#PT_KCBH').combobox('setValue', '');
				$('#PT_SKJSLX').combobox('setValue', '0');
				$('#PT_KSXS').combobox('setValue', '');
				$('#PT_XF').numberbox('setValue', '');
				$('#PT_ZKS').numberbox('setValue', '');
				$('#PT_TEACODE').combobox('setValue', '');
				$('#SEM_ZYMC_CX').textbox('setValue', '');
				$('#SEM_XZBMC_CX').textbox('setValue', '');
				//$("#classList").datagrid("reload");
				loadClassGrid();
				//取消tree选中
				/*
				var totalData = $('#classTree').tree('getChecked');
				for(var i=0; i<totalData.length; i++){
					$('#classTree').tree('uncheck', totalData[i].target);
				}
				$('#classTree').tree('collapseAll');
				$('#classTree').find('.tree-node-selected').removeClass('tree-node-selected');
				document.getElementById('treeDiv').scrollTop = 0;
				*/
			}
		});
	}

	/**加载 datagrid控件，读取页面信息**/
	function loadGrid(){
		$('#semCourseList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Pksz',
			queryParams:{'active':'queSemCourseList','XNXQBM_CX':$('#XNXQBM_CX').combobox('getValue'),
				'ZYMC_CX':encodeURI($('#ZYMC_CX').textbox('getValue')),
				'XZBMC_CX':encodeURI($('#XZBMC_CX').textbox('getValue')),
				'KCMC_CX':encodeURI($('#KCMC_CX').textbox('getValue'))},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:20,
			singleSelect:false,
			pageNumber:1,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'编号',hidden:true},
				{field:'学年学期名称',title:'学年学期名称',width:fillsize(0.15),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.2),align:'center'},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.2),align:'center'},
				{field:'课程名称',title:'课程名称',width:fillsize(0.35),align:'center'},
				{field:'授课教师姓名',title:'教师姓名',width:fillsize(0.1),align:'center'}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				//主键赋值
				iKeyCode = rowData.NJDM;
				row = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
	};
	
	/**加载班级信息TREE**/
	/**
	var checkZyFlag = false;
	var checkType = '';
	var checkObj = '';
	function loadClassTree(){
		$('#classTree').tree({
			checkbox: true,
			url:'<-%=request.getContextPath()%>/Svl_Pksz?active=queSemClassTree&level=0',
			/*
			onSelect:function(node){
				if(node.checked == false)
					$(this).tree('check', node.target);
				else
					$(this).tree('uncheck', node.target);
			},
		    onCheck:function(node){
		    	if(node.id.indexOf('zy') > -1)
					checkType = 'zy';
				else if(node.id.indexOf('bj') > -1)
					checkType = 'bj';
		    
		    	if(node.checked==false && checkType=='zy'){
		    		checkZyFlag = true;
			    	$(this).tree('expand', node.target);
			    	checkFlag = true;
			    	checkObj = node.target;
		    	}
			},
		    onBeforeExpand:function(node,param){
			  	$('#classTree').tree('options').url="<-%=request.getContextPath()%>/Svl_Pksz?active=queSemClassTree&level=1&parentCode=" + node.id.substring(3);
			},
			onLoadSuccess:function(node, data){
				if(checkZyFlag == true){
					$(this).tree('check', checkObj);
					checkZyFlag = false;
					checkType = '';
					checkObj = '';
				}
			}
		});
	};
	*/
	
	/**加载 班级信息datagrid控件，读取页面信息**/
	function loadClassGrid(){
		$('#classList').datagrid({
			url: '<%=request.getContextPath()%>/Svl_Pksz',
			queryParams:{'active':'queSemClassList','SEM_ZYMC_CX':encodeURI($('#SEM_ZYMC_CX').textbox('getValue')),
				'SEM_XZBMC_CX':encodeURI($('#SEM_XZBMC_CX').textbox('getValue'))},
			title:'',
			width:'100%',
			nowrap: false,
			fit:true, //自适应父节点宽度和高度
			showFooter:true,
			striped:true,
			pagination:true,
			pageSize:50,
			pageList: [20,50,100,150,200], 
			singleSelect:false,
			pageNumber:1,
			rownumbers:true,
			fitColumns: true,
			columns:[[
				//field为读取数据的数据名，title为显示的数据名，width宽度设置，align数字在表格中显示的位置
				{field:'ck',checkbox:true},
				{field:'行政班代码',hidden:true},
				{field:'行政班名称',title:'行政班名称',width:fillsize(0.5),align:'center'},
				{field:'专业名称',title:'专业名称',width:fillsize(0.5),align:'center'}
			]],
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
		
		//查询班级列表
		if(id == 'queryClass'){
			loadClassGrid();
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'add'){
			$('#semCourseDialog').dialog('open');
		}

		if(id == 'del'){
			var courseData = $('#semCourseList').datagrid('getSelections');
			if(courseData.length == 0){
				alertMsg('请选择学期课程');
				return;
			}
			
			var delCode = "";
			for(var i=0; i<courseData.length; i++){
				delCode += courseData[i].编号+',';
			}
			delCode = delCode.substring(0, delCode.length-1);
			ConfirmMsg('是否确定要删除当前选择的学期课程', 'delSemCourse("' + delCode + '");', '');
		}
		
		if(id == 'addSemCourse'){
			if($('#PT_XNXQBM').combobox('getValue') == ''){
				alertMsg('请选择学年学期');
				return;
			}
			
			if($('#PT_KCBH').combobox('getValue') == ''){
				alertMsg('请选择课程');
				return;
			}
			
			if($('#PT_KSXS').combobox('getValue') == ''){
				alertMsg('请选择考试形式');
				return;
			}
			
			if($('#PT_XF').numberbox('getValue') == ''){
				alertMsg('请填写学分');
				return;
			}
			
			if($('#PT_ZKS').numberbox('getValue') == ''){
				alertMsg('请填写总课时');
				return;
			}
			
			if($('#PT_SKJSLX').combobox('getValue')=='0' && ($('#PT_TEACODE').combobox('getValue')=='' || $('#PT_TEACODE').combobox('getValue')==undefined)){
				alertMsg('请选择教师');
				return;
			}
			
			var totalData = $('#classList').datagrid('getSelections');
			var classCode = '';
			
			if(totalData.length == 0){
				alertMsg('请至少选择一个班级');
				return;
			}
			for(var i=0; i<totalData.length; i++){
				classCode += totalData[i].行政班代码+',';
			}
			classCode = classCode.substring(0, classCode.length-1);
			
			$('#active').val(id);
			$('#classCode').val(classCode);
			$('#PT_KCMC').val($('#PT_KCBH').combobox('getText'));
			$('#PT_SKJSBH').val($('#PT_TEACODE').combobox('getValues'));
			$('#PT_SKJSMC').val($('#PT_TEACODE').combobox('getText'));
			$("#form1").submit();
		}
	}
	
	//删除
	function delSemCourse(delCode){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Pksz',
			data : 'active=delSemCourse&delCode=' + delCode,
			dataType:"json",
			success : function(data){
				if(data[0].MSG == '删除成功'){
					showMsg(data[0].MSG);
					loadGrid();
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Pksz',
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
				
				loadGrid();
				$('#semCourseDialog').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
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
	/*
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
	*/
</script>
</html>