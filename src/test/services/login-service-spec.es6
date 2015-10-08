//set up test syntax
var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should();

//get js file we are testing
var loginService = require(process.cwd() + '/release/server/services/login-service');

//tests
describe('loginService', () => {

    describe('loginService.isValidUser("nathan", "nathan")', () => {
        it('should respond with true', () => loginService.isValidUser("nathan", "nathan").should.equal(true));
    });

    describe('loginService.isValidUser("nathan", "billy")', () => {
        it('should respond with false', () => loginService.isValidUser("nathan","billy").should.equal(false));
    });
});