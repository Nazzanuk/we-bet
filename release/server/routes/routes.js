'use strict';

var Get = require('./get');

module.exports = {
    init: function init(app) {
        app.get('/api/', Get.index);
        app.get('/api/users/:id', Get.users);
    }
};