//2005/3/10 shenqh
var currFocus;
var ExistAttaInfo = new Array();
var oldDelAttas = new Array();
var attaIdx = 0;
var IsIE;


function fSetArrEmailAddress(){
	var win = window.parent.addrdata;
	gArrEmailAddress = win.addr_data;
}
function fInitMSIE(){
	if (navigator.userAgent.indexOf("MSIE") != -1){
		IsIE = true;
	}else{
		IsIE = false;
	}
}
function setFocusObj( obj ) 
{
    //don't use the element'ID directly,use getElementById method
	var sendmail = document.sendmail;

	currFocus = obj;
	sendmail.ToInd.style.visibility = "hidden";
	sendmail.CcInd.style.visibility = "hidden";
	sendmail.BccInd.style.visibility = "hidden";

	if(obj.name == "to")
		sendmail.ToInd.style.visibility = "visible";
	if(obj.name == "cc")
		sendmail.CcInd.style.visibility = "visible";
	if(obj.name == "bcc")
		sendmail.BccInd.style.visibility = "visible";		
}
function Preview(){
	if(ishtml==false){
		ViewAsHtml();
		return;
	}else{
		var frm = document.frmHtmlPreview;
		if ( document.all ){
			frm.content.value = document.all.htmlletter.html;
		}else if (document.htmlletter){
			frm.content.value = document.htmlletter.html;
		}
		frm.stationery.value = document.sendmail.stationery.value;
		frm.ishtml.value='1';
		frm.submit();
		return;
	}
	return;
}
function ViewAsHtml() {
	var frmSendmail = document.sendmail;
	if ( !frmSendmail.chkHtmlMessage_text.checked ){
		alert("请选择以HTML格式发送");
	}else{
		var frm = document.frmHtmlPreview;
		frm.content.value = document.getElementById("lettercontent").value;
		frm.ishtml.value = '1';
		frm.submit();
		return;
	}
}

function SetVals( optype ) {
	if( optype+"" == "timeset.x" )
	{
		if( document.all.year.value == "" || document.all.month.value =="" || document.all.day.value == "" || document.all.hour.value == "")
		{
			alert( "请填写定时发信的年、月、日、时刻等。" );
			event.returnValue = false;
			return false;
		}
	}
	if( optype+"" == "send.x" || optype+"" == "timeset.x" )
	{
		if( document.all.to.value == "" ){
			alert( "请填写收件人Email。");
			event.returnValue = false;
			return false;
		}
	}
	if( !checkEmail( document.all.to.value ))
		return false;
	if( !checkEmail( document.all.cc.value ))
		return false;
	if( !checkEmail( document.all.bcc.value ))
		return false;
	if( document.all.subject.value == "" && optype+"" != "newsign.x")
	{
		if( confirm( "请您确定真的不需要写 主题 吗?") == true )
		{
			document.all.subject.value = "(无主题)";
		}
		else
		{
			window.event.returnValue = false;
			return false;
		}
	}
	
	if( optype + "" != "" && optype + "" != "undefined"){
		showSending(optype);
	}


	if(ishtml==false){
		var obj = document.getElementById("lettercontent");
		document.sendmail.htext.value = obj.value;
		if (document.sendmail.chkHtmlMessage_text.checked)
			document.sendmail.chkHtmlMessage.value="y";
		else
			document.sendmail.chkHtmlMessage.value="";
	}else{
		var obj = document.getElementById('htmlletter');
		document.sendmail.htext.value = obj.html;
		document.sendmail.chkHtmlMessage.value="y";
	}
	return true;
}



function dispCCorBcc( obj )
{
	if( obj.innerText == "抄送")
	{
		tr_cc.style.display='';
		obj.style.display='none';
		document.all.cc.focus();
	}
	if( obj.innerText == "密送" )
	{
		tr_bcc.style.display='';obj.style.display='none';document.all.bcc.focus();
	}
}

function changeKeyCode() {
	var code;
	var e = window.event;

	if (e.keyCode)	{
		code = e.keyCode;
	}else if (e.which){
		code = e.which;
    }
	return;

	if (code == 13 ) {	//回车 -> Tab
		if (e.keyCode) e.keyCode=9;	//Tab
		else if (e.which) e.which=9;
	}
}

function checkEmail( mails )
{
	if( mails == "")
		return true;
	mails = mails.replace( /;/g, ",");
	var mailArray = mails.split( ",");
	var unregs = "";
	for( var i=0;i < mailArray.length; i++ )
	{
		var pos = mailArray[i].lastIndexOf( "@");
		if( pos == -1)
		{	unregs = unregs + mailArray[i] + ",\n";
			continue;
		}
		if( mailArray[i].length == pos + 1)
		{	unregs = unregs + mailArray[i] + ",\n";
			continue;
		}
		if( mailArray[i].charAt( mailArray[i].length-1 ) == ">" ){
			fix = mailArray[i].substring( pos , mailArray[i].length-1 );
		}else{
			fix= mailArray[i].substr( pos );
		}
		if( !isEmailAddress(fix)){
			unregs = unregs + mailArray[i] + ",\n";
		}
	}
	if( unregs != "")
	{
		alert( "请正确填写以下Email地址：\n" + unregs.substring( 0, unregs.length-2));
		event.returnValue = false;
		return false;
	}
	return true;
}
function isEmailAddress( email )
{
	var reg=/^@([\w-]+\.)+[\w-]{2,4}$$$/;
	return ( email.match( reg ) != null ) ? true:false;
}
function showAttaInfoOpName( deleted ) {
	if (deleted == 'y') {
		document.write('<U>取消删除</U>');
	}
	else if (deleted == 'n') {
		document.write('&nbsp;&nbsp;<U>删除</U>');
	}
}
function opAttaInfo(index , filename) {
	var rowObjId = "idAttaInfoRow" + index;
	var rowObj = document.getElementById(rowObjId);
	var statusObjId = "idDeleteAtta" + index;
	var statusObj = document.getElementById(statusObjId);
	var opObjId = "idOpAttaInfo" + index;
	var opObj = document.getElementById(opObjId);
	var attanameObj = document.getElementById("attaname" + index);
	attanameObj.value = filename;
	
	if (rowObj.className == "attainfoy") {//cancel delete
		rowObj.className = "attainfon";
		statusObj.value = "";
		opObj.innerHTML = "&nbsp;&nbsp;<u>删除</U>";
		oldDelAttas[index] = "";
	}
	else if (rowObj.className == "attainfon") {//delete
		rowObj.className = "attainfoy";
		statusObj.value = "y";
		opObj.innerHTML = "<table><tr><td class='attainfot'>取消删除</td></tr></table>";
		oldDelAttas[index] = "y";
	}
}
function showSending(optype) {
	var msg = "";
	if(optype == "send.x" )
	{
		msg = "  正在发送信件, 请稍等...";
	}
	if( optype == "savetodraft.x" )
	{    msg = " 正在保存信件, 请稍等...";
	}
	if( optype == "timeset.x" )
	{  msg = " 正在保存定时发信，请稍等...";
	}
	sending_sub.appendChild( document.createTextNode(msg));
	if( msg != "" )
	{
		sending.style.visibility="visible";
	}
}




//本js文件用于compose.htm同时发送多个附件的js函数，支持 IE 和 NNx 系列。
//所有的函数都适用于 IE 和 NNx 系列，直接调用就可以
//函数里面会根据语句判断浏览器类型，然后依据不同的bowser属性增加元素
//主要功能是动态创建 各种表单元素 和 删除表单元素
//Edit by shen

//    function list
//    -------------------------------------------------------------------------------------------------------------------
//    IsIEBrowser()  返回值: true(IE) false(NNx)
//    exist(fileId)  判断fileId是否已经存在
//    addInputFile(spanId,fileId)  在名为spanId 的元素里面增加序号为index的input file
//    addbr(spanId,brId)  在名为spanId 的元素里面增加id为brId的换行符
//    adddel(spanId,index) 在名为spanId 的元素里面增加序号为index的"删除"按钮，他可以删除相应的input file 和 br
//    addhidden(spanId,hiddenId,value)  在名为spanId 的元素里面增加id为hiddenId，值为value的input type=hidden
//    -------------------------------------------------------------------------------------------------------------------



//search,全局变量
//-----------------------------------------------------------------------------------------------------------
function IsIEBrowser() {
	if (navigator.userAgent.indexOf("MSIE") != -1) {
		return true;
		} else {
		return false;
		}
}

// 增加附件函数 ()，增加到 idfilespan,基数为 attaIdx 。
function add() {

	addfile("idfilespan",attaIdx);
	attaIdx++;
	return false;
}
//获取附件路径
function getAppFile(){
//alert(attaIdx);
	//var obj;
	//var arr=new Array();
	var arr;
	arr="";
	for ( var i= 0 ; i < form1.elements.length ; i ++ ) {				    
		if ( form1.elements[i].type == "file" ) {
	//		alert(form1.elements[i].value);
	        if (form1.elements[i].value.length>0){    //HADES  需要去掉空的记录
				arr=arr+form1.elements[i].value+",";
			}
		}
	}
	//alert(arr.substring(0,arr.length-1));
	return arr.substring(0,arr.length-1);
	
}

//----------------------------------------fileexist()----------------------------------------------------------
//shenqh
function exist(fileId) {
      var file = document.getElementById(fileId);
	  var result = false;
	  if ( file !=null ) {
			if ( file.value != null && file.value != "") {
				result = existFile( file);
			}//if
	  }
	  if( result)
	  {
		alert("此附件已粘贴, 如果在原有附件列表中并已删除，请取消删除；\n如果在新粘贴附件列表中，请先删除再粘贴。");
		//--attaIdx;
		//document.getElementById( "idAttachOper"+ attaIdx).click();
	  }
}//end of function
//shenqh
function getfilename( attaName ) {
	var s = attaName.lastIndexOf( '\\' );
	return attaName.substr(s+1, attaName.length - s -1);
}
//shenqh
function existFile( file)
{
	var form = document.sendmail;
	for ( var i= 0 ; i < form.elements.length ; i ++ ) {				    
		if ( form.elements[i].type == "file" && form.elements[i].name != file.name ) {
			if ( file.value == form.elements[i].value ) {
				return true;
			}
		}
	}//for
	for (var i=0; i<ExistAttaInfo.length; i++) {
		var theName = ExistAttaInfo[i];
		if ( theName != null && theName != "" && theName == getfilename(file.name) ) {
			return true;
		}
	}
	return false;
}
//----------------------------------------addfile(spanId,index)----------------------------------------------
function addfile(spanId,index)
{
       var strIndex = "" + index;
	   var fileId = "attachfile"+ strIndex;
	   var brId = "idAttachBr" + strIndex;
	   addInputFile(spanId,fileId);
  // HADES 这个按钮根本没作用，不如去掉
	//  adddel(spanId,index);

	   addbr(spanId,brId);
	   //document.getElementById( "attachfile"+ strIndex).click();
	   return;
}
//-------------------------------------------sub function----------------------------------------------------
function addInputFile(spanId,fileId)
{
	  var span = document.getElementById(spanId);
	  if ( span !=null ) {
	                if ( !IsIE ) {
						var fileObj = document.createElement("input");
						if ( fileObj != null ) {
							fileObj.type="file";
							fileObj.name = fileId;
							fileObj.id = fileId;
							fileObj.size="50";
							var clickEvent = "exist('" + fileId + "')";
							fileObj.setAttribute("onclick",clickEvent,0);  
							span.appendChild(fileObj);
						}//if fileObj
					}// !IsIE

					if ( IsIE ) {
						var fileTag = "<input type='file' id ='" + fileId + "' name='" + fileId + "' size=50 onchange=exist('" + fileId + "')>";
						var fileObj = document.createElement(fileTag); 
						span.appendChild(fileObj);
					}//IsIE if
			
	  }//if span
}

function addbr(spanId,brId)
{
	  var span = document.getElementById(spanId);
	  if ( span !=null ) {
			var brObj = document.createElement("br");
			if ( brObj !=null ) {
				brObj.name = brId;
				brObj.id = brId;
				span.appendChild(brObj);
            }//if
     }//if
	 return;
}

function adddel(spanId,index)
{
      var strIndex = "" + index;
	  var delId = "idAttachOper" + strIndex;
	  var span = document.getElementById(spanId);
	  if ( span != null ) {
			var oTextNode = document.createElement("<span style='width:5px'> </span>");
			//oTextNode.style.width="5px";
			span.appendChild(oTextNode);
		    if ( IsIE ) {
	        var tag = "<input type='button' id='" + delId + "' onclick=delfile('" + spanId + "',"+strIndex+")></input>";
			var delObj = document.createElement(tag);
			if ( delObj != null ) {
				span.appendChild(delObj);
			}//if

			}// Is IE
			
	        if ( !IsIE ) {
				var delObj = document.createElement("input");
				if ( delObj != null ) {
					delObj.name = delId;
					delObj.id = delId;
					delObj.type = "button";
					var clickEvent = "return delfile('" + spanId + "',"+strIndex+");";
					delObj.setAttribute("onclick",clickEvent);  
					span.appendChild(delObj);
				}//if
			}// !IsIE if
			if( delObj != null) delObj.value = "删除";
		}//main if
		return;
}


//-------------------------------------------------------------------------------------------------------------


//---------------------------------------------delete input file-----------------------------------------------
function delfile(spanId,index)
{
	   var strIndex = "" + index;
	   var fileId = "attachfile"+ strIndex;
	   var brId = "idAttachBr" + strIndex;
	   var delId = "idAttachOper" + strIndex;
	   //first,get the element
       var span = document.getElementById(spanId);
	   //alert(  "del span: " + span  );
	   if ( span == null ) return false;

	   var fileObj = document.getElementById(fileId);
	   if ( fileObj == null ) return false;

	   var brObj = document.getElementById(brId);
	   if ( brObj ==null ) return false;

	   var delObj = document.getElementById(delId );
	   //alert(  "del delId: " + delObj  );
	   if ( delObj == null ) return false;

       //second,create the replace element
	   var temp= document.createElement("<span>");
	   //third,replace it
	    span.replaceChild(temp,fileObj);
		span.replaceChild(temp,brObj);
		span.replaceChild(temp,delObj);	
		//alert( span.innerHTML );
		return false;
}



//------------------------------Sync and subit Form ---------------------------------

function compose_send(optype) {
		var bRight = SetVals(optype);
		if(!bRight){
			return false;
		}			

		var form = document.sendmail;
		form.optype.value = optype;
		var actionVars = new Array("fid", "order", "desc", "oldmid", "mid", "sid",
					"priority", "sign", 
					"stationery", "chkSendImgWithLetter", 
					"setreplyflag", "setforwardflag", 
					"chkHtmlMessage", "ifsavetosent", "return_receipt",
					"draftmid", "draftmsid", "compinfo_urlfilesmode","compinfo_minute");
	    var actionurl = "/coremail/cgi/attachfapps?";

		//构造 action URL
		for ( var i = 0; i < actionVars.length; i++ ) {
			var varName = actionVars[i];
			//得到里面的sendmail里面的各个元素，然后对其操作
			var srcObj = document.sendmail.elements[ varName ];
			if ( srcObj!=null && srcObj.value!=null ) {
				var type = srcObj.type;
				if (type != null && type != "" && type == "checkbox" ) {
					if ( srcObj.checked ) {
						actionurl += '&' + varName + '=' + srcObj.value;
					}
				}
				else {
					actionurl += '&' + varName + '=' + srcObj.value;
				}
			}
		}	
	//构造 form 的 action URL
	document.sendmail.action = actionurl + document.sendmail.action;
	
	var obj = document.getElementById('idOpType');
	obj.name = optype;
	obj.value = 1;
	
	document.sendmail.submit();
	return true;
}


function addhidden_ie(spanId,hiddenId,value) 
{
         var span = document.getElementById(spanId);
	     if ( span == null ) return;
	     if ( IsIE ) {
	    var tag = "<input type=hidden name='" + hiddenId + "' value='" + value + "'>";
		var delObj = document.createElement(tag);
		if ( delObj != null ) {
				span.appendChild(delObj);
	    }//if
		return;
	 }
}

//-------------------------------------add hidden function-----------------------------------------------
function addhidden(spanId,hiddenId,value)
{
      if ( !IsIE ) {
		  var span = document.getElementById(spanId);
		  if ( span == null ) return;

		  var hiddenObj = document.createElement("input");
		  if ( hiddenObj == null ) return;
		  hiddenObj.type="hidden";
		  hiddenObj.name = hiddenId;
		  hiddenObj.id = hiddenId;
		  hiddenObj.value = value;
		  span.appendChild(hiddenObj);
	  }
      else {
		  addhidden_ie(spanId,hiddenId,value);
	  }
	  return ;
}


//------------------------------------------------------------------------------------------------





function onCancelButtonClick()
{
	if( confirm( "你确定要取消发信吗？这样您写的内容会全部消失的。\n【确定】：取消发信；\n【取消】：继续写信。"))
		window.location.href = "/coremail/fcg/ldapapp?funcid=foldmain&sid="+ sid;
}



//Here Beginning:2004-12-17
function changeEditorEx()
{
    if ( !IsIE ){ 
		alert("只有Microsoft IE 系列的浏览器才支持本功能，你的浏览器并不支持本功能。");
		return;
	}
	if ( ishtml ){	
	    conf = confirm("将把正文转成纯文本格式，您确定么？");
		if (! conf)
		return;
    	ishtml = 0;
		change2Normalor();
	}
	else
	{
		ishtml = 1 ;
		change2Htmlor();
	}
}
function change2Normalor()
{
	var obj = document.getElementById("htmlletter");
	if ( !obj ){
		alert("error");
	}else{
		con = obj.text;
	}

	editor_hideLayer("trComposeHtml");
	editor_showLayer("trComposeTxt");
	editor_hideLayer("htmlor");
	editor_showLayer("normalor");
	
	obj = document.getElementById("lettercontent");
	if ( !obj ){
		alert("error2");
	}else{
		obj.value = con;
	}
}
function change2Htmlor()
{
	var obj = document.getElementById("lettercontent");
	if ( !obj ){
		alert("error");
	}else{
		con = obj.value;
	}

	editor_showLayer("trComposeHtml");
	editor_hideLayer("trComposeTxt");	
	editor_showLayer("htmlor");
	editor_hideLayer("normalor");
	//----------------------------------
	//var s =  document.sendmail.stationery;


	//var stationeryoption = "<option value='2'> one </option> <option value='3'> two </option> ";
	//var select = "<SELECT name='stationery' id='stationery'><OPTION value='0' selected>不使用</OPTION>";
	//select += stationeryoption;
	//select += "</SELECT>";
	//s.outerHTML = select;
	//---------------------------------
	obj = document.getElementById("htmlletter");
	if ( !obj ){
		alert("error2");
	}else{
		obj.text = con;	
	}
}
function editor_hideLayer(name) {
	var layer = editor_getLayer(name);
	if ( !layer ) return false;
	if ( layer ) layer.style.display = "none";
}

function editor_showLayer(name) {
	var layer = editor_getLayer(name);  
	if ( !layer ) return false;
	if ( layer ) layer.style.display = "block";
}
function editor_getLayer(name) {
	var obj = document.getElementById(name);
	if ( obj ) return obj;
	return false;
}
function adjustEditorAtStart(){
	var obj = document.getElementById("lettercontent");
	obj.value = document.sendmail.htext.value;

	var objHtml = document.getElementById("htmlletter");
	objHtml.text = document.sendmail.htext.value;	

	//if ( ishtml ) {change2Htmlor();}
}
function fBodyOnload(){	
	adjustEditorAtStart();

	var form = document.sendmail;		

	if (form.to.value == "" || form.to.value == null){
		form.to.focus();
	}
}