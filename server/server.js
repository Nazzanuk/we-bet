var express = require('express');
var app = express();
var restGet = require('./get');

console.log(restGet);
app.get('/:id', restGet.people);

var port = process.env.PORT || 3000;
var server = app.listen(port, function () {
    var host = server.address().address;
    console.log('Example app listening at http://%s:%s', host, port);
});