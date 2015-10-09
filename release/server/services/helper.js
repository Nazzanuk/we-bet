'use strict';

module.exports = {

    index: function index(req, res) {
        res.send("Hello");
    },

    users: function users(req, res) {
        var id = req.params.id;
        res.send("Returns user with id " + id + "!");
    }
};