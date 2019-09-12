// bbsCommon.js

// newFunction
function Popup(url, window_name, window_width, window_height){ 
	settings=
	"toolbar=no,location=no,directories=no,"+
	"status=no,menubar=no,scrollbars=yes,"+
	"resizable=yes,width="+window_width+",height="+window_height;

NewWindow=window.open(url,window_name,settings); }


function icon(theicon) {
	document.input.content.value += " "+theicon;
	document.input.content.focus();
}