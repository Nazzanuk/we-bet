//set up test syntax
var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should();

//get js file we are testing

//internal testing functions
class Res {
    send(msg) {
        this.response = msg;
        this.called = true;
    }
}

//tests
describe('GET', () => {
    var GET = require(process.cwd() + '/release/server/routes/get'),
        res;

    beforeEach(() => res = new Res());

    describe('/ (index)', () => {
        beforeEach(() => GET.index({}, res));

        it('should respond with Hello', () => res.response.should.equal("Hello"));
        it('should show function has been called', () => res.called.should.equal(true));
    });

    describe('/users', () => {
        beforeEach(() => GET.users({params: {id: "123"}}, res));

        describe('/users:123', () => {
            it(`should respond with: "Returns user with id 123!"`, () => res.response.should.equal("Returns user with id 123!"));
            it('should show function has been called', () => res.called.should.equal(true));
        });
    });
});