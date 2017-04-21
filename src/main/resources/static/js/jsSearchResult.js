/**
 * Created by Igbalajobi Jamiu O on 4/13/17.
 */
$(function () {
    //Hide Hotel Availability if greater than 2
    // $(".room-avail").find(".item-option:gt(2)")
    //     .hide().end();


    $.each($(".room-avail"), function (o, roomAvail) {
        $.each($(roomAvail).find('.item-option'), function (i, obj) {
            if (i >= 2) {
                $(obj).hide();
            }
        });
    });

    var isDisplayed = false;
    $(".seeall").on('click', function () {

    });

    $(".get-policy").on('click', function (e) {
        e.preventDefault();
        var policyId = $(this).data('roomid');
        var ajaxTask = $.ajax({
            url: ''
        });
    });

    $(".booknow").on('click', function (e) {
        e.preventDefault();
        var $this = $(this);
        $this.html('<span class="fa fa-spinner fa-spin"></span>');
        $.growl.warning({
            // title: "checking availability",
            message: "We're verifying the availability of this room...please wait",
            duration: 15000
        });
        $(this).queryHotelXhr   (HOTEL_ROOM_VERIFICATION_API + "/" + $(this).attr('data-hotelid'), null, $this, true, function (response) {
            if (response.hotelCount <= 0) {
                $this.html("Sold Out");
                $this.addClass('disabled');
                $("#no-room-modal").modal('show');
            } else {
                //Proceed
                window.location.href = '/hotel/booking/' + $this.attr('data-hotelid') + "/" + $this.attr('data-roomcode');
            }
        });
    });

    /**
     * Filter and Sorting of Results
     *
     */
    $('input[name=filtername]').on('keyup paste', function () {
        var filterVal = $(this).val();
        $.each($("#main").find(".items").find('li,.item'), function (i, obj) {
            var itemName = $(obj).attr('data-hotelname');
            if (itemName.toLocaleLowerCase().indexOf(filterVal) === -1)
                $(obj).hide();
            else
                $(obj).show();

        });
    });

    $("input[name=rating]").click(function () {
        var checkRatings = [];
        var cItem = $(this).attr('data-starrating');
        $.each($("input[name=rating]"), function (i, obj) {
            if ($(obj).prop('checked') === true)
                checkRatings.push($(obj).val());
            else {
                checkRatings = jQuery.grep(checkRatings, function (v) {
                    return v != cItem;
                })
            }
        });
        var $this = $(this);
        $.each($("#main").find(".items").find('.item'), function (i, obj) {
            if ($this.prop('checked') === true) {
                if (jQuery.inArray($(obj).attr('data-starrating'), checkRatings) !== -1 || checkRatings.length <= 0)
                    $(obj).show();
                else {
                    $(obj).hide();
                }
            } else {
                $(obj).show();
            }
        });
    });


    $(".xhr-sort-pricerate").click(function () {
        NProgress.start();
        var order = $(this).attr('data-sortprice');
        var itemWrapper = $("#main").find(".items");
        itemWrapper.find('li,.item').sort(function (a, b) {
            console.log(a, b);
            if (order === 'decend') {
                return a.getAttribute("data-lowestrate") - b.getAttribute("data-lowestrate");
            } else {
                return a.getAttribute("data-lowestrate") - b.getAttribute("data-lowestrate");
            }
        }).appendTo(itemWrapper);
        NProgress.done();
        $('.items').click();
        return false;
    });

});

