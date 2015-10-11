'use strict';

app.factory('Alert', function ($timeout, $rootScope) {


    var active = false,
        message = false,
        colour = "primary",
        timeout;

    var showMessage = (msg) => {
        showError(msg);
        colour = "primary";
    };

    var showError = (msg) => {
        console.log('hello')
        colour = "red";
        setActive(true);
        message = msg;
        $timeout.cancel(timeout);
        timeout = $timeout(() => setActive(false), 2000);
    };

    var getColour = () => {
        return colour;
    };

    var getMessage = () => {
        return message;
    };

    var getActive = () => {
        return active;
    };

    var setActive = (flag) => {
        active = flag;
    };

    var switchActive = () => {
        active = !active;
    };

    var init = () => {

    };

    init();

    $rootScope.Alert = {
        showMessage: showMessage,
        showError: showError,
        getMessage: getMessage,
        getColour: getColour,
        getActive: getActive,
        setActive: setActive,
        switchActive: switchActive
    };

    return {
        showMessage: showMessage,
        showError: showError,
        getMessage: getMessage,
        getColour: getColour,
        getActive: getActive,
        setActive: setActive,
        switchActive: switchActive
    };
});