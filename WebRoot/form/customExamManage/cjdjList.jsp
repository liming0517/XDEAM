<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.Vector"%>
<%@ page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%
	/**
		创建人：ZhaiXuChao
		Create date: 2017.06.30
		功能说明：成绩登记
		页面类型:列表及模块入口
		修订信息(有多次时,可有多个)
		修订日期:
		原因:
		修订人:
		修订时间:
	**/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<title>成绩登记</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/publicScript.js"></script>
	
	<style>
		#uploadDiv{
			position:relative;
			width:56px;
			height:26px;
			border:1px solid #ffffff;
			background-color:#ffffff;
			cursor:pointer;
			overflow:hidden;
		}
	
		#File1{
			height:24px;
			position:absolute;
			z-index:99;
			top:0;
			left:-170;
			opacity: 0; 
			filter: "alpha(opacity=0)"; 
			filter: alpha(opacity=0); 
			-moz-opacity: 0;
			cursor:pointer;
		}
		
		.maskStyle{
			width:100%;
			height:100%;
			overflow:hidden;
			display:none;
			background-color:#D2E0F2;
			filter:alpha(opacity=80);
			position:absolute;
			top:0px;
			left:0px;
			z-index:100;
		}
		#maskFont{
			 font-size:16px;
			 font-weight:bold;
			 color:#2B2B2B;
			 width:200px;
			 height:100%;
			 position:absolute;
			 top:50%;
			 left:50%;
			 margin-top:-10px;
			 margin-left:-80px;
		}
	</style>
</head>
<%
	/*
		获得角色信息
	 */
	UserProfile up = new UserProfile(request,
			MyTools.getSessionUserCode(request));
	String userName = up.getUserName();
	if (userName == null) {
		userName = "";
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
<body class="easyui-layout">
    <div id="north" region="north" title="班级成绩输入" style="height:59px;">
   		<table class="tablestyle" width="100%" border="0">
   			<tr>
				<td class="titlestyle">学年</td>
				<td >
					<select name="XN_CX" id="XN_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td class="titlestyle">学期</td>
				<td >
					<select name="XQ_CX" id="XQ_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td class="titlestyle">考试名称</td>
				<td>
					<input name="KSMC_CX" id="KSMC_CX" class="easyui-textbox" style="width:180px;"/>
				</td>
				<td class="titlestyle">考试类别</td>
				<td >
					<select name="KSLB_CX" id="KSLB_CX" class="easyui-combobox" style="width:180px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>	
			</tr>
	    </table>	    				
    </div>
	<div region="center">
		<table id="classList"></table>
	</div> 
	
	<!--dialog 考试成绩输入 -->
 	<div id="classScoreDialog" style="overflow:hidden;">
 		<%-- 遮罩层 --%>
    	<div id="divPageMask" class="maskStyle">
    		<div id="maskFont"></div>
    	</div>
 		<div style="width:100%; height:100%;" class="easyui-layout">
 			<div region="west" title="班级信息列表" style="width:200px;">
 				<ul id="classTree" class="easyui-tree" style="display:none">
				</ul>
 			</div>
 			<div region="center" title="学生成绩信息列表" style="overflow:hidden;">
 				<div class="easyui-layout" style="width:100%; height:100%;">
	 				<div region="north" style="height:36px;overflow:hidden;">
	 				<form id="formdr" name="formdr" method="POST">
		 				<table>
							<tr>
								<td id="saveTd"><a href="#" onclick="doToolbar(this.id);" id="save" class="easyui-linkbutton" plain="true" iconCls="icon-save" disabled="true">保存</a></td>
								<td><a href="#" onclick="doToolbar(this.id);" id="export" class="easyui-linkbutton" plain="true" iconCls="icon-submit" disabled="true">导出</a></td>
								<td id="importTd">
									<div id="uploadDiv">
										<a href="#" id="import" class="easyui-linkbutton" plain="true" iconCls="icon-submit" disabled="true" style="">导入</a>
										<input type="file" id="File1" onchange="doToolbar(this.id)" name="File1" style="display:none;"
											onmouseenter="$('#uploadDiv').css('border','1px solid #B7D2FF'); $('#uploadDiv').css('background-color','#E9F1FF');" 
											onmouseleave="$('#uploadDiv').css('border','1px solid #ffffff'); $('#uploadDiv').css('background-color','#ffffff');"/>
									</div>
								</td>
							</tr>
						</table>
						<input type="hidden" id="examCode" name="examCode"/>
						<input type="hidden" id="classCode" name="classCode"/>
						<input type="hidden" name="sxpath" id="sxpath"/>
						<input type="hidden" id="activedr" name="activedr"/>
						<input type="hidden" name="authcode" id="authcode"/>
			 		</form>
	 				</div>
	 				<div region="center" id="stuScoreDiv">
	 					<table id="loadMask" style="width:100%; height:100%; text-align:center; display:none;">
							<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"></td></tr>
						</table>
	 					<table id="stuScoreList"></table>
	 				</div>
 				</div>
 			</div>
 		</div>
 		<form id="form1" name="form1" method="POST">	
 			<input type="hidden" id="active" name="active"/>
 			<input type="hidden" id="CJ_KSBH" name="CJ_KSBH"/>
 			<input type="hidden" id="CJ_BJDM" name="CJ_BJDM"/>
 			<input type="hidden" id="updateScore" name="updateScore"/>
 		</form>
 	</div>
 	
 	<!-- 下载excel -->
	<iframe id="exportIframe" src="" style="width:0; height:0;"></iframe>
</body>
<script type="text/javascript">

	var sAuth = '<%=sAuth%>';
	var authType = '<%=MyTools.StrFiltr(request.getParameter("authType"))%>';
  	var USERCODE="<%=MyTools.getSessionUserCode(request)%>";
	var iKeyCode = '';
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var curSelClass = '';
	var colData = '';
	var lastIndex = 0;
	var tempNodeTarget = '';
	var examName_EX = "";
	var className_EX = "";
	var examCode_DR = "";
	var classCode_DR = "";
	var levelData = '';
	var existOption = new Array();
	var dfFlag = "set";
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjdj',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				if(data[0].curXnxq != ''){
					curXn = data[0].curXnxq.substring(0, 4);
					curXq = data[0].curXnxq.substring(4, 5);
				}
				levelData = data[0].djData;
				initCombobox(data[0].xnData, data[0].xqData, data[0].lbData);
				loadGrid(data[0].listData);
			}
		});
	}
	
	/**加载 datagrid控件，读取最外层页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#classList').datagrid({
			data:listData,
			title:'考试信息列表',
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
					
			//下面是表单中加载显示的信息
			columns:[[
				{field:'学年学期名称',title:'学年学期',width:fillsize(0.2),align:'center'},
				{field:'考试名称',title:'考试名称',width:fillsize(0.35),align:'center'},
				{field:'类别名称',title:'考试类别',width:fillsize(0.1),align:'center'},
				{field:'登分开始时间',title:'登分开始时间',width:fillsize(0.1),align:'center'},
				{field:'登分结束时间',title:'登分结束时间',width:fillsize(0.1),align:'center'},
				{field:'AA_SET',align:'center',title:'操作',width:fillsize(0.15),formatter:function(value,rec){
					var str = '';
					str = "<input type='button' ";
					if(rec.登分状态 == 'true'){
						str += "value='[输入成绩]' onclick='openScoreList(\"set\",\"" + rec.考试编号 + "\",\"" + rec.考试名称 + "\")'";
					}else{
						str += "value='[查询成绩]' onclick='openScoreList(\"view\",\"" + rec.考试编号 + "\",\"" + rec.考试名称 + "\")'";
					}
					str += "id='openScoreList' style='cursor:pointer;'>";
					return str;
				}}
			]],
			onClickRow:function(rowIndex,rowData){ 
				 iKeyCode = rowData.考试编号;
			},
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){
			},
			//成功加载时
			onLoadSuccess:function(data){//查询成功时候把后台的查询语句赋值给变量listsql
				iKeyCode = '';
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(xnData, xqData, lbData){
		$('#XN_CX').combobox({
			data:xnData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'140', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXn != ''){
						$(this).combobox('setValue', curXn);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#XQ_CX').combobox({
			data:xqData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
			onLoadSuccess:function(data){
				//判断data参数是否为空
				if(data != ''){
					if(curXq != ''){
						$(this).combobox('setValue', curXq);
					}else{
						//初始化combobox时赋值
						$(this).combobox('setValue', '');
					}
				}
			},
			//下拉列表值改变事件
			onChange:function(data){}
		});
		
		$('#KSLB_CX').combobox({
			data:lbData,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto', //combobox高度
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
	
	/**读取datagrid数据**/
	function loadData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjdj',
			dataType:"json",
			data : 'active=queryRec&page=' + pageNum + '&rows=' + pageSize + 
				'&CJ_XN=' + encodeURI($('#XN_CX').combobox('getValue')) + 
				'&CJ_XQ=' + encodeURI($('#XQ_CX').combobox('getValue')) +
				'&CJ_KSMC=' + encodeURI($('#KSMC_CX').textbox('getValue'))+
				'&CJ_KSLB=' + encodeURI($('#KSLB_CX').combobox('getValue')),
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	/**打开班级成绩输入对话框**/
	function openScoreList(type, examCode, examName){
		dfFlag = type;
		examName_EX = examName;
		examCode_DR = examCode;
		loadExamClassTree(examCode);
		//addStuScoreInfo(examCode);
		
		if(dfFlag == 'set'){
			$('#saveTd').show();
			$('#importTd').show();
		}else{
			$('#saveTd').hide();
			$('#importTd').hide();
		}
		
		$('#classScoreDialog').dialog('setTitle', examName);
		$('#classScoreDialog').dialog('open');
	}
	
	/**加载班级信息TREE
		@classData
	**/
	function loadExamClassTree(examCode){
		$('#classTree').tree({
			checkbox: false,
			url:'<%=request.getContextPath()%>/Svl_Cjdj?active=loadExamClassTree&level=0&sAuth=' + sAuth + '&CJ_KSBH=' + examCode,
		    onClick:function(node){
		    	//判断点击的是不是班级
		    	if($('#classTree').tree('getParent', node.target) != null){
		    		//判断点击的班级是不是当前选中的班级
		    		if(node.id != curSelClass){
		    			$('#File1').show();
		    			var updateScoreData = new Array;
		    			$('#import').linkbutton('enable');
		    			$('#export').linkbutton('enable');
		    			if(curSelClass != ''){
		    				//判断修改的课表是否保存过
			    			$('#stuScoreList').datagrid('endEdit', lastIndex);
							updateScoreData = $('#stuScoreList').datagrid('getChanges');
						}
		    			curSelClass = node.id;
		    			className_EX = node.text;
		    			classCode_DR = node.id;
		    			tempNodeTarget = node.target;
			    		if(updateScoreData.length > 0){
			    			ConfirmMsg('修改的成绩信息还未保存，是否需要保存？', 'doToolbar("save");', 'loadScoreList();');
			    		}else{
			    			loadScoreList();
			    		}
		    		}
		    	}else{
					$('#classTree').tree('select', tempNodeTarget);
		    	}
			},
			onDblClick:function(node){
				//判断点击的是不是班级
		    	if($('#classTree').tree('getParent', node.target) == null){
		    		$('#classTree').tree('select', tempNodeTarget);
		    	}
			},
		    onBeforeExpand:function(node,param){
			  	$('#classTree').tree('options').url="<%=request.getContextPath()%>/Svl_Cjdj?active=loadExamClassTree&level=1&sAuth=" + sAuth + "&CJ_KSBH=" + examCode + "&parentCode="+node.id;
			},
			onLoadSuccess:function(node, data){
				$("#classTree").show();
			}
		});
	};
	
	
	/**加载 dialog控件**/
	function initDialog(){
		$('#classScoreDialog').dialog({
			maximized:true,
			modal:true,
			closed: true,  
			cache: false,
            resizable:true, 
			draggable:true,//是否可移动dialog框设置
			toolbar:[{
				//返回
				text:'返回',
				iconCls:'icon-back',
				handler:function(){
					$('#classScoreDialog').dialog('close');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			onBeforeClose:function(){
				if(curSelClass != ''){
    				//判断是否有需要保存的数据
	    			$('#stuScoreList').datagrid('endEdit', lastIndex);
					updateScoreData = $('#stuScoreList').datagrid('getChanges');
	    			if(updateScoreData.length > 0){
	    				//提示保存成绩
	    				ConfirmMsg('有修改的成绩信息未保存，是否需要保存？', 'doToolbar("save");', 'closeDialog();');
	    				return false;
	    			}
	    		}
	    	},
			//关闭事件
			onClose:function(data){
				curSelClass = '';
				$('#import').linkbutton('disable');
				$('#export').linkbutton('disable');
				$('#save').linkbutton('disable');
				$('#stuScoreDiv').html('<table id="loadMask" style="width:100%; height:100%; text-align:center; display:none;">' +
					'<tr><td><img src="<%=request.getContextPath()%>/images/loading_3.gif"></td></tr></table>' +
					'<table id="stuScoreList"></table>');
				$('#classTree').html('');
			}
		});
	}	
	function closeDialog(){
		curSelClass = '';
		$("#classScoreDialog").dialog("close");
	}
	
	
	//dialog中的数据
	function loadScoreList(){
		$('#save').linkbutton('disable');
		$('#import').linkbutton('disable');
		$('#export').linkbutton('disable');
		$('#stuScoreList').hide();
		$('#loadMask').show();
	
	
		//读取科目信息和成绩信息
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjdj',
			data : 'active=loadExamInfo&sAuth=' + sAuth + '&CJ_KSBH=' + iKeyCode + '&CJ_BJDM=' + curSelClass +'&dfFlag=' + dfFlag,
			dataType:"json",
			success : function(data){
				//生成列信息
				colData = data[0].colData;
				var cols = new Array();
				
				if(dfFlag == 'set'){
					for(var i=0; i<colData.length; i++){
						if(colData[i].colType == '1'){
							cols.push({field:colData[i].colField,title:colData[i].colName,width:'120px',align:'center',
								editor:{
								type:'numberbox',
									options:{
										min:0,
										max:100,
										precision:1
									}
								},formatter:function(value,rec){
									var score = value;
									if(score!=undefined && score.substring(score.length-2) == '.0'){
										score = score.substring(0, score.length-2);
									}
									return score;
									
								}
							});
						}else if(colData[i].colType == '2'){
							cols.push({field:colData[i].colField,title:colData[i].colName,width:'120px',align:'center',
								editor:{
									type: 'combobox',
									options:{
					        			panelHeight:'auto',
					            		valueField:'comboValue',
					                    textField:'comboName',
					                    editable:false,
					                    data:levelData
									}
								},formatter:function(value,rec){
									var levelName = '';
									for(var i=0; i<levelData.length; i++){
										if(value!='' && levelData[i].comboValue==value){
											levelName = levelData[i].comboName;
											break;
										}
									}
									return levelName;
								}
							});
						}
					}
				}else{
					for(var i=0; i<colData.length; i++){
						if(colData[i].colType == '1'){
							cols.push({field:colData[i].colField,title:colData[i].colName,width:'120px',align:'center',
								formatter:function(value,rec){
									var score = value;
									if(score!=undefined && score.substring(score.length-2) == '.0'){
										score = score.substring(0, score.length-2);
									}
									return score;
									
								}
							});
						}else if(colData[i].colType == '2'){
							cols.push({field:colData[i].colField,title:colData[i].colName,width:'120px',align:'center',
								formatter:function(value,rec){
									var levelName = '';
									for(var i=0; i<levelData.length; i++){
										if(value!='' && levelData[i].comboValue==value){
											levelName = levelData[i].comboName;
											break;
										}else{
											levelName='缺考';
										}
									}
									return levelName;
								}
							});
						}
					}
				}
				
				$('#stuScoreList').datagrid({
					data:data[0].listData,
					width: '100%',
					nowrap: false,
					fit:true,
					//showFooter:true,
					striped:true, //隔行变色
					singleSelect:true,
					//fitColumns: true,
					frozenColumns:[[
						{field:'CX_KH',title:'班内学号',width:120,align:'center'},
						{field:'CX_XM',title:'姓名',width:80,align:'center'}
					]],
					columns:[cols],
					//单击某行时触发
					onClickRow:function(rowIndex, rowData){
						if(dfFlag == 'set'){
							$(this).datagrid('endEdit', lastIndex);
							$(this).datagrid('beginEdit', rowIndex);
							lastIndex = rowIndex;
							
							var djzARRAY = new Array();
							djzARRAY=rowData.flag;
							for(var i=0;i<djzARRAY.length;i++){
								if(djzARRAY[i].substring(djzARRAY[i].length-1)!='1'){
									var ed = $('#stuScoreList').datagrid('getEditor', {index:rowIndex,field:djzARRAY[i].substring(0,djzARRAY[i].length-1)});
									$(ed.target).combobox("destroy");
								}
								
							};
						}
					},
					//双击某行时触发
					onDblClickRow:function(rowIndex,rowData){
					},
					//读取datagrid之前加载
					onBeforeLoad:function(){},
					onClickCell:function(rowIndex, field, value){
						if(dfFlag == 'set'){
							setTimeout(function(){
								var edfirstClick=$('#stuScoreList').datagrid('getEditor', {index:rowIndex,field:field});//获取当前编辑器
								
								for(var i=0; i<colData.length; i++){
									var ed = $('#stuScoreList').datagrid('getEditor', {index:rowIndex,field:colData[i].colField});//获取当前编辑器
									initScoreInput(ed.target);
								}
								if(edfirstClick!=null && edfirstClick.type=='numberbox'){
									$(edfirstClick.target).next("span").children().first().focusEnd();
								}
							},10);
						}
					},
					//加载成功后触发
					onLoadSuccess: function(data){
						lastIndex = 0;
						$('#save').linkbutton('enable');
						$('#import').linkbutton('enable');
						$('#export').linkbutton('enable');
						$('#loadMask').hide();
						$('#stuScoreList').show();
						
					}	
				});
			}
		});
	}
	
	
	function doToolbar(iToolbar){
		//查询
		if(iToolbar == "query"){
			loadData();
		}
		//保存成绩
		if(iToolbar == 'save'){
			$('#stuScoreList').datagrid('endEdit', lastIndex);
			var updateScoreData = $('#stuScoreList').datagrid('getChanges');
			
			//判断是否有修改成绩
			if(updateScoreData.length > 0){
				//处理需要更新的成绩信息
				var updateResultArray = new Array();
				var stuCode = '';
				var score = '';
				
				for(var i=0; i<updateScoreData.length; i++){
					stuCode = updateScoreData[i].CX_XH;
					//alert(updateScoreData[i].)
					
					for(var j=0; j<colData.length; j+=2){
						updateResultArray.push(stuCode);
						updateResultArray.push(colData[j].colField.substring(0,colData[j].colField.length-2));
						score = updateScoreData[i][colData[j].colField];
						if(score.substring(score.length-2) == '.0'){
							score = score.substring(0, score.length-2);
						}
						updateResultArray.push(score);
						
						score = updateScoreData[i][colData[j+1].colField];
						if(score.substring(score.length-2) == '.0'){
							score = score.substring(0, score.length-2);
						}
						updateResultArray.push(score);
						//updateResult += stuCode+','+colData[j].colField+','+updateScoreData[i][colData[j].colField]+',';
					}
				}
				$('#active').val('saveScore');
				$('#CJ_BJDM').val(curSelClass);
				$('#CJ_KSBH').val(iKeyCode);
				$('#updateScore').val(updateResultArray.toString());
				$('#form1').submit();
			}else{
				showMsg('保存成功');
			}
		}
		//导出
		if(iToolbar == "export"){
			$('#maskFont').html('成绩信息导出中...');
    		$('#divPageMask').show();
			ExportExcel();
		}
		
		if(iToolbar == "File1"){
    		var uploadFile = $("#File1").val();
			var extension = uploadFile.substring(uploadFile.lastIndexOf('.')+1, uploadFile.length);//导入文件后缀名
			if(''==uploadFile){//未选择需要导入的文件
				alertMsg('请选择需要导入的文件!',0);
				//alert('请选择需要导入的文件');
				return;
			}
			if(extension != ('xls') && extension != ('xlsx')){//根据后缀判断是否为excel文件
				alertMsg('只允许导入excel文件，请重新选择导入文件!',0);
				return;
			}
			$('#maskFont').html('成绩信息导入中...');
    		$('#divPageMask').show();
			//importExcel();
			$('#examCode').val(examCode_DR);
			$('#classCode').val(classCode_DR);
			$('#authcode').val(sAuth);
			$('#sxpath').val(escape(uploadFile));
			$('#formdr').attr("enctype","multipart/form-data");
			$('#formdr').attr("encoding", "multipart/form-data");
			$('#activedr').val("saveimportxlsZSKSGL");
			$('#formdr').submit();
    	}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Cjdj',
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				loadScoreList();
				//提示信息
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	//输入框获取焦点并将光标移至文本最后位置
	$.fn.setCursorPosition = function(position){
	    if(this.lengh == 0) return this;
	    return $(this).setSelection(position, position);
	};
	
	$.fn.setSelection = function(selectionStart, selectionEnd) {
	    if(this.lengh == 0) return this;
	    input = this[0];
	
	    if (input.createTextRange) {
	        var range = input.createTextRange();
	        range.collapse(true);
	        range.moveEnd('character', selectionEnd);
	        range.moveStart('character', selectionStart);
	        range.select();
	    } else if (input.setSelectionRange) {
	        input.focus();
	        input.setSelectionRange(selectionStart, selectionEnd);
	    }
	
	    return this;
	};
	
	$.fn.focusEnd = function(){
	    this.setCursorPosition(this.val().length);
	};
	
	//导出Excel班级学生成绩
	function ExportExcel(){
	//alert($('#easy').val());
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Cjdj',
			data : 'active=ExportExcel&sAuth=' + sAuth + '&CJ_KSBH=' + iKeyCode + '&CJ_BJDM=' + curSelClass + '&examName=' + examName_EX + '&className=' + className_EX + '&dfFlag=' + dfFlag,
			dataType:"json",
			success : function(data) {
				$('#divPageMask').hide();
				if(data[0].MSG == '文件生成成功'){
					//下载文件到本地
					$("#exportIframe").attr("src", '<%=request.getContextPath()%>/form/scoreQuery/download.jsp?filePath=' + encodeURI(encodeURI(data[0].filePath)));
				}else{
					alertMsg(data[0].MSG);
				}
			}
		});
	}
	
	
	$('#formdr').form({
   		//定位到servlet位置的url
   		url:'<%=request.getContextPath()%>/Svl_Cjdj',
   		//当点击事件后触发的事件
   		onSubmit : function(data) {
   			$('#File1').hide();
   			$('.easyui-linkbutton').linkbutton('disable');
   		},
   		//当点击事件并成功提交后触发的事件
   		success : function(data) {
   			$('#divPageMask').hide();
   			$('#formdr').attr("enctype","application/x-www-form-urlencoded");
   			$('#formdr').attr("encoding", "application/x-www-form-urlencoded");
   			$('#File1').show();
   			$('.easyui-linkbutton').linkbutton('enable');
   			//根据记录状态，判断页面显示数据
   			var json = eval("(" + data + ")");
   			var afile = $("#File1");
   			afile.replaceWith(afile.clone());
   			$('#sxpath').val('');
   			if(json[0].MSG == '导入成功'){//反馈信息不为空
				showMsg(json[0].MSG);
				loadScoreList();
			}else{
				alertMsg(json[0].MSG);
				loadScoreList();
			}
   			$('#examCode').val('0');
   			$('#classCode').val('0');
   			$('#File1').val("");
   		}
    }); 
    
    
   function initScoreInput(ed){
   		$(ed).next().children().bind('keydown', function(e){
		//ie火狐兼容
		e = e || window.event;
		var curKey = e.which || e.keyCode;
		var curValue = $(this).val();
		//判断如果已经输入过0
		if(curValue == '0'){
			//backspace/delete/tab
			if(curKey==8 || curKey==9 || curKey==46) return true;
			//上下左右键
			if(curKey==37 || curKey==38 || curKey==39 || curKey==40) return true;
			//.
			if(curKey==190 || curKey==110) return true;
		}
		//判断是小数点后第几位
		else if(curValue.indexOf('.')>-1 && curValue.substring(curValue.indexOf('.')).length == 2){
			//backspace/delete/tab
			if(curKey==8 || curKey==9 || curKey==46) return true;
			//上下左右键
			if(curKey==37 || curKey==38 || curKey==39 || curKey==40) return true;
			}else{
				//判断如果已经输入过-
				if(curValue == '-'){
					//减号
					if(curKey==109 || curKey==189) return false;
					//0
					if(curKey==48 || curKey==96) return false;
					//.
					if(curKey==190 || curKey==110) return false;
				}
										
				//判断是否输入过小数点
				if(curValue.indexOf('.')<0 && curValue.length>0){
					//.
					if(curKey==190 || curKey==110) return true;
				}
										
				//数字
				if(curKey>=48 && curKey<=57) return true;
				//小数字键盘
				if(curKey>=96 && curKey<=105) return true;
				//减号
				if(curValue=='' && (curKey==109 || curKey==189)) return true;
				//backspace/delete/tab
				if(curKey==8 || curKey==9 || curKey==46) return true;
				//上下左右键
				if(curKey==37 || curKey==38 || curKey==39 || curKey==40) return true;
			}
									
			return false;
		}).bind('keyup', function(e){
				//ie火狐兼容
				e = e || window.event;
				var curKey = e.which || e.keyCode;
				var score = $(this).val();
									
				//应对中文输入法输入负号时，键值为229被屏蔽的情况。
				if(score=='' && curKey=='189'){
					$(this).val('-');
				}
									
				if(score > 100){
					score = '100';
					$(this).val(score);
				}
		});	
   }
	
</script>
</html>