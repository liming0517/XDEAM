<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：yeq
		Create date: 2015.05.14
		功能说明：用于设置学年学期
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
	<title>学年学期信息列表</title>
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
	<div id="north" region="north" title="学年学期信息" style="height:91px;" >
		<table>
			<tr>
				<td><a href="#" id="new" class="easyui-linkbutton" plain="true" iconcls="icon-new" onClick="doToolbar(this.id);" title="">新建</a></td>
				<td><a href="#" id="edit" class="easyui-linkbutton" plain="true" iconcls="icon-edit" onClick="doToolbar(this.id);" title="">编辑</a></td>
			</tr>
		</table>
		<table id="ee" singleselect="true" width="100%" class="tablestyle">
			<tr>
				<td style="width:150px;" class="titlestyle">学年学期名称</td>
				<td>
					<input name="GX_XNXQMC_CX" id="GX_XNXQMC_CX" class="easyui-textbox" style="width:250px;"/>
				</td>
				<td style="width:150px;" class="titlestyle">教学性质</td><!-- 根据当前登录人权限 -->
				<td>
					<select name="GX_JXXZ_CX" id="GX_JXXZ_CX" class="easyui-combobox" panelHeight="auto" style="width:250px;">
					</select>
				</td>
				<td style="width:150px;" align="center">
					<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
				</td>				
			</tr>
	    </table>
	</div>
	<div region="center">
		<table id="xnxqList" style="width:100%;"></table>
	</div>
	
	<!-- 学年学期信息新建编辑 -->
	<div id="xnxqInfo" style="overflow:hidden;">
		<form id="form1" method='post'>
			<table style="width:100%;" class="tablestyle">
				<tr>
					<td class="titlestyle" width="35%">学年</td>
					<td>
						<input name="GX_XN" id="GX_XN" class="easyui-textbox" style="width:180px;" maxlength="4" required="true">
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学期</td>
					<td>
						<select name="GX_XQ" id="GX_XQ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:180px;"  required="true">
								<option value="">请选择</option>
								<option value="1">第一学期</option>
								<option value="2">第二学期</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">教学性质</td>
					<td>
						<select name="GX_JXXZ" id="GX_JXXZ" class="easyui-combobox" panelHeight="auto" editable="false" style="width:180px;">
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学期开始日期</td>
					<td>
						<input style="width:180px;" class="easyui-datebox" id="GX_XQKSSJ" name="GX_XQKSSJ" editable="false" required="true"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">学期结束日期</td>
					<td>
						<input style="width:180px;" class="easyui-datebox" id="GX_XQJSSJ" name="GX_XQJSSJ" editable="false" required="true"/>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">排课截止日期</td>
					<td>
						<input style="width:180px;" class="easyui-datebox" id="GX_PKJZSJ" name="GX_PKJZSJ" editable="false" required="true"/>
					</td>
				</tr>
				<!--
					editTime:2015-05-28
					editUser:wangzh
					description:添加每周天数,上午节数,下午节数,晚上节数
				 -->
				<tr>
					<td class="titlestyle">每周天数</td>
					<td>
						<select name="GX_MZTS" id="GX_MZTS" class="easyui-combobox" panelHeight="105" editable="false" style="width:180px;">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
							<option value="25">25</option>
							<option value="26">26</option>
							<option value="27">27</option>
							<option value="28">28</option>
							<option value="29">29</option>
							<option value="30">30</option>
							<option value="31">31</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">上午节数</td>
					<td>
						<select name="GX_SWJS" id="GX_SWJS" class="easyui-combobox" panelHeight="105" editable="false" style="width:180px;">
							<option value="0">0</option>
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">中午节数</td>
					<td>
						<select name="GX_ZWJS" id="GX_ZWJS" class="easyui-combobox" panelHeight="105" editable="false" style="width:180px;">
							<option value="0">0</option>
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">下午节数</td>
					<td>
						<select name="GX_XWJS" id="GX_XWJS" class="easyui-combobox" panelHeight="105" editable="false" style="width:180px;">
							<option value="0">0</option>
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">晚上节数</td>
					<td>
						<select name="GX_WSJS" id="GX_WSJS" class="easyui-combobox" panelHeight="105" editable="false" style="width:180px;">
							<option value="0">0</option>
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="titlestyle">实际上课周数</td>
					<td>
						<input name="GX_SJSKZS" id="GX_SJSKZS" class="easyui-numberspinner" style="width:180px;" min="0" increment="1" precision="0" editable="true" onblur="" />
					</td>
				</tr>
			</table>
			
			<input type="hidden" id="active" name="active"/>
			<input type="hidden" id="GX_XNXQBM" name="GX_XNXQBM"/>
			<input type="hidden" id="GX_XQFW" name="GX_XQFW"/>
		</form>
	</div>
	
	<!-- 学期校历信息 -->
	<div id="xqxlInfo" style="overflow:hidden;">
		<table id="xlHead" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div class="d1"><span class="s1 titleFont">星期</span><span class="s2 titleFont">周次</span></div>
				</td>
				<td class="titleFont tdWidth">日</td>
				<td class="titleFont tdWidth">一</td>
				<td class="titleFont tdWidth">二</td>
				<td class="titleFont tdWidth">三</td>
				<td class="titleFont tdWidth">四</td>
				<td class="titleFont tdWidth">五</td>
				<td class="titleFont tdWidth">六</td>
				<td ><div style="width:16px; height:50px;"></div></td>
			</tr>
		</table>
		<div style="width:99.9%; height:330px; overflow-y:scroll;">
			<table id="xlDetail" cellspacing="0" cellpadding="0">
			</table>
		</div>
	</div>
	
	<!--
		editTime:2015-05-28
		editUser:wangzh
		description:添加节次时间设置界面
	 -->
	<div id="setTime" style="overflow:hidden;">
		<form id='form2' method='post' style="margin: 0px">
			<table id="xlTime" cellspacing="0" cellpadding="0" style="height:100%;border-collapse:collapse;"> 
				<tbody id="content"> 
					 
				</tbody> 
			</table> 
			<input type="hidden" id="active1" name="active1"/>
			<input type="hidden" id="GJ_XNXQBM" name="GJ_XNXQBM"/>
			<input type="hidden" id="GJ_JC" name="GJ_JC"/>
			<input type="hidden" id="GJ_KSSJ" name="GJ_KSSJ"/>
			<input type="hidden" id="GJ_JSSJ" name="GJ_JSSJ"/>
		</form>
	</div>
</body>
<script type="text/javascript">
	var row = '';      //行数据
	var iKeyCode = ''; //定义主键
	var lastIndex = -1;//使datagrid选中行取消，以便下次选择从新开始
	var pageNum = 1;   //datagrid初始当前页数
	var pageSize = 20; //datagrid初始页内行数
	var GX_XNXQMC_CX = '';//查询条件
	var GX_JXXZ_CX = '';
	
	var jxxz = "";     //教学性质下拉框数据
	var saveType = "";     //判断打开窗口的操作（new or edit）
	
	var tempHolidayArray = ''; //用于临时保存当前选中学期节假日
	
	var savexn="";
	var savexq="";
	var savejxxz="";
	var saveksrq="";
	var savejsrq="";
	var savejzrq="";
	var savemtzs="";
	var saveswjs="";
	var savezwjs="";
	var savexwjs="";
	var savewsjs="";
	var savesjskzs="";
	
	$(document).ready(function(){
		initDialog();//初始化对话框
		initData();//页面初始化加载数据
	});
	
	/**页面初始化加载数据**/
	function initData(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xnxq',
			data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
			dataType:"json",
			success : function(data) {
				jxxz = data[0].jxxzData;
				loadGrid(data[0].listData);
				initCombobox(jxxz);
			}
		});
	}
	
	/**加载combobox控件
		@jxxzData 下拉框数据
	**/
	function initCombobox(jxxzData){
		$('#GX_JXXZ_CX').combobox({
			data:jxxzData,
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

		$('#GX_JXXZ').combobox({
			data:jxxzData,
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

	/**加载 dialog控件**/
	/**
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:添加加载节次时间设置dialog控件
	 */
	function initDialog(){
		$('#xnxqInfo').dialog({   
			width: 350,//宽度设置   
			height: 368,//高度设置 
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
			},{
				//保存编辑
				text:'节次时间设置',
				iconCls:'icon-edit',
				handler:function(){
					//传入save值进入doToolbar方法，用于保存
					doToolbar('set');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				emptyDialog();
				saveType = '';
			}
		});
		
		$('#xqxlInfo').dialog({   
			title:'学期校历信息',
			width: 800,//宽度设置   
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
					doToolbar('saveHoliday');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				$('#xlDetail').html('');
			}
		}); 
		
		$('#setTime').dialog({   
			title:'节次时间设置',
			width: 800,//宽度设置   
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
					doToolbar('saveTime');
				}
			}],
			//打开事件
			onOpen:function(data){},
			//读取事件
			onLoad:function(data){},
			//关闭事件
			onClose:function(data){
				
			}
		}); 
	}

	/**加载 datagrid控件，读取页面信息
		@listData 列表数据
	**/
	function loadGrid(listData){
		$('#xnxqList').datagrid({
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
				{field:'GX_XN',title:'学年',hidden:true},
				{field:'GX_XQ',title:'学期',hidden:true},
				{field:'GX_XNXQBM',title:'学年学期编码',width:fillsize(0.12),align:'center'},
				{field:'GX_XNXQMC',title:'学年学期名称',width:fillsize(0.12),align:'center'},
				{field:'GX_JXXZ',title:'教学性质',width:fillsize(0.11),align:'center',formatter:function(value, row, index){
					for(var i=0; i<jxxz.length; i++){
						if(value == jxxz[i].comboValue){
							return jxxz[i].comboName;
						}
					}
					return value;
				}},
				{field:'GX_XQKSSJ',title:'学期开始日期',width:fillsize(0.15),align:'center'},
				{field:'GX_XQJSSJ',title:'学期结束日期',width:fillsize(0.15),align:'center'},
				{field:'GX_PKJZSJ',title:'排课截止日期',width:fillsize(0.15),align:'center'},
				{field:'weekNum',title:'周次',width:fillsize(0.1),align:'center'},
				{field:'GX_JJRQ',title:'节假日期',hidden:true},
				{field:'col4',title:'操作',width:fillsize(0.1),align:'center',
						formatter:function(value,rec){
							return "<input type='button' value='[校历]' onclick='loadSchoolCalendar(\""+rec.GX_XQKSSJ+"\",\""+rec.GX_XQJSSJ+"\",\""+rec.GX_JJRQ+"\");'>";
				}}
			]],
			//双击某行时触发
			onDblClickRow:function(rowIndex,rowData){},
			//读取datagrid之前加载
			onBeforeLoad:function(){},
			//单击某行时触发
			onClickRow:function(rowIndex,rowData){
				lastIndex = rowIndex;
				//主键赋值
				iKeyCode = rowData.GX_XNXQBM;
				row = rowData;
			},
			//加载成功后触发
			onLoadSuccess: function(data){
				iKeyCode = '';
			}
		});
		
		$("#xnxqList").datagrid("getPager").pagination({
			total:listData.total,
			afterPageText: '页&nbsp;<a href="#" onclick="goEnterPage();">Go</a>&nbsp;&nbsp;&nbsp;共 {pages} 页',
			onSelectPage:function (pageNo, pageSize_1) { 
				pageNum = pageNo;
				pageSize = pageSize_1;
				loadData();
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
			url : '<%=request.getContextPath()%>/Svl_Xnxq',
			data : 'active=query&page=' + pageNum + '&rows=' + pageSize + 
				'&GX_XNXQMC_CX=' + encodeURI(GX_XNXQMC_CX) + 
				'&GX_JXXZ_CX=' + GX_JXXZ_CX,
			dataType:"json",
			success : function(data) {
				loadGrid(data[0].listData);
			}
		});
	}
	
	function loadTime(){
		$.ajax({
			type : "POST",
			url : '<%=request.getContextPath()%>/Svl_Xnxq',
			data : 'active=loadTime&GJ_XNXQBM='+iKeyCode,
			dataType : 'json',
			success : function(datas) {
				var html='';
				html += '<tr align="center">'+
							'<th>'+
								'节次' +
							'</th>'+
							'<th>'+
								'开始时间' +
							'</th>'+
							'<th>'+
								'结束时间' +
							'</th>'+
						'</tr>';
				for(var i=0;i<datas.length;i++){
					html += '<tr align="center">'+
								'<th class="jc">'+
									datas[i].节次 +
								'</th>'+
								'<td class="ks">'+
									datas[i].开始时间 +
								'</td>'+
								'<td class="js">'+
									datas[i].结束时间 +
								'</td>'+
							'</tr>';
				}
				$('#content').html(html);
				$.parser.parse(('#content'));
				$('#setTime').dialog('open');
				editTable();
			}
		});
	}
	
	function editTable(){
		var content; 
		$("#content td").click(function(){ 
			var clickObj = $(this); 
			content = clickObj.html();
			changeToEdit(clickObj); 
		}); 
		function changeToEdit(node){ 
			node.html(""); 
			var inputObj = $("<input type='text'/>"); 
			inputObj.css("border","0").css("background-color",node.css("background-color")) 
			.css("font-size",node.css("font-size")).css("height","17px")
			.css("width",node.css("width")).css("width","50px")
			.css("text-align",node.css("text-align")).css("text-align","center")
			.val(content).appendTo(node) 
			.get(0).select();
			inputObj.click(function(){
				return false;
			}).keyup(function(event){ 
				var keyvalue = event.which; 
				if(keyvalue==13){ 
					node.html(node.children("input").val()); 
				} 
				if(keyvalue==27){ 
					node.html(content); 
				} 
			}).blur(function(){ 
				if(node.children("input").val()!=content){ 
					//if(confirm("是否保存修改的内容？","Yes","No")){ 
					node.html(node.children("input").val()); 
					//}else{
						//node.html(content); 
					//} 
				}else{
					node.html(content); 
				} 
			});
		} 
	}
	
	/**工具栏按钮调用方法，传入按钮的id
		@id 当前按钮点击事件
	**/
	function doToolbar(id){
		//查询
		if(id == 'query'){
			GX_XNXQMC_CX = $('#GX_XNXQMC_CX').textbox('getValue'); 
			GX_JXXZ_CX = $('#GX_JXXZ_CX').combobox('getValue');
			loadData();
		}
		
		//判断获取参数为new，执行新建操作
		if(id == 'new'){
			//取消选中行
			$('#xnxqList').datagrid("unselectRow", lastIndex);
			iKeyCode = "";
			row = "";
			//打开dialog
			$('#xnxqInfo').dialog({   
				title: '新建学年学期信息'
			});
			saveType = 'new';
			$('#xnxqInfo').dialog('open');
		}

		//判断获取参数为edit，执行编辑操作
		if(id == 'edit'){
			//如果没有选中，则给予用户提示
			if(iKeyCode == ''){
				//提示信息
				alertMsg('请选择一行数据');
				return;
			}else{
				saveType = 'edit';
				//打开dialog
				$('#xnxqInfo').dialog({   
					title: '编辑学年学期信息'   
				});
				$('#xnxqInfo').dialog('open');

				if(row!=undefined && row!=''){
					$('#form1').form('load', row);
					savexn=$('#GX_XN').val();
					savexq=$('#GX_XQ').combobox('getValue')
					savejxxz=$('#GX_JXXZ').combobox('getValue');
					saveksrq=$('#GX_XQKSSJ').datebox('getValue');
					savejsrq=$('#GX_XQJSSJ').datebox('getValue');
					savejzrq=$('#GX_PKJZSJ').datebox('getValue');
					savemtzs=$('#GX_MZTS').combobox('getValue');
					saveswjs=$('#GX_SWJS').combobox('getValue');
					savezwjs=$('#GX_ZWJS').combobox('getValue');
					savexwjs=$('#GX_XWJS').combobox('getValue');
					savewsjs=$('#GX_WSJS').combobox('getValue');
					savesjskzs=$('#GX_SJSKZS').numberbox('getValue');
				}
			}
		}
		
		//判断获取参数为set，执行节次时间设置操作
		if(id == 'set'){
			//如果没有选中，则给予用户提示
			if(iKeyCode == ''){
				//提示信息
				alertMsg('请选择一行数据');
				return;
			}else{
				loadTime();
			}
		}
		
		//判断获取参数为save，执行保存操作
		if(id == 'save'){ 
			var xn = $('#GX_XN').textbox('getValue');
			
			//判断填写的学年是否符合规范
			if(xn == ''){
				alertMsg('请填写学年');
				return;
			}else{
				if(!xn.match(/^\d{4,4}$/)){
					alertMsg('填写的学年不符合规范，请重新填写。');
					return;
				}
			}
			
			if($('#GX_XQ').combobox('getValue') == ''){
				alertMsg("请选择学期");
				return;
			}
			
			if($('#GX_JXXZ').combobox('getValue') == ''){
				alertMsg("请选择教学性质");
				return;
			}
			
			var beginDate = $("#GX_XQKSSJ").datebox('getValue');
			var endData = $("#GX_XQJSSJ").datebox('getValue');
			var pkData = $("#GX_PKJZSJ").datebox('getValue');
			
			//判断开始时间是否大于结束时间
			if(beginDate > endData){
				alertMsg("开学日期必须在结束日期之前");
				return;
			}
			
			//判断开始时间是否大于排课截止日期
			//if(beginDate > pkData){
			//	alertMsg("开学日期必须在排课截止日期之前");
			//	return;
			//}
			
			if(saveType=="new"){
				dosave();
			}else{
				if(savexn==$('#GX_XN').val()&&savexq==$('#GX_XQ').combobox('getValue')&&savejxxz==$('#GX_JXXZ').combobox('getValue')&&saveksrq==$('#GX_XQKSSJ').datebox('getValue')&&savejsrq==$('#GX_XQJSSJ').datebox('getValue')&&savejzrq==$('#GX_PKJZSJ').datebox('getValue')&&savemtzs==$('#GX_MZTS').combobox('getValue')&&saveswjs==$('#GX_SWJS').combobox('getValue')&&savezwjs==$('#GX_ZWJS').combobox('getValue')&&savexwjs==$('#GX_XWJS').combobox('getValue')&&savewsjs==$('#GX_WSJS').combobox('getValue')&&savesjskzs==$('#GX_SJSKZS').numberbox('getValue')){
					//没有改动过
	
					//提示信息
					showMsg("保存成功");
					$("#active").val("");
					//清空并关闭dialog
					emptyDialog();
					$('#xnxqInfo').dialog('close');
				}else{//改动过
					//学期开始日期和学期结束日期没有改动过
					if(saveksrq==$('#GX_XQKSSJ').datebox('getValue')&&savejsrq==$('#GX_XQJSSJ').datebox('getValue')){
						$('#GX_XQFW').val("0");//未改动
					}else{
						$('#GX_XQFW').val("1");//改动过
					}
					ConfirmMsg("设置信息有改变，将重置节次时间设置，是否确定？","dosave()","");
				}
			}
			
		}
		
		//保存校历
		if(id == 'saveHoliday'){
			var str = '';
			//检查是否已经是节假日
			for(var i=0; i<tempHolidayArray.length; i++){
				str += tempHolidayArray[i]+',';
			}
			if(str.length > 0){
				str = str.substring(0, str.length-1);
			}
			
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Xnxq',
				data : 'active=updateHoliday&GX_XNXQBM=' + iKeyCode + '&GX_JJRQ=' + encodeURI(str),
				dataType:"json",
				success : function(data) {
					if(data[0].MSG == '保存成功'){
						$('#xqxlInfo').dialog('close');
						
						//更新datagrid数据
						var temp = "{GX_JJRQ:'" + str + "'}";
						var datas = eval("(" + temp + ")");
						$('#xnxqList').datagrid('updateRow',{
							index: lastIndex,
							row: datas
						});
						
						showMsg(data[0].MSG);
					}else{
						alertMsg(data[0].MSG);
					}
				}
			});
		}
		
		//保存设置的节次时间
		if(id == 'saveTime'){
			$("#active1").val('saveTime');
			$("#GJ_XNXQBM").val(iKeyCode);
			var tab=document.getElementById("content");
     		var rows=tab.rows;
     		var datePattern = /^(?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]$/;
     		for(var i=1;i<rows.length;i++){
		        for(var j=1;j<rows[i].cells.length;j++){
					if(!datePattern.test(rows[i].cells[j].innerHTML) || rows[i].cells[j].innerHTML==""){
						alertMsg((rows[i].cells[j].innerHTML)+"不符合格式。（正确格式为00:00-23:59之间）");
						return;
					}
		        }
			}
			
			var tempObj = '';
		    var startTime = '';
		    var endTime = '';
		    var orderName = '';
		    
		    tempObj = $('.ks');
			for(var i=0; i<tempObj.length;i ++){
				startTime += $(tempObj[i]).html()+",";
		    } 
			startTime = startTime.substring(0, startTime.length-1);
		    
		    tempObj = $('.js');
			for(var i=0; i<tempObj.length; i++){
				endTime += $(tempObj[i]).html()+",";
		    } 
		    endTime = endTime.substring(0, endTime.length-1);
		    
		    tempObj = $('.jc');
			for(var i=0; i<tempObj.length; i++){
				orderName += $(tempObj[i]).html()+",";
		    } 
		    orderName = orderName.substring(0, orderName.length-1);
		    
		    //20170203添加检验节次时间的正确性yeq
		    var startTimeArray = startTime.split(',');
		    var endTimeArray = endTime.split(',');
		    var flag = true;
		    
		    for(var i=0; i<startTimeArray.length; i++){
		    	if(parseInt(startTimeArray[i].replace(':', ''),10) > parseInt(endTimeArray[i].replace(':', ''),10)){
		    		flag = false;
		    		break;
		    	}
		    	
		    	if(i > 0){
		    		if(parseInt(startTimeArray[i].replace(':', ''),10) <= parseInt(endTimeArray[i-1].replace(':', ''),10)){
		    			flag = false;
		    			break;
		    		}
		    	}
		    }
		    
		    if(flag == false){
		    	alertMsg('节次时间设置不符合以下规则：<br/>1.开始时间必须小于等于结束时间<br/>2.开始时间必须大于上一节次的结束时间<br/><br/>请修改后重新保存！');
		    	return;
		    }
		    
		    $('#GJ_KSSJ').val(startTime);
		    $('#GJ_JSSJ').val(endTime);
		    $('#GJ_JC').val(orderName);
		    $("#form2").submit();
		}
	}
	
	/**表单提交**/
	$('#form1').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Xnxq',
		//当点击事件后触发的事件
		onSubmit: function(data){
			return $(this).form('validate');//验证
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				loadData();
				//提示信息
				showMsg(json[0].MSG);
				$("#active").val("");
				//清空并关闭dialog
				emptyDialog();
				$('#xnxqInfo').dialog('close');
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	$('#form2').form({
		//定位到servlet位置的url
		url:'<%=request.getContextPath()%>/Svl_Xnxq',
		//当点击事件后触发的事件
		onSubmit: function(data){
			
		}, 
		//当点击事件并成功提交后触发的事件
		success:function(data){
			var json  =  eval("("+data+")");
			if(json[0].MSG == '保存成功'){
				showMsg(json[0].MSG);
			}else{
				alertMsg(json[0].MSG);
			}
		}
	});
	
	function dosave(){
		$("#active").val('save');
			
		if(saveType == 'new'){
			$('#GX_XNXQBM').val('');//主键
		}else{
			$('#GX_XNXQBM').val(iKeyCode);//主键
		}
		$("#form1").submit();
	}
	
	/**清空Dialog中表单元素数据
	 * editTime:2015-05-28
	 * editUser:wangzh
	 * description:初始化每周天数,上午节数,下午节数,晚上节数
	 */
	function emptyDialog(){
		$('#GX_XN').textbox('setValue', '');
		$('#GX_XQ').combobox('setValue', '');
		$('#GX_JXXZ').combobox('setValue', '');
		$('#GX_XQKSSJ').datebox('setValue', '');
		$('#GX_XQJSSJ').datebox('setValue', '');
		$('#GX_PKJZSJ').datebox('setValue', '');
		$('#GX_MZTS').combobox('setValue', '1');
		$('#GX_SWJS').combobox('setValue', '0');
		$('#GX_ZWJS').combobox('setValue', '0');
		$('#GX_XWJS').combobox('setValue', '0');
		$('#GX_WSJS').combobox('setValue', '0');
	}
	
	//判断浏览器类型
	function getOs(){  
			   
		if(navigator.userAgent.indexOf("MSIE")>0) {  
			return "MSIE";  
		}  
		if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
			return "Firefox";  
		}  
		if(isSafari=navigator.userAgent.indexOf("Safari")>0) {  
			return "Safari";  
		}   
		if(isCamino=navigator.userAgent.indexOf("Camino")>0){  
			return "Camino";  
		}  
		if(isMozilla=navigator.userAgent.indexOf("Gecko/")>0){  
			return "Gecko";  
		}  
			    
	}
	
	/**读取校历详情
		@startDate 学期开始日期
		@endDate 学期结束日期
		@holiday 节假日
	**/
	function loadSchoolCalendar(startDate, endDate, holiday){
		$('#xqxlInfo').dialog('open');
		tempHolidayArray = holiday.split(',');
		var weekly = 1;
		var count = 0;
		var startTime = new Date(startDate.replace(/-/g, "/"));
		var endTime = new Date(endDate.replace(/-/g, "/"));
		var tempTime = startTime;
		var content = '';
		
		//获取开始日期是星期几
		count =  new Date(startDate.replace(/-/g, "/")).getDay();
		
		//判断如果开始日期不是星期日，补足空td
		if(count != 0){
			content += '<tr style="height:40px;"><td class="titleFont">' + (weekly<10?'0'+weekly:weekly) + '</td>';
			for(var i=0; i<count; i++){
				content += '<td class="tdWidth">&nbsp</td>';
			}
		}
		
		//循环生成所有日期
		var day = '';
		var month = '';
		var year = '';
		var temp = '';
		while(tempTime <= endTime){
			//获取日期
			day = tempTime.getDate();
			if(day < 10){
				day = '0'+day;
			}
			month = tempTime.getMonth()+1;
			if(month < 10){
				month = '0'+month;
			}
			year = tempTime.getFullYear();
			temp = year+'-'+month+'-'+day;
			
			if(count == 0){
				content += '<tr style="height:40px;"><td class="titleFont">' + (weekly<10?'0'+weekly:weekly) + '</td>';
			}
			
			//检查是否节假日
			var datecolor="black";
			for(var i=0; i<tempHolidayArray.length; i++){
				if(tempHolidayArray[i] == temp){
					datecolor="red";		
					break;
				}
			}
			
			if(datecolor=="red"){
				content += '<td id="date_' + weekly+'_'+count + '" class="tdWidth" title="'+temp+'" name="'+temp+'" style="cursor:pointer;color:red;" onclick="changeDateState(this.id,\''+temp+'\');" onmouseover="$(this).css(\'background\',\'#FFE48D\');" onmouseout="$(this).css(\'background\',\'white\');"';
			}else{
				content += '<td id="date_' + weekly+'_'+count + '" class="tdWidth" title="'+temp+'" name="'+temp+'" style="cursor:pointer;" onclick="changeDateState(this.id,\''+temp+'\');" onmouseover="$(this).css(\'background\',\'#FFE48D\');" onmouseout="$(this).css(\'background\',\'white\');"';
			}
			
			//判断显示的日期格式
			if(parseInt(day) == 1){
				temp = month+'-'+day;
				if(parseInt(month) == 1){
					temp = year+'-'+month+'-'+day;
				}
			}else{
				temp = day;
			}
			content += '>' + temp + '</td>';
			
			if(count == 6){
				content += '</tr>';
			}
			tempTime = tempTime.getTime()+1000*60*60*24;
			tempTime = new Date(tempTime);
			count++;
			
			if(count == 7){
				weekly++;
				count = 0;
			}
		}
		
		//如果结束日期不是星期六,补足空td至星期六
		if(count != 0){
			while(count < 7){
				count++;
				content += '<td>&nbsp;</td>';
			}
			content += '</tr>';
		}
		
		$('#xlDetail').html(content);
	}
	
	/**修改日期为节假日
		@id 单元格id
		@curDate 选中日期
	**/
	function changeDateState(id, curDate){
		$('#'+id).css('color', 'red');
		var flag = false;
		
		//检查是否已经是节假日
		for(var i=0; i<tempHolidayArray.length; i++){
			if(tempHolidayArray[i] == curDate){
				tempHolidayArray.splice(i, 1);
				flag = true;
				break;
			}
		}
		
		//判断选中或取消
		if(flag){
			$('#'+id).css('color', 'black');
		}else{
			tempHolidayArray.push(curDate);
			$('#'+id).css('color', 'red');
		}
	}
</script>
</html>