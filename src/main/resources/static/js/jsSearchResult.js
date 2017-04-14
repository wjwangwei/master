/**
 * Created by Igbalajobi Jamiu O on 4/13/17.
 */
(function () {
    //Hide Hotel Availability if greater than 2
    // $(".room-avail").find(".item-option:gt(2)")
    //     .hide().end();

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
        $this.addClass('disabled');
        $this.html('loading...');
        $.ajax({
            url: HOTEL_ROOM_VERIFICATION_API,
            method: '',
            beforeSend: function () {
                $.growl.notice({
                    title: "Verifying Availability",
                    message: "We're verifying the availability of this room...",
                    duration: 15000
                });
            },
            success: function (response) {

            }
        })
    });
})();

