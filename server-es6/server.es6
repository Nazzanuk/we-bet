var express = require('express');
var app = express();
var port = process.env.PORT || 3000;

var routes = require('./routes/routes');

routes.init(app);

var server = app.listen(port, () => {
    var host = server.address().address;
    console.log('Example app listening at http://%s:%s', host, port);
});