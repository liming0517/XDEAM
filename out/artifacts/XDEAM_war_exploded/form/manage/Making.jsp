<%
/**
文件说明
Create date: 2011.3.21
说明：用于规章制度查询
页面类型:查询列表（非编辑型）
创建人：Hades Ying

注意：只有指定的用户区可以修改或增加内容！！！

包括
1-页面布局显示（一般不引用除公共类外的任何JAVA代码）-----------------------------------美工

1a-页面设置:(根据需求文挡界面样例) //style="width:800px; height:550px;"
1b-页面标题:(根据需求文挡界面样例) //title="规章制度表"
1c-按钮设置:(根据需求文挡界面样例)
1d-数据输入:(根据需求文挡界面样例布局，根据数据库设计文挡设置元素的id- "ic"+大写字段名)
/

2-前台代码执行（JS 一般在页面显示后，调用数据初始化AJAX代码）-----------------------------------美工/程序员

2a-iKeyCode:(主关键字变量)  //iKeyCode="" +  < % =request.getParameter("id主关键字字段名") % > + "";    
2b-showMsg:(标题) //title:'规章制度表',
2c-ajax提交事件:    //程序员根据要求处理
2d-工具按钮--代码:(按钮点后的动作)    //程序员根据要求处理
2e-loadData(读取界面信息):(根据数据库设计文挡 设置 //在页面上显示数据） //规章制度编号$("#ic字段名").html(data.字段名);)
2f-showFile（显示附件）

3-模块特定代码（JS 一般在页面提交前执行）-----------------------------------程序员

**/
 %>
<!--  必须使用UTF-8 字符集合 -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	   
	   <!--用户区 ：JAVA引用-这里可以列出需要引用的包  -->
		<%@ page import="com.pantech.base.common.tools.MyTools" %>
		<%@page import="com.pantech.base.common.tools.PublicTools"%>
	   <!--用户区 ：JAVA引用-结束  -->	
		
<html>
<head>
<title>规章制度</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
    <!-- JQuery 专用4个文件 -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/JQueryUI/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/JQueryUI/themes/icon.css">
	<script type="text/javascript" src="<%=request.getContextPath()%>/JQueryUI/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/JQueryUI/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/JQueryUI/locale/easyui-lang-zh_CN.js"></script> 
     <!--用户区 ：JS引用-这里可以列出需要引用的JS包  -->
		
	 <!--用户区 ：JS引用-结束  -->	

</head>

<%
	//用户区 ：页面初始化JAVA代码-这里可以编写各模块独有的功能代码
	
	
	
	//获取应用设置中参数
	//PublicParametersLib pplBean = new PublicParametersLib(request);
	
	//用户区 ：页面初始化JAVA代码-结束 
 %>

<jsp:useBean class="com.pantech.base.common.tags.security.PurViewTools" id="pc" scope="page" />
<%String mcode = pc.GetModuleCodeFromUrl(request); %>
<security:securityForPage pagePermissionID="<%=mcode%>">

<body class="cent">
<form action="<%=request.getContextPath()%>/Svl_Ruler" method="Post" id="form1" >
    <!--用户区 ：1a-页面设置-这里设置width 和 height 值,一般不需要修改 ,一旦修改，则以下所有用户区 对应 width和height 值也必须修改 -->
	<div id="divleft" class="easyui-layout" style="width:100%; height:100%;" align="left">
	<!-- div id="divleft" class="easyui-layout" style="width:100%; height:550px;" align="left "-->
	<!--用户区 ：页面设置-结束  -->	
	   <!--用户区 ：1b-页面标题-这里设置title 值,一般根据模块要求修改  -->
      <div region="north" class="easyui-layout"  title="规章制度列表"  style="overflow:hidden;height:150px;background:#fafafa;">
    
		<!--用户区 ：页面标题-结束  -->
			
		<!--用户区 ：1c-按钮设置-这里设置页面工具条按钮  -->	
		<div region="north" style="padding:5px;background:#fafafa;height:40px;width:100%;">
			<a href="#" onclick="doToolbar(this.id);" id="new"  class="easyui-linkbutton" plain="true" iconCls="icon-new">新建</a> <!-- target="_blank" -->
			<a href="#" onclick="doToolbar(this.id);" id="edit"  class="easyui-linkbutton" plain="true" iconCls="icon-edit">编辑</a>
			<a href="#" onclick="doToolbar(this.id);" id="view"  class="easyui-linkbutton" plain="true" iconCls="icon-search" >详情</a>
			
		</div>
		<!--用户区 ：按钮设置-结束  -->
		
		<!--用户区 ：1d-数据输入-这里可以放置数据录入控件或查询条件 仅需对tr 和 td 做处理  -->
	    <div id="divcenter"  region="center" style="overflow:hidden;background:#fafafa;">
          <table id="ee"  singleselect="true" iconcls="icon-edit" width="100%" class="tablestyle">
            <tr>
			  <td ><label for="ic_1">录入日期</label></td>
			  <td >
					<input class="easyui-datebox" id="icDR_INPUTDATE_S" name="icDR_INPUTDATE_S" style="width:100px;" type="text" required="false"  editable="false"/>
					
					----
					<input id="icDR_INPUTDATE_E" name="icDR_INPUTDATE_E" class="easyui-datebox" style="width:100px;" type="text"  required="false" editable="false"/>
			  </td>
			 
         
              <td ><label for="ic_1"> 类别</label> </td>
              <td>	<select id="icDR_TYPE" name="icDR_TYPE" class="easyui-combobox" style="width:100px;" required="false" editable="false" >
								<option value="">请选择</option>
						</select>
			  </td>
			 
			 <!-- 
			 </tr> 
			 <tr> 
			  -->
			  
			  <td><label for="ic_1">状态</label></td>
              <td><select id="icDR_STATE" name="icDR_STATE" class="easyui-combobox" style="width:100px;" required="false" editable="false">
								<option value="">请选择</option>
								<option value="2">有效</option>
								<option value="3">无效</option>
					</select>
				</td>
				
			  <td> <label for="ic_1">描述</label> </td>
              <td><input id="icDR_SPEC" name="icDR_SPEC" class="easyui-validatebox wid100"  type="text"  required="false"></td>
			 <!-- 
			 </tr> 
			 <tr> 
			  -->
			  	<td colspan="8" align="center"><a href="#" onclick="doToolbar(this.id);" id="que" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
            </tr>
          </table>
        </div>
      </div>
      <!--用户区 ：数据输入-结束  -->
      
      <!--用户区 ：数据列表-这里可以放置数据表格， 仅需对table做处理  -->
	   <div region="center" title=""  align="left" style="background:#fafafa;">
        <!-- div style="background:#fafafa;" -->
          <table id="rulerlist" width="100%"></table>
        <!--  /div -->
	   </div>
	   <!--用户区 ：数据列表-结束  -->
</div>
</form>
</body>
</security:securityForPage>

<script language="JavaScript">
   //用户区 ：JS代码
   //以下为AJAX方法处理时JS代码
   ////////////2a-初始化///一般不用修改，只需要增加////////////////
   var iKeyCode="";// 主关键字 每当某一行被选中时设置，可被 编辑等按钮调用
   
		//开始获取后台设置的数据，用于下拉表
		//这里父类别代码='00001' 必须修改为实际值
		
		//类别
	    var iDR_TYPE=<%=PublicTools.getComboboxOptions("V_信息类别_类别操作","类别名称","类别名称","where 父类别代码='000021' and 状态<>'N'",request)%>;
	   // alert(iDR_TYPE);
	    //此处只需要修改COMBOX ID就可以
	    $("#icDR_TYPE").combobox({
			data:iDR_TYPE,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto'
		});
		
		//状态
		var iState=<%=PublicTools.getComboboxOptions("V_信息类别_类别操作","类别数据","类别名称","where 父类别代码='00005' and 状态<>'N'",request)%>;
	    //alert(iDR_TYPE);
	    //此处只需要修改COMBOX ID就可以
	    $("#icDR_STATE").combobox({
			data:iState,
			valueField:'comboValue',
			textField:'comboName',
			editable:false,
			panelHeight:'auto'
		});	
       
       //结束获取后台设置的数据
	
	////////////////////////////////////     
        //2b-显示消息--名称必须固定
		function showMsg(str){
		  
			$.messager.show({
				title:'规章制度表',
				msg:str,
				showType:'slide'
			});
		}
		
		//显示警告--名称必须固定
		function alertMsg(str){
		  
			$.messager.alert("提示",str);
		}
	
	
	
	//2e-获取表格中的数据（传入所有查询参数）--名称必须固定（若有多个则允许改变）
	function loadGrid(icDR_INPUTDATE_S,icDR_INPUTDATE_E,icDR_TYPE,icDR_STATE,icDR_SPEC){
		$(function(){
			var lastIndex = -1;
			$('#rulerlist').datagrid({
				url: '<%=request.getContextPath()%>/Svl_Ruler', 
				queryParams:{"active":"que","icDR_INPUTDATE_S":icDR_INPUTDATE_S,"icDR_INPUTDATE_E":icDR_INPUTDATE_E,"icDR_TYPE":icDR_TYPE,"icDR_STATE":icDR_STATE,"icDR_SPEC":icDR_SPEC},
				//loadMsg:"信息加载中请稍侯!",
				title: '',
				//width: '100%',
				width:$(this).parent().width(),        
				height: 360,   //此高度需要根据表格之上的输入区域高度进行调整
				//height:$(this).parent().height(),
				nowrap: false,
				//fitColumns: true,
				striped:true, //隔行变色
				pagination:true,
				//pageSize:20,
				//pageList:null,
				singleSelect:true,
				pageNumber:1,
				rownumbers:true,
				frozenColumns:[[
				//显示固定列
					{field:'规章制度编号',title:'规章制度编号',editor:'text',width:120},
					{field:'类别',title:'类别',editor:'text',width:120}
				]],
				columns:[[
				//显示，可滚动列
				    {field:'描述',title:'描述',editor:'text',width:120},
					{field:'录入日期',title:'录入日期',width:120}
					//{field:'基本工资',title:'基本工资',width:120,editor:{type:'numberbox',validType:'maxlength[12]'}},
					//{field:'基本绩效工资',title:'绩效奖基数',width:120,editor:{type:'numberbox',validType:'maxlength[12]'}}
				]],
				/*
				toolbar:[{
				//工具条
					text:'保存',
					iconCls:'icon-save',
					handler:function(){
						$('#rulerlist').datagrid('endEdit', lastIndex);
						$('#active').val('save');
						var val = $('#rulerlist').datagrid('getChanges');
						lastIndex = -1;
						setRec(val);
						$('#form1').submit();
					}
				}],
				*/
				onClickRow:function(rowIndex,rowData){
				//点某行时触发
					iKeyCode=rowData.规章制度编号;//设置主关键子
					//if (lastIndex != rowIndex){
					//	$('#rulerlist').datagrid('endEdit', lastIndex);
					//	$('#rulerlist').datagrid('beginEdit', rowIndex);
					//	lastIndex = rowIndex;
					//}
				},
				onBeforeEdit:function(index,row){     
				//开始编辑之前触发的事件 
					//alert("onBeforeEdit==[lastIndex]=" + lastIndex);
					//alert("开始编辑");
			        //row.editing = true;      
			        //$('#rulerlist').datagrid('refreshRow', index);      
			    },      
			    onAfterEdit:function(index,row){      
			    //编辑完毕之后触发的事件
			    	//alert("onAfterEdit==[lastIndex]=" + lastIndex);
			        //row.editing = false;  
			        //alert("编辑结束");
			        //var val = $('#rulerlist').datagrid('getChanges');
			        //将值赋值到影藏的变量中
					//$('#active').val('save');
					//$('#rulerlist').datagrid('acceptChanges');
					//setRec(val);
					//$('#form1').submit();
					
					//$('#rulerlist').datagrid('acceptChanges');
		           // $('#rulerlist').datagrid('refreshRow',index); 
		            //$('#rulerlist').datagrid('unselectAll');
		            //lastIndex=-1;  
			    },      
			    onCancelEdit:function(index,row){      
			    //取消编辑事件
			    	alert("onCancelEdit");
			        //row.editing = false;      
			        $('#rulerlist').datagrid('refreshRow', index);      
			    },
			    onLoadSuccess:function(data) {
			    //成功获取数据后事件
			    	//alert("onLoadSuccess==[lastIndex]=" + lastIndex);
			    	//if (lastIndex != -1) {
				    	$('#rulerlist').datagrid('acceptChanges');
			            //$('#rulerlist').datagrid('refreshRow',lastIndex); 
			            $('#rulerlist').datagrid('unselectAll');
			            //lastIndex = -1;
		           	//}
		           	//alert("lastIndex=" + lastIndex);
			    },
			    onLoadError:function() {
			    //获取数据失败后处理
			    	$('#rulerlist').datagrid('reload');
			    }
			});
		});
	}
	/*
		2c-form1，ajax提交事件
	*/
	$('#form1').form({
		url:'<%=request.getContextPath()%>/Svl_Ruler', 
	    onSubmit: function(){ 
	    	//alert("提交"); //一般不需要

	    }, 
	    success:function(data){
	    	//alert("okk"); //一般不需要

	    }
	});

	$(document).ready(function(){ 
	    //页面初始化时就获取数据
	    dosubmit();
		//loadGrid($("#ic_qUserName").val(),$("#ic_qUserCode").val());
	});
	
	//保存记录--名称必须固定 
	function setRec(val){
	     //--此处不用
         //重新显示界面元素
		 //$('#ic_USERNAME').val(val[0].姓名);
	}
	
	//提交 --名称必须固定 
	function dosubmit(){
	    //alert($("#icDR_TYPE").combobox('getValue'));
	    ////20110414 hadesying 修正 下拉表 应该使用.combobox('getValue')
		loadGrid($("#icDR_INPUTDATE_S").datebox('getValue'),$("#icDR_INPUTDATE_E").datebox('getValue'),$("#icDR_TYPE").combobox('getValue'),$("#icDR_STATE").combobox('getValue'),$("#icDR_SPEC").val());
	}
	
	//2d-工具按钮--代码
	//名称必须固定，参数可变(多态)
	function doToolbar(iToolbar){
	   //alert(iToolbar);
	   if (iToolbar=="new"){
	      //新建
	      //当前窗口
	      location.href="RulerInfo.jsp";
	      //新窗口
	      //window.open("");
	   }
	   
	   if (iToolbar=="edit"){
	      //编辑
	      if(iKeyCode==""){
	        alertMsg("请先选中一行记录"); 
	        return
	      }
	      
	      //当前窗口
	      location.href="RulerInfo.jsp?type=edit&icDR_CODE="+ iKeyCode;
	      //新窗口
	      //window.open("");
	   }
	   if (iToolbar=="view"){
	      //详情
	      //新窗口
	      showFile();
	   }
	   if (iToolbar=="que"){
	      //查询
	      //当前窗口
	    // alert($("#icDR_INPUTDATE_S").datebox('getValue')); 
	    // alert($("#icDR_INPUTDATE_S").text()); 
	     dosubmit();
	      //新窗口
	      //window.open("");
	   }
	   //;//执行 
	}
	
	//2f-显示附件-名称固定
	function showFile(){
	   //一般只修改附件标识
	   var pjtype="401";   //附件标识
	   var filedecsc="401";//附件标识
	   var filetype="附件";
	   
	   var str = "<%=request.getContextPath()%>/form/File/FileList.jsp" +
				"?type=view&pjcode="+ iKeyCode +" &pjtype=" + pjtype + "&realid=&filedesc=" + filedecsc + "&filetype=" + filetype + "&Refreah=F";
	   window.open(str,'','state=no,width=800,height=600,resizable=yes');
	}
	//用户区 ：JS代码-结束
</script>
</html>