/*
 * 将数字转换为中文大写格式
 * Joakim
 */
function setFormatCC(n) {  //金额大写转换函数
    if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
        return "数据非法";
    var unit = "千百拾亿千百拾万千百拾元角分"
    var str = "";
    n += "00";
    var p = n.indexOf('.');
    if (p >= 0)
        n = n.substring(0, p) + n.substr(p+1, 2);
    unit = unit.substr(unit.length - n.length);
    for (var i=0; i < n.length; i++)
        str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
    var returnVal =  str.replace(/零(千|百|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整");
    //alert(returnVal);
    return returnVal;
}

/**
function formatCC(p_amount){
     var rtstr = "",tmpstr = "";
     var knum;
     var tmpint;
     if(p_amount > 999999999999.99)
       return "数字太大，无法转换！";
     if(p_amount / 10000 >= 1){
       knum = p_amount % 10000;
       p_amount = (p_amount - knum)/10000;

       tmpstr = formatCC(p_amount);
       if(tmpstr != null && tmpstr.length != 0)
         rtstr += tmpstr + "万";

       rtstr += formatCC(knum);
     }else{
       tmpint = parseInt(p_amount / 1000);
       if(tmpint >= 1)
         rtstr += numToCC(tmpint)+ "仟";
       else{
         if(rtstr.length == 0)
           rtstr += "零";
       }

       p_amount = p_amount - tmpint * 1000;
       tmpint = parseInt(p_amount / 100);
       if(tmpint >= 1)
         rtstr += numToCC(tmpint)+ "佰";
       else{ 
         if(rtstr.length == 0 || !(rtstr.substring(rtstr.length -1 ,rtstr.length) == "零")){
           rtstr += "零";
     }
       }
       p_amount = p_amount - tmpint * 100;
       tmpint = parseInt(p_amount / 10);
       if(tmpint >= 1)
         rtstr += numToCC(tmpint)+ "拾";
       else{   
         if(rtstr.length == 0 || !(rtstr.substring(rtstr.length -1 ,rtstr.length) == "零"))
           rtstr += "零";
       }

       p_amount = p_amount - tmpint * 10;
       tmpint = parseInt(p_amount / 1);
       if(tmpint >= 1)
         rtstr += numToCC(tmpint);

       p_amount = p_amount - tmpint;
       if(p_amount > 0){
     if(rtstr.indexOf("零") == rtstr.length - 1)
           rtstr = rtstr.substring(0,rtstr.length - 1);
         rtstr += "圆";
    }
       if(p_amount > 0.009){

         p_amount = p_amount * 100;
         tmpint = parseInt(p_amount / 10);
         if(tmpint >= 1)
           rtstr += numToCC(tmpint)+ "角";
         p_amount = p_amount - tmpint * 10;
         tmpint = parseInt(p_amount / 1);
         if(p_amount - tmpint > 0.5)
           tmpint++;
         if(tmpint >= 1)
           rtstr += numToCC(tmpint) + "分";
       }
     }
     return rtstr;
   }
function setFormatCC(p_amount){
     var amount=0;
     var rtstr;
     amount=parseFloat(p_amount);
     amount += 0.0001;
     rtstr = formatCC(amount);
     if(rtstr.lastIndexOf("万") != rtstr.indexOf("万")){
       rtstr = rtstr.substring(0,rtstr.indexOf("万"))+"亿"+rtstr.substring(rtstr.indexOf("万")+1,rtstr.length);
     }
     if(rtstr.indexOf("零") == 0)
       rtstr = rtstr.substring(1,rtstr.length);
     rtstr += "整";
     return rtstr;
   }
function numToCC(i){
     switch(i){
       case 1 : return "壹";
       case 2 : return "贰";
       case 3 : return "叁";
       case 4 : return "肆";
       case 5 : return "伍";
       case 6 : return "陆";
       case 7 : return "柒";
       case 8 : return "捌";
       case 9 : return "玖";
       case 0 : return "零";
       default: return "";
     }
} */