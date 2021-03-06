'use strict';

var LoginService = require('./../services/login-service');
var MongoService = require('./../services/mongo-service');

module.exports = {

    index(req, res) {
        res.send("Hello");
    },

    users(req, res) {
        res.send({
            _id: req.params._id,
            username: "nathan"
        });
    },

    groups(req, res) {
        var params = {};
        if (req.params) params = req.params;

        MongoService.find('groups', params, {}, (response) => {
            res.send(response);
        });
    },

    login(req, res) {
        var username = req.query.username;
        var password = req.query.password;

        var valid = LoginService.isValidUser(username, password);

        res.send(valid);
    }
};

