<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	/**
		创建人：wangzh
		Create date: 2015.06.05
		功能说明：用于设置班级固排禁排
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
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css">
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	</head>
	<style>
		#xlTime{
			width:100%;
			border-right:1px solid #99bbe8;
		}
		#xlTime{
			width:99.9%;
			border-top:1px solid #99bbe8;
		}
		#xlTime td{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
			font-size:11;
		}
		#xlTime th{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
		}
		
		#xlTime9{
			width:100%;
			border-right:1px solid #99bbe8;
		}
		#xlTime9{
			width:99.9%;
			border-top:1px solid #99bbe8;
		}
		#xlTime9 td{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
			font-size:11;
		}
		#xlTime9 th{
			width:300px;
			border-left:1px solid #99bbe8;
			border-bottom:1px solid #99bbe8;
			text-align:center;
			empty-cells:show;
		}
		#divPageMask2{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
		#divPageMask3{background-color:#D2E0F2; filter:alpha(opacity=50);left:0px;top:0px;z-index:100;}
		
	</style>
	<body class="easyui-layout">
		<%-- 遮罩层 --%>
    	<div id="divPageMask2" style="position:absolute;top:0px;left:0px;width:100%;height:100%;">
    	</div>
    	
		<div data-options="region:'center'" title="" style = "height:130px;width:100%;">
			<table  class = "tablestyle" width = "100%">
				<tr>
					<td>
						<a href="#" onclick="doToolbar(this.id);" id="newks"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-new'">新建</a>
						<a href="#" onclick="doToolbar(this.id);" id="edit"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">编辑</a>
						<a href="#" onclick="doToolbar(this.id);" id="export" class="easyui-linkbutton" data-options="iconCls:'icon-submit',plain:true">导出</a>
					</td>
				</tr>
			</table>
			<table id='list' style="width:100%;height:20%;">
			
			</table>
		</div>
		
		
		<!-- 场次安排 -->
		<div id="ic_ksccap" style="width:786px;height:91px;float:left;" >
			<div style="width:100%;height:464px;">
				<div style="border-left:1px solid #95B8E7;">
				<table >
					<tr>
						<td ><a href="#" id="addselect" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="doToolbar(this.id);" title="">添加</a></td>
						<td ><a href="#" id="delselect" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="doToolbar(this.id);" title="">删除</a></td>
					</tr>
				</table>
				</div>
				<div style="width:100%;height:432px;">
					<table id="courseList3" style="width:100%;">2</table>
				</div>
			</div>
		</div>
		
		<div id="ic_ksccapedit" style="width:786px;height:91px;float:left;" >
			<div style="width:100%;height:400px;">
				<div>
				<table>
					<tr>
						<td><a href="#" id="saveselect" class="easyui-linkbutton" plain="true" iconcls="icon-save" onClick="doToolbar(this.id);" title="">保存</a></td>
					</tr>
				</table>
				<table id="ee" singleselect="true" width="100%" class="tablestyle">
					<tr>	
						<td style="width:80px;" class="titlestyle">班级名称</td>
						<td>
							<input name="BJMC_CX" id="BJMC_CX" class="easyui-textbox" style="width:120px;"/>
						</td>
						<td style="width:80px;" class="titlestyle">课程名称</td>
						<td>
							<input name="KCMC_CX" id="KCMC_CX" class="easyui-textbox" style="width:120px;"/>
						</td>
						<td style="width:80px;" class="titlestyle">所属专业</td>
						<td>
							<select name="ZYDM_CX" id="ZYDM_CX" class="easyui-combobox" style="width:120px;">
							</select>
						</td>
						<td colspan=2 style="width:80px;" align="center">
							<a href="#" onclick="doToolbar(this.id)" id="query" class="easyui-linkbutton" plain="true" iconcls="icon-search">查询</a>
						</td>				
					</tr>
			    </table>
			    </div>
			    <div style="width:100%;height:402px;">
					<table id="courseList2" style="width:100%;">1</table>
				</div>
			</div>
		</div>
		
		<!-- 参考学生 -->
		<div id="ic_cjksxs" style="width:786px;height:91px;float:left;" >
			<div style="width:100%;height:464px;">
				<div style="border-left:1px solid #95B8E7;">
				<table >
					<tr>
						<td ><a href="#" id="addselect" class="easyui-linkbutton" plain="true" iconcls="icon-add" onClick="" title="">添加</a></td>
						<td ><a href="#" id="delselect" class="easyui-linkbutton" plain="true" iconcls="icon-cancel" onClick="" title="">删除</a></td>
					</tr>
				</table>
				</div>
				<div style="width:100%;height:432px;">
					<table id="courseList5" style="width:100%;">5</table>
				</div>
			</div>
		</div>
		
		<!-- 编辑考试信息 -->
		<div id="win" title="编辑">
			<form id='fm' method='post' style="margin: 0px">
				<table class = "tablestyle" width="100%">
					<tr>
						<a href="#" class="easyui-linkbutton" id="savesfks" name="savesfks" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
					</tr>
					
					<tr>
						<td class="titlestyle" id="GS_KCLXN">是否考试</td>
						<td id="CH_KCLX">
							<select name="GS_SFKS" id="GS_SFKS" class="easyui-combobox" panelHeight="auto" style="width:250px;">
								<option value="1">是</option>
								<option value="2">否</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >考试形式</td>
						<td onclick="">
							<select name="GS_KSXS" id="GS_KSXS" class="easyui-combobox" style="width:250px;" panelHeight="auto">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >场地类型</td>
						<td onclick="">
							<select name="GS_CDLX" id="GS_CDLX" class="easyui-combobox" style="width:250px;" panelHeight="auto">
							</select>
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >场地要求</td>
						<td onclick="openroom();">
							<input name="GS_CDYQ" id="GS_CDYQ" class="easyui-textbox" style="width:250px;"  />
						</td>
					</tr>
					<tr>
						<td class="titlestyle" >试卷类型</td>
						<td onclick="">
							<select name="GS_SJLX" id="GS_SJLX" class="easyui-combobox" style="width:250px;" panelHeight="auto">
							</select>
						</td>
					</tr>
					
				</table>
				
				<!-- 隐藏属性,传参用 -->
				<input type="hidden" id="active" name="active" />
				<input type="hidden" id="GS_SKJHMXBH" name="GS_SKJHMXBH" /><!-- 授课计划明细编号 -->
				<input type="hidden" id="GS_XZBDM" name="GS_XZBDM" />
				<input type="hidden" id="GS_XNXQBM" name="GS_XNXQBM" />
				<input type="hidden" id="GS_KCDM" name="GS_KCDM" />
				<input type="hidden" id="GS_KCMCDM" name="GS_KCMCDM" />
				<input type="hidden" id="GS_SKJSBH" name="GS_SKJSBH" /><!-- 授课教师编号 -->
				<input type="hidden" id="GS_CDMC" name="GS_CDMC" /><!-- 场地名称（存的场地要求）；GS_CDYQ存的场地名称 -->
				<input type="hidden" id="GS_SKZCXQ" name="GS_SKZCXQ" /><!-- 授课周次详情（存的授课周次）；GS_SKZC存的授课周次详情 -->
				<input type="hidden" id="iUSERCODE" name="iUSERCODE" />
			</form>
		</div>
		
		<!-- 新建考试信息 -->
		<div id="newExam" title="新建">
			
				<table class = "tablestyle" width="100%">
					<tr>
						<a href="#" class="easyui-linkbutton" id="saveNewExam" name="saveNewExam" iconCls="icon-save" plain="true" onClick="doToolbar(this.id)">保存</a>
					</tr>					
					<tr>
						<td class="titlestyle">学科名称</td>
						<td id="CH_KCDM">
							<select name="GS_KCMC" id="GS_KCMC" class="easyui-combobox" panelHeight="auto" style="width:250px;" >
							</select>
						</td>
					</tr>
					
				</table>
							
		</div>
		
		<div id="room" title="编辑">
			<div >
				<table id="roomtable" class = "tablestyle" width="566px" >
					<tr>
						<a href="#" class="easyui-linkbutton" id="submit5" name="submit5" iconCls="icon-submit" plain="true" onClick="doToolbar(this.id)">确定</a>
					</tr>
					<tr>
						<td width="25%" align="center"><input type="checkbox" id="choosetype" name="" onclick="chooseType();" /> 只选类型</td>
						<td width="25%"></td><td width="25%"></td><td width="25%"></td>
					</tr>
					<tr>
						<td width="25%" align="center">普通教室</td>
						<td width="25%" align="center"><input class="easyui-numberbox" id="normalroom" name="" /></td>
						<td width="25%" align="center">多媒体教室</td>
						<td width="25%" align="center"><input class="easyui-numberbox" id="meidaroom" name="" /></td>
					</tr>
					<tr>
						<td width="25%" align="center"><input type="checkbox" id="chooseroom" name="" onclick="chooseRoom();" /> 指定教室</td>
						<td width="25%"></td><td width="25%"></td><td width="25%"></td>
					</tr>
					<tr>
						<td width="25%" align="center">校区</td>
						<td width="25%" align="center"><select id="school" name="" class="easyui-combobox" style="width:90%" panelHeight="auto"></select></td>
						<td width="25%" align="center">教学楼</td>
						<td width="25%" align="center"><select id="house" name="" class="easyui-combobox" style="width:90%" panelHeight="auto"></select></td>
					</tr>
					<tr>
						<td width="25%" align="center">教室类型</td>
						<td width="25%" align="center"><select id="clstype" name="clstype" class="easyui-combobox" style="width:90%" panelHeight="auto"></select></td>
						
					</tr>
				</table>
			</div>
			<div >
					
				<form id='fm5' method='post' style="margin: 0px;">
					
					<table id="clstable" class = "tablestyle" width="686px">
										
					</table>
				</form>
			</div>
		</div>
		
		<!-- 考试课程导出页面 -->
		<div id="exportTimetable">
			<!--引入编辑页面用Ifram-->
			<iframe id="exportTimetableiframe" name="exportTimetableiframe" src='' style='width:100%; height:100%;' frameborder="0" scrolling="no"></iframe>
		</div>
		
	</body>
	<script type="text/javascript">
		var iKeyCode = "";//授课计划明细编号
		var pageNum = 1;   //datagrid初始当前页数
		var pageSize = 20; //datagrid初始页内行数
		//var xkmc = "";//学科名称下拉框数据
		//var rkjs = "";//任课教师下拉框数据
		var kcmc = "";//课程名称
		var jsxm = "";//教师姓名
		var skzcxq = "";//授课周次详情
		var cdyq = "";//场地要求
		var cdmc = "";//场地名称
		var zjs = "";//总节数
		var gjs = "";//固排已排节数
		var lj = "";//连节
		var lc = "";//连次
		var lcs= "";//已排连次次数
		var aod= "";//判断添加或删除
		var checkrec="";
		var delinfo = "";
		var classId1 = window.parent.document.frames["left"].classId;
		var xnxqVal1 = window.parent.document.frames["left"].xnxqVal;
		var jxxzVal1 = window.parent.document.frames["left"].jxxzVal;
		var weeks1="";
		var lastIndex=-1;//保存上次选中的授课计划
		var classnum="";//班级编号
		var savexqbh1="";//保存传递过来的学期编码
		var savexqbh2="";//保存传递过来的学期编码
		var qzqm="";//期中期末
		var paike="0";//排课状态
		var rowid="";//所选行checkbox的id
		var rownum="";//所选行编号
		var roomsel=0;//选择教室模式
		var clsidarray =new Array();//存放场地编号
		var clsarray =new Array();//存放场地
		var clsinfoidarray =new Array();//存放选择的场地编号
		var clsinfoarray =new Array();//存放选择的场地名称
		//var saveType = "";//判断打开窗口的操作（new or edit）

		var iKeyCode = ''; //定义主键
		var NJDM_CX = '';//查询条件
		var ZYDM_CX = '';
		var KCDM_CX = '';
		var KCMC_CX = '';
		var BJMC_CX = '';
		var saveType = '';
		var examinfoidarray =new Array();//存放选择的考试场次
		var delexaminfoidarray =new Array();//存放选择的考试场次
		var examid="";//考试编号
		
		var cdlx=""; //场地类型
		var ksbh=""; //考试形式编号
		var ksxs=""; //考试形式
		
		$(document).ready(function(){
			checkksgl();
			initDialog();//初始化dialog
			initGridData(classId1,xnxqVal1,jxxzVal1,"1","1");//页面初始化加载数据
			initData();
			roomtypeCombobox();
			papertypeCombobox();
			
		});
		
		//检查考试管理表，与授课计划同步
		function checkksgl(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=checkksgl',
				dataType:"json",
				success : function(data) {
			
				}
			});
		}
		
		//页面初始化加载数据
		function initGridData(classId,xnxqVal,jxxzVal,qzqmVal,n){
			classnum=classId;
			savexqbh1=xnxqVal;
			savexqbh2=jxxzVal;
			qzqm=qzqmVal;
			checkpaike();
	
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_examSet',
				data : 'active=initGridData&page=' + pageNum + '&rows=' + pageSize + '&XZBDM=' + classId + '&XNXQ=' + xnxqVal + '&JXXZ=' + jxxzVal + '&QZQM=' + qzqmVal,
				dataType:"json",
				success : function(data) {
					//xkmc = data[0].xkmcData;//获取学科名称下拉框数据
					//rkjs = data[0].rkjsData;//获取任课教师下拉框数据					

					loadGrid(data[0].listData);//加载特殊规则列表
					//initCombobox(xkmc,rkjs);//初始化下拉框

				}
			});
		}
		
		/**页面初始化加载数据**/
		function initData(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=initData&page=' + pageNum + '&rows=' + pageSize,
				dataType:"json",
				success : function(data) { 
					initCombobox(data[0].kclxData, data[0].zydmData, data[0].ksxsData);
				}
			});
		}
		
		//检查当前时间是否超过允许排课时间
		function checkpaike(){ 
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_Skjh',
				data : 'active=checkpaike&GS_XNXQBM=' + (savexqbh1+savexqbh2),
				async : false,
				dataType:"json",
				success : function(data) {
					paike=data[0].MSG;
				}
			});
		}
		
		var editIndex = undefined;
		function loadGrid(listData){
			$('#list').datagrid({
				data:listData,
				loadMsg : "信息加载中请稍后!",//载入时信息
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:true,
				fitColumns: true,//设置边距
				columns:[[
					{field:'是否考试',title:'是否考试',width:50,align:'center',
						formatter:function(value,rec,row){
							//var sfksbox='<input type="checkbox" id="sfks'+rec.授课计划明细编号+'" name="ksjh" onclick="editExamInfo(this.id,'+row+');" />';
							var sfksbox="";
							if(rec.是否考试=='1'){
								sfksbox="是";
							}else{
								sfksbox="否";
							}
							return sfksbox;
						}
					},
					{field:'课程名称',title:'科目名称',width:140},
					{field:'考试形式',title:'考试形式',width:60},
					{field:'试卷类型',title:'试卷类型',width:60},
					{field:'名称',title:'场地类型',width:80},
					{field:'场地要求',title:'场地要求',width:80},
					{field:'上课周期',title:'上课周期',width:60},
					{field:'考试场次安排',title:'考试场次',width:60,align:'center',
						formatter:function(value,rec){
							var ksccap="";
							if(rec.是否考试=='1'){
								ksccap='<input id="ksap'+rec.授课计划明细编号+'" type="button" value="[设置]" style="cursor:pointer;display:block;" onclick="openksap(this.id);" />';
							}else{
								ksccap='';							
							}
							return ksccap;
						}
					},
					{field:'参加考试学生',title:'参考学生',width:80,align:'center',
						formatter:function(value,rec){
							var ksccap="";
							if(rec.是否考试=='1'){
								ksccap='<input id="ckxs'+rec.授课计划明细编号+'" type="button" value="[参考学生]" style="cursor:pointer;display:block;" onclick="openckxs(this.id);" />';
							}else{
								ksccap='';							
							}
							return ksccap;
						}
					}
// 					{field:'GS_SKZCXQ',title:'授课周次详情',width:50,
// 						formatter:function(value,rec){
// 							var skzcxq2=rec.GS_SKZCXQ;
// 							skzcxq2=skzcxq2.replace(new RegExp('odd','gm'),'单周');
// 							skzcxq2=skzcxq2.replace(new RegExp('even','gm'),'双周');
// 							return skzcxq2;
// 						}
// 					}
				]],
				onClickRow:function(rowIndex, rowData){
					//$('#list').datagrid('unselectRow',rowIndex);
					
					iKeyCode = rowData.授课计划明细编号;
					cdlx=rowData.场地类型;
					cdyq=rowData.场地要求;
					ksbh=rowData.编号;
	
					//qzqm=rowData.期中期末;
// 					kcmc = rowData.课程名称;
// 					jsxm = rowData.授课教师姓名;
// 					skzcxq = rowData.GS_SKZCXQ;
// 					lj = rowData.GS_LJ;
// 					lc = rowData.GS_LC;
// 					cdyq=rowData.GS_CDYQ;
// 					cdmc=rowData.GS_CDMC;
// 					classId1 = window.parent.document.frames["left"].classId;
// 					xnxqVal1 = window.parent.document.frames["left"].xnxqVal;
// 					jxxzVal1 = window.parent.document.frames["left"].jxxzVal;
// 					weeks1 = window.parent.document.frames["left"].weeks;
	
				},
				onClickCell:function(index, field){
					//alert(index+"|"+field);
				},
				onLoadSuccess: function(data){
					//parentId=window.parent.document.frames["left"].parentId;
					//if(parentId==""){
					//	$('#new').linkbutton('disable');
					//	$('#edit').linkbutton('disable');
					//}else{
					//	$('#new').linkbutton('enable');
					//	$('#edit').linkbutton('enable');
					//}
					//iKeyCode="";
							
					
					if(data.total == 0){
						$('#save').linkbutton('disable');
					}else{
						$('#save').linkbutton('enable');
					}
					setTimeout("$('#divPageMask2').hide()",1000);
					setTimeout("$('#divPageMask3').hide()",1000);
				},
				onLoadError:function(none){
					
				}
				//onClickCell:onClickCell
			});
			
			$("#list").datagrid("getPager").pagination({ 
				total:listData.total, 
				onSelectPage:function (pageNo, pageSize_1) { 
					pageNum = pageNo;
					pageSize = pageSize_1;
					loadData();
				} 
			});
		}
		
		function editExamInfo(id,row){
			rowid=id;
			rownum=row;
			var checkbox = document.getElementById(id);
			var currentBtn = document.getElementById("ksap"+id.substring(4,id.length));
			var editkszq="";
			if(checkbox.checked){//勾选
				currentBtn.style.display = "block"; //style中的display属性
				//$('#list').datagrid('beginEdit', rownum);
				//editkszq = $('#list').datagrid('getEditor', { index: rownum, field: '考试周期' }); 
			}else{
				currentBtn.style.display = "none";
				//editkszq = $('#list').datagrid('getEditor', { index: rownum, field: '考试周期' }); 
				//$(editkszq.target).val("");
				//$('#list').datagrid('endEdit', rownum); 
			}	
			
		    
		}
		
		function openksap(id){ 
			examid=id;
			$('#ic_ksccap').show();
			$('#ic_ksccap').dialog("open");	
			examinfoidarray.splice(0, examinfoidarray.length);
			loadGrid3();
		}
		
		function openckxs(id){ 
			examid=id;
			$('#ic_cjksxs').show();
			$('#ic_cjksxs').dialog("open");	
			loadGrid5();
		}
		
		//工具按钮
		function doToolbar(id){
			if(id == "savesfks"){
				//alert(classnum+"|"+savexqbh1+"|"+savexqbh2+"|"+qzqm+"|");
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=savesfks&GG_SKJHMXBH=' + iKeyCode + '&GG_SFKS='+$('#GS_SFKS').combobox('getValue')+ '&GG_CDLX='+$('#GS_CDLX').combobox('getValue') + '&GG_CDYQ='+$('#GS_CDYQ').textbox('getValue') + '&GG_SJLX='+$('#GS_SJLX').combobox('getValue') + '&GG_KSXS='+$('#GS_KSXS').combobox('getValue') + '&GG_XNXQBM='+(savexqbh1+savexqbh2)+'&QZQM='+qzqm,
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						$('#win').dialog("close");
						initGridData(classnum,savexqbh1,savexqbh2,qzqm,"1");	
					}
				});
			}
			if(id == "export"){//导出
				$('#exportTimetable').dialog('open');
				var titleinfo=savexqbh1+","+savexqbh2+","+qzqm;
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportExamCourse.jsp?exportType=examCourse&xnxq=" + savexqbh1 + "&jxxz=" + savexqbh2 + "&qzqm=" + qzqm +"&titleinfo="+encodeURI(encodeURI(titleinfo)) );
			}
			if(id == "resit"){//补考
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=resitInfo&XNXQ=' + savexqbh1 + '&JXXZ=' + savexqbh2 + '&QZQM=' + qzqm,
					dataType:"json",
					success : function(data) {
						alertMsg(data[0].MSG);
					}
				});
			}
			if(id == "exportResit"){//导出补考名册
				$('#exportTimetable').dialog('open');
				$("#exportTimetableiframe").attr("src","<%=request.getContextPath()%>/form/timetableQuery/exportResit.jsp?xnxq=" + savexqbh1 + "&jxxz=" + savexqbh2 + "&qzqm=" + qzqm );
			}
			
			if(id == "submit5"){
				var roms="";
				var html="";
				if(roomsel==1){//只选类型	
					if($('#normalroom').val()+$('#meidaroom').val()<1){
						alertMsg("教室数量至少为1");
						return;
					}	
					if(!$('#normalroom').val()==""){	
						for(var j=0;j<$('#normalroom').val();j++){
							if(j==0){
								html+="普通教室";
								roms+="5";
							}else{
								html+="+普通教室";
								roms+="+5";
							}
						}
					}
					if(!$('#meidaroom').val()==""){	
						if($('#normalroom').val()==""||$('#normalroom').val()==0||$('#meidaroom').val()==0){
							html+="";
							roms+="";
						}else{
							html+="+";
							roms+="+";
						}
						for(var j=0;j<$('#meidaroom').val();j++){
							if(j==0){
								html+="多媒体教室";
								roms+="1";
							}else{
								html+="+多媒体教室";
								roms+="+1";
							}
						}					
					}

				}else if(roomsel==2){//指定教室
					//var rObj = document.getElementsByName("roomall");		
					var roms="";
					var html="";
					for (var i = 0;i < clsinfoidarray.length;i++) {
						if(html==""){
							roms+=clsinfoidarray[i];
							html+=clsinfoarray[i];
						}else{
							roms+="+"+clsinfoidarray[i];
							html+="+"+clsinfoarray[i];
						}
					}
					
				}else{
				
				}
				
				$('#GS_CDYQ').textbox('setValue',html);

				$('#room').dialog("close");
			}
			//查询
			if(id == 'query'){
				BJMC_CX = $('#BJMC_CX').textbox('getValue'); 
				KCMC_CX = $('#KCMC_CX').textbox('getValue');
				ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
				loadGrid2(BJMC_CX,KCMC_CX,ZYDM_CX);
			}
			//执行保存操作
			if(id == 'saveselect'){ 
				var skid="";	
				var delexamid="";
				for (var i = 0;i < examinfoidarray.length;i++) {
					skid+=examinfoidarray[i]+",";					
				}
				for (var i = 0;i < delexaminfoidarray.length;i++) {
					delexamid+=delexaminfoidarray[i]+",";					
				}
				
				skid=skid.substring(0,skid.length-1);
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=saveselect&skid='+skid+'&examid='+examid+'&delexamid='+delexamid,
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						if(data[0].MSG=="保存成功"){
							emptyic_ksccapedit();	
							$('#ic_ksccapedit').dialog("close");
						}
					}
				});
			}
			if(id == 'edit'){
				if(iKeyCode==""){
					alertMsg("请选择一行数据");
					return;
				}else{
					$('#GS_CDLX').combobox('setValue',cdlx);
					$('#GS_CDYQ').textbox('setValue',cdyq);
					$('#GS_KSXS').combobox('setValue',ksbh);
					$('#win').dialog({   
						title: '编辑是否考试与场地要求'
					});
					$('#win').dialog("open");
					
				}
			}
			if(id == "newks"){//新建考试
			
				$('#newExam').dialog({   
						title: '新建考试'
				});
				$('#newExam').dialog("open");
				KCMCCombobox();
			}
			if(id == "saveNewExam"){//保存新建考试
				if(classnum==""){
					alertMsg("请选择班级或选修课");
					return;
				}
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=saveNewExam&GG_XZBDM='+classnum+'&XNXQ='+savexqbh1+'&JXXZ='+savexqbh2+'&QZQM='+qzqm+'&KCDM='+$('#GS_KCMC').combobox('getValue')+'&KCMC='+$('#GS_KCMC').combobox('getText'),
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						$('#newExam').dialog("close");
						initGridData(classnum,savexqbh1,savexqbh2,qzqm,"1");
					}
				});	
			}
			//添加
			if(id == 'addselect'){
				$('#ic_ksccapedit').show();
				$('#ic_ksccapedit').dialog("open");
				BJMC_CX = $('#BJMC_CX').textbox('getValue'); 
				KCMC_CX = $('#KCMC_CX').textbox('getValue');
				ZYDM_CX = $('#ZYDM_CX').combobox('getValue');
				loadGrid2(BJMC_CX,KCMC_CX,ZYDM_CX);
			}
			//删除
			if(id == 'delselect'){
				var skjhid="";
				var rObj=document.getElementsByName("ksjh3");
				for (var j=0;j < rObj.length;j++) {
					var checkbox = document.getElementById(rObj[j].id);
					if(checkbox.checked){
						skjhid+="'"+rObj[j].id.substring(4,rObj[j].id.length)+"',";
					}
				}
				skjhid=skjhid.substring(0, skjhid.length-1);
				$.ajax({
					type : "POST",
					url : '<%=request.getContextPath()%>/Svl_examSet',
					data : 'active=delselect&skjhid='+skjhid,
					dataType:"json",
					success : function(data) {
						showMsg(data[0].MSG);
						loadGrid3();
					}
				});		
			}
		}
		
		
		
		function initDialog(){
						
			$('#ic_ksccap').dialog({   
				title: '设置同时开考科目',   
				width: 800,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					
				}
			});
			
			$('#ic_ksccapedit').dialog({   
				title: '添加考试场次安排',   
				width: 800,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					emptyic_ksccapedit();
				}
			});
			
			$('#ic_cjksxs').dialog({   
				title: '参加考试学生',   
				width: 800,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){
					
				},
				//关闭事件
				onClose:function(data){
					
				}
			});
			
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
			
			$('#win').dialog({
				width : 350,
				height: 189,
				modal : true,
				closed : true,
				onOpen : function(data) {
					$('#win').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					emptyDialog();//初始化信息
					saveType = '';
				}
			});
			
			$('#newExam').dialog({
				width : 350,
				height: 89,
				modal : true,
				closed : true,
				onOpen : function(data) {
					$('#newExam').find('table').css('display', 'block');
				},
				onLoad : function(data) {
				},
				onClose : function(data) {
					//emptyDialog();//初始化信息
					//saveType = '';
				}
			});
			
			$('#room').dialog({   
				title: '场地要求',   
				width: 580,//宽度设置   
				height: 500,//高度设置 
				modal:true,
				closed: true,   
				cache: false, 
				draggable:true,//是否可移动dialog框设置
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					//clsinfoidarray.splice(0,teainfoidarray.length);
					//clsinfoarray.splice(0,teainfoarray.length);
				}
			});
			
			$('#exportTimetable').dialog({
				title:'导出预览',
				maximized:true,
				modal:true,
				closed: true,   
				cache: false, 
				draggable:false,//是否可移动dialog框设置
				//打开事件
				onOpen:function(data){},
				//读取事件
				onLoad:function(data){},
				//关闭事件
				onClose:function(data){
					$("#exportTimetableiframe").attr('src', '');
				}
			});
		}
		
		function emptyic_ksccapedit(){ 
			examinfoidarray.splice(0,examinfoidarray.length);//清空examinfoidarray
			delexaminfoidarray.splice(0,delexaminfoidarray.length);//清空delexaminfoidarray	
			$('#BJMC_CX').textbox('setValue','');
			$('#KCMC_CX').textbox('setValue','');
			$('#ZYDM_CX').combobox('setValue','');
			var rObj = document.getElementsByName("ksjh2");
			for (var i = 0;i < rObj.length;i ++) {
				$('#'+rObj[i].id).removeAttr("checked");				
			}
			loadGrid3();
		}
		
		//打开room编辑窗口
		function openroom(){

			document.getElementById("win").focus(); 
			
			//初始化
			$('#chooseroom').attr("checked",false);
			$('#choosetype').attr("checked",false);
			$('#normalroom').numberbox('setValue','');
			$('#meidaroom').numberbox('setValue','');
			$('#normalroom').numberbox({ disabled: true });
			$('#meidaroom').numberbox({ disabled: true });
			$('#school').combobox('setValue','');
			$('#house').combobox('setValue','');
			$('#clstype').combobox('setValue','');
			
			loadGridCls();
            schoolCombobox();
			houseCombobox();
			classtypeCombobox();
				
					//填充数据
// 					if($('#'+inputid).val()!=""&&clsidarray[(inputid.substring(7,inputid.length)-1)]!=""){
// 						var clsid=clsidarray[(inputid.substring(7,inputid.length)-1)].split("+");
// 						var clsna=clsarray[(inputid.substring(7,inputid.length)-1)].split("+");
// 						for(var i=0;i<clsid.length;i++){
// 							clsinfoidarray.push(clsid[i]);
// 							clsinfoarray.push(clsna[i]);				
// 						}
// 				 	}
					
					//alert("clsarray[i]:"+clsidarray[inputid.substring(7,inputid.length)-1]);
// 					if($('#'+inputid).val()!=""){
// 						var clsidname=clsidarray[(inputid.substring(7,inputid.length)-1)];
// 						if(clsidname.indexOf("+")>-1){
// 							var clsid=clsidname.split("+");
// 							if(clsid[0]=="5"||clsid[0]=="1"){//只选类型
// 								$('#choosetype').attr("checked","checked");
// 								chooseType();							
// 								var normalrooms=0;
// 								var mediarooms=0;
// 								for(var j=0;j<clsid.length;j++){
// 									if(clsid[j]=="5"){
// 										normalrooms++;
// 									}else{
// 										mediarooms++;
// 									}
// 								}
// 								$('#normalroom').numberbox('setValue',normalrooms);
// 								$('#meidaroom').numberbox('setValue',mediarooms);
// 							}else{//指定教室
// 								$('#chooseroom').attr("checked","checked");
// 								chooseRoom();
// 								var rObj=document.getElementsByName("roomall");
// 								for(var i=0;i<clsid.length;i++){
// 									var rObj=document.getElementsByName("roomall");
// 									for (var j = 0;j < rObj.length;j++) {
// 										var rObjid=rObj[j].hidename.split("|");
// 										if(rObjid[0]==clsid[i]){
// 											$('#room'+j).attr("checked","checked");
// 										}
// 									}	
// 								}
// 							}
// 						}else{
// 							if(clsidname=="5"||clsidname=="1"){//只选类型
// 								$('#choosetype').attr("checked","checked");
// 								chooseType();							
// 								var normalrooms=0;
// 								var mediarooms=0;
// 								if(clsidname=="5"){
// 									normalrooms++;
// 								}else{
// 									mediarooms++;
// 								}
// 								$('#normalroom').numberbox('setValue',normalrooms);
// 								$('#meidaroom').numberbox('setValue',mediarooms);
// 							}else{//指定教室
// 								$('#chooseroom').attr("checked","checked");
// 								chooseRoom();
// 								var rObj=document.getElementsByName("roomall");
// 								for (var j = 0;j < rObj.length;j++) {
// 									var rObjid=rObj[j].hidename.split("|");
// 									if(rObjid[0]==clsidname){
// 										$('#room'+j).attr("checked","checked");
// 									}
// 								}	
// 							}
// 						}
//  					}else{//教室为空
//  						$('#school').combobox("disable");
// 						$('#house').combobox("disable");
// 						$('#clstype').combobox("disable");
// 						clsinfoidarray.splice(0,clsinfoidarray.length);
// 						clsinfoarray.splice(0,clsinfoarray.length);
//  					}		
			
			$('#room').show();
			
			$('#room').dialog("open");
		}
		
		function loadGridCls(){
			$('#clstable').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Skjh',
	 			queryParams: {"active":"openRoom","selschool":$('#school').combobox('getValue'),"selhouse":$('#house').combobox('getValue'),"seltype":$('#clstype').combobox('getValue')},
				loadMsg : "信息加载中请稍后!",//载入时信息
				width:566,
				height:311,
				rownumbers: true,
				animate:true,
				striped : true,//隔行变色
				pageSize : pageSize,//每页记录数
				singleSelect : true,//单选模式
				pageNumber : pageNum,//当前页码
				pagination:false,
				fit:false,
				fitColumns: true,//设置边距
				columns:[[
					{field:'1',title:'选择',width:30,align:'center',
						formatter:function(value,rec){
							var gh='<input type="checkbox" id="'+rec.教室编号+'" name="clscb" hidename="'+rec.教室名称+'" onclick="editClsInfo(this.id);" />';
							return gh;
						}
					},
					{field:'校区名称',title:'校区',width:80,align:'center'},
					{field:'建筑物名称',title:'教学楼',width:80,align:'center'},
					{field:'名称',title:'教室类型',width:80,align:'center'},
					{field:'教室名称',title:'教室',width:80,align:'center'}
				]],
				onClickRow:function(rowIndex, rowData){
					$('#clstable').datagrid("unselectRow", rowIndex);
				},
				onLoadSuccess: function(data){
					var rObj = document.getElementsByName("clscb");
					var checkbox = document.getElementById('chooseRoom');
					for (var i = 0;i < rObj.length;i ++) {
						if(checkbox.checked){
							$('#'+rObj[i].id).attr("disabled",false);
						}else{
							$('#'+rObj[i].id).attr("disabled",true);
						}					
					}
			   	
					if($('#'+inputid).val()!=""&&clsidarray[(inputid.substring(7,inputid.length)-1)]!=""){
						var clsid=clsidarray[(inputid.substring(7,inputid.length)-1)].split("+");
						for(var i=0;i<clsid.length;i++){
							$('#'+clsid[i]).attr("checked","checked");
						}
						for(var i=0;i<clsinfoidarray.length;i++){
							$('#'+clsinfoidarray[i]).attr("checked","checked");
						}
		 			}
				},
				onLoadError:function(none){
					
				}
			});
		}
		
		//保存选择的任课教师信息
		function editClsInfo(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				clsinfoidarray.push(checkbox.id);
				clsinfoarray.push(checkbox.hidename);
			}else{//不勾选
				for(var i=0;i<clsinfoidarray.length;i++){
					if(checkbox.id==clsinfoidarray[i]){
						clsinfoidarray.splice(i,1);
						clsinfoarray.splice(i,1);
					}
				}
			}
		}
		
		//只选类型，指定教室
		function chooseType(){
			var rObj = document.getElementsByName("clscb");
			for (var i = 0;i < rObj.length;i ++) {
				$('#'+rObj[i].id).attr("disabled",true);				
			}
			clsinfoidarray.splice(0,clsinfoidarray.length);
			clsinfoarray.splice(0,clsinfoarray.length);
			var checkbox = document.getElementById('choosetype');
			if(checkbox.checked){
				roomsel=1;
				$('#chooseroom').attr("checked",false);
				$('#normalroom').numberbox({ disabled: false });
				$('#meidaroom').numberbox({ disabled: false });
				$('#school').combobox("disable");
				$('#house').combobox("disable");
				$('#clstype').combobox("disable");
				for(var i=0;i<roomnum;i++){
					$('#room'+i).attr("disabled","disabled");
				}
				$('#rooms').attr("disabled","disabled");
			}else{
				roomsel=0;
				$('#normalroom').numberbox('setValue','');
				$('#meidaroom').numberbox('setValue','');
				$('#normalroom').numberbox({ disabled: true });
				$('#meidaroom').numberbox({ disabled: true });				
			}
		}
		//指定教室
		function chooseRoom(){
			var checkbox = document.getElementById('chooseroom');
			if(checkbox.checked){
				roomsel=2;
				$('#normalroom').numberbox('setValue','');
				$('#meidaroom').numberbox('setValue','');
				$('#normalroom').numberbox({ disabled: true });
				$('#meidaroom').numberbox({ disabled: true });
				$('#choosetype').attr("checked",false);
				$('#school').combobox("enable");
				$('#house').combobox("enable");
				$('#clstype').combobox("enable");
				var rObj = document.getElementsByName("clscb");
				for (var i = 0;i < rObj.length;i ++) {
					$('#'+rObj[i].id).attr("disabled",false);				
				}
			}else{
				roomsel=0;
				$('#school').combobox("disable");
				$('#house').combobox("disable");
				$('#clstype').combobox("disable");
				var rObj = document.getElementsByName("clscb");
				for (var i = 0;i < rObj.length;i ++) {
					$('#'+rObj[i].id).attr("disabled",true);				
				}
				clsinfoidarray.splice(0,clsinfoidarray.length);
				clsinfoarray.splice(0,clsinfoarray.length);
			}
		}
		
		//加载下拉框数据
		function schoolCombobox(){
			$('#school').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=schoolCombobox",
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
					if(roomsel!=2){
						$('#school').combobox("disable");
					}
							
				},
				//下拉列表值改变事件
				onChange:function(data){
					loadGridCls();
				}
			});
		}
		
		//加载下拉框数据
		function houseCombobox(){
			$('#house').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=houseCombobox",
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
					if(roomsel!=2){
						$('#house').combobox("disable");
					}
					
				},
				//下拉列表值改变事件
				onChange:function(data){ 
					loadGridCls();
				}
			});
		}
		
		//加载下拉框数据
		function classtypeCombobox(){
			$('#clstype').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=classtypeCombobox",
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
					if(roomsel!=2){
						$('#clstype').combobox("disable");
					}
							
				},
				//下拉列表值改变事件
				onChange:function(data){
					loadGridCls();
				}
			});
		}
		
		//加载下拉框数据
		function roomtypeCombobox(){
			$('#GS_CDLX').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=classtypeCombobox",
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
				onChange:function(data){
				
				}
			});
		}
		
		//加载下拉框数据
		function papertypeCombobox(){
			$('#GS_SJLX').combobox({
				url:"<%=request.getContextPath()%>/Svl_Skjh?active=papertypeCombobox",
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
				onChange:function(data){
			
				}
			});
		}
		
		function KCMCCombobox(){
			$('#GS_KCMC').combobox({
				url:"<%=request.getContextPath()%>/Svl_examSet?active=KCMCCombobox&GG_XZBDM=" + classnum+"&GG_XNXQBM=" + savexqbh1+savexqbh2,
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
				onChange:function(data){
						
				}
			});
		}
		
		/**加载combobox控件
			@jxxzData 下拉框数据
		**/
		function initCombobox(kclxData, zydmData, ksxsData){
			$('#KCLX').combobox({
				data:kclxData,
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
				onChange:function(data){
					if(saveType=='new'){
						if(data=="01"){
							$('#ZYDM').combobox('setValue', '');
							$('#ZYDM').combobox("disable");
						}else if(data=="02"){
							$('#ZYDM').combobox("enable");
						}else{
							$('#ZYDM').combobox("disable");
						}
					}
				}
			});
			
			$('#ZYDM_CX').combobox({
				data:zydmData,
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
	
			$('#ZYDM').combobox({
				data:zydmData,
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
			
			//加载下拉框数据
			$('#GS_KSXS').combobox({
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
				}
			});	
		}
		
			
		/**加载 datagrid控件，读取页面信息
			@listData 列表数据
		**/
		//添加删除合班考试
		function loadGrid2(BJMC_CX,KCMC_CX,ZYDM_CX){
			$('#courseList2').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listkskc&XNXQ='+savexqbh1+'&JXXZ='+savexqbh2+'&QZQM='+qzqm+'&BJMC_CX='+encodeURI(BJMC_CX)+'&KCMC_CX='+encodeURI(KCMC_CX)+'&ZYDM_CX='+ZYDM_CX+'&examid='+examid,
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
					{field:'sele',title:'',width:fillsize(0.2),align:'center',
						formatter:function(value,rec,row){
							var sfksbox='<input type="checkbox" id="'+rec.授课计划明细编号+'" name="ksjh2" onclick="editExamID(this.id);" />';
							return sfksbox;
						}
					},
					{field:'行政班名称',title:'班级名称',width:fillsize(0.2),align:'center'},
					{field:'课程名称',title:'课程名称',width:fillsize(0.2),align:'center'},
					{field:'专业名称',title:'所属专业',width:fillsize(0.2),align:'center'}
				]],
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){
				
				},
				//加载成功后触发
				onLoadSuccess: function(data){
					for(var i=0;i<examinfoidarray.length;i++){
						$('#'+examinfoidarray[i]).attr("checked","checked");
					} 			
				}
			});
			
			
		};
		
		//显示一起考试课程
		function loadGrid3(){
			$('#courseList3').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listtskc&XNXQ='+savexqbh1+'&JXXZ='+savexqbh2+'&QZQM='+qzqm+'&examid='+examid,
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
					{field:'sele',title:'',width:fillsize(0.1),align:'center',
						formatter:function(value,rec,row){
							var sfksbox='<input type="checkbox" id="sfks'+rec.授课计划明细编号+'" name="ksjh3" onclick="" />';
							if(rec.授课计划明细编号.substring(6,rec.授课计划明细编号.length)==rec.考试场次编号.substring(6,rec.考试场次编号.length)){
								sfksbox="";
							}
							examinfoidarray.push(rec.授课计划明细编号);							
							return sfksbox;
						}
					},
					{field:'考试场次编号',title:'考试场次编号',width:fillsize(0.2),align:'center'},
					{field:'行政班名称',title:'班级名称',width:fillsize(0.2),align:'center'},
					{field:'课程名称',title:'课程名称',width:fillsize(0.2),align:'center'},
					{field:'所属专业',title:'所属专业',width:fillsize(0.2),align:'center'}
				]],
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){
					
				},
				//加载成功后触发
				onLoadSuccess: function(data){
					
				}
			});
			
			
		};
		
		//显示参考学生
		function loadGrid5(){
			$('#courseList5').datagrid({
				url : '<%=request.getContextPath()%>/Svl_examSet?active=listckxs&XNXQ='+savexqbh1+'&JXXZ='+savexqbh2+'&QZQM='+qzqm+'&examid='+examid,
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
					{field:'sele',title:'',width:fillsize(0.1),align:'center',
						formatter:function(value,rec,row){
							var ksxxbox='<input type="checkbox" id="ksxx'+rec.授课计划明细编号+'" name="ksxx3" onclick="" />';
														
							return ksxxbox;
						}
					},
					{field:'行政班名称',title:'班级名称',width:fillsize(0.2),align:'center'},
					{field:'学号',title:'学号',width:fillsize(0.2),align:'center'},
					{field:'姓名',title:'姓名',width:fillsize(0.2),align:'center'}
				]],
				//双击某行时触发
				onDblClickRow:function(rowIndex,rowData){},
				//读取datagrid之前加载
				onBeforeLoad:function(){},
				//单击某行时触发
				onClickRow:function(rowIndex,rowData){
					
				},
				//加载成功后触发
				onLoadSuccess: function(data){
					
				}
			});
			
		};
		
		//保存选择的考试信息
		function editExamID(id){
			var checkbox = document.getElementById(id);
			if(checkbox.checked){//勾选
				examinfoidarray.push(checkbox.id);
				for(var i=0;i<delexaminfoidarray.length;i++){
					if(checkbox.id==delexaminfoidarray[i]){
						delexaminfoidarray.splice(i,1);
					}
				}	
			}else{//不勾选
				delexaminfoidarray.push(checkbox.id);
				for(var i=0;i<examinfoidarray.length;i++){
					if(checkbox.id==examinfoidarray[i]){
						examinfoidarray.splice(i,1);
					}
				}
			}
		}
		
		
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
					loadGrid2(data[0].listData);
				}
			});
		}
		
		
		
		//删除
		function delClass(){
			$.ajax({
				type : "POST",
				url : '<%=request.getContextPath()%>/Svl_CourseSet',
				data : 'active=del&KCDM=' + iKeyCode,
				dataType:"json",
				success : function(data){
					if(data[0].MSG == '删除成功'){
						showMsg(data[0].MSG);
						loadData();
					}else{
						alertMsg(data[0].MSG);
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
		
		
	</script>
</html>