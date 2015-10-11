'use strict';

var GET = require('./get');
var POST = require('./post');

module.exports = {
    init (app) {
        app.get('/api/', GET.index);
        //app.get('/api/users/:id', GET.users);
        app.get('/api/groups/', GET.groups);
        app.get('/api/groups/_id/:_id', GET.groups);
        app.get('/api/login/', GET.login);
        //app.get('*', (req, res) => {
        //    res.send("zz")
        //});
    }
};