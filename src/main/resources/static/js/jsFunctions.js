/**
 * Created by Igbalajobi Jamiu on 4/11/17.
 */

function displayCurrency(currencyCode, dom) {
    console.log(currencyCode);
    var displayCode = currencyCode;
    switch (currencyCode) {
        case "USD":
            displayCode = "$";
            break;
    }
    dom.style = 'content: ' + displayCode;
}

(function () {
    $.fn.queryHotelXhr = function (url, data, btn, isHotel, funcResponse) {
        // (function () {
        $.ajax({
            url: url,
            data: data,
            dataType: 'JSON',
            type: 'GET',
            beforeSend: function () {
                btn.addClass('disabled');
            },
            success: function (response) {
                if (response.rewriteKeyCount <= response.completeRewriteKeyCount) {
                    funcResponse(response)
                } else
                    return $(this).queryHotelXhr(url, data, btn, isHotel, funcResponse);
            },
            complete: function () {
                btn.removeClass('disabled');
            },
            error: function () {
                NProgress.done();
                return false;
            }
        });
        // })()
    };
})();

function xhrRoomVerifyURL(hotelId, url) {
    NProgress.start();
    (function () {
        $(this).queryHotelXhr(HOTEL_ROOM_VERIFICATION_API + "/" + hotelId, null, $(this), true, function (response) {
            if (response.hotelCount > 0) {
                window.location.href = url;
            }
        });
        return false;
    })(jQuery);
}

function urlRedirect(url, urlParams) {
    return window.location.href = url + urlParams;
}