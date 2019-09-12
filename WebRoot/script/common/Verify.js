//---------------------------------------进仓单模块验证
function VerifierIncome(paramForm,totalrownums)
  {
    if (isNaN(paramForm.G_f_Total_Num.value)) 
	  {	
		alert("总件数必须是数值型");
		paramForm.G_f_Total_Num.select();
		return false;
      }
	if ((paramForm.TotalRow.value)=="")
		  {	
			alert("至少要添加一种商品");
			return false;
		  }	
    if(paramForm.G_v_Bargin_Num.value=="") 
	    {   //2003/6/7  合同号非必填    
		//	alert("必须选择一个合同");
		//	window.open("","popupnav","top=150,left=220,width=310,height=310,resizable=0").location.href ="../common/Bargin_INFO_cx.asp"
		//	return false;
	    }
	if(paramForm.TotalRow.value=="1")
	   if(paramForm.Goods_Name1.value=="") 
	    {    
			alert("至少要选择一种商品");
			window.open("","popupnav","top=150,left=220,width=310,height=310,resizable=0").location.href ="../common/GOODS_INFO_cx.asp?rowNum=1"
			return false;
	    }
	if(isNaN(paramForm.TotalCase.value) && paramForm.TotalCase.value!="")
      {
        alert("总箱数必须是数值型");
        paramForm.TotalCase.select();
        return false;
      }
    
    if(isNaN(paramForm.TotalAW.value) && paramForm.TotalAW.value!="")
      {
        alert("总毛重必须是数值型");
        paramForm.TotalAW.select();
        return false;
      }
    
	for(i=1;i<=totalRownums;i++)
	  {
	    //alert(document.all.item("Goods_NW" + parseInt(i)).value + "->" + i);
	    //alert(isNaN(document.all.item("Goods_NW" + parseInt(i)).value));
	    if(isNaN(parseInt(document.all.item("Goods_QTY" + parseInt(i)).value))||document.all.item("Goods_QTY" + parseInt(i)).value==""||parseInt(document.all.item("Goods_QTY" + parseInt(i)).value)==0)
	      {
	        alert("第" + i + "个商品的数量必须是一个不为零的整数");
	        document.all.item("Goods_QTY" + parseInt(i)).select();
	        return false;
	      }
	    /*if(isNaN(parseInt(document.all.item("Goods_JH" + parseInt(i)).value))||document.all.item("Goods_JH" + parseInt(i)).value==""||parseInt(document.all.item("Goods_JH" + parseInt(i)).value)==0)
	      {
	        //alert(document.all.item("Goods_JH" + parseInt(i).value));
	        alert("第" + i + "个商品的件号必须是一个不为零的整数");
	        document.all.item("Goods_JH" + parseInt(i)).select();
	        return false;
	      }*/
	      
	      if(isNaN(document.all.item("Goods_NW" + parseInt(i)).value)||trim(document.all.item("Goods_NW" + parseInt(i)).value)=="")
	      {
	        alert("第" + i + "个商品的净重必须是一个不为零的整数");
	        document.all.item("Goods_NW" + parseInt(i)).select();
	        return false;
	      }
	      
            if(document.all.item("Goods_JH" + parseInt(i)).value=="")
	      {
	        //alert(document.all.item("Goods_JH" + parseInt(i).value));
	        alert("第" + i + "个商品的件号不能为空！");
	        document.all.item("Goods_JH" + parseInt(i)).select();
	        return false;
	      }
    }
    SubmitData(paramForm); 
  }
//------------------------------------------------

//------------------------------------------------
function SubmitData(paramForm)
  {
    paramForm.submit();
  }
function trim(str)
{
	var reg=/ /ig;  //规则表达式
	return str.replace(reg,"");
}
