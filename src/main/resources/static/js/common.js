String.prototype.format = function(o){
    var self = this;
    for(var i in o)self = self.replace(new RegExp("\\$\\{" + i + "\\}", "g"), o[i]);
    return self;
};

function Base64() {

    // private property
    _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    // public method for encoding
    this.encode = function (input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = _utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
                _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    }

    // public method for decoding
    this.decode = function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = _utf8_decode(output);
        return output;
    }

    // private method for UTF-8 encoding
    _utf8_encode = function (string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }
        return utftext;
    }

    // private method for UTF-8 decoding
    _utf8_decode = function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while ( i < utftext.length ) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    }
}

/**
 * jQuery serializeObject
 * @copyright 2014, macek <paulmacek@gmail.com>
 * @link https://github.com/macek/jquery-serialize-object
 * @license BSD
 * @version 2.5.0
 */
!function(e,i){if("function"==typeof define&&define.amd)define(["exports","jquery"],function(e,r){return i(e,r)});else if("undefined"!=typeof exports){var r=require("jquery");i(exports,r)}else i(e,e.jQuery||e.Zepto||e.ender||e.$)}(this,function(e,i){function r(e,r){function n(e,i,r){return e[i]=r,e}function a(e,i){for(var r,a=e.match(t.key);void 0!==(r=a.pop());)if(t.push.test(r)){var u=s(e.replace(/\[\]$/,""));i=n([],u,i)}else t.fixed.test(r)?i=n([],r,i):t.named.test(r)&&(i=n({},r,i));return i}function s(e){return void 0===h[e]&&(h[e]=0),h[e]++}function u(e){switch(i('[name="'+e.name+'"]',r).attr("type")){case"checkbox":return"on"===e.value?!0:e.value;default:return e.value}}function f(i){if(!t.validate.test(i.name))return this;var r=a(i.name,u(i));return l=e.extend(!0,l,r),this}function d(i){if(!e.isArray(i))throw new Error("formSerializer.addPairs expects an Array");for(var r=0,t=i.length;t>r;r++)this.addPair(i[r]);return this}function o(){return l}function c(){return JSON.stringify(o())}var l={},h={};this.addPair=f,this.addPairs=d,this.serialize=o,this.serializeJSON=c}var t={validate:/^[a-z_][a-z0-9_]*(?:\[(?:\d*|[a-z0-9_]+)\])*$/i,key:/[a-z0-9_]+|(?=\[\])/gi,push:/^$/,fixed:/^\d+$/,named:/^[a-z0-9_]+$/i};return r.patterns=t,r.serializeObject=function(){return new r(i,this).addPairs(this.serializeArray()).serialize()},r.serializeJSON=function(){return new r(i,this).addPairs(this.serializeArray()).serializeJSON()},"undefined"!=typeof i.fn&&(i.fn.serializeObject=r.serializeObject,i.fn.serializeJSON=r.serializeJSON),e.FormSerializer=r,r});

$.extend(FormSerializer.patterns, {
    validate: /^.*$/
});

//  json2.js
//  2016-10-28
//  Public Domain.
//  NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.
//  See http://www.JSON.org/js.html
//  This code should be minified before deployment.
//  See http://javascript.crockford.com/jsmin.html

"object"!==typeof JSON&&(JSON={});
(function(){function k(a){return 10>a?"0"+a:a}function p(){return this.valueOf()}function q(a){r.lastIndex=0;return r.test(a)?'"'+a.replace(r,function(a){var c=u[a];return"string"===typeof c?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+a+'"'}function l(a,c){var g,d,m=e,f,b=c[a];b&&"object"===typeof b&&"function"===typeof b.toJSON&&(b=b.toJSON(a));"function"===typeof h&&(b=h.call(c,a,b));switch(typeof b){case "string":return q(b);case "number":return isFinite(b)?String(b):"null";case "boolean":case "null":return String(b);case "object":if(!b)return"null";e+=n;f=[];if("[object Array]"===Object.prototype.toString.apply(b)){d=b.length;for(a=0;a<d;a+=1)f[a]=l(a,b)||"null";c=0===f.length?"[]":e?"[\n"+e+f.join(",\n"+e)+"\n"+m+"]":"["+f.join(",")+"]";e=m;return c}if(h&&"object"===typeof h)for(d=h.length,a=0;a<d;a+=1)"string"===typeof h[a]&&(g=h[a],(c=l(g,b))&&f.push(q(g)+(e?": ":":")+c));else for(g in b)Object.prototype.hasOwnProperty.call(b,g)&&(c=l(g,b))&&f.push(q(g)+(e?": ":":")+c);c=0===f.length?"{}":e?"{\n"+e+f.join(",\n"+e)+"\n"+m+"}":"{"+f.join(",")+"}";e=m;return c}}var v=/^[\],:{}\s]*$/,w=/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,x=/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,y=/(?:^|:|,)(?:\s*\[)+/g,r=/[\\"\u0000-\u001f\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,t=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;"function"!==typeof Date.prototype.toJSON&&(Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+k(this.getUTCMonth()+1)+"-"+k(this.getUTCDate())+"T"+k(this.getUTCHours())+":"+k(this.getUTCMinutes())+":"+k(this.getUTCSeconds())+"Z":null},Boolean.prototype.toJSON=p,Number.prototype.toJSON=p,String.prototype.toJSON=p);var e,n,u,h;"function"!==typeof JSON.stringify&&(u={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},JSON.stringify=function(a,c,g){var d;n=e="";if("number"===typeof g)for(d=0;d<g;d+=1)n+=" ";else"string"===typeof g&&(n=g);if((h=c)&&"function"!==typeof c&&("object"!==typeof c||"number"!==typeof c.length))throw Error("JSON.stringify");return l("",{"":a})});"function"!==typeof JSON.parse&&(JSON.parse=function(a,c){function g(a,e){var f,b,d=a[e];if(d&&"object"===typeof d)for(f in d)Object.prototype.hasOwnProperty.call(d,f)&&(b=g(d,f),void 0!==b?d[f]=b:delete d[f]);return c.call(a,e,d)}a=String(a);t.lastIndex=0;t.test(a)&&(a=a.replace(t,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)}));if(v.test(a.replace(w,"@").replace(x,"]").replace(y,"")))return a=eval("("+a+")"),"function"===typeof c?g({"":a},""):a;throw new SyntaxError("JSON.parse");})})();

$(function(){
	
	$(".history-cont:first").addClass("history-cont-top");
	
	var _windowWidth = $(window).width();
	if ( _windowWidth > 1400 ) {
		$(".resize-img-bd").addClass("resize-img-bd-wid");
	}else{
		$(".resize-img-bd").removeClass("resize-img-bd-wid");
	}
		
	
	var _resize = function(){		
		
		var _windowWidth = $(window).width();
		if ( _windowWidth > 1400 ) {
		$(".resize-img-bd").addClass("resize-img-bd-wid");
	}else{
		$(".resize-img-bd").removeClass("resize-img-bd-wid");
	}
		
	};
	_resize();
	$(window).on('resize', _resize);
	
	
	$(".employ-list li:first").addClass("employ-list-open");
	$(".employ-list li:first").find(".employ-list-cont").show();
	$(".employ-list li .employ-list-hd").click(function(){
		
		if( $(this).parents("li").hasClass("employ-list-open") ){
			return;
		}
		
		$(".employ-list li").removeClass("employ-list-open");
		$(".employ-list-cont").slideUp();
		$(this).parents("li").addClass("employ-list-open");
		$(this).siblings(".employ-list-cont").slideDown();
	})
	
	var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
	if(isWin){
		$("body").addClass("body-win-font");
	}

	$(".advantage-list-icon-1,.advantage-list-icon-2").bind('inview', function(event, visible) {
		if (visible) {
			$(".advantage-list-icon-1,.advantage-list-icon-2").fadeIn(1500);
		}
	});

	$(".advantage-list-icon-3,.advantage-list-icon-4").bind('inview', function(event, visible) {
		if (visible) {
			$(".advantage-list-icon-3,.advantage-list-icon-4").fadeIn(1500);
		}
	});

	$(".index-top-banner-fade").animate({left:"0px",opacity:"1"},1000);

	//加载用户的月订单状态
	$.ajax({
		type:"POST",
		url:"getOrderNum",
		dataType:"json",
		success:function(data){
			var orderData = data.data;
			$(".made-fee-in-week-order").html(orderData[5]);
			$(".made-free-cancel-order").html(orderData[4]);
			$(".except-order").html(orderData[1]);
			$(".has-cancel-in-week-order").html(orderData[6]);
			$(".made-pay-cancel-order").html(orderData[3]);
			$(".editing-order").html(orderData[7]);
			$(".not-checkIn-order").html(orderData[2]);
			$(".has-edited-in-week-order").html(orderData[8]);
		}
	});
});


function selectDropdownEvent(obj){
	var thisspan=obj.parent().find("span[class=\"dispaly-value\"]");
	var thisul=obj.parent().find("ul");
	var thisinput = obj.parent().find("input");
	if(thisul.css("display")=="none"){
		obj.parent().addClass("open");
		if(thisul.height()>350){
			thisul.css({height:"350"+"px","overflow-y":"scroll"})
		};
		thisul.fadeIn("100");
		thisul.find("li").click(function(){
				$(this).addClass("hover");
				thisspan.html($(this).text());
				thisul.fadeOut("100");
				thisul.parent().removeClass("open");
				thisinput.val($(this).attr("nation-id"))
			})
			.hover(function(){
				$(this).addClass("hover");
			},function(){$(this).removeClass("hover");});
		thisul.find("li.title").unbind('mouseenter').unbind('mouseleave').unbind('click').click(function(){stopEvent(event);});
	}else{
		thisul.fadeOut("fast");
		obj.parent().removeClass("open");
	}
}

function hideSelect(obj) {
	$(document).bind("click",function (event) {
		event = event || window.event;
		var target = event.target || event.srcElement;
		if(target == obj[0] || target == obj.parent().find("span[class=\"icon-item\"]")[0]){
			return;
		}
		var objParent = obj.parent();
		objParent.removeClass("open");
		objParent.find("ul").hide();
	});
}

function hideElement(obj,objBtn){
	$(document).bind("click",(function (event) {
			event = event || window.event;
			var target = event.target || event.srcElement;
			if(target == obj[0] || obj.find(target).size() > 0){
				return;
			}else if(target == objBtn[0]){
				if (obj.hasClass("none-display")){
					obj.removeClass("none-display");

				}else{
					obj.addClass("none-display");
				}
			}else {
				obj.addClass("none-display");
			}
		})
	);
}

function timeCover0(s) {
	return s < 10 ? '0' + s: s;
}

function getEditDetailInfo(data) {
	if (data == undefined || data == null){
		return "不可修改";
	}

	if (data.policies == undefined || data.policies == null){
		return "不可修改";
	}

	if (data.policies.amendment == undefined || data.policies.amendment.length <= 0){
		return "不可修改";
	}

	var editHtml = "";
	for (var i = data.policies.amendment.length - 1; i >= 0; i--) {
		var policy = data.policies.amendment[i];


		if(policy.startDate != '') {
			var startPolicyTime = convertDate2OptionDay(policy.endDate.split("T"), "");
			var endPolicyTime = convertDate2OptionDay(policy.startDate.split("T"), "");
			if(endPolicyTime === '2049-12-31 23:59'){
				editHtml += startPolicyTime + '之后';
			}else{
				editHtml += startPolicyTime + ' 至' + endPolicyTime;
			}

		}
		editHtml +=  policy.remark+'<br/>';
	}
	return editHtml;
}

function getRenameDetailInfo(data,checkIn) {
	if (data == undefined || data == null){
		return "不可改名";
	}

	if (data.policies == undefined || data.policies == null){
		return "不可改名";
	}

	if (data.policies.changename == undefined || data.policies.changename.length <= 0){
		return "不可改名";
	}

	var renameHtml = "";
	for (var i = data.policies.changename.length - 1; i >= 0; i--) {
		var policy = data.policies.changename[i];

		if(policy.startDate != ''){
			var startPolicyTime = convertDate2OptionDay(policy.endDate.split("T"),"");

			var endPolicyTime = convertDate2OptionDay(policy.startDate.split("T"),"");
			if(endPolicyTime === '2049-12-31 23:59'){
				renameHtml += startPolicyTime + '之后';
			}else{
				renameHtml += startPolicyTime + ' 至' + endPolicyTime;
			}
		}
		renameHtml += policy.remark+'<br/>';
	}
	return renameHtml;
}

function getRefundableDetailInfo(data,checkIn) {
	if (data == undefined || data == null){
		return "不可取消、不可修改。<br/>如果取消预订或未如期入住，酒店将收取全额费用";
	}

	if (data.policies == undefined || data.policies == null){
		return "不可取消、不可修改。<br/>如果取消预订或未如期入住，酒店将收取全额费用";
	}
	if (!data.policies.cancellation){
		return "不可取消、不可修改。<br/>如果取消预订或未如期入住，酒店将收取全额费用";
	}
	var refundableHtml = "";
	for (var i = data.policies.cancellation.length - 1; i >= 0; i--) {
		var policy = data.policies.cancellation[i];

		var endDate = policy.startDate;

		var endDateDay = endDate.split("T")[0].split("-");
		var endTime = endDate.split("T")[1].split(":");
		var month = Number(endDateDay[1])-1;
		var endDateTime = new Date(endDateDay[0],month,endDateDay[2],endTime[0],endTime[1],0);
		var endDayDate = new Date(endDateDay[0],month,endDateDay[2],0,0,0);
		var nowDateTime = new Date();
		if (nowDateTime.getTime() > endDateTime.getTime()){
			continue;
		}



		if (policy.endDate == '' || policy.endDate == null){
			refundableHtml +='当前时间';
		}

		var startPolicyTime = convertDate2OptionDay(policy.endDate.split("T"),"");

		var endPolicyTime = convertDate2OptionDay(policy.startDate.split("T"),"");
		if (checkIn.getTime() <= endDayDate.getTime() ){
			endPolicyTime ="之后";
		}else{
			endPolicyTime = ' 至 ' + endPolicyTime;
		}


		refundableHtml += startPolicyTime + endPolicyTime + '   取消费用：' + policy.remark + '<br/>';

		//refundableHtml += startPolicyTime + ' 至' + endPolicyTime + '   取消费用：收取' + policy.nights + '晚房费的' + policy.amount + '%作为取消费<br/>';

	}
	if(refundableHtml == ''){
		refundableHtml = '不可取消、不可修改。<br/>如果取消预订或未如期入住，酒店将收取全额费用';
	}
	return refundableHtml;
}

function convertMiliSecond2DateString(milisecond){
	var time = new Date(milisecond);
	var month = time.getMonth() + 1;
	return time.getFullYear()+"-"+timeCover0(month)+"-"+timeCover0(time.getDate());
}
function convertMiliSecond2DateTimeExcSecondString(milisecond){
	if(!milisecond){
		return '';
	}
	var time = new Date(milisecond);
	var month = time.getMonth() + 1;
	return time.getFullYear()+"-"+timeCover0(month)+"-"+timeCover0(time.getDate())+" "
		+ timeCover0(time.getHours()) +":"+ timeCover0(time.getMinutes());
}
function convertMiliSecond2DateTimeExcMinuString(milisecond){
	var time = new Date(milisecond);
	var month = time.getMonth() + 1;
	return time.getFullYear()+"-"+timeCover0(month)+"-"+timeCover0(time.getDate())+" "
		+ timeCover0(time.getHours()) +":00";
}

function convertMiliSecond2DateTimeString(milisecond){
	if (milisecond == undefined || milisecond == null){
		return "";
	}
	var time = new Date(milisecond);
	var month = time.getMonth() + 1;
	return time.getFullYear()+"-"+timeCover0(month)+"-"+timeCover0(time.getDate())+" "
		+ timeCover0(time.getHours()) +":"+ timeCover0(time.getMinutes())+":"+timeCover0time.getSeconds();
}

function getHotelUrl(hotelId,imgId,extension){
	var imgUrl = "http://mogux.b0.upaiyun.com/hotel/";
	var hotelIdShort = hotelId.substring(hotelId.length - 3);
	imgUrl += hotelIdShort + "/" + hotelId +"/" + imgId+extension;
	return imgUrl;
}

function getDateDay(milisecond){
	var time = new Date(milisecond);
	var dayNames = new Array("周日","周一","周二","周三","周四","周五","周六");
	return dayNames[time.getDay()];
}

function convertDate2OptionDayCN(dateStr){
	var date = dateStr[0].split("-");
	var time = dateStr[1].split(":");
	return date[0]+'年'+date[1]+'月'+date[2]+'日 '+time[0] + '时'+time[1]+'分前';
}

function convertDate2OptionDay(dateStr,apendStr){
	if(dateStr == null || dateStr == ''){
		return '';
	}
	var time = dateStr[1].split(":");
	return dateStr[0] + ' '+time[0]+":"+time[1]+apendStr;
}

function convert2DayDate(checkInStr){
	var checkInDates = checkInStr.split("-");
	return new Date(checkInDates[0],Number(checkInDates[1]) - 1,checkInDates[2],0,0,0)
}

function convertScore(score){
	if (score >= 9.5){
		return "优异的";
	}
	if (score < 9.5 && score >= 9){
		return "好极了";
	}
	if (score < 9 && score >= 8.6){
		return "很棒";
	}
	if (score < 8.6 && score >= 8){
		return "非常好";
	}
	if (score < 8 && score >= 7){
		return "好";
	}
	if (score < 7){
		return "还行";
	}
}

var firseResult = "loading";
var retAllRs = "loading";
var requestWiResult = 0;
var detailInfoCollection="";

function showDetailInfos(index,type){
	if(detailInfoCollection[index]){
		var detail = detailInfoCollection[index];

		if(detail[type]){
			var hotelCodes = "";

			for(var hotelCode in detail[type]){
				hotelCodes += detail[type][hotelCode] +", ";
			}
			var closeBtn = '<div id="closeDebugDetailForm" onclick="$(\'#detailDebugDiv\').addClass(\'none-display\')">关闭</div>';

			if($("#detailDebugDiv").size() == 0){
				$("body").append('<div style="position:fixed;z-index:10000000;width:500px;height:300px;background:#000;color:#FFF;top:10px;right:50%;overflow:auto" id="detailDebugDiv"></div>');
			}

			$("#detailDebugDiv").removeClass("none-display").html(hotelCodes+closeBtn);
		}
	}
}

function showDebugInfo(data){
	//data.debugInfo !=""; added by zengtao 2016.09.07 for MoguService.
	if (data.debugInfo != undefined && data.debugInfo != null && data.debugInfo !=""){
		if (data.debugInfo.indexOf("chenzhen!") < 0) {
			if ($("#testInfoArea").size() > 0) {
				if (data['time'] == undefined || data['time'] == null) {
					$("#testInfoArea").append('<br/>' + data.debugInfo);
				} else {
					$("#testInfoArea").append( '<br/>' + data.debugInfo + ' ，' + data['time']);
				}
			} else {
				var closeSearchBtn = '<div class="close-debug-search-form" id="debugSearchClose">关闭</div>';
				$("body").append('<div style="position:fixed;z-index:10000000;width:500px;height:300px;background:#000;color:#FFF;top:10px;left:10px;opacity: 0.4;overflow:auto" id="testInfoArea"></div>')
				$("body").append('<div class="none-display" id="debugSearchBtnToggle">打开search调试信息</div>');
				if (data['time'] == undefined || data['time'] == null) {
					$("#testInfoArea").append( '<br/>' + data.debugInfo+closeSearchBtn);
				} else {
					$("#testInfoArea").append( '<br/>' + data.debugInfo + ' ，' + data['time']+closeSearchBtn);
				}
				//moveDiv("testInfoArea");
				bindDebugOpenEvent();
				bindDebugCloseEvent();
			}
		}else{
			var debugInfos = data.debugInfo.split("chenzhen!");
			var detailInfo = "";
			if(debugInfos[1].indexOf("#chenzhen#") > 0){
				detailInfo = debugInfos[1].split("#chenzhen#");
			}
			detailInfoCollection = eval('(' + detailInfo[1] + ')');
			if(data.data.searchData != null && data.data.searchData.length >0) {
				if (firseResult == "loading") {
					var endDate = new Date();
					firseResult = endDate.getTime() - startTime.getTime();
				}
				requestWiResult++;
			}



			if(data.data.isOver == "true" || data.data.isOver == true){
				var endDate = new Date();
				retAllRs = endDate.getTime() - startTime.getTime();
			}else if(data['time'] != null && data['time'] == endTimes){
				var endDate = new Date();
				retAllRs = endDate.getTime() - startTime.getTime();
			}else{
				//do nothing
			}
			var gdsInfo = detailInfo[0].replace("fTChen",firseResult);
			gdsInfo = gdsInfo.replace("aRTChen",retAllRs);
			gdsInfo = gdsInfo.replace("tRNWRChen",requestWiResult);
			var closeGdsBtn = '<div class="close-debug-gds-form" id="debugGdsClose">关闭</div>';

			if ($("#testInfoArea").size() > 0) {
				if (data['time'] == undefined || data['time'] == null) {
					gdsInfo = gdsInfo.replace("tRNChen","unkown");
					$("#testInfoArea").append('<br/>' + debugInfos[0]);

					$("#testDebugInfoArea").html(gdsInfo+closeGdsBtn);

				} else {
					gdsInfo = gdsInfo.replace("tRNChen",data['time']);
					$("#testInfoArea").append( '<br/>' +debugInfos[0] + ' ，' + data['time']);
					$("#testDebugInfoArea").html(gdsInfo+closeGdsBtn);
				}

				bindGdsCloseEvent();
			} else {
				var closeSearchBtn = '<div class="close-debug-search-form" id="debugSearchClose">关闭</div>';


				$("body").append('<div style="position:fixed;z-index:10000000;width:500px;height:600px;background:#000;color:#FFF;top:10px;left:10px;opacity: 0.4;overflow:auto" id="testInfoArea"></div>');
				$("body").append('<div class="none-display" style="position:fixed;z-index:10000000;width:800px;height:600px;background:#000;color:#FFF;top:10px;right:10px;opacity: 0.4;overflow:auto" id="testDebugInfoArea"></div>');
				$("body").append('<div class="none-display" id="debugSearchBtnToggle">打开search调试信息</div>');
				$("body").append('<div  id="debugGdsBtnToggle">打开gds调试信息</div>');
				if (data['time'] == undefined || data['time'] == null) {
					 gdsInfo = gdsInfo.replace("tRNChen","unkown");
					$("#testInfoArea").append( '<br/>' +debugInfos[0]+closeSearchBtn);
					$("#testDebugInfoArea").html('<br/>'+ gdsInfo+closeGdsBtn);
				} else {
					 gdsInfo = gdsInfo.replace("tRNChen",data['time']);
					$("#testInfoArea").append('<br/>' + debugInfos[0] + ' ，' + data['time']+closeSearchBtn);
					$("#testDebugInfoArea").html( gdsInfo+closeGdsBtn);
				}

				//moveDiv("testInfoArea");
				//moveDiv("testDebugInfoArea");
				bindDebugOpenEvent();
				bindDebugCloseEvent();
			}
		}

	}
}

function bindDebugCloseEvent(){
	$("#debugSearchClose").click(function(){
		$("#testInfoArea").addClass("none-display");
		$("#debugSearchBtnToggle").removeClass("none-display");
		stopEvent(event);
	});

	bindGdsCloseEvent();

}

function bindDebugOpenEvent(){
	$("#debugSearchBtnToggle").click(function(){
		$("#testInfoArea").removeClass("none-display");
		$("#debugSearchBtnToggle").addClass("none-display");
		stopEvent(event)
	});

	$("#debugGdsBtnToggle").click(function(){
		$("#testDebugInfoArea").removeClass("none-display");
		$("#debugGdsBtnToggle").addClass("none-display");
		stopEvent(event);
	});

}
function bindGdsCloseEvent(){
	$("#debugGdsClose").click(function(){
		$("#testDebugInfoArea").addClass("none-display");
		$("#debugGdsBtnToggle").removeClass("none-display");
		stopEvent(event);
	});
}


function stopEvent(event){
	//取消事件冒泡
	var e=arguments.callee.caller.arguments[0]||event; //若省略此句，下面的e改为event，IE运行可以，但是其他浏览器就不兼容
	if (e && e.stopPropagation) {
		// this code is for Mozilla and Opera
		e.stopPropagation();
	} else if (window.event) {
		// this code is for IE
		window.event.cancelBubble = true;
	}
}

function moveDiv(id){
	var o=document.getElementById(id);

	o.onmousedown = function(e) {
		e = e||window.event;
		var x=e.layerX||e.offsetX;
		var y=e.layerY||e.offsetY;
		x=x-document.body.scrollLeft;
		y=y-document.body.scrollTop;
		document.onmousemove = function(e){
			e=e||window.event;
			o.style.left=(e.clientX-x)+"px";
			o.style.top=(e.clientY-y)+"px";
		};
		document.onmouseup=function(){
			document.onmousemove=null;
		};
	};
};
/**
 * edit by wangjie on 2016/9/8.
 * 返回顶部
 */

function backToTop() {
		$("body").append('<div class="side_right fr">');
		$(".side_right").append('<div class="back_top_qr back-qr-hover"  id="backToTopQR">' +
			'<div class="pop-backToTop-bd-qr">' +
				'<div class="pop-backToTop-bd-qr-img"></div>' +
				'<div class="pop-backToTop-bd-qr-word"><h1>关注有惊喜</h1>微信关注<br/>蘑菇公众号<br/>特惠活动抢先知<br/>礼物红包发不停</div>'+
			'</div>'+
		'</div>');
		$(".side_right").append('<div class="back_top_phone back-phone-hover"  id="backToTopPhone">' +
			'<div class="pop-backToTop-bd">' +
			'<h1>客服电话</h1>' +
			'<div><p>4000-437-837</p>周一到周五9:30-19:00</div>'+
			'<div class="pop-margin"><p>13811467515,13811467115</p>非工作时间紧急电话</div>'+
			'</div>'+
			'</div>');
		$(".side_right").append('<div class="back_top_QQ back-top-hover"  id="backToTopQQ" onclick=window.open("http://crm2.qq.com/page/portalpage/wpa.php?uin=4000437837&f=1&ty=1")>' +
			'<div class="pop-backToTop-bd-top">' +
			'<h1>点击在线咨询</h1>' +
			'</div>' +
			'</div>');
		$(".side_right").append('<div class="back_top back-top-hover"  id="backToTop" onclick="javascript:window.scroll(0, 0);">' +
			'<div class="pop-backToTop-bd-top">' +
			'<h1>点击返回顶部</h1>' +
			'</div>' +
			'</div>');
		$("body").append('</div>');
		//返回顶部
		backToTopButtonShowAndhidden();
		//返回顶部页面位置控制
		backToTopShowContorll();
};
/**
 * edit by wangjie on 2016/9/8.
 * 返回顶部页面位置控制
 */
function backToTopShowContorll(){
	var win = $(window);
	var win_height = win.height();
	var body_height = $("body").height();
	var side_right_obj = $(".side_right");

	var backToTopQQ = $("#backToTopQQ");
	var backToTopPhone = $("#backToTopPhone");
	var backToTop = $("#backToTop")
	var backToTopQR = $("#backToTopQR");

	backToTopQR.css({bottom : 150});
	backToTopPhone.css({bottom : 110});
	backToTopQQ.css({bottom : 70});
	backToTop.css({bottom : 30});

	if(body_height < win_height){
		side_right_obj.css({bottom : 280});
	}

	win.resize(function(){side_right_obj.css({right : (win.width() - 990)/2 - 30});});
	side_right_obj.css({right : (win.width() - 990)/2 - 30});

};

/**
 * edit by wangjie on 2016/9/8.
 * 返回顶部
 */
function backToTopButtonShowAndhidden(){
	var mousewheelevt=(/Firefox/i.test(navigator.userAgent))? "DOMMouseScroll" : "mousewheel"; //FF doesn't recognize mousewheel as of FF3.x

	if (document.attachEvent) //if IE (and Opera depending on user setting)
		document.attachEvent("on"+mousewheelevt, function(e){
			var t=document.body.scrollTop+document.documentElement.scrollTop;
			if(t<300){
				$(".back_top").css("display","none"); ;
			}
			else{
				$(".back_top").css("display","block");
			}
		});
	else if (document.addEventListener) //WC3 browsers
		document.addEventListener(mousewheelevt, function(e){
			var t=document.body.scrollTop+document.documentElement.scrollTop;
			if(t<300){
				$(".back_top").css("display","none");
			}
			else{
				$(".back_top").css("display","block");
			}
		}, false);

}

function backToTopShowFooterContorll(){
	var backToTopQR = $("#backToTopQR");
	var backToTopQQ = $("#backToTopQQ");
	var backToTopPhone = $("#backToTopPhone");
	var backToTop = $("#backToTop");
	var qrpimg= $(".pop-backToTop-bd-qr-img");

	var side_right_obj = $(".side_right");
	var scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();
	var g = $("#footer");
	var e = g.outerHeight();
	if(scrollBottom<=208){
		side_right_obj.css({position : "absolute"});
		backToTopQR.css({position : "absolute"});
		backToTop.css({position : "absolute"});
		backToTopPhone.css({position : "absolute"});
		backToTopQQ.css({position : "absolute"});
		qrpimg.css({position : "absolute"});
		backToTopQR.css({bottom : e+150});
		backToTopPhone.css({bottom : e+110});
		backToTopQQ.css({bottom : e+70});
		backToTop.css({bottom : e+30});

	}else{
		side_right_obj.css({position : "fixed"});
		backToTopQR.css({position : "fixed"});
		backToTop.css({position : "fixed"});
		backToTopPhone.css({position : "fixed"});
		backToTopQQ.css({position : "fixed"});
		qrpimg.css({position : "fixed"});
		backToTopQR.css({bottom : 150});
		backToTopPhone.css({bottom : 110});
		backToTopQQ.css({bottom : 70});
		backToTop.css({bottom : 30});
	}
}
$(window).scroll(function () {
		backToTopShowFooterContorll();
});
var processArray = [0,1000,1000,1000,1000,1000,2000,2000,2000,2000,2000,4000,4000,4000,4000,4000,4000,4000,4000,4000,8000];
var endTimes = 15;

function noteUncheckHotels(checkHotels){
	$.ajax({
		url:'uncheckHotel',
		data:{'info':JSON.stringify(checkHotels)},
		type:'post'
	});
}


function jsTrackLog(logInfo,logName){
	$.ajax({
		url:'saveJsLog',
		data:{'log':logInfo,'logName':logName},
		type:'post'
	});
}