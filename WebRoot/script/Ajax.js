		
		// Ajax.js
   /**
    Ajax 封装对象
    使用方法：
    （1）提交请求，比如不刷新页面的情况下在页面制定DIV中显示输入表单或者其他内容：
     HTML:文件中定义<div id="result"></div>
     触发函数：
     1var url = "http://somehost.com/do.action";
     2var object = new Ajax();
     3object.sendRequest(url,"result");
     4//默认为GET方法，如果在TOMCAT5中遇到乱码问题可以选择使用POST方法提交
     5//object.sendRequest(url,"result","POST");
     
     (2) 提交请求，如删除某个项目之后刷新页面或者重新读取列表，操作提示用alert方法
      1var url = "http://somehost.com/do.action?id=ID";
      2var object = new Ajax();
      3//设定请求完成之后调用的函数
      4object.setPostHook(function(){window.location.reload();});
      5object.sendRequest(url);

     （3）自动用Ajax方式提交表单
      在上面的js封装中，有一个submitForm()方法，这个方法是为提交字段比较长的表单而特别设计的，它根据表单的各个属性来自动生成URL，自动提交请求.
      它的效果与直接提交表单的效果差不多
      1form对象（提交到action目标中）
      2显示返回结果用的div
      3请求完成之后调用的函数
      HTML:文件中定义<div id="result"></div>
      submitForm(document.forms["form1"], div, function(){window.location.reload();}, "POST")   
   
      注：若div为空或"",则无任何显示
          若需要根据返回值做处理，则应该先把返回值放到div中
   */
    
  /**//*Author: yican@cs-air.com
  a simple ajax wrapper
  */
  function Ajax() {
      var req = null;
      var postAction = null;//a hook
      var divName = "";
      /**//*post action hook definition*/
      this.setPostHook = function(f) {
         postAction = f;
     }
     //execute the remote method and refresh the page'd div
     this.sendRequest = function(url, div, method) {
         var callback = this.report;//default alert
         if(div != null && div!==""){
             callback = this.processAjaxResponse;
             divName = div;
         }
         if(method == null) {
             //method = "POST";//default POST - no character encoding problem
             method = "GET";//default GET
         } else {
             method = method.toUpperCase();
         }
         /**//*encode URL with Chinese*/
         //url = encodeURI(url);  //在执行的时候再用
 
         //execute the remote method
         this.executeXhr(method, callback, url);
     }
     //this is call back method 仅当需要在页面某个DIV显示返回信息时使用
     this.processAjaxResponse = function() {
         
         
         // only if req shows "loaded"
         var div = document.getElementById(divName);
         //display the process bar
         div.innerHTML = "loading";
         
         
         if (req.readyState == 4) {
             if (req.status == 200) {
                //alert(req.responseText);
             //显示返回的内容
                div.innerHTML = req.responseText;
 //               div.innerText = req.responseText;
             } else {
             alert("There was a problem retrieving the XML data:\n" +
             req.statusText);
             }
             //execute hook function
             if(postAction){
                 //alert("!!");
                 postAction();
             }
         }
     }
     //alert 不需要显示返回信息，则无任何显示
     this.report = function() {
         if (req.readyState == 4) {
             // only if "OK"
             if (req.status == 200) {
                //alert(req.responseText);
             } else {
                alert("There was a problem retrieving the XML data:\n" +
             req.statusText);
             }
             //execute hook function
             if(postAction){
                 postAction();
             }
         }
     }
     //execute ajax request
     this.executeXhr = function(method, callback, url) {
         // difine a XMLHttpRequest object
        /*
         if (window.XMLHttpRequest) {
             // branch for native XMLHttpRequest object
             req = new XMLHttpRequest();
         } else {
             // branch for IE/Windows ActiveX version
             req = new ActiveXObject("Microsoft.XMLHTTP");
         }
         try{
             req.setRequestHeader("Cache-Control: no-store, no-cache, must-revalidate");
             req.setRequestHeader("Connection","close");
         } catch(e){}
         */
         
         if(window.XMLHTTPRequest)
		{
			req=new XMLHTTPRequest();
			if(req.overrideMimeType)
	        {
	        	req.overrideMimeType("text/xml");
	        }
		}
        else if(window.ActiveXObject)
        {
        	try
        	{
        		req=new ActiveXObject("Msxml2.XMLHTTP");
        	}
        	catch(e)
        	{
        		try
        		{
        			req=new ActiveXObject("Microsoft.XMLHTTP");
        		}
        		catch(e){}
        	}
        }
        if(!req)
        {
        	window.alert("can not create XMLHttpRequest object！");
        	return false;
        }
         //
         req.onreadystatechange = callback;
         if(method == "POST") {
             //split the url
             var urlWithParam = url.split("?");//split the url in two parts
             var urlPrefix = urlWithParam[0];//the url
             var arg = urlWithParam[1];//the arguments //这个参数不要ENCDOEURL 编码,因为在前面已编过了

             req.open("POST", urlPrefix, true);
//             req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
             req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            
             req.send(arg);
             //??????????????????
               //var sBody=getRequestBody(document.forms["form1"]);
               //DEBUG-显示POST的数据
              // document.all.showmsg.innerText =document.all.showmsg.innerText + "------"  + arg + "------" + sBody;
     		  // req.send(sBody);
         } else {
             req.open("GET", encodeURI(url), true);
            //req.send(null);
             req.send();
         }
     }
 }
 ///
 /*
 function getRequestBody(oForm)
			{  var aParams=new Array(); 
			   for(var i=0;i<oForm.elements.length;i++){  
			     var sParam=encodeURIComponent(oForm.elements[i].name); 
			     sParam+="=";
			     sParam+=encodeURIComponent(oForm.elements[i].value);
			     aParams.push(sParam); 
			   } 
			  return aParams.join("&");
			 }			
*/
 ////////////////////////////
	//POST 方式 提交AJAX页面时专用
	function getUrlForPost(oForm)
	{  
	   
	         
	   var url = "";
       url += (oForm.action + "?");//url start with the form's action
	   var aParams=new Array(); 
	   for(var i=0;i<oForm.elements.length;i++){  
	     var oParam=oForm.elements[i]
	     var sParam=encodeURIComponent(oForm.elements[i].name); 
	     if(sParam != '') {//you can omit some value by set the element's name to blank
            if(oParam.type == 'checkbox' || oParam.type == 'radio') {
                //alert(e);
                //if it is a checked box
                if(oParam.checked) {
                	sParam+="=";
	           		sParam+=encodeURIComponent(oParam.value);
                }
            } else {
                sParam+="=";
	            sParam+=encodeURIComponent(oParam.value);
            }
            aParams.push(sParam);
        }
	     var sParam=encodeURIComponent(oForm.elements[i].name); 
	      
	   } 
	  return  url + aParams.join("&");
	
	 }			
	//////////////////////////
 
//get all the form elements value
function getUrlForPost_OLD(form) {
    var url = "";
    url += (form.action + "?");//url start with the form's action
    var len = form.elements.length;
    for (i = 0; i < len; i++)
    {
        var e = form.elements[i];
        /**//*FIXME: can't get value of selectd options of multi select(a list)*/
        if(e.name != '') {//you can omit some value by set the element's name to blank
            if(e.type == 'checkbox' || e.type == 'radio') {
                //alert(e);
                //if it is a checked box
                if(e.checked) {
                    url += (encodeURIComponent(e.name) + "=");
                    url += (encodeURIComponent(e.value) + "&");
                }
            } else {
                url += (encodeURIComponent(e.name) + "=");
                url += (encodeURIComponent(e.value) + "&");
            }
        }
    }
    if(url.lastIndexOf('&') == (url.length - 1)) {
        //if the last char is a '&', get rid of it
        url = url.substring(0, url.length - 1);
    }
    //alert(url);//DEBUG
   return url;
}
/**//*A simple wrapper for submit a form*/
function submitForm(form, div, done, method) {
    var url = getUrlForPost(form);
    
    var object = new Ajax();
    if(done != null) {
        object.setPostHook(done);
    }
    //post is the most common method for post a form
    if(method == null){
        method = "POST";//default 'POST'
    }
   //document.all.showmsg.innerText=url; 
   //alert(method);
    object.sendRequest(url, div, method);
}
	