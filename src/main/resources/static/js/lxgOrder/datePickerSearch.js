/**
 * Created by 陈臻 on 2016/4/17.
 */
/**
 * @namespace _CalF
 * 日历控件所用便捷函数
 * */
_CalF = {
    // 选择元素
    selectOption:function(arg,context){
        var tagAll,n,eles=[],i,sub = arg.substring(1);
        context = context||document;
        if(typeof arg =='string'){
            switch(arg.charAt(0)){
                case '#':
                    return document.getElementById(sub);
                    break;
                case '.':
                    if(context.getElementsByClassName) return context.getElementsByClassName(sub);
                    tagAll = _CalF.selectOption('*',context);
                    n = tagAll.length;
                    for(i = 0;i<n;i++){
                        if(tagAll[i].className.indexOf(sub) > -1) eles.push(tagAll[i]);
                    }
                    return eles;
                    break;
                default:
                    return context.getElementsByTagName(arg);
                    break;
            }
        }
    },
    // 绑定事件
    bind:function(node,type,handler){
        node.addEventListener?node.addEventListener(type, handler, false):node.attachEvent('on'+ type, handler);
    },
    // 获取元素位置
    getPos:function (node) {
        var scrollx = document.documentElement.scrollLeft || document.body.scrollLeft,
            scrollt = document.documentElement.scrollTop || document.body.scrollTop;
        pos = node.getBoundingClientRect();
        return {top:pos.top + scrollt, right:pos.right + scrollx, bottom:pos.bottom + scrollt, left:pos.left + scrollx }
    },
    // 添加样式名
    addClass:function(c,node){
        if (!this.hasClass(node, c)) node.className += " " + c;
    },
    // 移除样式名
    removeClass:function(c,node){
        if (this.hasClass(node, c)) {
            var reg = new RegExp('(\\s|^)' + c + '(\\s|$)');
            node.className = node.className.replace(reg, ' ');
        }
    },
    hasClass:function (node, c) {
        return node.className.match(new RegExp('(\\s|^)' + c + '(\\s|$)'));
    },
    // 阻止冒泡
    stopPropagation:function(event){
        event = event || window.event;
        event.stopPropagation ? event.stopPropagation() : event.cancelBubble = true;
    },
    unbind:function removeEvents(target, type, func){
        if (target.removeEventListener)
            target.removeEventListener(type, func, false);
        else if (target.detachEvent)
            target.detachEvent("on" + type, func);
        else target["on" + type] = null;
    }
};
/**
 * @name Calender
 * @constructor
 * @created by VVG
 * @http://www.cnblogs.com/NNUF/
 * @mysheller@163.com
 * */
function Calender() {
    this.initialize.apply(this, arguments);
}
Calender.prototype = {
    constructor:Calender,
    // 模板数组
    _template :[
        '<div class="pop-date clearfix">'+
        '<div class="fl pop-date-bd">'+
        '<div class="pop-date-hd">'+
        '<i class="pop-date-arrow pop-date-arrow-left"></i>'+
        '<div class="pop-date-slt  f14">'+
        '</div>'+
        '</div>'+
        '<div class="pop-date-cont pt10">'+
        '<table class="pop-date-table curMonthShow" id="curMonthShow" width="100%" border="0" cellspacing="0" cellpadding="0">'+

        '</table>'+
        '</div>'+
        '</div>'+
        '<div class="fr pop-date-bd">'+
        '<div class="pop-date-hd">'+
        '<div class="next-month-title">'+
        '</div>'+
        '<i class="pop-date-arrow pop-date-arrow-right"></i>'+
        '</div>'+
        '<div class="pop-date-cont pt10">'+
        '<table class="pop-date-table nextMonthShow" id="nextMonthShow" width="100%" border="0" cellspacing="0" cellpadding="0">'+
        '</table>'+
        '</div>'+
        '</div>'+
        '</div>'],
    // 初始化对象
    initialize :function (options) {
        this.startid = options.startId; // 开始时间的input的ID
        this.endid = options.endId;
        this.startTimeInput = _CalF.selectOption('#'+ this.startid); // 获取INPUT元素
        this.endTimeInput = _CalF.selectOption('#'+ this.endid);
        this.startValue = options.startValue;//获取默认的开始时间
        this.endValue = options.endValue;//获取默认的结束时间
        this.isSelect = options.isSelect;   // 是否支持下拉SELECT选择年月，默认不显示
        this.inputEvent(); // input的事件绑定，获取焦点事件
        this.minusDaysElementId = options.minusDaysElementId;//用于显示用户共住几晚
    },
    // 创建日期最外层盒子，并设置盒子的绝对定位
    createContainer:function(){
        // 如果存在，则移除整个日期层Container
        var odiv = _CalF.selectOption('#'+ this.startid + '-date');
        if(!!odiv) odiv.parentNode.removeChild(odiv);
        var container = this.container = document.createElement('div');
        container.id = this.startid + '-date';
        container.style.position = "absolute";
        container.style.zIndex = 999;
        // 获取input表单位置inputPos
        var input = _CalF.selectOption('#' + this.startid),
            inputPos = _CalF.getPos(input);
        // 根据input的位置设置container高度
        container.style.left = inputPos.left + 'px';
        var containerTop = inputPos.bottom + 5;
        container.style.top = containerTop + 'px';
        // 设置日期层上的单击事件，仅供阻止冒泡，用途在日期层外单击关闭日期层
        _CalF.bind(container, 'click', _CalF.stopPropagation);
        document.body.appendChild(container);
    },
    // 渲染日期
    drawDate:function (odate) { // 参数 odate 为日期对象格式
        var dateWarp, titleDate, curMonth, nextMonth,year, month, date, days, weekStart,i,l,dayAreaHtml=[],nextDayAreaHtml=[],textNode;
        var nowDate = new Date(),nowyear = nowDate.getFullYear(),nowmonth = nowDate.getMonth(),
            nowdate = nowDate.getDate();
        this.dateWarp = dateWarp = document.createElement('div');
        dateWarp.className = 'calendar';

        dateWarp.innerHTML = this._template.join('');
        this.year = year = odate.getFullYear();
        this.month = month = odate.getMonth()+1;
        this.date = date = odate.getDate();
        this.titleDate = titleDate = _CalF.selectOption('.pop-date-slt', dateWarp)[0];


        var currentMonth = Number(nowmonth) + 1;
        var currentYear  = Number(nowyear);

        var nextMonthTitle = _CalF.selectOption('.next-month-title', dateWarp)[0];
        var nextMonth = this.month + 1;
        var nextYear = this.year;
        if(nextMonth > 12){
            nextMonth = 1;
            nextYear++;
        }
        nextMonthTitle.innerHTML= '<span>'+nextYear+'年'+nextMonth + '月</span>';

        var selectHtmls =[];
        selectHtmls.push('');


        selectHtmls.push('<div class="select-cur-month-value">'+this.year+'年'+this.month+'月<span class="select-current-month-icon"></span></div>');
        selectHtmls.push('<div class = "month-select-detail none-display" ><ul>');

        for(i = 1;i<13;i++){
            selectHtmls.push('<li class ="monthdetail" id="'+currentYear + '-' + currentMonth+'">'+ currentYear+'年'+currentMonth + '月</li>');

            currentMonth++;
            if(currentMonth > 12){
                currentMonth = 1;
                currentYear++;
            }
        }

        // selectHtmls.push('</select>');
        selectHtmls.push('</ul></div>');
        titleDate.innerHTML = selectHtmls.join('');
        // 绑定change事件
        //this.selectChange();

        // 获取模板中唯一的curMonth元素
        this.curMonth = curMonth = _CalF.selectOption('.curMonthShow',dateWarp)[0];
        // 获取本月天数
        days = new Date(year, month, 0).getDate();
        // 获取本月第一天是星期几
        weekStart = new Date(year, month-1,1).getDay();
        //设置增量dayCount 当等于7的时候换行置零并添加tr标签
        var dayCount = 0;
        dayAreaHtml.push('<tr><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th><th>日</th></tr>');
        dayAreaHtml.push('<tr>');

        // 开头显示空白段
        if (weekStart == 0){
            dayAreaHtml.push('<td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td>');
            dayCount += 6;
        }else{
            for (i = 0; i < weekStart-1; i++) {
                dayAreaHtml.push('<td class="date-outside"></td>');
                dayCount++;
            }
        }
        // 循环显示日期
        for (i = 1; i <= days; i++) {
            if (dayCount ==7) {
                dayCount = 0;
                dayAreaHtml.push('</tr><tr>');
            };
            dayAreaHtml.push('<td><span class="date-number enable-date" id="'+year+'-'+timeCover0(month)+'-'+timeCover0(i)+'">'+i+'</span></td>');

            dayCount++;
        }

        //把最后一行的数据补全
        for (var i = dayCount; i < 7; i++) {
            dayAreaHtml.push('<td class="date-outside"></td>');
        };

        dayAreaHtml.push('</tr>');
        curMonth.innerHTML = dayAreaHtml.join('');

        // 获取模板中唯一的nextMonth元素
        this.nextMonthElement = nextMonthElement = _CalF.selectOption('.nextMonthShow',dateWarp)[0];

        var nextMonthNum = month+1;
        var nextYearNum = year;
        if (nextMonthNum == 13){
            nextMonthNum = 1;
            nextYearNum = year+1;
        }
        // 获取本月天数
        nextMonthdays = new Date(year, month+1, 0).getDate();
        // 获取本月第一天是星期几
        nextMonthWeekStart = new Date(year, month,1).getDay();
        //设置增量dayCount 当等于7的时候换行置零并添加tr标签
        var dayCount = 0;
        nextDayAreaHtml.push('<tr><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th><th>日</th></tr>');
        nextDayAreaHtml.push('<tr>');

        // 开头显示空白段
        if (nextMonthWeekStart == 0){
            nextDayAreaHtml.push('<td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td><td class="date-outside"></td>');
            dayCount += 6;
        }else{
            for (i = 0; i < nextMonthWeekStart-1; i++) {
                nextDayAreaHtml.push('<td class="date-outside"></td>');
                dayCount++;
            }
        }


        // 循环显示日期
        for (i = 1; i <= nextMonthdays; i++) {
           
            if (dayCount ==7) {
                dayCount = 0;
                nextDayAreaHtml.push('</tr><tr>');
            };
            nextDayAreaHtml.push('<td><span class="date-number enable-date" id="'+nextYearNum+'-'+timeCover0(nextMonthNum)+'-'+timeCover0(i)+'">'+i+'</span></td>');

            dayCount++;
        }

        //把最后一行的数据补全
        for (var i = dayCount; i < 7; i++) {
            nextDayAreaHtml.push('<td class="date-outside"></td>');
        };

        nextDayAreaHtml.push('</tr>');
        nextMonthElement.innerHTML = nextDayAreaHtml.join('');

        // 如果存在，则先移除
        this.removeDate();
        // 添加
        this.container.appendChild(dateWarp);

        //选中默认的时间

        if (_CalF.selectOption('#'+this.startValue) != null && _CalF.selectOption('#'+this.startValue) != undefined){
            _CalF.addClass("select-element-start",_CalF.selectOption('#'+this.startValue));
        }

        if (_CalF.selectOption('#'+this.endValue) != null && _CalF.selectOption('#'+this.endValue) != undefined){
            _CalF.addClass("select-element-end",_CalF.selectOption('#'+this.endValue));
        }



        this.onRangeShow(this.endValue,this.startValue);
        //IE6 select遮罩
        var ie6  = !!window.ActiveXObject && !window.XMLHttpRequest;
        if(ie6) dateWarp.appendChild(this.createIframe());

        

        this.changeMonthClick();

        // 鼠标点击事件事件绑定
        this.elementMouseEvent();
        // 区域外事件绑定
        this.outClick();
    },

    createIframe:function(){
        var myIframe =  document.createElement('iframe');
        myIframe.src = 'about:blank';
        myIframe.style.position = 'absolute';
        myIframe.style.zIndex = '-1';
        myIframe.style.left = '-1px';
        myIframe.style.top = 0;
        myIframe.style.border = 0;
        myIframe.style.filter = 'alpha(opacity= 0 )';
        myIframe.style.width = this.container.offsetWidth + 'px';
        myIframe.style.height = this.container.offsetHeight + 'px';
        return myIframe;

    },

    // SELECT CHANGE 事件
    selectChange:function(){
        var selects,yearSelect,monthSelect,that = this;
        selects = _CalF.selectOption('select',this.titleDate);
        dateSelect = selects[0];
        _CalF.bind(dateSelect, 'change',function(){
            var date = dateSelect.value;
            var year = date.split('-')[0];
            var month = date.split('-')[1];
            that.drawDate(new Date(year, month-1, that.date));
        });
    },
    // 移除日期DIV.calendar
    removeDate:function(){
        var odiv = _CalF.selectOption('.calendar',this.container)[0];
        if(!!odiv) this.container.removeChild(odiv);
    },
    // 表单的事件
    inputEvent:function(){
        var that = this;
        _CalF.bind(that.endTimeInput, 'focus',function(){
            that.onFocusInputId = that.endTimeInput.id;
            that.createContainer();
            if (that.endTimeInput.value !=undefined && that.endTimeInput.value != "") {
                that.drawDate(new Date(that.endTimeInput.value));

            }else{
                that.drawDate(new Date());
            }
        });
        _CalF.bind(that.startTimeInput, 'focus',function(){
            that.onFocusInputId = that.startTimeInput.id;
            that.createContainer();
            if (that.startTimeInput.value !=undefined && that.startTimeInput.value != "") {
                that.drawDate(new Date(that.startTimeInput.value));
            }else{
                that.drawDate(new Date());
            }
        });
    },
    changeMonthClick:function(){
        that = this;
        var curDateTime = new Date(that.year +'-' +that.month +'-'+that.date);
         _CalF.selectOption(".pop-date-arrow-left",this.dateWarp)[0].onclick = function(){
            that.createContainer();
            var prevmonth = that.month-1;
            var preyear = that.year;
            if (prevmonth < 1){
                prevmonth = 12;
                preyear--;
            }
            that.drawDate(new Date(preyear +'-' +prevmonth +'-'+that.date));
         }
         _CalF.selectOption(".pop-date-arrow-right",this.dateWarp)[0].onclick = function(){
            that.createContainer();
            var nextmonth = that.month+1;
            var nextyear = that.year;
            if (nextmonth > 12){
                nextmonth = 1;
                nextyear++;
            }
            that.drawDate(new Date(nextyear +'-' +nextmonth +'-'+that.date));
         }
    },
    elementMouseEvent:function(){
        var elements = _CalF.selectOption('.enable-date', this.dateWarp),that = this;
        for (var i = 0; i < elements.length; i++) {
            elements[i].index = i;

            elements[i].onmouseover = function(){
                _CalF.addClass("on",elements[this.index]);
                var curElementId = elements[this.index].id;
                if (that.startValue == '' || that.startValue == undefined){
                    return;
                }

                //加上时间开始的小三角
                if (elements[this.index].id == that.startValue){
                        _CalF.removeClass("select-element-start",elements[this.index]);
                }

                //加上时间结束的小三角
                if (elements[this.index].id == that.endValue){
                        _CalF.removeClass("select-element-end",elements[this.index]);
                }

                

                var curElementDate = new Date(curElementId);

                var startTimeDate = new Date(that.startValue)
                var endTimeDate = new Date(that.endValue)

                if (curElementDate.getTime() > endTimeDate.getTime()){
                    if (_CalF.selectOption('#'+that.endValue) != null && _CalF.selectOption('#'+that.endValue) != undefined){
                            _CalF.removeClass("select-element-end",_CalF.selectOption('#'+that.endValue));
                    }
                }

                if (curElementDate.getTime() > startTimeDate.getTime()){
                    that.onRangeShow(curElementId,that.startValue);
                }

                // if (that.endValue == '' || that.endValue == undefined){
                //     return;
                // }
                // var endValueDate = new Date(that.endValue);
                // if (startTimeDate.getTime() < endValueDate.getTime()){
                //     that.onRangeShow(that.endValue,that.startValue);
                // }
            };
            elements[i].onmouseout = function(){
                _CalF.removeClass("on",elements[this.index]);

                
                var dateElement = _CalF.selectOption('.enable-date', this.dateWarp);

                for (var i = 0; i < dateElement.length; i++) {
                    _CalF.removeClass("span-area",dateElement[i]);
                    
                    //加上时间结束的小三角
                    if (dateElement[i].id ==that.endValue){
                        if(!_CalF.hasClass(dateElement[i],"select-element-end")){
                            _CalF.addClass("select-element-end",dateElement[i])
                        }
                    }

                    //加上时间开始的小三角
                    if (dateElement[i].id ==that.startValue){
                        if(!_CalF.hasClass(dateElement[i],"select-element-start")){
                            _CalF.addClass("select-element-start",dateElement[i])
                        }
                    }
                }
                that.onRangeShow(that.endValue,that.startValue);
            };
            elements[i].onclick = function(){
                if (that.onFocusInputId == that.startTimeInput.id){
                    var selectTime = new Date(elements[this.index].id);
                    var curEndTime = new Date(that.endValue);
                    var curStartTime = new Date(that.startValue);

                    if (curEndTime.getTime() < selectTime.getTime()){
                        var endDate =  new Date(selectTime.getTime() + 3*24*60*60*1000);
                        var endMonth = endDate.getMonth() +1;
                        that.endValue = endDate.getFullYear() +'-'+timeCover0(endMonth)+'-'+timeCover0(endDate.getDate());
                        that.endTimeInput.value = that.endValue;

                    }else if (curStartTime.getTime() > selectTime.getTime()){
                        that.startValue = elements[this.index].id;
                    }
                    var startSelectElement = _CalF.selectOption('#'+that.startValue);
                    if (startSelectElement!= null && startSelectElement!= undefined){
                        _CalF.removeClass("select-element-start",startSelectElement);
                    }
                    that.startValue = elements[this.index].id;
                    var dateElement = _CalF.selectOption('.enable-date', this.dateWarp);
                    for (var i = 0; i < dateElement.length; i++) {
                        _CalF.removeClass("span-area",dateElement[i]);
                    }
                    var startInputmonth = selectTime.getMonth()+1;
                    that.startTimeInput.value = selectTime.getFullYear() +'-'+timeCover0(startInputmonth)+'-'+timeCover0(selectTime.getDate());
                    _CalF.selectOption('#'+that.endTimeInput.id).focus();
                    that.onFocusInputId = that.endTimeInput.id;

                    startSelectElement = _CalF.selectOption('#'+that.startValue);
                    if (startSelectElement!= null && startSelectElement!= undefined){
                        _CalF.addClass("select-element-start",startSelectElement);
                    }


                    that.rebindElementEvent(elements[this.index].id);
                    return;
                }

                if (that.onFocusInputId == that.endTimeInput.id){
                    var endSelectTime = new Date(elements[this.index].id);
                    var endSelectMonth = endSelectTime.getMonth() + 1;
                    that.endTimeInput.value = endSelectTime.getFullYear() +'-'+timeCover0(endSelectMonth)+'-'+timeCover0(endSelectTime.getDate());;

                    var endSelectElement = _CalF.selectOption('#'+that.endValue);
                    if (endSelectElement!= null && endSelectElement!= undefined){
                        _CalF.removeClass("select-element-end",endSelectElement);
                    }

                    that.endValue = elements[this.index].id;
                    endSelectElement = _CalF.selectOption('#'+that.endValue);
                    if (endSelectElement!= null && endSelectElement!= undefined){
                        _CalF.addClass("select-element-end",endSelectElement);
                    }
                    that.removeDate();
                }
                //that.date = this.innerHTML;
                // that.input.value = that.year + '-' + that.month + '-' + that.date;

            }
        };

        var selectMonth = _CalF.selectOption('.select-cur-month-value', this.dateWarp)[0];

        selectMonth.onclick = function(){
            var monthdetail = _CalF.selectOption('.month-select-detail', this.dateWarp)[0];

            if(_CalF.hasClass(monthdetail,"none-display")){
                _CalF.removeClass("none-display",monthdetail);

                _CalF.addClass("detail",_CalF.selectOption('.select-current-month-icon', this.dateWarp)[0]);
            }else{
                _CalF.addClass("none-display",monthdetail);
                _CalF.removeClass("detail",_CalF.selectOption('.select-current-month-icon', this.dateWarp)[0]);
            }
        }

        var montitems = _CalF.selectOption('.monthdetail', this.dateWarp);

        for (var i = 0; i < montitems.length; i++) {
            montitems[i].index = i;

            montitems[i].onmouseover = function(){
                _CalF.addClass("focus",montitems[this.index]);
            };
            montitems[i].onmouseout = function(){
                _CalF.removeClass("focus",montitems[this.index]);
            };
            montitems[i].onclick = function(){
                var monthdetail = _CalF.selectOption('.month-select-detail', this.dateWarp)[0];
                monthdetail.style.display = 'none';
                var date = this.id;
                var year = date.split('-')[0];
                var month = date.split('-')[1];
                that.drawDate(new Date(year, month-1, that.date));
            }
        };


    },
    rebindElementEvent:function(startElementId){
        var that = this;
        var hoverStartDate = startElementId.split('-');//0-年  1-月 2-日
        var starttime = new Date(hoverStartDate[0],hoverStartDate[1] - 1,hoverStartDate[2]);
        var dateElement = _CalF.selectOption('.date-number', that.dateWarp);

        for (var i = 0; i < dateElement.length; i++) {
            _CalF.unbind(dateElement[i],'mouseover',function(){});
            _CalF.unbind(dateElement[i],'mouseout',function(){});
            _CalF.unbind(dateElement[i],'click',function(){});

            var date = new Date(dateElement[i].id);
            if (date.getTime() < starttime.getTime()){
                _CalF.removeClass("enable-date",dateElement[i]);
                _CalF.addClass("disable-date",dateElement[i]);
            }else{
                _CalF.removeClass("disable-date",dateElement[i]);
                _CalF.addClass("enable-date",dateElement[i]);
            }
        }

        that.elementMouseEvent();
    },

    onRangeShow:function(curElementId,startElementId){
        var that = this;
        var curTime = new Date(curElementId);
        var startTime = new Date(startElementId);
        if (curTime.getTime() < startTime.getTime()){
            //do nothing;
        }else if (curTime > startTime){
            var hoverStartDate = startElementId.split('-');//0-年  1-月 2-日
            var hoverCurDate = curElementId.split('-');//0-年  1-月 2-日
            var starttime = new Date(hoverStartDate[0],hoverStartDate[1] - 1,hoverStartDate[2]);
            var curtime = new Date(hoverCurDate[0],hoverCurDate[1] - 1,hoverCurDate[2]);
            var index = 0;
            var milisecond = starttime.getTime() + 1 * 24 * 60 * 60 * 1000;
            starttime = new Date(milisecond);
            while (curtime.getTime() >  starttime.getTime()) {
                var curmonth = starttime.getMonth() + 1;
                var dateAreaElements = _CalF.selectOption('#'+starttime.getFullYear() +'-'+ timeCover0(curmonth)+'-'+timeCover0(starttime.getDate()));
                if (dateAreaElements != null && dateAreaElements != undefined){
                    _CalF.addClass("span-area",dateAreaElements);
                }
                starttime = new Date(starttime.getTime() + 1 * 24 * 60 * 60 * 1000)
            }
        }

    },
    // 鼠标在对象区域外点击，移除日期层
    outClick:function(){
        var that = this;
        _CalF.bind(document, 'click',function(event){
            event = event || window.event;
            var target = event.target || event.srcElement;
            if(target == that.startTimeInput || target == that.endTimeInput)return;
            that.removeDate();
            var endTime = new Date(that.endTimeInput.value);
            var startTime = new Date(that.startTimeInput.value);
            if (startTime.getTime() > endTime.getTime()){
                var newEndTime = new Date(startTime.getTime() + 3*24*60*60*1000);
                var endTimeMonth = newEndTime.getMonth() + 1;
                var newEndTime = newEndTime.getFullYear()+"-"+timeCover0(endTimeMonth)+"-"+timeCover0(newEndTime.getDate());
                that.endTimeInput.value=newEndTime;
                that.endValue = newEndTime;
            }
            
        })
    }
};
