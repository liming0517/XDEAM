WindowTop = 0;
function getAbsTop(){
	var diffY;
	if (document.documentElement && document.documentElement.scrollTop){
		diffY = document.documentElement.scrollTop;
	}else if (document.body){
		diffY = document.body.scrollTop
	}else{/*Netscape stuff*/}
	
	percent=(diffY-WindowTop); 

	if(percent>0){
		percent=Math.ceil(percent);
	}else{
		percent=Math.floor(percent);
	}
	
	WindowTop = WindowTop + percent;
	return WindowTop;
}

function setCenter(ObjID){
	//获取窗口宽度 document.body.clientWidth
	//获取窗口高度 document.body.clientHeight
	var getTop = getAbsTop() + ( document.body.clientHeight - $("#xMsg"+ObjID).height() ) / 2;
	var getLeft = ( document.body.clientWidth - $("#xMsg"+ObjID).width() ) / 2;
	$("#xMsg"+ObjID).css("top",getTop);
	$("#xMsg"+ObjID).css("left",getLeft);
	$("#xMsg"+ObjID+"bg").css("top",getTop+6);
	$("#xMsg"+ObjID+"bg").css("left",getLeft+6);
} 