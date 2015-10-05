//set up test syntax
var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should();

//get js file we are testing
var restGet = require('../server/get');

//internal testing functions
var called, response;
var res = {
    send: (msg) => {
        response = msg;
        called = true;
    }
};

//tests
describe('get', () => {
    describe('/get/me', () => {
        restGet.people({params: {id: "me"}}, res);

        it('should respomd with me!', () => response.should.equal("me!"));
        it('should show function has been called', () => called.should.equal(true));
    });
});