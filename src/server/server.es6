'use strict';

var express = require('express');
var routes = require('./routes/routes');

//server setup
var app = express();
var port = process.env.PORT || 3000;
app.use(express.static('../release'));

var server = app.listen(port, () => {
    var host = server.address().address;
    console.log('Example app listening at http://%s:%s', host, port);
});

//app init
routes.init(app);