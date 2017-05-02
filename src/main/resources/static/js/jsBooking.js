
function inputValToUpperCase(ctrl) {
    ctrl.value = ctrl.value.toUpperCase();
};

(function () {
    'use strict';

    $('#bookNowBTN').on("click", function () {
        var specialOptArr = [];
        var specialRequestText = "";

        $('input[name="specialOpt"]').each(function() {
            if ($(this).get(0).checked) {
                specialOptArr.push($(this).val());
            }
        });

        if (specialOptArr.length > 0) {
            specialRequestText += specialOptArr;
        }

        var optionVal = $("#optionTEXT").val();
        if (optionVal.length > 0) {
            if (specialRequestText.length > 0) {
                specialRequestText += ",";
            }
            specialRequestText += optionVal;
        }

        var msg;
        var errorColor = '#d9534f';
        if (specialRequestText.length > 254) {
            msg = "Special Options is too long";
            $("#optionTEXT").css({'border-color': '1px solid ' + errorColor});
            $.growl.error({
                title: "Text of special request is too long",
                message: msg
            })
            return false;
        }

        var firstNameArr = [];
        var lastNameArr = [];
        $('input[name="firstname"]').each(function() {
            firstNameArr.push($(this).val());
        });
        $('input[name="lastname"]').each(function() {
            lastNameArr.push($(this).val());
        });
        var queryId = $('#queryId').val();
        var hotelId = $('#hotelId').val();
        var targetIndex = $('#targetIndex').val();
        console.log("special request text:", specialRequestText);
        console.log("firstname array:", firstNameArr);
        console.log("lastname array:", lastNameArr);
        console.log("queryid:", queryId);
        console.log("targetindex:", targetIndex);
        var data = "firstname=" + firstNameArr + "&lastname=" + lastNameArr + "&queryid=" + queryId + "&hotelid=" + hotelId + "&targetindex=" + targetIndex;
        var btn = $(this).find('button[type=submit]');
        $(this).queryHotelXhr(HOTEL_BOOKING_API, data, btn, false, function (response) {
            console.log(response);
            /*
            var queryId = response.queryId;
            window.location.href = "/hotel/search-result?queryId=" + queryId + "&" + data;
            */
        });
        return true;
    });
})();


