// auth_list.js
function delconfirm()
{
	return confirm("ÊÇ·ñ¼ÌÐø?");
}

function do_post(id,type)
{
	document.DELETE.ID.value = id;
	document.DELETE.TYPE.value = type;
	//alert(document.DELETE.ID.value);
	document.DELETE.submit();
}