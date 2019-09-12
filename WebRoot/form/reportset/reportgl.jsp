<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.pantech.base.common.tools.MyTools"%>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<%@ page import="com.pantech.src.develop.Logs.*"%>
<%@ page import="com.pantech.base.common.tools.*"%>
<%@ page import="com.pantech.src.develop.store.user.*"%>
<%@ page import="com.pantech.src.develop.manage.workremind.WorkRemind"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.*"%>
<%@ page import="com.pantech.base.common.db.DBSource"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css"
		href="<%=request.getContextPath()%>/css/themes/icon.css">
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>

	  <body class="easyui-layout" style="width:100%;height:100%">
	  	<div region="north" align="left" title="报表筛选"
			style="overflow: hidden; height: 50px;">
			<a  onclick="doToolbar(this.id);"
				id="quedin" name="quedin" class="easyui-linkbutton" plain="true"
				iconCls="icon-edit">确定</a>
		</div>
		<div region="center" style="background: #fafafa; width: 100%;height:100%">
			
				<div region="center" id="showreport" name="showreport" style="background: #fafafa;">
					<!-- 动态加载 -->
					
				</div>
				 
		</div>
	  </body>

  	  
  	   
  <script type="text/javascript">
  	var cursxid="";
  	
  	var rows = null;
  	var userreportid="<%=request.getParameter("userreportid")%>";
  	var view=<%=PublicTools.getsxfield(request.getParameter("userreportid"),request)%>;
  	var address="";
  	var tiaojian="";
  	$(document).ready(function(){
  		//loadGrid();
  		showreport();
  	});
  	
	function showreport(){
		$.ajax({
				type: "POST",
				url: '<%=request.getContextPath()%>/Svl_userreportinfo',   //设置 SVL
				async:false,
				data: "active=quesxcol&userreportid=" + userreportid,  // 设置模式，一般为view ，主关键字值		
				dataType: 'json',
				success: function(datas){
					//alert(datas[0].div);
					$('#showreport').html(datas[0].div);
					$(".easyui-combobox").combobox();
				}
		});
	}
		


	function doToolbar(iToolbar) {
  		if(iToolbar=="quedin"){
  			//alert();
			for(var i=0;i<$("#jishu").html();i++){
				//
				if($("#inp"+i).val()!=""){
					tiaojian+=" and "+$("#hid"+i).val();
					//tiaojian+="='";
					//tiaojian+=$("#inp"+i).val()+"'";
					//alert();
					//alert($("#com2").combobox('getValue'));
					
					if($("#com"+i).combobox('getValue')=="001"){
						tiaojian+="='";
						tiaojian+=$("#inp"+i).val()+"'";
					}
					if($("#com"+i).combobox('getValue')=="002"){
						//alert(tiaojian);
						tiaojian+=" like ';K;";
						tiaojian+=$("#inp"+i).val()+";K;'";
					}
					if($("#com"+i).combobox('getValue')=="003"){
						tiaojian+=">'";
						tiaojian+=$("#inp"+i).val()+"'";
					}
					if($("#com"+i).combobox('getValue')=="004"){
						tiaojian+=">='";
						tiaojian+=$("#inp"+i).val()+"'";
					}
					if($("#com"+i).combobox('getValue')=="005"){
						tiaojian+="='";
						tiaojian+=$("#inp"+i).val()+"'";
					}
					if($("#com"+i).combobox('getValue')=="006"){
						tiaojian+="<='";
						tiaojian+=$("#inp"+i).val()+"'";
					}
					if($("#com"+i).combobox('getValue')=="007"){
						tiaojian+="<'";
						tiaojian+=$("#inp"+i).val()+"'";
					}
					
				}
			}
			
			//alert(window.parent.document.getElementById("inp1").value);
			window.opener.document.getElementById("tiaojian").value=tiaojian;
			window.opener.tijiao();
			window.close();
  		}
  	}
 	
		
  	
  </script>
  
</html>
