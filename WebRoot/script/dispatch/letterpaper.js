function fInitHTMLStationery(){
	if(!IsIE) return false;
	var destObj = document.getElementById("selHTMLStationery");
	var srcObj	= document.getElementById("selSrcStationery");

	var j = 0;

	for(var i=0;i<srcObj.length;i++){
		if(srcObj.options[i].text != "" && srcObj.options[i].value !=""){
			destObj.options[j]=new Option(srcObj.options[i].text,srcObj.options[i].value);
			j++;
		}				
	}
}
function fHTMLStationery(){
	if(!ishtml){
		changeEditorEx();
	}
	var destObj = document.getElementById("selHTMLStationery");
	var srcObj	= document.getElementById("selSrcStationery");	
	var s = destObj.options[destObj.selectedIndex].value;

	for(var i=0;i<srcObj.length;i++){
		if(srcObj.options[i].value == s){
			srcObj.options[i].selected = true;
			fStationery();
			break;
		}				
	}
}

function fStationery(){
//Added by sanlly :2004-2-27 , in order to change stationery for the background of the letter.
	var sValue = document.sendmail.stationery.options[document.sendmail.stationery.selectedIndex].value ;

	if (document.sendmail.zStationery.value == "" && sValue == "0") return;

	var sStationeryBG = "";
	var sFirstContent; 
	var sS1 ="";
	var sS2 ="";
	var sS3 ="";
	var stemp = "";
	var arrtemp;

	var sFirstHtml = document.sendmail.htmlletter.html;
	if (sFirstHtml =="" ){
		sFirstContent = document.all.lettercontent.value;
	}else{
		sFirstContent = sFirstHtml;
	}

	if (document.sendmail.zStationery.value == ""){
		sS2 ="亲爱的朋友：<br>　　您好！<br>"+ sFirstContent +"<br><br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致<br>礼！<br><br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ uidatdomain +"<br>";
	}else if(document.sendmail.zStationery.value==0){
		sS2 = sFirstContent;
	}else{
		stemp = sFirstContent;
		arrtemp = stemp.split("<STATIONERY>");
		sS2 = arrtemp[1];
		if (sS2 == "" || sS2 == "undefined"){
			sS2 = sFirstContent;
		}
	}
	
	switch (sValue){
		case "0":
			sStationeryBG = sS2;
			break;
		case "1":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"97\" background=\"http://mimg.163.com/stationery/20050204/newyear_1_top1_1.jpg\" height=\"134\">&nbsp;</td>    <td width=\"136\" background=\"http://mimg.163.com/stationery/20050204/newyear_1_top1_2.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/newyear_1_top_bg.jpg\">&nbsp;</td>    <td width=\"148\" background=\"http://mimg.163.com/stationery/20050204/newyear_1_top1_3.jpg\">&nbsp;</td>    <td width=\"82\" background=\"http://mimg.163.com/stationery/20050204/newyear_1_top1_4.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFE1\">  <tr>    <td width=\"94\" height=\"20\">&nbsp;</td>    <td width=\"100\" background=\"http://mimg.163.com/stationery/20050204/newyear_1_top2_1.jpg\">&nbsp;</td>    <td>&nbsp;</td>    <td width=\"105\" background=\"http://mimg.163.com/stationery/20050204/newyear_1_top2_2.jpg\">&nbsp;</td>    <td width=\"52\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFE1\">  <tr>    <td width=\"100\"></td>    <td> <div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #EC701E;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY></div></td>    <td width=\"80\">&nbsp;</td>  </tr></table><table width=\"99%\" height=\"85\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" background=\"http://mimg.163.com/stationery/20050204/newyear_1_botflower_bg.gif\">  <tr>    <td>&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "2":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"167\" height=\"157\" background=\"http://mimg.163.com/stationery/20050204/newyear_2_top1.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/newyear_2_top_bg.jpg\">&nbsp;</td>    <td width=\"206\" valign=\"bottom\" background=\"http://mimg.163.com/stationery/20050204/newyear_2_top2.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FDF1D7\">  <tr>    <td width=\"36\">&nbsp;</td>    <td width=\"137\" height=\"50\" background=\"http://mimg.163.com/stationery/20050204/newyear_2_hi.jpg\">&nbsp;</td>    <td>&nbsp;</td>    <td width=\"200\" background=\"http://mimg.163.com/stationery/20050204/newyear_2_top3.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FDF1D7\">  <tr>    <td width=\"60\">&nbsp;</td>    <td><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #EC701E;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY></div></td>    <td width=\"60\">&nbsp;</td>  </tr></table><table width=\"99%\" height=\"4\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FF9900\">  <tr>    <td></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "3":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"190\" height=\"87\" background=\"http://mimg.163.com/stationery/20050204/newyear_3_top1_1.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/newyear_3_top1_bg.jpg\">&nbsp;</td>    <td width=\"210\" background=\"http://mimg.163.com/stationery/20050204/newyear_3_top1_2.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <TR>    <TD width=\"143\" background=\"http://mimg.163.com/stationery/20050204/newyear_3_left.jpg\"></TD>    <TD bgcolor=\"#E9E9E9\"><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #333333;text-align:left;\"><STATIONERY>"



            sS3 ="<STATIONERY> </div></TD>    <TD width=\"156\" background=\"http://mimg.163.com/stationery/20050204/newyear_3_right.jpg\">&nbsp;</TD>  </TR></TABLE><table width=\"99%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">  <tr>    <td width=\"280\" height=\"196\" background=\"http://mimg.163.com/stationery/20050204/newyear_3_bot_left.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/newyear_3_botbg.jpg\">&nbsp;</td>    <td width=\"121\" background=\"http://mimg.163.com/stationery/20050204/newyear_3_bot_right.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "4":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#F3E7E1\">  <tr>    <td align=\"left\" bgcolor=\"#FFFFFF\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_4_top1.jpg\" width=\"217\" height=\"141\"></td>    <td align=\"right\" bgcolor=\"#FFFFFF\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_4_top2.gif\" width=\"230\" height=\"141\"></td>  </tr>  <tr>    <td height=\"5\" colspan=\"2\" bgcolor=\"#B95840\"></td>  </tr>  <tr>    <td colspan=\"2\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_4_dear.gif\" width=\"67\" height=\"34\"></td>  </tr>  <tr>    <td colspan=\"2\"><TABLE width=\"85%\" align=\"center\"><TR>	<TD><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #AC3209;text-align:left;\"><STATIONERY>"
            sS3 =" <STATIONERY></div></TD></TR></TABLE>   <br>	</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "5":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFF4E1\">  <tr bgcolor=\"#FEEBC7\">    <td rowspan=\"2\" align=\"left\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_3_cup.gif\" width=\"144\" height=\"134\"></td>    <td height=\"77\" align=\"right\" valign=\"bottom\" bgcolor=\"#FEEBC7\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_3_top1.gif\" width=\"356\" height=\"38\"></td>  </tr>  <tr>    <td height=\"57\" align=\"right\" bgcolor=\"#FEEBC7\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_3_top2.gif\" width=\"391\" height=\"57\"></td>  </tr>  <tr>    <td colspan=\"2\"><br><TABLE width=\"85%\" align=\"center\"><TR>	<TD>    <div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #79452F;text-align:left;\"><STATIONERY>"
            sS3 =" <STATIONERY></div></TD></TR></TABLE>	</td>  </tr>  <tr>    <td align=\"left\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_1_flower.gif\" width=\"96\" height=\"90\"></td>    <td></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "6":
            sS1 ="<table width=\"99%\" align=center border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#F5E0BA\">  <tr>    <td rowspan=\"2\" align=\"left\" valign=\"middle\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_1_top_cha.jpg\" width=\"177\" height=\"73\"></td>    <td height=\"87\" align=\"right\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_1_top1.jpg\" width=\"335\" height=\"87\"></td>  </tr>  <tr>    <td height=\"72\" align=\"right\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_1_top2.jpg\" width=\"335\" height=\"72\"></td>  </tr>  <tr bgcolor=\"#BC8870\">    <td height=\"5\" colspan=\"2\"></td>  </tr>  <tr>    <td colspan=\"2\" ><TABLE width=\"85%\" align=\"center\"><TR>	<TD>	<div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #79452F;text-align:left;\"><STATIONERY>"
            sS3 =" <STATIONERY></div></TD></TR></TABLE>	</td>  </tr>  <tr>    <td colspan=\"2\" align=\"left\" valign=\"bottom\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_1_flower.gif\" width=\"96\" height=\"90\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "7":
            sS1 ="<table width=\"99%\" align=center border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFF4C6\">  <tr>    <td rowspan=\"2\" align=\"left\" valign=\"middle\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_2_top_cha.jpg\" width=\"177\" height=\"73\"></td>    <td height=\"87\" align=\"right\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_2_top1.jpg\" width=\"335\" height=\"87\"></td>  </tr>  <tr>    <td height=\"74\" align=\"right\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_2_top2.jpg\" width=\"335\" height=\"74\"></td>  </tr>  <tr bgcolor=\"#BC8870\">    <td height=\"5\" colspan=\"2\"></td>  </tr>  <tr>    <td colspan=\"2\"><br><TABLE width=\"85%\" align=\"center\"><TR>	<TD>    <div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #79452F;text-align:left;\"><STATIONERY>"
            sS3 =" <STATIONERY></div></TD></TR></TABLE>   </td>  </tr>  <tr>    <td colspan=\"2\" align=\"left\" valign=\"bottom\"><img src=\"http://mimg.163.com/stationery/20050204/loveday_1_flower.gif\" width=\"96\" height=\"90\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "8":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td align=\"center\" background=\"http://mimg.163.com/stationery/20050204/classmate_2_top1_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_3_top1.jpg\" width=\"496\" height=\"75\"></td>  </tr>  <tr>    <td align=\"center\" background=\"http://mimg.163.com/stationery/20050204/classmate_2_top2_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_3_top2.jpg\" width=\"496\" height=\"83\"></td>  </tr>  <tr>    <td><table width=\"85%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td><br>	<div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #333333;text-align:left;\"><STATIONERY>"
            sS3 =" <STATIONERY></div>	</td>  </tr></table>	</td>  </tr>  <tr>    <td align=\"center\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_3_bot_cha.jpg\" width=\"186\" height=\"24\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "9":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td align=\"center\" background=\"http://mimg.163.com/stationery/20050204/classmate_2_top1_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_2_top1.jpg\" width=\"474\" height=\"75\"></td>  </tr>  <tr>    <td align=\"center\" background=\"http://mimg.163.com/stationery/20050204/classmate_2_top2_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_2_top2.jpg\" width=\"474\" height=\"83\"></td>  </tr>  <tr>    <td><table width=\"85%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td><br>	<div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #333333;text-align:left;\"><STATIONERY>"
            sS3 =" <STATIONERY></div>	</td>  </tr></table>	</td>  </tr>  <tr>    <td align=\"center\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_2_bot_cha.jpg\" width=\"232\" height=\"24\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "10":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"50%\" background=\"http://mimg.163.com/stationery/20050204/classmate_1_top_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_1_top1_1.jpg\" width=\"199\" height=\"151\"></td>    <td width=\"50%\" align=\"right\" valign=\"bottom\" background=\"http://mimg.163.com/stationery/20050204/classmate_1_top_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_1_top2_2.jpg\" width=\"117\" height=\"66\"></td>  </tr>  <tr>    <td colspan=\"2\">	<table width=\"80%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td><br>	<div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #666666;text-align: left;\"><STATIONERY>"
            sS3 =" <STATIONERY></div>	</td>  </tr></table>	</td>  </tr>  <tr align=\"left\" >    <td colspan=\"2\" background=\"http://mimg.163.com/stationery/20050204/classmate_1_bot_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/classmate_1_bot_left.jpg\" width=\"58\" height=\"57\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "11":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"254\" height=\"191\" background=\"http://mimg.163.com/stationery/20050204/birthday_2_top1.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/birthday_2_top_bg.jpg\">&nbsp;</td>    <td width=\"236\" background=\"http://mimg.163.com/stationery/20050204/birthday_2_top2.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FDFDF7\">  <TR>    <TD width=\"60\"></TD>    <TD><br><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #000000;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div><br><br></TD>    <TD width=\"60\">&nbsp;</TD>  </TR></TABLE>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "12":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"26\" height=\"26\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_top_left.jpg\">&nbsp;</td>    <td align=\"right\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_top_bg.jpg\">&nbsp;</td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_top_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#EAAB76\">  <tr>    <td width=\"26\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_left.jpg\">&nbsp;</td>    <td align=\"left\" valign=\"top\"><img src=\"http://mimg.163.com/stationery/20050204/birthday_1_star.jpg\" width=\"112\" height=\"67\"></td>    <td width=\"175\" valign=\"top\"><img src=\"http://mimg.163.com/stationery/20050204/birthday_1_cha.jpg\" width=\"175\" height=\"78\"></td>    <td width=\"142\"><img src=\"http://mimg.163.com/stationery/20050204/birthday_1_deer.jpg\" width=\"142\" height=\"153\"></td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#EAAB76\">  <tr>    <td width=\"26\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_left.jpg\">&nbsp;</td>    <td width=\"30\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/birthday_1_cen_bg.jpg\"><div id=\"here\" style=\"font-size: 11pt;line-height: 35px;color: #000000;text-align:left;\"><STATIONERY>"

            sS3 ="<STATIONERY></div></td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#EAAB76\">  <tr>    <td width=\"26\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_left.jpg\">&nbsp;</td>    <td align=\"left\"><img src=\"http://mimg.163.com/stationery/20050204/birthday_1_star.jpg\" width=\"112\" height=\"67\"></td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"26\" height=\"32\" align=\"left\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_bot_left.jpg\">&nbsp;</td>    <td align=\"right\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_bot_bg.jpg\">&nbsp;</td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/birthday_1_bot_right.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "13":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"178\" height=\"114\" background=\"http://mimg.163.com/stationery/20050204/unsort_2_top1.jpg\">&nbsp;</td>    <td width=\"205\" background=\"http://mimg.163.com/stationery/20050204/unsort_2_top2.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/unsort_2_topbg.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" bgcolor=\"#EFF0E2\">  <TR>    <TD width=\"60\">&nbsp;</TD>    <TD background=\"http://mimg.163.com/stationery/20050204/unsort_2_cen_bg.gif\"><div id=\"here\" style=\"font-size: 11pt;line-height: 34px;color: #666666;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></TD>    <TD width=\"60\">&nbsp;</TD>  </TR></TABLE><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#EFF0E2\">  <tr>    <td background=\"http://mimg.163.com/stationery/20050204/unsort_2_bot_bg.gif\">&nbsp;</td>    <td width=\"409\" background=\"http://mimg.163.com/stationery/20050204/unsort_2_bot.jpg\" height=\"66\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "14":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td background=\"http://mimg.163.com/stationery/20050204/activepaper_2_top1_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/activepaper_2_top1_1.jpg\" width=\"273\" height=\"96\"><img src=\"http://mimg.163.com/stationery/20050204/activepaper_2_top1_2.jpg\" width=\"226\" height=\"96\"></td>  </tr>  <tr>    <td><br>	<table width=\"90%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"30\"> </td>    <td  background=\"http://mimg.163.com/stationery/20050204/activepaper_2_cen_bg.jpg\"><div id=\"here\" style=\"font-size: 11pt;line-height: 28px;color: #447BA4;text-align:left; \"><STATIONERY>"
            sS3 =" <STATIONERY></div></td>  </tr></table>	</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "15":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"36\" height=\"67\" background=\"http://mimg.163.com/stationery/20050204/activepaper_1_top1_1.jpg\">&nbsp;</td>    <td width=\"38\" background=\"http://mimg.163.com/stationery/20050204/activepaper_1_top1_2.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/activepaper_1_top1_bg.jpg\">&nbsp;</td>    <td width=\"159\" background=\"http://mimg.163.com/stationery/20050204/activepaper_1_top1_3.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FAFAFA\">  <tr>    <td width=\"36\" background=\"http://mimg.163.com/stationery/20050204/activepaper_1_left.jpg\">&nbsp;</td>    <td width=\"30\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/activepaper_1_cen_bg.jpg\"><div id=\"here\" style=\"font-size: 11pt;line-height: 28px;color: #333333;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></td>    <td width=\"21\" background=\"http://mimg.163.com/stationery/20050204/activepaper_1_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"36\" height=\"44\" background=\"http://mimg.163.com/stationery/20050204/activepaper_1_bot_left.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/activepaper_1_bot_bg.jpg\">&nbsp;</td>    <td width=\"21\" background=\"http://mimg.163.com/stationery/20050204/activepaper_1_bot_right.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "16":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"213\" height=\"128\" background=\"http://mimg.163.com/stationery/20050204/season_6_top1_1.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/season_6_top1_bg.jpg\">&nbsp;</td>    <td width=\"28\" valign=\"top\" background=\"http://mimg.163.com/stationery/20050204/season_6_top1_2.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <TR>    <TD width=\"48\" background=\"http://mimg.163.com/stationery/20050204/season_6_left.jpg\">&nbsp;</TD>    <TD width=\"20\">&nbsp;</TD>    <TD><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #666666;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></TD>    <TD width=\"28\" background=\"http://mimg.163.com/stationery/20050204/season_6_right.jpg\">&nbsp;</TD>  </TR></TABLE><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"48\" height=\"164\" valign=\"bottom\" background=\"http://mimg.163.com/stationery/20050204/season_6_bot_left.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/season_6_bot_bg.jpg\">&nbsp;</td>    <td width=\"133\" background=\"http://mimg.163.com/stationery/20050204/season_6_bot_right.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "17":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"111\" height=\"95\" valign=\"top\" background=\"http://mimg.163.com/stationery/20050204/season_5_top1_1.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/season_5_top1_bg.jpg\">&nbsp;</td>    <td width=\"292\" background=\"http://mimg.163.com/stationery/20050204/season_5_top1_2.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"111\" background=\"http://mimg.163.com/stationery/20050204/season_5_left.jpg\">&nbsp;</td>    <td bgcolor=\"#FCFFF6\">      <div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #666666;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></td>    <td width=\"59\" background=\"http://mimg.163.com/stationery/20050204/season_5_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"216\" height=\"95\" background=\"http://mimg.163.com/stationery/20050204/season_5_bot_left.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/season_5_bot_bg.jpg\">&nbsp;</td>    <td width=\"151\" background=\"http://mimg.163.com/stationery/20050204/season_5_bot_right.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "18":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td align=\"left\" background=\"http://mimg.163.com/stationery/20050204/season_4_top_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/season_4_top1.jpg\" width=\"504\" height=\"140\"></td>  </tr>  <tr>    <td><TABLE width=\"85%\" align=\"center\"><TR>	<TD>	<div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #666666;text-align:left;\"><STATIONERY> "
            sS3 =" <STATIONERY></div></TD></TR></TABLE>	</td>  </tr>  <tr>    <td align=\"left\" background=\"http://mimg.163.com/stationery/20050204/season_4_bot_bg.gif\"><img src=\"http://mimg.163.com/stationery/20050204/season_4_bot.jpg\" width=\"467\" height=\"51\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "19":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td height=\"134\" background=\"http://mimg.163.com/stationery/20050204/season_3_top_bg.jpg\">&nbsp;</td>    <td width=\"157\" background=\"http://mimg.163.com/stationery/20050204/season_3_top1.jpg\">&nbsp;</td>    <td width=\"175\" background=\"http://mimg.163.com/stationery/20050204/season_3_top2.jpg\">&nbsp;</td>    <td width=\"168\" background=\"http://mimg.163.com/stationery/20050204/season_3_top3.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/season_3_top_bg.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" bgcolor=\"#F2F2F2\">  <TR>    <TD width=\"60\">&nbsp;</TD>    <TD><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #CC7A00;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></TD>    <TD width=\"60\">&nbsp;</TD>  </TR></TABLE><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#F2F2F2\">  <tr>    <td width=\"72\" height=\"71\" background=\"http://mimg.163.com/stationery/20050204/season_3_le.jpg\">&nbsp;</td>    <td>&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "20":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td background=\"http://mimg.163.com/stationery/20050204/season_2_top_bg.jpg\">&nbsp;</td>    <td width=\"248\" height=\"166\" background=\"http://mimg.163.com/stationery/20050204/season_2_top1.jpg\">&nbsp;</td>    <td width=\"233\" background=\"http://mimg.163.com/stationery/20050204/season_2_top2.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" bgcolor=\"#FFFFFF\">  <TR>    <TD width=\"60\">&nbsp;</TD>    <TD><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #666666;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></TD>    <TD width=\"60\">&nbsp;</TD>  </TR></TABLE><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"350\" background=\"http://mimg.163.com/stationery/20050204/season_2_bot_bg.jpg\">&nbsp;</td>    <td width=\"26\" height=\"42\" background=\"http://mimg.163.com/stationery/20050204/season_2_bot.jpg\">&nbsp;</td>    <td>&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "21":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr align=\"left\" valign=\"bottom\">    <td height=\"149\" background=\"http://mimg.163.com/stationery/20050204/season_1_top_big.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/season_1_sanj.jpg\" width=\"372\" height=\"63\"></td>  </tr>  <tr>    <td><TABLE width=\"85%\" align=\"center\"><TR> 	<TD>	<div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #666666;text-align:left;\"><STATIONERY> "
            sS3 =" <STATIONERY></div></TD></TR></TABLE>	</td>  </tr>  <tr align=\"left\">    <td background=\"http://mimg.163.com/stationery/20050204/season_1_bot_bg.gif\" ><img src=\"http://mimg.163.com/stationery/20050204/season_1_bot.gif\" width=\"420\" height=\"55\"></td>  </tr></table> "
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "22":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"94\" height=\"60\" background=\"http://mimg.163.com/stationery/20050204/girl_4_top1_1.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/girl_4_top_bg.jpg\">&nbsp;</td>    <td width=\"72\" height=\"60\" background=\"http://mimg.163.com/stationery/20050204/girl_4_top1_2.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <TR>    <TD width=\"52\" background=\"http://mimg.163.com/stationery/20050204/girl_4_left.jpg\">&nbsp;</TD>    <TD width=\"23\">&nbsp;</TD>    <TD><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #666666;text-align:left;\">"
            sS3 ="</div></TD>    <TD width=\"24\" background=\"http://mimg.163.com/stationery/20050204/girl_4_right.jpg\">&nbsp;</TD>  </TR></TABLE><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"94\" height=\"163\" background=\"http://mimg.163.com/stationery/20050204/girl_4_bot_left.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/girl_4_bot_bg.jpg\">&nbsp;</td>    <td width=\"173\" background=\"http://mimg.163.com/stationery/20050204/girl_4_bot_right.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "23":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"62\" height=\"86\" background=\"http://mimg.163.com/stationery/20050204/girl_3_top1_1.gif\">&nbsp;</td>    <td  background=\"http://mimg.163.com/stationery/20050204/girl_3_top1_bg.gif\">&nbsp;</td>    <td width=\"128\" background=\"http://mimg.163.com/stationery/20050204/girl_3_top1_2.gif\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"19\" background=\"http://mimg.163.com/stationery/20050204/girl_3_left.gif\">&nbsp;</td>    <td width=\"45\">&nbsp;</td>    <td><div id=\"here\" style=\"font-size: 11pt;line-height: 28px;color: #666666;text-align:left; \"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></td>    <td width=\"16\" background=\"http://mimg.163.com/stationery/20050204/girl_3_right.gif\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\" class=\"table_bg3\">  <tr>    <td width=\"62\" height=\"208\" background=\"http://mimg.163.com/stationery/20050204/girl_3_bigBot.gif\">&nbsp;</td>    <td width=\"117\" background=\"http://mimg.163.com/stationery/20050204/girl_3_bigBot2.gif\">&nbsp;</td>    <td align=\"right\" valign=\"top\" background=\"http://mimg.163.com/stationery/20050204/girl_3_bot_bg2.gif\">&nbsp;</td>    <td width=\"69\"  background=\"http://mimg.163.com/stationery/20050204/girl_3_bot2.gif\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "24":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFE1F1\">  <tr>    <td valign=\"top\"><table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">      <tr>        <td width=\"50\">&nbsp;</td>        <td>&nbsp;<br><br>            <div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #FC7295;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY></div></td>      </tr>    </table></td>    <td width=\"186\" valign=\"top\"><img src=\"http://mimg.163.com/stationery/20050204/girl_1_right_1.jpg\" width=\"186\" height=\"123\"><img src=\"http://mimg.163.com/stationery/20050204/girl_1_right_2.jpg\" width=\"186\" height=\"128\"></td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"201\" height=\"50\" background=\"http://mimg.163.com/stationery/20050204/girl_1_bot.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/girl_1_bot_bg.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "25":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"251\" height=\"179\" valign=\"top\" background=\"http://mimg.163.com/stationery/20050204/girl_2_top1.jpg\">&nbsp;</td>    <td width=\"246\" background=\"http://mimg.163.com/stationery/20050204/girl_2_top2.jpg\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/girl_2_top_bg.jpg\">&nbsp;</td>  </tr></table><TABLE width=\"99%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <TR>    <TD width=\"60\">&nbsp;</TD>    <TD> <div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #FC7295;text-align:left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></TD>    <TD width=\"60\">&nbsp;</TD>  </TR></TABLE><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"37\" height=\"41\" background=\"http://mimg.163.com/stationery/20050204/girl_2_bot.gif\">&nbsp;</td>    <td>&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "26":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"296\" height=\"181\" valign=\"top\" background=\"http://mimg.163.com/stationery/20050204/child_5_top_1.gif\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/child_5_top_bg.gif\">&nbsp;</td>    <td width=\"268\" background=\"http://mimg.163.com/stationery/20050204/child_5_top_2.gif\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"70\">&nbsp;</td>    <td><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #EC93B5;text-align: left;\"><STATIONERY>"
            sS3 ="<STATIONERY>      </div>      <br></td>    <td width=\"70\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"70\" height=\"57\" background=\"http://mimg.163.com/stationery/20050204/child_5_bot.gif\">&nbsp;</td>    <td>&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "27":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"251\" height=\"166\" background=\"http://mimg.163.com/stationery/20050204/child_1_top1_1.gif\">&nbsp;</td>    <td width=\"229\" background=\"http://mimg.163.com/stationery/20050204/child_1_top1_2.gif\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/child_1_top1_bg.gif\">&nbsp;</td>    <td width=\"17\" valign=\"top\" background=\"http://mimg.163.com/stationery/20050204/child_1_top1_3.gif\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"14\" background=\"http://mimg.163.com/stationery/20050204/child_1_left.gif\"></td>    <td width=\"50\"></td>    <td><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #669900;text-align: left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></td>    <td width=\"17\" background=\"http://mimg.163.com/stationery/20050204/child_1_right.gif\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"99\" height=\"91\" background=\"http://mimg.163.com/stationery/20050204/child_1_bot_left.gif\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/child_1_bot_bg.gif\">&nbsp;</td>    <td width=\"50\" background=\"http://mimg.163.com/stationery/20050204/child_1__bot_right1.gif\">&nbsp;</td>    <td width=\"17\" background=\"http://mimg.163.com/stationery/20050204/child_1_bot_right2.gif\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "28":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" background=\"http://mimg.163.com/stationery/20050204/child_4_top_bigbg.gif\">  <tr>    <td height=\"162\" align=\"left\" valign=\"bottom\"><img src=\"http://mimg.163.com/stationery/20050204/child_4_to.gif\" width=\"90\" height=\"84\"></td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td valign=\"top\"><table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">      <tr>        <td width=\"70\">&nbsp;</td>        <td>		<div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #FF9900;text-align: left;\"><STATIONERY>"
            sS3 ="<STATIONERY></div>  <br>          <br><br></td>      </tr>    </table>	</td>    <td width=\"148\" rowspan=\"2\" valign=\"bottom\"><img src=\"http://mimg.163.com/stationery/20050204/child_4_bot_right.jpg\" width=\"148\" height=\"242\"></td>  </tr>  <tr>    <td height=\"129\" valign=\"bottom\" background=\"http://mimg.163.com/stationery/20050204/child_4_bot_bg.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/child_4_bot_left.jpg\" width=\"258\" height=\"129\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "29":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"171\" height=\"192\" background=\"http://mimg.163.com/stationery/20050204/child_3_top1.jpg\">&nbsp;</td>    <td align=\"center\" background=\"http://mimg.163.com/stationery/20050204/child_3_top_bg.jpg\"><br>    <br>    <br>    <br>    <br>    <br>    <img src=\"http://mimg.163.com/stationery/20050204/child_3_top_cen.gif\" width=\"218\" height=\"36\"></td>    <td width=\"165\" background=\"http://mimg.163.com/stationery/20050204/child_3_top1_2.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" background=\"http://mimg.163.com/stationery/20050204/child_3_page_bg.gif\">  <tr>    <td width=\"60\">&nbsp;</td>    <td><br>      <div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #FF9900;text-align: left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div><br><br></td>    <td width=\"60\">&nbsp;</td>  </tr></table> "
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "30":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"26\" height=\"26\" background=\"http://mimg.163.com/stationery/20050204/child_2_top_left.jpg\">&nbsp;</td>    <td align=\"right\" background=\"http://mimg.163.com/stationery/20050204/child_2_top_bg.jpg\">&nbsp;</td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/child_2_top_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FDFCEA\">  <tr>    <td width=\"26\" background=\"http://mimg.163.com/stationery/20050204/child_2_left.jpg\">&nbsp;</td>    <td align=\"left\" valign=\"top\"><img src=\"http://mimg.163.com/stationery/20050204/child_2_star.jpg\" width=\"112\" height=\"67\"></td>    <td width=\"142\"><img src=\"http://mimg.163.com/stationery/20050204/child_2_deer.jpg\" width=\"142\" height=\"153\"></td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/child_2_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FEFBEA\">  <tr>    <td width=\"26\" background=\"http://mimg.163.com/stationery/20050204/child_2_left.jpg\">&nbsp;</td>    <td width=\"30\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/child_2_cen_bg.jpg\"><div id=\"here\" style=\"font-size: 11pt;line-height: 35px;color: #666666;text-align: left; \"><STATIONERY>"
            sS3 ="<STATIONERY></div></td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/child_2_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FEFBEA\">  <tr>    <td width=\"26\" background=\"http://mimg.163.com/stationery/20050204/child_2_left.jpg\">&nbsp;</td>    <td align=\"left\"><img src=\"http://mimg.163.com/stationery/20050204/child_2_star.jpg\" width=\"112\" height=\"67\"></td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/child_2_right.jpg\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"26\" height=\"32\" align=\"left\" background=\"http://mimg.163.com/stationery/20050204/child_2_bot_left.jpg\">&nbsp;</td>    <td align=\"right\" background=\"http://mimg.163.com/stationery/20050204/child_2_bot_bg.jpg\">&nbsp;</td>    <td width=\"28\" background=\"http://mimg.163.com/stationery/20050204/child_2_bot_right.jpg\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "31":
            sS1 ="<table width=\"99%\" align=center  border=\"0\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#EFE6DE\">  <tr align=\"left\" valign=\"bottom\">    <td height=\"184\" background=\"http://mimg.163.com/stationery/20050204/seesight_2_topBig.jpg\"><img src=\"http://mimg.163.com/stationery/20050204/seesight_2_top_left.jpg\" width=\"362\" height=\"66\"></td>  </tr>  <tr>    <td><br>	<TABLE width=\"85%\" align=\"center\">    <TR>    	<TD><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #CC7A00;text-align: left;\"><STATIONERY> "
            sS3 =" <STATIONERY></div></TD>    </TR>    </TABLE>		</td>  </tr>  <tr align=\"left\">    <td background=\"http://mimg.163.com/stationery/20050204/seesight_2_bot_bg.gif\"><img src=\"http://mimg.163.com/stationery/20050204/seesight_2_bot_left.jpg\" width=\"191\" height=\"34\"></td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		case "32":
            sS1 ="<table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_top_bg.gif\">  <tr>    <td width=\"138\" height=\"132\" valign=\"bottom\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_top_1.gif\">&nbsp;</td>    <td width=\"83\" valign=\"bottom\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_top_2.gif\">&nbsp;</td>    <td width=\"115\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_top_3.gif\">&nbsp;</td>    <td>&nbsp;</td>    <td width=\"113\" valign=\"bottom\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_top_4.gif\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFFFFF\">  <tr>    <td width=\"60\">&nbsp;</td>    <td><div id=\"here\" style=\"font-size: 11pt;line-height: 25px;color: #0080D5;text-align: left;\"><STATIONERY>"
            sS3 ="<STATIONERY>    </div></td>    <td width=\"60\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">  <tr>    <td width=\"237\" height=\"33\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_bot_1.gif\">&nbsp;</td>    <td background=\"http://mimg.163.com/stationery/20050204/seesight_1_bot_cen_bg.gif\">&nbsp;</td>    <td width=\"253\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_bot_2.gif\">&nbsp;</td>  </tr></table><table width=\"99%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" background=\"http://mimg.163.com/stationery/20050204/seesight_1_bot_2_bg.gif\">  <tr>    <td height=\"49\">&nbsp;</td>  </tr></table>"
			sStationeryBG = sS1 + sS2 + sS3;
			break;
		default:
			sStationeryBG = sS2;
			break;
	}
	document.sendmail.zStationery.value = sValue;	//记下信纸种类
	document.sendmail.zFirstContent.value = sS2; //记下信纸切换前的所有内容；

	document.sendmail.htmlletter.html = sStationeryBG; //赋值和信纸给编辑框
	//document.sendmail.stationery.options[sValue].selected = true;//初始化信纸选择下拉框
}