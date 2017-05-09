/**
 * Created by 李秀广 on 2017/5/9.
 */

function showOrderQueryResultGroups(data){
    var resultHtml = "";

    for(var index in data){
        var groupCode = data[index].companyGroupCode.toUpperCase();
        var groupSize = data[index].hotelOrderListRSDtoList.length;
        resultHtml += '<tr style="border: 0px;border-bottom: 1px solid #DCDCDC;" class="result-group-td" ><td colspan="9" class="result-gro-span"><span ><i class="check-box-icon-group selectOrderGroup" group-id="'+groupCode+'"></i><span class="fl bold">团号:</span><span class="bold fl group-td-code">'+groupCode ;
        resultHtml+='</span>总订单：<span class="spanNum ">'+groupSize+'</span>';
        resultHtml +='已确认：<i class="group-Amount-confirmNum c-red " group-id="'+groupCode+'"></i>金额：<i class="group-Amount-confirm c-red " group-id="'+groupCode+'"></i><span class="">已取消：</span><i class="group-Amount-cancelNum c-red " group-id="'+groupCode+'"></i>取消费：<i class="group-Amount-cancel c-red " group-id="'+groupCode+'"></i></span></td><td class="wid90 ta-c pd8 show-detail" ><div class="hiddenorshow" onclick="hiddenorshow(this)"><u  group-id="';
        resultHtml +=groupCode +'" class="hidden-show">收起</u><i class="icon-item-group icon-item-group-close " ></i></div></td></tr>';
        resultHtml += showOrderQueryResult(data[index].hotelOrderListRSDtoList);
    }

    return resultHtml;
}



function showOrderQueryResult(data){
    changresultShowType(true);
    var resultHtml = "";
    for(var index in data) {
        var orderData = data[index];

        var totalPeople = Number(orderData.adultQuantity) + Number(orderData.childrenQuantity);
        var isSendVoucher = isOrderSendVoucher(orderData);
        var isSendBooking = isOrderSendBooking(orderData);
        var orderStus = getOrderStatus(orderData.orderStatusCN);
        var optionTimeStr = getOptionTimeStr(orderData.xOptionDateStr);
        var reference = orderData.referenceCode;
        var OrderFeeStr = getOrderFeeStr(orderData.orderStatus,orderData.salePrice,orderData.customerOperationFee,orderData.companyCurrency);
        if(reference == undefined || reference == null){
            reference = '';
        }
        var countryName = orderData.countryName;
        if(countryName == undefined || countryName == null){
            countryName = '';
        }

        var companyGroupId = orderData.companyGroupCode;
        if(companyGroupId == undefined || companyGroupId == null){
            companyGroupId = '';
        }else{
            companyGroupId = companyGroupId.toUpperCase();
        }



        resultHtml += '<tr class="so-result-item '+getOrderStatusClass(orderData.orderStatus)+ '" '+OrderFeeStr+' order-id="'+orderData.orderIdStr+'" group-id="'+companyGroupId +'">';

        resultHtml += '<td class="wid50 ta-c select-item soResults1"><i class="so-check-box-icon ';
        if(isSendBooking == true || isSendBooking == 'true'){
            resultHtml += ' send-booking-order ';
        }

        if(isSendVoucher == true || isSendVoucher == 'true'){
            resultHtml += ' send-voucher-order ';
        }
        var productName = orderData.productName;
        var hotelname = null;
        if(!productName){
            productName = '';
        }else{
            var hotelnames = productName.split("/");
            if(hotelnames.length==1 || hotelnames[0] == hotelnames[1]){
                hotelname = hotelnames[0];
            }else{
                hotelname = productName;
            }
        }
        var payFlag = orderData.payFlag;
        var paymentChannel = orderData.paymentChannel;
        var transactionStatus = orderData.transactionStatus;

        resultHtml += 'currenyAmuount " currenyAmuount="'+orderData.companyCurrency+':'+digitForIgnore(orderData.salePrice,1)+'" select-id="'+orderData.orderIdStr+'" ></i></td>'
            +'<td class="wid130 ta-c pd10 show-detail soResults2"><span> ' + orderData.orderIdStr + '</span><br/>' + companyGroupId +'<br/>' + reference + '</td>'
            +'<td class="wid95  ta-c pd10 show-detail soResults3" >' + countryName + '<br/>' + orderData.cityName + '</td>'
            +'<td class="wid160  ta-c pd10 show-detail soResults4" >' + hotelname + '</td>'
            +'<td class="wid95 ta-c pd10 show-detail soResults5">'+orderData.checkInStr+'<br/>'+orderData.checkOutStr+'<br/>'+orderData.roomNum+'间/'+orderData.duration+'晚</td>'
            +'<td class="wid120 ta-c pd10 show-detail soResults6">'+orderData.customerName+'<br/>'+totalPeople+'人</td>'
            +'<td class="wid85 ta-c pd10 show-detail">'+optionTimeStr+'</a></td>'
            +'<td class="wid110 ta-c pd16 bold show-detail ">'+orderData.companyCurrency+' '+digitForIgnore(orderData.salePrice,1)+'</td>'
            +'<td class="wid100 ta-c pd10 bold show-detail">';
        resultHtml +=orderStus+'<br/>'+orderData.customerPayStatusCN;
            console.log("payFlag:"+payFlag);
            if(payFlag == 99){
                //隐藏订单list页面支付状态抬头
                //$("#listPaymentStatus").addClass("none-display");
            }else{
                if (paymentChannel) {
                    if(payFlag == 1){
                        resultHtml += '<br/>待支付';
                    }else if(payFlag == 0){
                        if (transactionStatus == 6) {
                            resultHtml += '<br/>已关闭';
                        }else {
                            resultHtml += '<br/>已支付';
                        }
                    }else{
                        resultHtml += '<br/>待退款';
                    }
                }
            }


        resultHtml += '</td>';

        resultHtml +='<td class="wid90 ta-c pd16 show-detail">'+ orderData.userName+'</td>'
            +'<td class="wid92 ta-c pd8 show-detail" id="skipToPayment">'
            +'<span onclick="orderDetail(\''+orderData.orderIdStr+'\')"><u>查看详情</u></span>';

            if(payFlag == 1){
                resultHtml += '<br/><a class="paymentOrder payment-button" target="_blank" href="./onlinePaymentAfterBook?orderId=' + orderData.orderIdStr  + '&flag=second" orderid="'+orderData.orderIdStr+ '">立即支付</a>';
            }
        resultHtml += '</td>'
            +'</tr>';
    }
    return resultHtml;
}

function changeMilTime2TimeDateShow(miliTime){
    var time =  new Date(miliTime);
    timeMonth = time.getMonth() + 1;
    return time.getFullYear() +"/"+ timeMonth +"/"+ time.getDate()+" "
        + time.getHours() +":"+ time.getMinutes();
}

function changeMilTime2TimeShow(miliTime){
    var time =  new Date(miliTime);
    timeMonth = time.getMonth() + 1;
    return time.getFullYear() +"/"+ timeMonth +"/"+ time.getDate();
}

//清空选择框
function clearOrderCondition(){
    var orderStatus = $("#order_status").val();

    $(".condition-input-area .select-condition-item").val("");
    //回复其他选择框至默认全部
    $(".default-zero-select-condition-item").each(function(){
        $(this).find("input").val(0);
        $(this).find("span[class=\"dispaly-value\"]").html("全部");
    });
    $(".default--negative-select-condition-item").each(function(){
        $(this).find("input").val(-1);
        $(this).find("span[class=\"dispaly-value\"]").html("全部");
    });

    if($("#order-stauts-all").hasClass("none-display")){//当不是选择所有订单时 重置为不可显示的选择框
        setorderStatus(orderStatus);
    }else{
        setorderStatus(0);//
    }
}
function setorderStatus(orderStatus){
    $("#order_status").val(orderStatus);
    if(orderStatus ==0){//设置显示筛选框
        $("#order-stauts-all .dispaly-value").html("全部");
        $("#order-stauts-all").removeClass("none-display");
        $(".disabledAreaBox").addClass("none-display");
    }else{//设置显示不可筛选的框
        var statusMsg = "li[nation-id='"+orderStatus+"']";
        $("#order-stauts-all").addClass("none-display");
        $(".disabledAreaBox").removeClass("none-display");
        $(".disabledAreaBox").text($(statusMsg).text());
    }
}
//重置所有状态到初始状态
function clearAllOrderCondition(){
    $("#rowsNum").addClass("none-display");
    $(".condition-input-area .select-condition-item").val("");
    $(".quick-item-sec").removeClass("select");
    $("#searchOrderType").val("");
    //回复其他选择框至默认全部
    $(".default-zero-select-condition-item").each(function(){
        $(this).find("input").val(0);
        $(this).find("span[class=\"dispaly-value\"]").html("全部");
    });
    $(".default--negative-select-condition-item").each(function(){
        $(this).find("input").val(-1);
        $(this).find("span[class=\"dispaly-value\"]").html("全部");
    });
    $("#queryFromSample").removeClass("none-display");
    $("#queryForm").addClass("none-display");
    $(".search-btn-area").addClass("none-display");
    $("#selectAllOrders").removeClass("filter-checked-icon");
    resetTabs();
    hideCheckInfo();
}

function hiddenOrderCondition(){
    $(".condition-input-area .select-condition-item").val("");
    $("#searchOrderType").val("");
    //回复其他选择框至默认全部
    $(".default-zero-select-condition-item").each(function(){
        $(this).find("input").val(0);
        $(this).find("span[class=\"dispaly-value\"]").html("全部");
    });
    $(".default--negative-select-condition-item").each(function(){
        $(this).find("input").val(-1);
        $(this).find("span[class=\"dispaly-value\"]").html("全部");
    });

    $("#selectAllOrders").removeClass("filter-checked-icon");
    hideCheckInfo();
}

function queryOrderSumbit(){
    clearAllSelect();
    var queryForm = $("#queryForm");
    $.ajax({
        type: "POST",
        url: "./queryList",
        data: queryForm.serialize(),
        dataType: "json",
        timeout:30000,
        success: function (data) {
            if (data == undefined || data == null || data.total == undefined ||
                data == null || data.total == 0){
                $("#searchResultDiv").addClass("none-display");
                $("#noResultShow").removeClass("none-display");
                $("#rowsNum").addClass("none-display");
                $("#soResults tr:gt(0)").remove();
            }else {
                var resultHtml = showOrderQueryResult(data.rows);
                $("#soResults tr:gt(0)").remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                $("#soResults").append(resultHtml);
                checkResultEvent();
                $("#searchHotelNum").html(data.total);
                $("#searchHotelNumUnit").html("订单");
                paginationOrder(data.total);
                $("#searchResultDiv").removeClass("none-display");
                if(data.total>20){
                    $("#rowsNum").removeClass("none-display");
                }else{
                    $("#rowsNum").addClass("none-display");
                }

                $("#noResultShow").addClass("none-display");
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    })
}

function queryOrderGroup(){
    clearAllSelect();
    $.ajax({
        type: "POST",
        url: "./queryListForGroupId",
        dataType: "json",
        timeout:30000,
        success: function (data) {
            if (data == undefined || data == null || data.total == undefined ||
                data == null || data.total == 0){
                $("#searchResultDiv").addClass("none-display");
                $("#noResultShow").removeClass("none-display");
                $("#soResults tr:gt(0)").remove();
            }else {
                var resultHtml = showOrderQueryResultGroups(data.rows);

                $("#soResults tr:gt(0)").remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                $("#soResults").append(resultHtml);
                $(".hiddenorshow").click();
                checkResultEvent();
                showTotalAmountGroup();
                $("#searchHotelNum").html(data.total);
                $("#searchHotelNumUnit").html("团");
                paginationGroup(data.total);
                $("#searchResultDiv").removeClass("none-display");
                $("#noResultShow").addClass("none-display");
                changresultShowType(false);
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    })
}


function initSearchOrder(){
    //提交按钮的点击事件
    $("#submitQueryCondition").bind("click", function () {
        _hmt.push(['_trackEvent','orderlist','sousuo','-','-']);
        $("#searchResultTitle").html("搜索到");
        $(".quick-item-sec").removeClass("select");
        $("#searchOrderType").val("");
        $("#search-formal").removeClass("search-type-checked");
        $("#search-formal").click();
    });
    //时间控件的初始化
    var myDate1 = new Calender({startId: 'checkInTime', endId: 'checkOutTime', isSelect: !0});
    var myDate2 = new Calender({startId: 'bookStartTime', endId: 'bookEndTime', isSelect: !0});
    var myDate3 = new Calender({startId: 'cancelStartTime', endId: 'cancelEndTime', isSelect: !0});
    //收起搜索框
    $(".search-btn-area #hidden-all-condition").bind("click",function(){
        var orderId = $("#orderIdFull").val();

        $("#queryFromSample").removeClass("none-display");
        $("#queryForm").addClass("none-display");
        $(".search-btn-area").addClass("none-display");
        clearOrderCondition();
        $("#orderIdSample").val(orderId);
    });
    $(".search-btn-area #clear-all-condition").bind("click",function() {
        clearOrderCondition();
    });
    ////输入框回车搜索
    //$("#queryForm input").keyup(function(){
    //    queryOrderSumbit();
    //});
    //查询条件的所有快捷操作的绑定
    //1.1预定最近7天的点击事件
    $("#bookBeforeWeek").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','option in 7days','filter conditions','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        var nowDate = new Date();
        var bookBeforeWeekDate = new Date(nowDate.getTime() - 7*24*60*60*1000);
        var nowMonth = nowDate.getMonth() + 1;
        var bookBeforeWeekDateMonth = bookBeforeWeekDate.getMonth() + 1;
        $("#bookStartTime").val(bookBeforeWeekDate.getFullYear() + "-"+timeCover0(bookBeforeWeekDateMonth)+"-"+timeCover0(bookBeforeWeekDate.getDate()));
        $("#bookEndTime").val(nowDate.getFullYear() + "-"+timeCover0(nowMonth)+"-"+timeCover0(nowDate.getDate()));
        $("#searchResultTitle").html("搜索到");
        queryOrderSumbit();
    });
    //1.2预定最近一个月的点击事件
    $("#bookBeforeMonth").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','30tianneiyuding','-','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        var nowDate = new Date();
        var bookBeforeWeekDate = new Date(nowDate.getTime() - 30*24*60*60*1000);
        var nowMonth = nowDate.getMonth() + 1;
        var bookBeforeWeekDateMonth = bookBeforeWeekDate.getMonth() + 1;
        $("#bookStartTime").val(bookBeforeWeekDate.getFullYear() + "-"+timeCover0(bookBeforeWeekDateMonth)+"-"+timeCover0(bookBeforeWeekDate.getDate()));
        $("#bookEndTime").val(nowDate.getFullYear() + "-"+timeCover0(nowMonth)+"-"+timeCover0(nowDate.getDate()));
        $("#searchResultTitle").html("搜索到");
        queryOrderSumbit();
    });

    //1.3入住日未来7天的点击事件
    $("#checkInAfterWeek").bind("click",function(){
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        //var nowDate = new Date();
        //var checkInAfterWeekDate = new Date(nowDate.getTime() + 7*24*60*60*1000);
        //var nowMonth = nowDate.getMonth() + 1;
        //var checkInAfterWeekMonth = checkInAfterWeekDate.getMonth() + 1;
        //$("#checkInTime").val(nowDate.getFullYear() + "-"+timeCover0(nowMonth)+"-"+timeCover0(nowDate.getDate()));
        //$("#checkOutTime").val(checkInAfterWeekDate.getFullYear() + "-"+timeCover0(checkInAfterWeekMonth)+"-"+timeCover0(checkInAfterWeekDate.getDate()));
        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('NC');
    });

    //1.4截止日未来7天的点击事件
    $("#optionAfterWeek").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','7tianneiquxiaojiezhi','-','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('FCO7');
    });
    //1.5无入住点击事件
    $("#checkInFuture").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','no show','filter conditions','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));

        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('NC');
    });

    //1.6 异常订单
    $("#excptOrder").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','exception','filter conditions','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));

        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('EO');
    });

    //1.7 可免费取消订单
    $("#freeCancelOrder").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','free cancel','filter conditions','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));

        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('FCO');
    });

    //1.8 不可免费取消订单
    $("#feeCancelOrder").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','no free cancel','filter conditions','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));


        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('NFCO');
    });

    //1.9 最近七天内修改完成订单
    $("#hasEditOrderInWeek").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','7 days amended','-','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('AR7');
    });
    //1.10 7天取消完成订单
    $("#hasCanceledOrderInWeek").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','7tianneiquxiao','-','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));

        queryQuickOrderSumbit('CCO7');
    });
    //1.11 修改待确认
    $("#editWaiting").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','xiugaidaiquren','-','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('AR');
    });
    $("#CancelWithFee").bind("click",function(){
        _hmt.push(['_trackEvent','orderlist','canceled with charges','-','-']);
        clearAllOrderCondition();
        setHighLightBtnStyle($(this));
        $("#searchResultTitle").html("搜索到");
        $("#searchOrderType").val($(this).attr("ID"));
        queryQuickOrderSumbit('CWF');
    });


    //批量发送入住邮件
    $("#benchSendHotelOrders").bind("click",function(){
        var selectOrderIds = getSelectBookingOrderIds();
        showSendEmailDiv('HOTEL',selectOrderIds);

    });
    //批量发送确认邮件
    $("#benchSendVoucherOrders").bind("click",function(){
        var selectOrderIds = getSelectVoucherOrderIds();
        showSendEmailDiv('VENCHOR',selectOrderIds);
    });

    //批量发送入住邮件
    $("#benchDownloadHotelOrders").bind("click",function(){
        var selectOrderIds = getSelectBookingOrderIds();
        benchDownloadPdf('HOTEL',selectOrderIds);

    });

    //批量发送入住邮件
    $("#benchDownloadVoucherOrders").bind("click",function(){
        var selectOrderIds = getSelectVoucherOrderIds();
        benchDownloadPdf('VENCHOR',selectOrderIds);
    });
    $("#sortbycheckin").bind("click",function(){
        changsort("orders.check_in")
    });
    $("#sortbyid").bind("click",function(){
        changsort("orders.order_id")
    });
    $("#sortbyoption").bind("click",function(){
        changsort("orders.x_option_date")
    });

    //切换tab
    $(".search-order-status-tabs").parent().bind("click", function () {
        var $thistabs = $(this).children();
        $(".search-order-status-ch").removeClass("search-order-status-ch").addClass("search-order-status-un");
        $thistabs.parent().removeClass("search-order-status-un").addClass("search-order-status-ch");
        $(".search-order-status-tabs").addClass("search-order-status-border");
        $thistabs.removeClass("search-order-status-border");
        $thistabs.parent().next().children().removeClass("search-order-status-border");
        setorderStatus($thistabs.attr("status-type"));
        $("#submitQueryCondition").click();
    });


    //更多查询条件
    $("#more-condition").bind("click",function(){
        $("#orderIdFull").val($("#orderIdSample").val());
        $("#queryFromSample").addClass("none-display");
        $("#queryForm").removeClass("none-display");
        $(".search-btn-area").removeClass("none-display");
    });
    //单号查询模式查询订单
    $("#submitQueryConditionSimple").bind("click",function(){
        $("#orderIdFull").val($("#orderIdSample").val());
        $("#submitQueryCondition").click();

    });
    //切换两种排序方式 当前排序方式已被选中时切换无效
    $(".search-type-sort").bind("click",function(){
        if($(this).hasClass("search-type-checked")){
            return;
        }else{
            var searchType = $(this).attr("id");
            setCookie("detail-show-type",searchType,24 * 365,"/");
            if(searchType == "search-group"){
                clearAllOrderCondition();
                $("#search-group").addClass("search-type-checked");
                $("#search-formal").removeClass("search-type-checked");
                queryOrderGroup();

            } else{
                $("#search-formal").addClass("search-type-checked");
                $("#search-group").removeClass("search-type-checked");
                queryOrderSumbit();
            }
        }
    });


}
//重置选中的tab为所有订单，不重新搜索
function resetTabs(){
    var $thistabs = $(".search-tab-first");
    $(".search-order-status-ch").removeClass("search-order-status-ch").addClass("search-order-status-un");
    $thistabs.removeClass("search-order-status-un").addClass("search-order-status-ch");
    $(".search-order-status-tabs").addClass("search-order-status-border");
    $thistabs.next().children().removeClass("search-order-status-border");
    $("#order_status").val("0");
    $("#order-stauts-all").removeClass("none-display");
    $(".disabledAreaBox").addClass("none-display");
}

//切换每页显示的订单数
function changeCows(){
    $(".pageSize").val($('#rowsNumSelect option:selected').val());
    initSearchData();
}

//切换标准排序和团号排序两种模式的显示
function changresultShowType(obj){
    if(obj==true){
        $(".soResults1").removeClass("none-display");
        $(".soResults2").addClass("wid130");
        $(".soResults3").addClass("wid95");
        $(".soResults4").addClass("wid160");
        $(".soResults5").addClass("wid95");
        $(".soResults6").addClass("wid120");
    }else{
        $(".soResults1").addClass("none-display");
        $(".soResults2").addClass("wid140");
        $(".soResults3").addClass("wid105");
        $(".soResults4").addClass("wid170");
        $(".soResults5").addClass("wid105");
        $(".soResults6").addClass("wid130");

    }
}


//计算选中的订单的总金额
function showTotalAmount(data) {
    var selectOrderAmounts = [];

    data.each(function () {
        selectOrderAmounts.push($(this).attr("currenyAmuount"))
    });

    var items = {}, key;
    for (var i = 0; i < selectOrderAmounts.length; i++) {
        var orderAmount = selectOrderAmounts[i].split(":");
        key = orderAmount[0];
        if(key=="null"){
            continue;
        }
        //check是否存在该货币
        if (!items[key]) {
            items[key] = ['', 0];
        }
        //存入货币
        items[key][0] = orderAmount[0];
        //计算同种货币的订单数量
        var salePrice = orderAmount[1];
        items[key][1] += parseFloat(salePrice);
    }
    var title = "";
    for (key in items) {
        var outputArr = items[key];
        var currency = outputArr[0];
        title += currency + digitForIgnore(outputArr[1],1)+" ";
    }
    return title;
}


//计算每个团的总金额
function showTotalAmountGroup(){

    var currency=$(".currency").html();
    $(".group-Amount-confirm").each(function(){
        var groupCode = $(this).attr("group-id");
        var total = '';
        total  = showTotalAmount($("tr.confirmd[group-id='"+groupCode+"']"));
        if(total==''){
            $(this).html(currency+"0.0");
        }else {
            $(this).html(total);
        }


    });
    $(".group-Amount-cancel").each(function(){
        var groupCode = $(this).attr("group-id");
        var total = '';
        total  = showTotalAmount($("tr.canceled[group-id='"+groupCode+"']"));
        if(total==''){
            $(this).html(currency+"0.0");
        }else {
            $(this).html(total);
        }
    });
    $(".group-Amount-confirmNum").each(function(){
        var groupCode = $(this).attr("group-id");
        var total = 0;
        total =$("tr.confirmd[group-id='"+groupCode+"']").size();
        $(this).html(total);
    });
    $(".group-Amount-cancelNum").each(function(){
        var groupCode = $(this).attr("group-id");
        var total = 0;
        total=  $("tr.canceled[group-id='"+groupCode+"']").size();
        $(this).html(total);
    });


}

function hiddenorshow(obj){
    var $that = $(obj).children(".hidden-show");
    var  groupid = $that.attr("group-id");
    if($that.html()=="收起"){
        $that.next().removeClass("icon-item-group-close");
        $that.html("展开");
        $("tr[group-id='"+ groupid +"']").addClass("none-display");

    }else{
        $that.next().addClass("icon-item-group-close");
        $that.html("收起");
        $("tr[group-id='"+ groupid +"']").removeClass("none-display");
        _hmt.push(['_trackEvent', 'orderList', 'expand order list', 'team organization']);

    }

}
function changsort(sortname){

    var searchType = getCookieValue("detail-show-type");
    if(searchType == "search-group"){
        return;
    }
    if(sortname==$("#sortname").val()){
        if($("#sortflag").val()=="DESC"){
            $("#sortflag").val("ASC");
        }else{
            $("#sortflag").val("DESC");
        }
    }else{
        $("#sortname").val(sortname);
        $("#sortflag").val("DESC");
    }
    initSearchData();
}
function checkbox(){
    $(".special-requirement-list i.check-box-icon").bind("click",function(){
        var checkbox = $(this);
        if(checkbox.hasClass("filter-checked-icon")){
            checkbox.removeClass("filter-checked-icon");
        }else{
            checkbox.addClass("filter-checked-icon");
        }

        if(checkbox.hasClass("time-special")){
            if (checkbox.hasClass("filter-checked-icon")) {
                $(this).parent().siblings(".select-time-show").removeClass("none-display");
            } else {
                $(this).parent().siblings(".select-time-show").addClass("none-display");
                $(this).parent().siblings(".select-time-show span[calss=\"dispaly-value\"]").html("请选择时间");
                $(this).parent().siblings(".select-time-show input").val("");
            }
        }
    });

}

var isLimSaName = 'false';
/**
 * 加载该报价的房间信息
 */
function loadRoomDetailInfo(orderId){
    $.ajax({
        type:'POST',
        data:{"orderId":orderId},
        url:'./getRoomDetailInfo',
        dataType:'json',
        timeout:30000,
        success:function(data){
            showDebugInfo(data);
            //优先加载名字是否要判断重复的事件
            isLimSaName = data.data.limitSameName;
            if(isLimSaName === 'true'){
                bindSameNameJudgeEvet();
            }
            var roomDetailInfo = data.data.roomInfo;
            //酒店的图片
            var hotelImage = $("#boHotelImg");
            var imageId = hotelImage.attr("imgId");
            hotelImage.attr("src",getHotelUrl(roomDetailInfo.hotelId,imageId,"!small"));

            //取消政策
            var checkInStr = roomDetailInfo.checkIn;
            var checkInDate = convert2DayDate(checkInStr);
            var cancelHtml = getRefundableDetailInfo(roomDetailInfo.hotelRooms.policies,checkInDate);
            $("#cancelPolicyDescripArea").html(cancelHtml);

            var editPolicyHtml = getEditDetailInfo(roomDetailInfo.hotelRooms.policies);
            if (editPolicyHtml == null || editPolicyHtml == ''){
                //修改政策
                $("#editPoliciesDiv").addClass("none-display");
            }else{
                //修改政策
                $("#editPolicyDescripArea").html(editPolicyHtml);
            }
            var renamePolicyHtml = getRenameDetailInfo(roomDetailInfo.hotelRooms.policies);
            if (renamePolicyHtml == null || renamePolicyHtml == ''){
                //改名政策
                $("#renamePoliciesDiv").addClass("none-display");
            }else{
                //改名政策
                $("#renamePolicyDescripArea").html(renamePolicyHtml);
            }

            //图片
            var imageUrl = getHotelUrl(roomDetailInfo.hotelId,$(".hotel-pic").attr("imgId"),"!small");
            $(".hotel-pic").attr("src",imageUrl);
            //餐食说明（早餐描述，如果早餐描述为空，填写餐食描述）
            var mealHtml = "";
            if(roomDetailInfo.hotelRooms.meal.breakfastDescription != null && roomDetailInfo.hotelRooms.meal.breakfastDescription != ''){
                mealHtml += roomDetailInfo.hotelRooms.meal.breakfastDescription;
            }else{
                mealHtml += roomDetailInfo.hotelRooms.meal.mealDescription;
            }
            $("#dinnerDescripArea").html(mealHtml);
            if (mealHtml == ''){
                $("#orderDinnerArea").addClass("none-display");
            }

            //重要说明(每个房间的重要说明 格式：房间：重要说明)
            var importantMessage = data.data.importantMessages+data.data.extraBed;
            if (importantMessage == ''){
                $("#orderImportantArea").addClass("none-display");
            }else{
                $("#importantInfoArea").html(importantMessage);
            };
            $("#orderMessage").val(data.data.extraBed);

            //注意事项,主要是其他网络平台的禁售列表
            var considerations = data.data.reminder;
            if (considerations == 'false'){
                $("#orderConsiderationsArea").addClass("none-display");
            }else{
                $("#considerationsArea").html(data.data.reminderContent);
            };

            //价格浮窗
            if(roomDetailInfo.hotelRooms && roomDetailInfo.hotelRooms.rooms && roomDetailInfo.hotelRooms.rooms[0].priceBreakdowns && roomDetailInfo.hotelRooms.rooms[0].priceBreakdowns.length > 0){
                var priceDetail = '<i class="book-price-detail pos-bd pop-price-hover">';
                priceDetail += priceDetaiInfo(roomDetailInfo.hotelRooms.rooms,roomDetailInfo.totalRate,roomDetailInfo.totalOriginalRate);
                priceDetail += '</i>';
                $("#orderTotalRate").append(priceDetail);
            }


            //判断该订单是否是可免费取消和是否包含不确定房型
            var cancelPolicy = roomDetailInfo.hotelRooms.policies;
            if(cancelPolicy != undefined && cancelPolicy != null && cancelPolicy.refundable == 'true'){
                $("#isRefundableRoom").val('true');
            }else{
                $("#isRefundableRoom").val('false');
            }

            var isStandardRoom = 'false';
            for(var roomIndex in roomDetailInfo.hotelRooms.rooms){
                var room = roomDetailInfo.hotelRooms.rooms[roomIndex];
                if (room.roomType == "DOUBLE OR TWIN" || room.roomType == "STANDARD"){
                    isStandardRoom = 'true';
                    break;
                }
            }
            $("#isStandardRoom").val(isStandardRoom);

            if(cancelPolicy != undefined && cancelPolicy != null && cancelPolicy.refundable == 'true'){
                $("#isRefundableRoom").val('true');
            }else{
                $("#isRefundableRoom").val('false');
            }

            var isCheckInWarning = data.data.isCheckInWarning;

            if(isCheckInWarning === 'true'){
                $("#isCheckInWarning").removeClass("none-display");
            }

            $("#isCompatRoomTipShow").val(data.data.isCompatTipShow);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    })
}

function editOrder(orderId,tStatus,pStatus){
    if((tStatus != 2 && tStatus != 5 ) && (pStatus== 1||pStatus==2||pStatus==3||pStatus==5)){
        $("#orderMask").removeClass("none-display");
        initModifyOrder(orderId);
        $("#orderModifyTip").removeClass("none-display");

    }else{
        $("#orderMask").removeClass("none-display");
        $("#unableEditDiv").removeClass("none-display");
    }

}
function initModifyOrder(orderId){
    var roomTypeEnum =null;
    $(".close-modfiy-disagreeTip").bind("click",function(){
        $("#orderModifyTip").removeClass("zindex9999");
        $("#orderModfiyAgreeTip").addClass("none-display");
    });
    $(".close-inputUnchangdTip").bind("click",function(){
        $("#orderModifyTip").removeClass("zindex9999");
        $("#inputUnchangdTip").addClass("none-display");
    });
    $(".tip-ModifySuccess-btn").bind("click",function(){
        location.reload();
    });
    $(".tip-ModifyFile-area").bind("click",function(){
        $("#orderModifyTip").removeClass("zindex9999");
        $(this).parent().addClass("none-display");
    });

    //根据标志位判断该tip是否显示
   $(".amendableFlag").each(function(){

        if($(this).html()=='false'){
            var str = $(this).attr("id");
            $("."+str).addClass("none-display");
        }
    });

    var divWeight = 748;
    $(".modify-tabs").each(function(){
        if(!$(this).parent().hasClass("none-display")){
            var num = $(this).parent().attr("wid");
            divWeight -= num;
        }
    });
    $(".modify-tab-tit:not(.none-display):first").children().attr("style","border: 0");
    $(".modify-tab-tit:not(.none-display):first").click();

    $("#modify-padding").attr("style","width:"+parseInt(divWeight)+"px");

    $.ajax({
        url:"./getRoomTypeEnum",
        dataType:"json",
        success:function(data){
            roomTypeEnum = eval('(' + data.data + ')');
        }
    });
    $.ajax({
        url:"./modifyTips?orderId="+orderId,
        dataType:"json",
        success:function(data){

            $(".modify-orderId").val(data.data.order.orderIdStr);
            $(".modify-submit").bind("click",function(){//提交按钮
                var isOk=true;
                var modifyType = "name";
                if($(this).attr("modify-type")=="name"){
                    modifyType = "name";
                    isOk=isOk && checkNameforModify();
                }else if($(this).attr("modify-type")=="groupCode"){
                    modifyType = "groupCode";
                    isOk=isOk && checkoperator();
                }else if($(this).attr("modify-type")=="checkinDate"){
                    modifyType = "checkinDate";
                    isOk=isOk &&checkcheckinDate();
                }else{
                    modifyType = "roomNum";
                    isOk=isOk &&checkroomNum();
                }

                if(!isOk){
                    return;
                }

                var agereddiv = $(this).closest(".submit-modify").find(".selectAgree");
                if(agereddiv.hasClass("filter-checked-icon")){
                    submitModify($(this).closest("form"),modifyType);

                }else{
                    $("#orderModifyTip").addClass("zindex9999");
                    $("#orderModfiyAgreeTip").removeClass("none-display");
                }

                });
            getCustomRoomNameInfo(data.data.hotelRoomInfo,roomTypeEnum);//加载修改客人名称页
            getCustomRoomInfo(data.data.hotelRoomInfo,roomTypeEnum);//加载更改房间数页
            getCustomDate(data.data);//加载更改入住日期
            getCustomGroupInfo(data.data);//加载联系人团号
            $(".delete-room-btn").bind("click",function(){
                if($(".delete-room-btn").size()<=1){
                    return;
                }else{
                    $(this).parent().parent().remove();
                }

            })
        },
        error:function(){
            alert("获取修改信息失败");
        }
    });
}

function getCustomGroupInfo(data){
    $("#change-operator").val(data.order.operator);
    $("#change-operator").attr("defaultVa",data.order.operator);
    $("#change-operator-mobile").val(data.order.operatorMobile);
    $("#change-operator-mobile").attr("defaultVa",data.order.operatorMobile);
    $("#change-operator-mail").val(data.order.operatorEmail);
    $("#change-operator-mail").attr("defaultVa",data.order.operatorEmail);
    $("#change-groupcode").val(data.order.companyGroupCode);
    $("#change-groupcode").attr("defaultVa",data.order.companyGroupCode);
}
function getCustomDate(data){
    var startDate = new Date( data.checkIn);
    var startMonth = startDate.getMonth() + 1;
    var startDateStr = startDate.getFullYear() + '-'+timeCover0(startMonth)+'-'+timeCover0(startDate.getDate());
    $("#checkInTimeModify").val(startDateStr);
    var nextDate = new Date(data.checkOut);
    var nextMonth = nextDate.getMonth() + 1;
    var nextDateStr = nextDate.getFullYear() + '-'+timeCover0(nextMonth)+'-'+timeCover0(nextDate.getDate());
    $("#checkOutTimeModify").val(nextDateStr);
    $("#minusDay").html(data.order.duration);
    $("#minusDaydefault").html(data.order.duration);
    var myDate1 = new Calender({startId:'checkInTimeModify',endId:'checkOutTimeModify',startValue:startDateStr,endValue:nextDateStr,isSelect:!0,minusDaysElementId:'minusDay'});

}
//申请修改，修改入住人名单
function getCustomRoomNameInfo(rooms,roomTypeEnums){
    var roomsHtml = '';
    var roomIndex = 1;
    for (var key in rooms){
        var room = rooms[key];
        var totalPeople = Number(room.adultsQuantity) + Number(room.childrenQuantity);
        //拼接客人的用关系数组保存(leader,adult,children)
        var roomTravelHtml = [];
        roomTravelHtml['adult'] ='';
        roomTravelHtml['children'] ='';
        var adultIndex = 1;
        var childIndex = 1;
        var travelIndex = 0;
        var checkfun = 'value=value.toUpperCase();value=value.replace(/[^\\sa-zA-Z]/g,\'\')';
        var checkInfo = 'onkeyup="'+checkfun+'" onpaste="'+checkfun+'" oncontextmenu="'+checkfun+'" ONBLUR="'+checkfun+'" placeholder="如：ZHANG SANFENG"';
        for(var travelKey in room.roomInfo){
            var travel = room.roomInfo[travelKey];
            if (travel.type == '0' || travel.type == 0){
                roomTravelHtml['adult'] += '<li>';
                roomTravelHtml['adult'] += '<input type="hidden"name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].title" value="'+travel.title+'">';
                roomTravelHtml['adult'] += '<input type="hidden"name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].type" value="'+travel.type+'">';
                roomTravelHtml['adult'] += '<input type="hidden"name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].age" value="'+travel.age+'">';
                roomTravelHtml['adult'] +='成人'+adultIndex+'<input class="input-cust-name" name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].fullName" value="'+travel.fullName+'"'+checkInfo+'defaultVa="'+travel.fullName+'"></li>';
                adultIndex ++;
            }else if (travel.type == '1' || travel.type == 1){
                roomTravelHtml['children'] += '<li>';
                roomTravelHtml['children'] += '<input type="hidden"name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].title" value="'+travel.title+'">';
                roomTravelHtml['children'] += '<input type="hidden"name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].type" value="'+travel.type+'">';
                roomTravelHtml['children'] += '<input type="hidden"name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].age" value="'+travel.age+'">';
                roomTravelHtml['children'] +='儿童'+childIndex+'<input class="input-cust-name" name="hotelRoomInfo['+(roomIndex-1)+'].roomInfo['+travelIndex+'].fullName" value="'+travel.fullName+'"'+checkInfo+'></li>';
                childIndex ++;
            }
            travelIndex ++;
        }

        //拼接房间信息
        roomsHtml += '<tr class="pos-bd pd20-0 clearfix modify-border-top "><td class="pd20-0 hotel-room-name"><span class="hotel-room-index fl">';
        roomsHtml += roomIndex+'</span><div class="fl hotel-room-name-s"><div>';
        if(roomTypeEnums.hasOwnProperty(room.roomType)){
            roomsHtml += roomTypeEnums[room.roomType];
            roomsHtml += "-";
        }
        roomsHtml+= room.roomName + '</div>';
        if(room.bedExtraStatus === 1 || room.bedExtraStatus ==='1'){
            roomsHtml +='<div class="c-green"><i class="add-bed-icon"></i>'+room.bedExtraDescription+'</div>';
        }
        roomsHtml +='</div></td>'
            + '<td class="modify-people-number">'+totalPeople+'人</td>'
            + '<td class="clearfix input-cust-name-td"><ul class="mg10-0">'
            +  roomTravelHtml['adult']+roomTravelHtml['children']
            +  '</ul><input type="hidden" name="hotelRoomInfo['+(roomIndex-1)+'].roomId" class="room-id" value="'+room.roomId+'">'
            +'</td></tr>';
        roomIndex++;

    }
    $(".change-cust-name").html(roomsHtml);

}
//修改申请删除房间,加载房间名
function getCustomRoomInfo(rooms,roomTypeEnums){//rooms= data.data.hotelRoomInfo
    var roomsHtml = '';
    var roomIndex = 1;
    for (var key in rooms){
        var room = rooms[key];
        var totalPeople = Number(room.adultsQuantity) + Number(room.childrenQuantity);
        //拼接客人的用关系数组保存(leader,adult,children)
        var roomTravelHtml = [];
        roomTravelHtml['adult'] ='';
        roomTravelHtml['children'] ='';
        var adultIndex = 1;
        var childIndex = 1;
        for(var travelKey in room.roomInfo){
            var travel = room.roomInfo[travelKey];
            if (travel.type == '0' || travel.type == 0){
                roomTravelHtml['adult'] += '<li>成人'+adultIndex+'：<span class="bold">'+travel.fullName+'</span></li>';
                adultIndex ++;
            }else if (travel.type == '1' || travel.type == 1){
                roomTravelHtml['children'] += '<li>儿童'+childIndex+'('+travel.age+'岁)'+'：<span class="bold">'+travel.fullName+'</span></li>';
                childIndex ++;
            }
        }

        //拼接房间信息
        roomsHtml += '<tr class="pos-bd clearfix customRoomNum';
        if(roomIndex != 1){
            roomsHtml +=' modify-border-top';
        }
        roomsHtml += '"><td class="hotel-room-name pd20-0"><span class="hotel-room-index fl">';
        roomsHtml += roomIndex+'</span><div class="hotel-room-name-s"><div>';
        if(roomTypeEnums.hasOwnProperty(room.roomType)){
            roomsHtml += roomTypeEnums[room.roomType];
            roomsHtml += "-";
        }
        roomsHtml+= room.roomName + '</div>';
        if(room.bedExtraStatus === 1 || room.bedExtraStatus ==='1'){
            roomsHtml +='<div class="c-green"><i class="add-bed-icon"></i>'+room.bedExtraDescription+'</div>';
        }
        roomsHtml +='</div></td>'
            + '<td class="modify-people-number">'+totalPeople+'人</td>'
            + '<td  class="room-custom-name"><ul>'
            +  roomTravelHtml['adult']+roomTravelHtml['children']
            +'</ul></td>';
        roomsHtml +='<td ><div class="delete-room-btn">删除房间 </div></td>'
            + '<input type="hidden" name="hotelRoomInfo['+(roomIndex-1)+'].roomId" value="'+room.roomId+'">'
            +'</tr>';
        roomIndex++;

    }
    roomsHtml +='<tr class="none-display"><td><div id="customRoomNum" >'+(roomIndex-1)+'</div></td><tr>';
    $(".room-List").html(roomsHtml);

}
function clearCancelOrderClick(){
    $("#cancelOrderTip .tip-orange-btn").unbind("click");
	isCancelFlag = false;
}
/***orderTip Function area***Start**/
var isCancelFlag = false;
function cancelOrder(orderId){
    if(isCancelFlag){
        return;
    }
    isCancelFlag = true;
    $("#orderMask").removeClass("none-display");
    $("#loadTipContent").html("系统正在处理您的取消请求，请稍候...");
    $("#loadingTip").removeClass("none-display");
    $.ajax({
        url:"./cancelTips",
        type:"post",
        data:{"orderId":orderId},
        dataType:"json",
        timeout:30000,
        success:function(data) {
            $("#loadingTip").addClass("none-display");
            if(data.resCode == '950') {
                $("#cancelOrderTipFail").removeClass("none-display");
            }else if(data.resCode == '520'){
                $("#contactUsTitle").html("取消失败");
                $("#contactUsDiv").removeClass("none-display");
            }else if(data.resCode == '510' || data.resCode == '800') {
                var tipOrderId = orderId;
                var policies = data.data.policyCommon;
                var policiesHtml = getCancelRefundableDetailInfo(policies);
                if(!policiesHtml){
                    policiesHtml = "取消订单将收取下单金额的100%作为取消费用";
                }
                $("#cancelContPolicies").html(policiesHtml);
                $("#cancelOrderTip").removeClass("none-display");
                $("#cancelOrderTip .tip-orange-btn").click(function () {
                    $("#cancelOrderTip").addClass("none-display");
                    $("#loadingTip").removeClass("none-display");
                    var queryId = $("#ordQueryId").val();
                    $.ajax({
                        url: "./cancelOrder",
                        type: "post",
                        data: {"orderId": tipOrderId,"queryId":queryId},
                        dataType: "json",
                        timeout: 30000,
                        success: function (data) {
                            $("#loadingTip").addClass("none-display");

                            // if (data.resCode == "950" || data.resCode == "210" || data.resCode =='500' || data.resCode =='900') {
                            //     $("#contactUsTitle").html("取消失败");
                            //     $("#contactUsDiv").removeClass("none-display");
                            // } else
                            // if(data.resCode =='200'){
                            if(data.resCode == '00000000'){
                                $("#tipSuccessTilte").html("取消成功");
                                $("#optionSuccess").removeClass("none-display");
                            }else{
                                $("#contactUsTitle").html("取消失败");
                                $("#contactUsDiv").removeClass("none-display");
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            if (textStatus == 'timeout') {
                                $("#loadingTip").addClass("none-display");
                                $("#contactUsTitle").html("取消失败");
                                $("#contactUsDiv").removeClass("none-display");
                            }
                        }
                    });
                })
            }else{
                $("#contactUsTitle").html("取消失败");
                $("#contactUsDiv").removeClass("none-display");
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                $("#loadingTip").addClass("none-display");
                $("#contactUsTitle").html("取消失败");
                $("#contactUsDiv").removeClass("none-display");
            }
        }
    })
}
var validator = {
    "messages":{
        "limitSameName":'<div class="book-invalid-name-msg-area same-name-msg"><i class="warning-orange-item"></i><span class="important" >姓名不能重复,请重新填写</div>',
        "limitNameMaxLenLastName":'<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >姓(LastName)字符长度最多${n}位</div>',
        "limitNameMaxLenFirstName":'<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >名(FirstName)字符长度最多${n}位</div>'
    }
};
/**
 * created by wangjie on 2016.9.20
 * 检查姓名字符串是否符合条件
 */
function checkNameString(){
    var emptyName = '<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >请填写客人姓名</div>';
    var invalidName = '<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >姓和名需要用空格隔开</div>';
    var lastNameNotOneWord = '<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >姓(LastName)字符长度需要最少2位</div>';
    var firstNameNotOneWord = '<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >名(FirstName)字符长度需要最少2位</div>';
    var limitSameName = validator.messages.limitSameName;



    $(".book-invalid-name-msg-area").remove();
    var isOk = true;
    var travellerNameStr = "";

    var travels = $(".travel-name").each(function(){
        var val = $(this).val();
        val = val.replace(/(^\s*)|(\s*$)/g, "");
        $(this).removeClass("border-highlight");

        if (val == ""){
            isOk = false;
            $(this).after(emptyName);
            $(this).addClass("border-highlight");
            return;
        }
        var index = val.indexOf(" ");
        if (index < 0){
            isOk = false;
            $(this).after(invalidName);
            $(this).addClass("border-highlight");
            return;
        }
        if (index == 1){
            isOk = false;
            $(this).after(lastNameNotOneWord);
            $(this).addClass("border-highlight");
            return;
        }

        var lastIndex = val.lastIndexOf(" ");
        if (lastIndex == val.length - 2){
            isOk = false;
            $(this).after(firstNameNotOneWord);
            $(this).addClass("border-highlight");
            return;
        }
        if(isLimSaName === 'true'){
            if(travellerNameStr.indexOf(val) >= 0){
                isOk = false;
                $(this).after(limitSameName);
                $(this).addClass("border-highlight");
                return;
            }else{
                travellerNameStr += val+",";
            }
        }

        var lastName = val.substring(0,index);
        var firstName = val.substring(index+1);

        if(typeof (limitNameMaxLen)!="undefined"){
            if(lastName.length>limitNameMaxLen){
                isOk = false;
                $(this).after(validator.messages.limitNameMaxLenLastName.format({"n":limitNameMaxLen}));
                $(this).addClass("border-highlight");
                return;
            }

            if(firstName.length>limitNameMaxLen){
                isOk = false;
                $(this).after(validator.messages.limitNameMaxLenFirstName.format({"n":limitNameMaxLen}));
                $(this).addClass("border-highlight");
                return;
            }
        }

        var parent = $(this).parents(".traveller-name-select");
        parent.find(".first-name").val(firstName.toUpperCase());
        parent.find(".last-name").val(lastName.toUpperCase());
    });
    var isScollSet = false;
    if(!isOk){
        $("#orderMask").addClass("none-display");
        $("#loadingTip").addClass("none-display");
        var inputTravellerNameArea = $("#omInputNameArea").offset().top;
        $(document).scrollTop(inputTravellerNameArea);
        isScollSet = true;
    }

    //检查填写人信息,电话，姓名和邮箱
    var contactPersonName = $("#operator").val().trim();
    var contactPersonPhone = $("#operatorMobile").val().trim();
    var contactPersonEmail = $("#operatorEmail").val().trim();
    if(!contactPersonName){
        $("#operator").addClass("border-highlight");
        isOk = false;
        $("#operatorMsg").addClass("important").html('请填写您本人姓名');
    }else{
        $("#operator").removeClass("border-highlight");
        $("#operatorMsg").html('');
    }

    if(!contactPersonPhone){
        $("#operatorMobile").addClass("border-highlight");
        $("#operatorMobileMsg").addClass("important").html('请填写您本人电话');
        isOk = false;
    }else{
        $("#operatorMobile").removeClass("border-highlight");
        $("#operatorMobileMsg").removeClass("important").html('订单发生异常时与您紧急联系');
    }

    if(!contactPersonEmail){
        $("#operatorEmail").addClass("border-highlight");
        $("#operatorEmailMsg").addClass("important").html('请填写您本人邮箱');
        isOk = false;
    }else{
        if(!checkEmail(contactPersonEmail)){
            $("#operatorEmail").addClass("border-highlight");
            $("#operatorEmailMsg").addClass("important").html('邮箱格式不正确');
            isOk = false;
        }else{
            $("#operatorEmail").removeClass("border-highlight");
            $("#operatorEmailMsg").removeClass("important").html('接收订单确认、临近取消日提醒等邮件通知');
        }
    }

    if(!isOk){
        var inputTravellerNameArea = $("#contactInputArea").offset().top;
        if(!isScollSet){
            $(document).scrollTop(inputTravellerNameArea);
        }
    }
    setCookie("contact_person_name",contactPersonName,24 * 365,"/");
    setCookie("contact_person_phone",contactPersonPhone,24 * 365,"/");
    setCookie("contact_person_email",contactPersonEmail,24 * 365,"/");
    return isOk;
}

function checkEmail(str){
    var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
    if(re.test(str)){
        return true;
    }
    return false;
}

function checkMobile(str) {
    var re = /^1\d{10}$/
    if (re.test(str)) {
        return true;
    }
    return false;
}
/***orderTip Function area**End***/
function checkPrice(){
    try {
        setRemarkInfo();
        $.ajax({
            type: "POST",
            data: $("#bookForm").serialize(),
            url: "./checkRate",
            dataType: "json",
            timeout: 120000,
            success: function (data) {
                if (data == undefined || data == null) {
                    alert("查询酒店库存信息失败，请稍候再试");
                    $("#loadingTip").addClass("none-display");
                    $("#orderMask").addClass("none-display");
                    return;
                }
                showDebugInfo(data);
                if (data.resCode == "1000") {
                    //成功
                    $("#checkRateSessionKey").val(data.data.checkRateSessionKey);
                    makeOrder();
                } else if (data.resCode == "600") {
                    //价格升高
                    var highPrice = data.data.agentHotelAvailabilityList[data.data.targetIndex].currency + data.data.agentHotelAvailabilityList[data.data.targetIndex].totalRate;
                    $("#orderHighPrice").html(highPrice);
                    $("#checkRateSessionKey").val(data.data.checkRateSessionKey);

                    $("#loadingTip").addClass("none-display");
                    $("#highPriceTip").removeClass("none-display");
                }else if (data.resCode == "610") {
                    $("#policyChangeArea").addClass("none-display");
                    $("#policyAndPriceChangeTitle").html("政策变动");
                    //政策变化
                    $("#checkRateSessionKey").val(data.data.checkRateSessionKey);
                    $("#policyAndPriceChangeContent").html("我们抱歉的通知您，您想预订的房间的取消与退改政策发生变化。");
                    var checkInInOrder = $("#checkInInOrder").html();
                    var checkInDate = convert2DayDate(checkInInOrder);
                    var changePolicyHtml = showPolicyChanged(data.data.policies,checkInDate);
                    $("#policyChangeDetail").html(changePolicyHtml);
                    $("#loadingTip").addClass("none-display");
                    $("#policyAndPriceChangeTip").removeClass("none-display");
                }else if (data.resCode == "620") {
                    //价格升高,政策变化
                    var highPrice = data.data.agentHotelAvailabilityList[data.data.targetIndex].currency + data.data.agentHotelAvailabilityList[data.data.targetIndex].totalRate;
                    $("#orderHighPriceInPolicyChanged").html(highPrice);
                    $("#policyAndPriceChangeTitle").html("价格和政策变动");
                    $("#checkRateSessionKey").val(data.data.checkRateSessionKey);
                    $("#policyAndPriceChangeContent").html("我们抱歉的通知您，您想预订的房间的价格与政策发生变化。")
                    var checkInInOrder = $("#checkInInOrder").html();
                    var checkInDate = convert2DayDate(checkInInOrder);
                    var changePolicyHtml = showPolicyChanged(data.data.policies,checkInDate);
                    $("#policyChangeArea").removeClass("none-display");
                    $("#policyChangeDetail").html(changePolicyHtml);
                    $("#loadingTip").addClass("none-display");
                    $("#policyAndPriceChangeTip").removeClass("none-display");
                } else if (data.resCode == "700") {
                    //没空房间
                    $("#loadingTip").addClass("none-display");

                    var orderId = $("input[name=\"searchId\"]").val();
                    $("#searchOtherRoom").attr("order-id", orderId);
                    $("#noRoomLeave").removeClass("none-display");
                } else if (data.resCode == "110") {
                    //下单失败
                    $("#loadingTip").addClass("none-display");
                    $("#orderFailTip").removeClass("none-display");
                } else if (data.resCode == "500") {
                    //不清楚
                    $("#loadingTip").addClass("none-display");
                    $("#appendOrderDiv").removeClass("none-display");

                } else if (data.resCode == "33") {
                    //系统异常
                    $("#loadingTip").addClass("none-display");
                    $("#orderFailTip").removeClass("none-display");
            } else if(data.resCode =="8111"){
                $("#reSubmitTip .tip-orange-btn").bind('click', function(){
                    $("#reSubmitTip").addClass("none-display");
                    $("#orderMask").addClass("none-display");
                });
                $("#loadingTip").addClass("none-display");
                $("#reSubmitTip").removeClass("none-display");
            }else{
                    $("#orderFailTip").removeClass("none-display");
                    $("#loadingTip").addClass("none-display");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (textStatus == 'timeout') {
                    $("#loadingTip").addClass("none-display");
                    $("#appendOrderDiv").removeClass("none-display");
                } else {
                    alert("查询酒店库存信息失败，请稍候再试");
                    $("#loadingTip").addClass("none-display");
                    $("#orderMask").addClass("none-display");
                }
                var logInfo = JSON.stringify(XMLHttpRequest) + '|' + JSON.stringify(textStatus) + '|' + JSON.stringify(errorThrown);
                jsTrackLog(logInfo, 'jsSecondVertifyAjax');
            }
        });
    }catch (e){
        var logInfo = e;
        if(e.message && e.stack){
            logInfo = e.message+'|'+e.stack;
        }
        jsTrackLog(logInfo, 'jsSecondVertify');
    }
}

var isHasMakeOrder = false;
function makeOrder(){
    try {
        if (!isHasMakeOrder) {
            isHasMakeOrder = true;
        }
        var searchId = $("#searchId").val();

        $("#loadTipContent").html("系统正在下单，请勿关闭页面...");

        $("#highPriceTip").addClass("none-display");
        $("#policyAndPriceChangeTip").addClass("none-display");
        $("#orderMask").removeClass("none-display");
        $("#loadingTip").removeClass("none-display");

        setRemarkInfo();

        $.ajax({
            type: "POST",
            data: $("#bookForm").serialize(),
            url: "./hotelBookingOnline",
            dataType: "json",
            timeout: 120000,
            success: function (data) {
                showDebugInfo(data);
                //alert("data.resCode(100先下单后支付，50先支付后下单):" + data.resCode);
                if (data.resCode == "100") {
                    //先下单后支付
                    if (data.msg == "BOOK_CONFIRMED") {
                        location.href = "./bookSuccess?orderId=" + data.data.orderIdStr;
                    } else if (data.msg == "BOOK_FAILED") {
                        //没空房间
                        $("#loadingTip").addClass("none-display");
                        var orderId = $("input[name=\"searchId\"]").val();
                        $("#searchOtherRoom").attr("order-id", orderId);
                        bookNoRoom(data.data.orderIdStr,searchId);
                    } else {
                        //不清楚
                        $("#loadingTip").addClass("none-display");
                        $("#appendOrderDiv").removeClass("none-display");
                    }
                } else if (data.resCode == "50") {
                    //先支付后下单
                    location.href = "./onlinePaymentBeforeBook?orderId=" + data.data.orderIdStr;
                }  else if(data.resCode == "120"){
                    //授信余额不足
                    $("#loadingTip").addClass("none-display");
                    $("#NotEnoughTip").removeClass("none-display");
                }
                else if (data.resCode == "600") {
                    //价格升高
                    var highPrice = data.data.agentHotelAvailabilityList[data.data.targetIndex].currency + data.data.agentHotelAvailabilityList[data.data.targetIndex].totalRate;
                    $("#orderHighPrice").html(highPrice);
                    $("#checkRateSessionKey").val(data.data.checkRateSessionKey);

                    $("#continueBooking").bind('click', makeOrder);
                    $("#highPriceTip .tip-white-btn").bind('click', function () {
                        $("#highPriceTip").addClass("none-display");
                        $("#orderMask").addClass("none-display");
                    });
                    $("#loadingTip").addClass("none-display");
                    $("#highPriceTip").removeClass("none-display");
                } else if (data.resCode == "700") {
                    //没空房间
                    $("#loadingTip").addClass("none-display");
                    bookNoRoom(data.data.orderIdStr,searchId);
                } else if (data.resCode == "500" || data.resCode == "110") {
                    //不清楚
                    $("#loadingTip").addClass("none-display");
                    $("#appendOrderDiv").removeClass("none-display");

                } else if (data.resCode == "33") {
                    //系统异常
                    $("#loadingTip").addClass("none-display");
                    $("#orderFailTip").removeClass("none-display");
                } else {
                    alert("下单失败")
                    $("#loadingTip").addClass("none-display");
                    $("#orderFailTip").removeClass("none-display");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (textStatus == 'timeout') {
                    $("#orderFailTip").removeClass("none-display");
                    $("#loadingTip").addClass("none-display");
                }
                var logInfo = JSON.stringify(XMLHttpRequest) + '|' + JSON.stringify(textStatus) + '|' + JSON.stringify(errorThrown);
                jsTrackLog(logInfo, 'jsBookingAjax');
            }
        });
    }catch (e){
        var logInfo = e;
        if(e.message && e.stack){
            logInfo = e.message+'|'+e.stack;
        }
        jsTrackLog(logInfo, 'jsBooking');
    }
}

function setTravelNames(){
    var peopleNum = $(".travel-name").size();
    $.ajax({
        type:'POST',
        url:'./setRandomName',
        data:{count:peopleNum},
        dataType:'json',
        timeout:30000,
        success:function(data){
            var i = 0;
            for(key in data.data){
                $(".travel-name")[i].value = data.data[key];
                i++;
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    })
}

function exportPdf(){
    _hmt.push(['_trackEvent', 'bookSuccess', 'voucher download', 'search rasult','-']);
    downLoad("VENCHOR");
}

function exportCheckInPdf(){
    _hmt.push(['_trackEvent', 'bookSuccess', 'comfirmation download', 'search rasult','-']);
    downLoad("HOTEL");
}
function downLoad(fileType) {
    var form = $("<form>");   //定义一个form表单
    form.attr('style', 'display:none');   //在form表单中添加查询参数
    form.attr('target', '');
    form.attr('method', 'post');
    form.attr('action', 'downloadPdf');

    var input1 = $('<input>');
    input1.attr('type', 'hidden');
    input1.attr('name', 'orderId');
    input1.attr('value', orderId);

    var input2 = $('<input>');
    input2.attr('type', 'hidden');
    input2.attr('name', 'fileType');
    input2.attr('value', fileType);
    $('body').append(form);  //将表单放置在web中
    form.append(input1);   //将查询参数控件提交到表单上
    form.append(input2);
    form.submit();

}

function getTimeStr(obj){
    var  month = obj.getMonth()+1;
    return obj.getFullYear()+'-'+month+'-'+obj.getDate()+'&nbsp;'+obj.getHours()+':'+timeCover0(obj.getMinutes())+':'+timeCover0(obj.getSeconds());
}

function initOrderEditHistory(orderId){
    $.ajax({
        type:'POST',
        url:"./getOrderEditHistorys",
        data:{"orderId":orderId},
        dataType:"json",
        timeout:30000,
        success:function(data){
            if(data == null){
                return;
            }
            var changCustomListFlag = true;
            var item =  data.data;
            var htmlStr = '';
            if(item != null && item!=''){
                for(index in item){
                    htmlStr += '<div class="clearfix mg10-0 modify-history-time"><div class="mr10 fl"  style="width: 140px;">'+getTimeStr(new Date(item[index].createTime))+'</div>';
                    if(item[index].modifyType>0 && item[index].resultType == null){

                        if(item[index].modifyType==1){
                            changCustomListFlag = false;
                        }
                        var modifyTypeStr = getmodifyTypeStr(item[index].modifyType);
                        htmlStr += '<i class="fl"  style="width: 487px;">您提交了订单修改申请，修改项为：【'+modifyTypeStr+'】</i></div>';
                    }else{
                        if(item[index].resultType==0){
                            htmlStr += '<i class="fl" style="width: 487px;">您的修改申请已被确认';
                        }else{
                            htmlStr +='<i class="fl" style="width: 487px;">抱歉，您的修改申请已被驳回。驳回原因：【'+item[index].remark+'】';
                        }
                        htmlStr +='。确认人：蘑菇客服【'+item[index].creator+'】。请查看订单详情，如需其他帮助，请联系蘑菇在线客服。</i></div>';
                    }
                }
                if(!changCustomListFlag){
                    $("#changCustomListFlag").html("false");//标记是否可以更改客人名单,当为false不可以修改过
                }

                $(".order-edit-list").html(htmlStr);
                $(".history-num").html(item.length);
                $(".order-edit-history").removeClass("none-display");
            }
        }

    })
}
function getmodifyTypeStr(obj){
    if(obj==1){
        return "更改客人名单";
    }
    if(obj==2){
        return "更改入住日期";
    }
    if(obj==3){
        return "更改房间数";
    }
    if(obj==4){
        return "更改联系人团号";
    }
}

function initOrderInfo(orderId){

    $.ajax({
        type:'POST',
        url:"./getDetails",
        data:{"orderId":orderId},
        dataType:"json",
        timeout:30000,
        success:function(data){
            if(data == null){
                return;
            }
            var order = data.data.order;
            var rooms = data.data.hotelRoomList;
            var hotel = data.data.product;
            var roomTypeEnums = data.data.roomTypeEnums;
            var orderSpecialServices = data.data.orderSpecialServices;
            var doublecheckNo = order.hotelAffirmNo;
            $("#ordQueryId").val(hotel.queryId);
            //订单编号
            $("#orderCode").html(order.orderIdStr);
            //订单金额
            $(".order-total-rate").each(function(){
                $(this).html(order.orderPrice.companyCurrency+' '+digitForIgnore(order.orderPrice.salePrice,1));
            });
            if(doublecheckNo!=undefined && doublecheckNo!=null && doublecheckNo!=''){
               $("#dbcheckicon").removeClass("none-display");
            }
            //价格浮窗
            var breakDown = eval('(' + rooms[0].priceBreakDown + ')');
            if(breakDown != null && breakDown.length > 0) {
                var priceDetail = '<i class="order-price-detail pos-bd pop-price-hover">';
                priceDetail += orderPriceDetaiInfo(rooms, order.orderPrice.salePrice);
                priceDetail += '</i>';
                $(".order-total-rate").append(priceDetail);
            }

            //酒店图片
            $("#orderHotelImage").attr("src",getHotelUrl(hotel.hotelId,hotel.imageUrl,"!small"));
            //入住日期、退房日期、预定日期
            if(order.bookingDate){
                $("#bookTime").html(data.data.bookingDateStr);
            }
            if(order.checkIn){
                $("#checkInTime").html(data.data.checkInStr);
            }
            if(order.checkOut){
                $("#checkOutTime").html(data.data.checkOutStr);
            }
            if(order.optionDate){
                $("#orderOptionDay").html(data.data.xOptionDateStr);
            }
            //酒店位置
            var hotelLocation = "";
            if(hotel.address){
                hotelLocation += hotel.address;
            }
            $("#hotelLocation").html(hotelLocation);
            //酒店名称
            var hotelnames = hotel.productName.split("/");
            var hotelname = null;
            if(hotelnames.length==1 || hotelnames[0] == hotelnames[1]){
                hotelname = hotelnames[0];
            }else{
                hotelname = hotel.productName;
            }

            $(".order-hotel-name").html(hotelname);

            var transactionStatusEnum = eval('(' + data.data.transactionStatusEnum + ')');

            //天数 儿童数 成人数
            //酒店名称
            $("#spulierId").html(order.referenceCode);
            $("#totalNight").html(order.duration);
            $("#childNum").html(order.childrenQuantity);
            $("#adultNum").html(order.adultQuantity);
            $("#roomNum").html(rooms.length);
            $("#orderStatus").html(order.orderStatusCN);
            $("#nationality").html(order.travellerCountryCn);
            //单结且白名单，显示支付状态
            $("#paymentStatus").html(transactionStatusEnum[order.transactionStatus]);
            //订单操作
            if (data.data.payFlag == 1 || data.data.payFlag == '1'){
                $("#odOptionArea").html('<a class="paymentOrder" target="_blank" href="./onlinePaymentAfterBook?orderId=' + orderId  + '&flag=second" orderid="'+orderId+ '"><span class="order-detail-option-btn mogu-red-bg mr10 od-edit-ben">支付订单</span></a>');
                $("#paymnetShow").css("display","block");
            }
            if(order.orderStatus == '120' || order.orderStatus == 120){
                if (order.bookingDate < order.xOptionDate){
                    $(".order-pay-message-div").removeClass("none-display");
                }
                $(".od-booking-item").removeClass("none-display");

/*                if (data.data.payFlag == 1 || data.data.payFlag == '1'){
                    $("#odOptionArea").html('<span class="order-detail-option-btn mogu-red-bg mr10 od-edit-ben" onclick="paymentOrder(\''+orderId+'\')">支付订单</span>'
                        +'<span class="order-detail-option-btn mogu-red-bg mr10 od-edit-ben" onclick="editOrder(\''+orderId+'\')">修改订单</span>'
                        +'<span class="order-detail-option-btn dark-grey-bg od-cancel-btn" onclick="cancelOrder(\''+orderId+'\')">申请取消</span>');
                }else {*/
                $("#odOptionArea").append('<span class="order-detail-option-btn mogu-red-bg mr10 od-edit-ben" onclick="editOrder(\''+ orderId +'\','+order.transactionStatus+','+order.customerPayStatus + ')">修改订单</span>');
                if (new Date().getTime()<(order.checkIn+86399999)){
                    $("#odOptionArea").append('<span class="order-detail-option-btn dark-grey-bg od-cancel-btn" onclick="cancelOrder(\'' + orderId + '\')">申请取消</span>');
                }
                //}

                if (order.voucherLabel != null && order.voucherLabel !=''){
                    $(".od-voucher-item").removeClass("none-display");
                }
            }
            orderDetailRooms(rooms,roomTypeEnums);

            //团号
            $("#linkman").html(order.userName);
            //联系人
            if (order.companyGroupCode == undefined || order.companyGroupCode ==null|| order.companyGroupCode ==''){
                $("#orderGourpIdArea").addClass("none-display");
            }else{
                $("#orderGourpId").html(order.companyGroupCode);
                $("#orderGourpIdWrite").val(order.companyGroupCode);
            }
            var starHtml = "";
            var starCount = hotel.level / 10;
            for (var i = 1; i <= starCount; i++) {
                starHtml += '<i class="star-icon"></i>';
            }
            $("#hotelStar").html(starHtml);
            //特殊要求
            var specialRequestsHtml ="";
            if (order.remark != undefined && order.remark!=null && order.remark!=""){
                var specialRequests = order.remark.split(",");
                for(var key in specialRequests){
                    specialRequestsHtml +='<div>'+specialRequests[key]+'</div>';
                }
            }
            if(orderSpecialServices != undefined && orderSpecialServices!=null && orderSpecialServices.length !=0){
                var speDerviceType=['','早餐','加床'];
                for(var key in orderSpecialServices){
                    if(orderSpecialServices[key].type === 3){//其他
                        specialRequestsHtml +='<div>'+orderSpecialServices[key].itemName+', '+order.orderPrice.companyCurrency+orderSpecialServices[key].salePrice+'</div>';
                    }else{
                        specialRequestsHtml +='<div>'+speDerviceType[orderSpecialServices[key].type]+', '+orderSpecialServices[key].companyCurrency+orderSpecialServices[key].salePrice+'</div>';
                    }

                }
            }
            if(specialRequestsHtml == ''){
                specialRequestsHtml = "无";
            }
            $("#specialRequest").html(specialRequestsHtml);
            //各种政策
            var cancelPolicyHtml ="";
            var editPolicyHtml ="";
            var renamePolicyHtml ="";
            var cancelpolicies = data.data.policyCommon;
            if (cancelpolicies != undefined && cancelpolicies!=null && cancelpolicies.length > 0){
                for(var iCancel = cancelpolicies.length -1; iCancel >= 0;iCancel--) {
                    var policy = cancelpolicies[iCancel];
                    var startTime = '';
                    var endTime = '之后';
                    if (policy.endDate == null){
                        startTime = "预订时间"
                    }else{
                        startTime = convertMiliSecond2DateTimeExcSecondString(policy.endDate);
                    }

                    if(policy.startDate < order.checkIn){
                        endTime = '至 ' + convertMiliSecond2DateTimeExcSecondString(policy.startDate);
                    }


                    if (policy.policyType == 'cancellation'){
                        if (order.bookingDate > policy.startDate){
                            continue;
                        }
                        cancelPolicyHtml += startTime + endTime + '   取消费用：' + policy.remark + '<br/>';
                    }else if(policy.policyType == 'amendment'){
                        if(endTime != ''){
                            editPolicyHtml += startTime + endTime;
                        }
                        editPolicyHtml += policy.remark+'<br/>';
                    }else if(policy.policyType == 'changename'){
                        if(endTime != ''){
                            renamePolicyHtml += startTime + endTime;
                        }
                        renamePolicyHtml += policy.remark+'<br/>';
                    }else{
                        //do nothing
                    }
                }

            }
            if (cancelPolicyHtml != ''){
                $("#cancelPolicies").html(cancelPolicyHtml);
            }else{
                $("#cancelPolicies").html("取消订单将收取下单金额的100%作为取消费用");
            }

            if (editPolicyHtml != ''){
                $("#editPolicies").html(editPolicyHtml);

            }else{
                $("#editPolicies").html("不可修改");
            }

            if (renamePolicyHtml != ''){
                $("#renamePolicies").html(renamePolicyHtml);
            }else{
                $("#renamePolicies").html("不可改名");
            }


            //餐食说明
            var mealHtml ='';
            if (order.breakfastDescription != null && order.breakfastDescription != ''){
                mealHtml = order.breakfastDescription;
                $("#mealInfo").html(mealHtml);
            }else if(order.mealDescription != null && order.mealDescription != ''){
                mealHtml = order.mealDescription;
                $("#mealInfo").html(mealHtml);
            }else{
                $("#mealArea").addClass("none-display");
            }

            //注意事项
            var reminderHtml ='';
            if (hotel.isReminder && hotel.isReminder === 1){
                $("#reminderMessage").html(hotel.reminderContent);
            }else{
                $("#reminderArea").addClass("none-display");
            }

            //重要信息
            var messageHtml ='';
            var room = rooms[0];
            if (room.rateComments != undefined && room.rateComments!= null && room.rateComments != ''){
                messageHtml += room.rateComments+'<br/>';
            }
            if (room.message != undefined && room.message!= null){
                var messageArray  = eval('(' + room.message + ')');
                for(var iMessage in messageArray){
                    var message = messageArray[iMessage];
                    if(message != null) {
                        if (message.name == "rateComments") {
                            continue;
                        }
                        if(message.description) {
                            if (messageHtml.indexOf(message.description) < 0) {
                                messageHtml += message.description;
                            }
                        }
                    }
                }
            }

            if(order.message != null){
                messageHtml += order.message;
            }

            if (messageHtml != ''){
                $("#importantMessage").html(messageHtml);
            }else{
                $("#imporMessageArea").addClass("none-display");
            }

            var hotelInfos = [];
            var hotelInfo = {};
            hotelInfo.hotelId = hotel.hotelId;
            hotelInfo.hotelName = hotelname;
            hotelInfos.push(hotelInfo);
            checkHotels['checkType'] = 3;
            checkHotels['hotelInfos'] = hotelInfos;
            noteUncheckHotels(checkHotels);
            if(data.data.amendmentFlag == "false"||order.customerPayStatus==2){
                $("#changRoomNumFlag").html("false");
                $("#changCheckInFlag").html("false");
            }
            if(data.data.changeNameFlag=='false'){
                $("#changCustomListFlag").html("false");
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    })
}
var checkHotels = {};

/***orderDetailRooms***/
function orderDetailRooms(rooms,roomTypeEnums){
    var roomsHtml = '';
    var roomIndex = 1;
    for (var key in rooms){
        var room = rooms[key];
        var totalPeople = Number(room.adultsQuantity) + Number(room.childrenQuantity);
        //拼接客人的用关系数组保存(leader,adult,children)
        var roomTravelHtml = [];
        roomTravelHtml['adult'] ='';
        roomTravelHtml['children'] ='';
        var adultIndex = 1;
        var childIndex = 1;
        for(var travelKey in room.travellerList){
            var travel = room.travellerList[travelKey];
            if (travel.type == '0' || travel.type == 0){
                roomTravelHtml['adult'] += '<div>成人'+adultIndex+'：<span class="bold">'+travel.lastName+' '+travel.firstName+'</span></div>';
                adultIndex ++;
            }else if (travel.type == '1' || travel.type == 1){
                roomTravelHtml['children'] += '<div>儿童'+childIndex+'('+travel.age+'岁)'+'：<span class="bold">'+travel.lastName+' '+travel.firstName+'</span></div>';
                childIndex ++;
            }
        }

        //拼接房间信息
        roomsHtml += '<div class="order-hotel-detail-info ';
        if (roomIndex != 1){
            roomsHtml += ' border-top-d1-1 ';
        }
        roomsHtml +=' clearfix">'
            + '<div class="fl hotel-room-name"> <span class="hotel-room-index fl">'+ roomIndex
            + '</span><div class="fl hotel-room-s-name"><div>';
        if(roomTypeEnums.hasOwnProperty(room.roomType)){
            roomsHtml += roomTypeEnums[room.roomType];
            roomsHtml += "-";
        }
        roomsHtml+= room.roomName + '</div>';
        if(room.bedExtraStatus === 1 || room.bedExtraStatus ==='1'){
            roomsHtml +='<div class="c-green"><i class="add-bed-icon"></i>'+room.bedExtraDescription+'</div>';
        }
        roomsHtml +='</div></div>'
            + '<div class="fl hotel-room-people-number">'+totalPeople+'人</div>'
            + '<div class="fl">'
            +  roomTravelHtml['adult']+roomTravelHtml['children']
            +'</div></div>';
        roomIndex++;

    }
    $("#orderHotelRoomArea").html(roomsHtml);
}

/****pagenation*************start*/
function paginationOrder(count){
    var pageIndex = 0;     //页面索引初始值
    var pageSize = $('.pageSize').val();     //每页显示条数初始化，修改显示条数，修改这里即可
    //如果总数不大于每页个数，则不显示分页
    if (count < pageSize){
        $("#orderPagination").empty();
        return;
    }

    //分页，PageCount是总条目数，这是必选参数，其它参数都是可选
    $("#orderPagination").pagination(count, {
        callback: PageCallback,  //PageCallback() 为翻页调用次函数。
        prev_text: "« 上一页",
        next_text: "下一页 »",
        items_per_page:pageSize,
        num_edge_entries: 2,       //两侧首尾分页条目数
        num_display_entries: 6,    //连续分页主体部分分页条目数
        current_page: pageIndex   //当前页索引
    });
    //翻页调用
    function PageCallback(index, jq) {
        InitTable(index);
    }

    //请求数据
    function InitTable(pageIndex) {
        clearAllSelect();
        var queryForm = $("#queryForm");
        pageIndex +=1;
        var querySearchData = queryForm.serialize() + '&page='+pageIndex+'&rows='+pageSize;
        $.ajax({
            type: "POST",
            dataType: "json",
            url: 'queryList',      //提交到一般处理程序请求数据
            data:querySearchData ,          //提交两个参数：pageIndex(页面索引)，pageSize(显示条数)
            timeout:30000,
            success: function(data) {
                if (data == undefined || data == null || data.total == undefined ||
                    data == null || data.total == 0){
                    $("#searchResultDiv").addClass("none-display");
                    $("#rowsNum").addClass("none-display");
                    $("#noResultShow").removeClass("none-display");
                    $("#soResults tr:gt(0)").remove();
                }else {
                    var resultHtml = showOrderQueryResult(data.rows);
                    $("#soResults tr:gt(0)").remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                    $("#soResults").append(resultHtml);
                    checkResultEvent();
                    $("#searchHotelNum").html(data.total);
                    $("#searchHotelNumUnit").html("订单");
                    $("#searchResultDiv").removeClass("none-display");
                    $("#rowsNum").removeClass("none-display");
                    $("#noResultShow").addClass("none-display");
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
                if(textStatus == 'timeout'){
                    //todo note log
                }
            }
        });
    }
}

function paginationQuickSearchOrder(count,flag){
    var pageIndex = 0;     //页面索引初始值
    var pageSize = $('.pageSize').val();     //每页显示条数初始化，修改显示条数，修改这里即可
    //如果总数不大于每页个数，则不显示分页
    if (count < pageSize){
        $("#orderPagination").empty();
        return;
    }

    //分页，PageCount是总条目数，这是必选参数，其它参数都是可选
    $("#orderPagination").pagination(count, {
        callback: PageCallback,  //PageCallback() 为翻页调用次函数。
        prev_text: "« 上一页",
        next_text: "下一页 »",
        items_per_page:pageSize,
        num_edge_entries: 2,       //两侧首尾分页条目数
        num_display_entries: 6,    //连续分页主体部分分页条目数
        current_page: pageIndex   //当前页索引
    });
    //翻页调用
    function PageCallback(index, jq) {
        InitTable(index);
    }

    //请求数据
    function InitTable(pageIndex) {
        clearAllSelect();
        pageIndex +=1;
        var sortname=$("#sortname").val();
        var sortflag=$("#sortflag").val();
        $.ajax({
            type: "POST",
            dataType: "json",
            url: 'getOrderNumInfo',      //提交到一般处理程序请求数据
            data:{'flag':flag,'page':pageIndex,'rows':pageSize,'sortname':sortname,'sortflag':sortflag} ,          //提交两个参数：pageIndex(页面索引)，pageSize(显示条数)
            timeout:30000,
            success: function(data) {
                if (data == undefined || data == null || data.total == undefined ||
                    data == null || data.total == 0){
                    $("#searchResultDiv").addClass("none-display");
                    $("#noResultShow").removeClass("none-display");
                    $("#rowsNum").addClass("none-display");
                    $("#soResults tr:gt(0)").remove();
                }else {
                    var resultHtml = showOrderQueryResult(data.rows);
                    $("#soResults tr:gt(0)").remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                    $("#soResults").append(resultHtml);
                    checkResultEvent();
                    $("#searchHotelNum").html(data.total);
                    $("#searchHotelNumUnit").html("订单");
                    $("#rowsNum").removeClass("none-display");
                    $("#searchResultDiv").removeClass("none-display");
                    $("#noResultShow").addClass("none-display");
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
                if(textStatus == 'timeout'){
                    //todo note log
                }
            }
        });
    }
}

//团号管理订单时的分页调用，每页显示5个订单
function paginationGroup(count){
    var pageIndex = 0;     //页面索引初始值
    var pageSize = 5;     //每页显示条数初始化，修改显示条数，修改这里即可
    //如果总数不大于每页个数，则不显示分页
    if (count < pageSize){
        $("#orderPagination").empty();
        return;
    }
    //分页，PageCount是总条目数，这是必选参数，其它参数都是可选
    $("#orderPagination").pagination(count, {
        callback: PageCallback,  //PageCallback() 为翻页调用次函数。
        prev_text: "« 上一页",
        next_text: "下一页 »",
        items_per_page:pageSize,
        num_edge_entries: 2,       //两侧首尾分页条目数
        num_display_entries: 6,    //连续分页主体部分分页条目数
        current_page: pageIndex   //当前页索引
    });
    //翻页调用
    function PageCallback(index, jq) {
        InitTable(index);
    }

    //请求数据
    function InitTable(pageIndex) {
        clearAllSelect
        pageIndex +=1;

        $.ajax({
            type: "POST",
            dataType: "json",
            url: 'queryListForGroupId',      //提交到一般处理程序请求数据
            data:{'page':pageIndex,'rows':pageSize} ,          //提交两个参数：pageIndex(页面索引)，pageSize(显示条数)
            timeout:30000,
            success: function(data) {
                if (data == undefined || data == null || data.total == undefined ||
                    data == null || data.total == 0){
                    $("#searchResultDiv").addClass("none-display");
                    $("#noResultShow").removeClass("none-display");
                    $("#soResults tr:gt(0)").remove();
                }else {
                    var resultHtml = showOrderQueryResultGroups(data.rows);//todo
                    $("#soResults tr:gt(0)").remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                    $("#soResults").append(resultHtml);
                    $(".hiddenorshow").click();
                    checkResultEvent();
                    showTotalAmountGroup();
                    $("#searchHotelNum").html(data.total);
                    $("#searchHotelNumUnit").html("团");
                    $("#searchResultDiv").removeClass("none-display");
                    $("#noResultShow").addClass("none-display");
                    changresultShowType(false);
                }
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
                if(textStatus == 'timeout'){
                    //todo note log
                }
            }
        });
    }
}



/****pagenation*************end*/

/**orderList export-excel-icon *****/
function exportExcel(){

}

function initSummaryPosition(){
    var summaryHeight = $(".book-summary").offset().top - 30;
    var summaryWidth = $(".book-summary").offset().left;
    //滚动条事件
    $(window).scroll(function(){
        //获取滚动条的滑动距离
        var scroH = $(this).scrollTop();
        //滚动条的滑动距离大于等于定位元素距离浏览器顶部的距离，就固定，反之就不固定
        if(scroH>=summaryHeight){
            $(".book-summary").addClass("order-summary-fixed");
            $(".book-summary").css("left",summaryWidth+"px");
            $(".book-summary").removeClass("fr");
        }else if(scroH<summaryHeight){
            $(".book-summary").removeClass("order-summary-fixed");
            $(".book-summary").addClass("fr");
            summaryWidth = $(".book-summary").offset().left;
        }
    });
}

function getHistoryGroupTip(){
    $.ajax({
        type:"POST",
        url:"./getHistoryGroup",
        dataType:"json",
        timeout:30000,
        success:function(data){
            var historyGroupHtml="";

            var historyGroupInput = $('#historyGroupInput');
            var historyGroupDiv = $("#historyGrounpTipArea");

            if (data.data.companyGroupId != undefined && data.data.companyGroupId != null && data.data.companyGroupId.length >0) {
                for (var key in data.data.companyGroupId) {
                    var historyInfo = data.data.companyGroupId[key];
                    historyGroupHtml += '<li>'+historyInfo+'</li>';
                }
                historyGroupDiv.find("ul").html(historyGroupHtml);

                historyGroupDiv.find("ul li").bind("click",function(){
                    $("#historyGrounpTipArea").addClass("none-display");
                    var historyGrounpId = $(this).html();
                    $("#historyGroupInput").val(historyGrounpId);
                    _hmt.push(['_trackEvent', 'showBookRoom', 'choose agent refrence number','-']);
                    $.ajax({
                        type:"POST",
                        data:{'companyGroupId':historyGrounpId},
                        dataType:'json',
                        timeout:30000,
                        url:'getHistoryGroupTravellerInfo',
                        success:function(data){
                            //if (data.resCode =="00000000"){
                            var travellerHtml = '';
                            var travelInputs = $(".travel-name");

                            var travellers = data.traveller;
                            for(var iTravel = 0; iTravel < travellers.length;iTravel++){
                                var traveller = travellers[iTravel];

                                if(travelInputs.size() > iTravel){
                                    travelInputs[iTravel].value = traveller.lastName+' '+traveller.firstName;

                                }
                                travellerHtml += '<li nation-id="'+traveller.lastName+' '+traveller.firstName+'">'+traveller.lastName+' '+traveller.firstName+'</li>'
                            }

                            $(".room-people-detail .traveller-name-select .select_ul").html(travellerHtml);
                            $(".room-people-detail .traveller-name-select .traveller-select-icon-div").removeClass("none-display");
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown){
                            if(textStatus == 'timeout'){
                                //todo note log
                            }
                        }
                    })
                });
            }else{
                historyGroupDiv.html("无历史团号");
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    });
}

function showHistGroupTraveller(){

    $('.traveller-name-select .traveller-select-icon-div').each(function(){
        var thisul = $(this).parent().find(".select_ul");
        var thisinput = $(this).parent().find(".travel-name");
        var thatBtn = $(this);
        $(document).bind("click",function (event) {
            event = event || window.event;
            var target = event.target || event.srcElement;
            if(target == thatBtn[0] || target.parentNode == thatBtn[0]){

                if(thisul.css("display")=="none"){
                    if(thisul.height()>200){
                        thisul.css({height:"200"+"px","overflow-y":"scroll"})
                    };
                    thisul.fadeIn("100");
                    thisul.find("li").click(function(){
                            thisul.fadeOut("100");
                            thisinput.val($(this).attr("nation-id"))
                        })
                        .hover(function(){
                            $(this).addClass("hover");
                        },function(){$(this).removeClass("hover");});
                }else{
                    thisul.fadeOut("fast");
                }
                return ;
            }

            thisul.fadeOut("fast");
        });
    });
}

function orderPriceDetaiInfo(roomsOriData,totalPrice) {
    var roomsData = [];
//根据roomRateId获取相同房型的个数
    for (var roomIndex in roomsOriData) {
        var roomRateId = roomsOriData[roomIndex].roomRateId;
        var roomNumObj = roomsData[roomRateId];
        var roomNum = 1;
        if (roomNumObj != null) {
            roomNum = roomNumObj.number+ 1;
        }
        var roomInfo = [];
        roomInfo['number'] = roomNum;
        roomInfo['name'] = roomsOriData[roomIndex].roomName;
        roomsData[roomRateId] = roomInfo;
    }


    var priceHtml = '<div class="pop-black-bd pop-price-bd">'
        + '<h2 class="mb10"><b>详细报价</b> </h2>'
        + '<table class="pop-price-table" width="100%" border="0" cellspacing="0" cellpadding="0">'
        + '<tr><td></td>';
    //房间名称即各个列标题
    for (var roomKey in roomsData) {
        priceHtml += '<td>' + roomsData[roomKey].name+'×'+ roomsData[roomKey].number+ '间</td>';
    }

    var firstRoomPriceDetail = eval('(' + roomsOriData[0].priceBreakDown + ')');
    if(roomsData.length > 1 && firstRoomPriceDetail.length >1){
        priceHtml += '<td>合计</td><td>总计</td></tr>';
    }else{
        priceHtml += '<td>总计</td></tr>';
    }


    //第二行开始为各个日期房型的价格

    var totalCurrency = firstRoomPriceDetail[0].currency;
    for (var dateIndex = 0; dateIndex < firstRoomPriceDetail.length; dateIndex++) {
        var priceBreakdown = firstRoomPriceDetail[dateIndex];
        priceHtml += '<tr><td>' + priceBreakdown.date + '</td>';
        var dateTotalRate = 0;
        for (var dateKey in roomsData) {
            for (var oriIndex in roomsOriData) {
                if (dateKey == roomsOriData[oriIndex].roomRateId) {
                    var roomData = roomsOriData[oriIndex];
                    var speciRoomBreakDown = eval('(' + roomData.priceBreakDown + ')');
                    var price = speciRoomBreakDown[dateIndex].rate;
                    var currency = speciRoomBreakDown[dateIndex].currency;
                    priceHtml += '<td valign="top">' + currency + ' ' + price;
                    dateTotalRate += price;
                    priceHtml += '</td>';
                    break;
                }
            }
        }

        if(roomsData.length > 1 && firstRoomPriceDetail.length >1){
            priceHtml += '<td valign="top">' + currency + ' ' + digitForIgnore(dateTotalRate,1);
            //每天合计的金额
            priceHtml += '</td>';
        }

        //报价总价
        if (dateIndex == 0) {
            priceHtml += '<td valign="middle" rowspan="' + firstRoomPriceDetail.length + '">' + '<b>' + totalCurrency + digitForIgnore(totalPrice,1) + '</b>';
        }
        priceHtml += "</tr>";
    }
    priceHtml += '</table><p class="f12">*价格包含酒店常规税费。不包含城市税(如有)、服务费(如有)等税费，具体以实际酒店规定为准。<br/>*每晚价格仅供参考,在预订时，最终价格以总价为准。</p></div>';

    return priceHtml;
}

function showSendEmailDiv(fileType,orderId){
    if(fileType == "HOTEL"){
        _hmt.push(['_trackEvent', 'bookSuccess', 'comfirmation email', 'search rasult','-']);
    }else if(fileType == "VENCHOR"){
        _hmt.push(['_trackEvent', 'bookSuccess', 'voucher email', 'search rasult','-']);
    }

    $("#sendMailMessage").html("");
    var sendEmailDiv =  $("#sendEmailDiv");

    sendEmailDiv.removeClass("none-display");
    sendEmailDiv.find("input").val("");
    sendEmailDiv.find("textarea").val("");
    $("#orderMask").removeClass("none-display");

    var filtTypeInput = $("#sendMailInfo").find("input[name=\"fileType\"]");
    if(filtTypeInput.size() > 0){
        filtTypeInput.val(fileType);
    }else{
        fileTypeInput = $('<input>');
        fileTypeInput.attr('type', 'hidden');
        fileTypeInput.attr('name', 'fileType');
        fileTypeInput.attr('value', fileType);
        $("#sendMailInfo").append(fileTypeInput);
    }

    var orderInput = $("#sendMailInfo").find("input[name=\"orderId\"]");
    if(orderInput.size() > 0){
        orderInput.val(orderId);
    }else{
        orderInput = $('<input>');
        orderInput.attr('type', 'hidden');
        orderInput.attr('name', 'orderId');
        orderInput.attr('value', orderId);
        $("#sendMailInfo").append(orderInput);
    }
}

function sendEmail(){
    var orderInput = $("#sendMailInfo").find("input[name=\"orderId\"]");
    if (orderInput == undefined || orderInput == null || orderInput.val() == ''){
        alert("没有要发送的订单");
        return;
    }
    var sendPeople=$("input[name=\"sendPeople\"]").val();
    if( sendPeople == ''){
        $("#sendMailMessage").html("请输入收件人邮箱");
        return;
    }

    //正则验证邮箱
    var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (!filter.test(sendPeople)){
        $("#sendMailMessage").html("邮箱的格式不正确");
        return;
    }
    $("#sendEmailDiv").addClass("none-display");
    $("#loadTipContent").html("正在发送邮件，请稍候");
    $("#loadingTip").removeClass("none-display");
    $.ajax({
        type:'POST',
        data:$("#sendMailInfo").serialize(),
        dataType:'json',
        url:'sendOrderEmail',
        timeout:30000,
        success:function(data){
		$("#loadingTip").addClass("none-display");
            $("#sendMailInfo").find("input").val("");
            if (data.resCode == "00000000"){
                $("#sendEmailSuccess").removeClass("none-display");
            }else{
                $("#contactUsTitle").html("发送失败");
                $("#contactUsDiv").removeClass("none-display");
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    })
}

//orderList
function checkResultEvent(){
    $("div.search-result-area .so-check-box-icon").click(function(event){
        var that = $(this);
        if (that.hasClass("filter-checked-icon")){
            that.removeClass("filter-checked-icon");
            showCheckInfo();
            $("#selectAllOrders").removeClass("filter-checked-icon");
            if ($("div.search-result-area .so-check-box-icon.filter-checked-icon").size() == 0){
                hideCheckInfo();
            }
        }else{
            that.addClass("filter-checked-icon");
            if ($("div.search-result-area .so-check-box-icon").size() == $("div.search-result-area .so-check-box-icon.filter-checked-icon").size()){
                $("#selectAllOrders").addClass("filter-checked-icon");
            }
            showCheckInfo();
        }

        stopEvent(event);
    });
    $("div.search-result-area .check-box-icon-group").click(function(event){
        var that = $(this);

        var groupId =that.attr("group-id");
        if (that.hasClass("filter-checked-icon")){
            that.removeClass("filter-checked-icon");
            $("tr[group-id='"+groupId+"'] .so-check-box-icon").removeClass("filter-checked-icon");
            showCheckInfo();
            $("#selectAllOrders").removeClass("filter-checked-icon");
            if ($("div.search-result-area .so-check-box-icon.filter-checked-icon").size() == 0){
                hideCheckInfo();
            }
        }else{
            that.addClass("filter-checked-icon");
            $("tr[group-id='"+groupId+"'] .so-check-box-icon").addClass("filter-checked-icon");
            if ($("div.search-result-area .so-check-box-icon").size() == $("div.search-result-area .so-check-box-icon.filter-checked-icon").size()){
                $("#selectAllOrders").addClass("filter-checked-icon");
            }
            showCheckInfo();
        }

        stopEvent(event);
    });


    $("#soResults .so-result-item .so-skip-item").click(function(){
        window.open("./orderDetail?orderId="+$(this).attr("order-id"));
    });

    $("#soResults .so-result-item .select-item").click(function(){
        stopEvent(event);
    });
}

/*
function skipToDetail(orderIdStr){
    alert('orderIdStr:'+orderIdStr);
    window.open("./orderDetail?orderId='"+orderIdStr+"'");
}
*/

function selectAllOrderBtn(){
    $("#selectAllOrders").click(function(){
        var that = $(this);
        if (that.hasClass("filter-checked-icon")){
            that.removeClass("filter-checked-icon");
            $("div.search-result-area .so-check-box-icon").removeClass("filter-checked-icon");
            $("div.search-result-area .check-box-icon-group").removeClass("filter-checked-icon");
            hideCheckInfo();
        }else{
            that.addClass("filter-checked-icon");
            $("div.search-result-area .so-check-box-icon").addClass("filter-checked-icon");
            $("div.search-result-area .check-box-icon-group").addClass("filter-checked-icon");
            showCheckInfo();
        }
    });
}

//清空全选
function clearAllSelect(){
    $("#selectAllOrders").removeClass("filter-checked-icon");
    hideCheckInfo();
}


function showCheckInfo(){
    var selectNum = $("div.search-result-area .so-check-box-icon.filter-checked-icon").size();
    $("#selectResultNum").html(selectNum);
    var allResultNum = $("div.search-result-area .so-check-box-icon").size();
    $("#allResultNum").html(allResultNum);
    var totalAmount =showTotalAmount($("div.search-result-area .so-check-box-icon.filter-checked-icon"));
    $("#totalAmount").html(totalAmount);
    $("#selectResultShowArea").removeClass("none-display");
    var totalOrderSize = $("div.search-result-area .so-check-box-icon.filter-checked-icon").size();
    var bookOrderSize = $("div.search-result-area .so-check-box-icon.filter-checked-icon.send-booking-order").size()
    var voucherOrderSize = $("div.search-result-area .so-check-box-icon.filter-checked-icon.send-voucher-order").size();
    if(bookOrderSize > 0){
        showSpeciElement("benchSendHotelOrders");
        setAvailBenchNum("benchSendHotelOrders",bookOrderSize);
        hideSpeciElement("benchDisableDownloadHotelOrders");
        showSpeciElement("benchDownloadHotelOrders");
        setAvailBenchNum("benchDownloadHotelOrders",bookOrderSize);
        hideSpeciElement("benchDisableSendHotelOrders");
    }else{
        showSpeciElement("benchDisableDownloadHotelOrders");
        hideSpeciElement("benchSendHotelOrders");
        showSpeciElement("benchDisableSendHotelOrders");
        hideSpeciElement("benchDownloadHotelOrders");
    }

    if(voucherOrderSize > 0){
        showSpeciElement("benchSendVoucherOrders");
        setAvailBenchNum("benchSendVoucherOrders",voucherOrderSize);
        hideSpeciElement("benchDisableSendVoucherOrders");
        showSpeciElement("benchDownloadVoucherOrders");
        setAvailBenchNum("benchDownloadVoucherOrders",voucherOrderSize);
        hideSpeciElement("benchDisableDownloadVoucherOrders");
    }else{
        showSpeciElement("benchDisableSendVoucherOrders");
        hideSpeciElement("benchSendVoucherOrders");
        showSpeciElement("benchDisableDownloadVoucherOrders");
        hideSpeciElement("benchDownloadVoucherOrders");
    }

    $("#hotelOrderNum").html(bookOrderSize);
    $("#voucherOrderNum").html(voucherOrderSize);
    $("#selectResultShowArea").removeClass("none-dispaly");
    $(".search-result-number0").addClass("none-display");

}

function showSpeciElement(showEleId){
    $("#"+showEleId).removeClass("none-display");
}

function setAvailBenchNum(eleId,num){
    $("#"+eleId).find("span").html(num);
}

function hideSpeciElement(hideEleId){
    $("#"+hideEleId).addClass("none-display");
}

function hideCheckInfo(){
    $("#selectResultShowArea").addClass("none-display");
    $("span.search-result-option-btn.enabled").addClass("none-display");
    $("span.search-result-option-btn.disabled").removeClass("none-display");
    $("#selectResultShowArea").addClass("none-dispaly");
    $(".search-result-number0").removeClass("none-display");
}
function getOperatUsers(){
    $.ajax({
        type:'post',
        dataType:'json',
        url:'./getUser',
        timeout:30000,
        success:function(data){
            var operateUser = '<li nation-id="0">全部</li>';
            var users = data.data;
            for(var iUser = 0; iUser < users.length;iUser++){
                var user = users[iUser];
                operateUser += '<li nation-id="'+user.userId+'">'+user.name+'</li>'
            }

            $("#searchUsers .select_ul").html(operateUser);
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    });
}

function getSelectVoucherOrderIds(){
    var selectOrderIds = '';
    $("div.search-result-area .so-check-box-icon.filter-checked-icon.send-voucher-order").each(function(){
        selectOrderIds += $(this).attr("select-id")+',';
    });
    return selectOrderIds;
}



function getSelectBookingOrderIds(){
    var selectOrderIds = '';
    $("div.search-result-area .so-check-box-icon.filter-checked-icon.send-booking-order").each(function(){
        selectOrderIds += $(this).attr("select-id")+',';
    });

    return selectOrderIds;
}

function bindTipEevet(){
    $(".tip-close-area").bind("click",function(){
        $(this).parent().addClass("none-display");
        $("#orderMask").addClass("none-display");
    });
    $(".reload").bind("click",function(){
        $(this).parent().addClass("none-display");
        $("#orderMask").addClass("none-display");
        location.reload(true);
    });
    $(".close-tip-btn").bind("click",function(){
        $(this).parent().parent().addClass("none-display");
        $("#orderMask").addClass("none-display");
    });
    $(".clear-cancel-event").bind("click",function(){
        clearCancelOrderClick();
    });
    $(".tip-cancel-close-area").bind("click",function(){
        clearCancelOrderClick();
    });


    $(".reload-close-btn").bind("click",function(){
        $(this).parent().parent().addClass("none-display");
        $("#orderMask").addClass("none-display");
        location.reload(true);
    });
    $(".reload-close-btn-skip").bind("click",function(){
        $(this).parent().parent().addClass("none-display");
        $("#orderMask").addClass("none-display");
        location.href = "./mainpage";
    });
    $("#searchOtherRoom").click(function(){
        var orderId = $(this).attr("order-id");
        location.href = "./searchOtherHotel?orderId="+ orderId;
    });
    $("#continueBooking").bind('click', makeOrder);

    $("#highPriceTip .tip-white-btn").bind('click', function () {
        $("#highPriceTip").addClass("none-display");
        $("#orderMask").addClass("none-display");
    });

    $("#policyAndPriceChangeTip .tip-white-btn").bind('click', function () {
        $("#policyAndPriceChangeTip").addClass("none-display");
        $("#orderMask").addClass("none-display");
    });

    $("#continuePolicyBooking").bind('click', makeOrder);
}

function getCancelRefundableDetailInfo(data) {
    var refundableHtml = "";
    if (data != undefined && data!=null && data.length > 0) {
        for (var iCancel = data.length - 1; iCancel >= 0; iCancel--) {
            var policy = data[iCancel];
            var startPolicyTime = null;
            var nowDateTime = new Date();

            var endPolicyTime = convertMiliSecond2DateTimeExcSecondString(policy.startDate);
            if (nowDateTime.getTime() > policy.startDate) {
                continue;
            }

            if (policy.endDate == null) {
                startPolicyTime = "当前时间"
            } else {
                startPolicyTime  = convertMiliSecond2DateTimeExcSecondString(policy.endDate);
            }


            if (policy.policyType=="cancellation") {
                refundableHtml += startPolicyTime + ' 至 ' + endPolicyTime + '   取消费用：' + policy.remark + '<br/>';
            }

        }
    }
    return refundableHtml;
}
function isOrderSendVoucher(orderData){
    if (orderData.customerPayStatus === 2 || orderData.customerPayStatus === '2'){
        return true;
    }
    if (orderData.orderStatus == 120 || orderData.orderStatus == '120'){
        if(orderData.voucherLabel ==='V'){
            return true;
        }
    }

    return false;
}

function isOrderSendBooking(orderData){
    if (orderData.orderStatus == 120 || orderData.orderStatus == '120'){
        return true;
    }
    return false;
}

function setRemarkInfo(){

    var remarkVal = '';
    //获取选择common的特殊要求
    var checkboxes = $(".special-requirement-list i.check-box-icon.filter-checked-icon");
    if(checkboxes.length > 0){
        _hmt.push(['_trackEvent','showBookRoom','special request','-','-']);
    }

    checkboxes.each(function(){
        var thatBox = $(this);
        if(thatBox.hasClass("common")){
            remarkVal += $(this).attr("special-value")+',';
        }
        if(thatBox.hasClass("time-special")){
            var time = $(this).parent().siblings(".select-time-show").find("input").val();

            if(time != ''){
                remarkVal += $(this).attr("special-value") + time+',';
            }
        }

    });

    remarkVal += $("#otherRemark").val();

    $("#specialRequire").val(remarkVal);
    var otherRemarkSt = $("#otherRemark").val();
    if(otherRemarkSt!=undefined && otherRemarkSt!=null &&otherRemarkSt.trim()!='' ){
        _hmt.push(['_trackEvent', 'showBookRoom', 'Others','-']);
    }
}

function getOrderStatusClass(status){
    if(status == 120){
        return 'confirmd';
    }else if(status == 320){
        return 'canceled';
    }else {
        return ''
    }
}
//返回currenyamuount="CNY:124" 这种样式
function getOrderFeeStr(stus,saleprice,operationFee,companyCurrency){
    if(stus == 120){
        return 'currenyamuount="'+companyCurrency+':'+digitForIgnore(saleprice,1)+'"';
    }else if(stus == 320){
        return 'currenyamuount="'+companyCurrency+':'+digitForIgnore(operationFee,1)+'"';
    }else {
        return ''
    }
}

function getOrderStatus(stus){
    if(stus == '已确认' ||  stus == '修改成功'){
        return '<span style="color:#32BB88">'+stus+'</span>';
    }else if(stus == '取消待确认' || stus == '预订待确认' || stus == '修改申请中'){
        return '<span style="color:#F9543C">'+stus+'</span>';
    }else{
        return stus;
    }
}
function getOptionTimeStr(optionTimeStr){
    var nowtime = new Date();
    var optiondate = new Date(optionTimeStr);
    var timelength = 3*3600*24*1000;
    var optionTimeResult = '';

    if(optionTimeStr == undefined || optionTimeStr == null || optionTimeStr == ''){
        optionTimeResult = '';
    }else if((optiondate-nowtime<=timelength)&&(optiondate>=nowtime)){
        var str =  optionTimeStr.split(' ');
        optionTimeResult='<span style="color:#F9543C">'+str[0]+'<br/>';
        if(str.length > 1){
            optionTimeResult += str[1];
        }else{
            optionTimeResult += '00:00:00'
        }
        optionTimeResult +='</span>'
    }else{
        var str =  optionTimeStr.split(' ');
        optionTimeResult = str[0] +'<br/>';
        if(str.length > 1){
            optionTimeResult += str[1];
        }else{
            optionTimeResult += '00:00:00'
        }
    }
    return optionTimeResult;
}
function setHighLightBtnStyle(obj){
    $(".quick-item-sec").removeClass("select");
    obj.addClass("select");
}

function initSearchData(){
    var type = $("#searchOrderType").val();
    initquickNum();
    if (type != ''){
        $("#"+type).click();
    }else{
        var searchType = getCookieValue("detail-show-type");
        if(searchType == "search-group"){
            $("#search-group").addClass("search-type-checked");
            $("")
            queryOrderGroup();
        } else{
            $("#search-formal").addClass("search-type-checked");
            queryOrderSumbit();
        }

    }
}
function initquickNum(){
    $.ajax({
        type:"POST",
        url:"getOrderNum",
        dataType:"json",
        success:function(data){
            var orderData = data.data;
            $(".made-fee-in-week").html(orderData[5]);//七天内取消截止
            $(".made-free-cancel").html(orderData[4]);//可免取消
            $(".made-pay-cancel").html(orderData[3]);//补课免费取消
            $(".not-checkIn").html(orderData[2]);//未入住
            $(".has-edited-in-week").html(orderData[8]);//最近七天修改完成
            $(".except-order-num").html(orderData[1]);
            $(".has-canceled-with-fee").html(orderData[10])
        }
    });
}
function queryQuickOrderSumbit(quickSearchType){
    var sortname=$("#sortname").val();
    var sortflag=$("#sortflag").val();
    var rows = $(".pageSize").val();
    setCookie("detail-show-type","search-formal",24 * 365,"/");
    $.ajax({
        type: "POST",
        url: "./getOrderNumInfo",
        data: {"flag":quickSearchType,"sortname":sortname,"sortflag":sortflag,"rows":rows,},
        dataType: "json",
        timeout:30000,
        success: function (data) {
            $("#search-formal").addClass("search-type-checked");
            $("#search-group").removeClass("search-type-checked");
            if (data == undefined || data == null || data.total == undefined ||
                data == null || data.total == 0){
                $("#searchResultDiv").addClass("none-display");
                $("#noResultShow").removeClass("none-display");
                $("#rowsNum").addClass("none-display");
                $("#soResults tr:gt(0)").remove();
            }else {
                var resultHtml = showOrderQueryResult(data.rows);
                $("#soResults tr:gt(0)").remove();        //移除Id为Result的表格里的行，从第二行开始（这里根据页面布局不同页变）
                $("#soResults").append(resultHtml);
                checkResultEvent();
                $("#searchHotelNum").html(data.total);
                $("#searchHotelNumUnit").html("订单");
                paginationQuickSearchOrder(data.total,quickSearchType);
                $("#searchResultDiv").removeClass("none-display");
                if(data.total>20){
                    $("#rowsNum").removeClass("none-display");
                }else{
                    $("#rowsNum").addClass("none-display");
                }
                $("#noResultShow").addClass("none-display");
            }

        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    })

}

function benchDownloadPdf(fileType,orderIds){
    var orderInput = $("#benchDownloadPdfInfo").find("input[name=\"orderId\"]");
    if (fileType ===null  || fileType === '' || orderIds === ''|| orderIds === null){
        alert("选择订单的数据异常，请刷新网页重试。");
        return;
    }

    $("#bdFileType").val(fileType);
    $("#bdOrderIds").val(orderIds);

    $("#bdPdfForm").submit();
}

function initBookOrderTip(){
    $("#goBoBtn").click(function(){
        $("#standardRoomTip").addClass("none-display");
        $("#loadingTip").removeClass("none-display");
        if($("#isRefundableRoom").val() == 'false'){
            $("#refundableOrderTip .pop-info-content").html($("#cancelPolicyDescripArea").html());
            $("#loadingTip").addClass("none-display");
            $("#refundableOrderTip").removeClass("none-display");
            return;
        }
        if(!checkNameString()) return;
        checkPrice();
    });

    $("#payCancelOrderBtn").click(function(){
        $("#refundableOrderTip").addClass("none-display");
        $("#loadingTip").removeClass("none-display");
        if(!checkNameString()) return;
        checkPrice();
    });

    $("#compatAvailBtn").click(function(){
        $("#compatRoomTip").addClass("none-display");
        $("#loadingTip").removeClass("none-display");

        if($("#isStandardRoom").val() == 'true'){
            $("#loadingTip").addClass("none-display");
            $("#standardRoomTip").removeClass("none-display");
            return;
        }

        if($("#isRefundableRoom").val() == 'false'){
            $("#refundableOrderTip .pop-info-content").html($("#cancelPolicyDescripArea").html());
            $("#loadingTip").addClass("none-display");
            $("#refundableOrderTip").removeClass("none-display");
            return;
        }

        if(!checkNameString()) return;
        checkPrice();
    });

    $("#payCancelResearchBtn").click(function(){
        var searchOrderId = $("#searchId").val();
        location.href = "./searchOtherHotel?orderId="+ searchOrderId;
    });


    $("#bookOrderBtn").click(function(){
        if(!checkNameString()) return;
        $("#orderMask").removeClass("none-display");
        $("#loadTipContent").html("系统正在验证价格，请稍候...");
        $("#loadingTip").removeClass("none-display");

        if($("#isCompatRoomTipShow").val() === 'true'){
            $("#loadingTip").addClass("none-display");
            $("#compatRoomTip").removeClass("none-display");
            return;
        }

        if($("#isStandardRoom").val() == 'true'){
            $("#loadingTip").addClass("none-display");
            $("#standardRoomTip").removeClass("none-display");
            return;
        }

        if($("#isRefundableRoom").val() == 'false'){
            $("#refundableOrderTip .pop-info-content").html($("#cancelPolicyDescripArea").html());
            $("#loadingTip").addClass("none-display");
            $("#refundableOrderTip").removeClass("none-display");
            return;
        }
        checkPrice();
    });
}
function getHistoryContactInfo(){
    var cpName = getCookieValue('contact_person_name');
    var cpPhone = getCookieValue('contact_person_phone');
    var cpEmail = getCookieValue('contact_person_email');

    if(cpName){
        $("#operator").val(cpName);
    }

    if(cpPhone){
        $("#operatorMobile").val(cpPhone);
    }

    if(cpEmail){
        $("#operatorEmail").val(cpEmail);
    }
}

/*
 *点击立即支付按钮
 * created by wangjie on 2016.9.24
 **/
$(document).on("click",".paymentOrder",function(){
    var go=false;
    var orderId = $(this).attr("orderid");
    $.ajaxSettings.async = false; //请求前设置为同步请求
    $.ajax({
        type: "POST",
        url: "./createChargePayOnline",
        timeout: 30000,
        data: {orderId: orderId,flag:1},
        dataType: "json",
        success: function (data) {
            $.ajaxSettings.async = true;//切记，请求完成之后要设置回异步
            console.log("data.resCode:"+data.resCode);
            if (data.resCode == "100"){
                //支付剩余时间
                if(data.msg == '0:0'){
                    //chargeID过期
                    $("#orderMask").removeClass("none-display");
                    $("#orderPayment").html("订单已超时，请您重新预订");
                    $("#orderPaymentTip").removeClass("none-display");
                }else {
                    go =true;
                }
            }else if(data.resCode == "400"){
                go = true;
            }else if(data.resCode == "200"){
                $("#orderMask").removeClass("none-display");
                $("#orderPayment").html("该订单正在支付中，请稍后再支付");
                $("#orderPaymentTip").removeClass("none-display");
            }else if(data.resCode == "300"){
                $("#orderMask").removeClass("none-display");
                $("#orderPayment").html("该订单不需要支付和退款");
                $("#orderPaymentTip").removeClass("none-display");
            }else if(data.resCode == "500"){
                $("#orderMask").removeClass("none-display");
                $("#orderPayment").html("系统错误，请重试");
                $("#orderPaymentTip").removeClass("none-display")
            }else if(data.resCode == "800"){
                $("#orderMask").removeClass("none-display");
                $("#orderPaymentSkip").html("订单已超时，请您重新预订");
                $("#orderPaymentTipSkip").removeClass("none-display")
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $.ajaxSettings.async = true;//切记，请求完成之后要设置回异步
            if (textStatus == 'timeout') {
                //todo note log
            }
        }
    });  return go;
})

/*
 *弹开新窗口，避免浏览器拦截
 * created by wangjie on 2016.11.10
 **/
function openWindow(orderId){
    window.open("./onlinePaymentAfterBook?orderId=" + orderId  + "&flag=second");
}
/*
 *点击查看详情按钮
 * created by wangjie on 2016.9.24
 **/
function orderDetail(orderId){
    window.open("./orderDetail?orderId="+orderId);
}

//0舍1入算法
function digitForIgnore(digit,count)
{
    if(digit == null){
        digit = "0";
    }
    digit=digit.toString();
    var str="";
    if(digit.indexOf(".")!=-1){
        var n=digit.split(".");
        var b=n[1];
        for(var i=0;i<count;i++){
            b+="0";
        }
        str=b.substring(0,count);

        if(b.substring(count,count+1)!=0){
            if(b.substring(0,count)==9){
                str = "0";
                 n[0]++;
            }else{
                str++;
            }
        }
        str=n[0]+"."+str;
    }else{
        for(var i=1;i<=count;i++){
            str+="0";
        }
        str=digit+"."+str;
    }
    return str;
}

function textareaLimitWords(){
    $("#otherRemark").keyup(function(){
        var len = $(this).val().length;
        if(len > 200){
            $(this).val($(this).val().substring(0,200));
        }
    });
}
function changeGroupId(){
    $("#iread").addClass("none-display");
    $("#iwrite").removeClass("none-display");
}
function resetChangBtn(){
    $("#iread").removeClass("none-display");
    $("#iwrite").addClass("none-display");
}
function updateGroupId(){
    var newGroupId = $("#orderGourpIdWrite").val();
    var orderId = $("#orderCode").html();
    $.ajax({
        type:"POST",
        url:"./updateGroupId",
        timeout:60000,
        data:{"companyGroupCode":newGroupId,"orderId":orderId},
        dataType:"json",
        success: function (data) {

            if(data.resCode == "00000000") {
                $("#orderGourpId").html(newGroupId);
                resetChangBtn();
            }else {
                alert("修改失败");
                resetChangBtn();
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            alert("修改失败");
            resetChangBtn();
        }
    });

}
function showPolicyChanged(data,checkInDate){
    var refundableHtml = '<h2><b>取消政策</b></h2><p>';
    if (data.policies == undefined || data.policies == null){
        refundableHtml += '不可取消、不可修改。<br/>如果取消预订或未如期入住，酒店将收取全额费用<br/>';
    }else {
        refundableHtml += getRefundableDetailInfo(data,checkInDate);
    }
    refundableHtml +='</p>';
    refundableHtml += getOtherPolicies(data);
    return refundableHtml;
}


function initModifyBtn(){
    //切换tab
    $(".modify-tabs").parent().bind("click", function () {
        var $thistabs = $(this).children();
        $(".modify-ch").removeClass("modify-ch").addClass("modify-un");
        $thistabs.parent().removeClass("modify-un").addClass("modify-ch");
        $(".modify-tabs").addClass("modify-border");
        $thistabs.removeClass("modify-border");
        $thistabs.parent().nextAll(":not(.none-display):first").children().removeClass("modify-border");
        $(".modify-det").addClass("none-display");
        var tabname = $thistabs.attr("change-type");
        $("."+tabname).removeClass("none-display");
    });
    $(".modify-cancelTip").bind("click",function(){
        $("#orderMask").addClass("none-display");
        $(".order-Modify-Tip").addClass("none-display")
    });
    $(".selectAgree").bind("click",function(){
        if($(this).hasClass("filter-checked-icon")){
            $(this).removeClass("filter-checked-icon");
        }else{
            $(this).addClass("filter-checked-icon");
        }
    })

}

function checkNameforModify() {
    var emptyName = '<div class="book-invalid-name-msg-area" ><i class="warning-orange-item"></i><span class="important" >请填写客人姓名</span></div>';
    var invalidName = '<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important " >姓和名需要用空格隔开</div>';
    var lastNameNotOneWord = '<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >姓(LastName)字符长度需要最少2位</div>';
    var firstNameNotOneWord = '<div class="book-invalid-name-msg-area"><i class="warning-orange-item"></i><span class="important" >名(FirstName)字符长度需要最少2位</div>';


    $(".book-invalid-name-msg-area").remove();
    var isOk = true;
    var ischanged = false;
    var travels = $(".input-cust-name").each(function () {
        var val = $(this).val();

        val = val.replace(/(^\s*)|(\s*$)/g, "");
        $(this).val(val);
        if (val == "") {
            isOk = false;
            $(this).after(emptyName);
            $(this).addClass("border-highlight");
            return isOk;
        }
        if(val != $(this).attr("defaultVa")){
            ischanged=true;
        }
        var index = val.indexOf(" ");
        if (index < 0) {
            isOk = false;
            $(this).after(invalidName);
            $(this).addClass("border-highlight");
            return isOk;
        }
        if (index == 1) {
            isOk = false;
            $(this).after(lastNameNotOneWord);
            $(this).addClass("border-highlight");
            return isOk;
        }

        var lastIndex = val.lastIndexOf(" ");
        if (lastIndex == val.length - 2) {
            isOk = false;
            $(this).after(firstNameNotOneWord);
            $(this).addClass("border-highlight");
            return isOk;
        }
    });
    if(!ischanged){
        isOk = false;
        $("#orderModifyTip").addClass("zindex9999");
        $("#inputUnchangdTip").removeClass("none-display");
    }

    return isOk;
}
function checkoperator(){
    var isOk = true;
    //检查填写人信息,电话，姓名和邮箱
    var contactPersonName = $("#change-operator").val().trim();
    var contactPersonPhone = $("#change-operator-mobile").val().trim();
    var contactPersonEmail = $("#change-operator-mail").val().trim();
    if(!contactPersonName){
        $("#change-operator").addClass("border-highlight");
        isOk = false;
        $("#change-operatorMsg").addClass("important").html('请填写您本人姓名');
    }else{
        $("#change-operator").removeClass("border-highlight");
        $("#change-operatorMsg").html('');
    }

    if(!contactPersonPhone){
        $("#change-operator-mobile").addClass("border-highlight");
        $("#change-operator-mobileMsg").addClass("important").html('请填写您本人电话');
        isOk = false;
    }else{
        $("#change-operator-mobile").removeClass("border-highlight");
        $("#change-operator-mobileMsg").removeClass("important").html('订单发生异常时与您紧急联系');
    }

    if(!contactPersonEmail){
        $("#change-operator-mail").addClass("border-highlight");
        $("#change-operator-mailMsg").addClass("important").html('请填写您本人邮箱');
        isOk = false;
    }else{
        if(!checkEmail(contactPersonEmail)){
            $("#change-operator-mail").addClass("border-highlight");
            $("#change-operator-mailMsg").addClass("important").html('邮箱格式不正确');
            isOk = false;
        }else{
            $("#change-operator-mail").removeClass("border-highlight");
            $("#change-operator-mailMsg").removeClass("important").html('接收订单确认、临近取消日提醒等邮件通知');
        }
    };
    var ischanged = false;
    $(".ch-operator").each(function () {
        var val = $(this).val();
        if(val!=$(this).attr("defaultVa")){
            ischanged=true;
        }
    });
    if(!ischanged){
        isOk = false;
        $("#orderModifyTip").addClass("zindex9999");
        $("#inputUnchangdTip").removeClass("none-display");
    }
    return isOk;
}
function checkroomNum(){
    var isOk = true;

    if($(".customRoomNum").size()==$("#customRoomNum").html()){
        isOk = false;
        $("#orderModifyTip").addClass("zindex9999");
        $("#inputUnchangdTip").removeClass("none-display");
    }
    return isOk;
}
function checkcheckinDate(){
    var isOk = true;
    if($("#minusDay").html()==$("#minusDaydefault").html()){
        isOk = false;
        $("#orderModifyTip").addClass("zindex9999");
        $("#inputUnchangdTip").removeClass("none-display");
    }
    return isOk;
}




function submitModify(obj,modifyType) {
    if(amendOrderFlag==false){
        return;
    }else{
        amendOrderFlag = false;
    }
    var thisurl = obj.attr('action');
    $("#loadTipContent").html("系统正在处理您的取消请求，请稍候...");
    $("#orderModifyTip").addClass("zindex9999");
    $("#loadingTip").removeClass("none-display");

    $.ajax({
        type: "POST",
        url: thisurl,
        timeout: 30000,
        data: obj.serialize(),
        dataType: "json",
        success: function (data) {
            if (data.resCode == "00000000") {
                $("#loadingTip").addClass("none-display");
                if(modifyType=="groupCode"){
                    $("#tipModifySuccessTilte").html("修改成功");
                }else{
                    $("#tipModifySuccessTilte").html("修改申请已提交，请等待蘑菇客服处理");
                }
                $("#ModifySuccess").removeClass("none-display");
            } else {
                $("#loadingTip").addClass("none-display");
                $("#ModifyFileTitle").html("申请修改失败");
                $("#ModifyFile").removeClass("none-display");
                amendOrderFlag = true;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("#loadingTip").addClass("none-display");
            $("#ModifyFileTitle").html("申请修改失败");
            $("#ModifyFile").removeClass("none-display");
            amendOrderFlag = true;
        }
    });
}
function bindSameNameJudgeEvet(){
    $(".travel-name").blur(judgeSameName);
}


function judgeSameName(){
    var limitSameName = validator.messages.limitSameName;
    var travelerNames = $(".travel-name");
    var travelerMap = {};
    for (var i = 0; i < travelerNames.length;i++){
        var travelerName = $(travelerNames[i]);
        var curTravellerName = travelerName.val().trim();

        if(curTravellerName){
            if(travelerMap[curTravellerName]){
                if(!travelerName.hasClass("border-highlight")){
                    travelerName.after(limitSameName);
                    travelerName.addClass("border-highlight");
                }
            }else{
                travelerName.parent(".traveller-name-select").children(".same-name-msg").remove();
                travelerName.removeClass("border-highlight");
            }

            travelerMap[curTravellerName] = true;
        }
    }
}

function bookNoRoom(orderId,searchId){

    $("#nestGroupReminder").removeClass("none-dispaly");
    $.ajax({
        url:"/dealwithNoRoom?orderId="+orderId+"&searchId="+searchId,
        dataType:"json",
        success:function(data){
            if(data.resCode =="00001"){
                $("#nestGroupReminder").addClass("none-dispaly");
            }else{
                $("#noRoomGroupId").html(data.data);
                $("#nestGroupReminder").removeClass("none-display");
            }
        },
        error:function(){
            $("#nestGroupReminder").addClass("none-dispaly");
        }

    });
    $("#NoRoomOrderTip").removeClass("none-display");
}

function getNoRoomTraveller(orderId){
    _hmt.push(['_trackEvent', 'showBookRoom', 'getGroupCode', '-']);//添加百度统计
    $.ajax({
        type:"POST",
        data:{'orderId':orderId},
        dataType:'json',
        timeout:30000,
        url:'getTravellerByOrderId',
        success:function(data){
            if(data!=null && data!=''){
                setTravellerName(data)
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            if(textStatus == 'timeout'){
                //todo note log
            }
        }
    });
}

function setTravellerName(data){
    var travellerHtml = '';
    var travelInputs = $(".travel-name");

    var travellers = data.data;
    if(travellers.length>0){
        $("#historyGroupInput").val(travellers[0].companyGroupId);
    }
    for(var iTravel = 0; iTravel < travellers.length;iTravel++){
        var traveller = travellers[iTravel];

        if(travelInputs.size() > iTravel){
            travelInputs[iTravel].value = traveller.lastName+' '+traveller.firstName;
        }
        travellerHtml += '<li nation-id="'+traveller.lastName+' '+traveller.firstName+'">'+traveller.lastName+' '+traveller.firstName+'</li>'
    }

    $(".room-people-detail .traveller-name-select .select_ul").html(travellerHtml);
    $(".room-people-detail .traveller-name-select .traveller-select-icon-div").removeClass("none-display");
    $("#noRoomGroupCode").unbind('click');
    $("#noRoomGroupCode").attr("onclick",'');
    $("#noRoomGroupCode").click(function(){
        clearTravellerName(data);
    });
}

function clearTravellerName(data){
//清空团号和姓名输入框
    $("#historyGroupInput").val("");
    $(".travel-name").val("");
    $(".room-people-detail .traveller-name-select .select_ul").html("");
    $(".room-people-detail .traveller-name-select .traveller-select-icon-div").addClass("none-display");
    $("#noRoomGroupCode").unbind('click');
    $("#noRoomGroupCode").click(function(){
        setTravellerName(data);
    });
}