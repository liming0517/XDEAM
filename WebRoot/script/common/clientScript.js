/**
公用的一些功能，公用字符串定义、通用提示函数
本文件仅支持 UTF-8 字符集 功能
支持RDC 2011版 JQUERY 框架

  功能函数
 *
 *				函数名										描述
 *
 *	showMsg(str)							|JQUERY浮动提示
 *	alertMsg(str)							|JQUERY警告提示（对话框）
 *
*/

//用于前台界面交互时的消息及变量定义
//全局使用，可能被后继函数调用
//全局使用，可能被后继函数调用
var objForm={
 	sAuditAlert:"请注意：提交后记录不可修改！是否提交？",  //审核提交前的提示
    sViewOnly:"本记录不可编辑！"                          //用于提示是否有修改权
};

//var sAuditAlert="请注意：提交后记录不可修改！是否提交？";
//var sViewOnly="本记录仅可查阅！";
/*
form1，ajax浮动显示消息
 */
function showMsg(str){
  
	$.messager.show({
		title:'',
		msg:str,
		showType:'slide'
	});
}

//显示警告--名称必须固定
//ajax对话框形式出现
function alertMsg(str){
  
	$.messager.alert("",str);
}


//显示警告--名称必须固定
//str:提示文本
//strTrue:确认后待执行函数名()
//strFalse:取消后待执行函数名
//ajax对话框形式出现
function ConfirmMsg(str,strTrue,strFalse){
	$.messager.confirm('提示',str, function(r){
			if (r){
				//alert("true");
				try{
					eval(strTrue);
					return true;
				}catch (e)
				{return false; }
				
				
			}else{
				try{
					eval(strFalse);
					return false;
				}catch (e)
				{return false;}
				
				
			}
		});
		  
}
/*
 * Tommy Shen 
 * 将json对象转为json字符串给servlet
 */
function json2str(o){
	   var r = [];
	   //alert("0000======"+(o==""));
	   //alert(typeof o == "string" || o == null||o=="");
	   //alert("11111===="+(typeof o == "string"));
	   //alert("2222====="+(o == null));
	  // return false;
       if(typeof o == "string" ||typeof o == "number"||typeof o == "boolean"|| o == null) {
         return o;
       }
       if(typeof o == "object"){
      
         if(!o.sort){
           r[0]="{"
           for(var i in o){             
              // if(json2str(o[i])!=null && json2str(o[i])!=""){
                // alert(obj2str(o[i]));
                     r[r.length+1]=i;
                     r[r.length+1]=":\"";
                     r[r.length+1]=json2str(o[i])?json2str(o[i]):"";
                     r[r.length+1]="\",";
              //  }
           }
           r[r.length-1]="\"}"
         }else{
           r[0]="{\"dataList\":["
       // alert(obj2str(o[i]));
           for(var i =0;i<o.length;i++){
             r[r.length+1]=json2str(o[i]);
             r[r.length]=",";
           }
           r[r.length-1]="]}"
         }
      
         return r.join("");
       }
       return r.toString();

	} 
/*
 * Tommy Shen 
 * 将json对象转为json字符串给servlet
 * ojson对象，strTab对应表格的id
 */
function dataGrid2jsonstr(o,strTab){
	   var r = [];
	   //alert("0000======"+(o==""));

	   //alert(typeof o == "string" || o == null||o=="");
	   //alert(typeof o);
	   //alert("11111===="+(typeof o == "string"));
	   //alert("2222====="+(o == null));
	  // return false;
       if(typeof o == "string" ||typeof o == "number"|| o == null) {
         return o;
       }
       if(typeof o == "object" ){
      
         if(!o.sort){
           r[0]="{"
           for(var i in o){             
              // if(json2str(o[i])!=null && json2str(o[i])!=""){
                // alert(obj2str(o[i]));
                     r[r.length+1]=i;
                     r[r.length+1]=":\"";
                     r[r.length+1]=dataGrid2jsonstr(o[i],strTab)?dataGrid2jsonstr(o[i],strTab):"";
                     r[r.length+1]="\",";
              //  }
           }
           r[r.length-1]="\"}"
         }else{
           r[0]="{\""+strTab+"dataList\":["
       // alert(obj2str(o[i]));
           for(var i =0;i<o.length;i++){
             r[r.length+1]=dataGrid2jsonstr(o[i],strTab);
             r[r.length]=",";
           }
           r[r.length-1]="]}"
         }
      
         return r.join("");
       }
       return r.toString();
       //setDatagridjson(strTab,r.toString());

	}
/**Tommy Shen 2011-07-08
@formid  传入需要转换成json字符串的form的id，用于获取该表单中的空间
**/ 
function form2jsonStr(formid){ //需要转换的form的id
alert("begin...."); 
var jstr='{';
//alert($("*").each());
$("*").each(function()
		{if(($(this).attr("id")!="")&&(($(this).attr("id"))!=null)&&(($(this).attr("id").indexOf("dataList"))==-1)){/**alert($(this).attr("type"));**/if($(this).attr("type")=="hidden"||$(this).attr("type")=="text"||$(this).attr("type")=="select"){jstr+=$(this).attr("id")+':"'+$(this).val().replace(/\\/g,'\\\\')+'",';}if($(this).attr("type")=="radio"){jstr+=$(this).attr("id")+':"'+$(this).val().replace(/\\/g,'\\\\')+'",';}if($(this).attr("type")=="checkbox"){jstr+=$(this).attr("id")+':"'+$(this).attr("value").replace(/\\/g,'\\\\')+'",';}}});

//alert($("hidden").each().length);

//alert(a);
 //for(var i=0;i<jform.length;i++){
 // var el=jform.elements[i];
  //if(el.type.toLowerCase()!='submit'&&el.type.toLowerCase()!='reset'){

   //jstr+=el.id+':"'+el.value.replace(/\\/g,'\\\\')+'",'; //需要把\换成\\，不然被JS认为是转义了 
  //}
 //}
 jstr=jstr.substr(0,jstr.length-1)+'}';
 //alert("jstr");
//alert("jstr"+jstr);
 setformjson(formid,"{\""+formid+"Content\":["+jstr+"]}");  //检测是否表单中是否已经存在用于存放form表单内容的隐藏控件，如果没有增加一个，如果有责赋值

 //return "{\""+formid+"Content\":["+jstr+"]}";
  
}
/**
	用于检测是否存在如果不存在添加
**/
function setformjson(formid,json){
	//alert("begin2.........");
	//alert($("#"+formid+"Content").length);
	//alert("begin2======="+(($("#"+formid+"Content").length)==0));
	if(($("#"+formid+"Content").length)==0){//判断用于存放表单内容的隐藏控件是否存在
		 //alert("11");
		//定义控件并将控件添加至传入的form下面
		 $("<input type='hidden' name='"+formid+"Content' id='"+formid+"Content'>").appendTo($("#"+formid)); 
		
		 //alert("22");
		 //inputNode.attr("type","hidden");
		 //inputNode.attr("name",formid+"Content");
		 //inputNode.attr("id",formid+"Content");
		// alert("33");
		 //inputNode.attr("value",json);
		// alert("44");
		// $("#"+formid+"Content").appendTo($(formid)); 
		 //给隐藏控件赋值
		 $("#"+formid+"Content").val(json);
			//alert("55"); 
			//alert("form1cONTENT==="+$("#"+formid+"Content").val());
			 //alert(document.getElementById("form1Content").getAttribute("value"));
		 }else{
		// alert("22");
		// alert("json==="+json);
			$("#"+formid+"Content").val(json);
			//alert("form1cONTENT==="+$("#"+formid+"Content").val());
			//alert("33");
		 }
	 //alert(eval($("form1Content")));
	 //alert(eval($("form1Content"))=="object");
	// alert($(formid+"Content").getAttribute("value"));
}
/**
用于检测是否存在如果不存在添加
**/
function setDatagridjson(tabid,json){
if(($("#"+tabid+"dataList").length)==0){//判断用于存放表单内容的隐藏控件是否存在
	//定义控件并将控件添加至传入的form下面
	 $("<input type='hidden' name='"+tabid+"dataList' id='"+tabid+"dataList'>").appendTo($("#"+tabid)); 
	 $("#"+tabid+"dataList").val(json);
	 }else{
		$("#"+tabid+"dataList").val(json);
	 }
}
//用于得到容器宽度的一个比例，
//可以用于 按表格宽度自动设置列宽的功能
function fillsize(percent) {    var bodyWidth = document.body.clientWidth;    return (bodyWidth -90) * percent;}
//Tommy  初始化菜单
/**
function InitLeftMenu() {
	$("#nav").accordion({animate:false});

    $.each(_menus.menus, function(i, n) {//遍历菜单json，第一层菜单
		var menulist ='';
		menulist +='<ul >';
		//遍历
        $.each(n.menus, function(j, o) { //遍历第二层菜单
        	//alert(o.url==null;
        	//alert(o.url);
        	
        	if(o.menus==undefined){//判断第二层是否有子菜单
        		menulist +=getMenuItem(o,"","2");
			}else{
				
				menulist +='<li  class="3">';
				menulist += '<div class="nav2"><a ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span class="icon icon-edit_add" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div><ul >';
				$.each(o.menus, function(p,q) { //遍历第三层菜单
					
					if(q.menus==undefined){//判断第三层是否有子菜单
		        		menulist += getMenuItem(q,"none","3");//'<li><div><a ref="'+q.menuid+'" href="#" rel=".' + q.url + '" ><span class="icon '+q.icon+'" >&nbsp;</span><span class="nav">' + q.menuname + '</span></a></div></li> ';
					}else{
						
						menulist +='<li style="display:none" class="levle-3 4">';
						menulist += '<div class="nav3"><a ref="'+q.menuid+'" href="#" rel="' + q.url + '" ><span class="icon icon-edit_add" >&nbsp;</span><span class="nav">' + q.menuname + '[更多]</span></a></div><ul >';
						$.each(q.menus, function(r,s) { //遍历第四层菜单
							menulist += getMenuItem(s,"none","4");
						})
						menulist += '<ul></li>';
					}
					
				})
				menulist += '<ul></li>';
				
			}
			//$.each(o.menus, function(p,q) { //遍历第三层菜单
				
			//})
		})
		
		menulist += '</ul>';
		//添加菜单，设置第一层菜单的内容
		$('#nav').accordion('add', {
            title: n.menuname,
            content: menulist,
            iconCls: 'icon ' + n.icon
        });
		

    });
*/
function InitLeftMenu() {
	$("#nav").accordion({animate:false});

    $.each(_menus.menus, function(i, n) {//遍历菜单json，第一层菜单
		var menulist ='';
		menulist +='<ul >';
		//遍历
        $.each(n.menus, function(j, o) { //遍历第二层菜单
        	if(o.menus==undefined){//判断第二层是否有子菜单
        		menulist +=getMenuItem(o,"","2");
			}else{
				menulist +='<li  class="3">';
				menulist += '<div class="nav"><a ref="'+o.menuid+'" href="#" rel=".' + o.url + '" ><span class="icon  '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '&nbsp;&nbsp<img src="./images/jt.png"/></span></a></div><ul >';
				$.each(o.menus, function(p,q) { //遍历第三层菜单
					if(q.menus==undefined){//判断第三层是否有子菜单
		        		menulist += getMenuItem(q,"none","3");//'<li><div><a ref="'+q.menuid+'" href="#" rel=".' + q.url + '" ><span class="icon '+q.icon+'" >&nbsp;</span><span class="nav">' + q.menuname + '</span></a></div></li> ';
					}else{
						menulist +='<li style="display:none" class="levle-3 4">';
						menulist += '<div class="nav"><a ref="'+q.menuid+'" href="#" rel=".' + q.url + '" ><span class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + q.menuname + '&nbsp;&nbsp<img src="./images/jt.png"/></span></a></div><ul >';
						$.each(q.menus, function(r,s) { //遍历第四层菜单
							menulist += getMenuItem(s,"none","4");
						})
						menulist += '<ul></li>';
					}
					
				})
				menulist += '<ul></li>';
				
			}
			//$.each(o.menus, function(p,q) { //遍历第三层菜单
				
			//})
		})
		
		menulist += '</ul>';
		//添加菜单，设置第一层菜单的内容
		$('#nav').accordion('add', {
            title: n.menuname,
            content: menulist,
            iconCls: 'icon ' + n.icon
        });
		

    });
 /**   
function getMenuItem(o,hidden,levle){
	//if(!isNull(o.url))
	//Tommy Shen 判断是否为外部链接，如果是外部链接则不加点，加点时为了兼容权限系统
	if(o.url!=undefined){
	if(o.url.substr(0,4).toLowerCase()!="http"){
					o.url='.'+o.url;
				}
	}
	//处理结束
	
	//}
	if(o.auth=="true"){//判断是否有权限，有权限时为超链接，无权限时无时间
				return '<li class="levle-'+levle+'" style="display:'+hidden+'"><div><a id="" ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div></li> ';
			}else{
				return '<li class="levle-'+levle+'" style="display:'+hidden+'"><div class="nav1" title="无权限"><span class="icon '+o.icon+'" >&nbsp;</span><span >' + o.menuname + '</span></div></li> ';
			}
}
*/
function getMenuItem(o,hidden,levle){
	if(o.auth=="true"){//判断是否有权限，有权限时为超链接，无权限时无时间
				return '<li class="levle-'+levle+'" style="display:'+hidden+'"><div><a id="" ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div></li> ';
			}else{
				return '<li class="levle-'+levle+'" style="display:'+hidden+'"><div class="nav1" title="无权限"><span class="icon '+o.icon+'" >&nbsp;</span><span >' + o.menuname + '</span></div></li> ';
			}
}
//Tommy 菜单点击事件
	$('.easyui-accordion li a').click(function(){
		var tabTitle = $(this).children('.nav').text();
		var url = $(this).attr("rel");
		var menuid = $(this).attr("ref");
		var icon = getIcon(menuid,icon);
		//alert($(this).find('li').size());
		//if($(this).children()){
		//alert($(this).parent().parent().find('ul').find('li').length);
		//alert($(this).parent().parent().find('ul').find('li').css("display"));
		//alert($(this).parent().parent().find('ul').find('li').css("display")=="none");
		var temp=$(this).parent().parent().attr('class').substring($(this).parent().parent().attr('class').length-1);
		//alert(temp);
		//alert($(this).parent().parent().find('ul').find('li.levle-'+temp).length);
		//alert(this.id);
		if($(this).parent().parent().find('ul').find('li.levle-'+temp).length>0){
			if($(this).parent().parent().find('ul').find('li.levle-'+temp).css("display")=="none"){
				$(this).parent().parent().find('ul').find('li.levle-'+temp).css("display","");
				$(this).parent().parent();
			}else{
				$(this).parent().parent().find('ul').find('li').css("display","none");
			}
			
		}else{
		//以下用于处理菜单的点击事件，当http开头时，以window.open方式打开 Tommy shen 2013-11-04 为招生管理项目特殊处理
			if(url.substr(0,4).toLowerCase()!="http"){
				addTab(tabTitle,rootPath+url,icon);
			}else{
				window.open(url);
			}
			//特殊处理结束
		$('.easyui-accordion li div').removeClass("selected");
		$(this).parent().addClass("selected");
		}
		
		//}
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});

	//选中第一个
	var panels = $('#nav').accordion('panels');
	var t = panels[0].panel('options').title;
    $('#nav').accordion('select', t);
}
//获取左侧导航的图标
function getIcon(menuid){
	var icon = 'icon ';
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				icon += o.icon;
			}
		 })
	})

	return icon;
}

function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			icon:icon
		});
	}else{
		$('#tabs').tabs('select',subtitle);
		$('#mm-tabupdate').click();
	}
	tabClose();
}

function createFrame(url)
{
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose()
{
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}
//绑定右键菜单事件
function tabCloseEven()
{
	//刷新
	$('#mm-tabupdate').click(function(){
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		$('#tabs').tabs('update',{
			tab:currTab,
			options:{
				content:createFrame(url)
			}
		})
	})
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close',currtab_title);
	})
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			$('#tabs').tabs('close',t);
		});
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		$('#mm-tabcloseright').click();
		$('#mm-tabcloseleft').click();
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==0){
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	})
}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}




