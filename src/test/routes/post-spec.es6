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

    before(() => res = new Res());

    describe('/login', () => {
        before(() => POST.login({body: {username: "nathan", password: "nathan"}}, res));

        describe('/users:123', () => {
            it(`should respond with: "Returns user with id 123!"`, () => res.response.should.equal(true));
            it('should show function has been called', () => res.called.should.equal(true));
        });
    });

    describe('/login', () => {
        before(() => POST.login({body: {username: "nathan", password: "billy"}}, res));

        describe('/users:123', () => {
            it(`should respond with: "Returns user with id 123!"`, () => res.response.should.equal(false));
            it('should show function has been called', () => res.called.should.equal(true));
        });
    });
});