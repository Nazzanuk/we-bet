//set up test syntax
var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should();

//internal testing functions
class Res {
    send(msg) {
        this.response = msg;
        this.called = true;
    }
}

//tests
describe('POST', () => {
    var POST = require(process.cwd() + '/release/server/routes/post'),
        res;

    beforeEach(() => res = new Res());


});