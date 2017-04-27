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

var xhrCount = 1;
var xhrProcessArray = [0, 1, 1, 1, 2, 2, 2, 4, 4, 4, 8, 8, 8, 8, 8];
var lastHotelCount;
(function () {
    $.fn.queryHotelXhr = function (url, data, btn, isHotel, funcResponse) {
        // (function () {
        console.log("timeout: " + xhrProcessArray[xhrCount] * 1000);
        $.ajax({
            url: url,
            data: data,
            dataType: 'JSON',
            timeout: xhrProcessArray[xhrCount] * 1000,
            type: 'GET',
            beforeSend: function () {
                btn.addClass('disabled');
            },
            success: function (response) {
                console.log(xhrCount >= MAX_XHR_RETRY);
                if (response.rewriteKeyCount <= response.completeRewriteKeyCount || xhrCount >= MAX_XHR_RETRY) {
                    return funcResponse(response);
                } else {
                    return $(this).queryHotelXhr(url, data, btn, isHotel, funcResponse);
                }
                // else if (lastHotelCount !== response.hotelCount) {
                //     lastHotelCount = response.hotelCount;
                //     return $(this).queryHotelXhr(url, data, btn, isHotel, funcResponse);
                // }
                // funcResponse(response)
            },
            complete: function () {
                xhrCount = xhrCount + 1;
                btn.removeClass('disabled');
            },
            error: function (error) {
                if (xhrCount < MAX_XHR_RETRY) {
                    return $(this).queryHotelXhr(url, data, btn, isHotel, funcResponse);
                } else {
                    xhrCount = 1;
                    NProgress.done();
                    $.growl.error({
                        title: "Request failed",
                        message: 'Sorry, your request failed. Please try again later.'
                    });
                    return false;
                }
            }
        });
        // })()
    };

    $.fn.queryHotelXhrPost = function (url, data, btn, funcResponse) {
        // (function () {
        $.ajax({
            url: url,
            data: data,
            dataType: 'JSON',
            contentType: 'application/json',
            timeout: 2000,
            type: 'POST',
            beforeSend: function () {
                btn.addClass('disabled');
            },
            success: function (response) {
                console.log(response);
                funcResponse(response);

            },
            complete: function () {
                btn.removeClass('disabled');
            },
            error: function () {
                NProgress.done();
                $.growl.error({
                    title: "Request failed",
                    message: 'Sorry, your request failed. Please try again later.'
                });
                return false;
            }
        });
        // })()
    };
})();

function xhrRoomVerifyHref(hotelId) {
    NProgress.start();
    (function () {
        hotelId = (hotelId === 'undefined' || hotelId === '') ? $(this).attr('data-hotelid') : hotelId;
        $(this).queryHotelXhr(HOTEL_ROOM_VERIFICATION_API + "/" + hotelId, {hotelId: hotelId}, $(this), true, function (response) {
            console.log(hotelId);
            if (response.hotelCount > 0) {
                window.location.href = '/hotel/' + hotelId;
            }
        });
        return false;
    })(jQuery);
}

function paginationHref(pageId) {
    NProgress.start();
    (function () {
        var param = window.location.search;
        if (param.indexOf('page=') !== -1) {
            param = param.split('&page=')[0] + "&page=" + pageId;
        } else {
            param = param + "&page=" + pageId;
        }
        console.log(param, param.indexOf('page='));
        $(this).queryHotelXhr(HOTEL_SEARCH_API, param, $(this), false, function (response) {
            if (response.hotelCount > 0) {
                window.location.href = '/hotel/search-result' + param;
            }
        });
        return false;
    })(jQuery);
}


function urlRedirect(url, urlParams) {
    return window.location.href = url + urlParams;
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function getCurrencySymbol(currencyCode) {
    switch (currencyCode.toLowerCase()) {
        case "cny":
            return "&yen;";
            break;
        case "usd":
            return "$";
            break;
        case "eur":
            return "&euro;";
            break;
        case "gbp":
            return "&pound;";
            break;
        case "jpy":
            return "&yen;";
            break;
        case "hkd":
            return "HK$";
            break;
        default:
            return currencyCode;
    }
}