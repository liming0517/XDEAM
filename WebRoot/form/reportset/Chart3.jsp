<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/script/FusionCharts/Contents/Style.css" type="text/css" />
<script type="text/javascript"
		src="${pageContext.request.contextPath}/script/JQueryUI/jquery-1.4.4.min.js"></script>
<script language="JavaScript" src="${pageContext.request.contextPath}/script/FusionCharts/FusionCharts.js"></script>
<%@ page import="com.pantech.base.common.tools.PublicTools"%>
<div  style="height:100%; width:100%">
	<div  style="text-align:right">
		<a href="#"
				 onclick="doToolbar(this.id);"
				id="ok" name="ok" class="easyui-linkbutton" plain="true"
				iconCls="icon-ok">刷新</a>
	</div>
	<div id="chartdivs3" align="center" style="height:88%; width:100%"></div>
</div>
      <script type="text/javascript">
      		var chartsql="<%=PublicTools.getsql(request)%>";
      		//alert(chartsql);
      		var chartsx="";
      		var charttype="<%=request.getParameter("charttype")%>";
      		var charxml="";
      		doToolbar("ok");
      		
			function Column3D(charts){
				var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/Column3D.swf", "ChartId", ""+(document.all.chartdivs3.offsetWidth*0.98)+"", ""+(document.all.chartdivs3.offsetHeight*0.85)+"", "0", "0");
	     		chart.setDataXML(charts);
	     		chart.setTransparent(true);
	     		chart.render("chartdivs3");
			}
			
			function Line2D(charts){
				var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/Line.swf", "ChartId", ""+(document.all.chartdivs3.offsetWidth*0.98)+"", ""+(document.all.chartdivs3.offsetHeight*0.85)+"", "0", "0");
	     		chart.setDataXML(charts);
	     		chart.setTransparent(true);
	     		chart.render("chartdivs3");
			}
			
			function MSColumn2D(charts){
				var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/MSColumn2D.swf", "ChartId", ""+(document.all.chartdivs3.offsetWidth*0.98)+"", ""+(document.all.chartdivs3.offsetHeight*0.85)+"", "0", "0");
	     		chart.setDataXML(charts);
	     		chart.setTransparent(true);
	     		chart.render("chartdivs3");
			}
			
			function Pie3D(charts){
				var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/Pie3D.swf", "ChartId", ""+(document.all.chartdivs3.offsetWidth*0.98)+"", ""+(document.all.chartdivs3.offsetHeight*0.85)+"", "0", "0");
	     		chart.setDataXML(charts);
	     		chart.setTransparent(true);
	     		chart.render("chartdivs3");
			}
			
			function doToolbar(id){
				if(id=="ok"){
					//alert(chartsql);
					$.ajax({
						type: "POST",
						url: '<%=request.getContextPath()%>/Svl_Chart',   //设置 SVL
						async:false,
						data: "active2=shuaxin&chartsql=" + chartsql,  // 设置模式，一般为view ，主关键字值		
						dataType: 'text',
						success: function(datas){
							
							datas=datas.replace(/-/g,">");
							datas=datas.replace(/\+/g,"<");
							datas=datas.replace(/\"/g,"'");
							//alert(datas);
							//var chart = getChartFromId("ChartId");
							//alert(charttype);
							//chart.setDataXML(datas);
							if(charttype=="001"){
								Column3D(datas);
							}
							if(charttype=="002"){
								Pie3D(datas);
							}
							if(charttype=="003"){
								Line2D(datas);
							}
							if(charttype=="004"){
								MSColumn2D(datas);
							}
							charxml=datas;
							//chart.render("chartdivs3");
						}
					});
				}
		    }
		    
		    function chartsqls(sql){
		    	chartsql=sql;
		    }
		    
		    function chartsxs(sx){
		    	chartsx=sx;
		    }
		    
		    function charttypes(type){
		    	charttype=type;
		    }
		    
		    function getchartxml(){
		    	return charxml;
		    }
		</script> 

