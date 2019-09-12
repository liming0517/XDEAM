
<link rel="stylesheet" href="${pageContext.request.contextPath}/script/FusionCharts/Contents/Style.css" type="text/css" />
<script language="JavaScript" src="${pageContext.request.contextPath}/script/FusionCharts/FusionCharts.js"></script>
<div id="chartdiv3" align="center" style="height:100%; width:100%" > 
        FusionCharts. </div>
      <script type="text/javascript">
		   var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/MSColumn2D.swf", "ChartId", ""+(document.all.chartdiv3.offsetWidth*0.95)+"", ""+(document.all.chartdiv3.offsetHeight*0.95)+"", "0", "0");
		   chart.setDataURL("${pageContext.request.contextPath}/form/Gallery/Data/MSColumn2D.xml");		   
		   chart.render("chartdiv3");
		</script>
