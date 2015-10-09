'use strict';

//npm dependencies
var express = require('express');
var cors = require('cors');
var bodyParser = require('body-parser');

//local dependencies
var routes = require('./routes/routes');


//server setup
var app = express();
var port = process.env.PORT || 3000;

app.use(cors());
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());
app.use(express.static('../client'));

var server = app.listen(port, () => {
    var host = server.address().address;
    console.log('Example app listening at http://%s:%s', host, port);
});

//app init
routes.init(app);

module.exports = server;