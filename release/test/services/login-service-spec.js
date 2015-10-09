//set up test syntax
'use strict';

var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should();

//get js file we are testing
var loginService = require(process.cwd() + '/release/server/services/login-service');

//tests
describe('loginService', function () {

    describe('loginService.isValidUser("nathan", "nathan")', function () {
        it('should respond with true', function () {
            return loginService.isValidUser("nathan", "nathan").should.equal(true);
        });
    });

    describe('loginService.isValidUser("nathan", "billy")', function () {
        it('should respond with false', function () {
            return loginService.isValidUser("nathan", "billy").should.equal(false);
        });
    });
});