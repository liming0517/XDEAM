//审核部分用到的固定变量，控制重复提交
var isSubmitAudit = false;
var isNowStep = -1;
var isHaveAfterEnnent=false;
var ischeCkAuditDdta=false;
//审核部分的AJAX操作所需要的JS过程
//1- 点击审核时，需要执行的操作
//alert("this is show!");
function Audit_ShowAudit(NowUserStep){
//	alert("this is show!");
	isNowStep = NowUserStep
	if(!isSubmitAudit){
		$("#Audit_NowUserStep").val(NowUserStep);
		$("#Show_Audit_Control").html($("#Audit_Control").html());
		//判断元素是不是存在
		if( $("#Audit_Control").length>0 ){
			$("#Show_Audit_Control").html($("#Audit_Control").html());
		}
		MM_showHideLayers('Layer_00','','show');
	}else{
		alert("正在处理中，请稍候再试或刷新页面后重试。"); 
	}
	isSubmitAudit = true;
}

//2- 审核填写完成，点击保存或确定时，需要执行的操作
function doAudit(Svl_url){
	if(!isSubmitAudit){  //20110721 modify by hades 出错释放锁
	//	alert("正在处理中，请稍候再试或刷新页面后重试。");   
	//	return false;
	}
	//alert(888);
	var SHCROSS = "";
	isSubmitAudit = false;
	//验证输入
	if($('input[name=SHCROSS]').get(0).checked || $('input[name=SHCROSS]').get(1).checked){
		if($('input[name=SHCROSS]').get(0).checked){
			if(!confirm("你确认审核【通过】吗？")){
				isSubmitAudit = true;
				return false;
			}else {
				//alert(777);
				//reparentWin();
				//填用户名和密码<!--20120806 modify by hades 不需要输入密码-->
				/*if($("#UserPass").val()==""){
					alert("请输入密码！");
					isSubmitAudit = true;
					return false;

				} */
				
				//记录审核意见，如果有没有写，就默认"通过"
				//if($("#SHYJ").val()==""){
				//	$("#SHYJ").val("通过");
				//}
			}
			SHCROSS = "1";
		}
		if($('input[name=SHCROSS]').get(1).checked){
			if(!confirm("你确认审核【不通过】吗？")){
				isSubmitAudit = true;
				return false;
			}
			SHCROSS = "0";
		}
	}else{
		alert("请选择审核结果。");
		isSubmitAudit = true;
		return false;
	}

	//当前审核的步骤
	var NowUserStep = $("#Audit_NowUserStep").val();
	//当前的审核意见
//	var SHYJ = $("#SHYJ").val();
//	alert("SHYJ==" + 意见不转CODE);
	// modfiby by hades 20110623
	var SHYJ = escape(encodeURIComponent($("#SHYJ").val()));

	//当前的审核密码
	var UserPass = $("#UserPass").val();
	//当前的审核是否通过
	//改为在判断的时候获取。
	//.......
	
	//处理后台传来的参数，一般不需要再编码
	
	//获取相关标签参数 MouldID 处理中文 // modfiby by hades 20110623
	var MouldID = escape(encodeURIComponent($("#Audit_MouldID").val()));
	//获取相关标签参数--KEY-已后台编码为UTF-8
	var OprateID = $("#Audit_OprateID").val();
	//获取相关标签参数
	var OprateStep = $("#Audit_OprateStep").val();
	//获取相关标签参数--事务提醒参数 -已后台编码为UTF-8
	var Message = $("#Audit_Message").val();
	//获取相关标签参数-特定SQL -已后台编码为UTF-8
	var DoTag = $("#Audit_DoTag").val();

	//获取附加的控件名数组 Audit_Control_List-已后台编码为UTF-8
	var Audit_Control_List = $("#Audit_Control_List").val();
	//获取附加的控件数据库字段名 Audit_Control_Cols-已后台编码为UTF-8
	var Audit_Control_Cols = $("#Audit_Control_Cols").val();
    ////modify by hades 20110823 当指定特殊审核人时，界面需要保持此信息并在审核时传入SVL
	var AuditContrl=$("#AuditContrl").val();
	
	var Control_Value = "";
	var arrCList = new Array();
	if(Audit_Control_List!=undefined && Audit_Control_List!=""){
		arrCList = Audit_Control_List.split(",");
	}
	if(arrCList.length>=1){
		Control_Value = "&";
	}
	for(var i=0;i<arrCList.length;i++){
		//判断控件类型，然后获取值
		if($("#"+arrCList[i]).is("input:text")){
			Control_Value += arrCList[i] + "=" + $("#"+arrCList[i]).val();
		}else if($("#"+arrCList[i]).is("input:checkbox")){
			Control_Value += arrCList[i] + "=" + $('input[id=' + arrCList[i] + '][checked]').val();
		}else if($("#"+arrCList[i]).is("input:radio")){
			Control_Value += arrCList[i] + "=" + $('input[id=' + arrCList[i] + '][checked]').val();
		}else{
			Control_Value += arrCList[i] + "=" + $("#"+arrCList[i]).val();
		}
		if(i+1<arrCList.length){
			Control_Value += "&";
		}
	}
	//alert("TEST");
	//alert(AuditContrl);
	//alert("SHCROSS = " + SHCROSS);
//alert("go");
	$.ajax({
		type: "POST",
		url: Svl_url,
		//必要的参数
		data: "active=doAuditSH&NowUserStep=" + NowUserStep + "&SHYJ=" + SHYJ + "&UserPass=" + UserPass + "&SHCROSS=" + SHCROSS + 
				"&MouldID=" + MouldID + "&OprateID=" + OprateID + "&OprateStep=" + OprateStep + "&Message=" + Message + 
				"&DoTag=" + DoTag + "&Audit_Control_List=" + Audit_Control_List + "&Audit_Control_Cols=" + Audit_Control_Cols + Control_Value +
				"&AuditContrl="+AuditContrl,
		success: function(result){ AuditOK (result);}, //function(result){ AuditOK (result);}, //modfiby by hades 
		error: function(msg){ isSubmitAudit=false;alert("审核信息错误，请[重试]或[刷新]后操作。");} //20110721 modify by hades 出错释放锁
	});
}

//3- 后台执行审核完成后，前台需要执行的操作
function AuditOK(msg){
//	alert(msg);
	//返回内容包括
	//1.当前审核的步骤
	//2.下一步的步骤
	//3.审核意见
	//4.审核时间
	//5.审核人名字
	var msg1 = getXMLMsgValue(msg,"state");
	var msg2=getXMLMsgValue(msg,"msgval"); 
	//alert(555);
	//alert(msg1);
	//alert(msg2);
	isSubmitAudit = false;  //20110721 modify by hades 出错释放锁
	if(msg1=="1"){
		var ReturnArrayValue = getXMLValues( msg,new Array("NowSHStep","NextSHStep","SHIdea","SHTime","SHState","SHName") );
		//操作成功，隐藏点击的审核按钮
		//alert(ReturnArrayValue[1][0]);
		$("#Audit_SH_"+ReturnArrayValue[0][0]).hide();
		
		
		//操作成功的情况下，开始控制显示的内容
		if(ReturnArrayValue[0].length>0){
			//貌似实际上只有一次循环。
			for(var ai=0;ai<ReturnArrayValue[0].length ;ai++){
				//拼装状态的图片的image标签
				var StateImg = "<img width='55px' height='30px' src='/SCH/images/Names/" + ReturnArrayValue[5][ai] + "" + (ReturnArrayValue[4][ai]=="1"?"Y":"N") + ".jpg'>";
				//拼装名字的图片的image标签
				var NameImg = "<img width='55px' height='30px' src='/SCH/images/Names/" + ReturnArrayValue[5][ai] + "Name.jpg'>";
				
				//程序员需要预先定义的ID
				$("#Audit_Idea" + ReturnArrayValue[0][ai]).html( ReturnArrayValue[2][ai] );
				$("#Audit_Time" + ReturnArrayValue[0][ai]).html( ReturnArrayValue[3][ai] );
				$("#Audit_State" + ReturnArrayValue[0][ai]).html( StateImg );
				//用户标签中的名字显示
				$("#Audit_Auth" + ReturnArrayValue[0][ai]).html( NameImg );
			}
			//最后，判断当前人员可以审核的步骤，并显示相应的审核按钮，以及隐藏其他的按钮

			var maxStep = $("#Audit_MaxStep").val();
			//判断是不是在审核范围内，如果是，则显示，否则不显示
			//首先把所有审核按钮不显示
			for(var i=0;i<maxStep;i++){
				$("#Audit_SH_"+(i+1)).hide();
			}
			//然后获取可以点亮的按钮，进行显示
			if(ReturnArrayValue[1][0]!=undefined && ReturnArrayValue[1][0]!=""){
				var _str = ReturnArrayValue[1][0].split(",");
				for(var j=0;j<_str.length;j++){
					for(var i=0;i<maxStep;i++){
						if(_str[j]==i+1){
							$("#Audit_SH_"+(i+1)).show();
							break;
						}
					}
				}
			}
		}
		//一旦操作成功，则需要清除前面输入过的内容
		//如果最后一步审核通过，才执行页面上的JS代码，那么在业务模块代码中需要再次判断状态，否则在多步审核时，每次审核通过都会执行这1代码
		if($('input[name=SHCROSS]').get(0).checked&&isHaveAfterEnnent==true){afterEnvent();}
		
		$("#SHYJ").val("");
		$("#UserPass").val("");
		$('input[name=SHCROSS]').get(0).checked = false;
		$('input[name=SHCROSS]').get(1).checked = false;
		
		MM_showHideLayers('Layer_00','','hide');
		//alert(333);
		reparentWin();
	}else{
		//如果操作没有成功，则重新显示该审核按钮。避免无法操作
		//该操作时在判断操作成功后隐藏的，所以在没有成功的时候，不影响按钮显示。无需操作
		//$("#Audit_SH_"+ReturnArrayValue[0][ai]).show();
		//alert("aaa");
		 alert( getXMLMsgValue(msg,"msgval") );
	}
	//此处插入需要显示返回的操作信息的代码。
	//例如 $("#Audit_MSG").html( getXMLMsgValue(msg,"msgval") );
}

//edit by winder 2010-12-15
//功能:判断调用函数时，是否传入了code参数。
//如果有值，则不做任何操作
//如果无值，则到后台，判断当前用户是否是属于多个部门。
	//如果是多个部门，则强制需要用户选择其中一个，并赋值到相应的hidden标记中。
	//如果是单个部门，则直接复制到相应的hidden标记中
function checkCode(code){
//alert(code);
	if(code == undefined || code == ''){
		//开始到后台判断部门。
		$.ajax({
			type: "POST",
			url: '/SCH/Svl_AuditConduct',
			//必要的参数
			data: "active=getUserDepts",
			success: getUserDetpsOK,
			error: function(msg){ alert("获取用户部门信息错误，将取消该操作！");}
		});
	}else{
		return false;
	}
}

//创建层部分
function mackdiv(){
	var maskdiv = document.createElement("DIV");
	maskdiv.id = "window_mask";
	maskdiv.style.top = 0;
	maskdiv.style.left = 0;
	maskdiv.style.visibility = 'visible';
    //objdiv.style.width = 800;
    //objdiv.style.height = 100;
	maskdiv.style.border = 0;
	maskdiv.style.position = "absolute";
    document.body.appendChild(maskdiv);
    $('#window_mask').css("width",$(window).width());
	$('#window_mask').css("height",document.body.scrollHeight);
	$('#window_mask').css("background-color","#666666");
	$('#window_mask').css("filter","alpha(opacity=50)");
	$('#window_mask').css("z-index",11);

    
	var objdiv = document.createElement("DIV");
    var objname = "window_1";
    objdiv.id = objname;
    objdiv.style.top = 100;
    objdiv.style.left = "30%";
    objdiv.style.visibility = 'visible';
    objdiv.style.width = 200;
    objdiv.style.height = 100;
    objdiv.style.border = 0;
    objdiv.style.position = "absolute";
    //objdiv.style.z-index = 12;
    objdiv.innerHTML="请选择您执行本操作时的部门：";
    document.body.appendChild(objdiv);
    $('#window_1').css("z-index",12);
    $('#window_1').css("background-color","#FFF");
    $('#window_1').css("padding-top","15");
    $('#window_1').css("padding-left","15");
    //objname.onMouseDown=divmove(objname);
}   


function getUserDetpsOK(msg){
	//alert(msg);
	var msg1 = getXMLMsgValue(msg,"state");
	//alert(msg1);
	if(msg1=="1"){
		var ReturnArrayValue = getXMLValues( msg,new Array("deptcode","deptname") );
		if(ReturnArrayValue[0].length>1){
//alert(">1");
			mackdiv();
			//貌似实际上只有一次循环。
			var strVal = "<select name='window_selectDept' id='window_selectDept'>";
			strVal += "<option value=''>请选择</option>";
			for(var ai=0;ai<ReturnArrayValue[0].length ;ai++){
				strVal += "<option value='" + ReturnArrayValue[0][ai] + "'>";
				strVal += ReturnArrayValue[0][ai] + " - " + ReturnArrayValue[1][ai] + "</option>"
			}
			strVal += "</select><br/>";
			strVal += "<input type='button' value='确认' onclick='set_UserDetps($(\"#window_selectDept\").val())'>";
			//alert(strVal);
			$('#window_1').html($('#window_1').html() + "<br/>" + strVal);
		}else if(ReturnArrayValue[0].length==1){
//alert("=1");
			set_UserDetps(ReturnArrayValue[0][0]);
			//if( $("#selectDept").val() != undefined ){
			//	$("#selectDept").val( $("#window_selectDept").val() );
			//}
		}else if(ReturnArrayValue[0].length==0){
//alert("<1");
			set_UserDetps('');
		}else{
			alert("获取用户部门信息错误，将取消该操作");
		}
	}
}

function set_UserDetps(val){
	$("#selectDept").val( val );
	$("#window_mask").hide();
	$("#window_1").hide();
//	alert($("#selectDept").val());
	
}

//获取XML中的列表值
function getXMLValues(msg,valueTag){
	var appendixXML = msg;
	var doc = XMLJS.parseXML(appendixXML);
	var root = XMLJS.getXMLRootNode(doc);
	var nodes = XMLJS.getXMLChildNodes(root);//根节点下面所有子节点元素对象
	var ReturnArrayValue = new Array();
	var getVal = new Array();
	for(var i=0;i<valueTag.length;i++){
		var getVal = XMLJS.getAttributeNodeValues(nodes,"ValTag",valueTag[i]);
		ReturnArrayValue.push(getVal);
	}
	return ReturnArrayValue;
}

//获取XML中的Msg值
function getXMLMsgValue(msg,valueTag){
	var appendixXML = msg;
	var doc = XMLJS.parseXML(appendixXML);
	var root = XMLJS.getXMLRootNode(doc);
	var nodes = XMLJS.getXMLChildNodes(root);//根节点下面所有子节点元素对象
	return XMLJS.getAttributeNodeValues(nodes,"Msg",valueTag);
}

function MM_findObj(n, d) { //v4.01
	var p,i,x;
	if(!d) d=document;
	if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);
	}
	if(!(x=d[n])&&d.all)	x=d.all[n];
	for (i=0;!x&&i<d.forms.length;i++)		x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && d.getElementById) x=d.getElementById(n);
	return x;
}

//显示或隐藏DIV窗口
function MM_showHideLayers() { //v6.0
	var i,p,v,obj,args=MM_showHideLayers.arguments;
	for (i=0; i<(args.length-2); i+=3) {
		if ((obj=MM_findObj(args[i]))!=null) {
			v=args[i+2];
			if (obj.style) {
				obj=obj.style;
				v=(v=='show')?'visible':(v=='hide')?'hidden':v;
				if(v=='visible'){
					$('#mask').show();
					$('#mask').css("width",$(window).width());
					$('#mask').css("height",document.body.scrollHeight);
				}else if(v=='hidden'){
					$('#mask').hide();
					//公共审核部分的代码，用来控制审核状态中，如果为FALSE，则无法继续其他审核步骤，因为当前正在执行中
					isSubmitAudit = false;
				}else{
					$('#mask').hide();
					//公共审核部分的代码，用来控制审核状态中，如果为FALSE，则无法继续其他审核步骤，因为当前正在执行中
					isSubmitAudit = false;
				}
			}
			obj.visibility=v;
			obj.marginTop = (document.body.scrollTop + 50) + "px";
		}
	}
}

var XMLJS = function(){};

XMLJS.apply = function(o, c, defaults){
    if(defaults){
        // no "this" reference for friendly out of scope calls
        XMLJS.apply(o, defaults);
    }
    if(o && c && typeof c == 'object'){
        for(var p in c){
            o[p] = c[p];
        }
    }
    return o;
};

XMLJS.apply(XMLJS, {
docObjs : new Array,
/**
* 初始化xml对象文件连接池
*/
initDocs : function(count)
{
   if(count == null) 
    count = 3;
  
   for(var i = 0; i < count; i++)
   {
    XMLJS.docObjs[i] = new Object;
    //把浏览器判断换成代码判断，而且把标准代码先判断执行，更加合适和具有兼容性
    //由于直接执行不兼容的代码会报错，所以采用try catch解决
    try
    {
     XMLJS.docObjs[i].doc = new DOMParser();
     XMLJS.docObjs[i].doc.async = false;
     XMLJS.docObjs[i].free = true;
    }
    catch(e)
    {
     XMLJS.docObjs[i].doc = new ActiveXObject('Microsoft.XMLDOM');
     XMLJS.docObjs[i].doc.async = false;
     XMLJS.docObjs[i].free = true;
    }

   }
},

/**
* 获得空闲xml文件docObj对象
*/
getFreeDocObj : function ()
{
   var docObj = null;
  
   for(var i = 0; i < XMLJS.docObjs.length; i++)
   {
    if(XMLJS.docObjs[i].free)
    {
     XMLJS.docObjs[i].free = false;
     docObj = XMLJS.docObjs[i];
     break;
    }
   }
  
   return docObj;
},

/**
* 解析xml
*/
parseXML : function (xmlContent)
{
   var docObj = XMLJS.getFreeDocObj();
   try
   { 
    var XmlDom = docObj.doc.parseFromString(xmlContent,"text/xml");
    docObj.free = true;
    return XmlDom;
   }
   catch(e)
   {
    docObj.doc.loadXML (xmlContent);//解析字符串不能用load必须用loadXML.(load解析载入文档的)
    var doc = docObj.doc;
    docObj.free = true;
    return doc;
   }
},

/**
* 解析xml
*/
parseXML2 : function (xmlContent)
{
   var xmlDoc;
   try
   { 
    xmlDoc = new DOMParser();
    var XmlDom = xmlDoc.parseFromString(xmlContent,"text/xml");
    return XmlDom;
   }
   catch(e)
   {
    xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
    xmlDoc.async = false;
    xmlDoc.loadXML(xmlContent);
    return xmlDoc;
   }
},

/**
* 获取xml子节点
* @param{domNode}dom节点
* @param{subNodeName}子节点名称
*/
getXMLSubNodes : function (domNode, subNodeName)
{
   var subNodes = [];
  
   var currentNode = domNode;
   var childNodes = null;
  
   var paths = subNodeName.split ("/");
  
   for (var i = 0; paths != null && i < paths.length-1; i++)
   {
    if (paths[i] == "")
     continue;
   
    childNodes = currentNode.childNodes;
  
    for (var j = 0; childNodes != null && j < childNodes.length; j++)
    {
     if (childNodes[j].nodeName.toLowerCase() == paths[i].toLowerCase())
     {
      currentNode = childNodes[j];
      break;
     }
    }
   }
  
   childNodes = currentNode.childNodes;
  
   for (var i = 0; i < childNodes.length; i++)
   {
    if (childNodes[i].nodeName.toLowerCase() == paths[paths.length-1].toLowerCase())
     subNodes.push (childNodes[i]);
   }
  
   return subNodes;
},

/**
* 获取xml根节点
* @param{doc}xml对象
*/
getXMLRootNode : function (doc)
{
   if (doc == null)
    return null;
  
   return doc.documentElement;
},

getXMLChildNodes: function(root)
{
   var itemNodes = [];
   var nodes = root.childNodes;
  
   for(var i=0;i<nodes.length;i++)
   {
    if(nodes[i].nodeType == 1)
    {
     itemNodes.push(nodes[i]);
     //alert(nodes[i].nodeName + nodes[i].nodeType);
    }
   }
  
   return itemNodes;
},

getXMLChildNode: function(doc_el, nodeName)
{
   var element = doc_el.getElementsByTagName (nodeName);
   return element.documentElement;
},

/**
* 获取xml节点的值
* @param{doc}xml对象
* @param{NodeName}节点名称
*/
getXMLNodeValue : function (doc,nodeName)
{
   if (doc == null)
   {
    return null;
   }
   return XMLJS.getXMLSingleNodeValue (doc.documentElement, nodeName);
},

/**
* 简化 DOM 访问的函数
* @param{doc_el} req.responseXML.documentElement
* @param{name} getElementsByTagName("name"), element name
* @param{idx} element index,exp:elements[0].firstChild.data
* @return nodevalue
* 
*/
getXMLSingleNodeValue: function(doc_el, nodeName)
{
   var element = doc_el.getElementsByTagName (nodeName);
   var nodevalue = "";
   if(element[0].firstChild != null)
   {
    nodevalue =element[0].firstChild.nodeValue;
   }
   return nodevalue;
   //return element[0].firstChild.nodeValue;
},

/**
* 获取xml节点的值
* @param{doc}xml对象
* @param{NodeName}节点名称
*/
getAttributeNodeValues : function (doc_el,nodeName,attributeName)
{
   if (doc_el == null)
   {
    return null;
   }
   var values = [];
   var len = doc_el.length;
   var value = "";
   for(var i = 0; i < len; i++)
   {
    if(doc_el[i].nodeName == nodeName)
    {
     value = doc_el[i].getAttribute(attributeName);
     values.push(value);
    }
   }
   return values;
},

/**
* 获取节点属性值的方法
* @param{doc_el} req.responseXML.documentElement
* @param{attributeName} node Attribute name
* @return nodevalue
* 
*/
getAttributeNodeValue: function(doc_el,attributeName)
{
   var nodevalue = "";
   if(doc_el != null)
   {
    nodevalue =doc_el.getAttribute(attributeName);
   }
   return nodevalue;
}

});
function reparentWin(){
	//alert(222);
}
/**
* @deprecated 调用初始化xml文件对象方法,必须在定义后才能调用
* 
*/
XMLJS.initDocs();