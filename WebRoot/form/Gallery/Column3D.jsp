<link rel="stylesheet" href="${pageContext.request.contextPath}/script/FusionCharts/Contents/Style.css" type="text/css" />
<script language="JavaScript" src="${pageContext.request.contextPath}/script/FusionCharts/FusionCharts.js"></script>
 <div id="chartdiv1" align="center" style="height:100%; width:100%"> 
        2013年学生出勤率报表 </div>
      <script type="text/javascript">
		   var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/Column3D.swf", "ChartId", ""+(document.all.chartdiv1.offsetWidth*0.95)+"", ""+(document.all.chartdiv1.offsetHeight*0.95)+"", "0", "0");
		   chart.setDataURL("${pageContext.request.contextPath}/form/Gallery/Data/Column3D.xml");		
		   //chart.setDataXML("<chart caption='aaaq' xAxisName='10' yAxisName='14'><set lable='0' value='1'/><set lable='0' value='2'/></chart>");
		   chart.render("chartdiv1");
		</script> 

