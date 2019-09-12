// config_list.js
function delconfirm()
{
	return confirm("ÊÇ·ñ¼ÌÐø?");
}

function do_delete(tag,type)
{
	var target = eval("document.DELETE.ID");
	if(type == "UT" || type == "SC" || type == "DS")
	{
		var object = eval("document.LIST."+tag+"");
		for(var i = 0;i < object.length;i++)
		{
			target[i].value = object[i].value;
			//alert(target[i].value);
		}
	}
	else
	{
		target.value = tag;
		//alert(target.value);
	}
	document.DELETE.TYPE.value = type;
	document.DELETE.submit();
}

function do_modify(tag,type)
{
	var target = eval("document.MODIFY.ID");
	if(type == "UT" || type == "SC" || type == "DS")
	{
		var object = eval("document.LIST."+tag+"");
		for(var i = 0;i < object.length;i++)
		{
			target[i].value = object[i].value;
		}
	}
	else
	{
		target.value = tag;
	}
	document.MODIFY.TYPE.value = type;
	document.MODIFY.submit();
}