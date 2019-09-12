// sysconfig_info.js

function close_window(frame)
{
	if(confirm("是否放弃修改?") == true)
	{
		//alert(frame.name);
		frame.location="./Svl_update_sysconfig_info";
	}
}

function do_confirm(tag,warning_message)
{
	var object = eval("document.all."+tag+"");
	var object_id = eval("document.all.ID");
	if(object.value == "")
	{
		alert(warning_message);
		object.focus();
		return false;
	}
	else
	{
		if(confirm("是否提交保存修改?") == true)
		{
			if(object_id.value != object.value)
			{
				object_id.value = object.value;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}

function do_confirm_all(type)
{
	var list_UT = new Array(["BRANCH_ID","分公司代码必选!"],["SO_ID","营业处代码必选!"],["COM_TIME","一般发送单上传时间设定必填!"]);
	
	var list_SC = new Array(["BRANCH_ID","分公司代码必选!"],["SO_ID","营管处代码必选!"],["HO_PATH","总公司路径必填!"],["BRANCH_PATH","分公司路径必填!"],["IMAGE_PATH","影像存放路径必填!"],["IMAGE_BACK","影像备份路径必填!"],["COM_FREQ","上传频率设定必填!"]);
	
	var list_DS = new Array(["DI_ID","业务单证种类代码必填!"],["DI_SPLIT_ID","切割后的单证代码必填!"]);

	var object;
	var list_id;

	if(type == "UT")
	{
		for(var i = 0;i < list_UT.length;i++)
		{
			object = eval("document.all."+list_UT[i][0]+"");
			if(object.value == "")
			{
				alert(list_UT[i][1]);
				object.focus();
				return false;
			}
		}
		if(confirm("是否提交保存修改?") == true)
		{
			/*
			list_id = eval("document.all.ID");
			for(var i = 0;i < list_UT.length;i++)
			{
				object = eval("document.all."+list_UT[i][0]+"");
				list_id[i].value = object.value;
			}
			*/
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		if(type == "SC")
		{
			for(var i = 0;i < list_SC.length;i++)
			{
				object = eval("document.all."+list_SC[i][0]+"");
				if(object.value == "")
				{
					alert(list_SC[i][1]);
					object.focus();
					return false;
				}
			}
			if(confirm("是否提交保存修改?") == true)
			{
				/*
				list_id = eval("document.all.ID");
				for(var i = 0;i < list_UT.length;i++)
				{
					object = eval("document.all."+list_UT[i][0]+"");
					list_id[i].value = object.value;
				}
				*/
				return true;
			}
			else
			{
				return false;
			}
		}
		else//TYPE = "DS"
		{
			for(var i = 0;i < list_DS.length;i++)
			{
				object = eval("document.all."+list_DS[i][0]+"");
				if(object.value == "")
				{
					alert(list_DS[i][1]);
					object.focus();
					return false;
				}
			}
			if(confirm("是否提交保存修改?") == true)
			{
				/*
				list_id = eval("document.all.ID");
				for(var i = 0;i < list_UT.length;i++)
				{
					object = eval("document.all."+list_UT[i][0]+"");
					list_id[i].value = object.value;
				}
				*/
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}