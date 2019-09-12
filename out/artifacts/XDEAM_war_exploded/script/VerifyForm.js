// verifyForm.js
// 动态验证表单中输入值的有效性
// 正则表达式语法可参阅 http://www.soulogic.com/doc/RegularExpressions/
// 需要检查的表单元素需要添加一个自定义属性validator，其值为PatternsDict的属性名（如timePat）
// newFunction
function validateForm(theForm){// 若验证通过则返回true
	//====在此定义需要检查的validator及对应的正则表达式=========
	var PatternsDict = new Object();
	//匹配年月日(YYYYMMDD)
	PatternsDict.ymdPat = /^[1-2]\d{3}(0[1-9]|1[0-2])(0[1-9]|[1-2]\d|3[0-1])$/;
　	//匹配05:04 或22:34,但不匹配75:83
　	PatternsDict.timePat = /^([0-1]\d|[2][0-3]):[0-5]\d$/;
	//匹配中文字符
	PatternsDict.chinesePat = /^[\u4e00-\u9fa5]+$/;
	//匹配E-mail
	PatternsDict.emailPat = /^[a-zA-Z]+\w*[a-zA-Z0-9]*[@][a-zA-Z0-9]+[.][a-zA-Z]+$/;
	//匹配整数
	PatternsDict.numberPat = /^-?[1-9]\d*$/;
	//浮点数
	PatternsDict.floatPat = /^-?[1-9]\d*.\d+$/;
	//纯数值型字符
	PatternsDict.numPat = /^\d+$/;
	//大写英文
	PatternsDict.upperPat=/^[A-Z]+$/;
	//小写英文
	PatternsDict.lowerPat=/^[a-z]+$/;
	//英文字符
	PatternsDict.charPat=/^[a-zA-Z]+$/;
	//代码(abc001型)
	PatternsDict.codePat=/^[a-zA-Z]+\d+$/;
	//GUID(e.g:{04DE8592-B789-41B5-BDB8-04B3F5FAFBD6})
	PatternsDict.guidPat=/^[{][0-9a-zA-Z]{8}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{4}-[0-9a-zA-Z]{12}[}]$/;
	//匹配IP地址：
	PatternsDict.ipPat = /^((([1-2]\d)|[1-9])*\d.){3}(([1-2]\d)|[1-9])*\d$/;
	//不能为空
	
	//电话号码
	
	//手机号码
	
	//身份证
	
	//--------------------------------------------------------
	
	//======================检查表单===========================
	var elArr = theForm.elements; // 将表单中的所有元素放入数组

　 	for(var i = 0; i < elArr.length; i++){
	　  with(elArr[i]){              // 对于表单中的每一个元素...
	　   	var v = elArr[i].validator; // 获取其validator属性
	　   	if(!v) continue;            // 如果该属性不存在,忽略当前元素
	
	　   	var thePat = PatternsDict[v];   // 选择验证用的正则表达式
	　   	var gotIt = thePat.exec(value); // 用正则表达式验证elArr[i]的值
	
	　   	if(!gotIt){
	　    		alert(name + ": 输入值与正则表达式不匹配（" + v + " —— " + value + "）"); 
				elArr[i].focus();
				return false;
			}
	　  } 
	}
	return true;
}
//四舍五入   2004-9-20 shenqh len表示保留的小数位
Number.prototype.toFixed=function(len)
{
    //alert("this"+this);
	var add = 0;
    var s,temp;
    var s1 = this + "";
    var start = s1.indexOf(".");
	if(start!=-1){
	//alert(s1.substr(start+len+1,1));
    if(this>0 && s1.substr(start+len+1,1)>=5)add=1;     //当
	if(this<0 && s1.substr(start+len+1,1)>=5)add=-1;  
    var temp = Math.pow(10,len);
    s = Math.floor(this * temp)
	//alert(s1.substr(start+len+1,1)!="");
	if(this<0 && s1.substr(start+len+1,1)!="")s=s+1;
	//alert("s="+s);
	s=s+ add;
	//if(s<0)s=s-add;

    return s/temp;
	}
	else
	{
		return this;
	}
}


function titleset(obj){
	var text = obj;
	text.title = text.value;
}

function titleset2(obj){
	var text = obj;
	text.title='请输入' + text.maxLength + '字以内\n最多还能输入' + (text.maxLength - text.value.length) + '个字符';
}

function titleset3(obj,num){
	var text = obj;
	text.title='请输入' + num + '字以内\n最多还能输入' + (num - text.value.length) + '个字符';
}
