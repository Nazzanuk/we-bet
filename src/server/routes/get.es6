'use strict';

module.exports = {

    index (req, res) {
        res.send("Hello");
    },

    users (req, res) {
        var id = req.params.id;
        res.send(`Returns user with id ${id}!`);
    }
};

