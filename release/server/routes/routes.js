'use strict';

var GET = require('./get');
var POST = require('./post');

module.exports = {
    init: function init(app) {
        app.get('/api/', GET.index);
        app.get('/api/users/:id', GET.users);
        app.post('/api/login/', POST.login);
    }
};