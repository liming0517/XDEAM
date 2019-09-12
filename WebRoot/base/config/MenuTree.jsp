<%@ page 
language="java"
contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"
%>

<%@ page import="java.io.*,java.util.*,com.pantech.base.common.purview.*"%>
<%@ page import="com.pantech.base.common.config.*"%>
<%@ page import="javax.naming.NamingException"%>
<%@ page import="java.sql.SQLException"%>
<%
//树的类型,在不同模块，可能都要用到此树，但是显示形态可能不一样。
//于是定义一个type参数，告诉此树应该如何显示。如有时候显示为带复选框的树，有时候显示为一般树。
String treeType = request.getParameter("type");
if (treeType == null) {
	treeType = "MenuConfig";
}
%>
<%!static Hashtable images = new Hashtable();
static Hashtable actions = new Hashtable();
static String script;
static {
	images.put("IMAGE_PLUS", "images/plus.gif");
	images.put("IMAGE_PLUS_LAST", "images/pluslast.gif");
	images.put("IMAGE_MINUS", "images/minus.gif");
	images.put("IMAGE_MINUS_LAST", "images/minuslast.gif");
	images.put("IMAGE_MIDBLK", "images/midblk.gif");
	images.put("IMAGE_BLANK", "images/blank.gif");
	images.put("IMAGE_LASTBLK", "images/lastblk.gif");
	images.put("IMAGE_LINE", "images/line.gif");
	images.put("IMAGE_FOLDER", "images/folder.gif");
	images.put("IMAGE_LEAF", "images/leaf.gif");
	images.put("IMAGES_FOLDER_OPEN", "images/folderopen.gif");
	StringBuffer sc = new StringBuffer("<script type=\"text/javascript\">\r\n");
	Iterator imgs = images.values().iterator();
	int k = 0;
	while (imgs.hasNext()) {
		sc.append("var image" + k + "=new Image();\r\n");
		sc.append("image" + k + ".src=\"" + (String) imgs.next() + "\";\r\n");
		k++;
	}
	sc.append("</script>\r\n");
	script = sc.toString();
	actions.put("CLICK_FOLDER", "clickFolder");
	actions.put("CLICK_DOCUMENT", "clickDocument");
	actions.put("CLICK_FOLDER_IMG", "openFolder");
	actions.put("MOUSEOVER", "overMouse");
	actions.put("MOUSEOUT", "outMouse");
}
//**********************
//** 生成子结点
//**********************
void paintChilds(Iterator childs, String treeType, Writer w, HttpServletRequest req) throws IOException,SQLException,NamingException {
	while (childs.hasNext()) {
		paintNode((Node) childs.next(), treeType, w, req);
	}
}

//**********************
//**  生成结点主方法
//**  n - 节点/treeType - 树的类型，根据type的不同，树的显示形态也不同[如有时显示checkbox，有时显示为图片]。
//**********************
void paintNode(Node n, String treeType, Writer w, HttpServletRequest req) throws IOException,SQLException,NamingException {
	w.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>");
	int level = n.getLevel();
	int id = n.getId();
	int parId = n.getParent().getId();
	Node parent = null;
	String name = ((Group) n.getValue()).getName();
	boolean last = false;

	//生成树支
	for (int i = level - 1; i > 0; i--) {
		parent = n.getParent(i);
		last = parent.isLast();
		w.write(
			"<td><img src=\""
				+ req.getContextPath()
				+ "/"
				+ (String) images.get(last ? "IMAGE_BLANK" : "IMAGE_LINE")
				+ "\" border=\"0\"></td>");
		w.write("\n");
	}
	last = n.isLast();
	if (n.hasChilds()) {
		w.write(
			"<td id=\"plus"
				+ id
				+ "\" style=\"cursor:hand\" onClick=\""
				+ ((String) actions.get("CLICK_FOLDER_IMG") + "(document," + id + ")")
				+ "\"><img id=\""
				+ "plusImg"
				+ id
				+ "\" "
				+ "src=\""
				+ req.getContextPath()
				+ "/"
				+ (String) images.get(last ? "IMAGE_MINUS_LAST" : "IMAGE_MINUS")
				+ "\" border=\"0\"></td>");
		w.write("\n");
	} else {
		w.write(
			"<td id=\"plus"
				+ id
				+ "\"><img src=\""
				+ req.getContextPath()
				+ "/"
				+ (String) images.get(last ? "IMAGE_MINUS_LAST" : "IMAGE_MINUS")
				+ "\" border=\"0\"></td>");
		w.write("\n");
	}

	//生成结点图片
	if (n.hasChilds()) { //---------- 有子结点的情况
		w.write(
			"<td id=\"f"
				+ id
				+ "\"><img src=\""
				+ req.getContextPath()
				+ "/"
				+ (String) images.get("IMAGE_FOLDER")
				+ "\" border=\"0\"></td>");

		w.write("<td id=\"td" + id
						
		+ "\" style=\"cursor:hand\" onClick=\""
		+ "SendValue('"+id+"','"+name+"','"+ parId +"','"+ new Group().GetModuleID(id,req) +"')"
		+"\">" + id + ":" + name + "</td>");
		w.write("\n");
	} else { //---------- 无子结点的情况
		w.write(
			"<td id=\"f"
				+ id
				+ "\"><img src=\""
				+ req.getContextPath()
				+ "/"
				+ (String) images.get("IMAGE_LEAF")
				+ "\" border=\"0\"></td>");

		w.write("<td id=\"td" + id
		+ "\" style=\"cursor:hand\" onClick=\""
		+ "SendValue('"+id+"','"+name+"','"+ parId +"','"+ new Group().GetModuleID(id,req) +"')"
		+"\">"+ parId + ":"+ id + "-" + name + "</td>");
		w.write("\n");
	}
	w.write("</tr></table>");
	if (n.hasChilds()) {
		w.write("<div id=\"div" + id + "\" style=\"display:none\">");
		paintChilds(n.getChilds(), treeType, w, req);
		w.write("</div>");
		w.write("\n");
	}
	w.flush();
}

//根据trteetype来确定按纽是否显示
String GetState(String type) {
	String state = "";
	if (type.equalsIgnoreCase("Module")) {
		state = "";
	} else {
		state = "none";
	}
	return state;
}
%>
<html>
<head>
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>tree</title>
<link href="<%=request.getContextPath()%>/css/wiserunoa.css" rel="stylesheet" type="text/css">
<%=script%>
<script type="text/javascript">
/**
 *
 */
function changeColor(doc,k){
  var old=doc.form1.selectedNode.value;
  if(old!=k){
    if(old!=""){
      doc.all("td"+old).style.backgroundColor=doc.form1.bgColor.value;
    }
    doc.all("td"+k).style.backgroundColor=doc.form1.selectedColor.value;
    doc.form1.selectedNode.value=k;
  }
}
/**
 *
 */
function clickDocument(doc,k){
  changeColor(doc,k);
  alert("Click document "+doc.all("td"+k).innerText+".");
}
/**
 *
 */
function openFolder(doc,k){
  var o=doc.all("div"+k);
  if(o.style.display=="none"){
    o.style.display="block";
  }
  else{
    o.style.display="none";
  }
  replaceImg(doc,k);
  var k=0;
  while((o=o.parentElement)!=doc&&o!=null){
    k=o.id.indexOf("div");
    if(k!=-1){
      if(o.style.display=="none"){
        o.style.display="block";
		replaceImg(doc,o.id.substring(k));
      }
    }
  }
}
/**
 *
 */
function clickFolder(doc,k){
  changeColor(doc,k);
  alert("Click folder "+doc.all("td"+k).innerText+".");
}

/**
 * 将结点的值传递到左边框架中
 * @param id - 结点id
 */
function SendValue(id,name){
	//获得左边框架对象
	var leftFrame = eval("window.parent.left.location");
	<%if(request.getParameter("use")!=null&&request.getParameter("use").equalsIgnoreCase("helpInfo")){%>
		leftFrame.href = "HelpList.jsp?id="+id+"&name="+name;
	<%}else{%>
		leftFrame.href = "MenuList.jsp?id="+id;
	<%}%>
}


/**
 * 显示选中的菜单
 */
function showItems(){
	document.form1.submit();
}

/**
 * 全部选中
 */
function selectAll(){
	var a = document.getElementsByTagName("INPUT");
	for(var i=0; i<a.length; i++)
	{
	  if(a[i].type=="checkbox") a[i].checked=true;
	}
}


/**
 * 全部展开
 */
function ExpandAll(){
	var a = document.getElementsByTagName("DIV");
	//alert(a.length);
	for(var i=0; i<a.length; i++)
	{
	  if(a[i].style.display == "none"){
	  	a[i].style.display = "block";
	  }
	}
}
/**
 * 全部收缩
 */
function DrawInAll(){
	var a = document.getElementsByTagName("DIV");
	//alert(a.length);
	for(var i=0; i<a.length; i++)
	{
	  if(a[i].style.display == "block" || a[i].style.display == ""){
	  	a[i].style.display = "none";
	  }
	}
}


/**
 * 全部不选中
 */
function unSelectAll(){
var a = document.getElementsByTagName("INPUT");
for(var i=0; i<a.length; i++)
{
  if(a[i].type=="checkbox") a[i].checked=false;
}

}

<%out.println("/**");
out.println(" * 切换图片");
out.println(" */");
out.println("function replaceImg(doc,k){");
out.println("	var src =doc.all(\"plusImg\"+k).src;");
out.println("	var o = doc.all(\"div\"+k);");
out.println("	if(o.style.display==\"none\"){");
out.println("	doc.all(\"plusImg\"+k).src = \"" + request.getContextPath() + "/images/plus.gif\";");
out.println("	}else{");
out.println("	doc.all(\"plusImg\"+k).src = \"" + request.getContextPath() + "/images/minus.gif\";");
out.print("}");
out.print("}");
%>


</script>
</head>


<body onload="ExpandAll()">
<form name="form1" action="ModuleTree.jsp" method="post"><%out.flush();
//File f=new File(this.getServletConfig().getServletContext().getRealPath("/tree")).getParentFile();
//Tree t=Tree.getTree(f,new FileContainer());
String code = request.getParameter("PerCode");
if (code != null) {
	out.println(code);
	//根据percode显示权限树
}
Tree t = Tree.getTree(Group.getGroups(treeType, code,request), 0);
paintChilds(t.getChilds(), treeType, response.getWriter(), request);
%> 
<input type="hidden" name="selectedNode"> <input type="hidden"
	name="bgColor" value="#FFFFFF"> <input type="hidden"
	name="selectedColor" value="#9999FF"> <input
	type="button" value="全部展开" onclick="ExpandAll()"> <input type="button"
	value="全部收缩" onclick="DrawInAll()"> <!-- 接受从左边传递过来的值 --> <input
	type="hidden" name="PerCode" value=""></form>
</body>
</html>
