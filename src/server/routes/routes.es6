'use strict';

var Get = require('./get');

module.exports = {
    init (app) {
        app.get('/', Get.index);
        app.get('/users/:id', Get.users);
    }
};