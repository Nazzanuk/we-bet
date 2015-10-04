var assert = require("assert");
var chai = require('chai'),
    expect = chai.expect,
    should = chai.should();

var restGet = require('../server/get');

var called, response;
var res = {
    send: function (msg) {
        response = msg;
        called = true;
    }
};

describe('get', function () {
    describe('/get/me', function () {

        restGet.people({params: {id: "me"}}, res);
        it('me!', function () {
            response.should.equal("me!");
        });

        it('should show it has been called', function () {
            called.should.equal(true);
        });
    });
});