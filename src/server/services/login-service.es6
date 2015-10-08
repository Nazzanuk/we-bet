'use strict';

module.exports = {

    isValidUser (username, password) {
        var valid = false;

        valid = (username == "nathan" && password == "nathan");

        return valid;
    }
};
