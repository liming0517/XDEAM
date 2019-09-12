//审核部分用到的固定变量，控制重复提交

//审核部分的AJAX操作所需要的JS过程
//1- 点击审核时，需要执行的操作
//alert("this is show!");



//2- 审核填写完成，点击保存或确定时，需要执行的操作
function subaudit(Svl_url){
	var SHCROSS = "";
	//验证输入
	//alert();
	if($('input[name=SHCROSS]').get(0).checked || $('input[name=SHCROSS]').get(1).checked){
		if($('input[name=SHCROSS]').get(0).checked){
			if(!confirm("你确认审核【通过】吗？")){
				return false;
			}else {
			}
			SHCROSS = "1";
		}
		if($('input[name=SHCROSS]').get(1).checked){
			if(!confirm("你确认审核【不通过】吗？")){
				
				return false;
			}
			SHCROSS = "0";
		}
	}else{
		alertMsg("请选择审核结果。");
		return false;
	}
	//alert("$(#SHYJ).val()==============="+$("#SHYJ").val());
	var SHYJ = $("#SHYJ").val();
	//alert("SHYJ==============="+SHYJ);
	var MouldID = escape(encodeURIComponent($("#Audit_MouldID").val()));
	//获取相关标签参数--KEY-已后台编码为UTF-8
	var OprateID = $("#Audit_OprateID").val();
	var tablename=$("#tablename").val();
	var ikeycodename=$("#ikeycodename").val();
	var state=$("#state").val();
	$.ajax({
		type: "POST",
		url: Svl_url,
		//必要的参数
		data: "active=doAuditSH&SHYJ=" + encodeURI(SHYJ) + "&SHCROSS=" + SHCROSS + 
				"&MouldID=" + MouldID + "&OprateID=" + OprateID+
				"&tablename="+encodeURI(tablename)+
				"&ikeycodename="+encodeURI(ikeycodename)+"&state="+encodeURI(state),
		success: function(result){ 
			
			if(tablename=="V_模版配置表"||tablename=="V_文档编辑"){
				window.close();
				window.opener.extend(SHCROSS);
				window.opener.location.reload();
			}else{
				$('#auditedit').window('close');
				//alert(1);
				extend(SHCROSS);
				//alert(2);
				window.location.reload(); 
			}

			
		}, //modfiby by hades 
		error: function(msg){ //20110721 modify by hades 出错释放锁
			alertMsg("审核信息错误，请[重试]或[刷新]后操作。");
		}
	});
}

function extend(SHCROSS){
	
}


function auditOver(){
	
}
		
function yiyue(Svl_url){
	var SHCROSS = "1";
	var SHYJ = "已经阅读,自动通过";
	//alert("SHYJ==============="+SHYJ);
	var MouldID = escape(encodeURIComponent($("#Audit_MouldID").val()));
	//获取相关标签参数--KEY-已后台编码为UTF-8
	var OprateID = $("#Audit_OprateID").val();
	var tablename=$("#tablename").val();
	var ikeycodename=$("#ikeycodename").val();
	$.ajax({
		type: "POST",
		url: Svl_url,
		//必要的参数
		data: "active=doAuditSH&SHYJ=" + encodeURI(SHYJ) + "&SHCROSS=" + SHCROSS + 
				"&MouldID=" + MouldID + "&OprateID=" + OprateID+"&tablename="+encodeURI(tablename)+"&ikeycodename="+encodeURI(ikeycodename),
		success: function(result){ 
			extend(SHCROSS);
			window.location.reload(); 	
		}, //modfiby by hades 
		error: function(msg){ //20110721 modify by hades 出错释放锁
			//alertMsg("审核信息错误，请[重试]或[刷新]后操作。");
		}
	});
}
	
function auditsh(){
	
}
			



function adudittables_toggle(){
	//alert();
	$("#adudittables").window('open');
	//$("#adudittables").window('restore');
	
}

function show_auditedit(){
	$('#auditedit').window('open');
}

function show_feededit(){
	$('#feededit').window('open');
}

function show_yj(path){
	var MouldID = escape(encodeURIComponent($("#Audit_MouldID").val()));
	//获取相关标签参数--KEY-已后台编码为UTF-8
	var OprateID = $("#Audit_OprateID").val();
	var tablename=$("#tablename").val();
	var ikeycodename=$("#ikeycodename").val();
	$.ajax({
		type: "POST",
		url: path,   //设置 SVL
		async:false,
		data: "active=addaudit&MouldID=" + MouldID + "&OprateID=" + OprateID+"&tablename="+encodeURI(tablename)+"&ikeycodename="+encodeURI(ikeycodename),
		dataType: 'json',
		success: function(datas){
			alertMsg("提交审核成功");
			window.location.reload(); 
		}
	});
	
}

function show_open(path){
	window.open(path,'','width=600,height=400');
}

function sublzaudit(Svl_url){
	var SHCROSS = "";
	//验证输入
	
	if($('input[name=SHCROSS]').get(0).checked || $('input[name=SHCROSS]').get(1).checked){
		if($('input[name=SHCROSS]').get(0).checked){
			if(!confirm("你确认审核【通过】吗？")){
				return false;
			}else {
			}
			SHCROSS = "1";
		}
		if($('input[name=SHCROSS]').get(1).checked){
			if(!confirm("你确认审核【不通过】吗？")){
				
				return false;
			}
			SHCROSS = "0";
		}
	}else{
		alertMsg("请选择审核结果。");
		return false;
	}
	//alert("$(#SHYJ).val()==============="+$("#SHYJ").val());
	var SHYJ = $("#SHYJ").val();
	//alert(SHYJ);
	//alert("SHYJ==============="+SHYJ);
	var MouldID = escape(encodeURIComponent($("#Audit_MouldID").val()));
	//获取相关标签参数--KEY-已后台编码为UTF-8
	var OprateID = $("#Audit_OprateID").val();
	var tablename=$("#tablename").val();
	var ikeycodename=$("#ikeycodename").val();
	
	$.ajax({
		type: "POST",
		url: Svl_url,
		//必要的参数
		data: "active=dolzAuditSH&SHYJ=" + encodeURI(SHYJ) + "&SHCROSS=" + SHCROSS + 
				"&MouldID=" + MouldID + "&OprateID=" + OprateID+"&tablename="+encodeURI(tablename)+"&ikeycodename="+encodeURI(ikeycodename),
		success: function(result){ 
			
			$('#auditedit').window('close');
			extend(SHCROSS);
			window.location.reload(); 

			
		}, //modfiby by hades 
		error: function(msg){ //20110721 modify by hades 出错释放锁
			alertMsg("审核信息错误，请[重试]或[刷新]后操作。");
		}
	});
}