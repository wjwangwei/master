/**
 * Created by Igbalajobi Jamiu on 4/11/17.
 */
(function () {
    'use strict';

    var typingTimer, doneTypingInterval = 3000, nationalitySearchDOM = $("#nationalitySearch");
    nationalitySearchDOM.on('keyup paste', function () {
        typingTimer = setTimeout(searchNationality, doneTypingInterval);
    });

    nationalitySearchDOM.on('keydown', function () {
        clearTimeout(typingTimer);
    });

    function searchNationality() {
        var $this = nationalitySearchDOM;
        $.ajax({
            url: '/api/suggest/nationality',
            method: 'GET',
            data: {
                search: $this.val()
            },
            beforeSend: function () {
                // $('<span class="fa fa-spinner fa-spin"></span>').appendTo($this);
            },
            success: function (resp) {

            }
        })
    }

    function customRange(dates) {
        if (this.id == 'dateCheckin') {
            $('#dateCheckout').datepick('option', 'minDate', dates[0] || null);
        } else {
            $('#dateCheckin').datepick('option', 'maxDate', dates[0] || null);
        }
    }

    $('#dateCheckin,#dateCheckout').on('focus', function () {
        $(this).closest('.has-icon').addClass('dropdown-open');
        $('.dropdown-room').removeClass('open');
    });

    $('#dateCheckin,#dateCheckout').datepick({
        alignment: 'top',
        onSelect: customRange,
        minDate: $.datepick.today(),
        monthsToShow: 2,
        changeMonth: false,
        popupContainer: '.calendardaten',
        nextText: '',
        prevText: '',
        showAnim: '',
        onClose: function () {
            $('.has-icon').removeClass('dropdown-open');
        }
    });

    $("form[name=hotelSearch]").on('submit', function (e) {
        e.preventDefault();


    });

})();