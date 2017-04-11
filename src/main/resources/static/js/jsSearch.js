/**
 * Created by Igbalajobi Jamiu on 4/11/17.
 */
(function () {
    'use strict';

    var typingTimer, doneTypingInterval = 1000, nationalitySearchDOM = $("#nationalitySearch");
    nationalitySearchDOM.on('keyup paste', function () {
        typingTimer = setTimeout(searchNationality, doneTypingInterval);
    });

    nationalitySearchDOM.on('keydown', function () {
        clearTimeout(typingTimer);
    });

    function searchNationality() {
        var $this = nationalitySearchDOM;
        if ($this.val().length >= 2) {
            $.ajax({
                url: '/api/suggest/nationality',
                method: 'GET',
                data: {
                    search: $this.val()
                },
                beforeSend: function () {
                    $(".fa-spinner").remove();
                    // $('<span class="fa fa-spinner fa-spin"></span>').appendTo($this);
                    $("#ul-nationality").html('');
                },
                success: function (resp, status, xhr) {
                    var dpDown = $('#nationality-dropdown');
                    dpDown.removeClass('hide');
                    $("#ul-nationality").html('');
                    if (xhr.status === 200 && resp.length > 0) {
                        var active = "";
                        $.each(resp, function (i, obj) {
                            // active = i === 0 ? "active" : "";
                            var dpListHtml = "<li data-nationalityId=\"" + obj.id + "\" class=\"selectNationality\">" + obj.nationality + "</li>";
                            $(dpListHtml).appendTo($("#ul-nationality"));
                        });
                        selectNationalityXhr();
                    } else {
                        $("<li>No nationality found for " + $this.val() + "</li>").appendTo($("#ul-nationality"));
                    }
                }
            })
        }
    }

    function selectNationalityXhr() {
        $(".selectNationality").on('click', function () {
            var nationalityId = $(this).attr('data-nationalityId');
            $('input[name=nationalityId]').attr('value', nationalityId);
            var selectedName = $(this).html();
            $("#nationalitySearch").attr('value', selectedName);
        });
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