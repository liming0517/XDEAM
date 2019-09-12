// JScript 文件

// JavaScript Document
(function()
{

    var vmlss = window.vmlss = function(options)
    {
        return new vmlss.fn.init(options);
    }

    vmlss.fn = vmlss.prototype = {

        chartType: { cylinder: "cylinder_h", pic: "picchart", line: "linechart" }, //绘制类型

        init: function(options)
        {//初始化
            this._options = vmlss.fn.extend(
				{ width: 400, height: 600,
				    type: "", //图形类型，竖向柱状:cylinder-h、横向柱状:cylinder-v、饼图:picchart、折线图:linechart	
				    title: null, //标题
				    vLabel: null, //横向小标题
				    hLabel: null, //竖向小标题
				    color: false, //true:采用随机器产生颜色，false:采用固定已设置颜色，[]:采用自定义颜色
				    data: null, //数据
				    element: null, //html输出的对象
				    calnum: null,
				    zoom: 1//缩放倍数
				}, options);
            if (typeof this._options.color == "boolean")
            {
                var _c1, _c2;
                _c1 = new Array();
                _c2 = new Array();
                if (this._options.color)
                {
                    var i;
                    for (i = 0; i < 10; i++)
                    {
                        ;
                        //Math.
                        //_c1[0]="#"+Math.random();
                    }
                } else
                {
                    //浅色组
                    _c1 = new Array("#f9f", "#f99", "#ff9", "#9f9", "#9ff", "#99f", "#f99", "#ff9", "#9f9", "#9ff", "#6fc", "#69f", "#f9c", "#fc9", "#6f6", "#9fc", "#69f", "#c9f", "#f9c", "#ff6", "#9f9", "#6f9", "#6cf", "#a9f", "#c9f", "#f99", "#cf9", "#9f9", "#9ff", "#9af");
                    //深色组
                    _c2 = new Array("#f0f", "#f00", "#ff0", "#0f0", "#0ff", "#00f", "#f09", "#f60", "#9f0", "#0f6", "#09f", "#60f", "#f03", "#fc0", "#3f0", "#0fc", "#03f", "#c0f", "#f06", "#f90", "#6f0", "#0f9", "#06f", "#90f", "#f0c", "#f30", "#cf0", "#0f3", "#0cf", "#30f");
                }
                this._options.color = new Array(_c1, _c2);
            }
            ////////////////////

        }
    };
    vmlss.extend = vmlss.fn.extend = function()
    {//对象扩展
        // copy reference to target object
        var target = arguments[0] || {},
				i = 1,
				length = arguments.length,
				deep = false,
				options;

        // Handle a deep copy situation
        if (typeof target == "boolean")
        {
            deep = target;
            target = arguments[1] || {};
            // skip the boolean and the target
            i = 2;
        }

        // Handle case when target is a string or something (possible in deep copy)
        if (typeof target != "object" && typeof target != "function")
            target = {};

        // extend jQuery itself if only one argument is passed
        if (length == i)
        {
            target = this;
            --i;
        }

        for (; i < length; i++)
        // Only deal with non-null/undefined values
            if ((options = arguments[i]) != null)
        // Extend the base object
            for (var name in options)
        {
            var src = target[name], copy = options[name];

            // Prevent never-ending loop
            if (target === copy)
                continue;

            // Recurse if we're merging object values
            if (deep && copy && typeof copy == "object" && !copy.nodeType)
                target[name] = $.fn.extend(deep,
            // Never move original objects, clone them
								src || (copy.length != null ? [] : {})
							, copy);

            // Don't bring in undefined values
            else if (copy !== undefined)
                target[name] = copy;

        }

        // Return the modified object
        return target;
    };
    vmlss.fn.extend({
        getMaxMin: function(data, t)
        {//获取最大最小值
            var _min, _max;
            if (!t)
            {
                _min = _max = data[0].value;
                for (var i = 1; i < data.length; i++)
                {
                    if (data[i].value > _max)
                        _max = data[i].value;
                    if (data[i].value < _min)
                        _min = data[i].value;
                }
            } else
            {
                _min = _max = data.values[0].value[0];
                for (var i = 0; i < data.values.length; i++)
                {
                    for (var j = 0; j < data.values[i].value.length; j++)
                    {
                        if (data.values[i].value[j] > _max)
                            _max = data.values[i].value[j];
                        if (data.values[i].value[j] < _min)
                            _min = data.values[i].value[j];
                    }
                }
            }
            return { max: _max, min: _min, diff: _max - _min };
        },

        getInterval: function(temp1)
        {//获取一个比temp1大的最小整5值
            temp1 = parseInt(temp1 + temp1 / 50)
            var temp2, temp3;
            if (temp1 > 9)
            {
                temp2 = temp1.toString().substr(1, 1)
                if (temp2 > 4)
                {
                    temp3 = (parseInt((temp1 / (Math.pow(10, (temp1.toString().length - 1)))).toString()) + 1) * Math.pow(10, (temp1.toString().length - 1));
                }
                else
                {
                    temp3 = (parseInt((temp1 / (Math.pow(10, (temp1.toString().length - 1)))).toString()) + 0.5) * Math.pow(10, (temp1.toString().length - 1));
                }
            } else
            {
                if (temp1 > 4)
                    temp3 = 10;
                else
                    temp3 = 5;
            }
            return temp3;
        },

        getCalibration: function(num, cal, h)
        {
            if (cal != 0 && num % cal == 0 && cal * 15 <= h)
                return cal;
            var e = (num + 1) / 2;
            var rs = 0;
            e = (e * 15) > h ? (h / 15) : e;
            if (cal > e) cal = 0;
            for (var i = 3; i < e; i = i + 2)
            {
                if (num % i == 0)
                {
                    if (i < cal || cal == 0)
                    {
                        rs = i;
                    } else
                    {
                        if (cal - rs > i - cal)
                        {
                            return i;
                        } else
                            return rs;
                    }


                }
            }
            return rs == 0 ? cal : rs;
        }

    });

    vmlss.extend({
        vss: "1.0" //版本号
    });

    vmlss.fn.extend({
        cylinder_h: function(options)
        //数据格式：data[{name1,value1},{name2,value2}....]
        {//竖向柱状输出

            var _htmlstr = ""; //存放vml代码
            var _width, _height; //实际宽度和高度
            var _calnum = 9; //刻度份数
            var _maxcal = 0; //刻度最大值
            var _linecolor = "#69f"; //线条颜色
            var _strWidth = 40, _strHeight = 18;
            var _rectW = 30; //柱形宽度
            var _3Dline = _rectW / 2; //3D竖线的偏移位置
            var _3Doffsetx = _3Dline, _3Doffsety = _3Dline / Math.sqrt(2); //实际起始位置
            var _zoom = 1; //缩放倍数
            var _hspace = 0, _vspace = 15; //竖向、横向用于标题及刻度的空间
            var _mm; //存放最大最小值
            var _numsize = 8; //存放单个数字的宽度

            if (options.zoom && options.zoom != 0)
                _zoom = options.zoom;
            if (options.calnum && options.calnum != 0)
                _calnum = options.calnum;
            if (options.data.length <= 0)
            {
                return;
            }
            _height = options.height;
            if (options.title)
            {
                _hspace = 25;
                _height -= 25;
            }
            if (options.vLabel)
                _height -= 25;

            _height -= 18; //标尺高度

            if (options.hLabel)
                _vspace += 30;

            _mm = this.getMaxMin(options.data); //获取最大最小值

            //计算刻度标值用的宽度
            _vspace += (parseInt(_mm.max).toString().length * _numsize);
            _width = options.width - _vspace;


            _maxcal = this.getInterval(_mm.max);
            if (_maxcal.toString().length < 2)
            {
                _calnum = 5;
            } else
            {
                _calnum = this.getCalibration(parseInt(_maxcal.toString().substr(0, 3)), _calnum, _height);
            }

            _htmlstr += '<div><v:group style="position:absolute; width:' + options.width + 'px;height:' + options.height + 'px" coordsize="' + (options.width / _zoom) + ',' + (options.height / _zoom) + '">';

            /****************************绘制背景begin*********************************/
            _htmlstr += "<v:rect style='width:" + (_width) + "px;height:" + (_height) + "px;left:" + (_vspace) + "px;top:" + (_hspace) + "px;' fillcolor='#9cf' stroked='f'>";
            _htmlstr += "<v:fill rotate='t' angle='-45' focus='100%' type='gradient'/>";
            _htmlstr += "</v:rect>";
            /****************************绘制背景end*********************************/

            /****************************绘制坐标轴begin*********************************/
            //绘制y轴begin
            _htmlstr += "<v:line style='' from='" + _vspace + "," + _hspace + "' to='" + _vspace + "," + (_hspace + _height) + "'>";
            //设置y轴箭头
            _htmlstr += "<v:stroke startarrow='classic'/>";
            _htmlstr += "</v:line>";
            //绘制y轴end

            //绘制x轴begin
            _htmlstr += "<v:line style='' from='" + _vspace + "," + (_hspace + _height) + "' to='" + (_vspace + _width) + "," + (_hspace + _height) + "'>";
            //设置x轴箭头
            _htmlstr += "<v:stroke endarrow='classic'/>";
            _htmlstr += "</v:line>";
            //绘制x轴end

            //绘制3D线
            _htmlstr += "<v:line strokecolor=" + _linecolor + " style='' from='" + (_vspace + _3Doffsetx) + "," + (_hspace - _3Doffsety) + "' to='" + (_vspace + _3Doffsetx) + "," + (_hspace + _height - _3Doffsety) + "'>";
            _htmlstr += "</v:line>";
            _htmlstr += "<v:line strokecolor=" + _linecolor + " style='' from='" + (_vspace + _3Doffsetx) + "," + (_hspace + _height - _3Doffsety) + "' to='" + (_vspace + _width) + "," + (_hspace + _height - _3Doffsety) + "'>";
            _htmlstr += "</v:line>";
            _htmlstr += "<v:line strokecolor=" + _linecolor + " style='' from='" + _vspace + "," + (_hspace + _height) + "' to='" + (_vspace + _3Doffsetx) + "," + (_hspace + _height - _3Doffsety) + "'>";
            _htmlstr += "</v:line>";
            /****************************绘制坐标轴end*********************************/
            var endy = _height + _hspace;
            var _cal = _height / _calnum;
            /****************************绘制刻度begin*********************************/

            for (var i = 1; i < _calnum; i++)
            {
                _htmlstr += "<v:line strokecolor=" + _linecolor + " style='' from='" + (_vspace - 5) + "," + (endy - i * _cal) + "' to='" + _vspace + "," + (endy - i * _cal) + "'></v:line>";
                _htmlstr += "<v:line strokecolor=" + _linecolor + " style='' from='" + (_vspace) + "," + (endy - i * _cal) + "' to='" + (_vspace + _3Doffsetx) + "," + (endy - i * _cal - _3Doffsety) + "'></v:line>";
                _htmlstr += "<v:line strokecolor=" + _linecolor + " style='' from='" + (_vspace + _3Doffsetx) + "," + (endy - i * _cal - _3Doffsety) + "' to='" + (_vspace + _width) + "," + (endy - i * _cal - _3Doffsety) + "'></v:line>";
                _htmlstr += "<v:shape style='left:" + (0) + "px;top:" + (endy - i * _cal - 7) + "px;width:" + (_vspace - 10) + "px;height:15px;z-index:1'>";
                _htmlstr += "<v:textbox inset='0px,0px,0px,0px'><div style='text-align:right;'>" + (i * _maxcal / _calnum) + "&nbsp;</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }

            /****************************绘制刻度end*********************************/

            var data = options.data;
            _3Doffsetx += 2;
            var _rectspace = (_width - _3Doffsetx) / data.length;
            /****************************绘制柱状begin*********************************/
            for (var i = 0; i < data.length; i++)
            {
                // var temp_space = table_space / 2 + table_space * i + this._pillar_width * i;
                // 柱子
                var _ith = data[i].value * _height / _maxcal;
                _htmlstr += "<v:rect style='left:" + (_rectspace * i + _vspace + _3Doffsetx) + "px;top:" + (endy - _ith) + "px;width:" + (_rectW) + "px;height:" + _ith + "px;z-index:1' fillcolor='" + options.color[1][i] + "'>";
                _htmlstr += "<v:fill color2='" + options.color[0][i] + "' rotate='t' angle='180' focus='100%' type='gradient'/>";
                _htmlstr += "<v:extrusion backdepth='" + ((_3Dline) * 2 * _zoom) + "px' color='" + options.color[1][i] + "' on='t'/>";
                _htmlstr += "</v:rect>";
                //alert(rtn);
                _htmlstr += "<v:shape style='left:" + (_rectspace * i + _vspace + _3Doffsetx + _3Dline / Math.sqrt(2) + 4) + "px;top:" + (endy - _ith - _3Dline / Math.sqrt(2) - 15) + "px;width:" + (_rectW * 2) + "px;height:18px;z-index:1'>";
                _htmlstr += "<v:textbox inset='0px,0px,0px,0px'><div style='text-align:left'>" + data[i].value + "</div></v:textbox>";
                _htmlstr += "</v:shape>";
                _htmlstr += "<v:shape style='position:absolute;left:" + (_rectspace * i + _vspace + _3Doffsetx) + "px;top:" + (endy + 5) + "px;width:" + (_rectspace - 15) + "px;height:18px;z-index:1'>";
                _htmlstr += "<v:textbox inset='0px,0px,0px,0px'><div style='text-align:left;' title='" + data[i].name + "'>" + data[i].name + "</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            /****************************绘制柱状end*********************************/

            /****************************绘制标题begin*********************************/
            if (options.title)
            {
                _htmlstr += "<v:shape style='left:0;top:0px;width:" + options.width + "px;height:20px:z-index:1'>";
                _htmlstr += "<v:textbox><div style='text-align:center;'>" + options.title + "</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            if (options.vLabel)
            {
                _htmlstr += "<v:shape style='left:0;top:" + (options.height - 25) + "px;width:" + options.width + "px;height:25px;z-index:1'>";
                _htmlstr += "<v:textbox><div style='text-align:center;'>" + options.vLabel + "</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            if (options.hLabel)
            {
                _htmlstr += "<v:shape style='left:0;top:" + (options.height - options.hLabel.length * 20) / 2 + "px;width:20px;height:" + (options.hLabel.length * 25) + "px;z-index:1'>";
                _htmlstr += "<v:textbox><span style='text-align:center;vertical-align:middle;'>" + options.hLabel + "</span></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            /****************************绘制标题end*********************************/
            _htmlstr += "</v:group></div>"

            if (options.element)
            {
                options.element.innerHTML = _htmlstr;
            }
            return _htmlstr;

        }
    });

    vmlss.fn.extend({
        getSumAndMaxLength: function(data)
        {//获取值的总和及名称中的最大长度
            var temp = 0, temp1 = 0;
            for (i = 0; i < data.length; i++)
            {
                temp += data[i].value;
                if (data[i].name.length > temp1)
                    temp1 = data[i].name.length;
            }
            return { sum: temp, maxlength: temp1 };
        }
    });

    vmlss.fn.extend({//饼图
        picchart: function(options)
        //数据格式：data[{name1,value1},{name2,value2}....]
        {
            var _htmlstr = ""; //存放vml代码
            var _width, _height; //实际宽度和高度
            var _zoom = 1; //缩放倍数
            var _numsize = 8; //存放单个数字的宽度
            var _sl = 0; //存放总数及最大长度
            var _x, _y, _w, _h;
            var _hspace = 0;
            var _const = 22020096 + 1572864;
            var _offset = 20;
            var _wh = 0.8;
            if (options.zoom && options.zoom != 0)
                _zoom = options.zoom;

            if (options.data.length <= 0)
            {
                return;
            }
            _height = options.height;
            if (options.title)
            {
                _hspace = 55;
                _height -= 55;
            }

            _sl = this.getSumAndMaxLength(options.data);
            _height -= 20; //考虑3D
            if (options.width * _wh > _height)
            {
                _width = _height / _wh
            } else
            {
                _width = options.width;
                _height = _width * _wh
            }
            _x = _w = parseInt(_width / 2);
            _h = parseInt(_height / 2);
            _y = _h + _hspace;
            _htmlstr += '<div><v:group style="position:absolute; width:' + options.width + 'px;height:' + options.height + 'px" coordsize="' + (options.width / _zoom) + ',' + (options.height / _zoom) + '">';

            /****************************绘制标题begin*********************************/
            if (options.title)
            {
                _htmlstr += "<v:shape style='left:0;top:0px;width:" + _width + "px;height:20px:z-index:1'>";
                _htmlstr += "<v:textbox><div style='text-align:center;'>" + options.title + "</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            /****************************绘制标题end*********************************/
            var data = options.data;
            var _s = 0, _t; //_s累计弧度，_t当前弧度
            var _op = (_height + _width) / 5.5//
            var zi = data.length;
            var _htmln = "";
            /****************************绘制饼begin*********************************/
            for (var i = 0; i < data.length; i++)
            {
                var _p = Math.round(data[i].value * 100000 / _sl.sum) / 1000;
                var _ti = data[i].name + "：\n值：\t\t" + data[i].value + "\n总值：\t\t" + _sl.sum + "\n百分比：\t" + _p + "%";
                _t = Math.round(_const * _p / 100);
                var _b = _s * 2 / _const;
                _htmlstr += "<v:group style='width:10;height:10;z-index:";
                if (_b < 0.5 || _b > 1.5)
                {
                    zi--;
                } else
                    zi++;
                _htmlstr += zi;
                _b = (_s * 2 + _t) / _const; //当前分饼终线到原起点线的弧度分数
                _htmlstr += "' CoordSize='10,10'  title='" + (_ti) + "' onmouseover=vmlss.over(this) onmouseout=vmlss.out(this) _ol=" + (parseInt(_offset * Math.cos(Math.PI * _b))) + " _ot=" + (-parseInt(_offset * Math.sin(Math.PI * _b))) + " >"
                _htmlstr += "<v:shape id='' style='width:10;height:10;top" + _hspace + ";left:0;' CoordSize='10,10' strokecolor='white' fillcolor='" + options.color[1][i] + "'>";
                _htmlstr += "<v:extrusion backdepth='10px' color='" + options.color[0][i] + "' skewangle=90  on='t'/>";
                _htmlstr += "<v:path v='m " + _x + "," + _y + " ae " + _x + "," + _y + "," + _w + "," + _h + "," + _s + "," + (_t) + " xe'/>";
                _htmlstr += "</v:shape>";
                _htmlstr += "<v:shape style='width:60;height:20;left:" + (_x - 30 + parseInt(_op * Math.cos(Math.PI * _b))) + ";top:" + (_y - 8 - parseInt(_op * Math.sin(Math.PI * _b))) + ";z-index:1' CoordSize='10,10'>"
                _htmlstr += "<v:textbox inset='0px,0px,0px,0px' ><div align='left'>" + _p + "%</div></v:textbox>";
                _htmlstr += "</v:shape>";
                _htmlstr += "</v:group>"

                //示例图
                _htmln += "<v:shape style='position:relative;left:0;top:" + (i * 18) + " ;width:70;height:20px;'><v:textbox inset='0px,0px,0px,0px' style='width:80;'>";
                _htmln += "<div title='" + data[i].name + "' align='left' style='width:80px' >" + data[i].name + "</div>";
                _htmln += "</v:textbox></v:shape>";
                _htmln += "<v:rect style='position:relative;left:75;top:" + (i * 18) + ";width:50;height:14' fillcolor='" + options.color[1][i] + "' ><v:extrusion  diffusity=0.96 /><v:fill color2='" + options.color[0][i] + "' rotate='t' angle='0' focus='100%' type='gradient'/></v:rect>"
                _htmln += "<v:shape style='position:relative;left:75;top:" + (i * 18) + " ;width:50;height:14px;'><v:textbox inset='0px,0px,0px,0px'>";
                _htmln += "<div title='" + _p + "%' align='left'  >" + _p + "%</div>";
                _htmln += "</v:textbox></v:shape>";
                _s += _t;
            }
            /****************************绘制饼end*********************************/

            /****************************绘制图例begin*********************************/
            _htmlstr += "<v:group style='position:absolute;width:10;height:10;left:" + (_width + 10) + ";top:20' Coordsize='10,10'>";
            _htmlstr += "<v:rect style='position:relative;left:0;top:-5;width:130;height:" + (data.length * 18 + 5) + "'/>"
            _htmlstr += _htmln;
            _htmlstr += "</v:group>";
            /****************************绘制图例end*********************************/
            _htmlstr += "</v:group></div>"

            if (options.element)
            {
                options.element.innerHTML = _htmlstr;
            }
            return _htmlstr;
        }
    });
    vmlss.extend({
        over: function(obj)
        {

            obj.style.top = parseInt(obj.style.top) || 0;
            obj.style.left = parseInt(obj.style.left) || 0;
            obj.style.top = parseInt(obj.style.top) + parseInt(obj._ot);
            obj.style.left = parseInt(obj.style.left) + parseInt(obj._ol);
            obj.ozi = obj.style.zIndex;
            obj.style.zIndex = 999;

        },
        out: function(obj)
        {
            obj.style.top = parseInt(obj.style.top) - parseInt(obj._ot);
            obj.style.left = parseInt(obj.style.left) - parseInt(obj._ol);
            obj.style.zIndex = obj.ozi;
            //obj.style.left-=obj._ol;
        }
    });

    vmlss.fn.extend({ //折线图
        //数据格式：[names:[name1,name2],values:[{title1,value[value11,value21...]},{title2,value[value12,value22...]}...]]
        linechart: function(options)
        {
            var _htmlstr = ""; //存放vml代码
            var _width, _height; //实际宽度和高度
            var _calnum = 9; //刻度份数
            var _maxcal = 0; //刻度最大值
            var _linecolor = "#69f"; //线条颜色
            var _strWidth = 40, _strHeight = 18;
            var _rectW = 30; //柱形宽度
            var _3Dline = _rectW / 2; //3D竖线的偏移位置
            var _3Doffsetx = _3Dline, _3Doffsety = _3Dline / Math.sqrt(2); //实际起始位置
            var _zoom = 1; //缩放倍数
            var _hspace = 0, _vspace = 15; //竖向、横向用于标题及刻度的空间
            var _mm; //存放最大最小值
            var _numsize = 8; //存放单个数字的宽度
            var data = options.data;
            if (options.zoom && options.zoom != 0)
                _zoom = options.zoom;
            if (options.calnum && options.calnum != 0)
                _calnum = options.calnum;
            if (options.data.length <= 0)
            {
                return;
            }
            _height = options.height;
            if (options.title)
            {
                _hspace = 25;
                _height -= 25;
            }
            if (options.vLabel)
                _height -= 25;

            _height -= 18; //标尺高度

            if (options.hLabel)
                _vspace += 30;

            _mm = this.getMaxMin(options.data, "l"); //获取最大最小值

            //计算刻度标值用的宽度
            _vspace += (parseInt(_mm.max).toString().length * _numsize);
            _width = options.width - _vspace -(160- options.width / data.values.length) ;


            _maxcal = this.getInterval(_mm.max);
            if (_maxcal.toString().length < 2)
            {
                _calnum = 5;
            } else
            {
                _calnum = this.getCalibration(parseInt(_maxcal.toString().substr(0, 3)), _calnum, _height);
            }
            _htmlstr += '<div><v:group style="position:absolute; width:' + options.width + 'px;height:' + options.height + 'px" coordsize="' + (options.width / _zoom) + ',' + (options.height / _zoom) + '">';

            /****************************绘制背景begin*********************************/
            _htmlstr += "<v:rect style='width:" + (_width) + "px;height:" + (_height) + "px;left:" + (_vspace) + "px;top:" + (_hspace) + "px;' fillcolor='#9cf' stroked='f'>";
            _htmlstr += "<v:fill rotate='t' angle='-45' focus='100%' type='gradient'/>";
            _htmlstr += "</v:rect>";
            /****************************绘制背景end*********************************/

            /****************************绘制坐标轴begin*********************************/
            //绘制y轴begin
            _htmlstr += "<v:line style='' from='" + _vspace + "," + _hspace + "' to='" + _vspace + "," + (_hspace + _height) + "'>";
            //设置y轴箭头
            _htmlstr += "<v:stroke startarrow='classic'/>";
            _htmlstr += "</v:line>";
            //绘制y轴end

            //绘制x轴begin
            _htmlstr += "<v:line style='' from='" + _vspace + "," + (_hspace + _height) + "' to='" + (_vspace + _width) + "," + (_hspace + _height) + "'>";
            //设置x轴箭头
            _htmlstr += "<v:stroke endarrow='classic'/>";
            _htmlstr += "</v:line>";
            //绘制x轴end
            var endy = _height + _hspace;
            var _cal = _height / _calnum;
            /****************************绘制竖刻度begin*********************************/

            for (var i = 1; i < _calnum; i++)
            {
                _htmlstr += "<v:line strokecolor='#000' style='' from='" + (_vspace - 5) + "," + (endy - i * _cal) + "' to='" + _vspace + "," + (endy - i * _cal) + "'></v:line>";
                _htmlstr += "<v:line strokecolor=" + _linecolor + " style='' from='" + (_vspace) + "," + (endy - i * _cal) + "' to='" + (_vspace + _width) + "," + (endy - i * _cal) + "'></v:line>";
                _htmlstr += "<v:shape style='left:" + (0) + "px;top:" + (endy - i * _cal - 7) + "px;width:" + (_vspace - 5) + "px;height:15px;z-index:1'>";
                _htmlstr += "<v:textbox inset='0px,0px,0px,0px'><div style='text-align:right;'>" + (i * _maxcal / _calnum) + "&nbsp;</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }

            /****************************绘制竖刻度end*********************************/

            /****************************绘制横刻度及描点begin*********************************/
            _cal = _width / data.values.length;
            for (var i = 0; i < data.values.length; i++)
            {
                //绘制刻度
                _htmlstr += "<v:line strokecolor='#000' style='' from='" + (_vspace + i * _cal) + "," + (endy) + "' to='" + (_vspace + i * _cal) + "," + (endy + 5) + "'></v:line>";
                _htmlstr += "<v:shape style='left:" + (_vspace - 20 + i * _cal) + "px;top:" + (endy + 5) + "px;width:" + (_cal) + "px;height:15px;z-index:1'>";
                _htmlstr += "<v:textbox inset='0px,0px,0px,0px'><div style='text-align:left;'>" + (data.values[i].name) + "&nbsp;</div></v:textbox>";
                _htmlstr += "</v:shape>";

                //描点
                for (var j = 0; j < data.names.length; j++)
                {
                    var _ith = data.values[i].value[j] * _height / _maxcal;
                    _htmlstr += "<v:oval fillcolor='" + options.color[1][j] + "' style='z-index:2;width:4;height:4;left:" + (_vspace + i * _cal - 2) + ";top:" + (_hspace + _height - _ith - 2) + "' title='"+data.values[i].name+"： " + data.names[j] + "\n\t\t" + data.values[i].value[j] + "' />";
                    if (i != 0)//连线
                    {
                        _htmlstr += "<v:line title='" + data.names[j] + "' from='" + (_vspace + (i - 1) * _cal) + "," + ((_hspace + _height - (data.values[i - 1].value[j] * _height / _maxcal))) + "' to='" + (_vspace + i * _cal) + "," + ((_hspace + _height - data.values[i].value[j] * _height / _maxcal)) + "'  strokecolor='" + options.color[1][j] + "' style='z-index:1' />";
                    }
                }
            }

            /****************************绘制横刻度及描点end*********************************/

            /****************************绘制示例图begin*********************************/
            _htmlstr += "<v:group style='position:absolute;width:10;height:10;left:" + (options.width - 135) + ";top:20' Coordsize='10,10'>";
            _htmlstr += "<v:rect style='position:relative;left:0;top:-5;width:130;height:" + (data.names.length * 18 + 5) + "'/>"
            for (var i = 0; i < data.names.length; i++)
            {
                _htmlstr += "<v:shape style='position:relative;left:0;top:" + (i * 18) + " ;width:70;height:20px;'><v:textbox inset='0px,0px,0px,0px' style='width:80;'>";
                _htmlstr += "<div title='" + data.names[i] + "' align='left' style='width:80px' >" + data.names[i] + "</div>";
                _htmlstr += "</v:textbox></v:shape>";
                _htmlstr += "<v:rect title='"+data.names[i]+"' style='position:relative;left:75;top:" + (i * 18) + ";width:50;height:14' fillcolor='" + options.color[1][i] + "' ><v:extrusion  diffusity=0.96 /><v:fill color2='" + options.color[0][i] + "' rotate='t' angle='0' focus='100%' type='gradient'/></v:rect>";
            }
            _htmlstr += "</v:group>";
            /****************************绘制示例图end*********************************/


            /****************************绘制标题begin*********************************/
            if (options.title)
            {
                _htmlstr += "<v:shape style='left:0;top:0px;width:" + options.width + "px;height:20px:z-index:1'>";
                _htmlstr += "<v:textbox><div style='text-align:center;'>" + options.title + "</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            if (options.vLabel)
            {
                _htmlstr += "<v:shape style='left:0;top:" + (options.height - 25) + "px;width:" + options.width + "px;height:25px;z-index:1'>";
                _htmlstr += "<v:textbox><div style='text-align:center;'>" + options.vLabel + "</div></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            if (options.hLabel)
            {
                _htmlstr += "<v:shape style='left:0;top:" + (options.height - options.hLabel.length * 20) / 2 + "px;width:20px;height:" + (options.hLabel.length * 25) + "px;z-index:1'>";
                _htmlstr += "<v:textbox><span style='text-align:center;vertical-align:middle;'>" + options.hLabel + "</span></v:textbox>";
                _htmlstr += "</v:shape>";
            }
            /****************************绘制标题end*********************************/
            _htmlstr += "</v:group></div>"

            if (options.element)
            {
                options.element.innerHTML = _htmlstr;
            }
            return _htmlstr;
        }
    });
    /*,
		
		linechart ：function(options){//折线图输出
			
    },
		
		picchart : function(options){//饼图输出
			
    }*/

    vmlss.fn.init.prototype = vmlss.fn;
    vmlss.fn.extend({
        draw: function()
        { //输出

            if (this._options.data && (this._options.data.length > 0 || this._options.data.names))
            {
                return vmlss.fn[this._options.type](this._options);
            }
            else
            {
                alert("尚未设置数据，无法绘制，请用setData函数设置数据后执行绘制！");
            }
            return null;
        }
    });

})();