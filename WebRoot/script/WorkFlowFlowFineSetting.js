// WorkFlowFlowFineSetting.js
//工作流设定前台JS
//HADES
//2005-4-15


//背景颜色


var TrbgColor = new Array();
TrbgColor[0]="#edd9b6";
TrbgColor[1]="#27cbd8";



function setBgColor(){
//设定 表格中某行的背景色
//判定上一行是否为红，若不是，则本行为红，否则为蓝

	//if (objTr.rowIndex<4){
	    //头一行
	      //if (objTr.parentNode.rows(objTr.rowIndex-1).bgColor==TrbgColor[0]){
	//	     objTr.bgColor=TrbgColor[0];
		 // }else{
		 //    objTr.bgColor=TrbgColor[1];  
		 // }   
	   
//	}else{
            //设置步骤颜色 icTrFlowFine 
		      var obj=document.getElementsByName("icTrFlowFine");
		      var tbArrWorkFlow = document.getElementsByName("icTBWorkflow");
  			  var tbArrRelation = document.getElementsByName("icTBRelation");
  			  //alert(tbArrWorkFlow[i].rows);
  			  
  			  
			      for (var i=0;i<obj.length;i++){
			          obj[i].bgColor=TrbgColor[i%2];
			        //设置相关的步骤条件颜色
			        
					       try{
						         for (var j=1;j<tbArrWorkFlow[i].rows.length ;j++){
						             //标题行忽略
						             tbArrWorkFlow[i].rows[j].bgColor=obj[i].bgColor;
						         }
					        }catch(e) {
				       
				            }  
			        
			        //设置相关的节点关系颜色
			       //if (tbArrRelation[i].rows!=null){
				        try{
					         for (var j=1;j<tbArrRelation[i].rows.length ;j++){
					             //标题行忽略
					             tbArrRelation[i].rows[j].bgColor=obj[i].bgColor;
					         }
			     		}catch(e) {
				           
				        }  
		          }
		       
		     
		
		
		  
//	}


}

function showdata(indexNo){
//显示列表中的数据
	var obj;
	var sTmp;
	
	obj=eval("document.form1.arrSerialNo"+indexNo +"");
	document.form1.icSerialNo.value=obj.value;
	
	obj=eval("document.form1.arrModuleName"+indexNo +"" );
	document.form1.icModuleName.value=obj.value;
	
    obj=eval("document.form1.arrModuleCode"+indexNo +"" );
	document.form1.icModuleCode.value=obj.value;

	obj=eval("document.form1.arrIsNeed"+indexNo +"" );
	sTmp=obj.value;
	if (sTmp=="1"){
	  document.form1.icIsNeed(0).checked=1;
	}else{
	  document.form1.icIsNeed(1).checked=1;
	}
	
	obj=eval("document.form1.arrMsg"+indexNo +"" );
	document.form1.icMsg.value=obj.value;
	
	obj=eval("document.form1.arrInceptCode"+indexNo +"" );
	document.form1.icInceptCode.value=obj.value;
	
 }
 
 function showDetail(index){
 //显示详细情况
 
	var obj = eval("document.all.icTrWorkflow"+index);
	var obj2 = eval("document.all.icTrRelation"+index);
//	var objbut = document.getElementsByName("button");
//	var objb = objbut[index];
	
	var objb =  eval("document.all.button"+index);
	//alert(obj.style.display);
	if (objb.value == " +"){
		obj.style.display = "block";
		obj2.style.display = "block";
		objb.value = " -";
	}else{
		obj.style.display = "none";
		obj2.style.display = "none";
		objb.value = " +";
	}
 }

 
function SumFlowFine(){
  //alert("d");
  
   //重新计算 每一步 的条件记录数和节点数
  
  //循环所有步骤，
  //工作流步骤数=（整个工作流步骤表格行数-1）/3
   var obj = eval("document.all.icTBFlowFine");
   var j;
   j=(obj.rows.length-1)/3 ;
   var inputArrWorkFlow = document.getElementsByName("arrCountWorkFlow");
   var inputArrRelation = document.getElementsByName("arrCountRelation");
   
   var tbArrWorkFlow = document.getElementsByName("icTBWorkflow");
   var tbArrRelation = document.getElementsByName("icTBRelation");
   //理论上 有一个统计项（隐藏），就说明有一个条件表格和节点关系表格
  for (var i=0;i<inputArrWorkFlow.length;i++){
     // alert(tbArrRelation[i].rows.length);  
     // alert(tbArrRelation[i].rows.length);
    
		//自动加一行条件
		if (tbArrWorkFlow[i].rows.length==1){
			addWorkFlow(i);
		}
		
		//自动加一行节点关系
		if (tbArrRelation[i].rows.length==1){
			addRelation(i);	
		}
	
      inputArrWorkFlow[i].value=tbArrWorkFlow[i].rows.length-1;
      
      
      
      inputArrRelation[i].value=tbArrRelation[i].rows.length-1;
    //  alert(inputArrWorkFlow[i].value);   
     // alert(document.all.arrCountRelation[i].value);  
      
   }
   //提取表格中的HTML代码
   document.all.htmlcode.value= document.all.icTBFlowFine.innerHTML;
   
}  
  

function addRow(){
//添加工作流步骤
//每个工作流步骤分为3行,第一行是步骤本身,第二行是步骤的完成条件,第三行是步骤之间的节点关系
//整个工作流步骤表格行数=3*工作流步骤数+1 (标题行)

  var obj = eval("document.all.icTBFlowFine");
  var j;
  //alert(obj.rows.length);  由于记录可能被删除，因此j应该根据 button 元素后面的 数据定，比如最后行button名为button10，那么j=10+1=11
  j=(obj.rows.length-1)/3 ; //下标从0开始 (头一行为标题) 必须要在INSERTROW前写

  try{
    var i;

    for(i=j+5;i>0;i--){
       //为提高效率，只对50行内可能出现的错误进行纠正 倒过来查速度快
       var objbu =eval("document.all.button"+i);
//    alert(typeof(objbu));
       if (typeof(objbu)!="undefined"){
          //如果按钮存在且 i> j则纠正j后退出
          if (i>=j){
             j=i+1;
          }

          break;
       }
      // alert("i===="+i);
      // alert("newJ=="+j);
    }
   
      
    
  }catch (e){
    //如果出错则使用原来的j
     alert("error");
  }
  //alert("newJA=="+j);  



  var objTr=obj.insertRow();
  
  
  //alert(j);
  objTr.CLASS ="fieldbg";
  objTr.id="icTrFlowFine";
  objTr.name="icTrFlowFine";
  
  
  //10
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  ////主体部分
	var sObj;
    sObj="<input type=\"button\" name=\"button"+j+"\" id=\"button"+j+"\"  value=\" +\" onclick=\"showDetail("+j+");\">";
	sObj=sObj+" <input type=\"button\" value=\"DEL\" name=\"delbutton"+j+"\" id=\"delbutton"+j+"\" onclick=\"delGroup(this.parentNode.parentNode,icTrWorkflow"+j+",icTrRelation"+j+");\" >";
    objTr.cells(0).innerHTML=sObj;
	//这里必须写一个TD
	sObj="<TD><input style=\"width:100%\" type='text' name=arrSerialNo id=arrSerialNo></td>";
	objTr.cells(1).innerHTML=sObj;
	sObj="<TD><input style=\"width:100%\" type='text' name=arrTitle id=arrTitle></td>";
	objTr.cells(2).innerHTML=sObj;
	///
	  sObj=""; // "<TD><input style=\"width:100%\" type='text' name=arrIsNeed id=arrIsNeed></td>";
	  sObj=sObj+"<TD  ><SELECT id=name=\"arrIsNeed\" name=\"arrIsNeed\">";
	  sObj=sObj+"<OPTION value=\"1\" SELECTED>是</OPTION>";
	  sObj=sObj+"<OPTION value=\"0\" >否</OPTION>";
	  sObj=sObj+"</SELECT></TD>";   
	objTr.cells(3).innerHTML=sObj;
	
	sObj="<TD><input style=\"width:100%\" type='text' name=arrModuleName id=arrModuleName></td>";
	objTr.cells(4).innerHTML=sObj;
	///
	sObj="<TD><input style=\"width:100%\" type='text' name=arrModuleCode id=arrModuleCode></td>";
	objTr.cells(5).innerHTML=sObj;
	
	sObj="<TD><input style=\"width:100%\" type='text' name=arrInceptCode id=arrInceptCode></td>";
	objTr.cells(6).innerHTML=sObj;
	
	 sObj=""; // "<TD><input style=\"width:100%\" type='text' name=arrIsNeed id=arrIsNeed></td>";
	  sObj=sObj+"<TD  ><SELECT id=name=\"arrCanModify\" name=\"arrCanModify\">";
	  sObj=sObj+"<OPTION value=\"1\" SELECTED>有</OPTION>";
	  sObj=sObj+"<OPTION value=\"0\" >无</OPTION>";
 	 sObj=sObj+"</SELECT></TD>";   
	objTr.cells(7).innerHTML=sObj;
	
	
	
	sObj="<TD><input style=\"width:100%\" type='text' name=arrMsg id=arrMsg>";
	sObj=sObj+"<input type=hidden name=arrCountWorkFlow id=arrCountWorkFlow >";
	sObj=sObj+"<input type=hidden name=arrCountRelation id=arrCountRelation >"+"</td>";
	objTr.cells(8).innerHTML=sObj;//最后一列保存 该步骤 的条件数 和节点关系记录数 在存盘前重新计算
	
    sObj="<TD><input style=\"width:100%\" type='text' name=arrSvlName  id=arrSvlName></td> " ;
	objTr.cells(9).innerHTML=sObj;
											      
	
	//条件部分
	//objTr=null;
	objTr=obj.insertRow();
	objTr.id="icTrWorkflow"+j;
	objTr.style.display = "none";
	//objTr.name="icTrWorkflow"+j;
	
	
	objTr.CLASS ="fieldbg";
	//sObj="<TR name=icTrWorkflow"+j+" id=icTrWorkflow"+j+"  style=\"display:none\" class=\"tableline\" >";
	objTr.insertCell();
	objTr.cells(0).colSpan =8;
//	sObj=sObj+"<TD colspan=7>";  
	sObj="<!--步骤条件 结果区 新 -->";
	sObj=sObj+"<TABLE name=\"icTBWorkflow\" id=\"icTBWorkflow\" class=\"tablestyle\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" >";
	sObj=sObj+"<TBODY>";
	sObj=sObj+"<TR class=\"fieldbg\">";
	sObj=sObj+"<TD>" ;
// 	sObj=sObj+"<input type=\"button\" value=\"添加新条件\" name=\"addWorkFlowRow\" onclick=\"addWorkFlow("+j+");\" >";
  	sObj=sObj+"<input type=\"button\" value=\"添加新条件\" name=\"addWorkFlowRow\" onclick=\"addWorkFlowByButton(this);\" >";
  	sObj=sObj+"</TD>";
	sObj=sObj+"<TD width=\"50%\" align=\"center\">逻辑条件</TD>";
	sObj=sObj+"<TD width=\"10%\" align=\"center\">逻辑关系</TD>";
	sObj=sObj+"</TR>";
	sObj=sObj+"</TBODY>";									
	sObj=sObj+"</TABLE>";
	//sObj=sObj+"</TD>";
	//sObj=sObj+"</TR>";
	objTr.cells(0).innerHTML=sObj;
	
	//关系部分
	objTr=obj.insertRow();
	objTr.id="icTrRelation"+j;
	objTr.style.display = "none";
	//objTr.name="icTrRelation"+j;
	
	
	objTr.CLASS ="fieldbg";
	objTr.insertCell();
	objTr.cells(0).colSpan =8;
	sObj="<!--步骤节点关系 结果区  新-->";
	sObj=sObj+"<TABLE name=\"icTBRelation\" id=\"icTBRelation\" class=\"tablestyle\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" >";
	sObj=sObj+"<TBODY>";
	sObj=sObj+"<TR class=\"fieldbg\">";
	sObj=sObj+"<TD width=\"30%\">" ;
// 	sObj=sObj+"<input type=\"button\" value=\"添加节点关系\" name=\"addWorkFlowRow\" onclick=\"addRelation("+j+");\" >";
 	sObj=sObj+"<input type=\"button\" value=\"添加节点关系\" name=\"addRelationRow\" onclick=\"addRelationByButton(this);\" >";
  	sObj=sObj+"</TD>";
	sObj=sObj+"<TD width=\"20%\" align=\"center\">父节点模块编号</TD>";
	sObj=sObj+"<TD width=\"40%\" align=\"center\">逻辑条件</TD>";
	sObj=sObj+"<TD width=\"10%\" align=\"center\">逻辑关系</TD>";
	sObj=sObj+"</TR>";
	sObj=sObj+"</TBODY>";									
	sObj=sObj+"</TABLE>";							
	objTr.cells(0).innerHTML=sObj;							     
	
	
	
	//设置颜色
	setBgColor();	
	//添加条件和节点关系
	//addWorkFlow(j);
	//addRelation(j);
	 var objTmp ;
	 objTmp=document.getElementsByName("addWorkFlowRow");
     addWorkFlowByButton(objTmp[objTmp.length-1]);
	 
 	 objTmp=document.getElementsByName("addRelationRow");
     addRelationByButton(objTmp[objTmp.length-1]);
	
	//当步骤数超过1个时，当前步骤编号是上一步骤编号 +1 
	 var inputArrSerialNo  = document.getElementsByName("arrSerialNo");
	 if (inputArrSerialNo.length>1){
	    //当前步骤编号是上一步骤编号 +1 
	 	inputArrSerialNo[inputArrSerialNo.length-1].value=parseInt( inputArrSerialNo[inputArrSerialNo.length-2].value) + 1;
	 }else{
	    inputArrSerialNo[inputArrSerialNo.length-1].value=1;
	 }
											
} 
////////////////////////////////////////////////////////////
/**
添加新条件公共部分
*/
function addWorkFlowClass(obj){
 var j;
  j=obj.rows.length-1; //下标从0开始
  var objTr=obj.insertRow();
  objTr.CLASS ="fieldbg";

 
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();

	var sObj;
//	alert(obj.innerHTML);
	sObj="<input type=\"button\" value=\"删除\" name=\"delWorkFlowRow\" onclick=\"delRow(this.parentNode.parentNode);\" >";
    objTr.cells(0).innerHTML=sObj;
	//这里必须写一个TD
	sObj="<input style=\"width:100%\" type='text' name=arrRelationVal id=arrRelationVal>";
	objTr.cells(1).innerHTML=sObj;
	
	sObj=""; //"<input style=\"width:100%\" type='text' name=arrOper id=arrOper>";
	sObj=sObj+"<TD  ><SELECT id=\"arrOper\" name=\"arrOper\">";
    sObj=sObj+"<OPTION value=\"AND\" SELECTED>AND</OPTION>";
    sObj=sObj+"<OPTION value=\"OR\" >OR</OPTION>";
    sObj=sObj+"</SELECT></TD>";     
    
	objTr.cells(2).innerHTML=sObj;
	//obj.innerHTML=sObj;
	
	//设置颜色
	setBgColor();	
}
/**
 * 添加新条件
 */
function addWorkFlowByButton(objBut){
//为了防止动态删除后无法添加行，因此增加了这个方法
    var obj;

      //为一个工作流节点增加一行完成条件  从TD TR TBODY -》TABLE
  obj=objBut.parentNode.parentNode.parentNode.parentNode;
  addWorkFlowClass(obj);
} 
 
/**
 * 添加新条件
 */
function addWorkFlow(index){
//添加工作流条件
//一个条件一行
//条件表格的总行数=条件行数+1(标题)
//index --步骤所在的行数


  //为一个工作流节点增加一行完成条件
  var objtb = document.getElementsByName("icTBWorkflow");
  var obj = objtb[index];
 
  addWorkFlowClass(obj);
}



////////////////////////////////////////////////////////////



/**
添加新节点关系公共部分
*/
function addRelationClass(obj){
//  var obj = eval("document.all.icTBRelation"+index);
  var j;
  j=obj.rows.length-1; //下标从0开始
  var objTr=obj.insertRow();

 // objTr.id="icTrRelation"+index;
 // objTr.name="icTrRelation"+index;
 // objTr.STYLE =obj.rows(0).STYLE;

  
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
  objTr.insertCell();
	var sObj;
	

	sObj="<input type=\"button\" value=\"删除\" name=\"delRelationRow\" onclick=\"delRow(this.parentNode.parentNode);\" >";
    objTr.cells(0).innerHTML=sObj;
	sObj="<TD><input style=\"width:100%\" type='text' name=arrR_FatherModuleCode id=arrR_FatherModuleCode</td>";
    objTr.cells(1).innerHTML=sObj;
	//这里必须写一个TD
	sObj="<TD><input style=\"width:100%\" type='text' name=arrR_RelationVal id=arrR_RelationVal</td>";
	objTr.cells(2).innerHTML=sObj;
	// "<TD><input style=\"width:100%\" type='text' name=arrR_Oper id=arrR_Oper</td>";
	sObj="";   
	sObj=sObj+"<TD   ><SELECT id=\"arrR_Oper\" name=\"arrR_Oper\">";
    sObj=sObj+"<OPTION value=\"AND\" SELECTED>AND</OPTION>";
    sObj=sObj+"<OPTION value=\"OR\" >OR</OPTION>";
    sObj=sObj+"</SELECT></TD>";       
	
	objTr.cells(3).innerHTML=sObj;
	//obj.innerHTML=sObj;
	
	//设置颜色
	setBgColor();	
	//当步骤数超过1个时，当前父节点编号应该是上一步的模块编号
	 var inputArrModuleCode  = document.getElementsByName("arrModuleCode");
 	 var inputArrR_FatherModuleCode  = document.getElementsByName("arrR_FatherModuleCode");
 	// alert(inputArrModuleCode.length);
	 if (inputArrModuleCode.length>1){
	    //当前父节点编号应该是上一步的模块编号
	   // alert("Start");
	 	inputArrR_FatherModuleCode[inputArrR_FatherModuleCode.length-1].value=inputArrModuleCode[inputArrModuleCode.length-2].value;
	 
	 }
}

/**
 * 添加新接点关系
 */
function addRelationByButton(objBut){
//为了防止动态删除后无法添加行，因此增加了这个方法
    var obj;

      //为一个工作流节点增加一行完成条件  从TD TR TBODY -》TABLE
  obj=objBut.parentNode.parentNode.parentNode.parentNode;
  addRelationClass(obj);
} 

/**
 * 添加新接点关系
 */
function addRelation(index){
//添加工作流节点关系
//一个关系一行
//表格的总行数=关系行数+1(标题)
//index --步骤所在的行数


  //为一个工作流节点增加一行节点关系
  var objtb = document.getElementsByName("icTBRelation");
  var obj = objtb[index];
   addRelationClass(obj);
		     
  			      
}
////////////////////////////////////////////////////////////

function delRow(objTr){
//删除行
//objTr --行对象
//alert(objTr.innerHTML);
//alert(obj1.parentNode.parentNode.innerHTML);
objTr.removeNode(true);
}

/**
 * 当删除一个步骤时，其附属的新条件和节点关系组也要删除[共删除3行，而不是一行]
 */
function delGroup(objTr,obj1,obj2){
	objTr.removeNode(true);
	obj1.removeNode(true);
	obj2.removeNode(true);
}

