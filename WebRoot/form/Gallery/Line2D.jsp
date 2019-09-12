<link rel="stylesheet" href="${pageContext.request.contextPath}/script/FusionCharts/Contents/Style.css" type="text/css" />
<script language="JavaScript" src="${pageContext.request.contextPath}/script/FusionCharts/FusionCharts.js"></script>
<div id="chartdiv2" align="center" style="height:100%; width:100%"> 
        FusionCharts. </div>
      <script type="text/javascript">
	  //alert(document.all.chartdiv2.offsetHeight-2);
		   var chart = new FusionCharts("${pageContext.request.contextPath}/script/FusionCharts/Charts/Line.swf", "ChartId", ""+(document.all.chartdiv2.offsetWidth*0.95)+"", ""+(document.all.chartdiv2.offsetHeight*0.95)+"", "0", "0");
		   chart.setDataURL("${pageContext.request.contextPath}/form/Gallery/Data/Line2D.xml");		   
		   chart.render("chartdiv2");
		</script> 

