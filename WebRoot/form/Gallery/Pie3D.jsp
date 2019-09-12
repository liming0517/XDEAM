<link rel="stylesheet" href="${pageContext.request.contextPath}/script/FusionCharts/Contents/Style.css" type="text/css" />
<script language="JavaScript" src="${pageContext.request.contextPath}/script/FusionCharts/FusionCharts.js"></script>
<div id="chartdiv4" align="center" style="height:100%; width:100%"> 
         </div>
      <script type="text/javascript">
		   var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/Pie3D.swf", "ChartId", ""+(document.all.chartdiv4.offsetWidth*0.95)+"", ""+(document.all.chartdiv4.offsetHeight*0.95)+"", "0", "0");
		   chart.setDataURL("${pageContext.request.contextPath}/form/Gallery/Data/Pie3D.xml");		   
		   chart.render("chartdiv4");
		</script> 

