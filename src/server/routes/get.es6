'use strict';

var LoginService = require('./../services/login-service');

module.exports = {

    index (req, res) {
        res.send("Hello");
    },

    users (req, res) {
        //var id = req.params.id;

        res.send({
            id:req.params.id,
            username:"nathan"
        });
    },

    login (req, res) {

        var username = req.query.username;
        var password = req.query.password;

        var valid = LoginService.isValidUser(username, password);

        res.send(valid);
    }
};

