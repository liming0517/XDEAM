//***********************************************************************
//函数名称：	isDate()
//函数功能：	判定是否为日期型数据
//函数描述：	正确日期型数据返回true,否则返回false,年月日间隔: /  -  .
//完成日期：	2002.03.14
//函数作者：	zzk
//***********************************************************************
function isDate(Obj) {
	if(Obj.substring(4,5) != '-' && Obj.substring(4,5) != '/' && Obj.substring(4,5) != '.') {
		return false;
	}
	var aryNum = new Array();
	for(var i = 0 ; i < 10 ; i++) {
		aryNum[i] = i;
	}
	var intNum = 0;
	for(var i = 0 ; i < Obj.length ; i++) {
		if(i != 4 && i != 7) {
			for(var j = 0 ; j < 10 ; j++) {
				if(Obj.substring(i,i+1) == aryNum[j]) {
					intNum++;
				}
			}
		}
	}
	if(intNum != 8) {
		return false;
	}
	var year = parseInt(Obj.substring(0,4));
	var month;
	var day;
	if(Obj.substring(5,6) == '0') {
		month = parseInt(Obj.substring(6,7));
	}
	else {
		month = parseInt(Obj.substring(5,7));
	}
	if(Obj.substring(8,9) == '0') {
		day = parseInt(Obj.substring(9,10));
	}
	else {
		day = parseInt(Obj.substring(8,10));
	}
	if(month > 12 || month < 1 || day < 1) {
		return false;
	}
	if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
		if(day > 31) {
			return false;
		}
	}
	else if(month == 4 || month == 6 || month == 9 || month == 11) {
		if(day > 30) {
			return false;
		}
	}
	else if(month == 2) {
		if(year % 4 > 0) {
			if(day > 28) {
				return false;
			}
		}
		else if(year % 100 == 0 && year % 400 > 0) {
			if(day > 28) {
				return false;
			}
		}
		else {
			if(day > 29) {
				return false;
			}
		}
	}
	return true;
}

//***********************************************************************
//函数名称：	isInt()
//函数功能：	判定是否为整数型数据
//函数描述：	正确整数型数据返回true,否则返回false,没有位数限制
//完成日期：	2002.03.14
//函数作者：	zzk
//***********************************************************************
function isInt(Obj) {
	if(Obj.length == 0) {
		return false;
	}
	var aryNum = new Array();
	for(var i = 0 ; i < 10 ; i++) {
		aryNum[i] = i;
	}
	if(Obj.substring(0,1) == '0' && Obj.length > 1 && parseInt(Obj) != 0) {
		return false;
	}
	var intNum = 0;
	for(var i = 0 ; i < Obj.length ; i++) {
		for(var j = 0 ; j < 10 ; j++) {
			if(Obj.substring(i,i+1) == aryNum[j]) {
				intNum++;
			}
		}
	}
	if(intNum != Obj.length - 1 && Obj.substring(0,1) == '-') {
		return false;
	}
	else if(intNum != Obj.length && Obj.substring(0,1) != '-') {
		return false;
	}
	return true;
}

function VerifyAssign()
{
	var vVip = UPDATE.AS_VIP_PERC.value;
	var vCom = UPDATE.AS_COM_PERC.value;
	if(isInt(vVip) && isInt(vCom))
	{
		return true;
	}
	else
	{
		alert("VIP百分比和COM百分比必须为整数");
		return false;
	}
}

function VerifyPerson()
{
	var vFromDate = UPDATE.PE_FROMDATE.value;
	var vToDate = UPDATE.PE_TODATE.value;
	if(isDate(vFromDate) && isDate(vToDate))
	{
		return true;
	}
	else
	{
		alert("日期格式应为YYYY-MM-DD");
		return false;
	}
}
