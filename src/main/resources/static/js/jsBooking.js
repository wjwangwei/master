
function inputValToUpperCase(ctrl) {
    ctrl.value = ctrl.value.toUpperCase();
};

(function () {
    'use strict';

    $('#bookNowBTN').on("click", function () {
        var specialOptArr = [];
        var data = "";

        $('input[name="specialOpt"]').each(function() {
            if ($(this).get(0).checked) {
                specialOptArr.push($(this).val());
            }
        });

        if (specialOptArr.length > 0) {
            data += specialOptArr;
        }

        var optionVal = $("#optionTEXT").val();
        if (optionVal.length > 0) {
            if (data.length > 0) {
                data += ",";
            }
            data += optionVal;
        }

        var msg;
        var errorColor = '#d9534f';
        if (data.length > 254) {
            msg = "Special Options is too long";
            $("#optionTEXT").css({'border-color': '1px solid ' + errorColor});
            $.growl.error({
                title: "Field long",
                message: msg
            })
            return false;
        }

        return true;
    });
})();


