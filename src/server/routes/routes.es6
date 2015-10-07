'use strict';

var Get = require('./get');

module.exports = {
    init (app) {
        app.get('/api/', Get.index);
        app.get('/api/users/:id', Get.users);
    }
};