'use strict';

var LoginService = require('./../services/login-service');

module.exports = {

    login (req, res) {
        //console.log('/post/login')
        var username = req.body.username;
        var password = req.body.password;

        var valid = LoginService.isValidUser(username, password);

        res.send(valid);
    }
};

