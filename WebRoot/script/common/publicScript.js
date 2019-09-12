
window.onload = function(){
	document.getElementsByTagName("body")[0].onkeydown =function(){
		//获取事件对象
		var elem = event.relatedTarget || event.srcElement || event.target ||event.currentTarget;
		
		if(event.keyCode==8){//判断按键为backSpace键
			//获取按键按下时光标做指向的element
			var elem = event.srcElement || event.currentTarget;
			
			//判断是否需要阻止按下键盘的事件默认传递 
			var name = elem.nodeName;
			if(name!='INPUT' && name!='TEXTAREA'){
				return _stopIt(event);
			}
			
			var type_e = elem.type.toUpperCase();
			if(name=='INPUT' && (type_e!='TEXT' && type_e!='TEXTAREA' && type_e!='PASSWORD' && type_e!='FILE')){
				return _stopIt(event);
			}
			if(name=='INPUT' && (elem.readOnly==true || elem.disabled ==true)){
				return _stopIt(event);
			}
		}
	};
};

function _stopIt(e){
	if(e.returnValue){
		e.returnValue = false;
	}
	if(e.preventDefault ){
		e.preventDefault();
	}
	
	return false;
}

/**设置textbox输入长度限制*/
function setInputMaxLen(id, len){
	$('#' + id).next("span").find("input").attr('maxlength', len);
}

/**获取当前日期*/
function getCurTime(){
	var curr_time = new Date();
	return curr_time.getFullYear()+"-"+(curr_time.getMonth()+1)+"-"+curr_time.getDate(); 
}

/** 
 * combobox和combotree模糊查询 
 */  
(function(){  
    //combobox可编辑，自定义模糊查询  
    $.fn.combobox.defaults.editable = true;  
    $.fn.combobox.defaults.filter = function(q, row){  
        var opts = $(this).combobox('options');  
        return row[opts.textField].indexOf(q) >= 0;  
    };  
    //combotree可编辑，自定义模糊查询  
    $.fn.combotree.defaults.editable = true;  
    $.extend($.fn.combotree.defaults.keyHandler,{  
        up:function(){  
            console.log('up');  
        },  
        down:function(){  
            console.log('down');  
        },  
        enter:function(){  
            console.log('enter');  
        },  
        query:function(q){  
            var t = $(this).combotree('tree');  
            var nodes = t.tree('getChildren');  
            for(var i=0; i<nodes.length; i++){  
                var node = nodes[i];  
                if (node.text.indexOf(q) >= 0){  
                    $(node.target).show();  
                } else {  
                    $(node.target).hide();  
                }  
            }  
            var opts = $(this).combotree('options');  
            if (!opts.hasSetEvents){  
                opts.hasSetEvents = true;  
                var onShowPanel = opts.onShowPanel;  
                opts.onShowPanel = function(){  
                    var nodes = t.tree('getChildren');  
                    for(var i=0; i<nodes.length; i++){  
                        $(nodes[i].target).show();  
                    }  
                    onShowPanel.call(this);  
                };  
                $(this).combo('options').onShowPanel = opts.onShowPanel;  
            }  
        }  
    });  
})(jQuery);

/**解决ie9以下数组没有indexOf方法*/
if (!Array.prototype.indexOf)
{
  Array.prototype.indexOf = function(elt /*, from*/)
  {
    var len = this.length >>> 0;
    var from = Number(arguments[1]) || 0;
    from = (from < 0)
         ? Math.ceil(from)
         : Math.floor(from);
    if (from < 0)
      from += len;
    for (; from < len; from++)
    {
      if (from in this &&
          this[from] === elt)
        return from;
    }
    return -1;
  };
}