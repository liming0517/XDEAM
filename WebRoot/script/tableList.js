//2005/3/10 shenqh
var currFocus;
var ExistAttaInfo = new Array();
var oldDelAttas = new Array();
var attaIdx = 0;   //表格中的总行数
var IsIE; 
//本js文件用于通用的表格编写 可以自动增加行 注意，一个页面只支持一个这样的动态表格。
//所有的函数都适用于 IE 和 NNx 系列，直接调用就可以
//函数里面会根据语句判断浏览器类型，然后依据不同的bowser属性增加元素
//主要功能是动态创建 各种表单元素 和 删除表单元素

//注意事项：1-在增加表单元素前，需要有一个表格，所有内容加在这个表格中（当然，FORM也可以，但一般用表格）
//         2-表格中每一行都应该具有相同的对象数量和类型，如果缺有需要不同，必须把不要的对象设置为HIDDEN
//Edit by shen 
//MODIFY BY HADES  2005/3/16



//    function list
//    -------------------------------------------------------------------------------------------------------------------
//	  add(sourceTableName)
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

// 增加一行 ()，基数为 attaIdx 。
//sourceTableName--页面上的表格的ID，新的元素将增加在这里
function addrow(sourceTableName) {
  
	addfile(sourceTableName,attaIdx);
	attaIdx++;
	return false;
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

	   adddel(spanId,index);

	   addbr(spanId,brId);
	   
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