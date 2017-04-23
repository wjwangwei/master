/**
 * Created by Igbalajobi Jamiu O. on 4/13/17.
 */
NProgress.configure({minimum: 0.1});
var MAX_XHR_RETRY = 3;
var HOTEL_SEARCH_API = "/api/hotel/search";
var HOTEL_ROOM_VERIFICATION_API = "/api/hotel/availability";


(function () {
    var cookieName = "c_currency",
        activeCurrency = "cny"; //by default
    if (getCookie(cookieName) !== "")
        activeCurrency = getCookie(cookieName);
    $("#active-currency-code").html(getCurrencySymbol(activeCurrency) + " &nbsp;");
    $("#active-currency-name").html(activeCurrency.toUpperCase() + " &nbsp;");

    $('.change-currency').click(function () {
        setCookie(cookieName, $(this).attr('data-currency'));
        window.location.reload();
        return false;
    });
})(jQuery);