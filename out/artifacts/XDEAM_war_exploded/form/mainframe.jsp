<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.pantech.base.common.tools.PublicTools"  %>
<%@ page import="com.pantech.base.common.tools.MyTools"  %>
<%@ page import="com.pantech.src.develop.store.user.UserProfile"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
UserProfile up = new UserProfile(request,MyTools.getSessionUserCode(request));

String userName = up.getUserName();
String userCode = up.getUserCode();
%>
<head id="Head1">

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>上海市现代职业技术学校</title>
   <link href="<%=request.getContextPath()%>/css/default.css" rel="stylesheet" type="text/css" />
   <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/themes/icon.css"/>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/JQueryUI/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/common/clientScript.js"></script>
    <script type="text/javascript">
    var _menusFull = "";
    var state = "";
    var rootPath = "<%=request.getContextPath()%>";
    //var _menus=<-%=PublicTools.getLeftMenu(request, "T")%>;
    var id = "<%=MyTools.StrFiltr(request.getParameter("iKeyCode"))%>";
    if(id == ''){
		window.location = "<%=request.getContextPath()%>/form/homePage.jsp";
	}
    var _menus = <%=PublicTools.getLeftMenu(request,"T", MyTools.StrFiltr(request.getParameter("iKeyCode")))%>;
    
	$(document).ready(function(){
		if(id == '336048791'){
			$('#backHome').hide();
			$('#backPj').show();
		}
	});   
    
    function CheckMenu(){
    		 $.ajax({
    		   type: "POST",
			   url: '<%=request.getContextPath()%>/Svl_Attestation',
			   data: "active=CheckMenu&usercode="+"<%=userCode%>",
			   dataType: 'json',
			   success: function(datas){
			   //根据记录状态显示数据
			  /** var _menusTop="";
			   var _menusButoom="";
			   _menusFull = '{"menus":[';
			   _menusTop = '{"menuid":"1","icon":"icon-sys","menuname":"后台设置","auth":"","menus":[';
			   _menusButoom = ']},{"menuid":"2","icon":"icon-sys","menuname":"系统管理","auth":"","menus":[';
			   for(var i=0;i<datas.length;i++){
				   var data = datas[i];
                   if(data.parId=='6'){
                	  // alert(data.menuname);
				      // _menus = '{"menus":['+
						//	'{"menuid":"1","icon":"icon-sys","menuname":"后台设置","auth":"",'+
						//'"menus":['+
						//	'{"menuid":"'+data.menuid+'","menuname":"'+data.menuname+'","icon":"icon-users","url":".'+data.url+'","auth":"'+data.state+'"}'+
						//	']'+
						//	'}'+
						//']}';
						_menusTop += '{"menuid":"'+data.menuid+'","menuname":"'+data.menuname+'","icon":"a_'+data.menuid+'","url":".'+data.url+'","auth":"'+data.state+'"},';
                   }
                   else if(data.parId=='1000'){
                	   _menusButoom += '{"menuid":"'+data.menuid+'","menuname":"'+data.menuname+'","icon":"'+data.menuid+'","url":".'+data.url+'","auth":"'+data.state+'"},';
                   }
			   }
			   _menusTop = _menusTop.substring(0,_menusTop.length-1);
			   _menusButoom = _menusButoom.substring(0,_menusButoom.length-1);
			   _menusFull += _menusTop;
			   _menusFull += _menusButoom;
			   _menusFull += "]}]}";
			    //var data = datas[0];
			    //var data1 = datas[1];
			    //var data2 = datas[2];
			    //var data3 = datas[3];
			    _menus = strToObj(_menusFull);
			    //alertMsg(_menus);**/
			    //var _menus=< %=PublicTools.getLeftMenu(request,"T")%>;
			    //alert(_menus);
			   InitLeftMenu();
    		   }
    	});
    }
    
   // var _menus = _menusFull;
   //将string类型转换成object类型
    function strToObj(json){ 
         return eval("("+json+")"); 
}
    /*
    	var _menus = {"menus":[
		{"menuid":"1","icon":"icon-sys","menuname":"后台设置","auth":"",
"menus":[
									{"menuid":"12","menuname":"用户注册","icon":"icon-users","url":"./form/user/Usermanage.jsp","auth":"true"},
									{"menuid":"12","menuname":"权限设置","icon":"icon-customer","url":"./form/auth/AuthorityMain.jsp","auth":"true"},
									{"menuid":"12","menuname":"角色设置","icon":"icon-person","url":"./form/role/RoleList.jsp","auth":"true"},
									{"menuid":"12","menuname":"登录认证","icon":"icon-business","url":"./form/atte/AttestationList.jsp","auth":"true"},
									{"menuid":"12","menuname":"系统转向","icon":"icon-project","url":"./form/turn/TurnList.jsp","auth":"true"},
									{"menuid":"12","menuname":"用户维护","icon":"icon-customs","url":"./form/userMainte/UserMaintenanceList.jsp","auth":"true"},
									{"menuid":"13","menuname":"认证维护","icon":"icon-confirming","url":"./form/atteMainte/AtteMaintenance.jsp","auth":"true"}
									]
						},
						{"menuid":"2","icon":"icon-sys","menuname":"系统管理（模块）","auth":"",
							"menus":[
									{"menuid":"12","menuname":"菜单管理","icon":"","url":"./base/config/MenuConfig.jsp","auth":"true"},
									{"menuid":"12","menuname":"系统权限设置","icon":"","url":"./base/purview/PurviewConfigFrame.jsp","auth":"true"},
									{"menuid":"12","menuname":"角色管理","icon":"","url":"./form/system/AuthConfig.jsp","auth":"true"},
									{"menuid":"13","menuname":"部门管理","icon":"","url":"./form/system/DeptConfig.jsp","auth":"true"},
									{"menuid":"13","menuname":"人员所属角色管理","icon":"","url":"./form/system/UserAuth/UserAuthConfig.jsp","auth":"true"},
									{"menuid":"13","menuname":"人员所属部门管理","icon":"","url":"./form/system/UserDept/UserDeptConfig.jsp","auth":"true"}
									
								]
						}
		]};
     
*/

    

       
      		

//获取离开系统的当前时间
function	getOutTime(){
	$.ajax({
		url:"<%=request.getContextPath()%>/Svl_Attestation",
		data:"active=getOutTime&user="+"<%=userCode%>",
		type:"post",
		dataType:"json",
		success:function(datas){
			var data = datas[0]
			//alert(data.MSG);
			//$.messager.messagge("此处需要登录代码...");
			location.href = "SsoLogout.jsp";
		}
	});
}
        
        
$(function(){
	//alert(_menus);
	InitLeftMenu();
	//tabClose();
	//tabCloseEven();
	$('#loginOut').click(function() {
           // $.messager.confirm('系统提示', '您确定要退出本次登录吗?', function(r) {
           //     if (r) {
//		 getOutTime();//获取离开系统的当前时间
            //    }
            //});
           location.href = "Logout.jsp";
            // ConfirmMsg('您确定要退出本次登录吗?','getOutTime()','');
        })
})
//function openwin(){
//alert(1);
// window.open("changePass.html");
//}


</script>

</head>
<body class="easyui-layout" style="overflow-y: hidden"  scroll="no">
<noscript>
<div style=" position:absolute; z-index:100000; height:2046px;top:0px;left:0px; width:100%; background:white; text-align:center;">
    <img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
</div></noscript>
    <div region="north" split="true" border="false" style="overflow: hidden; height: 30px;
        background: url(images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%;
        line-height: 20px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
        <div style="float: left;padding-left: 5px;style="">
        	<!-- <img src="<-%=request.getContextPath()%>/images/logo_xinxi.png"/> -->
        </div>
        <span style="float:right; padding-right:20px;" class="head"><b>欢迎<%=userName %> </b><a href="#" id="backHome" onclick="window.location.href='<%=request.getContextPath()%>/form/homePage.jsp';">返回首页</a><a href="#" id="backPj" onclick="window.location.href='<%=request.getContextPath()%>/form/questSurvey/wjdcList.jsp';" style="display:none;">返回首页</a><!-- <a href="#" id="editpass">修改密码</a> --> <a href="#" id="loginOut">安全退出</a></span>
        <span style="padding-left:10px; font-size: 16px; "><span width="165" height="25" align="absmiddle">  </span>上海市现代职业技术学校</span>
    </div>
    <div region="south" split="true" style="height: 30px; background: #D2E0F2; ">
        <div class="footer"></div>
    </div>
    <div region="west" hide="true" split="true" title="导航菜单" style="width:180px;" id="west">
			<div id="nav" class="easyui-accordion" fit="true" border="false">
					<!--  导航内容 -->
			</div>

    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title="欢迎使用" style="padding:5px; overflow:hidden; color:red;">
				<iframe id ="right" src="./Welcome.jsp" width="100%" height="100%" scrolling=yes noresize frameborder="0"></iframe>
				<iframe id ="foot1" src="" width="0" height="0" scrolling=no noresize frameborder="0"></iframe>
			</div>
		</div>
    </div>
</body>
</html>