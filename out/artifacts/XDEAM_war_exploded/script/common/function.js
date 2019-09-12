/**
 *    Last Modify by hadesying 2011-06-22
 *    
 *	  JavaScript数据校验常规代码（0.01）
 *
 *
 *
 *	  功能函数
 *
 *				函数名										描述
 *
 *	 isTDate(year,month,day,sStr)			|		是否是有效的日期值
 *	 isDateNum(year,month,day)				|		是否是有效的日期值
 *	 SeleVal(objName)                       |       返回下拉框选中的值（未选中，返回false）
 *   SeleTxt(objName)                       |       返回下拉框选中的Text（未选中，返回false）
 *	 isMailBox(objName,sStr)				|		是否是有效的Mail地址
 *	 isNumeric(objName,sStr)				|       是否是有效的数值（一）入口参数为对象名称
 *	 isNumeric2(data,sStr)					|       是否是有效的数值（一）入口参数为字符串
 *	 isNumeric3(objName,sStr)				|       是否是有效的数值（一）入口参数为对象名称
 *	 isPostID(objName,sStr)					|	    是否是有效的身份证代码
 *	 isNull(objName,sStr)					|		是否为空（一）入口参数为对象名称
 *	 isNull2(data,sStr)						|		是否为空（二）入口参数为字符串
 *	 isSelected(objName,sStr)				|		是否是有选项被选中(从第二项起)		相关函数：GetObjID(ObjName)
 *	 isTSelected(objName,sStr)				|		是否是有选项被选中(选择的值不为-1)	相关函数：GetObjID(ObjName)
 *   isLength(objName,num,sStr)				|		长度是否等于指定长度
 *	 maxValue(objName,min,max,sStr)			|		输入的数值是否在指定的范围内
 *	 isChecked(objName,sStr)				|		是否有选项选中
 *	 isChecked2(objName,i,sStr)				|		是否有选项或有指定的选项选中（数组）
 *	 isPassword(obj1,obj2)				    |		输入口令是否正确
 *　 del(url)								|		是否要删除此条记录　入口参数为联接的文件名称
 *	 setText(objName,sStr)------------------------------------- 设置下拉框TEXT
**/

function selectchange(str,str1){
	//将一个下拉表显示内容给文本栏 2003/6/17 modify by hades
	//alert("ff");
	var obj1 = eval("document.all." + str);  // getObj(str);
	var obj2 = eval("document.all." + str1); //getObj(str1);
	var i_index = obj1.selectedIndex;

	//alert(i_index);
	//obj2.value = obj1.options[i_index].value;//Mem by Bryan 2003-5-15 
	obj2.value = obj1.options[i_index].text;//Add by Bryan 2003-5-15 
}

function trimall(data)
{
  var datastr = data;
  var leftspace = datastr.search(/\S/gi);
  if (leftspace > 0)
  {
    datastr = datastr.substr(leftspace);
  }

  leftspace = datastr.search(/\s/gi);
  var rightspace = 0;
  var endspace = 0;
  
  var leftstr = datastr;
  while (leftspace != -1) {
    rightspace = rightspace + leftspace;
    leftstr = leftstr.substr(leftspace);
    leftspace = leftstr.search(/\S/gi);
    if (leftspace != -1) {
      rightspace = rightspace + leftspace;
      leftstr = leftstr.substr(leftspace);
      leftspace = leftstr.search(/\s/gi);
      endspace = 0;
    } else {
      endspace = 1;
    }
  }

  if ((endspace != 0) && (rightspace > 0)) {
    datastr = datastr.substring(0, rightspace);
  }
  
  return datastr;
}

function setText(objName,sStr)						//设置下拉框TEXT
{

//	alert("aaa");
	//var objName=eval(objNameold);
	//alert(objName.value);
 	 //2003 /6/ 10 HADES
	 j=document.all.item(objName).length;
	ObjID=GetObjID(objName);
	for(i=0;i<j;i++)
	{
//			alert(i);
	     if (trimall(document.form1.elements[ObjID].options[i].text)==trimall(sStr) )
		{
		//	alert(sStr);
		   document.form1.elements[ObjID].options[i].selected =true ;
		}
	}
//	return false;
}

function isDate(year,month,day,sStr)						//是否是有效的日期值
{
	//alert(document.all.item(year).value);
	if (!isNumeric(year,sStr))								//检验输入的年份是否是数值类型的		
		return false;

	if (parseInt(document.all.item(year).value)<1000)		//检验输入的年份是否是四位
	{
		if (sStr!="")										//输入的年份不是四位，则检验是否要提示错误
		{
			alert(sStr);									//提示错误
			document.all.item(year).select();				//选择出错位置
		}
		return false;
	}

	if (!isNumeric(month,sStr))
		return false;

	if (parseInt(document.all.item(month).value)<=0 || parseInt(document.all.item(month).value)>12) //检验输入的月份是否合法
	{
		if (sStr!="")										//输入的月份不合法，则检验是否要提示错误
		{
			alert(sStr);									//提示错误
			document.all.item(month).select();				//选择出错位置
		}
		return false;
	}

	if (!isNumeric(day,sStr))
		return false;

	if (parseInt(document.all.item(day).value)<=0 || parseInt(document.all.item(day).value)>31)	//检验输入的日期是否合法
	{
		if (sStr!="")										//输入的日期不合法，则检验是否要提示错误
		{
			alert(sStr);									//提示错误
			document.all.item(day).select();				//选择出错位置
		}
		return false;
	}
	return true;
}

function isMailBox(objName,sStr)							//是否是有效的Mail地址
{
	var data;
	data=document.all.item(objName).value;				

	if (data.indexOf('@')==-1)								//查找字符串中的'@'
	{
		if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
		return false;
	}

	var i;
	i=data.indexOf('@');									//标示出字符串中的'@'的位置

	if (data.indexOf('.',i)==-1 || data.lastIndexOf('.')==data.length-1) //查找出'.'的位置且位置不为最后一位
	{
		if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
		return false;
	}

	return true;
}

function isNumeric(objName,sStr)						   //是否是有效的数值（一）入口参数为对象名称
{
	if (isNaN(document.all.item(objName).value) || trim(document.all.item(objName).value)=="")
	{
		if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
		return false;
	}
	return true;
}

function isNumeric2(data,sStr)							//是否是有效的数值（二）入口参数为字符串
{
	data=trim(data);
	if (isNaN(data) || data=="")
	{
		if (sStr!="") 
			alert(sStr);
		return false;
	}
	return true;
}


function isPostID(objName,sStr)							//是否是有效的身份证代码
{
	//if (!isNumeric(objName,sStr)) return false;
	if (document.all.item(objName).value.length==15 || document.all.item(objName).value.length==18)
		return true;
	if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
	return false;
}

function isNull(objName,sStr)							//是否为空（一）入口参数为对象名称
{
	if (trim(document.all.item(objName).value)=="")
	{
		if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
		return false;
	}
	return true;
}

function isNull2(data,sStr)								//是否为空（二）入口参数为字符串
{
	data=trim(data)
	if (data=="")
	{
		if (sStr!="") 
			alert(sStr);
		return false;
	}
	return true;
}


function GetObjID(ObjName)							  //得到对象名称所对应的数组下标
{ for ( var ObjID=0; ObjID < window.form1.elements.length; ObjID++ )
      if (window.form1.elements[ObjID].name == ObjName )
      {  return(ObjID);
         break;
      }
  return(-1);
}

function isSelected(objName,sStr)					  //是否是有选项被选中
{
	j=document.all.item(objName).length;
	ObjID=GetObjID(objName);
	for(i=1;i<j;i++)
	{
		if (document.form1.elements[ObjID].options[i].selected)
			return true;
	}
	if (sStr!="") alert(sStr);
	document.all.item(objName).focus();
	return false;
}

function isTSelected(objName,sStr)					//是否是有选项被选中
{
	j=document.all.item(objName).length;
	ObjID=GetObjID(objName);
	for(i=1;i<j;i++)
	{
		if (document.form1.elements[ObjID].options[i].selected && document.form1.elements[ObjID].options[i].value!=-1)
			return true;
	}
	if (sStr!="") alert(sStr);
	document.all.item(objName).focus();
	return false;
}

function isLength(objName,num,sStr)				  //长度是否等于指定长度
{
	var tmpstr
	tmpstr=trim(document.all.item(objName).value)
	if (tmpstr.length!=num)
	{
		if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
		return false;
	}
	return true;
}

function maxLength(objName,num,sStr)			//长度是否小于等于指定长度
{
	var tmpstr
	tmpstr=trim(document.all.item(objName).value);
	if (tmpstr.length<num)
	{
		if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
		return false;
	}
	return true;
}

function maxValue(objName,min,max,sStr)		  //输入的数值是否在指定的范围内
{
	if (!isNumeric(objName,sStr))
		return false;
	if (parseFloat(document.all.item(objName).value)<min || parseFloat(document.all.item(objName).value)>max)
	{
		if (sStr!="") 
		{
			alert(sStr);
			document.all.item(objName).select();
		}
		return false;
	}
	return true;
}

function isChecked(objName,sStr)			//是否有选项选中
{
	if (document.all.item(objName).checked==false)
	{
		if (sStr!="") alert(sStr);
		return false;
	}
	return true;
}

function isChecked2(objName,i,sStr)		   //是否有选项或有指定的选项选中（数组）
{
	var j,flag,sFlag;
	j=document.all.item(objName).length;
	if (j==null) return isChecked(objName,sStr)
	flag=false;
	sFlag=false;
	if (i==-1) sFlag=true;
	for (w=0;w<j;w++)
	{
		if (document.all.item(objName)[w].checked==true)
			flag=true;
		if (w==i && document.all.item(objName)[w].checked==true)
			sFlag=true;
	}
	if (!(flag & sFlag))
	{
		if (sStr!="") alert(sStr);
		return false;
	}

	return true;
}

function isChecked3(objName,sStr)		   //是否有选项或有指定的选项选中（数组）
{
	var j,flag,sFlag;
	j=document.all.item(objName).length;
	for (w=0;w<j;w++)
	{
		if (document.all.item(objName)[w].checked==true)
			return true;
	}
	if (sStr!="") alert(sStr);
		return false;
	return false;
}


function ltrim(str)
{
    var flag
	flag=false;
	var i,j
	var tmpstr
	tmpstr=str;
	i=0;
	if (str.length==0) return "";
	for (i=0;i<str.length;i++)
	{
		if (str.charAt(i)!=" ")
			flag=true;
		if (!flag && str.charAt(i)==" ")
			tmpstr=str.substring(i+1,j);
	}
	return tmpstr;
}


function rtrim(str)
{
    var flag
	flag=true;
	var i,j
	var tmpstr
	tmpstr=str;
	i=0;
	if (str.length==0) return "";
	for (i=0;i<str.length;i++)
	{
		if (str.charAt(i)!=" ")
			flag=false;
		if (!flag && str.charAt(i)==" ")
			tmpstr=str.substring(0,i-1);
	}
	if (flag) tmpstr=""	
	return tmpstr;
}

function trim(str)
{
	var tmpstr
	tmpstr=ltrim(str)
	return rtrim(tmpstr);
}

function IsChinese(str,sStr)  
{
	if(escape(str).indexOf("%u")!=-1)
	  	return true;
	else
		return false;
}

function isPassword(obj1,obj2)
{
    if (!isNull(obj1,"口令不能为空")) return false;
	if (!isNull(obj2,"确认口令不能为空")) return false;
	strPassword=document.all.item(obj1).value;
    for (pwIndex=0; pwIndex<strPassword.length; pwIndex++)  
    {  
        cCheck = strPassword.charAt(pwIndex);
        if ( cCheck==' ' || cCheck==':' || cCheck=='\'' || cCheck=='"' )  
        {  
            alert("口令不能使用空格和单双引号( ,',\")");  
            document.all.item(obj1).focus();  
            return false;  
        }  
    }

	if (document.all.item(obj1).value != document.all.item(obj2).value)
		{
			alert("两遍输入的口令不一致");
			document.all.item(obj1).focus();
			return false;
		}
		return true;
}

function isDateNum(year,month,day)						//是否是有效的日期值
{
	//alert(document.all.item(year).value);
	if (!isNumeric2(year,""))								//检验输入的年份是否是数值类型的		
		return -1;

	if (!isNumeric2(month,""))
		return -2;

	if (!isNumeric2(day,""))
		return -3;
	
	tmpYear=year;
    tmpMonth=month;
	tmpDay=day;

	if (tmpYear<1000 || (tmpYear%parseInt(tmpYear))!=0)		//检验输入的年份是否是四位
		return -1;

	if (tmpMonth>12 || tmpMonth<1 || (tmpMonth%parseInt(tmpMonth))!=0)	 //检验输入的月份是否合法
		return -2;
	
	if ((parseInt(tmpMonth/8)+tmpMonth)%2==1)
	{
		if (tmpDay<1 || tmpDay>31 || (tmpDay%parseInt(tmpDay))!=0)	    //检验输入的日期是否合法
			return -3;
	}
	else
	{
		if (tmpDay<1 || tmpDay>30 || (tmpDay%parseInt(tmpDay))!=0)						//检验输入的日期是否合法
			return -3;
	}

	if (tmpMonth==2)
	{
		if ((tmpYear%4==0 && tmpYear%100!=0) || (tmpYear%400==0))
		{
			if (tmpDay<1 || tmpDay>29 || (tmpDay%parseInt(tmpDay))!=0)								
				return -3;
		}
		else
		{
			if (tmpDay<1 || tmpDay>28 || (tmpDay%parseInt(tmpDay))!=0)								
				return -3;
		}
	}
	return true;
}	

function isTDate(year,month,day,sStr)						//是否是有效的日期值
{
	tmpYear=SeleVal(year);
	tmpMonth=SeleVal(month);
	tmpDay=SeleVal(day);
	tmp=isDateNum(tmpYear,tmpMonth,tmpDay);
	if (tmp==-1)
	{
		if (sStr!="") 	alert(sStr);
		document.all.item(year).focus();
		return false
	}

	if (tmp==-2)
	{
		if (sStr!="") 	alert(sStr);
		document.all.item(month).focus();
		return false
	}

	if (tmp==-3)
	{
		if (sStr!="") 	alert(sStr);
		document.all.item(day).focus();
		return false
	}
	return true;
}
function SeleVal(objName)
{
	j=document.all.item(objName).length;
	ObjID=GetObjID(objName);
	for(i=0;i<j;i++)
	{
		if (document.form1.elements[ObjID].options[i].selected)
			return document.form1.elements[ObjID].options[i].value;
	}
	return false;
}

function SeleTxt(objName)
{
	j=document.all.item(objName).length;
	ObjID=GetObjID(objName);
	for(i=0;i<j;i++)
	{
		if (document.form1.elements[ObjID].options[i].selected)
			return document.form1.elements[ObjID].options[i].text;
	}
	return false;
}

function del(url)
{
	if (confirm("是否要删除此条记录？")==true)
	{
		form1.action=url;
		form1.submit();
	}
}


function isNumeric3(objName,sStr)
{
	if (isNull(objName,"")==false) return true;
	return isNumeric(objName,sStr);	
}/*******************************************************/
/*******************************************************/
/************         报表打印           ****************/
/*******************************************************/
/*******************************************************/
	function look(formname,printname,closename,memoname)
	{
	var d,s="查询时间:";
	d=new Date();
	s+=d.getYear()+"年";
	s+=(d.getMonth()+1)+"月";
	s+=d.getDate()+"日";
	printname.value=s;
	closename.value="";
	memoname.value=""
	window.print();
	printname.value="打印";
	closename.value="关闭窗口";
	memoname.value="查看备注内容"
	}
	function lookmemo(formname,printname,closename)
	{
	var d,s="查询时间:";
	d=new Date();
	s+=d.getYear()+"年";
	s+=(d.getMonth()+1)+"月";
	s+=d.getDate()+"日";
	printname.value=s;
	closename.value="";
	window.print();
	printname.value="打印";
	closename.value="关闭窗口";
	}

	
	

