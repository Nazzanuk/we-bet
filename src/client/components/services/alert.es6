'use strict';

app.factory('Alert', function ($timeout) {

    var active = false,
        message = false,
        colour = "primary",
        timeout;

    var showMessage = (msg) => {
        showError(msg);
        colour = "primary";
    };

    var showError = (msg) => {
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

    return {
        showMessage: showMessage,
        showError: showError,
        getMessage: getMessage,
        getColour: getColour,
        getActive: getActive,
        setActive: setActive,
        switchActive: switchActive
    }
});