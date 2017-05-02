(function () {
    'use strict';

    var msg = "";

    function trim(str) { //删除左右两端的空格
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }

    function signInInputValidation() {
        var isValid = false;
        var errorColor = '#d9534f';

        var username = $("#username");
        var password = $("#password");
        if (trim(username.val()).length == 0) {
            msg = "Username is required";
            username.css({'border-color': '1px solid ' + errorColor});
            isValid = false;
        } else if (password.val().length == 0) {
            msg = "Password is required";
            password.css({'border-color': '1px solid ' + errorColor});
            isValid = false;
        } else {
            isValid = true;
            msg = "";
        }

        return isValid;
    }

    $("form[name=signInForm]").on('submit', function (e) {
        if (!signInInputValidation()) {
            $.growl.error({
                title: "Field Required",
                message: msg
            })
            return false;
        }

        var username = $("#username");
        var password = $("#password");
        if ($("#rememberUsername").get(0).checked) {
            $.cookie("username", trim(username.val()), { expires: 30 });
        } else {
            $.cookie("username", "");
        }

        return true;
    });

    //读取cookie值
    var username = $.cookie("username");
    $("#username").val(username);

})();