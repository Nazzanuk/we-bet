//set up test syntax
'use strict';

var _createClass = (function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ('value' in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; })();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError('Cannot call a class as a function'); } }

var assert = require("assert"),
    chai = require('chai'),
    expect = chai.expect,
    should = chai.should();

//internal testing functions

var Res = (function () {
    function Res() {
        _classCallCheck(this, Res);
    }

    //tests

    _createClass(Res, [{
        key: 'send',
        value: function send(msg) {
            this.response = msg;
            this.called = true;
        }
    }]);

    return Res;
})();

describe('GET', function () {
    var GET = require(process.cwd() + '/release/server/routes/get'),
        res;

    beforeEach(function () {
        return res = new Res();
    });

    describe('/ (index)', function () {
        beforeEach(function () {
            return GET.index({}, res);
        });

        it('should respond with Hello', function () {
            return res.response.should.equal("Hello");
        });
        it('should show function has been called', function () {
            return res.called.should.equal(true);
        });
    });

    describe('/users', function () {
        beforeEach(function () {
            return GET.users({ params: { _id: "123" } }, res);
        });

        describe('/users:123', function () {
            it('should respond with: "Returns user with id 123"', function () {
                return res.response.should.have.property('_id', '123');
            });
            it('should respond with: "Returns username"', function () {
                return res.response.should.have.property('username');
            });
            it('should show function has been called', function () {
                return res.called.should.equal(true);
            });
        });
    });

    describe('/login?username=nathan&password=nathan', function () {
        beforeEach(function () {
            return GET.login({ query: { username: "nathan", password: "nathan" } }, res);
        });

        describe('/users:123', function () {
            it('should respond with: "true"', function () {
                return res.response.should.equal(true);
            });
            it('should show function has been called', function () {
                return res.called.should.equal(true);
            });
        });
    });
});