$(function () {

    $("#username_error_message").hide();
    $("#password_error_message").hide();
    $("#retype_password_error_message").hide();

    var error_username = false;
    var error_password = false;
    var error_retype_password = false;


    $("#form_password").focusout(function () {

        check_password();

    });

    $("#form_retype_password").focusout(function () {

        check_retype_password();

    });


    function check_password() {

        var password_length = $("#form_password").val().length;

        if (password_length < 8) {
            $("#password_error_message").html(" 8 chars");
            $("#password_error_message").show();
            error_password = true;
        } else {
            $("#password_error_message").hide();
        }

    }

    function check_retype_password() {

        var password = $("#form_password").val();
        var retype_password = $("#form_retype_password").val();

        if (password !=  retype_password) {
            $("#retype_password_error_message").html(" Non valide");
            $("#retype_password_error_message").show();
            error_retype_password = true;
        } else {
            $("#retype_password_error_message").hide();
        }

    }



});
