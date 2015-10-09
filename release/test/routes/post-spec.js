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

describe('POST', function () {
    var POST = require(process.cwd() + '/release/server/routes/post'),
        res;

    beforeEach(function () {
        return res = new Res();
    });
});